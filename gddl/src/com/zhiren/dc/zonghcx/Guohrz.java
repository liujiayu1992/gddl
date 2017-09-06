package com.zhiren.dc.zonghcx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Guohrz extends BasePage implements PageValidateListener {
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

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
//
//	private boolean _SaveChick = false;
//
//	public void SaveButton(IRequestCycle cycle) {
//		_SaveChick = true;
//	}

		private boolean _RefurbishChick = false;
	
		public void RefurbishButton(IRequestCycle cycle) {
			_RefurbishChick = true;
		}
	public void submit(IRequestCycle cycle) {
//		if (_SaveChick) {
//			_SaveChick = false;
//			Save();
////			getSelectData();
//		}
		if(_RefurbishChick){
			_RefurbishChick=false;
			this.getSelectData();
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		String strMokmc = "" ;
		strMokmc = this.getMeikdqmcValue().getValue();
		JDBCcon con = new JDBCcon();
		String sql = "select to_char(caozsj,'yyyy-mm-dd hh24:mi:ss') as caozsj,caozy,caoz,ip,beiz from rizb\n" +
			"       where  to_char(caozsj,'yyyy-mm-dd')>='"+ getRiq() +"' and\n" + 
			"       to_char(caozsj,'yyyy-mm-dd')<='"+ getriq1()+"'  and mokmc='" + strMokmc + "'  order by caozsj desc";

		
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rizb");
		egu.getColumn("caozsj").setHeader("操作时间");
		egu.getColumn("caozsj").setEditor(null);
		egu.getColumn("caozsj").update=false;
		egu.getColumn("caozy").setHeader("操作员");
		egu.getColumn("caozy").setEditor(null);
		egu.getColumn("caozy").update=false;
		egu.getColumn("caoz").setHeader("操作类型");
		egu.getColumn("caoz").setEditor(null);
		egu.getColumn("caoz").update=false;
		egu.getColumn("ip").setHeader("网络地址");
		egu.getColumn("ip").setEditor(null);
		egu.getColumn("ip").update=false;
		egu.getColumn("beiz").setHeader("操作记录");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("beiz").update=false;
		egu.getColumn("beiz").setWidth(800);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		
		egu.addTbarText("模块名称:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("MokmcDropDown");
		comb2.setId("mokmc");
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(130);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("修改时间:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("riq","Form0");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		egu.addToolbarItem(df.getScript());
		
		DateField df1 = new DateField();
		df1.setValue(this.getriq1());
		df1.Binding("riq1","Form0");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		egu.addToolbarItem(df1.getScript());
		GridButton rbtn = new GridButton("查看",
							"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		//egu.addOtherScript("mokmc.on('select',function(){document.forms[0].submit();});");
		egu.addTbarBtn(rbtn);
		//egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		//egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(e.column!=6){return;};var biaot=e.record.get('BIAOT');var records=gridDiv_ds.getRange(e.row);for (var i=0;i<records.length;i++){if(records[i].get('BIAOT')==biaot){records[i].set('ZHID',e.value);}}});");
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
			setMeikdqmcValue(null);
			getIMokmcModels();
		}
		getSelectData();
	}
	private String riq1;
	public String getriq1() {
		if(riq1==null||riq1.equals("")){
			riq1=DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setriq1(String riq1) {
		this.riq1 = riq1;
	}
	private String riq;
	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(DateUtil.AddDate(new Date(),0, DateUtil.AddType_intDay));
		}
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	// 模块名称
	public boolean _mokmcchange = false;

	private IDropDownBean _MokmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MokmcValue == null) {
			if(getIMokmcModels().getOptionCount() >1){
				_MokmcValue = (IDropDownBean) getIMokmcModels().getOption(0);
			}else{
				_MokmcValue = new IDropDownBean(-1,"无选项");
			}
			
		}
		return _MokmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MokmcValue != null) {
			id = _MokmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_mokmcchange = true;
			} else {
				_mokmcchange = false;
			}
		}
		_MokmcValue = Value;
	}

	private IPropertySelectionModel _IMokmcModel;

	public void setIMokmcModel(IPropertySelectionModel value) {
		_IMokmcModel = value;
	}

	public IPropertySelectionModel getIMokmcModel() {
		if (_IMokmcModel == null) {
			getIMokmcModels();
		}
		return _IMokmcModel;
	}

	public IPropertySelectionModel getIMokmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select rownum as id,  mokmc as mingc from (select distinct mokmc from rizb ) ";
			_IMokmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMokmcModel;
	}
}
