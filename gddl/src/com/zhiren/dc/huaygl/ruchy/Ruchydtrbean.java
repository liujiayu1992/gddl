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
public class Ruchydtrbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;// ID

	private int xuh;

	private long zhillsb_id;// ZHILLSB_id

	private Date fenxrq;// FENXRQ

	private long fenxxmb_id;// FENXXMB_ID

	private String huayyqbh;// HUAYYQBH

	private double qimzl;// QIMZL

	private double qimmyzl;

	private double meiyzl;// MEIYZL

	private double zhi;// ZHI

	private int shenhzt;// SHENHZT

	private String shenhry;// SHENHRY

	private String lury;// LURY

	private double tianjwrz;// TIANJWRZ

	private double tianjwzl;// TIANJWZL

	/**
	 * @return qimmyzl
	 */
	public double getQimmyzl() {
		return qimmyzl;
	}

	/**
	 * @param qimmyzl Ҫ���õ� qimmyzl
	 */
	public void setQimmyzl(double qimmyzl) {
		this.qimmyzl = qimmyzl;
	}

	/**
	 * @return fenxrq
	 */
	public Date getFenxrq() {
		return fenxrq;
	}

	/**
	 * @param fenxrq
	 *            Ҫ���õ� fenxrq
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
	 *            Ҫ���õ� fenxxmb_id
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
	 *            Ҫ���õ� huayyqbh
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
	 *            Ҫ���õ� id
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
	 *            Ҫ���õ� lury
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
	 *            Ҫ���õ� meiyzl
	 */
	public void setMeiyzl(double meiyzl) {
		this.meiyzl = meiyzl;
	}

	/**
	 * @return qimzl
	 */
	public double getQimzl() {
		return qimzl;
	}

	/**
	 * @param qimzl
	 *            Ҫ���õ� qimzl
	 */
	public void setQimzl(double qimzl) {
		this.qimzl = qimzl;
	}

	/**
	 * @return shenhry
	 */
	public String getShenhry() {
		return shenhry;
	}

	/**
	 * @param shenhry
	 *            Ҫ���õ� shenhry
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
	 *            Ҫ���õ� shenhzt
	 */
	public void setShenhzt(int shenhzt) {
		this.shenhzt = shenhzt;
	}

	/**
	 * @return tianjwrz
	 */
	public double getTianjwrz() {
		return tianjwrz;
	}

	/**
	 * @param tianjwrz
	 *            Ҫ���õ� tianjwrz
	 */
	public void setTianjwrz(double tianjwrz) {
		this.tianjwrz = tianjwrz;
	}

	/**
	 * @return tianjwzl
	 */
	public double getTianjwzl() {
		return tianjwzl;
	}

	/**
	 * @param tianjwzl
	 *            Ҫ���õ� tianjwzl
	 */
	public void setTianjwzl(double tianjwzl) {
		this.tianjwzl = tianjwzl;
	}

	/**
	 * @return zhi
	 */
	public double getZhi() {
		return zhi;
	}

	/**
	 * @param zhi
	 *            Ҫ���õ� zhi
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
	 *            Ҫ���õ� zhillsb_id
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

	public Ruchydtrbean() {

	}

	public Ruchydtrbean(long id, int xuh, long zhillsb_id, Date fenxrq,
			long fenxxmb_id, String huayyqbh, double qimzl, double qimmyzl,
			double meiyzl, double zhi, int shenhzt, String shenhry,
			String lury, double tianjwrz, double tianjwzl) {

		this.id = id;

		this.xuh = xuh;

		this.zhillsb_id = zhillsb_id;

		this.fenxrq = fenxrq;

		this.fenxxmb_id = fenxxmb_id;

		this.huayyqbh = huayyqbh;

		this.qimzl = qimzl;

		this.qimmyzl = qimmyzl;

		this.meiyzl = meiyzl;

		this.zhi = zhi;

		this.shenhzt = shenhzt;

		this.shenhry = shenhry;

		this.lury = lury;

		this.tianjwrz = tianjwrz;

		this.tianjwzl = tianjwzl;

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
		buffer.append(qimzl);
		buffer.append(',');
		buffer.append(qimmyzl);
		buffer.append(',');
		buffer.append(meiyzl);
		buffer.append(',');
		buffer.append(zhi);
		buffer.append(',');
		buffer.append(shenhzt);
		buffer.append(',');
		buffer.append(shenhry);
		buffer.append(',');
		buffer.append(lury);
		buffer.append(',');
		buffer.append(tianjwrz);
		buffer.append(',');
		buffer.append(tianjwzl);

		return buffer.toString();
	}
}
