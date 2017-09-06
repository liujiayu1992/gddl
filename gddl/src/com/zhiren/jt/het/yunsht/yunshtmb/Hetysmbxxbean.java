package com.zhiren.jt.het.yunsht.yunshtmb;

import java.io.Serializable;
import java.util.Date;

public class Hetysmbxxbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private long hetgysid;
	private long gongysid;
	private long diancxxb_id;//Ðè·½
//	private String hetmc;
	private String hetbh;
	private Date shengxsj;
	private Date guoqsj;
	private Date qiandsj;
	private String qianddd;
	private long jihkjb_id;
	private String GONGFDWMC;
	private String GONGFDWDZ;
	private String GONGFDH;
	private String GONGFFDDBR;
	private String GONGFWTDLR;
	private String GONGFDBGH;
	private String GONGFKHYH;
	private String GONGFZH;
	private String GONGFYZBM;
	private String gongfsh;
	private String XUFDWMC;
	private String XUFDWDZ;
	private String XUFFDDBR;
	private String XUFWTDLR;
	private String XUFDH;
	private String XUFDBGH;
	private String XUFKHYH;
	private String XUFZH;
	private String XUFYZBM;
	private String xufsh;
	private String hetyj;
	private long yunsjgfab_id;
//	private String meiks;
	
//	public String getMeiks() {
//		return meiks;
//	}
//	public void setMeiks(String meiks) {
//		this.meiks = meiks;
//	}
	public String getQianddd() {
		if(qianddd==null){
			qianddd="";
		}
		return qianddd;
	}
	public void setQianddd(String qianddd) {
		this.qianddd = qianddd;
	}
	public long getDiancxxb_id() {
		return diancxxb_id;
	}
	public void setDiancxxb_id(long diancxxb_id) {
		this.diancxxb_id = diancxxb_id;
	}
	public String getGONGFDBGH() {
		if(GONGFDBGH==null){
			GONGFDBGH="";
		}
		return GONGFDBGH;
	}
	public void setGONGFDBGH(String gongfdbgh) {
		GONGFDBGH = gongfdbgh;
	}
	public String getGONGFDH() {
		if(GONGFDH==null){
			GONGFDH="";
		}
		return GONGFDH;
	}
	public void setGONGFDH(String gongfdh) {
		GONGFDH = gongfdh;
	}
	public String getGONGFDWDZ() {
		if(GONGFDWDZ==null){
			GONGFDWDZ="";
		}
		return GONGFDWDZ;
	}
	public void setGONGFDWDZ(String gongfdwdz) {
		GONGFDWDZ = gongfdwdz;
	}
	public String getGONGFDWMC() {
		if(GONGFDWMC==null){
			GONGFDWMC="";
		}
		return GONGFDWMC;
	}
	public void setGONGFDWMC(String gongfdwmc) {
		GONGFDWMC = gongfdwmc;
	}
	public String getGONGFFDDBR() {
		if(GONGFFDDBR==null){
			GONGFFDDBR="";
		}
		return GONGFFDDBR;
	}
	public void setGONGFFDDBR(String gongffddbr) {
		GONGFFDDBR = gongffddbr;
	}
	public String getGONGFKHYH() {
		if(GONGFKHYH==null){
			GONGFKHYH="";
		}
		return GONGFKHYH;
	}
	public void setGONGFKHYH(String gongfkhyh) {
		GONGFKHYH = gongfkhyh;
	}
	public String getGongfsh() {
		if(gongfsh==null){
			gongfsh="";
		}
		return gongfsh;
	}
	public void setGongfsh(String gongfsh) {
		this.gongfsh = gongfsh;
	}
	public String getGONGFWTDLR() {
		if(GONGFWTDLR==null){
			GONGFWTDLR="";
		}
		return GONGFWTDLR;
	}
	public void setGONGFWTDLR(String gongfwtdlr) {
		GONGFWTDLR = gongfwtdlr;
	}
	public String getGONGFYZBM() {
		if(GONGFYZBM==null){
			GONGFYZBM="";
		}
		return GONGFYZBM;
	}
	public void setGONGFYZBM(String gongfyzbm) {
		GONGFYZBM = gongfyzbm;
	}
	public String getGONGFZH() {
		if(GONGFZH==null){
			GONGFZH="";
		}
		return GONGFZH;
	}
	public void setGONGFZH(String gongfzh) {
		GONGFZH = gongfzh;
	}
	public long getGongysid() {
		return gongysid;
	}
	public void setGongysid(long gongysid) {
		this.gongysid = gongysid;
	}
	public Date getGuoqsj() {
		if(guoqsj==null){
			guoqsj=new Date();
		}
		return guoqsj;
	}
	public void setGuoqsj(Date guoqsj) {
		this.guoqsj = guoqsj;
	}
	public String getHetbh() {
		if(hetbh==null){
			hetbh="";
		}
		return hetbh;
	}
	public void setHetbh(String hetbh) {
		this.hetbh = hetbh;
	}
	public long getHetgysid() {
		return hetgysid;
	}
	public void setHetgysid(long hetgysid) {
		this.hetgysid = hetgysid;
	}
	public String getHetyj() {
		if(hetyj==null){
			hetyj="";
		}
		return hetyj;
	}
	public void setHetyj(String hetyj) {
		this.hetyj = hetyj;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getJihkjb_id() {
		return jihkjb_id;
	}
	public void setJihkjb_id(long jihkjb_id) {
		this.jihkjb_id = jihkjb_id;
	}
	
	public long getYunsjgfab_id() {
		return yunsjgfab_id;
	}
	public void setYunsjgfab_id(long jihkjb_id) {
		this.yunsjgfab_id = yunsjgfab_id;
	}
	public Date getQiandsj() {
		if(qiandsj==null){
			qiandsj=new Date();
		}
		return qiandsj;
	}
	public void setQiandsj(Date qiandsj) {
		this.qiandsj = qiandsj;
	}
	public Date getShengxsj() {
		if(shengxsj==null){
			shengxsj=new Date();
		}
		return shengxsj;
	}
	public void setShengxsj(Date shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getXUFDBGH() {
		if(XUFDBGH==null){
			XUFDBGH="";
		}
		return XUFDBGH;
	}
	public void setXUFDBGH(String xufdbgh) {
		XUFDBGH = xufdbgh;
	}
	public String getXUFDH() {
		if(XUFDH==null){
			XUFDH="";
		}
		return XUFDH;
	}
	public void setXUFDH(String xufdh) {
		XUFDH = xufdh;
	}
	public String getXUFDWDZ() {
		if(XUFDWDZ==null){
			XUFDWDZ="";
		}
		return XUFDWDZ;
	}
	public void setXUFDWDZ(String xufdwdz) {
		XUFDWDZ = xufdwdz;
	}
	public String getXUFDWMC() {
		if(XUFDWMC==null){
			XUFDWMC="";
		}
		return XUFDWMC;
	}
	public void setXUFDWMC(String xufdwmc) {
		XUFDWMC = xufdwmc;
	}
	public String getXUFFDDBR() {
		if(XUFFDDBR==null){
			XUFFDDBR="";
		}
		return XUFFDDBR;
	}
	public void setXUFFDDBR(String xuffddbr) {
		XUFFDDBR = xuffddbr;
	}
	public String getXUFKHYH() {
		if(XUFKHYH==null){
			XUFKHYH="";
		}
		return XUFKHYH;
	}
	public void setXUFKHYH(String xufkhyh) {
		XUFKHYH = xufkhyh;
	}
	public String getXufsh() {
		if(xufsh==null){
			xufsh="";
		}
		return xufsh;
	}
	public void setXufsh(String xufsh) {
		this.xufsh = xufsh;
	}
	public String getXUFWTDLR() {
		if(XUFWTDLR==null){
			XUFWTDLR="";
		}
		return XUFWTDLR;
	}
	public void setXUFWTDLR(String xufwtdlr) {
		XUFWTDLR = xufwtdlr;
	}
	public String getXUFYZBM() {
		if(XUFYZBM==null){
			XUFYZBM="";
		}
		return XUFYZBM;
	}
	public void setXUFYZBM(String xufyzbm) {
		XUFYZBM = xufyzbm;
	}
	public String getXUFZH() {
		if(XUFZH==null){
			XUFZH="";
		}
		return XUFZH;
	}
	public void setXUFZH(String xufzh) {
		XUFZH = xufzh;
	}
	
	/*public String getHetmc() {
		if(hetmc==null){
			hetmc="";
		}
		return hetmc;
	}
	public void setHetmc(String hetmc) {
		this.hetmc = hetmc;
	}*/
	
	
	public Hetysmbxxbean() {
		super();
	}
	public Hetysmbxxbean(long id, long hetgysid, long gongysid, long diancxxb_id, String hetbh, Date shengxsj, Date guoqsj, Date qiandsj, String qianddd,long jihkjb_id, String gongfdwmc, String gongfdwdz, String gongfdh, String gongffddbr, String gongfwtdlr, String gongfdbgh, String gongfkhyh, String gongfzh, String gongfyzbm, String gongfsh, String xufdwmc, String xufdwdz, String xuffddbr, String xufwtdlr, String xufdh, String xufdbgh, String xufkhyh, String xufzh, String xufyzbm, String xufsh, String hetyj,long yunsjgfab_id) {
		super();
		this.id = id;
		this.hetgysid = hetgysid;
		this.gongysid = gongysid;
		this.diancxxb_id = diancxxb_id;
		this.hetbh = hetbh;
		this.shengxsj = shengxsj;
		this.guoqsj = guoqsj;
		this.qiandsj = qiandsj;
		this.qianddd=qianddd;
		this.jihkjb_id = jihkjb_id;
		this.yunsjgfab_id=yunsjgfab_id;
		GONGFDWMC = gongfdwmc;
		GONGFDWDZ = gongfdwdz;
		GONGFDH = gongfdh;
		GONGFFDDBR = gongffddbr;
		GONGFWTDLR = gongfwtdlr;
		GONGFDBGH = gongfdbgh;
		GONGFKHYH = gongfkhyh;
		GONGFZH = gongfzh;
		GONGFYZBM = gongfyzbm;
		this.gongfsh = gongfsh;
		XUFDWMC = xufdwmc;
		XUFDWDZ = xufdwdz;
		XUFFDDBR = xuffddbr;
		XUFWTDLR = xufwtdlr;
		XUFDH = xufdh;
		XUFDBGH = xufdbgh;
		XUFKHYH = xufkhyh;
		XUFZH = xufzh;
		XUFYZBM = xufyzbm;
		this.xufsh = xufsh;
		this.hetyj = hetyj;
//		this.meiks=meiksmc;
	}
}
