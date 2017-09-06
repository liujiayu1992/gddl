package com.zhiren.jt.zdt.monthreport.yuecgjhb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/* 
 * ʱ�䣺2009-06-26
 * ���ߣ� sy
 * �޸����ݣ��޸ġ�������ơ�����Ϊ��������λ����ͳһ����
 *			
 */
/* 
 * ʱ�䣺2009-06-26
 * ���ߣ� sy
 * �޸����ݣ��޸��·�ʱ�õ��糧ID��sql��ԭsql�糧����û�ӵ����ţ�sql����
 *			
 */
/* 
 * ʱ�䣺2009-09-21
 * ���ߣ� sy
 * �޸����ݣ��޸Ĺ�Ӧ�������˵���ѯsql�����Բ�����й�Ӧ�̡��ֹ�Ӧ����ʱδ���糧��������ĵ糧ѡ���˹�Ӧ�̡�
 *			
 */

/* 
 * ʱ�䣺2009-09-29
 * ���ߣ� sy
 * �޸����ݣ��޸ķ�վ��վ�����˵���ѯsql�����Բ�����г�վ���ֳ�վ��ʱδ���糧��������ĵ糧ѡ���˳�վ��
 *			
 */
/* 
 * ʱ�䣺2012-04-25
 * ���ߣ� ��ʤ��
 * �޸����ݣ����䷽ʽ��������վǰ
 *         �������Զ������ɳ����+�˷�+�ӷ�
 *         �����п�
 *         ǰ̨���������⣬��ú���ۼ�������			
 */
/* 
 * ʱ�䣺2012-07-11
 * ���ߣ� ���
 * �޸����ݣ���д���ɷ���
 * 			���¸��������¼ƻ�����
 * 			ȡ���´﹦��
 * 			����Ʒ���ֶ�
 */

public class Yuecgjhb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO �Զ����ɷ������
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if (flag != -1) {
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	private boolean _ShengcChick = false;

	public void ShengcButton(IRequestCycle cycle) {
		_ShengcChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if (_ShengcChick) {
			_ShengcChick = false;
			Shengc();
		}

		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
	}
	
	public String getDate(){
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}

		String StrMonth1 = "";
		if (intMonth < 10) {
			StrMonth1 = "0" + intMonth;
		} else {
			StrMonth1 = "" + intMonth;
		}
		// �������
		String strriq = intyear + "-" + StrMonth1 + "-01";
		return strriq;
	}
	
//	 ����
	public void Shengc() {
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		
		String strriq=getDate();
		String inser_sql ="insert into yuecgjhb\n" +
		"(id, riq, diancxxb_id, gongysb_id,meikxxb_id, jihkjb_id,pinzb_id, faz_id, daoz_id, yunsfsb_id,yuejhcgl,\n" + 
		"chebjg, yunf, zaf, rez, jiakk, huiff, liuf, daocj,biaomdj)\n" + 
		"(SELECT getnewid(diancxxb_id),riq,diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,yunsfsb_id,hej*10000,\n" + 
		"chebjg,yunf,zaf,rez,0, huiff, liuf, chebjg+yunf+zaf daocj,\n" + 
		"Round((chebjg+yunf -Round((chebjg-chebjg/1.17),2)-Round(yunf*0.07,2)+zaf)*29.271/rez,2) biaomdj\n" + 
		"FROM(SELECT riq,diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,yunsfsb_id,\n" + 
		"round(sum(hej),4)hej,\n" + 
		"round(decode(sum(hej),0,0,sum(hej*chebjg)/SUM(hej)),2)chebjg,\n" + 
		"round(decode(sum(hej),0,0,sum(hej*yunf)/SUM(hej)),2)yunf,\n" + 
		"round(decode(sum(hej),0,0,sum(hej*zaf)/SUM(hej)),2)zaf,\n" + 
		"round(decode(sum(hej),0,0,sum(hej*rez)/SUM(hej)),3)rez,\n" + 
		"round(decode(sum(hej),0,0,sum(hej*huiff)/SUM(hej)),3)huiff,\n" + 
		"round(decode(sum(hej),0,0,sum(hej*liuf)/SUM(hej)),3)liuf\n" + 
		" FROM NIANDHTQKB q  WHERE diancxxb_id=" + getTreeid() + "  AND riq=DATE'"+ strriq + "' AND hej>0\n" + 
		" GROUP BY (riq,diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,yunsfsb_id) )\n" + 
		" )";


		con.getInsert(inser_sql);// ��������
		con.Close();
	}
	
//	 ������������
	public void CoypLastYueData() {
		JDBCcon con = new JDBCcon();
		String strriq=getDate();
		
		String inser_sql = "insert into yuecgjhb\n" +
				"(id, riq, diancxxb_id, gongysb_id,meikxxb_id, jihkjb_id, pinzb_id,faz_id, daoz_id, yunsfsb_id,yuejhcgl,\n" + 
				"chebjg, yunf, zaf, rez, jiakk, huiff, liuf, daocj,biaomdj)\n" + "(select getnewid(y.diancxxb_id) as id,\n"
				+ "       date'" + strriq + "' as riq,\n"
				+ "       y.diancxxb_id as diancxxb_id,\n"
				+ "       y.gongysb_id as gongysb_id,\n"
				+ "       y.meikxxb_id as meikxxb_id,\n"
				+ "       y.jihkjb_id as jihkjb_id,\n"
				+ "       y.pinzb_id as pinzb_id,\n"
				+ "       y.faz_id as faz_id,\n"
				+ "       y.daoz_id as daoz_id,\n"	
				+ "       y.yunsfsb_id as yunsfsb_id,\n"	
				+ "       y.yuejhcgl as yuejhcgl,\n"
				+ "       y.chebjg as chebjg,\n"
				+ "       y.yunf as yunf,\n"
				+ "       y.zaf as zaf,\n"
				+ "       y.rez as rez,\n"
				+ "       y.jiakk as jiakk,\n"
				+ "       y.huiff as huiff,\n"
				+ "       y.liuf as liuf,\n"
				+ "       y.daocj as daocj,\n"
				+ "       y.biaomdj as biaomdj\n"
				+ "  from yuecgjhb y \n"
				+ " where y.riq =ADD_MONTHS(date'" + strriq + "',-1)  \n" 
				+ "   and y.diancxxb_id = " + getTreeid() + ")";;
				
		con.getInsert(inser_sql);// ��������
		con.Close();
	}


	public void getSelectData() {
		JDBCcon con = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		int jib = this.getDiancTreeJib();

		StringBuffer strSQL = new StringBuffer();

		StringBuffer strgrouping = new StringBuffer();
		StringBuffer strwhere = new StringBuffer();
		StringBuffer strgroupby = new StringBuffer();
		StringBuffer strhaving = new StringBuffer();
		StringBuffer strorderby = new StringBuffer();

		if (jib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			// �ֹ�˾
			strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,dc.mingc) as diancxxb_id,\n");
			strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,'С��',g.mingc) as gongysb_id,\n");
			strwhere.append(" and (dc.fgsid=" + this.getTreeid()+ " or dc.rlgsid= " + this.getTreeid()+ ") \n");
			strgroupby.append("group by rollup(dc.fgsmc,dc.mingc,g.mingc,mk.mingc,j.mingc,p.mingc,ch.mingc,che.mingc,ys.mingc,y.id)\n");
			strhaving.append("  having not (grouping(g.mingc) || grouping(y.id)) =1 and not grouping(dc.fgsmc)=1 \n");
			strorderby.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
				
		} else if (jib == 3) {// ѡ�糧ֻˢ�³��õ糧
			strgrouping.append("decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as diancxxb_id,\n");
			strgrouping.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,'С��',g.mingc) as gongysb_id,\n");
			strwhere.append(" and dc.id=").append(this.getTreeid());
			strgroupby.append("group by rollup(dc.mingc,g.mingc,mk.mingc,j.mingc,p.mingc,ch.mingc,che.mingc,ys.mingc,y.id)\n");
			strhaving.append("having not (grouping(g.mingc) || grouping(y.id)) =1 and not grouping(dc.mingc)=1\n");
			strorderby.append("order by grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
		}

		strSQL.append("SELECT ID,riq,diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id,faz_id,daoz_id,yuejhcgl,\n");
		strSQL.append("rez,rez_dk,huiff,liuf,chebjg,yunf,zaf,daocj,\n");
		strSQL.append("DECODE(rez,0,0,round((chebjg+yunf-round(chebjg-chebjg/1.17,2)-round(yunf*0.07,2)+zaf)*29.271/rez,2))biaomdj,\n");
		strSQL.append("jiakk from (\n");
		strSQL.append("select \n");
		strSQL.append("max(y.id) as id,max(y.riq) as riq,\n");
		strSQL.append(strgrouping);
		strSQL.append("nvl(mk.mingc,'-') as meikxxb_id,nvl(j.mingc,'-') as jihkjb_id,nvl(p.mingc,'-') as pinzb_id,nvl(ys.mingc,'') as yunsfsb_id,nvl(ch.mingc,'-') as faz_id,nvl(che.mingc,'-') as daoz_id,\n");
		strSQL.append("sum(y.yuejhcgl) as yuejhcgl,");
		strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl), 3)) as rez,\n");
		strSQL.append("decode(sum(y.yuejhcgl),0,0,round(sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl)*1000/4.1816, 0)) as rez_dk,\n");// ��ֵ_��
		strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.huiff*y.yuejhcgl)/sum(y.yuejhcgl),2)) as huiff,\n");// �ӷ���
		strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.liuf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as liuf,\n");// ���
		strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.chebjg*y.yuejhcgl)/sum(y.yuejhcgl),2)) as chebjg,\n");
		strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.yunf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as yunf,\n");
		strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.zaf*y.yuejhcgl)/sum(y.yuejhcgl),2)) as zaf,\n");
		strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum((y.chebjg+y.yunf+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl),2)) as daocj,\n");
		strSQL.append("Round(decode(sum(y.rez*y.yuejhcgl),0,0,Round((sum((y.chebjg/(1+0.17)+y.yunf*(1-0.07)+y.zaf)*y.yuejhcgl)/sum(y.yuejhcgl)),2)*29.271/Round((sum(y.rez*y.yuejhcgl)/sum(y.yuejhcgl)), 3)),2) as biaomdj,\n");
		strSQL.append("max(y.jiakk) as jiakk\n");
		strSQL.append(" from yuecgjhb y, gongysb g, chezxxb ch, chezxxb che, vwdianc dc,jihkjb j,yunsfsb ys,meikxxb mk,pinzb p\n");
		strSQL.append("  where y.gongysb_id = g.id(+) and y.meikxxb_id = mk.id(+) and y.pinzb_id = p.id(+)\n");
		strSQL.append(" and y.faz_id = ch.id(+) and y.daoz_id = che.id(+) and y.diancxxb_id = dc.id(+) \n");
		strSQL.append(" and y.jihkjb_id = j.id(+) and y.yunsfsb_id = ys.id(+)");
		strSQL.append(" and y.riq=to_date('"+getDate()+"','yyyy-mm-dd') \n");
		strSQL.append(strwhere);
		strSQL.append(strgroupby);
		strSQL.append(strhaving);
		strSQL.append(strorderby);
		strSQL.append(")");
		
		ResultSetList rsl = con.getResultSetList(strSQL.toString());
		
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yuecgjhb");
		egu.setWidth("bodyWidth");
		egu.getColumn("riq").setCenterHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);

		egu.setDefaultsortable(false);//����ÿ�б�ͷ����󲻿�������
		egu.getColumn("diancxxb_id").setCenterHeader("�糧����");
		egu.getColumn("gongysb_id").setCenterHeader("������λ");
		egu.getColumn("MEIKXXB_ID").setCenterHeader("ú��λ");
		egu.getColumn("jihkjb_id").setCenterHeader("�ƻ��ھ�");
		egu.getColumn("pinzb_id").setCenterHeader("Ʒ��");
		egu.getColumn("yunsfsb_id").setCenterHeader("���䷽ʽ");
		egu.getColumn("faz_id").setCenterHeader("��վ");
		egu.getColumn("daoz_id").setCenterHeader("��վ");		
		egu.getColumn("yuejhcgl").setCenterHeader("�ƻ���<br>(��)");
		egu.getColumn("rez").setCenterHeader("��ֵ<br>(MJ/Kg)");
		egu.getColumn("rez_dk").setCenterHeader("��ֵ<br>(kcal/Kg)");
		egu.getColumn("rez_dk").setEditor(null);
		egu.getColumn("rez_dk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rez_dk").setUpdate(false);
		egu.getColumn("huiff").setCenterHeader("�ӷ���<br>%");
		egu.getColumn("liuf").setCenterHeader("���<br>%");
		egu.getColumn("chebjg").setCenterHeader("�����<br>(Ԫ/��)");
		egu.getColumn("yunf").setCenterHeader("�˷�<br>(Ԫ/��)");
		egu.getColumn("zaf").setCenterHeader("�ӷ�<br>(Ԫ/��)");
		
		egu.getColumn("daocj").setCenterHeader("������<br>(Ԫ/��)");
		egu.getColumn("daocj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("biaomdj").setCenterHeader("����˰��ú<br>����(Ԫ/��)");
		egu.getColumn("biaomdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jiakk").setCenterHeader("�ӿۿ�<br>(Ԫ/��)");
		egu.getColumn("jiakk").setHidden(true);


		((NumberField) egu.getColumn("rez").editor).setDecimalPrecision(3);
		((NumberField) egu.getColumn("huiff").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("liuf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("chebjg").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("yunf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("zaf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("daocj").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("biaomdj").editor).setDecimalPrecision(2);
		
		
		// ���ò��ɱ༭����ɫ
		// egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");

		// �趨�г�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setWidth(100);
		
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("pinzb_id").setWidth(70);
		egu.getColumn("yunsfsb_id").setWidth(70);
		egu.getColumn("yunsfsb_id").setDefaultValue("");
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("yuejhcgl").setWidth(80);
		egu.getColumn("yuejhcgl").setDefaultValue("0");
		egu.getColumn("rez").setWidth(45);
		egu.getColumn("rez").setDefaultValue("0.000");
		egu.getColumn("rez_dk").setWidth(60);
		egu.getColumn("rez_dk").setDefaultValue("0");
		egu.getColumn("huiff").setWidth(50);
		egu.getColumn("huiff").setDefaultValue("0.00");
		egu.getColumn("liuf").setWidth(45);
		egu.getColumn("liuf").setDefaultValue("0.00");
		egu.getColumn("chebjg").setWidth(70);
		egu.getColumn("chebjg").setDefaultValue("0.00");
		egu.getColumn("yunf").setWidth(70);
		egu.getColumn("yunf").setDefaultValue("0.00");
		egu.getColumn("zaf").setWidth(70);
		egu.getColumn("zaf").setDefaultValue("0.00");
		egu.getColumn("daocj").setWidth(90);
		egu.getColumn("daocj").setDefaultValue("0.00");
		egu.getColumn("daocj").setEditor(null);
		egu.getColumn("biaomdj").setWidth(90);
		egu.getColumn("biaomdj").setDefaultValue("0.00");
		egu.getColumn("biaomdj").setEditor(null);
		egu.getColumn("jiakk").setWidth(50);
		egu.getColumn("jiakk").setDefaultValue("0");


		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);// ���÷�ҳ
		egu.setWidth("bodyWidth");
		
		
		
		// *****************************************����Ĭ��ֵ****************************
		// �糧������
		if (jib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where (fuid="
									+ getTreeid() + " or shangjgsid ="
									+ getTreeid() + ") order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (jib == 3) {// ѡ�糧ֻˢ�³��õ糧
			// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select id,mingc from diancxxb where id="+ getTreeid() + " order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		}

		// ���õ糧Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
		// �������ڵ�Ĭ��ֵ,
		egu.getColumn("riq").setDefaultValue(getDate());

		// *************************������*****************************************88
//		 ���ù�Ӧ�̵�������
		// egu.getColumn("gongysb_id").setEditor(new ComboBox());
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		
		//��糧������Ĺ�Ӧ�� 
		//���Ӳ����й�Ӧ�̣��ֹ�Ӧ�̺͵糧����������鲻����Ӧ��
		String GongysSql = 
			"select distinct g.id,g.mingc from vwdianc dc,gongysdcglb gd,gongysb g\n" +
			"where gd.diancxxb_id=dc.id and gd.gongysb_id=g.id "+strwhere+" and g.leix = 1\n" + 
			"union\n" + 
			"select distinct g.id,g.mingc from yuesbjhb n,gongysb g,vwdianc dc\n" + 
			"where n.gongysb_id=g.id and n.diancxxb_id=dc.id "+strwhere+" and g.leix = 1\n" +
			"union\n" + 
			"select distinct g.id,g.mingc from gongysb g where  g.leix = 1 ";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		
//		 ����ú���������
		ComboBox cb_meik=new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cb_meik);
		cb_meik.setEditable(true);
		String cb_meikSql = "select id, mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(cb_meikSql));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
		// ���üƻ��ھ���������
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		String JihkjSql = "select id,mingc from jihkjb order by mingc ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(JihkjSql));
		
//		����Ʒ��������
		ComboBox cb_pinz=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cb_pinz);
		cb_pinz.setEditable(true);
		egu.getColumn("pinzb_id").editor.setAllowBlank(true);//�����������Ƿ�����Ϊ��
		String pinzSql="select id,mingc from pinzb order by id ";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		
//		 ���÷�վ������
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						" where f.diancxxb_id=dc.id  and f.faz_id=c.id and c.leib='��վ' "+strwhere+
						" union\n" +
						" select distinct c.id,c.mingc from yuesbjhb n,vwdianc dc,chezxxb c\n" + 
						" where n.diancxxb_id=dc.id and n.faz_id=c.id " +
						" and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+ ""+
		                 "union\n" + 
		                 "select distinct c.id,c.mingc from chezxxb c  ";  
//		System.out.println(fazSql);
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		// ���õ�վ������
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);

		String daozSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						 " where f.diancxxb_id=dc.id  and f.daoz_id=c.id and c.leib='��վ' "+strwhere+
						 " union\n" +
						 " select distinct c.id,c.mingc from yuesbjhb n,vwdianc dc,chezxxb c\n" + 
						 " where n.diancxxb_id=dc.id and n.daoz_id=c.id " +
						 " and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+ ""+
		                 "union\n" + 
		                 "select distinct c.id,c.mingc from chezxxb c  ";
//		System.out.println(daozSql);
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));

		// �������䷽ʽ������
		ComboBox cb_yunsfs = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(cb_yunsfs);
		cb_daoz.setEditable(true);

		String yunsfsSql = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsSql));
		egu.getColumn("yunsfsb_id").editor.allowBlank=true;
		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("�·�:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		// �趨�������������Զ�ˢ��
//		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���

		// ˢ�°�ť
		StringBuffer rsb2 = new StringBuffer();
		rsb2.append("function (){");
		rsb2.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NIANF').value+'��'+Ext.getDom('YUEF').value+'�µ�����,���Ժ�'",true));
		rsb2.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr2 = new GridButton("ˢ��", rsb2.toString());
		gbr2.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr2);


		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		if (showBtn) {
//			 ���ɰ�ť
			StringBuffer rsb = new StringBuffer();
			rsb.append("function (){document.getElementById('ShengcButton').click();}");
			GridButton gbr = new GridButton("����", rsb.toString());
			gbr.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbr);
//			���ư�ť
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("����ǰ������", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
		
//		2012-07-13 ȡ���ܼƼ�С���е���ɫ
		String BgColor=MainGlobal.getXitxx_item("��ƻ�", "�ܼ�С������ɫ", "0", "#E3E3E3");

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		
		sb.append("var rows=gridDiv_ds.getTotalCount();\n" +
				"for (var i=rows-1;i>=0;i--){\n" + 
				"\t var rec1=gridDiv_ds.getAt(i);\n" + 
				"\t var gonghdw1=rec1.get('GONGYSB_ID');\n" + 
//				"\t var xiaoj=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);//ȡС����\n" + 
				"\t if (gonghdw1==\"-\" ||  gonghdw1==\"С��\"){//С����\n" + 
				"\t\tgridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n" + 
				"\t}\n" + 
				"}\n");
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){\n"
					    + "var mingc=e.record.get('GONGYSB_ID');\n"
						+ "if(mingc=='-'|| mingc=='С��'){e.cancel=true;}//�ж�Ϊ����ʱ�в��ɱ༭\n"
						+ "});\n"
						+ "\n"
						+ "gridDiv_grid.on('afteredit',function(e){\n"
						+ "e.record.set('DAOCJ',Round(eval(e.record.get('CHEBJG')||0)+eval(e.record.get('YUNF')||0)+eval(e.record.get('ZAF')||0),2));\n"
						+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(eval(Round((eval(e.record.get('CHEBJG')||0)/1.17+eval(e.record.get('YUNF')||0)*(1-0.07)+eval(e.record.get('ZAF')||0))*29.271/eval(e.record.get('REZ')||0),2)||0),2));}\n"
//						�����
						+ "if(e.field == 'REZ'){e.record.set('REZ_DK',Round(e.value/0.0041816, 0));}\n"
						+ "if(e.field == 'REZ_DK'){e.record.set('REZ',Round(e.value*0.0041816, 3));}\n"
						+ "\n"
						+ "\tvar rows=gridDiv_ds.getTotalCount();\n"
						+ "\tvar colDianc=2;\n"
						+ "\tvar colGongys=3;\n"
						+ "\tvar colJihl=8;//�ɹ��ƻ���\n"
						+ "\tvar colRez=9;//��ֵ\n"
						+ "\tvar colRez_dk=10;//��ֵ_dk\n"
						+ "\tvar colHuiff=11;//�ӷ���\n"
						+ "\tvar colLiuf=12;//���\n"
						+ "\tvar colChebjg=13;//����۸�\n"
						+ "\tvar colYunf=14;//�˷�\n"
						+ "\tvar colZaf=15;//�ӷ�\n"
						+ "\tvar colDaocj=16;//������\n"
						+ "\tvar colBiaomdj=17;//��ú����\n"
						+ "\n"
						+ "\tvar rowXJ=0;\n"
						+ "\tvar rowZj=1;\n"
						+ "\tvar ArrSumZJ=new Array(20);\n"
						+ "\tvar ArrSumXJ=new Array(20);\n"
						+ "\tvar cgl_xj;//�ɹ���С��\n"
						+ "\n"
						+ "\tfor (var i=0 ;i<20;i++){\n"
						+ "\t\tArrSumXJ[i]=0.0;\n"
						+ "\t\tArrSumZJ[i]=0.0;\n"
						+ "\t}\n"
						+ "\tfor (var i=rows-1;i>=0;i--){\n"
						+ "\t\t var rec1=gridDiv_ds.getAt(i);\n"
						+ "\t\t var gonghdw1=rec1.get('GONGYSB_ID');\n"
						+ "\t\t if (gonghdw1==\"С��\"){//С����\n"
						+ "\t\t \trec1.set('YUEJHCGL',ArrSumXJ[colJihl]);\n"
						+ "\t\t \tif (ArrSumXJ[colJihl]==0.0){\n"
						+ "\t\t \t\trec1.set('REZ',0);\n"
						+ "\t\t \t\trec1.set('REZ_DK',0);\n"
						+ "\t\t \t\trec1.set('HUIFF',0);\n"
						+ "\t\t \t\trec1.set('LIUF',0);\n"
						+ "\t\t \t\trec1.set('CHEBJG',0);\n"
						+ "\t\t \t\trec1.set('YUNF',0);\n"
						+ "\t\t \t\trec1.set('ZAF',0);\n"
						+ "\t\t \t\trec1.set('DAOCJ',0);\n"
						+ "\t\t \t\trec1.set('BIAOMDJ',0);\n"
						+ "\t\t \t}else{\n"
						+ "\t\t \t\trec1.set('REZ',Round(ArrSumXJ[colRez]/ArrSumXJ[colJihl], 3));\n"
						+ "\t\t \t\trec1.set('REZ_DK',Round(ArrSumXJ[colRez_dk]/ArrSumXJ[colJihl], 0));\n"
						+ "\t\t \t\trec1.set('HUIFF',Round(ArrSumXJ[colHuiff]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('LIUF',Round(ArrSumXJ[colLiuf]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('CHEBJG',Round(ArrSumXJ[colChebjg]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('YUNF',Round(ArrSumXJ[colYunf]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('ZAF',Round(ArrSumXJ[colZaf]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('DAOCJ',Round(ArrSumXJ[colDaocj]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t var rez=eval(Round(ArrSumXJ[colRez]/ArrSumXJ[colJihl], 3));\n"
						+ "\t\t var mj=eval(Round(ArrSumXJ[colChebjg]/ArrSumXJ[colJihl], 2)) ;\n"
						+ "\t\t var yj=eval(Round(ArrSumXJ[colYunf]/ArrSumXJ[colJihl], 2));\n"
						+ "\t\t var zf=eval(Round(ArrSumXJ[colZaf]/ArrSumXJ[colJihl], 2));\n"
						+ "\t\t var bmdj=0;\n"
						+ "\t\t if(rez!=0){\n"
						+ "\t\t bmdj=Round((mj+yj -Round((mj-mj/1.17),2)-Round(yj*0.07,2)+zf)*29.271/rez,2);\n"
						+ "\t\t \t}\n"
						+ "\t\t rec1.set('BIAOMDJ',bmdj);\n"

						+ "\t\t \t}\n"
						+ "\t\t \t//�ۼ��ܼ�\n"
						+ "\t\t \tArrSumZJ[colJihl]=ArrSumZJ[colJihl]+ArrSumXJ[colJihl];\n"
						+ "\t\t \tArrSumZJ[colRez]=ArrSumZJ[colRez]+ArrSumXJ[colRez];\n"
						+ "\t\t \tArrSumZJ[colRez_dk]=ArrSumZJ[colRez_dk]+ArrSumXJ[colRez_dk];\n"
						+ "\t\t\tArrSumZJ[colHuiff]=ArrSumZJ[colHuiff]+ArrSumXJ[colHuiff];\n"
						+ "\t\t\tArrSumZJ[colLiuf]=ArrSumZJ[colLiuf]+ArrSumXJ[colLiuf];\n"
						+ "\t\t\tArrSumZJ[colChebjg]=ArrSumZJ[colChebjg]+ArrSumXJ[colChebjg];\n"
						+ "\t\t\tArrSumZJ[colYunf]=ArrSumZJ[colYunf]+ArrSumXJ[colYunf];\n"
						+ "\t\t\tArrSumZJ[colZaf]=ArrSumZJ[colZaf]+ArrSumXJ[colZaf];\n"
						+ "\t\t\tArrSumZJ[colDaocj]=ArrSumZJ[colDaocj]+ArrSumXJ[colDaocj];\n"
						+ "\t\t\tArrSumZJ[colBiaomdj]=ArrSumZJ[colBiaomdj]+ArrSumXJ[colBiaomdj];\n"
						+ "\t\t\t//���С��\n"
						+ "\t\t \tArrSumXJ[colJihl]=0;\n"
						+ "\t\t \tArrSumXJ[colRez]=0;\n"
						+ "\t\t \tArrSumXJ[colRez_dk]=0;\n"
						+ "\t\t \tArrSumXJ[colHuiff]=0;\n"
						+ "\t\t\tArrSumXJ[colLiuf]=0;\n"
						+ "\t\t\tArrSumXJ[colChebjg]=0;\n"
						+ "\t\t\tArrSumXJ[colYunf]=0;\n"
						+ "\t\t\tArrSumXJ[colZaf]=0;\n"
						+ "\t\t\tArrSumXJ[colDaocj]=0;\n"
						+ "\t\t\tArrSumXJ[colBiaomdj]=0;\n"
						+ "\t\t\t gridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n"						
						+ "\t\t }else if ( i==0) {//�ܼ���\n"
						+ "\t\t \trec1.set('YUEJHCGL',ArrSumZJ[colJihl]);\n"
						+ "\t\t \tif (ArrSumZJ[colJihl]==0.0){\n"
						+ "\t\t \t\trec1.set('REZ',0);\n"
						+ "\t\t \t\trec1.set('REZ_DK',0);\n"
						+ "\t\t \t\trec1.set('HUIFF',0);\n"
						+ "\t\t \t\trec1.set('LIUF',0);\n"
						+ "\t\t \t\trec1.set('CHEBJG',0);\n"
						+ "\t\t \t\trec1.set('YUNF',0);\n"
						+ "\t\t \t\trec1.set('ZAF',0);\n"
						+ "\t\t \t\trec1.set('DAOCJ',0);\n"
						+ "\t\t \t\trec1.set('BIAOMDJ',0);\n"
						+ "\t\t \t}else{\n"
						+ "\t\t \t\trec1.set('REZ',Round(ArrSumZJ[colRez]/ArrSumZJ[colJihl], 3));\n"
						+ "\t\t \t\trec1.set('REZ_DK',Round(ArrSumZJ[colRez_dk]/ArrSumZJ[colJihl], 0));\n"
						+ "\t\t \t\trec1.set('HUIFF',Round(ArrSumZJ[colHuiff]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('LIUF',Round(ArrSumZJ[colLiuf]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('CHEBJG',Round(ArrSumZJ[colChebjg]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('YUNF',Round(ArrSumZJ[colYunf]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('ZAF',Round(ArrSumZJ[colZaf]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('DAOCJ',Round(ArrSumZJ[colDaocj]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t var rez=eval(Round(ArrSumZJ[colRez]/ArrSumZJ[colJihl], 3));\n"
						+ "\t\t var mj=eval(Round(ArrSumZJ[colChebjg]/ArrSumZJ[colJihl], 2)) ;\n"
						+ "\t\t var yj=eval(Round(ArrSumZJ[colYunf]/ArrSumZJ[colJihl], 2));\n"
						+ "\t\t var zf=eval(Round(ArrSumZJ[colZaf]/ArrSumZJ[colJihl], 2));\n"
						+ "\t\t var bmdj=0;\n"
						+ "\t\t if(rez!=0){\n"
						+ "\t\t \t	bmdj=Round((mj+yj -Round((mj-mj/1.17),2)-Round(yj*0.07,2)+zf)*29.271/rez,2);\n"
						+ "\t\t \t}\n"
						+ "\t\t rec1.set('BIAOMDJ',bmdj);\n"
						+ "\t\t \t}\n"
						+ "\t\t\t gridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n"						
						+ "\t\t }else{\n"
						+ "\t\t \t//�ۼӵ糧��\n"
						+ "\t\t \tArrSumXJ[colJihl]=ArrSumXJ[colJihl]+eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t \tArrSumXJ[colRez]=ArrSumXJ[colRez]+eval(rec1.get('REZ')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t \tArrSumXJ[colRez_dk]=ArrSumXJ[colRez_dk]+eval(rec1.get('REZ_DK')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t \tArrSumXJ[colHuiff]=ArrSumXJ[colHuiff]+eval(rec1.get('HUIFF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colLiuf]=ArrSumXJ[colLiuf]+eval(rec1.get('LIUF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colChebjg]=ArrSumXJ[colChebjg]+eval(rec1.get('CHEBJG')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colYunf]=ArrSumXJ[colYunf]+eval(rec1.get('YUNF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colZaf]=ArrSumXJ[colZaf]+eval(rec1.get('ZAF')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colDaocj]=ArrSumXJ[colDaocj]+eval(rec1.get('DAOCJ')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colBiaomdj]=ArrSumXJ[colBiaomdj]+eval(rec1.get('BIAOMDJ')||0)*eval(rec1.get('YUEJHCGL')||0);\n"
						+ "\n" + "\t\t }\n" + "\t}\n" + "});");

		// �趨�ϼ��в�����
		sb.append("function gridDiv_save(record){ \n" +
				" var gonghdw1=record.get('GONGYSB_ID');\n"+
				" var mm; \n"+
				" if (gonghdw1=='-'||gonghdw1==''|| gonghdw1=='С��') return 'continue';}\n "
//				" if (gonghdw1.length>6){mm=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);\n}" +
//				" if(mm==''|| mm=='С��') return 'continue';}\n"
				);
		
		egu.addOtherScript(sb.toString());
//		 ---------------ҳ��js�������--------------------------
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

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

//	private long flage = 0;

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
		}
		getSelectData();

	}

	// ���
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// �õ��糧ID
	public String getIDropDownDiancid(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancid = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.id from diancxxb d where d.mingc='"
				+ diancmcId + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancid = rs.getString("id");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

	}

	// �õ��糧����
	public String getIDropDownDiancbm(String diancmc) {
		if (diancmc == null || diancmc.equals("")) {
			diancmc = "1";
		}
		String IDropDownDiancid = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select bianm from diancxxb d where d.mingc='"
				+ diancmc + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancid = rs.getString("bianm");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

	}

	// ������λ����
	public String getIDropDownGonghdwbm(String gonghdwmc) {
		if (gonghdwmc == null || gonghdwmc.equals("")) {
			gonghdwmc = "1";
		}
		String IDropDownGonghdwbm = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "  select bianm from gongysb  where mingc ='"+ gonghdwmc + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownGonghdwbm = rs.getString("bianm");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownGonghdwbm;

	}

	// �õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
		 
		 StringBuffer sql = new StringBuffer();
		sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
}