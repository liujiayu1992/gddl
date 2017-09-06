package com.zhiren.zidy;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.*;

public class Zidyjcsjyms extends BasePage implements PageValidateListener {
	public List gridColumns;
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
		getExtGrid().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private void Return(IRequestCycle cycle){
		cycle.activate("Zidyjcsjy");
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}


	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(visit.getString9());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData(String tt) {
		String sql1 = "";
	
		JDBCcon con = new JDBCcon();
		 sql1="select zs.id as id,zs.zidyjcsjy_id as zidyjcsjy_id,zs.z_column as z_column,zs.z_column_cn as z_column_cn," +
		 		     "zs.z_datatypes as z_datatypes,zs.z_format as z_format,zs.z_width as z_width,decode(zs.z_isdatacol,1,'��',0,'��') as z_isdatacol," +
		 		     "zs.z_operational as z_operational,zs.z_weighted as z_weighted,decode(zs.z_align,"+Table.ALIGN_LEFT+",'�����',"+Table.ALIGN_RIGHT+",'�Ҷ���',"+Table.ALIGN_CENTER+",'����') as z_align,zs.z_fontsize as z_fontsize,zs.z_remark as z_remark " +
		 		     "from zidyjcsjyms zs where zidyjcsjy_id="+tt;
		
		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zidyjcsjyms");
		egu.getColumn("zidyjcsjy_id").setHidden(true);
		egu.getColumn("z_column").setHeader("����");
		egu.getColumn("z_column_cn").setHeader("������");
		egu.getColumn("z_datatypes").setHeader("��������");
		egu.getColumn("z_format").setHeader("��ʽ");
		egu.getColumn("z_format").editor.setAllowBlank(true);
		egu.getColumn("z_width").setHeader("Ĭ�Ͽ��");
		egu.getColumn("z_width").setDefaultValue("80");
		egu.getColumn("z_width").setWidth(60);
		egu.getColumn("z_isdatacol").setHeader("�Ƿ������ֶ�");
		egu.getColumn("z_operational").setHeader("ͳ�Ʒ���");
		egu.getColumn("z_weighted").setHeader("��Ȩ���ʽ");
		egu.getColumn("z_align").setHeader("���뷽ʽ");
		egu.getColumn("z_fontsize").setHeader("�ַ���С");
		egu.getColumn("z_fontsize").setDefaultValue("12");
		egu.getColumn("z_remark").setHeader("��ע");
		
		egu.getColumn("zidyjcsjy_id").setDefaultValue(tt);
		
		String sql = ZidyOperation.getBasicSQL(con,tt);
		rsl = con.getResultSetList(sql);
		rsl.getColumnNames();
		List colnames = new ArrayList();
		for(int i = 0; i< rsl.getColumnNames().length ; i++){
			colnames.add(new IDropDownBean(i,rsl.getColumnNames()[i]));
		}
		egu.getColumn("z_column").setEditor(new ComboBox());
		egu.getColumn("z_column").setComboEditor(egu.gridId,
				new IDropDownModel(colnames));
		egu.getColumn("z_column").setReturnId(false);
		
		List shifsy = new ArrayList();
		shifsy.add(new IDropDownBean(0, "date"));
		shifsy.add(new IDropDownBean(1, "datetime"));
		shifsy.add(new IDropDownBean(2, "varchar"));
		shifsy.add(new IDropDownBean(3, "number"));
		egu.getColumn("z_datatypes").setEditor(new ComboBox());
		egu.getColumn("z_datatypes").setComboEditor(egu.gridId,
				new IDropDownModel(shifsy));
		egu.getColumn("z_datatypes").setDefaultValue("date");
		egu.getColumn("z_datatypes").setReturnId(false);
		egu.getColumn("z_datatypes").setWidth(60);
		
		List shifsy2 = new ArrayList();
		shifsy2.add(new IDropDownBean(0, "��"));
		shifsy2.add(new IDropDownBean(1, "��"));
	
		
		egu.getColumn("z_isdatacol").setEditor(new ComboBox());
		egu.getColumn("z_isdatacol").setComboEditor(egu.gridId,
				new IDropDownModel(shifsy2));
		egu.getColumn("z_isdatacol").setDefaultValue("��");
		egu.getColumn("z_isdatacol").setReturnId(true);
		egu.getColumn("z_isdatacol").setWidth(80);
		
		List shifsy1 = new ArrayList();
		shifsy1.add(new IDropDownBean(0, "��"));
		shifsy1.add(new IDropDownBean(1, "��Ȩ"));
		shifsy1.add(new IDropDownBean(2, "sum"));
		shifsy1.add(new IDropDownBean(3, "avg"));
		shifsy1.add(new IDropDownBean(4, "max"));
		shifsy1.add(new IDropDownBean(5, "min"));
		shifsy1.add(new IDropDownBean(6, "count"));
		
		egu.getColumn("z_operational").setEditor(new ComboBox());
		egu.getColumn("z_operational").setComboEditor(egu.gridId,
				new IDropDownModel(shifsy1));
		egu.getColumn("z_operational").setDefaultValue("��");
		egu.getColumn("z_operational").setReturnId(false);
		egu.getColumn("z_operational").setWidth(60);
		
		List shifsy3 = new ArrayList();
		shifsy3.add(new IDropDownBean(Table.ALIGN_LEFT, "�����"));
		shifsy3.add(new IDropDownBean(Table.ALIGN_RIGHT, "�Ҷ���"));
		shifsy3.add(new IDropDownBean(Table.ALIGN_CENTER, "����"));
		
		egu.getColumn("z_align").setEditor(new ComboBox());
		egu.getColumn("z_align").setComboEditor(egu.gridId,
				new IDropDownModel(shifsy3));
		egu.getColumn("z_align").setDefaultValue("�����");
		egu.getColumn("z_align").setReturnId(true);
		egu.getColumn("z_align").setWidth(60);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
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
			getSelectData(visit.getString9());
		}
	}

}


