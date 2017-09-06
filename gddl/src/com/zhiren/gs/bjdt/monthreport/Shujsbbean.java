package com.zhiren.gs.bjdt.monthreport;

import java.io.Serializable;

public class Shujsbbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4680621456255052317L;

	/**
	 * 
	 */
	private boolean m_select;
	
	private String m_shangbb;// 收货单位

	public Shujsbbean(String m_shangbb) {
		super();
		this.m_shangbb = m_shangbb;
	}

	public boolean getSelect() {
		return m_select;
	}

	public void setSelect(boolean Select) {
		this.m_select = Select;
	}

	public String getShangbb() {
		return m_shangbb;
	}

	public void setShangbb(String Shangbb) {
		this.m_shangbb = Shangbb;
	}

	public Shujsbbean(){
		
	}

}