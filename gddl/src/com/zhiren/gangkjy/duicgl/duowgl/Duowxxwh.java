package com.zhiren.gangkjy.duicgl.duowgl;
/*
 * 作者:杨宏杰
 * 时间:2009-3-21
 * 内容:跺位信息维护
 */
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

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
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
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Duowxxwh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
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
					sql.append(",").append(rsl.getColumnNames()[i]);
					sql2.append(",").append(
							visit.getExtGrid1().getValueSql(
									visit.getExtGrid1().getColumn(
											rsl.getColumnNames()[i]),
									rsl.getString(i)));
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
		if(flag!=-1){
 			setMsg(ErrorMessage.SaveSuccessMessage);
 		}else{
 			setMsg("保存失败！");
 		}
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
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		}

		String sql="select distinct m.id,m.xuh,dc.mingc as diancxxb_id,dw.mingc as duowgsb_id, m.mingc,\n" +
			"                m.piny,kuc,changd,kuand, mianj,gaod,tij,diandml,meict,m.beiz\n" + 
			"  from meicb m, diancxxb dc,duowgsb dw\n" + 
			" where jib = 3 and dw.id(+)=m.duowgsb_id and dc.id = m.diancxxb_id\n" + 
				str+
			" order by m.xuh, m.mingc";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meicb");
		egu.getColumn("xuh").setHeader(Local.xuh);
		egu.getColumn("mingc").setHeader(Local.mingc);
		egu.getColumn("piny").setHeader(Local.piny);
		egu.getColumn("kuc").setHeader(Local.zuidkc);
		egu.getColumn("changd").setHeader(Local.changd);
		egu.getColumn("kuand").setHeader(Local.kuand);
		egu.getColumn("mianj").setHeader(Local.mianj);
		egu.getColumn("gaod").setHeader(Local.gaod);
		egu.getColumn("tij").setHeader(Local.tij);
		egu.getColumn("meict").setHeader(Local.meict);
		egu.getColumn("diandml").setHeader(Local.diandml);
		egu.getColumn("beiz").setHeader(Local.beiz);
		egu.getColumn("diancxxb_id").setHeader("存煤地点");
		egu.getColumn("duowgsb_id").setHeader("公司名称");
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("mingc").setWidth(100);
		egu.getColumn("piny").setWidth(80);
		egu.getColumn("kuc").setWidth(90);
		egu.getColumn("changd").setWidth(80);
		egu.getColumn("kuand").setWidth(80);
		egu.getColumn("mianj").setWidth(90);
		egu.getColumn("gaod").setWidth(80);
		egu.getColumn("tij").setWidth(80);
		egu.getColumn("meict").setWidth(80);
		egu.getColumn("diandml").setWidth(80);
		egu.getColumn("beiz").setWidth(120);
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("duowgsb_id").setWidth(100);
		egu.getColumn("meict").setHidden(true);
		egu.getColumn("meict").editor = null;

		egu.getColumn("meict").setDefaultValue(" ");
		// egu.getColumn("diancxxb_id").editor = null;
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		egu.setWidth(990);
		egu.setWidth("bodyWidth");
		egu.addPaging(18);
		
//		 公司名称
		ComboBox shouhdw = new ComboBox();
		egu.getColumn("duowgsb_id").setEditor(shouhdw);
		shouhdw.setEditable(true);
		String shouhdwSql = "select id,mingc from duowgsb order by mingc";
		egu.getColumn("duowgsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(shouhdwSql));
		egu.getColumn("duowgsb_id").setReturnId(true);
		
		ComboBox luj = new ComboBox();
		luj.setEditable(true);
		// ------------------------------
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// 选集团时刷新出所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu
					.getColumn("diancxxb_id")
					.setComboEditor(
							egu.gridId,
							new IDropDownModel(
									"select id,mingc from diancxxb where jib=3 and cangkb_id<>1 order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where fuid="
									+ getTreeid() + " and cangkb_id<>1 order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
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
			r.close();
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		}
		// -----------------------------

		egu.addTbarText("燃料公司名称:");
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
//		 打印按钮
		GridButton gbp = new GridButton("打印", "function (){"
				+ MainGlobal.getOpenWinScript("Mcreport&lx=rezc")
				+ "}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid(null);
		}
		getSelectData();
	}
}