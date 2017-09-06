package com.zhiren.jt.het.renyjs;

import java.io.Serializable;
import java.util.Date;

public class Renyjsbean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1384175805146815550L;

    private int m_id;//

    private String RENYXXB_ID;// Ãû³Æ

    private String JUESB_ID;//
    private Date qissj;
    private Date guoqsj;
    private String shouqr_id;
    

    // ID RENYXXB_ID JUESB_ID SHENHLX
    private int xuh;

    public int getXuh() {
        return xuh;
    }

    public void setXuh(int xuh) {
        this.xuh = xuh;
    }

    public int getId() {
        return m_id;
    }

    public void setId(int id) {
        this.m_id = id;
    }

    public String getJUESB_ID() {
        return JUESB_ID;
    }

    public void setJUESB_ID(String juesb_id) {
        JUESB_ID = juesb_id;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getRENYXXB_ID() {
        return RENYXXB_ID;
    }

    public void setRENYXXB_ID(String renyxxb_id) {
        RENYXXB_ID = renyxxb_id;
    }

    public Renyjsbean() {
    }

    public Renyjsbean(int xuh) {
        this.xuh = xuh;
    }

    public Renyjsbean(int id, String JUESB_ID, String RENYXXB_ID, int xuh,Date qissj,Date guoqsj,String shouqr_id) {
        this.xuh = xuh;
        this.m_id = id;
        this.RENYXXB_ID = RENYXXB_ID;
        this.JUESB_ID = JUESB_ID;
        this.guoqsj=guoqsj;
        this.qissj=qissj;
        this.shouqr_id=shouqr_id;
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

	public Date getQissj() {
		if(qissj==null){
			qissj=new Date();
		}
		return qissj;
	}

	public void setQissj(Date qissj) {
		this.qissj = qissj;
	}

	public String getShouqr_id() {
		return shouqr_id;
	}

	public void setShouqr_id(String shouqr_id) {
		this.shouqr_id = shouqr_id;
	}
}
