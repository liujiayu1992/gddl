package com.zhiren.pub.reny;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Renyqxcx   extends BasePage implements PageValidateListener{
	
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

//	报表展示
	public String getPrintTable(){
		return getSelectData();
	}

    private String getSelectData()
    {
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		String strZYSQL="SELECT DECODE(Z.JIB,\n" +
			"              3,\n" + 
			"              '&nbsp&nbsp&nbsp' || Z.MINGC,\n" + 
			"              2,\n" + 
			"              '&nbsp&nbsp' || Z.MINGC,\n" + 
			"              1,\n" + 
			"              '&nbsp' || Z.MINGC) MINGC,\n" + 
			"       Z.MINGC ZYMC,\n" + 
			"       Z.WENJWZ\n" + 
			"  FROM ZIYXXB Z\n" + 
			" START WITH FUID = 0\n" + 
			"CONNECT BY PRIOR ID = FUID";
		
		String strrySQL="SELECT DISTINCT R.QUANC--, ZY.MINGC, ZY.WENJWZ\n" +
			"  FROM (SELECT * FROM RENYXXB) R,\n" + 
			"       (SELECT RENYXXB_ID, ZUXXB_ID FROM RENYZQXB) Q,\n" + 
			"       (SELECT ZUXXB_ID, ZIYXXB_ID FROM ZUQXB) Z,\n" + 
			"       (SELECT MINGC, WENJWZ, ID FROM ZIYXXB) ZY,\n" + 
			"       ZUXXB ZX\n" + 
			" WHERE R.ID = Q.RENYXXB_ID\n" + 
			"   AND Q.ZUXXB_ID = Z.ZUXXB_ID\n" + 
			"   AND Z.ZIYXXB_ID = ZY.ID\n" + 
			"   AND ZX.ID = Q.ZUXXB_ID\n" + 
			"   AND R.Diancxxb_Id=304";

		String strryzySQL="SELECT R.QUANC, ZY.MINGC, ZY.WENJWZ\n" +
			"  FROM (SELECT * FROM RENYXXB) R,\n" + 
			"       (SELECT RENYXXB_ID, ZUXXB_ID FROM RENYZQXB) Q,\n" + 
			"       (SELECT ZUXXB_ID, ZIYXXB_ID FROM ZUQXB) Z,\n" + 
			"       (SELECT MINGC, WENJWZ, ID FROM ZIYXXB) ZY,\n" + 
			"       ZUXXB ZX\n" + 
			" WHERE R.ID = Q.RENYXXB_ID\n" + 
			"   AND Q.ZUXXB_ID = Z.ZUXXB_ID\n" + 
			"   AND Z.ZIYXXB_ID = ZY.ID\n" + 
			"   AND ZX.ID = Q.ZUXXB_ID\n" + 
			"   AND R.Diancxxb_Id=304";
		
        ResultSetList zyrs = cn.getResultSetList(strZYSQL);
        ResultSetList ryrs = cn.getResultSetList(strrySQL);
        ResultSetList ryzyrs = cn.getResultSetList(strryzySQL);
        
        int totalcol=ryrs.getRows();
        int totalrow=zyrs.getRows();
        String ArrHeader[][]=new String[totalrow+1][totalcol+1];
        
        int rownum=0;
        while(zyrs.next()){
        	
        	if(rownum==0){
        		int col=1;
        		while(ryrs.next()){
        			ArrHeader[0][col]=ryrs.getString("QUANC");
        			col++;
        		}
        	}
        	ArrHeader[rownum+1][0]=zyrs.getString("MINGC");
        	int colnum=0;
        	ryrs.beforefirst();
        	while(ryrs.next()){
        		ArrHeader[rownum+1][colnum+1]=" ";
        		ryzyrs.beforefirst();
        		while(ryzyrs.next()){
        			if(ryzyrs.getString("QUANC").equals(ryrs.getString("QUANC"))&&(ryzyrs.getString("MINGC")+ryzyrs.getString("WENJWZ")).equals(zyrs.getString("ZYMC")+zyrs.getString("WENJWZ"))){
        				ArrHeader[rownum+1][colnum+1]="X";
        				break;
        			}
        		}        		
        		colnum++;
        	}
        	rownum++;
        }

//        int ArrWidth[]=new int[] {45,70,54,70,54,54,80,60,54,70,70};
		Report rt=new Report();
//		 定义页Title
//		Cell c = new Cell();
//		c.setBorderNone();
//		Table title = new Table(4, ArrWidth.length, c);
//		title.setWidth(ArrWidth);
//		title.setBorderNone();
//		title.setCellValue(2, 1, "国电内蒙古东胜热电有限公司");
//		title.setCellAlign(2, 1, Table.ALIGN_CENTER);
//		title.setCellFont(2, 1, "", 16, true);
//		title.mergeRowCells(2);
		
//		rt.setTitle(title);			
		rt.setBody(new Table(ArrHeader,0,0,0));
//		rt.body.setWidth(ArrWidth);
        
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if(_AllPages == 0)
            _CurrentPage = 0;
        cn.Close();
        return rt.getAllPagesHtml();
    
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
	 //	页面判定方法
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

  //******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setToolbar(null);
		}
		getToolBars();
		getSelectData();
	}

    public void getToolBars()
    {
        Toolbar tb1 = new Toolbar("tbdiv");

        ToolbarButton tb = new ToolbarButton(null, "刷新", "function(){document.Form0.submit();}");
        tb.setIcon(SysConstant.Btn_Icon_Refurbish);
        tb1.addItem(tb);
        setToolbar(tb1);
    }

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	 public String getToolbarScript(){
	      return getToolbar().getRenderScript();
	}
}