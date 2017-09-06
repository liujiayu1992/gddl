package com.zhiren.common.ext.tree;

import com.zhiren.common.ext.Button;

public class TreeButton extends Button{

	/**
	 * 
	 */
	private static final long serialVersionUID = 728262068905488978L;
	public static final int ButtonType_Ok = 0;
	public static final int ButtonType_Window_Checked_Ok = 10;
	public static final int ButtonType_Window_Ok = 11;
	public static final int ButtonType_Cancel = 1;
	
	public String form;
	public String parentId ;
	public String tapestryBtnId;
	
//	getChecked

	public TreeButton(String text, String hander) {
		super(text, hander);
		// TODO 自动生成构造函数存根
	}
	
	public TreeButton(int btnType,String parentId,String tapestryBtnId) {
		super();
		this.parentId = parentId;
		this.tapestryBtnId = tapestryBtnId;
		this.form = "forms[0]";
		setButton(btnType);
	}
	
	public void setTapestryBtnId(String tapestryBtnId) {
		this.tapestryBtnId = tapestryBtnId;
	}
	
	public void setForm(String form) {
		this.form = form;
	}
	
	public void setButton(int btnType) {
		switch(btnType) {
			case ButtonType_Ok: 
				text = "确定";
				handler = "function() {\n" +
					"var cks = "+parentId+"_treePanel.getChecked();\n"+
					"var cksid = new Array();\n" +
					"for(i=0;i<cks.length;i++){\n"+
						"cksid[i] = cks[i].id;\n" +
					"}"+
					parentId+"_history = cksid.join(',');\n"+
					"var obj = document.getElementById('"+parentId+"_CHANGE');\n"+
					"obj.value = "+parentId+"_history;\n"+
					(tapestryBtnId==null?"":"document.getElementById('"+tapestryBtnId+"').click();\n")+
					(form==null||"".equals(form)?"":"document."+form+".submit();") +
				"}\n";
				break;
			case ButtonType_Cancel: 
				text = "取消";
				handler = "function() {}\n";
				break;
			case ButtonType_Window_Checked_Ok:
				text = "确定";
				handler = "function() {var cks = "+parentId+"_treePanel.getChecked();" +
						"var ckstxt = new Array();" +
						"var cksids = new Array();" +
						"for(i=0;i<cks.length;i++){" +
						"cksids[i] = cks[i].id;" +
						//"ckstxt[i] = cks[i].text;" +
						"}" +
						"var obj0 = document.getElementById('"+parentId+"_id');" +
						"obj0.value = cksids.join(',');" +
						//"var obj1 = document.getElementById('"+parentId+"_text');" +
						//"obj1.value = ckstxt.join(',');" +
						parentId+"_window.hide();" +
						(tapestryBtnId==null?"":"document.getElementById('"+tapestryBtnId+"').click();\n")+
						(form==null||"".equals(form)?"":"document."+form+".submit();") +
								"}";
				break;
			case ButtonType_Window_Ok:
				text = "确定";
				handler = "function() {var cks = "+parentId+"_treePanel.getSelectionModel().getSelectedNode();" +
				"if(cks==null){"+parentId+"_window.hide();return;}"+
				"var obj0 = document.getElementById('"+parentId+"_id');" +
				"obj0.value = cks.id;" +
//				"var obj1 = document.getElementById('"+parentId+"_text');" +
				parentId+"_text.setValue(cks.text);" +
				parentId+"_window.hide();" +
				(tapestryBtnId==null?"":"document.getElementById('"+tapestryBtnId+"').click();\n")+
				(form==null||"".equals(form)?"":"document."+form+".submit();") +
						"}";
				break;
			default: 
				text = "按钮" ;
				handler = "";
			break;
		}
	}
	
	public String getSaveScript() {
		StringBuffer record = new StringBuffer();
		return record.toString();
	}
	
	public String getScript() {
		StringBuffer buttonjs = new StringBuffer();
		buttonjs.append("text:'").append(text).append("',");
		if(handler == null || "".equals(handler)) {
			buttonjs.deleteCharAt(buttonjs.length()-1);
		}else {
			buttonjs.append("handler:").append(handler);
		}
		return buttonjs.toString();
	}
}
