package com.zhiren.dc.huaygl.huaybb.baob;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhilhyd extends BasePage {

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

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
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
	
		return Huaydreport() ;
	}

	private String Huaydreport() {
		Visit visit = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		String sql=

			"select\n" +
			"       fh.mkmc as meik,\n" + 
			"       zm.bianm as huaybh,\n" + 
			"       decode(sum(fh.biaoz),0,0,round_new(sum(ls.qnet_ar*fh.biaoz)/sum(fh.biaoz),2)) as qnet_ar,\n" + 
			"       decode(sum(fh.biaoz),0,0,round_new(sum(ls.mt*fh.biaoz)/sum(fh.biaoz),2)) as mt,\n" + 
			"       decode(sum(fh.biaoz),0,0,round_new(sum(ls.mad*fh.biaoz)/sum(fh.biaoz),2)) as mad,\n" + 
			"       decode(sum(fh.biaoz),0,0,round_new(sum(ls.aad*fh.biaoz)/sum(fh.biaoz),2)) as aad,\n" + 
			"       decode(sum(fh.biaoz),0,0,round_new(sum(ls.vdaf*fh.biaoz)/sum(fh.biaoz),2)) as vadf,\n" + 
			"       decode(sum(fh.biaoz),0,0,round_new(sum(ls.stad*fh.biaoz)/sum(fh.biaoz),2)) as stad,\n" + 
			"       decode(sum(fh.biaoz),0,0,round_new(sum(ls.had*fh.biaoz)/sum(fh.biaoz),2))  as had,\n" + 
			"       ls.huayy as huayy,\n" + 
			"       ls.huaylb\n" + 
			" from\n" + 
			"    (select zhilb_id,sum(biaoz) biaoz,max(m.mingc) mkmc　from fahb f, meikxxb m\n" + 
			"  where f.meikxxb_id = m.id\n" + 
			"     group by zhilb_id) fh,zhillsb ls,zhuanmb zm,zhuanmlb zb ,diancxxb dc ,fahb f\n" + 
			"  where  fh.zhilb_id = ls.zhilb_id\n" + 
			"     and ls.id = zm.zhillsb_id\n" + 
			"     and zm.zhuanmlb_id=zb.id\n" + 
			"     and zb.jib=3\n" + 
			"     and ls.zhilb_id="+hy+"\n" + 
			"    group by rollup (fh.mkmc,(ls.huayy,ls.huaylb,zm.bianm))\n" + 
			"    having not(grouping(zm.bianm)=1)\n" + 
			"    order by grouping(fh.mkmc) desc ,fh.mkmc ,grouping(ls.huayy) desc,ls.huayy ,\n" + 
			"    grouping (zm.bianm) desc ,grouping(ls.huaylb) desc, ls.huaylb";
		
		ResultSet rs = con.getResultSet(sql);
//		System.out.println(sql);

		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][11];
		ArrHeader[0]=new String[] {"煤矿","化验编码","地位热(Qnet,ar)","全水(Mt)","空干基水(Mad)","空干基灰(Aad)","干燥无灰基挥发分(Vad)","干基硫(Std)","空干基氢(Had)","化验人员","样品类别","化验单"};
		
		int ArrWidth[]=new int[] {120,100,50,50,50,50,50,50,50,80,80};
		

		rt.setTitle("化验单", ArrWidth);
		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 3, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 5, "查询日期:" + getRiq() + "至" + getAfter(),Table.ALIGN_CENTER);
		rt.setDefaultTitle(10, 2, "单位:吨",Table.ALIGN_RIGHT );
		
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.mergeFixedCols();
		for (int i = 1; i <= 11; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "打印日期:"+ DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 5, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(10, 2, "制表:", Table.ALIGN_RIGHT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
	//	rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}
   private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_ReturnChick) {
			_ReturnChick = false;
		}
	}

	public void getSelectData() {

	}
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}
		
	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
	
    private String hy;
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		  hy=cycle.getRequestContext().getParameter("zhilb_id");
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			getSelectData();
		}
		blnIsBegin = true;
		getSelectData();

	}

}
