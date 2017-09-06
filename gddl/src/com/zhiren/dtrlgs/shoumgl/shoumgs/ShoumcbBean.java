package com.zhiren.dtrlgs.shoumgl.shoumgs;

public class  ShoumcbBean{
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
	
	public ShoumcbBean(long dcid,long fhfyid){
		this.diancId = dcid;
		this.fhfy_id = fhfyid;
	}
}

