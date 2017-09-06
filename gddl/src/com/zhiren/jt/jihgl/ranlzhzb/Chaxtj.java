package com.zhiren.jt.jihgl.ranlzhzb;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Chaxtj extends BasePage implements PageValidateListener {
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

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _PowerChick = false;

	public void PowerButton(IRequestCycle cycle) {
		_PowerChick = true;
	}
	
	private boolean _Power1Chick = false;

	public void Power1Button(IRequestCycle cycle) {
		_Power1Chick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		
		if (_PowerChick) {
			_PowerChick = false;
			gotoShijwc(cycle);
		}		
		if (_Power1Chick) {
			_Power1Chick = false;
			gotoShijwc1(cycle);
		}	
	}
	
	public void gotoShijwc(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		visit.setString10(this.getChaxtjb_id());
		String sql = "select nianf, beginyuef, endyuef from chaxtjb where id = " + getChaxtjb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		while(rsl.next()) {
			visit.setString11(rsl.getString("nianf"));
			visit.setString12(rsl.getString("beginyuef"));
			visit.setString13(rsl.getString("endyuef"));
		}
		cycle.activate("Shijwc");
	}
	
	public void gotoShijwc1(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		visit.setString10(this.getChaxtjb_id());
		String sql = "select nianf, beginyuef, endyuef from chaxtjb where id = " + getChaxtjb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		while(rsl.next()) {
			visit.setString11(rsl.getString("nianf"));
			visit.setString12(rsl.getString("beginyuef"));
			visit.setString13(rsl.getString("endyuef"));
		}
		cycle.activate("Ranmcgyszbwh");
	}
	private String Chaxtjb_id;//记录查询条件表ID

	public String getChaxtjb_id() {
		
		return Chaxtjb_id;
	}

	public void setChaxtjb_id(String value) {
		
		Chaxtjb_id = value;
	}
	
	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

    //	得到起始月份
	public String getYuef() {
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		return StrMonth;
	}
	
    //	得到终止月份
	public String getYuefen() {
		long intMonth;
		if (getYuefenValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefenValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		return StrMonth;
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql = "select * from chaxtjb where nianf = " + getNianf();

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("chaxtjb");
		egu.setWidth("bodyWidth");
		
		egu.getColumn("mingc").setHeader("标题");
		egu.getColumn("zhibr").setHeader("制表人");
		egu.getColumn("nianf").setHidden(true);
		egu.getColumn("nianf").setDefaultValue(getNianf());
		
		egu.getColumn("beginyuef").setHeader("起始月份");
		ComboBox beginyuef = new ComboBox();
		egu.getColumn("beginyuef").setEditor(beginyuef);
		beginyuef.setEditable(false);
		String Sql = "select mvalue, mlabel from yuefb";
		egu.getColumn("beginyuef").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));

		egu.getColumn("endyuef").setHeader("终止月份");
		ComboBox endyuef = new ComboBox();
		egu.getColumn("endyuef").setEditor(endyuef);
		endyuef.setEditable(false);
		String sql1 = "select mvalue, mlabel from yuefb";
		egu.getColumn("endyuef").setComboEditor(egu.gridId,
				new IDropDownModel(sql1));
		
		egu.getColumn("beiz").setHeader("备注");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

        // 年份ComBox
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(false);
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		
        // 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();})");
		
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
		
		GridButton jinrwhym = new GridButton("进入维护指标完成页面", "function(){"
				+ "var Mrcd = gridDiv_grid.getStore().getModifiedRecords(); \n"
				+ "		if(Mrcd.length > 0) { \n"
				+ "    		Ext.MessageBox.alert('提示信息','请先保存数据！');"
				+ "    		return false;"
				+ "		}"
				+ "var Mr = gridDiv_grid.getStore().getRange(); \n"
				+ "    if(Mr.length == 0) { \n"
				+ "         Ext.MessageBox.alert('提示信息','请先添加选择条件！');  \n"
				+ "         return false;" 
				+ "    }"
				+ "var rec = gridDiv_sm.getSelected(); \n"
				+"     if(rec!=null){\n"
				+"  	    gridDiv_history = rec.get('ID');\n"
			    +"  	    document.getElementById('CHAXTJB_ID').value=gridDiv_history; \n"
			    +"     }else{\n"
			    +"  	    Ext.MessageBox.alert('提示信息','请选中一个条件!'); \n"
			    +"  	    return;"
			    +"     }"
				+ "document.getElementById('PowerButton').click();}");
		egu.addToolbarItem("{" + jinrwhym.getScript() + "}");
		
		GridButton jinrwhym1 = new GridButton("进入燃煤实际完成维护页面", "function(){"
				+ "var Mrcd = gridDiv_grid.getStore().getModifiedRecords(); \n"
				+ "		if(Mrcd.length > 0) { \n"
				+ "    		Ext.MessageBox.alert('提示信息','请先保存数据！');"
				+ "    		return false;"
				+ "		}"
				+ "var Mr = gridDiv_grid.getStore().getRange(); \n"
				+ "    if(Mr.length == 0) { \n"
				+ "         Ext.MessageBox.alert('提示信息','请先添加选择条件！');  \n"
				+ "         return false;" 
				+ "    }"
				+ "var rec = gridDiv_sm.getSelected(); \n"
				+"     if(rec!=null){\n"
				+"  	    gridDiv_history = rec.get('ID');\n"
			    +"  	    document.getElementById('CHAXTJB_ID').value=gridDiv_history; \n"
			    +"     }else{\n"
			    +"  	    Ext.MessageBox.alert('提示信息','请选中一个条件!'); \n"
			    +"  	    return;"
			    +"     }"
				+ "document.getElementById('Power1Button').click();}");
		egu.addToolbarItem("{" + jinrwhym1.getScript() + "}");

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

			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
		}
		getSelectData();
	}
	
	// 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	// 起始月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean3() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}
	
//	 终止月份
	public boolean Changeyuefen = false;

	public IPropertySelectionModel getYuefenModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYuefenModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IDropDownBean getYuefenValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean4((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYuefenValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean4() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuefen = true;
			} else {
				Changeyuefen = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);

	}

	public IPropertySelectionModel getYuefenModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setYuefenModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(_value);
	}

}
