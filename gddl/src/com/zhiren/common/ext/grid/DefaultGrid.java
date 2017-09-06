package com.zhiren.common.ext.grid;

import java.io.Serializable;
import java.util.Date;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;

public class DefaultGrid implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6846608054462478228L;
	
	private String[][] data;
	private EditorGridPanel grid;
	private String id;
	private String[][] griddata;
	private String tableName;
	
	public DefaultGrid(String id,ResultSetList rsl) {
		this.id = id;
		grid = new EditorGridPanel();
		
		ColumnModel cm = new ColumnModel();
		cm.setId(id+"_cm");
		for(int i = 0 ; i < rsl.getColumnNames().length ; i++) {
			Column c = new Column();
			if("ID".equals(rsl.getColumnNames()[i].toUpperCase())) {
				c.setHidden(true);
			}else {
				String orctp = rsl.getColumnTypes()[i];
				if(SysConstant.Oracle_DataType_Date.equals(orctp)) {
					c.setDataType(SysConstant.Ext_DataType_Date);
					c.setEditor(new DateField());
					c.setRenderer(SysConstant.Ext_Renderer_Date);
					for(int m=0;m<rsl.getRows();m++) {
						rsl.setString(m,i,DateUtil.FormatDate(new Date(Long.parseLong(rsl.getString(m, i)))));
					}
				}else
					if(SysConstant.Ext_DataType_Number.equals(orctp)) {
						c.setEditor(new NumberField());
						c.setAlign("right");
						((NumberField)c.getEditor()).setDecimalPrecision(rsl.getColScales()[i]);
					}else{
						c.setEditor(new TextField());
						((TextField)c.getEditor()).setMaxLength(rsl.getColPrecisions()[i]);
					}
			}
			if(c.getEditor() != null) {
				c.getEditor().allowBlank = rsl.isNullable(i);
			}
			cm.addColumn(c);
		}
		initData(rsl.getRows(),rsl.getColumnCount());
		while(rsl.next()) {
			setRowData(rsl.getRow(),rsl.getRowString());
		}
		grid.setCM(cm);
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public EditorGridPanel getEditorGridPanel() {
		return grid;
	}
	public void setEditorGridPanel(EditorGridPanel grid) {
		this.grid = grid;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
	/**
	 * 设置grid的全部数据
	 * 
	 * @param data String[][]
	 */
	public void setData(String[][] data) {
		griddata = data;
	}
	
	public String[][] getData(){
		
		return griddata;
	}
	/**
	 * 初始化Data数组
	 * 
	 * @param rows 行数	
	 */
	public void initData(int rows,int cols) {
		griddata = new String [rows][cols];
	}
	
	/**
	 * 设置行数据
	 * @param rowIndex 行下标 0 to ...
	 * @param data String[] 数据
	 */
	public void setRowData(int rowIndex,String [] data) {
		if(rowIndex>=griddata.length) {
	//			异常
			return;
		}
		griddata[rowIndex] = data;
	}
	
	/**
	 * 设置每个单元格的值
	 * 
	 * @param rowIndex 行标 0 to ...
	 * @param colIndex 列标 0 to ...
	 * @param data Stirng 值
	 */
	public void setDataValue(int rowIndex,int colIndex, String data) {
		if(rowIndex>=griddata.length) {
	//			异常
		return;
	}
	if(colIndex>=griddata[rowIndex].length) {
	//			异常
			return;
		}
		griddata[rowIndex][colIndex] = data;
	}
	
	//	返回单元格的值
	public String getDataValue(int rowIndex,int colIndex) {
		if(rowIndex>=griddata.length) {
	//			异常
	return "";
	}
	if(colIndex>=griddata[rowIndex].length) {
	//			异常
	return "";
		}
		return griddata[rowIndex][colIndex];
	}

}
