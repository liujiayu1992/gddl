package com.zhiren.zidy;

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
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zidybxcs extends BasePage implements PageValidateListener {
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
			getSelectData();
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList(
						"select zidybxcs.id,\n" +
						"       z_paramname,\n" + 
						"       z_datatypes,\n" + 
						"       z_prompttext,\n" + 
						"       z_controltype,\n" + 
						"       z_controlwidth,\n" + 
						"       zidysjy.z_name as zidysjy_id,\n" + 
						"       z_labelfield,\n" + 
						"       z_valuefield,\n" + 
						"       z_defaultvalue,\n" + 
						"       z_description\n" + 
						"  from zidybxcs,zidysjy where zidysjy.id(+) = zidysjy_id"
);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zidybxcs");
		egu.getColumn("z_paramname").setHeader("参数名");
		egu.getColumn("z_paramname").setWidth(100);
		egu.getColumn("z_datatypes").setHeader("数据类型");
		egu.getColumn("z_datatypes").setWidth(80);
		ComboBox comb1= new ComboBox();
		egu.getColumn("z_datatypes").setEditor(comb1);
		comb1.setEditable(true);
		comb1.setForceSelection(false);
		List l1= new ArrayList();
		 l1.add(new IDropDownBean(0, "date"));
		 l1.add(new IDropDownBean(1, "datetime"));
		 l1.add(new IDropDownBean(2, "varchar"));
		 l1.add(new IDropDownBean(3, "number"));
		egu.getColumn("z_datatypes").setComboEditor(egu.gridId, new IDropDownModel(l1));
		egu.getColumn("z_datatypes").returnId = false;
		
		
		
		egu.getColumn("z_prompttext").setHeader("提示文字");
		egu.getColumn("z_prompttext").setWidth(100);
//		egu.getColumn("mingc").setHidden(true);
		
		egu.getColumn("z_controltype").setHeader("控件类型");
		egu.getColumn("z_controltype").setWidth(80);
		ComboBox comb2= new ComboBox();
		egu.getColumn("z_controltype").setEditor(comb2);
		comb2.setEditable(true);
		comb2.setForceSelection(false);
		List l2= new ArrayList();
		 l2.add(new IDropDownBean(0, "combo"));
		 l2.add(new IDropDownBean(1, "textFiled"));
		 l2.add(new IDropDownBean(2, "numberField"));
		 l2.add(new IDropDownBean(3, "dateField"));
		egu.getColumn("z_controltype").setComboEditor(egu.gridId, new IDropDownModel(l2));
		egu.getColumn("z_controltype").returnId = false;
		
		egu.getColumn("z_controlwidth").setHeader("控件宽度");
		egu.getColumn("z_controlwidth").setWidth(70);
		egu.getColumn("zidysjy_id").setHeader("自定义数据源");
		egu.getColumn("zidysjy_id").setWidth(100);
		
		ComboBox shujy = new ComboBox();
		egu.getColumn("zidysjy_id").setEditor(shujy);
		egu.getColumn("zidysjy_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, z_name from zidysjy where z_remark = '基础数据源'"));
		egu.getColumn("zidysjy_id").returnId = true;
		egu.getColumn("zidysjy_id").editor.setAllowBlank(true);
		
		egu.getColumn("z_labelfield").setHeader("数据源显示字段");
		egu.getColumn("z_labelfield").setWidth(100);
		egu.getColumn("z_valuefield").setHeader("数据源值字段");
		egu.getColumn("z_valuefield").setWidth(100);
		egu.getColumn("z_defaultvalue").setHeader("默认值");
		egu.getColumn("z_defaultvalue").setWidth(70);
		ComboBox comb3= new ComboBox();
		egu.getColumn("z_defaultvalue").setEditor(comb3);
		comb3.setEditable(true);
		comb3.setForceSelection(false);
		
		
		
		List l3= new ArrayList();
		 l3.add(new IDropDownBean(0, "?本日"));
		 l3.add(new IDropDownBean(1, "?上一日"));
		 l3.add(new IDropDownBean(2, "?下一日"));
		 l3.add(new IDropDownBean(3, "?本月"));
		 l3.add(new IDropDownBean(4, "?上一月"));
		 l3.add(new IDropDownBean(5, "?下一月"));
		 l3.add(new IDropDownBean(3, "?本年"));
		 l3.add(new IDropDownBean(4, "?上一年"));
		 l3.add(new IDropDownBean(5, "?下一年"));
		egu.getColumn("z_defaultvalue").setComboEditor(egu.gridId, new IDropDownModel(l3));
		egu.getColumn("z_defaultvalue").returnId = false;
		comb3.setForceSelection(false);
		
		
		egu.getColumn("z_description").setHeader("描述");
		egu.getColumn("z_description").setWidth(100);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
//				+ "var end = url.indexOf(';');"
//				+ "url = url.substring(0,end);"
//				+ "url = url + '?service=page/' + 'Chezreport&lx=rezc';"
//				+ " window.open(url,'newWin');";
//		egu.addToolbarItem("{"
//				+ new GridButton("打印", "function (){" + str + "}").getScript()
//				+ "}");
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
