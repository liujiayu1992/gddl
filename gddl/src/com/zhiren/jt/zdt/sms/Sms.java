/* 
* 时间：2009-03-23 
* 作者： chh 
* 修改内容： 1.sql出错时,关闭连接。
* 			2.newAutoExcuteTask 中的sql错误
*/ 
package com.zhiren.jt.zdt.sms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.huawei.api.SMEntryEx;
import com.huawei.api.SMException;
import com.huawei.api.bean.SMExBean;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

public class Sms {
	public static final String LEIB_DAY="每日";
	public static final String LEIB_WEEK="每周";
	public static final String LEIB_MONTH="每月";
	
	public static final String CYCLE_MINUTE="分钟";
	public static final String CYCLE_HOUR="小时";
	
	public static final long DUNAXLB_TEXT=0;//直接发送文本的短信类型
	public static final long DUNAXLB_FUNFCTION=1;//按函数计返回结果的短信类型
	
	public String getIniSMS(){
		//初始化，登录短信网关
		System.out.println(System.getProperty("user.dir"));
		SMEntryEx.init("192.168.1.52","sa","sa2000");
	  	try {
			SMEntryEx.logIn("燃料管理","123456");
		} catch (SMException e) {
			return e.getErrorDesc();
		}
		return "";
	}
	
	public SMExBean getIniSMExBean(){
		SMExBean smb=new SMExBean();
		smb.setOrgAddr("106575020950");
	  	smb.setNeedStateReport(0);
	  	smb.setServiceID("MBJ0010500");
	  	smb.setFeeType("01");
	  	smb.setFeeCode("0");
		return smb;
	}
	
	//处理定时任务，检查定时任务，将满足触发条件的定时任务保存至短信发送任务表
	public void doTimerTask(){
		//检查，自动发送短信的设置
		JDBCcon cn = new JDBCcon();
		ResultSet rs=cn.getResultSet("select dy.id, dc.mingc,dc.jib,dy.diancxxb_id,dy.jiesrq,dy.fujxx,dy.fujdh,"+
				"dy.duanxdypzb_id,pz.fashs,pl.leib,pl.fasrq,pl.faszqsssj,pl.faszqjssj,pl.fasmrpd ,pl.faszq,pl.faszqdw"+
				" from duanxdyb  dy,duanxdypzb pz,duanxplb pl,diancxxb dc" +
				" where dy.id=pl.duanxdyb_id" +
				" and dy.diancxxb_id=dc.id" +
				" and dy.duanxdypzb_id= pz.id" +
				" and dy.zhuangt=1");
		
		boolean blnIsToday=false;
		
		Date timeNow=new Date();
		Date timeStart=new Date();
		Date timeEnd=new Date();
		Date timeSend=new Date();
		
		int intEndMinutes=0;
		int intMinutes=0;
		int intNowMinutes=0;
		
		String[] arrfasrq=null;
		
		try {
			while (rs.next()){
				//是否有满足条件的的，短信要发
				//首先检查是不是当天要发的
				blnIsToday=false;
				arrfasrq=rs.getString("fasrq").split(",");//fasrq,发送日期 用","号分隔的，如每月的1 号3好 得到的值为"1,3"
				if (Sms.LEIB_DAY.equals(rs.getString("leib"))){
					blnIsToday=true;
				}else if (Sms.LEIB_WEEK.equals(rs.getString("leib"))){
					for (int i=0;i<arrfasrq.length; i++){
						//每周的第几天与今天的日期匹配，如果匹配则，此记录要判断时间，否则不用处理
						if (DateUtil.GetDayOfWeek()==Integer.parseInt(arrfasrq[i])){
							blnIsToday=true;
							break;
						}
					}
				}else if (Sms.LEIB_MONTH.equals(rs.getString("leib"))){
					for (int i=0;i<arrfasrq.length; i++){
						//每月的第几天与今天的日期匹配，如果匹配则，此记录要判断时间，否则不用处理
						if (DateUtil.getDay(timeNow)==Integer.parseInt(arrfasrq[i])){
							blnIsToday=true;
							break;
						} 
					}
				}		
				
				if (blnIsToday){//是今天需要发送的，进一步判断时间
					//得到发送周期
					int intCycle=rs.getInt("faszq");
					int intCycleMinutes=intCycle;
					
					timeStart=rs.getTimestamp("faszqsssj");
					timeEnd=rs.getTimestamp("faszqjssj");
					
					if (Sms.CYCLE_HOUR.equals(rs.getString("faszqdw"))){
						intCycleMinutes=intCycle*60;
					}
					
					if (intCycle==0){//一次性发送
						timeSend=DateUtil.getDateTime( DateUtil.FormatDate(timeNow)+ " "+DateUtil.Formatdate("hh:mm:ss", timeStart));
						
						//检查数据库中是否有此自动任务此时间的信息，如果没有则产生一个任务
						if (!hasAutoTask(rs.getLong("id"),timeSend)){
							newAutoExcuteTask (rs.getLong("diancxxb_id"),timeSend,rs.getString("fujxx"),"",rs.getLong("duanxdypzb_id"),rs.getLong("id"));
						}
					}else{//多次重复发送
						intMinutes=DateUtil.getHour(timeStart)*60+DateUtil.getMinutes(timeStart);
						intEndMinutes=DateUtil.getHour(timeEnd)*60+DateUtil.getHour(timeEnd);
						intNowMinutes=DateUtil.getHour(timeNow)*60+DateUtil.getMinutes(timeNow);
						
						//在指定的时间范围内,
						if (intNowMinutes>intMinutes && intNowMinutes<= intEndMinutes){
							int intMod=0;
							
							//计算最近的一次要发生任务的时间
							intMod=(intNowMinutes-intMinutes) % intCycleMinutes;
							timeSend=DateUtil.AddDate(timeNow, -intMod, DateUtil.AddType_intMinutes);
							
							//检查数据库中是否有此自动任务此时间的信息，如果没有则产生一个任务
							if (!hasAutoTask(rs.getLong("id"),timeSend)){
								newAutoExcuteTask (rs.getLong("diancxxb_id"),timeSend,rs.getString("fujxx"),"",rs.getLong("duanxdypzb_id"),rs.getLong("id"));
							}
						}
					}
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cn.closeRs();
		cn.Close();
	}
	
	//是否已存在某时间的自动发送任务的记录
	public boolean hasAutoTask(long lngDuanxdyb_id,Date datDate){
		JDBCcon cn = new JDBCcon();
		boolean blnhasTask=false;
		ResultSetList rs = cn.getResultSetList("select * from duanxfsb where duanxdyb_id="+lngDuanxdyb_id +" and  riq=to_date('"+ DateUtil.Formatdate("yyyy-MM-dd hh:mm", datDate)+"','yyyy-MM-dd HH24:mi:ss')") ;
		
		if (!(rs==null)){
			while (rs.next()){
				blnhasTask=true;
			}
			rs.close();
		}
		cn.Close();
		return blnhasTask;
	}
	
	//保存一条
	public boolean newAutoExcuteTask(long lngDianxxxb_id,Date datSendTime, String fujxx,String fujdh,long lngDuanxdypzb_id,long lngDuanxdyb_id){
		int iCount=0;
		JDBCcon cn = new JDBCcon();
		String strNewID=MainGlobal.getNewID(lngDianxxxb_id);
		
		cn.setAutoCommit(true);
		
		if (fujxx==null){
			fujxx="";
		}
		//保存任务
		iCount=cn.getInsert(" insert into duanxfsb(id,diancxxb_id,riq,neir,fujdh,lury,lurysj,leib,zhuangt,duanxdypzb_id,duanxdyb_id) "
					+"values("+strNewID+"," +
							lngDianxxxb_id+"," +		
							"to_date('"+DateUtil.Formatdate("yyyy-MM-dd hh:mm", datSendTime)+"','yyyy-mm-dd HH24:mi')," +
							"'" +fujxx+"',"+
							"'" +fujdh+"',"+
							"'系统服务'," +
							"sysdate," +
							"1,0," +
							lngDuanxdypzb_id+","+
							lngDuanxdyb_id+")");
		if (iCount!=1) {
			cn.rollBack();
			cn.setAutoCommit(false);
			cn.Close();
			return false;
		}
		
		//保存联系人
		iCount=cn.getInsert("insert into duanxjsrb " + 
	       " select '"+lngDianxxxb_id+"'||xl_xul_id.nextval,"+strNewID+",js.jiesr,js.jiesr_cw, zhuangt" +
	       " from duanxjsrb js" +
	       " where duanxfsb_id=" +lngDuanxdyb_id);
		
		if (iCount!=1) {//有问题
			cn.rollBack();
			cn.setAutoCommit(false);
			cn.Close();
			return false;
		}
		
		cn.commit();
		cn.setAutoCommit(false);
		cn.Close();
		return true;
	}
	
	//保存短信发送日志
	public boolean saveSendTaskLog(String strNeir,String strJiesr,String strDianh,long lngDuanxdyb_id ){
		return true;
	}
	//处理所有未处理任务发送任务
	public void doSendTask(){
		
		doTimerTask();
		
		SMExBean smb=new SMExBean();
		smb=getIniSMExBean();
		
		JDBCcon cn = new JDBCcon();
		ResultSetList rs = cn.getResultSetList("select fs.riq,fs.id,fs.neir,fs.fujdh,fs.leib,pz.fashs,Duanxdypzb_id,duanxdyb_id from duanxfsb fs,duanxdypzb pz " 
					+ " where fs.zhuangt=0 and riq<=sysdate and fs.Duanxdypzb_id=pz.id(+)") ;
		
		if (!(rs==null)){
			while (rs.next()){
				doOneSendTask (rs.getDate("riq"), rs.getLong("id"),rs.getString("neir"),rs.getString("fujdh"),rs.getString("fashs"),smb);
			}
			rs.close();
		}
		cn.Close();
	}
	
	public boolean isNullString(String strValue){
		if (strValue==null){
			return true;
		} 
		
		if (strValue.equals("")){
			return true;
		}
		return false;
	}

	//处理一行发送任务
	public String doOneSendTask(Date datSend,long lngDuanxfsbId,String strNeir,String strFujdh,String strFashs,SMExBean smb){
		JDBCcon cn = new JDBCcon();
		String strRenyIDs="";				//人员id集合
		String strAllErrorReceivers="";		//记录所有发送出错的人员名字
		String strErrorIDs="";				//记录每行接受人中的错误人id
		String strPhoneNumber="";			//接收人的电话
		String strHansSmsInfo="";			//函数查询返回的信息
		boolean  blnOk =false;
		boolean blnError=false;
		ResultSetList rsNum=null;
		if (strFashs==null ){
			strFashs="";
		}
		if (strNeir==null){
			strNeir="";
		}
		//查询接收人的id集合
		ResultSetList rs = cn.getResultSetList( "select id,jiesr from duanxjsrb where duanxfsb_id=" +lngDuanxfsbId);
		if (!(rs==null)){
			while (rs.next()){
				strRenyIDs=rs.getString("jiesr");
				strErrorIDs="";
				
				if  (!strRenyIDs.trim().equals("")){//联系人id的集合是否为空
					//查询联系人集合的移动电话,及函数返回的内容
					if (strFashs.equals("")){
						rsNum=cn.getResultSetList("select '' as SmsInfo,diancxxb_id,id,xingm,shouj from lianxrb  where id in("+strRenyIDs+") ");
					}else{
						rsNum=cn.getResultSetList("select "+strFashs+"(diancxxb_id,to_date('"+DateUtil.Formatdate("yyyy-MM-dd hh:mm:ss", datSend)+"','yyyy-mm-dd hh24:mi:ss')) as SmsInfo ,id,xingm,shouj from lianxrb  where id in("+strRenyIDs+") ");
					}

					if (!(rsNum==null)){//sql查询错误时，返回null
						while (rsNum.next()){
							strPhoneNumber=rsNum.getString("shouj");
							strHansSmsInfo=rsNum.getString("SmsInfo");
							if (isNullString(strHansSmsInfo)){//函数返回的为空，
								blnOk =sendSms(strPhoneNumber,strNeir, smb);//发送一条短信
							}else{
								blnOk =sendSms(strPhoneNumber,strHansSmsInfo+strNeir, smb);//发送一条短信
							}
							
							if (!blnOk ){//有错误
								//记录出错的人员ID，用逗号分隔
								if (strErrorIDs.length()>0){
									strAllErrorReceivers=strAllErrorReceivers+"," +rsNum.getString("xingm");
									strErrorIDs=strErrorIDs +","+rsNum.getString("id");
								}else{
									strErrorIDs=rsNum.getString("id");
									strAllErrorReceivers=rsNum.getString("xingm");
								}
								blnError=true;//如果有一个错误，标记此任务为错误
							}
						}
						rsNum.close();
					}else{
						if  (strAllErrorReceivers.length()>0){
							strAllErrorReceivers=strAllErrorReceivers+","+strRenyIDs;
						}else{
							strAllErrorReceivers=strRenyIDs;
						}
					}
				}
				
				if (strErrorIDs.length()==0){//没有错误将状态更新为1，标识已成功
					cn.getUpdate(" update duanxjsrb set jiesr_cw=null,zhuangt=1 where id="+rs.getString("id"));
				}else{//有错误记录发送错误的人员，下次再发送
					cn.getUpdate(" update duanxjsrb set jiesr_cw='"+strErrorIDs+"' where id="+rs.getString("id"));
				}
			}
			rs.close();
			
			if (!blnError){//没有一个错误，更新任务状态为1,标识执行成功
				cn.getUpdate("update duanxfsb set zhuangt=1 where id="+lngDuanxfsbId);
			}
			cn.Close();
			return strAllErrorReceivers;
		}
		cn.Close();
		return "查询数据库错误!";
	}
	
	//发送一条短信
	public boolean sendSms(String strPhoneNumber ,String strMsg,SMExBean smb){
		try{
			if (strPhoneNumber==null){//号码为空
				return false;
			}else if(strPhoneNumber.trim().equals("")){//号码为空
				return false;
			}
			
			if (strMsg==null){
				return true;//如果没有信息，返回发送成功
			}else if (strMsg.trim().equals("")){
				return true;
			}
			smb.setDestAddr(strPhoneNumber);
			smb.setSmContent(strMsg);
			int count=SMEntryEx.submitShortMessage(smb);
			if (count>0){
				return true;//发送成功
			}
		}catch(Exception e) {
			System.out.println( e.getMessage());
		}
		return false;
	}
}
