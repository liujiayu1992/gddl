package com.zhiren.dc.hesgl.jiesd;

import java.io.Serializable;
import java.util.Date;

/*
 * 作者：夏峥
 * 时间：2012-05-30
 * 描述：增加gongysb_id,meikxxb_id字段，并重新构造初始化方法
 */

public class Huirdbean implements Serializable {
	private String HUIRD_HETBZ;
	private String HUIRD_GONGFBZ;
	private String HUIRD_JIESBZ;
	private String HUIRD_XIANGCSL;
	private String HUIRD_ZHEJBZ;
	private String HUIRD_ZHEHJE;
	public Huirdbean(String HUIRD_HETBZ, String HUIRD_GONGFBZ, String HUIRD_JIESBZ, String HUIRD_XIANGCSL, String HUIRD_ZHEJBZ, String HUIRD_ZHEHJE) {
		this.HUIRD_HETBZ = HUIRD_HETBZ;
		this.HUIRD_GONGFBZ = HUIRD_GONGFBZ;
		this.HUIRD_JIESBZ = HUIRD_JIESBZ;
		this.HUIRD_XIANGCSL = HUIRD_XIANGCSL;
		this.HUIRD_ZHEJBZ = HUIRD_ZHEJBZ;
		this.HUIRD_ZHEHJE = HUIRD_ZHEHJE;
	}

	public String getHUIRD_HETBZ() {
		return HUIRD_HETBZ;
	}

	public void setHUIRD_HETBZ(String HUIRD_HETBZ) {
		this.HUIRD_HETBZ = HUIRD_HETBZ;
	}

	public String getHUIRD_GONGFBZ() {
		return HUIRD_GONGFBZ;
	}

	public void setHUIRD_GONGFBZ(String HUIRD_GONGFBZ) {
		this.HUIRD_GONGFBZ = HUIRD_GONGFBZ;
	}

	public String getHUIRD_JIESBZ() {
		return HUIRD_JIESBZ;
	}

	public void setHUIRD_JIESBZ(String HUIRD_JIESBZ) {
		this.HUIRD_JIESBZ = HUIRD_JIESBZ;
	}

	public String getHUIRD_XIANGCSL() {
		return HUIRD_XIANGCSL;
	}

	public void setHUIRD_XIANGCSL(String HUIRD_XIANGCSL) {
		this.HUIRD_XIANGCSL = HUIRD_XIANGCSL;
	}

	public String getHUIRD_ZHEJBZ() {
		return HUIRD_ZHEJBZ;
	}

	public void setHUIRD_ZHEJBZ(String HUIRD_ZHEJBZ) {
		this.HUIRD_ZHEJBZ = HUIRD_ZHEJBZ;
	}

	public String getHUIRD_ZHEHJE() {
		return HUIRD_ZHEHJE;
	}

	public void setHUIRD_ZHEHJE(String HUIRD_ZHEHJE) {
		this.HUIRD_ZHEHJE = HUIRD_ZHEHJE;
	}
}
