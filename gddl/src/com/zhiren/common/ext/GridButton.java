package com.zhiren.common.ext;

import java.util.List;

import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;

public class GridButton extends Button{

	/**
	 * 
	 */
	private static final long serialVersionUID = 728262068905488978L;
	public static final int ButtonType_Insert = 0;
	public static final int ButtonType_Delete = 1;
	public static final int ButtonType_Save = 2;
	public static final int ButtonType_Update = 3;
	public static final int ButtonType_Refresh = 4;
	public static final int ButtonType_Cancel = 5;
	public static final int ButtonType_Inserts = 6;
	public static final int ButtonType_SaveAll = 7;
	public static final int ButtonType_Copy = 8;
	public static final int ButtonType_SubmitSel = 9;
	public static final int ButtonType_Insert_condition = 10;
	public static final int ButtonType_Save_condition = 11;
	public static final int ButtonType_SubmitSel_condition = 12;
	public static final int ButtonType_Delete_confirm = 13;
	public static final int ButtonType_Insert_specified_line=14; 
	public static final int ButtonType_Inserts_condition = 15;
	public static final int ButtonType_Sel = 99;
	
	private int minWidth = 75;
	public String parentId ;
	public List columns;
	public String tapestryBtnId;
	public String condition;
	private boolean disabled;

	public void setMinWidth(int minwidth) {
		this.minWidth = minwidth;
	}
	public boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public GridButton(String text, String hander) {
		super(text, hander);
		// TODO 自动生成构造函数存根
	}
	
	public GridButton(String text, String hander,String icon) {
		super(text, hander,icon);
		// TODO 自动生成构造函数存根
	}
	
	public GridButton(int btnType,String parentId,List columns,String tapestryBtnId) {
//		super();
		this.parentId = parentId;
		this.columns = columns;
		this.tapestryBtnId = tapestryBtnId;
		setButton(btnType);
	}
	
	public GridButton(int btnType,String parentId,List columns,String tapestryBtnId,String condition) {
//		super();
		this.parentId = parentId;
		this.columns = columns;
		this.tapestryBtnId = tapestryBtnId;
		this.condition=condition;
		setButton(btnType);
	}
	
	public GridButton(String text,int btnType,String parentId,List columns,String tapestryBtnId) {
		this.text = text;
		this.parentId = parentId;
		this.columns = columns;
		this.tapestryBtnId = tapestryBtnId;
		setButton(btnType);
	}
	
	public GridButton(String text,int btnType,String parentId,List columns,String tapestryBtnId,String condition) {
		this.text = text;
		this.parentId = parentId;
		this.columns = columns;
		this.tapestryBtnId = tapestryBtnId;
		this.condition=condition;
		setButton(btnType);
	}
	
	public void setTapestryBtnId(String tapestryBtnId) {
		this.tapestryBtnId = tapestryBtnId;
	}
	
	public String getRecordScript(int type) {
		
		StringBuffer record = new StringBuffer();
		record.append("var plant = new ").append(parentId).append("_plant({");
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(type == 0) {
					record.append(gc.dataIndex).append(": '").append(gc.defaultvalue).append("',");
				}else 
				if(type ==1 ){
					record.append(gc.dataIndex).append(": rec.get('").append(gc.dataIndex).append("'),");
				}
			}
		}
		record.deleteCharAt(record.length()-1);
		record.append("});\n");
		return record.toString();
	}
	
	public void setButton(int btnType) {
		switch(btnType) {
			case ButtonType_Insert: 
				if(text == null || "".equals(text))text = " 添加";
				icon = SysConstant.Btn_Icon_Insert;
				handler = "function() {\n" +
					getRecordScript(0)	+
					parentId+"_ds.insert("+parentId+"_ds.getCount(),plant);}\n";
				break;
			case ButtonType_Inserts: 
				icon = SysConstant.Btn_Icon_Insert;
				if(text == null || "".equals(text))text = " 添加";
				handler = "function() {\n" +
					"Ext.MessageBox.prompt('提示', '请输入添加记录数', function(btn,text){" +
					"if(btn=='ok'){if(text>0){for(i=0;i<text;i++){" +
					getRecordScript(0) +parentId+"_ds.insert("
					+parentId+"_ds.getCount(),plant);}}}});}\n";
				break;
			case ButtonType_Insert_condition: 
				if(text == null || "".equals(text))text = " 添加";
				icon = SysConstant.Btn_Icon_Insert;
				handler = "function() {\n" +
					this.condition	+
					getRecordScript(0)	+
					parentId+"_ds.insert("+parentId+"_ds.getCount(),plant);}\n";
				break;
			case ButtonType_Inserts_condition: 
				icon = SysConstant.Btn_Icon_Insert;
				if(text == null || "".equals(text))text = " 添加";
				handler = "function() {\n" +
					this.condition + 
					"Ext.MessageBox.prompt('提示', '请输入添加记录数', function(btn,text){" +
					"if(btn=='ok'){if(text>0){for(i=0;i<text;i++){" +
					getRecordScript(0) +parentId+"_ds.insert("
					+parentId+"_ds.getCount(),plant);}}}});}\n";
				break;
			case ButtonType_Insert_specified_line: //将数据插入到选中行的后面
				if(text == null || "".equals(text))text = " 添加";
				icon = SysConstant.Btn_Icon_Insert;
				handler = "function() {\n" +
					getRecordScript(0)	+
					"var row="+parentId+"_grid.getStore().indexOf("+parentId+"_grid.getSelectionModel().getSelected());\n"+
					" if (row==-1){\n"+ //如果没有选中某行，在最后一行加
					parentId+"_ds.insert("+parentId+"_ds.getCount(),plant);\n"+
					" }else{\n" + //在选中的行后面加
					parentId+"_ds.insert(row+1,plant);\n"+
					" }}";
				break;
			case ButtonType_Delete: 
				if(text == null || "".equals(text))text = " 删除";
				icon = SysConstant.Btn_Icon_Delete;
				handler = getDeleteScript();
					/*"function() {\n" +
					"for(i=0;"+parentId+"_sm.getSelections().length;i++){\n"+
					"	record = "+parentId+"_sm.getSelections()[i];\n" +
					"	if(record.get('ID') != '0'){\n"+
					"		"+parentId+"_history += 'D,'+record.get('ID') + ';';\n"+
					"	}\n" + 
					"	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);" + 
					"}}\n";*/
				break;
			case ButtonType_Delete_confirm: 
				if(text == null || "".equals(text))text = " 删除";
				icon = SysConstant.Btn_Icon_Delete;
				handler = getDeleteScript(true);
					/*"function() {\n" +
					"for(i=0;"+parentId+"_sm.getSelections().length;i++){\n"+
					"	record = "+parentId+"_sm.getSelections()[i];\n" +
					"	if(record.get('ID') != '0'){\n"+
					"		"+parentId+"_history += 'D,'+record.get('ID') + ';';\n"+
					"	}\n" + 
					"	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);" + 
					"}}\n";*/
				break;
			case ButtonType_Save: 
				if(text == null || "".equals(text))text = " 保存";
				icon = SysConstant.Btn_Icon_Save;
				handler = getSaveScript(SysConstant.SaveMode_Upsubmit);
				break;
			case ButtonType_Update: 
				if(text == null || "".equals(text))text = " 修改";
				handler = "";
				break;
			case ButtonType_Refresh: 
				if(text == null || "".equals(text))text = " 刷新";
				icon = SysConstant.Btn_Icon_Refurbish;
				if(tapestryBtnId!=null && !"".equals(tapestryBtnId)){
					handler = "function(){document.getElementById('"+tapestryBtnId+"').click();}";
				}else{
					handler = "";
				}
				break;	
			case ButtonType_Cancel:
				if(text == null || "".equals(text))text = " 取消";
				icon = SysConstant.Btn_Icon_Cancel;
				handler = "function(){"+parentId+"_ds.rejectChanges();}";
				break;
			case ButtonType_SaveAll:
				if(text == null || "".equals(text))text = " 保存";
				icon = SysConstant.Btn_Icon_Save;
				handler = getSaveScript(SysConstant.SaveMode_Allsubmit);
				break;
			case ButtonType_Save_condition: 
				if(text == null || "".equals(text))text = " 保存";
				icon = SysConstant.Btn_Icon_Save;
				handler = getSaveScript(SysConstant.SaveMode_Upsubmit,this.condition);
				break;	
			case ButtonType_Copy:
				if(text == null || "".equals(text))text = " 复制";
				icon = SysConstant.Btn_Icon_Copy;
				handler = getCopyScript();
				break;
			case ButtonType_SubmitSel:
				icon = SysConstant.Btn_Icon_SelSubmit;
				if(text == null || "".equals(text))text = " 提交";
				handler = getSaveScript(SysConstant.SaveMode_Selsubmit);
				break;
			case ButtonType_SubmitSel_condition:
				icon = SysConstant.Btn_Icon_SelSubmit;
				if(text == null || "".equals(text))text = " 提交";
				handler = getSaveScript(SysConstant.SaveMode_Selsubmit,this.condition);
				break;		
			case ButtonType_Sel:
				icon = SysConstant.Btn_Icon_SelSubmit;
				handler = getSaveScript(SysConstant.SaveMode_Selsubmit);
				break;
			default: 
				text = " 按钮" ;
				handler = "";
			break;
		}
	}
	public String getCopyScript() {
		StringBuffer record = new StringBuffer();
		record.append("function() {\n");
		record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
		record.append("	rec = "+parentId+"_sm.getSelections()[i];\n");
		record.append(getRecordScript(1));
		record.append(parentId+"_ds.insert("+parentId+"_ds.getCount(),plant);\n");
		record.append("}}");
		return record.toString();
	}
	
	public String getDeleteScript(boolean isConfirm){
		StringBuffer record = new StringBuffer();
		record.append("function() {\n");
		if(isConfirm){
			record.append("Ext.MessageBox.confirm('提示信息','删除操作将不可撤销,是否确认删除?',function(btn){if(btn=='yes'){");
		}
		record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
		record.append("	record = "+parentId+"_sm.getSelections()[i];\n");
		
		StringBuffer sb = new StringBuffer();
		//sb.append(b);
		sb.append(parentId).append("_history += '<result>' ")
		.append("+ '<sign>D</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
						.append(gc.dataIndex).append("'))?").append("record.get('")
						.append(gc.dataIndex).append("'):").append("record.get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else if("datetime".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'))?").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'):").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d H:i'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		sb.append(" + '</result>' ;");
		record.append(sb);
		
		record.append("	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);}" );
		if(isConfirm){
			record.append("var Cobj = document.getElementById('CHANGE');");
			record.append("Cobj.value = ").append("'<result>'+").append(parentId).append("_history").append("+").append(parentId).append("save_history+'</result>';");
			record.append("document.getElementById('").append(this.tapestryBtnId).append("').click();");
			record.append("}else{return;}})");
		}
		record.append("}");
		return record.toString();
	}
	public String getDeleteScript() {
		StringBuffer record = new StringBuffer();
		record.append("function() {\n");
		record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
		record.append("	record = "+parentId+"_sm.getSelections()[i];\n");
		
		StringBuffer sb = new StringBuffer();
		//sb.append(b);
		sb.append(parentId).append("_history += '<result>' ")
		.append("+ '<sign>D</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
						.append(gc.dataIndex).append("'))?").append("record.get('")
						.append(gc.dataIndex).append("'):").append("record.get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else if ("datetime".equals(gc.datatype)){ 
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
						.append(gc.dataIndex).append("'))?").append("record.get('")
						.append(gc.dataIndex).append("'):").append("record.get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d H:i'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		sb.append(" + '</result>' ;");
		record.append(sb);
		
		record.append("	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);}}");
		return record.toString();
	}
	
	public String getSaveScript(int saveMode) {
		StringBuffer record = new StringBuffer();
		record.append("function(){\n var "+parentId+"save_history = '';");
		if(saveMode == SysConstant.SaveMode_Allsubmit )
			record.append("var Mrcd = ").append(parentId).append("_ds.getRange();\n");
		else 
			if(saveMode == SysConstant.SaveMode_Upsubmit )
				record.append("var Mrcd = ").append(parentId).append("_ds.getModifiedRecords();\n");
			else
				if(saveMode == SysConstant.SaveMode_Selsubmit)
					record.append("var Mrcd = ").append(parentId).append("_grid.getSelectionModel().getSelections();\n");
		record.append("for(i = 0; i< Mrcd.length; i++){\n");
		record.append("if(typeof(").append(parentId).append("_save)=='function'){ var revalue = ").append(parentId).append("_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n");
		StringBuffer sb = new StringBuffer();
		sb.append(parentId).append("save_history += '<result>' ")
		.append("+ '<sign>U</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.editor != null && !gc.editor.allowBlank) {
					if(GridColumn.DataType_Float.equals(gc.datatype)) {
						String min = gc.editor.getMinValue();
						min = "".equals(min)?"-100000000":min;
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("') < ").append(min).append("){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段").append(gc.header).append(" 低于最小值 "+min+"');return;\n}");
						String max = gc.editor.getMaxValue();
						max = "".equals(max)?"100000000000":max;
						record.append("if(").append(" Mrcd[i].get('").append(gc.dataIndex).append("') >").append(max).append("){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 ").append(gc.header).append(" 高于最大值 "+max+"');return;\n}");
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("')!=0 && ").append("Mrcd[i].get('").append(gc.dataIndex).append("') == ''){Ext.MessageBox.alert('提示信息','字段 ").append(gc.header).append(" 不能为空');return;\n}");
					}else {
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("') == ''){Ext.MessageBox.alert('提示信息','字段 ").append(gc.header).append(" 不能为空');return;\n}");
					}
				}
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'))?").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'):").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					} else if ("datetime".equals(gc.datatype)){
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'))?").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'):").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d H:i'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					} else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("Mrcd[i].get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		record.append(sb);
		record.append(" + '</result>' ; }\n"); 
		record.append("if(").append(parentId).append("_history=='' && ").append(parentId).append("save_history==''){ \n");
		if(saveMode == SysConstant.SaveMode_Selsubmit){
			
			record.append("Ext.MessageBox.alert('提示信息','没有选择数据信息');\n");
		}else{
			
			record.append("Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n");
		}
		record.append("}else{\n").append("var Cobj = document.getElementById('CHANGE');");
		record.append("Cobj.value = ").append("'<result>'+").append(parentId).append("_history").append("+").append(parentId).append("save_history+'</result>';");
		record.append("document.getElementById('").append(this.tapestryBtnId).append("').click();")
//		增加保存的提示
//		.append("Ext.MessageBox.alert('提示信息','正在处理数据...');")
		.append(MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200))
		.append("\n}\n}");
		return record.toString();
	}
	
	public String getSaveScript(int saveMode,String condition) {
		StringBuffer record = new StringBuffer();
		record.append("function(){\n var "+parentId+"save_history = '';");
		record.append(this.condition);
		if(saveMode == SysConstant.SaveMode_Allsubmit )
			record.append("var Mrcd = ").append(parentId).append("_ds.getRange();\n");
		else 
			if(saveMode == SysConstant.SaveMode_Upsubmit )
				record.append("var Mrcd = ").append(parentId).append("_ds.getModifiedRecords();\n");
			else
				if(saveMode == SysConstant.SaveMode_Selsubmit)
					record.append("var Mrcd = ").append(parentId).append("_grid.getSelectionModel().getSelections();\n");
		record.append("for(i = 0; i< Mrcd.length; i++){\n");
		record.append("if(typeof(").append(parentId).append("_save)=='function'){ var revalue = ").append(parentId).append("_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n");
		StringBuffer sb = new StringBuffer();
		sb.append(parentId).append("save_history += '<result>' ")
		.append("+ '<sign>U</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.editor != null && !gc.editor.allowBlank) {
					if(GridColumn.DataType_Float.equals(gc.datatype)) {
						String min = gc.editor.getMinValue();
						min = "".equals(min)?"-100000000":min;
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("') < ").append(min).append("){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段").append(gc.header).append(" 低于最小值 "+min+"');return;\n}");
						String max = gc.editor.getMaxValue();
						max = "".equals(max)?"100000000000":max;
						record.append("if(").append(" Mrcd[i].get('").append(gc.dataIndex).append("') >").append(max).append("){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 ").append(gc.header).append(" 高于最大值 "+max+"');return;\n}");
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("')!=0 && ").append("Mrcd[i].get('").append(gc.dataIndex).append("') == ''){Ext.MessageBox.alert('提示信息','字段 ").append(gc.header).append(" 不能为空');return;\n}");
					}else {
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("') == ''){Ext.MessageBox.alert('提示信息','字段 ").append(gc.header).append(" 不能为空');return;\n}");
					}
				}
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'))?").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'):").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else if("datetime".equals(gc.datatype)){
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'))?").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'):").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d H:i'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					} else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("Mrcd[i].get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		record.append(sb);
		record.append(" + '</result>' ; }\n"); 
		record.append("if(").append(parentId).append("_history=='' && ").append(parentId).append("save_history==''){ \n");
		if(saveMode == SysConstant.SaveMode_Selsubmit){
			
			record.append("Ext.MessageBox.alert('提示信息','没有选择数据信息');\n");
		}else{
			
			record.append("Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n");
		}
		record.append("}else{\n").append("var Cobj = document.getElementById('CHANGE');");
		record.append("Cobj.value = ").append("'<result>'+").append(parentId).append("_history").append("+").append(parentId).append("save_history+'</result>';");
		record.append("document.getElementById('").append(this.tapestryBtnId).append("').click();")
//		增加保存的提示
		.append(MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200))
		.append("\n}\n}");
		return record.toString();
	}
	
	public String getScript() {
		StringBuffer buttonjs = new StringBuffer();
		buttonjs.append("text:'").append(text).append("',");
		if(id !=null) {
			buttonjs.append("id:'").append(id).append("',");
		}
		if(icon != null) {
			buttonjs.append("icon:'").append(icon).append("',");
			buttonjs.append("cls:'x-btn-text-icon',");
		}
		if(getDisabled()) {
			buttonjs.append("disabled:").append(getDisabled()).append(",");
		}
		buttonjs.append("minWidth:").append(minWidth).append(",");
		if(handler == null || "".equals(handler)) {
			buttonjs.deleteCharAt(buttonjs.length()-1);
		}else {
			buttonjs.append("handler:").append(handler);
		}
		return buttonjs.toString();
	}
}