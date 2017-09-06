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
 * ����
 * ����ϵͳ��ѯĬ�Ͽ�ʼ������ʱ��ĳ�ʼ��
 * �ְ汾:1.1.1.12
 */
/**
 * ���ߣ����
 * ʱ�䣺2012-12-11
 * ���÷�Χ������������ȵ�
 * ����������WINDOWS_CA��֤
 * 		ʹ�ò�����������MainGlobal.getXitxx_item("��½", "ʹ��WINDOWS_CA��֤����ר�ã�","0", "��")
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
//		ȡ�������е��û���
		userName = cycle.getRequestContext().getParameter("user") == null ? ""
				: cycle.getRequestContext().getParameter("user");
//		ȡ�������е�����
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
//		ȡ�������е��û���
		userName = cycle.getRequestContext().getParameter("user") == null ? ""
				: cycle.getRequestContext().getParameter("user");
		String pwd = "";
//		ȡ�������е�����
		pwd = cycle.getRequestContext().getParameter("pwd") == null ? ""
				: cycle.getRequestContext().getParameter("pwd");
//		���VISIT��û��ע����û��� �� �û����ѹ���
		if(visit.getRenyID() == 0) {
			pageName = LoginUser(userName,pwd,ipage);
//		���VISIT��ע����û���			
		}else {
//			����´�����û���������visit�й��е��û���
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
	//			û�о�����½ҳ��ע�� Ҳû���û������� ���ش���ҳ��
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
	//				�û���������
					((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_noUser);
					return ErrorPageName;
				}
				DataBassUtil dbu = new DataBassUtil();
				try {
					ByteArrayOutputStream out = dbu.GetBlob("renyxxb", "mim", Renyid);
					byte[] bytEnPwd = out.toByteArray();
					byte[] bytDePwd = En_Decrypt_ZR.decryptByDES(bytEnPwd, En_Decrypt_ZR.keybyte, En_Decrypt_ZR.ivbyte);
					String strDePwd = En_Decrypt_ZR.Byt2Str(bytDePwd);
//					�糧��վ���뱾ϵͳ(�����½����)ʹ��MD5���봫��
					if("��".equals(MainGlobal.getXitxx_item("��½", "����MD5_32λ����", String.valueOf("0"), "��"))){
						strDePwd = MD5.toMD5(strDePwd).toLowerCase();
					}
					
					//����WINDOWS_CA��֤����
//					if("��".equals(MainGlobal.getXitxx_item("��½", "ʹ��WINDOWS_CA��֤����ר�ã�","0", "��"))){
//						LDAPAuthenticate ca=new LDAPAuthenticate();
//						if(!ca.ValidADUser(userName,pwd)){
////							������֤û��ͨ��
//							((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_errPwd);
//							return ErrorPageName;
//						}
//					}
//					���������WINDOWS_CA��֤
					else if (!strDePwd.equals(pwd) && !En_Decrypt_ZR.Byt2Str(bytDePwd).equals(pwd)) {
	//					������֤û��ͨ��
						((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_errPwd);
						return ErrorPageName;
					}
				} catch (SQLException e) {
					// SQL���� ����δ֪����ҳ��
					e.printStackTrace();
					return ErrorPageName;
				} catch (IOException e) {
					// ��д���� ����δ֪����ҳ��
					e.printStackTrace();
					return ErrorPageName;
				} catch (Exception e) {
					// δ֪���� ����δ֪����ҳ��
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
	//				�жϵ�ǰҳ�������Ƿ�������û������� ��Ӧ����ԴȨ����
					if(ipage.getPageName().equals(rsl.getString(0))
							||"GotoLogin".equals(ipage.getPageName())) {
						hasPower = true;
						break;
					}
				}
				if(!hasPower) {
	//				���û��޷���ҳ���Ȩ��
					((Visit)ipage.getVisit()).setErrcode(SysConstant.ErrCode_noPower);
					return ErrorPageName;
				}
			}
	//		���˵����е�½����֮�󽫵�ǰ�û���Ϣд��VISIT
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
				visit.setRenymc("����Ա");
				visit.setDiancxxb_id(999);
				visit.setDiancmc("����");
				visit.setDiancqc("����");
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
			String show = MainGlobal.getXitsz("������ʾ��ֵ",""+visit.getDiancxxb_id(),"0");
			if(show.equals("1")) {
				visit.setReportShowZero(true);
			}
			String filepath = MainGlobal.getXitsz("ϵͳ�ļ���λ��", ""+visit.getDiancxxb_id(), "D:/zhiren/")+visit.getDiancxxb_id()+"/";
			visit.setXitwjjwz(filepath);
//			����ϵͳ��ѯĬ�Ͽ�ʼʱ��
			Date morkssj = new Date();
			String sql = "select zhi from xitxxb where mingc ='ϵͳ��ѯĬ�Ͽ�ʼʱ��'";
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
//			����ϵͳ��ѯĬ�Ͻ���ʱ��
			Date morjssj = new Date();
			sql = "select zhi from xitxxb where mingc ='ϵͳ��ѯĬ�Ͻ���ʱ��'";
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
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='��¯��Ȩ��'");
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
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='��ֵ����λ��'");
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
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='ȫˮ����λ��'");
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
	 * ���ݵ糧�趨����������������Լ��С��λ��
	 * �޸�ʱ�䣺2008-12-03
	 * �޸��ˣ�����
	 */
	public static void  Shuldec(Visit visit){
		JDBCcon con = new JDBCcon();
		visit.setShuldec(0);
		try{
			ResultSetList rs=con.getResultSetList("select zhi from xitxxb where mingc='��������λ��' and diancxxb_id =" + visit.getDiancxxb_id());
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
//		���VISIT��û��ע����û��� �� �û����ѹ���
		if(visit.getRenyID() == 0) {
			pageName = LoginUser(userName,pwd,ipage);
//		���VISIT��ע����û���			
		}else {
//			����´�����û���������visit�й��е��û���
			if(!userName.equals(visit.getRenymc())) {
				pageName = LoginUser(userName,pwd,ipage);
			}
		}
		return pageName;
	}
}
