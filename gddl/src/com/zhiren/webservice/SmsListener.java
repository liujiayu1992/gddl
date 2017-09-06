package com.zhiren.webservice;

/**
 * 作者：陈环红
 * 时间：2011-06-07
 * 描述：增加发送短信的监听服务
 * 		需要在Web.xml中增加如下配置
 *     <listener>
 *	    <listener-class>com.zhiren.webservice.SmsListener</listener-class>
 *     </listener>
 */	
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zhiren.common.MainGlobal;
import com.zhiren.sms.SmsManager;

public class SmsListener  implements ServletContextListener{
	
	private static final  Timer  timer=new  Timer();  
	private long count = 0;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		timer.cancel();
	}
	public void contextInitialized(ServletContextEvent arg0) {
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8"); 
	    TimeZone.setDefault(tz); 
	    
		System.out.println("短信监听服务启动!");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MINUTE,2);//2分钟后执行
		Date tim=ca.getTime();
		int Shij=Integer.parseInt(MainGlobal.getXitxx_item("短信", "短信轮询分钟", "0", "5"));
		long period = 1000 * 60*Shij;//1分钟检查执行一次
		timer.schedule(new SmsTask(), tim,period);
	}
	
	class SmsTask extends TimerTask{
		public  void  run()  { 
//			计数器
			if(count ==  4294967295L){
				count = 1;
			}else{
				count += 1;
			}
			System.out.println("短信任务:第 " + count +" 次"+ new Date());
			SmsManager.SendSms();//   发短信内容
			System.out.println("短信任务结束!");
		}
	}
}