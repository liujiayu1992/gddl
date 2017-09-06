package com.zhiren.dc.meic;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meicwh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private void Save() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		// 对于 传递过来的 数据 里面 有&的要排除 ，否则出错
		// System.out.println(getChange());
		if (this.getChange() != null) {
			this.Change = this.getChange().replaceAll("(&lt;)+(.)*(&gt;)+", "");
		}
		// System.out.println(Change);
		
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql = new StringBuffer("begin \n");
		while (drsl.next()) {
			sql.append("delete from ").append("meicbext").append(" where id = ")
					.append(drsl.getString(0)).append(";\n");
		}
		
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (rsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(rsl.getString("ID"))) {
				sql.append("insert into ").append("meicbext").append("(id");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					//if (!rsl.getColumnNames()[i].equals("MEICT")){
						sql.append(", ").append(rsl.getColumnNames()[i]);
						sql2.append(", ").append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i)));
					//}
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append("meicbext").append(" set ");
				for (int i = 1; i < rsl.getColumnCount(); i++) {
					sql.append(rsl.getColumnNames()[i]).append(" = ");
					sql.append(
							visit.getExtGrid1().getValueSql(
									visit.getExtGrid1().getColumn(
											rsl.getColumnNames()[i]),
									rsl.getString(i))).append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id = ").append(rsl.getString("ID")).append(";\n");
			}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag < 0) {
			setMsg("保存失败！");
		} else {
			setMsg("保存成功！");
			con.commit();
		}
		con.Close();
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
		
	public void submit(IRequestCycle cycle) {	
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		
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
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
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
		JDBCcon con = new JDBCcon();
		gridColumns = null;
		
		String sql = "SELECT MC.ID, MC.XUH, MC.DIANCXXB_ID, MC.MINGC, MC.BEIZ" +
				     "  FROM MEICBEXT MC" +
				     " WHERE MC.DIANCXXB_ID = " + getTreeid() + 
				     " ORDER BY XUH, MINGC";
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meicbext");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("mingc").setHeader("煤场名称");
		egu.getColumn("mingc").setWidth(100);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(100);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		
		egu.addTbarText("电厂名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		GridButton refresh = new GridButton("刷新", "function(){document.getElementById('RefurbishButton').click();}");
		refresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refresh);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid(null);
		}
		getSelectData();
	}
}