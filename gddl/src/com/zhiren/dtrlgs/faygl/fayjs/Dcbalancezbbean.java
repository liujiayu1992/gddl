package com.zhiren.dtrlgs.faygl.fayjs;

import java.io.Serializable;

public class Dcbalancezbbean implements Serializable {

	// 合同_Begin
	private String Qnetar_ht;

	private String Std_ht;

	private String Ad_ht;

	private String Vdaf_ht;

	private String Mt_ht;

	private String Qgrad_ht;

	private String Qbad_ht;

	private String Had_ht;

	private String Stad_ht;

	private String Mad_ht;

	private String Aar_ht;

	private String Aad_ht;

	private String Vad_ht;

	private String T2_ht;
	
	private String Yunju_ht;
	
	private String Star_ht;
	// 合同_End

	// 厂方指标_Begin

	private double Qnetar_cf;

	private double Std_cf;

	private double Ad_cf;

	private double Vdaf_cf;

	private double Mt_cf;

	private double Qgrad_cf;

	private double Qbad_cf;

	private double Had_cf;

	private double Stad_cf;

	private double Mad_cf;

	private double Aar_cf;

	private double Aad_cf;

	private double Vad_cf;

	private double T2_cf;
	
	private double Yunju_cf;
	
	private double Star_cf;

	// 厂方指标_End

	// 矿方指标_Begin

	private double Qnetar_kf;

	private double Std_kf;

	private double Ad_kf;

	private double Vdaf_kf;

	private double Mt_kf;

	private double Qgrad_kf;

	private double Qbad_kf;

	private double Had_kf;

	private double Stad_kf;

	private double Mad_kf;

	private double Aar_kf;

	private double Aad_kf;

	private double Vad_kf;

	private double T2_kf;
	
	private double Yunju_kf;
	
	private double Star_kf;

	// 矿方指标_End

	// 结算指标_Begin

	private double Qnetar_js;

	private double Std_js;

	private double Ad_js;

	private double Vdaf_js;

	private double Mt_js;

	private double Qgrad_js;

	private double Qbad_js;

	private double Had_js;

	private double Stad_js;

	private double Mad_js;

	private double Aar_js;

	private double Aad_js;

	private double Vad_js;

	private double T2_js;
	
	private double Yunju_js;
	
	private double Star_js;

	// 结算指标_End

	// 盈亏_Begin

	private double Qnetar_yk;

	private double Std_yk;

	private double Ad_yk;

	private double Vdaf_yk;

	private double Mt_yk;

	private double Qgrad_yk;

	private double Qbad_yk;

	private double Had_yk;

	private double Stad_yk;

	private double Mad_yk;

	private double Aar_yk;

	private double Aad_yk;

	private double Vad_yk;

	private double T2_yk;
	
	private double Yunju_yk;
	
	private double Star_yk;

	// 盈亏_End

	// 折单价_Begin

	private double Qnetar_zdj;

	private double Std_zdj;

	private double Ad_zdj;

	private double Vdaf_zdj;

	private double Mt_zdj;

	private double Qgrad_zdj;

	private double Qbad_zdj;

	private double Had_zdj;

	private double Stad_zdj;

	private double Mad_zdj;

	private double Aar_zdj;

	private double Aad_zdj;

	private double Vad_zdj;

	private double T2_zdj;
	
	private double Yunju_zdj;
	
	private double Star_zdj;

	// 折单价_End

	// 折金额_Begin

	private double Qnetar_zje;

	private double Std_zje;

	private double Ad_zje;

	private double Vdaf_zje;

	private double Mt_zje;

	private double Qgrad_zje;

	private double Qbad_zje;

	private double Had_zje;

	private double Stad_zje;

	private double Mad_zje;

	private double Aar_zje;

	private double Aad_zje;

	private double Vad_zje;

	private double T2_zje;
	
	private double Yunju_zje;
	
	private double Star_zje;

	// 折金额_End

	public Dcbalancezbbean() {
	}

	public Dcbalancezbbean(
			// 合同
			String Qnetar_ht,
			String Std_ht,
			String Ad_ht,
			String Vdaf_ht,
			String Mt_ht,
			String Qgrad_ht,
			String Qbad_ht,
			String Had_ht,
			String Stad_ht,
			String Mad_ht,
			String Aar_ht,
			String Aad_ht,
			String Vad_ht,
			String T2_ht,
			String Yunju_ht,
			String Star_ht,
			// 供方
			double Qnetar_kf,
			double Std_kf,
			double Ad_kf,
			double Vdaf_kf,
			double Mt_kf,
			double Qgrad_kf,
			double Qbad_kf,
			double Had_kf,
			double Stad_kf,
			double Mad_kf,
			double Aar_kf,
			double Aad_kf,
			double Vad_kf,
			double T2_kf,
			double Yunju_kf,
			double Star_kf,
			// 厂方
			double Qnetar_cf,
			double Std_cf,
			double Ad_cf,
			double Vdaf_cf,
			double Mt_cf,
			double Qgrad_cf,
			double Qbad_cf,
			double Had_cf,
			double Stad_cf,
			double Mad_cf,
			double Aar_cf,
			double Aad_cf,
			double Vad_cf,
			double T2_cf,
			double Yunju_cf,
			double Star_cf,
			// 结算
			double Qnetar_js, double Std_js, double Ad_js,
			double Vdaf_js,
			double Mt_js,
			double Qgrad_js,
			double Qbad_js,
			double Had_js,
			double Stad_js,
			double Mad_js,
			double Aar_js,
			double Aad_js,
			double Vad_js,
			double T2_js,
			double Yunju_js,
			double Star_js,
			// 盈亏
			double Qnetar_yk, double Std_yk, double Ad_yk, double Vdaf_yk,
			double Mt_yk, double Qgrad_yk, double Qbad_yk,
			double Had_yk,
			double Stad_yk,
			double Mad_yk,
			double Aar_yk,
			double Aad_yk,
			double Vad_yk,
			double T2_yk,
			double Yunju_yk,
			double Star_yk,
			// 折单价
			double Qnetar_zdj, double Std_zdj, double Ad_zdj, double Vdaf_zdj,
			double Mt_zdj, double Qgrad_zdj, double Qbad_zdj, double Had_zdj,
			double Stad_zdj, double Mad_zdj, double Aar_zdj,
			double Aad_zdj,
			double Vad_zdj,
			double T2_zdj,
			double Yunju_zdj,
			double Star_zdj,
			// 折金额
			double Qnetar_zje, double Std_zje, double Ad_zje, double Vdaf_zje,
			double Mt_zje, double Qgrad_zje, double Qbad_zje, double Had_zje,
			double Stad_zje, double Mad_zje, double Aar_zje, double Aad_zje,
			double Vad_zje, double T2_zje,double Yunju_zje,double Star_zje
			
		) {

		// 合同
		this.Qnetar_ht = Qnetar_ht;
		this.Std_ht = Std_ht;
		this.Ad_ht = Ad_ht;
		this.Vdaf_ht = Vdaf_ht;
		this.Mt_ht = Mt_ht;
		this.Qgrad_ht = Qgrad_ht;
		this.Qbad_ht = Qbad_ht;
		this.Had_ht = Had_ht;
		this.Stad_ht = Stad_ht;
		this.Mad_ht = Mad_ht;
		this.Aar_ht = Aar_ht;
		this.Aad_ht = Aad_ht;
		this.Vad_ht = Vad_ht;
		this.T2_ht = T2_ht;
		this.Yunju_ht=Yunju_ht;
		this.Star_ht = Star_ht;

		// 矿方
		this.Qnetar_kf = Qnetar_kf;
		this.Std_kf = Std_kf;
		this.Ad_kf = Ad_kf;
		this.Vdaf_kf = Vdaf_kf;
		this.Mt_kf = Mt_kf;
		this.Qgrad_kf = Qgrad_kf;
		this.Qbad_kf = Qbad_kf;
		this.Had_kf = Had_kf;
		this.Stad_kf = Stad_kf;
		this.Mad_kf = Mad_kf;
		this.Aar_kf = Aar_kf;
		this.Aad_kf = Aad_kf;
		this.Vad_kf = Vad_kf;
		this.T2_kf = T2_kf;
		this.Yunju_kf =	Yunju_kf;
		this.Star_kf =	Star_kf;
		
		// 厂方
		this.Qnetar_cf = Qnetar_cf;
		this.Std_cf = Std_cf;
		this.Ad_cf = Ad_cf;
		this.Vdaf_cf = Vdaf_cf;
		this.Mt_cf = Mt_cf;
		this.Qgrad_cf = Qgrad_cf;
		this.Qbad_cf = Qbad_cf;
		this.Had_cf = Had_cf;
		this.Stad_cf = Stad_cf;
		this.Mad_cf = Mad_cf;
		this.Aar_cf = Aar_cf;
		this.Aad_cf = Aad_cf;
		this.Vad_cf = Vad_cf;
		this.T2_cf = T2_cf;
		this.Yunju_cf = Yunju_cf;
		this.Star_cf = Star_cf;

		// 结算
		this.Qnetar_js = Qnetar_js;
		this.Std_js = Std_js;
		this.Ad_js = Ad_js;
		this.Vdaf_js = Vdaf_js;
		this.Mt_js = Mt_js;
		this.Qgrad_js = Qgrad_js;
		this.Qbad_js = Qbad_js;
		this.Had_js = Had_js;
		this.Stad_js = Stad_js;
		this.Mad_js = Mad_js;
		this.Aar_js = Aar_js;
		this.Aad_js = Aad_js;
		this.Vad_js = Vad_js;
		this.T2_js = T2_js;
		this.Yunju_js = Yunju_js;
		this.Star_js = Star_js;

		// 盈亏
		this.Qnetar_yk = Qnetar_yk;
		this.Std_yk = Std_yk;
		this.Ad_yk = Ad_yk;
		this.Vdaf_yk = Vdaf_yk;
		this.Mt_yk = Mt_yk;
		this.Qgrad_yk = Qgrad_yk;
		this.Qbad_yk = Qbad_yk;
		this.Had_yk = Had_yk;
		this.Stad_yk = Stad_yk;
		this.Mad_yk = Mad_yk;
		this.Aar_yk = Aar_yk;
		this.Aad_yk = Aad_yk;
		this.Vad_yk = Vad_yk;
		this.T2_yk = T2_yk;
		this.Yunju_yk = Yunju_yk;
		this.Star_yk = Star_yk;

		// 折单价
		this.Qnetar_zdj = Qnetar_zdj;
		this.Std_zdj = Std_zdj;
		this.Ad_zdj = Ad_zdj;
		this.Vdaf_zdj = Vdaf_zdj;
		this.Mt_zdj = Mt_zdj;
		this.Qgrad_zdj = Qgrad_zdj;
		this.Qbad_zdj = Qbad_zdj;
		this.Had_zdj = Had_zdj;
		this.Stad_zdj = Stad_zdj;
		this.Mad_zdj = Mad_zdj;
		this.Aar_zdj = Aar_zdj;
		this.Aad_zdj = Aad_zdj;
		this.Vad_zdj = Vad_zdj;
		this.T2_zdj = T2_zdj;
		this.Yunju_zdj = Yunju_zdj;
		this.Star_zdj = Star_zdj;

		// 折金额
		this.Qnetar_zje = Qnetar_zje;
		this.Std_zje = Std_zje;
		this.Ad_zje = Ad_zje;
		this.Vdaf_zje = Vdaf_zje;
		this.Mt_zje = Mt_zje;
		this.Qgrad_zje = Qgrad_zje;
		this.Qbad_zje = Qbad_zje;
		this.Had_zje = Had_zje;
		this.Stad_zje = Stad_zje;
		this.Mad_zje = Mad_zje;
		this.Aar_zje = Aar_zje;
		this.Aad_zje = Aad_zje;
		this.Vad_zje = Vad_zje;
		this.T2_zje = T2_zje;
		this.Yunju_zje = Yunju_zje;
		this.Star_zje = Star_zje;
	}

	public double getAad_cf() {
		return Aad_cf;
	}

	public void setAad_cf(double aad_cf) {
		Aad_cf = aad_cf;
	}

	public String getAad_ht() {
		return Aad_ht;
	}

	public void setAad_ht(String aad_ht) {
		Aad_ht = aad_ht;
	}

	public double getAad_js() {
		return Aad_js;
	}

	public void setAad_js(double aad_js) {
		Aad_js = aad_js;
	}

	public double getAad_kf() {
		return Aad_kf;
	}

	public void setAad_kf(double aad_kf) {
		Aad_kf = aad_kf;
	}

	public double getAad_yk() {
		return Aad_yk;
	}

	public void setAad_yk(double aad_yk) {
		Aad_yk = aad_yk;
	}

	public double getAad_zdj() {
		return Aad_zdj;
	}

	public void setAad_zdj(double aad_zdj) {
		Aad_zdj = aad_zdj;
	}

	public double getAad_zje() {
		return Aad_zje;
	}

	public void setAad_zje(double aad_zje) {
		Aad_zje = aad_zje;
	}

	public double getAar_cf() {
		return Aar_cf;
	}

	public void setAar_cf(double aar_cf) {
		Aar_cf = aar_cf;
	}

	public String getAar_ht() {
		return Aar_ht;
	}

	public void setAar_ht(String aar_ht) {
		Aar_ht = aar_ht;
	}

	public double getAar_js() {
		return Aar_js;
	}

	public void setAar_js(double aar_js) {
		Aar_js = aar_js;
	}

	public double getAar_kf() {
		return Aar_kf;
	}

	public void setAar_kf(double aar_kf) {
		Aar_kf = aar_kf;
	}

	public double getAar_yk() {
		return Aar_yk;
	}

	public void setAar_yk(double aar_yk) {
		Aar_yk = aar_yk;
	}

	public double getAar_zdj() {
		return Aar_zdj;
	}

	public void setAar_zdj(double aar_zdj) {
		Aar_zdj = aar_zdj;
	}

	public double getAar_zje() {
		return Aar_zje;
	}

	public void setAar_zje(double aar_zje) {
		Aar_zje = aar_zje;
	}

	public double getAd_cf() {
		return Ad_cf;
	}

	public void setAd_cf(double ad_cf) {
		Ad_cf = ad_cf;
	}

	public String getAd_ht() {
		return Ad_ht;
	}

	public void setAd_ht(String ad_ht) {
		Ad_ht = ad_ht;
	}

	public double getAd_js() {
		return Ad_js;
	}

	public void setAd_js(double ad_js) {
		Ad_js = ad_js;
	}

	public double getAd_kf() {
		return Ad_kf;
	}

	public void setAd_kf(double ad_kf) {
		Ad_kf = ad_kf;
	}

	public double getAd_yk() {
		return Ad_yk;
	}

	public void setAd_yk(double ad_yk) {
		Ad_yk = ad_yk;
	}

	public double getAd_zdj() {
		return Ad_zdj;
	}

	public void setAd_zdj(double ad_zdj) {
		Ad_zdj = ad_zdj;
	}

	public double getAd_zje() {
		return Ad_zje;
	}

	public void setAd_zje(double ad_zje) {
		Ad_zje = ad_zje;
	}

	public double getHad_cf() {
		return Had_cf;
	}

	public void setHad_cf(double had_cf) {
		Had_cf = had_cf;
	}

	public String getHad_ht() {
		return Had_ht;
	}

	public void setHad_ht(String had_ht) {
		Had_ht = had_ht;
	}

	public double getHad_js() {
		return Had_js;
	}

	public void setHad_js(double had_js) {
		Had_js = had_js;
	}

	public double getHad_kf() {
		return Had_kf;
	}

	public void setHad_kf(double had_kf) {
		Had_kf = had_kf;
	}

	public double getHad_yk() {
		return Had_yk;
	}

	public void setHad_yk(double had_yk) {
		Had_yk = had_yk;
	}

	public double getHad_zdj() {
		return Had_zdj;
	}

	public void setHad_zdj(double had_zdj) {
		Had_zdj = had_zdj;
	}

	public double getHad_zje() {
		return Had_zje;
	}

	public void setHad_zje(double had_zje) {
		Had_zje = had_zje;
	}

	public double getMad_cf() {
		return Mad_cf;
	}

	public void setMad_cf(double mad_cf) {
		Mad_cf = mad_cf;
	}

	public String getMad_ht() {
		return Mad_ht;
	}

	public void setMad_ht(String mad_ht) {
		Mad_ht = mad_ht;
	}

	public double getMad_js() {
		return Mad_js;
	}

	public void setMad_js(double mad_js) {
		Mad_js = mad_js;
	}

	public double getMad_kf() {
		return Mad_kf;
	}

	public void setMad_kf(double mad_kf) {
		Mad_kf = mad_kf;
	}

	public double getMad_yk() {
		return Mad_yk;
	}

	public void setMad_yk(double mad_yk) {
		Mad_yk = mad_yk;
	}

	public double getMad_zdj() {
		return Mad_zdj;
	}

	public void setMad_zdj(double mad_zdj) {
		Mad_zdj = mad_zdj;
	}

	public double getMad_zje() {
		return Mad_zje;
	}

	public void setMad_zje(double mad_zje) {
		Mad_zje = mad_zje;
	}

	public double getMt_cf() {
		return Mt_cf;
	}

	public void setMt_cf(double mt_cf) {
		Mt_cf = mt_cf;
	}

	public String getMt_ht() {
		return Mt_ht;
	}

	public void setMt_ht(String mt_ht) {
		Mt_ht = mt_ht;
	}

	public double getMt_js() {
		return Mt_js;
	}

	public void setMt_js(double mt_js) {
		Mt_js = mt_js;
	}

	public double getMt_kf() {
		return Mt_kf;
	}

	public void setMt_kf(double mt_kf) {
		Mt_kf = mt_kf;
	}

	public double getMt_yk() {
		return Mt_yk;
	}

	public void setMt_yk(double mt_yk) {
		Mt_yk = mt_yk;
	}

	public double getMt_zdj() {
		return Mt_zdj;
	}

	public void setMt_zdj(double mt_zdj) {
		Mt_zdj = mt_zdj;
	}

	public double getMt_zje() {
		return Mt_zje;
	}

	public void setMt_zje(double mt_zje) {
		Mt_zje = mt_zje;
	}

	public double getQbad_cf() {
		return Qbad_cf;
	}

	public void setQbad_cf(double qbad_cf) {
		Qbad_cf = qbad_cf;
	}

	public String getQbad_ht() {
		return Qbad_ht;
	}

	public void setQbad_ht(String qbad_ht) {
		Qbad_ht = qbad_ht;
	}

	public double getQbad_js() {
		return Qbad_js;
	}

	public void setQbad_js(double qbad_js) {
		Qbad_js = qbad_js;
	}

	public double getQbad_kf() {
		return Qbad_kf;
	}

	public void setQbad_kf(double qbad_kf) {
		Qbad_kf = qbad_kf;
	}

	public double getQbad_yk() {
		return Qbad_yk;
	}

	public void setQbad_yk(double qbad_yk) {
		Qbad_yk = qbad_yk;
	}

	public double getQbad_zdj() {
		return Qbad_zdj;
	}

	public void setQbad_zdj(double qbad_zdj) {
		Qbad_zdj = qbad_zdj;
	}

	public double getQbad_zje() {
		return Qbad_zje;
	}

	public void setQbad_zje(double qbad_zje) {
		Qbad_zje = qbad_zje;
	}

	public double getQgrad_cf() {
		return Qgrad_cf;
	}

	public void setQgrad_cf(double qgrad_cf) {
		Qgrad_cf = qgrad_cf;
	}

	public String getQgrad_ht() {
		return Qgrad_ht;
	}

	public void setQgrad_ht(String qgrad_ht) {
		Qgrad_ht = qgrad_ht;
	}

	public double getQgrad_js() {
		return Qgrad_js;
	}

	public void setQgrad_js(double qgrad_js) {
		Qgrad_js = qgrad_js;
	}

	public double getQgrad_kf() {
		return Qgrad_kf;
	}

	public void setQgrad_kf(double qgrad_kf) {
		Qgrad_kf = qgrad_kf;
	}

	public double getQgrad_yk() {
		return Qgrad_yk;
	}

	public void setQgrad_yk(double qgrad_yk) {
		Qgrad_yk = qgrad_yk;
	}

	public double getQgrad_zdj() {
		return Qgrad_zdj;
	}

	public void setQgrad_zdj(double qgrad_zdj) {
		Qgrad_zdj = qgrad_zdj;
	}

	public double getQgrad_zje() {
		return Qgrad_zje;
	}

	public void setQgrad_zje(double qgrad_zje) {
		Qgrad_zje = qgrad_zje;
	}

	public double getQnetar_cf() {
		return Qnetar_cf;
	}

	public void setQnetar_cf(double qnetar_cf) {
		Qnetar_cf = qnetar_cf;
	}

	public String getQnetar_ht() {
		return Qnetar_ht;
	}

	public void setQnetar_ht(String qnetar_ht) {
		Qnetar_ht = qnetar_ht;
	}

	public double getQnetar_js() {
		return Qnetar_js;
	}

	public void setQnetar_js(double qnetar_js) {
		Qnetar_js = qnetar_js;
	}

	public double getQnetar_kf() {
		return Qnetar_kf;
	}

	public void setQnetar_kf(double qnetar_kf) {
		Qnetar_kf = qnetar_kf;
	}

	public double getQnetar_yk() {
		return Qnetar_yk;
	}

	public void setQnetar_yk(double qnetar_yk) {
		Qnetar_yk = qnetar_yk;
	}

	public double getQnetar_zdj() {
		return Qnetar_zdj;
	}

	public void setQnetar_zdj(double qnetar_zdj) {
		Qnetar_zdj = qnetar_zdj;
	}

	public double getQnetar_zje() {
		return Qnetar_zje;
	}

	public void setQnetar_zje(double qnetar_zje) {
		Qnetar_zje = qnetar_zje;
	}

	public double getStad_cf() {
		return Stad_cf;
	}

	public void setStad_cf(double stad_cf) {
		Stad_cf = stad_cf;
	}

	public String getStad_ht() {
		return Stad_ht;
	}

	public void setStad_ht(String stad_ht) {
		Stad_ht = stad_ht;
	}

	public double getStad_js() {
		return Stad_js;
	}

	public void setStad_js(double stad_js) {
		Stad_js = stad_js;
	}

	public double getStad_kf() {
		return Stad_kf;
	}

	public void setStad_kf(double stad_kf) {
		Stad_kf = stad_kf;
	}

	public double getStad_yk() {
		return Stad_yk;
	}

	public void setStad_yk(double stad_yk) {
		Stad_yk = stad_yk;
	}

	public double getStad_zdj() {
		return Stad_zdj;
	}

	public void setStad_zdj(double stad_zdj) {
		Stad_zdj = stad_zdj;
	}

	public double getStad_zje() {
		return Stad_zje;
	}

	public void setStad_zje(double stad_zje) {
		Stad_zje = stad_zje;
	}

	public double getStd_cf() {
		return Std_cf;
	}

	public void setStd_cf(double std_cf) {
		Std_cf = std_cf;
	}

	public String getStd_ht() {
		return Std_ht;
	}

	public void setStd_ht(String std_ht) {
		Std_ht = std_ht;
	}

	public double getStd_js() {
		return Std_js;
	}

	public void setStd_js(double std_js) {
		Std_js = std_js;
	}

	public double getStd_kf() {
		return Std_kf;
	}

	public void setStd_kf(double std_kf) {
		Std_kf = std_kf;
	}

	public double getStd_yk() {
		return Std_yk;
	}

	public void setStd_yk(double std_yk) {
		Std_yk = std_yk;
	}

	public double getStd_zdj() {
		return Std_zdj;
	}

	public void setStd_zdj(double std_zdj) {
		Std_zdj = std_zdj;
	}

	public double getStd_zje() {
		return Std_zje;
	}

	public void setStd_zje(double std_zje) {
		Std_zje = std_zje;
	}

	public double getT2_cf() {
		return T2_cf;
	}

	public void setT2_cf(double t2_cf) {
		T2_cf = t2_cf;
	}

	public String getT2_ht() {
		return T2_ht;
	}

	public void setT2_ht(String t2_ht) {
		T2_ht = t2_ht;
	}

	public double getT2_js() {
		return T2_js;
	}

	public void setT2_js(double t2_js) {
		T2_js = t2_js;
	}

	public double getT2_kf() {
		return T2_kf;
	}

	public void setT2_kf(double t2_kf) {
		T2_kf = t2_kf;
	}

	public double getT2_yk() {
		return T2_yk;
	}

	public void setT2_yk(double t2_yk) {
		T2_yk = t2_yk;
	}

	public double getT2_zdj() {
		return T2_zdj;
	}

	public void setT2_zdj(double t2_zdj) {
		T2_zdj = t2_zdj;
	}

	public double getT2_zje() {
		return T2_zje;
	}

	public void setT2_zje(double t2_zje) {
		T2_zje = t2_zje;
	}

	public double getVad_cf() {
		return Vad_cf;
	}

	public void setVad_cf(double vad_cf) {
		Vad_cf = vad_cf;
	}

	public String getVad_ht() {
		return Vad_ht;
	}

	public void setVad_ht(String vad_ht) {
		Vad_ht = vad_ht;
	}

	public double getVad_js() {
		return Vad_js;
	}

	public void setVad_js(double vad_js) {
		Vad_js = vad_js;
	}

	public double getVad_kf() {
		return Vad_kf;
	}

	public void setVad_kf(double vad_kf) {
		Vad_kf = vad_kf;
	}

	public double getVad_yk() {
		return Vad_yk;
	}

	public void setVad_yk(double vad_yk) {
		Vad_yk = vad_yk;
	}

	public double getVad_zdj() {
		return Vad_zdj;
	}

	public void setVad_zdj(double vad_zdj) {
		Vad_zdj = vad_zdj;
	}

	public double getVad_zje() {
		return Vad_zje;
	}

	public void setVad_zje(double vad_zje) {
		Vad_zje = vad_zje;
	}

	public double getVdaf_cf() {
		return Vdaf_cf;
	}

	public void setVdaf_cf(double vdaf_cf) {
		Vdaf_cf = vdaf_cf;
	}

	public String getVdaf_ht() {
		return Vdaf_ht;
	}

	public void setVdaf_ht(String vdaf_ht) {
		Vdaf_ht = vdaf_ht;
	}

	public double getVdaf_js() {
		return Vdaf_js;
	}

	public void setVdaf_js(double vdaf_js) {
		Vdaf_js = vdaf_js;
	}

	public double getVdaf_kf() {
		return Vdaf_kf;
	}

	public void setVdaf_kf(double vdaf_kf) {
		Vdaf_kf = vdaf_kf;
	}

	public double getVdaf_yk() {
		return Vdaf_yk;
	}

	public void setVdaf_yk(double vdaf_yk) {
		Vdaf_yk = vdaf_yk;
	}

	public double getVdaf_zdj() {
		return Vdaf_zdj;
	}

	public void setVdaf_zdj(double vdaf_zdj) {
		Vdaf_zdj = vdaf_zdj;
	}

	public double getVdaf_zje() {
		return Vdaf_zje;
	}

	public void setVdaf_zje(double vdaf_zje) {
		Vdaf_zje = vdaf_zje;
	}

	public double getYunju_cf() {
		return Yunju_cf;
	}

	public void setYunju_cf(double yunju_cf) {
		Yunju_cf = yunju_cf;
	}

	public String getYunju_ht() {
		return Yunju_ht;
	}

	public void setYunju_ht(String yunju_ht) {
		Yunju_ht = yunju_ht;
	}

	public double getYunju_js() {
		return Yunju_js;
	}

	public void setYunju_js(double yunju_js) {
		Yunju_js = yunju_js;
	}

	public double getYunju_kf() {
		return Yunju_kf;
	}

	public void setYunju_kf(double yunju_kf) {
		Yunju_kf = yunju_kf;
	}

	public double getYunju_yk() {
		return Yunju_yk;
	}

	public void setYunju_yk(double yunju_yk) {
		Yunju_yk = yunju_yk;
	}

	public double getYunju_zdj() {
		return Yunju_zdj;
	}

	public void setYunju_zdj(double yunju_zdj) {
		Yunju_zdj = yunju_zdj;
	}

	public double getYunju_zje() {
		return Yunju_zje;
	}

	public void setYunju_zje(double yunju_zje) {
		Yunju_zje = yunju_zje;
	}

	public double getStar_cf() {
		return Star_cf;
	}

	public void setStar_cf(double star_cf) {
		Star_cf = star_cf;
	}

	public String getStar_ht() {
		return Star_ht;
	}

	public void setStar_ht(String star_ht) {
		Star_ht = star_ht;
	}

	public double getStar_js() {
		return Star_js;
	}

	public void setStar_js(double star_js) {
		Star_js = star_js;
	}

	public double getStar_kf() {
		return Star_kf;
	}

	public void setStar_kf(double star_kf) {
		Star_kf = star_kf;
	}

	public double getStar_yk() {
		return Star_yk;
	}

	public void setStar_yk(double star_yk) {
		Star_yk = star_yk;
	}

	public double getStar_zdj() {
		return Star_zdj;
	}

	public void setStar_zdj(double star_zdj) {
		Star_zdj = star_zdj;
	}

	public double getStar_zje() {
		return Star_zje;
	}

	public void setStar_zje(double star_zje) {
		Star_zje = star_zje;
	}
}
