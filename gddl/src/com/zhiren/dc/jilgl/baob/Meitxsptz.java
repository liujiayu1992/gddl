package com.zhiren.dc.jilgl.baob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import java.math.*;


/*作者:王总兵
 *日期:2010-5-16 9:51:11
 *描述:阳城煤炭销售票逐日登记台帐
 * 
 * 
 */

public class Meitxsptz extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}
	
	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	
	
	
	private String Markmk = "true"; // 标记煤矿单位下拉框是否被选择
	
	public String getMarkmk() {
		return Markmk;
	}

	public void setMarkmk(String markmk) {
		Markmk = markmk;
	}
	
	

	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
//	煤矿单位下拉框_开始
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = 

			
			"select distinct m.id,m.mingc from meitxspjb p,meikxxb m\n" + 
			"where p.meikxxb_id=m.id\n" + 
			"and p.riq=to_date('"+getRiq()+"','yyyy-mm-dd')";


		setMeikdwModel(new IDropDownModel(sql, "请选择"));
	}
//	煤矿单位下拉框_结束

	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		long meik=this.getMeikdwValue().getId();
		
		
		
		String sql = "";

			
	
			sql=


//同一天同一个矿最多只能有1000车的销售票,超过1000车因为case when里没有统计,所以会数据计算错误
				"select --a.xuh,a.fenz,\n" +
				"decode(a.riq,null,'本页合计',a.riq) as riq,a.mingc as mingc,\n" + 
				"decode(a.riq,null,'',max(a.cheh)) as cheh,sum(a.shul) as shul,'' as tan,\n" + 
				"sum(a.shul1) as shul1,sum(a.zhi) as zhi,'' as fuzr\n" + 
				" from (\n" + 
				" select  (case\n" + 
				"    when 0<rownum/50 and rownum/50<=1 then 0\n" + 
				"    when 1<rownum/50 and rownum/50<=2 then 1\n" + 
				"    when 2<rownum/50 and rownum/50<=3 then 2\n" + 
				"    when 3<rownum/50 and rownum/50<=4 then 3\n" + 
				"    when 4<rownum/50 and rownum/50<=5 then 4\n" + 
				"    when 5<rownum/50 and rownum/50<=6 then 5\n" + 
				"    when 6<rownum/50 and rownum/50<=7 then 6\n" + 
				"    when 7<rownum/50 and rownum/50<=8 then 7\n" + 
				"    when 8<rownum/50 and rownum/50<=9 then 8\n" + 
				"    when 9<rownum/50 and rownum/50<=10 then 9\n" + 
				"    when 10<rownum/50 and rownum/50<=11 then 10\n" + 
				"    when 11<rownum/50 and rownum/50<=12 then 11\n" + 
				"    when 12<rownum/50 and rownum/50<=13 then 12\n" + 
				"    when 13<rownum/50 and rownum/50<=14 then 13\n" + 
				"    when 14<rownum/50 and rownum/50<=15 then 14\n" + 
				"    when 15<rownum/50 and rownum/50<=16 then 15\n" + 
				"    when 16<rownum/50 and rownum/50<=17 then 16\n" + 
				"    when 17<rownum/50 and rownum/50<=18 then 17\n" + 
				"    when 18<rownum/50 and rownum/50<=19 then 18\n" + 
				"    when 19<rownum/50 and rownum/50<=20 then 19\n" + 
				" else 20\n" + 
				" end) as fenz,\n" + 
				" rownum as xuh,\n" + 
				"  to_char(m.riq,'yyyy-mm-dd') as riq,mk.mingc,m.cheh,m.shul as shul,\n" + 
				"  '' as tan,m.shul as shul1,'1' as zhi,'' as fuzr\n" + 
				"from meitxspjb  m,meikxxb mk\n" + 
				"\t\t\t\twhere m.riq=to_date('"+getRiq()+"','yyyy-mm-dd')\n" + 
				"\t\t\t\t and m.meikxxb_id=mk.id\n" + 
				"\t\t\t\tand m.meikxxb_id="+meik+" ) a\n" + 
				" group by rollup(a.fenz,a.riq,a.mingc,a.xuh)\n" + 
				" having not (grouping(a.xuh)+grouping(a.mingc)=1 or grouping(a.xuh)+grouping(a.riq)=1 or\n" + 
				"  grouping(a.xuh)+grouping(a.mingc)+grouping(a.fenz)+grouping(a.riq)=4)\n" + 
				" order by a.fenz,a.xuh";

			

			String sql2=
				"select to_char(m.riq,'yyyy-mm-dd') as riq,sum(m.shul) as shul,count(*) as zhi\n" +
				"from meitxspjb  m,meikxxb mk\n" + 
				" where m.riq=to_date('"+getRiq()+"','yyyy-mm-dd')\n" + 
				" and m.meikxxb_id=mk.id\n" + 
				"and m.meikxxb_id="+meik+"\n" + 
				"group by rollup (m.riq)\n" + 
				"having not (grouping(m.riq)=1)";



		ResultSetList rs =  con.getResultSetList(sql);
		ResultSetList rs2 =  con.getResultSetList(sql2);
		String[][] ArrHeader = new String[2][8];
		ArrHeader[0] = new String[]{"日期", "煤(炭)<br>来源企业", "购买(吨)", "购买(吨)", "购买(吨)","实回收煤炭销售票","实回收煤炭销售票","回收负责人<br>签字"};
		ArrHeader[1] = new String[]{"日期", "煤(炭)<br>来源企业", "车号", "煤", "碳","吨","支","回收负责人<br>签字"};
		int[] ArrWidth = new int[] {90, 120, 90, 80, 70,70,60,80};
		

		rt.setTitle("晋城市终端用煤企业购煤炭逐日登记台帐", ArrWidth);
		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		//rt.setDefaultTitle(1, 3, "日期:"+getRiq(),Table.ALIGN_LEFT);
		
		
	

		rt.setBody(new Table(rs, 2, 0, 2));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		for(int i=1;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.setPageRows(51);
		rt.body.merge(1, 1, 2, 1);
		rt.body.merge(1, 2, 2, 2);
		rt.body.merge(1, 3, 1, 5);
		rt.body.merge(1, 6, 2, 7);
		rt.body.merge(1, 8, 2, 8);
		//rt.body.mergeFixedCols();
		//rt.body.mergeFixedRow();
		
		String riq1="";
		if(rt.body.getRows()>3){
			riq1=rt.body.getCellValue(3, 1);
		}
		for (int i=4;i<=rt.body.getRows();i++){
			String riq2=rt.body.getCellValue(i, 1);
			if(riq2.equals(riq1)){
				
				//分页时,只有第一行显示日期和煤矿名称,其它行全清空
				  if((i-3)%51!=0){
					  rt.body.setCellValue(i, 1, "");
					  rt.body.setCellValue(i, 2, "");
					  
				  }
				  
			}
			
		}
		
		String leij="";
		String zhi="";
		while(rs2.next()){
			leij=String.valueOf(rs2.getDouble("shul"));
			zhi=String.valueOf(rs2.getLong("zhi"));
			
		}
		
		
		rt.createDefautlFooter(ArrWidth);
        rt.setDefautlFooter(1, 5, " &nbsp;&nbsp;&nbsp;&nbsp;累&nbsp;&nbsp;&nbsp;&nbsp;计:" +
        		"&nbsp;&nbsp;&nbsp;购买煤炭  "+leij+"  吨,回收票据  "+zhi+"   支  "+leij+"    吨;", Table.ALIGN_LEFT);
		
		
		
		
		
		rt.body.setRowHeight(17);
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rs.close();
		rs2.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("日期："));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
//		df.Binding("Riq", "forms[0]");
		df.setListeners("change:function(own,newValue,oldValue){document.getElementById('Riq').value = newValue.dateFormat('Y-m-d'); " +
			"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		
		
		tbr.addText(new ToolbarText("煤矿单位："));
		ComboBox mkdw = new ComboBox();
		mkdw.setTransform("Meikdw");
		mkdw.setWidth(120);
		mkdw.setListeners("select:function(){document.getElementById('Mark_mk').value = 'false'; " +
			" document.forms[0].submit();}");
		mkdw.setLazyRender(true);
		mkdw.setEditable(false);
		tbr.addField(mkdw);
		tbr.addText(new ToolbarText("-"));
		
		
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 *  取得页面要显示的页数
	 * @param rslrows 结果集行数
	 * @return
	 */
	public int getPages(int rslrows) {
		int pages = rslrows / 50;
		if (rslrows % 50 > 0) {
			pages ++;
		}
		return pages;
	}
	
	/**
	 * 将传入的String型日期转换为Data型日期
	 * @param date
	 * @return
	 */
	public static Date getDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date tempdate = new Date();
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			tempdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tempdate;
	}
	
	/**
	 * 获取传日期的年份
	 * @param date
	 * @return
	 */
	public int getYear(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int year = DateUtil.getYear(new Date());
		try {
			Date tempdate = sdf.parse(date);
			year = DateUtil.getYear(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return year;
	}
	
	/**
	 * 获取传日期的月份
	 * @param date
	 * @return
	 */
	public int getMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int month = DateUtil.getMonth(new Date());
		try {
			Date tempdate = sdf.parse(date);
			month = DateUtil.getMonth(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}
	
	/**
	 * 获取煤矿单位名称
	 * @param mkmc
	 * @return
	 */
	public String getMeikdwmc(String mkmc) {
		if (!mkmc.equals("请选择")){
			return mkmc;
		} else {
			return "";
		}
	}
	
	/**
	 * 获取编号
	 * @param mkmc
	 * @return
	 */
	public String getBianh(String bh) {
		if (!bh.equals("请选择")){
			return bh;
		} else {
			return "";
		}
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			
			visit.setProSelectionModel3(null); // 煤矿单位下拉框
			visit.setDropDownBean3(null);
			visit.setProSelectionModel2(null); // 编号下拉框
			visit.setDropDownBean2(null);
		}
		if (getMarkmk().equals("true")) { // 判断如果getMarkmk()返回"true"，那么重新初始化煤矿单位下拉框和编号下拉框
			getMeikdwModels();
			
		}
		
		getSelectData();
	}
}