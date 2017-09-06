package com.zhiren.dc.huaygl.huaybl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ErrorMessage;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.dc.huaygl.Compute;
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
 * 
 * 
 * 
 */

/* 修改时间:2010-01-25 
 * 人员：liht
 * 修改内容:化验时间录入时由原来年月日基础上精确到时、分
 * 
 * 
 * 
 */
public class Ruchylrtb extends BasePage implements PageValidateListener {
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
		JDBCcon con = new JDBCcon();
		String sql = "";
		sql =
			"select nvl(zhilb.id,0)zhilb_id0,fahb.zhilb_id,to_char(fahb.daohrq,'yyyy-mm-dd')daohrq,luncxxb.mingc||'/'||fahb.chec quanm,g.mingc mkmc,pinzb.mingc pinz,fahb.biaoz,\n" + 
			"zhilb.mt,--1\n" + 
			"zhilb.mad,--2\n" + 
			"zhilb.aad,--3\n" + 
			"zhilb.vad,--4\n" + 
			"zhilb.fcad,--5\n" + 
			"zhilb.stad,--6\n" + 
			"zhilb.qnet_ar,--7\n" + 
			"  round_new(zhilb.aad * (100 - zhilb.mt) / (100 - zhilb.mad),2)aar,--31aar收到基\n" + 
			"  round_new(zhilb.aad  * 100 / (100 - zhilb.mad),2)ad,--32ad 干燥机\n" + 
			"  round_new(zhilb.vad * 100 / (100 - zhilb.mad -  zhilb.aad ),2)vdaf,--41vadf空感悟汇集\n" + 
			"  round_new(zhilb.vad * (100 - zhilb.mt) / (100 - zhilb.mad),2)var,--42var收到基\n" + 
			"  round_new(zhilb.stad  * 100 / (100 - zhilb.mad),2)std,--61std干燥机\n" + 
			"  round_new(zhilb.stad * 100 / (100 - zhilb.mad -  zhilb.aad ),2)sdaf,--62stdaf空感悟汇集\n" + 
			"----------------\n" + 
			"zhilb.hdaf,zhilb.had,zhilb.qgrad_daf,zhilb.qgrad,zhilb.qbad\n" + 
			"from fahb,zhilb,luncxxb,meikxxb g,pinzb\n" + 
			"where fahb.pinzb_id=pinzb.id and fahb.zhilb_id=zhilb.id(+) and fahb.luncxxb_id=luncxxb.id and fahb.meikxxb_id=g.id " +
			"and to_char(fahb.daohrq,'yyyy-mm-dd')>='"+getRiq1()+"' and to_char(fahb.daohrq,'yyyy-mm-dd')<='"+getRiq2()+"' order by fahb.id";
	
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("zhilb");
		// 设置页面宽度以便增加滚动条
		egu.setWidth("bodyWidth");
//		 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// /设置显示列名称
		egu.getColumn("zhilb_id0").setHeader("隐含1");
		egu.getColumn("zhilb_id0").hidden=true;
		egu.getColumn("zhilb_id0").setEditor(null);
		egu.getColumn("zhilb_id").setHeader("隐含2");
		egu.getColumn("zhilb_id").hidden=true;
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("quanm").setHeader("船名/航次");
		egu.getColumn("quanm").setWidth(100);
		egu.getColumn("quanm").setEditor(null);
		egu.getColumn("mkmc").setHeader("矿别");
		egu.getColumn("mkmc").setWidth(100);
		egu.getColumn("mkmc").setEditor(null);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(70);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("biaoz").setHeader("运单量");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("mt").setHeader("全水Mt(%)");
		egu.getColumn("mt").setWidth(70);
		egu.getColumn("mad").setHeader("空干基水Mad(%)");
		egu.getColumn("mad").setWidth(70);
		egu.getColumn("aad").setHeader("空干基灰分Aad(%)");
		egu.getColumn("aad").setWidth(70);
		egu.getColumn("vad").setHeader("空干基挥发分Vad(%)");
		egu.getColumn("vad").setWidth(70);
		egu.getColumn("fcad").setHeader("固定碳(%)");
		egu.getColumn("fcad").setWidth(70);
		egu.getColumn("stad").setHeader("空干基硫份(%)");
		egu.getColumn("stad").setWidth(70);
		egu.getColumn("qnet_ar").setHeader("收到基发热量(Mj/Kg)");
		egu.getColumn("qnet_ar").setWidth(70);
		//导出
		egu.getColumn("aar").setHeader("收到基灰分(%)");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("干燥基灰分(%)");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("干燥无灰基挥发分(%)");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("var").setHeader("收到基挥发分(%)");
		egu.getColumn("var").setEditor(null);
		egu.getColumn("std").setHeader("干燥基硫份(%)");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("sdaf").setHeader("干燥无灰基硫份(%)");
		egu.getColumn("sdaf").setEditor(null);
		//可选
		egu.getColumn("hdaf").setHeader("干燥无灰基氢(%)");
		egu.getColumn("had").setHeader("空干基氢(%)");
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基高位热值(Mj/Kg)");
		egu.getColumn("qgrad").setHeader("高位热值(Mj/Kg)");
		egu.getColumn("qbad").setHeader("弹桶热量(Mj/Kg)");
		 
//		到货日期查询
		egu.addTbarText("到货日期:");
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="";
		String strMs="";
//		if(strchange.equals("")){strMs="没有任何更改！";setMsg(strMs);return;};
		ResultSetList cMrs = getExtGrid().getModifyResultSet(strchange);
		while (cMrs.next()) {
			long zhilb_id0 = Long.parseLong(cMrs.getString("zhilb_id0"));
			long zhilb_id = Long.parseLong(cMrs.getString("zhilb_id"));
			String  huaybh = cMrs.getString("daohrq")+cMrs.getString("quanm");
			
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
			double star=CustomMaths.Round_new(stad* (100 - mt) / (100 - mad),2);
			
			double hdaf = cMrs.getDouble("hdaf");
			double had = cMrs.getDouble("had");
			double qgrad_daf = cMrs.getDouble("qgrad_daf");
			double qgrad = cMrs.getDouble("qgrad");
			double qbad = cMrs.getDouble("qbad");
			if(zhilb_id0==0){//如果是第一次录入化验值
				sql="insert into zhilb\n" +
				"  (id,\n" + 
				"   huaybh,\n" + 
				"   caiyb_id,\n" + 
				"   huaysj,\n" + 
				"   qnet_ar,\n" + 
				"   aar,\n" + 
				"   ad,\n" + 
				"   vdaf,\n" + 
				"   mt,\n" + 
				"   stad,\n" + 
				"   aad,\n" + 
				"   mad,\n" + 
				"   qbad,\n" + 
				"   had,\n" + 
				"   vad,\n" + 
				"   fcad,\n" + 
				"   std,\n" + 
				"   qgrad,\n" + 
				"   hdaf,\n" + 
				"   qgrad_daf,\n" + 
				"   sdaf,\n" + 
				"   var,\n" + 
				"   huayy,\n" + 
				"   lury,\n" + 
				"   shenhzt,\n" + 
				"   liucztb_id,\n" + 
				"   star)\n" + 
				"values\n" + 
				"  ("+zhilb_id+",\n" + 
				"   '"+huaybh+"',\n" + 
				"   0,\n" + 
				"   sysdate,\n" + 
				"   "+qnet_ar+",\n" + 
				"   "+aar+",\n" + 
				"   "+ad+",\n" + 
				"   "+vdaf+",\n" + 
				"   "+mt+",\n" + 
				"   "+stad+",\n" + 
				"   "+aad+",\n" + 
				"   "+mad+",\n" + 
				"   "+qbad+",\n" + 
				"   "+had+",\n" + 
				"   "+vad+",\n" + 
				"   "+fcad+",\n" + 
				"   "+std+",\n" + 
				"   "+qgrad+",\n" + 
				"   "+hdaf+",\n" + 
				"   "+qgrad_daf+",\n" + 
				"   "+sdaf+",\n" + 
				"   "+var+",\n" + 
				"   '"+visit.getRenymc()+"',\n" + 
				"    '"+visit.getRenymc()+"',\n" + 
				"   1,\n" + 
				"   1,\n" + 
				"   "+star+")";
				con.getInsert(sql);

			}else{//对已经存在化验值进行更改

				sql="update zhilb\n" +
				"   set  "+
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
				"       var = "+var+",\n" + 
				"       star = "+star+"\n" + 
				" where id = "+zhilb_id0;
				con.getUpdate(sql);
			}
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