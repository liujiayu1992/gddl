package com.zhiren.dc.caiygl;

import java.util.Date;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Caiymxcx  extends BasePage implements PageValidateListener{
	
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
		String strSQL=
			"SELECT ROWNUM XUH,\n" +
			"       SR.MINGC,\n" + 
			"       SR.CHEPH,\n" + 
			"       SR.MAOZ,\n" + 
			"       SR.PIZ,\n" + 
			"       SR.JINGZ,\n" + 
			"       SR.ZHONGCSJ\n" + 
			"  FROM (SELECT MK.MINGC,\n" + 
			"               CP.CHEPH,\n" + 
			"               CP.MAOZ,\n" + 
			"               CP.PIZ,\n" + 
			"               CP.MAOZ - CP.PIZ JINGZ,\n" + 
			"               TO_CHAR(CP.ZHONGCSJ, 'yyyy-mm-dd HH24:mi:ss') ZHONGCSJ\n" + 
			"          FROM CAIYB C, YANGPDHB Y, ZHUANMB Z, CHEPB CP, FAHB F, MEIKXXB MK\n" + 
			"         WHERE F.ZHILB_ID = C.ZHILB_ID\n" + 
			"           AND Y.CAIYB_ID = C.ID\n" + 
			"           AND Z.ZHILLSB_ID = Y.ZHILBLSB_ID\n" + 
			"           AND CP.FAHB_ID = F.ID\n" + 
			"           AND F.MEIKXXB_ID = MK.ID\n" + 
			"           AND Z.ID = "+getBianmValue().getStrId()+"\n" + 
			"         ORDER BY CP.ZHONGCSJ, CP.CHEPH) SR";
		
        java.sql.ResultSet rs = cn.getResultSet(strSQL);
        
		String ArrHeader[][] = new String[2][8];
		String caiybh="<B>采样编号的车辆信息</B>";
		if(!getBianmValue().getStrId().equals("-1")){
			caiybh="<B>采样编号"+getBianmValue().getValue()+"的车辆信息</B>";
		}
	
		ArrHeader[0] = new String[] { caiybh,caiybh,caiybh,caiybh,caiybh,caiybh,caiybh };
		ArrHeader[1] = new String[] { "序号","煤矿名称","车牌号","毛重","皮重","净重","入厂时间" };
		int ArrWidth[] = new int[] { 60, 150, 100, 80, 80, 80, 150};
		
		Report rt = new Report();
		rt.setBody(new Table(rs, 2, 0, 1));
		
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();
        rt.body.ShowZero=true;
        rt.getPages();
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
      
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
			setRIQ(DateUtil.FormatDate(new Date()));
			setBianmValue(null);
			setBianmModel(null);
			setToolbar(null);
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}
		getToolBars();
		getSelectData();
	}

//	工具条
    public void getToolBars()
    {
        Toolbar tb1 = new Toolbar("tbdiv");
        
        tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(getRIQ());
		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("化验编码:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(150);
		shij.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		
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
	 
//	 日期是否变化
	private boolean riqchange = false;

//	绑定日期
	public String getRIQ() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRIQ(String riq) {
		if (((Visit) this.getPage().getVisit()).getString1() != null) {
			if (!((Visit) this.getPage().getVisit()).getString1().equals(riq))
				riqchange = true;
		}
		((Visit) this.getPage().getVisit()).setString1(riq);
	}
	
//	绑定采样编码下拉框
	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
//		通过日期刷新采样编码
		String SQL="SELECT DISTINCT Z.ID, Z.BIANM\n" +
		"  FROM (SELECT DISTINCT ZHILB_ID\n" + 
		"          FROM CHEPB C, FAHB F\n" + 
		"         WHERE C.FAHB_ID = F.ID\n" + 
		"           AND F.YUNSFSB_ID != 1\n" + 
		"           AND (TO_DATE(TO_CHAR(C.ZHONGCSJ, 'yyyy-mm-dd'), 'yyyy-mm-dd') = TO_DATE('"+getRIQ()+"', 'yyyy-mm-dd')\n" + 
		"               OR TO_DATE(TO_CHAR(C.QINGCSJ, 'yyyy-mm-dd'), 'yyyy-mm-dd') =TO_DATE('"+getRIQ()+"', 'yyyy-mm-dd'))\n" + 
		"        UNION\n" + 
		"        SELECT DISTINCT ZHILB_ID\n" + 
		"          FROM CHEPB C, FAHB F\n" + 
		"         WHERE C.FAHB_ID = F.ID\n" + 
		"           AND F.YUNSFSB_ID = 1\n" + 
		"           AND TO_DATE(TO_CHAR(C.ZHONGCSJ, 'yyyy-mm-dd'), 'yyyy-mm-dd') = TO_DATE('"+getRIQ()+"', 'yyyy-mm-dd')) J,\n" + 
		"       CAIYB C,YANGPDHB Y,ZHUANMB Z,ZHUANMLB ZZ\n" + 
		" WHERE J.ZHILB_ID = C.ZHILB_ID\n" + 
		"   AND Y.CAIYB_ID = C.ID\n" + 
		"   AND Z.ZHILLSB_ID = Y.ZHILBLSB_ID\n" + 
		"   AND Z.ZHUANMLB_ID = ZZ.ID\n" + 
		"   AND ZZ.JIB = 1";

		setBianmModel(new IDropDownModel(SQL, "请选择"));
	}

}