package com.zhiren.jt.zdt.zonghcx.pandbg;

import java.io.Serializable;

public class Pandcytjbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @author Yangzong
	 * @since 2008-08-21
	 */
	private long id = -1;

	private int xuh = 1;

	private String xiangm = "";// 项目

	private int shulm = 0;// 数量(煤)

	private int shuly = 0;// 数据(油)

	public long getId() {
		return id;
	}

	public int getShulm() {
		return shulm;
	}

	public int getShuly() {
		return shuly;
	}

	public String getXiangm() {
		return xiangm;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setShulm(int shulm) {
		this.shulm = shulm;
	}

	public void setShuly(int shuly) {
		this.shuly = shuly;
	}

	public void setXiangm(String xiangm) {
		this.xiangm = xiangm;
	}

	public int getXuh() {
		return xuh;
	}

	public void setXuh(int xuh) {
		this.xuh = xuh;
	}

	public Pandcytjbean() {
	}

	public Pandcytjbean(long id, int xuh, String xiangm, int shulm, int shuly) {
		this.id = id;
		this.xuh = xuh;
		this.xiangm = xiangm;
		this.shulm = shulm;
		this.shuly = shuly;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(this.id);
		buffer.append(",");
		buffer.append(this.xiangm);
		buffer.append(",");
		buffer.append(this.xuh);
		buffer.append(",");
		buffer.append(this.shulm);
		buffer.append(",");
		buffer.append(this.shuly);

		return buffer.toString();
	}

}