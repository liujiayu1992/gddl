/*
 * ���ڣ�2011��10��8��
 * ���ߣ�Qiuzw
 * ����������������ʱ��ʹ��id����long�͡��ȷ���bean�Ĺ���Ҳ�������������͵��������
 * */

/*
 * ʱ�䣺2007-10-22
 * ���ߣ�Qiuzuwei
 * ������ʹ��StringBuffer������������ʱ������ʹ��Ĭ��ֵѡ��
 */

/*
 * ʱ�䣺2007-01-03
 * ���ߣ�Qiuzuwei
 * �������޸��������������IDropDownSelectionModel����������
 *      ��ʹ��"order by"��sql����У�����ʹ�������������Ϊ��ʹsql��д��Ƕ�ף�Ҳ�����cpuռ��100%�����
 *      ����ͨ���ı�getResultSet()�����ڵĲ�������ʵ��
 *      ����������ɵ�ġ�������Ϣ��ģ��ͷ�����
 */

/* 
 * �������� 2005-5-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
		dropdownlist.add(new IDropDownBean(-1, "��ѡ��"));
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
		dropdownlist.add(new IDropDownBean(-1, "��ѡ��"));
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
		 * ʱ�䣺2007-01-03
		 * ���ߣ�Qiuzuwei
		 * ��������ʹ��"order by"��sql����У�����ʹ�������������Ϊ��ʹsql��д��Ƕ�ף�Ҳ�����cpuռ��100%�����
		 *      ����������ɵ�ġ�������Ϣ��ģ��ͷ�����
		 */
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(-1, "��ѡ��"));
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
		 * ʱ�䣺2007-10-22
		 * ���ߣ�Qiuzuwei
		 * ������ʹ��StringBuffer������Model
		 */
		List dropdownlist = new ArrayList();
		if (defaultValue == null) {
			defaultValue = "��ѡ��";
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
			defaultValue = "��ѡ��";
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
