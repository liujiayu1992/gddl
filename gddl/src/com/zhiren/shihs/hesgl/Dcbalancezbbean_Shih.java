package com.zhiren.shihs.hesgl;

import java.io.Serializable;

public class Dcbalancezbbean_Shih implements Serializable {

	// 合同_Begin
	
	private String CaO_ht;	//碳算钙

	private String MgO_ht;	//碳酸镁

	private String Xid_ht;	//细度
	// 合同_End

	
	// 厂方指标_Begin

	private double CaO_cf;	

	private double MgO_cf;

	private double Xid_cf;
	// 厂方指标_End

	
	// 矿方指标_Begin

	private double CaO_kf;

	private double MgO_kf;

	private double Xid_kf;
	// 矿方指标_End

	
	// 结算指标_Begin

	private double CaO_js;

	private double MgO_js;

	private double Xid_js;
	// 结算指标_End

	
	// 盈亏_Begin

	private double CaO_yk;

	private double MgO_yk;

	private double Xid_yk;
	// 盈亏_End

	
	// 折单价_Begin

	private double CaO_zdj;

	private double MgO_zdj;

	private double Xid_zdj;
	// 折单价_End

	
	// 折金额_Begin

	private double CaO_zje;

	private double MgO_zje;

	private double Xid_zje;
	// 折金额_End

	public Dcbalancezbbean_Shih() {
	}

	public Dcbalancezbbean_Shih(
			// 合同
			String CaO_ht,
			String MgO_ht,
			String Xid_ht,
			
			// 供方
			double CaO_kf,
			double MgO_kf,
			double Xid_kf,
			
			// 厂方
			double CaO_cf,
			double MgO_cf,
			double Xid_cf,
			
			// 结算
			double CaO_js, 
			double MgO_js, 
			double Xid_js,
			
			// 盈亏
			double CaO_yk, 
			double MgO_yk, 
			double Xid_yk, 
			
			// 折单价
			double CaO_zdj, 
			double MgO_zdj, 
			double Xid_zdj, 
			
			// 折金额
			double CaO_zje, 
			double MgO_zje, 
			double Xid_zje
			
		) {

		// 合同
		this.CaO_ht = CaO_ht;
		this.MgO_ht = MgO_ht;
		this.Xid_ht = Xid_ht;
		

		// 矿方
		this.CaO_kf = CaO_kf;
		this.MgO_kf = MgO_kf;
		this.Xid_kf = Xid_kf;
		
		
		// 厂方
		this.CaO_cf = CaO_cf;
		this.MgO_cf = MgO_cf;
		this.Xid_cf = Xid_cf;
		

		// 结算
		this.CaO_js = CaO_js;
		this.MgO_js = MgO_js;
		this.Xid_js = MgO_js;
		

		// 盈亏
		this.CaO_yk = CaO_yk;
		this.MgO_yk = MgO_yk;
		this.Xid_yk = Xid_yk;
		

		// 折单价
		this.CaO_zdj = CaO_zdj;
		this.MgO_zdj = MgO_zdj;
		this.Xid_zdj = Xid_zdj;

		
		// 折金额
		this.CaO_zje = CaO_zje;
		this.MgO_zje = MgO_zje;
		this.Xid_zje = Xid_zje;
	}

	public double getCaO_cf() {
		return CaO_cf;
	}

	public void setCaO_cf(double caO_cf) {
		CaO_cf = caO_cf;
	}

	public String getCaO_ht() {
		return CaO_ht;
	}

	public void setCaO_ht(String caO_ht) {
		CaO_ht = caO_ht;
	}

	public double getCaO_js() {
		return CaO_js;
	}

	public void setCaO_js(double caO_js) {
		CaO_js = caO_js;
	}

	public double getCaO_kf() {
		return CaO_kf;
	}

	public void setCaO_kf(double caO_kf) {
		CaO_kf = caO_kf;
	}

	public double getCaO_yk() {
		return CaO_yk;
	}

	public void setCaO_yk(double caO_yk) {
		CaO_yk = caO_yk;
	}

	public double getCaO_zdj() {
		return CaO_zdj;
	}

	public void setCaO_zdj(double caO_zdj) {
		CaO_zdj = caO_zdj;
	}

	public double getCaO_zje() {
		return CaO_zje;
	}

	public void setCaO_zje(double caO_zje) {
		CaO_zje = caO_zje;
	}

	public double getMgO_cf() {
		return MgO_cf;
	}

	public void setMgO_cf(double mgO_cf) {
		MgO_cf = mgO_cf;
	}

	public String getMgO_ht() {
		return MgO_ht;
	}

	public void setMgO_ht(String mgO_ht) {
		MgO_ht = mgO_ht;
	}

	public double getMgO_js() {
		return MgO_js;
	}

	public void setMgO_js(double mgO_js) {
		MgO_js = mgO_js;
	}

	public double getMgO_kf() {
		return MgO_kf;
	}

	public void setMgO_kf(double mgO_kf) {
		MgO_kf = mgO_kf;
	}

	public double getMgO_yk() {
		return MgO_yk;
	}

	public void setMgO_yk(double mgO_yk) {
		MgO_yk = mgO_yk;
	}

	public double getMgO_zdj() {
		return MgO_zdj;
	}

	public void setMgO_zdj(double mgO_zdj) {
		MgO_zdj = mgO_zdj;
	}

	public double getMgO_zje() {
		return MgO_zje;
	}

	public void setMgO_zje(double mgO_zje) {
		MgO_zje = mgO_zje;
	}

	public double getXid_cf() {
		return Xid_cf;
	}

	public void setXid_cf(double xid_cf) {
		Xid_cf = xid_cf;
	}

	public String getXid_ht() {
		return Xid_ht;
	}

	public void setXid_ht(String xid_ht) {
		Xid_ht = xid_ht;
	}

	public double getXid_js() {
		return Xid_js;
	}

	public void setXid_js(double xid_js) {
		Xid_js = xid_js;
	}

	public double getXid_kf() {
		return Xid_kf;
	}

	public void setXid_kf(double xid_kf) {
		Xid_kf = xid_kf;
	}

	public double getXid_yk() {
		return Xid_yk;
	}

	public void setXid_yk(double xid_yk) {
		Xid_yk = xid_yk;
	}

	public double getXid_zdj() {
		return Xid_zdj;
	}

	public void setXid_zdj(double xid_zdj) {
		Xid_zdj = xid_zdj;
	}

	public double getXid_zje() {
		return Xid_zje;
	}

	public void setXid_zje(double xid_zje) {
		Xid_zje = xid_zje;
	}

	
}
