package com.zhiren.heiljkhh;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
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

/**
 * @author ly
 *
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

public class Zhoubcx  extends BasePage implements PageValidateListener{

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
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			visit.setList1(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
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

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String sql_dc = "";
		String sql_rq = "";
		String rq = "";
		String sj = "";
		StringBuffer strSQL = new StringBuffer();
		
		String str = "";
//		if (visit.isJTUser()) {
//			str = "";
//		} else {
//			if (visit.isGSUser()) {
//				str = "and (d.id = " + getTreeid_dc() + " or d.fuid = "
//						+ getTreeid_dc() + ")";
//			} else {
//				str = "and d.id = " + getTreeid_dc() + "";
//			}
//		}
		int treejib = this.getDiancTreeJib();
		String sql = "";
		
		if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid_dc() + " or d.fuid = "
					+ getTreeid_dc() + ")";
			sql =
				"select decode(grouping(d.quanc),1,'省公司合计',d.quanc) as diancxxb_id,\n" +
				"       x.riq,\n" + 
				"       sum(laimhj),\n" + 
				"       sum(laim_tp),\n" + 
				"       sum(cbj_tp),\n" + 
				"       sum(laim_df),\n" + 
				"       sum(cbj_df),\n" + 
				"       sum(haoy),\n" + 
				"       sum(kuc),\n" + 
				"       x.beiz\n" + 
				"from diancxxb d,\n" + 
				"(select d.id as diancxxb_id,\n" + 
				"       decode(h.leix,'周','周计','月','月累',to_char(h.riq,'MM')||'.'||to_char(h.riq,'dd')) as riq,\n" + 
				"       h.laimhj,\n" + 
				"       h.laim_tp,\n" + 
				"       h.cbj_tp,\n" + 
				"       h.laim_df,\n" + 
				"       h.cbj_df,\n" + 
				"       h.haoy,\n" + 
				"       h.kuc,\n" + 
				"       GetHeilj_zbbz(h.diancxxb_id,'"+getBRiq()+"') as beiz\n" + 
				"from hlj_zhoub h,diancxxb d,\n" + 
				"     (select TO_CHAR( to_date('" + getBRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" + 
				"       TO_CHAR( to_date('" + getBRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
				"      from dual) w\n" + 
				"where h.diancxxb_id = d.id\n" + 
				   str + "\n" + 
				"      and h.riq>=to_date(w.yi,'yyyy-MM-dd') and h.riq<=to_date(w.ri,'yyyy-MM-dd')) x\n" + 
				"where x.diancxxb_id = d.id\n" + 
				"group by grouping sets ('1',x.riq,(d.quanc,d.xuh,x.riq,x.beiz))\n" + 
				"having not (grouping(d.quanc)+grouping(x.riq)) = 2\n" + 
				"order by grouping(d.quanc) desc,d.xuh,decode(x.riq,'月累',3,'周计',2,1),x.riq";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid_dc() + "";
			
			sql = 
				"select d.quanc as diancxxb_id,\n" + 
				"       decode(h.leix,'周','周计','月','月累',to_char(h.riq,'MM')||'.'||to_char(h.riq,'dd')) as riq,\n" + 
				"       h.laimhj,\n" + 
				"       h.laim_tp,\n" + 
				"       h.cbj_tp,\n" + 
				"       h.laim_df,\n" + 
				"       h.cbj_df,\n" + 
				"       h.haoy,\n" + 
				"       h.kuc,\n" + 
				"       GetHeilj_zbbz(h.diancxxb_id,'"+getBRiq()+"') as beiz\n" + 
				"from hlj_zhoub h,diancxxb d,\n" + 
				"     (select TO_CHAR( to_date('" + getBRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D')) ) + 2,'YYYY-MM-DD' ) yi,\n" + 
				"       TO_CHAR( to_date('" + getBRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D')) ) + 8,'YYYY-MM-DD' ) ri\n" + 
				"      from dual) w\n" + 
				"where h.diancxxb_id = d.id\n" + 
				   str + "\n" + 
				"      and h.riq>=to_date(w.yi,'yyyy-MM-dd') and h.riq<=to_date(w.ri,'yyyy-MM-dd')\n" + 
				"order by d.xuh,decode(h.leix,'日',1,'周',2,'月',3,4),h.riq\n";
		}
		

		strSQL.append(sql);
		
		String ArrHeader[][]=new String[3][10];
		ArrHeader[0]=new String[] {"单 位"," 统 计日 期","来        煤","来        煤","来        煤","来        煤","来        煤","耗 用<br>(吨)","库  存<br>(吨) ","备   注"};
		ArrHeader[1]=new String[] {"单 位"," 统 计日 期","合 计  (吨)","其     中","其     中","其     中","其     中","耗 用<br>(吨)","库  存<br>(吨) ","备   注"};
		ArrHeader[2]=new String[] {"单 位"," 统 计日 期","合 计  (吨)","统 配(吨)","车 板 价 格 <br> (元/吨)","地 煤(吨)","车 板 价 格<br> (元/吨)","耗 用<br>(吨)","库  存<br>(吨) ","备   注"};

		int ArrWidth[]=new int[] {120,80,80,65,65,65,65,65,65,80};
		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		// 数据
		
		Table tb=new Table(rs, 3, 0, 1);
		rt.setBody(tb);
		
		String[] a = getBRiq().split("-");
		rq = a[0]+"年"+a[1]+"月";
		
		sql_rq = 
			"select to_char(a.w+2,'yyyy')||'年'||to_char(a.w+2,'MM')||'月'||to_char(a.w+2,'dd')||'日-'||to_char(a.w+8,'MM')||'月'||to_char(a.w+8,'dd')||'日'  as sj\n" +
			"from (select to_date('" + getBRiq() + "','yyyy-MM-dd') - TO_NUMBER( decode(TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D'),'1','8',TO_CHAR(to_date('" + getBRiq() + "','yyyy-MM-dd'),'D'))) as w from dual) a";
		ResultSetList rsl = cn.getResultSetList(sql_rq);
		if(rsl.next()){
			sj = rsl.getString("SJ");
		}
		
		rt.setTitle(rq+"生产燃料来耗存情况表", ArrWidth);
		rt.setDefaultTitle(1, 2, "单位：", Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 5, "(" + sj + ")", Table.ALIGN_CENTER);
		
		
		//int start_r=0;
		boolean flag=false;
		String dcmc="";
		int dc_r_new=0;
		int dc_r_old=0;
		for(int i=0;i<rt.body.getRows();i++){
//			System.out.println(rt.body.getCellValue(i, 1)+"---");
			if(rt.body.getCellValue(i, 1).equals("单位")){
				dc_r_old++;
				continue;
			}
			
			if(!dcmc.equals(rt.body.getCellValue(i, 1))){//是具体电厂  需要查看  该电厂行数
				
				if(flag){
					rt.body.merge(dc_r_old-dc_r_new, 10, dc_r_old-1, 10);
				}
				dc_r_new=0;
				dc_r_new++;
				dc_r_old++;
				flag=true;
				dcmc=rt.body.getCellValue(i, 1);
			}else{
				dc_r_new++;
				dc_r_old++;
			}
		}
		
		if(rt.body.getRows()>0){
			rt.body.merge(dc_r_old-dc_r_new, 10, dc_r_old, 10);
		}
		
		rt.body.setRowHeight(18);//
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		
		rt.body.ShowZero=true;
			
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
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
	
//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END----------
	
//得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid_dc();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}
	
}