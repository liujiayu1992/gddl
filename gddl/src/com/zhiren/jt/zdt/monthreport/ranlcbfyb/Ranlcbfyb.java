package com.zhiren.jt.zdt.monthreport.ranlcbfyb;

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
import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ranlcbfyb extends BasePage implements PageValidateListener {
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

//		 ����������ݺ��·�������
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
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//--------------------------------
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");

		while (drsl.next()) {
			sql_delete.append("delete from ").append("ranlcbfyb").append(" where id =").append(drsl.getLong("id")).append(";\n");
			sql_delete.append("delete from ranlcbfyb where diancxxb_id=getDiancId('"+drsl.getString("diancxxb_id")+"')" +
					" and riq=to_date('"+ drsl.getString("riq")+ "','yyyy-mm-dd')" +
					" and fenx='�ۼ�'").append(";\n");
		}
		sql_delete.append("end;");
		//System.out.println(sql_delete.length());
		if(sql_delete.length()>11){
			con.getUpdate(sql_delete.toString());
		
		}
		
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		String diancxxb_id="";
		
		StringBuffer sql = new StringBuffer();
		while (rsl.next()) {
			sql.delete(0, sql.length());
			sql.append("begin \n");
			long id = 0;
			diancxxb_id=rsl.getString("diancxxb_id");
		

			if ("0".equals(rsl.getString("ID"))) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql.append("insert into ranlcbfyb("
								+ "id,riq,diancxxb_id,ranlzcb,fadmtcb,gongrmtcb,fadycb,gongrycb,faddwrlcb,fenx)values("
								+ id
								+ ",to_date('"
								+ rsl.getString("riq")
								+ "','yyyy-mm-dd'),"
								+"getDiancId('"+rsl.getString("diancxxb_id")+"'),"+""
								+ rsl.getDouble("ranlzcb") + ","
								+ rsl.getDouble("fadmtcb") + ","
								+ rsl.getDouble("gongrmtcb") + ","
								+ rsl.getDouble("fadycb") + ","
								+ rsl.getDouble("gongrycb") + ","
								+" Round("+rsl.getDouble("faddwrlcb")+",2),"
								+ "'����');\n");
				
				
			} else {
				 sql.append("update ranlcbfyb set diancxxb_id="
				 +"getDiancId('"+rsl.getString("diancxxb_id")+"'),"+""
				 +"ranlzcb="+
				 + rsl.getDouble("ranlzcb")+",fadmtcb="
				 + rsl.getDouble("fadmtcb")+",gongrmtcb="
				 + rsl.getDouble("gongrmtcb")+",fadycb="
				 + rsl.getDouble("fadycb")+",gongrycb="
				 + rsl.getDouble("gongrycb")+",faddwrlcb="
				 + rsl.getDouble("faddwrlcb")
				 + " where id=" + rsl.getLong("id")+";\n");
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
			
//			�����ۼ�ֵ*******************************
			ResultSetList rsllj = LeijSelect(diancxxb_id);
			 StringBuffer sqllj = new StringBuffer("begin \n");
			
			 while (rsllj.next()) {
				
				 sqllj.append("delete from ranlcbfyb where diancxxb_id ="+rsllj.getLong("diancxxb_id")+" " +
				 		" and riq=to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd') " +
				 		" and fenx='�ۼ�'").append(";\n");
				 
				 
				 long yuemtgyqkb_id= 0;
				 yuemtgyqkb_id = Long.parseLong(MainGlobal.getNewID(((Visit)getPage().getVisit()).getDiancxxb_id()));
				 sqllj.append(
				 "insert into ranlcbfyb(id,riq,diancxxb_id,ranlzcb,fadmtcb,gongrmtcb,fadycb,gongrycb,faddwrlcb,fenx)values(" 
				 + yuemtgyqkb_id
				 +",to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd'),"
				 +""+rsllj.getLong("diancxxb_id")
				 +","+rsllj.getDouble("ranlzcb")
				 +","+rsllj.getDouble("fadmtcb")
				 +","+rsllj.getDouble("gongrmtcb")
				 +","+rsllj.getDouble("fadycb")
				 +","+rsllj.getDouble("gongrycb")
				 +",Round("+rsllj.getDouble("faddwrlcb")+",2)"
				 +",'�ۼ�');\n");
						
			 }
			 sqllj.append("end;");
			 con.getInsert(sqllj.toString());
			//*****************************
			
		}
		con.Close();
		setMsg("����ɹ���");
	}
	public ResultSetList LeijSelect(String diancxxb_id) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 ����������ݺ��·�������
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
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		

		String sqllj = "select r.diancxxb_id,sum(r.ranlzcb)as ranlzcb,sum(r.fadmtcb) as fadmtcb,sum(r.gongrmtcb) as gongrmtcb,\n"
				+ "       sum(r.fadycb) as fadycb,sum(r.gongrycb) as gongrycb,sum(r.faddwrlcb) as faddwrlcb\n"
				+ "  from ranlcbfyb r\n"
				+ " where r.riq >= getYearFirstDate(to_date('"+ intyear + "-"+ StrMonth + "-01', 'yyyy-mm-dd'))\n"
				+ "   and r.riq <= to_date('"+ intyear + "-"+ StrMonth + "-01', 'yyyy-mm-dd')\n"
				+ "   and r.fenx = '����'\n"
				+ "   and r.diancxxb_id = getDiancId('"+diancxxb_id+"')\n"
				+ "   group by(r.diancxxb_id)";


		ResultSetList rsllj = con.getResultSetList(sqllj);
		con.Close();
		return rsllj;
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

		
		
	
		
		String chaxun = "select r.id,r.riq,dc.mingc as diancxxb_id,r.ranlzcb,r.fadmtcb,r.gongrmtcb,r.fadycb,\n"
				+ "       r.gongrycb,r.faddwrlcb,r.fenx\n"
				+ "  from ranlcbfyb r, diancxxb dc\n"
				+ "  where r.riq=to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd')\n"
				+ " and r.diancxxb_id=dc.id\n"
				+ "  and r.fenx='����'";


		
		
		
	//System.out.println(chaxun);	
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("ranlcbfyb");
   	
	egu.getColumn("riq").setHeader("����");
	egu.getColumn("diancxxb_id").setHeader("�糧����");
	egu.getColumn("ranlzcb").setHeader("ȼ���ܳɱ�");
	egu.getColumn("ranlzcb").setEditor(null);
	egu.getColumn("fadmtcb").setHeader("����ú̿�ɱ�");
	egu.getColumn("gongrmtcb").setHeader("����ú̿�ɱ�");
	egu.getColumn("fadycb").setHeader("�����ͳɱ�");
	egu.getColumn("gongrycb").setHeader("�����ͳɱ�");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("faddwrlcb").setHeader("���絥λȼ�ϳɱ�");
	
	egu.getColumn("fenx").setHeader("����");
	egu.getColumn("fenx").setEditor(null);
	egu.getColumn("fenx").setHidden(true);
	egu.getColumn("fenx").setDefaultValue("����");
	
	//�趨�г�ʼ���
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("diancxxb_id").setWidth(120);
	egu.getColumn("ranlzcb").setWidth(90);
	egu.getColumn("fadmtcb").setWidth(90);
	egu.getColumn("gongrmtcb").setWidth(90);
	egu.getColumn("fadycb").setWidth(90);
	egu.getColumn("gongrycb").setWidth(90);
	egu.getColumn("faddwrlcb").setWidth(110);
	
//	���ò��ɱ༭����ɫ
	egu.getColumn("ranlzcb").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
	
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
	
	
	//�������ڵ�Ĭ��ֵ,
	egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	egu.getColumn("ranlzcb").setDefaultValue("0");
	egu.getColumn("fadmtcb").setDefaultValue("0");
	egu.getColumn("gongrmtcb").setDefaultValue("0");
	egu.getColumn("fadycb").setDefaultValue("0");
	egu.getColumn("gongrycb").setDefaultValue("0");
	egu.getColumn("faddwrlcb").setDefaultValue("0");
	
	
	
	
	//*************************������*****************************************88
	
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
		
		
		
		
		
		//�趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("->");
		egu.addTbarText("<font color=\"#EE0000\">��λ:��Ԫ</font>");
		
		
//		---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			
			
			//ȼ���ܳɱ�=����ú�ɱ�+����ú�ɱ�+�����ͳɱ�+�����ͳɱ�+
			sb.append("if(e.field == 'FADMTCB'||e.field == 'GONGRMTCB'||e.field == 'FADYCB'||e.field == 'GONGRYCB'){e.record.set('RANLZCB',Round((parseFloat(e.record.get('FADMTCB')==''?0:e.record.get('FADMTCB'))+parseFloat(e.record.get('GONGRMTCB')==''?0:e.record.get('GONGRMTCB'))+parseFloat(e.record.get('FADYCB')==''?0:e.record.get('FADYCB'))+parseFloat(e.record.get('GONGRYCB')==''?0:e.record.get('GONGRYCB'))),2))};");
			
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
			setTbmsg(null);
			
		}
		
			getSelectData();
		
		
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
