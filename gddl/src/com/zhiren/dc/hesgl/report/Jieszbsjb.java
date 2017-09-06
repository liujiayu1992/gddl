package com.zhiren.dc.hesgl.report;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

    public class Jieszbsjb extends BasePage {
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
	

		
	private String mstrReportName="";
    private boolean blnIsBegin = false;
	
        public String getPrintTable(){

	    return getdianhwhcx();
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
         
			if(cycle.getRequestContext().getParameters("yansbm") !=null) {
				
				if(cycle.getRequestContext().getParameters("yansbm")[0].lastIndexOf("(")>-1){
					
					String strtmp=cycle.getRequestContext().getParameters("yansbm")[0];
					
					visit.setString1(strtmp.substring(0,strtmp.lastIndexOf("(")));
					
					visit.setString2("yansbm");
				}else{
					
					visit.setString1(cycle.getRequestContext().getParameters("yansbm")[0]);
				
					visit.setString2("yansbm");
				}
			}
		
        getPrintTable();
        
        
        
        
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName =visit.getString1();
        }else{
        	if(visit.getString1().equals("")) {
        		mstrReportName =visit.getString1();
            }
        }
	
		blnIsBegin = true;
				
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
	private String getdianhwhcx(){
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		sbsql.append("select  z.bianm as 结算指标,j.changf as 厂方,j.gongf as 矿方,j.jies as 结算 from jieszbsjb j,zhibb z\n" );
		sbsql.append("where j.zhibb_id=z.id and z.leib=1 and j.yansbhb_id="+visit.getString4()+" order by z.bianm");


		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		String ArrHeader[][]=new String[1][8];
		ArrHeader[0]=new String[] {"结算指标","厂方","矿方","结算"};
		
		int ArrWidth[]=new int[] {100,100,100,100};
	
		rt.setTitle("---",ArrWidth);
		rt.setBody(new Table(rs,1,0,0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCol(1);
		rt.body.ShowZero=false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,1,"审核:",Table.ALIGN_RIGHT);
		rt.setDefautlFooter(3,2,"制表:",Table.ALIGN_LEFT);
		
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}


}
