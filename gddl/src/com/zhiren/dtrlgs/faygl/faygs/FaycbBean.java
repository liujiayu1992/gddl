package com.zhiren.dtrlgs.faygl.faygs;

public class  FaycbBean{
	private long diancId =-1;
	private long fhfy_id = -1;
	
	public void setDiancID(long value){
		this.diancId = value;
	}
	public long getDiancID(){
		return this.diancId;
	}
	
	public void setFhfyID(long value){
		this.fhfy_id = value;
	}
	public long getFhfyID(){
		return this.fhfy_id;
	}
	
	public FaycbBean(long dcid,long fhfyid){
		this.diancId = dcid;
		this.fhfy_id = fhfyid;
	}
}

