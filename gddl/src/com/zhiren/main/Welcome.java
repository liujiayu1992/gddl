package com.zhiren.main;
/*
 * 时间：2008-12-29
 * 作者：chh
 * 关键字：Welcome,首页，getShouhChart
 * 描述：收耗对比图表在集团账号进入时多显示了燃料公司的数据
 * 修改 getShouhChart 的集团对应的电厂过滤条件
 */

/*
 * 时间：2008-12-21
 * 作者：chh
 * 关键字：Welcome,首页
 * 描述：在通知、文件后加入市场动态连接包含
 *  秦皇港岛煤炭市场行情价格周报 
 * 	环球煤炭电子交易平台NEWC指数 
 * 	国际原油价格速览 
 * 在系统信息中增加'首页显示市场动态链接'设置值为"是"，则显示市场动态信息
 */

/*
 * 时间：2008-09-09
 * 作者：chh
 * 关键字：Welcome,首页
 * 描述：图表选项卡，增加重要通知div 默认显示，重要通知，
 * 在系统设置中可以设置
 */
/*
 * 时间：2008-09-10
 * 作者：chh
 * 关键字：Welcome,首页
 * 描述：重要通知按发布时间从后到前排序
 * 在系统设置中可以设置
 */
/*
 * 时间：2008-09-26
 * 作者：chh
 * 关键字：Welcome,首页
 * 描述：快捷菜单按增加的先后顺序显示
 * 在系统设置中可以设置
 */
/*
 * 时间：2009-04-23
 * 作者：ly
 * 关键字：Welcome,首页，getZuorshc()
 * 描述：昨日来耗存中的数据，集团登陆时保留整数，公司和电厂登陆时保留2位有效数字
 * 修改: getZuorshc()方法
 */
/*
* 时间：2009-12-04
* 作者：chh
* 关键字：Welcome,首页
* 描述：一厂多制下用总厂登录可以收耗存显示总厂收耗存，图表数据,
* 
*/
/*
 * 时间:2010-01-28
 * 作者:tzf
 * 修改内容:主页显示时  增加 弹出框 消息提示。
 */
/*
 * 时间:2010-01-28
 * 作者:tzf
 * 修改内容:主页显示时  弹出框 消息中的入炉煤量 改成  入炉质量。
 */
/*
 * 时间:2010-03-21
 * 作者:tzf
 * 修改内容:主页显示时  弹出框 消息中的shij  改成 riq字段。
 * 
 */
/*
 * 时间:2011-06-08
 * 作者:夏峥
 * 描述:修改煤炭库存走势图
 * 		显示前90天的每周3的库存情况
 * 
 */
/*
 * 作者：王磊
 * 时间：2011-06-27
 * 描述：解决在IE7\8\9下显示不了库存等图片的问题。
 * 		修改在java中生成的js获得对象的语法
 * 		由document.all.item(...)改为document.getElementById(...)
 */
/*
 * 作者：夏峥
 * 时间：2013-01-25
 * 描述：动态获得下载地址
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.jfree.data.category.CategoryDataset;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public abstract class Welcome extends BasePage {
	private int msg = 1;

	public int getMsg() {
		return msg;
	}

	public void setMsg(int msg) {
		this.msg = msg;
	}

	public String getUserDanwmc(){
		Visit visit = (Visit)getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		return visit.getDiancqc();
	}
	
	public String getQuickStart() {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbhtml = new StringBuffer();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		//chh 2008-09-26 排序按增加的先后顺序排列
		sb.append("select z.id,z.wenjwz,z.mingc from ziyxxb z, quickstart q \n")
		.append("where z.id = q.ziyxxb_id and q.renyxxb_id = ")
		.append(visit.getRenyID()).append(" order by q.id");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		
		String sql = "select distinct x.zhi from xitxxb x where x.mingc='快速通道名称'";
		String strBeiymc = "备用通道";
		ResultSetList rsmc = con.getResultSetList(sql);
		if(rsmc.next()){
			strBeiymc = rsmc.getString("zhi");
		}
		
		sbhtml.append("<tr>");
		for(int i=1;i<9 ; i++) {
			if(i==5) {
				sbhtml.append("<td width=\"43%\">&nbsp;</td></tr><tr><td colspan=\"7\"><img src=\"imgs/startpage/spacer.gif\" width=\"1\" height=\"6\" /></td></tr><tr>");
			}else {
				//sbhtml.append("</tr><tr>");
			}
			if(i == 1 || i == 5) {
				sbhtml.append("<td width=\"1%\" align=\"right\"><img src=\"imgs/startpage/start_page_link_icon.gif\" alt=\"\" width=\"16\" height=\"16\" /></td>");
			}else {
				sbhtml.append("<td width=\"8%\" align=\"right\"><img src=\"imgs/startpage/start_page_link_icon.gif\" alt=\"\" width=\"16\" height=\"16\" /></td>");
			}
			if( rsl!= null && rsl.next()) {
				
				sbhtml.append("<td width=\"10%\" nowrap=\"nowrap\">").append("<a href=\"#\"  title='单击打开' id=Node")
				.append(rsl.getString("id")).append(" url=\"").append(rsl.getString("wenjwz")).append("\"  onclick=\"QuickStart(this);\">")
				.append(rsl.getString("mingc")).append("</a></td>");
			}else {
				sbhtml.append("<td width=\"10%\" nowrap=\"nowrap\">").append("<a href=\"#\"  title='单击打开' id=Node")
				.append("").append(" url=\"").append("").append("\"  onclick=\"\">")
				.append(strBeiymc).append("</a></td>");
			}
		}
		sbhtml.append("<td>&nbsp;</td></tr>");
		con.Close();
		return sbhtml.toString();
	}
//	public String addtd() {
//		
//	}
	
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
		}	
	}
	
//	得到电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(long diancmcId) {
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	public String getShouhChart(){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		StringBuffer sbsql = new StringBuffer();
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];
		
		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		
		String diancid = "";
		String groupid = "";
		String dcmc = "";
		String zhi = "否";
		String idDianc = "";
		
		if(isJitUserShow()){//选集团时刷新出所有的电厂
			diancid = " ";
			dcmc = " dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" and dc.ranlgs<>1  ";
			groupid = "dc.fuid";
		}else if (isGongsUser()){//选分公司的时候刷新出分公司下所有的电厂
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" or  dc.shangjgsid="+((Visit) getPage().getVisit()).getDiancxxb_id()+") ";
			dcmc = " (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" or  dc.shangjgsid="+((Visit) getPage().getVisit()).getDiancxxb_id()+") ";
			groupid = "dc.id";
		}else{//选电厂只刷新出该电厂
			zhi = MainGlobal.getXitsz("电厂柱状图是否显示煤矿",String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()) , "是");
			if (zhi.equals("是")){
				idDianc = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
				if (IsZongc(cn ,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
					diancid = " and dc.id in(select id from diancxxb where fuid in(select id from diancxxb where id=" + idDianc + ")) \n";
				}else{
					diancid = " and dc.id=" + idDianc + " \n";
				}

			}else{
				if (IsZongc(cn,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
					diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id();
					dcmc = " dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
				}else{
					diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
					dcmc = " dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				}
				
				groupid = "dc.id";
			}
		}
		
//		柱状图
		if (zhi.equals("是")){
			sbsql.append("select quanc||' '||mingc as mingc,fenx,laimsl shuj from   \n");
			sbsql.append(" (select dc.mingc as quanc,m.mingc, decode(1,1,'供应','') as fenx,sum(f.laimsl) as laimsl \n");
			sbsql.append(" from fahb f, meikxxb m,diancxxb dc \n");
			sbsql.append("where f.meikxxb_id = m.id \n");
			sbsql.append("and f.daohrq = to_date('" + strDate + "', 'yyyy-mm-dd') \n");
			sbsql.append("and dc.id=f.diancxxb_id \n");
			sbsql.append(diancid);
			sbsql.append("group by dc.mingc,m.mingc) s order by s.quanc");
		}else{
			sbsql.append("select fx.mingc,fx.fenx,nvl(sj.shul,0) as shuj from   \n");
			sbsql.append("(select dc.mingc as danw,fenx,shul from   \n");
			sbsql.append("(select "+groupid+" as danwid,decode(1,1,'供应','') as fenx,sum(nvl(dangrgm,0)) as shul from shouhcrbb rb,diancxxb dc   \n");
			sbsql.append("where rb.riq=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=dc.id "+diancid+" group by ("+groupid+")  \n");
			sbsql.append("union  \n");
			sbsql.append("select "+groupid+" as danwid,decode(1,1,'耗用','') as fenx,sum(nvl(haoyqkdr,0)) as shul from shouhcrbb rb,diancxxb dc  \n");
			sbsql.append("where rb.riq=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=dc.id "+diancid+" group by ("+groupid+") ) s,diancxxb dc where s.danwid=dc.id  \n");
			sbsql.append("order by fenx )sj,  \n");
			sbsql.append("(select dc.mingc,fx.fenx,fx.xuh from  \n");
			sbsql.append("(select mingc from diancxxb dc where "+dcmc+") dc, \n");
			sbsql.append("(select decode(1,1,'供应','') as fenx,1 as xuh from dual union select decode(1,1,'耗用','') as fenx,2 as xuh from dual)  fx ) fx \n");
			sbsql.append("where sj.danw(+)=fx.mingc and sj.fenx(+)=fx.fenx  \n");
			sbsql.append("order by mingc,fx.xuh \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		
		CategoryDataset dataset = cd.getRsDataChart(rs, "mingc", "fenx", "shuj");//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;//倾斜显示X轴的文字
		
		/*--------------设置图片参数结束-------------------*/
		
		//生成柱状图并显示到页面
		ArrHeader[0]=new String[] {""+ct.ChartBar3D(getPage(), dataset,DateUtil.Formatdate("yyyy年MM月dd日",dat)+ "煤炭收煤量图", 600, 295)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		
		cn.Close();

		return rt.getHtml();
	}
	
	public String getKucChart(){
		JDBCcon cn = new JDBCcon();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		Report rt=new Report();
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];

		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		
		long lngDays=90; //走势区间天数
		String WeekDay="4";//星期日为1，星期一为2,....以此类推，星期六为7
		String diancid = "";
		
		if(isJitUserShow()){
			diancid = " and dc.ranlgs<>1 ";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
		}else{//选电厂只刷新出该电厂
			if (IsZongc(cn,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
				diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" and dc.ranlgs<>1 ";
			}else{
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" and dc.ranlgs<>1 ";
			}
		}
		//陈环红 2008-12-24 构造行数的表有diancxxb 改为all_objects
		StringBuffer sbsql = new StringBuffer();
//		时序图
		sbsql.append("select  to_char(riq,'mm-dd') riq, fenx, kuc from (select * from  \n");
		sbsql.append(" (select bqrq.riq as riq,decode(1,1,'本期库存','') as fenx,round_new(nvl(kuc,0)/10000,2) as kuc from   \n");
		sbsql.append(" (select riq,sum(nvl(kuc,0)) as kuc  from shouhcrbb s,diancxxb dc   \n");
		sbsql.append("      where riq > to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+"   \n");
		sbsql.append("      and  riq <= to_date('"+strDate+"','yyyy-mm-dd') and s.diancxxb_id=dc.id "+diancid+" group by riq) bqsj,  \n");
		sbsql.append(" (SELECT riq, to_char(riq,'d') zhou FROM(select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from all_objects where rownum<="+lngDays+")WHERE to_char(riq,'d')="+WeekDay+") bqrq  \n");
		sbsql.append("   where bqrq.riq=bqsj.riq(+)   \n");
		sbsql.append(" union \n");
		sbsql.append("  select add_months(tqrq.riq,12) as riq,decode(1,1,'同期库存','') as fenx,round_new(nvl(kuc,0)/10000,2) as kuc from \n");
		sbsql.append(" (select riq,sum(nvl(kuc,0)) as kuc from shouhcrbb s,diancxxb dc   \n");
		sbsql.append("      where riq > add_months(to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+",-12)  \n");
		sbsql.append("      and  riq <= add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12) and s.diancxxb_id=dc.id "+diancid+" group by riq) tqsj,  \n");
		sbsql.append(" (SELECT add_months(riq,-12) riq, to_char(riq,'d') zhou FROM(select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from all_objects dc where rownum<="+lngDays+")WHERE to_char(riq,'d')="+WeekDay+") tqrq  \n");
		sbsql.append("  where tqrq.riq=tqsj.riq(+)   \n");
		sbsql.append("  ) order by riq ) \n");
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		
		CategoryDataset data2 = cd.getRsDataChart(rs,  "riq","fenx", "kuc");//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.xfontSize=9;		//	x轴字体大小
//		ct.dateApeakShowbln = false;//竖直显示x轴的日期
		ct.xTiltShow=false;  //x轴字体是否倾斜
//		ct.lineDateFormatOverride = "dd";//x轴的日期只显示天
		/*--------------设置图片参数结束-------------------*/		
		ArrHeader[0]=new String[] {""+ct.ChartXYLine(getPage(), data2, "煤炭库存走势图(前90天)", 600, 295)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		cn.Close();

		return rt.getHtml();
	}
	
	//是一厂多制的总厂，级别是3，并且有孩子
	private boolean IsZongc(JDBCcon cn,String strDiancID ){
		String sqlq="select id from diancxxb where fuid in(select id from diancxxb where id="+strDiancID+" and jib=3)";
		ResultSetList rs=new ResultSetList();
		rs=cn.getResultSetList(sqlq);
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
	
	public String getZuorshc(){
		
		JDBCcon con = new JDBCcon();
		ResultSetList rs;
		
		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		Date datmm=DateUtil.getFirstDayOfMonth(dat);//月初
		Date datyy=DateUtil.getFirstDayOfYear(dat);//年初
		String fdom=DateUtil.FormatDate(datmm);//日期字符
		String fdoy=DateUtil.FormatDate(datyy);//日期字符
		String diancid = "";
		String strMeil = "";
		String strDanw = "";
		
		
		if(isJitUserShow()){
			diancid = "";
			strMeil = "select (select dia.quanc from diancxxb dia where id="+((Visit) getPage().getVisit()).getDiancxxb_id() +") as quanc  ,round(c.laim/10000,0) as laim,round(c.haom/10000,0) as haom,round(c.kuc/10000,0) as kuc,round(a.nljlm/10000,0) as nlm,round(a.nljhm/10000,0) as nhm,round(b.yljlm/10000,0) as ylm,round(b.yljhm/10000,0) as yhm\n" ;
			strDanw = "万吨";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
			strMeil = "select (select dia.quanc from diancxxb dia where id="+((Visit) getPage().getVisit()).getDiancxxb_id() +") as quanc  ,round(c.laim/10000,2) as laim,round(c.haom/10000,2) as haom,round(c.kuc/10000,2) as kuc,round(a.nljlm/10000,2) as nlm,round(a.nljhm/10000,2) as nhm,round(b.yljlm/10000,2) as ylm,round(b.yljhm/10000,2) as yhm\n" ;
			strDanw = "万吨";
		}else{//选电厂只刷新出该电厂
			//在系统信息表中查询，如果是厂级，是否显示各矿来煤情况
			//是一厂多制显示，如果是总厂显示子厂的合计
			if (IsZongc(con,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
				diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}else{
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			
			strMeil = "select dc.quanc as quanc ,round(c.laim,2) as laim,round(c.haom,2) as haom,round(c.kuc,2) as kuc,round(a.nljlm/10000,2) as nlm,round(a.nljhm/10000,2) as nhm,round(b.yljlm,2) as ylm,round(b.yljhm,2) as yhm\n" ;
			strDanw = "吨";
		}
		
		
		String sql = strMeil +
//			"select round(c.laim/10000,0) as laim,round(c.haom/10000,0) as haom,round(c.kuc/10000,0) as kuc,round(a.nljlm/10000,0) as nlm,round(a.nljhm/10000,0) as nhm,round(b.yljlm/10000,0) as ylm,round(b.yljhm/10000,0) as yhm\n" +
			"from shouhcrbb rb,diancxxb dc,(select nvl(sum(rb.dangrgm),0) as nljlm,nvl(sum(rb.haoyqkdr),0) as nljhm from shouhcrbb rb,diancxxb dc\n" + 
			"where rb.diancxxb_id = dc.id and dc.ranlgs<>1 "+diancid+" \n" + 
			"and rb.riq < =to_date('"+strDate+"','yyyy-mm-dd') and rb.riq >= to_date('"+fdoy+"','yyyy-mm-dd')) a,(select nvl(sum(rb.dangrgm),0) as yljlm,nvl(sum(rb.haoyqkdr),0) as yljhm\n" + 
			" from shouhcrbb rb,diancxxb dc\n" + 
			"where rb.diancxxb_id = dc.id and dc.ranlgs<>1 "+diancid+" \n" + 
			"and rb.riq < =to_date('"+strDate+"','yyyy-mm-dd') and rb.riq >= to_date('"+fdom+"','yyyy-mm-dd')) b,(select nvl(sum(rb.dangrgm),0) as laim,nvl(sum(rb.haoyqkdr),0) as haom,nvl(sum(rb.kuc),0) as kuc\n" + 
			" from shouhcrbb rb,diancxxb dc\n" + 
			"where rb.diancxxb_id = dc.id and dc.ranlgs<>1 "+diancid+" \n" + 
			"and rb.riq = to_date('"+strDate+"','yyyy-mm-dd'))c\n" + 
			"where\n" + 
			"rb.diancxxb_id=dc.id and dc.ranlgs<>1 "+diancid+"\n" + 
			"group by dc.quanc,c.laim,c.haom,a.nljlm,a.nljhm,b.yljlm,b.yljhm,c.kuc";

		rs = con.getResultSetList(sql);
		String laim="";
		String ylm="";
		String nlm="";
		String haom="";
		String yhm="";
		String nhm="";
		String kuc="";
		String strshouhc = "";
		String diancMC = "";
		if(rs.next()){
			diancMC = rs.getString("quanc");
			laim = rs.getString("laim");			
			if(laim.equals("0")||laim.equals("")){
				laim="昨日来煤: ";
			}else{
				laim= diancMC + " 昨日来煤:<a style=\"color: #FF6600;font-size:18px;\">"+laim+"</a>"+strDanw+";<br/>";
			}
			ylm = rs.getString("ylm");
			if(ylm.equals("0")||ylm.equals("")){
				ylm="月来煤: ";
			}else{
				ylm= diancMC + " 月来煤:<a style=\"color: #FF6600;font-size:18px;\">"+ylm+"</a>"+strDanw+";<br/>";
			}
			nlm = rs.getString("nlm");
			if(nlm.equals("0")||nlm.equals("")){
				nlm="年来煤: ";
			}else{
				nlm= diancMC + " 年来煤:<a style=\"color: #FF6600;font-size:18px;\">"+nlm+"</a>万吨;<br/>";
			}
			
			haom = rs.getString("haom");
			if(haom.equals("0")||haom.equals(""))
			{
				haom="昨日耗煤: ";
			}
			else
			{
				haom= diancMC + " 昨日耗煤:<a style=\"color: #FF6600;font-size:18px;\">"+haom+"</a>"+strDanw+";<br/>";
			}
			yhm = rs.getString("yhm");
			if(yhm.equals("0")||yhm.equals(""))
			{
				yhm="月耗煤: ";
			}
			else
			{
				yhm= diancMC + " 月耗煤:<a style=\"color: #FF6600;font-size:18px;\">"+yhm+"</a>"+strDanw+";<br/>";
			}
			nhm = rs.getString("nhm");
			if(nhm.equals("0")||nhm.equals(""))
			{
				nhm="年耗煤: ";
			}
			else
			{
				nhm= diancMC + " 年耗煤:<a style=\"color: #FF6600;font-size:18px;\">"+nhm+"</a>万吨;<br/>";
			}
			kuc = rs.getString("kuc");
			if(kuc.equals("0")||kuc.equals(""))
			{
				kuc="库存量: </br>";
			}
			else
			{
				kuc= diancMC + " 库存量:<a style=\"color: #FF6600;font-size:18px;\">"+kuc+"</a>"+strDanw+"&nbsp;</br>";
			}
			
			
			}
		if(strshouhc.equals("")){
			strshouhc = laim+haom+kuc+ylm+yhm+kuc+nlm+nhm+kuc;
		}
		con.Close();
		return strshouhc;
	}

	public String getKucgjdc(){
		JDBCcon con = new JDBCcon();
		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		
		String diancid = "";
		if(isJitUserShow()){
			diancid = "";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
		}else{//选电厂只刷新出该电厂
			if (IsZongc(con,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
				diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}else{
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}
		}
		
		String sql = "select * from (select dc.id as id,dc.mingc\n"
					+ "  from shouhcrbb rb,diancxxb dc\n"
					+ " where rb.diancxxb_id=dc.id " +
							"and rb.riq=to_date('"+strDate+"','yyyy-mm-dd') "+
							diancid+" \n"
					+ "   and rb.kuc<dc.jingjcml order by dc.xuh)";

		ResultSetList rs = con.getResultSetList(sql);
		
		String strkucgjdc = "";
		int danws = 0;//单位个数
		for(int i=0;rs.next();i++){
			
			danws = danws+1;
			if(i<50){
				if(strkucgjdc.equals("")){
					strkucgjdc ="<a href='#' onclick='getShouhcPage("+ rs.getString("id") +");'>" + rs.getString("mingc") + "</a>";
				}else{
					strkucgjdc = strkucgjdc+"<a href='#' onclick='getShouhcPage("+ rs.getString("id") +");'>" + rs.getString("mingc") + "</a>";
				}
				if(danws>=2 && danws%2==0){
					strkucgjdc = strkucgjdc+"</br>";
				}else{
					strkucgjdc = strkucgjdc+"<a>&nbsp;&nbsp;&nbsp;&nbsp;</a>";
				}
			}else{
				break;
			}
		}
		if(danws>0 && danws%2!=0){
			strkucgjdc = strkucgjdc+"<br>";
		}
		setMsg((danws+1)/2);
		con.Close();
		return strkucgjdc;
	}
	
public String getWenj(){
		JDBCcon con = new JDBCcon();
		Date dat=new Date();//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		Visit visit = (Visit)this.getPage().getVisit();
		String diancid = "";
		if(isJitUserShow()){
			diancid = "";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
		}else{//选电厂只刷新出该电厂
			diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		String sql =
			"select w.id as id,r.mingc reny,to_char(min(f.shij),'YYYY-MM-DD HH24:MI:SS')shij,to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd') as tians,x.zhi as tiansbj,"
			+"to_char(min(f.youxq),'YYYY-MM-DD')youxsj,getMainHtmlAlert('main','"+MainGlobal.getHomeContext(this)
			+"','Tongzwj','wenj',w.id, w.biaot,to_char(f.shij,'yyyy-mm-dd'),(x.zhi-(to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd')))) as biaot,getJiesdws(w.id,f.shij) jiesdws\n" +
			"from fabwjb f,renyxxb r,wenjb w,xitxxb x \n" + 
			"where f.renyxxb_id=r.id and f.wenjb_id=w.id and f.diancxxb_id = "+visit.getDiancxxb_id()+" and x.mingc='新闻天数设置' \n" +
			"and f.youxq>=sysdate\n" + 
			"group by w.id,r.mingc ,w.biaot ,f.shij,x.zhi order by shij desc";

		ResultSetList rs = con.getResultSetList(sql);
		
		
		int i = 0;
		int j = 0;
		int iAllLines=13;
		
		String wenj = "";
		String strbiaot = "";
		String strShicdt=getShicdtLink();
		if (strShicdt.length()>0) {//有市场动态的相关设置，在总行数中给市场动态留3行
			iAllLines=iAllLines-3;
		}
		
		for( i=0;rs.next();i++){
			if(i<iAllLines){
				if(rs.getInt("tians")<=rs.getInt("tiansbj")){
					strbiaot = "<img src='"+ MainGlobal.getHomeContext(this)+"/imgs/login/zdtgs/news.gif'" +">"+rs.getString("biaot");
				}else{
					strbiaot = rs.getString("biaot");
				}
				if(wenj.equals("")){
					wenj ="<tr><td class=\"news\">" + strbiaot+"&nbsp</td></tr>" ;
				}else{
					wenj = wenj+"<tr><td class=\"news\">" +strbiaot+"&nbsp</td></tr>" ;
				}
			}else{
				break;
			}
			j = i;
		}
		for(int k =j;k<iAllLines;k++){
			if(wenj.equals("")){
				wenj="<tr><td>&nbsp;</td></tr>";
			}else{
				wenj=wenj+"<tr><td>&nbsp;</td></tr>";
			}
		}
		wenj=wenj+"<tr><td>&nbsp;</td></tr>";
		wenj=wenj+getShicdtLink();
//		wenj = wenj+"<tr><td class=\"news\" align=\"right\"><a href=\"#\">更多 >></a></td></tr>";
		con.Close();
		return wenj;
	}
	
	public String getShicdtLink(){
		JDBCcon cn=new JDBCcon();
		String strSz ="";
		ResultSetList rsmc = cn.getResultSetList( "select distinct x.zhi from xitxxb x where x.mingc='首页显示市场动态链接'");
		if(rsmc.next()){
			strSz= rsmc.getString("zhi");
		}
		rsmc.close();
		cn.Close();
		
		String wenj="";
		if ("是".equals(strSz)){//如果设置了在“首页显示市场动态链接”显示，则生成相关链接
			wenj ="<tr><td class=\"news\" id=\"GangkjgReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">秦皇港岛煤炭市场行情价格周报</a></td></tr>" ;
			wenj =wenj+"\n<tr><td class=\"news\" id=\"GuojmjReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">环球煤炭电子交易平台NEWC指数</a></td></tr>" ;
			wenj =wenj+"\n<tr><td class=\"news\" id=\"GuojyjReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">国际原油价格速览</a></td></tr>" ;
		}
		return wenj;
	}
	public String getOneTongz(String strWenjID ){
		StringBuffer str=new StringBuffer();	
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String biaot="";
		String neir="";
		String yuanmc="";
		String url="";
		String fujbItem="";
		String fabr="";
		String fabsj="";
		try {
			String sql="select id,biaot,neir from wenjb where id="+strWenjID;
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				biaot=""+rs.getString("biaot");
				neir="&nbsp;&nbsp;&nbsp;&nbsp;"+rs.getString("neir");
				
				String newurl=MainGlobal.getHomeContext(this);
				String sql1="select f.yuanmc,NVL(SUBSTR(URL,0,INSTR('"+newurl+"','service=page')-1),'"+newurl+"')||SUBSTR(URL,INSTR(URL,'service=page')-1) newurl from fujb f where f.wenjb_id="+rs.getLong("id");
				ResultSet rs1=con.getResultSet(sql1);
				int i=0;
				while(rs1.next()){
					i++;
					yuanmc=rs1.getString("yuanmc");
					url=rs1.getString("url");
					System.out.println("url:"+url);
					fujbItem=fujbItem+ "附件"+i+":"+ "<a  onclick=\"window.open('"+url+"')\" href=\"#\" >"+yuanmc+"</a><br>";
				}
				rs1.close();
				String sql2="select r.quanc,f.shij from fabwjb f,renyxxb r where f.renyxxb_id=r.id and f.diancxxb_id="+visit.getDiancxxb_id()+" and wenjb_id="+rs.getLong("id")+"";
				ResultSet rs2=con.getResultSet(sql2);
				while(rs2.next()){
					fabr=rs2.getString("quanc");
					fabsj=rs2.getString("shij");
				}
				rs2.close();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		str.append("<table ><tr> \n");
		str.append("<td width=20></td> \n");
		str.append("<td width=90% > \n");
		str.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
		str.append("<tr><td height=5></td></tr> \n");
		str.append("<tr><td align=\"center\" style=\"font-size: 15pt;color: #FF0000\" height=\"50\" ><b>"+biaot+"</b></td></tr> \n");
		str.append("<tr><td height=30 align=right valign=top >发布人："+fabr+"&nbsp;&nbsp;&nbsp;&nbsp;发布时间："+fabsj+"<hr></td></tr> \n");
		str.append("<tr><td  align=\"left\"  valign=top style=\"line-height: 150%\" >&nbsp;&nbsp;&nbsp;&nbsp; "+neir+"</td></tr>");
		str.append("<tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr> \n");
		str.append("<tr><td align=\"left\"><hr>"+fujbItem+"</td></tr></table></td><td width=20></td> \n");
		str.append("</tr></table> \n");
		return str.toString();
	}
	public String getTongz() {//返回页面数据。biaot,neir,fujbItem
		StringBuffer str=new StringBuffer();	
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		try {
			 //得到置顶的个文件
			 String strsql="select w.id as id \n" + 
				"      from fabwjb f,wenjb w ,renyxxb ry\n" + 
				"      where f.wenjb_id=w.id and f.diancxxb_id ="+visit.getDiancxxb_id()+"\n" + 
				"      and ry.id=f.renyxxb_id\n" + 
				"      and f.zhid=1\n" + 
				"      and f.youxq>=sysdate order by f.shij desc\n"  ;
			 ResultSet rs=con.getResultSet(strsql);
			 while (rs.next()){
				 if (str.length()>0){
					str.append("\n<br>\n"); 
				 }
				 str.append(getOneTongz(rs.getString("id")));
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return str.toString();
	}
	
	public String getSetDefaultItem(){
		//默认显示，库存图，还是通知
		Visit visit = (Visit) getPage().getVisit();
		String defaultItem=MainGlobal.getXitsz("首页默认显示选项卡",visit.getDiancxxb_id()+"", "库存图");
		String strItem ="kuc";
		if (defaultItem.equals("库存图")){
			strItem="kuc";
		}else if(defaultItem.equals("重要通知")){
			strItem="tongz";
		}else if(defaultItem.equals("收耗比较图")){
			strItem="shouh";
		}
		return "<script>\n" +
			"ChangeChart(document.getElementById(\""+strItem+"\"));\n"+
			"</script>\n";
	}
	
	
	private String DaibrwMsg;//待办任务提示信息内容

	public String getDaibrwMsg() {
		this.ShezDaibrwMsg();
		return DaibrwMsg;
	}

	public void setDaibrwMsg(String daibrwMsg) {
		DaibrwMsg = daibrwMsg;
	}
	
	private void ShezDaibrwMsg(){//设置待办任务提示信息
		
		String sql=" select * from xitxxb where mingc='主页右下角出现待办任务信息提示'  and zhi='是' and leib='主页' and zhuangt=1 ";
		JDBCcon con=new JDBCcon();
		if(!con.getHasIt(sql)){
			con.Close();
			return;
		}
		
		String tabrTitle="消息提示";
		String conTitle="待办任务&nbsp;&nbsp;"+this.getYestodayStr("yyyy年MM月dd日",new Date(),0);
		String msgstr="";
		String handler="";
		
		ResultSetList rsl=this.hasDianc(con, ((Visit)this.getPage().getVisit()).getDiancxxb_id());
		ResultSetList rs=null;
		String brq=DateUtil.FormatOracleDate(this.getYestodayStr("yyyy-MM-dd",new Date(),-1));//昨天
		String qrq=DateUtil.FormatOracleDate(this.getYestodayStr("yyyy-MM-dd",new Date(),-2));//前天
		int cun=0;
		while(rsl.next()){
			String diancxxb_id=rsl.getString("id");
			String dcid=rsl.getString("dcid");
			
			StringBuffer bf=new StringBuffer();
			bf.append(" select \n");
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='fahb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;来煤量:未导入','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='fahb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='fahb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;来煤量:未完全导入' " +
					") " +
					")  " //为空 说明有些发货数据没有导入成功
					);
			
			bf.append(" || \n");
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='zhilb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;入厂质量:未导入','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='zhilb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='zhilb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;入厂质量:未完全导入' " +
					") " +
					")  " //为空 说明有些发货数据没有导入成功
					);
			bf.append(" || \n");
			
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;入炉质量:未导入','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;入炉质量:未完全导入' " +
					") " +
					")  " //为空 说明有些发货数据没有导入成功
					);
			bf.append(" || \n");
			
			
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;收耗存:未导入','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;收耗存:未完全导入' " +
					") " +
					")  " //为空 说明有些发货数据没有导入成功
					);
			bf.append(" || \n");
			
			
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='riscsjb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;日生产数据:未导入','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='riscsjb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='riscsjb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;日生成数据:未完全导入' " +
					") " +
					")   \n " //为空 说明有些发货数据没有导入成功
					);
			bf.append(" || \n");
			
			
			
			bf.append(" decode(" +
					"(select count(distinct leib) from hetb where diancxxb_id="+dcid+" and liucztb_id!=1 ),\n" +
					"0,''," +
					"1,decode( (select distinct leib from hetb where diancxxb_id="+dcid+" and liucztb_id!=1 ),0,'&nbsp;&nbsp;合同:未导入','&nbsp;&nbsp;合同:未审核'),\n" +
					"'&nbsp;&nbsp;合同:未导入和未审核'" +
					")\n");
			bf.append(" || \n");
			
			
			
			bf.append(" ( select decode(count(distinct leib),2,nvl('&nbsp;&nbsp;结算单:未导入和未审核',''),decode(max(leib),0,nvl('&nbsp;&nbsp;结算单:未导入',''),1,nvl('&nbsp;&nbsp;结算单:未审核','') )  )  from "+
					" (select  0 leib from jiesb where diancxxb_id="+dcid+" and liucztb_id=0 and rownum=1 union select 0 leib from jiesyfb where diancxxb_id="+dcid+" and liucztb_id=0 and rownum=1 \n" +
					" union select  1 leib from kuangfjsmkb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 union select  1 leib from kuangfjsyfb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 \n" +
					" union select  1 leib from diancjsmkb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 union select   1 leib from diancjsyfb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 " +
					") " +
					") ");
		
			
			bf.append(" msg from dual \n");
	
			rs=con.getResultSetList(bf.toString());
			cun++;
			if(rs.next()){
				if(rs.getString("msg")!=null && !rs.getString("msg").equals("")){
				msgstr+=" <A href=\\'\\' onclick =\\'parent.openNewPage("+dcid+")\\'  hidefocus=false id=\\'btCommand"+cun+"\\' style=\\'text-decoration: underline\\'> "+"<FONT color=#ff0000>"+rsl.getString("mingc")+":"+"</FONT>"+rs.getString("msg")+"</A>"+"<br><br>";
//				handler+=" oPopup.document.getElementById(\\'btCommand"+cun+"\\').onclick =openNewPage;";
				}
			}
		}
		
		rs.close();
		rsl.next();
		con.Close();
		this.setDaibrwMsg("popmsg('"+tabrTitle+"','"+conTitle+"','"+msgstr+"','"+handler+"');");
	}
	
	protected void initialize(){
		this.setDaibrwMsg("");
	}
	
	
//  判断电厂Tree中所选电厂时候还有子电厂   
    private ResultSetList hasDianc(JDBCcon con,long id){ 
		
//		String sql="select z.jiekzhb_id id,d.id dcid, d.mingc from diancxxb d,dianczhb z where d.id=z.diancxxb_id and  fuid = " + id +" order by xuh asc ";
//		ResultSetList rsl=con.getResultSetList(sql);
		
//		if(rsl.getRows()>0){//说明是 父电厂 或者公司级
//			return rsl;
//		}
//		
		String sql="select nvl(z.jiekzhb_id,-1) id,d.id dcid, d.mingc from diancxxb d,dianczhb z where d.id=z.diancxxb_id(+) and ( d.id = " + id +" or d.fuid="+id+") order by xuh asc ";
		ResultSetList rsl=con.getResultSetList(sql);
		return rsl;
	}
    
    
    
	//得到昨天的日期
	private String getYestodayStr(Date date){
		
		return getYestodayStr( null, date,-1);
	}
	
//	得到前天的日期
	private String getYestodayStr(String format,Date date,int i){
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		cal.add(Calendar.DATE, i);
		
		if(format==null || format.equals(""))
			return DateUtil.FormatDate(cal.getTime());
		
		return DateUtil.Formatdate(format, cal.getTime());
	}
    
    
}