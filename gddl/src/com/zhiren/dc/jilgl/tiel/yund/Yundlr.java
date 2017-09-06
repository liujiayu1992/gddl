package com.zhiren.dc.jilgl.tiel.yund;

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
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * 
 * @author Rock
 * 
 */

/*
 * 2010-3-30 王刚 修改自动车次下拉框取数方法。
 * 
 */

/*
 * 2009-02-16 王磊 修改列显示位置，将计划口径与品种的位置互换。
 */

/*
 * 2009-12-29 王刚 修改车皮号保存时过滤空格符。
 */

/*
 * 2010-1-14 王刚 修改自动车次下拉框SQL，修改后避免不同到货日期同一采样日期造成使用同一采样编码的问题
 * 
 */

/*
 * 作者：张森涛 时间：2010-01-19 描述：增加煤矿是否有合同进煤控制
 */
public class Yundlr extends BasePage implements PageValidateListener {
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
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yundlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}

		StringBuffer sb = new StringBuffer();
		// 插入车皮临时表
		sb.append("begin\n");
		double biaoz = 0.0;
		int ches = 0;
		ches = rsl.getRows();
		while (rsl.next()) {

			int flg = Jilcz.Hetkzjm(con, visit.getDiancxxb_id(),
					((IDropDownModel) getMeikModel()).getBeanId(rsl
							.getString("meikxxb_id")), rsl.getString("fahrq"),
					SysConstant.YUNSFS_HUOY);
			if (flg == -1) {
				con.rollBack();
				con.Close();
				// WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
				setMsg("该煤矿单位没有对应的合同，请输入合同后重新录入!");
				return;
			}
			long diancxxb_id = 0;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
						.getBeanId(rsl.getString("diancxxb_id"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			biaoz += rsl.getDouble("biaoz");
			sb
					.append("insert into cheplsb\n")
					.append(
							"(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, yunsfsb_id, chec,cheph, biaoz, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, daozch, lury, beiz,caiyrq,xiecfsb_id)\n")
					.append("values (getnewid(").append(diancxxb_id).append(
							"),").append(diancxxb_id).append(",").append(
							((IDropDownModel) getGongysModel()).getBeanId(rsl
									.getString("gongysb_id"))).append(",")
					.append(
							((IDropDownModel) getMeikModel()).getBeanId(rsl
									.getString("meikxxb_id"))).append(",")
					.append(
							(getExtGrid().getColumn("pinzb_id").combo)
									.getBeanId(rsl.getString("pinzb_id")))
					.append(",").append(
							((IDropDownModel) getChezModel()).getBeanId(rsl
									.getString("faz_id"))).append(",").append(
							(getExtGrid().getColumn("daoz_id").combo)
									.getBeanId(rsl.getString("daoz_id")))
					.append(",").append(
							(getExtGrid().getColumn("jihkjb_id").combo)
									.getBeanId(rsl.getString("jihkjb_id")))
					.append(",").append(
							DateUtil.FormatOracleDate(rsl.getString("fahrq")))
					.append(",")
					.append(DateUtil.FormatOracleDate("2050-12-31"))
					.append(",").append(SysConstant.YUNSFS_HUOY).append(",'")
					.append(rsl.getString("chec")).append("','").append(
							rsl.getString("cheph").trim()).append("'").append(
							",").append(rsl.getDouble("biaoz")).append(",")
					.append(
							(getExtGrid().getColumn("chebb_id").combo)
									.getBeanId(rsl.getString("chebb_id")))
					.append(",").append(
							(getExtGrid().getColumn("yuandz_id").combo)
									.getBeanId(rsl.getString("yuandz_id")))
					.append(",").append(
							(getExtGrid().getColumn("yuanshdwb_id").combo)
									.getBeanId(rsl.getString("yuanshdwb_id")))
					.append(",'").append(rsl.getString("yuanmkdw")).append(
							"','").append(rsl.getString("daozch"))
					.append("','").append(visit.getRenymc()).append("','")
					.append(rsl.getString("beiz")).append("'").append(",")
					.append(DateUtil.FormatOracleDate(rsl.getString("caiyrq")))
					.append(",").append(
							getExtGrid().getColumn("xiecfsb_id").combo
									.getBeanId(rsl.getString("xiecfsb_id")))
					.append(");\n");
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			setMsg(ErrorMessage.Yundlr001);
			return;
		}
		flag = Jilcz.Updatezlid(con, visit.getDiancxxb_id(),
				SysConstant.YUNSFS_HUOY, null);
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Yundlr002);
			setMsg(ErrorMessage.Yundlr002);
			return;
		}
		flag = Jilcz.INSorUpfahb(con, visit.getDiancxxb_id());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Yundlr003);
			setMsg(ErrorMessage.Yundlr003);
			return;
		}
		flag = Jilcz.InsChepb(con, visit.getDiancxxb_id(),
				SysConstant.YUNSFS_HUOY, SysConstant.HEDBZ_TJ);
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Yundlr004);
			setMsg(ErrorMessage.Yundlr004);
			return;
		}
		sb.delete(0, sb.length());
		sb.append("select distinct fahb_id from cheplsb");
		rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Yundlr005);
			setMsg(ErrorMessage.Yundlr005);
			return;
		}
		while (rsl.next()) {
			flag = Jilcz.updateLieid(con, rsl.getString("fahb_id"));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Yundlr006);
				setMsg(ErrorMessage.Yundlr006);
				return;
			}
		}
		con.commit();
		con.Close();
		setMsg("您保存了 " + ches + " 车的信息,共计票重 " + biaoz + " 吨。");
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select c.id, c.diancxxb_id, c.cheph, c.biaoz, '' gongysb_id, '' meikxxb_id, \n")
				.append(
						"'' as faz_id, '' as jihkjb_id, '' as pinzb_id, c.fahrq as fahrq, \n")
				.append("c.caiyrq as caiyrq, c.chec, '' as chebb_id,")
				.append(
						"(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id")
				.append(", '' as daoz_id, \n")
				.append(
						"'' as yuandz_id, '' as yuanshdwb_id, c.yuanmkdw, c.daozch ")

				.append(", c.beiz \n").append("from cheplsb c \n");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("cheplsb");
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// 设置页面高度
		// egu.setHeight(570);
		// 设置每页显示行数
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		if (visit.isFencb()) {
			ComboBox dc = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql = "select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		} else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
			// egu.getColumn("diancxxb_id").setDefaultValue(defaultvalue)
		}
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("cheph").editor
				.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(60);

		StringBuffer lins = new StringBuffer();
		lins
				.append("specialkey:function(own,e){ \n")
				.append("if(row+1 == gridDiv_grid.getStore().getCount()){ \n")
				.append("Ext.MessageBox.alert('提示信息','已到达数据末尾！');return; \n")
				.append("} \n")
				.append("row = row+1; \n")
				.append(
						"last = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("gridDiv_grid.getSelectionModel().selectRow(row); \n")
				.append(
						"cur = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("copylastrec(last,cur); \n").append(
						"gridDiv_grid.startEditing(row , 3); }");

		egu.getColumn("biaoz").editor.setListeners(lins.toString());
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("caiyrq").setHeader(Locale.caiyrq_caiyb);
		egu.getColumn("caiyrq").setWidth(70);
		egu.getColumn("caiyrq").setEditor(null);
		egu.getColumn("caiyrq").setHidden(true);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yuanmkdw").setWidth(90);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("xiecfsb_id").setHeader("卸车方式");
		/*
		 * // 设置供应商下拉框 ComboBox c1 = new ComboBox();
		 * egu.getColumn("gongysb_id").setEditor(c1); c1.setEditable(true);
		 * String gysSql = "select id,mingc from gongysb order by mingc";
		 * egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new
		 * IDropDownModel(gysSql)); // 设置煤矿单位下拉框 ComboBox c2 = new ComboBox();
		 * egu.getColumn("meikxxb_id").setEditor(c2); c2.setEditable(true);
		 * c2.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		 * String mkSql = "select id,mingc from meikxxb order by mingc";
		 * egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new
		 * IDropDownModel(mkSql)); // 设置发站下拉框 ComboBox c3 = new ComboBox();
		 * egu.getColumn("faz_id").setEditor(c3); c3.setEditable(true); String
		 * FazSql = "select id,mingc from chezxxb order by mingc";
		 * egu.getColumn("faz_id").setComboEditor(egu.gridId,new
		 * IDropDownModel(FazSql));
		 */// 设置默认到站
		egu.getColumn("daoz_id").setDefaultValue(visit.getDaoz());
		// 设置发货日期和到货日期的默认值
		String riq = DateUtil.FormatDate(new Date());
		egu.getColumn("fahrq").setDefaultValue(riq);
		egu.getColumn("caiyrq").setDefaultValue(riq);
		// 设置到站下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c4);
		c4.setEditable(true);
		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		// 设置品种下拉框
		ComboBox c5 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);
		String pinzSql = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		// 设置口径下拉框
		ComboBox c6 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c6);
		c6.setEditable(true);
		String jihkjSql = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
		// 设置车别下拉框
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "路车"));
		ls.add(new IDropDownBean(2, "自备车"));
		ComboBox c7 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c7);
		c7.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ls));
		egu.getColumn("chebb_id").setDefaultValue("路车");
		// 设置原到站下拉框
		ComboBox c8 = new ComboBox();
		egu.getColumn("yuandz_id").setEditor(c8);
		c8.setEditable(true);
		String YuandzSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
				new IDropDownModel(YuandzSql));
		egu.getColumn("yuandz_id").setDefaultValue(visit.getDaoz());
		// 设置原收货单位下拉框
		ComboBox c9 = new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c9);
		c9.setEditable(true);// 设置可输入
		String Sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue("" + visit.getDiancmc());
		// 设置卸车方式下拉框
		ComboBox c11 = new ComboBox();
		c11.setTransform("xiecfsSe");
		Visit v = (Visit) this.getPage().getVisit();
		String XiecfsSql = "select id,mingc from xiecfsb where diancxxb_id = "
				+ v.getDiancxxb_id() + " order by id";
		c11.setEditable(true);
		egu.getColumn("xiecfsb_id").setEditor(c11);
		egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(XiecfsSql));
		// 设置车次下拉框
		String sql = "select * from xitxxb where diancxxb_id = "
				+ visit.getDiancxxb_id()
				+ " and mingc ='火车车次自动计算' and leib= '数量' and zhi = '是'";
		ResultSetList rs = con.getResultSetList(sql);
		if (rs.next()) {
			ComboBox c10 = new ComboBox();
			egu.getColumn("chec").setEditor(c10);
			c10.setEditable(true);
			String time = "sysdate";
			String ChecSql = "select chec\n"
					+ "  from (Select distinct substr(chec, 1, 3) chec\n"
					+ "          From fahb f, chepb c\n"
					+ "         Where f.id = c.fahb_id\n"
					+ "           and f.yunsfsb_id = "
					+ SysConstant.YUNSFS_HUOY
					+ "\n"
					+ "           and f.diancxxb_id = "
					+ visit.getDiancxxb_id()
					+ "\n"
					+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
					+ "               to_date(to_char("+time+", 'yyyy-mm-dd'), 'yyyy-mm-dd')\n"
					+ "         order by (case chec\n"
					+ "                    when '星期一' then\n"
					+ "                     1\n"
					+ "                    when '星期二' then\n"
					+ "                     2\n"
					+ "                    when '星期三' then\n"
					+ "                     3\n"
					+ "                    when '星期四' then\n"
					+ "                     4\n"
					+ "                    when '星期五' then\n"
					+ "                     5\n"
					+ "                    when '星期六' then\n"
					+ "                     6\n"
					+ "                    when '星期日' then\n"
					+ "                     7\n"
					+ "                  end) desc)\n" + " where rownum < 2";
			String strSQL = Jilcz.getChecSql(ChecSql, visit.getDiancxxb_id(),time);
			egu.getColumn("chec").setComboEditor(egu.gridId,
					new IDropDownModel(strSQL));
			egu.getColumn("chec").setDefaultValue(
					""
							+ ((IDropDownBean) (egu.getColumn("chec").combo
									.getOption(0))).getValue());
			egu.getColumn("chec").setReturnId(false);
		}
		// 设置页面按钮
		egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		// egu.addToolbarButton(GridButton.ButtonType_Copy, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu
				.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
						+ "row = irow; \n"
						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
						+ "gongysTree_window.show();}});\n");
		// egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){alert();if(e.Field=='MEIKXXB_ID'){e.record.set('YUANMKDW',e.value)}});\n");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz_kj,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				.append(
						"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append(
						"rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("if(cks.getDepth() == 4){ \n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.parentNode.text);\n")
				.append(
						"rec.set('MEIKXXB_ID', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('YUANMKDW', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('FAZ_ID', cks.parentNode.text);rec.set('JIHKJB_ID', cks.text);\n")
				.append("}else if(cks.getDepth() == 3){\n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
				.append("rec.set('YUANMKDW', cks.parentNode.text);\n").append(
						"rec.set('FAZ_ID', cks.text);\n").append(
						"}else if(cks.getDepth() == 2){\n").append(
						"rec.set('GONGYSB_ID', cks.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.text);\n").append(
						"rec.set('YUANMKDW', cks.text);\n").append(
						"}else if(cks.getDepth() == 1){\n").append(
						"rec.set('GONGYSB_ID', cks.text); }\n").append(
						"gongysTree_window.hide();\n").append("return;")
				.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	//	
	public IPropertySelectionModel getXiecfsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setXiecfsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();

	}

	public void setXiecfsModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void setXiecfsModels() {
		Visit v = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from xiecfsb where diancxxb_id= "
				+ v.getDiancxxb_id() + " order by id";
		setXiecfsModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDefaultTree(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			setTbmsg(null);
			getSelectData();
		}
	}
}