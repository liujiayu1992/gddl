package com.zhiren.zidy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IPage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.report.Cell;
import com.zhiren.report.Chart;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Aotcr implements Serializable{

/*
 * 作者：夏峥
 * 时间：2011-06-14
 * 描述：新增描述
 * 		 新增构建方法，传入自定义数据标识和经济分析综合报告ID
 * 		 新增方法通过自定义方案ID和经济分析综合报告ID得到其对应的标题
 *		 新增标题行
 * 		除注释外，所有新增方法暂停使用。。。。
 */
	
	private static final long serialVersionUID = -1749845565126865373L;

// Aotcr -> Analysis of the custom reports
	private String DesignID;
	
	private Report report;
	
	private IPage ipage;
	
	private List Css;
	private List Parameters;
	private List DataSources;
	
//	private String JJFXZHBG_ID="";
	
	private int[] colsWidth = null;
	
	public JDBCcon con = null;
	
	public Aotcr(String zidyid) {
		DesignID = zidyid;
		init();
	}
	
////	新增构建方法，传入自定义数据标识和经济分析综合报告ID
//	public Aotcr(String zidyid,String JJFXZHBG_ID) {
//		this.DesignID = zidyid;
//		this.JJFXZHBG_ID=JJFXZHBG_ID;
//		init();
//		report.setCenter(true);
//	}
	
	public void setIPage(IPage page) {
		ipage = page;
	}
	
	public void init() {
		con = new JDBCcon();
		report = new Report();
		String sql = "select * from zidyfa where id =" + DesignID;
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()) {
			report.setOrientation(rs.getInt("z_Orientation"));
			report.setPaper(rs.getInt("z_pagesize"));
			report.setPageRows(rs.getInt("z_PagesRows"));
		}
		rs.close();
	}
	public void setData() {
		FillDataSources();
		setColsWidth();
		report.setTitle(getTable(ZidyOperation.group_strHeader));
		if(report.title != null) {
			report.title.setBorderNone();
			if(report.title.getRows()>3){
				report.title.setCellAlign(2,1,Table.ALIGN_CENTER);
			}
//			report.title.setUseDefaultCss(true);
		}
		//group_strtHeader
		report.setBody(getTable(ZidyOperation.group_strtBodyer));
		if(report.body != null) {
			report.body.InsertFixedRows(getTable(ZidyOperation.group_strtHeader));
			setFixedRow();
			MergerCol();
			report.body.setUseDefaultCss(true);
		}
		report.setFooter(getTable(ZidyOperation.group_strFooter));
		if(report.footer != null) {
			report.footer.setBorderNone();
		}
	}
	public void setFixedRow() {
		int rows = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) fixedRow from zidyfah where z_group = '表头' and zidyfa_id =").append(DesignID);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			rows = rsl.getInt("fixedRow");
		}
		rsl.close();
		report.body.setFixedRows(rows);
	}
	public void MergerCol() {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from zidyfal where z_ismerger = 1 and zidyfa_id =").append(DesignID).append(" order by z_col");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			report.body.mergeCol(rsl.getInt("z_col"),true);
		}
		rsl.close();
	}
//	初始化参数数组
	public void initParaMeters() {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id, c.z_paramname, c.z_datatypes, c.z_prompttext, c.z_controltype,\n")
		.append("c.z_controlwidth,c.z_labelfield, c.z_valuefield, c.z_defaultvalue, s.z_query\n")
		.append("                from zidyfacsb c,zidysjy s \n")
		.append("where c.zidysjy_id = s.id(+) and c.zidyfa_id = ").append(DesignID)
		.append("\n order by c.id");
//		String sql = "select * from zidyfacsb where zidyfa_id = " + DesignID ;
		ZidyParam zid = new ZidyParam();
		zid.setId(null);
		zid.setName("方案ID");
		zid.setSvalue(DesignID);
		getParaMeters().add(zid);
		ZidyParam zoth = new ZidyParam();
		zoth.setId(null);
		zoth.setName("其它条件");
		zoth.setSvalue(getOtherParam());
		getParaMeters().add(zoth);
		ResultSetList rs = con.getResultSetList(sb.toString());
		while(rs.next()) {
			ZidyParam p = 
				new ZidyParam(rs.getString("id"),
					rs.getString("z_paramname"),
					rs.getString("z_controltype"),
					rs.getString("z_controlwidth"),
					rs.getString("z_defaultvalue"),
					rs.getString("z_labelfield"),
					rs.getString("z_valuefield"),
					rs.getString("z_query"),
					ZidyOperation.getDefaultValueField(rs.getString("z_defaultvalue"),rs.getString("z_datatypes")),
					rs.getString("z_prompttext"),
					ZidyOperation.getDefaultValueSql(rs.getString("z_defaultvalue"),rs.getString("z_datatypes")),
					rs.getString("z_datatypes"));
			getParaMeters().add(p);
		}
		rs.close();
	}
	
	public String getOtherParam() {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.z_group, z.z_alias, c.z_operators, c.z_value, z.z_type\n")
		.append("from zidyfacsz c,zidycszd z\n")
		.append("where c.zidycszd_id = z.id and z.zidyfa_id = ")
		.append(DesignID)
		.append("\n order by c.z_group");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		String group = "";
		StringBuffer param = new StringBuffer();
		while(rsl.next()) {
			if(rsl.getRow()==0) {
				group = rsl.getString("z_group");
				param.append("(");
			}else
				if(!group.equals(rsl.getString("z_group"))) {
					param.append(") or (");
				}else
					param.append(" and ");
			
			param.append(rsl.getString("z_alias"))
			.append(rsl.getString("z_operators"));
			
			if("in".equals(rsl.getString("z_operators"))) {
				param.append("(").append(rsl.getString("z_value")).append(")");
			}else
				if("like".equals(rsl.getString("z_operators"))) {
					param.append("'%").append(rsl.getString("z_value")).append("%'");
				}else
					param.append(ZidyOperation.
						getTypeValue(rsl.getString("z_type"),rsl.getString("z_value")));
		}
		rsl.close();
		if(param.length()>0){
			param.append(")");
			param.insert(0, " and ");
		}
		return param.toString(); 
	}
	
	public List getParaMeters() {
		if(Parameters == null) {
			Parameters = new ArrayList();
			initParaMeters();
		}
		return Parameters;
	}
	
	public List getDataSources() {
		if(DataSources == null) {
			DataSources = new ArrayList();
		}
		return DataSources;
	}
	
	public void initCss(){
		StringBuffer sb = new StringBuffer();
		sb.append("select * from zidyfaysb where zidyfa_id = ").append(DesignID);
		ResultSetList rs = con.getResultSetList(sb.toString());
		while(rs.next()) {
			ZidyCss z = new ZidyCss(
						rs.getString("id"),
						rs.getString("cssname"),
						rs.getString("css"));
			Css.add(z);
		}
		rs.close();
	}
	
	public List getCss() {
		if(Css == null) {
			Css = new ArrayList();
			initCss();
		}
		return Css;
	}
	
//	填充数据源
	public void FillDataSources() {
		DataSources = new ArrayList();
		String sql = "select s.* from zidyfasjy fs,zidysjy s where fs.zidysjy_id = s.id and fs.zidyfa_id = " + DesignID + " order by fs.zidysjy_id";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()) {
			sql = "select * from zidysjysql where zidysjy_id = "+ rs.getString("id") + " order by xuh";
			ResultSetList rsl = con.getResultSetList(sql);
			sql = "";
			while(rsl.next()){
				sql += rsl.getString("z_Query");
			}
			sql = ZidyOperation.getDataSourceSql(getParaMeters(),sql);
			DataSources.add(new ZidyDataSource(
					rs.getString("id"),con.getResultSetList(sql)));
		}
		rs.close();
	}

	public void setColsWidth() {
		String sql = "select z_width from zidyfal where zidyfa_id=" + DesignID
			+ " order by z_col";
		ResultSetList rs = con.getResultSetList(sql);
		colsWidth = new int[rs.getRows()];
		while(rs.next()) {
			colsWidth[rs.getRow()] = rs.getInt("z_width");
		}
		rs.close();
	}
	
	public int getMaxNum(int type,String group) {
		int max = 0;
		String tableName = "zidyfah";
		String sqlGroup = " and z.z_group='" + group + "'";
		int direction  = 1;
		String spanName = "z_rowspan";
		String order = "z_row";
		if(ZidyOperation.direction_Col == type) {
			tableName = "zidyfal";
			sqlGroup = "";
			direction = 2;
			spanName = "z_colspan";
			order = "z_col";
		}
		String sql = "select * from " + tableName + " z where z.zidyfa_id=" + DesignID + sqlGroup +" order by " + order;
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()) {
			int maxNum = 0;
			sql = "select * from zidyfanr n where n.zidyfa_id=" + DesignID 
			+ " and n."+tableName+"_id =" + rs.getString("id");
			ResultSetList rsh = con.getResultSetList(sql);
			while(rsh.next()) {
				if(rsh.getInt("zidysjy_id") != 0 && rsh.getInt("z_direction") == direction && !"图".equals(rsh.getString("z_alias"))) {
					for(int i = 0; i < DataSources.size() ; i++) {
						ZidyDataSource zds = (ZidyDataSource)DataSources.get(i);
						if((zds.getDataSourceID()).equals(rsh.getString("zidysjy_id"))) {
							maxNum = Math.max(maxNum, zds.getDataSource().getRows());
							break;
						}
					}
				}else {
					maxNum = Math.max(maxNum, rsh.getInt(spanName));
				}
			}
			if(rs.getInt("z_row") + maxNum -1 > max) {
				max = rs.getInt("z_row") + maxNum -1;
			}
		}
		rs.close();
		return max;
	}
	
	public int getMaxRows(String group) {
		return getMaxNum(ZidyOperation.direction_Row,group);
	}
	
	public int getMaxCols() {
		return getMaxNum(ZidyOperation.direction_Col,"");
	}
	
	public Table getTable(String group) {
		Table tb = null;
		Cell c = new Cell();
//		如果是页眉或页脚，则无边框
		if(ZidyOperation.group_strHeader.equals(group)
				|| ZidyOperation.group_strFooter.equals(group)) {
//			设置边框为无边框
			c.setBorderNone();
		}
		int iRows = getMaxRows(group);
//		if(iRows == 0) {
//			return null;
//		}
//		if(ZidyOperation.group_strHeader.equals(group)){
//			iRows=iRows+1;
//		}
		tb = new Table(iRows,colsWidth.length,c);
		tb.setWidth(colsWidth);
		
		int curRow = 0;			//记录当前table行
		long curRowID = 0;		//当前行ID
		int dataRow = 0;		//数据行数
		String sql = "select n.zidysjy_id, n.zidyfah_id, n.z_alias, n.z_rowspan,\n" 
			+ "n.z_colspan, n.z_direction, n.z_style cellcn, n.z_font_name, \n"
			+ "n.z_font_size, n.z_border, n.z_align, h.z_group, h.z_row, h.z_height, h.z_style rowcn,\n"
			+ "l.z_col, l.z_width, l.z_style colcn\n"
			+ " from zidyfanr n,zidyfah h,zidyfal l\n"
			+ "where n.zidyfah_id = h.id and n.zidyfal_id = l.id\n"
			+ "and n.zidyfa_id = "+ DesignID +" and h.z_group = '" + group +"'\n order by h.z_row,l.z_col";
		ResultSetList rs = con.getResultSetList(sql);
		
////		如果是标题且结果集为空，则手动添加一条标题行
//		if(ZidyOperation.group_strHeader.equals(group)&& rs.getRows()<1){
////				重写表构建方法
//				tb = new Table(1,1,c);
//				colsWidth = new int[1];
//				colsWidth[0]=600;
//				tb.setWidth(colsWidth);
////				将标题放入表内
//				tb.setRowHeight(1, 14); 
//				tb.setRowClassName(1, "className");
//				Cell cell1 = tb.getCell(1, 1);
//				cell1.className = "";
//				cell1.fontName = "宋体";
//				cell1.fontSize = 16;
//				cell1.value = getBiaot();
//				cell1.align = Table.ALIGN_LEFT;
////				tb.mergeCell(1, 1, 1, 1);
//		}
		
		while(rs.next()) {
			int iRow = rs.getInt("z_row");
			int iCol = rs.getInt("z_col");
			int iRowspan = rs.getInt("z_rowspan");
			int iColspan = rs.getInt("z_colspan");
//			初始化数据行
			
			if(rs.getRow() == 0) {
				curRow = iRow;
				curRowID = rs.getLong("zidyfah_id");
				dataRow = 1;
			}
			if(curRowID != rs.getLong("zidyfah_id")) {
				curRow = curRow + dataRow;
				dataRow = 1;
				curRowID = rs.getLong("zidyfah_id");
			}
			if(iRow == 0) {
				iRow = curRow;
			}
			
////			设置标题内容
//			if(ZidyOperation.group_strHeader.equals(group)){
////				通过方法得到自定义的定义标题
//				String biaot=getBiaot();
////				将标题放入表内
//				tb.setRowHeight(1, rs.getInt("z_height")); 
//				tb.setRowClassName(1, rs.getString("rowcn"));
//				Cell cell1 = tb.getCell(1, iCol);
//				cell1.className = rs.getString("cellcn");
//				cell1.fontName = rs.getString("z_font_name");
//				cell1.fontSize = rs.getInt("z_font_size");
//				cell1.value = biaot;
//				cell1.align = Table.ALIGN_LEFT;
//				tb.mergeCell(iRow, iCol, 1, iCol + iColspan -1);
////				标题添加完成后，默认的行数加一
//				iRow=iRow+1;
//			}
			
			String zidysjy_id = rs.getString("zidysjy_id");
			if("0".equals(zidysjy_id)) {
				tb.setRowHeight(iRow, rs.getInt("z_height")); 
				tb.setRowClassName(iRow, rs.getString("rowcn"));
				Cell cell = tb.getCell(iRow, iCol);
				cell.className = rs.getString("cellcn");
				cell.fontName = rs.getString("z_font_name");
				cell.fontSize = rs.getInt("z_font_size");
				cell.value = rs.getString("z_alias");
				cell.align = rs.getInt("z_align");
				tb.mergeCell(iRow, iCol, iRow + iRowspan -1, iCol + iColspan -1);
			}else {
				if("图".equals(rs.getString("z_alias")) ){
					Chart ct = new Chart(ipage);
					String ctsql = "select * from zidyfatb \n"
						+ "where zidyfa_id = " + DesignID + " \n" 
						+ "and zidysjy_id = " + zidysjy_id;
					ResultSetList ctrs = con.getResultSetList(ctsql);
					while(ctrs.next()){
						String name = ctrs.getString("z_name");
						String v = ctrs.getString("z_value");
						ct.setAttribute(name, v);
					}
					for(int i = 0; i < getDataSources().size() ; i++) {
						ZidyDataSource zds = (ZidyDataSource)getDataSources().get(i);
						if(zds.getDataSourceID().equals(zidysjy_id)) {
							ct.setRsl(zds.getDataSource());
							break;
						}
					}
					String value = ct.getStringChart(ZidyOperation.getChartTypeInt(rs.getString("cellcn")));
					tb.setRowHeight(iRow, rs.getInt("z_height")); 
					tb.setRowClassName(iRow, rs.getString("rowcn"));
					Cell cell = tb.getCell(iRow, iCol);
					cell.value = value;
					tb.mergeCell(iRow, iCol, iRow + iRowspan -1, iCol + iColspan -1);
				}else
					for(int i = 0; i < DataSources.size() ; i++) {
						ZidyDataSource zds = (ZidyDataSource)DataSources.get(i);
						if(zds.getDataSourceID().equals(zidysjy_id)) {
							zds.getDataSource().beforefirst();
							while(zds.getDataSource().next()) {
								int rowtmp = iRow + zds.getDataSource().getRow();
								tb.setRowHeight(rowtmp, rs.getInt("z_height")); 
								tb.setRowClassName(rowtmp, rs.getString("rowcn"));
								Cell cell = tb.getCell(rowtmp, iCol);
								cell.className = rs.getString("cellcn");
								cell.fontName = rs.getString("z_font_name");
								cell.fontSize = rs.getInt("z_font_size");
								cell.value = zds.getDataSource().getString(rs.getString("z_alias"));
								cell.align = rs.getInt("z_align");
								tb.mergeCell(rowtmp , iCol, rowtmp + iRowspan -1, iCol + iColspan -1);
							}
							dataRow = zds.getDataSource().getRows();
							break;
						}
					}
			}
			if(dataRow < iRowspan) {
				dataRow = iRowspan;
			}
		}
		rs.close();
//		con.Close();
		return tb;
	}
	
	public String getHtml() {
		return report.getHtml();
	}
////	通过自定义方案ID和经济分析综合报告ID得到其对应的标题
//	private String getBiaot(){
//		String bt_sql=
//			"SELECT Z.ZIDYFA_ID, S.BIAOT JJFXDXMK_ID\n" +
//			"  FROM JJFXZHBGSZB S, JJFXDXFXSZB Z\n" + 
//			" WHERE S.JJFXDXMK_ID = Z.JJFXDXMK_ID\n" + 
//			"   AND Z.ZIDYFA_ID = "+DesignID+"\n" +
//			"	AND S.JJFXZHBG_ID ="+JJFXZHBG_ID;
//		ResultSetList bt_rs = con.getResultSetList(bt_sql);
//	//	定义标题
//		String biaot="";
//		while(bt_rs.next()){
//			biaot=bt_rs.getString("JJFXDXMK_ID");
//		}
//		bt_rs.close();
//		return biaot;
//	}
}
