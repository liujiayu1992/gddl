package com.zhiren.common.ext.grid;

import java.io.Serializable;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.data.Store;


/**
 * @author ÍõÀÚ
 *
 */
public class EditorGridPanel implements Serializable {

	private static final long serialVersionUID = 7020946077527159829L;
	private Toolbar bbar;
	private int clicksToEdit;
	private ColumnModel cm;
	private String el;
	private boolean frame;
	private String height;
	private String id;
	private RowSelectionModel sm;
	private Store store;
	private Toolbar tbar;
	private String title;
	private String width;
	
	public EditorGridPanel() {
	}
	
	public Toolbar getBbar() {
		return bbar;
	}
	public void setBbar(Toolbar bbar) {
		this.bbar = bbar;
	}
	
	public int getCte() {
		return clicksToEdit;
	}
	public void setCte(int clicksToEdit) {
		this.clicksToEdit = clicksToEdit;
	}
	
	public ColumnModel getCM() {
		return cm;
	}
	public void setCM(ColumnModel cm) {
		this.cm = cm;
	}
	
	public String getEL() {
		return el;
	}
	public void setEL(String el) {
		this.el = el;
	}
	
	public boolean isFrame() {
		return frame;
	}
	public void setFrame(boolean frame) {
		this.frame = frame;
	}
	
	public String getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = String.valueOf(height);
	}
	public void setHeight(String height) {
		this.height = height;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public RowSelectionModel getSM() {
		return sm;
	}
	public void setSM(RowSelectionModel sm) {
		this.sm = sm;
	}
	
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	public Toolbar getTbar() {
		return tbar;
	}
	public void setTbar(Toolbar tbar) {
		this.tbar = tbar;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = String.valueOf(width);
	}
	public void setWidth(String width) {
		this.width = width;
	}
	
	public String getScript() {
		StringBuffer sb = new StringBuffer();
		if(getId() != null) {
			sb.append("var ").append(getId()).append(" =");
		}
		sb.append("new Ext.grid.EditorGridPanel({\n");
		if(getBbar() != null) {
			sb.append("bbar: ").append(getBbar().getId()).append(",\n");
		}
		if(getCte() > 0) {
			sb.append("clicksToEdit: ").append(getCte()).append(",\n");
		}
		if(getCM() != null) {
			sb.append("cm: ").append(getCM()).append(",\n");
		}
		if(getEL() != null) {
			sb.append("el: '").append(getEL()).append("',\n");
		}
		if(isFrame()) {
			sb.append("frame: true,\n");
		}
		if(getHeight() != null) {
			sb.append("height: ").append(getHeight()).append(",\n");
		}
		if(getSM() != null) {
			sb.append("sm: ").append(getSM().getId()).append(",\n");
		}
		if(getStore() != null) {
			sb.append("ds: ").append(getStore().getId()).append(",\n");
		}
		if(getTbar() != null) {
			sb.append("tbar: ").append(getTbar().getId()).append(",\n");
		}
		if(getTitle() != null) {
			sb.append("title: '").append(getTitle()).append("',\n");
		}
		if(getWidth() != null) {
			sb.append("width: ").append(getWidth()).append(",\n");
		}
		sb.deleteCharAt(sb.length() -2);
		sb.append("});\n");
		return sb.toString();
	}
	
}