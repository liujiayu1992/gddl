package com.zhiren.dc.jilgl.jicxx;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者:tzf
 * 时间:2009-08-25
 * 内容:对一审核发货数据 提供 修改 功能 ，对已审核的发货进行修改.
 */
/*
 * 作者:zl
 * 时间:2009-10-20
 * 内容:增加成本查询按钮
 */
public class Fahxgsh extends BasePage implements PageValidateListener {

	public static final String YUNSFS_QY = "QY";// 汽运

	public static final String YUNSFS_HY = "HY";// 火运

	public static final String YUNSFS_All = "ALL";// 全部

	public static final String FAHXGSH_MT = "MT";// 码头
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

	// 绑定日期
	public String getRiqi() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit) this.getPage().getVisit()).setString3(riqi);
	}

	public String getRiq2() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {
		((Visit) this.getPage().getVisit()).setString4(riq2);
	}

	public String getYunsfs() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setYunsfs(String yunsfs) {
		((Visit) this.getPage().getVisit()).setString1(yunsfs);
	}

	public String getKuangb() {
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setKuangb(String kuangb) {
		((Visit) this.getPage().getVisit()).setString5(kuangb);
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Fahxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		List fhlist = new ArrayList();
		while (rsl.next()) {
			Jilcz.addFahid(fhlist, rsl.getString("id"));

			if (!rsl.getString("id").equals("0"))
				MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
						.getRenymc(), SysConstant.RizOpType_UP,
						SysConstant.RizOpMokm_Fahxg, "fahb", rsl
								.getString("id"));
		}
		rsl.close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		con.Close();
		getExtGrid().Save(getChange(), visit);
		setMsg("保存成功");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _ChengbChick = false;

	public void ChengbButton(IRequestCycle cycle) {
		_ChengbChick = true;
	}

	public void submit(IRequestCycle cycle) throws ParseException {
		if (_RefreshChick) {
			_RefreshChick = false;
			if (getReportType().equals(FAHXGSH_MT)) {
				getSelectData();
			} else {
				getSelectData1();
			}
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			if (getReportType().equals(FAHXGSH_MT)) {
				getSelectData();
			} else {
				getSelectData1();
			}
		}
		if (_ChengbChick) {
			_ChengbChick = false;
			getChengb(cycle);
		}
	}

	private void getChengb(IRequestCycle cycle) {
		((Visit) this.getPage().getVisit()).setString5(this.getKuangbValue()
				.getValue());
		cycle.activate("Chengbcx");
	}

	public void getSelectData() throws ParseException {
		// 进行判断如果选定的到货日期包含已经出月报的内容，则出月报的内容不显示出来
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = ((Visit) this.getPage().getVisit()).getString3();
		String[] dat = date1.split("-");
		String a1 = dat[0];
		String a2 = dat[1];
		String strd = a1 + "-" + a2 + "-01";
		String date2 = dfm.format(new Date());
		String[] aa = date2.split("-");
		String strdate = aa[0] + "-" + aa[1] + "-01";
		Date dt1 = dfm.parse(date1);
		Date dt2 = dfm.parse(strdate);
		JDBCcon cn = new JDBCcon();
		String sql = "select * from yuetjkjb where riq=to_date('" + strd
				+ "','yyyy-MM-dd')";
		ResultSetList rs1 = cn.getResultSetList(sql);
		if (rs1.getRows() == 0) {
			// if(dt1.getTime()<dt2.getTime()){
			String b = "";
			String sql_yunsfs = "";
			if (YUNSFS_HY.equals(getYunsfs())) {
				sql_yunsfs = " and f.yunsfsb_id = " + SysConstant.YUNSFS_HUOY;
			} else if (YUNSFS_QY.equals(getYunsfs())) {
				sql_yunsfs = " and f.yunsfsb_id = " + SysConstant.YUNSFS_QIY;
			}
			JDBCcon con = new JDBCcon();
			Visit visit = ((Visit) getPage().getVisit());

			if (getKuangbValue().getId() == -1) {
				b = "";
			} else {
				b = " and f.meikxxb_id=" + getKuangbValue().getId();
			}

			StringBuffer sb = new StringBuffer();
			sb
					.append("select * from (select  distinct f.id,d.mingc as diancxxb_id,g.mingc as gongysb_id, \n");
			sb
					.append(" m.mingc as meikxxb_id,(select mingc from chezxxb c where c.id=f.faz_id) as faz_id, \n");
			sb
					.append(" j.mingc as jihkjb_id, y.mingc as yunsfsb_id, p.mingc as pinzb_id, daohrq, fahrq, \n");
			sb.append(" h.hetbh as hetb_id, \n");
			sb
					.append(" (select mingc from vwyuanshdw d where d.id=f.yuanshdwb_id) as yuanshdwb_id, \n");
			sb
					.append(" (select mingc from chezxxb c where c.id=f.daoz_id) as daoz_id, \n");
			sb
					.append(" (select mingc from chezxxb c where c.id=f.yuandz_id) as yuandz_id, \n");
			sb.append("  maoz,piz,biaoz,yuns, f.hedbz  \n");
			sb
					.append(" from fahb f,diancxxb d,gongysb g,meikxxb m,pinzb p,  \n");
			sb.append(" jihkjb j,hetb h,yunsfsb y where f.gongysb_id=g.id "
					+ Jilcz.filterDcid(visit, "f") + " \n");
			sb.append(" and f.daohrq>=to_date('" + getRiqi()
					+ "','yyyy-mm-dd') and f.daohrq<to_date('" + getRiq2()
					+ "','yyyy-mm-dd')+1 " + b);
			sb.append(" and f.diancxxb_id=d.id  \n");
			sb.append(" and f.meikxxb_id=m.id and f.pinzb_id=p.id  \n");
			sb.append(" and f.jihkjb_id=j.id and f.yunsfsb_id=y.id  \n");
			sb.append(" and f.hedbz = " + SysConstant.HEDBZ_YSH + " \n");
			sb.append(sql_yunsfs + " \n");
			sb
					.append(" and f.hetb_id=h.id(+) order by daohrq desc, f.id desc)");
			ResultSetList rsl = con.getResultSetList(sb.toString());
			if (rsl == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:"
						+ sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("fahb");
			ComboBox dc = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			egu.getColumn("diancxxb_id").returnId = true;
			dc.setEditable(true);
			String dcSql;
			if (visit.isFencb()) {
				dcSql = "select id,mingc from diancxxb where fuid="
						+ visit.getDiancxxb_id();
				egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
				egu.getColumn("diancxxb_id").setWidth(70);
			} else {
				dcSql = "select id,mingc from diancxxb where id="
						+ visit.getDiancxxb_id();
				egu.getColumn("diancxxb_id").setHidden(true);
				egu.getColumn("diancxxb_id").editor = null;
			}
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(dcSql));

			egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
			egu.getColumn("gongysb_id").setWidth(80);
			egu.getColumn("gongysb_id").setEditor(null);
			egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
			egu.getColumn("meikxxb_id").setWidth(80);
			egu.getColumn("meikxxb_id").setEditor(null);
			egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
			egu.getColumn("pinzb_id").setWidth(45);
			egu.getColumn("pinzb_id").setEditor(null);
			egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
			egu.getColumn("maoz").setWidth(45);
			egu.getColumn("maoz").setEditor(null);
			egu.getColumn("maoz").setDefaultValue("0");
			egu.getColumn("maoz").setEditor(null);
			egu.getColumn("piz").setHeader(Locale.piz_fahb);
			egu.getColumn("piz").setWidth(45);
			egu.getColumn("piz").setEditor(null);
			egu.getColumn("piz").setDefaultValue("0");
			egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
			egu.getColumn("biaoz").setWidth(45);
			egu.getColumn("biaoz").setEditor(null);
			egu.getColumn("biaoz").setDefaultValue("0");
			egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
			egu.getColumn("yuns").setWidth(45);
			egu.getColumn("yuns").setEditor(null);
			egu.getColumn("yuns").setDefaultValue("0");
			egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
			egu.getColumn("faz_id").setWidth(50);
			egu.getColumn("faz_id").setEditor(null);
			egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
			egu.getColumn("daoz_id").setWidth(50);
			egu.getColumn("daoz_id").setEditor(null);
			egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
			egu.getColumn("jihkjb_id").setWidth(60);
			egu.getColumn("jihkjb_id").setEditor(null);
			egu.getColumn("hetb_id").setHeader(Locale.hetb_id_fahb);
			egu.getColumn("hetb_id").setWidth(80);
			egu.getColumn("hetb_id").setEditor(null);
			egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
			egu.getColumn("yunsfsb_id").setWidth(40);
			egu.getColumn("yunsfsb_id").setEditor(null);
			egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
			egu.getColumn("yuandz_id").setWidth(50);
			egu.getColumn("yuandz_id").setEditor(null);
			egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
			egu.getColumn("yuanshdwb_id").setWidth(80);
			egu.getColumn("yuanshdwb_id").setEditor(null);
			egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
			egu.getColumn("daohrq").setWidth(80);
			egu.getColumn("daohrq").setEditor(null);
			egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
			egu.getColumn("fahrq").setWidth(80);
			egu.getColumn("fahrq").setEditor(null);
			egu.getColumn("hedbz").setHidden(true);
			egu.getColumn("hedbz").setDefaultValue("" + SysConstant.HEDBZ_YSH);
			egu.getColumn("hedbz").setEditor(null);

			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.addPaging(25);
			egu.setWidth(Locale.Grid_DefaultWidth);
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from diancxxb"));
			egu.getColumn("diancxxb_id").setDefaultValue(
					((Visit) getPage().getVisit()).getDiancmc());
			
			egu.getColumn("gongysb_id").setEditor(new ComboBox());
			egu.getColumn("gongysb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id, mingc from gongysb where leix=1 "));
			egu.getColumn("meikxxb_id").setEditor(new ComboBox());
			egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from meikxxb"));
			egu.getColumn("pinzb_id").setEditor(new ComboBox());
			egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from pinzb"));
			egu.getColumn("faz_id").setEditor(new ComboBox());
			egu.getColumn("faz_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from chezxxb"));
			egu.getColumn("daoz_id").setEditor(new ComboBox());
			egu.getColumn("daoz_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from chezxxb"));
			egu.getColumn("jihkjb_id").setEditor(new ComboBox());
			egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from jihkjb"));
			egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
			egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from yunsfsb"));
			egu.getColumn("yuandz_id").setEditor(new ComboBox());
			egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from chezxxb"));
			egu.getColumn("yuanshdwb_id").setEditor(new ComboBox());
			egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, mingc from vwyuanshdw"));
			egu.getColumn("hetb_id").setEditor(new ComboBox());
			egu.getColumn("hetb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id, hetbh from hetb"));

			// 到货日期查询
			egu.addTbarText("到货日期:");
			DateField df = new DateField();
			df.setReadOnly(true);
			df.setValue(this.getRiqi());
			df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
			df.setId("riqi");
			egu.addToolbarItem(df.getScript());
			egu.addTbarText("至:");
			DateField df1 = new DateField();
			df1.setReadOnly(true);
			df1.setValue(this.getRiq2());
			df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
			df1.setId("riq2");
			egu.addToolbarItem(df1.getScript());
			egu.addTbarText("-");

			egu.addTbarText("矿别:");
			ComboBox kuangb = new ComboBox();
			kuangb.setTransform("KuangbSelect");
			// kuangb.setValue(this.getKuangbValue().getValue());
			kuangb.setWidth(150);
			df1.setId("kuangb");
			kuangb
					.setListeners("select:function(own,rec,index){Ext.getDom('KuangbSelect').selectedIndex=index}");
			egu.addToolbarItem(kuangb.getScript());

			egu.addTbarText("-");
			GridButton gbt = new GridButton("刷新",
					"function(){document.getElementById('RefreshButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
			egu.addTbarBtn(gbt);

			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

			GridButton cb = new GridButton("成本",
					"function(){document.getElementById('ChengbButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
			egu.addTbarBtn(cb);
			Checkbox cbdhth = new Checkbox();
			cbdhth.setChecked(false);
			cbdhth.setId("duohth");
			egu.addToolbarItem(cbdhth.getScript());
			egu.addTbarText("多行替换");
			egu
					.addOtherScript("gridDiv_grid.on('afteredit',function(e){			\n"
							+ "	if(!duohth.checked){											\n"
							+ "		return;														\n"
							+ "	}																\n"
							+ "	for(i=e.row; i<gridDiv_ds.getCount(); i++){						\n"
							+ "		if(e.field=='MAOZ'||e.field=='PIZ'||e.field=='BIAOZ'||e.field=='YUNS'){	\n"
							+ "			return;													\n"
							+ "		};															\n"
							+ "		gridDiv_ds.getAt(i).set(e.field,e.value);					\n"
							+ "	}																\n" + "});																\n");
			egu.addTbarText("-");
			setExtGrid(egu);
			con.Close();
		} else {
			String b = "";
			String sql_yunsfs = "";
			if (YUNSFS_HY.equals(getYunsfs())) {
				sql_yunsfs = " and f.yunsfsb_id = " + SysConstant.YUNSFS_HUOY;
			} else if (YUNSFS_QY.equals(getYunsfs())) {
				sql_yunsfs = " and f.yunsfsb_id = " + SysConstant.YUNSFS_QIY;
			}
			JDBCcon con = new JDBCcon();
			Visit visit = ((Visit) getPage().getVisit());

			if (getKuangbValue().getId() == -1) {
				b = "";
			} else {
				b = " and f.meikxxb_id=" + getKuangbValue().getId();
			}

			StringBuffer sb = new StringBuffer();
			sb
					.append("select * from (select  distinct f.id,d.mingc as diancxxb_id,g.mingc as gongysb_id, \n");
			sb
					.append(" m.mingc as meikxxb_id,(select mingc from chezxxb c where c.id=f.faz_id) as faz_id, \n");
			sb
					.append(" j.mingc as jihkjb_id, y.mingc as yunsfsb_id, p.mingc as pinzb_id, daohrq, fahrq, \n");
			sb.append(" h.hetbh as hetb_id, \n");
			sb
					.append(" (select mingc from vwyuanshdw d where d.id=f.yuanshdwb_id) as yuanshdwb_id, \n");
			sb
					.append(" (select mingc from chezxxb c where c.id=f.daoz_id) as daoz_id, \n");
			sb
					.append(" (select mingc from chezxxb c where c.id=f.yuandz_id) as yuandz_id, \n");
			sb.append("  maoz,piz,biaoz,yuns, f.hedbz  \n");
			sb
					.append(" from fahb f,diancxxb d,gongysb g,meikxxb m,pinzb p,  \n");
			sb.append(" jihkjb j,hetb h,yunsfsb y where f.gongysb_id=g.id "
					+ Jilcz.filterDcid(visit, "f") + " \n");
			sb.append(" and f.daohrq>=to_date('" + strdate
					+ "','yyyy-mm-dd') and f.daohrq<to_date('" + getRiq2()
					+ "','yyyy-mm-dd')+1 " + b);
			sb.append(" and f.diancxxb_id=d.id  \n");
			sb.append(" and f.meikxxb_id=m.id and f.pinzb_id=p.id  \n");
			sb.append(" and f.jihkjb_id=j.id and f.yunsfsb_id=y.id  \n");
			sb.append(" and f.hedbz = " + SysConstant.HEDBZ_YSH + " \n");
			sb.append(sql_yunsfs + " \n");
			sb
					.append(" and f.hetb_id=h.id(+) order by daohrq desc, f.id desc)");
			ResultSetList rsl = con.getResultSetList(sb.toString());
			if (rsl == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:"
						+ sb);
				setMsg(ErrorMessage.NullResult);
				return;
			}
			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("fahb");
			ComboBox dc = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			egu.getColumn("diancxxb_id").returnId = true;
			dc.setEditable(true);
			String dcSql;
			if (visit.isFencb()) {
				dcSql = "select id,mingc from diancxxb where fuid="
						+ visit.getDiancxxb_id();
				egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
				egu.getColumn("diancxxb_id").setWidth(70);
			} else {
				dcSql = "select id,mingc from diancxxb where id="
						+ visit.getDiancxxb_id();
				egu.getColumn("diancxxb_id").setHidden(true);
				egu.getColumn("diancxxb_id").editor = null;
			}
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(dcSql));

			egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
			egu.getColumn("gongysb_id").setWidth(80);
			egu.getColumn("gongysb_id").setEditor(null);
			egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
			egu.getColumn("meikxxb_id").setWidth(80);
			egu.getColumn("meikxxb_id").setEditor(null);
			egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
			egu.getColumn("pinzb_id").setWidth(45);
			egu.getColumn("pinzb_id").setEditor(null);
			egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
			egu.getColumn("maoz").setWidth(45);
			egu.getColumn("maoz").setEditor(null);
			egu.getColumn("maoz").setDefaultValue("0");
			egu.getColumn("maoz").setEditor(null);
			egu.getColumn("piz").setHeader(Locale.piz_fahb);
			egu.getColumn("piz").setWidth(45);
			egu.getColumn("piz").setEditor(null);
			egu.getColumn("piz").setDefaultValue("0");
			egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
			egu.getColumn("biaoz").setWidth(45);
			egu.getColumn("biaoz").setEditor(null);
			egu.getColumn("biaoz").setDefaultValue("0");
			egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
			egu.getColumn("yuns").setWidth(45);
			egu.getColumn("yuns").setEditor(null);
			egu.getColumn("yuns").setDefaultValue("0");
			egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
			egu.getColumn("faz_id").setWidth(50);
			egu.getColumn("faz_id").setEditor(null);
			egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
			egu.getColumn("daoz_id").setWidth(50);
			egu.getColumn("daoz_id").setEditor(null);
			egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
			egu.getColumn("jihkjb_id").setWidth(60);
			egu.getColumn("jihkjb_id").setEditor(null);
			egu.getColumn("hetb_id").setHeader(Locale.hetb_id_fahb);
			egu.getColumn("hetb_id").setWidth(80);
			egu.getColumn("hetb_id").setEditor(null);
			egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
			egu.getColumn("yunsfsb_id").setWidth(40);
			egu.getColumn("yunsfsb_id").setEditor(null);
			egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
			egu.getColumn("yuandz_id").setWidth(50);
			egu.getColumn("yuandz_id").setEditor(null);
			egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
			egu.getColumn("yuanshdwb_id").setWidth(80);
			egu.getColumn("yuanshdwb_id").setEditor(null);
			egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
			egu.getColumn("daohrq").setWidth(80);
			egu.getColumn("daohrq").setEditor(null);
			egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
			egu.getColumn("fahrq").setWidth(80);
			egu.getColumn("fahrq").setEditor(null);
			egu.getColumn("hedbz").setHidden(true);
			egu.getColumn("hedbz").setDefaultValue("" + SysConstant.HEDBZ_YSH);
			egu.getColumn("hedbz").setEditor(null);

			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.addPaging(25);
			egu.setWidth(Locale.Grid_DefaultWidth);
//			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
//					new IDropDownModel("select id, mingc from diancxxb"));
//			egu.getColumn("diancxxb_id").setDefaultValue(
//					((Visit) getPage().getVisit()).getDiancmc());
			// 到货日期查询
			egu.addTbarText("到货日期:");
			DateField df = new DateField();
			df.setReadOnly(true);
			df.setValue(this.getRiqi());
			df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
			df.setId("riqi");
			egu.addToolbarItem(df.getScript());
			egu.addTbarText("至:");
			DateField df1 = new DateField();
			df1.setReadOnly(true);
			df1.setValue(this.getRiq2());
			df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
			df1.setId("riq2");
			egu.addToolbarItem(df1.getScript());
			egu.addTbarText("-");

			egu.addTbarText("矿别:");
			ComboBox kuangb = new ComboBox();
			kuangb.setTransform("KuangbSelect");
			// kuangb.setValue(this.getKuangbValue().getValue());
			kuangb.setWidth(150);
			df1.setId("kuangb");
			kuangb
					.setListeners("select:function(own,rec,index){Ext.getDom('KuangbSelect').selectedIndex=index}");
			egu.addToolbarItem(kuangb.getScript());

			egu.addTbarText("-");
			GridButton gbt = new GridButton("刷新",
					"function(){document.getElementById('RefreshButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
			egu.addTbarBtn(gbt);

			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

			GridButton cb = new GridButton("成本",
					"function(){document.getElementById('ChengbButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
			egu.addTbarBtn(cb);
			Checkbox cbdhth = new Checkbox();
			cbdhth.setChecked(false);
			cbdhth.setId("duohth");
			egu.addToolbarItem(cbdhth.getScript());
			egu.addTbarText("多行替换");
			egu
					.addOtherScript("gridDiv_grid.on('afteredit',function(e){			\n"
							+ "	if(!duohth.checked){											\n"
							+ "		return;														\n"
							+ "	}																\n"
							+ "	for(i=e.row; i<gridDiv_ds.getCount(); i++){						\n"
							+ "		if(e.field=='MAOZ'||e.field=='PIZ'||e.field=='BIAOZ'||e.field=='YUNS'){	\n"
							+ "			return;													\n"
							+ "		};															\n"
							+ "		gridDiv_ds.getAt(i).set(e.field,e.value);					\n"
							+ "	}																\n" + "});																\n");
			egu.addTbarText("-");
			setExtGrid(egu);
			con.Close();
			setMsg("已出月报的内容无法显示出来");
		}
	}

	public void getSelectData1() {
		String b = "";
		String sql_yunsfs = "";
		if (YUNSFS_HY.equals(getYunsfs())) {
			sql_yunsfs = " and f.yunsfsb_id = " + SysConstant.YUNSFS_HUOY;
		} else if (YUNSFS_QY.equals(getYunsfs())) {
			sql_yunsfs = " and f.yunsfsb_id = " + SysConstant.YUNSFS_QIY;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());

		if (getKuangbValue().getId() == -1) {
			b = "";
		} else {
			b = " and f.meikxxb_id=" + getKuangbValue().getId();
		}

		StringBuffer sb = new StringBuffer();
		sb
				.append("select * from (select  distinct f.id,d.mingc as diancxxb_id,g.mingc as gongysb_id, \n");
		sb
				.append(" m.mingc as meikxxb_id,(select mingc from chezxxb c where c.id=f.faz_id) as faz_id, \n");
		sb
				.append(" j.mingc as jihkjb_id, y.mingc as yunsfsb_id, p.mingc as pinzb_id, daohrq, fahrq, \n");
		sb.append(" h.hetbh as hetb_id, \n");
		sb
				.append(" (select mingc from vwyuanshdw d where d.id=f.yuanshdwb_id) as yuanshdwb_id, \n");
		sb
				.append(" (select mingc from chezxxb c where c.id=f.daoz_id) as daoz_id, \n");
		sb
				.append(" (select mingc from chezxxb c where c.id=f.yuandz_id) as yuandz_id, \n");
		sb.append("  maoz,piz,biaoz,yuns, f.hedbz  \n");
		sb.append(" from fahb f,diancxxb d,gongysb g,meikxxb m,pinzb p,  \n");
		sb.append(" jihkjb j,hetb h,yunsfsb y where f.gongysb_id=g.id "
				+ Jilcz.filterDcid(visit, "f") + " \n");
		sb.append(" and f.daohrq>=to_date('" + getRiqi()
				+ "','yyyy-mm-dd') and f.daohrq<to_date('" + getRiq2()
				+ "','yyyy-mm-dd')+1 " + b);
		sb.append(" and f.diancxxb_id=d.id  \n");
		sb.append(" and f.meikxxb_id=m.id and f.pinzb_id=p.id  \n");
		sb.append(" and f.jihkjb_id=j.id and f.yunsfsb_id=y.id  \n");
		sb.append(" and f.hedbz = " + SysConstant.HEDBZ_YSH + " \n");
		sb.append(sql_yunsfs + " \n");
		sb.append(" and f.hetb_id=h.id(+) order by daohrq desc, f.id desc)");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fahb");
		ComboBox dc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		egu.getColumn("diancxxb_id").returnId = true;
		dc.setEditable(true);
		String dcSql;
		if (visit.isFencb()) {
			dcSql = "select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		} else {
			dcSql = "select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(dcSql));

		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(80);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(45);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
		egu.getColumn("maoz").setWidth(45);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_fahb);
		egu.getColumn("piz").setWidth(45);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(45);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setDefaultValue("0");
		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(45);
		egu.getColumn("yuns").setEditor(null);
		egu.getColumn("yuns").setDefaultValue("0");
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(50);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(50);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("hetb_id").setHeader(Locale.hetb_id_fahb);
		egu.getColumn("hetb_id").setWidth(80);
		egu.getColumn("hetb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
		egu.getColumn("yunsfsb_id").setWidth(40);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(50);
		egu.getColumn("yuandz_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(80);
		egu.getColumn("yuanshdwb_id").setEditor(null);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("hedbz").setHidden(true);
		egu.getColumn("hedbz").setDefaultValue("" + SysConstant.HEDBZ_YSH);
		egu.getColumn("hedbz").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from diancxxb"));
		egu.getColumn("diancxxb_id").setDefaultValue(
				((Visit) getPage().getVisit()).getDiancmc());
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id, mingc from gongysb where leix=1 "));
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meikxxb"));
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from pinzb"));
		egu.getColumn("faz_id").setEditor(new ComboBox());
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("daoz_id").setEditor(new ComboBox());
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from jihkjb"));
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb"));
		egu.getColumn("yuandz_id").setEditor(new ComboBox());
		egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("yuanshdwb_id").setEditor(new ComboBox());
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from vwyuanshdw"));
		egu.getColumn("hetb_id").setEditor(new ComboBox());
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, hetbh from hetb"));
		egu.getColumn("hetb_id").editor.setAllowBlank(true) ;

		// 到货日期查询
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");

		egu.addTbarText("矿别:");
		ComboBox kuangb = new ComboBox();
		kuangb.setTransform("KuangbSelect");
		// kuangb.setValue(this.getKuangbValue().getValue());
		kuangb.setWidth(150);
		df1.setId("kuangb");
		kuangb
				.setListeners("select:function(own,rec,index){Ext.getDom('KuangbSelect').selectedIndex=index}");
		egu.addToolbarItem(kuangb.getScript());

		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		GridButton cb = new GridButton("成本",
				"function(){document.getElementById('ChengbButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(cb);
		Checkbox cbdhth = new Checkbox();
		cbdhth.setChecked(false);
		cbdhth.setId("duohth");
		egu.addToolbarItem(cbdhth.getScript());
		egu.addTbarText("多行替换");
		egu
				.addOtherScript("gridDiv_grid.on('afteredit',function(e){			\n"
						+ "	if(!duohth.checked){											\n"
						+ "		return;														\n"
						+ "	}																\n"
						+ "	for(i=e.row; i<gridDiv_ds.getCount(); i++){						\n"
						+ "		if(e.field=='MAOZ'||e.field=='PIZ'||e.field=='BIAOZ'||e.field=='YUNS'){	\n"
						+ "			return;													\n" + "		};															\n"
						+ "		gridDiv_ds.getAt(i).set(e.field,e.value);					\n"
						+ "	}																\n" + "});																\n");
		egu.addTbarText("-");
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

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	// 煤矿下拉框
	public IDropDownBean getKuangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getKuangbModel().getOptionCount() > 0) {
				setKuangbValue((IDropDownBean) getKuangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setKuangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getKuangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setKuangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setKuangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void setKuangbModels() {
		String cheh = "";
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();

		sb
				.append("select distinct m.id,m.mingc from fahb f,meikxxb m where f.meikxxb_id=m.id ");
		sb.append("order by m.id \n");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1, "请选择"));
		setKuangbModel(new IDropDownModel(list, sb));
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			setYunsfs(reportType);
			visit.setString1(cycle.getRequestContext().getParameter("lx"));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if (visit.getActivePageName().toString().equals("Chengbcx")) {

			} else {
				visit.setActivePageName(getPageName().toString());
				if (reportType == null) {
					setYunsfs(YUNSFS_All);
				}
				visit.setList1(null);
				setRiqi(DateUtil.FormatDate(new Date()));
				setRiq2(DateUtil.FormatDate(new Date()));
				setKuangbModels();
				if(getReportType().equals(FAHXGSH_MT)){
				try {
					getSelectData();
				} catch (ParseException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				}else{
					getSelectData1();
				}
			}
		}
	}
	//

}
