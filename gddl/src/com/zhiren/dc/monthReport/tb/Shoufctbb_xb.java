package com.zhiren.dc.monthReport.tb;

import org.apache.tapestry.html.BasePage;


import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;


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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;

import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public abstract class Shoufctbb_xb extends BasePage {//	����ҳ����ʾ��Ϣ������
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

	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
//	 ���������
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
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

	// �·�������
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
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} 
			else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
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
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		
		String sql = 
			"select id,diancxxb_id," +
			"decode(shoufcyllbb_id,0,'ú',1,'��')as	shoufcyllbb_id," +
			"		riq,\n" +
			"       qickc_shul,\n" + 
			"       qickc_danj,\n" + 
			"       qickc_jine,\n" + 
			"       benyrk_shul,\n" + 
			"       benyrk_danj,\n" + 
			"       benyrk_jine,\n" + 
			"       benyhy_shul,\n" + 
			"       benyhy_danj,\n" + 
			"       benyhy_jine,\n" + 
			"       shuifctzl_shul,\n" + 
			"       shuifctzl_jine,\n" + 
			"       pandks_shul,\n" + 
			"       pandks_jine,\n" + 
			"       yuemyekc_shul,\n" + 
			"       yuemyekc_danj,\n" + 
			"       yuemyekc_jine\n" + 
			"  from shoufctbb\n" + 
			"where to_char(riq,'yyyy-mm')='"+getNianf()+"-"+getYuef()+"'";
		


	ResultSetList rsl = con.getResultSetList(sql);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("SHOUFCTBB");
   	egu.setWidth("bodyWidth");
   	
   	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	
   	egu.getColumn("diancxxb_id").setHidden(true);
	egu.getColumn("diancxxb_id").setEditor(null);
	egu.getColumn("diancxxb_id").setHeader("�糧id");
	egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(visit.getDiancxxb_id()));
	
	egu.getColumn("shoufcyllbb_id").setHeader("ԭ��<br>���");
	egu.getColumn("shoufcyllbb_id").setWidth(60);
	
	List l = new ArrayList();
	l.add(new IDropDownBean(0, "ú"));
	l.add(new IDropDownBean(1, "��"));
	egu.getColumn("shoufcyllbb_id").setEditor(new ComboBox());
	egu.getColumn("shoufcyllbb_id").setComboEditor(egu.gridId,
			new IDropDownModel(l));
	egu.getColumn("shoufcyllbb_id").setReturnId(true);
	
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("riq").setDefaultValue(getNianf()+"-"+getYuef()+"-01");
	
	egu.getColumn("qickc_shul").setHeader("�ڳ����<br>������");
	egu.getColumn("qickc_shul").setWidth(70);
	
	egu.getColumn("qickc_danj").setHeader("�ڳ����<br>������");
	egu.getColumn("qickc_danj").setWidth(70);
	
	egu.getColumn("qickc_jine").setHeader("�ڳ����<br>�����");
	egu.getColumn("qickc_jine").setWidth(80);

	egu.getColumn("benyrk_shul").setHeader("�������<br>������");
	egu.getColumn("benyrk_shul").setWidth(70);
	
	egu.getColumn("benyrk_danj").setHeader("�������<br>������");
	egu.getColumn("benyrk_danj").setWidth(70);
	
	egu.getColumn("benyrk_jine").setHeader("�������<br>�����");
	egu.getColumn("benyrk_jine").setWidth(80);
	
	egu.getColumn("benyhy_shul").setHeader("���º���<br>������");
	egu.getColumn("benyhy_shul").setWidth(70);
	
	egu.getColumn("benyhy_danj").setHeader("���º���<br>������");
	egu.getColumn("benyhy_danj").setWidth(70);
	
	egu.getColumn("benyhy_jine").setHeader("���º���<br>�����");
	egu.getColumn("benyhy_jine").setWidth(80);
	
	egu.getColumn("shuifctzl_shul").setHeader("ˮ�ֲ��<br>��������");
	egu.getColumn("shuifctzl_shul").setWidth(70);
	
	egu.getColumn("shuifctzl_jine").setHeader("ˮ�ֲ��<br>�������");
	egu.getColumn("shuifctzl_jine").setWidth(80);
	
	egu.getColumn("pandks_shul").setHeader("�̵����<br>������");
	egu.getColumn("pandks_shul").setWidth(70);
	
	egu.getColumn("pandks_jine").setHeader("�̵����<br>�����");
	egu.getColumn("pandks_jine").setWidth(80);
	
	egu.getColumn("yuemyekc_shul").setHeader("��ĩ���<br>�������");
	egu.getColumn("yuemyekc_shul").setWidth(70);
	
	egu.getColumn("yuemyekc_danj").setHeader("��ĩ���<br>��浥��");
	egu.getColumn("yuemyekc_danj").setWidth(70);
	
	egu.getColumn("yuemyekc_jine").setHeader("��ĩ���<br>�����");
	egu.getColumn("yuemyekc_jine").setWidth(80);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(25);//���÷�ҳ
	//*****************************************����Ĭ��ֵ****************************
	egu.addTbarText("���");
	ComboBox comb1 = new ComboBox();
	comb1.setWidth(60);
	comb1.setTransform("NianfDropDown");
	comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
	comb1.setLazyRender(true);// ��̬��
	comb1.setEditable(true);
	egu.addToolbarItem(comb1.getScript());

	egu.addTbarText("�·�");
	ComboBox comb2 = new ComboBox();
	comb2.setWidth(50);
	comb2.setTransform("YuefDropDown");
	comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
	comb2.setLazyRender(true);// ��̬��
	comb2.setEditable(true);
	egu.addToolbarItem(comb2.getScript());

	egu.addTbarText("-");// ���÷ָ���
	

	GridButton rBtn = new GridButton("ˢ��","function(){document.Form0.submit();}");		//ˢ�°�ť
	rBtn.setIcon(SysConstant.Btn_Icon_Refurbish);
	egu.addTbarBtn(rBtn);
	
	egu.addToolbarButton(GridButton.ButtonType_Insert, null);
	egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	
	String script = "gridDiv_grid.on('afteredit',function(e){\n" + 
		"if(e.field == 'QICKC_SHUL' || e.field == 'BENYRK_SHUL' || e.field == 'BENYHY_SHUL' || e.field == 'SHUIFCTZL_SHUL' || e.field == 'PANDKS_SHUL' ||" +
		"   e.field == 'QICKC_DANJ' || e.field == 'BENYRK_DANJ' || e.field == 'BENYHY_DANJ'){\n" +
		"	var qickc_shul = e.record.get('QICKC_SHUL');\n" +
		"	var benyrk_shul = e.record.get('BENYRK_SHUL');\n" +
		"	var benyhy_shul = e.record.get('BENYHY_SHUL');\n" +
		"	var shuifctzl_shul = e.record.get('SHUIFCTZL_SHUL');\n" +
		"	var pandks_shul = e.record.get('PANDKS_SHUL');\n" +
		
		"	var qickc_danj = e.record.get('QICKC_DANJ');\n" +
		"	var benyrk_danj = e.record.get('BENYRK_DANJ');\n" +
		"	var benyhy_danj = e.record.get('BENYHY_DANJ');\n" +
		
		"	var qickc_jine = eval(qickc_shul||0)*eval(qickc_danj||0);\n" +
		"	e.record.set('QICKC_JINE',qickc_jine.toFixed(2));\n" +
		"	var benyrk_jine = eval(benyrk_shul||0)*eval(benyrk_danj||0);\n" +
		"	e.record.set('BENYRK_JINE',benyrk_jine.toFixed(2));\n" +
		"	var benyhy_jine = eval(benyhy_shul||0)*eval(benyhy_danj||0);\n" +
		"	e.record.set('BENYHY_JINE',benyhy_jine.toFixed(2));\n" +
		"	var shuifctzl_jine = eval(shuifctzl_shul||0)*eval(benyhy_danj||0);\n" +
		"	e.record.set('SHUIFCTZL_JINE',shuifctzl_jine.toFixed(2));\n" +
		"	var pandks_jine = eval(pandks_shul||0)*eval(benyhy_danj||0);\n" +
		"	e.record.set('PANDKS_JINE',pandks_jine.toFixed(2));\n" +
		
		"	var yuemyekc_shul = eval(qickc_shul||0)+eval(benyrk_shul||0)-(eval(benyhy_shul||0)-eval(shuifctzl_shul||0))-eval(pandks_shul||0);\n" +
		"	var yuemyekc_jine = eval(qickc_jine||0)+eval(benyrk_jine||0)-(eval(benyhy_jine||0)-eval(shuifctzl_jine||0))-eval(pandks_jine||0);\n" +
		"	var yuemyekc_danj = eval(yuemyekc_jine||0)/eval(yuemyekc_shul||0);\n" +
		"	e.record.set('YUEMYEKC_SHUL',yuemyekc_shul.toFixed(2));\n" +
		"	e.record.set('YUEMYEKC_JINE',yuemyekc_jine.toFixed(2));\n" +
		"	e.record.set('YUEMYEKC_DANJ',yuemyekc_danj.toFixed(2));\n" +
		"}\n" + 
		"if(e.field == 'QICKC_JINE' || e.field == 'BENYRK_JINE' || e.field == 'BENYHY_JINE' || e.field == 'SHUIFCTZL_JINE' || e.field == 'PANDKS_JINE' ){" +
		"	var qickc_jine = e.record.get('QICKC_JINE');\n" +
		"	var benyrk_jine = e.record.get('BENYRK_JINE');\n" +
		"	var benyhy_jine = e.record.get('BENYHY_JINE');\n" +
		"	var shuifctzl_jine = e.record.get('SHUIFCTZL_JINE');\n" +
		"	var pandks_jine = e.record.get('PANDKS_JINE');\n" +
		"	var yuemyekc_jine = eval(qickc_jine||0)+eval(benyrk_jine||0)-(eval(benyhy_jine||0)-eval(shuifctzl_jine||0))-eval(pandks_jine||0);" +
		"	e.record.set('YUEMYEKC_JINE',yuemyekc_jine.toFixed(2));" +
		"	var yuemyekc_shul = e.record.get('YUEMYEKC_SHUL');\n" +
		"	var yuemyekc_jine = e.record.get('YUEMYEKC_JINE');\n" +
		"	var yuemyekc_danj = eval(yuemyekc_jine||0)/eval(yuemyekc_shul||0);" +
		"	e.record.set('YUEMYEKC_DANJ',yuemyekc_danj.toFixed(2));" +
		"}\n" + 
		"});";
	
	egu.addOtherScript(script);
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
		}
		setRiq();
		getSelectData();
		
		
	}


}
