package com.zhiren.webservice;

import java.io.Serializable;
import java.util.List;

public class CommonBean implements Serializable {

private String shujxy;//数据协议
private String caoz;//数据操作
private String zhuj;//数据主键
private String renwsj;//数据时间
private  String diancxxb_id;//数据厂别
private List shujjl;//数据记录，双list结构
public String getCaoz() {
	return caoz;
}
public void setCaoz(String caoz) {
	this.caoz = caoz;
}
public String getDiancxxb_id() {
	return diancxxb_id;
}
public void setDiancxxb_id(String diancxxb_id) {
	this.diancxxb_id = diancxxb_id;
}
public String getRenwsj() {
	return renwsj;
}
public void setRenwsj(String renwsj) {
	this.renwsj = renwsj;
}
public List getShujjl() {
	return shujjl;
}
public void setShujjl(List shujjl) {
	this.shujjl = shujjl;
}
public String getShujxy() {
	return shujxy;
}
public void setShujxy(String shujxy) {
	this.shujxy = shujxy;
}
public String getZhuj() {
	return zhuj;
}
public void setZhuj(String zhuj) {
	this.zhuj = zhuj;
}

}