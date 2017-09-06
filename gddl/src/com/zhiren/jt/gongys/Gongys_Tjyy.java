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

public class Gongys_Tjyy extends BasePage implements PageValidateListener {

	private final int columns_Length = 42;

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

	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}

	private boolean _SelectChick = false;

	public void SelectButton(IRequestCycle cycle) {
		_SelectChick = true;
	}

	private String Gongysmc_value;

	public String getGongysmc_value() {
		return Gongysmc_value;
	}

	public void setGongysmc_value(String gongysmc_value) {
		Gongysmc_value = gongysmc_value;
	}

	public void submit(IRequestCycle cycle) {
		if (_ReturnChick) {
			_ReturnChick = false;
			cycle.activate("Gongysxg");
		} else if (_SelectChick) {
			_SelectChick = false;
			getSelectData();
		} else if (_RbChick) {
			_RbChick = false;
			// System.out.println(Gongysmc_value);
			Visit visit = (Visit) getPage().getVisit();
			visit.setString12(Gongysmc_value);
			cycle.activate("Gongysxg");
		}
	}

	// public String[] getGongys(){
	// int a = 1000;
	// String[] gys = new String[a];
	// InterFac_dt dt = new InterFac_dt();
	// try {
	// gys = dt.getJiecxx_Sj("GYS");
	// } catch (Exception e) {
	// // TODO �Զ����� catch ��
	// e.printStackTrace();
	// }
	// return gys;
	// }

	private String[][] getDataSourceStr() {
		String[][] ds = null;
		InterFac_dt dt = new InterFac_dt();
		int rows = 0;
		try {
			String records[] = dt.getJiecxx_Sj("GYS");
			if (records != null) {

				rows += records.length;
			}
			ds = new String[rows + 2][columns_Length];
			ds[0] = new String[] { "aa", "bb", "cc", "dd", "aa", "bb", "cc",
					"dd", "aa", "bb", "bb", "cc", "dd", "cc", "aa", "bb", "cc",
					"dd", "aa", "bb", "cc", "dd", "aa", "bb", "cc", "dd", "aa",
					"bb", "cc", "dd", "aa", "bb", "bb", "cc", "dd", "cc", "aa",
					"bb", "cc", "dd", "aa", "bb" };
			ds[1] = new String[] { "aa", "bb", "cc", "dd", "aa", "bb", "cc",
					"dd", "aa", "bb", "bb", "cc", "dd", "cc", "aa", "bb", "cc",
					"dd", "aa", "bb", "cc", "dd", "aa", "bb", "cc", "dd", "aa",
					"bb", "cc", "dd", "aa", "bb", "bb", "cc", "dd", "cc", "aa",
					"bb", "cc", "dd", "aa", "bb" };
			int arr_index = 2;
			for (int i = 0; i < rows; i++) {

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
		// Visit v = (Visit) this.getPage().getVisit();
		// JDBCcon con = new JDBCcon();
		String[][] gys = this.getDataSourceStr();
		ExtGridUtil egu = new ExtGridUtil("", "", "", "gridDiv", gys);
		// System.out.print("gys!!!!!!!!!!!!!!!!!!!!!!!!!:"+gys);

		egu.setTableName("gongysb");
		egu.getColumn(0).setHeader("ID");
		egu.getColumn(1).setHeader("������");
		egu.getColumn(2).setHeader("���");
		egu.getColumn(3).setHeader("���");
		egu.getColumn(4).setHeader("ȫ��");
		egu.getColumn(5).setHeader("ƴ��");
		egu.getColumn(6).setHeader("����");
		egu.getColumn(7).setHeader("��λ��ַ");
		egu.getColumn(8).setHeader("����������");
		egu.getColumn(9).setHeader("ί�д�����");
		egu.getColumn(10).setHeader("��������");
		egu.getColumn(11).setHeader("�˺�");
		egu.getColumn(12).setHeader("�绰");
		egu.getColumn(13).setHeader("˰��");
		egu.getColumn(14).setHeader("��������");
		egu.getColumn(15).setHeader("����");
		egu.getColumn(16).setHeader("ú̿��Դ");
		egu.getColumn(17).setHeader("ú��");
		egu.getColumn(18).setHeader("��������");
		egu.getColumn(19).setHeader("��������");
		egu.getColumn(20).setHeader("��������");
		egu.getColumn(21).setHeader("��������");
		egu.getColumn(22).setHeader("��Ӧ����");
		egu.getColumn(23).setHeader("����");
		egu.getColumn(24).setHeader("���䷽ʽ");
		egu.getColumn(25).setHeader("�г��ɹ���");
		egu.getColumn(26).setHeader("�ص��ͬ");
		egu.getColumn(27).setHeader("��������");
		egu.getColumn(28).setHeader("��������");
		egu.getColumn(29).setHeader("��Ǣ��ϵ");
		egu.getColumn(30).setHeader("����");
		egu.getColumn(31).setHeader("��˾����");
		egu.getColumn(32).setHeader("�ɹ�Ӧú��");
		egu.getColumn(33).setHeader("�ɹ�Ӧú��ָ��");
		egu.getColumn(34).setHeader("ʡ��");
		egu.getColumn(35).setHeader("�Ƿ�����");
		egu.getColumn(36).setHeader("���е�ַ");
		egu.getColumn(37).setHeader("�Բ�����");
		egu.getColumn(38).setHeader("��ú����");
		egu.getColumn(39).setHeader("��������");
		egu.getColumn(40).setHeader("���б�");
		egu.getColumn(41).setHeader("��ע");

		egu.getColumn(0).setHidden(true);
		egu.getColumn(1).setHidden(true);
		egu.getColumn(8).setHidden(true);
		egu.getColumn(9).setHidden(true);
		egu.getColumn(10).setHidden(true);
		egu.getColumn(11).setHidden(true);
		egu.getColumn(12).setHidden(true);
		egu.getColumn(13).setHidden(true);
		egu.getColumn(14).setHidden(true);
		egu.getColumn(15).setHidden(true);
		egu.getColumn(16).setHidden(true);
		egu.getColumn(17).setHidden(true);
		egu.getColumn(18).setHidden(true);
		egu.getColumn(19).setHidden(true);
		egu.getColumn(20).setHidden(true);
		egu.getColumn(21).setHidden(true);
		egu.getColumn(22).setHidden(true);
		egu.getColumn(23).setHidden(true);
		egu.getColumn(24).setHidden(true);
		egu.getColumn(25).setHidden(true);
		egu.getColumn(26).setHidden(true);
		egu.getColumn(27).setHidden(true);
		egu.getColumn(28).setHidden(true);
		egu.getColumn(29).setHidden(true);
		egu.getColumn(30).setHidden(true);
		egu.getColumn(31).setHidden(true);
		egu.getColumn(32).setHidden(true);
		egu.getColumn(33).setHidden(true);
		egu.getColumn(35).setHidden(true);
		egu.getColumn(36).setHidden(true);
		egu.getColumn(37).setHidden(true);
		egu.getColumn(38).setHidden(true);
		egu.getColumn(39).setHidden(true);
		egu.getColumn(41).setHidden(true);

		egu.getColumn(0).setDataindex("ID");
		egu.getColumn(1).setDataindex("FUID");
		egu.getColumn(2).setDataindex("XUH");
		egu.getColumn(3).setDataindex("MINGC");
		egu.getColumn(4).setDataindex("QUANC");
		egu.getColumn(5).setDataindex("PINY");
		egu.getColumn(6).setDataindex("BIANM");
		egu.getColumn(7).setDataindex("DANWDZ");
		egu.getColumn(8).setDataindex("FADDBR");
		egu.getColumn(9).setDataindex("WEITDLR");
		egu.getColumn(10).setDataindex("KAIHYH");
		egu.getColumn(11).setDataindex("ZHANGH");
		egu.getColumn(12).setDataindex("DIANH");
		egu.getColumn(13).setDataindex("SHUIH");
		egu.getColumn(14).setDataindex("YOUZBM");
		egu.getColumn(15).setDataindex("CHUANZ");
		egu.getColumn(16).setDataindex("MEITLY");
		egu.getColumn(17).setDataindex("MEIZ");
		egu.getColumn(18).setDataindex("CHUBNL");
		egu.getColumn(19).setDataindex("KAICNL");
		egu.getColumn(20).setDataindex("KAICNX");
		egu.getColumn(21).setDataindex("SHENGCNL");
		egu.getColumn(22).setDataindex("GONGYNL");
		egu.getColumn(23).setDataindex("LIUX");
		egu.getColumn(24).setDataindex("YUNSFS");
		egu.getColumn(25).setDataindex("SHICCGL");
		egu.getColumn(26).setDataindex("ZHONGDHT");
		egu.getColumn(27).setDataindex("YUNSNL");
		egu.getColumn(28).setDataindex("HEZNX");
		egu.getColumn(29).setDataindex("RONGQGX");
		egu.getColumn(30).setDataindex("XINY");
		egu.getColumn(31).setDataindex("GONGSXZ");
		egu.getColumn(32).setDataindex("KEGYWFMZ");
		egu.getColumn(33).setDataindex("KEGYWFMZZB");
		egu.getColumn(34).setDataindex("SHENGFB_ID");
		egu.getColumn(35).setDataindex("SHIFSS");
		egu.getColumn(36).setDataindex("SHANGSDZ");
		egu.getColumn(37).setDataindex("ZICBFB");
		egu.getColumn(38).setDataindex("SHOUMBFB");
		egu.getColumn(39).setDataindex("QITBFB");
		egu.getColumn(40).setDataindex("CHENGSB_ID");
		egu.getColumn(41).setDataindex("BEIZ");

//		��ҳ���м��븴ѡ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		
		egu.addToolbarItem("{"+ new GridButton("����","function(){document.getElementById('ReturnButton').click();}").getScript() + "}");
		egu.addTbarText("��Ӧ������");
		TextField tf = new TextField();
		tf.setId("gongysmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("ģ����ѯ",
			"function(){var mh_value=gongysmc.getValue(); mohcx(mh_value,gridDiv_data,gridDiv_ds);}"));
		String add = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelections().length == 0){\n" + 
			"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ��ӵĹ�Ӧ�̣�');\n" + 
			"        return;\n" + 
			"    }else{\n" + 
			"        var data_str = \"\";\n" + 
			"        var recs = gridDiv_sm.getSelections();\n" + 
			"        for (var i=0;i<gridDiv_sm.getSelections().length;i++) {\n" + 
			"            data_str += \"{ID:'0',\";\n" + 
			"            if(recs[i].get('FUID')==null||recs[i].get('FUID')==\"\"){\n" + 
			"                data_str += \"FUID:'��ѡ��',\";\n" + 
			"            }else{\n" + 
			"                data_str += \"FUID:'\"+recs[i].get('FUID')+\"',\";\n" + 
			"            }\n" + 
			"            data_str += \"XUH:'\"+recs[i].get('XUH')+\"',MINGC:'\"+recs[i].get('MINGC')+\"',QUANC:'\"+recs[i].get('QUANC')+\"',PINY:'\"+recs[i].get('PINY')+\"',BIANM:'\"+recs[i].get('BIANM')+\n" + 
			"            \"',DANWDZ:'\"+recs[i].get('DANWDZ')+\"',FADDBR:'\"+recs[i].get('FADDBR')+\"',WEITDLR:'\"+recs[i].get('WEITDLR')+\"',KAIHYH:'\"+recs[i].get('KAIHYH')+\"',ZHANGH:'\"+recs[i].get('ZHANGH')+\n" + 
			"            \"',DIANH:'\"+recs[i].get('DIANH')+\"',SHUIH:'\"+recs[i].get('SHUIH')+\"',YOUZBM:'\"+recs[i].get('YOUZBM')+\"',CHUANZ:'\"+recs[i].get('CHUANZ')+\"',MEITLY:'\"+recs[i].get('MEITLY')+\n" + 
			"            \"',MEIZ:'\"+recs[i].get('MEIZ')+\"',CHUBNL:'\"+recs[i].get('CHUBNL')+\"',KAICNL:'\"+recs[i].get('KAICNL')+\"',KAICNX:'\"+recs[i].get('KAICNX')+\"',SHENGCNL:'\"+recs[i].get('SHENGCNL')+\n" + 
			"            \"',GONGYNL:'\"+recs[i].get('GONGYNL')+\"',LIUX:'\"+recs[i].get('LIUX')+\"',YUNSFS:'\"+recs[i].get('YUNSFS')+\"',SHICCGL:'\"+recs[i].get('SHICCGL')+\"',ZHONGDHT:'\"+recs[i].get('ZHONGDHT')+\n" + 
			"            \"',YUNSNL:'\"+recs[i].get('YUNSNL')+\"',HEZNX:'\"+recs[i].get('HEZNX')+\"',RONGQGX:'\"+recs[i].get('RONGQGX')+\"',XINY:'\"+recs[i].get('XINY')+\"',GONGSXZ:'\"+recs[i].get('GONGSXZ')+\n" + 
			"            \"',KEGYWFMZ:'\"+recs[i].get('KEGYWFMZ')+\"',KEGYWFMZZB:'\"+recs[i].get('KEGYWFMZZB')+\"',SHENGFB_ID:'\"+recs[i].get('SHENGFB_ID')+\"',SHIFSS:'\"+recs[i].get('SHIFSS')+\n" + 
			"            \"',SHANGSDZ:'\"+recs[i].get('SHANGSDZ')+\"',ZICBFB:'\"+recs[i].get('ZICBFB')+\"',SHOUMBFB:'\"+recs[i].get('SHOUMBFB')+\"',QITBFB:'\"+recs[i].get('QITBFB')+\n" + 
			"            \"',CHENGSB_ID:'\"+recs[i].get('CHENGSB_ID')+\"',SHANGJGSBM:'\"+recs[i].get('BIANM')+\"',ZHUANGT:'',BEIZ:'\"+recs[i].get('BEIZ')+\"'}&\";\n" + 
			"        }\n" + 
			"        data_str=data_str.substring(0,data_str.lastIndexOf('&'));\n" + 
//			"        alert(data_str);\n" + 
			"        document.all.Gongysmc_value.value=data_str;\n" + 
			"        document.all.MsgAdd.value='toAdd';\n" + 
			"        document.getElementById('RbButton').click();\n" + 
			"    }\n" + 
			"}";

//			"function(){ \n"
//				+ "if(gridDiv_sm.getSelected()== null){ \n"
//				+ "	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ���ܹ�Ӧ��');"
//				+ "	return; \n"
//				+ "}else{ var rec=gridDiv_sm.getSelected(); \n"
//				+ "var str=\"{ID:'0\"+\"',\";"
//				+ "if(rec.get('FUID')==null||rec.get('FUID')==\"\"){ \n" // �ж�FUIDΪ�վʹ�����ѡ��
//				+ "    str+=\"FUID:'��ѡ��',\"; \n"
//				+ "}else{ \n"
//				+ "	   str+=\"FUID:'\"+rec.get('FUID')+\"',\"; \n"
//				+ "} \n"
//				+ "str+=\"XUH:'\"+rec.get('XUH')+\"',MINGC:'\"+rec.get('MINGC')+\"',QUANC:'\"+rec.get('QUANC')+\"',PINY:'\"+rec.get('PINY')+\"',BIANM:'\"+rec.get('BIANM')+\"',DANWDZ:'\"+rec.get('DANWDZ')+\"',FADDBR:'\"+rec.get('FADDBR')+\"',WEITDLR:'\"+rec.get('WEITDLR')+\"',KAIHYH:'\"+rec.get('KAIHYH')+\"',ZHANGH:'\"+rec.get('ZHANGH')+\"',DIANH:'\"+rec.get('DIANH')+\"',SHUIH:'\"+rec.get('SHUIH')+\"',YOUZBM:'\"+rec.get('YOUZBM')+\"',CHUANZ:'\"+rec.get('CHUANZ')+\"',MEITLY:'\"+rec.get('MEITLY')+\"',MEIZ:'\"+rec.get('MEIZ')+\"',CHUBNL:'\"+rec.get('CHUBNL')+\"',KAICNL:'\"+rec.get('KAICNL')+\"',KAICNX:'\"+rec.get('KAICNX')+\"',SHENGCNL:'\"+rec.get('SHENGCNL')+\"',GONGYNL:'\"+rec.get('GONGYNL')+\"',LIUX:'\"+rec.get('LIUX')+\"',YUNSFS:'\"+rec.get('YUNSFS')+\"',SHICCGL:'\"+rec.get('SHICCGL')+\"',ZHONGDHT:'\"+rec.get('ZHONGDHT')+\"',YUNSNL:'\"+rec.get('YUNSNL')+\"',HEZNX:'\"+rec.get('HEZNX')+\"',RONGQGX:'\"+rec.get('RONGQGX')+\"',XINY:'\"+rec.get('XINY')+\"',GONGSXZ:'\"+rec.get('GONGSXZ')+\"',KEGYWFMZ:'\"+rec.get('KEGYWFMZ')+\"',KEGYWFMZZB:'\"+rec.get('KEGYWFMZZB')+\"',SHENGFB_ID:'\"+rec.get('SHENGFB_ID')+\"',SHIFSS:'\"+rec.get('SHIFSS')+\"',SHANGSDZ:'\"+rec.get('SHANGSDZ')+\"',ZICBFB:'\"+rec.get('ZICBFB')+\"',SHOUMBFB:'\"+rec.get('SHOUMBFB')+\"',QITBFB:'\"+rec.get('QITBFB')+\"',CHENGSB_ID:'\"+rec.get('CHENGSB_ID')+\"',SHANGJGSBM:'\"+rec.get('BIANM')+\"',BEIZ:'\"+rec.get('BEIZ')+\"'}\"; \n"
//				+ "document.all.Gongysmc_value.value=str; \n"
//				+ "document.all.MsgAdd.value='toAdd'; \n"
//				// + " alert('123'); \n"
//				+ "document.getElementById('RbButton').click(); \n" + "}} \n";
		egu.addTbarBtn(new GridButton("�����ѡ��Ӧ��", add));

		setExtGrid(egu);
	}

	// ģ����ѯ
	public String getGongysmc() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setGongysmc(String value) {
		((Visit) getPage().getVisit()).setString1(value);
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
			Gongysmc_value = "";
			getSelectData();
		}
	}
}
