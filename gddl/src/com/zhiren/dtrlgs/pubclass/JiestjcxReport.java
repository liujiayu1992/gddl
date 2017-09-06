package com.zhiren.dtrlgs.pubclass;

import java.sql.ResultSet;
import java.util.ArrayList;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.jt.het.hetcx.Hetcxbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class JiestjcxReport extends BasePage {
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
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

	private String REPORT_NAME_KEHXQYJH = "jiestjcx";//DuowkcbReport&lx=duowkc
	private String mstrReportName; //= "jiestjcx";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_KEHXQYJH)) {
			if(getLeixDropDownValue().getValue().equals("全部")){
				return getJiestjQReport();
			}
			return getJiestjReport();
		} else {
			return "无此报表";
		}
	}
	
	private String getJiestjQReport(){
		JDBCcon con = new JDBCcon();
		String sql=
		"select\n" +
		"       --grouping(sj.dmingc) dc,\n" + 
		"       --grouping(sj.gmingc) gy,\n" + 
		"       --grouping(sj.flag) flag,\n" + 
		"       decode(grouping(sj.dmingc),1,'总计',sj.dmingc) dc,\n" + 
		"       decode(grouping(sj.dmingc)+grouping(sj.gmingc)+grouping(sj.flag),2,'合计',sj.gmingc) gys,\n" + 
		"       sj.flag,\n" + 
		"	    getHtmlAlert('"
				+ MainGlobal.getHomeContext(this)
				+ "','ShoumShowjsd','jiesdbh',sj.bianm,sj.bianm),\n" + 		
		"       to_char(sj.jiesrq,'yyyy-mm-dd') jiesrq,\n" + 
		"       to_char(sj.ruzrq,'yyyy-mm-dd') ruzrq,\n" + 
		"       sum(sj.jiessl) as jiessl,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf,\n" + 
		"       decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy,\n" + 
		"       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0,\n" + 
		"       round_new(round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as hansbmdj,\n" + 
		"       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0,\n" + 
		"       round_new((round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)-round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)\n" + 
		"        -round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)-round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)\n" + 
		"        -round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2))*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as buhsbmdj\n" + 
		"from\n" + 
		"(--完整数据\n" + 
		" select * from (\n" + 
		" --表头\n" + 
		"      select a.*,d.mingc dmingc,g.mingc gmingc from (\n" + 
		"            select id,xuh,flag from\n" + 
		"            (\n" + 
		"                select rownum xuh ,\n" + 
		"                       decode(basicx.flag,'销售',\n" + 
		"                              decode(basicx.id,0,basicx.wid,basicx.id),\n" + 
		"                              decode(basicx.wid,0,basicx.id,basicx.wid)) id,\n" + 
		"                       flag\n" + 
		"                from(\n" + 
		"                    select id,wid,flag,max(xuhx) xuhx from --,max(xuhx) xuhx\n" + 
		"                         (\n" + 
		"                             select id,wid,xuhx from (\n" + 
		"                                    select d.id,d.kuangfjsmkb_id wid,rownum xuhx\n" + 
		"                                    from diancjsmkb d\n" + 
		"                                    where d.diancxxb_id=199\n" + 
		"                                      and d." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + "\n" + 
		"                                      and d." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"                                    union\n" + 
		"                                    select k.diancjsmkb_id id,k.id wid,rownum+0.1 xuhx\n" + 
		"                                    from kuangfjsmkb k\n" + 
		"                                    where k.diancxxb_id=199\n" + 
		"                                      and k." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + "\n" + 
		"                                      and k." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"                                    )\n" + 
		"                         ) basic,\n" + 
		"                         (\n" + 
		"                             select '销售' flag from dual\n" + 
		"                             union\n" + 
		"                             select '采购' flag from dual\n" + 
		"                         ) ex\n" + 
		"                    group by id,wid,flag\n" + 
		"                    order by xuhx,id,flag\n" + 
		"                ) basicx\n" + 
		"            )biaot order by xuh\n" + 
		"        ) a,\n" + 
		"        (\n" + 
		"        select id,diancxxb_id,gongysb_id from diancjsmkb d\n" + 
		"        union\n" + 
		"        select id,diancxxb_id,gongysb_id from kuangfjsmkb k\n" + 
		"        ) b,diancxxb d,gongysb g\n" + 
		"        where a.id=b.id(+) and b.diancxxb_id =d.id(+) and b.gongysb_id=g.id(+)\n" + 
		"--表头（要形成的样子）。与数据连接时用*jsmkb.id和flag关联\n" + 
		" )biaot,\n" + 
		" -----------------------------------------basicdata\n" + 
		" (\n" + 
		"    (--union1——dianc\n" + 
		"     select fx.*,nvl(sj.jiessl,0) as jiessl,\n" + 
		"     nvl(farl,0) as farl,'销售' flagx,\n" + 
		"     nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,\n" + 
		"     nvl(sj.zengzs,0) as zengzs,nvl(sj.kuangyf,0) as kuangyf,\n" + 
		"     nvl(sj.jiaohqyzf,0) as jiaohqyzf, nvl(sj.tielyf,0) as tielyf,\n" + 
		"     nvl(sj.tielyfs,0) as tielyfs,nvl(sj.tielzf,0) as tielzf,\n" + 
		"     nvl(sj.qiyf,0) as qiyf,nvl(sj.qiys,0) as qiys,nvl(sj.qiyzf,0) as qiyzf,\n" + 
		"     nvl(sj.haiyf,0) as haiyf,nvl(haiys,0) as haiys,nvl(sj.gangzf,0) as gangzf,\n" + 
		"     nvl(sj.qitfy,0) as qitfy\n" + 
		"     from(\n" + 
		"         select diancxxb_id,gongysb_id,bianm,---------bianm\n" + 
		"         sum(sj.jiessl) as jiessl,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf,\n" + 
		"         decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy\n" + 
		"         from(\n" + 
		"             select js.id,js.diancxxb_id,js.gongysb_id,js.bianm, -----js.bianm\n" + 
		"             nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,\n" + 
		"             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj,\n" + 
		"             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))\n" + 
		"              +decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(0,0)/yf.jiessl,2))\n" + 
		"              +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" + 
		"              +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" + 
		"              +decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" + 
		"              +decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" + 
		"              +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" + 
		"              +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" + 
		"              +0 as zonghj,\n" + 
		"             decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs,\n" + 
		"             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf,\n" + 
		"             decode(nvl(yf.jiessl,0),0,0,round_new(nvl(0,0)/yf.jiessl,2)) as jiaohqyzf, --ditzf\n" + 
		"             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf,\n" + 
		"             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs,\n" + 
		"             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf,\n" + 
		"             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf,\n" + 
		"             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys,\n" + 
		"             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf,\n" + 
		"             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf,\n" + 
		"             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys,\n" + 
		"             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf,\n" + 
		"             0 as qitfy\n" + 
		"             from\n" + 
		"                diancjsmkb js,diancjsyfb yf,hetb ht,yunsfsb ys,diancxxb dc,\n" + 
		"                (select jss.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl\n" + 
		"                 from(\n" + 
		"                     select id," + getRiqLeixName() + " from diancjsmkb\n" + 
		"                     union\n" + 
		"                     select id," + getRiqLeixName() + " from diancjsyfb\n" + 
		"                     )jss,\n" + 
		"                     jieszbsjb zl,\n" + 
		"                     zhibb zb\n" + 
		"                 where jss.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar'\n" + 
		"                 and jss." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + "\n" + 
		"                 and jss." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"                ) rl\n" + 
		"             where js.id=yf.diancjsmkb_id(+)\n" + 
		"               and js.hetb_id=ht.id(+)\n" + 
		"               and js.id=rl.id\n" + 
		"               and js.yunsfsb_id=ys.id\n" + 
		"               and js.diancxxb_id=dc.id\n" + 
		"               and dc.id = 199\n" + 
		"               and js." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + "\n" + 
		"               and js." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"         )sj\n" + 
		"         group by (diancxxb_id,gongysb_id,bianm)---------bianm---------\n" + 
		"     )sj,\n" + 
		"     (select distinct js.diancxxb_id,js.gongysb_id,js.bianm,js.jiesrq,js.ruzrq,js.id basicid\n" + 
		"      from diancjsmkb js,diancjsyfb yf,hetb ht,yunsfsb ys,diancxxb dc\n" + 
		"      where js.id=yf.diancjsmkb_id(+)\n" + 
		"        and js.hetb_id=ht.id(+) and js.yunsfsb_id=ys.id\n" + 
		"        and js.diancxxb_id=dc.id  and dc.id = 199\n" + 
		"        and js." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + " " +
		"		 and js." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"     )fx\n" + 
		"     where sj.diancxxb_id(+)=fx.diancxxb_id and sj.gongysb_id(+)=fx.gongysb_id and sj.bianm(+)=fx.bianm---------\n" + 
		"    )\n" + 
		"    union------------------union\n" + 
		"    (--union2__kuangf\n" + 
		"     select fx.*,nvl(sj.jiessl,0) as jiessl,nvl(farl,0) as farl, '采购' flagx,\n" + 
		"            nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,\n" + 
		"            nvl(sj.zengzs,0) as zengzs,nvl(sj.kuangyf,0) as kuangyf,\n" + 
		"            nvl(sj.jiaohqyzf,0) as jiaohqyzf,nvl(sj.tielyf,0) as tielyf,\n" + 
		"            nvl(sj.tielyfs,0) as tielyfs,nvl(sj.tielzf,0) as tielzf,\n" + 
		"            nvl(sj.qiyf,0) as qiyf,nvl(sj.qiys,0) as qiys,\n" + 
		"            nvl(sj.qiyzf,0) as qiyzf,nvl(sj.haiyf,0) as haiyf,\n" + 
		"            nvl(haiys,0) as haiys,nvl(sj.gangzf,0) as gangzf,\n" + 
		"            nvl(sj.qitfy,0) as qitfy\n" + 
		"     from(\n" + 
		"          select diancxxb_id,gongysb_id,bianm,---------bianm\n" + 
		"                 sum(sj.jiessl) as jiessl,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf,\n" + 
		"                 decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy\n" + 
		"          from(\n" + 
		"               select js.id,js.diancxxb_id,js.gongysb_id,js.bianm, -----js.bianm\n" + 
		"                      nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,\n" + 
		"                      decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj,\n" + 
		"                      decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))\n" + 
		"                      +decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(0,0)/yf.jiessl,2))\n" + 
		"                      +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" + 
		"                      +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" + 
		"                      +decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" + 
		"                      +decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" + 
		"                      +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" + 
		"                      +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" + 
		"                      +0 as zonghj,\n" + 
		"                      decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs,\n" + 
		"                      decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf,\n" + 
		"                      decode(nvl(yf.jiessl,0),0,0,round_new(nvl(0,0)/yf.jiessl,2)) as jiaohqyzf, --ditzf\n" + 
		"                      decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf,\n" + 
		"                      decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs,\n" + 
		"                      decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf,\n" + 
		"                      decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf,\n" + 
		"                      decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys,\n" + 
		"                      decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf,\n" + 
		"                      decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf,\n" + 
		"                      decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys,\n" + 
		"                      decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf,\n" + 
		"                      0 as qitfy\n" + 
		"               from kuangfjsmkb js,kuangfjsyfb yf,hetb ht,yunsfsb ys,diancxxb dc,\n" + 
		"                  (select jss.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl\n" + 
		"                   from(\n" + 
		"                        select id," + getRiqLeixName() + " from kuangfjsmkb\n" + 
		"                        union\n" + 
		"                        select id," + getRiqLeixName() + " from kuangfjsyfb\n" + 
		"                   ) jss,\n" + 
		"                   jieszbsjb zl,\n" + 
		"                   zhibb zb\n" + 
		"                   where jss.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar'\n" + 
		"                     and jss." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + "\n" + 
		"                     and jss." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"               ) rl\n" + 
		"               where js.id=yf.kuangfjsmkb_id(+)\n" + 
		"               and js.hetb_id=ht.id(+)\n" + 
		"               and js.id=rl.id and js.yunsfsb_id=ys.id\n" + 
		"               and js.diancxxb_id=dc.id  and dc.id = 199\n" + 
		"               and js." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + "\n" + 
		"               and js." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"          ) sj\n" + 
		"          group by (diancxxb_id,gongysb_id,bianm)---------bianm---------\n" + 
		"     ) sj,\n" + 
		"     (select distinct js.diancxxb_id,js.gongysb_id,js.bianm,js.jiesrq,js.ruzrq,js.id basicid\n" + 
		"      from kuangfjsmkb js,kuangfjsyfb yf,hetb ht,yunsfsb ys,diancxxb dc\n" + 
		"      where js.id=yf.kuangfjsmkb_id(+)\n" + 
		"        and js.hetb_id=ht.id(+)\n" + 
		"        and js.yunsfsb_id=ys.id\n" + 
		"        and js.diancxxb_id=dc.id\n" + 
		"        and dc.id = 199\n" + 
		"        and js." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + "\n" + 
		"        and js." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n" + 
		"     ) fx\n" + 
		"     where sj.diancxxb_id(+)=fx.diancxxb_id and sj.gongysb_id(+)=fx.gongysb_id and sj.bianm(+)=fx.bianm---------\n" + 
		"     )\n" + 
		"--basicdata(所有基本数据)=kuangf union dianc\n" + 
		" ) basicdata\n" + 
		" where biaot.id=basicdata.basicid(+)\n" + 
		"   and biaot.flag=basicdata.flagx(+)\n" + 
		" order by xuh\n" + 
		"--完整数据\n" + 
		") sj,diancxxb dc,gongysb gy\n" + 
		"where sj.diancxxb_id =dc.id(+)\n" + 
		"  and sj.gongysb_id=gy.id(+)\n" + 
		"group by rollup\n" + 
		"  (sj.dmingc,(sj.gmingc,sj.xuh,sj.flag,sj.basicid,sj.bianm,sj.jiesrq,sj.ruzrq))\n" + 
		"  --having not grouping(sj.flag)=1\n" + 
		"order by\n" + 
		"  grouping(sj.dmingc) desc,grouping(sj.gmingc) desc,sj.xuh,sj.flag";

		
		System.out.println(sql.toString());
	    ResultSet rs = con.getResultSet(sql.toString());
		
		Report rt = new Report();
		int[] ArrWidth;
		String[][] ArrHeader;
		
		ArrHeader=new String[1][25];
		ArrHeader[0]=new String[] {"电厂名称","供应商名称","采购销售","结算单编号","结算日期",
				"入账日期","数量","入厂热量","综合价<br>(元/吨)","煤价<br>(元/吨)","增值税<br>(元/吨)",
				"矿运费<br>(元/吨)","交货<br>前运杂费<br>(元/吨)","铁路运费<br>(元/吨)","铁路<br>运费税额<br>(元/吨)",
				"到站<br>运费税额<br>(元/吨)","汽车运费<br>(元/吨)","汽运税额<br>(元/吨)","汽运杂费<br>(元/吨)",
				"海(水)<br>运费<br>(元/吨)","海(水)<br>运税额<br>(元/吨)","港杂费<br>(元/吨)","其它费用<br>(元/吨)",
				"含税<br>标煤单价<br>(元/吨)","不含税<br>标煤单价<br>(元/吨)"};
		ArrWidth = new int[25];
		ArrWidth=new int[] {80,80,30,75,70,70,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,80};
		
		rt.setTitle("结算统计查询", ArrWidth);

		
		rt.setDefaultTitle(1,25,getBRiq() + "  至  " + getERiq(),Table.ALIGN_CENTER);
		rt.setDefaultTitle(1,8,"填制单位：" + getTianzdw(((Visit)getPage().getVisit()).getDiancxxb_id()),Table.ALIGN_LEFT);
		rt.setBody(new Table(rs, 1, 0, 3));//行，0，列 
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(22,4,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		
		return rt.getAllPagesHtml();
	}
	
	private String getJiestjReport() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql=new StringBuffer();
	    
	    if("采购".equals(getLeix()[2])){
	    	sql.append(" select decode(grouping(gy.mingc),1,'总计',gy.mingc) gys,\n");
	    }else{
	    	sql.append(" select decode(grouping(dc.mingc),1,'总计',dc.mingc) dc,\n");
	    	sql.append("	    decode(grouping(dc.mingc)+grouping(gy.mingc),1,'合计',gy.mingc) gys,\n");
	    }
	    sql.append("		decode(grouping(dc.mingc)+grouping(gy.mingc),0,'" + getLeix()[2] + "','') as ck,");
	    sql.append("	    getHtmlAlert('"
					+ MainGlobal.getHomeContext(this)
					+ "','ShoumShowjsd','jiesdbh',sj.bianm,sj.bianm),\n");	    
	    sql.append("	    to_char(sj.jiesrq,'yyyy-mm-dd') jiesrq,\n");
	    sql.append("	    to_char(sj.ruzrq,'yyyy-mm-dd') ruzrq,\n");
	    sql.append("		sum(sj.jiessl) as jiessl,");
	    sql.append("	    decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl, \n");
	    sql.append("	    decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj, \n");
	    sql.append("	    decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj, \n");
	    sql.append("	    decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs, \n");
	    sql.append("	    decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf,\n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf, \n");
	    sql.append("		decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy, \n");
	    sql.append("		decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0,\n");
	    sql.append("		round_new(round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as hansbmdj,\n");
	    sql.append("		decode(sum(nvl(sj.jiessl,0)),0,0,decode(round_new(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),0),0,0, \n");
	    sql.append("		round_new((round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)-round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)\n");
	    sql.append("		  -round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)-round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2) \n");
	    sql.append("		  -round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2))*29.2712/round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2),2))) as buhsbmdj \n");
	    sql.append(" from\n");
	    sql.append("	(select fx.*,nvl(sj.jiessl,0) as jiessl,nvl(farl,0) as farl, \n");
	    sql.append("		nvl(sj.hansdj,0) as hansdj,nvl(sj.zonghj,0) as zonghj,nvl(sj.zengzs,0) as zengzs,nvl(sj.kuangyf,0) as kuangyf,nvl(sj.jiaohqyzf,0) as jiaohqyzf, \n");
	    sql.append("		nvl(sj.tielyf,0) as tielyf,nvl(sj.tielyfs,0) as tielyfs,nvl(sj.tielzf,0) as tielzf,nvl(sj.qiyf,0) as qiyf,nvl(sj.qiys,0) as qiys,nvl(sj.qiyzf,0) as qiyzf,\n");
	    sql.append("		nvl(sj.haiyf,0) as haiyf,nvl(haiys,0) as haiys,nvl(sj.gangzf,0) as gangzf,nvl(sj.qitfy,0) as qitfy  \n");
	    sql.append("	 from\n");
	    sql.append("		(select diancxxb_id,gongysb_id,\n");
	    sql.append("			sum(sj.jiessl) as jiessl,");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),2)) as farl,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf,\n");
	    sql.append("			decode(sum(sj.jiessl),0,0,round_new(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy \n");
	    sql.append("		from\n");
	    sql.append("			(select js.id,js.diancxxb_id,js.gongysb_id, \n");
	    sql.append("				nvl(js.jiessl,0) as jiessl,round_new(nvl(rl.farl,0),2) as farl,decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2)) as hansdj,\n");
	    sql.append("				decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.hansmk,0)/js.jiessl),2))\n");
	    sql.append("				  +decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)+round_new(nvl(0,0)/yf.jiessl,2))\n");
	    sql.append("				  +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n");
	    sql.append("				  +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n");
	    sql.append("				  +decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n");
	    sql.append("				  +decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n");
	    sql.append("				  +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n");
	    sql.append("				  +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n");
	    sql.append("				  +0 as zonghj, \n");
	    sql.append("				decode(nvl(js.jiessl,0),0,0,round_new((nvl(js.shuik,0)/js.jiessl),2)) as zengzs,\n");
	    sql.append("				decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf,\n");
	    sql.append("				decode(nvl(yf.jiessl,0),0,0,round_new(nvl(0,0)/yf.jiessl,2)) as jiaohqyzf, --ditzf\n");
	    sql.append("				decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf,\n");
	    sql.append("				decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs,\n");
	    sql.append("				decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf,\n");
	    sql.append("				decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf,\n");
	    sql.append("				decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys,\n");
	    sql.append("				decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf,\n");
	    sql.append("				decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf,\n");
	    sql.append("				decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys,\n");
	    sql.append("				decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round_new(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf,\n");
	    sql.append("				0 as qitfy \n");
	    sql.append("			from\n");
	    sql.append("				" + getLeix()[0] + " js," + getLeix()[1] + " yf,hetb ht,yunsfsb ys,diancxxb dc, \n");
	    sql.append("				(select jss.id,kcalkg_to_Mjkg(nvl(zl.jies,0),2) as farl  \n");
	    sql.append("					from \n ");
	    sql.append("					(select id," + getRiqLeixName() + " from " + getLeix()[0] + " union");
	    sql.append("					 select id," + getRiqLeixName() + " from " + getLeix()[1]);
	    sql.append("					) jss,");
	    sql.append("					jieszbsjb zl,zhibb zb\n");
	    sql.append("				 where jss.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='Qnetar' \n");
	    sql.append("				 	and jss." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + " \n");
	    sql.append("					and jss." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n");
	    sql.append("				) rl\n");
	    sql.append("			where js.id=yf." + getLeix()[0] + "_id(+) and js.hetb_id=ht.id(+) and js.id=rl.id and js.yunsfsb_id=ys.id\n");
	    sql.append("				and js.diancxxb_id=dc.id  and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + "\n");
	    sql.append("				and js." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + " \n");
	    sql.append("				and js." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + " \n");
	    sql.append("			) sj \n");
	    sql.append("		group by (diancxxb_id,gongysb_id)\n");
	    sql.append("		) sj,\n");
	    sql.append("		(select distinct js.diancxxb_id,js.gongysb_id ,js.bianm,js.jiesrq,js.ruzrq  \n");
	    sql.append("		 from " + getLeix()[0] + " js," + getLeix()[1] + " yf,hetb ht,yunsfsb ys,diancxxb dc \n");
	    sql.append("		 where js.id=yf." + getLeix()[0] + "_id(+) and js.hetb_id=ht.id(+) and js.yunsfsb_id=ys.id \n");
	    sql.append("				and js.diancxxb_id=dc.id  and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id() + "\n");
	    sql.append("				and js." + getRiqLeixName() + ">=" + DateUtil.FormatOracleDate(briq) + " and js." + getRiqLeixName() + "<=" + DateUtil.FormatOracleDate(eriq) + "\n");
	    sql.append("		) fx\n");
	    sql.append("	where sj.diancxxb_id(+)=fx.diancxxb_id and sj.gongysb_id(+)=fx.gongysb_id \n");
	    sql.append("	) sj,\n");
	    sql.append("	diancxxb dc,gongysb gy \n");
	    sql.append("where sj.diancxxb_id=dc.id and sj.gongysb_id=gy.id \n");
	    
	    sql.append("group by rollup\n");
	    if("采购".equals(getLeix()[2])){
	    	sql.append("	(dc.mingc,(gy.mingc,sj.bianm,sj.jiesrq,sj.ruzrq))\n");
	    }else{
	    	sql.append("	(dc.mingc,(gy.mingc,sj.bianm,sj.jiesrq,sj.ruzrq))\n");
	    }
	    sql.append("order by \n");
	    if(!"采购".equals(getLeix()[2])){
	    	sql.append("	grouping(dc.mingc) desc,\n");
	    }	    
	    sql.append("	grouping(gy.mingc)desc");
	    
	    
	    System.out.println(sql.toString());
	    ResultSet rs = con.getResultSet(sql.toString());
		
		Report rt = new Report();
		int[] ArrWidth;
		String[][] ArrHeader;
		if("采购".equals(getLeix()[2])){
			ArrHeader=new String[1][24];
			ArrHeader[0]=new String[] {"供应商名称","采购销售","结算单编号","结算日期","入账日期","数量","入厂热量",
					"综合价<br>(元/吨)","煤价<br>(元/吨)","增值税<br>(元/吨)","矿运费<br>(元/吨)","交货<br>前运杂费<br>(元/吨)",
					"铁路运费<br>(元/吨)","铁路<br>运费税额<br>(元/吨)","到站<br>运费税额<br>(元/吨)","汽车运费<br>(元/吨)",
					"汽运税额<br>(元/吨)","汽运杂费<br>(元/吨)","海(水)<br>运费<br>(元/吨)","海(水)<br>运税额<br>(元/吨)",
					"港杂费<br>(元/吨)","其它费用<br>(元/吨)","含税<br>标煤单价<br>(元/吨)","不含税<br>标煤单价<br>(元/吨)"};
			ArrWidth = new int[24];
			ArrWidth=new int[] {80,30,75,70,70,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,80};
			rt.setTitle("结算统计查询", ArrWidth);
			rt.setDefaultTitle(1,8,"填制单位：" + getTianzdw(((Visit)getPage().getVisit()).getDiancxxb_id()),Table.ALIGN_LEFT);
			rt.setDefaultTitle(10,5,getBRiq() + "  至  " + getERiq(),Table.ALIGN_CENTER);
		}else{//厂方、矿方厂方
			ArrHeader=new String[1][25];
			ArrHeader[0]=new String[] {"电厂名称","供应商名称","采购销售","结算单编号","结算日期",
					"入账日期","数量","入厂热量","综合价<br>(元/吨)","煤价<br>(元/吨)","增值税<br>(元/吨)",
					"矿运费<br>(元/吨)","交货<br>前运杂费<br>(元/吨)","铁路运费<br>(元/吨)","铁路<br>运费税额<br>(元/吨)",
					"到站<br>运费税额<br>(元/吨)","汽车运费<br>(元/吨)","汽运税额<br>(元/吨)","汽运杂费<br>(元/吨)",
					"海(水)<br>运费<br>(元/吨)","海(水)<br>运税额<br>(元/吨)","港杂费<br>(元/吨)","其它费用<br>(元/吨)",
					"含税<br>标煤单价<br>(元/吨)","不含税<br>标煤单价<br>(元/吨)"};
			ArrWidth = new int[25];
			ArrWidth=new int[] {80,80,30,75,70,70,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,80};
			rt.setTitle("结算统计查询", ArrWidth);
			
			rt.setDefaultTitle(1,25,getBRiq() + "  至  " + getERiq(),Table.ALIGN_CENTER);
			rt.setDefaultTitle(1,8,"填制单位：" + getTianzdw(((Visit)getPage().getVisit()).getDiancxxb_id()),Table.ALIGN_LEFT);
		}
		rt.setBody(new Table(rs, 1, 0, 3));//行，0，列 		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);		
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		if(ArrWidth.length==24){
			rt.body.setColAlign(3, Table.ALIGN_LEFT);
		}else
			rt.body.setColAlign(4, Table.ALIGN_LEFT);//第4列左右居中
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 4, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(21,4,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
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
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("时间类型:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("RiqLeixDropDown");
		cb1.setEditable(true);
		cb1.setWidth(100);
		tb1.addField(cb1);
//		
		tb1.addText(new ToolbarText("-"));
//		
		//日期
		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("rqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("rqe");
		tb1.addField(dfe);

		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("结算方:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("LeixDropDown");
		cb2.setEditable(true);
		cb2.setWidth(100);
		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	   // 时间类型
    public IDropDownBean getRiqLeixDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getRiqLeixDropDownModel().getOption(0));
    	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
    public void setRiqLeixDropDownValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);
    }
    public void setRiqLeixDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }
    public IPropertySelectionModel getRiqLeixDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getRiqLeixDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getRiqLeixDropDownModels() {
    	StringBuffer sql=new StringBuffer();
    	sql.append("select 1 id,'结算' mingc from dual union\n");
    	sql.append("select 2 id,'入账' mingc from dual ");
    	
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql.toString()));
        return ;
    }

    // 结算方
    public IDropDownBean getLeixDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixDropDownModel().getOption(0));
    	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixDropDownValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }
    public void setLeixDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }
    public IPropertySelectionModel getLeixDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixDropDownModels() {
    	StringBuffer sql=new StringBuffer();
    	sql.append("select 1 id,'采购' mingc from dual union\n");
    	sql.append("select 2 id,'销售' mingc from dual union\n");
    	sql.append("select 3 id,'全部' mingc from dual ");
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql.toString()));
        return ;
    }
    
    private String[] getLeix(){
    	String[] a=new String[3];
    	if(1==getLeixDropDownValue().getId()){
    		a[0]="kuangfjsmkb";
    		a[1]="kuangfjsyfb";
    		a[2]="采购";//矿方
    	}else if(2==getLeixDropDownValue().getId()){
    		a[0]="diancjsmkb";
    		a[1]="diancjsyfb";
    		a[2]="销售";//厂方
    	}
    	return a;
    }
    private String getRiqLeixName(){
    	if(1==getRiqLeixDropDownValue().getId()){
    		return "jiesrq";
    	}else{
    		return "ruzrq";
    	}
    }
    
    public String getTianzdw(long diancxxb_id) {//填制单位
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select quanc from diancxxb where id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("quanc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			String rq=DateUtil.FormatDate(new Date());
			this.setBRiq(rq);
			this.setERiq(rq);
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			
		}
		if(getBRiq()==null || getERiq()==null){
			String rq=DateUtil.FormatDate(new Date());
			this.setBRiq(rq);
			this.setERiq(rq);
		}
		getToolbars();
		if(cycle.getRequestContext().getParameter("lx")!=null){
			mstrReportName = cycle.getRequestContext().getParameter("lx");
		}
		blnIsBegin = true;
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
	
}
