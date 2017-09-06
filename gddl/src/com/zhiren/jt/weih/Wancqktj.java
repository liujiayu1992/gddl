package com.zhiren.jt.weih;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ����
 * ʱ�䣺2011-05-17
 * �������޸�SQL��ѯ��ʽ���ڱ�������FULL JOIN�����ظ������µı������
 */

public class Wancqktj extends BasePage {
	
	public boolean getRaw() {
		return true;
	}
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


	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;

		//return getShouhc()+"<div>&nbsp;</div>"+getShouhcDetail();
		return getShouhc();
	}
	
	private String getShouhc() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));

		StringBuffer SQL = new StringBuffer();
		String beginRiq=this.getBeginriqDate();
		SQL.append("SELECT DIANC.MINGC, \n");
		SQL.append("  DECODE(KC.DRKC, '', '<b>������</b>', KC.DRKC) DRKC, \n");
		SQL.append("  DECODE(KC.DRKDKC, '', '<b>������</b>', KC.DRKDKC) DRKDKC, \n");
		SQL.append("  DECODE(LM.MEIJ, '', '<b>δ���</b>', DECODE(LM.YUNJ, '', '<b>δ���</b>', DECODE(LM.REZ, '', '<b>δ���</b>', 0, '<b>δ���</b>', '<b>����</b>'))) RIGJ, \n");
		SQL.append("  DECODE(LM.LAIMSL, '', '<b>������</b>', LM.LAIMSL) LAIMSL \n");
		SQL.append("FROM \n");
		SQL.append("  (SELECT DR.DIANCXXB_ID DIANCXXB_ID, \n");
		SQL.append("    DR.KUC DRKC, \n");
		SQL.append("    DR.KEDKC DRKDKC \n");
		SQL.append("  FROM \n");
		SQL.append("    (SELECT HC.DIANCXXB_ID AS DIANCXXB_ID, \n");
		SQL.append("      SUM(DANGRGM) GM, \n");
		SQL.append("      SUM(KUC)   AS KUC, \n");
		SQL.append("      SUM(KEDKC) AS KEDKC \n");
		SQL.append("    FROM SHOUHCRBB HC \n");
		SQL.append("    WHERE RIQ = TO_DATE('"+beginRiq+"', 'yyyy-mm-dd') \n");
		SQL.append("    GROUP BY HC.DIANCXXB_ID \n");
		SQL.append("    ) DR \n");
		SQL.append("  ) KC, \n");
		SQL.append("  (SELECT SHC.DIANCXXB_ID DIANCXXB_ID, \n");
		SQL.append("    SHC.MEIJ, \n");
		SQL.append("    SHC.YUNJ, \n");
		SQL.append("    SHC.REZ, \n");
		SQL.append("    SHC.LAIMSL LAIMSL \n");
		SQL.append("  FROM \n");
		SQL.append("    (SELECT DIANCXXB_ID, \n");
		SQL.append("      DECODE(SUM(LAIMSL), 0, 0, SUM(MEIJ * LAIMSL) / SUM(LAIMSL)) MEIJ, \n");
		SQL.append("      DECODE(SUM(LAIMSL), 0, 0, SUM(YUNJ * LAIMSL) / SUM(LAIMSL)) YUNJ, \n");
		SQL.append("      DECODE(SUM(LAIMSL), 0, 0, SUM(REZ  * LAIMSL) / SUM(LAIMSL)) REZ, \n");
		SQL.append("      SUM(NVL(LAIMSL, 0)) LAIMSL \n");
		SQL.append("    FROM SHOUHCFKB \n");
		SQL.append("    WHERE RIQ = TO_DATE('"+beginRiq+"', 'yyyy-mm-dd') \n");
		SQL.append("    GROUP BY DIANCXXB_ID \n");
		SQL.append("    ) SHC \n");
		SQL.append("  ) LM, \n");
		SQL.append("  (SELECT DISTINCT DC.ID, \n");
		SQL.append("    DC.XUH, \n");
		SQL.append("    DC.MINGC \n");
		SQL.append("  FROM VWDIANC DC \n");
		SQL.append("  WHERE DC.ID IN ("+getTreeid()+") \n");
		SQL.append("  ) DIANC \n");
		SQL.append("WHERE DIANC.ID = KC.DIANCXXB_ID(+) \n");
		SQL.append("AND DIANC.ID   = LM.DIANCXXB_ID(+) \n");
		SQL.append("ORDER BY DIANC.XUH");

		ResultSetList rs = con.getResultSetList(SQL.toString());
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][4];
		ArrHeader[0]=new String[] {"��λ","���տ��������","���տ��������","�ֿ���ú���","�ֿ���ú���"};
		ArrHeader[1]=new String[] {"��λ","������","���ÿ��","�չ���","������ú��"};
		int ArrWidth[]=new int[] {100,80,80,80,80};
		//rs.beforefirst();
		rt.setBody(new Table(rs, 2, 0, 0));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
		rt.setTitle(getDiancmc(String.valueOf(visit.getDiancxxb_id()))+"ȼ���ձ�������ͳ��",ArrWidth);
		rt.setDefaultTitle(1, 4, riq, Table.ALIGN_CENTER);
		
		rt.body.mergeFixedRowCol();
		//rt.body.setPageRows(21);
		// ����ҳ��
		rt.body.ShowZero=true;
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
			+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
//	ʹ��Visit�е�String4��Ϊ��ʼ����///
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.setTime(new Date());
			stra.add(Calendar.DATE, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("����:"));
//		�������ڿ�
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);
//		�����糧��
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
//		ˢ�°�ť
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			//��ʼ���糧��������
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setExtTree1(null);
			//��ʼ�����ڿ�
			visit.setString4(null);
			setTreeid(null);
		}
		getToolbars();
		blnIsBegin = true;
	}

//	��ʼ����糧���������
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
//	�糧����
//	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

//	private boolean _DiancmcChange=false;
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
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}

//	�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}
//	 �õ���½��Ա�����糧��ֹ�˾������
	public String getDiancmc(String diancId) {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		//long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;

	}
	public String getDiancmc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "��ϵ糧";
		}else{
			return getDiancmc(str[0]);
		}
	}
//	�������
	
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
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