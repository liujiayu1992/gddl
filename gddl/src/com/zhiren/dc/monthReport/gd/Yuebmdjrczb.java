package com.zhiren.dc.monthReport.gd;

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

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuebmdjrczb extends BasePage implements PageValidateListener {
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}

		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();			
		}
		getSelectData();
	}

	public void DelData() {
		String diancxxb_id = this.getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		String strSql = "delete from yuebmdjrczb where riq=" + CurrODate
				+ " and diancxxb_id=" + diancxxb_id;

		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + strSql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}

	public void CreateData() {
		long diancxxb_id = Long.parseLong(this.getTreeid());
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrSDate = getNianf() + "-" + getYuef();
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		int intYuef = Integer.parseInt(getYuef());
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		String strSql = "";
		int flag = 0;
		long lngId = 0;
		
		con.setAutoCommit(false);
		
		// 删除已生成的数据
		strSql = "delete from yuebmdjrczb where to_char(riq,'yyyy-mm')='" + CurrSDate + 
				"' and diancxxb_id=" + diancxxb_id;
		flag = con.getDelete(strSql);

		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "引发错误SQL:" + strSql);
			con.Close();
			return;
		}
		strSql = 
			"SELECT TJ.RIQ,\n" +
			"       TJ.GONGYSB_ID,\n" + 
			"       TJ.JIHKJB_ID,\n" + 
			"       --TJ.PINZB_ID,\n" + 
			"       TJ.YUNSFSB_ID,\n" + 
			"       TJ.FENX,\n" + 
			"       SUM(SL.JINGZ) AS SHUL,\n" + 
			"       SUM(SL.BIAOZ) AS BIAOZ,\n" + 
			"       SUM(SL.JINGZ) AS YANS,\n" + 
			"       SUM(SL.JIANJL) AS JIANJL,\n" + 
			"       SUM(SL.JINGZ) AS JIANZL,\n" + 
			"       DECODE(SUM(JINGZ),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              ROUND_NEW(SUM(JINGZ * (ZL.QNET_AR * 1000)) / SUM(JINGZ), 0)) AS REZ,\n" + 
			"       DECODE(SUM(JINGZ),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              ROUND_NEW(SUM(JINGZ * ZL.MT) / SUM(JINGZ), 1)) AS SHOUDJQS,\n" + 
			"       DECODE(SUM(JINGZ),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              ROUND_NEW(SUM(JINGZ * ZL.AAD) / SUM(JINGZ), 2)) AS KONGGJHF,\n" + 
			"       0 AS MEIJ,\n" + 
			"       0 AS YUNF,\n" + 
			"       0 AS YUNZF,\n" + 
			"       0 AS BIAOMDJ\n" + 
			"  FROM YUESLB SL,\n" + 
			"       YUEZLB ZL,\n" + 
			"       (SELECT ID,\n" + 
			"               RIQ,\n" + 
			"               DIANCXXB_ID,\n" + 
			"               GONGYSB_ID,\n" + 
			"               JIHKJB_ID,\n" + 
			"               PINZB_ID,\n" + 
			"               YUNSFSB_ID,\n" + 
			"               NVL('本月', '') AS FENX\n" + 
			"          FROM YUETJKJB\n" + 
			"         WHERE TO_CHAR(RIQ, 'yyyy-mm') = '" + CurrSDate + "') TJ\n" + 
			" WHERE TJ.ID = SL.YUETJKJB_ID(+)\n" + 
			"   AND TJ.FENX = SL.FENX(+)\n" + 
			"   AND TJ.ID = ZL.YUETJKJB_ID(+)\n" + 
			"   AND TJ.FENX = ZL.FENX(+)\n" + 
			"   AND TJ.DIANCXXB_ID = " + diancxxb_id + "\n" + 
			" GROUP BY TJ.RIQ, TJ.GONGYSB_ID, TJ.JIHKJB_ID, TJ.YUNSFSB_ID, TJ.FENX\n" + 
			" ORDER BY GONGYSB_ID, TJ.JIHKJB_ID, TJ.YUNSFSB_ID, TJ.FENX";

		ResultSetList rsl = con.getResultSetList(strSql);

		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:"
					+ strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}

		while (rsl.next()) {
			lngId = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
			strSql = "insert into yuebmdjrczb values(\n" + lngId + ",\n"
					+ CurrODate + ",\n'" + rsl.getString("fenx") + "',\n"
					+ diancxxb_id + ",\n"
					+ rsl.getLong("gongysb_id") + ",\n"
					+ rsl.getLong("jihkjb_id") + ",\n"
					+ rsl.getLong("yunsfsb_id") + ",\n"
					+ rsl.getDouble("shul") + ",\n" 
					+ rsl.getDouble("biaoz") + ",\n" 
					+ rsl.getDouble("yans") + ",\n"
					+ rsl.getDouble("jianjl") + ",\n" 
					+ rsl.getDouble("jianzl") + ",\n" 
					+ rsl.getDouble("meij") + ",\n"
					+ rsl.getDouble("yunf") + ",\n" 
					+ rsl.getDouble("yunzf") + ",\n" 
					+ "0" + ",\n" 
					+ "0" + ",\n" 
					+ rsl.getDouble("rez") + ",\n"
					+ rsl.getDouble("shoudjqs") + ",\n" 
					+ rsl.getDouble("konggjhf") + ",\n" 
					+ rsl.getDouble("buhsbmdj") + "\n" 
					+ ")";

			flag = con.getInsert(strSql);
			if (flag == -1) {
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ strSql);
				setMsg("生成过程出现错误！月入厂标煤单价累计插入失败！");
				con.rollBack();
				con.Close();
				return;
			}
			
//			更新累计数
			strSql=
				"SELECT * \n" +
				"  FROM YUEBMDJRCZB\n" + 
				" WHERE RIQ = " + LastODate + "\n" +
				"  AND FENX = '累计'" + "\n" +
				"  AND GONGYSB_ID=" + rsl.getString("gongysb_id") + 
				"  AND JIHKJB_ID =" + rsl.getString("jihkjb_id") + "\n" + 
				"  AND YUNSFSB_ID =" + rsl.getString("yunsfsb_id") + "\n" + 
				"  AND DIANCXXB_ID =" + diancxxb_id;
			ResultSetList rsL = con.getResultSetList(strSql);
			if (rsL == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
				con.rollBack();
				con.Close();
				return;
			}
			long gongysb_id = 0;
			long jihkjb_id = 0;
			long yunsfsb_id = 0;
			double shul = 0;
			double biaoz = 0;
			double yans = 0;
			double jianjl = 0;
			double jianzl = 0;
			double rez = 0;
			double shoudjqs = 0;
			double konggjhf = 0;
			double meij = 0;
			double yunf = 0;
			double yunzf = 0;
			double shicjgyx = 0;
			double suopl = 0;
			double biaomdj = 0;
			
			if (rsL.next() && intYuef!=1) {
				gongysb_id = rsL.getLong("gongysb_id");
				jihkjb_id = rsL.getLong("jihkjb_id");
				yunsfsb_id = rsL.getLong("yunsfsb_id");
				shul = rsL.getDouble("shul") + rsl.getDouble("shul") ;
				biaoz = rsL.getDouble("biaoz")+ rsl.getDouble("biaoz") ;
				yans = rsL.getDouble("yans")+ rsl.getDouble("yans") ;
				jianjl = rsL.getDouble("jianjl") + rsl.getDouble("jianjl") ;
				jianzl = rsL.getDouble("jianzl") + rsl.getDouble("jianzl") ;
				
				if (shul > 0) {
					rez = CustomMaths.Round_new(
							CustomMaths.div(rsL.getDouble("rez")*rsL.getDouble("shul")+rsl.getDouble("rez")*rsl.getDouble("shul"),
							shul), 2);
					shoudjqs = CustomMaths.Round_new(
							CustomMaths.div(rsL.getDouble("shoudjqs")*rsL.getDouble("shul")+rsl.getDouble("shoudjqs")*rsl.getDouble("shul"),
							shul), 1);
					konggjhf = CustomMaths.Round_new(
							CustomMaths.div(rsL.getDouble("konggjhf")*rsL.getDouble("shul")+rsl.getDouble("konggjhf")*rsl.getDouble("shul"),
							shul), 2);
					meij = CustomMaths.Round_new(
							CustomMaths.div(rsL.getDouble("meij")*rsL.getDouble("shul")+rsl.getDouble("meij")*rsl.getDouble("shul"),
							shul), 3);
					yunf = CustomMaths.Round_new(
							CustomMaths.div(rsL.getDouble("yunf")*rsL.getDouble("shul")+rsl.getDouble("yunf")*rsl.getDouble("shul"),
							shul), 2);
					yunzf = CustomMaths.Round_new(
							CustomMaths.div(rsL.getDouble("yunzf")*rsL.getDouble("shul")+rsl.getDouble("yunzf")*rsl.getDouble("shul"),
							shul), 2);
					shicjgyx = CustomMaths.Round_new(
							(rsL.getDouble("shicjgyx")*rsL.getDouble("shul")+rsl.getDouble("shicjgyx")*rsl.getDouble("shul"))/shul, 2);
				}
				suopl = rsL.getDouble("suopl") + rsl.getDouble("suopl");
				if (rez > 0) {
					biaomdj = CustomMaths.Round_new((meij + yunf + yunzf) * 29271 / rez, 2);
				}
				
			} else {
				gongysb_id = rsl.getLong("gongysb_id");
				jihkjb_id = rsl.getLong("jihkjb_id");
				yunsfsb_id = rsl.getLong("yunsfsb_id");
				shul = rsl.getDouble("shul");
				biaoz = rsl.getDouble("biaoz");
				yans = rsl.getDouble("yans");
				jianjl = rsl.getDouble("jianjl");
				jianzl = rsl.getDouble("jianzl");
				rez = rsl.getDouble("rez");
				shoudjqs = rsl.getDouble("shoudjqs");
				konggjhf = rsl.getDouble("konggjhf");
				meij = rsl.getDouble("meij");
				yunf = rsl.getDouble("yunf");
				yunzf = rsl.getDouble("yunzf");
				shicjgyx = rsl.getDouble("shicjgyx");
				suopl = rsl.getDouble("suopl");
				biaomdj = rsl.getDouble("biaomdj");
			}
			
			flag = con.getUpdate(insertLjYuebmdjrczb(gongysb_id, jihkjb_id, yunsfsb_id, 
					diancxxb_id, shul, biaoz, yans, jianjl, jianzl, rez, shoudjqs, konggjhf,
					meij, yunf, yunzf, shicjgyx, suopl, biaomdj, CurrODate));
			if (flag == -1) {
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ strSql);
				setMsg("生成过程出现错误！月入厂标煤单价累计插入失败！");
				con.rollBack();
				con.Close();
				return;
			}
		}

		rsl.close();
		con.commit();
		con.Close();
		setMsg(CurrZnDate + "的数据成功生成昇！");
	}
	
	private String insertLjYuebmdjrczb(long gongysb_id, long jihkjb_id, long yunsfsb_id,
			long diancxxb_id, double shul, double biaoz, double yans, double jianjl, 
			double jianzl, double rez, double shoudjqs, double konggjhf, double meij, 
			double yunf, double yunzf,double shicjgyx, double suopl,  double biaomdj, 
			String CurrODate) {
		
		long lngId = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
		String SQL = 
			"INSERT INTO YUEBMDJRCZB VALUES(\n" +
			lngId + ",\n" +
			CurrODate + ",\n" +
			"'累计',\n" +
			diancxxb_id + ",\n" +
			gongysb_id + ",\n" +
			jihkjb_id + ",\n" +
			yunsfsb_id + ",\n" +
			shul + ",\n" +
			biaoz + ",\n" +
			yans + ",\n" +
			jianjl + ",\n" +
			jianzl + ",\n" +
			meij  + ",\n" +
			yunf  + ",\n" +
			yunzf  + ",\n" +
			shicjgyx + ",\n" +
			suopl + ",\n" +
			rez + ",\n" +
			shoudjqs + ",\n" +
			konggjhf  + ",\n" +
			biaomdj  + "\n" +
			")";
		return SQL;
	}

	public void getSelectData() {
		Visit visit = (Visit)getPage().getVisit();
		long diancxxb_id = Long.parseLong(this.getTreeid());
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		JDBCcon con = new JDBCcon();

		String strSql = 
			"select * from yueslb s, yuetjkjb k where s.yuetjkjb_id = k.id and k.riq = "
			+ CurrODate + " and k.diancxxb_id=" + diancxxb_id;
		boolean isLocked = !con.getHasIt(strSql);
		boolean isDisable = false;
		if (visit.isFencb() && diancxxb_id == visit.getDiancxxb_id()) {
			isDisable = true;
			isLocked = true;
		}
		if (isLocked && !isDisable) {
			setMsg("请在使用本模块之前，首先完成月数量数据的计算！");
		}

		strSql =
			"SELECT RC.ID,\n" +
			"       G.MINGC AS GONGYSB_ID,\n" + 
			"       J.MINGC AS JIHKJB_ID,\n" + 
			"       YS.MINGC AS YUNSFSB_ID,\n" + 
			"       FENX,\n" + 
			"       SHUL,\n" + 
			"       BIAOZ,\n" + 
			"       YANS,\n" + 
			"       JIANJL,\n" + 
			"       JIANZL,\n" + 
			"       MEIJ,\n" + 
			"       YUNF,\n" + 
			"       YUNZF,\n" + 
			"       SHICJGYX,\n" + 
			"       SUOPL,\n" + 
			"       REZ,\n" + 
			"       SHOUDJQS,\n" + 
			"       KONGGJHF,\n" + 
			"       BIAOMDJ\n" + 
			"  FROM YUEBMDJRCZB RC, GONGYSB G, JIHKJB J, YUNSFSB YS\n" + 
			" WHERE RC.GONGYSB_ID = G.ID\n" + 
			"   AND RC.JIHKJB_ID = J.ID\n" + 
			"   AND RC.YUNSFSB_ID = YS.ID\n" + 
			"   AND RIQ = " + CurrODate + "\n" + 
			"   AND RC.DIANCXXB_ID = " + diancxxb_id + "\n" +			
			" ORDER BY GONGYSB_ID, FENX";

		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuebmdjrczb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(60);
		// egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		// egu.getColumn("pinzb_id").setWidth(40);
		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
		egu.getColumn("fenx").setWidth(40);
		egu.getColumn("shul").setCenterHeader("数量");
		egu.getColumn("shul").setWidth(60);
		egu.getColumn("biaoz").setCenterHeader("票重");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yans").setCenterHeader("验收");
		egu.getColumn("yans").setWidth(60);
		egu.getColumn("jianjl").setCenterHeader("检斤量");
		egu.getColumn("jianjl").setWidth(60);
		egu.getColumn("jianzl").setCenterHeader("检值量");
		egu.getColumn("jianzl").setWidth(60);
		egu.getColumn("meij").setCenterHeader("煤价<br>不含税");
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("yunf").setCenterHeader("运费<br>不含税");
		egu.getColumn("yunf").setWidth(60);
		egu.getColumn("yunzf").setCenterHeader("运杂费");
		egu.getColumn("yunzf").setWidth(60);
		egu.getColumn("shicjgyx").setCenterHeader("市场价格<br>影响");
		egu.getColumn("shicjgyx").setWidth(80);
		egu.getColumn("suopl").setCenterHeader("亏吨拒付<br>索赔量");
		egu.getColumn("suopl").setWidth(80);
		egu.getColumn("rez").setCenterHeader("发热量<br>(千焦/千克)");
		egu.getColumn("rez").setWidth(80);
		egu.getColumn("shoudjqs").setCenterHeader("收到基全水");
		egu.getColumn("shoudjqs").setWidth(90);
		egu.getColumn("konggjhf").setCenterHeader("空干基灰分");
		egu.getColumn("konggjhf").setWidth(90);
		egu.getColumn("biaomdj").setCenterHeader("标煤单价<br>不含税");
		egu.getColumn("biaomdj").setWidth(90);

		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setEditor(null);
		// egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("biaomdj").setEditor(null);
		egu.setDefaultsortable(false);
		
		((NumberField) egu.getColumn("shicjgyx").editor).setDecimalPrecision(2);

		egu.getColumn("gongysb_id").update = false;
		egu.getColumn("jihkjb_id").update = false;
		// egu.getColumn("pinzb_id").update=false;
		egu.getColumn("yunsfsb_id").update = false;
		egu.getColumn("fenx").update = false;

		egu.addTbarText("年份");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("月份");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		// 判断数据是否被锁定
		// boolean isLocked = isLocked(con);
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月标煤单价入厂数据,请稍候！'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		// 生成按钮
		GridButton gbc = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		gbc.setDisabled(isLocked);

		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		// 删除按钮
		GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
		gbd.setDisabled(isLocked);

		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		// 保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		gbs.setDisabled(isLocked);

		egu.addTbarBtn(gbs);
		// 打印按钮
		String openScript =
			"var openUrl = 'http://'+document.location.host+document.location.pathname; " +
			"var end = openUrl.indexOf(';');" +
			"openUrl = openUrl.substring(0,end);" +
			"openUrl = openUrl + '?service=page/GdMonthReport&lx=yuebmdjrczb';" +
			"window.open(openUrl ,'newWin','resizable=1');";
		GridButton gbp = new GridButton("打印", "function (){" + openScript + "}");
//		gbp.setDisabled(isDisable);
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setChecked(false);
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("累计数据可编辑");
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit', function(e){afteredit(gridDiv_ds, e);});\n");
		strSql = 
			"SELECT G.MINGC AS GONGYSMC, J.MINGC AS JIHKJMC, Y.MINGC AS YUNSFSMC, DJ.*\n" +
			"  FROM YUEBMDJRCZB DJ, GONGYSB G, JIHKJB J, YUNSFSB Y\n" + 
			" WHERE DJ.GONGYSB_ID = G.ID\n" + 
			"   AND DJ.JIHKJB_ID = J.ID\n" + 
			"   AND DJ.YUNSFSB_ID = Y.ID\n" + 
			"   AND DJ.FENX = '累计'\n" + 
			"   AND DJ.RIQ = " + LastODate + "\n" + 
			"   AND DJ.DIANCXXB_ID = " + diancxxb_id;

		ResultSetList rs = con.getResultSetList(strSql);
		sb.append("varRecord = new Array();\n");
		int i = 0;
		while (rs.next()) {		
			sb.append("  varOneRecord = new Array();\n");
			sb.append("  varOneRecord[v_Gongys]='" + rs.getString("gongysmc")+ "';\n");
			sb.append("  varOneRecord[v_Jihkj]='" + rs.getString("jihkjmc")+ "';\n");
			sb.append("  varOneRecord[v_Yunsfs]='" + rs.getString("yunsfsmc")+ "';\n");
			sb.append("  varOneRecord[v_Meij]=" + rs.getDouble("meij")+ ";\n");
			sb.append("  varOneRecord[v_Yunf]=" + rs.getDouble("yunf")+ ";\n");
			sb.append("  varOneRecord[v_Yunzf]=" + rs.getDouble("yunzf")+ ";\n");
			sb.append("  varOneRecord[v_Shicjgyx]=" + rs.getDouble("shicjgyx")+ ";\n");
			sb.append("  varOneRecord[v_Rez]=" + rs.getDouble("rez")+ ";\n");
			sb.append("  varRecord[" + i + "] = varOneRecord;\n");
			i++;
		}
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("beforeedit(gridDiv_ds,e,SelectLike);");// 当"累计"时,这一行不允许编辑
		sb.append("});");

		egu.addOtherScript(sb.toString());

		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "年" + getYuef() + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			setRiq();
			getSelectData();
		}
	}

	public boolean isLocked(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		return con.getHasIt("select * from yueshchjb where riq=" + CurrODate);
	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}

	// 年份下拉框
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}
