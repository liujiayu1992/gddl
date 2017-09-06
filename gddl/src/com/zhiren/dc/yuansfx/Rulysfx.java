package com.zhiren.dc.yuansfx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rulysfx extends BasePage implements PageValidateListener {
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

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long xiangmmcID = this.getYuansxmmcValue().getId();
		
		long diancxxb_id = visit.getDiancxxb_id();
		
		if (visit.isFencb()) {
			diancxxb_id = Long.parseLong(getTreeid());
		}
		
		ResultSetList rsl = con
				.getResultSetList("select r.id, diancxxb_id, y.zhi as rulysfxxm_id, r.riq, r.zhi, r.zhuangt\n"
						+ "  from rulysfxb r,\n"
						+ "       (select id, zhi\n"
						+ "          from xitxxb\n"
						+ "         where leib = '��¯'\n"
						+ "           and zhuangt = 1\n"
					    + "            and id=" +xiangmmcID
					    +") y\n"
						+ " where r.diancxxb_id = "
						+ diancxxb_id
						+ "\n"
						+ "   and to_char(r.riq, 'yyyy') = '"
						+ intyear
						+ "'\n" + "   and r.rulysfxxm_id = y.id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("rulysfxb");
		// /������ʾ������
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("rulysfxxm_id").setHeader("��Ŀ����");

		egu.getColumn("zhi").setHeader("ֵ");
		egu.getColumn("riq").setHeader("��������");

		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setDefaultValue("1");
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + diancxxb_id);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// /��̬������
		egu.getColumn("rulysfxxm_id").setEditor(new ComboBox());
		egu.getColumn("rulysfxxm_id").setComboEditor(
				egu.gridId,
				new IDropDownModel("select y.id, y.zhi\n"
						+ "  from (select id, zhi\n"
						+ "          from xitxxb\n"
						+ "         where leib = '��¯'\n"
						+ "           and mingc like '��¯������'\n"
						+ "           and zhuangt = 1\n"
						+ "           and diancxxb_id = diancxxb_id\n"
						+ "        union\n" + "        select id, zhi\n"
						+ "          from xitxxb\n"
						+ "         where leib = '��¯'\n"
						+ "           and mingc like '��¯������'\n"
						+ "           and zhuangt = 1) y"));
		// /����������Ĭ��ֵ
		egu.getColumn("rulysfxxm_id").setDefaultValue(
				getYuansxmmcValue().getValue());

		// ********************������************************************************
		
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");
		
		// ��ҳ���ϵ��������
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("��Ŀ����:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("YuansxmmcDropDown");
		comb3.setId("yuansxm");
		comb3.setLazyRender(true);// ��̬��
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());
		// �趨�������������Զ�ˢ��
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});yuansxm.on('select',function(){document.forms[0].submit();});");
		// /���ð�ť
		
		if (visit.isFencb()) {
			if (!(visit.getDiancxxb_id() + "").equals(getTreeid())) {
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				// egu.addToolbarItem("{"+new
				// GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			}
		} else {
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			// egu.addToolbarItem("{"+new
			// GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/Yuansfxreport&lx='+NIANF.getValue()+'&lx='+gongys.getValue()+'&lx='+yuansxm.getValue();"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + str + "}").getScript()
				+ "}");
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

	private String treeid = "";

	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
			this.setNianfValue(null);
			this.getNianfModels();
			setYuansxmmcValue(null);
			getIYuansxmmcModels();

		}
		getSelectData();
	}

	// ���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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

	// ��Ŀ����
	public boolean _Yuansxmmcchange = false;

	private IDropDownBean _YuansxmmcValue;

	public IDropDownBean getYuansxmmcValue() {
		if (_YuansxmmcValue == null) {
			_YuansxmmcValue = (IDropDownBean) getIYuansxmmcModels()
					.getOption(0);
		}
		return _YuansxmmcValue;
	}

	public void setYuansxmmcValue(IDropDownBean Value) {
		long id = -2;
		if (_YuansxmmcValue != null) {
			id = _YuansxmmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Yuansxmmcchange = true;
			} else {
				_Yuansxmmcchange = false;
			}
		}
		_YuansxmmcValue = Value;
	}

	private IPropertySelectionModel _IYuansxmmcModel;

	public void setIYuansxmmcModel(IPropertySelectionModel value) {
		_IYuansxmmcModel = value;
	}

	public IPropertySelectionModel getIYuansxmmcModel() {
		if (_IYuansxmmcModel == null) {
			getIYuansxmmcModels();
		}
		return _IYuansxmmcModel;
	}

	public IPropertySelectionModel getIYuansxmmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select y.id, y.zhi\n" + "  from (select id, zhi\n"
					+ "          from xitxxb\n"
					+ "         where leib = '��¯'\n"
					+ "           and mingc like '��¯������'\n"
					+ "           and zhuangt = 1\n"
					+ "           and diancxxb_id = diancxxb_id\n"
					+ "        union\n" + "        select id, zhi\n"
					+ "          from xitxxb\n"
					+ "         where leib = '��¯'\n"
					+ "           and mingc like '��¯������'\n"
					+ "           and zhuangt = 1) y";
			_IYuansxmmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IYuansxmmcModel;
	}
}
