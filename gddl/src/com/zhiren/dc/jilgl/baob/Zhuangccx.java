package com.zhiren.dc.jilgl.baob;

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
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;


public class Zhuangccx extends BasePage {
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

	// 得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
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



	public String getPrintTable() {
		setMsg(null);
        if(getPoundValue().getId()==0){
        	return getYunsdwcx_hz();
			
		}else{
			return getYunsdwcx_mx();
		}
		
	
	}

	private String getYunsdwcx_hz() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String yunsdw="";
		String meikdw="";
		
		if(getYunsdwValue().getId()==-1){
			yunsdw="";
		}else{
			yunsdw="and zhuangcdw_item_id ="+
        		            getYunsdwValue().getId();
		}
		if(getTreeid().equals("0")){
			meikdw="";
		}else{
			meikdw="and meikxxb_id="+getTreeid();
		}
		
/*
 * 改成按zhongcsj统计
 */	
		StringBuffer sbsql = new StringBuffer();
        sbsql.append(
        		"select meikxxb.mingc,item.mingc,sum(chepb.maoz) as maoz,sum(chepb.piz) as piz,sum(chepb.maoz)-sum(chepb.piz)-sum(chepb.zongkd) as jinz,sum(chepb.zongkd) as koud,count(item.mingc) as ches\n" +
        		"from chepb,meikxxb,fahb,item\n" + 
        		"where chepb.zhuangcdw_item_id=item.id and chepb.fahb_id=fahb.id and fahb.meikxxb_id=meikxxb.id and zhongcsj>=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
        		"and zhongcsj<=to_date('"+getRiq2()+"','yyyy-mm-dd')+1\n" + 
        		yunsdw+
        		meikdw+
        		"group by meikxxb.mingc,item.mingc order by meikxxb.mingc");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][] = new String[1][7];
		ArrHeader[0] = new String[] {"煤矿单位","装车单位", "毛重", "皮重",
				"净重", "总扣杂","车数" };

		int ArrWidth[] = new int[] { 100, 100, 60, 60, 60, 60, 60 };

		rt.setTitle("装车汇总信息", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		
//		rt.body.setColAlign(4, Table.ALIGN_CENTER);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);

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
	private String getYunsdwcx_mx() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String yunsdw="";
		String meikdw="";
		
		if(getYunsdwValue().getId()==-1){
			yunsdw="";
		}else{
			yunsdw=" and zhuangcdw_item_id ="+
        		            getYunsdwValue().getId();
		}
		if(getTreeid()=="0"){
			meikdw="";
		}else{
			meikdw="and meikxxb_id="+getTreeid();
		}
/*
 * 改成按zhongcsj统计
 */			
		StringBuffer sbsql = new StringBuffer();
        sbsql.append(
        		"select meikxxb.mingc, item.mingc, chepb.cheph, chepb.maoz, chepb.piz,chepb.maoz-chepb.piz-chepb.koud as jinz,chepb.koud\n" +
        		"from chepb, item,meikxxb,fahb where chepb.zhuangcdw_item_id=item.id and chepb.fahb_id=fahb.id and fahb.meikxxb_id=meikxxb.id and zhongcsj>=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
        		"and zhongcsj<=to_date('"+getRiq2()+"','yyyy-mm-dd') + 1\n" +
        		yunsdw+
        		meikdw+
        		"and meikxxb_id="+getTreeid()+
        		"order by meikxxb.mingc, item.mingc");
		ResultSet rs = cn.getResultSet(sbsql.toString());


		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] {"煤矿单位", "装车单位", "车皮号","毛重", "皮重",
				"净重", "总扣杂"};

		int ArrWidth[] = new int[] { 100, 100, 100, 60, 60, 60, 60, 60,
				60,60 };

		rt.setTitle("装车详细信息", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		
//		rt.body.setColAlign(4, Table.ALIGN_CENTER);
//		rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(40);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);

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



	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())){
			visit.setActivePageName(getPageName().toString());
			
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean4(null);
			getGongysDropDownModels();
			getYunsdwModels() ;
			setYunsdwValue(null) ;
			getYunsdwModel() ;
			getPoundModels();
			setPoundValue(null);
			getPoundModel();
			setRiqi(null);
			setRiq2(null);
			visit.setboolean3(false);
			
	

		}
		
		getSelectData();
	
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	// 卸煤点
	public boolean _Poundchange = false;

	public IDropDownBean getPoundValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getPoundModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setPoundValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getPoundModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getPoundModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setPoundModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getPoundModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			List.add(new IDropDownBean(0, "汇总"));
			List.add(new IDropDownBean(1, "明细"));

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
//运输单位
	
	public boolean _Yunsdwchange = false;

	public IDropDownBean getYunsdwValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getYunsdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYunsdwValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean4(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getYunsdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYunsdwModels() {
		JDBCcon con = new JDBCcon();
       String sql="select id,mingc from item where itemsortid=1010";
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql,"全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
//	获取供应商
	public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
        setGongysDropDownModel(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    
	public void getSelectData() {
		
		Visit visit=(Visit)getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("开始日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("结束日期:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("汇总或明细:"));
		ComboBox gh = new ComboBox();
		gh.setTransform("PoundDropDown");
		gh.setEditable(true);
		gh.setWidth(60);
		//gh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gh);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("装车单位:"));
		ComboBox ys = new ComboBox();
		ys.setTransform("YunsdwDropDown");
		ys.setEditable(true);
		ys.setWidth(80);
		//ys.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(ys);
		

		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));

		setToolbar(tb1);

	}
	
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			//treeid=String.valueOf("");
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
