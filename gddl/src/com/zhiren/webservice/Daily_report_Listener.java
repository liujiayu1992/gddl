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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.diaoygl.AutoCreateDaily_Report_gd;
/*
 * 作者：夏峥
 * 修改时间：2011-11-23
 * 适用范围：国电电力及其下属单位
 * 描述：修正接口调用的方法。
 */
/*
 * 作者：夏峥
 * 时间：2012-06-27
 * 适用范围：邯郸电厂
 * 描述：为邯郸电厂增加相应的保存方法和获取方法
 */	
public class Daily_report_Listener implements ServletContextListener {

	private static final JDBCcon con = new JDBCcon();
	private static final  Timer  timer=new  Timer();  
	private long count = 0;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO 自动生成方法存根  
		timer.cancel();
		con.Close();
		System.out.println("析构");
	}
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO 自动生成方法存根  每天00:00
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8"); 
	    TimeZone.setDefault(tz); 
		Calendar ca = Calendar.getInstance();
		Date curd = new Date();
//		ca.setTime(new Date());
		Date lastd = curd;//DateUtil.AddDate(curd, -1, DateUtil.AddType_intDay);
//		从系统信息表中读取日报每日生成时间点，如果没有则默认为每日凌晨1点
		int Shij=Integer.parseInt(MainGlobal.getXitxx_item("收耗存日报", "日报生成时间", "0", "01"));
		ca.set(DateUtil.getYear(lastd), DateUtil.getMonth(lastd)-1, DateUtil.getDay(lastd),	Shij, 00, 00);
		Date tim=ca.getTime();
		if(tim.getTime()<System.currentTimeMillis()){
			tim = DateUtil.AddDate(tim, 1, DateUtil.AddType_intDay);
		}
		long period = 1000 * 60 * 60 *24;
//		临时设置为60秒执行一次
//		period=5000;
		System.out.println("日报 listener start : " + tim);
		timer.schedule(new AutoInvTask(), tim.getTime() - curd.getTime(), period);//86400000
//		timer.schedule(new AutoInvTask(), 1, period);
	}
	class AutoInvTask extends TimerTask{
		
		public void run() {
//			计数器
			if(count ==  4294967295L){
				count = 1;
			}else{
				count += 1;
			}
			TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8"); 
		    TimeZone.setDefault(tz); 
			Calendar ca = Calendar.getInstance();
			Date curd = new Date();
			Date lastd = DateUtil.AddDate(curd, -1, DateUtil.AddType_intDay);
			ca.set(DateUtil.getYear(lastd), DateUtil.getMonth(lastd)-1, DateUtil.getDay(lastd), 23, 59, 59);
			System.out.println("日报  Listener Run:第 " + count +" 次"+ new Date());
			con.JDBCBegin(1);
			try {
//				从系统信息表中读取参与自动生成日报的单位信息，如果没有则为空
				ResultSetList rsl=con.getResultSetList("SELECT ID,mingc FROM DIANCXXB WHERE ID IN ("+MainGlobal.getXitxx_item("收耗存日报", "日报参与单位标识", "0", "-1")+")");
				
				while(rsl.next()){
//					从系统信息表中读取自动生成日报的时间与当前时间的时间差值，默认为当前日期的前一日
					java.sql.Date rq = java.sql.Date.valueOf(DateUtil.FormatDate(DateUtil.AddDate(new Date(), Integer.parseInt(MainGlobal.getXitxx_item("收耗存日报", "日报生成时间差", "0", "-1")), DateUtil.AddType_intDay)));
					AutoCreateDaily_Report_gd RP=new AutoCreateDaily_Report_gd();
					String rbb="";
					String fcb="";
					if(MainGlobal.getXitxx_item("收耗存日报", "是否按运输方式生成日报", "0", "否").equals("否")){
						rbb=RP.CreateRBB(con, rsl.getLong("ID"), rq);
						fcb=RP.CreateFCB(con, rsl.getLong("ID"), rq);
					}else{
						rbb=RP.CreateRBB(con, rsl.getLong("ID"), rq);
						fcb=RP.CreateFCB_HD(con, rsl.getLong("ID"), rq);
					}

					String Smsg="";
					if(rbb.length()>0){
						Smsg+=rbb+"<br>";
					}
					if(fcb.length()>0){
						Smsg+=fcb+"<br>";
					}
					
					if(Smsg.length()>10){
						System.out.println(rsl.getString("mingc"));
						System.out.println(Smsg);
					}else{
						System.out.println(rsl.getString("mingc")+"生成成功");
					}
				}
			}finally{
				System.out.println("日报  Listener Run End:第 " + count +" 次"+ new Date());
			}
			
		}
		
	}
	
}
