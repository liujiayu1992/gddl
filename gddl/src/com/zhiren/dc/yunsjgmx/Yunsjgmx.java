package com.zhiren.dc.yunsjgmx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunsjgmx extends BasePage implements PageValidateListener {
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
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
        _ReturnChick = true;
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("Yunsjgfa");
        }
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql =

			"select y.id,yg.mingc as yunsjgfab_id,l.mingc liclxb_id,y.lic,y.licjg " +
			"from yunsjgmxb y,liclxb l,yunsjgfab yg\n" +
			"where y.yunsjgfab_id=" +v.getString1()+
			" and y.liclxb_id=l.id " +
			"and yg.id=y.yunsjgfab_id";

		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("yunsjgmxb");
//		������
		egu.getColumn("yunsjgfab_id").hidden=true;
		// /������ʾ������
		//egu.getColumn("yunsjgfab_id").setHeader("���䷽��");
		egu.getColumn("liclxb_id").setHeader("�������");
		egu.getColumn("lic").setHeader("���(����)");
		egu.getColumn("licjg").setHeader("��̼۸�");
		// //���õ�ǰ��Ĭ��ֵ
	egu.getColumn("yunsjgfab_id").setDefaultValue(v.getString1());
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// /��̬������
//		egu.getColumn("yunsjgfab_id").setEditor(new ComboBox());
//		egu.getColumn("yunsjgfab_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from yunsjgfab where id="+v.getString1()));
		egu.getColumn("liclxb_id").setEditor(new ComboBox());
		egu.getColumn("liclxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from liclxb"));

		// /�Ƿ񷵻��������ֵ��ID
		// egu.getColumn("bumb_id").setReturnId(true);
		// egu.getColumn("leibb_id").setReturnId(true);
		// /���ð�ť
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		GridButton insert =new GridButton("���","function() {"+
				"lictmp ='';"+
				"if(gridDiv_ds.getCount()>0){"+
				"gridDiv_grid.getSelectionModel().selectRow(gridDiv_ds.getCount()-1);"+
				"var cur = gridDiv_grid.getSelectionModel().getSelected();"+
				"lictmp = cur.get('LICLXB_ID');"+
				"}"+
				"var plant = new gridDiv_plant({ID: '0',YUNSJGFAB_ID:' "+v.getString1()+"',LICLXB_ID: lictmp ,LIC: '',LICJG: ''});"+

				"gridDiv_ds.insert(gridDiv_ds.getCount(),plant);"+
				"}",SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(insert);

		//insertbuton.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton btnreturn = new GridButton("����",
				"function (){document.getElementById('ReturnButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
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
			getSelectData();
		}
	}
}
