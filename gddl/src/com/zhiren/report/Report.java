package com.zhiren.report;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：新增关于报表的A4纸的格式方法，
 * 		 根据具体的要求设置不同的打印纸张类型，
 *       默认把报表的格式设定为A4打印形式。
 */
/*
 * 作者：李琛基
 * 时间：2010-07-28 
 * 描述：新增getPageHtml_GD与getAllPagesHtml_GD方法，
 * 为了使打印多张报表时，
 * 能够实现一个报表占用一页纸的情况（每个报表都是单独一个方法）。
 *       
 */
public class Report {
	int miPages = 1;

	int miPagesRows = 0;

	private String mstrTitle = "";

	public Table title;

	public Table body;

	public Table footer;

	// 默认值的Cell
	public int DefaultColWidth = 80;

	public int DefaultRowHeight = 24;

	public static final int PAPER_A4 = 4;

	public static final int PAPER_B5 = 5;

	public static final int PAPER_B4 = 7;

	public static final int PAPER_A3 = 3;

	public static final int PAPER_SUser = 2;

	public static final int PAPER_B5_HEIGHT = 970;

	public static final int PAPER_B5_WIDTH = 680;

	public static final int PAPER_A4_HEIGHT = 1120;

	public static final int PAPER_A4_WIDTH = 790;

	public static final int PAPER_B4_HEIGHT = 1335;

	public static final int PAPER_B4_WIDTH = 940;

	public static final int PAPER_A3_HEIGHT = 1580;

	public static final int PAPER_A3_WIDTH = 1120;

	public static final int PAPER_Portrait = 1;// 纵向

	public static final int PAPER_Landscape = 2;// 横向

	public static final int PAGE_HEIGHT = 0;// 按高度换页

	public static final int PAGE_ROWS = 1;// 按行换页

	private int miMarginLeft = 10;

	private int miMarginTop = 10;

	private int miMarginRight = 10;

	private int miMarginBottom = 10;

	private int miPaper = PAPER_A4;

	private int miOrientation = PAPER_Portrait;

	private int miPaperHeight = PAPER_A4_HEIGHT;

	private int miPagerWidth = PAPER_A4_WIDTH;

	public static final int ROW_PAGE_SPLIT = 1;

	public static final int HEIGHT_PAGE_SPLIT = 1;

	private int miSplitPageStyle = PAGE_ROWS;

	private boolean mblnCenter = true;

	public static final int PAPER_ROWS = 38;//设置页面行数
	
	public static final int PAPER_COLROWS = 22;//设置页面横向行数

	public void setCenter(boolean blnCenter) {
		mblnCenter = blnCenter;
	}

	public void setBodyHeight() {
		if (body != null) {
			body.setSplitPageStyle(miSplitPageStyle);
			body.setPageMaxHeight(getBodyMaxHeight());
		}
	}

	public void setSplitPageStyle(int intSplitPageStyle) {
		miSplitPageStyle = intSplitPageStyle;
	}

	public int getSplitPageStyle() {
		return miSplitPageStyle;
	}

	public void setPaperSize(int iPaperHeight, int iPagerWidth) {
		miPaperHeight = iPaperHeight;
		miPagerWidth = iPagerWidth;
	}

	// 定义纸张
	public int getPaper() {
		return miPaper;
	}

	public void setPaper(int iPaper) {
		miPaper = iPaper;
	}

	// 设置纸张方向
	public void setOrientation(int iOrientation) {
		miOrientation = iOrientation;
	}

	public int getPages() {
		body.setSplitPageStyle(miSplitPageStyle);
		body.setPageMaxHeight(getBodyMaxHeight());
		return body.getPages();
	}

	public int getOrientation() {
		return miOrientation;
	}

	private int getBodyMaxHeight() {
		int iHeight = getPaperHeight();

		if (title != null) {
			iHeight = iHeight - title.getHeight();
		}

		if (footer != null) {
			iHeight = iHeight - footer.getHeight();
		}

		return iHeight - miMarginTop - miMarginBottom;
	}

	private int getPaperHeight() {
		int iHeight = PAPER_A4_HEIGHT;
		int iWidth = PAPER_A4_WIDTH;

		if (miPaper == PAPER_B5) {
			iHeight = PAPER_B5_HEIGHT;
			iWidth = PAPER_B5_WIDTH;

		} else if (miPaper == PAPER_A4) {
			iHeight = PAPER_A4_HEIGHT;
			iWidth = PAPER_A4_WIDTH;

		} else if (miPaper == PAPER_B4) {
			iHeight = PAPER_B4_HEIGHT;
			iWidth = PAPER_B4_WIDTH;
		} else if (miPaper == PAPER_A3) {
			iHeight = PAPER_A3_HEIGHT;
			iWidth = PAPER_A3_WIDTH;
		}

		if (miOrientation == PAPER_Landscape) {
			return iWidth;
		} else {
			return iHeight;
		}
	}

	// 设定页边距
	public void setMargin(int iMarginLeft, int iMarginTop, int iMarginRight,
			int iMarginBottom) {
		miMarginLeft = iMarginLeft;
		miMarginTop = iMarginTop;
		miMarginRight = iMarginRight;
		miMarginBottom = iMarginBottom;
	}

	public void setMarginLeft(int iMarginLeft) {
		miMarginLeft = iMarginLeft;
	}

	public int getMarginLeft() {
		return miMarginLeft;
	}

	public int getMarginTop() {
		return miMarginTop;
	}

	public void setMarginTop(int iMarginTop) {
		miMarginTop = iMarginTop;
	}

	public int getMarginRight() {
		return miMarginRight;
	}

	public void setMarginRight(int iMarginRight) {
		miMarginRight = iMarginRight;
	}

	public int getMarginBottom() {
		return miMarginBottom;
	}

	public void setMarginBottom(int iMarginBottom) {
		miMarginBottom = iMarginBottom;
	}

	public Report() {

	}

	private String getTitleHtml() {
		return "";
	}

	private String mstrtitleHtml = "";

	public void setPageRows(int iPageRows) {
		miPagesRows = iPageRows;
	}

	public String getHtml(int iIndexStart) {
		StringBuffer sb = new StringBuffer();
		if (mblnCenter) {
			sb.append("<center>");
		}

		sb.append("<span id='reportpage").append(iIndexStart).append("'>\n");

		if (title != null) {
			sb.append(title.getHtml());
		}

		if (body != null) {
			sb.append(body.getHtml());
		}

		if (footer != null) {
			sb.append(footer.getHtml());
		}

		sb.append("</span>\n");
		if (mblnCenter) {
			sb.append("</center>");
		}
		return sb.toString();
	}

	public String getHtml() {
		StringBuffer sb = new StringBuffer();
		if (mblnCenter) {
			sb.append("<center>");
		}

		sb.append("<span id='reportpage").append(1).append("'>\n");

		if (title != null) {
			sb.append(title.getHtml());
		}

		if (body != null) {
			sb.append(body.getHtml());
		}

		if (footer != null) {
			sb.append(footer.getHtml());
		}

		sb.append("</span>\n");
		if (mblnCenter) {
			sb.append("</center>");
		}
		return sb.toString();
	}

	public String getAllPagesHtml(int iIndexStart) {
		StringBuffer sb = new StringBuffer();
		sb.append("<center>");
		if (body != null) {
			miPages = body.getPages();
			for (int i = 1; i <= miPages; i++) {
				sb.append(getPageHtml(i, miPages, iIndexStart));
			}
		}
		sb.append("</center>");
		return sb.toString();
	}

	public String getAllStyle() {
		return "<style>\n" + body.getAllStylesDefine() + "\n</style>\n";
	}

	public String getAllPapersHtml(int iIndexStart) {
		int i = 0;
		int k = 0;
		int iPages = 0;

		body.setSplitPageStyle(miSplitPageStyle);
		body.setPageMaxHeight(getBodyMaxHeight());

		StringBuffer sbTitle = new StringBuffer();
		StringBuffer sbFooter = new StringBuffer();

		// if (title!=null){
		// sbTitle.append(title.getHtml());
		// }
		//		
		// if (footer!=null){
		// sbFooter.append(footer.getHtml());
		// }

		StringBuffer sb = new StringBuffer();
		StringBuffer sbFixed = new StringBuffer();
		body.setPageMaxHeight(getBodyMaxHeight());
		body.splitPages();// 先计算分页

		for (i = 1; i <= body.getRows(); i++) {
			if (body.rows[i].newPage) {// 如果碰到换页，开始一页
				if (iPages > 0) {
					sb.append("</table>");// 结束上页表body
					sb.append(footer.getTitleHtml(iPages, body.getPages()));// 结束上页
					sb.append("</span>");// 结束上页容器
				}

				iPages = iPages + 1;

				body.setCurrentPage(iPages);// 设置当前页变量
				if (iPages == 1 && iIndexStart == 0) {
					sb.append("<span id='reportpage").append(
							iPages + iIndexStart).append("' >\n");
				} else {
					sb.append("<p style='page-break-after: always'></p>");
					sb.append("<span id='reportpage").append(
							iPages + iIndexStart).append(
							"' style='display:none' >\n");
				}

				if (title != null) {
					title.setCurrentPage(iPages);
					sb.append(title.getTitleHtml(iPages, body.getPages()));// 加表头
				}
				sb.append(body.getTableH("oData" + iPages));// 表定义

				// 得到FixedRow的html
				if (body.rows[i].fixed) {
					sbFixed.setLength(0);
					for (k = i; k <= body.getRows(); k++) {
						if (body.rows[k].fixed) {
							sbFixed.append(body.getRowHtml(k));
						} else {
							break;
						}
					}
					i = k;// 执行完固定行，跳到数据行
				}
				sb.append(sbFixed);
			}
			if (i <= body.getRows()) {
				sb.append(body.getRowHtml(i));
			}
		}
		System.out.print(body.getPages());
		if (iPages > 0) {
			sb.append("</table>");// 结束表body
			if (footer != null) {// 结束页
				sb.append(footer.getTitleHtml(iPages, body.getPages()));
			}
			sb.append("</span>");// 结束页容器
		}
		return sb.toString();
	}

	public String getAllPagesHtml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<center>");

		if (body != null) {
			miPages = body.getPages();
			for (int i = 1; i <= miPages; i++) {
				sb.append(getPageHtml(i, miPages, 0));
			}
		}
		sb.append("</center>");
		return sb.toString();
	}

	// 得到某页的page
//	public String getPageHtml(int iPage, int iAllPages, int iIndexStart) {
//		StringBuffer sb = new StringBuffer();
//
//		if (iPage == 1 && iIndexStart == 0) {
//			sb.append("<span id='reportpage").append(iPage + iIndexStart)
//					.append("' >\n");
//		} else {
//			sb.append("<p style='page-break-after: always'></p>");
//			sb.append("<span id='reportpage").append(iPage + iIndexStart)
//					.append("' style='display=none' >\n");
//		}
//
//		if (title != null) {
//			title.setCurrentPage(iPage);
//			sb.append(title.getTitleHtml(iPage, body.getPages()));
//		}
//
//		sb.append(body.getHtml(iPage));
//
//		if (footer != null) {
//			sb.append(footer.getTitleHtml(iPage, body.getPages()));
//		}
//		sb.append("</span>\n");
//		return sb.toString();
//	}
	
	public String getPageHtml(int iPage, int iAllPages, int iIndexStart) {
		StringBuffer sb = new StringBuffer();

		if (iPage != iAllPages) {
			sb.append("<p style='page-break-after: always'>");
		}
		
		if (iPage == 1 && iIndexStart ==0 ) {
			sb.append("<span id='reportpage").append(iPage + iIndexStart).append("' >\n");
		} else {
			//sb.append("<p style='page-break-after: always'></p>");
			sb.append("<span id='reportpage").append(iPage + iIndexStart).append("' style='display:none' >\n");
		}
		
		if (title != null) {
			title.setCurrentPage(iPage);
			sb.append(title.getTitleHtml(iPage,body.getPages()));
		}
	
		sb.append(body.getHtml(iPage));
		
		if (footer != null) {
			sb.append(footer.getTitleHtml(iPage,body.getPages()));
		}
		
		sb.append("</span>\n");
		if (iPage != iAllPages) {
//			sb.append("<p style='page-break-after: always'></p>");
					
			sb.append("</p>");
		}
		return sb.toString();
	}
	
	
	
	public String getAllPagesHtml_GD(int iIndexStart) {
		StringBuffer sb = new StringBuffer();
		sb.append("<center>");
		if (body != null) {
			miPages = body.getPages();
			for (int i = 1; i <= miPages; i++) {
				sb.append(getPageHtml_GD(i, miPages, iIndexStart));
			}
		}
		sb.append("</center>");
		return sb.toString();
	}
	
	public String getPageHtml_GD(int iPage, int iAllPages, int iIndexStart) {
		StringBuffer sb = new StringBuffer();

		if (iPage == 1 && iIndexStart ==0 ) {
			sb.append("<span id='reportpage").append(iPage + iIndexStart).append("' >\n");
		} else {
			//sb.append("<p style='page-break-after: always'></p>");
			sb.append("<span id='reportpage").append(iPage + iIndexStart).append("' style='display:none' >\n");
		}
		if (title != null) {
			title.setCurrentPage(iPage);
			sb.append(title.getTitleHtml(iPage,body.getPages()));
		}
		sb.append(body.getHtml(iPage));
		
		if (footer != null) {
			sb.append(footer.getTitleHtml(iPage,body.getPages()));
		}

		sb.append("<p style='page-break-after: always'></p>");
		sb.append("</span>\n");
		
		return sb.toString();
	}


	// 增加页头
	public void setTitle(Table hd) {
		title = hd;
	}
    public void setTitle(String strTitle, int ColWidth[],int excludeCols) {
        int colWight0=ColWidth.length-excludeCols;
        if(excludeCols>0){
            colWight0=ColWidth.length-excludeCols;
        }else{
            colWight0=ColWidth.length;
        }
        Cell c = new Cell();
        c.setBorderNone();
        title = new Table(3, colWight0,c);

        title.setWidth(ColWidth);
        title.setBorderNone();
        title.setCellValue(2, 1, strTitle);
        title.setCellAlign(2, 1, Table.ALIGN_CENTER);
        title.setCellFont(2, 1, "", 16, true);
        title.mergeRowCells(2);
    }
	// 在执行setTitle 方法后调用
	public void setDefaultTitleLeft(String strLeft, int iCols) {
		int rownum = title.getRows();
		title.setCellValue(rownum, 1, strLeft);

		if (iCols > title.getCols() || iCols <= 1) {
			return;
		}
		title.mergeCell(rownum, 1, rownum, iCols);
	}

	public void setDefaultTitle(int iCol, int iMergeCols, String strValue,
			int iAlign) {
		if (iCol > title.getCols() || iCol + iMergeCols - 1 > title.getCols()
				|| iMergeCols <= 1) {
			return;
		}

		int rownum = title.getRows();
		title.setCellValue(rownum, iCol, strValue);
		title.setCellAlign(rownum, iCol, iAlign);
		title.mergeCell(rownum, iCol, rownum, iCol + iMergeCols - 1);

	}

	// 在执行setTitle 方法后调用
	public void setDefaultTitleRight(String strRight, int iCols) {
		int iCol = 0;
		int rownum = title.getRows();
		iCol = title.getCols() - iCols + 1;
		if (iCol <= title.getCols()) {
			title.setCellValue(rownum, iCol, strRight);
			title.setCellAlign(rownum, iCol, Table.ALIGN_RIGHT);
			title.mergeCell(rownum, iCol, rownum, title.getCols());
		} else {
			System.out.println("右表眉与左标题重复合并");
		}
	}

	// 在使用setDefaultTitleLeft和setDefaultTitleRight方法后使用
	public void setDefaultTitleCenter(String strCenter) {
		int iStarCol = 0;
		int iEndCol = 0;
		int rownum = title.getRows();
		iStarCol = title.getCell(rownum, 1).colSpan + 1;

		// 右title的位置
		for (int i = title.getCols(); i > iStarCol; i--) {
			if (title.getCell(rownum, i).used) {
				iEndCol = i - 1;
				break;
			}
		}

		if (iEndCol < iStarCol) {
			iEndCol = iStarCol;
		}

		title.setCellValue(rownum, iStarCol, strCenter);
		title.setCellAlign(rownum, iStarCol, Table.ALIGN_CENTER);
		title.mergeCell(rownum, iStarCol, rownum, iEndCol);
	}

	public void setTitle(String strTitle, int ColWidth[]) {
		Cell c = new Cell();
		c.setBorderNone();
		title = new Table(3, ColWidth.length, c);
		title.setWidth(ColWidth);
		title.setBorderNone();
		title.setCellValue(2, 1, strTitle);
		title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		title.setCellFont(2, 1, "", 16, true);
		title.mergeRowCells(2);
	}

	// 增加多行title时 可选定行进行 文本表头 设置
	public void setTitle(String strTitle, int iRow) {
		title.setCellValue(iRow, 1, strTitle);
		title.setCellAlign(iRow, 1, Table.ALIGN_CENTER);
		title.setCellFont(iRow, 1, "", 16, true);
		title.mergeRowCells(iRow);
	}

	// 增加多行title时 可选定行进行image表头设置
	public void setImageTitle(String strTitle, int iStartCol, int iEndCol,
			int iRow, int iAlign) {
		title.setCellValue(iRow, iStartCol, strTitle);
		title.setCellAlign(iRow, iStartCol, iAlign);
		// title.setCellAlign(2,2,Table.ALIGN_CENTER);
		title.setCellFont(iRow, iStartCol, "", 16, true);
		title.mergeCell(iRow, iStartCol, iRow, iEndCol);
		title.merge(iRow, iEndCol + 1, iRow, title.getCols());
	}

	public void setImageTitle(String strTitle, int iStartCol, int iEndCol,
			int ColWidth[]) {
		Cell c = new Cell();
		c.setBorderNone();
		title = new Table(3, ColWidth.length, c);
		title.setWidth(ColWidth);
		title.setBorderNone();
		title.setCellValue(2, iStartCol, strTitle);
		title.setCellAlign(2, iStartCol, Table.ALIGN_LEFT);
		// title.setCellAlign(2,2,Table.ALIGN_CENTER);
		title.setCellFont(2, iStartCol, "", 16, true);
		title.mergeCell(2, iStartCol, 2, iEndCol);
		title.merge(2, iEndCol + 1, 2, ColWidth.length);
	}

	public void setImageTitle(String strTitle, int iStartCol, int iEndCol,
			int ColWidth[], int iAlign) {
		Cell c = new Cell();
		c.setBorderNone();
		title = new Table(3, ColWidth.length, c);
		title.setWidth(ColWidth);
		title.setBorderNone();
		title.setCellValue(2, iStartCol, strTitle);
		title.setCellAlign(2, iStartCol, iAlign);
		// title.setCellAlign(2,2,Table.ALIGN_CENTER);
		title.setCellFont(2, iStartCol, "", 16, true);
		title.mergeCell(2, iStartCol, 2, iEndCol);
		title.merge(2, iEndCol + 1, 2, ColWidth.length);
	}

	public void setImageTitle(String strTitle, int ColWidth[]) {
		Cell c = new Cell();
		c.setBorderNone();
		title = new Table(3, ColWidth.length, c);
		title.setWidth(ColWidth);
		title.setBorderNone();
		title.setCellValue(2, 2, strTitle);
		title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		title.setCellAlign(2, 2, Table.ALIGN_CENTER);
		title.setCellFont(2, 2, "", 16, true);
		title.mergeCell(2, 2, 2, title.getCols() - 1);
	}

	public void createTitle(int iRows, int ColWidth[]) {
		Cell c = new Cell();
		c.setBorderNone();
		title = new Table(iRows, ColWidth.length, c);
		title.setWidth(ColWidth);
		title.setBorderNone();
	}

	public void createDefaultTitle(int ColWidth[]) {
		Cell c = new Cell();
		c.setBorderNone();

		title = new Table(3, ColWidth.length, c);
		title.setRowHeight(2, 40);
		title.setRowCells(2, Table.PER_FONTSIZE, 20);
		title.setWidth(ColWidth);
		title.setBorderNone();
	}

	public void createFooter(int iRows, int ColWidth[]) {
		Cell c = new Cell();
		c.setBorderNone();
		footer = new Table(iRows, ColWidth.length, ColWidth, c);
		footer.setBorderNone();
		// footer.setRowHeight(1, 10);
	}

	public void createDefautlFooter(int ColWidth[]) {
		Cell c = new Cell();
		c.setBorderNone();
		footer = new Table(2, ColWidth.length, c);
		footer.setWidth(ColWidth);
		footer.setBorderNone();
		footer.setRowHeight(1, 10);
	}

	public void setDefautlFooter(int iCol, int iMergeCols, String strValue,
			int iAlign) {
		try {
			if (iCol > footer.getCols()
					|| iCol + iMergeCols - 1 > footer.getCols()) {
				return;
			}
			int iRow = footer.getRows();
			footer.setCellValue(iRow, iCol, strValue);
			footer.setCellAlign(iRow, iCol, iAlign);
			footer.mergeCell(iRow, iCol, iRow, iCol + iMergeCols - 1);
		} catch (Exception e) {
			System.out.println("setDefautlFooter错误!");
		}
	}

	// 得把rs的数据填写到数组中
	public String[][] getDataFromRs(ResultSet rs, int iFixedRows,
			int iFooterRows) {
		String[][] ArrString = null;
		try {
			int i = 0;
			int j = 0;
			int iRows = 0;
			int iCols = 0;

			ResultSetMetaData rsmd = rs.getMetaData();

			if (!rs.isAfterLast()) {
				rs.last();
				iRows = rs.getRow();
				rs.beforeFirst();
			}
			iRows = iFixedRows + iRows + iFooterRows;
			iCols = rsmd.getColumnCount();

			ArrString = new String[iRows][iCols];

			i = iFixedRows;
			while (rs.next()) {
				for (j = 0; j < iCols; j++) {
					if (rs.getString(j + 1) != null) {
						ArrString[i][j] = rs.getString(j + 1);
					}
				}
				i = i + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ArrString;
	}

	public void setTitleHtml(String strHeadHtml) {
		mstrtitleHtml = strHeadHtml;
	}

	// 增加数据
	public void setBody(Table tb) {
		body = tb;
	}

	// 增加页脚
	public void setFooter(Table tb) {
		footer = tb;
	}

	/**
	 * @author yxz
	 * @param arrWidth
	 *            数组列宽
	 * @param paperStyle
	 *            纸张的样式 Report.PAPER_B5_WIDTH
	 * @return int[]
	 */
	public int[] getArrWidth(int[] arrWidth, int paperStyle) {

		for (int i = 0; i < arrWidth.length; i++) {
			arrWidth[i] = (int) (CustomMaths.div(paperStyle, PAPER_A4_WIDTH) * arrWidth[i]);
		}
		return arrWidth;
	}
	
	/**
	 * @author yxz
	 * @param pageRowA4
	 *            A4纸的页数
	 * @param paperStyle
	 *            纸张的样式 Report.PAPER_B5_WIDTH
	 * @return int
	 */
	public int getPageRows(int pageRowA4, int paperStyle) {

		pageRowA4 = (int) (CustomMaths.div(paperStyle, PAPER_A4_WIDTH) * pageRowA4);

		return pageRowA4;
	}
	/*
	 * 设置A4纸打印的共通方法
     */	
	private int paperStyle;// 定义纸张类型
	/**
	 * @author chenzt
	 * @param diancxxb_id
	 *            电厂id
	 * @param pagewith
	 *            传递纸张的样式 
	 * @return int
	 */
	public int paperStyle(long diancxxb_id,String pagewith) {
		// 函数功能：更具纸张类型设定纸张宽度
		// 函数逻辑：先从系统信息表中读取纸张类型的值（zhi）然后赋值给paperStlye
		// 参数：dianc_id 代表电厂信息表的id
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
//		int paperStyle = Report.PAPER_B4_WIDTH;
//		int paperStyle = Report.PAPER_B5;
		if(pagewith!=null){//||!"".equals(pagewith)
			
			paperStyle = Integer.parseInt(pagewith);
		}else{
			
			ResultSetList rsl = con
			.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='纸张类型' and diancxxb_id="
					+ diancxxb_id);//((Visit) this.getPage().getVisit()).getDiancxxb_id());
			if (rsl.next()) {
				paperStyle = rsl.getInt("zhi");
			}
			
			rsl.close();
		}
		con.Close();
		return paperStyle;
	}
}
