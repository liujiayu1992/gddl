package com.zhiren.sms;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ZDTSmsListener  implements ServletContextListener{
	private static final  Timer  timer=new  Timer();  
	public void contextDestroyed(ServletContextEvent arg0) {
		timer.cancel();
	}
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("���ŷ����������!");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.SECOND, 60);//1���Ӻ�ִ��
		Date tim=ca.getTime();
		timer.schedule(new SmsTask(), tim,60000);
	}
	class SmsTask extends TimerTask{
		Sendms sm=new Sendms();
		public  void  run()  {  
			sm.doSendTask();
		}
	}
}