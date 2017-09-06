package com.zhiren.webservice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.zhiren.common.DateUtil;
import com.zhiren.common.En_Decrypt_ZR;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.webservice.RandomGUID;

public class Interface {
	private static String SearchGuid(JDBCcon con,String username,String ip){
		StringBuffer sb = new StringBuffer();
		sb.append("select guid from interfaceLog where ");
		sb.append("ip='").append(ip).append("' and username='").append(username).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			return rsl.getString("guid");
		}else {
			return null;
		}
	}
	
	private static boolean ValidateGuid(JDBCcon con, String guid) {
		StringBuffer sb = new StringBuffer();
		sb.append("select username from interfaceLog where guid='")
		.append(guid).append("' and riq=").append(DateUtil.FormatOracleDate(new Date()));
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			return false;
		}
		return rsl.next();
	}
	
	private static String ValidateUser(JDBCcon con, String username, byte[] password) {
		StringBuffer sb = new StringBuffer();
		byte[] pwd;
		try {
			pwd = En_Decrypt_ZR.decryptByDES(password);
			String pwdstr = new String(pwd,"GB2312");
			sb.append("select password from InterfaceUser where ")
			.append("username='").append("'").append(" and zhuangt=1");
			String _password = "";
			ResultSetList rsl = con.getResultSetList(sb.toString());
			if(rsl.next()) {
				_password = rsl.getString("password");
				if(pwdstr.equals(_password)) {
					return null;
				}else {
					return String.valueOf(SysConstant.WS_EC_ValiPwdfail);
				}
			}else {
				return String.valueOf(SysConstant.WS_EC_ValiUserfail);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return String.valueOf(SysConstant.WS_EC_IOExp);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			return String.valueOf(SysConstant.WS_EC_En_Decryptfail);
		}
	}
	
	private static String CreateGuid(JDBCcon con, String username, String ip) {
		StringBuffer sb = new StringBuffer();
		RandomGUID myGUID = new RandomGUID();
		String GUID = myGUID.toString();
		sb.append("insert into InterfaceLog (ip,riq,username,guid) values(")
		.append("'").append(ip).append("',").append(DateUtil.FormatOracleDate(new Date()))
		.append(",'").append(username).append("','").append(GUID).append("'");
		int flag = con.getInsert(sb.toString());
		if(flag == -1) {
			return null;
		}
		com.zhiren.common.WriteLog.writeInfoLog("得到新GUID:"+ GUID);
		return GUID;
	}
	
	public static String TransStart(String username,byte[] password){
		MessageContext mc = MessageContext.getCurrentContext();       
		HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);       
		String RemoteAddr = request.getRemoteAddr();
		if(RemoteAddr == null || "".equals(RemoteAddr)){
			RemoteAddr = "未知";
		}
		com.zhiren.common.WriteLog.writeInfoLog("********身份验证过程开始********");
		com.zhiren.common.WriteLog.writeInfoLog("来访者IP:"+ RemoteAddr);
		JDBCcon con = new JDBCcon();
		String GUID = "";
		String valiusercode = ValidateUser(con,username,password);
		if(valiusercode != null) {
			//如果用户名或密码不对
			com.zhiren.common.WriteLog.writeInfoLog("验证失败 来访者使用 用户名:"+username);
			com.zhiren.common.WriteLog.writeInfoLog("********身份验证过程结束********");
			con.Close();
			return valiusercode;
		}
		GUID = SearchGuid(con,username,RemoteAddr);
		if(GUID == null) {
			com.zhiren.common.WriteLog.writeInfoLog("数据库中无可用EXID 进入生成EXID");
			GUID = CreateGuid(con,username,RemoteAddr);
			if(GUID == null) {
				con.Close();
				return String.valueOf(SysConstant.WS_EC_CreateGuidfail);
			}
			con.Close();
			return GUID;
		}else {
			com.zhiren.common.WriteLog.writeInfoLog("验证成功 在库中查找返回GUID:"+ GUID);
			com.zhiren.common.WriteLog.writeInfoLog("********身份验证过程结束********");
			con.Close();
			return GUID;
		}
	}
	public static String TransData(String GUID, String DataType, byte[ ] XMLData, String CRC){
		MessageContext mc = MessageContext.getCurrentContext(); 
		HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST); 
		String RemoteAddr = request.getRemoteAddr();
		if(RemoteAddr == null || "".equals(RemoteAddr)){
			RemoteAddr = "未知";
		}
		JDBCcon con = new JDBCcon();
		if(!ValidateGuid(con, GUID)) {
			return String.valueOf(SysConstant.WS_EC_ValiGuidfail);
		}
		StringReader sr = null;
		FileOutputStream fos = null;
		try {
			byte[] MingWen;
			MingWen = En_Decrypt_ZR.decryptByDES(XMLData);
			String XMLdecryptStr;
			XMLdecryptStr = new String(MingWen,"GB2312");
			sr = new StringReader(XMLdecryptStr);
			InputSource is = new InputSource(sr);
			Document doc = (new SAXBuilder()).build(is);
			Format format = Format.getPrettyFormat();
			format.setEncoding("gb2312");
			XMLOutputter XMLOut = new XMLOutputter(format);
			XMLOut.getFormat();
			String filepath = MainGlobal.getXitsz("系统文件夹位置", "0", "C:/zhiren/")+"/webservice/receive";
			File receivePath = new File(filepath);
			if(!receivePath.exists()) {
				if(!receivePath.mkdirs()) {
					return String.valueOf(SysConstant.WS_EC_CreateFilefail);
				}
			}
			String FileName = DataType + DateUtil.Formatdate("yyyyMMddHHmmss", new Date())+".xml";
			File receiveFile = new File(receivePath,FileName);
			fos = new FileOutputStream(receiveFile);
			XMLOut.output(doc, fos);
			fos.close();
		} catch (JDOMException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
			String.valueOf(SysConstant.WS_EC_JDOMExp);
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			String.valueOf(SysConstant.WS_EC_FileNotFound);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			String.valueOf(SysConstant.WS_EC_IOExp);
		} catch (Exception e2) {
			// TODO 自动生成 catch 块
			e2.printStackTrace();
			String.valueOf(SysConstant.WS_EC_En_Decryptfail);
		} finally {
			sr.close();
		}
		return String.valueOf(SysConstant.WS_EC_Success);
	}
}

