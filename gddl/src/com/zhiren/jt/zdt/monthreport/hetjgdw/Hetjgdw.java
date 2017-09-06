package com.zhiren.jt.zdt.monthreport.hetjgdw;
/* 
* ʱ�䣺2009-08-20
* ���ߣ� ll
* �޸����ݣ��޸Ĺ�Ӧ��������sql,������ѡ�糧��id,��Ӧ����������ʾ�õ糧���õ��Ĵ�Ӧ��
* 		   
*/ 
/* 
* ʱ�䣺2009-08-29
* ���ߣ� ll
* �޸����ݣ��޸ķ�վ����վ������,��������ѡ�糧��id,��վ����վ��������ʾ�õ糧���õ��ĳ�վ����
* 		   
*/ 
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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Hetjgdw extends BasePage implements PageValidateListener {
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

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		//-----------------------------------
		String str = "";
		String strdiancTreeID2="";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
			strdiancTreeID2=" and fuid= "+getTreeid();
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
			strdiancTreeID2=" and id="+getTreeid();
		}

		//��Ӧ������
		long gongysID=this.getMeikdqmcValue().getId();
		String strGongysId="  and g.id="+gongysID;
		
		
		/*s*/
		String chaxun = "select h.id,\n"
				+ "       h.riq as riq,\n"
				+ "       dc.mingc as diancxxb_id,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       c1.mingc as faz_id,\n"
				+ "       c2.mingc as daoz_id,\n"
				+ "       c3.mingc as gangk,\n" + "       h.hetl,\n"
				+ "       h.beiz\n" + "  from hetjgdwb h,\n"
				+ "       diancxxb dc,\n" + "       gongysb  g,\n"
				+ "       chezxxb  c1,\n" + "       chezxxb  c2,\n"
				+ "       chezxxb  c3\n" + " where h.diancxxb_id = dc.id\n"
				+ "   and h.gongysb_id = g.id\n" 
				+ "   and h.faz_id = c1.id(+)\n"
				+ "   and h.daoz_id = c2.id(+)\n" 
				+ "   and to_char(h.riq,'yyyy-mm-dd') ='" + intyear + "-01-01' "+str+" \n"
				+ "   and h.gangk = c3.id(+)";

		
		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("hetjgdwb");
   	egu.setWidth("bodyWidth");
	egu.getColumn("riq").setHeader("����");
	egu.getColumn("diancxxb_id").setHeader("��λ");
	egu.getColumn("gongysb_id").setHeader("������");	
	egu.getColumn("faz_id").setHeader("��վ");
	egu.getColumn("daoz_id").setHeader("��վ");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("gangk").setHeader("�ۿ�");
	egu.getColumn("hetl").setHeader("��ͬ��");
	egu.getColumn("hetl").setHidden(true);
	egu.getColumn("hetl").setEditor(null);
	
	egu.getColumn("beiz").setHeader("��ע");
	
	
	//�趨�г�ʼ���
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(100);
	egu.getColumn("diancxxb_id").setWidth(100);
	egu.getColumn("faz_id").setWidth(70);
	egu.getColumn("daoz_id").setWidth(70);
	egu.getColumn("gangk").setWidth(70);
	egu.getColumn("hetl").setWidth(80);
	egu.getColumn("beiz").setWidth(100);
	
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(25);//���÷�ҳ
	
	
	
	//*****************************************����Ĭ��ֵ****************************
	//	�糧������
	int treejib2 = this.getDiancTreeJib();

	if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib2 == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib2 == 3) {// ѡ�糧ֻˢ�³��õ糧
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
	
	//���õ糧Ĭ�ϵ�վ
	egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
	//�������ڵ�Ĭ��ֵ,
	egu.getColumn("riq").setDefaultValue(intyear+"-01-01");
	
	egu.getColumn("hetl").setDefaultValue("0");
	
	
	//*************************������*****************************************88
	//���ù�Ӧ�̵�������
	ComboBox cb_gongsy=new ComboBox();
	egu.getColumn("gongysb_id").setEditor(cb_gongsy);
	cb_gongsy.setEditable(true);
	/*
	 //��糧������Ĺ�Ӧ��
	String GongysSql = "select g.id,g.mingc from diancxxb d,gongysdcglb gd,gongysb  g\n"
				+ "where gd.diancxxb_id=d.id\n"
				+ "and gd.gongysb_id=g.id\n"
				+ "and d.id="+visit.getDiancxxb_id();
	*/
	String GongysSql="select distinct g.id as id,g.mingc as mingc from gongysb g,diancgysglb gl,diancxxb dc\n" +
			"where g.id=gl.gongysb_id and gl.diancxxb_id=dc.id "+str+"\n" + 
			" order by g.mingc";
	egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
	//���÷�վ������
	ComboBox cb_faz=new ComboBox();
	egu.getColumn("faz_id").setEditor(cb_faz);
	cb_faz.setEditable(true);
//	String fazSql="select id ,mingc from chezxxb c where c.leib='��վ' order by c.mingc";
	String fazSql= "select distinct cz.id as id,cz.mingc as mingc\n" +
					"from fahb fh,chezxxb cz,diancxxb dc\n" + 
					"where fh.diancxxb_id=dc.id and fh.faz_id=cz.id and cz.leib='��վ' "+str+"\n" + 
					"order by cz.mingc";

	egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
	//���õ�վ������
	ComboBox cb_daoz=new ComboBox();
	egu.getColumn("daoz_id").setEditor(cb_daoz);
	cb_daoz.setEditable(true);
	/*String daozSql=
		"select c.id,c.mingc  from chezxxb c ,diancdzb dz,diancxxb d\n" +
		"where dz.diancxxb_id=d.id\n" + 
		"and dz.chezxxb_id=c.id\n" + 
		"and d.id="+this.getTreeid();*/
//	String daozSql="select id,mingc from chezxxb c where c.leib='��վ' order by mingc";
	String daozSql= "select distinct cz.id as id,cz.mingc as mingc\n" +
					"from fahb fh,chezxxb cz,diancxxb dc\n" + 
					"where fh.diancxxb_id=dc.id and fh.daoz_id=cz.id and cz.leib='��վ' "+str+"\n" + 
					"order by cz.mingc";
	egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daozSql));
	
	//���øۿ�������
	ComboBox cb_gangk=new ComboBox();
	egu.getColumn("gangk").setEditor(cb_gangk);
	cb_gangk.setEditable(true);
	egu.getColumn("gangk").editor.setAllowBlank(true);
	String huanzgSql="select c.id,c.mingc from chezxxb c where c.leib='�ۿ�' order by mingc ";
	egu.getColumn("gangk").setComboEditor(egu.gridId, new IDropDownModel(huanzgSql));
    
	
	//********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		
		//������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		
	
		
		//�趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		/*egu.addTbarText("->");
		egu.addTbarText("<font color=\"#EE0000\">��λ:���</font>");
		*/
		

		
		
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.getYuefModels();
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			setTbmsg(null);
			//getSelectData();
		}
		//if(nianfchanged||Changeyuef||_meikdqmcchange){
			getSelectData();
		//}
		
	}
//	 ���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
//	 �������
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from gongysb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
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
