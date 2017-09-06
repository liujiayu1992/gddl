package com.zhiren.webservice;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
public class ServerListener  implements ServletContextListener{
	private static final  Timer  timer=new  Timer();  
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO �Զ����ɷ������  
		System.out.println("����");
	}
	public void contextInitialized(ServletContextEvent arg0) {
		Calendar cal=Calendar.getInstance();
		//��������ʱ��ѯ����
		String sql=
			"select dianczhb.diancxxb_id,jiekzhb.id,dingsqb.renwmc,dingsqb.dingsqsj,dingsqb.dingsqjg\n" +
			"from jiekzhb,dianczhb,dingsqb\n" + 
			"where jiekzhb.id=dianczhb.jiekzhb_id and dingsqb.zhuangt=1 and endpointaddress is not null\n" + 
			"";
		JDBCcon con=new JDBCcon();
		ResultSet rs=con.getResultSet(sql);
		try{
			while(rs.next()){
				String diancxxb_id= rs.getString("diancxxb_id");
				String zhuanghb_id= rs.getString("id");
				String renwmc=rs.getString("renwmc");
				String dingsqsj=rs.getString("dingsqsj");
				String dingsqjg=rs.getString("dingsqjg");
				String[] Artim1=dingsqsj.split(":");
				cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH), Integer.parseInt(Artim1[0]), Integer.parseInt(Artim1[1]), Integer.parseInt(Artim1[2]));
				Date tim1=cal.getTime();
				if(tim1.getTime()<new Date().getTime()){//(���С�ڷ���������ʱ�䣬����ִ��)���ϵͳ����ִ��ʱ����ڷ���������ʱ����Ϊ����ִ�У����С��������ִ��
					tim1=DateUtil.AddDate(tim1, 1, DateUtil.AddType_intDay);
				}
				timer.schedule(new RequestTask(zhuanghb_id,diancxxb_id,renwmc), tim1,Long.parseLong(dingsqjg));//86400000
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	class RequestTask extends TimerTask{//�е�Ͷ�������˶�ʱ��������
		InterCom req = new  InterCom();
		String renw ="";
		String diancxxb_id="";
		String zhuangh_id="";
		public RequestTask(String zhuangh_id,String diancxxb_id,String renw){
			this.renw=renw;
			this.diancxxb_id=diancxxb_id;
			this.zhuangh_id=zhuangh_id;
		}
		public  void  run()  {  
			System.out.println(renw);
			req.getRenw(zhuangh_id, diancxxb_id, renw,"","");
		}  	
	}
}
