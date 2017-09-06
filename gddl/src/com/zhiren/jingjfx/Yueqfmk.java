package com.zhiren.jingjfx;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 王磊
 * 月欠付煤款
 *
 */
public class Yueqfmk extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
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
		Visit visit = (Visit)this.getPage().getVisit();
		getExtGrid().Save(getChange(), visit);
	}
 
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb.append("select * from jjfxqfmk where riq = to_date('")
		.append(getYearValue().getId())
		.append("-").append(getMonthValue().getId())
		.append("-01','yyyy-mm-dd')");
		if(!con.getHasIt(sb.toString())) {
			StringBuffer Isql = new StringBuffer();  
			Isql.append("insert into jjfxqfmk\n")
			.append("(id, diancxxb_id, riq) values(getnewId(").append(visit.getDiancxxb_id()).append("),")
			.append(visit.getDiancxxb_id()).append(",to_date('").append(getYearValue().getId()).append("-").append(getMonthValue().getId()).append("-01','yyyy-mm-dd'))");
			con.getInsert(Isql.toString());
		}
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("jjfxqfmk");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").editor = null;
		egu.getColumn("chenqmk").setHeader("陈欠煤矿");
		egu.getColumn("chenqmk").setWidth(80);
		egu.getColumn("xinqmk").setHeader("新欠煤款");
		egu.getColumn("xinqmk").setWidth(80);
		
		egu.addTbarText("年:");
		ComboBox y=new ComboBox();
		y.setWidth(60);
		y.setTransform("Year");
		y.setEditable(true);
		egu.addToolbarItem(y.getScript());
		
		egu.addTbarText("月");
		ComboBox m=new ComboBox();
		m.setWidth(60);
		m.setTransform("Month");
		m.setEditable(true);
		egu.addToolbarItem(m.getScript());
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新 '+Ext.getDom('YEAR').value+' 年 '+Ext.getDom('MONTH').value+' 月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
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
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
//	设置年下拉框
	public IDropDownBean getYearValue() {
		if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYearModel().getOptionCount(); i++) {
				Object obj = getYearModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setYearValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}

	public void setYearValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getYearModel() {
		if (((Visit)this.getPage().getVisit()).getProSelectionModel3() == null) {
			getYearModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYearModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getYearModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if(i<10)
				listMin.add(new IDropDownBean(i, "0"+i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setYearModel(new IDropDownModel("select yvalue,ylabel from nianfb where to_char(sysdate,'yyyy')+1 >= yvalue"));
	}
	
//	设置月下拉框
	public IDropDownBean getMonthValue() {
		if (((Visit)this.getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getMonthModel().getOptionCount(); i++) {
				Object obj = getMonthModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMonthValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}

	public void setMonthValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getMonthModel() {
		if (((Visit)this.getPage().getVisit()).getProSelectionModel4() == null) {
			getMonthModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	
	public void setMonthModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void getMonthModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if(i<10)
				listMin.add(new IDropDownBean(i, "0"+i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMonthModel(new IDropDownModel("select mvalue,mlabel from yuefb"));
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
			visit.setString1(null);
			setYearValue(null);
			setYearModel(null);
			getYearModels();
			setMonthValue(null);
			setMonthModel(null);
			getMonthModels();
			getSelectData();
		}
	}
}