package com.zhiren.jt.gongys;

import java.io.Serializable;

public class Gongysbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5128127251695960784L;

	private long m_id;//
	
	private long m_fuid;

	private String m_bianm;// 供应商编码

	private String m_quanc;// 供应商名称

	private String m_mingc;// 供应商简称

	private String m_shengf;// 所属省

	private String m_leib;// 供应商类别

	private String obj_id;

	public String getObj_id() {
		return obj_id;
	}

	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}
	
	public long getFuid(){
		return m_fuid;
	}
	
	public void setFuid(long fuid){
		this.m_fuid = fuid;
	}

	public String getBianm() {
		return m_bianm;
	}

	public void setBianm(String bianm) {
		this.m_bianm = bianm;
	}

	public String getQuanc() {
		return m_quanc;
	}

	public void setQuanc(String quanc) {
		this.m_quanc = quanc;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getShengf() {
		return m_shengf;
	}

	public void setShengf(String shengf) {
		this.m_shengf = shengf;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public Gongysbean() {
	}

	public Gongysbean(long id, String bianm, String quanc, String mingc,
			String shengf, String leib,String obj_id,long fuid) {

		this.m_id = id;
		this.m_bianm = bianm;
		this.m_quanc = quanc;
		this.m_mingc = mingc;
		this.m_shengf = shengf;
		this.m_leib = leib;
		this.obj_id = obj_id;
		this.m_fuid = fuid;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_bianm);
		buffer.append(',');
		buffer.append(m_quanc);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_shengf);
		buffer.append(',');
		buffer.append(m_leib);
		buffer.append(',');
		buffer.append(obj_id);
		buffer.append(',');
		buffer.append(m_fuid);

		return buffer.toString();
	}

}
