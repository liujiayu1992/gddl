package com.zhiren.dc.jilgl.gongl.rijh;


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
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rijhcyds extends BasePage implements PageValidateListener {

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
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
		String sql = "begin\n";
		while(rs.next()){
			sql += "update qicrjhcpb set caiyds = "+
			rs.getString("caiyds") 
			+" where id = "+rs.getString("id")+";\n";
		}
		sql += "end;\n";
		con.getUpdate(sql);
		con.Close();
	}
	
	private void Return(IRequestCycle cycle){
		cycle.activate("Rijhcywh");
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
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
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData() {
		Visit v = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select p.id,c.cheph,p.yunmcs,p.caiyds\n" +
			"from qicrjhcpb p, chelxxb c\n" + 
			"where p.chelxxb_id = c.id and p.qicrjhb_id = "+v.getString10();			
		ResultSetList rs = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("cheph").setWidth(100);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("yunmcs").setEditor(null);
		egu.getColumn("yunmcs").setWidth(100);
		egu.getColumn("yunmcs").setHeader(Locale.qicrjh_jhcs);
		egu.getColumn("caiyds").setWidth(100);
		egu.getColumn("caiyds").setHeader(Locale.qicrjh_cyds);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//只能单选中行
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(21);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('ReturnButton').click();}");
		GridButton gbr = new GridButton("返回",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(gbr);
		
		sql = "select decode(nvl(sum(p.yunmcs),0),0,0,ceil(75/nvl(sum(p.yunmcs),0))) ds from qicrjhcpb p where p.qicrjhb_id ="+v.getString10();
		rs = con.getResultSetList(sql);
		String defaultds = "1";
		if(rs.next()){
			defaultds = rs.getString("ds");
		}
		rs.close();
		StringBuffer dsb = new StringBuffer();
		dsb.append("function (){")
		.append("var Mrcd = gridDiv_ds.getRange();\n")
		.append("for(m = 0;m < Mrcd.length; m++){")
		.append("rec = Mrcd[m];")
		.append("rec.set('CAIYDS','").append(defaultds).append("');")
		.append("}}");
		GridButton gbd = new GridButton("设置默认值",dsb.toString());
		egu.addTbarBtn(gbd);
		
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
//	页面判定方法
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
			visit.setActivePageName(this.getPageName().toString());
			getSelectData();
		}
		
	}
}