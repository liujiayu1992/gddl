package com.zhiren.common.ext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.menu.Menu;

public class Toolbar extends Component implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6652020760151558096L;

	public String id;
	
	public String width;
	public List items;
	
	public String getId() {
		return id;
	}

//	public void setId(String id) {
//		this.id = id;
//	}

	public List getItems() {
		if(items==null) {
			items = new ArrayList();
		}
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}
	
	public void addMenu(Menu m) {
		getItems().add(m);
	}
	
	public void addItem(ToolbarItem tb) {
		getItems().add(tb);
	}
	
	public void addField(Field f) {
		getItems().add(f);
	}
	
	public void addText(ToolbarText tt) {
		getItems().add(tt);
	}
	
	public void addFill() {
		getItems().add(new ToolbarText("->"));
	}
	
	public void deleteItem() {
		getItems().remove(getItems().size()-1);
	}
	
	public void deleteItem(int index) {
		getItems().remove(index);
	}
	
	public void setItem(int index, ToolbarItem tb) {
		getItems().add(index, tb);
	}
//	public void addButton() {
//		
//	}
	
	public Object getItem(String id) {
		if(getItems().isEmpty() || id ==null) {
			return null;
		}
		for(int i =0 ; i < getItems().size() ; i++) {
			if(id.equals(
					((Component)getItems().get(i)).getId())) {
				return getItems().get(i);
			}
		}
		return null;
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
	
	public Toolbar(String id) {
		this.id = id;
	}
	
	public String getItemScript() {
		if(getItems().isEmpty()) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(int i =0 ; i < getItems().size() ; i++) {
			Object item = getItems().get(i);
			if("com.zhiren.common.ext.menu.Menu".equalsIgnoreCase(item.getClass().getName())){
				sb.append("{text:'").append(((Menu)item).getText()).append("',menu:")
				.append(((Menu)item).getScript()).append("},");
			}else
				sb.append(((Component)getItems().get(i)).getScript()).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public String getRenderScript() {
		StringBuffer sb = new StringBuffer();
		sb.append(getScript());
		sb.append(id).append("_Toolbar.render();\n");
		return sb.toString();
	}
	
	public String getScript() {
		// TODO 自动生成方法存根
		StringBuffer sb = new StringBuffer();
		if(id != null && !"".equals(id)) {
			sb.append("var ").append(id).append("_Toolbar = ");
		}
		sb.append("new Ext.Toolbar({\n");
		if(id != null && !"".equals(id)) {
			sb.append("\tel:'").append(id).append("',\n");
		}
		if(getWidth()!=null) {
			sb.append("\twidth:").append(width).append(",\n");
		}
		if(getItemScript()!=null) {
			sb.append("\titems:[").append(getItemScript()).append("],\n");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.deleteCharAt(sb.length()-1);
		sb.append("})\n");
		return sb.toString();
	}
}
