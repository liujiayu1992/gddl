package com.zhiren.dtrlgs.fkgl;

import java.io.Serializable;

import java.util.Date;

public class Shengcfkdbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2779051763385785362L;

	private int m_hangh;
	
	private long m_id;//

	private String m_diancmc;//电厂名称

    private String m_jiesdbh;//结算单编号
    
    private String m_jieslx;//结算类型

	private String m_yufkbh;//预付款编号
	
	private String m_fapbh;//发票编号
	
	private String m_jiakhj;//价款合计
	
	private String m_jiaksk;//价款税款
	
	private String m_meikhj;//煤款合计
	
	private double m_hansdj;//含税单价
	
	private String m_yunfhj;//运费合计
	
	private double m_jiessl;//结算数量

	private int m_ches;//车数
	
	
	private boolean m_select;
	
	private String m_fahdw;
	
	private String m_shoukdw;
	
	private double m_hexyfkje;

	public boolean getSelect() {
		return m_select;
	}

	public void setSelect(boolean Select) {
		this.m_select = Select;
	}

	public int getHangh(){
		return m_hangh;
	}
	public void setHangh(int hangh){
		this.m_hangh = hangh;
	}
	
	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public String getFahdw() {
		return m_fahdw;
	}

	public void setFahdw(String fahdw) {
		this.m_fahdw = fahdw;
	}
	
	public String getShoukdw() {
		return m_shoukdw;
	}

	public void setShoukdw(String shoukdw) {
		this.m_shoukdw = shoukdw;
	}
	
	public String getDiancmc() {
		return m_diancmc;
	}

	public void setDiancmc(String diancmc) {
		this.m_diancmc = diancmc;
	}

	public String getJiesdbh() {
		return m_jiesdbh;
	}

	public void setJiesdbh(String jiesdbh) {
		this.m_jiesdbh = jiesdbh;
	}
    
    public String getJieslx() {
        return m_jieslx;
    }

    public void setJieslx(String jieslx) {
        this.m_jieslx = jieslx;
    }

//	public String getYufkbh() {
//		return m_yufkbh;
//	}
//
//	public void setYufkbh(String yufkbh) {
//		this.m_yufkbh = yufkbh;
//	}
	
	public String getFapbh() {
		return m_fapbh;
	}

	public void setFapbh(String fapbh) {
		this.m_fapbh = fapbh;
	}
	
	public String getJiakhj() {
		return m_jiakhj;
	}

	public void setJiakhj(String jiakhj) {
		this.m_jiakhj = jiakhj;
	}
	
	public String getJiaksk() {
		return m_jiaksk;
	}

	public void setJiaksk(String jiaksk) {
		this.m_jiaksk = jiaksk;
	}
	
	public String getMeikhj() {
		return m_meikhj;
	}

	public void setMeikhj(String meikhj) {
		this.m_meikhj = meikhj;
	}
	
	public double getHansdj() {
		return m_hansdj;
	}

	public void setHansdj(double hansdj) {
		this.m_hansdj = hansdj;
	}
	
	public String getYunfhj() {
		return m_yunfhj;
	}

	public void setYunfhj(String yunfhj) {
		this.m_yunfhj = yunfhj;
	}	
	
	public double getJiessl() {
		return m_jiessl;
	}

	public void setJiessl(double jiessl) {
		this.m_jiessl = jiessl;
	}
	
	public int getChes() {
		return m_ches;
	}

	public void setChes(int ches) {
		this.m_ches = ches;
	}

//	public double getHexyfkje() {
//		return m_hexyfkje;
//	}
//
//	public void setHexyfkje(double hexyfkje) {
//		this.m_hexyfkje = hexyfkje;
//	}
	
	public Shengcfkdbean() {
	}

	public Shengcfkdbean(int hangh,long id,String fahdw,String shoukdw,String diancmc,String jiesdbh,String jieslx
			,String fapbh,String jiakhj,String jiaksk,String meikhj,double hansdj,String yunfhj,double jiessl,int ches) {

//		public Shengcfkdbean(int hangh,long id,String fahdw,String shoukdw,String diancmc,String jiesdbh,String jieslx,String yufkbh
//				,String fapbh,String jiakhj,String jiaksk,String meikhj,double hansdj,String yunfhj,double jiessl,int ches,double hexyfkje) {	

		this.m_hangh = hangh;
		this.m_id = id;
		this.m_fahdw = fahdw;
		this.m_shoukdw = shoukdw;
		this.m_diancmc = diancmc;
		this.m_jiesdbh = jiesdbh;
        this.m_jieslx = jieslx;
//		this.m_yufkbh = yufkbh;
		this.m_fapbh = fapbh;
		this.m_jiakhj = jiakhj;
		this.m_jiaksk = jiaksk;
		this.m_meikhj= meikhj;
		this.m_hansdj=hansdj;
		this.m_yunfhj = yunfhj;
		this.m_jiessl = jiessl;
		this.m_ches = ches;
		
//		this.m_hexyfkje=hexyfkje;

	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_hangh);
		buffer.append(',');
		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_fahdw);
		buffer.append(',');
		buffer.append(m_shoukdw);
		buffer.append(',');
		buffer.append(m_diancmc);
		buffer.append(',');
		buffer.append(m_jiesdbh);
		buffer.append(',');
		buffer.append(m_jieslx);
		buffer.append(',');
		buffer.append(m_yufkbh);
		buffer.append(',');
		buffer.append(m_meikhj);
		buffer.append(',');
		buffer.append(m_ches);
		buffer.append(',');
		buffer.append(m_yunfhj);
		buffer.append(',');
		buffer.append(m_jiessl);
		buffer.append(',');
		buffer.append(m_hexyfkje);

		return buffer.toString();
	}

	private long yufkiddiv;
	private String yufkbhdiv;
	private String yufkrqdiv;
	private String yufkjediv;
	private String yufkyediv;
	private double hexyfkdiv;
	private boolean seldiv;
	
	public long getYufkiddiv(){
		return this.yufkiddiv;
	}
	public void setYufkiddiv(long v_yfkid){
		this.yufkiddiv = v_yfkid;
	}
	
	public String getYufkbhdiv(){
		return this.yufkbhdiv;
	}
	public void setYufkbhdiv(String v_yfkbh){
		this.yufkbhdiv = v_yfkbh;
	}
	
	public String getYufkrqdiv(){
		return this.yufkrqdiv;
	}
	public void setYufkrqdiv(String v_yufkrq){
		this.yufkrqdiv = v_yufkrq;
	}
	
	public String getYufkjediv(){
		return this.yufkjediv;
	}
	public void setYufkjediv(String v_yufkje){
		this.yufkjediv = v_yufkje;
	}
	
	public String getYufkyediv(){
		return this.yufkyediv;
	}
	public void setYufkyediv(String v_yufkye){
		this.yufkyediv = v_yufkye;
	}
	
	public double getHexyfkdiv(){
		return this.hexyfkdiv;
	}
	public void setHexyfkdiv(double v_hexyfk){
		this.hexyfkdiv = v_hexyfk;
	}
	
	public boolean getSeldiv(){
		return this.seldiv;
	}
	public void setSeldiv(boolean v_sel){
		this.seldiv = v_sel;
	}
	
	public Shengcfkdbean(long yfkid,boolean sel,String yfkbh,String yfkrq,String yfkje,String yfkye,double hexyfk) {
		this.yufkiddiv = yfkid;
		this.seldiv = sel;
		this.yufkbhdiv = yfkbh;
		this.yufkrqdiv = yfkrq;
		this.yufkjediv = yfkje;
		this.yufkyediv = yfkye;
		this.hexyfkdiv = hexyfk;
	}
}

