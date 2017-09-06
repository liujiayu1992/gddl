package com.zhiren.jt.zdt.xitgl.gonghdw;

import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Gongys extends BasePage implements PageValidateListener {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
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
	
	private String _mingc;
	
	public void setMingc(String mingc){
		_mingc = mingc;
	}
	
	public String getMingc(){
		return _mingc;
	}
	
	private String _bianm;
	
	public void setBianm(String bianm){
		_bianm = bianm;
	}
	
	public String getBianm(){
		return _bianm;
	}
	
	
	private void Insert(IRequestCycle cycle) {
		Visit visit = ((Visit) getPage().getVisit());
		String lianpID = "-1";
		visit.setboolean1(false);
		visit.setboolean2(false);
		cycle.getRequestContext().getRequest().setAttribute("id", lianpID);
		visit.setString1(lianpID);
		cycle.activate("Gonghdwdmsq_insert");
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
//		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Gongysbean) _list.get(i)).getXXX();
		getSelectData();
		// setMsg();
	}
	
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}



	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert(cycle);
		}
	}

	private Gongysbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Gongysbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Gongysbean EditValue) {
		_EditValue = EditValue;
	}
	

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getcontext() {
        return this.getRequestCycle().getRequestContext().getRequest()
                .getScheme()
                + "://"
                + this.getRequestCycle().getRequestContext().getServerName()
                + ":"
                + this.getRequestCycle().getRequestContext().getServerPort()
                + this.getEngine().getContextPath();
    }

	public void getSelectData() {
		Visit visit = ((Visit) getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String dianc="";
		if(visit.isJTUser()){
			dianc="";
		}else if(visit.isGSUser()){
			dianc="and dc.fuid="+visit.getDiancxxb_id();
		}else if(visit.isDCUser()) {
			dianc="and dc.id="+visit.getDiancxxb_id();
		}
		String context = MainGlobal.getHomeContext(this);
		String chaxun = "select g.id as id,\n"
				+ "       g.xuh as xuh,\n"
				+ "       g.bianm as bianm,\n"
				+ "       g.quanc as quanc,\n"
				+ "       g.mingc as mingc,\n"
				+ "       shengfb.quanc as shengf,\n"
				+ "       g.fuid as fuid,\n"
				+ "       decode(g.fuid, 0, '大供货单位', '小供货单位') as leib,\n"
				+ "       g.shenqr as shenqr,\n"
				+ "       decode(g.zhuangt, 0, '批复中', '批复通过') as zhuangt,\n"
				+ "       decode(1,1,'查看') as chak,\n"
				+ "       decode(1,1,'修改') as xiug\n"
				+ "  from gonghdwdmsqb g, shengfb ,gongysdcglb gl,diancxxb dc\n"
				+ " where shengfb.id = g.shengfb_id\n"
				+ " and gl.gongysb_id=g.id\n" 
				+ " and gl.diancxxb_id=dc.id "+dianc+"\n"
				+ " order by g.xuh, g.mingc";
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
	
		egu.setTableName("gonghdwdmsqb");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(40);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("shengf").setHeader("省份");
		egu.getColumn("shengf").setWidth(80);
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("shenqr").setHeader("申请人");
		egu.getColumn("shenqr").setWidth(80);
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setWidth(80);
		egu.getColumn("chak").setHeader("");
		egu.getColumn("xiug").setHeader("");
		egu.getColumn("chak").setRenderer(
						"function(value,p,record){return String.format('<a href="+context+"/app?service=page/{1}&id={2}&zhuangt=1>{0}</a>',value,'Gonghdwdmsq_insert',record.data['ID']);}");
		egu
		.getColumn("xiug")
		.setRenderer(
				        "function(value,p,record){return String.format('<a href="+context+"/app?service=page/{1}&id={2}&zhuangt=2>{0}</a>',value,'Gonghdwdmsq_insert',record.data['ID']);}");
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("xiug").setEditor(null);
		egu.getColumn("chak").setEditor(null);
		egu.getColumn("fuid").setHidden(true);
		visit.setboolean2(true);
		egu.addPaging(22);
	
		egu.addToolbarItem("{"+new GridButton("添加","function(){document.getElementById('InsertButton').click();}").getScript()+"}");
		String str=
       		" var url = 'http://'+document.location.host+document.location.pathname;"+
            "var end = url.indexOf(';');"+
			"url = url.substring(0,end);"+
       	    "url = url + '?service=page/' + 'GonghdwdmReport';" +
       	    " window.open(url,'newWin');";
		String str1=
			"if(gridDiv_sm.getSelected()==null){alert('请选择供货单位！');return;}" +
       		" var url = 'http://'+document.location.host+document.location.pathname;"+
            "var end = url.indexOf(';');"+
			"url = url.substring(0,end);"+
       	    "url = url + '?service=page/' + 'GonghdwdmReport&gongysb_id='+gridDiv_sm.getSelected().get('ID');" +
       	    " window.open(url,'newWin');";
		
		//egu.addToolbarItem("{"+new GridButton("打印供应单位列表","function (){"+str+"}").getScript()+"}");
		//egu.addToolbarItem("{"+new GridButton("打印供应商明细","function (){"+str1+"}").getScript()+"}");

		
		setExtGrid(egu);
		con.Close();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setList1(null);
			setMingc(null);
			setBianm(null);
			getShengfModels();
			getGongyslbModels();
			
			getSelectData();
		}
	
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



	public IDropDownBean getShengfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getShengfModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setShengfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getShengfModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getShengfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getShengfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setShengfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getShengfModels() {
		String sql = "select id, quanc from shengfb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));
	}

	public IDropDownBean getGongyslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongyslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongyslbValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongyslbModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getGongyslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongyslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setGongyslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getGongyslbModels() {
		String sql = "select lb.*\n"
				+ "  from (select 0 as id, decode(1, 1, '大供货单位') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1, 1, '小供货单位') as mingc from dual) lb";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
	}

	private int m_pages = 0;

	public int getPages() {
		return m_pages;
	}

	public void setPages(int pages) {
		m_pages = pages;
	}



	private int m_totalpages = 0;

	public int getTotalPages() {
		return m_totalpages;
	}

	public void setTotalPages(int _value) {
		m_totalpages = _value;
	}

	private int m_currentpage = 0;

	public int getCurrentPage() {
		return m_currentpage;
	}

	public void setCurrentPage(int _value) {
		m_currentpage = _value;
	}

	private int m_page = 1;

	public int getGoPage() {
		return m_page;
	}

	public void setGoPage(int page) {
		m_page = page;
	}

	private StringBuffer m_sql = new StringBuffer("");

	public StringBuffer getSQL() {
		return m_sql;
	}

	public void setSQL(StringBuffer sql) {
		m_sql = sql;
	}

	
}