package com.zhiren.webservice;
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
import com.zhiren.common.En_Decrypt_ZR;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
public class InterCom { 
	private static final String error000="1,000,���ճɹ�";
	private static final String error001="-1,001,XMLData����ת��Ϊgb312����ʱ����";
	private static final String error002="-1,002,�ĵ�������dom�淶,�����Ƿ������ñ���sql���ֶ�Ϊ����ʱû��д����";
	private static final String error003="-1,003,�����쳣�����鷢�ͽ��յ����ã����緢�����ݵ�����ʱ�����ͱ����ø�ʽ�ַ��������ն˱��������ֶε����õȻ�Զ�����ݿ�����ظ������ݿ�����ʧ��";//
	private static final String error004="-1,004,Զ��ִ��sqlʱ���ݿ�����ʧ��";
	
	private static final String error005="-1,005,Զ��ִ��sqlʱ�����������ǿͻ������ɵ�sql��䲻���Ϲ淶�����鷢�����ñ�";
	
	private static final String error007="1,007,ɾ��Զ������ʱû���ҵ������ֶ����ƣ�������Զ�̽�������û��������������";//ɾ�����ɹ�ʱ���´β���ִ��
	private static final String error008="1,008,ɾ��0�����ݣ�Ҳ����Զ�������뱾������û��ͬ��";
	
	private static final String error009="-1,009,�û����Ʋ��ڣ���ĵ糧��û���ڼ���ע���û�";
	private static final String error010="-1,010,�û��˻��������������ϵͳ��������";
	private static final String error011="-1,011,�������ݿ�Ľ��������в�ʶ�������";
	private static final String error013="-1,013,���Ͷ�����ն����õ��ֶθ�����һ��";
	private static final String error014="-1,014,����ɾ��ʱ��δ֪�쳣";
	private static final String error016="-1,016,����ʱ��������������Կ����";
	private int cipherFlag=0;//100000��ʮ�򣩱�ʾ�Ǽ��ܴ��䡣
//	private Long diancxxb_id=null;String diancxxb_id,String riq1,String riq2,String leix)
	private  StringBuffer diancxxb_id=new StringBuffer();
	public static String getSqlString(String zhuangh_id,String sqlStr){
		String ENDPOINTADDRESS="";
		String resultStr="";
		JDBCcon con=new JDBCcon();
		String sql="select yonghmc,mim,endpointaddress from jiekzhb where id="+zhuangh_id;
		ResultSetList rs=con.getResultSetList(sql);
		try {
			if(rs.next()){
				ENDPOINTADDRESS=rs.getString("endpointaddress");
			}
			if(ENDPOINTADDRESS.equals("")||ENDPOINTADDRESS==null){
				return "������";
			}
			Service service = new Service();
			Call call = (Call) service.createCall();//Զ�̵�����
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
			call.setOperationName("getSqlString");
			call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.SOAP_STRING);
			resultStr=String.valueOf(call.invoke(new Object[] {sqlStr}));//
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			return "������";
		}finally{
			con.Close();
		}
		return resultStr;
	}
	public boolean getShangc(String zhuangh_id,String diancxxb_id,String renwm,String riq1,String riq2){
		String ENDPOINTADDRESS="";
		JDBCcon con=new JDBCcon();
		String sql="select yonghmc,mim,endpointaddress from jiekzhb where id="+zhuangh_id;
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
			call.setOperationName("shangc");
			call.addParameter("diancxxb_id", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq1", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq2", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("leix", XMLType.SOAP_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.AXIS_VOID);
			call.invoke(new Object[] {diancxxb_id,riq1,riq2,renwm});//
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			return false;
		}finally{
			con.Close();
		}
		return true;
	}
	public boolean getRenw(String zhuangh_id,String diancxxb_id,String renwm,String riq1,String riq2){
		String ENDPOINTADDRESS="";
		JDBCcon con=new JDBCcon();
		String sql="select yonghmc,mim,endpointaddress from jiekzhb where id="+zhuangh_id;
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
			call.addParameter("diancxxb_id", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq1", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq2", XMLType.SOAP_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.AXIS_VOID);
			call.invoke(new Object[] {renwm,diancxxb_id,riq1,riq2});//
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			return false;
		}finally{
			con.Close();
		}
		return true;
	}
	public boolean getRenwall(String zhuangh_id,String diancxxb_id,String riq1,String riq2){
			String ENDPOINTADDRESS="";
			JDBCcon con=new JDBCcon();
			String sql="select yonghmc,mim,endpointaddress from jiekzhb where id="+zhuangh_id;
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
				call.addParameter("diancxxb_id", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("riq1", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("riq2", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.AXIS_VOID);
				call.invoke(new Object[] {diancxxb_id,riq1,riq2});//
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
			if(cipherFlag==100000){//����Ǽ��ܴ���
				try {
					XMLData=En_Decrypt_ZR.decryptByDES(XMLData);
				} catch (Exception e) {
					// TODO �Զ����� catch ��
					message=error016;
					e.printStackTrace();
					return message;
				}
			}
			message=xmlToDb(XMLData);
		}
//		System.out.print(message);
//		System.out.print(diancxxb_id);
		return message;
	}
	public String inceptTrans(String user,String password, byte[ ] XMLData){
		String message="";
		int v=yanz(user,password,diancxxb_id);
		if(v==-1){//�û������� 
			message=error009;
			return message;
		}else if(v==0){//�������
			message=error010;
			return message;
		}else{//ͨ����֤
			try {
				XMLData=En_Decrypt_ZR.decryptByDES(XMLData);
			} catch (Exception e) {
				message=error016;
				e.printStackTrace();
				return message;
			}
			message=xmlToDbTrans(XMLData);
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
	private  String xmlToDbTrans( byte[ ] XMLData){//XMLDataΪ������Ϣ��ʽ
		String message=error000;
		JDBCcon con=new JDBCcon();
		con.setAutoCommit(false);
		Element elShiwh=null;//�����
		Element elMingli=null;//����i
		
		Element elShujxy=null;
		Element elGuanlid=null;
		Element elCaoz=null;
		Element elRenwrq=null;
		Element eldiancxxb_id=null;
		int j=0;
		try{
				String xmlstr= new String(XMLData,"GB2312");
				StringReader sr = new StringReader(xmlstr);
				InputSource is = new InputSource(sr);
				Document doc = (new SAXBuilder()).build(is);//���ÿһ������֡�ļ�
				Element root = doc.getRootElement();//<����></����>
//				System.out.print(root.getName());
				List table = root.getChildren();
				elShiwh = (Element) table.get(0);//�����
				 for(j=1;j<table.size();j++){//�����������дӵ�1����ʼ��Table.size()-1��ѭ��ִ��������һ�������˳�
					elMingli=(Element) table.get(j);//����i
					List table_mingl = elMingli.getChildren();//����i
					elShujxy = (Element) table_mingl.get(0);//����Э��
					elCaoz = (Element) table_mingl.get(1);//����
					elGuanlid = (Element) table_mingl.get(2);//ɾ������
					elRenwrq = (Element) table_mingl.get(3);//��������
					eldiancxxb_id = (Element) table_mingl.get(4);//�糧��Ϣ��
					StringBuffer bufferTable=new StringBuffer();
					StringBuffer bufferValus=new StringBuffer();
				    String sql="select renwmc,jiekjspzb.zidmc,leix,jiekbmzhpzb.zidmc zhuanmsql,zhujmc,weik\n" + 
					"from jiekjspzb,jiekbmzhpzb\n" + 
					"where  jiekjspzb.bianm=jiekbmzhpzb.bianm(+) and renwmc='"+elShujxy.getText()+"' order by jiekjspzb.id";
//					System.out.println(sql); 
				    ResultSetList rs=con.getResultSetList(sql);
					if(elCaoz.getText().equals("0")){//0:����1:ɾ�� ���Ϊ���Ӳ���
						//���룺1,���ɱ�ͷ(id,mingc...)2,��ȡ���ݼ�¼��֯��valus3,�γɲ���sql
						//��ͷ�ڶ�������Ϣʱ˳������
						//��ɾ��������
						//����ʱʹ������ɾ��ʱ��ʹ������
						 bufferTable.append("(");
						 int kk=0;
						 while(rs.next()){
							 if(kk==0){
								 bufferTable.append(rs.getString("zidmc"));
							 }else{
								 bufferTable.append(","+rs.getString("zidmc")); 
							 }
							 kk++;
						 }
						 bufferTable.append(")");
//						 System.out.println(bufferTable);
						if( table_mingl.size()>5&&(((Element) table_mingl.get(5)).getChildren()).size()!=rs.getRows()){//���Ͷ�����ն��ֶ��������ò�һ��
							 message=error013+"����λ�ã�"+j+"����";
							 con.rollBack();
							 return message;
						}
						sql=
							"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"='"+elGuanlid.getText()+"'";
						con.getDelete(sql);
						for (int i = 5; i < table_mingl.size(); i++) {//���ݼ�¼(һ��ֻ��һ��)
							Element shujjl = (Element) table_mingl.get(i);
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
									con.rollBack();
									return message;
								}
								if("varchar".equals(leix)||"number".equals(leix)||"id".equals(leix)){
									bufferValus.append("'"+((Element)elZids.get(k)).getText()+"'");
								}else if("bianm".equals(leix)){
									bufferValus.append("("+rs.getString(k,"zhuanmSql")+"'"+((Element)elZids.get(k)).getText()+"')");
								}else if("date".equals(leix)){
									bufferValus.append("to_date('"+((Element)elZids.get(k)).getText()+"','YYYY-MM-DD')");
								}else if("time".equals(leix)){
									bufferValus.append("to_date('"+((Element)elZids.get(k)).getText()+"','YYYY-MM-DD HH24:MI:SS')");
								}else if("decode".equals(leix)){//varchar,nubmer
									bufferValus.append("("+rs.getString(k,"zhuanmSql").replaceAll("%%", "'"+((Element)elZids.get(k)).getText()+"'")+")");//�滻����
								}else{//��ʶ�������
									message=error011+":"+leix;
									con.rollBack();
									return message;
								}
							}
							bufferValus.append(")");
							
							sql="insert into "+elShujxy.getText()+bufferTable.toString()+"values\n"+bufferValus.toString();
							System.out.println(sql); 
							int flag=con.getInsert(sql);
							if(flag==-1){
								throw new SQLException();
							}
						}
					}else {
						//ɾ���������γ�ɾ��sql
						 sql=
							"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"="+elGuanlid.getText();
						 int count=con.getDelete(sql);
						 if(count==-1){//"-1,003,"+str+""ɾ��"+count+"������
							 message=error007+"����λ�ã�"+j+"����";;
							 con.rollBack();
							 return message;
						 }if(count==0){
							 message=error000;
						 }else{
							 message=error000;
						 }
					}
				 }
			}catch(IOException e){
				con.rollBack();
				message=error001+"����λ�ã�"+j+"����";;
				return message;
			}catch(JDOMException e1){
				con.rollBack();
				message=error002+"����λ�ã�"+j+"����";;
				return message;
			}catch(SQLException e2){
				con.rollBack();
				message=error003+"����λ�ã�"+j+"����";;//��
				return message;
			}catch(Exception e3){
				con.rollBack();
				message=error014+"����λ�ã�"+j+"����";;//��
				return message;
			}
			finally{
				  con.Close();
				  Xiejsrzb(diancxxb_id.toString(),elShujxy.getText(),elGuanlid.getText(),message+"����ţ�"+elShiwh.getText(),elCaoz.getText(),elRenwrq.getText(),eldiancxxb_id.getText());
			}
			con.commit();
			return error000;//���سɹ�
				
}
	private  String xmlToDb( byte[ ] XMLData){
		String message=error000;
		JDBCcon con=new JDBCcon();
		Element elShujxy=null;
		Element elGuanlid=null;
		Element elCaoz=null;
		Element elRenwrq=null;
		Element eldiancxxb_id=null;
		
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
				 elRenwrq = (Element) Table.get(3);//��������
				 eldiancxxb_id = (Element) Table.get(4);//�糧��Ϣ��
				 
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
					//����ʱʹ������ɾ��ʱ��ʹ������
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
					if( Table.size()>5&&(((Element) Table.get(5)).getChildren()).size()!=rs.getRows()){//���Ͷ�����ն��ֶ��������ò�һ��
						 message=error013;
						 return message;
					}
					con.setAutoCommit(false);
					sql=
						"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"='"+elGuanlid.getText()+"'";
					con.getDelete(sql);
					for (int i = 5; i < Table.size(); i++) {//���ݼ�¼(һ��ֻ��һ��)
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
//						System.out.println(sql); 
						int flag=con.getInsert(sql);
						if(flag==-1){
							throw new SQLException();
						}
					}
					
					if(elShujxy.getText().equals("rulmzlb")||elShujxy.getText().equals("meihyb")){
						UpdateRulzlID(con,rulrq,eldiancxxb_id.getText());//.longValue()
					}
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
			message=error001;
			return message;
		}catch(JDOMException e1){
			con.rollBack();
			message=error002;
			return message;
		}catch(SQLException e2){
			con.rollBack();
			message=error003;//��
			return message;
		}catch(Exception e3){
			con.rollBack();
			message=error014;//��
		}
		finally{
			  con.Close();
			  Xiejsrzb(diancxxb_id.toString(),elShujxy.getText(),elGuanlid.getText(),message,elCaoz.getText(),elRenwrq.getText(),eldiancxxb_id.getText());
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
//			System.out.println(zhuanmsql+"'"+date+"'");
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
		"select mim,id,diancxxb_id\n" +
		"from jiekzhb\n" + 
		"where jiekzhb.yonghmc='"+user+"'";
		ResultSetList rs=con.getResultSetList(sql);
		 try{
			if(rs.next()){
					diancxxb_id.setLength(0);
					diancxxb_id.append(rs.getString(1));
					cipherFlag=rs.getInt(2);
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
	private  void UpdateRulzlID(JDBCcon con,String riq,String diancxxb_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("update meihyb h ")
		.append("set rulmzlb_id = ( \n")
		.append("select nvl(max(id),0) from rulmzlb z where z.rulrq = h.rulrq \n")
		.append("and z.diancxxb_id = h.diancxxb_id and z.rulbzb_id = h.rulbzb_id \n")
		.append("and z.jizfzb_id = h.jizfzb_id ) where h.rulrq = ").append(DateUtil.FormatOracleDate(riq))
		.append(" and  h.diancxxb_id=").append(diancxxb_id);
		con.getUpdate(sb.toString());
	}
	private void Xiejsrzb(String zhangh_id,String renw,String renwbs,String message,String caoz,String renwrq,String diancxxb_id){//д������־��
		String[]dat=message.split(",");
//		if(dat[0].equals("-1")){//���Ϊ����
			JDBCcon con=new JDBCcon();
//			System.out.print(renwrq);
			String sql=
			"insert into jiekjsrzb(id,diancxxb_id,renw,renwbs,shij,cuowdm,cuowxx,caoz,zhixzt,riq,zhuangh_id)values(\n" +
			"xl_diancxxb_id.nextval,"+diancxxb_id+",'"+renw+"',"+renwbs+",sysdate,'"+dat[1]+"','"+dat[2]+"','"+caoz+"',"+dat[0]+",to_date('"+renwrq+"','yyyy-mm-dd hh24:mi:ss'),"+zhangh_id+")";
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
}