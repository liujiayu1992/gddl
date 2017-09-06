package com.zhiren.webservice;

/**
 * ���ߣ��»���
 * ʱ�䣺2011-06-07
 * ���������ӷ��Ͷ��ŵļ�������
 * 		��Ҫ��Web.xml��������������
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
	    
		System.out.println("���ż�����������!");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MINUTE,2);//2���Ӻ�ִ��
		Date tim=ca.getTime();
		int Shij=Integer.parseInt(MainGlobal.getXitxx_item("����", "������ѯ����", "0", "5"));
		long period = 1000 * 60*Shij;//1���Ӽ��ִ��һ��
		timer.schedule(new SmsTask(), tim,period);
	}
	
	class SmsTask extends TimerTask{
		public  void  run()  { 
//			������
			if(count ==  4294967295L){
				count = 1;
			}else{
				count += 1;
			}
			System.out.println("��������:�� " + count +" ��"+ new Date());
			SmsManager.SendSms();//   ����������
			System.out.println("�����������!");
		}
	}
}