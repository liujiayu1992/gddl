package com.zhiren.dc.huaygl.huaybb.feihbgd;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Feihbbtz extends BasePage {
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

	private boolean blnIsBegin = false;

	// private String leix = "";

	// private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;

		return getFeihlzfx();
	}

	private String getFeihlzfx() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();

		sbsql
				.append("select decode(grouping(r.caiyrq),1,'合计',to_char(r.caiyrq, 'yyyy-mm-dd')) as caiyrq, \n" +
		"j.jizbh as jizb_id,f.mingc as fenxxmb_id, \n"+
        "round(avg(zhi),2) as zhi \n"+
        "from rulfhhyb r, jizb j,fenxxmb f where r.jizb_id=j.id \n"+
        "and r.fenxxmb_id=f.id \n"+
        "and r.fenxrq>="+DateUtil.FormatOracleDate(getBRiq())+" \n"+
        "and r.fenxrq<="+DateUtil.FormatOracleDate(getERiq())+" \n"+
        "and r.diancxxb_id="+getTreeid()+" \n"+
        "and r.jizb_id = "+getJizbhValue().getId()+" \n"+
        "having(grouping(r.caiyrq) + grouping(r.fenxrq) + grouping(f.mingc) + \n"+
                "grouping(j.jizbh) = 0 \n"+
             "or grouping(r.caiyrq) + grouping(r.fenxrq) + grouping(f.mingc) + \n"+
                "grouping(j.jizbh) = 4) \n"+
        "group by rollup(r.caiyrq, r.fenxrq, j.jizbh, f.mingc) \n"+
        "order by r.caiyrq,r.fenxrq,j.jizbh");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][7];
		ArrHeader[0] = new String[] { "采样日期", "机组编号", "分析项目", "平均值"};

		int ArrWidth[] = new int[] { 120, 100, 120, 100 };

		rt.setTitle("飞灰及炉渣可燃物报表台帐", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2, 2, "分析人:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核人:", Table.ALIGN_RIGHT);
		rt.setDefaultTitle(1, 2, "分析日期：" + getBRiq()+" 至 "+getERiq(), Table.ALIGN_LEFT);
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

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			setDiancmcModels();
			setDiancjb();	
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel4(null);
			visit.setDropDownBean4(null);
			getJizbhModels() ;
			setJizbhValue(null) ;
			getJizbhModel() ;
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				;
			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			// mstrReportName = visit.getString1();
		} 
		if(tree){
			visit.setProSelectionModel4(null);
			visit.setDropDownBean4(null);
			getJizbhModels() ;
			setJizbhValue(null) ;
			getJizbhModel() ;
			tree=false;
		}
		blnIsBegin = true;

		getSelectData();

	}

//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//		绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	private boolean tree=false;
	public void setTreeid(String treeid) {
		
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
				tree=true;
			}
		}
		this.treeid = treeid;
		
	}

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			setDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
	public void setDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
	}
	
	public IPropertySelectionModel Diancjb;
	public void setDiancjb() {
		String sql = "select id,jib from diancxxb";
		Diancjb =new IDropDownModel(sql);
	}
	
//机组编号
	
	public boolean _Jizbhchange = false;

	public IDropDownBean getJizbhValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getJizbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setJizbhValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean4(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getJizbhModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getJizbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setJizbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getJizbhModels() {
		JDBCcon con = new JDBCcon();
       String sql="select id,jizbh from jizb where diancxxb_id= "+getTreeid();
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("分析日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"Form0",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("机组编号:"));
		ComboBox jz = new ComboBox();
		jz.setTransform("JizbhDropDown");
		jz.setEditable(true);
		jz.setWidth(80);
		//jz.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(jz);
		
		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
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
