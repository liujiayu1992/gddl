package com.zhiren.shanxdted.huayrb;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ����:tzf
 * ʱ��:2009-07-07
 * ����:ú�ʼ����ձ�
 */
public class Meizjyrb extends BasePage implements PageValidateListener {

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

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public boolean getRaw() {
		return true;
	}

	// ����������
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	private String REPORT_NAME_MEIZJYRB = "Meizjyrb";// zhillsb�е�����

	private String REPORT_NAME_MEIZJYRB_ZHILB = "Meizjyrb_zhilb";// zhilb�е�����
	
	private String REPORT_NAME_MEIZJYRB_ZHILB_1="Meizjyrb_zhilb_1"; 
	
	private String REPORT_NAME_MEIZJYRB_ZHILB_hd="Meizjyrb_zhilb_hd"; 
	/**2008-10-18 huochaoyuan
	*����һ������ú���ɶ��������ŵ����������ԭ�ȱ�����ʾ�����⣬�������һ����ʽ�ı���
	*������getMeizjyrb_zhilb_1()
**/
	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	// private String leix = "";

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_MEIZJYRB)) {
			return getMeizjyrb();
		} else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB)) {
			return getMeizjyrb_zhilb();
		} else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_1)) {
			return getMeizjyrb_zhilb_1();
		} else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_hd)) {
			return getMeizjyrb_zhilb_hd();
		} else {
			return "�޴˱���";
		}
	}


	
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
	private String getMeizjyrb() {
		Visit v = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String s="";
		
		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//���� ����������;
		}
		// DateUtil custom = new DateUtil();
		String sql = "SELECT distinct a.BIANH,\n"
				+ "       a.MEIKDQMC,\n"
				+ "       a.MEIKDWMC,\n"
				+ "       a.PINZ,\n"
				+ "       a.FAZMC,\n"
				+ "       a.CHES,\n"
				+ "       a.JZSL,\n"
				+ "       round_new(z.mt, "+v.getMtdec()+") as QUANSF,\n"
				+ "       round_new(z.mad, 2) as KONGQGZJSF,\n"
				+ "       round_new(z.aad, 2) as KONGQGZJHF,\n"
				+ "        round_new(z.ad, 2) as GANZJHF,\n"
				+ "       round_new(z.aar, 2) as SHOUDJHF,\n"
				+ "       round_new(z.vad, 2) as KONGQGZJHFF,\n"
				+ "       round_new(z.vdaf, 2) as HUIFF,\n"
				+ "       round_new(z.qbad, "+v.getFarldec()+") * 1000 as DANTRL,\n"
				+ "       round_new(z.qnet_ar, "+v.getFarldec()+") * 1000 as FARL,\n"
				+ "       round_new(round_new(z.qnet_ar, "+v.getFarldec()+") * 7000 / 29.271, 0) as FARL1,\n"
				+ "       ROUND_NEW(z.sdaf,2) as SDAF,\n"
				+ "       round_new(z.std, 2) as GANZJL,round_new(z.stad, 2) as stad,round_new(z.stad*(100-z.mt)/(100-z.mad), 2) as star,\n"
				+ "       round_new(z.hdaf, 2) as HDAF,round_new(z.had, 2) as had,\n"
				
				+ "       round_new(z.fcad, 2) as FCAD,round_new(z.qgrd, 2) as QGRD,\n"
				
				+ "       TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD') AS CAIYSJ,\n"
				+ "       z.huayy,\n"
				+ "       y.lurry as caiyry from\n"
				+ "(select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,zm.bianm as bianh,\n"
				+ "                cz.mingc as fazmc,\n"
				+ "                p.mingc as pinz,\n"
				+ "                f.zhilb_id as zhilb_id,\n"
				+ "                sum(round_new(f.laimsl, "+v.getShuldec()+")) as jzsl,\n"
				+ "                f.ches\n"
				+ "  from fahb f, zhillsb z, meikxxb m, chezxxb cz, pinzb p,zhuanmb zm,zhuanmlb zb,gongysb g\n"
				+ " where z.zhilb_id = f.zhilb_id\n"
				
				+s
				
				+ "   and f.gongysb_id=g.id\n"
				+ "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id = cz.id\n"
				+ "   and f.meikxxb_id = m.id\n"
				+ "   and z.id=zm.zhillsb_id\n"
				+ "   and zm.zhuanmlb_id=zb.id\n"
				+ "   and zb.jib=3\n"
				+ "   and f.daohrq="
				+ DateUtil.FormatOracleDate(riq)
				+ " group by g.mingc,m.mingc, cz.mingc,zm.bianm,p.mingc,f.zhilb_id,f.ches) a,zhillsb z,yangpdhb y,fahb f\n"
				+ " where z.zhilb_id = a.zhilb_id\n"
				+"    and f.zhilb_id=z.zhilb_id\n"
				+ "   and y.zhilblsb_id = z.id\n" + "   and f.daohrq="
				+ DateUtil.FormatOracleDate(riq) + "\n" + "order by a.bianh";

		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][23];

		ArrHeader[0] = new String[] { "����<br>���", "������λ", "ú��λ����", "Ʒ��", "��վ",
				"����", "����<br>����<br>(��)", "ȫˮ<br>��(%)<br>Mt",
				"����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
				"����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad", "����<br>���ҷ�<br>(%)<br>Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)<br>Aar",
				"����<br>����<br>����<br>����<br>(%)<br>Vad",
				"����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
				"��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
				"�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
				"�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>Sdaf", "����<br>����<br>(%)<br>St,d","����<br>�������<br>(%)<br>St,ad","�յ�<br>����<br>(%)<br>St,ar",
				"����<br>�޻�<br>����<br>(%)<br>Hdaf","����<br>����<br>����<br>(%)<br>Had",
				"�̶�<br>̼<br>(%)<br>Fcad",
				"�ɻ�<br>��λ<br>��<br>(%)<br>Qgrd",
				
				"ȡ��<br>����", "����Ա", "����Ա" };
		int[] ArrWidth = new int[28];

		ArrWidth = new int[] { 50, 80, 80, 35, 45, 35, 35, 35, 35, 35, 35, 35,
				35, 35, 35, 35, 35, 35, 35, 35,35, 35, 35,35,35, 70, 45, 45 };

		rt.setTitle("ú  ��  ��  ��  ��  ��"
				+ ((getChangbValue().getId() > 0 && getChangbModel()
						.getOptionCount() > 2) ? "("
						+ getChangbValue().getValue() + ")" : ""), ArrWidth);
		rt.title.setRowHeight(2, 45);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 22);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "", "", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00","", "", "" };

		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(22);
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 28; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		// rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 4, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(24);

		return rt.getAllPagesHtml();

	}

	private String getMeizjyrb_zhilb() {
		Visit v = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String s="";
		
		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//���� ����������;
		}
		
		//���䵥λ
		String ysdw="";
		if(this.getMeikid()!=null && !this.getMeikid().equals("") && !this.getMeikid().equals("0")){
			ysdw=" where cp.yunsdwb_id="+this.getMeikid();
		}
		
		//ú��
		String meik="";
		String fahgl="";
		if(this.getTreeid()!=null && !this.getTreeid().equals("") && !this.getTreeid().equals("0")){
			meik=" where id="+this.getTreeid();
			fahgl=" and meikxxb_id="+this.getTreeid();
		}
		// DateUtil custom = new DateUtil();
		String sql =

		"SELECT distinct a.BIANH,\n"
				+ "       a.MEIKDQMC,\n"
				+ "       a.MEIKDWMC,\n"
				+" 		 (select ys.mingc from yunsdwb ys where ys.id in (select yunsdwb_id from chepb where fahb_id=f.id and rownum=1) ) mk,\n"
				+ "       a.PINZ,\n"
				+ "       a.FAZMC,\n"
				+ "       a.CHES,\n"
				+ "       a.JZSL,\n"
				+ "       round_new(z.mt, "+v.getMtdec()+") as QUANSF,\n"
				+ "       round_new(z.mad, 2) as KONGQGZJSF,\n"
				+ "       round_new(z.aad, 2) as KONGQGZJHF,\n"
				+ "        round_new(z.ad, 2) as GANZJHF,\n"
				+ "       round_new(z.aar, 2) as SHOUDJHF,\n"
				+ "       round_new(z.vad, 2) as KONGQGZJHFF,\n"
				+ "       round_new(z.vdaf, 2) as HUIFF,\n"
				+ "       round_new(z.qbad, "+v.getFarldec()+") * 1000 as DANTRL,\n"
				+ "       round_new(z.qnet_ar, "+v.getFarldec()+") * 1000 as FARL,\n"
				+ "       round_new(round_new(z.qnet_ar, "+v.getFarldec()+") * 7000 / 29.271, 0) as FARL1,\n"
				+ "       ROUND_NEW(z.sdaf , 2) AS SDAF,\n"
				+ "       round_new(z.std, 2) as GANZJL,round_new(z.stad, 2) as stad,round_new(z.stad*(100-z.mt)/(100-z.mad), 2) as star,\n"
				+ "       round_new(z.hdaf, 2) as HDAF,round_new(z.had, 2) as had,\n"
				+ "       round_new(z.fcad, 2) as FCAD,round_new(z.qgrd, 2) as QGRD,\n"
				+ "       TO_CHAR(z.huaysj, 'YYYY-MM-DD') AS CAIYSJ,\n"
				+ "       z.huayy,\n"
				+ "       y.lurry as caiyry from\n"
				+ "(select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,z.huaybh as bianh,\n"
				+ "                cz.mingc as fazmc,\n"
				+ "                p.mingc as pinz,\n"
				+ "                f.zhilb_id as zhilb_id,\n"
				+ "                sum(round_new(laimsl, "+v.getShuldec()+")) as jzsl,\n"
				+ "                f.ches\n"
				+ "  from (select * from fahb where id in ( select distinct cp.fahb_id from chepb cp "+ysdw+" ) "+fahgl+") f, zhilb z, (select * from meikxxb "+meik+") m, chezxxb cz, pinzb p,gongysb g\n"
				+ " where z.id = f.zhilb_id\n"
				
				
				+s
				
				
				+ "   and f.gongysb_id=g.id\n"
				+ "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id = cz.id\n"
				+ "   and f.meikxxb_id = m.id\n"
				+ "   and f.daohrq="
				+ DateUtil.FormatOracleDate(riq)
				+ "\n"
				+ " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id,f.ches) a,zhilb z,yangpdhb y,fahb f\n"
				+ " where a.zhilb_id=z.id\n" + " and z.caiyb_id=y.caiyb_id and f.zhilb_id=z.id\n"
				+ " and f.daohrq=" + DateUtil.FormatOracleDate(riq) + "\n"
				+ " order by a.bianh";

		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][23];

		ArrHeader[0] = new String[] { "����<br>���", "������λ", "ú��λ����", "���䵥λ","Ʒ��", "��վ",
				"����", "����<br>����<br>(��)", "ȫˮ<br>��(%)<br>Mt",
				"����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
				"����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad", "����<br>���ҷ�<br>(%)<br>Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)<br>Aar",
				"����<br>����<br>����<br>����<br>(%)<br>Vad",
				"����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
				"��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
				"�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
				"�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>Sdaf", "����<br>����<br>(%)<br>St,d","����<br>�������<br>(%)<br>St,ad","�յ�<br>����<br>(%)<br>St,ar",
				"����<br>�޻�<br>����<br>(%)<br>Hdaf","����<br>����<br>����<br>(%)<br>Had",
				"�̶�<br>̼<br>(%)<br>Fcad",
				"�ɻ�<br>��λ<br>��<br>(%)<br>Qgrd",
				"����<br>����", "����Ա", "����Ա" };
		int[] ArrWidth = new int[29];

		ArrWidth = new int[] { 50, 80, 80, 80,35, 45, 35, 35, 35, 35, 35, 35, 35,
				35, 35, 35, 35, 35, 35, 35, 35,35, 35, 35,35,35, 70, 45, 45 };

		rt.setTitle("ú  ��  ��  ��  ��  ��"
				+ ((getChangbValue().getId() > 0 && getChangbModel()
						.getOptionCount() > 2) ? "("
						+ getChangbValue().getValue() + ")" : ""), ArrWidth);
		rt.title.setRowHeight(2, 45);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "","", "", "", "", "", "0.00", "0.00",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "" };

		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 28; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		// rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 4, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(24);
		
		return rt.getAllPagesHtml();
	}

//

	private String getMeizjyrb_zhilb_1() {
		Visit v = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String s="";
		
		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//���� ����������;
		}
		
		
		// DateUtil custom = new DateUtil();
		String sql =

		"SELECT a.BIANH,\n"
				+ "       a.MEIKDQMC,\n"
				+ "       a.MEIKDWMC,\n"
				+ "       a.PINZ,\n"
				+ "       a.FAZMC,\n"
				+ "       a.CHES,\n"
				+ "       a.JZSL,\n"
				+ "       round_new(z.mt, "+v.getMtdec()+") as QUANSF,\n"
				+ "       round_new(z.mad, 2) as KONGQGZJSF,\n"
				+ "       round_new(z.aad, 2) as KONGQGZJHF,\n"
				+ "        round_new(z.ad, 2) as GANZJHF,\n"
				+ "       round_new(z.aar, 2) as SHOUDJHF,\n"
				+ "       round_new(z.vad, 2) as KONGQGZJHFF,\n"
				+ "       round_new(z.vdaf, 2) as HUIFF,\n"
				+ "       round_new(z.qbad, "+v.getFarldec()+") * 1000 as DANTRL,\n"
				+ "       round_new(z.qnet_ar, "+v.getFarldec()+") * 1000 as FARL,\n"
				+ "       round_new(round_new(z.qnet_ar, "+v.getFarldec()+") * 7000 / 29.271, 0) as FARL1,\n"
				+ "       ROUND_NEW(z.sdaf , 2) AS SDAF,\n"
				+ "       round_new(z.std, 2) as GANZJL,\n"
				+ "       round_new(z.hdaf, 2) as HDAF,\n"
				+ "       round_new(z.fcad, 2) as FCAD,round_new(z.qgrd, 2) as QGRD,\n"
				+ "       TO_CHAR(z.huaysj, 'YYYY-MM-DD') AS CAIYSJ,\n"
				+ "       z.huayy,\n"
				+ "       z.lury as caiyry from\n"
				+ "(select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,z.huaybh as bianh,\n"
				+ "                cz.mingc as fazmc,\n"
				+ "                p.mingc as pinz,\n"
				+ "                f.zhilb_id as zhilb_id,\n"
				+ "                sum(round_new(laimsl, "+v.getShuldec()+")) as jzsl,\n"
				+ "                f.ches\n"
				+ "  from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
				+ " where z.id = f.zhilb_id\n"
				
				+s
				
				+ "   and f.gongysb_id=g.id\n"
				+ "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id = cz.id\n"
				+ "   and f.meikxxb_id = m.id\n"
				+ "   and f.daohrq="
				+ DateUtil.FormatOracleDate(riq)
				+ "\n"
				+ " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id,f.ches) a,zhilb z,fahb f\n"
				+ " where a.zhilb_id=z.id\n"
				+ " and f.daohrq=" + DateUtil.FormatOracleDate(riq) + "\n"
				+ " order by a.bianh";

		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][25];

		ArrHeader[0] = new String[] { "����<br>���", "������λ", "ú��λ����", "Ʒ��", "��վ",
				"����", "����<br>����<br>(��)", "ȫˮ<br>��(%)<br>Mt",
				"����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
				"����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad", "����<br>���ҷ�<br>(%)<br>Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)<br>Aar",
				"����<br>����<br>����<br>����<br>(%)<br>Vad",
				"����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
				"��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
				"�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
				"�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>Sdaf", "����<br>����<br>(%)<br>St,d",
				"����<br>�޻�<br>����<br>(%)<br>Hdaf",
				"�̶�<br>̼<br>(%)<br>Fcad",
				"�ɻ�<br>��λ<br>��<br>(%)<br>Qgrd",
				"����<br>����", "����Ա", "¼��Ա" };
		int[] ArrWidth = new int[25];

		ArrWidth = new int[] { 50, 80, 80, 35, 45, 35, 35, 35, 35, 35, 35, 35,
				35, 35, 35, 35, 35, 35, 35, 35,35,35, 70, 45, 45 };

		rt.setTitle("ú  ��  ��  ��  ��  ��"
				+ ((getChangbValue().getId() > 0 && getChangbModel()
						.getOptionCount() > 2) ? "("
						+ getChangbValue().getValue() + ")" : ""), ArrWidth);
		rt.title.setRowHeight(2, 45);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "", "", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00", "", "", "" };

		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 25; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		// rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 5, "��ӡ����:" +DateUtil.FormatDate(new Date()) , Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(16, 4, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(24);
		
		return rt.getAllPagesHtml();
	}
	
	private String getMeizjyrb_zhilb_hd() {
		Visit v = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String s="";
		
		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//���� ����������;
		}
		
		String sql ="SELECT decode(grouping(f.daohrq)+grouping(a.meikdwmc),1,'����',to_char(f.daohrq,'yyyy-mm-dd')) as daohrq,\n"
			+ " a.meikdwmc,sum(a.JZSL) as jzsl,\n"
			+ " decode(sum(a.jzsl),0,0,sum(round_new(z.mt,"+v.getMtdec()+")*a.jzsl)/sum(a.jzsl)) as QUANSF,\n"
			+ " decode(sum(a.jzsl),0,0,sum(round_new(z.mad, 2)*a.jzsl)/sum(a.jzsl)) as KONGQGZJSF,\n"
			+ " decode(sum(a.jzsl),0,0,sum(round_new(z.aad,2)*a.jzsl)/sum(a.jzsl)) as KONGQGZJHF,\n"
			+ " decode(sum(a.jzsl),0,0,sum(round_new(z.vad,2)*a.jzsl)/sum(a.jzsl)) as KONGQGZJHFF,\n"
			+ " decode(sum(a.jzsl),0,0,sum(round_new(z.stad,2)*a.jzsl)/sum(a.jzsl)) as stad,\n"
			+ " decode(sum(a.jzsl),0,0,sum(round_new(z.qbad,"+v.getFarldec()+")*a.jzsl)/sum(a.jzsl)) as DANTRL,\n"
			+ " decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"+v.getFarldec()+")*a.jzsl)/sum(a.jzsl)) as FARL,\n"
			+ " round_new(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"+v.getFarldec()+")*a.jzsl)/sum(a.jzsl))*7000/29.271,0) as FARL1\n"
	        + " from\n"
	        + " (select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,z.huaybh as bianh,\n"
	        + "         cz.mingc as fazmc,\n"
	        + "         p.mingc as pinz,\n"
	        + "         f.zhilb_id as zhilb_id,\n"
	        + "         sum(round_new(laimsl,"+v.getShuldec()+")) as jzsl,\n"
	        + "         f.ches\n"
	        + " from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
	        + " where z.id = f.zhilb_id\n"
			+s
	        + " and f.gongysb_id=g.id\n"
	        + " and f.pinzb_id = p.id\n"
	        + " and f.faz_id = cz.id\n"
	        + " and f.meikxxb_id = m.id\n"
	        + " and f.daohrq="+ DateUtil.FormatOracleDate(riq)+"\n"
	        + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id,f.ches) a,zhilb z,fahb f\n"
	        + " where a.zhilb_id=z.id and f.zhilb_id=z.id\n"
	        + " and f.daohrq="+ DateUtil.FormatOracleDate(riq)
			+ " group by rollup(f.daohrq,a.MEIKDWMC)\n"
			+ " having not grouping(f.daohrq)=1";
		StringBuffer buffer = new StringBuffer();
		buffer.append(sql);
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		 String ArrHeader[][]=new String[1][11];
		 ArrHeader[0]=new String[] {"��úʱ��","���","ú��<br>T","ȫˮ��<br>Mt<br>(%)","�ոɻ�ˮ��Mad<br>(%)","�ҷ�<br>Aad<br>(%)","�ӷ���<br>Vad<br>(%)","ȫ��<br>St,ad<br>(%)","��Ͳ������<br>Qb,ad<br>(MJ/kg)","��λ������<br>Qnet,ar (MJ/kg)","����(cal)"};

		int[] ArrWidth = new int[11];
		ArrWidth=new int[] {80,60,60,60,60,60,60,60,60,60,60};

		rt.setTitle("ú  ��  ��  ��  ��  ��"
				+ ((getChangbValue().getId() > 0 && getChangbModel()
						.getOptionCount() > 2) ? "("
						+ getChangbValue().getValue() + ")" : ""), ArrWidth);
		rt.title.setRowHeight(2, 45);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "0","0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"};

		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 11; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		// rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
		// rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(24);
		
		return rt.getAllPagesHtml();
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
			getSelectData();
		}
	}
	
//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END----------
	
//	public String getTreeScript() {
//		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
//	}
//	
	
	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		

		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("daohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		
		
//ú��
		
		DefaultTree gystree=new DefaultTree();
		gystree.setTree_window(this.getMKSql().toString(), "gongysTree", visit.getDiancxxb_id()+"", "forms[0]", this.getTreeid(), this.getTreeid());
		visit.setDefaultTree(gystree);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("ú��:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		
		//���䵥λ--------------------
		
		DefaultTree mktree=new DefaultTree();
		mktree.setTree_window(this.getDTsql().toString(), "meikTree", visit.getDiancxxb_id()+"", "forms[0]", this.getMeikid(), this.getMeikid());
		this.setTree(mktree);
		
		TextField tfmk=new TextField();
		tfmk.setId("meikTree_text");
		tfmk.setWidth(100);
		tfmk.setValue(((IDropDownModel)this.getMeikModel()).getBeanValue(Long
				.parseLong(this.getMeikid() == null || "".equals(this.getMeikid()) ? "-1"
						: this.getMeikid())));
		
		

		ToolbarButton tb4 = new ToolbarButton(null, null,
				"function(){meikTree_window.show();}");
		tb4.setIcon("ext/resources/images/list-items.gif");
		tb4.setCls("x-btn-icon");
		tb4.setMinWidth(20);
		
		tb1.addText(new ToolbarText("���䵥λ"));
		tb1.addField(tfmk);
		tb1.addItem(tb4);
		
		tb1.addText(new ToolbarText("-"));
	//-------------------------------------------------------	
		
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	
	//------------���䵥λ----------

	private String meikid = "";
		public String getMeikid() {
			if (meikid==null || meikid.equals("")) {

				meikid = "0";
			}
			return meikid;
		}
		public void setMeikid(String meikid) {
			if(meikid!=null) {
				if(!meikid.equals(this.meikid)) {
					((TextField)getToolbar().getItem("meikTree_text")).setValue
					(((IDropDownModel)this.getMeikModel()).getBeanValue(Long.parseLong(meikid)));
					this.getTree().getTree().setSelectedNodeid(meikid);
				}
			}
			this.meikid = meikid;
		}
	
		
	
//	������䵥λ ���νṹsql
	private StringBuffer getDTsql(){
		
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'ȫ��' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from yunsdwb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
	}
	
	
	DefaultTree mktr;
	
	public DefaultTree getTree() {
		return mktr;
	}
	public void setTree(DefaultTree etu) {
		mktr=etu;
	}

	public String getTreeScriptMK() {
		return this.getTree().getScript();
	}
	
	


	public IPropertySelectionModel getMeikModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getMeikModels() {
		String sql = "select 0 id,'ȫ��' mingc from dual union select id,mingc  from yunsdwb";
		setMeikModel(new IDropDownModel(sql));
	}
	
	//-------------------------------------------------
	
	
//	��ȡú��
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	private StringBuffer getMKSql(){
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'ȫ��' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from meikxxb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		
		return bf;
	}
	public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
    public void getGongysDropDownModels() {
    	String sql="";
    	
    	sql+="  select 0 id, 'ȫ��' mingc from dual union select id ,mingc from meikxxb";
//        setGongysDropDownModel(new IDropDownModel(sql,"ȫ��")) ;
    	setGongysDropDownModel(new IDropDownModel(sql)) ;
        return ;
    }
	
	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		((DateField) getToolbar().getItem("daohrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString1("");
			setChangbValue(null);
			setChangbModel(null);
			this.setMeikid(null);
			this.setTreeid(null);
			this.getMeikModels();
			this.getGongysDropDownModels();
			this.getDiancmcModels();
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		// mstrReportName="diaor04bb";
		// if (mstrReportName.equals("Meizjyrb")) {
		// leix = "1";
		// } else if (mstrReportName.equals("Meizjyrb_zhilb")) {
		// leix = "2";
		// }
		blnIsBegin = true;
	}

//	ҳ���½��֤
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
