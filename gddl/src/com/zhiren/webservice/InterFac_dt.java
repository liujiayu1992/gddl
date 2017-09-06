package com.zhiren.webservice;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.zhiren.common.En_Decrypt_ZR;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

/**
 * @author ��������
 * ������	InterFac		����	����
	������1��	��Ӧ����	request	
	������1��	�û���	usr	String
	������2��	����	password	String
	������3��	������	task	String
	����ֵ��	�ɹ�/ʧ��1,ʧ��2		String
 
	������2��	��Ӧ����	requestall	 
	������1��	�û���	usr	String
	������2��	����	password	String
	����ֵ��	�ɹ�/ʧ��1,ʧ��2		String
 *
 */
//���е�Ͷ���������𣺲�ʹ�õ糧��Ϣ����Ϊid��Ψһ��CreateSql��requestall��request��sql��diancxxb_idȥ��
public class InterFac_dt  {
	private static final String error006="-1,006,Զ��webservice�������δ֪���⣬������webservice����ʧ�ܡ���";
	private static final String error101="-1,101,��������ʧ�ܣ���url���ܶ�λ������";
	private static final String error102="-1,102,��Դ��λ��url�����������";
	private static final String error103="-1,103,����������sql���ִ��ʱ����";//
	private static final String error104="-1,104,����web������ʧ�ܣ�";
	private static final String error105="-1,105,����λ���쳣��";
	
	private static final String rizPath="c:/fasrz";// ������־·��
	private String user;
	private String password;

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }

    private String endpointAddress;

//	
	public InterFac_dt() {
		super();
		// TODO �Զ����ɹ��캯�����
		user=MainGlobal.getXitxx_item("�ӿ���","�ӿ��ϴ��û���", "197", " ");
		password=MainGlobal.getXitxx_item("�ӿ���","�ӿ��ϴ�����", "197", " ");
		endpointAddress=MainGlobal.getXitxx_item("�ӿ���","��ڵ�ַ", "197", " ");
	}
	public void request(String task){////Զ�̵��á����ض�ʱ����
		String renwmc="",renllx="",id="",shujjl="",tiaoj="",xml="",id0="",tem1="";
		String message="";
		//ɾ���鿴�ļ��е������ļ�
		File file=new File(rizPath);
		if(!file.exists()){
			file.mkdir();
		}
		File[] files=file.listFiles();
		for(int j=0;j<files.length;j++){
			files[j].delete();
		}
		JDBCcon con=new JDBCcon();
		String sql=
			"select j.id id0,j.renwmc,j.renllx, j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" +//||' '||p.renwbs||''''||changbb.id_jit||j.renwbs||''''
			"from jiekrwb j,jiekfspzb p \n" + 
			"where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc  and j.mingllx=p.mingllx and j.renwmc='"+task+"' and shiwh is  null order by id0";
		ResultSet rs=con.getResultSet(sql);
		Service service = new Service();
		try{
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renllx=rs.getString("renllx");
				id=rs.getString("id");//����Ψһ�����ʶid
				id0=rs.getString("id0");//����id
				shujjl=rs.getString("shujjl");
				tiaoj=rs.getString("tiaoj");
				shujjl=shujjl.replaceAll("%%", tiaoj);
				 if(renllx.equals("2")){//��Ϊ����ǰ��Ҫɾ���������޸���������һ���Ķ�����ɾ��������
				    	renllx="0";
				    }
					xml=CreateXml(renwmc,renllx,id,	con.getResultSet(shujjl));
					Call call = (Call) service.createCall();//Զ�̵�����
					java.net.URL url=new java.net.URL(endpointAddress);
					call.setTargetEndpointAddress(url);
					call.setOperationName("incept");
					call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("XMLData", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
					call.setReturnType(XMLType.SOAP_STRING);
					tem1=String.valueOf(call.invoke(new Object[] {user,password,xml.getBytes()}));//д��־
					Xierz(tem1,id0);
//				}
				
			}
			StringBuffer id0s=new StringBuffer();
			List sqls=CreateSql(id0s);
			for (int ii=0;ii<sqls.size();ii++){
				Call call = (Call) service.createCall();//Զ�̵�����
				call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
				call.setOperationName("execute");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				
				String data=String.valueOf(call.invoke(new Object[] {user,password,sqls.get(ii).toString()}));//д��־
//				String in=sqls.get(ii).toString().substring(sqls.get(ii).toString().lastIndexOf("in"));
				Xierzexe(data,id0s.toString());
			}
		}catch(SQLException e){
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//����urlʱ����
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//Զ��δ֪������������No route to host: connect
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// �������
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			message=error104;
			e.printStackTrace();
		}finally{
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	public  void requestall() {////Զ�̵��á����ض�ʱ����
		String renwmc="",renllx="",id="",shujjl="",tiaoj="",xml="",id0="",tem1="";
		String message="";
		//ɾ���鿴�ļ��е������ļ�
		File file=new File(rizPath);
		if(!file.exists()){
			file.mkdir();
		}
		File[] files=file.listFiles();
		for(int j=0;j<files.length;j++){
			files[j].delete();
		}
		JDBCcon con=new JDBCcon();
		JDBCcon con1=null;
		String sql=
			"select j.id id0,j.renwmc,j.renllx,j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj\n" +//||' '||p.renwbs||''''||changbb.id_jit||j.renwbs||''''
			"from jiekrwb j,jiekfspzb p \n" + 
			"where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.mingllx=p.mingllx and shiwh is  null order by id0";
		ResultSet rs=con.getResultSet(sql);
		Service service = new Service();
		try{
			while(rs.next()){//a
				renwmc=rs.getString("renwmc");
				renllx=rs.getString("renllx");
				id=rs.getString("id");//����Ψһ�����ʶid
				id0=rs.getString("id0");//����id
				shujjl=rs.getString("shujjl");
				tiaoj=rs.getString("tiaoj");
				
				shujjl=shujjl.replaceAll("%%", tiaoj);
				con1=new JDBCcon();
				 if(renllx.equals("2")){//��Ϊ����ǰ��Ҫɾ���������޸���������һ���Ķ�����ɾ��������
				    	renllx="0";
				    }
					xml=CreateXml(renwmc,renllx,id,	con1.getResultSet(shujjl));
					Call call = (Call) service.createCall();//Զ�̵�����
					java.net.URL url=new java.net.URL(endpointAddress);
					call.setTargetEndpointAddress(url);
					call.setOperationName("incept");
					call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("XMLData", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
					call.setReturnType(XMLType.SOAP_STRING);
					tem1=String.valueOf(call.invoke(new Object[] {user,password,xml.getBytes()}));//д��־
					Xierz(tem1,id0);
//				}
				con1.Close();
			}
			StringBuffer id0s=new StringBuffer();
			List sqls=CreateSql(id0s);
			for (int ii=0;ii<sqls.size();ii++){
				Call call = (Call) service.createCall();//Զ�̵�����
				call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
				call.setOperationName("execute");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("sql", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				
				String data=String.valueOf(call.invoke(new Object[] {user,password,sqls.get(ii).toString()}));//д��־
//				String in=sqls.get(ii).toString().substring(sqls.get(ii).toString().lastIndexOf("in"));
				Xierzexe(data,id0s.toString());
			}
		}catch(SQLException e){
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//����urlʱ����
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//Զ��δ֪������������No route to host: connect
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// �������
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			message=error104;
			e.printStackTrace();
		}finally{
			if(con1!=null){
				con1.Close();
			}
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	private  String CreateXml(String shujxy,String caoz,String guanlId,ResultSet rs){
		String xmlAray="";//û�м�¼�ͻ����쳣����������
		// TODO �Զ����ɷ������
		Element root = new Element("����֡");
		Document document = new Document(root);
		
//		root.setAttribute(new Attribute("vin", "123fhg5869705iop90"));
		root.addContent(new Element("����Э��").addContent(shujxy));
		root.addContent(new Element("����").addContent(caoz));
		root.addContent(new Element("����").addContent(guanlId));
		try {  
			while(rs.next()){
				Element elShujjl = new Element("���ݼ�¼");
				root.addContent(elShujjl);
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
					elShujjl.addContent(new Element(rs.getMetaData().getColumnName(i)).addContent(rs.getString(i)));
					
				}
			}
			File file=new File(rizPath) ;
			FileWriter writer = new FileWriter(file.getAbsolutePath()+"/"+shujxy+guanlId+caoz+".xml");
			XMLOutputter outputter = new XMLOutputter();   
			Format format=Format.getPrettyFormat(); 
			format.setEncoding("gb2312"); 
//			format.setOmitDeclaration(true);
			outputter.setFormat(format);
			outputter.output(document, writer);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			outputter.output(document, bo);
			xmlAray=bo.toString();
			//xmlAray=bo.toByteArray();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	
		return xmlAray;
	}
	public  void requestallTrans(){//ִ�����е�����
		//
		String xml="",tem1="",shiwh0="",shiwh1="",tiaoj="",shujjl="";
		byte[] xml_by=null;
		String message="";
		//ɾ���鿴�ļ��е������ļ�
		File file=new File(rizPath);
		if(!file.exists()){
			file.mkdir();
		}
		File[] files=file.listFiles();
		for(int j=0;j<files.length;j++){
			files[j].delete();
		}
		//��ת�������񼯺�
		List transSet=new ArrayList();
		JDBCcon con=new JDBCcon();
		JDBCcon con1=null;
		TransBean tranbean=null;
		String sql=
			"select j.id id0,j.renwmc,j.renllx,changbb_id,to_char(renwsj,'yyyy-mm-dd hh24:mi:ss')renwsj,j.renwbs id,p.renwsql  shujjl,p.renwbs||''''||j.renwbs||''''  tiaoj,shiwh\n" +//||' '||p.renwbs||''''||changbb.id_jit||j.renwbs||''''
			"from jiekrwb j,jiekfspzb p \n" + 
			"where j.renllx!=3 and (zhixzt=0 or zhixzt=-1) and j.renwmc=p.renwmc and j.mingllx=p.mingllx  and shiwh is not null order by shiwh,id0";
		ResultSetList rs=con.getResultSetList(sql);
		Service service = new Service();
		try{
			List comList=null;
			while(rs.next()){//a
				shiwh1=rs.getString("shiwh");//��ǰ����������
				
				//����
				CommonBean comBean=new CommonBean();
				comBean.setCaoz(rs.getString("renllx"));//ÿ����¼����һ������
				comBean.setDiancxxb_id(rs.getString("changbb_id"));
				comBean.setRenwsj(rs.getString("renwsj"));
				comBean.setShujxy(rs.getString("renwmc"));
				comBean.setZhuj(rs.getString("id"));
				List shujList=new ArrayList();
				shujjl=rs.getString("shujjl");
				tiaoj=rs.getString("tiaoj");
				shujjl=shujjl.replaceAll("%%", tiaoj);
				loadDateRec(shujjl,shujList);//��������shujjl��shujList��
				comBean.setShujjl(shujList);
				if(shiwh1.equals(shiwh0)){//�����Ⱦ�˵����ͬһ������
					comList.add(comBean);//�����������������
				}else{//������ÿ������ŵı仯������һ�������
					tranbean= new TransBean();
					tranbean.setShiwbh(shiwh1);
					comList=new ArrayList();//��������
					comList.add(comBean);//����
					tranbean.setListCommon(comList);
					transSet.add(tranbean);
				}
				shiwh0=shiwh1;//�ϴ�����
			}
			//ִ�����񼯺�transSet
			for(int jj=0;jj<transSet.size();jj++){
				Call call = (Call) service.createCall();//Զ�̵�����
				java.net.URL url=new java.net.URL(endpointAddress);
				call.setTargetEndpointAddress(url);
				call.setOperationName("incept");
				call.addParameter("user", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("password", XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("XMLData", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				tranbean=(TransBean)transSet.get(jj);
				xml=tranbean.CreateXml(rizPath);
				xml_by=En_Decrypt_ZR.encryptByDES(xml.getBytes());//����
				tem1=String.valueOf(call.invoke(new Object[] {user,password,xml_by}));//д��־
				Xierztrans(tem1,tranbean.getShiwbh());
			}
		}catch(SQLException e){
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//����urlʱ����
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//Զ��δ֪������������No route to host: connect
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// �������
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			message=error105;
			e.printStackTrace();
		}
		finally{
			if(con1!=null){
				con1.Close();
			}
			con.Close();
			if(!message.equals("")){
				Xierztrans(message,tranbean.getShiwbh());
			}
		}
		return ;
	}
	
//	�ļ����俪ʼ
	public  void requestFile() {////Զ�̵��á����ض�ʱ����
		String renwmc="",filePath="",id_jit="",renllx="",id0="",tem1="";
		Object[] parmeter=null;
		String message="";
		JDBCcon con=new JDBCcon();
		String sql="SELECT J.ID ID0, J.RENWMC, J.RENLLX, J.CHANGBB_ID, J.MINGLCS\n" +
			"  FROM JIEKRWB J\n" + 
			" WHERE J.RENLLX != 3\n" + 
			"   AND (ZHIXZT = 0 OR ZHIXZT = -1)\n" + 
			"   AND J.MINGLLX = 'file'\n" + 
			" ORDER BY ID0";
		
	    ResultSet rs=con.getResultSet(sql);
		Service service = new Service();
		try{
			while(rs.next()){
				renwmc=rs.getString("renwmc");
				renllx=rs.getString("renllx");
				id0=rs.getString("id0");//����id
				filePath=rs.getString("minglcs");//�ļ�����·�����������ϵľ���·����
				id_jit=rs.getString("changbb_id");//��λID
				//����filePath��renwmc��id_jit,renllx������ļ��洢���ĸ�����
				parmeter=parmeterFilestroe(filePath,renwmc,id_jit,renllx);//��������0:path1:name2:caoz3��������true;
				if(parmeter[3].equals("true")){//���Ϊ��˵���߱����ϴ�����
					Call call = (Call) service.createCall();//Զ�̵�����
					java.net.URL url=new java.net.URL(endpointAddress);
					call.setTargetEndpointAddress(url);
					call.setOperationName("fileStroe");
					call.addParameter("path", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("name", XMLType.SOAP_STRING,ParameterMode.IN);
					call.addParameter("file", XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
					call.addParameter("caoz", XMLType.SOAP_STRING,ParameterMode.IN);
					call.setReturnType(XMLType.SOAP_STRING);
					tem1=String.valueOf(call.invoke(new Object[] {parmeter[0],parmeter[1],fileByte,parmeter[2]}));//д��־
				}else{
					tem1="-1,"+"106,"+parmeter[3].toString();//�ͻ��˴���
				}
				Xierz(tem1,id0);////"1,'000','���ճɹ�'";
			}
			
		}catch(SQLException e){//����жϵĴ���
			e.printStackTrace();
			message=error103;
		}catch(MalformedURLException e){//����urlʱ����
			message=error102;
			e.printStackTrace();
		}catch(RemoteException e){//Զ��δ֪������������No route to host: connect
			System.out.println(e.getMessage()) ;
			if(e.getCause()!=null&&e.getCause().getMessage().indexOf(":")!=-1&&e.getCause().getMessage().substring(0, e.getCause().getMessage().indexOf(":")).equals("No route to host")){// �������
				message=error101;
			} else{
				message=error006;
			}
		}catch(ServiceException e){
			message=error104;
			e.printStackTrace();
		}catch(Exception e){
			message=error105;
			e.printStackTrace();
		}finally{
			con.Close();
			if(!message.equals("")){
				Xierz(message,id0);
			}
		}
		return ;
	}
	private byte[] fileByte=null;
	private Object[] parmeterFilestroe(String filePath,String renwmc,String id_jit,String renllx){
		//filePath:D:\Tomcat 5.0\webapps\ftp\20081210.dmp
		//0:����+�糧��pandtb��108��1:name2:caoz3,err
		Object[] temArry=new String[4];
		InputStream is = null;
		
		//���ļ�ת���ɶ���������byte
		ByteArrayOutputStream byteOut=null;

		if (filePath != null&& !filePath.equals("")) {
			try {
				File file=new File(filePath);
				//0:����+�糧��pandtb��108��
				temArry[0]=renwmc+","+id_jit;
				
				//1:�ϴ����ļ�����
				//��ȷ���ܷ�ȡ����ȷ���ļ���
				temArry[1]=filePath.substring(filePath.lastIndexOf("\\")+1);
				
				is = new FileInputStream(file);
				byteOut=new ByteArrayOutputStream();
				byte[] buff = new byte[1024];
				while (is.read(buff) != -1) {
					byteOut.write(buff);
				}
				fileByte=byteOut.toByteArray();//���ض���������byte
				temArry[2]=renllx;
			} catch (Exception e) {
				e.printStackTrace();
//				System.out.print("�ϴ�����");
				temArry[3]=e.getMessage();
				return temArry;
			} finally {
				try {
					byteOut.close();
					is.close();
				} catch (Exception e) {
					
				}
			}
		}else{
			temArry[3]="·��Ϊ�գ�";
			return temArry;
		}
		temArry[3]="true";
		return temArry;
	}
//	�ļ��������
	
	
	private void loadDateRec(String sql,List list) throws SQLException{
		JDBCcon con=new JDBCcon();
		ResultSet rs=con.getResultSet(sql);
		
		while(rs.next()){//���ݼ�¼����
			List zidList=new ArrayList();
			for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
				zidList.add(new String[]{rs.getMetaData().getColumnName(i),rs.getString(i)});
			}
			list.add(zidList);
		}
	}
	private   List CreateSql(StringBuffer id0s){
		List resultSql=new ArrayList();
		JDBCcon con=new JDBCcon();
		String ids="";
		String sql=
			"select j.id,p.renwsql biaom,p.gengxy,j.minglcs gengxyz,p.renwbs,j.renwbs renwbsz\n" +
			"from jiekrwb j,jiekfspzb p\n" + 
			"where j.renwmc=p.renwmc and j.mingllx=p.mingllx and j.renllx=3 and zhixzt=0 " +
			" order by biaom,gengxy,gengxyz,renwbs";
		ResultSet rs=con.getResultSet(sql);
		String biaom="",gengxy="",gengxyz="",renwbs="",renwbsz="";
		String biaom_p="",gengxy_p="",gengxyz_p="",renwbs_p="";
		String in="(";
		int kk=0;
		try{
			while(rs.next()){
				ids=ids+rs.getString("id")+",";
				biaom=rs.getString("biaom");
				gengxy=rs.getString("gengxy");
				gengxyz=rs.getString("gengxyz");
				renwbs=rs.getString("renwbs");
				renwbsz=rs.getString("renwbsz");
				if(kk==0||(biaom.equals(biaom_p)&&gengxy.equals(gengxy_p)&&gengxyz.equals(gengxyz_p)&&renwbs.equals(renwbs_p))){//�����ǰֵ��һ����Ȼ��һ��ֵ��˵��Ϊͬһ������
					//׷��in��ģ���������
					biaom_p=biaom;
					gengxy_p=gengxy;
					gengxyz_p=gengxyz;
					renwbs_p=renwbs;
					
					if(kk==0){
						in+=renwbsz;
					}else{
						in+=","+renwbsz;
					}
					kk++;
				}else{ //����������һ���·��顣
					in+=")";
					resultSql.add("update "+biaom_p+" set "+gengxy_p+"'"+gengxyz_p+"' where "+renwbs_p+in);
					//��ʼ����������
					kk=1;
					in="(";
					biaom_p=biaom;
					gengxy_p=gengxy;
					gengxyz_p=gengxyz;
					renwbs_p=renwbs;
					in+=renwbsz;
				}
			}
			//���һ������
			if(kk!=0){
				in+=")";
				resultSql.add("update "+biaom+" set "+gengxy+"'"+gengxyz+"' where "+renwbs+in);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		if(!ids.equals("")){
			id0s.append(ids.substring(0, ids.lastIndexOf(",")));
		}
		return resultSql;
	}
	private void Xierz(String data,String id0){//д��־
		//"1,'000','���ճɹ�'";
		String[]dat=data.split(",");
		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		
		"where id='"+id0+"'";
		con.getUpdate(sql);
		con.Close();
	}
	private void  Xierztrans(String data,String id0){//д��־
		//"1,'000','���ճɹ�'";
		String[]dat=data.split(",");
		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		
		"where shiwh='"+id0+"'";
		con.getUpdate(sql);
		con.Close();
	}
	private void Xierzexe(String data,String in){//ִ����־
		//"1,'000','���ճɹ�'";
		String[]dat=data.split(",");
		JDBCcon con=new JDBCcon();
		String sql=
		"update jiekrwb\n" +
		"set jiekrwb.zhixzt="+dat[0]+",\n" + 
		"cuowlb='"+dat[1]+"',\n" + 
		"zhixbz='"+dat[2]+"',\n" + 
		"zhixsj=sysdate\n" + 
		"where id in("+in+")";
		con.getUpdate(sql);
		con.Close();
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
	
	public String[] getJiecxx_Sj(String Type) throws Exception {
//		˵������д����2009-02-23
//		��д��Ա��zsj
//		��ҪĿ�ģ�������Ϣ����ͬ��
//		��Ҫ���ܣ��¼���λͨ�����øú��������ϼ���λ��jws�ӿڣ���������
//		�ɴ�/gddl/context/InterCom_dt.jws�в�ѯ����Ӧ������
		String[] resultData=null;
		String ErrorMessage="";
		String result="";
//			
		if(!endpointAddress.equals("")){
//			�õ����ϼ���λ��ڵ�ַ
			
			Service ser=new Service();
			Call call = null;
			try {
				
//				���ýӿڳ���
				call = (Call) ser.createCall();
				call.setTargetEndpointAddress(new java.net.URL(endpointAddress));
				call.setOperationName("getShangj_Jicxx");
				call.addParameter("Type", XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				result=String.valueOf(call.invoke(new Object[] {Type}));
				
//				������ֵת��Ϊ��������
				if(!result.equals("")){
					
					resultData=result.split("~%&");
				}
				
			}catch (ServiceException e) {
				
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
				ErrorMessage=error102;
				resultData=new String[]{"����"+ErrorMessage};
				
			}catch (RemoteException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
				ErrorMessage=error006;
				resultData=new String[]{"����"+ErrorMessage};
				
			} //���ýӿ�
			
		}else{
			
			ErrorMessage="û�������ϼ���λ����ڵ�ַ";
			resultData=new String[]{"����"+ErrorMessage};
		}
		
		return resultData;
	}
//	����һ��sql����ܹ��������Ľ���ַ������ַ�����ʽ 1123 ��20
	//������֤����
	public String getSqlString(String sql){
		String reslutStr="";
		JDBCcon con=new JDBCcon();
		ResultSet rs=con.getResultSet(sql);
		try{
			int Colucount=rs.getMetaData().getColumnCount();
			if(rs.next()){//�涨ֻ����һ������ֵ
				for(int i=1;i<=Colucount;i++){
					String tem=rs.getString(i);
					if(i==1){
						reslutStr+=(tem==null?"":tem);
					}else{
						reslutStr+=","+(tem==null?"":tem);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return reslutStr;
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
