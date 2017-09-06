package com.zhiren.gs.bjdt.diaoygl;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shouhcrbtj extends BasePage {
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

	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	
//	 得到单位全称
	public String getTianzdwQuanc(String diancxxb_ID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ diancxxb_ID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getShouhcreport();
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getShouhcreport() {
		JDBCcon con = new JDBCcon();
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
		String sql = 

			" SELECT\n" +
			
			" DECODE(GROUPING(RIQ), 1, '总计', DC.MINGC) AS DIANCMC,\n" + 
			" TO_CHAR(ZH.RIQ, 'yyyy-mm-dd') AS RIQI,\n" + 
			"SUM(ZH.DANGRGM) AS DR,\n" + 
			"DECODE(GROUPING(ZH.RIQ), 1, '',  SUM(ZH.LJ_DANGRGM)) AS LJ,\n" + 
			" SUM(ZH.HAOYQKDR) AS DRHY,\n" + 
			" DECODE(GROUPING(ZH.RIQ), 1, '', SUM(ZH.LJ_HAOYQKDR)) AS LJHY,\n" + 
			"DECODE(GROUPING(ZH.RIQ), 1, '', SUM(ZH.KUC)) AS KC\n" + 
			"  FROM (SELECT HC.DIANCXXB_ID,\n" + 
			"               HC.DANGRGM,\n" + 
			"               HC.FADY + HC.GONGRY + HC.QITY HAOYQKDR,\n" + 
			"               HC.KUC,\n" + 
			"               HC.RIQ,\n" + 
			"               DAYCOUNT(HC.RIQ) DAYSCOUNT,\n" + 
			"               TO_CHAR(HC.RIQ, 'dd') TOCHARDAYS,\n" + 
			"               (SELECT SUM(H.DANGRGM) AS DANGRGM\n" + 
			"                  FROM SHOUHCRBB H\n" + 
			"                 WHERE H.RIQ >= FIRST_DAY(HC.RIQ)\n" + 
			"                   AND H.RIQ <= HC.RIQ\n" + 
			"                   AND H.DIANCXXB_ID = HC.DIANCXXB_ID\n" + 
			"                 GROUP BY H.DIANCXXB_ID) LJ_DANGRGM,\n" + 
			"               (SELECT SUM(H.FADY + H.GONGRY + H.QITY) AS HAOYQKDR\n" + 
			"                  FROM SHOUHCRBB H\n" + 
			"                 WHERE H.RIQ >= FIRST_DAY(HC.RIQ)\n" + 
			"                   AND H.RIQ <= HC.RIQ\n" + 
			"                   AND H.DIANCXXB_ID = HC.DIANCXXB_ID\n" + 
			"                 GROUP BY H.DIANCXXB_ID) LJ_HAOYQKDR\n" + 
			"          FROM SHOUHCRBB HC\n" + 
			"         WHERE HC.RIQ >= TO_DATE('"+beginRiq+"', 'yyyy-mm-dd')\n" + 
			"           AND HC.RIQ <= TO_DATE('"+endRiq+"', 'yyyy-mm-dd')) ZH,\n" + 
			"       DIANCXXB DC\n" + 
			" WHERE ZH.DIANCXXB_ID = DC.ID\n" + 
			"   AND DC.ID in ("+this.getTreeid()+" )\n" + 
			" GROUP BY ROLLUP((RIQ, DC.MINGC))";
;


        ResultSet rs = con.getResultSet(sql);
		Report rt = new Report();
		
		String ArrHeader[][] = new String[3][7];
		ArrHeader[0] = new String[] { "单位名称", "日期", "供煤情况", "供煤情况", "耗用情况","耗用情况", "库存" };
		ArrHeader[1] = new String[] { "单位名称", "日期", "当日", "累计", "当日", "累计","库存"};
		ArrHeader[2] = new String[] { "1", "2", "3", "4", "5", "6", "7"};
		int ArrWidth[] = new int[] { 140, 80, 70, 75, 70, 75, 80};
		
		rt.setBody(new Table(rs, 3, 0, 0));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
		rt.setTitle(getTianzdwQuanc(this.getTreeid())+"收耗存日报明细查询",ArrWidth);
		rt.setDefaultTitle(5, 3, "单位:吨、元/吨、MJ/Kg", Table.ALIGN_RIGHT);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.createFooter(1, ArrWidth);
		
		rt.setDefautlFooter(1, 2, "主管：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(3, 3, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 2, "制表：", Table.ALIGN_CENTER);
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		//rt.body.setPageRows(21);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(DateUtil.getFDOfMonth(this.getEndriqDate())));
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setValue(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		
		
		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"Form0",null,getTreeid());
		
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){"+MainGlobal.getExtMessageShow("请等待", "请等待....", 200)+"document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());

			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setString4(null);
			visit.setString5(null);

		}
		getToolbars();

		blnIsBegin = true;

	}



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
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
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

	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}


}