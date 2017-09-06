package com.zhiren.jt.zdt.monthreport.yuesyhcqkb;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuesyhcqkb extends BasePage implements PageValidateListener {
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
		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		// --------------------------------
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1()
				.getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");

		while (drsl.next()) {
			sql_delete.append("delete from ").append("yuesyhcqkb").append(
					" where id =").append(drsl.getLong("id")).append(";\n");
			sql_delete
					.append("delete from ")
					.append("yuesyhcqkb")
					.append(" where id =")
					.append(
							"(select id from yuesyhcqkb where diancxxb_id=(select id from diancxxb dc where dc.mingc='"
									+ drsl.getString("diancxxb_id")
									+ "') and riq=to_date('"
									+ intyear
									+ "-"
									+ StrMonth
									+ "-01','yyyy-mm-dd') and fenx='累计')")
					.append(";\n");
		}
		sql_delete.append("end;");
//		System.out.println(sql_delete.length());
		if(sql_delete.length()>11){
		con.getUpdate(sql_delete.toString());
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		String diancxxb_id = "";
		StringBuffer sql = new StringBuffer();

		while (rsl.next()) {
			sql.delete(0, sql.length());
			sql.append("begin \n");
			long id = 0;
			// long yuemtgyqkb_id= 0;
			diancxxb_id = rsl.getString("diancxxb_id");

			if ("0".equals(rsl.getString("ID"))) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
						.getVisit()).getDiancxxb_id()));
				sql
						.append("insert into yuesyhcqkb("
								+ "id,riq,diancxxb_id,shiygyl,rucsyfrl,rucpjdj,quanbhyyyl,fadhyl,gongrhyl,qithyl,chucshl,shijkc,zhangmkc,fenx,rulsyfrl)values("
								+ id
								+ ",to_date('"
								+ rsl.getString("riq")
								+ "','yyyy-mm-dd'),(select id from diancxxb dc where dc.mingc='"
								+ rsl.getString("diancxxb_id") + "'),"
								+ rsl.getDouble("shiygyl") + ","
								+ rsl.getDouble("rucsyfrl") + ","
								+ rsl.getDouble("rucpjdj") + ","
								+ rsl.getDouble("quanbhyyyl") + ","
								+ rsl.getDouble("fadhyl") + ","
								+ rsl.getDouble("gongrhyl") + ","
								+ rsl.getDouble("qithyl") + ","
								+ rsl.getDouble("chucshl") + ","
								+ rsl.getDouble("shijkc") + ","
								+ rsl.getDouble("zhangmkc") + ",'本月',"
								+ rsl.getDouble("rulsyfrl")+");\n");

			} else {
				sql
						.append("update yuesyhcqkb set diancxxb_id=(select id from diancxxb dc where dc.mingc='"
								+ rsl.getString("diancxxb_id")
								+ "'),shiygyl="
								+ rsl.getDouble("shiygyl")
								+ ",rucsyfrl="
								+ rsl.getDouble("rucsyfrl")
								+ ",rucpjdj="
								+ rsl.getDouble("rucpjdj")
								+ ",quanbhyyyl="
								+ rsl.getDouble("quanbhyyyl")
								+ ",fadhyl="
								+ rsl.getDouble("fadhyl")
								+ ",gongrhyl="
								+ rsl.getDouble("gongrhyl")
								+ ",qithyl="
								+ rsl.getDouble("qithyl")
								+ ",chucshl="
								+ rsl.getDouble("chucshl")
								+ ",shijkc="
								+ rsl.getDouble("shijkc")
								+ ",zhangmkc="
								+ rsl.getDouble("zhangmkc")
								+ ",rulsyfrl="
								+ rsl.getDouble("rulsyfrl")
								+ " where id="
								+ rsl.getLong("id") + ";\n");
			}
			sql.append("end;");
			con.getUpdate(sql.toString());

			ResultSetList rsllj = LeijSelect(diancxxb_id);
			StringBuffer sqllj = new StringBuffer("begin \n");

			while (rsllj.next()) {
				sqllj.append("delete from ").append("yuesyhcqkb").append(
						" where id =").append(
						"(select id from yuesyhcqkb where diancxxb_id="
								+ rsllj.getString("diancxxb_id")
								+ " and riq=to_date('" + intyear + "-"
								+ StrMonth
								+ "-01','yyyy-mm-dd') and fenx='累计')").append(
						";\n");
				long yuesyhcqkb_id = 0;
				yuesyhcqkb_id = Long.parseLong(MainGlobal
						.getNewID(((Visit) getPage().getVisit())
								.getDiancxxb_id()));
				sqllj
						.append("insert into yuesyhcqkb(id,riq,diancxxb_id,shiygyl,rucsyfrl,rucpjdj,"
								+ "quanbhyyyl,fadhyl,gongrhyl,qithyl,chucshl,shijkc,zhangmkc,fenx,rulsyfrl)values("
								+ yuesyhcqkb_id
								+ ",to_date('"
								+ intyear
								+ "-"
								+ StrMonth
								+ "-01','yyyy-mm-dd'),"
								+ rsllj.getString("diancxxb_id")
								+ ","
								+ rsllj.getDouble("shiygyl")
								+ ","
								+ rsllj.getDouble("rucsyfrl")
								+ ","
								+ rsllj.getDouble("rucpjdj")
								+ ","
								+ rsllj.getDouble("quanbhyyyl")
								+ ","
								+ rsllj.getDouble("fadhyl")
								+ ","
								+ rsllj.getDouble("gongrhyl")
								+ ","
								+ rsllj.getDouble("qithyl")
								+ ","
								+ rsllj.getDouble("chucshl")
								+ ","
								+ rsllj.getDouble("shijkc")
								+ ","
								+ rsllj.getDouble("zhangmkc") + ",'累计'," 
								+ rsllj.getDouble("rulsyfrl") + ");\n");
			}
			sqllj.append("end;");
			con.getInsert(sqllj.toString());
		}
		con.Close();
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

	public ResultSetList LeijSelect(String diancxxb_id) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		// -----------------------------------
		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";

		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		String sqllj =

		"select dc.id as diancxxb_id,\n"
				+ "      sum(yshc.shiygyl) as shiygyl,\n"
				+ "      decode(sum(yshc.shiygyl),0,0,round(sum(yshc.rucsyfrl)*sum(yshc.shiygyl)/sum(yshc.shiygyl),3)) as rucsyfrl,\n"
				+ "      decode((sum(yshc.fadhyl)+sum(yshc.gongrhyl)),0,0,round(sum(yshc.rulsyfrl)*(sum(yshc.fadhyl)+sum(yshc.gongrhyl))/(sum(yshc.fadhyl)+sum(yshc.gongrhyl)),3)) as rulsyfrl,\n"
				+ "      decode(sum(yshc.shiygyl),0,0,round(sum(yshc.rucpjdj)*sum(yshc.shiygyl)/sum(yshc.shiygyl),2)) as rucpjdj,\n"
				+ "      sum(yshc.quanbhyyyl) as quanbhyyyl,\n"
				+ "      sum(yshc.fadhyl) as fadhyl,\n"
				+ "      sum(yshc.gongrhyl) as gongrhyl,\n"
				+ "             sum(yshc.qithyl) as qithyl,\n"
				+ "             sum(yshc.chucshl) as chucshl,\n"
				+ "             sum(yshc.shijkc) as shijkc,\n"
				+ "             sum(yshc.zhangmkc) as zhangmkc\n"
				+ "  from yuesyhcqkb yshc, diancxxb dc\n"
				+ "  where ((yshc.riq = add_months(to_date('" + intyear + "-"
				+ StrMonth + "-01', 'yyyy-mm-dd'), -1) and\n"
				+ "       fenx = '累计') or\n" + "       (yshc.riq = to_date('"
				+ intyear + "-" + StrMonth
				+ "-01', 'yyyy-mm-dd') and fenx = '本月'))\n"
				+ "   and yshc.diancxxb_id = dc.id\n"
				+ "   and dc.id= (select id from diancxxb dc where dc.mingc='"
				+ diancxxb_id + "')\n" + " group by (dc.id)";

		ResultSetList rsllj = con.getResultSetList(sqllj);
		con.Close();
		return rsllj;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		// -----------------------------------
		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";

		}

		String chaxun = "select ysyhc.id,ysyhc.riq, dc.mingc as diancxxb_id ,ysyhc.shiygyl,"
				+ "ysyhc.rucsyfrl,ysyhc.rulsyfrl,ysyhc.rucpjdj,ysyhc.quanbhyyyl,"
				+ "ysyhc.fadhyl,ysyhc.gongrhyl,ysyhc.qithyl,ysyhc.chucshl,ysyhc.shijkc,ysyhc.zhangmkc,ysyhc.fenx"
				+ "  from yuesyhcqkb ysyhc,diancxxb dc \n"
				+ " where ysyhc.diancxxb_id=dc.id\n"
				+ "   and ysyhc.fenx='本月' and to_char(ysyhc.riq,'yyyy-mm') ='"
				+ intyear
				+ "-"
				+ StrMonth
				+ "'  \n"
				+ "    "
				+ str
				+ "  "
				+ "   order by dc.mingc,ysyhc.id";
		// System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yuesyhcqkb");

		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("shiygyl").setHeader("石油供应量");
		egu.getColumn("rucsyfrl").setHeader("入厂石油发热量");
		egu.getColumn("rulsyfrl").setHeader("入炉石油发热量");
		egu.getColumn("rucpjdj").setHeader("入厂平均单价");
		egu.getColumn("quanbhyyyl").setHeader("全部耗用原油量");
		egu.getColumn("quanbhyyyl").setEditor(null);
		egu.getColumn("fadhyl").setHeader("发电耗油量");
		egu.getColumn("gongrhyl").setHeader("供热耗油量");
		egu.getColumn("qithyl").setHeader("其它耗油量");
		egu.getColumn("chucshl").setHeader("储存损耗量");
		egu.getColumn("shijkc").setHeader("实际库存");
		egu.getColumn("zhangmkc").setHeader("帐面库存");
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setHidden(true);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setDefaultValue("本月");

		// 设置不可编辑的颜色
		egu
				.getColumn("quanbhyyyl")
				.setRenderer(
						"function(value,metadata){metadata.css='tdTextext'; return value;}");

		// 设定列初始宽度
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("shiygyl").setWidth(80);
		egu.getColumn("rucsyfrl").setWidth(100);
		egu.getColumn("rulsyfrl").setWidth(100);
		egu.getColumn("rucpjdj").setWidth(80);
		egu.getColumn("quanbhyyyl").setWidth(100);
		egu.getColumn("fadhyl").setWidth(80);
		egu.getColumn("gongrhyl").setWidth(80);
		egu.getColumn("qithyl").setWidth(80);
		egu.getColumn("chucshl").setWidth(80);
		egu.getColumn("shijkc").setWidth(80);
		egu.getColumn("zhangmkc").setWidth(80);
		egu.getColumn("fenx").setWidth(80);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(100);// 设置分页
		egu.setWidth(1000);// 设置页面的宽度,当超过这个宽度时显示滚动条

		// *****************************************设置默认值****************************
		// 电厂下拉框
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// 选集团时刷新出所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu
					.getColumn("diancxxb_id")
					.setComboEditor(
							egu.gridId,
							new IDropDownModel(
									"select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where fuid="
									+ getTreeid() + " order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where id="
									+ getTreeid() + " order by mingc"));
			ResultSetList r = con
					.getResultSetList("select id,mingc from diancxxb where id="
							+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		}

		// 设置日期的默认值,
		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		// 设定工具栏下拉框自动刷新
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("->");
		egu.addTbarText("<font color=\"#EE0000\">单位:万千瓦时、吨、克/千瓦时</font>");

		// ---------------页面js的计算开始------------------------------------------
		// StringBuffer sb = new StringBuffer();
		// // sb.append("gridDiv_grid.on('afteredit',function(e){");
		// //
		// sb.append("e.record.set('DAOHLDY',parseFloat(e.record.get('SHIDLDY')==''?0:e.record.get('SHIDLDY'))/parseFloat(e.record.get('JIHLDY')==''?0:e.record.get('JIHLDY'))*100);");
		// // sb.append("});");
		//		
		//		
		// sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if(e.record.get('DIANCXXB_ID')=='合计'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		// sb.append("});");
		//		
		//		
		// //设定合计列不保存
		// sb.append("function
		// gridDiv_save(record){if(record.get('diancxxb_id')=='合计') return
		// 'continue';}");
		//		

		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");

		sb
				.append("e.record.set('QUANBHYYYL',parseFloat(e.record.get('FADHYL')==''?0:e.record.get('FADHYL'))+parseFloat(e.record.get('GONGRHYL')==''?0:e.record.get('GONGRHYL'))+parseFloat(e.record.get('QITHYL')==''?0:e.record.get('QITHYL')));");
		sb.append("});");
		egu.addOtherScript(sb.toString());
		// ---------------页面js计算结束--------------------------

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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.getYuefModels();

		}

		getSelectData();

	}

	// 年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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

	// 月份
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			cn.Close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// 得到电厂的默认到站
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
}
