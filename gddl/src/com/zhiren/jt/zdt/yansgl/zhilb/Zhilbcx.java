package com.zhiren.jt.zdt.yansgl.zhilb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhilbcx extends BasePage {

	// *********************��������************************//
	public boolean getRaw() {
		return true;
	}

	// �õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return biaotmc;

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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (getLeixSelectValue().getValue().equals("�ֳ�")) {
			return getSelectData();
		} else if (getLeixSelectValue().getValue().equals("�ֿ�")) {
			return getSelectData();
		} else if (getLeixSelectValue().getValue().equals("���̱�")) {
			return getQibb();
		} else {
			return "�޴˱���";
		}
	}

	// *********************��������************************//
	// *******************���̱�******************//
	private String getQibb() {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		String riq = FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1 = FormatDate(DateUtil.getDate(getEndriqDate()));
		// �糧����
		int jib = this.getDiancTreeJib();
		String diancCondition = "";
		if (jib == 1) {
			diancCondition = ",diancxxb gs where gs.fuid=dc.id and gs.id=fh.diancxxb_id \n ";
			// }else if(){

		} else {
			diancCondition = " where dc.id=fh.diancxxb_id and fh.diancxxb_id in (select id\n"
					+ " from(\n"
					+ " select id from diancxxb\n"
					+ " start with id="
					+ getTreeid()
					+ "\n"
					+ " connect by fuid=prior id\n"
					+ " )\n"
					+ " union\n"
					+ " select id\n"
					+ " from diancxxb\n"
					+ " where id="
					+ getTreeid() + ")";
		}
		// ��Ӧ������
		// String gongysCondition=" and fh.gongysb_id in(select id from gongysb
		// where fuid="+getGongysDropDownValue().getId() +"\n)";
		// String gongysid=" and fh.gongysb_id
		// ="+getGongysDropDownValue().getId()+"\n";
		String gongysfuid = "(select fuid from gongysb where id="
				+ getGongysDropDownValue().getId() + "\n)";
		long fuid = 0;
		ResultSet rs1 = conn.getResultSet(gongysfuid);
		try {
			if (rs1.next()) {
				fuid = rs1.getLong("fuid");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrWidth = new int[] { 150, 40, 80, 80, 80, 80, 80, 80 };

		String gongystj = "";
		if (getGongysDropDownValue().getId() == -1) {
			gongystj = ",(select xgys.id as id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc\n"
					+ "  from gongysb xgys,gongysb dgys\n"
					+ "  where xgys.fuid=dgys.id(+))gys";

		} else {
			gongystj = ",(select gys.id,gys.xuh,gys.mingc from gongysb gys  where (gys.id="
					+ getGongysDropDownValue().getId()
					+ " or  gys.fuid="
					+ getGongysDropDownValue().getId() + "))gys ";

		}
		StringBuffer strRow = new StringBuffer();
		strRow
				.append("select decode(grouping(gys.mingc),1,'С��',gys.mingc) as ��Ӧ�� \n");
		strRow.append("  from fahb fh,diancxxb dc--,gongysb gys  \n");
		// strRow.append(" where dc.id=fh.diancxxb_id \n");
		strRow.append(gongystj);
		strRow.append(diancCondition);
		strRow.append("     and daohrq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd')  \n");
		strRow.append("     and daohrq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd') \n");
		strRow.append("		and fh.gongysb_id=gys.id \n");

		strRow.append("  group by rollup(gys.mingc)  \n");
		strRow
				.append("  order by grouping(gys.mingc) desc,min(gys.xuh) ,gys.mingc \n");

		StringBuffer strCol = new StringBuffer();
		strCol
				.append("select decode(grouping(dc.mingc),1,'С��',0,dc.mingc,'С��') as �糧  \n");
		strCol.append("from fahb fh,diancxxb dc--,gongysb gys  \n");
		strCol.append(gongystj);
		strCol.append(diancCondition);
		strCol.append("     and fh.gongysb_id=gys.id  \n");
		strCol.append("     and daohrq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd')  \n");
		strCol.append("     and daohrq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd')  \n");
		strCol.append("group by rollup(dc.mingc)  \n");
		strCol
				.append("order by grouping(dc.mingc) desc,min(dc.xuh),dc.mingc \n");

		StringBuffer sbsql = new StringBuffer();
		sbsql
				.append("select grouping(gys.mingc) as rowjb,grouping(dc.mingc) as coljb, \n");
		sbsql
				.append("    decode(grouping(gys.mingc),0,gys.mingc,'С��') as ��Ӧ��, \n");
		sbsql
				.append("    decode(grouping(dc.mingc),0,dc.mingc,2,'С��','С��') as �糧, \n");
		sbsql
				.append("    decode(sum(round(fh.jingz,0)),0,0,round(sum(round(qnet_ar,2) * round(fh.jingz,0)) / sum(round(fh.jingz,0)), 2))  as ���� , \n");
		sbsql
				.append("    decode(sum(round(fh.jingz,0)),0,0,round(sum(round(qnet_ar,2) * round(fh.jingz,0)) / sum(round(fh.jingz,0)), 2))  as �ۼ�\n");
		sbsql.append("from fahb fh,zhilb z,diancxxb dc--,gongysb gys \n");
		sbsql.append(gongystj);
		sbsql.append(diancCondition);
		sbsql.append("     and daohrq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd')  \n");
		sbsql.append("     and daohrq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd')  \n");
		sbsql.append("	   and fh.gongysb_id=gys.id and fh.zhilb_id=z.id \n");
		sbsql.append("     group by cube(gys.mingc ,dc.mingc)  \n");
		sbsql.append("  --  having  not (grouping(dc.mingc)=0 ) \n");
		sbsql
				.append("     order by grouping(gys.mingc) desc,min(gys.xuh) ,gys.mingc, \n");
		sbsql
				.append("           grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
		System.out.println(sbsql);
		cd.setRowNames("��Ӧ��");
		cd.setColNames("�糧");
		cd.setDataNames("����,�ۼ�");
		cd.setDataOnRow(true);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		int cols = ArrWidth.length;
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 80;
		}
		ArrWidth[0] = 150;
		ArrWidth[1] = 40;
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;
		rt.setTitle("�볧ú��ֵ����(���̱�)", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.body.setCellValue(1, 0, "��λ");
		rt.body.setCellValue(1, 1, "��λ");
		for (int j = 1; j < cols + 1; j++) {
			if (j == 2) {
				rt.body.setColAlign(j, Table.ALIGN_CENTER);
			}
			if (j > 2) {
				rt.body.setColAlign(j, Table.ALIGN_RIGHT);
			}
		}
		rt
				.setDefaultTitle(1, 2, "�Ʊ�λ:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, riq + "-" + riq1, Table.ALIGN_RIGHT);
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,
				"��ӡ����:"
						+ FormatDate(DateUtil.getDate(DateUtil
								.FormatDate(new Date()))), Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 1, "��λ:������", Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}

	// *******************�ۺϱ�����******************//

	// *******************����SQL���*********************//

	/**
	 * ���ɼ�Ȩ���� ˮ��", "�ҷ�","�ӷ���", "���", "��λ������", "��λ������
	 * mt��aar��vdaf��stad��qnet_ar��qgrad_daf
	 * 
	 * @return ���ؼ�Ȩ����
	 */
	private String getJiaq_sum() {
		StringBuffer jiaq_sum = new StringBuffer();
		jiaq_sum.append("round(sum(round(jingz,0)),0) as jingz,\n");
		jiaq_sum
				.append("decode(sum(decode(mt,0,0,round(jingz,0))),0,0,round(sum(round(mt,1) * round(jingz,0)) / sum(decode(mt,0,0,round(jingz,0))), 1)) as mt,\n");
		jiaq_sum
				.append("decode(sum(decode(aar,0,0,round(jingz,0))),0,0,round(sum(round(aar,2) * round(jingz,0)) / sum(decode(aar,0,0,round(jingz,0))), 2)) as aar,\n");
		jiaq_sum
				.append("decode(sum(decode(vdaf,0,0,round(jingz,0))),0,0,round(sum(round(vdaf,2) * round(jingz,0)) / sum(decode(aar,0,0,round(jingz,0))), 2)) as vdaf,\n");
		jiaq_sum
				.append("decode(sum(decode(stad,0,0,round(jingz,0))),0,0,round(sum(round(stad,2) * round(jingz,0)) / sum(decode(stad,0,0,round(jingz,0))), 2)) as stad,\n");
		jiaq_sum
				.append("decode(sum(decode(qnet_ar,0,0,round(jingz,0))),0,0,round(sum(round(qnet_ar,2) * round(jingz,0)) / sum(decode(qnet_ar,0,0,round(jingz,0))), 2)) as qnet_ar,\n");
		jiaq_sum
				.append("decode(sum(decode(qnet_ar,0,0,round(jingz,0))),0,0,round((sum(round(qnet_ar,2) * round(jingz,0)) / sum(decode(qnet_ar,0,0,round(jingz,0))))*1000/4.1816, 0)) as qgrad_daf \n");
		return jiaq_sum.toString();
	}

	/**
	 * ����SQL��ͷ���� ��������λ���ƣ��糧��������λ���� ���ڣ���ʱ���,��ʽ��and daohrq>='2008-01-16' and
	 * daohrq<='2008-01-30'�� ����������danwtj��diancid=100 �� fengsid=100��
	 * 
	 * @return ���ر�ͷ����
	 */
	private String getTitel(String danwmc, String riq, String danwtj) {
		StringBuffer title = new StringBuffer();
		title.append("(select distinct " + danwmc + ",fenx from vwzhilcx,\n");
		title
				.append("(select '1' fenx from dual union select '2' from dual) fenx \n");
		title.append("where 1=1 " + riq + " " + danwtj + ") title,\n");
		return title.toString();
	}

	/**
	 * ����ԭʼ���� ���������ܵ�λ���ƣ����ڣ���ʱ��Σ����գ������ͣ��������գ������ۼƣ�
	 * 
	 * @return ����ԭʼ����
	 */
	private String getYuanssj(String danwmc, String riq, String leix,
			String danwtj) {
		if (getJiaq_sum() != null) {// �жϼ�Ȩ�����Ƿ�����
			if (danwmc.equals("") || riq.equals("") || leix.equals("")) {
				System.out.println("����ԭʼ����ʧ�ܣ���������ݴ���");
				return "";
			} else {
				StringBuffer yuanssj = new StringBuffer();
				yuanssj.append("select").append(danwmc).append(",'").append(
						leix).append("' as fenx,\n");
				yuanssj.append(getJiaq_sum()).append("\n");
				yuanssj.append("from vwzhilcx where 1=1 ").append(riq).append(
						danwtj).append("\n");
				yuanssj.append("group by ").append(danwmc).append("\n");
				return yuanssj.toString();
			}
		} else {
			System.out.println("���ɼ�Ȩ����ʱʧ�ܣ�����getJiaq_sum()������");
			return "";
		}
	}

	/**
	 * ���ɱ����ѯ��� ���ͣ��ֳ����ֿ�����������
	 * �������������ƣ�groupingmc����λ���ƣ�danwmc���������ڣ�riq1�����ڶΣ�riq2��where:����
	 * 
	 * @return ����SQL��ѯ���
	 */
	private String getStrSQL(String groupingmc, String where, String group,
			String order, String danwmc, String riq1, String riq2, String danwtj) {
		StringBuffer Str_sql = new StringBuffer();
		Str_sql.append("select \n");
		Str_sql.append(groupingmc).append("\n");
		Str_sql.append("decode(title.fenx, 1, '����', '�ۼ�') as fenx,\n");
		Str_sql.append(getJiaq_sum()).append("\n");// �õ����ܵ�������
		Str_sql.append("from \n");
		Str_sql.append(getTitel(danwmc, riq2, danwtj)).append("\n");
		// ��������
		Str_sql.append("(").append(getYuanssj(danwmc, riq2, "2", danwtj))
				.append(" union \n");
		Str_sql.append(getYuanssj(danwmc, riq1, "1", danwtj))
				.append(" ) nr \n");
		Str_sql.append(" where 1=1 ").append(where).append("\n");
		Str_sql.append(group).append("\n");
		Str_sql.append(" having not(grouping(title.fenx) = 1) \n");
		Str_sql.append(order).append("\n");
		return Str_sql.toString();
	}

	private String getSelectData() {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		int iFixedRows = 1;// �̶��к�
		String riq1 = "";// ��������
		String riq2 = "";// ���ڶ�
		riq1 = " and daohrq='" + getEndriqDate() + "'";
		riq2 = " and daohrq>='" + getBeginriqDate() + "' and daohrq<='"
				+ getEndriqDate() + "'";
		String groupingmc = "";// ��������
		String danwmc = "";// GROUP BY ������
		String where = "";// ��������
		String group = "";// ��ʲô����
		String order = "";// ��ʲô����
		String riqbg = FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riqed = FormatDate(DateUtil.getDate(getEndriqDate()));

		StringBuffer Strdanwtj = new StringBuffer();
		String danwtj = "";
		String strdiancid = "";// �糧����
		String titlename = "";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// ѡ����ʱˢ�³����еĵ糧
			strdiancid = " ";
		} else if (jib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancid = "  and fengsid=  " + this.getTreeid();
		} else if (jib == 3) {// ѡ�糧ֻˢ�³��õ糧
			strdiancid = " and diancid= " + this.getTreeid();
		} else if (jib == -1) {
			strdiancid = " and diancid = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		Strdanwtj.append(strdiancid);
		String gongysmc = "";
		if (getGongysDropDownValue() != null
				&& getGongysDropDownValue().getId() != -1) {
			gongysmc = " and gongysb_id=" + getGongysDropDownValue().getId();
		}
		Strdanwtj.append(gongysmc);
		danwtj = Strdanwtj.toString();
		if (getLeixSelectValue() != null) {

			if (getLeixSelectValue().getValue().equals("�ֳ�")) {
				if (jib != 1 && jib !=2) {// ���糧��
					titlename = "�볧ú����ͳ��(�ֳ�)";
					groupingmc = " decode(grouping(title.diancmc),1,'�ܼ�',title.diancmc) as dianwmc,\n";
					danwmc = " DIANCID,DIANCMC,FENGSMC";
					where = " and title.diancid=nr.diancid(+) and title.fenx=nr.fenx(+) \n";
					group = " group by rollup(title.fenx,title.diancmc)\n";
					order = " order by grouping(title.diancmc) desc,title.diancmc,title.fenx\n";
				} else {//
					titlename = "�볧ú����ͳ��(�ֳ�)";
					groupingmc = " decode(grouping(title.fengsmc) + grouping(title.diancmc),2,'�ܼ�',1,title.fengsmc,'&nbsp;&nbsp;'||title.diancmc) as dianwmc,\n";
					danwmc = " DIANCID,DIANCMC,FENGSMC";
					where = " and title.diancid=nr.diancid(+) and title.fenx=nr.fenx(+) \n";
					group = " group by rollup(title.fenx, title.fengsmc, title.diancmc)\n";
					order = " order by grouping(title.fengsmc) desc,title.fengsmc,grouping(title.diancmc) desc,title.diancmc,title.fenx\n";
				}
			} else if (getLeixSelectValue().getValue().equals("�ֿ�")) {
				titlename = "�볧ú����ͳ�ƣ��ֿ�";
				groupingmc = " decode(grouping(title.gongysbmc),1,'�ܼ�',title.gongysbmc) as dianwmc,\n";// '&nbsp;&nbsp;&nbsp;&nbsp;'||
				danwmc = " GONGYSB_ID,GONGYSBMC";
				where = " and title.gongysb_id=nr.gongysb_id(+) and title.fenx=nr.fenx(+) \n";
				group = " group by rollup(title.fenx, title.gongysbmc)\n";
				order = " order by grouping(title.gongysbmc) desc,title.gongysbmc,title.fenx";
			}
			// �������ƣ�groupingmc����λ���ƣ�danwmc���������ڣ�riq1�����ڶΣ�riq2��where:����
			strSQL = getStrSQL(groupingmc, where, group, order, danwmc, riq1,
					riq2, danwtj);
			// else if (getBaoblxValue().getValue().equals("�볧ú�����ͬú�ʶԱ�(�ֳ�)")) {
			// strSQL = getFenck(jib,dr_riq,lj_iq,strdiancid,gongysmc);
			// } else if (getBaoblxValue().getValue().equals("�ֳ�")) {
			// strSQL = "";
			// }
		}
		System.out.print(strSQL);
		// ֱ���ֳ��ֿ����
		if (getLeixSelectValue() != null) {
			if (getLeixSelectValue().getValue().equals("�ֳ�")
					|| getLeixSelectValue().getValue().equals("�ֿ�")) {
				ArrHeader = new String[2][9];
				ArrHeader[0] = new String[] { "��λ", "��λ", "����<br>(��)",
						"ˮ��<br>Mt(%)", "�ҷ�<br>Ad(%)", "�ӷ���<br>Vdaf(%)",
						"���<br>St,d(%)", "������qnet,ar", "������qnet,ar" };
				ArrHeader[1] = new String[] { "��λ", "��λ", "����<br>(��)",
						"ˮ��<br>Mt(%)", "�ҷ�<br>Ad(%)", "�ӷ���<br>Vdaf(%)",
						"���<br>St,d(%)", "MJ/kg", "Kacl/Kg" };

				ArrWidth = new int[] { 150, 40, 80, 80, 80, 80, 80, 80, 80 };
			} else if (getLeixSelectValue().getValue()
					.equals("�볧ú�����ͬú�ʶԱ�(�ֳ�)")) {
				ArrHeader[0] = new String[] { "��λ", "��λ", "����<br>(��)",
						"ˮ��<br>Mt(%)", "�ҷ�<br>Ad(%)", "�ӷ���<br>Vdaf(%)",
						"���<br>St,d(%)", "������qnet,ar", "������qnet,ar" };
				ArrHeader[1] = new String[] { "��λ", "��λ", "����<br>(��)",
						"ˮ��<br>Mt(%)", "�ҷ�<br>Ad(%)", "�ӷ���<br>Vdaf(%)",
						"���<br>St,d(%)", "MJ/kg", "Kacl/Kg" };

				ArrWidth = new int[] { 150, 40, 80, 80, 80, 80, 80, 80, 80 };
			}
		}

		iFixedRows = 1;

		ResultSet rs = cn.getResultSet(strSQL);

		// ResultSetList rs = cn.getResultSetList(strSQL);
		// ����
		rt.setBody(new Table(rs, 2, 0, iFixedRows));
		if (cn.getHasIt(strSQL)) {// ֵ
			rt.body.setCellAlign(3, 1, 1);// �ܼƾ���
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
		}
		rt.setTitle(titlename, ArrWidth);
		rt
				.setDefaultTitle(1, 3, "�Ʊ�λ:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 5, riqbg + "-" + riqed, Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		// rt.body.setColFormat(ArrFormat);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		// System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			// stra.set(DateUtil.getYear(new Date()), 0, 1);
			stra.setTime(new Date());
			stra.add(Calendar.DATE, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra
					.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate() {
		if (((Visit) getPage().getVisit()).getString5() == null
				|| ((Visit) getPage().getVisit()).getString5() == "") {
			((Visit) getPage().getVisit()).setString5(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setEndriqDate(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("��ѯ����:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		visit.setDefaultTree(dt);
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

		tb1.addText(new ToolbarText("��Ӧ��:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		// meik.setWidth(60);
		// meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
		}
		getToolbars();

		blnIsBegin = true;

	}

	// ��Ӧ��
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getGongysDropDownModels() {
		String sql = "select id,mingc\n" + "from gongysb\n";
		// +"where gongysb.fuid=0";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
		return;
	}

	// ����
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);

	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ֳ�"));
		list.add(new IDropDownBean(2, "�ֿ�"));
		list.add(new IDropDownBean(3, "���̱�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// �糧����
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}