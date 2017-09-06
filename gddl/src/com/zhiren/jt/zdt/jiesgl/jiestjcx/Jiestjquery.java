package com.zhiren.jt.zdt.jiesgl.jiestjcx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */
/*
 * 作者：liht
 * 日期：2010-04-23
 * 描述：报表中增加结算热值列，并可设置报表中可显示项
 */
/*
 * 作者：夏峥
 * 时间：2011-05-27
 * 描述：修改结算统计报表
 * 		 复制分厂选项对应的报表更名为分厂分矿报表。
 * 		分厂报表只显示电厂信息
 */
/*
 * 作者：赵胜男
 * 时间：2012-11-23
 * 描述：变更电厂树为多选电厂树
 *            入炉煤数量修改为列名结算数量
 * 		      取消年份月份下拉框改为日期段选择查询
 * 		
 */

public class Jiestjquery extends BasePage {
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	public boolean getRaw() {
		return true;
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}
	
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	private String REPORT_JIESTJQUERY="jiestjquery";
	private String REPORT_JIESTJZB="jiestjzb";//结算统计总表
	private String mstrReportName="";
	
	//得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		
		if(! isBegin){
			return "";
		}
		
		if(mstrReportName.equals(REPORT_JIESTJQUERY)){
			return getJiestjquery();
		}else if(mstrReportName.equals(REPORT_JIESTJZB)){
			return getJiestjzb();
		}else{
			return "无此报表";
		}
	}

	public String getJiestjquery(){
		Visit visit = (Visit) getPage().getVisit();

		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		String having = "";
//		int treejib = this.getDiancTreeJib();
//	
//		if(isGongsUser()){//分公司用户
			having = " having not (grouping(gs.mingc)=1 and grouping(dc.mingc)=1) ";
//			str = " and (dc.id in ("+getTreeid()+" )or dc.fuid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id() + "  or dc.shangjgsid="+((Visit)getPage().getVisit()).getDiancxxb_id() +")";
//			if (treejib == 3) {// 选电厂
//				having = " having not grouping(dc.mingc)=1 ";
				str = " and dc.id  in ( " + getTreeid() + " )";
//			}
//		}else if(isDiancUser()){//电厂用户
//			having = " having not grouping(dc.mingc)=1 ";
//			str = " and dc.id in ( " + ((Visit)getPage().getVisit()).getDiancxxb_id() + ") ";
//		}else{
//			having = "";
//			str = "";
//		}
		
		String strleibztbId = ""; 
		if(getZhuangtValue().getId()==1){
			strleibztbId = " and js.liucztb_id=1 ";
		}else if(getZhuangtValue().getId()==2){
			strleibztbId = " and js.liucztb_id=0 ";
		}else{
			strleibztbId = " ";
		}
		
		StringBuffer sbsql = new StringBuffer();
		//新增日期形式
//		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
//		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
		if(beginRiq.equals(endRiq)){
			beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
		}
		if(getBaoblxValue().getValue().equals("分厂")){
			sbsql.setLength(0);
			sbsql.append("select case when grouping(gy.mingc)=0 then '&emsp;&emsp;&emsp;'||gy.mingc else \n");
			sbsql.append("       case when grouping(kj1.mingc)=0 then '&emsp;'||kj1.mingc else \n");
			sbsql.append("       case when grouping(dc.mingc)=0 then dc.mingc else \n");
			sbsql.append("       case when grouping(gs.mingc)=0 then gs.mingc else '总计' end end  end end 供煤单位,  \n");
			sbsql.append("       sj.fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0, \n");
			sbsql.append("         round_new(round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as hansbmdj, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0, \n");
			sbsql.append("         round_new((round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)-round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)-round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2))*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as buhsbmdj \n");
			sbsql.append("  from  \n");
			sbsql.append("  (select fx.*,nvl(sj.jiessl,0) as jiessl,nvl(farl,0) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("          nvl(sj.jiesrl, 0) as jiesrl, \n");
			sbsql.append("          nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,nvl(sj.zengzs,0) as zengzs,nvl(sj.kuangyf,0) as kuangyf,nvl(sj.jiaohqyzf,0) as jiaohqyzf, \n");
			sbsql.append("          nvl(sj.tielyf,0) as tielyf,nvl(sj.tielyfs,0) as tielyfs,nvl(sj.tielzf,0) as tielzf,nvl(sj.qiyf,0) as qiyf,nvl(sj.qiys,0) as qiys,nvl(sj.qiyzf,0) as qiyzf, \n");
			sbsql.append("          nvl(sj.haiyf,0) as haiyf,nvl(haiys,0) as haiys,nvl(sj.gangzf,0) as gangzf,nvl(sj.qitfy,0) as qitfy from  \n");
			sbsql.append("  (select diancxxb_id,jihkjb_id,gongysb_id,fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy from  \n");
			sbsql.append("    ( select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'本月','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +0 as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf, \n");
			sbsql.append("             0 as qitfy \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("              (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("                where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                  and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("               ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("        -------本月数据  \n");
			sbsql.append("    union \n");
			sbsql.append("      select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'累计','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +0 as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf, \n");
			sbsql.append("             0 as qitfy \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("            (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("              where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("             ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+"  \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd')  and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("     -------累计数据  \n");
			sbsql.append("      ) sj group by (diancxxb_id,jihkjb_id,gongysb_id,fenx)) sj,   \n");
			sbsql.append("     ( select * from ( \n");
	//		sbsql.append("           (select distinct js.diancxxb_id,ht.jihkjb_id,js.gongysb_id \n");
	//		sbsql.append("              from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc \n");
	//		sbsql.append("             where js.id=yf.diancjsmkb_id and js.hetb_id=ht.id and js.yunsfsb_id=ys.id and js.liucztb_id=zt.id \n");
	//		sbsql.append("               and zt.leibztb_id=0 and js.diancxxb_id=dc.id  \n");
	//		sbsql.append("               and js.jiesrq>=to_date('"+strdate+"-01-01','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)  \n");
	//		sbsql.append("         union \n");
			sbsql.append("            select distinct js.diancxxb_id,ht.jihkjb_id,js.gongysb_id \n");
			sbsql.append("              from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc \n");
			sbsql.append("             where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.yunsfsb_id=ys.id \n");
			sbsql.append("               "+strleibztbId+" and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("               and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd')  and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("            ) aa, \n");
			sbsql.append("           (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx  \n");
			sbsql.append("      ) fx  \n");
			sbsql.append("      where sj.diancxxb_id(+)=fx.diancxxb_id and sj.jihkjb_id(+)=fx.jihkjb_id and sj.gongysb_id(+)=fx.gongysb_id and sj.fenx(+)=fx.fenx) sj, \n");
			sbsql.append("      diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj1 \n");
			sbsql.append("	where sj.diancxxb_id=dc.id and sj.gongysb_id=gy.id and sj.jihkjb_id=kj1.id and dc.fuid=gs.id  \n");
			sbsql.append("group by grouping sets (sj.fenx,(kj1.mingc,sj.fenx),(gs.mingc,sj.fenx),(gs.mingc,kj1.mingc,sj.fenx),(dc.mingc,sj.fenx), \n");
			sbsql.append("    (dc.mingc,kj1.mingc,sj.fenx),(dc.mingc,kj1.mingc,gy.mingc,sj.fenx)) \n");
			
//			if(isGongsUser()){//分公司用户
				having = "HAVING ((grouping(kj1.mingc) +grouping(gs.mingc)+grouping(dc.mingc)=2 AND grouping(dc.mingc)=0)OR(grouping(kj1.mingc) +grouping(gs.mingc)+grouping(dc.mingc)=2 AND grouping(gs.mingc)=0))";
//				if (treejib == 3) {// 选电厂
//					having = "HAVING (grouping(kj1.mingc) +grouping(gs.mingc)+grouping(dc.mingc)=2 AND grouping(dc.mingc)=0)";
//				}
//			}else {
//				having = "HAVING (grouping(kj1.mingc) +grouping(gs.mingc)+grouping(dc.mingc)=2 AND grouping(dc.mingc)=0)";
//			}
			sbsql.append("  "+having+"  \n");
			sbsql.append("order by \n");
			sbsql.append("    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj1.mingc),1,4,3),0) desc, \n");
			sbsql.append("    gs.mingc, gs.mingc,dc.mingc,grouping(kj1.mingc) desc,min(kj1.xuh),grouping(gy.mingc) desc,gy.mingc,sj.fenx \n");
			   
		} else if(getBaoblxValue().getValue().equals("分厂分矿")){
			sbsql.setLength(0);
			sbsql.append("select case when grouping(gy.mingc)=0 then '&emsp;&emsp;&emsp;'||gy.mingc else \n");
			sbsql.append("       case when grouping(kj1.mingc)=0 then '&emsp;'||kj1.mingc else \n");
			sbsql.append("       case when grouping(dc.mingc)=0 then dc.mingc else \n");
			sbsql.append("       case when grouping(gs.mingc)=0 then gs.mingc else '总计' end end  end end 供煤单位,  \n");
			sbsql.append("       sj.fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0, \n");
			sbsql.append("         round_new(round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as hansbmdj, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0, \n");
			sbsql.append("         round_new((round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)-round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)-round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2))*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as buhsbmdj \n");
			sbsql.append("  from  \n");
			sbsql.append("  (select fx.*,nvl(sj.jiessl,0) as jiessl,nvl(farl,0) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("          nvl(sj.jiesrl, 0) as jiesrl, \n");
			sbsql.append("          nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,nvl(sj.zengzs,0) as zengzs,nvl(sj.kuangyf,0) as kuangyf,nvl(sj.jiaohqyzf,0) as jiaohqyzf, \n");
			sbsql.append("          nvl(sj.tielyf,0) as tielyf,nvl(sj.tielyfs,0) as tielyfs,nvl(sj.tielzf,0) as tielzf,nvl(sj.qiyf,0) as qiyf,nvl(sj.qiys,0) as qiys,nvl(sj.qiyzf,0) as qiyzf, \n");
			sbsql.append("          nvl(sj.haiyf,0) as haiyf,nvl(haiys,0) as haiys,nvl(sj.gangzf,0) as gangzf,nvl(sj.qitfy,0) as qitfy from  \n");
			sbsql.append("  (select diancxxb_id,jihkjb_id,gongysb_id,fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy from  \n");
			sbsql.append("    ( select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'本月','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +0 as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf, \n");
			sbsql.append("             0 as qitfy \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("              (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("                where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                  and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("               ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("        -------本月数据  \n");
			sbsql.append("    union \n");
			sbsql.append("      select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'累计','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +0 as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf, \n");
			sbsql.append("             0 as qitfy \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("            (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("              where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("             ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+"  \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("     -------累计数据  \n");
			sbsql.append("      ) sj group by (diancxxb_id,jihkjb_id,gongysb_id,fenx)) sj,   \n");
			sbsql.append("     ( select * from ( \n");
	//		sbsql.append("           (select distinct js.diancxxb_id,ht.jihkjb_id,js.gongysb_id \n");
	//		sbsql.append("              from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc \n");
	//		sbsql.append("             where js.id=yf.diancjsmkb_id and js.hetb_id=ht.id and js.yunsfsb_id=ys.id and js.liucztb_id=zt.id \n");
	//		sbsql.append("               and zt.leibztb_id=0 and js.diancxxb_id=dc.id  \n");
	//		sbsql.append("               and js.jiesrq>=to_date('"+strdate+"-01-01','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strdate+"','yyyy-mm-dd'),1)  \n");
	//		sbsql.append("         union \n");
			sbsql.append("            select distinct js.diancxxb_id,ht.jihkjb_id,js.gongysb_id \n");
			sbsql.append("              from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc \n");
			sbsql.append("             where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.yunsfsb_id=ys.id \n");
			sbsql.append("               "+strleibztbId+" and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("               and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("            ) aa, \n");
			sbsql.append("           (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx  \n");
			sbsql.append("      ) fx  \n");
			sbsql.append("      where sj.diancxxb_id(+)=fx.diancxxb_id and sj.jihkjb_id(+)=fx.jihkjb_id and sj.gongysb_id(+)=fx.gongysb_id and sj.fenx(+)=fx.fenx) sj, \n");
			sbsql.append("      diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj1 \n");
			sbsql.append("	where sj.diancxxb_id=dc.id and sj.gongysb_id=gy.id and sj.jihkjb_id=kj1.id and dc.fuid=gs.id  \n");
			sbsql.append("group by grouping sets (sj.fenx,(kj1.mingc,sj.fenx),(gs.mingc,sj.fenx),(gs.mingc,kj1.mingc,sj.fenx),(dc.mingc,sj.fenx), \n");
			sbsql.append("    (dc.mingc,kj1.mingc,sj.fenx),(dc.mingc,kj1.mingc,gy.mingc,sj.fenx)) \n");
			sbsql.append("  "+having+"  \n");
			sbsql.append("order by \n");
			sbsql.append("    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj1.mingc),1,4,3),0) desc, \n");
			sbsql.append("    gs.mingc, gs.mingc,dc.mingc,grouping(kj1.mingc) desc,min(kj1.xuh),grouping(gy.mingc) desc,gy.mingc,sj.fenx \n");
			
		}else if(getBaoblxValue().getValue().equals("分矿")){
			sbsql.setLength(0);
			sbsql.append("select case when grouping(dc.mingc)=0 then '&emsp;&emsp;&emsp;'||dc.mingc else \n");
			sbsql.append("       case when grouping(gy.mingc)=0 then '&emsp;'||gy.mingc else \n");
			sbsql.append("       case when grouping(kj1.mingc)=0 then kj1.mingc else '总计' end end end 供煤单位,  \n");
			sbsql.append("       sj.fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),2),0,0, \n");
			sbsql.append("         round_new(round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as hansbmdj, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),2),0,0, \n");
			sbsql.append("         round_new((round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)-round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)-round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2))*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),0))) as buhsbmdj \n");
			sbsql.append("  from  \n");
			sbsql.append("  (select fx.*,nvl(sj.jiessl,0) as jiessl,nvl(farl,0) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("          nvl(sj.jiesrl, 0) as jiesrl, \n");
			sbsql.append("          nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,nvl(sj.zengzs,0) as zengzs,nvl(sj.kuangyf,0) as kuangyf,nvl(sj.jiaohqyzf,0) as jiaohqyzf, \n");
			sbsql.append("          nvl(sj.tielyf,0) as tielyf,nvl(sj.tielyfs,0) as tielyfs,nvl(sj.tielzf,0) as tielzf,nvl(sj.qiyf,0) as qiyf,nvl(sj.qiys,0) as qiys,nvl(sj.qiyzf,0) as qiyzf, \n");
			sbsql.append("          nvl(sj.haiyf,0) as haiyf,nvl(haiys,0) as haiys,nvl(sj.gangzf,0) as gangzf,nvl(sj.qitfy,0) as qitfy from  \n");
			sbsql.append("  (select diancxxb_id,jihkjb_id,gongysb_id,fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy from  \n");
			sbsql.append("    ( select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'本月','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +0 as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf, \n");
			sbsql.append("             0 as qitfy \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("              (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("                where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                  and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("               ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("        -------本月数据  \n");
			sbsql.append("    union \n");
			sbsql.append("      select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'本月','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) \n");
			sbsql.append("                   +0 as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_tiel+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_gongl+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys, \n");
			sbsql.append("             decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf, \n");
			sbsql.append("             0 as qitfy \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("            (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("              where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("             ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("     -------累计数据  \n");
			sbsql.append("      ) sj group by (diancxxb_id,jihkjb_id,gongysb_id,fenx)) sj,   \n");
			sbsql.append("     ( select * from  \n");
			sbsql.append("           (select distinct js.diancxxb_id,ht.jihkjb_id,js.gongysb_id \n");
			sbsql.append("              from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc \n");
			sbsql.append("             where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.yunsfsb_id=ys.id  \n");
			sbsql.append("               "+strleibztbId+" and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("               and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("            ) aa, \n");
			sbsql.append("           (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx  \n");
			sbsql.append("      ) fx  \n");
			sbsql.append("      where sj.diancxxb_id(+)=fx.diancxxb_id and sj.jihkjb_id(+)=fx.jihkjb_id and sj.gongysb_id(+)=fx.gongysb_id and sj.fenx(+)=fx.fenx ) sj, \n");
			sbsql.append("      diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj1 \n");
			sbsql.append("where sj.diancxxb_id=dc.id and sj.gongysb_id=gy.id and sj.jihkjb_id=kj1.id and dc.fuid=gs.id  \n");
			sbsql.append("group by grouping sets (sj.fenx,(kj1.mingc,sj.fenx),(gy.mingc,sj.fenx),(gy.mingc,dc.mingc,sj.fenx)) \n");
			sbsql.append("order by \n");
			sbsql.append("    decode(grouping(dc.mingc)+grouping(gy.mingc)+grouping(kj1.mingc),3,3,0) desc,min(kj1.xuh), \n");
			sbsql.append("    grouping(gy.mingc) desc,gy.mingc,grouping(dc.mingc) desc,dc.mingc,grouping(kj1.mingc) desc,sj.fenx \n");
			 
		}
			
		ResultSetList rs = cn.getResultSetList(sbsql.toString());
	//System.out.print(sbsql.toString());
		if (rs == null) {
			return null;
		}
		
		ResultSetList rsl = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String [] Zidm = null;
		StringBuffer SB = new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='MEITJSTJ' " 
					+ "order by xuh");
        ResultSetList r = cn.getResultSetList(SB.toString());
		
        if (r.getRows() > 0) {
        	ArrWidth = new int[r.getRows()];
        	strFormat = new String[r.getRows()];
        	String biaot = r.getString(0,1);
        	String [] Arrbt = biaot.split("!@");
        	ArrHeader = new String [Arrbt.length][r.getRows()];
        	Zidm = new String[r.getRows()];
        	rsl = new ResultSetList();
        	while (r.next()) {
        		Zidm[r.getRow()]=r.getString("zidm");
        		ArrWidth[r.getRow()]=r.getInt("kuand");
        		strFormat[r.getRow()]=r.getString("format") == null?"":r.getString("format");
        		String[] title=r.getString("biaot").split("!@");
        		for(int i = 0; i < title.length; i++){
        			ArrHeader[i][r.getRow()]=title[i];
        		}
        	}
        	rsl.setColumnNames(Zidm);
        	while (rs.next()) {
        		rsl.getResultSetlist().add(rs.getArrString(Zidm));
        	}
        } else {
        	rsl = rs;
    		ArrHeader = new String[][] {{"单位","本月<br>累计","结算数量<br>(吨)","入厂热值<br>(MJ/kg)","结算热值<br>(MJ/kg)","综合价<br>(元/吨)","煤价<br>(元/吨)","增值税<br>(元/吨)","矿运费<br>(元/吨)","交货<br>前运杂费<br>(元/吨)",
    					 					"铁路运费<br>(元/吨)","铁路<br>运费税额<br>(元/吨)","到站<br>铁路杂费<br>(元/吨)","汽车运费<br>(元/吨)","汽运税额<br>(元/吨)","汽运杂费<br>(元/吨)",
    					 					"海(水)<br>运费<br>(元/吨)","海(水)<br>运税额<br>(元/吨)","港杂费<br>(元/吨)","其它费用<br>(元/吨)","结算标煤单价<br>含税<br>(元/吨)","结算标煤单价<br>不含税<br>(元/吨)"}};
    		ArrWidth = new int[] {140,40,65,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50};
    		RPTInit.getInsertSql(visit.getDiancxxb_id(), sbsql.toString(), rt, "煤炭结算统计("+getBaoblxValue().getValue()+")", "MEITJSTJ");
    	}

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString6());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("煤炭结算统计("+getBaoblxValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(9, 3,  endRiq, Table.ALIGN_CENTER);
		rt.setDefaultTitle(20, 2, "单位:吨 元/吨",Table.ALIGN_RIGHT);
		
		rt.setBody(new Table(rs,1,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(1, 50);
		rt.body.setPageRows(22);
		rt.body.ShowZero = false;
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		if(rt.body.getRows()>1){
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getCellValue(2, 1).equals("总计")){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
		}
//		页脚 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,4,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(9,2,"审核:",Table.ALIGN_CENTER);

		 if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
		 			
			 rt.setDefautlFooter(20,2, "制表:",Table.ALIGN_RIGHT);
		 		}else{
		 			
		 			rt.setDefautlFooter(20,2, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
		 		}
//		 rt.setDefautlFooter(20,2,"制表:",Table.ALIGN_RIGHT);
		 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	public String getJiestjzb(){//结算统计总表
		Visit visit = (Visit) getPage().getVisit();
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();
		//新增日期形式
//				String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
//				String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
				String beginRiq=this.getBeginriqDate();
				String endRiq=this.getEndriqDate();
				if(beginRiq.equals(endRiq)){
					beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
				}
		String str = "";
		String having = "";
//		int treejib = this.getDiancTreeJib();
//		
//		if(isJitUserShow()){//集团用户
//			if (treejib == 1) {// 选集团
//				str = "";
//				having = " ";
//				
//			} else if (treejib == 2) {// 选分公司
				having = " having not (grouping(gs.mingc)=1 and grouping(dc.mingc)=1) ";
//				str = " and (dc.id in ("+getTreeid()+" or dc.fuid = "+ getTreeid() +" ) or dc.shangjgsid="+getTreeid()+ ") ";
//			} else if (treejib == 3) {// 选电厂
//				having = " having not grouping(dc.mingc)=1 ";
				str = " and dc.id ( " + getTreeid() + " )";
//			}
//		}else if(isGongsUser()){//分公司用户
//			having = " having not (grouping(gs.mingc)=1 and grouping(dc.mingc)=1) ";
//			str = " and (dc.id in ("+getTreeid()+" )or dc.fuid = "+ ((Visit)getPage().getVisit()).getDiancxxb_id()  +"  or dc.shangjgsid="+((Visit)getPage().getVisit()).getDiancxxb_id() +") ";
//			if (treejib == 3) {// 选电厂
//				having = " having not grouping(dc.mingc)=1 ";
//				str = " and dc.id in  ( " + getTreeid() + " )";
//			}
//		}else if(isDiancUser()){//电厂用户
//			having = " having not grouping(dc.mingc)=1 ";
//			str = " and (dc.id in =" + getTreeid() + " or dc.fuid = "+ getTreeid() +")";
//		}else{
//			having = "";
//			str = "";
//		}
//		
		String strleibztbId = ""; 
		if(getZhuangtValue().getId()==1){
			strleibztbId = " and js.liucztb_id=1 ";
		}else if(getZhuangtValue().getId()==2){
			strleibztbId = " and js.liucztb_id=0 ";
		}else{
			strleibztbId = " ";
		}
		 
		StringBuffer sbsql = new StringBuffer();
		if(getBaoblxValue().getValue().equals("分厂分矿")){
			 
			sbsql.setLength(0);
			sbsql.append("select case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||gy.mingc else \n");
			sbsql.append("       case when grouping(kj1.mingc)=0 then '&nbsp;&nbsp;'||kj1.mingc else \n");
			sbsql.append("       case when grouping(dc.mingc)=0 then dc.mingc else \n");
			sbsql.append("       case when grouping(gs.mingc)=0 then gs.mingc else '总计' end end  end end 供煤单位,  \n");
			sbsql.append("       sj.fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.yunf)/sum(sj.jiessl),2)) as yunf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zaf)/sum(sj.jiessl),2)) as zaf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansbmdj)/sum(sj.jiessl),2)) as hansbmdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.buhsbmdj)/sum(sj.jiessl),2)) as buhsbmdj \n");
			sbsql.append("  from  \n");
			sbsql.append("  (select fx.*,nvl(sj.jiessl,0) as jiessl,nvl(farl,0) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("          nvl(sj.jiesrl, 0) as jiesrl, \n");
			sbsql.append("          nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,nvl(sj.yunf,0) as yunf,nvl(sj.zaf,0) as zaf, \n");
			sbsql.append("          nvl(sj.hansbmdj,0) as hansbmdj,nvl(sj.buhsbmdj,0) as buhsbmdj from  \n");
			sbsql.append("  (select diancxxb_id,jihkjb_id,gongysb_id,fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.yunf)/sum(sj.jiessl),2)) as yunf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zaf)/sum(sj.jiessl),2)) as zaf, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0, \n");
			sbsql.append("         round_new(round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as hansbmdj, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),2),0,0, \n");
			sbsql.append("         round_new((round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)-round_new(sum(sj.jiessl*sj.meis)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.yunfs)/sum(sj.jiessl),2))*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as buhsbmdj     \n");
			sbsql.append("   from  \n");
			sbsql.append("    ( select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'本月','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2) \n");
			sbsql.append("                   +round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)+round_new(nvl(yf.guotzf,0)/yf.jiessl,2)+0) as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0)+nvl(yf.dityf,0))/yf.jiessl,2)) as yunf, \n");
/*
*huochaoyuan
*2009-10-22以下语句少个"）"导致显示有问题" 2)) as zaf "，故修改该bug
*/			
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotzf,0)+nvl(yf.ditzf,0))/yf.jiessl,2)) as zaf, \n");
//			end
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new(nvl(js.shuik,0)/js.jiessl,2)) as meis, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)) as yunfs      \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("              (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("                where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                  and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("               ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("        -------本月数据  \n");
			sbsql.append("    union \n");
			sbsql.append("      select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'累计','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2) \n");
			sbsql.append("                   +round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)+round_new(nvl(yf.guotzf,0)/yf.jiessl,2)+0) as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0)+nvl(yf.dityf,0))/yf.jiessl,2)) as yunf, \n");
/*
*huochaoyuan
*2009-10-22以下语句少个"）"导致显示有问题" 2)) as zaf "，故修改该bug
*/				
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotzf,0)+nvl(yf.ditzf,0))/yf.jiessl,2)) as zaf, \n");
//			end	
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new(nvl(js.shuik,0)/js.jiessl,2)) as meis, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)) as yunfs \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("            (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("              where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("             ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("     -------累计数据  \n");
			sbsql.append("      ) sj group by (diancxxb_id,jihkjb_id,gongysb_id,fenx)) sj,   \n");
			sbsql.append("     ( select * from \n");
			sbsql.append("           (select distinct js.diancxxb_id,ht.jihkjb_id,js.gongysb_id \n");
			sbsql.append("              from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc \n");
			sbsql.append("             where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.yunsfsb_id=ys.id \n");
			sbsql.append("               "+strleibztbId+" and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("               and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("            ) aa, \n");
			sbsql.append("           (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx  \n");
			sbsql.append("      ) fx  \n");
			sbsql.append("      where sj.diancxxb_id(+)=fx.diancxxb_id and sj.jihkjb_id(+)=fx.jihkjb_id and sj.gongysb_id(+)=fx.gongysb_id and sj.fenx(+)=fx.fenx ) sj, \n");
			sbsql.append("      diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj1 \n");
			sbsql.append("where sj.diancxxb_id=dc.id and sj.gongysb_id=gy.id and sj.jihkjb_id=kj1.id and dc.fuid=gs.id  \n");
			sbsql.append("group by grouping sets (sj.fenx,(kj1.mingc,sj.fenx),(gs.mingc,sj.fenx),(gs.mingc,kj1.mingc,sj.fenx),(dc.mingc,sj.fenx), \n");
			sbsql.append("    (dc.mingc,kj1.mingc,sj.fenx),(dc.mingc,kj1.mingc,gy.mingc,sj.fenx)) \n");
			sbsql.append("  "+having+"  \n");
			sbsql.append("order by \n");
			sbsql.append("    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj1.mingc),1,4,3),0) desc, \n");
			sbsql.append("    gs.mingc, gs.mingc,dc.mingc,grouping(kj1.mingc) desc,min(kj1.xuh),grouping(gy.mingc) desc,gy.mingc,sj.fenx \n");
			
		}else{
			sbsql.setLength(0);

			sbsql.append("select case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc else \n");
			sbsql.append("       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;'||gy.mingc else \n");
			sbsql.append("       case when grouping(kj1.mingc)=0 then kj1.mingc else '总计' end end end 供煤单位,  \n");
			sbsql.append("       sj.fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.yunf)/sum(sj.jiessl),2)) as yunf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zaf)/sum(sj.jiessl),2)) as zaf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansbmdj)/sum(sj.jiessl),2)) as hansbmdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.buhsbmdj)/sum(sj.jiessl),2)) as buhsbmdj \n");
			sbsql.append("  from  \n");
			sbsql.append("  (select fx.*,nvl(sj.jiessl,0) as jiessl,nvl(farl,0) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("          nvl(sj.jiesrl, 0) as jiesrl, \n");
			sbsql.append("          nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,nvl(sj.yunf,0) as yunf,nvl(sj.zaf,0) as zaf, \n");
			sbsql.append("          nvl(sj.hansbmdj,0) as hansbmdj,nvl(sj.buhsbmdj,0) as buhsbmdj from  \n");
			sbsql.append("  (select diancxxb_id,jihkjb_id,gongysb_id,fenx,sum(sj.jiessl) as jiessl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*kcalkg_to_mjkg(sj.jiesrl, 2))/sum(sj.jiessl),2)) as jiesrl, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.yunf)/sum(sj.jiessl),2)) as yunf, \n");
			sbsql.append("       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zaf)/sum(sj.jiessl),2)) as zaf, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),2),0,0, \n");
			sbsql.append("         round_new(round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as hansbmdj, \n");
			sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),2),0,0, \n");
			sbsql.append("         round_new((round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)-round_new(sum(sj.jiessl*sj.meis)/sum(sj.jiessl),2) \n");
			sbsql.append("         -round_new(sum(sj.jiessl*sj.yunfs)/sum(sj.jiessl),2))*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as buhsbmdj \n");
			sbsql.append("   from  \n");
			sbsql.append("    ( select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'本月','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2) \n");
			sbsql.append("                   +round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)+round_new(nvl(yf.guotzf,0)/yf.jiessl,2)+0) as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0)+nvl(yf.dityf,0))/yf.jiessl,2)) as yunf, \n");
/*
*huochaoyuan
*2009-10-22以下语句少个"）"导致显示有问题" 2)) as zaf "，故修改该bug
*/				
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotzf,0)+nvl(yf.ditzf,0))/yf.jiessl,2)) as zaf, \n");
//			end	
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new(nvl(js.shuik,0)/js.jiessl,2)) as meis, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)) as yunfs \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("              (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("                where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                  and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("               ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("        -------本月数据  \n");
			sbsql.append("    union \n");
			sbsql.append("      select js.id,js.diancxxb_id,ht.jihkjb_id,js.gongysb_id,decode(1,1,'累计','') as fenx, \n");
//			 报表中增加结算热值列，并可设置报表中可显示项
			sbsql.append("             nvl(kcalkg_to_mjkg(js.jiesrl, 2), 0) as jiesrl, \n");
			sbsql.append("             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(yf.ditzf,0)/yf.jiessl,2) \n");
			sbsql.append("                   +round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)+round_new(nvl(yf.guotzf,0)/yf.jiessl,2)+0) as zonghj, \n");
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0)+nvl(yf.dityf,0))/yf.jiessl,2)) as yunf, \n");
/*
*huochaoyuan
*2009-10-22以下语句少个"）"导致显示有问题" 2)) as zaf "，故修改该bug
*/				
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotzf,0)+nvl(yf.ditzf,0))/yf.jiessl,2)) as zaf, \n");
//			end	
			sbsql.append("             decode(nvl(js.jiessl,0),0,0,round_new(nvl(js.shuik,0)/js.jiessl,2)) as meis, \n");
			sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)) as yunfs \n");
			sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
			sbsql.append("            (select js.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl from jiesb js,jieszbsjb zl,zhibb zb \n");
			sbsql.append("              where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
			sbsql.append("                and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("             ) rl \n");
			sbsql.append("       where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id "+strleibztbId+" \n");
			sbsql.append("         and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("         and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("     -------累计数据  \n");
			sbsql.append("      ) sj group by (diancxxb_id,jihkjb_id,gongysb_id,fenx)) sj,   \n");
			sbsql.append("     ( select * from  \n");
			sbsql.append("           (select distinct js.diancxxb_id,ht.jihkjb_id,js.gongysb_id \n");
			sbsql.append("              from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,diancxxb dc \n");
			sbsql.append("             where js.id=yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) and js.yunsfsb_id=ys.id  \n");
			sbsql.append("               "+strleibztbId+" and js.diancxxb_id=dc.id "+str+" \n");
			sbsql.append("               and js.jiesrq>=to_date('"+beginRiq+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+endRiq+"','yyyy-mm-dd'),1)  \n");
			sbsql.append("            ) aa, \n");
			sbsql.append("           (select 1 as xuh,decode(1,1,'本月','') as fenx from dual union select 2 as xuh,decode(1,1,'累计','') as fenx from dual) fx  \n");
			sbsql.append("      ) fx  \n");
			sbsql.append("      where sj.diancxxb_id(+)=fx.diancxxb_id and sj.jihkjb_id(+)=fx.jihkjb_id and sj.gongysb_id(+)=fx.gongysb_id and sj.fenx(+)=fx.fenx ) sj, \n");
			sbsql.append("      diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj1 \n");
			sbsql.append("where sj.diancxxb_id=dc.id and sj.gongysb_id=gy.id and sj.jihkjb_id=kj1.id and dc.fuid=gs.id  \n");
			sbsql.append("group by grouping sets (sj.fenx,(kj1.mingc,sj.fenx),(gy.mingc,sj.fenx),(gy.mingc,dc.mingc,sj.fenx)) \n");
			sbsql.append("order by \n");
			sbsql.append("    decode(grouping(dc.mingc)+grouping(gy.mingc)+grouping(kj1.mingc),3,3,0) desc,min(kj1.xuh), \n");
			sbsql.append("    grouping(gy.mingc) desc,gy.mingc,grouping(dc.mingc) desc,dc.mingc,grouping(kj1.mingc) desc,sj.fenx \n");
			 
		}
			
		ResultSetList rs = cn.getResultSetList(sbsql.toString());
		if (rs == null) {
			return null;
		}
		
		ResultSetList rsl = null;
		String[][] ArrHeader = new String[2][11];;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String [] Zidm = null;
		StringBuffer SB = new StringBuffer();
		SB.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='MEITJSTJZB' " 
					+ "order by xuh");
        ResultSetList r = cn.getResultSetList(SB.toString());
		
        if (r.getRows() > 0) {
        	ArrWidth = new int[r.getRows()];
        	strFormat = new String[r.getRows()];
        	String biaot = r.getString(0,1);
        	String [] Arrbt = biaot.split("!@");
        	ArrHeader = new String [Arrbt.length][r.getRows()];
        	Zidm = new String[r.getRows()];
        	rsl = new ResultSetList();
        	while (r.next()) {
        		Zidm[r.getRow()]=r.getString("zidm");
        		ArrWidth[r.getRow()]=r.getInt("kuand");
        		strFormat[r.getRow()]=r.getString("format") == null?"":r.getString("format");
        		String[] title=r.getString("biaot").split("!@");
        		for(int i = 0; i < title.length; i++){
        			ArrHeader[i][r.getRow()]=title[i];
        		}
        	}
        	rsl.setColumnNames(Zidm);
        	while (rs.next()) {
        		rsl.getResultSetlist().add(rs.getArrString(Zidm));
        	}
        } else {
        	rsl = rs;
        	ArrHeader[0] = new String[] {"单位", "本月<br>累计", "结算数量<br>(吨)", "入厂热值<br>(MJ/kg)", "结算热值<br>(MJ/kg)", "综合价<br>(元/吨)", "煤价<br>(元/吨)", 
        			                     "运费<br>(元/吨)", "杂费<br>(元/吨)", "标煤单价(元/吨)", "标煤单价(元/吨)"};
        	ArrHeader[1] = new String[] {"单位", "本月<br>累计", "结算数量<br>(吨)", "入厂热值<br>(MJ/kg)", "结算热值<br>(MJ/kg)", "综合价<br>(元/吨)", "煤价<br>(元/吨)", 
        			                     "运费<br>(元/吨)", "杂费<br>(元/吨)", "含税", "不含税"};
    		ArrWidth = new int[] {150, 40, 75, 65, 65, 65, 65, 65, 65, 65, 65};
    		RPTInit.getInsertSql(visit.getDiancxxb_id(), sbsql.toString(), rt, "煤炭结算统计总表("+getBaoblxValue().getValue()+")", "MEITJSTJZB");
    	}

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("煤炭结算统计总表("+getBaoblxValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, endRiq, Table.ALIGN_CENTER);
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
		rt.body.setPageRows(22);
		rt.body.ShowZero = false;
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		if(rt.body.getRows()>2){
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getCellValue(3, 1).equals("总计")){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
		}
//		页脚 
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,4,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
//		 rt.setDefautlFooter(15,3,"单位:吨  元/吨",Table.ALIGN_RIGHT);
		 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

//	初始化多选电厂树中的默认值
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			visit.setProSelectionModel11(null);
			visit.setString6(null);
			visit.setString5(null);
			this.setTreeid(null);
			
			
			//begin方法里进行初始化设置
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);保存传递的非默认纸张的样式
				 initDiancTree();
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean2(null);
					visit.setProSelectionModel2(null);
					visit.setDropDownBean3(null);
					visit.setProSelectionModel3(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					visit.setDropDownBean10(null);
					visit.setProSelectionModel10(null);
					this.setTreeid(null);
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
//        	mstrReportName=REPORT_JIESTJQUERY;
        }
		getToolBars();
		isBegin=true;
	}

//	 分公司下拉框
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
		setDiancxxModel(new IDropDownModel(sql,"中电投"));
	}
	
	

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("组合电厂");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));
		
		
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();

		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BAOBLX");
		cb.setWidth(60);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("审核状态:"));
		ComboBox zt = new ComboBox();
		zt.setTransform("ZHUANGT");
		zt.setWidth(60);
		zt.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(zt);
		
		tb1.addText(new ToolbarText("-"));
		

		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
//	 统计口径
	public boolean _Baoblxchange = false;

	public IDropDownBean getBaoblxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getIBaoblxModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean10() != null) {
			id = ((Visit)getPage().getVisit()).getDropDownBean10().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}


	public void setIBaoblxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getIBaoblxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getIBaoblxModels() {

		List List = new ArrayList();
		List.add(new IDropDownBean(-1,"分厂"));
		List.add(new IDropDownBean(0,"分厂分矿"));
		List.add(new IDropDownBean(1,"分矿"));
		
		((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel(List));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	
//	结算单状态
	public boolean _Zhuangtchange = false;

	public IDropDownBean getZhuangtValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
			((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getIZhuangtModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean11();
	}

	public void setZhuangtValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean11() != null) {
			id = ((Visit)getPage().getVisit()).getDropDownBean11().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Zhuangtchange = true;
			} else {
				_Zhuangtchange = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean11(Value);
	}

	public void setIZhuangtModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getIZhuangtModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel11() == null) {
			getIZhuangtModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel11();
	}

	public IPropertySelectionModel getIZhuangtModels() {

		List List = new ArrayList();
		List.add(new IDropDownBean(0,"全部"));
		List.add(new IDropDownBean(1,"已审核"));
		List.add(new IDropDownBean(2,"未审核"));
		
		((Visit)getPage().getVisit()).setProSelectionModel11(new IDropDownModel(List));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel11();
	}
//	电厂名称-----------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {

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
	
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	/////// 年月份有关----
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
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
		
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

	private String treeid;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
		return getTree().getWindowTreeScript()+getOtherScript("diancTree");
	}

//	增加电厂多选树的级联
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
	
	
}