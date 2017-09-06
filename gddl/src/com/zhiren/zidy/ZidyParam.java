package com.zhiren.zidy;

import java.io.Serializable;

public class ZidyParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5718387472774014835L;
	
	private String Id;
	private String Name;
	private String Ctrl;
	private String CtrlWidth;
	private String Defv;
	private String Dslabel;
	private String Dsvalue;
	private String Dssql;
	private String Fvalue;
	private String Ptext;
	private String Svalue;
	private String Vtype;
	
	public String getCtrl() {
		return Ctrl;
	}
	public void setCtrl(String ctrl) {
		Ctrl = ctrl;
	}
	
	public String getCtrlWidth() {
		return CtrlWidth;
	}
	public void setCtrlWidth(String ctrlwidth) {
		CtrlWidth = ctrlwidth;
	}
	
	public String getDefv() {
		return Defv;
	}
	public void setDefv(String defv) {
		Defv = defv;
	}
	public String getDslabel() {
		return Dslabel;
	}
	public void setDslabel(String dslabel) {
		Dslabel = dslabel;
	}
	public String getDssql() {
		return Dssql;
	}
	public void setDssql(String dssql) {
		Dssql = dssql;
	}
	public String getDsvalue() {
		return Dsvalue;
	}
	public void setDsvalue(String dsvalue) {
		Dsvalue = dsvalue;
	}
	public String getFvalue() {
		return Fvalue;
	}
	public void setFvalue(String fvalue) {
		Fvalue = fvalue;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPtext() {
		return Ptext;
	}
	public void setPtext(String ptext) {
		Ptext = ptext;
	}
	public String getSvalue() {
		return Svalue;
	}
	public void setSvalue(String svalue) {
		Svalue = svalue;
	}
	public String getVtype() {
		return Vtype;
	}
	public void setVtype(String vtype) {
		Vtype = vtype;
	}
	
	public ZidyParam() {
		
	}
	
	public ZidyParam(String id, String name, String ctrl, String ctrlwidth,
			String defv, String dslabel, String dsvalue, String dssql,
			String fvalue, String ptext, String svalue, String vtype) {
		setId(id);
		setCtrl(ctrl);
		setCtrlWidth(ctrlwidth);
		setDefv(defv);
		setDslabel(dslabel);
		setDssql(dssql);
		setDsvalue(dsvalue);
		setFvalue(fvalue);
		setName(name);
		setPtext(ptext);
		setSvalue(svalue);
		setVtype(vtype);
	}
	
	
}
