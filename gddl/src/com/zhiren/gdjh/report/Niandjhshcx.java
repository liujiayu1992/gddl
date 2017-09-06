package com.zhiren.gdjh.report;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Niandjhshcx extends BasePage implements PageValidateListener {

	public void submit(IRequestCycle cycle) {

	}
	
	// ����չʾ
	public String getPrintTable() {
		return getCgjh()+getZfjh()+getZhib();
	}

	private String getCgjh() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		Visit visit = (Visit) getPage().getVisit();
		String intyear= visit.getString3();
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String dcid=visit.getString4();

		String strSQL =
			"SELECT GHDW,JIHKJ,HET_SL,HET_REZ,HET_MEIJ,HET_YUNJ,JIH_SL,JIH_REZ,JIH_REZC,JIH_MEIJ,JIH_MEIJBHS,JIH_YUNJ,\n" +
			"JIH_YUNJBHS,JIH_ZAF,JIH_ZAFBHS,JIH_QIT,JIH_QITBHS,(JIH_MEIJ+JIH_YUNJ+JIH_ZAF+JIH_QIT)JIH_DAOCJ," +
			"DECODE(JIH_REZ,0,0,ROUND((JIH_MEIJ+JIH_YUNJ+JIH_ZAF+JIH_QIT)*29.271/JIH_REZ,2))JIH_DAOCBMDJ\n" +
			"FROM ("+
			"SELECT DECODE(GROUPING(G.MINGC), 1, '<b>�ܼ�</b>', G.MINGC) GHDW, J.MINGC JIHKJ,\n" +
			"      SUM(CG.HET_SL) HET_SL,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_REZ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_REZ,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_MEIJ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_MEIJ,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_YUNJ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_YUNJ,\n" + 
			"      SUM(CG.JIH_SL) JIH_SL,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_REZ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_REZ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM((CG.JIH_REZ - NVL(ZB.RUCRLRZC, 0)) * CG.JIH_SL) /SUM(CG.JIH_SL)),2) JIH_REZC,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_MEIJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_MEIJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_MEIJBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_MEIJBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_YUNJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_YUNJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_YUNJBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_YUNJBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_ZAF * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_ZAF,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_ZAFBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_ZAFBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_QIT * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_QIT,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_QITBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_QITBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_DAOCJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_DAOCJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_DAOCBMDJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_DAOCBMDJ\n" + 
			"  FROM NIANDJH_CAIG CG, GONGYSB G, NIANDJH_ZHIB ZB, JIHKJB J\n" + 
			" WHERE CG.GONGYSB_ID = G.ID AND CG.JIHKJB_ID=J.ID\n" + 
			"   AND CG.DIANCXXB_ID = ZB.DIANCXXB_ID\n" + 
			"   AND CG.RIQ = ZB.RIQ\n" + 
			"   AND CG.RIQ = "+curdate+"\n" + 
			"   AND CG.DIANCXXB_ID="+dcid+"\n" + 
			" GROUP BY ROLLUP((G.MINGC,J.MINGC)))";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 4, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[4][19];
		ArrHeader[0] = (new String[] { "������λ","�ƻ��ھ�", intyear+"���ͬ��", intyear+"���ͬ��", intyear+"���ͬ��", intyear+"���ͬ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��"});
		ArrHeader[1] = (new String[] { "������λ","�ƻ��ھ�", "����", "��ֵ", "�����<br>(��˰)", "�˷�<br>(��˰)","������","��ֵ","����¯��ֵ","�����","�����<br>(����˰)","�˷�","�˷�<br>(����˰)","�ӷ�","�ӷ�<br>(����˰)","����","����<br>(����˰)","������","������<br>ú����" });
		ArrHeader[2] = (new String[] { "������λ","�ƻ��ھ�", "��","�׽�/ǧ��", "Ԫ/��", "Ԫ/��","��","�׽�/ǧ��","�׽�/ǧ��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��"});
		ArrHeader[3] = (new String[] { "��","��", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16","17"});
		
		ArrWidth = (new int[] { 130,60,70,60,60,60,70,60,60,60,60,60,60,60,60,60,60,60,60 });
		String rptitle="��һ��"+intyear+"��ú̿�ɹ��ƻ�(���С��ӷѡ���ָú̿������ͬ��Լ������ú����˷�һͬ����ķ��ã������װ����վ̨�����ӵȷ��ã�һƱ����ĳ���)";
		rt.setTitle("", ArrWidth);

		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 3, 1);
		rt.body.merge(1, 2, 3, 2);
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getZfjh() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		Visit visit = (Visit) getPage().getVisit();
		long intyear= Long.parseLong(visit.getString3());
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String dcid=visit.getString4();
		String strSQL =
			"SELECT DECODE(GROUPING(ZAFMC), 1, '<b>�ܼ�</b>', ZAFMC) ZAFMC,\n" +
			"       SUM(YUCJE),\n" + 
			"       YUCSM,\n" + 
			"       SUM(SHIJWCJE),\n" + 
			"       SUM(YUJWCJE),\n" + 
			"       YUJWCSM\n" + 
			"  FROM NIANDJH_ZAF\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+dcid+"\n" + 
			" GROUP BY ROLLUP((ZAFMC, YUCSM, YUJWCSM))";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 2, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[2][6];
		ArrHeader[0] = (new String[] { "��������", intyear+"��Ԥ��", intyear+"��Ԥ��", (intyear-1)+"��Ԥ�����", (intyear-1)+"��Ԥ�����",(intyear-1)+"��Ԥ�����"});
		ArrHeader[1] = (new String[] { "��������", "Ԥ��(Ԫ)", "˵��", "ʵ�����(Ԫ)", "Ԥ��(Ԫ)","˵��"});
		
		ArrWidth = (new int[] { 170,120,440,120,120,260});
		String rptitle="���:����ȼ�ϳɱ����������ã�ȼ���ӷѣ�";
		rt.setTitle("", ArrWidth);
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
		rt.body.mergeFixedRowCol();
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getZhib() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		Visit visit = (Visit) getPage().getVisit();
		String intyear= visit.getString3();
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String dcid=visit.getString4();
		
		String strSQL ="SELECT DECODE(0, 0, '�ϼ�') QIB,\n" +
			"       ZB.FADL,\n" + 
			"       ZB.GONGDMH,\n" + 
			"       ZB.FADCYDL,\n" + 
			"       ZB.FADBML,\n" + 
			"       ZB.GONGRL,\n" + 
			"       ZB.GONGRMH,\n" + 
			"       ZB.GONGRBML,\n" + 
			"       ZB.BIAOMLHJ,\n" + 
			"       ZB.MEIZBML,\n" + 
			"       ZB.MEIZBMDJ,\n" + 
			"       ZB.RANYL, \n" +
			"		ZB.YOUZBML,\n" + 
			"       ZB.RANYDJ,\n" + 
			"       ZB.YOUZBMDJ,\n" + 
			"       ZB.QITFY,\n" + 
			"       ZB.RLZHBMDJ\n" + 
			"  FROM NIANDJH_ZHIB ZB\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+dcid+"";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[3][17];
		ArrHeader[0] = (new String[] { "�ڱ�", "������", "����ú��", "���糧<br>�õ���", "�����ú��","������","����ú��","���ȱ�ú��","��ú���ϼ�","ú�۱�ú��","ú��<br>��ú����","ȼ����","����<br>��ú��","ȼ�͵���<br>(����˰)","����<br>��ú����","��������","��¯�ۺϱ�ú����"});
		ArrHeader[1] = (new String[] { "�ڱ�", "��ǧ��ʱ","��/ǧ��ʱ", "%", "��","�򼪽�","ǧ��/����","��","��","��","Ԫ/��","��","��","Ԫ/��","Ԫ/��","Ԫ","Ԫ/��"});
		ArrHeader[2] = (new String[] { "��", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16"});
		
		ArrWidth = (new int[] { 60,80,70,60,80,80,70,80,80,80,60,80,70,70,70,80,60});
		String rptitle="����:"+intyear+"�����ָ��Ԥ��";
		rt.setTitle("", ArrWidth);
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 2, 1);
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArrWidth);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// ***************************�����ʼ����***************************//
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

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

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

	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
//			visit.setActivePageName(getPageName().toString());
			visit.setString3("");
			visit.setString4("");
//			ͨ�������������ں͵糧��ʶ
			if(cycle.getRequestContext().getParameter("lx") != null){
				visit.setString3(cycle.getRequestContext().getParameter("lx"));
			}
			if(cycle.getRequestContext().getParameter("dc") != null){
				visit.setString4(cycle.getRequestContext().getParameter("dc"));
			}
			if(visit.getString3()==null || visit.getString3().equals("")){
				visit.setString3(DateUtil.getYear(new Date()) + "");
			}
			if(visit.getString4()==null || visit.getString4().equals("")){
				visit.setString4(visit.getDiancxxb_id()+"");
			}
			
			visit.setList1(null);
		}
		
	}
	
}