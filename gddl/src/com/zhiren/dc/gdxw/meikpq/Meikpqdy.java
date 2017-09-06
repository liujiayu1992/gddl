package com.zhiren.dc.gdxw.meikpq;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/* 作者:王总兵
 * 日期:2010-7-14 10:59:06
 * 描述:煤矿井口与煤矿地区对应
 * 
 * 
 * 
 * 
 */
public class Meikpqdy extends BasePage implements PageValidateListener {
	
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
			
			String meikxxb_id = "(select max(id) from meikxxb where mingc='"+delrsl.getString("meikxxb_id")+"')";
			sbsql.append("delete from meikdqglb where meikxxb_id = ").append(meikxxb_id).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			
			String  meikxxb_id = "(select max(id) from meikxxb where mingc='"+mdrsl.getString("meikxxb_id")+"')";
			String meikdqb_xw_id= "(select max(id) from meikdqb_xw where meikdqmc='"+mdrsl.getString("meikdqb_xw_id")+"')";
		
			
			if ("".equals(mdrsl.getString("id"))||mdrsl.getString("id")==null) {
				sbsql.append("insert into meikdqglb(id,  meikxxb_id, meikdqb_xw_id) values( xl_xul_id.nextval")
				.append(", ").append(meikxxb_id).append(", ").append(meikdqb_xw_id).append(");\n");
			} else {
				sbsql.append("update meikdqglb set meikxxb_id = ").append(meikxxb_id).append(", ")
					.append("meikdqb_xw_id = ").append(meikdqb_xw_id).append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql = "select gl.id,mk.mingc as meikxxb_id,dq.piny||'-'||dq.meikdqmc as meikdqb_xw_id\n" +
			"from meikxxb mk,meikdqb_xw dq,meikdqglb gl\n" + 
			"where mk.id=gl.meikxxb_id(+)\n" + 
			"and gl.meikdqb_xw_id=dq.id(+) order by mk.mingc";



		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿(井口)");
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("meikxxb_id").setEditor(null);
		
		
		egu.getColumn("meikdqb_xw_id").setHeader("煤矿地区");
		
	    ComboBox cmk= new ComboBox();
		egu.getColumn("meikdqb_xw_id").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql="select m.id,m.piny||'-'||m.meikdqmc from meikdqb_xw m order by xuh";
		egu.getColumn("meikdqb_xw_id").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql));
		egu.getColumn("meikdqb_xw_id").setWidth(150);
		
		
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为空或是空串，那么向数据库保存0
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
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
		
		}
		getSelectData();
	}

}
