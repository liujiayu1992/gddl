package com.zhiren.dc.hesgl.daltjs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 2010-08-13
 * 类名：结算方案关联
 */

public class Jiesfagl extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String rowNumIndex; // 保存从上个页面传过来的jiesfab_id

	public String getRowNumIndex() {
		return rowNumIndex;
	}

	public void setRowNumIndex(String rowNumIndex) {
		this.rowNumIndex = rowNumIndex;
	}
	
	private String jiesdw; // 保存结算单位id，这是一个diancxxb_id
	
	public String getJiesdw() {
		return jiesdw;
	}

	public void setJiesdw(String jiesdw) {
		this.jiesdw = jiesdw;
	}
	
	private long jieslx; // 保存结算类型(煤款结算、国铁运费)
	
	public long getJieslx() {
		return jieslx;
	}

	public void setJieslx(long jieslx) {
		this.jieslx = jieslx;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

//	"重算"按钮
	private boolean _ChongsClick = false;

	public void ChongsButton(IRequestCycle cycle) {
		_ChongsClick = true;
	}
	
//	"返回"按钮
	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			cycle.activate("Jiesfawh");
		}
		if (_ChongsClick) {
			_ChongsClick = false;
			chongs();
		}
	}
	
	/**
	 * 删除jiesfaglb表中与结算方案关联的发货信息，当页面加载时会从fahb中重新查询数据。
	 */
	public void chongs() {
		
		JDBCcon con = new JDBCcon();
		
		StringBuffer sbsql = new StringBuffer("begin\n");		
		sbsql.append("delete from jiesfaglb gl where gl.jiesfab_id = ").append(((Visit) this.getPage().getVisit()).getString10()).append(";\n")
		.append("end;");
		con.getUpdate(sbsql.toString());
		con.Close();
		setMsg("数据重算成功！");
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		sbsql.append("delete from jiesfaglb gl where gl.jiesfab_id = ").append(visit.getString10()).append(";\n");
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			while (mdrsl.next()) {
				sbsql.append("insert into jiesfaglb(id, jiesfab_id, fahb_id, hetb_id, jieskd, bukyqyf, yunsdwb_id) values(getnewid(")
				.append(getJiesdw()).append("), ").append(visit.getString10()).append(", ").append(mdrsl.getString("FAHB_ID")).append(", ")
				.append(getSqlValue4Combox((getExtGrid().getColumn("HETB_ID").combo).getBeanId(mdrsl.getString("HETB_ID")))).append(", ")
				.append(getSqlValue(mdrsl.getString("JIESKD"))).append(", ")
				.append(getSqlValue(mdrsl.getString("BUKYQYF"))).append(", ")
				.append((getExtGrid().getColumn("YUNSDWB_ID").combo).getBeanId(mdrsl.getString("YUNSDWB_ID"))).append(");\n");
			}
		} else {
			while (mdrsl.next()) {
				sbsql.append("insert into jiesfaglb(id, jiesfab_id, fahb_id, hetb_id, jieskd, bukyqjk) values(getnewid(")
				.append(getJiesdw()).append("), ").append(visit.getString10()).append(", ").append(mdrsl.getString("FAHB_ID")).append(", ")
				.append(getSqlValue4Combox((getExtGrid().getColumn("HETB_ID").combo).getBeanId(mdrsl.getString("HETB_ID")))).append(", ")
				.append(getSqlValue(mdrsl.getString("JIESKD"))).append(", ")
				.append(getSqlValue(mdrsl.getString("BUKYQJK"))).append(");\n");
			}
		}
		
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit) getPage().getVisit();
		String fahqssj = "";	// 发货起始时间
		String fahjzsj = "";	// 发货截止时间
		
		JDBCcon con = new JDBCcon();
		String sql = "select fa.jiesdw_id, fa.fahqssj, fa.fahjzsj, fa.jieslx from jiesfab fa where fa.id = " + visit.getString10();
		ResultSetList rsl = con.getResultSetList(sql);
		
		while (rsl.next()) { // 取得结算方案信息
			fahqssj = rsl.getDateString("fahqssj");
			fahjzsj = rsl.getDateString("fahjzsj");
			setJiesdw(rsl.getString("jiesdw_id"));
			setJieslx(rsl.getLong("jieslx"));
		}
		
		sql = "select gl.id from jiesfaglb gl where gl.jiesfab_id = " + visit.getString10();
		rsl = con.getResultSetList(sql);
		if (rsl.next()) { // 结算方案已关联结算信息
			
			if (getJieslx() == Locale.guotyf_feiylbb_id) {
				
				sql = 
					"select /*grouping(fa.yunsdwmc) ysdw, grouping(fa.meikxxb_id) mk, grouping(fa.fahb_id) fh,*/\n" +
					"       decode(grouping(fa.fahb_id), 1, '', max(fa.zhuangt)) zhuangt,\n" + 
					"       /*decode(grouping(fa.fahb_id), 1, '', max(fa.js_fahb_id)) js_fahb_id,*/\n" + 
					"       fa.fahb_id,\n" + 
					"       decode(grouping(fa.yunsdwmc), 1, '总计', fa.yunsdwmc) yunsdwb_id,\n" + 
					"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id), 1, '合计', fa.meikxxb_id) meikxxb_id,\n" + 
					"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, '小计', 2, '', 3, '', max(fa.pinzb_id)) pinzb_id,\n" + 
					"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, '', 2, '', 3, '', max(fa.jihkjb_id)) jihkjb_id,\n" + 
					"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, to_date(null), 2, '', 3, '', max(fa.fahrq)) fahrq,\n" + 
					"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, to_date(null), 2, '', 3, '', max(fa.daohrq)) daohrq,\n" + 
					"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, '', 2, '', 3, '', max(fa.hetb_id)) hetb_id,\n" + 
					"       sum(fa.jieskd) jieskd,\n" + 
					"       sum(fa.bukyqyf) bukyqyf,\n" + 
					"       sum(fa.maoz) maoz,\n" + 
					"       sum(fa.piz) piz,\n" + 
					"       sum(fa.biaoz) biaoz,\n" + 
					"       sum(fa.yingd) yingd,\n" + 
					"       sum(fa.yingk) yingk,\n" + 
					"       sum(fa.yuns) yuns,\n" + 
					"       sum(fa.koud) koud,\n" + 
					"       sum(fa.ches) ches\n" + 
					"    from (\n" + 
					"        select decode(grouping(xx.fahb_id), 1, '', case when max(js.fahb_id) is null then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×' else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√' end) as zhuangt,\n" + 
					"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, '', 2, '', 3, '', max(js.fahb_id)) js_fahb_id,\n" + 
					"               /*grouping(xx.yunsdwmc) ysdw, grouping(xx.meikmc) mk, grouping(xx.fahb_id) fhid,*/\n" + 
					"               xx.fahb_id,\n" + 
					"               max(xx.yunsdwb_id) yunsdwb_id,\n" + 
					"               decode(grouping(xx.yunsdwmc), 1, '总计', xx.yunsdwmc) yunsdwmc,\n" + 
					"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc), 1, '合计', xx.meikmc) meikxxb_id,\n" + 
					"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, '小计', 2, '', 3, '', max(xx.pinzb_id)) pinzb_id,\n" + 
					"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, '', 2, '', 3, '', max(xx.jihkjb_id)) jihkjb_id,\n" + 
					"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, to_date(null), 2, '', 3, '', max(xx.fahrq)) fahrq,\n" + 
					"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, to_date(null), 2, '', 3, '', max(xx.daohrq)) daohrq,\n" + 
					"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, '', 2, '', 3, '', max(js.hetb_id)) hetb_id,\n" + 
					"               max(js.jieskd) jieskd,\n" + 
					"               max(js.bukyqyf) bukyqyf,\n" + 
					"               sum(xx.maoz) maoz,\n" + 
					"               sum(xx.piz) piz,\n" + 
					"               sum(xx.biaoz) biaoz,\n" + 
					"               sum(xx.yingd) yingd,\n" + 
					"               sum(xx.yingk) yingk,\n" + 
					"               sum(xx.yuns) yuns,\n" + 
					"               sum(xx.koud) koud,\n" + 
					"               count(xx.chepb_id) ches\n" + 
					"          from (select fh.id fahb_id,\n" + 
					"                       ysdw.id yunsdwb_id,\n" + 
					"                       ysdw.mingc yunsdwmc,\n" + 
					"                       mk.mingc meikmc,\n" + 
					"                       pz.mingc pinzb_id,\n" + 
					"                       kj.mingc jihkjb_id,\n" + 
					"                       fh.fahrq,\n" + 
					"                       fh.daohrq,\n" + 
					"                       cp.maoz,\n" + 
					"                       cp.piz,\n" + 
					"                       cp.biaoz,\n" + 
					"                       cp.yingd,\n" + 
					"                       cp.yingk,\n" + 
					"                       cp.yuns,\n" + 
					"                       cp.koud,\n" + 
					"                       cp.id chepb_id\n" + 
					"                  from fahb    fh,\n" + 
					"                       gongysb gys,\n" + 
					"                       meikxxb mk,\n" + 
					"                       pinzb   pz,\n" + 
					"                       chezxxb faz,\n" + 
					"                       chezxxb daoz,\n" + 
					"                       jihkjb  kj,\n" + 
					"                       zhilb   zl,\n" + 
					"                       chepb   cp,\n" + 
					"                       danjcpb djcp,\n" + 
					"                       yunsdwb ysdw\n" + 
					"                 where fh.fahrq >= to_date('"+ fahqssj +"', 'yyyy-mm-dd')\n" + 
					"                   and fh.fahrq <= to_date('"+ fahjzsj +"', 'yyyy-mm-dd')\n" + 
					"                   and fh.liucztb_id = 1\n" + 
					"                   and fh.gongysb_id = gys.id\n" + 
					"                   and fh.meikxxb_id = mk.id\n" + 
					"                   and fh.pinzb_id = pz.id\n" + 
					"                   and fh.faz_id = faz.id\n" + 
					"                   and fh.daoz_id = daoz.id\n" + 
					"                   and fh.jihkjb_id = kj.id\n" + 
					"                   and fh.zhilb_id = zl.id\n" + 
					"                   and cp.fahb_id = fh.id\n" + 
					"                   and djcp.chepb_id(+) = cp.id\n" + 
					"                   and (djcp.yunfjsb_id = 0 or djcp.yunfjsb_id is null)\n" + 
					"                   and cp.yunsdwb_id = ysdw.id\n" + 
					"                 order by fh.id, ysdw.mingc, fh.meikxxb_id) xx,\n" + 
					"               (select gl.fahb_id, gl.yunsdwb_id, gl.jieskd, gl.bukyqyf, ysht.hetbh hetb_id\n" + 
					"                  from jiesfaglb gl, hetys ysht\n" + 
					"                 where gl.jiesfab_id = "+ visit.getString10() +"\n" +
					"                   and gl.hetb_id = ysht.id(+)\n" + 
					"                 order by gl.fahb_id) js\n" + 
					"         where xx.fahb_id = js.fahb_id(+)\n" + 
					"           and xx.yunsdwb_id = js.yunsdwb_id(+)\n" + 
					"         group by rollup(xx.yunsdwmc, xx.meikmc, xx.fahb_id)\n" + 
					"         having not grouping(xx.fahb_id) = 1) fa\n" + 
					"group by rollup(fa.yunsdwmc, fa.meikxxb_id, fa.fahb_id)";

				
			} else {
				
				sql = 
					"select decode(grouping(fh.id), 1, '', case when max(fagl.fahb_id) is null then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×' else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√' end) as zhuangt,\n" +
					"       fh.id fahb_id,\n" +
					"		/*grouping(gys.mingc) gmc, grouping(mk.mingc) km, grouping(fh.id) gid,*/\n" +
					"       decode(grouping(gys.mingc), 1, '总计', gys.mingc) as gongysb_id,\n" + 
					"       decode(grouping(gys.mingc) + grouping(mk.mingc), 1, '合计', mk.mingc) as meikxxb_id,\n" + 
					"       decode(grouping(mk.mingc) + grouping(fh.id), 1, '小计', 2, '', max(pz.mingc)) as pinzb_id,\n" + 
					"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(faz.mingc)) as faz_id,\n" + 
					"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(daoz.mingc)) as daoz_id,\n" + 
					"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(kj.mingc)) as jihkjb_id,\n" + 
					"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.fahrq)) as fahrq,\n" + 
					"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.daohrq)) as daohrq,\n" + 
					"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(fagl.hetbh)) as hetb_id,\n" + 
					"		decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fagl.jieskd)), sum(decode(fagl.fahb_id, '', 0, fagl.jieskd))) as jieskd,\n" +
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fagl.bukyqjk)), sum(decode(fagl.fahb_id, '', 0, fagl.bukyqjk))) as bukyqjk,\n" +		
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.maoz)), sum(decode(fagl.fahb_id, '', 0, fh.maoz))) as maoz,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.piz)), sum(decode(fagl.fahb_id, '', 0, fh.piz))) as piz,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.jingz)), sum(decode(fagl.fahb_id, '', 0, fh.jingz))) as jingz,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.biaoz)), sum(decode(fagl.fahb_id, '', 0, fh.biaoz))) as biaoz,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.yingd)), sum(decode(fagl.fahb_id, '', 0, fh.yingd))) as yingd,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.yingk)), sum(decode(fagl.fahb_id, '', 0, fh.yingk))) as yingk,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.yuns)), sum(decode(fagl.fahb_id, '', 0, fh.yuns))) as yuns,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.koud)), sum(decode(fagl.fahb_id, '', 0, fh.koud))) as koud,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.ches)), sum(decode(fagl.fahb_id, '', 0, fh.ches))) as ches,\n" + 
					"       decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(fh.laimsl)), sum(decode(fagl.fahb_id, '', 0, fh.laimsl))) as laimsl,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.qnet_ar)), sum(decode(fagl.fahb_id, '', 0, zl.qnet_ar) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 3) as qnet_ar,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.aar)), sum(decode(fagl.fahb_id, '', 0, zl.aar) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as aar,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.ad)), sum(decode(fagl.fahb_id, '', 0, zl.ad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as ad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.vdaf)), sum(decode(fagl.fahb_id, '', 0, zl.vdaf) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as vdaf,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.mt)), sum(decode(fagl.fahb_id, '', 0, zl.mt) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as mt,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.stad)), sum(decode(fagl.fahb_id, '', 0, zl.stad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as stad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.aad)), sum(decode(fagl.fahb_id, '', 0, zl.aad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as aad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.mad)), sum(decode(fagl.fahb_id, '', 0, zl.mad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as mad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.qbad)), sum(decode(fagl.fahb_id, '', 0, zl.qbad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as qbad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.had)), sum(decode(fagl.fahb_id, '', 0, zl.had) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as had,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.vad)), sum(decode(fagl.fahb_id, '', 0, zl.vad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as vad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.fcad)), sum(decode(fagl.fahb_id, '', 0, zl.fcad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as fcad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.std)), sum(decode(fagl.fahb_id, '', 0, zl.std) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as std,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.qgrad)), sum(decode(fagl.fahb_id, '', 0, zl.qgrad) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as qgrad,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.hdaf)), sum(decode(fagl.fahb_id, '', 0, zl.hdaf) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as hdaf,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.qgrad_daf)), sum(decode(fagl.fahb_id, '', 0, zl.qgrad_daf) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as qgrad_daf,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.sdaf)), sum(decode(fagl.fahb_id, '', 0, zl.sdaf) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as sdaf,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.var)), sum(decode(fagl.fahb_id, '', 0, zl.var) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as var,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.t1)), sum(decode(fagl.fahb_id, '', 0, zl.t1) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as t1,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.t2)), sum(decode(fagl.fahb_id, '', 0, zl.t2) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as t2,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.t3)), sum(decode(fagl.fahb_id, '', 0, zl.t3) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as t3,\n" + 
					"       round_new(decode(max(fagl.fahb_id), '', decode(grouping(fh.id), 1, 0, max(zl.t4)), sum(decode(fagl.fahb_id, '', 0, zl.t4) * fh.laimsl) / sum(decode(fagl.fahb_id, '', 0, fh.laimsl))), 2) as t4" +
					"  from fahb    fh,\n" + 
					"       gongysb gys,\n" + 
					"       meikxxb mk,\n" + 
					"       pinzb   pz,\n" + 
					"       chezxxb faz,\n" + 
					"       chezxxb daoz,\n" + 
					"       jihkjb  kj,\n" + 
					"       hetb    ht,\n" + 
					"       zhilb   zl,\n" + 
					"       (select gl.fahb_id, gl.jieskd, gl.bukyqjk, ht.hetbh\n" +
					"         from jiesfaglb gl, hetb ht\n" + 
					"        where gl.jiesfab_id = "+ visit.getString10() +"\n" + 
					"          and gl.hetb_id = ht.id(+)) fagl\n" +
					" where fh.fahrq >= to_date('"+ fahqssj +"', 'yyyy-mm-dd')\n" + 
					"   and fh.fahrq <= to_date('"+ fahjzsj +"', 'yyyy-mm-dd')\n" + 
					"   and fh.jiesb_id = 0\n" + 
					"   and fh.liucztb_id = 1\n" +
					"   and fh.gongysb_id = gys.id\n" + 
					"   and fh.meikxxb_id = mk.id\n" + 
					"   and fh.pinzb_id = pz.id\n" + 
					"   and fh.faz_id = faz.id\n" + 
					"   and fh.daoz_id = daoz.id\n" + 
					"   and fh.jihkjb_id = kj.id\n" + 
					"   and fh.hetb_id = ht.id(+)\n" + 
					"   and fh.zhilb_id = zl.id\n" + 
					"   and fh.id = fagl.fahb_id(+)" +
					" group by rollup (gys.mingc, mk.mingc, fh.id)";
			}
			
		} else if (getJieslx() == Locale.guotyf_feiylbb_id) {
			
			sql = 
				"select decode(grouping(fh.id), 1, '', '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√') as zhuangt,\n" +
				"       /*grouping(ysdw.mingc) ysdw, grouping(mk.mingc) mk, grouping(fh.id) fhid,*/\n" + 
				"       fh.id fahb_id,\n" + 
				"       decode(grouping(ysdw.mingc), 1, '总计', ysdw.mingc) yunsdwb_id,\n" + 
				"       decode(grouping(ysdw.mingc) + grouping(mk.mingc), 1, '合计', mk.mingc) meikxxb_id,\n" + 
				"       decode(grouping(ysdw.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '小计', 2, '', 3, '', max(pz.mingc)) pinzb_id,\n" + 
				"       decode(grouping(ysdw.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(kj.mingc)) jihkjb_id,\n" + 
				"       decode(grouping(ysdw.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.fahrq)) fahrq,\n" + 
				"       decode(grouping(ysdw.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.daohrq)) daohrq,\n" + 
				"       decode(grouping(ysdw.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', gethetysbh('"+ fahqssj +"', '"+ fahjzsj +"', "+ getJiesdw() +")) hetb_id,\n" + 
				"       to_number(null) as jieskd,\n" + 
				"       to_number(null) as bukyqyf,\n" + 
				"       sum(cp.maoz) maoz,\n" + 
				"       sum(cp.piz) piz,\n" + 
				"       sum(cp.biaoz) biaoz,\n" + 
				"       sum(cp.yingd) yingd,\n" + 
				"       sum(cp.yingk) yingk,\n" + 
				"       sum(cp.yuns) yuns,\n" + 
				"       sum(cp.koud) koud,\n" + 
				"       count(cp.id) ches\n" + 
				"  from fahb    fh,\n" + 
				"       gongysb gys,\n" + 
				"       meikxxb mk,\n" + 
				"       pinzb   pz,\n" + 
				"       chezxxb faz,\n" + 
				"       chezxxb daoz,\n" + 
				"       jihkjb  kj,\n" + 
				"       zhilb   zl,\n" + 
				"       chepb   cp,\n" + 
				"       danjcpb djcp,\n" + 
				"       yunsdwb ysdw\n" + 
				" where fh.fahrq >= to_date('"+ fahqssj +"', 'yyyy-mm-dd')\n" + 
				"   and fh.fahrq <= to_date('"+ fahjzsj +"', 'yyyy-mm-dd')\n" + 
				"   and fh.liucztb_id = 1\n" +
				"   and fh.diancxxb_id = "+ getJiesdw() +"\n" +
				"   and fh.gongysb_id = gys.id\n" + 
				"   and fh.meikxxb_id = mk.id\n" + 
				"   and fh.pinzb_id = pz.id\n" + 
				"   and fh.faz_id = faz.id\n" + 
				"   and fh.daoz_id = daoz.id\n" + 
				"   and fh.jihkjb_id = kj.id\n" + 
				"   and fh.zhilb_id = zl.id\n" + 
				"   and cp.fahb_id = fh.id\n" + 
				"   and djcp.chepb_id(+) = cp.id\n" + 
				"   and (djcp.yunfjsb_id = 0 or djcp.yunfjsb_id is null)\n" + 
				"   and cp.yunsdwb_id = ysdw.id\n" + 
				" group by rollup(ysdw.mingc, mk.mingc, fh.id)";

		} else {
			
			sql = 
				"select decode(grouping(fh.id), 1, '', '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√') as zhuangt,\n" +
				"       fh.id fahb_id,\n" + 
				"       /*grouping(gys.mingc) gmc, grouping(mk.mingc) km, grouping(fh.id) gid,*/\n" + 
				"       decode(grouping(gys.mingc), 1, '总计', gys.mingc) as gongysb_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc), 1, '合计', mk.mingc) as meikxxb_id,\n" + 
				"       decode(grouping(mk.mingc) + grouping(fh.id), 1, '小计', 2, '', max(pz.mingc)) as pinzb_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(faz.mingc)) as faz_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(daoz.mingc)) as daoz_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(kj.mingc)) as jihkjb_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.fahrq)) as fahrq,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.daohrq)) as daohrq,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', decode(max(ht.hetbh), '', gethetbh('"+ fahqssj +"', '"+ fahjzsj +"', "+ getJiesdw() +"), max(ht.hetbh))) as hetb_id,\n" + 
				"       to_number(null) as jieskd,\n" +
				"       to_number(null) as bukyqjk,\n" +
				"       sum(fh.maoz) maoz,\n" + 
				"       sum(fh.piz) piz,\n" + 
				"       sum(fh.jingz) jingz,\n" + 
				"       sum(fh.biaoz) biaoz,\n" + 
				"       sum(fh.yingd) yingd,\n" + 
				"       sum(fh.yingk) yingk,\n" + 
				"       sum(fh.yuns) yuns,\n" + 
				"       sum(fh.koud) koud,\n" + 
				"       sum(fh.ches) ches,\n" + 
				"       sum(fh.laimsl) laimsl,\n" + 
				"       round_new(sum(zl.qnet_ar * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 3) qnet_ar,\n" + 
				"       round_new(sum(zl.aar * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) aar,\n" + 
				"       round_new(sum(zl.ad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) ad,\n" + 
				"       round_new(sum(zl.vdaf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) vdaf,\n" + 
				"       round_new(sum(zl.mt * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) mt,\n" + 
				"       round_new(sum(zl.stad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) stad,\n" + 
				"       round_new(sum(zl.aad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) aad,\n" + 
				"       round_new(sum(zl.mad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) mad,\n" + 
				"       round_new(sum(zl.qbad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) qbad,\n" + 
				"       round_new(sum(zl.had * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) had,\n" + 
				"       round_new(sum(zl.vad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) vad,\n" + 
				"       round_new(sum(zl.fcad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) fcad,\n" + 
				"       round_new(sum(zl.std * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) std,\n" + 
				"       round_new(sum(zl.qgrad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) qgrad,\n" + 
				"       round_new(sum(zl.hdaf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) hdaf,\n" + 
				"       round_new(sum(zl.qgrad_daf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) qgrad_daf,\n" + 
				"       round_new(sum(zl.sdaf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) sdaf,\n" + 
				"       round_new(sum(zl.var * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) var,\n" + 
				"       round_new(sum(zl.t1 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t1,\n" + 
				"       round_new(sum(zl.t2 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t2,\n" + 
				"       round_new(sum(zl.t3 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t3,\n" + 
				"       round_new(sum(zl.t4 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t4\n" + 
				"  from fahb    fh,\n" + 
				"       gongysb gys,\n" + 
				"       meikxxb mk,\n" + 
				"       pinzb   pz,\n" + 
				"       chezxxb faz,\n" + 
				"       chezxxb daoz,\n" + 
				"       jihkjb  kj,\n" + 
				"       hetb    ht,\n" + 
				"       zhilb   zl\n" + 
				" where fh.fahrq >= to_date('"+ fahqssj +"', 'yyyy-mm-dd')\n" + 
				"   and fh.fahrq <= to_date('"+ fahjzsj +"', 'yyyy-mm-dd')\n" + 
				"   and fh.jiesb_id = 0\n" + 
				"   and fh.liucztb_id = 1\n" +
				"   and fh.gongysb_id = gys.id\n" + 
				"   and fh.meikxxb_id = mk.id\n" + 
				"   and fh.pinzb_id = pz.id\n" + 
				"   and fh.faz_id = faz.id\n" + 
				"   and fh.daoz_id = daoz.id\n" + 
				"   and fh.jihkjb_id = kj.id\n" + 
				"   and fh.hetb_id = ht.id(+)\n" + 
				"   and fh.zhilb_id = zl.id\n" + 
				" group by rollup (gys.mingc, mk.mingc, fh.id)";
		}
		
		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setDefaultsortable(false);
		
		egu.getColumn("zhuangt").setHeader("&nbsp;&nbsp;状态");
		egu.getColumn("zhuangt").setWidth(50);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").setHeader("FAHB_ID");
		egu.getColumn("fahb_id").setEditor(null);
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			egu.getColumn("yunsdwb_id").setHeader("运输单位");
			egu.getColumn("yunsdwb_id").setEditor(null);
			egu.getColumn("yunsdwb_id").setEditor(new ComboBox());
			egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select ys.id, ys.mingc from yunsdwb ys where ys.diancxxb_id = "+ getJiesdw() +" order by ys.mingc"));
			egu.getColumn("yunsdwb_id").setEditor(null);
		} else {
			egu.getColumn("gongysb_id").setHeader("供应商");
			egu.getColumn("gongysb_id").setEditor(null);
		}
		
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("pinzb_id").setEditor(null);
		
		if (getJieslx() == Locale.meikjs_feiylbb_id) {
			egu.getColumn("faz_id").setHeader("发站");
			egu.getColumn("faz_id").setWidth(90);
			egu.getColumn("faz_id").setEditor(null);
			egu.getColumn("daoz_id").setHeader("到站");
			egu.getColumn("daoz_id").setWidth(90);
			egu.getColumn("daoz_id").setEditor(null);
		}
		
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("hetb_id").setHeader("合同编号");
		
		egu.getColumn("jieskd").setHeader("结算扣吨");
		egu.getColumn("jieskd").setWidth(60);
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			egu.getColumn("bukyqyf").setHeader("补扣以前运费");
			egu.getColumn("bukyqyf").setWidth(90);
		} else {
			egu.getColumn("bukyqjk").setHeader("补扣以前价款");
			egu.getColumn("bukyqjk").setWidth(90);
		} 
		
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setWidth(70);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("yingd").setHeader("盈吨");
		egu.getColumn("yingd").setEditor(null);
		egu.getColumn("yingd").setWidth(50);
		egu.getColumn("yingk").setHeader("盈亏");
		egu.getColumn("yingk").setEditor(null);
		egu.getColumn("yingk").setWidth(50);
		egu.getColumn("yuns").setHeader("运损");
		egu.getColumn("yuns").setEditor(null);
		egu.getColumn("yuns").setWidth(50);
		egu.getColumn("koud").setHeader("扣吨");
		egu.getColumn("koud").setEditor(null);
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(50);
		
		if (getJieslx() == Locale.meikjs_feiylbb_id ) {
			egu.getColumn("jingz").setHeader("净重");
			egu.getColumn("jingz").setWidth(70);
			egu.getColumn("jingz").setEditor(null);
			egu.getColumn("laimsl").setHeader("来煤数量");
			egu.getColumn("laimsl").setEditor(null);
			egu.getColumn("qnet_ar").setHeader("QNET_AR");
			egu.getColumn("qnet_ar").setEditor(null);
			egu.getColumn("qnet_ar").setWidth(60);
			egu.getColumn("aar").setHeader("AAR");
			egu.getColumn("aar").setEditor(null);
			egu.getColumn("aar").setWidth(50);
			egu.getColumn("ad").setHeader("AD");
			egu.getColumn("ad").setEditor(null);
			egu.getColumn("ad").setWidth(50);
			egu.getColumn("vdaf").setHeader("VDAF");
			egu.getColumn("vdaf").setEditor(null);
			egu.getColumn("vdaf").setWidth(50);
			egu.getColumn("mt").setHeader("MT");
			egu.getColumn("mt").setEditor(null);
			egu.getColumn("mt").setWidth(50);
			egu.getColumn("stad").setHeader("STAD");
			egu.getColumn("stad").setEditor(null);
			egu.getColumn("stad").setWidth(50);
			egu.getColumn("aad").setHeader("AAD");
			egu.getColumn("aad").setEditor(null);
			egu.getColumn("aad").setWidth(50);
			egu.getColumn("mad").setHeader("MAD");
			egu.getColumn("mad").setEditor(null);
			egu.getColumn("mad").setWidth(50);
			egu.getColumn("qbad").setHeader("QBAD");
			egu.getColumn("qbad").setEditor(null);
			egu.getColumn("qbad").setWidth(50);
			egu.getColumn("had").setHeader("HAD");
			egu.getColumn("had").setEditor(null);
			egu.getColumn("had").setWidth(50);
			egu.getColumn("vad").setHeader("VAD");
			egu.getColumn("vad").setEditor(null);
			egu.getColumn("vad").setWidth(50);
			egu.getColumn("fcad").setHeader("FACD");
			egu.getColumn("fcad").setEditor(null);
			egu.getColumn("fcad").setWidth(50);
			egu.getColumn("fcad").setHidden(true);
			egu.getColumn("std").setHeader("STD");
			egu.getColumn("std").setEditor(null);
			egu.getColumn("std").setWidth(50);
			egu.getColumn("qgrad").setHeader("QGRAD");
			egu.getColumn("qgrad").setEditor(null);;
			egu.getColumn("qgrad").setWidth(50);
			egu.getColumn("qgrad").setHidden(true);
			egu.getColumn("hdaf").setHeader("HDAF");
			egu.getColumn("hdaf").setEditor(null);
			egu.getColumn("hdaf").setWidth(50);
			egu.getColumn("hdaf").setHidden(true);
			egu.getColumn("qgrad_daf").setHeader("QGRAD_DAF");
			egu.getColumn("qgrad_daf").setEditor(null);
			egu.getColumn("qgrad_daf").setWidth(50);
			egu.getColumn("qgrad_daf").setHidden(true);
			egu.getColumn("sdaf").setHeader("SDAF");
			egu.getColumn("sdaf").setEditor(null);
			egu.getColumn("sdaf").setWidth(50);
			egu.getColumn("sdaf").setHidden(true);
			egu.getColumn("var").setHeader("VAR");
			egu.getColumn("var").setEditor(null);
			egu.getColumn("var").setWidth(50);
			egu.getColumn("var").setHidden(true);
			egu.getColumn("t1").setHeader("T1");
			egu.getColumn("t1").setEditor(null);
			egu.getColumn("t1").setWidth(50);
			egu.getColumn("t1").setHidden(true);
			egu.getColumn("t2").setHeader("T2");
			egu.getColumn("t2").setEditor(null);
			egu.getColumn("t2").setWidth(50);
			egu.getColumn("t2").setHidden(true);
			egu.getColumn("t3").setHeader("T3");
			egu.getColumn("t3").setEditor(null);
			egu.getColumn("t3").setWidth(50);
			egu.getColumn("t3").setHidden(true);
			egu.getColumn("t4").setHeader("T4");
			egu.getColumn("t4").setEditor(null);
			egu.getColumn("t4").setWidth(50);
			egu.getColumn("t4").setHidden(true);
		}
		
		String het_sql = 
			"select ht.id, ht.hetbh\n" +
			"  from hetb ht,\n" + 
			"       (select distinct fh.gongysb_id\n" + 
			"          from fahb fh, gongysb gys\n" + 
			"         where fh.fahrq >= to_date('"+ fahqssj +"', 'yyyy-mm-dd')\n" + 
			"           and fh.fahrq <= to_date('"+ fahjzsj +"', 'yyyy-mm-dd')\n" + 
			"           and fh.jiesb_id = 0\n" + 
			"           and fh.gongysb_id = gys.id) gysb\n" + 
			" where to_date('"+ fahqssj +"', 'yyyy-mm-dd') >= ht.qisrq\n" + 
			"   and to_date('"+ fahjzsj +"', 'yyyy-mm-dd') <= ht.guoqrq\n" + 
			"   and ht.diancxxb_id = "+ getJiesdw() +"\n" + 
			"   and ht.gongysb_id = gysb.gongysb_id\n" + 
			" order by ht.hetbh";
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			het_sql = 
				"select distinct ht.id, ht.hetbh\n" +
				"  from hetys ht,\n" + 
				"       hetysjgb jg,\n" + 
				"       (select distinct fh.meikxxb_id\n" + 
				"          from fahb fh, meikxxb mk\n" + 
				"         where fh.fahrq >= to_date('"+ fahqssj +"', 'yyyy-mm-dd')\n" + 
				"           and fh.fahrq <= to_date('"+ fahjzsj +"', 'yyyy-mm-dd')\n" + 
				"           and fh.meikxxb_id = mk.id) mkxx\n" + 
				" where to_date('"+ fahqssj +"', 'yyyy-mm-dd') >= ht.qisrq\n" + 
				"   and to_date('"+ fahjzsj +"', 'yyyy-mm-dd') <= ht.guoqrq\n" + 
				"   and ht.diancxxb_id = "+ getJiesdw() +"\n" + 
				"   and ht.id = jg.hetys_id\n" + 
				"   and (jg.meikxxb_id = mkxx.meikxxb_id or jg.meikxxb_id = 0)\n" + 
				" order by ht.hetbh";
		}
		
		ComboBox hetbh = new ComboBox();
		hetbh.setListWidth(160);
		egu.getColumn("hetb_id").setEditor(hetbh);
		egu.getColumn("hetb_id").setComboEditor(egu.gridId, new IDropDownModel(het_sql));
		egu.getColumn("hetb_id").setWidth(130);
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		String yunfjs_info = "";	// 运费结算信息
//		String yunfjs_check = "";
		String meikjs_info = "";	// 补扣以前运费
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			
//			yunfjs_check = 
//				"            if(Mrcd[i].get('YUNSDWB_ID') == ''){\n" + 
//				"                Ext.MessageBox.alert('提示信息','字段 运输单位 不能为空');return;\n" + 
//				"            }\n";
			
			yunfjs_info = 
				"            + '<YUNSDWB_ID update=\"true\">' + Mrcd[i].get('YUNSDWB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNSDWB_ID>'\n" +
				"            + '<BUKYQYF update=\"true\">' + Mrcd[i].get('BUKYQYF')+ '</BUKYQYF>'\n";
			
		} else if (getJieslx() == Locale.meikjs_feiylbb_id) {
			
			meikjs_info = 
				"            + '<BUKYQJK update=\"true\">' + Mrcd[i].get('BUKYQJK')+ '</BUKYQJK>'\n";
		} else {	// 两票结算
			
//			yunfjs_check = 
//				"            if(Mrcd[i].get('YUNSDWB_ID') == ''){\n" + 
//				"                Ext.MessageBox.alert('提示信息','字段 运输单位 不能为空');return;\n" + 
//				"            }\n";
			
			yunfjs_info = 
				"            + '<YUNSDWB_ID update=\"true\">' + Mrcd[i].get('YUNSDWB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</YUNSDWB_ID>'\n" +
				"            + '<BUKYQYF update=\"true\">' + Mrcd[i].get('BUKYQYF')+ '</BUKYQYF>'\n";
			
			meikjs_info = 
				"            + '<BUKYQJK update=\"true\">' + Mrcd[i].get('BUKYQJK')+ '</BUKYQJK>'\n";
		}
		
		String condition = 
			"function(){\n" +
			"    var gridDivsave_history = '';\n" + 
			"    var Mrcd = gridDiv_ds.getRange(0, gridDiv_ds.getCount());\n" + 
			"    for(i = 0; i< Mrcd.length; i++){\n" + 
			"        if(typeof(gridDiv_save)=='function'){\n" + 
			"            var revalue = gridDiv_save(Mrcd[i]);\n" + 
			"            if(revalue=='return'){\n" + 
			"                return;\n" + 
			"            }else if(revalue=='continue'){\n" + 
			"                continue;\n" + 
			"            }\n" + 
			"        }\n" + 
			"        if (Mrcd[i].get('ZHUANGT') == '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√' && Mrcd[i].get('FAHB_ID') != '') {\n" + // yunfjs_check +
			"            gridDivsave_history += '<result>' + '<sign>U</sign>'\n" + yunfjs_info + meikjs_info +
			"            + '<FAHB_ID update=\"true\">' + Mrcd[i].get('FAHB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FAHB_ID>'\n" + 
			"            + '<HETB_ID update=\"true\">' + Mrcd[i].get('HETB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</HETB_ID>'\n" + 
			"            + '<JIESKD update=\"true\">' + Mrcd[i].get('JIESKD')+ '</JIESKD>'\n" + 
			"            + '</result>';\n" + 
			"        }\n" + 
			"    }\n" + 
			"    if(gridDiv_history=='' && gridDivsave_history==''){\n" + 
			"        Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n" + 
			"    }else{\n" + 
			"        var Cobj = document.getElementById('CHANGE');\n" + 
			"        Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" + 
			"        document.getElementById('SaveButton').click();\n" + 
			"        Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
			"    }\n" + 
			"}";
		GridButton savebtn = new GridButton("保存", condition, SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(savebtn);
		egu.addTbarText("-");
		
		String handler = 
			"function (){\n" +
			"    Ext.MessageBox.confirm('提示信息','是否确认重算？',\n" + 
			"        function(btn){\n" + 
			"            if(btn == 'yes'){\n" + 
			"                document.getElementById('ChongsButton').click();\n" + 
			"                Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...'," +
			"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
			"            };\n" + 
			"        }\n" + 
			"    );\n" + 
			"}";
		GridButton chongs = new GridButton("重算", handler, SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(chongs);
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("返回","function(){document.all.ReturnButton.click();}", SysConstant.Btn_Icon_Return));
		
		egu.addTbarText("-");
		Checkbox cbdhth = new Checkbox();
		cbdhth.setChecked(false);
		cbdhth.setId("duohth");
		egu.addToolbarItem(cbdhth.getScript());
		egu.addTbarText("多行替换");
		
		egu.addOtherScript( // 多选替换js
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"    if(!duohth.checked){\n" + 
				"        return;\n" + 
				"    }\n" + 
				"    if(gridDiv_ds.getAt(e.row).get('FAHB_ID') == ''){\n" + 
				"        return;\n" + 
				"    }\n" + 
				"    var xj = 0;\n" + 
				"    for (xj = e.row; xj < gridDiv_ds.getCount(); xj++) {\n" + 
				"        if (gridDiv_ds.getAt(xj).get('PINZB_ID') == '小计' ) {\n" + 
				"            break;\n" + 
				"        }\n" + 
				"    }\n" + 
				"    for(var i = e.row; i < xj; i++){\n" + 
				"        if(e.field=='HETB_ID'){\n" + 
				"            gridDiv_ds.getAt(i).set(e.field,e.value);\n" + 
				"        };\n" + 
				"    }\n" + 
				"});");
		
		egu.addOtherScript( // 页面自动计算js
				"gridDiv_grid.on('cellclick',function(own,row,col,e){\n" +
				"if (own.getColumnModel().getDataIndex(col) != 'ZHUANGT') {\n" +
				"    return;\n" + 
				"}\n" + 
				"//\t如果选择的是小计、合计、总计行，那么不进行任何计算直接退出该方法\n" + 
				"if (gridDiv_ds.getAt(row).get('FAHB_ID') == '') {\n" + 
				"    return;\n" + 
				"}\n" + 
				"\n" + 
				"if(duohth.checked) {\n" + 
				"    var xj = 0;\n" + 
				"    for (xj = row; xj < gridDiv_ds.getCount(); xj++) {\n" + 
				"        if (gridDiv_ds.getAt(xj).get('PINZB_ID') == '小计' ) {\n" + 
				"            break;\n" + 
				"        }\n" + 
				"    }\n" + 
				"    if(gridDiv_ds.getAt(row).get('ZHUANGT')=='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×'){\n" + 
				"        for(var i = row; i < xj; i++){\n" + 
				"            gridDiv_ds.getAt(i).set('ZHUANGT','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√');\n" + 
				"        }\n" + 
				"    }else if (gridDiv_ds.getAt(row).get('ZHUANGT')=='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√') {\n" + 
				"        for(var i = row; i < xj; i++){\n" + 
				"            gridDiv_ds.getAt(i).set('ZHUANGT','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×');\n" + 
				"        }\n" + 
				"    }\n" + 
				"} else {\n" + 
				"    if(gridDiv_ds.getAt(row).get('ZHUANGT')=='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×'){\n" + 
				"        gridDiv_ds.getAt(row).set('ZHUANGT','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√');\n" + 
				"    }else if(gridDiv_ds.getAt(row).get('ZHUANGT')!=''){\n" + 
				"        gridDiv_ds.getAt(row).set('ZHUANGT','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;×');\n" + 
				"    }\n" + 
				"}" +
				" //\t行被点击时计算小计\n" + 
				"    var jieskd = 0, bukyqjk = 0, bukyqyf = 0, maoz = 0, piz = 0, jingz = 0, biaoz = 0, yingd = 0, yingk = 0, yuns = 0, koud = 0, ches = 0, laimsl = 0,\n" + 
				"        qnet_ar = 0, aar = 0, ad = 0, vdaf = 0, mt = 0, stad = 0, aad = 0, mad = 0, qbad = 0, had = 0, vad = 0, fcad = 0,\n" + 
				"        std = 0, qgrad = 0, hdaf = 0, qgrad_daf = 0, sdaf = 0, var_zl = 0, t1 = 0, t2 = 0, t3 = 0, t4 = 0;\n" + 
				"    var pxj = 0;\n" + 
				"    for (pxj = row; pxj > 0; pxj --) {\n" + 
				"        if (gridDiv_ds.getAt(pxj).get('PINZB_ID') == '小计' ) {\n" + 
				"            break;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    var xj = 0;\n" + 
				"    for (xj = row; xj < gridDiv_ds.getCount(); xj++) {\n" + 
				"        if (gridDiv_ds.getAt(xj).get('PINZB_ID') == '小计' ) {\n" + 
				"            break;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    var recs = gridDiv_ds.getRange(pxj, xj);\n" + 
				"    for (var i = 0; i < recs.length; i ++) {\n" + 
				"        if (recs[i].get('FAHB_ID') != '' && recs[i].get('ZHUANGT') == '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√') {\n" +
				"            jieskd += eval(recs[i].get('JIESKD')||0);\n" + 
				"            bukyqjk += eval(recs[i].get('BUKYQJK')||0);\n" +
				"            bukyqyf += eval(recs[i].get('BUKYQYF')||0);\n" + 
				"            maoz += eval(recs[i].get('MAOZ')||0);\n" + 
				"            piz += eval(recs[i].get('PIZ')||0);\n" + 
				"            jingz += eval(recs[i].get('JINGZ')||0);\n" + 
				"            biaoz += eval(recs[i].get('BIAOZ')||0);\n" + 
				"            yingd += eval(recs[i].get('YINGD')||0);\n" + 
				"            yingk += eval(recs[i].get('YINGK')||0);\n" + 
				"            yuns += eval(recs[i].get('YUNS')||0);\n" + 
				"            koud += eval(recs[i].get('KOUD')||0);\n" + 
				"            ches += eval(recs[i].get('CHES')||0);\n" + 
				"            laimsl += eval(recs[i].get('LAIMSL')||0);\n" + 
				"            qnet_ar += eval(recs[i].get('QNET_AR')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            aar += eval(recs[i].get('AAR')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            ad += eval(recs[i].get('AD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            vdaf += eval(recs[i].get('VDAF')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            mt += eval(recs[i].get('MT')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            stad += eval(recs[i].get('STAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            aad += eval(recs[i].get('AAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            mad += eval(recs[i].get('MAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            qbad += eval(recs[i].get('QBAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            had += eval(recs[i].get('HAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            vad += eval(recs[i].get('VAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            fcad += eval(recs[i].get('FCAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            std += eval(recs[i].get('STD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            qgrad += eval(recs[i].get('QGRAD')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            hdaf += eval(recs[i].get('HDAF')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            qgrad_daf += eval(recs[i].get('QGRAD_DAF')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            sdaf += eval(recs[i].get('SDAF')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            var_zl += eval(recs[i].get('VAR')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            t1 += eval(recs[i].get('T1')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            t2 += eval(recs[i].get('T2')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            t3 += eval(recs[i].get('T3')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"            t4 += eval(recs[i].get('T4')||0) * eval(recs[i].get('JINGZ')||0);\n" + 
				"        }\n" + 
				"    }\n" + 
				"	 if (jingz == 0) {\n" +
				"    	 jingz = 1;\n" + 
				"	 }\n" +
				"    gridDiv_ds.getAt(xj).set('JIESKD',jieskd.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('BUKYQJK',bukyqjk.toFixed(2));\n" +
				"    gridDiv_ds.getAt(xj).set('BUKYQYF',bukyqyf.toFixed(2));\n" +
				"    gridDiv_ds.getAt(xj).set('MAOZ',maoz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('PIZ',piz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('JINGZ',jingz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('BIAOZ',biaoz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('YINGD',yingd.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('YINGK',yingk.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('YUNS',yuns.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('KOUD',koud.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('CHES',ches.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('LAIMSL',laimsl.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('QNET_AR',(qnet_ar / jingz).toFixed(3));\n" + 
				"    gridDiv_ds.getAt(xj).set('AAR',(aar / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('AD',(ad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('VDAF',(vdaf / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('MT',(mt / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('STAD',(stad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('AAD',(aad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('MAD',(mad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('QBAD',(qbad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('HAD',(had / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('VAD',(vad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('FCAD',(fcad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('STD',(std / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('QGRAD',(qgrad / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('HDAF',(hdaf / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('QGRAD_DAF',(qgrad_daf / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('SDAF',(sdaf / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('VAR',(var_zl / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('T1',(t1 / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('T2',(t2 / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('T3',(t3 / jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(xj).set('T4',(t4 / jingz).toFixed(2));\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"//\t行被点击时计算合计\n" + 
				"    var hj_jieskd = 0, hj_bukyqjk = 0, hj_bukyqyf = 0, hj_maoz = 0, hj_piz = 0, hj_jingz = 0, hj_biaoz = 0, hj_yingd = 0, hj_yingk = 0, hj_yuns = 0, hj_koud = 0, hj_ches = 0, hj_laimsl = 0,\n" + 
				"        hj_qnet_ar = 0, hj_aar = 0, hj_ad = 0, hj_vdaf = 0, hj_mt = 0, hj_stad = 0, hj_aad = 0, hj_mad = 0, hj_qbad = 0, hj_had = 0, hj_vad = 0,\n" + 
				"        hj_fcad = 0, hj_std = 0, hj_qgrad = 0, hj_hdaf = 0, hj_qgrad_daf = 0, hj_sdaf = 0, hj_var_zl = 0, hj_t1 = 0, hj_t2 = 0, hj_t3 = 0, hj_t4 = 0;\n" + 
				"    var phj = 0;\n" + 
				"    for (phj = row; phj > 0; phj --) {\n" + 
				"        if (gridDiv_ds.getAt(phj).get('MEIKXXB_ID') == '合计') {\n" + 
				"            break;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    var hj = 0;\n" + 
				"    for (hj = row; hj < gridDiv_ds.getCount(); hj ++) {\n" + 
				"        if (gridDiv_ds.getAt(hj).get('MEIKXXB_ID') == '合计') {\n" + 
				"            break;\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    var rechj = gridDiv_ds.getRange(phj, hj);\n" + 
				"    for (var i = 0; i < rechj.length; i ++) {\n" + 
				"        if (rechj[i].get('FAHB_ID') == '' && rechj[i].get('PINZB_ID') == '小计') {\n" +
				"            hj_jieskd += eval(rechj[i].get('JIESKD')||0);\n" + 
				"            hj_bukyqjk += eval(rechj[i].get('BUKYQJK')||0);\n" +
				"            hj_bukyqyf += eval(rechj[i].get('BUKYQYF')||0);\n" + 
				"            hj_maoz += eval(rechj[i].get('MAOZ')||0);\n" + 
				"            hj_piz += eval(rechj[i].get('PIZ')||0);\n" + 
				"            hj_jingz += eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_biaoz += eval(rechj[i].get('BIAOZ')||0);\n" + 
				"            hj_yingd += eval(rechj[i].get('YINGD')||0);\n" + 
				"            hj_yingk += eval(rechj[i].get('YINGK')||0);\n" + 
				"            hj_yuns += eval(rechj[i].get('YUNS')||0);\n" + 
				"            hj_koud += eval(rechj[i].get('KOUD')||0);\n" + 
				"            hj_ches += eval(rechj[i].get('CHES')||0);\n" + 
				"            hj_laimsl += eval(rechj[i].get('LAIMSL')||0);\n" + 
				"            hj_qnet_ar += eval(rechj[i].get('QNET_AR')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_aar += eval(rechj[i].get('AAR')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_ad += eval(rechj[i].get('AD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_vdaf += eval(rechj[i].get('VDAF')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_mt += eval(rechj[i].get('MT')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_stad += eval(rechj[i].get('STAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_aad += eval(rechj[i].get('AAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_mad += eval(rechj[i].get('MAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_qbad += eval(rechj[i].get('QBAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_had += eval(rechj[i].get('HAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_vad += eval(rechj[i].get('VAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_fcad += eval(rechj[i].get('FCAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_std += eval(rechj[i].get('STD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_qgrad += eval(rechj[i].get('QGRAD')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_hdaf += eval(rechj[i].get('HDAF')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_qgrad_daf += eval(rechj[i].get('QGRAD_DAF')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_sdaf += eval(rechj[i].get('SDAF')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_var_zl += eval(rechj[i].get('VAR')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_t1 += eval(rechj[i].get('T1')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_t2 += eval(rechj[i].get('T2')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_t3 += eval(rechj[i].get('T3')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"            hj_t4 += eval(rechj[i].get('T4')||0) * eval(rechj[i].get('JINGZ')||0);\n" + 
				"        }\n" + 
				"    }\n" +
				"    gridDiv_ds.getAt(hj).set('JIESKD',hj_jieskd.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('BUKYQJK',hj_bukyqjk.toFixed(2));\n" +
				"    gridDiv_ds.getAt(hj).set('BUKYQYF',hj_bukyqyf.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('MAOZ',hj_maoz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('PIZ',hj_piz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('JINGZ',hj_jingz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('BIAOZ',hj_biaoz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('YINGD',hj_yingd.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('YINGK',hj_yingk.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('YUNS',hj_yuns.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('KOUD',hj_koud.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('CHES',hj_ches.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('LAIMSL',hj_laimsl.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('QNET_AR',(hj_qnet_ar / hj_jingz).toFixed(3));\n" + 
				"    gridDiv_ds.getAt(hj).set('AAR',(hj_aar / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('AD',(hj_ad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('VDAF',(hj_vdaf / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('MT',(hj_mt / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('STAD',(hj_stad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('AAD',(hj_aad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('MAD',(hj_mad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('QBAD',(hj_qbad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('HAD',(hj_had / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('VAD',(hj_vad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('FCAD',(hj_fcad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('STD',(hj_std / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('QGRAD',(hj_qgrad / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('HDAF',(hj_hdaf / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('QGRAD_DAF',(hj_qgrad_daf / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('SDAF',(hj_sdaf / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('VAR',(hj_var_zl / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('T1',(hj_t1 / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('T2',(hj_t2 / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('T3',(hj_t3 / hj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(hj).set('T4',(hj_t4 / hj_jingz).toFixed(2));\n" + 
				"\n" + 
				"\n" + 
				"//  行被点击时计算总计\n" + 
				"    var zongj_jieskd = 0, zongj_bukyqjk = 0, zongj_bukyqyf = 0, zongj_maoz = 0, zongj_piz = 0, zongj_jingz = 0, zongj_biaoz = 0, zongj_yingd = 0, zongj_yingk = 0, zongj_yuns = 0, zongj_koud = 0,\n" + 
				"        zongj_ches = 0, zongj_laimsl = 0, zongj_qnet_ar = 0, zongj_aar = 0, zongj_ad = 0, zongj_vdaf = 0, zongj_mt = 0, zongj_stad = 0,\n" + 
				"        zongj_aad = 0, zongj_mad = 0, zongj_qbad = 0, zongj_had = 0, zongj_vad = 0, zongj_fcad = 0, zongj_std = 0, zongj_qgrad = 0,\n" + 
				"        zongj_hdaf = 0, zongj_qgrad_daf = 0, zongj_sdaf = 0, zongj_var_zl = 0, zongj_t1 = 0, zongj_t2 = 0, zongj_t3 = 0, zongj_t4 = 0;\n" + 
				"    var zongj_rec = gridDiv_ds.getRange(0, gridDiv_ds.getCount());\n" + 
				"    for (var i = 0; i < zongj_rec.length; i ++) {\n" + 
				"        if (zongj_rec[i].get('FAHB_ID') == '' && zongj_rec[i].get('MEIKXXB_ID') == '合计') {\n" +
				"            zongj_jieskd += eval(zongj_rec[i].get('JIESKD')||0);\n" + 
				"            zongj_bukyqjk += eval(zongj_rec[i].get('BUKYQJK')||0);\n" +
				"            zongj_bukyqyf += eval(zongj_rec[i].get('BUKYQYF')||0);\n" +
				"            zongj_maoz += eval(zongj_rec[i].get('MAOZ')||0);\n" + 
				"            zongj_piz += eval(zongj_rec[i].get('PIZ')||0);\n" + 
				"            zongj_jingz += eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_biaoz += eval(zongj_rec[i].get('BIAOZ')||0);\n" + 
				"            zongj_yingd += eval(zongj_rec[i].get('YINGD')||0);\n" + 
				"            zongj_yingk += eval(zongj_rec[i].get('YINGK')||0);\n" + 
				"            zongj_yuns += eval(zongj_rec[i].get('YUNS')||0);\n" + 
				"            zongj_koud += eval(zongj_rec[i].get('KOUD')||0);\n" + 
				"            zongj_ches += eval(zongj_rec[i].get('CHES')||0);\n" + 
				"            zongj_laimsl += eval(zongj_rec[i].get('LAIMSL')||0);\n" + 
				"            zongj_qnet_ar += eval(zongj_rec[i].get('QNET_AR')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_aar += eval(zongj_rec[i].get('AAR')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_ad += eval(zongj_rec[i].get('AD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_vdaf += eval(zongj_rec[i].get('VDAF')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_mt += eval(zongj_rec[i].get('MT')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_stad += eval(zongj_rec[i].get('STAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_aad += eval(zongj_rec[i].get('AAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_mad += eval(zongj_rec[i].get('MAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_qbad += eval(zongj_rec[i].get('QBAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_had += eval(zongj_rec[i].get('HAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_vad += eval(zongj_rec[i].get('VAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_fcad += eval(zongj_rec[i].get('FCAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_std += eval(zongj_rec[i].get('STD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_qgrad += eval(zongj_rec[i].get('QGRAD')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_hdaf += eval(zongj_rec[i].get('HDAF')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_qgrad_daf += eval(zongj_rec[i].get('QGRAD_DAF')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_sdaf += eval(zongj_rec[i].get('SDAF')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_var_zl += eval(zongj_rec[i].get('VAR')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_t1 += eval(zongj_rec[i].get('T1')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_t2 += eval(zongj_rec[i].get('T2')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_t3 += eval(zongj_rec[i].get('T3')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"            zongj_t4 += eval(zongj_rec[i].get('T4')||0) * eval(zongj_rec[i].get('JINGZ')||0);\n" + 
				"        }\n" + 
				"    }\n" +
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JIESKD',zongj_jieskd.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BUKYQJK',zongj_bukyqjk.toFixed(2));\n" +
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BUKYQYF',zongj_bukyqyf.toFixed(2));\n" +
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MAOZ',zongj_maoz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('PIZ',zongj_piz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',zongj_jingz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',zongj_biaoz.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGD',zongj_yingd.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',zongj_yingk.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',zongj_yuns.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('KOUD',zongj_koud.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',zongj_ches.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('LAIMSL',zongj_laimsl.toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNET_AR',(zongj_qnet_ar / zongj_jingz).toFixed(3));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('AAR',(zongj_aar / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('AD',(zongj_ad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('VDAF',(zongj_vdaf / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MT',(zongj_mt / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STAD',(zongj_stad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('AAD',(zongj_aad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MAD',(zongj_mad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QBAD',(zongj_qbad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('HAD',(zongj_had / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('VAD',(zongj_vad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('FCAD',(zongj_fcad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STD',(zongj_std / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QGRAD',(zongj_qgrad / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('HDAF',(zongj_hdaf / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QGRAD_DAF',(zongj_qgrad_daf / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('SDAF',(zongj_sdaf / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('VAR',(zongj_var_zl / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('T1',(zongj_t1 / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('T2',(zongj_t2 / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('T3',(zongj_t3 / zongj_jingz).toFixed(2));\n" + 
				"    gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('T4',(zongj_t4 / zongj_jingz).toFixed(2));\n" + 
				"});"
		);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为-1，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue4Combox(long value) {
		return value == -1 ? "default" : String.valueOf(value);
	}
	
	/**
	 * 如果在页面上取到的值为空或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRowNumIndex(visit.getString10());	// 将前个页面传过来的结算方案表id(jiesfab_id)保存到RowNumIndex变量中。
		}
		getSelectData();
	}

}