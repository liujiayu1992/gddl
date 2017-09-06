package com.zhiren.dc.zhengcylbz;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhengcylbz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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

	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	// ���ɰ�ť
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}

	// �������ڰ�ť
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
		}

		if (_DeleteClick) {
			_DeleteClick = false;
			delete();
		}

		if (_CreateClick) {
			_CreateClick = false;
			create();
		}

		if (_CopyClick) {
			_CopyClick = false;
			copysq();
		}
		getSelectData();

	}

	// ���ܣ����ɷ���
	// �߼������жϻ��ܱ�����û�е�ǰ���ڵ�ǰ�糧���������û�в���һ�����жϱ�׼������û�е�ǰ����
	// ��ǰ�糧�����������ɾ������ITEMSORT��ITEM���ж�ȡ��Ӧ�������ȴ����׼��Ȼ����ʾ��ҳ���ϣ�ά������ٴ����׼��
	// ������
	public void create() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		
		String sql = "select id from ZHENGCYLBZBHZB where diancxxb_id = \n"
				+ getTreeid() + "and riq = " + CurrODate;

		String sql1 = "insert into ZHENGCYLBZBHZB(id,diancxxb_id,riq)\n"
				+ "values(getNewID(" + visit.getDiancxxb_id() + "),"
				+ getTreeid() + "," + CurrODate + ")";

		ResultSetList rsl = con.getResultSetList(sql);

		if (!rsl.next()) {
			con.getInsert(sql1);
		}

		String sqlS = "select * from ZHENGCYLBZB where diancxxb_id = "
				+ getTreeid() + " and riq = " + CurrODate;
		String sqlD = "delete from ZHENGCYLBZB where diancxxb_id = "
				+ getTreeid() + " and riq = " + CurrODate;

		ResultSetList rsl1 = con.getResultSetList(sqlS);
		if (rsl1.next()) {
			con.getDelete(sqlD);
		}

		String sql2 = "select i.id from item i, itemsort it\n"
				+ "where i.itemsortid = it.itemsortid and it.bianm = 'ZCYLZB' \n";
		ResultSetList rsl2 = con.getResultSetList(sql2);

		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		while (rsl2.next()) {
			String sql3 = "insert into ZHENGCYLBZB(id, diancxxb_id, riq,"
					+ " zhengcylzb_item_id, danwb_id, biaoz, gongs, beiz)\n"
					+ "values(getNewID(" + visit.getDiancxxb_id() + "), "
					+ getTreeid() +", "+ CurrODate + ", " + rsl2.getLong("id")
					+ ",0,0,'','');";
			sb.append(sql3);
		}
		sb.append("end;");

		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getInsert(sb.toString());
		}
		rsl.close();
		rsl1.close();
		rsl2.close();
		con.Close();
	}

	// ���ܣ��������ڷ���
	// �߼������жϻ��ܱ�����û�е�ǰ���ڵ�ǰ�糧���������û�в���һ�����жϱ�׼������û�е�ǰ����
	// ��ǰ�糧�����������ɾ����������һ������ݴ����׼����
	// ������
	public void copysq() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		int nianf = Integer.parseInt(getNianf()) - 1;
		String Nianf = nianf + "";
		
		String sql = "select id from ZHENGCYLBZBHZB where diancxxb_id = \n"
				+ getTreeid() + " and riq = " + CurrODate;

		String sql1 = "insert into ZHENGCYLBZBHZB(id,diancxxb_id,riq)\n"
				+ "values(getNewID(" + visit.getDiancxxb_id() + "),"
				+ getTreeid() + "," + CurrODate + ")";

		ResultSetList rsl = con.getResultSetList(sql);

		if (!rsl.next()) {
			con.getInsert(sql1);
		}

		String sqlS = "select * from ZHENGCYLBZB where diancxxb_id = " + getTreeid() + " and riq = " + CurrODate;
		String sqlD = "delete from ZHENGCYLBZB where diancxxb_id = " + getTreeid() + " and riq = " + CurrODate;

		ResultSetList rsl1 = con.getResultSetList(sqlS);
		if (rsl1.next()) {
			con.getDelete(sqlD);
		}

		String sql2 = "select i.id, bz.danwb_id, bz.biaoz, bz.gongs, bz.beiz from item i, itemsort it, zhengcylbzb bz, zhengcylbzbhzb hz\n"
				+ "where i.itemsortid = it.itemsortid and it.bianm = 'ZCYLZB' and bz.zhengcylzb_item_id = i.id\n"
				+ "and bz.diancxxb_id = hz.diancxxb_id and hz.diancxxb_id = "
				+ getTreeid() + " and bz.riq = hz.riq and to_char(hz.riq,'yyyy') = '" + Nianf + "'";
		ResultSetList rsl2 = con.getResultSetList(sql2);

		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		while (rsl2.next()) {
			String sql3 = "insert into ZHENGCYLBZB(id, diancxxb_id, riq, zhengcylzb_item_id, danwb_id, biaoz, gongs, beiz)\n"
					+ "values(getNewID("
					+ visit.getDiancxxb_id()
					+ "), "
					+ getTreeid()
					+ ", "
					+ CurrODate
					+ ", "
					+ rsl2.getLong("id")
					+ ",\n"
					+ rsl2.getLong("danwb_id")
					+ ",\n"
					+ rsl2.getDouble("biaoz")
					+ ", '"
					+ rsl2.getString("gongs")
					+ "', '" + rsl2.getString("beiz") + "');";
			sb.append(sql3);
		}
		sb.append("end;");

		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getInsert(sb.toString());
		}
		rsl.close();
		rsl1.close();
		rsl2.close();
		con.Close();
	}

	// ���ܣ�ɾ��
	// �߼����������ڡ��糧ɾ�����ܱ�ͱ�׼��������
	// ������
	public void delete() {
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		String sql = "delete from ZHENGCYLBZBHZB where diancxxb_id = "
				+ getTreeid() + " and riq = " + CurrODate + ";";
		String sqlD = "";
		String sqls = "select bz.* from zhengcylbzb bz, zhengcylbzbhzb hz where bz.diancxxb_id = hz.diancxxb_id \n"
				+ "and hz.diancxxb_id = "
				+ getTreeid()
				+ " and bz.riq = hz.riq and hz.riq = "
				+ CurrODate;
		ResultSetList rsl = con.getResultSetList(sqls);
		StringBuffer sb = new StringBuffer("begin \n");
		while (rsl.next()) {
			sqlD = "delete from ZHENGCYLBZB where diancxxb_id = " + getTreeid() + " and riq = "	+ CurrODate + ";";
			sb.append(sqlD);
		}
		sb.append(sql).append("end;");
		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getDelete(sb.toString());
		}
		rsl.close();
		con.Close();
	}

	// ���ܣ���ҳ������ʾ����
	// �߼����ӻ��ܱ���׼��item��itemsort��danwb���ж�ȡ��ǰ���ڵ�ǰ�糧���ݲ���ʾ��ҳ����
	// ������
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		String sql = "select bz.id, bz.diancxxb_id, bz.riq, i.mingc as zhengcylzb_item_id, nvl(dw.mingc,'') as danwb_id, bz.biaoz, bz.gongs, bz.beiz \n"
				+ "from item i, itemsort it, zhengcylbzb bz, zhengcylbzbhzb hz, danwb dw where i.itemsortid = it.itemsortid \n"
				+ "and it.bianm = 'ZCYLZB' and bz.zhengcylzb_item_id = i.id and bz.diancxxb_id = hz.diancxxb_id and bz.danwb_id = dw.id(+) and hz.diancxxb_id = \n"
				+ getTreeid()
				+ " and bz.riq = hz.riq and to_char(hz.riq,'yyyy') = '"
				+ getNianf()
				+ "'";

		ResultSetList rsl = con.getResultSetList(sql);
		System.out.println(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("ZHENGCYLBZB");
		egu.setWidth("bodyWidth");

		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setReturnId(true);
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setReturnId(true);
		egu.getColumn("riq").setDefaultValue(CurrODate);

		egu.getColumn("zhengcylzb_item_id").setHeader("��Ŀ����");
		egu.getColumn("zhengcylzb_item_id").setEditor(null);
		egu.getColumn("zhengcylzb_item_id").update = false;

		egu.getColumn("danwb_id").setHeader("��λ");
		// ��λ������
		ComboBox cb = new ComboBox();
		egu.getColumn("danwb_id").setEditor(cb);
		cb.setEditable(false);
		String Sql = "select id,mingc from danwb order by id";
		egu.getColumn("danwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));

		egu.getColumn("biaoz").setHeader("��׼");
		egu.getColumn("gongs").setHeader("���ֹ�ʽ");
		egu.getColumn("beiz").setHeader("��ע");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		// ���ComBox
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(false);
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");

		// �糧��
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		// �趨�������������Զ�ˢ��
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");

		// ˢ�°�ť
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);

		// ���ɰ�ť
		GridButton CreateButton = new GridButton("����",
				getBtnHandlerScript("CreateButton"));
		CreateButton.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(CreateButton);

		// egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		GridButton DeleteButton = new GridButton("ɾ��",
				getBtnHandlerScript("DeleteButton"));
		DeleteButton.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(DeleteButton);

		egu.addToolbarButton(GridButton.ButtonType_Save_condition,
				"SaveButton", "var Mr = gridDiv_grid.getStore().getRange(); \n"
						+ "for(i = 0; i< Mr.length; i++){ \n"
						+ "		if(Mr[i].get('DANWB_ID')==0){ \n"
						+ "			Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ��λ!');\n"
						+ " 		return false;" + " } \n" + " } \n");

		// �������ڰ�ť
		GridButton CopyButton = new GridButton("��������",
				getBtnHandlerScript("CopyButton"));
		egu.addTbarBtn(CopyButton);

		setExtGrid(egu);
		rsl.close();
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton") || btnName.endsWith("CopyButton")) {
			btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		} else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));

			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			getSelectData();
		}
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

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

	// ���
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

}
