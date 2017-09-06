package com.zhiren.main.gongs;

import java.io.Serializable;

public class GongsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4098530395074383461L;

	private boolean selected;
	private long id;
	private String mingc;
	private String leix;
	private int zhuangt;
	private String beiz;
	private String gongs;
	
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getBeiz() {
		return beiz;
	}
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	public String getGongs() {
		return gongs;
	}
	public void setGongs(String gongs) {
		this.gongs = gongs;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLeix() {
		return leix;
	}
	public void setLeix(String leix) {
		this.leix = leix;
	}
	public String getMingc() {
		return mingc;
	}
	public void setMingc(String mingc) {
		this.mingc = mingc;
	}
	public int getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(int zhuangt) {
		this.zhuangt = zhuangt;
	}
	
	
	
	/**
	 * @param selected
	 * @param id
	 * @param mingc
	 * @param leix
	 * @param zhuangt
	 * @param beiz
	 */
	public GongsBean(boolean selected, long id, String mingc, String leix, int zhuangt, String beiz) {
		super();
		this.selected = selected;
		this.id = id;
		this.mingc = mingc;
		this.leix = leix;
		this.zhuangt = zhuangt;
		this.beiz = beiz;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((beiz == null) ? 0 : beiz.hashCode());
		result = PRIME * result + ((gongs == null) ? 0 : gongs.hashCode());
		result = PRIME * result + (int) (id ^ (id >>> 32));
		result = PRIME * result + ((leix == null) ? 0 : leix.hashCode());
		result = PRIME * result + ((mingc == null) ? 0 : mingc.hashCode());
		result = PRIME * result + zhuangt;
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GongsBean other = (GongsBean) obj;
		if (beiz == null) {
			if (other.beiz != null)
				return false;
		} else if (!beiz.equals(other.beiz))
			return false;
		if (gongs == null) {
			if (other.gongs != null)
				return false;
		} else if (!gongs.equals(other.gongs))
			return false;
		if (id != other.id)
			return false;
		if (leix == null) {
			if (other.leix != null)
				return false;
		} else if (!leix.equals(other.leix))
			return false;
		if (mingc == null) {
			if (other.mingc != null)
				return false;
		} else if (!mingc.equals(other.mingc))
			return false;
		if (zhuangt != other.zhuangt)
			return false;
		return true;
	}
	
	
	
}
