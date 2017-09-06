/****2010-05-19 chh 
*修改内容 :加入新的分矿到货情况到货情况表
*单独管理集团所要求的分矿单位（jitrbdwb）
*单独管理集团提供的日报中重点和区域订货的各矿的合同量（jitrbjhb）
*建立电厂分矿日报供应和集团集团、区域订货合同单位之间的关系（扩展原有的diancgysmykjb）
*然后按分矿日报填报的数据和3中所建立的关系查询统计集团的分矿到货日报
*/

/****2010-05-26 chh 
*修改内容 :重点矿到货率加入电厂类别小计
*/
/****2010-05-28 yanghj 
*修改内容 :添加分矿分厂的查询
*/

package com.zhiren.gs.bjdt.diaoygl;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.dtgsinterface.DBconn;

public class Hetdxqkcx extends BasePage {
	private final static int  LX_ZHONGD=1;
	private final static int LX_BUC=2;
	private final static int LX_YUEDHQK=3;
	private final static int LX_NIANDHQK=4;
	
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

	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
    
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		
		blnIsBegin = false;
		if (getBaoblxValue().getId()==LX_YUEDHQK){
			return getYuefkdhqk(false);
		}else if (getBaoblxValue().getId()==LX_NIANDHQK){
			return getYuefkdhqk(true);
		}else{
			return getShouhcreport();
		}
	}
	
	private boolean runNian(int year){   
		boolean t=false;
		if(year%4==0){
			if(year%100!=0){
				t=true;
			}else if(year%400==0){
				t=true;
			}
		}
		return t;
	}
	
	private int getYearDays(Date dat){
		if (runNian(DateUtil.getYear(dat))){
			return 366;
		}else{
			return 365;
		}
	}
	
	private String getYuefkdhqk(boolean blnNian){
		JDBCcon con = new JDBCcon();
		StringBuffer sb=new StringBuffer(); 
		Date datEndRiq=DateUtil.getDate(getEndriqDate());
		
		String strDateTitle="时间:"+DateUtil.getYear(datEndRiq)+"年"+DateUtil.getMonth(datEndRiq)+"月"+DateUtil.getDay(datEndRiq)+"日";
		String selectDay=DateUtil.FormatDate(datEndRiq);
		String yearFirstDay=DateUtil.FormatDate(DateUtil.getFirstDayOfYear(datEndRiq));
		String monthFirstDay=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(datEndRiq));
		
		int jib=this.getDiancTreeJib();
		int yearDays=getYearDays(datEndRiq);
		
		String strGongsID="";
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
		}else if (jib==2){
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+") ";
		}else if (jib==3){
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		String leijsm="round(sum(yuelj)/10000,2) as 月收煤";
		String leijjh="             round(sum(shul)/"+yearDays+"*(to_number(to_char(to_date('"+selectDay+"','yyyy-mm-dd'),'dd'))),2) as 月计划,\n";
		String strLeij="月";
		if (blnNian){//是年累计表，如果是年累计，按年初到当天计算累计计划，计划和收煤
			leijsm="round(sum(lianlj)/10000,2) as 月收煤";
			leijjh="             round(sum(shul)/"+yearDays+"*(to_date('"+selectDay+"','yyyy-mm-dd')- to_date('"+yearFirstDay+"','yyyy-mm-dd')+1),2) as 月计划,\n ";
			strLeij="年";
		}
		
		sb.append("select  decode(flfz+dcflz+dwfz,3,'大唐国际',2,jh.电厂||'{'||jh.分类||'}',1,jh.电厂,jh.单位) as 单位,\n")
		.append("	年计划, 日计划,月计划,日收煤,月收煤,\n")
		.append("	decode(日计划,0,0, round(日收煤/日计划*100,2)) as 日到货率,\n")
		.append("	decode(月计划,0,0, round(月收煤/月计划*100,2)) as 月到货率,'' 日报月累计,'' 差额")
		.append("	from (select decode(grouping(dc.mingc)+grouping(fl.mingc)+grouping(dw.mingc),3,'全部',fl.mingc) as 分类,\n")
		.append("             decode(grouping(dc.mingc)+grouping(fl.mingc)+grouping(dw.mingc),3,'大唐国际',2,'大唐国际'||dc.mingc,dc.mingc) as 电厂,\n")
		.append("             decode(grouping(dw.mingc),1,'小计',dw.mingc)  as 单位,\n ")
		.append("             sum(shul) as 年计划 ,round(sum(shul)/365,2) as 日计划,\n ")
		.append(leijjh)
		.append("             grouping(fl.mingc) flfz,min(fl.xuh) flxh,fl.mingc as flmc,\n ")
		.append("            grouping(dc.mingc) dcflz,min(jh.xuh) dcxh, grouping(dw.mingc) dwfz\n ")
		.append("      from jitrbjhb jh,diancxxb dc,jitrbdwb dw,item fl")
		.append("             where riq=to_date('").append(yearFirstDay).append("','yyyy-mm-dd')\n ")
		.append("             and dc.id=jh.diancxxb_id\n ")
		.append(strGongsID)
		.append("             and jh.jitrbdwb_id=dw.id\n ")
		.append("             and jh.fenl=fl.id\n ")
		.append("      group by rollup(fl.mingc,dc.mingc,dw.mingc)\n ")
		.append("      order by grouping(fl.mingc) desc,max(fl.xuh),fl.mingc,\n ")
		.append("            grouping(dc.mingc) desc,min(jh.xuh),\n ")
		.append("            grouping(dw.mingc) desc) jh,\n ")
		.append("      (select decode(grouping(dc.mingc)+grouping(fl.mingc)+grouping(dw.mingc),3,'全部',fl.mingc) as 分类,\n ")
		.append("             decode(grouping(dc.mingc)+grouping(fl.mingc)+grouping(dw.mingc),3,'大唐国际',2,'大唐国际'||dc.mingc,dc.mingc) as 电厂,\n ")
		.append("             decode(grouping(dw.mingc),1,'小计',dw.mingc)  as 单位,\n ")
		.append("             "+leijsm+",round(sum(shoum)/10000,2) as 日收煤\n ")
		.append("      from (select dn.diancxxb_id,dn.gongysb_id,nvl(gl.jitrbdwb_id,101999) as jitrbdwb_id,nvl(gl.fenl,124)  as fenl,\n ")
		.append("                     nvl(dn.shoum,0) as lianlj,nvl(dy.shoum,0) as yuelj,nvl(dr.shoum,0) as shoum\n ")
		.append("            from  (select rb.diancxxb_id,my.gongysb_id,sum(duns) as shoum from fenkshcrb rb, vwdcgysmykjgl my\n ")
		.append("                      where riq=to_date('").append(selectDay).append("','yyyy-mm-dd')\n ")
		.append("                       and rb.gongysb_id=my.id\n ")
		.append("                       group by  rb.diancxxb_id,my.gongysb_id)  dr,\n ")
		.append("                  (select rb.diancxxb_id,my.gongysb_id,sum(duns) as shoum from fenkshcrb rb, vwdcgysmykjgl my\n ")
		.append("                       where riq>=to_date('").append(monthFirstDay).append("','yyyy-mm-dd')\n ")
		.append("						  and riq<=to_date('").append(selectDay).append("','yyyy-mm-dd')\n ")
		.append("                         and rb.gongysb_id=my.id\n ")
		.append("                       group by  rb.diancxxb_id,my.gongysb_id)  dy,\n ")
		.append("                 (select rb.diancxxb_id,my.gongysb_id,sum(duns) as shoum from fenkshcrb rb, vwdcgysmykjgl my\n ")
		.append("                       where riq>=to_date('").append(yearFirstDay).append("','yyyy-mm-dd')\n ")
		.append("                         and riq<=to_date('").append(selectDay).append("','yyyy-mm-dd') \n ")
		.append("                         and rb.gongysb_id=my.id\n ")
		.append("                       group by  rb.diancxxb_id,my.gongysb_id)  dn,jitrbgysglb gl\n ")
		.append("         where dn.gongysb_id=dy.gongysb_id(+) and dn.diancxxb_id=dy.diancxxb_id(+)\n ")
		.append("               and dn.gongysb_id=dr.gongysb_id(+) and dn.diancxxb_id=dr.diancxxb_id(+)\n ")
		.append("               and dn.gongysb_id=gl.gongysb_id(+) and dn.diancxxb_id=gl.diancxxb_id(+)) sm,diancxxb dc,jitrbdwb dw,item fl\n ")
		.append("            where  dc.id=sm.diancxxb_id\n ")
		.append("            and sm.jitrbdwb_id=dw.id\n ")
		.append("            and sm.fenl=fl.id\n ")
		.append("            group by rollup(fl.mingc,dc.mingc,dw.mingc)) dh\n ")
		.append("      where jh.分类=dh.分类(+) and jh.电厂=dh.电厂(+) and jh.单位=dh.单位(+) and jh.单位<>'其他'\n ")
		.append("order by flfz desc,flxh,flmc,dcflz desc,dcxh,dwfz desc\n  ");

		ResultSet rs = con.getResultSet(sb.toString());
		Report rt = new Report();

		String ArrHeader[][]=new String[3][9];
		ArrHeader[0]=new String[] {strDateTitle,strDateTitle,strDateTitle,strDateTitle,strDateTitle,strDateTitle,strDateTitle,strDateTitle,strDateTitle,strDateTitle};
		ArrHeader[1]=new String[] {"名称","计划","计划","计划","实供","实供","到货率","到货率","日报月累计","差额"};
		ArrHeader[2]=new String[] {"名称","年计划<br>(万吨)","当日<br>(万吨)",strLeij+"累计<br>(万吨)","当日<br>(万吨)",strLeij+"累计<br>(万吨)","当日(%)",strLeij+"累计(%)","日报月累计","差额"};

		int ArrWidth[]=new int[] {370,50,50,60,50,60,60,60,60,50};
		if (blnNian){
			rt.setTitle("本年分矿点到货情况表", ArrWidth);
		}else{
			rt.setTitle("本月分矿点到货情况表", ArrWidth);
		}
		rt.setBody(new Table(rs,3, 0, 1));
		
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getShouhcreport() {
		JDBCcon con = new JDBCcon();
		String sbsql = "";
		
//		String Start_riq=getBeginriqDate();
		String End_riq=getEndriqDate();
		
		String strGongsID = "";
//		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+") ";
//			notHuiz=" and not grouping(gs.mingc)=1 ";//当电厂树是分公司时,去掉集团汇总
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
//			notHuiz=" and not  grouping(dc.mingc)=1";//当电厂树是电厂时,去掉分公司和集团汇总
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		long leix = getBaoblxValue().getId();
		
		String strLeix = " and vw.gongyskjb_id="+leix+""; 

		
		if(getBaoblxValue().getId()==2){
			sbsql=
				"select case when grouping(vw.gongysmy)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||vw.gongysmy else case when grouping(dc.mingc)=0 then dc.mingc else\n" + 
				"       case when grouping(px.shujsbzt)=0 then decode(px.shujsbzt,1,'电厂合计',0,'运城、新余合计') else '总计' end end end as danwmc,\n" + 
				"       round(sum(nvl(dr.dangrjh,0))/10000,2) as dangrjh,\n" + 
				"       round(sum(nvl(ylj.leijjh,0))/10000,2) as yueljjh,\n" + 
				"       round(sum(nvl(nlj.leijjh,0))/10000,2) as nianljjh,\n" + 
				"       round(sum(nvl(dr.dangrsg,0))/10000,2) as dangrsg,\n" + 
				"       round(sum(nvl(ylj.leijsg,0))/10000,2) as yueljsg,\n" + 
				"       round(sum(nvl(nlj.leijsg,0))/10000,2) as nianljsg,\n" + 
				"       decode(sum(nvl(dr.dangrjh,0)),0,0,round((sum(nvl(dr.dangrsg,0))/sum(nvl(dr.dangrjh,0)))*100,2)) as dangrdhl,\n" + 
				"       decode(sum(nvl(ylj.leijjh,0)),0,0,round((sum(nvl(ylj.leijsg,0))/sum(nvl(ylj.leijjh,0)))*100,2)) as yueljdhl,\n" + 
				"       decode(sum(nvl(nlj.leijjh,0)),0,0,round((sum(nvl(nlj.leijsg,0))/sum(nvl(nlj.leijjh,0)))*100,2)) as nianljdhl\n" + 
				"  from diancxxb dc,dianckjpxb px,vwdcgysmykjgl vw,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,nvl(fk.rijjhl,0) as dangrjh,nvl(fk.duns,0) as dangrsg from fenkshcrb fk,vwdcgysmykjgl vw \n" + 
				"        where fk.riq=to_date('"+End_riq+"','yyyy-mm-dd')  and vw.id=fk.gongysb_id "+strLeix+" )dr,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,sum(fk.rijjhl) as leijjh,sum(fk.duns) as leijsg from fenkshcrb fk,vwdcgysmykjgl vw\n" + 
				"        where fk.riq>=first_day(to_date('"+End_riq+"','yyyy-mm-dd')) and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd') and vw.id=fk.gongysb_id\n" + 
				"          "+strLeix+"  group by fk.diancxxb_id,fk.gongysb_id ) ylj,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,sum(fk.rijjhl) as leijjh,sum(fk.duns) as leijsg from fenkshcrb fk,vwdcgysmykjgl vw\n" + 
				"        where fk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd') and vw.id=fk.gongysb_id\n" + 
				"         "+strLeix+"  group by fk.diancxxb_id,fk.gongysb_id ) nlj,\n" + 
				"\n" + 
				"       (select distinct vw.id,fk.diancxxb_id	--,fk.gongysb_id,decode(gy.fuid,0,gy.id,gy.fuid) as fuid \n"+
				"		   from fenkshcrb fk,vwdcgysmykjgl vw\n" + 
				"   where fk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd') \n" + 
				"     "+strLeix+" and fk.gongysb_id=vw.id ) gys\n" + 
				"\n" + 
				"  where dr.gongysb_id(+)=gys.id and ylj.gongysb_id(+)=gys.id and nlj.gongysb_id(+)=gys.id --and gys.fuid=gy.id \n" + 
				"    and gys.diancxxb_id=dc.id and gys.id=vw.id and px.diancxxb_id=dc.id and px.kouj='月报'\n" + strGongsID +
				"  group by rollup(px.shujsbzt,dc.mingc,vw.gongysmy)\n" +
				"having round(sum(nvl(nlj.leijjh,0))/10000,2)<>0 or  round(sum(nvl(nlj.leijsg,0))/10000,2)<>0\n"+
				"  order by grouping(px.shujsbzt) desc,min(px.xuh),grouping(dc.mingc) desc,min(px.xuh),grouping(vw.gongysmy) desc,vw.gongysmy ";
		}else if(getBaoblxValue().getId()==1){
			
			
			/*sbsql=
			"select case\n" + 
	        " when grouping(vw.gongysmy) = 0 then\n" + 
	         " '&nbsp;&nbsp;&nbsp;&nbsp;' || vw.gongysmy\n" + 
	         "else\n" + 
	         " case\n" + 
	         "when grouping(dc.mingc) = 0 then\n" + 
	         " dc.mingc\n" + 
	         "else\n" + 
	         " case\n" + 
	         "when grouping(px.shujsbzt) = 0 then\n" + 
	         " decode(px.shujsbzt, 1, '电厂合计', 0, '运城、新余合计')\n" + 
	         "else\n" + 
	         " '总计'\n" + 
	         " end end end as danwmc,\n" + 
	         " round(sum(nvl(jih.hej, 0)) , 2) as nianljjh,\n" +  
	         "round(sum(nvl(jih.dangrjh, 0)) , 2) as dangrjh,\n" + 
	         " round(sum(nvl(jih.leijjh, 0)) , 2) as yueljjh,\n" + 
	       
	         " round(sum(nvl(dr.dangrsg, 0)) / 10000, 2) as dangrsg,\n" + 
	         " round(sum(nvl(ylj.leijsg, 0)) / 10000, 2) as yueljsg,\n" + 
//	         " round(sum(nvl(nlj.leijsg, 0)) / 10000, 2) as nianljsg,\n" + 
	         " decode(round(sum(nvl(jih.dangrjh, 0)) , 2),\n" + 
	         "    0,\n" + 
	         "     0,\n" + 
	         "     round( round(sum(nvl(dr.dangrsg, 0)) / 10000, 2) / round(sum(nvl(jih.dangrjh, 0)) , 2) * 100,2))\n" + 
	         "            as dangrdhl,\n" + 
	         " decode( round(sum(nvl(jih.leijjh, 0)) , 2),\n" + 
	         "     0,\n" + 
	         "     0,\n" + 
	         "        round( round(sum(nvl(ylj.leijsg, 0)) / 10000, 2) /  round(sum(nvl(jih.leijjh, 0)) , 2) * 100,2))\n" + 
	         "              as yueljdhl\n" + 
//	         " decode( round(sum(nvl(jih.nianleijjh, 0)) , 2),\n" + 
//	         "     0,\n" + 
//	         "      0,\n" + 
//	         "        round(  round(sum(nvl(nlj.leijsg, 0)) / 10000, 2) /  round(sum(nvl(jih.nianleijjh, 0)) , 2) * 100,2))\n" + 
//	         "             as nianljdhl\n" + 
	         " from diancxxb dc,\n" + 
	         "  dianckjpxb px,\n" + 
	         " vwdcgysmykjgl vw,\n" + 
	         " (select fk.diancxxb_id,\n" + 
	         "      fk.gongysb_id,\n" + 
	         "      nvl(fk.rijjhl, 0) as dangrjh,\n" + 
	         "      nvl(fk.duns, 0) as dangrsg\n" + 
	         "   from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
	         "  where fk.riq = to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
	         "   and vw.id = fk.gongysb_id\n" + 
	         "   and vw.gongyskjb_id = 1) dr,\n" + 
	         "  (select fk.diancxxb_id,\n" + 
	         "      fk.gongysb_id,\n" + 
	         "       sum(fk.rijjhl) as leijjh,\n" + 
	         "      sum(fk.duns) as leijsg\n" + 
	         "  from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
	         " where fk.riq >= first_day(to_date('"+End_riq+"', 'yyyy-mm-dd'))\n" + 
	         "   and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
	         "   and vw.id = fk.gongysb_id\n" + 
	         "   and vw.gongyskjb_id = 1\n" + 
	         " group by fk.diancxxb_id, fk.gongysb_id) ylj,\n" + 
	         "(select fk.diancxxb_id,\n" + 
	         "      fk.gongysb_id,\n" + 
	         "       sum(fk.rijjhl) as leijjh,\n" + 
	         "      sum(fk.duns) as leijsg\n" + 
	         "  from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
	         "  where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
	         "    and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
	         "   and vw.id = fk.gongysb_id\n" + 
	         "    and vw.gongyskjb_id = 1\n" + 
	         "  group by fk.diancxxb_id, fk.gongysb_id) nlj,\n" +
	         "(select distinct vw.id, fk.diancxxb_id,vw.gongysb_id --,fk.gongysb_id,decode(gy.fuid,0,gy.id,gy.fuid) as fuid \n" + 
	         "   from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
	         "   where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
	         "     and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
	         "    and vw.gongyskjb_id = 1\n" + 
	         "    and fk.gongysb_id = vw.id\n" +
	         " union\n" +
	         " select distinct vw.id, vw.diancxxb_id,vw.gongysb_id \n" +
	         " from niancgjhb n, vwdcgysmykjgl vw\n" +
	         " where n.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" +
	         "  and n.riq <= first_day(to_date('"+End_riq+"', 'yyyy-mm-dd'))\n" +
	         " and vw.gongyskjb_id = 1\n" +
	         " and n.gongysb_id = vw.gongysb_id\n" +
	         " and n.diancxxb_id=vw.gongysb_id\n" +
	         ") gys\n" + 
	         "  ,(  select v.id vgx_id,  njhylj.gongysb_id,njhylj.diancxxb_id,njhylj.dangrjh,njhylj.leijjh,njhnlj.nianleijjh,njhnlj.hej  from \n" + 
	         "   (\n" + 
	         "   select n.gongysb_id ,n.diancxxb_id ,n.riq,round(n.hej/(add_months(n.riq,1)-n.riq),2) as dangrjh ,\n" + 
	          "   round(n.hej/(add_months(n.riq,1)-n.riq),2)*(to_date('"+End_riq+"','yyyy-mm-dd')-first_day(to_date('"+End_riq+"','yyyy-mm-dd'))+1)as leijjh\n" + 
	          "  from niancgjhb n \n" + 
	          "   where n.riq=first_day(to_date('"+End_riq+"', 'yyyy-mm-dd'))\n" + 
	          "  )njhylj   ,\n" + 
	          " (\n" + 
	          "   select n.gongysb_id,n.diancxxb_id,sum(hej) hej,\n" + 
	          "   round(sum(n.hej)/(add_months(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd'),12)-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')),2)*(to_date('"+End_riq+"','yyyy-mm-dd')-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')+1) as nianleijjh\n" + 
	           "    from niancgjhb n \n" + 
	           "   where  n.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd') \n" + 
	           "   and    n.riq<=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-12-1','yyyy-mm-dd')\n" + 
	           "   group by n.gongysb_id,n.diancxxb_id\n" + 
	           " )njhnlj,\n" + 
	           " vwdcgysmykjgl v\n" + 
	           "  where  njhylj.gongysb_id=njhnlj.gongysb_id\n" + 
	      " and    njhylj.diancxxb_id=njhnlj.diancxxb_id \n" + 
	      " and v.gongysb_id=njhnlj.gongysb_id\n" + 
	      " and v.diancxxb_id=njhnlj.diancxxb_id\n" + 
	      " and v.gongyskjb_id = 1\n"+
	      " )jih\n" + 
	      " where dr.gongysb_id(+) = gys.id\n" + 
	      "  and ylj.gongysb_id(+) = gys.id\n" + 
	      "  and nlj.gongysb_id(+) = gys.id \n" + 
	      " and jih.vgx_id(+)=gys.id\n" + 
	      " and gys.diancxxb_id = dc.id\n" + 
	      " and gys.id = vw.id\n" + 
	      " and px.diancxxb_id = dc.id\n" + 
	      " and px.kouj = '月报'\n"  
	       + strGongsID +
	      " group by rollup(px.shujsbzt, dc.mingc, vw.gongysmy)\n" + 
	      " having round(sum(nvl(nlj.leijjh, 0)) / 10000, 2) <> 0 or round(sum(nvl(nlj.leijsg, 0)) / 10000, 2) <> 0\n" + 
	      " order by grouping(px.shujsbzt) desc,\n" + 
	      "      min(px.xuh),\n" + 
	      "    grouping(dc.mingc) desc,\n" + 
	      "   min(px.xuh),\n" + 
	      "    grouping(vw.gongysmy) desc,\n" + 
	      "    vw.gongysmy\n" ;*/
			
//------------------------------添加年累计--------------------------------------			
			sbsql=  " select decode(grouping(lb.mingc)+grouping(dc.mingc)+grouping(g.quanc),3,'总计',2,lb.mingc||'小计',1,dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||g.quanc||getMeiyMingc(dc.id,g.id)) as danw,\n" + 
			  " round(sum(nvl(jih.nianjhl, 0)) , 2) as nianjhl,\n" + 
			  " round(sum(nvl(jih.dangnlj, 0)) , 2) as nianljjh,\n" + 
			  "round(sum(nvl(jih.dangrjh, 0)) , 2) as dangrjh,\n" + 
			  "round(sum(nvl(jih.dangylj, 0)) , 2) as yueljjh,\n" + 
			
			  " round(sum(nvl(dr.dangrsg, 0)) / 10000, 2) as dangrsg,\n" + 
			  " round(sum(nvl(ylj.leijsg, 0)) / 10000, 2) as yueljsg,\n" + 
			  " round(sum(nvl(ylj.nianljsg, 0)) / 10000, 2) as nianljsg,\n" + 
			  " decode(round(sum(nvl(jih.dangrjh, 0)) , 2), 0, 0,round( round(sum(nvl(dr.dangrsg, 0)) / 10000, 2) / round(sum(nvl(jih.dangrjh, 0)) , 2) * 100,2))\n" + 
			  "            as dangrdhl,\n" + 
			  " decode( round(sum(nvl(jih.dangylj, 0)),2),0,0,round( round(sum(nvl(ylj.leijsg, 0)) / 10000, 2) /  round(sum(nvl(jih.dangylj, 0)) , 2) * 100,2))\n" + 
			  "             as yueljdhl,  \n" +     
			  " decode( round(sum(nvl(jih.dangnlj, 0)),2),0,0,round( round(sum(nvl(ylj.nianljsg, 0)) / 10000, 2) /  round(sum(nvl(jih.dangnlj, 0)) , 2) * 100,2))\n" + 
			  "             as nianljdhl  \n" +     

			  " from diancxxb dc,  dianclbb lb, dianckjpxb px,\n" + 
			  "(select  vw.gongysb_id,vw.diancxxb_id,sum(nvl(fk.duns, 0)) as dangrsg\n" + 
			  "         from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  "       where fk.riq = to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  "       and vw.id = fk.gongysb_id\n" + 
			  "        and vw.gongyskjb_id = 1\n" + 
			  "       group by vw.diancxxb_id, vw.gongysb_id) dr,\n" + 
			  "(select vw.diancxxb_id,vw.gongysb_id,\n" + 
			  "sum(decode(to_char(fk.riq,'yyyy-mm'),to_char(to_date('"+End_riq+"', 'yyyy-mm-dd'),'yyyy-mm'),fk.duns,0)) as leijsg,sum(fk.duns) as nianljsg  \n"+
//			  "sum(decode(to_char(fk.riq,'yyyy-mm'),to_char(to_date('"+End_riq+"', 'yyyy-mm-dd'),'yyyy-mm'),fk.duns,0))) as leijsg,sum(fk.duns) as nianljsg \n"+

			  " from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  "where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  " and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  " and vw.id = fk.gongysb_id\n" + 
			  " and vw.gongyskjb_id = 1\n" + 
			  " group by vw.diancxxb_id, vw.gongysb_id) ylj,\n" + 
			  "(select vw.diancxxb_id, vw.gongysb_id,\n" + 
			  "     sum(fk.duns) as leijsg\n" + 
			  " from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  " where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  "   and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  "  and vw.id = fk.gongysb_id\n" + 
			  "  and vw.gongyskjb_id = 1\n" + 
			  " group by vw.diancxxb_id, vw.gongysb_id) nlj,\n" + 
			  "(select distinct vw.diancxxb_id,vw.gongysb_id \n" + 
			  "  from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  "  where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  "   and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  "   and vw.gongyskjb_id = 1\n" + 
			  "  and fk.gongysb_id = vw.id\n" + 
			  " union\n" + 
			  " select n.diancxxb_id,n.gongysb_id \n" + 
			  " from niandjh n\n" + 
			  " where n.riq = to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  "	) gys ,gongysb g,\n" + 
			  "(select diancxxb_id,gongysb_id, n.nianjhl,\n" + 
			  "  round(n.nianjhl/(add_months(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd'),12)-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')),2) as dangrjh,\n" + 
			  "  round(n.nianjhl/(add_months(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd'),12)-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')),2)*( to_date('"+End_riq+"','yyyy-mm-dd')-first_day(to_date('"+End_riq+"','yyyy-mm-dd'))+1) as dangylj,\n" + 
			  "  round(n.nianjhl/(add_months(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd'),12)-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')),2)*(to_date('"+End_riq+"','yyyy-mm-dd')-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')+1)  as dangnlj\n" + 
			  "  from niandjh n\n" + 
			  "     where riq=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')\n" + 
			  " )jih\n" + 
			 
			  " where dr.gongysb_id(+) = gys.gongysb_id\n" + 
			  "  and dr.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and ylj.gongysb_id(+) = gys.gongysb_id\n" + 
			  "  and ylj.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and nlj.gongysb_id(+) = gys.gongysb_id \n" + 
			  "  and nlj.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and jih.gongysb_id(+)=gys.gongysb_id\n" + 
			  "  and jih.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and g.id=gys.gongysb_id\n" + 
			  "  and dc.id=gys.diancxxb_id\n" + 
			  "  and dc.id=px.diancxxb_id\n" + 
			  " and px.kouj = '月报' and dc.dianclbb_id=lb.id\n" 
			  + strGongsID +
			  "  group by rollup(lb.mingc ,(dc.mingc,dc.id),(g.quanc,g.id))  \n" + 
			  " order by  grouping(lb.mingc ) desc,min(lb.xuh) ,min(px.xuh),grouping(dc.mingc) desc,min(px.xuh),grouping(g.quanc) desc,g.quanc\n" ;
			
		}else{   //分矿分厂统计
			
			sbsql=  " select decode(grouping(lb.mingc)+grouping(dc.mingc)+grouping(g.quanc),3,'总计',2,lb.mingc||'小计',1,g.quanc||getMeiyMingc(dc.id,g.id),'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n" + 
			  " round(sum(nvl(jih.nianjhl, 0)) , 2) as nianjhl,\n" + 
			  " round(sum(nvl(jih.dangnlj, 0)) , 2) as nianljjh,\n" + 
			  "round(sum(nvl(jih.dangrjh, 0)) , 2) as dangrjh,\n" + 
			  "round(sum(nvl(jih.dangylj, 0)) , 2) as yueljjh,\n" + 
			
			  " round(sum(nvl(dr.dangrsg, 0)) / 10000, 2) as dangrsg,\n" + 
			  " round(sum(nvl(ylj.leijsg, 0)) / 10000, 2) as yueljsg,\n" + 
			  " round(sum(nvl(ylj.nianljsg, 0)) / 10000, 2) as nianljsg,\n" + 
			  " decode(round(sum(nvl(jih.dangrjh, 0)) , 2), 0, 0,round( round(sum(nvl(dr.dangrsg, 0)) / 10000, 2) / round(sum(nvl(jih.dangrjh, 0)) , 2) * 100,2))\n" + 
			  "            as dangrdhl,\n" + 
			  " decode( round(sum(nvl(jih.dangylj, 0)),2),0,0,round( round(sum(nvl(ylj.leijsg, 0)) / 10000, 2) /  round(sum(nvl(jih.dangylj, 0)) , 2) * 100,2))\n" + 
			  "             as yueljdhl,  \n" +     
			  " decode( round(sum(nvl(jih.dangnlj, 0)),2),0,0,round( round(sum(nvl(ylj.nianljsg, 0)) / 10000, 2) /  round(sum(nvl(jih.dangnlj, 0)) , 2) * 100,2))\n" + 
			  "             as nianljdhl  \n" +     

			  " from diancxxb dc,  dianclbb lb, dianckjpxb px,\n" + 
			  "(select  vw.gongysb_id,vw.diancxxb_id,sum(nvl(fk.duns, 0)) as dangrsg\n" + 
			  "         from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  "       where fk.riq = to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  "       and vw.id = fk.gongysb_id\n" + 
			  "        and vw.gongyskjb_id = 1\n" + 
			  "       group by vw.diancxxb_id, vw.gongysb_id) dr,\n" + 
			  "(select vw.diancxxb_id,vw.gongysb_id,\n" + 
			  "sum(decode(to_char(fk.riq,'yyyy-mm'),to_char(to_date('"+End_riq+"', 'yyyy-mm-dd'),'yyyy-mm'),fk.duns,0)) as leijsg,sum(fk.duns) as nianljsg  \n"+
//			  "sum(decode(to_char(fk.riq,'yyyy-mm'),to_char(to_date('"+End_riq+"', 'yyyy-mm-dd'),'yyyy-mm'),fk.duns,0))) as leijsg,sum(fk.duns) as nianljsg \n"+

			  " from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  "where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  " and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  " and vw.id = fk.gongysb_id\n" + 
			  " and vw.gongyskjb_id = 1\n" + 
			  " group by vw.diancxxb_id, vw.gongysb_id) ylj,\n" + 
			  "(select vw.diancxxb_id, vw.gongysb_id,\n" + 
			  "     sum(fk.duns) as leijsg\n" + 
			  " from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  " where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  "   and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  "  and vw.id = fk.gongysb_id\n" + 
			  "  and vw.gongyskjb_id = 1\n" + 
			  " group by vw.diancxxb_id, vw.gongysb_id) nlj,\n" + 
			  "(select distinct vw.diancxxb_id,vw.gongysb_id \n" + 
			  "  from fenkshcrb fk, vwdcgysmykjgl vw\n" + 
			  "  where fk.riq >= to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  "   and fk.riq <= to_date('"+End_riq+"', 'yyyy-mm-dd')\n" + 
			  "   and vw.gongyskjb_id = 1\n" + 
			  "  and fk.gongysb_id = vw.id\n" + 
			  " union\n" + 
			  " select n.diancxxb_id,n.gongysb_id \n" + 
			  " from niandjh n\n" + 
			  " where n.riq = to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01', 'yyyy-mm-dd')\n" + 
			  "	) gys ,gongysb g,\n" + 
			  "(select diancxxb_id,gongysb_id, n.nianjhl,\n" + 
			  "  round(n.nianjhl/(add_months(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd'),12)-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')),2) as dangrjh,\n" + 
			  "  round(n.nianjhl/(add_months(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd'),12)-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')),2)*( to_date('"+End_riq+"','yyyy-mm-dd')-first_day(to_date('"+End_riq+"','yyyy-mm-dd'))+1) as dangylj,\n" + 
			  "  round(n.nianjhl/(add_months(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd'),12)-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')),2)*(to_date('"+End_riq+"','yyyy-mm-dd')-to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')+1)  as dangnlj\n" + 
			  "  from niandjh n\n" + 
			  "     where riq=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-1-1','yyyy-mm-dd')\n" + 
			  " )jih\n" + 
			 
			  " where dr.gongysb_id(+) = gys.gongysb_id\n" + 
			  "  and dr.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and ylj.gongysb_id(+) = gys.gongysb_id\n" + 
			  "  and ylj.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and nlj.gongysb_id(+) = gys.gongysb_id \n" + 
			  "  and nlj.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and jih.gongysb_id(+)=gys.gongysb_id\n" + 
			  "  and jih.diancxxb_id(+)=gys.diancxxb_id\n" + 
			  "  and g.id=gys.gongysb_id\n" + 
			  "  and dc.id=gys.diancxxb_id\n" + 
			  "  and dc.id=px.diancxxb_id\n" + 
			  " and px.kouj = '月报' and dc.dianclbb_id=lb.id\n" 
			  + strGongsID +
			  "  group by rollup(lb.mingc,(g.quanc,g.id) ,(dc.mingc,dc.id))  \n" + 
			  " order by  grouping(lb.mingc ) desc,min(lb.xuh) ,grouping(g.quanc) desc,g.quanc,min(px.xuh),grouping(dc.mingc) desc\n" ;
			
		}
/*
		}else{
//			统计大供应商和小供应商的明细，如果电厂将数据录到大供应商上，则将该供应商视为小供应商，其fuid就是本身的ID
			sbsql = 
				"select case when grouping(xgys.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||xgys.mingc else\n" +
				"       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||decode(gy.mingc,'大同煤矿集团','同煤本部','大同煤业','同煤本部',gy.mingc) else\n" + 
				"       case when grouping(dc.mingc)=0 then dc.mingc else\n" + 
				"       case when grouping(px.shujsbzt)=0 then decode(px.shujsbzt,1,'电厂合计',0,'运城、新余合计') else '总计' end end end end as danwmc,\n" + 
				"       round(sum(nvl(dr.dangrjh,0))/10000,2) as dangrjh,\n" + 
				"       round(sum(nvl(ylj.leijjh,0))/10000,2) as yueljjh,\n" + 
				"       round(sum(nvl(nlj.leijjh,0))/10000,2) as nianljjh,\n" + 
				"       round(sum(nvl(dr.dangrsg,0))/10000,2) as dangrsg,\n" + 
				"       round(sum(nvl(ylj.leijsg,0))/10000,2) as yueljsg,\n" + 
				"       round(sum(nvl(nlj.leijsg,0))/10000,2) as nianljsg,\n" + 
				"       decode(sum(nvl(dr.dangrjh,0)),0,0,round((sum(nvl(dr.dangrsg,0))/sum(nvl(dr.dangrjh,0)))*100,2)) as dangrdhl,\n" + 
				"       decode(sum(nvl(ylj.leijjh,0)),0,0,round((sum(nvl(ylj.leijsg,0))/sum(nvl(ylj.leijjh,0)))*100,2)) as yueljdhl,\n" + 
				"       decode(sum(nvl(nlj.leijjh,0)),0,0,round((sum(nvl(nlj.leijsg,0))/sum(nvl(nlj.leijjh,0)))*100,2)) as nianljdhl\n" + 
				"  from diancxxb dc,dianckjpxb px,gongysb gy,gongysb xgys,\n" + 
				"\t     (select fk.diancxxb_id,fk.gongysb_id,nvl(fk.rijjhl,0) as dangrjh,nvl(fk.duns,0) as dangrsg from fenkshcrb fk\n" + 
				"         where fk.riq=to_date('"+End_riq+"','yyyy-mm-dd') "+strLeix+" )dr,\n" + 
				"       (select fk.diancxxb_id,fk.gongysb_id,sum(fk.rijjhl) as leijjh,sum(fk.duns) as leijsg from fenkshcrb fk\n" + 
				"         where fk.riq>=first_day(to_date('"+End_riq+"','yyyy-mm-dd')) and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd')\n" + 
				"           "+strLeix+"  group by fk.diancxxb_id,fk.gongysb_id ) ylj,\n" + 
				"       (select fk.diancxxb_id,fk.gongysb_id,sum(fk.rijjhl) as leijjh,sum(fk.duns) as leijsg from fenkshcrb fk\n" + 
				"         where fk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd')\n" + 
				"           "+strLeix+"  group by fk.diancxxb_id,fk.gongysb_id ) nlj,\n" + 
				"\n" + 
				"       (select fk.diancxxb_id,fk.gongysb_id as id,decode(gy.fuid,0,gy.id,gy.fuid) as fuid from fenkshcrb fk,gongysb gy\n" + 
				"         where fk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd')\n" + 
				"           "+strLeix+" and fk.gongysb_id=gy.id ) gys\n" + 
				"\n" + 
				" where dr.diancxxb_id(+)=gys.diancxxb_id and ylj.diancxxb_id(+)=gys.diancxxb_id and nlj.diancxxb_id(+)=gys.diancxxb_id\n" + 
				"   and dr.gongysb_id(+)=gys.id and ylj.gongysb_id(+)=gys.id and nlj.gongysb_id(+)=gys.id and gys.fuid=gy.id and gys.id=xgys.id\n" + 
				"   and gys.diancxxb_id=dc.id and px.diancxxb_id=dc.id and px.kouj='月报'\n" + strGongsID +
				" group by rollup(px.shujsbzt,dc.mingc,gy.mingc,xgys.mingc)\n" + 
				" order by grouping(px.shujsbzt) desc,min(px.xuh),grouping(dc.mingc) desc,min(px.xuh),grouping(gy.mingc) desc,gy.mingc,grouping(xgys.mingc) desc,xgys.mingc";
		}
		*/
//		System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][10];
		ArrHeader[0]=new String[] {"单位","计划(万吨)","计划(万吨)","计划(万吨)","实供(万吨)","实供(万吨)","实供(万吨)","到货率(%)","到货率(%)","到货率(%)"};
		ArrHeader[1]=new String[] {"单位","当日","月累计","年累计","当日","月累计","年累计","当日","月累计","年累计"};

		int ArrWidth[]=new int[] {380,50,50,60,50,60,60,60,60,50,50};
		if(getBaoblxValue().getId()==1 || getBaoblxValue().getId()==5){
			 ArrHeader=new String[2][10];
			ArrHeader[0]=new String[] {"单位","计划(万吨)","计划(万吨)","计划(万吨)","计划(万吨)","实供(万吨)","实供(万吨)","实供(万吨)","到货率(%)","到货率(%)","到货率(%)"};
			ArrHeader[1]=new String[] {"单位","年计划","年累计","当日","月累计","当日","月累计","年累计","当日","月累计","年累计"};

			 ArrWidth=new int[] {380,50,50,60,50,60,60,60,60,50,50};
		}
		rt.setTitle(FormatDate(DateUtil.getDate(End_riq)).substring(0,8)+"份燃料"+getBaoblxValue().getValue().substring(0,2)+"合同兑现情况表", ArrWidth);
		rt.setDefaultTitle(3, 3, FormatDate(DateUtil.getDate(End_riq)), Table.ALIGN_CENTER);
		rt.setBody(new Table(rs,2, 0, 1));
		
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		
		// 设置页数
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

	private boolean _UploadClick = false;

	public void UploadButton(IRequestCycle cycle) {
		_UploadClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
		if (_UploadClick) {
			_UploadClick = false;
			UploadData();
		}
	}

	public void UploadData(){
		
		JDBCcon con = new JDBCcon();
		String strMsg = "";
		String End_riq=getEndriqDate();
		ResultSetList rsl;
		String sql = "select s.*,dc.mingc as dcmc,decode(s.gysbm,'999999999','其它（补充合同）',gy.mingc) as gysmc from diancxxb dc,gongysb gy ,"
			+"(select rb.diancxxb_id,decode(substr(gy.bianm,0,6),'212198','212199',substr(gy.bianm,0,6)) as gysbm,"//辽宁小矿编码：大唐国际的212198，中国大唐的212199
			+"to_char(rb.riq,'yyyy-mm-dd') as riq,'重点' as leix,sum(rb.ches) as ches,sum(rb.duns) as duns\n" +
			"  from fenkshcrb rb,vwdcgysmykjgl vw,gongysb gy\n" + 
			" where rb.gongysb_id=vw.id and vw.gongyskjb_id=1 and vw.gongysb_id=gy.id\n" + 
			"   and rb.riq=to_date('"+End_riq+"','yyyy-mm-dd')\n" + 
			" group by (rb.diancxxb_id,gy.fuid,substr(gy.bianm,0,6),rb.riq)\n" + 
			" union	\n" + 
			"select rb.diancxxb_id,'999999999' as gysbm,to_char(rb.riq,'yyyy-mm-dd') as riq,'重点' as leix,sum(rb.ches) as ches,sum(rb.duns) as duns\n" + 
			"  from fenkshcrb rb,vwdcgysmykjgl vw where rb.gongysb_id=vw.id and vw.gongyskjb_id=2\n" + 
			"   and rb.riq=to_date('"+End_riq+"','yyyy-mm-dd')\n" + 
			" group by (rb.diancxxb_id,rb.riq) ) s  where s.diancxxb_id=dc.id and s.gysbm=gy.bianm(+) order by dc.mingc,gy.mingc ";
		
		rsl = con.getResultSetList(sql);
		
		String gysSql = "";
		String strHtml = "";
		String strDiancmc = "";
		String strGongysmc = "";
		String strGysbm = "";
		StringBuffer msgInfo = new StringBuffer();
		
		int count = 0;
		while(rsl.next()){
			strDiancmc = rsl.getString("dcmc");
			strGongysmc = rsl.getString("gysmc");
			strGysbm = rsl.getString("gysbm");
			gysSql = "select nvl(max(id),0) as id from vwdiancgysgl gy where gy.gysbm='"+rsl.getString("gysbm")+"' and gy.dcid="+rsl.getLong("diancxxb_id");
			
			strHtml = getJtDcGysInfo(con,gysSql,"Select");
			
			if(strHtml.equals("false")){//false:没有找到电厂供应商关联关系
				
				if(msgInfo.length()==0){
					strMsg = "请先在集团燃料系统里的电厂供应商关联功能下，添加电厂供应商关联信息。<br>设置完成以后请重新上报数据。缺少信息如下：<br>'+ \n";
					msgInfo.append("'<table style=font-family:宋体;border-color:#000000;font-size:9pt;>'+ \n");
					msgInfo.append("'<tr>*************************************************************</tr>'+ \n");
					msgInfo.append("'<tr><td>电厂名称</td><td>供应商名称</td><td>供应商编码</td></tr>'+ \n");
					msgInfo.append("'<tr><td colspan=3>----------------------------------------------------------------------</td></tr>'+ \n");
				}
				msgInfo.append( "'<tr><td>"+strDiancmc+"</td><td>"+strGongysmc+"</td><td>"+strGysbm+"</td></tr>'+ \n");
				
				count++;
				if(count==15){//最多只显示15行信息
					break;
				}
			}
		}
		
		if(msgInfo.length()!=0){
			strMsg = strMsg + msgInfo.toString()+"'</table>";
			
		}else{
			rsl.beforefirst();
			
			long diancxxb_id = -1;
			String strsql = "";
			int i = 0;
			StringBuffer sb = new StringBuffer();
			sb.append("begin \n");
			
			while(rsl.next()){
				i++;
				if(diancxxb_id != rsl.getLong("diancxxb_id")){
					diancxxb_id = rsl.getLong("diancxxb_id");
					sb.append("delete from fenkshcrb rb where rb.riq=to_date('"+End_riq+"','yyyy-mm-dd') and rb.diancxxb_id="+diancxxb_id+";\n");
				}
				strsql = "insert into fenkshcrb (id, diancxxb_id, gongysb_id, riq, leix, ches, duns, shangbzt) values ( "
					+"getnewid("+rsl.getLong("diancxxb_id")+"), "+rsl.getLong("diancxxb_id")
						+", (select max(id) as id from vwdiancgysgl gy where gy.gysbm='"+rsl.getString("gysbm")+"' and gy.dcid="+rsl.getLong("diancxxb_id")
						+"), to_date('"+rsl.getString("riq")+"','yyyy-mm-dd'), "
						+"'"+rsl.getString("leix")+"', "+rsl.getInt("ches")+", "+rsl.getDouble("duns")+", 0);";
				
				sb.append(strsql +"\n");
//				System.out.println(strsql);
			}
			sb.append("end;");
			if(i<=0){
				strMsg = "没有数据可上传！"; 
			}else{
				strMsg = getJtDcGysInfo(con,sb.toString(),"Update");
			}
		}
		con.Close();
		setMsg(strMsg);
	}
	
	public String getJtDcGysInfo(JDBCcon con,String strSql,String strLeix){//参数说明：数据库连接，执行的SQL，执行类型（Select\Update）
		
		ResultSet rs;//用来存放集团数据库连接信息
		ResultSetList rsl;//用来存放strSql查询结果
		String strMsg = "";
		int result = -1;
		
		String mhostname = "";
		String msid = "";
		String mduank = "";
		String myonghm = "";
		String mmim = "";
		String ljsql = "select pz.* from diancxtpz pz where pz.jitdcid=100";
			
		try{
			rs = con.getResultSet(ljsql);
			if(rs.next()){
				 mhostname = rs.getString("ip");
				 msid = rs.getString("sid");
				 mduank = rs.getString("duank");
				 myonghm = rs.getString("yonghm");
				 mmim = rs.getString("mim");
			}

			DBconn dbcn=new DBconn(mhostname,mduank,msid);
			dbcn.setAutoCommit(false);
			dbcn.setUserName(myonghm);
			dbcn.setPassword(mmim);
			
			if("Select".equals(strLeix)){
				rsl = dbcn.getResultSetList(strSql);
				if(rsl.next()){
					if(rsl.getLong(0)>0){//集团有该电厂与供应商的关联关系
						strMsg = "true";
						dbcn.close();
					}else{
						strMsg = "false";//在集团没有找到电厂供应商的关联关系
						dbcn.close();
					}
				}else{
					strMsg = "false";//在集团没有找到电厂供应商的关联关系
					dbcn.close();
				}
			
			}else if("Update".equals(strLeix)){
				result = dbcn.getUpdate(strSql);
				if(result<0){
					 strMsg = "数据上报失败！";
					 dbcn.rollBack();
					 dbcn.close();
				}else{
					 strMsg = "数据上报成功！";
					 dbcn.commit();
					 dbcn.close();
				}
			}
			
		}catch(Exception e){
			 strMsg = "数据操作失败！";
			 con.Close();
			 System.out.println("执行以下SQL语句失败：\n"+strSql);
			 e.printStackTrace();
		}finally{
//			 con.Close();
		}
		return strMsg;
	}
	
//	public String getPageHome() {
//		if (((Visit) getPage().getVisit()).getboolean1()) {
//			return "window.location = '" + MainGlobal.getHomeContext(this)
//			+ "';";
//		} else {
//			return "";
//		}
//	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
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
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		/*tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("TongjkjDropDown");
		cb1.setId("Tongjkj");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));*/
		
//		供煤类型
		tb1.addText(new ToolbarText("统计类型:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("BaoblxDropDown");
		cb2.setId("Baoblx");
		cb2.setWidth(160);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton sbtb = new ToolbarButton(null,"数据上报","function(){document.getElementById('UploadButton').click();}");
		tb1.addItem(sbtb);

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
			visit.setString4(null);
			visit.setString5(null);

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
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
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
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
//	分公司下拉框
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
			.setDropDownBean4((IDropDownBean) getFengsModel()
					.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
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
		setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
	}

//	得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
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

//	报表类型
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List leixList = new ArrayList();
		leixList.add(new IDropDownBean(3,"分矿到货情况"));
		leixList.add(new IDropDownBean(4,"分矿到货情况_年累计"));
		leixList.add(new IDropDownBean(1,"重点"));
		leixList.add(new IDropDownBean(2,"补充"));
		leixList.add(new IDropDownBean(5,"分矿分厂"));
		
		
//		leixList.add(new IDropDownBean(3,"补充明细"));

		_IBaoblxModel = new IDropDownModel(leixList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
//	报表统计口径
	public boolean _Tongjkjchange = false;
	private IDropDownBean _TongjkjValue;

	public IDropDownBean getTongjkjValue() {
		if(_TongjkjValue==null){
			_TongjkjValue=(IDropDownBean)getITongjkjModels().getOption(0);
		}
		return _TongjkjValue;
	}

	public void setTongjkjValue(IDropDownBean Value) {
		long id = -2;
		if (_TongjkjValue != null) {
			id = _TongjkjValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Tongjkjchange = true;
			} else {
				_Tongjkjchange = false;
			}
		}
		_TongjkjValue = Value;
	}

	private IPropertySelectionModel _ITongjkjModel;

	public void setITongjkjModel(IPropertySelectionModel value) {
		_ITongjkjModel = value;
	}

	public IPropertySelectionModel getITongjkjModel() {
		if (_ITongjkjModel == null) {
			getITongjkjModels();
		}
		return _ITongjkjModel;
	}

	public IPropertySelectionModel getITongjkjModels() {
		JDBCcon con = new JDBCcon();
		try{
		List tongjkjList = new ArrayList();
		tongjkjList.add(new IDropDownBean(0,"分厂分矿"));
		tongjkjList.add(new IDropDownBean(1,"分矿分厂"));

		_ITongjkjModel = new IDropDownModel(tongjkjList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _ITongjkjModel;
	}
	
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}


}