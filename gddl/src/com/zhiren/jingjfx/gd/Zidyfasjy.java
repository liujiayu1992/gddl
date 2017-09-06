package com.zhiren.jingjfx.gd;



import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：李琛基
 * 时间:2010年11月5日
 * 描述：增加zidyfasjy表的维护页面，作用是关联zidyfa和zidysjy表。
 */
public class Zidyfasjy extends BasePage implements PageValidateListener {
	public List gridColumns;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		StringBuffer sql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sql.append("delete from zidyfasjy where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
				if("0".equals(mdrsl.getString("ID"))){
					sql.append("insert into zidyfasjy(id, zidyfa_id, zidysjy_id) values(getnewid(").append(v.getDiancxxb_id())
						.append("), ").append(getExtGrid().getColumn("zidyfa_id").combo.getBeanId(mdrsl.getString("zidyfa_id"))).append(", ")
						.append(getExtGrid().getColumn("zidysjy_id").combo.getBeanId(mdrsl.getString("zidysjy_id")))
						.append(");\n");
				}else{
					sql.append("update zidyfasjy set zidyfa_id = ")
						.append(getExtGrid().getColumn("zidyfa_id").combo.getBeanId(mdrsl.getString("zidyfa_id")))
						.append(",zidysjy_id=").append(getExtGrid().getColumn("zidysjy_id").combo.getBeanId(mdrsl.getString("zidysjy_id")))
						.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
				}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"+ sql.toString());
			setMsg(ErrorMessage.UpdateDatabaseFail);
		} else {
			setMsg("保存成功！");
		}
		mdrsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}else if(_RefreshChick){
			_RefreshChick=false;
		}
		getSelectData();
		
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		String sql=
			"select a.id, b.z_name zidyfa_id, c.z_name zidysjy_id\n" +
			"  from zidyfasjy a, zidyfa b, zidysjy c\n" + 
			" where a.zidyfa_id = b.id(+)\n" + 
			"   and a.zidysjy_id = c.id(+) order by b.z_name";

		ResultSetList rs = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rs);
		egu.setTableName("zidyfasjy");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("zidyfa_id").setHeader("方案名称");
		egu.getColumn("zidyfa_id").setWidth(200);
		egu.getColumn("zidysjy_id").setHeader("数据源名称");
		egu.getColumn("zidysjy_id").setWidth(200);
		
		
//		ComboBox c = new ComboBox();
//		c.setEditable(false);
//		egu.getColumn("meikxxb_id").setEditor(c);
//		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select distinct id,mingc from meikxxb "
//						+ " where id = " + visit.getString1() 
//						+ " order by mingc"));
//		egu.getColumn("meikxxb_id").setReturnId(true);
		
		egu.getColumn("zidyfa_id").setEditor(new ComboBox());
		egu.getColumn("zidyfa_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,z_name from zidyfa order by z_name"));
		egu.getColumn("zidyfa_id").returnId=true;
		
		egu.getColumn("zidysjy_id").setEditor(new ComboBox());
		egu.getColumn("zidysjy_id").setComboEditor(egu.gridId, new IDropDownModel("select id,z_name from zidysjy order by z_name"));
		egu.getColumn("zidysjy_id").returnId=true;

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
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
			//visit.setList1(null);
			setExtGrid(null);
			getSelectData();
		}
	}

}



