package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {

	public static void info(String msg) {
		System.out.println(getDate() + " | INFO: " + msg);
	}
	
	public static void warning(String msg) {
		System.out.println(getDate() + " | WARN: " + msg);
	}
	
	public static void warning(String msg, Throwable t) {
		System.out.println(getDate() + " | WARN: " + msg);
		t.printStackTrace();
	}
	
	public static void error(String msg) {
		System.out.println(getDate() + " | ERROR: " + msg);
	}
	
	public static void error(String msg, Throwable t) {
		System.out.println(getDate() + " | ERROR: " + msg);
		t.printStackTrace();
	}
	
	private static String getDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
}
