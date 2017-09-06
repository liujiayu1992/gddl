package com.zhiren.webservice.dtgsinterface;

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
import com.zhiren.gs.bjdt.diaoygl.ReaddcDayFile;
import com.zhiren.gs.bjdt.diaoygl.UploadData;
public class ServerListener_dtgs  implements ServletContextListener{
	private static final  Timer  timer=new  Timer();  
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO �Զ����ɷ������  
		System.out.println("����");
	}
	public void contextInitialized(ServletContextEvent arg0) {
		Calendar cal=Calendar.getInstance();
		//��������ʱ��ѯ����
		String sql = "select dingsqb.renwmc,dingsqb.dingsqsj,dingsqb.dingsqjg \n"
				+ "from dingsqb where dingsqb.zhuangt=1 order by id desc \n";
		JDBCcon con=new JDBCcon();
		ResultSet rs=con.getResultSet(sql);
		try{
			while(rs.next()){
				String renwmc=rs.getString("renwmc");
				String dingsqsj=rs.getString("dingsqsj");
				String dingsqjg=rs.getString("dingsqjg");
				String[] Artim1=dingsqsj.split(":");
				cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DAY_OF_MONTH), Integer.parseInt(Artim1[0]), Integer.parseInt(Artim1[1]), Integer.parseInt(Artim1[2]));
				Date tim1=cal.getTime();
				
//				if(tim1.getTime()<new Date().getTime()){//(���С�ڷ���������ʱ�䣬����ִ��)���ϵͳ����ִ��ʱ����ڷ���������ʱ����Ϊ����ִ�У����С��������ִ��
//					tim1=DateUtil.AddDate(tim1, 1, DateUtil.AddType_intDay);
//				}
				
//				cal.setTime(new Date());
//				cal.add(Calendar.SECOND, 20);//10���Ӻ�ִ��
//				tim1 = cal.getTime();
				
				if(renwmc.equals("shouhcrbsbjt")){//�պĴ��ձ��ϱ�����
					timer.schedule(new ShouhcrbUpLoadTask(), tim1,Long.parseLong(dingsqjg));//300000
				
				}else if(renwmc.equals("readdayfile")){//��ȡ�糧�ϱ����պĴ������ļ�
					timer.schedule(new ReadDayFileTask(), tim1,Long.parseLong(dingsqjg));//86400000
				
				}else{//���ݽӿ�����
					timer.schedule(new RequestTask(renwmc), tim1,Long.parseLong(dingsqjg));//86400000
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
}

class RequestTask extends TimerTask{//���ƹ�˾�������˶�ʱ��������
	InterFace_dtgs req = new  InterFace_dtgs();
	String renw ="";
	public RequestTask(String renw){
		this.renw=renw;
	}
	public  void  run()  {  
//		System.out.println(renw);
		req.getRenw(renw);
	}  	
}

class ShouhcrbUpLoadTask extends TimerTask{//���ƹ�˾�������˶�ʱ������������Ƽ����ϱ����ƹ��ʸ������պĴ��ձ����ݣ�
	UploadData ud = new  UploadData();
//	String renw ="";
	int renwcs = 0;//ִ���������
	public ShouhcrbUpLoadTask(){
		this.renwcs = 1;
	}
	public  void  run()  {  
		boolean renwzxzt = false;//����ִ��״̬
		Date date = new Date();
	    Calendar rightNow = Calendar.getInstance();
	    rightNow.setTime(date);
//	    Date Yesterday = MainGlobal.getYesterday(date);
	    
	    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
	    int min = rightNow.get(Calendar.MINUTE);
	    
		if(hour==9 && (min >= 25 && min <=35)){
			ud.ShangcTXTFile(false);
			renwzxzt = true;
		}
		System.out.println("��"+(this.renwcs++)+"��ִ���ձ��ϴ�����ʱ�䣺"+hour+":"+min+"��ִ��״̬��"+renwzxzt);
		renwzxzt = false;
	}
}
	
class ReadDayFileTask extends TimerTask{//���ƹ�˾�������˶�ʱ��������(��ȡ�����ϱ����պĴ��ձ��ļ�����)
	ReaddcDayFile rd = new  ReaddcDayFile();
//		String renw ="";
	public ReadDayFileTask(){
	}
	public  void  run()  {  
//		rd.readFileDayData();
	} 
}

