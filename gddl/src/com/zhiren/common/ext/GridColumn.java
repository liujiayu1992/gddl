package com.zhiren.common.ext;

import java.io.Serializable;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.form.LovComboBox;

public class GridColumn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5738937586726884673L;
	public static final int ColType_default = 0;
	public static final int ColType_Rownum = 1;
	public static final int ColType_Check = 2;
	
	public static final String DataType_String= "string";
	public static final String DataType_Float= "float";
	public static final String DataType_Date= "date";
	public static final String DataType_DateTime= "datetime";
	
	
	public static final String Renderer_Date = "function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d'));}";
	public static final String Renderer_DateTime = "function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d H:i'));}";
	public static final String Renderer_usMoney = "'usMoney'";
//	public static final int Editype_text = 0;
//	public static final int Editype_number = 0;
//	public static final int Editype_combo = 1;
//	public static final int Editype_date = 2;
	
	private String id;
	public int coltype;
	public String header; 
	public String dataIndex;
	public boolean sortable;
	public boolean hidden;
	public String defaultvalue;
	public String datatype;
	public Field editor;
	public int width;
	public String renderer;
	private String align;
	private boolean unique;
//	public int editortype;
	public String parentId;
	public IDropDownModel combo;
	public boolean returnId;
	public boolean update;
	public boolean fixed;
	
	public GridColumn(int coltype){
		this.coltype = coltype;
		this.dataIndex = "";
	}
	
	public GridColumn(int coltype,String dataIndex,String header,int width){
		this.coltype = coltype;
		this.dataIndex = dataIndex;
		this.header=header;
		this.width=width;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void setColtype(int coltype) {
		this.coltype = coltype;
	}
	
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public void setCenterHeader(String header) {
		this.header = "<center>"+header+"</center>";
	}
	
	public void setCenterHeader(String header,int length) {
		int len = length==0?5:length;
		int begin = 0;
		int end = len;
		String newHeader = "";
		while(end < header.length()) {
			newHeader += header.substring(begin, end) + "<br>";
			begin = end;
			end += len;
		}
		if(end >= header.length()) {
			newHeader += header.substring(begin, header.length());
		}
		setCenterHeader(newHeader);
	}
	
	public void setCenterHeader(String header,int length, int rows) {
		int len = length==0?5:length;
		int begin = 0;
		int end = len;
		String newHeader = "";
		while((end < header.length()) && (rows-- >1)) {
			//rows--;
			newHeader += header.substring(begin, end) + "<br>";
			begin = end;
			end += len;
		}
		if(begin < header.length()) {
			newHeader += header.substring(begin, header.length());
		}
		setCenterHeader(newHeader);
	}
	
	public void setDataindex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	
	public void setDefaultValue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setDataType(String datatype) {
		this.datatype = datatype;
	}
	
	public void setReturnId(boolean bool) {
		this.returnId = bool;
	}
	
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public void setEditor(Field editor) {
		this.editor = editor;
	}
	
	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}
	public String getAlign() {
		return align==null?"":align;
	}

	public void setAlign(String align) {
		this.align = align;
	}
	
	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public void setComboEditor(String parentId,IDropDownModel iddm) {
		this.returnId = true;
		this.combo = iddm;
		this.parentId = parentId;
		if(this.editor != null) {
			((ComboBox)this.editor).setTransform("cbo_"+parentId+"_"+this.dataIndex);
			((ComboBox)this.editor).setLazyRender(true);
		}
	}
	
	public void setLovComboEditor(String parentId,IDropDownModel iddm) {
		this.returnId = true;
		this.combo = iddm;
		this.parentId = parentId;
		if(this.editor != null) {
			((LovComboBox)this.editor).setTransform("cbo_"+parentId+"_"+this.dataIndex);
			((LovComboBox)this.editor).setLazyRender(true);
		}
	}
	
	public String getComboHtml() {
		if(combo == null) {
			return "";
		}
		StringBuffer html = new StringBuffer();
		html.append("<select style='display:none' id = cbo_").append(parentId).append("_").append(dataIndex).append(">");
		for(int i = 0 ; i < combo.getOptionCount() ; i++) {
			html.append("<option value='").append(((IDropDownBean)combo.getOption(i)).getValue()).append("'>");
			html.append(((IDropDownBean)combo.getOption(i)).getValue()).append("</option>\n");
		}
		html.append("</select>");
		return html.toString();
	}
	
	public GridColumn(String header,String dataIndex){
		coltype = ColType_default;
		this.header = header;
		this.dataIndex = dataIndex;
		hidden = false;
		defaultvalue = "";
		datatype = DataType_String;
		update = true;
	}
	
	public GridColumn(String header,String dataIndex,boolean hidden){
		coltype = ColType_default;
		this.header = header;
		this.dataIndex = dataIndex;
		this.hidden = hidden;
		defaultvalue = "";
		datatype = DataType_String;
		update = true;
	}
	public String getEditorScript() {
		return editor==null?"":"editor :"+editor.getScript()+",";
	}
	public String getScript() {
		switch(coltype) {
			case ColType_default : 
				if(getId()==null)
					return getDefaultScript();
				else
					return getId();
			case ColType_Rownum : return "new Ext.grid.RowNumberer()";
			case ColType_Check : return "new Ext.grid.CheckboxSelectionModel()";
			default : return getDefaultScript();
		}
	}
	public String getDefaultScript() {
		StringBuffer sb = new StringBuffer();
		sb.append("{header:'").append(header).append("',");
		sb.append("dataIndex:'").append(dataIndex).append("',");
		if(sortable) {
			sb.append("sortable:").append(sortable).append(",");
		}
		if(fixed){
			sb.append("fixed:true").append(",");
		}
//		if(!"".equals(getAlign())) {
//			sb.append("align:'").append(getAlign()).append("',");
//		}
		sb.append(renderer==null?"":"renderer:"+renderer+",");
		sb.append(width==0?"":"width:"+width+",");
		sb.append(getEditorScript());
		
		sb.append("hidden :").append(hidden).append("}");
		return sb.toString();
	}

}
