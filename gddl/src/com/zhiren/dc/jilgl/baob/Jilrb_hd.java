package com.zhiren.dc.jilgl.baob;

import java.sql.ResultSet;
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
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-25
 * �������޸Ĳ�ѯSQL ����
 */
public class Jilrb_hd extends BasePage {

	private static final String ROUNDPOINT_QIY = "�����ձ�С��λ"; // ϵͳ��Ϣ���ж�Ӧ��С���㱣��������������������Ч��

	private static final String BAOBPZB_GUANJZ = "GUOHRB_hd";// baobpzb�ж�Ӧ�Ĺؼ���
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
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
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

	private boolean hasFenC(String id) { // �Ƿ��зֳ�

		JDBCcon con = new JDBCcon();

		String sql = "select mingc from diancxxb where fuid=" + id;

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			rsl.close();
			return true;

		}
		rsl.close();
		return false;

	}



	// ��ȡ������
	public String getRptTitle() {
		String sb;
		sb= "�ձ�";
		return sb;
	}

	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null,
				getTreeid());
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

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		String sql2 = "";
		if (this.hasFenC(getTreeid())) {
			sql2 = " and d.fgsid = " + getTreeid();
		}else{
			sql2 = " and d.id = " + getTreeid();
		}
		if(getRiq() == null || "".equals(getRiq())){
			return "��ѯʧ��������ˢ�±���";
		}
		String sql = 

			"select\n" +
			"decode(grouping(g.mingc) + grouping(m.mingc),2,'�ܼ�',\n" + 
			"1,g.mingc||nvl('(С��)','')||p.mingc,m.mingc) as mk,\n" + 
			"0 as byjh, sum(dr.ches) dcs,sum(dr.jz) djz,sum(dr.bz) dbz,\n" + 
			"sum(dr.yk) dyk,sum(dr.ys) dys,0 as drjh,\n" + 
			"sum(lj.ches) lcs,sum(lj.jz) ljz,sum(lj.bz) lbz,\n" + 
			"sum(lj.yk) lyk,sum(lj.ys) lys,0 as daohlv from\n" + 
			"(select f.meikxxb_id,f.pinzb_id,sum(round(f.ches,0)) ches,\n" + 
			"sum(round(f.jingz,0)) jz,sum(round(f.biaoz,0)) bz,sum(round(f.yingk,0)) yk,sum(round(f.yuns,0)) ys\n" + 
			"from fahb f,vwdianc d where f.daohrq = "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
			" and f.diancxxb_id = d.id  "+sql2+"\n" + 
			"group by f.meikxxb_id,f.pinzb_id) dr,\n" + 
			"(select f.meikxxb_id,f.pinzb_id,sum(round(f.ches,0)) ches,\n" + 
			"sum(round(f.jingz,0)) jz,sum(round(f.biaoz,0)) bz,sum(round(f.yingk,0)) yk,sum(round(f.yuns,0)) ys\n" + 
			"from fahb f,vwdianc d,meikxxb m where f.daohrq >= first_day("+DateUtil.FormatOracleDate(getRiq())+")\n" + 
			"and daohrq <= "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
			" and f.meikxxb_id = m.id and f.diancxxb_id = d.id  "+sql2+"\n" + 
			"group by f.meikxxb_id,f.pinzb_id) lj,meikxxb m, pinzb p, gongysb g\n" + 
			"where lj.meikxxb_id = m.id and lj.pinzb_id = p.id\n" + 
			"and m.meikdq_id = g.id(+)\n" + 
			"and dr.meikxxb_id(+) = lj.meikxxb_id\n" + 
			"and dr.pinzb_id(+) = lj.pinzb_id\n" + 
			"group by rollup(g.mingc,p.mingc,m.mingc)\n" + 
			"having not grouping(g.mingc) + grouping(p.mingc) + grouping(m.mingc) = 2";
		ResultSetList rstmp = con.getResultSetList(sql);
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
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
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;

			ArrHeader = new String[][] { {Locale.meikxxb_id_fahb, "���¼ƻ�",
				"����","����","����","����","����",
				"�ۼ�","�ۼ�","�ۼ�","�ۼ�","�ۼ�","�ۼ�","������"},{
					Locale.meikxxb_id_fahb, "���¼ƻ�",
					Locale.ches_fahb, 
					Locale.jingz_fahb, Locale.biaoz_fahb, Locale.yingk_fahb,
					Locale.yuns_fahb, "���ռƻ�",Locale.ches_fahb, 
					Locale.jingz_fahb, Locale.biaoz_fahb, Locale.yingk_fahb,
					Locale.yuns_fahb,"������" } };

			ArrWidth = new int[] { 200, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60 };

			//rt.setTitle(getRptTitle(), ArrWidth);
		}
		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ��"
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 4, "�������ڣ�" + getRiq(), Table.ALIGN_CENTER);
		rt.setDefaultTitle(12, 3, "��λ���֡���", Table.ALIGN_RIGHT);

		// String[] arrFormat = new String[] { "", "", "", "", "", "", "",
		// "", "", "", "", "" };

		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);

		// rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		// rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		// rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"
				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "�Ʊ�"+getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		// rt.footer.setRowHeight(1, 1);
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt,
				getRptTitle(), "" +BAOBPZB_GUANJZ);
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

	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
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
		// String sql="select d.id,d.mingc from diancxxb d where d.id="+((Visit)
		// getPage().getVisit()).getDiancxxb_id()+" or d.fuid="+((Visit)
		// getPage().getVisit()).getDiancxxb_id();
		String sql = "select d.id,d.mingc from diancxxb d ";
		setGongysDropDownModel(new IDropDownModel(sql, "ȫ��"));
		return;
	}

	//	������ʹ�õķ���
	//������ʹ�õķ���
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
		//	System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	//	-------------------------�糧Tree-----------------------------------------------------------------

}
