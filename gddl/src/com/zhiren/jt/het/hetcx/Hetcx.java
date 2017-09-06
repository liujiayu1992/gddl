package com.zhiren.jt.het.hetcx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ߣ�����
 * ʱ�䣺2010-01-16
 * ����������Ƽ��Ź涨��ͬ������������ɺ�ͬ������ʾ���е�������޸Ĳ�ѯʱ��ͬ�����п�
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-20 16��05
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/*
 * ���ߣ�songye
 * ʱ�䣺2011-6-13
 * ���������ӿ�ʼʱ��ͽ���ʱ����
 * 
 */
/*
 * ���ߣ�songye
 * ʱ�䣺2011-6-14
 * �������۸�ֿ���ú�ۺ��˷���
 * 
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-05-17
 * ����������������λŲ����ǰ�ˣ�
 *      �ж������ �跽��λ�����ڡ���ͬ���͡�����״̬�����������У����������о��ҡ�
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-07-24
 * ��������������״̬һ�У����ӽ��㷽ʽ�С�
 * 		���ݿ������ĵ���2012-07-24(���).txt��
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-12-20
 * �������������������е����ݱ��Ϊֻ��ʾ����˺����������֣����ӵ�λС�ƺ��ܼ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-3-20
 * ��������ѯ��������ͬ�������ع��ܣ������ͬ�и������Զ�����������ݣ�������ʾϵͳ��ͬ���ݡ�
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-6-07
 * ������������ͬ��ѯʱ������ʽ������ͬ��Ž�������
 */
/*
 * ���ߣ���ҫ��
 * ʱ�䣺2014-2-20
 * ��������ǩ�����ڲ�ѯ��Ϊ���������ڲ�ѯ��
 */
public class Hetcx extends BasePage {
	public boolean getRaw() {
		return true;
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

	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetcx();
	}

	private String getHetcx() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		String hetbhCondi = "";
		String hetlCondi = "";
		String weizCondi = "";
		String leixCondi = "";
		String gongfCondi = "";
		String xufCondi = "";
		String jihkjCondi="";
		String diancCondi="";
		if (gethetbh() != null && !gethetbh().equals("")) {
			hetbhCondi = " and  a.hetbh like '%" + gethetbh() + "%' ";
		}
		if (gethetl1() != null && gethetl2() != null && !gethetl1().equals("")
				&& !gethetl2().equals("")) {
			hetlCondi = " and a.hetl>=" + gethetl1() + " and hetl<="
					+ gethetl2();
		}
		if (getweizSelectValue().getId() != -1) {
			weizCondi = " and a.leibztb_id=" + getweizSelectValue().getId();
		}
		 if(getJihxzValue().getId()!= -1){
			 jihkjCondi=" and a.jihkj='" + getJihxzValue().getValue()+"' ";
		 }
		if (Long.parseLong(getTreeid())!=-1&&MainGlobal.getXitxx_item("��ͬ", "��ʾ�跽", "0", "��").equals("��")) {
			xufCondi = " and a.diancxxb_id " + "in (select id\n" + "from(\n"
					+ "select id from diancxxb\n" + "start with (fuid="
					+ Long.parseLong(getTreeid()) + " or shangjgsid="
					+ Long.parseLong(getTreeid()) + ") \n"
					+ "connect by fuid=prior id\n" + ")\n" + "union\n"
					+ "select id\n" + "from diancxxb\n" + "where id="
					+ Long.parseLong(getTreeid()) + ")";
		}

		if (getLeixSelectValue().getId() != -1) {
			leixCondi = " and a.leibid=" + getLeixSelectValue().getId();
		}
		if (getGongysDropDownValue().getId() != -1) {
			gongfCondi = " and a.gongysb_id " + "in( select id\n"
					+ " from gongysb\n" + " where gongysb.id="
					+ getGongysDropDownValue().getId() + " or gongysb.fuid="
					+ getGongysDropDownValue().getId() + ")";
		}
		if (getDiancDropDownValue().getId() != -1) {
			diancCondi=" and a.shouhr="+getDiancDropDownValue().getId();
		}
	
//		����ļ�����·��
		String Imagelj=MainGlobal.getXitxx_item("��ͬ", "��ͬ����·��", "0", "D:\\\\zhiren\\\\het");
		Imagelj=Imagelj + "\\\\" + this.getTreeid() + "\\\\";
		Imagelj=Imagelj.replaceAll("\\\\", "\\\\\\\\");
		String chak="";
		if(MainGlobal.getXitxx_item("��ͬ", "�Ƿ�鿴���Ӱ��ͬ����", "0", "��").equals("��")){
			chak="decode(GETHETFILENAME(id),null,SR.HETBH,'<a href=# onclick=window.open(''"+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETHETFILENAME(id)||''')>'||HETBH||'</a>') as HETBH, \n";
		}else{
			chak="SR.HETBH HETBH,\n";
		}
		
		
		buffer.append("SELECT ID,\n" +
						chak+
						"		DIANCXXB_ID,\n" +
						"       DECODE(GROUPING(ID) + GROUPING(DIANCXXB_ID),1,'С��',2,'�ϼ�',XUFDWMC) XUFDWMC,\n" + 
						"       GONGFDWMC,\n" + 
						"       SUM(HETL) HETL,\n" + 
						"       QIANDRQ,\n" + 
						"       QISRQ,\n" + 
						"       GUOQRQ,\n" + 
						"       JIHKJ,\n" + 
						"       ROUND(DECODE(SUM(HETL), 0, 0, SUM(REL * HETL) / SUM(HETL)), 0) REL,\n" + 
						"       ROUND(DECODE(SUM(HETL), 0, 0, SUM(MEIJ * HETL) / SUM(HETL)), 0) MEIJ,\n" + 
						"       ROUND(DECODE(SUM(HETL), 0, 0, SUM(YUNJ * HETL) / SUM(HETL)), 0) YUNJ,\n" + 
						"       JIESFS,\n" + 
						"       FID,\n" + 
						"       FBH\n" + 
						"  FROM (SELECT ID,\n" + 
						"               DIANCXXB_ID,\n" + 
						"               GONGYSB_ID,\n" + 
						"               HETGYSBID,\n" + 
						"               XUFDWMC,\n" + 
						"               GONGFDWMC,\n" + 
						"               HETL,\n" + 
						"               QIANDRQ,\n" + 
						"               QISRQ,\n" + 
						"               GUOQRQ,\n" + 
						"               JIHKJ,\n" + 
						"               REL,\n" + 
						"               MEIJ,\n" + 
						"               YUNJ,\n" + 
						"               JIESFS,\n" + 
						"               HETBH,\n" + 
						"               DECODE(LEIBZTB_ID, 0, '����', 1, '����', 2, 'Զ�̴���', WEIZ) WEIZ,\n" + 
						"               LEIB,\n" + 
						"               LIUCZTBID,\n" + 
						"               LEIBID,\n" + 
						"               LEIBZTB_ID,\n" + 
						"               FID,\n" + 
						"               FBH\n" + 
						"          FROM (SELECT GETHETCXREL(HETB.ID) REL,\n" + 
						"                       GETHETCXJIAG(HETB.ID) MEIJ,\n" + 
						"                       GETHETCXYUNJ(HETB.ID) YUNJ,\n" + 
						"                       GETHETJSFS(HETB.ID) JIESFS,\n" + 
						"                       HETB.HETBH,\n" + 
						"                       D.MINGC XUFDWMC,\n" + 
						"                       HETB.GONGFDWMC,\n" + 
						"                       SUM(HETSLB.HETL) HETL,\n" + 
						"                       TO_CHAR(HETB.QIANDRQ, 'YYYY-MM-DD') QIANDRQ,\n" + 
						"                       TO_CHAR(HETB.QISRQ, 'YYYY-MM-DD') QISRQ,\n" + 
						"                       TO_CHAR(HETB.GUOQRQ, 'YYYY-MM-DD') GUOQRQ,\n" + 
						"                       JIHKJB.MINGC JIHKJ,\n" + 
						"                       LEIBZTB.MINGC WEIZ,\n" + 
						"                       DECODE(HETB.LEIB,0,'�糧�ɹ�',1,'��������',2,'����ɹ�') LEIB,\n" + 
						"                       HETB.ID,\n" + 
						"                       HETB.DIANCXXB_ID,\n" + 
						"                       HETSLB.DIANCXXB_ID AS SHOUHR,\n" + 
						"                       HETB.GONGYSB_ID,\n" + 
						"                       HETB.HETGYSBID,\n" + 
						"                       HETB.LIUCZTB_ID LIUCZTBID,\n" + 
						"                       HETB.LEIB LEIBID,\n" + 
						"                       DECODE(LEIBZTB.ID, NULL, HETB.LIUCZTB_ID, LEIBZTB.ID) LEIBZTB_ID,\n" + 
						"                       B.ID FID,\n" + 
						"                       B.HETBH FBH\n" + 
						"                  FROM HETB,\n" + 
						"                       HETSLB,\n" + 
						"                       JIHKJB,\n" + 
						"                       LIUCZTB,\n" + 
						"                       LEIBZTB,\n" + 
						"                       HETB     B,\n" + 
						"                       DIANCXXB D\n" + 
						"                 WHERE HETB.DIANCXXB_ID = D.ID\n" + 
						"                   AND HETB.FUID = B.ID(+)\n" + 
						"                   AND HETB.ID = HETSLB.HETB_ID(+)\n" + 
						"                   AND HETB.JIHKJB_ID = JIHKJB.ID\n" + 
						"                   AND LIUCZTB.LEIBZTB_ID = LEIBZTB.ID(+)\n" + 
						"                   AND HETB.LIUCZTB_ID = LIUCZTB.ID(+)\n" + 
						"                  GROUP BY HETB.HETBH,D.MINGC,HETB.GONGFDWMC,HETB.QIANDRQ,JIHKJB.MINGC,LEIBZTB.MINGC,HETB.ID,\n" + 
						"                  (HETB.LEIB, HETB.QISRQ, HETB.GUOQRQ),HETB.DIANCXXB_ID,HETSLB.DIANCXXB_ID,HETB.GONGYSB_ID,HETB.HETGYSBID,\n" + 
						"                  HETB.LIUCZTB_ID,HETB.LEIB,LEIBZTB.ID,B.ID,B.HETBH) A\n" + 
						"         WHERE A.QISRQ >= '" + getBeginriqDate()+ "' \n" + 
						"           AND A.QISRQ <= '" + getEndriqDate() + "'" + hetbhCondi+jihkjCondi+ hetlCondi + weizCondi + xufCondi + leixCondi + gongfCondi+diancCondi+ "  order by hetbh,xufdwmc,gongfdwmc) SR\n"+
						" GROUP BY ROLLUP(DIANCXXB_ID,(ID, HETBH, XUFDWMC, GONGFDWMC, QIANDRQ, QISRQ, GUOQRQ,JIHKJ, JIESFS, FID, FBH))\n" + 
						" ORDER BY GROUPING(DIANCXXB_ID), DIANCXXB_ID,SR.HETBH,GROUPING(ID),ID");

		ResultSetList rsl=con.getResultSetList(buffer.toString());
		
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][13];
		ArrWidth = new int[] { 100, 250, 180, 55, 70, 50,50, 70,70,70,  70, 60,120 };
		ArrHeader[0] = new String[] { "�跽��λ", "������λ","��ͬ��",  "����<br>(��)",
				"����<br>(ǧ��/ǧ��)", "ú��<br>(Ԫ/��)",  "�˼�<br>(Ԫ/��)","���㷽ʽ","ǩ������", "��ʼ����","��ֹ����","��ͬ����","����ͬ"};
		rt.setBody(new Table(rsl.getRows() + 1, 13));
		rt.body.setHeaderData(ArrHeader);
		
		int i=2;
		String context=MainGlobal.getHomeContext(this);
		while (rsl.next()){
			rt.body.setCellValue(i, 1, rsl.getString("XUFDWMC"));
			rt.body.setCellValue(i, 2, rsl.getString("GONGFDWMC"));
			if(rsl.getString("ID")==null||rsl.getString("ID").equals("")){
				rt.body.setCellValue(i, 3, "");
			}else{
				System.out.println();
				if(rsl.getString("HETBH").indexOf("downfile.jsp?filepath")==-1){
					rt.body.setCellValue(i, 3, "<a target=_blank href="+ context+ "/app?service=page/Shenhrz&hetb_id=" +rsl.getString("ID")+ ">"+ rsl.getString("HETBH")+ "</a>");
				}else{
					rt.body.setCellValue(i, 3, rsl.getString("HETBH"));
				}
			}
			rt.body.setCellValue(i, 4, rsl.getString("HETL"));
			rt.body.setCellValue(i, 5, rsl.getString("REL"));
			rt.body.setCellValue(i, 6, rsl.getString("MEIJ"));
			rt.body.setCellValue(i, 7, rsl.getString("YUNJ"));
			rt.body.setCellValue(i, 8, rsl.getString("JIESFS"));
			rt.body.setCellValue(i, 9, rsl.getString("QIANDRQ"));
			rt.body.setCellValue(i, 10, rsl.getString("QISRQ"));
			rt.body.setCellValue(i, 11, rsl.getString("GUOQRQ"));
			rt.body.setCellValue(i, 12, rsl.getString("JIHKJ"));
			if(rsl.getString("FBH")==null||rsl.getString("FBH").equals("")){
				rt.body.setCellValue(i, 13, "");
			}else{
				rt.body.setCellValue(i, 13, "<a target=_blank href="+ context	+ "/app?service=page/Shenhrz&hetb_id=" + rsl.getString("FID") + ">"+ rsl.getString("FBH") + "</a>");
			}	
			i++;
		}
		
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString6());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle("ú   ̿   ��   ��   ��   ͬ   ��   ѯ", ArrWidth);
		// rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);

		rt.body.setPageRows(18);
		//	���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		//�����е�����
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		rt.body.setColAlign(12, Table.ALIGN_CENTER);
		rt.body.setColAlign(13, Table.ALIGN_CENTER);
		
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 10, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_RIGHT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}

	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		//��λ
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		visit.setDefaultTree(dt);
		if(MainGlobal.getXitxx_item("��ͬ", "��ʾ�跽", "0", "��").equals("��")){//
			
			TextField tf = new TextField();
			tf.setId("diancTree_text");
			tf.setWidth(100);
			tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
			
			ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
			tb2.setIcon("ext/resources/images/list-items.gif");
			tb2.setCls("x-btn-icon");
			tb2.setMinWidth(20);
			tb1.addText(new ToolbarText("��λ:"));
			tb1.addField(tf);
			tb1.addItem(tb2);
		}

		// ǩ������
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(80);
		tb1.addField(df1);
		// ��ͬ���
		tb1.addText(new ToolbarText("��ͬ���:"));

		TextField hetbh = new TextField();
		hetbh.setAllowBlank(true);
		hetbh.setId("ext_hetId");
		hetbh.setListeners("change:function(own,newValue,oldValue){"
				+ "document.all.item('hetbh').value=newValue;}");
		tb1.addField(hetbh);
		
		ToolbarButton tbb = new ToolbarButton(null, "ˢ��",
				"function(){document.forms[0].submit()}");
		tb1.addItem(tbb);
		ToolbarButton gengdtj = new ToolbarButton(null, "��������",
				"function(){Tiaojsz('xians')}");// ��������
		tb1.addItem(gengdtj);
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			setTreeid(null);
			visit.setDefaultTree(null);
			visit.setInt1(2);
			visit.setString1("");
			visit.setString2("");
			visit.setString3("");
			visit.setString4("");
			visit.setString5("");
			
			//begin��������г�ʼ������
			visit.setString6(null);
				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {
					visit.setString6(pagewith);
				}
		}
		if (visit.getboolean2()) {// ��˾���Ƹ���ʱ
			visit.setboolean2(false);
			this.getGongysDropDownModels();
		}
		getToolbars();
		blnIsBegin = true;

	}
	
// �糧��Ϣ
	
	public IDropDownBean getDiancDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getDiancDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setDiancDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setboolean2(true);
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setDiancDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getDiancDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getDiancDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getDiancDropDownModels() {
		String sql = "";
		//ר��Ϊ����趨
		String diancqc=((Visit)getPage().getVisit()).getDiancqc();
		if(diancqc.equals("�Ϻ�����ŵڶ������������ι�˾")){
			sql="select distinct d.id,d.mingc from hetslb h,diancxxb d\n" +
			"       where h.diancxxb_id=d.id and d.id=215\n" + 
			" order by d.id";
		}else{
				sql="select distinct d.id,d.mingc from hetslb h,diancxxb d\n" +
				"       where h.diancxxb_id=d.id\n" + 
				" order by d.id";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, "ȫ��"));
	}

	// ��Ӧ��
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean) getGongysDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getGongysDropDownModels() {
		String sql="";
		if (getDiancDropDownValue().getId() == -1) {
			 sql = 
				"select distinct gys.id,gys.mingc from hetb h,gongysb gys\n" +
				"       where h.gongysb_id=gys.id\n" + 
				"			  and gys.leix=1\n" +
				"             and h.qiandrq>=to_date('"+this.getBeginriqDate()+"','yyyy-MM-dd')\n" + 
				"             and h.qiandrq<=to_date('"+this.getEndriqDate()+"','yyyy-MM-dd') order by gys.mingc";
		}else{
			sql = 
				"select distinct gys.id,gys.mingc from hetb h,gongysb gys,hetslb sl\n" +
				"       where h.gongysb_id=gys.id\n" + 
				"			  and gys.leix=1\n" +
				"             and h.id=sl.hetb_id\n"+
				"             and sl.diancxxb_id="+getDiancDropDownValue().getId()+"\n"+
				"             and h.qiandrq>=to_date('"+this.getBeginriqDate()+"','yyyy-MM-dd')\n" + 
				"             and h.qiandrq<=to_date('"+this.getEndriqDate()+"','yyyy-MM-dd') order by gys.mingc";
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "ȫ��"));
	}

	// ����
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1, "ȫ��"));
		list.add(new IDropDownBean(0, Locale.dianccg_hetlx));
		list.add(new IDropDownBean(1, Locale.quyxs_hetlx));
		list.add(new IDropDownBean(2, Locale.quycg_hetlx));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
		return;
	}

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}


	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(_value);
	}

	// λ��
	public IDropDownBean getweizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getweizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setweizSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setweizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getweizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getweizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getweizSelectModels() {
		String sql = "select *\n"
				+ "from(\n"
				+ "select��leibztb.id,leibztb.mingc\n"
				+ "from leibztb,liuclbb\n"
				+ "where leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='��ͬ'\n"
				+ "union\n" + "select id,mingc\n" + "from leibztb\n"
				+ "where leibztb.liuclbb_id=0\n" + ")";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, "ȫ��"));
		return;
	}

	public String gethetl1() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void sethetl1(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String gethetl2() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void sethetl2(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	public String gethetbh() {
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void sethetbh(String value) {
		((Visit) getPage().getVisit()).setString3(value);
	}

	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null || ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate() {
		if (((Visit) getPage().getVisit()).getString5() == null
				|| ((Visit) getPage().getVisit()).getString5() == "") {
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setEndriqDate(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}
	 //�ƻ��ھ�
    public IDropDownBean getJihxzValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean3()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean)getIJihxzModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setJihxzValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }


    public void setIJihxzModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getIJihxzModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
            getIJihxzModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void getIJihxzModels() {
        String sql = 
        	"select id,mingc\n" +
        	"from jihkjb";
        ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"")) ;
        return ;
    }

    private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
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

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}