package com.zhiren.dc.huaygl.huaybb.huaybgd;


import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Huaybgd_zr extends BasePage {

	private static final String REPORTNAME_HUAYBGD_ZHILB = "Huaybgd_zhilb";

	private static final String REPORTNAME_HUAYBGD_ZHILLSB = "Huaybgd_zhillsb"; // 当没有设置资源或者

	// 资源不正确也以此默认取数

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

	// 界面用户提示
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

	// 查询zhillsb的数据
	private StringBuffer getSql_zhillsb() {
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,decode(a.fahrq,null,'',to_char(a.fahrq,'yyyy-mm-dd')) as fahrq,");
		sqlHuaybgd.append("decode(GetCaiyry(y.id),\n");
		sqlHuaybgd.append("              null,\n");
		sqlHuaybgd.append("              '',\n");
		sqlHuaybgd.append("              decode(GetZhiyry(y.id),\n");
		sqlHuaybgd.append("                     null,\n");
		sqlHuaybgd.append("                     '',\n");
		sqlHuaybgd
				.append(" nvl('','采样人员:')||GetCaiyry(y.id) || chr(38) || 'nbsp;' || chr(38) ||\n");
		sqlHuaybgd.append(" 'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'||\n");
		sqlHuaybgd.append("chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||\n");
		sqlHuaybgd.append("'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'||\n");
		sqlHuaybgd.append("chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||\n");
		sqlHuaybgd.append("'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'||\n");
		sqlHuaybgd.append("chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||\n");
		sqlHuaybgd.append("'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'||\n");
		sqlHuaybgd.append("chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||\n");
		sqlHuaybgd.append("'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'||\n");
		sqlHuaybgd.append("chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||\n");
		sqlHuaybgd.append("'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'||\n");
		sqlHuaybgd.append("chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||\n");
		sqlHuaybgd
				.append("'nbsp;'||chr(38)||'nbsp;'||nvl('','制样人员:')||GetZhiyry(y.id))) lurry,\n");
		sqlHuaybgd.append("       TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,");

		sqlHuaybgd
				.append(" TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,2) as QBAD,round_new(z.qgrd,2) as QGRD,round_new(z.qgrad,2) as QGRAD,\n");
		sqlHuaybgd
				.append("round_new(z.qgrad_daf,2) as GANZWHJGWRZ,round_new(z.qnet_ar,2) as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,z.shenhzt,\n");
		sqlHuaybgd
				.append("'"
						+ getBianmValue().getValue()
						+ "' as bianh,decode(a.meikdwmc,null,' ',a.meikdwmc) as meikdwmc,decode(a.chez,null,' ',a.chez) as chez,decode(a.pinz,null,' ',a.pinz) as pinz,decode(a.cheph,null,' ',a.cheph) as cheph,nvl(a.ches,0) as ches,nvl(a.meil,0) as meil,'' as beiz,' ' as meikdm from zhillsb z,yangpdhb y,\n");
		sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
		sqlHuaybgd.append("cz.mingc as chez,\n");
		sqlHuaybgd.append("f.fahrq as fahrq,\n");
		sqlHuaybgd.append("p.mingc as pinz,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd.append("round_new(sum(f.laimsl), 2) as meil,\n");
		sqlHuaybgd.append("sum(f.ches) AS CHES, \n");
		sqlHuaybgd.append("GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH \n");
		sqlHuaybgd
				.append("from fahb f, zhillsb z,meikxxb m,chezxxb cz,pinzb p\n");
		sqlHuaybgd.append("where z.zhilb_id = f.zhilb_id\n");
		sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
		sqlHuaybgd.append("and f.faz_id=cz.id\n");
		sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId()).append("\n");
		sqlHuaybgd
				.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id,f.fahrq) a \n");
		sqlHuaybgd.append("where z.zhilb_id=a.zhilb_id(+)\n");
		sqlHuaybgd.append("and y.zhilblsb_id(+)=z.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
		// System.out.println(sqlHuaybgd.toString());
		return sqlHuaybgd;
	}

	// 查询zhilb的数据
	private StringBuffer getSql_zhilb() {
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiyrq,null,' ',TO_CHAR(Y.CAIYRQ, 'YYYY-MM-DD')) AS CAIYRQ,to_char(a.fahrq,'yyyy-mm-dd') as fahrq,decode(z.lury,null,' ',z.lury) as lurry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,2) as QBAD,round_new(z.qgrd,2) as QGRD,round_new(z.qgrad,2) as QGRAD,\n");
		sqlHuaybgd
				.append("round_new(z.qgrad_daf,2) as GANZWHJGWRZ,round_new(z.qnet_ar,2) as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,\n");
		sqlHuaybgd
				.append("'"
						+ getBianmValue().getValue()
						+ "' as bianh,decode(a.meikdwmc,null,'',a.meikdwmc) as meikdwmc,decode(a.chez,null,'',a.chez) as chez,decode(a.pinz,null,'',a.pinz) as pinz,decode(a.cheph,null,'',a.cheph) as cheph,nvl(a.ches,0) as ches,nvl(a.meil,0) as meil,decode(z.beiz,null,'',z.beiz) as beiz,nvl(y.meikdm,'') as meikdm from zhilb z,caiyb y,\n");
		sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
		sqlHuaybgd.append("cz.mingc as chez,\n");
		sqlHuaybgd.append("p.mingc as pinz,\n");
		sqlHuaybgd.append("f.fahrq as fahrq,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd.append("round_new(sum(f.laimsl), 2) as meil,\n");
		sqlHuaybgd.append("sum(f.ches) AS CHES, \n");
		sqlHuaybgd.append("GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH \n");
		sqlHuaybgd
				.append("from fahb f, zhilb z,meikxxb m,chezxxb cz,pinzb p\n");
		sqlHuaybgd.append("where z.id = f.zhilb_id\n");
		sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
		sqlHuaybgd.append("and f.faz_id=cz.id\n");
		sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId()).append("\n");
		sqlHuaybgd
				.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id,f.fahrq) a \n");
		sqlHuaybgd.append("where z.id=a.zhilb_id\n");
		sqlHuaybgd.append("and z.caiyb_id=y.id\n");
		sqlHuaybgd.append("and y.zhilb_id=z.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
		// System.out.println(sqlHuaybgd.toString());
		return sqlHuaybgd;
	}

	// 
	private String getHuaybgd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		boolean isChes = MainGlobal.getXitxx_item(
				"化验",
				"化验报告单显示车数",
				String.valueOf(((Visit) this.getPage().getVisit())
						.getDiancxxb_id()), "否").equals("是");

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
		String[][] ArrHeader = new String[18][6];
		try {
			if (rs.next()) {
				lury = rs.getString("HUAYY");
				Table tb = new Table(1, 1);
				// 是否显示车数
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
								.getXitxx_item("化验", "化验报告单显示车号数量", String
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
						// 显示全部车号
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
				// 当从zhilb中取数时，显示矿别、车站。(二级审核过了)
				if (this.getReportName().equals(REPORTNAME_HUAYBGD_ZHILLSB)) {

					// 判断是否显示矿别还是显示代码
					boolean isKuangb = MainGlobal.getXitxx_item(
							"化验",
							"化验报告单显示矿别",
							String.valueOf(((Visit) this.getPage().getVisit())
									.getDiancxxb_id()), "是").equals("是");

					boolean isMeikdm = MainGlobal.getXitxx_item(
							"化验",
							"化验报告单显示矿别代码",
							String.valueOf(((Visit) this.getPage().getVisit())
									.getDiancxxb_id()), "是").equals("是");
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
											.getResultSet("select decode(meikmc,null,'',meikmc) as kuangb,"
													+ "nvl('','采样人员: ')||decode(caiyry,null,'',caiyry) as caiyry,"
													+ "nvl('','制样人员: ')||decode(zhiyry,null,'',zhiyry) as zhiyry,"
													+ "decode(caiyrq,null,'',to_char(caiyrq,'yyyy-mm-dd')) caiysj "
													+ "from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"
													+ getBianmValue()
															.getValue() + "'");
									if (rskb.next()) {
										strKuangb = rskb.getString("kuangb");
										strCzy = rskb.getString("caiyry")
												+ "&nbsp;&nbsp;&nbsp;&nbsp;"
												+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
												+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
												+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
												+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
												+ rskb.getString("zhiyry");
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
						if (rs.getLong("shenhzt") == 7) {
							strKuangb = rs.getString("MEIKDWMC");
							strChez = rs.getString("chez");
						}
					}
				} else if (this.getReportName()
						.equals(REPORTNAME_HUAYBGD_ZHILB)) {
					strKuangb = rs.getString("MEIKDWMC");
					strChez = rs.getString("chez");
				}
				ArrHeader[0] = new String[] { "矿别", "" + strKuangb + "",
						"化验日期", "" + rs.getString("HUAYRQ") + "", "编号",
						"" + rs.getString("BIANH") + "" };

				ArrHeader[1] = new String[] { "发站", "" + strChez + "", "采样日期",
						"" + strCaiyrq + "", "煤量(t)",
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

				ArrHeader[2] = new String[] { strLury, strLury, strLury,
						strLury, strLury, strLury };

				// 是否显示车数
				if (isChes) {
					ArrHeader[3] = new String[] { "车数:",
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches") };
				} else {
					ArrHeader[3] = new String[] { "车号", "" + cheph + "",
							"" + cheph + "", "" + cheph + "", "" + cheph + "",
							"" + cheph + "" };
				}
				ArrHeader[4] = new String[] { "全水分Mt(%)", "全水分Mt(%)",
						"全水分Mt(%)", "" + rs.getDouble("MT") + "", "备注", "备注" };
				ArrHeader[5] = new String[] { "空气干燥基水分Mad(%)", "空气干燥基水分Mad(%)",
						"空气干燥基水分Mad(%)", "" + rs.getDouble("MAD") + "", "", "" };
				ArrHeader[6] = new String[] { "空气干燥基灰分Aad(%)", "空气干燥基灰分Aad(%)",
						"空气干燥基灰分Aad(%)", "" + rs.getDouble("AAD") + "", "", "" };
				ArrHeader[7] = new String[] { "收到基灰分Aar(%)", "收到基灰分Aar(%)",
						"收到基灰分Aar(%)", "" + rs.getDouble("AAR") + "", "", "" };
				ArrHeader[8] = new String[] { "干燥基灰分Ad(%)", "干燥基灰分Ad(%)",
						"干燥基灰分Ad(%)", "" + rs.getDouble("AD") + "", "0", "0" };
				ArrHeader[9] = new String[] { "空气干燥基挥发分Vad(%)",
						"空气干燥基挥发分Vad(%)", "空气干燥基挥发分Vad(%)",
						"" + rs.getDouble("VAD") + "", "", "" };
				ArrHeader[10] = new String[] { "干燥无灰基挥发分Vdaf(%)",
						"干燥无灰基挥发分Vdaf(%)", "干燥无灰基挥发分Vdaf(%)",
						"" + rs.getDouble("VDAF") + "", "", "" };
				ArrHeader[11] = new String[] { "空气干燥基全硫St,ad(%)",
						"空气干燥基全硫St,ad(%)", "空气干燥基全硫St,ad(%)",
						"" + rs.getDouble("STAD") + "", "", "" };
				ArrHeader[12] = new String[] { "干燥基全硫St,d(%)", "干燥基全硫St,d(%)",
						"干燥基全硫St,d(%)", "" + rs.getDouble("STD") + "", "", "" };

				ArrHeader[13] = new String[] { "空气干燥基氢Had(%)", "空气干燥基氢Had(%)",
						"空气干燥基氢Had(%)", "" + rs.getDouble("HAD") + "", "", "" };

				ArrHeader[14] = new String[] { "弹筒热值Qb,ad(MJ/Kg)",
						"弹筒热值Qb,ad(MJ/Kg)", "弹筒热值Qb,ad(MJ/Kg)",
						"" + rs.getDouble("QBAD") + "", "", "" };
				ArrHeader[15] = new String[] { "干燥基高位热值Qgr,d(MJ/Kg)",
						"干燥基高位热值Qgr,d(MJ/Kg)", "干燥基高位热值Qgr,d(MJ/Kg)",
						"" + rs.getDouble("QGRD") + "", "", "" };
				ArrHeader[16] = new String[] { "空气干燥基高位热值Qgr,ad(MJ/Kg)",
						"空气干燥基高位热值Qgr,ad(MJ/Kg)", "空气干燥基高位热值Qgr,ad(MJ/Kg)",
						"" + rs.getDouble("QGRAD") + "", "", "" };

				ArrHeader[17] = new String[] { "收到基低位热值Qnet,ar(MJ/Kg)",
						"收到基低位热值Qnet,ar(MJ/Kg)", "收到基低位热值Qnet,ar(MJ/Kg)",
						tb.format("" + rs.getDouble("QNETAR") + "", "0.00"),
						"" + num + "" + "(千卡/千克)", "" + num + "" + "(千卡/千克)" };
				String beiz = rs.getString("beiz");

				if (beiz != null && !"".equals(beiz)) {
					for (int i = 5; i <= 16; i++) {
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
		Visit visit = (Visit) this.getPage().getVisit();

		rt.setTitle(visit.getDiancqc() + "<br>" + "煤  质  检  验  报  告", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitleLeft("报告日期:" + DateUtil.FormatDate(new Date()), 2);

		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);

		StringBuffer buf = new StringBuffer();

		sqlHuaybgd.setLength(0);
		sqlHuaybgd.append("SELECT QUANC FROM RENYXXB WHERE ZHIW = '二审'");

		rs = con.getResultSet(sqlHuaybgd);
		buf.append("批准:");
		try {
			while (rs.next()) {
				buf.append(rs.getString(1)).append(",");
			}
			buf.setLength(buf.length() - 1);
			buf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			buf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			buf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			buf.append("审核:");

			sqlHuaybgd.setLength(0);
			sqlHuaybgd.append("SELECT QUANC FROM RENYXXB WHERE ZHIW = '化验班长'");

			rs = con.getResultSet(sqlHuaybgd);

			while (rs.next()) {
				buf.append(rs.getString(1)).append(",");
			}
			buf.setLength(buf.length() - 1);
			buf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			buf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			buf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			buf.append("化验:");

			sqlHuaybgd.setLength(0);
			sqlHuaybgd.append("SELECT QUANC FROM RENYXXB WHERE ZHIW = '化验'");

			rs = con.getResultSet(sqlHuaybgd);

			while (rs.next()) {
				buf.append(rs.getString(1)).append(",");
			}
			buf.setLength(buf.length() - 1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		rt.setDefautlFooter(1, 6, buf.toString()
				+ "<p>本报告仅对来样负责，根据国家标准:GB/T211-2007《煤中全水分的测定方法》"
				+ "<p>GB/T212-2008《煤的工业分析方法》 GB/T213-2008《煤的发热量测定方法》<p>"
				+ "GB/T214-2007《煤中全硫的测定方法》", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(18, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		for (int i = 1; i < 18; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
		for (int i = 1; i <= 18; i++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCellValue(5, 4, rt.body.format(rt.body.getCellValue(5, 4),
				"0.0"));
		for (int i = 6; i < 18; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0.00"));
		}

		// rt.body.setCellValue(i, j, strValue);

		rt.body.setCellFontSize(4, 2, 9);
		rt.body.setCells(2, 1, 18, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(3, 1, 3, 1, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.merge(5, 1, 18, 3);
		rt.body.merge(5, 5, 18, 6);
		rt.body.merge(4, 2, 4, 6);
		rt.body.mergeCell(3, 1, 3, 6);
		rt.body.ShowZero = false;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// 判断电厂Tree中所选电厂时候还有子电厂
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

	// -------------------------电厂Tree-----------------------------------------------------------------
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

	// -------------------------电厂Tree END----------

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		// if (visit.isFencb()) {
		// tb1.addText(new ToolbarText("厂别:"));
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

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("化验日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("huayrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("化验编码:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
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
		tb1.addText(new ToolbarText("显示全部车号"));
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	// 工具栏使用的方法
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
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

	// 日期是否变化
	private boolean riqchange = false;

	// 绑定日期
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

	// 页面变化记录
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
		String s = "";// 当所选择的不是父电厂时，以他自身 作为查询条件 ，否则 有子电厂时 全部查询
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
					.append(" union select id,huaybh from zhillsb where huaysj=")
					.append(DateUtil.FormatOracleDate(getRiq())).append(
							" and huaylb='临时样'");
		}

		setBianmModel(new IDropDownModel(sb.toString(), "请选择"));
	}

	// 厂别下拉框
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
