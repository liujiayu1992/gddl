package com.zhiren.jt.het.gongzlgl;

import java.io.Serializable;

public class Liucbbean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

    private int xuh;

    private String mingc;
    private String liuclbb_id;
    
    public String getLiuclbb_id() {
		return liuclbb_id;
	}

	public void setLiuclbb_id(String liuclbb_id) {
		this.liuclbb_id = liuclbb_id;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMingc() {
        return mingc;
    }

    public void setMingc(String mingc) {
        this.mingc = mingc;
    }

    public int getXuh() {
        return xuh;
    }

    public void setXuh(int xuh) {
        this.xuh = xuh;
    }

    public Liucbbean() {
    }

    public Liucbbean(long id, int xuh, String mingc,String liuclbb_id) {
        super();
        // TODO 自动生成构造函数存根
        this.id = id;
        this.xuh = xuh;
        this.mingc = mingc;
        this.liuclbb_id=liuclbb_id;
    }

}
