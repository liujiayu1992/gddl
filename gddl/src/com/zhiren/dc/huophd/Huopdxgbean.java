package com.zhiren.dc.huophd;

/*
 * 时间：2007-11-05
 * 作者：Qiuzuwei
 * 修改原因：增加了核对费用中使用的变量：毛重、皮重、运损、扣吨、扣杂、煤管量
 * */
/*
 * 时间：2006-12-15
 * 作者：Qiuzuwei
 * 内容：增加了记录标重调整的字段
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Huopdxgbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8541725537928253816L;

	private long Faz_id;

	private String Faz;

	private long Daoz_id;

	private String Daoz;

	private String cheb;

	private String Biaoz;

	private String Fahrq;

	private String Yunjlc;

	private double Baojje;

	private String Fahdw;

	private String Diancmc;

	private String Chephs;
	
	private String Beiz;

	private String Piaojbh;

	private double Hej;

	private String Feiymc0;

	private double Jine0;
	
	private String Feiymc1;

	private double Jine1;

	private String Feiymc2;

	private double Jine2;

	private String Feiymc3;

	private double Jine3;

	private String Feiymc4;

	private double Jine4;

	private String Feiymc5;

	private double Jine5;

	private String Feiymc6;

	private double Jine6;

	private String Feiymc7;

	private double Jine7;

	private String Feiymc8;

	private double Jine8;

	private String Feiymc9;

	private double Jine9;

	private String Feiymc10;

	private double Jine10;

	private String Feiymc11;

	private double Jine11;

	private String Feiymc12;

	private double Jine12;

	private String Feiymc13;

	private double Jine13;

	private String Feiymc14;

	private double Jine14;

	private String Feiymc15;

	private double Jine15;

	private String Feiymc16;

	private double Jine16;

	private String Feiymc17;

	private double Jine17;

	private String Feiymc18;

	private double Jine18;
	
	private long feiyid0 = -1;

	private long feiyid1 = -1;

	private long feiyid2 = -1;

	private long feiyid3 = -1;

	private long feiyid4 = -1;

	private long feiyid5 = -1;

	private long feiyid6 = -1;

	private long feiyid7 = -1;

	private long feiyid8 = -1;

	private long feiyid9 = -1;

	private long feiyid10 = -1;

	private long feiyid11 = -1;

	private long feiyid12 = -1;

	private long feiyid13 = -1;

	private long feiyid14 = -1;

	private long feiyid15 = -1;

	private long feiyid16 = -1;

	private long feiyid17 = -1;

	private long feiyid18 = -1;
	
	private double m_maoz = 0.0;

	private double m_piz = 0.0;

	private double m_yuns = 0.0;

	private double m_koud = 0.0;

	private double m_kouz = 0.0;

	private double m_meigl = 0.0;
	
	private int m_shuib0;
	
	private int m_shuib1;
	
	private int m_shuib2;
	
	private int m_shuib3;
	
	private int m_shuib4;
	
	private int m_shuib5;
	
	private int m_shuib6;
	
	private int m_shuib7;
	
	private int m_shuib8;
	
	private int m_shuib9;
	
	private int m_shuib10;
	
	private int m_shuib11;
	
	private int m_shuib12;
	
	private int m_shuib13;
	
	private int m_shuib14;
	
	private int m_shuib15;
	
	private int m_shuib16;
	
	private int m_shuib17;
	
	private int m_shuib18;

	private List lstHuoptz = new ArrayList();

	public String getCheb() {
		return cheb;
	}

	public void setCheb(String _cheb) {
		cheb = _cheb;
	}

	/**
	 * @return feiyid0
	 */
	public long getHPFEIYID0() {
		return feiyid0;
	}
	/**
	 * @param feiyid0 要设置的 feiyid0
	 */
	public void setHPFEIYID0(long feiyid0) {
		this.feiyid0 = feiyid0;
	}
	
	public long getHPFEIYID1() {
		return feiyid1;
	}

	public void setHPFEIYID1(long id) {
		feiyid1 = id;
	}

	public long getHPFEIYID2() {
		return feiyid2;
	}

	public void setHPFEIYID2(long id) {
		feiyid2 = id;
	}

	public long getHPFEIYID3() {
		return feiyid3;
	}

	public void setHPFEIYID3(long id) {
		feiyid3 = id;
	}

	public long getHPFEIYID4() {
		return feiyid4;
	}

	public void setHPFEIYID4(long id) {
		feiyid4 = id;
	}

	public long getHPFEIYID5() {
		return feiyid5;
	}

	public void setHPFEIYID5(long id) {
		feiyid5 = id;
	}

	public long getHPFEIYID6() {
		return feiyid6;
	}

	public void setHPFEIYID6(long id) {
		feiyid6 = id;
	}

	public long getHPFEIYID7() {
		return feiyid7;
	}

	public void setHPFEIYID7(long id) {
		feiyid7 = id;
	}

	public long getHPFEIYID8() {
		return feiyid8;
	}

	public void setHPFEIYID8(long id) {
		feiyid8 = id;
	}

	public long getHPFEIYID9() {
		return feiyid9;
	}

	public void setHPFEIYID9(long id) {
		feiyid9 = id;
	}

	public long getHPFEIYID10() {
		return feiyid10;
	}

	public void setHPFEIYID10(long id) {
		feiyid10 = id;
	}

	public long getHPFEIYID11() {
		return feiyid11;
	}

	public void setHPFEIYID11(long id) {
		feiyid11 = id;
	}

	public long getHPFEIYID12() {
		return feiyid12;
	}

	public void setHPFEIYID12(long id) {
		feiyid12 = id;
	}

	public long getHPFEIYID13() {
		return feiyid13;
	}

	public void setHPFEIYID13(long id) {
		feiyid13 = id;
	}

	public long getHPFEIYID14() {
		return feiyid14;
	}

	public void setHPFEIYID14(long id) {
		feiyid14 = id;
	}

	public long getHPFEIYID15() {
		return feiyid15;
	}

	public void setHPFEIYID15(long id) {
		feiyid15 = id;
	}

	public long getHPFEIYID16() {
		return feiyid16;
	}

	public void setHPFEIYID16(long id) {
		feiyid16 = id;
	}

	public long getHPFEIYID17() {
		return feiyid17;
	}

	public void setHPFEIYID17(long id) {
		feiyid17 = id;
	}

	public long getHPFEIYID18() {
		return feiyid18;
	}

	public void setHPFEIYID18(long id) {
		feiyid18 = id;
	}

	public String getFaz() {
		return Faz;
	}

	public void setFaz(String faz) {
		this.Faz = faz;
	}

	public String getDaoz() {
		return Daoz;
	}

	public void setDaoz(String daoz) {
		this.Daoz = daoz;
	}

	public String getBiaoz() {
		return Biaoz;
	}

	public void setBiaoz(String biaoz) {
		this.Biaoz = biaoz;
	}

	public String getFahrq() {
		return Fahrq;
	}

	public void setFahrq(String fahrq) {
		this.Fahrq = fahrq;
	}

	public String getYunjlc() {
		return Yunjlc;
	}

	public void setYunjlc(String yunjlc) {
		this.Yunjlc = yunjlc;
	}

	public double getBaojje() {
		return Baojje;
	}

	public void setBaojje(double baojje) {
		this.Baojje = baojje;
	}

	public String getFahdw() {
		return Fahdw;
	}

	public void setFahdw(String fahdw) {
		this.Fahdw = fahdw;
	}

	public String getDiancmc() {
		return Diancmc;
	}

	public void setDiancmc(String diancmc) {
		this.Diancmc = diancmc;
	}

	public String getChephs() {
		return Chephs;
	}

	public void setChephs(String chephs) {
		this.Chephs = chephs;
	}

	public String getChepH() {
		return Chephs;
	}

	public void setChepH(String cheph) {
		this.Chephs = cheph;
	}

	public String getBeiz() {
		return Beiz;
	}

	public void setBeiz(String beiz) {
		this.Beiz = beiz;
	}

	public String getPiaojbh() {
		return Piaojbh;
	}

	public void setPiaojbh(String bianh) {
		this.Piaojbh = bianh;
	}

	public double getHej() {
		return Hej;
	}

	public void setHej(double hej) {
		this.Hej = hej;
	}

	public String getFeiymc1() {
		return Feiymc1;
	}

	public void setFeiymc1(String Feiymc1) {
		this.Feiymc1 = Feiymc1;
	}

	public double getJine1() {
		return Jine1;
	}

	public void setJine1(double jine1) {
		this.Jine1 = jine1;
	}

	public String getFeiymc2() {
		return Feiymc2;
	}

	public void setFeiymc2(String Feiymc2) {
		this.Feiymc2 = Feiymc2;
	}

	public double getJine2() {
		return Jine2;
	}

	public void setJine2(double jine2) {
		this.Jine2 = jine2;
	}

	public String getFeiymc3() {
		return Feiymc3;
	}

	public void setFeiymc3(String Feiymc3) {
		this.Feiymc3 = Feiymc3;
	}

	public double getJine3() {
		return Jine3;
	}

	public void setJine3(double jine3) {
		this.Jine3 = jine3;
	}

	public String getFeiymc4() {
		return Feiymc4;
	}

	public void setFeiymc4(String Feiymc4) {
		this.Feiymc4 = Feiymc4;
	}

	public double getJine4() {
		return Jine4;
	}

	public void setJine4(double jine4) {
		this.Jine4 = jine4;
	}

	public String getFeiymc5() {
		return Feiymc5;
	}

	public void setFeiymc5(String Feiymc5) {
		this.Feiymc5 = Feiymc5;
	}

	public double getJine5() {
		return Jine5;
	}

	public void setJine5(double jine5) {
		this.Jine5 = jine5;
	}

	public String getFeiymc6() {
		return Feiymc6;
	}

	public void setFeiymc6(String Feiymc6) {
		this.Feiymc6 = Feiymc6;
	}

	public double getJine6() {
		return Jine6;
	}

	public void setJine6(double jine6) {
		this.Jine6 = jine6;
	}

	public String getFeiymc7() {
		return Feiymc7;
	}

	public void setFeiymc7(String Feiymc7) {
		this.Feiymc7 = Feiymc7;
	}

	public double getJine7() {
		return Jine7;
	}

	public void setJine7(double jine7) {
		this.Jine7 = jine7;
	}

	public String getFeiymc8() {
		return Feiymc8;
	}

	public void setFeiymc8(String Feiymc8) {
		this.Feiymc8 = Feiymc8;
	}

	public double getJine8() {
		return Jine8;
	}

	public void setJine8(double jine8) {
		this.Jine8 = jine8;
	}

	public String getFeiymc9() {
		return Feiymc9;
	}

	public void setFeiymc9(String Feiymc9) {
		this.Feiymc9 = Feiymc9;
	}

	public double getJine9() {
		return Jine9;
	}

	public void setJine9(double jine9) {
		this.Jine9 = jine9;
	}

	public String getFeiymc10() {
		return Feiymc10;
	}

	public void setFeiymc10(String Feiymc10) {
		this.Feiymc10 = Feiymc10;
	}

	public double getJine10() {
		return Jine10;
	}

	public void setJine10(double jine10) {
		this.Jine10 = jine10;
	}

	public String getFeiymc11() {
		return Feiymc11;
	}

	public void setFeiymc11(String Feiymc11) {
		this.Feiymc11 = Feiymc11;
	}

	public double getJine11() {
		return Jine11;
	}

	public void setJine11(double jine11) {
		this.Jine11 = jine11;
	}

	public String getFeiymc12() {
		return Feiymc12;
	}

	public void setFeiymc12(String Feiymc12) {
		this.Feiymc12 = Feiymc12;
	}

	public double getJine12() {
		return Jine12;
	}

	public void setJine12(double jine12) {
		this.Jine12 = jine12;
	}

	public String getFeiymc13() {
		return Feiymc13;
	}

	public void setFeiymc13(String Feiymc13) {
		this.Feiymc13 = Feiymc13;
	}

	public double getJine13() {
		return Jine13;
	}

	public void setJine13(double jine13) {
		this.Jine13 = jine13;
	}

	public String getFeiymc14() {
		return Feiymc14;
	}

	public void setFeiymc14(String Feiymc14) {
		this.Feiymc14 = Feiymc14;
	}

	public double getJine14() {
		return Jine14;
	}

	public void setJine14(double jine14) {
		this.Jine14 = jine14;
	}

	public String getFeiymc15() {
		return Feiymc15;
	}

	public void setFeiymc15(String Feiymc15) {
		this.Feiymc15 = Feiymc15;
	}

	public double getJine15() {
		return Jine15;
	}

	public void setJine15(double jine15) {
		this.Jine15 = jine15;
	}

	public String getFeiymc16() {
		return Feiymc16;
	}

	public void setFeiymc16(String Feiymc16) {
		this.Feiymc16 = Feiymc16;
	}

	public double getJine16() {
		return Jine16;
	}

	public void setJine16(double jine16) {
		this.Jine16 = jine16;
	}

	public String getFeiymc17() {
		return Feiymc17;
	}

	public void setFeiymc17(String Feiymc17) {
		this.Feiymc17 = Feiymc17;
	}

	public double getJine17() {
		return Jine17;
	}

	public void setJine17(double jine17) {
		this.Jine17 = jine17;
	}

	public String getFeiymc18() {
		return Feiymc18;
	}

	public void setFeiymc18(String Feiymc18) {
		this.Feiymc18 = Feiymc18;
	}

	public double getJine18() {
		return Jine18;
	}

	public void setJine18(double jine18) {
		this.Jine18 = jine18;
	}

	private String m_biaoz = "";

	public String getMultiBiaoz() {
		return m_biaoz;
	}

	public void setMultiBiaoz(String biaoz) {
		m_biaoz = biaoz;
	}


	public double getMaoz() {
		return m_maoz;
	}

	public void setMaoz(double maoz) {
		m_maoz = maoz;
	}

	public double getPiz() {
		return m_piz;
	}

	public void setPiz(double piz) {
		m_piz = piz;
	}

	public double getYuns() {
		return m_yuns;
	}

	public void setYuns(double yuns) {
		m_yuns = yuns;
	}

	public double getKoud() {
		return m_koud;
	}

	public void setKoud(double koud) {
		m_koud = koud;
	}

	public double getKouz() {
		return m_kouz;
	}

	public void setKouz(double kouz) {
		m_kouz = kouz;
	}

	public double getMeigl() {
		return m_meigl;
	}

	public void setMeigl(double meigl) {
		m_meigl = meigl;
	}

	public List getHuoptz() {
		return lstHuoptz;
	}

	public void setHuoptz(List lst) {
		lstHuoptz = lst;
	}
	
	/**
	 * @return daoz_id
	 */
	public long getDaoz_id() {
		return Daoz_id;
	}

	/**
	 * @param daoz_id
	 *            要设置的 daoz_id
	 */
	public void setDaoz_id(long daoz_id) {
		Daoz_id = daoz_id;
	}

	/**
	 * @return faz_id
	 */
	public long getFaz_id() {
		return Faz_id;
	}

	/**
	 * @param faz_id
	 *            要设置的 faz_id
	 */
	public void setFaz_id(long faz_id) {
		Faz_id = faz_id;
	}
	
	/**
	 * @return Feiymc0
	 */
	public String getFeiymc0() {
		return Feiymc0;
	}

	/**
	 * @param Feiymc0 要设置的 Feiymc0
	 */
	public void setFeiymc0(String Feiymc0) {
		this.Feiymc0 = Feiymc0;
	}

	/**
	 * @return jine0
	 */
	public double getJine0() {
		return Jine0;
	}

	/**
	 * @param jine0 要设置的 jine0
	 */
	public void setJine0(double jine0) {
		Jine0 = jine0;
	}
	

	/**
	 * @return m_shuib0
	 */
	public int getShuib0() {
		return m_shuib0;
	}

	/**
	 * @param m_shuib0 要设置的 m_shuib0
	 */
	public void setShuib0(int m_shuib0) {
		this.m_shuib0 = m_shuib0;
	}

	/**
	 * @return m_shuib1
	 */
	public int getShuib1() {
		return m_shuib1;
	}

	/**
	 * @param m_shuib1 要设置的 m_shuib1
	 */
	public void setShuib1(int m_shuib1) {
		this.m_shuib1 = m_shuib1;
	}

	/**
	 * @return m_shuib10
	 */
	public int getShuib10() {
		return m_shuib10;
	}

	/**
	 * @param m_shuib10 要设置的 m_shuib10
	 */
	public void setShuib10(int m_shuib10) {
		this.m_shuib10 = m_shuib10;
	}

	/**
	 * @return m_shuib11
	 */
	public int getShuib11() {
		return m_shuib11;
	}

	/**
	 * @param m_shuib11 要设置的 m_shuib11
	 */
	public void setShuib11(int m_shuib11) {
		this.m_shuib11 = m_shuib11;
	}

	/**
	 * @return m_shuib12
	 */
	public int getShuib12() {
		return m_shuib12;
	}

	/**
	 * @param m_shuib12 要设置的 m_shuib12
	 */
	public void setShuib12(int m_shuib12) {
		this.m_shuib12 = m_shuib12;
	}

	/**
	 * @return m_shuib13
	 */
	public int getShuib13() {
		return m_shuib13;
	}

	/**
	 * @param m_shuib13 要设置的 m_shuib13
	 */
	public void setShuib13(int m_shuib13) {
		this.m_shuib13 = m_shuib13;
	}

	/**
	 * @return m_shuib14
	 */
	public int getShuib14() {
		return m_shuib14;
	}

	/**
	 * @param m_shuib14 要设置的 m_shuib14
	 */
	public void setShuib14(int m_shuib14) {
		this.m_shuib14 = m_shuib14;
	}

	/**
	 * @return m_shuib15
	 */
	public int getShuib15() {
		return m_shuib15;
	}

	/**
	 * @param m_shuib15 要设置的 m_shuib15
	 */
	public void setShuib15(int m_shuib15) {
		this.m_shuib15 = m_shuib15;
	}

	/**
	 * @return m_shuib16
	 */
	public int getShuib16() {
		return m_shuib16;
	}

	/**
	 * @param m_shuib16 要设置的 m_shuib16
	 */
	public void setShuib16(int m_shuib16) {
		this.m_shuib16 = m_shuib16;
	}

	/**
	 * @return m_shuib17
	 */
	public int getShuib17() {
		return m_shuib17;
	}

	/**
	 * @param m_shuib17 要设置的 m_shuib17
	 */
	public void setShuib17(int m_shuib17) {
		this.m_shuib17 = m_shuib17;
	}

	/**
	 * @return m_shuib18
	 */
	public int getShuib18() {
		return m_shuib18;
	}

	/**
	 * @param m_shuib18 要设置的 m_shuib18
	 */
	public void setShuib18(int m_shuib18) {
		this.m_shuib18 = m_shuib18;
	}

	/**
	 * @return m_shuib2
	 */
	public int getShuib2() {
		return m_shuib2;
	}

	/**
	 * @param m_shuib2 要设置的 m_shuib2
	 */
	public void setShuib2(int m_shuib2) {
		this.m_shuib2 = m_shuib2;
	}

	/**
	 * @return m_shuib3
	 */
	public int getShuib3() {
		return m_shuib3;
	}

	/**
	 * @param m_shuib3 要设置的 m_shuib3
	 */
	public void setShuib3(int m_shuib3) {
		this.m_shuib3 = m_shuib3;
	}

	/**
	 * @return m_shuib4
	 */
	public int getShuib4() {
		return m_shuib4;
	}

	/**
	 * @param m_shuib4 要设置的 m_shuib4
	 */
	public void setShuib4(int m_shuib4) {
		this.m_shuib4 = m_shuib4;
	}

	/**
	 * @return m_shuib5
	 */
	public int getShuib5() {
		return m_shuib5;
	}

	/**
	 * @param m_shuib5 要设置的 m_shuib5
	 */
	public void setShuib5(int m_shuib5) {
		this.m_shuib5 = m_shuib5;
	}

	/**
	 * @return m_shuib6
	 */
	public int getShuib6() {
		return m_shuib6;
	}

	/**
	 * @param m_shuib6 要设置的 m_shuib6
	 */
	public void setShuib6(int m_shuib6) {
		this.m_shuib6 = m_shuib6;
	}

	/**
	 * @return m_shuib7
	 */
	public int getShuib7() {
		return m_shuib7;
	}

	/**
	 * @param m_shuib7 要设置的 m_shuib7
	 */
	public void setShuib7(int m_shuib7) {
		this.m_shuib7 = m_shuib7;
	}

	/**
	 * @return m_shuib8
	 */
	public int getShuib8() {
		return m_shuib8;
	}

	/**
	 * @param m_shuib8 要设置的 m_shuib8
	 */
	public void setShuib8(int m_shuib8) {
		this.m_shuib8 = m_shuib8;
	}

	/**
	 * @return m_shuib9
	 */
	public int getShuib9() {
		return m_shuib9;
	}

	/**
	 * @param m_shuib9 要设置的 m_shuib9
	 */
	public void setShuib9(int m_shuib9) {
		this.m_shuib9 = m_shuib9;
	}

	public Huopdxgbean() {
	}

	public Huopdxgbean(long faz_id, String faz, long daoz_id, String daoz,
			String cheb, String biaoz, String fahrq,
			String yunjlc, double baojje, String fahdw, String diancmc,
			String chephs, String beiz, String bianh, double hej, 
			String Feiymc0,double jine0,String Feiymc1,double jine1, 
			String Feiymc2, double jine2,	String Feiymc3,double jine3, 
			String Feiymc4, double jine4, String Feiymc5,double jine5, 
			String Feiymc6, double jine6, String Feiymc7,double jine7, 
			String Feiymc8, double jine8, String Feiymc9,double jine9, 
			String Feiymc10, double jine10, String Feiymc11,double jine11, 
			String Feiymc12, double jine12, String Feiymc13,double jine13, 
			String Feiymc14, double jine14, String Feiymc15,double jine15, 
			String Feiymc16, double jine16, String Feiymc17,double jine17, 
			String Feiymc18, double jine18, 
			long v_feiyid0,long v_feiyid1,long v_feiyid2,long v_feiyid3, 
			long v_feiyid4,	long v_feiyid5,	long v_feiyid6, long v_feiyid7, 
			long v_feiyid8,	long v_feiyid9,	long v_feiyid10, long v_feiyid11, 
			long v_feiyid12,long v_feiyid13,long v_feiyid14, long v_feiyid15,
			long v_feiyid16,long v_feiyid17,long v_feiyid18,
			int m_shuib0,int m_shuib1,int m_shuib2,int m_shuib3,int m_shuib4,
			int m_shuib5,int m_shuib6,int m_shuib7,int m_shuib8,int m_shuib9,
			int m_shuib10,int m_shuib11,int m_shuib12,int m_shuib13,int m_shuib14,
			int m_shuib15,int m_shuib16,int m_shuib17,int m_shuib18
		) {
		
		// this.Piaojbh = piaojbh;
		this.Faz_id = faz_id;
		this.Faz = faz;
		this.Daoz_id = daoz_id;
		this.Daoz = daoz;
		this.cheb = cheb;
		this.Biaoz = biaoz;
		this.Fahrq = fahrq;
		this.Yunjlc = yunjlc;
		this.Baojje = baojje;
		this.Fahdw = fahdw;
		this.Diancmc = diancmc;
		this.Chephs = chephs;
		this.Beiz = beiz;
		this.Piaojbh = bianh;
		this.Hej = hej;
		this.Feiymc0=Feiymc0;
		this.Jine0=jine0;
		this.Feiymc1 = Feiymc1;
		this.Jine1 = jine1;
		this.Feiymc2 = Feiymc2;
		this.Jine2 = jine2;
		this.Feiymc3 = Feiymc3;
		this.Jine3 = jine3;
		this.Feiymc4 = Feiymc4;
		this.Jine4 = jine4;
		this.Feiymc5 = Feiymc5;
		this.Jine5 = jine5;
		this.Feiymc6 = Feiymc6;
		this.Jine6 = jine6;
		this.Feiymc7 = Feiymc7;
		this.Jine7 = jine7;
		this.Feiymc8 = Feiymc8;
		this.Jine8 = jine8;
		this.Feiymc9 = Feiymc9;
		this.Jine9 = jine9;
		this.Feiymc10 = Feiymc10;
		this.Jine10 = jine10;
		this.Feiymc11 = Feiymc11;
		this.Jine11 = jine11;
		this.Feiymc12 = Feiymc12;
		this.Jine12 = jine12;
		this.Feiymc13 = Feiymc13;
		this.Jine13 = jine13;
		this.Feiymc14 = Feiymc14;
		this.Jine14 = jine14;
		this.Feiymc15 = Feiymc15;
		this.Jine15 = jine15;
		this.Feiymc16 = Feiymc16;
		this.Jine16 = jine16;
		this.Feiymc17 = Feiymc17;
		this.Jine17 = jine17;
		this.Feiymc18 = Feiymc18;
		this.Jine18 = jine18;
		this.feiyid0 = v_feiyid0;
		this.feiyid1 = v_feiyid1;
		this.feiyid2 = v_feiyid2;
		this.feiyid3 = v_feiyid3;
		this.feiyid4 = v_feiyid4;
		this.feiyid5 = v_feiyid5;
		this.feiyid6 = v_feiyid6;
		this.feiyid7 = v_feiyid7;
		this.feiyid8 = v_feiyid8;
		this.feiyid9 = v_feiyid9;
		this.feiyid10 = v_feiyid10;
		this.feiyid11 = v_feiyid11;
		this.feiyid12 = v_feiyid12;
		this.feiyid13 = v_feiyid13;
		this.feiyid14 = v_feiyid14;
		this.feiyid15 = v_feiyid15;
		this.feiyid16 = v_feiyid16;
		this.feiyid17 = v_feiyid17;
		this.feiyid18 = v_feiyid18;
		this.m_shuib0=m_shuib0;
		this.m_shuib1=m_shuib1;
		this.m_shuib2=m_shuib2;
		this.m_shuib3=m_shuib3;
		this.m_shuib4=m_shuib4;
		this.m_shuib5=m_shuib5;
		this.m_shuib6=m_shuib6;
		this.m_shuib7=m_shuib7;
		this.m_shuib8=m_shuib8;
		this.m_shuib9=m_shuib9;
		this.m_shuib10=m_shuib10;
		this.m_shuib11=m_shuib11;
		this.m_shuib12=m_shuib12;
		this.m_shuib13=m_shuib13;
		this.m_shuib14=m_shuib14;
		this.m_shuib15=m_shuib15;
		this.m_shuib16=m_shuib16;
		this.m_shuib17=m_shuib17;
		this.m_shuib18=m_shuib18;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("");

		buffer.append(Faz);
		buffer.append(',');
		buffer.append(Daoz);
		buffer.append(',');
		buffer.append(cheb);
		buffer.append(',');
		buffer.append(Biaoz);
		buffer.append(',');
		buffer.append(Fahrq);
		buffer.append(',');
		buffer.append(Yunjlc);
		buffer.append(',');
		buffer.append(Baojje);
		buffer.append(',');
		buffer.append(Fahdw);
		buffer.append(',');
		buffer.append(Diancmc);
		buffer.append(',');
		buffer.append(Chephs);
		buffer.append(',');
		buffer.append(Beiz);
		buffer.append(',');
		buffer.append(Hej);
		buffer.append(',');
		buffer.append(Feiymc1);
		buffer.append(',');
		buffer.append(Jine1);
		buffer.append(',');
		buffer.append(Feiymc2);
		buffer.append(',');
		buffer.append(Jine2);
		buffer.append(',');
		buffer.append(Feiymc3);
		buffer.append(',');
		buffer.append(Jine3);
		buffer.append(',');
		buffer.append(Feiymc4);
		buffer.append(',');
		buffer.append(Jine4);
		buffer.append(',');
		buffer.append(Feiymc5);
		buffer.append(',');
		buffer.append(Jine5);
		buffer.append(',');
		buffer.append(Feiymc6);
		buffer.append(',');
		buffer.append(Jine6);
		buffer.append(',');
		buffer.append(Feiymc7);
		buffer.append(',');
		buffer.append(Jine7);
		buffer.append(',');
		buffer.append(Feiymc8);
		buffer.append(',');
		buffer.append(Jine8);
		buffer.append(',');
		buffer.append(Feiymc9);
		buffer.append(',');
		buffer.append(Jine9);
		buffer.append(',');
		buffer.append(Feiymc10);
		buffer.append(',');
		buffer.append(Jine10);
		buffer.append(',');
		buffer.append(Feiymc11);
		buffer.append(',');
		buffer.append(Jine11);
		buffer.append(',');
		buffer.append(Feiymc12);
		buffer.append(',');
		buffer.append(Jine12);
		buffer.append(',');
		buffer.append(Feiymc13);
		buffer.append(',');
		buffer.append(Jine13);
		buffer.append(',');
		buffer.append(Feiymc14);
		buffer.append(',');
		buffer.append(Jine14);
		buffer.append(',');
		buffer.append(Feiymc15);
		buffer.append(',');
		buffer.append(Jine15);
		buffer.append(',');
		buffer.append(Feiymc16);
		buffer.append(',');
		buffer.append(Jine16);
		buffer.append(',');
		buffer.append(Feiymc17);
		buffer.append(',');
		buffer.append(Jine17);
		buffer.append(',');
		buffer.append(Feiymc18);
		buffer.append(',');
		buffer.append(Jine18);

		return buffer.toString();
	}

}
