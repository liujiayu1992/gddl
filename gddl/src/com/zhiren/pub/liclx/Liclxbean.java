package com.zhiren.pub.liclx;

import java.io.Serializable;

public class Liclxbean implements Serializable {

    private static final long serialVersionUID = -4547823110709519212L;

    private long m_id;// ID

    private int m_xuh;// ÐòºÅ

    private String m_mingc;// Ãû³Æ

    private String m_piny;// Æ´Òô

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

    public String getPiny() {
        return m_piny;
    }

    public void setPiny(String piny) {
        this.m_piny = piny;
    }

    public String getBeiz() {
        return m_beiz;
    }

    public void setBeiz(String beiz) {
        this.m_beiz = beiz;
    }

    public Liclxbean() {
    }

    public Liclxbean(long id, int xuh, String mingc, String piny, String beiz) {

        this.m_id = id;
        this.m_xuh = xuh;
        this.m_mingc = mingc;
        this.m_piny = piny;
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
        buffer.append(m_piny);
        buffer.append(',');
        buffer.append(m_beiz);

        return buffer.toString();
    }

}
