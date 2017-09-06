package com.zhiren.dc.monthReport_mt;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ߣ�����
 * ʱ�䣺2010-08-25
 * ��������ͷ�糧�ֿ��պĴ��ձ�
 */

public class FenkshcReport extends BasePage {
	private static final String BAOBPZB_GUANJZ = "FENKSHCRB";// baobpzb�ж�Ӧ�Ĺؼ���
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

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

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

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String MonthFirstDate = "";
		if(rq.length==3){
			MonthFirstDate = DateUtil.FormatOracleDate(rq[0]+"-"+rq[1]+"-01");
		}
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		String sql = 
			"select meikxxb_id,yuejh,shangrkc,dangrlm,leijlm,dangrhm,leijhm,dangrkc,beiz\n" +
			"from\n" + 
			"((select '�ص㶩��' as jihkjb_id,\n" + 
			"       g.mingc as gongysb_id,\n" + 
			"       decode(grouping(g.mingc)+grouping(m.mingc),2,'�ص��ͬ��ϼ�',1,'&nbsp;&nbsp;'||g.mingc||'С��','&nbsp;&nbsp;&nbsp;&nbsp;'||m.mingc) as meikxxb_id,\n" + 
			"       sum(nvl(bt.yuejh, 0)) as yuejh,\n" + 
			"       sum(nvl(zr.kuc, 0)) as shangrkc,\n" + 
			"       sum(nvl(bt.laim, 0)) as dangrlm,\n" + 
			"       sum(nvl(lj.laim, 0)) as leijlm,\n" + 
			"       sum(nvl(bt.haom, 0)) as dangrhm,\n" + 
			"       sum(nvl(lj.haom, 0)) as leijhm,\n" + 
			"       sum(nvl(bt.kuc, 0)) as dangrkc,\n" + 
			"       '' as beiz\n" + 
			"  from gongysb g,\n" + 
			"       meikxxb m,\n" + 
			"       (select f.jihkjb_id,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               f.yuejh,\n" + 
			"               f.laim,\n" + 
			"               f.haom,\n" + 
			"               f.kuc,\n" + 
			"               f.beiz\n" + 
			"          from fenkshcrbb_mt f\n" + 
			"         where f.riq = "+CurrODate+") bt,\n" + 
			"\n" + 
			"       (select f.gongysb_id, f.meikxxb_id, f.kuc\n" + 
			"          from fenkshcrbb_mt f\n" + 
			"         where f.riq = ("+CurrODate+" - 1)) zr,\n" + 
			"\n" + 
			"       (select f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               sum(f.laim) as laim,\n" + 
			"               sum(f.haom) as haom\n" + 
			"          from fenkshcrbb_mt f\n" + 
			"         where f.riq >= "+MonthFirstDate+"\n" + 
			"           and f.riq <= "+CurrODate+"\n" + 
			"         group by f.gongysb_id, f.meikxxb_id) lj\n" + 
			" where bt.gongysb_id = g.id\n" + 
			"   and bt.meikxxb_id = m.id\n" + 
			"   and bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"   and bt.meikxxb_id = zr.meikxxb_id(+)\n" + 
			"   and bt.gongysb_id = lj.gongysb_id(+)\n" + 
			"   and bt.meikxxb_id = lj.meikxxb_id(+)\n" + 
			"   and bt.jihkjb_id = 1\n" + 
			" group by rollup (g.mingc,m.mingc)\n" + 
			" )\n" + 
			"\n" + 
			" union all\n" + 
			"\n" + 
			" (select '�г��ɹ�' as jihkjb_id,\n" + 
			"       g.mingc as gongysb_id,\n" + 
			"       decode(grouping(g.mingc)+grouping(m.mingc),2,'�г��Թ��ϼ�',1,g.mingc,m.mingc) as meikxxb_id,\n" + 
			"       sum(nvl(bt.yuejh, 0)) as yuejh,\n" + 
			"       sum(nvl(zr.kuc, 0)) as shangrkc,\n" + 
			"       sum(nvl(bt.laim, 0)) as dangrlm,\n" + 
			"       sum(nvl(lj.laim, 0)) as leijlm,\n" + 
			"       sum(nvl(bt.haom, 0)) as dangrhm,\n" + 
			"       sum(nvl(lj.haom, 0)) as leijhm,\n" + 
			"       sum(nvl(bt.kuc, 0)) as dangrkc,\n" + 
			"       '' as beiz\n" + 
			"  from gongysb g,\n" + 
			"       meikxxb m,\n" + 
			"       (select f.jihkjb_id,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               f.yuejh,\n" + 
			"               f.laim,\n" + 
			"               f.haom,\n" + 
			"               f.kuc,\n" + 
			"               f.beiz\n" + 
			"          from fenkshcrbb_mt f\n" + 
			"         where f.riq = "+CurrODate+") bt,\n" + 
			"\n" + 
			"       (select f.gongysb_id, f.meikxxb_id, f.kuc\n" + 
			"          from fenkshcrbb_mt f\n" + 
			"         where f.riq = ("+CurrODate+" - 1)) zr,\n" + 
			"\n" + 
			"       (select f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               sum(f.laim) as laim,\n" + 
			"               sum(f.haom) as haom\n" + 
			"          from fenkshcrbb_mt f\n" + 
			"         where f.riq >= "+MonthFirstDate+"\n" + 
			"           and f.riq <= "+CurrODate+"\n" + 
			"         group by f.gongysb_id, f.meikxxb_id) lj\n" + 
			" where bt.gongysb_id = g.id(+)\n" + 
			"   and bt.meikxxb_id = m.id(+)\n" + 
			"   and bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"   and bt.meikxxb_id = zr.meikxxb_id(+)\n" + 
			"   and bt.gongysb_id = lj.gongysb_id(+)\n" + 
			"   and bt.meikxxb_id = lj.meikxxb_id(+)\n" + 
			"   and bt.jihkjb_id <> 1\n" + 
			" group by rollup (g.mingc,m.mingc)\n" + 
			" having not grouping(g.mingc)+grouping(m.mingc)<2\n" + 
			" ))\n" + 
			" order by jihkjb_id desc,gongysb_id desc,decode(meikxxb_id,'&nbsp;&nbsp;'||gongysb_id||'С��',0,1)\n";

		ResultSetList rstmp = con.getResultSetList(sql);
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		int aw=0;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con
					.getResultSetList("select biaot from baobpzb where guanjz='"
							+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;
			ArrHeader = new String[][] { {"��λ����","�¼ƻ�","���տ��","������ú","�ۼ���ú","���պ�ú","�ۼƺ�ú","���տ��","��ע"} };
			ArrWidth = new int[] {150,75,75,75,75,75,75,75,100};

			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			rt.setTitle("�ֿ��պĴ��ձ�", ArrWidth);
		}
		ArrWidth = new int[] {150,75,75,75,75,75,75,75,100};
		rt.setTitle("�ֿ��պĴ��ձ�", ArrWidth);
//		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		rt.setDefaultTitle(1, 3, "�Ʊ�λ��"
//				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
//				Table.ALIGN_LEFT);
//		rt.setDefaultTitle(4, 4, "�������ڣ�" + getRiq(), Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 4, "��λ���֡���", Table.ALIGN_RIGHT);

		// String[] arrFormat = new String[] { "", "", "", "", "", "", "",
		// "", "", "", "", "" };

		rt.setBody(new Table(rs, 1, 0, 1));
//		rt.body.setColAlign(1, Table.ALIGN_CENTER);
//		rt.body.setColAlign(2, Table.ALIGN_CENTER);
//		rt.body.setColAlign(3, Table.ALIGN_CENTER);
//		rt.body.setColAlign(4, Table.ALIGN_CENTER);
//		rt.body.setColAlign(5, Table.ALIGN_CENTER);
//		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(12, Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(rt.PAPER_ROWS);
		//	���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();

		// rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1, ArrWidth);
//		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"
//				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
//				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(8, 2, "�Ʊ�"+getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		// rt.footer.setRowHeight(1, 1);
//		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(25);
		RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt,
				"�ֿ��պĴ��ձ�", "" +BAOBPZB_GUANJZ);
		
		rstmp.close();
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();
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
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString1(null);
			getSelectData();
		}
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