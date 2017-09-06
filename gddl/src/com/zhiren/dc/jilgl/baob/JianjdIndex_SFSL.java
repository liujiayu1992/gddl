package com.zhiren.dc.jilgl.baob;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.IReport;
import com.zhiren.report.Table;

public class JianjdIndex_SFSL extends BasePage {
	// ���䷽ʽ
	private static final int RPTTYPE_GH_ALL = 1;// ��+����

	private static final int RPTTYPE_GH_HUOY = 2;// ��

	private static final int RPTTYPE_GH_QIY = 3;// ����

	// ģʽ��̬��

	private static final int SELECT_MOS_ALL = 1;// ����ģʽ

	private static final int SELECT_MOS_BYMEIK = 2;// �����ģʽ

	private static final int SELECT_MOS_BYYUNS = 3;// �����䵥λ��ģʽ

	// �����û���ʾ

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {
		((Visit) this.getPage().getVisit()).setString1(riq);
		this.riq = riq;
	}

	// �����ڷ�Χ
	private String riq2;

	public String getRiq2() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setRiq2(String riq2) {
		((Visit) this.getPage().getVisit()).setString2(riq2);
		this.riq2 = riq2;
	}

	// ��ʱ��
	private String shijvalue;

	public String getShijvalue() {
		return this.shijvalue;
	}

	public void setShijvalue(String shijvalue) {
		this.shijvalue = shijvalue;
	}

	// ����ʱ�������˵�
	private String shijlistsel;

	public String getShijlistsel() {
		return this.shijlistsel;
	}

	public void setShijlistsel(String shijlistsel) {
		this.shijlistsel = shijlistsel;
	}

	// ����ģʽ�����˵�
	private String moslist;

	public String getMoslist() {
		return moslist;
	}

	public void setMoslist(String moslist) {
		this.moslist = moslist;
	}

	// ��ģʽ
	private String mosvalue = "1";

	public String getMosvalue() {
		return this.mosvalue;
	}

	public void setMosvalue(String mosvalue) {
		this.mosvalue = mosvalue;
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

	// ���ݲ�ͬ�Ĳ�ѯģʽ��ȡ��ص�SQL
	/*
	 * ���Ӱ����ҳ �޸�ʱ�䣺2009-01-12 �޸���:��һ��
	 */

	/*
	 * �޸���:ͯ�Ҹ� �޸�ʱ��:2009-4-8 �޸�����:����ʱ��guohbû�й�ϵ,��sql��估����ص��߼���д
	 */
	// �õ�����ģʽ�Ĳ���sql
	private StringBuffer SELECT_MOS_ALL_sql() {

		StringBuffer sb = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();

		String diancxxb_id = visit.getDiancxxb_id() + "";
		if (visit.isFencb()) {
			diancxxb_id = this.getChangbValue().getStrId();
		}

		if (visit.getInt1() == RPTTYPE_GH_ALL) {
			// ����
			sb.append("select ");
			sb.append(" yu.mingc as yunsfs ,");

			sb
					.append("g.mingc as gonghdw,y.mingc as yunsdw,m.mingc as meikdw,\n");
			sb
					.append(" c.cheph ,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) as yuns,\n");
			sb.append(" sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// ����������
			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id\n ");
			sb.append(getYunsfs("1"));
			sb.append(" group by rollup(");

			sb.append("yu.mingc,");

			sb.append("g.mingc,m.mingc,c.cheph,p.mingc,y.mingc) having\n");

			sb.append(" grouping(yu.mingc)=0 and \n");

			sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1 and grouping(y.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0) and grouping(y.mingc)=0)");

			sb.append(" union \n");

			// ����
			sb.append("select ");
			sb.append(" yu.mingc as yunsfs ,");

			sb
					.append("g.mingc as gonghdw,y.mingc as yunsdw,m.mingc as meikdw,\n");
			sb
					.append(" c.cheph ,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) as yuns,\n");
			sb.append(" sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id\n ");
			sb.append(getYunsfs("2"));
			sb.append(" group by rollup(");

			sb.append("yu.mingc,");

			sb.append("g.mingc,m.mingc,c.cheph,p.mingc,y.mingc) having\n");

			sb.append(" grouping(yu.mingc)=0 and \n");

			sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1 and grouping(y.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0) and grouping(y.mingc)=0)");

		}

		if (visit.getInt1() == RPTTYPE_GH_HUOY) {

			sb.append("select ");
			sb
					.append("g.mingc as gonghdw,y.mingc as yunsdw,m.mingc as meikdw,\n");
			sb
					.append(" c.cheph ,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) as yuns,\n");
			sb.append(" sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id\n ");
			sb.append(getYunsfs(null));
			sb.append(" group by rollup(");
			sb.append("g.mingc,m.mingc,c.cheph,p.mingc,y.mingc) having\n");
			sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1 and grouping(y.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0) and grouping(y.mingc)=0)");
		}

		if (visit.getInt1() == RPTTYPE_GH_QIY) {

			sb.append("select ");
			sb
					.append("decode(g.mingc, null, '�ܼ�', g.mingc) as gonghdw,y.mingc as yunsdw,m.mingc as meikdw,\n");
			sb
					.append(" decode(c.cheph, null, to_char(count(c.id))||'��', c.cheph) cheph,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) as yuns,\n");
			sb.append(" sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id\n ");
			sb.append(getYunsfs(null));
			sb.append(" group by rollup(");
			sb.append("g.mingc,m.mingc,c.cheph,p.mingc,y.mingc,c.id) having\n");
			//sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1 and grouping(y.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0) and grouping(y.mingc)=0 and grouping(c.id) = 0)");

		}

		return sb;
	}

	// �õ�ú��ģʽ�Ĳ���sql
	private StringBuffer SELECT_MOS_BYMEIK_sql(String r) {
		StringBuffer sb = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();

		String diancxxb_id = visit.getDiancxxb_id() + "";
		if (visit.isFencb()) {
			diancxxb_id = this.getChangbValue().getStrId();
		}

		if (visit.getInt1() == RPTTYPE_GH_ALL) {
			// ����
			sb.append("select ");

			sb.append(" yu.mingc as yunsfs ,");

			sb.append("g.mingc as gonghdw,m.mingc as meikdw,\n");
			sb
					.append(" c.cheph ,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");
			sb.append("sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id and m.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs("1"));
			sb.append(" group by rollup(");

			sb.append("yu.mingc,");

			sb.append("g.mingc,m.mingc,c.cheph,p.mingc) having\n");

			sb.append(" grouping(yu.mingc)=0 and \n");

			sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0))");

			sb.append("  union \n");

			// ����
			sb.append("select ");

			sb.append(" yu.mingc as yunsfs ,");

			sb.append("g.mingc as gonghdw,m.mingc as meikdw,\n");
			sb
					.append(" c.cheph ,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");
			sb.append("sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and  p.id=f.pinzb_id and f.yunsfsb_id=yu.id and m.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs("2"));
			sb.append(" group by rollup(");

			sb.append("yu.mingc,");

			sb.append("g.mingc,m.mingc,c.cheph,p.mingc) having\n");

			sb.append(" grouping(yu.mingc)=0 and \n");

			sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0))");

		}
		if (visit.getInt1() == RPTTYPE_GH_HUOY) {
			sb.append("select ");

			sb.append("g.mingc as gonghdw,m.mingc as meikdw,\n");
			sb
					.append(" c.cheph ,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");
			sb.append("sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id and m.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs(null));
			sb.append(" group by rollup(");

			sb.append("g.mingc,m.mingc,c.cheph,p.mingc) having\n");

			sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0))");
		}
		if (visit.getInt1() == RPTTYPE_GH_QIY) {
			sb.append("select ");

			sb.append("g.mingc as gonghdw,m.mingc as meikdw,\n");
			sb
					.append(" c.cheph ,p.mingc as pinz ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) as biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");
			sb.append("sum(c.yingd) as yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and  p.id=f.pinzb_id and f.yunsfsb_id=yu.id and m.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs(null));
			sb.append(" group by rollup(");

			sb.append("g.mingc,m.mingc,c.cheph,p.mingc) having\n");

			sb.append("grouping(g.mingc)=0 and\n");
			sb
					.append("((grouping(m.mingc)=1 and grouping(c.cheph)=1 and grouping(p.mingc)=1)or(grouping(m.mingc)=0 and grouping(c.cheph)=0 and grouping(p.mingc)=0))");
		}

		return sb;
	}

	// �õ����䵥λģʽ�Ĳ���sql
	private StringBuffer SELECT_MOS_BYYUNS_sql(String r) {
		StringBuffer sb = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();

		String diancxxb_id = visit.getDiancxxb_id() + "";
		if (visit.isFencb()) {
			diancxxb_id = this.getChangbValue().getStrId();
		}

		if (visit.getInt1() == RPTTYPE_GH_ALL) {
			// ����
			sb.append("select y.mingc as yunsdw,\n");

			sb
					.append(" yu.mingc as yunsfs,m.mingc as meikdw,c.cheph ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");

			sb.append("sum(c.yingd) yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id and y.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs("1"));

			sb.append(" group by rollup(y.mingc,yu.mingc,m.mingc,c.cheph)\n");
			sb
					.append(" having grouping(y.mingc)=0 and ((grouping(yu.mingc)=1 and grouping(m.mingc)=1 and grouping(c.cheph)=1)or(grouping(yu.mingc)=0 and grouping(m.mingc)=0 and grouping(c.cheph)=0))");

			sb.append(" union \n");

			// ����
			sb.append("select y.mingc as yunsdw,\n");

			sb
					.append(" yu.mingc as yunsfs,m.mingc as meikdw,c.cheph ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");

			sb.append("sum(c.yingd) yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id and y.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs("2"));

			sb.append(" group by rollup(y.mingc,yu.mingc,m.mingc,c.cheph)\n");
			sb
					.append(" having grouping(y.mingc)=0 and ((grouping(yu.mingc)=1 and grouping(m.mingc)=1 and grouping(c.cheph)=1)or(grouping(yu.mingc)=0 and grouping(m.mingc)=0 and grouping(c.cheph)=0))");

		}
		if (visit.getInt1() == RPTTYPE_GH_HUOY) {
			sb.append("select y.mingc as yunsdw,\n");

			sb
					.append(" m.mingc as meikdw,c.cheph ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");

			sb.append("sum(c.yingd) yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id and y.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs(null));

			sb.append(" group by rollup(y.mingc,m.mingc,c.cheph)\n");
			sb
					.append(" having grouping(y.mingc)=0 and (( grouping(m.mingc)=1 and grouping(c.cheph)=1)or( grouping(m.mingc)=0 and grouping(c.cheph)=0))");

		}
		if (visit.getInt1() == RPTTYPE_GH_QIY) {
			sb.append("select y.mingc as yunsdw,\n");

			sb
					.append(" m.mingc as meikdw,c.cheph ,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.maoz-c.piz-c.zongkd) as jingz,sum(c.biaoz) biaoz,sum(c.sanfsl) as sanfsl,sum(c.yuns) yuns,\n");

			sb.append("sum(c.yingd) yingd,sum(c.yingd-c.yingk) as kuid \n");
			sb
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
			sb
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id \n");

			// �������޸�
			sb.append(Jilcz.filterDcid(visit, "f")).append(" \n");

			sb
					.append(" and y.id(+)=c.yunsdwb_id and  p.id=f.pinzb_id and f.yunsfsb_id=yu.id and y.mingc='"
							+ r + "'\n");
			sb.append(getYunsfs(null));

			sb.append(" group by rollup(y.mingc,m.mingc,c.cheph)\n");
			sb
					.append(" having grouping(y.mingc)=0 and (( grouping(m.mingc)=1 and grouping(c.cheph)=1)or( grouping(m.mingc)=0 and grouping(c.cheph)=0))");

		}

		return sb;
	}

	public StringBuffer getBaseSql(String r) {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();

		switch (new Integer(this.getMosvalue()).intValue()) {
		default:
		case SELECT_MOS_ALL:
			sb = this.SELECT_MOS_ALL_sql();
			break;
		case SELECT_MOS_BYMEIK:
			sb = this.SELECT_MOS_BYMEIK_sql(r);
			break;
		case SELECT_MOS_BYYUNS:
			sb = this.SELECT_MOS_BYYUNS_sql(r);
			break;

		}

		System.out.println(sb.toString());
		return sb;
	}

	// ��ȡ������
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			sb = "��ﵥ";
			break;
		case RPTTYPE_GH_HUOY:
			sb = "�𳵼�ﵥ";
			break;
		case RPTTYPE_GH_QIY:
			sb = "������ﵥ";
			break;
		default:
			sb = "��ﵥ";
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
		default:
			sb = "��������";
			break;
		}
		return sb;
	}

	// �������䷽ʽ�õ����ݿ��ѯ��������
	public String getYunsfs(String a) {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;

		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			sb = "";
			if (a.equals("1")) {// ���˵����� -----���� ��· ��· ��־
				sb = " and yu.mingc='" + Locale.yunsfs_tiel + "'\n";
				sb += " and gu.guohsj<=" + "to_date('" + this.getRiq()
						+ "','yyyy-mm-dd')+1 and gu.guohsj>=to_date('"
						+ this.getRiq2() + "','yyyy-mm-dd')" + "\n";
			}
			if (a.equals("2")) {// ���˵�����
				sb = "and yu.mingc ='" + Locale.yunsfs_gongl + "'\n";
				sb += " and c.qingcsj<=" + "to_date('" + this.getRiq()
						+ "','yyyy-mm-dd')+1 and c.qingcsj>=to_date('"
						+ this.getRiq2() + "','yyyy-mm-dd')" + "\n";
			}

			break;
		case RPTTYPE_GH_HUOY:
			sb = " and yu.mingc='" + Locale.yunsfs_tiel + "'\n";
			sb += " and gu.guohsj=to_date('" + this.getRiq() + " "
					+ this.getShijvalue() + "','yyyy-mm-dd hh24:mi:ss')\n";
			break;
		case RPTTYPE_GH_QIY:
			sb = "and yu.mingc ='" + Locale.yunsfs_gongl + "'\n";
			sb += " and c.qingcsj<=" + "to_date('" + this.getRiq()
					+ "','yyyy-mm-dd')+1 and c.qingcsj>=to_date('"
					+ this.getRiq2() + "','yyyy-mm-dd')" + "\n";
			break;
		default:
			sb = "\n";
			break;
		}
		return sb;
	}

	// ���ɲ�ѯѡ�����
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		// ����ext������
		Toolbar tb1 = new Toolbar("tbdiv");
		if (visit.isFencb()) {
			tb1.addText(new ToolbarText("����:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText(getRiqTitle() + ":"));
		// ext��������������ѡ��
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		df
				.setListeners("change:function(own,newValue,oldValue){"
						+ " document.getElementById('Riq').value = newValue.dateFormat('Y-m-d');"
						+ "  document.getElementById('RefurbishButton').click();"
						+ "   } ");

		// ����ǻ���,��ʾ��Ӧ��ʱ��
		switch (visit.getInt1()) {
		case RPTTYPE_GH_HUOY:

			ComboBox cb = new ComboBox();
			cb.setWidth(80);
			cb.setId("shijlistsel");
			cb.setTransform("shijlist");
			cb.setEditable(false);
			cb.setForceSelection(false);// ��Ϊ�п�ֵ��������֣���������Ϊ��ǿ��ѡ��
			tb1.addField(df);
			tb1.addText(new ToolbarText("ʱ�䣺"));
			tb1.addField(cb);

			StringBuffer sb = new StringBuffer();

			JDBCcon con = new JDBCcon();
			ResultSet rs = null;
			StringBuffer sbl = new StringBuffer();
			sbl.append("select gu.guohsj as guohsj\n");
			sbl
					.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
			sbl
					.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id and\n");
			sbl
					.append("y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id\n");
			if (visit.getInt1() == RPTTYPE_GH_HUOY) {
				sbl.append(" and yu.mingc='" + Locale.tiel_yunsfs + "'\n");
			}
			if (visit.getInt1() == RPTTYPE_GH_QIY) {
				sbl.append(" and yu.mingc='" + Locale.gongl_yunsfs + "'\n");
			}
			sbl.append(" and to_char(gu.guohsj,'yyyy-mm-dd')='");
			sbl.append(this.getRiq() + "'");
			rs = con.getResultSet(sbl.toString());
			try {
				sb.append("\n<select id='shijlist'>\n");
				while (rs.next()) {
					if (this.getShijvalue().equals(
							rs.getTime("guohsj").toString())) {
						sb.append(" <option value='"
								+ rs.getTime("guohsj").toString()
								+ "' selected>"
								+ rs.getTime("guohsj").toString()
								+ "</option>\n");
					} else {
						sb.append(" <option value='"
								+ rs.getTime("guohsj").toString() + "'>"
								+ rs.getTime("guohsj").toString()
								+ "</option>\n");
					}
				}
				sb.append("</select>\n");
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			setShijlistsel(sb.toString());
			break;
		// ����ǹ�·��ȫ��,��ʾ�ڶ�������file
		case RPTTYPE_GH_ALL:
		case RPTTYPE_GH_QIY:
		default:
			DateField df2 = new DateField();
			df2.Binding("Riq2", "");
			df2.setValue(getRiq2());
			df2.setId("guohrq2");
			df2
					.setListeners("change:function(own,newValue,oldValue){"
							+ " document.getElementById('Riq2').value = newValue.dateFormat('Y-m-d');"
							+ "  document.getElementById('RefurbishButton').click();"
							+ "   } ");
			tb1.addField(df2);
			tb1.addText(new ToolbarText("��"));
			tb1.addField(df);
			break;
		}

		tb1.addText(new ToolbarText("ģʽ��"));
		// �ж�ѡ����
		StringBuffer sbmos = new StringBuffer();
		sbmos.append("<select id='selectmode'>\n");
		switch (new Integer(this.getMosvalue()).intValue()) {
		case SELECT_MOS_ALL:
			sbmos.append(" <option value='1' selected>����</option>\n");
			sbmos.append("<option value='2'>�ֿ�</option>\n");
			sbmos.append(" <option value='3'>�����䵥λ</option>\n");
			break;
		case SELECT_MOS_BYMEIK:
			sbmos.append(" <option value='1' >����</option>\n");
			sbmos.append("<option value='2' selected>�ֿ�</option>\n");
			sbmos.append(" <option value='3'>�����䵥λ</option>\n");
			break;
		case SELECT_MOS_BYYUNS:
			sbmos.append(" <option value='1'>����</option>\n");
			sbmos.append("<option value='2'>�ֿ�</option>\n");
			sbmos.append(" <option value='3' selected>�����䵥λ</option>\n");
			break;
		default:
			sbmos.append(" <option value='1' selected>����</option>\n");
			sbmos.append("<option value='2'>�ֿ�</option>\n");
			sbmos.append(" <option value='3'>�����䵥λ</option>\n");
		}
		sbmos.append("</select>\n");
		this.setMoslist(sbmos.toString());
		ComboBox cb = new ComboBox();
		cb.setWidth(90);
		cb.setId("mocombox");
		cb.setTransform("selectmode");
		cb.setEditable(false);
		cb.setForceSelection(false);

		tb1.addField(cb);

		ToolbarButton rbtn;
		if (visit.getInt1() == RPTTYPE_GH_HUOY) {
			rbtn = new ToolbarButton(
					null,
					"��ѯ",
					"function(){document.getElementById('shij').value = shijlistsel.getValue();document.getElementById('mos').value = mocombox.getValue();document.getElementById('RefurbishButton').click();}");
		} else {
			rbtn = new ToolbarButton(
					null,
					"��ѯ",
					"function(){document.getElementById('mos').value = mocombox.getValue();document.getElementById('RefurbishButton').click();}");
		}
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addText(new ToolbarText("-"));
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));

		setToolbar(tb1);
	}

	// ���ɱ��
	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		ResultSet rs = null;
		ArrayList al = new ArrayList();
		Visit visit = (Visit) this.getPage().getVisit();
		IReport rt = new IReport();
		String[][] ArrHeader;
		int[] ArrWidth;
		// ���ݲ�ͬ��ģʽ�γɲ�ͬ�����ݣ��ͱ����ʽ
		switch (new Integer(this.getMosvalue()).intValue()) {
		default:
		case SELECT_MOS_ALL:
			// ��ģʽ����

			rs = con.getResultSet(getBaseSql(null),
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			al.add(new Table(rs, 1, 0, 3));
			// ����ʽ���
			if (visit.getInt1() == RPTTYPE_GH_ALL) {
				ArrHeader = new String[][] { { Locale.yunsfsb_id_fahb,
						Locale.gongysb_id_fahb, Locale.yunsdw_id_chepb,
						Locale.meikxxb_id_fahb, Locale.cheph_chepb,
						Locale.pinzb_id_fahb, Locale.maoz_chepb,
						Locale.piz_fahb, Locale.jingz_fahb, Locale.biaoz_fahb,
						"ʵ����", Locale.yuns_fahb, Locale.yingd_fahb,
						Locale.kuid_fahb } };
				ArrWidth = new int[] { 100, 65, 65, 50, 45, 45, 45, 45, 45, 45,
						45, 45, 45, 45 };
			} else {

				ArrHeader = new String[][] { { Locale.gongysb_id_fahb,
						Locale.yunsdw_id_chepb, Locale.meikxxb_id_fahb,
						Locale.cheph_chepb, Locale.pinzb_id_fahb,
						Locale.maoz_chepb, Locale.piz_fahb, Locale.jingz_fahb,
						Locale.biaoz_fahb, "ʵ����", Locale.yuns_fahb,
						Locale.yingd_fahb, Locale.kuid_fahb } };
				ArrWidth = new int[] { 100, 65, 65, 50, 45, 45, 45, 45, 45, 45,
						45, 45, 45 };

			}
			break;
		case SELECT_MOS_BYMEIK:
			StringBuffer sbl = new StringBuffer();
			sbl.append(" select distinct meikdw from ( \n");

			if (visit.getInt1() != RPTTYPE_GH_QIY) {
				sbl.append(" select distinct m.mingc as meikdw\n");
				sbl
						.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
				sbl
						.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id \n");
				sbl.append(this.getYunsfs("1"));
			}

			if (visit.getInt1() == RPTTYPE_GH_ALL) {
				sbl.append(" union \n");
			}

			if (visit.getInt1() != RPTTYPE_GH_HUOY) {
				sbl.append(" select distinct m.mingc as meikdw\n");
				sbl
						.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
				sbl
						.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id and y.id(+)=c.yunsdwb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id \n");
				sbl.append(this.getYunsfs("2"));
			}

			sbl.append(" )");
			ResultSet rsother = con.getResultSet(sbl);
			try {
				while (rsother.next()) {
					al.add(new Table(con.getResultSet(getBaseSql(rsother
							.getString("meikdw")),
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY), 1, 0, 3));
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			if (visit.getInt1() == RPTTYPE_GH_ALL) {
				ArrHeader = new String[][] { { Locale.yunsfsb_id_fahb,
						Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb,
						Locale.cheph_chepb, Locale.pinzb_id_fahb,
						Locale.maoz_chepb, Locale.piz_fahb, Locale.jingz_fahb,
						Locale.biaoz_fahb, "ʵ����", Locale.yuns_fahb,
						Locale.yingd_fahb, Locale.kuid_fahb } };
				ArrWidth = new int[] { 100, 65, 65, 50, 45, 45, 45, 45, 45, 45,
						45, 45, 45 };
			} else {
				ArrHeader = new String[][] { { Locale.gongysb_id_fahb,
						Locale.meikxxb_id_fahb, Locale.cheph_chepb,
						Locale.pinzb_id_fahb, Locale.maoz_chepb,
						Locale.piz_fahb, Locale.jingz_fahb, Locale.biaoz_fahb,
						"ʵ����", Locale.yuns_fahb, Locale.yingd_fahb,
						Locale.kuid_fahb } };
				ArrWidth = new int[] { 100, 65, 65, 50, 45, 45, 45, 45, 45, 45,
						45, 45 };
			}
			break;
		case SELECT_MOS_BYYUNS:
			StringBuffer sbly = new StringBuffer();

			sbly.append(" select distinct yunsdw from (\n");

			if (visit.getInt1() != RPTTYPE_GH_QIY) {
				sbly.append(" select distinct y.mingc as yunsdw\n");
				sbly
						.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,guohb gu,pinzb p,yunsfsb yu\n");
				sbly
						.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id and y.id(+)=c.yunsdwb_id and gu.id=c.guohb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id\n");
				sbly.append(this.getYunsfs("1"));
			}

			if (visit.getInt1() == RPTTYPE_GH_ALL) {
				sbly.append(" union \n");
			}

			if (visit.getInt1() != RPTTYPE_GH_HUOY) {
				sbly.append(" select distinct y.mingc as yunsdw\n");
				sbly
						.append("from gongysb g,meikxxb m,yunsdwb y,fahb f ,chepb c,pinzb p,yunsfsb yu\n");
				sbly
						.append("where f.id=c.fahb_id and g.id=f.gongysb_id and m.id=f.meikxxb_id and y.id(+)=c.yunsdwb_id and p.id=f.pinzb_id and f.yunsfsb_id=yu.id\n");
				sbly.append(this.getYunsfs("2"));
			}

			sbly.append(")");
			ResultSet rso = con.getResultSet(sbly);
			try {
				while (rso.next()) {
					al.add(new Table(con.getResultSet(getBaseSql(rso
							.getString("yunsdw")),
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY), 1, 0, 3));
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			if (visit.getInt1() == RPTTYPE_GH_ALL) {
				ArrHeader = new String[][] { { Locale.yunsdw_id_chepb,
						Locale.yunsfsb_id_fahb, Locale.meikxxb_id_fahb,
						Locale.cheph_chepb, Locale.pinzb_id_fahb,
						Locale.maoz_chepb, Locale.piz_fahb, Locale.jingz_fahb,
						Locale.biaoz_fahb, "ʵ����", Locale.yuns_fahb,
						Locale.yingd_fahb, Locale.kuid_fahb } };
				ArrWidth = new int[] { 100, 65, 65, 50, 45, 45, 45, 45, 45, 45,
						45, 45, 45, 45 };
			} else {
				ArrHeader = new String[][] { { Locale.yunsdw_id_chepb,
						Locale.meikxxb_id_fahb, Locale.cheph_chepb,
						Locale.pinzb_id_fahb, Locale.maoz_chepb,
						Locale.piz_fahb, Locale.jingz_fahb, Locale.biaoz_fahb,
						"ʵ����", Locale.yuns_fahb, Locale.yingd_fahb,
						Locale.kuid_fahb } };
				ArrWidth = new int[] { 100, 65, 65, 50, 45, 45, 45, 45, 45, 45,
						45, 45, 45 };
			}
			break;

		}

		if (al == null) {
			setMsg("���ݻ�ȡʧ�ܣ�������������״����������� JLRB-001");
			return "";
		}

		rt.setTabels(al);
		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ��"
				+ ((Visit) this.getPage().getVisit()).getDiancmc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, "�������ڣ�" + getRiq(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 5, "��λ���֡���", Table.ALIGN_RIGHT);

		// String[] arrFormat = new String[] { "", "", "", "", "", "", "",
		// "", "", "", "", "" };

		// rt.setBody(new Table(rs, 1, 0, 3));
		// rt.body.setColAlign(0, Table.ALIGN_CENTER);r
		int count = 0;
		for (int i = 0; i < rt.getTabels().size(); i++) {
			((Table) rt.getTabels().get(i)).setColAlign(1, Table.ALIGN_CENTER);
			((Table) rt.getTabels().get(i)).setColAlign(2, Table.ALIGN_CENTER);
			((Table) rt.getTabels().get(i)).setColAlign(3, Table.ALIGN_CENTER);
			((Table) rt.getTabels().get(i)).setColAlign(4, Table.ALIGN_CENTER);
			((Table) rt.getTabels().get(i)).setColAlign(5, Table.ALIGN_RIGHT);
			((Table) rt.getTabels().get(i)).setColAlign(6, Table.ALIGN_RIGHT);
			((Table) rt.getTabels().get(i)).setColAlign(7, Table.ALIGN_RIGHT);
			((Table) rt.getTabels().get(i)).setColAlign(8, Table.ALIGN_RIGHT);
			((Table) rt.getTabels().get(i)).setColAlign(9, Table.ALIGN_RIGHT);
			((Table) rt.getTabels().get(i)).setColAlign(10, Table.ALIGN_RIGHT);
			((Table) rt.getTabels().get(i)).setColAlign(11, Table.ALIGN_RIGHT);
			((Table) rt.getTabels().get(i)).setColAlign(12, Table.ALIGN_RIGHT);
			// rt.body.setCellVAlign(i, j, intAlign)
			((Table) rt.getTabels().get(i)).setWidth(ArrWidth);
			((Table) rt.getTabels().get(i)).setHeaderData(ArrHeader);
			// ((Table)rt.getTabels().get(i)).setColFormat(arrFormat);
			((Table) rt.getTabels().get(i)).setPageRows(34);
			((Table) rt.getTabels().get(i)).mergeFixedCols();
			((Table) rt.getTabels().get(i)).mergeFixedRow();

			((Table) rt.getTabels().get(i)).setRowHeight(21);
			count += ((Table) rt.getTabels().get(i)).getPages();
		}
		// rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"
				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		// rt.footer.setRowHeight(1, 1);
		con.Close();
		if (count > 0) {
			setCurrentPage(1);
			setAllPages(count);
		}
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			// setRiq(DateUtil.FormatDate(new Date()));
			visit.setInt1(Integer.parseInt(reportType));
		}

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setShijvalue(DateUtil.FormatTime(new Date()));
			// ����Ĭ�ϵĲ�ѯ��ʼ����
			Date date = new Date();
			int month = DateUtil.getMonth(date);
			int year = DateUtil.getYear(date);
			int day = DateUtil.getDay(date);
			// ����Ĭ��ʱ��Ϊ��������
			if (month < 4) {
				month += 9;
				year -= 1;
			} else {
				month -= 3;
			}
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-mm-dd");
			Date date1 = new Date();
			//���ӵ�����������
			try {
				String zhi = MainGlobal.getXitsz("��ﵥͳ��ʱ��", visit.getDiancxxb_id()
						+ "","");
				if (zhi.equals(null) && zhi.equals("")) {
					date1 = sim.parse(year + "-" + month + "-" + day);
				} else if (zhi.equals("����")) {
					date1 = new Date();
				}
			} catch (ParseException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
			// date.setMonth(date.getMonth() - 3);// ����Ĭ��ʱ��Ϊ��������
			this.setRiq2(DateUtil.FormatDate(date1));
			if (reportType == null) {
				visit.setInt1(RPTTYPE_GH_ALL);
			}

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

}
