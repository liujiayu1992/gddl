/* 增加计算到货率除数等于零时到货率等于null的判断
 * 09-04-22  
 * sy*/

package com.zhiren.jt.zdt.yansgl.meitdhqk;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.File;

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

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.tools.FtpUpload;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.tools.FtpCreatTxt;


import org.apache.tapestry.contrib.palette.SortMode;

/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Meitdhqk  extends BasePage implements PageValidateListener{
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
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.quanc as mingc from diancxxb dc where dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}
	
	
	//开始日期v
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
		}
		((Visit) getPage().getVisit()).setDate2(_BeginriqValue);
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
	
	private boolean _ShangcChick = false;

	public void ShangcButton(IRequestCycle cycle) {
		_ShangcChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if(_ShangcChick){
			_ShangcChick=false;
		}
	}
	

	private void Refurbish() {
        //为"刷新" 按钮添加处理程序
		isBegin=true;
		if(getBaoblxValue()!=null){
			
				getSelectData();
			
		}
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());

			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			isBegin=true;
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			this.setTreeid(null);
//			this.getShouhcrb();
//			this.getSelectData();
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
		}
		if (_BeginriqChange){
			_BeginriqChange=false;
			Refurbish();
		}
		if(_Baoblxchange){
			_Baoblxchange=false;
			Refurbish();
		}
		if(_fengschange){
			_fengschange=false;
			Refurbish();
		}
		setUserName(visit.getRenymc());
		getToolBars();
		Refurbish();
	}
	private String RT_HET="meitdhqk";
	private String mstrReportName="meitdhqk";
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			if(getBaoblxValue()!=null){
				
					return getSelectData();
				
			}
			return "";
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

	/**
	 * 发电集团煤炭到货情况
	 * @author xzy
	 */
	private String getSelectData() {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		Date dat=getBeginriqDate();//日期
		String riq=OraDate(dat);//日期字符
		String strdiancid="";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// 选集团时刷新出所有的电厂
			strdiancid = " ";
		} else if (jib == 2) {// 选分公司或者燃料公司时
			
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			ResultSetList rl = cn.getResultSetList(ranlgs);
			if(rl.getRows()!=0){//燃料公司
				strdiancid=" and dc.shangjgsid="+ this.getTreeid();
			}else{
				strdiancid = "  and dc.fuid=  " + this.getTreeid();
			}
			
		} else if (jib == 3) {// 选电厂只刷新出该电厂
			strdiancid = " and dc.id= " + this.getTreeid();
		} else if (jib == -1) {
			strdiancid = " and dc.id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		ArrHeader = new String[2][7];
		ArrHeader[0] = new String[] { "单位", "月计划", "日均计划", "实际供煤", "实际供煤","与计划累计差",  "到货率(%)" };
		ArrHeader[1] = new String[] { "单位", "月计划", "日均计划", "当日", "累计","与计划累计差",  "到货率(%)" };
		ArrWidth = new int[] {150, 80, 80, 80, 80, 80, 80 };
		String titlename="";
		
		if (getBaoblxValue().getValue().equals("分厂")) {
			titlename = "煤炭到货情况" + "(分厂)";
			strSQL=
				"select decode(grouping(f.mingc)+grouping(dc.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;'||dc.mingc) as danw,\n" +
				"       round_new(sum(jh.yuejhl),0) as yuejh,\n" + 
				"       round_new(sum(jh.rijjh),0) as rijh,\n" + 
				"       round_new(sum(dr.shisl),0) as drssl,\n" + 
				"       round_new(sum(lj.shisl),0) as ljssl,\n" + 
				"       round_new(sum(lj.shisl),0)-\n" + 
				"       round_new(nvl(sum(jh.rijjh),0),0)*(select to_char(to_date('"+riq+"','yyyy-mm-dd'),'dd') from dual) as yujhljc,\n" + 
				"       decode(sum(jh.rijjh),0,null,null,null,\n" + 
				"       Round_new(sum(lj.shisl)*100/(sum(jh.rijjh)*(select to_char(to_date('"+riq+"','yyyy-mm-dd'),'dd') from dual)),2)) as dhl\n" + 
				"from\n" + 
				"  (select fh.diancxxb_id,max(fh.daohrq) as riq ,sum(fh.biaoz+fh.yingd-(fh.yingd-fh.yingk)) as shisl from fahb fh\n" + 
				"    where fh.daohrq=to_date('"+riq+"','yyyy-mm-dd')\n" + 
				"   group by(fh.diancxxb_id)) dr,\n" + 
				"    (select fh.diancxxb_id, max(fh.daohrq) as riq,sum(fh.biaoz+fh.yingd-(fh.yingd-fh.yingk)) as shisl\n" + 
				"    from fahb fh where fh.daohrq>=First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"    and fh.daohrq<=to_date('"+riq+"','yyyy-mm-dd')\n" + 
				"    group by(fh.diancxxb_id)) lj,\n" + 
				"    (select y.diancxxb_id,sum(y.yuejhcgl) as yuejhl,\n" + 
				"     sum(y.yuejhcgl)/(select daycount(to_date('"+riq+"','yyyy-mm-dd')) from dual) as rijjh\n" + 
				"    from yuecgjhb y where y.riq=First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"    group by (y.diancxxb_id)) jh,diancxxb dc,vwfengs f\n" + 
				"where lj.diancxxb_id=dr.diancxxb_id(+)\n" + 
				"and lj.diancxxb_id=jh.diancxxb_id(+)\n" + 
				"and lj.diancxxb_id(+)=dc.id\n" + 
				"and dc.fuid=f.id\n" + 
				"and dc.jib=3\n" + 
				""+strdiancid+"\n"+
				"group by rollup (f.mingc,dc.mingc)\n" + 
				"order by  grouping(f.mingc) desc ,f.mingc,grouping (dc.mingc) desc ,max(dc.xuh)";

			
		} else if (getBaoblxValue().getValue().equals("分矿")) {
			titlename = "煤炭到货情况" + "(分矿)";
			strSQL=
				"select decode(grouping(smc) + grouping(dqmc),\n" +
				"              2,\n" + 
				"              '总计',\n" + 
				"              1,\n" + 
				"              smc,\n" + 
				"              '&nbsp;&nbsp;&nbsp;&nbsp;' || dqmc) as danw,\n" + 
				"       round_new(sum(jh.yuejhl), 0) as yuejh,\n" + 
				"       round_new(sum(jh.rijjh), 0) as rijh,\n" + 
				"       round_new(sum(dr.shisl), 0) as drssl,\n" + 
				"       round_new(sum(lj.shisl), 0) as ljssl,\n" + 
				"       round_new(sum(lj.shisl), 0) -\n" + 
				"       round_new(nvl(sum(jh.rijjh), 0), 0) *\n" + 
				"       (select to_char(to_date('"+riq+"', 'yyyy-mm-dd'), 'dd') from dual) as yujhljc,\n" + 
				"       decode(sum(jh.rijjh),\n" + 
				"              0,\n" + 
				"              null,\n" + 
				"              null,\n" + 
				"              null,\n" + 
				"              Round_new(sum(lj.shisl) * 100 /\n" + 
				"                        (sum(jh.rijjh) *\n" + 
				"                         (select to_char(to_date('"+riq+"', 'yyyy-mm-dd'),\n" + 
				"                                         'dd')\n" + 
				"                            from dual)),\n" + 
				"                        2)) as dhl\n" + 
				"  from (select fh.diancxxb_id,\n" + 
				"               fh.gongysb_id,\n" + 
				"               max(fh.daohrq) as riq,\n" + 
				"               sum(fh.biaoz + fh.yingd - (fh.yingd - fh.yingk)) as shisl\n" + 
				"          from fahb fh\n" + 
				"         where fh.daohrq = to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
				"         group by (fh.diancxxb_id, fh.gongysb_id)) dr,\n" + 
				"       (select fh.diancxxb_id,\n" + 
				"               fh.gongysb_id,\n" + 
				"               max(fh.daohrq) as riq,\n" + 
				"               sum(fh.biaoz + fh.yingd - (fh.yingd - fh.yingk)) as shisl\n" + 
				"          from fahb fh\n" + 
				"         where fh.daohrq >= First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"           and fh.daohrq <= to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
				"         group by (fh.diancxxb_id, fh.gongysb_id)) lj,\n" + 
				"       (select y.diancxxb_id,\n" + 
				"               y.gongysb_id,\n" + 
				"               sum(y.yuejhcgl) as yuejhl,\n" + 
				"               sum(y.yuejhcgl) /\n" + 
				"               (select daycount(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"                  from dual) as rijjh\n" + 
				"          from yuecgjhb y\n" + 
				"         where y.riq = First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"         group by (y.diancxxb_id, y.gongysb_id)) jh,\n" + 
				"       vwdianc dc,\n" + 
				"       vwgongys g\n" + 
				" where lj.diancxxb_id = dr.diancxxb_id(+)\n" + 
				"   and lj.diancxxb_id = jh.diancxxb_id(+)\n" + 
				"   and lj.diancxxb_id(+) = dc.id\n" + 
				"   and lj.gongysb_id = dr.gongysb_id(+)\n" + 
				"   and lj.gongysb_id = jh.gongysb_id(+)\n" + 
				"   and lj.gongysb_id = g.id\n" + 
				"   and (dc.fgsid = 102 or dc.rlgsid = 102)\n" + 
				" group by rollup(smc, dqmc)\n" + 
				" order by grouping(smc) desc,\n" + 
				"          max(sxh),\n" + 
				"          smc,\n" + 
				"          grouping(dqmc) desc,\n" + 
				"          max(dqxh),\n" + 
				"          dqmc";
			
		}else if (getBaoblxValue().getValue().equals("分厂分矿")) {
			titlename = "煤炭到货情况" + "(分厂分矿)";
			strSQL=
				"select decode(grouping(fgsmc) + grouping(dc.mingc) + grouping(dqmc),\n" +
				"              3,\n" + 
				"              '总计',\n" + 
				"              2,\n" + 
				"              fgsmc,\n" + 
				"              1,\n" + 
				"              '&nbsp;&nbsp;&nbsp;&nbsp;' || dc.mingc,\n" + 
				"              '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || dqmc) as danw,\n" + 
				"       round_new(sum(jh.yuejhl), 0) as yuejh,\n" + 
				"       round_new(sum(jh.rijjh), 0) as rijh,\n" + 
				"       round_new(sum(dr.shisl), 0) as drssl,\n" + 
				"       round_new(sum(lj.shisl), 0) as ljssl,\n" + 
				"       round_new(sum(lj.shisl), 0) -\n" + 
				"       round_new(nvl(sum(jh.rijjh), 0), 0) *\n" + 
				"       (select to_char(to_date('"+riq+"', 'yyyy-mm-dd'), 'dd') from dual) as yujhljc,\n" + 
				"       decode(sum(jh.rijjh),\n" + 
				"              0,\n" + 
				"              null,\n" + 
				"              null,\n" + 
				"              null,\n" + 
				"              Round_new(sum(lj.shisl) * 100 /\n" + 
				"                        (sum(jh.rijjh) *\n" + 
				"                         (select to_char(to_date('"+riq+"', 'yyyy-mm-dd'),\n" + 
				"                                         'dd')\n" + 
				"                            from dual)),\n" + 
				"                        2)) as dhl\n" + 
				"  from (select fh.diancxxb_id,\n" + 
				"               fh.gongysb_id,\n" + 
				"               max(fh.daohrq) as riq,\n" + 
				"               sum(fh.biaoz + fh.yingd - (fh.yingd - fh.yingk)) as shisl\n" + 
				"          from fahb fh\n" + 
				"         where fh.daohrq = to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
				"         group by (fh.diancxxb_id, fh.gongysb_id)) dr,\n" + 
				"       (select fh.diancxxb_id,\n" + 
				"               fh.gongysb_id,\n" + 
				"               max(fh.daohrq) as riq,\n" + 
				"               sum(fh.biaoz + fh.yingd - (fh.yingd - fh.yingk)) as shisl\n" + 
				"          from fahb fh\n" + 
				"         where fh.daohrq >= First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"           and fh.daohrq <= to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
				"         group by (fh.diancxxb_id, fh.gongysb_id)) lj,\n" + 
				"       (select y.diancxxb_id,\n" + 
				"               y.gongysb_id,\n" + 
				"               sum(y.yuejhcgl) as yuejhl,\n" + 
				"               sum(y.yuejhcgl) /\n" + 
				"               (select daycount(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"                  from dual) as rijjh\n" + 
				"          from yuecgjhb y\n" + 
				"         where y.riq = First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"         group by (y.diancxxb_id, y.gongysb_id)) jh,\n" + 
				"       vwdianc dc,\n" + 
				"       vwgongys g\n" + 
				" where lj.diancxxb_id = dr.diancxxb_id(+)\n" + 
				"   and lj.diancxxb_id = jh.diancxxb_id(+)\n" + 
				"   and lj.diancxxb_id(+) = dc.id\n" + 
				"   and lj.gongysb_id = dr.gongysb_id(+)\n" + 
				"   and lj.gongysb_id = jh.gongysb_id(+)\n" + 
				"   and lj.gongysb_id = g.id\n" + 
				"   and (dc.fgsid = 102 or dc.rlgsid = 102)\n" + 
				" group by rollup(fgsmc, dc.mingc, dqmc)\n" + 
				" order by grouping(fgsmc) desc,\n" + 
				"          max(fgsxh),\n" + 
				"          fgsmc,\n" + 
				"          grouping(dc.mingc) desc,\n" + 
				"          max(dc.xuh),\n" + 
				"          dc.mingc,\n" + 
				"          grouping(dqmc) desc,\n" + 
				"          max(dqxh),\n" + 
				"          dqmc";

		}else if (getBaoblxValue().getValue().equals("分矿分厂")) {
			titlename = "煤炭到货情况" + "(分矿分厂)";
			strSQL=
				"select decode(grouping(fgsmc) + grouping(dc.mingc) + grouping(dqmc),\n" +
				"              3,\n" + 
				"              '总计',\n" + 
				"              2,\n" + 
				"              dqmc,\n" + 
				"              1,\n" + 
				"              '&nbsp;&nbsp;&nbsp;&nbsp;' || fgsmc,\n" + 
				"              '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' || dc.mingc) as danw,\n" + 
				"       round_new(sum(jh.yuejhl), 0) as yuejh,\n" + 
				"       round_new(sum(jh.rijjh), 0) as rijh,\n" + 
				"       round_new(sum(dr.shisl), 0) as drssl,\n" + 
				"       round_new(sum(lj.shisl), 0) as ljssl,\n" + 
				"       round_new(sum(lj.shisl), 0) -\n" + 
				"       round_new(nvl(sum(jh.rijjh), 0), 0) *\n" + 
				"       (select to_char(to_date('"+riq+"', 'yyyy-mm-dd'), 'dd') from dual) as yujhljc,\n" + 
				"       decode(sum(jh.rijjh),\n" + 
				"              0,\n" + 
				"              null,\n" + 
				"              null,\n" + 
				"              null,\n" + 
				"              Round_new(sum(lj.shisl) * 100 /\n" + 
				"                        (sum(jh.rijjh) *\n" + 
				"                         (select to_char(to_date('"+riq+"', 'yyyy-mm-dd'),\n" + 
				"                                         'dd')\n" + 
				"                            from dual)),\n" + 
				"                        2)) as dhl\n" + 
				"  from (select fh.diancxxb_id,\n" + 
				"               fh.gongysb_id,\n" + 
				"               max(fh.daohrq) as riq,\n" + 
				"               sum(fh.biaoz + fh.yingd - (fh.yingd - fh.yingk)) as shisl\n" + 
				"          from fahb fh\n" + 
				"         where fh.daohrq = to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
				"         group by (fh.diancxxb_id, fh.gongysb_id)) dr,\n" + 
				"       (select fh.diancxxb_id,\n" + 
				"               fh.gongysb_id,\n" + 
				"               max(fh.daohrq) as riq,\n" + 
				"               sum(fh.biaoz + fh.yingd - (fh.yingd - fh.yingk)) as shisl\n" + 
				"          from fahb fh\n" + 
				"         where fh.daohrq >= First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"           and fh.daohrq <= to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
				"         group by (fh.diancxxb_id, fh.gongysb_id)) lj,\n" + 
				"       (select y.diancxxb_id,\n" + 
				"               y.gongysb_id,\n" + 
				"               sum(y.yuejhcgl) as yuejhl,\n" + 
				"               sum(y.yuejhcgl) /\n" + 
				"               (select daycount(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"                  from dual) as rijjh\n" + 
				"          from yuecgjhb y\n" + 
				"         where y.riq = First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n" + 
				"         group by (y.diancxxb_id, y.gongysb_id)) jh,\n" + 
				"       vwdianc dc,\n" + 
				"       vwgongys g\n" + 
				" where lj.diancxxb_id = dr.diancxxb_id(+)\n" + 
				"   and lj.diancxxb_id = jh.diancxxb_id(+)\n" + 
				"   and lj.diancxxb_id(+) = dc.id\n" + 
				"   and lj.gongysb_id = dr.gongysb_id(+)\n" + 
				"   and lj.gongysb_id = jh.gongysb_id(+)\n" + 
				"   and lj.gongysb_id = g.id\n" + 
				"   and (dc.fgsid = 102 or dc.rlgsid = 102)\n" + 
				" group by rollup(dqmc, fgsmc, dc.mingc)\n" + 
				" order by grouping(dqmc) desc,\n" + 
				"          max(dqxh),\n" + 
				"          dqmc,\n" + 
				"          grouping(fgsmc) desc,\n" + 
				"          max(fgsxh),\n" + 
				"          fgsmc,\n" + 
				"          grouping(dc.mingc) desc,\n" + 
				"          max(dc.xuh),\n" + 
				"          dc.mingc";
		}
		
		ResultSet rs = cn.getResultSet(strSQL);

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		// 数据
		rt.setBody(new Table(rs, 2, 0, 1));
		if (cn.getHasIt(strSQL)) {// 值
			rt.body.setCellAlign(3, 1, 1);// 总计居中
			
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
		}
		rt.setTitle(titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2, FormatDate(DateUtil.getDate(riq)), Table.ALIGN_CENTER);
		rt.setDefaultTitle(6, 2, "单位:吨", Table.ALIGN_RIGHT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.body.setPageRows(36);
		rt.setDefautlFooter(1, 2, "制表时间:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "审核:",Table.ALIGN_CENTER);
		if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
			
			rt.setDefautlFooter(6, 2, "制表:",Table.ALIGN_RIGHT);
			}else{
				
				rt.setDefautlFooter(6, 2, "制表:"+((Visit) getPage().getVisit()).getDiancmc

	(),Table.ALIGN_RIGHT);
			}
//		rt.setDefautlFooter(6, 2, "制表:",Table.ALIGN_RIGHT);
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
//	******************************************************************************

	

	
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
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}
	
//	矿别名称
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{

		
		String sql="";
		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
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


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
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
		setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
	}
	
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
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"分厂"));
		fahdwList.add(new IDropDownBean(1,"分矿"));
		fahdwList.add(new IDropDownBean(2,"分厂分矿"));
		fahdwList.add(new IDropDownBean(3,"分矿分厂"));
		

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
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
		
		tb1.addText(new ToolbarText("报表类型:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setListeners("select:function(){document.Form0.submit();}");
		cb.setId("Baoblx");
		cb.setWidth(120);
		tb1.addField(cb);
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
		return getTree().getWindowTreeScript();
	}

}