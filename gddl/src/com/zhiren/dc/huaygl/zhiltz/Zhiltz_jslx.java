package com.zhiren.dc.huaygl.zhiltz;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * �޸��ˣ���ʤ��	
 * �޸�ʱ�䣺2012-06-29
 * ������
 *     ������������������⣬
 */
public class Zhiltz_jslx extends BasePage implements PageValidateListener  {

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

	private String mstrReportName = "";
	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getZhiltz();
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

    //���������ж�
    private String getWhereLeix(){
		String where= "";
		long yunsfsId = getJieslxValue().getId();
		if(yunsfsId == 0){
			where = " \n";
		}else if(yunsfsId == 1){
			where = "          and f.jiesb_id=0\n";
		}else if(yunsfsId == 2){
			where = "          and f.jiesb_id<>0\n";
		}else if(yunsfsId == 3){
			where = "          and f.jiesb_id in (select id from jiesb where ruzrq is not null)\n";
		}
    	return where;
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

		String s = "";

		if (!this.hasDianc(this.getTreeid_dc())) {
			s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
																		// ����������;
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
						+ "       jingz,\n"
						+ "       biaoz,\n"
						+ "       yuns,\n"
						+ "       yingk,\n"
						+ "       ches,\n"
						+ "       mt,\n"
						+ "       mad,\n"
						+ "       aad,\n"
						+ "       ad,\n"
						+ "       aar,\n"
						+ "       vad,\n"
						+ "       vdaf,\n"
						+ "       qbad*1000,\n"
						+ "       farl*1000,\n"
						+ "       round_new(farl*1000/4.1816,0) as qbar,\n"
						+ "       stad,\n"
						+ "       std,star,\n"
						+ "      had, fcad \n"
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
						+ "               sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) jingz,\n"
						+ "               sum(f.biaoz) biaoz,\n"
						+ "               sum(round(f.yuns,2)) yuns,\n"
						+ "               sum(round(f.yingk,2)) yingk,\n"
						+ "               sum(f.zongkd) zongkd,\n"
						+ "               sum(f.ches) ches,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.mt,"
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
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.mad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as mad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as aad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.ad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as ad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.aar * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as aar,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as vad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.vdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as vdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qbad,"
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
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
						+ v.getFarldec()
						+ ") * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) /\n"
						+ "                                          sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ "))\n"
						+ "                                           * 1000 / 4.1816,\n"
						+ "                                0)) as qbar,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.qnet_ar,"
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
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.sdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as sdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.stad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as stad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.std * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as std,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as star,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.hdaf * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as hdaf,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.had * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as had,\n"
						+

						"               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.fcad * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as fcad,\n"
						+ "               decode(sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")),\n"
						+ "                      0,\n"
						+ "                      0,\n"
						+ "                      round_new(sum(z.qgrd * round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")) / sum(round_new(f.laimsl,"
						+ v.getShuldec()
						+ ")), 2)) as qgrd,\n"
						+ "               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n"
						+ "               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n"
//						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, zhilb z\n"
						+ "          from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, "+source_table+"\n"
						+ "         where f.gongysb_id = g.id\n"
						+getGysParam()+

						""
						+ s
						+

						"           and f.meikxxb_id = m.id\n"
						+ "           and f.pinzb_id = p.id\n"
						+ "           and f.faz_id = c.id\n"
//						+ "           and f.zhilb_id = z.id\n"
						+ "           and f.zhilb_id = "+source_con+"\n"
						+ "           and f.daohrq >= to_date('"
						+ getBRiq()
						+ "', 'yyyy-mm-dd')\n"
						+ "           and f.daohrq <= to_date('"
						+ getERiq()
						+ "', 'yyyy-mm-dd')\n"
						+ getWhereLeix()
						+ "         group by rollup(g.mingc, m.mingc, f.daohrq, p.mingc, c.mingc,f.chec)\n"
						+ "        having grouping(f.daohrq) = 1 or grouping(f.chec) = 0\n"
						+ "          order by grouping(g.mingc) DESC ,g.mingc, grouping(m.mingc) DESC , m.mingc, daohrq desc)");

//		System.out.println(buffer.toString());
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][25];

		ArrHeader[0] = new String[] { "������λ", "ú��λ", "��������", "Ʒ��", "��վ",
				"������<br>��(��)", "Ʊ��(��)","����(��)","ӯ��(��)","����", "ȫˮ<br>��<br>(%)Mt",
				"����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad", "����<br>��<br>�ҷ�<br>(%)Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", "������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", "��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"����<br>����<br>����<br>(%)<br>St,ad", "����<br>��ȫ<br>��(%)<br>St,d",
				"�յ�<br>��ȫ<br>��(%)<br>St,ar", 
				"����<br>����<br>����<br>(%)<br>Had",

				"�̶�<br>̼<br>(%)<br>Fcad"};
		int[] ArrWidth = new int[24];

		ArrWidth = new int[] { 85, 100, 90, 50, 50, 80, 50, 50, 50, 40, 40, 40,
				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 ,40};

		rt.setTitle("��  ��  ̨  ��", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "����:" + getBRiq() + "��" + getERiq(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "", "", "", "", "", "","","","", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00", "0.00","0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
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
		rt.setDefautlFooter(14, 4, "�Ʊ�:", Table.ALIGN_LEFT);
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
	private String BRiq;
	public String getBRiq() {
		if (BRiq == null || BRiq.equals("")) {
			BRiq = DateUtil.FormatDate(new Date());
		}
		return BRiq;
	}
	public void setBRiq(String BRiq) {
		if (this.BRiq != null && !this.BRiq.equals(BRiq)) {
			this.BRiq = BRiq;
			riqichange = true;
		}
	}

	boolean riq2change = false;
	private String ERiq;
	public String getERiq() {
		if (ERiq == null || ERiq.equals("")) {
			ERiq = DateUtil.FormatDate(new Date());
		}
		return ERiq;
	}
	public void setERiq(String ERiq) {

		if (this.ERiq != null && !this.ERiq.equals(ERiq)) {
			this.ERiq = ERiq;
			riq2change = true;
		}
	}

	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			Refurbish();
		}
	}
	private void Refurbish() {
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
		df.setValue(this.getBRiq());
		df.Binding("BRiq", "");// ��htmlҳ�е�id��
		df.setId("BRiq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getERiq());
		df1.Binding("ERiq", "");// ��htmlҳ�е�id��
		df1.setId("ERiq");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
//		��Ӧ����
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(90);
		tf.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("������λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��������:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("JieslxSelect");
		meik.setEditable(true);
		meik.setWidth(100);
//		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);

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
//	������ʹ�õķ���
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	
//  ���ѡ������ڵ�Ķ�Ӧ�Ĺ�Ӧ������   
    private String[] getGys(String id){ 
    	String[] gys={"ȫ��","-1"};
    	if(id==null || "".equals(id)){
    		return gys;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			gys[0]=rsl.getString("mingc");
			gys[1]=rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
//  ȡ�ù�Ӧ�̲���SQL
    String id=""; 
    private String getGysParam(){
//		��Ӧ��ú������
		String gyssql = "";
		if("1".equals(getGys(getTreeid())[1])){
			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
			id=getTreeid();
		}else if("0".equals(getGys(getTreeid())[1])){
			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
			id=getTreeid();
		}
		return gyssql;
    }
	
//	 ��������������
	private IDropDownBean JieslxValue;
	public IDropDownBean getJieslxValue() {
		if (JieslxValue == null) {
			JieslxValue = (IDropDownBean) JieslxModel.getOption(0);
		}
		return JieslxValue;
	}
	public void setJieslxValue(IDropDownBean Value) {
		if (!(JieslxValue == Value)) {
			JieslxValue = Value;
		}
	}
	private IPropertySelectionModel JieslxModel;
	public void setJieslxModel(IPropertySelectionModel value) {
		JieslxModel = value;
	}
	public IPropertySelectionModel getJieslxModel() {
		if (JieslxModel == null) {
			getJieslxModels();
		}
		return JieslxModel;
	}
	public IPropertySelectionModel getJieslxModels() {
		String sql = 
			"select 0 id,'ȫ��' mingc from dual\n" +
			"union all\n" + 
			"select 1 id,'δ����' mingc from dual\n" + 
			"union all\n" + 
			"select 2 id,'�ѽ���' mingc from dual\n" + 
			"union all\n" + 
			"select 3 id,'����' mingc from dual";
		JieslxModel = new IDropDownModel(sql);
		return JieslxModel;
	}
	
	//ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
//		if (cycle.getRequestContext().getParameters("lx") != null) {
//			if (!visit.getString1().equals(
//					cycle.getRequestContext().getParameters("lx")[0])) {
//
//				visit.setProSelectionModel10(null);
//				visit.setDropDownBean10(null);
//				visit.setProSelectionModel5(null);
//				visit.setDropDownBean5(null);
//				this.setBRiq(null);
//				this.setERiq(null);
//			}
//			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
//			mstrReportName = visit.getString1();
//		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
//		}
		if(cycle.getRequestContext().getParameter("ds") !=null){
			
			if(!cycle.getRequestContext().getParameter("ds").equals(this.getDataSource())){//��Ҫ��յı���
				
				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				this.setBRiq(null);
				this.setERiq(null);
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
