package com.zhiren.jt.gongys;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterFac_dt;

/*
 * ʱ�䣺2010-01-05
 * ���ߣ�������
 * ������
 *      1������"ú�����"�ֶΣ�
 *      2���޸�"�����ѡú��λ"��ť��Js
 */

public class Meikxx_Tjyy extends BasePage implements PageValidateListener {
	private final int columns_Length = 14;

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

	private String DataSource;

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}

	private String Parameters;//��¼ID

	public String getParameters() {
		return Parameters;
	}

	public void setParameters(String value) {
		Parameters = value;
	}

//	��������ҳ��ѡ��ļ�¼
	private String Meikdwmc_value;

	public String getMeikdwmc_value() {
		return Meikdwmc_value;
	}

	public void setMeikdwmc_value(String meikdwmc_value) {
		Meikdwmc_value = meikdwmc_value;
	}

	private boolean _FanHClick = false;

	public void FanHBt(IRequestCycle cycle) {
		_FanHClick = true;
	}

	private boolean _MoHCHClick = false;

	public void MoHCHBt(IRequestCycle cycle) {
		_MoHCHClick = true;
	}

	private boolean _TianjClick = false;

	public void TianjBt(IRequestCycle cycle) {
		_TianjClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_FanHClick) { //���ذ�ť����
			_FanHClick = false;
			cycle.activate("Meikxx");
		}
		if (_MoHCHClick) { //ģ����ѯ��ť����
			_MoHCHClick = false;

		}
		if (_TianjClick) { //�����ѡú��ť����
			_TianjClick = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString13(Meikdwmc_value);
			cycle.activate("Meikxx");
		}

	}

	private String[][] getDataSourceStr() {

		String[][] ds = null;
		InterFac_dt dt = new InterFac_dt();

		try {
			String records[] = dt.getJiecxx_Sj("MK");
			ds = new String[records.length + 2][columns_Length];
			ds[0] = new String[] { "aa", "bb", "cc", "dd", "aa", "bb", "cc",
					"dd", "aa", "bb", "bb", "cc", "dd", "aa"};
			ds[1] = new String[] { "aa", "bb", "cc", "dd", "aa", "bb", "cc",
					"dd", "aa", "bb", "bb", "cc", "dd", "aa"};
			int arr_index = 2;
			for (int i = 0; i < records.length; i++) {

				String[] record = records[i].split(",");
				int k = 0;
				String[] rec = new String[columns_Length];
				for (int j = 0; j < record.length; j++) {
					rec[k++] = record[j].replaceAll("'", "");
				}
				ds[arr_index++] = rec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}

	public void getSelectData() {

		String[][] ds = this.getDataSourceStr();
		ExtGridUtil egu = new ExtGridUtil("", "", "", "gridDiv", ds);

		egu.setTableName("meikxxb");
		egu.getColumn(0).setHeader("ID");
		egu.getColumn(1).setHeader("���");
		egu.getColumn(2).setHeader("ú�����");
		egu.getColumn(3).setHeader("����");
		egu.getColumn(4).setHeader("����");
		egu.getColumn(5).setHeader("ȫ��");
		egu.getColumn(6).setHeader("ƴ��");
		egu.getColumn(7).setHeader("ʡ��");
		egu.getColumn(8).setHeader("���");
		egu.getColumn(9).setHeader("�ƻ��ھ�");
		egu.getColumn(10).setHeader("����");
		egu.getColumn(11).setHeader("��λ��ַ");
		egu.getColumn(12).setHeader("���б�");
		egu.getColumn(13).setHeader("��ע");

		egu.getColumn(0).setHidden(true);
		egu.getColumn(12).setHidden(true);
		egu.getColumn(13).setHidden(true);

		egu.getColumn(0).setDataindex("ID");
		egu.getColumn(1).setDataindex("XUH");
		egu.getColumn(1).setWidth(50);
		egu.getColumn(2).setDataindex("MEIKDQ");
		egu.getColumn(3).setDataindex("BIANM");
		egu.getColumn(4).setDataindex("MINGC");
		egu.getColumn(5).setDataindex("QUANC");
		egu.getColumn(6).setDataindex("PINY");
		egu.getColumn(7).setDataindex("SHENGFB_ID");
		egu.getColumn(8).setDataindex("LEIB");
		egu.getColumn(9).setDataindex("JIHKJB_ID");
		egu.getColumn(10).setDataindex("LEIX");
		egu.getColumn(11).setDataindex("DANWDZ");
		egu.getColumn(12).setDataindex("CHENGSB_ID");
		egu.getColumn(13).setDataindex("BEIZ");
		
//		��ҳ���м��븴ѡ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));

		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
		egu.addTbarBtn(new GridButton("����","function(){document.all.FanHBt.click();}"));
		egu.addTbarText("-");
		egu.addTbarText("ú��λ����:");
		TextField tf = new TextField();
		tf.setId("Meikdwmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("ģ����ѯ","function(){ var mh_value=Meikdwmc.getValue(); mohcx(mh_value,gridDiv_data,gridDiv_ds);} "));
		egu.addTbarText("-");
//		egu.addTbarBtn(new GridButton(
//						"�����ѡú��λ",
//						"function(){" +
//						"	if(gridDiv_sm.getSelected()== null){" +
//						"		Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��ú��λ');" +
//						"	}else{ " +
//						"		var rec=gridDiv_sm.getSelected(); " +
//						"		var str=\"{ID:'0\"+\"',XUH:'\"+rec.get('XUH')+\"',MEIKDQ_ID:'\"+\"',BIANM:'\"+rec.get('BIANM')+\"',MINGC:'\"+rec.get('MINGC')+" +
//						"			\"',QUANC:'\"+rec.get('QUANC')+\"',PINY:'\"+rec.get('PINY')+\"',SHENGFB_ID:'\"+rec.get('SHENGFB_ID')+\"',LEIB:'\"+" +
//						"			rec.get('LEIB')+\"',LEIX:'\"+rec.get('LEIX')+\"',JIHKJB_ID:'\"+rec.get('JIHKJB_ID')+\"',DANWDZ:'\"+rec.get('DANWDZ')+" +
//						"			\"',BEIZ:'\"+rec.get('BEIZ')+\"',SHANGJGSBM:'\"+rec.get('BIANM')+\"',CHENGSB_ID:'\"+rec.get('CHENGSB_ID')+\"'}\";" +
//						"		document.all.Meikdwmc_value.value=str; " +
//						"		document.all.MsgAdd.value='toAdd';" +
//						"		document.all.TianjBt.click();" +
//						"	}" +
//						"}"));
//		egu.addOtherScript("gridDiv_sm.singleSelect=true;\n");
		egu.addTbarBtn(new GridButton(
				"�����ѡú��λ",
				"function(){\n" +
				"    if(gridDiv_sm.getSelections().length == 0){\n" + 
				"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ��ӵ�ú��λ��');\n" + 
				"    }else{\n" + 
				"        var data_str = \"\";\n" + 
				"        var recs = gridDiv_sm.getSelections();\n" + 
				"        for (var i=0;i<gridDiv_sm.getSelections().length;i++) {\n" + 
				"            data_str += \"{ID:'0',XUH:'\"+recs[i].get('XUH')+\"',MEIKDQ_ID:'\"+recs[i].get('MEIKDQ')+\"',BIANM:'\"+recs[i].get('BIANM')+\"',MINGC:'\"+recs[i].get('MINGC')+\"',QUANC:'\"+recs[i].get('QUANC')+\"',PINY:'\"+recs[i].get('PINY')+\n" + 
				"            \"',MEIKDCGLB_ID:'',SHENGFB_ID:'\"+recs[i].get('SHENGFB_ID')+\"',LEIB:'\"+recs[i].get('LEIB')+\"',LEIX:'\"+recs[i].get('LEIX')+\"',JIHKJB_ID:'\"+recs[i].get('JIHKJB_ID')+\"',DANWDZ:'\"+recs[i].get('DANWDZ')+\n" + 
				"            \"',BEIZ:'\"+recs[i].get('BEIZ')+\"',SHANGJGSBM:'\"+recs[i].get('BIANM')+\"',CHENGSB_ID:'\"+recs[i].get('CHENGSB_ID')+\"'}&\";\n" + 
				"        }\n" + 
				"        data_str=data_str.substring(0,data_str.lastIndexOf('&'));\n" + 
//				"        alert(data_str);\n" + 
				"        document.all.Meikdwmc_value.value=data_str;\n" + 
				"        document.all.MsgAdd.value='toAdd';\n" + 
				"        document.all.TianjBt.click();\n" + 
				"    }\n" + 
				"}"));
		setExtGrid(egu);
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
			//visit.setString9(null);
			Meikdwmc_value = "";
			getSelectData();
		}
		getSelectData();
	}

}
