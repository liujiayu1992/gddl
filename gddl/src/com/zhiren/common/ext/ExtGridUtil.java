package com.zhiren.common.ext;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.grid.CheckColumn;
import com.zhiren.main.Visit;
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-18
 * �������޸�grid���������г�ʼ�������̣���ֹ�ظ���ʼ�������⡣
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-05
 * �������޸�grid���������к��и�ѡ������ȷ������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-10 17��11
 * �������޸�grid�����������ʱrownumber �б��ú������
 */
/**
 * @author ����
 * 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-01 11��05
 * �������޸�grid����������Ŵ�����ȷ������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-12 14��27
 * �������޸�GRID����ʱ��ʼ���������¶��������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-11 16��50
 * ����������Grid����������
 */
/*
 * 2009-04-16
 * ����
 * ����ģ�����Ʊ���
 */
/*
 * 2008-02-18
 * ����
 * ���ӱ��淽������ֵ
 */
 /*
 * 2009-12-18
 * ww
 * ����Grid viewConfig ����
 */
/*
 * ���ߣ�yinjm
 * ʱ�䣺2010-01-27
 * ����������currentPage����������ҳ����Grid��ʾ�ĵ�ǰҳ�š�
 */

/*
 * 2010-05-26
 * ww
 * ����Grid enableHdMenu ����ֵΪtrueʱ������ʾGrid �е������˵���ť
 */
public class ExtGridUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7020946077527159829L;

	public static final int Gridstyle_Read = 0;

	public static final int Gridstyle_Edit = 1;

	public static final int GridselModel_Row = 0;

	public static final int GridselModel_Check = 1;

	public static final int GridselModel_Row_single = 2;

	public static final int GridselModel_Row_formgrid = 3;
	
	private boolean initCustomSet = false;
	
	private boolean readCustomSet = false;
	
	private String CustomKey; 

	public String title;
	
	public String mokmc;	//ģ�����ƣ���¼��־ʱʹ��

	public int gridType;

	public String gridId;

	public String width;

	public String height;
	
	public String viewConfig;

	private CheckColumn Checkplugins;
	
	private String plugins="";

	private boolean defaultrender = true;

	// public String strwidth;
	// public String strheight;
	public int gridsm;

	public List gridColumns;

	public String[][] griddata;

	public String tbars;
	
	public int currentPage = 1; // ����Grid��ʾ�ĵ�ǰҳ��

	public int pagsize = 25;

	public String otherScript;

	public int clicksToEdit = 1;

	public boolean frame = true;

	public boolean defaultsortable = true;

	public String tableName;

	public String gridJsCode = "";

	public String gridJsDs = "";
	
	private String headers="";	//������б�ͷ
	
	private boolean isEL = true;
	
	private boolean enableHdMenu = true;  //True to enable the drop down button for menu in the headers.

	public ExtGridUtil() {
	}

	/**
	 * @param gridId
	 *            ����grid��Id ����󶨵�DIV��id
	 * 
	 */
	public ExtGridUtil(String gridId) {
		this.gridId = gridId;
	}
	/**
	 * @param gridId
	 *            ����grid��Id ����󶨵�DIV��id
	 * @param rsl
	 *            ȡ���ļ�¼��
	 */
	public ExtGridUtil(String gridId, ResultSetList rsl) {
		this.gridId = gridId;
		setExtGridColumn(rsl,null);
	}
	/**
	 * @param gridId
	 *            ����grid��Id ����󶨵�DIV��id
	 * @param rsl
	 *            ȡ���ļ�¼��
	 * @param CustomKey  
	 * 			  �Զ���༭����ʽ��key
	 */
	public ExtGridUtil(String gridId, ResultSetList rsl, String CustomKey) {
		this.gridId = gridId;
		setExtGridColumn(rsl,CustomKey);
	}
	
	/**
	 * @author xzy
	 * @param gridId
	 *            ����grid��Id ����󶨵�DIV��id
	 * @param sql
	 *            �Զ������̱�ȡ���ķ���
	 */
	/**
	 * ���õ�Ԫ���ֵ
	 * @author yjm
	 * @param rsl
	 * 			ȡ���ļ�¼��
	 * @param colDataIndex
	 * 			������
	 */
	public void modifyColumnContent(ResultSetList rsl, String colDataIndex) {
		int colIndex = -1;
		for (int i = 0; i < rsl.getColumnNames().length; i ++) {
			if (colDataIndex.toUpperCase().equals(getColumn(i).dataIndex.toUpperCase())) {
				colIndex = i - 1;
				break;
			}
		}
		if (colIndex != -1) {
			for (int rowIndex = 0; rowIndex < rsl.getRows(); rowIndex ++) {
				if(rsl.getString(rowIndex, colIndex) != null && !"".equals(rsl.getString(rowIndex, colIndex))){
					this.setDataValue(rowIndex, colIndex, DateUtil.FormatDateTimeMinute(new Date(Long.parseLong(rsl.getString(rowIndex, colIndex)))));
				}
			}
		}
	}
	public ExtGridUtil(String colmane,String colmane1,String colmane2,String gridId,String[][] data){
		this.gridId = gridId;
		for (int i = 0; i < data[0].length; i++) {
			if (i == 0) {
				GridColumn gc = new GridColumn(colmane, colmane);

				gc.setEditor(new TextField());
				((TextField) gc.editor).setMaxLength(40);

				addColumn(gc);
			} else if (i == 1) {
				GridColumn gc = new GridColumn(colmane1, colmane1);

				gc.setEditor(new TextField());
				((TextField) gc.editor).setMaxLength(40);

				addColumn(gc);
			}else if (i == 2) {
				GridColumn gc = new GridColumn(colmane2, colmane2);

				gc.setEditor(new TextField());
				((TextField) gc.editor).setMaxLength(200);

				addColumn(gc);
			}else {
				GridColumn gc = new GridColumn(data[1][i], data[1][i]);

				gc.setEditor(new TextField());
				((TextField) gc.editor).setMaxLength(80);

				addColumn(gc);
			}
		}
		initData(data.length-2, data[0].length);
		for (int i=2;i<data.length;i++){
			setRowData(i-2,data[i]);
		}
		
	}
	
	public String OratypeOfExt(String oratype) {
		if ("NUMBER".equals(oratype)) {
			return GridColumn.DataType_Float;
		}
		if ("DATE".equals(oratype)) {
			return GridColumn.DataType_Date;
		}
		// if(oratype.equals(anObject)) {

		// }
		return GridColumn.DataType_String;
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
	
	public String getMokmc() {
		return mokmc;
	}

	public void setMokmc(String mokmc) {
		this.mokmc = mokmc;
	}

	public boolean getDefaultRender() {
		return defaultrender;
	}

	public void setDefaultRender(boolean defaultrender) {
		this.defaultrender = defaultrender;
	}
	
	public boolean isEL() {
		return this.isEL;
	}

	public void setEL(boolean el) {
		this.isEL = el;
	}

	public CheckColumn getCheckPlugins() {
		return Checkplugins;
	}

	public void setCheckPlugins(CheckColumn plugins) {
		this.Checkplugins = plugins;
	}
	
	/**
	 * @return viewConfig
	 */
	public String getViewConfig() {
		return viewConfig;
	}
	
	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}
	
	/**
	 * @return boolean
	 */
	public boolean getEnableHdMenu() {
		return enableHdMenu;
	}
	public void setEnableHdMenu(boolean enableHdMenu) {
		this.enableHdMenu = enableHdMenu;
	}

	/**
	 * @return width
	 */
	public String getWidth() {
		return "".equals(width) ? null : width;
	}

	/**
	 * @param width
	 *            grid�Ŀ��
	 */
	public void setWidth(int width) {
		this.width = String.valueOf(width);
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return "".equals(height) ? null : height;
	}

	public void setHeight(int height) {
		this.height = String.valueOf(height);
	}

	public void setHeight(String height) {
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
	
//	/**
//	 * @return ����ҳ����ʼ��
//	 */
//	public int getPageStartRow() {
//		return pageStartRow;
//	}
//
//	/**
//	 * @param pageStartRow
//	 * ����ҳ����ʼ��
//	 */
//	public void setPageStartRow(int pageStartRow) {
//		this.pageStartRow = pageStartRow;
//	}
	
	/**
	 * @return 
	 * ����Grid��ǰ��ʾ��ҳ��
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 * ����Grid��ʾ��ҳ��
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
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
	 * @get set ����������б�ͷ
	 */
	public String getHeaders(){
		
		return headers;
	}
	
	public void setHeaders(String value){
		
		headers=value;
	}
	
	public String getPlugins(){
		
		return plugins;
	}
	
	public void setPlugins(String value){
		
		plugins=value;
	}
	
	public int getSelModelColumnNumber(){
		int cn = 0;
		for (int i = 0; i < gridColumns.size(); i++) {
			GridColumn gc = (GridColumn) gridColumns.get(i);
			if (gc.coltype != GridColumn.ColType_default) {
				cn++;
			}
		}
		return cn;
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

	/**
	 * @param column
	 *            ����һ��
	 */
	public void addColumn(GridColumn column) {
		getGridColumns().add(column);
	}

	public void addColumn(int index, GridColumn column) {
		getGridColumns().add(index, column);
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

	public GridColumn getColByHeader(String header) {
		if (header == null || "".equals(header)) {
			return null;
		}
		for (int i = 0; i < getGridColumns().size(); i++) {
			if (header.toUpperCase().equals(
					getColumn(i).header == null ? "" : getColumn(i).header
							.toUpperCase())) {
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
    public void addToolbarButton(int gridButtonType, String tapestryBtnId,boolean zhuangt) {
        GridButton gb = new GridButton(gridButtonType, gridId,
                getGridColumns(), tapestryBtnId);
        gb.setDisabled(zhuangt);
        addTbarBtn(gb);
    }
	// ��Ӧ�ж�������ť���� condition����
	public void addToolbarButton(int gridButtonType, String tapestryBtnId,
			String condition) {
		GridButton gb = new GridButton(gridButtonType, gridId,
				getGridColumns(), tapestryBtnId, condition);
		addTbarBtn(gb);
	}

	public void addToolbarButton(String text, int gridButtonType,
			String tapestryBtnId, String condition) {
		GridButton gb = new GridButton(text, gridButtonType, gridId,
				getGridColumns(), tapestryBtnId, condition);
		addTbarBtn(gb);
	}

	public void addToolbarButton(String text, int gridButtonType,
			String tapestryBtnId) {
		GridButton gb = new GridButton(text, gridButtonType, gridId,
				getGridColumns(), tapestryBtnId);
		addTbarBtn(gb);
	}

	public void addToolbarButton(String text, int gridButtonType,
			String tapestryBtnId, String condition, String icon) {
		GridButton gb;
		if (condition != null) {
			gb = new GridButton(text, gridButtonType, gridId, getGridColumns(),
					tapestryBtnId, condition);
		} else {
			gb = new GridButton(text, gridButtonType, gridId, getGridColumns(),
					tapestryBtnId);
		}
		if (icon != null) {
			gb.setIcon(icon);
		}
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
		// tf.setValue(defaultValue);
		addToolbarItem(tf.getScript());
		addOtherScript(treeid + "_text.setValue(" + treeid
				+ "_treePanel.getSelectionModel().getSelectedNode()==null?'':"
				+ treeid
				+ "_treePanel.getSelectionModel().getSelectedNode().text);");
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

	public String[][] getData() {

		return griddata;
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

	// ���ص�Ԫ���ֵ
	public String getDataValue(int rowIndex, int colIndex) {
		if (rowIndex >= griddata.length) {
			// �쳣
			return "";
		}
		if (colIndex >= griddata[rowIndex].length) {
			// �쳣
			return "";
		}
		return griddata[rowIndex][colIndex];
	}

	public void addOtherScript(String script) {
		if (this.otherScript == null) {
			this.otherScript = "";
		}
		this.otherScript += script;
	}

	public String getOtherScript() {
		return this.otherScript;
	}

	/**
	 * @return ����ѡ��ģʽscript����
	 */
	public String getSelModelScript() {
		switch (getGridSelModel()) {
		case GridselModel_Row:
			return "var " + gridId + "_sm = new Ext.grid.RowSelectionModel()";
		case GridselModel_Check:
			return "var " + gridId
					+ "_sm = new Ext.grid.CheckboxSelectionModel()";
		case GridselModel_Row_single:
			return "var "
					+ gridId
					+ "_sm = new Ext.grid.RowSelectionModel({singleSelect:true})";
		case GridselModel_Row_formgrid:
			return "var "
					+ gridId
					+ "_sm = new Ext.grid.RowSelectionModel({singleSelect:true,listeners: {rowselect: function(sm, row, rec) {Ext.getCmp('company-form').getForm().loadRecord(rec);}}})";
		default:
			return "var " + gridId + "_sm = new Ext.grid.RowSelectionModel()";
		}
	}

	/**
	 * @return ���ر�ͷ��Script
	 */
	public String getColumnsScript() {
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
				"_cm.defaultSortable=").append(isDefaultsortable()).append(
				";\n");
		return columnsScript.toString();
	}

	/**
	 * @return ����������¼��Script
	 */
	public String getPlantScript() {
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
	public String getToolbarScript() {
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
	public String getDataScript() {
		StringBuffer djs = new StringBuffer("var " + gridId + "_data = [\n");
		for (int r = 0; r < griddata.length; r++) {
			djs.append("[");
			for (int c = 0; c < griddata[r].length; c++) {
				djs.append("'");
				if (griddata[r][c]==null){
					djs.append("");
				}else{
					djs.append(griddata[r][c].replaceAll("'", "\\\\'"));
				}
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
	public String getDsScript() {
		StringBuffer dsScript = new StringBuffer();
		dsScript.append("var " + gridId + "_ds = new Ext.data.Store({\n");
		dsScript.append("proxy : new Ext.zr.data.PagingMemoryProxy(" + gridId
				+ "_data),\n");
		dsScript.append("pruneModifiedRecords:true,\n");
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
		if(this.CustomKey != null){
			JDBCcon con = new JDBCcon();
			String sql = "select * from xitpzb where guanjz = '"+CustomKey+"' and leib = 'ϵͳ��������'";
			readCustomSet = con.getHasIt(sql);
			initCustomSet = !readCustomSet;
			if(initCustomSet){
				initCustomSet();
			}else if(readCustomSet){
				readCustomSet();
			}
			con.Close();
		}
		StringBuffer gridScript = new StringBuffer();
		if (getCheckPlugins() != null) {
			gridScript.append(getCheckPlugins().getScript());
		}
		gridScript.append(getSelModelScript()).append("\n");
		gridScript.append(getColumnsScript()).append("\n");
		
		if(!getHeaders().equals("")){
//			���б�ͷ
			gridScript.append(gridId).append("_cm.headers=[").append(getHeaders()).append("];\n");
		}
		gridScript.append(gridId).append("_cm.defaultSortable=").append(
				isDefaultsortable()).append(";\n");
		gridScript.append(getDataScript()).append("\n");
		gridScript.append(getDsScript()).append("\n");
		gridScript.append(getPagSize() > 0 ? getPagingScript() : "").append(
				"\n");
		gridScript.append(getGridColumns().size() > 0 ? getPlantScript() : "");
		gridScript.append("var ").append(gridId)
				.append("_grid = new Ext.grid.").append(getGridPanelName())
				.append("({\n");
		if(isEL()){
			gridScript.append("el:'").append(gridId).append("',\n");
		}
		gridScript.append("cm:").append(gridId).append("_cm,\n");
		gridScript.append("".equals(getSelModelScript()) ? "" : "sm:" + gridId
				+ "_sm,\n");
		gridScript.append("ds:").append(gridId).append("_ds,\n");
		
		if(!getPlugins().equals("")){
			gridScript.append("plugins:").append(getPlugins()).append(
					",\n");
			
		}else if (getCheckPlugins() != null) {
			gridScript.append("plugins:").append(getCheckPlugins().getId()).append(
					",\n");
		}
		
		gridScript.append(getTitle() == null ? "" : "title:'" + getTitle()
				+ "',\n");
		gridScript.append(getWidth() == null ? "" : "width:" + getWidth()
				+ ",\n");
		gridScript.append(getHeight() == null ? "" : "height:" + getHeight()
				+ ",\n");
		// �������ڴ��м�����������������չ
		gridScript.append(getViewConfig()==null ? "": "viewConfig:" + getViewConfig() 
				+ ",\n");
		gridScript.append(getEnableHdMenu()? "" : "enableHdMenu:" + getEnableHdMenu() + ",\n");  
		gridScript.append("clicksToEdit:").append(clicksToEdit).append(",");
		gridScript.append("frame:").append(frame).append(",");

		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");

		gridScript.append(getPagSize() > 0 ? "bbar:" + gridId + "_pag,\n" : "");
		gridScript.append("stripeRows:true});\n");
		if (isEL() && getDefaultRender()) {
			gridScript.append(gridId).append("_grid.render();");
		}
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:").append((getCurrentPage()-1)*getPagSize()).append(", limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		gridScript.append(getOtherScript() == null ? "" : getOtherScript());
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

	public int Save(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = getDeleteResultSet(strchange);
		while (delrsl.next()) {
			if(!(mokmc==null)&&!mokmc.equals("")){
				String id = delrsl.getString("id");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,mokmc,
						tableName,id);
			}
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(
							getValueSql(getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i)));
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				if(!(mokmc==null)&&!mokmc.equals("")){
					String id = mdrsl.getString("id");
					//����ʱ������־
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,mokmc,
							tableName,id);
				}
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					sql.append(
							getValueSql(getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i))).append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		con.Close();
		return flag;
	}

	public boolean isRepeat(JDBCcon con, String colname, String value) {
		String sql = "select id from " + tableName + " where " + colname
				+ " = " + value;
		return con.getHasIt(sql);
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}

	public String getHtml() {
		StringBuffer sb = new StringBuffer();
		for (int c = 0; c < getGridColumns().size(); c++) {
			GridColumn gc = (GridColumn) getGridColumns().get(c);
			if (gc.combo != null) {
				sb.append(gc.getComboHtml()).append("\n");
			}
		}
		return sb.toString();
	}

	public boolean isDefaultsortable() {
		return defaultsortable;
	}

	public void setDefaultsortable(boolean defaultsortable) {
		this.defaultsortable = defaultsortable;
	}

	public String getChange(String change) {
		change = change.replaceAll("'", "''");
		return change;
	}

	public List getElmentList(String strchange) {
		strchange = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + getChange(strchange);
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
	
	public void setExtGridColumn(ResultSetList rsl,String CustomKey){
		if(CustomKey != null) {
			this.CustomKey = CustomKey;
		}else{
			this.CustomKey = null;
		}
		addColumn(new GridColumn(GridColumn.ColType_Rownum));
		for (int i = 0; i < rsl.getColumnNames().length; i++) {
			GridColumn gc = new GridColumn(rsl.getColumnNames()[i], rsl
					.getColumnNames()[i]);
			if ("id".equals(rsl.getColumnNames()[i].toLowerCase())) {
				gc.setDefaultValue("0");
				gc.setEditor(null);
				gc.setHidden(true);
			} else {
				String extType = OratypeOfExt(rsl.getColumnTypes()[i]);
				gc.setDataType(extType);
				if (GridColumn.DataType_Float.equals(extType)) {
					gc.setEditor(new NumberField());
					gc.setAlign("right");
					((NumberField) gc.editor).setDecimalPrecision(rsl
							.getColScales()[i]);
				} else if (GridColumn.DataType_String.equals(extType)) {
					gc.setEditor(new TextField());
					((TextField) gc.editor)
							.setMaxLength(rsl.getColPrecisions()[i]);
				} else if (GridColumn.DataType_Date.equals(extType)) {
					gc.setEditor(new DateField());
					gc.setRenderer(GridColumn.Renderer_Date);
					for (int m = 0; m < rsl.getRows(); m++) {
						if(rsl.getString(m, i) != null && !"".equals(rsl.getString(m,i))){
							rsl.setString(m, i, DateUtil.FormatDate(new Date(Long
									.parseLong(rsl.getString(m, i)))));
						}
						
					}
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
		rsl.close();
	}
	
	public void initCustomSet(){
		JDBCcon con = new JDBCcon();
		String id = MainGlobal.getNewID(100);
		String sql = "insert into xitpzb(id,guanjz,biaot,miaos,zhuangt,leib,beiz)\n" +
			"values("+id+",'"+CustomKey+"','"+CustomKey+"','"+CustomKey+"',1,'ϵͳ��������','')";
		con.getUpdate(sql);
		List ggcs = getGridColumns();
		for(int i = 0, j = 1; i< ggcs.size(); i++){
			GridColumn gc = (GridColumn)ggcs.get(i);
			if(gc.coltype == 0){
				sql = "insert into xitpzzb(id,xitpzb_id,zidm,biaot,xuh,kuand,shifxs,format)\n" +
					"values(getnewid(100),"+id+",'"+gc.dataIndex.toUpperCase()+"','"+gc.header+"',"+ j++ +","+
					gc.width +","+(gc.hidden==true?0:1)+",'')";
				con.getUpdate(sql);
			}
		}
		con.Close();
	}
	
	private void copyData(String[][] newData,int srcCol, int newCol){
		for(int r = 0; r < getData().length ; r++ ){
			newData[r][newCol] = getData()[r][srcCol];
		}
	}
	
	public void readCustomSet(){
		JDBCcon con = new JDBCcon();
		String sql = "select distinct z.zidm,z.biaot,z.kuand,z.shifxs,z.xuh\n" +
			"from xitpzzb z, xitpzb p\n" + 
			"where z.xitpzb_id = p.id\n" + 
			"and p.guanjz = '"+CustomKey+"'\n" + 
			"order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		List newGridColumns = new ArrayList();
		newGridColumns.addAll(getGridColumns());
		String[][] newDatas = new String[getData().length][rsl.getRows()];
		int specialCol = getSelModelColumnNumber();
		for(int i = 0 , j = 0; i < getGridColumns().size() ; i ++){
			GridColumn gc = (GridColumn)getGridColumns().get(i);
			if(gc.coltype != 0){
				newGridColumns.set(gc.coltype-1,gc);
			}else{
				while(rsl.next()){
					String dataIndex = rsl.getString("zidm");
					if(gc.dataIndex.equalsIgnoreCase(dataIndex)){
						gc.header = rsl.getString("biaot");
						gc.width = rsl.getInt("kuand");
						gc.hidden = rsl.getInt("shifxs")==0?true:false;
						newGridColumns.set(rsl.getRow()+specialCol, gc);
						copyData(newDatas, j++, rsl.getRow());
						rsl.beforefirst();
						break;
					}
				}
			}
		}
		if(!newGridColumns.isEmpty()){
			gridColumns = newGridColumns;
			griddata = newDatas;
		}
		rsl.close();
		con.Close();
	}

}