package model;


public class WebserviceMetricUpdate extends Webservice{

	private MetricSet metrics;
	
	public WebserviceMetricUpdate(String name, String url, MetricSet metrics) {
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
