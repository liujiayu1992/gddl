package com.zhiren.common.ext.tree;

import java.io.Serializable;

import com.zhiren.common.ext.data.Node;

public class TreeNode extends Node implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6910370338459413948L;

	public boolean checkbox;
	public boolean checked;
	public boolean expanded;
//	public 
	public String href;
	public String hrefTarget;
	public String text;
	
	public TreeNode(String id,String text) {
		super(id);
		this.text = text;
	}
	
	public TreeNode(String id,String text,boolean checkbox) {
		super(id);
		this.text = text;
		this.checkbox = checkbox;
	}
	
	public TreeNode(String id,String text,boolean checkbox,boolean checked,String href,String hrefTarget) {
		super(id);
		this.text = text;
		this.checkbox = checkbox;
		this.checked = checked;
		this.href = href;
		this.hrefTarget = hrefTarget;
	}
	
	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHrefTarget() {
		return hrefTarget;
	}

	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}
	
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	public String getScript() {
		StringBuffer sb = new StringBuffer();
		sb.append("new Ext.tree.TreeNode({");
		if(!"".equals(getId())) {
			sb.append("\tid:'").append(getId()).append("',\n");
		}
		if(!"".equals(getText())) {
			sb.append("\ttext:'").append(getText()).append("',\n");
		}
		if(isCheckbox()) {
			sb.append("\tchecked:").append(isChecked()).append(",\n");
		}
		if(isExpanded()) {
			sb.append("\texpanded:").append(isExpanded()).append(",\n");
		}
		if(isLeaf()) {
			sb.append("\tleaf:").append(isLeaf()).append(",\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
		return sb.toString();
	}

}
