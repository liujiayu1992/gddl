package com.zhiren.common.ext.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.ext.Component;

public class Menu extends Component implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4649349784324260850L;

	public String id;
	public String text;
	public List items;
	
	public Menu(){
		
	}
	public Menu(String text){
		this.text = text;
	}
	
	public String getId() {
		return id;
	}
	public String getText(){
		return text;
	}
	public void setText(String text){
		this.text = text;
	}
	public List getItems() {
		if(items==null) {
			items = new ArrayList();
		}
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}
	public void addTextItem(String text){
		TextItem ti = new TextItem(null,text,"onDefMenuItemClick");
		addItem(ti);
	}
	public void addItem(MenuItem mb) {
		getItems().add(mb);
	}
	
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
	
	public String getItemScript() {
		if(getItems().isEmpty()) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(int i =0 ; i < getItems().size() ; i++) {
			sb.append(((Component)getItems().get(i)).getScript()).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public String getScript() {
		// TODO 自动生成方法存根
		StringBuffer sb = new StringBuffer();
		if(id != null && !"".equals(id)) {
			sb.append("var ").append(id).append("_Menu = ");
		}
		sb.append("new Ext.menu.Menu({\n");
		if(id != null && !"".equals(id)) {
			sb.append("\tid:'").append(id).append("',\n");
		}
		if(getItemScript()!=null) {
			sb.append("\titems:[").append(getItemScript()).append("],\n");
		}
		sb.deleteCharAt(sb.length()-1);
		
		if(getItemScript()!=null){
//			为了防止item为空
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("})\n");
		return sb.toString();
	}
}
