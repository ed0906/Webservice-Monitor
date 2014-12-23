package model;

import org.apache.commons.lang3.builder.EqualsBuilder;


public class MetricSet {
	private int responseCode;
	private long responseTime;
	private long date;
	
	public MetricSet(int responseCode, long responseTime, long date){
		this.responseCode = responseCode;
		this.responseTime = responseTime;
		this.date = date;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public long getDate() {
		return date;
	}
	
	@Override
	public boolean equals(Object object){
		return EqualsBuilder.reflectionEquals(this, object);
	}
}
