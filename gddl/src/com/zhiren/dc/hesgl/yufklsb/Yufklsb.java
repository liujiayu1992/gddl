package com.zhiren.dc.hesgl.yufklsb;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;

import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


    public class Yufklsb extends BasePage {
	public boolean getRaw() {
		return true;
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
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	private String REPORT_NAME_DIANHWHCX="jieszbsjb";
		
	private String mstrReportName="";
    private boolean blnIsBegin = false;
	
        public String getPrintTable(){

	    return Mingx();
    }
		

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

 		if (!visit.getActivePageName().toString().equals(
			this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);
			visit.setString1("");
			visit.setString2("");
		}
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName =visit.getString1();
        }else{
        	if(visit.getString1().equals("")) {
        		mstrReportName =visit.getString1();
            }
        }
//		blnIsBegin = true;
		getPrintTable();
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
	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+  getpageLink() + "','');";
		} else {
			return "";
		}
	}
	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
	public String Mingx() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();

		sbsql.append("select rownum as xuh,to_char(yl.shiysj,'yyyy-mm-dd'),yl.jine,y.yue,yl.beiz  from yufklsb yl,yufkb y where yl.yufkb_id=y.id and y.id="+mstrReportName);

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][5];
		ArrHeader[0] = new String[] { "序号", "使用日期", "使用金额", "余额", "备注" };

		int ArrWidth[] = new int[] { 50, 100, 100, 70, 200 };

		rt.setTitle("预付款使用明细", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(4, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}

}
