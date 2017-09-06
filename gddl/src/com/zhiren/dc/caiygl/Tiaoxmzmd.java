package com.zhiren.dc.caiygl;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Tiaoxmzmd extends BasePage {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	public boolean getRaw() {
		return true;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		setToolbar(tb1);
	}


	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select t.samplingcode,cy.caiybm,cy.zhiybm from(\n" 
				+ " select distinct c.caiybm, z.zhiybm\n"
				+ "  from (select bianm as caiybm, zhillsb_id\n"
				+ "          from zhuanmb\n"
				+ "         where zhillsb_id in\n"
				+ "               (select zm.zhillsb_id as id\n"
				+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
				+ "                 where zm.zhuanmlb_id = lb.id\n"
				+ "                   and lb.jib = 1\n"
				+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
				+ "                   and f.zhilb_id = z.zhilb_id\n"
				+ "                   and z.id = zm.zhillsb_id)\n"
				+ "           and zhuanmlb_id =\n"
				+ "               (select id from zhuanmlb where mingc = '��������')) c,\n"
				+ "       (select bianm as zhiybm, zhillsb_id\n"
				+ "          from zhuanmb\n"
				+ "         where zhillsb_id in\n"
				+ "               (select zm.zhillsb_id as id\n"
				+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
				+ "                 where zm.zhuanmlb_id = lb.id\n"
				+ "                   and lb.jib = 3\n"
				+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
				+ "                   and f.zhilb_id = z.zhilb_id\n"
				+ "                   and z.id = zm.zhillsb_id)\n"
				+ "           and zhuanmlb_id =\n"
				+ "               (select id from zhuanmlb where mingc = '��������')) z,\n"
				+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
				+ "          from zhillsb z, fahb f, chepb c\n"
				+ "         where f.zhilb_id = z.zhilb_id\n"
				+ "           and c.fahb_id = f.id\n"
				+ "           and to_date('" + getRiq()
				+ "', 'yyyy-mm-dd') + 1 > c.zhongcsj \n"
				+ "           and to_date('" + getRiq()
				+ "', 'yyyy-mm-dd') <= c.zhongcsj) s\n"
				+ " where c.zhillsb_id = z.zhillsb_id\n"
				+ "   and c.zhillsb_id = s.zid\n"
				+ "   and z.zhillsb_id = s.zid\n" 
				+ " \n" + Jilcz.filterDcid(visit, "s")
				+ " ) cy, v_Sampling t  where cy.caiybm=t.samcode(+) order by caiybm,zhiybm");
		ResultSet rs = con.getResultSet(sbsql.toString());
		
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		
		Report rt = new Report();
		
		String ArrHeader[][] = new String[1][3];
		ArrHeader[0] = new String[] { "������","��������", "��������" };

		int ArrWidth[] = new int[] { 100, 100, 100};

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), 
				((Visit) this.getPage().getVisit()).getString2());// ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
		rt.setTitle("��������ת��������", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 3));

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(38);
		// ���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 1, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(2, 1, "�Ʊ�:", Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	
//	������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			// begin��������г�ʼ������
			visit.setString2(null);
			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {
				visit.setString2(pagewith);
			}
		}
		getSelectData();
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
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
	
	public static String filterDcid(Visit v, String Prefix) {
		Prefix = Prefix == null || "".equals(Prefix) ? "" : Prefix + ".";
		String sqltmp = " and  " + Prefix + "diancxxb_id = "
				+ v.getDiancxxb_id();
		if (v.isFencb()) {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con
					.getResultSetList("select id from diancxxb where fuid="
							+ v.getDiancxxb_id());
			sqltmp = "";
			while (rsl.next()) {
				sqltmp += "or " + Prefix + "diancxxb_id = "
						+ rsl.getString("id") + " ";
			}
			sqltmp = " and (" + sqltmp.substring(2) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}
	
}
