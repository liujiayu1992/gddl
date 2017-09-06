package com.zhiren.pub.jihkj;

import java.io.Serializable;

public class Jihkjbean implements Serializable {

    private static final long serialVersionUID = -4547823110709519212L;

    private long m_id;// ID

    private int m_xuh;// ÐòºÅ

    private String m_mingc;// Ãû³Æ

    private String m_bianm;// ¿Ú¾¶±àºÅ

    private String m_beiz;// ±¸×¢

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        this.m_id = id;
    }

    public int getXuh() {
        return m_xuh;
    }

    public void setXuh(int xuh) {
        this.m_xuh = xuh;
    }

    public String getMingc() {
        return m_mingc;
    }

    public void setMingc(String mingc) {
        this.m_mingc = mingc;
    }

    public String getBianm() {
        return m_bianm;
    }

    public void setBianm(String bianm) {
        this.m_bianm = bianm;
    }

    public String getBeiz() {
        return m_beiz;
    }

    public void setBeiz(String beiz) {
        this.m_beiz = beiz;
    }

    public Jihkjbean() {
    }

    public Jihkjbean(long id, int xuh, String mingc, String bianm, String beiz) {

        this.m_id = id;
        this.m_xuh = xuh;
        this.m_mingc = mingc;
        this.m_bianm = bianm;
        this.m_beiz = beiz;
    }

    public String toString() {

        StringBuffer buffer = new StringBuffer("");

        buffer.append(m_id);
        buffer.append(',');
        buffer.append(m_xuh);
        buffer.append(',');
        buffer.append(m_mingc);
        buffer.append(',');
        buffer.append(m_bianm);
        buffer.append(',');
        buffer.append(m_beiz);

        return buffer.toString();
    }

}
