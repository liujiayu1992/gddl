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
 * 王磊
 * 修改自动生成收耗存日报的方法加入【判断是否自动生成
 */
public class PublicAutoRunListener_gd  implements ServletContextListener{
	private static final JDBCcon con = new JDBCcon();
	private static final  Timer  timer=new  Timer();  
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO 自动生成方法存根  
		timer.cancel();
		con.Close();
//		System.out.println("析构");
	}
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO 自动生成方法存根  每天00:00
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8"); 
	    TimeZone.setDefault(tz); 
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		//ca.add(Calendar.SECOND, 600);//10分钟后执行
		Date tim=ca.getTime();
//		查询系统信息表中是否设置了收耗存日报自动上传
		ResultSetList rs = con.getResultSetList("select to_date(zhi,'yyyy-mm-dd hh24:mi:ss') zhi,danw,beiz,diancxxb_id from xitxxb where mingc = '自动计算收耗存' and zhuangt = 1 ");
		if(rs.next()){
//			如果设置则得到设置的上传时间
			Date AutoCreatetime = rs.getDate("zhi"); 
//			System.out.println(AutoCreatetime);
//			上传时间的变化范围
			long range = rs.getLong("danw");
//			System.out.println(range);
//			触发器的执行频度
			long period = rs.getLong("beiz");
//			System.out.println(period);
//			电厂ID
			long dcid = rs.getLong("diancxxb_id");
//			System.out.println(dcid);
//			监听时间判断
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
