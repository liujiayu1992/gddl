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
		// TODO 自动生成方法存根  
		System.out.println("析构");
	}
	public void contextInitialized(ServletContextEvent arg0) {
		Calendar cal=Calendar.getInstance();
		//服务器定时轮询请求
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
				
//				if(tim1.getTime()<new Date().getTime()){//(如果小于服务器启动时间，明天执行)如果系统设置执行时间大于服务器启动时间则为今日执行，如果小于则明天执行
//					tim1=DateUtil.AddDate(tim1, 1, DateUtil.AddType_intDay);
//				}
				
//				cal.setTime(new Date());
//				cal.add(Calendar.SECOND, 20);//10分钟后执行
//				tim1 = cal.getTime();
				
				if(renwmc.equals("shouhcrbsbjt")){//收耗存日报上报集团
					timer.schedule(new ShouhcrbUpLoadTask(), tim1,Long.parseLong(dingsqjg));//300000
				
				}else if(renwmc.equals("readdayfile")){//收取电厂上报的收耗存数据文件
					timer.schedule(new ReadDayFileTask(), tim1,Long.parseLong(dingsqjg));//86400000
				
				}else{//数据接口任务
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

class RequestTask extends TimerTask{//大唐公司服务器端定时调用任务
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

class ShouhcrbUpLoadTask extends TimerTask{//大唐公司服务器端定时调用任务（向大唐集团上报大唐国际各厂的收耗存日报数据）
	UploadData ud = new  UploadData();
//	String renw ="";
	int renwcs = 0;//执行任务次数
	public ShouhcrbUpLoadTask(){
		this.renwcs = 1;
	}
	public  void  run()  {  
		boolean renwzxzt = false;//任务执行状态
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
		System.out.println("第"+(this.renwcs++)+"次执行日报上传任务；时间："+hour+":"+min+"；执行状态："+renwzxzt);
		renwzxzt = false;
	}
}
	
class ReadDayFileTask extends TimerTask{//大唐公司服务器端定时调用任务(收取各厂上报的收耗存日报文件数据)
	ReaddcDayFile rd = new  ReaddcDayFile();
//		String renw ="";
	public ReadDayFileTask(){
	}
	public  void  run()  {  
//		rd.readFileDayData();
	} 
}

