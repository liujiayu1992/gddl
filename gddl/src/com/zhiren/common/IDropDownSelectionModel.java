/*
 * 日期：2011年10月8日
 * 作者：Qiuzw
 * 描述：构造下拉框时，使用id采用long型。既符合bean的构型也避免了数据类型的溢出错误。
 * */

/*
 * 时间：2007-10-22
 * 作者：Qiuzuwei
 * 描述：使用StringBuffer对象构造下拉框时，可以使用默认值选项
 */

/*
 * 时间：2007-01-03
 * 作者：Qiuzuwei
 * 描述：修改了生成下拉框的IDropDownSelectionModel（）方法，
 *      在使用"order by"的sql语句中，建议使用这个方法，因为即使sql中写了嵌套，也会出现cpu占用100%的情况
 *      现在通过改变getResultSet()方法内的参数类型实现
 *      这个问题在辽电的“生产信息”模块就发生了
 */

/* 
 * 创建日期 2005-5-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.common;

import org.apache.tapestry.form.IPropertySelectionModel;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class IDropDownSelectionModel implements IPropertySelectionModel,
        Serializable {
	
	
	/**n
	 * 
	 */
	private static final long serialVersionUID = 5438137317122637017L;
	
	
	private List DropDownList;
	
	
	
	public IDropDownSelectionModel(List dropdownlist) {
		this.DropDownList = dropdownlist;
	}
	
	
	public IDropDownSelectionModel(String sql) {
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(-1, "请选择"));
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				long id = rs.getLong(1);
				String mc = rs.getString(2);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		this.DropDownList = dropdownlist;
	}
	
	
	public IDropDownSelectionModel(String sql, JDBCcon con) {
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(-1, "请选择"));
		//		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				long id = rs.getLong(1);
				String mc = rs.getString(2);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.DropDownList = dropdownlist;
	}
	
	
	public IDropDownSelectionModel(StringBuffer sql) {
		/*
		 * 时间：2007-01-03
		 * 作者：Qiuzuwei
		 * 描述：在使用"order by"的sql语句中，建议使用这个方法，因为即使sql中写了嵌套，也会出现cpu占用100%的情况
		 *      这个问题在辽电的“生产信息”模块就发生了
		 */
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(-1, "请选择"));
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				long id = rs.getLong(1);
				String mc = rs.getString(2);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		this.DropDownList = dropdownlist;
	}
	
	
	public IDropDownSelectionModel(StringBuffer sql, String defaultValue) {
		/*
		 * 时间：2007-10-22
		 * 作者：Qiuzuwei
		 * 描述：使用StringBuffer对象构造Model
		 */
		List dropdownlist = new ArrayList();
		if (defaultValue == null) {
			defaultValue = "请选择";
		}
		dropdownlist.add(new IDropDownBean(-1, defaultValue));
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				long id = rs.getLong(1);
				String mc = rs.getString(2);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
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
		return String
				.valueOf(((IDropDownBean) DropDownList.get(index)).getId());
	}
	
	
	public Object translateValue(String value) {
		int i = 0;
		for (; i < DropDownList.size(); i++) {
			if (value.equalsIgnoreCase(String
					.valueOf(((IDropDownBean) DropDownList.get(i)).getId())))
				break;
		}
		if(i == DropDownList.size()){
			return null;
		}
		return getOption(i);
	}
	
	
	public IDropDownSelectionModel(String sql, String defaultValue) {
		List dropdownlist = new ArrayList();
		if (defaultValue == null) {
			defaultValue = "请选择";
		}
		dropdownlist.add(new IDropDownBean(-1, defaultValue));
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				long id = rs.getLong(1);
				String mc = rs.getString(2);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		this.DropDownList = dropdownlist;
	}
	
}
