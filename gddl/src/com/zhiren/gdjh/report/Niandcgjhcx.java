package com.zhiren.gdjh.report;

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
import com.zhiren.common.ResultSetList;
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
 * ʱ�䣺2012-10-19
 * �����������ƻ��ھ���
 */
/*
 * ���ߣ� ��ʤ��
 * ʱ�䣺2012-11-13
 * �����������ۺ͵�����ú���ۺ�˰�Ͳ���˰������ú���� �������������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-19
 * ��������������
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-11-19
 * ��������������
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-12-17
 * ��������������������ƣ������ؼƻ��ӷ�һ�У�����Ԥ�Ƶ�������˰��ú������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-12-20
 * �����������������
 * 		����ǰ̨�������
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-12-24
 * �����������λΪ��ѡ�糧��
 */

public class Niandcgjhcx extends BasePage implements PageValidateListener {

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

	// ����չʾ
	public String getPrintTable() {
		return getSelectData();
	}

	private String getSelectData() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String strDate=getNianfValue().getValue() +"-01-01";
		String strSQL =
				"SELECT MK,\n" +
						"       GONGYS,\n" + 
						"       JIHKJ,\n" + 
						"       DAOHL,\n" + 
						"       REZ,\n" + 
						"       CHEBJ,\n" + 
						"       YUNF,\n" + 
//						"       ZAF,\n" + 
						"       QIT,\n" + 
						"       (CHEBJ + YUNF + ZAF + QIT) DAOCJ,\n" + 
						"       DECODE(REZ,\n" + 
						"              0,\n" + 
						"              0,\n" + 
						"              ROUND((CHEBJ + YUNF + ZAF + QIT) * 29.271 / REZ, 2)) DAOCBMJ,\n" +
						"              DECODE(REZ,0,0,ROUND((ROUND(CHEBJ / 1.17,2) + ROUND(YUNF * 0.93,2) + ZAFBHS + QITBHS) *29.271 / REZ,2)) DAOCBMJBHS," + 					
						"       RLZHBMDJ\n" +
						"  FROM (" +
				"SELECT DECODE(GROUPING(DC.MINGC), 1, '��˾�ϼ�', DC.MINGC) MK,\n" +
						"       DECODE(GROUPING(DC.MINGC),1,'',DECODE(GROUPING(GS.MINGC), 1, '�ϼ�', GS.MINGC)) GONGYS,\n" + 
						"       J.MINGC JIHKJ,\n" + 
						"       NVL(SUM(CG.DAOHL), 0) AS DAOHL,\n" + 
						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.REZ*CG.DAOHL)/SUM(CG.DAOHL)),2), 0) AS REZ,\n" +
						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.CHEBJ*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)CHEBJ,\n" + 
						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.YUNF*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)YUNF,\n" + 
						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.ZAF*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)ZAF,\n" +
						"       NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.ZAFBHS*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)ZAFBHS," + 
						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.QIT*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)QIT,\n" +
						"      NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.QITBHS*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)QITBHS," + 
//						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.DAOCJ*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)DAOCJ,\n" + 
//						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.DAOCBMJ*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)DAOCBMJ,\n" + 
//						"		NVL(round(DECODE (SUM(CG.DAOHL),0,0,SUM(CG.BIAOMDJBHS*CG.DAOHL)/SUM(CG.DAOHL)),2), 0)BIAOMDJBHS,\n" + 
						"		NVL(round(DECODE(SUM(ZB.BIAOMLHJ),0,0,SUM(ZB.BIAOMLHJ * ZB.RLZHBMDJ) / SUM(ZB.BIAOMLHJ)),2),0) RLZHBMDJ"+
						"  FROM (SELECT NC.DIANCXXB_ID  AS DIANCXXB_ID,\n" + 
						"               NC.GONGYSB_ID   AS GONGYSB_ID, \n" +
						"				NC.JIHKJB_ID AS JIHKJB_ID, \n" + 
						"               NC.JIH_SL       AS DAOHL,\n" + 
						"               NC.JIH_REZ      AS REZ,\n" + 
						"               NC.JIH_MEIJ     AS CHEBJ,\n" + 
						"               NC.JIH_YUNJ     AS YUNF,\n" + 
						"               NC.JIH_ZAF      AS ZAF,\n" +
						"               NC.JIH_ZAFBHS   AS ZAFBHS," + 
						"               NC.JIH_QIT      AS QIT,\n" + 
						"               NC.JIH_DAOCJ    AS DAOCJ,\n" + 
						"               NC.JIH_DAOCBMDJ AS DAOCBMJ,\n" +
						"              NC.JIH_QITBHS   AS QITBHS," + 
						"               ROUND(ROUND(DECODE(NC.JIH_REZ,0,0,(NC.JIH_MEIJBHS + NC.JIH_YUNJBHS +NC.JIH_QITBHS) * 29.2712 / NC.JIH_REZ),2),2) AS BIAOMDJBHS\n" + 
						"          FROM NIANDJH_CAIG NC\n" + 
						"         WHERE NC.RIQ = DATE'"+strDate+"') CG,\n" + 
						"       (SELECT ZB.DIANCXXB_ID, ZB.BIAOMLHJ, ZB.RLZHBMDJ\n" + 
						"          FROM NIANDJH_ZHIB ZB\n" + 
						"         WHERE ZB.RIQ = DATE'"+strDate+"') ZB, GONGYSB GS, JIHKJB J, DIANCXXB DC\n" + 
						" WHERE CG.GONGYSB_ID = GS.ID\n" +
						"	AND CG.JIHKJB_ID=J.ID \n" + 
						"   AND CG.DIANCXXB_ID(+) = DC.ID\n" + 
						"   AND ZB.DIANCXXB_ID(+) = DC.ID\n" + 
						"   AND DC.JIB = 3 and (DC.ID in ("+this.getTreeid()+") )\n" + 
						"  GROUP BY ROLLUP((DC.MINGC, DC.XUH), GS.MINGC,J.MINGC)\n" +
						"	 HAVING (GROUPING(J.MINGC)=0 OR GROUPING(GS.MINGC)=1)\n" + 
						" ORDER BY GROUPING(DC.MINGC) DESC,DC.XUH,GROUPING(GS.MINGC) DESC,J.MINGC,GS.MINGC)";


		ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[3][12];
		ArrHeader[0] = (new String[] { "��λ", "������λ","�ƻ��ھ�", "Ԥ��<br>������", "Ԥ��<br>������ֵ", "Ԥ��<br>����ú��<br>(��˰)", "Ԥ��<br>�����˼�<br>(��˰)", "Ԥ��<br>��������", "������", "Ԥ�Ƶ�����ú����", "Ԥ�Ƶ�����ú����", "Ԥ����¯�ۺϱ�ú����"});
		ArrHeader[1] = (new String[] { "��λ", "������λ","�ƻ��ھ�", "Ԥ��<br>������", "Ԥ��<br>������ֵ", "Ԥ��<br>����ú��<br>(��˰)", "Ԥ��<br>�����˼�<br>(��˰)", "Ԥ��<br>��������", "������", "��˰", "����˰", "Ԥ����¯�ۺϱ�ú����"});
		ArrHeader[2] = (new String[] { "��λ", "������λ","�ƻ��ھ�", "��", "�׽�/ǧ��", "Ԫ/�� ", "Ԫ/��","Ԫ/�� ", "Ԫ/��", "Ԫ/�� ", "Ԫ/��", "Ԫ/�� " });
		ArrWidth = (new int[] { 100, 200,70, 70, 70, 70, 70, 70, 70, 70, 70,70});
		rt.setTitle(intyear + "ú̿�ɹ��ƻ�����Ҫָ��Ԥ�����", ArrWidth);
		rt.body.setWidth(ArrWidth);

		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);

//		�����кϲ���Ԫ��
//			rt.body.mergeRow(1);
//			rt.body.merge(1, 1, 3, 1);
//			rt.body.merge(1, 2, 3, 2);
//			rt.body.merge(1, 3, 2, 3);
//			rt.body.merge(1, 4, 2, 4);
//			rt.body.merge(1, 5, 2, 5);
//			rt.body.merge(1, 6, 2, 6);
//			rt.body.merge(1, 7, 2, 7);
//			rt.body.merge(1, 8, 2, 8);
//			rt.body.merge(1, 9, 2, 9);
//			rt.body.merge(1, 12, 2, 12);
			
		rt.body.mergeFixedRowCol();
		rt.body.mergeCol(12);
 		rt.getPages();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(12, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 1, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols(), 1, "(��Page/Pagesҳ)",
				Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}

	// ���
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
		for (i = 2011; i <= DateUtil.getYear(new Date()) + 1; i++) {
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

	// ***************************�����ʼ����***************************//
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

	// ҳ���ж�����
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
//	��ʼ����ѡ�糧���е�Ĭ��ֵ
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString()
				.equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			getNianfModels();
//			getIDiancmcModels();
			getDiancmcModel();
			this.setTreeid(null);
			setToolbar(null);
				 initDiancTree();
			
		}
		getToolBars();
		getSelectData();
		
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
//		Visit visit = (Visit) getPage().getVisit();
//		 ��λ
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("��ϵ糧");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));
		
		
		// ���
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
//		ˢ��
		ComboBox comb1 = new ComboBox();
		ToolbarButton tb = new ToolbarButton(null, "ˢ��","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		setToolbar(tb1);
		comb1.setId("NIANF");// ���Զ�ˢ�°�
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

	// �糧��
	// �糧����
//	�糧����-----------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {

		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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
		return getTree().getWindowTreeScript()+getOtherScript("diancTree");
	}
//	���ӵ糧��ѡ���ļ���
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
	
}