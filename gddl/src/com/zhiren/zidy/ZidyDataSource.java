package com.zhiren.zidy;

import java.io.Serializable;

import com.zhiren.common.ResultSetList;

public class ZidyDataSource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4435161179995028135L;
	
	private String DataSourceID;
	private ResultSetList DataSource;
	
	public ResultSetList getDataSource() {
		return DataSource;
	}
	public void setDataSource(ResultSetList dataSource) {
		DataSource = dataSource;
	}
	public String getDataSourceID() {
		return DataSourceID;
	}
	public void setDataSourceID(String dataSourceID) {
		DataSourceID = dataSourceID;
	}
	
	/**
	 * 
	 */
	public ZidyDataSource() {
		super();
		// TODO 自动生成构造函数存根
	}
	/**
	 * @param dataSourceID
	 * @param dataSource
	 */
	public ZidyDataSource(String dataSourceID, ResultSetList dataSource) {
		super();
		DataSourceID = dataSourceID;
		DataSource = dataSource;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getDataSourceID());
		return sb.toString();
	}
	
	
}
