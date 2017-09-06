/**
 * 
 */
package com.zhiren.dc.huaygl.ruchy;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Antonio
 * 
 */
public class Ruchylfbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private int xuh;

	private long zhillsb_id;// ZHILLSB_id

	private Date fenxrq;// FENXRQ

	private long fenxxmb_id;// FENXXMB_ID

	private String huayyqbh;// HUAYYQBH

	private double meiyzl;// MEIYZL

	private double zhi;// ZHI

	private int shenhzt;// SHENHZT

	private String shenhry;// SHENHRY

	private String lury;// LURY

	private double liuf;

	private double qimzl;

	private double qimmyzl;

	/**
	 * @return qimmyzl
	 */
	public double getQimmyzl() {
		return qimmyzl;
	}

	/**
	 * @param qimmyzl 要设置的 qimmyzl
	 */
	public void setQimmyzl(double qimmyzl) {
		this.qimmyzl = qimmyzl;
	}

	/**
	 * @return qimzl
	 */
	public double getQimzl() {
		return qimzl;
	}

	/**
	 * @param qimzl 要设置的 qimzl
	 */
	public void setQimzl(double qimzl) {
		this.qimzl = qimzl;
	}

	/**
	 * @return liuf
	 */
	public double getLiuf() {
		return liuf;
	}

	/**
	 * @param liuf 要设置的 liuf
	 */
	public void setLiuf(double liuf) {
		this.liuf = liuf;
	}

	/**
	 * @return fenxrq
	 */
	public Date getFenxrq() {
		return fenxrq;
	}

	/**
	 * @param fenxrq
	 *            要设置的 fenxrq
	 */
	public void setFenxrq(Date fenxrq) {
		this.fenxrq = fenxrq;
	}

	/**
	 * @return fenxxmb_id
	 */
	public long getFenxxmb_id() {
		return fenxxmb_id;
	}

	/**
	 * @param fenxxmb_id
	 *            要设置的 fenxxmb_id
	 */
	public void setFenxxmb_id(long fenxxmb_id) {
		this.fenxxmb_id = fenxxmb_id;
	}

	/**
	 * @return huayyqbh
	 */
	public String getHuayyqbh() {
		return huayyqbh;
	}

	/**
	 * @param huayyqbh
	 *            要设置的 huayyqbh
	 */
	public void setHuayyqbh(String huayyqbh) {
		this.huayyqbh = huayyqbh;
	}

	/**
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return lury
	 */
	public String getLury() {
		return lury;
	}

	/**
	 * @param lury
	 *            要设置的 lury
	 */
	public void setLury(String lury) {
		this.lury = lury;
	}

	/**
	 * @return meiyzl
	 */
	public double getMeiyzl() {
		return meiyzl;
	}

	/**
	 * @param meiyzl
	 *            要设置的 meiyzl
	 */
	public void setMeiyzl(double meiyzl) {
		this.meiyzl = meiyzl;
	}

	/**
	 * @return shenhry
	 */
	public String getShenhry() {
		return shenhry;
	}

	/**
	 * @param shenhry
	 *            要设置的 shenhry
	 */
	public void setShenhry(String shenhry) {
		this.shenhry = shenhry;
	}

	/**
	 * @return shenhzt
	 */
	public int getShenhzt() {
		return shenhzt;
	}

	/**
	 * @param shenhzt
	 *            要设置的 shenhzt
	 */
	public void setShenhzt(int shenhzt) {
		this.shenhzt = shenhzt;
	}

	/**
	 * @return zhi
	 */
	public double getZhi() {
		return zhi;
	}

	/**
	 * @param zhi
	 *            要设置的 zhi
	 */
	public void setZhi(double zhi) {
		this.zhi = zhi;
	}

	/**
	 * @return zhillsb_id
	 */
	public long getZhillsb_id() {
		return zhillsb_id;
	}

	/**
	 * @param zhillsb_id
	 *            要设置的 zhillsb_id
	 */
	public void setZhillsb_id(long zhillsb_id) {
		this.zhillsb_id = zhillsb_id;
	}

	public int getXuh() {
		return xuh;
	}

	public void setXuh(int xuh) {
		this.xuh = xuh;
	}

	public Ruchylfbean() {

	}

	public Ruchylfbean(long id, int xuh, long zhillsb_id, Date fenxrq,
			long fenxxmb_id, String huayyqbh, double qimzl, double qimmyzl,
			double meiyzl, double zhi, double liuf, int shenhzt,
			String shenhry, String lury) {

		this.id = id;

		this.xuh = xuh;

		this.zhillsb_id = zhillsb_id;

		this.fenxrq = fenxrq;

		this.fenxxmb_id = fenxxmb_id;

		this.huayyqbh = huayyqbh;

		this.meiyzl = meiyzl;

		this.zhi = zhi;

		this.qimzl = qimzl;

		this.qimmyzl = qimmyzl;

		this.liuf = liuf;

		this.shenhzt = shenhzt;

		this.shenhry = shenhry;

		this.lury = lury;

	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");
		buffer.append(id);
		buffer.append(',');
		buffer.append(xuh);
		buffer.append(',');
		buffer.append(zhillsb_id);
		buffer.append(',');
		buffer.append(fenxrq);
		buffer.append(',');
		buffer.append(fenxxmb_id);
		buffer.append(',');
		buffer.append(huayyqbh);
		buffer.append(',');
		buffer.append(meiyzl);
		buffer.append(',');
		buffer.append(qimzl);
		buffer.append(',');
		buffer.append(qimmyzl);
		buffer.append(',');
		buffer.append(zhi);
		buffer.append(',');
		buffer.append(liuf);
		buffer.append(',');
		buffer.append(shenhzt);
		buffer.append(',');
		buffer.append(shenhry);
		buffer.append(',');
		buffer.append(lury);
		return buffer.toString();
	}
}
