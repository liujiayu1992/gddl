package com.zhiren.dtrlgs.jihgl;

import java.sql.ResultSet;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class KehxqyjhReport extends BasePage {
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

	private String REPORT_NAME_KEHXQYJH = "kehxqyjh";//KehxqyjhReport&lx=kehxqyjh
	private String mstrReportName = "kehxqyjh";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_KEHXQYJH)) {
			return getKehxqyjhReport();
		} else {
			return "无此报表";
		}
	}
	
	private String getKehxqyjhReport() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql=new StringBuffer();
		sql.append("select ");
		sql.append("       decode(grouping(d.mingc),1,'总计',d.mingc) dm,");
		sql.append("       decode(grouping(d.mingc)+grouping(g.mingc),1,'小计',g.mingc) gm,");
		sql.append("       decode(grouping(g.mingc)+grouping(p.mingc),1,'小计',p.mingc) gm,");
//		sql.append("       p.mingc pm,");
		sql.append("       sum(round(meil)),");
		sql.append("       k.beiz ");
		sql.append("from kehxqyjhb k, diancxxb d, pinzb p, gongysb g ");
		sql.append("where k.diancxxb_id = d.id");
		sql.append("	 and k.pinzb_id = p.id");
		sql.append("	 and k.gongysb_id = g.id");
		sql.append("	 and k.riq = to_date('" +getNianSelectValue()+"-"+ getYueSelectValue()+"-"+1+"','yyyy-mm-dd')");
		if(getTreeJib()==1){
			sql.append("	and d.jib=3 ");
		}else if(getTreeJib()==2){
			sql.append("	and d.fuid=" + getTreeid());
		}else{
			sql.append("	and d.id=" + getTreeid());
		}
		if(getGongysDropDownValue().getId()!=-1){
			sql.append("	 and g.id = " + getGongysDropDownValue().getId()+" ");
		}
		sql.append("group by rollup(d.mingc,g.mingc,(p.mingc,k.beiz)) ");
		sql.append("order by grouping(d.mingc) desc ,d.mingc,grouping(g.mingc) desc,g.mingc,grouping(p.mingc) desc,p.mingc");
		
		System.out.println("\n"+sql+"\n");
		ResultSet rs = con.getResultSet(sql.toString());
		
		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][5];
		ArrHeader[0]=new String[] {"公司","供应商","品种","煤量","备注"};
		int[] ArrWidth = new int[5];
		ArrWidth=new int[] {100,80,60,60,150};

		rt.setTitle("客户需求计划表", ArrWidth);
//		rt.title.setRowHeight(2, 40);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5,getNianSelectValue()+"年"+getYueSelectValue()+"月1日",Table.ALIGN_CENTER);
//		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));//行，0，列 
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
//		for (int i = 1; i <= 23; i++) {//居中
//			rt.body.setColAlign(i, Table.ALIGN_CENTER);
//		}
		rt.body.setColAlign(5, Table.ALIGN_LEFT);//第五列左右居中
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
//		标识第几页，共有多少页
		rt.setDefautlFooter(4,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT );
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
//		rt.body.setRowHeight(21);

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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nian = new ComboBox();
		nian.setTransform("NianfDropDown");
		nian.setWidth(60);
		nian.setListWidth(60);
		tb1.addField(nian);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yue = new ComboBox();
		yue.setTransform("YuefDropDown");
		yue.setWidth(40);
		yue.setListWidth(40);
		tb1.addField(yue);
		
		tb1.addText(new ToolbarText("-"));
		
			Visit visit = (Visit) getPage().getVisit();
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
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		cb2.setWidth(100);
		tb1.addField(cb2);
		

		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
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

    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getGongysDropDownModel().getOption(0));
    	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }
    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc\n" +
    	"from gongysb\n";
//    	"where gongysb.fuid<=0";
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"全部")) ;
        return ;
    }
   

//树
    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	private int getTreeJib(){
		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select jib from diancxxb where id="+getTreeid());
		rsl.next();
		con.Close();
		return rsl.getInt("jib");
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
		


//	年
	public IDropDownBean getNianSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getNianSelectModel()
							.getOption(DateUtil.getYear(new Date())-2007));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}
	public void setNianSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}
	public void setNianSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}
	public IPropertySelectionModel getNianSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	public void getNianSelectModels() {
				StringBuffer sql=new StringBuffer();
		int i=0;
		for(i=2007;i<DateUtil.getYear(new Date()) + 2;i++){
			sql.append("select " + i + " id," + i + " mingc from dual union all ");
		}
		sql.append("select " + i + " id," + i + " mingc from dual ");
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}
	
//	月
	public IDropDownBean getYueSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getYueSelectModel()
							.getOption(DateUtil.getMonth(new Date())));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setYueSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public void setYueSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}
	public IPropertySelectionModel getYueSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYueSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	public void getYueSelectModels() {
		StringBuffer sql=new StringBuffer();
		int i=0;
		for(i=1;i<12;i++){
			sql.append("select " + i + " id," + i + " mingc from dual union all ");
		}
		sql.append("select " + i + " id," + i + " mingc from dual ");
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
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
			setTreeid(null);
//			if(cycle.getRequestContext().getParameter("lx")!=null){
//				mstrReportName = cycle.getRequestContext().getParameter("lx");
//			}
		}
		getToolbars();
		
		blnIsBegin = true;
	}
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
