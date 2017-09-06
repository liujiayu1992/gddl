package com.zhiren.webservice;/*
 * 日期：2011-10-17
 * 作者：Qiuzw
 * 描述：补充上传文件（主要是盘点图片）的方法
* */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysInfo;
import com.zhiren.webservice.InterFac;

public class ShujscListener  implements ServletContextListener{
	private static final  Timer  timer=new  Timer();
	private  static long  cal =0;
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("析构");
		jies();
		timer.cancel();
	}
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("webservice定时服务启动!");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.SECOND, 6);//10分钟后执行
		Date tim=ca.getTime();
		timer.schedule(new UploadDataTask(), tim,60*1000);//86400000
	}
	class UploadDataTask extends TimerTask{
		InterFac sender=null;
		SysInfo xitxx=new SysInfo();
		public  void  run()  {
			if(sender==null){
				sender = new  InterFac();
				jies();//解决突然挂机问题
			}
			String fenz="0";
			fenz=MainGlobal.getXitxx_item("接口用","定时器间隔(分钟)", "197", "20");
			if(Long.parseLong(fenz)==0){
				return;
			}
			cal+=1;
			if(cal<Long.parseLong(fenz)){
				return;
			}
			cal=0;
			long a=new Date().getTime();
			System.out.println( new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()) +"开始执行" );

			sender.requestall("-1","","");
			sender.requestFile("-1", "", "", "");
			long b=new Date().getTime();
			System.out.println("用时:"+(b-a)/1000+"" );
		}
	}
	public void excute(){
		InterFac sender = new  InterFac();
		sender.requestall("-1","","");
	}
	private void jies(){//对 0与-1的解锁
		JDBCcon con=new JDBCcon();
		String sql="update jiekrwb\n" +
				"set jiekrwb.zhixzt=0\n" +
				"where jiekrwb.zhixzt=2 and jiekrwb.zhixbz is null";
		con.getResultSet(sql);
		sql=
				"update jiekrwb\n" +
						"set jiekrwb.zhixzt=-1\n" +
						"where jiekrwb.zhixzt=2 and jiekrwb.zhixbz  is not null";
		con.getResultSet(sql);
		con.Close();
	}
}