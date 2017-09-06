package com.zhiren.jt.zdt.monthreport.diancgysgl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Diancgysgl extends BasePage implements PageValidateListener {
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
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("begin \n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from diancgysglb where id = ")
			.append(delrsl.getString("id")).append("; \n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			
			if ("0".equals(mdrsl.getString("id"))||"".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into diancgysglb (id,diancxxb_id,gongysb_id,shiyzt) values(")
				.append("getnewid("+ getTreeid() +"),(select distinct max(id) from diancxxb  where mingc='")
				.append(mdrsl.getString("diancmc")).append("'), (select distinct max(id) from gongysb  where mingc='").append(mdrsl.getString("gysmc")).append("'), ")
				.append("(select decode('"+ mdrsl.getString("shiyzt")+ "','已使用',1,2) as shiyzt from dual)").append("); \n");
			} else {
				sbsql.append("update diancgysglb set diancxxb_id = (select distinct max(id) from diancxxb  where mingc='").append(mdrsl.getString("diancmc"))
				.append("'),gongysb_id=(select distinct max(id) from gongysb  where mingc='").append(mdrsl.getString("gysmc"))
				.append("'),shiyzt=(select decode('"+ mdrsl.getString("shiyzt")+ "','已使用',1,2) as shiyzt from dual)").append(" where id = ").append(mdrsl.getString("id"))
				.append("; \n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
		
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		}
		String sql_diancgysgl="select gl.id as id,gl.diancxxb_id as diancid,dc.mingc as diancmc,gl.gongysb_id as gysid,gy.mingc as gysmc,decode(gl.shiyzt,1,'已使用',2,'未使用') as shiyzt\n" +
						"from diancgysglb gl,gongysb gy,diancxxb dc\n" + 
						"where gl.diancxxb_id=dc.id and gl.gongysb_id=gy.id "+str+"\n" + 
						"order by dc.mingc";
		ResultSetList rsl_gl = con.getResultSetList(sql_diancgysgl);
		if(rsl_gl.equals(null)|| rsl_gl.getRows()==0){
			String sql=	
				"select distinct null as id,kj.diancxxb_id as diancid,dc.mingc as diancmc,kj.gongysb_id as gysid,gy.mingc as gysmc,'未使用' as shiyzt " +
				"from yuetjkjb kj,gongysb gy,diancxxb dc\n" + 
				"where kj.gongysb_id=gy.id and kj.diancxxb_id=dc.id "+str+"\n" + 
				"order by dc.mingc";
		
		  rsl_gl = con.getResultSetList(sql);
		}
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl_gl);
		egu.setTableName("diancgysglb");
		egu.setWidth("bodyWidth");
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setWidth(60);
		egu.getColumn("diancid").setHeader("电厂id");
		egu.getColumn("diancid").setHidden(true);
		egu.getColumn("diancid").setWidth(60);
		egu.getColumn("diancmc").setHeader("单位名称");
		egu.getColumn("diancmc").setWidth(120);
		egu.getColumn("gysid").setHeader("供应商id");
		egu.getColumn("gysid").setHidden(true);
		egu.getColumn("gysid").setWidth(60);
		egu.getColumn("gysmc").setHeader("供应商名称");
		egu.getColumn("gysmc").setWidth(150);
		egu.getColumn("shiyzt").setHeader("使用状态");
		egu.getColumn("shiyzt").setWidth(80);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		//单位名称下拉框
		//电厂下拉框
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// 选集团时刷新出所有的电厂
			egu.getColumn("diancmc").setEditor(new ComboBox());
			egu.getColumn("diancmc").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancmc").setReturnId(true);
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			egu.getColumn("diancmc").setEditor(new ComboBox());
			egu.getColumn("diancmc").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
			egu.getColumn("diancmc").setReturnId(true);
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			egu.getColumn("diancmc").setEditor(new ComboBox());
			egu.getColumn("diancmc").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancmc").setDefaultValue(mingc);
		}		
		//供应商下拉框
		egu.getColumn("gysmc").setEditor(new ComboBox());
		egu.getColumn("gysmc").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from gongysb where fuid=0 or fuid=-1 order by mingc"));
		egu.getColumn("gysmc").setReturnId(true);
		//是否启用
		egu.getColumn("shiyzt").setHeader("使用状态");
		ComboBox Shiyong=new ComboBox();
		egu.getColumn("shiyzt").setEditor(Shiyong);
		List Shiyonglist=new ArrayList();
		Shiyonglist.add(new IDropDownBean(1,"未使用"));
		Shiyonglist.add(new IDropDownBean(2,"已使用"));
	    egu.getColumn("shiyzt").setComboEditor(egu.gridId, new IDropDownModel(Shiyonglist));
	    egu.getColumn("shiyzt").setDefaultValue("未使用");
	    egu.getColumn("shiyzt").setReturnId(true);
	    
		// 电厂树
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
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
			setTreeid(null);
		}
		getSelectData();
	}

	

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid==null||treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
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

}