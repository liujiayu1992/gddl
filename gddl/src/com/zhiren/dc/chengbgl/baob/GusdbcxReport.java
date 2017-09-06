package com.zhiren.dc.chengbgl.baob;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：估收对比查询报表
 */

public class GusdbcxReport extends BasePage implements PageValidateListener {
	
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
	
//	到货日期
	private String daohrq;

	public String getDaohrq() {
		return daohrq;
	}

	public void setDaohrq(String daohrq) {
		this.daohrq = daohrq;
	}
	
//	供应商下拉框_开始
	public IDropDownBean getGongysValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getGongysModel().getOptionCount() > 0) {
				setGongysValue((IDropDownBean) getGongysModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setGongysValue(IDropDownBean LuhValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LuhValue);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getGongysModels() {
		String sql = 
			"select distinct g.id, g.mingc\n" +
			"  from fahb f, gongysb g\n" + 
			" where f.gongysb_id = g.id\n" + 
			"   and g.leix = 1\n" + 
			"   and f.id in\n" + 
			"       (select distinct f.id\n" + 
			"          from fahb f, jiesb j\n" + 
			"         where f.jiesb_id = j.id(+)\n" + 
			"           and j.ruzrq is not null\n" + 
			"           and f.diancxxb_id = "+ getTreeid() +"\n" + 
			"           and f.daohrq <= to_date('"+ getDaohrq() +"', 'yyyy-MM-dd'))\n" + 
			" order by g.mingc";
		setGongysModel(new IDropDownModel(sql, "全部"));
	}
//	供应商拉框_结束
	
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
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String[][] ArrHeader = null;
		int[] ArrWidth = null;
		String sql = "";
		
		if (getGongysValue().getStrId().equals("-1")) {
			sql = 
				"select decode(gys.mingc, null, '总计', gys.mingc) gysmc,\n" +
				"       gus.biaoz, gus.yingk, gus.yuns, gus.gusl, gus.rez, gus.guj, gus.meij, gus.yunj, gus.biaomdj, gus.buhsbmdj,\n" + 
				"       jies.jiesj, jies.js_meij, jies.js_yunj, jies.js_biaomdj, jies.js_buhsbmdj,\n" + 
				"       gus.guj - jies.jiesj as chaz_jiesj,\n" + 
				"       gus.meij - jies.js_meij as chaz_meij,\n" + 
				"       gus.yunj - jies.js_yunj as chaz_yunj,\n" + 
				"       gus.biaomdj - jies.js_biaomdj as chaz_biaomdj,\n" + 
				"       gus.buhsbmdj - jies.js_buhsbmdj as chaz_buhsbmdj from\n" + 
				"  (select decode(mk.meikdq_id, null, -2, mk.meikdq_id) meikdq_id,\n" + 
				"       sum(round_new(fh.biaoz, 0)) biaoz,\n" + 
				"       sum(round_new(fh.jingz, 0) + round_new(fh.yuns, 0) - round_new(fh.biaoz, 0)) yingk,\n" + 
				"       sum(round_new(fh.yuns, 0)) yuns,\n" + 
				"       sum(round_new(fh.jingz, 0)) gusl,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4id(ls.id,'rez')))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 0) as rez,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * (getGusxx4id(ls.id,'meij') + getGusxx4id(ls.id,'yunf'))))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as guj,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4id(ls.id,'meij')))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as meij,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * (getGusxx4id(ls.id,'yunf')+getGusxx4id(ls.id,'zaf')+getGusxx4id(ls.id,'fazzf')+getGusxx4id(ls.id,'ditf'))))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as yunj,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4id(ls.id,'meij')+getGusxx4id(ls.id,'yunf')+getGusxx4id(ls.id,'zaf')+getGusxx4id(ls.id,'fazzf')+getGusxx4id(ls.id,'ditf'))*7000/getGusxx4id(ls.id,'rez'),2)))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as biaomdj,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4id(ls.id,'meij')+getGusxx4id(ls.id,'yunf')+getGusxx4id(ls.id,'zaf')+getGusxx4id(ls.id,'fazzf')+getGusxx4id(ls.id,'ditf')-getGusxx4id(ls.id,'meis')-getGusxx4id(ls.id,'yunfs'))*7000/getGusxx4id(ls.id,'rez'),2)))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as buhsbmdj\n" + 
				"    from fahb fh, meikxxb mk, guslsb ls\n" + 
				"   where fh.meikxxb_id = mk.id\n" + 
				"     and ls.fahb_id = fh.id\n" + 
				"     and ls.id in (\n" + 
				"            select max(ls.id) guslsb_id\n" + 
				"              from fahb fh, jiesb js, guslsb ls\n" + 
				"             where fh.jiesb_id = js.id\n" + 
				"               and js.ruzrq is not null\n" + 
				"               and fh.diancxxb_id = "+ getTreeid() +"\n" + 
				"               and fh.daohrq <= to_date('"+ getDaohrq() +"', 'yyyy-MM-dd')\n" + 
				"               and ls.fahb_id = fh.id\n" + 
				"               and ls.leix != 4\n" + 
				"               group by ls.fahb_id\n" + 
				"         )\n" + 
				"  group by rollup(mk.meikdq_id)) gus,\n" + 
				"  (select decode(mk.meikdq_id, null, -2, mk.meikdq_id) meikdq_id,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * (getGusxx4id(ls.id, 'meij') + getGusxx4id(ls.id, 'yunf'))))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as jiesj,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4id(ls.id, 'meij')))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_meij,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * (getGusxx4id(ls.id,'yunf')+getGusxx4id(ls.id,'zaf')+getGusxx4id(ls.id,'fazzf')+getGusxx4id(ls.id,'ditf'))))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_yunj,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4id(ls.id,'meij')+getGusxx4id(ls.id,'yunf')+getGusxx4id(ls.id,'zaf')+getGusxx4id(ls.id,'fazzf')+getGusxx4id(ls.id,'ditf'))*7000/getGusxx4id(ls.id,'rez'),2)))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_biaomdj,\n" + 
				"       round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4id(ls.id,'meij')+getGusxx4id(ls.id,'yunf')+getGusxx4id(ls.id,'zaf')+getGusxx4id(ls.id,'fazzf')+getGusxx4id(ls.id,'ditf')-getGusxx4id(ls.id,'meis')-getGusxx4id(ls.id,'yunfs'))*7000/getGusxx4id(ls.id,'rez'),2)))\n" + 
				"           / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_buhsbmdj\n" + 
				"    from fahb fh, meikxxb mk, guslsb ls\n" + 
				"   where fh.meikxxb_id = mk.id\n" + 
				"     and ls.fahb_id = fh.id\n" + 
				"     and ls.id in (\n" + 
				"            select max(ls.id) guslsb_id\n" + 
				"              from fahb fh, jiesb js, guslsb ls\n" + 
				"             where fh.jiesb_id = js.id\n" + 
				"               and js.ruzrq is not null\n" + 
				"               and fh.diancxxb_id = "+ getTreeid() +"\n" + 
				"               and fh.daohrq <= to_date('"+ getDaohrq() +"', 'yyyy-MM-dd')\n" + 
				"               and ls.fahb_id = fh.id\n" + 
				"               and ls.leix = 4\n" + 
				"            group by ls.fahb_id\n" + 
				"         )\n" + 
				"  group by rollup(mk.meikdq_id)) jies,\n" + 
				"  gongysb gys\n" + 
				"where gus.meikdq_id = jies.meikdq_id\n" + 
				"  and gus.meikdq_id = gys.id(+)\n" + 
				"order by gus.meikdq_id";
			
			ArrHeader = new String[2][21];
			ArrWidth = new int[] {150,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60};
			ArrHeader[0] = new String[]{"煤矿地区","票重","盈亏","运损","估收量","热值","暂估","暂估","暂估","暂估","暂估","实际结算","实际结算","实际结算","实际结算","实际结算",
				"差值","差值","差值","差值","差值"};
			ArrHeader[1] = new String[]{"煤矿地区","票重","盈亏","运损","估收量","热值","估价","煤价","运价","标煤<br>单价","不含税<br>标煤单价","结算价","煤价","运价","标煤<br>单价","不含税<br>标煤单价",
					"结算价","煤价","运价","标煤<br>单价","不含税<br>标煤单价"};
		} else {
			sql = 
				"select tongj.mingc, tongj.fahrq, tongj.daohrq, tongj.chec, tongj.biaoz, tongj.yingk, tongj.yuns, tongj.gusl,\n" +
				"       tongj.rez, tongj.guj, tongj.meij, tongj.yunj, tongj.biaomdj, tongj.buhsbmdj, tongj.jiesj, tongj.js_meij,\n" + 
				"       tongj.js_yunj, tongj.biaomdj, tongj.buhsbmdj,\n" + 
				"       tongj.guj - decode(tongj.jiesj, null, 0, tongj.jiesj) as chaz_jiesj,\n" + 
				"       tongj.meij - decode(tongj.js_meij, null, 0, tongj.js_meij) as chaz_meij,\n" + 
				"       tongj.yunj - decode(tongj.js_yunj, null, 0, tongj.js_yunj) as chaz_yunj,\n" + 
				"       tongj.biaomdj - decode(tongj.js_biaomdj, null, 0, tongj.js_biaomdj) as chaz_biaomdj,\n" + 
				"       tongj.buhsbmdj - decode(tongj.js_buhsbmdj, null, 0, tongj.js_buhsbmdj) as chaz_buhsbmdj from (\n" + 
				"  select /*grouping(mk.mingc) mkmc, grouping(fh.fahrq) fhrq, grouping(fh.daohrq) dhrq, grouping(fh.chec) cc,*/\n" + 
				"         decode(mk.mingc, null, '总计', mk.mingc) mingc,\n" + 
				"         decode(fh.fahrq, null, decode(mk.mingc, null, '', '小计'), to_char(fh.fahrq, 'yyyy-mm-dd')) fahrq,\n" + 
				"         to_char(fh.daohrq, 'yyyy-mm-dd') daohrq,\n" + 
				"         fh.chec,\n" + 
				"         sum(round_new(fh.biaoz, 0)) biaoz,\n" + 
				"         sum(round_new(fh.jingz, 0) + round_new(fh.yuns, 0) - round_new(fh.biaoz, 0)) yingk,\n" + 
				"         sum(round_new(fh.yuns, 0)) yuns,\n" + 
				"         sum(round_new(fh.jingz, 0)) gusl,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4jies(fh.id, 'rez')))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 0) as rez,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * (getGusxx4zang(fh.id, 'meij') + getGusxx4zang(fh.id, 'yunf'))))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as guj,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4zang(fh.id, 'meij')))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as meij,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * (getGusxx4zang(fh.id,'yunf')+getGusxx4zang(fh.id,'zaf')+getGusxx4zang(fh.id,'fazzf')+getGusxx4zang(fh.id,'ditf'))))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as yunj,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4zang(fh.id,'meij')+getGusxx4zang(fh.id,'yunf')+getGusxx4zang(fh.id,'zaf')+getGusxx4zang(fh.id,'fazzf')+getGusxx4zang(fh.id,'ditf'))*7000/getGusxx4zang(fh.id,'rez'),2)))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as biaomdj,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4zang(fh.id,'meij')+getGusxx4zang(fh.id,'yunf')+getGusxx4zang(fh.id,'zaf')+getGusxx4zang(fh.id,'fazzf')+getGusxx4zang(fh.id,'ditf')-getGusxx4zang(fh.id,'meis')-getGusxx4zang(fh.id,'yunfs'))*7000/getGusxx4zang(fh.id,'rez'),2)))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as buhsbmdj,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * (getGusxx4jies(fh.id, 'meij') + getGusxx4jies(fh.id, 'yunf'))))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as jiesj,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz, 0), 0, 0, fh.jingz * getGusxx4jies(fh.id, 'meij')))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_meij,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * (getGusxx4jies(fh.id,'yunf')+getGusxx4jies(fh.id,'zaf')+getGusxx4jies(fh.id,'fazzf')+getGusxx4jies(fh.id,'ditf'))))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_yunj,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4jies(fh.id,'meij')+getGusxx4jies(fh.id,'yunf')+getGusxx4jies(fh.id,'zaf')+getGusxx4jies(fh.id,'fazzf')+getGusxx4jies(fh.id,'ditf'))*7000/getGusxx4jies(fh.id,'rez'),2)))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_biaomdj,\n" + 
				"         round_new(sum(decode(round_new(fh.jingz,0), 0, 0, round_new(fh.jingz,0) * round_new((getGusxx4jies(fh.id,'meij')+getGusxx4jies(fh.id,'yunf')+getGusxx4jies(fh.id,'zaf')+getGusxx4jies(fh.id,'fazzf')+getGusxx4jies(fh.id,'ditf')-getGusxx4jies(fh.id,'meis')-getGusxx4jies(fh.id,'yunfs'))*7000/getGusxx4jies(fh.id,'rez'),2)))\n" + 
				"             / sum(decode(round_new(fh.jingz, 0), 0, 1, round_new(fh.jingz,0))), 2) as js_buhsbmdj\n" + 
				"    from fahb fh, meikxxb mk, gongysmkglb gmgl\n" + 
				"   where fh.id in (select distinct ls.fahb_id\n" + 
				"                     from fahb fh, jiesb js, guslsb ls\n" + 
				"                    where fh.jiesb_id = js.id\n" + 
				"                      and js.ruzrq is not null\n" + 
				"                      and fh.diancxxb_id = "+ getTreeid() +"\n" + 
				"                      and fh.daohrq <= to_date('"+ getDaohrq() +"', 'yyyy-MM-dd')\n" + 
				"                      and ls.fahb_id = fh.id\n" + 
				"                      and ls.leix = 4)\n" + 
				"     and fh.meikxxb_id = mk.id\n" + 
				"     and mk.id = gmgl.meikxxb_id\n" + 
				"     and gmgl.gongysb_id = "+ getGongysValue().getStrId() +"\n" + 
				"   group by rollup(mk.mingc, fh.fahrq, fh.daohrq, fh.chec)\n" + 
				"   having not (grouping(fh.fahrq) <> 1 and grouping(fh.chec) = 1)\n" + 
				"   order by grouping(mk.mingc) desc, mk.mingc, grouping(fh.fahrq) desc, fh.fahrq) tongj";

			ArrHeader = new String[2][24];
			ArrWidth = new int[] {130,80,80,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60};
			ArrHeader[0] = new String[]{"煤矿","发货日期","到货日期","车次","票重","盈亏","运损","估收量","热值","暂估","暂估","暂估","暂估","暂估","实际结算","实际结算","实际结算","实际结算","实际结算",
				"差值","差值","差值","差值","差值"};
			ArrHeader[1] = new String[]{"煤矿","发货日期","到货日期","车次","票重","盈亏","运损","估收量","热值","估价","煤价","运价","标煤<br>单价","不含税<br>标煤单价","结算价","煤价","运价","标煤<br>单价","不含税<br>标煤单价",
					"结算价","煤价","运价","标煤<br>单价","不含税<br>标煤单价"};
		}
		
		ResultSetList rslData =  con.getResultSetList(sql);
		rt.setTitle("暂估与实际结算对比查询", ArrWidth);
		rt.setBody(new Table(rslData, 2, 0, 1));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		if (!getGongysValue().getStrId().equals("-1")) {
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
		}
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.setDefaultTitle(1, 5, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 4, "审核：", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(12, 4, "制表：", Table.ALIGN_RIGHT);

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
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
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
		
		tbr.addText(new ToolbarText("到货日期："));
		DateField df = new DateField();
		df.setValue(getDaohrq());
		df.setId("Daohrq");
		df.Binding("Daohrq", "forms[0]");
//		df.setListeners("change:function(){document.forms[0].submit();}");
		tbr.addField(df);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("供应商："));
		ComboBox gys_comb = new ComboBox();
		gys_comb.setTransform("Gongys");
		gys_comb.setWidth(100);
		gys_comb.setListWidth(120);
//		gys_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		gys_comb.setLazyRender(true);
		tbr.addField(gys_comb);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName().toString());
			this.setTreeid(null);
			setDaohrq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel3(null); // 供应商下拉框
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}
}