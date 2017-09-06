package com.zhiren.shanxdted.meicrzc;

import java.sql.ResultSet;
import java.sql.SQLException;
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
* ���ߣ���ʤ��
* ʱ�䣺2012-11-28
* ���÷�Χ��ֻ���ڴ�ͬ�糧
* ������	������ֵ���ѯ����
*/
/*
* ���ߣ����
* ʱ�䣺2012-12-01
* ���÷�Χ��ֻ���ڴ�ͬ�糧
* ������	������������
*/
public class Meicrzccx extends BasePage {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
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
		setMsg(null);
		return getMeicrz();
	}
	
	private String getMeicrz() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
		String SQL="SELECT RIQ,MINGC,RUCML,RUC_QNET_AR, ROUND(RUC_QNET_AR/4.1816*1000)RUC_DK,\n" +
					"RUC_MT,RUC_STD,RULML,RUL_QNET_AR,ROUND(RUL_QNET_AR/4.1816*1000)RUL_DK,RUL_MT,RUL_ST,\n" + 
					"(RUC_QNET_AR-RUL_QNET_AR)RZC,ROUND(RUC_QNET_AR/4.1816*1000)-ROUND(RUL_QNET_AR/4.1816*1000)RZC_DK\n"+
						"from (SELECT DECODE(GROUPING(MC.RIQ) + GROUPING(DC.MINGC),\n" + 
						"        2,\n" + 
						"        '�ܼ�',\n" + 
						"        TO_CHAR(MC.RIQ, 'yyyy-mm-dd')) RIQ,\n" + 
						" DECODE(GROUPING(MC.RIQ) + GROUPING(DC.MINGC), 1, 'С��', DC.MINGC) MINGC,\n" + 
						" SUM(MC.RUCML) AS RUCML,\n" + 
						" ROUND_NEW(DECODE(SUM(RUCML), 0, 0, SUM(MC.RUCRZ * RUCML) / SUM(RUCML)), 2) RUC_QNET_AR,\n" + 
						" ROUND_NEW(DECODE(SUM(RUCML), 0, 0, SUM(MC.RUCMT * RUCML) / SUM(RUCML)), 2) RUC_MT,\n" + 
						" ROUND_NEW(DECODE(SUM(RUCML), 0, 0, SUM(MC.RUCST * RUCML) / SUM(RUCML)), 2) RUC_STD,\n" + 
						" SUM(MC.RULML) AS RULML,\n" + 
						" ROUND_NEW(DECODE(SUM(RULML), 0, 0, SUM(MC.RULRZ * RULML) / SUM(RULML)), 2) RUL_QNET_AR,\n" + 
						" ROUND_NEW(DECODE(SUM(RULML), 0, 0, SUM(MC.RULMT * RULML) / SUM(RULML)), 2) RUL_MT,\n" + 
						" ROUND_NEW(DECODE(SUM(RULML), 0, 0, SUM(MC.RULST * RULML) / SUM(RULML)), 2) RUL_ST\n" + 
						"  FROM MEICRZC MC, DIANCXXB DC\n" + 
						" WHERE MC.DIANCXXB_ID = DC.ID\n" +
						"AND DC.ID  IN ("+getTreeid()+")\n" + 
						"   AND RIQ >= TO_DATE('"+beginRiq+"', 'yyyy-mm-dd')\n" + 
						"   AND RIQ <= TO_DATE('"+endRiq+"', 'yyyy-mm-dd')\n" + 
						" GROUP BY ROLLUP(MC.RIQ, DC.MINGC)\n" + 
						" ORDER BY GROUPING(MC.RIQ) DESC ,MC.RIQ DESC ,GROUPING(DC.MINGC) DESC ,DC.MINGC)SR";
		ResultSetList rs = con.getResultSetList(SQL);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][14];
		ArrHeader[0]=new String[] {"����","��λ����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/Kg)","�볧��ֵ<br>(Kcal/Kg)","�볧ˮ��<br>(%)","�볧���<br>(%)",
									"��¯ú��<br>(��)","��¯��ֵ<br>(MJ/Kg)","��¯��ֵ<br>(Kcal/Kg)","��¯ˮ��<br>(%)","��¯���<br>(%)",
									"�볧��¯��ֵ��<br>(MJ/Kg)","�볧��¯��ֵ��<br>(Kcal/Kg)"};
		int ArrWidth[]=new int[] {100,100,90,60,60,60,60,90,60,60,60,60,60,60};

		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		 
		rt.setTitle("ú����ֵ���ѯ",ArrWidth);
		String baot="";
		if(this.getTreeid().equals("300")){
			baot="�����ͬ����(1-10)";
		}else if(this.getTreeid().equals("301")){
			baot="�����ͬһ��(1-6)";
		}else if(this.getTreeid().equals("302")){
			baot="�����ͬ����(7-8)";
		}else if(this.getTreeid().equals("303")){
			baot="�����ͬ����(9-10)";
		}else if(this.getTreeid().equals("304")){
			baot="�����ͬ���繫˾(ϴú��)";
		}else if(this.getTreeid().equals("302,303")){
			baot="�����ͬ���繫˾(7-10)";
		}else if(this.getTreeid().equals("300,301,302,303")||this.getTreeid().equals("301,302,303")){
			baot="�����ͬ����(1-10)";
		}else if(this.getTreeid().equals("300,301,302,303,304")){
			baot="�����ͬ����(1-10)";
		}
		rt.setDefaultTitle(1, 3, "��λ��" + baot, Table.ALIGN_LEFT);
		
//		rt.setDefaultTitle(1, 2, "���λ:"+getDiancmc(), Table.ALIGN_LEFT);
//		rt.setDefaultTitle(3, 3, endRiq, Table.ALIGN_CENTER);
//		rt.setDefaultTitle(6, 3, "��λ:�֡�Ԫ/�֡�MJ/Kg", Table.ALIGN_RIGHT);
//		rt.body.ShowZero=true;
		
//		ȫ����������
		for(int i=0;i<=ArrHeader[0].length;i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
	
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
//		rt.createFooter(1, ArrWidth);
//		rt.setDefautlFooter(1, 2, "���ܣ�", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(3, 3, "��ˣ�", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(6, 2, "�Ʊ�", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
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

	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
		
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString6()==null||((Visit) getPage().getVisit()).getString6()==""){
			((Visit) getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString6();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString6(value);
		
	}

	private void getToolbars(){
			Toolbar tb1 = new Toolbar("tbdiv");

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
			
			tb1.addText(new ToolbarText("����:"));
			DateField df = new DateField();

			df.setValue(this.getBeginriqDate());
			df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
			df.setWidth(80);
			tb1.addField(df);
			tb1.addText(new ToolbarText("��"));
			DateField df1 = new DateField();

			df1.setValue(this.getEndriqDate());
			df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
			df1.setWidth(80);
			tb1.addField(df1);
			tb1.addText(new ToolbarText("-"));

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
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setExtTree1(null);
			visit.setString5(null);
			visit.setString6(null);
			setTreeid(null);
			initDiancTree();
		}
		getToolbars();
		blnIsBegin = true;
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

//	 �õ���½��Ա�����糧��ֹ�˾������
	public String getDiancmc(String diancId) {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="+ diancId;
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
