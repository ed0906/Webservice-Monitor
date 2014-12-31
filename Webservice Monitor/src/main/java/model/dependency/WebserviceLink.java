package model.dependency;

import model.Webservice;

public class WebserviceLink {
	private Webservice service1;
	private Webservice service2;
	private LinkSeverity severity;
	
	public WebserviceLink(Webservice service1, Webservice service2, LinkSeverity severity){
		this.service1 = service1;
		this.service2 = service2;
		this.severity = severity;
	}

	public Webservice getService1() {
		return service1;
	}

	public Webservice getService2() {
		return service2;
	}

	public LinkSeverity getSeverity() {
		return severity;
	}
}
