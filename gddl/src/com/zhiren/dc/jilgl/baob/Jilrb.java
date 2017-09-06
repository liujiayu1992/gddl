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
 * 作者:tzf
 * 时间:2010-01-25
 * 修改内容:根据龙江公司要求，盈亏和运损相互计算，否则报表账面不平，用参数控制。
 */

/*
 * 作者：陈泽天
 * 时间：2010-01-20 17：24
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：夏峥
 * 时间：2013-06-18
 * 描述：处理界面电厂树初始化BUG。
 */
public class Jilrb extends BasePage {

	/*
	 * 时间:2009-03-19 作者:童忠付 内容:对过衡日报增加盈亏字段，当运输方式是汽运时，要在xitxxb中查找对应计算的小数点保留位数，
	 * 并且增加电厂字段，进行统计 相应的数据库配置说明已经落实，具体请查看
	 */
	private static final int RPTTYPE_GH_ALL = 1;

	private static final int RPTTYPE_GH_HUOY = 2;

	private static final int RPTTYPE_GH_QIY = 3;

	private static final int RPTTYPE_SB_ALL = 4;

	private static final int RPTTYPE_SB_HUOY = 5;

	private static final int RPTTYPE_SB_QIY = 6;

	private static final String ROUNDPOINT_QIY = "汽车日报小数位"; // 系统信息表中对应的小数点保留个数（对汽车运输有效）

	private static final String ROUNDPOINT_HUOY = "火车日报小数位"; // 系统信息表中对应的小数点保留个数（对火车有效）

	/**
	 * 作者:童忠付
	 * 时间:2009-4-3
	 * 内容:报表配置时,增加报表关键字
	 */
	private static final String BAOBPZB_GUANJZ = "GUOHRB_GJZ";// baobpzb中对应的关键字
	// 界面用户提示

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

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	// 页面变化记录
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

	// 厂别下拉框
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
//设置制表人默认当前用户
	private String getZhibr(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String zhibr="";
		String zhi="否";
		String sql="select zhi from xitxxb where mingc='月报管理制表人是否默认当前用户' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSet rs=con.getResultSet(sql);
		try{
		  while(rs.next()){
			  zhi=rs.getString("zhi");
		  }
		}catch(Exception e){
			System.out.println(e);
		}
		if(zhi.equals("是")){
			zhibr=visit.getRenymc();
		}	
		return zhibr;
	}
	
	private String getMingc(String id) { // 获得选择的树节点的对应的电厂名称
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

	private boolean hasFenC(String id) { // 是否有分厂

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

	// 获取相关的SQL
	/*
	 * 增加来煤数量(laimsl)字段 并按照设定进行修约 修改时间：2008-12-04 修改人：王磊
	 */
	public StringBuffer getBaseSql() {
		Visit visit = (Visit) this.getPage().getVisit();
		// 此处应该根据什么来查询相应的zhi？
		String roundPoint_QIY_sql = "select r.zhi from xitxxb r where r.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ " and r.mingc='"
				+ ROUNDPOINT_QIY
				+ "' and r.zhuangt=1";
		
//		读取系统信息表中"火车日报小数位"的小数位数.
		String roundPoint_HUOY_sql = "select r.zhi from xitxxb r where r.diancxxb_id="
			+ visit.getDiancxxb_id()
			+ " and r.mingc='"
			+ ROUNDPOINT_HUOY
			+ "' and r.zhuangt=1";
		
		String huoh_qic=" select * from xitxxb where mingc='汽车过衡日报为轻车时间' and zhi='是' and zhuangt=1 and leib='数量' and diancxxb_id="+visit.getDiancxxb_id();
		
		JDBCcon con=new JDBCcon();
		
		ResultSetList rsl=con.getResultSetList(huoh_qic);
		
		String shijname="zhongcsj";
		if(rsl.next()){
			shijname="qingcsj";
		}
		
		String tongjrq=" select * from xitxxb where mingc='计量报表统计日期'  and zhuangt=1 and leib='数量' and diancxxb_id="+visit.getDiancxxb_id();
		rsl=con.getResultSetList(tongjrq);
		
		String strTongjrq="daohrq";
		
		if(rsl.next()){
			strTongjrq=rsl.getString("zhi");
		}
		
		
		String xhjs_str=" select * from xitxxb where mingc='数量报表盈亏运损相互计算' and zhi='是' and leib='数量' and zhuangt=1 ";//盈亏运损是否通过相互计算得到
		rsl=con.getResultSetList(xhjs_str);
		
		String yuns_huoc_all=" sum(round_new(f.yuns,"+visit.getShuldec()+")) yuns,\n";//火车的 或者 所有运输方式的 运损 计算表达式
		
		String yuns_qic=" sum(round_new(f.yuns, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  ))  yuns,\n";//汽车的运损 计算表达式
	
		if(rsl.next()){
			
			 yuns_huoc_all=" sum(round_new(f.yingk,"+visit.getShuldec()+")) + sum(round_new(f.biaoz,"+visit.getShuldec()+")) " +
			"- sum(round_new(f.jingz,"+visit.getShuldec()+")) yuns,\n ";//火车的 或者 所有运输方式的 运损 计算表达式
	
			 yuns_qic=" sum(round_new(f.yingk, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  )) + " +
			" sum(round_new(f.biaoz, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  ))  -" +
					" sum(round_new(f.jingz, nvl((" + roundPoint_QIY_sql + "),"+ visit.getShuldec() + ")  ))  yuns,\n";//汽车的运损 计算表达式
	
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
					.append("(  select decode(grouping(d.fgsmc)+grouping(d.mingc)+grouping(g.mingc)+grouping(m.mingc),4,'总计', 3,decode(grouping(d.mingc)+grouping(m.mingc)+grouping(d.fgsmc),3,'"
							+ this.getMingc(this.getTreeid())
							+ "',d.mingc),d.mingc) as dianc,\n");

		} else {
			sb
					.append("(  select decode(grouping(d.fgsmc)+grouping(d.mingc)+grouping(g.mingc)+grouping(m.mingc),4,'总计', 3,decode(grouping(d.mingc)+grouping(m.mingc)+grouping(d.fgsmc),3,'"
							+ visit.getDiancxxb_id()
							+ "',d.mingc),d.mingc) as dianc,\n");
		}

		sb
				.append("decode(grouping(g.mingc)+grouping(m.mingc)-grouping(d.mingc),2,'合计',g.mingc) as fahdw, decode(grouping(m.mingc)-grouping(g.mingc)-grouping(d.mingc),1,'小计',m.mingc) as meikdw,z.mingc faz,p.mingc pinz, \n");

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
			// 汽车运输方式要参照系统信息表的值来更改小数点的保留位数

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
//			 火车运输方式要参照系统信息表的值来更改小数点的保留位数
			
			
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

	// 获取表表标题
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

	// 获取表日期
	public String getRiqTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		switch (visit.getInt1()) {
		case RPTTYPE_GH_ALL:
			sb = "过衡日期";
			break;
		case RPTTYPE_GH_HUOY:
			sb = "过衡日期";
			break;
		case RPTTYPE_GH_QIY:
			sb = "过衡日期";
			break;
		case RPTTYPE_SB_ALL:
			sb = "到货日期";
			break;
		case RPTTYPE_SB_HUOY:
			sb = "到货日期";
			break;
		case RPTTYPE_SB_QIY:
			sb = "到货日期";
			break;
		default:
			sb = "到货日期";
			break;
		}
		return sb;
	}

	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		/*
		 * if(visit.isFencb()) { tb1.addText(new ToolbarText("厂别:")); ComboBox
		 * changbcb = new ComboBox(); changbcb.setTransform("ChangbSelect");
		 * changbcb.setWidth(130);
		 * changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		 * tb1.addField(changbcb); tb1.addText(new ToolbarText("-")); }
		 */
		tb1.addText(new ToolbarText(getRiqTitle() + ":"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
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

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
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
			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
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

			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.setTitle(getRptTitle(), ArrWidth);
		}
		ArrWidth = new int[] { 100, 120, 70, 50, 50, 50, 50, 50, 50, 50, 50 };
		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "制表单位："
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, "报表日期：" + getRiq(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4, "单位：吨、车", Table.ALIGN_RIGHT);

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
		//	增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		// rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "制表："+getZhibr(), Table.ALIGN_LEFT);
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
			String str = MainGlobal.getXitxx_item("数量", "是否显示速度统计栏", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
			if (str.equals("是")) {
				
				String sql = 
					"select case\n" +
					"         when to_char(temp.jib) = '1' then '1Km以下'\n" + 
					"         when to_char(temp.jib) = '2' then '1Km至15Km'\n" + 
					"         when to_char(temp.jib) = '3' then '15Km以上'\n" + 
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
				
				int least = 0;		// 保存过衡车速是1KM以下的车数
				int middle = 0;		// 保存过衡车速是1KM至15KM之间的车数
				int most = 0;		// 保存过衡车速是15KM以上的车数
				
				Table tb_ches = new Table(4, 2);
				String[][] ArrHeader_ches = new String[1][2];
				ArrHeader_ches[0] = new String[]{"车速","车数"};
				int[] ArrWidth_ches = new int[] {140, 140};
				
				tb_ches.setCellValue(2, 1, "1Km以下");
				tb_ches.setCellValue(3, 1, "1Km至15Km");
				tb_ches.setCellValue(4, 1, "15Km以上");
				
				while(rsldata.next()) {
					if(rsldata.getString("fanw").equals("1Km以下")) {
						least = Integer.parseInt(rsldata.getString("ches")) ;
					} else if(rsldata.getString("fanw").equals("1Km至15Km")) {
						middle = Integer.parseInt(rsldata.getString("ches"));
					} else {
						most = Integer.parseInt(rsldata.getString("ches"));
					}
				}
				
				tb_ches.setCellValue(2, 2, String.valueOf(least));
				tb_ches.setCellValue(3, 2, String.valueOf(middle));
				tb_ches.setCellValue(4, 2, String.valueOf(most));
				
				rt_ches.setTitle("速度统计栏", ArrWidth_ches);
				rt_ches.setBody(tb_ches);
				rt_ches.body.setWidth(ArrWidth_ches);
				rt_ches.body.setHeaderData(ArrHeader_ches);
				rt_ches.body.setColAlign(1, Table.ALIGN_CENTER);
				rt_ches.body.setColAlign(2, Table.ALIGN_CENTER);
				rt_ches.setDefaultTitle(1, 2, "单位：Km、车", Table.ALIGN_RIGHT);
				rt_ches.body.ShowZero = true;
				
				rsldata.close();
				strsb.append(rt_ches.getAllPagesHtml());
			}
		}
		con.Close();
		return strsb.toString();
	}

	// 工具栏使用的方法
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

	// 页面初始化
	
	
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			if (reportType == null) {
				visit.setInt1(RPTTYPE_GH_ALL);
			}
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
			getGongysDropDownModels();	
			setTreeid(null);
			setChangbValue(null);
			setChangbModel(null);
			getSelectData();
		}
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// 页面登陆验证
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

	// 获取供应商
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
		setGongysDropDownModel(new IDropDownModel(sql, "全部"));
		return;
	}

	//	工具栏使用的方法
	//工具栏使用的方法
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

	//	-------------------------电厂Tree-----------------------------------------------------------------

}