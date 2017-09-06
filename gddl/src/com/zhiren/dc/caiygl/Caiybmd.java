package com.zhiren.dc.caiygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-28 10:28
 * ���������Ӳ���ʾӯ���Ŀ��Ʋ���
 */
/**
 * huochaoyuan
 * 2010-01-16������ŵ糧���󣬻�ú����Ҫת�룬ֱ����ʾ�����ŵĲ�������
 * �ʶ�ԭ�л𳵲�������Ӳ������ƹ��ܣ�����Ϊҳ����Դ��Ӳ���jib(1,2,3)����1��2��3�����룬Ĭ��Ϊ��ʵ1�����룻 
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-20 18��06
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Caiybmd extends BasePage {

	private String REPORT_NAME_CAIYBMD_TIEL = "Tiel";// �𳵲�����

	private String REPORT_NAME_CAIYBMD_GONGL = "Gongl";// ����������

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

	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
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

	// �ⵥ������
	public IDropDownBean getHengdValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getHengdModel().getOptionCount() > 0) {
				setHengdValue((IDropDownBean) getHengdModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHengdValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getHengdModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setHengdModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHengdModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
	}

	public void setHengdModels() {
		// Visit visit = (Visit)this.getPage().getVisit();
		// JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		// sb.append("select zhi from xitxxb where mingc='���ⵥ��ӡʱ�䷶Χ' and
		// leib='����' and diancxxb_id="+visit.getDiancxxb_id());
		// ResultSetList rsl = con.getResultSetList(sb.toString());
		// String tians = "30";
		// if(rsl.next()) {
		// tians = rsl.getString("zhi");
		// }
		// sb.delete(0, sb.length());
		sb
				.append("select rownum as id, a.lursj as lursj\n"
						+ "  from (select distinct to_char(lursj, 'yyyy-mm-dd hh24:mi:ss') lursj\n"
						+ "          from chepb c, fahb f\n"
						+ "         where to_char(lursj, 'yyyy-mm-dd') = '"
						+ getRiq() + "'\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and f.yunsfsb_id = 1) a");
		// sb
		// .append(
		// "select rownum as id, a.lursj as lursj from (select distinct
		// to_char(lursj, 'yyyy-mm-dd hh24:mi:ss') lursj from chepb where
		// to_char(lursj, 'yyyy-mm-dd') ='")
		// .append(getRiq()).append("') a");
		setHengdModel(new IDropDownModel(sb.toString()));
	}
	
	private boolean isShowYingk(JDBCcon con){
		Visit visit = (Visit) getPage().getVisit();
		boolean isShowYingk = true;
		String sql = "select * from xitxxb where diancxxb_id =" + visit.getDiancxxb_id() 
			+ " and mingc='��������ʾӯ��' and zhuangt =1 and zhi = '��'" ;  
		isShowYingk = !con.getHasIt(sql);
		return isShowYingk;
	}

	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("�˵�¼��ʱ��:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("lursj");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		if (mstrReportName.equals(REPORT_NAME_CAIYBMD_TIEL)) {
			ComboBox hengdcb = new ComboBox();
			hengdcb.setTransform("HengdSelect");
			hengdcb.setWidth(130);
			hengdcb
					.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
			tb1.addField(hengdcb);
			tb1.addText(new ToolbarText("-"));
		}
		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_CAIYBMD_TIEL)) {
			return getTielcyd();
		} else if (mstrReportName.equals(REPORT_NAME_CAIYBMD_GONGL)) {
			return getGonglcyd();
		} else {
			return "�޴˱���";
		}
	}

	private String getTielcyd() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		long hengdid = -1;
		if (getHengdValue() != null) {
			hengdid = getHengdValue().getId();
		}
		// ����ʱ��
		String time = (hengdid > 0) ? getHengdValue().getValue()
				: "1990-01-01 23:00:00";
		sb
				.append("select distinct y.id from fahb f,caiyb y,\n")
				.append("(select * from chepb where lursj = ")
				.append(DateUtil.FormatOracleDateTime(time))
				.append(
						" ) c where f.id = c.fahb_id and f.zhilb_id = y.zhilb_id and f.yunsfsb_id = 1");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		String[][] bianms = new String[rsl.getRows()][2];
		while (rsl.next()) {
			bianms[rsl.getRow()][0] = rsl.getString("id");
			sb.delete(0, sb.length());
			sb
					.append(
							"select y.leib||':'||z.bianm bm from zhuanmb z,yangpdhb y where z.zhuanmlb_id = (select id from zhuanmlb where jib = "+visit.getInt1()+")")
					.append(
							" and y.zhilblsb_id = z.zhillsb_id and y.caiyb_id =")
					.append(rsl.getString("id"));
			ResultSetList rbm = con.getResultSetList(sb.toString());
			sb.delete(0, sb.length());
			while (rbm.next()) {
				sb.append(rbm.getString("bm")).append("\n");
			}
			bianms[rsl.getRow()][1] = sb.toString();
		}

		sb.delete(0, sb.length());
		sb
				.append("select b.caiybm, rownum as xuh, b.cheph, b.biaoz, b.yingd, b.kuid, b.caiysj, to_char(b.zhongcsj,'yyyy-mm-dd hh24:mi:ss') zcsj\n"
						+ "  from (select y.id caiybm,\n"
						+ "               c.cheph,\n"
						+ "               c.biaoz,\n");
						if(isShowYingk(con)){
							sb.append("               c.yingd,\n"
									+ "               c.yingd - c.yingk kuid,\n");
						}else{
							sb.append("0 as yingd, 0 as kuid,\n");
						}
						sb.append(
						"               '' as caiysj, c.zhongcsj\n"
						+ "          from fahb f,\n"
						+ "               caiyb y,\n"
						+ "               (select *\n"
						+ "                  from chepb\n"
						+ "                 where lursj =\n"
						+ DateUtil.FormatOracleDateTime(time)
						+ ") c\n"
						+ "         where f.id = c.fahb_id\n"
						+ "           and f.zhilb_id = y.zhilb_id\n"
						+ "           and f.yunsfsb_id = 1\n"
						+ "         order by c.xuh) b");
		rsl = con.getResultSetList(sb.toString());
		while (rsl.next()) {
			for (int i = 0; i < bianms.length; i++) {
				if (rsl.getString("caiybm").equals(bianms[i][0])) {
					rsl.setString("caiybm", bianms[i][1]);
					break;
				}
			}
		}
		rsl.beforefirst();
		String mingc="��������";
		if(visit.getInt1()==2){
			mingc="������";
		}else if (visit.getInt1()==3)
		{mingc="�������";
		}
		String[][] ArrHeader = new String[][] { { mingc, "���",
				Locale.cheph_chepb, Locale.biaoz_chepb, Locale.yingd_chepb,
				Locale.kuid_chepb, "����ʱ��", Locale.zhongcsj_chepb } };

		int[] ArrWidth = new int[] { 100, 50, 70, 60, 60, 60, 90, 120 };
		Report rt = new Report();
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("�� �� �� �� ��", ArrWidth);
		rt.setDefaultTitle(1, 5, "��ӡʱ�䣺" + DateUtil.FormatDateTime(new Date()),
				Table.ALIGN_LEFT);
		rt.setBody(new Table(rsl, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "������Ա��", Table.ALIGN_LEFT);
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;

	}

	private String getGonglcyd() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		String sql = "";
		String caiyy = "";
		sql = "select * from xitxxb where mingc ='�������Ƿ���ʾ����Ա' and leib='����' and zhuangt=1";
		ResultSetList rss = con.getResultSetList(sql);
		if(rss.next()){
			if(rss.getString("zhi").equals("��")){
				sql = "SELECT DISTINCT r.quanc FROM yangpdhb y,caiyryglb c,renyxxb r\n" +
				"WHERE c.yangpdhb_id = y.id AND c.renyxxb_id = r.id\n" + 
				"AND y.id IN (SELECT ID FROM YANGPDHB WHERE caiyb_id IN (SELECT DISTINCT Y.ID\n" + 
				"  FROM FAHB F,\n" + 
				"       CAIYB Y,\n" + 
				"       (SELECT *\n" + 
				"          FROM CHEPB\n" + 
				"         WHERE TO_CHAR(LURSJ, 'yyyy-mm-dd') = '"+getRiq()+"') C\n" + 
				" WHERE F.ID = C.FAHB_ID\n" + 
				"   AND F.ZHILB_ID = Y.ZHILB_ID\n" + 
				"   AND F.YUNSFSB_ID = 2))";
				ResultSetList rs = con.getResultSetList(sql);
				while(rs.next()){
					caiyy += rs.getString("quanc")+"  ";
				}
			}
		}

		sb
				.append("select distinct y.id from fahb f,caiyb y,\n")
				.append(
						"(select * from chepb where to_char(lursj,'yyyy-mm-dd') = '")
				.append(getRiq())
				.append(
						"' ) c where f.id = c.fahb_id and f.zhilb_id = y.zhilb_id and f.yunsfsb_id = 2");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		String[][] bianms = new String[rsl.getRows()][2];
		while (rsl.next()) {
			bianms[rsl.getRow()][0] = rsl.getString("id");
			sb.delete(0, sb.length());
			sb
					.append(
							"select y.leib||':'||z.bianm bm from zhuanmb z,yangpdhb y where z.zhuanmlb_id = (select id from zhuanmlb where jib = 1)")
					.append(
							" and y.zhilblsb_id = z.zhillsb_id and y.caiyb_id =")
					.append(rsl.getString("id"));
			ResultSetList rbm = con.getResultSetList(sb.toString());
			sb.delete(0, sb.length());
			while (rbm.next()) {
				sb.append(rbm.getString("bm")).append("\n");
			}
			bianms[rsl.getRow()][1] = sb.toString();
		}

		sb.delete(0, sb.length());
		sb
				.append("select b.caiybm, rownum as xuh, b.cheph, b.biaoz, b.yingd, b.kuid, b.caiysj, to_char(b.zhongcsj,'yyyy-mm-dd hh24:mi:ss') zcsj\n"
						+ "  from (select y.id caiybm,\n"
						+ "               c.cheph,\n"
						+ "               c.biaoz,\n");
						if(isShowYingk(con)){
							sb.append("               c.yingd,\n"
									+ "               c.yingd - c.yingk kuid,\n");
						}else{
							sb.append("0 as yingd, 0 as kuid,\n");
						}
						sb.append(
						 "               '' as caiysj, c.zhongcsj\n"
						+ "          from fahb f,\n"
						+ "               caiyb y,\n"
						+ "               (select *\n"
						+ "                  from chepb\n"
						+ "                 where to_char(lursj,'yyyy-mm-dd') ='"
						+ getRiq()
						+ "') c\n"
						+ "         where f.id = c.fahb_id\n"
						+ "           and f.zhilb_id = y.zhilb_id\n"
						+ "           and f.yunsfsb_id = 2\n"
						+ "         order by y.id,c.xuh) b");
		rsl = con.getResultSetList(sb.toString());
		while (rsl.next()) {
			for (int i = 0; i < bianms.length; i++) {
				if (rsl.getString("caiybm").equals(bianms[i][0])) {
					rsl.setString("caiybm", bianms[i][1]);
					break;
				}
			}
		}
		rsl.beforefirst();
		String[][] ArrHeader = new String[][] { { "��������", "���",
				Locale.cheph_chepb, Locale.biaoz_chepb, Locale.yingd_chepb,
				Locale.kuid_chepb, "����ʱ��", Locale.zhongcsj_chepb } };

		int[] ArrWidth = new int[] { 100, 50, 70, 60, 60, 60, 90, 120 };
		Report rt = new Report();
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("�� �� �� �� ��", ArrWidth);
		rt.setDefaultTitle(1, 5, "��ӡʱ�䣺" + DateUtil.FormatDateTime(new Date()),
				Table.ALIGN_LEFT);
		rt.setBody(new Table(rsl, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		String strBianm = "";
		int xuh = 0;
		for(int i=1;i<rt.body.getRows();i++){
			if(! rt.body.getCellValue(i+1, 1).equals(strBianm)){
				strBianm = rt.body.getCellValue(i+1, 1);
				xuh = 1;
			}
			rt.body.setCellValue(i+1, 2, ""+xuh++);
		}
		
		rt.body.setPageRows(rt.PAPER_ROWS);
		//	���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "������Ա��"+caiyy, Table.ALIGN_LEFT);
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;

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
		((DateField) getToolbar().getItem("lursj")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}

	// ҳ���ʼ��
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setString1(null);
			visit.setInt1(1);
			setRiq(DateUtil.FormatDate(new Date()));
			setHengdValue(null);
			setHengdModel(null);
			
			//begin��������г�ʼ������
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ

		}
		if (riqchange) {
			riqchange = false;
			setHengdValue(null);
			setHengdModel(null);
			setTbmsg(null);
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
			if(cycle.getRequestContext().getParameter("jib")==null){
				visit.setInt1(1);
			}else visit.setInt1(Integer.parseInt(cycle.getRequestContext().getParameter("jib")));
			getSelectData();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		blnIsBegin = true;
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
