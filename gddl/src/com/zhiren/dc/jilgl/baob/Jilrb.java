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
 * ����:tzf
 * ʱ��:2010-01-25
 * �޸�����:����������˾Ҫ��ӯ���������໥���㣬���򱨱����治ƽ���ò������ơ�
 */

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-20 17��24
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-06-18
 * �������������糧����ʼ��BUG��
 */
public class Jilrb extends BasePage {

	/*
	 * ʱ��:2009-03-19 ����:ͯ�Ҹ� ����:�Թ����ձ�����ӯ���ֶΣ������䷽ʽ������ʱ��Ҫ��xitxxb�в��Ҷ�Ӧ�����С���㱣��λ����
	 * �������ӵ糧�ֶΣ�����ͳ�� ��Ӧ�����ݿ�����˵���Ѿ���ʵ��������鿴
	 */
	private static final int RPTTYPE_GH_ALL = 1;

	private static final int RPTTYPE_GH_HUOY = 2;

	private static final int RPTTYPE_GH_QIY = 3;

	private static final int RPTTYPE_SB_ALL = 4;

	private static final int RPTTYPE_SB_HUOY = 5;

	private static final int RPTTYPE_SB_QIY = 6;

	private static final String ROUNDPOINT_QIY = "�����ձ�С��λ"; // ϵͳ��Ϣ���ж�Ӧ��С���㱣��������������������Ч��

	private static final String ROUNDPOINT_HUOY = "���ձ�С��λ"; // ϵͳ��Ϣ���ж�Ӧ��С���㱣���������Ի���Ч��

	/**
	 * ����:ͯ�Ҹ�
	 * ʱ��:2009-4-3
	 * ����:��������ʱ,���ӱ���ؼ���
	 */
	private static final String BAOBPZB_GUANJZ = "GUOHRB_GJZ";// baobpzb�ж�Ӧ�Ĺؼ���
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
//�����Ʊ���Ĭ�ϵ�ǰ�û�
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
	
	private String getMingc(String id) { // ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����
		JDBCcon con = new JDBCcon();
		String mingc = null;
		String sql = "select mingc from diancxxb where id=" + id;

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = rsl.getString("mingc");

		}
		rsl.close();
		return mingc;
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

	// ��ȡ��ص�SQL
	/*
	 * ������ú����(laimsl)�ֶ� �������趨������Լ �޸�ʱ�䣺2008-12-04 �޸��ˣ�����
	 */
	public StringBuffer getBaseSql() {
		Visit visit = (Visit) this.getPage().getVisit();
		// �˴�Ӧ�ø���ʲô����ѯ��Ӧ��zhi��
		String roundPoint_QIY_sql = "select r.zhi from xitxxb r where r.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ " and r.mingc='"
				+ ROUNDPOINT_QIY
				+ "' and r.zhuangt=1";
		
//		��ȡϵͳ��Ϣ����"���ձ�С��λ"��С��λ��.
		String roundPoint_HUOY_sql = "select r.zhi from xitxxb r where r.diancxxb_id="
			+ visit.getDiancxxb_id()
			+ " and r.mingc='"
			+ ROUNDPOINT_HUOY
			+ "' and r.zhuangt=1";
		
		String huoh_qic=" select * from xitxxb where mingc='���������ձ�Ϊ�ᳵʱ��' and zhi='��' and zhuangt=1 and leib='����' and diancxxb_id="+visit.getDiancxxb_id();
		
		JDBCcon con=new JDBCcon();
		
		ResultSetList rsl=con.getResultSetList(huoh_qic);
		
		String shijname="zhongcsj";
		if(rsl.next()){
			shijname="qingcsj";
		}
		
		String tongjrq=" select * from xitxxb where mingc='��������ͳ������'  and zhuangt=1 and leib='����' and diancxxb_id="+visit.getDiancxxb_id();
		rsl=con.getResultSetList(tongjrq);
		
		String strTongjrq="daohrq";
		
		if(rsl.next()){
			strTongjrq=rsl.getString("zhi");
		}
		
		
		String xhjs_str=" select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
		rsl=con.getResultSetList(xhjs_str);
		
		String yuns_huoc_all=" sum(round_new(f.yuns,"+visit.getShuldec()+")) yuns,\n";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
		
		String yuns_qic=" sum(round_new(f.yuns, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  ))  yuns,\n";//���������� ������ʽ
	
		if(rsl.next()){
			
			 yuns_huoc_all=" sum(round_new(f.yingk,"+visit.getShuldec()+")) + sum(round_new(f.biaoz,"+visit.getShuldec()+")) " +
			"- sum(round_new(f.jingz,"+visit.getShuldec()+")) yuns,\n ";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
	
			 yuns_qic=" sum(round_new(f.yingk, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  )) + " +
			" sum(round_new(f.biaoz, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  ))  -" +
					" sum(round_new(f.jingz, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  ))  yuns,\n";//���������� ������ʽ
	
		}
		
		rsl.close();
		con.Close();
		
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						"select dianc, fahdw, meikdw, faz, pinz, laimsl, jingz, biaoz,  \n")
				.append(" yingk, yuns, zongkd, ches from \n");

		if (getTreeid() != null && !"".equals(getTreeid())
				&& !"0".equals(getTreeid())) {

			sb
					.append("(  select decode(grouping(d.fgsmc)+grouping(d.mingc)+grouping(g.mingc)+grouping(m.mingc),4,'�ܼ�', 3,decode(grouping(d.mingc)+grouping(m.mingc)+grouping(d.fgsmc),3,'"
							+ this.getMingc(this.getTreeid())
							+ "',d.mingc),d.mingc) as dianc,\n");

		} else {
			sb
					.append("(  select decode(grouping(d.fgsmc)+grouping(d.mingc)+grouping(g.mingc)+grouping(m.mingc),4,'�ܼ�', 3,decode(grouping(d.mingc)+grouping(m.mingc)+grouping(d.fgsmc),3,'"
							+ visit.getDiancxxb_id()
							+ "',d.mingc),d.mingc) as dianc,\n");
		}

		sb
				.append("decode(grouping(g.mingc)+grouping(m.mingc)-grouping(d.mingc),2,'�ϼ�',g.mingc) as fahdw, decode(grouping(m.mingc)-grouping(g.mingc)-grouping(d.mingc),1,'С��',m.mingc) as meikdw,z.mingc faz,p.mingc pinz, \n");

		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:

			sb.append(
					"sum(round_new(f.laimsl," + visit.getShuldec()
							+ ")) laimsl, \n ").append(
					"sum(round_new(f.jingz," + visit.getShuldec()
							+ ")) jingz, \n ").append(
					"sum(round_new(f.biaoz," + visit.getShuldec()
							+ ")) biaoz, \n")
			// .append("sum(round_new(f.yingd,"+visit.getShuldec()+")) yingd,
			// \n")
					// .append("sum(round_new(f.yingd -
					// f.yingk,"+visit.getShuldec()+")) kuid, \n")
					.append(
							"sum(round_new(f.yingk," + visit.getShuldec()
									+ ")) yingk, \n").append(
											yuns_huoc_all).append(
							"sum(round_new(f.zongkd," + visit.getShuldec()
									+ ")) zongkd, \n").append(
							"sum(c.ches) ches \n");
			;

			sb
					.append("from fahb f, (select distinct fahb_id,count(id) ches from chepb where zhongcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and zhongcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 group by fahb_id) c,gongysb g, vwdianc d, meikxxb m, chezxxb z, pinzb p\n");

			sb.append(" where 1=1 ");
			sb.append(" and f.id = c.fahb_id ");
			break;
		case RPTTYPE_GH_HUOY:

			sb.append(
					"sum(round_new(f.laimsl," + visit.getShuldec()
							+ ")) laimsl, \n ").append(
					"sum(round_new(f.jingz," + visit.getShuldec()
							+ ")) jingz, \n ").append(
					"sum(round_new(f.biaoz," + visit.getShuldec()
							+ ")) biaoz, \n")
			// .append("sum(round_new(f.yingd,"+visit.getShuldec()+")) yingd,
			// \n")
					// .append("sum(round_new(f.yingd -
					// f.yingk,"+visit.getShuldec()+")) kuid, \n")
					.append(
							"sum(round_new(f.yingk," + visit.getShuldec()
									+ ")) yingk, \n").append(
											yuns_huoc_all).append(
							"sum(round_new(f.zongkd," + visit.getShuldec()
									+ ")) zongkd, \n").append(
							"sum(c.ches) ches \n");

			sb
					.append("from fahb f, (select distinct fahb_id,count(id) ches from chepb where zhongcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and zhongcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 group by fahb_id) c, gongysb g, vwdianc d, meikxxb m, chezxxb z, pinzb p \n");
			sb.append("where  f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
			sb.append(" and f.id = c.fahb_id ");
			break;
		case RPTTYPE_GH_QIY:
			// �������䷽ʽҪ����ϵͳ��Ϣ���ֵ������С����ı���λ��

			sb.append(
					"sum(round_new(f.laimsl,nvl((" + roundPoint_QIY_sql + "),"
							+ visit.getShuldec() + "))) laimsl, \n ").append(
					"sum(round_new(f.jingz,nvl((" + roundPoint_QIY_sql + "),"
							+ visit.getShuldec() + "))) jingz, \n ").append(
					"sum(round_new(f.biaoz,nvl((" + roundPoint_QIY_sql + "),"
							+ visit.getShuldec() + "))) biaoz, \n")
			// .append("sum(round_new(f.yingd,nvl(("+roundPoint_QIY_sql+"),"+visit.getShuldec()+")))
			// yingd, \n")
					// .append("sum(round_new(f.yingd -
					// f.yingk,nvl(("+roundPoint_QIY_sql+"),"+visit.getShuldec()+")))
					// kuid, \n")
					.append(
							"sum(round_new(f.yingk,nvl((" + roundPoint_QIY_sql
									+ ")," + visit.getShuldec()
									+ "))) yingk, \n").append(
											yuns_qic).append(
							"sum(round_new(f.zongkd,nvl((" + roundPoint_QIY_sql
									+ ")," + visit.getShuldec()
									+ "))) zongkd, \n").append(
							"sum(c.ches) ches \n");

			sb
					.append("from fahb f,(select distinct fahb_id,count(id) ches from chepb where "+shijname+">=to_date('"+this.getRiq()+"','yyyy-mm-dd') and "+shijname+"<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 group by fahb_id) c,gongysb g, vwdianc d, meikxxb m, chezxxb z, pinzb p\n");
			sb.append("where  f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY);
			sb.append(" and f.id = c.fahb_id ");
			break;
		case RPTTYPE_SB_ALL:

			sb.append(
					"sum(round_new(f.laimsl," + visit.getShuldec()
							+ ")) laimsl, \n ").append(
					"sum(round_new(f.jingz," + visit.getShuldec()
							+ ")) jingz, \n ").append(
					"sum(round_new(f.biaoz," + visit.getShuldec()
							+ ")) biaoz, \n")
			// .append("sum(round_new(f.yingd,"+visit.getShuldec()+")) yingd,
			// \n")
					// .append("sum(round_new(f.yingd -
					// f.yingk,"+visit.getShuldec()+")) kuid, \n")
					.append(
							"sum(round_new(f.yingk," + visit.getShuldec()
									+ ")) yingk, \n").append(
											yuns_huoc_all).append(
							"sum(round_new(f.zongkd," + visit.getShuldec()
									+ ")) zongkd, \n").append(
							"sum(f.ches) ches \n");

			sb
					.append("from fahb f, vwdianc d, gongysb g, meikxxb m, chezxxb z, pinzb p \n");
			sb.append("where f." + strTongjrq + " = ").append(
					DateUtil.FormatOracleDate(getRiq())).append(" \n");
			break;
		case RPTTYPE_SB_HUOY:
//
//			 �����䷽ʽҪ����ϵͳ��Ϣ���ֵ������С����ı���λ��
			
			
			sb.append(
					"sum(round_new(f.laimsl,nvl((" + roundPoint_HUOY_sql + "),"
					+ visit.getShuldec() + "))) laimsl, \n ").append(
			"sum(round_new(f.jingz,nvl((" + roundPoint_HUOY_sql + "),"
					+ visit.getShuldec() + "))) jingz, \n ").append(
			"sum(round_new(f.biaoz,nvl((" + roundPoint_HUOY_sql + "),"
					+ visit.getShuldec() + "))) biaoz, \n")
			// .append("sum(round_new(f.yingd,"+visit.getShuldec()+")) yingd,
			// \n")
					// .append("sum(round_new(f.yingd -
					// f.yingk,"+visit.getShuldec()+")) kuid, \n")
					.append(
							"sum(round_new(f.yingk,nvl((" + roundPoint_HUOY_sql
							+ ")," + visit.getShuldec()
							+ "))) yingk, \n").append(
									yuns_qic).append(
					"sum(round_new(f.zongkd,nvl((" + roundPoint_HUOY_sql
							+ ")," + visit.getShuldec()
							+ "))) zongkd, \n").append(
					"sum(f.ches) ches \n");

			sb
					.append("from fahb f, vwdianc d, gongysb g, meikxxb m, chezxxb z, pinzb p \n");
			sb.append("where f.yunsfsb_id = ").append(SysConstant.YUNSFS_HUOY);
			sb.append(" and f." + strTongjrq + " = ").append(
					DateUtil.FormatOracleDate(getRiq())).append(" \n");
			break;
		case RPTTYPE_SB_QIY:

			sb.append(
					"sum(round_new(f.laimsl,nvl((" + roundPoint_QIY_sql + "),"
							+ visit.getShuldec() + "))) laimsl, \n ").append(
					"sum(round_new(f.jingz,nvl((" + roundPoint_QIY_sql + "),"
							+ visit.getShuldec() + "))) jingz, \n ").append(
					"sum(round_new(f.biaoz,nvl((" + roundPoint_QIY_sql + "),"
							+ visit.getShuldec() + "))) biaoz, \n")
			// .append("sum(round_new(f.yingd,nvl(("+roundPoint_QIY_sql+"),"+visit.getShuldec()+")))
			// yingd, \n")
					// .append("sum(round_new(f.yingd -
					// f.yingk,nvl(("+roundPoint_QIY_sql+"),"+visit.getShuldec()+")))
					// kuid, \n")
					.append(
							"sum(round_new(f.yingk,nvl((" + roundPoint_QIY_sql
									+ ")," + visit.getShuldec()
									+ "))) yingk, \n").append(
											yuns_qic).append(
							"sum(round_new(f.zongkd,nvl((" + roundPoint_QIY_sql
									+ ")," + visit.getShuldec()
									+ "))) zongkd, \n").append(
							"sum(f.ches) ches \n");

			sb
					.append("from fahb f, vwdianc d, gongysb g, meikxxb m, chezxxb z, pinzb p \n");
			sb.append("where f.yunsfsb_id = ").append(SysConstant.YUNSFS_QIY);
			sb.append(" and f." + strTongjrq + " = ").append(
					DateUtil.FormatOracleDate(getRiq())).append(" \n");
			break;
		default:
			break;
		}
		sb.append("and f.gongysb_id = g.id and f.meikxxb_id = m.id  \n");

		if (getTreeid() != null && !"".equals(getTreeid())
				&& !"0".equals(getTreeid())) {

			if (this.hasFenC(getTreeid())) {
				sb.append(" and f.diancxxb_id = d.id and d.fgsid =").append(
						this.getTreeid()).append("\n");
				sb.append("  and f.faz_id = z.id and f.pinzb_id = p.id \n");
				sb
						.append("group by grouping sets ((g.mingc),(d.mingc),(g.mingc,d.mingc),(d.fgsmc,g.mingc,d.mingc,m.mingc,p.mingc,z.mingc)) \n");
			} else {
				sb.append(" and f.diancxxb_id = d.id and d.id =").append(
						this.getTreeid()).append("\n");
				sb.append("  and f.faz_id = z.id and f.pinzb_id = p.id \n");
				sb
						.append("group by grouping sets ((d.mingc),(g.mingc,d.mingc),(d.fgsmc,g.mingc,d.mingc,m.mingc,p.mingc,z.mingc)) \n");
			}

		} else {

			if (this.hasFenC(((Visit) this.getPage().getVisit())
					.getDiancxxb_id()
					+ "")) {
				sb.append(" and f.diancxxb_id = d.id and d.fgsid =").append(
						((Visit) this.getPage().getVisit()).getDiancxxb_id())
						.append("\n");
				sb.append("  and f.faz_id = z.id and f.pinzb_id = p.id \n");
				sb
						.append("group by grouping sets ((g.mingc),(d.mingc),(g.mingc,d.mingc),(d.fgsmc,g.mingc,d.mingc,m.mingc,p.mingc,z.mingc)) \n");
			} else {
				sb.append(" and f.diancxxb_id = d.id and d.id =").append(
						((Visit) this.getPage().getVisit()).getDiancxxb_id())
						.append("\n");
				sb.append("  and f.faz_id = z.id and f.pinzb_id = p.id \n");
				sb
						.append("group by grouping sets ((d.mingc),(g.mingc,d.mingc),(d.fgsmc,g.mingc,d.mingc,m.mingc,p.mingc,z.mingc)) \n");
			}

		}

		sb
				.append("order by grouping(d.mingc) desc,d.mingc,g.mingc desc,grouping(m.mingc) desc) \n");
		return sb;
	}

	// ��ȡ������
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			sb = Locale.RptTitle_Jilrb_GH_All;
			break;
		case RPTTYPE_GH_HUOY:
			sb = Locale.RptTitle_Jilrb_GH_Huoy;
			break;
		case RPTTYPE_GH_QIY:
			sb = Locale.RptTitle_Jilrb_GH_Qiy;
			break;
		case RPTTYPE_SB_ALL:
			sb = Locale.RptTitle_Jilrb_SB_All;
			break;
		case RPTTYPE_SB_HUOY:
			sb = Locale.RptTitle_Jilrb_SB_Huoy;
			break;
		case RPTTYPE_SB_QIY:
			sb = Locale.RptTitle_Jilrb_SB_Qiy;
			break;
		default:
			sb = Locale.RptTitle_Jilrb_GH_All;
			break;
		}
		return sb;
	}

	// ��ȡ������
	public String getRiqTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			sb = "��������";
			break;
		case RPTTYPE_GH_HUOY:
			sb = "��������";
			break;
		case RPTTYPE_GH_QIY:
			sb = "��������";
			break;
		case RPTTYPE_SB_ALL:
			sb = "��������";
			break;
		case RPTTYPE_SB_HUOY:
			sb = "��������";
			break;
		case RPTTYPE_SB_QIY:
			sb = "��������";
			break;
		default:
			sb = "��������";
			break;
		}
		return sb;
	}

	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		/*
		 * if(visit.isFencb()) { tb1.addText(new ToolbarText("����:")); ComboBox
		 * changbcb = new ComboBox(); changbcb.setTransform("ChangbSelect");
		 * changbcb.setWidth(130);
		 * changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		 * tb1.addField(changbcb); tb1.addText(new ToolbarText("-")); }
		 */
		tb1.addText(new ToolbarText(getRiqTitle() + ":"));
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

		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		int aw=0;
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
			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;

			ArrHeader = new String[][] { {Locale.diancxxb_id_fahb, Locale.gongysb_id_fahb,
					Locale.meikxxb_id_fahb, Locale.faz_id_fahb,
					Locale.pinzb_id_fahb, Locale.laimsl_fahb,
					Locale.jingz_fahb, Locale.biaoz_fahb, Locale.yingk_fahb,
					Locale.yuns_fahb, Locale.zongkd_fahb, Locale.ches_fahb } };

			ArrWidth = new int[] { 100, 120, 70, 50, 50, 50, 50, 50, 50, 50, 50 };

			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			rt.setTitle(getRptTitle(), ArrWidth);
		}
		ArrWidth = new int[] { 100, 120, 70, 50, 50, 50, 50, 50, 50, 50, 50 };
		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ��"
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, "�������ڣ�" + getRiq(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4, "��λ���֡���", Table.ALIGN_RIGHT);

		// String[] arrFormat = new String[] { "", "", "", "", "", "", "",
		// "", "", "", "", "" };

		rt.setBody(new Table(rs, 1, 0, 3));
		// rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		// rt.body.setColAlign(1, Table.ALIGN_CENTER);
		// rt.body.setColAlign(2, Table.ALIGN_CENTER);
		// rt.body.setColAlign(3, Table.ALIGN_CENTER);
		// rt.body.setColAlign(4, Table.ALIGN_CENTER);
		// rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		// rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);

		// rt.body.setCellVAlign(i, j, intAlign)
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		// rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(Report.PAPER_ROWS);
		//	���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		// rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"
				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "�Ʊ�"+getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		// rt.footer.setRowHeight(1, 1);
//		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), getBaseSql().toString(), rt,
				getRptTitle(), "" +BAOBPZB_GUANJZ);
		
		StringBuffer strsb = new StringBuffer(rt.getAllPagesHtml());
		
		if (v.getInt1() == Jilrb.RPTTYPE_GH_HUOY) {
			String str = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ�ٶ�ͳ����", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
			if (str.equals("��")) {
				
				String sql = 
					"select case\n" +
					"         when to_char(temp.jib) = '1' then '1Km����'\n" + 
					"         when to_char(temp.jib) = '2' then '1Km��15Km'\n" + 
					"         when to_char(temp.jib) = '3' then '15Km����'\n" + 
					"         else '' end as fanw, count(temp.ches) ches from (\n" + 
					"select case\n" + 
					"         when cp.ches < 1.0 then 1\n" + 
					"         when cp.ches >= 1.0 and cp.ches <= 15.0 then 2\n" + 
					"         when cp.ches > 15.0 then 3\n" + 
					"         else 0.0 end as jib,\n" + 
					"       cp.fahb_id,\n" + 
					"       cp.id chepb_id,\n" + 
					"       cp.ches ches\n" + 
					"  from chepb cp, fahb fh, gongysb gys, meikxxb mk, vwdianc d, chezxxb cz, pinzb pz\n" + 
					" where cp.fahb_id = fh.id\n" + 
					"   and fh.yunsfsb_id = 1\n" + 
					"   and fh.gongysb_id = gys.id\n" + 
					"   and fh.meikxxb_id = mk.id\n" + 
					"   and fh.diancxxb_id = d.id\n" + 
					"   and d.fgsid = "+ getTreeid() +"\n" + 
					"   and fh.faz_id = cz.id\n" + 
					"   and fh.pinzb_id = pz.id\n" + 
					"   and cp.zhongcsj >= to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
					"   and cp.zhongcsj < to_date('"+ getRiq() +"', 'yyyy-mm-dd') + 1 order by jib) temp\n" + 
					"group by temp.jib";

				ResultSetList rsldata = con.getResultSetList(sql);
				Report rt_ches = new Report();
				
				int least = 0;		// ������⳵����1KM���µĳ���
				int middle = 0;		// ������⳵����1KM��15KM֮��ĳ���
				int most = 0;		// ������⳵����15KM���ϵĳ���
				
				Table tb_ches = new Table(4, 2);
				String[][] ArrHeader_ches = new String[1][2];
				ArrHeader_ches[0] = new String[]{"����","����"};
				int[] ArrWidth_ches = new int[] {140, 140};
				
				tb_ches.setCellValue(2, 1, "1Km����");
				tb_ches.setCellValue(3, 1, "1Km��15Km");
				tb_ches.setCellValue(4, 1, "15Km����");
				
				while(rsldata.next()) {
					if(rsldata.getString("fanw").equals("1Km����")) {
						least = Integer.parseInt(rsldata.getString("ches")) ;
					} else if(rsldata.getString("fanw").equals("1Km��15Km")) {
						middle = Integer.parseInt(rsldata.getString("ches"));
					} else {
						most = Integer.parseInt(rsldata.getString("ches"));
					}
				}
				
				tb_ches.setCellValue(2, 2, String.valueOf(least));
				tb_ches.setCellValue(3, 2, String.valueOf(middle));
				tb_ches.setCellValue(4, 2, String.valueOf(most));
				
				rt_ches.setTitle("�ٶ�ͳ����", ArrWidth_ches);
				rt_ches.setBody(tb_ches);
				rt_ches.body.setWidth(ArrWidth_ches);
				rt_ches.body.setHeaderData(ArrHeader_ches);
				rt_ches.body.setColAlign(1, Table.ALIGN_CENTER);
				rt_ches.body.setColAlign(2, Table.ALIGN_CENTER);
				rt_ches.setDefaultTitle(1, 2, "��λ��Km����", Table.ALIGN_RIGHT);
				rt_ches.body.ShowZero = true;
				
				rsldata.close();
				strsb.append(rt_ches.getAllPagesHtml());
			}
		}
		con.Close();
		return strsb.toString();
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
		String reportType = cycle.getRequestContext().getParameter("lx");
//		System.out.println(reportType+"----");
		if (reportType != null) {
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setInt1(Integer.parseInt(reportType));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			if (reportType == null) {
				visit.setInt1(RPTTYPE_GH_ALL);
			}
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			getGongysDropDownModels();	
			setTreeid(null);
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