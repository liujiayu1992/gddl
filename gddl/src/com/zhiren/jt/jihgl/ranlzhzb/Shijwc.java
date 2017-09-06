package com.zhiren.jt.jihgl.ranlzhzb;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shijwc extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO 自动生成方法存根
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sb = new StringBuffer("begin \n");
		String sql = "";
		while(mdrsl.next()){
			sql = "update shijwcb set shijwc = " + mdrsl.getDouble("shijwc") + "where id = " + mdrsl.getLong("id") + ";";
			
			sb.append(sql);
		}
		sb.append("end;");
		if(sb.length() < 13) {
			con.Close();
		} else {
			con.getUpdate(sb.toString());
		}
		mdrsl.close();
		con.Close();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	// 生成按钮
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	
	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
		}

		if (_DeleteClick) {
			_DeleteClick = false;
			delete();
		}

		if (_CreateClick) {
			_CreateClick = false;
			create();
		}
		getSelectData();
		
		if (_ReturnClick) {
			_ReturnClick = false;
			Return(cycle);
		}
	}

	public void Return(IRequestCycle cycle) {
		cycle.activate("Chaxtj");
	}
	
	private double getShijwc(long item_id, String nianf, String beginyuef, String endyuef) {
		JDBCcon con = new JDBCcon();
		double shijwc = 0;
		ResultSetList rsl = null;
		String sql = "select * from item where bianm in (select COLUMN_NAME from USER_TAB_COLUMNS WHERE TABLE_NAME = 'YUEZBB')" +
				" and id = " + item_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			sql = "select sum("+rsl.getString("bianm")+") as shijwc from yuezbb where fenx = '本月' and to_char(riq,'yyyy') = '"+nianf+"'" +
				"and to_char(riq,'mm') between '"+beginyuef+"' and '"+endyuef+"' and diancxxb_id = " + getTreeid();
			rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				shijwc = rsl.getDouble("shijwc");
			}
		}
		rsl.close();
		con.Close();
		return shijwc;
	}
	
	public void create() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sqlS = "select * from shijwcb where diancxxb_id = "
				+ getTreeid() + " and chaxtjb_id = " + visit.getString10();
		String sqlD = "delete from shijwcb where diancxxb_id = "
				+ getTreeid() + " and chaxtjb_id = " + visit.getString10();

		ResultSetList rsl = con.getResultSetList(sqlS);
		if (rsl.next()) {
			con.getDelete(sqlD);
		}

		sqlS = "select i.id, i.beiz from item i, itemsort it\n"
				+ "where i.itemsortid = it.itemsortid and it.bianm = 'RANLZHZB' \n";
		rsl = con.getResultSetList(sqlS);

		String beginyuef = visit.getString12();
		if (Integer.parseInt(beginyuef) < 10) {
			beginyuef = "0" + beginyuef;
		}
		
		String endyuef = visit.getString13();
		if (Integer.parseInt(endyuef) < 10) {
			endyuef = "0" + endyuef;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		while (rsl.next()) {
			sqlD = "insert into shijwcb(id, diancxxb_id, item_id, chaxtjb_id, shijwc, beiz)\n"
					+ "values(getNewID(" + visit.getDiancxxb_id() + "), "
					+ getTreeid() +", "+ rsl.getLong("id") + ", " + visit.getString10()
					+ ", "+getShijwc(rsl.getLong("id"), visit.getString11(), beginyuef, endyuef)+", '"+rsl.getString("beiz")+"');";
			sb.append(sqlD);
		}
		sb.append("end;");

		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getInsert(sb.toString());
		}
		
		rsl.close();
		con.Close();
	}

	public void delete() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sqlD = "";
		String sqlS = "select * from shijwcb where diancxxb_id = " + getTreeid() + " and chaxtjb_id = " + visit.getString10();
		
		ResultSetList rsl = con.getResultSetList(sqlS);
		StringBuffer sb = new StringBuffer("begin \n");
		while (rsl.next()) {
			sqlD = "delete from shijwcb where diancxxb_id = " + getTreeid() + " and chaxtjb_id = " + visit.getString10() + ";";
			sb.append(sqlD);
		}
		sb.append("end;");
		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getDelete(sb.toString());
		}
		rsl.close();
		con.Close();
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "select sj.id, i.mingc, sj.shijwc, yj.quannyj, sj.beiz from shijwcb sj, item i, quannyjb yj\n" + 
			" where sj.item_id = i.id and sj.item_id = yj.item_id and sj.diancxxb_id = yj.diancxxb_id and sj.diancxxb_id = " 
			+ getTreeid() + " and to_char(yj.riq,'yyyy') = '"+visit.getString11()+"' and chaxtjb_id = " + visit.getString10();
        
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight - 150");
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("shijwcb");
		
		egu.getColumn("mingc").setHeader("指标名称");
		egu.getColumn("mingc").editor = null;
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("shijwc").setHeader("实际完成");
		egu.getColumn("shijwc").setWidth(74);
		egu.getColumn("quannyj").setHeader("全年预计");
		egu.getColumn("quannyj").setEditor(null);
		egu.getColumn("quannyj").setRenderer("function(data,cell,record,rowIndex,columnIndedx,store){"+
 
					"cell.attr='style=background-color:#E3E3E3';"+
					"return data;"+
		            "}");
		egu.getColumn("quannyj").setWidth(74);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").editor = null;
		egu.getColumn("beiz").setWidth(74);

		// 电厂树
		egu.addTbarText("电厂树:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		egu.addToolbarItem("{"
						+ new GridButton("刷新",
								"function(){document.getElementById('RefurbishButton').click();}")
								.getScript() + "}");

		// 生成按钮
		GridButton CreateButton = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		CreateButton.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(CreateButton);

		GridButton DeleteButton = new GridButton("删除",
				getBtnHandlerScript("DeleteButton"));
		DeleteButton.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(DeleteButton);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		GridButton ReturnButton = new GridButton("返回",
				"function(){document.getElementById('ReturnButton').click();}");
		ReturnButton.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(ReturnButton);

		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer btnsb = new StringBuffer();
		String cnDate = visit.getString11() + "年";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} 
		if (btnName.endsWith("DeleteButton")){
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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

			visit.setDefaultTree(null);
			setTreeid(null);
			getSelectData();
		}
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
}
