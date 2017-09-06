package com.zhiren.dc.jilgl.baob;

import java.util.Date;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * @author Rock
 * @since 2009-07-15
 * @description 邯郸电厂提出的 带有运输方式的数量台帐报表
 */
public class Jiltz_ysfs extends BasePage {

	private static final String BAOBPZB_GUANJZ = "JILTZ_YSFS";// baobpzb中对应的关键字
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
    
	private String getBaseSql() {
		String diancid = getTreeid_dc() ;
		String sql = 
			"select decode(grouping(d.mingc)+grouping(y.mingc),2,'合计',1,d.mingc||'小计',d.mingc) dcmc,\n" + 
			"       decode(grouping(y.mingc) + grouping(f.daohrq),1,y.mingc ||'小计',y.mingc) ysfs,\n" + 
			"       decode(grouping(f.daohrq) + grouping(f.chec),1,'日小计',to_char(f.daohrq,'yyyy-mm-dd')) daohrq,\n" + 
			"       f.chec,\n" + 
			"       m.mingc mk,\n" + 
			"       p.mingc pz,\n" + 
			"       sum(f.ches) ches,\n" + 
			"       sum(f.jingz) jingz,\n" + 
			"       sum(f.biaoz) biaoz,\n" + 
			"       sum(f.maoz) maoz,\n" + 
			"       sum(f.piz) piz,\n" + 
			"       sum(f.yuns) yuns\n" + 
			"  FROM FAHB f,\n" + 
			"       gongysb g,\n" + 
			"       meikxxb m,\n" + 
			"       pinzb p,\n" + 
			"       yunsfsb y,\n" + 
			"       (select id, xuh, fuid, mingc\n" + 
			"          from diancxxb\n" + 
			"         start with id = "+diancid+"\n" + 
			"        connect by fuid = prior id) d\n" + 
			" where f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq())+"\n" + 
			"   and f.daohrq <= "+DateUtil.FormatOracleDate(getERiq())+"\n" + 
			"   and f.gongysb_id = g.id\n" + 
			"   and f.meikxxb_id = m.id\n" + 
			"   and f.pinzb_id = p.id\n" + 
			"   and f.diancxxb_id = d.id\n" + 
			"   and f.yunsfsb_id = y.id\n" + 
			" group by rollup(d.mingc, y.mingc, f.daohrq, f.chec, m.mingc, p.mingc)\n" + 
			" having (grouping(f.chec) = 1 or grouping(p.mingc)=0)\n" + 
			" order by grouping(d.mingc) ,max(d.xuh),d.mingc, grouping(y.mingc), grouping(f.daohrq)";
		return sql;
	}

	
//	刷新衡单列表
	public void getSelectData() {
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
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
//		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+v.getInt1()+"' order by xuh");
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+BAOBPZB_GUANJZ+v.getInt1()+"' order by xuh");
        ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+BAOBPZB_GUANJZ+v.getInt1()+"'");
        	String Htitle="数量台帐" ;
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	 ArrHeader = new String[][] {{Locale.diancxxb_id_fahb, Locale.yunsfsb_id_fahb, Locale.daohrq_id_fahb, Locale.chec_fahb, 
    			Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb, Locale.ches_fahb, Locale.jingz_fahb,
    			Locale.biaoz_fahb, Locale.maoz_fahb, Locale.piz_fahb, Locale.yuns_fahb} };
    
    		 ArrWidth = new int[] {80, 80, 80, 80, 100, 80, 50, 50, 50, 50, 50, 50 };
    
    		rt.setTitle("数量台帐", ArrWidth);
    		strFormat = new String[] { "", "", "", "", "", "", "",
    				"", "", "" };
        }
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 5, getBRiq() + " 至 " + getERiq(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 3, "单位：吨、车", Table.ALIGN_RIGHT);

		rt.setBody(new Table(rs, 1, 0, 3));
		//rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(4, Table.ALIGN_CENTER);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
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
		RPTInit.getInsertSql(v.getDiancxxb_id(),getBaseSql().toString(),rt,"数量台帐",""+BAOBPZB_GUANJZ+v.getInt1());
     	return rt.getAllPagesHtml();// ph;
	}
//	工具栏使用的方法
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
			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
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
}
