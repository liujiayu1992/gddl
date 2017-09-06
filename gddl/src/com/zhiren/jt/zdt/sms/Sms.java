/* 
* ʱ�䣺2009-03-23 
* ���ߣ� chh 
* �޸����ݣ� 1.sql����ʱ,�ر����ӡ�
* 			2.newAutoExcuteTask �е�sql����
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
	public static final String LEIB_DAY="ÿ��";
	public static final String LEIB_WEEK="ÿ��";
	public static final String LEIB_MONTH="ÿ��";
	
	public static final String CYCLE_MINUTE="����";
	public static final String CYCLE_HOUR="Сʱ";
	
	public static final long DUNAXLB_TEXT=0;//ֱ�ӷ����ı��Ķ�������
	public static final long DUNAXLB_FUNFCTION=1;//�������Ʒ��ؽ���Ķ�������
	
	public String getIniSMS(){
		//��ʼ������¼��������
		System.out.println(System.getProperty("user.dir"));
		SMEntryEx.init("192.168.1.52","sa","sa2000");
	  	try {
			SMEntryEx.logIn("ȼ�Ϲ���","123456");
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
	
	//����ʱ���񣬼�鶨ʱ���񣬽����㴥�������Ķ�ʱ���񱣴������ŷ��������
	public void doTimerTask(){
		//��飬�Զ����Ͷ��ŵ�����
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
				//�Ƿ������������ĵģ�����Ҫ��
				//���ȼ���ǲ��ǵ���Ҫ����
				blnIsToday=false;
				arrfasrq=rs.getString("fasrq").split(",");//fasrq,�������� ��","�ŷָ��ģ���ÿ�µ�1 ��3�� �õ���ֵΪ"1,3"
				if (Sms.LEIB_DAY.equals(rs.getString("leib"))){
					blnIsToday=true;
				}else if (Sms.LEIB_WEEK.equals(rs.getString("leib"))){
					for (int i=0;i<arrfasrq.length; i++){
						//ÿ�ܵĵڼ�������������ƥ�䣬���ƥ���򣬴˼�¼Ҫ�ж�ʱ�䣬�����ô���
						if (DateUtil.GetDayOfWeek()==Integer.parseInt(arrfasrq[i])){
							blnIsToday=true;
							break;
						}
					}
				}else if (Sms.LEIB_MONTH.equals(rs.getString("leib"))){
					for (int i=0;i<arrfasrq.length; i++){
						//ÿ�µĵڼ�������������ƥ�䣬���ƥ���򣬴˼�¼Ҫ�ж�ʱ�䣬�����ô���
						if (DateUtil.getDay(timeNow)==Integer.parseInt(arrfasrq[i])){
							blnIsToday=true;
							break;
						} 
					}
				}		
				
				if (blnIsToday){//�ǽ�����Ҫ���͵ģ���һ���ж�ʱ��
					//�õ���������
					int intCycle=rs.getInt("faszq");
					int intCycleMinutes=intCycle;
					
					timeStart=rs.getTimestamp("faszqsssj");
					timeEnd=rs.getTimestamp("faszqjssj");
					
					if (Sms.CYCLE_HOUR.equals(rs.getString("faszqdw"))){
						intCycleMinutes=intCycle*60;
					}
					
					if (intCycle==0){//һ���Է���
						timeSend=DateUtil.getDateTime( DateUtil.FormatDate(timeNow)+ " "+DateUtil.Formatdate("hh:mm:ss", timeStart));
						
						//������ݿ����Ƿ��д��Զ������ʱ�����Ϣ�����û�������һ������
						if (!hasAutoTask(rs.getLong("id"),timeSend)){
							newAutoExcuteTask (rs.getLong("diancxxb_id"),timeSend,rs.getString("fujxx"),"",rs.getLong("duanxdypzb_id"),rs.getLong("id"));
						}
					}else{//����ظ�����
						intMinutes=DateUtil.getHour(timeStart)*60+DateUtil.getMinutes(timeStart);
						intEndMinutes=DateUtil.getHour(timeEnd)*60+DateUtil.getHour(timeEnd);
						intNowMinutes=DateUtil.getHour(timeNow)*60+DateUtil.getMinutes(timeNow);
						
						//��ָ����ʱ�䷶Χ��,
						if (intNowMinutes>intMinutes && intNowMinutes<= intEndMinutes){
							int intMod=0;
							
							//���������һ��Ҫ���������ʱ��
							intMod=(intNowMinutes-intMinutes) % intCycleMinutes;
							timeSend=DateUtil.AddDate(timeNow, -intMod, DateUtil.AddType_intMinutes);
							
							//������ݿ����Ƿ��д��Զ������ʱ�����Ϣ�����û�������һ������
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
	
	//�Ƿ��Ѵ���ĳʱ����Զ���������ļ�¼
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
	
	//����һ��
	public boolean newAutoExcuteTask(long lngDianxxxb_id,Date datSendTime, String fujxx,String fujdh,long lngDuanxdypzb_id,long lngDuanxdyb_id){
		int iCount=0;
		JDBCcon cn = new JDBCcon();
		String strNewID=MainGlobal.getNewID(lngDianxxxb_id);
		
		cn.setAutoCommit(true);
		
		if (fujxx==null){
			fujxx="";
		}
		//��������
		iCount=cn.getInsert(" insert into duanxfsb(id,diancxxb_id,riq,neir,fujdh,lury,lurysj,leib,zhuangt,duanxdypzb_id,duanxdyb_id) "
					+"values("+strNewID+"," +
							lngDianxxxb_id+"," +		
							"to_date('"+DateUtil.Formatdate("yyyy-MM-dd hh:mm", datSendTime)+"','yyyy-mm-dd HH24:mi')," +
							"'" +fujxx+"',"+
							"'" +fujdh+"',"+
							"'ϵͳ����'," +
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
		
		//������ϵ��
		iCount=cn.getInsert("insert into duanxjsrb " + 
	       " select '"+lngDianxxxb_id+"'||xl_xul_id.nextval,"+strNewID+",js.jiesr,js.jiesr_cw, zhuangt" +
	       " from duanxjsrb js" +
	       " where duanxfsb_id=" +lngDuanxdyb_id);
		
		if (iCount!=1) {//������
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
	
	//������ŷ�����־
	public boolean saveSendTaskLog(String strNeir,String strJiesr,String strDianh,long lngDuanxdyb_id ){
		return true;
	}
	//��������δ��������������
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

	//����һ�з�������
	public String doOneSendTask(Date datSend,long lngDuanxfsbId,String strNeir,String strFujdh,String strFashs,SMExBean smb){
		JDBCcon cn = new JDBCcon();
		String strRenyIDs="";				//��Աid����
		String strAllErrorReceivers="";		//��¼���з��ͳ������Ա����
		String strErrorIDs="";				//��¼ÿ�н������еĴ�����id
		String strPhoneNumber="";			//�����˵ĵ绰
		String strHansSmsInfo="";			//������ѯ���ص���Ϣ
		boolean  blnOk =false;
		boolean blnError=false;
		ResultSetList rsNum=null;
		if (strFashs==null ){
			strFashs="";
		}
		if (strNeir==null){
			strNeir="";
		}
		//��ѯ�����˵�id����
		ResultSetList rs = cn.getResultSetList( "select id,jiesr from duanxjsrb where duanxfsb_id=" +lngDuanxfsbId);
		if (!(rs==null)){
			while (rs.next()){
				strRenyIDs=rs.getString("jiesr");
				strErrorIDs="";
				
				if  (!strRenyIDs.trim().equals("")){//��ϵ��id�ļ����Ƿ�Ϊ��
					//��ѯ��ϵ�˼��ϵ��ƶ��绰,���������ص�����
					if (strFashs.equals("")){
						rsNum=cn.getResultSetList("select '' as SmsInfo,diancxxb_id,id,xingm,shouj from lianxrb  where id in("+strRenyIDs+") ");
					}else{
						rsNum=cn.getResultSetList("select "+strFashs+"(diancxxb_id,to_date('"+DateUtil.Formatdate("yyyy-MM-dd hh:mm:ss", datSend)+"','yyyy-mm-dd hh24:mi:ss')) as SmsInfo ,id,xingm,shouj from lianxrb  where id in("+strRenyIDs+") ");
					}

					if (!(rsNum==null)){//sql��ѯ����ʱ������null
						while (rsNum.next()){
							strPhoneNumber=rsNum.getString("shouj");
							strHansSmsInfo=rsNum.getString("SmsInfo");
							if (isNullString(strHansSmsInfo)){//�������ص�Ϊ�գ�
								blnOk =sendSms(strPhoneNumber,strNeir, smb);//����һ������
							}else{
								blnOk =sendSms(strPhoneNumber,strHansSmsInfo+strNeir, smb);//����һ������
							}
							
							if (!blnOk ){//�д���
								//��¼�������ԱID���ö��ŷָ�
								if (strErrorIDs.length()>0){
									strAllErrorReceivers=strAllErrorReceivers+"," +rsNum.getString("xingm");
									strErrorIDs=strErrorIDs +","+rsNum.getString("id");
								}else{
									strErrorIDs=rsNum.getString("id");
									strAllErrorReceivers=rsNum.getString("xingm");
								}
								blnError=true;//�����һ�����󣬱�Ǵ�����Ϊ����
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
				
				if (strErrorIDs.length()==0){//û�д���״̬����Ϊ1����ʶ�ѳɹ�
					cn.getUpdate(" update duanxjsrb set jiesr_cw=null,zhuangt=1 where id="+rs.getString("id"));
				}else{//�д����¼���ʹ������Ա���´��ٷ���
					cn.getUpdate(" update duanxjsrb set jiesr_cw='"+strErrorIDs+"' where id="+rs.getString("id"));
				}
			}
			rs.close();
			
			if (!blnError){//û��һ�����󣬸�������״̬Ϊ1,��ʶִ�гɹ�
				cn.getUpdate("update duanxfsb set zhuangt=1 where id="+lngDuanxfsbId);
			}
			cn.Close();
			return strAllErrorReceivers;
		}
		cn.Close();
		return "��ѯ���ݿ����!";
	}
	
	//����һ������
	public boolean sendSms(String strPhoneNumber ,String strMsg,SMExBean smb){
		try{
			if (strPhoneNumber==null){//����Ϊ��
				return false;
			}else if(strPhoneNumber.trim().equals("")){//����Ϊ��
				return false;
			}
			
			if (strMsg==null){
				return true;//���û����Ϣ�����ط��ͳɹ�
			}else if (strMsg.trim().equals("")){
				return true;
			}
			smb.setDestAddr(strPhoneNumber);
			smb.setSmContent(strMsg);
			int count=SMEntryEx.submitShortMessage(smb);
			if (count>0){
				return true;//���ͳɹ�
			}
		}catch(Exception e) {
			System.out.println( e.getMessage());
		}
		return false;
	}
}
