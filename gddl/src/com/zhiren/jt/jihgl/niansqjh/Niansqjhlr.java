package com.zhiren.jt.jihgl.niansqjh;

import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.Format;
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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.DateUtil;
//import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Niansqjhlr extends BasePage implements PageValidateListener {
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		System.out.println(getChange());
		visit.getExtGrid1().Save(getChange(), visit);
		setMsg("保存成功!");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
	}

	// 复制上月
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date())-1;
		} else {
			intyear = getNianfValue().getId()-1;
		}
		
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
					+ ")";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";

		}

		String chaxun =" select n.id,n.nianf,d.mingc as diancxxb_id,g.mingc as gongysb_id," +
		"(select c.mingc from chezxxb c where n.faz_id=c.id) as faz_id," +
		"(select c.mingc from chezxxb c where n.daoz_id=c.id) as daoz_id,j.mingc as jihkjb_id," +
		"n.nianjh_fdl,n.nianjh_grl,n.biaomh_fdl,n.biaomh_grl,\n" +
		"n.yjrlmt,n.tianrmh_fdl,n.tianrmh_grl,n.hej, n.fadl," +
		"n.gongrl,n.qit,n.yuns,n.changs,n.rezss,n.quannxyzbm,n.beiz\n" + 
		"from niansqjhb n,diancxxb d,gongysb g,jihkjb j\n" + 
		"where n.diancxxb_id=d.id and n.gongysb_id=g.id and n.jihkjb_id=j.id and n.nianf="+intyear+" "+str;

		
		// System.out.println("复制同期的数据:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(chaxun);
		while (rslcopy.next()) {
			String nianf =rslcopy.getString("nianf");
			String diancxxb_id = rslcopy.getString("diancxxb_id");
			String gongysb_id=rslcopy.getString("gongysb_id");
			String faz_id=rslcopy.getString("faz_id");
			String daoz_id=rslcopy.getString("daoz_id");
			String jihkjb_id=rslcopy.getString("jihkjb_id");
			double nianjh_fdl =rslcopy.getDouble("nianjh_fdl");
			double nianjh_grl =rslcopy.getDouble("nianjh_grl");
			double biaomh_fdl =rslcopy.getDouble("biaomh_fdl");
			double biaomh_grl =rslcopy.getDouble("biaomh_grl");
			double yjrlmt =rslcopy.getDouble("yjrlmt");
			double tianrmh_fdl =rslcopy.getDouble("tianrmh_fdl");
			double tianrmh_grl =rslcopy.getDouble("tianrmh_grl");
			double hej =rslcopy.getDouble("hej");
			double fadl =rslcopy.getDouble("fadl");
			double gongrl =rslcopy.getDouble("gongrl");
			double qit =rslcopy.getDouble("qit");
			double yuns =rslcopy.getDouble("yuns");
			double changs =rslcopy.getDouble("changs");
			double rezss =rslcopy.getDouble("rezss");
			double quannxyzbm =rslcopy.getDouble("quannxyzbm");
			String beiz =rslcopy.getString("beiz");
			

			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			con
					.getInsert("insert into niansqjhb(ID,NIANF,DIANCXXB_ID,GONGYSB_ID,FAZ_ID,DAOZ_ID,JIHKJB_ID,NIANJH_FDL,NIANJH_GRL," +
							"BIAOMH_FDL,BIAOMH_GRL,YJRLMT,TIANRMH_FDL,TIANRMH_GRL,HEJ,FADL,GONGRL," +
							"QIT,YUNS,CHANGS,REZSS,QUANNXYZBM,BEIZ) values("
							+
							_id
							+ ","
							+ nianf
							+ ",(select id from diancxxb where mingc='"
							+ diancxxb_id
							+ "'),(select id from gongysb where mingc='"
							+ gongysb_id
							+ "'),(select id from chezxxb where mingc='"
							+ faz_id
							+ "'),(select id from chezxxb where mingc='"
							+ daoz_id
							+ "'),(select id from jihkjb where mingc='"
							+ jihkjb_id
							+ "'),"
							+ nianjh_fdl
							+ ","
							+ nianjh_grl
							+ ","
							+ biaomh_fdl
							+ ","
							+ biaomh_grl
							+ ","
							+ yjrlmt
							+ ","
							+ tianrmh_fdl
							+ ","
							+ tianrmh_grl
							+ ","
							+ hej
							+ ","
							+ fadl
							+ ","
							+ gongrl
							+ ","
							+ qit
							+ ","
							+ yuns
							+ ","
							+ changs
							+ ","
							+ rezss
							+ ","
							+ quannxyzbm
							+ ",'" + beiz + "')");

		}
		getSelectData(null);
		con.Close();
	}

	/*private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}*/

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
//		long intMonth;
//		if (getYuefValue() == null) {
//			intMonth = DateUtil.getMonth(new Date());
//		} else {
//			intMonth = getYuefValue().getId();
//		}
//		// 当月份是1的时候显示01,
//		String StrMonth = "";
//		if (intMonth < 10) {
//
//			StrMonth = "0" + intMonth;
//		} else {
//			StrMonth = "" + intMonth;
//		}
		// -----------------------------------
		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
					+ ")";
			str = "  and (d.fuid=  " +this.getTreeid()+" or d.shangjgsid="+this.getTreeid()+")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";

		}
		if (rsl == null) {
			String chaxun =" select n.id,n.nianf,d.mingc as diancxxb_id,g.mingc as gongysb_id," +
			"(select c.mingc from chezxxb c where n.faz_id=c.id) as faz_id," +
			"(select c.mingc from chezxxb c where n.daoz_id=c.id) as daoz_id,j.mingc as jihkjb_id," +
			"n.nianjh_fdl,n.nianjh_grl,n.biaomh_fdl,n.biaomh_grl,\n" +
			"n.yjrlmt,n.tianrmh_fdl,n.tianrmh_grl,n.hej, n.fadl," +
			"n.gongrl,n.qit,n.yuns,n.changs,n.rezss,n.quannxyzbm,n.beiz\n" + 
			"from niansqjhb n,diancxxb d,gongysb g,jihkjb j\n" + 
			"where n.diancxxb_id=d.id and n.gongysb_id=g.id and n.jihkjb_id=j.id and n.nianf="+intyear+" "+str;

			// System.out.println(chaxun);
			rsl = con.getResultSetList(chaxun);
		}
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("niansqjhb");

		egu.getColumn("nianf").setHeader("年份");
		egu.getColumn("nianf").setHidden(true);
		egu.getColumn("nianf").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("nianjh_fdl").setHeader("年计划<br>发电量");
		egu.getColumn("nianjh_grl").setHeader("年计划<br>供热量");
		egu.getColumn("biaomh_fdl").setHeader("计划标煤耗<br>发电量");
		egu.getColumn("biaomh_grl").setHeader("计划标煤耗<br>供热量");
		egu.getColumn("yjrlmt").setHeader("预计入炉<br>煤炭热值");
		egu.getColumn("tianrmh_fdl").setHeader("计划天然煤耗<br>发电量");
		egu.getColumn("tianrmh_grl").setHeader("计划天然煤耗<br>供热量");
		//***********计算字段开始*************//
		egu.getColumn("hej").setHeader("合计");
		egu.getColumn("hej").setEditor(null);
		egu.getColumn("fadl").setHeader("发电量");
		egu.getColumn("fadl").setEditor(null);
		egu.getColumn("gongrl").setHeader("供热量");
		egu.getColumn("gongrl").setEditor(null);
		egu.getColumn("quannxyzbm").setHeader("全年需用<br>折标煤");
		egu.getColumn("quannxyzbm").setEditor(null);
		//***********计算字段结束*************//
		egu.getColumn("qit").setHeader("其他");
		egu.getColumn("yuns").setHeader("运损");
		egu.getColumn("changs").setHeader("场损");
		egu.getColumn("rezss").setHeader("热值损失");
		egu.getColumn("beiz").setHeader("备注");

		//设定不可编辑列的颜色
		//egu.getColumn("tianrmh_fdl").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		//egu.getColumn("tianrmh_grl").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hej").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fadl").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("gongrl").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("quannxyzbm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		// 设定列初始宽度
		egu.getColumn("nianf").setWidth(60);
		egu.getColumn("diancxxb_id").setWidth(80);
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("nianjh_fdl").setWidth(60);
		egu.getColumn("nianjh_grl").setWidth(60);
		egu.getColumn("biaomh_fdl").setWidth(80);
		egu.getColumn("biaomh_grl").setWidth(80);
		egu.getColumn("yjrlmt").setWidth(80);
		egu.getColumn("tianrmh_fdl").setWidth(80);
		egu.getColumn("tianrmh_grl").setWidth(80);
		egu.getColumn("hej").setWidth(60);
		egu.getColumn("fadl").setWidth(60);
		egu.getColumn("gongrl").setWidth(60);
		egu.getColumn("qit").setWidth(60);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("changs").setWidth(60);
		egu.getColumn("rezss").setWidth(80);
		egu.getColumn("quannxyzbm").setWidth(80);
		egu.getColumn("beiz").setWidth(60);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(100);// 设置分页
		egu.setWidth(1020);// 设置页面的宽度,当超过这个宽度时显示滚动条

		// ********************R*********************设置默认值****************************
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
							"select id,mingc from diancxxb where(fuid="
									+ getTreeid() + " or shangjgsid ="+getTreeid()+") order by mingc"));
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
			egu.getColumn("diancxxb_id").setReturnId(true);
		}

		// 设置日期的默认值,
		egu.getColumn("nianf").setDefaultValue(""+intyear);
		
//		 *************************下拉框*****************************************88
		// 设置供应商的下拉框
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		String GongysSql = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		// 设置发站下拉框
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql = "select id ,mingc from chezxxb c where c.leib='车站' order by c.mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		egu.getColumn("faz_id").setReturnId(true);
		// 设置到站下拉框
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);

		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		//设置到站下拉框
		ComboBox cb_jihkj = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(cb_jihkj);
		cb_daoz.setEditable(true);

		String jihkjSql = "select id,mingc from jihkjb order by mingc";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
		

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符
		

		// 设置树
		egu.addTbarText("单位:");
		System.out.println(getTreeid());
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// 设定工具栏下拉框自动刷新
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		if (showBtn) {
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("复制同期数据", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}

		// ---------------页面js的计算开始------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			sb.append("e.record.set('FADL',parseFloat(e.record.get('NIANJH_FDL')==''?0:e.record.get('NIANJH_FDL'))*parseFloat(e.record.get('TIANRMH_FDL')==''?0:e.record.get('TIANRMH_FDL'))/1000000);");	
			sb.append("e.record.set('GONGRL',parseFloat(e.record.get('NIANJH_GRL')==''?0:e.record.get('NIANJH_GRL'))*parseFloat(e.record.get('TIANRMH_GRL')==''?0:e.record.get('TIANRMH_GRL'))/1000000);");	
			sb.append("e.record.set('HEJ',parseFloat(e.record.get('FADL')==''?0:e.record.get('FADL'))" +
					"+parseFloat(e.record.get('GONGRL')==''?0:e.record.get('GONGRL'))" +
					"+parseFloat(e.record.get('QIT')==''?0:e.record.get('QIT'))" +
					"+parseFloat(e.record.get('YUNS')==''?0:e.record.get('YUNS'))" +
					"+parseFloat(e.record.get('CHANGS')==''?0:e.record.get('CHANGS'))" +
					"+parseFloat(e.record.get('REZSS')==''?0:e.record.get('REZSS')));");

			sb.append("e.record.set('QUANNXYZBM',parseFloat(e.record.get('HEJ')==''?0:e.record.get('HEJ'))/7000/parseFloat(e.record.get('YJRLMT')==''?0:e.record.get('YJRLMT')));");
			
			
		sb.append("});");
		
		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
//		sb.append("if (e.row==7){e.cancel=true;}");//设定第8列不可编辑,索引是从0开始的
//		sb.append(" if(e.field=='GONGYSMC'){ e.cancel=true;}");//设定供应商列不可编辑
//		sb.append(" if(e.field=='MINGC'){ e.cancel=true;}");//设定电厂信息列不可编辑
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
			//供应商，煤矿，车站
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();

		}
		getSelectData(null);

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
		cn.Close();
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}

	//集联下拉框内容，供应商，煤矿，车站
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