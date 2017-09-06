package com.zhiren.dc.huaygl.zhiltz;


import java.sql.ResultSet;
import java.util.Date;

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

public class Zhiltz_jz extends BasePage {
	
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
				.append(
						"\n" +
						"select decode(grouping(a.dagys),1,'�ܼ�',a.dagys) as dagys,\n" + 
						"       decode(grouping(a.dagys)+grouping(a.xiaogys),1,'С��',a.xiaogys) as xiaogys ,\n" + 
						"        sum(round_new(a.laimsl,0))  as laimsl,sum(a.ches) as ches,\n" + 
						"  decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(round_new(a.mt,1) * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 1)) as mt,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.mad * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as mad,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.aad * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as aad,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.ad * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as ad,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.aar * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as aar,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.vad * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as vad,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.vdaf * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as vdaf,\n" + 
						"                decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(round_new(a.qbad,2) * round_new(a.laimsl,0))/ sum(round_new(a.laimsl,0)), 2)*1000) as qbad,\n" + 
						"                decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(round_new(a.qnet_ar,2) * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)*1000) as farl1,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(round_new(a.qnet_ar,2) * round_new(a.laimsl,0)) /\n" + 
						"                                          sum(round_new(a.laimsl,0))\n" + 
						"                                           * 1000 / 4.1816,\n" + 
						"                                0)) as qbar,\n" + 
						"\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.sdaf * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as sdaf,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.stad * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as stad,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.std * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as std,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(round_new(a.stad*(100-a.mt)/(100-a.mad),2) * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as star,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.hdaf * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as hdaf,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.had * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as had,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.fcad * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as fcad,\n" + 
						"               decode(sum(round_new(a.laimsl,0)),\n" + 
						"                      0,\n" + 
						"                      0,\n" + 
						"                      round_new(sum(a.qgrd * round_new(a.laimsl,0)) / sum(round_new(a.laimsl,0)), 2)) as qgrd\n" + 
						"from (\n" + 
						"  select decode(gg.mingc,null,g.mingc,gg.mingc) as dagys ,g.mingc as xiaogys,\n" + 
						"         f.ches,f.laimsl,z.MT,z.MAD,z.AAD,z.AD,z.AAR,z.VAD,z.VDAF,\n" + 
						"         z.QBAD,z.qnet_ar,z.SDAF,z.STAD,z.STD,z.HDAF,z.HAD,z.FCAD,z.qgrd\n" + 
						"   from meikxxb g, gongysb gg,fahb f ,zhilb z\n" + 
						"  where g.meikdq_id=gg.id(+) and not exists(select * from gongysb where fuid=g.id)\n" + 
						"  and f.meikxxb_id=g.id\n" + 
						"  and f.zhilb_id=z.id\n" + 
						"  and f.daohrq >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
						"  and f.daohrq <= to_date('"+getRiq2()+"', 'yyyy-mm-dd')\n" + 
						") a\n" + 
						"group by rollup ( a.dagys,a.xiaogys)\n" + 
						"order by a.dagys desc,a.xiaogys desc");


//		System.out.println(buffer.toString());
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][22];

		ArrHeader[0] = new String[] { "������λ", "ú��λ", 
				"������<br>��(��)", "����", "ȫˮ<br>��<br>(%)Mt",
				"����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad", "����<br>��<br>�ҷ�<br>(%)Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", "������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", "��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>(%)<br>Sdaf",
				"����<br>����<br>����<br>(%)<br>St,ad", "����<br>��ȫ<br>��(%)<br>St,d",
				"�յ�<br>��ȫ<br>��(%)<br>St,ar", "����<br>�޻�<br>����<br>(%)<br>Hdaf",
				"����<br>����<br>����<br>(%)<br>Had",

				"�̶�<br>̼<br>(%)<br>Fcad", "�ɻ�<br>��λ<br>��<br>(J/g)<br>Qgrd" };
		int[] ArrWidth = new int[22];

		ArrWidth = new int[] { 85, 100,  40, 50, 40, 40, 40, 40, 40,
				40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 };

		rt.setTitle("ú  ��  ��  ��  ̨  ��", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "жú����:" + getRiqi() + "��" + getRiq2(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		String[] strFormat = new String[] { "", "",  "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "" };

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
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
/*
		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("YUNSFSSelect");
		meik.setEditable(true);
		meik.setWidth(100);
		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);*/

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
