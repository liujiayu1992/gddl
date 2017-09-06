package com.zhiren.dc.chengbgl.baob;

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
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-19
 * ����������SQL����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-03
 * �������糧ID��ʼ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-08
 * �������ɱ�̨������Ʒ�ּ�����˰��ú����
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-21 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Chengbyb_zl extends BasePage implements PageValidateListener  {
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
		String sql = "";
		String sql2 = "";
		if (this.hasFenC(getTreeid_dc())) {
			sql2 = " and d.fgsid = " + getTreeid_dc();
		}else{
			sql2 = " and d.id = " + getTreeid_dc();
		}
		sql=	
			"select decode(grouping(m.mingc),1,'�ܼ�',m.mingc) mingc,\n" +
			"p.mingc pinz,\n" + 
			"sum(round(s.biaoz,0)) biaoz,\n" + 
			"decode(sum(decode(s.qnet_ar,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.qnet_ar)/\n" + 
			"(0.0041816*sum(decode(s.qnet_ar,0,0,s.biaoz))),0)) qnetar,\n" + 
			"decode(sum(decode(s.mt,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.mt)/\n" + 
			"sum(decode(s.mt,0,0,s.biaoz)),1)) mt,\n" + 
			"decode(sum(decode(s.vad,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.vad)/\n" + 
			"sum(decode(s.vad,0,0,s.biaoz)),2)) vad,\n" + 
			"decode(sum(decode(s.Aad,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.Aad)/\n" + 
			"sum(decode(s.Aad,0,0,s.biaoz)),2)) Aad,\n" + 
			"decode(sum(decode(s.Stad,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.Stad)/\n" + 
			"sum(decode(s.Stad,0,0,s.biaoz)),2)) Stad,\n" + 
			"decode(sum(decode(s.meij,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.meij)/\n" + 
			"sum(decode(s.meij,0,0,s.biaoz)),2)) meij,\n" + 
			"decode(sum(decode(s.yunj,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.yunj)/\n" + 
			"sum(decode(s.yunj,0,0,s.biaoz)),2)) yunj,\n" + 
			"decode(sum(decode(s.zf,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.zf)/\n" + 
			"sum(decode(s.zf,0,0,s.biaoz)),2)) zf,\n" + 
			"decode(sum(decode(s.meij+s.yunj+s.zf,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*(s.meij+s.yunj+s.zf))/\n" + 
			"sum(decode(s.meij+s.yunj+s.zf,0,0,s.biaoz)),2)) hej,\n" + 
			"decode(sum(decode(s.qnet_ar,0,0,decode(s.meij+s.yunj+s.zf,0,0,s.biaoz))),0,0,round_new(sum(\n" + 
			"decode(s.qnet_ar,0,0,s.biaoz*(s.meij+s.yunj+s.zf)*29.271/s.qnet_ar))/\n" + 
			"sum(decode(s.qnet_ar,0,0,decode(s.meij+s.yunj+s.zf,0,0,s.biaoz))),2)) bmdj," +
			"decode(sum(decode(s.qnet_ar,0,0,decode(s.meij-s.meijs+s.yunj-s.yunjs+s.zf,0,0,s.biaoz))),0,0,round_new(sum(\n" + 
			"decode(s.qnet_ar,0,0,s.biaoz*(s.meij-s.meijs+s.yunj-s.yunjs+s.zf)*29.271/s.qnet_ar))/\n" + 
			"sum(decode(s.qnet_ar,0,0,decode(s.meij-s.meijs+s.yunj-s.yunjs+s.zf,0,0,s.biaoz))),2)) bmdjbhs from\n" + 
			"(select f.meikxxb_id,f.pinzb_id,f.jingz as biaoz,z.qnet_ar,z.mt,z.vad,z.aad,z.stad,r.meij,r.meijs,r.yunj,r.yunjs,(r.jiaohqzf + r.zaf + r.daozzf + r.qitfy) zf\n" + 
			"from fahb f, ruccb r, zhilb z, vwdianc d\n" + 
			"where f.ruccbb_id = r.id(+) and f.zhilb_id = z.id(+)\n" + 
			"and f.daohrq >= "+DateUtil.FormatOracleDate(getRiq())+"\n" + 
			"and f.daohrq <= "+DateUtil.FormatOracleDate(getAfter())+"\n" + 
			sql2 + " and f.diancxxb_id = d.id ) s, meikxxb m, pinzb p\n" + 
			"where s.meikxxb_id = m.id and s.pinzb_id =p.id\n" + 
			"group by rollup(m.mingc,p.mingc) having not grouping(m.mingc) + grouping(p.mingc) = 1\n" + 
			"order by grouping(m.mingc) desc,max(m.xuh),m.mingc, grouping(p.mingc)desc, max(p.xuh), p.mingc";

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
		tb1.addText(new ToolbarText("-"));
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
		if(getRiq()==null || getAfter()==null){
			return "";
		}
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
		
		ArrHeader = new String[2][14];
		ArrHeader[0]=new String[]{"ú��", "Ʒ��","����<br>���֣�","������<br>��Kcal��"
				,"������%��","������%��","������%��","������%��"
				,"�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�"};
		
		ArrHeader[1]=new String[]{"ú��","Ʒ��","����<br>���֣�","������<br>��Kcal��"
				
				,"ˮ��<br>��Mt��","�ӷ���<br>��Vad��","�ҷ�<br>��Aad��","��<br>��Stad��"
				,"ú��","�˷�","�ӷ�","�ϼ�","��ú����","��ú����<br>����˰"};
		ArrWidth = new int[] { 80, 40, 60, 55, 40, 45, 50, 45, 60, 60, 40, 65, 55, 60};
		strFormat = new String[] {
				"" , "", "0"  ,"0"
				,"0.0","0.00","0.00","0.00"
				,"0.00" ,"0.00" ,"0.00"   ,"0.00"   ,"0.00","0.00"   };
			
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("�ɱ��±�", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 6, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 5, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "�Ʊ�", Table.ALIGN_LEFT);
//		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
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
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getDiancmcModels();
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
				
		}
		getSelectData();
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
		}
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
