package com.zhiren.heiljkhh.laihcrb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 黑龙江来耗存日报报表
 */

public class LaihcrbReport extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
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
	
	public String getPrintTable() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String zhi = MainGlobal.getXitxx_item("数量", "计划内是否有多口径", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
		String kouj = "and i.mingc = decode(lhc.diq, '地方煤', '市场采购', '七台河', '龙煤计划内', '双鸭山', '龙煤计划内', '鸡西', '龙煤计划内', lhc.diq)),\n";
		if (zhi.equals("是")) {
			kouj = 
				"and i.mingc = decode(lhc.diq, '地方煤', '市场采购', (\n" +
				"    select case\n" + 
				"             when (select it.mingc\n" + 
				"                     from ranlxyjhb jh, item it\n" + 
				"                    where jh.ranlxyjhzbb_id =\n" + 
				"                          (select distinct zb.id\n" + 
				"                             from ranlxyjhzbb zb\n" + 
				"                            where zb.diancxxb_id = lhc.diancxxb_id\n" + 
				"                              and to_char(zb.nianf, 'yyyy') = '"+ getYear(getRiq()) +"')\n" + 
				"                      and jh.zhibmc_item_id = it.id\n" + 
				"                      and it.mingc = lhc.diq) is null then\n" + 
				"              '龙煤计划内'\n" + 
				"             else\n" + 
				"              (select it.mingc\n" + 
				"                 from ranlxyjhb jh, item it\n" + 
				"                where jh.ranlxyjhzbb_id =\n" + 
				"                      (select distinct zb.id\n" + 
				"                         from ranlxyjhzbb zb\n" + 
				"                        where zb.diancxxb_id = lhc.diancxxb_id\n" + 
				"                          and to_char(zb.nianf, 'yyyy') = '"+ getYear(getRiq()) +"')\n" + 
				"                  and jh.zhibmc_item_id = it.id\n" + 
				"                  and it.mingc = lhc.diq)\n" + 
				"           end as a\n" + 
				"      from dual))),\n";
		}
		
		String selectSql = "";
		String dianc = "";
		String dianc_id = "";
		int dayOfMonth = getDay(getRiq());
		String totalDayOfMonth = getLastDayOfMonth(getRiq());
		
		if (getDiancTreeJib(con) == 2 || visit.isFencb() && getDiancTreeJib(con) == 3 && visit.getRenyjb() != 2) {
			dianc = "and dc.fuid = " + getTreeid();
			dianc_id = "in (select distinct diancxxb_id from heiljlhcrbb where riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd'))";
			
			selectSql += "select * from (\n" +
			"select t.dcmc, ycjc.yuecjc, t.kouj, t.diq, t.yuejhl, t.dangrlml, t.leijlml,\n" + 
			"		decode(t.kj+t.dq,\n" +
			"   		2, round(t.leijlml / (decode(t.yuejhl, 0, 1, t.yuejhl) / "+ totalDayOfMonth +" * "+ dayOfMonth +"), 4) * 100,\n" + 
			"   		1, round(t.leijlml / (decode(t.yuejhl, 0, 1, t.yuejhl) / "+ totalDayOfMonth +" * "+ dayOfMonth +"), 4) * 100,\n" + 
			"   		round(t.leijlml / (decode(t.yuejhl, 0, 1, t.yuejhl) / "+ totalDayOfMonth +" * "+ dayOfMonth +"), 4) * 100) as leijdhl,\n" +
			"       mh.dangrhml, ljmh.leijhml, bqkc.benqkc, round_new(bqkc.benqkc / mh.dangrhml, 0) pingjkyts, drjyl.dangrjyl,\n" + 
			"       ljjyl.leijjyl, drhyl.dangrhyl, ljhyl.leijhyl, kcy.kucy, '' as jizbh, '' as shebzt\n" + 
			"  from (select grouping(lhc.kouj) kj, grouping(lhc.diq) dq, '合计' dcmc,\n" + 
			"               decode(grouping(lhc.kouj), 1, '合计', lhc.kouj) kouj,\n" + 
			"               decode(grouping(lhc.kouj) + grouping(lhc.diq), 1, '小计', 2, '合计', lhc.diq) diq,\n" + 
			"               sum(nvl((select jh.y"+ getMonth(getRiq()) +"\n" + 
			"                         from ranlxyjhb jh, item i\n" + 
			"                        where jh.ranlxyjhzbb_id =\n" + 
			"                              (select distinct zb.id\n" + 
			"                                 from ranlxyjhzbb zb\n" + 
			"                                where zb.diancxxb_id = lhc.diancxxb_id\n" + 
			"                                  and to_char(zb.nianf, 'yyyy') = '"+ getYear(getRiq()) +"')\n" + 
			"                          and jh.zhibmc_item_id = i.id\n" + kouj +
			"                       0)) * 10000 yuejhl,\n" + 
			"               sum(lhc.dangrlml) dangrlml,\n" + 
			"               sum(lhc.leijlml) leijlml\n" + 
			"          from heiljlhcrbb lhc, diancxxb dc\n" + 
			"         where lhc.diancxxb_id = dc.id\n" + dianc + 
			"        \n and lhc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"         group by rollup(lhc.kouj, lhc.diq)\n" + 
			"	      having (grouping(lhc.diq) = 1 or lhc.kouj != '统配煤')\n" +
			"         order by grouping(lhc.kouj) desc, lhc.kouj, grouping(lhc.diq) desc) t,\n" + 
			"       (select nvl(sum(shc.haoyqkdr),0) dangrhml\n" + 
			"          from shouhcrbb shc\n" + 
			"         where shc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shc.diancxxb_id "+ dianc_id +") mh,\n" + 
			"       (select nvl(sum(shc.haoyqkdr),0) leijhml\n" + 
			"          from shouhcrbb shc\n" + 
			"         where shc.riq >= to_date('"+ getFirstDayOfThisMonth(getRiq()) +"', 'yyyy-mm-dd')\n" + 
			"           and shc.riq <= to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shc.diancxxb_id "+ dianc_id +") ljmh,\n" + 
			"       (select nvl(sum(shc.kuc),0) as benqkc\n" + 
			"          from shouhcrbb shc\n" + 
			"         where shc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shc.diancxxb_id "+ dianc_id +") bqkc,\n" + 
//			"       (select nvl(sum(round_new(shc.kuc / decode(shc.haoyqkdr, 0, 1, shc.haoyqkdr),2)),0) as pingjkyts\n" + 
//			"          from shouhcrbb shc\n" + 
//			"         where shc.riq = to_date('"+ getYesterday(getRiq()) +"', 'yyyy-mm-dd')\n" + 
//			"           and shc.diancxxb_id "+ dianc_id +") pjts,\n" + 
			"       (select nvl(sum(shcy.shourl),0) as dangrjyl\n" + 
			"          from shouhcrbyb shcy\n" + 
			"         where shcy.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shcy.diancxxb_id "+ dianc_id +") drjyl,\n" + 
			"       (select nvl(sum(shcy.shourl), 0) leijjyl\n" + 
			"          from shouhcrbyb shcy\n" + 
			"         where shcy.riq >= to_date('"+ getFirstDayOfThisMonth(getRiq()) +"', 'yyyy-mm-dd')\n" + 
			"           and shcy.riq <= to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shcy.diancxxb_id "+ dianc_id +") ljjyl,\n" + 
			"       (select nvl(sum(shcy.fady + shcy.gongry + shcy.qity),0) as dangrhyl\n" + 
			"          from shouhcrbyb shcy\n" + 
			"         where shcy.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shcy.diancxxb_id "+ dianc_id +") drhyl,\n" + 
			"       (select nvl(sum(shcy.fady + shcy.gongry + shcy.qity),0) as leijhyl\n" + 
			"          from shouhcrbyb shcy\n" + 
			"         where shcy.riq >= to_date('"+ getFirstDayOfThisMonth(getRiq()) +"', 'yyyy-mm-dd')\n" + 
			"           and shcy.riq <= to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shcy.diancxxb_id "+ dianc_id +") ljhyl,\n" + 
			"       (select nvl(sum(shcy.kuc),0) as kucy\n" + 
			"          from shouhcrbyb shcy\n" + 
			"         where shcy.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"           and shcy.diancxxb_id "+ dianc_id +") kcy,\n" + 
			"       (select nvl(sum(yhc.kuc),0) as yuecjc\n" + 
			"          from yuehcb yhc, yuetjkjb ykj\n" + 
			"         where yhc.yuetjkjb_id = ykj.id\n" + 
			"           and ykj.riq = to_date('"+ getFirstDayOfPreMonth(getRiq()) +"', 'yyyy-MM-dd')\n" + 
			"           and ykj.diancxxb_id "+ dianc_id +"\n" + 
			"           and yhc.fenx = '本月') ycjc)\n" + 
			"union all\n";
		} else {
			dianc = "and dc.id = " + getTreeid();
			dianc_id = "= ("+ getTreeid() +")";
		}
		
		selectSql += 
			"select * from (\n" + 
			"select dcb.dcmc, dcb.yuecjc, dcb.kouj, dcb.diq, dcb.yuejhl, dcb.dangrlml, dcb.leijlml,\n" + 
			"       dcb.leijdhl, dcb.dangrhml, dcb.leijhml, dcb.benqkc, dcb.pingjkyts, dcb.dangrjyl, dcb.leijjyl,\n" + 
			"       dcb.dangrhyl, dcb.leijhyl, dcb.kucy, jzb.jizbh, jzb.shebzt from\n" + 
			"(select row_number() over(partition by x.diancxxb_id order by x.kj desc, x.kouj, x.dq desc) con, x.* from\n" + 
			"(select grouping(d.kouj) kj,  grouping(d.diq) dq,\n" + 
			"       max(d.diancxxb_id) diancxxb_id,\n" + 
			"       max(d.dcmc) dcmc,\n" + 
			"       max(d.yuecjc) yuecjc,\n" + 
			"       decode(grouping(d.kouj)+grouping(d.diq), 2, '合计', d.kouj) kouj,\n" + 
			"       decode(grouping(d.kouj)+grouping(d.diq), 2, '合计', 1, '小计', d.diq) diq,\n" + 
			"       sum(d.yuejhl) * 10000 yuejhl,\n" + 
			"       sum(d.dangrlml) dangrlml,\n" + 
			"       sum(d.leijlml) leijlml,\n" + 
			"		decode(grouping(d.kouj)+grouping(d.diq),\n" +
			"   		2, round(sum(d.leijlml) / (decode(sum(d.yuejhl), 0, 1, sum(d.yuejhl)) * 10000 / "+ totalDayOfMonth +" * "+ dayOfMonth +"), 4) * 100,\n" + 
			"   		1, round(sum(d.leijlml) / (decode(sum(d.yuejhl), 0, 1, sum(d.yuejhl)) * 10000 / "+ totalDayOfMonth +" * "+ dayOfMonth +"), 4) * 100,\n" + 
			"   		round(max(d.leijlml) / (decode(max(d.yuejhl), 0, 1, max(d.yuejhl)) * 10000 / "+ totalDayOfMonth +" * "+ dayOfMonth +"), 4) * 100) as leijdhl,\n" +
			"       max(d.dangrhml) dangrhml,\n" + 
			"       max(d.leijhml) leijhml,\n" + 
			"       max(d.benqkc) benqkc,\n" + 
			"       max(d.pingjkyts) pingjkyts,\n" + 
			"       max(d.dangrjyl) dangrjyl,\n" + 
			"       max(d.leijjyl) leijjyl,\n" + 
			"       max(d.dangrhyl) dangrhyl,\n" + 
			"       max(d.leijhyl) leijhyl,\n" + 
			"       max(d.kucy) kucy\n" + 
			"  from (select lhc.id,\n" + 
			"               lhc.diancxxb_id,\n" + 
			"               dc.mingc dcmc,\n" + 
			"               nvl((select sum(yhc.kuc) as kuc\n" + 
			"                     from yuehcb yhc, yuetjkjb ykj\n" + 
			"                    where yhc.yuetjkjb_id = ykj.id\n" + 
			"                      and ykj.riq = to_date('"+ getFirstDayOfPreMonth(getRiq()) +"', 'yyyy-MM-dd')\n" + 
			"                      and ykj.diancxxb_id = lhc.diancxxb_id\n" + 
			"                      and yhc.fenx = '本月'),\n" + 
			"                   0) yuecjc,\n" + 
			"               lhc.kouj,\n" + 
			"               lhc.diq,\n" + 
			"               nvl((select jh.y"+ getMonth(getRiq()) +"\n" + 
			"                     from ranlxyjhb jh, item i\n" + 
			"                    where jh.ranlxyjhzbb_id =\n" + 
			"                          (select distinct zb.id\n" + 
			"                             from ranlxyjhzbb zb\n" + 
			"                            where zb.diancxxb_id = lhc.diancxxb_id\n" + 
			"                              and to_char(zb.nianf, 'yyyy') = '"+ getYear(getRiq()) +"')\n" + 
			"                      and jh.zhibmc_item_id = i.id\n" + kouj +
			"                   0) yuejhl,\n" + 
			"               lhc.dangrlml,\n" + 
			"               lhc.leijlml,\n" + 
			"               nvl((select shc.haoyqkdr\n" + 
			"                     from shouhcrbb shc\n" + 
			"                    where shc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                      and shc.diancxxb_id = lhc.diancxxb_id),\n" + 
			"                   0) dangrhml,\n" + 
			"               nvl((select sum(shc.haoyqkdr)\n" + 
			"                     from shouhcrbb shc\n" + 
			"                    where shc.riq >= to_date('"+ getFirstDayOfThisMonth(getRiq()) +"', 'yyyy-mm-dd')\n" + 
			"                      and shc.riq <= to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                      and shc.diancxxb_id = lhc.diancxxb_id),\n" + 
			"                   0) leijhml,\n" + 
			"               nvl((select shc.kuc\n" + 
			"                     from shouhcrbb shc\n" + 
			"                    where shc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                      and shc.diancxxb_id = lhc.diancxxb_id),\n" + 
			"                   0) benqkc,\n" + 
			"			   nvl((select round_new(shc.kuc / decode(gethaoyqkdr(lhc.diancxxb_id, '"+ getYesterday(getRiq()) +"'), null, 1, '', 1, gethaoyqkdr(lhc.diancxxb_id, '"+ getYesterday(getRiq()) +"')), 0) pjts\n" +
			"      				  from shouhcrbb shc\n" + 
			"     				 where shc.riq = to_date('"+ getYesterday(getRiq()) +"', 'yyyy-mm-dd')\n" + 
			"       			   and shc.diancxxb_id = lhc.diancxxb_id),\n" + 
			"    				0) pingjkyts,\n" +
			"               nvl((select shcy.shourl\n" + 
			"                     from shouhcrbyb shcy\n" + 
			"                    where shcy.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                      and shcy.diancxxb_id = lhc.diancxxb_id),\n" + 
			"                   0) dangrjyl,\n" + 
			"               nvl((select sum(shcy.shourl) shourl\n" + 
			"                     from shouhcrbyb shcy\n" + 
			"                    where shcy.riq >= to_date('"+ getFirstDayOfThisMonth(getRiq()) +"', 'yyyy-mm-dd')\n" + 
			"                      and shcy.riq <= to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                      and shcy.diancxxb_id = lhc.diancxxb_id),\n" + 
			"                   0) leijjyl,\n" + 
			"                nvl((select shcy.fady + shcy.gongry + shcy.qity as hyl\n" + 
			"                  from shouhcrbyb shcy\n" + 
			"                 where shcy.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                   and shcy.diancxxb_id = lhc.diancxxb_id),0) dangrhyl,\n" + 
			"                nvl((select sum(shcy.fady + shcy.gongry + shcy.qity) as ljhyl\n" + 
			"                  from shouhcrbyb shcy\n" + 
			"                 where shcy.riq >= to_date('"+ getFirstDayOfThisMonth(getRiq()) +"', 'yyyy-mm-dd')\n" + 
			"                   and shcy.riq <= to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                   and shcy.diancxxb_id = lhc.diancxxb_id),0) leijhyl,\n" + 
			"                nvl((select shcy.kuc\n" + 
			"                  from shouhcrbyb shcy\n" + 
			"                 where shcy.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                   and shcy.diancxxb_id = lhc.diancxxb_id),0) kucy\n" + 
			"          from heiljlhcrbb lhc, diancxxb dc\n" + 
			"         where lhc.diancxxb_id = dc.id\n" + dianc + 
			"        \n and lhc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')) d\n" + 
			"  group by rollup (d.dcmc, d.kouj, d.diq, d.id)\n" + 
			"  having not (grouping(d.diq)+grouping(d.id) = 1 or grouping(d.dcmc) = 1 or (grouping(d.diq)||d.kouj = '1统配煤' and max(d.diancxxb_id) != 228))\n" + 
			"  order by d.dcmc, grouping(d.kouj) desc, d.kouj, grouping(d.diq) desc) x) dcb,\n" + 
			"(select row_number() over(partition by jzzt.diancxxb_id order by jzzt.diancxxb_id, jzzt.jizbh) con, jzzt.* from (\n" + 
			"select distinct jz.diancxxb_id, jz.jizbh, qk.shebzt\n" + 
			"  from jizb jz, jizyxqkb qk\n" + 
			" where jz.diancxxb_id "+ dianc_id +"\n" + 
			"   and qk.jizb_id = jz.id\n" + 
			"   and qk.kaisrq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"   order by jz.diancxxb_id, jz.jizbh) jzzt) jzb\n" + 
			"  where dcb.con = jzb.con(+)\n" + 
			"    and dcb.diancxxb_id = jzb.diancxxb_id(+)\n" + 
			"order by dcb.dcmc, dcb.kj desc, dcb.kouj, dcb.dq desc, dcb.con)";
		
		String[][] ArrHeader = new String[2][19];
		
		ArrHeader[0] = new String[]{"项目","月初结存","统计口径","统计口径","月计划","来煤","来煤","来煤","耗煤","耗煤","本期库存",
			"平均可</br>用天数","油","油","油","油","油","机组运行","机组运行"};
		ArrHeader[1] = new String[]{"项目","月初结存","统计口径","统计口径","月计划","当日","累计","累计到货率(%)","当日","累计",
			"本期库存","平均可</br>用天数","进油","累计","耗油","累计","库存","机组运行","机组运行"};

		int[] ArrWidth = new int[] {95, 50, 55, 70, 50, 50, 50, 90, 50, 50, 50, 50, 55, 55, 55, 55, 55, 35, 45};
		
		ResultSetList rslData =  con.getResultSetList(selectSql);
		rt.setTitle(visit.getDiancqc()+"生产燃料来耗存情况日报表", ArrWidth);
		rt.setBody(new Table(rslData, 2, 0, 4));
		
		rt.body.setWidth(ArrWidth);
//		rt.body.setPageRows(33);
		rt.body.setHeaderData(ArrHeader);
		if (rt.body.getRows() > 2) {
			for(int i = 9; i<= 17; i++){
				rt.body.merge(3, i, rt.body.getRows(), i);
			}
			for(int i = 9; i<= 17; i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
		}
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		for(int i = 4; i< rt.body.getRows() ; i++){
			rt.body.merge(i, 3, i, 4);
		}
		rt.body.ShowZero = true;
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(18, Table.ALIGN_LEFT);
		rt.body.setColAlign(19, Table.ALIGN_LEFT);
		rt.body.setRowHeight(18);
		
		rt.setDefaultTitle(1, 4, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 7, "报表日期："+DateUtil.Formatdate("yyyy年MM月dd日", stringToDate(getRiq())), Table.ALIGN_CENTER);
		rt.setDefaultTitle(16, 2, "单位：吨", Table.ALIGN_CENTER);
//		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_CENTER);
//		rt.setDefautlFooter(10, 2, "审核：", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(15, 4, "制表：", Table.ALIGN_LEFT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + 
			visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel) getDiancmcModel())
			.getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null, null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("日期："));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
//		df.setListeners("change:function(){document.forms[0].submit();}");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	/**
	 * 取得在电厂树中选择的电厂的级别，1是集团，2是分公司，3是电厂
	 * @return
	 */
	public int getDiancTreeJib(JDBCcon con) {
		int jib = 3;
		String sql = "select d.jib from diancxxb d where d.id = " + this.getTreeid();
		ResultSetList rsl = con.getResultSetList(sql);
		
		if (rsl.next()) {
			jib = rsl.getInt("jib");
		}
		return jib;
	}
	
	/**
	 * 获取传入日期的月份的第一天
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfThisMonth(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			Date tempdate = sdf.parse(date);
			int month = DateUtil.getMonth(tempdate);
			int year =DateUtil.getYear(tempdate);
			return year+"-"+month+"-1";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(new Date());
	}
	
	/**
	 * 获取传入日期的上个月份的第一天
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfPreMonth(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			Date tempdate = sdf.parse(date);
			int month = DateUtil.getMonth(tempdate);
			int year =DateUtil.getYear(tempdate);
			if(month == 1){
				year -= 1;
				month = 12;
			}else{
				month -= 1;
			}
			return year+"-"+month+"-1";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return DateUtil.FormatDate(new Date());
	}
	
	/**
	 * 获取传入日期的前一天
	 * @param date
	 * @return
	 */
	public static String getYesterday(String date) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		String[] strdate = date.split("-");
		Calendar dateTime = Calendar.getInstance();
		dateTime.set(Integer.parseInt(strdate[0]), Integer.parseInt(strdate[1])-1, Integer.parseInt(strdate[2])-1);
		Date d = dateTime.getTime();
		return dft.format(d);
	}
	
	/**
	 * 获取传入日期的年份
	 * @param date
	 * @return
	 */
	public int getYear(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int year = DateUtil.getYear(new Date());
		try {
			Date tempdate = sdf.parse(date);
			year = DateUtil.getYear(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return year;
	}
	
	/**
	 * 获取传入日期的月份
	 * @param date
	 * @return
	 */
	public int getMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int month = DateUtil.getMonth(new Date());
		try {
			Date tempdate = sdf.parse(date);
			month = DateUtil.getMonth(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}
	
	/**
	 * 获取传入的日期是当月的第几天
	 * @param date
	 * @return
	 */
	public int getDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int day = DateUtil.getDay(new Date());
		try {
			Date tempdate = sdf.parse(date);
			day = DateUtil.getDay(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}
	
	/**
	 * 返回当月的最后一天
	 * @param strDate
	 * @return
	 */
	public static String getLastDayOfMonth(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), 0);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return sdf.format(ca.getTime());
	}
	
	/**
	 * String型日期转Data型日期
	 * @param date
	 * @return
	 */
	public Date stringToDate(String date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date temD = new Date();
		
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		try {
			temD = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temD;
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
	
//	电厂树_开始
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂树_结束
	
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			setRiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}