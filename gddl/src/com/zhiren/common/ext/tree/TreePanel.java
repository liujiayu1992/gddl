package com.zhiren.common.ext.tree;

import java.io.Serializable;

import com.zhiren.common.ext.Component;
import com.zhiren.common.ext.Toolbar;

public class TreePanel extends Component implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8096104414597537148L;

	private String id;

	private String treeRootNodeid;

	private boolean animate;

	private boolean line;

	private boolean enableDD;

	private boolean rootVisible;

	private boolean autoScroll;

	private String height;

	private String width;

	private String title;
	
	private String render;

	private Toolbar tbar;

	private Toolbar bbar;

	public TreePanel(String id) {
		this.id = id;
		this.animate = true;
		this.line = true;
		this.enableDD = false;
		this.rootVisible = true;
		this.autoScroll = true;
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}

	public boolean isAutoScroll() {
		return autoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		this.autoScroll = autoScroll;
	}

	public Toolbar getBbar() {
		return bbar;
	}

	public void setBbar(Toolbar bbar) {
		this.bbar = bbar;
	}

	public boolean isEnableDD() {
		return enableDD;
	}

	public void setEnableDD(boolean enableDD) {
		this.enableDD = enableDD;
	}

	public String getHeight() {
		return height;
	}
	
	public void setHeight(String height) {
		this.height = height;
	}

	public void setHeight(int height) {
		this.height = String.valueOf(height);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isLine() {
		return line;
	}

	public void setLine(boolean line) {
		this.line = line;
	}

	public boolean isRootVisible() {
		return rootVisible;
	}

	public void setRootVisible(boolean rootVisible) {
		this.rootVisible = rootVisible;
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

	public String getTreeRootNodeid() {
		return treeRootNodeid==null?"":treeRootNodeid;
	}
	
	public String getRender() {
		return render==null?"":render;
	}

	public void setRender(String render) {
		this.render = render;
	}

	public void setTreeRootNodeid(String treeRootNodeid) {
		this.treeRootNodeid = treeRootNodeid;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	public void setWidth(int width) {
		this.width = String.valueOf(width);
	}

	public void addTbar(Toolbar tbar) {
		this.tbar = tbar;
	}

	public void addBbar(Toolbar bbar) {
		this.bbar = bbar;
	}

	public String getTbarScript() {
		if (getTbar() == null) {
			return null;
		}
		return getTbar().getScript();
	}

	public String getBbarScript() {
		if (getBbar() == null) {
			return null;
		}
		return getBbar().getScript();
	}

	public String getScript() {
		StringBuffer sb = new StringBuffer();
		if(getId()!=null && !"".equals(getId())) {
			sb.append("var ").append(id).append("_treePanel =");
		}
		sb.append("new Ext.tree.TreePanel({\n");
		if("".equals(getRender())) {
			sb.append("\tel:'").append(id).append("',\n");
		}
		if(isAutoScroll()) {
			sb.append("\tautoScroll:").append(autoScroll).append(",\n");
		}
		if(isAnimate()) {
			sb.append("\tanimate:").append(isAnimate()).append(",\n");
		}
		if(!isLine()) {
			sb.append("\tline:").append(isLine()).append(",\n");
		}
		if(isEnableDD()) {
			sb.append("\tenableDD:").append(isEnableDD()).append(",\n");
		}
		if(!isRootVisible()) {
			sb.append("\trootVisible:").append(isRootVisible()).append(",");
		}
		if(getTitle()!=null && !"".equals(getTitle())) {
			sb.append("\ttitle:'").append(title).append("',");
		}
		if(getWidth()!=null) {
			sb.append("\twidth:").append(getWidth()).append(",\n");
		}
		if(getHeight()!=null) {
			sb.append("\theight:").append(getHeight()).append(",\n");
		}
		if(!"".equals(getTreeRootNodeid())) {
			sb.append("\troot:").append(id).append(getTreeRootNodeid()).append(",\n");
		}
		if(getTbarScript() != null) {
			sb.append("\ttbar:").append(getTbarScript()).append(",\n");
		}
		if(getBbarScript() != null) {
			sb.append("\tbbar:").append(getBbarScript()).append(",\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
		return sb.toString();
	}
	public String getRenderScript() {
		StringBuffer sb = new StringBuffer();
		sb.append(getScript()).append(";").append(id).append("_treePanel.render(").append(getRender()).append(");");
		return sb.toString();
	}

}
