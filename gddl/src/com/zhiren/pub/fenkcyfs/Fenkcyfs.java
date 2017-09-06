package com.zhiren.pub.fenkcyfs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-28 17��30
 * �������޸Ĳ�����ʽ���õ�ʱ��ѡ��ֳ�������Ҫ�Գ�����д���ʱѡ��糧��
 */
/*
 * ���ߣ�songy
 * ʱ�䣺2011-03-23 
 * �������޸������˵�������Ҫ�������ƽ�������
 */
public class Fenkcyfs extends BasePage implements PageValidateListener {
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
		}
		getSelectData();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("SELECT   f.ID, m.mingc AS meikxxb_id, j.mingc AS jihkjb_id,\n"
						+ "         y.mingc AS yunsfsb_id, ch.mingc AS chezxxb_id,\n"
						+ "         ca.mingc AS caizhfsb_id, d.id AS diancxxb_id\n"
						+ "    FROM fenkcyfsb f,\n"
						+ "         meikxxb m,\n"
						+ "         yunsfsb y,\n"
						+ "         jihkjb j,\n"
						+ "         chezxxb ch,\n"
						+ "         diancxxb d,\n"
						+ "         caizhfsb ca\n"
						+ "   WHERE f.meikxxb_id = m.ID\n"
						+ "     AND f.yunsfsb_id = y.ID\n"
						+ "     AND f.jihkjb_id = j.ID\n"
						+ "     AND f.chezxxb_id = ch.ID\n"
						+ "     AND f.diancxxb_id = d.ID\n"
						+ "     AND f.caizhfsb_id = ca.ID\n"
						+ "     AND (f.diancxxb_id = "
						+ getTreeid() + " or d.fuid = " + getTreeid()
						+ ")\n" + "ORDER BY f.meikxxb_id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("fenkcyfsb");
		// /������ʾ������
		egu.getColumn("meikxxb_id").setHeader("ú����Ϣ");
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("chezxxb_id").setHeader("��վ");
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("caizhfsb_id").setHeader("���ƻ���ʽ");
		// //�����п��
		//egu.getColumn("jihkjb_id").setWidth(200);
		// //���õ�ǰ���Ƿ�༭
		// /���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// /��̬������
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meikxxb  order by mingc "));
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb  order by mingc "));
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from jihkjb  order by mingc "));
		egu.getColumn("chezxxb_id").setEditor(new ComboBox());
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb  order by mingc "));
		egu.getColumn("caizhfsb_id").setEditor(new ComboBox());
		egu.getColumn("caizhfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from caizhfsb where diancxxb_id = " + 
						visit.getDiancxxb_id()+" order by mingc "));
		
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		/*
		ComboBox dc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		dc.setEditable(true);
		String sql = 
			"select id, mingc from (\n" +
			"select id, mingc, level,CONNECT_BY_ISLEAF leaf\n" + 
			"  from diancxxb\n" + 
			" start with id = "+getTreeid()+"\n" + 
			"connect by fuid = prior id)\n" + 
			"where leaf = 1";
		IDropDownModel dcmd = new IDropDownModel(sql);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dcmd);
		if(dcmd.getOptionCount()>1){
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else{
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
			egu.getColumn("diancxxb_id").setDefaultValue(dcmd.getBeanValue(getTreeid()));
		}
		*/
		// /����������Ĭ��ֵ
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		// /���ð�ť
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		GridButton gbp = new GridButton("��ӡ", "function (){"
//				+ MainGlobal.getOpenWinScript("Fenkcyfsreport&lx=rezc") + "}");
//		gbp.setIcon(SysConstant.Btn_Icon_Print);
//		egu.addTbarBtn(gbp);
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
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
	}
}
