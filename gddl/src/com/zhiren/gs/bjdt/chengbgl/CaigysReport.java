package com.zhiren.gs.bjdt.chengbgl;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class CaigysReport extends BasePage {
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	
	// ***************设置消息框******************//
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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}
	
	
	public String getPrintTable(){
		return getCaigysb();
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
    
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid_dc();
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	
	

	private String getCaigysb() {
		JDBCcon cn = new JDBCcon();
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID = "" ;
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancTreeID=" and d.id= " +this.getTreeid_dc();
			
		}
		
		
		StringBuffer sbsql = new StringBuffer();

		sbsql.append(
				"select\n" +
				"decode(grouping(d.mingc),1,'公司合计',d.mingc)dc,\n" + 
				"decode(grouping(d.mingc)+grouping(j.mingc),2,'"+this.getNianfValue().getValue()+"年预算',1,'"+this.getNianfValue().getValue()+"年预算',\n" + 
				"decode(grouping(j.mingc)+grouping(g.mingc),0,g.mingc,j.mingc||'合计')) jh,\n" + 
				" sum(round_new(n.dinghl,0))as dinghl,\n" + 
				" sum(round_new(n.yujdhl,0))as yujdhl,\n" + 
				" sum(round_new(n.caigl,0))as caigl,\n" + 
				" round_new(sum(n.caigl*10000*n.daocrz)/sum(n.caigl*10000)*1000,0)rez,\n" + 
				" round_new(sum(n.chukjg*n.caigl*10000)/sum(n.caigl*10000),2)jg,\n" + 
				" round_new(sum(n.yunj*n.caigl*10000)/sum(n.caigl*10000),2)yunj,\n" + 
				" round_new(sum(n.zaf*n.caigl*10000)/sum(n.caigl*10000),2)zaf,\n" + 
				" round_new(sum(n.qitfy*n.caigl*10000)/sum(n.caigl*10000),2)qtfy ,\n" + 
				" round_new(sum(n.daoczhjbhs*n.caigl*10000)/sum(n.caigl*10000),2)dczhjbhs,\n" + 
				" round_new(sum(n.daoczhjhs*n.caigl*10000)/sum(n.caigl*10000),2)dczhjhs,\n" + 
				" round_new(sum(n.buhsbmdj*n.caigl*10000)/sum(n.caigl*10000),2)bhsbmdj\n" + 
				" from niancgysb n,diancxxb d, gongysb g,jihkjb j\n" + 
				"where n.diancxxb_id=d.id and n.gongysb_id=g.id and j.id=n.jihkjb_id\n" +
				strdiancTreeID+" and n.riq=to_date("+getNianfValue().getValue()+",'yyyy') "+
				"group by rollup(d.mingc,j.mingc,g.mingc)\n" + 
				"order by grouping(d.mingc) desc,d.mingc,\n" + 
				"grouping(j.mingc) desc,j.mingc,\n" + 
				"grouping(g.mingc) desc,g.mingc");
//        System.out.println(sbsql.toString());
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][13];
		ArrHeader[0] = new String[] { "单位", "矿别", "订货量(万吨)", "预计到货量（万吨）", "采购量（万吨）", "到厂热值（KJ/kg）",
				"出矿价格（元/吨）", "运价", "杂费", "其他费用","到厂综合价（含税）","到厂综合价（不含税）","不含税标煤单价" };

		int ArrWidth[] = new int[] { 100,100,100,100,60,60,60,60,60,60,120,120,
				120 };

		rt.setTitle("燃煤采购预算表（明细）", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		for(int i = 2; i< rt.body.getRows() ; i++){
			rt.body.merge(i, 1, i, 2);
		}
		rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 5);
		
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
		}
		getSelectData();
	}


//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());

            this.setNianfValue(null);
            this.getNianfModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
	}
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		年份
		tb1.addText(new ToolbarText("年份:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("Nianf");
		pinz.setWidth(80);
		pinz.setListeners("select:function(own,rec,index){Ext.getDom('Nianf').selectedIndex=index}");
		tb1.addField(pinz);
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



		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}
	
//	 年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	
	
	//电厂树
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

	
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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
}
