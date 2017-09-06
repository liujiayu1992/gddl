package com.zhiren.dtrlgs.fkgl;

import java.io.Serializable;

import java.util.Date;

import com.zhiren.main.Visit;

public class Createfktzdbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4309776993362927061L;

	private String mfuksy;
	private String m_Fuktzdbh;
	private String mtianzdw;
	private long mgongysb_id;
	private String mshoukdw;
	private String mbianm;
	private String mdiz;
	private String mkaihyh;
	private String mzhangh;
	
	private String mfapbhs;
	private double mfapje;
	private double mhexyfk;
	private double mshijfk; 
	private String mShijfkdx;
	
	private double mkouyf;//扣运费
	private double mkouhkf;//扣回空费
	private String mshengcfkdrq;
	
	private String mxiangmmc;
	private String mxiangmbh;
	
	
	public String getFuksy(){
		return this.mfuksy;
	}
	public void setFuksy(String fuksy){
		this.mfuksy = fuksy;
	}
	
	public String getFuktzdbh(){
		return this.m_Fuktzdbh;
	}
	public void setFuktzdbh(String fuktzdbh){
		this.m_Fuktzdbh = fuktzdbh;
	}
	
	public String getTianzdw(){
		return this.mtianzdw;
	}
	public void setTianzdw(String tianzdw){
		this.mtianzdw = tianzdw;
	}
	
	public long getGongysb_id(){
		return this.mgongysb_id;
	}
	public void setGongysb_id(long gongysb_id){
		this.mgongysb_id = gongysb_id;
	}
	
	public String getShoukdw(){
		return this.mshoukdw;
	}
	public void setShoukdw(String shoukdw){
		this.mshoukdw = shoukdw;
	}
	
	public String getGongysbm(){
		return this.mbianm;
	}
	public void setGongysbm(String gongysbm){
		this.mbianm = gongysbm;
	}
	
	public String getDiz(){
		return this.mdiz;
	}
	public void setDiz(String diz){
		this.mdiz = diz;
	}
	
	public String getKaihyh(){
		return this.mkaihyh;
	}
	public void setKaihyh(String kaihyh){
		this.mkaihyh = kaihyh;
	}
	
	public String getZhangh(){
		return this.mzhangh;
	}
	public void setZhangh(String zhangh){
		this.mzhangh = zhangh;
	}
	
	public String getFapbhs(){
		return this.mfapbhs;
	}
	public void setFapbhs(String fapbhs){
		this.mfapbhs = fapbhs;
	}
	
	public double getFapje(){
		return this.mfapje;
	}
	public void setFapje(double fapje){
		this.mfapje = fapje;
	}
	
	public double getHexyfk(){
		return this.mhexyfk;
	}
	public void setHexyfk(double hexyfk){
		this.mhexyfk = hexyfk;
	}
	
	public double getShijfk(){
		return this.mshijfk;
	}
	public void setShijfk(double shijfk){
		this.mshijfk = shijfk;
	}
	
	public String  getShijfkdx(){
		return this.mShijfkdx;
	}
	public void setShijfkdx(String shijfkdx){
		this.mShijfkdx = shijfkdx;
	}
	
	public double getKouyf(){
		return this.mkouyf;
	}
	public void setKouyf(double kouyf){
		this.mkouyf = kouyf;
	}
	
	public double getKouhkf(){
		return this.mkouhkf;
	}
	public void setKouhkf(double kouhkf){
		this.mkouhkf = kouhkf;
	}
	
	public String  getShengcfkdrq(){
		return this.mshengcfkdrq;
	}
	public void setShengcfkdrq(String shengcfkdrq){
		this.mshengcfkdrq = shengcfkdrq;
	}
	
	public String  getXiangmmc(){
		return this.mxiangmmc;
	}
	public void setXiangmmc(String xiangmmc){
		this.mxiangmmc = xiangmmc;
	}
	
	public String  getXiangmbh(){
		return this.mxiangmbh;
	}
	public void setXiangmbh(String xiangmbh){
		this.mxiangmbh = xiangmbh;
	}
	
	
	
	public Createfktzdbean() {
	}

	public Createfktzdbean(String fuksy,String Fuktzdbh,String tianzdw,String shengcfkdrq,long gongysb_id,String shoukdw,String bianm,String diz,String kaihyh,String zhangh,String fapbhs,
							double fapje,double hexyfk,double shijfk,String Shijfkdx,double kouyf,double kouhkf,String xiangmmc,String xiangmbh) {
		
		
		this.mfuksy = fuksy;
		this.m_Fuktzdbh=Fuktzdbh;
		if(tianzdw==null){
		    this.mtianzdw="";
		}else{
		    this.mtianzdw = tianzdw;
		}
		this.mshengcfkdrq=shengcfkdrq;
		this.mgongysb_id = gongysb_id;
		this.mshoukdw = shoukdw;
		this.mbianm = bianm;
		this.mdiz = diz;
		this.mkaihyh=kaihyh;
		this.mzhangh = zhangh;
		this.mfapbhs = fapbhs;
		this.mfapje = fapje;
		this.mhexyfk = hexyfk;
		this.mshijfk = shijfk;		
		this.mShijfkdx = Shijfkdx;
		this.mkouyf = kouyf;
		this.mkouhkf = kouhkf;
		
		this.mxiangmmc = xiangmmc;
		this.mxiangmbh = xiangmbh;
	}
	
	public Createfktzdbean(long id,double old_value,double db_value){
		this.id = id;
		this.oldvalue = old_value;
		this.dbvalue = db_value;
	}
	private long id;
	private double oldvalue;
	private double dbvalue;
	public long getId(){
		return this.id;
	}
	public void setId(long _id){
		this.id=_id;
	}
	
	public double getOldvalue(){
		return this.oldvalue;
	}
	public void setOldvalue(double _olddb){
		this.oldvalue = _olddb;
	}

	public double getDbvalue(){
		return this.dbvalue;
	}
	public void setDbvalue(double _db){
		this.dbvalue = _db;
	}
}
