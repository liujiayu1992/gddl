package com.zhiren.jt.zdt.xitgl.daimgl.gongyscx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Gongyscx_tc extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
		Visit visit1 = (Visit) this.getPage().getVisit();
		visit1.getExtGrid1().Save(getChange(), visit1);
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
		Visit visit1 = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String tiaoj="";
	    tiaoj=visit1.getString1();
		
		String Sql="select gs.id, g.mingc as meikdq,gs.mingc as meikjc,gs.quanc as meikqc,gs.bianm as meikbm,\n"
				+ "s.quanc as shengf,c.quanc as chengs\n"
				+ "from gongysb g,gongysb gs,shengfb s,chengsb c\n"
				+ "where gs.fuid=g.id\n"
				+ "and gs.shengfb_id=s.id(+)\n"
				+ "and gs.chengsb_id=c.id(+)\n"
				+ "and gs.quanc like '%"+tiaoj+"%'\n"
				+ "order by g.mingc,gs.bianm";

		ResultSetList rsl = con.getResultSetList(Sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("gongysb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikdq").setCenterHeader("煤矿地区");
		egu.getColumn("meikjc").setCenterHeader("煤矿简称");
		egu.getColumn("meikqc").setCenterHeader("煤矿全称");
		egu.getColumn("meikbm").setCenterHeader("煤矿编码");
		egu.getColumn("shengf").setCenterHeader("省份");
		egu.getColumn("chengs").setCenterHeader("城市");
		egu.addPaging(100);
		
		egu.getColumn("meikjc").setWidth(120);
		egu.getColumn("meikqc").setWidth(230);
		egu.getColumn("shengf").setWidth(80);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));
	
	
	

		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		
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
		Visit visit1 = (Visit) getPage().getVisit();
		visit1.setString1("");
		if (cycle.getRequestContext().getParameter("tiaoj") != null) {
			visit1.setString1(String.valueOf(cycle.getRequestContext().getParameter("tiaoj")));
			
		}
		if (!visit1.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit1.setActivePageName(getPageName().toString());
			visit1.setList1(null);
		
		}
		
		getSelectData();
	}
}
