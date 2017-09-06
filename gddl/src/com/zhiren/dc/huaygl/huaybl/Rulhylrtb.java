package com.zhiren.dc.huaygl.huaybl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 修改时间：2009-09-18
 * 修改人：  ww
 * 修改内容：
 * 			1 增加厂别判断
 * 			在获取元素分析时没有加入厂别的判断，无法获取元素分析项目的值
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '分厂同矿氢值不同',
 '是',
 '',
 '化验',
 1,
 '使用'
 )
 
 *          2 添加采样时间,默认不显示
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '化验录入显示采样日期',
 '是',
 '',
 '化验',
 1,
 '使用'
 )
 */

/*作者:王总兵
 * 时间:2009-10-26 10:33:14
 * 修改内容:刷新加入制样日期的判断,只有宣威电厂用这种方法,
 *         暂时对其它电厂不适用
 */

/* 修改时间:2010-01-25 
 * 人员：liht
 * 修改内容:化验时间录入时由原来年月日基础上精确到时、分
 */
/*
 * 作者：夏峥
 * 时间：2012-09-20
 * 描述：将不可编辑字段使用灰色背景区分
 * 		 调整界面宽度
 * 		 查询时使用登录用户所对应的电厂标识对所选内容进行筛分
 */

public class Rulhylrtb extends BasePage implements PageValidateListener {
//	 绑定日期
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			setRiq1(DateUtil.FormatDate(new Date()));
		}
		return riq1;
	}
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}

	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}
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

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String condition="AND RULMZLB.DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n";
		
		String sql = "";
		sql =
			"select rulmzlb.id,fenxrq, rulbzb.mingc||jizfzb.mingc xiangm,\n" +
			"mt,--1\n" + 
			"mad,--2\n" + 
			"aad,--3\n" + 
			"vad,--4\n" + 
			"fcad,--5\n" + 
			"stad,--6\n" + 
			"qnet_ar,--7\n" + 
			"  round_new(aad * (100 - mt) / (100 - mad),2)aar,--31aar收到基\n" + 
			"  round_new(aad  * 100 / (100 - mad),2)ad,--32ad 干燥机\n" + 
			"  round_new(vad * 100 / (100 - mad -  aad ),2)vdaf,--41vadf空感悟汇集\n" + 
			"  round_new(vad * (100 - mt) / (100 - mad),2)var,--42var收到基\n" + 
			"  round_new(stad  * 100 / (100 - mad),2)std,--61std干燥机\n" + 
			"  round_new(stad * 100 / (100 - mad -  aad ),2)sdaf,--62stdaf空感悟汇集\n" + 
			"----------------\n" + 
			"hdaf,had,qgrad_daf,qgrad,qbad\n" + 
			"from rulmzlb,rulbzb,jizfzb\n" + 
			"where rulmzlb.rulbzb_id=rulbzb.id \n" +
			"and rulmzlb.jizfzb_id=jizfzb.id \n" +
			"and to_char(rulmzlb.rulrq,'yyyy-mm-dd')>='"+getRiq1()+"' \n" +
			"and to_char(rulmzlb.rulrq,'yyyy-mm-dd')<='"+getRiq2()+"' \n" +
			condition+
			"ORDER BY FENXRQ ";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("rulmzlb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth("bodyWidth");
//		 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// /设置显示列名称
		egu.getColumn("id").setHeader("隐含1");
		egu.getColumn("id").hidden=true;
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("fenxrq").setHeader("分析日期");
		egu.getColumn("fenxrq").setWidth(70);
		egu.getColumn("fenxrq").setEditor(null);
		egu.getColumn("xiangm").setHeader("班组/机组");
		egu.getColumn("xiangm").setWidth(120);
		egu.getColumn("xiangm").setEditor(null);
		 
		egu.getColumn("mt").setHeader("全水<br>Mt(%)");
		egu.getColumn("mt").setWidth(70);
		egu.getColumn("mad").setHeader("空干基水<br>Mad(%)");
		egu.getColumn("mad").setWidth(70);
		egu.getColumn("aad").setHeader("空干基灰分<br>Aad(%)");
		egu.getColumn("aad").setWidth(70);
		egu.getColumn("vad").setHeader("空干基挥发分<br>Vad(%)");
		egu.getColumn("vad").setWidth(70);
		egu.getColumn("fcad").setHeader("固定碳<br>(%)");
		egu.getColumn("fcad").setWidth(70);
		egu.getColumn("stad").setHeader("空干基硫份<br>(%)");
		egu.getColumn("stad").setWidth(70);
		egu.getColumn("qnet_ar").setHeader("收到基发热量<br>(MJ/Kg)");
		egu.getColumn("qnet_ar").setWidth(80);
		//导出
		egu.getColumn("aar").setHeader("收到基灰分<br>(%)");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("aar").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("ad").setHeader("干燥基灰分<br>(%)");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("ad").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("vdaf").setHeader("干燥无灰基<br>挥发分(%)");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("vdaf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("var").setHeader("收到基<br>挥发分(%)");
		egu.getColumn("var").setEditor(null);
		egu.getColumn("var").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("std").setHeader("干燥基<br>硫份(%)");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("std").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("sdaf").setHeader("干燥无灰基<br>硫份(%)");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("sdaf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		//可选
		egu.getColumn("hdaf").setHeader("干燥无灰基<br>氢(%)");
		egu.getColumn("had").setHeader("空干基<br>氢(%)");
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基<br>高位热值(MJ/Kg)");
		egu.getColumn("qgrad").setHeader("高位热值<br>(MJ/Kg)");
		egu.getColumn("qbad").setHeader("弹桶热量<br>(MJ/Kg)");
		 
//		到货日期查询
		egu.addTbarText("入炉日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq1());
		df.Binding("riq1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		egu.addTbarText("至:");
		DateField df1 = new DateField();
//		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
//		GridButton baocButton = new GridButton("保存",
//		"function (){document.getElementById('SaveButton').click();}");
//		baocButton.setIcon(SysConstant.Btn_Icon_Save);
//		egu.addTbarBtn(baocButton);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		con.Close();
	}

	public void Save(String strchange) {
		/*zhilb.aad * (100 - zhilb.mt) / (100 - zhilb.mad)aar,--31aar收到基
		zhilb.aad  * 100 / (100 - zhilb.mad)ad,--32ad 干燥机
		zhilb.vad * 100 / (100 - zhilb.mad -  zhilb.aad )vdaf,--41vadf空感悟汇集
		zhilb.vad * (100 - zhilb.mt) / (100 - zhilb.mad)var,--42var收到基
		zhilb.stad  * 100 / (100 - zhilb.mad)std,--61std干燥机
		zhilb.stad * 100 / (100 - zhilb.mad -  zhilb.aad )sdaf,--62stdaf空感悟汇集*/
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="";
		String strMs="";
//		if(strchange.equals("")){strMs="没有任何更改！";setMsg(strMs);return;};
		ResultSetList cMrs = getExtGrid().getModifyResultSet(strchange);
		while (cMrs.next()) {
			long id = Long.parseLong(cMrs.getString("id"));
			double mt = cMrs.getDouble("mt");
			double mad = cMrs.getDouble("mad");
			double aad = cMrs.getDouble("aad");
			double vad = cMrs.getDouble("vad");
			double fcad = cMrs.getDouble("fcad");
			double stad = cMrs.getDouble("stad");
			double qnet_ar = cMrs.getDouble("qnet_ar");
			
			double aar=CustomMaths.Round_new(aad* (100 - mt) / (100 - mad), 2);
			double ad=CustomMaths.Round_new(aad  * 100 / (100 - mad),2);
			double vdaf=CustomMaths.Round_new(vad * 100 / (100 - mad -  aad ),2);
			double var=CustomMaths.Round_new(vad * (100 - mt) / (100 - mad),2);
			double std=CustomMaths.Round_new(stad  * 100 / (100 - mad),2);
			double sdaf=CustomMaths.Round_new(stad * 100 / (100 - mad -  aad ),2);
//			double star=CustomMaths.Round_new(stad* (100 - mt) / (100 - mad),2);
			
			double hdaf = cMrs.getDouble("hdaf");
			double had = cMrs.getDouble("had");
			double qgrad_daf = cMrs.getDouble("qgrad_daf");
			double qgrad = cMrs.getDouble("qgrad");
			double qbad = cMrs.getDouble("qbad");
			sql="update rulmzlb\n" +
			"   set shenhzt=3, "+
			"       qnet_ar = "+qnet_ar+",\n" + 
			"       aar = "+aar+",\n" + 
			"       ad = "+ad+",\n" + 
			"       vdaf = "+vdaf+",\n" + 
			"       mt = "+mt+",\n" + 
			"       stad = "+stad+",\n" + 
			"       aad = "+aad+",\n" + 
			"       mad = "+mad+",\n" + 
			"       qbad = "+qbad+",\n" + 
			"       had = "+had+",\n" + 
			"       vad = "+vad+",\n" + 
			"       fcad = "+fcad+",\n" + 
			"       std = "+std+",\n" + 
			"       qgrad = "+qgrad+",\n" + 
			"       hdaf = "+hdaf+",\n" + 
			"       qgrad_daf = "+qgrad_daf+",\n" + 
			"       sdaf = "+sdaf+",\n" + 
			"       var = "+var+"\n" + 
			//"       star = "+star+"\n" + 
			" where id = "+id;
			con.getUpdate(sql);
		}
		con.Close();
		strMs="保存成功！";
		setMsg(strMs);
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	 
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
 

	public void submit(IRequestCycle cycle) {
 
		if (_SaveChick) {
			_SaveChick = false;
			Save(getChange());
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
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

	public String gridId;

	public int pagsize;

	public int getPagSize() {
		return pagsize;
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
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
			setExtGrid(null);
			visit.setString6(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}