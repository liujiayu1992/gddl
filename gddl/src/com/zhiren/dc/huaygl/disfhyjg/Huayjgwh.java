package com.zhiren.dc.huaygl.disfhyjg;


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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huayjgwh extends BasePage implements PageValidateListener {
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

	private void Save(String strchange, Visit visit) {

	
		JDBCcon con = new JDBCcon();
		
		//获得删除的数据
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
//		
		StringBuffer sql = new StringBuffer();
		
		while (delrsl.next()) {
			ResultSetList rs = con.getResultSetList("select id from jiesszbmglb where jiesszbmzb_id ="+delrsl.getString("id"));
			while(rs.next()){
				sql.append("begin \n");
				sql.append("delete from ").append("jiesszbmzb").append(" where id ='").append(delrsl.getString("id")).append("';\n");
				sql.append("delete from ").append("jiesszbmglb").append(" where id = ").append(rs.getString("id")+";\n");
				sql.append("end;");
				con.getDelete(sql.toString());
			}

		}
		
		delrsl.close();
		
		
		
		
		//获得所有修改的数据
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		
		//以下是添加操作
		ResultSetList rsll = con.getResultSetList("select id from jiesszbmb where bianm = '"+Locale.addhyjg_jies+"'");
		String jiesszbmb_id = "";
		while(rsll.next()){
			jiesszbmb_id = rsll.getString("id");
		}
		while(mdrsl.next()){
			StringBuffer sql1 = new StringBuffer("");
			//随机获取id
			String id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			if ("0".equals(mdrsl.getString("ID"))) {
				sql1.append("begin \n");
				sql1.append("insert into jiesszbmzb values(").append(id+",").append(mdrsl.getString("BIANM")+",").append(mdrsl.getString("ZHI")+");\n");
				sql1.append("insert into jiesszbmglb values(").append(id+",").append(jiesszbmb_id+",").append(id+");\n");
				
				sql1.append("end;");
				con.getInsert(sql1.toString());
				
			}else{
				sql1.append("update jiesszbmzb set bianm = '").append(mdrsl.getString("BIANM")+"',").append("zhi = '").append(mdrsl.getString("ZHI")+"'").append(" where id = "+mdrsl.getString("ID"));
				con.getUpdate(sql1.toString());
			}
		}	
	
		mdrsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save(getChange(),visit);
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select distinct j.id,j.bianm,j.zhi\n" +
			"  from jiesszbmzb j, jiesszbmb jb, jiesszbmglb gl\n" + 
			" where j.id = gl.jiesszbmzb_id\n" + 
			"       and jb.id = gl.jiesszbmb_id\n" + 
			"       and jb.bianm = '"+Locale.addhyjg_jies+"'\n" + 
			"       order by id";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("zhi").setHeader("值");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		
		
		GridButton insertButton = new GridButton("添加", GridButton.ButtonType_Insert, "gridDiv", egu.getGridColumns(), null);
		insertButton.setIcon(SysConstant.Btn_Icon_Insert);
		
		
		GridButton deleteButton = new GridButton("删除", GridButton.ButtonType_Delete, "gridDiv", egu.getGridColumns(), null);
		deleteButton.setIcon(SysConstant.Btn_Icon_Delete);
		
		
		GridButton saveButton = new GridButton("保存", GridButton.ButtonType_Save, "gridDiv", egu.getGridColumns(), "SaveButton");
		saveButton.setIcon(SysConstant.Btn_Icon_Save);
		
		//判断结算设置编码表是否有编码为“化验机构”的数据
		ResultSetList r = con.getResultSetList("select * from jiesszbmb j where j.bianm = '"+Locale.addhyjg_jies+"'");
		if(!(r.next())){
			refurbish.setDisabled(true);
			insertButton.setDisabled(true);
			deleteButton.setDisabled(true);
			saveButton.setDisabled(true);
			setMsg("请先维护结算设置页面！！！");	 
		}
		
		egu.addTbarBtn(refurbish);
		egu.addTbarBtn(insertButton);
		egu.addTbarBtn(deleteButton);
		egu.addTbarBtn(saveButton);
		
		setExtGrid(egu);
		con.Close();
	}
//	// 工具栏使用的方法
//	public Toolbar getToolbar() {
//		return ((Visit) this.getPage().getVisit()).getToolbar();
//	}
//
//	public void setToolbar(Toolbar tb1) {
//		((Visit) this.getPage().getVisit()).setToolbar(tb1);
//	}
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
