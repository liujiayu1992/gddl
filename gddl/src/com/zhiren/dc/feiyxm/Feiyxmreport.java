package com.zhiren.dc.feiyxm;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Feiyxmreport extends BasePage {
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
	private String[] param=new String[1];
	private String[] REPORT_NAME_REZC=param;
	
	private String[] mstrReportName= new String[1];
	
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_NAME_REZC)){
			return getSelectData();
		}else{	
			return "无此报表";
		}
	}
	private String getSelectData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select fm.mingc as feiymcb_id,fx.gongs,decode(fx.shuib,1,'可抵','不可抵') as shuib,\n");
		sbsql.append(" fl.mingc as feiylbb_id,decode(fx.juflx,1,'拒付','不拒付') as juflx from FEIYXMB fx,FEIYMCB fm,diancxxb dc,FEIYLBB fl \n");
		sbsql.append("where fx.feiymcb_id=fm.id and fx.diancxxb_id=dc.id and fx.FEIYLBB_ID=fl.id and fl.leib= \n");
		sbsql.append(param[0]);
		sbsql.append(" and dc.id= \n");
		sbsql.append(visit.getDiancxxb_id());
		
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		//定义表头数据
		 String ArrHeader[][]=new String[1][5];
		 ArrHeader[0]=new String[] {"费用名称","公式","税别","费用类型","拒付类型"};

		
		//列宽
		 int ArrWidth[]=new int[] {120,120,80,100,80};



		//设置页标题
		rt.setTitle("费用项目信息表",ArrWidth);
//		rt.setDefaultTitle(1,4,"填报单位:"+danwqc,Table.ALIGN_LEFT);
//		rt.setDefaultTitle(6,4,_NianfValue.getId()+"年"+ _YuefValue.getId()+"月",Table.ALIGN_CENTER);
//		rt.setDefaultTitle(11,2,"调然16-1表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,1,0,0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCol(1);
		rt.body.ShowZero=false;
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,5,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
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
			getSelectData();
		}
		
		String[] param = null;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
            param= cycle.getRequestContext().getParameters("lx");
            if(param != null ) {
            	mstrReportName=param;
            }
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