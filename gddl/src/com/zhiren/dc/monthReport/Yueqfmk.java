package com.zhiren.dc.monthReport;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 王磊
 * @since 2009-06-03
 * @describe 月欠付煤款(功能1完成甘肃地区未结算煤量、煤款及结算未给付煤款的填报上报功能)
 * @version 1.0
 */
public class Yueqfmk extends BasePage implements PageValidateListener {
	/*
	 * 反馈到页面的用户提示。
	 * 在后台设置提示信息,相当于在后台向前台传送了一个java script 脚本
	 */
	private String msg = "";
	public String getMsg() {return msg;}
	public void setMsg(String msg) {this.msg = MainGlobal.getExtMessageBox(msg, false);}
	/*
	 * 页面初始加载的时候执行的方法
	 * 一些每次刷新页面需刷新的变量  的初始化方法可以写在这里
	 * @see org.apache.tapestry.AbstractPage#initialize()
	 */
	protected void initialize() {super.initialize();setMsg("");}

	/*
	 * 用来传输页面改动的一个textfield组件
	 * 里面的内容为一个xml文档 
	 * 可用ResultSetList 中的getModifyResultSet 和 getDeleteResultSet 进行解析
	 */
	private String Change;
	public String getChange() {return Change;}
	public void setChange(String change) {Change = change;}
	/*
	 * 页面按钮的监听源自Tapestry的组件
	 * 现在被隐藏，通过Ext构造的按钮的click事件调用
	 */
	/* 添加按钮 */
	private boolean _Insertclick = false;
	public void InsertButton(IRequestCycle cycle) {_Insertclick = true;}
	/* 刷新按钮 */
	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {_Refreshclick = true;}
	/* 生成按钮 */
	private boolean _CreateClick = false;
	public void CreateButton(IRequestCycle cycle) {_CreateClick = true;}
	/* 删除按钮 */
	private boolean _DelClick = false;
	public void DelButton(IRequestCycle cycle) {_DelClick = true;}
	/* 保存按钮 */
	private boolean _SaveClick = false;
	public void SaveButton(IRequestCycle cycle) {_SaveClick = true;}
	/* 上报按钮 */
	private boolean _ShangbClick = false;
	public void ShangbButton(IRequestCycle cycle) {_ShangbClick = true;}
	/* 修改申请按钮 */
	private boolean _ShenqxgClick = false;
	public void ShenqxgButton(IRequestCycle cycle) {_ShenqxgClick = true;}
	/*
	 * 监听页面的form 的submit事件
	 * cycle 相当于jsp中的 request
	 */
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		if (_Insertclick) {
			_Insertclick = false;
			Insert();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_ShangbClick) {
			_ShangbClick = false;
			Shangb();
		}
		if (_ShenqxgClick) {
			_ShenqxgClick = false;
			Shenqxg();
		}
		initGrid();
	}
	
	private String getInsertSql(String diancxxb_id, String gongysb_id, 
			String CurrODate, String fenx){
		String sql = "insert into yueqfmkb(id,diancxxb_id,gongysb_id,riq,fenx) values(" +
		"getnewid(" + diancxxb_id + ")," + diancxxb_id + "," + gongysb_id + "," +
		CurrODate + ",'" + fenx + "')";
		return sql;
	}
	/**
	 * @describe 根据用户选择的供应商插入欠付煤款信息
	 *
	 */
	private void Insert(){
		String gongysid = "";
		if(getGongysValue() == null || getGongysValue().getId() ==-1){
			setMsg("请选择需要添加欠付款信息的供应商");
			return ;
		}
		gongysid = getGongysValue().getStrId();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String sql = "select * from yueqfmkb where diancxxb_id =" + diancxxb_id + 
		" and gongysb_id=" + gongysid + " and riq=" + CurrODate;
		if(con.getHasIt(sql)){
			setMsg("此供应商数据已存在,不能再次添加！");
			return;
		}
		con.getInsert(getInsertSql(diancxxb_id,gongysid,CurrODate,SysConstant.Fenx_Beny));
		con.getInsert(getInsertSql(diancxxb_id,gongysid,CurrODate,SysConstant.Fenx_Leij));
	}
	/**
	 * @describe 根据历史未结算数据及上月数据生成供应商列表 并计算出未结煤量
	 * 		1、删除本月数据
	 * 		2、生成供应商列表并插入数据库
	 * 		3、根据数据库中供应商信息计算未结煤量
	 * 		
	 */
	private void CreateData() {
		String diancxxb_id = getTreeid();	//得到所选电厂ID
		JDBCcon con = new JDBCcon();		//构造数据库JDBC连接
		con.setAutoCommit(false);			//设定JDBC不自动提交
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
											//中文的年月
		Date cd = DateUtil.getDate(getNianf() + "-"
				+ getYuef() + "-01");		//当前选中年月对应的Date日期
		String CurrODate = DateUtil.FormatOracleDate(cd);
											//当前选中年月Oracle格式日期
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
											//计算得到上个月的Oracle格式日期
//		删除本月数据
		String sql = "delete from yueqfmkb where riq = " + CurrODate +
		" and diancxxb_id = " + diancxxb_id;
		con.getDelete(sql);
//		生成供应商列表并插入数据库
		sql = "select distinct gongysb_id from yueqfmkb where diancxxb_id =" +
		diancxxb_id + " and riq = " + LastODate + " union select distinct g.dqid " +
		"from fahb f,vwgongys g where f.jiesb_id = 0 and f.gongysb_id = g.id" + 
		" and f.diancxxb_id = " + diancxxb_id;
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.getRows()>0){
			while(rsl.next()){
				con.getInsert(getInsertSql(diancxxb_id,rsl.getString("gongysb_id"),CurrODate,SysConstant.Fenx_Beny));
				con.getInsert(getInsertSql(diancxxb_id,rsl.getString("gongysb_id"),CurrODate,SysConstant.Fenx_Leij));
			}
			rsl.close();
		}
		sql = "select * from yueqfmkb where riq = " + CurrODate +
		" and diancxxb_id = " + diancxxb_id;
		rsl = con.getResultSetList(sql);
		ResultSetList rs = null;
		while(rsl.next()){
			String tmp = SysConstant.Fenx_Beny.equals(rsl.getString("fenx"))?
					" and f.daohrq>" + CurrODate + " and f.daohrq < add_months("+
					CurrODate+",1)":"";
			sql = "select sum(f.laimsl) laimsl,sum(f.laimsl * r.meij / 10000) qiank from fahb f,vwgongys g,ruccb r " +
					"where f.gongysb_id = g.id and f.ruccbb_id = r.id and diancxxb_id = " + 
					rsl.getString("diancxxb_id") + " and g.dqid = " +
					rsl.getString("gongysb_id") + " and f.jiesb_id = 0 " + tmp;
			rs = con.getResultSetList(sql);
			if(rs.next()){
				sql = "update yueqfmkb set meil =" + rs.getDouble("laimsl") + 
				",qiank =" + rs.getDouble("qiank") +
				" where id=" + rsl.getString("id");
				con.getUpdate(sql);
			}
			rs.close();
		}
		rsl.close();
		con.commit();
		con.Close();
		setMsg(CurrZnDate + "的数据成功生成！");
	}
	/**
	 *  @describe 删除当月数据
	 *
	 */
	public void DelData() {
		String diancxxb_id = getTreeid();	//得到所选电厂ID
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf() + "年" + getYuef() + "月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String sql = "delete from yueqfmkb where riq = " + CurrODate +
		" and diancxxb_id = " + diancxxb_id;
		int flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ sql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}
	/**
	 * @describe 保存修改后的欠付煤款信息
	 *
	 */
	private void Save() {
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl.getRows()>0){
			String sql = "begin\n";
			while(rsl.next()){
				sql += "update yueqfmkb set meil=" + rsl.getDouble("meil")+
				",qiank = "+ rsl.getDouble("qiank") + " where id =" +
				rsl.getString("id") + ";\n";
			}
			rsl.close();
			sql += "end;\n";
			JDBCcon con = new JDBCcon();
			con.getUpdate(sql);
			con.Close();
		}
		rsl.close();

		setMsg(ErrorMessage.SaveSuccessMessage);
	}
	/**
	 * @describe 将数据上报
	 *
	 */
	private void Shangb(){
		Date cd = DateUtil.getDate(getNianf() + "-"
				+ getYuef() + "-01");		//当前选中年月对应的Date日期
		String CurrODate = DateUtil.FormatOracleDate(cd);
											//当前选中年月Oracle格式日期
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		MainGlobal.Shujshcz(con,getTreeid(),CurrODate,
				"0","Yueqfmk",v.getRenymc(),"");
		con.Close();
		setMsg("上报数据成功！");
	}
	/**
	 * @describe 申请对上报数据的修改
	 *
	 */
	private void Shenqxg(){
		Date cd = DateUtil.getDate(getNianf() + "-"
				+ getYuef() + "-01");		//当前选中年月对应的Date日期
		String CurrODate = DateUtil.FormatOracleDate(cd);
											//当前选中年月Oracle格式日期
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//con.getUpdate(getInsRptTableSql());
		MainGlobal.Shujshcz(con,getTreeid(),CurrODate,
				"0","Yueqfmk",v.getRenymc(),getChange());
	}
	/**
	 * @describe 初始化页面
	 *
	 */
	public void initGrid() {
//		得到当前电厂树所选择的电厂
		String diancxxb_id = getTreeid();
//		得到当前选择年月对应的Oracle格式的日期
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
//		取得数据SQL
		String sql = "select s.id, g.mingc gongysb_id, s.fenx, s.meil, s.qiank, beiz\n" +
			"from yueqfmkb s,vwdianc d,vwgongys g\n" + 
			"where s.diancxxb_id = d.id and s.gongysb_id = g.id\n" + 
			"and s.riq= " + CurrODate + " and d.id = " + diancxxb_id +
			" order by g.xuh,g.mingc,s.fenx";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
// 		设置grid宽度
		egu.setWidth("bodyWidth");
//		设置为编辑不分页
		egu.addPaging(0);
//		设置为可编辑grid
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		设置列名
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
		egu.getColumn("meil").setHeader("未结算煤量(吨)");
		egu.getColumn("qiank").setHeader("欠款(万元)");
		egu.getColumn("beiz").setHeader("备注");
//		设置列宽
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("meil").setWidth(120);
		egu.getColumn("qiank").setWidth(100);
		egu.getColumn("beiz").setWidth(100);
//		设置列不可编辑
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setRenderer(
				"function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer(
				"function(value,metadata){metadata.css='tdTextext'; return value;}");
		
//		设置年份下拉框
		egu.addTbarText("年份");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
//		设置月份下拉框
		egu.addTbarText("月份");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
//		设置分隔符
		egu.addTbarText("-");
// 		设置电厂选择树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
// 		设置分隔符
		egu.addTbarText("-");

// 		判断数据是否被锁定(根据状态判断状态1已上报锁定0未上报锁定)
		boolean isLocked = isLocked(con);
// 		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox(
				"'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+" +
				"Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",
		true)).append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
// 		生成按钮
		GridButton gbc = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		if (isLocked) {
			gbc.setDisabled(true);
		}
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
// 		删除按钮
		GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
		if (isLocked) {
			gbd.setDisabled(true);
		}
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
// 		保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		if (isLocked) {
			gbs.setDisabled(true);
		}
		egu.addTbarBtn(gbs);
//		设置分隔符
		egu.addTbarText("-");
//		设置供应商下拉框
		egu.addTbarText("供应商");
		ComboBox comb3 = new ComboBox();
		comb3.setWidth(120);
		comb3.setTransform("GongysDropDown");
		comb3.setId("GongysDropDown");// 和自动刷新绑定
		comb3.setLazyRender(true);// 动态绑定
		comb3.setEditable(true);
		egu.addToolbarItem(comb3.getScript());
		
//		添加按钮
		GridButton gbi = new GridButton("添加",
				"function (){document.getElementById('InsertButton').click();}");
		egu.addTbarBtn(gbi);
//		设置分隔符
		egu.addTbarText("-");
		if(isLocked){
			GridButton gbxg = new GridButton("申请修改", 
			"function(){Rpt_window.show();}");
			gbxg.setIcon(SysConstant.Btn_Icon_Return);
			egu.addTbarBtn(gbxg);
		}else{
			GridButton gbsb = new GridButton("上报", 
			"function (){document.getElementById('ShangbButton').click();}");
			gbsb.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(gbsb);
		}
		
//		// 打印按钮
//		GridButton gbp = new GridButton("打印", "function (){"
//				+ MainGlobal.getOpenWinScript("MonthReport&lx=yueslb") + "}");
//		gbp.setIcon(SysConstant.Btn_Icon_Print);
//		egu.addTbarBtn(gbp);

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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			initGrid();
		}
	}

	public boolean isLocked(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		return con.getHasIt("select zhuangt from shujshb where diancxxb_id = "+getTreeid()+"\n" +
				"and mokmc = 'Yueqfmk' and zhuangt = 1 and riq = " + CurrODate);
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

// 	供应商
    public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGongysValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getGongysModels() {
		String sql = "select g.dqid,g.dqmc from vwgongys g order by g.dqxh";
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, "请选择"));
		return;
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

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}