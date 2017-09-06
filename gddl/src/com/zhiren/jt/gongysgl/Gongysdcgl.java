package com.zhiren.jt.gongysgl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 类名：供应商电厂关联
 */

public class Gongysdcgl extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveChick) {
			_SaveChick = false;
			save();
		}
	}

	private void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from gongysdcglb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			
			if ("0".equals(mdrsl.getString("id"))) {
				
				sbsql.append("insert into gongysdcglb(id, diancxxb_id, gongysb_id) values(getnewid(")
				.append(getTreeid()).append("), ")
				.append((getExtGrid().getColumn("diancxxb_id").combo).getBeanId(mdrsl.getString("diancxxb_id"))).append(", ")
				.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id")))
				.append(");\n");
			} else {
				
				sbsql.append("update gongysdcglb set diancxxb_id = ")
				.append((getExtGrid().getColumn("diancxxb_id").combo).getBeanId(mdrsl.getString("diancxxb_id")))
				.append(", gongysb_id = ")
				.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id")))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}

	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql = 
			"select gl.id, dc.mingc diancxxb_id, gys.mingc gongysb_id\n" +
			"  from gongysdcglb gl, diancxxb dc, gongysb gys\n" + 
			" where gl.diancxxb_id = dc.id\n" + 
			"   and gl.gongysb_id = gys.id\n" +
			"   and (dc.id = "+ getTreeid() +" or dc.fuid = "+ getTreeid() +")\n" + 
			" order by dc.id, gys.mingc";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("diancxxb_id").setHeader("单位名称");
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("gongysb_id").setHeader("供应商名称");
		egu.getColumn("gongysb_id").setWidth(200);

		if (getDiancTreeJib() == 2) {
			
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select dc.id, dc.mingc from diancxxb dc where dc.fuid = "+ getTreeid() +" order by dc.mingc"));
			
		} else if (getDiancTreeJib() == 3) {
			
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select dc.id, dc.mingc from diancxxb dc where dc.id = " + getTreeid()));
			
			String diancmc = "";
			ResultSetList rs = con.getResultSetList("select dc.id, dc.mingc from diancxxb dc where dc.id = " + getTreeid());
			while(rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
			egu.getColumn("diancxxb_id").setDefaultValue(diancmc);
		}
		
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from gongysb where leix = 1 and id not in (select distinct gongysb_id from gongysdcglb) order by mingc"));

		egu.addTbarText("单位名称：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,
			((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
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
		egu.addTbarText("-");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
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

//	电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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
//	电厂树_结束

//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();

		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id = "+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setTreeid(null);
		}
		getSelectData();
	}

}