package com.zhiren.jt.gsdt.ranltjrb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：王磊
 * 时间：2009-06-02
 * 描述：修改页面初始日期为当前日期的前一天、修改燃料统计日报车数不加入汽车车数
 */
/*
 * 作者：王磊
 * 时间：2009-07-22
 * 描述：修改累计来煤没有的时无法生成收耗存数据的问题
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Ranltjrb extends BasePage implements  PageValidateListener{
	
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
	
// 日期控件
	boolean riqichange=false;
	private String riq;
	public String getRiq() {
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return riq;
	}
	public void setRiq(String riq) {
		
		if(this.riq!=null &&!this.riq.equals(riq)){
			this.riq = riq;
			riqichange=true;
		}		
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			//this.getSelectData();
		}
	}
	
	private void Refurbish() {
       //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}

	
//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			this.getTree();
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
				
			isBegin=true;
			riq=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		
		getToolBars() ;
		this.Refurbish();
	}
	
	private String GSDT_RANLTJRB="Ranltjrb";//大唐甘肃公司燃料统计日报表
	private String mstrReportName="Ranltjrb";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(GSDT_RANLTJRB)){
			return getSelectData();
		}else{
			return "无此报表";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	
	private String getSelectData(){
		String strSQL="";
		String strSQL2 = "";
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		String strBaobmc = "";
		String strGongsID = "";
		String strRiq = "";
		String strYear = "";
		String strMonth = "";
		String strDay = "";
		
		strRiq = this.getRiq();
		strYear = strRiq.substring(0, 4);
		strMonth = strRiq.substring(5, 7);
		strDay = strRiq.substring(8, 10);
		//报表标题名称
		strBaobmc = this.getBiaotmc(Long.parseLong(this.getTreeid()));
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			
		} else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "and dc.fuid=" + this.getTreeid();
			
		} else if (jib==3){//选电厂只刷新出该电厂
			strGongsID ="and dc.id= " +this.getTreeid();
	
		}else if (jib==-1){
			strGongsID ="and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
			
		//报表表头定义
		Report rt = new Report();
		Report rt2 = new Report();
		int ArrWidth[] = null;
		int ArrWidth2[] = null;
		String ArrHeader[][] = null;
		String ArrHeader2[][] = null;
		String arrFormat[]=null;
		
		//---------报表内容----------//
		/*
		 * 燃料统计日报表
		 */
		strSQL = 
			  "select dc.mingc as diancxxb_id,\n" +
			  "decode(grouping(jihkjb.mingc),1,'合计',jihkjb.mingc) as jihkjb_id,\n" + 
			  "round_new(sum(nvl(dr_laim.ches,0)),0) as dr_ches,\n" + 
			  "round_new(sum(nvl(dr_laim.laimsl,0)),0) as dr_laimsl,\n" +
			  "round_new(sum(nvl(lj_laim.ches,0)),0) as lj_ches,\n" +
			  "round_new(sum(nvl(lj_laim.laimsl,0)),0) as lj_laimsl,\n" + 
			  "round_new(avg(nvl(dr_fadl.fadl,0)),0) as dr_fadl,\n" + 
			  "round_new(avg(nvl(lj_fadl.fadl,0)),0) as lj_fadl,\n" + 
			  "round_new(avg(nvl(dr_haom.haoml,0)),0) as dr_haoml,\n" + 
			  "round_new(avg(nvl(lj_haom.haoml,0)),0) as lj_haoml,\n" + 
			  "round_new(avg(nvl(dr_haom.kuc,0)),0) as mei_kuc,\n" + 
			  "decode(avg(nvl(dr_haom.haoml,0)),0,0,round_new(avg(nvl(dr_haom.kuc,0))/avg(nvl(dr_haom.haoml,0)),0)) as mei_kyts,\n" +
			  "round_new(avg(nvl(dc.jingjcml,0)),0) as jingjcml,\n" + 
			  "round_new(avg(nvl(dr_haoy.haoyl,0)),0) as dr_haoyl,\n" + 
			  "round_new(avg(nvl(lj_haoy.haoyl,0)),0) as lj_haoyl,\n" + 
			  "round_new(avg(nvl(dr_haoy.kuc,0)),0) as you_kuc\n" + 
			  "from\n" + 
			  "(select diancxxb_id,jihkjb_id\n" + 
			  "from fahb where daohrq between First_day(to_date('" + strRiq + "','yyyy-mm-dd')) and to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id,jihkjb_id\n" + 
			  " union select diancxxb_id,1 from riscsjb\n" + 
			  "where riq between First_day(to_date('" + strRiq + "','yyyy-mm-dd')) and to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id\n" + 
			  ")al,\n" + 
			  "(select diancxxb_id,jihkjb_id,sum(laimsl) as laimsl,sum(decode(yunsfsb_id,"+SysConstant.YUNSFS_HUOY+",ches,0)) as ches\n" + 
			  "from fahb where daohrq=to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id,jihkjb_id\n" + 
			  ")dr_laim,\n" + 
			  "(select diancxxb_id,jihkjb_id,sum(laimsl) as laimsl,sum(decode(yunsfsb_id,"+SysConstant.YUNSFS_HUOY+",ches,0)) as ches\n" + 
			  "from fahb where daohrq between First_day(to_date('" + strRiq + "','yyyy-mm-dd')) and to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id,jihkjb_id\n" + 
			  ")lj_laim,\n" + 
			  "(select diancxxb_id,sum(fadl) as fadl from riscsjb\n" + 
			  "where riq=to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id\n" + 
			  ")dr_fadl,\n" + 
			  "(select diancxxb_id,sum(fadl) as fadl from riscsjb\n" + 
			  "where riq between First_day(to_date('" + strRiq + "','yyyy-mm-dd')) and to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id\n" + 
			  ")lj_fadl,\n" + 
			  "(select diancxxb_id,sum(haoyqkdr) as haoml,sum(kuc) as kuc from shouhcrbb\n" + 
			  "where riq=to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id\n" + 
			  ")dr_haom,\n" + 
			  "(select diancxxb_id,sum(haoyqkdr) as haoml from shouhcrbb\n" + 
			  "where riq between First_day(to_date('" + strRiq + "','yyyy-mm-dd')) and to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id\n" + 
			  ")lj_haom,\n" + 
			  "(select diancxxb_id,sum(fady+gongry+qity) as haoyl,sum(kuc) as kuc from shouhcrbyb\n" + 
			  "where riq=to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id\n" + 
			  ")dr_haoy,\n" + 
			  "(select diancxxb_id,sum(fady+gongry+qity) as haoyl from shouhcrbyb\n" + 
			  "where riq between First_day(to_date('" + strRiq + "','yyyy-mm-dd')) and to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			  "group by diancxxb_id\n" + 
			  ")lj_haoy,\n" + 
			  "diancxxb dc,\n" + 
			  "jihkjb\n" + 
			  "where dc.id=al.diancxxb_id\n" + 
			  "and al.diancxxb_id=lj_laim.diancxxb_id(+)\n" + 
			  "and al.diancxxb_id=dr_laim.diancxxb_id(+)\n" + 
			  "and al.jihkjb_id=dr_laim.jihkjb_id(+)\n" + 
			  "and al.jihkjb_id=lj_laim.jihkjb_id(+)\n" + 
			  "and al.diancxxb_id=dr_fadl.diancxxb_id(+)\n" + 
			  "and al.diancxxb_id=lj_fadl.diancxxb_id(+)\n" + 
			  "and al.diancxxb_id=dr_haom.diancxxb_id(+)\n" + 
			  "and al.diancxxb_id=lj_haom.diancxxb_id(+)\n" + 
			  "and al.diancxxb_id=dr_haoy.diancxxb_id(+)\n" + 
			  "and al.diancxxb_id=lj_haoy.diancxxb_id(+)\n" + 
			  "and al.jihkjb_id=jihkjb.id\n" + strGongsID +"\n" + 
			  "group by rollup (dc.mingc,jihkjb.mingc)\n" + 
			  "having not grouping(dc.mingc)=1\n" + 
			  "order by dc.mingc,jihkjb.mingc desc";
		
		ArrHeader=new String[2][16];
		ArrHeader[0]=new String[] {
				 "单位名称", "单位名称", "当日供煤", "当日供煤", "月累计供煤", 
				 "月累计供煤", "当日发电量", "累计发电量","当日耗煤","累计耗煤",
				 "煤库存量","可用天数","警戒线", "当日耗油","累计耗油","油库存量"
				 };
		ArrHeader[1]=new String[] {
				 "单位名称", "单位名称","车数","煤量","车数","煤量",
				 "当日发电量", "累计发电量","当日耗煤","累计耗煤",
				 "煤库存量","可用天数","警戒线", "当日耗油","累计耗油","油库存量"
				 };		 
		ArrWidth=new int[] {120, 60, 59, 59, 59, 59, 59, 59,
				 59, 59, 59, 49, 59, 49, 49, 49};
//		arrFormat= new String []{"", "", "0", "0", "0", "0", "0", "0", "0",
//				 "0", "0", "0", "0", "0", "0"};
		ResultSet rs = cn.getResultSet(strSQL);		
		Table tb = new Table(rs, 2, 0, 2);
		rt.setBody(tb);		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle( strBaobmc + "燃料统计日报表", ArrWidth);
//		rt.setDefaultTitle(1, 3, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);		
		rt.setDefaultTitle(7, 3, strYear + " 年 " + strMonth + " 月 " + strDay + " 日", Table.ALIGN_LEFT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		//合并7到之后一列
		for (int j = 7; j <= rt.body.getCols(); j++) {
			int iStartMegeRow = 0;
			for (int i = 3; i <= rt.body.getRows(); i++) {
				if (rt.body.getCell(i, 2).value.equals("合计")) {
					if (iStartMegeRow != 0 && iStartMegeRow != i){
						rt.body.mergeCell(iStartMegeRow, j, i - 1, j);
					}
					iStartMegeRow = i;
				}
				if (i == rt.body.getRows())
					rt.body.mergeCell(iStartMegeRow, j, i, j);
			}
		}
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.ShowZero = false;
		
		/*
		 * 计划供煤完成情况日报表
		 */
		strSQL2 = 
			"select decode(grouping(gys.smc),1,'总供煤情况',\n" +
			"decode(grouping(gys.dqmc),1,decode(gys.smc,null,'其他',gys.smc)||'小计',gys.dqmc)) as meikmc,\n" + 
			"decode(grouping(gys.smc),1,'总供煤情况',\n" + 
			"decode(grouping(gys.dqmc),1,decode(gys.smc,null,'其他',gys.smc)||'小计',\n" + 
			"decode(grouping(dc.mingc),1,'合计',dc.mingc))) as diancmc,\n" + 
			"round_new(sum(nvl(dr_laim.ches,0)),0) as dr_ches,\n" + 
			"round_new(sum(nvl(dr_laim.laimsl,0)),0) as dr_laimsl,\n" + 
			"round_new(sum(nvl(lj_laim.ches,0)),0) as lj_ches,\n" + 
			"round_new(sum(nvl(lj_laim.laimsl,0)),0) as lj_laimsl,\n" + 
			"round_new(sum(nvl(yunsjh.rijhcs,0)),0) as rijhcs,\n" + 
			"round_new(sum(nvl(yunsjh.yuejhcs,0)),0) as yuejhcs,\n" + 
			"round_new(sum(nvl(lj_laim.laimsl,0)/to_char(to_date('" + strRiq + "','yyyy-mm-dd'),'dd')),0) as rijgml,\n" + 
			"round_new(sum(nvl(dr_laim.ches,0)-nvl(yunsjh.rijhcs,0)),0) as yurjb,\n" + 
			"round_new(sum(nvl(lj_laim.ches,0)-nvl(yunsjh.yuejhcs,0)),0) as yuyjb,\n" + 
			"decode(sum(nvl(yunsjh.yuejhcs,0)),0,0,round_new(sum(nvl(lj_laim.ches,0))/sum(nvl(yunsjh.yuejhcs,0))*100,0)) as daohl\n" + 
			"from\n" + 
			"(select diancxxb_id,gongysb_id,sum(laimsl) as laimsl,sum(ches) as ches\n" + 
			"from fahb where daohrq=to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			"group by diancxxb_id,gongysb_id\n" + 
			")dr_laim,\n" + 
			"(select diancxxb_id,gongysb_id,sum(laimsl) as laimsl,sum(ches) as ches\n" + 
			"from fahb where daohrq between First_day(to_date('" + strRiq + "','yyyy-mm-dd')) and to_date('" + strRiq + "','yyyy-mm-dd')\n" + 
			"group by diancxxb_id,gongysb_id\n" + 
			")lj_laim,\n" + 
			"(select diancxxb_id,gongysb_id,pic as yuejhcs,round_new(pic/to_char(Last_day(to_date('" + strRiq + "','yyyy-mm-dd')),'dd'),0) as rijhcs\n" + 
			"from yunsjhb\n" + 
			"where to_char(riq,'yyyy-mm')=to_char(to_date('" + strRiq + "','yyyy-mm-dd'),'yyyy-mm')\n" + 
			") yunsjh,\n" + 
			"(select gy.id,gy.mingc,gy.xuh ,\n" +
			"       nvl(dgys.id,gy.id) as dqid,\n" + 
			"       nvl(dgys.mingc,gy.mingc) as dqmc,\n" + 
			"       nvl(dgys.xuh,gy.xuh) as dqxh,\n" + 
			"       (select id from shengfb where id=nvl(dgys.shengfb_id,gy.shengfb_id)) as sid,\n" + 
			"       (select quanc from shengfb where id=nvl(dgys.shengfb_id,gy.shengfb_id)) as smc,\n" + 
			"       (select xuh from shengfb where id=nvl(dgys.shengfb_id,gy.shengfb_id)) as sxh\n" + 
			"from gongysb gy,gongysb dgys\n" + 
			"       where gy.fuid=dgys.id(+)\n" + 
			") gys, \n" +
			"diancxxb dc \n" + 
			"where dc.id=lj_laim.diancxxb_id\n" + 
			"and lj_laim.diancxxb_id=dr_laim.diancxxb_id(+)\n" + 
			"and lj_laim.diancxxb_id=yunsjh.diancxxb_id(+)\n" + 
			"and lj_laim.gongysb_id=gys.id\n" + 
			"and lj_laim.gongysb_id=dr_laim.gongysb_id(+)\n" + 
			"and lj_laim.gongysb_id=yunsjh.gongysb_id(+)\n" + strGongsID + "\n" + 
			"group by rollup (gys.smc,gys.dqmc,dc.mingc)\n" + 
			"order by grouping(gys.smc) desc,gys.smc desc,gys.dqmc desc,dc.mingc desc";
		
		ArrHeader2=new String[2][12];
		ArrHeader2[0]=new String[] {
				 "煤矿名称", "煤矿名称", "当日供煤", "当日供煤", "月累计供煤", 
				 "月累计供煤", "计划车数(车)", "计划车数(车)","实际完成(车/日)",
				 "实际完成(车/日)", "实际完成(车/日)","到货率"
				 };
		ArrHeader2[1]=new String[] {
				 "煤矿名称", "煤矿名称","车数","煤量","车数","煤量",
				 "日计划", "月计划","日均供煤","与日计比", "与月计比","%"
				 };		 
		ArrWidth2=new int[] {127, 120, 72, 72, 72, 72, 72, 
				 72, 72, 72, 72, 72};
		ResultSet rs2 = cn.getResultSet(strSQL2);		
		Table tb2 = new Table(rs2, 2, 0, 2);
		rt2.setBody(tb2);
		int aw2=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt2.getArrWidth(ArrWidth2, aw2);//添加报表的A4控制
		rt2.setTitle( strBaobmc + "计划供煤完成情况日报表", ArrWidth2);
		rt2.setDefaultTitle(5, 3, strYear + " 年 " + strMonth + " 月 " + strDay + " 日", Table.ALIGN_LEFT);
		
		rt2.body.setWidth(ArrWidth2);
		rt2.body.setHeaderData(ArrHeader2);// 表头数据
		
		rt2.body.mergeFixedRow();
//		rt2.body.mergeFixedCols();
		rt2.body.merge(2, 1, rt2.body.getRows(), 2);
		rt2.body.setColAlign(1, Table.ALIGN_CENTER);
		rt2.body.ShowZero = false;
				
//		rt.body.setColFormat(arrFormat);
		//页脚 
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
//		rt.setDefautlFooter(10,2,"审核:",Table.ALIGN_LEFT);
//		rt.setDefautlFooter(13,3,"制表:",Table.ALIGN_LEFT);
//		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		 		
//		_CurrentPage=1;
//		_AllPages=rt.body.getPages();
//		if (_AllPages==0){
//			_CurrentPage=0;
//		}
		cn.Close();	

		String strTable = 
			"<table align=\"center\" boder=\"0\">" + "\n"
			+ "<tr> \n"
			+ "<td align=\"center\"> \n" 
			+ rt.getAllPagesHtml() + "\n"
			+ "</td> \n"
			+ "<tr> \n"
			+"<td align=\"center\"> \n"
			+ rt2.getAllPagesHtml() + "\n"
			+ "</td> \n"
			+ "</tr> \n"
			+ "</table>";
			
		return strTable;
	}
//	得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc(long diancxxb_id){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select mingc from diancxxb where id=" + diancxxb_id;
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				 biaotmc=rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return biaotmc;
		
	}
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}
	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
	 //	页面判定方法
   public void pageValidate(PageEvent arg0) {
   	String PageName = arg0.getPage().getPageName();
   	String ValPageName = Login.ValidateLogin(arg0.getPage());
   	if (!PageName.equals(ValPageName)) {
   		ValPageName = Login.ValidateAdmin(arg0.getPage());
   		if(!PageName.equals(ValPageName)) {
   			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
       		throw new PageRedirectException(ipage);
   		}
		}
	}
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");	
		//日期
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ","");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);		
		tb1.addText(new ToolbarText("-"));			
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);	
		setToolbar(tb1);	
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf((
					(Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
	
}
