package com.zhiren.dc.zhuangh.caiygl;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*作者:王总兵
 *日期:2010-7-3 12:01:40
 *描述:增加电厂树
 * 
 * 
 */


public class Duowgl_zh extends BasePage implements PageValidateListener {
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		String sql = "begin \n";
		while(rs.next()){
			sql += "delete duowglb  where id = "+ rs.getString("id")+";\n";
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getDelete(sql);
		}
		rs.close();
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		Visit visit=(Visit)getPage().getVisit();
		while(rs.next()){
			if(rs.getInt("id") == 0){
				
				sql += "insert into duowglb(id,diancxxb_id,mingc_old,mingc_new,beiz) values(getnewId("
					+ v.getDiancxxb_id() + "),"+getTreeid() + ",'" 
					+ rs.getString("mingc_old") +"','"+
					rs.getString("mingc_new") +"',"
					+ "'" + rs.getString("beiz") +"'" +");\n";
			}else{
				sql += "update duowglb set mingc_old='" 
					+ rs.getString("mingc_old")
				+ "' , mingc_new='" + rs.getString("mingc_new")
				+ "' , beiz='"+ rs.getString("beiz")
				+ "' where id = "+ rs.getString("id")+";\n";
			}
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getInsert(sql);
		}
		rs.close();
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
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 

			"select g.id,g.mingc_old,g.mingc_new,g.beiz from duowglb g where " +
			"g.diancxxb_id="+getTreeid();


		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		egu.setTableName("duowglb");
		egu.getColumn("beiz").setHeader("备注");
//		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
//		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
//		egu.getColumn("tongjkj").setHeader("统计口径");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
//		egu.getColumn("diancxxb_id").setDefaultValue(v.getDiancxxb_id()+"");
//		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
//		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
//						new IDropDownModel(SysConstant.SQL_Kouj));
//		egu.getColumn("jihkjb_id").editor.allowBlank = true;
		egu.getColumn("mingc_old").setHeader("上一年煤堆编号");
		egu.getColumn("mingc_new").setHeader("下一年煤堆编号");
		egu.getColumn("mingc_old").setEditor(new ComboBox());
		egu.getColumn("mingc_old").setComboEditor(egu.gridId,
				new IDropDownModel(" select id,mingc from duow "));
//		egu.getColumn("yunsfsb_id").editor.allowBlank = true;
		egu.getColumn("mingc_new").setEditor(new ComboBox());
		egu.getColumn("mingc_new").setComboEditor(egu.gridId,
						new IDropDownModel(
								" select id,mingc from duow "
										));
		/*
		 * huochaoyuan 2008-12-21
		 * 修改上边取数sql，mk.quanc改为mk.mingc,取全称容易造成混淆，该页面取数时也选用mingc,故这里改为该字段
		 */
//		List kj = new ArrayList();
//		kj.add(new IDropDownBean(1,"净重"));
//		kj.add(new IDropDownBean(2,"票重"));
//		kj.add(new IDropDownBean(3,"实收"));
//		sql = "select i.id,i.mingc from item i, itemsort s\n" +
//			"where i.itemsortid = s.id\n" + 
//			"and s.mingc = '来煤数量项目'";
//		egu.getColumn("tongjkj").setEditor(new ComboBox());
//		egu.getColumn("tongjkj").setComboEditor(egu.gridId, new IDropDownModel(sql));
//		egu.getColumn("tongjkj").setReturnId(false);
//		egu.getColumn("lurry").setHeader("人员");
//		egu.getColumn("lurry").setEditor(null);
//		egu.getColumn("lurry").setDefaultValue(v.getRenymc());
		
//		是否显示选择电厂树
		if (v.isFencb()) {
			egu.addTbarText("单位名称:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
			egu.addTbarText("-");
		}
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

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid("");
			
		}
		init();
	}
	
	private void init() {
		setExtGrid(null);
		setTree(null);
		getSelectData();
	}
}
