package com.zhiren.shanxdted.fakxxcx;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Fakxxcx extends BasePage implements PageValidateListener {

	private static final String BAOBPZB_GUANJZ = "FAKXXCX";// baobpzb�ж�Ӧ�Ĺؼ���
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

	// ��ȡ��ص�SQL

	public StringBuffer getBaseSqlStr() {
		StringBuffer sb = new StringBuffer();
		
		String meiksql = "";
		String yunsdwsql = "";
		if(!"0".equals(getMeikuangid())){
			meiksql = "	and j.meikxxb_id="+getMeikuangid()+"\n";
		}
		if(!"0".equals(getMeikid())){
			yunsdwsql = "	and j.yunsdwb_id="+getMeikid()+"\n";
		}
		sb.append(
				"select d.mingc,m.mingc,y.mingc,cheh,chex,to_char(faksj,'yyyy-mm-dd hh24:mi:ss'),fakr from jianjghb j,diancxxb d,meikxxb m,yunsdwb y\n" +
				" where \n" + 
				"   j.maoz is null\n" + 
				"   and j.zhuangt in(1)\n" +
				"   and j.diancxxb_id=d.id\n" + 
				"   and (d.id="+getTreeid()+" or d.fuid="+getTreeid()+")\n" +
				"   and (j.faksj between to_date('"+getRiq()+"','yyyy-mm-dd') and to_date('"+getRiq()+"','yyyy-mm-dd')+1)" +
				"   and j.meikxxb_id=m.id\n" +
				"   and j.yunsdwb_id=y.id\n"
				+ meiksql + yunsdwsql + 
				" order by d.mingc,m.mingc,y.mingc,faksj" 
				
				);

		return sb;
	}

	// ��ȡ������
	public String getRptTitle() {
		return "���������š�Ϣ���顡ѯ";
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		// ú��

		DefaultTree gystree = new DefaultTree();
		gystree.setTree_window(this.getMKSql().toString(), "meikuangTree",
				visit.getDiancxxb_id() + "", "forms[0]", this.getMeikuangid(),
				this.getMeikuangid());
		this.setMeikuangtr(gystree);
		TextField tf1 = new TextField();
		tf1.setId("meikuangTree_text");
		tf1.setWidth(100);
		tf1
				.setValue(((IDropDownModel) getMeiKuangDropDownModel())
						.getBeanValue(Long.parseLong(getMeikuangid() == null
								|| "".equals(getMeikuangid()) ? "-1"
								: getMeikuangid())));

		ToolbarButton tb5 = new ToolbarButton(null, null,
				"function(){meikuangTree_window.show();}");
		tb5.setIcon("ext/resources/images/list-items.gif");
		tb5.setCls("x-btn-icon");
		tb5.setMinWidth(20);

		tb1.addText(new ToolbarText("ú��:"));
		tb1.addField(tf1);
		tb1.addItem(tb5);

		tb1.addText(new ToolbarText("-"));

		// ���䵥λ--------------------

		DefaultTree mktree = new DefaultTree();
		mktree.setTree_window(this.getDTsql().toString(), "meikTree", visit
				.getDiancxxb_id()
				+ "", "forms[0]", this.getMeikid(), this.getMeikid());
		this.setTree(mktree);

		TextField tfmk = new TextField();
		tfmk.setId("meikTree_text");
		tfmk.setWidth(100);
		tfmk.setValue(((IDropDownModel) this.getMeikModel()).getBeanValue(Long
				.parseLong(this.getMeikid() == null
						|| "".equals(this.getMeikid()) ? "-1" : this
						.getMeikid())));

		ToolbarButton tb4 = new ToolbarButton(null, null,
				"function(){meikTree_window.show();}");
		tb4.setIcon("ext/resources/images/list-items.gif");
		tb4.setCls("x-btn-icon");
		tb4.setMinWidth(20);

		tb1.addText(new ToolbarText("���䵥λ"));
		tb1.addField(tfmk);
		tb1.addItem(tb4);

		tb1.addText(new ToolbarText("-"));
		// -------------------------

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "forms[0]",
				getTreeid(), getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(getTreeid() == null
						|| "".equals(getTreeid()) ? "-1" : getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

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
	
	// ���ú��λ ���νṹsql
	private StringBuffer getMKSql() {
		StringBuffer bf = new StringBuffer();
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'ȫ��' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from meikxxb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		return bf;
	}

	// ������䵥λ ���νṹsql
	private StringBuffer getDTsql() {
		StringBuffer bf = new StringBuffer();
		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'ȫ��' mingc,0 jib from dual\n");
		bf.append(" union \n select id ,mingc,1 jib from yunsdwb ");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");
		return bf;
	}

	// ------------ú��----------

	private String meikid = "";

	public String getMeikid() {
		if (meikid.equals("")) {

			meikid = "0";
		}
		return meikid;
	}

	public void setMeikid(String meikid) {
		if (meikid != null) {
			if (!meikid.equals(this.meikid)) {
				((TextField) getToolbar().getItem("meikTree_text"))
						.setValue(((IDropDownModel) this.getMeikModel())
								.getBeanValue(Long.parseLong(meikid)));
				this.getTree().getTree().setSelectedNodeid(meikid);
			}
		}
		this.meikid = meikid;
	}
	

	DefaultTree mktr;

	public DefaultTree getTree() {
		return mktr;
	}

	public void setTree(DefaultTree etu) {
		mktr = etu;
	}

	public String getTreeScriptMK() {
		return this.getTree().getScript();
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getMeikModels() {
		String sql = "select 0 id,'ȫ��' mingc from dual union select id,mingc  from yunsdwb ";
		setMeikModel(new IDropDownModel(sql));
	}

	// -------------------------------------------------

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rstmp = con.getResultSetList(getBaseSqlStr().toString());
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		int[] ArrWidth = null;

		rs = rstmp;

		ArrHeader = new String[][] { { "�糧����","ú��λ","���䵥λ","����","����","����ʱ��","������" } };

		ArrWidth = new int[] { 100, 200, 200, 70, 70, 120, 80 };

		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		rt.setDefaultTitle(1, 3, "�Ʊ�λ��"
//				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
//				Table.ALIGN_LEFT);
//		rt.setDefaultTitle(4, 4, "�������ڣ�" + getRiq(), Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 4, "��λ���֡���", Table.ALIGN_RIGHT);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		// rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"
				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(8, 2, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;

		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
//		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();// ph;
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

	private String meikuangid = "";

	public String getMeikuangid() {
		if (meikuangid.equals("")) {

			meikuangid = "0";
		}
		return meikuangid;
	}

	public void setMeikuangid(String meikuangid) {
		if (meikuangid != null) {
			if (!meikuangid.equals(this.meikuangid)) {
				((TextField) getToolbar().getItem("meikuangTree_text"))
						.setValue(((IDropDownModel) this
								.getMeiKuangDropDownModel()).getBeanValue(Long
								.parseLong(meikuangid)));
				this.getMeikuangtr().getTree().setSelectedNodeid(meikuangid);
			}
		}
		this.meikuangid = meikuangid;
	}

	public IPropertySelectionModel getMeiKuangDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getMeiKuangDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setMeiKuangDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getMeiKuangDropDownModels() {
		String sql = "";

		sql += "  select 0 id, 'ȫ��' mingc from dual union select id ,mingc from meikxxb";
		// setMeiKuangDropDownModel(new IDropDownModel(sql,"ȫ��")) ;
		setMeiKuangDropDownModel(new IDropDownModel(sql));
		return;
	}

	DefaultTree meikuangtr;

	public DefaultTree getMeikuangtr() {
		return meikuangtr;
	}

	public void setMeikuangtr(DefaultTree etu) {
		meikuangtr = etu;
	}

	public String getMeikuangtrScriptMK() {
		return this.getMeikuangtr().getScript();
	}
	
	// ��ȡ��Ӧ��
	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getGongysDropDownModels() {
		String sql = "select d.id,d.mingc from diancxxb d ";
		setGongysDropDownModel(new IDropDownModel(sql, "ȫ��"));
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
						.setValue(((IDropDownModel) getGongysDropDownModel())
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
	
	
	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));

			this.meikid = "";
			this.meikuangid = "";
			this.getMeikModels();
			this.getMeiKuangDropDownModels();
			this.getGongysDropDownModels();

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
