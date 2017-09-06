package com.zhiren.dc.monthReport;

import org.apache.tapestry.html.BasePage;

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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterFac_dt;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * 作者：刘雨
 * 时间：2010-05-04
 * 描述：修改01表油数据修约小数位为保留3位
 */
public class BiaomdjfxReport extends BasePage implements PageValidateListener {

	private String _msg;

	private int _CurrentPage = -1;

	private int _AllPages = -1;

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	public boolean getRaw() {
		return true;
	}

	private boolean reportShowZero() {
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	// ***************设置消息框******************//
	// 页面判定方法
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

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// 得到单位全称
	public String getTianzdwQuanc(String dcid) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();
		ResultSetList rs = cn
				.getResultSetList(" select quanc from diancxxb where id="
						+ dcid);
		if (rs.next()) {
			_TianzdwQuanc = rs.getString("quanc");
		}
		rs.close();
		return _TianzdwQuanc;
	}

	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// 电厂信息表id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月1日" ;
		String treemc = getIDropDownDiancmc(lngDiancId);
		String sql = "";
		if(getJibbyDCID(cn,lngDiancId)==2||!getDiancg()){
			sql = 
				"select decode(grouping(dc.mingc) + grouping(bt.leix),\n" +
				"              2,\n" + 
				"              '"+treemc+"合计',\n" + 
				"              decode(grouping(bt.leix), '1', dc.mingc, bt.leix)) as leix,\n" + 
				"		'',\n" +
				"       decode(sum(rc.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benqlj * rc.benqljl) / sum(rc.benqljl), 2)) as benqlj,\n" + 
				"       decode(sum(rc.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangqlj * rc.shangqljl) / sum(rc.shangqljl),\n" + 
				"                        2)) as shangqlj,\n" + 
				"       decode(sum(rc.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benqlj * rc.benqljl) / sum(rc.benqljl), 2)) -\n" + 
				"       decode(sum(rc.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangqlj * rc.shangqljl) / sum(rc.shangqljl),\n" + 
				"                        2)) as tongb,\n" + 
				"       decode(sum(rc.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangnpj * rc.shangnpjl) / sum(rc.shangnpjl),\n" + 
				"                        2)) as shangnpj,\n" + 
				"       decode(sum(rc.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benqlj * rc.benqljl) / sum(rc.benqljl), 2)) -\n" + 
				"       decode(sum(rc.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangnpj * rc.shangnpjl) / sum(rc.shangnpjl),\n" + 
				"                        2)) as pingjb,\n" + 
				"       decode(sum(rc.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benywc * rc.benywcl) / sum(rc.benywcl), 2)) as benywc,\n" + 
				"       decode(sum(rc.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangywc * rc.shangywcl) / sum(rc.shangywcl),\n" + 
				"                        2)) as shangywc,\n" + 
				"       decode(sum(rc.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benywc * rc.benywcl) / sum(rc.benywcl), 2)) -\n" + 
				"       decode(sum(rc.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangywc * rc.shangywcl) / sum(rc.shangywcl),\n" + 
				"                        2)) as huanb,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) benqlj_rl,\n" + 
				"       decode(sum(rl.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangqlj * rl.shangqljl) / sum(rl.shangqljl),\n" + 
				"                        2)) as shangqlj_rl,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) -\n" + 
				"       decode(sum(rl.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangqlj * rl.shangqljl) / sum(rl.shangqljl),\n" + 
				"                        2)) as tongb_rl,\n" + 
				"       decode(sum(rl.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangnpj * rl.shangnpjl) / sum(rl.shangnpjl),\n" + 
				"                        2)) as shangnpj_rl,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) -\n" + 
				"       decode(sum(rl.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangnpj * rl.shangnpjl) / sum(rl.shangnpjl),\n" + 
				"                        2)) as pingjb_rl,\n" + 
				"       decode(sum(rl.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benywc * rl.benywcl) / sum(rl.benywcl), 2)) as benywc_rl,\n" + 
				"       decode(sum(rl.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangywc * rl.shangywcl) / sum(rl.shangywcl),\n" + 
				"                        2)) as shangywc_rl,\n" + 
				"       decode(sum(rl.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benywc * rl.benywcl) / sum(rl.benywcl), 2)) -\n" + 
				"       decode(sum(rl.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangywc * rl.shangywcl) / sum(rl.shangywcl),\n" + 
				"                        2)) as huanb_rl,\n" + 
				"       decode(sum(rl.bennysl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.bennys * rl.bennysl) / sum(rl.bennysl), 2)) as bennys_rl,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) -\n" + 
				"       decode(sum(rl.bennysl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.bennys * rl.bennysl) / sum(rl.bennysl), 2)) as yusb\n" + 
				"  from --表头\n" + 
				"       (select dc.id, lx.leix\n" + 
				"          from (select '燃煤' as leix\n" + 
				"                  from dual\n" + 
				"                union\n" + 
				"                select '燃油' as leix from dual) lx,\n" + 
				"               diancxxb dc\n" + 
				"         where dc.fuid = "+lngDiancId+") bt,\n" + 
				"       --入厂标煤单价\n" + 
				"       (select rc.diancxxb_id,\n" + 
				"               rc.leix,\n" + 
				"               rc.benqljl,\n" + 
				"               rc.benqlj,\n" + 
				"               rc.shangqljl,\n" + 
				"               rc.shangqlj,\n" + 
				"               rc.shangnpjl,\n" + 
				"               rc.shangnpj,\n" + 
				"               rc.benywcl,\n" + 
				"               rc.benywc,\n" + 
				"               rc.shangywcl,\n" + 
				"               rc.shangywc\n" + 
				"          from rucbmdjfxb rc, diancxxb dc\n" + 
				"         where rc.riq = to_date('"+strDate+"', 'yyyy-MM-dd')\n" + 
				"           and rc.diancxxb_id = dc.id\n" + 
				"           and dc.fuid = "+lngDiancId+") rc,\n" + 
				"       --入炉标煤单价\n" + 
				"       (select rl.diancxxb_id,\n" + 
				"               rl.leix,\n" + 
				"               rl.benqljl,\n" + 
				"               rl.benqlj,\n" + 
				"               rl.shangqljl,\n" + 
				"               rl.shangqlj,\n" + 
				"               rl.shangnpjl,\n" + 
				"               rl.shangnpj,\n" + 
				"               rl.benywcl,\n" + 
				"               rl.benywc,\n" + 
				"               rl.shangywcl,\n" + 
				"               rl.shangywc,\n" + 
				"               rl.bennysl,\n" + 
				"               rl.bennys\n" + 
				"          from rulbmdjfxb rl, diancxxb dc\n" + 
				"         where rl.riq = to_date('"+strDate+"', 'yyyy-MM-dd')\n" + 
				"           and rl.diancxxb_id = dc.id\n" + 
				"           and dc.fuid = "+lngDiancId+") rl,\n" + 
				"       diancxxb dc\n" + 
				" where bt.id = dc.id\n" + 
				"   and bt.id = rc.diancxxb_id(+)\n" + 
				"   and bt.id = rl.diancxxb_id(+)\n" + 
				"   and bt.leix = rc.leix(+)\n" + 
				"   and bt.leix = rl.leix(+)\n" + 
				" group by grouping sets(1,(dc.mingc),(bt.leix),(dc.mingc, bt.leix))\n" + 
				" order by grouping(dc.mingc) desc,\n" + 
				"          dc.mingc,\n" + 
				"          grouping(bt.leix) desc,\n" + 
				"          decode(bt.leix, '燃煤', 2, '燃油', 1, 0)";
		} else {
			sql = 
				"select decode(grouping(bt.leix), 1, dc.mingc, bt.leix) as leix,\n" +
				"		'',\n" +
				"       decode(sum(rc.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benqlj * rc.benqljl) / sum(rc.benqljl), 2)) as benqlj,\n" + 
				"       decode(sum(rc.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangqlj * rc.shangqljl) / sum(rc.shangqljl),\n" + 
				"                        2)) as shangqlj,\n" + 
				"       decode(sum(rc.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benqlj * rc.benqljl) / sum(rc.benqljl), 2)) -\n" + 
				"       decode(sum(rc.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangqlj * rc.shangqljl) / sum(rc.shangqljl),\n" + 
				"                        2)) as tongb,\n" + 
				"       decode(sum(rc.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangnpj * rc.shangnpjl) / sum(rc.shangnpjl),\n" + 
				"                        2)) as shangnpj,\n" + 
				"       decode(sum(rc.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benqlj * rc.benqljl) / sum(rc.benqljl), 2)) -\n" + 
				"       decode(sum(rc.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangnpj * rc.shangnpjl) / sum(rc.shangnpjl),\n" + 
				"                        2)) as pingjb,\n" + 
				"       decode(sum(rc.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benywc * rc.benywcl) / sum(rc.benywcl), 2)) as benywc,\n" + 
				"       decode(sum(rc.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangywc * rc.shangywcl) / sum(rc.shangywcl),\n" + 
				"                        2)) as shangywc,\n" + 
				"       decode(sum(rc.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.benywc * rc.benywcl) / sum(rc.benywcl), 2)) -\n" + 
				"       decode(sum(rc.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rc.shangywc * rc.shangywcl) / sum(rc.shangywcl),\n" + 
				"                        2)) as huanb,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) benqlj_rl,\n" + 
				"       decode(sum(rl.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangqlj * rl.shangqljl) / sum(rl.shangqljl),\n" + 
				"                        2)) as shangqlj_rl,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) -\n" + 
				"       decode(sum(rl.shangqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangqlj * rl.shangqljl) / sum(rl.shangqljl),\n" + 
				"                        2)) as tongb_rl,\n" + 
				"       decode(sum(rl.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangnpj * rl.shangnpjl) / sum(rl.shangnpjl),\n" + 
				"                        2)) as shangnpj_rl,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) -\n" + 
				"       decode(sum(rl.shangnpjl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangnpj * rl.shangnpjl) / sum(rl.shangnpjl),\n" + 
				"                        2)) as pingjb_rl,\n" + 
				"       decode(sum(rl.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benywc * rl.benywcl) / sum(rl.benywcl), 2)) as benywc_rl,\n" + 
				"       decode(sum(rl.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangywc * rl.shangywcl) / sum(rl.shangywcl),\n" + 
				"                        2)) as shangywc_rl,\n" + 
				"       decode(sum(rl.benywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benywc * rl.benywcl) / sum(rl.benywcl), 2)) -\n" + 
				"       decode(sum(rl.shangywcl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.shangywc * rl.shangywcl) / sum(rl.shangywcl),\n" + 
				"                        2)) as huanb_rl,\n" + 
				"       decode(sum(rl.bennysl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.bennys * rl.bennysl) / sum(rl.bennysl), 2)) as bennys_rl,\n" + 
				"       decode(sum(rl.benqljl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.benqlj * rl.benqljl) / sum(rl.benqljl), 2)) -\n" + 
				"       decode(sum(rl.bennysl),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"              round_new(sum(rl.bennys * rl.bennysl) / sum(rl.bennysl), 2)) as yusb\n" + 
				"from --表头\n" + 
				"(select dc.id, lx.leix\n" + 
				"          from (select '燃煤' as leix\n" + 
				"                  from dual\n" + 
				"                union\n" + 
				"                select '燃油' as leix from dual) lx,\n" + 
				"               diancxxb dc\n" + 
				"         where dc.id = "+lngDiancId+") bt,\n" + 
				"       --入厂标煤单价\n" + 
				"       (select rc.diancxxb_id,\n" + 
				"               rc.leix,\n" + 
				"               rc.benqljl,\n" + 
				"               rc.benqlj,\n" + 
				"               rc.shangqljl,\n" + 
				"               rc.shangqlj,\n" + 
				"               rc.shangnpjl,\n" + 
				"               rc.shangnpj,\n" + 
				"               rc.benywcl,\n" + 
				"               rc.benywc,\n" + 
				"               rc.shangywcl,\n" + 
				"               rc.shangywc\n" + 
				"          from rucbmdjfxb rc, diancxxb dc\n" + 
				"         where rc.riq = to_date('"+strDate+"', 'yyyy-MM-dd')\n" + 
				"           and rc.diancxxb_id = dc.id\n" + 
				"           and dc.id = "+lngDiancId+") rc,\n" + 
				"       --入炉标煤单价\n" + 
				"       (select rl.diancxxb_id,\n" + 
				"               rl.leix,\n" + 
				"               rl.benqljl,\n" + 
				"               rl.benqlj,\n" + 
				"               rl.shangqljl,\n" + 
				"               rl.shangqlj,\n" + 
				"               rl.shangnpjl,\n" + 
				"               rl.shangnpj,\n" + 
				"               rl.benywcl,\n" + 
				"               rl.benywc,\n" + 
				"               rl.shangywcl,\n" + 
				"               rl.shangywc,\n" + 
				"               rl.bennysl,\n" + 
				"               rl.bennys\n" + 
				"          from rulbmdjfxb rl, diancxxb dc\n" + 
				"         where rl.riq = to_date('"+strDate+"', 'yyyy-MM-dd')\n" + 
				"           and rl.diancxxb_id = dc.id\n" + 
				"           and dc.id = "+lngDiancId+") rl,\n" + 
				"       diancxxb dc\n" + 
				" where bt.id = dc.id\n" + 
				"   and bt.id = rc.diancxxb_id(+)\n" + 
				"   and bt.id = rl.diancxxb_id(+)\n" + 
				"   and bt.leix = rc.leix(+)\n" + 
				"   and bt.leix = rl.leix(+)\n" + 
				" group by rollup(dc.mingc, bt.leix)\n" + 
				" having not grouping(dc.mingc)+grouping(bt.leix)=2\n" + 
				" order by grouping(bt.leix) desc,\n" + 
				"          decode(bt.leix, '燃煤', 1, '燃油', 2, 3)";
		}
		sbsql.append(sql);
		ResultSetList rsl = cn.getResultSetList(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][]=new String[3][20];
		ArrHeader[0]=new String[] {"单位","行                次","入厂标煤单价分析","入厂标煤单价分析","入厂标煤单价分析","入厂标煤单价分析","入厂标煤单价分析","入厂标煤单价分析","入厂标煤单价分析","入厂标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析","入炉标煤单价分析"};
		ArrHeader[1]=new String[] {"单位","行                次","同比分析","同比分析","同比分析","与09年平均比分析","与09年平均比分析","环比分析","环比分析","环比分析","同比分析","同比分析","同比分析","与09年平均比分析","与09年平均比分析","环比分析","环比分析","环比分析","预算比分析","预算比分析"};
		ArrHeader[2]=new String[] {"单位","行                次","本期累计完成","上期累计完成","升高或降低","09年平均完成","升高或降低","本月完成","上月完成","升高或降低","本期累计完成","上期累计完成","升高或降低","09年平均完成","升高或降低","本月完成","上月完成","升高或降低","10年预算","升高或降低"};

		int ArrWidth[]=new int[] {100,20,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50};
		// 设置页标题
		rt.setTitle("中国大唐集团公司标煤单价分析表", ArrWidth);
		rt.setDefaultTitle(1, 5, "编制单位:大唐陕西发电有限公司", Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 2, strMonth, Table.ALIGN_CENTER);
		rt.setDefaultTitle(19, 2, "单位：元/吨", Table.ALIGN_CENTER);	
		
		// 数据
		
		rt.setBody(new Table(rsl, 3, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setRowHeight(1, 35);
		rt.body.setRowHeight(2, 35);
		rt.body.setRowHeight(3, 35);
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(4);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);

		int n=1;
		for(int i=4;i<=rsl.getRows()+3;i++){
			rt.body.setCellValue(i, 2, n+"");
			n++;
		}
		// 页脚
		// rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(6,1,"制表:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(10,1,"审核:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private void ArrWidth(int i) {
		// TODO 自动生成方法存根

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
			getSelectData();
		}

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}

	private void init() {
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel10(null);
		visit.setDropDownBean10(null);
		visit.setProSelectionModel3(null);
		visit.setDropDownBean3(null);
		visit.setDefaultTree(null);
		setDiancmcModel(null);
		setTreeid(visit.getDiancxxb_id() + "");
		paperStyle();
		getSelectData();
	}

	private boolean getDiancg() {
		JDBCcon con = new JDBCcon();
		String sqlq = "select id from diancxxb where fuid in(select id from diancxxb where id="
				+ getTreeid() + " and jib=3)";
		ResultSetList rs = con.getResultSetList(sqlq);
		if (rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		JDBCcon con = new JDBCcon();
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
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

	private int paperStyle;

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
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

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

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='纸张类型' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
	}

	public int getJibbyDCID(JDBCcon con, String dcid) {
		int jib = 3;
		ResultSetList rsl = con
				.getResultSetList("select jib from diancxxb where id =" + dcid);
		if (rsl.next()) {
			jib = rsl.getInt("jib");
		}
		rsl.close();
		return jib;
	}
	
//	 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
}