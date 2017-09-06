package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;

import org.apache.tapestry.html.BasePage;

import java.awt.Color;
import java.awt.GradientPaint;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

public class DayShouhcy extends BasePage {
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	 //	页面判定方法
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private void setReturnValue(){
		String strDiancid=getTreeid();
		String strOldId="";
		int jib=this.getDiancTreeJib();
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select fuid from diancxxb d where d.id="+ strDiancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				strOldId = rs.getString("fuid");
				if (strOldId.equals("0")){
					return;
				}
			}
			setTreeid(strOldId);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
	}
	
//	开始日期v
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_BeginriqChange=true;
		}
	}
//	截止日期v
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_EndriqChange=true;
		}
	}
//	开始日期v
	private boolean _QisrqChange=false;
	public String getQisrq() {
		if (((Visit)getPage().getVisit()).getString12()==null){
			((Visit)getPage().getVisit()).setString12(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit)getPage().getVisit()).getString12();
	}
	
	public void setQisrq(String _value) {
		if (((Visit)getPage().getVisit()).getString12().equals(_value)) {
			_QisrqChange=false;
		} else {
			((Visit)getPage().getVisit()).setString12(_value);
			_QisrqChange=true;
		}
	}
//	截至日期v
	private boolean _JiezrqChange=false;
	public String getJiezrq() {
		if (((Visit)getPage().getVisit()).getString11()==null){
			((Visit)getPage().getVisit()).setString11(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getString11();
	}
	
	public void setJiezrq(String _value) {
		if (((Visit)getPage().getVisit()).getString11().equals(_value)) {
			_JiezrqChange=false;
		} else {
			((Visit)getPage().getVisit()).setString11(_value);
			_JiezrqChange=true;
		}
	}
	public boolean getRaw() {
		return true;
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}
	
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	//得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		return getShouhcy();
	}
	
	public String getShouhcy(){
		Date dat=getBeginriqDate();//日期
		Date dat2=getEndriqDate();
		String strDate=DateUtil.FormatDate(dat);//日期字符
		String strDate2=DateUtil.FormatDate(dat2);
		String strGongs = "";
		String strGroupID = "";
		
		int jib=this.getDiancTreeJib();
		
		if(jib==1){//选集团时刷新出所有的电厂
			//strGongs = "select distinct dc.fgsid as id,dc.xuh,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
			//修改sql,否则用集团进的时候分公司的库存计算不正确 ---wzb
			strGongs = " select dc.id,dc.xuh,dc.mingc from diancxxb dc where dc.jib=2 and dc.ranlgs=-1";//取得集团下的所有分公司
			
			strGroupID = "dc.fgsid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongs = "select distinct dc.id,dc.xuh,dc.mingc from vwdianc dc where (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+") ";//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongs = "select distinct dc.id,dc.xuh,dc.mingc from vwdianc dc where dc.id="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==-1){
			strGongs = "select distinct dc.id,dc.xuh,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}
		 
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
			
		
		if (jib==3){
			sbsql.append(" select getLinkMingxTaiz('"+this.getTreeid()+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
			
		}else if (jib==2){
//			sbsql.append(" select getLinkMingxTaiz(decode(grouping(dc.mingc),1,-1,max(dc.id)),'"+this.getTreeid()+"',decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
			sbsql.append(" select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,\n");
		}else{
			sbsql.append(" select getAlink('厂',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,");
			
		}
		try{

			sbsql.append("       sum(nvl(dr.shourl,0)) as gm,sum(nvl(lj.shourl,0)) as gmlj,sum(nvl(dr.haoyhj,0)) as haoyhj,sum(nvl(dr.fady,0)) as fady,  \n");
			sbsql.append("       sum(nvl(dr.gongry,0)) as gongry,sum(nvl(dr.qity,0)) as qity,sum(nvl(dr.cuns,0)) as cuns,sum(nvl(dr.panyk,0)) as panyk, \n");
			sbsql.append("       sum(nvl(dr.kuc,0)) as kuc \n");
			sbsql.append(" from      \n");
			sbsql.append("    (").append(strGongs).append(") dc,   \n");
			sbsql.append("    (select ").append(strGroupID).append(" as id,sum(nvl(shourl,0)) as shourl,  \n");
			sbsql.append("          sum(fady+gongry+qity) as haoyhj,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity,  \n");
			sbsql.append("          sum(cuns) as cuns,sum(panyk) as panyk,sum(kuc) as kuc  \n");
			sbsql.append("     from vwdianc dc,shouhcrbyb y,pinzb pz  \n");
			sbsql.append("     where y.riq=to_date('").append(strDate2).append("','yyyy-mm-dd') and y.diancxxb_id=dc.id and pz.id=y.pinzb_id and pz.leib='油' \n");
			sbsql.append("     group by ").append(strGroupID).append(")dr,  \n");
			sbsql.append("     (select ").append(strGroupID).append(" as id,sum(nvl(shourl,0)) as shourl,  \n");
			sbsql.append("          sum(fady+gongry+qity) as haoyhj,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity,  \n");
			sbsql.append("          sum(cuns) as cuns,sum(panyk) as panyk,sum(kuc) as kuc  \n");
			sbsql.append("     from vwdianc dc,shouhcrbyb y,pinzb pz  \n");
			sbsql.append("     where y.riq>=to_date('").append(strDate).append("','yyyy-mm-dd') and y.riq<=to_date('").append(strDate2).append("','yyyy-mm-dd')   \n");
			sbsql.append("           and y.diancxxb_id=dc.id and pz.id=y.pinzb_id and pz.leib='油' \n");
			sbsql.append("     group by ").append(strGroupID).append(") lj  \n");
			sbsql.append("where dc.id=dr.id(+)  and dc.id=lj.id(+)  \n");
			sbsql.append("group by rollup(dc.mingc) order by grouping(dc.mingc), max(dc.xuh) \n");
			
			String ArrHeader[][] =new String[3][10];
			 ArrHeader[0]=new String[] {"油收耗存日报情况","油收耗存日报情况","油收耗存日报情况","油收耗存日报情况","油收耗存日报情况","油收耗存日报情况","油收耗存日报情况","油收耗存日报情况","油收耗存日报情况","油收耗存日报情况"};
			 ArrHeader[1]=new String[] {"单位","入厂油量","入厂油量","耗用","耗用","耗用","耗用","损耗","盘盈亏","库存"};
			 ArrHeader[2]=new String[] {"单位","当日","累计","合计","发电","供热","其它用","损耗","盘盈亏","库存"};

			 int ArrWidth[] =new int[] {180,90,90,90,90,90,90,80,80,90};
			ResultSet rs = cn.getResultSet(sbsql.toString());

			Report rt=new Report();
			rt.setCenter(false);
			rt.setBody(new Table(rs,3,0,1));
			rt.body.setHeaderData(ArrHeader);
			rt.body.setFontSize(12);
			rt.body.setBorder(2);
			rt.body.setUseCss(true);
			rt.body.setColHeaderClass("tab_colheader");
			rt.body.setRowHeaderClass("tab_rowheader");
			rt.body.setFirstDataRowClass("tab_data_line_one");
			rt.body.setSecondDataRowClass("tab_data_line_two");
			rt.body.setCellsClass("tab_cells");
			rt.body.setTableClass("tab_body");
			rt.body.setWidth(ArrWidth);
			for(int i=1;i<=10;i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			//rt.body.ShowZero = true;

			return rt.getHtml();
		
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}finally{
			cn.Close();
		}
	}
	

	
/*	public String getChartLineBig(){
		return ((Visit) getPage().getVisit()).getString6();
	}*/
	
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ReturnClick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			return;
		}
		if (_ReturnClick){
			_ReturnClick=false;
			setReturnValue();
			return;
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString3("-1");
			
			visit.setString12(DateUtil.FormatDate(new Date()));
			visit.setString11(DateUtil.FormatDate(new Date()));
			
			visit.setDate1(null);
			visit.setDate2(null);
			
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			this.setTreeid(null);
			
			this.setBeginriqDate(visit.getMorkssj());
			this.setEndriqDate(visit.getMorjssj());
		}
		getToolBars();
		isBegin=true;
	}	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
	
	public void getToolBars() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","");// 与html页中的id绑定,并自动刷新
		df.setWidth(60);
 		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		 
		tb1.addText(new ToolbarText("到:"));
		DateField df1 = new DateField();
		df1.setValue(DateUtil.FormatDate(this.getEndriqDate()));
		df1.Binding("endriqDateSelect","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(60);
 		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
	
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setId("diancTree_text");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		ToolbarButton tbok = new ToolbarButton(null,"确定","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);

		tb1.addItem(tbok);
		tb1.addItem(tb);
		String diancxxbid=this.getTreeid();
		long strDiancid=visit.getDiancxxb_id();
		String Diancid=""+strDiancid;
		if(!diancxxbid.equals(Diancid)){
		ToolbarButton tbfh = new ToolbarButton(null,"返回上级","function(){document.getElementById('ReturnButton').click();}");
		tbfh.setIcon(SysConstant.Btn_Icon_Return);
		tb1.addItem(tbfh);
		}
		setToolbar(tb1);
	}
	
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;
	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql="";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
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

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		Visit visit =((Visit) getPage().getVisit());
		visit.setString2(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
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
	
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
	
	private int getDigts(){
		return 0;
	}
}