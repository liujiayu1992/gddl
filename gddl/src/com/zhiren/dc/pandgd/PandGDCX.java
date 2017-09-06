package com.zhiren.dc.pandgd;

import java.sql.ResultSet;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ����
 * ʱ�䣺2012-07-27
 * ������������ѯBUG
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-28
 * ������������ѡ�糧����
 * 		�����¼�û�Ϊ�����������ô��ʾ�����������·������򣬲���ʾ�糧��
 * 		�����¼�û���Ϊ�����������ô��ʾ���������͵糧������ˢ�³��糧���е�λ��ȫ���̵���Ϣ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-10
 * �����������̵��ѯ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-12-05
 * �����������̵��ѯ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-12-20
 * �����������̵��ѯ���棬�����ܼ���
 */
public class PandGDCX   extends BasePage implements PageValidateListener{
	
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

//	����չʾ
	public String getPrintTable(){
		return getSelectData();
	}

    private String getSelectData()
    {
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
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
//		����ļ�����·��
		String Imagelj=MainGlobal.getXitxx_item("�̵�", "��ú����·��", "0", "D:\\\\zhiren\\\\pand");
		Imagelj=Imagelj.replaceAll("\\\\", "\\\\\\\\");
		Visit visit = (Visit) getPage().getVisit();
		int dcid=Integer.parseInt(visit.getDiancxxb_id()+"");
		
		String strSQL="";
		if(dcid==112){
			strSQL="SELECT DECODE(GROUPING(DANW),1,'�ܼ�',DANW)DIANC,SUM(ZK)ZK,SUM(SK)SK,SUM(CSL)CSL,SUM(SZL)SZL,SUM(YK)YK,FUJMC FROM \n" +
			"		(SELECT D.MINGC DANW,D.XUH,\n" + 
    		"               ROUND(P.ZHANGMKC,0)    ZK,\n" + 
    		"               ROUND(P.SHIPKC,0)      SK,\n" + 
    		"               ROUND(P.CHANGSL,0)     CSL,\n" + 
    		"               ROUND(P.SHUIFCTZL,0)   SZL,\n" + 
    		"               ROUND(P.YINGKD,0)      YK,\n" + 
    		"	    		DECODE(NVL(P.FUJMC,''),'','�޸���','<a href=\""+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"\\'||P.DIANCXXB_ID||'\\&filename='||GETFILENAMEVIAID(P.ID)||'\" >'||D.MINGC||'"+intyear+"��"+intMonth+"��ú���̵㱨��'||'</a>') FUJMC \n"+
    		"       FROM PAND_GDJT P, DIANCXXB D\n" + 
    		" 		WHERE P.RIQ = DATE '" + intyear + "-" + intMonth + "-01'\n" + 
    		" 		AND P.DIANCXXB_ID(+) = D.ID\n" + 
    		" 		AND (D.ID ="+dcid+" OR D.FUID="+dcid+")\n" + 
    		" 		AND D.JIB = 3)SR\n" + 
    		" 		GROUP BY ROLLUP((DANW,XUH,FUJMC))  ORDER BY GROUPING(DANW),XUH";
		}else{
			strSQL="SELECT D.MINGC,TO_CHAR(P.RIQ,'yyyy-mm-dd') riq,\n" + 
    		"               ROUND(P.ZHANGMKC,0)    ZK,\n" + 
    		"               ROUND(P.SHIPKC,0)      SK,\n" + 
    		"               ROUND(P.CHANGSL,0)     CSL,\n" + 
    		"               ROUND(P.SHUIFCTZL,0)   SZL,\n" + 
    		"               ROUND(P.YINGKD,0)      YK,\n" + 
    		"	    		DECODE(NVL(P.FUJMC,''),'','�޸���','<a href=\""+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"\\'||P.DIANCXXB_ID||'\\&filename='||GETFILENAMEVIAID(P.ID)||'\" >'||D.MINGC||'"+intyear+"��'||TO_CHAR(P.RIQ,'mm')||'��ú���̵㱨��'||'</a>') FUJMC \n"+
    		"       FROM PAND_GDJT P, DIANCXXB D\n" + 
    		" 		WHERE TO_CHAR(P.RIQ,'yyyy') = '" + intyear + "'\n" + 
    		" 		AND P.DIANCXXB_ID(+) = D.ID\n" + 
    		" 		AND (D.ID ="+getTreeid()+")\n" + 
    		" 		AND D.JIB = 3\n" + 
    		" 		ORDER BY P.RIQ,D.XUH";
		}
		
        ResultSet rs = cn.getResultSet(strSQL);
        Table tb = new Table(rs, 1, 0, 1);
        rt.setBody(tb);
        if(dcid==112){
        	ArrHeader = new String[1][7];
            ArrHeader[0] = (new String[] {"��λ", "������", "ʵ�̿��", "������", "ˮ�ֲ������", "ӯ����", "����"});
            ArrWidth = (new int[] {120, 80, 80, 80, 80, 80, 200});
            rt.setTitle(intyear + "��" + intMonth + "���̵������ѯ", ArrWidth);
            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);
        }else{
        	ArrHeader = new String[1][8];
            ArrHeader[0] = (new String[] {"��λ","����", "������", "ʵ�̿��", "������", "ˮ�ֲ������", "ӯ����", "����"});
            ArrWidth = (new int[] {120,80, 80, 80, 80, 80, 80, 200});
            rt.setTitle(intyear + "���̵������ѯ", ArrWidth);
            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);
        }
        
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();
        rt.body.ShowZero=true;
        rt.getPages();
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(rt.body.getCols(), Table.ALIGN_LEFT);
        rt.createDefautlFooter(ArrWidth);
        rt.setDefautlFooter(1, 2, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
        rt.setDefautlFooter(3, 2, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 1, "�Ʊ�:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(rt.footer.getCols(), 1, "(��Page/Pagesҳ)", Table.ALIGN_RIGHT);
        
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
		for (i = 2011; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * �·�
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

  //******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			getIDiancmcModels();
			this.setTreeid(null);
			setToolbar(null);
		}
		getToolBars();
		getSelectData();
	}

    public void getToolBars()
    {
        Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();
		double dcid=visit.getDiancxxb_id();
//		���糧����ֵ
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		if(dcid!=112){
			TextField tf = new TextField();
			tf.setId("diancTree_text");
			tf.setWidth(100);
			tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
			
			ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
			tb2.setIcon("ext/resources/images/list-items.gif");
			tb2.setCls("x-btn-icon");
			tb2.setMinWidth(20);
			
			tb1.addText(new ToolbarText("��λ:"));
			tb1.addField(tf);
			tb1.addItem(tb2);
			tb1.addText(new ToolbarText("-"));
		}
		
        tb1.addText(new ToolbarText("���:"));
        ComboBox nianf = new ComboBox();
        nianf.setTransform("NIANF");
        nianf.setWidth(60);
        tb1.addField(nianf);
        tb1.addText(new ToolbarText("-"));
        
        if(dcid==112){
            tb1.addText(new ToolbarText("�·�:"));
            ComboBox yuef = new ComboBox();
            yuef.setTransform("YUEF");
            yuef.setWidth(60);
            tb1.addField(yuef);
            tb1.addText(new ToolbarText("-"));
        }

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
	 
//	�糧��
//		�糧����
		public boolean _diancmcchange = false;
		private IDropDownBean _DiancmcValue;

		public IDropDownBean getDiancmcValue() {
			if(_DiancmcValue==null){
				_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
			}
			return _DiancmcValue;
		}

		public void setDiancmcValue(IDropDownBean Value) {
			long id = -2;
			if (_DiancmcValue != null) {
				id = _DiancmcValue.getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_diancmcchange = true;
				} else {
					_diancmcchange = false;
				}
			}
			_DiancmcValue = Value;
		}

		private IPropertySelectionModel _IDiancmcModel;

		public void setIDiancmcModel(IPropertySelectionModel value) {
			_IDiancmcModel = value;
		}

		public IPropertySelectionModel getIDiancmcModel() {
			if (_IDiancmcModel == null) {
				getIDiancmcModels();
			}
			return _IDiancmcModel;
		}

		public void getIDiancmcModels() {
			String sql="";
			sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
			_IDiancmcModel = new IDropDownModel(sql);
		}
		
		
		public String getTreeid() {
			String treeid=((Visit) getPage().getVisit()).getString2();
			if(treeid==null||treeid.equals("")){
				((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
			}
			return ((Visit) getPage().getVisit()).getString2();
		}
		public void setTreeid(String treeid) {
			((Visit) getPage().getVisit()).setString2(treeid);
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
}