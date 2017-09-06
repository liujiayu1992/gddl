package com.zhiren.gangkjy.taiz;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


public class Zhuangczltz  extends BasePage implements PageValidateListener{

	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}



//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			this.getSelectData();
		}
		isBegin=true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;

	/**
	 * 发电集团电煤信息日报表
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String jiaql = "z.zhuangcl";//加权量
		StringBuffer strSQL = new StringBuffer();
		String sql = 	
			"select  decode(grouping(z.ligsj)+grouping(l.mingc),2,'合计',1,'日小计',to_char(z.ligsj,'yyyy-MM-dd')) as ligrq,\n" +
			"        l.mingc as chuanm,\n" + 
			"        x.mingc as liux,\n" + 
			"        z.hangc,\n" + 
			"        sum(z.zhuangcl) as zhuangcl,\n" + 
			"        ls.huaybh,\n" + 
			"        b.mingc as jianydw,\n" + 
			"        to_char(ls.huaysj,'yyyy-MM-dd') as huayrq,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.qnet_ar*"+jiaql+")/sum("+jiaql+"),2)) as qnet_ar,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.mt*"+jiaql+")/sum("+jiaql+"),2)) as mt,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.mad*"+jiaql+")/sum("+jiaql+"),2)) as mad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.ad*"+jiaql+")/sum("+jiaql+"),2)) as ad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.aad*"+jiaql+")/sum("+jiaql+"),2)) as aad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.vad*"+jiaql+")/sum("+jiaql+"),2)) as vad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.vdaf*"+jiaql+")/sum("+jiaql+"),2)) as vdaf,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.std*"+jiaql+")/sum("+jiaql+"),2)) as std,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.stad*"+jiaql+")/sum("+jiaql+"),2)) as stad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.had*"+jiaql+")/sum("+jiaql+"),2)) as had,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.qgrad*"+jiaql+")/sum("+jiaql+"),2)) as qgrad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.qgrad_daf*"+jiaql+")/sum("+jiaql+"),2)) as qgr_daf,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.t1*"+jiaql+")/sum("+jiaql+"),2)) as dt,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.t2*"+jiaql+")/sum("+jiaql+"),2)) as st,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.t3*"+jiaql+")/sum("+jiaql+"),2)) as ht,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.t4*"+jiaql+")/sum("+jiaql+"),2)) as ft,\n" + 
			"        ls.huayy,\n" + 
			"        ls.shenhry,\n" + 
			"        null\n" + 
			"from zhuangcb z,luncxxb l,vwxuqdw x,zhilb zl,zhillsb ls,caiyb c,\n" + 
			"     (select t.id,t.mingc from item t, itemsort s\n" + 
			"      where t.itemsortid = s.id and s.mingc = '质量检验单位') b\n" + 
			"where z.luncxxb_id = l.id\n" + 
			"      and z.xiaosgysb_id = x.id\n" + 
			"      and z.zhilb_id = zl.id\n" + 
			"      and ls.zhilb_id = zl.id\n" + 
			"      and ls.caiyb_id = c.id\n" + 
			"      and c.songjdwb_id = b.id\n" + 
			" 	   and z.ligsj>="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   and z.ligsj<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"	   and z.diancxxb_id = " + visit.getDiancxxb_id() + "\n" +
			"group by rollup (z.ligsj,l.mingc,x.mingc,z.hangc,ls.huaybh,b.mingc,ls.huaysj,ls.huayy,ls.shenhry)\n" + 
			"having not (grouping(z.ligsj)||grouping(ls.shenhry))=1\n" + 
			"order by grouping(z.ligsj) desc,z.ligsj,grouping(l.mingc),l.mingc,x.mingc,z.hangc,ls.huaybh,b.mingc,ls.huaysj,ls.huayy,ls.shenhry\n";

		strSQL.append(sql);
		
		String ArrHeader[][]=new String[2][27];
		ArrHeader[0]=new String[] {Local.ligsj_zhuangcb,Local.chuanm_zhuangcb,Local.liux_zhuangcb,Local.hangc_zhuangcb,Local.zhuangcl_zhuangcb,Local.huaydh,Local.jianydw,Local.huayrq,Local.diwfrl,Local.shuif,Local.shuif,Local.huif,Local.huif,Local.huiff,Local.huiff,Local.quanl,Local.quanl,Local.qing,Local.gaowfrl,Local.gaowfrl,Local.huird,Local.huird,Local.huird,Local.huird,Local.jianyr,Local.shenh,Local.pizhun};
		ArrHeader[1]=new String[] {Local.ligsj_zhuangcb,Local.chuanm_zhuangcb,Local.liux_zhuangcb,Local.hangc_zhuangcb,Local.zhuangcl_zhuangcb,Local.huaydh,Local.jianydw,Local.huayrq,"Qnet,ar(MJ/kg)","Mt","Mad","Ad","Aad","Vad","Vdaf","St,d","St,ad","Had","ad","vdaf","DT","ST","HT","FT",Local.jianyr,Local.shenh,Local.pizhun};

		int ArrWidth[]=new int[] {120,100,80,40,40,60,120,100,39,30,30,30,30,30,30,30,30,30,39,39,30,30,30,30,45,60,60};
		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		Table tb=new Table(rs, 2, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle("装船质量台帐", ArrWidth);
		rt.setDefaultTitle(1, 27, getBRiq() + " 至 " + getERiq(), Table.ALIGN_CENTER);
		
		//合并单元格
		rt.body.mergeCell(1, 1, 2, 1);
		rt.body.mergeCell(1, 2, 2, 2);
		rt.body.mergeCell(1, 3, 2, 3);
		rt.body.mergeCell(1, 4, 2, 4);
		rt.body.mergeCell(1, 5, 2, 5);
		rt.body.mergeCell(1, 6, 2, 6);
		rt.body.mergeCell(1, 7, 2, 7);
		rt.body.mergeCell(1, 8, 2, 8);
		rt.body.mergeCell(1, 10, 1,11);
		rt.body.mergeCell(1, 12, 1,13);
		rt.body.mergeCell(1, 14, 1,15);
		rt.body.mergeCell(1, 16, 1,17);
		rt.body.mergeCell(1, 19, 1,20);
		rt.body.mergeCell(1, 21, 1,24);
		rt.body.mergeCell(1, 25, 2, 25);
		rt.body.mergeCell(1, 26, 2, 26);
		rt.body.mergeCell(1, 27, 2, 27);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_LEFT);
		rt.body.setColAlign(7, Table.ALIGN_LEFT);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
		rt.body.setColAlign(20, Table.ALIGN_RIGHT);
		rt.body.setColAlign(21, Table.ALIGN_RIGHT);
		rt.body.setColAlign(22, Table.ALIGN_RIGHT);
		rt.body.setColAlign(23, Table.ALIGN_RIGHT);
		rt.body.setColAlign(24, Table.ALIGN_RIGHT);
		rt.body.setColAlign(25, Table.ALIGN_LEFT);
		rt.body.setColAlign(26, Table.ALIGN_LEFT);
		rt.body.setColAlign(27, Table.ALIGN_LEFT);
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
	}
//	******************************************************************************
	
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("离港日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("xiemrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}


}