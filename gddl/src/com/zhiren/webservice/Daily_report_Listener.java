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
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-23
 * ���÷�Χ�������������������λ
 * �����������ӿڵ��õķ�����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-06-27
 * ���÷�Χ�������糧
 * ������Ϊ�����糧������Ӧ�ı��淽���ͻ�ȡ����
 */	
public class Daily_report_Listener implements ServletContextListener {

	private static final JDBCcon con = new JDBCcon();
	private static final  Timer  timer=new  Timer();  
	private long count = 0;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO �Զ����ɷ������  
		timer.cancel();
		con.Close();
		System.out.println("����");
	}
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO �Զ����ɷ������  ÿ��00:00
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8"); 
	    TimeZone.setDefault(tz); 
		Calendar ca = Calendar.getInstance();
		Date curd = new Date();
//		ca.setTime(new Date());
		Date lastd = curd;//DateUtil.AddDate(curd, -1, DateUtil.AddType_intDay);
//		��ϵͳ��Ϣ���ж�ȡ�ձ�ÿ������ʱ��㣬���û����Ĭ��Ϊÿ���賿1��
		int Shij=Integer.parseInt(MainGlobal.getXitxx_item("�պĴ��ձ�", "�ձ�����ʱ��", "0", "01"));
		ca.set(DateUtil.getYear(lastd), DateUtil.getMonth(lastd)-1, DateUtil.getDay(lastd),	Shij, 00, 00);
		Date tim=ca.getTime();
		if(tim.getTime()<System.currentTimeMillis()){
			tim = DateUtil.AddDate(tim, 1, DateUtil.AddType_intDay);
		}
		long period = 1000 * 60 * 60 *24;
//		��ʱ����Ϊ60��ִ��һ��
//		period=5000;
		System.out.println("�ձ� listener start : " + tim);
		timer.schedule(new AutoInvTask(), tim.getTime() - curd.getTime(), period);//86400000
//		timer.schedule(new AutoInvTask(), 1, period);
	}
	class AutoInvTask extends TimerTask{
		
		public void run() {
//			������
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
			System.out.println("�ձ�  Listener Run:�� " + count +" ��"+ new Date());
			con.JDBCBegin(1);
			try {
//				��ϵͳ��Ϣ���ж�ȡ�����Զ������ձ��ĵ�λ��Ϣ�����û����Ϊ��
				ResultSetList rsl=con.getResultSetList("SELECT ID,mingc FROM DIANCXXB WHERE ID IN ("+MainGlobal.getXitxx_item("�պĴ��ձ�", "�ձ����뵥λ��ʶ", "0", "-1")+")");
				
				while(rsl.next()){
//					��ϵͳ��Ϣ���ж�ȡ�Զ������ձ���ʱ���뵱ǰʱ���ʱ���ֵ��Ĭ��Ϊ��ǰ���ڵ�ǰһ��
					java.sql.Date rq = java.sql.Date.valueOf(DateUtil.FormatDate(DateUtil.AddDate(new Date(), Integer.parseInt(MainGlobal.getXitxx_item("�պĴ��ձ�", "�ձ�����ʱ���", "0", "-1")), DateUtil.AddType_intDay)));
					AutoCreateDaily_Report_gd RP=new AutoCreateDaily_Report_gd();
					String rbb="";
					String fcb="";
					if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�Ƿ����䷽ʽ�����ձ�", "0", "��").equals("��")){
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
						System.out.println(rsl.getString("mingc")+"���ɳɹ�");
					}
				}
			}finally{
				System.out.println("�ձ�  Listener Run End:�� " + count +" ��"+ new Date());
			}
			
		}
		
	}
	
}
