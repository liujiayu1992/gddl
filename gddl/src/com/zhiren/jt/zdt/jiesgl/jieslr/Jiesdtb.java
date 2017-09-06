package com.zhiren.jt.zdt.jiesgl.jieslr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jiesdtb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}
	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
		
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			((Visit) getPage().getVisit()).setPagePreferences("Jiesdtb");
			cycle.activate("Jieslr");
		}
//		if (_DeleteChick) {
//			_DeleteChick = false;
//		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			setGongysValue(null);
			setGongysModel(null);
			getSelectData();
		}
	
	}
	private void Save() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1()
				.getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");

		while (drsl.next()) {
			sql_delete.append("delete from ").append("jiesb").append(
					" where id =").append(drsl.getLong("id")).append(";\n");
			sql_delete.append("delete from ").append("jiesyfb").append(
			" where diancjsmkb_id =").append(drsl.getLong("id")).append(";\n");
			sql_delete.append("delete from ").append("jieszbsjb").append(
			" where jiesdid =").append(drsl.getLong("id")).append(";\n");
		}
		sql_delete.append("end;");
		con.getUpdate(sql_delete.toString());
		con.Close();
	}

//	private void Delete(){
//	    JDBCcon con=new JDBCcon();
//	    Visit visit = (Visit) getPage().getVisit();
//	    try{
//	    	String sql="";
//	    	long mid = Long.parseLong(visit.getString1().substring(2));
//	    	boolean flag=false;
//	    	if(getEditValues()!= null && !getEditValues().isEmpty()){
//	    		//������ɾ���˷�
//	    			sql="select id from jiesb d where d.id="+mid+"";
//	    			
//	    			ResultSet rs=con.getResultSet(sql);
//	    			if(rs.next()){
//	    				
//	    					sql="delete from jiesb where id="+rs.getLong("id");
//	    					con.getDelete(sql);
//	    					
//	    					sql="delete from jiesyfb  where DIANCJSMKB_ID="+rs.getLong("id");
//	    					con.getDelete(sql);
//	    					
//	    					sql="delete from jieszbsjb where jiesdid="+rs.getLong("id");
//	    					con.getDelete(sql);
//	    					
//	    					flag=true;
//	    			
//	    			}
////	    			����ɾ���˷�
//				setMsg("���㵥��ɾ����");
//	    	}
//	     }catch(Exception e){
//	    	
//	    	e.printStackTrace();
//	    }finally{
//	    	
//	    	con.Close();
//	    }
//		getEditValues().clear();
//	}
	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = " and jib=3 ";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and dc.fuid = "+ getTreeid() + "";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + "";
		}
		
		String gys="";
		if(getGongysValue().getValue().equals("��ѡ��")){
			gys=" ";
		}else{
			gys=" and gy.id="+getGongysValue().getId();
		}
		String context = MainGlobal.getHomeContext(this);
		String sql=
			"select j.id as id,dc.mingc as diancxxb_id,j.bianm,gy.mingc as gongysb_id,j.jiesrq,ysfs.mingc as yunsfsb_id,j.shoukdw,j.ches,j.jiessl,j.hansmk,j.hansdj,jyf.buhsyf,\n" +
			"           j.ranlbmjbr,h.id as hetb_id,decode(1,1,'�鿴') as chak,decode(1,1,'�޸�') as xiug\n" + 
			"      from jiesb j,jiesyfb jyf,gongysb gy,diancxxb dc ,yunsfsb ysfs,hetb h\n" + 
			"      where j.hetb_id=h.id(+) and j.gongysb_id=gy.id "+gys+"and jyf.diancjsmkb_id= j.id  and  j.jiesrq>=to_date('"+getRiqi()+"','yyyy-mm-dd') and j.jiesrq<=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
//			"            and j.diancxxb_id=dc.id "+str+" and j.yunsfsb_id=ysfs.id ";
			"            and j.diancxxb_id=dc.id and j.yunsfsb_id=ysfs.id "+str+"";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jiesb");
		egu.setWidth("bodyWidth");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("bianm").setHeader("���㵥����");
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("������λ");
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("jiesrq").setHeader("��������");	
		egu.getColumn("jiesrq").setEditor(null);
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("shoukdw").setHeader("�տλ");
		egu.getColumn("shoukdw").setEditor(null);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("jiessl").setHeader("ʵ�ʽ�����");
		egu.getColumn("jiessl").setEditor(null);
		egu.getColumn("hansmk").setHeader("��˰ú��");
		egu.getColumn("hansmk").setEditor(null);
		egu.getColumn("hansdj").setHeader("��˰����");
		egu.getColumn("hansdj").setEditor(null);
		egu.getColumn("buhsyf").setHeader("�˷�");
		egu.getColumn("buhsyf").setEditor(null);
		egu.getColumn("ranlbmjbr").setHeader("������");
		egu.getColumn("ranlbmjbr").setEditor(null);
		egu.getColumn("hetb_id").setHidden(true);
		
		egu.getColumn("chak").setHeader("�鿴");
		egu.getColumn("chak").setEditor(null);
		String str1=
       		" var url1 = 'http://'+document.location.host+document.location.pathname;"+
            " var end1 = url1.indexOf(';');"+
			" url1 = url1.substring(0,end1);"+
       	    " url1 = url1 + '?service=page/' + 'Jiesdcx&bianm='+'mk'+record.data['ID'];";
		egu.getColumn("chak").setRenderer(
				"function(value,p,record){" +str1+
				"return \"<a href=# onclick=window.open('\"+url1+\"','_blank')>�鿴</a>\"}"
		);
//		egu.getColumn("chak").setRenderer(
//				"function(value,p,record){return String.format('<a href="+context+"/app?service=page/{1}&bianm={2}>{0}</a>',value,'Jiesdcx','mk'+record.data['ID']);}");
		egu.getColumn("xiug").setHeader("�޸�");
		egu.getColumn("xiug").setEditor(null);
		egu.getColumn("xiug").setRenderer(
				"function(value,p,record){if(record.data['HETB_ID']==''){ return '����Ч��ͬ��' }else{ return String.format('<a href="+context+"/app?service=page/{1}&bianm={2}>{0}</a>',value,'Jieslrdxg',record.data['ID']);}}");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("bianm").setWidth(70);
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("jiesrq").setWidth(80);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("shoukdw").setWidth(80);
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("jiessl").setWidth(80);
		egu.getColumn("hansmk").setWidth(70);
		egu.getColumn("hansdj").setWidth(60);
		egu.getColumn("buhsyf").setWidth(60);
		egu.getColumn("ranlbmjbr").setWidth(70);
		egu.getColumn("chak").setWidth(50);
		egu.getColumn("xiug").setWidth(100);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.addPaging(25);		
		
		// �糧��
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		egu.addTbarText("��ʼ����:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��ֹ����:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		

		// ������λ
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");


	//���
		egu.addTbarText("-");
		egu.addToolbarItem("{"+new GridButton("�������㵥","function(){ document.getElementById('InsertButton').click();}").getScript()+"}");
		egu.addTbarText("-");
//		//���
//		GridButton kf=new GridButton("���","function(){ document.Form0.InsertButton.click(); }");
//		kf.setIcon(SysConstant.Btn_Icon_Insert);
//		egu.addTbarBtn(kf);

//		ˢ��
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append("document.getElementById('RetrunsButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

//		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Gongys.getRawValue()=='��ѡ��'){alert('��ѡ��Ӧ��'); return;}");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		
		con.Close();
	}
	
	

//������λ

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {

		String  sql ="select distinct gy.id,gy.mingc as mingc from jiesb j,gongysb gy \n" +
		"where j.gongysb_id=gy.id  and j.jiesrq>=to_date('"+getRiqi()+"','yyyy-mm-dd') and j.jiesrq<=to_date('"+getRiq2()+"','yyyy-mm-dd')";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
//----------------------
	//-----------------
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.isFencb();

			setGongysValue(null);
			setGongysModel(null);
//			getGongysModels();
			setTreeid(null);
			
		}
		getSelectData();	
			
	}
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}
	
	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid==null||treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
}