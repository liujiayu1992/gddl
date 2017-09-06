package com.zhiren.dc.huaygl.huaysh.shenhht;
/*
* 作者：赵胜男
* 时间：2012-11-28
* 适用范围：只限于酒泉电厂
* 描述：	要求“审核回退“下“化验编号”取消截取的化验编号，直接从zhilb中取得，
*             加参数判断
*/
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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Shenhcl;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ersht extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
//	绑定日期
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	// 绑定日期
	boolean riqchange1 = false;

	private String riq1;

	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}

	public void setRiq1(String riq1) {

		if (this.riq1 != null && !this.riq1.equals(riq1)) {
			this.riq1 = riq1;
			riqchange1 = true;
		}

	}
	
	private boolean riqchange2 = false;//结束日期是否变化
	
	private String riq2;
	public String getRiq2(){
		if(riq2 == null || riq2.equals("")){
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq2){
		if(this.riq2 != null && !this.riq2.equals(riq2)){
			this.riq2 = riq2;
			riqchange2 = true;
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			riq1 = null;
			riqchange1 = false;
			riq2 = null;
			riqchange2 = false;
			
			
		}
		if(riqchange1 || riqchange2){
			riqchange1 = false;
			riqchange2 = false;
			this.setJincpcModel(null);
			this.setJincpcValue(null);
			
			getSelectData();
		}
		getSelectData();
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
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	//刷新按钮
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		 
		_RefurbishChick = true;
	}

	private boolean _HuitClick = false;
	public void HuitButton(IRequestCycle cycle){
		_HuitClick = true;
	}

	public void submit(IRequestCycle cycle) {

		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if(_HuitClick){
			_HuitClick = false;
			getHuit();
			getSelectData();
		}
	}

	public void getHuit(){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		String dianc = Long.toString(((Visit)getPage().getVisit()).getDiancxxb_id());
		ResultSetList mrsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sb = new StringBuffer("begin \n");
		String zhilbid = "";
		if(mrsl.next()){
			zhilbid = mrsl.getString("id");
		}
		sb.append("update zhillsb  set shenhzt=5 where zhilb_id=").append(zhilbid).append(";\n");
		sb.append("delete from zhilb where id=").append(zhilbid).append(";\n");
		sb.append("end;\n");
		//更改时增加日志
		MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
				"二审申请回退",getExtGrid().mokmc,
				"zhillsb",zhilbid);
		int flag = con.getUpdate(sb.toString());
		if(flag == -1){
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
		setJincpcValue(null);
		setJincpcModel(null);
		
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	public void getSelectData(){
		JDBCcon con = new JDBCcon();
//		String bianm = this.getJincpcValue().getValue();
		String zhillsb = Long.toString(this.getJincpcValue().getId());
		
		String choice = "";
//		if(bianm == "请选择"){
//			choice = "-1 ";
//		}else{
//			choice = bianm;
//		}
		choice = this.getZhillsbid(zhillsb);
		
		String sql = 
			"select z.id,\n" +
			"       m.mingc meikdw,\n" + 
			"       cz.mingc as faz,\n" + 
			"       0 as caiyb_id,\n" +
			"       p.mingc as pinz,\n" + 
			"       f.jingz as shul,\n";
//		if(MainGlobal.getXitxx_item("审核回退", "是否显示原化验数据", "938", "否").equals("是")){
			sql +="  z.huaybh, \n";
//		}else{
//			sql +="       (select max(zl.bianh) as bianh\n" +
//					"          from (select substr(to_char(Sysdate, 'yyyy') ||\n" +
//					"                              decode(to_char(Sysdate, 'yy'),\n" +
//					"                                     substr(max(huaybh), 0, 2),\n" +
//					"                                     substr(max(huaybh), 3),\n" +
//					"                                     '00000') + 1,\n" +
//					"                              3) as bianh\n" +
//					"                  from zhilb) zl) as huaybh,\n";
//		}


                sql +="   z.huaysj,\n" +
                        "to_char(z.qnet_ar,'90.99') qnet_ar,"+
                        "to_char(z.aar,'90.99') aar,"+
                        "to_char(z.ad,'90.99') ad,"+
                        "to_char(z.vdaf,'90.99') vdaf,"+
                        "to_char(z.mt,'90.9') mt,"+
                        "to_char(z.stad,'90.99') stad,"+
                        "to_char(z.aad,'90.99') aad,"+
                        "to_char(z.mad,'90.99') mad,"+
                        "to_char(round_new(z.qbad,2),'90.99') qbad,"+
                        "to_char(z.had,'90.99') had,"+
                        "to_char(z.vad,'90.99') vad,"+
                        "to_char(z.fcad,'90.99') fcad,"+
                        "to_char(z.std,'90.99') std,"+
                        "to_char(round_new(z.qgrad,2),'90.99') qgrad,"+
                        "to_char(z.hdaf,'90.99') hdaf,"+
                        "to_char(round_new(z.qgrad_daf,2),'90.99') qgrad_daf,"+
                        "to_char(z.sdaf,'90.99') sdaf,"+
			"       z.t1,\n" + 
			"       z.t2,\n" + 
			"       z.t3,\n" + 
			"       z.t4,\n" + 
			"       GetHuayy(z.id) as huayy,\n" + 
			"       GetLury(z.id) as lury,\n" + 
			"       GetBeiz(z.id) as beiz\n" + 
			"  from (select l.id as id,\n" +
			"z.bianm huaybh," +
			"               l.huaysj as huaysj,\n" + 
			"               l.qnet_ar qnet_ar,\n" + 
			"               l.aar aar,\n" + 
			"               l.ad ad,\n" + 
			"               l.vdaf vdaf,\n" + 
			"               l.mt mt,\n" + 
			"               l.stad stad,\n" + 
			"               l.aad aad,\n" + 
			"               l.mad mad,\n" +
			"               l.qbad qbad,\n" + 
			"               l.had had,\n" + 
			"               l.vad vad,\n" + 
			"               l.fcad fcad,\n" + 
			"               l.std std,\n" + 
			"               l.qgrad qgrad,\n" + 
			"               l.hdaf hdaf,\n" + 
			"               l.qgrad_daf qgrad_daf,\n" + 
			"               l.sdaf sdaf,\n" + 
			"               l.t1 t1,\n" + 
			"               l.t2 t2,\n" + 
			"               l.t3 t3,\n" + 
			"               l.t4 t4\n" + 
			"          from zhilb l left join zhillsb s on l.id=s.zhilb_id left join zhuanmb z on z.zhillsb_id=s.id and z.zhuanmlb_id=100663\n" +
			"         where l.id in ("+zhillsb+") and l.shenhzt = 1 \n" + 
			"         order by l.id, l.huaysj) z,\n" + 
			"      --caiyb c,\n" +
			"       (select sum(laimsl) as jingz, meikxxb_id, zhilb_id,\n" + 
			"        pinzb_id,faz_id\n" + 
			"          from fahb\n" + 
			"         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n" + 
			"       meikxxb m,\n" + 
			"       pinzb p, chezxxb cz\n" + 
			" where --f.zhilb_id = c.zhilb_id\n" +
			"    f.zhilb_id = z.id\n" +
			"  -- and c.zhilb_id = z.id\n" +
			"   and f.meikxxb_id = m.id\n" + 
			"   and f.pinzb_id = p.id   and f.faz_id=cz.id\n";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setTableName("zhilb");
		egu.setMokmc("二审回退");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("shul").setHeader("数量(吨)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("收到基地位热量<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("aar").setHeader("收到基灰分<p>Aar(%)</p>");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("干燥基灰分<p>Ad(%)</p>");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("干燥无灰基挥发分<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("全水分<p>Mt(%)</p>");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("stad").setHeader("空气干燥基全硫<p>St,ad(%)</p>");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("空气干燥基灰分<p>Aad(%)</p>");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("空气干燥基水分<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("空气干燥基挥发分<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("固定碳<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("干燥基全硫<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("空气干燥基高位热值<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("干燥无灰基氢<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基高位热值<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("干燥无灰基全硫<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("t1").setHeader("T1(℃)");
		egu.getColumn("t1").setEditor(null);
		egu.getColumn("t2").setHeader("T2(℃)");
		egu.getColumn("t2").setEditor(null);
		egu.getColumn("t3").setHeader("T3(℃)");
		egu.getColumn("t3").setEditor(null);
		egu.getColumn("t4").setHeader("T4(℃)");
		egu.getColumn("t4").setEditor(null);
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("化验录入员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setHeader("化验备注");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("huaybh").setWidth(130);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		egu.addPaging(25);
		
		egu.addTbarText("-");
		egu.addTbarText("起始日期");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		egu.addTbarText("截止日期");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");
		
		GridButton refurbish = new GridButton("查看",
		"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton("取消审核", GridButton.ButtonType_Sel,"HuitButton");
		
		egu.addTbarText("-");
		egu.addTbarText("进厂批次号:");
		ComboBox shij = new ComboBox();
		shij.setTransform("JincpcSelect");
		shij.setWidth(150);
		shij.setListeners("select:function(own,rec,index){Ext.getDom('JincpcSelect').selectedIndex=index;document.forms[0].submit();}");
		egu.addToolbarItem(shij.getScript());
		setExtGrid(egu);
		con.Close();
	}


	public String getZhillsbid(String zhilsb){
		JDBCcon con = new JDBCcon();
		String lisb_id ="";
		String sql = "select id from zhillsb where zhilb_id="+zhilsb;
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			lisb_id = rsl.getString("id");
		}else{
			lisb_id = "-1";
		}
		rsl.close();
		con.Close();
		return lisb_id;
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

	
	public IDropDownBean getJincpcValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getJincpcModel().getOptionCount()>0) {
				setJincpcValue((IDropDownBean)getJincpcModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setJincpcValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getJincpcModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setJincpcModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setJincpcModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setJincpcModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct z.zhilb_id, m.bianm\n" +
                " from fahb f,zhillsb z,zhuanmb m\n" +
                " where f.zhilb_id = z.zhilb_id \n" +
                " and m.zhillsb_id=z.id\n" +
                " and z.shenhzt=7  \n" +
                " and m.zhuanmlb_id=100663\n")
//		sb.append("select distinct c.zhilb_id,c.bianm from caiyb c,fahb f,zhillsb z\n")
//		.append(" where f.zhilb_id = c.zhilb_id and c.zhilb_id = z.zhilb_id and z.shenhzt=7  \n")
		.append(" and f.daohrq>=").append(DateUtil.FormatOracleDate(getRiq1()))
		.append(" and f.daohrq <=").append(DateUtil.FormatOracleDate(getRiq2()))
		.append(" order by m.bianm");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"请选择"));
		setJincpcModel(new IDropDownModel(list,sb));
	}
	
}
