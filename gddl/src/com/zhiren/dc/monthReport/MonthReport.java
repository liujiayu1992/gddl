package com.zhiren.dc.monthReport;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterFac_dt;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * 2009-04-20
 * ����
 * 01�� ������Ȼú�� ���㲻��ȷ Ӧʹ��gongrl ԭʹ�� fadl
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-04 15:25
 * �������޸�01 ���м��㹫ʽ
 * 		�����׼ú��(24��) = ָ���.����ú�۱�ú�� + ָ���.�������۱�ú�� + ָ���.�������۱�ú��
 * 	  	���ȱ�׼ú��(25��) = ָ���.����ú�۱�ú�� + ָ���.�������۱�ú�� + ָ���.�������۱�ú��
 * 		�ۺ�ȼ�Ϸ�����(26��) ȼú������(27��) ��ʽ�ж�Ӧ����������ϵ�����޸�
 * 		 
 */
/*
 * ���ߣ����پ�
 * ʱ�䣺2009-07-20 14��30
 * �������޸�08 ���ۼ�ֵ����
 * 		 �ۼ�ֵ��������ɼ�Ȩ�����ģ��ָĳ��ڼ��㡰�ܼơ���ú����ʱ�ӱ����ܼ������õ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-16 9��47
 * �������Ķ��ϱ��±�ʱɾ�����ݲ����ֳ��������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-17 
 * �������޸��±������޸�Ϊʵʱ�ϴ����ֹ�˾
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-01
 * �������޸��±�04��������������ȡƱ��Ĭ�ϼ�����100%
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-03
 * �������޸ĵ�ȼ01���������ȡ��jizb sum(jizurl)
 */
/*
 * ���ߣ�������
 * ʱ�䣺2009-11-04
 * �������޸��ϴ����ƹ�����ָ���������ı�����getRenwmc(),zhibwcqkb�ĳ�zhibwcqkyb
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-09
 * ������03��Ĺ������������Ϊ�������ʹ����ʶ�Ϊ0
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-01-15
 * �������޸�01����������ԼС��λΪ����3λ
 */
/*
 * ���ߣ����پ�
 * ʱ�䣺2010-05-05
 * �������޸�08-1���ۼƵ����ۺϼۡ���ú���ۡ�����˰��ú�����㷨��
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-05-30
 * �������޸�getDiaor16()���ڳ�����ۼ���Ϣ��ʾΪ����1���ڳ��ۼ���Ϣ
 */
public class MonthReport extends BasePage implements PageValidateListener {

	private static final String RT_SL = "yueslb";// ����

	private static final String RT_HC = "yuehcb";// �Ĵ�

	private static final String RT_ZL = "yuezlb";// ����

	private static final String RT_DR16 = "diaor16b";

	private static final String RT_DR01 = "diaor01b";

	private static final String RT_DR03 = "diaor03b";

	private static final String RT_DR04 = "diaor04b";

	private static final String RT_DR08 = "diaor08b";

	private static final String RT_DR08ZR = "diaor08bzr";

	private static final String RT_DR08_1 = "diaor08b-1";

	private static final String RT_DR08_1ZR = "diaor08b-1zr";
	
	private static final String RT_DR08_1CW = "diaor08cw";

	private static final String Yueqfmk = "Yueqfmk";

	private static final String SB_Yes = "y";	//�ϱ�
	
	private static final String SB_No = "n";	//���ϱ�

	private static final String RT_YUEZBWCQK = "yuezbwcqk";// ��ָ��������
	
	private static final String ITEM_ONE = "*"; //�ƻ��ھ��ϼ�ǰ��ӵķ��ţ����ת��Ϊ��д�������
	
	private static final String ITEM_TWO = "#"; //ͳ�䡢�ط�С��ǰ��ӵķ��ţ����ת��ΪСд�������

	private String _msg;

	private int _CurrentPage = -1;

	private int _AllPages = -1;

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	public boolean getRaw() {
		return true;
	}

	private boolean reportShowZero() {
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	// ***************������Ϣ��******************//
	// ҳ���ж�����
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

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}

	public String _miaos;

	public String getMiaos() {
		return _miaos;
	}

	public void setMiaos(String miaos) {
		_miaos = miaos;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// �õ���λȫ��
	public String getTianzdwQuanc(String dcid) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();
		ResultSetList rs = cn
				.getResultSetList(" select quanc from diancxxb where id="
						+ dcid);
		if (rs.next()) {
			_TianzdwQuanc = rs.getString("quanc");
		}
		rs.close();
		return _TianzdwQuanc;
	}
	
	private void convertItem(Table tb) {
		String tbCell = "";
		String compareCell = "default"; 
		int t = -1;
		int k = 0;
		int j = 0;
		
		for (int i = 1; i< tb.getRows()-1; i++) {
			tbCell = tb.getCellValue(i, 1);
			t = tbCell.indexOf(ITEM_ONE);
			if (t > -1) {
				//��ֹ�����ϲ�����ͬ�����ۼ����k
				if (!compareCell.equals(tbCell)) k++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, getDxValue(k) + "��" + tbCell.substring(t + 1));
				if (k > 1) j = 0;  //��������һ���ƻ��ھ�ʱ��j���㿪ʼ
			}
			t = tbCell.indexOf(ITEM_TWO);
			if (t > -1) {
				if (!compareCell.equals(tbCell)) j++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, j + "��" + tbCell.substring(t + 1));
			}
		}
	}
	
	public String getDxValue(int xuh) {
		String reXuh = "";
		String[] dx = { "", "һ", "��", "��", "��", "��", 
				"��", "��", "��", "��", "��", "ʮ" };
		String strXuh = String.valueOf(xuh);
		for (int i = 0; i < strXuh.length(); i++)
			reXuh = reXuh + dx[Integer.parseInt(strXuh.substring(i, i + 1))];

		return reXuh;
	}
	
	public String getPrintTable() {
		if (getReportType().equals(RT_SL)) {
			return getYueslb();
		} else if (getReportType().equals(RT_HC)) {
			return getYuehcb();
		} else if (getReportType().equals(RT_ZL)) {
			return getYuezlb();
		} else if (getReportType().equals(RT_DR01)) {
			return getDiaor01();
		} else if (getReportType().equals(RT_DR04)) {
			return getDiaor04();
		} else if (getReportType().equals(RT_DR16)) {
			return getDiaor16();
		} else if (getReportType().equals(RT_DR03)) {
			return getDiaor03();
		} else if (getReportType().equals(RT_DR08)) {
			return getDiaor08();
		} else if (getReportType().equals(RT_DR08ZR)) {
			return getDiaor08zr();
		} else if (getReportType().equals(RT_DR08_1)) { // �����
			return getDiaor08_1();
		}else if (getReportType().equals(RT_DR08_1CW)){  //����Ҫ�����±�
			return getDiaor08_1CW(); 
		}else if (getReportType().equals(RT_DR08_1ZR)) { // �����
			return getDiaor08_1zr();
		} else if (getReportType().equals(Yueqfmk)) { // �����
			return getyueqfmk();
		} else if (getReportType().equals(RT_YUEZBWCQK)) { // ��ָ��������
			return getYuezbwcqk();
		} else {
			return "�޴˱���";
		}
	}

	private String getZhibr() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhibr = "";
		String zhi = "��";
		String sql = "select zhi from xitxxb where mingc='�±������Ʊ����Ƿ�Ĭ�ϵ�ǰ�û�' and diancxxb_id="
				+ visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				zhi = rs.getString("zhi");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		if (zhi.equals("��")) {
			zhibr = visit.getRenymc();
		}
		return zhibr;
	}

	/*
	 * �޸��������������ͷΪLocale�б��� �޸�ʱ�䣺2008-12-05 �޸��ˣ�����
	 */
	private String getYueslb() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// �糧��Ϣ��id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		sbsql
				.append("select * from (select gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,yunsfsb.mingc as yunsfsb_id,fenx,\n");
		sbsql.append("jingz,biaoz,yingd,kuid,yuns,koud,kous,\n");
		sbsql
				.append(" kouz,koum,zongkd,sanfsl,jianjl,ructzl,yingdzje,kuidzje,suopsl,suopje from yuetjkjb tj,yueslb sl,gongysb,jihkjb,pinzb,yunsfsb\n");
		sbsql
				.append("  where tj.id=sl.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id \n");
		sbsql
				.append("  and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and diancxxb_id="
						+ lngDiancId
						+ " and riq=to_date('"
						+ strDate
						+ "','yyyy-mm-dd') order by sl.id )");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][] = new String[1][22];
		ArrHeader[0] = new String[] { Locale.gongysb_id_fahb,
				Locale.jihkjb_id_fahb, Locale.pinzb_id_fahb,
				Locale.yunsfsb_id_fahb, Locale.MRtp_fenx, Locale.laimsl_fahb,
				Locale.biaoz_fahb, Locale.yingd_fahb, Locale.kuid_fahb,
				Locale.yuns_fahb, Locale.koud_fahb, Locale.kous_fahb,
				Locale.kouz_fahb, Locale.koum_fahb, Locale.zongkd_fahb,
				Locale.sanfsl_fahb, Locale.MRtp_jianjl, Locale.MRtp_ructzl,
				Locale.MRtp_yingdzje, Locale.MRtp_kuidzje, Locale.MRtp_suopsl,
				Locale.MRtp_suopje };
		// �п�
		int ArrWidth[] = new int[] { 80, 60, 40, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50, 65, 65, 65, 50, 50 };
		// if(this.isA4parper()){
		// ArrWidth=new int[]
		// {70,30,20,50,50,50,50,30,30,30,50,50,50,50,50,50,50,50,50};
		// }
		//		
		// int e=0;
		// for(int i=0;i<ArrWidth.length;i++){
		// e+=ArrWidth[i];
		// }
		// System.out.println(e);
		// ����ҳ����
		rt.setTitle("������ͳ�Ʊ�", ArrWidth);
		rt.setDefaultTitle(9, 2, strMonth, Table.ALIGN_CENTER);

		// ����
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);

		// ҳ��
		// rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(6,1,"�Ʊ�:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(10,1,"���:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * �޸��ºĴ�������ͷΪLocale�б��� �޸�ʱ�䣺2008-12-05 �޸��ˣ�����
	 */
	private String getYuehcb() { // �Ĵ��
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// �糧��Ϣ��id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		sbsql
				.append("select gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,yunsfsb.mingc as yunsfsb_id,fenx,\n");
		sbsql.append("fady,gongry,qith,sunh,diaocl,panyk,kuc��\n");
		sbsql
				.append("  from yuetjkjb tj,yuehcb hc,gongysb,jihkjb,pinzb,yunsfsb\n");
		sbsql
				.append("  where tj.id=hc.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id \n");
		sbsql
				.append("  and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and diancxxb_id="
						+ lngDiancId
						+ " and riq=to_date('"
						+ strDate
						+ "','yyyy-mm-dd')");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { Locale.gongysb_id_fahb,
				Locale.jihkjb_id_fahb, Locale.pinzb_id_fahb,
				Locale.yunsfsb_id_fahb, Locale.MRtp_fenx, Locale.fady,
				Locale.gongry, Locale.qity, Locale.cuns, Locale.diaocl,
				Locale.panyk, Locale.kuc };
		// �п�
		int ArrWidth[] = new int[] { 80, 60, 50, 60, 60, 60, 60, 60, 60, 60,
				60, 60 };

		// ����ҳ����
		rt.setTitle("�ºĴ�ͳ�Ʊ�", ArrWidth);
		rt.setDefaultTitle(6, 2, strMonth, Table.ALIGN_CENTER);

		// ����
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);

		// ҳ��
		// rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(6,1,"�Ʊ�:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(10,1,"���:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getYuezlb() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// �糧��Ϣ��id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		sbsql
				.append("select yz.gongysb_id,yz.jihkjb_id,yz.pinzb_id,yz.yunsfsb_id,yz.fenx,yz.qnet_ar,yz.aar,yz.ad,yz.vdaf,yz.mt,yz.stad,yz.aad,yz.mad,yz.qbad,yz.had,yz.vad,yz.fcad,yz.std,yz.qbrad,");
		sbsql
				.append(" yz.hdaf,yz.qgrad_daf,yz.sdaf,yz.var,yz.qnet_ar_kf,yz.aar_kf,yz.ad_kf,yz.vdaf_kf,yz.mt_kf,yz.stad_kf,yz.aad_kf,");
		sbsql
				.append(" yz.mad_kf,yz.qbad_kf,yz.had_kf,yz.vad_kf,yz.fcad_kf,yz.std_kf,");
		sbsql
				.append(" yz.qbrad_kf,yz.hdaf_kf,yz.qgrad_daf_kf,yz.sdaf_kf,yz.var_kf,yz.zhijbfml,yz.zhijbfje,yz.suopje,yz.lsuopsl,yz.lsuopje from (select yz.id, gy.mingc as gongysb_id,jh.mingc as jihkjb_id,pz.mingc as pinzb_id,ys.mingc as yunsfsb_id,");
		sbsql
				.append(" yz.fenx,yz.qnet_ar,yz.aar,yz.ad,yz.vdaf,yz.mt,yz.stad,yz.aad,yz.mad,yz.qbad,yz.had,yz.vad,yz.fcad,yz.std,yz.qbrad,");
		sbsql
				.append(" yz.hdaf,yz.qgrad_daf,yz.sdaf,yz.var,yz.qnet_ar_kf,yz.aar_kf,yz.ad_kf,yz.vdaf_kf,yz.mt_kf,yz.stad_kf,yz.aad_kf,");
		sbsql
				.append(" yz.mad_kf,yz.qbad_kf,yz.had_kf,yz.vad_kf,yz.fcad_kf,yz.std_kf,");
		sbsql
				.append(" yz.qbrad_kf,yz.hdaf_kf,yz.qgrad_daf_kf,yz.sdaf_kf,yz.var_kf,yz.zhijbfml,yz.zhijbfje,yz.suopje,yz.lsuopsl,yz.lsuopje\n");
		sbsql
				.append(" from yuezlb yz,yuetjkjb yj,gongysb gy,jihkjb jh,pinzb pz,yunsfsb ys");
		sbsql
				.append(" where yz.yuetjkjb_id=yj.id and yj.gongysb_id=gy.id and yj.jihkjb_id=jh.id and yj.pinzb_id=pz.id");
		sbsql.append(" and yj.yunsfsb_id=ys.id and yj.diancxxb_id="
				+ lngDiancId + " and yj.riq=to_date('" + strDate
				+ "','yyyy-mm-dd') order by yz.id)yz");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][] = new String[2][46];
		ArrHeader[0] = new String[] { "������λ", "�ƻ�<br>�ھ�", "Ʒ��", "����<br>��ʽ",
				"����", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���",
				"���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���", "���ڻ���",
				"���ڻ���", "���ڻ���", "���ڻ���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���",
				"�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���",
				"�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���",
				"�ʼ�<br>����<br>ú��<br>(��)", "�ʼ�<br>����<br>���<br>(Ԫ)",
				"����<br>���<br>(Ԫ)", "����<br>����<br>����<br>(��)",
				"����<br>����<br>���<br>(Ԫ)" };
		ArrHeader[1] = new String[] { "������λ", "�ƻ�<br>�ھ�", "Ʒ��", "����<br>��ʽ",
				"����", "�յ���<br>��λ<br>��ֵ<br>Qnet,ar(Mj/kg)", "�յ���<br>�ҷ�<br>Aar",
				"�����<br>�ҷ�<br>Ad", "����<br>�޻һ�<br>�ӷ���<br>Vdaf", "ȫˮ<br>Mt",
				"����<br>�����<br>��<br>St,ad", "����<br>�����<br>�ҷ�<br>Aad",
				"����<br>�����<br>ˮ��<br>Mad", "��Ͳ<br>��ֵ", "����<br>�����<br>��<br>Had",
				"����<br>�����<br>�ӷ���<br>Vad", "�̶�̼", "�����<br>��<br>St,d",
				"����<br>�����<br>��λ��<br>Qbr,ad", "����<br>�޻һ�<br>��<br>Hdaf",
				"����<br>�޻һ�<br>��λ��<br>Qbr,daf", "����<br>�޻һ�<br>��<br>St,daf",
				"�յ���<br>�ӷ���<br>Var", "�յ���<br>��λ<br>��ֵ<br>Qnet,ar(Mj/kg)",
				"�յ���<br>�ҷ�<br>Aar", "�����<br>�ҷ�<br>Ad",
				"����<br>�޻һ�<br>�ӷ���<br>Vdaf", "ȫˮ<br>Mt",
				"����<br>�����<br>��<br>St,ad", "����<br>�����<br>�ҷ�<br>Aad",
				"����<br>�����<br>ˮ��<br>Mad", "��Ͳ<br>��ֵ", "����<br>�����<br>��<br>Had",
				"����<br>�����<br>�ӷ���<br>Vad", "�̶�̼", "�����<br>��<br>St,d",
				"����<br>�����<br>��λ��<br>Qbr,ad", "����<br>�޻һ�<br>��<br>Hdaf",
				"����<br>�޻һ�<br>��λ��<br>Qbr,daf", "����<br>�޻һ�<br>��<br>St,daf",
				"�յ���<br>�ӷ���<br>Var", "�ʼ�<br>����<br>ú��<br>(��)",
				"�ʼ�<br>����<br>���<br>(Ԫ)", "����<br>���<br>(Ԫ)",
				"����<br>����<br>����<br>(��)", "����<br>����<br>���<br>(Ԫ)" };
		// �п�
		int ArrWidth[] = new int[] { 100, 50, 40, 40, 40, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50 };

		// ����ҳ����
		rt.setTitle("������ͳ�Ʊ�", ArrWidth);
		rt.setDefaultTitle(20, 2, strMonth, Table.ALIGN_CENTER);

		// ����
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		// ҳ��
		// rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(6,1,"�Ʊ�:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(10,1,"���:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * ��ԭ��Լ����round��Ϊround_new ��ֵС��λȡϵͳ���� �޸�ʱ�䣺2008-12-05 �޸��ˣ�����
	 */
	/*
	 * ���ߣ���ʤ��
	 * ���ڣ�2013-05-10
	 * ����������ˮ�ֲ����һ��
	 */
	 
	private String getDiaor16() {
//		Visit v = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql = "select --decode(gongysb.mingc, null, '�ܼ�', gongysb.mingc) as kuangb,\n" + 
			
			"DECODE(GROUPING(S.TJKJ),\n" +
			"              1,\n" + 
			"              '�ܼ�',\n" + 
			"              DECODE(GROUPING(JIHKJB.MINGC),\n" + 
			"                     1,\n" + 
			"                     '*' || S.TJKJ,\n" + 
			"                     DECODE(GROUPING(S.QUANC),\n" + 
			"                            1,\n" + 
			"                            '#' || DECODE(JIHKJB.MINGC,\n" + 
			"                                          '�г��ɹ�',\n" + 
			"                                          '�ط���',\n" + 
			"                                          JIHKJB.MINGC) || 'С��',\n" + 
			"                            DECODE(GROUPING(S.DQMC),\n" + 
			"                                   1,\n" + 
			"                                   '<I>' || S.QUANC || '</I>',\n" + 
			"                                   DECODE(GROUPING(GONGYSB.MINGC),\n" + 
			"                                          1,\n" + 
			"                                          S.DQMC || 'С��',\n" + 
			"                                          GONGYSB.MINGC))))) AS KUANGB," 
			
				+ "       decode(pinzb.mingc,null,'_',pinzb.mingc) as pinz,\n"
				+ "       nvl(s.fenx,'') fenx,\n"
				+ "       nvl(sum(s.qickc), 0) as shangykc,\n"
				+ "     SUM(JINCML) AS JINML,\n" 
				+ "       sum(yuns) as yuns,\n"
				+ "       sum(kuid) kuid,\n" +
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * QNET_AR) / SUM(JINCML), 2)) AS QNET_AR,\n" + 
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * AAD) / SUM(JINCML), 2)) AS AAD,\n" + 
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * MT) / SUM(JINCML), 2)) AS Mar,\n" + 
				"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * Vdaf) / SUM(JINCML), 2)) AS Vdaf,\n"
				+ "       sum(s.hej) as hej,\n"
				+ "       sum(s.fady) as fady,\n"
				+ "       sum(s.gongry) as gongry,\n"
				+ "       sum(s.qith) as qith,\n"
				+ "       sum(s.sunh) as sunh,\n"
				+ "       sum(s.diaocl) as diaocl,\n"
				+ "       sum(s.panyk) as panyk,\n"
				+ " SUM(decode(diancxxb_id,324,s.shuifctz*0.4,s.shuifctz)) AS shuifctz,\n"
				+ "       sum(s.kuc) as kuc\n"
				+ "  from ("
				+ getBaseSql(false)
				+ ") s,\n"
				+ "       gongysb, JIHKJB,\n"
				+ "       pinzb\n"
				+ " where s.gongysb_id = gongysb.id and s.pinzb_id = pinzb.id and gongysb.leix = 0 \n"
				+ "   AND S.JIHKJB_ID = JIHKJB.ID\n" 
				+ "group by rollup(s.fenx, S.TJKJ,JIHKJB.MINGC,S.QUANC,S.DQMC, gongysb.mingc, pinzb.mingc)\n"
				+ "having not(GROUPING(GONGYSB.MINGC) + GROUPING(PINZB.MINGC) = 1 OR GROUPING(FENX) = 1)\n"
//				+ "order by grouping(gongysb.mingc) desc,gongysb.mingc,grouping(pinzb.mingc) desc,pinzb.mingc,s.fenx"
				+ "ORDER BY S.TJKJ DESC,JIHKJB.MINGC DESC,S.QUANC DESC, GROUPING(S.DQMC) DESC,S.DQMC,GONGYSB.MINGC DESC,s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		// String ArrHeader[][];
		// int ArrWidth[];
		String ArrHeader[][] = new String[4][19];
		ArrHeader[0] = new String[] { "���", "Ʒ��", "����<br>��<br>�ۼ�", "�³������",
				"ʵ�ʹ�Ӧ���", "ʵ�ʹ�Ӧ���", "ʵ�ʹ�Ӧ���", "ʵ�ʹ�Ӧ���", "ʵ�ʹ�Ӧ���", "ʵ�ʹ�Ӧ���",
				"ʵ�ʹ�Ӧ���", "ʵ������", "ʵ������", "ʵ������", "ʵ������", "ʵ������", "������", "��ӯ��","ˮ�ֲ����",
				"��ĩ�����" };
		ArrHeader[1] = new String[] { "���", "Ʒ��", "����<br>��<br>�ۼ�", "�³������",
				"�󷽹�Ӧ��", "�󷽹�Ӧ��", "�󷽹�Ӧ��", "������", "�ҷ֣�", "ˮ�֣�", "�ӷ��֣�", "�ϼ�",
				"����", "����", "����", "�������", "������", "��ӯ��","ˮ�ֲ����", "��ĩ�����" };
		ArrHeader[2] = new String[] { "���", "Ʒ��", "����<br>��<br>�ۼ�", "�³������",
				"�ϼ�", "�������", "����", "������", "�ҷ֣�", "ˮ�֣�", "�ӷ��֣�", "�ϼ�", "����",
				"����", "����", "�������", "������", "��ӯ��","ˮ�ֲ����", "��ĩ�����" };
		ArrHeader[3] = new String[] { "��", "��", "��", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16","17" };

		int ArrWidth[] = new int[] {120, 40, 40, 59, 59, 50, 50, 50, 50, 50,
				50, 55, 55, 55, 55, 55, 55, 55, 55,55 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// ����ҳ����
		rt.setTitle("������ú̿��Ӧ�����������±�", ArrWidth);
		Visit visit = ((Visit) getPage().getVisit());
		String _Danwqc = getTianzdwQuanc(getTreeid());
		if (_Danwqc.equals("��������ȼ�����޹�˾") && visit.getRenyjb() == 2) {
			_Danwqc = "���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���";
		}
		rt.setDefaultTitle(1, 5, "���λ:" + _Danwqc, Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 2, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "����16-1��",
				Table.ALIGN_RIGHT);

		// ����ҳ��
		rt.setMarginBottom(rt.getMarginBottom() + 25);
		// ����
		rt.setBody(new Table(rs, 4, 0, 3));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");

		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.getPages();

		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		
		rt.body.ShowZero = reportShowZero();
		
		// ����������
		convertItem(rt.body);
		
		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(6, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(11, 2, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(15, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.getPages();

		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
		// return rt.getAllPapersHtml(0);
	}

	private String getDiaor01() {
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql = "select d.mingc, s.jjizrl, s.fenx, s.biaoz, s.mhxj, s.mfady, s.mgongry,"
				+ "s.mqith, s.msunh, s.mkuc, s.shouyl, s.yhxj, s.fadyy, s.gongryy, s.qithy, s.sunhy, s.kucy,"
				+ "s.FADL, s.GONGRL, s.FADBZMH, s.GONGRBZMH, s.fadtrmh, s.gongrtrmh, s.FADBZML, s.GONGRBZML,"
				+ "s.zonghrlfrl, s.ranmrlfrl"
				+ " from ("
				+ getBaseSql(false)
				+ ") s, diancxxb d where s.diancxxb_id = d.id order by s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[4][27];
		ArrHeader[0] = new String[] { "��λ<br>����", "����<br>�豸<br>����",
				"����<br>��<br>�ۼ�", "ú̿(��)", "ú̿(��)", "ú̿(��)", "ú̿(��)", "ú̿(��)",
				"ú̿(��)", "ú̿(��)", "ʯ��(��)", "ʯ��(��)", "ʯ��(��)", "ʯ��(��)", "ʯ��(��)",
				"ʯ��(��)", "ʯ��(��)", "ʵ�����", "ʵ�����", "ú����", "ú����", "ú����", "ú����",
				"��׼ú��", "��׼ú��", "������", "������" };
		ArrHeader[1] = new String[] { "��λ<br>����", "����<br>�豸<br>����",
				"����<br>��<br>�ۼ�", "ʵ��", "����", "����", "����", "����", "����", "���",
				"ʵ��", "����", "����", "����", "����", "����", "���", "������<br>(��ǧ��ʱ)",
				"������<br>(����)", "��׼ú����", "��׼ú����", "��Ȼú����", "��Ȼú����",
				"������<br>(��)", "������<br>(��)", " �ۺ�ȼ��<br>(�׽�/ǧ��)",
				"��Ȼú<br>(�׽�/ǧ��)" };
		ArrHeader[2] = new String[] { "��λ<br>����", "����<br>�豸<br>����",
				"����<br>��<br>�ۼ�", "ʵ��", "С��", "����", "����", "����", "���", "���",
				"ʵ��", "С��", "����", "����", "����", "���", "���", "������<br>(��ǧ��ʱ)",
				"������<br>(����)", "����<br>(��/ǧ��ʱ)", "����<br>(ǧ��/����)",
				"����<br>(��/ǧ��ʱ)", "����<br>(ǧ��/����)", "������<br>(��)", "������<br>(��)",
				" �ۺ�ȼ��<br>(�׽�/ǧ��)", "��Ȼú<br>(�׽�/ǧ��)" };
		ArrHeader[3] = new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
				"19", "20", "21", "22", "23", "24", "25", "26", "27" };

		ArrWidth = new int[] { 65, 33, 33, 43, 43, 40, 33, 33, 33, 33, 33, 33,
				33, 33, 33, 33, 33, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// ����ҳ����
		rt.setTitle("������ú̿��Ӧ������������ܱ�", ArrWidth);
		rt.setDefaultTitle(1, 7, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 3, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ01��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 4, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		// rt.body.mergeFixedRow();
		rt.body.ShowZero = reportShowZero();
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setRowCells(1, Table.PER_FONTSIZE, 8);
		rt.body.setRowCells(2, Table.PER_FONTSIZE, 8);
		rt.body.setRowCells(3, Table.PER_FONTSIZE, 8);
		for (int i = 1; i <= 27; i++) {
			rt.body.setColCells(i, Table.PER_FONTSIZE, 8);
		}
		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(14, 2, "�ֹ��쵼:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(19, 2, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(23, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * ��ԭ��Լ����round��Ϊround_new �޸�ʱ�䣺2008-12-05 �޸��ˣ�����
	 */
	private String getDiaor03() {
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql =

		" select decode(grouping(g.mingc), 1, '�ܼ�', g.mingc) as ú������,\n"
				+ "       s.fenx as fenx,\n"
				+ "       sum(s.jincl),\n"
				+ "       sum(s.jianjl) as guohl,\n"
				+ "       decode(sum(s.jincl),0,0,round_new(sum(s.jianjl) / sum(s.jincl) * 100, 2)) as �ٷֱ�,\n"
				+ "       decode(sum(s.jincl),0,0,round_new(sum(s.jianjl) / sum(s.jincl) * 100, 2)) as guoh,\n"
				+ "       decode(sum(s.jincl),0,0,100 -  decode(sum(s.jincl),0,0,round_new(sum(s.jianjl) / sum(s.jincl) * 100, 2))) as jianc,\n"
				+ "       sum(nvl(s.yingd, 0)) as yind,\n"
				+ "       sum(nvl(s.yingdzje, 0)) as yingdje,\n"
				+ "       sum(nvl(s.kuid,0)) as kuid,\n"
				+ "       sum(nvl(s.kuidzje, 0)) as kuidje,\n"
				+ "       sum(nvl(suopsl, 0)) as suopsl,\n"
				+ "       sum(nvl(suopje, 0)) as suopje,\n"
				+ "       '' as shuom\n"
				+ "from ("
				+ getBaseSql(false)
				+ ")s, gongysb g\n"
				+ "   where s.gongysb_id = g.id\n"
				+ "   group by rollup(s.fenx, g.mingc) having(grouping(s.fenx) = 0)\n"
				+ "order by grouping(g.mingc) desc, g.mingc, max(g.xuh), grouping(s.fenx) desc, s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[4][14];
		ArrHeader[0] = new String[] { "��λ", "����<br>��<br>�ۼ�", "��������<br>(��)",
				"         �����������", "         �����������", "         �����������",
				"         �����������", "ӯ�����", "ӯ�����", "ӯ�����", "ӯ�����",
				"��������<br>(��)", "������<br>(Ԫ)", "˵��" };
		ArrHeader[1] = new String[] { "��λ", "����<br>��<br>�ۼ�", "��������<br>(��)",
				"      ����", "      ����", "      ����", "      ����", "ӯú<br>(��)",
				"ӯú���<br>(Ԫ)", "����<br>(��)", "���ֽ��<br>(Ԫ)", "��������<br>(��)",
				"������<br>(Ԫ)", "˵��" };
		ArrHeader[2] = new String[] { "��λ", "����<br>��<br>�ۼ�", "��������<br>(��)",
				"(��)", "%", "����%", "���%", "ӯú<br>(��)", "ӯú���<br>(Ԫ)",
				"����<br>(��)", "���ֽ��<br>(Ԫ)", "��������<br>(��)", "������<br>(Ԫ)", "˵��" };
		ArrHeader[3] = new String[] { "��", "��", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12" };
		ArrWidth = new int[] { 100, 40, 60, 60, 60, 60, 60, 60, 80, 60, 80, 80,
				80, 120 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// int e=0;
		// for(int i=0;i<ArrWidth.length;i++){
		// e+=ArrWidth[i];
		// }
		// System.out.println(e);
		// ����ҳ����
		rt.setTitle("����ú����ӯ���±���", ArrWidth);
		rt.setDefaultTitle(1, 5, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 2, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ03��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 4, 0, 1));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.getPages();
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.mergeFixedCol(1);

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(5, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(9, 2, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols(), 1, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * ��ԭ��Լ����round��Ϊround_new ȫˮ����ֵС��λȡϵͳ���� �޸�ʱ�䣺2008-12-05 �޸��ˣ�����
	 */
	private String getDiaor04() {
		JDBCcon cn = new JDBCcon();
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";
		String sql = "select decode(grouping(g.mingc),1,'�ܼ�',g.mingc) as ������λ,\n"
				+ " decode(grouping(p.mingc ),1,'- ',p.mingc )as Ʒ��,\n"
				+ " s.fenx as ���»��ۼ�,\n"
				+ " nvl(sum(s.jincl),0) as ����ú��,nvl(sum(s.jincl),0) as ����ú��,\n"
				+ " nvl(round_new(decode(sum(s.jincl),0,0,(sum(s.jincl) / sum(s.jincl)) * 100), 2),0) as ������,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2)),0) as QNET_AR,\n"
				+ " nvl(dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2))),0) as �ȼ�,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.mt_kf*s.jincl)/sum(s.jincl),1)),0) as MT_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.aar_kf*s.jincl)/sum(s.jincl),2)),0) as AAR_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.vdaf_kf*s.jincl)/sum(s.jincl),2)),0) as VDAF_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.std_kf*s.jincl)/sum(s.jincl),2)),0) as STD_KF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2)),0) as QNET_AR,\n"
				+ " nvl(dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2))),0) as �ȼ�,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.mt*s.jincl)/sum(s.jincl),1)),0) as MT,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.aar*s.jincl)/sum(s.jincl),2)),0) as AAR,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.vdaf*s.jincl)/sum(s.jincl),2)),0) as VDAF,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.std*s.jincl)/sum(s.jincl),2)),0) as STD,\n"
				+ " nvl(\n"
				+ " dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2)))-\n"
				+ " dengji(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2))),0) as �ȼ���,\n"
				+ " nvl(decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar*s.jincl)/sum(s.jincl),2))\n"
				+ "   -decode(sum(s.jincl),0,0,round_new(sum(s.qnet_ar_kf*s.jincl)/sum(s.jincl),2)),0) as ��ֵ��,\n"
				+ "\n"
				+ " decode(sum(s.jincl),0,0,round_new(sum(s.jincl*s.zhijbflv)/sum(s.jincl),2)) as �ٷֱ�,\n"
				+ " decode(sum(s.jincl*s.zhijbflv/100),0,0,round_new(sum(s.zhijbfje)/sum(s.jincl*s.zhijbflv/100),2)) as ���۲�,\n"
				+ " sum(s.zhijbfje) as �ܽ��,sum(s.suopje) as ������,\n"
				+ " sum(s.lsuopsl) as ����������,sum(s.lsuopje) as ��������\n"
				+ " from\n"
				+ "( "
				+ getBaseSql(false)
				+ ") s,gongysb g, pinzb p\n"
				+ "where s.gongysb_id = g.id and s.pinzb_id = p.id\n"
				+ " group by rollup(s.fenx,g.mingc,p.mingc)\n"
				+ " having(grouping(s.fenx) = 0) and not(grouping(g.mingc)=0 and grouping(p.mingc)=1)\n"
				+ " order by grouping(g.mingc) desc, g.mingc,p.mingc,s.fenx";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[3][26];
		ArrHeader[0] = new String[] { "���", "Ʒ��", "����<br>��<br>�ۼ�", "��������",
				"��������", "��������", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���", "�󷽻���",
				"��������", "��������", "��������", "��������", "��������", "��������", "�ȼ���", "��ֵ��",
				"�ʼ۲������", "�ʼ۲������", "�ʼ۲������", "������<br>(Ԫ)", "��������", "��������" };

		ArrHeader[1] = new String[] { "���", "Ʒ��", "����<br>��<br>�ۼ�",
				"����<br>ú��<br>(��)", "����<br>ú��<br>(��)", "����<br>��%",
				"Qnet,<br>ar(MJ<br>/Kg)", "�ȼ�", "Mt%", "Aar%", "Vdaf%",
				"St,d%", "Qnet,<br>ar(MJ<br>/Kg)", "�ȼ�", "Mt%", "Aar%",
				"Vdaf%", "St,d%", "�ȼ�", "Qnet,<br>ar(MJ<br>/Kg)", "%",
				"���۲�<br>(Ԫ/��)", "�ܽ��<br>(Ԫ)", "������<br>(Ԫ)", "����<br>(��)",
				"���<br>(Ԫ)" };
		ArrHeader[2] = new String[] { "��", "��", "��", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
				"17", "18", "19", "20", "21", "22", "23" };

		ArrWidth = new int[] { 85, 40, 38, 45, 45, 30, 35, 30, 30, 30, 30, 30,
				30, 30, 30, 30, 30, 30, 40, 40, 30, 60, 75, 75, 45, 50 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// ����ҳ����
		rt.setTitle("����ú�������Ƽ�ú����������±�", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 2, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ04��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 3, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.ShowZero = reportShowZero();
		if (rt.body.getRows() > rt.body.getFixedRows()) {
			rt.body.mergeFixedCol(2);
		}
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.0");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.0");
		rt.body.setColFormat(20, "0.00");
		rt.body.setColFormat(21, "0.00");
		rt.body.setColFormat(23, "0.00");
		rt.body.setColFormat(24, "0.00");
		rt.body.setColFormat(25, "");
		rt.body.setColFormat(26, "0.00");
		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(11, 2, "�ֹ��쵼:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 2, "�Ʊ�:" + getZhibr(), Table.ALIGN_CENTER);
		rt.setDefautlFooter(22, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	Report rt_zhibb = null;

	/*
	 * 08�� �����ˣ����پ� ���ڣ�2009-04-13
	 */
	private String getDiaor08() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql =

		" select  mingc,fenx,laiml,decode(mingc,'�ܼ�',kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy,daoczhj) as daoczhj,\n"
				+ "		 kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,\n"
				+ "        syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
				+ "		 decode(mingc,'�ܼ�',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy)*29.271/qnet_ar,2),bmdj) as bmdj,\n"
				+ "		 decode(mingc,'�ܼ�',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy-kjs-tlyfs-syfs-qyfs)*29.271/qnet_ar,2),bhsbmj) as bhsbmj\n"
				+ "        from (\n"
				+ "	select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
				+ "       decode(gys.mingc,null,'�ܼ�',gys.mingc) as mingc,\n"
				+ "       sl.fenx,\n"
				+ "       t.gongysb_id,\n"
				+ "       sum(sl.biaoz) as laiml,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum((j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz)/sum(sl.biaoz),2)) as daoczhj,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as daoczhj,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.meij*sl.biaoz)/sum(sl.biaoz),2)) as kj,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meij*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as  kj,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.meijs*sl.biaoz)/sum(sl.biaoz),2)) as kjs,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meijs*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as kjs,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.jiaohqzf*sl.biaoz)/sum(sl.biaoz),2)) as jhqyzf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.jiaohqzf*sl.biaoz))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as jhqyzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0))/sum(sl.biaoz),2)) as tlyf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0))/sum(sl.biaoz),2)) as tlyfs, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyfs,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0))/sum(sl.biaoz),2)) as tlzf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0))/sum(sl.biaoz),2)) as syf, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0)))	\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0))/sum(sl.biaoz),2)) as syfs, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syfs,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0))/sum(sl.biaoz),2)) as szf, 	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as szf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0))/sum(sl.biaoz),2)) as qyf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0))/sum(sl.biaoz),2)) as qyfs,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0)))\n"
//				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.daozzf*sl.biaoz)/sum(sl.biaoz),2)) as dzzf,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.daozzf*sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as dzzf,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.qit*sl.biaoz)/sum(sl.biaoz),2)) as qtfy,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qit*sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qtfy,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.qnet_ar*sl.biaoz)/sum(sl.biaoz),2)) as qnet_ar,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qnet_ar * sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qnet_ar,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.biaomdj*sl.biaoz)/sum(sl.biaoz),2)) as bmdj,	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.biaomdj * sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bmdj,\n"
				+ "		  decode(sum(nvl(sl.biaoz,0)),0,0,round_new(sum(j.buhsbmdj*sl.biaoz)/sum(sl.biaoz),2)) as bhsbmj	\n"
//				+ "       round_new(sum(decode(sl.biaoz,0,0,j.buhsbmdj * sl.biaoz))\n"
//				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bhsbmj\n"
				+ "\n"
				+ "  from yuercbmdj j,yueslb sl,yuetjkjb t,vwgongys gys\n"
				+ " where j.yuetjkjb_id = t.id\n"
				+ "   and sl.yuetjkjb_id=t.id\n"
				+ "   and j.yuetjkjb_id=sl.yuetjkjb_id\n"
				+ "   and t.gongysb_id = gys.id\n"
				+ "   and j.fenx=sl.fenx\n"
				+ "   and t.riq = to_date('"
				+ strDate
				+ "', 'yyyy-mm-dd')\n"
				+ "   and (t.diancxxb_id = "
				+ getTreeid()
				+ " or t.diancxxb_id in(select id from diancxxb where fuid="
				+ getTreeid()
				+ "))\n"
				+ " group by rollup(sl.fenx,gys.mingc,t.gongysb_id)\n"
				+ "\n"
				+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
				+ "            or grouping(sl.fenx)=1)\n"
				+ " order by xuh,sl.fenx)";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][21];
		ArrHeader[0] = new String[] { "��Ӧ��", "����<br>��<br>�ۼ�", "ú��(��)",
				"����<br>�ۺϼ�<br>(Ԫ/��)", "���<br>(Ԫ/��)", "��ֵ<br>˰��<br>(Ԫ/��)",
				"����ǰ<br>���ӷ�<br>(Ԫ/��)", "��·<br>�˷�<br>(Ԫ/��)", "��·<br>˰��<br>(Ԫ/��)",
				"��·<br>�ӷ�<br>(Ԫ/��)", "ˮ�˷�<br>(Ԫ/��)", "ˮ��<br>˰��<br>(Ԫ/��)",
				"ˮ��<br>�ӷ�<br>(Ԫ/��)", "���˷�<br>(Ԫ/��)", "����<br>˰��<br>(Ԫ/��)",
				"���ӷ�<br>(Ԫ/��)", "��վ<br>�ӷ�<br>(Ԫ/��)", "����<br>����<br>(Ԫ/��)",
				"��ֵ<br>Mj/kg", "��ú����<br>��˰<br>(Ԫ/��)", "��ú����<br>����˰<br>(Ԫ/��)" };
		ArrHeader[1] = new String[] { "��", "��", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19" };

		ArrWidth = new int[] { 95, 40, 55, 47, 47, 38, 47, 47, 47, 47, 47, 47,
				47, 47, 47, 47, 47, 47, 47, 47, 47 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// ����ҳ����
		rt.setTitle("��ú�۸������", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ08��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		rt.body.setColFormat(20, "0.000");
		rt.body.setColFormat(21, "0.00");

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "�ֹ��쵼:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 2, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiaor08zr() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql =

		" select  mingc,fenx,laiml,decode(mingc,'�ܼ�',kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy,daoczhj) as daoczhj,\n"
				+ "		 kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
				+ "		 decode(mingc,'�ܼ�',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy)*29.271/qnet_ar,2),bmdj) as bmdj,\n"
				+ "		 decode(mingc,'�ܼ�',round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf\n"
				+ "			+dzzf+qtfy-kjs-tlyfs-syfs-qyfs)*29.271/qnet_ar,2),bhsbmj) as bhsbmj\n"
				+ "        from (\n"
				+ "	select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
				+ "       decode(gys.mingc,null,'�ܼ�',gys.mingc) as mingc,\n"
				+ "       sl.fenx,\n"
				+ "       t.gongysb_id,\n"
				+ "       sum(sl.biaoz) as laiml,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as daoczhj,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meij*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as  kj,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.meijs*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as kjs,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.jiaohqzf*sl.biaoz))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as jhqyzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyfs,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syfs,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as szf,\n"
				+ "\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0)))\n"
				+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.daozzf*sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as dzzf,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qit*sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qtfy,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.qnet_ar * sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qnet_ar,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.biaomdj * sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bmdj,\n"
				+ "       round_new(sum(decode(sl.biaoz,0,0,j.buhsbmdj * sl.biaoz))\n"
				+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bhsbmj\n"
				+ "\n"
				+ "  from yuercbmdj j,yueslb sl,yuetjkjb t,vwgongys gys\n"
				+ " where j.yuetjkjb_id = t.id\n"
				+ "   and sl.yuetjkjb_id=t.id\n"
				+ "   and j.yuetjkjb_id=sl.yuetjkjb_id\n"
				+ "   and t.gongysb_id = gys.id\n"
				+ "   and j.fenx=sl.fenx\n"
				+ "   and t.riq = to_date('"
				+ strDate
				+ "', 'yyyy-mm-dd')\n"
				+ "   and (t.diancxxb_id = "
				+ getTreeid()
				+ " or t.diancxxb_id in(select id from diancxxb where fuid="
				+ getTreeid()
				+ "))\n"
				+ " group by rollup(sl.fenx,gys.mingc,t.gongysb_id)\n"
				+ "\n"
				+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
				+ "            or grouping(sl.fenx)=1)\n"
				+ " order by xuh,sl.fenx)";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][21];
		ArrHeader[0] = new String[] { "��Ӧ��", "����<br>��<br>�ۼ�", "ú��(��)",
				"����<br>�ۺϼ�<br>(Ԫ/��)", "���<br>(Ԫ/��)", "��ֵ˰��<br>(Ԫ/��)",
				"����ǰ<br>���ӷ�<br>(Ԫ/��)", "��·�˷�<br>(Ԫ/��)", "��·˰��<br>(Ԫ/��)",
				"��·�ӷ�<br>(Ԫ/��)", "���˷�<br>(Ԫ/��)", "����˰��<br>(Ԫ/��)",
				"���ӷ�<br>(Ԫ/��)", "��վ�ӷ�<br>(Ԫ/��)", "��������<br>(Ԫ/��)",
				"��ֵ<br>Mj/kg", "��ú����<br>��˰<br>(Ԫ/��)", "��ú����<br>����˰<br>(Ԫ/��)" };
		ArrHeader[1] = new String[] { "��", "��", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19" };

		ArrWidth = new int[] { 100, 40, 60, 50, 50, 40, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// ����ҳ����
		rt.setTitle("��ú�۸������", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ08��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.000");
		rt.body.setColFormat(18, "0.00");

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "�ֹ��쵼:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 2, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 2, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

//	private void ArrWidth(int i) {
//		// TODO �Զ����ɷ������
//	}

	private String getYuezbwcqk() {
		JDBCcon con = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		String yuejsbmdj = "yuercbmdj";
		ResultSet rs = con
				.getResultSet("SELECT ZHI FROM XITXXB WHERE MINGC = '��ָ��������'");
		try {
			if (rs.next()) {
				yuejsbmdj = rs.getString("ZHI");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String Yearm = this.getNianfValue().getId() + YUEF;
		String Yearm_tq = (this.getNianfValue().getId() - 1) + YUEF;
		// ���±���
		String Wdanw = "10000";
		String sql1 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')='"
				+ Yearm
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')='"
				+ Yearm
				+ "'and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2) jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--������Ȼú��\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--����ú������\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--������Ȼú����\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--������ú����\n"
				+ "sum(s.yingd-s.kuid)yingk,--����úӯ����\n"
				+ "sum(s.suopje)suopje,--����������\n"
				+ "sum(z.suopje)relspje,--����������\n"
				+ "sum(z.lsuopje)lsuopje--����������\n"
				+ "from (select * from yueslb where fenx='����') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='����') z,\n"
				+ "(select * from "
				+ yuejsbmdj
				+ " where fenx='����') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--��¯ú������\n"
				+ "yuezbb.faddwrlcb,--��λȼ�ϳɱ�\n" + "yuezbb.fadl,--������\n"
				+ "yuezbb.gongrl,--������\n" + "yuezbb.shoudl,--��������\n"
				+ "yuezbb.gongdbzmh,--����ú��\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--������Ȼú��\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--���ñ�ú��\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm + "' and yuezbb.fenx='����'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_bb = con.getResultSetList(sql1);
		// ����ͬ��
		String sql2 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')='"
				+ Yearm_tq
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')='"
				+ Yearm_tq
				+ "'and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2)jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--������Ȼú��\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--����ú������\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--������Ȼú����\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--������ú����\n"
				+ "sum(s.yingd-s.kuid)yingk,--����úӯ����\n"
				+ "sum(s.suopje)suopje,--����������\n"
				+ "sum(z.suopje)relspje,--����������\n"
				+ "sum(z.lsuopje)lsuopje--����������\n"
				+ "from (select * from yueslb where fenx='����') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='����') z,\n"
				+ "(select * from yuercbmdj where fenx='����') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm_tq
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--��¯ú������\n"
				+ "yuezbb.faddwrlcb,--��λȼ�ϳɱ�\n" + "yuezbb.fadl,--������\n"
				+ "yuezbb.gongrl,--������\n" + "yuezbb.shoudl,--��������\n"
				+ "yuezbb.gongdbzmh,--����ú��\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--������Ȼú��\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--���ñ�ú��\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm_tq + "' and yuezbb.fenx='����'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_bt = con.getResultSetList(sql2);
		// �ۼƱ���
		String sql3 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')<='"
				+ Yearm
				+ "'and  to_char(jiesb.ruzrq,'yyyy')='"
				+ this.getNianfValue().getId()
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')<='"
				+ Yearm
				+ "'and  to_char(jiesyfb.ruzrq,'yyyy')='"
				+ this.getNianfValue().getId()
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2)jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--������Ȼú��\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--����ú������\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--������Ȼú����\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--������ú����\n"
				+ "sum(s.yingd-s.kuid)yingk,--����úӯ����\n"
				+ "sum(s.suopje)suopje,--����������\n"
				+ "sum(z.suopje)relspje,--����������\n"
				+ "sum(z.lsuopje)lsuopje--����������\n"
				+ "from (select * from yueslb where fenx='�ۼ�') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='�ۼ�') z,\n"
				+ "(select * from "
				+ yuejsbmdj
				+ " where fenx='�ۼ�') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--��¯ú������\n"
				+ "yuezbb.faddwrlcb,--��λȼ�ϳɱ�\n" + "yuezbb.fadl,--������\n"
				+ "yuezbb.gongrl,--������\n" + "yuezbb.shoudl,--��������\n"
				+ "yuezbb.gongdbzmh,--����ú��\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--������Ȼú��\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--���ñ�ú��\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm + "' and yuezbb.fenx='�ۼ�'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_lb = con.getResultSetList(sql3);
		// �ۼ�ͬ��
		String sql4 = "select round(feizb.jinctrml/"
				+ Wdanw
				+ ",2) jinctrml,feizb.jincmfrl,zhib.rultrmpjfrl,feizb.meij,feizb.biaomdj,\n"
				+ "zhib.faddwrlcb,zhib.fadl,zhib.gongrl,zhib.shoudl,zhib.gongdbzmh,round(zhib.fadgrytrml/"
				+ Wdanw
				+ ",2) fadgrytrml,\n"
				+ "round(rulmzbzml/"
				+ Wdanw
				+ ",2) rulmzbzml,feizb.yingk,round(feizb.suopje/"
				+ Wdanw
				+ ",2) suopje,round(feizb.relspje/"
				+ Wdanw
				+ ",2) relspje,round(feizb.lsuopje/"
				+ Wdanw
				+ ",2) lsuopje,\n"
				+ "round(((select sum(jiesb.hansmk) from jiesb where  to_char(jiesb.ruzrq,'yyyymm')<='"
				+ Yearm_tq
				+ "'and  to_char(jiesb.ruzrq,'yyyy')='"
				+ (this.getNianfValue().getId() - 1)
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ ")\n"
				+ "+(select nvl(sum(jiesyfb.hansyf),0) from jiesyfb where  to_char(jiesyfb.ruzrq,'yyyymm')<='"
				+ Yearm_tq
				+ "'and  to_char(jiesyfb.ruzrq,'yyyy')='"
				+ (this.getNianfValue().getId() - 1)
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "))/"
				+ Wdanw
				+ ",2)jine\n"
				+ "from(\n"
				+ "select t.riq,sum(s.biaoz)jinctrml,--������Ȼú��\n"
				+ "round(sum(z.qnet_ar*s.biaoz)/sum(s.biaoz),2) jincmfrl,--����ú������\n"
				+ "round(sum((d.meij-d.meijs+d.yunj-d.yunjs)*s.biaoz)/sum(s.biaoz),2)meij,--������Ȼú����\n"
				+ "round(sum(d.BUHSBMDJ*s.biaoz)/sum(s.biaoz),2)biaomdj,--������ú����\n"
				+ "sum(s.yingd-s.kuid)yingk,--����úӯ����\n"
				+ "sum(s.suopje)suopje,--����������\n"
				+ "sum(z.suopje)relspje,--����������\n"
				+ "sum(z.lsuopje)lsuopje--����������\n"
				+ "from (select * from yueslb where fenx='�ۼ�') s,\n"
				+ "yuetjkjb t,\n"
				+ "(select * from yuezlb where fenx='�ۼ�') z,\n"
				+ "(select * from yuercbmdj where fenx='�ۼ�') d\n"
				+ "where s.yuetjkjb_id=t.id and t.id=z.yuetjkjb_id\n"
				+ "and t.id=d.yuetjkjb_id\n"
				+ "and  to_char(t.riq,'yyyymm')='"
				+ Yearm_tq
				+ "' and diancxxb_id= "
				+ getTreeid()
				+ "\n"
				+ "group by t.riq\n"
				+ ")feizb,(\n"
				+ "select yuezbb.riq,round(yuezbb.rultrmpjfrl/1000,2)rultrmpjfrl,--��¯ú������\n"
				+ "yuezbb.faddwrlcb,--��λȼ�ϳɱ�\n" + "yuezbb.fadl,--������\n"
				+ "yuezbb.gongrl,--������\n" + "yuezbb.shoudl,--��������\n"
				+ "yuezbb.gongdbzmh,--����ú��\n"
				+ "round(yuezbb.fadgrytrml,2)fadgrytrml,--������Ȼú��\n"
				+ "round(yuezbb.rulmzbzml,2)rulmzbzml--���ñ�ú��\n"
				+ "from yuezbb\n" + "where to_char(yuezbb.riq,'yyyymm')='"
				+ Yearm_tq + "' and yuezbb.fenx='�ۼ�'and diancxxb_id= "
				+ getTreeid() + ")zhib\n" + "where feizb.riq=zhib.riq(+)";
		ResultSetList rs_lt = con.getResultSetList(sql4);
		Report rt = new Report();
		rt_zhibb = rt;
		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[1][7];
		ArrHeader[0] = new String[] { "ָ������", "��λ", "���ջ��ۼ�", "����", "ͬ��",
				"�仯���", "��ע" };
		ArrWidth = new int[] { 100, 100, 80, 80, 80, 80, 100 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		// ����ҳ����
		rt.setTitle("���糧ȼ�Ϲ���ָ��������ͳ��", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, "����:" + this.getNianfValue().getId() + "��"
				+ YUEF + "��", Table.ALIGN_LEFT);
		rt.title.setRowHeight(40);
		// ����
		rt.setBody(new Table(35, 7));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(40, this.paperStyle));
		//
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = reportShowZero();
		// ��д����
		// ��д�����
		rt.body.setCellValue(1, 1, "ָ������");
		rt.body.setCellValue(1, 2, "��λ");
		rt.body.setCellValue(1, 3, "���ջ��ۼ�");
		rt.body.setCellValue(1, 4, "����");
		rt.body.setCellValue(1, 5, "ͬ��");
		rt.body.setCellValue(1, 6, "�仯���");
		rt.body.setCellValue(1, 7, "��ע");
		// ��д������
		rt.body.setCellValue(2, 1, "������Ȼú��");
		rt.body.setCellValue(3, 1, "������Ȼú��");
		rt.body.setCellValue(4, 1, "����ú������");
		rt.body.setCellValue(5, 1, "����ú������");
		rt.body.setCellValue(6, 1, "��¯ú������");
		rt.body.setCellValue(7, 1, "��¯ú������");
		rt.body.setCellValue(8, 1, "������Ȼú����");
		rt.body.setCellValue(9, 1, "������Ȼú����");
		rt.body.setCellValue(10, 1, "������ú����");
		rt.body.setCellValue(11, 1, "������ú����");
		rt.body.setCellValue(12, 1, "��λȼ�ϳɱ�");
		rt.body.setCellValue(13, 1, "��λȼ�ϳɱ�");
		rt.body.setCellValue(14, 1, "������");
		rt.body.setCellValue(15, 1, "������");
		rt.body.setCellValue(16, 1, "������");
		rt.body.setCellValue(17, 1, "������");
		rt.body.setCellValue(18, 1, "��������");
		rt.body.setCellValue(19, 1, "��������");
		rt.body.setCellValue(20, 1, "����ú��");
		rt.body.setCellValue(21, 1, "����ú��");
		rt.body.setCellValue(22, 1, "������Ȼú��");
		rt.body.setCellValue(23, 1, "������Ȼú��");
		rt.body.setCellValue(24, 1, "���ñ�ú��");
		rt.body.setCellValue(25, 1, "���ñ�ú��");
		rt.body.setCellValue(26, 1, "����úӯ����");
		rt.body.setCellValue(27, 1, "����úӯ����");
		rt.body.setCellValue(28, 1, "����������");
		rt.body.setCellValue(29, 1, "����������");
		rt.body.setCellValue(30, 1, "����������");
		rt.body.setCellValue(31, 1, "����������");
		rt.body.setCellValue(32, 1, "����������");
		rt.body.setCellValue(33, 1, "����������");
		rt.body.setCellValue(34, 1, "ȼ�Ͻ�����");
		rt.body.setCellValue(35, 1, "ȼ�Ͻ�����");
		//
		rt.body.setCellValue(2, 2, "���");
		rt.body.setCellValue(3, 2, "���");
		rt.body.setCellValue(4, 2, "Mj/kg");
		rt.body.setCellValue(5, 2, "Mj/kg");
		rt.body.setCellValue(6, 2, "Mj/kg");
		rt.body.setCellValue(7, 2, "Mj/kg");
		rt.body.setCellValue(8, 2, "Ԫ/��");
		rt.body.setCellValue(9, 2, "Ԫ/��");
		rt.body.setCellValue(10, 2, "Ԫ/��");
		rt.body.setCellValue(11, 2, "Ԫ/��");
		rt.body.setCellValue(12, 2, "Ԫ/ǧǧ��ʱ");
		rt.body.setCellValue(13, 2, "Ԫ/ǧǧ��ʱ");
		rt.body.setCellValue(14, 2, "��ǧ��ʱ");
		rt.body.setCellValue(15, 2, "��ǧ��ʱ");
		rt.body.setCellValue(16, 2, "ʮ�ڽ���");
		rt.body.setCellValue(17, 2, "ʮ�ڽ���");
		rt.body.setCellValue(18, 2, "��ǧ��ʱ");
		rt.body.setCellValue(19, 2, "��ǧ��ʱ");
		rt.body.setCellValue(20, 2, "g/kwh");
		rt.body.setCellValue(21, 2, "g/kwh");
		rt.body.setCellValue(22, 2, "���");
		rt.body.setCellValue(23, 2, "���");
		rt.body.setCellValue(24, 2, "���");
		rt.body.setCellValue(25, 2, "���");
		rt.body.setCellValue(26, 2, "��");
		rt.body.setCellValue(27, 2, "��");
		rt.body.setCellValue(28, 2, "��Ԫ");
		rt.body.setCellValue(29, 2, "��Ԫ");
		rt.body.setCellValue(30, 2, "��Ԫ");
		rt.body.setCellValue(31, 2, "��Ԫ");
		rt.body.setCellValue(32, 2, "��Ԫ");
		rt.body.setCellValue(33, 2, "��Ԫ");
		rt.body.setCellValue(34, 2, "��Ԫ");
		rt.body.setCellValue(35, 2, "��Ԫ");
		// ��дֵ
		int k1 = 0, k2 = 0;
		// if(rs_bb.next()){
		// k++;
		// }
		rs_bb.next();
		rs_bt.next();
		rs_lb.next();
		rs_lt.next();
		for (int i = 2; i < 36; i++) {
			// ���»��ۼ�
			if (i % 2 == 0) {// ż��
				rt.body.setCellValue(i, 3, "����");
				// ���ڵ�4��
				rt.body.setCellValue(i, 4, rs_bb.getString(k1));
				// ͬ�ڵ�5��
				rt.body.setCellValue(i, 5, rs_bt.getString(k1));
				// �仯���6��
				rt.body.setCellValue(i, 6, String
						.valueOf((rs_bb.getString(k1) == null) ? 0 : rs_bb
								.getDouble(k1)
								- ((rs_bt.getString(k1) == null) ? 0 : rs_bt
										.getDouble(k1))));

				k1++;
			} else {
				rt.body.setCellValue(i, 3, "�ۼ�");
				// ���ڵ�4��
				rt.body.setCellValue(i, 4, rs_lb.getString(k2));
				// ͬ�ڵ�5��
				rt.body.setCellValue(i, 5, rs_lt.getString(k2));
				// �仯���6��
				rt.body.setCellValue(i, 6, String
						.valueOf((rs_lb.getString(k2) == null) ? 0 : rs_lb
								.getDouble(k2)
								- ((rs_lt.getString(k2) == null) ? 0 : rs_lt
										.getDouble(k2))));
				k2++;
			}
		}
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		// rt.body.setRowCells(1, Table.ALIGN_RIGHT, 1);
		// ҳ��
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(3, 2, "�Ʊ�:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(5, 2, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols(), 1, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	/*
	 * 08-1�� �����ˣ����پ� ���ڣ�2009-04-10
	 */
	private String getDiaor08_1() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql = "select mingc,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj,\n"
				+ "        		kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar, \n"
				+ "				decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj,	\n"
				+ "				decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk	\n"
				+ "         from (\n"
				+ "select decode(gongysb.mingc,null,0,avg(gongysb.xuh)) as xuh,\n" + 
//				+ "       decode(gys.mingc,null,'�ܼ�',gys.mingc) as mingc,\n" 
				
				"DECODE(GROUPING(J.TJKJ),\n" +
				"              1,\n" + 
				"              '�ܼ�',\n" + 
				"              DECODE(GROUPING(JIHKJB.MINGC),\n" + 
				"                     1,\n" + 
				"                     '*' || J.TJKJ,\n" + 
				"                     DECODE(GROUPING(J.QUANC),\n" + 
				"                            1,\n" + 
				"                            '#' || decode(JIHKJB.MINGC,'�г��ɹ�','�ط���',JIHKJB.MINGC) || 'С��',\n" + 
				"                            DECODE(GROUPING(J.DQMC),\n" + 
				"                                   1,\n" + 
				"                                   '<I>'||J.QUANC||'</I>',\n" + 
				"                                   DECODE(GROUPING(GONGYSB.MINGC),\n" + 
				"                                          1,\n" + 
				"                                          J.DQMC || 'С��',\n" + 
				"                                          GONGYSB.MINGC))))) AS MINGC," 
				
				+ "       j.fenx,\n"
				+ "       --t.gongysb_id,\n"
				+ "       sum(j.jiesl) as jsl,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum((j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl)/sum(j.jiesl),2)) as daoczhj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.meij*j.jiesl)/sum(j.jiesl),2)) as kj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.meijs*j.jiesl)/sum(j.jiesl),2)) as kjs,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.kuangqyf*j.jiesl)/sum(j.jiesl),2)) as jhqyzf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,1,j.yunj*j.jiesl,0))/sum(j.jiesl),2)) as tlyf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,1,j.yunjs*j.jiesl,0))/sum(j.jiesl),2)) as tlyfs, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,1,j.zaf*j.jiesl,0))/sum(j.jiesl),2)) as tlzf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,3,j.yunj*j.jiesl,0))/sum(j.jiesl),2)) as syf, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,3,j.yunjs*jiesl,0))/sum(j.jiesl),2)) as syfs, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,3,j.zaf*j.jiesl,0))/sum(j.jiesl),2)) as szf, 	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,2,j.yunj*j.jiesl,0))/sum(j.jiesl),2)) as qyf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(decode(j.yunsfsb_id,2,j.yunj*j.jiesl,0))/sum(j.jiesl),2)* (select nvl(max(zhi),0.07) from xitxxb where xitxxb.mingc='�˷�˰��')) as qyfs,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
//				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2)* (select nvl(max(zhi),0.07) from xitxxb where xitxxb.mingc='�˷�˰��') as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.daozzf*j.jiesl)/sum(j.jiesl),2)) as dzzf,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.qit*j.jiesl)/sum(j.jiesl),2)) as qtfy,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.qnet_ar*j.jiesl)/sum(j.jiesl),2)) as qnet_ar,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.biaomdj*j.jiesl)/sum(j.jiesl),2)) as bmdj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
				+ "		  decode(sum(nvl(j.jiesl,0)),0,0,round_new(sum(j.buhsbmdj*j.jiesl)/sum(j.jiesl),2)) as bhsbmj,	\n"
//				+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
//				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
				+ "       round(sum(j.meij * j.jiesl)/10000,2) mk\n" + 

				"from (SELECT j.*,\n" +
				"               to_date('" + strDate + "','yyyy-mm-dd') riq, tj.diancxxb_id, tj.gongysb_id,\n" + 
				"               tj.DQID,\n" + 
				"               tj.DQMC,\n" + 
				"               SF.QUANC QUANC,\n" + 
				"               DECODE(tj.JIHKJB_ID, 1, '�ص㶩��', 3, '�ص㶩��', kj.MINGC) TJKJ,\n" + 
				"               tj.JIHKJB_ID,\n" + 
				"               yunsfsb_id\n" + 
				"          FROM YUEJSBMDJ_DK j, (select yuetjkjb.id,yunsfsb_id, gongysb_id, diancxxb_id, '����' as fenx, JIHKJB_ID, DQID, DQMC\n" + 
				"                        from yuetjkjb , VWGONGYSDQ DQ\n" + 
				"                       where riq = to_date('" + strDate + "','yyyy-mm-dd')\n" + 
				"                         AND YUETJKJB.GONGYSB_ID = DQ.ID\n" + 
				"                         and diancxxb_id = " + getTreeid() + "\n" + 
				"                      union\n" + 
				"                      select yuetjkjb.id,yunsfsb_id, gongysb_id, diancxxb_id, '�ۼ�' as fenx, JIHKJB_ID, DQID, DQMC\n" + 
				"                        from yuetjkjb, VWGONGYSDQ DQ\n" + 
				"                       where riq = to_date('" + strDate + "','yyyy-mm-dd')\n" + 
				"                         AND YUETJKJB.GONGYSB_ID = DQ.ID\n" + 
				"                         and diancxxb_id = " + getTreeid() + ") tj,\n" + 
				"       SHENGFB SF, MEIKDQB DQ, jihkjb kj\n" + 
				" where j.yuetjkjb_id(+) = tj.id\n" + 
				"   and tj.fenx = j.fenx(+)\n" + 
				"   AND tj.JIHKJB_ID = kj.ID\n" + 
				"   AND tj.DQID = DQ.ID\n" + 
				"   AND DQ.SHENGFB_ID = SF.ID) j, gongysb, jihkjb\n" + 
				" WHERE j.gongysb_id = gongysb.id and gongysb.leix = 0\n" + 
				"   AND j.JIHKJB_ID = JIHKJB.ID\n" + 
				" group by rollup(j.fenx, j.TJKJ,jihkjb.MINGC,j.QUANC, j.DQMC, gongysb.mingc)\n" + 
				" having not (grouping(j.fenx)=1)\n" + 
				" order by j.TJKJ DESC,jihkjb.MINGC DESC,j.QUANC DESC, GROUPING(j.DQMC) DESC,j.DQMC,gongysb.MINGC DESC,j.fenx,xuh)";


		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][22];
		ArrHeader[0] = new String[] { "��Ӧ��", "����<br>��<br>�ۼ�", "ú��(��)",
				"����<br>�ۺϼ�<br>(Ԫ/��)", "���<br>(Ԫ/��)", "��ֵ<br>˰��<br>(Ԫ/��)",
				"����ǰ<br>���ӷ�<br>(Ԫ/��)", "��·<br>�˷�<br>(Ԫ/��)", "��·<br>˰��<br>(Ԫ/��)",
				"��·<br>�ӷ�<br>(Ԫ/��)", "ˮ�˷�<br>(Ԫ/��)", "ˮ��<br>˰��<br>(Ԫ/��)",
				"ˮ��<br>�ӷ�<br>(Ԫ/��)", "���˷�<br>(Ԫ/��)", "����<br>˰��<br>(Ԫ/��)",
				"���ӷ�<br>(Ԫ/��)", "��վ<br>�ӷ�<br>(Ԫ/��)", "����<br>����<br>(Ԫ/��)",
				"��ֵ<br>Mj/kg", "��ú����<br>��˰<br>(Ԫ/��)", "��ú����<br>����˰<br>(Ԫ/��)",
				"ú��<br>(��Ԫ)" };
		ArrHeader[1] = new String[] { "��", "��", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20" };

		ArrWidth = new int[] {120, 40, 55, 47, 47, 40, 45, 45, 45, 45, 45, 45,
				47, 47, 47, 47, 47, 47, 47, 47, 47, 57 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		
		// ����ҳ����
		rt.setTitle("��ú�۸������", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ08-1��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		
		rt.body.ShowZero = reportShowZero();
		
		convertItem(rt.body);

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		rt.body.setColFormat(20, "0.000");
		rt.body.setColFormat(21, "0.00");
		rt.body.setColFormat(22, "0.00");
		// rt.body.setColFormat(23,"0.00");

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 2, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(18, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiaor08_1zr() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql = "select mingc,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj, \n"
				+ " 		kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar, \n"
				+ "         decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj, \n" 
				+ " 		decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk\n"
				+ "         from (\n"
				+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
				+ "       decode(gys.mingc,null,'�ܼ�',gys.mingc) as mingc,\n"
				+ "       j.fenx,\n"
				+ "       t.gongysb_id,\n"
				+ "       sum(j.jiesl) as jsl,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
				+ "\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
				+ "\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
				+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyfs,\n"
				+ "       0 gangzf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
				+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
				+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
				+ "       round(sum(j.meij * j.jiesl)/10000,2) mk\n"
				+ "  from yuejsbmdj j, yuetjkjb t, vwgongys gys\n"
				+ " where j.yuetjkjb_id = t.id\n"
				+ "   and t.gongysb_id = gys.id\n"
				+ "   and t.riq = to_date('"
				+ strDate
				+ "', 'yyyy-mm-dd')\n"
				+ "   and t.diancxxb_id = "
				+ getTreeid()
				+ "\n"
				+ " group by rollup(fenx,gys.mingc,t.gongysb_id)\n"
				+ "\n"
				+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
				+ "            or grouping(fenx)=1)\n"
				+ " order by xuh,fenx  )\n";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][19];
		ArrHeader[0] = new String[] { "��Ӧ��", "����<br>��<br>�ۼ�", "ú��(��)",
				"����<br>�ۺϼ�<br>(Ԫ/��)", "���<br>(Ԫ/��)", "��ֵ˰��<br>(Ԫ/��)",
				"����ǰ<br>���ӷ�<br>(Ԫ/��)", "��·�˷�<br>(Ԫ/��)", "��·˰��<br>(Ԫ/��)",
				"��·�ӷ�<br>(Ԫ/��)", "���˷�<br>(Ԫ/��)", "����˰��<br>(Ԫ/��)",
				"���ӷ�<br>(Ԫ/��)", "��վ�ӷ�<br>(Ԫ/��)", "��������<br>(Ԫ/��)",
				"��ֵ<br>Mj/kg", "��ú����<br>��˰<br>(Ԫ/��)", "��ú����<br>����˰<br>(Ԫ/��)",
				"ú��<br>(��Ԫ)" };
		ArrHeader[1] = new String[] { "��", "��", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17" };

		ArrWidth = new int[] { 100, 40, 60, 50, 50, 40, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 60 };
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// ����ҳ����
		rt.setTitle("��ú�۸������", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ08-1��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		// rt.body.setColFormat(23,"0.00");

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 2, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(18, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getDiaor08_1CW() {
		JDBCcon cn = new JDBCcon();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strMonth = getNianfValue().getValue() + "��"
				+ getYuefValue().getValue() + "��";

		String sql = "select  mingc,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj, \n" 
			+ "			kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar, \n"
			+ "         decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj, \n" 
			+ " 		decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk,sun,round(sl,2) \n"
			+ "         from (\n"
			+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
			+ "       decode(gys.mingc,null,'�ܼ�',gys.mingc) as mingc,\n"
			+ "       j.fenx,\n"
			+ "       t.gongysb_id,\n"
			+ "       sum(j.jiesl) as jsl,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
			+ "\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
			+ "\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
			+ "\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
			+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyfs,\n"
			+ "       0 gangzf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
			+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
			+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
			+ "       round(sum(j.meij * j.jiesl),2) mk,\n"
			+ "		sum(j.sunhzje) sun, \n"
			+ " 	(sum(j.sunhzje)/(sum(j.meij * j.jiesl)))* 100  sl \n"
			+ "  from yuejsbmdjcw j, yuetjkjb t, vwgongys gys\n"
			+ " where j.yuetjkjb_id = t.id\n"
			+ "   and t.gongysb_id = gys.id\n"
			+ "   and t.riq = to_date('"
			+ strDate
			+ "', 'yyyy-mm-dd')\n"
			+ "   and t.diancxxb_id = "
			+ getTreeid()
			+ "\n"
			+ " group by rollup(fenx,gys.mingc,t.gongysb_id)\n"
			+ "\n"
			+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
			+ "            or grouping(fenx)=1)\n"
			+ " order by xuh,fenx  )\n";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		// �����ͷ����
		String ArrHeader[][];
		int ArrWidth[];

		ArrHeader = new String[2][19];
		ArrHeader[0] = new String[] { "��Ӧ��", "����<br>��<br>�ۼ�", "ú��(��)",
				"����<br>�ۺϼ�<br>(Ԫ/��)", "���<br>(Ԫ/��)", "��ֵ˰��<br>(Ԫ/��)",
				"����ǰ<br>���ӷ�<br>(Ԫ/��)", "��·�˷�<br>(Ԫ/��)", "��·˰��<br>(Ԫ/��)",
				"��·�ӷ�<br>(Ԫ/��)", "���˷�<br>(Ԫ/��)", "����˰��<br>(Ԫ/��)",
				"���ӷ�<br>(Ԫ/��)", "��վ�ӷ�<br>(Ԫ/��)", "��������<br>(Ԫ/��)",
				"��ֵ<br>Mj/kg", "��ú����<br>��˰<br>(Ԫ/��)", "��ú����<br>����˰<br>(Ԫ/��)",
				"ú��<br>(��Ԫ)","����۽��<br>(Ԫ)","�����<br>(%)" };
		ArrHeader[1] = new String[] { "��", "��", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17","18","19" };

		ArrWidth = new int[] { 100, 40, 60, 50, 50, 40, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 80 ,50,50};
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);

		// ����ҳ����
		rt.setTitle("��ú�۸������", ArrWidth);
		rt.setDefaultTitle(1, 4, "���λ:" + getTianzdwQuanc(getTreeid()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 2, strMonth, Table.ALIGN_LEFT);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "��ȼ08-1��",
				Table.ALIGN_RIGHT);

		// ����
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = reportShowZero();

		rt.body.setColFormat(4, "0.00");
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		rt.body.setColFormat(14, "0.00");
		rt.body.setColFormat(15, "0.00");
		rt.body.setColFormat(16, "0.00");
		rt.body.setColFormat(17, "0.00");
		rt.body.setColFormat(18, "0.00");
		rt.body.setColFormat(19, "0.00");
		rt.body.setColFormat(20, "0.00");
		rt.body.setColFormat(21, "0.00");
		// rt.body.setColFormat(23,"0.00");

		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(9, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 2, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(18, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	private String getyueqfmk() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		JDBCcon con = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		Report rt = new Report();
		String rowTitleSql = "select decode(grouping(g.dqmc),1,'�ϼ�',g.dqmc) gys,q.fenx\n"
				+ "from vwgongys g,yueqfmkb q\n"
				+ "where q.gongysb_id = g.dqid\n"
				+ "group by rollup(q.fenx,g.dqmc) having not grouping(q.fenx) = 1\n"
				+ "order by grouping(g.dqmc) desc,max(g.xuh),q.fenx";

		String colTitleSql = "";

		String bodySql = "select decode(grouping(d.mingc),1,'�ϼ�',d.mingc) dianc,\n"
				+ "decode(grouping(g.dqmc),1,'�ϼ�',g.dqmc) gys,\n"
				+ "decode(grouping(q.fenx),1,'�ϼ�',q.fenx) fenx,\n"
				+ "sum(q.meil) \"ú��(��)\",sum(q.qiank) \"Ƿ��(��Ԫ)\"\n"
				+ " from yueqfmkb q,(select id, mingc, fuid, level as jib\n"
				+ "from diancxxb start with id= "
				+ getTreeid()
				+ "\n"
				+ "connect by fuid = prior id\n"
				+ "order SIBLINGS by xuh ) d, vwgongys g\n"
				+ "where q.gongysb_id = g.dqid and q.diancxxb_id = d.id\n"
				+ "and q.riq="
				+ strOraDate
				+ "\n"
				+ "group by cube(d.mingc,g.dqmc,q.fenx)";

		int jib = getJibbyDCID(con, getTreeid());
		if (jib == 3) {
			colTitleSql = "select d.mingc dianc from vwdianc d where d.id ="
					+ getTreeid();
		} else if (jib == 2) {
			colTitleSql = "select decode(grouping(d.mingc),1,'�ϼ�',d.mingc) dianc\n"
					+ "from vwdianc d\n"
					+ "where d.fuid = "
					+ getTreeid()
					+ "\n"
					+ "group by rollup(d.mingc) order by grouping(d.mingc) desc,max(d.xuh)";
		} else if (jib == 1) {
			colTitleSql = "select decode(grouping(d.mingc),1,'�ϼ�',d.mingc) dianc "
					+ "from diancxxb d where d.fuid ="
					+ getTreeid()
					+ "\n"
					+ "group by rollup(d.mingc) order by grouping(d.mingc) desc,max(d.xuh)";

			bodySql = "select decode(grouping(d.mingc),1,'�ϼ�',d.mingc) dianc,\n"
					+ "decode(grouping(g.dqmc),1,'�ϼ�',g.dqmc) gys,\n"
					+ "decode(grouping(q.fenx),1,'�ϼ�',q.fenx) fenx,\n"
					+ "sum(q.meil) \"ú��(��)\",sum(q.qiank) \"Ƿ��(��Ԫ)\"\n"
					+ " from yueqfmkb q,(select id, mingc, fuid, level as jib\n"
					+ "from diancxxb start with id= "
					+ getTreeid()
					+ "\n"
					+ "connect by fuid = prior id\n"
					+ "order SIBLINGS by xuh ) d, vwgongys g, vwdianc vd\n"
					+ "where q.gongysb_id = g.dqid and q.diancxxb_id = vd.id\n"
					+ "and vd.fuid = d.id\n"
					+ "and q.riq="
					+ strOraDate
					+ "\n"
					+ "group by cube(d.mingc,g.dqmc,q.fenx)";

		}
		ResultSetList rsl = con.getResultSetList(colTitleSql);
		int ArrWidth[] = new int[rsl.getRows() * 2 + 2];
		ArrWidth[0] = 100;
		ArrWidth[1] = 50;
		for (int i = 2; i < ArrWidth.length; i++) {
			ArrWidth[i] = 80;
		}
		cd.setRowNames("gys,fenx");
		cd.setColNames("dianc");
		cd.setDataNames("ú��(��),Ƿ��(��Ԫ)");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(rowTitleSql, colTitleSql, bodySql);

		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;

		rt.setTitle("��Ƿ��ú����Ϣ", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		// rt.setDefaultTitle(1, 2, "��λ:���",Table.ALIGN_LEFT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _SbClick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbClick = true;
	}

	private boolean _SqxgClick = false;

	public void SqxgButton(IRequestCycle cycle) {
		_SqxgClick = true;
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SbClick) {
			_SbClick = false;
			Shangb();
			getSelectData();
		}
		if (_SqxgClick) {
			_SqxgClick = false;
			Shenqxg();
			getSelectData();
		}
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
			getSelectData();
		}

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (cycle.getRequestContext().getParameter("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameter("lx"));
			init();
		}
		if (cycle.getRequestContext().getParameter("sb") != null) {
			visit.setString2(cycle.getRequestContext().getParameter("sb"));
		}
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			if (visit.getString1() == null || "".equals(visit.getString1())) {
				visit.setString1(RT_DR16);
			}
			if (visit.getString2() == null || "".equals(visit.getString2())) {
				visit.setString2(SB_No);
			}
			init();
		}
	}

	private void init() {
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel10(null);
		visit.setDropDownBean10(null);
		visit.setProSelectionModel3(null);
		visit.setDropDownBean3(null);
		visit.setDefaultTree(null);
		setDiancmcModel(null);
		setTreeid(visit.getDiancxxb_id() + "");
		paperStyle();
		getSelectData();
	}

	private String getMokmc() {
		if (getReportType().equals(RT_SL)) {
			return "";// SysConstant;
		} else if (getReportType().equals(RT_HC)) {
			return "";// getYuehcb();
		} else if (getReportType().equals(RT_ZL)) {
			return "";// getYuezlb();
		} else if (getReportType().equals(RT_DR01)) {
			return SysConstant.Diaor01b;
		} else if (getReportType().equals(RT_DR04)) {
			return SysConstant.Diaor04b;
		} else if (getReportType().equals(RT_DR16)) {
			return SysConstant.Diaor16b;
		} else if (getReportType().equals(RT_DR03)) {
			return SysConstant.Diaor03b;
		} else if (getReportType().equals(RT_DR08_1)) {
			return SysConstant.Diaor08b;
		} else if (getReportType().equals(RT_DR08)) {
			return "��ȼ08_1��";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			return "��ָ���";
		} else {
			return "";
		}
	}

	private String getBaseSql(boolean id) {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		String newid = "";
		if (id) {
			newid = "getnewid(" + getTreeid() + ") id,";
		}
		if (getReportType().equals(RT_DR16)) {
			return "select "
					+ newid
					+ strOraDate
					+ " riq,z.diancxxb_id,z.gongysb_id,\n"
					+ "       Z.DQID,\n" 
					+ "       Z.DQMC,\n" 
					+ "       SHENGFB.QUANC QUANC,\n" 
					+ "       DECODE(Z.JIHKJB_ID, 1, '�ص㶩��', 3, '�ص㶩��', J.MINGC) TJKJ,\n" 
					+ "       Z.JIHKJB_ID,\n" 
					+ "       SL.JINGZ+sl.yuns AS JINCML,\n" 
					+ "       z.pinzb_id,yunsfsb_id,\n"
					+ "       decode(zl.FENX,'����',HC.QICKC,GETQICKCLJ(z.ID))QICKC,\n"
					+ "       nvl(sl.biaoz, 0) as biaoz,\n"
					+ "       sl.yuns as yuns,\n"
					+ "       sl.kuid - sl.yingd as kuid,\n"
					+ "       nvl(zl.qnet_ar, 0) as farl,\n"
					+ "       nvl(zl.aad, 0) as huif,\n"
					+ "       nvl(zl.mt, 0) as shuif,\n"
					+ "       nvl(zl.vdaf, 0) as huiff,\n"
					+ "       hc.fady + hc.gongry + hc.qith + hc.sunh as hej,\n"
					+ "       hc.fady,\n"
					+ "       hc.gongry,\n"
					+ "       hc.qith,\n"
					+ "       hc.sunh,\n"
					+ "       hc.diaocl,\n"
					+ "       hc.panyk,\n"
					+ "       hc.shuifctz,\n"
					+ "		  ZL.*," 
					+ "       kuc\n"
					+ "  from (select * from yueslb) sl,\n"
					+ "       (select * from yuehcb) hc,\n"
					+ "       (select * from yuezlb) zl,\n"
					+ "       (select yuetjkjb.id, gongysb_id, pinzb_id, yunsfsb_id, diancxxb_id, '����' as fenx, JIHKJB_ID, DQID, DQMC\n"
					+ "          from yuetjkjb, VWGONGYSDQ DQ\n"
					+ "         where riq = "
					+ strOraDate
					+ "\n"
					+ " AND YUETJKJB.GONGYSB_ID = DQ.ID"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ "or diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid()
					+ "))\n"
					+ "        union\n"
					+ "        select yuetjkjb.id, gongysb_id, pinzb_id, yunsfsb_id, diancxxb_id, '�ۼ�' as fenx, JIHKJB_ID, DQID, DQMC\n"
					+ "          from yuetjkjb, VWGONGYSDQ DQ\n" + "         where riq = "
					+ strOraDate + "\n" 
					+ " AND YUETJKJB.GONGYSB_ID = DQ.ID"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ "or diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid() + "))) z,\n" 
					+"       JIHKJB J,\n" 
					+ "       MEIKDQB,\n" 
					+ "       SHENGFB\n" 
					+ " where z.id = sl.yuetjkjb_id(+)\n"
					+ "   and z.fenx = sl.fenx(+)\n"
					+ "   and z.id = hc.yuetjkjb_id(+)\n"
					+ "   and z.fenx = hc.fenx(+)\n"
					+ "   and z.id = zl.yuetjkjb_id(+)\n" 
					+ "   AND Z.JIHKJB_ID = J.ID\n" 
					+ "   AND Z.DQID = MEIKDQB.ID\n" 
					+ "   AND MEIKDQB.SHENGFB_ID = SHENGFB.ID"
					+ "   and z.fenx = zl.fenx(+)";
		} else if (getReportType().equals(RT_DR01)) {

			return "select "
					+ newid
					+ strOraDate
					+ " riq,"
					+ getTreeid()
					+ " as diancxxb_id,\n"
					+ "       jz.jizurl jjizrl,\n"
					+ "       nvl(fx.fenx, '') fenx,\n"
					+ "       sl.biaoz,\n"
					+ "       (hc.fady + hc.gongry + hc.qith + hc.sunh) mhxj,\n"
					+ "       hc.fady mfady,\n"
					+ "       hc.gongry mgongry,\n"
					+ "       hc.qith mqith,\n"
					+ "       hc.sunh msunh,\n"
					+ "       hc.kuc mkuc,\n"
					+ "       yo.shouyl,\n"
					+ "       (yo.fadyy + yo.gongry + yo.qithy + yo.sunh) yhxj,\n"
					+ "       yo.fadyy,\n"
					+ "       yo.gongry gongryy,\n"
					+ "       yo.qithy,\n"
					+ "       yo.sunh sunhy,\n"
					+ "       yo.kuc kucy,\n"
					+ "       zb.FADL,\n"
					+ "       zb.GONGRL,\n"
					+ "       zb.FADBZMH,\n"
					+ "       zb.GONGRBZMH,\n"
					+ "       decode(nvl(zb.fadl,0), 0, 0, round_new((nvl(yo.fadyy,0) * 2 + nvl(hc.fady,0))*100 / zb.fadl, 0)) fadtrmh,\n"
					+ "       decode(nvl(zb.GONGRL,0),0,0,round_new((nvl(yo.gongry,0) * 2 + nvl(hc.gongry,0)) *1000/ zb.GONGRL, 0)) gongrtrmh,\n"
					+ "       (nvl(zb.FADMZBML,0)+ nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0)) FADBZML ,\n"
					+ "       (nvl(zb.GONGRMZBML,0)+ nvl(zb.GONGRYZBZML,0)+ nvl(zb.GONGRQZBZML,0)) GONGRBZML,\n"
					+ "       decode((nvl(yo.fadyy,0) * 2 + nvl(hc.fady,0)),0,0,\n"
					+ "              round_new(nvl(nvl(zb.FADMZBML,0)+ nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0),0) * 7000 * 0.0041816 /(nvl(yo.fadyy,0) * 2 + nvl(hc.fady,0)),2)) zonghrlfrl,\n"
					+ "       decode(nvl(hc.fady,0),0,0,round_new((nvl(nvl(zb.FADMZBML,0)+ nvl(zb.FADYZBZML,0)+ nvl(zb.FADQZBZML,0),0) * 7000 - nvl(yo.fadyy,0) * 10000) * 0.0041816 / nvl(hc.fady,0), 2)) ranmrlfrl\n"
					+ "  from (select sum(biaoz) biaoz, fenx\n"
					+ "          from yueslb s, yuetjkjb t\n"
					+ "         where s.yuetjkjb_id = t.id\n"
					+ "           and t.riq = "
					+ strOraDate
					+ "\n"
					+ "           and (t.diancxxb_id = "
					+ getTreeid()
					+ " or t.diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid()
					+ "))\n"
					+ "         group by fenx) sl,\n"
					+ "       (select riq,fenx,sum(qickc) qickc,sum(shouml) shouml,sum(fady) fady,sum(gongry) gongry,sum(qith) qith,sum(sunh) sunh,\n"
					+ "         sum(diaocl) diaocl,sum(panyk) panyk,sum(kuc) kuc,sum(shuifctz) shuifctz from yueshchjb\n"
					+ "         where riq = "
					+ strOraDate
					+ "\n"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid()
					+ ")) group by riq,fenx) hc,\n"
					+ "       (select riq,fenx,round_new(sum(qickc),3) qickc,round_new(sum(shouyl),3) shouyl,round_new(sum(fadyy),3) fadyy,round_new(sum(gongry),3) gongry,round_new(sum(qithy),3) qithy,round_new(sum(sunh),3) sunh, \n"
					+ "         round_new(sum(diaocl),3) diaocl,round_new(sum(panyk),3) panyk,round_new(sum(kuc),3) kuc from yueshcyb\n"
					+ "         where riq = "
					+ strOraDate
					+ "\n"
					+ "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid()
					+ ")) group by riq,fenx) yo,\n"
					+ "       (select riq,fenx, sum(fadytrml) fadytrml,sum(gongrytrml) gongrytrml,sum(fadl) fadl,sum(gongrl) gongrl,\n"
					+ "         sum(FADMZBML) FADMZBML,sum(FADYZBZML) FADYZBZML,sum(FADQZBZML) FADQZBZML,sum(GONGRMZBML) GONGRMZBML,\n"
					+ "         sum(GONGRYZBZML) GONGRYZBZML,sum(GONGRQZBZML) GONGRQZBZML,\n"
					+ "         Round_New( DECODE((sum(FADL)) ,0,0,sum((FADMZBML+ FADYZBZML+ FADQZBZML))*100/sum((FADL))) ,0) FADBZMH,\n"
					+ "         Round_New(DECODE(sum(GONGRL),0,0, sum((GONGRMZBML+ GONGRYZBZML+ GONGRQZBZML))*1000/ sum(GONGRL)),0) GONGRBZMH \n"
					+ "         from yuezbb\n" + "         where riq = "
					+ strOraDate + "\n" + "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid() + ")) group by riq,fenx) zb,\n"
					+ "       (select sum(jizurl) jizurl\n"
					+ "          from jizb\n" + "         where toucrq <= "
					+ strOraDate + "\n" + "           and (diancxxb_id = "
					+ getTreeid()
					+ " or diancxxb_id in(select id from diancxxb where fuid="
					+ getTreeid() + "))) jz,\n" + "       (select '����' fenx\n"
					+ "          from dual\n" + "        union\n"
					+ "        select '�ۼ�' fenx from dual) fx\n"
					+ " where fx.fenx = hc.fenx(+)\n"
					+ "   and fx.fenx = yo.fenx(+)\n"
					+ "   and fx.fenx = zb.fenx(+)\n"
					+ "   and fx.fenx = sl.fenx(+)";
		} else if (getReportType().equals(RT_DR03)) {
			return "select "
					+ newid
					+ strOraDate
					+ " riq,t.diancxxb_id,t.gongysb_id,t.yunsfsb_id,\n"
					+ "       y.fenx,\n"
					+ "       y.biaoz jincl,\n"
					+ "       y.biaoz jianjl,\n"
					+ "       round_new(decode(y.biaoz, 0, 0, y.biaoz * 100 / y.biaoz), 2) chouclv,\n"
					+ "       round_new(decode(y.biaoz, 0, 0, y.biaoz * 100 / y.biaoz), 2) guohlv,\n"
					+ "       decode(y.biaoz,0,0,null,0,"
					+ "		(100 - round_new(decode(y.biaoz, 0, 0, y.biaoz * 100 / y.biaoz), 2))) jianclv,\n"
					+ "       y.yingd,\n"
					+ "       y.yingdzje,\n"
					+ "       y.kuid,\n"
					+ "       y.kuidzje,\n"
					+ "       y.suopsl,\n"
					+ "       y.suopje,\n"
					+ "       '' shuom\n"
					+ "  from yueslb y, yuetjkjb t\n"
					+ " where y.yuetjkjb_id = t.id\n"
					+ "   and t.riq = "
					+ strOraDate
					+ "\n"
					+ "   and (t.diancxxb_id = "
					+ getTreeid()
					+ " or t.diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid() + "))";

		} else if (getReportType().equals(RT_DR04)) {
			return "select "
					+ newid
					+ strOraDate
					+ " riq,t.diancxxb_id,t.gongysb_id, t.pinzb_id, t.yunsfsb_id,y.fenx, l.biaoz jincl, l.biaoz as jianjl,\n"
					+ "decode(l.jingz,0,0,round_new(l.jingz*100/l.jingz,2)) jianjlv,\n"
					+ "y.qnet_ar_kf, dengji(y.qnet_ar_kf) dengj_kf, y.mt_kf, y.aar_kf,\n"
					+ "y.vdaf_kf, y.std_kf, y.qnet_ar, dengji(y.qnet_ar) dengj, y.mt,\n"
					+ "y.aar, y.vdaf, y.std, dengji(y.qnet_ar) - dengji(y.qnet_ar_kf) dengjc,\n"
					+ "y.qnet_ar - y.qnet_ar_kf rezc,\n"
					+ "decode(l.biaoz,0,0,round_new(y.zhijbfml*100/l.biaoz,2)) zhijbflv,\n"
					+ "decode(y.zhijbfml,0,0,round_new(y.zhijbfje/y.zhijbfml,3)) danjc,\n"
					+ "y.zhijbfje,\n"
					+ "y.suopje,\n"
					+ "y.lsuopsl,\n"
					+ "y.lsuopje\n"
					+ "from yuezlb y, yueslb l, yuetjkjb t\n"
					+ "where y.yuetjkjb_id = t.id\n"
					+ "and l.yuetjkjb_id = t.id\n"
					+ "and l.fenx = y.fenx\n"
					+ "and t.riq = "
					+ strOraDate
					+ "\n"
					+ "and (t.diancxxb_id = "
					+ getTreeid()
					+ " or t.diancxxb_id in (select id from diancxxb where fuid="
					+ getTreeid() + "))";
		} else if (getReportType().equals(RT_DR08_1)) {
			return "select  "
					+ newid
					+ " riq,diancxxb_id,gongysb_id,1 yunsfsbid,fenx,jsl,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj, \n" 
					+ "			kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,\n"
					+ "         syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
					+ "			decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj, \n"
					+ " 		decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj,mk\n"
					+ "         from (\n"
					+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
					+ "       decode(gys.mingc,null,'�ܼ�',gys.mingc) as mingc,\n"
					+ "       j.fenx,\n"
					+ "       t.gongysb_id,\n"
					+ strOraDate
					+ " riq,max(t.diancxxb_id)diancxxb_id,"
					+ "       sum(j.jiesl) as jsl,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.kuangqyf)*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as daoczhj,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.meij*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as  kj,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.meijs*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as kjs,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.kuangqyf*j.jiesl))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as jhqyzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.yunjs*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlyfs,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,1,j.zaf*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as tlzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.yunjs*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as syfs,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,3,j.zaf*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as szf,\n"
					+ "\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qyf,\n"
					
					+ "       round_new(sum(decode(j.jiesl,0,0,decode(t.yunsfsb_id,2,j.yunj*j.jiesl,0)))\n"
					+ "                    /sum(decode(j.jiesl,0,1,j.jiesl)),2)* (select nvl(max(zhi),0.07) from xitxxb where xitxxb.mingc='�˷�˰��') as qyfs,\n"
			
					+ "       0 gangzf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.daozzf*j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as dzzf,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.qit*j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qtfy,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.qnet_ar * j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as qnet_ar,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.biaomdj * j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bmdj,\n"
					+ "       round_new(sum(decode(j.jiesl,0,0,j.buhsbmdj * j.jiesl))\n"
					+ "               /sum(decode(j.jiesl,0,1,j.jiesl)),2) as bhsbmj,\n"
					+ "       sum(j.meij * j.jiesl) mk\n"
					+ "  from yuejsbmdj j, yuetjkjb t, vwgongys gys\n"
					+ " where j.yuetjkjb_id = t.id\n"
					+ "   and t.gongysb_id = gys.id\n"
					+ "   and t.riq = to_date('"
					+ strDate
					+ "', 'yyyy-mm-dd')\n"
					+ "   and t.diancxxb_id = "
					+ getTreeid()
					+ "\n"
					+ " group by rollup(fenx,gys.mingc,t.gongysb_id)\n"
					+ "\n"
					+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
					+ "            or grouping(fenx)=1)and grouping(gys.mingc)=0\n"
					+ " order by xuh,fenx  )\n";
		} else if (getReportType().equals(RT_DR08)) {
			return "select  "
					+ newid
					+ " riq,diancxxb_id,gongysb_id,fenx,laiml,(kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy) daoczhj,\n"
					+ "        kj,kjs,jhqyzf,tlyf,tlyfs,tlzf,syf,syfs,szf,qyf,qyfs,gangzf,dzzf,qtfy,qnet_ar,\n"
					+ "		   decode(nvl(qnet_ar,0),0,0,round_new((kj+jhqyzf+tlyf+tlzf+syf+szf+qyf+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bmdj,\n"
					+ " 	   decode(nvl(qnet_ar,0),0,0,round_new((kj-kjs+jhqyzf+tlyf-tlyfs+tlzf+syf-syfs+szf+qyf-qyfs+gangzf+dzzf+qtfy)*29.271/qnet_ar,2)) bhsbmj \n"
					+ "        from (\n"
					+ "select decode(gys.mingc,null,0,avg(gys.xuh)) as xuh,\n"
					+ "       decode(gys.mingc,null,'�ܼ�',gys.mingc) as mingc,\n"
					+ "       sl.fenx,\n"
					+ "       t.gongysb_id,\n"
					+ strOraDate
					+ " riq,max(t.diancxxb_id)diancxxb_id,"
					+ "       sum(sl.biaoz) as laiml,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,(j.meij+j.yunj+j.zaf+j.daozzf+j.qit+j.jiaohqzf)*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as daoczhj,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.meij*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as  kj,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.meijs*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as kjs,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.jiaohqzf*sl.biaoz))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as jhqyzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunj*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.yunjs*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlyfs,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,1,j.zaf*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as tlzf,\n"
					+ "\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunj*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.yunjs*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as syfs,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,3,j.zaf*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as szf,\n"
					+ "\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunj*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,decode(t.yunsfsb_id,2,j.yunjs*sl.biaoz,0)))\n"
					+ "                    /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qyfs,\n"
					+ "       0 gangzf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.daozzf*sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as dzzf,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.qit*sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qtfy,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.qnet_ar * sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as qnet_ar,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.biaomdj * sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bmdj,\n"
					+ "       round_new(sum(decode(sl.biaoz,0,0,j.buhsbmdj * sl.biaoz))\n"
					+ "               /sum(decode(sl.biaoz,0,1,sl.biaoz)),2) as bhsbmj\n"
					+ "\n"
					+ "  from yuercbmdj j,yueslb sl,yuetjkjb t,vwgongys gys\n"
					+ " where j.yuetjkjb_id = t.id\n"
					+ "   and sl.yuetjkjb_id=t.id\n"
					+ "   and j.yuetjkjb_id=sl.yuetjkjb_id\n"
					+ "   and t.gongysb_id = gys.id\n"
					+ "   and j.fenx=sl.fenx\n"
					+ "   and t.riq = to_date('"
					+ strDate
					+ "', 'yyyy-mm-dd')\n"
					+ "   and t.diancxxb_id = "
					+ getTreeid()
					+ "\n"
					+ " group by rollup(sl.fenx,gys.mingc,t.gongysb_id)\n"
					+ "\n"
					+ " having not ((grouping(gys.mingc)=0 and grouping(t.gongysb_id)=1)\n"
					+ "            or grouping(sl.fenx)=1)and grouping(gys.mingc)=0\n"
					+ " order by xuh,sl.fenx)";
		} else {
			return "";
		}
	}

	private String getInsRptTableSql() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		String sql = "";
		if (getReportType().equals(RT_DR16)) {
			sql = "insert into diaor16bb (id,riq,diancxxb_id,gongysb_id,pinz,yunsfsb_id,"
					+ "fenx,shangyjc,kuangfgyl,yuns,kuid,farl,huif,shuif,huiff,shijhylhj,"
					+ "shijhylfdy,shijhylgry,shijhylqty,shijhylzcsh,diaocl,panypk,yuemjc)("
					+ getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR01)) {
			sql = "insert into diaor01bb (id,riq,diancxxb_id,fadsbrl,fenx,meitsg,meithyhj,"
					+ "meithyfd,meithygr,meithyqt,meithysh,meitkc,shiysg,shiyhyhj,shiyhyfd,"
					+ "shiyhygr,shiyhyqt,shiyhysh,shiykc,fadl,gongrl,biaozmhfd,biaozmhgr,"
					+ "tianrmhfd,tianrmhgr,biaozmlfd,biaozmlgr,zonghrl,zonghm) ("
					+ getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR03)) {
			sql = "insert into diaor03bb (id,riq,diancxxb_id,gongysb_id,yunsfsb_id,fenx,"
					+ "jincsl,choucsl,zhanjcm,guoh,jianc,yingdsl,yingdzje,kuid,kuidzje,"
					+ "suopsl,suopje,shuom) (" + getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR04)) {
			sql = "insert into diaor04bb (id,riq,diancxxb_id,gongysb_id,pinz,yunsfsb_id,fenx,"
					+ "jincsl,yanssl,jianzl,kuangffrl,kuangfdj,kuangfsf,kuangfhf,kuangfhff,kuangflf,"
					+ "changffrl,changfdj,changfsf,changfhf,changfhff,changflf,dengjc,"
					+ "rejc,bufl,danjc,zongje,suopje,liuspsl,liusp) ("
					+ getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_DR08_1)) {
			sql = "insert into diaor08bb (id,riq,diancxxb_id,gongysb_id,yunsfsb_id,fenx,"
					+ "meil,daoczhj,kuangj,zengzse,jiaohqyzf,tielyf,tieyse,tielzf,shuiyf,"
					+ "shuiyse,shuiyzf,qiyf,qiyse,gangzf,daozzf,qitfy,rez,biaomdj,buhsbmdj,"
					+ "meik) (" + getBaseSql(true) + ")";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			sql = "begin \n";
			for (int i = 2; i <= 35; i++) {
				sql += "insert into zhibwcqkb(id,xuh,riq,fenx,diancxxb_id,zhibmc,danwmc,benq,tongq,bianhqk,beiz)values\n"
						+ "(getnewid("
						+ getTreeid()
						+ "),"
						+ (i - 1)
						+ ","
						+ strOraDate
						+ ",'"
						+ rt_zhibb.body.getCellValue(i, 3)
						+ "',"
						+ getTreeid()
						+ ",'"
						+ rt_zhibb.body.getCellValue(i, 1)
						+ "','"
						+ rt_zhibb.body.getCellValue(i, 2)
						+ "',"
						+ ((rt_zhibb.body.getCellValue(i, 4).equals("")) ? "0"
								: rt_zhibb.body.getCellValue(i, 4))
						+ ","
						+ ((rt_zhibb.body.getCellValue(i, 5).equals("")) ? "0"
								: rt_zhibb.body.getCellValue(i, 5))
						+ ","
						+ ((rt_zhibb.body.getCellValue(i, 6).equals("")) ? "0"
								: rt_zhibb.body.getCellValue(i, 6))
						+ ",'"
						+ rt_zhibb.body.getCellValue(i, 7) + "');\n";
			}
			sql += "end ;";

		} else if (getReportType().equals(RT_DR08)) {
			sql = "insert into DIAOR08BB_NEW (id,riq,diancxxb_id,gongysb_id,fenx,"
					+ "meil,daoczhj,kuangj,zengzse,jiaohqyzf,tielyf,tieyse,tielzf,shuiyf,"
					+ "shuiyse,shuiyzf,qiyf,qiyse,gangzf,daozzf,qitfy,rez,biaomdj,buhsbmdj"
					+ ") (" + getBaseSql(true) + ")";
		}
		return sql;
	}

	private String getDelRptTableSql(String strOraDate) {
		String talbeName = "";
		if (getReportType().equals(RT_DR16)) {
			talbeName = "diaor16bb";
		} else if (getReportType().equals(RT_DR01)) {
			talbeName = "diaor01bb";
		} else if (getReportType().equals(RT_DR03)) {
			talbeName = "diaor03bb";
		} else if (getReportType().equals(RT_DR04)) {
			talbeName = "diaor04bb";
		} else if (getReportType().equals(RT_DR08_1)) {
			talbeName = "diaor08bb";
		} else if (getReportType().equals(RT_DR08)) {
			talbeName = "diaor08bb_new";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			talbeName = "zhibwcqkb";
		}
		String sql = "delete from " + talbeName + " where riq = " + strOraDate
				+ " and diancxxb_id=" + getTreeid();
		return sql;
	}

	private String getRenwmc() {
		String talbeName = "";
		if (getReportType().equals(RT_DR16)) {
			talbeName = "diaor16bb";
		} else if (getReportType().equals(RT_DR01)) {
			talbeName = "diaor01bb";
		} else if (getReportType().equals(RT_DR03)) {
			talbeName = "diaor03bb";
		} else if (getReportType().equals(RT_DR04)) {
			talbeName = "diaor04bb";
		} else if (getReportType().equals(RT_DR08_1)) {
			talbeName = "diaor08bb";
		} else if (getReportType().equals(RT_DR08)) {
			talbeName = "diaor08bb_new";
		} else if (getReportType().equals(RT_YUEZBWCQK)) {
			talbeName = "zhibwcqkyb";
		}
		return talbeName;
	}

	private void Shangb() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.getDelete(getDelRptTableSql(strOraDate));
		con.getUpdate(getInsRptTableSql());
		MainGlobal.Shujshcz(con, getTreeid(), strOraDate, "0", getMokmc(), v
				.getRenymc(), "");
		con.commit();
		con.Close();
		InterFac_dt senter = new InterFac_dt();
		senter.request(getRenwmc());
		setMsg("�ϱ����ݳɹ���");
	}

	private void Shenqxg() {
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strDate = getNianfValue().getValue() + "-" + YUEF + "-01";
		String strOraDate = DateUtil.FormatOracleDate(strDate);
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// con.getUpdate(getInsRptTableSql());
		MainGlobal.Shujshcz(con, getTreeid(), strOraDate, "0", getMokmc(), v
				.getRenymc(), getMiaos());
		con.Close();
		InterFac_dt senter = new InterFac_dt();
		senter.request(getRenwmc());
	}

	private boolean getDiancg() {
		JDBCcon con = new JDBCcon();
		String sqlq = "select id from diancxxb where fuid in(select id from diancxxb where id="
				+ getTreeid() + " and jib=3)";
		ResultSetList rs = con.getResultSetList(sqlq);
		if (rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		JDBCcon con = new JDBCcon();
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);

		if (SB_Yes.equals(visit.getString2())) {
			if (getDiancg()) {
				ToolbarButton tbsb = new ToolbarButton(null, "�ϱ�",
						"function(){document.getElementById('SbButton').click();}");
				tbsb.setIcon(SysConstant.Btn_Icon_SelSubmit);
				tb1.addItem(tbsb);
				ToolbarButton tbsq = new ToolbarButton(null, "�����޸�",
						"function(){Rpt_window.show();}");
				tbsq.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(tbsq);

				String YUEF = getYuefValue().getValue();
				if (Integer.parseInt(YUEF) < 10) {
					YUEF = "0" + YUEF;
				} else {
					YUEF = getYuefValue().getValue();
				}
				String strDate = getNianfValue().getValue() + "-" + YUEF
						+ "-01";
				String strOraDate = DateUtil.FormatOracleDate(strDate);
				String sql = "select zhuangt from shujshb where diancxxb_id = "
						+ getTreeid() + "\n" + "and mokmc = '" + getMokmc()
						+ "' and riq = " + strOraDate;
				ResultSetList rsl = con.getResultSetList(sql);
				if (rsl.next()) {
					if (rsl.getInt("zhuangt") == 1) {
						tbsb.setDisabled(true);
						tbsq.setDisabled(false);
					} else if (rsl.getInt("zhuangt") == 2) {
						tbsb.setDisabled(true);
						tbsq.setDisabled(true);
					} else if (rsl.getInt("zhuangt") == 0) {
						tbsb.setDisabled(false);
						tbsq.setDisabled(true);
					}
				} else {
					tbsb.setDisabled(false);
					tbsq.setDisabled(true);
				}
			}
		}
		setToolbar(tb1);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	// ���������
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�������
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	private int paperStyle;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='ֽ������' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
	}

	public int getJibbyDCID(JDBCcon con, String dcid) {
		int jib = 3;
		ResultSetList rsl = con
				.getResultSetList("select jib from diancxxb where id =" + dcid);
		if (rsl.next()) {
			jib = rsl.getInt("jib");
		}
		rsl.close();
		return jib;
	}
}