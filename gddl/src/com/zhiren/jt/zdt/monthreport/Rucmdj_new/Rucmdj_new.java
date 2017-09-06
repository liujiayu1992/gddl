package com.zhiren.jt.zdt.monthreport.Rucmdj_new;

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
 * 2009-05-23
 * sy
 * ���������ʱ ��"����: �Ƿ񸲸ǵ�ǰ����"��Ϊ "��ʾ:�Ƿ�ȷ������cpi01������?"
 * �޸ĸ��ư�ť����Ϊ����cpi01������
 */
/*
 * 2009-06-19
 * ll
 * ���ӡ���ֵ��������ť��ʵ�������ֵС��20.7MJ/Kg,�����ֱֵ�ӵ���Ϊ20.7MJ/Kg��
 * ����ۺ͵����۽�������ֵΪ20.7MJ/Kg���¼��㡣
 */
/*
 * 2009-06-22
 * ll
 * ҳ�������޸ĺ󳵰�ۡ��޸ĺ���ֵ���޸ĺ󵽳�������js������ֶΣ�������ԭ�г���ۡ�����ֵ�������۽��бȽϡ�
 * ���ӱ��淽����
 */
/*
 * 2009-06-22
 * sy
 * �޸ĵ����۹�ʽ���ֵ�����Ϊ��˰��
 * 
 */
/*
 * 2009-06-23
 * sy
 * �޸�js��ֵ�������ж��ص㶩���Ŀھ�������
 * 
 */


public class Rucmdj_new extends BasePage implements PageValidateListener {
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

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//		setMsg("����ɹ���");
//	}
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
		String CurrODate = DateUtil.FormatOracleDate(intyear+"-"+StrMonth+"-01");
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");

		while (drsl.next()) {
			sql_delete.append("delete from ").append("yuedmjgmxb").append(
					" where id =").append(drsl.getLong("id")).append(";\n");
		}
		sql_delete.append("end;");
		if(sql_delete.length()>11){
		con.getUpdate(sql_delete.toString());
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		while (rsl.next()) {
			sql.delete(0, sql.length());
			sql.append("begin \n");
			long id = 0;
			if ("0".equals(rsl.getString("ID"))) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql.append("insert into yuedmjgmxb(id,diancxxb_id,gongysb_id,jihkjb_id,daohl,chebj,farl,daocj,riq,beiz) values("
								+ id
								+",(select max(id) from diancxxb where mingc ='"
								+rsl.getString("diancxxb_id")
								+ "'),(select max(id) from gongysb where mingc ='"
								+rsl.getString("gongysb_id")+"'),(select max(id) from jihkjb where mingc ='"
								+rsl.getString("jihkjb_id")+"'),"
								+rsl.getDouble("daohl")+","
								+rsl.getDouble("chebj_new")+","
								+rsl.getDouble("farl_new")+","
								+rsl.getDouble("daocj_new")+","
								+CurrODate+",'"
								+rsl.getString("beiz")
								+"');\n");
				
			} else {
				 sql.append("update yuedmjgmxb set diancxxb_id=(select max(id) from diancxxb where mingc ='"
					 + rsl.getString("diancxxb_id")+"'),gongysb_id=(select max(id) from gongysb where mingc ='"
					 + rsl.getString("gongysb_id")+"'),jihkjb_id=(select max(id) from jihkjb where mingc ='"
					 + rsl.getString("jihkjb_id")+"'),daohl="
					 + rsl.getDouble("daohl")+",chebj="
					 + rsl.getDouble("chebj_new")+",farl="
					 + rsl.getDouble("farl_new")+",daocj="
					 + rsl.getDouble("daocj_new")+",riq="
					 + CurrODate+",beiz='"
					 + rsl.getString("beiz")
					 + "' where id=" + rsl.getLong("id")+";\n");
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
		}
		con.Close();
		setMsg("����ɹ�!");
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
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
		if(_CopyButton){
			_CopyButton=false;
			CoypData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}
	public void CoypData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

//		����������ݺ��·�������
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
		

		
		int deletesql = con.getDelete("delete yuedmjgmxb y where y.riq=to_date('" + intyear + "-" + StrMonth+ "-01' ,'yyyy-mm-dd')");
		
		String copyData = "select kj.diancxxb_id,kj.gongysb_id,kj.jihkjb_id,\n"
				+ "       sum(sl.laimsl) as daohl,\n"
				+ "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.meij)/sum(sl.laimsl),2)) as chebj,\n"
				+ "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*zl.qnet_ar)/sum(sl.laimsl),3)) as farl,\n"
				+ "       decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.daozzf+y.zaf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as daoczhj\n"
				+ " from yuercbmdj y,yuetjkjb kj,yueslb sl,yuezlb zl\n"
				+ " where kj.id=y.yuetjkjb_id\n"
				+ " and kj.id=sl.yuetjkjb_id\n" 
				+ " and kj.id=zl.yuetjkjb_id\n"
				+ " and y.fenx='����'\n" 
				+ " and sl.fenx='����'\n"
				+ " and zl.fenx='����' \n"
				+ " and kj.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')\n"
				+ " group by (kj.diancxxb_id,kj.gongysb_id,kj.jihkjb_id)";



		// System.out.println("��������:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData);
		while(rslcopy.next()){
		
			long diancxxb_id=rslcopy.getLong("diancxxb_id");
			long gongysb_id=rslcopy.getLong("gongysb_id");
			long jihkjb_id=rslcopy.getLong("jihkjb_id");
			double daohl=rslcopy.getDouble("daohl");
			double chebj=rslcopy.getDouble("chebj");
			double farl=rslcopy.getDouble("farl");
			double daoczhj=rslcopy.getDouble("daoczhj");
			
			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id());
			
			con.getInsert("insert into yuedmjgmxb(id,diancxxb_id,gongysb_id,jihkjb_id,daohl,chebj,farl,daocj,riq) values(" +
					_id+","+ diancxxb_id+","+gongysb_id +","+jihkjb_id+","+daohl+","+chebj+","+farl+","+daoczhj+","+"to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd'))");
					
		}
		
		con.Close();
		
		
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

		
		String chaxun = "select y.id,\n" 
				+ "       dc.mingc as diancxxb_id,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       j.mingc as jihkjb_id,\n"
				+ "       y.daohl,\n"
				+ "       y.chebj,\n" 
				+ "       y.chebj as chebj_new,\n"
				+ "       y.farl,\n"
				+ "       y.farl as farl_new,\n"
				+ "       y.daocj,\n" 
				+ "       y.daocj as daocj_new,\n"
				+ "       y.riq,\n" 
				+ "       y.beiz\n"
				+ "  from yuedmjgmxb y ,diancxxb dc, gongysb g, jihkjb j\n"
				+ " where y.diancxxb_id = dc.id(+)\n"
				+ "   and y.gongysb_id = g.id(+)\n"
				+ "   and to_char(y.riq,'yyyy-mm') ='" + intyear + "-" + StrMonth+ "'  \n"
				+ "   and y.jihkjb_id = j.id(+)  "+str+"";

		
		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("yuedmjgmxb");
   	egu.setWidth("bodyWidth");
	egu.getColumn("riq").setHeader("����");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("diancxxb_id").setHeader("�糧����");
	egu.getColumn("gongysb_id").setHeader("��ú����");
	egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
	egu.getColumn("daohl").setHeader("������(��)");
	egu.getColumn("chebj").setHeader("�����(Ԫ/��)");
	egu.getColumn("chebj").setEditor(null);
	egu.getColumn("chebj_new").setHeader("�޸ĺ󳵰��(Ԫ/��)");
	egu.getColumn("farl").setHeader("����ֵ(�׽�/ǧ��)");
	egu.getColumn("farl").setEditor(null);
	egu.getColumn("farl_new").setHeader("�޸ĺ���ֵ(�׽�/ǧ��)");
	egu.getColumn("daocj").setHeader("������(Ԫ)");
	egu.getColumn("daocj").setEditor(null);
	egu.getColumn("daocj_new").setHeader("�޸ĺ󵽳���(Ԫ)");
	egu.getColumn("beiz").setHeader("��ע");
	
	//�趨�г�ʼ���
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(100);
	egu.getColumn("diancxxb_id").setWidth(100);
	egu.getColumn("jihkjb_id").setWidth(70);
	egu.getColumn("daohl").setWidth(80);
	egu.getColumn("farl").setWidth(110);
	egu.getColumn("farl_new").setWidth(110);
	egu.getColumn("chebj").setWidth(80);
	egu.getColumn("chebj_new").setWidth(80);
	egu.getColumn("daocj").setWidth(80);
	egu.getColumn("daocj_new").setWidth(80);
	egu.getColumn("beiz").setWidth(120);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(30);//���÷�ҳ
	
//	�趨���ɱ༭�е���ɫ
	egu.getColumn("chebj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("farl").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
	egu.getColumn("daocj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
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
	
	egu.getColumn("jihkjb_id").setDefaultValue("�ص㶩��");
	egu.getColumn("daohl").setDefaultValue("0");
	
	
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
	egu.getColumn("jihkjb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
	String jihkjSql="select j.id,j.mingc from jihkjb j order by id  ";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
	
//	 ---------------ҳ��js�ļ��㿪ʼ------------------------
	
	String sb_str=
        //�ж�ҳ����ÿ����ֵ�Ƿ�С��20.7��С����������¼���
		"  var rows=gridDiv_ds.getTotalCount();\n" +
		"  for (var i=0;i<rows;i++){\n" + 
		"    var rec1=gridDiv_ds.getAt(i);\n" + 
		" if(rec1.get('JIHKJB_ID')!='�ص㶩��'){\n" + 
		"      if(eval(rec1.get('FARL')||0)<20.07){\n" + 
		"      var zaf= Round((eval(rec1.get('DAOCJ')||0)-eval(rec1.get('CHEBJ')||0)),2);\n" + 
		"      rec1.set('DAOCJ_NEW', Round((eval(rec1.get('CHEBJ')||0) * 20.07/ eval(rec1.get('FARL')||0)+(eval(rec1.get('DAOCJ')||0)-eval(rec1.get('CHEBJ')||0))),2));\n" + 
		"      rec1.set('CHEBJ_NEW', Round((eval(rec1.get('CHEBJ')||0) * 20.07/ eval(rec1.get('FARL')||0)),2));\n" + 
		"      rec1.set('FARL_NEW', 20.07);\n" + 
		"       }\n" + 
		"    }\n" + 
		"  }";

	//---------------ҳ��js�������--------------------------
	
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
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NIANF').value+'��'+Ext.getDom('YUEF').value+'�µ�����,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String ss="Ext.MessageBox.confirm('��ʾ', '�Ƿ�ȷ������cpi01������?', function(btn) { if(btn=='yes'){document.getElementById('CopyButton').click();}})";
		egu.addToolbarItem("{"+new GridButton("����cpi01������","function(){"+ss+"}").getScript()+"}");
		egu.addToolbarItem("{"+new GridButton("��ֵ����","function(){"+sb_str+"}").getScript()+"}");
		egu.addTbarText("->");
		egu.addTbarText("<font color=\"#EE0000\">��λ:��</font>");
		

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
