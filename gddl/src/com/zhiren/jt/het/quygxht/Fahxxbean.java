package com.zhiren.jt.het.quygxht;

import java.io.Serializable;

/**
 * @author cao
 *11.26日完成了合同模板的详细设计，并完成了合同信息，数量信息的基本工作。
 *
 *
 *
 */
public class Fahxxbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  long id;
	private String pinz;
	private String yunsfs;
	private String faz;
	private String daoz;
	private String shouhr;
	private long hej;
	private long Y1;
	private long Y2;
	private long Y3;
	private long Y4;
	private long Y5;
	private long Y6;
	private long Y7;
	private long Y8;
	private long Y9;
	private long Y10;
	private long Y11;
	private long Y12;
	public String getDaoz() {
		return daoz;
	}
	public void setDaoz(String daoz) {
		this.daoz = daoz;
	}
	public String getFaz() {
		return faz;
	}
	public void setFaz(String fahz) {
		this.faz = fahz;
	}
	public long getHej() {
		return hej;
	}
	public void setHej(long hej) {
		this.hej = hej;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPinz() {
		return pinz;
	}
	public void setPinz(String pinz) {
		this.pinz = pinz;
	}
	public String getShouhr() {
		return shouhr;
	}
	public void setShouhr(String shouhr) {
		this.shouhr = shouhr;
	}
	public long getY1() {
		return Y1;
	}
	public void setY1(long y1) {
		Y1 = y1;
	}
	public long getY10() {
		return Y10;
	}
	public void setY10(long y10) {
		Y10 = y10;
	}
	public long getY11() {
		return Y11;
	}
	public void setY11(long y11) {
		Y11 = y11;
	}
	public long getY12() {
		return Y12;
	}
	public void setY12(long y12) {
		Y12 = y12;
	}
	public long getY2() {
		return Y2;
	}
	public void setY2(long y2) {
		Y2 = y2;
	}
	public long getY3() {
		return Y3;
	}
	public void setY3(long y3) {
		Y3 = y3;
	}
	public long getY4() {
		return Y4;
	}
	public void setY4(long y4) {
		Y4 = y4;
	}
	public long getY5() {
		return Y5;
	}
	public void setY5(long y5) {
		Y5 = y5;
	}
	public long getY6() {
		return Y6;
	}
	public void setY6(long y6) {
		Y6 = y6;
	}
	public long getY7() {
		return Y7;
	}
	public void setY7(long y7) {
		Y7 = y7;
	}
	public long getY8() {
		return Y8;
	}
	public void setY8(long y8) {
		Y8 = y8;
	}
	public long getY9() {
		return Y9;
	}
	public void setY9(long y9) {
		Y9 = y9;
	}
	public String getYunsfs() {
		return yunsfs;
	}
	public void setYunsfs(String yunsfs) {
		this.yunsfs = yunsfs;
	}
	
	public Fahxxbean(String daoz,String shouhr) {
		super();
		this.daoz=daoz;
		this.shouhr=shouhr;
	}
	public Fahxxbean(long id, String pinz, String yunsfs, String faz, String daoz, String shouhr, long hej, long y1, long y2, long y3, long y4, long y5, long y6, long y7, long y8, long y9, long y10, long y11, long y12) {
		super();
		this.id = id;
		this.pinz = pinz;
		this.yunsfs = yunsfs;
		this.faz = faz;
		this.daoz = daoz;
		this.shouhr = shouhr;
		this.hej = hej;
		Y1 = y1;
		Y2 = y2;
		Y3 = y3;
		Y4 = y4;
		Y5 = y5;
		Y6 = y6;
		Y7 = y7;
		Y8 = y8;
		Y9 = y9;
		Y10 = y10;
		Y11 = y11;
		Y12 = y12;
	}
}
