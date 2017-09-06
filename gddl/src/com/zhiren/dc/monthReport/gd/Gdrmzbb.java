package com.zhiren.dc.monthReport.gd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.diaoygl.AutoCreateDaily_Report_gd;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ���衻�
 * ʱ�䣺2010-08-16
 * ������
 *     ���ݹ�����Ҫ���� ȼú����ܱ���
 */
/*
 * ���ߣ�sy
 * ʱ�䣺2011-4-30
 * ������
 *     �޸�ȼú����ܱ���sql
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-11-02
 * ���÷�Χ���������
 * �����������������ظ��Ĵ�ͬ��������
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-11-12
 * ���÷�Χ���������
 * �����������������ڿ򲢶Բ�ѯ�������Ӧ�޸�
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-11-17
 * ���÷�Χ���������
 * ������������ѯ��䣬��ú�ۣ��˼ۣ���ֵ���������Ϣʱ�������Ȩ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-11-23
 * ���÷�Χ���������
 * ��������������ṹ�������볧��ú�������ۼ��У��ҿ�������Ϣȡ������
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-12-13
 * ���÷�Χ���������
 * ���������������Ȩ���㷽��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-16
 * ���÷�Χ���������
 * ��������������������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-26
 * ���÷�Χ���������
 * ���������������Ȩ����
 */
public class Gdrmzbb extends BasePage {
	
	//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		setMsg(null);
		return getRanmzb();
	}

	private String getRanmzb() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));

		StringBuffer strSQL = new StringBuffer();

		String sql=	"select diancmc,rucml,rucml-hrucml hrucml,rulml,rulml-hrulml hrulml,kedkc,kedkc-hkedkc hkedkc,keyts,hkeyts,meij,meij-hmeij hmeij,yunf,yunf-hyunf hyunf,\n" +
			"round_new(rez/4.1816*1000,0)rez,\n" +
			"round_new((rez-hrez)/4.1816*1000,0) hrez,\n" +
			"round_new(decode(nvl(rez,0),0,0,(meij/1.17+yunf*0.93)*29.271/rez),2) bhsbmdj,\n" + 
			"round_new(decode(nvl(rez,0),0,0,(meij/1.17+yunf*0.93)*29.271/rez),2)-round_new(decode(nvl(hrez,0),0,0,(hmeij/1.17+hyunf*0.93)*29.271/hrez),2) hbusbmdj,\n" + 
			"round_new(decode(nvl(ljrez,0),0,0,(ljmeij/1.17+ljyunf*0.93)*29.271/ljrez),2) ljbhsbmdj\n" + 
			"from (\n" + 
			"select decode(grouping(dianc.mingc), 1, '�ϼ�', dianc.mingc) diancmc,\n" + 
			"round_new(sum(shouhc.rucml), 0) rucml,\n" + 
			"round_new(sum(hshouhc.rucml), 0) hrucml,\n" + 
			"round_new(sum(shouhc.rulml), 0) rulml,\n" + 
			"round_new(sum(hshouhc.rulml), 0) hrulml,\n" + 
			"--round_new(sum(dangrsj.kuc), 0) kuc,\n" + 
			"round_new(sum(dangrsj.kuc), 0) kedkc,\n" + 
			"round_new(sum(hdangrsj.kuc) , 0) hkedkc,\n" + 
			"decode(grouping(dianc.mingc),0,keyts_rb_new(dianc.id, date '"+this.getEndriqDate()+"', dianc.jib),round_new(decode(sum(dianc.rijhm),0,0,sum(dangrsj.kuc) / sum(dianc.rijhm)),0)) keyts,\n" + 
			"decode(grouping(dianc.mingc),0,sum(keyts_rb_new(dianc.id, date '"+this.getEndriqDate()+"', dianc.jib))-sum(keyts_rb_new(dianc.id, date '"+this.getHBEndriqDate()+"', dianc.jib)),round_new(decode(sum(dianc.rijhm),0,0,sum(dangrsj.kuc) / sum(dianc.rijhm) - sum(hdangrsj.kuc) / sum(dianc.rijhm)),0)) hkeyts,\n" + 
			"round_new(decode(sum(decode(nvl(gus.hsmeij,0),0,0,gus.laimslmj)),0,0,sum(gus.hsmeij * gus.laimslmj) / sum(decode(nvl(gus.hsmeij,0),0,0,gus.laimslmj))),2) meij,\n" +
			"round_new(decode(sum(decode(nvl(hgus.hsmeij,0),0,0,hgus.laimslmj)),0,0,sum(hgus.hsmeij * hgus.laimslmj) / sum(decode(nvl(hgus.hsmeij,0),0,0,hgus.laimslmj))),2) hmeij,\n" + 
			"round_new(decode(sum(decode(nvl(ljgus.hsmeij,0),0,0,ljgus.laimslmj)),0,0,sum(ljgus.hsmeij * ljgus.laimslmj) / sum(decode(nvl(ljgus.hsmeij,0),0,0,ljgus.laimslmj))),2) ljmeij,\n" + 
			"round_new(decode(sum(decode(nvl(gus.hsmeij,0),0,0,gus.laimslmj)),0,0,sum(decode(gus.hsmeij,0,0,gus.hsyunf) * gus.laimslmj) / sum(decode(nvl(gus.hsmeij,0),0,0,gus.laimslmj))),2) yunf,\n" + 
			"round_new(decode(sum(decode(nvl(hgus.hsmeij,0),0,0,hgus.laimslmj)),0,0,sum(decode(hgus.hsmeij,0,0,hgus.hsyunf) * hgus.laimslmj) / sum(decode(nvl(hgus.hsmeij,0),0,0,hgus.laimslmj))),2) hyunf,\n" + 
			"round_new(decode(sum(decode(nvl(ljgus.hsmeij,0),0,0,ljgus.laimslmj)),0,0,sum(decode(ljgus.hsmeij,0,0,ljgus.hsyunf) * ljgus.laimslmj) / sum(decode(nvl(ljgus.hsmeij,0),0,0,ljgus.laimslmj))),2) ljyunf,\n" + 
			"round_new(decode(sum(decode(nvl(gus.rez,0),0,0,gus.laimslrez)),0,0,sum(gus.rez * gus.laimslrez) / sum(decode(nvl(gus.rez,0),0,0,gus.laimslrez))),2) rez,\n" + 
			"round_new(decode(sum(decode(nvl(hgus.rez,0),0,0,hgus.laimslrez)),0,0,sum(hgus.rez * hgus.laimslrez) / sum(decode(nvl(hgus.rez,0),0,0,hgus.laimslrez))),2) hrez,\n" + 
			"round_new(decode(sum(decode(nvl(ljgus.rez,0),0,0,ljgus.laimslrez)),0,0,sum(ljgus.rez * ljgus.laimslrez) / sum(decode(nvl(ljgus.rez,0),0,0,ljgus.laimslrez))),2) ljrez,"+
			"dianc.xuh,2 xuh2\n" + 
			"from (select id, xuh,jib, fuid, mingc, rijhm from diancxxb where id in("+this.getTreeid()+") and id <> 112 and  id <> 300) dianc,\n" + 
			"(select diancxxb_id,\n" + 
			"sum(dangrgm) rucml,\n" + 
			"sum(haoyqkdr) rulml,\n" + 
			"sum(kuc) kuc,\n" + 
			"sum(kedkc) kedkc\n" + 
			"from shouhcrbb s\n" + 
			"where s.riq >= date '"+this.getBeginriqDate()+"'\n" + 
			"and s.riq <= date '"+this.getEndriqDate()+"'\n" + 
			"group by diancxxb_id) shouhc,\n" + 
			"(select diancxxb_id,\n" + 
			"sum(dangrgm) rucml,\n" + 
			"sum(haoyqkdr) rulml,\n" + 
			"sum(kuc) kuc,\n" + 
			"sum(kedkc) kedkc\n" + 
			"from shouhcrbb s\n" + 
			"where s.riq >= date '"+this.getHBBeginriqDate()+"'\n" + 
			"and s.riq <= date '"+this.getHBEndriqDate()+"'\n" + 
			"group by diancxxb_id) hshouhc,\n" + 
			"(select diancxxb_id, kuc, kedkc\n" + 
			"from shouhcrbb s\n" + 
			"where s.riq = date '"+this.getEndriqDate()+"') dangrsj,\n" + 
			"(select diancxxb_id, kuc, kedkc\n" + 
			"from shouhcrbb s\n" + 
			"where s.riq = date '"+this.getHBEndriqDate()+"') hdangrsj,\n" + 
			"(select g.diancxxb_id dianid,sum(g.laimsl) laimsl,sum(decode(g.meij,0,0,g.laimsl)) laimslmj,sum(decode(g.rez,0,0,g.laimsl)) laimslrez,\n" + 
			"decode(sum(decode(g.meij,0,0,g.laimsl)),0,0,sum(g.meij * g.laimsl) / sum(decode(g.meij,0,0,g.laimsl))) hsmeij,\n" + 
			"--decode(sum(decode(NVL(g.yunj,0),0,0,g.laimsl)),0,0,sum((g.yunj *g.laimsl) / sum(decode(g.yunj,0,0,g.laimsl))) hsyunf,\n" + 
			"decode(sum(decode(g.meij,0,0,g.laimsl)),0,0,sum(g.laimsl * decode(g.meij,0,0,g.yunj)) / sum(decode(g.meij,0,0,g.laimsl))) hsyunf,\n" + 
			"decode(sum(decode(g.rez,0,0,g.laimsl)),0,0,sum(g.rez * g.laimsl) / sum(decode(g.rez,0,0,g.laimsl))) rez\n" + 
			"from  shouhcfkb g\n" + 
			"where g.riq >= date '"+this.getBeginriqDate()+"'\n" + 
			"and g.riq <= date '"+this.getEndriqDate()+"'\n" + 
			"group by g.diancxxb_id) gus,\n" + 
			"(select g.diancxxb_id dianid,sum(g.laimsl) laimsl,sum(decode(g.meij,0,0,g.laimsl)) laimslmj,sum(decode(g.rez,0,0,g.laimsl)) laimslrez,\n" + 
			"decode(sum(decode(g.meij,0,0,g.laimsl)),0,0,sum(g.meij * g.laimsl) / sum(decode(g.meij,0,0,g.laimsl))) hsmeij,\n" + 
			"--decode(sum(decode(NVL(g.yunj,0),0,0,g.laimsl)),0,0,sum((g.yunj *g.laimsl) / sum(decode(g.yunj,0,0,g.laimsl))) hsyunf,\n" + 
			"decode(sum(decode(g.meij,0,0,g.laimsl)),0,0,sum(g.laimsl * decode(g.meij,0,0,g.yunj)) / sum(decode(g.meij,0,0,g.laimsl))) hsyunf,\n" + 
			"decode(sum(decode(g.rez,0,0,g.laimsl)),0,0,sum(g.rez * g.laimsl) / sum(decode(g.rez,0,0,g.laimsl))) rez\n" + 
			"from shouhcfkb g\n" + 
			"where  g.riq >= date '"+this.getHBBeginriqDate()+"'\n" + 
			"and g.riq <= date '"+this.getHBEndriqDate()+"'\n" + 
			"group by g.diancxxb_id) hgus,\n" + 
			"(select g.diancxxb_id dianid,sum(g.laimsl) laimsl,sum(decode(g.meij,0,0,g.laimsl)) laimslmj,sum(decode(g.rez,0,0,g.laimsl)) laimslrez,\n" + 
			"decode(sum(decode(g.meij,0,0,g.laimsl)),0,0,sum(g.meij * g.laimsl) / sum(decode(g.meij,0,0,g.laimsl))) hsmeij,\n" + 
			"--decode(sum(decode(NVL(g.yunj,0),0,0,g.laimsl)),0,0,sum((g.yunj *g.laimsl) / sum(decode(g.yunj,0,0,g.laimsl))) hsyunf,\n" + 
			"decode(sum(decode(g.meij,0,0,g.laimsl)),0,0,sum(g.laimsl * decode(g.meij,0,0,g.yunj)) / sum(decode(g.meij,0,0,g.laimsl))) hsyunf,\n" + 
			"decode(sum(decode(g.rez,0,0,g.laimsl)),0,0,sum(g.rez * g.laimsl) / sum(decode(g.rez,0,0,g.laimsl))) rez\n" + 
			"from  shouhcfkb g\n" + 
			"where g.riq >= first_day(date '"+this.getEndriqDate()+"')\n" + 
			"and g.riq <= date '"+this.getEndriqDate()+"'\n" + 
			"group by g.diancxxb_id)ljgus\n" + 
			" where dianc.id = shouhc.diancxxb_id(+)\n" + 
			"  and dianc.id = gus.dianid(+)\n" + 
			" and dianc.id = hshouhc.diancxxb_id(+)\n" + 
			" and dianc.id = hgus.dianid(+)\n" + 
			" AND dianc.id = ljgus.dianid(+)\n" + 
			" and dianc.id = dangrsj.diancxxb_id(+)\n" + 
			" and dianc.id = hdangrsj.diancxxb_id(+)\n" + 
			" group by rollup(dianc.fuid, (dianc.mingc, dianc.xuh,dianc.id,dianc.jib))\n" + 
			"    having  not  grouping(dianc.fuid) + grouping(dianc.mingc)=1\n" + 
			" ) order by xuh2,xuh";
		
		
		ResultSetList rs = con.getResultSetList(sql);
		
		Report rt = new Report();
		
		String ArrHeader[][]=new String[3][18];
		ArrHeader[0]=new String[] {"��λ","���볧ԭú��<br>���֣�","���볧ԭú��<br>���֣�","�ܺ���ԭú��<br>���֣�","�ܺ���ԭú��<br>���֣�","���","���","���","���","��Ȼú����(��˰)<br>��Ԫ/�֣�","��Ȼú����(��˰)<br>��Ԫ/�֣�","�˷�(��˰)<br>��Ԫ/��)","�˷�(��˰)<br>��Ԫ/��)","����ú��ֵ<br>��ǧ��/ǧ�ˣ�","����ú��ֵ<br>��ǧ��/ǧ�ˣ�","�볧��ú����(����˰)<br>��Ԫ/�֣�","�볧��ú����(����˰)<br>��Ԫ/�֣�","�볧��ú����(����˰)<br>��Ԫ/�֣�"};
		ArrHeader[1]=new String[] {"��λ","���볧ԭú��<br>���֣�","���볧ԭú��<br>���֣�","�ܺ���ԭú��<br>���֣�","�ܺ���ԭú��<br>���֣�","�������֣�","�������֣�","��������","��������","��Ȼú����(��˰)<br>��Ԫ/�֣�","��Ȼú����(��˰)<br>��Ԫ/�֣�","�˷�(��˰)<br>��Ԫ/��)","�˷�(��˰)<br>��Ԫ/��)","����ú��ֵ<br>��ǧ��/ǧ�ˣ�","����ú��ֵ<br>��ǧ��/ǧ�ˣ�","�볧��ú����(����˰)<br>��Ԫ/�֣�","�볧��ú����(����˰)<br>��Ԫ/�֣�","�볧��ú����(����˰)<br>��Ԫ/�֣�"};
		ArrHeader[2]=new String[] {"��λ","����","����","����","����","����","����","����","����","����","����","����","����","����","����","����","����","���ۼ�"};
		int ArrWidth[]=new int[] {70,55,55,55,55,55,55,40,40,55,55,50,50,55,55,55,55,55};
		//rs.beforefirst();
		rt.setBody(new Table(rs, 3, 0, 0));

		if(rs.getRows()!=0){
				for(int j=4;j<=rs.getRow();j++){
					for(int k=2;k<=18;k++){
						rt.body.setCellAlign(j, k, Table.ALIGN_RIGHT);
					}
				}
		}
		for(int i=1;i<=3+rs.getRows();i++){
			for(int j=1;j<=18;j++){
				rt.body.setCellFont(i, j, "����", 9, false);
			}
		}
		//rt.body.setRowHeight(30);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.merge(1, 1, 3, 1);
//		rt.body.merge(1, 2, 2, 3);
//		rt.body.merge(1, 4, 2, 5);
//		rt.body.merge(1, 6, 2, 6);
//		rt.body.merge(1, 7, 1, 10);
//		rt.body.merge(1, 11, 2, 12);
//		rt.body.merge(1, 13, 2, 14);
//		rt.body.merge(1, 15, 2, 16);
//		rt.body.merge(1, 17, 2, 18);
//		rt.body.merge(2, 7, 2, 8);
//		rt.body.merge(2, 9, 2, 10);
		rt.body.mergeFixedRowCol();
		
		rt.createTitle(3, ArrWidth);
		rt.title.setCellValue(2, 1, "�������ȼú����ܱ���", 18);
		rt.title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		rt.title.setCellFont(2, 1, "����", 16, true);
		rt.title.setCellValue(3, 1, "����:"+riq+"-"+riq1,14);
		rt.title.setCellValue(3, 15, "���λ:ȼ������",4);
		rt.title.setCellFont(3, 1, "����", 10, false);
		rt.title.setCellFont(3, 2, "����", 10, false);
		rt.title.setRowHeight(1, 1);
		rt.title.setRowHeight(2, 10);
		rt.title.setRowHeight(3, 10);

		rt.body.setPageRows(21);
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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	///��ʼ����////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	
	//////////��������//////////
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	///���ȿ�ʼ����////
	public String getHBBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString6()==null||((Visit) getPage().getVisit()).getString6()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString6(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString6();
	}
	public void setHBBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString6(value);
	}
	
	//////////���Ƚ�������//////////
	public String getHBEndriqDate(){
		if(((Visit) getPage().getVisit()).getString7()==null||((Visit) getPage().getVisit()).getString7()==""){
			((Visit) getPage().getVisit()).setString7(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString7();
	}
	public void setHBEndriqDate(String value){
		((Visit) getPage().getVisit()).setString7(value);
	}
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("��������:"));
		DateField hbdf = new DateField();

		hbdf.setValue(this.getHBBeginriqDate());
		hbdf.Binding("Hbksrq","");// ��htmlҳ�е�id��,���Զ�ˢ��
		hbdf.setWidth(80);
		tb1.addField(hbdf);
		tb1.addText(new ToolbarText("��"));
		DateField hbdf1 = new DateField();
		hbdf1.setValue(this.getHBEndriqDate());
		hbdf1.Binding("Hbjsrq","");// ��htmlҳ�е�id��,���Զ�ˢ��
		hbdf1.setWidth(80);
		tb1.addField(hbdf1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ʼ����:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("��"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

//		Visit visit = (Visit) getPage().getVisit();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("��ϵ糧");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);

//		��ˢ��ʱ���û���ʾ�Ƿ���ڲ���������
		setMsg(null);
		Date beginRiq=DateUtil.getDate(this.getBeginriqDate());
		Date endRiq=DateUtil.getDate(this.getEndriqDate());
//		�������Ϊ���գ�����ʾ���յ�ú�۵���Ϣ��������ʾ�ۼ���Ϣ
		AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
		JDBCcon con = new JDBCcon();
		String errmsg=DR.RPChk(con, getTreeid(),beginRiq,endRiq );
		if(errmsg.length()>0){
			setMsg(errmsg+"��Ϣ����ȫ����ע��");
		}
		con.Close();
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
//			������ڿ�
			visit.setString4(null);
			visit.setString5(null);
			visit.setString6(null);
			visit.setString7(null);
			setTreeid(null);
			visit.setExtTree1(null);
		}
		getToolbars();

		blnIsBegin = true;

	}



	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
//	�糧����
//	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

//	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
//		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
//			_DiancmcChange = false;
//		}else{
//			_DiancmcChange = true;
//		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
//	�ֹ�˾������
//	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
			.setDropDownBean4((IDropDownBean) getFengsModel()
					.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
//		if (getFengsValue().getId() != Value.getId()) {
//			_fengschange = true;
//		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"�й����Ƽ���"));
	}

//	�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}

	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}
//	���ӵ糧��ѡ���ļ���
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}

}