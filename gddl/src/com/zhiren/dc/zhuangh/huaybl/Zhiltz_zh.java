package com.zhiren.dc.zhuangh.huaybl;
/*
 * ����:tzf
 * ʱ��:2009-10-16
 * �޸�����:���ӷ���getZhiltz_pz����  �õ���Ʒ��С�Ƶı���
 */
/*
 * ����:���
 * ʱ��:2009-05-12
 * �޸����ݣ��޸Ĳ�ѯ���qgrd*1000��������С��λ�������ɻ���λ�ȵĵ�λ��ʾ
 */
/*
 * ����:tzf
 * ʱ��:2009-05-11
 * ����:��Զ�ɶ໯��ʵ�ֲ�ͬ����Դ(zhilb  zhillsb)������չʾ
 */
/*
 * 2009-04-17
 * ����
 * ���䷽ʽ����ȫ��ѡ��
 */
/*
 * ����:tzf
 * ʱ��:2009-4-16
 * �޸����ݣ����� �糧���� ���������ܳ���ѯ ��������
 */
/*
* ���ߣ�ww
* ʱ�䣺2009-12-26
* ������1�����ѡ�񱨱�ͳ��ʱ���ǰ��������ڻ��ǰ��������ڣ�Ĭ��Ϊ��������
* 	   ���ò�����
* 		insert into xitxxb values(
			getnewid(257),
			1,
			257,                 --diancxxbID
			'��������ͳ������',
			'fahrq',             --fahrq,daohrq
			'',
			'����',
			1,
			'ʹ��'
		)
*/
import java.sql.ResultSet;
import java.util.ArrayList;
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
import com.zhiren.common.ResultSetList;
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
 * ���ߣ�����
 * ʱ�䣺2009-10-21
 * �������޸Ĺ�Ӧ��ú���ϵ�󱨱��ѯ���иı�getZhiltz_pz
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-21 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2011-08-31 
 * ���÷�Χ��ׯ�ӵ糧
 * ������������������λ�������ֶ�
 * 		ȡqgradֵʱ�������ӳ�����Ϊ����ж�
 */
public class Zhiltz_zh extends BasePage {
	
	private static final String REPORTNAME_HUAYBGD_ZHILB="Zhiltz_zhilb";//��û��������Դ���� ��Դ����ȷҲ�Դ�Ĭ��ȡ��
	private static final String REPORTNAME_HUAYBGD_ZHILLSB="Zhiltz_zhillsb"; 
	
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	// ***************������Ϣ��******************//
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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	private String TAIZ = "Zhiltz";

	private String YUEB = "Zhilyb";

	private boolean blnIsBegin = false;

	// private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;
		
		if(this.mstrReportName==null || this.mstrReportName.equals("")){
			return getZhiltz();
		}else if(this.mstrReportName.equals("zhilpz")){
			return this.getZhiltz_pz();
		}
		
		return "";

	}
	
//	�����Ʊ���Ĭ�ϵ�ǰ�û�
	private String getZhibr(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String zhibr="";
		String zhi="��";
		String sql="select zhi from xitxxb where mingc='�±������Ʊ����Ƿ�Ĭ�ϵ�ǰ�û�' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSet rs=con.getResultSet(sql);
		try{
		  while(rs.next()){
			  zhi=rs.getString("zhi");
		  }
		}catch(Exception e){
			System.out.println(e);
		}
		if(zhi.equals("��")){
			zhibr=visit.getRenymc();
		}	
		return zhibr;
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
	
    private String getTongjRq(JDBCcon con) {
    	Visit visit = (Visit) getPage().getVisit();
    	if (con == null){
    		con = new JDBCcon();
    	}
		String tongjrq=" select * from xitxxb where mingc='��������ͳ������'  and zhuangt=1 and leib='����' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(tongjrq);
		
		String strTongjrq="daohrq";
		
		if(rsl.next()){
			strTongjrq=rsl.getString("zhi");
		}
		
		rsl.close();
		con.Close();
		return strTongjrq;
    }

	// ȫ������
	/*
	 * ������������jingz��Ϊ������laimsl�ֶ� ���Ե�Ͳ��ֵ����������MJ������������Kcal��������Լ �޸�ʱ�䣺2008-12-04
	 * �޸��ˣ�����
	 */
	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String strTongjRq = getTongjRq(con);
		String pinz = "";
		String s = "";

		if (!this.hasDianc(this.getTreeid_dc())) {
			s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
																		// ����������;
		}
		
		if(getPinzValue().getId()!=-1){
			pinz = " and f.pinzb_id="+getPinzValue().getId();
		}
		
		String YunsfsSql = "";
		if (getYunsfsValue().getId() != -1) {
			YunsfsSql = "           and f.yunsfsb_id = "
					+ getYunsfsValue().getId() + "\n";
		}
		
		String source_table="";
		String source_con="";
		if(this.getDataSource().equals(REPORTNAME_HUAYBGD_ZHILLSB)){
			
			source_table="zhillsb z";
			source_con="z.zhilb_id";
		}else{
			source_table="zhilb z";
			source_con="z.id";
		}

		StringBuffer buffer = new StringBuffer();

		buffer
				.append("select fhdw,\n"
						+ "       mkdw,\n"
						+ "       daohrq,\n"
						+ "       pz,\n"
						+ "       fz,\n"
						+ "       shuicl,\n"
						+ "       biaoz,\n"
						+ "       mt,\n"
						+ "       mad,\n"
						+ "       aad,\n"
						+ "       vad,\n"
						+"       stad,\n"
						+ "       had,\n"
						+"       fcad,\n"
						+ "       aar,\n"
						+ "       vdaf,\n"
						+ "       ad," 
						+
								"vd,\n"
						+ "       qbad,\n"
//						+

//						+ "       std,star,\n"
//						+

						+ "       qgrd,qgrad,farl\n"
//						+ "      , farl,\n"
//						+ "       round_new(farl*1000/4.1816,0) as qbar\n"
						+ "  from (select decode(grouping(g.mingc), 1, '�ܼ�', g.mingc) as fhdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc),\n"
						+ "                      1,\n"
						+ "                      '�ϼ�',\n"
						+ "                      m.mingc) mkdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc) +\n"
						+ "                      grouping(f.daohrq),\n"
						+ "                      1,\n"
						+ "                      'С��',\n"
						+ "                      to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
						+ "               p.mingc pz,\n"
						+ "               c.mingc fz,\n"
						+ "               sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) shuicl,\n"
						+ "               sum(f.biaoz) biaoz,\n"
						+ "               sum(f.yuns) yuns,\n"
						+ "               sum(f.yingk) yingk,\n"
						+ "               sum(f.zongkd) zongkd,\n"
						+ "               sum(f.ches) ches,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.mt,"
						+ v.getMtdec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getMtdec()
						+ ")) as mt,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.mad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as mad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as aad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.ad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as ad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aar * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as aar,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as vad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vdaf * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as vdaf,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qbad,"
						+ v.getFarldec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as qbad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) /\n"
						+ "                                          sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ "))\n"
						+ "                                           * 1000 / 4.1816,\n"
						+ "                                0)) as qbar,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as farl,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.sdaf * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as sdaf,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.stad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as stad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.std * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as std,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as star,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.hdaf * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as hdaf,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.had * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as had,\n"
						+

						"               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.fcad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as fcad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.qgrd * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as qgrd,round_new((sum(z.vad)/(sum(100-z.mad)))*100,2) as vd ,\n" 
//						+" round_new(sum(z.qgrad * round_new(f.sanfsl,2)) / sum(round_new(f.sanfsl,2)), 2) as qgrad,\n"
						+"       decode(sum(round_new(f.sanfsl,2)),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+"				round_new(sum(z.qgrad * round_new(f.sanfsl,"+v.getShuldec()+")) / sum(round_new(f.sanfsl,"+v.getShuldec()+")), 2)) as qgrad,"	
						+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
						+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
//						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, "+source_table+"\n"
						+ "         where f.gongysb_id = g.id(+)\n"
						+

						""
						+ s
						+ pinz +

						"           and f.meikxxb_id = m.id\n"
						+ "           and f.pinzb_id = p.id\n"
						+ "           and f.faz_id = c.id\n"
//						+ "           and f.zhilb_id = z.id\n"
						+ "           and f.zhilb_id = "+source_con+"\n"
						+ "           and f." + strTongjRq + " >= to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')\n"
						+ "           and f." + strTongjRq + " <= to_date('"
						+ getRiq2()
						+ "', 'yyyy-mm-dd')\n"
						+ YunsfsSql
						+ "         group by rollup(g.mingc, m.mingc, f.daohrq, p.mingc, c.mingc)\n"
						+ "        having grouping(f.daohrq) = 1 or grouping(c.mingc) = 0\n"
						+ "         order by dx, fhdw, mx, mkdw,");
		ResultSetList paix = con.getResultSetList("select zhi from xitxxb where mingc='�ʼ�̨��������������' and leib='����' and zhuangt=1");
		if(paix.next()){
			if(paix.getString("zhi").equals("��")){
				buffer.append(" daohrq)");
			}else{
				buffer.append(" daohrq desc)");
			}
		}
		

//		System.out.println(buffer.toString());
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][21];//25

		ArrHeader[0] = new String[] { "������λ", "ú��λ", "��������", "Ʒ��", "��վ",
				"ˮ����<br>(��)", "�˵���<br>(��)", "ȫˮ<br>��<br>Mt(%)",
				"����<br>����<br>��ˮ<br>��<br>Mad(%)",
				"����<br>����<br>����<br>��<br>Aad(%)",
				"������<br>�����<br>����<br>Vad(%)",
				"����<br>����<br>����<br>St,ad<br>(%)",
				"����<br>����<br>����<br>Had<br>(%)",
				"�̶�<br>̼<br>Fcad<br>(%)", 
				"�յ�<br>��<br>�ҷ�<br>Aar(%)",
				"������<br>�һ���<br>����<br>Vdaf(%)", 
				"����<br>��<br>�ҷ�<br>Ad(%)",
				"����<br>����<br>����<br>Vd(%)",//Vd(%):������ӷ���
				"��Ͳ��<br>����<br>Qb,ad<br>(MJ/kg)",
				"�ɻ�<br>��λ<br>��<br>Qgrd<br>(MJ/kg)" ,
				"����<br>�����<br>��λ��ֵ<br>Qgr,ad<br>(MJ/kg)"//: 
//				"�յ�<br>��ȫ<br>��(%)<br>St,ar", 
//				"����<br>�޻�<br>����<br>(%)<br>Hdaf",

				,"�յ���<br>��λ��<br>����(MJ/Kg)<br>Qnet,ar",
//				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)"
				};
		int[] ArrWidth = new int[21];

		ArrWidth = new int[] { 80, 100,80, 50, 50, 40, 50, 40, 40, 40, 40, 40,
				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };

		rt.setTitle("ú  ��  ��  ��  ̨  ��", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "жú����:" + getRiqi() + "��" + getRiq2(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

//		String[] strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
//				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", 
////				"", "",
//				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

		rt.setBody(new Table(rs, 1, 0, 3));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),pagewith);//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.mergeFixedCols();
//		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 20; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "�Ʊ�:"+getZhibr(), Table.ALIGN_LEFT);
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
	
	//��������̨�� ����Ʒ��С�ƹ���
	private String getZhiltz_pz(){

		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		String s = "";
		String pinz = "";

		if (!this.hasDianc(this.getTreeid_dc())) {
			s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
																		// ����������;
		}
		if(getPinzValue().getId()!=-1){
			pinz = " and f.pinzb_id="+getPinzValue().getId();
		}
		
		String YunsfsSql = "";
		if (getYunsfsValue().getId() != -1) {
			YunsfsSql = "           and f.yunsfsb_id = "
					+ getYunsfsValue().getId() + "\n";
		}
		
		String source_table="";
		String source_con="";
		if(this.getDataSource().equals(REPORTNAME_HUAYBGD_ZHILLSB)){
			
			source_table="zhillsb z";
			source_con="z.zhilb_id";
		}else{
			source_table="zhilb z";
			source_con="z.id";
		}

		StringBuffer buffer = new StringBuffer();

		buffer
				.append("select fhdw,\n"
						+ "       mkdw,\n"
						+ "       pz,\n"
						+ "       daohrq,\n"
						+ "       fz,\n"
						+ "       jingz,\n"
						+ "       ches,\n"
						+ "       mt,\n"
						+ "       mad,\n"
						+ "       aad,\n"
						+ "       ad,\n"
						+ "       aar,\n"
						+ "       vad,\n"
						+ "       vdaf,\n"
						+ "       qbad*1000,\n"
//						+ "       farl*1000,\n"
//						+ "       round_new(farl*1000/4.1816,0) as qbar,\n"
						+

						"       sdaf,stad,\n"
						+ "       std,star,\n"
						+ "       hdaf,had,\n"
						+

						"       fcad,\n"
						+ "       qgrd*1000\n"
						+ "  from (select decode(grouping(g.mingc), 1, '�ܼ�', g.mingc) as fhdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc),\n"
						+ "                      1,\n"
						+ "                      '�ϼ�',\n"
						+ "                      m.mingc) mkdw,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc) +\n"
						+ "                       grouping(p.mingc),\n"
						+ "                      1,\n"
						+ "                      'С��',\n"
						+ "                      p.mingc ) pz,\n"
						+ "               decode(grouping(g.mingc) + grouping(m.mingc) + grouping(p.mingc)+ \n"
						+"                  grouping(f.daohrq), 1, 'С��',  to_char(f.daohrq, 'yyyy-mm-dd')) daohrq,\n"
						+ "               c.mingc fz,\n"
						+ "               sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) jingz,\n"
						+ "               sum(f.biaoz) biaoz,\n"
						+ "               sum(f.yuns) yuns,\n"
						+ "               sum(f.yingk) yingk,\n"
						+ "               sum(f.zongkd) zongkd,\n"
						+ "               sum(f.ches) ches,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.mt,"
						+ v.getMtdec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getMtdec()
						+ ")) as mt,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.mad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as mad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as aad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.ad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as ad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aar * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as aar,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as vad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vdaf * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as vdaf,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qbad,"
						+ v.getFarldec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as qbad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) /\n"
						+ "                                          sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ "))\n"
						+ "                                           * 1000 / 4.1816,\n"
						+ "                                0)) as qbar,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), "
						+ v.getFarldec()
						+ ")) as farl,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.sdaf * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as sdaf,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.stad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as stad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.std * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as std,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as star,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.hdaf * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as hdaf,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.had * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as had,\n"
						+

						"               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.fcad * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as fcad,\n"
						+ "               decode(sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.qgrd * round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.sanfsl,"
						+ v.getShuldec()
						+ ")), 2)) as qgrdround_new((z.vad/(100-z.mad))*100,2) as vd ,\n" +
								" round_new(sum(z.qgrad * round_new(f.sanfsl,2)) / sum(round_new(f.sanfsl,2)), 2)) as qgrad,\n"
						+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
						+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
//						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, "+source_table+"\n"
						+ "         where m.meikdq_id = g.id\n"
						+

						""
						+ s
						+ pinz +

						"           and f.meikxxb_id = m.id\n"
						+ "           and f.pinzb_id = p.id\n"
						+ "           and f.faz_id = c.id\n"
//						+ "           and f.zhilb_id = z.id\n"
						+ "           and f.zhilb_id = "+source_con+"\n"
						+ "           and f.daohrq >= to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')\n"
						+ "           and f.daohrq <= to_date('"
						+ getRiq2()
						+ "', 'yyyy-mm-dd')\n"
						+ YunsfsSql
						+ "         group by rollup(g.mingc, m.mingc,p.mingc, f.daohrq, c.mingc)\n"
						+ "        having grouping(f.daohrq) = 1 or grouping(c.mingc) = 0\n"
						+ "          order by dx, fhdw, mx, mkdw,grouping(p.mingc) desc,pz, daohrq desc)");

//		System.out.println(buffer.toString());
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][23];//25

		ArrHeader[0] = new String[] { "������λ", "ú��λ",  "Ʒ��","��������", "��վ",
				"������<br>��(��)", "����", "ȫˮ<br>��<br>(%)Mt",
				"����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad", "����<br>��<br>�ҷ�<br>(%)Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", "������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", "��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
//				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
//				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>(%)<br>Sdaf",
				"����<br>����<br>����<br>(%)<br>St,ad", "����<br>��ȫ<br>��(%)<br>St,d",
				"�յ�<br>��ȫ<br>��(%)<br>St,ar", "����<br>�޻�<br>����<br>(%)<br>Hdaf",
				"����<br>����<br>����<br>(%)<br>Had",

				"�̶�<br>̼<br>(%)<br>Fcad", "�ɻ�<br>��λ<br>��<br>(J/g)<br>Qgrd" };
		int[] ArrWidth = new int[22];

		ArrWidth = new int[] { 85, 100,50, 90,  50, 40, 50, 40, 40, 40, 40, 40,
				40, 40, 40, 40, 
//				40, 40, 
				40, 40, 40, 40, 40, 40, 40, 40 };

		rt.setTitle("ú  ��  ��  ��  ̨  ��", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "жú����:" + getRiqi() + "��" + getRiq2(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", 
//				"", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

		rt.setBody(new Table(rs, 1, 0, 3));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),pagewith);//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 22; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "�Ʊ�:"+getZhibr(), Table.ALIGN_LEFT);
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

	// ���䷽ʽ������
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		String sql = "select id,mingc from yunsfsb";
		YunsfsModel = new IDropDownModel(sql, "ȫ��");
		return YunsfsModel;
	}


//	Ʒ��������
	public IDropDownBean getPinzValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getPinzModel().getOptionCount()>0) {
				setPinzValue((IDropDownBean)getPinzModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setPinzValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getPinzModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setPinzModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setPinzModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setPinzModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,mingc from pinzb order by id");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"ȫ��"));
		setPinzModel(new IDropDownModel(list,sb));
	}
	
	String pagewith=null;
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		pagewith = cycle.getRequestContext().getParameter("pw");//�ж��Ƿ�����������

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			setPinzValue(null);
			setPinzModel(null);
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);
				this.setRiq2(null);

			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}

		}
		
		
		if(cycle.getRequestContext().getParameter("ds") !=null){
			
			if(!cycle.getRequestContext().getParameter("ds").equals(this.getDataSource())){//��Ҫ��յı���
				
				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);
				this.setRiq2(null);
			}
			
			this.setDataSource(cycle.getRequestContext().getParameter("ds"));
		}else{
			if(this.getDataSource().equals("")){
				this.setDataSource("");
			}
			
		}
		blnIsBegin = true;
		getSelectData();

	}
	
	private void setDataSource(String source){
		Visit visit = (Visit) getPage().getVisit();
		
		if(source==null){
			visit.setString14(REPORTNAME_HUAYBGD_ZHILB);
			return;
		}
		if(source.equals(REPORTNAME_HUAYBGD_ZHILLSB)){
			visit.setString14(REPORTNAME_HUAYBGD_ZHILLSB);
		}else{
			visit.setString14(REPORTNAME_HUAYBGD_ZHILB);
		}
	}
	
	private String getDataSource(){
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString14();
	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

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

	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {

			Refurbish();
			_RefurbishClick = false;
		}

	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		getZhiltz();
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

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();

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
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("riq2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("YUNSFSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("Ʒ��:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("PinzSelect");
		pinz.setWidth(80);
		pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
		tb1.addField(pinz);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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
}