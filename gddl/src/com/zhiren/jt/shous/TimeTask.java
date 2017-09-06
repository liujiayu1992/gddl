package com.zhiren.jt.shous;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class TimeTask {

	private static final  Timer  timer=new  Timer();  
	 
	//��ʱ��������ȴ�30���ʼִ��
	private static final int DELAY1 = 30000;
	
	// ��ʱ��ִ�к�ÿ���300��ִ��һ��
	private static final int INTERVAL1 = 3000000;
	
	public  void  Timer_start()  {  
		ReadDayFileTask rdf = new  ReadDayFileTask();
        timer.scheduleAtFixedRate( rdf, DELAY1, INTERVAL1); 
        
        ReadMonthFileTask rmf = new  ReadMonthFileTask();
        timer.scheduleAtFixedRate( rmf, DELAY1, INTERVAL1);
   		
        //timer.cancel();//ʹ����������˳�����
    }           
}

class ReadDayFileTask extends TimerTask{

	Shous ss = new Shous();
	public  void  run()  {  
//		ss.readFileDayData();//�ձ�������ȡ
	}  	
}
class ReadMonthFileTask extends TimerTask{

	Shous ss = new Shous();
	public  void  run()  {  
		
		Date date = new Date();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DATE);

	    if(day>=3 && day<=15){
//	    	ss.readFileMonthData(year,month);//�±�������ȡ
	    }
	}  	
}



