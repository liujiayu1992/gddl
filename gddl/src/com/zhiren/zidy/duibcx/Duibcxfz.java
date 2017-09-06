package com.zhiren.zidy.duibcx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Duibcxfz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			System.out.println("!@@@@@@@@@@@@@@@@@@@@@@");
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id, xuh, mingc, zhi\n"
						+ "  from duibcxfzlbb");
					 
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("duibcxfzlbb");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("zhi").setHeader("分组值");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		//egu.getColumn("id").setDefaultValue(""+visit.getDiancxxb_id());
		 
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("var temp_ds = new Ext.data.Store({"+
				"proxy : new Ext.zr.data.PagingMemoryProxy(gridDiv_data),"+
				"pruneModifiedRecords:true,"+
				"reader: new Ext.data.ArrayReader({}, ["+
				"{name:'ID'},{name:'XUH'},{name:'MINGC'},{name:'ZHI'}])});\n"+
				"temp_ds.load();\n"+
				"gridDiv_grid.on('afteredit',function(e){\n"+
				/*"if(e.value==e.originalValue)return;\n"+
				"var Mrcd=temp_ds.getRange();\n"+
				"for(k=0;k<=Mrcd.length-1;k++){ \n"+
				"if(e.record.get('MINGC')==Mrcd[k].get('MINGC')){ \n"+
				"e.record.set('MINGC',e.originalValue);"+
				"e.value=e.originalValue;\n"+
				"Ext.MessageBox.alert('action','名称列不能重复');\n"+
				"return;"+
				"}}});"*/
				/* ***** **/
				"if(e.value==e.originalValue)return;"+
				"var Mrcd=temp_ds.getRange();"+
				"for(i=0;i<=Mrcd.length-1;i++){"+
				"if(e.record.get('MINGC')==Mrcd[i].get('MINGC')){"+
				"e.record.set('MINGC',e.originalValue);"+
				"e.value=e.originalValue;"+
				"Ext.MessageBox.alert('提示','名称项不能重复');"+
				"return;"+
				"}}});"
				);
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Duibcxfz';" +
   	    " window.open(url,'newWin');";
	//egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}
}
