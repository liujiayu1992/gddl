package com.zhiren.dc.huaygl.huaysh.rulhysh;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：罗朱平
 * 时间：2009-11-27
 * 描述：入炉化验审核回退sql添加根据rulmzlb中diancxxb_id过滤。
 */
/*
 * 作者：夏峥
 * 时间：2013-11-26
 * 描述：调整资源显示名称将Qgad,daf变更为Qgar,daf
 */

/*
 * 作者：陈环红
 * 时间：2017-01-19
 * 描述：选择第一个日期的时候，页面不刷新，改为自动刷新
 */
public class Rulshht extends BasePage implements PageValidateListener {
	private static final String Rulshht="Rulshht";//默认
	private static final String Rultjht="Rultjht";
	private static String value=Rulshht;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
//	绑定日期
//	private String FormatDate(Date _date) {
//		if (_date == null) {
//			return "";
//		}
//		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
//	}
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

			riq1 = null;
			riqchange1 = false;
			riq2 = null;
			riqchange2 = false;
			String lx = cycle.getRequestContext().getParameter("lx");
			if (Rultjht.equals(lx)) {
				this.value=Rultjht;
			}
			
		}
		if(riqchange1 || riqchange2){
			riqchange1 = false;
			riqchange2 = false;

			
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
		String dianc = Long.toString(((Visit)getPage().getVisit()).getDiancxxb_id());
		ResultSetList mrsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sb = new StringBuffer("begin \n");
		String zhilbid = "";
		if(mrsl.next()){
			zhilbid = mrsl.getString("id");
		}
		if(value.equals(Rultjht)){
			sb.append("update rulmzlb  set shenhzt=0  where id=").append(zhilbid).append(";\n");
		}else{
			sb.append("update rulmzlb  set shenhzt=1  where id=").append(zhilbid).append(";\n");
		}
		sb.append("end;\n");
		int flag = con.getUpdate(sb.toString());
		if(flag == -1){
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();

		
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		String bianm = this.getJincpcValue().getValue();
		String briq = DateUtil.FormatOracleDate(getRiq1());
		String eriq = DateUtil.FormatOracleDate(getRiq2());

		
		StringBuffer sql = new StringBuffer();

			sql.append("select distinct r.id,\n" +
			"       to_char(r.rulrq, 'yyyy-mm-dd') as rulrq,\n" + 
			"       to_char(r.fenxrq, 'yyyy-mm-dd') as fenxrq,\n" + 
			"       rb.mingc as rulbzb_id,\n" + 
			"       j.mingc as jizfzb_id,\n" + 
			"       r.qnet_ar,\n" + 
			"       r.aar,\n" + 
			"       r.ad,\n" + 
			"       r.vdaf,\n" + 
			"       r.mt,\n" + 
			"       r.stad,\n" + 
			"       r.aad,\n" + 
			"       r.mad,\n" + 
			"       r.qbad,\n" + 
			"       r.had,\n" + 
			"       r.vad,\n" + 
			"       r.fcad,\n" + 
			"       r.std,\n" + 
			"       r.qgrad,\n" + 
			"       r.hdaf,\n" + 
			"       r.sdaf,\n" + 
			"       r.var,\n" + 
			"       r.har,\n" + 
			"       r.qgrd,\n" + 
			"       r.qgrad_daf,\n" + 
			"       r.huayy,\n" + 
			"       r.lury,\n" + 
			"       to_char(r.lursj, 'yyyy-mm-dd') as lursj,\n" + 
			"       r.bianm,\n" + 
			"       r.beiz\n" + 
			"  from rulmzlb r, rulbzb rb, jizfzb j, diancxxb d\n" + 
			" where r.rulbzb_id = rb.id\n" + 
			"   and r.jizfzb_id = j.id\n" + 
			"   and (d.id=r.diancxxb_id or d.fuid=r.diancxxb_id) \n" +
			"	and (d.id="+visit.getDiancxxb_id()+" or d.fuid="+visit.getDiancxxb_id()+")\n");
			if(value.equals(Rultjht)){
				sql.append("   and r.shenhzt = 1\n");
			}else{
				sql.append("   and r.shenhzt = 3\n"); 
			}
			sql.append("   and  r.fenxrq >="+briq +
			"   and r.fenxrq<="+eriq );
			sql.append(" order by rulrq");


		ResultSetList rsl = con.getResultSetList(sql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setTableName("zhilb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulrq").setHeader("入炉日期");
		egu.getColumn("fenxrq").setEditor(null);
		egu.getColumn("fenxrq").setHeader("分析日期");
		egu.getColumn("rulbzb_id").setHeader("入炉班组");
		egu.getColumn("rulbzb_id").setEditor(null);
		egu.getColumn("jizfzb_id").setHeader("机组");
		egu.getColumn("jizfzb_id").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("Qnet,ar(Mj/kg)");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("aar").setHeader("Aar(%)");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("Ad(%)");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("Vdaf(%)");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("stad").setHeader("St,ad(%)");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("Aad(%)");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("qbad").setHeader("Qb,ad(Mj/kg)");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("Had(%)");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("FCad(%)");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("St,d(%)");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("Qgr,ad(Mj/kg)");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("Hdaf(%)");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("Qgar,daf(Mj/kg)");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("Sdaf(%)");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("var").setHeader("Var(%)");
		egu.getColumn("var").setEditor(null);
		egu.getColumn("har").setHeader("Har(%)");
		egu.getColumn("har").setEditor(null);
		egu.getColumn("qgrd").setHeader("Qgrd(%)");
		egu.getColumn("qgrd").setEditor(null);
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("化验录入员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("beiz").setHeader("化验备注");
		egu.getColumn("beiz").setEditor(null);

		// //设置列宽度
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("var").setWidth(80);
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
		
//		GridButton refurbish = new GridButton("刷新",
//		"function (){document.getElementById('RefurbishButton').click();}");
//		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
//		egu.addTbarBtn(refurbish);
//		egu.addTbarText("-");
		if(value.equals(Rultjht)){
			egu.addToolbarButton("取消提交", GridButton.ButtonType_Sel,"HuitButton");
		}else{
			egu.addToolbarButton("取消审核", GridButton.ButtonType_Sel,"HuitButton");
		}
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

	

	
}

