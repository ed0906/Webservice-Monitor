package runner;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import util.Logger;

public class WebserviceRunner {

	private Server server;
	private int port;
	private String host;

	public WebserviceRunner(String host, int port) {
		this.server = new Server(port);
		this.host = host;
		this.port = port;
	}

	public String getWebXml() {
		return "WebRoot/WEB-INF/web.xml";
	}

	public String getResourceBase() {
		return "./";
	}
	
	public String getHostAndPort() {
		return host + ":" + port;
	}

	public void start() throws Exception {
		HandlerCollection handlers = new HandlerCollection();
		WebAppContext handler = new WebAppContext();
		handler.setResourceBase(getResourceBase());

		final String webXmlAddress = getWebXml();
		final File webXml = new File(webXmlAddress);
		if (!webXml.exists()) {
			throw new RuntimeException("web.xml not found at: " + webXml.getAbsolutePath());
		}

		handler.setDescriptor(webXmlAddress);
		handler.setContextPath("/");
		handler.setClassLoader(Thread.currentThread().getContextClassLoader());

		handlers.addHandler(handler);

		server.setHandler(handlers);
		server.start();
		Logger.info("API started... at 'http://" + getHostAndPort() + "'");
	}
	
	public void stop() throws Exception {
		server.stop();
		Logger.info("API stopped");
		
	}
	
	public static void main(String[] args) throws Exception {
		new WebserviceRunner("localhost", 8315).start();
	}
}