package com.zhiren.dc.zhuangh.shulgl;

import java.io.Serializable;

public class jijbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int xuh;

	private long id;

	private String zhibb_id;

	private String tiaojb_id;

	private double shangx;

	private double xiax;

	private String danwb_id;

	private double jij;

	private String jijdwid;
	
	private String jijgs;	//基价公式

	private String hetjsfsb_id;

	private String hetjjfsb_id;

	private String hetjsxsb_id;

	private double yunj;

	private String yunjdw_id;

	private String yingdkf;

	private String yunsfsb_id;

	private double zuigmj;
	
	private String zuigmjdw;

	private double fengsjj;
	
	private String fengsjjdw;

	private String jijlx; // 基价类型（含税，不含税）
	private String pinz;
	public String getPinz() {
		return pinz;
	}
	public void setPinz(String pinz) {
		this.pinz = pinz;
	}
	public String getJijlx() {
		return jijlx;
	}

	public void setJijlx(String jijlx) {
		this.jijlx = jijlx;
	}

	public String getHetjjfsb_id() {
		return hetjjfsb_id;
	}

	public void setHetjjfsb_id(String hetjjfsb_id) {
		this.hetjjfsb_id = hetjjfsb_id;
	}

	public int getXuh() {
		return xuh;
	}

	public void setXuh(int xuh) {
		this.xuh = xuh;
	}

	public String getDanwb_id() {
		return danwb_id;
	}

	public void setDanwb_id(String danwb_id) {
		this.danwb_id = danwb_id;
	}

	public String getHetjsfsb_id() {
		if (hetjsfsb_id == null) {
			hetjsfsb_id = "____";
		}
		return hetjsfsb_id;
	}

	public void setHetjsfsb_id(String hetjsfsb_id) {
		this.hetjsfsb_id = hetjsfsb_id;
	}

	public String getHetjsxsb_id() {
		if (hetjsxsb_id == null) {
			hetjsxsb_id = "____";
		}
		return hetjsxsb_id;
	}

	public void setHetjsxsb_id(String hetjsxsb_id) {
		this.hetjsxsb_id = hetjsxsb_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getJij() {
		return jij;
	}

	public void setJij(double jij) {
		this.jij = jij;
	}

	public String getJijdwid() {
		return jijdwid;
	}

	public void setJijdwid(String jijdwid) {
		this.jijdwid = jijdwid;
	}
	
	public String getJijgs() {
		return jijgs;
	}

	public void setJijgs(String jijgs) {
		this.jijgs = jijgs;
	}

	public double getShangx() {
		return shangx;
	}

	public void setShangx(double shangx) {
		this.shangx = shangx;
	}

	public String getTiaojb_id() {
		return tiaojb_id;
	}

	public void setTiaojb_id(String tiaojb_id) {
		this.tiaojb_id = tiaojb_id;
	}

	public double getXiax() {
		return xiax;
	}

	public void setXiax(double xiax) {
		this.xiax = xiax;
	}

	public String getYingdkf() {
		return yingdkf;
	}

	public void setYingdkf(String yingdkf) {
		this.yingdkf = yingdkf;
	}

	public double getYunj() {
		return yunj;
	}

	public void setYunj(double yunj) {
		this.yunj = yunj;
	}

	public String getYunjdw_id() {

		return yunjdw_id;
	}

	public void setYunjdw_id(String yunjdw_id) {
		this.yunjdw_id = yunjdw_id;
	}

	public String getYunsfsb_id() {
		if (yunsfsb_id == null) {
			yunsfsb_id = "____";
		}
		return yunsfsb_id;
	}

	public void setYunsfsb_id(String yunsfsb_id) {
		this.yunsfsb_id = yunsfsb_id;
	}

	public String getZhibb_id() {
		return zhibb_id;
	}

	public void setZhibb_id(String zhibb_id) {
		this.zhibb_id = zhibb_id;
	}

	public double getZuigmj() {
		return zuigmj;
	}

	public void setZuigmj(double zuigmj) {
		this.zuigmj = zuigmj;
	}
	
	public String getZuigmjdw() {
		return zuigmjdw;
	}
	public void setZuigmjdw(String zuigmjdw) {
		this.zuigmjdw = zuigmjdw;
	}

	public jijbean(int xuh, String danw) {
		this.xuh = xuh;
		this.yunjdw_id = danw;
		this.jijdwid = danw;
	}

	public void setFengsjj(double fengsjj) {

		this.fengsjj = fengsjj;
	}

	public double getFengsjj() {

		return fengsjj;
	}
	
	public String getFengsjjdw() {
		return fengsjjdw;
	}
	public void setFengsjjdw(String fengsjjdw) {
		this.fengsjjdw = fengsjjdw;
	}

	public jijbean() {

	}

	public jijbean(int xuh, long id, String zhibb_id, String tiaojb_id,
			double shangx, double xiax, String danwb_id, double jij,
			String jijdwid, String jijgs,String hetjsfsb_id, String hetjjfsb_id,
			String hetjsxsb_id, double yunj, String yunjdw_id, String yingdkf,
			String yunsfsb_id, double zuigmj, String zuigmjdw, double fengsjj, String fengsjjdw, String jijlx,String pinz) {
		super();
		this.id = id;
		this.zhibb_id = zhibb_id;
		this.tiaojb_id = tiaojb_id;
		this.shangx = shangx;
		this.xiax = xiax;
		this.danwb_id = danwb_id;
		this.jij = jij;
		this.jijdwid = jijdwid;
		this.jijgs = jijgs;
		this.hetjsfsb_id = hetjsfsb_id;
		this.hetjsxsb_id = hetjsxsb_id;
		this.yunj = yunj;
		this.yunjdw_id = yunjdw_id;
		this.yingdkf = yingdkf;
		this.yunsfsb_id = yunsfsb_id;
		this.zuigmj = zuigmj;
		this.zuigmjdw = zuigmjdw;
		this.xuh = xuh;
		this.hetjjfsb_id = hetjjfsb_id;
		this.fengsjj = fengsjj;
		this.fengsjjdw = fengsjjdw;
		this.jijlx = jijlx;
		this.pinz=pinz;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((danwb_id == null) ? 0 : danwb_id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(fengsjj);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((hetjjfsb_id == null) ? 0 : hetjjfsb_id.hashCode());
		result = PRIME * result + ((hetjsfsb_id == null) ? 0 : hetjsfsb_id.hashCode());
		result = PRIME * result + ((hetjsxsb_id == null) ? 0 : hetjsxsb_id.hashCode());
		temp = Double.doubleToLongBits(jij);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((jijdwid == null) ? 0 : jijdwid.hashCode());
		result = PRIME * result + ((jijlx == null) ? 0 : jijlx.hashCode());
		result = PRIME * result + ((pinz == null) ? 0 : pinz.hashCode());
		temp = Double.doubleToLongBits(shangx);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((tiaojb_id == null) ? 0 : tiaojb_id.hashCode());
		temp = Double.doubleToLongBits(xiax);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + xuh;
		result = PRIME * result + ((yingdkf == null) ? 0 : yingdkf.hashCode());
		temp = Double.doubleToLongBits(yunj);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		result = PRIME * result + ((yunjdw_id == null) ? 0 : yunjdw_id.hashCode());
		result = PRIME * result + ((yunsfsb_id == null) ? 0 : yunsfsb_id.hashCode());
		result = PRIME * result + ((zhibb_id == null) ? 0 : zhibb_id.hashCode());
		temp = Double.doubleToLongBits(zuigmj);
		result = PRIME * result + ((zuigmjdw == null) ? 0 : zuigmjdw.hashCode());
		result = PRIME * result + ((fengsjjdw == null) ? 0 : fengsjjdw.hashCode());
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final jijbean other = (jijbean) obj;
		if (danwb_id == null) {
			if (other.danwb_id != null)
				return false;
		} else if (!danwb_id.equals(other.danwb_id))
			return false;
		if (Double.doubleToLongBits(fengsjj) != Double.doubleToLongBits(other.fengsjj))
			return false;
		if (hetjjfsb_id == null) {
			if (other.hetjjfsb_id != null)
				return false;
		} else if (!hetjjfsb_id.equals(other.hetjjfsb_id))
			return false;
		if (hetjsfsb_id == null) {
			if (other.hetjsfsb_id != null)
				return false;
		} else if (!hetjsfsb_id.equals(other.hetjsfsb_id))
			return false;
		if (hetjsxsb_id == null) {
			if (other.hetjsxsb_id != null)
				return false;
		} else if (!hetjsxsb_id.equals(other.hetjsxsb_id))
			return false;
		if (Double.doubleToLongBits(jij) != Double.doubleToLongBits(other.jij))
			return false;
		if (jijdwid == null) {
			if (other.jijdwid != null)
				return false;
		} else if (!jijdwid.equals(other.jijdwid))
			return false;
		if (jijlx == null) {
			if (other.jijlx != null)
				return false;
		} else if (!jijlx.equals(other.jijlx))
			return false;
		if (pinz == null) {
			if (other.pinz != null)
				return false;
		} else if (!pinz.equals(other.pinz))
			return false;
		if (Double.doubleToLongBits(shangx) != Double.doubleToLongBits(other.shangx))
			return false;
		if (tiaojb_id == null) {
			if (other.tiaojb_id != null)
				return false;
		} else if (!tiaojb_id.equals(other.tiaojb_id))
			return false;
		if (Double.doubleToLongBits(xiax) != Double.doubleToLongBits(other.xiax))
			return false;
		if (xuh != other.xuh)
			return false;
		if (yingdkf == null) {
			if (other.yingdkf != null)
				return false;
		} else if (!yingdkf.equals(other.yingdkf))
			return false;
		if (Double.doubleToLongBits(yunj) != Double.doubleToLongBits(other.yunj))
			return false;
		if (yunjdw_id == null) {
			if (other.yunjdw_id != null)
				return false;
		} else if (!yunjdw_id.equals(other.yunjdw_id))
			return false;
		if (zuigmjdw == null) {
			if (other.zuigmjdw != null)
				return false;
		} else if (!zuigmjdw.equals(other.zuigmjdw))
			return false;
		if (fengsjjdw == null) {
			if (other.fengsjjdw != null)
				return false;
		} else if (!fengsjjdw.equals(other.fengsjjdw))
			return false;
		if (yunsfsb_id == null) {
			if (other.yunsfsb_id != null)
				return false;
		} else if (!yunsfsb_id.equals(other.yunsfsb_id))
			return false;
		if (zhibb_id == null) {
			if (other.zhibb_id != null)
				return false;
		} else if (!zhibb_id.equals(other.zhibb_id))
			return false;
		if (Double.doubleToLongBits(zuigmj) != Double.doubleToLongBits(other.zuigmj))
			return false;
		return true;
	}

}