package model;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class Webservice {

	private String name;
	private String url;
	
	public Webservice(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(this, object);
	}
	
	@Override
	public String toString() {
		return "name:" + this.name + ",url:" + this.url;
	}
}
