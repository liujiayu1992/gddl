package com.zhiren.dc.zonghcx;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ����:tzf
 * ʱ��:2010-01-26
 * �޸�����:����������˾Ҫ��ӯ���������໥���㣬���򱨱����治ƽ���ò������ơ�
 */
public class Yanscx extends BasePage {

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	public boolean getRaw() {
		return true;
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

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	// ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����
	private String getDcMingc(String id) {
		if (id == null || "".equals(id)) {
			return "";
		}
		JDBCcon con = new JDBCcon();
		String mingc = "";
		String sql = "select mingc from diancxxb where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}

	// ���ѡ������ڵ�Ķ�Ӧ�Ĺ�Ӧ������
	private String[] getGys(String id) {
		String[] gys = { "ȫ��", "-1" };
		if (id == null || "".equals(id)) {
			return gys;
		}
		JDBCcon con = new JDBCcon();
		String sql = "select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			gys[0] = rsl.getString("mingc");
			gys[1] = rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}

	public String getPrintTable() {
		if (getLeixValue().getValue().equals("����")) {
			return getShulReport();
		} else if (getLeixValue().getValue().equals("����")) {
			return getZhilReport();
		} else if (getLeixValue().getValue().equals("������������")) {
			return getHuizReport();
		} else {
			return "�޴˱���";
		}
	}
	
    private String getGysParam(){
//		��Ӧ��ú������
		String gyssql = "";
		if("1".equals(getGys(getTreeid())[1])){
			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
		}else if("0".equals(getGys(getTreeid())[1])){
			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
		}
		return gyssql;
    }

	private String getDateCondition() {
		String cond = "";
		String Condfield = "f.fahrq";
		String strOraStartDate = "";
		String strOraEndDate = "";

		if (getRiq() == null || "".equals(getRiq())) {
			strOraStartDate = DateUtil.FormatOracleDate(new Date());
		} else {
			strOraStartDate = DateUtil.FormatOracleDate(getRiq());
		}
		if (getAfter() == null || "".equals(getAfter())) {
			strOraEndDate = DateUtil.FormatOracleDate(new Date());
		} else {
			strOraEndDate = DateUtil.FormatOracleDate(getAfter());
		}
		if (getWeizSelectValue() != null) {
			if (getWeizSelectValue().getValue().equals("���������ڲ�")) {
				Condfield = "f.fahrq";
			} else if (getWeizSelectValue().getValue().equals("���������ڲ�")) {
				Condfield = "f.daohrq";
			} else if (getWeizSelectValue().getValue().equals("���������ڲ�")) {
				Condfield = "z.huaysj";
			}
		}
		cond += " and " + Condfield + " >= " + strOraStartDate + " \n and "
				+ Condfield + " <" + strOraEndDate + "+1 \n";
		return cond;
	}

	private String getShulReport() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		

		String xhjs_str=" select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
		ResultSetList rsl=con.getResultSetList(xhjs_str);
		String yuns_js=" sum(round_new(f.yuns,"+v.getShuldec()+")) yuns,\n";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
		
		if(rsl.next()){
			
			 yuns_js=" sum(round_new(f.yingk,"+v.getShuldec()+")) + sum(round_new(f.biaoz,"+v.getShuldec()+")) " +
			"- sum(round_new(f.jingz,"+v.getShuldec()+")) yuns,\n ";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
	
		}
		rsl.close();
		
		String sql =

		"select\n"
				+ "decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n"
				+ "decode(grouping(m.mingc) + grouping(g.mingc), 2, '', 1, '�ϼ�', m.mingc) mk,\n"
				+ "decode(grouping(g.mingc) + grouping(m.mingc) + grouping(f.daohrq),1,'�ϼ�',2,'', to_char(f.daohrq,'yyyy-mm-dd')) daohrq,\n"
				+ "p.mingc as pz,\n"
				+ "c.mingc as fz,\n"
				+ "sum(f.ches) ches,\n"
				+ "sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) laimsl, sum(round_new(f.jingz,"
				+ v.getShuldec()
				+ ")) jingz,\n"
				+ "sum(round_new(f.biaoz,"
				+ v.getShuldec()
				+ ")) biaoz,"
				+yuns_js
				+ "sum(round_new(f.yingk,"
				+ v.getShuldec()
				+ ")) yingk, sum(round_new(f.zongkd,"
				+ v.getShuldec()
				+ ")) zongkd\n"
				+ "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c,zhilb z\n"
				+ "where f.gongysb_id = g.id and f.meikxxb_id = m.id\n"
				+ "and f.pinzb_id = p.id and f.faz_id = c.id\n"
				+ "and f.zhilb_id=z.id\n"
				+ getDateCondition()+getGysParam()
				+ "and f.diancxxb_id="
				+ getTreeid_dc()
				+ "\n"
				+ "group by rollup(g.mingc,m.mingc,f.daohrq,p.mingc,c.mingc)\n"
				+ "having not grouping(f.daohrq) +grouping(c.mingc) = 1\n"
				+ "order by grouping(g.mingc)desc,g.mingc,grouping(m.mingc)desc,m.mingc,\n"
				+ "grouping(f.daohrq)desc,f.daohrq";

		ResultSet rs = con.getResultSet(sql);
		Report rt = new Report();

		String ArrHeader[][] = new String[1][12];
		ArrHeader = new String[][] { { Locale.gongysb_id_fahb,
				Locale.meikxxb_id_fahb, Locale.daohrq_id_fahb,
				Locale.pinzb_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb,
				Locale.laimsl_fahb, Locale.jingz_fahb, Locale.biaoz_fahb,
				Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb } };

		int ArrWidth[] = new int[] { 90, 90, 90, 90, 60, 45, 45, 45, 45, 45,
				45, 45 };

		rt.setTitle("������ѯ", ArrWidth);

		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 3, "�Ʊ�λ:" + getZhibdwmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 5, "��ѯ����:" + getRiq() + "��" + getAfter(),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(11, 2, "��λ:��", Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.mergeFixedCols();
		// rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 12; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 3, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(11, 2, "�Ʊ�:", Table.ALIGN_RIGHT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();
	}

	private String getZhilReport() {

		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";

		sql = "select fhdw,mkdw, daohrq, pz,fz,jingz,ches, "
				+ "       mt,mad,aad,ad,aar,vad,vdaf,\n"
				+ "       qbad*1000 as qbar,\n"
				+ "       farl*1000 as farl,\n"
				+ "       round_new(farl*1000/4.1816,0) as qbar,\n"
				+ "       sdaf,stad,std,star,hdaf,had,fcad,\n"
				+ "       qgrd*1000 as qgrd\n"
				+ "  from (select decode(grouping(g.mingc), 1, '�ܼ�', g.mingc) as fhdw,\n"
				+ "               decode(grouping(g.mingc) + grouping(m.mingc),1,'�ϼ�', m.mingc) mkdw,\n"
				+ "               decode(grouping(g.mingc) + grouping(m.mingc) + grouping(f.daohrq), 1, '�ϼ�', to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
				+ "               p.mingc as pz,\n"
				+ "               c.mingc as fz,\n"
				+ "               sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ "))as laimsl,\n"
				+ "               sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ "))as jingz,\n"
				+ "               sum(round_new(f.biaoz,"
				+ v.getShuldec()
				+ "))as biaoz,\n"
				+ "               sum(round_new(f.yuns,"
				+ v.getShuldec()
				+ "))as yuns,\n"
				+ "               sum(round_new(f.yingk,"
				+ v.getShuldec()
				+ "))as yingk,\n"
				+ "               sum(round_new(f.zongkd,"
				+ v.getShuldec()
				+ "))as zongkd,\n"
				+ "               sum(f.ches)as ches,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.mt,"
				+ v.getMtdec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), "
				+ v.getMtdec()
				+ ")) as mt,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.mad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as mad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.aad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as aad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.ad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as ad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.aar * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as aar,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.vad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as vad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.vdaf * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as vdaf,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.qbad,"
				+ v.getFarldec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), "
				+ v.getFarldec()
				+ ")) as qbad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.qnet_ar,"
				+ v.getFarldec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) /sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) * 1000 / 4.1816, 0)) as qbar,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.qnet_ar,"
				+ v.getFarldec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), "
				+ v.getFarldec()
				+ ")) as farl,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.sdaf * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as sdaf,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.stad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as stad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.std * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as std,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) /sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as star,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.hdaf * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as hdaf,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.had * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as had,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.fcad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as fcad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.qgrd * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as qgrd\n"
				+ "      from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c,zhilb z\n"
				+ "          where f.gongysb_id = g.id(+)\n"
				+ "              and f.pinzb_id=p.id\n"
				+ "              and f.meikxxb_id = m.id\n"
				+ "              and f.pinzb_id = p.id\n"
				+ "              and f.faz_id = c.id\n"
				+ "              and f.zhilb_id = z.id\n"
				+ getDateCondition()+getGysParam()
				+ "              and f.diancxxb_id="
				+ getTreeid_dc()
				+ "\n"
				+ "          group by rollup(g.mingc, m.mingc,f.daohrq, p.mingc, c.mingc)\n"
				+ "         having not(grouping (f.daohrq)+grouping(c.mingc) =1)\n"
				+ "        order by grouping(g.mingc) desc,g.mingc,grouping(m.mingc) desc,m.mingc ,grouping(f.daohrq)desc,f.daohrq,grouping(p.mingc) ,p.mingc,grouping(c.mingc)desc,c.mingc)";

		ResultSet rs = con.getResultSet(sql);

		String[][] ArrHeader = new String[1][25];

		ArrHeader[0] = new String[] { "������λ", "ú��λ", "��������", "Ʒ��", "��վ",
				"����(��)", "����", "ȫˮ<br>��<br>(%)Mt",
				"����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad", "����<br>��<br>�ҷ�<br>(%)Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", "������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", "��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>(%)<br>Sdaf",
				"����<br>����<br>����<br>(%)<br>St,ad", "����<br>��ȫ<br>��(%)<br>St,d",
				"�յ�<br>��ȫ<br>��(%)<br>St,ar", "����<br>�޻�<br>����<br>(%)<br>Hdaf",
				"����<br>����<br>����<br>(%)<br>Had", "�̶�<br>̼<br>(%)<br>Fcad",
				"�ɻ�<br>��λ<br>��<br>(J/g)<br>Qgrd" };
		int[] ArrWidth = new int[25];
		Report rt = new Report();
		ArrWidth = new int[] { 85, 100, 90, 50, 50, 50, 50, 40, 40, 40, 40, 40,
				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };

		rt.setTitle("������ѯ", ArrWidth);
		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 3, "�Ʊ�λ:" + getZhibdwmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 7, "��ѯ����:" + getRiq() + "��" + getAfter(),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(24, 2, "��λ:��", Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 25; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(24, 2, "�Ʊ�:", Table.ALIGN_RIGHT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}

	private String getHuizReport() {

		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		

		String xhjs_str=" select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
		ResultSetList rsl=con.getResultSetList(xhjs_str);
		String yuns_js=" sum(round_new(f.yuns,"+v.getShuldec()+")) yuns,\n";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
		
		if(rsl.next()){
			
			 yuns_js=" sum(round_new(f.yingk,"+v.getShuldec()+")) + sum(round_new(f.biaoz,"+v.getShuldec()+")) " +
			"- sum(round_new(f.jingz,"+v.getShuldec()+")) yuns,\n ";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
	
		}
		rsl.close();
		
		
		
		String sffhfz=" select * from xitxxb where mingc='�Ƿ񰴷�������' and zhi='��' and leib='����' and zhuangt=1 ";//�Ƿ񰴷������з���
		ResultSetList rsl1=con.getResultSetList(sffhfz);
		String fenz= "      group by rollup(g.mingc, m.mingc, f.daohrq, p.mingc, c.mingc)\n"
		            + "     having not(grouping (f.daohrq)+grouping(c.mingc) =1)\n" ;//Ĭ��ֵ�������������з���
		
		if(rsl1.next()){
			
			 fenz="      group by rollup(g.mingc, m.mingc, f.id,f.daohrq, p.mingc, c.mingc)\n"
		        + "      having not(grouping(c.mingc)=1 and grouping(f.id)=0 )\n" ;//���������з���
	
		}
		rsl1.close();
		
		String sql = "select fhdw,mkdw, daohrq, pz,fz,laimsl,jingz, biaoz,yuns,yingk,\n"
				+ "       zongkd, ches, mt,mad,aad,ad,aar,vad,vdaf,\n"
				+ "       qbad*1000 as qbar,\n"
				+ "       farl*1000 as farl,\n"
				+ "       round_new(farl*1000/4.1816,0) as qbar,\n"
				+ "       sdaf,stad,std,star,hdaf,had,fcad,\n"
				+ "       qgrd*1000 as qgrd\n"
				+ "  from (select decode(grouping(g.mingc), 1, '�ܼ�', g.mingc) as fhdw,\n"
				+ "               decode(grouping(g.mingc) + grouping(m.mingc),1,'�ϼ�', m.mingc) mkdw,\n"
				+ "               decode(grouping(g.mingc) + grouping(m.mingc) + grouping(f.daohrq), 1, '�ϼ�', to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
				+ "               p.mingc as pz,\n"
				+ "               c.mingc as fz,\n"
				+ "               sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ "))as laimsl,\n"
				+ "               sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ "))as jingz,\n"
				+ "               sum(round_new(f.biaoz,"
				+ v.getShuldec()
				+ "))as biaoz,\n"
				+ yuns_js
				+ "               sum(round_new(f.yingk,"
				+ v.getShuldec()
				+ "))as yingk,\n"
				+ "               sum(round_new(f.zongkd,"
				+ v.getShuldec()
				+ "))as zongkd,\n"
				+ "               sum(f.ches)as ches,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.mt,"
				+ v.getMtdec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), "
				+ v.getMtdec()
				+ ")) as mt,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.mad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as mad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.aad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as aad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.ad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as ad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.aar * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as aar,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.vad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as vad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.vdaf * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as vdaf,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.qbad,"
				+  v.getFarldec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), "
				+ v.getFarldec()
				+ ")) as qbad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.qnet_ar,"
				+ v.getFarldec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) /sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) * 1000 / 4.1816, 0)) as qbar,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.qnet_ar,"
				+ v.getFarldec()
				+ ") * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), "
				+ v.getFarldec()
				+ ")) as farl,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.sdaf * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as sdaf,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.stad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as stad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.std * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as std,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) /sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as star,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.hdaf * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as hdaf,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.had * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as had,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.fcad * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as fcad,\n"
				+ "               decode(sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 0,0, round_new(sum(z.qgrd * round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")) / sum(round_new(f.laimsl,"
				+ v.getShuldec()
				+ ")), 2)) as qgrd\n"
				+ "      from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c,zhilb z\n"
				+ "          where f.gongysb_id = g.id(+)\n"
				+ "              and f.pinzb_id=p.id\n"
				+ "              and f.meikxxb_id = m.id\n"
				+ "              and f.pinzb_id = p.id\n"
				+ "              and f.faz_id = c.id\n"
				+ "              and f.zhilb_id = z.id\n"
				+ getDateCondition()+getGysParam()
				+ "              and f.diancxxb_id="
				+ getTreeid_dc()
				+ "\n"
				+fenz
//				+ "          group by rollup(g.mingc, m.mingc, f.daohrq, p.mingc, c.mingc)\n"
//				+ "         having not(grouping (f.daohrq)+grouping(c.mingc) =1)\n"
				+ "        order by grouping(g.mingc) desc,g.mingc,grouping(m.mingc) desc,m.mingc ,grouping(f.daohrq)desc,f.daohrq,grouping(p.mingc) ,p.mingc,grouping(c.mingc)desc,c.mingc)";

		ResultSet rs = con.getResultSet(sql);

		Report rt = new Report();

		String ArrHeader[][] = new String[1][30];
		ArrHeader = new String[][] { { "������λ", "ú��λ", "��������", "Ʒ��", "��վ",
				"��ú����", "����", "Ʊ��", "����", "ӯ��", "�ܿ���", "����",
				"ȫˮ<br>��<br>(%)Mt", "����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad", "����<br>��<br>�ҷ�<br>(%)Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", "������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", "��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>(%)<br>Sdaf",
				"����<br>����<br>����<br>(%)<br>St,ad", "����<br>��ȫ<br>��(%)<br>St,d",
				"�յ�<br>��ȫ<br>��(%)<br>St,ar", "����<br>�޻�<br>����<br>(%)<br>Hdaf",
				"����<br>����<br>����<br>(%)<br>Had", "�̶�<br>̼<br>(%)<br>Fcad",
				"�ɻ�<br>��λ<br>��<br>(J/g)<br>Qgrd" } };

		int ArrWidth[] = new int[] { 90, 90, 90, 50, 50, 65, 45, 45, 45, 45,
				45, 45, 60, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45,
				45, 45, 45, 45 };

		rt.setTitle("���������ۺϲ�ѯ", ArrWidth);

		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 3, "�Ʊ�λ:" + getZhibdwmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 5, "��ѯ����:" + getRiq() + "��" + getAfter(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(29, 2, "��λ:��", Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.mergeFixedCols();
		// rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 30; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 5, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(29, 2, "�Ʊ�:", Table.ALIGN_RIGHT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();

	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();

		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������:"));
		ComboBox riqxz = new ComboBox();
		riqxz.setTransform("WeizSelectx");
		riqxz.setEditable(true);
		riqxz.setWidth(100);
		riqxz.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(riqxz);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		// �糧��
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null,
				getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton dc = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		dc.setIcon("ext/resources/images/list-items.gif");
		dc.setCls("x-btn-icon");
		dc.setMinWidth(20);

		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(dc);
		tb1.addText(new ToolbarText("-"));
		// ��Ӧ��
		DefaultTree gys = new DefaultTree(DefaultTree.tree_gongys_win,
				"gongysTree", "" + visit.getDiancxxb_id(), "forms[0]",
				getTreeid(), getTreeid());
		visit.setDefaultTree(gys);
		TextField tfgys = new TextField();
		tfgys.setId("gongysTree_text");
		tfgys.setWidth(100);
		tfgys.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addText(new ToolbarText("��Ӧ��"));
		tb1.addField(tfgys);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("����:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("LeixDropDown");
		leix.setEditable(true);
		leix.setWidth(100);
		leix.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(leix);
		tb1.addText(new ToolbarText("-"));
         
		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		tb.setIcon(SysConstant.Btn_Icon_Print);
		setToolbar(tb1);
	}

	private String getZhibdwmc() {
		Visit visit = (Visit) getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="
				+ visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}

	// ������ʹ�õķ���
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			// treeid=String.valueOf(((Visit)
			// this.getPage().getVisit()).getDiancxxb_id());
			treeid = "0";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("gongysTree_text"))
						.setValue(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// �糧Tree_begin
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

	DefaultTree dc;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}

	// �糧Tree_end
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	// ����

	public IDropDownBean getLeixValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLeixModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLeixValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getLeixModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getLeixModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setLeixModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLeixModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();

		List.add(new IDropDownBean(0, "����"));
		List.add(new IDropDownBean(1, "����"));
		List.add(new IDropDownBean(2, "������������"));

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// ���ڲ�ѯ����

	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getWeizSelectModels() {
		List list = new ArrayList();
		Visit v = (Visit) this.getPage().getVisit();
		list.add(new IDropDownBean(1, "���������ڲ�"));
		list.add(new IDropDownBean(2, "���������ڲ�"));
		list.add(new IDropDownBean(3, "���������ڲ�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
		return ((Visit) getPage().getVisit()).getProSelectionModel12();
	}

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			setTreeid_dc(visit.getDiancxxb_id() + "");
			setLeixValue(null);
			setLeixModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
		}
		getSelectData();

	}

}