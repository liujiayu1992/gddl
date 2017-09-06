package com.zhiren.dc.gdxw.jicxx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meikkdszb extends BasePage implements PageValidateListener {
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
		JDBCcon con = new JDBCcon();
		
		
		
		StringBuffer sbsqlDel = new StringBuffer("begin\n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsqlDel.append("delete from meikkdszb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		sbsqlDel.append("end;");
		if(sbsqlDel.length()>15){
			con.getUpdate(sbsqlDel.toString());
		}
		
		delrsl.close();
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into meikkdszb(id,meikxxb_id, meikdwmc,shul) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append(mdrsl.getString("meikxxb_id")+",")
				.append("(select mingc from meikxxb where id="+mdrsl.getString("meikxxb_id")+")")
				.append(",").append(mdrsl.getString("shul"))
				.append(");\n");
			} else {
				sbsql.append("update meikkdszb set ")
				.append("meikxxb_id=").append(mdrsl.getString("meikxxb_id"))
				.append(",meikdwmc=").append("(select mingc from meikxxb where id="+mdrsl.getString("meikxxb_id")+")")
				.append(", shul = ").append(mdrsl.getString("shul"))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		if(sbsql.length()>15){
			con.getUpdate(sbsql.toString());
		}
		
		mdrsl.close();
		con.Close();
		
		
		
		
		
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		getSelectData();
	}

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="select id,meikxxb_id,meikdwmc,shul\n" +
			"from meikkdszb\n" + 
			"order by id";



		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meikkdszb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("meikxxb_id").setHeader("入炉编码");
		egu.getColumn("meikxxb_id").setWidth(150);
	
		egu.getColumn("meikdwmc").setHeader("名称");
		egu.getColumn("meikdwmc").setWidth(150);
		egu.getColumn("meikdwmc").setHidden(true);
		egu.getColumn("meikdwmc").setEditor(null);
		
		egu.getColumn("shul").setHeader("入炉个数");
		egu.getColumn("shul").setWidth(80);
		egu.getColumn("shul").setDefaultValue("0");
		
		
//		设置煤矿ID下拉框
		ComboBox cmk_id= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cmk_id); cmk_id.setEditable(true);
		String mkSql_id="select id,id  as mingc  from meikxxb";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql_id));


		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
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
			getSelectData();
		}
	}
}
