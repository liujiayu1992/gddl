package com.zhiren.gdjh.report;

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
 * ���ߣ���ʤ��
 * ʱ�䣺2012-04-12
 * �����������±��ϱ������ѯ����
 */

public class Yuedjhsbqk   extends BasePage implements PageValidateListener{

	//��ʼ����
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

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			getNianfModels();
			setToolbar(null);
		}
		getToolBars();
		getSelectData();
	}
	private String RT_HET="Yuedjhsbqk";//��ú��������±�
	private String mstrReportName="Yuedjhsbqk";
	
	public String getPrintTable(){
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "�޴˱���";
		}
	}

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
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
        strSQL.append("SELECT Rownum,DC.MINGC,\n" +
        				"       DECODE(SR.M01C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m01C,\n" + 
        				"       DECODE(SR.M01Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m01Z,\n" + 
        				"       DECODE(SR.M02C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m02C,\n" + 
        				"       DECODE(SR.M02Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m02Z,\n" + 
        				"       DECODE(SR.M03C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m03C,\n" + 
        				"       DECODE(SR.M03Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m03Z,\n" + 
        				"       DECODE(SR.M04C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m04C,\n" + 
        				"       DECODE(SR.M04Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m04Z,\n" + 
        				"       DECODE(SR.M05C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m05C,\n" + 
        				"       DECODE(SR.M05Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m05Z,\n" + 
        				"       DECODE(SR.M06C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m06C,\n" + 
        				"       DECODE(SR.M06Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m06Z,\n" + 
        				"       DECODE(SR.M07C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m07C,\n" + 
        				"       DECODE(SR.M07Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m07Z,\n" + 
        				"       DECODE(SR.M08C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m08C,\n" + 
        				"       DECODE(SR.M08Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m08Z,\n" + 
        				"       DECODE(SR.M09C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m09C,\n" + 
        				"       DECODE(SR.M09Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m09Z,\n" + 
        				"       DECODE(SR.M10C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m10C,\n" + 
        				"       DECODE(SR.M10Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m10Z,\n" + 
        				"       DECODE(SR.M11C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m11C,\n" + 
        				"       DECODE(SR.M11Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m11Z,\n" + 
        				"       DECODE(SR.M12C,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m12C,\n" + 
        				"       DECODE(SR.M12Z,'0','<font color=\"red\">δ�ϴ�</font>','���ϴ�')m12Z\n" + 
        				"  FROM (\n" + 
        				"  SELECT DCID,\n" + 
        				"                MAX(DECODE(SR.RIQ, '01', CAIGZT, NULL)) AS M01C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '01', ZHIBZT, NULL)) AS M01Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '02', CAIGZT, NULL)) AS M02C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '02', ZHIBZT, NULL)) AS M02Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '03', CAIGZT, NULL)) AS M03C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '03', ZHIBZT, NULL)) AS M03Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '04', CAIGZT, NULL)) AS M04C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '04', ZHIBZT, NULL)) AS M04Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '05', CAIGZT, NULL)) AS M05C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '05', ZHIBZT, NULL)) AS M05Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '06', CAIGZT, NULL)) AS M06C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '06', ZHIBZT, NULL)) AS M06Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '07', CAIGZT, NULL)) AS M07C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '07', ZHIBZT, NULL)) AS M07Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '08', CAIGZT, NULL)) AS M08C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '08', ZHIBZT, NULL)) AS M08Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '09', CAIGZT, NULL)) AS M09C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '09', ZHIBZT, NULL)) AS M09Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '10', CAIGZT, NULL)) AS M10C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '10', ZHIBZT, NULL)) AS M10Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '11', CAIGZT, NULL)) AS M11C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '11', ZHIBZT, NULL)) AS M11Z,\n" + 
        				"                MAX(DECODE(SR.RIQ, '12', CAIGZT, NULL)) AS M12C,\n" + 
        				"                MAX(DECODE(SR.RIQ, '12', ZHIBZT, NULL)) AS M12Z\n" + 
        				"          FROM (SELECT TB_HEAD.ID DCID,\n" + 
        				"                       TO_CHAR(TB_HEAD.QUANNRQ, 'mm') RIQ,\n" + 
        				"                       NVL(SR.ZHUANGT, 0) CAIGZT,\n" + 
        				"                       NVL(SR2.ZHUANGT, 0) ZHIBZT\n" + 
        				"                  FROM (SELECT RIQ.QUANNRQ, DC.ID, DC.MINGC\n" + 
        				"                          FROM -- ��� ��1\n" + 
        				"                               (SELECT TO_DATE('" + intyear + "-' ||SUBSTR('0' || TO_CHAR(ROWNUM),-2,2) || '-01','yyyy-mm-dd') QUANNRQ\n" + 
        				"                                  FROM DUAL\n" + 
        				"                                CONNECT BY ROWNUM <= 12) RIQ,\n" + 
        				"                               --��2\n" + 
        				"                               (SELECT DC.ID, DC.MINGC\n" + 
        				"                                  FROM DIANCXXB DC\n" + 
        				"                                 WHERE DC.JIB = 3) DC) TB_HEAD,\n" + 
        				"                       (\n" + 
        				"                        -- ��3\n" + 
        				"                        SELECT Y.DIANCXXB_ID, Y.RIQ, 1 ZHUANGT\n" + 
        				"                          FROM YUEDJH_CAIG Y\n" + 
        				"                         WHERE TRUNC(RIQ, 'yyyy') = DATE '" + intyear + "-01-01'\n" + 
        				"                         GROUP BY Y.DIANCXXB_ID, Y.RIQ\n" + 
        				"                         ) SR,(\n" + 
        				"                         SELECT Y.DIANCXXB_ID, Y.RIQ, 1 ZHUANGT\n" + 
        				"                          FROM YUEDJH_ZHIB Y\n" + 
        				"                         WHERE TRUNC(RIQ, 'yyyy') = DATE '" + intyear + "-01-01'\n" + 
        				"                         GROUP BY Y.DIANCXXB_ID, Y.RIQ )SR2\n" + 
        				"                 WHERE TB_HEAD.QUANNRQ = SR.RIQ(+)\n" + 
        				"                   AND TB_HEAD.ID = SR.DIANCXXB_ID(+)\n" + 
        				"                   AND TB_HEAD.QUANNRQ = SR2.RIQ(+)\n" + 
        				"                   AND TB_HEAD.ID = SR2.DIANCXXB_ID(+)\n" + 
        				"                 ORDER BY ID, QUANNRQ) SR\n" + 
        				"         GROUP BY DCID) SR,  DIANCXXB DC\n" + 
        				" WHERE SR.DCID = DC.ID\n" + 
        				" ORDER BY Rownum,DC.XUH");
        java.sql.ResultSet rs = cn.getResultSet(strSQL.toString());
        Table tb = new Table(rs, 2, 0, 1);
        rt.setBody(tb);
        ArrHeader = new String[2][26];
        ArrHeader[0] = (new String[] {"���", "��λ", "һ��", "һ��", "����", "����",  "����", "����","����","����",  "����", "����",  "����", "����", "����","����", 
        		                                       "����", "����","����", "����", "ʮ��","ʮ��", "ʮһ��","ʮһ��","ʮ����","ʮ����"});
        ArrHeader[1] = (new String[] {"���", "��λ", "�²ɹ�",  "��ָ��", "�²ɹ�",  "��ָ��",  "�²ɹ�",  "��ָ��", "�²ɹ�",  "��ָ��",
        		 "�²ɹ�",  "��ָ��",  "�²ɹ�",  "��ָ��", "�²ɹ�",  "��ָ��",  "�²ɹ�",  "��ָ��",  "�²ɹ�",  "��ָ��",  "�²ɹ�",  "��ָ��",  "�²ɹ�",  "��ָ��",  "�²ɹ�",  "��ָ��"});
        ArrWidth = (new int[] {30, 80, 50, 50, 50,50, 50, 50, 50, 50, 50, 50, 50,50,50,50,50,50,50,50,50,50,50,50,50,50});
        rt.setTitle(intyear + "���¶Ȳɹ���ָ�������ѯ", ArrWidth);
        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(22);
        rt.body.setHeaderData(ArrHeader);
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();
        rt.getPages();
        rt.body.setColAlign(1, 1);
        rt.body.setColAlign(2, 1);
        rt.createDefautlFooter(ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()), -1);
        rt.setDefautlFooter(10, 2, "���:", -1);
        rt.setDefautlFooter(14, 3, "�Ʊ�:", -1);
        rt.setDefautlFooter(rt.footer.getCols() - 1, 2, "(��Page/Pagesҳ)", 2);
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if(_AllPages == 0)
            _CurrentPage = 0;
        cn.Close();
        return rt.getAllPagesHtml();
    
	}

//	���
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
		for (i = 2010; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
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

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	
//	***************************�����ʼ����***************************//
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
	 //	ҳ���ж�����
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
        tb1.addText(new ToolbarText("���:"));
        ComboBox nianf = new ComboBox();
        nianf.setTransform("NIANF");
        nianf.setWidth(60);
        tb1.addField(nianf);
        tb1.addText(new ToolbarText("-"));
        ToolbarButton tb = new ToolbarButton(null, "ˢ��", "function(){document.Form0.submit();}");
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