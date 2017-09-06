package com.zhiren.dc.hesgl.changnzf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
/**
 * @author ����
 * 2010-01-27
 * ������װ������ѡ��ҳ��
 */
public class Zhuangcjs extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {

		super.initialize();
		setMsg("");
	}

	// ����������ȡֵ

	public String getChangb() {

		if (((Visit) this.getPage().getVisit()).getString3().equals("")) {

			((Visit) this.getPage().getVisit())
					.setString3(((IDropDownBean) getChangbSelectModel()
							.getOption(0)).getValue());
		}

		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}

	// �����㵥λ������
	public String getMainjsdw() {

		if (((Visit) this.getPage().getVisit()).getString13().equals("")) {

			((Visit) this.getPage().getVisit())
					.setString13(((IDropDownBean) getChangbSelectModel()
							.getOption(0)).getValue());
		}

		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setMainjsdw(String value) {

		((Visit) this.getPage().getVisit()).setString13(value);
	}

	// ���ձ��������ȡֵ

	public String getYansbh() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setYansbh(String yansbh) {
		((Visit) this.getPage().getVisit()).setString4(yansbh);
	}

	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString5(rbvalue);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		getSelectData();
	}

	private boolean _SbChick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbChick = true;
	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}
	
	private boolean _DJChick = false;

	public void DJButton(IRequestCycle cycle) {
		_DJChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_SbChick) {
			_SbChick = false;
			getSelectData();
		}

		if (_RbChick) {
			_RbChick = false;
			gotoZafjs(cycle);
		}
		
		if (_DJChick) {
			_DJChick = false;
			cycle.activate("Zhuangcdjwh");
		}
	}
	
	Calendar temp1; // ������ʱ�������ڱȽ���ʼ���ںͽ�ֹ����
	Calendar temp2;
	//���ܣ���ת����
	//�߼���������ҳ����Ҫ�Ĳ������浽visit�ı����У���ת��װ�����㣨Zcjs��ҳ��
	private void gotoZafjs(IRequestCycle cycle) {
		
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = this.getExtGrid();
		ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
		ResultSetList mdrsl1 = egu.getModifyResultSet(this.getChange());
		Visit visit = (Visit)this.getPage().getVisit();
		
		//������
		SimpleDateFormat sdfbm = new SimpleDateFormat("yyyyMMdd");
		String bmsql = "select max(bianm) as bianm from zafjsb where bianm like '"+ sdfbm.format(new Date()).substring(0, 6) +"___'";
		ResultSetList bmrsl = con.getResultSetList(bmsql);
		String bianm = "";
		while(bmrsl.next()) {
			if (bmrsl.getString("bianm").equals("")) {
				bianm = sdfbm.format(new Date()).substring(0, 6) + "001";
			} else {
				bianm = (Integer.parseInt(bmrsl.getString("bianm"))+1)+"";
			}
		}
		visit.setString12(bianm);
		
		//itemID;fahbID
		StringBuffer sbsql = new StringBuffer();
		while(mdrsl1.next()) {
			sbsql.append(mdrsl1.getString("id")).append(";").append(mdrsl1.getString("fahid")).append(",");
		}
		visit.setString11(sbsql.substring(0, sbsql.length()-1));

		int sumChes = 0;//����
		double sumKoud = 0.00;//�۶�
		double zhi = 0.00;//˰��
		double sumShul = 0.0; // �����ܺ�
		double sumDanj = 0.0; // �����ܺ�
		double sumFeiyhj = 0.0; // ���úϼ��ܺ�
		double sumBuhszf = 0.0; // ����˰�ӷ��ܺ�
		int mark = 0; // ��һ��ѭ���Ƚ���ʼ���ڡ���ֹ���ڡ���ע�ı��
		String gys = "";
		String shoukdw = ""; //�տλ
		String beiz = "";//��ע��ú��λ
		StringBuffer strsb = new StringBuffer();
		ResultSetList rsl_skdw = null;//�ж��տλ
		String sql_skdw="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		while(mdrsl.next()) {
//			��ȡѡ���¼�Ĺ�����λ�ֶε����й�Ӧ��
			gys = gys+"'"+mdrsl.getString("fahdw")+"',";
			
			//����
			sumChes = sumChes + mdrsl.getInt("ches");
			
			//�۶�
			sumKoud = sumKoud + mdrsl.getDouble("koud");
			
//			��ȡѡ���¼�������ֶε��ܺ�
			sumShul = sumShul + Double.parseDouble(mdrsl.getString("duns"));
			
//			��ȡѡ���¼�ĵ����ֶε�ֵ��ȡ���ģ�
			if(sumDanj<Double.parseDouble(mdrsl.getString("danj"))){
				sumDanj = Double.parseDouble(mdrsl.getString("danj"));
			}
			
//			��ȡѡ���¼�Ĳ���˰�ӷѵ��ܺ�(����˰�ӷ�=���úϼ�*(1 - ˰��))
//			sumBuhszf = sumBuhszf +  Double.parseDouble(mdrsl.getString("feiyhj")) * (1.0 - Double.parseDouble(mdrsl.getString("shuil")));
//			visit.setDouble4(Math.round(sumBuhszf * 100)/100.0);
			
//			��ȡ�տλ����
			sql_skdw = "select mingc from shoukdw where mingc='"+mdrsl.getString("zhuangcdw")+"'\n";
			rsl_skdw = con.getResultSetList(sql_skdw);
			if(rsl_skdw.next()){
				shoukdw = mdrsl.getString("zhuangcdw");
			} else {
				setMsg("װ����λ"+mdrsl.getString("zhuangcdw")+"���տλ���в����ڣ����������տλ��");
			}
			
//			��ȡ�տλID
			visit.setString13(mdrsl.getString("shoukdw_id"));
			
//			��ȡ��ѡ��¼����ʼ���ںͽ�ֹ���ڣ����ѡ�������¼��ô��ȡ��С�����ں���������
			try {
				
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				c1.setTime(sdf.parse(mdrsl.getString("fahrq")));
				c2.setTime(sdf.parse(mdrsl.getString("fahrq")));
				if (mark == 0) { // ��һ��ѭ�����Ƚ���ʼ���÷�Χ�ͽ�ֹ���÷�Χ��ֻ�����Ƿŵ��������Թ��´�ѭ���Ƚ�
					temp1 = c1;
					temp2 = c2;
				} else {
					if (!temp1.before(c1)) {
						temp1 = c1;
					}
					if (!temp2.after(c2)) {
						temp2 = c2;
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
//			ú����Ϣ�����ڱ�ע�
			if (mark == 0) {//��һ��ѭ��
				strsb.append(mdrsl.getString("meikxxb_id"));
			} else {
				strsb.append(",").append(mdrsl.getString("meikxxb_id"));
			}
			
//			if (mdrsl.getRows() == 1) {
//				visit.setString10(mdrsl.getString("beiz"));
//			} else {
//				strsb.append(mdrsl.getString("beiz")).append(";");
//				visit.setString10(strsb.substring(0, strsb.length() - 1));
//			}
			
			if(zhi<mdrsl.getDouble("shuil")){
				zhi=mdrsl.getDouble("shuil");
			}
			
			mark ++;
		}
		
//		��ȡѡ���¼�ķ��úϼ��ֶε��ܺ�
		sumFeiyhj = CustomMaths.mul(sumShul,sumDanj);

		
//		��ȡ��Ӧ�����ƣ����ѡ��ļ�¼�Ĺ�Ӧ����ͬ���ڽ��㵥��ʾ��������ʾ��
		String sql = "select distinct mingc from gongysb where mingc in(" + gys.substring(0, gys.length()-1)  + ")";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() == 1) {
			if(rsl.next()){
				visit.setString1(rsl.getString("mingc"));
			}
		} else {
			visit.setString1("");
		}
		
		visit.setDouble2(CustomMaths.Round_new(sumShul, 2));//��������
		visit.setDouble3(CustomMaths.Round_new(sumDanj,2));//����
		visit.setDouble4(CustomMaths.Round_new(CustomMaths.mul(sumFeiyhj, 1-zhi), 2));//����˰�ӷ�
		visit.setDouble5(CustomMaths.Round_new(CustomMaths.mul(sumFeiyhj, zhi),2));//�ӷ�˰��
		visit.setDouble6(CustomMaths.Round_new(sumFeiyhj, 2));//���úϼ�
		visit.setDouble7(CustomMaths.Round_new(sumKoud, 2));//�۶�
		visit.setInt2(sumChes);//����
		
		visit.setString7(shoukdw);//�տλ
		visit.setString8(sdf.format(temp1.getTime()));//��ʼ����
		visit.setString9(sdf.format(temp2.getTime()));//��ֹ����
		visit.setDouble16(CustomMaths.Round_new(zhi, 2));//˰��
		
		sql = "select mingc from meikxxb where id in (" +strsb.toString()+")";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			beiz=beiz+rsl.getString("mingc")+",";
		}
		visit.setString10(beiz.substring(0,beiz.length()-1));
		
		rsl.close();
		rsl_skdw.close();
		con.Close();
		
////		��ȡѡ���¼�ĵ��۲�������Visit�У����ѡ����Ƕ�����¼��ô����=���úϼ�/��������
//		visit.setDouble3(Math.round(sumFeiyhj/sumShul*100)/100.0);
		
//		��ȡ�ӷ�˰�������Visit�У��ӷ�˰��=���úϼ�-����˰�ӷ�
//		visit.setDouble5(Math.round((sumFeiyhj - (Math.round(sumBuhszf * 100)/100.0)) * 100) / 100.0);
		
		cycle.activate("Zcjs");
	}

	public String rb1() {
		if (getRbvalue() == null || getRbvalue().equals("")) {

			return "fh.fahrq";

		} else {
			if (getRbvalue().equals("fahrq")) {
				return "fh.fahrq";
			} else
				return "fh.daohrq";
		}

	}

	public String rb2() {
		if (getRbvalue() == null || getRbvalue().equals("")) {

			return Locale.fahrq_fahb + ":";// ��������

		} else {
			if (getRbvalue().equals("fahrq")) {
				return Locale.fahrq_fahb + ":";
			} else
				return Locale.daohrq_id_fahb + ":";// ��������
		}

	}

	public String riq1() {
		String riqTiaoq = this.getQisriqi();
		if (riqTiaoq == null || riqTiaoq.equals("")) {
			riqTiaoq = DateUtil.FormatDate(new Date());

		}
		return riqTiaoq;
	}

	public String riq2() {
		String riqTiaoj = this.getJiezriqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());

		}
		return riqTiaoj;
	}

	// private String tree = "";

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		String str = "";
		String tiaoj_fahrq = "";
		String tiaoj_daohrq = "";
		String tiaoj_changb = "";
		String tiaoj_returnvalue_changb = "";

		if (getRbvalue() == null || getRbvalue().equals("fahrq")
				|| getRbvalue().equals("")) {

			tiaoj_fahrq = "checked:true, \n";

		} else if (getRbvalue().equals("daohrq")) {

			tiaoj_daohrq = "checked:true, \n";
		}

		tiaoj_changb = ", \n" + "{  \n" + "	xtype:'textfield', \n"
				+ "	fieldLabel:'����', \n" + "	width:0  \n" + "}, \n"
				+ "Changb=new Ext.zr.select.Selectcombo({ \n"
				+ "	multiSelect:true,\n" + "	width:150, \n"
				+ "	transform:'ChangbDropDown', \n" + "	lazyRender:true, \n"
				+ "	triggerAction:'all', \n" + "	typeAhead:true, \n"
				+ "	forceSelection:true \n" + "}),	\n" + "{	\n"
				+ "	xtype:'textfield', \n" + "	fieldLabel:'�����㵥λ', \n"
				+ "	width:0  \n" + "}, \n"
				+ "Mainjsdw=new Ext.form.ComboBox({	\n" + "	width:150,	\n"
				+ "	selectOnFocus:true,	\n"
				+ "	transform:'MainjsdwDropDown',	\n" + "	lazyRender:true, \n"
				+ "	triggerAction:'all', \n" + "	forceSelection:true \n"
				+ "})	\n";

		tiaoj_returnvalue_changb = "	document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue();	\n"
				+ "	document.getElementById('TEXT_MAINJSDW_VALUE').value=Mainjsdw.getRawValue();	\n";

		str = "var form = new Ext.form.FormPanel({ " + "baseCls: 'x-plain', \n"
				+ "labelAlign:'right', \n" + "defaultType: 'radio',\n"
				+ "items: [ \n" + "{ \n" + "    	xtype:'textfield', \n"
				+ "    	fieldLabel:'����ѡ��',\n"

				+ "    	width:0 \n" + "    },	\n" + " { \n" + "		boxLabel:'"
				+ Locale.fahrq_fahb + "', \n" + "     anchor:'95%', \n";

		str += tiaoj_fahrq;

		str += "     Value:'fahrq', \n"
				+ "		id:'fahrq',\n"
				+ "		name:'test',\n"
				+ "		listeners:{ \n"
				+ "				'focus':function(r,c){\n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "				},\n"
				+ "				'check':function(r,c){ \n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "					\tif(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){	\n"
				+ " 				\t	document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;} \n"
				+ "				}\n" + "		} \n"

				+ "	},\n" + "	{  \n"

				+ "		boxLabel:'" + Locale.daohrq_id_fahb + "',\n";
		str += tiaoj_daohrq;

		str += "		Value:'daohrq', \n"
				+ "     anchor:'95%',	\n"
				+ "		id:'daohrq',\n"
				+ "		name:'test',\n"
				+ "		listeners:{ \n"
				+ "				'focus':function(r,c){ \n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "				}, \n"
				+ "				'check':function(r,c){\n"
				+ "					document.getElementById('rbvalue').value=r.Value;\n"
				+ "					\tif(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){	\n"
				+ " 				\t	document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;} \n"
				+ "				}\n"

				+ "		}	\n"
				+ "	}		\n"

				+ tiaoj_changb
				+ "]\n"
				+ " });\n"

				+ " win = new Ext.Window({\n"
				+ " el:'hello-win',\n"
				+ " layout:'fit',\n"
				+ " width:500,\n"
				+ " height:300,\n"
				+ " closeAction:'hide',\n"
				+ " plain: true,\n"
				+ " title:'����',\n"
				+ " items: [form],\n"

				+ "buttons: [{\n"
				+ "    	text:'ȷ��',\n"
				+ "   	handler:function(){  \n"
				+ "   	win.hide();\n"
				+ tiaoj_returnvalue_changb
				+ "		document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
				+ " 	document.forms[0].submit(); \n"
				+ "  }   \n"
				+ "},{\n"
				+ "   text: 'ȡ��',\n"
				+ "   handler: function(){\n"
				+ "       	win.hide();\n"
				+ "			document.getElementById('TEXT_MAINJSDW_VALUE').value='';	\n"
				+ "			document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
				+ "			document.getElementById('TEXT_YANSBH_VALUE').value='';	\n"
				+ "   }\n" + "}]\n" + " });";

		String chaxun = "select it.id,fh.id as fahid,decode(grouping(it.mingc),1,'�ϼ�',it.mingc) as zhuangcdw,\n"
				+ "       fh.fahrq,\n"
				+ "       fh.daohrq,\n"
				+ "       fh.gongysb_id,\n"
				+ "       gys.mingc as fahdw,\n"
				+ "       fh.meikxxb_id,\n"
				+ "       m.mingc as meikdw,\n"
				+ "       sum(c.ches) ches,\n"
				+ "       sum(c.maoz - c.piz - c.zongkd) duns,\n"
				+ "       nvl(GetDanj(it.id,fh.id),0) as danj,\n"
				+ "       nvl(GetShuil_zc(it.id,fh.id),0) as shuil,\n"
				+ "       sum(c.zongkd) as koud\n"
				+ "  from fahb fh,gongysb gys,meikxxb m,\n"
				+ "       (select fahb_id,\n"
				+ "               zhuangcdw_item_id item_id,\n"
				+ "               count(id) ches,\n"
				+ "               sum(maoz) maoz,\n"
				+ "               sum(piz) piz,\n"
				+ "               sum(zongkd) zongkd\n"
				+ "          from chepb\n"
				+ "         group by fahb_id, zhuangcdw_item_id) c,\n"
				+ "       item it,\n"
				+ "       zhuangcjsglb gl\n"
				+ " where fh.id = c.fahb_id\n"
				+ "   and it.id = c.item_id\n"
//				+ "   and it.id = jg.zhuangcdw_item_id(+)\n"
				+ "   and c.fahb_id = gl.fahb_id(+)\n"
				+ "   and c.item_id = gl.zhuangcdwb_item_id(+)\n"
				+ "   and c.fahb_id not in (select fahb_id from zhuangcjsglb)\n"
				+ "   and fh.gongysb_id = gys.id\n"
				+ "   and fh.meikxxb_id = m.id\n"
				+ getWhere()
				+ " group by rollup(fh.id,\n"
				+ "          c.item_id,\n"
				+ "          it.id,\n"
				+ "          it.mingc,\n"
				+ "          fh.fahrq,\n"
				+ "          fh.daohrq,\n"
				+ "          fh.gongysb_id,\n"
				+ "          gys.mingc,\n"
				+ "          fh.meikxxb_id,\n"
				+ "          m.mingc)\n"
				+ " having not (grouping(fh.id)=0 and grouping(m.mingc)=1)"
				+ " order by it.mingc,fh.fahrq, fh.daohrq, fh.gongysb_id,gys.mingc,fh.meikxxb_id,m.mingc";

		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("fahid").setHidden(true);
		egu.getColumn("zhuangcdw").setHeader("װ����λ");
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("meikdw").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("duns").setHeader("����(ë-Ƥ-��)");
		egu.getColumn("danj").setHeader("����");

		egu.getColumn("zhuangcdw").setWidth(100);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("fahdw").setWidth(100);
		egu.getColumn("meikdw").setWidth(100);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("duns").setWidth(80);
		egu.getColumn("danj").setWidth(80);
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("meikxxb_id").setHidden(true);
		egu.getColumn("shuil").setHidden(true);
		egu.getColumn("koud").setHidden(true);

		egu.addPaging(0); // ���÷�ҳ
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		egu.addTbarText(rb2());
		DateField df1 = new DateField();
		df1.setValue(this.getQisriqi());
		df1.Binding("QISRIQI", "");// forms[0] ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("fahrq");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");

		egu.addTbarText("��:");
		DateField df2 = new DateField();
		df2.setValue(this.getJiezriqi());
		df2.Binding("JIEZRIQI", "");// forms[0] ��htmlҳ�е�id��,���Զ�ˢ��
		df2.setId("fahrq2");
		egu.addToolbarItem(df2.getScript());
		egu.addTbarText("-");
		// /
		egu.setDefaultsortable(false);
		egu.addTbarText("װ����λ:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("FazDropDown");
		comb1.setId("faz");
		// comb1.setEditable(true);
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		// ������
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition = " and " + rb1() + ">=to_date('" + this.getQisriqi()
				+ "','yyyy-MM-dd') and " + rb1() + "<=to_date('"
				+ this.getJiezriqi() + "','yyyy-MM-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(), condition);
		etu.setWidth(60);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
//		egu
//				.addOtherScript("gongysTree.on('select',function(){document.forms[0].submit();});");

		// egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarItem("{"+ new GridButton("ˢ��",
								"function(){if(faz.getValue()==-1){\n" +
								"		Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ��װ����λ��');\n" +
								"		return;\n" +
								"} else {\n" +
								"	document.getElementById('RefurbishButton').click();}\n" +
								"}\n ")
								.getScript() + "}");
		egu.addToolbarItem("{"+ new GridButton(
								"����",
								"function(){ if(!win){ "
										+ str
										+ "}"
										+ " win.show(this);	\n"
										+ " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n"
										+ "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
										+ "	\t}	\n"
										+ " \tif(document.getElementById('TEXT_MAINJSDW_VALUE').value!=''){	\n"
										+ "		\tMainjsdw.setRawValue(document.getElementById('TEXT_MAINJSDW_VALUE').value);	\n"
										+ "	\t}	\n" + "}").getScript() + "}");
		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,
				"RbButton");
		egu.addToolbarItem("{"+new GridButton("��������","function(){ document.getElementById('DJButton').click();" +
			"}").getScript()+"}");
		egu
				.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
						+ " \tvar ches=0,duns=0;	\n"
						+ "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"
						+ " for(var i=0;i<rec.length;i++){ \n"
						+ " \tif(''!=rec[i].get('ID')){ \n"
						+ "		\tches+=eval(rec[i].get('CHES'));"
						+ "		\tduns+=eval(rec[i].get('DUNS'));"
						+ " \t}	\n"
						+ " }	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('DUNS',Round_new(duns,2));	\n"
						+ " });	");

		egu.addOtherScript("function gridDiv_save(rec){	\n"
				+ "	\tif(rec.get('ZHUANGCDW')=='�ϼ�'){	\n"
				+ "	\treturn 'continue';	\n" + "	\t}}");
		setExtGrid(egu);
		con.Close();
	}

	private String getWhere() {
		// TODO �Զ����ɷ������
		// ������
		String where = "";

		// ��������
		where += " and " + rb1() + ">= to_date('" + riq1()
				+ "', 'yyyy-mm-dd') and " + rb1() + "<= to_date('" + riq2()
				+ "', 'yyyy-mm-dd')";

		// ��Ӧ��
		if (getFazSelectValue().getId() > -1) {

			where += " and it.id=" + getFazSelectValue().getId() + "	\n";
		}
		if (!getTreeid().equals("0")) {

			where += " and (m.id = " + getTreeid() + " or gys.id = "
					+ getTreeid() + ")";
		}
		// where += TreeID();
		// ����ʱע������ʽ����Ҫ��
		if (((Visit) getPage().getVisit()).isFencb()) {

			where += " and fh.diancxxb_id in ("
					+ MainGlobal.getProperIds(this.getChangbSelectModel(), this
							.getChangb()) + ")";
		} else {
			where += " and fh.diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		return where;
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

	// /////��վ������

	public IDropDownBean getFazSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getFazSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setFazSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setFazSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getFazSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getFazSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getFazSelectModels() {
		String sql = "select id, mingc\n"
			+ "  from item\n"
			+ " where itemsortid in (select id from itemsort where bianm = 'ZHUANGCDW')\n"
			+ " order by mingc";	
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}

	// /����
	public IDropDownBean getChangbSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {

			if (getChangbSelectModel().getOptionCount() > 0) {

				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getChangbSelectModel()
								.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setChangbSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public void setChangbSelectModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getChangbSelectModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getChangbSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getChangbSelectModels() {
		String sql = "";
		JDBCcon con = new JDBCcon();

		sql = "select id,mingc from diancxxb d where d.fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + " \n "
				+ "order by xuh,mingc";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() == 0) {

			sql = "select id,mingc from diancxxb where id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		rsl.close();
		con.Close();

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql));
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
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
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// �õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(-1);
			visit.setList1(null);

			visit.setString2(""); // Treeid
			visit.setString3(""); // ����������ȡֵ
			visit.setString4(""); // ���ձ��������ȡֵ
			visit.setString5(""); // Radio����ѡ��ȡֵ
			
			visit.setString1(""); // ��Ӧ������
			visit.setDouble2(0);//��������
			visit.setDouble3(0);//����
			visit.setDouble4(0);//����˰�ӷ�
			visit.setDouble5(0);//�ӷ�˰��
			visit.setDouble6(0);//���úϼ�
			visit.setString7("");//�տλ
			visit.setString8("");//��ʼ����
			visit.setString9("");//��ֹ����
			visit.setString10(""); // ���ձ��
			visit.setString11("");//itemID;fahbID
			visit.setString12(""); // bianm
			visit.setString13(""); // Mainjsdw�������㵥λ��
			visit.setString14(""); // riq1
			visit.setString15(""); // riq2
			visit.setString16(""); // ˰��

			// ҳ�洫ֵ��ʼ����ʼ
//			visit.setLong1(0); // Diancxxb_id
//			visit.setLong2(0); // Feiylbb_id(������)
//			// visit.setLong3(0); //Meikxxb_id
//			visit.setLong4(0); // Faz_id
//			visit.setLong5(0); // Daoz_id
//			visit.setString8(""); // ���ձ��
//			visit.setString9(""); // Fahrq
			
			// ҳ�洫ֵ��ʼ������
			setFazSelectValue(null);
			setFazSelectModel(null);
			setChangbSelectValue(null);
			setChangbSelectModel(null);
			getFazSelectModel();
			getChangbSelectModel();
		}
		getSelectData();
	}

	public String getTreeid() {

		if (((Visit) getPage().getVisit()).getString2().equals("")) {

			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {

		if (!((Visit) getPage().getVisit()).getString2().equals(treeid)) {

			((Visit) getPage().getVisit()).setString2(treeid);
//			getFazSelectModels();
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

	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}

	public void setEditTableRow(int value) {

		((Visit) this.getPage().getVisit()).setInt1(value);
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

	public String getQisriqi() {
		if (((Visit) this.getPage().getVisit()).getString14() == null
				|| ((Visit) this.getPage().getVisit()).getString14().equals("")) {

			((Visit) this.getPage().getVisit()).setString14(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString14();
	}

	public void setQisriqi(String qisriqi) {

		if (((Visit) this.getPage().getVisit()).getString14() != null
				&& !((Visit) this.getPage().getVisit()).getString14().equals(
						qisriqi)) {

			((Visit) this.getPage().getVisit()).setString14(qisriqi);
		}

	}

	public String getJiezriqi() {
		if (((Visit) this.getPage().getVisit()).getString15() == null
				|| ((Visit) this.getPage().getVisit()).getString15().equals("")) {

			((Visit) this.getPage().getVisit()).setString15(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString15();
	}

	public void setJiezriqi(String jiezriqi) {

		if (((Visit) this.getPage().getVisit()).getString15() != null
				&& !((Visit) this.getPage().getVisit()).getString15().equals(
						jiezriqi)) {

			((Visit) this.getPage().getVisit()).setString15(jiezriqi);
		}

	}

	public void pageValidate(PageEvent arg0) {
		// TODO �Զ����ɷ������

	}
}
