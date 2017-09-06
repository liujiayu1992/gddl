package com.zhiren.pub.duanxin;

import java.io.Serializable;


public class Zidfdxszbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3506183064240337507L;

	private String m_xiangm;// 标题

	private String m_user;// 时间类型

	private long m_id;

	public String getXiangm() {
		return m_xiangm;
	}

	public void setXiangm(String value) {
		this.m_xiangm = value;
	}

	public String getUser() {
		return m_user;
	}

	public void setUser(String value) {
		this.m_user = value;
	}

	public long getId() {
		return m_id;
	}

	public void setId(long value) {
		this.m_id = value;
	}

	public Zidfdxszbean(String xiangm, String user, long _id) {

		this.m_xiangm = xiangm;
		this.m_user = user;
		this.m_id=_id;
	}

	public Zidfdxszbean() {
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_xiangm);
		buffer.append(',');
		buffer.append(m_user);

		return buffer.toString();
	}

}
