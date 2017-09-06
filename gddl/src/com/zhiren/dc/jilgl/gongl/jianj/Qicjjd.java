package com.zhiren.dc.jilgl.gongl.jianj;

import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.DateUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 2009-05-13
 * ����
 * ������ɽ��ڵ糧��ﵥ ���ؼ���ʱ���ֵĸ��������������BUG
 */
/*
 * 2009-05-08
 * ����
 * ���Ӵ��ƹ�����ɽ�糧��������ⵥ 
 * ������ʾ���� PRINT_GUIGUAN
 */
/*
 * 2009-04-21
 * ����
 * �޸ı�עû��Ĭ��ֵ��ɼ�ﵥ�޷���ʾ��BUG
 */
/*
 * ���ߣ���ΰ
 * ʱ�䣺2009-03-24
 * �޸����ݣ�
 *  	��ͷ���糧��ﵥ����糧����̨ͷ�ͼ��Ա
 */
/*
 * ����:���ش�
 * ʱ�䣺2009-10-29
 * �޸����ݣ�
 *  	������糧��ﵥ����߿���ʾƷ�ֺ�װ����Ϣ
 */

public class Qicjjd extends BasePage {
	// private static final String PRINT_MOR = "PRINT_MOR";

	private static final String PRINT_BAOER = "PRINT_BAOER";

	private static final String PRINT_HUXIAN = "PRINT_HUXIAN";

	private static final String PRINT_GUIGUAN = "PRINT_GUIGUAN";
	
	private static final String PRINT_LUBEI = "PRINT_LUBEI";

	private static final String PRINT_HBW = "PRINT_HBW";
	
	private static final String PRINT_WEIHE = "PRINT_WEIHE";

	private static final String PRINT_BEIZ = "PRINT_BEIZ"; //�ϵļ�ﵥ��ʽ��û��ú����ֻ�б�ע��������

	// �����û���ʾ
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

	/*
	 * ����:ͯ�Ҹ� ʱ��:2009-4-13 �޸�����:����һ������ʱ�糧id�Ĺ���,��� ���ذ�ť
	 * 
	 */
	private String filterDcid(Visit v) {

		String sqltmp = " (" + v.getDiancxxb_id() + ")";
		if (v.isFencb()) {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList("select id from diancxxb where fuid="
							+ v.getDiancxxb_id());
			sqltmp = "";
			while (rsl.next()) {
				sqltmp += "," + rsl.getString("id");
			}
			sqltmp = "(" + sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}

	// ��ȡ��ѯ���

	public String getBaseSql(String chepid) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		String strbz = "c.beiz";
		sql
				.append("select zhi from xitxxb where mingc = '�������ﵥ��ע��Ϣ' and diancxxb_id = "
						+ visit.getDiancxxb_id() + " and zhuangt=1");
		ResultSetList rsl = con.getResultSetList(sql.toString());
		while (rsl.next()) {
			strbz = rsl.getString("zhi");
		}
		rsl.close();

		String diancid = visit.getDiancxxb_id() + "";
		String sql1 = " select * from xitxxb where mingc='չʾ�����Ա�Ͳ���ʱ��' and leib='����'and zhuangt=1 and zhi='��' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSetList rt = con.getResultSetList(sql1);
		String pas_sql = " C.ZHONGCJJY||'/'||C.QINGCJJY JIANJY,  \n";
		boolean flag = false;
		if (rt.next()) {
			flag = true;
			pas_sql = " c.lury||jianjrizcz(c.id,'caozy','chepb'," + diancid
					+ ") JIANJY, \n";
		}

		rt.close();

		StringBuffer sbsql = new StringBuffer();
		sbsql
				.append("SELECT  G.MINGC GONGYSB_ID, M.MINGC MEIKXXB_ID, RY.QUANC RENYXXB_ID,P.MINGC PINZB_ID, \n");
		sbsql
				.append("        C.CHEPH, C.PIAOJH, C.MAOZ, C.PIZ, C.BIAOZ,c.xuh, \n");
		sbsql
				.append("        (C.MAOZ-C.PIZ-C.ZONGKD) JINGZ,c.koud,c.kous,c.kouz, C.ZONGKD,i.mingc zhuangcdw_item_id, \n");
		//		sbsql.append("        C.ZHONGCJJY||'/'||C.QINGCJJY JIANJY, \n");
		sbsql.append(pas_sql);
		sbsql
				.append("        TO_CHAR(C.QINGCSJ,'YYYY-MM-DD HH24:MI:SS') QINGCSJ, \n");
		sbsql
				.append("        TO_CHAR(C.ZHONGCSJ,'YYYY-MM-DD HH24:MI:SS') ZHONGCSJ,ys.mingc,mc.mingc as meic,ht.hetbh,ht.gongfdwmc,xc.mingc as xiecfs, \n");
		sbsql.append(strbz + " beiz \n");
		sbsql
				.append("        FROM FAHB F, CHEPB C, GONGYSB G, MEIKXXB M, RENYXXB RY,PINZB P,YUNSDWB YS,meicb mc,hetb ht,xiecfsb xc,item i \n");
		sbsql.append("WHERE F.ID = C.FAHB_ID AND F.GONGYSB_ID = G.ID  \n");
		sbsql.append("AND F.MEIKXXB_ID = M.ID AND F.PINZB_ID = P.ID   \n");
		sbsql
				.append("and C.YUNSDWB_ID=YS.id(+) and c.meicb_id = mc.id(+) and c.renyxxb_id=ry.id(+) and f.hetb_id = ht.id(+) and c.xiecfsb_id = xc.id(+) and c.zhuangcdw_item_id=i.id(+) \n");
		sbsql.append("AND C.ID=");
		sbsql.append(chepid);
		// sbsql.append(" AND F.DIANCXXB_ID=");
		// sbsql.append(visit.getDiancxxb_id());

		//		sbsql.append(" AND F.DIANCXXB_ID = ");
		//		sbsql.append(visit.getString13());

		sbsql.append(" AND F.YUNSFSB_ID =");
		sbsql.append(SysConstant.YUNSFS_QIY);
		return sbsql.toString();
	}

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getString1() == null || "".equals(visit.getString1())) {
			return "��������ȷ";
		}
		ResultSetList rsl = con
				.getResultSetList(getBaseSql(visit.getString1()));
		if (rsl == null) {
			setMsg("���ݻ�ȡʧ�ܣ�������������״����������� XXCX-001");
			return "";
		}
		int fontSize = 9;
		String SQL = "select zhi from xitxxb where leib = '����' "
				+ " and mingc = '������ﵥ�����ֺ�' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		if (rs.next()) {
			fontSize = rs.getInt("zhi");
		}
		rs.close();
		if (PRINT_BAOER.equals(visit.getString15())) {
			String data[][] = new String[12][1];
			Visit v = (Visit) this.getPage().getVisit();
			data[0][0] = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ v.getDiancmc();
			data[1][0] = Locale.xuh_chepb;
			data[2][0] = Locale.cheph_chepb;
			data[3][0] = Locale.ched_chepb;
			data[4][0] = Locale.kuangb_chepb;
			data[5][0] = Locale.zhongcsj_chepb;
			data[6][0] = Locale.qingcsj_chepb;
			data[7][0] = Locale.maoz_chepb;
			data[8][0] = Locale.piz_chepb;
			data[9][0] = Locale.koud_chepb;
			data[10][0] = Locale.jingz_chepb;
			data[11][0] = "���Ա:" + v.getRenymc();
			if (rsl.next()) {
				data[1][0] = data[1][0] + ":" + rsl.getString("xuh");
				data[2][0] = data[2][0] + ":" + rsl.getString("cheph");
				data[3][0] = data[3][0] + ":" + rsl.getString("mingc");
				data[4][0] = data[4][0] + ":" + rsl.getString("MEIKXXB_ID");
				data[5][0] = data[5][0] + ":" + rsl.getString("ZHONGCSJ");
				data[6][0] = data[6][0] + ":" + rsl.getString("QINGCSJ");
				data[7][0] = data[7][0] + ":" + rsl.getString("MAOZ");
				data[8][0] = data[8][0] + ":" + rsl.getString("PIZ");
				data[9][0] = data[9][0] + ":" + rsl.getString("ZONGKD");
				data[10][0] = data[10][0] + ":" + rsl.getString("JINGZ");
			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 400 };

			// rt.setTitle("��������ؼ�¼��", ArrWidth);
			// rt.title.fontSize=10;
			// rt.title.setRowHeight(2, 40);
			// rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			// rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);

			rt.setBody(new Table(data, 0, 0, 0));
			rt.body.setWidth(ArrWidth);
			rt.body.setBorderNone();
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_BOTTOM, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_LEFT, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_RIGHT, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_TOP, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_FONTSIZE, 12);
			rt.body.setColAlign(1, Table.ALIGN_LEFT);

			rt.setPageRows(1);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			// rt.body.setRowHeight(30);
			rt.body.setRowHeight(25);
			return rt.getAllPagesHtml();// ph;
		} else if (PRINT_LUBEI.equals(visit.getString15())) {

			String jianjsj = "";
			String fahdw = "";
			String jianjy = "";
			String shoulr = ""; //������
			Date date = new Date();
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			int day	= DateUtil.getDay(date);
			String data[][] = new String[6][8];
//			data[0][0] = Locale.piaojh_chepb;
//			data[1][0] = Locale.maoz_chepb;
//			data[2][0] = Locale.piz_chepb;
//			data[3][0] = Locale.jingz_chepb;
//			data[0][2] = Locale.xuh_chepb;
//			data[1][2] = Locale.ched_chepb;
//			data[2][2] = Locale.koud_chepb;
//			data[3][2] = Locale.beiz_chepb;
//			data[0][4] = Locale.cheph_chepb;
//			data[2][4] = Locale.biaoz_chepb;
			if (rsl.next()) {
				data[0][2] = ""+year;
				data[0][3] = ""+month;
				data[0][4] = ""+day;
				data[0][6] = rsl.getString("PIAOJH");
				data[1][1] = rsl.getString("GONGYSB_ID");
				data[1][3] = "����³�����繫˾";
				data[1][6] = rsl.getString("cheph");
				data[2][3] = rsl.getString("ZHONGCSJ");
				data[2][6] = rsl.getString("QINGCSJ");
				data[3][0] = rsl.getString("PINZB_ID");
				data[3][3] = "��";
				data[3][4] = rsl.getString("BIAOZ");
				data[3][5] = rsl.getString("MAOZ");
				data[3][6] = rsl.getString("PIZ");
				data[3][7] = rsl.getString("JINGZ");
				shoulr = rsl.getString("RENYXXB_ID");
				jianjy = rsl.getString("JIANJY");
			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 80 ,130,90,50,80,80,65,65};

			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, jianjy, Table.ALIGN_RIGHT);
			rt.setDefautlFooter(5, 2, shoulr, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize = 10;

			rt.setBody(new Table(data, 0, 0, 0));
			// rt.body.setWidth(ArrWidth);
			
			rt.body.setColWidth(1, 80);
			rt.body.setColWidth(2, 130);
			rt.body.setColWidth(3, 90);
			rt.body.setColWidth(4, 50);
			rt.body.setColWidth(5, 80);
			rt.body.setColWidth(6, 80);
			rt.body.setColWidth(7, 65);
			rt.body.setColWidth(8, 65);

			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_CENTER);
			rt.body.setColAlign(7, Table.ALIGN_CENTER);
			rt.body.setColAlign(8, Table.ALIGN_CENTER);

			rt.body.mergeCell(1, 7, 1, 8);
			rt.body.mergeCell(2, 4, 2, 5);
			rt.body.mergeCell(2, 7, 2, 8);
			rt.body.mergeCell(2, 4, 2, 5);
			rt.body.mergeCell(3, 4, 3, 5);
			rt.body.mergeCell(3, 7, 3, 8);
			rt.body.mergeCell(4, 1, 4, 2);
			rt.body.mergeCell(5, 1, 5, 2);
			rt.body.mergeCell(6, 2, 6, 8);
			rt.body.setBorderNone(); 
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_BOTTOM, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_LEFT, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_RIGHT, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_TOP, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_FONTSIZE, 12);
			rt.setPageRows(1);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			rt.body.setFontSize(fontSize);
			return rt.getAllPagesHtml();// ph;
		} else if (PRINT_GUIGUAN.equals(visit.getString15())) {
			String gonghdw = "";
			String kuangd = "";
			String jily = "";
			String xiehdd = "";
			String chellx = "";
			String guohsj = "";
			String hetbh = "";
			String hetdw = "";
			String piaojh = "";
			String data[][] = new String[3][9];
			data[0][0] = "����";//Locale.cheh_chepb;
			data[0][1] = "��������<br>";//Locale.huowmc_chepb;
			data[0][2] = "ë��<br>(��)"; //Locale.maoz_chepb;
			data[0][3] = "��������<br>(��)"; //Locale.chelzz_chepb;
			data[0][4] = "����<br>(��)"; //Locale.huowsz_chepb;
			data[0][5] = "���ʯ<br>(��)"; //Locale.kouds_chepb;
			data[0][6] = "��ˮ��<br>(��)"; //Locale.kousf_chepb;
			data[0][7] = "����<br>(��)"; //Locale.koun_chepb;
			data[0][8] = "������<br>(��)"; //Locale.jingz_chepb;
			data[2][0] = "��ע"; //Locale.beiz_chepb;
			if (rsl.next()) {
				data[1][0] = rsl.getString("CHEPH");
				data[1][1] = rsl.getString("PINZB_ID");
				data[1][2] = rsl.getString("MAOZ");
				data[1][3] = rsl.getString("PIZ");
				data[1][4] = String.valueOf(CustomMaths.sub(rsl
						.getDouble("maoz"), rsl.getDouble("piz")));
				data[1][5] = rsl.getString("koud");
				data[1][6] = rsl.getString("kous");
				data[1][7] = rsl.getString("kouz");
				data[1][8] = rsl.getString("jingz");
				guohsj = rsl.getString("QINGCSJ");
				kuangd = rsl.getString("MEIKXXB_ID");
				gonghdw = rsl.getString("GONGYSB_ID");
				xiehdd = rsl.getString("meic");
				jily = rsl.getString("JIANJY");
				hetbh = rsl.getString("hetbh");
				hetdw = rsl.getString("gongfdwmc");
				chellx = rsl.getString("xiecfs");
				piaojh = rsl.getString("piaojh");
				//					data[0][0] = rsl.getString("����");
				//					data[0][2] = rsl.getString("ë��");
				//				    data[0][3] = rsl.getString("��������");
				//				    data[0][4] = rsl.getString("����ʵ��");
				//				    data[0][5] = rsl.getString("��ʯ");
				//					data[0][6] = rsl.getString("����");
				//					data[0][7] = rsl.getString("�۶�");
				//					data[0][8] = rsl.getString("����");
				//					data[2][0] = rsl.getString("��ע");
				//						gonghdw = rsl.getString("GONGHDW_ID");
				//					    jily = rsl.getString("JILY");

			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 150, 150, 150, 70, 70, 110 };

			rt.createTitle(5, ArrWidth);
			rt
					.setTitle(
							"<span style=\"text-decoration: underline\">���ƹ�ں�ɽ�������޹�˾�������ⵥ</span>",
							2);
			setTitle(rt.title, "No.:" + piaojh, 4, 6, 3, Table.ALIGN_RIGHT);
			setTitle(rt.title, "��ú��λ:" + gonghdw, 1, 2, 4, Table.ALIGN_LEFT);
			setTitle(rt.title, "���:" + kuangd, 3, 4, 4, Table.ALIGN_LEFT);
			setTitle(rt.title, "ж�����ͣ�" + chellx, 5, 6, 4, Table.ALIGN_LEFT);
			setTitle(rt.title, "��ͬ��ţ�" + hetbh, 1, 2, 5, Table.ALIGN_LEFT);
			setTitle(rt.title, "��ͬ��λ:" + hetdw, 3, 4, 5, Table.ALIGN_LEFT);
			setTitle(rt.title, "ж���ص�:" + xiehdd, 5, 6, 5, Table.ALIGN_LEFT);
			rt.title.fontSize = 10;
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 14);
			/*
			 rt.setTitle("���ƹ�ں�ɽ�������޹�˾�������ⵥ", ArrWidth);
			 rt.title.fontSize = 10;
			 rt.title.setRowHeight(2, 40);
			 rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			 rt.title.setRowCells(2, Table.PER_FONTSIZE, 14);
			 
			 
			 String title_str="���ƹ�ں�ɽ�������޹�˾����������";
			 String[][] header=new String[][]{{title_str,title_str,title_str,title_str,title_str,title_str},{"","","","","",""},{"��ú��λ:","��ú��λ:","���:","���:","��������:","��������:"},{"��ͬ���:","��ͬ���:","��ͬ��λ:","��ͬ��λ:","ж���ص�:","ж���ص�:"}};
			 Table td=new Table(header,0,0,0);
			 
			 td.setWidth(ArrWidth);
			 td.merge(1,1,1,6);
			 td.merge(2,1,2,6);
			 td.merge(3,1,3,2);
			 td.merge(3,3,3,4);
			 td.merge(3,5,3,6);
			 td.merge(4,1,4,2);
			 td.merge(4,3,4,4);
			 td.merge(4,5,4,6);
			 
			 
			 td.setCellFont(1, 1, "����", 14, true);
			 
			 rt.title=td;*/
			//					rt.setDefaultTitle(1, 3, "������λ��"  + gonghdw, Table.ALIGN_LEFT);
			//					rt.setDefaultTitle(4, 2, "��㣺"  + kuangd, Table.ALIGN_LEFT);
			//					rt.setDefaultTitle(1, 8, "�������ͣ�"  + chellx, Table.ALIGN_LEFT);
			//					rt.setDefaultTitle(5, 2, "��λ����", + Table.ALIGN_RIGHT);
			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, "����ʱ�䣺" + guohsj, Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "����Ա��" + jily, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize = 10;

			rt.setBody(new Table(data, 0, 0, 0));
			// rt.body.setWidth(ArrWidth);
			// rt.body.setBorderNone();
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_BOTTOM, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_LEFT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_RIGHT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_TOP , 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_FONTSIZE , 12);

			rt.body.setRowHeight(1, 54);
			rt.body.setRowHeight(2, 42);
			rt.body.setRowHeight(3, 39);

			rt.body.setColWidth(1, 100);
			rt.body.setColWidth(2, 75);
			rt.body.setColWidth(3, 75);
			rt.body.setColWidth(4, 75);
			rt.body.setColWidth(5, 75);
			rt.body.setColWidth(6, 75);
			rt.body.setColWidth(7, 75);
			rt.body.setColWidth(8, 75);
			rt.body.setColWidth(9, 75);

			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_CENTER);
			rt.body.setColAlign(7, Table.ALIGN_CENTER);
			rt.body.setColAlign(8, Table.ALIGN_CENTER);
			rt.body.setColAlign(9, Table.ALIGN_CENTER);

			//					rt.body.mergeCell(2, 4, 2, 6);
			rt.body.mergeCell(3, 2, 3, 9);
			rt.setPageRows(1);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			//rt.body.setRowHeight(30);
			return rt.getAllPagesHtml();// ph;
		} else if (PRINT_HUXIAN.equals(visit.getString15())) {

			String jianjsj = "";
			String fahdw = "";
			String jianjy = "";
			String data[][] = new String[4][6];
			data[0][0] = Locale.piaojh_chepb;
			data[1][0] = Locale.maoz_chepb;
			data[2][0] = Locale.piz_chepb;
			data[3][0] = Locale.jingz_chepb;
			data[0][2] = Locale.xuh_chepb;
			data[1][2] = Locale.ched_chepb;
			data[2][2] = Locale.koud_chepb;
			data[3][2] = Locale.beiz_chepb;
			data[0][4] = Locale.cheph_chepb;
			data[2][4] = Locale.biaoz_chepb;
			if (rsl.next()) {
				data[0][1] = rsl.getString("piaojh");
				data[1][1] = rsl.getString("maoz");
				data[2][1] = rsl.getString("piz");
				data[3][1] = rsl.getString("jingz");
				data[0][3] = rsl.getString("xuh");
				data[1][3] = rsl.getString("mingc");
				data[2][3] = rsl.getString("zongkd");
				data[3][3] = rsl.getString("beiz");
				data[0][5] = rsl.getString("cheph");
				data[2][5] = rsl.getString("biaoz");
				jianjsj = rsl.getString("ZHONGCSJ");
				fahdw = rsl.getString("GONGYSB_ID");
				jianjy = rsl.getString("JIANJY");
			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 100, 140, 100, 100, 100, 100 };

			rt.setTitle("��������ؼ�¼��", ArrWidth);
			rt.title.fontSize = 10;
			rt.title.setRowHeight(2, 40);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);

			rt.setDefaultTitle(1, 3, "������λ��" + fahdw, Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 2, "��λ����", Table.ALIGN_RIGHT);

			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, "���ʱ�䣺" + jianjsj, Table.ALIGN_LEFT);
			rt.setDefautlFooter(5, 2, "���Ա��" + jianjy, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize = 10;

			rt.setBody(new Table(data, 0, 0, 0));
			// rt.body.setWidth(ArrWidth);
			// rt.body.setBorderNone();
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_BOTTOM, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_LEFT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_RIGHT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_TOP , 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_FONTSIZE , 12);

			rt.body.setColWidth(1, 100);
			rt.body.setColWidth(2, 140);
			rt.body.setColWidth(3, 100);
			rt.body.setColWidth(4, 100);
			rt.body.setColWidth(5, 100);
			rt.body.setColWidth(6, 100);

			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_CENTER);

			rt.body.mergeCell(2, 4, 2, 6);
			rt.body.mergeCell(4, 4, 4, 6);
			rt.setPageRows(1);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			rt.body.setFontSize(fontSize);
			return rt.getAllPagesHtml();// ph;
		} else if (PRINT_HBW.equals(visit.getString15())) {

			String data[][] = new String[14][1];
			Visit v = (Visit) this.getPage().getVisit();
			data[0][0] = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ v.getDiancmc();
			data[1][0] = Locale.xuh_chepb;
			data[2][0] = Locale.cheph_chepb;
			data[3][0] = Locale.ched_chepb;
			data[4][0] = Locale.kuangb_chepb;
			data[5][0] = Locale.zhongcsj_chepb;
			data[6][0] = Locale.qingcsj_chepb;
			data[7][0] = Locale.maoz_chepb;
			data[8][0] = Locale.piz_chepb;
			data[9][0] = Locale.koud_chepb;
			data[10][0] = Locale.jingz_chepb;
			data[11][0] = Locale.pinzb_id_fahb;
			data[12][0] = "װ����Ϣ";
			data[13][0] = "���Ա:" + v.getRenymc();
			if (rsl.next()) {
				data[1][0] = data[1][0] + ":" + rsl.getString("xuh");
				data[2][0] = data[2][0] + ":" + rsl.getString("cheph");
				data[3][0] = data[3][0] + ":" + rsl.getString("mingc");
				data[4][0] = data[4][0] + ":" + rsl.getString("MEIKXXB_ID");
				data[5][0] = data[5][0] + ":" + rsl.getString("ZHONGCSJ");
				data[6][0] = data[6][0] + ":" + rsl.getString("QINGCSJ");
				data[7][0] = data[7][0] + ":" + rsl.getString("MAOZ");
				data[8][0] = data[8][0] + ":" + rsl.getString("PIZ");
				data[9][0] = data[9][0] + ":" + rsl.getString("ZONGKD");
				data[10][0] = data[10][0] + ":" + rsl.getString("JINGZ");
				data[11][0] = data[11][0] + ":" + rsl.getString("pinzb_id");
				data[12][0] = data[12][0] + ":"
						+ rsl.getString("zhuangcdw_item_id");
			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 200 };

			// rt.setTitle("��������ؼ�¼��", ArrWidth);
			// rt.title.fontSize=10;
			// rt.title.setRowHeight(2, 40);
			// rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			// rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);

			rt.setBody(new Table(data, 0, 0, 0));
			rt.body.setWidth(ArrWidth);
			//rt.body.setBorderNone();
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_BOTTOM, 1);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_LEFT, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_RIGHT, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_BORDER_TOP, 0);
			rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
					Table.PER_FONTSIZE, 12);
			rt.body
					.setCells(6, 1, 7, rt.body.getCols(), Table.PER_FONTSIZE,
							10);

			rt.body.setColAlign(1, Table.ALIGN_LEFT);

			rt.setPageRows(1);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			// rt.body.setRowHeight(30);
			rt.body.setRowHeight(25);
			return rt.getAllPagesHtml();// ph;

		} else if (PRINT_BEIZ.equals(visit.getString15())) {

			String zhongcsj = "";
			String qingcsj = "";
			String fahdw = "";
			String jianjy = "";
			String meikxx = "";
			String data[][] = new String[4][6];
			data[0][0] = Locale.piaojh_chepb;
			data[1][0] = Locale.maoz_chepb;
			data[2][0] = Locale.piz_chepb;
			data[3][0] = Locale.jingz_chepb;
			data[0][2] = Locale.xuh_chepb;
			data[1][2] = Locale.ched_chepb;
			data[2][2] = Locale.koud_chepb;
			data[3][2] = Locale.beiz_chepb;
			data[0][4] = Locale.cheph_chepb;
			data[1][4] = Locale.beiz_chepb;
			data[2][4] = Locale.biaoz_chepb;
			if (rsl.next()) {
				data[0][1] = rsl.getString("piaojh");
				data[1][1] = rsl.getString("maoz");
				data[2][1] = rsl.getString("piz");
				data[3][1] = rsl.getString("jingz");
				data[0][3] = rsl.getString("xuh");
				data[1][3] = rsl.getString("mingc");
				data[2][3] = rsl.getString("zongkd");
				data[3][3] = rsl.getString("meic");
				data[0][5] = rsl.getString("beiz");
				data[1][5] = rsl.getString("beiz");
				data[2][5] = rsl.getString("biaoz");
				zhongcsj = rsl.getString("ZHONGCSJ");
				qingcsj = rsl.getString("qingcsj");
				fahdw = rsl.getString("GONGYSB_ID");
				meikxx = rsl.getString("MEIKXXB_ID");
				jianjy = rsl.getString("JIANJY");
			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 100, 140, 100, 100, 100, 100 };

			rt.setTitle(visit.getDiancmc() + "<p>��������ؼ�¼��", ArrWidth);
			rt.title.fontSize = 13;
			rt.title.setRowHeight(2, 40);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 17);

			rt.setDefaultTitle(1, 2, "������λ��" + fahdw, Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, "ú��" + meikxx, Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 2, "��λ����", Table.ALIGN_RIGHT);

			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, "�س�ʱ�䣺" + zhongcsj, Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "�ᳵʱ�䣺" + qingcsj, Table.ALIGN_LEFT);
			rt.setDefautlFooter(5, 2, "���Ա��" + jianjy, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize = 10;

			rt.setBody(new Table(data, 0, 0, 0));
			// rt.body.setWidth(ArrWidth);
			// rt.body.setBorderNone();
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_BOTTOM, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_LEFT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_RIGHT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_TOP , 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_FONTSIZE , 12);

			rt.body.setColWidth(1, 100);
			rt.body.setColWidth(2, 140);
			rt.body.setColWidth(3, 100);
			rt.body.setColWidth(4, 100);
			rt.body.setColWidth(5, 100);
			rt.body.setColWidth(6, 100);
			rt.body.setFontSize(13);

			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_CENTER);

			rt.body.mergeCell(2, 4, 2, 6);
			rt.body.mergeCell(4, 4, 4, 6);
			rt.setPageRows(1);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			rsl.close();
			return rt.getAllPagesHtml();// ph;
		}else if(PRINT_WEIHE.equals(visit.getString15())){
			String gonghdw = "";
			String kuangd = "";
			String jily = "";
			String xiehdd = "";
			String chellx = "";
			String guohsj = "";
			String hetbh = "";
			String hetdw = "";
			String piaojh = "";
			String data[][] = new String[3][9];
			data[0][0] = "����";//Locale.cheh_chepb;
			data[0][1] = "��������<br>";//Locale.huowmc_chepb;
			data[0][2] = "ë��<br>(��)"; //Locale.maoz_chepb;
			data[0][3] = "��������<br>(��)"; //Locale.chelzz_chepb;
			data[0][4] = "����<br>(��)"; //Locale.huowsz_chepb;
			data[0][5] = "���ʯ<br>(��)"; //Locale.kouds_chepb;
			data[0][6] = "��ˮ��<br>(��)"; //Locale.kousf_chepb;
			data[0][7] = "����<br>(��)"; //Locale.koun_chepb;
			data[0][8] = "����<br>(��)"; //Locale.jingz_chepb;
			data[2][0] = "��ע"; //Locale.beiz_chepb;
			if (rsl.next()) {
				data[1][0] = rsl.getString("CHEPH");
				data[1][1] = rsl.getString("PINZB_ID");
				data[1][2] = rsl.getString("MAOZ");
				data[1][3] = rsl.getString("PIZ");
				data[1][4] = rsl.getString("jingz");
				data[1][5] = rsl.getString("koud");
				data[1][6] = rsl.getString("kous");
				data[1][7] = rsl.getString("kouz");
				data[1][8] = rsl.getString("biaoz");
				data[2][1] = rsl.getString("beiz");
				guohsj = rsl.getString("QINGCSJ");
				kuangd = rsl.getString("MEIKXXB_ID");
				gonghdw = rsl.getString("GONGYSB_ID");
				xiehdd = rsl.getString("meic");
				jily = rsl.getString("JIANJY");
				hetbh = rsl.getString("hetbh");
				hetdw = rsl.getString("mingc");
				chellx = rsl.getString("xiecfs");
				piaojh = rsl.getString("piaojh");

			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 150, 150, 150, 70, 70, 110 };

			rt.createTitle(5, ArrWidth);
			rt
					.setTitle(
							"<p><font size=5><span style=\"text-decoration: underline\">"+visit.getDiancqc()+"��������ؼ�¼��</span></font>",
							2);
			setTitle(rt.title, "No.:" + piaojh, 4, 6, 3, Table.ALIGN_RIGHT);
			setTitle(rt.title, "��ú��λ:" + gonghdw, 1, 2, 4, Table.ALIGN_LEFT);
			setTitle(rt.title, "���:" + kuangd, 3, 4, 4, Table.ALIGN_LEFT);
			setTitle(rt.title, "ж�����ͣ�" + chellx, 5, 6, 4, Table.ALIGN_LEFT);
			setTitle(rt.title, "��ͬ��ţ�" + hetbh, 1, 2, 5, Table.ALIGN_LEFT);
			setTitle(rt.title, "���˵�λ:" + hetdw, 3, 4, 5, Table.ALIGN_LEFT);
			setTitle(rt.title, "ж���ص�:" + xiehdd, 5, 6, 5, Table.ALIGN_LEFT);
			rt.title.fontSize = 15;
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 15);
			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, "����ʱ�䣺" + guohsj, Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 4, "����Ա��" + jily, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize = 13;

			rt.setBody(new Table(data, 0, 0, 0));

			rt.body.setRowHeight(1, 54);
			rt.body.setRowHeight(2, 42);
			rt.body.setRowHeight(3, 39);

			rt.body.setColWidth(1, 100);
			rt.body.setColWidth(2, 75);
			rt.body.setColWidth(3, 75);
			rt.body.setColWidth(4, 75);
			rt.body.setColWidth(5, 75);
			rt.body.setColWidth(6, 75);
			rt.body.setColWidth(7, 75);
			rt.body.setColWidth(8, 75);
			rt.body.setColWidth(9, 75);

			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_CENTER);
			rt.body.setColAlign(7, Table.ALIGN_CENTER);
			rt.body.setColAlign(8, Table.ALIGN_CENTER);
			rt.body.setColAlign(9, Table.ALIGN_CENTER);

			//					rt.body.mergeCell(2, 4, 2, 6);
			rt.body.mergeCell(3, 2, 3, 9);
			rt.setPageRows(1);
			rt.body.setFontSize(13);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			//rt.body.setRowHeight(30);
			return rt.getAllPagesHtml();// ph;
		
			
		}
		/**
		 * huochaoyuan
		 * 2009-11-10Ӧ�糧Ҫ����泵��ﵥ��ʽ�����޸ģ��޸�ҳ�������С�����ú����ȥ����ע
		 */
		else {
			String zhongcsj = "";
			String qingcsj = "";
			String fahdw = "";
			String jianjy = "";
			String meikxx = "";
			String data[][] = new String[4][6];
			data[0][0] = Locale.piaojh_chepb;
			data[1][0] = Locale.maoz_chepb;
			data[2][0] = Locale.piz_chepb;
			data[3][0] = Locale.jingz_chepb;
			data[0][2] = Locale.xuh_chepb;
			data[1][2] = Locale.ched_chepb;
			data[2][2] = Locale.koud_chepb;
			data[3][2] = "ú��";
			data[0][4] = Locale.cheph_chepb;
			data[1][4] = Locale.beiz_chepb;
			data[2][4] = Locale.biaoz_chepb;
			if (rsl.next()) {
				data[0][1] = rsl.getString("piaojh");
				data[1][1] = rsl.getString("maoz");
				data[2][1] = rsl.getString("piz");
				data[3][1] = rsl.getString("jingz");
				data[0][3] = rsl.getString("xuh");
				data[1][3] = rsl.getString("mingc");
				data[2][3] = rsl.getString("zongkd");
				data[3][3] = rsl.getString("meic");
				data[0][5] = rsl.getString("cheph");
				data[1][5] = rsl.getString("beiz");
				data[2][5] = rsl.getString("biaoz");
				zhongcsj = rsl.getString("ZHONGCSJ");
				qingcsj = rsl.getString("qingcsj");
				fahdw = rsl.getString("GONGYSB_ID");
				meikxx = rsl.getString("MEIKXXB_ID");
				jianjy = rsl.getString("JIANJY");
			}
			Report rt = new Report();
			int[] ArrWidth = new int[] { 100, 140, 100, 100, 100, 100 };

			rt.setTitle(visit.getDiancmc() + "<p>��������ؼ�¼��", ArrWidth);
			rt.title.fontSize = 13;
			rt.title.setRowHeight(2, 40);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 17);

			rt.setDefaultTitle(1, 2, "������λ��" + fahdw, Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, "ú��" + meikxx, Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 2, "��λ����", Table.ALIGN_RIGHT);

			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 2, "�س�ʱ�䣺" + zhongcsj, Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "�ᳵʱ�䣺" + qingcsj, Table.ALIGN_LEFT);
			rt.setDefautlFooter(5, 2, "���Ա��" + jianjy, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.footer.fontSize = 10;

			rt.setBody(new Table(data, 0, 0, 0));
			// rt.body.setWidth(ArrWidth);
			// rt.body.setBorderNone();
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_BOTTOM, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_LEFT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_RIGHT, 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_BORDER_TOP , 0);
			// rt.body.setCells(1, 1, rt.body.getRows(), rt.body.getCols(),
			// Table.PER_FONTSIZE , 12);

			rt.body.setColWidth(1, 100);
			rt.body.setColWidth(2, 140);
			rt.body.setColWidth(3, 100);
			rt.body.setColWidth(4, 100);
			rt.body.setColWidth(5, 100);
			rt.body.setColWidth(6, 100);
			rt.body.setFontSize(13);

			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_CENTER);
			rt.body.setColAlign(5, Table.ALIGN_CENTER);
			rt.body.setColAlign(6, Table.ALIGN_CENTER);

			rt.body.mergeCell(2, 4, 2, 6);
			rt.body.mergeCell(4, 4, 4, 6);
			rt.setPageRows(1);
			con.Close();
			if (rt.body.getPages() > 0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			rsl.close();
			return rt.getAllPagesHtml();// ph;
		}
	}

	public void setTitle(Table title, String strTitle, int iStartCol,
			int iEndCol, int iRow, int iAlign) {
		title.setCellValue(iRow, iStartCol, strTitle);
		title.setCellAlign(iRow, iStartCol, iAlign);
		// title.setCellAlign(2,2,Table.ALIGN_CENTER);
		title.setCellFont(iRow, iStartCol, "", 13, false);
		title.mergeCell(iRow, iStartCol, iRow, iEndCol);
		//		title.merge(iRow, iEndCol + 1, iRow, title.getCols());
	}

	public void getSelectData() {
		// Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		ToolbarButton rbtn = new ToolbarButton(null, "����",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Return);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
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
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		return getToolbar().getRenderScript();
	}

	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setString10(visit.getActivePageName());
			//visit.setActivePageName(getPageName().toString());
			setTbmsg(null);
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