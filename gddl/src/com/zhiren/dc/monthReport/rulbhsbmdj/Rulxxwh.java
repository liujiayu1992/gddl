package com.zhiren.dc.monthReport.rulbhsbmdj;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:ly
 * 时间:2010-07-27
 * 描述:入炉信息维护，为计算入炉不含税标煤单价维护系统中得不到的数据。
 */

public class Rulxxwh extends BasePage implements PageValidateListener {
	// 界面用户提示
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String strSql = "delete from rulxxwh where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ getTreeid() + "\n";
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


	public void CreateData() {
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		Date cd = DateUtil.getDate(getNianf() + "-" + getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		int intYuef = Integer.parseInt(getYuef());
		int flag;
		String sql = "delete rulxxwh where riq = " + CurrODate
			+ " and diancxxb_id = " + diancxxb_id + "\n";
		flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ "删除入炉信息维护表时发生异常！" + "\nSQL:" + sql);
			setMsg("删除入炉信息维护表时发生异常！");
			con.rollBack();
			con.Close();
			return;
		}
		
		String sql_cmdj = "";
		String jizfz = "";
		ResultSetList rsl_dj = null;
		String sql_jz = "select * from jizfzb where diancxxb_id = " + diancxxb_id + "\n";
		ResultSetList rsl = con.getResultSetList(sql_jz);
		String newID = "";
		sql = "begin \n";
		while(rsl.next()){
			jizfz = "";
			if(rsl.getString("mingc").equals("二期")){
				jizfz = "'贫瘦煤', '无烟煤'";
			} else if(rsl.getString("mingc").equals("三期")){
				jizfz = "'气肥煤','褐煤'";
			}
			
			newID = MainGlobal.getNewID(Long.parseLong(diancxxb_id));
		
			sql_cmdj = 
				"select decode(nvl(cm2.shangycmsl, 0) + nvl(cf.meil, 0) +\n" +
				"              nvl(bygj.benygjsl, 0) - nvl(sygj.shangygjsl, 0),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new((nvl(cm1.shangycmdj, 0) * nvl(cm2.shangycmsl, 0) +\n" + 
				"                        nvl(cf.chengfje, 0) + nvl(bygj.benygjje, 0) -\n" + 
				"                        nvl(sygj.shangygjje, 0) + nvl(zf.changnzf, 0)) /\n" + 
				"                        (nvl(cm2.shangycmsl, 0) + nvl(cf.meil, 0) +\n" + 
				"                        nvl(bygj.benygjsl, 0) - nvl(sygj.shangygjsl, 0)),\n" + 
				"                        2)) as shangycmdj\n" + 
				"  from --二期\n" + 
				"       --上月存煤单价\n" + 
				"        (select r.shangycmdj\n" + 
				"           from rulxxwh r\n" + 
				"          where r.diancxxb_id = " + diancxxb_id + "\n" + 
				"            and r.riq = add_months("+CurrODate+", -1)\n" + 
				"            and r.jizfzb_id = "+rsl.getString("id")+") cm1,\n" + 
				"       --上月存煤数量\n" + 
				"       (select nvl(sum(y.kuc), 0) as shangycmsl\n" + 
				"          from yuehcb y, yuetjkjb tj\n" + 
				"         where y.yuetjkjb_id = tj.id\n" + 
				"           and y.fenx = '本月'\n" + 
				"           and tj.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and tj.riq = add_months("+CurrODate+", -2)\n" + 
				"           and tj.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))) cm2,\n" + 
				"       --本月承付金额、数量\n" + 
				"       (select sum((y.meij + y.yunj + y.zaf + y.daozzf + y.qit + y.kuangqyf) *\n" + 
				"                   y.jiesl) as chengfje,\n" + 
				"               sum(y.jiesl) as meil\n" + 
				"          from yuejsbmdj y, yuetjkjb tj\n" + 
				"         where y.yuetjkjb_id = tj.id\n" + 
				"           and y.fenx = '本月'\n" + 
				"           and tj.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and tj.riq = add_months("+CurrODate+", -1)\n" + 
				"           and tj.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))) cf,\n" + 
				"       --本月估价金额、数量\n" + 
				"       (select nvl(sum(max(g.meij) * f.laimsl), 0) as benygjje,\n" + 
				"               nvl(sum(f.laimsl), 0) as benygjsl\n" + 
				"          from guslsb g, fahb f\n" + 
				"         where g.fahb_id = f.id\n" + 
				"           and g.leix < 4\n" + 
				"           and f.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and last_day(f.daohrq) =\n" + 
				"               last_day(add_months("+CurrODate+", -1))\n" + 
				"           and f.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))\n" + 
				"         group by f.id, f.laimsl) bygj,\n" + 
				"       --上月估价金额、数量\n" + 
				"       (select nvl(sum(max(g.meij) * f.laimsl), 0) as shangygjje,\n" + 
				"               nvl(sum(f.laimsl), 0) as shangygjsl\n" + 
				"          from guslsb g, fahb f\n" + 
				"         where g.fahb_id = f.id\n" + 
				"           and g.leix < 4\n" + 
				"           and f.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and last_day(f.daohrq) =\n" + 
				"               last_day(add_months("+CurrODate+", -2))\n" + 
				"           and f.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+jizfz+"))\n" + 
				"         group by f.id, f.laimsl) sygj,\n" + 
				"       (select r.changnzf\n" + 
				"          from rulxxwh r, jizfzb j\n" + 
				"         where r.jizfzb_id = j.id\n" + 
				"           and j.mingc = '"+rsl.getString("mingc")+"'\n" + 
				"           and r.diancxxb_id = " + diancxxb_id + "\n" + 
				"           and r.riq = add_months("+CurrODate+", -1)) zf";
			rsl_dj = con.getResultSetList(sql_cmdj);
			if(rsl_dj.next()){
				sql += "insert into rulxxwh values("+newID+","+diancxxb_id+","+CurrODate+","+rsl_dj.getString("shangycmdj")+",0,0,"+rsl.getString("id")+");\n";
			} else {
				sql += "insert into rulxxwh values("+newID+","+diancxxb_id+","+CurrODate+",0,0,0,"+rsl.getString("id")+");\n";
			}
		}
		sql += "end; \n";
		
		flag = con.getInsert(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ "入炉信息维护生成时发生异常！"+ "\nSQL:" + sql);
			setMsg("入炉信息维护生成时发生异常！");
			con.rollBack();
			con.Close();
			return;
		}

		con.commit();
		con.Close();
		setMsg(CurrZnDate + "的数据成功生成！");
	}

	public void getSelectData() {
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
		String sql = "select r.id,r.diancxxb_id,j.mingc as jizfzb_id,r.riq,shangycmdj,youqsdj,changnzf from rulxxwh r,jizfzb j \n"
				+ "	  where r.diancxxb_id = " + diancxxb_id + "\n"
				+ "		and r.jizfzb_id = j.id\n"
				+ "		and r.riq = " + CurrODate + "\n";
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("rulxxwh");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// egu.getColumn("xuh").setHeader("序号");
		// egu.getColumn("xuh").setWidth(50);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("jizfzb_id").setHeader("机组");
		egu.getColumn("jizfzb_id").setWidth(100);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("shangycmdj").setHeader("上月存煤单价");
		egu.getColumn("shangycmdj").setWidth(100);
		egu.getColumn("youqsdj").setHeader("油去税单价");
		egu.getColumn("youqsdj").setWidth(100);
		egu.getColumn("changnzf").setHeader("燃料厂内费用");
		egu.getColumn("changnzf").setWidth(100);

		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("jizfzb_id").setEditor(new ComboBox());
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select distinct id,mingc from jizfzb order by mingc"));
		egu.getColumn("jizfzb_id").returnId=true;
		egu.getColumn("jizfzb_id").setEditor(null);
		
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

		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符

		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(MainGlobal.getExtMessageBox(
										"'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		// 生成按钮
		GridButton gbc = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		// 删除按钮
		GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		// 保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);

		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "年" + getYuef() + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将删除").append(cnDate).append("的已存数据，是否继续？");
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
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
