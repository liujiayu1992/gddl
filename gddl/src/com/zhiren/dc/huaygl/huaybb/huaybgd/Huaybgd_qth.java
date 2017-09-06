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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Huaybgd_qth extends BasePage {

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

	// ȼ�ϲɹ���ָ���������ձ�
	private String getHuaybgd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		int shenhzt=0;
		StringBuffer sql=new StringBuffer();
		sql.append("select shenhzt from zhillsb where id=" + getBianmValue().getId());
		ResultSet rsl = con.getResultSet(sql);
		try{
			while(rsl.next()){
				shenhzt=rsl.getInt("shenhzt");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(shenhzt==7){
			StringBuffer sqlHuaybgd = new StringBuffer();
			sqlHuaybgd
			.append("select * from (select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,(select distinct GetCaiyry(caiyryglb.yangpdhb_id) cai \n");
			sqlHuaybgd
			.append("from caiyryglb where yangpdhb_id = y.id) lurry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
			sqlHuaybgd
			.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
			sqlHuaybgd
			.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,2)*1000 as QBAD,round_new(z.qgrd,2)*1000 as QGRD,round_new(z.qgrad,2)*1000 as QGRAD,\n");
			sqlHuaybgd
			.append("round_new(z.qgrad_daf,2)*1000 as GANZWHJGWRZ,round_new(z.qnet_ar,2)*1000 as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,\n");
			sqlHuaybgd
			.append("'"
					+ getBianmValue().getValue()
					+ "' as bianh,a.meikdwmc,a.chez,a.pinz,a.cheph,a.meil from zhillsb z,yangpdhb y,\n");
			sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
			sqlHuaybgd.append("cz.mingc as chez,\n");
			sqlHuaybgd.append("p.mingc as pinz,\n");
			sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
			sqlHuaybgd
			.append("round_new(sum(f.laimsl), 2) as meil,\n");
			sqlHuaybgd.append("sum(f.ches) AS CHEPH \n");
			sqlHuaybgd
			.append("from fahb f, zhillsb z,meikxxb m,chezxxb cz,pinzb p\n");
			sqlHuaybgd.append("where z.zhilb_id = f.zhilb_id\n");
			sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
			sqlHuaybgd.append("and f.faz_id=cz.id\n");
			sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
			sqlHuaybgd.append("and z.id = " + getBianmValue().getId()).append("\n");
			sqlHuaybgd.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id) a \n");
			sqlHuaybgd.append("where z.zhilb_id=a.zhilb_id\n");
			sqlHuaybgd.append("and y.zhilblsb_id=z.id\n");
			sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
			sqlHuaybgd.append("), (select * from (SELECT DISTINCT (SELECT decode(mingc,null,' ',mingc) as mingc \n");
			sqlHuaybgd.append(" FROM guobb \n");
			sqlHuaybgd.append("WHERE xiangmmc = 'ȫˮ��') AS MTFF,\n");
			sqlHuaybgd.append(" (SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("   FROM guobb \n");
			sqlHuaybgd.append("  WHERE xiangmmc = '���������ˮ��') AS MADFF,\n");
			sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
			sqlHuaybgd.append("FROM guobb \n");
			sqlHuaybgd.append("WHERE xiangmmc = '����������ҷ�') AS AADFF,\n");
			sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
			sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ����ҷ�') AS AARFF,\n");
			sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
			sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '������ҷ�') AS ADFF,\n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '����������ӷ���') AS VADFF, \n");
	        sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc) as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����޻һ��ӷ���') AS VDAFFF,\n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '���������ȫ��') AS STADFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����ȫ��') AS STDFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ���ȫ��') AS STARFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����������') AS HADFF, \n");
	        sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc) as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ�����') AS HARFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����������Ͳ��ֵ') AS QBADFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append(" WHERE xiangmmc = '�������λ��ֵ') AS QGRDFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc) as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����������λ��ֵ') AS QGRADFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc) as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����޻һ���λ��ֵ') AS QGRDAFFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ�����λ��ֵ') AS QNETARFF \n");
		    sqlHuaybgd.append("FROM guobb) g)");
			ResultSet rs = con.getResultSet(sqlHuaybgd);
			String shangjshry = "";
			String lury = "";
			String[][] ArrHeader = new String[22][6];
			try {
				if (rs.next()) {
					lury = rs.getString("HUAYY");
					StringBuffer buffer = new StringBuffer();
					String cheph = rs.getString("CHEPH");
					String[] list = cheph.split(",");
					for (int i = 1; i <= list.length; i++) {
						if (i % 11 == 0) {
							buffer.append(list[i - 1] + ",<br>");
						} else {
							buffer.append(list[i - 1] + ",");
						}
					}
					cheph = buffer.toString().substring(0, buffer.length() - 1);
					String num = rs.getString("FRL");
					ArrHeader[0] = new String[] { "������",
							"" + rs.getString("BIANH") + "", "���",
							"" + rs.getString("MEIKDWMC") + "", "��վ",
							"" + rs.getString("chez") + "" };
					
					ArrHeader[1] = new String[] { "��������",
							"" + rs.getString("HUAYRQ") + "", "��������",
							"" + rs.getString("CAIYRQ") + "", "ú��(t)",
							"" + rs.getString("MEIL") + "" };
					ArrHeader[2] = new String[] { "ú��",
							"" + rs.getString("PINZ") + "", "��������Ա",
							"" + rs.getString("lurry") + "",
							"" + rs.getString("lurry") + "",
							"" + rs.getString("lurry") + "" };
					ArrHeader[3] = new String[] { "����", "" + cheph + "",
							"" + cheph + "", "" + cheph + "", "" + cheph + "",
							"" + cheph + "" };
					ArrHeader[4] = new String[] { "ȫˮ��Mt(%)", "ȫˮ��Mt(%)",
							"ȫˮ��Mt(%)", "" + rs.getDouble("MT") + "", ""+rs.getString("MTFF")+"", ""+rs.getString("MTFF")+"" };
					ArrHeader[5] = new String[] { "���������ˮ��Mad(%)", "���������ˮ��Mad(%)",
							"���������ˮ��Mad(%)", "" + rs.getDouble("MAD") + "", ""+rs.getString("MADFF")+"", ""+rs.getString("MADFF")+"" };
					ArrHeader[6] = new String[] { "����������ҷ�Aad(%)", "����������ҷ�Aad(%)",
							"����������ҷ�Aad(%)", "" + rs.getDouble("AAD") + "", ""+rs.getString("AADFF")+"", ""+rs.getString("AADFF")+""};
					ArrHeader[7] = new String[] { "�յ����ҷ�Aar(%)", "�յ����ҷ�Aar(%)",
							"�յ����ҷ�Aar(%)", "" + rs.getDouble("AAR") + "", ""+rs.getString("AARFF")+"", ""+rs.getString("AARFF")+"" };
					ArrHeader[8] = new String[] { "������ҷ�Ad(%)", "������ҷ�Ad(%)",
							"������ҷ�Ad(%)", "" + rs.getDouble("AD") + "", ""+rs.getString("ADFF")+"", ""+rs.getString("ADFF")+"" };
					ArrHeader[9] = new String[] { "����������ӷ���Vad(%)",
							"����������ӷ���Vad(%)", "����������ӷ���Vad(%)",
							"" + rs.getDouble("VAD") + "", ""+rs.getString("VADFF")+"", ""+rs.getString("VADFF")+"" };
					ArrHeader[10] = new String[] { "�����޻һ��ӷ���Vdaf(%)",
							"�����޻һ��ӷ���Vdaf(%)", "�����޻һ��ӷ���Vdaf(%)",
							"" + rs.getDouble("VDAF") + "", ""+rs.getString("VDAFFF")+"", ""+rs.getString("VDAFFF")+"" };
					ArrHeader[11] = new String[] { "���������ȫ��St,ad(%)",
							"���������ȫ��St,ad(%)", "���������ȫ��St,ad(%)",
							"" + rs.getDouble("STAD") + "", ""+rs.getString("STADFF")+"", ""+rs.getString("STADFF")+"" };
					ArrHeader[12] = new String[] { "�����ȫ��St,d(%)", "�����ȫ��St,d(%)",
							"�����ȫ��St,d(%)", "" + rs.getDouble("STD") + "", ""+rs.getString("STDFF")+"", ""+rs.getString("STDFF")+"" };
					ArrHeader[13] = new String[] { "�յ���ȫ��St,ar(%)",
							"�յ���ȫ��St,ar(%)", "�յ���ȫ��St,ar(%)",
							"" + rs.getDouble("STAR") + "", ""+rs.getString("STARFF")+"", ""+rs.getString("STARFF")+"" };
					ArrHeader[14] = new String[] { "�����������Had(%)", "�����������Had(%)",
							"�����������Had(%)", "" + rs.getDouble("HAD") + "", ""+rs.getString("HADFF")+"", ""+rs.getString("HADFF")+"" };
					ArrHeader[15] = new String[] { "�յ�����Har(%)", "�յ�����Har(%)",
							"�յ�����Har(%)", "" + rs.getDouble("HAR") + "", ""+rs.getString("HARFF")+"", ""+rs.getString("HARFF")+"" };
					ArrHeader[16] = new String[] { "�����������Ͳ��ֵQb,ad(J/g)",
							"�����������Ͳ��ֵQb,ad(J/g)", "�����������Ͳ��ֵQb,ad(J/g)",
							"" + rs.getDouble("QBAD") + "",""+rs.getString("QBADFF")+"", ""+rs.getString("QBADFF")+"" };
					ArrHeader[17] = new String[] { "�������λ��ֵQgr,d(J/g)",
							"�������λ��ֵQgr,d(J/g)", "�������λ��ֵQgr,d(J/g)",
							"" + rs.getDouble("QGRD") + "", ""+rs.getString("QGRDFF")+"", ""+rs.getString("QGRDFF")+"" };
					ArrHeader[18] = new String[] { "�����������λ��ֵQgr,ad(J/g)",
							"�����������λ��ֵQgr,ad(J/g)", "�����������λ��ֵQgr,ad(J/g)",
							"" + rs.getDouble("QGRAD") + "",""+rs.getString("QGRADFF")+"", ""+rs.getString("QGRADFF")+"" };
					ArrHeader[19] = new String[] { "�����޻һ���λ��ֵQgr,daf(J/g)",
							"�����޻һ���λ��ֵQgr,daf(J/g)", "�����޻һ���λ��ֵQgr,daf(J/g)",
							"" + rs.getDouble("GANZWHJGWRZ") + "",""+rs.getString("QGRDAFFF")+"", ""+rs.getString("QGRDAFFF")+"" };
					ArrHeader[20] = new String[] { "�յ�����λ��ֵQnet,ar(J/g)",
							"�յ�����λ��ֵQnet,ar(J/g)", "�յ�����λ��ֵQnet,ar(J/g)",
							"" + rs.getDouble("QNETAR") + "", ""+rs.getString("QNETARFF")+"", ""+rs.getString("QNETARFF")+"" };
					ArrHeader[21] = new String[] { "","", "",
							"" + num + "" + "(ǧ��/ǧ��)", "","" };
				} else
					return null;
			} catch (Exception e) {
				System.out.println(e);
			}	
			int[] ArrWidth = new int[] { 100, 95, 95, 155, 95, 95 };
			
			rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
			rt.title.setRowHeight(2, 35);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			
			String str = DateUtil.FormatDate(new Date());
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 2, "��ӡ����:" + str, -1);
			rt.setDefautlFooter(3, 1, "�����ˣ�", -1);
			rt.setDefautlFooter(4, 1, "��ˣ�" + shangjshry, -1);
			rt.setDefautlFooter(5, 2, "����Ա��" + lury, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
			
			rt.setBody(new Table(22, 6));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(24);
			String[][] ArrHeader1 = new String[1][6];
			ArrHeader1[0] = ArrHeader[0];
			rt.body.setHeaderData(ArrHeader1);// ��ͷ����
			for (int i = 1; i < 22; i++) {
				for (int j = 0; j < 6; j++) {
					if (ArrHeader[i][j]==null || ArrHeader[i][j].length() == 0) {
						ArrHeader[i][j] = "0";
					}
					rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
				}
			}
			for (int i = 4; i < 22; i++) {
				for (int j = 4; j < 6; j++) {
					if (ArrHeader[i][j].equals("null") || ArrHeader[i][j].length() == 0) {
						ArrHeader[i][j] = "";
					}
					rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
				}
			}
		}else{
			StringBuffer sqlHuaybgd = new StringBuffer();
			sqlHuaybgd
			.append("select * from (select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,(select distinct GetCaiyry(caiyryglb.yangpdhb_id) cai \n");
			sqlHuaybgd
			.append("from caiyryglb where yangpdhb_id = y.id) lurry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
			sqlHuaybgd
			.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(0,2) as STAD,round_new(0,2) as STD,\n");
			sqlHuaybgd
			.append("ROUND_NEW(0, 2) AS STAR,round_new(0,2) as HAD,round_new(0,2) as HAR,round_new(z.qbad,2)*1000 as QBAD,round_new(z.qgrd,2)*1000 as QGRD,round_new(z.qgrad,2)*1000 as QGRAD,\n");
			sqlHuaybgd
			.append("round_new(z.qgrad_daf,2)*1000 as GANZWHJGWRZ,round_new(z.qnet_ar,2)*1000 as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,' ' as shenhry,\n");
			sqlHuaybgd
			.append("'"
					+ getBianmValue().getValue()
					+ "' as bianh,' ' as meikdwmc,a.chez,a.pinz,a.cheph,a.meil from zhillsb z,yangpdhb y,\n");
			sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
			sqlHuaybgd.append("' ' as chez,\n");
			sqlHuaybgd.append("p.mingc as pinz,\n");
			sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
			sqlHuaybgd
			.append("round_new(sum(f.laimsl), 2) as meil,\n");
			sqlHuaybgd.append("sum(f.ches) AS CHEPH \n");
			sqlHuaybgd
			.append("from fahb f, zhillsb z,meikxxb m,chezxxb cz,pinzb p\n");
			sqlHuaybgd.append("where z.zhilb_id = f.zhilb_id\n");
			sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
			sqlHuaybgd.append("and f.faz_id=cz.id\n");
			sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
			sqlHuaybgd.append("and z.id = " + getBianmValue().getId()).append("\n");
			sqlHuaybgd.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id) a \n");
			sqlHuaybgd.append("where z.zhilb_id=a.zhilb_id\n");
			sqlHuaybgd.append("and y.zhilblsb_id=z.id\n");
			sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
			sqlHuaybgd.append("), (select * from (SELECT DISTINCT (SELECT decode(mingc,null,' ',mingc) as mingc \n");
			sqlHuaybgd.append(" FROM guobb \n");
			sqlHuaybgd.append("WHERE xiangmmc = 'ȫˮ��') AS MTFF,\n");
			sqlHuaybgd.append(" (SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("   FROM guobb \n");
			sqlHuaybgd.append("  WHERE xiangmmc = '���������ˮ��') AS MADFF,\n");
			sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
			sqlHuaybgd.append("FROM guobb \n");
			sqlHuaybgd.append("WHERE xiangmmc = '����������ҷ�') AS AADFF,\n");
			sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
			sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ����ҷ�') AS AARFF,\n");
			sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
			sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '������ҷ�') AS ADFF,\n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '����������ӷ���') AS VADFF, \n");
	        sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc) as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����޻һ��ӷ���') AS VDAFFF,\n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '���������ȫ��') AS STADFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����ȫ��') AS STDFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ���ȫ��') AS STARFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����������') AS HADFF, \n");
	        sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ�����') AS HARFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����������Ͳ��ֵ') AS QBADFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append(" WHERE xiangmmc = '�������λ��ֵ') AS QGRDFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����������λ��ֵ') AS QGRADFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append(" FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�����޻һ���λ��ֵ') AS QGRDAFFF, \n");
		    sqlHuaybgd.append("(SELECT decode(mingc,null,' ',mingc)  as mingc \n");
		    sqlHuaybgd.append("FROM guobb \n");
		    sqlHuaybgd.append("WHERE xiangmmc = '�յ�����λ��ֵ') AS QNETARFF \n");
		    sqlHuaybgd.append("FROM guobb) g)");
			ResultSet rs = con.getResultSet(sqlHuaybgd);
			String shangjshry = "";
			String lury = "";
			String[][] ArrHeader = new String[22][6];
			try {
				if (rs.next()) {
					lury = rs.getString("HUAYY");
					StringBuffer buffer = new StringBuffer();
					String cheph = rs.getString("CHEPH");
					String[] list = cheph.split(",");
					for (int i = 1; i <= list.length; i++) {
						if (i % 11 == 0) {
							buffer.append(list[i - 1] + ",<br>");
						} else {
							buffer.append(list[i - 1] + ",");
						}
					}
					cheph = buffer.toString().substring(0, buffer.length() - 1);
					String num = rs.getString("FRL");
					ArrHeader[0] = new String[] { "������",
							"" + rs.getString("BIANH") + "", "���",
							"" + rs.getString("MEIKDWMC") + "", "��վ",
							"" + rs.getString("chez") + "" };
					
					ArrHeader[1] = new String[] { "��������",
							"" + rs.getString("HUAYRQ") + "", "��������",
							"" + rs.getString("CAIYRQ") + "", "ú��(t)",
							"" + rs.getString("MEIL") + "" };
					ArrHeader[2] = new String[] { "ú��",
							"" + rs.getString("PINZ") + "", "��������Ա",
							"" + rs.getString("lurry") + "",
							"" + rs.getString("lurry") + "",
							"" + rs.getString("lurry") + "" };
					ArrHeader[3] = new String[] { "����", "" + cheph + "",
							"" + cheph + "", "" + cheph + "", "" + cheph + "",
							"" + cheph + "" };
					ArrHeader[4] = new String[] { "ȫˮ��Mt(%)", "ȫˮ��Mt(%)",
							"ȫˮ��Mt(%)", "" + rs.getDouble("MT") + "", ""+rs.getString("MTFF")+"", ""+rs.getString("MTFF")+"" };
					ArrHeader[5] = new String[] { "���������ˮ��Mad(%)", "���������ˮ��Mad(%)",
							"���������ˮ��Mad(%)", "" + rs.getDouble("MAD") + "", ""+rs.getString("MADFF")+"", ""+rs.getString("MADFF")+"" };
					ArrHeader[6] = new String[] { "����������ҷ�Aad(%)", "����������ҷ�Aad(%)",
							"����������ҷ�Aad(%)", "" + rs.getDouble("AAD") + "", ""+rs.getString("AADFF")+"", ""+rs.getString("AADFF")+""};
					ArrHeader[7] = new String[] { "�յ����ҷ�Aar(%)", "�յ����ҷ�Aar(%)",
							"�յ����ҷ�Aar(%)", "" + rs.getDouble("AAR") + "", ""+rs.getString("AARFF")+"", ""+rs.getString("AARFF")+"" };
					ArrHeader[8] = new String[] { "������ҷ�Ad(%)", "������ҷ�Ad(%)",
							"������ҷ�Ad(%)", "" + rs.getDouble("AD") + "", ""+rs.getString("ADFF")+"", ""+rs.getString("ADFF")+"" };
					ArrHeader[9] = new String[] { "����������ӷ���Vad(%)",
							"����������ӷ���Vad(%)", "����������ӷ���Vad(%)",
							"" + rs.getDouble("VAD") + "", ""+rs.getString("VADFF")+"", ""+rs.getString("VADFF")+"" };
					ArrHeader[10] = new String[] { "�����޻һ��ӷ���Vdaf(%)",
							"�����޻һ��ӷ���Vdaf(%)", "�����޻һ��ӷ���Vdaf(%)",
							"" + rs.getDouble("VDAF") + "", ""+rs.getString("VDAFFF")+"", ""+rs.getString("VDAFFF")+"" };
					ArrHeader[11] = new String[] { "���������ȫ��St,ad(%)",
							"���������ȫ��St,ad(%)", "���������ȫ��St,ad(%)",
							"" + rs.getDouble("STAD") + "", ""+rs.getString("STADFF")+"", ""+rs.getString("STADFF")+"" };
					ArrHeader[12] = new String[] { "�����ȫ��St,d(%)", "�����ȫ��St,d(%)",
							"�����ȫ��St,d(%)", "" + rs.getDouble("STD") + "", ""+rs.getString("STDFF")+"", ""+rs.getString("STDFF")+"" };
					ArrHeader[13] = new String[] { "�յ���ȫ��St,ar(%)",
							"�յ���ȫ��St,ar(%)", "�յ���ȫ��St,ar(%)",
							"" + rs.getDouble("STAR") + "", ""+rs.getString("STARFF")+"", ""+rs.getString("STARFF")+"" };
					ArrHeader[14] = new String[] { "�����������Had(%)", "�����������Had(%)",
							"�����������Had(%)", "" + rs.getDouble("HAD") + "", ""+rs.getString("HADFF")+"", ""+rs.getString("HADFF")+"" };
					ArrHeader[15] = new String[] { "�յ�����Har(%)", "�յ�����Har(%)",
							"�յ�����Har(%)", "" + rs.getDouble("HAR") + "", ""+rs.getString("HARFF")+"", ""+rs.getString("HARFF")+"" };
					ArrHeader[16] = new String[] { "�����������Ͳ��ֵQb,ad(J/g)",
							"�����������Ͳ��ֵQb,ad(J/g)", "�����������Ͳ��ֵQb,ad(J/g)",
							"" + rs.getDouble("QBAD") + "",""+rs.getString("QBADFF")+"", ""+rs.getString("QBADFF")+"" };
					ArrHeader[17] = new String[] { "�������λ��ֵQgr,d(J/g)",
							"�������λ��ֵQgr,d(J/g)", "�������λ��ֵQgr,d(J/g)",
							"" + rs.getDouble("QGRD") + "", ""+rs.getString("QGRDFF")+"", ""+rs.getString("QGRDFF")+"" };
					ArrHeader[18] = new String[] { "�����������λ��ֵQgr,ad(J/g)",
							"�����������λ��ֵQgr,ad(J/g)", "�����������λ��ֵQgr,ad(J/g)",
							"" + rs.getDouble("QGRAD") + "",""+rs.getString("QGRADFF")+"", ""+rs.getString("QGRADFF")+"" };
					ArrHeader[19] = new String[] { "�����޻һ���λ��ֵQgr,daf(J/g)",
							"�����޻һ���λ��ֵQgr,daf(J/g)", "�����޻һ���λ��ֵQgr,daf(J/g)",
							"" + rs.getDouble("GANZWHJGWRZ") + "",""+rs.getString("QGRDAFFF")+"", ""+rs.getString("QGRDAFFF")+"" };
					ArrHeader[20] = new String[] { "�յ�����λ��ֵQnet,ar(J/g)",
							"�յ�����λ��ֵQnet,ar(J/g)", "�յ�����λ��ֵQnet,ar(J/g)",
							"" + rs.getDouble("QNETAR") + "", ""+rs.getString("QNETARFF")+"", ""+rs.getString("QNETARFF")+"" };
					ArrHeader[21] = new String[] { "","", "",
							"" + num + "" + "(ǧ��/ǧ��)", "","" };
				} else
					return null;
			} catch (Exception e) {
				System.out.println(e);
			}	
			int[] ArrWidth = new int[] { 100, 95, 95, 155, 95, 95 };
			
			rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
			rt.title.setRowHeight(2, 35);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			
			String str = DateUtil.FormatDate(new Date());
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 2, "��ӡ����:" + str, -1);
			rt.setDefautlFooter(3, 1, "�����ˣ�", -1);
			rt.setDefautlFooter(4, 1, "��ˣ�" + shangjshry, -1);
			rt.setDefautlFooter(5, 2, "����Ա��" + lury, Table.ALIGN_RIGHT);
			rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
			
			rt.setBody(new Table(22, 6));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(24);
			String[][] ArrHeader1 = new String[1][6];
			ArrHeader1[0] = ArrHeader[0];
			rt.body.setHeaderData(ArrHeader1);// ��ͷ����
			for (int i = 1; i < 22; i++) {
				for (int j = 0; j < 6; j++) {
					if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
						ArrHeader[i][j] = "0";
					}
					rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
				}
			}
			for (int i = 4; i < 22; i++) {
				for (int j = 4; j < 6; j++) {
					if (ArrHeader[i][j].equals("null") || ArrHeader[i][j].length() == 0) {
						ArrHeader[i][j] = "";
					}
					rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
				}
			}
		}
		for (int i = 1; i <= 22; i++) {
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
		rt.body.setCells(2, 1, 22, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.merge(2, 1, 22, 3);
		for (int i = 5; i < 23; i++) {
			rt.body.mergeCell(i, 5, i, 6);
		}
//		rt.body.merge(2, 5, 22, 6);
		rt.body.merge(4, 2, 4, 6);
		rt.body.merge(3, 4, 3, 6);
		rt.body.ShowZero = false;
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(35);
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

	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if (visit.isFencb()) {
			tb1.addText(new ToolbarText("����:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
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
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			getSelectData();
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}
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
		sb
				.append(
						"select l.id,z.bianm from zhuanmb z,zhillsb l,caiyb c\n")
				.append(
						"where z.zhillsb_id= l.id and c.zhilb_id = l.zhilb_id\n")
				.append("and l.huaysj = ")
				.append(DateUtil.FormatOracleDate(getRiq()))
				.append("\n")
				.append("and z.zhuanmlb_id = \n")
				.append(
						"(select id from zhuanmlb where jib = (select nvl(max(jib),0) from zhuanmlb))");
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
