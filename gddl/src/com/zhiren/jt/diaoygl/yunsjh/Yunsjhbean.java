package com.zhiren.jt.diaoygl.yunsjh;

import java.io.Serializable;

public class Yunsjhbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -130751886140336084L;

	/**
	 * 
	 */
	
	private long id;

	private long xuh;// ���

	private long dianchangid;

	private String dianchangmc;// �ջ���λ

	private long meikdqid;

	private String meikdqmc;//ú�����

	private long nianf;

	private long yuef;

	private long fazid;//��վid

	private String fazmc;//��վ����

	private long daozid;//��վid

	private String daozmc;//��վ����

	private String pinm;//Ʒ��

	private long pic;//����

	private long pid;//����

	private String pizjhh;//��׼�ƻ���

	private String huanzg;//��װ��

	private String zhongdg;//�յ���

	private String zibbcc;//�Ա�������

	private long shunh;//˳��

	private String tielj;//��·��

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getDaozid() {
		return daozid;
	}

	public void setDaozid(long daozid) {
		this.daozid = daozid;
	}

	public String getDaozmc() {
		return daozmc;
	}

	public void setDaozmc(String daozmc) {
		this.daozmc = daozmc;
	}

	public long getDianchangid() {
		return dianchangid;
	}

	public void setDianchangid(long dianchangid) {
		this.dianchangid = dianchangid;
	}

	public String getDianchangmc() {
		return dianchangmc;
	}

	public void setDianchangmc(String dianchangmc) {
		this.dianchangmc = dianchangmc;
	}

	public long getFazid() {
		return fazid;
	}

	public void setFazid(long fazid) {
		this.fazid = fazid;
	}

	public String getFazmc() {
		return fazmc;
	}

	public void setFazmc(String fazmc) {
		this.fazmc = fazmc;
	}

	public String getHuanzg() {
		return huanzg;
	}

	public void setHuanzg(String huanzg) {
		this.huanzg = huanzg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMeikdqid() {
		return meikdqid;
	}

	public void setMeikdqid(long meikdqid) {
		this.meikdqid = meikdqid;
	}

	public String getMeikdqmc() {
		return meikdqmc;
	}

	public void setMeikdqmc(String meikdqmc) {
		this.meikdqmc = meikdqmc;
	}

	public long getNianf() {
		return nianf;
	}

	public void setNianf(long nianf) {
		this.nianf = nianf;
	}

	public long getPic() {
		return pic;
	}

	public void setPic(long pic) {
		this.pic = pic;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getPinm() {
		return pinm;
	}

	public void setPinm(String pinm) {
		this.pinm = pinm;
	}

	public String getPizjhh() {
		return pizjhh;
	}

	public void setPizjhh(String pizjhh) {
		this.pizjhh = pizjhh;
	}

	public long getShunh() {
		return shunh;
	}

	public void setShunh(long shunh) {
		this.shunh = shunh;
	}

	public String getTielj() {
		return tielj;
	}

	public void setTielj(String tielj) {
		this.tielj = tielj;
	}

	public long getXuh() {
		return xuh;
	}

	public void setXuh(long xuh) {
		this.xuh = xuh;
	}

	public long getYuef() {
		return yuef;
	}

	public void setYuef(long yuef) {
		this.yuef = yuef;
	}

	public String getZhongdg() {
		return zhongdg;
	}

	public void setZhongdg(String zhongdg) {
		this.zhongdg = zhongdg;
	}

	public String getZibbcc() {
		return zibbcc;
	}

	public void setZibbcc(String zibbcc) {
		this.zibbcc = zibbcc;
	}

	public Yunsjhbean(){
		
	}
	
	public Yunsjhbean(long id, long xuh, long dianchangid, String dianchangmc, long meikdqid, String meikdqmc, long nianf, long yuef, long fazid, String fazmc, long daozid, String daozmc, String pinm, long pic, long pid, String pizjhh, String huanzg, String zhongdg, String zibbcc, long shunh, String tielj) {
		super();
		this.id = id;
		this.xuh = xuh;
		this.dianchangid = dianchangid;
		this.dianchangmc = dianchangmc;
		this.meikdqid = meikdqid;
		this.meikdqmc = meikdqmc;
		this.nianf = nianf;
		this.yuef = yuef;
		this.fazid = fazid;
		this.fazmc = fazmc;
		this.daozid = daozid;
		this.daozmc = daozmc;
		this.pinm = pinm;
		this.pic = pic;
		this.pid = pid;
		this.pizjhh = pizjhh;
		this.huanzg = huanzg;
		this.zhongdg = zhongdg;
		this.zibbcc = zibbcc;
		this.shunh = shunh;
		this.tielj = tielj;
	}

	

}