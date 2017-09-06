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
 *日期:2010-4-13 11:37:07
 *描述:1.增大每一行的宽度,设置成正好打印一张纸
 *     2.设定合计行float显示2位小数
 */
/**
 * @author yinjm
 * 煤炭销售票据报表
 */

public class MeitxspjReport extends BasePage implements PageValidateListener {
	
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
	
	private String Markbh = "true"; // 标记编号下拉框是否被选择
	
	public String getMarkbh() {
		return Markbh;
	}

	public void setMarkbh(String markbh) {
		Markbh = markbh;
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
		String sql = "select distinct mk.id, mk.mingc from meitxspjb pj, meikxxb mk " +
			"where pj.meikxxb_id = mk.id and pj.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')";
		setMeikdwModel(new IDropDownModel(sql, "请选择"));
	}
//	煤矿单位下拉框_结束
	
//	编号下拉框_开始
	public IDropDownBean getBianhValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getBianhModel().getOptionCount() > 0) {
				setBianhValue((IDropDownBean)getBianhModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setBianhValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getBianhModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getBianhModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setBianhModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getBianhModels() {
		String sql = "select rownum as xuh,bianh from (select distinct  pj.bianh from meitxspjb pj, meikxxb mk " +
			"where pj.meikxxb_id = mk.id and pj.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd') and mk.id = " + getMeikdwValue().getId()+")";
		setBianhModel(new IDropDownModel(sql, "请选择"));
	}
//	编号下拉框_结束
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String sql = 
			"select pj.piaoh, pj.cheh, pj.shul\n" +
			"  from meitxspjb pj\n" + 
			" where pj.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and pj.meikxxb_id = "+ getMeikdwValue().getId() +"\n" + 
			"   and pj.bianh = '"+ getBianhValue().getValue() +"'\n" +
			" order by pj.piaoh";
		
		ResultSetList rslData =  con.getResultSetList(sql);
		String[][] ArrHeader = new String[1][8];
		ArrHeader[0] = new String[]{"序号", "票号", "车号", "数量", "序号", "票号", "车号", "数量"};
		int[] ArrWidth = new int[] {50, 100, 100, 90, 50, 100, 100, 90};
		
		rt.setTitle("山西省煤炭销售票(公路内销)"+ getYear(getRiq()) +"年"+ getMonth(getRiq()) +"月回收明细表", ArrWidth);

		Table tb = new Table(1 + getPages(rslData.getRows()) * 26, 8);
		
		if (rslData.getRows() != 0) {
			String[][] strData = new String[rslData.getRows()][3];
			
			int temp = 0;
			while(rslData.next()) {
				strData[temp][0] = rslData.getString("piaoh");
				strData[temp][1] = rslData.getString("cheh");
				strData[temp][2] = rslData.getString("shul");
				temp ++;
			}
			
			for (int page = 0; page < getPages(rslData.getRows()); page ++) {
				int left_xuh = 1;
				int right_xuh = 26;
				for (int i = 2 + (page * 26); i <= (page + 1) * 26; i ++) {
					tb.setCellValue(i, 1, String.valueOf(left_xuh ++));
					tb.setCellAlign(i, 1, Table.ALIGN_CENTER);
					tb.setCellValue(i, 5, String.valueOf(right_xuh ++));
					tb.setCellAlign(i, 5, Table.ALIGN_CENTER);
				}
				tb.setCellValue((page + 1) * 27 - page, 5, "合计");
				tb.setCellAlign((page + 1) * 27 - page, 5, Table.ALIGN_CENTER);
			}
			
			int index = 0;
			for (int page = 0; page < getPages(rslData.getRows()); page ++) {
				float sum_ches = 0.0f;
				for (int i = 2 + (page * 26); i <= (page + 1) * 26; i ++) {
					if (strData.length - (50 * page) > i - (2 + (page * 26))) {
						tb.setCellValue(i, 2, strData[page * 25 + index][0]);
						tb.setCellAlign(i, 2, Table.ALIGN_CENTER);
						tb.setCellValue(i, 3, strData[page * 25 + index][1]);
						tb.setCellAlign(i, 3, Table.ALIGN_CENTER);
						tb.setCellValue(i, 4, strData[page * 25 + index][2]);
						sum_ches += Float.parseFloat(strData[page * 25 + index][2]);
						tb.setCellAlign(i, 4, Table.ALIGN_CENTER);
					}
					if (strData.length - (50 * page) > i - (2 + (page * 26)) + 25) {
						tb.setCellValue(i, 6, strData[page * 25 + index + 25][0]);
						tb.setCellAlign(i, 6, Table.ALIGN_CENTER);
						tb.setCellValue(i, 7, strData[page * 25 + index + 25][1]);
						tb.setCellAlign(i, 7, Table.ALIGN_CENTER);
						tb.setCellValue(i, 8, strData[page * 25 + index + 25][2]);
						sum_ches += Float.parseFloat(strData[page * 25 + index + 25][2]);
						tb.setCellAlign(i, 8, Table.ALIGN_CENTER);
					}
					index ++;
				}
				//设置float型显示两位小数
				BigDecimal b = new BigDecimal(sum_ches); 
				float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
				
				tb.setCellValue((page + 1) * 27 - page, 8, String.valueOf(f1));
				tb.setCellAlign((page + 1) * 27 - page, 8, Table.ALIGN_CENTER);
			}
		}
		
		tb.setWidth(ArrWidth);
		tb.setHeaderData(ArrHeader);
		tb.setPageRows(26);
		rt.setBody(tb);
		rt.setDefaultTitle(1, 3, "煤矿单位：" + getMeikdwmc(getMeikdwValue().getValue()), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, "时间：" + DateUtil.Formatdate("yyyy年MM月dd日", getDate(getRiq())), Table.ALIGN_CENTER);
		rt.setDefaultTitle(7, 2, "编号：" + getBianh(getBianhValue().getValue()), Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "制表：", Table.ALIGN_LEFT);
		for(int i=1;i<=rt.body.getRows();i++){
			rt.body.setRowHeight(i, 32);
		}
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
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
			"document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		mkdw.setLazyRender(true);
		mkdw.setEditable(false);
		tbr.addField(mkdw);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("编号："));
		ComboBox bh = new ComboBox();
		bh.setTransform("Bianh");
		bh.setWidth(100);
		bh.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false'; document.forms[0].submit();}");
		bh.setLazyRender(true);
		bh.setEditable(false);
		tbr.addField(bh);
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
			getBianhModels();
		}
		if (getMarkbh().equals("true")) { // 判断如果getMarkbh()返回"true"，那么重新初始化编号下拉框
			getBianhModels();
		}
		getSelectData();
	}
}