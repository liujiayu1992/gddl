package com.zhiren.webservice;/*
 * ���ڣ�2011-10-17
 * ���ߣ�Qiuzw
 * �����������ϴ��ļ�����Ҫ���̵�ͼƬ���ķ���
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
		System.out.println("����");
		jies();
		timer.cancel();
	}
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("webservice��ʱ��������!");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.SECOND, 6);//10���Ӻ�ִ��
		Date tim=ca.getTime();
		timer.schedule(new UploadDataTask(), tim,60*1000);//86400000
	}
	class UploadDataTask extends TimerTask{
		InterFac sender=null;
		SysInfo xitxx=new SysInfo();
		public  void  run()  {
			if(sender==null){
				sender = new  InterFac();
				jies();//���ͻȻ�һ�����
			}
			String fenz="0";
			fenz=MainGlobal.getXitxx_item("�ӿ���","��ʱ�����(����)", "197", "20");
			if(Long.parseLong(fenz)==0){
				return;
			}
			cal+=1;
			if(cal<Long.parseLong(fenz)){
				return;
			}
			cal=0;
			long a=new Date().getTime();
			System.out.println( new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��").format(new Date()) +"��ʼִ��" );

			sender.requestall("-1","","");
			sender.requestFile("-1", "", "", "");
			long b=new Date().getTime();
			System.out.println("��ʱ:"+(b-a)/1000+"" );
		}
	}
	public void excute(){
		InterFac sender = new  InterFac();
		sender.requestall("-1","","");
	}
	private void jies(){//�� 0��-1�Ľ���
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