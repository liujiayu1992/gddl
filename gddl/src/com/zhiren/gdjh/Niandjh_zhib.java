package com.zhiren.gdjh;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2012-10-18
 * 描述：新增复制计划按钮（如果界面中没有数据则显示该按钮，否则不显示）
 * 		增加总厂不能填报的限制。
 */
/*
 * 作者：夏峥
 * 时间：2012-11-19
 * 适用范围：国电电力及其下属电厂
 * 描述：调整年计划指标中标煤单价和入炉综合标煤单价的计算公式。
 */
/*
 * 作者：夏峥
 * 时间：2013-01-15
 * 适用范围：国电电力及其下属电厂
 * 描述：修正年份未初始化的BUG
 */
public class Niandjh_zhib extends BasePage implements PageValidateListener {
	public static final int col_odd = 1;
	public static final int col_even = 0;
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

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
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
			int _nianf = DateUtil.getYear(new Date())+1;
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
		for (i = 2009; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
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
			getNianfModels();
			setNianfValue(null);
			setRiq();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	private boolean _Cpyclick = false;

	public void CpyButton(IRequestCycle cycle) {
		_Cpyclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		if(_Cpyclick){
			_Cpyclick=false;
			DelData();
			CopyData();
		}
		getSelectData();
	}

	public void DelData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String CurrZnDate = getNianf() + "年";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String strSql = "delete from niandjh_zhib where riq = " + CurrODate
				+ " and diancxxb_id =" + diancxxb_id;
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

	/**
	 * @param con
	 * @return true:已上传状态中 不能修改数据 false:未上传状态中 可以修改数据
	 */
	private boolean getZhangt(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String sql = "select s.zhuangt zhuangt\n" + "  from niandjh_zhib s\n"
				+ " where  s.diancxxb_id = " + getTreeid() + "\n"
				+ "   and s.riq = " + CurrODate;
		ResultSetList rs = con.getResultSetList(sql);
		boolean zt = true;
		if (con.getHasIt(sql)) {
			while (rs.next()) {
				if (rs.getInt("zhuangt") == 0 || rs.getInt("zhuangt") == 2) {
					zt = false;
				}
			}
		} else {
			zt = false;
		}
		return zt;
	}

	public void getSelectData() {
//		initData();
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		ExtGridUtil egu = new ExtGridUtil("gridDiv");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		egu.setDefaultsortable(false);
		egu.addColumn(new GridColumn(GridColumn.ColType_Rownum));

		GridColumn ColName1 = new GridColumn(0, "COLNAME1", "字段名1", 90);
		ColName1.setDataType(GridColumn.DataType_String);
		ColName1.setUpdate(true);
		ColName1.setHidden(true);
		egu.addColumn(ColName1);
		GridColumn Item1 = new GridColumn(0, "ITEM1", "项目1", 200);
		Item1.setDataType(GridColumn.DataType_String);
		Item1.setUpdate(true);
		Item1.setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
		egu.addColumn(Item1);
		GridColumn Unit1 = new GridColumn(0, "UNIT1", "单位1", 80);
		Unit1.setDataType(GridColumn.DataType_String);
		Unit1.setUpdate(true);
		Unit1.setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
		egu.addColumn(Unit1);
		GridColumn Value1 = new GridColumn(0, "VALUE1", "值1", 80);
		NumberField nfkd = new NumberField();
		nfkd.setAllowBlank(false);
		nfkd.setDecimalPrecision(2);
		Value1.setEditor(nfkd);
		Value1.setUpdate(true);
		Value1.setDataType(GridColumn.DataType_Float);
		egu.addColumn(Value1);

		GridColumn ColName2 = new GridColumn(0, "COLNAME2", "字段名2", 90);
		ColName2.setDataType(GridColumn.DataType_String);
		ColName2.setUpdate(true);
		ColName2.setHidden(true);
		egu.addColumn(ColName2);
		GridColumn Item2 = new GridColumn(0, "ITEM2", "项目2", 200);
		Item2.setDataType(GridColumn.DataType_String);
		Item2.setUpdate(true);
		Item2.setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
		egu.addColumn(Item2);
		GridColumn Unit2 = new GridColumn(0, "UNIT2", "单位2", 80);
		Unit2.setDataType(GridColumn.DataType_String);
		Unit2.setUpdate(true);
		Unit2.setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
		egu.addColumn(Unit2);
		GridColumn Value2 = new GridColumn(0, "VALUE2", "值2", 80);
		NumberField nfkd2 = new NumberField();
		nfkd2.setAllowBlank(false);
		nfkd2.setDecimalPrecision(2);
		Value2.setEditor(nfkd);
		Value2.setUpdate(true);
		Value2.setDataType(GridColumn.DataType_Float);
		egu.addColumn(Value2);
		// 取得grid数据
		int rows = getGridDataRows(con, diancxxb_id);

		String BeforeEditScript = "gridDiv_grid.on('beforeedit',function(e){";
		String countScript = "function countEval(e){";
		String afterEditScript = "gridDiv_grid.on('afteredit',countEval);";
		String afterEditGSScript = "";
		String[][] data = null;
		if (rows >= 0) {
			data = new String[rows][8];
			String[] varCss = getGridData(con, diancxxb_id, rows, col_even,
					data, CurrODate);
			Value2.setRenderer(varCss[1]);
			countScript += varCss[0];
			BeforeEditScript += varCss[2];
			afterEditGSScript += varCss[3];
			varCss = getGridData(con, diancxxb_id, rows, col_odd, data, CurrODate);
			Value1.setRenderer(varCss[1]);
			BeforeEditScript += varCss[2];
			afterEditGSScript += varCss[3];
			countScript += varCss[0];
		} else {
			data = new String[][] { { "", "", "", "", "", "", "", "" } };
		}
		BeforeEditScript += "});\n";
		egu.setData(data);
		// /设置显示列名称
		egu.setWidth(Locale.Grid_DefaultWidth);

		egu.addOtherScript(BeforeEditScript);
		countScript += getOtherVarScript(con, diancxxb_id);
		countScript += getCountColSetScript(con, diancxxb_id);
		countScript += afterEditGSScript;
		countScript += ";}\n";
		egu.addOtherScript(countScript);
		egu.addOtherScript(afterEditScript);

		egu.addTbarText("年份");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符

		GridButton gbr = new GridButton("刷新",
				"function (){document.getElementById('RefreshButton').click()}");
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

		// 删除按钮
		GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");

		// 判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if (getZhangt(con)) {
			if(visit.getDiancxxb_id()==112){
				egu.addTbarBtn(gbd);
				egu.addTbarBtn(gbs);
			}else{
				setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
			}
		} else {
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("计划模块", "分厂别总厂显示按钮", this.getTreeid(), "否").equals("否")){
				
			}else{
				egu.addTbarBtn(gbd);
				egu.addTbarBtn(gbs);
//				如果界面中没有数据则显示复制按钮
				String sql = "select s.id zhuangt from niandjh_zhib s\n"
						+ " where  s.diancxxb_id = " + getTreeid() + "\n"
						+ "   and s.riq = " + CurrODate;
				if(!con.getHasIt(sql)){
					String handler="function (){document.getElementById('CpyButton').click();"+
					"Ext.MessageBox.show({msg:'正在处理数据,请稍后...'," +
					"progressText:'处理中...',width:300,wait:true,waitConfig: " +
					"{interval:200},icon:Ext.MessageBox.INFO});}";
					GridButton cpy = new GridButton("复制上年度计划", handler);
					cpy.setIcon(SysConstant.Btn_Icon_Copy);
					egu.addTbarBtn(cpy);
				}
			}
		}

		egu.addOtherScript(getAfterColorScript(con, diancxxb_id, col_even));
		egu.addOtherScript(getAfterColorScript(con, diancxxb_id, col_odd));

		setExtGrid(egu);
		con.Close();
	}

	private int UpdateDataBase(JDBCcon con, String diancxxb_id, String CurrODate, String upsql) {
		int flag = getUpdateSql(con, diancxxb_id, CurrODate, upsql);
		if (flag == -1) {
			setMsg("保存过程发生异常！");
			return -1;
		}
		// 更新当月计算值
		flag = countNianjhzbgs(con, diancxxb_id, CurrODate);
		if (flag == -1) {
			setMsg("保存过程发生异常！");
			return -1;
		}
		// 计算煤折标煤单价，其他费用，及入炉综合标煤单价
		flag = countBMDJ(con, diancxxb_id, CurrODate);
		if (flag == -1) {
			setMsg("保存过程发生异常！");
			return -1;
		}
		return 0;
	}
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		return con.getHasIt(sql);
	}

	private void initData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String sql = "select * from niandjh_zhib where riq = " + CurrODate
				+ " and diancxxb_id =" + diancxxb_id;
		if (!con.getHasIt(sql)) {
			con.getInsert(getInsertSql(diancxxb_id, CurrODate));
		}
		con.Close();
	}

	private double parseDouble(String value) {
		double dv = 0.0;
		try {
			dv = Double.parseDouble(value);
		} catch (Exception e) {
			return dv;
		}
		return dv;
	}

	private boolean isNotNAN(String value) {
		boolean isnotnan = true;
		if (value == null) {
			return false;
		}
		if ("".equals(value)) {
			return false;
		}
		return isnotnan;
	}

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("更新为空！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String upsql;
		initData();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		// 构造更新本月数据的SQL
		upsql = "";
		while (rsl.next()) {
			if (isNotNAN(rsl.getString("COLNAME1"))) {
				upsql += "," + rsl.getString("COLNAME1") + "="
						+ parseDouble(rsl.getString("VALUE1"));
			}
			if (isNotNAN(rsl.getString("COLNAME2"))) {
				upsql += "," + rsl.getString("COLNAME2") + "="
						+ parseDouble(rsl.getString("VALUE2"));
			}
		}
		rsl.close();
		int flag = UpdateDataBase(con, diancxxb_id, CurrODate, upsql);
		con.commit();
		con.Close();
		if (flag == 0) {
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}
	
//	复制功能
	private void CopyData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		String strDate=getNianf()+"-01-01";
		
		String CopySql=
			"INSERT INTO NIANDJH_ZHIB\n" +
			"(ID,DIANCXXB_ID,RIQ,FADL,GONGDMH,FADCYDL,FADBML,GONGRL,GONGRMH,GONGRBML,BIAOMLHJ,\n" + 
			"RUCRLRZC,MEIZBML,MEIZBMDJ,RANYL,YOUZBML,RANYDJ,YOUZBMDJ,QITFY,RLZHBMDJ,ZHUANGT)\n" + 
			"(SELECT GETNEWID("+this.getTreeid()+"),DIANCXXB_ID,ADD_MONTHS(RIQ, 12) RIQ,FADL,GONGDMH,FADCYDL,\n" + 
			"FADBML,GONGRL,GONGRMH,GONGRBML,BIAOMLHJ,RUCRLRZC,MEIZBML,0 MEIZBMDJ,RANYL,YOUZBML,\n" + 
			"RANYDJ,YOUZBMDJ,0 QITFY,0 RLZHBMDJ,0 ZHUANGT\n" + 
			"FROM NIANDJH_ZHIB\n" + 
			"WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n" + 
			"AND DIANCXXB_ID = "+this.getTreeid()+")";

		con.getInsert(CopySql);
// 		每次保存时重新计算指标中的相关参数
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
		setMsg("复制操作完成！");
	}	

	private int countNianjhzbgs(JDBCcon con, String diancxxb_id,
			String CurrODate) {
		String sql = getNianjhzbdySql(diancxxb_id, "gongs", "leijgs");
		ResultSetList rsl = con.getResultSetList(sql);
		String upsql = "";
		while (rsl.next()) {
			upsql = "," + rsl.getString("ZIDM") + "=" + rsl.getString("GONGS");
			getUpdateSql(con, diancxxb_id, CurrODate, upsql);
		}
		rsl.close();
		return 1;
	}

	private int countBMDJ(JDBCcon con, String diancxxb_id, String CurrODate) {
		String upsql = 
			"SELECT MEIZBMDJ,\n" +
			"       ZAFJE,\n" + 
			"       ROUND(DECODE(BIAOMLHJ,0,0,(MEIZBML * MEIZBMDJ + RANYL * RANYDJ + ZAFJE) / BIAOMLHJ),2) RULZHBMDJ\n" + 
			"  FROM (SELECT NVL(CGJH.JIH_REZ, 0) JIH_REZ,\n" + 
			"               NVL(CGJH.JIH_DAOCJ, 0) JIH_DAOCJ,\n" + 
			"               NVL(ZAF.ZAFJE, 0) ZAFJE,\n" + 
			"               NVL(ZHIB.RUCRLRZC, 0) RUCRLRZC,\n" + 
			"               NVL(ZHIB.MEIZBML, 0) MEIZBML,\n" + 
			"               NVL(ZHIB.RANYL, 0) RANYL,\n" + 
			"               NVL(ZHIB.RANYDJ, 0) RANYDJ,\n" + 
			"               NVL(ZHIB.BIAOMLHJ, 0) BIAOMLHJ,\n" + 
			"               ROUND(DECODE((NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0)),0,0,NVL(JIH_DAOCJ, 0) * 29.2712 /(NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0))),2) MEIZBMDJ\n" + 
			"          FROM (SELECT SUM(JIH_SL) JIHSL,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * JIH_REZ) / SUM(JIH_SL)) JIH_REZ,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * (JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS+JIH_QITBHS)) / SUM(JIH_SL)) JIH_DAOCJ\n" + 
			"                  FROM NIANDJH_CAIG\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") CGJH,\n" + 
			"               (SELECT SUM(YUCJE) ZAFJE\n" + 
			"                  FROM NIANDJH_ZAF\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND TRUNC(RIQ, 'yyyy') = "+CurrODate+") ZAF,\n" + 
			"               (SELECT RUCRLRZC, MEIZBML, RANYL, RANYDJ, BIAOMLHJ\n" + 
			"                  FROM NIANDJH_ZHIB\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") ZHIB) SJ";
		
		ResultSetList rsl = con.getResultSetList(upsql);
		
		String updateSql="";
		if(rsl.next()) {
			double MEIZBMDJ=rsl.getDouble("MEIZBMDJ"); 
			double QITFY=rsl.getDouble("ZAFJE"); 
			double RULZHBMDJ=rsl.getDouble("RULZHBMDJ"); 
			updateSql = "update niandjh_zhib set MEIZBMDJ="+MEIZBMDJ+", QITFY="+QITFY+", RLZHBMDJ="+RULZHBMDJ+" where riq = " + CurrODate + " and diancxxb_id=" + diancxxb_id;
		}
		rsl.close();
		if(updateSql.length()>1){
			con.getUpdate(updateSql);
			return 1;
		}else{
			return -1;
		}
		
	}

	private String getNianjhzbdySql(String diancxxb_id, String gongsm) {
		return getNianjhzbdySql(diancxxb_id, gongsm, null);
	}

	private String getNianjhzbdySql(String diancxxb_id, String gongsm,
			String valigongs) {
		String sqltmp = valigongs == null || valigongs.equals("") ? ""
				: " and " + valigongs + " is null ";
		String sql = "select ZIDM,GONGS from JIHZBDYB\n"
				+ "where zhuangt = 1 and " + gongsm + " is not null " + sqltmp
				+ " \n" + " and diancxxb_id =" + diancxxb_id
				+ " and mokmc='年计划' order by bianm";
		return sql;
	}

	private String getInsertSql(String diancxxb_id, String CurrODate) {
		String sql = "insert into niandjh_zhib (id,diancxxb_id,riq) values(getnewid("
				+ diancxxb_id + ")," + diancxxb_id + "," + CurrODate + ")";
		return sql;
	}

	private int getUpdateSql(JDBCcon con, String diancxxb_id, String CurrODate,
			String upsql) {
		upsql = upsql.substring(1);
		upsql = "update niandjh_zhib set " + upsql + " where riq = "
				+ CurrODate + " and diancxxb_id=" + diancxxb_id;
		return con.getUpdate(upsql);
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "年";
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

	private int getGridDataRows(JDBCcon con, String diancxxb_id) {
		int rows = 0;
		String sql = "select nvl(sum(decode(mod(xuh,2),0,1,0)),0) even\n"
				+ ",nvl(sum(mod(xuh,2)),0) odd from JIHZBDYB\n"
				+ "where zhuangt = 1 and mokmc='年计划' and diancxxb_id = "
				+ diancxxb_id;
		ResultSetList rs = con.getResultSetList(sql);
		if (rs.next()) {
			rows = Math.max(rs.getInt("even"), rs.getInt("odd"));
		}
		rs.close();
		return rows;
	}

	private String getColumnValue(JDBCcon con, String date, String diancxxb_id,
			String zidm) {
		String value = "";
		String sql = "select " + zidm + " from niandjh_zhib where diancxxb_id="
				+ diancxxb_id + " and riq = " + date + "";
		ResultSetList rs = con.getResultSetList(sql);
		if (rs == null) {
			return null;
		}
		if (rs.next()) {
			value = rs.getString(zidm);
		}
		rs.close();
		return value;
	}

	private String[] getGridData(JDBCcon con, String diancxxb_id, int rows,
			int col, String[][] data, String CurrODate) {

		String[] GridVar = new String[4];
		String ValueName = "VALUE1";
		int colstart = 0;
		if (col == col_even) {
			colstart = 4;
			ValueName = "VALUE2";
		}
		String sql = "select *\n" + "from JIHZBDYB\n" + "where mod(xuh,2)= "
				+ col + " and  zhuangt = 1 and mokmc='年计划'\n"
				+ "and diancxxb_id = " + diancxxb_id + "\n" + "order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		GridVar[0] = "";
		GridVar[1] = "function(value,metadata,record,rowIndex,colIndex,store){";
		GridVar[2] = "";
		GridVar[3] = "";
		while (rsl.next()) {
			GridVar[0] += "var " + rsl.getString("ZIDM")
					+ "= parseFloat(gridDiv_grid.getStore().getAt("
					+ rsl.getRow() + ").get('" + ValueName + "'));\n"
					+ rsl.getString("ZIDM") + "=eval(" + rsl.getString("ZIDM")
					+ "||0);\n";
			// 如果公式不为空则将其加入计算列，并计算背景色

			if (rsl.getString("GONGS") != null
					&& !"".equals(rsl.getString("GONGS"))) {
				GridVar[1] += "if(rowIndex==" + rsl.getRow()
						+ " && colIndex == " + (colstart + 4)
						+ "){metadata.css='tdTextext2';}\n";
				GridVar[2] += "if(e.row ==" + rsl.getRow() + " && e.column == "
						+ (colstart + 4) + "){e.cancel=true;}\n";
				GridVar[3] += "gridDiv_grid.getStore().getAt(" + rsl.getRow()
						+ ").set('" + ValueName + "',isNaN("
						+ rsl.getString("ZIDM") + ")?0:("
						+ rsl.getString("ZIDM") + "==\"Infinity\"?0:"
						+ rsl.getString("ZIDM") + "));\n";
			}
			String zhi = getColumnValue(con, CurrODate, diancxxb_id, rsl
					.getString("zidm"));
			if (zhi == null) {
				data[rsl.getRow()][colstart] = "";
				data[rsl.getRow()][colstart + 1] = "";
				data[rsl.getRow()][colstart + 2] = "";
				data[rsl.getRow()][colstart + 3] = "";
				continue;
			}
			data[rsl.getRow()][colstart] = rsl.getString("zidm");
			data[rsl.getRow()][colstart + 1] = rsl.getString("mingc");
			data[rsl.getRow()][colstart + 2] = rsl.getString("danw");
			data[rsl.getRow()][colstart + 3] = zhi;
		}
		GridVar[1] += "return value;}";
		if (rows > rsl.getRows())
			for (int i = rsl.getRows(); i < rows; i++) {
				data[i][colstart] = "";
				data[i][colstart + 1] = "";
				data[i][colstart + 2] = "";
				data[i][colstart + 3] = "";
			}
		rsl.close();
		return GridVar;
	}

	private String getAfterColorScript(JDBCcon con, String diancxxb_id, int col) {

		String sql = "select *\n" + "from JIHZBDYB\n" + "where mod(xuh,2)= "
				+ col + " and zhuangt = 1 and mokmc='年计划'\n"
				+ "and diancxxb_id = " + diancxxb_id + "\n" + "order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);

		if (col == col_odd) {
			col = 0;
		} else {
			col = 1;
		}

		StringBuffer script = new StringBuffer();
		script.append("gridDiv_grid.on('afteredit',function(e){\n");
		while (rsl.next()) {
			if (rsl.getString("GONGS") != null
					&& !"".equals(rsl.getString("GONGS"))) {
				script.append("gridDiv_grid.getView().getCell(" + rsl.getRow()
						+ "," + (col * 4 + 4)
						+ ").style.backgroundColor='#E3E3E3';\n");
			}
		}
		script.append("});\n");
		return script.toString();
	}

	private String getOtherVarScript(JDBCcon con, String diancxxb_id) {
		String script = "";
		String sql = "select * from JIHZBDYB where zhuangt = 0 and mokmc='年计划' and diancxxb_id ="
				+ diancxxb_id + " order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			script += "var " + rsl.getString("ZIDM") + "= 0;\n";
		}
		rsl.close();
		return script;
	}

	private String getCountColSetScript(JDBCcon con, String diancxxb_id) {
		String gongs = "";
		String sql = getNianjhzbdySql(diancxxb_id, "gongs");
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			gongs += rsl.getString("ZIDM") + "=eval(" + rsl.getString("GONGS")
					+ ");\n";
		}
		rsl.close();
		return gongs;
	}

}
