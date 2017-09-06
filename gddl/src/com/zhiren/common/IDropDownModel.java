/* 
 * �������� 2005-5-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.zhiren.common;

/*
 * ʱ�䣺2007-01-15
 * ���ߣ�Qiuzuwei
 * ����������һ�����캯��IDropDownModel(List title, StringBuffer sql),����sql�������order by��ʹ�ô˹��캯��
 */

/*
 * ʱ�䣺2010-06-19
 * ���ߣ�ww
 * ���������FillModel(List title, JDBCcon con, String sql)��IDropDownModel(JDBCcon con, String sql)����
 * 		����Oracle͸�����ز������sql server2000����ʱ�������������
 * 		���� con.setAutoCommit(false); ....... con.commit();		
 * 		�ʱ��봫��һ�����Ӷ���
 */
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * @author Administrator
 * 
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
	 * ����Oracle͸�����ز������sql server2000ʱ�������������
	 * ���� con.setAutoCommit(false); ....... con.commit();
	 * �ʱ��봫��һ�����Ӷ���
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
			defaultValue = "��ѡ��";
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
