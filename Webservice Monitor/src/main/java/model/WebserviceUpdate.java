package model;


public class WebserviceUpdate extends Webservice{

	private MetricSet metrics;
	
	public WebserviceUpdate(String name, String url, MetricSet metrics) {
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
