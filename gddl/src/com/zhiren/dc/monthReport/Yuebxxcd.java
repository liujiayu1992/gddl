package com.zhiren.dc.monthReport;
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


public class Yuebxxcd extends BasePage  implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	������
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	private String after;
	public String getAfter() {
		return after;
	}
	public void setAfter(String after) {
		this.after = after;
	}
	
//	ҳ��仯��¼
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

	public String getBaseSql() {
		String sql2 = "";
		if (this.hasFenC(getTreeid_dc())) {
			sql2 = " and d.fgsid = " + getTreeid_dc();
		}else{
			sql2 = " and d.id = " + getTreeid_dc();
		}
		String sql = 

			"select gys,mk,pz,jingz,biaoz,yuns,yingk,qnetar,mt,vad,aad,stad,\n" +
			"mj,yj,zj,round(decode(qnetar,0,0,zj*7000/qnetar),2) bmdj from\n" + 
			"(select decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n" + 
			"decode(grouping(g.mingc) + grouping(m.mingc),2,'�ܼ�',1,g.mingc||'�ϼ�',m.mingc) mk,\n" + 
			"decode(grouping(g.mingc) + grouping(m.mingc) + grouping(p.mingc),3,'�ܼ�',\n" + 
			"2,g.mingc||'�ϼ�',1,'С��',p.mingc) pz,\n" + 
			"sum(round(f.jingz,0)) jingz, sum(round(f.biaoz,0)) biaoz,\n" + 
			"sum(round(f.yuns,0)) yuns, sum(round(f.yingk,0)) yingk,\n" + 
			"decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.jingz)),0,0,round_new(sum(z.qnet_ar*f.jingz)/\n" + 
			"sum(decode(nvl(z.qnet_ar,0),0,0,f.jingz))/0.0041816,2)) qnetar,\n" + 
			"decode(sum(decode(nvl(z.mt,0),0,0,f.jingz)),0,0,round_new(sum(z.mt*f.jingz)/\n" + 
			"sum(decode(nvl(z.mt,0),0,0,f.jingz)),1)) mt,\n" + 
			"decode(sum(decode(nvl(z.vad,0),0,0,f.jingz)),0,0,round_new(sum(z.vad*f.jingz)/\n" + 
			"sum(decode(nvl(z.vad,0),0,0,f.jingz)),2)) vad,\n" + 
			"decode(sum(decode(nvl(z.aad,0),0,0,f.jingz)),0,0,round_new(sum(z.aad*f.jingz)/\n" + 
			"sum(decode(nvl(z.aad,0),0,0,f.jingz)),2)) aad,\n" + 
			"decode(sum(decode(nvl(z.stad,0),0,0,f.jingz)),0,0,round_new(sum(z.stad*f.jingz)/\n" + 
			"sum(decode(nvl(z.stad,0),0,0,f.jingz)),2)) stad,\n" + 
			"decode(sum(decode(nvl(r.meij-r.meijs,0),0,0,f.jingz)),0,0,round(sum((r.meij - r.meijs)\n" + 
			"* f.jingz)/sum(decode(nvl(r.meij-r.meijs,0),0,0,f.jingz)),2)) mj,\n" + 
			"decode(sum(decode(nvl(r.yunj - r.yunjs,0),0,0,f.jingz)),0,0,round(sum((r.yunj - r.yunjs)\n" + 
			"* f.jingz)/sum(decode(nvl(r.yunj - r.yunjs,2),0,0,f.jingz)),2)) yj,\n" + 
			"decode(sum(decode(nvl(r.meij - r.meijs + r.yunj - r.yunjs,0),0,0,f.jingz)),0,0,round(sum((r.meij - r.meijs + r.yunj - r.yunjs)\n" + 
			"* f.jingz)/sum(decode(nvl(r.meij - r.meijs + r.yunj - r.yunjs,0),0,0,f.jingz)),2)) zj\n" + 
			"/*,\n" + 
			"decode(sum(decode(nvl(r.meij-r.meijs+r.yunj-r.yunjs,0),0,0,decode(nvl(z.qnet_ar,0),0,0,f.jingz))),0,0,\n" + 
			"round(sum(decode(nvl(z.qnet_ar,0),0,0,nvl(r.meij - r.meijs + r.yunj - r.yunjs,0)\n" + 
			"* f.jingz*29.271/z.qnet_ar))/\n" + 
			"sum(decode(nvl(r.meij-r.meijs+r.yunj-r.yunjs,0),0,0,decode(nvl(z.qnet_ar,0),0,0,f.jingz))),2)) bmdj*/\n" + 
			"from fahb f, zhilb z, ruccb r, gongysb g, meikxxb m, pinzb p, vwdianc d\n" + 
			"where f.zhilb_id = z.id(+) and f.ruccbb_id = r.id(+)\n" + 
			"and f.meikxxb_id = m.id and f.pinzb_id = p.id\n" + 
			"and m.meikdq_id = g.id and f.diancxxb_id = d.id\n" + 
			"and f.daohrq >= "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
			"and f.daohrq <= "+DateUtil.FormatOracleDate(getAfter())+"\n" + 
			sql2 + "\n" + 
			"group by rollup(g.mingc,m.mingc,p.mingc)\n" + 
			"order by grouping(g.mingc), max(g.xuh),g.mingc,max(m.xuh),m.mingc,max(p.xuh),p.mingc\n" + 
			")s";
		return sql;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "");
		df.setId("Riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("��"));
		DateField df1 = new DateField();
		df1.setValue(getAfter());
		df1.Binding("After", "");
		df1.setId("After");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));
		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		
		ResultSetList rs = con.getResultSetList(getBaseSql());
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		String[] strFormat;
	
		ArrHeader = new String[2][16];
		ArrHeader[0]=new String[]{"����","ú��","Ʒ��"
								 ,"�������֣�","�������֣�","�������֣�","�������֣�"
								 ,"��������Kcal��"
								 ,"������%��","������%��","������%��","������%��"
								 ,"�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�"};
		ArrHeader[1]=new String[]{"����","ú��","Ʒ��"
								 ,"����","Ʊ��","����","ӯ��"
								 ,"������<br>��Kcal��"
								 ,"ˮ��<br>��Mt��","�ӷ���<br>��Vad��","�ҷ�<br>��Aad��","��<br>��Stad��"
								 ,"ú��","�˷�","�ϼ�","��ú����"};

		ArrWidth = new int[] { 70, 70, 60, 40, 40, 40, 40
									, 40, 40, 40, 40, 40
									, 50, 50, 50, 55};
		
		strFormat = new String[16];
		strFormat = new String[] {
								"", "", "",
								"0", "0", "0" ,"0", 
								"0", "0.00", "0.00", "0.00", "0.00",							
								"0","0","0","0",};
		
		rt.setTitle("�±���Ϣ�鶨��", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.mergeCell(1, 8, 2, 8);
		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "��ˣ�", Table.ALIGN_CENTER);
		rt.setDefautlFooter(10, 3, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;
	}
//	������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setInt1(Integer.parseInt(reportType));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setAfter(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
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
	
//	-------------------------�糧Tree-----------------------------------------------------------------
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
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
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
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
}
