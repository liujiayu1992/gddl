package com.zhiren.jt.zdt.xiasmxq;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author ly
 * @2009-08-14
 * 下水煤需求查询
 *
 */
public class XiasmxqReport  extends BasePage implements PageValidateListener{

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

	public String getPrintTable() {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		int[] ArrWidth;
		
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intmonth;
		if (getYuefValue() == null) {
			intmonth = DateUtil.getMonth(new Date());
		} else {
			intmonth = getYuefValue().getId();
		}
		int jib=this.getDiancTreeJib();
		
		StringBuffer strRow = new StringBuffer();
	      strRow.append(
	          "select gongys as 发货单位,pinz as 煤种\n" +
	          "from\n" + 
	          "(select s.quanc as shengf,\n" + 
	          "       decode(grouping(s.quanc)+grouping(g.mingc),2,'合计',1,s.quanc,g.mingc) as gongys,\n" + 
	          "       decode(grouping(s.quanc)+grouping(g.mingc)+grouping(p.mingc),0,p.mingc,'小计') as pinz\n" + 
	          "from xiasmxqtb x,gongysb g,shengfb s,pinzb p,diancxxb d\n" + 
	          "where x.gongysb_id = g.id\n" + 
	          "      and g.shengfb_id = s.id\n" + 
	          "      and x.pinz_id = p.id\n" + 
	          "		 and x.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
	          "		 and x.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
	          "		 and x.diancxxb_id = d.id\n" +
	          "		 and (d.id = "+this.getTreeid()+" or d.fuid = "+this.getTreeid()+")\n " +
	          "group by rollup (s.quanc,g.mingc,p.mingc)\n" + 
	          "having not (count(g.mingc) + grouping(g.mingc) + grouping(p.mingc) )=2)\n" + 
	          "order by decode(shengf,'',0,1),shengf,decode(gongys,shengf,0,1),gongys,decode(pinz,'小计',0,1),pinz");
	      
	      //得到列标题-电厂
	      StringBuffer strCol = new StringBuffer();
	      strCol.append(
	          "select '' as 收货单位,decode(grouping(d.mingc), 0, d.mingc, '合计') as 电厂\n" +
	          "from diancxxb d, xiasmxqtb x\n" + 
	          "where x.diancxxb_id = d.id\n" + 
	          "		 and x.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
	          "		 and x.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
	          "		 and (d.id = "+this.getTreeid()+" or d.fuid = "+this.getTreeid()+")\n " +
	          "group by rollup(d.mingc) order by grouping(d.mingc) desc,max(d.xuh)");
	      
	      
	      //总数据
	      StringBuffer sbsql = new StringBuffer();
	      sbsql.append(
	          "select\n" +
	          "'' as 收货单位,\n" +
	          "decode(grouping(d.mingc),1,'合计',d.mingc) as 电厂,\n" + 
	          "decode(grouping(g.mingc),0,g.mingc,decode(grouping(s.quanc),0,s.quanc,'合计')) as 发货单位,\n" + 
	          "decode(grouping(p.mingc),0,p.mingc,'小计') as 煤种,\n" + 
	          "round_new(sum(j.xuql)/10000,2) as 需求量,round_new(sum(j.hedl)/10000,2) as 核定量,round_new(sum(j.hetl)/10000,2) as 合同量\n" + 
	          "from xiasmxqtb j,\n" + 
	          "diancxxb d,gongysb g,shengfb s,pinzb p\n" + 
	          "where j.diancxxb_id=d.id and j.gongysb_id=g.id and j.pinz_id=p.id and g.shengfb_id=s.id\n" + 
	          "		 and j.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
	          "		 and j.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
	          "		 and (d.id = "+this.getTreeid()+" or d.fuid = "+this.getTreeid()+")\n " +
	          "group by cube (d.mingc,s.quanc,g.mingc,p.mingc)");
	      
	      
	      
	      //*******************************************************************************//
	      if(jib==1){//选集团时刷新出所有的电厂
	    	  strRow.setLength(0);
	    	  strRow.append(
	    	          "select gongys as 发货单位,pinz as 煤种\n" +
	    	          "from\n" + 
	    	          "(select s.quanc as shengf,\n" + 
	    	          "       decode(grouping(s.quanc)+grouping(g.mingc),2,'合计',1,s.quanc,g.mingc) as gongys,\n" + 
	    	          "       decode(grouping(s.quanc)+grouping(g.mingc)+grouping(p.mingc),0,p.mingc,'小计') as pinz\n" + 
	    	          "from xiasmxqtb x,gongysb g,shengfb s,pinzb p,diancxxb d\n" + 
	    	          "where x.gongysb_id = g.id\n" + 
	    	          "      and g.shengfb_id = s.id\n" + 
	    	          "      and x.pinz_id = p.id\n" + 
	    	          "		 and x.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
	    	          "		 and x.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
	    	          "		 and x.diancxxb_id = d.id\n" +
	    	          "group by rollup (s.quanc,g.mingc,p.mingc)\n" + 
	    	          "having not (count(g.mingc) + grouping(g.mingc) + grouping(p.mingc) )=2)\n" + 
	    	          "order by decode(shengf,'',0,1),shengf,decode(gongys,shengf,0,1),gongys,decode(pinz,'小计',0,1),pinz");
	    	      
	    	  //得到列标题-电厂
	    	  strCol.setLength(0);
		      strCol.append(
		          "select '' as 收货单位,decode(grouping(d.fgsmc), 0, d.fgsmc, '合计') as 电厂\n" +
		          "from vwdianc d, xiasmxqtb x\n" + 
		          "where x.diancxxb_id = d.id\n" + 
		          "		 and x.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
		          "		 and x.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
		          "group by rollup(d.fgsmc) order by grouping(d.fgsmc) desc,max(d.xuh)");
		      
		      
		      //总数据
		      sbsql.setLength(0);
		      sbsql.append(
		          "select\n" +
		          "'' as 收货单位,\n" +
		          "decode(grouping(d.fgsmc),1,'合计',d.fgsmc) as 电厂,\n" + 
		          "decode(grouping(g.mingc),0,g.mingc,decode(grouping(s.quanc),0,s.quanc,'合计')) as 发货单位,\n" + 
		          "decode(grouping(p.mingc),0,p.mingc,'小计') as 煤种,\n" + 
		          "round_new(sum(j.xuql)/10000,2) as 需求量,round_new(sum(j.hedl)/10000,2) as 核定量,round_new(sum(j.hetl)/10000,2) as 合同量\n" +
		          "from xiasmxqtb j,\n" + 
		          "vwdianc d,gongysb g,shengfb s,pinzb p\n" + 
		          "where j.diancxxb_id=d.id and j.gongysb_id=g.id and j.pinz_id=p.id and g.shengfb_id=s.id\n" + 
		          "		 and j.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
		          "		 and j.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
		          "group by cube (d.fgsmc,s.quanc,g.mingc,p.mingc)");
		      
		      
			}else if (jib==3){
				strRow.setLength(0);
		    	  strRow.append(
		    	          "select gongys as 发货单位,pinz as 煤种\n" +
		    	          "from\n" + 
		    	          "(select s.quanc as shengf,\n" + 
		    	          "       decode(grouping(s.quanc)+grouping(g.mingc),2,'合计',1,s.quanc,g.mingc) as gongys,\n" + 
		    	          "       decode(grouping(s.quanc)+grouping(g.mingc)+grouping(p.mingc),0,p.mingc,'小计') as pinz\n" + 
		    	          "from xiasmxqtb x,gongysb g,shengfb s,pinzb p,diancxxb d\n" + 
		    	          "where x.gongysb_id = g.id\n" + 
		    	          "      and g.shengfb_id = s.id\n" + 
		    	          "      and x.pinz_id = p.id\n" + 
		    	          "		 and x.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
		    	          "		 and x.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
		    	          "		 and x.diancxxb_id = d.id\n" +
		    	          "	     and d.id="+this.getTreeid()+"\n " +
		    	          "group by rollup (s.quanc,g.mingc,p.mingc)\n" + 
		    	          "having not (count(g.mingc) + grouping(g.mingc) + grouping(p.mingc) )=2)\n" + 
		    	          "order by decode(shengf,'',0,1),shengf,decode(gongys,shengf,0,1),gongys,decode(pinz,'小计',0,1),pinz");
		    	  
//				得到列标题-电厂
				  strCol.setLength(0);
			      strCol.append(
			          "select '' as 收货单位,decode(grouping(d.mingc), 0, d.mingc, '合计') as 电厂\n" +
			          "from diancxxb d, xiasmxqtb x\n" + 
			          "where x.diancxxb_id = d.id\n" + 
			          "		 and x.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
			          "		 and x.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
			          "	     and d.id="+this.getTreeid()+"\n " +
			          "group by rollup(d.mingc) order by grouping(d.mingc) desc,max(d.xuh)");
			      
			      
			      //总数据
			      sbsql.setLength(0);
			      sbsql.append(
			          "select\n" +
			          "'' as 收货单位,\n" +
			          "decode(grouping(d.mingc),1,'合计',d.mingc) as 电厂,\n" + 
			          "decode(grouping(g.mingc),0,g.mingc,decode(grouping(s.quanc),0,s.quanc,'合计')) as 发货单位,\n" + 
			          "decode(grouping(p.mingc),0,p.mingc,'小计') as 煤种,\n" + 
			          "round_new(sum(j.xuql)/10000,2) as 需求量,round_new(sum(j.hedl)/10000,2) as 核定量,round_new(sum(j.hetl)/10000,2) as 合同量\n" +
			          "from xiasmxqtb j,\n" + 
			          "diancxxb d,gongysb g,shengfb s,pinzb p\n" + 
			          "where j.diancxxb_id=d.id and j.gongysb_id=g.id and j.pinz_id=p.id and g.shengfb_id=s.id\n" + 
			          "		 and j.riq>=to_date('"+intyear+"-"+intmonth+"-01','yyyy-MM-dd') " +
			          "		 and j.riq<add_months(to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd'),1)\n" +
			          "	     and d.id="+this.getTreeid()+"\n " +
			          "group by cube (d.mingc,s.quanc,g.mingc,p.mingc)");
			      
			}
	      
	      try{
				ResultSetList rl = cn.getResultSetList(sbsql.toString());
				
				if(!rl.next()){
					return "无数据！";
				}
				
				rl.close();

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}  

	    cd.setRowNames("发货单位,煤种");
	    cd.setColNames("收货单位,电厂");
	    cd.setDataNames("合同量,需求量,核定量");
	    cd.setDataOnRow(false);
	    cd.setRowToCol(false);
	    //生成棋盘表数组
	    cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		
	    ArrWidth = new int[cd.DataTable.getCols()];
		int cols = ArrWidth.length;
		ArrWidth[0] = 150;
		ArrWidth[1] = 50;
		for (int i = 2; i < ArrWidth.length; i++) {
			ArrWidth[i] = 45;
		}

		rt.setBody(cd.DataTable);
		
		rt.body.setWidth(ArrWidth);

		rt.body.ShowZero = true;
		rt.setTitle("秦皇岛下水电煤"+intyear+"年"+intmonth+"月份需求计划提报统计汇总表", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		for (int i=3;i<=cd.DataTable.getCols();i++){
			cd.DataTable.setCellValue(1, i, "收货单位");
		}
		cd.DataTable.setCellValue(1, 1, "发货单位");
		cd.DataTable.setCellValue(2, 1, "发货单位");
		cd.DataTable.setCellValue(3, 1, "发货单位");
		cd.DataTable.setCellValue(1, 2, "煤种");
		cd.DataTable.setCellValue(2, 2, "煤种");
		cd.DataTable.setCellValue(3, 2, "煤种");
		for (int i=1;i<=cd.DataTable.getRows();i++){
			String shuj=cd.DataTable.getCellValue(i, 2);
			if(shuj.equals("小计")){
				cd.DataTable.setCellValue(i, 2, "");
			}
			
		}
		rt.body.mergeFixedRowCol();
		rt.setDefaultTitle(1, 2, "编制单位:"+getTreeDiancmc(this.getTreeid()),Table.ALIGN_LEFT);
		rt.setDefaultTitle(cols-2, 3, "单位: 万吨",Table.ALIGN_LEFT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}
	
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}


//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();
			this.setTreeid(null);
		}
		getToolBars();
		
	}

//	******************************************************************************
	//年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
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
	
//	月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel5() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean5((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean5();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean5()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean5(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel5(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel5(_value);
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		
		tb1.addText(new ToolbarText("月份"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.getElementById('RefurbishButton').click();}");
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

//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	private String treeid;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}

}