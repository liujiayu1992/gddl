package com.zhiren.dc.monthReport;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
public class Yuercbmdjbmx extends BasePage {
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	//  
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		String zhuangtWhere="";
		if(getZhuangtValue().getId()==-1){//全部
			zhuangtWhere=" ";
		}else if(getZhuangtValue().getId()==1){//未结算
			zhuangtWhere=" and r.zhuangt<2 ";
		}else{//2已经结算
			zhuangtWhere=" and r.zhuangt=2 ";
		}
		buffer.append(
		"select g.mingc gongysmc,jihkjb.mingc jihkj,pinzb.mingc pinz,yunsfsb.mingc yunsfs,m.mingc meikdwmc,caiyb.bianm,\n" +
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.meij,0,0,laimsl)),0,0,sum(f.laimsl*r.meij)/sum(decode(r.meij,0,0,laimsl)))),0),2)meij,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.meijs,0,0,laimsl)),0,0,sum(f.laimsl*r.meijs)/sum(decode(r.meijs,0,0,laimsl)))),0),2)meijs,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.yunj,0,0,laimsl)),0,0,sum(f.laimsl*r.yunj)/sum(decode(r.yunj,0,0,laimsl)))),0),2)yunj,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.yunjs,0,0,laimsl)),0,0,sum(f.laimsl*r.yunjs)/sum(decode(r.yunjs,0,0,laimsl)))),0),2)yunjs,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.jiaohqzf,0,0,laimsl)),0,0,sum(f.laimsl*r.jiaohqzf)/sum(decode(r.jiaohqzf,0,0,laimsl)))),0),2)jiaohqzf,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.zaf,0,0,laimsl)),0,0,sum(f.laimsl*r.zaf)/sum(decode(r.zaf,0,0,laimsl)))),0),2)zaf,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.daozzf,0,0,laimsl)),0,0,sum(f.laimsl*r.daozzf)/sum(decode(r.daozzf,0,0,laimsl)))),0),2)daozzf,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.qitfy,0,0,laimsl)),0,0,sum(f.laimsl*r.qitfy)/sum(decode(r.qitfy,0,0,laimsl)))),0),2)qitfy,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.qnet_ar,0,0,laimsl)),0,0,sum(f.laimsl*r.qnet_ar)/sum(decode(r.qnet_ar,0,0,laimsl)))),0),2)daozzf,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.relzj,0,0,laimsl)),0,0,sum(f.laimsl*r.relzj)/sum(decode(r.relzj,0,0,laimsl)))),0),2)relzj,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.liuzj,0,0,laimsl)),0,0,sum(f.laimsl*r.liuzj)/sum(decode(r.liuzj,0,0,laimsl)))),0),2)liuzj,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.huifzj,0,0,laimsl)),0,0,sum(f.laimsl*r.huifzj)/sum(decode(r.huifzj,0,0,laimsl)))),0),2)huifzj,\n" + 
		"round(nvl(decode(sum(f.laimsl),0,0,decode(sum(decode(r.shuifzj,0,0,laimsl)),0,0,sum(f.laimsl*r.shuifzj)/sum(decode(r.shuifzj,0,0,laimsl)))),0),2)shuifzj,\n" + 
		"round(nvl(decode(sum(f.laimsl*r.Qnet_ar),0,0,sum((r.meij+r.meijs+r.yunj+r.yunjs+r.jiaohqzf+r.zaf+r.daozzf+r.qitfy)*f.laimsl*29.271)/sum(f.laimsl*r.Qnet_ar)),0),2) biaomdj,\n" + 
		"decode(f.zhilb_id,null,'',decode(max(r.zhuangt),0,'数量估价',1,'质量估价',2,'结算价',''))zhuangt "+
		"from ruccb r,fahb f,gongysb g,meikxxb m,caiyb,jihkjb,pinzb,yunsfsb\n" + 
		"where r.fahb_id=f.id and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.zhilb_id=caiyb.zhilb_id and f.jihkjb_id=jihkjb.id\n" + 
		"and f.pinzb_id=pinzb.id and yunsfsb.id=f.yunsfsb_id\n" + 
		"and f.daohrq>=to_date('"+getRiq1()+"','yyyy-mm-dd') and f.daohrq<=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
		zhuangtWhere + 
		" group by  grouping sets((g.mingc,jihkjb.mingc,pinzb.mingc,yunsfsb.mingc,m.mingc,m.mingc,caiyb.bianm,f.zhilb_id)\n" + 
		",(g.mingc,jihkjb.mingc,pinzb.mingc,yunsfsb.mingc))");


		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][21];
		ArrWidth = new int[] { 80, 52, 40,40,80,70,40,40,40,40,40,40,40,40,40,40,40,40,40,40,50};
		ArrHeader[0] = new String[] { "供应商", "口径", "品种","运输<br>方式","煤矿","批次编码","煤价","煤价税","运价","运价税","交换前杂费","杂费","到站<br>杂费","其他<br>费用","热量<br>Mj/kc","热量<br>折价","硫分<br>折价","灰分<br>折价","水分<br>折价","标煤<br>单价","状态"};
		rt.setBody(new Table(rs, 1, 0, 6));
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("月 度 标 煤 单 价 明 细", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 8, "单位:(元)" ,Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
		rt.body.setPageRows(21);
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 21, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	boolean riqichange = false;
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(DateUtil.getFirstDayOfMonth( DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intMonth)));
		}
		return riq1;
	}
	public void setRiq1(String riq) {
		if (this.riq1 != null && !this.riq1.equals(riq)) {
			this.riq1 = riq;
			riqichange = true;
		}
	}
	
	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 =  DateUtil.FormatDate(DateUtil.getLastDayOfMonth( DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intMonth)));
		}
		return riq2;
	}
	public void setRiq2(String riq) {
		if (this.riq2!= null && !this.riq2.equals(riq)) {
			this.riq2 = riq;
			riqichange = true;
		}
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ1");
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		
		tb1.addText(new ToolbarText("状态:"));
		ComboBox comb1=new ComboBox();
		comb1.setWidth(100);
		comb1.setTransform("Zhuangt");
		comb1.setLazyRender(true);//动态绑定weizSelect
		tb1.addField(comb1);
		ToolbarButton tb = new ToolbarButton(null, "刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
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
			visit.setDefaultTree(null);
			setTreeid(null);
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
	//状态
	public IPropertySelectionModel getZhuangtModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getZhuangtModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}
	public IDropDownBean getZhuangtValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getZhuangtModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}
	
	public void setZhuangtValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getZhuangtModels() {
		  List list=new ArrayList();
	        list.add(new IDropDownBean(-1,"全部"));
	        list.add(new IDropDownBean(1,"未结算"));
	        list.add(new IDropDownBean(2,"已结算"));
	        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setZhuangtModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(_value);
	}
	//
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
