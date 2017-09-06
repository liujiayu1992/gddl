package com.zhiren.gangkjy.taiz;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

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
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


public class Caigjstz  extends BasePage implements PageValidateListener{

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
			setYewlxModel(null);
			setYewlxValue(null);
			setRiqLeixDropDownValue(null);
			setRiqLeixDropDownModel(null);
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
		StringBuffer strSQL = new StringBuffer();
		
		String yewlx="";
		if(getYewlxValue().getId()!=-1){
			yewlx="  	and k.yewlxb_id="+getYewlxValue().getStrId()+" \n";
		}
		
		String sql = 

			"select decode(grouping(k.gongysmc),1,'总计',k.gongysmc) as fahr,\n" +
			"       decode(grouping(k.bianm)+grouping(k.gongysmc),1,'小计',k.bianm) as bianh,\n" + 
			"       to_char(k.jiesrq,'yyyy-MM-dd') as jiesrq,\n" + 
			"       to_char(k.ruzrq,'yyyy-MM-dd') as ruzrq,\n" + 
//			"       d.mingc as shouhr,\n" + 
			"       k.meiz as pinz,\n" + 
			"       sum(k.ches) as ches,\n" + 
			"       sum(k.jiessl) as meil,\n" + 
			"       decode(sum(decode(k.jiesrl,0,0,k.jiessl)),0,0,round_new(sum(k.jiesrl*k.jiessl)/sum(decode(k.jiesrl,0,0,k.jiessl)),0)) as farl,\n" + 
			"       decode(sum(decode(k.jieslf,0,0,k.jiessl)),0,0,round_new(sum(k.jieslf*k.jiessl)/sum(decode(k.jieslf,0,0,k.jiessl)),1)) as liuf,\n" +
			"		decode(sum(decode(k.jiesrl,0,0,k.jiessl)),0,round_new(k.buhsdj,2),round_new(sum(k.buhsdj*decode(k.jiesrl,0,0,k.jiessl))/sum(decode(k.jiesrl,0,0,k.jiessl)),2)) as buhsdj,\n" + 
			"       sum(k.hansmk)+sum(y.hansyf) as hej,\n" + 
			"       sum(k.buhsmk) as buhsmk,\n" + 
			"       sum(k.shuik) as shuik,\n" + 
			"       sum(y.buhsyf) as buhsyf,\n" + 
			"       sum(y.shuik) as yunfs,\n" + 
			"       decode(sum(decode(k.jiesrl,0,0,k.jiesrl)),0,0,round_new(sum(decode(k.jiesrl,0,0,(k.hansdj)*7000))/sum(decode(k.jiesrl,0,0,k.jiesrl)),2)) as biaomdj,\n" + 
			"       decode(sum(decode(k.jiesrl,0,0,k.jiesrl)),0,0,round_new(sum(decode(k.jiesrl,0,0,(k.buhsdj)*7000))/sum(decode(k.jiesrl,0,0,k.jiesrl)),2)) as buhsbmdj \n"+
//			"       decode(sum(k.jiessl),0,0,round_new(sum(((k.hansmk+y.hansyf)*29.271)/k.jiesrl*k.jiessl)/sum(k.jiessl),3)) as biaomdj,\n" + 
//			"       decode(sum(k.jiessl),0,0,round_new(sum(((k.buhsmk+y.buhsyf)*29.271)/k.jiesrl*k.jiessl)/sum(k.jiessl),3)) as buhsbmdj\n" + 
			"from kuangfjsmkb k,diancxxb d,kuangfjsyfb y\n" + 
			"where k.diancxxb_id = d.id\n" + 
			"      and y.kuangfjsmkb_id = k.id\n" + 
			" 	   and k."+getRiqLeixName()+">="+DateUtil.FormatOracleDate(getBRiq()) +
			" 	   and k."+getRiqLeixName()+"<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"	   and k.diancxxb_id = " + visit.getDiancxxb_id() + "\n" +
			yewlx+
			"group by rollup(k.gongysmc,k.bianm,(k.jiesrq,k.ruzrq,k.meiz,k.buhsdj))\n" + 
			"having not (grouping(k.bianm)||grouping(k.meiz))=1\n" + 
			"order by grouping(k.gongysmc) desc,k.gongysmc,grouping(k.bianm) desc,k.bianm,k.jiesrq,k.ruzrq,k.meiz";

		strSQL.append(sql);
		
		 String ArrHeader[][]=new String[1][19];
		 ArrHeader[0]=new String[] {Local.fahr,Local.jiesbh_jies,Local.jiesrq_jies,Local.ruzrq_jies,Local.pinz,Local.ches,Local.meil_jies,"发热量(Kcal/kg)",Local.liuf_jies,"不含税单价",Local.hej_jies,Local.buhsmk_jies,Local.meis_jies,Local.buhsyf_jies,Local.yunfs_jies,Local.biaomdj_jies,Local.buhsbmdj_jies};

		 int ArrWidth[]=new int[] {120,180,80,80,50,50,60,50,40,50,100,100,100,50,50,50,50};
		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		
		Table tb=new Table(rs, 1, 0, 2);
		rt.setBody(tb);
		
		rt.setTitle("结算台账(采购)", ArrWidth);
		rt.setDefaultTitle(1, 19, getBRiq() + " 至 " + getERiq(), Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
			
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
		tb1.addText(new ToolbarText("时间类型:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("RiqLeixDropDown");
		cb1.setEditable(true);
		cb1.setWidth(60);
		cb1.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("日期:"));
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
		tb1.addText(new ToolbarText("业务类型:"));
		ComboBox Yewlx = new ComboBox();
		Yewlx.setEditable(true);
		Yewlx.setId("YewlxDrop");
		Yewlx.setWidth(100);
		Yewlx.setLazyRender(true);
		Yewlx.setTransform("YewlxDropDown");
		Yewlx.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(Yewlx);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
//	 时间类型
    public IDropDownBean getRiqLeixDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getRiqLeixDropDownModel().getOption(0));
    	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
    public void setRiqLeixDropDownValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);
    }
    public void setRiqLeixDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }
    public IPropertySelectionModel getRiqLeixDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getRiqLeixDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getRiqLeixDropDownModels() {
    	List list=new ArrayList();
    	list.add(new IDropDownBean(1, "结算"));
    	list.add(new IDropDownBean(2, "入账"));
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
        return ;
    }
    private String getRiqLeixName(){
    	if(1==getRiqLeixDropDownValue().getId()){
    		return "jiesrq";
    	}else{
    		return "ruzrq";
    	}
    }
//	业务类型 DropDownBean10
//  业务类型 ProSelectionModel10
   
   public void setYewlxModel(IPropertySelectionModel value){
       ((Visit)getPage().getVisit()).setProSelectionModel10(value);
   }
   
   public IPropertySelectionModel getYewlxModel(){
   	if(((Visit)getPage().getVisit()).getProSelectionModel10()==null){
   		getYewlxModels();
       }
       return ((Visit)getPage().getVisit()).getProSelectionModel10();
   }
   
   public IPropertySelectionModel getYewlxModels(){
	((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel("select id, mingc from yewlxb where mingc<>'采购' order by id","全部"));
	return ((Visit)getPage().getVisit()).getProSelectionModel10();
   }
   
   public IDropDownBean getYewlxValue() {
   	if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
   		((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getYewlxModel().getOption(0));
   	}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
   public void setYewlxValue(IDropDownBean value) {
		if(((Visit) getPage().getVisit()).getDropDownBean10()!=value){
			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
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