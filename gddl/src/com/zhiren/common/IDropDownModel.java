/* 
 * 创建日期 2005-5-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.common;

/*
 * 时间：2007-01-15
 * 作者：Qiuzuwei
 * 描述：增加一个构造函数IDropDownModel(List title, StringBuffer sql),建议sql语句中有order by的使用此构造函数
 */

/*
 * 时间：2010-06-19
 * 作者：ww
 * 描述：添加FillModel(List title, JDBCcon con, String sql)、IDropDownModel(JDBCcon con, String sql)方法
 * 		当用Oracle透明网关插件调用sql server2000数据时必须放在事务中
 * 		即： con.setAutoCommit(false); ....... con.commit();		
 * 		故必须传入一个连接对象
 */
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * @author Administrator
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class IDropDownModel implements IPropertySelectionModel, Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7682850099219428033L;
	
	
	private List DropDownList;
	
	
	
	
	// private String[] options;
	
	public IDropDownModel(List dropdownlist) {
		this.DropDownList = dropdownlist;
	}
	
	
	public IDropDownModel(String[] options) {
		// this.options = options;
	}
	
	public void FillModel(List title, String sql) {
		List dropdownlist = new ArrayList();
		if(title != null) {
			dropdownlist.addAll(title);
		}
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		if(rs != null)
			while (rs.next()) {
				String id = rs.getString(0);
				String mc = rs.getString(1);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
		con.Close();
		this.DropDownList = dropdownlist;
	}
	
	/**
	 * @author ww
	 * 当用Oracle透明网关插件调用sql server2000时必须放在事务中
	 * 即： con.setAutoCommit(false); ....... con.commit();
	 * 故必须传入一个连接对象
	 */
	public void FillModel(List title, JDBCcon con, String sql) {
		List dropdownlist = new ArrayList();
		if(title != null) {
			dropdownlist.addAll(title);
		}
//		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		if(rs != null)
			while (rs.next()) {
				String id = rs.getString(0);
				String mc = rs.getString(1);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
//		con.Close();
		this.DropDownList = dropdownlist;
	}	
	
	public IDropDownModel(JDBCcon con, String sql) {
		FillModel(null, con, sql);
	}
	
	public IDropDownModel(String sql) {
		FillModel(null,sql);
	}
	
	public IDropDownModel(List title, String sql) {
		FillModel(title,sql);
	}
	
	public IDropDownModel(StringBuffer sql) {
		FillModel(null,sql.toString());
	}
	
	public IDropDownModel(List title, StringBuffer sql) {
		FillModel(title,sql.toString());
	}
	
	public IDropDownModel(String sql, String defaultValue) {
		List dropdownlist = new ArrayList();
		if (defaultValue == null) {
			defaultValue = "请选择";
		}
		dropdownlist.add(new IDropDownBean(-1, defaultValue));
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		if(rs != null)
			while (rs.next()) {
				String id = rs.getString(0);
				String mc = rs.getString(1);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
		con.Close();
		this.DropDownList = dropdownlist;
	}
	
	public int getOptionCount() {
		return DropDownList.size();
	}
	
	
	public Object getOption(int index) {
		return (IDropDownBean) DropDownList.get(index);
	}
	
	
	public String getLabel(int index) {
		return ((IDropDownBean) DropDownList.get(index)).getValue();
	}
	
	
	public String getValue(int index) {
		return ((IDropDownBean) DropDownList.get(index)).getStrId();
	}
	
	
	public Object translateValue(String value) {
		int i = 0;
		for (; i < DropDownList.size(); i++) {
			if (value.equalsIgnoreCase(String
					.valueOf(((IDropDownBean) DropDownList.get(i)).getId())))
				break;
		}
		if(i == DropDownList.size()) {
			return "";
		}
		return getOption(i);
	}
	
	
	public String getBeanValue(long id) {
		int i = 0;
		for (; i < DropDownList.size(); i++) {
			if (id == ((IDropDownBean) DropDownList.get(i)).getId())
				break;
		}
		if(i == DropDownList.size()) {
			return null;
		}
		return ((IDropDownBean) DropDownList.get(i)).getValue();
	}
	
	public String getBeanValue(String id) {
		int i = 0;
		for (; i < DropDownList.size(); i++) {
			if (id.equals(((IDropDownBean) DropDownList.get(i)).getStrId()))
				break;
		}
		if(i == DropDownList.size()) {
			return null;
		}
		return ((IDropDownBean) DropDownList.get(i)).getValue();
	}
	
	public long getBeanId(String value) {
		int i = 0;
		for (; i < DropDownList.size(); i++) {
			if (value.equals(((IDropDownBean) DropDownList.get(i)).getValue()))
				break;
		}
		if( i == DropDownList.size()) {
			return -1;
		}
		return ((IDropDownBean) DropDownList.get(i)).getId();
	}
	
	public String getBeanStrId(String value) {
		int i = 0;
		for (; i < DropDownList.size(); i++) {
			if (value.equals(((IDropDownBean) DropDownList.get(i)).getValue()))
				break;
		}
		if( i == DropDownList.size()) {
			return null;
		}
		return ((IDropDownBean) DropDownList.get(i)).getStrId();
	}
}
