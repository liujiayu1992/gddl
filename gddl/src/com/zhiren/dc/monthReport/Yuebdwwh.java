package com.zhiren.dc.monthReport;

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

/*2014-3-7 
 * hc
 * 直管口径维护
 * */

public class Yuebdwwh extends BasePage implements PageValidateListener {
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin\n");

		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sql.append("delete from yuebdwb where id = ").append(
					delrsl.getString("id")).append(";\n");
		}
		delrsl.close();

		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
				sql
						.append(
								"insert into yuebdwb(id, xuh,diancxxb_id, fuid, mingc, quanc, zhuangt) values(getnewid("
										+ visit.getDiancxxb_id() + "),'")
						.append(mdrsl.getString("xuh")).append("', ").append(
								mdrsl.getLong("diancxxb_id")).append(",")
						.append(mdrsl.getLong("fuid"))
						.append(",'").append(mdrsl.getString("mingc")).append(
								"','").append(mdrsl.getString("quanc")).append(
								"',").append(
								(getExtGrid().getColumn("zhuangt").combo)
										.getBeanId(mdrsl.getString("zhuangt")))
						.append(");\n");
			} else {
				sql.append("update yuebdwb set fuid = ").append(mdrsl.getLong("fuid")).append(",xuh='").append(
						mdrsl.getString("xuh")).append("',mingc='").append(
						mdrsl.getString("mingc")).append("',quanc='").append(
						mdrsl.getString("quanc")).append("',zhuangt=").append(
						(getExtGrid().getColumn("zhuangt").combo)
								.getBeanId(mdrsl.getString("zhuangt"))).append(",diancxxb_id=")
						.append(mdrsl.getLong("diancxxb_id")).append(
								" where id = ").append(mdrsl.getString("id"))
						.append(";\n");
			}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ sql.toString());
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

	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(visit.getString9());
		}
	}

	public void getSelectData(String tt) {
		JDBCcon con = new JDBCcon();
		String sql = "select distinct y.id,y.xuh,y.diancxxb_id,y.fuid fuid,\n"
				+ "y.mingc,y.quanc,decode(y.zhuangt,0,'停用','使用') zhuangt\n"
				+ "from yuebdwb y order by xuh,id";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yuebdwb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("diancxxb_id").setHeader("电厂id");
		egu.getColumn("fuid").setHeader("父级单位");
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("zhuangt").setHeader("使用状态");
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(
				egu.gridId,
				new IDropDownModel("select 1 id,'使用' zhuangt from dual\n"
						+ "union\n" + "select 0 id,'停用' zhuangt from dual"));
		egu.getColumn("zhuangt").returnId = true;
		egu.getColumn("zhuangt").setDefaultValue("使用");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'ZhigkjReport&lx=zhigkj';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("打印", "function (){" + str + "}",
						SysConstant.Btn_Icon_Print).getScript() + "}");
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData(visit.getString9());
		}
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
}
