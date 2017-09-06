package com.zhiren.dc.zhuangh.meicang;

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
 * 作者:chenzt
 * 时间：2010-5-12
 * 
 */
public class Meicsc extends BasePage implements PageValidateListener {
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
		con.setAutoCommit(false);
		//对于 传递过来的  数据  里面 有&的要排除 ，否则出错
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
			sql.append("delete from ").append("duowsc").append(" where id =")
					.append(drsl.getString(0)).append(";\n");
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (rsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(rsl.getString("ID"))) {
				sql.append("insert into ").append("duowsc").append("(id");
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
				if(rsl.getInt("SHENGCS")>0 && !"".equals(rsl.getString("GUIZ"))){
					StringBuffer sql3 = new StringBuffer("begin\n");
					for(int i=0;i<rsl.getInt("SHENGCS");i++){
						
						String qis="";
						qis=String.valueOf(Integer.parseInt(rsl.getString("BIANMQS"))+i);
						if("0".equals(qis.substring(0, 1))&&qis.length()>1){
							qis="0"+qis;
						}else if(qis.length()<=1){
							qis="0"+qis;
						}
						sql3.append(" insert into duow (id,DIANCXXB_ID,XUH,MINGC,BIANM) values (getnewid(")
						.append(visit.getDiancxxb_id()).append(")").append(","+visit.getDiancxxb_id()+","+(i+1))
						.append(",'"+rsl.getString("GUIZ")+qis+"','"+rsl.getString("GUIZ")+qis+"'").append(");\n");
					}
					sql3.append("end;");
					int flag=con.getUpdate(sql3.toString());
					if(flag<0){
						setMsg("保存失败！");
					}else{
						setMsg("保存成功！");
						con.commit();
					}
				}
			} else {
				sql.append("update ").append("duowsc").append(" set ");
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
			setMsg("保存失败！");
		}else{
			setMsg("保存成功！");
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
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		}

		ResultSetList rsl = con
				.getResultSetList("select distinct m.id,m.xuh,m.guiz ,m.bianmqs,m.shengcs," 
//						",kuc,changd,kuand,mianj,gaod,tij,diandml," +
//						"'<a href=\""+MainGlobal.getHomeContext(this)+"/app?service=page/" +
//								"ImageReport&&id='||m.id||'&&mk=meicxx\" target=\"_blank\">查看</a>' as meict,"
						+ "m.beiz from duowsc m"
						+ "" + 
						// " and m.diancxxb_id ="+ getTreeid() +
						" order by m.xuh,m.guiz");
		int count =0;
		if(rsl.getRows()>0){
			count=rsl.getRows();
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("duowsc");
		egu.setWidth("bodyWidth");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("GUIZ").setHeader("规则");
		egu.getColumn("bianmqs").setHeader("编码起始数");

		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("shengcs").setHeader("生成数目");
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("xuh").setDefaultValue("0");
		egu.getColumn("guiz").setWidth(100);
		egu.getColumn("guiz").editor.setAllowBlank(false);
		egu.getColumn("bianmqs").setWidth(80);
		egu.getColumn("bianmqs").editor.setAllowBlank(false);
		egu.getColumn("shengcs").editor.setAllowBlank(false);

//		egu.getColumn("diancxxb_id").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		ComboBox luj = new ComboBox();
		luj.setEditable(true);


		
		// -----------------------------

		egu.addTbarText("电厂名称:");
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
		
		StringBuffer sb = new StringBuffer();
		if(count>0){
		sb.append("gridDiv_grid.on('beforeedit',function(e){");

		// 设定某一行不能编辑
		sb
				.append("if(e.row<"+count+"){e.cancel=true;}");//

		sb.append("});");
		}
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
		con.Close();
	}

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		
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