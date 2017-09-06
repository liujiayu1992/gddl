package com.zhiren.dc.huaygl.huaybb.baob;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Changkhyzbdb extends BasePage {
private static final String BAOBPZB_GUANJZ = "CHANGKDB";// baobpzb中对应的关键字

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
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


	private boolean blnIsBegin = false;
	
	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
	
		return getChangkhyzbdbreport();
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
	
	
//  获得选择的树节点的对应的电厂名称   
    private String getDcMingc(String id){ 
    	if(id == null || "".equals(id)){
    		return "";
    	}
		JDBCcon con=new JDBCcon();
		String mingc="";
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
//  获得选择的树节点的对应的供应商名称   
    private String[] getGys(String id){ 
    	String[] gys={"全部","-1"};
    	if(id==null || "".equals(id)){
    		return gys;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			gys[0]=rsl.getString("mingc");
			gys[1]=rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
//	取得日期参数SQL
    private String getDateParam(){
//		日期条件
		String rqsql = "";
		if(getRiq() == null || "".equals(getRiq())){
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(new Date())+"\n";
		}else{
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(getRiq())+"\n";
		}
		if(getAfter() == null || "".equals(getAfter())){
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(new Date())+"+1\n";
		}else{
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(getAfter())+"+1\n";
		}
		return rqsql;
    }
    
	public String getRptTitle() {
		String sb;
		sb= "厂矿化验指标对比查询";
		return sb;
	}
	private String getChangkhyzbdbreport() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();	
		String sql=

			"select decode(grouping(gongysb_id), 1, '总计', gongysb_id) gonghdw,\n" +
			"       decode(grouping(gongysb_id) + grouping(meikxxb_id),1, gongysb_id||'小计', meikxxb_id) miekdw,\n" + 
			"       decode(grouping(gongysb_id)+ grouping(meikxxb_id)+grouping(to_char(t.fahrq,'yyyy-mm-dd')),1,meikxxb_id||'小计',to_char(t.fahrq,'yyyy-mm-dd')) fahrq,\n"+
			"       sum(round_new(biaoz,"+visit.getShuldec()+")) as biaoz,\n" + 
			"       sum(round_new(laimsl,"+visit.getShuldec()+")) as laimsl,\n" + 
			"       ---------------------------------------------------------------------矿方\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qnet_ar_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) qnet_ar_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(aar_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) aar_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(ad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) ad_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(vdaf_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) vdaf_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(mt_k, 1) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        1)) mt_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(stad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) stad_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(aad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) aad_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(mad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) mad_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qbad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) qbad_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(had_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) had_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(fcad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) fcad_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(std_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) std_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qgrad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) qgrad_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(hdaf_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) hdaf_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qgrad_daf_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) qgrad_daf_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(sdaf_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) sdaf_k,\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(var_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                        2)) var_k,\n" + 
			"       ----------------------------------------------------------------------厂方\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qnet_ar_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) qnet_ar_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(aar_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) aar_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(ad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) ad_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(vdaf_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) vdaf_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(mt_c, 1) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        1)) mt_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(stad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) stad_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(aad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) aad_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(mad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) mad_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qbad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) qbad_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(had_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) had_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(fcad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) fcad_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(std_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) std_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qgrad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) qgrad_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(hdaf_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) hdaf_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(qgrad_daf_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) qgrad_daf_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(sdaf_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) sdaf_c,\n" + 
			"       decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(round_new(var_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                        sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                        2)) var_c,\n" + 
			"       -----------------------------------------------------------------------差值\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qnet_ar_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qnet_ar_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) qunet_ar_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(aar_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(aar_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) aar_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(ad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(ad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) ad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(vdaf_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(vdaf_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) vdaf_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(mt_c, 1) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         1)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(mt_k, 1) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         1))) mt_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(stad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(stad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) stad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(aad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(aad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) aad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(mad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(mad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) mad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qbad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qbad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) qbad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(had_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(had_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) had_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(vad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(vad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) vad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(fcad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(fcad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) fcad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(std_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(std_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) std_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qgrad_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qgrad_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) qgrad_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(hdaf_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(hdaf_k, 2) * round_new(biaoz,"+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz,"+visit.getShuldec()+")),\n" + 
			"                         2))) hdaf_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qgrad_daf_c, 2) *\n" + 
			"                             round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz,"+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(qgrad_daf_k, 2) * round_new(biaoz,"+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz,"+visit.getShuldec()+")),\n" + 
			"                         2))) qgrad_daf_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(sdaf_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(sdaf_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) sdaf_cz,\n" + 
			"       (decode(sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(var_c, 2) * round_new(laimsl, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(laimsl, "+visit.getShuldec()+")),\n" + 
			"                         2)) -\n" + 
			"       decode(sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"               0,\n" + 
			"               0,\n" + 
			"               round_new(sum(round_new(var_k, 2) * round_new(biaoz, "+visit.getShuldec()+")) /\n" + 
			"                         sum(round_new(biaoz, "+visit.getShuldec()+")),\n" + 
			"                         2))) var_cz\n" + 
			"  from (select distinct nvl(k.id, 0) as id,\n" + 
			"                        f.id as fahb_id,\n" + 
			"                        g.mingc as gongysb_id,\n" + 
			"                        m.mingc as meikxxb_id,\n" + 
			"                        f.fahrq as fahrq,\n" + 
			"                        f.daohrq as daohrq,\n" + 
			"                        f.biaoz,\n" + 
			"                        f.laimsl,\n" + 
			"                        nvl(k.qnet_ar, 0) as qnet_ar_k,\n" + 
			"                        nvl(k.aar, 0) as aar_k,\n" + 
			"                        nvl(k.ad, 0) as ad_k,\n" + 
			"                        nvl(k.vdaf, 0) as vdaf_k,\n" + 
			"                        nvl(k.mt, 0) as mt_k,\n" + 
			"                        nvl(k.stad, 0) as stad_k,\n" + 
			"                        nvl(k.aad, 0) as aad_k,\n" + 
			"                        nvl(k.mad, 0) as mad_k,\n" + 
			"                        nvl(k.qbad, 0) as qbad_k,\n" + 
			"                        nvl(k.had, 0) as had_k,\n" + 
			"                        nvl(k.vad, 0) as vad_k,\n" + 
			"                        nvl(k.fcad, 0) as fcad_k,\n" + 
			"                        nvl(k.std, 0) as std_k,\n" + 
			"                        nvl(k.qgrad, 0) as qgrad_k,\n" + 
			"                        nvl(k.hdaf, 0) as hdaf_k,\n" + 
			"                        nvl(k.qgrad_daf, 0) as qgrad_daf_k,\n" + 
			"                        nvl(k.sdaf, 0) as sdaf_k,\n" + 
			"                        nvl(k.var, 0) as var_k,\n" + 
			"                        nvl(z.qnet_ar, 0) as qnet_ar_c,\n" + 
			"                        nvl(z.aar, 0) as aar_c,\n" + 
			"                        nvl(z.ad, 0) as ad_c,\n" + 
			"                        nvl(z.vdaf, 0) as vdaf_c,\n" + 
			"                        nvl(z.mt, 0) as mt_c,\n" + 
			"                        nvl(z.stad, 0) as stad_c,\n" + 
			"                        nvl(z.aad, 0) as aad_c,\n" + 
			"                        nvl(z.mad, 0) as mad_c,\n" + 
			"                        nvl(z.qbad, 0) as qbad_c,\n" + 
			"                        nvl(z.had, 0) as had_c,\n" + 
			"                        nvl(z.vad, 0) as vad_c,\n" + 
			"                        nvl(z.fcad, 0) as fcad_c,\n" + 
			"                        nvl(z.std, 0) as std_c,\n" + 
			"                        nvl(z.qgrad, 0) as qgrad_c,\n" + 
			"                        nvl(z.hdaf, 0) as hdaf_c,\n" + 
			"                        nvl(z.qgrad_daf, 0) as qgrad_daf_c,\n" + 
			"                        nvl(z.sdaf, 0) as sdaf_c,\n" + 
			"                        nvl(z.var, 0) as var_c\n" + 
			"          from kuangfzlb k, gongysb g, meikxxb m, fahb f, zhillsb z\n" + 
			"         where f.kuangfzlb_id = k.id(+)\n" + 
			"           and f.zhilb_id = z.zhilb_id\n" + 
			"           and g.id = f.gongysb_id\n" + 
			"           and g.leix = 1\n" + 
			"           and f.meikxxb_id = m.id\n" + 
			"           and f.diancxxb_id = "+getTreeid_dc()+"\n"+ 
			            getDateParam()+") t\n" + 
			" group by rollup(gongysb_id, meikxxb_id, to_char(t.fahrq,'yyyy-mm-dd'))\n" + 
			" order by gongysb_id, meikxxb_id, to_char(t.fahrq,'yyyy-mm-dd')";
   
		ResultSetList rstmp = con.getResultSetList(sql);
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con.getResultSetList("select biaot from baobpzb where guanjz='"+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;
			ArrHeader = new String[1][57];
			ArrHeader[0]=new String[] {
					"供货单位","煤矿单位","发货日期","票重","来煤数量","收到基<br>低位发<br>热量(J/g)<br>Qnet,ar(矿方)","收到基<br>灰份<br>(%)Aar(矿方)","干燥基<br>灰分<br>(%)Ad(矿方)",
                    "干燥无<br>灰基挥<br>发分<br>(%)Vdaf(矿方)","全水分<br>(%)Mt(矿方)","空气干<br>燥基<br>全硫<br>(%)<br>St,ad(矿方)","空气<br>干燥<br>基灰<br>份<br>(%)Aad(矿方)",
                    "空气干<br>燥基水<br>分<br>(%)Mad(矿方)","弹筒发<br>热量<br>(J/g)<br>Qb,ad(矿方)","空气<br>干燥<br>基氢<br>(%)<br>Had(矿方)","固定炭<bt>Fcad(%)(矿方)",
                    "干燥基<br>全硫(%)<br>St,d(矿方)","空气<br>干燥<基<br>高位热<br>(%)Qgrad(矿方)","干燥无<br>灰基氢<br>(%)Hdaf(矿方)","干燥无<br>灰基空<br>气干燥<br><基高位热<br>(%)Qgrad_daf(矿方)",
                    "干燥无<br>灰基硫<br>(%)Sdaf(矿方)","收到基<br>挥发分<br>(%)Var(矿方)","收到基<br>低位发<br>热量(J/g)<br>Qnet,ar(厂方)","收到基<br>灰分<br>(%)Aar(厂方)",
                    "干燥基<br>灰分<br>(%)Ad(厂方)","干燥无<br>灰基挥<br>发分<br>(%)Vdaf(厂方)","全水分<br>(%)Mt(厂方)","空气干<br>燥基<br>全硫<br>(%)<br>St,ad(厂方)",
                    "空气<br>干燥<br>基灰<br>分<br>(%)Aad(厂方)","空气<br>干燥<br>基水<br>分<br>(%)Mad(厂方)","弹筒发<br>热量<br>(J/g)<br>Qb,ad(厂方)","空气<br>干燥<br>基氢<br>(%)<br>Had(厂方)",
                    "固定炭<br>Fcad(%)(厂方)","干燥基<br>全硫(%)<br>St,d(厂方)", "干燥无<br>灰基空<br>气干燥<br>基高位热<br>Qgrad_daf(厂方)","干燥<br>无灰基<br>氢<br>Hdaf(%)(厂方)",
                    "空气<br>干燥基<br>高位热<br>(%)Qgrad(厂方)","干燥无<br>灰基硫<br>(%)Sdaf(厂方)","收到基<br>挥发分<br>(%)Var(厂方)","收到基<br>低位发<br>热量(J/g)<br>Qnet,ar(差值)",
                    "收到基<br>灰分<br>(%)Aar(差值)","干燥基<br>灰分<br>(%)Ad(差值)","干燥无<br>灰基挥<br>发分<br>(%)Vdaf(差值)","全水分<br>(%)Mt(差值)","空气<br>干燥基<br>全硫<br>(%)<br>St,ad(差值)",
                    "空气<br>干燥<br>基灰<br>分<br>(%)Aad(差值)","空气<br>干燥<br>基水<br>分<br>(%)Mad(差值)", "弹筒发<br>热量<br>(J/g)<br>Qb,ad(差值)","空气<br>干燥<br>基氢<br>(%)<br>Had(差值)",
                    "收到基<br>灰分<br>(%)<br>Var(差值)","固定炭<br>Fcad(%)(差值)", "干燥基<br>全硫(%)<br>St,d(差值)","干燥无<br>灰基空<br>气干燥<br><基高位热<br>(%)Qgrad_daf(差值)",
                    "干燥<br>无灰基<br>氢<br>Hdaf(%)(差值)","空气<br>干燥基<br>高位热<br>(%)Qgrad(差值)","干燥无<br>灰基硫<br>(%)Sdaf(差值)","收到基<br>挥发分<br>(%)Var(差值)"};
			}
			
			 ArrWidth=new int[] {90,100,120,90,60,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,
					              45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,};
			
	
			rt.setTitle(getRptTitle(), ArrWidth);
			
			rt.title.setRowHeight(1, 40);
			rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
			rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

			rt.setDefaultTitle(1, 3, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(18, 5, "查询日期:" + getRiq() + "至" + getAfter(),Table.ALIGN_CENTER);
			rt.setDefaultTitle(37, 2, "单位:吨", Table.ALIGN_RIGHT);
			rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

			rt.setBody(new Table(rs, 1, 0, 3));
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.setPageRows(25);
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.mergeFixedCols();
//			rt.body.setColFormat(strFormat);
			for (int i = 1; i <= 57; i++) {
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}

			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期:"+ DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			rt.setDefautlFooter(18, 6, "审核:", Table.ALIGN_CENTER);
			rt.setDefautlFooter(37, 2, "制表:", Table.ALIGN_RIGHT);
			rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
			// 设置页数
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			con.Close();
			rt.body.setRowHeight(21);
			RPTInit.getInsertSql(visit.getDiancxxb_id(), sql, rt,getRptTitle(), "" +BAOBPZB_GUANJZ);
			return rt.getAllPagesHtml();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("发货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
			
//电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton dc = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		dc.setIcon("ext/resources/images/list-items.gif");
		dc.setCls("x-btn-icon");
		dc.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(dc);
		tb1.addText(new ToolbarText("-"));
//供应商
		DefaultTree gys = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		visit.setDefaultTree(gys);
		TextField tfgys = new TextField();
		tfgys.setId("gongysTree_text");
		tfgys.setWidth(100);
		tfgys.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addText(new ToolbarText("供应商"));
		tb1.addField(tfgys);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		tb.setIcon(SysConstant.Btn_Icon_Print);
		setToolbar(tb1);
	}
	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}
	
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//电厂Tree_begin
	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//电厂Tree_end
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}
	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			setTreeid_dc(visit.getDiancxxb_id() + "");
		}
		blnIsBegin = true;
		getSelectData();

	}

}
