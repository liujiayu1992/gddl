package com.zhiren.jt.shous;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
	 
	public void contextInitialized(ServletContextEvent event) {
		
		TimeTask ustt = new TimeTask ();  
		ustt.Timer_start();
		System.out.println("/****************¼àÌýÒÑÆô¶¯£¡"+(new Date())+"*******************/");
	}

	public void contextDestroyed(ServletContextEvent event) {
	  
	}
	
}

