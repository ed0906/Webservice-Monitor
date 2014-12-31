package api;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.MetricSet;
import model.Webservice;
import model.WebserviceMetricUpdate;
import storage.StorageClient;
import util.Logger;

public class Scheduler {
	
	private final static int DELAY_SECONDS_UPDATE_TASK = 60;
	private final static int DELAY_DAYS_CLEANUP_TASK = 1;
	private final static int METRICS_LIFETIME_DAYS = 7;

	private static ScheduledExecutorService scheduler;
	
	public Scheduler() {
		if(scheduler == null){
			scheduler = Executors.newScheduledThreadPool(1);
		}
	}
	
	public void start() throws SQLException, Exception {
		scheduler.scheduleWithFixedDelay(getRunnableUpdateTask(), 5l, DELAY_SECONDS_UPDATE_TASK, TimeUnit.SECONDS);
		scheduler.scheduleWithFixedDelay(getRunnableCleanupTask(), 0l, DELAY_DAYS_CLEANUP_TASK, TimeUnit.DAYS);
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
					StorageClient storage = new StorageClient();
					try{
						List<WebserviceMetricUpdate> services = api.getWebserviceList();
						for(Webservice service : services){
							MetricSet metrics = api.getUpdate(service.getName());
							storage.save(service.getName(), metrics);
						}
					}catch(Exception e){
						Logger.error("Scheduled update task failure", e);
					}
				}
		};
	}
	
	private static Runnable getRunnableCleanupTask() {
		return new Runnable() {
			@Override
			public void run() {
				Logger.info("Running cleanup task");
				StorageClient storage = new StorageClient();
				try {
					storage.deleteMetricsOlderThan(METRICS_LIFETIME_DAYS);
				} catch (Exception e) {
					Logger.error("Scheduled cleanup task failure", e);
				}
			}
			
		};
	}
}
