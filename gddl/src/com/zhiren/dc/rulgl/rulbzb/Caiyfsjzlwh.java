package com.zhiren.dc.rulgl.rulbzb;

import java.util.ArrayList;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 类名：采样方式及重量维护
 */

public class Caiyfsjzlwh extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riq; // 分析日期
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from caiyfsjzl where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if ("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into caiyfsjzl(id, diancxxb_id, fenxrq, rulbzb_id, jizfzb_id, caiyfs, zongyzl) values(getnewid(")
				.append(getTreeid()).append("), ").append(getTreeid()).append(", to_date('")
				.append(getRiq()).append("', 'yyyy-mm-dd'), ")
				.append((getExtGrid().getColumn("rulbzb_id").combo).getBeanId(mdrsl.getString("rulbzb_id"))).append(", ")
				.append((getExtGrid().getColumn("jizfzb_id").combo).getBeanId(mdrsl.getString("jizfzb_id"))).append(", '")
				.append(mdrsl.getString("caiyfs")).append("', ")
				.append(getSqlValue(mdrsl.getString("zongyzl"))).append(");\n");
			} else {
				sbsql.append("update caiyfsjzl set ")
				.append(" rulbzb_id = ").append((getExtGrid().getColumn("rulbzb_id").combo).getBeanId(mdrsl.getString("rulbzb_id")))
				.append(", jizfzb_id = ").append((getExtGrid().getColumn("jizfzb_id").combo).getBeanId(mdrsl.getString("jizfzb_id")))
				.append(", caiyfs = '").append(mdrsl.getString("caiyfs"))
				.append("', zongyzl = ").append(getSqlValue(mdrsl.getString("zongyzl")))
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
			"select cy.id, bz.mingc rulbzb_id, jzfz.mingc jizfzb_id, cy.caiyfs, cy.zongyzl\n" +
			"  from caiyfsjzl cy, rulbzb bz, jizfzb jzfz\n" + 
			" where cy.fenxrq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and cy.diancxxb_id = "+ getTreeid() +"\n" + 
			"   and cy.rulbzb_id = bz.id\n" + 
			"   and cy.jizfzb_id = jzfz.id" +
			" order by bz.xuh, jzfz.xuh";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caiyfsjzl");

		egu.getColumn("rulbzb_id").setHeader("班组");
		egu.getColumn("jizfzb_id").setHeader("机组分组");
		egu.getColumn("caiyfs").setHeader("采样方式");
		egu.getColumn("zongyzl").setHeader("总样重量(kg)");
		
		egu.getColumn("rulbzb_id").setEditor(new ComboBox());
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from rulbzb where diancxxb_id = "+ getTreeid() +" order by xuh"));
		
		egu.getColumn("jizfzb_id").setEditor(new ComboBox());
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from jizfzb where diancxxb_id = "+ getTreeid() +" order by xuh"));
		
		ArrayList list = new ArrayList();
		list.add(new IDropDownBean(1, "机械"));
		list.add(new IDropDownBean(2, "人工"));
		egu.getColumn("caiyfs").setEditor(new ComboBox());
		egu.getColumn("caiyfs").setComboEditor(egu.gridId, new IDropDownModel(list));
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		egu.addTbarText("分析日期：");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
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
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为空或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
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
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
			this.setTreeid(null);
			setRiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}

}
