package com.zhiren.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultSetList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3821486959746360373L;
	
	private int Pages = 1;

	private int CurrentRow = -1;

	private List ResultSetlist = null;

	private String[] ColumnNames = null;

	private String[] ColumnTypes = null;
	
	private boolean[] Nullables = null;
	
	private long[] ColPrecisions = null; 
	
	private long[] ColScales = null;
	
	public int getPages() {
		return Pages;
	}
	
	public void setPages(int ipage) {
		Pages = ipage;
	}

	public ResultSetList() {
		ResultSetlist = new ArrayList();
	}

	public int getRows() {
		if (ResultSetlist == null) {
			return 0;
		}
		return ResultSetlist.size();
	}

	public int getColumnCount() {
		if (ColumnNames == null) {
			return 0;
		}
		return ColumnNames.length;
	}

	public List getResultSetlist() {
		return ResultSetlist;
	}

	public void setResultSetlist(List resultSetlist) {
		ResultSetlist = resultSetlist;
	}

	public String[] getColumnNames() {
		return ColumnNames;
	}

	public void setColumnNames(String[] columnNames) {
		ColumnNames = columnNames;
	}

	public String[] getColumnTypes() {
		return ColumnTypes;
	}

	public void setColumnTypes(String[] columnTypes) {
		ColumnTypes = columnTypes;
	}
	
	public long[] getColPrecisions() {
		return ColPrecisions;
	}

	public void setColPrecisions(long[] colPrecisions) {
		this.ColPrecisions = colPrecisions;
	}
	
	public long[] getColScales() {
		return ColScales;
	}

	public void setColScales(long[] colScales) {
		this.ColScales = colScales;
	}
	
	public boolean[] getNullables() {
		return Nullables;
	}

	public void setNullables(boolean[] nullables) {
		Nullables = nullables;
	}
	
	public boolean isNullable(int colNum) {
		if (ResultSetlist == null) {
			return true;
		}
		if (colNum > getColumnCount()) {
			return true;
		}
		return Nullables[colNum];
	}
	
	public String getString(int rowNum, int colNum) {
		if (ResultSetlist == null) {
			return null;
		}
		if(rowNum <0 || rowNum >= ResultSetlist.size() || colNum < 0 || colNum >getColumnCount()) {
			return null;
		}
		String[] strarrrs = (String[]) ResultSetlist.get(rowNum);
		return strarrrs[colNum];
	}

	public String getString(int row, String colName) {
		return getString(row,findCol(colName.toUpperCase()));
	}

	public String getString(String colName) {
		return getString(CurrentRow,findCol(colName.toUpperCase()));
	}

	public String getString(int colNum) {
		return getString(CurrentRow,colNum);
	}
	
	public long getLong(int colNum) {
		String value = getString(colNum);
		long longv = 0;
		try {
			longv = Long.parseLong(value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return longv;
	}
	
	public int getInt(int colNum) {
		String value = getString(colNum);
		int intv = 0;
		try {
			intv = Integer.parseInt(value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return intv;
	}
	
	public double getDouble(int colNum) {
		String value = getString(colNum);
		double doublev = 0.0;
		try {
			doublev = Double.parseDouble(value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return doublev;
	}
	
	public Date getDate(int colNum) {
		Date date = new Date(getLong(colNum));
		return date;
	}
	
	public String getDateString(int colNum) {
		Date date = new Date(getLong(colNum));
		return DateUtil.FormatDate(date);
	}
	
	public String getDateTimeString(int colNum) {
		Date date = new Date(getLong(colNum));
		return DateUtil.FormatDateTime(date);
	}
	
	public long getLong(String colNum) {
		String value = getString(colNum);
		long longv = 0;
		try {
			if(value == null || "".equals(value)){
				return longv;
			}
			longv = Long.parseLong(value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return longv;
	}
	
	public int getInt(String colNum) {
		String value = getString(colNum);
		int intv = 0;
		try {
			if(value == null || "".equals(value)){
				return intv;
			}
			intv = Integer.parseInt(value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return intv;
	}
	
	public double getDouble(String colNum) {
		String value = getString(colNum);
		double doublev = 0.0;
		try {
			if(value == null || "".equals(value)){
				return doublev;
			}
			doublev = Double.parseDouble(value);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return doublev;
	}
	
	public Date getDate(String colNum) {
		Date date = new Date(getLong(colNum));
		return date;
	}
	
	public String getDateString(String colNum) {
		Date date = new Date(getLong(colNum));
		return DateUtil.FormatDate(date);
	}
	
	public String getDateTimeString(String colNum) {
		Date date = new Date(getLong(colNum));
		return DateUtil.FormatDateTime(date);
	}
	
	public String[] getRowString() {
		return (String[]) ResultSetlist.get(CurrentRow);
	}
	
	public void setString(int colNum,String value) {
		if (ResultSetlist == null) {
			return;
		}
		if (colNum > getColumnCount()) {
			return;
		}
		String[] strarrrs = (String[]) ResultSetlist.get(CurrentRow);
		strarrrs[colNum] = value;
	}
	
	public void setString(String colName,String value) {
		if (ResultSetlist == null) {
			return;
		}
		String[] strarrrs = (String[]) ResultSetlist.get(CurrentRow);
		strarrrs[findCol(colName.toUpperCase())] = value;
	}
	
	public void setString(int rowNum,int colNum, String value) {
		if (ResultSetlist == null) {
			return ;
		}
		String[] strarrrs = (String[]) ResultSetlist.get(rowNum);
		strarrrs[colNum] = value;
	}
	
	public void setString(int rowNum,String colName, String value) {
		if (ResultSetlist == null) {
			return ;
		}
		String[] strarrrs = (String[]) ResultSetlist.get(rowNum);
		strarrrs[findCol(colName.toUpperCase())] = value;
	}
	
	public String[] getArrString(int currrow,String[] arrcolname){
		if(arrcolname==null || arrcolname.length==0){
			return null;
		}
		if (ResultSetlist == null) {
			return null;
		}
		if(currrow<0 || currrow> getRows()){
			return null;
		}
		String[] arrcol = new String[arrcolname.length];
		for(int i = 0 ; i < arrcolname.length ; i ++){
			for(int j=0 ; j<getColumnCount(); j++){
				if(arrcolname[i].equalsIgnoreCase(getColumnNames()[j])){
					String[] strarrrs = (String[]) ResultSetlist.get(currrow);
					arrcol[i] = strarrrs[j];
					break;
				}
			}
		}
		return arrcol;
	}
	public String[] getArrString(String[] arrcolname){
		return getArrString(CurrentRow,arrcolname);
	}

	public int findCol(String colName) {
		int i = 0;
		for (; i < getColumnCount(); i++) {
			if (getColumnNames()[i].equalsIgnoreCase(colName)) {
				return i;
			}
		}
		return -1;
	}

	public boolean next() {
		if (getRows() == 0) {
			return false;
		}
		return ++CurrentRow < getRows();
	}

	public void beforefirst() {
		CurrentRow = -1;
	}
	
	public int getRow() {
		return CurrentRow;
	}
	
	public void Remove(int intRow) {
		if (ResultSetlist == null) {
			return ;
		}
		if(intRow >= getRows()) {
			return;
		}
		ResultSetlist.remove(intRow);
		if(getRow()>-1) {
			CurrentRow--;
		}
	}
	
	public void close(){
		ResultSetlist = null;
		ColumnNames = null;
		ColumnTypes = null;
		Nullables = null;
		ColPrecisions = null; 
		ColScales = null;
	}
}
