package com.zhiren.shanxdted.meigzxbb;

import java.util.Calendar;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Yunsdwxypjb extends BasePage  implements PageValidateListener{

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	public boolean getRaw() {
		return true;
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		setMsg(null);
		return getKuangcrzc();
	}

	private String getKuangcrzc(){
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
		sql.append(
				"select dq.mingc, m.mingc, c.mingc, sum(f.biaoz), sum(f.biaoz)/100, sum(f.jingz),\n" +
				"       sum(f.biaoz)*m.yunsl, sum(f.biaoz)-sum(f.jingz),\n" + 
				"       sum(f.biaoz)*m.yunsl- sum(f.biaoz)-sum(f.jingz),\n" + 
				"       sum(f.biaoz)*m.yunsl- sum(f.biaoz)-sum(f.jingz) + 100,\n" + 
				"       decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/4.1816*1000,0)),\n" + 
				"       0,\n" + 
				"       0,\n" + 
				"       0,\n" + 
				"       0\n" + 
				"\n" + 
				"  from fahb f, meikxxb m, zhilb z, meikdqb dq,\n" + 
				"       (select c.fahb_id,y.mingc from chepb c, yunsdwb y\n" + 
				"         where c.yunsdwb_id=y.id and to_char(c.zhongcsj,'yyyy-mm-dd')>='" + beginRiq + "'\n" + 
				"           and to_char(c.zhongcsj,'yyyy-mm-dd')<='" + endRiq + "'\n" + 
				"         group by c.fahb_id,y.mingc ) c\n" + 
				" where f.meikxxb_id=m.id\n" + 
				"   and f.zhilb_id=z.id\n" + 
				"   and f.id=c.fahb_id\n" + 
				"   and m.meikdq_id=dq.id\n" + 
				"	and m.jihkjb_id=2\n" +
				" group by dq.mingc,m.mingc,c.mingc,m.yunsl\n" + 
				" order by dq.mingc,m.mingc,c.mingc,m.yunsl");
		ResultSetList rs = con.getResultSetList(sql.toString());
		Report rt = new Report();
		String[][] ArrHeader = null;
		ArrHeader=new String[][] {{"地区","煤矿名称","承运单位","矿发数量","量分值","到厂数量","合理运损","损差","运损分值","到厂热值","合同热值","热值差","亏卡分值","总分值","信誉度"}};
		int ArrWidth[]=new int[] {60,220,220,80,80,80,80,80,80,80,80,80,80,80,80,80};
		rt.setTitle(beginRiq+"至"+endRiq+"直购煤承运车队信誉评价表", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);

//		rt.body.ShowZero=true;
		rt.body.mergeFixedRow();	
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}
//
//	public String getPageHome() {
//		if (((Visit) getPage().getVisit()).getboolean1()) {
//			return "window.location = '" + MainGlobal.getHomeContext(this)
//					+ "';";
//		} else {
//			return "";
//		}
//	}

	private boolean beginChange = false;

	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil
					.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra
					.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		if (value != null) {
			if (!value.equals(((Visit) getPage().getVisit()).getString4())) {
				beginChange = true;
			} else {
				beginChange = false;
			}
		}
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		if(beginChange){
			((Visit) getPage().getVisit()).setString5(getBeginriqDate());
		}else{
			((Visit) getPage().getVisit()).setString5(value);
		}
		
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		 df.Binding("daohrq1","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
		if(beginChange){
			df1.setValue(this.getBeginriqDate());
		}else{
			df1.setValue(this.getEndriqDate());
		}
		df1.Binding("daohrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setMsg(null);
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setExtTree1(null);
			visit.setString4(null);
			visit.setString5(null);
		}
		getToolbars();
		blnIsBegin = true;

	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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
}