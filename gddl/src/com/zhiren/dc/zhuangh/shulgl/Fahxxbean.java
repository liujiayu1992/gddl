package com.zhiren.dc.zhuangh.shulgl;

import java.io.Serializable;

/**
 * @author cao
 *11.26日完成了合同模板的详细设计，并完成了合同信息，数量信息的基本工作。
 *
 *
 *
 */
public class Fahxxbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  long id;
	private String pinz;
	private String yunsfs;
	private String faz;
	private String daoz;
	private String shouhr;
	private long hej;
	private long Y1;
	private long Y2;
	private long Y3;
	private long Y4;
	private long Y5;
	private long Y6;
	private long Y7;
	private long Y8;
	private long Y9;
	private long Y10;
	private long Y11;
	private long Y12;
	public String getDaoz() {
		return daoz;
	}
	public void setDaoz(String daoz) {
		this.daoz = daoz;
	}
	public String getFaz() {
		return faz;
	}
	public void setFaz(String fahz) {
		this.faz = fahz;
	}
	public long getHej() {
		return hej;
	}
	public void setHej(long hej) {
		this.hej = hej;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPinz() {
		return pinz;
	}
	public void setPinz(String pinz) {
		this.pinz = pinz;
	}
	public String getShouhr() {
		return shouhr;
	}
	public void setShouhr(String shouhr) {
		this.shouhr = shouhr;
	}
	public long getY1() {
		return Y1;
	}
	public void setY1(long y1) {
		Y1 = y1;
	}
	public long getY10() {
		return Y10;
	}
	public void setY10(long y10) {
		Y10 = y10;
	}
	public long getY11() {
		return Y11;
	}
	public void setY11(long y11) {
		Y11 = y11;
	}
	public long getY12() {
		return Y12;
	}
	public void setY12(long y12) {
		Y12 = y12;
	}
	public long getY2() {
		return Y2;
	}
	public void setY2(long y2) {
		Y2 = y2;
	}
	public long getY3() {
		return Y3;
	}
	public void setY3(long y3) {
		Y3 = y3;
	}
	public long getY4() {
		return Y4;
	}
	public void setY4(long y4) {
		Y4 = y4;
	}
	public long getY5() {
		return Y5;
	}
	public void setY5(long y5) {
		Y5 = y5;
	}
	public long getY6() {
		return Y6;
	}
	public void setY6(long y6) {
		Y6 = y6;
	}
	public long getY7() {
		return Y7;
	}
	public void setY7(long y7) {
		Y7 = y7;
	}
	public long getY8() {
		return Y8;
	}
	public void setY8(long y8) {
		Y8 = y8;
	}
	public long getY9() {
		return Y9;
	}
	public void setY9(long y9) {
		Y9 = y9;
	}
	public String getYunsfs() {
		return yunsfs;
	}
	public void setYunsfs(String yunsfs) {
		this.yunsfs = yunsfs;
	}
	public Fahxxbean() {
	}
	public Fahxxbean(String daoz,String shouhr) {
		super();
		this.daoz=daoz;
		this.shouhr=shouhr;
	}
	public Fahxxbean(long id, String pinz, String yunsfs, String faz, String daoz, String shouhr, long hej, long y1, long y2, long y3, long y4, long y5, long y6, long y7, long y8, long y9, long y10, long y11, long y12) {
		super();
		this.id = id;
		this.pinz = pinz;
		this.yunsfs = yunsfs;
		this.faz = faz;
		this.daoz = daoz;
		this.shouhr = shouhr;
		this.hej = hej;
		Y1 = y1;
		Y2 = y2;
		Y3 = y3;
		Y4 = y4;
		Y5 = y5;
		Y6 = y6;
		Y7 = y7;
		Y8 = y8;
		Y9 = y9;
		Y10 = y10;
		Y11 = y11;
		Y12 = y12;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (int) (Y1 ^ (Y1 >>> 32));
		result = PRIME * result + (int) (Y10 ^ (Y10 >>> 32));
		result = PRIME * result + (int) (Y11 ^ (Y11 >>> 32));
		result = PRIME * result + (int) (Y12 ^ (Y12 >>> 32));
		result = PRIME * result + (int) (Y2 ^ (Y2 >>> 32));
		result = PRIME * result + (int) (Y3 ^ (Y3 >>> 32));
		result = PRIME * result + (int) (Y4 ^ (Y4 >>> 32));
		result = PRIME * result + (int) (Y5 ^ (Y5 >>> 32));
		result = PRIME * result + (int) (Y6 ^ (Y6 >>> 32));
		result = PRIME * result + (int) (Y7 ^ (Y7 >>> 32));
		result = PRIME * result + (int) (Y8 ^ (Y8 >>> 32));
		result = PRIME * result + (int) (Y9 ^ (Y9 >>> 32));
		result = PRIME * result + ((daoz == null) ? 0 : daoz.hashCode());
		result = PRIME * result + ((faz == null) ? 0 : faz.hashCode());
		result = PRIME * result + (int) (hej ^ (hej >>> 32));
		result = PRIME * result + (int) (id ^ (id >>> 32));
		result = PRIME * result + ((pinz == null) ? 0 : pinz.hashCode());
		result = PRIME * result + ((shouhr == null) ? 0 : shouhr.hashCode());
		result = PRIME * result + ((yunsfs == null) ? 0 : yunsfs.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Fahxxbean other = (Fahxxbean) obj;
		if (Y1 != other.Y1)
			return false;
		if (Y10 != other.Y10)
			return false;
		if (Y11 != other.Y11)
			return false;
		if (Y12 != other.Y12)
			return false;
		if (Y2 != other.Y2)
			return false;
		if (Y3 != other.Y3)
			return false;
		if (Y4 != other.Y4)
			return false;
		if (Y5 != other.Y5)
			return false;
		if (Y6 != other.Y6)
			return false;
		if (Y7 != other.Y7)
			return false;
		if (Y8 != other.Y8)
			return false;
		if (Y9 != other.Y9)
			return false;
		if (daoz == null) {
			if (other.daoz != null)
				return false;
		} else if (!daoz.equals(other.daoz))
			return false;
		if (faz == null) {
			if (other.faz != null)
				return false;
		} else if (!faz.equals(other.faz))
			return false;
		if (hej != other.hej)
			return false;
		if (id != other.id)
			return false;
		if (pinz == null) {
			if (other.pinz != null)
				return false;
		} else if (!pinz.equals(other.pinz))
			return false;
		if (shouhr == null) {
			if (other.shouhr != null)
				return false;
		} else if (!shouhr.equals(other.shouhr))
			return false;
		if (yunsfs == null) {
			if (other.yunsfs != null)
				return false;
		} else if (!yunsfs.equals(other.yunsfs))
			return false;
		return true;
	}
}
