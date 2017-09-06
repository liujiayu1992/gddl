package com.zhiren.jt.zdt.monthreport.tianb.shul;

import java.io.Serializable;


public class Slbbean implements Serializable {
	private long m_oldtjid;
	private long m_newtjid;
	private String m_zlid;
	private String m_cbmid;

	public double getOldtjid(){
		return m_oldtjid;
	}
	public void setOldtjid(long oldtjid){
		this.m_oldtjid=oldtjid;
	}
	public double getNewtjid(){
		return m_newtjid;
	}
	public void setNewtjid(long newtjid){
		this.m_newtjid=	newtjid;
	}
	public String getZlid(){
		return m_zlid;
	}
	public void setZlid(String zlid){
		this.m_zlid=zlid;
	}
	public String getCbmid(){
		return m_cbmid;
	}
	public void setCbmid(String cbmid){
		this.m_cbmid=cbmid;
	}
	public Slbbean(long oldtjid,long newtjid, String zlid,String cbmid
			) {
		super();
		this.m_oldtjid = oldtjid;
		this.m_newtjid= newtjid;
		this.m_zlid=zlid;
		this.m_cbmid = cbmid;
		
	}
	
	public Slbbean(	) {
super();


}
	public String toString(){
		StringBuffer buffer = new StringBuffer("");
		buffer.append("m_oldtjid");		
		buffer.append(",");
		buffer.append("m_newtjid");
		buffer.append(",");
		buffer.append("m_zlid");
		buffer.append(",");
		buffer.append("m_cbmid");
		return buffer.toString();
	}

}
