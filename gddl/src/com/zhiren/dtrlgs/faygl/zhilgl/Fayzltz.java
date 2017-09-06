package com.zhiren.dtrlgs.faygl.zhilgl;

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


public class Fayzltz  extends BasePage implements PageValidateListener{

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
			setTreeid(null);
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
		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = " and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and dc.id = " + getTreeid() + "";
		}
		String jiaql = "z.meil";//加权量
		StringBuffer strSQL = new StringBuffer();
		String sql =
			"select  decode(grouping(z.fahrq)+grouping(l.mingc),2,'合计',1,'日小计',to_char(z.fahrq,'yyyy-MM-dd')) as fahrq,\n" +
			"		 dc.mingc as fahdw,\n"+		
			"        dc2.mingc as shouhr,\n" + 
			"        z.chec as chec,\n" + 
			"        l.mingc as chuanm,\n" + 
			"        sum(z.meil) as meil,\n" + 
			"        ls.huaybh,\n" + 
			"        b.mingc as jianydw,\n" + 
			"        to_char(ls.huaysj,'yyyy-MM-dd') as huayrq,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.qnet_ar*"+jiaql+"*1000/4.1816)/sum("+jiaql+"),0)) as qnet_ark,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.qnet_ar*"+jiaql+")/sum("+jiaql+"),2)) as qnet_ar,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.mt*"+jiaql+")/sum("+jiaql+"),2)) as mt,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.mad*"+jiaql+")/sum("+jiaql+"),2)) as mad,\n" + 
			"         decode(sum("+jiaql+"),0,0,round_new(sum(ls.aar*"+jiaql+")/sum("+jiaql+"),2))     as aar,\n"+
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.ad*"+jiaql+")/sum("+jiaql+"),2)) as ad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.aad*"+jiaql+")/sum("+jiaql+"),2)) as aad,\n" + 
			 "        decode(sum("+jiaql+"),0,0,round_new(sum(ls.var*"+jiaql+")/sum("+jiaql+"),2))   as var,\n"+
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.vad*"+jiaql+")/sum("+jiaql+"),2)) as vad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.vdaf*"+jiaql+")/sum("+jiaql+"),2)) as vdaf,\n" + 
			"         decode(sum("+jiaql+"),0,0,round_new(sum(ls.star*"+jiaql+")/sum("+jiaql+"),2))    as star,\n"+
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.std*"+jiaql+")/sum("+jiaql+"),2)) as std,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.stad*"+jiaql+")/sum("+jiaql+"),2)) as stad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.had*"+jiaql+")/sum("+jiaql+"),2)) as had,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.qgrad*"+jiaql+")/sum("+jiaql+"),2)) as qgrad,\n" + 
			"        decode(sum("+jiaql+"),0,0,round_new(sum(ls.qgrad_daf*"+jiaql+")/sum("+jiaql+"),2)) as qgr_daf,\n" + 
			"        ls.huayy,\n" + 
			"        ls.shenhry,\n" + 
			"        null\n" + 
			"from fayslb z,luncxxb l,diancxxb dc2,zhillsb ls,caiyb c, diancxxb dc,\n" + 
			"     (select t.id,t.mingc from item t, itemsort s\n" + 
			"      where t.itemsortid = s.id and s.mingc = '质量检验单位') b\n" + 
			"where z.luncxxb_id = l.id(+) and z.diancxxb_id=dc.id \n" + 
			"      and dc2.id=z.shr_diancxxb_id\n" + 
			"      and z.zhilb_id =  ls.zhilb_id \n" + 
			"      and ls.caiyb_id = c.id\n" + 
			"      and c.songjdwb_id = b.id\n" + 
			" \t   and z.fahrq>="+DateUtil.FormatOracleDate(getBRiq()) +"\n"+
			"     and z.fahrq<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			str+" \n"+ 
			"group by rollup ((z.fahrq,dc.mingc,dc2.mingc,z.chec,l.mingc,ls.huaybh,b.mingc,ls.huaysj,ls.huayy,ls.shenhry))\n" + 
			"order by grouping(z.fahrq) desc,z.fahrq,grouping(dc.mingc),dc.mingc,dc2.mingc,z.chec,l.mingc,ls.huaybh,b.mingc,ls.huaysj,ls.huayy,ls.shenhry";

		strSQL.append(sql);
		
		String ArrHeader[][]=new String[2][28];
		ArrHeader[0]=new String[] {Local.fahrq,"发货单位",Local.shouhr_jies,Local.chec,Local.chuanm_zhuangcb,Local.meil_jies,Local.huaydh,Local.jianydw,Local.huayrq,Local.diwfrl,Local.diwfrl,Local.shuif,Local.shuif,Local.huif,Local.huif,Local.huif,Local.huiff,Local.huiff,Local.huiff,Local.quanl,Local.quanl,Local.quanl,Local.qing,Local.gaowfrl,Local.gaowfrl,Local.jianyr,Local.shenh,Local.pizhun};
		ArrHeader[1]=new String[] {Local.fahrq,"发货单位",Local.shouhr_jies,Local.chec,Local.chuanm_zhuangcb,Local.meil_jies,Local.huaydh,Local.jianydw,Local.huayrq,"Qnet,ar<br>(Kcal/kg)","Qnet,ar<br>(MJ/kg)","Mt","Mad","Aar","Ad","Aad","Var","Vad","Vdaf","Star","St,d","St,ad","Had","Qgrad","Qgr_daf",Local.jianyr,Local.shenh,Local.pizhun};

		int ArrWidth[]=new int[] {80,100,80,80,60,40,80,140,80,45,39,30,30,30,30,30,30,30,30,30,30,30,30,39,39,45,60,60};
		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		Table tb=new Table(rs, 2, 0, 1);
		rt.setBody(tb);
//		电厂信息表固定 秦皇岛港为501
		String biaot="";
		if(getTreeid().equals("501")){
			biaot="装 船 质 量 台 帐";
		}else{
			biaot="发 运 质 量 台 帐";
		}
		rt.setTitle(biaot, ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "制表单位:"	+ visit.getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 6, "发货日期:"+ getBRiq()+ " 至 " + getERiq(), Table.ALIGN_CENTER);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		
		
		//合并单元格
		rt.body.mergeCell(1, 1, 2, 1);
		rt.body.mergeCell(1, 2, 2, 2);
		rt.body.mergeCell(1, 3, 2, 3);
		rt.body.mergeCell(1, 4, 2, 4);
		rt.body.mergeCell(1, 5, 2, 5);
		rt.body.mergeCell(1, 6, 2, 6);
		rt.body.mergeCell(1, 7, 2, 7);
		rt.body.mergeCell(1, 8, 2, 8);
		rt.body.mergeCell(1, 9, 2, 9);
		rt.body.mergeCell(1, 10, 1,11);
		rt.body.mergeCell(1, 12, 1,13);
		rt.body.mergeCell(1, 14, 1,16);
		rt.body.mergeCell(1, 17, 1,19);
		rt.body.mergeCell(1, 20, 1,22);
		rt.body.mergeCell(1, 23, 2, 23);
		rt.body.mergeCell(1, 24, 1, 25);
		rt.body.mergeCell(1, 26, 2, 26);
		rt.body.mergeCell(1, 27, 2, 27);
		rt.body.mergeCell(1, 28, 2, 28);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_LEFT);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
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
		rt.body.setColAlign(23, Table.ALIGN_LEFT);
		rt.body.setColAlign(24, Table.ALIGN_LEFT);
		rt.body.setColAlign(25, Table.ALIGN_LEFT);

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "制表日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(18, 3, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_FONTSIZE, 10);
		
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
		Visit visit = (Visit) this.getPage().getVisit();
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
		
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
//	添加电厂树
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
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