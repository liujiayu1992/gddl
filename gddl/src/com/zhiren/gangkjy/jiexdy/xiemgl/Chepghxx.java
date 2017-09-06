package com.zhiren.gangkjy.jiexdy.xiemgl;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Chepghxx extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			cycle.activate("Xiemxx");
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "select\n" + "cp.id,cp.xuh,cheph,cb.mingc as mingc ,cp.maoz,cp.piz,cp.biaoz,cp.ches,cp.beiz\n"
				+ "from chepb cp,chebb cb where cp.fahb_id = " + visit.getString10() +"and cp.chebb_id=cb.id";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 新建grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置grid可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置数据不分页
		egu.addPaging(0);
		// 设置grid为单选
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置grid列标题
		egu.getColumn("xuh").setHeader(Local.xuh);
		egu.getColumn("cheph").setHeader(Local.cheph);

		egu.getColumn("mingc").setEditor(new ComboBox());
		egu.getColumn("mingc").setHeader("类型");
		egu.getColumn("mingc").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from chebb order by mingc"));
		egu.getColumn("mingc").editor.setAllowBlank(false);
		egu.getColumn("mingc").setReturnId(true);
		egu.getColumn("maoz").setHeader(Local.maoz);
		egu.getColumn("piz").setHeader(Local.piz);
		egu.getColumn("biaoz").setHeader(Local.biaoz);
		egu.getColumn("ches").setHeader(Local.chesd);
		egu.getColumn("beiz").setHeader(Local.beiz);
		//		设置列宽度
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("mingc").setWidth(60);
		egu.getColumn("cheph").setWidth(80);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("beiz").setWidth(200);

		// 添加按钮
		GridButton insert = new GridButton(GridButton.ButtonType_Inserts,
				"gridDiv", egu.getGridColumns(), "");
		egu.addTbarBtn(insert);
		// 删除按钮
		GridButton delete = new GridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "");
		egu.addTbarBtn(delete);
		// 保存按钮
		GridButton save = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(save);

		//		 详细过衡按钮
		GridButton Return = new GridButton("返回", "ReturnXiemxx");
		Return.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(Return);

		setExtGrid(egu);
		con.Close();

	}

	private void save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有未提交改动！");
			return;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String sql;
		int flag;
		// 删除数据
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		sql = "begin \n";
		while (rs.next()) {
			sql += "delete from chepb where id =" + rs.getString("id") + ";\n";
		}
		sql += "end;\n";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.DeleteDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		// 修改数据
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		while (rs.next()) {
			long id = rs.getLong("id");
			String fahb_id = visit.getString10();
			int xuh = rs.getInt("xuh");
			String cheph = rs.getString("cheph");
			String chebb_id = getExtGrid().getValueSql(getExtGrid().getColumn("mingc"),
					rs.getString("mingc"));
			double maoz = rs.getDouble("maoz");
			double piz = rs.getDouble("piz");
			double biaoz = rs.getDouble("biaoz");
			double jingz = CustomMaths.sub(maoz, piz);
			double yunsl = getYunsl(fahb_id);
			double yingd = 0.0;
			double kuid = 0.0;
			double yingk = CustomMaths.sub(jingz, biaoz);
			double yuns = CustomMaths.mul(biaoz, yunsl);
			// 计算盈亏运损
			if (yingk >= 0) {
				yuns = 0;
				yingd = yingk;
				kuid = 0;
			} else {
				if (Math.abs(yingk) <= yuns) {
					yuns = Math.abs(yingk);
					yingd = 0;
					kuid = 0;
				} else {
					// Yuns = Yuns;
					yingd = 0;
					kuid = CustomMaths.sub(Math.abs(yingk), yuns);
				}
			}
			yingk = yingd - kuid;
			double chesd = rs.getDouble("ches");
			String beiz = rs.getString("beiz");
			if (id == 0) {
				sql += "insert into chepb(id,fahb_id,chebb_id,xuh,cheph,maoz,piz,"
						+ "biaoz,yingk,yingd,yuns,ches,lury,beiz) values(getnewid("
						+ visit.getDiancxxb_id() + ")," + fahb_id + ",'"+ chebb_id +"'," + xuh
						+ ",'" + cheph + "'," + maoz + "," + piz + "," + biaoz
						+ "," + yingk + "," + yingd + "," + yuns + "," + chesd
						+ ",'" + visit.getRenymc() + "','" + beiz + "');\n";
			} else {
				sql += "update chepb set \n" + " xuh = " + xuh + ",\n"
						+ " cheph = '" + cheph + "',\n"+ "chebb_id ='"+ chebb_id +"',\n " 
						+" maoz = " + maoz
						+ ",\n" + " piz = " + piz + ",\n" + " biaoz = " + biaoz
						+ ",\n" + " yingk = " + yingk + ",\n" + " yingd = "
						+ yingd + ",\n" + " yuns = " + yuns + ",\n"
						+ " ches = " + chesd + ",\n" + " lury = '"
						+ visit.getRenymc() + "',\n" + " beiz = '" + beiz
						+ "'" + " where id=" + id + ";\n";
			}

		}
		sql += "end;";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	public static double getYunsl(String fahb_id) {
		JDBCcon con = new JDBCcon();
		String sql = "select yunsl from fahb where id=" + fahb_id;
		ResultSetList rsl;
		double yunsl = 0.012;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			yunsl = rsl.getDouble("yunsl");
		}
		return yunsl;
	}

	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			init();
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