package com.zhiren.webservice.iccardinterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.tapestry.IPage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.En_Decrypt_ZR;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;

public class ICCardLogin {
	
	public static String Byt2Str(byte[] bt) {
		String ss=null;
		char[] chars = new char[bt.length];
		for(int i=0;i<bt.length;i++) {
			chars[i] = (char)bt[i];
		}
		ss = String.valueOf(chars);
		return ss;
	}
	/**
	 * 
	 * @param userName
	 * @param pwd
	 * @return 
	 * 		   登录成功 renyb_id 
	 * 		   登录失败 -1
	 */
	public static long LoginUser(String userName,String pwd) {
		
		JDBCcon con = new JDBCcon();
		long Renyid = 0;
		
		ResultSetList rsl = con.getResultSetList(
				"select r.id renyid,r.quanc renymc,d.jib renyjb\n" +
				"from renyxxb r,diancxxb d where r.diancxxb_id = d.id\n" + 
				"and r.mingc = '"+userName+"' and r.zhuangt = 1");
		if(rsl == null) {
			return -1;
		}
		if (rsl.next()) {
			Renyid = rsl.getLong(0);
			try {
				DataBassUtil dbu = new DataBassUtil();
				ByteArrayOutputStream out;
				out = dbu.GetBlob("renyxxb", "mim", Renyid);
				byte[] bytEnPwd = out.toByteArray();
				for (int i=0;i<bytEnPwd.length;i++)
					System.out.println(bytEnPwd[i]);
				
//				String ww = Byt2Str(bytEnPwd);
//				System.out.println(ww);
				byte[] bytDePwd = En_Decrypt_ZR.decryptByDES(bytEnPwd, En_Decrypt_ZR.keybyte, En_Decrypt_ZR.ivbyte);
				String strDePwd = En_Decrypt_ZR.Byt2Str(bytDePwd);
				if(!strDePwd.equals(pwd)) {
					//密码验证没有通过
					return -1;
				} 
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Renyid;	
	}
	
	
	public static long PasswordRight(String username,String pas){
		JDBCcon con = new JDBCcon();
		long Renyid = -1;
		ResultSetList rsl = con.getResultSetList(
				"select r.id renyid,r.quanc renymc,d.jib renyjb\n" +
				"from renyxxb r,diancxxb d where r.diancxxb_id = d.id\n" + 
				"and r.mingc = '"+username+"' and r.zhuangt = 1");
		try{
		if (rsl.next()){
			Renyid = rsl.getLong("renyid");
			DataBassUtil dbu = new DataBassUtil();
			//ByteArrayOutputStream out;
			dbu.UpdateBlob("renyxxb", "mim", Renyid, pas,true);
			Renyid = LoginUser(username,pas);
			return Renyid;
			//byte[] bytEnPwd = out.toByteArray();
		}
			return Renyid;
		}catch(SQLException e){
			
			e.printStackTrace();
			
		}catch(IOException e){
			
			e.printStackTrace();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return Renyid;

	}
}
