package com.zhiren.jt.zdt.monthreport.niancgjhb;


/*����:���ܱ�
 * ����:2010-3-24
 * �޸�����:�޸�ҳ���еĿ������������,ȥ�����е�ú���������
 * 
 * 
 * 
 */
/* 
* ʱ�䣺2009-10-10
* ���ߣ� sy
* �޸����ݣ������˵���Ӧ�̡���վ����վ��sql�����Բ�����еģ�ԭ����Ҫ�͵糧��������ϵͳ��δ����
* 		   
*/   
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
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
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Niancgjhb extends BasePage implements PageValidateListener {
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
		visit.getExtGrid1().Save(getChange(), visit);
		setMsg("����ɹ�!");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	private boolean _DeleteButton = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
		if (_DeleteButton) {
			_DeleteButton = false;
			DeleteData();

		}
	}

	public void DeleteData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());

		for (int i = 0; rsl.next(); i++) {
			String id = rsl.getString("id");
			String delSql = "delete from niancgjh where id = " + id + "";
			int flag = con.getDelete(delSql);
			if (flag != -1) {
				setMsg("������ɾ����");
			}
		}

		con.Close();
	}

	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date()) - 1;
		} else {
			intyear = getNianfValue().getId() - 1;
		}

		String str_jizzt = "";

		if (getLeixDropDownValue() != null) {
			str_jizzt = " and y.jizzt=" + getLeixDropDownValue().getId();
		}

		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";

		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";

		}
		String copyData = "select y.id as id,\n"
				+ "      add_months(y.riq,+12) as riq,\n"
				+ "      dc.mingc as diancxxb_id,\n"
				+ "       g.mingc as gongysb_id,\n"
				+ "       j.mingc as jihkjb_id,\n"
				+ "       ch.mingc as faz_id,\n"
				+ "       che.mingc as daoz_id,\n"
				+ "       ys.mingc as yunsfsb_id,\n"
				+ "       y.nianjhcgl as nianjhcgl,\n"
				+ "       y.chebjg as chebjg,\n"
				+ "       y.yunf as yunf,\n"
				+ "       y.zaf as zaf,\n"
				+ "       y.rez as rez,\n"
				+ "       y.jiakk as jiakk,\n"
				+ "       y.jihddsjysl as jihddsjysl\n ,y.jizzt \n"
				+ "  from niancgjh y, gongysb g, chezxxb ch, chezxxb che, diancxxb dc,jihkjb j,yunsfsb ys\n"
				+ " where y.gongysb_id = g.id(+)\n"
				+ "   and y.faz_id = ch.id(+)\n"
				+ "   and y.daoz_id = che.id(+)\n"
				+ "   and y.diancxxb_id = dc.id(+)\n"
				+ "   and y.jihkjb_id = j.id(+)\n"
				+ "   and y.yunsfsb_id = ys.id(+)\n"
				+
				// " and to_char(y.riq,'yyyy-mm') ='" + intyear + "-" +
				// StrMonth+ "' \n"+
				"   and y.riq=to_date('" + intyear
				+ "-01-01','yyyy-mm-dd')   \n" + "    " + str + "  "
				+ str_jizzt +
				// " group by rollup
				// (dc.mingc,(g.mingc,j.mingc,ch.mingc,che.mingc,y.rez,y.jiakk,y.jihddsjysl))\n"
				// +
				// " having grouping (che.mingc)=0 or grouping(dc.mingc)!=0\n" +
				"   order by dc.mingc,g.mingc,j.mingc";

		// System.out.println("����ͬ�ڵ�����:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData);
		while (rslcopy.next()) {

			long yuejhcgl = rslcopy.getLong("nianjhcgl");
			long chebjg = rslcopy.getLong("chebjg");
			long yunf = rslcopy.getLong("yunf");
			long zaf = rslcopy.getLong("zaf");
			String rez = rslcopy.getString("rez");
			String jiakk = rslcopy.getString("jiakk");
			String jihddsjysl = rslcopy.getString("jihddsjysl");
			String jizzt = rslcopy.getString("jizzt");
			Date riq = rslcopy.getDate("riq");
			// int year=DateUtil.getYear(riq);
			// int yue=DateUtil.getMonth(riq);
			// int day=DateUtil.getDay(riq);
			//		
			// String strriq=year+"-"+yue+1+"-"+day;
			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			con
					.getInsert("insert into niancgjh(id,riq,diancxxb_id,gongysb_id,jihkjb_id,faz_id,daoz_id,nianjhcgl,chebjg,yunf,zaf,rez,jiakk,jihddsjysl,yunsfsb_id,jizzt) values("
							+
							// _id+","+"to_date('"+strriq+"','yyyy-mm-dd')"
							_id
							+ ","
							+ "to_date('"
							+ FormatDate(riq)
							+ "','yyyy-mm-dd')"
							+ ",(select id from diancxxb where mingc='"
							+ rslcopy.getString("diancxxb_id")
							+ "'),(select id from gongysb where mingc='"
							+ rslcopy.getString("gongysb_id")
							+ "'),(select id from jihkjb where mingc='"
							+ rslcopy.getString("jihkjb_id")
							+ "'),(select id from chezxxb where mingc='"
							+ rslcopy.getString("faz_id")
							+ "'),(select id from chezxxb where mingc='"
							+ rslcopy.getString("daoz_id")
							+ "'),"
							+ yuejhcgl
							+ ","
							+ chebjg
							+ ","
							+ yunf
							+ ","
							+ zaf
							+ ",'"
							+ rez
							+ "','"
							+ jiakk
							+ "','"
							+ jihddsjysl
							+ "',(select id from yunsfsb where mingc='"
							+ rslcopy.getString("yunsfsb_id")
							+ "'),"
							+ jizzt
							+ ")");

		}
		con.Close();
		getSelectData(null);
		
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		visit.setString3("" + intyear);// ���

		String str_jizzt = "";

		if (getLeixDropDownValue() != null) {
			str_jizzt = " and y.jizzt=" + getLeixDropDownValue().getId();
		}

		int jib = this.getDiancTreeJib();

		StringBuffer strSQL = new StringBuffer();

		StringBuffer strgrouping = new StringBuffer();
		StringBuffer strwhere = new StringBuffer();
		StringBuffer strgroupby = new StringBuffer();
		StringBuffer strhaving = new StringBuffer();
		StringBuffer strorderby = new StringBuffer();

		if (jib == 1) {// ѡ����ʱˢ�³����еĵ糧
			strgrouping
					.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,dc.mingc) as diancxxb_id,\n");
			strgrouping
					.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
			strwhere.append("");
			strgroupby
					.append("group by rollup(dc.fgsmc,dc.mingc,g.mingc,j.mingc,p.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt)\n");
			strhaving
					.append(" having not (grouping(g.mingc) || grouping(y.jizzt)) =1  \n");
			strorderby
					.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
		} else if (jib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			String ranlgs = "select id from diancxxb where shangjgsid= "
					+ this.getTreeid();

			try {
				ResultSet rl = con.getResultSet(ranlgs);
				if (rl.next()) {// ȼ�Ϲ�˾
					strgrouping
							.append("decode(grouping(dc.rlgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.rlgsmc,dc.mingc) as diancxxb_id,\n");
					strgrouping
							.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
					strwhere.append(" and (dc.fgsid=" + this.getTreeid()
							+ " or dc.rlgsid= " + this.getTreeid() + ") \n");
					strgroupby
							.append("group by rollup(dc.rlgsmc,dc.mingc,g.mingc,j.mingc,p.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt)\n");
					strhaving
							.append("having not (grouping(g.mingc) || grouping(y.jizzt)) =1 and not grouping(dc.rlgsmc)=1\n");
					strorderby
							.append("order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
				} else {// �ֹ�˾
					strgrouping
							.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,dc.mingc) as diancxxb_id,\n");
					strgrouping
							.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
					strwhere.append(" and (dc.fgsid=" + this.getTreeid()
							+ " or dc.rlgsid= " + this.getTreeid() + ") \n");
					strgroupby
							.append("group by rollup(dc.fgsmc,dc.mingc,g.mingc,j.mingc,p.mingc,ch.mingc,che.mingc,ys.mingc,y.jizzt)\n");
					strhaving
							.append("  having not (grouping(g.mingc) || grouping(y.jizzt)) =1 and not grouping(dc.fgsmc)=1 \n");
					strorderby
							.append("order by grouping(dc.fgsmc) desc,dc.fgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
				}
				rl.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.Close();
			}
		} else if (jib == 3) {// ѡ�糧ֻˢ�³��õ糧
			strgrouping
					.append("decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc) as diancxxb_id,\n");
			strgrouping
					.append("decode(grouping(dc.mingc)+grouping(g.mingc),2,'-',1,getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),'С��'),g.mingc) as gongysb_id,\n");
			strwhere.append(" and dc.id=").append(this.getTreeid());
			strgroupby
					.append("group by rollup(dc.mingc,g.mingc,j.mingc,ch.mingc,p.mingc,che.mingc,ys.mingc,y.jizzt)\n");
			strhaving
					.append("having not (grouping(g.mingc) || grouping(y.jizzt)) =1 and not grouping(dc.mingc)=1\n");
			strorderby
					.append("order by grouping(dc.mingc) desc,max(dc.xuh),grouping(g.mingc) desc,g.mingc\n");
		}

		strSQL.append("select \n");
		strSQL.append("max(y.id) as id,max(y.riq) as riq,\n");
		strSQL.append(strgrouping);
		strSQL
				.append("nvl(j.mingc,'-') as jihkjb_id,nvl(p.mingc,'-') as pinzb_id,nvl(ch.mingc,'-') as faz_id,nvl(che.mingc,'-') as daoz_id,nvl(ys.mingc,'') as yunsfsb_id,\n");
		strSQL.append("sum(y.nianjhcgl) as nianjhcgl,");
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,Round(sum(y.rez*y.nianjhcgl)/sum(y.nianjhcgl),2)) as rez,\n");
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,round(sum(y.rez*y.nianjhcgl)/sum(y.nianjhcgl)*1000/4.1816,2)) as rez_dk,\n");// ��ֵ_��
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,Round(sum(y.huiff*y.nianjhcgl)/sum(y.nianjhcgl),2)) as huiff,\n");// �ӷ���
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,Round(sum(y.liuf*y.nianjhcgl)/sum(y.nianjhcgl),2)) as liuf,\n");// ���
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,Round(sum(y.chebjg*y.nianjhcgl)/sum(y.nianjhcgl),2)) as chebjg,\n");
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,Round(sum(y.yunf*y.nianjhcgl)/sum(y.nianjhcgl),2)) as yunf,\n");
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,Round(sum(y.zaf*y.nianjhcgl)/sum(y.nianjhcgl),2)) as zaf,\n");
		strSQL
				.append("decode(sum(y.nianjhcgl),0,0,Round(sum((y.chebjg+y.yunf+y.zaf)*y.nianjhcgl)/sum(y.nianjhcgl),2)) as daocj,\n");
		// strSQL.append("decode(sum(y.yuejhcgl),0,0,Round(sum(y.biaomdj*y.yuejhcgl)/sum(y.yuejhcgl),2))
		// as biaomdj,\n");

		strSQL
				.append("Round(decode(sum(y.rez*y.nianjhcgl),0,0,Round((sum((y.chebjg/(1+0.17)+y.yunf*(1-0.07)+y.zaf)*y.nianjhcgl)/sum(y.nianjhcgl)),2)*29.271/Round((sum(y.rez*y.nianjhcgl)/sum(y.nianjhcgl)),2)),2) as biaomdj,\n");

		strSQL
				.append("max(y.jiakk) as jiakk,max(y.jihddsjysl) as jihddsjysl,y.jizzt\n");
		strSQL
				.append(" from niancgjh y, gongysb g, chezxxb ch, chezxxb che, vwdianc dc,jihkjb j,pinzb p,yunsfsb ys\n");
		strSQL
				.append("  where y.gongysb_id = g.id(+) and y.pinzb_id = p.id(+) \n");
		strSQL
				.append(" and y.faz_id = ch.id(+) and y.daoz_id = che.id(+) and y.diancxxb_id = dc.id(+) \n");
		strSQL.append(" and y.jihkjb_id = j.id(+) and y.yunsfsb_id = ys.id(+)");
		strSQL.append(" and y.riq=to_date('" + intyear
				+ "-01-01','yyyy-mm-dd') \n");
		strSQL.append(strwhere).append(str_jizzt);
		strSQL.append(strgroupby);
		strSQL.append(strhaving);
		strSQL.append(strorderby);

		/*
		 * if(rsl==null){ String chaxun = "select y.id as id,\n" + " y.riq as
		 * riq,\n" + " dc.mingc as diancxxb_id,\n" + " g.mingc as gongysb_id,\n" + "
		 * j.mingc as jihkjb_id,\n" + " p.mingc as pinzb_id,\n"+ " ch.mingc as
		 * faz_id,\n" + " che.mingc as daoz_id,\n" + " ys.mingc as
		 * yunsfsb_id,\n" + " y.nianjhcgl as nianjhcgl,\n" + " y.rez as rez,\n" + "
		 * y.huiff as huiff,\n" + " y.liuf as liuf,\n" + " y.chebjg as
		 * chebjg,\n" + " y.yunf as yunf,\n" + " y.zaf as zaf,\n" + " y.daocj as
		 * daocj,\n" + " y.jiakk as jiakk,\n" + " y.jihddsjysl as jihddsjysl\n
		 * ,y.jizzt \n" + " from niancgjh y, gongysb g, chezxxb ch, chezxxb che,
		 * diancxxb dc,jihkjb j,pinzb p,yunsfsb ys\n" + " where y.gongysb_id =
		 * g.id(+)\n" + " and y.faz_id = ch.id(+)\n" + " and y.daoz_id =
		 * che.id(+)\n" + " and y.diancxxb_id = dc.id(+)\n" + " and y.jihkjb_id =
		 * j.id(+)\n" + " and y.pinzb_id = p.id(+)\n" + " and y.yunsfsb_id =
		 * ys.id(+)\n" + " and to_char(y.riq,'yyyy-mm') ='" + intyear + "-01'
		 * \n"+ str_jizzt + " "+str+" "+ " order by y.id";
		 * //System.out.println(chaxun); rsl = con.getResultSetList(chaxun); }
		 */
		rsl = con.getResultSetList(strSQL.toString());
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("niancgjh");
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		// ���ö�ѡ��
//		egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
//		egu.getColumn(1).setAlign("middle");
		egu.setDefaultsortable(false);//����ÿ�б�ͷ����󲻿�������
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("gongysb_id").setHeader("�������");
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("faz_id").setHeader("��վ");
		egu.getColumn("daoz_id").setHeader("��վ");
		egu.getColumn("yunsfsb_id").setHeader("����<br>��ʽ");
		egu.getColumn("nianjhcgl").setHeader("��ƻ���<br>����(��)");
		egu.getColumn("rez").setHeader("��ֵ<br>(MJ/Kg)");
		egu.getColumn("rez_dk").setHeader("��ֵ<br>(kcal/Kg)");
		egu.getColumn("rez_dk").setDefaultValue("0");
		egu.getColumn("rez_dk").setEditor(null);
		egu.getColumn("rez_dk").setUpdate(false);
		egu.getColumn("huiff").setHeader("�ӷ���<br>%");
		egu.getColumn("liuf").setHeader("���<br>%");
		egu.getColumn("chebjg").setHeader("�����<br>(Ԫ/��)");
		egu.getColumn("yunf").setHeader("�˷�<br>(Ԫ/��)");
		egu.getColumn("zaf").setHeader("�ӷ�<br>(Ԫ/��)");
		egu.getColumn("daocj").setHeader("������<br>(Ԫ/��)");
		egu.getColumn("biaomdj").setHeader("����˰��ú<br>����(Ԫ/��)");
		egu.getColumn("biaomdj").setDefaultValue("0");
		egu.getColumn("jiakk").setHeader("�ӿۿ�");
		egu.getColumn("jiakk").setHidden(true);
		egu.getColumn("jihddsjysl").setHeader("�ƻ�����ʱ<br>��������");
		egu.getColumn("jihddsjysl").setHidden(true);

		egu.getColumn("jizzt").setHeader("����״̬");
		egu.getColumn("jizzt").setHidden(true);

		((NumberField) egu.getColumn("rez").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("huiff").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("liuf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("chebjg").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("yunf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("zaf").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("daocj").editor).setDecimalPrecision(2);
		((NumberField) egu.getColumn("biaomdj").editor).setDecimalPrecision(2);

		// ���ò��ɱ༭����ɫ
		// egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext';
		// return value;}");
		
		// �趨�г�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("faz_id").setWidth(50);
		egu.getColumn("daoz_id").setWidth(50);
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("nianjhcgl").setWidth(60);
		egu.getColumn("rez").setWidth(60);
		egu.getColumn("rez_dk").setWidth(60);
		egu.getColumn("huiff").setWidth(60);
		egu.getColumn("liuf").setWidth(40);
		egu.getColumn("chebjg").setWidth(60);
		egu.getColumn("yunf").setWidth(40);
		egu.getColumn("zaf").setWidth(40);
		egu.getColumn("daocj").setWidth(60);
		egu.getColumn("jiakk").setWidth(60);
		egu.getColumn("jihddsjysl").setWidth(60);
		egu.getColumn("jizzt").setWidth(60);
		egu.getColumn("jizzt").setDefaultValue(
				"" + getLeixDropDownValue().getId());

		egu.getColumn("biaomdj").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);// ���÷�ҳ
		// egu.setWidth(1000);//����ҳ��Ŀ��,������������ʱ��ʾ������
		egu.setWidth("bodyWidth");

		// *****************************************����Ĭ��ֵ****************************
		// �糧������
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
			// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu
					.getColumn("diancxxb_id")
					.setComboEditor(
							egu.gridId,
							new IDropDownModel(
									"select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib2 == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
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
		} else if (treejib2 == 3) {// ѡ�糧ֻˢ�³��õ糧
			// egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(
					egu.gridId,
					new IDropDownModel(
							"select id,mingc from diancxxb where id="
									+ getTreeid() + " order by mingc"));
			ResultSetList r = con
					.getResultSetList("select id,mingc from diancxxb where id="
							+ getTreeid() + " order by mingc");
			String mingc = "";
			if (r.next()) {
				mingc = r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);

		}

		// ���õ糧Ĭ�ϵ�վ
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
		// �������ڵ�Ĭ��ֵ,
		egu.getColumn("riq").setDefaultValue(intyear + "-01-01");

//		 *************************������*****************************************88
		// ���ù�Ӧ�̵�������
		// egu.getColumn("gongysb_id").setEditor(new ComboBox());
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		
		//��糧������Ĺ�Ӧ�� 
		String GongysSql = 
			"select distinct g.id,g.mingc from vwdianc dc,gongysdcglb gd,gongysb g\n" +
			"where gd.diancxxb_id=dc.id and gd.gongysb_id=g.id "+strwhere+" and g.leix=1\n" + 
			"union\n" + 
			"select distinct g.id,g.mingc from niancgjh n,gongysb g,vwdianc dc\n" + 
			"where n.gongysb_id=g.id and n.diancxxb_id=dc.id "+strwhere+" and g.leix=1\n" + 
			"union\n" +
			"select distinct g.id,g.mingc from  gongysb g  where g.leix=1";
		//System.out.println(GongysSql);
		 
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		// ���üƻ��ھ���������
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		String JihkjSql = "select id,mingc from jihkjb order by mingc ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(JihkjSql));

		// ����Ʒ��������
		ComboBox cb_pinz = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cb_pinz);
		cb_pinz.setEditable(true);
		egu.getColumn("pinzb_id").editor.setAllowBlank(true);// �����������Ƿ�����Ϊ��
		String pinzSql = "select id,mingc from pinzb order by id ";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		egu.getColumn("pinzb_id").setDefaultValue("ԭú");

		// ���÷�վ������
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						" where f.diancxxb_id=dc.id  and f.faz_id=c.id and c.leib='��վ' "+strwhere+
						" union\n" +
						" select distinct c.id,c.mingc from niancgjh n,vwdianc dc,chezxxb c\n" + 
						" where n.diancxxb_id=dc.id and n.faz_id=c.id " +
						" and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+"\n" + 
						"union\n" +
						"select distinct c.id,c.mingc from  chezxxb c ";
		//System.out.println(fazSql);
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		// ���õ�վ������
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);

		String daozSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
						 " where f.diancxxb_id=dc.id  and f.daoz_id=c.id and c.leib='��վ' "+strwhere+
						 " union\n" +
						 " select distinct c.id,c.mingc from niancgjh n,vwdianc dc,chezxxb c\n" + 
						 " where n.diancxxb_id=dc.id and n.daoz_id=c.id " +
						 " and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+"\n" + 
							"union\n" +
							"select distinct c.id,c.mingc from  chezxxb c ";
		//System.out.println(daozSql);
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));

		// �������䷽ʽ������
		ComboBox cb_yunsfs = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(cb_yunsfs);
		cb_yunsfs.setEditable(true);

		String yunsfsSql = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsSql));

		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		// egu.addTbarText("�·�:");
		// ComboBox comb2=new ComboBox();
		// comb2.setTransform("YUEF");
		// comb2.setId("YUEF");//���Զ�ˢ�°�
		// comb2.setLazyRender(true);//��̬��
		// comb2.setWidth(50);
		// egu.addToolbarItem(comb2.getScript());
		// egu.addTbarText("-");//���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		// �趨�������������Զ�ˢ��
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("����״̬:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("LeixDropDown");
		comb2.setId("LeixDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(80);
		egu.addToolbarItem(comb2.getScript());

		egu
				.addOtherScript("LeixDropDown.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���

//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		GridButton gb_insert = new GridButton(GridButton.ButtonType_Insert_specified_line,
				egu.gridId, egu.getGridColumns(), null);
		gb_insert.setId("INSERT");
		egu.addTbarBtn(gb_insert);
		
		egu.addToolbarButton("ɾ��", GridButton.ButtonType_SubmitSel,
				"DeleteButton", "", SysConstant.Btn_Icon_Delete);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		 ���淽��
		GridButton gb_save = new GridButton(GridButton.ButtonType_Save,
				egu.gridId, egu.getGridColumns(), "SaveButton");
		gb_save.setId("SAVE");
		egu.addTbarBtn(gb_save);

		if (showBtn) {
			egu
					.addToolbarItem("{"
							+ new GridButton("����ͬ�ڼƻ�",
									"function(){document.getElementById('CopyButton').click();}")
									.getScript() + "}");
		}
		egu.addTbarText("->");
		egu.addTbarText("<font color=\"#EE0000\">��λ:�֡�Ԫ/�֡�ǧ��/ǧ��</font>");
		
//		2009-09-24 ȡ���ܼƼ�С���е���ɫ
		String BgColor="";
		String sql_color="select zhi from xitxxb where mingc ='�ܼ�С������ɫ' and zhuangt=1 ";
		
		rsl = con.getResultSetList(sql_color);
		
		if (rsl.next()){
			BgColor=rsl.getString("zhi");
		}
		rsl.close();

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		
		sb.append("var rows=gridDiv_ds.getTotalCount();\n" +
				"for (var i=rows-1;i>=0;i--){\n" + 
				"\t var rec1=gridDiv_ds.getAt(i);\n" + 
				"\t var gonghdw1=rec1.get('GONGYSB_ID');\n" + 
				"\t var xiaoj=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);//ȡС����\n" + 
				"\t if (gonghdw1==\"-\" ||  xiaoj==\"С��\"){//С����\n" + 
				"\t\tgridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n" + 
				"\t}\n" + 
				"}\n");
		
		sb
				.append("gridDiv_grid.on('beforeedit',function(e){\n"
						+ "if(e.record.get('GONGYSB_ID')=='-'){e.cancel=true;}//�ж�Ϊ����ʱ�в��ɱ༭\n"
						+ "var mingc=e.record.get('GONGYSB_ID');\n"
						+ "var le=mingc.length;\n"
						+ "var xiaoj=mingc.substring(le-6,le-4);//ȡС����\n"
						+ "if(xiaoj=='С��'){e.cancel=true;}//�ж�С��ʱ�в��ɱ༭\n"
						+ "});\n"
						+ "\n"
						+ "gridDiv_grid.on('afteredit',function(e){\n"
						+ "e.record.set('DAOCJ',Round(eval(e.record.get('CHEBJG')||0)+eval(e.record.get('YUNF')||0)+eval(e.record.get('ZAF')||0),2));\n"
						+ "if(e.field == 'REZ'){e.record.set('REZ_DK',Round(e.value/0.0041816,2));}\n"
						+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(eval(Round((eval(e.record.get('CHEBJG')||0)/1.17+eval(e.record.get('YUNF')||0)*(1-0.07)+eval(e.record.get('ZAF')||0))*29.271/eval(e.record.get('REZ')||0),2)||0),2));}\n"
						+ "\n"
						// +
						// "//if(e.record.get('ZHUANGT')=='1'){document.all.item('SaveButton').disabled=true;}\n"
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
						+ "\t\t var xiaoj=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);//ȡС����\n"
						+ "\t\t if (xiaoj==\"С��\"){//С����\n"
						+ "\t\t \trec1.set('NIANJHCGL',ArrSumXJ[colJihl]);\n"
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
						+ "\t\t \t\trec1.set('REZ',Round(ArrSumXJ[colRez]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('REZ_DK',Round(ArrSumXJ[colRez_dk]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('HUIFF',Round(ArrSumXJ[colHuiff]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('LIUF',Round(ArrSumXJ[colLiuf]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('CHEBJG',Round(ArrSumXJ[colChebjg]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('YUNF',Round(ArrSumXJ[colYunf]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('ZAF',Round(ArrSumXJ[colZaf]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('DAOCJ',Round(ArrSumXJ[colDaocj]/ArrSumXJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('BIAOMDJ',Round(ArrSumXJ[colBiaomdj]/ArrSumXJ[colJihl],2));\n"
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
						+ "\t\t \trec1.set('NIANJHCGL',ArrSumZJ[colJihl]);\n"
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
						+ "\t\t \t\trec1.set('REZ',Round(ArrSumZJ[colRez]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('REZ_DK',Round(ArrSumZJ[colRez_dk]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('HUIFF',Round(ArrSumZJ[colHuiff]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('LIUF',Round(ArrSumZJ[colLiuf]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('CHEBJG',Round(ArrSumZJ[colChebjg]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('YUNF',Round(ArrSumZJ[colYunf]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('ZAF',Round(ArrSumZJ[colZaf]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('DAOCJ',Round(ArrSumZJ[colDaocj]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t\trec1.set('BIAOMDJ',Round(ArrSumZJ[colBiaomdj]/ArrSumZJ[colJihl],2));\n"
						+ "\t\t \t}\n"
						+ "\t\t\t gridDiv_grid.getView().getRow(i).style.backgroundColor='"+BgColor+"';\n"						
						+ "\t\t }else{\n"
						+ "\t\t \t//�ۼӵ糧��\n"
						+ "\t\t \tArrSumXJ[colJihl]=ArrSumXJ[colJihl]+eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t \tArrSumXJ[colRez]=ArrSumXJ[colRez]+eval(rec1.get('REZ')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t \tArrSumXJ[colRez_dk]=ArrSumXJ[colRez_dk]+eval(rec1.get('REZ_DK')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t \tArrSumXJ[colHuiff]=ArrSumXJ[colHuiff]+eval(rec1.get('HUIFF')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colLiuf]=ArrSumXJ[colLiuf]+eval(rec1.get('LIUF')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colChebjg]=ArrSumXJ[colChebjg]+eval(rec1.get('CHEBJG')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colYunf]=ArrSumXJ[colYunf]+eval(rec1.get('YUNF')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colZaf]=ArrSumXJ[colZaf]+eval(rec1.get('ZAF')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colDaocj]=ArrSumXJ[colDaocj]+eval(rec1.get('DAOCJ')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\t\t\tArrSumXJ[colBiaomdj]=ArrSumXJ[colBiaomdj]+eval(rec1.get('BIAOMDJ')||0)*eval(rec1.get('NIANJHCGL')||0);\n"
						+ "\n" + "\t\t }\n" + "\t}\n" + "});");

		// �趨�ϼ��в�����
		sb
				.append("function gridDiv_save(record){ \n"
						+ " var gonghdw1=record.get('GONGYSB_ID');\n"
						+ " var mm; \n"
						+ " if (gonghdw1=='-') return 'continue';\n "
						+ " if (gonghdw1.length>6){mm=gonghdw1.substring(gonghdw1.length-6,gonghdw1.length-4);\n}"
						+ " if(mm==''|| mm=='С��') return 'continue';}\n");

		egu.addOtherScript(sb.toString());
		// ---------------ҳ��js�������--------------------------
		
//		�ж���������Ƿ��ظ���js
		String sb_str=
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"rec=gridDiv_ds.getAt(e.row);\n" + 
			"var falge=0;\n" + 
			"var diancxxb_id=rec.get('DIANCXXB_ID');\n"+
			"var gonghdw=rec.get('GONGYSB_ID');\n" + 
			"var pinzb=rec.get('PINZB_ID');\n" + 
			"var yunsfsb=rec.get('YUNSFSB_ID');\n" + 
			"var faz_id=rec.get('FAZ_ID');\n"+
			"var daoz_id=rec.get('DAOZ_ID');\n"+
			"var rows=gridDiv_ds.getTotalCount();\n" + 
			"for (var i=0;i<rows;i++){\n" + 
			"var rec1=gridDiv_ds.getAt(i);\n" + 
			"var diancxxb_id1=rec1.get('DIANCXXB_ID');\n"+
			"var gonghdw1=rec1.get('GONGYSB_ID');\n" + 
			"var pinzb1=rec1.get('PINZB_ID');\n" + 
			"var yunsfsb1=rec1.get('YUNSFSB_ID');\n" + 
			"var faz_id1=rec1.get('FAZ_ID');\n"+
			"var daoz_id1=rec1.get('DAOZ_ID');\n"+
			"if(i==e.row){\n" + 
			"continue;\n" + 
			"}else if (gonghdw==gonghdw1 && diancxxb_id==diancxxb_id1 && pinzb==pinzb1 && yunsfsb==yunsfsb1 && faz_id==faz_id1 && daoz_id==daoz_id1){\n" + 
			"falge=1;\n Ext.MessageBox.alert('��ʾ��Ϣ',\"��¼����������\"+(i+1)+\"������������ȫ��ͬ�������޸����ݣ�\");\n break;\n" + 
			"}else{\n" + 
			" continue;\n" + 
			"}\n" + 
			"}\n" + 
			"if(falge==1){\n" +
			"Ext.getCmp(\"SAVE\").setDisabled(true) ;\n" + 
			"Ext.getCmp(\"INSERT\").setDisabled(true) ;\n" +
			"}else{\n" + 
			"Ext.getCmp(\"SAVE\").setDisabled(false) ;\n" + 
			"Ext.getCmp(\"INSERT\").setDisabled(false) ;\n" +
			"}"+
			"});";

		StringBuffer sb2 = new StringBuffer(sb_str);
		egu.addOtherScript(sb2.toString());
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
			this.setMsg(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setString3(null);

		}
		getSelectData(null);

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
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
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

	private String treeid;

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
		// System.out.println("jib:" + DiancTreeJib);
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

	// ����
	public IDropDownBean getLeixDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getLeixDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeixDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeixDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeixDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeixDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeixDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "���ۻ���"));
		list.add(new IDropDownBean(1, "��������"));
		// list.add(new IDropDownBean(3, "���̱�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(list));
		return;
	}
}
