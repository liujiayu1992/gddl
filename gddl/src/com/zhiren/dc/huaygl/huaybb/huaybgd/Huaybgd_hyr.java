package com.zhiren.dc.huaygl.huaybb.huaybgd;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�������
 * ʱ�䣺2009-08-17
 * ���������ݲ������ã��ж��Ƿ���ʾ����������ʾ����Ƿ���ʾú�����(�ֶ�ά��ҳ��¼��)
 */
/*
 * ����:tzf
 * ʱ��:2009-05-11
 * ����:��Զ�ɶ໯��ʵ�ֲ�ͬ����Դ(zhilb  zhillsb)������չʾ
 */
/*
 * ����:tzf
 * ʱ��:2009-4-16
 * �޸�����:������������  ��Ϊ ���� ���������� ���� ����ֵ�ĸı� �ı䣬�������ܳ� �鿴�������ݣ�
 * 
 * ��Դ� ���޸�

 */
/*
 * ���ߣ���ΰ
 * ʱ�䣺2009-04-05
 * �޸����ݣ�
 * 		��ӱ�ע����
 * 		��ϵͳ��Ϣ����ӻ��鱨����ʾ�������ǳ��ţ�Ĭ��Ϊ����
 */

/*
 * ����: ����
 * ʱ�䣺2009-08-11
 * �޸����ݣ�
 * 		��ӷ�������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-15
 * �������޸ı����кϲ� �������ڣ�����������ͬʱ�����кϲ�
 */
/*
 * ���ߣ�������
 * ʱ�䣺2009-08-15
 * �������޸ı����г�����ʾ����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-04
 * �������޸Ļ��鱨�浥��ú�󡢷�վ��Ʒ�֡��������ڵ�ȡ����ʽ��Ϊ����ȡ���������ָ�겻ͬ��ɻ��鱨�浥��������⡣
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-07
 * �������ϴθ����޸�ȡ�������󷢻������Ѿ����ַ���ʽ�����ٽ�����Լ��ȥ��
 */

/*
 * ���ߣ�����
 * ʱ�䣺2010-01-15
 * �������޸�getSql_zhilb()����SQL��䣬�޸Ĳ�����Աȡ���������⡣
 */
public class Huaybgd_hyr extends BasePage {

	private static final String REPORTNAME_HUAYBGD_ZHILB = "Huaybgd_zhilb";

	private static final String REPORTNAME_HUAYBGD_ZHILLSB = "Huaybgd_zhillsb"; // ��û��������Դ����

	// ��Դ����ȷҲ�Դ�Ĭ��ȡ��

	private String check = "false";

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	// private String mstrReportName = "";
	public boolean getRaw() {
		return true;
	}

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

	public String getPrintTable() {
		return getHuaybgd();
	}

	public String getImgServletPath(String name) {
		String a="<img width=160 height=40 src='"
				+ MainGlobal.getHomeContext(getPage()) + "/imgs/report"+
				"/" + name + "' />";
		return a;
	}
	
	public String getXiaosw() {
		JDBCcon con = new JDBCcon();
		String xiaosw = "2";
		String sql = "select zhi from xitxxb where mingc='���鱨�浥������ʾС��λ' and zhuangt=1";
		ResultSetList rs = con.getResultSetList(sql);
		if (rs.next()) {
			xiaosw = rs.getString("zhi");
		}
		rs.close();
		con.Close();
		return xiaosw;
	}

	// ��ѯzhillsb������
	private StringBuffer getSql_zhillsb() {
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,a.fahrq as fahrq,decode(GetCaiyry(y.id),null,'',GetCaiyry(y.id)) as lurry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,3)*1000 as QBAD,round_new(z.qgrd,2)*1000 as QGRD,round_new(z.qgrad,2)*1000 as QGRAD,\n");
		sqlHuaybgd
				.append("round_new(z.qgrad_daf,2)*1000 as GANZWHJGWRZ,round_new(z.qnet_ar,2)*1000 as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,z.shenhzt,\n");
		sqlHuaybgd
				.append("'"
						+ getBianmValue().getValue()
						+ "' as bianh,decode(a.meikdwmc,null,' ',a.meikdwmc) as meikdwmc,decode(a.chez,null,' ',a.chez) as chez,decode(a.pinz,null,' ',a.pinz) as pinz,decode(a.cheph,null,' ',a.cheph) as cheph,nvl(a.ches,0) as ches,nvl(a.meil,0) as meil,'' as beiz,' ' as meikdm from zhillsb z,yangpdhb y,\n");
		sqlHuaybgd
				.append("(select distinct getHuayMkmc4zl(f.zhilb_id) as meikdwmc,\n");
		sqlHuaybgd.append("getHuayFz4zl(f.zhilb_id) as chez,\n");
		sqlHuaybgd.append("getHuayFhrq4zl(f.zhilb_id) as fahrq,\n");
		sqlHuaybgd.append("getHuayPz4zl(f.zhilb_id) as pinz,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd.append("round_new(sum(f.laimsl), " + getXiaosw()
				+ ") as meil,\n");
		sqlHuaybgd.append("sum(f.ches) AS CHES, \n");
		sqlHuaybgd.append("GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH \n");
		sqlHuaybgd.append("from fahb f, zhillsb z\n");
		sqlHuaybgd.append("where z.zhilb_id = f.zhilb_id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId()).append("\n");
		sqlHuaybgd.append("group by f.zhilb_id) a \n");
		sqlHuaybgd.append("where z.zhilb_id=a.zhilb_id(+)\n");
		sqlHuaybgd.append("and y.zhilblsb_id(+)=z.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
		// System.out.println(sqlHuaybgd.toString());
		return sqlHuaybgd;
	}

	// ��ѯzhilb������
	private StringBuffer getSql_zhilb() {
		String SQL = "";
		SQL = "select decode(y.caiyrq, null, ' ', TO_CHAR(Y.CAIYRQ, 'YYYY-MM-DD')) AS CAIYRQ,\n"
				+ "       a.fahrq as fahrq,\n"
				+ "       decode(GetCaiyry(h.id), null, '', GetCaiyry(h.id)) as lurry,\n"
				+ "       TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,\n"
				+ "       round_new(z.mt, 1) as MT,\n"
				+ "       round_new(z.mad, 2) as MAD,\n"
				+ "       round_new(z.aad, 2) as AAD,\n"
				+ "       round_new(z.aar, 2) as AAR,\n"
				+ "       round_new(z.ad, 2) as AD,\n"
				+ "       round_new(z.vad, 2) as VAD,\n"
				+ "       round_new(z.vdaf, 2) as VDAF,\n"
				+ "       round_new(z.stad, 2) as STAD,\n"
				+ "       round_new(z.std, 2) as STD,\n"
				+ "       ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,\n"
				+ "       round_new(z.had, 2) as HAD,\n"
				+ "       round_new(z.har, 2) as HAR,\n"
				+ "       round_new(z.qbad, 3) * 1000 as QBAD,\n"
				+ "       round_new(z.qgrd, 2) * 1000 as QGRD,\n"
				+ "       round_new(z.qgrad, 2) * 1000 as QGRAD,\n"
				+ "       round_new(z.qgrad_daf, 2) * 1000 as GANZWHJGWRZ,\n"
				+ "       round_new(z.qnet_ar, 2) * 1000 as QNETAR,\n"
				+ "       round_new(round_new(z.qnet_ar, 2) * 7000 / 29.271, 0) as FRL,\n"
				+ "       decode(z.huayy, null, ' ', z.huayy) as huayy,\n"
				+ "       '' as shenhry,\n"
				+ "       '"
				+ getBianmValue().getValue()
				+ "' as bianh,\n"
				+ "       decode(a.meikdwmc, null, '', a.meikdwmc) as meikdwmc,\n"
				+ "       decode(a.chez, null, '', a.chez) as chez,\n"
				+ "       decode(a.pinz, null, '', a.pinz) as pinz,\n"
				+ "       decode(a.cheph, null, '', a.cheph) as cheph,\n"
				+ "       nvl(a.ches, 0) as ches,\n"
				+ "       nvl(a.meil, 0) as meil,\n"
				+ "       decode(z.beiz, null, '', z.beiz) as beiz,\n"
				+ "       nvl(y.meikdm, '') as meikdm\n"
				+ "  from zhilb z,\n"
				+ "       caiyb y,\n"
				+ "       yangpdhb h,\n"
				+ "       (select distinct getHuayMkmc4zl(f.zhilb_id) as meikdwmc,\n"
				+ "                        getHuayFz4zl(f.zhilb_id) as chez,\n"
				+ "                        getHuayFhrq4zl(f.zhilb_id) as pinz,\n"
				+ "                        getHuayPz4zl(f.zhilb_id) as fahrq,\n"
				+ "                        f.zhilb_id as zhilb_id,\n"
				+ "                        round_new(sum(f.laimsl), 2) as meil,\n"
				+ "                        sum(f.ches) AS CHES,\n"
				+ "                        GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH\n"
				+ "          from fahb f\n"
				+ "         where f.zhilb_id = "
				+ getBianmValue().getId()
				+ "\n"
				+ "         group by f.zhilb_id) a\n"
				+ " where z.id = a.zhilb_id\n"
				+ "   and z.caiyb_id = y.id\n"
				+ "   and y.zhilb_id = z.id\n"
				+ "   and h.caiyb_id = y.id\n"
				+ "   and z.id = " + getBianmValue().getId() + "";
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd.append(SQL);
		return sqlHuaybgd;
	}

	// 
	private String getHuaybgd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		boolean isChes = MainGlobal.getXitxx_item(
				"����",
				"���鱨�浥��ʾ����",
				String.valueOf(((Visit) this.getPage().getVisit())
						.getDiancxxb_id()), "��").equals("��");

		StringBuffer sqlHuaybgd = new StringBuffer();

		if (this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILLSB)) {
			sqlHuaybgd = this.getSql_zhillsb();
		} else if (this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILB)) {
			sqlHuaybgd = this.getSql_zhilb();
		}
		ResultSet rs = con.getResultSet(sqlHuaybgd);
		String shangjshry = "";
		String lury = "";
		String strKuangb = "";
		String strChez = "";
		String[][] ArrHeader = new String[21][6];
		try {
			if (rs.next()) {
				lury = rs.getString("HUAYY");

				// �Ƿ���ʾ����
				StringBuffer buffer = new StringBuffer();
				StringBuffer bufferChe = new StringBuffer();
				String cheph = "";
				if (!isChes) {
					cheph = rs.getString("CHEPH");
					if (cheph.equals(" ")) {

					} else {
						String[] list = cheph.split(",");
						for (int i = 1; i <= list.length; i++) {
							if (i % 9 == 0) {
								buffer.append(list[i - 1] + ",<br>");
							} else {
								buffer.append(list[i - 1] + ",");
							}
						}

						int intChes = Integer.parseInt(MainGlobal
								.getXitxx_item("����", "���鱨�浥��ʾ��������", String
										.valueOf(((Visit) this.getPage()
												.getVisit()).getDiancxxb_id()),
										String.valueOf(list.length)));

						if (intChes > 0) {
							if (intChes >= list.length) {
								cheph = buffer.toString().substring(0,
										buffer.length() - 1);
							} else {
								for (int j = 1; j <= intChes - 1; j++) {
									if (j % 9 == 0) {
										bufferChe.append(list[j - 1] + ",<br>");
									} else {
										bufferChe.append(list[j - 1] + ",");
									}
								}
								bufferChe.append(list[intChes]);
								cheph = bufferChe.toString().substring(0,
										bufferChe.length());
							}
						}
						// ��ʾȫ������
						if (this.getCheck().equals("true")) {
							cheph = buffer.toString().substring(0,
									buffer.length() - 1);
						}
					}
				}

				String num = rs.getString("FRL");
				String strCzy = "";
				String strCaiyrq = "";
				strCaiyrq = rs.getString("CAIYRQ");
				// ����zhilb��ȡ��ʱ����ʾ��𡢳�վ��(������˹���)
				if (this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILLSB)) {

					// �ж��Ƿ���ʾ�������ʾ����
					boolean isKuangb = MainGlobal.getXitxx_item(
							"����",
							"���鱨�浥��ʾ���",
							String.valueOf(((Visit) this.getPage().getVisit())
									.getDiancxxb_id()), "��").equals("��");

					boolean isMeikdm = MainGlobal.getXitxx_item(
							"����",
							"���鱨�浥��ʾ������",
							String.valueOf(((Visit) this.getPage().getVisit())
									.getDiancxxb_id()), "��").equals("��");
					if (isKuangb) {
						strKuangb = rs.getString("MEIKDWMC");
						strChez = rs.getString("chez");
					} else {
						if (isMeikdm) {
							try {
								String sql = "select decode(c.meikdm,null,' ',c.meikdm) as meikdm from caiyb c,zhillsb z where c.zhilb_id=z.zhilb_id and z.id="
										+ getBianmValue().getId();
								ResultSet rec = con.getResultSet(sql);
								if (rec.next()) {
									strKuangb = rec.getString("meikdm");
								} else {
									ResultSet rskb = con
											.getResultSet("select decode(meikmc,null,'',meikmc) as kuangb,decode(caiyry,null,'',caiyry) as caiyry,decode(caiyrq,null,'',to_char(caiyrq,'yyyy-mm-dd')) caiysj from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"
													+ getBianmValue()
															.getValue() + "'");
									if (rskb.next()) {
										strKuangb = rskb.getString("kuangb");
										strCzy = rskb.getString("caiyry");
										strCaiyrq = rskb.getString("caiysj");
									} else {
										strKuangb = "";
										strCzy = "";
										strCaiyrq = "";
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							strChez = "";
						} else {
							strKuangb = "";
							strChez = "";
						}
					}
				} else if (this.getReportName()
						.equals(REPORTNAME_HUAYBGD_ZHILB)) {
					strKuangb = rs.getString("MEIKDWMC");
					strChez = rs.getString("chez");
				}
				ArrHeader[0] = new String[] { "������",
						"" + rs.getString("BIANH") + "", "���",
						"" + strKuangb + "", "��վ", "" + strChez + "" };

				ArrHeader[1] = new String[] { "��������",
						"" + rs.getString("HUAYRQ") + "", "��������",
						"" + strCaiyrq + "", "ú��(t)",
						"" + rs.getString("MEIL") + "" };
				String strFahrq = "";
				String strLury = "";
				if (rs.getString("fahrq") == null) {
					strFahrq = "";
				} else {
					strFahrq = rs.getString("fahrq");
				}
				if (rs.getString("lurry") == null) {
					strLury = strCzy;
				} else {
					strLury = rs.getString("lurry");
				}

				ArrHeader[2] = new String[] { "��������", "" + strFahrq + "", "ú��",
						"" + rs.getString("PINZ") + "", "��������Ա",
						"" + strLury + "" };

				// �Ƿ���ʾ����
				if (isChes) {
					ArrHeader[3] = new String[] { "����:",
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches") };
				} else {
					ArrHeader[3] = new String[] { "����", "" + cheph + "",
							"" + cheph + "", "" + cheph + "", "" + cheph + "",
							"" + cheph + "" };
				}
				ArrHeader[4] = new String[] { "ȫˮ��Mt(%)", "ȫˮ��Mt(%)",
						"ȫˮ��Mt(%)", "" + rs.getDouble("MT") + "", "��ע", "��ע" };
				ArrHeader[5] = new String[] { "���������ˮ��Mad(%)", "���������ˮ��Mad(%)",
						"���������ˮ��Mad(%)", "" + rs.getDouble("MAD") + "", "", "" };
				ArrHeader[6] = new String[] { "����������ҷ�Aad(%)", "����������ҷ�Aad(%)",
						"����������ҷ�Aad(%)", "" + rs.getDouble("AAD") + "", "", "" };
				ArrHeader[7] = new String[] { "�յ����ҷ�Aar(%)", "�յ����ҷ�Aar(%)",
						"�յ����ҷ�Aar(%)", "" + rs.getDouble("AAR") + "", "", "" };
				ArrHeader[8] = new String[] { "������ҷ�Ad(%)", "������ҷ�Ad(%)",
						"������ҷ�Ad(%)", "" + rs.getDouble("AD") + "", "0", "0" };
				ArrHeader[9] = new String[] { "����������ӷ���Vad(%)",
						"����������ӷ���Vad(%)", "����������ӷ���Vad(%)",
						"" + rs.getDouble("VAD") + "", "", "" };
				ArrHeader[10] = new String[] { "�����޻һ��ӷ���Vdaf(%)",
						"�����޻һ��ӷ���Vdaf(%)", "�����޻һ��ӷ���Vdaf(%)",
						"" + rs.getDouble("VDAF") + "", "", "" };
				ArrHeader[11] = new String[] { "���������ȫ��St,ad(%)",
						"���������ȫ��St,ad(%)", "���������ȫ��St,ad(%)",
						"" + rs.getDouble("STAD") + "", "", "" };
				ArrHeader[12] = new String[] { "�����ȫ��St,d(%)", "�����ȫ��St,d(%)",
						"�����ȫ��St,d(%)", "" + rs.getDouble("STD") + "", "", "" };
				ArrHeader[13] = new String[] { "�յ���ȫ��St,ar(%)",
						"�յ���ȫ��St,ar(%)", "�յ���ȫ��St,ar(%)",
						"" + rs.getDouble("STAR") + "", "", "" };
				ArrHeader[14] = new String[] { "�����������Had(%)", "�����������Had(%)",
						"�����������Had(%)", "" + rs.getDouble("HAD") + "", "", "" };
				ArrHeader[15] = new String[] { "�յ�����Har(%)", "�յ�����Har(%)",
						"�յ�����Har(%)", "" + rs.getDouble("HAR") + "", "", "" };
				ArrHeader[16] = new String[] { "�����������Ͳ��ֵQb,ad(J/g)",
						"�����������Ͳ��ֵQb,ad(J/g)", "�����������Ͳ��ֵQb,ad(J/g)",
						"" + rs.getDouble("QBAD") + "", "", "" };
				ArrHeader[17] = new String[] { "�������λ��ֵQgr,d(J/g)",
						"�������λ��ֵQgr,d(J/g)", "�������λ��ֵQgr,d(J/g)",
						"" + rs.getDouble("QGRD") + "", "", "" };
				ArrHeader[18] = new String[] { "�����������λ��ֵQgr,ad(J/g)",
						"�����������λ��ֵQgr,ad(J/g)", "�����������λ��ֵQgr,ad(J/g)",
						"" + rs.getDouble("QGRAD") + "", "", "" };
				ArrHeader[19] = new String[] { "�����޻һ���λ��ֵQgr,daf(J/g)",
						"�����޻һ���λ��ֵQgr,daf(J/g)", "�����޻һ���λ��ֵQgr,daf(J/g)",
						"" + rs.getDouble("GANZWHJGWRZ") + "", "", "" };
				ArrHeader[20] = new String[] { "�յ�����λ��ֵQnet,ar(J/g)",
						"�յ�����λ��ֵQnet,ar(J/g)", "�յ�����λ��ֵQnet,ar(J/g)",
						"" + rs.getDouble("QNETAR") + "",
						"" + num + "" + "(ǧ��/ǧ��)", "" + num + "" + "(ǧ��/ǧ��)" };
				String beiz = rs.getString("beiz");

				if (beiz != null && !"".equals(beiz)) {
					for (int i = 5; i <= 19; i++) {
						ArrHeader[i][4] = beiz;
						ArrHeader[i][5] = beiz;
					}
				}
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 100, 95, 95, 155, 95, 95 };

		rt.setTitle("  �� �� ú  ��  ��  ��  ��  ��", ArrWidth);
		rt.title.setCellValue(1, 1, getImgServletPath("dt_hlj_hyr.gif"));
		rt.title.merge(1, 1, 1, 3);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		// String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��������:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 1, "�����ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 1, "��ˣ�" + shangjshry, Table.ALIGN_CENTER);
		rt.setDefautlFooter(5, 2, "����Ա��" + lury, Table.ALIGN_CENTER);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(21, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		for (int i = 1; i < 21; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
		for (int i = 1; i <= 21; i++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCellValue(5, 4, rt.body.format(rt.body.getCellValue(5, 4),
				"0.0"));
		for (int i = 6; i < 17; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0.00"));
		}
		for (int i = 17; i < 22; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0"));
		}
		// rt.body.setCellValue(i, j, strValue);

		rt.body.setCellFontSize(4, 2, 9);
		rt.body.setCells(2, 1, 21, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.merge(5, 1, 21, 3);
		rt.body.merge(5, 5, 21, 6);
		rt.body.merge(4, 2, 4, 6);
		// rt.body.merge(3, 4, 3, 6);
		rt.body.ShowZero = false;

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(40);

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

	// �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧
	private boolean hasDianc(String id) {
		JDBCcon con = new JDBCcon();
		boolean mingc = false;
		String sql = "select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = true;
		}
		rsl.close();
		return mingc;
	}

	// -------------------------�糧Tree-----------------------------------------------------------------
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
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="
				+ visit.getDiancxxb_id() + " \n";
		sql += " union \n";
		sql += "  select d.id,d.mingc from diancxxb d where d.fuid="
				+ visit.getDiancxxb_id() + " \n";
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

	// -------------------------�糧Tree END----------

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		// if (visit.isFencb()) {
		// tb1.addText(new ToolbarText("����:"));
		// ComboBox changbcb = new ComboBox();
		// changbcb.setTransform("ChangbSelect");
		// changbcb.setWidth(130);
		// changbcb
		// .setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		// tb1.addField(changbcb);
		// tb1.addText(new ToolbarText("-"));
		// }

		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null,
				getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel())
				.getBeanValue(Long.parseLong(getTreeid_dc() == null
						|| "".equals(getTreeid_dc()) ? "-1" : getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);

		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("huayrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�������:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		Checkbox chk = new Checkbox();
		chk.setId("CHECKED");
		if (this.getCheck().equals("true")) {
			chk.setChecked(true);
		} else {
			chk.setChecked(false);
		}
		chk
				.setListeners("check:function(own,checked){if(checked){document.all.CHECKED.value='true'}else{document.all.CHECKED.value='false'}}");
		tb1.addField(chk);
		tb1.addText(new ToolbarText("��ʾȫ������"));
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
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
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString13("");
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}

		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}

		if (cycle.getRequestContext().getParameters("lx") != null) {

			if (!cycle.getRequestContext().getParameters("lx")[0].equals(this
					.getReportName())) {
				setChangbValue(null);
				setChangbModel(null);
				setBianmValue(null);
				setBianmModel(null);
				this.setRiq(DateUtil.FormatDate(new Date()));
			}
			setReportName(cycle.getRequestContext().getParameters("lx")[0]);
			// System.out.println(this.getReportName()+"----");
		} else {
			if (this.getReportName().equals("")) {
				this.setReportName("");
			}
		}
	}

	private void setReportName(String name) {
		Visit visit = (Visit) getPage().getVisit();

		if (name == null) {
			visit.setString13(REPORTNAME_HUAYBGD_ZHILLSB);
		}
		if (name.equals(REPORTNAME_HUAYBGD_ZHILB)) {
			visit.setString13(REPORTNAME_HUAYBGD_ZHILB);
		} else {
			visit.setString13(REPORTNAME_HUAYBGD_ZHILLSB);
		}

	}

	private String getReportName() {
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString13();
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

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		StringBuffer sb = new StringBuffer();
		// sb
		// .append(
		// "select l.id,z.bianm from zhuanmb z,zhillsb l,caiyb c\n")
		// .append(
		// "where z.zhillsb_id= l.id and c.zhilb_id = l.zhilb_id\n")
		// .append("and l.huaysj = ")
		// .append(DateUtil.FormatOracleDate(getRiq()))
		// .append("\n")
		// .append("and l.shenhzt>-1 \n")
		// .append("and z.zhuanmlb_id = \n")
		// .append(
		// "(select id from zhuanmlb where jib = (select nvl(max(jib),0) from
		// zhuanmlb))");
		String s = "";// ����ѡ��Ĳ��Ǹ��糧ʱ���������� ��Ϊ��ѯ���� ������ ���ӵ糧ʱ ȫ����ѯ
		if (!this.hasDianc(this.getTreeid_dc())) {
			s = " and f.diancxxb_id=" + this.getTreeid_dc() + " \n";

		} else {
			s = " and f.diancxxb_id in ( select distinct d.id from diancxxb d start with d.id="
					+ this.getTreeid_dc() + " connect by prior d.id=d.fuid )";
		}

		if (this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILB)) {

			sb
					.append(
							" select distinct z.id,z.huaybh  from zhilb z,fahb f,caiyb c   \n")
					.append(" where z.id=f.zhilb_id  and c.zhilb_id=z.id  \n")
					.append(" and z.caiyb_id=c.id \n").append(" and z.huaysj=")
					.append(DateUtil.FormatOracleDate(getRiq())).append(" \n")
					.append(" and z.shenhzt>-1 \n").append(s);

		} else if (this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILLSB)) {
			sb
					.append(
							"select distinct l.id,z.bianm from zhuanmb z,zhillsb l,caiyb c,fahb f \n")
					.append(
							"where z.zhillsb_id= l.id and c.zhilb_id = l.zhilb_id \n")
					.append(" and l.zhilb_id = f.zhilb_id ")
					.append(" \n")
					.append(s)
					.append("and l.huaysj = ")
					.append(DateUtil.FormatOracleDate(getRiq()))
					.append("\n")
					.append("and l.shenhzt>-1 \n")
					.append("and z.zhuanmlb_id = \n")
					.append(
							"(select id from zhuanmlb where jib = (select nvl(max(jib),0) from zhuanmlb))")
					.append(" union select id,beiz from zhillsb where huaysj=")
					.append(DateUtil.FormatOracleDate(getRiq())).append(
							" and huaylb='��ʱ��'");
		}

		setBianmModel(new IDropDownModel(sb.toString(), "��ѡ��"));
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
			setChangbModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModel() {
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
}
