package model;

import java.util.List;

public class WebserviceSummary {

	private Webservice service;
	private MetricSet currentMetrics;
	private List<String> dependantSystems;
	
	public WebserviceSummary(Webservice service, MetricSet currentMetrics, List<String> dependantSystems) {
		this.service = service;
		this.currentMetrics = currentMetrics;
		this.dependantSystems = dependantSystems;
	}

	public Webservice getService() {
		return service;
	}

	public MetricSet getCurrentMetrics() {
		return currentMetrics;
	}

	public List<String> getDependantSystems() {
		return dependantSystems;
	}

	public void setService(Webservice service) {
		this.service = service;
	}

	public void setCurrentMetrics(MetricSet currentMetrics) {
		this.currentMetrics = currentMetrics;
	}

	public void setDependantSystems(List<String> dependantSystems) {
		this.dependantSystems = dependantSystems;
	}
}
