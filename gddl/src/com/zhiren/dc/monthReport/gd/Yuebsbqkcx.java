package com.zhiren.dc.monthReport.gd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
 

/*
 * 作者：赵胜男
 * 时间：2012-04-12
 * 描述：制作月报上报情况查询报表
 */

public class Yuebsbqkcx   extends BasePage implements PageValidateListener{

	//开始日期
	private Date _BeginriqValue = new Date();
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	private String diancxxb_id;
	public void setDiancxxb_id(String diancxxb_id){
		this.diancxxb_id = diancxxb_id;
	}
	public String getDiancxxb_id(){
		return diancxxb_id;
	}
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
		} else {
			_BeginriqValue = _value;
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			setToolbar(null);
//	        isBegin = true;
		}
		getToolBars();
		getSelectData();
	}
	private String RT_HET="Yuebsbqkcx";//标煤耗用情况月报
	private String mstrReportName="Yuebsbqkcx";
	
	public String getPrintTable(){
//		if(!isBegin){
//			return "";
//		}
//		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "无此报表";
		}
	}

//	private boolean isBegin=false;

    private String getSelectData()
    {
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
        strSQL.append("SELECT Rownum,mingc,a,b,c,d,e,f,g,h FROM (SELECT DC.XUH,\n" +
        		"       DC.MINGC,\n" +
        		"       DECODE (NVL(ZB1.SLZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')a,\n" +
        		"       DECODE (NVL(ZB1.ZLZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')b,\n" +
        		"       DECODE (NVL(ZB2.SHZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')c,\n" +
        		"       DECODE (NVL(ZB1.HCZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')d,\n" +
        		"       DECODE (NVL(ZB1.JSZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')e,\n" +
        		"       DECODE (NVL(ZB3.SHZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')f,\n" +
        		"       DECODE (NVL(ZB4.SHZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')g,\n" +
        		"       DECODE (NVL(ZB5.SHZT, 0),0,'<font color=\"red\">未上报</font>',1,'已上报',2,'<font color=\"blue\">已回退</font>',3,'回退已上报')h\n" +
        		"  FROM (SELECT T.DIANCXXB_ID,\n" +
        		"               MAX(SL.ZHUANGT) SLZT,\n" +
        		"               MAX(ZL.ZHUANGT) ZLZT,\n" +
        		"               MAX(HC.ZHUANGT) HCZT,\n" +
        		"               MAX(JS.ZHUANGT) JSZT\n" +
        		"          FROM YUETJKJB T, YUESLB SL, YUEZLB ZL, YUEHCB HC, YUEJSBMDJ JS\n" +
        		"         WHERE T.ID = SL.YUETJKJB_ID(+)\n" +
        		"           AND T.ID = ZL.YUETJKJB_ID(+)\n" +
        		"           AND T.ID = HC.YUETJKJB_ID(+)\n" +
        		"           AND T.ID = JS.YUETJKJB_ID(+)\n" +
        		"           AND T.RIQ = DATE '" + intyear + "-" + intMonth + "-01'\n" + 
        		"         GROUP BY T.DIANCXXB_ID) ZB1,\n" + "\n" + 
        		"       (SELECT SH.DIANCXXB_ID, MAX(SH.ZHUANGT) SHZT\n" + 
        		"          FROM YUESHCHJB SH\n" + 
        		"         WHERE SH.RIQ= DATE '" + intyear + "-" + intMonth + "-01'" + 
        		"         GROUP BY SH.DIANCXXB_ID) ZB2,\n" + "\n" + 
        		"       (SELECT SH.DIANCXXB_ID, MAX(SH.ZHUANGT) SHZT\n" + 
        		"          FROM YUEZBB SH\n" + 
        		"         WHERE SH.RIQ = DATE '" + intyear + "-" + intMonth + "-01'\n" + 
        		"         GROUP BY SH.DIANCXXB_ID) ZB3,\n" + "\n" + 
        		"       (SELECT SH.DIANCXXB_ID, MAX(SH.ZHUANGT) SHZT\n" + 
        		"          FROM YUESHCYB SH\n" + 
        		"         WHERE SH.RIQ= DATE '" + intyear + "-" + intMonth + "-01'\n" + 
        		"         GROUP BY SH.DIANCXXB_ID) ZB4,\n" + "\n" + 
        		"       (SELECT SH.DIANCXXB_ID, MAX(SH.ZHUANGT) SHZT\n" + 
        		"          FROM ZAFB SH\n" + 
        		"         WHERE SH.RIQ = DATE '" + intyear + "-" + intMonth + "-01'\n" + 
        		"         GROUP BY SH.DIANCXXB_ID) ZB5,\n" + 
        		"       DIANCXXB DC\n" + " WHERE DC.ID = ZB1.DIANCXXB_ID(+)\n" + 
        		"   AND DC.ID = ZB2.DIANCXXB_ID(+)\n" + 
        		"   AND DC.ID = ZB3.DIANCXXB_ID(+)\n" + 
        		"   AND DC.ID = ZB4.DIANCXXB_ID(+)\n" + 
        		"   AND DC.ID = ZB5.DIANCXXB_ID(+)\n" + 
        		"   AND dc.jib>2 AND DC.ID NOT IN(300)\n" + 
        		"   ORDER BY dc.xuh)");
        java.sql.ResultSet rs = cn.getResultSet(strSQL.toString());
        Table tb = new Table(rs, 1, 0, 1);
        rt.setBody(tb);
        ArrHeader = new String[1][10];
        ArrHeader[0] = (new String[] {"序号", "单位", "数量", "质量", "耗存合计", "耗存", "入厂标煤单价", "月指标情况", "油收耗存", "杂费"});
        ArrWidth = (new int[] {40, 100, 80, 80, 80, 80, 80, 80, 80, 80});
        rt.setTitle(intyear + "年" + intMonth + "月报上报情况查询", ArrWidth);
        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(22);
        rt.body.setHeaderData(ArrHeader);
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();
        rt.getPages();
        rt.body.setColAlign(1, 1);
        rt.body.setColAlign(2, 1);
        rt.createDefautlFooter(ArrWidth);
        rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()), -1);
        rt.setDefautlFooter(10, 2, "审核:", -1);
        rt.setDefautlFooter(14, 3, "制表:", -1);
        rt.setDefautlFooter(rt.footer.getCols() - 1, 2, "(第Page/Pages页)", 2);
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if(_AllPages == 0)
            _CurrentPage = 0;
        cn.Close();
        return rt.getAllPagesHtml();
    
	}

//	年份
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}
 
	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * 月份
	 */
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
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


    public void getToolBars()
    {
        Toolbar tb1 = new Toolbar("tbdiv");
        tb1.addText(new ToolbarText("年份:"));
        ComboBox nianf = new ComboBox();
        nianf.setTransform("NIANF");
        nianf.setWidth(60);
        tb1.addField(nianf);
        tb1.addText(new ToolbarText("-"));
        tb1.addText(new ToolbarText("月份:"));
        ComboBox yuef = new ComboBox();
        yuef.setTransform("YUEF");
        yuef.setWidth(60);
        tb1.addField(yuef);
        tb1.addText(new ToolbarText("-"));
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