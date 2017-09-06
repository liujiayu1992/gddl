package com.zhiren.dc.jihk;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
//import org.krysalis.barcode4j.tools.MimeTypes;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class JihkPrint extends BasePage implements PageValidateListener {
	
	private static final String PRINT_BAOER = "PRINT_BAOER";    //包头二电
	private static final String PRINT_HBW = "PRINT_HBW";        //海勃湾
	private static final String PRINT_DLT = "PRINT_DLT";        //达电
	
	private static String IMG_TYPE = MimeTypes.MIME_PNG;
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public boolean getRaw() {
		return true;
	}
	
	private String getReportTitle() {
		if (PRINT_BAOER.equals(getDiancLb())) {
			return "内蒙华电包头第二热电厂运输卡";
		} else if (PRINT_HBW.equals(getDiancLb())) {
			return "北方联合电力公司海勃湾发电厂进厂煤验收单";
		} else if (PRINT_DLT.equals(getDiancLb())) {
			return "达拉特发电厂燃料部煤矿拉运计划卡";
		} else {
			return "";
		}
	}

	// 获取查询语句
	public String getBaseSql(String jihkID) {
		StringBuffer sql = new StringBuffer();
		if (PRINT_BAOER.equals(getDiancLb())) {
			sql.append(
					"select jhz.id,\n" +
					"       jhz.xuh,\n" + 
					"       mk.mingc as meikxxb_id,\n" + 
					"       yd.mingc as yunsdwb_id,\n" + 
					"       pz.mingc as pinzb_id,\n" + 
					"       to_char(jh.kaisrq, 'yyyy-mm-dd') as qianfrq,\n" + 
					"       to_char(jh.jiezrq, 'yyyy-mm-dd') as youxrq\n" + 
					"  from jihb jh, jihzb jhz, meikxxb mk, yunsdwb yd, pinzb pz\n" + 
					" where jh.id = jhz.jihb_id\n" + 
					"   and jh.meikxxb_id = mk.id\n" + 
					"   and jh.yunsdwb_id = yd.id(+)\n" + 
					"   and jh.pinzb_id = pz.id\n" +
					"   and jhz.shifsy=0\n" +
					"   and jh.id = " + jihkID + " order by jhz.xuh"
			);
			
		} else if (PRINT_HBW.equals(getDiancLb())) {
			sql.append(
					"select jhz.id,\n" +
					"       jhz.xuh,\n" + 
					"       mk.mingc as meikxxb_id,\n" + 
					"       yd.mingc as yunsdwb_id,\n" + 
					"       pz.mingc as pinzb_id,\n" + 
					"       to_char(jh.kaisrq, 'yyyy-mm-dd') as qianfrq,\n" + 
					"       to_char(jh.jiezrq, 'yyyy-mm-dd') as youxrq\n" + 
					"  from jihb jh, jihzb jhz, meikxxb mk, yunsdwb yd, pinzb pz\n" + 
					" where jh.id = jhz.jihb_id\n" + 
					"   and jh.meikxxb_id = mk.id\n" + 
					"   and jh.yunsdwb_id = yd.id(+)\n" + 
					"   and jh.pinzb_id = pz.id\n" +
					"   and jhz.shifsy=0\n" +
					"   and jh.id = " + jihkID + " order by jhz.xuh"
			);
			
		} else if (PRINT_DLT.equals(getDiancLb())) {
			sql.append(
					"select jhz.id,\n" +
					"       jhz.xuh,\n" + 
					"       mk.mingc as meikxxb_id,\n" + 
					"       yd.mingc as yunsdwb_id,\n" + 
					"       pz.mingc as pinzb_id,\n" + 
					"       to_char(jh.kaisrq, 'yyyy-mm-dd') as qianfrq,\n" + 
					"       to_char(jh.jiezrq, 'yyyy-mm-dd') as youxrq\n" + 
					"  from jihb jh, jihzb jhz, meikxxb mk, yunsdwb yd, pinzb pz\n" + 
					" where jh.id = jhz.jihb_id\n" + 
					"   and jh.meikxxb_id = mk.id\n" + 
					"   and jh.yunsdwb_id = yd.id(+)\n" + 
					"   and jh.pinzb_id = pz.id\n" +
					"   and jhz.shifsy=0\n" +
					"   and jh.id = " + jihkID + " order by jhz.xuh"
			);
		}
		
		return sql.toString();
	}
	
	public String TableAllHtml(JDBCcon con,String jihkID, Visit visit) {
		Visit v = (Visit) this.getPage().getVisit();
		ResultSetList rsl = null;
		Report rt = new Report();
		rsl = con.getResultSetList(getBaseSql(jihkID));
		int arrLen = rsl.getRows();		
		if (arrLen==0) {
			return "";
		}
		if (PRINT_BAOER.equals(getDiancLb())) {		
			int i=0;
			int gridCout = 16;
			String data[][] = new String[gridCout*arrLen][5];
			String title= getReportTitle(); //"内蒙华电包头第二热电厂运输卡";
			String foot1 = "裁剪线";
			String foot2 = "注：1）此卡只限本人使用，不得污损、转借他人。2） 必须由矿方裁剪，不得私自裁剪。3）盖章有效。";
			String left1= "进厂验收凭证";
			String left2="煤矿留存凭证";
			while (rsl.next()) {
				//表头 第一行
				for (int j=0;j<5;j++) {
					data[0+(i*gridCout)][j]= title;
				}		
				//第二行
				data[1+(i*gridCout)][0]="指定煤矿";
				data[1+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[1+(i*gridCout)][2]="条码";
				data[1+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[1+(i*gridCout)][4]=left1;
				//第三行
				data[2+(i*gridCout)][0]="煤种";
				data[2+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[2+(i*gridCout)][2]="条码";
				data[2+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[2+(i*gridCout)][4]=left1;
				//第四行
				data[3+(i*gridCout)][0]="承运单位";
				data[3+(i*gridCout)][1]=rsl.getString("yunsdwb_id");
				data[3+(i*gridCout)][2]="有效日期";
				data[3+(i*gridCout)][3]=rsl.getString("qianfrq") + "至" + rsl.getString("youxrq");
				data[3+(i*gridCout)][4]=left1;
				//第五行
				data[4+(i*gridCout)][0]="装煤时间";
				data[4+(i*gridCout)][1]="";
				data[4+(i*gridCout)][2]="车牌号";
				data[4+(i*gridCout)][3]="";
				data[4+(i*gridCout)][4]=left1;
				//第六行
				data[5+(i*gridCout)][0]="签发单位";
				data[5+(i*gridCout)][1]="";
				data[5+(i*gridCout)][2]="煤矿印签";
				data[5+(i*gridCout)][3]="";
				data[5+(i*gridCout)][4]=left1;
				//第七行
				for (int j=0;j<5;j++) {
					data[6+(i*gridCout)][j]= foot1;
				}
				//第八行 虚线
				for (int j=0;j<5;j++) {
					data[7+(i*gridCout)][j]= "<hr style=\"border:2px dashed black; height:2px\">";
				}
				
				//第二联
				//第九行
				for (int j=0;j<5;j++) {
					data[8+(i*gridCout)][j]= title;
				}
				//第十行
				data[9+(i*gridCout)][0]="指定煤矿";
				data[9+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[9+(i*gridCout)][2]="条码";
				data[9+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";;
				data[9+(i*gridCout)][4]=left2;
				//第十一行
				data[10+(i*gridCout)][0]="煤种";
				data[10+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[10+(i*gridCout)][2]="条码";
				data[10+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";;
				data[10+(i*gridCout)][4]=left2;
				//第十二行
				data[11+(i*gridCout)][0]="承运单位";
				data[11+(i*gridCout)][1]=rsl.getString("yunsdwb_id");
				data[11+(i*gridCout)][2]="有效日期";
				data[11+(i*gridCout)][3]=rsl.getString("qianfrq") + "至" + rsl.getString("youxrq");
				data[11+(i*gridCout)][4]=left2;
				//第十三行
				data[12+(i*gridCout)][0]="装煤时间";
				data[12+(i*gridCout)][1]="";
				data[12+(i*gridCout)][2]="车牌号";
				data[12+(i*gridCout)][3]="";
				data[12+(i*gridCout)][4]=left2;
				//第十四行		
				data[13+(i*gridCout)][0]="签发单位";
				data[13+(i*gridCout)][1]="";
				data[13+(i*gridCout)][2]="煤矿印签";
				data[13+(i*gridCout)][3]="";
				data[13+(i*gridCout)][4]=left2;
				//第十五行
				for (int j=0;j<5;j++) {
					data[14+(i*gridCout)][j]= foot2;
				}
	
				//第十六行 实线,如果i为偶数位置属于上半页需加实线分隔
				if (i%2==0) {
					for (int j=0;j<5;j++) {
						data[15+(i*gridCout)][j]= "<hr style=\"border:2px solid black; height:2px\">";
					}
				} else {
	//				for (int j=0;j<5;j++) {
	//					if (i!=arrLen-1) {
	//						data[15+(i*gridCout)][j]= "<div style=\"page-break-after:always\"> </div>";
	//					}
	//				}
				}
				i++;
			}
	
			int[] ArrWidth = new int[] {120, 180, 120, 195, 28};
			rt.setBody(new Table(data, 0, 0, 0));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(32);
			rt.body.setBorderNone();
			rt.body.setRowHeight(36);
			rt.body.setFontSize(12);
			for(int j=1;j<=rt.body.getCols();j++)  rt.body.setColAlign(j, Table.ALIGN_CENTER);
			
			for (int j=1,k=7;j<rt.body.getRows();j=j+8,k=k+8) {
				rt.body.merge(k+1, 1, k+1, 5);
				rt.body.merge(j, 1, j, 5);
				rt.body.merge(j+1, 3, j+2, 3);
				rt.body.merge(j+1, 4, j+2, 4);
				rt.body.merge(k, 1, k, 5);
				rt.body.merge(j+1, 5, j+1+4, 5);
				rt.body.setRowHeight(j+6,5);
				
				rt.body.setRowCells(j, Table.PER_BORDER_TOP, 0);
				rt.body.setRowCells(j, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(j, Table.PER_BORDER_RIGHT, 0);
				rt.body.setRowCells(j, Table.PER_FONTSIZE, 16);
				rt.body.setRowCells(j, Table.PER_FONTBOLD, true);
				rt.body.setRowHeight(j, 30);
				
				rt.body.setRowCells(k, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setRowCells(k, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(k, Table.PER_BORDER_RIGHT, 0);
	
				rt.body.setRowCells(k, Table.PER_FONTSIZE, 9);
				rt.body.setRowCells(k+1, Table.PER_FONTSIZE, 9);
				
				rt.body.setRowCells(k+1, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setRowCells(k+1, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(k+1, Table.PER_BORDER_RIGHT, 0);
				
				rt.body.setCells(j+1, 1, j+1+4, 1, Table.PER_BORDER_LEFT, 2);
				rt.body.setCells(j+1, 5, j+1+4, 5, Table.PER_BORDER_RIGHT, 2);
				rt.body.setRowCells(j+1, Table.PER_BORDER_TOP, 1);
				rt.body.setRowCells(j+1+4, Table.PER_BORDER_BOTTOM, 2);
				
				
				
			}
			
			for (int j=1;j<=rt.body.getRows();j++) {
				if (j%7==0) {
					rt.body.setRowCells(j, Table.PER_ALIGN, Table.ALIGN_CENTER);
	//				rt.body.setCells(j, 1, j, 5, Table.PER_VALIGN, Table.VALIGN_BOTTOM);
				} 
				if (j%32==0) {
					rt.body.setRowHeight(j,10);
					rt.body.setRowCells(j-1, Table.PER_ALIGN, Table.ALIGN_LEFT);
				} else if (j%16==0) {
					rt.body.setRowHeight(j,40);
					rt.body.setRowCells(j-1, Table.PER_ALIGN, Table.ALIGN_LEFT);
				} else if (j%8==0) {
					rt.body.setRowHeight(j,10);
				}
				
			}
		} else if (PRINT_HBW.equals(getDiancLb())) {
			
			int i=0;
			int gridCout = 18;
			String data[][] = new String[gridCout*arrLen][7];
			String title=getReportTitle(); //"北方联合电力公司海勃湾发电厂进厂煤验收单";
			String foot1 = "裁剪线";
			String foot2 = "注：1）此卡只限本人使用，不得污损、转借他人。2） 必须由矿方裁剪，不得私自裁剪。3）盖章有效。";
			String left1= "进厂验收凭证";
			String left2="煤矿留存凭证";
			while (rsl.next()) {
				//表头 第一行
				for (int j=0;j<5;j++) {
					data[0+(i*gridCout)][j]= title;
				}		
				//第二行
				data[1+(i*gridCout)][0]="指定煤矿";
				data[1+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[1+(i*gridCout)][2]=rsl.getString("meikxxb_id");
				data[1+(i*gridCout)][3]="条码";
				data[1+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[1+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[1+(i*gridCout)][6]=left1;
				//第三行
				data[2+(i*gridCout)][0]="煤种";
				data[2+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[2+(i*gridCout)][2]=rsl.getString("pinzb_id");
				data[2+(i*gridCout)][3]="条码";
				data[2+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[2+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[2+(i*gridCout)][6]=left1;
				//第四行
				data[3+(i*gridCout)][0]="承运单位";
				data[3+(i*gridCout)][1]="";
				data[3+(i*gridCout)][2]="";
				data[3+(i*gridCout)][3]="有效日期";
				data[3+(i*gridCout)][4]=rsl.getString("qianfrq") + "至" + rsl.getString("youxrq");
				data[3+(i*gridCout)][5]=rsl.getString("qianfrq") + "至" + rsl.getString("youxrq");
				data[3+(i*gridCout)][6]=left1;
				//第五行
				data[4+(i*gridCout)][0]="装煤时间";
				data[4+(i*gridCout)][1]="";
				data[4+(i*gridCout)][2]="";
				data[4+(i*gridCout)][3]="车牌号";
				data[4+(i*gridCout)][4]="";
				data[4+(i*gridCout)][5]="";
				data[4+(i*gridCout)][6]=left1;
				//第六行
				data[5+(i*gridCout)][0]="签发单位";
				data[5+(i*gridCout)][1]="";
				data[5+(i*gridCout)][2]="";
				data[5+(i*gridCout)][3]="煤矿印签";
				data[5+(i*gridCout)][4]="";
				data[5+(i*gridCout)][5]="";
				data[5+(i*gridCout)][6]=left1;
				//新加行
				data[6+(i*gridCout)][0]="全重";
				data[6+(i*gridCout)][1]="";
				data[6+(i*gridCout)][2]="净重";
				data[6+(i*gridCout)][3]="";
				data[6+(i*gridCout)][4]="检斤员";
				data[6+(i*gridCout)][5]="";
				data[6+(i*gridCout)][6]=left1;
				//第七行
				for (int j=0;j<7;j++) {
					data[7+(i*gridCout)][j]= foot1;
				}
				//第八行 虚线
				for (int j=0;j<7;j++) {
					data[8+(i*gridCout)][j]= "<hr style=\"border:2px dashed black; height:2px\">";
				}
				
				//第二联
				//第九行
				for (int j=0;j<7;j++) {
					data[9+(i*gridCout)][j]= title;
				}
				//第十行
				data[10+(i*gridCout)][0]="指定煤矿";
				data[10+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[10+(i*gridCout)][2]=rsl.getString("meikxxb_id");
				data[10+(i*gridCout)][3]="条码";
				data[10+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[10+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[10+(i*gridCout)][6]=left2;
				//第十一行
				data[11+(i*gridCout)][0]="煤种";
				data[11+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[11+(i*gridCout)][2]=rsl.getString("pinzb_id");
				data[11+(i*gridCout)][3]="条码";
				data[11+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[11+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[11+(i*gridCout)][6]=left2;
				//第十二行
				data[12+(i*gridCout)][0]="承运单位";
				data[12+(i*gridCout)][1]="";
				data[12+(i*gridCout)][2]="";
				data[12+(i*gridCout)][3]="有效日期";
				data[12+(i*gridCout)][4]=rsl.getString("qianfrq") + "至" + rsl.getString("youxrq");
				data[12+(i*gridCout)][5]=rsl.getString("qianfrq") + "至" + rsl.getString("youxrq");
				data[12+(i*gridCout)][6]=left2;
				//第十三行
				data[13+(i*gridCout)][0]="装煤时间";
				data[13+(i*gridCout)][1]="";
				data[13+(i*gridCout)][2]="";
				data[13+(i*gridCout)][3]="车牌号";
				data[13+(i*gridCout)][4]="";
				data[13+(i*gridCout)][5]="";
				data[13+(i*gridCout)][6]=left2;
				//第十四行		
				data[14+(i*gridCout)][0]="签发单位";
				data[14+(i*gridCout)][1]="";
				data[14+(i*gridCout)][2]="";
				data[14+(i*gridCout)][3]="煤矿印签";
				data[14+(i*gridCout)][4]="";
				data[14+(i*gridCout)][5]="";
				data[14+(i*gridCout)][6]=left2;
				//新加行
				data[15+(i*gridCout)][0]="全重";
				data[15+(i*gridCout)][1]="";
				data[15+(i*gridCout)][2]="净重";
				data[15+(i*gridCout)][3]="";
				data[15+(i*gridCout)][4]="检斤员";
				data[15+(i*gridCout)][5]="";
				data[15+(i*gridCout)][6]=left2;
				//第十五行
				for (int j=0;j<7;j++) {
					data[16+(i*gridCout)][j]= foot2;
				}

				//第十六行 实线,如果i为偶数位置属于上半页需加实线分隔
				if (i%2==0) {
					for (int j=0;j<7;j++) {
						data[17+(i*gridCout)][j]= "<hr style=\"border:2px solid black; height:2px\">";
					}
				} else {
//					for (int j=0;j<5;j++) {
//						if (i!=arrLen-1) {
//							data[15+(i*gridCout)][j]= "<div style=\"page-break-after:always\"> </div>";
//						}
//					}
				}
				i++;
			}

			int[] ArrWidth = new int[] {120,90,90,120,95,100,28};
			rt.setBody(new Table(data, 0, 0, 0));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(36);
			rt.body.setBorderNone();
			rt.body.setRowHeight(29);
			rt.body.setFontSize(12);
			for(int j=1;j<=rt.body.getCols();j++)  rt.body.setColAlign(j, Table.ALIGN_CENTER);
			
			for (int j=1,k=8;j<rt.body.getRows();j=j+9,k=k+9) {
				rt.body.merge(k+1, 1, k+1, 7);
				rt.body.merge(j, 1, j, 7);
				
				rt.body.merge(j+1, 2, j+1, 3);
				rt.body.merge(j+1, 5, j+1, 6);
				rt.body.merge(j+2, 2, j+2, 3);
				rt.body.merge(j+2, 5, j+2, 6);
				rt.body.merge(j+3, 2, j+3, 3);
				rt.body.merge(j+3, 5, j+3, 6);
				rt.body.merge(j+4, 2, j+4, 3);
				rt.body.merge(j+4, 5, j+4, 6);
				rt.body.merge(j+5, 2, j+5, 3);
				rt.body.merge(j+5, 5, j+5, 6);
				
				rt.body.merge(j+1, 4, j+2, 4);
				rt.body.merge(j+1, 5, j+2, 5);
				rt.body.merge(j+1, 6, j+2, 6);
				rt.body.merge(k, 1, k, 7);
				rt.body.merge(j+1, 7, j+1+5, 7);
				rt.body.setRowHeight(j+7,5);
				
				rt.body.setRowCells(j, Table.PER_BORDER_TOP, 0);
				rt.body.setRowCells(j, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(j, Table.PER_BORDER_RIGHT, 0);
				rt.body.setRowCells(j, Table.PER_FONTSIZE, 16);
				rt.body.setRowCells(j, Table.PER_FONTBOLD, true);
				rt.body.setRowHeight(j, 30);
				
				rt.body.setRowCells(k, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setRowCells(k, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(k, Table.PER_BORDER_RIGHT, 0);

				rt.body.setRowCells(k, Table.PER_FONTSIZE, 9);
				rt.body.setRowCells(k+1, Table.PER_FONTSIZE, 9);
				
				rt.body.setRowCells(k+1, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setRowCells(k+1, Table.PER_BORDER_LEFT, 0);
				rt.body.setRowCells(k+1, Table.PER_BORDER_RIGHT, 0);
				
				rt.body.setCells(j+1, 1, j+1+5, 1, Table.PER_BORDER_LEFT, 2);
				rt.body.setCells(j+1, 7, j+1+5, 7, Table.PER_BORDER_RIGHT, 2);
				rt.body.setRowCells(j+1, Table.PER_BORDER_TOP, 1);
				rt.body.setRowCells(j+1+5, Table.PER_BORDER_BOTTOM, 2);
				
				
				
			}
			
			for (int j=1;j<=rt.body.getRows();j++) {
				if (j%8==0) {
					rt.body.setRowCells(j, Table.PER_ALIGN, Table.ALIGN_CENTER);
//					rt.body.setCells(j, 1, j, 5, Table.PER_VALIGN, Table.VALIGN_BOTTOM);
				} 
				if (j%34==0) {
					rt.body.setRowHeight(j,10);
					rt.body.setRowCells(j-1, Table.PER_ALIGN, Table.ALIGN_LEFT);
				} else if (j%18==0) {
					rt.body.setRowHeight(j,40);
					rt.body.setRowCells(j-1, Table.PER_ALIGN, Table.ALIGN_LEFT);
				} else if (j%9==0) {
					rt.body.setRowHeight(j,10);
				}
				
			}

		} else if (PRINT_DLT.equals(getDiancLb())) {
			int i=0;
			int gridCout = 6;
			String data[][] = new String[gridCout*arrLen][9];
			String title=getReportTitle(); //"达拉特发电厂燃料部煤矿拉运计划卡";
			
			while (rsl.next()) {
				//表头 第一行
				for (int j=0;j<9;j++) {
					data[0+(i*gridCout)][j]= title;
				}
				
				//第二行
				data[1+(i*gridCout)][0] = "矿名";
				for (int j=1;j<7;j++)
					data[1+(i*gridCout)][j] = rsl.getString("meikxxb_id");
				
				data[1+(i*gridCout)][7] = "承运单位";
				data[1+(i*gridCout)][8] = rsl.getString("yunsdwb_id");
				
				//第三行
				data[2+(i*gridCout)][0] = "发证日期";
				for (int j=1;j<7;j++)
					data[2+(i*gridCout)][j] = rsl.getString("qianfrq") + "至" + rsl.getString("youxrq");
				data[2+(i*gridCout)][7] = "车号";
				
				//第四行
				data[3+(i*gridCout)][0] = "扣吨";
				data[3+(i*gridCout)][1] = "扣水";
				data[3+(i*gridCout)][3] = "粒度";
				data[3+(i*gridCout)][5] = "其他";
				data[3+(i*gridCout)][7] = "司机名称";
				
				//第五行
				data[4+(i*gridCout)][0] = "扣吨";
				data[4+(i*gridCout)][1] = "扣水";
				data[4+(i*gridCout)][3] = "粒度";
				data[4+(i*gridCout)][5] = "其他";
				data[4+(i*gridCout)][7]="备注" + "&nbsp;&nbsp;&nbsp<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/></div>";
				data[4+(i*gridCout)][8] = "备注" + "&nbsp;&nbsp;&nbsp<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/></div>";
				
				//第六行
				data[5+(i*gridCout)][0] = "采样";
				data[5+(i*gridCout)][3] = "卸煤队";
				data[5+(i*gridCout)][4] = "卸煤队";
				data[5+(i*gridCout)][7] = "煤场管理";
				
				i++;
			}
			
			int[] ArrWidth = new int[] {90,30,50,30,50,30,50,90,180};
			rt.setBody(new Table(data, 0, 0, 0));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(6);
			rt.body.setBorderNone();
			rt.body.setRowHeight(29);
			rt.body.setFontSize(12);
			
			for (int j=0;j<i;j++) {
				rt.body.merge((j*6)+1, 1, (j*6)+1, 9);
				rt.body.merge((j*6)+2, 2, (j*6)+2, 7);
				rt.body.merge((j*6)+3, 2, (j*6)+3, 7);
				rt.body.merge((j*6)+4, 1, (j*6)+5, 1);
				rt.body.merge((j*6)+4, 2, (j*6)+5, 2);
				rt.body.merge((j*6)+4, 3, (j*6)+5, 3);
				rt.body.merge((j*6)+4, 4, (j*6)+5, 4);
				rt.body.merge((j*6)+4, 5, (j*6)+5, 5);
				rt.body.merge((j*6)+4, 6, (j*6)+5, 6);
				rt.body.merge((j*6)+4, 7, (j*6)+5, 7);
				rt.body.merge((j*6)+5, 8, (j*6)+5, 9);
				rt.body.merge((j*6)+6, 2, (j*6)+6, 3);
				rt.body.merge((j*6)+6, 4, (j*6)+6, 5);
				rt.body.merge((j*6)+6, 6, (j*6)+6, 7);
				
				//表头字体、对齐格式
				rt.body.setRowCells((j*6)+1, Table.PER_FONTSIZE, 16);
				rt.body.setRowCells((j*6)+1, Table.PER_ALIGN, Table.ALIGN_CENTER);
				
				rt.body.setRowHeight((j*6)+ 5, 29*3);
				

			}
//			//去掉边框
//			for (int j=1;j<=rt.body.getRows();j++) {
//				rt.body.setRowCells(j, Table.PER_BORDER_BOTTOM, 0);
//				rt.body.setRowCells(j, Table.PER_BORDER_LEFT, 0);
//				rt.body.setRowCells(j, Table.PER_BORDER_RIGHT, 0);
//				}			
		}
		rt.setPageRows(1);
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		return rt.getAllPagesHtml();
	}

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		StringBuffer printhtmls=new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		String jihkID = visit.getString8();
		if (jihkID == null || "".equals(jihkID)) {
			return "参数不正确";
		}
		
		
		printhtmls.append(TableAllHtml(con, jihkID, visit));			
		return printhtmls.toString();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		ToolbarButton rbtn = new ToolbarButton(null, "返回",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Return);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}

	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	private String diancLb;
	public void setDiancLb(String value) {
		diancLb = value;
	}
	
	private String getDiancLb() {
		return diancLb;
	}

	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setString10(visit.getActivePageName());
			setDiancLb(visit.getString15());
		}

		getSelectData();
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Visit visit = (Visit) getPage().getVisit();
			cycle.activate(visit.getString10());
		}
	}

	// 页面登陆验证
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
}
