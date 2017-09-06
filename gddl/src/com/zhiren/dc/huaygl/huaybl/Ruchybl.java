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


public class Ruchybl extends BasePage implements PageValidateListener {
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
			"select nvl(zhilb.id,0)zhilb_id0,fahb.zhilb_id,\n" +
			"to_char(fahb.daohrq,'yyyy-mm-dd')daohrq,\n" +
			"g.mingc mkmc,\n" +
			"pinzb.mingc pinz,\n" +
			"fahb.biaoz,\n" + 
			"zhilb.mt,--1\n" + 
			"zhilb.mad,--2\n" + 
			"zhilb.aad,--3\n" + 
			"zhilb.vad,--4\n" + 
			"zhilb.fcad,--5\n" + 
			"zhilb.stad,--6\n" + 
			"zhilb.qnet_ar,--7\n" + 
			"  round_new(zhilb.aad * (100 - zhilb.mt) / (100 - zhilb.mad),2)aar,--aar收到基\n" + 
			"  round_new(zhilb.aad  * 100 / (100 - zhilb.mad),2)ad,--ad 干燥机\n" + 
			"  round_new(zhilb.vad * 100 / (100 - zhilb.mad -  zhilb.aad ),2)vdaf,--vadf空干无灰基\n" + 
			"  round_new(zhilb.vad * (100 - zhilb.mt) / (100 - zhilb.mad),2)var,--42var收到基\n" + 
			"  round_new(zhilb.stad  * 100 / (100 - zhilb.mad),2)std,--61std干燥机\n" + 
			"  round_new(zhilb.stad * 100 / (100 - zhilb.mad -  zhilb.aad ),2)sdaf,--stad空干基\n" + 
			"zhilb.hdaf,zhilb.had,zhilb.qgrad_daf,zhilb.qgrad,zhilb.qbad\n" + 
			"from fahb,zhilb,meikxxb g,pinzb\n" + 
			"where fahb.pinzb_id=pinzb.id and fahb.zhilb_id=zhilb.id(+) and fahb.meikxxb_id=g.id " +
			"and to_char(fahb.daohrq,'yyyy-mm-dd')>='"+getRiq1()+"' and to_char(fahb.daohrq,'yyyy-mm-dd')<='"+getRiq2()+"' order by fahb.id";
	
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,"Ruchybl");
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
		egu.getColumn("daohrq").setWidth(100);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("mkmc").setHeader("矿别");
		egu.getColumn("mkmc").setWidth(100);
		egu.getColumn("mkmc").setEditor(null);
		egu.getColumn("mkmc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(70);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("biaoz").setHeader("运单量(吨)");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("mt").setHeader("全水<br>Mt(%)");
		egu.getColumn("mt").setWidth(70);
		egu.getColumn("mad").setHeader("空干基水<br>Mad(%)");
		egu.getColumn("mad").setWidth(70);
		egu.getColumn("aad").setHeader("空干基灰分<br>Aad(%)");
		egu.getColumn("aad").setWidth(70);
		egu.getColumn("vad").setHeader("空干基<br>挥发分<br>Vad(%)");
		egu.getColumn("vad").setWidth(70);
		egu.getColumn("fcad").setHeader("固定碳<br>fcad(%)");
		egu.getColumn("fcad").setWidth(70);
		egu.getColumn("stad").setHeader("空干基<br>硫份<br>stad(%)");
		egu.getColumn("stad").setWidth(70);
		egu.getColumn("qnet_ar").setHeader("收到基<br>发热量<br>qnet_ar(Mj/Kg)");
		egu.getColumn("qnet_ar").setWidth(80);
		//导出
		egu.getColumn("aar").setHeader("收到基<br>灰分<br>aar(%)");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("aar").setWidth(70);
		egu.getColumn("aar").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("ad").setHeader("干燥基<br>灰分<br>ad(%)");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("ad").setWidth(70);
		egu.getColumn("ad").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("vdaf").setHeader("干燥无灰基<br>挥发分<br>vdaf(%)");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("vdaf").setWidth(70);
		egu.getColumn("vdaf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("var").setHeader("收到基<br>挥发分<br>var(%)");
		egu.getColumn("var").setEditor(null);
		egu.getColumn("var").setWidth(70);
		egu.getColumn("var").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("std").setHeader("干燥基<br>硫份<br>std(%)");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("std").setWidth(70);
		egu.getColumn("std").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("sdaf").setHeader("干燥无灰基<br>硫份<br>sdaf(%)");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("sdaf").setWidth(70);
		egu.getColumn("sdaf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		//可选
		egu.getColumn("hdaf").setHeader("干燥无灰基<br>氢<br>hdaf(%)");
		egu.getColumn("hdaf").setWidth(70);
		egu.getColumn("had").setHeader("空干基<br>氢<br>had(%)");
		egu.getColumn("had").setWidth(70);
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基<br>高位热值<br>qgrad_daf(Mj/Kg)");
		egu.getColumn("qgrad_daf").setWidth(90);
		egu.getColumn("qgrad").setHeader("高位热值<br>qgrad(Mj/Kg)");
		egu.getColumn("qgrad").setWidth(70);
		egu.getColumn("qbad").setHeader("弹桶热量<br>qbad(Mj/Kg)");
		egu.getColumn("qbad").setWidth(70);
		 
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
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		con.Close();
	}

	public void Save(String strchange) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="";
		String strMs="";

		ResultSetList cMrs = getExtGrid().getModifyResultSet(strchange);
		while (cMrs.next()) {
			long zhilb_id0 = Long.parseLong(cMrs.getString("zhilb_id0"));
			long zhilb_id = Long.parseLong(cMrs.getString("zhilb_id"));
			String  huaybh = cMrs.getString("zhilb_id");
			
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
				sql="begin \n" +
				"insert into zhilb\n" +
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
				"   star,BEIZ)\n" + 
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
				"   "+star+",'补录数据');\n"+
				"update zhillsb set shenhzt=7 where zhilb_id ="+zhilb_id+";\n end;";
				con.getUpdate(sql);

			}else{//对已经存在化验值进行更改

				sql="update zhilb\n" +
				"   set qnet_ar = "+qnet_ar+",\n" + 
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
				"       star = "+star+",\n" +
				"		beiz='补录数据'\n" + 
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