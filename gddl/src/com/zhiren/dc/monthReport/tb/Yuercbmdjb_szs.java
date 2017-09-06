package com.zhiren.dc.monthReport.tb;

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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 日期：2011-12-23
 * 描述：修改保存方法中错误的关联项
 */
/*
 * 作者：赵胜男
 * 日期：2012-1-10
 * 描述：修正结算标煤单价填报界面中当结算热值为0时，js计算异常
 *       修正入厂标煤单价界面煤价税不可编辑，标题煤价变为含税煤价，交货前杂费增加宽度，发电量变为MJ/kg
 */


public abstract class Yuercbmdjb_szs extends BasePage {

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

	// 厂别下拉框
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	public void Save() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yuercbmdjb_szs.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}

		while (rsl.next()) {
			if ("0".equals(rsl.getString("ID"))
					|| "".equals(rsl.getString("ID"))) {
				sql.append("insert into yuercbmdj (\n");
				sql
						.append("id,fenx,yuetjkjb_id,meij,laimsl, meijs, jiaohqzf,yunj,yunjs, daozzf,zaf,qit,qnet_ar,biaomdj,buhsbmdj");
				sql.append(")\n");
				sql.append("values (");
				sql.append("getnewid(" + getTreeid() + ")");// id
				sql.append(",'").append(rsl.getString("fenx"));
				sql.append("',").append(rsl.getString("yuetjkjb_id"));
				sql.append(",").append(rsl.getDouble("meij"));
				sql.append(",").append(rsl.getDouble("laimsl"));
				sql.append(",").append(rsl.getDouble("meijs"));
				sql.append(",").append(rsl.getDouble("jiaohqzf"));
				sql.append(",").append(rsl.getDouble("yunj"));
				sql.append(",").append(rsl.getDouble("yunjs"));
				sql.append(",").append(rsl.getDouble("daozzf"));
				sql.append(",").append(rsl.getDouble("zaf"));
				sql.append(",").append(rsl.getDouble("qit"));
				sql.append(",").append(rsl.getDouble("qnet_ar"));
				sql.append(",").append(rsl.getDouble("biaomdj"));
				sql.append(",").append(rsl.getDouble("buhsbmdj"));
				sql.append(");\n");
			} else {
				sql.append("update yuercbmdj\n");
				sql.append("set meij =" + rsl.getDouble("meij") + ",\n");
				sql.append(" laimsl =" + rsl.getDouble("laimsl") + ",\n");
				sql.append(" meijs =" + rsl.getDouble("meijs") + ",\n");
				sql.append(" jiaohqzf =" + rsl.getDouble("jiaohqzf") + ",\n");
				sql.append(" yunj =" + rsl.getDouble("yunj") + ",\n");
				sql.append(" yunjs =" + rsl.getDouble("yunjs") + ",\n");
				sql.append(" daozzf =" + rsl.getDouble("daozzf") + ",\n");
				sql.append(" zaf =" + rsl.getDouble("zaf") + ",\n");
				sql.append(" qit =" + rsl.getDouble("qit") + ",\n");
				sql.append(" qnet_ar =" + rsl.getDouble("qnet_ar") + ",\n");
				sql.append(" biaomdj =" + rsl.getDouble("biaomdj") + ",\n");
				sql.append(" buhsbmdj =" + rsl.getDouble("buhsbmdj") + "\n");
				sql.append(" where id=" + rsl.getLong("id"));
				sql.append(";\n");
			}
		}

		if (!"".equals(sql) && sql != null) {
			int flag = con.getUpdate("begin\n" + sql + "\n end;");
			if (flag != -1) {
				rsl.beforefirst();
				sql.setLength(0);
				while (rsl.next()) {
					if ("累计".equals(rsl.getString("fenx")) && getIsSelectLike()) {
						String sq = "select\n"
								+ "round_new(sum(laimsl),0) as laimsl,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(meij*laimsl)/sum(laimsl)),2) meij,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(meijs*laimsl)/sum(laimsl)),2) meijs,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(yunj*laimsl)/sum(laimsl)),2) yunj,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(yunjs*laimsl)/sum(laimsl)),2) yunjs,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(daozzf*laimsl)/sum(laimsl)),2) daozzf,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(zaf*laimsl)/sum(laimsl)),2) zaf,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(qit*laimsl)/sum(laimsl)),2) qit,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(qnet_ar*laimsl)/sum(laimsl)),2) qnet_ar,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(biaomdj*laimsl)/sum(laimsl)),2) biaomdj,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(buhsbmdj*laimsl)/sum(laimsl)),2) buhsbmdj,\n"
								+ "round_new(decode(sum(laimsl),0,0,sum(jiaohqzf*laimsl)/sum(laimsl)),2) jiaohqzf\n"
								+ "  from yuercbmdj y,yuetjkjb yt,gongysb g,jihkjb j,pinzb p\n"
								+ " where y.yuetjkjb_id=yt.id\n"
								+ "	and yt.gongysb_id=g.id\n"
								+ "	AND yt.pinzb_id=p.id\n"
								+ "	and g.mingc='"
								+ rsl.getString("gmingc")
								+ "'\n"
								+ "	and j.mingc='"
								+ rsl.getString("jmingc")
								+ "'\n"
								+ "	and p.mingc='"
								+ rsl.getString("pmingc")
								+ "'\n"
								+ "   and yt.riq>=to_date('"
								+ getNianf()
								+ "-01-01"
								+ "','yyyy-mm-dd')\n"
								+ "   and yt.riq<=to_date('"
								+ getNianf()
								+ "-"
								+ getYuef()
								+ "-01"
								+ "','yyyy-mm-dd')\n"
								+ "   and y.fenx='"
								+ SysConstant.Fenx_Beny
								+ "'\n"
								+ "   and yt.diancxxb_id="
								+ getTreeid();
						ResultSetList rs = con.getResultSetList(sq);
						rs.next();
						sql.append("update yuercbmdj set " + "laimsl = "
								+ rs.getDouble("laimsl") + ",meij = "
								+ rs.getDouble("meij") + ",meijs = "
								+ rs.getDouble("meijs") + ",yunj = "
								+ rs.getDouble("yunj") + ",yunjs = "
								+ rs.getDouble("yunjs") + ",daozzf = "
								+ rs.getDouble("daozzf") + ",zaf = "
								+ rs.getDouble("zaf") + ",qit = "
								+ rs.getDouble("qit") + ",qnet_ar = "
								+ rs.getDouble("qnet_ar") + ",biaomdj = "
								+ rs.getDouble("biaomdj") + ",buhsbmdj = "
								+ rs.getDouble("buhsbmdj") + ",jiaohqzf = "
								+ rs.getDouble("jiaohqzf")
								+ " where yuetjkjb_id ="
								+ rsl.getString("yuetjkjb_id") + " and fenx = '"
								+ rsl.getString("fenx") + "';");
					}
				}
				if("".equals(sql) || sql==null){
					setMsg("保存成功!");
				}else{
					flag = con.getUpdate("begin\n" + sql.toString() + "\n end;");
					if (flag != -1) {
						setMsg("保存成功!");
					} else {
						setMsg("保存成功,累计数计算失败!");
					}
				}
			} else {
				setMsg("保存失败!");
			}

		}
	}

	public void DelData() {
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "delete from yuercbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate + " and diancxxb_id=" + getTreeid() + ")";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
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

		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		getSelectData();
	}

	public void getSelectData() {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "";
		strSql =

		"select y.id,s.gmingc,s.jmingc,s.pmingc,s.fenx,s.yuetjkjb_id,nvl(y.laimsl,0) laimsl,y.meij,y.meijs,y.jiaohqzf,y.yunj,y.yunjs,\n"
				+ "      y.daozzf,y.zaf,y.qit,y.qnet_ar,y.biaomdj,y.buhsbmdj from (\n"
				+ "      select x.fenx,y.id yuetjkjb_id,g.mingc gmingc,j.mingc jmingc,p.mingc pmingc\n"
				+ "        from yuetjkjb y,\n"
				+ "             (select nvl('本月', '') fenx\n"
				+ "                from dual\n"
				+ "              union\n"
				+ "              select nvl('累计', '') fenx from dual) x,\n"
				+ "             gongysb g,\n"
				+ "             jihkjb j,\n"
				+ "             pinzb p\n"
				+ "       where y.riq = "
				+ CurrODate
				+ "\n"
				+ "         and y.diancxxb_id="
				+ getTreeid()
				+ "\n"
				+ "         and y.gongysb_id=g.id\n"
				+ "         and y.jihkjb_id=j.id\n"
				+ "         and y.pinzb_id=p.id\n"
				+ "      ) s,yuercbmdj y\n"
				+ "      where y.yuetjkjb_id(+)=s.yuetjkjb_id\n"
				+ "        and y.fenx(+)=s.fenx\n"
				+ "      order by s.yuetjkjb_id,s.fenx";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuercbmdj");
		// /设置显示列名称
		egu.setWidth("bodyWidth");

		egu.getColumn("yuetjkjb_id").setHeader("月统计口径表ID");
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("yuetjkjb_id").setWidth(60);
		egu.getColumn("gmingc").setHeader("供货单位");
		egu.getColumn("gmingc").setEditor(null);
		egu.getColumn("gmingc").setWidth(120);
		egu.getColumn("jmingc").setHeader("计划口径");
		egu.getColumn("jmingc").setEditor(null);
		egu.getColumn("jmingc").setWidth(80);
		egu.getColumn("pmingc").setHeader("品种");
		egu.getColumn("pmingc").setEditor(null);
		egu.getColumn("pmingc").setWidth(60);

		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setWidth(60);

		egu.getColumn("yuetjkjb_id").setHeader("");
		egu.getColumn("yuetjkjb_id").setEditor(null);
		egu.getColumn("yuetjkjb_id").setWidth(70);

		egu.getColumn("laimsl").setHeader("来煤数量");
		// egu.getColumn("laimsl").setEditor(null);
		egu.getColumn("laimsl").setWidth(60);
		
		egu.getColumn("meij").setHeader("含税煤价");
		// egu.getColumn("meij").setEditor(null);
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meijs").setHeader("煤价税");
	    egu.getColumn("meijs").setEditor(null);
		egu.getColumn("meijs").setWidth(60);

		egu.getColumn("jiaohqzf").setHeader("交货前杂费");
		// egu.getColumn("JIAOHQZF").setEditor(null);
		egu.getColumn("jiaohqzf").setWidth(100);

		egu.getColumn("yunj").setHeader("含税运价");
		// egu.getColumn("yunj").setEditor(null);
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunjs").setHeader("运价税");
		// egu.getColumn("yunjs").setEditor(null);
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);

		egu.getColumn("zaf").setHeader("杂费");
		// egu.getColumn("zaf").setEditor(null);
		egu.getColumn("zaf").setWidth(60);

		egu.getColumn("qit").setHeader("其他");
		egu.getColumn("qit").setWidth(60);

		egu.getColumn("qnet_ar").setHeader("发热量");
		// egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("qnet_ar").setWidth(60);

		egu.getColumn("biaomdj").setHeader("标煤单价");
		// egu.getColumn("biaomdj").setEditor(null);
		egu.getColumn("biaomdj").setWidth(70);
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		// egu.getColumn("buhsbmdj").setEditor(null);
		egu.getColumn("buhsbmdj").setWidth(90);
		egu.getColumn("buhsbmdj").setDefaultValue("0");

		egu.setDefaultsortable(false);

		// 把所有列改为可以编辑的。
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		// /设置按钮
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

		// 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		// 保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_SaveAll,
				"gridDiv", egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		// 打印按钮
//		GridButton gbp = new GridButton("打印", "function (){"
//				+ MainGlobal.getOpenWinScript("MonthReport&lx=yuercbmdjb")
//				+ "}");
//		gbp.setIcon(SysConstant.Btn_Icon_Print);
//		egu.addTbarBtn(gbp);

		Checkbox cb = new Checkbox();
		cb.setId("SelectLike");
		// if(this.getChecklc().equals("true")){
		// cb.setChecked(true);
		// }
		cb
				.setListeners("check:function(own,checked){if(checked){document.all.SelectLike.value='true'}else{document.all.SelectLike.value='false'}}");
		egu.addToolbarItem(cb.getScript());
		egu.addTbarText("是否自动计算累计值");

		StringBuffer sb = new StringBuffer();
		
		String meijs = MainGlobal.getXitxx_item("结算", "煤款税率", this.getTreeid(), "0.17");
		String yunjs = MainGlobal.getXitxx_item("结算", "运费税率", this.getTreeid(), "0.07");
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n" + 
				"  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjs + ",2);\n" + 
				"    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n" + 
				"\n" + 
				"  if(e.field=='MEIJ'||e.field=='MEIJS'||e.field=='YUNJ'||e.field=='YUNJS'||e.field=='DAOZZF'||e.field=='ZAF'||e.field=='QIT'||e.field=='QNET_AR'){\n" + 
				"\n" + 
				"    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ')||0)+eval(gridDiv_ds.getAt(i).get('JIAOHQZF')||0)+eval(gridDiv_ds.getAt(i).get('YUNJ')||0)+eval(gridDiv_ds.getAt(i).get('DAOZZF')||0)+eval(gridDiv_ds.getAt(i).get('ZAF')||0)+eval(gridDiv_ds.getAt(i).get('QIT')||0);\n"+ 
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS')||0)-eval(gridDiv_ds.getAt(i).get('YUNJS')||0))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"\n" + 
				"    gridDiv_ds.getAt(i).set('BIAOMDJ',biaomdj);\n" + 
				"    gridDiv_ds.getAt(i).set('BUHSBMDJ',buhsbmdj);\n" + 
				"  }\n" + 
				
				"});"
		);

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
			btnsb.append("是否删除").append(cnDate).append("的所有数据？");
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

	// ----------------- 这里用来释放记录的
	// ----------------------------------------------------
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			this.setTreeid(null);
			setRiq();
			getSelectData();
		}
		getSelectData();
	}

	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ getTreeid();
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
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
			rs.close();
		} catch (SQLException e) {
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

	boolean treechange = false;

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

	// ---------------------------------------------------------------------------------------
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

	public boolean getIsSelectLike() {
		return ((Visit) this.getPage().getVisit()).getboolean8();
	}

	public String getSelectLike() {
		return ((Visit) this.getPage().getVisit()).getString8();
	}

	public void setSelectLike(String value) {
		boolean flag = false;
		if ("true".equals(value)) {
			flag = true;
		}
		((Visit) this.getPage().getVisit()).setboolean8(flag);
	}
}
