package com.zhiren.dc.yunsjgfa;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunsjgfa extends BasePage implements PageValidateListener {
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

	private void Save() {
		getExtGrid().Save(getChange(), (Visit) this.getPage().getVisit());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private void Shez(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Yunsjgmx");
	}

	private boolean _ShezChick = false;

	public void ShezButton(IRequestCycle cycle) {
		_ShezChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShezChick) {
			_ShezChick = false;
			Shez(cycle);
		}
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhuangt_t="";
		if(getZhuangtValue().getId()==1){
			zhuangt_t="and zhuangt=1";
		}else if(getZhuangtValue().getId()==2){
			zhuangt_t="and zhuangt=0";
		}else {
			zhuangt_t="";
		}
		
		ResultSetList rsl = con
				.getResultSetList("select id,diancxxb_id,mingc,decode(zhuangt,1,'����','������')zhuangt from yunsjgfab where  diancxxb_id="
						+ v.getDiancxxb_id() + zhuangt_t+" order by id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// ���ñ��������ڱ���
		egu.setTableName("yunsjgfab");
		// ������ʾ������
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("zhuangt").setHeader("״̬");
		// �����п��
		egu.getColumn("mingc").setWidth(200);
		
		List zhuangt = new ArrayList();
		zhuangt.add(new IDropDownBean("1","����"));
		zhuangt.add(new IDropDownBean("0","������"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(zhuangt));
		egu.getColumn("zhuangt").setDefaultValue("����");
		// ���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// ֻ��ѡ�е���
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		
		egu.getColumn("diancxxb_id").hidden = true;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(v.getDiancxxb_id()));
		
		egu.addTbarText("״̬:");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("ZhuangtDropDown");
		comb4.setId("Zhuangt");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(100);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Zhuangt.on('select',function(){document.forms[0].submit();});");

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("��������","function(){" +
				"if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){" +
				"Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��۸񷽰���');" +
				"return;}" +
				"grid1_rcd = gridDiv_sm.getSelections()[0];" +
				"if(grid1_rcd.get('ID') == '0'){" +
				"Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�񷽰�����֮ǰ���ȱ���!');" +
				"return;}" +
				"grid1_history = grid1_rcd.get('ID');" +
				"var Cobj = document.getElementById('CHANGE');" +
				"Cobj.value = grid1_history;" +
				"document.getElementById('ShezButton').click();" +
				"}").getScript()+"}");
//		GridButton gbp = new GridButton("��ӡ", "function (){"
//				+ MainGlobal.getOpenWinScript("Caizhfsreport") + "}");
//		gbp.setIcon(SysConstant.Btn_Icon_Print);
//		egu.addTbarBtn(gbp);
		setExtGrid(egu);
		con.Close();
	}

	public boolean _Zhuangtchange = false;

	public IDropDownBean getZhuangtValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getZhuangtModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setZhuangtValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getZhuangtModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getZhuangtModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setZhuangtModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getZhuangtModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			List.add(new IDropDownBean(0, "ȫ��"));
			List.add(new IDropDownBean(1, "����"));
			List.add(new IDropDownBean(2, "������"));

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
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
			setZhuangtValue(null);
			setZhuangtModel(null);
			getZhuangtModels();
			getSelectData();
		}
		getSelectData();
	}
}
