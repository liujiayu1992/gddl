package com.zhiren.jt.jihgl.niancgjh;

import java.io.Serializable;

public class Niancgjhbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8655311071276367018L;

	private long xuh;// 序号

	private long dianchangid;

	private String dianchangmc;// 收货单位
	
	private long meikdqid;
	
	private String meikdqmc;//煤矿地区
	
	private long nianf;

	private double yue1;// 一月

	private double yue2;// 二月
	
	private double yue3;// 三月
	
	private double yue4;// 四月
	
	private double yue5;// 五月
	
	private double yue6;// 六月
	
	private double yue7;// 七月
	
	private double yue8;// 八月
	
	private double yue9;// 九月
	
	private double yue10;// 十月
	
	private double yue11;// 十一月
	
	private double yue12;// 十二月
	

	public Niancgjhbean(){
		
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public long getDianchangid() {
		return dianchangid;
	}


	public void setDianchangid(long dianchangid) {
		this.dianchangid = dianchangid;
	}


	public String getDianchangmc() {
		return dianchangmc;
	}


	public void setDianchangmc(String dianchangmc) {
		this.dianchangmc = dianchangmc;
	}

	public long getMeikdqid() {
		return meikdqid;
	}


	public void setMeikdqid(long meikdqid) {
		this.meikdqid = meikdqid;
	}


	public String getMeikdqmc() {
		return meikdqmc;
	}


	public void setMeikdqmc(String meikdqmc) {
		this.meikdqmc = meikdqmc;
	}


	public long getNianf() {
		return nianf;
	}


	public void setNianf(long nianf) {
		this.nianf = nianf;
	}


	public long getXuh() {
		return xuh;
	}


	public void setXuh(long xuh) {
		this.xuh = xuh;
	}


	public double getYue1() {
		return yue1;
	}


	public void setYue1(double yue1) {
		this.yue1 = yue1;
	}


	public double getYue10() {
		return yue10;
	}


	public void setYue10(double yue10) {
		this.yue10 = yue10;
	}


	public double getYue11() {
		return yue11;
	}


	public void setYue11(double yue11) {
		this.yue11 = yue11;
	}


	public double getYue12() {
		return yue12;
	}


	public void setYue12(double yue12) {
		this.yue12 = yue12;
	}


	public double getYue2() {
		return yue2;
	}


	public void setYue2(double yue2) {
		this.yue2 = yue2;
	}


	public double getYue3() {
		return yue3;
	}


	public void setYue3(double yue3) {
		this.yue3 = yue3;
	}


	public double getYue4() {
		return yue4;
	}


	public void setYue4(double yue4) {
		this.yue4 = yue4;
	}


	public double getYue5() {
		return yue5;
	}


	public void setYue5(double yue5) {
		this.yue5 = yue5;
	}


	public double getYue6() {
		return yue6;
	}


	public void setYue6(double yue6) {
		this.yue6 = yue6;
	}


	public double getYue7() {
		return yue7;
	}


	public void setYue7(double yue7) {
		this.yue7 = yue7;
	}


	public double getYue8() {
		return yue8;
	}


	public void setYue8(double yue8) {
		this.yue8 = yue8;
	}


	public double getYue9() {
		return yue9;
	}


	public void setYue9(double yue9) {
		this.yue9 = yue9;
	}


	public Niancgjhbean(long xuh, long dianchangid, String dianchangmc, long meikdqid, String meikdqmc, long nianf, double yue1, double yue2, double yue3, double yue4, double yue5, double yue6, double yue7, double yue8, double yue9, double yue10, double yue11, double yue12) {
		super();
		this.xuh = xuh;
		this.dianchangid = dianchangid;
		this.dianchangmc = dianchangmc;
		this.meikdqid = meikdqid;
		this.meikdqmc = meikdqmc;
		this.nianf = nianf;
		this.yue1 = yue1;
		this.yue2 = yue2;
		this.yue3 = yue3;
		this.yue4 = yue4;
		this.yue5 = yue5;
		this.yue6 = yue6;
		this.yue7 = yue7;
		this.yue8 = yue8;
		this.yue9 = yue9;
		this.yue10 = yue10;
		this.yue11 = yue11;
		this.yue12 = yue12;
	}
	
	


}