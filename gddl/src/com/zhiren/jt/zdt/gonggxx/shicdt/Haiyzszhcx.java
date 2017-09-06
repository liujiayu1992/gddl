/*
* 时间：2009-04-11
* 作者：chh
* 修改内容：_friqValue 只在第一次进入页面是赋值,更改查询日期后，_friqValue不变化数据查询不正确，
* 改为_friqValue在每次提交页面时赋值，而不是只第一次赋值。
*/

package com.zhiren.jt.zdt.gonggxx.shicdt;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


public class Haiyzszhcx  extends BasePage implements PageValidateListener{

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
	
	
	//开始日期v
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
		}
	}
	
//	结束日期v
	private Date _friqValue = DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
	private boolean _friqChange=false;
	public Date getfriqDate() {
		if (_friqValue==null){
			_friqValue =DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
		}
		return _friqValue;
	}
	
	public void setfriqDate(Date _value) {
		if (FormatDate(_friqValue).equals(FormatDate(_value))) {
			_friqChange=false;
		} else {
			_friqValue = _value;
			_friqChange=true;
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
			_BeginriqValue = DateUtil.getDate(getZuiwrq());
			
			visit.setList1(null);
			this.getSelectData();
		}
		_friqValue = DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
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
		JDBCcon cn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		String riq = OraDate(_BeginriqValue);//当前日期
		String friq = OraDate(_friqValue);
		
// **煤价**
		//报表表头定义
		Report rt = new Report();
		String[][] ArrHeader=null;
		 ArrHeader =new String[1][5];
		 ArrHeader[0]=new String[] {"项目名称","市场运价","本周指数","上周相比涨跌情况(%)"};
		int[] ArrWidth;
		ArrWidth = new int[] {260,70,70,70};

		String sql="select  i.mingc itemmingc,yun.shicyj,yun.benzzs,yun.shangzxbzd \n"+
                   "from (select * from YUNJZSHQB yund where yund.riq="+riq+" ) yun,item i,itemsort it \n"+
                    "  where yun.item_id(+)=i.id\n"+
                     " and it.id=i.itemsortid\n"+
                     " and it.mingc in('国际运价指数')\n"+
                     " order by i.xuh";
	    ResultSet rsl=   	cn.getResultSet(sql);
	/*	
//		得到行数据：时间
		StringBuffer strCol= new StringBuffer();
		strCol.append("select to_char("+riq+"-rownum+1,'MM')||'月'|| to_char("+riq+"-rownum+1,'dd')||'日' as 时间  \n");
		strCol.append(" from all_objects");
		strCol.append(" where rownum<=10 \n");
		strCol.append(" order by 时间");
		
//		得到列数据：品种
		StringBuffer strRow = new StringBuffer();
		
		//--实际装车
		strRow.append("select distinct pz.mingc as 品种 \n");
		strRow.append("from gangkscjgb g,pinzb pz where g.pinzb_id=pz.id  \n");
		strRow.append("and riq>="+friq+" and riq<="+riq+"\n");
		strRow.append("order by pz.mingc desc \n");
		
//		 得到全部数据 棋盘表
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append(" select  riq as 时间, gk.mingc as 品种, gk.jiag as 价格上下限 \n");
		sbsql.append(" from (select pz.mingc ,pinzb_id,to_char(riq, 'MM')||'月'||to_char(riq, 'dd')||'日' as riq,(jiagxx||'~'||jiagsx) as jiag \n");
		sbsql.append("       from gangkscjgb gk ,pinzb pz \n");
		sbsql.append("       where gk.riq >="+friq+" and gk.riq <="+riq+"  \n");
		sbsql.append("       and gk.pinzb_id = pz.id  \n");
		sbsql.append("       )  gk order by riq \n");
		
		cd.setRowNames("品种");
		cd.setColNames("时间");
		cd.setDataNames("价格上下限");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 60;
		}
		ArrWidth[0] = 100;
		ArrWidth[1] = 60;*/
//		rt.setBody(cd.DataTable);
		Table tb = new Table(rsl,1, 0, 1);
		rt.setBody(tb);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(21);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;
		rt.setTitle("国际运价指数表", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.body.setCellValue(1, 0, "煤炭品种");
//		rt.body.setCellValue(1, 1, "煤炭品种");
		rt.setDefaultTitle(1, 2,"填报单位(盖章):"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_LEFT);
		//页脚 
		 rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		  rt.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(7,2,"制表:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		
		
		
// **港存**
		Report rt_gc = new Report();
		String[][] ArrHeader_gc;
		ArrHeader_gc =new String[1][5];
		ArrHeader_gc[0]=new String[] {"项目名称","市场运价","本周指数","上周相比涨跌情况(%)"};
		int[] ArrWidth_gc;
		ArrWidth_gc = new int[] {260,70,70,70};
		String sql_guonei="select  i.mingc itemmingc,yun.shicyj,yun.benzzs,yun.shangzxbzd \n"+
        "from (select * from YUNJZSHQB yund where yund.riq="+riq+" ) yun,item i,itemsort it \n"+
         "  where yun.item_id(+)=i.id\n"+
          " and it.id=i.itemsortid\n"+
          " and it.mingc in('国内运价指数')\n"+
          " order by i.xuh";
           ResultSet rsl_guonei=   	cn.getResultSet(sql_guonei);
/*		
//		得到行数据：时间
		StringBuffer strCol_gc= new StringBuffer();
		strCol_gc.append("select to_char("+riq+"-rownum+1,'MM')||'月'|| to_char("+riq+"-rownum+1,'dd')||'日' as 时间  \n");
		strCol_gc.append(" from all_objects");
		strCol_gc.append(" where rownum<=10 \n");
		strCol_gc.append(" order by 时间");

//		得到列数据：港口,分类
		StringBuffer strRow_gc = new StringBuffer();
		
		strRow_gc.append("select distinct c.mingc as 港口,g.fenl as 分类 \n");
		strRow_gc.append("from gangkgc g,chezxxb c where g.chezxxb_id=c.id  \n");
		strRow_gc.append("and riq>="+friq+" and riq<="+riq+"\n");
		strRow_gc.append("order by c.mingc desc,g.fenl desc \n");
		
//		 得到全部数据 棋盘表
		StringBuffer sbsql_gc = new StringBuffer();
		
		sbsql_gc.append(" select  riq as 时间, gk.mingc as 港口, gk.fenl as 分类, gk.gangc as 港存  \n");
		sbsql_gc.append(" from (select c.mingc ,chezxxb_id,to_char(riq, 'MM')||'月'||to_char(riq, 'dd')||'日' as riq,fenl,gangc \n");
		sbsql_gc.append("       from gangkgc gk ,chezxxb c \n");
		sbsql_gc.append("       where gk.riq >="+friq+" and gk.riq <="+riq+"  \n");
		sbsql_gc.append("       and gk.chezxxb_id = c.id  \n");
		sbsql_gc.append("       )  gk order by riq \n");
		
		cd.setRowNames("港口,分类");
		cd.setColNames("时间");
		cd.setDataNames("港存");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow_gc.toString(), strCol_gc.toString(), sbsql_gc.toString());
		ArrWidth_gc = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth_gc.length; i++) {
			ArrWidth_gc[i] = 60;
		}
		ArrWidth_gc[0] = 60;
		ArrWidth_gc[1] = 40;*/
//		rt_gc.setBody(cd.DataTable);
       	Table tb_guonei = new Table(rsl_guonei,1, 0, 1);
		rt_gc.setBody(tb_guonei);
		rt_gc.body.setWidth(ArrWidth_gc);
		rt_gc.body.setPageRows(21);
		rt_gc.body.setHeaderData(ArrHeader_gc);
		rt_gc.body.mergeFixedRowCol();
		rt_gc.body.ShowZero = true;
		rt_gc.setTitle("国内运价指数表", ArrWidth_gc);
		rt_gc.title.setRowHeight(2, 50);
		rt_gc.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt_gc.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt_gc.body.setCellValue(1, 0, "港口名称");
//		rt_gc.body.setCellValue(1, 1, "港口名称");
		rt_gc.setDefaultTitle(1, 2, "填报单位(盖章):"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_LEFT);
						
//		页脚 
		rt_gc.createDefautlFooter(ArrWidth_gc);
		rt_gc.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt_gc.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
		rt_gc.setDefautlFooter(7,2,"制表:",Table.ALIGN_LEFT);
		rt_gc.setDefautlFooter(rt_gc.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		
		
//		rt_gc.setDefautlFooter(1, 3,
//				"打印日期:"
//						+ FormatDate(DateUtil.getDate(DateUtil
//								.FormatDate(new Date()))), Table.ALIGN_LEFT);
//		rt_gc.setDefautlFooter(7, 1, "单位:车、吨", Table.ALIGN_RIGHT);
			
			_CurrentPage=1;
			_AllPages=1;
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml()+rt_gc.getAllPagesHtml();
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
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
//		tb1.addText(new ToolbarText("类型："));
		
		 ComboBox chaxunco=new ComboBox();
		 chaxunco.setTransform("WeizSelectx");
		 chaxunco.setWidth(100);
		 chaxunco.setLazyRender(true);
		 chaxunco.setId("chaxunco");
//		 tb1.addField(chaxunco);
		 tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
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

//	 报表类型

//	 查找方式下拉菜单
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean9()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean9(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		
	
		list.add(new IDropDownBean(200, "国际运价指数"));
		
		list.add(new IDropDownBean(201, "国内运价指数"));
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(list));
	}
	public String getZuiwrq(){
		JDBCcon con=new JDBCcon();
		String sql="select to_char(yun.riq,'yyyy-mm-dd') maxriq from  YUNJZSHQB  yun,item i,itemsort it \n"+
                    " where yun.item_id =i.id\n"+
                     " and it.id=i.itemsortid\n";
//                     " and it.mingc in('"+this.getWeizSelectValue().getValue()+"')\n";
		
		 ResultSet rsl= con.getResultSet(sql);
		 String maxriq="";
		 try {
			if(rsl.next()){
				 maxriq=rsl.getString("maxriq");
			 }else{
				 maxriq=DateUtil.FormatDate(new Date());
			 }
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		  con.Close();
		  return maxriq;
	}
}