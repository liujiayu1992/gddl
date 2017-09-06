package com.zhiren.jt.het.hetfhrgysgl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.*;
/*2009-08-12 
 * ly
 * 
 * 合同发货人供应商关联
 * 
 * */
public class Hetfhrgysgl extends BasePage implements PageValidateListener {
//	public List gridColumns;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		 int flag = -1;
		 flag = visit.getExtGrid1().Save(getChange(), visit);
		 if(flag>=0){
			 setMsg("保存成功！");
		 }else{
			 setMsg("保存失败！");
		 }
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	public void submit(IRequestCycle cycle) {
//		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "";
		JDBCcon con = new JDBCcon();
		sql = 
			"select h.id,h.hetfhr_id,g.mingc as gongysb_id,decode(h.zhuangt,1,'启用','不启用') as zhuangt\n" +
			"from hetfhrgysglb h,gongysb g\n" + 
			"where h.gongysb_id = g.id\n" + 
			"      and h.hetfhr_id = "+this.getFahrValue().getId()+"\n" +
			"order by h.id\n";		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("hetfhrgysglb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("hetfhr_id").setHeader("hetfhr_id");
		egu.getColumn("hetfhr_id").setDefaultValue(this.getFahrValue().getId()+"");
		egu.getColumn("hetfhr_id").setHidden(true);
		egu.getColumn("hetfhr_id").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("gongysb_id").setWidth(180);
		egu.getColumn("zhuangt").setHeader("状态");
//		egu.getColumn("zhuangt").setDefaultValue("1");
		
//		供应商
//		ComboBox c = new ComboBox();
//		c.setEditable(false);
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select distinct id,mingc from gongysb "
						+ " order by mingc"));
		egu.getColumn("gongysb_id").setReturnId(true);
		
//		 状态类型
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"启用"));
		list.add(new IDropDownBean(0,"未启用"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("zhuangt").setReturnId(true);//绑定了，页面显示汉字，库里显示数字。
		egu.getColumn("zhuangt").setDefaultValue("启用");
		
//********************工具栏************************************************
		egu.addTbarText("合同发货人:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("FahrDropDown");
		comb3.setId("Fahr");
		comb3.setLazyRender(true);// 动态绑定
		egu.addOtherScript("Fahr.on('select',function(){document.forms[0].submit();});");
		comb3.setWidth(180);
		egu.addToolbarItem(comb3.getScript());
		
		egu.addTbarText("-");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
			setFahrValue(null);	//3
			setFahrModel(null);
			getFahrModels();		//3
			visit.setboolean2(false);
		}
		getSelectData();
	}
	
	 //合同发货人
	public boolean _fahrchange = false;
	public IDropDownBean getFahrValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getFahrModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setFahrValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getFahrModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getFahrModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setFahrModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getFahrModels() {
		
		String sql ="select distinct h.gongysb_id,h.gongfdwmc from hetb h order by gongfdwmc ";
			
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

}



