package com.zhiren.jt.zdt.monthreport.rucmjgb;

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

public class Rucmjgb extends BasePage implements PageValidateListener {
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
			sql_delete.append("delete from ").append("rucmjgb").append(" where id =").append(drsl.getLong("id")).append(";\n");
			sql_delete.append("delete from rucmjgb where diancxxb_id=getDiancId('"+drsl.getString("diancxxb_id")+"')" +
					" and gongysb_id=getGongysId('"+drsl.getString("gongysb_id")+"')" +
					" and jihkjb_id=getJihkjbId('"+drsl.getString("jihkjb_id")+"')" +
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
		String gongysb_id="";
		String jihkjb_id="";
		StringBuffer sql = new StringBuffer();
		while (rsl.next()) {
			sql.delete(0, sql.length());
			sql.append("begin \n");
			long id = 0;
			diancxxb_id=rsl.getString("diancxxb_id");
			gongysb_id=rsl.getString("gongysb_id");
			jihkjb_id=rsl.getString("jihkjb_id");

			if ("0".equals(rsl.getString("ID"))) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql.append("insert into rucmjgb("
								+ "id,riq,diancxxb_id,gongysb_id,jihkjb_id,daohl,farl,chebj,yunf,zaf,biaomdj,daoczhj,fenx)values("
								+ id
								+ ",to_date('"
								+ rsl.getString("riq")
								+ "','yyyy-mm-dd'),"
								+"getDiancId('"+rsl.getString("diancxxb_id")+"'),"+""
								+"getGongysId('"+rsl.getString("gongysb_id")+"'),"+""
								+"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),"+
								+ rsl.getDouble("daohl") + ","
								+ rsl.getDouble("farl") + ","
								+ rsl.getDouble("chebj") + ","
								+ rsl.getDouble("yunf") + ","
								+ rsl.getDouble("zaf") + ","
								+" Round("+rsl.getDouble("biaomdj")+",2),"
								+ rsl.getDouble("daoczhj") + ",'����');\n");
				
				
			} else {
				 sql.append("update rucmjgb set diancxxb_id="
				 +"getDiancId('"+rsl.getString("diancxxb_id")+"'),"+""
				 + "gongysb_id="+"getGongysId('"+rsl.getString("gongysb_id")+"'),"+""
				 +" jihkjb_id="+"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),daohl="+
				 + rsl.getDouble("daohl")+",farl="
				 + rsl.getDouble("farl")+",chebj="
				 + rsl.getDouble("chebj")+",yunf="
				 + rsl.getDouble("yunf")+",zaf="
				 + rsl.getDouble("zaf")+",biaomdj="
				 + rsl.getDouble("biaomdj")+",daoczhj="
				 + rsl.getDouble("daoczhj")
				 + " where id=" + rsl.getLong("id")+";\n");
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
			
//			�����ۼ�ֵ*******************************
			ResultSetList rsllj = LeijSelect(diancxxb_id,gongysb_id,jihkjb_id);
			 StringBuffer sqllj = new StringBuffer("begin \n");
			
			 while (rsllj.next()) {
				
				 sqllj.append("delete from rucmjgb where diancxxb_id ="+rsllj.getLong("diancxxb_id")+" " +
				 		" and gongysb_id="+rsllj.getLong("gongysb_id")+" " +
				 		" and jihkjb_id= "+rsllj.getLong("jihkjb_id")+"" +
				 		" and riq=to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd') " +
				 		" and fenx='�ۼ�'").append(";\n");
				 
				 
				 long yuemtgyqkb_id= 0;
				 yuemtgyqkb_id = Long.parseLong(MainGlobal.getNewID(((Visit)getPage().getVisit()).getDiancxxb_id()));
				 sqllj.append(
				 "insert into rucmjgb(id,riq,diancxxb_id,gongysb_id,jihkjb_id,daohl,farl,chebj," +
				 "yunf,zaf,biaomdj,daoczhj,fenx)values("
				 + yuemtgyqkb_id
				 +",to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd'),"
				 +""+rsllj.getLong("diancxxb_id")+","+""
				 +""+rsllj.getLong("gongysb_id")+","+""
				 +""+rsllj.getLong("jihkjb_id")+""+""
				 +","+rsllj.getDouble("daohl")
				 +","+rsllj.getDouble("farl")
				 +","+rsllj.getDouble("chebj")
				 +","+rsllj.getDouble("yunf")
				 +","+rsllj.getDouble("zaf")
				 +",Round("+rsllj.getDouble("biaomdj")+",2)"
				 +","+rsllj.getDouble("daoczhj")
				 +",'�ۼ�');\n");
						
			 }
			 sqllj.append("end;");
			 con.getInsert(sqllj.toString());
			//*****************************
			
		}
		
		con.Close();
		setMsg("����ɹ�!");
	}
	public ResultSetList LeijSelect(String diancxxb_id,String gongysb_id,String jihkjb_id) {
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
		
		String sqllj = "select dc.id as diancxxb_id,\n"
				+ "       g.id as gongysb_id,\n"
				+ "       j.id as jihkjb_id,\n"
				+ "       sum(r.daohl) as daohl,\n"
				+ "       decode(sum(r.daohl),0,0,sum(r.daohl*r.farl)/sum(r.daohl)) as farl,\n"
				+ "       decode(sum(r.daohl),0,0,sum(r.daohl*r.chebj)/sum(r.daohl)) as chebj,\n"
				+ "       decode(sum(r.daohl),0,0,sum(r.daohl*r.yunf)/sum(r.daohl)) as yunf,\n"
				+ "       decode(sum(r.daohl),0,0,sum(r.daohl*r.zaf)/sum(r.daohl)) as zaf,\n"
				+ "       decode(sum(r.daohl),0,0,sum(r.daohl*r.biaomdj)/sum(r.daohl)) as biaomdj,\n"
				+ "       decode(sum(r.daohl),0,0,sum(r.daohl*r.daoczhj)/sum(r.daohl)) as daoczhj\n"
				+ "  from rucmjgb r,gongysb g,jihkjb j,diancxxb dc\n"
				+ " where r.riq >= getYearFirstDate(to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd'))\n"
				+ "   and r.riq <= to_date('"+ intyear + "-"+ StrMonth + "-01', 'yyyy-mm-dd')\n"
				+ "   and r.fenx='����'\n"
				+ "   and r.diancxxb_id=dc.id\n"
				+ "   and r.gongysb_id=g.id\n"
				+ "   and r.jihkjb_id=j.id\n"
				+ "   and dc.id=getDiancId('"+diancxxb_id+"')\n" 
				+ "   and g.id=getGongysId('"+gongysb_id+"')\n" 
				+ "   and j.id=getJihkjbId('"+jihkjb_id+"')\n"
				+ "   group by(dc.id,g.id,j.id)\n" 
				+ "   order by dc.id";

		

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

		
		
	
		
		String chaxun =   "select r.id      as id,\n"
				+ "       r.riq     as riq,\n"
				+ "       dc.mingc  as diancxxb_id,\n"
				+ "       g.mingc   as gongysb_id,\n"
				+ "       j.mingc   as jihkjb_id,\n"
				+ "       r.daohl   as daohl,\n"
				+ "       r.farl    as farl,\n"
				+ "       r.chebj   as chebj,\n"
				+ "       r.yunf    as yunf,\n" 
				+ "       r.zaf     as zaf,\n"
				+ "       r.daoczhj as daoczhj,\n"
				+ "       r.biaomdj as biaomdj,\n"
				+ "       r.fenx as fenx\n"
				+ "  from rucmjgb r, diancxxb dc, gongysb g, jihkjb j\n"
				+ " where r.diancxxb_id = dc.id(+)\n"
				+ "   and r.gongysb_id = g.id(+)\n"
				+ "   and to_char(r.riq,'yyyy-mm') ='" + intyear + "-" + StrMonth+ "'  \n"
				+ "   and r.jihkjb_id = j.id(+)"
				+"    and r.fenx='����' "+str+"";

		
		
		
	//System.out.println(chaxun);	
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("rucmjgb");
   	
	egu.getColumn("riq").setHeader("����");
	egu.getColumn("diancxxb_id").setHeader("�糧����");
	egu.getColumn("gongysb_id").setHeader("ú�����");
	egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
	egu.getColumn("daohl").setHeader("������(��)");
	egu.getColumn("farl").setHeader("����ֵ(�׽�/ǧ��)");
	egu.getColumn("chebj").setHeader("�����(Ԫ/��)");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("yunf").setHeader("�˷�(Ԫ)");
	egu.getColumn("zaf").setHeader("�ӷ�(Ԫ)");
	egu.getColumn("biaomdj").setHeader("��ú����(Ԫ/��)");
	egu.getColumn("biaomdj").setEditor(null);
	egu.getColumn("daoczhj").setHeader("�����ۺϼ�(Ԫ/��)");
	egu.getColumn("daoczhj").setEditor(null);
	egu.getColumn("fenx").setHeader("����");
	egu.getColumn("fenx").setEditor(null);
	egu.getColumn("fenx").setHidden(true);
	egu.getColumn("fenx").setDefaultValue("����");
	
	//�趨�г�ʼ���
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(120);
	egu.getColumn("diancxxb_id").setWidth(120);
	egu.getColumn("jihkjb_id").setWidth(70);
	egu.getColumn("daohl").setWidth(80);
	egu.getColumn("farl").setWidth(110);
	egu.getColumn("chebj").setWidth(80);
	egu.getColumn("yunf").setWidth(70);
	egu.getColumn("zaf").setWidth(70);
	egu.getColumn("biaomdj").setWidth(90);
	egu.getColumn("daoczhj").setWidth(110);
//	���ò��ɱ༭����ɫ
	egu.getColumn("BIAOMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("daoczhj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
	
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
	egu.getColumn("daohl").setDefaultValue("0");
	egu.getColumn("farl").setDefaultValue("0");
	egu.getColumn("chebj").setDefaultValue("0");
	egu.getColumn("yunf").setDefaultValue("0");
	egu.getColumn("zaf").setDefaultValue("0");
	egu.getColumn("biaomdj").setDefaultValue("0");
	egu.getColumn("daoczhj").setDefaultValue("0");
	
	
	
	
	//*************************������*****************************************88
	//���ù�Ӧ�̵�������
	ComboBox cb_gongys=new ComboBox();
	egu.getColumn("gongysb_id").setEditor(cb_gongys);
	cb_gongys.setEditable(true);
	/*
	 //��糧������Ĺ�Ӧ��
	String GongysSql = "select g.id,g.mingc from diancxxb d,gongysdcglb gd,gongysb  g\n"
				+ "where gd.diancxxb_id=d.id\n"
				+ "and gd.gongysb_id=g.id\n"
				+ "and d.id="+visit.getDiancxxb_id();
	*/
	String GongysSql="select id,mingc from gongysb order by mingc";
	egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
	//���üƻ��ھ�������
	ComboBox cb_jihkj=new ComboBox();
	egu.getColumn("jihkjb_id").setEditor(cb_jihkj);
	cb_jihkj.setEditable(true);
	
	String jihkjSql="select j.id,j.mingc from jihkjb j order by id  ";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
	
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
		egu.addTbarText("<font color=\"#EE0000\">��λ:��</font>");
		
		
//		---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//��ú����=(�����/1.13+�˷�*0.93+�ӷ�)*29.271/������
			sb.append("if(e.record.get('FARL')==0){ e.record.set('BIAOMDJ',0 );}else{");
			sb.append("if(e.field == 'CHEBJ'||e.field == 'YUNF'||e.field == 'ZAF'||e.field == 'FARL'){e.record.set('BIAOMDJ',Round(((parseFloat(e.record.get('CHEBJ')==''?0:e.record.get('CHEBJ'))/1.13+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))*0.93+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')))*29.271/parseFloat(e.record.get('FARL')==''?0:e.record.get('FARL'))),2) )};");
			sb.append("};");
			//�����ۺϼ�=�����+�˷�+�ӷ�
			sb.append("if(e.field == 'CHEBJ'||e.field == 'YUNF'||e.field == 'ZAF'){e.record.set('DAOCZHJ',Round((parseFloat(e.record.get('CHEBJ')==''?0:e.record.get('CHEBJ'))+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF'))),2))};");
			
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
