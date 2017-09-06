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
 * ʱ�䣺2009-12-08
 * �������ɱ��ձ�����Ʒ�ּ�����˰��ú����
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-21 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Chengbrb_zl extends BasePage implements PageValidateListener  {
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
		String CurrentDate = DateUtil.FormatOracleDate(getRiq());
		String FirstDateOfMonth = DateUtil.FormatOracleDate(DateUtil.getFDOfMonth(getRiq()));
		String sql = "";
		String sql2 = "";
		if (this.hasFenC(getTreeid())) {
			sql2 = " and d.fgsid = " + getTreeid();
		}else{
			sql2 = " and d.id = " + getTreeid();
		}
		sql=
			"select mingc,pz,fenx,biaoz,qnetar,mt,vad,Aad,Stad,meij,yunj,zf,meij+yunj+zf as hej,\n" +
			"round(decode(qnetar,0,0,(meij+yunj+zf)*7000/qnetar),2) hsdj,\n" + 
			"round(decode(qnetar,0,0,(meij+yunj+zf-meijs-yunjs)*7000/qnetar),2) buhsdj\n" + 
			"from\n" + 
			"(select\n" + 
			"decode(grouping(m.mingc),1,'�ܼ�',m.mingc) mingc,\n" + 
			"p.mingc pz,\n" + 
			"nvl(s.fenx,'') as fenx, sum(round(s.biaoz,0)) biaoz,\n" + 
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
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.meij)/\n" + 
			"sum(s.biaoz),2)) meij,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.yunj)/\n" + 
			"sum(s.biaoz),2)) yunj,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.meijs)/\n" + 
			"sum(s.biaoz),2)) meijs,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.yunjs)/\n" + 
			"sum(s.biaoz),2)) yunjs,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.zf)/\n" + 
			"sum(s.biaoz),2)) zf\n" + 
			" from\n" + 
			"(select c.meikxxb_id,c.pinzb_id,'����' fenx,biaoz,qnet_ar,mt,vad,aad,stad,meij,meijs,yunj,yunjs,zf from\n" + 
			"(select f.meikxxb_id,f.pinzb_id, f.jingz as biaoz,z.qnet_ar,z.mt,z.vad,z.aad,z.stad,r.meij,r.meijs,r.yunj,r.yunjs,(jiaohqzf+zaf+daozzf+ qitfy) zf\n" + 
			" from fahb f, ruccb r, zhilb z, vwdianc d\n" + 
			"where f.ruccbb_id = r.id(+) and f.zhilb_id = z.id(+)\n" + 
			"and daohrq = "+CurrentDate+"\n" + 
			"  " + sql2 + "\n" + 
			"and f.diancxxb_id = d.id) f,\n" + 
			"(select distinct meikxxb_id,pinzb_id from fahb, vwdianc d\n" + 
			"where diancxxb_id = d.id and daohrq >= "+FirstDateOfMonth+"\n" + 
			"and daohrq <= "+CurrentDate+"\n" + 
			" " + sql2 + "\n" + 
			") c where f.meikxxb_id(+) = c.meikxxb_id and f.pinzb_id(+) = c.pinzb_id\n" + 
			"union\n" + 
			"select f.meikxxb_id,f.pinzb_id,'�ۼ�' fenx,f.jingz as biaoz,z.qnet_ar,z.mt,z.vad,z.aad,z.stad,r.meij,r.meijs,r.yunj,r.yunjs,(jiaohqzf+zaf+daozzf+ qitfy) zf\n" + 
			"from fahb f, ruccb r, zhilb z, vwdianc d\n" + 
			"where f.ruccbb_id = r.id(+) and f.zhilb_id = z.id(+)\n" + 
			"and f.daohrq >= "+FirstDateOfMonth+"\n" + 
			"and f.daohrq <= "+CurrentDate+"\n" + 
			" " + sql2 + "\n" + 
			"and f.diancxxb_id = d.id ) s, meikxxb m, pinzb p\n" + 
			"where s.meikxxb_id = m.id and s.pinzb_id = p.id\n" + 
			"group by rollup(s.fenx,m.mingc,p.mingc) having not grouping(fenx) = 1\n" + 
			"and not grouping(m.mingc) + grouping(p.mingc) = 1\n" + 
			"order by grouping(m.mingc) desc,max(m.xuh),m.mingc,grouping(p.mingc) desc,max(p.xuh),p.mingc,s.fenx)\n" + 
			"";

			/*
			"select\n" +
			"decode(grouping(m.mingc),1,'�ܼ�',m.mingc) mingc,\n" + 
			"p.mingc pz,\n" + 
			"nvl(s.fenx,'') as fenx, sum(round(s.biaoz,0)) biaoz,\n" + 
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
//			"decode(sum(decode(s.meij,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.meij)/\n" + 
//			"sum(decode(s.meij,0,0,s.biaoz)),2)) meij,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.meij)/\n" + 
			"sum(s.biaoz),2)) meij,\n" + 
//			"decode(sum(decode(s.yunj,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.yunj)/\n" + 
//			"sum(decode(s.yunj,0,0,s.biaoz)),2)) yunj,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.yunj)/\n" + 
			"sum(s.biaoz),2)) yunj,\n" + 
//			"decode(sum(decode(s.zf,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*s.zf)/\n" + 
//			"sum(decode(s.zf,0,0,s.biaoz)),2)) zf,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.zf)/ \n"+
			"sum(s.biaoz),2)) zf,\n" + 
//			"decode(sum(decode(s.meij+s.yunj+s.zf,0,0,s.biaoz)),0,0,round_new(sum(s.biaoz*(s.meij+s.yunj+s.zf))/\n" + 
//			"sum(decode(s.meij+s.yunj+s.zf,0,0,s.biaoz)),2)) hej,\n" +
//			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*(s.meij+s.yunj+s.zf))/\n" + 
//			"sum(s.biaoz),2)) hej,\n" + 
			"decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.meij)/sum(s.biaoz),2))" +
			"+decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.yunj)/sum(s.biaoz),2))" +
			"+decode(sum(s.biaoz),0,0,round_new(sum(s.biaoz*s.zf)/sum(s.biaoz),2)) hej\n"+
//			"decode(sum(decode(s.qnet_ar,0,0,decode(s.meij+s.yunj+s.zf,0,0,s.biaoz))),0,0,round_new(sum(\n" + 
//			"decode(s.qnet_ar,0,0,s.biaoz*(s.meij+s.yunj+s.zf)*29.271/s.qnet_ar))/\n" + 
//			"sum(decode(s.qnet_ar,0,0,decode(s.meij+s.yunj+s.zf,0,0,s.biaoz))),2)) bmdj," +
			"decode(sum(decode(s.qnet_ar,0,0,s.biaoz)),0,0,round_new(sum(\n" + 
			"decode(s.qnet_ar,0,0,s.biaoz*(s.meij+s.yunj+s.zf)*29.271/s.qnet_ar))/\n" + 
			"sum(decode(s.qnet_ar,0,0,s.biaoz)),2)) bmdj," +
//			"decode(sum(decode(s.qnet_ar,0,0,decode(s.meij-s.meijs+s.yunj-s.yunjs+s.zf,0,0,s.biaoz))),0,0,round_new(sum(\n" + 
//			"decode(s.qnet_ar,0,0,s.biaoz*(s.meij-s.meijs+s.yunj-s.yunjs+s.zf)*29.271/s.qnet_ar))/\n" + 
//			"sum(decode(s.qnet_ar,0,0,decode(s.meij-s.meijs+s.yunj-s.yunjs+s.zf,0,0,s.biaoz))),2)) bmdjbhs from\n" + 
			"decode(sum(decode(s.qnet_ar,0,0,s.biaoz)),0,0,round_new(sum(\n" + 
			"decode(s.qnet_ar,0,0,s.biaoz*(s.meij-s.meijs+s.yunj-s.yunjs+s.zf)*29.271/s.qnet_ar))/\n" + 
			"sum(decode(s.qnet_ar,0,0,s.biaoz)),2)) bmdjbhs from\n" + 
			"(select c.meikxxb_id,c.pinzb_id,'����' fenx,biaoz,qnet_ar,mt,vad,aad,stad,meij,meijs,yunj,yunjs,zf from\n" + 
			"(select f.meikxxb_id,f.pinzb_id, f.jingz as biaoz,z.qnet_ar,z.mt,z.vad,z.aad,z.stad,r.meij,r.meijs,r.yunj,r.yunjs,(jiaohqzf+zaf+daozzf+ qitfy) zf\n" + 
			" from fahb f, ruccb r, zhilb z, vwdianc d\n" + 
			"where f.ruccbb_id = r.id(+) and f.zhilb_id = z.id(+)\n" + 
			"and daohrq = "+CurrentDate+"\n" +
			" " + sql2 + "\n" + 
			"and f.diancxxb_id = d.id) f,\n" + 
			"(select distinct meikxxb_id,pinzb_id from fahb, vwdianc d\n" + 
			"where diancxxb_id = d.id and daohrq >= "+FirstDateOfMonth+"\n" + 
			"and daohrq <= "+CurrentDate+"\n" +
			" " + sql2 + "\n" + ") c where f.meikxxb_id(+) = c.meikxxb_id and f.pinzb_id(+) = c.pinzb_id\n" + 
			"union\n" + 
			"select f.meikxxb_id,f.pinzb_id,'�ۼ�' fenx,f.jingz as biaoz,z.qnet_ar,z.mt,z.vad,z.aad,z.stad,r.meij,r.meijs,r.yunj,r.yunjs,(jiaohqzf+zaf+daozzf+ qitfy) zf\n" + 
			"from fahb f, ruccb r, zhilb z, vwdianc d\n" + 
			"where f.ruccbb_id = r.id(+) and f.zhilb_id = z.id(+)\n" + 
			"and f.daohrq >= "+FirstDateOfMonth+"\n" + 
			"and f.daohrq <= "+CurrentDate+"\n" + 
			" " + sql2 + "\n" + 
			"and f.diancxxb_id = d.id ) s, meikxxb m, pinzb p\n" + 
			"where s.meikxxb_id = m.id and s.pinzb_id = p.id\n" + 
			"group by rollup(s.fenx,m.mingc,p.mingc) having not grouping(fenx) = 1\n" + 
			"and not grouping(m.mingc) + grouping(p.mingc) = 1\n" + 
			"order by grouping(m.mingc) desc,max(m.xuh),m.mingc,grouping(p.mingc) desc,max(p.xuh),p.mingc,s.fenx";*/

		return sql;
	}
	
//	ˢ�ºⵥ�б�
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
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null,
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
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	public String getPrintTable(){
		if(getRiq()==null){
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

		String[][] ArrHeader = new String[2][12];
		ArrHeader[0]=new String[]{"ú��","Ʒ��","��Ŀ","����<br>���֣�","������<br>��Kcal��"
								 ,"������%��","������%��","������%��","������%��"
								 ,"�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�","�۸�Ԫ/�֣�"};
		ArrHeader[1]=new String[]{"ú��","Ʒ��","��Ŀ","����<br>���֣�","������<br>��Kcal��"
								 ,"ˮ��<br>��Mt��","�ӷ���<br>��Vad��","�ҷ�<br>��Aad��","��<br>��Stad��"
								 ,"ú��","�˷�","�ӷ�","�ϼ�","��ú����","��ú����<br>����˰"};

		int[] ArrWidth = new int[] { 75, 42, 40, 50, 50, 45, 45, 45, 45, 50, 50, 50, 65, 55, 55};
		
		String[] strFormat = new String[] {
								"", "", "", "0" ,"0", 
								"0.0", "0.00", "0.00", "0.00", 
								"0.00","0.00","0.00","0.00","0.00","0.00"};
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		

		rt.setTitle("�ɱ��ձ�", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
//		rt.setDefaultTitle(6, 6, "�������ڣ�" + getRiq(),
//				Table.ALIGN_CENTER);
//		rt.setDefaultTitle(15, 3, "��λ���֡���", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 2, 0, 3));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(rt.PAPER_ROWS);
		
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "��ˣ�", Table.ALIGN_CENTER);
		rt.setDefautlFooter(11, 2, "�Ʊ�", Table.ALIGN_LEFT);
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
//	String pagewith=null;
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setGongysDropDownModel(null);
			getSelectData();
			setTreeid(visit.getDiancxxb_id()+"");
			visit.setString1(null);
			
			
			String pagewith = cycle.getRequestContext().getParameter("pw");//�ж��Ƿ�����������
			if(reportType != null) {
				setRiq(DateUtil.FormatDate(new Date()));
				visit.setInt1(Integer.parseInt(reportType));
			}
			
			if(pagewith!=null){
				
				visit.setString1(pagewith);
			}
//			visit.setString1(pagewith);	 ҳ�����ֽ�Ŵ�С
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
	
//	 ��ȡ�糧
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
}
