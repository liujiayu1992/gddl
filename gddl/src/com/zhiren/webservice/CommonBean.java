package com.zhiren.webservice;

import java.io.Serializable;
import java.util.List;

public class CommonBean implements Serializable {

private String shujxy;//����Э��
private String caoz;//���ݲ���
private String zhuj;//��������
private String renwsj;//����ʱ��
private  String diancxxb_id;//���ݳ���
private List shujjl;//���ݼ�¼��˫list�ṹ
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