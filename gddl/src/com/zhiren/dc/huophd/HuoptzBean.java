package com.zhiren.dc.huophd;

/*
 * 时间：2007-01-11
 * 作者：Qiuzuwei
 * 描述：更改了标重的数据类型
 */

/*
 * 时间：2006-12-15
 * 作者：Qiuzuwei
 * 内容：记录相关的货票标重调整的数据
 */
import java.io.Serializable;

public class HuoptzBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8269101667812668481L;
	
	
	private long m_chepb_id = 0;
	
	
	private String m_cheph = "";
	
	
	private double m_biaoz = 0;
	
	
	private double m_yingd = 0.0;
	
	
	private double m_kuid = 0.0;
	
	
	private double m_yuns = 0.0;
	
	
	private double m_biaoz_old = 0;
	
	
	private double m_maoz = 0.0;
	
	
	private double m_piz = 0.0;
	
	
	private double m_yunsl = 0.0;
	
	
	private long m_fahb_id = 0;
	
	
	
	public double getM_biaoz() {
		return this.m_biaoz;
	}
	
	
	public void setM_biaoz(double m_biaoz) {
		this.m_biaoz = m_biaoz;
	}
	
	
	public double getM_biaoz_old() {
		return this.m_biaoz_old;
	}
	
	
	public void setM_biaoz_old(double m_biaoz_old) {
		this.m_biaoz_old = m_biaoz_old;
	}
	
	
	public long getM_chepb_id() {
		return this.m_chepb_id;
	}
	
	
	public void setM_chepb_id(long m_chepb_id) {
		this.m_chepb_id = m_chepb_id;
	}
	
	
	public String getM_cheph() {
		return this.m_cheph;
	}
	
	
	public void setM_cheph(String m_cheph) {
		this.m_cheph = m_cheph;
	}
	
	
	public double getM_kuid() {
		return this.m_kuid;
	}
	
	
	public void setM_kuid(double m_kuid) {
		this.m_kuid = m_kuid;
	}
	
	
	public double getM_maoz() {
		return this.m_maoz;
	}
	
	
	public void setM_maoz(double m_maoz) {
		this.m_maoz = m_maoz;
	}
	
	
	public double getM_piz() {
		return this.m_piz;
	}
	
	
	public void setM_piz(double m_piz) {
		this.m_piz = m_piz;
	}
	
	
	public double getM_yingd() {
		return this.m_yingd;
	}
	
	
	public void setM_yingd(double m_yingd) {
		this.m_yingd = m_yingd;
	}
	
	
	public double getM_yuns() {
		return this.m_yuns;
	}
	
	
	public void setM_yuns(double m_yuns) {
		this.m_yuns = m_yuns;
	}
	
	
	public double getM_yunsl() {
		return this.m_yunsl;
	}
	
	
	public void setM_yunsl(double m_yunsl) {
		this.m_yunsl = m_yunsl;
	}
	
	
	public long getM_fahb_id() {
		return m_fahb_id;
	}
	
	
	public void setM_fahb_id(long id) {
		m_fahb_id = id;
	}
	
	
	
	private String m_Cheb = "";
	
	
	
	public String getCheb() {
		return m_Cheb;
	}
	
	
	public void setCheb(String cheb) {
		m_Cheb = cheb;
	}
	
	
	
	
	//	记录选择的行
	private int m_SelectedIndex = -1;
	
	
	
	public int getSelectedIndex() {
		return m_SelectedIndex;
	}
	
	
	public void setSelectedIndex(int SelectedIndex) {
		m_SelectedIndex = SelectedIndex;
	}
	
	
	public HuoptzBean() {
		
	}
	
	
	public HuoptzBean(long id, String cheph, double maoz, double piz,
			double biaoz, double biaoz_old, double yingd, double kuid,
			double yuns, double yunsl, long fahb_id, int SelectedIndex,
			String cheb) {
		this.m_chepb_id = id;
		this.m_cheph = cheph;
		this.m_maoz = maoz;
		this.m_piz = piz;
		this.m_biaoz = biaoz;
		this.m_biaoz_old = biaoz_old;
		this.m_yingd = yingd;
		this.m_kuid = kuid;
		this.m_yuns = yuns;
		this.m_yunsl = yunsl;
		this.m_fahb_id = fahb_id;
		m_SelectedIndex = SelectedIndex;
		m_Cheb = cheb;
	}
	
	
	public String toString() {
		StringBuffer buffer = new StringBuffer("");
		
		buffer.append(m_chepb_id);
		buffer.append(',');
		buffer.append(m_cheph);
		buffer.append(',');
		buffer.append(m_biaoz);
		buffer.append(',');
		buffer.append(m_biaoz_old);
		buffer.append(',');
		buffer.append(m_yingd);
		buffer.append(',');
		buffer.append(m_kuid);
		buffer.append(',');
		buffer.append(m_yuns);
		return buffer.toString();
	}
	
}
