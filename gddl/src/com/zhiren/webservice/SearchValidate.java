package com.zhiren.webservice;

import java.util.Date;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;

public class SearchValidate {
	public String getDiancxxb_id(JDBCcon con,String bianm) {
		if(bianm==null || "".equals(bianm)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from diancxxb where bianm='")
		.append(bianm).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getGongysb_id(JDBCcon con,String bianm) {
		if(bianm==null || "".equals(bianm)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from gongysb where bianm='")
		.append(bianm).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getMeikxxb_id(JDBCcon con,String bianm) {
		if(bianm==null || "".equals(bianm)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from meikxxb where bianm='")
		.append(bianm).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getPinzb_id(JDBCcon con,String bianm) {
		if(bianm==null || "".equals(bianm)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from pinzb where bianm='")
		.append(bianm).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getJihkjb_id(JDBCcon con,String bianm) {
		if(bianm==null || "".equals(bianm)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from jihkjb where bianm='")
		.append(bianm).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getChezxxb_id(JDBCcon con,String bianm) {
		if(bianm==null || "".equals(bianm)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from chezxxb where bianm='")
		.append(bianm).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getYuanshdw_id(JDBCcon con,String mingc) {
		if(mingc==null || "".equals(mingc)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from vwyuanshdw where mingc='")
		.append(mingc).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getYunsdw_id(JDBCcon con,String mingc) {
		if(mingc==null || "".equals(mingc)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select id from yunsdwb where mingc='")
		.append(mingc).append("'");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next())
			return rsl.getString("id");
		return null;
	}
	public String getYunsfs_id(String yunsfs) {
		if(yunsfs == null || "".equals(yunsfs)) {
			return null;
		}
		if("铁路".equals(yunsfs)) {
			return String.valueOf(SysConstant.YUNSFS_HUOY);
		}else
			if("公路".equals(yunsfs)) {
				return String.valueOf(SysConstant.YUNSFS_QIY);
			}
		return String.valueOf(SysConstant.YUNSFS_HaiY);
	}
	public String getCheb_id(String cheb) {
		if(cheb == null || "".equals(cheb)) {
			return null;
		}
		if("路车".equals(cheb)) 
			return String.valueOf(SysConstant.CHEB_LC);
		else if("自备".equals(cheb))
			return String.valueOf(SysConstant.CHEB_ZB);
		else if("汽".equals(cheb))
			return String.valueOf(SysConstant.CHEB_QC);
		return String.valueOf(SysConstant.CHEB_C);
	}
	
	public String getDouble(String strDouble) {
		try {
			Double.parseDouble(strDouble);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return strDouble;
	}
	
	public String getOracleDate(String strDate) {
		Date sd = DateUtil.getDate(strDate);
		if(sd == null) {
			return null;
		}
		return DateUtil.FormatOracleDate(sd);
	}
	public String getOracleDateTime(String strDate) {
		Date sd = DateUtil.getDateTime(strDate);
		if(sd == null) {
			return null;
		}
		return DateUtil.FormatOracleDateTime(sd);
	}
	
}
