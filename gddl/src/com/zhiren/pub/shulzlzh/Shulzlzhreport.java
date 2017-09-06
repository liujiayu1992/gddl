package com.zhiren.pub.shulzlzh;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shulzlzhreport extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
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

	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	
//	***************设置消息框******************//
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
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	private String[] param=new String[2];
	private String[] mstrReportName = new String[2];
	
	public String getPrintTable(){
		if(mstrReportName.equals(param)){
			return getSelectData();
		}else{	
			return "无此报表";
		}
	}
	private String getSelectData(){
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		String YUEF="";
		if(Integer.parseInt(param[1])<10){
			YUEF="0"+param[1];
		}else{
			YUEF=param[1];
		}
	//id 不同，其他数据相同，不选id则缺少页面上的数据	
		sbsql.append("select distinct s.id as id, g.mingc as gongysb_id,y.mingc as yunsfsb_id,\n");
		sbsql.append(" p.mingc as pinzb_id,j.mingc as jihkjb_id,s.biaoz,s.jingz,s.yingd,s.kuid,s.yuns,s.koud,s.farl, \n");
		sbsql.append("s.huiff,s.liuf,s.quansf,s.shuif,s.huif \n");
		sbsql.append("from shulzlzhb s, gongysb g,yunsfsb y,jihkjb j,fahb f,zhilb z,pinzb p \n");
		sbsql.append("where s.gongysb_id=g.id(+) and s.yunsfsb_id=y.id(+) and s.pinzb_id=p.id(+) and s.jihkjb_id=j.id(+) \n");
		sbsql.append(" and to_char(riq,'yyyy-mm') ='"+ param[0] + "-"+ YUEF+"' \n");
		
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		//定义表头数据
		 String ArrHeader[][]=new String[1][17];
		 ArrHeader[0]=new String[] {"id","供应商","运输方式","品种","计划口径","票重","净重","赢吨","亏吨","运损","扣吨","发热量","挥发分","硫分","全水分","水分","灰分"};

		
		//列宽
		 int ArrWidth[]=new int[] {60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60};



		//设置页标题
		rt.setTitle("数量质量信息表",ArrWidth);
//		rt.setDefaultTitle(1,4,"填报单位:"+danwqc,Table.ALIGN_LEFT);
//		rt.setDefaultTitle(6,4,_NianfValue.getId()+"年"+ _YuefValue.getId()+"月",Table.ALIGN_CENTER);
//		rt.setDefaultTitle(11,2,"调然16-1表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,1,0,0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.setColCells(1, 13, false);
		rt.body.setCells(1, 1, 1, 1, 13, false);
		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCol(1);
		rt.body.ShowZero=false;
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,17,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
//		rt.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
//		rt.setDefautlFooter(19,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
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
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);
			
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
            param= cycle.getRequestContext().getParameters("lx");
            if(param != null ) {
            	mstrReportName=param;
            }
            getSelectData();
        }
	}
	
	public String getcontext() {
			return "";
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).get() + "';";
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
