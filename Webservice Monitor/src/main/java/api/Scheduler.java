package api;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.MetricSet;
import model.Webservice;
import model.WebserviceOverview;
import util.Logger;
import util.WebserviceStorageManager;

public class Scheduler {
	
	private final static int DELAY_SECONDS = 60;

	private static ScheduledExecutorService scheduler;
	
	public Scheduler() {
		if(scheduler == null){
			scheduler = Executors.newScheduledThreadPool(1);
		}
	}
	
	public void start() throws SQLException, Exception {
		scheduler.scheduleWithFixedDelay(getRunnableUpdateTask(), 5l, DELAY_SECONDS, TimeUnit.SECONDS);
	}
	
	public void stop(){
		scheduler.shutdownNow();
	}
	
	private static Runnable getRunnableUpdateTask() {
		return new Runnable(){
				@Override
				public void run() {
					Logger.info("Running scheduled update");
					MonitorAPI api = new MonitorAPI();
					try{
						WebserviceStorageManager storage = new WebserviceStorageManager();
						List<WebserviceOverview> services = api.getWebserviceList();
						for(Webservice service : services){
							MetricSet metrics = api.getUpdate(service.getName());
							storage.save(service.getName(), metrics);
						}
					}catch(Exception e){
						Logger.error("Scheduled task failure", e);
					}
				}
		};
	}
}
