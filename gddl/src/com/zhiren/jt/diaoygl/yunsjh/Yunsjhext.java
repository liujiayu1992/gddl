package com.zhiren.jt.diaoygl.yunsjh;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunsjhext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//���·���1��ʱ����ʾ01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
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
		
		
		String chaxun = "\n"
				+ "select y.id,y.riq,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       dc.mingc as diancxxb_id,\n"
				+ "       l.mingc as tielj,\n"
				+ "       c.mingc as faz_id,\n"
				+ "       c1.mingc as daoz_id,\n"
				+ "       p.mingc as pinm,\n"
				+ "       y.pic,\n"
				+ "       y.pid,\n"
				+ "       y.pizjhh,\n"
				+ "       c2.mingc as huanzg,\n"
				+ "       c3.mingc as zongdg,\n"
				+ "       y.zibccc,\n"
				+ "       y.shunh,\n"
				+ "       y.leix\n"
				+ "  from yunsjhb y, gongysb g, (select id,mingc,fuid from diancxxb where jib=3"+strdiancTreeID2+")dc, chezxxb c, chezxxb c1,chezxxb c2,chezxxb c3, lujxxb l,pinzb p\n"
				+ " where y.gongysb_id = g.id(+)\n"
				+ "   and y.diancxxb_id = dc.id(+)\n"
				+ "   and y.faz_id = c.id(+)\n"
				+ "   and y.daoz_id = c1.id(+)\n" 
				+ "   and y.pinm=p.id(+)\n"
				+ "   and y.huanzg=c2.id(+)\n"
				+ "   and y.zongdg=c3.id(+)\n"
				+ "   and y.tielj = l.id(+)\n"
				+ "   and to_char(y.riq,'yyyy-mm') ='" + intyear + "-" + StrMonth+ "'  \n"
				+ "    and y.leix = 1  "+str+"  "+strGongysId+"";
	//System.out.println(chaxun);	
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("yunsjhb");
   	
	egu.getColumn("riq").setHeader("����");
	egu.getColumn("gongysb_id").setHeader("��Ӧ��");
	egu.getColumn("diancxxb_id").setHeader("��λ");
	egu.getColumn("tielj").setHeader("��·��");
	egu.getColumn("faz_id").setHeader("��վ");
	egu.getColumn("daoz_id").setHeader("��վ");
	egu.getColumn("pinm").setHeader("Ʒ��");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("pic").setHeader("����");
	egu.getColumn("pid").setHeader("����");
	egu.getColumn("pizjhh").setHeader("��׼�ƻ���");
	egu.getColumn("huanzg").setHeader("��װ��");
	egu.getColumn("zongdg").setHeader("�յ���");
	egu.getColumn("zibccc").setHeader("�Ա�������");
	egu.getColumn("shunh").setHeader("˳��");
	egu.getColumn("leix").setHidden(true);
	egu.getColumn("leix").setEditor(null);
	
	
	//�趨�г�ʼ���
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(100);
	egu.getColumn("diancxxb_id").setWidth(100);
	egu.getColumn("tielj").setWidth(60);
	egu.getColumn("faz_id").setWidth(70);
	egu.getColumn("daoz_id").setWidth(70);
	egu.getColumn("pinm").setWidth(50);
	egu.getColumn("pic").setWidth(60);
	egu.getColumn("pid").setWidth(60);
	egu.getColumn("pizjhh").setWidth(80);
	egu.getColumn("huanzg").setWidth(70);
	egu.getColumn("zongdg").setWidth(70);
	egu.getColumn("zibccc").setWidth(90);
	egu.getColumn("shunh").setWidth(60);
	
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(100);//���÷�ҳ
	egu.setWidth(1000);//����ҳ��Ŀ��,������������ʱ��ʾ������
	
	
	
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
	egu.getColumn("leix").setDefaultValue("1");
	//���õ糧Ĭ�ϵ�վ
	egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
	//�������ڵ�Ĭ��ֵ,
	egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	//���ù�Ӧ�̵�Ĭ��ֵ
	egu.getColumn("gongysb_id").setDefaultValue(this.getMeikdqmcValue().getValue());
	
	
	
	
	//*************************������*****************************************88
	//���ù�Ӧ�̵�������
	egu.getColumn("gongysb_id").setEditor(new ComboBox());
	/*
	 //��糧������Ĺ�Ӧ��
	String GongysSql = "select g.id,g.mingc from diancxxb d,gongysdcglb gd,gongysb  g\n"
				+ "where gd.diancxxb_id=d.id\n"
				+ "and gd.gongysb_id=g.id\n"
				+ "and d.id="+visit.getDiancxxb_id();
	*/
	String GongysSql="select id,mingc from gongysb order by mingc";
	egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
	//���÷�վ������
	ComboBox cb_faz=new ComboBox();
	egu.getColumn("faz_id").setEditor(cb_faz);
	cb_faz.setEditable(true);
	String fazSql="select id ,mingc from chezxxb c where c.leib='��վ' order by c.mingc";
	egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
	//���õ�վ������
	ComboBox cb_daoz=new ComboBox();
	egu.getColumn("daoz_id").setEditor(cb_daoz);
	cb_daoz.setEditable(true);
	/*String daozSql=
		"select c.id,c.mingc  from chezxxb c ,diancdzb dz,diancxxb d\n" +
		"where dz.diancxxb_id=d.id\n" + 
		"and dz.chezxxb_id=c.id\n" + 
		"and d.id="+this.getTreeid();
;*/
	String daozSql="select id,mingc from chezxxb order by mingc";
	egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daozSql));
	//����Ʒ��������
	ComboBox cb_pinm=new ComboBox();
	egu.getColumn("pinm").setEditor(cb_pinm);
	cb_pinm.setEditable(true);
	String pinmSql="select id,mingc from pinzb order by mingc";
	egu.getColumn("pinm").setComboEditor(egu.gridId, new IDropDownModel(pinmSql));
	//���û�װ��������
	ComboBox cb_huanzg=new ComboBox();
	egu.getColumn("huanzg").setEditor(cb_huanzg);
	cb_huanzg.setEditable(true);
	egu.getColumn("huanzg").editor.setAllowBlank(true);
	String huanzgSql="select c.id,c.mingc from chezxxb c where c.leib='�ۿ�' order by mingc ";
	egu.getColumn("huanzg").setComboEditor(egu.gridId, new IDropDownModel(huanzgSql));
    //�����յ���������
	ComboBox cb_zongdg=new ComboBox();
	egu.getColumn("zongdg").setEditor(cb_zongdg);
	cb_zongdg.setEditable(true);
	egu.getColumn("zongdg").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String zongdgSql="select c.id,c.mingc from chezxxb c where c.leib='�ۿ�' order by mingc ";
	egu.getColumn("zongdg").setComboEditor(egu.gridId, new IDropDownModel(zongdgSql));
	//������·��������
	ComboBox cb_tielj=new ComboBox();
	egu.getColumn("tielj").setEditor(cb_tielj);
	cb_tielj.setEditable(true);
	String tieljSql="select id,mingc from lujxxb order by mingc  ";
	egu.getColumn("tielj").setComboEditor(egu.gridId, new IDropDownModel(tieljSql));
	

	
	//********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("�·�:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");//���÷ָ���
		//������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		
		
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��Ӧ��:");
		ComboBox comb3=new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);//��̬��
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());
		
		//�趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});gongys.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("->");
		egu.addTbarText("<font color=\"#EE0000\">��λ:��</font>");
		
		
//		---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		//sb.append("if (e.row==7){e.cancel=true;}");//�趨��8�в��ɱ༭,�����Ǵ�0��ʼ��
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");//�趨��Ӧ���в��ɱ༭
//		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�趨�糧��Ϣ�в��ɱ༭
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		//---------------ҳ��js�������--------------------------
		
		
		
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
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		cn.Close();
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
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
}
