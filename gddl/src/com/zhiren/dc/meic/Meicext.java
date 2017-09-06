package com.zhiren.dc.meic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:tzf
 * ʱ�䣺2009-5-4
 * �޸�����:����ͼƬ������
 */
public class Meicext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// private void Save() {
	// Visit visit = (Visit) this.getPage().getVisit();
	// visit.getExtGrid1().Save(getChange(), visit);
	// }
	private void Save() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		//���� ���ݹ�����  ����  ���� ��&��Ҫ�ų� ���������
//		System.out.println(getChange());
		if(this.getChange()!=null){
			this.Change=this.getChange().replaceAll("(&lt;)+(.)*(&gt;)+", "");
		}
//		System.out.println(Change);
		
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1()
				.getDeleteResultSet(getChange());
		StringBuffer sql = new StringBuffer("begin \n");
		while (drsl.next()) {
			sql.append("delete from ").append("meicb").append(" where id =")
					.append(drsl.getString(0)).append(";\n");
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (rsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(rsl.getString("ID"))) {
				sql.append("insert into ").append("meicb").append("(id");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					//if (!rsl.getColumnNames()[i].equals("MEICT")){
						sql.append(",").append(rsl.getColumnNames()[i]);
						sql2.append(",").append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i)));
					//}
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append("meicb").append(" set ");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					sql.append(rsl.getColumnNames()[i]).append(" = ");
					sql.append(
							visit.getExtGrid1().getValueSql(
									visit.getExtGrid1().getColumn(
											rsl.getColumnNames()[i]),
									rsl.getString(i))).append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(rsl.getString("ID")).append(
						";\n");
			}
		}
		sql.append("end;");
		int flag=con.getUpdate(sql.toString());
		if(flag<0){
			setMsg("����ʧ�ܣ�");
		}else{
			setMsg("����ɹ���");
			con.commit();
		}
		con.Close();
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

	public List gridColumns;

	public List getGridColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		return gridColumns;
	}

	public void addColumn(GridColumn column) {
		getGridColumns().add(column);
	}

	public GridColumn getColumn(int colindex) {
		return (GridColumn) getGridColumns().get(colindex);
	}

	public GridColumn getColumn(String coldataIndex) {
		for (int i = 0; i < getGridColumns().size(); i++) {
			if (coldataIndex.toUpperCase().equals(
					getColumn(i).dataIndex.toUpperCase())) {
				return getColumn(i);
			}
		}
		return null;
	}

	public void addTbarTreeBtn(String treeid) {
		TextField tf = new TextField();
		tf.setId(treeid + "_text");
		tf.setWidth(100);

		StringBuffer bf = new StringBuffer();
		bf.append("{").append("icon:'").append(
				"ext/resources/images/list-items.gif").append("',");
		bf.append("cls: 'x-btn-icon',");
		bf.append("handler:function(){").append(treeid).append(
				"_window.show();}}");

	}

	public String tbars;

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public int getDataColumnCount() {
		int count = 0;
		for (int c = 0; c < getGridColumns().size(); c++) {
			if (((GridColumn) getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
				count++;
			}
		}
		return count;
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

		if (treeid==null||treeid.equals("")) {

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

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		gridColumns = null;
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}

		ResultSetList rsl = con
				.getResultSetList("select distinct m.id,m.xuh,dc.mingc as diancxxb_id,m.mingc,m.piny,kuc,changd,kuand,mianj,gaod,tij,diandml,'<a href=\""+MainGlobal.getHomeContext(this)+"/app?service=page/ImageReport&&id='||m.id||'&&mk=meicxx\" target=\"_blank\">�鿴</a>' as meict,"
						+ "m.beiz from meicb m,diancxxb dc where jib=3  and dc.id=m.diancxxb_id "
						+ "" + str +
						// " and m.diancxxb_id ="+ getTreeid() +
						" order by m.xuh,m.mingc");

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meicb");
		egu.setWidth("bodyWidth");
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("piny").setHeader("ƴ��");
		egu.getColumn("kuc").setHeader("�����(t)");
		egu.getColumn("changd").setHeader("����(m)");
		egu.getColumn("kuand").setHeader("���(m)");
		egu.getColumn("mianj").setHeader("���(m<sup>2</sup>)");
		egu.getColumn("gaod").setHeader("�Ѹ�(m)");
		egu.getColumn("tij").setHeader("���(m<sup>3</sup>)");
		egu.getColumn("meict").setHeader("ú��ͼ");
		egu.getColumn("diandml").setHeader("���ú��(t)");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("xuh").setDefaultValue("0");
		egu.getColumn("mingc").setWidth(100);
		egu.getColumn("mingc").editor.setAllowBlank(false);
		egu.getColumn("piny").setWidth(80);
		egu.getColumn("piny").editor.setAllowBlank(false);
		egu.getColumn("kuc").setWidth(90);
		egu.getColumn("kuc").setDefaultValue("0");
		egu.getColumn("changd").setWidth(80);
		egu.getColumn("changd").setDefaultValue("0");
		egu.getColumn("kuand").setWidth(80);
		egu.getColumn("kuand").setDefaultValue("0");
		egu.getColumn("mianj").setWidth(90);
		egu.getColumn("gaod").setWidth(80);
		egu.getColumn("tij").setWidth(80);
		egu.getColumn("meict").setWidth(80);
		egu.getColumn("diandml").setWidth(80);
		egu.getColumn("diandml").setDefaultValue("0");
		egu.getColumn("beiz").setWidth(120);
		egu.getColumn("diancxxb_id").setWidth(100);
		//egu.getColumn("meict").setHidden(true);
		egu.getColumn("meict").editor = null;
		egu.getColumn("meict").update=false; //����ʱ�����¸��ֶ�
		egu.getColumn("meict").setDefaultValue(" ");
		// egu.getColumn("diancxxb_id").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		ComboBox luj = new ComboBox();
		luj.setEditable(true);
		// ------------------------------
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu
					.getColumn("diancxxb_id")
					.setComboEditor(
							egu.gridId,
							new IDropDownModel(
									"select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where fuid="
									+ getTreeid() + " order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
		 egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		 egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
		 new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con
					.getResultSetList("select id,mingc from diancxxb where id="
							+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		}
		// -----------------------------

		egu.addTbarText("�糧����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		// egu.getColumn("diancxxb_id").setDefaultValue(
		// "" + visit.getDiancxxb_id());
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str1 = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Meicreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + str1 + "}").getScript()
				+ "}");
		
		GridButton gbphoto = new GridButton("ͼƬ����","function (){var rec = gridDiv_sm.getSelected();if(!rec){"+MainGlobal.getExtMessageBox("��ѡ���豸�鿴��Ӧ��ͼƬ", false)+"\n return;}"+MainGlobal.getOpenWinScript("ImageUpLoad&mk=meicxx&id='+rec.get(\"ID\")+'")+ "}");
		gbphoto.setIcon(SysConstant.Btn_Icon_Show);
		egu.addTbarBtn(gbphoto);
		
		setExtGrid(egu);
		con.Close();
	}

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
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
			setTreeid(null);
		}
		getSelectData();
	}
}