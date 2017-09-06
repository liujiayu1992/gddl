
package com.zhiren.gs.bjdt.diaoygl;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shouhcrbfksmdz extends BasePage {
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
//		return getQibb();
//		if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getHetdxqkquery();
//		} else {
//		return "�޴˱���";
//		}
	}
	// ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getHetdxqkquery() {
		JDBCcon con = new JDBCcon();

//		String Start_riq=getBeginriqDate();
		String End_riq=getEndriqDate();

		String strdiancid = "";
		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if (jib==3){   //3 �ǵ糧
			strdiancid = "and dc.id="+this.getTreeid();
		}else if(jib==2){  // 2 �ǹ�˾
			if(getDiancmcValue().getId()==-1){
				strdiancid = "dc.fuid="+this.getTreeid();
			}else{
				strdiancid = "";
			}
		}

		String danw="";
		String table="";
		String where="";
		String groupby = "";
		String ordeby ="";

		JDBCcon cn = new JDBCcon();
		


		String sbsql=
			" select  decode(grouping(dc.lbmingc)+grouping(dc.mingc),2,'��������ȼ�����޹�˾',1,'��'||max(dc.lbmingc)||'С��',dc.mingc) as dianc,\n" +
			"        sum(nvl(r.dangrgm,0)) as dangrgm,\n" + 
			"        sum(nvl(f1.lxzd,0)) as zd,\n" + 
			"        sum(nvl(f2.lxbc,0)) as bc,\n" + 
			"        sum(nvl(f1.lxzd,0)+nvl(f2.lxbc,0)) as hj,\n" + 
			"        sum(nvl(r.dangrgm,0))-sum((nvl(f1.lxzd,0)+nvl(f2.lxbc,0))) as db\n" + 
			"\n" + 
			" from (select diancxxb_id,nvl(rb.dangrgm,0) as dangrgm  from shouhcrbb rb\n" + 
			"        where rb.riq=to_date('"+this.getEndriqDate()+"','yyyy-mm-dd')) r,\n" + 
			"\n" + 
			"      (select vw.diancxxb_id,sum(fk.duns) as lxzd from fenkshcrb fk,vwdcgysmykjgl vw \n" + 
			"        where fk.gongysb_id=vw.id and fk.riq=to_date('"+this.getEndriqDate()+"','yyyy-mm-dd') and vw.gongyskjb_id=1 \n" + 
			"       group by (vw.diancxxb_id) ) f1,\n" + 
			"\n" + 
			"         (select vw.diancxxb_id,sum(fk.duns) as lxbc  from fenkshcrb fk,vwdcgysmykjgl vw \n" + 
			"        where fk.gongysb_id=vw.id and fk.riq=to_date('"+this.getEndriqDate()+"','yyyy-mm-dd') and vw.gongyskjb_id=2 \n" + 
			"        group by (vw.diancxxb_id) ) f2,\n" + 
			"\n" + 
			"           (select d.id, px.xuh, d.mingc,d.fuid ,lb.mingc lbmingc,lb.xuh lbxuh \n" + 
			"          from diancxxb d, dianckjpxb px,dianclbb lb\n" + 
			"          where d.id = px.diancxxb_id\n" + 
			"           and d.jib = 3\n" + 
			"           and px.kouj = '�±�'\n and d.dianclbb_id=lb.id(+)\n" + 
			"           order by px.xuh) dc,\n" + 
			"\n" + 
			"         dianckjpxb px\n" + 
			"where dc.id=f1.diancxxb_id(+)\n" + 
			"      and dc.id=f2.diancxxb_id(+)\n" + 
			"      and dc.id= r.diancxxb_id(+)\n" + 
			"      and dc.id=px.diancxxb_id "+strdiancid+"\n" +
			"       and px.kouj='�±�'\n ";
			if(!this.getTreeid().equals("1")){
			sbsql=sbsql+"group by (dc.lbmingc,dc.mingc)\n"+
			"order by  grouping(dc.lbmingc) desc , max(dc.lbxuh),grouping(dc.mingc) desc, max(px.xuh)";
			}else{
		     sbsql=sbsql+"group by rollup(dc.lbmingc,dc.mingc)\n" + 
			"order by  grouping(dc.lbmingc) desc , max(dc.lbxuh),grouping(dc.mingc) desc, max(px.xuh)";
			}

		//System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		String ArrHeader[][] =new String[3][6];
		ArrHeader[0]=new String[] {"��λ����","�ձ���ú","��ͬ�������","��ͬ�������","��ͬ�������","�Ա�"};
		ArrHeader[1]=new String[] {"��λ����","�ձ���ú","�ص�","����","�ϼ�","�Ա�"};
		ArrHeader[2]=new String[] {"�糧","��"," �� ","��"," �� ","��"};

		int ArrWidth[]=new int[] {140,110,110,110,110,110};
//		String arrFormat[]=new String[]{"","","0","0","0.00","0","0","0.00","0.00","0"};

		Table bt=new Table(rs,3,0,1);
		rt.setBody(bt);
		//
		rt.body.setUseDefaultCss(true);
		rt.body.ShowZero=false;
//		rt.body.setColFormat(arrFormat);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();		//�ϲ���
		rt.body.mergeFixedCols();		//�Ͳ���
		rt.setTitle("�ձ���ֿ���ú��������Աȱ�", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "��λ:���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���",Table.ALIGN_LEFT);
//
		rt.setDefaultTitle(5,2,"���ڣ�"+FormatDate(DateUtil.getDate(End_riq)),Table.ALIGN_RIGHT);
		rt.setDefaultTitle(6,3,FormatDate(DateUtil.getDate(End_riq)),Table.ALIGN_CENTER);
		rt.body.setPageRows(30);  //ÿҳ������
		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(1, 2, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 2, "��ˣ�", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(10, 2, "�Ʊ�", Table.ALIGN_LEFT);  //�Ʊ�����дӵ�10�п�ʼռ2�С�
		// ����ҳ��
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

//	public String getPageHome() {
//		if (((Visit) getPage().getVisit()).getboolean1()) {
//			return "window.location = '" + MainGlobal.getHomeContext(this)
//			+ "';";
//		} else {
//			return "";
//		}
//	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		
		tb1.addText(new ToolbarText("��λ:"));		
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ѯ����:"));
		DateField df = new DateField();

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		

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
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setString4(null);
			visit.setString5(null);

		}
		getToolbars();

		blnIsBegin = true;

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
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
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
//	�ֹ�˾������
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
			.setDropDownBean4((IDropDownBean) getFengsModel()
					.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"��������"));
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

	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}


}
