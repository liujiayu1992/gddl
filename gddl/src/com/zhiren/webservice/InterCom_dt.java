package com.zhiren.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
public class InterCom_dt { 
	private static final String error000="1,000,���ճɹ�";
	private static final String error001="-1,001,XMLData����ת��Ϊgb312����ʱ����";
	private static final String error002="-1,002,�ĵ�������dom�淶,�����Ƿ������ñ���sql���ֶ�Ϊ����ʱû��д����";
	private static final String error003="-1,003,˫��û�а��սӿ�Э�鴫�����ݣ����鷢�ͽ��յ����ã����緢�����ݵ�����ʱ�����ͱ����ø�ʽ�ַ��������ն˱��������ֶε����õȻ�Զ�����ݿ�����ظ���Υ��ΨһԼ������";//
	private static final String error004="-1,004,ִ��Զ�̵�sqlʱԶ�̷��������ݿ�����ʧ��";
	private static final String error005="-1,005,ִ��Զ�̵�sqlʱ�����������ǿͻ������ɵ�sql��䲻���Ϲ淶�����鷢�����ñ�";
	private static final String error007="-1,007,ɾ��Զ������ʱû���ҵ������ֶ����ƣ�������Զ�̽�������û��������������!";
	private static final String error008="1,008,ɾ��0�����ݣ�Ҳ����Զ�������뱾������û��ͬ��";
	private static final String error009="-1,009,�û����Ʋ��ڣ���ĵ糧��û���ڼ���ע���û�";
	private static final String error010="-1,010,�û��˻��������������ϵͳ��������";
	private static final String error011="-1,011,�������ݿ�Ľ��������в�ʶ�������";
	private static final String error013="-1,013,���Ͷ�����ն����õ��ֶθ�����һ�¡�";
	private static final String error014="-1,014,δ֪�쳣��";
	private static final String error015="-1,015,д�ļ�ʱ������";
//	private static final String error016="-1,016,����ʱ��������������Կ����";
	
//	private Long diancxxb_id=null;
	private  StringBuffer diancxxb_id=new StringBuffer();
	public boolean getRenw(String diancxxb_id,String renwm){
		String ENDPOINTADDRESS="";
		JDBCcon con=new JDBCcon();
		String sql="select yonghmc,mim,endpointaddress from jiekzhb where diancxxb_id="+diancxxb_id;
		ResultSetList rs=con.getResultSetList(sql);
		try {
			if(rs.next()){
				ENDPOINTADDRESS=rs.getString("endpointaddress");
			}
			if(ENDPOINTADDRESS.equals("")||ENDPOINTADDRESS==null
			){
				return false;
			}
			Service service = new Service();
			Call call = (Call) service.createCall();//Զ�̵�����
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
			call.setOperationName("request");
			call.addParameter("task", XMLType.SOAP_STRING,ParameterMode.IN);
			call.invoke(new Object[] {renwm});//
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			return false;
		}finally{
			con.Close();
		}
		return true;
	}
	public boolean getRenwall(String diancxxb_id){
			String ENDPOINTADDRESS="";
			JDBCcon con=new JDBCcon();
			String sql="select yonghmc,mim,endpointaddress from jiekzhb where diancxxb_id="+diancxxb_id;
			ResultSetList rs=con.getResultSetList(sql);
			try {
				if(rs.next()){
					ENDPOINTADDRESS=rs.getString("endpointaddress");
				}
				if(ENDPOINTADDRESS.equals("")||ENDPOINTADDRESS==null
				){
					return false;
				}
				Service service = new Service();
				Call call = (Call) service.createCall();//Զ�̵�����
				call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
				call.setOperationName("requestall");
				call.invoke(new Object[] {});//
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
				return false;
			}finally{
				con.Close();
			}
			return true;
	}
	public String incept(String user,String password, byte[ ] XMLData){
		String message="";
		int v=yanz(user,password,diancxxb_id);
		if(v==-1){//�û������� 
			message=error009;
			return message;
		}else if(v==0){//�������
			message=error010;
			return message;
		}else{//ͨ����֤
			message=xmlToDb(XMLData);
		}
//		System.out.print(message);
//		System.out.print(diancxxb_id);
		return message;
	}
	public String execute(String user,String password,String sql) {
		System.out.print(sql);
		String message="";
		int v=yanz(user,password,diancxxb_id);
		if(v==-1){//�û������� 
			message=error009;
			return message;
		}else if(v==0){//�������
			message=error010;
			return message;
		}else{//ͨ����֤
			message=error000;
			Statement st=null;
			Connection dBConnection=null;
			try {
			String ConnStr="",UserName="",UserPassword="";
			if (ConnStr.equals("")) {
				ConnStr = MainGlobal.getDb_jdbcDriverURL();
			}
			if (UserName.equals("")) {
				UserName = MainGlobal.getDb_username();
			}
			if (UserPassword.equals("")) {
				UserPassword = MainGlobal.getDb_password();
			}
			Class.forName("oracle.jdbc.driver.OracleDriver");
			dBConnection = DriverManager.getConnection(ConnStr,UserName, UserPassword);
			st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			st.executeUpdate(sql);
			} catch (ClassNotFoundException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
				message=error004;
			}catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
				message=error005+sql;
			}finally{
				try {
					st.close();
					dBConnection.close();
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
			}
		}
		
		return 	message;
	}
	private  String xmlToDb( byte[ ] XMLData){
		String message=error000;
		JDBCcon con=new JDBCcon();
		Element elShujxy=null;
		Element elGuanlid=null;
		Element elCaoz=null;
		Statement st=null;
		Connection dBConnection=null;
		//0,����xmlȡ������1,ȡ����ʽ����2,����table��values
		try{
			//0
				String xmlstr= new String(XMLData,"GB2312");
				StringReader sr = new StringReader(xmlstr);
//				System.out.println(xmlstr);
				InputSource is = new InputSource(sr);
				Document doc = (new SAXBuilder()).build(is);//���ÿһ������֡�ļ�
				Element root = doc.getRootElement();
//				System.out.print(root.getName());
				List Table = root.getChildren();

				 elShujxy = (Element) Table.get(0);//����Э��
				 elCaoz = (Element) Table.get(1);//����
				 elGuanlid = (Element) Table.get(2);//ɾ������
			//1	2
				StringBuffer bufferTable=new StringBuffer();
				StringBuffer bufferValus=new StringBuffer();
			    String sql="select renwmc,jiekjspzb.zidmc,leix,jiekbmzhpzb.zidmc zhuanmsql,zhujmc,weik\n" + 
				"from jiekjspzb,jiekbmzhpzb\n" + 
				"where  jiekjspzb.bianm=jiekbmzhpzb.bianm(+) and renwmc='"+elShujxy.getText()+"' order by jiekjspzb.id";
//				System.out.println(sql); 
			    ResultSetList rs=con.getResultSetList(sql);
				if(elCaoz.getText().equals("0")){//0:����1:ɾ�� ���Ϊ���Ӳ���
					//���룺1,���ɱ�ͷ(id,mingc...)2,��ȡ���ݼ�¼��֯��valus3,�γɲ���sql
					//��ͷ�ڶ�������Ϣʱ˳������
					//��ɾ��������
					con.setAutoCommit(false);
					sql=
							"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"="+elGuanlid.getText();
					con.getDelete(sql);
					String rulrq="";
					 bufferTable.append("(");
					 int kk=0;
					 while(rs.next()){
						// System.out.println(kk);
						// jiespzBeanList.add(new InterCom.JiespzBean(rs.getString("renwmc"),rs.getString("zidmc"),rs.getString("leix"),rs.getString("zhuanmsql"),rs.getString("zhujmc"))) ;
						// System.out.println(((JiespzBean)jiespzBeanList.get(0)).getZidmc());
						 if(kk==0){
							 bufferTable.append(rs.getString("zidmc"));
						 }else{
							 bufferTable.append(","+rs.getString("zidmc")); 
						 }
						 kk++;
					 }
					 bufferTable.append(")");
//					 System.out.println(bufferTable);
					 if( Table.size()>3&&(((Element) Table.get(3)).getChildren()).size()!=rs.getRows()){//���Ͷ�����ն��ֶ��������ò�һ��
						 message=error013;
						 return message;
					 }
					for (int i = 3; i < Table.size(); i++) {//���ݼ�¼
						Element shujjl = (Element) Table.get(i);
						List elZids= shujjl.getChildren();
						bufferValus.setLength(0);
						bufferValus.append("(");
						for(int k=0;k<elZids.size();k++){//2����values,��֤���ݣ�������֤�Ϳ���֤��
							//�����ֶ����õ����ʹ�����Ӧ�ֶ�����
							String leix = rs.getString(k,"leix");
							if(k>0){
								bufferValus.append(",");
							}
							//���Խ��б�����֤�Ϳ���֤
							String str= jiaoy(rs.getString(k,"zidmc"),rs.getString(k,"zhuanmSql"),rs.getString(k,"weik"),((Element)elZids.get(k)).getText(),leix);// �ֶ����ơ��ֶα��롢�ֶ�Ϊ��
							if(!str.equals("")){
								message="-1,012,"+str+"";
								return message;
							}
							if("varchar".equals(leix)||"number".equals(leix)||"id".equals(leix)){
								bufferValus.append("'"+((Element)elZids.get(k)).getText()+"'");
							}else if("bianm".equals(leix)){
								bufferValus.append("("+rs.getString(k,"zhuanmSql")+"'"+((Element)elZids.get(k)).getText()+"')");
							}else if("date".equals(leix)){
								if(k==1){
									rulrq=((Element)elZids.get(k)).getText();
								}
								bufferValus.append("to_date('"+((Element)elZids.get(k)).getText()+"','YYYY-MM-DD')");
							}else if("time".equals(leix)){
								bufferValus.append("to_date('"+((Element)elZids.get(k)).getText()+"','YYYY-MM-DD HH24:MI:SS')");
							}else if("decode".equals(leix)){//varchar,nubmer
								bufferValus.append("("+rs.getString(k,"zhuanmSql").replaceAll("%%", "'"+((Element)elZids.get(k)).getText()+"'")+")");//�滻����
							}else{//��ʶ�������
								message=error011+":"+leix;
								return message;
							}
						}
						bufferValus.append(")");
						sql="insert into "+elShujxy.getText()+bufferTable.toString()+"values\n"+bufferValus.toString();
						int flag=con.getInsert(sql);
						if(flag==-1){
							throw new SQLException();
						}
					}
//					if(elShujxy.getText().equals("rulmzlb")||elShujxy.getText().equals("meihyb")){
//						UpdateRulzlID(rulrq,diancxxb_id.toString());//.longValue()
//					}
					con.commit();
				}else {
					//ɾ���������γ�ɾ��sql
					 sql=
						"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"="+elGuanlid.getText();
					 int count=con.getDelete(sql);
					 if(count==-1){//"-1,003,"+str+""ɾ��"+count+"������
						 message=error007;
					 }if(count==0){
						 message=error008;
					 }else{
						 message=error000;
					 }
					 
					 return message;
				}
		}catch(IOException e){
			con.rollBack();
			message=error001;//zhixzt cuowlb zhixbz 
			return message;
		}catch(JDOMException e1){
			con.rollBack();
			message=error002;//zhixzt cuowlb zhixbz 
			return message;
		}catch(SQLException e2){
			con.rollBack();
			message=error003;//zhixzt cuowlb zhixbz
			
			return message;
		}catch(Exception e4){
			con.rollBack();
			message=error014;//zhixzt cuowlb zhixbz
		}
		finally{
			  con.Close();
			  Xiejsrzb(diancxxb_id.toString(),elShujxy.getText(),elGuanlid.getText(),message,elCaoz.getText());
		}
		return message;
}
	private String jiaoy(String zidmc,String zhuanmsql,String zidwk,String date,String leix) throws SQLException{//�Ƚ��п���֤�ٽ��б�����֤
		String resultStr="";
		if((zidwk.equals("")||zidwk==null)&&(date.equals("")||date==null)){//���������Ϊ��,���ݻ�Ϊ���򷵻ش���
			resultStr=zidmc+"������Ϊ��";
			return resultStr;
		}
		JDBCcon con=new JDBCcon();
		try{
		if(!zhuanmsql.equals("")&&!date.equals("")&&date!=null){//�������ת�룬����ת������
			System.out.println(zhuanmsql+"'"+date+"'");
			if("bianm".equals(leix)){//
				ResultSetList rs=con.getResultSetList(zhuanmsql+"'"+date+"'");
				if(!rs.next()){
					resultStr=zidmc+"�ֶε�"+date+"���벻��ʶ��";
					return resultStr;
				}
			}else{//decode
				if(zidwk.equals("")){//������Ǳ������У��
					ResultSetList rs=con.getResultSetList(zhuanmsql.replaceAll("%%", "'"+date+"'"));
					if(rs.next()){
						if(rs.getString(0)==null||rs.getString(0).equals("")){
							resultStr=zidmc+"�ֶε�"+date+"�����ݿ����Ҳ�����Ӧ��ֵ��Ҳ����˵��Զ�����ݿ������տ��Ϊ��";
						}
						return resultStr;
					}
				}
			}
			
		}}
		finally{
			 con.Close();
		}
		return resultStr;
	}
	private int yanz(String user,String password,StringBuffer diancxxb_id){//��֤�˻�
		JDBCcon con=new JDBCcon();
		int v=1;
		String sql=
		"select mim,id\n" +
		"from jiekzhb\n" + 
		"where jiekzhb.yonghmc='"+user+"'";
		ResultSetList rs=con.getResultSetList(sql);
		 try{
			if(rs.next()){
					diancxxb_id.setLength(0);
					diancxxb_id.append(rs.getString(1));
					if(password.equals(rs.getString(0))){
						v=1;//����û����ڣ�����������ȷ����1
					}else{
						v=0;//�������
					}
			}else{
				v=-1;//�����ڸ��û�
			}
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 con.Close();
		 }
		 return v;
	}
	private  void UpdateRulzlID(String riq,String diancxxb_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("update meihyb h ")
		.append("set rulmzlb_id = ( \n")
		.append("select nvl(max(id),0) from rulmzlb z where z.rulrq = h.rulrq \n")
		.append("and z.diancxxb_id = h.diancxxb_id and z.rulbzb_id = h.rulbzb_id \n")
		.append("and z.jizfzb_id = h.jizfzb_id ) where h.rulrq = ").append(DateUtil.FormatOracleDate(riq))
		.append(" and  h.diancxxb_id=").append(diancxxb_id);
		JDBCcon con = new JDBCcon();
		con.getUpdate(sb.toString());
		con.Close();
	}
	private void Xiejsrzb(String diancxxb_id,String renw,String renwbs,String message,String caoz){//д������־��
		String[]dat=message.split(",");
//		if(dat[0].equals("-1")){//���Ϊ����
			JDBCcon con=new JDBCcon();
			String sql=
			"insert into jiekjsrzb(id,diancxxb_id,renw,renwbs,shij,cuowdm,cuowxx,caoz,zhixzt)values(\n" +
			"xl_xul_id.nextval,"+diancxxb_id+",'"+renw+"',"+renwbs+",sysdate,'"+dat[1]+"','"+dat[2]+"','"+caoz+"',"+dat[0]+
			")";
			con.getInsert(sql);
			con.Close();
//		}
	}
	public String[] sqlExe(String diancxxb_id,String[] sqls,boolean isTransaction){
		String ENDPOINTADDRESS="";
		JDBCcon con=new JDBCcon();
		String[] resultArry;
		String sql=
			"select d.diancxxb_id,z.endpointaddress\n" +
			"from dianczhb d,jiekzhb z\n" + 
			"where d.jiekzhb_id=z.id and d.diancxxb_id="+diancxxb_id;
		ResultSetList rs=con.getResultSetList(sql);
		try {
			if(rs.next()){
				ENDPOINTADDRESS=rs.getString("endpointaddress");
			}
			if(ENDPOINTADDRESS.equals("")||ENDPOINTADDRESS==null){
				return new String[]{"�õ糧û����ڵ�ַ��"};
			}
			Service service = new Service();
			Call call = (Call) service.createCall();//Զ�̵�����
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
			call.setOperationName("sqlExe");
			call.addParameter("sqls", XMLType.SOAP_ARRAY,ParameterMode.IN);
			call.addParameter("isTransaction", XMLType.SOAP_BOOLEAN,ParameterMode.IN);
			call.setReturnType(XMLType.SOAP_ARRAY);
			resultArry=(String[])call.invoke(new Object[] {sqls,Boolean.valueOf(isTransaction)});//���ִ�гɹ�����true
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			return new String[]{"��������"};
		}finally{
			con.Close();
		}
		return resultArry;
	}
	
	public String getShangj_Jicxx(String Type){
//		˵������д����2009-02-23
//			��д��Ա��zsj
//			��ҪĿ�ģ�������Ϣ����ͬ��
//			��Ҫ���ܣ��¼���λͨ�����øú��������ϼ���λ�Ļ�����Ϣ
		StringBuffer resultData=new StringBuffer("");
		JDBCcon con=new JDBCcon();
		String sql="";
		String ErrorMessage="";
		ResultSetList rsl=null;
		
			
		if(Type.equals("MK")){
			
			sql="select 0 as id,m.xuh,m.bianm,m.mingc,m.quanc,m.piny,sf.quanc as shengfb_id,leib,\n" +
				"       jh.mingc as jihkb_id,leix,danwdz,cs.mingc as chengsb_id,m.beiz\n" + 
				"       from meikxxb m,shengfb sf,jihkjb jh,chengsb cs\n" + 
				"       where m.shengfb_id=sf.id(+)\n" + 
				"             and m.jihkjb_id=jh.id\n" + 
				"             and m.chengsb_id=cs.id(+)\n" + 
				"       order by bianm";

			
			
		}else if(Type.equals("GYS")){
			
			sql="select 0 as id,sg.mingc as fuid,g.xuh,g.mingc,g.quanc,g.piny,g.bianm,\n" +
				"       g.danwdz,g.faddbr,g.weitdlr,g.kaihyh,g.zhangh,g.dianh,g.shuih,\n" + 
				"       g.youzbm,g.chuanz,g.meitly,g.meiz,g.chubnl,g.kaicnl,g.kaicnx,\n" + 
				"       g.shengcnl,g.gongynl,g.liux,g.yunsfs,g.shiccgl,g.zhongdht,\n" + 
				"       g.yunsnl,g.heznx,decode(g.rongqgx,1,'��',2,'��',3,'��',4,'��','') as rongqgx,\n" + 
				"       decode(g.xiny,1,'��',2,'��',3,'��',4,'��','') as xiny,g.gongsxz,g.kegywfmz,g.kegywfmzzb,\n" + 
				"       sf.quanc as shengfb_id,decode(g.shifss,1,'��',2,'��','') as shifss,g.shangsdz,g.zicbfb,g.shoumbfb,\n" + 
				"       g.qitbfb,cs.mingc as chengsb_id,g.beiz\n" + 
				"       from gongysb g,gongysb sg,shengfb sf,chengsb cs\n" + 
				"       where g.fuid=sg.id(+)\n" + 
				"             and g.shengfb_id=sf.id(+)\n" + 
				"             and g.chengsb_id=cs.id(+)\n" + 
				"       order by g.bianm";
		}
		
		if(!sql.equals("")){
			rsl=con.getResultSetList(sql);
			if(rsl.getRows()>0){
				int i=0;
				while(rsl.next()){
					for(int j=0;j<rsl.getColumnCount();j++){
						resultData.append("'").append(rsl.getString(j)).append("'").append(",");
					}
					resultData.deleteCharAt(resultData.length()-1);
					resultData.append("~%&");
					i++;
				}
			}
		}else{
			ErrorMessage="��ѯ���� '"+Type+"' δ����";
			resultData.append(ErrorMessage);
		}
		
		if(rsl!=null){
			rsl.close();
		}
		con.Close();
		return resultData.toString();
	}
	public String[]  sqlExe(String[] sqls,boolean isTransaction) throws Exception{ 
		//1�������ݿ�
		String ConnStr="",UserName="",UserPassword="";
		String[] arryRes=new String[sqls.length];
		if (ConnStr.equals("")) {
			ConnStr = MainGlobal.getDb_jdbcDriverURL();
		}
		if (UserName.equals("")) {
			UserName = MainGlobal.getDb_username();
		}
		if (UserPassword.equals("")) {
			UserPassword = MainGlobal.getDb_password();
		}
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection dBConnection = DriverManager.getConnection(ConnStr,UserName, UserPassword);
		Statement st = dBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		//2ѭ��ִ��sqls������¼ִ�н����
		try{
		if(isTransaction){//���������
			dBConnection.setAutoCommit(false);
			for (int i=0;i<sqls.length;i++){
				try {
					st.executeUpdate(sqls[i]);
					arryRes[0]="true";
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					arryRes[0]=e.getLocalizedMessage();
					e.printStackTrace();
					dBConnection.rollback();
					return arryRes;
				}
			}
			dBConnection.commit();
		}else{// ��������
			for (int i=0;i<sqls.length;i++){
				try {
					st.executeUpdate(sqls[i]);
					arryRes[i]="true";
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					arryRes[i]=e.getLocalizedMessage();
					e.printStackTrace();
				}
			}
		}
		}
		finally{
			st.close();
			dBConnection.close();
		}
		return arryRes;
	}
	
	public String[] Liuxj_danj(String Table,String Id,long Diancxxb_id,int Zhuangt){
		
//		�������ܣ�
//			���ƹ��ʷֹ�˾ͨ���÷������ֹ�˾������Ľ��㵥�·�����˵��糧��
//		�����߼���
//			1�����dianczhb,jiekzhb �иõ糧����ڵ�ַ��
//			2�������ַ���ڣ������ѯ�¼��糧liucztb����䣬�Ե�ǰliucztb_idΪǰ��״̬��
//				���Һ��״̬����ϲ������ƣ��õ����״̬id��ΪӦ�ø��µ糧������״̬id��
//			3��ִ����Ϻ���û���ʾ�Ƿ�ִ�гɹ���
//			
//			
//		�����βΣ�
//			(String)Table 		Ҫ�����ı���֧�ֶ��ֵ����','�ֿ���	
//			(String)Id			Ҫ�����ı�id��֧�ֶ��ֵ����','�ֿ���Ӧ��Table��ֵ˳���Ӧ��
//			��Number��Diancxxb_id	Ҫ�����ĵ糧
//			(Number)Zhuangt		Ҫִ�еĲ�����1��Ϊ��˳ɹ��·��糧��0��Ϊ��˲�ͨ�������˵糧��
		
		String ENDPOINTADDRESS="";	//�糧����ڵ�ַ
		String caoz="";						//��Ӧ�Ĳ���
		JDBCcon con=new JDBCcon();
		String[] resultArry=null;
		String[] sqls=null;
		StringBuffer sb=new StringBuffer();
		boolean isTransaction = false;
		
		sb.append("select d.diancxxb_id,z.endpointaddress\n" +
			"from dianczhb d,jiekzhb z\n" + 
			"where d.jiekzhb_id=z.id and d.diancxxb_id="+Diancxxb_id);
		ResultSetList rs=con.getResultSetList(sb.toString());
		
		try {
			if(rs.next()){
				ENDPOINTADDRESS=rs.getString("endpointaddress");
			}
			if(ENDPOINTADDRESS.equals("")||ENDPOINTADDRESS==null){
				return new String[]{"�õ糧û����ڵ�ַ��"};
			}
			
			String Talbes[]=Table.split(",");	//���������
			String Ids[]=Id.split(",");			//����Id����
			long   houjzt=0;					//���״̬
			String sql="";
			
			if(Zhuangt==1){
				
				caoz="�ύ";
			}else if(Zhuangt==0){
				
				caoz="����";
			}
			
			sb.setLength(0);
			sb.append("begin	\n");
			
			for(int i=0;i<Talbes.length;i++){
				
				sql="select liucztb_id from "+Talbes[i]+" where id="+Ids[i];
				rs=con.getResultSetList(sql);
				if(rs.next()){
					
					houjzt=rs.getLong("liucztb_id");
				}
				
				sb.append("update ").append(Talbes[i]).append(" set liucztb_id=").append("(select max(liuczthjid) as hjid from liucdzb where liucztqqid=")
					.append(houjzt).append(" and mingc='"+caoz+"') where id=").append(Ids[i]).append(";\n");
				
			}
			
			sb.append("end;");
			
			if(sb.length()>13){
				
				sqls=new String[1];
				sqls[0]=sb.toString();
				
				Service service = new Service();
				Call call = (Call) service.createCall();//Զ�̵�����
				call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
				call.setOperationName("sqlExe");
				call.addParameter("sqls", XMLType.SOAP_ARRAY,ParameterMode.IN);
				call.addParameter("isTransaction", XMLType.SOAP_BOOLEAN,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_ARRAY);
				resultArry=(String[])call.invoke(new Object[] {sqls,Boolean.valueOf(isTransaction)});//���ִ�гɹ�����true
			}
			
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			return new String[]{caoz+"����������"};
		}finally{
			rs.close();
			con.Close();
		}
		return resultArry;
	} 
	
//	�ļ�����
	public  String fileStroe(String path,String name,byte[] file,String caoz){
		String messager=error000;
		String realPath="";
		String[] renwmc_diancid=new String[2];
		String rootWeb=MainGlobal.getXitxx_item("�̵�", "��˾��ú����·��", "0", "E:\\zhiren\\pand\\");
		renwmc_diancid=path.split(",");
		
		//�����ļ�·��
		realPath=rootWeb+"\\"+renwmc_diancid[1];
		
		File fRealPath=new File(realPath);
		
		if(!fRealPath.exists()){
            //����̵�ͼ���ļ���Ŀ¼��������ʱ�Զ�����
			fRealPath.mkdirs();
		}
		File realFile=new File(realPath+"\\"+name);//�������̵��ļ���
		
		if(caoz.endsWith("0")||caoz.endsWith("2")){//����\�޸�
			FileOutputStream out;
			try {
				out = new FileOutputStream(realFile);
				out.write(file);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				messager=error015;
			}
			out = null;
		}else{
            //ɾ��
			if(realFile.delete()){
				 messager=error000;
			}else{
				messager=error008;
			}
		}
		return messager;
	}
}