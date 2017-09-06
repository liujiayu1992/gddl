package com.zhiren.jt.zdt.diancext;

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

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
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

public class Diancext extends BasePage implements PageValidateListener {
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
	 *            ����grid��Id ����󶨵�DIV��id
	 * 
	 */
	public void ExtGridUtil(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * @param gridId
	 *            ����grid��Id ����󶨵�DIV��id
	 * @param sql
	 *            ȡ���ķ���
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
	 * @return ����grid������
	 */
	public int getGridType() {
		return gridType;
	}

	/**
	 * @param gridType
	 *            ����grid������ Gridstyle_Read,Gridstyle_Edit
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
	 *            grid�Ŀ��
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
	 * @return ����ҳ������
	 */
	public int getPagSize() {
		return pagsize;
	}

	/**
	 * @param pagsize
	 *            ҳ������ ���÷�ҳ
	 */
	public void addPaging(int pagsize) {
		this.pagsize = pagsize;
	}

	/**
	 * @return ����grid ����ѡ��ģʽ
	 */
	public int getGridSelModel() {
		return gridsm;
	}

	/**
	 * @param selmodel
	 *            grid��ѡ��ģʽ
	 *            GridselModel_None,GridselModel_Row,GridselModel_Check
	 */
	public void setGridSelModel(int selmodel) {
		gridsm = selmodel;
	}

	/**
	 * @return �����еļ���
	 */
	public List getGridColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		return gridColumns;
	}

	/**
	 * @param column
	 *            ����һ��
	 */
	public void addColumn(GridColumn column) {
		getGridColumns().add(column);
	}

	/**
	 * @param colindex
	 *            ������
	 * @return ����һ��
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
	 * @return ���ذ�ť�ļ���
	 */
	// public List getGridButtons() {
	// if(gridbuttons == null) {
	// gridbuttons = new ArrayList();
	// }
	// return gridbuttons;
	// }
	/**
	 * @param gridButtonType
	 *            ��ť����
	 * @param tapestryBtnId
	 *            ��Ӧ��tapestry��ťID
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
	 * ����grid��ȫ������
	 * 
	 * @param data
	 *            String[][]
	 */
	public void setData(String[][] data) {
		griddata = data;
	}

	/**
	 * ��ʼ��Data����
	 * 
	 * @param rows
	 *            ����
	 */
	public void initData(int rows, int cols) {
		griddata = new String[rows][cols];
	}

	/**
	 * ����������
	 * 
	 * @param rowIndex
	 *            ���±� 0 to ...
	 * @param data
	 *            String[] ����
	 */
	public void setRowData(int rowIndex, String[] data) {
		if (rowIndex >= griddata.length) {
			// �쳣
			return;
		}
		griddata[rowIndex] = data;
	}

	/**
	 * ����ÿ����Ԫ���ֵ
	 * 
	 * @param rowIndex
	 *            �б� 0 to ...
	 * @param colIndex
	 *            �б� 0 to ...
	 * @param data
	 *            Stirng ֵ
	 */
	public void setDataValue(int rowIndex, int colIndex, String data) {
		if (rowIndex >= griddata.length) {
			// �쳣
			return;
		}
		if (colIndex >= griddata[rowIndex].length) {
			// �쳣
			return;
		}
		griddata[rowIndex][colIndex] = data;
	}

	/**
	 * @return ���ر�ͷ��Script
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
	 * @return ����������¼��Script
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
	 * @return ����Toolbar ��Script
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
	 * @return �������ݵ�Script
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
	 * @return �������ݵĽ���Script
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
	 * @return ���ط�ҳ��Script
	 */
	public String getPagingScript() {
		StringBuffer pagScript = new StringBuffer();
		pagScript.append("var ").append(gridId).append("_pag").append(
				" = new Ext.PagingToolbar({ \n");
		pagScript.append("pageSize: ").append(getPagSize()).append(",\n");
		pagScript.append("store: ").append(gridId).append("_ds").append(",\n");
		pagScript.append("displayInfo: true,\n");
		pagScript.append("displayMsg: '��ʾ�� {0} ���� {1} ����¼��һ�� {2} ��',\n");
		pagScript.append("emptyMsg: 'û�м�¼' });\n");
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		ResultSetList mdrsl = getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append(diancxxb_id);
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
//					if (i == 8) {
//						sql3
//								.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
//						sql3.append("getnewid(").append(visit.getDiancxxb_id())
//								.append("),");
//						sql3.append(mdrsl.getString("ID")).append(",");
//						sql3.append(getValueSql(getColumn(mdrsl
//								.getColumnNames()[i]), mdrsl.getString(i)));
//						sql3.append(",'��վ');\n");
//					} else if (i == 9) {
//						sql3
//								.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
//						sql3.append("getnewid(").append(visit.getDiancxxb_id())
//								.append("),");
//						sql3.append(mdrsl.getString("ID")).append(",");
//						sql3.append(getValueSql(getColumn(mdrsl
//								.getColumnNames()[i]), mdrsl.getString(i)));
//						sql3.append(",'�ۿ�');\n");
//					} else {
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(
								getValueSql(
										getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					//}
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
//					if (i == 8) {
//						sql3.append("delete diancdzb where diancxxb_id ="
//								+ mdrsl.getString("ID") + " and leib='��վ';\n");
//						sql3
//								.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
//						sql3.append("getnewid(").append(visit.getDiancxxb_id())
//								.append("),");
//						sql3.append(mdrsl.getString("ID")).append(",");
//						sql3.append(getValueSql(getColumn(mdrsl
//								.getColumnNames()[i]), mdrsl.getString(i)));
//						sql3.append(",'��վ');\n");
//					} else if (i == 9) {
//						sql3.append("delete diancdzb where diancxxb_id ="
//								+ mdrsl.getString("ID") + " and leib='�ۿ�';\n");
//						sql3
//								.append("insert into diancdzb (id,diancxxb_id,chezxxb_id,leib) values(");
//						sql3.append("getnewid(").append(visit.getDiancxxb_id())
//								.append("),");
//						sql3.append(mdrsl.getString("ID")).append(",");
//						sql3.append(getValueSql(getColumn(mdrsl
//								.getColumnNames()[i]), mdrsl.getString(i)));
//						sql3.append(",'�ۿ�');\n");
//					} else {
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getValueSql(
										getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
//					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		sql.append("end;");
		//sql3.append("end;");
		con.getUpdate(sql.toString());
		//con.getUpdate(sql3.toString());
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

//	private String treeid = "";

//	public String getTreeid() {
//
//		if (treeid.equals("")) {
//
//			treeid = String.valueOf(((Visit) getPage().getVisit())
//					.getDiancxxb_id());
//		}
//		return treeid;
//	}
//
//	public void setTreeid(String treeid) {
//
//		if (!this.treeid.equals(treeid)) {
//
//			((Visit) getPage().getVisit()).setboolean3(true);
//			this.treeid = treeid;
//		}
//	}

//	-----------------------------------------------
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
//	-----------------------------------------------
	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		tbars = "";
		gridColumns = null;
		String str = "";
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";
		}
		String sqlstr="SELECT distinct dc.ID, dc.xuh, dc.bianm, dc.jitbm, dc.mingc, dc.quanc, dc.piny,sf.quanc AS shengfb_id,cq.mingc as chanqxzb_id,dc.yunsfs,"//l.xingm as lianxrb_id," 
			+ "DECODE ((SELECT mingc FROM diancxxb WHERE ID = dc.shangjgsid), NULL, ''," 
			+ "(SELECT mingc FROM diancxxb WHERE ID = dc.shangjgsid)) AS shangjgsid," 
			+ "DECODE (dc.ranlgs, 1, '��', 2 , '��' ) AS ranlgs,\n"
			+ "DECODE ((SELECT mingc FROM diancxxb WHERE ID = dc.fuid), NULL, ''," 
			+"(SELECT mingc FROM diancxxb WHERE ID = dc.fuid)) AS fuid,\n"
			+ "c1.mingc AS daoz,dc.diz,\n"
			+ "c2.mingc AS daog,\n"
			+ "dc.lianxdz, dc.youzbm, dc.zongj,dc.zhuangjrl, dc.zuidkc,dc.xianfhkc, dc.rijhm,dc.faddbr, dc.weitdlr, dc.kaihyh, dc.zhangh,\n"
			+ "dc.dianh,dc.shuih, dc.jiexfs, dc.jiexx, dc.jiexnl, dc.caiyfs, dc.jilfs, jingjcml,\n"
			+ "DECODE (dc.jib, 1, '����', 2, '�ֹ�˾', 3, '�糧') AS jib, dc.beiz, lb.mingc AS dianclbb_id, dc.jiesbdcbs ,dc.zuidikc,\n"
			+ "dc.dongjjjcmsx,dc.dongjjjcmxx,dc.xiajjjcmsx,dc.xiajjjcmxx,dc.chumqcmzb,dc.jiangcqcmzb,dc.xitzjh,dc.shihzjh,dc.chuanz \n"
			+ "FROM diancxxb dc, shengfb sf, dianclbb lb,chanqxzb cq,lianxrb l,chezxxb c1,chezxxb c2 \n"
			+ "WHERE dc.shengfb_id = sf.ID AND dc.dianclbb_id = lb.ID "+str+" and dc.chanqxzb_id=cq.id(+)   and dc.daoz=c1.id(+) and dc.daog=c2.id(+)";// and dc.lianxrb_id=l.id(+)";
		ResultSetList rsl = con
				.getResultSetList(sqlstr);
		ExtGridUtil("gridDiv", rsl);
		setHeight(500);
		setWidth(1500);
		setTableName("diancxxb");
		getColumn("xuh").setHeader("���");
		getColumn("bianm").setHeader("���ܱ���");
		getColumn("jitbm").setHeader("���ű���");
		getColumn("shangjgsid").setHeader("�ϼ���˾");
		getColumn("dianclbb_id").setHeader("�糧���");
		getColumn("mingc").setHeader("���");
		getColumn("quanc").setHeader("ȫ��");
		getColumn("piny").setHeader("ƴ��");
		getColumn("shengfb_id").setHeader("ʡ��");
		getColumn("chanqxzb_id").setHeader("��Ȩ����");
		getColumn("chanqxzb_id").setDefaultValue("1");

//		getColumn("lianxrb_id").setHeader("��ϵ��");
//		getColumn("lianxrb_id").setDefaultValue("xxx");
		getColumn("fuid").setHeader("�ϼ���λ");
		getColumn("shangjgsid").setDefaultValue(visit.getDiancmc());
		

		getColumn("daoz").setHeader("��վ");
		getColumn("daog").setHeader("����");
		getColumn("lianxdz").setHeader("��ϵ��ַ");
		getColumn("diz").setHeader("��ַ");
		getColumn("youzbm").setHeader("��������");
		getColumn("zongj").setHeader("�ܻ�");
		getColumn("zhuangjrl").setHeader("װ������(MW)");
		getColumn("zhuangjrl").setDefaultValue("0");
		getColumn("zuidkc").setHeader("�����(��)");
		getColumn("zuidkc").setDefaultValue("0");
		getColumn("xianfhkc").setHeader("�޸��ɿ��(��)");
		getColumn("xianfhkc").setDefaultValue("0");
		getColumn("rijhm").setHeader("�վ���ú(��)");
		getColumn("rijhm").setDefaultValue("0");
		getColumn("faddbr").setHeader("����������");
		getColumn("weitdlr").setHeader("ί�д�����");
		getColumn("kaihyh").setHeader("��������");
		getColumn("zhangh").setHeader("�ʺ�");
		getColumn("dianh").setHeader("�绰");
		getColumn("shuih").setHeader("˰��");
		getColumn("jiexfs").setHeader("��ж��ʽ");
		getColumn("jiexx").setHeader("��ж��");
		getColumn("jiexx").setDefaultValue("1");
		getColumn("jiexnl").setHeader("��ж����(��)");
		getColumn("jiexnl").setDefaultValue("0");
		getColumn("caiyfs").setHeader("������ʽ");
		getColumn("jilfs").setHeader("������ʽ");
		getColumn("jingjcml").setHeader("������(��)");
		getColumn("jingjcml").setDefaultValue("0");
		getColumn("zuidikc").setHeader("��Ϳ��(��)");
		getColumn("zuidikc").setDefaultValue("0");
		getColumn("zuidikc").setHidden(true);
		getColumn("dongjjjcmsx").setHeader("�������ô�ú����");
		getColumn("dongjjjcmxx").setHeader("������ú��������");
		getColumn("xiajjjcmsx").setHeader("�ļ����ô�ú����");
		getColumn("xiajjjcmxx").setHeader("�ļ����ô�ú����");
		getColumn("chumqcmzb").setHeader("��ú�ڴ�úָ��");
		getColumn("jiangcqcmzb").setHeader("�����ڴ�úָ��");
		
		getColumn("dongjjjcmsx").setHidden(true);
		getColumn("dongjjjcmxx").setHidden(true);
		getColumn("xiajjjcmsx").setHidden(true);
		getColumn("xiajjjcmxx").setHidden(true);
		getColumn("chumqcmzb").setHidden(true);
		getColumn("jiangcqcmzb").setHidden(true);
		
		getColumn("jib").setHeader("����");
		getColumn("beiz").setHeader("��ע");
		getColumn("dianclbb_id").setHeader("�糧���");
		getColumn("jiesbdcbs").setHeader("���㵥�糧��ʶ");

		getColumn("ranlgs").setHeader("ȼ�Ϲ�˾");
		
		getColumn("xitzjh").setHeader("ϵͳ�м̺�");
		getColumn("shihzjh").setHeader("�л��м̺�");
		getColumn("chuanz").setHeader("����");
		getColumn("yunsfs").setHeader("���䷽ʽ");
		getColumn("xitzjh").setHidden(true);
		getColumn("shihzjh").setHidden(true);
		getColumn("chuanz").setHidden(true);
		
		addPaging(18);
		getColumn("ranlgs").setHidden(true);
		getColumn("fuid").setHidden(true);
		getColumn("shangjgsid").setHidden(true);
		getColumn("yunsfs").setHidden(true);
//		getColumn("lianxrb_id").setHidden(true);
		getColumn("chanqxzb_id").setHidden(true);
		getColumn("lianxdz").setHidden(true);
		getColumn("diz").setHidden(true);
		getColumn("youzbm").setHidden(true);
		getColumn("zongj").setHidden(true);
		getColumn("zhuangjrl").setHidden(true);
		getColumn("zuidkc").setHidden(true);
		getColumn("xianfhkc").setHidden(true);
		getColumn("rijhm").setHidden(true);
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
		getColumn("jingjcml").setHidden(true);
		getColumn("jib").setHidden(true);
		getColumn("beiz").setHidden(true);
		getColumn("dianclbb_id").setHidden(true);
		getColumn("jiesbdcbs").setHidden(true);

		List caiyfs = new ArrayList();
		caiyfs.add(new IDropDownBean(1, "�˹�"));
		caiyfs.add(new IDropDownBean(2, "��е"));
		getColumn("caiyfs").setEditor(new ComboBox());
		getColumn("caiyfs").setComboEditor(gridId, new IDropDownModel(caiyfs));
		getColumn("caiyfs").setReturnId(false);
		getColumn("caiyfs").setDefaultValue("�˹�");
		getColumn("caiyfs").editor.setAllowBlank(true);//�������������Ϊ��

		List ranlgs = new ArrayList();
		ranlgs.add(new IDropDownBean(1, "��"));
		ranlgs.add(new IDropDownBean(2, "��"));
		getColumn("ranlgs").setEditor(new ComboBox());
		getColumn("ranlgs").setComboEditor(gridId, new IDropDownModel(ranlgs));
		getColumn("ranlgs").setReturnId(true);
		getColumn("ranlgs").setDefaultValue("��");
		getColumn("ranlgs").editor.setAllowBlank(true);

		List jilfs = new ArrayList();
		jilfs.add(new IDropDownBean(1, "����"));
		jilfs.add(new IDropDownBean(2, "���"));
		getColumn("jilfs").setEditor(new ComboBox());
		getColumn("jilfs").setComboEditor(gridId, new IDropDownModel(jilfs));
		getColumn("jilfs").setReturnId(false);
		getColumn("jilfs").setDefaultValue("����");
		getColumn("jilfs").editor.setAllowBlank(true);
		List yunsfs = new ArrayList();
		yunsfs.add(new IDropDownBean(1, "����"));
		yunsfs.add(new IDropDownBean(2, "����"));
		yunsfs.add(new IDropDownBean(3, "ˮ��"));
		getColumn("yunsfs").setEditor(new ComboBox());
		getColumn("yunsfs").setComboEditor(gridId, new IDropDownModel(yunsfs));
		getColumn("yunsfs").setReturnId(false);
		getColumn("yunsfs").setDefaultValue("����");
		getColumn("yunsfs").editor.setAllowBlank(true);
		
		List jib = new ArrayList();
		jib.add(new IDropDownBean(1, "����"));
		jib.add(new IDropDownBean(2, "�ֹ�˾"));
		jib.add(new IDropDownBean(3, "�糧"));
		jib.add(new IDropDownBean(4, "����"));
		getColumn("jib").setEditor(new ComboBox());
		getColumn("jib").setComboEditor(gridId, new IDropDownModel(jib));
		getColumn("jib").setDefaultValue("����");
		getColumn("jib").editor.setAllowBlank(true);
		
		getColumn("shengfb_id").setEditor(new ComboBox());
		getColumn("shengfb_id").setComboEditor(gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		getColumn("chanqxzb_id").setEditor(new ComboBox());
		getColumn("chanqxzb_id").setComboEditor(gridId,
				new IDropDownModel("select id,mingc from chanqxzb"));
		getColumn("chanqxzb_id").editor.setAllowBlank(true);
//		getColumn("lianxrb_id").setEditor(new ComboBox());
//		getColumn("lianxrb_id").setComboEditor(gridId,
//				new IDropDownModel("select id,xingm from lianxrb"));
		getColumn("fuid").setEditor(new ComboBox());
		getColumn("fuid").setComboEditor(
				gridId,
				new IDropDownModel(
						"select id,mingc from diancxxb where jib < 3"));
		getColumn("fuid").editor.setAllowBlank(true);

		getColumn("shangjgsid").setEditor(new ComboBox());
		getColumn("shangjgsid")
				.setComboEditor(
						gridId,
						new IDropDownModel(
								"select id,mingc from diancxxb where jib<3"));
		getColumn("shangjgsid").editor.setAllowBlank(true);
		
		getColumn("daoz").setEditor(new ComboBox());
		getColumn("daoz").setComboEditor(
				gridId,
				new IDropDownModel(
						"select id,quanc from chezxxb where leib = '��վ'"));
		getColumn("daoz").editor.setAllowBlank(true);
		
		getColumn("daog").setDefaultValue("");
		getColumn("daog").setEditor(new ComboBox());
		getColumn("daog").setComboEditor(
				gridId,
				new IDropDownModel(
						"select id,quanc from chezxxb where leib = '�ۿ�'"));
		getColumn("daog").setDefaultValue("");
		getColumn("daog").editor.setAllowBlank(true);
		getColumn("dianclbb_id").setEditor(new ComboBox());
		getColumn("dianclbb_id").setComboEditor(gridId,
				new IDropDownModel("select id,mingc from dianclbb"));
		
		addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		addTbarTreeBtn("diancTree");
		addTbarText("-");
		addToolbarButton(GridButton.ButtonType_Insert, null);
		// addToolbarButton(GridButton.ButtonType_Delete, null);
		addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String s = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Diancreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		addToolbarItem("{"
				+ new GridButton("��ӡ", "function (){" + s + "}").getScript()
				+ "}");

		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	// /////////////////////////////��վ////////////////////////////////
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
		String sql = "select id,quanc as daoz from chezxxb where leib='��վ' order by quanc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "��ѡ��"));
		return;
	}

	// ///////////////////////////////����//////////////////////////////////////
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
		String sql = "select id,quanc as daoz from chezxxb where leib='�ۿ�'";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "��ѡ��"));
		return;
	}

	// /////////////////////////////�糧���///////////////////////////////
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
				.setProSelectionModel4(new IDropDownModel(sql, "��ѡ��"));
		return;
	}

	// ///////////////////////////////����//////////////////////////////////////
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
				+ "  from (select 1 as id, decode(1, 1, '����') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 2 as id, decode(1, 1, '��˾') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 3 as id, decode(1, 1, '�糧') as mingc from dual\n" 
				+ "        union\n"
				+ "        select 4 as id, decode(1, 1, '����') as mingc from dual) jb\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql));
		return;
	}
	
//	 ///////////////////////////////ȼ�Ϲ�˾//////////////////////////////////////
	public IDropDownBean getRanlgsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getRanlgsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setRanlgsValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean9(Value);
	}

	public void setRanlgsModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getRanlgsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getRanlgsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void getRanlgsModels() {
		String sql = "select ranlgl.id, ranlgl.mingc\n"
				+ "  from (select 1 as id, decode(1, 1, '��') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 2 as id, decode(1, 1, '��') as mingc from dual) ranlgl\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(sql));
		return;
	}
	
	
	// //////////////////////////�ϼ���λ///////////////////////////////////
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
		String sql = "select id, mingc from diancxxb where jib < 3";

		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql));
		return;
	}

	// /////////////////////////////////�ϼ���˾////////////////////////////////////
	public IDropDownBean getShangjgsidValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getShangjgsidModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setShangjgsidValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setShangjgsidModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getShangjgsidModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getShangjgsidModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getShangjgsidModels() {
		String sql = "select id, mingc from diancxxb where jib < 3";

		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql));
		return;
	}

	// ////////////////////////////ʡ��///////////////////////////////////////
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
				.setProSelectionModel3(new IDropDownModel(sql, "��ѡ��"));
		return;
	}
	////////////////////��Ȩ����////////////////////////////

	public IDropDownBean getChanqxzb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getChanqxzb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setChanqxzb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setChanqxzb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getChanqxzb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getChanqxzb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void getChanqxzb_idModels() {
		String sql = "select  id ,mingc from chanqxzb order by xuh";
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(sql, "��ѡ��"));
		return;
	}
	
	
////////////////////��ϵ�˱�////////////////////////////

//	public IDropDownBean getLianxrb_idValue() {
//		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
//			((Visit) getPage().getVisit())
//					.setDropDownBean8((IDropDownBean) getLianxrb_idModel()
//							.getOption(0));
//		}
//		return ((Visit) getPage().getVisit()).getDropDownBean8();
//	}
//
//	public void setLianxrb_idValue(IDropDownBean Value) {
//		((Visit) getPage().getVisit()).setDropDownBean8(Value);
//	}
//
//	public void setLianxrb_idModel(IPropertySelectionModel value) {
//		((Visit) getPage().getVisit()).setProSelectionModel8(value);
//	}
//
//	public IPropertySelectionModel getLianxrb_idModel() {
//		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
//			getLianxrb_idModels();
//		}
//		return ((Visit) getPage().getVisit()).getProSelectionModel8();
//	}
//
//	public void getLianxrb_idModels() {
//		String sql = "select  id ,xingm from lianxrb order by diancxxb_id";
//		((Visit) getPage().getVisit())
//				.setProSelectionModel8(new IDropDownModel(sql, "��ѡ��"));
//		return;
//	}
//	

	
	
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			visit.setProSelectionModel6(null);
			visit.setProSelectionModel7(null);
			visit.setProSelectionModel8(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			visit.setDropDownBean6(null);
			visit.setDropDownBean7(null);
			visit.setDropDownBean8(null);
			gridColumns = null;
			setTreeid(null);

		}
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
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
