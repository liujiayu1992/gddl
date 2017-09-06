package com.zhiren.dc.jilgl.tiel.zibc;

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
import com.zhiren.common.Locale;
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
import com.zhiren.report.Paragraph;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.report.Document;

/*
 * 作者：张乐
 * 时间：2010-08-18
 * 描述：新增自备车运行明细查询
 */

public class Zibcyxmx extends BasePage {
	private static final String rptCustomKey = "Zibcyxmx";
	
	private boolean isBegin;
	
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
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

	
//  获得选择的树节点的对应的电厂名称   
    private String getDcMingc(String id){ 
    	if(id == null || "".equals(id)){
    		return "";
    	}
		JDBCcon con=new JDBCcon();
		String mingc="";
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
//  获得选择的树节点的对应的供应商名称   
    private String[] getGys(String id){ 
    	String[] gys={"全部","-1"};
    	if(id==null || "".equals(id)){
    		return gys;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			gys[0]=rsl.getString("mingc");
			gys[1]=rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean isParentDc(String id){ 
    	boolean isParent= false;
    	if(id == null || "".equals(id)){
    		return isParent;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc from diancxxb where fuid = " + id;
		if(con.getHasIt(sql)){
			isParent = true;
		}
		con.Close();
		return isParent;
	}
//	取得日期参数SQL
    private String getDateParam(){
//		日期条件
		String rqsql = "";
		if(getBRiq() == null || "".equals(getBRiq())){
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(new Date())+"\n";
		}else{
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq())+"\n";
		}
		if(getERiq() == null || "".equals(getERiq())){
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(new Date())+"+1\n";
		}else{
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(getERiq())+"+1\n";
		}
		return rqsql;
    }
//  取得供应商参数SQL
    private String getGysParam(){
//		供应商煤矿条件
		String gyssql = "";
		if("1".equals(getGys(getTreeid())[1])){
			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
		}else if("0".equals(getGys(getTreeid())[1])){
			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
		}
		return gyssql;
    }
//  取得电厂参数SQL
    private String getDcParam(){
//		电厂sql
		String dcsql = "";
    	if(isParentDc(getTreeid_dc())){
    		dcsql = "and d.fuid = " + getTreeid_dc() + "\n";
    	}else{
    		dcsql = "and f.diancxxb_id = " + getTreeid_dc() + "\n";
    	}
		return dcsql;
    }
//	获取表表标题
	public String getRptTitle() {
		String sb = "自备车运行明细";
		return sb;
	}
	
//	刷新衡单列表
	public void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
//		电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
//		供应商树
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
	private Paragraph getZibcslcx(JDBCcon con){
		Visit v = (Visit) getPage().getVisit();
		Paragraph bt = new Paragraph();
		String sql ="SELECT c.cheph,to_char(c.zhongcsj,'yyyy-mm-dd') zhongcsj,d.mingc changb,g.mingc gongys,m.mingc meik,c.maoz,c.piz,(c.maoz-c.piz) AS jingz,c.biaoz,c.yingk,c.yuns \n"
				   +"FROM chepb c,fahb f,gongysb g,meikxxb m,diancxxb d \n"
				   +"WHERE c.fahb_id = f.id AND f.meikxxb_id = m.id AND f.gongysb_id = g.id \n"
				   +"AND f.diancxxb_id = d.id AND c.cheph IN (SELECT cheh FROM zibcb)\n"
				   +"AND c.zhongcsj>="+DateUtil.FormatOracleDate(getBRiq())+"\n"
				   +"AND c.zhongcsj<="+DateUtil.FormatOracleDate(getERiq())+"\n"
				   +getGysParam() + getDcParam() 
				   +"ORDER BY zhongcsj DESC,d.mingc,g.mingc,m.mingc";
		
		
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
		int aw=0;
        rs = rstmp;
        ArrHeader = new String[1][11];
        ArrHeader[0] = new String[] { "车号", "过衡时间", "厂别","供应商", "煤矿","毛重", "皮重", "净重", "票重", "盈亏","运损"};
        	
    	ArrWidth = new int[11] ;
    	ArrWidth = new int[] {100, 80, 80, 80, 80, 80, 80, 80, 80, 80,80};
    	
    	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
    	rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("", ArrWidth);
		rt.setBody(new Table(rs,1,0,1));
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero=true;
		
//		rt.title.fontSize=10;
//		rt.title.setRowHeight(2, 50);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
////		rt.setDefaultTitle(1, 3, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
////				Table.ALIGN_LEFT);
////		rt.setDefaultTitle(4, 5, getBRiq() + " 至 " + getERiq(),
////				Table.ALIGN_LEFT);
//		//rt.setDefaultTitle(6, 2, "单位：吨、车", Table.ALIGN_RIGHT);
//
//		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
//		
//		rt.body.setWidth(ArrWidth);
//		rt.body.setHeaderData(ArrHeader);
////		rt.body.setColFormat(strFormat);
//		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		//RPTInit.getInsertSql(v.getDiancxxb_id(),sql,rt,getRptTitle(),""+rptCustomKey);
     	bt.addText(rt.getAllPagesHtml());
     	return bt;
	}
	
	private Paragraph getHuiz(JDBCcon con){
		Visit v = (Visit) getPage().getVisit();
		Paragraph bt = new Paragraph();
		String sql ="SELECT x.ches,x.maoz,x.piz,x.jingz,x.biaoz,x.yingk,x.yuns,y.shiyl,z.yunyl\n" +
					"FROM\n" + 
					"(SELECT count(c.cheph) as ches,sum(c.maoz) maoz,sum(c.piz) piz,\n" + 
					"sum(c.maoz-c.piz) jingz,sum(c.biaoz) biaoz,sum(c.yingk) yingk,sum(c.yuns) yuns\n" + 
					"FROM chepb c\n" + 
					"WHERE  c.cheph IN (SELECT cheh FROM zibcb)\n" + 
					"AND c.zhongcsj>="+DateUtil.FormatOracleDate(getBRiq())+"\n" + 
					"AND c.zhongcsj<="+DateUtil.FormatOracleDate(getERiq())+"\n" + 
					"ORDER BY zhongcsj DESC) x,\n" + 
					"(SELECT DECODE(b.ches,0,0,round(a.ches/b.ches*100,2)) AS shiyl FROM\n" + 
					"(SELECT  count(cheph) ches FROM chepb WHERE cheph IN (SELECT cheh FROM zibcb)\n" + 
					"AND zhongcsj>="+DateUtil.FormatOracleDate(getBRiq())+"\n" + 
					"AND zhongcsj<="+DateUtil.FormatOracleDate(getERiq())+") a,\n" + 
					"(SELECT count(cheph) ches FROM chepb WHERE zhongcsj>="+DateUtil.FormatOracleDate(getBRiq())+"\n" + 
					"AND zhongcsj<="+DateUtil.FormatOracleDate(getERiq())+") b) y,\n" + 
					"(SELECT DECODE(b.ches,0,0,round(a.ches/b.ches*100,2)) yunyl FROM\n" +
					"(SELECT count(cheph) ches FROM (SELECT DISTINCT cheph\n" + 
					"FROM chepb WHERE cheph IN (SELECT cheh FROM zibcb))) a,\n" + 
					"(SELECT count(cheh) ches FROM zibcb) b)z";
		
		
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
		int aw=0;
        rs = rstmp;
        ArrHeader = new String[1][9];
        ArrHeader[0] = new String[] { "车数","毛重", "皮重", "净重", "票重", "盈亏","运损","使用率","运营率"};
        	
    	ArrWidth = new int[9] ;
    	ArrWidth = new int[] {100, 100, 100, 100, 100, 100, 100, 100, 100};
    	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
    	rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
    	rt.setTitle(getRptTitle(), ArrWidth);
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(4, 5, getBRiq() + " 至 " + getERiq(),
//				Table.ALIGN_LEFT);
		//rt.setDefaultTitle(6, 2, "单位：吨、车", Table.ALIGN_RIGHT);

		rt.setBody(new Table(rs, 1, 0, 1));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		rt.body.setColAlign(1, Table.ALIGN_RIGHT);
//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
//		rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
//				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(9, 2, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		bt.addText(rt.getAllPagesHtml());
		//RPTInit.getInsertSql(v.getDiancxxb_id(),sql,rt,getRptTitle(),""+rptCustomKey);
		return bt;
	}
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin = false;
		JDBCcon con = new JDBCcon();
		Document doc = new Document();
		doc.addParagraph(getHuiz(con));
		doc.addParagraph(getZibcslcx(con));
		
		con.Close();
		return doc.getHtml();
	}
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//	-------------------------电厂Tree-----------------------------------------------------------------
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
		return getToolbar().getRenderScript();
	}
//	页面初始化
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setTreeid_dc(visit.getDiancxxb_id() + "");
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
			isBegin = true;
			
		}
		initToolbar();
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
			isBegin = true;
		}
		initToolbar();
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
}