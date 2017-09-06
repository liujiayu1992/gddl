package com.zhiren.webservice;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.diaoygl.AutoCreateShouhcrb_gd;
/*
 * 2009-05-14
 * ����
 * �޸��Զ������պĴ��ձ��ķ������롾�ж��Ƿ��Զ�����
 */
public class PublicAutoRunListener_gd  implements ServletContextListener{
	private static final JDBCcon con = new JDBCcon();
	private static final  Timer  timer=new  Timer();  
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO �Զ����ɷ������  
		timer.cancel();
		con.Close();
//		System.out.println("����");
	}
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO �Զ����ɷ������  ÿ��00:00
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8"); 
	    TimeZone.setDefault(tz); 
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		//ca.add(Calendar.SECOND, 600);//10���Ӻ�ִ��
		Date tim=ca.getTime();
//		��ѯϵͳ��Ϣ�����Ƿ��������պĴ��ձ��Զ��ϴ�
		ResultSetList rs = con.getResultSetList("select to_date(zhi,'yyyy-mm-dd hh24:mi:ss') zhi,danw,beiz,diancxxb_id from xitxxb where mingc = '�Զ������պĴ�' and zhuangt = 1 ");
		if(rs.next()){
//			���������õ����õ��ϴ�ʱ��
			Date AutoCreatetime = rs.getDate("zhi"); 
//			System.out.println(AutoCreatetime);
//			�ϴ�ʱ��ı仯��Χ
			long range = rs.getLong("danw");
//			System.out.println(range);
//			��������ִ��Ƶ��
			long period = rs.getLong("beiz");
//			System.out.println(period);
//			�糧ID
			long dcid = rs.getLong("diancxxb_id");
//			System.out.println(dcid);
//			����ʱ���ж�
			timer.schedule(new AutoCreateShouhchjTask(AutoCreatetime,range,dcid), tim, period);//86400000
		}
		rs.close();
		
	}
	class AutoCreateShouhchjTask extends TimerTask{
		private Date _time;
		private long _range;
		private long _diancxxb_id;
		public AutoCreateShouhchjTask(Date time, long range, long dcid){
			this._time = time;
			this._range = range;
			this._diancxxb_id = dcid;
		}
		public  void  run()  {
			TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8"); 
		    TimeZone.setDefault(tz); 
			Date curDate = new Date();
//			System.out.println("Run:");
//			System.out.println("curDate:"+curDate);
//			System.out.println("setDate:"+_time);
			int curtime = DateUtil.getHour(curDate)*60 + DateUtil.getMinutes(curDate);
			int time = DateUtil.getHour(_time)*60 + DateUtil.getMinutes(_time);
//			System.out.print("Curtime:"+curtime + "     time:" + time);
			if(Math.abs(curtime - time) <= _range) {
				AutoCreateShouhcrb_gd.Create(con, _diancxxb_id, DateUtil.AddDate(curDate, -1, DateUtil.AddType_intDay), true);
				InterFac_dt sender = new InterFac_dt();
				sender.request("shouhcrbb");
			}
		}  	
	}
}
