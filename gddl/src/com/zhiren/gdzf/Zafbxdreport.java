package com.zhiren.gdzf;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
 

/*
 * 作者：夏峥
 * 时间：2013-11-14
 * 描述：增加电厂树
 */
public class Zafbxdreport  extends BasePage implements PageValidateListener{
	
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
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			setToolbar(null);
			setTreeid(null);
			setDiancmcModels();
		}
		getToolBars() ;
//		getSelectData();
	}
	
	public String getPrintTable(){
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
		
//		System.out.print(getTreeid());
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;

		//报表内容
			strSQL.append(

					"SELECT DECODE(GROUPING(ZF.BIANH), 1, '本月合计', '<a href=\"#\" onclick=\"openLinkZaf('||chr(39)||ZF.BIANH||chr(39)||')\">'||ZF.BIANH||'</a>') BIANH,\n" +
					"       SUM(ZF.ZONGJE) HJ,\n" + 
					"       SUM(MX.L1) L1,\n" + 
					"       SUM(MX.L2) L2,\n" + 
					"       SUM(MX.L3) L3,\n" + 
					"       SUM(MX.L4) L4,\n" + 
					"       SUM(MX.L5) L5\n" + 
					"  FROM ZAFFYBXD ZF,\n" + 
					"       (SELECT SR.ID,\n" + 
					"               SR.BIANH,\n" + 
					"               MAX(DECODE(SR.MINGC, '煤场管理', SR.JINE, 0)) L1,\n" + 
					"               MAX(DECODE(SR.MINGC, '二次搬倒', SR.JINE, 0)) L2,\n" + 
					"               MAX(DECODE(SR.MINGC, '煤泥翻晒', SR.JINE, 0)) L3,\n" + 
					"               MAX(DECODE(SR.MINGC, '大块煤碾压', SR.JINE, 0)) L4,\n" + 
					"               MAX(DECODE(SR.MINGC, '燃料管理服务费', SR.JINE, 0)) L5\n" + 
					"          FROM (SELECT BXD.ID,\n" + 
					"                       BXD.BIANH,\n" + 
					"                       MX.MINGC,\n" + 
					"                       SUM(ROUND(DJB.DANJ * DJB.SHUL, 2)) JINE\n" + 
					"                  FROM ZAFFYBXD            BXD,\n" + 
					"                       ZAFFYBXD_ZAFHTFYDJB ZJB,\n" + 
					"                       ZAFHTFYDJB          DJB,\n" + 
					"                       CHANGNFYXMB         MX\n" + 
					"                 WHERE ZJB.ZAFFYBXD_ID = BXD.ID\n" + 
					"                   AND ZJB.ZAFHTFYDJB_ID = DJB.ID\n" + 
					"                   AND MX.SHIYZT = 1\n" + 
					"                   AND DJB.CHANGNFYXMB_ID = MX.ID\n" + 
					"                   AND BXD.RIQ BETWEEN DATE'"+intyear+"-"+intMonth+"-01'\n" + 
					"                   AND ADD_MONTHS(DATE '"+intyear+"-"+intMonth+"-01', 1) - 1\n" + 
					"                 GROUP BY BXD.ID, BXD.BIANH, MX.MINGC) SR\n" + 
					"         GROUP BY SR.ID, SR.BIANH\n" + 
					"         ORDER BY SR.ID, SR.BIANH) MX,DIANCXXB DC\n" + 
					" WHERE ZF.ID = MX.ID\n" + 
					"   AND ZF.RIQ BETWEEN DATE '"+intyear+"-"+intMonth+"-01'\n" + 
					"   AND ADD_MONTHS(DATE '"+intyear+"-"+intMonth+"-01', 1) - 1\n" + 
					"	AND ZF.DIANCXXB_ID =DC.ID\n" +
					"	AND (DC.ID="+getTreeid()+" OR DC.FUID="+getTreeid()+")\n" + 
					" GROUP BY ROLLUP((ZF.BIANH))\n" + 
					"UNION ALL\n" + 
					"SELECT DECODE(GROUPING(ZF.BIANH), 1, '本年累计', '<a href=\"#\" onclick=\"openLinkZaf('||chr(39)||ZF.BIANH||chr(39)||')\">'||ZF.BIANH||'</a>') BIANH,\n" + 
					"       SUM(ZF.ZONGJE) HJ,\n" + 
					"       SUM(MX.L1) L1,\n" + 
					"       SUM(MX.L2) L2,\n" + 
					"       SUM(MX.L3) L3,\n" + 
					"       SUM(MX.L4) L4,\n" + 
					"       SUM(MX.L5) L5\n" + 
					"  FROM ZAFFYBXD ZF,\n" + 
					"       (SELECT SR.ID,\n" + 
					"               SR.BIANH,\n" + 
					"               MAX(DECODE(SR.MINGC, '煤场管理', SR.JINE, 0)) L1,\n" + 
					"               MAX(DECODE(SR.MINGC, '二次搬倒', SR.JINE, 0)) L2,\n" + 
					"               MAX(DECODE(SR.MINGC, '煤泥翻晒', SR.JINE, 0)) L3,\n" + 
					"               MAX(DECODE(SR.MINGC, '大块煤碾压', SR.JINE, 0)) L4,\n" + 
					"               MAX(DECODE(SR.MINGC, '燃料管理服务费', SR.JINE, 0)) L5\n" + 
					"          FROM (SELECT BXD.ID,\n" + 
					"                       BXD.BIANH,\n" + 
					"                       MX.MINGC,\n" + 
					"                       SUM(ROUND(DJB.DANJ * DJB.SHUL, 2)) JINE\n" + 
					"                  FROM ZAFFYBXD            BXD,\n" + 
					"                       ZAFFYBXD_ZAFHTFYDJB ZJB,\n" + 
					"                       ZAFHTFYDJB          DJB,\n" + 
					"                       CHANGNFYXMB         MX\n" + 
					"                 WHERE ZJB.ZAFFYBXD_ID = BXD.ID\n" + 
					"                   AND ZJB.ZAFHTFYDJB_ID = DJB.ID\n" + 
					"                   AND MX.SHIYZT = 1\n" + 
					"                   AND DJB.CHANGNFYXMB_ID = MX.ID\n" + 
					"                   AND BXD.RIQ BETWEEN trunc(DATE '"+intyear+"-"+intMonth+"-01','y')\n" + 
					"                   AND ADD_MONTHS(DATE '"+intyear+"-"+intMonth+"-01', 1) - 1\n" + 
					"                 GROUP BY BXD.ID, BXD.BIANH, MX.MINGC) SR\n" + 
					"         GROUP BY SR.ID, SR.BIANH\n" + 
					"         ORDER BY SR.ID, SR.BIANH) MX,DIANCXXB DC\n" + 
					" WHERE ZF.ID = MX.ID\n" + 
					"   AND ZF.RIQ BETWEEN trunc(DATE '"+intyear+"-"+intMonth+"-01','y')\n" + 
					"   AND ADD_MONTHS(DATE '"+intyear+"-"+intMonth+"-01', 1) - 1\n" + 
					"	AND ZF.DIANCXXB_ID =DC.ID\n" +
					"	AND (DC.ID="+getTreeid()+" OR DC.FUID="+getTreeid()+")\n" + 
					" GROUP BY ROLLUP((ZF.BIANH))\n" + 
					" HAVING GROUPING(ZF.BIANH)=1");
			 ArrHeader=new String[1][7];
			 ArrHeader[0]=new String[] {"单据编号","合计(元)","煤场管理费(元)","二次搬倒费(元)","煤泥翻晒费(元)","大块煤碾压费(元)","燃料管理服务费(元)"};
			 
			 ArrWidth=new int[] {200,120,120,120,120,120,120};
//			 iFixedRows=1;
			 arrFormat=new String[]{"","0.00","0.00","0.00","0.00","0.00","0.00"};
		
			ResultSet rs = cn.getResultSet(strSQL.toString());
			 
			// 数据
			Table tb = new Table(rs,1, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(intyear+"年"+intMonth+"月厂内费用明细", ArrWidth);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.ShowZero = true;
			rt.body.setColFormat(arrFormat);
		 
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			 
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
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

	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
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
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
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

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			setDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
	public void setDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
	}
	
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
}