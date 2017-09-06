package com.zhiren.jt.dianc;

import java.io.IOException;
import java.io.StringReader;
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
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 作者:tzf
 * 时间:2009-4-29
 * 修改内容:针对 一厂多制 的  页面无法展示的  相关问题 对 下拉框中的上级单位 进行更改
 *
 */
public class Diancext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		Visit visit = (Visit) this.getPage().getVisit();
		Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean m_falg;

	public boolean getFalg() {
		return m_falg;
	}

	public void setFalg(boolean falg) {
		m_falg = falg;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	// private static final long serialVersionUID = 7020946077527159829L;

	public static final int Gridstyle_Read = 0;

	public static final int Gridstyle_Edit = 1;

	public static final int GridselModel_Row = 0;

	public static final int GridselModel_Check = 1;

	public String title;

	public int gridType;

	public String gridId;

	public int width;

	public int height;

	public int gridsm;

	public List gridColumns;

	public String[][] griddata;

	public String tbars;

	public int pagsize;

	public String otherScript;

	public int clicksToEdit = 1;

	public boolean frame = true;

	public String tableName;

	public String gridJsCode = "";

	public String gridJsDs = "";

	public void ExtGridUtil() {
	}

	/**
	 * @param gridId
	 *            设置grid的Id 及其绑定的DIV的id
	 * 
	 */
	public void ExtGridUtil(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * @param gridId
	 *            设置grid的Id 及其绑定的DIV的id
	 * @param sql
	 *            取数的方法
	 */
	public void ExtGridUtil(String gridId, ResultSetList rsl) {
		this.gridId = gridId;
		addColumn(new GridColumn(GridColumn.ColType_Rownum));
		for (int i = 0; i < rsl.getColumnNames().length; i++) {
			GridColumn gc = new GridColumn(rsl.getColumnNames()[i], rsl
					.getColumnNames()[i]);
			if ("id".equals(rsl.getColumnNames()[i].toLowerCase())) {
				gc.setDefaultValue("0");
				gc.setHidden(true);
			} else {
				String extType = OratypeOfExt(rsl.getColumnTypes()[i]);
				gc.setDataType(extType);
				if ("float".equals(extType)) {
					gc.setEditor(new NumberField());
					((NumberField) gc.editor).setDecimalPrecision(rsl
							.getColScales()[i]);
				} else if ("string".equals(extType)) {
					gc.setEditor(new TextField());
					((TextField) gc.editor)
							.setMaxLength(rsl.getColPrecisions()[i]);
				} else if ("date".equals(extType)) {
					gc.setEditor(new DateField());
					gc.setRenderer(GridColumn.Renderer_Date);
				}
				if (gc.editor != null) {
					gc.editor.allowBlank = rsl.isNullable(i);
				}
			}
			addColumn(gc);
		}
		initData(rsl.getRows(), rsl.getColumnCount());
		while (rsl.next()) {
			setRowData(rsl.getRow(), rsl.getRowString());
		}
	}

	public String OratypeOfExt(String oratype) {
		if ("NUMBER".equals(oratype)) {
			return "float";
		}
		if ("DATE".equals(oratype)) {
			return "date";
		}
		// if(oratype.equals(anObject)) {

		// }
		return "string";
	}

	/**
	 * @return 返回grid的类型
	 */
	public int getGridType() {
		return gridType;
	}

	/**
	 * @param gridType
	 *            设置grid的类型 Gridstyle_Read,Gridstyle_Edit
	 */
	public void setGridType(int gridType) {
		this.gridType = gridType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            grid的宽度
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	/**
	 * @return 返回页面行数
	 */
	public int getPagSize() {
		return pagsize;
	}

	/**
	 * @param pagsize
	 *            页面行数 设置分页
	 */
	public void addPaging(int pagsize) {
		this.pagsize = pagsize;
	}

	/**
	 * @return 返回grid 的行选择模式
	 */
	public int getGridSelModel() {
		return gridsm;
	}

	/**
	 * @param selmodel
	 *            grid行选择模式
	 *            GridselModel_None,GridselModel_Row,GridselModel_Check
	 */
	public void setGridSelModel(int selmodel) {
		gridsm = selmodel;
	}

	/**
	 * @return 返回列的集合
	 */
	public List getGridColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		return gridColumns;
	}

	/**
	 * @param column
	 *            增加一列
	 */
	public void addColumn(GridColumn column) {
		getGridColumns().add(column);
	}

	/**
	 * @param colindex
	 *            列索引
	 * @return 返回一列
	 */
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

	public int getDataColumnCount() {
		int count = 0;
		for (int c = 0; c < getGridColumns().size(); c++) {
			if (((GridColumn) getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @return 返回按钮的集合
	 */
	// public List getGridButtons() {
	// if(gridbuttons == null) {
	// gridbuttons = new ArrayList();
	// }
	// return gridbuttons;
	// }
	/**
	 * @param gridButtonType
	 *            按钮类型
	 * @param tapestryBtnId
	 *            对应的tapestry按钮ID
	 */
	public void addToolbarButton(int gridButtonType, String tapestryBtnId) {
		GridButton gb = new GridButton(gridButtonType, gridId,
				getGridColumns(), tapestryBtnId);
		addTbarBtn(gb);
	}

	public void addTbarBtn(GridButton gb) {
		addToolbarItem("{" + gb.getScript() + "}");
	}

	public void addTbarText(String text) {
		addToolbarItem("'" + text + "'");
	}

	public void addTbarTreeBtn(String treeid) {
		TextField tf = new TextField();
		tf.setId(treeid + "_text");
		tf.setWidth(100);
		addToolbarItem(tf.getScript());
		StringBuffer bf = new StringBuffer();
		bf.append("{").append("icon:'").append(
				"ext/resources/images/list-items.gif").append("',");
		bf.append("cls: 'x-btn-icon',");
		bf.append("handler:function(){").append(treeid).append(
				"_window.show();}}");
		addToolbarItem(bf.toString());
	}

	public void addToolbarItem(String item) {
		StringBuffer sb = new StringBuffer();
		sb.append(getTbar()).append(item).append(",");
		setTbar(sb.toString());
	}

	/**
	 * 设置grid的全部数据
	 * 
	 * @param data
	 *            String[][]
	 */
	public void setData(String[][] data) {
		griddata = data;
	}

	/**
	 * 初始化Data数组
	 * 
	 * @param rows
	 *            行数
	 */
	public void initData(int rows, int cols) {
		griddata = new String[rows][cols];
	}

	/**
	 * 设置行数据
	 * 
	 * @param rowIndex
	 *            行下标 0 to ...
	 * @param data
	 *            String[] 数据
	 */
	public void setRowData(int rowIndex, String[] data) {
		if (rowIndex >= griddata.length) {
			// 异常
			return;
		}
		griddata[rowIndex] = data;
	}

	/**
	 * 设置每个单元格的值
	 * 
	 * @param rowIndex
	 *            行标 0 to ...
	 * @param colIndex
	 *            列标 0 to ...
	 * @param data
	 *            Stirng 值
	 */
	public void setDataValue(int rowIndex, int colIndex, String data) {
		if (rowIndex >= griddata.length) {
			// 异常
			return;
		}
		if (colIndex >= griddata[rowIndex].length) {
			// 异常
			return;
		}
		griddata[rowIndex][colIndex] = data;
	}

	/**
	 * @return 返回表头的Script
	 */
	private String getColumnsScript() {
		StringBuffer columnsScript = new StringBuffer();
		columnsScript.append("var ").append(gridId).append(
				"_cm = new Ext.grid.ColumnModel([");
		for (int c = 0; c < getGridColumns().size(); c++) {
			GridColumn Gc = (GridColumn) getGridColumns().get(c);

			columnsScript.append(Gc.getScript());
			columnsScript.append(",");
		}
		columnsScript.deleteCharAt(columnsScript.length() - 1);
		columnsScript.append("]);\n ").append(gridId).append(
				"_cm.defaultSortable=false;\n");
		return columnsScript.toString();
	}

	/**
	 * @return 返回新增记录的Script
	 */
	private String getPlantScript() {
		StringBuffer plantScript = new StringBuffer();
		plantScript.append("var ").append(gridId).append(
				"_plant = Ext.data.Record.create([");
		for (int c = 0; c < getGridColumns().size(); c++) {
			if (((GridColumn) getGridColumns().get(c)).coltype == 0) {
				GridColumn gc = ((GridColumn) getGridColumns().get(c));
				plantScript.append("{name: '").append(gc.dataIndex).append(
						"', type:'").append(gc.datatype).append("'},");
			}
		}
		plantScript.deleteCharAt(plantScript.length() - 1);
		plantScript.append("]);\n");
		return plantScript.toString();
	}

	/**
	 * @return 返回Toolbar 的Script
	 */
	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
	}

	/**
	 * @return 返回数据的Script
	 */
	private String getDataScript() {
		StringBuffer djs = new StringBuffer("var " + gridId + "_data = [\n");
		for (int r = 0; r < griddata.length; r++) {
			djs.append("[");
			for (int c = 0; c < griddata[r].length; c++) {
				djs.append("'");
				djs.append(griddata[r][c]);
				djs.append("',");
			}
			djs.deleteCharAt(djs.length() - 1);
			djs.append("],");
		}
		djs.deleteCharAt(djs.length() - 1);
		djs.append("];\n");
		return djs.toString();
	}

	/**
	 * @return 返回数据的解析Script
	 */
	private String getDsScript() {
		StringBuffer dsScript = new StringBuffer();
		dsScript.append("var " + gridId + "_ds = new Ext.data.Store({\n");
		dsScript.append("proxy : new Ext.zr.data.PagingMemoryProxy(" + gridId
				+ "_data),\n");
		dsScript.append("reader: new Ext.data.ArrayReader({}, [\n");
		for (int c = 0; c < getGridColumns().size(); c++) {
			if (((GridColumn) getGridColumns().get(c)).coltype == 0) {
				dsScript.append("{name:'");
				dsScript
						.append(((GridColumn) getGridColumns().get(c)).dataIndex);
				dsScript.append("'},");
			}
		}
		dsScript.deleteCharAt(dsScript.length() - 1);
		dsScript.append("])");
		dsScript.append("});\n");
		return dsScript.toString();
	}

	/**
	 * @return 返回分页的Script
	 */
	public String getPagingScript() {
		StringBuffer pagScript = new StringBuffer();
		pagScript.append("var ").append(gridId).append("_pag").append(
				" = new Ext.PagingToolbar({ \n");
		pagScript.append("pageSize: ").append(getPagSize()).append(",\n");
		pagScript.append("store: ").append(gridId).append("_ds").append(",\n");
		pagScript.append("displayInfo: true,\n");
		pagScript.append("displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',\n");
		pagScript.append("emptyMsg: '没有记录' });\n");
		return pagScript.toString();
	}

	public String getGridPanelName() {
		return getGridType() == 0 ? "GridPanel" : "EditorGridPanel";
	}

	public String getGridScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getColumnsScript()).append("\n");
		gridScript.append(getDataScript()).append("\n");
		gridScript.append(getDsScript()).append("\n");
		gridScript.append(getPagSize() > 0 ? getPagingScript() : "").append(
				"\n");
		gridScript.append(getGridColumns().size() > 0 ? getPlantScript() : "");
		return gridScript.toString();
	}

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript
					.append(gridId)
					.append("_ds.on('datachanged',function(){ ")
					.append(gridId)
					.append(
							"_history = ''; });diancTree_text.setValue(diancTree_treePanel.getSelectionModel().getSelectedNode()==null?'':diancTree_treePanel.getSelectionModel().getSelectedNode().text);");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
	}

	/**
	 * @param sql
	 * 
	 */
	public void setGridDs(String sql) {
		gridJsDs = "var grid1_data = [";
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		int cols = rs.getColumnCount();
		while (rs.next()) {
			gridJsDs += "[";
			for (int i = 0; i < cols; i++) {
				gridJsDs += "'" + rs.getString(i) + "'";
				if (i + 1 < cols) {
					gridJsDs += ",";
				}
			}
			gridJsDs += "]";
			if (rs.getRow() + 1 < rs.getRows()) {
				gridJsDs += ",";
			}
		}
		gridJsDs += "];";
	}

	public String getDataset() {
		return gridJsDs;
	}

	public void Save(String strchange, Visit visit) {
		if (strchange == null || "".equals(strchange)) {
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag =0;
		// ResultSetList delrsl = getDeleteResultSet(strchange);
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql3 = new StringBuffer("begin \n");
		// String tableib = "";
		String diancxxb_id = "select decode(max(id),0,100,(max(id)+1)) as id from diancxxb";
		ResultSet rs = con.getResultSet(diancxxb_id);
		try {
			while (rs.next()) {
				diancxxb_id = rs.getString("id");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		ResultSetList mdrsl = getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append(diancxxb_id);
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if (i == 8) {
						String daoz = mdrsl.getString(i);
						if (daoz.equals("") || daoz.equals("请选择")) {
						} else {
							sql3
									.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
							sql3.append("getnewid(").append(
									visit.getDiancxxb_id()).append("),");
							sql3.append(mdrsl.getString("ID")).append(",");
							sql3.append(getValueSql(getColumn(mdrsl
									.getColumnNames()[i]), mdrsl.getString(i)));
							sql3.append(",'车站');\n");
						}
					} else if (i == 9) {
						String daog = mdrsl.getString(i);
						if (daog.equals("") || daog.equals("请选择")) {
						} else {
							sql3
									.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
							sql3.append("getnewid(").append(
									visit.getDiancxxb_id()).append("),");
							sql3.append(mdrsl.getString("ID")).append(",");
							sql3.append(getValueSql(getColumn(mdrsl
									.getColumnNames()[i]), mdrsl.getString(i)));
							sql3.append(",'港口');\n");
						}

					} else {
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						if(mdrsl.getColumnNames()[i].toUpperCase().equals("FUID")){
							sql2.append(",").append("nvl((select id from diancxxb where mingc='"+mdrsl.getString("FUID")+"'),(select fuid from diancxxb where id="+this.getTreeid()+"))");
						}else{
							sql2.append(",").append(
									getValueSql(
											getColumn(mdrsl.getColumnNames()[i]),
											mdrsl.getString(i)));
						}
						
					}
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if (i == 8) {
						sql3.append("delete diancdzb where diancxxb_id ="
								+ mdrsl.getString("ID") + " and leib='车站';\n");
						sql3
								.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
						sql3.append("getnewid(").append(visit.getDiancxxb_id())
								.append("),");
						sql3.append(mdrsl.getString("ID")).append(",");
						sql3.append(getValueSql(getColumn(mdrsl
								.getColumnNames()[i]), mdrsl.getString(i)));
						sql3.append(",'车站');\n");
					} else if (i == 9) {
						sql3.append("delete diancdzb where diancxxb_id ="
								+ mdrsl.getString("ID") + " and leib='港口';\n");
						sql3
								.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
						sql3.append("getnewid(").append(visit.getDiancxxb_id())
								.append("),");
						sql3.append(mdrsl.getString("ID")).append(",");
						sql3.append(getValueSql(getColumn(mdrsl
								.getColumnNames()[i]), mdrsl.getString(i)));
						sql3.append(",'港口');\n");
					} else {
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						if(mdrsl.getColumnNames()[i].toUpperCase().equals("FUID")){
							sql.append("nvl((select id from diancxxb where mingc='"+mdrsl.getString("FUID")+"'),(select fuid from diancxxb where mingc='"+mdrsl.getString("MINGC")+"'))").append(",");
						}else{
							sql.append(
									getValueSql(
											getColumn(mdrsl.getColumnNames()[i]),
											mdrsl.getString(i))).append(",");
						}
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		sql.append("end;");
		sql3.append("end;");
		con.getUpdate(sql.toString());
		con.getUpdate(sql3.toString());
		if (flag==-1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sql.toString());
			setMsg(ErrorMessage.InsertDatabaseFail);
			return;
		}
		if(flag!=-1){
			setMsg("保存成功！");
		}
		con.commit();
		con.Close();
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value == null ? "" : "'" + value + "'";
				}
			} else {

				return "'" + value == null ? "" : "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}

	public String getGridHtml() {
		StringBuffer sb = new StringBuffer();
		for (int c = 0; c < getGridColumns().size(); c++) {
			GridColumn gc = (GridColumn) getGridColumns().get(c);
			if (gc.combo != null) {
				sb.append(gc.getComboHtml()).append("\n");
			}
		}
		return sb.toString();
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

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		tbars = "";
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
				.getResultSetList("SELECT dc.ID, dc.xuh, dc.bianm, dc.mingc, dc.quanc, dc.piny,\n"
						+ "       sf.quanc AS shengfb_id,\n"
						+ "       DECODE ((SELECT mingc\n"
						+ "                  FROM diancxxb\n"
						+ "                 WHERE ID = dc.fuid), NULL, '',\n"
						+ "               (SELECT mingc\n"
						+ "                  FROM diancxxb\n"
						+ "                 WHERE ID = dc.fuid)) AS fuid,\n"
						+ "       DECODE ((SELECT cz.quanc\n"
						+ "                  FROM chezxxb cz, diancdzb dz\n"
						+ "                 WHERE cz.ID = dz.chezxxb_id\n"
						+ "                   AND dz.leib = '车站'\n"
						+ "                   AND dz.diancxxb_id = dc.ID),\n"
						+ "               NULL, '请选择',\n"
						+ "               (SELECT cz.quanc\n"
						+ "                  FROM chezxxb cz, diancdzb dz\n"
						+ "                 WHERE cz.ID = dz.chezxxb_id\n"
						+ "                   AND dz.leib = '车站'\n"
						+ "                   AND dz.diancxxb_id = dc.ID)\n"
						+ "              ) AS daoz,\n"
						+ "       DECODE ((SELECT cz.quanc\n"
						+ "                  FROM chezxxb cz, diancdzb dz\n"
						+ "                 WHERE cz.ID = dz.chezxxb_id\n"
						+ "                   AND dz.leib = '港口'\n"
						+ "                   AND dz.diancxxb_id = dc.ID),\n"
						+ "               NULL, '请选择',\n"
						+ "               (SELECT cz.quanc\n"
						+ "                  FROM chezxxb cz, diancdzb dz\n"
						+ "                 WHERE cz.ID = dz.chezxxb_id\n"
						+ "                   AND dz.leib = '港口'\n"
						+ "                   AND dz.diancxxb_id = dc.ID)\n"
						+ "              ) AS daog,\n"
						+ "       dc.diz, dc.youzbm, dc.zongj, dc.ranlcdh, dc.zhuangjrl, dc.zuidkc,\n"
						+ "       dc.zhengccb, dc.xianfhkc, dc.rijhm, dc.jingjcmsx, dc.jingjcmxx,\n"
						+ "       dc.dongcmzb, dc.faddbr, dc.weitdlr, dc.kaihyh, dc.zhangh, dc.dianh,\n"
						+ "       dc.shuih, dc.jiexfs, dc.jiexx, dc.jiexnl, dc.caiyfs, dc.jilfs, ranlfzr,\n"
						+ "       lianxdz, jingjcml,\n"
						+ "       DECODE (dc.jib, 1, '集团', 2, '分公司', 3, '电厂') AS jib, dc.beiz,\n"
						+ "       lb.mingc AS dianclbb_id, dc.jiesbdcbs\n"
						+ "  FROM diancxxb dc, shengfb sf, dianclbb lb\n"
						+ " WHERE dc.shengfb_id = sf.ID AND dc.dianclbb_id = lb.ID(+) "
						+ str + " order by dc.xuh,dc.id, dc.fuid, dc.jib");
		ExtGridUtil("gridDiv", rsl);
		setHeight(500);
		setWidth(1500);
		setTableName("diancxxb");
		getColumn("xuh").setHeader("序号");
		getColumn("xuh").setWidth(50);
		getColumn("bianm").setHeader("编码");
		getColumn("bianm").setWidth(80);
		getColumn("mingc").setHeader("简称");
		getColumn("mingc").setWidth(80);
		getColumn("quanc").setHeader("全称");
		getColumn("quanc").setWidth(150);
		getColumn("piny").setHeader("拼音");
		getColumn("piny").setWidth(50);
		getColumn("shengfb_id").setHeader("省份");
		getColumn("shengfb_id").setWidth(80);
		getColumn("fuid").setHeader("上级公司");
		getColumn("fuid").setWidth(80);
		getColumn("daoz").setHeader("到站");
		getColumn("daoz").setWidth(65);
		getColumn("daog").setHeader("到港");
		getColumn("daog").setWidth(65);
		getColumn("diz").setHeader("地址");
		getColumn("youzbm").setHeader("邮政编码");
		getColumn("zongj").setHeader("总机");
		getColumn("ranlcdh").setHeader("燃料处电话");
		getColumn("zhuangjrl").setHeader("装机容量(MW)");
		getColumn("zhuangjrl").setDefaultValue("0");
		getColumn("zuidkc").setHeader("最大库存(吨)");
		getColumn("zuidkc").setDefaultValue("0");
		getColumn("zhengccb").setHeader("正常储备(吨)");
		getColumn("zhengccb").setDefaultValue("0");
		getColumn("xianfhkc").setHeader("限负荷库存(吨)");
		getColumn("xianfhkc").setDefaultValue("0");
		getColumn("rijhm").setHeader("日均耗煤(吨)");
		getColumn("rijhm").setDefaultValue("0");
		getColumn("jingjcmsx").setHeader("经济储煤上限(吨)");
		getColumn("jingjcmsx").setDefaultValue("0");
		getColumn("jingjcmxx").setHeader("经济储煤下限(吨)");
		getColumn("jingjcmxx").setDefaultValue("0");
		getColumn("dongcmzb").setHeader("冬储煤指标(吨)");
		getColumn("dongcmzb").setDefaultValue("0");
		getColumn("faddbr").setHeader("法定代表人");
		getColumn("weitdlr").setHeader("委托代理人");
		getColumn("kaihyh").setHeader("开户银行");
		getColumn("zhangh").setHeader("帐号");
		getColumn("dianh").setHeader("电话");
		getColumn("shuih").setHeader("税号");
		getColumn("jiexfs").setHeader("接卸方式");
		getColumn("jiexx").setHeader("接卸线");
		getColumn("jiexx").setDefaultValue("1");
		getColumn("jiexnl").setHeader("接卸能力(车)");
		getColumn("jiexnl").setDefaultValue("0");
		getColumn("caiyfs").setHeader("采样方式");
		getColumn("jilfs").setHeader("计量方式");
		getColumn("ranlfzr").setHeader("燃料负责人");
		getColumn("lianxdz").setHeader("联系地址");
		getColumn("jingjcml").setHeader("警戒存煤(吨)");
		getColumn("jingjcml").setDefaultValue("0");

		getColumn("jib").setHeader("级别");
		getColumn("beiz").setHeader("备注");
		getColumn("dianclbb_id").setHeader("电厂类别");
		getColumn("jiesbdcbs").setHeader("结算单电厂标识");
		addPaging(18);
		getColumn("diz").setHidden(true);
		getColumn("youzbm").setHidden(true);
		getColumn("zongj").setHidden(true);
		getColumn("ranlcdh").setHidden(true);
		getColumn("zhuangjrl").setHidden(true);
		getColumn("zuidkc").setHidden(true);
		getColumn("zhengccb").setHidden(true);
		getColumn("xianfhkc").setHidden(true);
		getColumn("rijhm").setHidden(true);
		getColumn("jingjcmsx").setHidden(true);
		getColumn("jingjcmxx").setHidden(true);
		getColumn("dongcmzb").setHidden(true);
		getColumn("faddbr").setHidden(true);
		getColumn("weitdlr").setHidden(true);
		getColumn("kaihyh").setHidden(true);
		getColumn("zhangh").setHidden(true);
		getColumn("dianh").setHidden(true);
		getColumn("shuih").setHidden(true);
		getColumn("jiexfs").setHidden(true);
		getColumn("jiexx").setHidden(true);
		getColumn("jiexnl").setHidden(true);
		getColumn("caiyfs").setHidden(true);
		getColumn("jilfs").setHidden(true);
		getColumn("ranlfzr").setHidden(true);
		getColumn("lianxdz").setHidden(true);
		getColumn("jingjcml").setHidden(true);
		getColumn("jib").setHidden(true);
		getColumn("beiz").setHidden(true);
		getColumn("dianclbb_id").setHidden(true);
		getColumn("jiesbdcbs").setHidden(true);
		List caiyfs = new ArrayList();
		caiyfs.add(new IDropDownBean(1, "人工"));
		caiyfs.add(new IDropDownBean(2, "机械"));
		getColumn("caiyfs").setEditor(new ComboBox());
		getColumn("caiyfs").setComboEditor(gridId, new IDropDownModel(caiyfs));
		getColumn("caiyfs").setReturnId(false);
		getColumn("caiyfs").setDefaultValue("人工");
		List jilfs = new ArrayList();
		caiyfs.add(new IDropDownBean(1, "过衡"));
		caiyfs.add(new IDropDownBean(2, "检尺"));
		getColumn("jilfs").setEditor(new ComboBox());
		getColumn("jilfs").setComboEditor(gridId, new IDropDownModel(jilfs));
		getColumn("jilfs").setReturnId(false);
		getColumn("jilfs").setDefaultValue("过衡");
		List jib = new ArrayList();
		jib.add(new IDropDownBean(1, "集团"));
		jib.add(new IDropDownBean(2, "分公司"));
		jib.add(new IDropDownBean(3, "电厂"));
		getColumn("jib").setEditor(new ComboBox());
		getColumn("jib").setComboEditor(gridId, new IDropDownModel(jib));
		getColumn("jib").setDefaultValue("请选择");
		getColumn("shengfb_id").setEditor(new ComboBox());
		getColumn("shengfb_id").setComboEditor(gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		
		getColumn("fuid").setEditor(new ComboBox());
//		getColumn("fuid").setComboEditor(
//				gridId,
//				new IDropDownModel(
//						"select id,mingc from diancxxb where jib < 3"));
		getColumn("fuid").setComboEditor(
				gridId,
				new IDropDownModel(
						"select id,mingc from diancxxb "));
		getColumn("fuid").editor.allowBlank=true;
		getColumn("daoz").setEditor(new ComboBox());
		getColumn("daoz").setComboEditor(
				gridId,
				new IDropDownModel(
						"select id,quanc from chezxxb where leib = '车站'"));
		getColumn("daoz").setDefaultValue("请选择");
		getColumn("daog").setEditor(new ComboBox());
		getColumn("daog").setComboEditor(
				gridId,
				new IDropDownModel(
						"select id,quanc from chezxxb where leib = '港口'"));
		getColumn("daog").setDefaultValue("请选择");
		getColumn("dianclbb_id").setEditor(new ComboBox());
		getColumn("dianclbb_id").setComboEditor(gridId,
				new IDropDownModel("select id,mingc from dianclbb"));
		addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		
		StringBuffer sb=new StringBuffer();
		sb.append("select id, mingc, level as jib \n").append("  from diancxxb \n").append(" start with id =")
		.append(((Visit) getPage().getVisit()).getDiancxxb_id()).append("\n connect by fuid = prior id \n").append(" order SIBLINGS by xuh");
		String sql = sb.toString();
		if(con.getHasIt("select * from diancxxb where ranlgs = 1 and id="+((Visit) getPage().getVisit()).getDiancxxb_id())) {
			sql = sql.replaceAll("fuid", "shangjgsid");
		}
		ResultSetList rs = con.getResultSetList(sql); 
		
		boolean t=false;
		while(rs.next()){//当选择 子电厂  对其上级单位更改了 而此时 单位树形结构中 可能就没有此显示了，默认为登陆人员  
			if(rs.getString("id").equals(this.getTreeid())){
				t=true;
			}
		}
		if(!t){
			etu.defaultSelectid=((Visit) getPage().getVisit()).getDiancxxb_id()+"";
		}
		setTree(etu);
		addTbarTreeBtn("diancTree");
		addTbarText("-");
		addToolbarButton(GridButton.ButtonType_Insert, null);
//		 addToolbarButton(GridButton.ButtonType_Delete, "");
		addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String s = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Diancreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		addToolbarItem("{"
				+ new GridButton("打印", "function (){" + s + "}",SysConstant.Btn_Icon_Print).getScript()
				+ "}");

		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	// /////////////////////////////到站////////////////////////////////
	public IDropDownBean getDaozValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDaozModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDaozValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setDaozModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getDaozModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDaozModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getDaozModels() {
		String sql = "select id,quanc as daoz from chezxxb where leib='车站'";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "请选择"));
		return;
	}

	// ///////////////////////////////到港//////////////////////////////////////
	public IDropDownBean getDaogValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getDaogModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setDaogValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setDaogModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getDaogModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDaogModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getDaogModels() {
		String sql = "select id,quanc as daoz from chezxxb where leib='港口'";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "请选择"));
		return;
	}

	// /////////////////////////////电厂类别///////////////////////////////
	public IDropDownBean getDianclbb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getDianclbb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setDianclbb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setDianclbb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getDianclbb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getDianclbb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getDianclbb_idModels() {
		String sql = "select id, mingc from dianclbb order by xuh, mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, "请选择"));
		return;
	}

	// ///////////////////////////////级别//////////////////////////////////////
	public IDropDownBean getJibValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJibModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJibValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJibModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJibModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJibModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJibModels() {
		String sql = "select jb.id, jb.mingc\n"
				+ "  from (select 1 as id, decode(1, 1, '集团') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 2 as id, decode(1, 1, '公司') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 3 as id, decode(1, 1, '电厂') as mingc from dual) jb\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql,"请选择"));
		return;
	}

	// //////////////////////////上级单位///////////////////////////////////
	public IDropDownBean getFuidValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getFuidModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setFuidValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setFuidModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getFuidModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getFuidModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	
	public void getFuidModels() {
		//String sql = "select id, mingc from diancxxb where jib < 3\n";
		StringBuffer bf=new StringBuffer();
		bf.append(" select id, mingc from diancxxb where jib < (select jib from diancxxb where id="+this.getTreeid()+") \n");
		bf.append(" union \n");
		bf.append(" select distinct nvl((select fuid from diancxxb where id=d.id),-1) id, \n");
		bf.append(" nvl((select mingc from diancxxb where id=( select distinct fuid from diancxxb where id=d.id)),'') mingc \n");
		bf.append(" from diancxxb d where d.id="+this.getTreeid()+" and not exists \n");
		bf.append(" (select id, mingc from diancxxb where jib < (select jib from diancxxb where id="+this.getTreeid()+")) \n");
		bf.append("  union \n");
		bf.append("  select  id,mingc from diancxxb where id = (select di.fuid from diancxxb di where di.id="+this.getTreeid()+") and jib=3 \n");
		bf.append(" and  exists  \n");
		bf.append(" (select id,mingc from diancxxb where fuid="+this.getTreeid()+")");
		
		String sql=bf.toString();
		
		
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql));
		return;
	}

	// ////////////////////////////省份///////////////////////////////////////
	public IDropDownBean getShengfb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getShengfb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setShengfb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setShengfb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getShengfb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getShengfb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getShengfb_idModels() {
		String sql = "select id,quanc from shengfb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, "请选择"));
		return;
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

	// //////////////////////////////////////////////////////////////////////////
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
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			
			gridColumns = null;

		}
		visit.setProSelectionModel6(null);
		visit.setDropDownBean6(null);
		getSelectData();
	}

	public ResultSetList getDeleteResultSet(String strchange) {
		ResultSetList rsl = new ResultSetList();
		List list = getElmentList(strchange);
		List cols = getUpdateColumns();
		String[] columnNames = new String[cols.size()];
		for (int i = 0; i < cols.size(); i++) {
			columnNames[i] = ((GridColumn) cols.get(i)).dataIndex;
		}
		rsl.setColumnNames(columnNames);
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			if ("D".equals(e.getChild("sign").getText())) {
				String[] newrecord = new String[rsl.getColumnCount()];
				for (int j = 0; j < rsl.getColumnCount(); j++) {
					newrecord[j] = e.getChildText(rsl.getColumnNames()[j]);
				}
				rsl.getResultSetlist().add(newrecord);
			}
		}
		return rsl;
	}

	public ResultSetList getModifyResultSet(String strchange) {
		ResultSetList rsl = new ResultSetList();
		List list = getElmentList(strchange);
		List cols = getUpdateColumns();
		String[] columnNames = new String[cols.size()];
		for (int i = 0; i < cols.size(); i++) {
			columnNames[i] = ((GridColumn) cols.get(i)).dataIndex;
		}
		rsl.setColumnNames(columnNames);
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			if ("U".equals(e.getChildText("sign"))) {
				String[] newrecord = new String[rsl.getColumnCount()];
				for (int j = 0; j < rsl.getColumnCount(); j++) {
					newrecord[j] = e.getChildText(rsl.getColumnNames()[j]);
				}
				rsl.getResultSetlist().add(newrecord);
			}
		}
		return rsl;
	}

	public List getElmentList(String strchange) {
		strchange = getChange(strchange);
		StringReader sr = new StringReader(strchange);
		InputSource is = new InputSource(sr);
		Document doc = null;
		try {
			doc = (new SAXBuilder()).build(is);
		} catch (JDOMException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		return root.getChildren();
	}

	public List getUpdateColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		List uplist = new ArrayList();
		for (int i = 0; i < gridColumns.size(); i++) {
			GridColumn gc = (GridColumn) gridColumns.get(i);
			if (gc.coltype == GridColumn.ColType_default) {
				if (gc.update) {
					uplist.add(gc);
				}
			}
		}
		return uplist;
	}

	public String getChange(String change) {
		change = change.replaceAll("'", "''");
		return change;
	}
}
