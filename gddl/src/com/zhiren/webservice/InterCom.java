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
	private static final String error000="1,000,接收成功";
	private static final String error001="-1,001,XMLData数据转换为gb312编码时出错";
	private static final String error002="-1,002,文档不符合dom规范,可能是发送配置表中sql的字段为函数时没有写别名";
	private static final String error003="-1,003,插入异常，请检查发送接收的配置，例如发送数据的日期时间类型必须用格式字符串，接收端编码类型字段的配置等或远程数据库编码重复或数据库连接失败";//
	private static final String error004="-1,004,远程执行sql时数据库连接失败";
	
	private static final String error005="-1,005,远程执行sql时出错，可能是客户端生成的sql语句不符合规范，请检查发送配置表";
	
	private static final String error007="1,007,删除远程数据时没有找到主键字段名称，可能是远程接收配置没有设置主键名称";//删除不成功时，下次不再执行
	private static final String error008="1,008,删除0条数据，也就是远程数据与本地数据没有同步";
	
	private static final String error009="-1,009,用户名称不在，你的电厂还没有在集团注册用户";
	private static final String error010="-1,010,用户账户的密码错误请检查系统密码设置";
	private static final String error011="-1,011,接收数据库的接收配置有不识别的类型";
	private static final String error013="-1,013,发送端与接收端配置的字段个数不一致";
	private static final String error014="-1,014,插入删除时的未知异常";
	private static final String error016="-1,016,解密时出错，可能是密钥错误";
	private int cipherFlag=0;//100000（十万）表示是加密传输。
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
				return "出错啦";
			}
			Service service = new Service();
			Call call = (Call) service.createCall();//远程调用者
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
			call.setOperationName("getSqlString");
			call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.SOAP_STRING);
			resultStr=String.valueOf(call.invoke(new Object[] {sqlStr}));//
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			return "出错啦";
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
			Call call = (Call) service.createCall();//远程调用者
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
			call.setOperationName("shangc");
			call.addParameter("diancxxb_id", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq1", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq2", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("leix", XMLType.SOAP_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.AXIS_VOID);
			call.invoke(new Object[] {diancxxb_id,riq1,riq2,renwm});//
		} catch (Exception e) {
			// TODO 自动生成 catch 块
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
			Call call = (Call) service.createCall();//远程调用者
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
			call.setOperationName("request");
			call.addParameter("task", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("diancxxb_id", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq1", XMLType.SOAP_STRING,ParameterMode.IN);
			call.addParameter("riq2", XMLType.SOAP_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.AXIS_VOID);
			call.invoke(new Object[] {renwm,diancxxb_id,riq1,riq2});//
		} catch (Exception e) {
			// TODO 自动生成 catch 块
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
				Call call = (Call) service.createCall();//远程调用者
				call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
				call.setOperationName("requestall");
				call.addParameter("diancxxb_id", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("riq1", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("riq2", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.AXIS_VOID);
				call.invoke(new Object[] {diancxxb_id,riq1,riq2});//
			} catch (Exception e) {
				// TODO 自动生成 catch 块
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
		if(v==-1){//用户不存在 
			message=error009;
			return message;
		}else if(v==0){//密码错误
			message=error010;
			return message;
		}else{//通过验证
			if(cipherFlag==100000){//如果是加密传输
				try {
					XMLData=En_Decrypt_ZR.decryptByDES(XMLData);
				} catch (Exception e) {
					// TODO 自动生成 catch 块
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
		if(v==-1){//用户不存在 
			message=error009;
			return message;
		}else if(v==0){//密码错误
			message=error010;
			return message;
		}else{//通过验证
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
		if(v==-1){//用户不存在 
			message=error009;
			return message;
		}else if(v==0){//密码错误
			message=error010;
			return message;
		}else{//通过验证
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
				// TODO 自动生成 catch 块
				e.printStackTrace();
				message=error004;
			}catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
				message=error005+sql;
			}finally{
				try {
					st.close();
					dBConnection.close();
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
			}
		}
		return 	message;
	}
	private  String xmlToDbTrans( byte[ ] XMLData){//XMLData为事务消息格式
		String message=error000;
		JDBCcon con=new JDBCcon();
		con.setAutoCommit(false);
		Element elShiwh=null;//事务号
		Element elMingli=null;//命令i
		
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
				Document doc = (new SAXBuilder()).build(is);//解读每一个数据帧文件
				Element root = doc.getRootElement();//<事务></事务>
//				System.out.print(root.getName());
				List table = root.getChildren();
				elShiwh = (Element) table.get(0);//事务号
				 for(j=1;j<table.size();j++){//处理命令序列从第1个开始共Table.size()-1。循环执行命令有一个报错退出
					elMingli=(Element) table.get(j);//命令i
					List table_mingl = elMingli.getChildren();//命令i
					elShujxy = (Element) table_mingl.get(0);//数据协议
					elCaoz = (Element) table_mingl.get(1);//操作
					elGuanlid = (Element) table_mingl.get(2);//删除主键
					elRenwrq = (Element) table_mingl.get(3);//任务日期
					eldiancxxb_id = (Element) table_mingl.get(4);//电厂信息表
					StringBuffer bufferTable=new StringBuffer();
					StringBuffer bufferValus=new StringBuffer();
				    String sql="select renwmc,jiekjspzb.zidmc,leix,jiekbmzhpzb.zidmc zhuanmsql,zhujmc,weik\n" + 
					"from jiekjspzb,jiekbmzhpzb\n" + 
					"where  jiekjspzb.bianm=jiekbmzhpzb.bianm(+) and renwmc='"+elShujxy.getText()+"' order by jiekjspzb.id";
//					System.out.println(sql); 
				    ResultSetList rs=con.getResultSetList(sql);
					if(elCaoz.getText().equals("0")){//0:增加1:删除 如果为增加操作
						//插入：1,生成表头(id,mingc...)2,读取数据记录组织成valus3,形成插入sql
						//表头在读配置信息时顺便生成
						//先删除后增加
						//增加时使用事务删除时不使用事务
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
						if( table_mingl.size()>5&&(((Element) table_mingl.get(5)).getChildren()).size()!=rs.getRows()){//发送端与接收端字段数量配置不一致
							 message=error013+"出错位置："+j+"命令";
							 con.rollBack();
							 return message;
						}
						sql=
							"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"='"+elGuanlid.getText()+"'";
						con.getDelete(sql);
						for (int i = 5; i < table_mingl.size(); i++) {//数据记录(一般只有一个)
							Element shujjl = (Element) table_mingl.get(i);
							List elZids= shujjl.getChildren();
							bufferValus.setLength(0);
							bufferValus.append("(");
							for(int k=0;k<elZids.size();k++){//2构造values,验证数据（编码验证和空验证）
								//根据字段配置的类型处理响应字段数据
								String leix = rs.getString(k,"leix");
								if(k>0){
									bufferValus.append(",");
								}
								//可以进行编码验证和空验证
								String str= jiaoy(rs.getString(k,"zidmc"),rs.getString(k,"zhuanmSql"),rs.getString(k,"weik"),((Element)elZids.get(k)).getText(),leix);// 字段名称、字段编码、字段为空
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
									bufferValus.append("("+rs.getString(k,"zhuanmSql").replaceAll("%%", "'"+((Element)elZids.get(k)).getText()+"'")+")");//替换参数
								}else{//不识别的类型
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
						//删除操作：形成删除sql
						 sql=
							"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"="+elGuanlid.getText();
						 int count=con.getDelete(sql);
						 if(count==-1){//"-1,003,"+str+""删除"+count+"行数据
							 message=error007+"出错位置："+j+"命令";;
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
				message=error001+"出错位置："+j+"命令";;
				return message;
			}catch(JDOMException e1){
				con.rollBack();
				message=error002+"出错位置："+j+"命令";;
				return message;
			}catch(SQLException e2){
				con.rollBack();
				message=error003+"出错位置："+j+"命令";;//　
				return message;
			}catch(Exception e3){
				con.rollBack();
				message=error014+"出错位置："+j+"命令";;//　
				return message;
			}
			finally{
				  con.Close();
				  Xiejsrzb(diancxxb_id.toString(),elShujxy.getText(),elGuanlid.getText(),message+"事务号："+elShiwh.getText(),elCaoz.getText(),elRenwrq.getText(),eldiancxxb_id.getText());
			}
			con.commit();
			return error000;//返回成功
				
}
	private  String xmlToDb( byte[ ] XMLData){
		String message=error000;
		JDBCcon con=new JDBCcon();
		Element elShujxy=null;
		Element elGuanlid=null;
		Element elCaoz=null;
		Element elRenwrq=null;
		Element eldiancxxb_id=null;
		
		//0,解析xml取出数据1,取出格式设置2,构造table、values
		try{
			//0
				String xmlstr= new String(XMLData,"GB2312");
				StringReader sr = new StringReader(xmlstr);
//				System.out.println(xmlstr);
				InputSource is = new InputSource(sr);
				Document doc = (new SAXBuilder()).build(is);//解读每一个数据帧文件
				Element root = doc.getRootElement();
//				System.out.print(root.getName());
				List Table = root.getChildren();

				 elShujxy = (Element) Table.get(0);//数据协议
				 elCaoz = (Element) Table.get(1);//操作
				 elGuanlid = (Element) Table.get(2);//删除主键
				 elRenwrq = (Element) Table.get(3);//任务日期
				 eldiancxxb_id = (Element) Table.get(4);//电厂信息表
				 
				StringBuffer bufferTable=new StringBuffer();
				StringBuffer bufferValus=new StringBuffer();
			    String sql="select renwmc,jiekjspzb.zidmc,leix,jiekbmzhpzb.zidmc zhuanmsql,zhujmc,weik\n" + 
				"from jiekjspzb,jiekbmzhpzb\n" + 
				"where  jiekjspzb.bianm=jiekbmzhpzb.bianm(+) and renwmc='"+elShujxy.getText()+"' order by jiekjspzb.id";
//				System.out.println(sql); 
			    ResultSetList rs=con.getResultSetList(sql);
				if(elCaoz.getText().equals("0")){//0:增加1:删除 如果为增加操作
					//插入：1,生成表头(id,mingc...)2,读取数据记录组织成valus3,形成插入sql
					//表头在读配置信息时顺便生成
					//先删除后增加
					//增加时使用事务删除时不使用事务
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
					if( Table.size()>5&&(((Element) Table.get(5)).getChildren()).size()!=rs.getRows()){//发送端与接收端字段数量配置不一致
						 message=error013;
						 return message;
					}
					con.setAutoCommit(false);
					sql=
						"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"='"+elGuanlid.getText()+"'";
					con.getDelete(sql);
					for (int i = 5; i < Table.size(); i++) {//数据记录(一般只有一个)
						Element shujjl = (Element) Table.get(i);
						List elZids= shujjl.getChildren();
						bufferValus.setLength(0);
						bufferValus.append("(");
						for(int k=0;k<elZids.size();k++){//2构造values,验证数据（编码验证和空验证）
							//根据字段配置的类型处理响应字段数据
							String leix = rs.getString(k,"leix");
							if(k>0){
								bufferValus.append(",");
							}
							//可以进行编码验证和空验证
							String str= jiaoy(rs.getString(k,"zidmc"),rs.getString(k,"zhuanmSql"),rs.getString(k,"weik"),((Element)elZids.get(k)).getText(),leix);// 字段名称、字段编码、字段为空
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
								bufferValus.append("("+rs.getString(k,"zhuanmSql").replaceAll("%%", "'"+((Element)elZids.get(k)).getText()+"'")+")");//替换参数
							}else{//不识别的类型
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
					//删除操作：形成删除sql
					 sql=
						"delete from "+elShujxy.getText()+" where "+rs.getString(0,"zhujmc")+"="+elGuanlid.getText();
					 int count=con.getDelete(sql);
					 if(count==-1){//"-1,003,"+str+""删除"+count+"行数据
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
			message=error003;//　
			return message;
		}catch(Exception e3){
			con.rollBack();
			message=error014;//　
		}
		finally{
			  con.Close();
			  Xiejsrzb(diancxxb_id.toString(),elShujxy.getText(),elGuanlid.getText(),message,elCaoz.getText(),elRenwrq.getText(),eldiancxxb_id.getText());
		}
		return message;
}
	private String jiaoy(String zidmc,String zhuanmsql,String zidwk,String date,String leix) throws SQLException{//先进行空验证再进行编码验证
		String resultStr="";
		if((zidwk.equals("")||zidwk==null)&&(date.equals("")||date==null)){//如果不允许为空,数据还为空则返回错误
			resultStr=zidmc+"不允许为空";
			return resultStr;
		}
		JDBCcon con=new JDBCcon();
		try{
		if(!zhuanmsql.equals("")&&!date.equals("")&&date!=null){//如果是有转码，并有转码数据
//			System.out.println(zhuanmsql+"'"+date+"'");
			if("bianm".equals(leix)){//
				ResultSetList rs=con.getResultSetList(zhuanmsql+"'"+date+"'");
				if(!rs.next()){
					resultStr=zidmc+"字段的"+date+"编码不能识别";
					return resultStr;
				}
			}else{//decode
				if(zidwk.equals("")){//不如果是必填项才校验
					ResultSetList rs=con.getResultSetList(zhuanmsql.replaceAll("%%", "'"+date+"'"));
					if(rs.next()){
						if(rs.getString(0)==null||rs.getString(0).equals("")){
							resultStr=zidmc+"字段的"+date+"在数据库中找不到对应的值，也就是说在远程数据库中昨日库存为空";
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
	private int yanz(String user,String password,StringBuffer diancxxb_id){//验证账户
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
						v=1;//如果用户存在，并且密码正确返回1
					}else{
						v=0;//密码错误
					}
			}else{
				v=-1;//不存在该用户
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
	private void Xiejsrzb(String zhangh_id,String renw,String renwbs,String message,String caoz,String renwrq,String diancxxb_id){//写接收日志表
		String[]dat=message.split(",");
//		if(dat[0].equals("-1")){//如果为错误
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
				return new String[]{"该电厂没有入口地址！"};
			}
			Service service = new Service();
			Call call = (Call) service.createCall();//远程调用者
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINTADDRESS));
			call.setOperationName("sqlExe");
			call.addParameter("sqls", XMLType.SOAP_ARRAY,ParameterMode.IN);
			call.addParameter("isTransaction", XMLType.SOAP_BOOLEAN,ParameterMode.IN);
			call.setReturnType(XMLType.SOAP_ARRAY);
			resultArry=(String[])call.invoke(new Object[] {sqls,Boolean.valueOf(isTransaction)});//如果执行成功返回true
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			return new String[]{"出错啦！"};
		}finally{
			con.Close();
		}
		return resultArry;
	}
}