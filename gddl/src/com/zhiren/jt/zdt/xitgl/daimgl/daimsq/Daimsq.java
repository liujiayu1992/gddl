package com.zhiren.jt.zdt.xitgl.daimgl.daimsq;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/* 
* ʱ�䣺2009-06-26
* ���ߣ� sy
* �޸����ݣ��޸ġ�ú��λ���ơ�����Ϊ��ú�����ȫ�ơ���
* �޸ġ�ú���ơ�����Ϊ��ú��λ��ơ���
* �޸ġ�ú��ȫ�ơ�����Ϊ��ú��λȫ�ơ���
* ͳһ���ƣ���Ȼ����������
*			
*/ 
public class Daimsq extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
		setMsg("����ɹ���");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		//-----------------------------------
		String str = "";
		long zhuangtID=this.getZhuangtValue().getId();
	
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		
		}

		StringBuffer sb=new StringBuffer();
		sb.append( "select sq.id,dc.mingc as diancxxb_id,sq.shenqrq,sq.gonghdwqc,\n");
		if(zhuangtID==1){
			sb.append( "sq.gonghdwbm,\n");
		}
		sb.append(" sq.meikjc, sq.meikqc,\n");
		if(zhuangtID==1){
			sb.append( "sq.meikbm,\n");
		}
		sb.append("      cz.mingc as faz_id,s.quanc as shengfb_id,cs.mingc as chengsb_id,\n");
		sb.append("       sq.shenqr,decode(sq.kuangb,1,'ͳ���',2,'�ط���','') as kuangb,sq.beiz,sq.zhuangt\n");
		sb.append(" from gongysmksqb sq,diancxxb dc ,chezxxb cz,shengfb s,chengsb cs\n");
		sb.append("where sq.diancxxb_id=dc.id\n");
		sb.append("and   sq.faz_id=cz.id(+)\n");
		sb.append("and   sq.shengfb_id=s.id(+)\n" );
		sb.append("and   sq.chengsb_id=cs.id(+) \n ");
		sb.append("and  sq.zhuangt="+zhuangtID+"  "+str+" order by sq.shenqrq desc");
		
 ;

		
		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(sb.toString());
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("gongysmksqb");
   	egu.setWidth("bodyWidth");
	egu.getColumn("id").setHeader("id");
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	egu.getColumn("diancxxb_id").setCenterHeader("�糧����");
	egu.getColumn("gonghdwqc").setCenterHeader("ú�����ȫ��");
	egu.getColumn("meikjc").editor.setAllowBlank(false);
	egu.getColumn("meikjc").setCenterHeader("ú��λ���");
	egu.getColumn("meikqc").setCenterHeader("ú��λȫ��");
	egu.getColumn("faz_id").setCenterHeader("��վ");
	egu.getColumn("shengfb_id").setCenterHeader("ʡ��");
	egu.getColumn("chengsb_id").setCenterHeader("����");
	egu.getColumn("kuangb").setCenterHeader("���");
	egu.getColumn("shenqr").setCenterHeader("������");
	egu.getColumn("shenqr").setHidden(true);
	egu.getColumn("shenqrq").setCenterHeader("��������");
	egu.getColumn("shenqrq").setHidden(true);
	egu.getColumn("beiz").setCenterHeader("��ע");
	egu.getColumn("zhuangt").setCenterHeader("����״̬");
	egu.getColumn("zhuangt").setEditor(null);
	egu.getColumn("zhuangt").setHidden(true);
	
	
	if(zhuangtID==1){//��״̬��������,���ݲ������޸�
		egu.getColumn("gonghdwbm").setCenterHeader("������λ����");
		egu.getColumn("gonghdwbm").setEditor(null);
		egu.getColumn("meikbm").setCenterHeader("ú�����");
		egu.getColumn("meikbm").setEditor(null);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gonghdwqc").setEditor(null);
		egu.getColumn("meikjc").setEditor(null);
		egu.getColumn("meikqc").setEditor(null);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("shengfb_id").setEditor(null);
		egu.getColumn("chengsb_id").setEditor(null);
		egu.getColumn("kuangb").setEditor(null);
		egu.getColumn("shenqrq").setHidden(false);
		//egu.getColumn("beiz").setEditor(null);
	}
	
	//�趨�г�ʼ���
	
	egu.getColumn("diancxxb_id").setWidth(100);
	egu.getColumn("gonghdwqc").setWidth(120);
	egu.getColumn("meikjc").setWidth(100);
	egu.getColumn("meikqc").setWidth(150);
	egu.getColumn("faz_id").setWidth(80);
	egu.getColumn("shengfb_id").setWidth(80);
	egu.getColumn("chengsb_id").setWidth(80);
	egu.getColumn("kuangb").setWidth(80);
	egu.getColumn("beiz").setWidth(200);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(30);//���÷�ҳ
	
	
	
	//*****************************************����Ĭ��ֵ****************************
	//	�糧������
	int treejib2 = this.getDiancTreeJib();

	if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
		ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
		String mingc="";
		if(r.next()){
			mingc=r.getString("mingc");
		}
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		
	}		
	
	
	//�������ڵ�Ĭ��ֵ,
	
	
	egu.getColumn("shenqr").setDefaultValue(visit.getRenymc());
	egu.getColumn("shenqrq").setDefaultValue(String.valueOf(DateUtil.FormatDate(new Date())));
	egu.getColumn("zhuangt").setDefaultValue("0");
	
	//*************************������*****************************************88
	//����ʡ�ݵ�������
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengfb_id").setEditor(cb_shengf);
	cb_shengf.setEditable(true);
	//egu.getColumn("shengfb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String ShengfbSql="select id,quanc from shengfb order by quanc";
	egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel(ShengfbSql));
	//���ó���������
	ComboBox cb_chengs=new ComboBox();
	egu.getColumn("chengsb_id").setEditor(cb_chengs);
	cb_chengs.setEditable(true);
	//egu.getColumn("chengsb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String ChengsbSql="select j.id,j.mingc from chengsb j order by id  ";
	egu.getColumn("chengsb_id").setComboEditor(egu.gridId, new IDropDownModel(ChengsbSql));
	//���÷�վ������
	ComboBox cb_faz=new ComboBox();
	egu.getColumn("faz_id").setEditor(cb_faz);
	cb_faz.setEditable(true);
	egu.getColumn("faz_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String fazSql="select j.id,j.mingc from chezxxb j order by id  ";
	egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
	
//	���ÿ��������
	ComboBox cb_kuangb=new ComboBox();
	egu.getColumn("kuangb").setEditor(cb_kuangb);
	cb_kuangb.setEditable(true);
	//egu.getColumn("kuangb").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String kuangbSql="select 1 as id,'ͳ���' as mingc from dual union select 2 as id,'�ط���' as mingc from dual ";
	egu.getColumn("kuangb").setComboEditor(egu.gridId, new IDropDownModel(kuangbSql));
	

	
	//********************������************************************************
		
		//������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");// ���÷ָ���
		//����״̬
		egu.addTbarText("����״̬:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("ZHUANGT");
		comb1.setId("ZHUANGT");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setListeners("select:function(){document.Form0.submit();}");
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		//�趨�������������Զ�ˢ��
	
		egu.addTbarText("-");// ���÷ָ���
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		if(zhuangtID==0){//��״̬��δ����ʱ,��ʾ��,ɾ,���水ť
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
	
		
		

		
		
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
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
			
			this.setTreeid(null);
			this.setZhuangtValue(null);
			this.getZhuangtModels();
			setTbmsg(null);
		}
		
			getSelectData();
		
		
	}

	

//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
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
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	
//	�󱨱�����
	public boolean _Zhuangtchange = false;
	private IDropDownBean _ZhuangtValue;

	public IDropDownBean getZhuangtValue() {
		if(_ZhuangtValue==null){
			_ZhuangtValue=(IDropDownBean)getZhuangtModels().getOption(0);
		}
		return _ZhuangtValue;
	}

	public void setZhuangtValue(IDropDownBean Value) {
		long id = -2;
		if (_ZhuangtValue != null) {
			id = _ZhuangtValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Zhuangtchange = true;
			} else {
				_Zhuangtchange = false;
			}
		}
		_ZhuangtValue = Value;
	}

	private IPropertySelectionModel _ZhuangtModel;

	public void setZhuangtModel(IPropertySelectionModel value) {
		_ZhuangtModel = value;
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (_ZhuangtModel == null) {
			getZhuangtModels();
		}
		return _ZhuangtModel;
	}

	public IPropertySelectionModel getZhuangtModels() {
		JDBCcon con = new JDBCcon();
		try{
		List ztList = new ArrayList();
		ztList.add(new IDropDownBean(0,"δ����"));
		ztList.add(new IDropDownBean(1,"������"));
	
		_ZhuangtModel = new IDropDownModel(ztList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _ZhuangtModel;
	}
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
}
