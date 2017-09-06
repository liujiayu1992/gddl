package com.zhiren.report;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zhiren.common.ResultSetList;
/**
 * @author chenhh
 * 
 */
public class Table {
	Cell cells[][] = null;

	public Column cols[] = null;

	public Row rows[] = null;

	public static final int ALIGN_LEFT = -1;

	public static final int ALIGN_RIGHT = 2;

	public static final int ALIGN_CENTER = 1;

	public static final int VALIGN_TOP = 1;

	public static final int VALIGN_BOTTOM = 2;

	public static final int VALIGN_CENTER = 1;

	public static final int PER_FONTNAME = 1;

	public static final int PER_FONTSIZE = 2;

	public static final int PER_FONTBOLD = 3;

	public static final int PER_FORECOLOR = 4;

	public static final int PER_VALUE = 5;

	public static final int PER_ALIGN = 6;

	public static final int PER_VALIGN = 7;

	public static final int PER_PADDING = 8;

	public static final int PER_BORDER_LEFT = 9;

	public static final int PER_BORDER_RIGHT = 10;

	public static final int PER_BORDER_TOP = 11;

	public static final int PER_BORDER_BOTTOM = 12;

	public static final int PER_USED = 13;

	public static final int PER_BACKCOLOR = 14;

	public static final String PAGENUMBER_NORMAL = "(第Page/Pages页)";

	public static final String PAGENUMBER_CHINA = "(共Pages页第Page页)";
	
	public static final String RowDataID="RowDataID";
	private int miRows = 0;

	private int miCols = 0;

	private int miPageRows = -1;

	private int miPages = 0;

	private int miFixedRows = 0;

	private int miFixedCols = 0;

	private int miFooterRows = 0;

	private int miBorderLeft = 2;

	private int miBorderRight = 1;

	private int miBorderTop = 2;

	private int miBorderBottom = 1;

	public int cellpadding =2;

	public int cellspacing = 0;

	public String bgColor = "";

	public String bgImg = "";

	public String fontName = "宋体";

	public int fontSize = 9;

	public Cell defalutCell = new Cell();

	public int defaultRowHeight = 24;

	public int defaultColWidth = 80;

	public boolean ShowZero = false;

	public boolean ShowPageNumber = true;

	private int miCurrentPage = 0;

	private int mTableHeadData[][];//记录多表头的位置
	
	private int miPageMaxHeight=0;
	
	private int miSplitPageStyle=Report.PAGE_ROWS ;
	
	private boolean mHasSplit=false;
	
	private String colHeadClass="";
	private String rowHeadClass="";
	private String firstDataRowClass="";
	private String secondDataRowClass="";
	private String tableClass="default_report_table_body";
	
	private HashMap mhmStyle=new HashMap();
	private String TableID="";
	
	private String borderColor="";
	private String bordercolorlight="";
	private String bordercolordark="";
	private int border=0;
	private boolean useCss=false;
	
	private String cellsClass="";
	
	public void setUseCss(boolean useCss){
		this.useCss=useCss;
		setColHeaderClass("tab_colheader");
		setRowHeaderClass("tab_rowheader");
		setFirstDataRowClass("tab_data_line_one");
		setSecondDataRowClass("tab_data_line_two");
		//setCellsClass("tab_cells");
		setTableClass("tab_body");
	}
	
	public void setUseDefaultCss(boolean useCss){
		this.useCss=useCss;
	}

	public void setCellspacing(int cellspacing){
		this.cellspacing=cellspacing;
	}
	public void setCellsClass(String cellsClass){
		this.cellsClass=cellsClass;
	}
	public void setFontSize(int fontSize){
		this.fontSize=fontSize;
	}
	
	public void setBorder(int border){
		this.border=border;
	}
	public void setBorderColorLight(String bordercolorlight){
		this.bordercolorlight=bordercolorlight;
	}
	
	public void setBorderColorDark(String bordercolordark){
		this.bordercolordark=bordercolordark;
	}
	public void setTableClass(String tableClass){
		this.tableClass=tableClass;
	}
	public void setBorderColor(String strBoderColor){
		borderColor=strBoderColor;
	}
	public void setTableID(String strTableID){
		this.TableID=strTableID;
	}
	
	public void setColHeaderClass(String colHeaderClass){
		this.colHeadClass=colHeaderClass;
	}
	
	public void setRowHeaderClass(String rowHeaderClass){
		this.rowHeadClass=rowHeaderClass;
	}
	
	public void setFirstDataRowClass(String firstDataRowClass){
		this.firstDataRowClass=firstDataRowClass;
	}	
	
	public void setSecondDataRowClass(String secondDataRowClass){
		this.secondDataRowClass=secondDataRowClass;
	}
	
	public void setSplitPageStyle(int intSplitPageStyle){
		miSplitPageStyle=intSplitPageStyle;
	}
	
	public int getSplitPageStyle(){
		return miSplitPageStyle;
	}
	public void setPageMaxHeight(int iPageMaxHeight){
		miPageMaxHeight=iPageMaxHeight;
	}
	
	public int getPageMaxHeight(){
		return miPageMaxHeight;
	}
	
	public void setTableTitlePos(int intArrPos[][]){
		mTableHeadData=intArrPos;
	}
	
	public void setCurrentPage(int iCurrentPage ){
		miCurrentPage=iCurrentPage;
	}
	
	public int getCurrentPage(){
		return miCurrentPage;
	}
	
	public void setColFormat(int iCol, String strFormat) {
		if (iCol <= 0 || iCol > cols.length) {
			System.out.println("setColFormat:Col" + iCol + "越界");
		} else {
			cols[iCol].format = strFormat;
		}
	}

	public void setColFormat(String[] arrFormat) {
		for (int i = 1; i <= arrFormat.length; i++) {
			if (i <= 0 || i > cols.length) {
				System.out.println("setColFormat:Col" + i + "越界");
			} else {
				cols[i].format = arrFormat[i - 1];
			}
		}
	}

	public int getFixedCols() {
		return miFixedCols;
	}

	public void setFixedCols(int iFixedCols) {
		miFixedCols = iFixedCols;
	}

	public Table(int iRows, int iCols) {
		iniTable(iRows, iCols);
		iniTableData(defalutCell);
		getPages();
	}

	public Table(int iRows, int iCols, Cell c) {
		iniTable(iRows, iCols);
		iniTableData(c);
		getPages();
	}
	
	public Table(int iRows, int iCols, int[] colWidth, Cell c) {
		iniTable(iRows, iCols);
		iniTableData(c);
		setWidth(colWidth);
		getPages();
	}

	public Table(ResultSet rs, int iFixedRows, int iFooterRows, int intFixedCols) {
		try {
			int i = 0;
			int j = 0;
			int iRows = 0;
			miFixedRows = iFixedRows;
			miFooterRows = iFooterRows;
			miFixedCols = intFixedCols;
			ResultSetMetaData rsmd = rs.getMetaData();

			i = miFixedRows;
			if (!rs.isAfterLast()) {
				rs.last();
				iRows = rs.getRow();
				rs.beforeFirst();
			}
			miRows = miFixedRows + iRows + miFooterRows;
			miCols = rsmd.getColumnCount();
			iniTable(miRows, miCols);
			iniTableData(defalutCell);

			while (rs.next()) {
				i = i + 1;
				for (j = 1; j <= miCols; j++) {
					if (rs.getString(j) != null) {
						cells[i][j].value = rs.getString(j);
					} else {
						cells[i][j].value = "";
					}
					if (j > intFixedCols) {
						cells[i][j].align = Table.ALIGN_RIGHT;
					}
				}
			}
			setFixedRow(1,miFixedRows,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getPages();
	}
    public Table(ResultSet rs, int iFixedRows, int iFooterRows, int intFixedCols,int intExcludeCols) {
        try {
            int i = 0;
            int j = 0;
            int iRows = 0;
            miFixedRows = iFixedRows;
            miFooterRows = iFooterRows;
            miFixedCols = intFixedCols;
            ResultSetMetaData rsmd = rs.getMetaData();

            i = miFixedRows;
            if (!rs.isAfterLast()) {
                rs.last();
                iRows = rs.getRow();
                rs.beforeFirst();
            }
            miRows = miFixedRows + iRows + miFooterRows;
            miCols = rsmd.getColumnCount()  - intExcludeCols;//去掉隐藏的“列”
            iniTable(miRows, miCols);
            iniTableData(defalutCell);

            while (rs.next()) {
                i = i + 1;
                for (j = 1; j <= miCols; j++) {
                    if (rs.getString(j) != null) {
                        cells[i][j].value = rs.getString(j);
                    } else {
                        cells[i][j].value = "";
                    }
                    if (j > intFixedCols) {
                        cells[i][j].align = Table.ALIGN_RIGHT;
                    }
                }
            }
            setFixedRow(1,miFixedRows,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getPages();
    }
	public boolean HasRowId(ResultSetList rs){
		String Namse[]=rs.getColumnNames();
		for (int i=0 ;i<Namse.length;i++){
			if (Namse[i].toLowerCase().equals(RowDataID.toLowerCase())){
				return true;
			}
		}
		return false; 
	}
	
	public Table(ResultSetList rs, int iFixedRows, int iFooterRows, int intFixedCols) {
		try {
			int i = 0;
			int j = 0;
			int iRows = 0;
			miFixedRows = iFixedRows;
			miFooterRows = iFooterRows;
			miFixedCols = intFixedCols;

			i = miFixedRows;
			iRows = rs.getRows();
			miRows = miFixedRows + iRows + miFooterRows;
			miCols = rs.getColumnCount();
			iniTable(miRows, miCols);
			iniTableData(defalutCell);

			while (rs.next()) {
				i = i + 1;
				for (j = 1; j <= miCols; j++) {
					if (rs.getString(j-1) != null) {
						cells[i][j].value = rs.getString(j-1);
					} else {
						cells[i][j].value = "";
					}
					if (j > intFixedCols) {
						cells[i][j].align = Table.ALIGN_RIGHT;
					}
				}
			}
			setFixedRow(1,miFixedRows,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getPages();
	}

	public Table(String[][] arrString, int iFixedRows,int iFooterRows, int intFixedCols) {
		int i = 0;
		int j = 0;
		if (arrString == null) {
			return;
		} else {
			miRows = arrString.length + iFixedRows+iFooterRows;
			miCols = arrString[0].length;
			miFixedRows=iFixedRows;
			miFixedCols=intFixedCols;
			miFooterRows = iFooterRows;
			
			iniTable(miRows, miCols);
			iniTableData();

			for (i = 1; i <= arrString.length; i++) {
				for (j = 1; j <= arrString[0].length; j++) {
					if  (arrString[i - 1][j - 1]==null){
						cells[i + miFixedRows][j] = new Cell("");
					}else{
						cells[i + miFixedRows][j] = new Cell(arrString[i - 1][j - 1]);
					}
				}
			}
		}
		setFixedRow(1,miFixedRows,true);
	}
	
	public void AddTableRow(int iRow) {
		AddTableRow(iRow,new Cell());
	}
	
	public void AddTableRow(int iRow,Cell c) {
		int i=0;
		int j=0;
		Cell cellsTemp[][] = null;
		cellsTemp = new Cell[miRows + iRow + 1][miCols + 1];
		rows = new Row[miRows + iRow + 1];	
//		复制原有的数据
		for (i = 1; i <=miRows; i++) {
			for (j = 1; j <=miCols; j++) {
				cellsTemp[i][j] =cells[i][j];
			}
			rows[i]=new Row();
		}
		for(i = miRows+1;i<miRows+iRow+1;i++) {
			for (j = 1; j <=miCols; j++) {
				cellsTemp[i][j] = new Cell(c);
			}
			rows[i]=new Row();
		}
		cells = cellsTemp;
		miRows = miRows + iRow;
		getPages();
	}
	
	public void AddTableData(String[][] arrString,ResultSetList rs){
		int i=0;
		int j=0;
		
		int iArrRows=0;
		int iRsRows=0;
		int iAllRows=0;
		
		
		Cell cellsTemp[][] = null;
		iRsRows = rs.getRow();
		
		iArrRows=arrString.length;
		iAllRows=iArrRows+miRows+iRsRows;
		
		cellsTemp = new Cell[iAllRows+ 1][miCols + 1];
		
		rows = new Row[iAllRows+1];
		for (i=1;i<=iAllRows;i++){
			rows[i]=new Row();
		}
		
		//复制原有的数据
		for (i = 1; i <=miRows; i++) {
			for (j = 1; j <=miCols; j++) {
				cellsTemp[i][j] =cells[i][j];
			}
		}
		
		//填写数组
		for (i = 1; i <= arrString.length; i++) {
			for (j = 1; j <= arrString[0].length; j++) {
				cellsTemp [miRows+i][j]= new Cell(arrString[i - 1][j - 1]);
			}
		}
		
		//填写记录
		i=0;
		while (rs.next()) {
			i = i + 1;
			for (j = 1; j <= miCols; j++) {
				cellsTemp[i+miRows+iArrRows][j]=new Cell("");
				if (rs.getString(j-1) != null) {
					cellsTemp[i+miRows+iArrRows][j].value = rs.getString(j-1);
				}
			}
			
		}
		cells=cellsTemp;
		miRows=iAllRows;
		getPages();
	}
	
	public void AddTableData(String[][] arrString,ResultSet rs){
		int i=0;
		int j=0;
		
		int iArrRows=0;
		int iRsRows=0;
		int iAllRows=0;
		
		
		Cell cellsTemp[][] = null;
		try {
			if (!rs.isAfterLast()) {
				rs.last();
				iRsRows = rs.getRow();
				rs.beforeFirst();
				
				iArrRows=arrString.length;
				iAllRows=iArrRows+miRows+iRsRows;
				
				cellsTemp = new Cell[iAllRows+ 1][miCols + 1];
				
				rows = new Row[iAllRows+1];
				for (i=1;i<=iAllRows;i++){
					rows[i]=new Row();
				}
				
				//复制原有的数据
				for (i = 1; i <=miRows; i++) {
					for (j = 1; j <=miCols; j++) {
						cellsTemp[i][j] =cells[i][j];
					}
				}
				
				//填写数组
				for (i = 1; i <= arrString.length; i++) {
					for (j = 1; j <= arrString[0].length; j++) {
						cellsTemp [miRows+i][j]= new Cell(arrString[i - 1][j - 1]);
					}
				}
				
				//填写记录
				i=0;
				while (rs.next()) {
					i = i + 1;
					for (j = 1; j <= miCols; j++) {
						cellsTemp[i+miRows+iArrRows][j]=new Cell("");
						if (rs.getString(j) != null) {
							cellsTemp[i+miRows+iArrRows][j].value = rs.getString(j);
						}
					}
					
				}
				cells=cellsTemp;
				miRows=iAllRows;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void InsertFixedRows(Table tb) {
		if(tb == null) {
			return;
		}
		int iRows=0;
		int i=0;
		int j=0;
		Cell cellsTemp[][] = null;
		Row rowsTemp[] = null;
		iRows = tb.getRows();
		
		cellsTemp = new Cell[miRows +iRows+ 1][miCols + 1];
		
		rowsTemp = new Row[miRows + iRows+1];
		
//		复制原有的数据
		for (i = iRows + 1; i <= miRows +iRows ; i++) {
			for (j = 1; j <=miCols; j++) {
				cellsTemp[i][j] =cells[i-iRows][j];
			}
			rowsTemp[i]=rows[i-iRows];
		}
		
//		填写表头数据
		for(i = 1;i<=iRows;i++) {
			for (j = 1; j <=miCols; j++) {
				cellsTemp[i][j] = tb.getCell(i, j);
			}
			rowsTemp[i]=tb.rows[i];
		}
		rows = rowsTemp;
		cells=cellsTemp;
		miFixedRows = miFixedRows + iRows;
		miRows=miRows+iRows;
		setFixedRow(1,miFixedRows,true);
		getPages();
		
	}
	
	public void AddTableData(ResultSetList rs){
		int iRows=0;
		int i=0;
		int j=0;
		
		Cell cellsTemp[][] = null;
		iRows = rs.getRows();
		
		//iniTable(miRows+iRows+1, miCols+1);
		cellsTemp = new Cell[miRows +iRows+ 1][miCols + 1];
		//cols = new Column[miCols + 1];
		rows = new Row[miRows + iRows+1];
		
		//复制原有的数据
		for (i = 1; i <=miRows; i++) {
			for (j = 1; j <=miCols; j++) {
				cellsTemp[i][j] =cells[i][j];
			}
			rows[i]=new Row();
		}
		
		//填写记录
		i=0;
		while (rs.next()) {
			i = i + 1;
			for (j = 1; j <= miCols; j++) {
				cellsTemp[i+miRows][j]=new Cell();
				if (rs.getString(j-1) != null) {
					cellsTemp[i+miRows][j].value = rs.getString(j-1);
				} else {
					cellsTemp[i+miRows][j].value = "";
				}
			}
			rows[i+miRows]=new Row();
		}
		cells=cellsTemp;
		miRows=miRows+iRows;
		getPages();
	}
	
	public void AddTableData(ResultSet rs){
		int iRows=0;
		int i=0;
		int j=0;
		
		Cell cellsTemp[][] = null;
		try {
			if (!rs.isAfterLast()) {
				rs.last();
				iRows = rs.getRow();
				rs.beforeFirst();
				
				//iniTable(miRows+iRows+1, miCols+1);
				cellsTemp = new Cell[miRows +iRows+ 1][miCols + 1];
				//cols = new Column[miCols + 1];
				rows = new Row[miRows + iRows+1];
				
				//复制原有的数据
				for (i = 1; i <=miRows; i++) {
					for (j = 1; j <=miCols; j++) {
						cellsTemp[i][j] =cells[i][j];
					}
					rows[i]=new Row();
				}
				
				
				//填写记录
				i=0;
				while (rs.next()) {
					i = i + 1;
					for (j = 1; j <= miCols; j++) {
						cellsTemp[i+miRows][j]=new Cell();
						if (rs.getString(j) != null) {
							cellsTemp[i+miRows][j].value = rs.getString(j);
						} else {
							cellsTemp[i+miRows][j].value = "";
						}
					}
					rows[i+miRows]=new Row();
				}
				cells=cellsTemp;
				miRows=miRows+iRows;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setBorderNone() {
		miBorderLeft = 0;
		miBorderRight = 0;
		miBorderTop = 0;
		miBorderBottom = 0;
	}

	public void setBorder(int left, int right, int top, int bottom) {
		miBorderLeft = left;
		miBorderRight = right;
		miBorderTop = top;
		miBorderBottom = bottom;
	}

	public void setBorderDefalut() {
		miBorderLeft = 2;
		miBorderRight =1;
		miBorderTop = 2;
		miBorderBottom = 1;
	}

	// 填写页脚数据
	public void setFooterData(String[][] arrString) {
		int i = 0;
		int j = 0;
		if (arrString == null) {
			return;
		} else {
			int iStarRow = miRows - miFooterRows;
			for (i = 1; i <= arrString.length; i++) {
				for (j = 1; j <= arrString[0].length; j++) {
					if (j <= miCols) {
						cells[iStarRow + i][j] = new Cell(
								arrString[i - 1][j - 1]);
					}
				}
			}
		}
		getPages();
	}

	// 设置Col序号
	public void SetColXuh(int iRow, int iStartCol) {
		int intIndex = 0;
		if (iRow <= miRows) {
			for (int i = iStartCol; i <= miCols; i++) {
				intIndex = intIndex + 1;
				cells[iRow][i].value = String.valueOf(intIndex);
			}
		}
	}

	// 设置Row序号
	public void SetRowXuh(int iCol, int iStartRow) {
		int intIndex = 0;
		if (iStartRow <= miRows) {
			for (int i = iStartRow; i <= miRows; i++) {
				intIndex = intIndex + 1;
				cells[i][iCol].value = String.valueOf(intIndex);
			}
		}
	}

	// 设置表头的数据
	public void setHeaderData(String[][] arrString) {
		int i = 0;
		int j = 0;
		for (i = 1; i <= arrString.length; i++) {
			for (j = 1; j <= arrString[0].length; j++) {
				if (j <= miCols) {
					cells[i][j].value = arrString[i - 1][j - 1];
					cells[i][j].align = Table.ALIGN_CENTER;

				}
			}
		}
		miFixedRows = arrString.length;
		setFixedRow(1,miFixedRows,true);
		getPages();
	}

	// 列头宽
	public void setWidth(int[] arrInt) {
		for (int j = 1; j <= arrInt.length; j++) {
			if (j <= miCols) {
				cols[j].width = arrInt[j - 1];
			}
		}
	}

	private void iniTableData() {
		int i, j;
		for (i = 0; i <= miRows; i++) {
			for (j = 0; j <= miCols; j++) {
				cells[i][j] = new Cell();
			}
			rows[i] = new Row();
			rows[i].height = defaultRowHeight;
		}
		for (i = 0; i <= miCols; i++) {
			cols[i] = new Column();
			cols[i].width = defaultColWidth;
		}
	}

	private void iniTableData(String strDefault) {
		int i, j;
		for (i = 0; i <= miRows; i++) {
			for (j = 0; j <= miCols; j++) {
				cells[i][j] = new Cell(strDefault);
			}
		}
	}

	// 由默认的cell的属性创建
	private void iniTableData(Cell c) {
		int i, j;
		for (i = 0; i <= miRows; i++) {
			for (j = 0; j <= miCols; j++) {
				cells[i][j] = new Cell(c);
			}
			rows[i] = new Row();
			rows[i].height = defaultRowHeight;
		}
		for (i = 0; i <= miCols; i++) {
			cols[i] = new Column();
			cols[i].width = defaultColWidth;
		}
	}

	// 初始
	private void iniTable(int iRows, int iCols) {
		miRows = iRows;
		miCols = iCols;
		cells = new Cell[miRows + 1][miCols + 1];
		cols = new Column[miCols + 1];
		rows = new Row[miRows + 1];
	}

	public int getRows() {
		return miRows;
	}

	public int getFooterRows() {
		return miFooterRows;
	}

	public int getFixedRows() {
		return miFixedRows;
	}

	public void setFixedRows(int iRows) {
		miFixedRows = iRows;
		setFixedRow(1,iRows,true);
	}

	public void setFixedRowsAlign(int iAlign) {
		if (miRows>0){
			for (int i=1;i<=miFixedRows;i++){
				this.setRowCells(i, Table.PER_ALIGN , iAlign);
			}
		}
	}
	
	public int getCols() {
		return miCols;
	}

	// 把数组数据填充到已初始化的Table中
	public boolean fillData(String[][] arrString, int iStartRow, int iStartCol) {
		int i, j;
		for (i = 1; i <= arrString.length; i++) {
			if (i + iStartRow - 1 > miRows) {
				System.out.println("fillData数组越界！i+iStartRow>miRows>" + miRows);
				return false;
			}

			for (j = 1; j <= arrString[0].length; j++) {
				if (j <= miCols) {
					cells[i + iStartRow - 1][j + iStartCol - 1].value = arrString[i - 1][j - 1];
					cells[i + iStartRow - 1][j + iStartCol - 1].align = Table.ALIGN_CENTER;
					if (cells[i + iStartRow - 1][j + iStartCol - 1].value == null) {
						cells[i + iStartRow - 1][j + iStartCol - 1].value = "";
					}
				}
			}
		}
		return true;
	}

	// 把一维数组填到一行中
	public boolean fillRowData(String[] arrString, int iRow) {
		int j;
		for (j = 1; j <= arrString.length; j++) {
			if (iRow > miRows) {
				System.out.println("fillRowData数组越界！iRow>miRows>" + miRows);
				return false;
			}
			if (j <= miCols) {
				cells[iRow][j].value = arrString[j - 1];
				cells[iRow][j].align = Table.ALIGN_CENTER;
				if (cells[iRow][j].value == null) {
					cells[iRow][j].value = "";
				}
			}
		}
		return true;
	}

	public int splitPages(){
		int iHeight=0;
		int iPages=0;
		int iFixedHeight=0;
		int iCurHeight=0;
		boolean isNewFixed=false;
		
		if (mHasSplit) {//如果已经计算过分页了，则不再重新计算 
			return miPages;
		}
		mHasSplit=true;
		if (miPageMaxHeight==0){
			if (miRows==0){
				return 0;
			}
			return 1;
		}
		
		iFixedHeight=0;//getFixedHeadHeight(1);//得到固定表头的高度
		iHeight=iFixedHeight;
		
		//在开始行作页标记
		if (rows.length>0){
			rows[1].newPage=true;
			iPages=iPages+1;
		}
		
		for (int i=1;i<=miRows;i++){
			//清除上次的分页标记
			if (i!=1){
				rows[i].newPage=false;
			}
			iCurHeight=rows[i].height;//当前行高
			if (rows[i].customNewPage){//如果是用户强制换页的行，则换页
				iPages=iPages+1;
				iHeight=iFixedHeight+iCurHeight;
				rows[i].newPage=true;
			}else if(!rows[i].hidden){//如果行不是隐藏的
				if (rows[i].fixed ){//如果是多表头其中的固定行，要获得固定表头的高度
					iFixedHeight=getFixedHeadHeight(i);
					iCurHeight=iFixedHeight;
					isNewFixed=true;
				}else{
					isNewFixed=false;
				}
				
				if (iHeight+iCurHeight>miPageMaxHeight){
					//本行需要换页
					rows[i].newPage=true;
					if (isNewFixed){
						i=i+getFixedRowsFromRow(i)-1;//移到固定行的末行
						iHeight=iFixedHeight;
					}else{
						iHeight=iCurHeight+iFixedHeight;//如果是数据行换页，初始加入固定行的高度
					}
					
					iPages=iPages+1;
				}else{
					if (isNewFixed){//跳到固定行的下一数据行
						i=i+getFixedRowsFromRow(i)-1;
					}
					iHeight=iHeight+iCurHeight;
				}
			}
		} 
		miPages=iPages;
		return miPages;
	}
	
	private void clearPageFlag(int iRow){
		for (int i=iRow;iRow<=miRows;iRow++){
			if (!rows[i].hidden){
				if (rows[i].fixed){
					rows[i].newPage=false;
				}else{
					break;
				}
			}
		}
	}
	
	private void setFixedRow(int iStartRow,int iEndStart, boolean blnFixed){
		if (iEndStart<=miRows){
			for (int i=iStartRow;i<=iEndStart;i++)
			rows[i].fixed=blnFixed;
		}
	}
	
	//从固定列的开始行得到
	public int getFixedHeadHeight(int iRow){
		int iHeight=0;
		if (iRow>miRows){
			return 0;
		}
		for (int i=iRow;i<=miRows;i++){
			if (!rows[i].hidden){
				if (rows[i].fixed){
					iHeight=iHeight+rows[i].height;
				}else{
					return iHeight;
				}
			}
		}
		return iHeight;
	}
	
	public int getFixedRowsFromRow(int iRow){
		int iRows=0;
		if (iRow>miRows){
			return 0;
		}
		for (int i=iRow;i<=miRows;i++){
			if (rows[i].fixed){
				iRows=iRows+1;
			}else{
				return iRows;
			}
		}
		return iRows;
	}

	private boolean hasMutiTableHead(){
		return (mTableHeadData==null);
	}
	
	// 计算有多少页
	public int getPages() {
		if (miSplitPageStyle==Report.PAGE_ROWS ){
			if (miPageRows == -1 || miPageRows == 0) {
				miPages = 1;
			} else {
				if ((miRows - miFixedRows) % miPageRows == 0) {
					miPages = (miRows - miFixedRows) / miPageRows;
				} else {
					miPages = (int) Math.floor((double) (miRows - miFixedRows)
							/ (double) miPageRows) + 1;
				}
			}
		}else{
			if (mHasSplit==false) {//如果已经计算过分页了，则不再重新计算 
				splitPages();
			}
			
		}
		if (miPages==0){
			miPages=1;
		}
		return miPages;
	}

	// 得到cell
	public Cell getCell(int i, int j) {
		if (i <= miRows && i > 0) {
			return cells[i][j];
		} else {
			System.out.println("getCell:行i" + i + "或列j" + j + "越界!");
			return new Cell();
		}
	}

	// 合并
	public void mergeRowCells(int iRow) {
		if (iRow < 0 || iRow > miRows) {
			System.out.println("mergeRowCells:行iRow" + iRow + "越界!");
		}
		for (int i = 2; i <= miCols; i++) {
			cells[iRow][i].used = false;
		}
		cells[iRow][1].colSpan = miCols;
	}

	// 直接合并
	// （合并单元格第一格的数据为合并后的值）在设置PageRows后使用（否则当合并行在两页上时就会有显示有问题）
	public void mergeCell(int iStartRow, int iStartCol, int iEndRow, int iEndCol) {
		int i, j;
		int iRowTemp = 0;

		if (iStartRow <= 0 || iStartCol <= 0 || iEndRow <= 0 || iEndCol <= 0
				|| iStartRow > miRows || iEndRow > miRows || iEndCol > miCols
				|| iStartCol > miCols) {
			System.out.println("错误的行或列！");
		}

		for (i = iStartRow; i <= iEndRow; i++) {
			for (j = iStartCol; j <= iEndCol; j++) {
				if (!cells[iStartRow][j].used
						|| cells[iStartRow][j].rowSpan > 1
						|| cells[iStartRow][j].colSpan > 1) {
					System.out.println("合并区域重叠！");
				}
			}
		}

		if (miPageRows == 0 || miPageRows == -1) {
			miPageRows = miRows;
		}

		iRowTemp = iStartRow;

		for (i = iStartRow; i <= iEndRow; i++) {
			for (j = iStartCol + 1; j <= iEndCol; j++) {
				cells[i][j].used = false;
			}

			if (isPageStartRow(i) || (iStartRow == i)) {
				iRowTemp = i;
				cells[iRowTemp][iStartCol].colSpan = iEndCol - iStartCol + 1;
				cells[iRowTemp][iStartCol].rowSpan = 1;
			} else {
				cells[i][iStartCol].used = false;
				cells[iRowTemp][iStartCol].rowSpan = cells[iRowTemp][iStartCol].rowSpan + 1;
			}
		}
	}

	// 是否是页的第一行数据
	private boolean isPageStartRow(int iRow) {
		if (iRow < 0 || iRow > miRows) {
			System.out.println("isPageStartRow:行" + iRow + "越界!");
			return false;
		}
		if (miPageRows != -1) {
			if ((iRow - 1 - miFixedRows) % miPageRows == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 设置表格的线的样式
	public void setCellBorderLeft(int i, int j, int intBorder) {
		cells[i][j].border_left = intBorder;
	}

	public void setCellBorderRight(int i, int j, int intBorder) {
		cells[i][j].border_right = intBorder;
	}

	public void setCellBorderTop(int i, int j, int intBorder) {
		cells[i][j].border_top = intBorder;
	}

	public void setCellBorderbottom(int i, int j, int intBorder) {
		cells[i][j].border_bottom = intBorder;
	}

	public void setCellBorder(int i, int j, int intLeft, int intRight,
			int intTop, int intBottom) {
		cells[i][j].border_left = intLeft;
		cells[i][j].border_right = intRight;
		cells[i][j].border_top = intTop;
		cells[i][j].border_bottom = intBottom;
	}

	public void setCellBorderNone(int i, int j) {
		cells[i][j].border_left = 0;
		cells[i][j].border_right = 0;
		cells[i][j].border_top = 0;
		cells[i][j].border_bottom = 0;
	}

	// /////////////////////////////////////////////////////////////////////////
	// 合并相同的数据不考虑上一行和前一列的合并情况，在设置PageRows后使用（否则当合并行在两页上时就会有显示有问题）
	public void merge(int iStartRow, int iStartCol, int iEndRow, int iEndCol) {
		int i = 0;
		int j = 0;
		int k = 0;

		if (iStartRow > miRows) {
			System.out.println("merge:开始行iStartRow" + iStartRow + "越界!");
			return;
		} else if (iStartCol > miCols) {
			System.out.println("merge:开始列iStartCol" + iStartCol + "越界!");
			return;
		} else if (iStartRow > miRows) {
			System.out.println("merge:结束行iEndRow" + iEndRow + "越界!");
			return;
		} else if (iEndCol > miCols) {
			System.out.println("merge:结束列iEndCol" + iEndCol + "越界!");
			return;
		} else if (iStartRow > miRows) {
			System.out.println("merge:开始行iStartRow" + iStartRow
					+ "大于结束行iEndRow" + iEndRow + "!");
			return;
		} else if (iEndCol > miCols) {
			System.out.println("merge:开始列iStartCol" + iStartCol
					+ "大于结束列iEndCol" + iEndCol + "!");
			return;
		}

		// 计算colSpan
		for (i = iStartRow; i <= iEndRow; i++) {
			for (j = iStartCol; j <= iEndCol; j++) {
				if (cells[i][j].used
						&& (cells[i][j].rowSpan == 1 || cells[i][j].colSpan == 1)) {
					for (k = j; k <= iEndCol - 1; k++) {
						if (cells[i][j].value.equals(cells[i][k + 1].value)) {
							cells[i][j].colSpan = cells[i][j].colSpan + 1;
							cells[i][k + 1].used = false;
						} else {
							break;
						}
					}
				}
			}
		}

		// 计算row合并
		for (j = iStartCol; j <= iEndCol; j++) {
			for (i = iStartRow; i <= iEndRow; i++) {
				if (cells[i][j].used) {
					for (k = i; k <= iEndRow - 1; k++) {
						if (cells[i][j].value.equals(cells[k + 1][j].value) && rows[k+1].newPage==false
 								&& cells[i][j].colSpan == cells[k + 1][j].colSpan
								&& !isPageStartRow(k + 1)) {
							cells[i][j].rowSpan = cells[i][j].rowSpan + 1;
							cells[k + 1][j].used = false;
						} else {
							break;
						}
					}
				}
			}
		}
	}
    // 合并相同的数据不考虑上一行和前一列的合并情况，在设置PageRows后使用（否则当合并行在两页上时就会有显示有问题）	
	public void merge(boolean mergeNull, int iStartRow, int iStartCol, int iEndRow, int iEndCol) {
		int i = 0;
		int j = 0;
		int k = 0;

		if (iStartRow > miRows) {
			System.out.println("merge:开始行iStartRow" + iStartRow + "越界!");
			return;
		} else if (iStartCol > miCols) {
			System.out.println("merge:开始列iStartCol" + iStartCol + "越界!");
			return;
		} else if (iStartRow > miRows) {
			System.out.println("merge:结束行iEndRow" + iEndRow + "越界!");
			return;
		} else if (iEndCol > miCols) {
			System.out.println("merge:结束列iEndCol" + iEndCol + "越界!");
			return;
		} else if (iStartRow > miRows) {
			System.out.println("merge:开始行iStartRow" + iStartRow
					+ "大于结束行iEndRow" + iEndRow + "!");
			return;
		} else if (iEndCol > miCols) {
			System.out.println("merge:开始列iStartCol" + iStartCol
					+ "大于结束列iEndCol" + iEndCol + "!");
			return;
		}

		// 计算colSpan
		for (i = iStartRow; i <= iEndRow; i++) {
			for (j = iStartCol; j <= iEndCol; j++) {
				if (cells[i][j].used
						&& (cells[i][j].rowSpan == 1 || cells[i][j].colSpan == 1)) {
					for (k = j; k <= iEndCol - 1; k++) {
						
						if (cells[i][j].value.equals(cells[i][k + 1].value)) {
							if(!mergeNull && cells[i][k + 1].value.equals("")){
								break;
							}
							cells[i][j].colSpan = cells[i][j].colSpan + 1;
							cells[i][k + 1].used = false;
						} else {
							break;
						}
					}
				}
			}
		}

		// 计算row合并
		for (j = iStartCol; j <= iEndCol; j++) {
			for (i = iStartRow; i <= iEndRow; i++) {
				if (cells[i][j].used) {
					for (k = i; k <= iEndRow - 1; k++) {
						if (cells[i][j].value.equals(cells[k + 1][j].value) &&  rows[k+1].newPage==false
								&& cells[i][j].colSpan == cells[k + 1][j].colSpan
								&& !isPageStartRow(k + 1)) {
							if(!mergeNull && cells[k + 1][j].value.equals("")){
								break;
							}
							cells[i][j].rowSpan = cells[i][j].rowSpan + 1;
							cells[k + 1][j].used = false;
						} else {
							break;
						}
					}
				}
			}
		}
	}

	// 合并相同的数据时考虑上一行和前一列的合并情况，在设置PageRows后使用（否则当合并行在两页上时就会有显示有问题）
	public void merge(int iStartRow, int iStartCol, int iEndRow, int iEndCol,
			boolean blnFromConect) {
		int i = 0;
		int j = 0;
		int k = 0;

		if (iStartRow > miRows) {
			System.out.println("merge:开始行iStartRow" + iStartRow + "越界!");
			return;
		} else if (iStartCol > miCols) {
			System.out.println("merge:开始列iStartCol" + iStartCol + "越界!");
			return;
		} else if (iStartRow > miRows) {
			System.out.println("merge:结束行iEndRow" + iEndRow + "越界!");
			return;
		} else if (iEndCol > miCols) {
			System.out.println("merge:结束列iEndCol" + iEndCol + "越界!");
			return;
		} else if (iStartRow > miRows) {
			System.out.println("merge:开始行iStartRow" + iStartRow
					+ "大于结束行iEndRow" + iEndRow + "!");
			return;
		} else if (iEndCol > miCols) {
			System.out.println("merge:开始列iStartCol" + iStartCol
					+ "大于结束列iEndCol" + iEndCol + "!");
			return;
		}

		// 计算colSpan
		for (i = iStartRow; i <= iEndRow; i++) {
			for (j = iStartCol; j <= iEndCol; j++) {
				if (cells[i][j].used
						&& (cells[i][j].rowSpan == 1 || cells[i][j].colSpan == 1)) {
					for (k = j; k <= iEndCol - 1; k++) {
						if (!cells[i][k + 1].used) {
							break;
						}
						if (cells[i][j].value.equals(cells[i][k + 1].value)) {
							if (blnFromConect) {
								if (i > 1) {
									if (cells[i - 1][k + 1].used) {
										break;
									}
								}
							}
							cells[i][j].colSpan = cells[i][j].colSpan + 1;
							cells[i][k + 1].used = false;
						} else {
							break;
						}
					}
				}
			}
		}

		// 计算rowSpan
		for (j = iStartCol; j <= iEndCol; j++) {
			for (i = iStartRow; i <= iEndRow; i++) {
				if (cells[i][j].used) {
					for (k = i; k <= iEndRow - 1; k++) {
						if (!cells[k + 1][j].used) {
							break;
						}

						if (cells[i][j].value.equals(cells[k + 1][j].value) &&   rows[k+1].newPage==false
								&& cells[i][j].colSpan == cells[k + 1][j].colSpan
								&& !isPageStartRow(k + 1)) {
							if (blnFromConect) {
								if (j > 1) {
									if (cells[k + 1][j - 1].used) {
										break;
									}
								}
							}
							cells[i][j].rowSpan = cells[i][j].rowSpan + 1;
							cells[k + 1][j].used = false;
						} else {
							break;
						}
					}
				}
			}
		}
	}

	public void mergeFixedRow() {
		if (miFixedRows > 0) {
			merge(1, 1, miFixedRows, miCols);
		}
	}

	public void mergeFixedRowCol() {
		mergeFixedRow();
		if (getRows() > getFixedRows()) {
			mergeFixedCol(miFixedCols);
		}
	}

	public void mergeRow(int iRow) {
		if (iRow <= miRows) {
			merge(iRow, 1, iRow, miCols);
		} 
	}

	public void mergeCol(int iCol) {
		if (iCol > miCols) {
		} else {
			merge(miFixedRows + 1, iCol, miRows, iCol);
		}
	}
	
	public void mergeCol(int iCol,boolean m) {
		if (iCol > miCols) {
		} else {
			merge(miFixedRows + 1, iCol, miRows, iCol, m);
		}
	}

	public void mergeFixedCol(int iFixedCols) {
		if (iFixedCols <miCols) {
			for (int i = 1; i <= iFixedCols; i++) {
				merge(miFixedRows + 1, i, miRows - miFooterRows, i, true);
			}
		}
	}

	public void mergeFixedCols() {
		if (miFixedRows > 0 && miFixedCols > 0) {
			merge(miFixedRows, 1, miRows, miFixedCols, true);
		}
	}

	private boolean hasCell(int i, int j) {
		if ((i > 0 && i <= miRows) || (j > 0 && j <= miCols)) {
			return true;
		} else {
			return false;
		}
	}

	public void setCellImage(int i, int j, int intWidth, int intHeight,
			String strImage) {
		if (!hasCell(i, j)) {
			return;
		}

		if (strImage == null) {
			cells[i][j].value = "";
			return;
		}

		if (strImage.equals("")) {
			cells[i][j].value = "";
			return;
		}
		cells[i][j].value = "<img style='WIDTH:" + intWidth + " px; HEIGHT: "
				+ intHeight + "' src='" + strImage + "'" + ">";
	}

	public void setCellValue(int i, int j, String strValue) {
		if (strValue == null) {
			return;
		}
		cells[i][j].value = strValue;
	}

	public String getCellValue(int i, int j) {
		return cells[i][j].value;
	}

	public void setCellValue(int i, int j, String strValue, int iMergerCols) {
		if (strValue == null) {
			return;
		}
		cells[i][j].value = strValue;
		this.mergeCell(i, j, i, j + iMergerCols - 1);
	}

	public void setCellAlign(int i, int j, int intAlign) {
		cells[i][j].align = intAlign;
	}

	public void setCellVAlign(int i, int j, int intAlign) {
		cells[i][j].valign = intAlign;
	}

	public void setCellFont(int i, int j, String strName, int intSize,
			boolean blnBold) {
		cells[i][j].fontName = strName;
		cells[i][j].fontSize = intSize;
		cells[i][j].fontBold = blnBold;
	}

	public void setCellFontName(int i, int j, String strName) {
		cells[i][j].fontName = strName;
	}

	public void setCellFontSize(int i, int j, int intSize) {
		cells[i][j].fontSize = intSize;
	}

	public void setCellFontBold(int i, int j, boolean blnBold) {
		cells[i][j].fontBold = blnBold;
	}

	public void setCellFontColor(int i, int j, String lngColor) {
		cells[i][j].foreColor = lngColor;
	}
	
	public boolean rowIsHidden(int iRow){
		for (int i=1;i<=miCols;i++){
			if (cells[iRow][i].used){
				return false;
			}
		}
		return true;
	}
	
	private StringBuffer getTr(int iRow){
		StringBuffer sb=new StringBuffer();
		sb.append("height=").append(rows[iRow].height);
		
		if (useCss){
			if ("".equals(rows[iRow].className)) {
				if (iRow<=miFixedRows){
					if (!"".equals(colHeadClass)){
						sb.append(" class=").append(colHeadClass);
					}
				}else{
					if (!"".equals(firstDataRowClass)){
						if ((iRow-miFixedRows) % 2==0){
							sb.append(" class=").append(secondDataRowClass);
						}else{
							sb.append(" class=").append(firstDataRowClass);
						}
					}
				}
			}else
			if("single".equals(rows[iRow].className)){
				if (iRow<=miFixedRows){
						sb.append(" class=tab_colheader");
				}else{
					if ((iRow-miFixedRows) % 2==0){
						sb.append(" class=tab_data_line_two");
					}else{
						sb.append(" class=tab_data_line_one");
					}
				}
			}else{
				sb.append(" class=").append(rows[iRow].className);
			}
		}
		return sb;
	}

	// 得到不分页的html
	public String getHtml() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(getTableH("oData1"));
		for (int i = 1; i <= miRows; i++) {
			sb.append(getRowHtml(i));
		}
		sb.append("</table>\n");
		
		return  sb.toString();
	}
	
	
	public String getTitleHtml(int iPage,int iPages ) {
		StringBuffer sb = new StringBuffer();

		miCurrentPage=iPage;
		miPages=iPages;

		sb.append(getTableH(""));
		for (int i = 1; i <= miRows; i++) {
			sb.append(getRowHtml(i));
		}
		sb.append("</table>\n");
		
		return sb.toString();
	}
	
	public StringBuffer getRowHtml(int iRow){
		StringBuffer sb = new StringBuffer();

		if (!rows[iRow].hidden){
			//sb.append("<tr onclick=\"classchange(this,'onclick',oData"+miCurrentPage+");\"").append(getTr(iRow)).append(">\n");
			sb.append("<tr ").append(getTr(iRow)).append(">\n");
			for (int j = 1; j <= miCols; j++) {
				if (cells[iRow][j].used) {
					sb.append(getCellHtml(iRow, j));
				}
			}
			sb.append("</tr>\n");
		}
		return sb;
	}
	
	// 得到某一页的html
	public String getHtml(int iPage) {
		StringBuffer sb = new StringBuffer();
		
		int i;
		int iStartRow = 0;
		int iEndRow = 0;
		int iFiexdRowTmp=0;
		
		getPages();
		miCurrentPage = iPage;

		iStartRow = getPageStartRow(iPage);
		iEndRow = getPageEndRow(iPage);

		sb.append(getTableH("oData"+iPage));
		if (mTableHeadData==null ){
			for (i = 1; i <= miFixedRows; i++) {
				sb.append(getRowHtml(i));
			}
		}else{
			//得到 miFixedRows
			for (i=mTableHeadData.length-1;i>0;i--){
				if (mTableHeadData[i][0]<=iStartRow){
					iFiexdRowTmp=i;
					break;
				}
			}
			for (i = mTableHeadData[iFiexdRowTmp][0]; i <=mTableHeadData[iFiexdRowTmp][1]; i++) {
				sb.append(getRowHtml(i));
			}
		}

		for (i = iStartRow; i <= iEndRow; i++) {
			if (!(isFixedRowData(i) && (isFixedRowData(iEndRow) ||  isFixedRowData(iStartRow)))){

				if (!isFixedRowData(i,iFiexdRowTmp)){
					sb.append(getRowHtml(i));
				}	
			}
		}
		sb.append("</table>\n");
		return sb.toString();
	}

	public boolean isFixedRowData(int iRow){
		if (mTableHeadData==null ){
			return false;
		}else{
			for (int i=0;i<mTableHeadData.length; i++){
				if( iRow>=mTableHeadData[i][0] && iRow<=mTableHeadData[i][1]){
					return true;
				}
			}
			return false;
		}
	}
	public boolean isFixedRowData(int iRow,int iFiexdRowPosIndex){
		if (mTableHeadData==null ){
			return false;
		}else{
			if( iRow>=mTableHeadData[iFiexdRowPosIndex][0] && iRow<=mTableHeadData[iFiexdRowPosIndex][0]){
				return true;
			}else{
				return false;
			}
		}
	}
	// 得到页开始于第几行
	public int getPageStartRow(int iPage) {
		int iStartRow = 0;

		if (miPages == -1) {
			iStartRow = 1;
		} else {
			iStartRow = (iPage - 1) * miPageRows + 1 + miFixedRows;
		}
		return iStartRow;
	}

	// 得到页结束于第几行
	public int getPageEndRow(int iPage) {
		int iEndRow = 0;
		if (miPageRows == -1) {
			iEndRow = miRows;
		} else {
			iEndRow = iPage * miPageRows + miFixedRows;
			if (iEndRow > miRows) {
				iEndRow = miRows;
			}
		}
		return iEndRow;
	}

	// 设置某一列cell的数据
	public void setColCells(int iCol, int intProperty, int intValue) {
		setCells(miFixedRows + 1, iCol, miRows, iCol, intProperty, intValue);
	}

	// 设置某一列cell的数据
	public void setColCells(int iCol, int intProperty, String strValue) {
		setCells(miFixedRows + 1, iCol, miRows, iCol, intProperty, strValue);
	}

	// 设置某一列cell的数据
	public void setColCells(int iCol, int intProperty, boolean blnValue) {
		setCells(miFixedRows + 1, iCol, miRows, iCol, intProperty, blnValue);
	}

	// 设置某一行cell的数据
	public void setRowCells(int iRow, int intProperty, int intValue) {
		setCells(iRow, 1, iRow, miCols, intProperty, intValue);
	}

	// 设置某一行cell的数据
	public void setRowCells(int iRow, int intProperty, String strValue) {
		setCells(iRow, 1, iRow, miCols, intProperty, strValue);
	}

	// 设置某一行cell的数据
	public void setRowCells(int iRow, int intProperty, boolean blnValue) {
		setCells(iRow, 1, iRow, miCols, intProperty, blnValue);
	}

	// 设置某范围内cells的数据
	public void setCells(int iRow, int iCol, int iEndRow, int iEndCol,
			int intProperty, int intValue) {
		if (iRow <= 0 || iRow > miRows) {
			return ;
		}

		if (iEndRow <= 0 || iEndRow > miRows) {
			return ;
		}

		if (iCol <= 0 || iCol > miCols) {
			return ;
		}

		if (iEndCol <= 0 || iEndCol > miCols) {
			return ;
		}

		switch (intProperty) {
		case Table.PER_ALIGN:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].align = intValue;
				}
			}
			break;
		case Table.PER_FONTSIZE:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].fontSize = intValue;
				}
			}
			break;

		case Table.PER_VALIGN:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].valign = intValue;
				}
			}
			break;
		case Table.PER_PADDING:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].padding = intValue;
				}
			}
			break;
		case Table.PER_BORDER_LEFT:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].border_left = intValue;
				}
			}
			break;
		case Table.PER_BORDER_RIGHT:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].border_right = intValue;
				}
			}
			break;
		case Table.PER_BORDER_TOP:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].border_top = intValue;
				}
			}
			break;

		case Table.PER_BORDER_BOTTOM:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].border_bottom = intValue;
				}
			}
			break;
		}
	}

	// 设定范围内cell的数据
	public void setCells(int iRow, int iCol, int iEndRow, int iEndCol,
			int intProperty, String strValue) {
		if (iRow <= 0 || iRow > miRows) {
			return ;
		}

		if (iEndRow <= 0 || iEndRow > miRows) {
			return ;
		}

		if (iCol <= 0 || iCol > miCols) {
			return ;
		}

		if (iEndCol <= 0 || iEndCol > miCols) {
			return ;
		}

		switch (intProperty) {
		case Table.PER_VALUE:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].value = strValue;
				}
			}
			break;
		case Table.PER_FONTNAME:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].fontName = strValue;
				}
			}
			break;
		case Table.PER_FORECOLOR:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].foreColor = strValue;
				}
			}
			break;
		case Table.PER_BACKCOLOR:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].backColor = strValue;
				}
			}
			break;
		}
	}

	// 设定范围内cell的数据
	public void setCells(int iRow, int iCol, int iEndRow, int iEndCol,
			int intProperty, boolean blnValue) {
		if (iRow <= 0 || iRow > miRows) {
			iRow = 1;
		}

		if (iEndRow <= 0 || iEndRow > miRows) {
			iEndRow = 1;
		}

		if (iCol <= 0 || iCol > miCols) {
			iCol = 1;
		}

		if (iEndCol <= 0 || iEndCol > miCols) {
			iEndCol = 1;
		}

		switch (intProperty) {
		case Table.PER_USED:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].used = blnValue;
				}
			}
			break;
		case Table.PER_FONTBOLD:
			for (int i = iRow; i <= iEndRow; i++) {
				for (int j = iCol; j <= iEndCol; j++) {
					cells[i][j].fontBold = blnValue;
				}
			}
			break;
		}
	}

	//	设定cell的className
	public void setCellClassName(int iRow, int iCol, String className) {
		if (iRow <= 0 || iRow > miRows) {
			iRow = 1;
		}

		if (iCol <= 0 || iCol > miCols) {
			iCol = 1;
		}
		
		cells[iRow][iCol].className = className;
	}
	
	public void setColClassName(int iCol, String className) {
		if (iCol <= 0 || iCol > miCols) {
			return;
		}
		cols[iCol].className=className;
	}

	// 设置列对齐
	public void setColAlign(int iCol, int intAlign) {
		setColCells(iCol, Table.PER_ALIGN, intAlign);
	}

	// 设置列垂直对齐
	public void setColVAlign(int iCol, int intAlign) {
		setColCells(iCol, Table.PER_VALIGN, intAlign);
	}

	// 设置列宽
	public void setColWidth(int iCol, int iColWidth) {
		if (iCol <= miCols) {
			cols[iCol].width = iColWidth;
		}
	}

	// 设置行高
	public void setRowHeight(int iRow, int iRowHeight) {
		if (iRow <= miRows) {
			rows[iRow].height = iRowHeight;
		}
	}

	public void setRowHeight(int iRowHeight) {
		for (int i = 1; i <= rows.length - 1; i++) {
			rows[i].height = iRowHeight;
		}
	}
	
	public void setRowClassName(int iRow, String className) {
		if (iRow <= miRows) {
			rows[iRow].className = className;
		} 
	}

	// 得到表宽度
	public int getWidth() {
		int width = 0;
		for (int i = 1; i <= miCols; i++) {
			width = width + cols[i].width;
		}
		return width;
	}

	// 得到表宽度
	public int getHeight() {
		int height = 0;
		for (int i = 1; i <= miRows; i++) {
			height = height + rows[i].height;
		}
		return height;
	}
	
	// 得到cell的宽度
	public int getCellWidth(int i, int j) {
		if (cells[i][j].colSpan == 1) {
			return cols[j].width;
		} else {
			int intWidth = 0;
			for (int k = 0; k < cells[i][j].colSpan; k++) {
				if (j + k > miCols) {
					System.out.println("getCellWidth数组访问越界:Row" + i + ",Col"
							+ j);
				} else {
					intWidth = intWidth + cols[j + k].width;
				}
			}
			return intWidth;
		}
	}

	// 得到cell的高度
	public int getCellHeight(int i, int j) {
		if (cells[i][j].rowSpan == 1) {
			return rows[i].height;
		} else {
			int intHeight = 0;
			for (int k = 0; k < cells[i][j].rowSpan; k++) {
				if ((i + k) > miRows) {
					System.out.println("getCellHeight数组访问越界:" + i + "," + j);
				} else {
					intHeight = intHeight + rows[i + k].height;
				}
			}
			return intHeight;
		}
	}

	// 得到表的定义
	public String getTableH(String strID ) {
		StringBuffer sb = new StringBuffer("<table ");
		if (!strID.equals("")) {
			sb.append(" id=\""+strID+"\"" );
		}
		sb.append(" width=").append(getWidth());
		sb.append(" cellPadding=").append(cellpadding);
		sb.append(" cellSpacing=").append(cellspacing);

		if (border>0){
			sb.append(" border=").append(border);
		}
		
		if (!bgColor.equals("")) {
			sb.append(" bgcolor=").append(bgColor);
		}
		
		if (!bgImg.equals("")) {
			sb.append(" background='").append(bgImg).append("'");
		}
		
		if (!bordercolordark.equals("")){
			sb.append(" bordercolordark=").append(bordercolordark);
		}
		
		if (!bordercolorlight.equals("")){
			sb.append(" bordercolorlight=").append(bordercolorlight);
		}
		
		if (useCss){
			if (!tableClass.equals("")){
				sb.append(" class=").append(tableClass);
			}
		}else{
			sb.append(" style=\"");

			if (!fontName.equals("-1")) {
				sb.append("font-family:").append(fontName).append(";");
			}

			if (!borderColor.equals("")){
				sb.append(" boder-color:").append(borderColor).append(";");
			}else{
				sb.append("border-color:#000000;");
			}
			
			if (fontSize != -1) {
				sb.append("font-size:").append(fontSize).append("pt;");
			}
			
			sb.append("border-style:solid;");
			sb.append("border-width:");
			sb.append(miBorderTop).append("px ");
			sb.append(miBorderRight).append("px ");
			sb.append(miBorderBottom).append("px ");
			sb.append(miBorderLeft ).append("px;");
			sb.append("\"");
		}
		sb.append(">\n");
		return sb.toString();
	}
	
	private String getCellStyleName(String strKey, int i ,int j){
		String strName ="";
		StringBuffer sb=getCellStyle(i,j);
		int iSize=0;

		if (sb.length()>0 ){
			if (mhmStyle.containsKey(sb.toString())){//如果存在style 返回id
				strName=strKey+"_class_"+mhmStyle.get(sb.toString()).toString(); 
			}else{
				iSize=mhmStyle.size();
				mhmStyle.put(sb.toString(), String.valueOf(iSize));
				strName=strKey +"_class_"+iSize;
			}
			return strName;
		}else{
			return "";
		}
	}
	
	public StringBuffer getCellStyle( int i ,int j){
		Cell c=cells[i][j];
		StringBuffer sb=new StringBuffer();

		boolean blnNoBorder=false;
		if (!c.fontName.equals("")) {
			if (!c.fontName.equals(fontName)) {
				sb.append("font-family:").append(c.fontName).append(";");
			}
		}else if (!"".equals(cols[j].fontName)){
			sb.append("font-family:").append(cols[j].fontName).append(";");
		}

		if (c.fontSize != -1) {
			if (c.fontSize != fontSize) {
				sb.append(" font-size:").append(c.fontSize).append("pt;");
			}
		}else if (!(cols[j].fontSize==-1)){
			sb.append(" font-size:").append(cols[j].fontSize).append("pt;");
		}

		if (c.fontBold) {
			sb.append(" font-weight: bold;");
		}else if (cols[j].fontBold){
			sb.append(" font-weight: bold;");
		}
		
		if (!c.foreColor.equals("")) {
			sb.append(" color:").append(c.foreColor).append(";");
		}else if(!"".equals(cols[j].foreColor)){
			sb.append(" color:").append(cols[j].foreColor).append(";");
		}

		if (!c.backColor.equals("")) {
			sb.append(" background-color:").append(c.backColor).append(";");
		}else if (!"".equals(cols[j].bgColor)){
			sb.append(" background-color:").append(cols[j].bgColor).append(";");
		}
		
		if (!useCss){
			sb.append("border-width:");
			if (c.border_top==c.border_right && c.border_top==c.border_bottom && c.border_top== c.border_left){
				sb.append(c.border_top).append("px;");
				if (c.border_top==0) { 
					blnNoBorder=true;
				}
			}else{
				sb.append(c.border_top).append("px ");
				sb.append(c.border_right).append("px ");
				sb.append(c.border_bottom).append("px ");
				sb.append(c.border_left ).append("px;");
			}
			
			if (!blnNoBorder){
				if (c.border_top_color.equals(c.border_right_color) && c.border_top_color.equals(c.border_bottom_color) && c.border_top_color.equals(c.border_left_color )){
					if (c.border_top_color.equals("")){
						if (borderColor.equals("")){
							sb.append("border-color:#000000;");
						}else{
							sb.append("border-color:").append(borderColor).append(";");
						}
					}else{
						sb.append("border-color:").append(c.border_top_color).append(";");
					}
				}else{
					sb.append("border-color:");
					sb.append(c.border_top_color).append(" ");
					sb.append(c.border_right_color).append(" ");
					sb.append(c.border_bottom_color).append(" ");
					sb.append(c.border_left_color ).append(";");
				}
				
				if (c.border_top_style.equals(c.border_right_style) && c.border_top_style.equals(c.border_bottom_style) && c.border_top_style.equals(c.border_left_style)){
					if (c.border_top_style.equals("")){
						sb.append("border-style:solid;");
					}else{
						sb.append("border-style:").append(c.border_top_style).append(";");
					}
				}else{
					sb.append("border-style:");
					sb.append(c.border_top_style).append(" ");
					sb.append(c.border_right_style).append(" ");
					sb.append(c.border_bottom_style).append(" ");
					sb.append(c.border_left_style).append(";");
				}
			}
		}
		
		return  sb;
	}
	
	public String getAllStylesDefine(){
		Iterator   ite   =   mhmStyle.entrySet().iterator();   
		Map.Entry  itm;   
		StringBuffer sbStyle=new StringBuffer();
		
		while(ite.hasNext()){   
			itm   =   (Map.Entry)ite.next();
	        sbStyle.append(".").append(TableID).append("_class_").append(itm.getValue());
	        sbStyle.append("{\n");
	        sbStyle.append(itm.getKey());
	        sbStyle.append("}\n");
		}
		return sbStyle.toString();
	}
	
//	 得到cell的html
	private String getCellHtml(int i, int j) {
		Cell c = cells[i][j];
		
		StringBuffer sb = new StringBuffer("<td ");
		
//		if (i<=miFixedRows){
			sb.append(" width=").append(getCellWidth(i, j));
//		}
		
		
		if (c.rowSpan > 1) {
			sb.append(" rowspan=").append(c.rowSpan);
		}

		if (c.colSpan > 1) {
			sb.append(" colspan=").append(c.colSpan);
		}
		
//		 垂直对齐
		switch (c.valign) {
		case 1:
			sb.append(" valign=\"top\"");
			break;
		case 2:
			sb.append(" valign=\"bottom\"");
		}

		// 水平对齐
		switch (c.align) {
		case -1:
			break;
		case 1:
			sb.append(" align=\"center\"");
			break;
		case 2:
			sb.append(" align=\"right\"");
		}
		
		if (useCss){
			if (!c.className.equals("")){
				sb.append(" class="+c.className );
			}
			/*else if (!cols[j].className.equals("")){
				sb.append(" class="+cols[j].className);
			}else if (!cellsClass.equals("")){
				sb.append(" class="+cellsClass );
			}*/
		}
		
		if (c.funEvent.equals("")){
			sb.append("").append(c.funEvent);
		}
		
		StringBuffer sbStyle =getCellStyle(i,j); 
		if (sbStyle.length()>0){
			sb.append(" style='").append(sbStyle).append("'");
		}
		sb.append(">");
		
		// 是用缺省tdClass
		if (c.value == null || c.value.trim().equals("")) {
			sb.append("&nbsp;");
		}else if (!ShowZero && (c.value.equals("0") || c.value.equals("0.0") ||c.value.equals(".0"))) {
			sb.append("&nbsp;");
		}else if (c.value.equals(Table.PAGENUMBER_NORMAL)) {
			sb.append("(第" + miCurrentPage+ "/" + miPages + "页)");
		}else if (c.value.equals(Table.PAGENUMBER_CHINA)) {
			sb.append("(共" + miPages + "页第" + miCurrentPage + "页)");
		} else {
			if (i > miFixedRows && !cols[j].format.equals("")) {
				sb.append(format(c.value, cols[j].format));
			} else {
				sb.append(c.value);
			}
		}
		sb.append("</td>\n");
		return sb.toString();
	}
	
	// 得到cell的html
	private String getCellHtmlOld(int i, int j) {
		Cell c = cells[i][j];
		StringBuffer sb = new StringBuffer("<td id=TdName ");

		sb.append(" width=").append(getCellWidth(i, j));

		if (c.rowSpan > 1) {
			sb.append(" rowspan=").append(c.rowSpan);
		}

		if (c.colSpan > 1) {
			sb.append(" colspan=").append(c.colSpan);
		}

		boolean blnUseDefaultCss = false;
		// 用缺省的css 减少返回的String的长度
//
//		if ((c.valign == -1 && c.align == -1 && !c.fontBold && c.fontSize == -1 || c.fontSize == fontSize)
//				&& (c.fontName.equals("") || c.fontName.equals(fontName))) {
//			if (c.border_left == 0 && c.border_top == 0 && c.border_right == 1
//					&& c.border_bottom == 1) {
//				switch (c.align) {
//				case -1:
//					sb.append(" class=tdLeft>");
//					break;
//				case 1:
//					sb.append(" align='center' class=tdNormal>");
//					break;
//				case 2:
//					sb.append(" align='right'  class=tdRight>");
//					break;
//				}
//				blnUseDefaultCss = true;
//			}
//			if (c.border_left == 0 && c.border_top == 0 && c.border_right == 0
//					&& c.border_bottom == 0) {
//				switch (c.align) {
//				case -1:
//					sb.append(" class=tdNoneLeft>");
//					break;
//				case 1:
//					sb.append(" align='center' class=tdNone>");
//					break;
//				case 2:
//					sb.append(" align='right'  class=tdNoneRight>");
//					break;
//				}
//				blnUseDefaultCss = true;
//			}
//		}

		// 垂直对齐
		switch (c.valign) {
		case 1:
			sb.append(" valign=top ");
			break;
		case 2:
			sb.append(" valign=bottom ");
			break;
		}

		// 是否使用了缺省的csss
		if (!blnUseDefaultCss) {
			// 水平对齐
			switch (c.align) {
			case -1:
				sb.append(" style=\" padding-left:2;");
				break;
			case 1:
				sb.append(" align='center' style=\"");
				break;
			case 2:
				sb.append(" align='right'");
				sb.append(" style=\"  padding-right:2;");
				break;
			}

			// 垂直对齐
			switch (c.valign) {
			case 1:
				sb.append("padding-top:2;");
				break;
			case 2:
				sb.append("padding-bottom:2;");
				break;
			}

			if (!c.fontName.equals("")) {
				if (!c.fontName.equals(fontName)) {
					sb.append("font-family:").append(c.fontName).append(";");
				}
			}

			if (c.fontSize != -1) {
				if (c.fontSize != fontSize) {
					sb.append("font-size:").append(c.fontSize).append("pt;");
				}
			}

			if (c.fontBold) {
				sb.append("font-weight: bold;");
			}

			if (!c.foreColor.equals("")) {
				sb.append("color:").append(c.foreColor).append(";");
			}

			if (!c.backColor.equals("")) {
				sb.append("background-color:").append(c.backColor).append(";");
			}else{
				if ("".equals(cols[j].bgColor)){
					sb.append("background-color:").append(cols[j].bgColor).append(";");
				}
			}
			sb.append(" border-left:").append(c.border_left).append("px")
					.append(" solid rgb(0,0,0);");
			sb.append(" border-top:").append(c.border_top).append("px").append(
					" solid rgb(0,0,0);");
			sb.append(" border-right:").append(c.border_right).append("px")
					.append(" solid rgb(0,0,0);");
			sb.append(" border-bottom:").append(c.border_bottom).append("px")
					.append(" solid rgb(0,0,0);\">");
		}
		// 是用缺省tdClass

		if (c.value == null || c.value.trim().equals("")) {
			sb.append("&nbsp;");
		} else if (!ShowZero && (c.value.equals("0") || c.value.equals("0.0") ||c.value.equals(".0"))) {
			sb.append("&nbsp;");
		} else if (c.value.equals(Table.PAGENUMBER_NORMAL)) {
			sb.append("(第" + miCurrentPage+ "/" + miPages + "页)");
		} else if (c.value.equals(Table.PAGENUMBER_CHINA)) {
			sb.append("(共" + miPages + "页第" + miCurrentPage + "页)");
		} else {
			if (i > miFixedRows && !cols[j].format.equals("")) {
				sb.append(format(c.value, cols[j].format));
			} else {
				sb.append(c.value);
			}
		}
		sb.append("</td>\n");

		return sb.toString();
	}

	
	public String format(String strValue, String strFormat) {
		DecimalFormat df = new DecimalFormat(strFormat);
		if (isDoubleValue(strValue)) {
			return df.format(getDoubleValue(strValue));
		} else {
			return strValue;
		}
	}

	public double Round_New(double Num ,int scale){
//		double floorNum = Math.floor(Num * Math.pow(10,scale ));
//		double floorNum1 = Math.floor(Num * Math.pow(10,scale -1))*10;
//		double floorNum2 = Math.floor(Num * Math.pow(10,scale +1));
//		double BitNum = floorNum - floorNum1;
//		double scaleNum = floorNum2 - floorNum*10;
////		alert(scaleNum);
//		if(scaleNum == 5){
//			if(BitNum == 0 || BitNum ==2 || BitNum ==4 || BitNum ==6 || BitNum ==8){
//				return Math.floor(Num * Math.pow(10,scale))/Math.pow(10,scale);
//			}else{
//				return (Math.floor(Num * Math.pow(10,scale))+1)/Math.pow(10,scale);
//			}
//		}else{
//			return Math.round(Num * Math.pow(10,scale))/Math.pow(10,scale);
//		}
		
		
		double value1=0;//拟舍弃数字的第一位等于5，且5前面的数字
    	
    	value1=Math.floor(Num*Math.pow(10,scale))-Math.floor(Num*Math.pow(10,scale-1))*10;
    	
    	double dbla=0;
		double dblb=0;
		dbla=(double)Math.round(Num * Math.pow(10, scale)*10000000)/10000000; 

		if ((dbla- Math.floor(Num * Math.pow(10, scale))) >= 0.5
				&& (dbla - Math.floor(Num * Math.pow(10, scale))) < 0.6) {
			if ((dbla- Math.floor(Num * Math.pow(10, scale))) == 0.5) {
    			if(value1==0 || value1==2 || value1==4 || value1==6 || value1==8 ){
    				return Math.floor(Num*Math.pow(10,scale))/Math.pow(10,scale);
    			}else{
    				return (Math.floor(Num*Math.pow(10,scale))+1)/Math.pow(10,scale);
    			}
    		}else{
    			return Math.round(Num * Math.pow (10,scale))/ Math.pow(10,scale);
    		}
    	}else{
    		return Math.round(Num * Math.pow (10,scale))/ Math.pow(10,scale);
    	}
    	
	}
	
	public double getCellDoubleValue(int i,int j){
		return getDoubleValue(cells[i][j].value);
	}
	private boolean isDoubleValue(String strValue) {
		try {
			if (strValue.equals("")) {
				return false;
			} else {
				Double.valueOf(strValue).doubleValue();
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private double getDoubleValue(String strValue) {
		try {
			return Double.valueOf(strValue).doubleValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public int getPageRows() {
		return miPageRows;
	}

	//设置分页的行数
	public void setPageRows(int miPageRows) {
		getPages();
		this.miPageRows = miPageRows;
	}
	
	public void filterColValue(int iCol){
		if (iCol <= 0 || iCol > miCols) {
			System.out.println("setCells:越界!iCol" + iCol + "不再范围1-" + miCols
					+ "内!");
			iCol = 1;
		}
		for (int i = this.miFixedRows + 1; i < this.miRows; i++) {
			if (cells[i][iCol].used) {
				for (int k = i; k < this.miRows; k++) {
					if (cells[i][iCol].value.equals(cells[k + 1][iCol].value)
							&& cells[i][iCol].colSpan == cells[k + 1][iCol].colSpan
							&& !isPageStartRow(k + 1)) {
						//cells[i][iCol].rowSpan = cells[i][iCol].rowSpan + 1;
						cells[k + 1][iCol].value = "";
					} else {
						i = k;
						break;
					}
				}
			}
		}
	}
}