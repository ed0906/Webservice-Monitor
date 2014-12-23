package api;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import model.Webservice;

public class Scheduler {

	private static ScheduledExecutorService scheduler;
	
	public Scheduler() {
		if(scheduler == null){
			scheduler = Executors.newScheduledThreadPool(1);
		}
	}
	
	public void start() throws SQLException, Exception {
		MonitorAPI api = new MonitorAPI();
		List<Webservice> services = api.getWebserviceList();
		
	}
}
