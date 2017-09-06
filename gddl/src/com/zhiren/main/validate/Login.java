package com.zhiren.main.validate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.DateUtil;
import com.zhiren.common.En_Decrypt_ZR;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MD5;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
/*
 * 2009-05-06
 * 王磊
 * 设置系统查询默认开始、结束时间的初始化
 * 现版本:1.1.1.12
 */
/**
 * 作者：夏峥
 * 时间：2012-12-11
 * 适用范围：国电电力大开热电
 * 描述：新增WINDOWS_CA认证
 * 		使用参数进行配置MainGlobal.getXitxx_item("登陆", "使用WINDOWS_CA认证（大开专用）","0", "否")
 */
 
public class Login {

	public static final String ErrorPageName = "ErrorPage";
	
	public Login() {
		
	}
	
	public static String ValidateAdmin(IPage ipage) {
		Visit visit = (Visit)ipage.getVisit();
		IRequestCycle cycle = ipage.getRequestCycle();
		String userName = "";
		String pwd = "";
//		取得请求中的用户名
		userName = cycle.getRequestContext().getParameter("user") == null ? ""
				: cycle.getRequestContext().getParameter("user");
//		取得请求中的密码
		pwd = cycle.getRequestContext().getParameter("pwd") == null ? ""
				: cycle.getRequestContext().getParameter("pwd");
		if(userName.equals(MainGlobal.getSuperUserName()) 
					&& pwd.equals(MainGlobal.getSuperUserPWD())){
			setVisitInfo(visit,-10000);
			return ipage.getPageName();
		}else {
			if(visit.getRenyID() == -10000) {
//					&& userName.equals("") && pwd.equals("")) {
				return ipage.getPageName();
			}
		}
		((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_illLogin);
		return ErrorPageName;
	}
	
	public static String ValidateLogin(IPage ipage) {
		Visit visit = (Visit)ipage.getVisit();
		IRequestCycle cycle = ipage.getRequestCycle();
		String pageName = ipage.getPageName();
		String userName = "";
//		取得请求中的用户名
		userName = cycle.getRequestContext().getParameter("user") == null ? ""
				: cycle.getRequestContext().getParameter("user");
		String pwd = "";
//		取得请求中的密码
		pwd = cycle.getRequestContext().getParameter("pwd") == null ? ""
				: cycle.getRequestContext().getParameter("pwd");
//		如果VISIT中没有注册过用户名 或 用户名已过期
		if(visit.getRenyID() == 0) {
			pageName = LoginUser(userName,pwd,ipage);
//		如果VISIT中注册过用户名			
		}else {
//			如果新传入的用户名不等于visit中固有的用户名
			if(!userName.equals("") && !userName.equals(visit.getRenymc())) {
				pageName = LoginUser(userName,pwd,ipage);
			}
		}
		return pageName;
	}
	
	public static String LoginUser(String userName,String pwd, IPage ipage) {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)ipage.getVisit();
		long Renyid = 0;
//		int Renyjb = 0;
//		String Renymc = "";
		try{
			
		
			if(userName.equals("")) {
	//			没有经过登陆页面注册 也没有用户名参数 返回错误页面
				((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_illLogin);
				return ErrorPageName;
			}else {
				ResultSetList rsl = con.getResultSetList(
						"select r.id renyid,r.quanc renymc,d.jib renyjb\n" +
						"from renyxxb r,diancxxb d where r.diancxxb_id = d.id\n" + 
						"and r.mingc = '"+userName+"' and r.zhuangt = 1");
				if(rsl == null) {
					((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_errdb);
					return ErrorPageName;
				}
				if(rsl.next()) {
					Renyid = rsl.getLong(0);
	//				Renyjb = Integer.parseInt(rsl.getString("renyjb"));
	//				Renymc = rsl.getString("renymc");
				}else {
	//				用户名不存在
					((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_noUser);
					return ErrorPageName;
				}
				DataBassUtil dbu = new DataBassUtil();
				try {
					ByteArrayOutputStream out = dbu.GetBlob("renyxxb", "mim", Renyid);
					byte[] bytEnPwd = out.toByteArray();
					byte[] bytDePwd = En_Decrypt_ZR.decryptByDES(bytEnPwd, En_Decrypt_ZR.keybyte, En_Decrypt_ZR.ivbyte);
					String strDePwd = En_Decrypt_ZR.Byt2Str(bytDePwd);
//					电厂网站跳入本系统(跨过登陆界面)使用MD5暗码传输
					if("是".equals(MainGlobal.getXitxx_item("登陆", "传入MD5_32位密码", String.valueOf("0"), "否"))){
						strDePwd = MD5.toMD5(strDePwd).toLowerCase();
					}
					
					//增加WINDOWS_CA认证配置
//					if("是".equals(MainGlobal.getXitxx_item("登陆", "使用WINDOWS_CA认证（大开专用）","0", "否"))){
//						LDAPAuthenticate ca=new LDAPAuthenticate();
//						if(!ca.ValidADUser(userName,pwd)){
////							密码验证没有通过
//							((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_errPwd);
//							return ErrorPageName;
//						}
//					}
//					如果不是用WINDOWS_CA认证
					else if (!strDePwd.equals(pwd) && !En_Decrypt_ZR.Byt2Str(bytDePwd).equals(pwd)) {
	//					密码验证没有通过
						((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_errPwd);
						return ErrorPageName;
					}
				} catch (SQLException e) {
					// SQL错误 返回未知错误页面
					e.printStackTrace();
					return ErrorPageName;
				} catch (IOException e) {
					// 读写错误 返回未知错误页面
					e.printStackTrace();
					return ErrorPageName;
				} catch (Exception e) {
					// 未知错误 返回未知错误页面
					e.printStackTrace();
					return ErrorPageName;
				}
				rsl = con.getResultSetList(
					"select distinct z.wenjwz\n" +
					"from ziyxxb z,zuqxb zq,renyzqxb r\n" + 
					"where r.zuxxb_id = zq.zuxxb_id\n" + 
					"and r.renyxxb_id = "+Renyid+"\n" + 
					"and z.id = zq.ziyxxb_id");
				boolean hasPower = false;
				if(rsl == null) {
					((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_errdb);
					return ErrorPageName;
				}
				while(rsl.next()) {
	//				判断当前页面名称是否存在于用户所在组 对应的资源权限中
					if(ipage.getPageName().equals(rsl.getString(0))
							||"GotoLogin".equals(ipage.getPageName())) {
						hasPower = true;
						break;
					}
				}
				if(!hasPower) {
	//				此用户无访问页面的权限
					((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_noPower);
					return ErrorPageName;
				}
			}
	//		过滤掉所有登陆错误之后将当前用户信息写入VISIT
			setVisitInfo(visit,Renyid);
			return ipage.getPageName();
		}catch(Exception e){
			return "";
		}finally{
			con.Close();
		}
	}
	
	public static void setVisitInfo(Visit visit,long Renyid) {
		JDBCcon con = new JDBCcon();
		try{
			visit.setActivePageName("");
			if(Renyid == -10000) {
				visit.setRenyID(-10000);
				visit.setRenyjb(2);
				visit.setRenymc("管理员");
				visit.setDiancxxb_id(999);
				visit.setDiancmc("智仁");
				visit.setDiancqc("智仁");
			}else {
				ResultSetList rsl = con.getResultSetList(
						"select r.quanc,d.jib,r.diancxxb_id,d.mingc,d.quanc " +
						"from renyxxb r,diancxxb d " +
						"where r.diancxxb_id = d.id and r.id = " + Renyid);
				if(rsl.next()) {
					int Renyjb = rsl.getInt(1);
					String Renymc = rsl.getString(0);
					long diancxxbid = rsl.getLong(2);
					String mingc = rsl.getString(3);
					String quanc = rsl.getString(4);
					visit.setRenyID(Renyid);
					visit.setRenymc(Renymc);
					visit.setRenyjb(Renyjb);
					visit.setDiancxxb_id(diancxxbid);
					visit.setDiancmc(mingc);
					visit.setDiancqc(quanc);
				}
				Ruljiaq(visit);
				Farldec(visit);
				Mtdec(visit);
				Shuldec(visit);
				rsl = con.getResultSetList("select * from diancxxb where fuid="+visit.getDiancxxb_id());
				if(rsl.next()) {
					visit.setFencb(true);
				}else{
					visit.setFencb(false);
				}
				rsl = con.getResultSetList("select cz.mingc daoz from diancxxb dc, chezxxb cz,diancdzb dd where dd.diancxxb_id = dc.id and dd.chezxxb_id=cz.id and dc.id = "+visit.getDiancxxb_id());
				if(rsl.next()) {
					visit.setDaoz(rsl.getString("daoz"));
				}
			}
			String show = MainGlobal.getXitsz("报表显示零值",""+visit.getDiancxxb_id(),"0");
			if(show.equals("1")) {
				visit.setReportShowZero(true);
			}
			String filepath = MainGlobal.getXitsz("系统文件夹位置", ""+visit.getDiancxxb_id(), "D:/zhiren/")+visit.getDiancxxb_id()+"/";
			visit.setXitwjjwz(filepath);
//			设置系统查询默认开始时间
			Date morkssj = new Date();
			String sql = "select zhi from xitxxb where mingc ='系统查询默认开始时间'";
			ResultSetList rsl = con.getResultSetList(sql);
			String morsj = "sysdate - to_number(to_char(sysdate-1,'dd'))";
			if(rsl.next()){
				morsj = rsl.getString("zhi");
			}
			rsl.close();
			sql = "select " + morsj + " kssj from dual";
			rsl = con.getResultSetList(sql);
			if(rsl.next()){
				morkssj = DateUtil.getDate(rsl.getDateString("kssj"));
			}
			rsl.close();
			visit.setMorkssj(morkssj);
//			设置系统查询默认结束时间
			Date morjssj = new Date();
			sql = "select zhi from xitxxb where mingc ='系统查询默认结束时间'";
			rsl = con.getResultSetList(sql);
			morsj = "sysdate - 1";
			if(rsl.next()){
				morsj = rsl.getString("zhi");
			}
			rsl.close();
			sql = "select " + morsj + " kssj from dual";
			rsl = con.getResultSetList(sql);
			if(rsl.next()){
				morjssj = DateUtil.getDate(rsl.getDateString("kssj"));
			}
			rsl.close();
			visit.setMorjssj(morjssj);
			
		}catch(Exception e){
			
		}finally{
			con.Close();
		}
		
	}
	
	public static void  Ruljiaq(Visit visit){
		JDBCcon con = new JDBCcon();
		visit.setRuljiaql("");
		try{
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='入炉加权量'");
			if(rs.next()){
				visit.setRuljiaql(rs.getString("zhi"));
			}
			rs.close();
		}catch(Exception e){
			
		}finally{
			con.Close();
		}
	}
	public static void  Farldec(Visit visit){
		JDBCcon con = new JDBCcon();
		visit.setFarldec(2);
		try{
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='热值保留位数'");
			if(rs.next()){
				visit.setFarldec(rs.getInt("zhi"));
			}
			rs.close();
		}catch(Exception e){
			
		}finally{
			con.Close();
		}
	}
	public static void  Mtdec(Visit visit){
		JDBCcon con = new JDBCcon();
		visit.setMtdec(1);
		try{
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='全水保留位数'");
			if(rs.next()){
				visit.setMtdec(rs.getInt("zhi"));
			}
			rs.close();
		}catch(Exception e){
			
		}finally{
			con.Close();
		}
	}
	/*
	 * 根据电厂设定决定报表中数量修约的小数位数
	 * 修改时间：2008-12-03
	 * 修改人：王磊
	 */
	public static void  Shuldec(Visit visit){
		JDBCcon con = new JDBCcon();
		visit.setShuldec(0);
		try{
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='数量保留位数' and diancxxb_id =" + visit.getDiancxxb_id());
			if(rs.next()){
				visit.setShuldec(rs.getInt("zhi"));
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	public static String MainLogin(IPage ipage,String userName,String pwd) {
		Visit visit = (Visit)ipage.getVisit();
		String pageName = ipage.getPageName();
//		如果VISIT中没有注册过用户名 或 用户名已过期
		if(visit.getRenyID() == 0) {
			pageName = LoginUser(userName,pwd,ipage);
//		如果VISIT中注册过用户名			
		}else {
//			如果新传入的用户名不等于visit中固有的用户名
			if(!userName.equals(visit.getRenymc())) {
				pageName = LoginUser(userName,pwd,ipage);
			}
		}
		return pageName;
	}
}
