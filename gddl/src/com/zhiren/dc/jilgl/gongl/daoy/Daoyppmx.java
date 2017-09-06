package com.zhiren.dc.jilgl.gongl.daoy;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Daoyppmx extends BasePage {
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
	public String getPrintTable() {
		setMsg(null);
        if(getPoundValue().getId()==0){
        	return getDaoypp_hz();
			
		}else{
			return getDaoypp_mx();
		}
		
	
	}

	private String getDaoypp_hz() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();	
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select gongysb_id,meikxxb_id,pinzb_id,faz_id, jihkjb_id,fahrq,daohrq,maoz,piz,jingz,biaoz,nvl(ches,0) as ches,nvl(zhongl,0) as zhongl,nvl(jingz,0)-nvl(zhongl,0) as chaz from (");
        sbsql.append("select * from (select f.id,g.mingc gongysb_id, m.mingc meikxxb_id,p.mingc pinzb_id,c.mingc faz_id,");
        sbsql.append("j.mingc jihkjb_id,f.fahrq,f.daohrq,f.maoz,f.piz,f.jingz,f.biaoz ");
        sbsql.append(" from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, jihkjb j where f.gongysb_id = g.id   and f.meikxxb_id = m.id   and f.pinzb_id = p.id   and f.faz_id = c.id   and f.jihkjb_id = j.id   and f.id=");
        sbsql.append(((Visit) this.getPage().getVisit()).getString1());
        sbsql.append("order by f.daohrq desc) a left join(select fahb_id,count(fahb_id) ches,sum(maoz-piz) as zhongl from daozcpb where fahb_id=");
        sbsql.append(((Visit) this.getPage().getVisit()).getString1());
        sbsql.append("group by fahb_id) b on a.id=b.fahb_id) huizxx");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][] = new String[1][7];
		ArrHeader[0] = new String[] {"供应商名称","煤矿名称", "品种", "发站",
				"计划口径", "发货日期","到货日期" ,"毛重","皮重","净重","票重","二次车数","二次重量","差值"};

		int ArrWidth[] = new int[] { 100, 100, 60, 60, 60, 60, 60,60,60,60,60,60 ,60,60};

		rt.setTitle("倒运车辆汇总信息", ArrWidth);
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
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
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
	private String getDaoypp_mx() {
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();

		StringBuffer sbsql = new StringBuffer();
        sbsql.append("select d.gongysmc,d.meikdwmc,d.pinz,d.faz,d.jihkj,d.yunsdw,d.chec,d.cheph,d.qingcsj,d.zhongcsj,d.maoz,d.piz,d.biaoz,d.lury from daozcpb d where 1=1 ");
        sbsql.append(" and d.fahb_id=").append(((Visit) this.getPage().getVisit()).getString1()).append(" order by qingcsj desc");
		ResultSet rs = cn.getResultSet(sbsql.toString());


		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] {"供应商名称", "煤矿名称", "品种","发站", "计划口径",
				"运输单位", "车次","车号","轻车时间","重车时间","毛重","皮重","票重","录入人员"};

		int ArrWidth[] = new int[] { 100, 100, 100, 60, 60, 60, 60, 60,
				60,60 ,60,60,60,60};

		rt.setTitle("倒运车辆详细信息", ArrWidth);
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
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		rt.body.setColAlign(12, Table.ALIGN_CENTER);
		rt.body.setColAlign(13, Table.ALIGN_CENTER);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		
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
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ReturnChick){
			_ReturnChick=false;
			gotoDaoypp(cycle);
			
		}
	}

	private void gotoDaoypp(IRequestCycle cycle) {

//		System.out.println(((Visit) this.getPage().getVisit()).getString1());
		cycle.activate("Daoypp");
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())){
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			getPoundModels();
			setPoundValue(null);
			getPoundModel();

			visit.setboolean3(false);
			
	

		}
		
		getSelectData();
	
	}
	
	public void getSelectData() {
		
		Visit visit=(Visit)getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("-"));


		tb1.addText(new ToolbarText("汇总或明细:"));
		ComboBox gh = new ComboBox();
		gh.setTransform("PoundDropDown");
		gh.setEditable(true);
		gh.setWidth(60);
		//gh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gh);
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		ToolbarButton retrunbt = new ToolbarButton(null,"返回","function(){document.getElementById('ReturnButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Return);
		tb1.addItem(retrunbt);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));

		setToolbar(tb1);

	}

	// 汇总明细
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
