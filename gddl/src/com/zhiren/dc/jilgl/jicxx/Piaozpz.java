package com.zhiren.dc.jilgl.jicxx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Piaozpz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		JDBCcon con = new JDBCcon();
		String sql = "";
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			if (mdrsl.getLong("id") == 0) {
				sql = "insert into piaozpzb values(getNewId(100), " + (getExtGrid().getColumn("meikxxb_id").
					  combo).getBeanId(mdrsl.getString("meikxxb_id")) + ", " + (getExtGrid().getColumn("jihkjb_id").
					  combo).getBeanId(mdrsl.getString("jihkjb_id")) + ", " + (getExtGrid().getColumn("yunsfsb_id").
					  combo).getBeanId(mdrsl.getString("yunsfsb_id")) + ", " + (getExtGrid().getColumn("pinzb_id").
					  combo).getBeanId(mdrsl.getString("pinzb_id")) + ")";
			} else {
				sql = "update piaozpzb set meikxxb_id = " + (getExtGrid().getColumn("meikxxb_id").combo).getBeanId
				      (mdrsl.getString("meikxxb_id")) + ", jihkjb_id = " + (getExtGrid().getColumn("jihkjb_id").
					  combo).getBeanId(mdrsl.getString("jihkjb_id")) + ", yunsfsb_id = " + (getExtGrid().getColumn("yunsfsb_id").
					  combo).getBeanId(mdrsl.getString("yunsfsb_id")) + ", pinzb_id = " + (getExtGrid().getColumn("pinzb_id").
					  combo).getBeanId(mdrsl.getString("pinzb_id")) + " where id = " + mdrsl.getLong("id");
			}
			
			con.getUpdate(sql);
		}
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sql = "delete from piaozpzb where id = " + delrsl.getLong("id");
			
			con.getDelete(sql);
		}
		
		mdrsl.close();
		delrsl.close();
		con.Close();
	}

	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
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
		
		if (_RefreshChick) {
			_RefreshChick = false;
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql = "select pz.id, mk.mingc as meikxxb_id, kj.mingc as jihkjb_id, ys.mingc as \n" +
				     "yunsfsb_id, p.mingc as pinzb_id from piaozpzb pz, meikxxb mk, jihkjb kj, \n" +
				     "yunsfsb ys, pinzb p where pz.meikxxb_id = mk.id(+) and pz.jihkjb_id = kj.id(+) \n" +
				     "and pz.yunsfsb_id = ys.id(+) and pz.pinzb_id = p.id(+) and \n" +
					 "meikxxb_id = " + getMeikdqmcValue().getId() + 
					 "";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("piaozpzb");
		
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meikxxb order by mingc"));
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from jihkjb order by mingc"));
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb order by mingc"));
		egu.getColumn("yunsfsb_id").setDefaultValue("公路");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from pinzb order by mingc"));
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		egu.addTbarText("煤矿:");
		ComboBox meik = new ComboBox();
		meik.setTransform("MeikmcDropDown");
		meik.setId("MeikmcDropDown");
		meik.setLazyRender(true);
		meik.setWidth(90);
		egu.addToolbarItem(meik.getScript());
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		
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
			
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
		}
		
		getSelectData();
	}
	
	// 煤矿名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			if(getIMeikdqmcModels().getOptionCount() >1){
				_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
			}else{
				_MeikdqmcValue = new IDropDownBean(-1, "无选项");
			}
			
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}
	
	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id, mingc from meikxxb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
}
