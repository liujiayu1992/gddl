package com.zhiren.shihs;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
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

public class Shihcpxg extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

//	 ������
	private String riqi;

	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
			this.riqi = riqi;
	}

	private String riq2;

	public String getRiq2() {
		return riq2;
	}

	public void setRiq2(String riq2) {
			this.riq2 = riq2;
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

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String count = " ";
		StringBuffer sb = new StringBuffer();
		sb.append("select count(cp.id) cs from shihcpb cp,shihgysb gys,shihpzb pz,chebb cb,yunsdwb dw ")
		.append("where gys.id = cp.gongysb_id ").append("and pz.id = cp.shihpzb_id ")
		.append("and cb.id = cp.chebb_id ").append("and dw.id = cp.yunsdwb_id ")
		.append("and cp.daohrq>=to_date('").append(getRiqi()).append("','yyyy-mm-dd') ")
		.append("and cp.daohrq<to_date('").append(getRiq2()).append("','yyyy-mm-dd')+1 ");
		ResultSetList rslc = con.getResultSetList(sb.toString());
		if(rslc.next()){
			if(rslc.getInt("cs")>500){
				count+="and rownum <= 500 ";
				setMsg("����ѯ�ļ�¼�����࣬����ɳ��������ٶȻ�����<br>ϵͳ�Ѿ��Զ�Ϊ����ȡ��ǰ500����¼��");
			}
		}
		
		ResultSetList rsl = con
				.getResultSetList("select cp.id,cp.diancxxb_id,cp.xuh,cp.fahrq,cp.daohrq, " 
						+ " gys.mingc as gongysb_id,pz.mingc as shihpzb_id,"
						+ " cp.cheph,cp.piaojh,cp.yuanmz,cp.maoz,cp.piz,cp.biaoz,cp.yingk,cp.yuns,cp.koud,cp.ches,cp.jianjfs, "
						+ " cp.chebb_id,cp.yuanghdw,dw.mingc as yunsdwb_id,"
						+ " cp.qingcsj,cp.qingchh,cp.qingcjjy,cp.zhongcsj,cp.zhongchh,cp.zhongcjjy," 
//						+ " cp.hedbz,cp.lursj,"
						+ " cp.lury,cp.beiz "
//						+ " cp.shihhtb_id,cp.shihcyb_id,cp.shihjsb_id,cp.shenhr,cp.shenhsj,cp.shenhzt "
						+ " from shihcpb cp,shihgysb gys,shihpzb pz,chebb cb,yunsdwb dw "
						+ " where gys.id = cp.gongysb_id and pz.id = cp.shihpzb_id and cb.id = cp.chebb_id and dw.id = cp.yunsdwb_id "
						+ " and cp.daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and cp.daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
						+ count
						+ " order by cp.daohrq,cp.xuh");
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
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(70);
		egu.getColumn("shihpzb_id").setHeader("Ʒ��");
		egu.getColumn("shihpzb_id").setWidth(70);
		egu.getColumn("cheph").setHeader("��Ƥ��");
		egu.getColumn("cheph").setWidth(70);
		egu.getColumn("piaojh").setHeader("Ʊ�ݺ�");
		egu.getColumn("piaojh").setHidden(true);
		egu.getColumn("piaojh").setWidth(70);
		egu.getColumn("yuanmz").setHeader("ԭë��");
		egu.getColumn("yuanmz").setWidth(60);
		egu.getColumn("yuanmz").setHidden(true);
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingk").setHeader("ӯ��");
		egu.getColumn("yingk").setWidth(50);
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("yuns").setWidth(50);
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").setHidden(true);
		egu.getColumn("jianjfs").setHeader("��﷽ʽ");
		egu.getColumn("jianjfs").setWidth(65);
		egu.getColumn("chebb_id").setHeader("����");
		egu.getColumn("chebb_id").setWidth(50);
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("yuanghdw").setHeader("ԭ������λ");
		egu.getColumn("yuanghdw").setWidth(70);
		egu.getColumn("yuanghdw").setHidden(true);
		egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
		egu.getColumn("yunsdwb_id").setWidth(70);
		
		egu.getColumn("qingcsj").setHeader("�ᳵʱ��");
		egu.getColumn("qingcsj").setWidth(70);
		egu.getColumn("qingcsj").setHidden(true);
		egu.getColumn("qingchh").setHeader("�ᳵ���");
		egu.getColumn("qingchh").setWidth(70);
		egu.getColumn("qingchh").setHidden(true);
		egu.getColumn("qingcjjy").setHeader("�ᳵ���Ա");
		egu.getColumn("qingcjjy").setWidth(70);
		egu.getColumn("qingcjjy").setHidden(true);
		
		egu.getColumn("zhongcsj").setHeader("�س�ʱ��");
		egu.getColumn("zhongcsj").setWidth(70);
		egu.getColumn("zhongcsj").setHidden(true);
		egu.getColumn("zhongchh").setHeader("�س����");
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("zhongchh").setHidden(true);
		egu.getColumn("zhongcjjy").setHeader("�س����Ա");
		egu.getColumn("zhongcjjy").setWidth(70);
		egu.getColumn("zhongcjjy").setHidden(true);
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
		
//		�������ڲ�ѯ
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
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
		ComboBox c3 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c3);
		c3.setEditable(true);
		String cbSql = "select id,mingc from chebb order by mingc";
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cbSql));
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
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		StringBuffer sb1 = new StringBuffer();
		sb1.append("gridDiv_grid.on('afteredit', function(e){\n" +
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
		egu.addOtherScript(sb1.toString());
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
			setRiqi(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
	}
}
