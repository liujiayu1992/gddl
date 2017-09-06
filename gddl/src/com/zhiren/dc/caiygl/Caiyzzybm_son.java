package com.zhiren.dc.caiygl;

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

public class Caiyzzybm_son extends BasePage implements PageValidateListener {
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
		
		String Sql=
			"select distinct c.zhillsb_id as id,c.caiybm, z.zhiybm\n" +
			"  from (select bianm as caiybm, zhillsb_id\n" + 
			"          from zhuanmb\n" + 
			"         where zhillsb_id in\n" + 
			"               (select zm.zhillsb_id as id\n" + 
			"                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
			"                 where zm.zhuanmlb_id = lb.id\n" + 
			"                   and lb.jib = 1\n" + 
			"                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
			"                   and f.zhilb_id = z.zhilb_id\n" + 
			"                   and z.id = zm.zhillsb_id)\n" + 
			"           and zhuanmlb_id =\n" + 
			"               (select id from zhuanmlb where mingc = '采样编码') and bianm like '%%') c,\n" + 
			"       (select bianm as zhiybm, zhillsb_id\n" + 
			"          from zhuanmb\n" + 
			"         where zhillsb_id in\n" + 
			"               (select zm.zhillsb_id as id\n" + 
			"                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
			"                 where zm.zhuanmlb_id = lb.id\n" + 
			"                   and lb.jib = 2\n" + 
			"                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
			"                   and f.zhilb_id = z.zhilb_id\n" + 
			"                   and z.id = zm.zhillsb_id)\n" + 
			"           and zhuanmlb_id =\n" + 
			"               (select id from zhuanmlb where mingc = '制样编码')) z,\n" + 
			"       (select distinct f.id, f.diancxxb_id, z.id as zid\n" + 
			"          from zhillsb z, fahb f, chepb c\n" + 
			"         where f.zhilb_id = z.zhilb_id\n" + 
			"           and c.fahb_id = f.id) s\n" + 
			" where c.zhillsb_id = z.zhillsb_id\n" + 
			"   and z.zhillsb_id = s.zid\n" + 
			"   and c.zhillsb_id = s.zid\n" + 
			"  and c.caiybm='"+tiaoj+"'\n" + 
			"  order by caiybm,zhiybm";


		ResultSetList rsl = con.getResultSetList(Sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("caiybm").setHeader("采样编码");
		egu.getColumn("caiybm").setWidth(90);
		egu.getColumn("caiybm").setEditor(null);
		egu.getColumn("zhiybm").setHeader("制样编码");
		egu.getColumn("zhiybm").setWidth(90);
		egu.getColumn("zhiybm").setEditor(null);
		egu.addPaging(100);
		
		
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
