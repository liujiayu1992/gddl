package com.zhiren.dc.gdxw.jicxx;

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
/*
 * 作者:王总兵
 * 描述:设置每个煤矿的扣吨率,为汽车衡回皮时控制每个煤矿的扣吨率而设置
 * 
 * 
 */
public class Meikkdlsz extends BasePage implements PageValidateListener {
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
		//Visit visit = (Visit) this.getPage().getVisit();
		//visit.getExtGrid1().Save(getChange(), visit);
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		
		
		StringBuffer sbsqlDel = new StringBuffer("begin\n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsqlDel.append("delete from meikkdlsz where id = ").append(delrsl.getString("id")).append(";\n");
		}
		sbsqlDel.append("end;");
		if(sbsqlDel.length()>15){
			con.getUpdate(sbsqlDel.toString());
		}
		
		delrsl.close();
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into meikkdlsz(id, meikxxb_id,meikmc,lursj, koudl,qiyrq,lurry, beiz) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append("(select nvl(max(id),0) from meikxxb where mingc='"+mdrsl.getString("meikmc")+"')")
				.append(",'").append(mdrsl.getString("meikmc")).append("', ")
				.append("sysdate").append(", ").append(mdrsl.getDouble("koudl"))
				.append(",to_date('").append(mdrsl.getString("qiyrq")).append("','yyyy-mm-dd')")
				.append(",'").append(""+visit.getRenymc()+"")
				.append("', '").append(mdrsl.getString("beiz")).append("');\n");
			} else {
				sbsql.append("update meikkdlsz set ")
				.append("meikxxb_id = ").append("(select nvl(max(id),0) from meikxxb where mingc='"+mdrsl.getString("meikmc")+"')")
				.append(",meikmc='").append(mdrsl.getString("meikmc"))
				.append("', lursj = ").append("sysdate,")
				.append(" lurry = '").append(mdrsl.getString("lurry"))
				.append("', koudl = ").append(mdrsl.getString("koudl"))
				.append(", beiz = '").append(mdrsl.getString("beiz"))
				.append("', qiyrq = to_date('").append(mdrsl.getString("qiyrq")).append("','yyyy-mm-dd')")
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		if(sbsql.length()>15){
			con.getUpdate(sbsql.toString());
		}
		
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
			Save();
			
		}
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		getSelectData();
	}

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="select rg.id, rg.meikmc,rg.koudl,rg.qiyrq,rg.lurry,to_char(rg.lursj,'yyyy-mm-dd hh24:mi:ss') as lursj,rg.beiz\n" +
			"from meikkdlsz rg,meikxxb mk\n" + 
			"where rg.meikxxb_id=mk.id(+)\n" + 
			"order by rg.id";


		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meikkdlsz");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
	
		egu.getColumn("meikmc").setHeader("煤矿名称");
		egu.getColumn("meikmc").setWidth(150);
		egu.getColumn("koudl").setHeader("扣吨率");
		egu.getColumn("koudl").setWidth(80);
		egu.getColumn("qiyrq").setHeader("启用日期");
		egu.getColumn("qiyrq").setWidth(80);
		egu.getColumn("lurry").setHeader("录入员");
		egu.getColumn("lurry").setWidth(90);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setHidden(true);
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setWidth(150);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(150);
		

//		设置煤矿下拉框
		ComboBox cmk= new ComboBox();
		egu.getColumn("meikmc").setEditor(cmk); cmk.setEditable(true);
		String mkSql="select id,piny || '-' ||mingc  as mingc  from meikxxb union select 0 ,'全部' as mingc  from  dual";
		egu.getColumn("meikmc").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql));
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
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
			getSelectData();
		}
	}
}
