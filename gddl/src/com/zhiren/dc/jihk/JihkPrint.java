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
	
	private static final String PRINT_BAOER = "PRINT_BAOER";    //��ͷ����
	private static final String PRINT_HBW = "PRINT_HBW";        //������
	private static final String PRINT_DLT = "PRINT_DLT";        //���
	
	private static String IMG_TYPE = MimeTypes.MIME_PNG;
	// �����û���ʾ
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
			return "���ɻ����ͷ�ڶ��ȵ糧���俨";
		} else if (PRINT_HBW.equals(getDiancLb())) {
			return "�������ϵ�����˾�����巢�糧����ú���յ�";
		} else if (PRINT_DLT.equals(getDiancLb())) {
			return "�����ط��糧ȼ�ϲ�ú�����˼ƻ���";
		} else {
			return "";
		}
	}

	// ��ȡ��ѯ���
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
			String title= getReportTitle(); //"���ɻ����ͷ�ڶ��ȵ糧���俨";
			String foot1 = "�ü���";
			String foot2 = "ע��1���˿�ֻ�ޱ���ʹ�ã���������ת�����ˡ�2�� �����ɿ󷽲ü�������˽�Բü���3��������Ч��";
			String left1= "��������ƾ֤";
			String left2="ú������ƾ֤";
			while (rsl.next()) {
				//��ͷ ��һ��
				for (int j=0;j<5;j++) {
					data[0+(i*gridCout)][j]= title;
				}		
				//�ڶ���
				data[1+(i*gridCout)][0]="ָ��ú��";
				data[1+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[1+(i*gridCout)][2]="����";
				data[1+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[1+(i*gridCout)][4]=left1;
				//������
				data[2+(i*gridCout)][0]="ú��";
				data[2+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[2+(i*gridCout)][2]="����";
				data[2+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[2+(i*gridCout)][4]=left1;
				//������
				data[3+(i*gridCout)][0]="���˵�λ";
				data[3+(i*gridCout)][1]=rsl.getString("yunsdwb_id");
				data[3+(i*gridCout)][2]="��Ч����";
				data[3+(i*gridCout)][3]=rsl.getString("qianfrq") + "��" + rsl.getString("youxrq");
				data[3+(i*gridCout)][4]=left1;
				//������
				data[4+(i*gridCout)][0]="װúʱ��";
				data[4+(i*gridCout)][1]="";
				data[4+(i*gridCout)][2]="���ƺ�";
				data[4+(i*gridCout)][3]="";
				data[4+(i*gridCout)][4]=left1;
				//������
				data[5+(i*gridCout)][0]="ǩ����λ";
				data[5+(i*gridCout)][1]="";
				data[5+(i*gridCout)][2]="ú��ӡǩ";
				data[5+(i*gridCout)][3]="";
				data[5+(i*gridCout)][4]=left1;
				//������
				for (int j=0;j<5;j++) {
					data[6+(i*gridCout)][j]= foot1;
				}
				//�ڰ��� ����
				for (int j=0;j<5;j++) {
					data[7+(i*gridCout)][j]= "<hr style=\"border:2px dashed black; height:2px\">";
				}
				
				//�ڶ���
				//�ھ���
				for (int j=0;j<5;j++) {
					data[8+(i*gridCout)][j]= title;
				}
				//��ʮ��
				data[9+(i*gridCout)][0]="ָ��ú��";
				data[9+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[9+(i*gridCout)][2]="����";
				data[9+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";;
				data[9+(i*gridCout)][4]=left2;
				//��ʮһ��
				data[10+(i*gridCout)][0]="ú��";
				data[10+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[10+(i*gridCout)][2]="����";
				data[10+(i*gridCout)][3]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";;
				data[10+(i*gridCout)][4]=left2;
				//��ʮ����
				data[11+(i*gridCout)][0]="���˵�λ";
				data[11+(i*gridCout)][1]=rsl.getString("yunsdwb_id");
				data[11+(i*gridCout)][2]="��Ч����";
				data[11+(i*gridCout)][3]=rsl.getString("qianfrq") + "��" + rsl.getString("youxrq");
				data[11+(i*gridCout)][4]=left2;
				//��ʮ����
				data[12+(i*gridCout)][0]="װúʱ��";
				data[12+(i*gridCout)][1]="";
				data[12+(i*gridCout)][2]="���ƺ�";
				data[12+(i*gridCout)][3]="";
				data[12+(i*gridCout)][4]=left2;
				//��ʮ����		
				data[13+(i*gridCout)][0]="ǩ����λ";
				data[13+(i*gridCout)][1]="";
				data[13+(i*gridCout)][2]="ú��ӡǩ";
				data[13+(i*gridCout)][3]="";
				data[13+(i*gridCout)][4]=left2;
				//��ʮ����
				for (int j=0;j<5;j++) {
					data[14+(i*gridCout)][j]= foot2;
				}
	
				//��ʮ���� ʵ��,���iΪż��λ�������ϰ�ҳ���ʵ�߷ָ�
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
			String title=getReportTitle(); //"�������ϵ�����˾�����巢�糧����ú���յ�";
			String foot1 = "�ü���";
			String foot2 = "ע��1���˿�ֻ�ޱ���ʹ�ã���������ת�����ˡ�2�� �����ɿ󷽲ü�������˽�Բü���3��������Ч��";
			String left1= "��������ƾ֤";
			String left2="ú������ƾ֤";
			while (rsl.next()) {
				//��ͷ ��һ��
				for (int j=0;j<5;j++) {
					data[0+(i*gridCout)][j]= title;
				}		
				//�ڶ���
				data[1+(i*gridCout)][0]="ָ��ú��";
				data[1+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[1+(i*gridCout)][2]=rsl.getString("meikxxb_id");
				data[1+(i*gridCout)][3]="����";
				data[1+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[1+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[1+(i*gridCout)][6]=left1;
				//������
				data[2+(i*gridCout)][0]="ú��";
				data[2+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[2+(i*gridCout)][2]=rsl.getString("pinzb_id");
				data[2+(i*gridCout)][3]="����";
				data[2+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[2+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[2+(i*gridCout)][6]=left1;
				//������
				data[3+(i*gridCout)][0]="���˵�λ";
				data[3+(i*gridCout)][1]="";
				data[3+(i*gridCout)][2]="";
				data[3+(i*gridCout)][3]="��Ч����";
				data[3+(i*gridCout)][4]=rsl.getString("qianfrq") + "��" + rsl.getString("youxrq");
				data[3+(i*gridCout)][5]=rsl.getString("qianfrq") + "��" + rsl.getString("youxrq");
				data[3+(i*gridCout)][6]=left1;
				//������
				data[4+(i*gridCout)][0]="װúʱ��";
				data[4+(i*gridCout)][1]="";
				data[4+(i*gridCout)][2]="";
				data[4+(i*gridCout)][3]="���ƺ�";
				data[4+(i*gridCout)][4]="";
				data[4+(i*gridCout)][5]="";
				data[4+(i*gridCout)][6]=left1;
				//������
				data[5+(i*gridCout)][0]="ǩ����λ";
				data[5+(i*gridCout)][1]="";
				data[5+(i*gridCout)][2]="";
				data[5+(i*gridCout)][3]="ú��ӡǩ";
				data[5+(i*gridCout)][4]="";
				data[5+(i*gridCout)][5]="";
				data[5+(i*gridCout)][6]=left1;
				//�¼���
				data[6+(i*gridCout)][0]="ȫ��";
				data[6+(i*gridCout)][1]="";
				data[6+(i*gridCout)][2]="����";
				data[6+(i*gridCout)][3]="";
				data[6+(i*gridCout)][4]="���Ա";
				data[6+(i*gridCout)][5]="";
				data[6+(i*gridCout)][6]=left1;
				//������
				for (int j=0;j<7;j++) {
					data[7+(i*gridCout)][j]= foot1;
				}
				//�ڰ��� ����
				for (int j=0;j<7;j++) {
					data[8+(i*gridCout)][j]= "<hr style=\"border:2px dashed black; height:2px\">";
				}
				
				//�ڶ���
				//�ھ���
				for (int j=0;j<7;j++) {
					data[9+(i*gridCout)][j]= title;
				}
				//��ʮ��
				data[10+(i*gridCout)][0]="ָ��ú��";
				data[10+(i*gridCout)][1]=rsl.getString("meikxxb_id");
				data[10+(i*gridCout)][2]=rsl.getString("meikxxb_id");
				data[10+(i*gridCout)][3]="����";
				data[10+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[10+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[10+(i*gridCout)][6]=left2;
				//��ʮһ��
				data[11+(i*gridCout)][0]="ú��";
				data[11+(i*gridCout)][1]=rsl.getString("pinzb_id");
				data[11+(i*gridCout)][2]=rsl.getString("pinzb_id");
				data[11+(i*gridCout)][3]="����";
				data[11+(i*gridCout)][4]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[11+(i*gridCout)][5]="<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/>";
				data[11+(i*gridCout)][6]=left2;
				//��ʮ����
				data[12+(i*gridCout)][0]="���˵�λ";
				data[12+(i*gridCout)][1]="";
				data[12+(i*gridCout)][2]="";
				data[12+(i*gridCout)][3]="��Ч����";
				data[12+(i*gridCout)][4]=rsl.getString("qianfrq") + "��" + rsl.getString("youxrq");
				data[12+(i*gridCout)][5]=rsl.getString("qianfrq") + "��" + rsl.getString("youxrq");
				data[12+(i*gridCout)][6]=left2;
				//��ʮ����
				data[13+(i*gridCout)][0]="װúʱ��";
				data[13+(i*gridCout)][1]="";
				data[13+(i*gridCout)][2]="";
				data[13+(i*gridCout)][3]="���ƺ�";
				data[13+(i*gridCout)][4]="";
				data[13+(i*gridCout)][5]="";
				data[13+(i*gridCout)][6]=left2;
				//��ʮ����		
				data[14+(i*gridCout)][0]="ǩ����λ";
				data[14+(i*gridCout)][1]="";
				data[14+(i*gridCout)][2]="";
				data[14+(i*gridCout)][3]="ú��ӡǩ";
				data[14+(i*gridCout)][4]="";
				data[14+(i*gridCout)][5]="";
				data[14+(i*gridCout)][6]=left2;
				//�¼���
				data[15+(i*gridCout)][0]="ȫ��";
				data[15+(i*gridCout)][1]="";
				data[15+(i*gridCout)][2]="����";
				data[15+(i*gridCout)][3]="";
				data[15+(i*gridCout)][4]="���Ա";
				data[15+(i*gridCout)][5]="";
				data[15+(i*gridCout)][6]=left2;
				//��ʮ����
				for (int j=0;j<7;j++) {
					data[16+(i*gridCout)][j]= foot2;
				}

				//��ʮ���� ʵ��,���iΪż��λ�������ϰ�ҳ���ʵ�߷ָ�
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
			String title=getReportTitle(); //"�����ط��糧ȼ�ϲ�ú�����˼ƻ���";
			
			while (rsl.next()) {
				//��ͷ ��һ��
				for (int j=0;j<9;j++) {
					data[0+(i*gridCout)][j]= title;
				}
				
				//�ڶ���
				data[1+(i*gridCout)][0] = "����";
				for (int j=1;j<7;j++)
					data[1+(i*gridCout)][j] = rsl.getString("meikxxb_id");
				
				data[1+(i*gridCout)][7] = "���˵�λ";
				data[1+(i*gridCout)][8] = rsl.getString("yunsdwb_id");
				
				//������
				data[2+(i*gridCout)][0] = "��֤����";
				for (int j=1;j<7;j++)
					data[2+(i*gridCout)][j] = rsl.getString("qianfrq") + "��" + rsl.getString("youxrq");
				data[2+(i*gridCout)][7] = "����";
				
				//������
				data[3+(i*gridCout)][0] = "�۶�";
				data[3+(i*gridCout)][1] = "��ˮ";
				data[3+(i*gridCout)][3] = "����";
				data[3+(i*gridCout)][5] = "����";
				data[3+(i*gridCout)][7] = "˾������";
				
				//������
				data[4+(i*gridCout)][0] = "�۶�";
				data[4+(i*gridCout)][1] = "��ˮ";
				data[4+(i*gridCout)][3] = "����";
				data[4+(i*gridCout)][5] = "����";
				data[4+(i*gridCout)][7]="��ע" + "&nbsp;&nbsp;&nbsp<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/></div>";
				data[4+(i*gridCout)][8] = "��ע" + "&nbsp;&nbsp;&nbsp<img src='" + MainGlobal.getHomeContext(getPage())+"/barcode?msg=" + rsl.getString("xuh")+ "&type=code128&fmt=" + IMG_TYPE + "' height=\"50px\" width=180px/></div>";
				
				//������
				data[5+(i*gridCout)][0] = "����";
				data[5+(i*gridCout)][3] = "жú��";
				data[5+(i*gridCout)][4] = "жú��";
				data[5+(i*gridCout)][7] = "ú������";
				
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
				
				//��ͷ���塢�����ʽ
				rt.body.setRowCells((j*6)+1, Table.PER_FONTSIZE, 16);
				rt.body.setRowCells((j*6)+1, Table.PER_ALIGN, Table.ALIGN_CENTER);
				
				rt.body.setRowHeight((j*6)+ 5, 29*3);
				

			}
//			//ȥ���߿�
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
			return "��������ȷ";
		}
		
		
		printhtmls.append(TableAllHtml(con, jihkID, visit));			
		return printhtmls.toString();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		ToolbarButton rbtn = new ToolbarButton(null, "����",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Return);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}

	// ������ʹ�õķ���
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

	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setString10(visit.getActivePageName());
			setDiancLb(visit.getString15());
		}

		getSelectData();
	}

	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Visit visit = (Visit) getPage().getVisit();
			cycle.activate(visit.getString10());
		}
	}

	// ҳ���½��֤
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
