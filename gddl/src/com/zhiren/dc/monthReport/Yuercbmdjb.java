package com.zhiren.dc.monthReport;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2010-03-12
 * 修改内容:当修改本月记录的 费用时  ，累计字段同样自动计算。
 */
/*
 * 作者：王磊
 * 时间：2009-12-09
 * 描述：重写了大唐国际的入厂标煤单价的算法
 */
/*
 * 作者：王磊
 * 时间：2009-11-09
 * 描述：增加删除按钮
 */
/*
 * 作者：王磊
 * 时间：2009-11-09
 * 描述：增加月报生成系统参数的判断 默认为ZGDT
 * mingc = '月报单位'
 * zhi = 'DTGJ'
 * diancxxb_id = 电厂ID
 * leib = '月报'
 */
/*
 * 作者：王磊
 * 时间：2009-06-29
 * 描述：修改煤价、煤价税、交货前杂费、运价、运价税、杂费为可编辑项
 */
/*
 * 作者：夏峥
 * 时间：2011-05-16
 * 描述：修改生成方法中数据源getSql_ZGDT的取值方式用以在不改变逻辑的情况下增快查询速度
 * 		 生成时累计值的取值为当年1月1日至今的数据
 */
/*
 * 作者：夏峥
 * 日期：2011-12-20
 * 描述：取消全部界面自动刷新功能，用户需手动点击刷新按钮才可刷新数据
 */
/*
 * 作者：夏峥
 * 日期：2011-12-22
 * 描述：修正界面显示错误
 */
/*
 * 作者：夏峥
 * 日期：2011-12-23
 * 描述：调整生成数据时的判断语句
 * 
 */
/*
 * 作者：赵胜男
 * 日期：2012-1-10
 * 描述：修正结算标煤单价填报界面中当结算热值为0时，js计算异常的问题
 *       修正入厂标煤单价界面煤价税不可编辑，标题煤价变为含税煤价，交货前杂费增加宽度，发电量变为MJ/kg
 */
/*
 * 作者：夏峥
 * 日期：2012-01-11
 * 描述：重写保存和生成方法
 * 
 */
public class Yuercbmdjb extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
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
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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

	private void Save() {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=this.getExtGrid();
		StringBuffer sql = new StringBuffer("begin \n");
		String strchange=this.getChange();
		String tableName="yuercbmdj";
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			sql.append("update ").append(tableName).append(" set ")
				.append("fenx='").append(mdrsl.getString("FENX"))
				.append("',meij=").append(mdrsl.getString("MEIJ"))
				.append(",meijs=").append(mdrsl.getString("MEIJS"))
				.append(",jiaohqzf=").append(mdrsl.getString("JIAOHQZF"))
				.append(",yunj=").append(mdrsl.getString("YUNJ"))
				.append(",yunjs=").append(mdrsl.getString("YUNJS"))
				.append(",daozzf=").append(mdrsl.getString("DAOZZF"))
				.append(",zaf=").append(mdrsl.getString("ZAF"))
				.append(",qit=").append(mdrsl.getString("QIT"))
				.append(",qnet_ar=").append(mdrsl.getString("QNET_AR"))
				.append(",biaomdj=").append(mdrsl.getString("BIAOMDJ"))
				.append(",buhsbmdj=").append(mdrsl.getString("BUHSBMDJ"))
				.append(",laimsl=").append(mdrsl.getString("LAIMSL"))
			    .append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
		}
		sql.append("end;");
		int flag=con.getUpdate(sql.toString());
		if(flag>=0){
//			计算累计数
			mdrsl.beforefirst();
			sql.setLength(0);
			while (mdrsl.next()) {
				if ("累计".equals(mdrsl.getString("fenx"))) {
					String sq = "select\n"
							+ "round_new(sum(laimsl),0) as laimsl,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(meij*laimsl)/sum(laimsl)),2) meij,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(meijs*laimsl)/sum(laimsl)),2) meijs,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(yunj*laimsl)/sum(laimsl)),2) yunj,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(yunjs*laimsl)/sum(laimsl)),2) yunjs,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(daozzf*laimsl)/sum(laimsl)),2) daozzf,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(zaf*laimsl)/sum(laimsl)),2) zaf,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(qit*laimsl)/sum(laimsl)),2) qit,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(qnet_ar*laimsl)/sum(laimsl)),2) qnet_ar,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(biaomdj*laimsl)/sum(laimsl)),2) biaomdj,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(buhsbmdj*laimsl)/sum(laimsl)),2) buhsbmdj,\n"
							+ "round_new(decode(sum(laimsl),0,0,sum(jiaohqzf*laimsl)/sum(laimsl)),2) jiaohqzf\n"
							+ "  from yuercbmdj y,yuetjkjb yt,gongysb g,jihkjb j,pinzb p\n"
							+ " where y.yuetjkjb_id=yt.id\n"
							+ "	and yt.gongysb_id=g.id\n"
							+ "	AND yt.pinzb_id=p.id\n"
							+ "	and g.mingc='"
							+ mdrsl.getString("gongysb_id")
							+ "'\n"
							+ "	and j.mingc='"
							+ mdrsl.getString("jihkjb_id")
							+ "'\n"
							+ "	and p.mingc='"
							+ mdrsl.getString("pinzb_id")
							+ "'\n"
							+ "   and yt.riq>=to_date('"
							+ getNianf()
							+ "-01-01"
							+ "','yyyy-mm-dd')\n"
							+ "   and yt.riq<=to_date('"
							+ getNianf()
							+ "-"
							+ getYuef()
							+ "-01"
							+ "','yyyy-mm-dd')\n"
							+ "   and y.fenx='"
							+ SysConstant.Fenx_Beny
							+ "'\n"
							+ "   and yt.diancxxb_id="
							+ getTreeid();
					
					ResultSetList rs = con.getResultSetList(sq);
					rs.next();
					sql.append("update yuercbmdj set " + "laimsl = "
							+ rs.getDouble("laimsl") + ",meij = "
							+ rs.getDouble("meij") + ",meijs = "
							+ rs.getDouble("meijs") + ",yunj = "
							+ rs.getDouble("yunj") + ",yunjs = "
							+ rs.getDouble("yunjs") + ",daozzf = "
							+ rs.getDouble("daozzf") + ",zaf = "
							+ rs.getDouble("zaf") + ",qit = "
							+ rs.getDouble("qit") + ",qnet_ar = "
							+ rs.getDouble("qnet_ar") + ",biaomdj = "
							+ rs.getDouble("biaomdj") + ",buhsbmdj = "
							+ rs.getDouble("buhsbmdj") + ",jiaohqzf = "
							+ rs.getDouble("jiaohqzf")
							+ " where yuetjkjb_id ="
							+ mdrsl.getString("yuetjkjb_id") + " and fenx = '"
							+ mdrsl.getString("fenx") + "';");
				}
			}
			if("".equals(sql) || sql==null){
				setMsg("保存成功!");
			}else{
				flag = con.getUpdate("begin\n" + sql.toString() + "\n end;");
				if (flag > 0) {
					setMsg("保存成功!");
				} else {
					setMsg("保存成功,累计数计算失败!");
				}
			}
		}
		con.Close();
		sql=null;
	}
	
	public void DelData() {
		JDBCcon con = new JDBCcon();
		String strDate[] = this.getRiq2().split("-");
		String CurrZnDate = strDate[0] + "年" + strDate[1] + "月";
		String CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-"
				+ strDate[1] + "-01");
		String strSql = "delete from yuercbmdj where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ getTreeid()
				+ ")";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(CurrZnDate + "的数据被成功删除！");
		}
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}

		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		getSelectData();
	}

	private String getSql_ZGDT(String diancxxb_id,long tjkjid, String fenx){
		String EndDate = "";
		String BeginDate ="";

		Visit visit = (Visit) getPage().getVisit();
		
		if(visit.getString10().equals(strParam)){
			EndDate=DateUtil.FormatOracleDate(this.getRiq2());
			BeginDate = DateUtil.FormatOracleDate(this.getRiq1());
			String strDate[] =this.getRiq2().split("-");
			if(fenx.equals(SysConstant.Fenx_Leij)){
				BeginDate = DateUtil.FormatOracleDate(strDate[0]+"-01-01");
			}
		}else{
			EndDate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
			BeginDate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
			if(fenx.equals(SysConstant.Fenx_Leij)){
				BeginDate = DateUtil.FormatOracleDate(getNianf()+"-01-01");
			}
		}
		
		String sql =
			"insert into yuercbmdj\n" +
			"  (id,\n" + 
			"   fenx,\n" + 
			"   yuetjkjb_id,\n" + 
			"   laimsl,meij,\n" + 
			"   meijs,\n" + 
			"   jiaohqzf,\n" + 
			"   yunj,\n" + 
			"   yunjs,\n" + 
			"   daozzf,\n" + 
			"   zaf,\n" + 
			"   qit,\n" + 
			"   qnet_ar,\n" + 
			"   biaomdj,\n" + 
			"   buhsbmdj)\n" + 
			"  (select getnewid("+diancxxb_id+") id,\n" + 
			"          nvl('"+fenx+"', '') as fenx,\n" + 
			"          t.id, \n"+
			"		   nvl(a.laimsl,0)laimsl,\n" + 
			"          nvl(a.meij, 0) meij,\n" + 
			"          nvl(a.meijs, 0) meijs,\n" + 
			"          nvl(a.fazzf, 0) jiaohqzf,\n" + 
			"          nvl(a.yunj, 0) yunj,\n" + 
			"          nvl(a.yunjs, 0) yunjs,\n" + 
			"          nvl(a.daozzf, 0) daozzf,\n" + 
			"          nvl(a.zaf, 0) zaf,\n" + 
			"          nvl(a.ditf, 0) qit,\n" + 
			"          z.qnet_ar,\n" + 
			"          nvl(decode(z.qnet_ar,\n" + 
			"                 0,\n" + 
			"                 0,\n" + 
			"                 round_new((a.meij + a.yunj + a.fazzf + a.zaf + a.ditf) *\n" + 
			"                           29.271 / z.qnet_ar,\n" + 
			"                           2)),0) bmdj,\n" + 
			"          nvl(decode(z.qnet_ar,\n" + 
			"                 0,\n" + 
			"                 0,\n" + 
			"                 round_new((a.meij - a.meijs + a.yunj - a.yunjs + a.fazzf +\n" + 
			"                           a.zaf + a.ditf) * 29.271 / z.qnet_ar,\n" + 
			"                           2)),0) bmdjbhs\n"+
		    "     from (select f.diancxxb_id,\n" + 
			"                  m.meikdq_id,\n" + 
			"                  f.jihkjb_id,\n" + 
			"                  f.pinzb_id,\n" + 
			"                  f.yunsfsb_id,\n" + 
			"                  nvl(decode(sum(f.laimsl),\n" + 
			"                             0,\n" + 
			"                             0,\n" + 
			"                             round_new(sum(f.laimsl * jg.meij) /\n" + 
			"                                       sum(f.laimsl),\n" + 
			"                                       2)),\n" + 
			"                      0) meij,\n" + 
			"                  nvl(decode(sum(f.laimsl),\n" + 
			"                             0,\n" + 
			"                             0,\n" + 
			"                             round_new(sum(f.laimsl * jg.meijs) /\n" + 
			"                                       sum(f.laimsl),\n" + 
			"                                       2)),\n" + 
			"                      0) meijs,\n" + 
			"                  nvl(decode(sum(f.laimsl),\n" + 
			"                             0,\n" + 
			"                             0,\n" + 
			"                             round_new(sum(f.laimsl * jg.fazzf) /\n" + 
			"                                       sum(f.laimsl),\n" + 
			"                                       2)),\n" + 
			"                      0) fazzf,\n" + 
			"                  nvl(decode(sum(f.laimsl),\n" + 
			"                             0,\n" + 
			"                             0,\n" + 
			"                             round_new(sum(f.laimsl * jg.yunj) /\n" + 
			"                                       sum(f.laimsl),\n" + 
			"                                       2)),\n" + 
			"                      0) yunj,\n" + 
			"                  nvl(decode(sum(f.laimsl),\n" + 
			"                             0,\n" + 
			"                             0,\n" + 
			"                             round_new(sum(f.laimsl * jg.yunjs) /\n" + 
			"                                       sum(f.laimsl),\n" + 
			"                                       2)),\n" + 
			"                      0) yunjs,\n" + 
			"                  0 daozzf,\n" + 
			"                  nvl(decode(sum(f.laimsl),\n" + 
			"                             0,\n" + 
			"                             0,\n" + 
			"                             round_new(sum(f.laimsl * jg.zaf) / sum(f.laimsl),\n" + 
			"                                       2)),\n" + 
			"                      0) zaf,\n" + 
			"                  nvl(decode(sum(f.laimsl),\n" + 
			"                             0,\n" + 
			"                             0,\n" + 
			"                             round_new(sum(f.laimsl * jg.ditf) /\n" + 
			"                                       sum(f.laimsl),\n" + 
			"                                       2)),\n" + 
			"                      0) ditf,\n" +
			"				sum(f.laimsl) laimsl" + 
			"             from (" +
			"SELECT F.ID,\n" +
			"       GS.MEIJ + GS.MEIS MEIJ,\n" + 
			"       GS.MEIS MEIJS,\n" + 
			"       GS.FAZZF FAZZF,\n" + 
			"       GS.YUNF + GS.YUNFS YUNJ,\n" + 
			"       GS.YUNFS YUNJS,\n" + 
			"       GS.ZAF ZAF,\n" + 
			"       GS.DITF DITF\n" + 
			"  FROM FAHB F,\n" + 
			"       (SELECT GS.FAHB_ID,GS.MEIJ,GS.MEIS,GS.FAZZF,GS.YUNF,GS.YUNFS, GS.ZAF ,GS.DITF\n" + 
			"          FROM (SELECT MAX(leix) leix,MAX(ID) ID, FAHB_ID FROM GUSLSB GROUP BY FAHB_ID) G,\n" + 
			"               GUSLSB GS\n" + 
			"         WHERE G.ID = GS.ID) GS\n" + 
			" WHERE F.DAOHRQ >= "+BeginDate+"\n" + 
			"   AND F.DAOHRQ <= "+EndDate+"\n" +
			"   AND GS.FAHB_ID(+) = F.ID"+
			"                   ) jg,\n" + 
			"                  fahb f,\n" + 
			"                  meikxxb m\n" + 
			"            where f.id = jg.id\n" + 
			"              and f.meikxxb_id = m.id\n" + 
			"            group by f.diancxxb_id,\n" + 
			"                     m.meikdq_id,\n" + 
			"                     f.jihkjb_id,\n" + 
			"                     f.pinzb_id,\n" + 
			"                     f.yunsfsb_id) a,\n" + 
			"          yuetjkjb t,\n" + 
			"          yuezlb z\n" + 
			"    where t.id = z.yuetjkjb_id\n" + 
			"      and z.fenx = '"+fenx+"'\n" + 
			"      and t.diancxxb_id = a.diancxxb_id(+)\n" + 
			"      and t.gongysb_id = a.meikdq_id(+)\n" + 
			"      and t.jihkjb_id = a.jihkjb_id(+)\n" + 
			"      and t.pinzb_id = a.pinzb_id(+)\n" + 
			"      and t.yunsfsb_id = a.yunsfsb_id(+)\n" + 
			"      and t.id = '"+tjkjid+"')";

		return sql;
	}

	public void CreateData() {
		JDBCcon con = new JDBCcon();
		String strDate [] = this.getRiq2().split("-");
		String diancxxb_id=this.getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(strDate[0]+"-"+strDate[1]+"-01");
		String strSql = "";
		if(Cleaning_up_History(this.getRiq1(),this.getRiq2(),"")){
			strSql = "select id from yuetjkjb where diancxxb_id = " + diancxxb_id +
				" and riq = " +CurrODate+"";
				ResultSetList rs=con.getResultSetList(strSql);
				while(rs.next()){
					con.getInsert(getSql_ZGDT(diancxxb_id,rs.getLong("id"),SysConstant.Fenx_Beny));
					con.getInsert(getSql_ZGDT(diancxxb_id,rs.getLong("id"),SysConstant.Fenx_Leij));
				}
				rs.close();
			setMsg("数据生成成功!");
			con.Close();
		}
	}
	
	private boolean getZhangt(JDBCcon con){
		Visit visit=(Visit)getPage().getVisit();
		String CurrODate = "";
		String sj[] = this.getRiq2().split("-");
		if(visit.getString10().equals(strParam)){
			CurrODate=DateUtil.FormatOracleDate(sj[0] + "-" + sj[1]+ "-01");
		}else{
			CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		}
		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from yuehcb s, yuetjkjb k\n" + 
			" where s.yuetjkjb_id = k.id\n" + 
			"   and k.diancxxb_id = "+getTreeid()+"\n" + 
			"   and k.riq = "+CurrODate;
		ResultSetList rs=con.getResultSetList(sql);
		boolean zt=true;
		if(con.getHasIt(sql)){
			while(rs.next()){
				if(rs.getInt("zhuangt")==0||rs.getInt("zhuangt")==2){
					zt=false;
				}
			}
		}else{
			zt=false;
		}
		return zt;
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strDate[] = this.getRiq2().split("-");
		String CurrODate = DateUtil.FormatOracleDate(strDate[0] + "-"
				+ strDate[1] + "-01");
		String diancxxb_id=this.getTreeid();
		String strSql = "";
		strSql =
		// hetj,,relzj,liufzj,huifzj,huiffzj,shuifzj,
		"select rcdj.id,tj.id as yuetjkjb_id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,yunsfsb.mingc as yunsfsb_id,\n"
				+ "        fenx,nvl(laimsl,0) laimsl,meij,meijs,jiaohqzf,yunj,yunjs,daozzf,zaf,qit,qnet_ar,biaomdj,buhsbmdj\n"
				+ "from yuetjkjb tj,yuercbmdj rcdj,gongysb,jihkjb,pinzb,yunsfsb\n"
				+ "where tj.id=rcdj.yuetjkjb_id and tj.gongysb_id=gongysb.id and tj.jihkjb_id=jihkjb.id\n"
				+ "      and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and diancxxb_id="
				+ diancxxb_id + " and riq=" +CurrODate + "\n" +
				// " and rcdj.fenx='"+SysConstant.Fenx_Beny+"'\n"+ //页面上只显示本页的数据。
				"order by rcdj.id";

		JDBCcon con = new JDBCcon();

		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:"
					+ strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuercbmdj");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");

		egu.getColumn("yuetjkjb_id").setHeader("月统计口径表ID");
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("yuetjkjb_id").setWidth(60);
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setWidth(70);
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setWidth(60);
		
		egu.getColumn("LAIMSL").setHeader("入厂数量");
		egu.getColumn("LAIMSL").setEditor(null);
		egu.getColumn("LAIMSL").setWidth(60);
		
		egu.getColumn("meij").setHeader("含税煤价");
//		egu.getColumn("meij").setEditor(null);
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meijs").setHeader("煤价税");
		egu.getColumn("meijs").setEditor(null);
		egu.getColumn("meijs").setWidth(60);
		
		egu.getColumn("JIAOHQZF").setHeader("交货前杂费");
//		egu.getColumn("JIAOHQZF").setEditor(null);
		egu.getColumn("JIAOHQZF").setWidth(100);
		
		egu.getColumn("yunj").setHeader("含税运价");
//		egu.getColumn("yunj").setEditor(null);
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunjs").setHeader("运价税");
     	egu.getColumn("yunjs").setEditor(null);
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);
		
		egu.getColumn("zaf").setHeader("杂费");
//		egu.getColumn("zaf").setEditor(null);
		egu.getColumn("zaf").setWidth(60);
		
		egu.getColumn("qit").setHeader("其他");
		egu.getColumn("qit").setWidth(60);
		
		egu.getColumn("qnet_ar").setHeader("发热量(MJ/kg)");
//		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("qnet_ar").setWidth(100);
		
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("biaomdj").setEditor(null);
		egu.getColumn("biaomdj").setWidth(70);
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		egu.getColumn("buhsbmdj").setEditor(null);
		egu.getColumn("buhsbmdj").setWidth(90);

		egu.setDefaultsortable(false);

		// 把所有列改为可以编辑的。
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		// /设置按钮
		if(visit.getString10().equals(strParam)){
	    //	egu.addOtherScript(sb1.toString());
	    	egu.addTbarText("时间:");
	    	DateField df = new DateField();
	    	df.setValue(this.getRiq1());
	    	df.Binding("RIQ1", "");
	    	df.setId("riq1");
	    	egu.addToolbarItem(df.getScript());
		
	    	egu.addTbarText("-");
	    	egu.addTbarText("至:");
	    	DateField df1 = new DateField();
	    	df1.setValue(this.getRiq2());
	    	df1.Binding("RIQ2", "");
	    	df1.setId("riq2");
	    	egu.addToolbarItem(df1.getScript());
	    	egu.addTbarText("-");
		}else{
		egu.addTbarText("年份");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("月份");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		}
		// 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'正在刷新"+strDate[0]+"年"+strDate[1]+"月的数据,请稍候！'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
		if(getZhangt(con)){
			setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
		}else{
			// 生成按钮
			GridButton gbc = new GridButton("生成",
					getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
			GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
			// 保存按钮
			GridButton gbs = new GridButton(GridButton.ButtonType_SaveAll, "gridDiv",
					egu.getGridColumns(), "SaveButton");
			// gbs.setHandler("function
			// (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
			egu.addTbarBtn(gbs);
			// 打印按钮
			GridButton gbp = new GridButton("打印", "function (){"
					+ MainGlobal.getOpenWinScript("MonthReport&lx=yuercbmdjb")
					+ "}");
			gbp.setIcon(SysConstant.Btn_Icon_Print);
			egu.addTbarBtn(gbp);
		}
		StringBuffer sb = new StringBuffer();
		String meijs = MainGlobal.getXitxx_item("结算", "煤款税率", this.getTreeid(), "0.17");
		String yunjs = MainGlobal.getXitxx_item("结算", "运费税率", this.getTreeid(), "0.07");
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n" + 
				"  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjs + ",2);\n" + 
				"    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n" + 
				"\n" + 
				"  if(e.field=='MEIJ'||e.field=='MEIJS'||e.field=='YUNJ'||e.field=='YUNJS'||e.field=='DAOZZF'||e.field=='ZAF'||e.field=='QIT'||e.field=='QNET_AR'){\n" + 
				"\n" + 
				"    var daoczhj=0,biaomdj=0,buhsbmdj=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    daoczhj=eval(gridDiv_ds.getAt(i).get('MEIJ')||0)+eval(gridDiv_ds.getAt(i).get('JIAOHQZF')||0)+eval(gridDiv_ds.getAt(i).get('YUNJ')||0)+eval(gridDiv_ds.getAt(i).get('DAOZZF')||0)+eval(gridDiv_ds.getAt(i).get('ZAF')||0)+eval(gridDiv_ds.getAt(i).get('QIT')||0);\n"+ 
				"    biaomdj=Round_new(daoczhj*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"    buhsbmdj=Round_new((daoczhj-eval(gridDiv_ds.getAt(i).get('MEIJS')||0)-eval(gridDiv_ds.getAt(i).get('YUNJS')||0))*29.271/eval(gridDiv_ds.getAt(i).get('QNET_AR')||0),2);\n" + 
				"\n" + 
				"    gridDiv_ds.getAt(i).set('BIAOMDJ',biaomdj);\n" + 
				"    gridDiv_ds.getAt(i).set('BUHSBMDJ',buhsbmdj);\n" + 
				"  }\n" + 
				
				"});"
		);
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('FENX')=='累计'){e.cancel=true;}");//当"累计"时,这一行不允许编辑
		sb.append("});");
	
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String strDate[] = this.getRiq2().split("-");
		String cnDate = strDate[0] + "年" + strDate[1] + "月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
//-----------------  这里用来释放记录的   ----------------------------------------------------
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String strDate = cycle.getRequestContext().getParameter("lx");
		if(strDate!=null){
			visit.setString10(strDate);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			this.setTreeid(null);
			visit.setString11("");
			visit.setString9("");
			setRiq();
			getSelectData();
		}
		getSelectData();
	}

	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ getTreeid();
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	// 得到电厂的默认到站
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

	boolean treechange = false;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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

	// ---------------------------------------------------------------------------------------
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}

	// 年份下拉框
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
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

	// 月份下拉框
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
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
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
	
	private boolean Cleaning_up_History(String Date1,String Date2,String Condition){
//		当用户点击“生成”按钮时，要求先删除两个表的数据，才能重新计算
//		当用户点击“保存”按钮时，要求先将累计数据删除，再从新计算累计值
		JDBCcon con = new JDBCcon();
		String sql="";
		boolean Falg=false;
		String diancxxb_id=this.getTreeid();
		
		sql=
				"delete from yuercbmdj where id in (\n" +
				"    select js.id\n" + 
				"       from yuercbmdj js,yuetjkjb kj\n" + 
				"       where js.yuetjkjb_id=kj.id\n" + 
				"			  and kj.diancxxb_id=" +diancxxb_id+"\n"+
				"             and kj.riq>="+DateUtil.FormatOracleDate(Date1)+" and kj.riq<="+DateUtil.FormatOracleDate(Date2)+"\n" +Condition+ 
				")";
		
		if(con.getDelete(sql)>=0){
			
			Falg=true;
		}
		con.Close();
		return Falg;
	}
	public String getRiq1(){
	       if(((Visit) this.getPage().getVisit()).getString11()==null||((Visit) this.getPage().getVisit()).getString11().equals("")){
				
				((Visit) this.getPage().getVisit()).setString11(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString11();
		}
		public void setRiq1(String riq1){
	         if(((Visit) this.getPage().getVisit()).getString11()!=null &&!((Visit) this.getPage().getVisit()).getString11().equals(riq1)){
				
				((Visit) this.getPage().getVisit()).setString11(riq1);
			}
		}
		
		public String getRiq2(){
	         if(((Visit) this.getPage().getVisit()).getString9()==null||((Visit) this.getPage().getVisit()).getString9().equals("")){
				
				((Visit) this.getPage().getVisit()).setString9(DateUtil.FormatDate(new Date()));
			}
			return ((Visit) this.getPage().getVisit()).getString9();
		}
		public void setRiq2(String riq2){
	        if(((Visit) this.getPage().getVisit()).getString9()!=null &&!((Visit) this.getPage().getVisit()).getString9().equals(riq2)){
				
				((Visit) this.getPage().getVisit()).setString9(riq2);
			}
		}
}