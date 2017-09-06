package com.zhiren.jt.het.gongzlgl;

import java.io.Serializable;

public class Liucztbbean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6010610998500478427L;

    private long m_id;//

    private String mingc;// 状态名称

//    private String liucm;// 流程名称

    private int paixh;//

    private int xuh;

    public static long getSerialVersionUID() {
		return serialVersionUID;
	}

//	public String getLiucm() {
//		return liucm;
//	}
//
//	public void setLiucm(String liucm) {
//		this.liucm = liucm;
//	}

	public long getM_id() {
		return m_id;
	}

	public void setM_id(long m_id) {
		this.m_id = m_id;
	}

	public String getMingc() {
		return mingc;
	}

	public void setMingc(String mingc) {
		this.mingc = mingc;
	}

	public int getPaixh() {
		return paixh;
	}

	public void setPaixh(int paixh) {
		this.paixh = paixh;
	}

	public int getXuh() {
		return xuh;
	}

	public void setXuh(int xuh) {
		this.xuh = xuh;
	}

	public Liucztbbean(long id, String mingc,  int paixh, int xuh) {
        this.xuh = xuh;
        this.m_id = id;
        this.mingc = mingc;
//        this.liucm = liucm;
        this.paixh = paixh;
    }
	public Liucztbbean(int xuh) {
        this.xuh = xuh;
    }

}
