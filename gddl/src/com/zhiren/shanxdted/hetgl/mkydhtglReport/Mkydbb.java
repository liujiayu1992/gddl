package com.zhiren.shanxdted.hetgl.mkydhtglReport;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Mkydbb extends BasePage implements PageValidateListener {
	
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
	
	public boolean getRaw() {
		return true;
	}
	
	public String getBriq() {
		if ("".equals(((Visit)getPage().getVisit()).getString5()) 
				|| ((Visit)getPage().getVisit()).getString5()==null) {
			setBriq(DateUtil.FormatDate(new Date()));
		}
		return ((Visit)getPage().getVisit()).getString5();
	}
	
	public void setBriq(String value) {
		((Visit)getPage().getVisit()).setString5(value);
	}
	
	public String getEriq() {
		if ("".equals(((Visit)getPage().getVisit()).getString6()) 
				|| ((Visit)getPage().getVisit()).getString6()==null) {
			setEriq(DateUtil.FormatDate(new Date()));
		}
		return ((Visit)getPage().getVisit()).getString6();
	}
	
	public void setEriq(String value) {
		((Visit)getPage().getVisit()).setString6(value);
	}
	
	public String getPrintTable() throws SQLException{	
		JDBCcon con = new JDBCcon();
		String strQisrq = getBriq();
		String strGuoqrq = getEriq() ;
		
		strQisrq = strQisrq.substring(0,4) + "年" 
			+ strQisrq.substring(5,7) + "月" 
			+ strQisrq.substring(8, 10) + "日";
		
		strGuoqrq = strGuoqrq.substring(0,4) + "年" 
			+ strGuoqrq.substring(5,7) + "月" 
			+ strGuoqrq.substring(8, 10) + "日";
		
		String SQL = 
			"SELECT YD.MINGC AS YSDW, MK.MINGC AS MKDW\n" +
			"  FROM MKYDHTGLB GL, HETB HT, MEIKXXB MK, YUNSDWB YD\n" + 
			" WHERE GL.HETB_ID = HT.ID\n" + 
			"   AND GL.MEIKXXB_ID = MK.ID\n" + 
			"   AND GL.YUNSDWB_ID = YD.ID\n" + 
			"   AND HT.Qisrq = TO_DATE('" + this.getBriq() + "', 'yyyy-mm-dd')\n" + 
			"   AND HT.GUOQRQ = TO_DATE('" + this.getEriq() + "', 'yyyy-mm-dd')\n" + 
			" ORDER BY YSDW";

		ResultSet rs = con.getResultSet(SQL);
		
		Report rt = new Report();
		
		String[][] ArrHeader = null;
		ArrHeader = new String[1][2];
		ArrHeader[0] = new String[] {"运输单位", "煤矿单位"};
		int[] ArrWidth = new int[] {300, 300};
		rt.setTitle(strQisrq + "至" + strGuoqrq + "合同段<br>运输单位及煤矿", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setBody(new Table(rs,1,0,1));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setFontSize(14);
		rt.body.setRowHeight(35);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.getPages();
		rt.body.setHeaderData(ArrHeader);//表头数据

		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		
		rs.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private void getSelectData() {
		
	}
	
//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {	
		getSelectData();			
	}
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();	
		this.setBriq(null);
		this.setEriq(null);
		if (cycle.getRequestContext().getParameter("lx") != null) {
			try {
				String sRiq = new String(cycle.getRequestContext()
					.getParameter("lx").getBytes("iso8859-1"), "gb2312");
				int pos = sRiq.indexOf("至");
				this.setBriq(sRiq.substring(0, pos));
				this.setEriq(sRiq.substring(pos+1));
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getSelectData();
	}
	
	
//	页面登陆验证
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
}
