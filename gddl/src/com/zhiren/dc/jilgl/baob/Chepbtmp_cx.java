package com.zhiren.dc.jilgl.baob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Chepbtmp_cx extends BasePage {
	
	/**
	 * 作者:wzb
	 * 时间:2009-11-23 10:38:59
	 * 内容:chepbtmp来煤查询报表
	 */
	
	/**
	 * 作者:夏峥
	 * 时间:2013-06-25
	 * 内容:新增getMeikmx2()方法;
	 */
//	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb中对应的关键字
//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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

//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
//	获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=visit.getDiancqc()+"燃料验收单";
		
		return sb;
	}
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		
		dfb.Binding("BRiq", "Form0");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERiq", "Form0");// 与html页中的id绑定,并自动刷新
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("煤矿单位:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(120);
		CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
	
//		结算类型
		tb1.addText(new ToolbarText("统计方式:"));
		tb1.addText(new ToolbarText("-"));
		ComboBox leix = new ComboBox();
		leix.setTransform("JIESLB");
		leix.setWidth(90);
		leix.setListeners("select:function(){document.Form0.submit();}");
		//leix.setEditable(true);
		tb1.addField(leix);
		

		if(MainGlobal.getXitxx_item("数量", "车辆信息查询是否显示衡器下拉框", "0", "是").equals("是")){
//			衡器类型
			tb1.addText(new ToolbarText("衡器:"));
			tb1.addText(new ToolbarText("-"));
			ComboBox hengh = new ComboBox();
			hengh.setTransform("HENGH");
			hengh.setWidth(90);
			hengh.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(hengh);
		}
		
//		电厂Tree
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
		
		//如果是一厂多制就显示电厂树,如果不是就不显示电厂
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("电厂:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		if (getJieslbValue().getValue().equals("汇总")){
			return getMeikbbhz();
		}else if(getJieslbValue().getValue().equals("明细")){
			if(MainGlobal.getXitxx_item("数量", "车辆信息查询是否显示司机姓名", "0", "否").equals("否")){
				return getMeikmx();
			}else{
				return getMeikmx2();
			}
		}else{
			return "无此报表";
		}
	}
	
	public String getMeikbbhz(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
//		StringBuffer talbe=new StringBuffer();	//报表输出
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String meik=this.getGongysValue().getValue();
		String meiktiaoj="";
		if(meik.equals("全部")){
			meiktiaoj="";
		}else{
			meiktiaoj=" and c.meikdwmc= '"+meik+"'";
		}
		
	
		long hengh=this.getHenghValue().getId();
		String henghTiaoj="";
		if(hengh==0){
			henghTiaoj="";
		}else if(hengh==1){
			if(v.getDiancxxb_id()==264){//264是阳城发电的id,阳城发电衡器号叫"A系统"
				henghTiaoj=" and c.qingchh='A系统'";
			}else{
				henghTiaoj=" and c.qingchh='C'";
			}
			
		}else if(hengh==2){
			if(v.getDiancxxb_id()==264){
				henghTiaoj=" and c.qingchh='B系统'";
			}else{
				henghTiaoj=" and c.qingchh='A'";
			}
		
		}else {
			henghTiaoj="";
		}
		
		
		Report rt = new Report(); //报表定义
				sbsql.setLength(0);
					sbsql.append(
							" select  decode(c.gongysmc,null,'总计',c.gongysmc) as gonghdwmc,\n"+
							"decode(grouping(c.meikdwmc)+grouping(c.gongysmc),2,'',1,'小计',c.meikdwmc) as meikdwmc,\n" +
							"count(c.id) as ches,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
							"sum(c.koud) as koud,to_char(min(c.daohrq),'yyyy-mm-dd') as kaisrq,to_char(max(c.daohrq),'yyyy-mm-dd') as jiezrq\n" + 
							"from chepbtmp c\n" + 
							"where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
							" "+meiktiaoj+"\n"+
							" "+henghTiaoj+"\n"+
							" and c.yunsfs='公路'\n"+
							"group by rollup (c.gongysmc,c.meikdwmc)\n" + 
							"order by c.gongysmc,c.meikdwmc"

					);
					rs=con.getResultSetList(sbsql.toString());
					
					String ArrHeader[][]=new String[1][9];
		
					ArrHeader[0]=new String[] {"供应商","煤矿","车数","毛重","皮重","净重","扣吨","开始日期","结束日期"};
					int ArrWidth[]=new int[] {120,120,45,75,75,75,75,80,80};
					  strFormat = new String[] {"", "", 
			    				"", "0.00", "0.00", "0.00", "0.00", "","" };
					// 数据
					rt.setTitle(v.getDiancmc()+"汽车煤进煤报告_(供煤单位汇总)",ArrWidth);
					rt.setDefaultTitle(1, 3, "报告时间:"+this.getBRiq()+"至"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 2));
					rt.body.setColFormat(strFormat);
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(30);
					
					rt.body.setHeaderData(ArrHeader);// 表头数据
					rt.body.mergeFixedRow();
					rt.body.mergeFixedCols();
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			
			rs.close();
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(25);
			
	     	return rt.getAllPagesHtml();// ph;
	}
	
	
	
	public String getMeikmx(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String meik=this.getGongysValue().getValue();
		String meiktiaoj="";
		if(meik.equals("全部")){
			meiktiaoj="";
		}else{
			meiktiaoj=" and c.meikdwmc= '"+meik+"'";
		}
		
		long hengh=this.getHenghValue().getId();
		String henghTiaoj="";
		if(hengh==0){
			henghTiaoj="";
		}else if(hengh==1){
			if(v.getDiancxxb_id()==264){//264是阳城发电的id,阳城发电衡器号叫"A系统"
				henghTiaoj=" and c.qingchh='A系统'";
			}else{
				henghTiaoj=" and c.qingchh='C'";
			}
			
		}else if(hengh==2){
			if(v.getDiancxxb_id()==264){
				henghTiaoj=" and c.qingchh='B系统'";
			}else{
				henghTiaoj=" and c.qingchh='A'";
			}
		
		}else {
			henghTiaoj="";
		}
		
		Report rt = new Report(); //报表定义
				sbsql.setLength(0);
					sbsql.append(
							"select decode(c.meikdwmc,null,'总计:'||count(c.id)||'车',c.meikdwmc) as meikdwmc,\n" +
							"decode(grouping(c.cheph)+grouping(c.meikdwmc) ,1,'小计:'||count(c.id)||'车',c.cheph) as cheph,\n" + 
							"sum(c.maoz) as maoz,sum(c.piz) as piz,\n" + 
							"sum(c.maoz-c.piz-c.koud) as jingz,sum(c.koud) as koud,\n" + 
							"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj\n"+
							"from chepbtmp c\n" + 
							"where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
							""+meiktiaoj+"\n"+
							""+henghTiaoj+"\n"+
							" and c.yunsfs='公路'\n"+
							"group by rollup (c.meikdwmc,c.zhongcsj,c.cheph,c.qingcsj)\n" + 
							"having not (grouping(c.cheph)+grouping(c.zhongcsj)+grouping(c.qingcsj)=2 or grouping(c.zhongcsj)+grouping(c.qingcsj)=1)\n" + 
							"order by c.meikdwmc,zhongcsj"
					);
					rs=con.getResultSetList(sbsql.toString());
					
					String ArrHeader[][]=new String[1][8];
		//			1120
					ArrHeader[0]=new String[] {"供煤单位","车号","毛重","皮重","净重","扣吨","重车时间","空车时间"};
					int ArrWidth[]=new int[] {120,65,60,60,60,60,120,120};
					  strFormat = new String[] { "","",	"0.00", "0.00", "0.00", "0.00", "","" };
					// 数据
					rt.setTitle(v.getDiancmc()+"汽车煤进煤报告_(矿点明细)",ArrWidth);
					rt.setDefaultTitle(1, 3, "报告时间:"+this.getBRiq()+"至"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(7, 2, "单位:吨", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 0));
					rt.body.setColFormat(strFormat);
//					设定小计行的背景色和字体
					for (int i=2;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 7);
						if((xiaoj.equals(""))){
							//rt.body.setCellValue(i, 10, "");
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
					}
					
					
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(30);
					rt.body.setHeaderData(ArrHeader);// 表头数据
					rt.createFooter(1, ArrWidth);
					rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
			rs.close();
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			
	     	return rt.getAllPagesHtml();// ph;
	}
	
	public String getMeikmx2(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String meik=this.getGongysValue().getValue();
		String meiktiaoj="";
		if(meik.equals("全部")){
			meiktiaoj="";
		}else{
			meiktiaoj=" and c.meikdwmc= '"+meik+"'";
		}
		
		long hengh=this.getHenghValue().getId();
		String henghTiaoj="";
		if(hengh==0){
			henghTiaoj="";
		}else if(hengh==1){
			if(v.getDiancxxb_id()==264){//264是阳城发电的id,阳城发电衡器号叫"A系统"
				henghTiaoj=" and c.qingchh='A系统'";
			}else{
				henghTiaoj=" and c.qingchh='C'";
			}
			
		}else if(hengh==2){
			if(v.getDiancxxb_id()==264){
				henghTiaoj=" and c.qingchh='B系统'";
			}else{
				henghTiaoj=" and c.qingchh='A'";
			}
		
		}else {
			henghTiaoj="";
		}
		
		Report rt = new Report(); //报表定义
			sbsql.setLength(0);
			sbsql.append(
							"select decode(c.meikdwmc,null,'总计:'||count(c.id)||'车',c.meikdwmc) as meikdwmc,\n" +
							"decode(grouping(c.cheph)+grouping(c.meikdwmc) ,1,'小计:'||count(c.id)||'车',c.cheph) as cheph,\n" + 
							"sum(c.maoz) as maoz,sum(c.piz) as piz,\n" + 
							"sum(c.maoz-c.piz-c.koud) as jingz,sum(c.koud) as koud,\n" + 
							"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,c.sijxm\n"+
							"from chepbtmp c\n" + 
							"where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
							""+meiktiaoj+"\n"+
							""+henghTiaoj+"\n"+
							" and c.yunsfs='公路'\n"+
							"group by rollup (c.meikdwmc,(c.zhongcsj,c.cheph,c.qingcsj,c.sijxm))\n" + 
							"order by c.meikdwmc,qingcsj"
					);
			rs=con.getResultSetList(sbsql.toString());
			
			String ArrHeader[][]=new String[1][9];
//			1120
			ArrHeader[0]=new String[] {"供煤单位","车号","毛重","皮重","净重","扣吨","重车时间","空车时间","司机姓名"};
			int ArrWidth[]=new int[] {120,65,60,60,60,60,120,120,100};
			  strFormat = new String[] { "","",	"0.00", "0.00", "0.00", "0.00", "","","" };
			// 数据
			rt.setTitle(v.getDiancmc()+"汽车煤进煤报告_(矿点明细)",ArrWidth);
			rt.setDefaultTitle(1, 3, "报告时间:"+this.getBRiq()+"至"+this.getERiq(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
//					设定小计行的背景色和字体
			for (int i=2;i<=rt.body.getRows();i++){
				String xiaoj=rt.body.getCellValue(i, 7);
				if((xiaoj.equals(""))){
					//rt.body.setCellValue(i, 10, "");
					for (int j=0;j<rt.body.getCols()+1;j++){
						rt.body.getCell(i, j).backColor="silver";
						rt.body.getCell(i, j).fontBold=true;
					}
				}
			}
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(30);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
			rt.body.ShowZero = true;
			rs.close();
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			
	     	return rt.getAllPagesHtml();// ph;
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
		String sql = "select id,mingc from diancxxb";
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
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
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
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			//visit.setInt1(Integer.parseInt(reportType));
			visit.setString15(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			setChangbValue(null);
			setChangbModel(null);
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
		}
		getSelectData();
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
			getSelectData();
		}
	}
//	页面登陆验证
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
	
//	 结算类别(验收单,拒付单,开票单)
	public IDropDownBean getJieslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJieslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJieslbValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJieslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJieslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJieslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJieslbModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "汇总"));
		list.add(new IDropDownBean(2, "明细"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
//	 新衡旧衡
	public IDropDownBean getHenghValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getHenghModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setHenghValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setHenghModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getHenghModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getHenghModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getHenghModels() {
		List list = new ArrayList();
		Visit visit = (Visit) this.getPage().getVisit();
		if(visit.getDiancxxb_id()==264){//阳城发电衡器叫A系统B系统
			list.add(new IDropDownBean(0, "全厂"));
			list.add(new IDropDownBean(1, "A系统"));
			list.add(new IDropDownBean(2, "B系统"));
		}else{//太原二电衡器叫新衡旧衡
			list.add(new IDropDownBean(0, "全厂"));
			list.add(new IDropDownBean(1, "新衡"));
			list.add(new IDropDownBean(2, "旧衡"));
		}
		
		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(list));
		return;
	}
	
//	 供应商下拉框
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getGongysModels() {
		String sql_gongys = 
			"select rownum as xuh, a.meikdwmc from (\n" +
			"select distinct meikdwmc from chepbtmp order by meikdwmc ) a";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"全部"));
		return;
	}
	
	
}
