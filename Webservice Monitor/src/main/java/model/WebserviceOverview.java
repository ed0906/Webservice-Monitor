package model;


public class WebserviceOverview extends Webservice{

	private MetricSet metrics;
	
	public WebserviceOverview(String name, String url, MetricSet metrics) {
		super(name, url);
		this.metrics = metrics;
	}
	
	public MetricSet getMetrics(){
		return metrics;
	}
	
	@Override
	public boolean equals(Object object){
		return super.equals(object);
	}

}
