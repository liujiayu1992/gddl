package com.zhiren.jt.het.yunsht.yunshtmb;

import java.io.Serializable;

public class Yunsmbjijbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int xuh;
	private long id;
	private String meikxxb_id;
	private double yunjia;//运价
//	private double yunj;//运距
	private String yunjdw_id;//运价单位
//	private String yunjudw_id;//运距单位
	
	private String zhibb_id;//指标表ID
	private double shangx;//上限
	private double xiax;//下限
	private String tiaoj_id;//条件ID
	private String zhibdw_id;//指标单位ID
	
	
	public String getZhibdw_id(){
		return this.zhibdw_id;
	}
	public void setZhibdw_id(String zhibdw_id){
		this.zhibdw_id = zhibdw_id;
	}
	
	public double getShangx(){
		return this.shangx;
	}
	public void setShangx(double shangx){
		this.shangx = shangx;
	}
	
	public double getXiax(){
		return this.xiax;
	}
	public void setXiax(double xiax){
		this.xiax = xiax;
	}
	
	public String getTiaoj_id(){
		return this.tiaoj_id;
	}
	public void setTiaoj_id(String tiaoj_id){
		this.tiaoj_id = tiaoj_id;
	}
	
	public String getZhibb_id(){
		return this.zhibb_id;
	}
	public void setZhibb_id(String zhibbid){
		this.zhibb_id = zhibbid;
	}
	
	public String getMeikxxb_id() {
		return meikxxb_id;
	}
	public void setMeikxxb_id(String meikxxb_id) {
		this.meikxxb_id = meikxxb_id;
	}
	public int getXuh() {
		return xuh;
	}
	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getYunjia() {
		return yunjia;
	}
	public void setYunjia(double Yunjia) {
		this.yunjia = Yunjia;
	}
//	public double getYunj() {
//		return yunj;
//	}
//	public void setYunj(double yunj) {
//		this.yunj = yunj;
//	}
	public String getYunjdw_id() {
		return yunjdw_id;
	}
	public void setYunjdw_id(String yunjdw_id) {
		this.yunjdw_id = yunjdw_id;
	}
	
//	public String getYunjudw_id(){
//		return this.yunjudw_id;
//	}
//	public void setYunjudw_id(String yunjudw){
//		this.yunjudw_id = yunjudw;
//	}
	
	
	public Yunsmbjijbean(int xuh,String danw){
		this.xuh=xuh;
		this.yunjia = 0;
	}
	public Yunsmbjijbean(){
		
	}
	public Yunsmbjijbean(int xuh,long id, String meikxxb_id, double yunjia, String yunjdw_id, String zhibb_id, String zhibdw_id, String tiaoj_id,double shangx,double xiax) {
		super();
		this.id = id;
		this.meikxxb_id = meikxxb_id;
		this.yunjia = yunjia;
		this.zhibdw_id = zhibdw_id;
//		this.yunj = yunj;
		this.yunjdw_id = yunjdw_id;
		this.xuh=xuh;
//		this.yunjudw_id = yunjudw_id;
		this.zhibb_id = zhibb_id;
		this.tiaoj_id = tiaoj_id;
		this.shangx = shangx;
		this.xiax = xiax;
	}
	
}
