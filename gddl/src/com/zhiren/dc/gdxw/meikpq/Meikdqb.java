package com.zhiren.dc.gdxw.meikpq;

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

public class Meikdqb extends BasePage implements PageValidateListener {
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

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
    
public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		
		while (delrsl.next()) {
			
			
			sbsql.append("delete from meikdqb_xw where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			
			String  meikpqb_id = "(select max(id) from meikpqb where mingc='"+mdrsl.getString("meikpqb_id")+"')";
			
		    String xuh=mdrsl.getString("xuh");
		    String piny=mdrsl.getString("piny");
		    String beiz=mdrsl.getString("beiz");
		    String meikdqmc=mdrsl.getString("meikdqmc");
		   
			if ("".equals(mdrsl.getString("id"))||mdrsl.getString("id")==null ||mdrsl.getInt("id")==0) {
				sbsql.append("insert into meikdqb_xw(id, xuh,piny, meikdqmc, beiz,meikpqb_id) values( xl_xul_id.nextval")
				.append(",").append(xuh).append(", '").append(piny).append("','").append(meikdqmc).append("', '").append(beiz).
				append("', ").append(meikpqb_id).append(");\n");
				
			} else {
				sbsql.append("update meikdqb_xw set xuh =").append(xuh).append(", ").append("piny='").append(piny).append("',").append("meikdqmc='").
				     append(meikdqmc).append("',").append("beiz='").append(beiz).append("',")
					.append("meikpqb_id = ").append(meikpqb_id).append(" where id = ").append(mdrsl.getString("id")).append(";\n");
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
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save();
			
		}
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		getSelectData();
	}

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql=
			"select m.id, m.xuh,m.meikdqmc,m.piny ,k.piny||'-'||k.mingc as meikpqb_id,m.beiz  from meikdqb_xw m, meikpqb k where m.meikpqb_id =k.id(+) order by xuh";

		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("meikdqb_xw");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(70);
		egu.getColumn("meikdqmc").setHeader("煤矿地区名称");
		egu.getColumn("meikdqmc").setWidth(150);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(250);
		ComboBox cb= new ComboBox();
		egu.getColumn("meikpqb_id").setHeader("煤矿片区");
		egu.getColumn("meikpqb_id").setEditor(cb);
		cb.setEditable(true);
		egu.getColumn("meikpqb_id").setComboEditor(egu.gridId,
				new IDropDownModel(" select id,piny|| '-' || mingc  from meikpqb  order by xuh"));
		egu.getColumn("meikpqb_id").setReturnId(true);
		
		
		egu.addPaging(0);
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
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
