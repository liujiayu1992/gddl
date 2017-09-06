package com.zhiren.jt.het.hetsh;

import java.io.Serializable;
import java.util.Date;
public class Hetshbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int xuh;
	private String heth;
	private String gongf;
	private String xuf;
	private long hetl;
	private Date qiandrq;
	private String hetzt;
	private String hetlb;
	private long hetlcztb_id;
	private int shendzt;
	private long liucb_id;
	
	public long getLiucb_id() {
		return liucb_id;
	}
	public void setLiucb_id(long liucb_id) {
		this.liucb_id = liucb_id;
	}
	public long getHetlcztb_id() {
		return hetlcztb_id;
	}
	public void setHetlcztb_id(long hetlcztb_id) {
		this.hetlcztb_id = hetlcztb_id;
	}
	public int getShendzt() {
		return shendzt;
	}
	public void setShendzt(int shendzt) {
		this.shendzt = shendzt;
	}
	public int getXuh() {
		return xuh;
	}
	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	public String getHetlb() {
		return hetlb;
	}
	public void setHetlb(String hetlb) {
		this.hetlb = hetlb;
	}
	public String getGongf() {
		return gongf;
	}
	public void setGongf(String gongf) {
		this.gongf = gongf;
	}
	public String getHeth() {
		return heth;
	}
	public void setHeth(String heth) {
		this.heth = heth;
	}
	public long getHetl() {
		return hetl;
	}
	public void setHetl(long hetl) {
		this.hetl = hetl;
	}
	public String getHetzt() {
		return hetzt;
	}
	public void setHetzt(String hetzt) {
		this.hetzt = hetzt;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getQiandrq() {
		return qiandrq;
	}
	public void setQiandrq(Date qiandrq) {
		this.qiandrq = qiandrq;
	}
	public String getXuf() {
		return xuf;
	}
	public void setXuf(String xuf) {
		this.xuf = xuf;
	}
	//´úÉó¶¨×´Ì¬µÄ
	public Hetshbean(int xuh,long id, String heth, String gongf, String xuf, long hetl, Date qiandrq, String hetzt,String leib,int shendzt,long Hetlcztb_id,long liucb_id) {
		super();
		this.id = id;
		this.heth = heth;
		this.gongf = gongf;
		this.xuf = xuf;
		this.hetl = hetl;
		this.qiandrq = qiandrq;
		this.hetzt = hetzt;
		this.hetlb=leib;
		this.xuh=xuh;
		this.hetlcztb_id=Hetlcztb_id;
		this.shendzt=shendzt;
		this.liucb_id=liucb_id;
	}
}
