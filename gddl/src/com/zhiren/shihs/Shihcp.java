package com.zhiren.shihs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihcp extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		setMsg("");
		super.initialize();
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || "".equals(riqTiaoj)) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		String sql =
			"SELECT CP.ID,\n" +
			"       CP.DIANCXXB_ID,\n" + 
			"       CP.XUH,\n" + 
			"       CP.FAHRQ,\n" + 
			"       CP.DAOHRQ,\n" + 
			"       GYS.MINGC AS GONGYSB_ID,\n" + 
			"       PZ.MINGC AS SHIHPZB_ID,\n" + 
			"       CP.CHEPH,\n" + 
			"       CP.PIAOJH,\n" + 
			"       CP.YUANMZ,\n" + 
			"       CP.MAOZ,\n" + 
			"       CP.PIZ,\n" + 
			"       CP.BIAOZ,\n" + 
			"       CP.YINGK,\n" + 
			"       CP.YUNS,\n" + 
			"       CP.KOUD,\n" + 
			"       CP.CHES,\n" + 
			"       CP.JIANJFS,\n" + 
			"       CP.CHEBB_ID,\n" + 
			"       CP.YUANGHDW,\n" + 
			"       DW.MINGC AS YUNSDWB_ID,\n" + 
			"       CP.QINGCSJ,\n" + 
			"       CP.QINGCHH,\n" + 
			"       CP.QINGCJJY,\n" + 
			"       CP.ZHONGCSJ,\n" + 
			"       CP.ZHONGCHH,\n" + 
			"       CP.ZHONGCJJY,\n" + 
			"       CP.LURY,\n" + 
			"       CP.BEIZ\n" + 
			"  FROM SHIHCPB  CP,\n" + 
			"       SHIHGYSB GYS,\n" + 
			"       SHIHPZB  PZ,\n" + 
			"       YUNSDWB  DW,\n" + 
			"       SHIHCYB  CY,\n" + 
			"       SHIHZLB  ZL\n" + 
			" WHERE GYS.ID(+) = CP.GONGYSB_ID\n" + 
			"   AND PZ.ID(+) = CP.SHIHPZB_ID\n" +  
			"   AND DW.ID(+) = CP.YUNSDWB_ID\n" + 
			"   AND CP.SHIHCYB_ID = CY.ID\n" + 
			"   AND CY.SHIHZLB_ID = ZL.ID(+)\n" + 
			"   AND NVL(ZL.SHENHZT, 0) = 0\n" + 
			"   AND CP.FAHRQ = TO_DATE('" + riqTiaoj + "', 'yyyy-mm-dd')\n" + 
			" ORDER BY CP.XUH";

			
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("shihcpb");
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(v.getDiancxxb_id()));
		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("fahrq").setDefaultValue(riqTiaoj);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").setDefaultValue(riqTiaoj);
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(70);
		egu.getColumn("shihpzb_id").setHeader("Ʒ��");
		egu.getColumn("shihpzb_id").setWidth(70);
		egu.getColumn("cheph").setHeader("��Ƥ��");
		egu.getColumn("cheph").setWidth(70);
		egu.getColumn("piaojh").setHeader("Ʊ�ݺ�");
		egu.getColumn("piaojh").setWidth(70);
		egu.getColumn("piaojh").setHidden(true);
		egu.getColumn("yuanmz").setHeader("ԭë��");
		egu.getColumn("yuanmz").setDefaultValue("0");
		egu.getColumn("yuanmz").setWidth(60);
		egu.getColumn("yuanmz").setHidden(true);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("maoz").setWidth(60);	
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setDefaultValue("0");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingk").setHeader("ӯ��");
		egu.getColumn("yingk").setDefaultValue("0");
		egu.getColumn("yingk").setWidth(50);
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("yuns").setDefaultValue("0");
		egu.getColumn("yuns").setWidth(50);
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setDefaultValue("0");
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setDefaultValue("0");
		egu.getColumn("ches").setHidden(true);
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("jianjfs").setHeader("��﷽ʽ");
		egu.getColumn("jianjfs").setDefaultValue("����");
		egu.getColumn("jianjfs").setWidth(65);
		egu.getColumn("chebb_id").setHeader("����");
		egu.getColumn("chebb_id").setDefaultValue("3");
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chebb_id").setWidth(50);
		egu.getColumn("yuanghdw").setHeader("ԭ������λ");
		egu.getColumn("yuanghdw").setHidden(true);
		egu.getColumn("yuanghdw").setWidth(70);
		egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
		egu.getColumn("yunsdwb_id").setDefaultValue("����");
		egu.getColumn("yunsdwb_id").setWidth(70);
		
		egu.getColumn("qingcsj").setHeader("�ᳵʱ��");
		egu.getColumn("qingcsj").setDefaultValue(riqTiaoj);
		egu.getColumn("qingcsj").setHidden(true);
		egu.getColumn("qingcsj").setWidth(70);
		egu.getColumn("qingchh").setHeader("�ᳵ���");
		egu.getColumn("qingchh").setHidden(true);
		egu.getColumn("qingchh").setWidth(70);
		egu.getColumn("qingcjjy").setHeader("�ᳵ���Ա");
		egu.getColumn("qingcjjy").setWidth(70);
		egu.getColumn("qingcjjy").setHidden(true);
		
		egu.getColumn("zhongcsj").setHeader("�س�ʱ��");
		egu.getColumn("zhongcsj").setDefaultValue(riqTiaoj);
		egu.getColumn("zhongcsj").setHidden(true);
		egu.getColumn("zhongcsj").setWidth(70);
		egu.getColumn("zhongchh").setHeader("�س����");
		egu.getColumn("zhongchh").setHidden(true);
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("zhongcjjy").setHeader("�س����Ա");
		egu.getColumn("zhongcjjy").setHidden(true);
		egu.getColumn("zhongcjjy").setWidth(70);
//		egu.getColumn("hedbz").setHeader("�˶Ա�־");
//		egu.getColumn("hedbz").setWidth(65);
//		egu.getColumn("lursj").setHeader("¼��ʱ��");
//		egu.getColumn("lursj").setWidth(70);
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setWidth(50);
		egu.getColumn("lury").setDefaultValue(v.getRenymc());
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(65);
//		egu.getColumn("shihhtb_id").setHeader("SHIHHTB_ID");
//		egu.getColumn("shihhtb_id").setHidden(true);
//		egu.getColumn("shihhtb_id").setWidth(70);
//		egu.getColumn("shihcyb_id").setHeader("SHIHCYB_ID");
//		egu.getColumn("shihcyb_id").setHidden(true);
//		egu.getColumn("shihcyb_id").setWidth(70);
//		egu.getColumn("shihjsb_id").setHeader("SHIHJSB_ID");
//		egu.getColumn("shihjsb_id").setHidden(true);
//		egu.getColumn("shihjsb_id").setWidth(70);
//		egu.getColumn("shenhr").setHeader("�����");
//		egu.getColumn("shenhr").setWidth(50);
//		egu.getColumn("shenhsj").setHeader("���ʱ��");
//		egu.getColumn("shenhsj").setWidth(70);
//		egu.getColumn("shenhzt").setHeader("���״̬");
//		egu.getColumn("shenhzt").setWidth(65);
		
//		 ���ù�Ӧ��������
		ComboBox c1 = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c1);
		c1.setEditable(true);
		String gysSql = "select id,mingc from shihgysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gysSql));
//		 ����Ʒ��������
		ComboBox c2 = new ComboBox();
		egu.getColumn("shihpzb_id").setEditor(c2);
		c2.setEditable(true);
		String pzSql = "select id,mingc from shihpzb order by mingc";
		egu.getColumn("shihpzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pzSql));
//		 ���ó���������
//		ComboBox c3 = new ComboBox();
//		egu.getColumn("chebb_id").setEditor(c3);
//		c3.setEditable(true);
//		String cbSql = "select id,mingc from chebb order by mingc";
//		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
//				new IDropDownModel(cbSql));
//		 �������䵥λ������
		ComboBox c4 = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(c4);
		c4.setEditable(true);
		String ysdwSql = "select id,mingc from yunsdwb order by mingc";
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ysdwSql));
////		 �������״̬������
//		List ls = new ArrayList();
//		ls.add(new IDropDownBean(1, "�����"));
//		ls.add(new IDropDownBean(2, "δ���"));
//		ComboBox c5 = new ComboBox();
//		egu.getColumn("shenhzt").setEditor(c5);
//		c5.setEditable(true);
//		egu.getColumn("shenhzt").setComboEditor(egu.gridId,
//				new IDropDownModel(ls));
//		egu.getColumn("shenhzt").setDefaultValue("δ���");
//		egu.getColumn("shenhzt").returnId = true;
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		//ˢ�°�ť
		GridButton rbtn = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(rbtn);
		
		egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		//���水ť
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");	
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit', function(e){\n" +
				"	if (e.field=='MAOZ'||e.field=='PIZ') {\n" +
				"		var MAOZ=eval(gridDiv_ds.getAt(e.row).get('MAOZ')||0);\n" +
				"		var PIZ=eval(gridDiv_ds.getAt(e.row).get('PIZ')||0);\n" +
				"		var BIAOZ=Round_new(MAOZ-PIZ, 2);\n" +
				"		if (BIAOZ<0) {\n" +
				"			Ext.MessageBox.alert('��ʾ��Ϣ','ë��С��Ƥ�أ�');" +
				"			BIAOZ=0;\n" +
				"		}\n" +
				"		gridDiv_ds.getAt(e.row).set('BIAOZ', BIAOZ);\n" +
				"		gridDiv_ds.getAt(e.row).set('YUANMZ', MAOZ);\n" +
				"	}\n" +
				"});\n");
		egu.addOtherScript(sb.toString());
		
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
			visit.setString5(null);			
		}
		getSelectData();
	}
}
