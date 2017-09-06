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
 * ���ߣ����
 * ʱ�䣺2012-03-12
 * �������������ڽӿڽ��ն������չʾ�ֶβ�ƥ����ɲ���������ȡú��������쳣���⡣
 */
public class Meikdq_gdTj extends BasePage implements PageValidateListener {

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

//	���ذ�ť
	private boolean _ReturnChick = false;
	
    public void ReturnButton(IRequestCycle cycle) {
    	_ReturnChick = true;
    }
    
//  �����ѡú�������ť
    private boolean _RbChick = false;
    
    public void RbButton(IRequestCycle cycle) {
    	_RbChick = true;
    }
    
//  ģ����ѯ��ť
    private boolean _SelectChick = false;
    
    public void SelectButton(IRequestCycle cycle) {
    	_SelectChick = true;
    }
	
//  ����ҳ����ѡ���ú�����������
    private String Gongysmc_value;
	
	public String getGongysmc_value() {
		return Gongysmc_value;
	}

	public void setGongysmc_value(String gongysmc_value) {
		Gongysmc_value = gongysmc_value; 
	}
		
	public void submit(IRequestCycle cycle) {
		
		if(_ReturnChick){
			_ReturnChick = false;
			cycle.activate("Meikdq_gd");
		}else if(_SelectChick){
			_SelectChick = false;
			getSelectData();
		}else if(_RbChick){
			_RbChick = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString12(Gongysmc_value);
			cycle.activate("Meikdq_gd");
		}
	}
	
	private String[][] getDataSourceStr(){
		
		String[][] ds=null;
		InterFac_dt dt=new InterFac_dt();
		int rows=0;
		try {
			String records[]=dt.getJiecxx_Sj("MKDQ");
			
			if(records!=null){
				rows+=records.length;
			}
			int columns_Length = 7;
			ds=new String[rows+2][columns_Length];
			ds[0]=new String[]{"aa","bb","cc","dd","aa","bb","cc"};
			ds[1]=new String[]{"aa","bb","cc","dd","aa","bb","cc"};
			int arr_index=2;
			for(int i=0;i<rows;i++){
				
				String[] record=records[i].split(",");
				int k=0;
				String[] rec=new String[columns_Length];
				for(int j=0;j<record.length;j++){
					rec[k++]=record[j].replaceAll("'", "");
				}
				ds[arr_index++]=rec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}

	public void getSelectData() {
		
		String[][] gys = this.getDataSourceStr();
		ExtGridUtil egu = new ExtGridUtil("","","","gridDiv",gys);
		
		egu.setTableName("meikdqb");
		egu.getColumn(0).setHeader("ID");
		egu.getColumn(1).setHeader("���");
		egu.getColumn(2).setHeader("���");
		egu.getColumn(3).setHeader("ȫ��");
		egu.getColumn(4).setHeader("����");
		egu.getColumn(5).setHeader("״̬");
		egu.getColumn(6).setHeader("ʡ��");
		
		egu.getColumn(0).setHidden(true);
		egu.getColumn(5).setHidden(true);
		
		egu.getColumn(0).setDataindex("ID");
		egu.getColumn(1).setDataindex("XUH");
		egu.getColumn(2).setDataindex("MINGC");
		egu.getColumn(3).setDataindex("QUANC");
		egu.getColumn(4).setDataindex("BIANM");
		egu.getColumn(5).setDataindex("ZHUANGT");
		egu.getColumn(6).setDataindex("SHENGFB_ID");
		
//		��ҳ���м��븴ѡ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));
		
		egu.addToolbarItem("{"+new GridButton("����","function(){document.getElementById('ReturnButton').click();}").getScript()+"}");
		egu.addTbarText("ú���������");
		TextField tf = new TextField();
		tf.setId("gongysmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("ģ����ѯ", 
				"function(){" +
				"	var mh_value=gongysmc.getValue(); " +
				"	mohcx(mh_value,gridDiv_data,gridDiv_ds);" +
				"}"));
		String add = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelections().length == 0){\n" + 
			"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ��ӵ�ú�������');\n" + 
			"        return;\n" + 
			"    }else{\n" + 
			"        var data_str = \"\";\n" + 
			"        var recs = gridDiv_sm.getSelections();\n" + 
			"        for (var i=0;i<gridDiv_sm.getSelections().length;i++) {\n" + 
			"        var data_str = \"\";\n" + 
			"        var recs = gridDiv_sm.getSelections();\n" + 
			"        for (var i=0;i<gridDiv_sm.getSelections().length;i++) {\n" + 
			"          data_str += \"{ID:'0',XUH:'\"+recs[i].get('XUH')+\"',MINGC:'\"+recs[i].get('MINGC')+\"',QUANC:'\"+recs[i].get('QUANC')+\"',BIANM:'\"+recs[i].get('BIANM')+\"',SHENGFB_ID:'\"+recs[i].get('SHENGFB_ID')+\"'}&\";\n" + 
			"        }\n" + 
			"        }\n" + 
			"        data_str=data_str.substring(0,data_str.lastIndexOf('&'));\n" + 
//			"        alert(data_str);\n" + 
			"        document.all.Gongysmc_value.value=data_str;\n" + 
			"        document.all.MsgAdd.value='toAdd';\n" + 
			"        document.getElementById('RbButton').click();\n" + 
			"    }\n" + 
			"}";
		egu.addTbarBtn(new GridButton("�����ѡú�����", add));
		
		setExtGrid(egu);
	}
	
    public String getGongysmc(){
    	return ((Visit) getPage().getVisit()).getString1();
    }
    public void setGongysmc(String value){
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