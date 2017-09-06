package com.zhiren.jt.monthreport;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

/**
 * 刘雨
 * 2010-05-20
 */

public class Rucmtqkreport extends BasePage {
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
	
//	 年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
		
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean3()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean3(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel3(_value);
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
	
	private String REPORT_Rucmtqkreport="Rucmtqkreport";
	private String REPORT_Rulmtqkreport="Rulmtqkreport";
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
		isBegin = false;
		if(mstrReportName.equals(REPORT_Rucmtqkreport)){
			return getRucmcbreport();
		} else if (mstrReportName.equals(REPORT_Rulmtqkreport)){
			return getRulmcbreport();
		} else{
			return "无此报表";
		}
	}

	public String getRucmcbreport(){
		
		_CurrentPage=1;
		_AllPages=1;
		
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		String strdate = intyear+"-"+StrMonth+"-01";
		String titdate = intyear+"年"+StrMonth+"月";
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 2) {// 选分公司
			str = " and (d.id="+getTreeid()+" or d.fuid = "+ getTreeid() + ") ";
		} else if (treejib == 3) {// 选电厂
			str = " and d.id = " + getTreeid() + " ";
		}
	
		String sql = 
			"select bt.diancxxb_id,\n" + 
			"       bt.gongysb_id,\n" + 
			"       bt.fenx,\n" + 
			"       rc.laim,\n" + 
			"       rc.haom,\n" + 
			"       rc.kuc,\n" + 
			"       rc.meij,\n" + 
			"       rc.zengzs,\n" + 
			"       rc.yunzf,\n" + 
			"       rc.daocj,\n" + 
			"       rc.yisj,\n" + 
			"       rc.farl,\n" + 
			"       rc.biaomdj,\n" + 
			"       rc.yisbmdj,\n" + 
			"       rl.rez,\n" +
			"		rc.farl-rl.rez as rezc,\n" +
			"       rl.laiy,\n" + 
			"       rl.haoy,\n" + 
			"       rl.youkc\n" + 
			"  from \n" + 
//			表头
			"        (select d.mingc as diancxxb_id,\n" + 
			"                decode(grouping(g.mingc), 1, '合计', g.mingc) as gongysb_id,\n" + 
			"                fx.fenx\n" + 
			"           from (select distinct diancxxb_id, d.mingc, y.gongysb_id\n" + 
			"                   from yuetjkjb y, diancxxb d\n" + 
			"                  where y.riq = to_date('"+strdate+"', 'yyyy-MM-dd')\n" + 
			                       str+"\n" + 
			"                    and y.diancxxb_id = d.id) dc,\n" + 
			"                (select '当月' as fenx\n" + 
			"                   from dual\n" + 
			"                 union\n" + 
			"                 select '本期' as fenx\n" + 
			"                   from dual\n" + 
			"                 union\n" + 
			"                 select '同期' as fenx\n" + 
			"                   from dual\n" + 
			"                 union\n" + 
			"                 select '差值' as fenx from dual) fx,\n" + 
			"                gongysb g,\n" + 
			"                diancxxb d\n" + 
			"          where dc.gongysb_id = g.id\n" + 
			"            and dc.diancxxb_id = d.id\n" + 
			"          group by rollup(d.mingc, fx.fenx, g.mingc)\n" + 
			"         having not grouping(d.mingc) + grouping(fx.fenx) + grouping(g.mingc) > 1) bt,\n" + 
//			入厂
			"       (select d.mingc as diancxxb_id,\n" + 
			"               decode(grouping(g.mingc), 1, '合计', g.mingc) as gongysb_id,\n" + 
			"               r.fenx,\n" + 
			"               sum(laim) as laim,\n" + 
			"               sum(haom) as haom,\n" + 
			"               sum(kuc) as kuc,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * meij) / sum(laim), 2)) as meij,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * zengzs) / sum(laim), 2)) as zengzs,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * yunzf) / sum(laim), 2)) as yunzf,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * daocj) / sum(laim), 2)) as daocj,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * yisj) / sum(laim), 2)) as yisj,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * farl) / sum(laim), 2)) as farl,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * biaomdj) / sum(laim), 2)) as biaomdj,\n" + 
			"               decode(sum(laim),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(laim * yisbmdj) / sum(laim), 2)) as yisbmdj\n" + 
			"          from rucmtqkb r, diancxxb d, gongysb g\n" + 
			"         where r.diancxxb_id = d.id\n" + 
			"           and r.gongysb_id = g.id\n" + 
			"           and r.riq = to_date('"+strdate+"', 'yyyy-MM-dd')\n" + 
						str+"\n" + 
			"         group by rollup(d.mingc, r.fenx, g.mingc)\n" + 
			"        having not grouping(d.mingc) + grouping(r.fenx) + grouping(g.mingc) > 1) rc,\n" + 
//			入炉、油
			"       (select d.mingc as diancxxb_id,\n" + 
			"               '合计' as gongysb_id,\n" + 
			"               r.fenx,\n" + 
			"               r.rez,\n" + 
			"               r.laiy,\n" + 
			"               r.haoy,\n" + 
			"               r.youkc\n" + 
			"          from rucmtqkzb r, diancxxb d\n" + 
			"         where r.diancxxb_id = d.id\n" + 
			"           and riq = to_date('"+strdate+"', 'yyyy-MM-dd')\n" + 
						str+"\n" + 
			"        ) rl\n" + 
			" where bt.diancxxb_id = rc.diancxxb_id(+)\n" + 
			"   and bt.diancxxb_id = rl.diancxxb_id(+)\n" + 
			"   and bt.gongysb_id = rc.gongysb_id(+)\n" + 
			"   and bt.gongysb_id = rl.gongysb_id(+)\n" + 
			"   and bt.fenx = rc.fenx(+)\n" + 
			"   and bt.fenx = rl.fenx(+)\n" + 
			" order by bt.diancxxb_id,\n" + 
			"          decode(bt.gongysb_id, '合计', 2, 1),\n" + 
			"          bt.gongysb_id,\n" + 
			"          decode(bt.fenx, '当月', 1, '本期', 2, '同期', 3, '差值', 4, 5)";

		 String ArrHeader[][]=new String[1][19];
		 ArrHeader[0]=new String[] {"电厂","矿别","矿别","来煤<br>(吨)","耗煤<br>(吨)","库存<br>(吨)","煤价<br>(元/吨)","增值税<br>(元/吨)","运杂费<br>(元/吨)","到厂价<br>(元/吨)","剔税价<br>(元/吨)","发热量<br>(MJ/kg)","标煤单价<br>(元/吨)","剔说标煤单价<br>(元/吨)","入炉煤热值<br>(MJ/kg)","热值差<br>(MJ/kg)","来油<br>(吨)","耗油<br>(吨)","油库存<br>(吨)"};
		 int ArrWidth[]=new int[] {150,100,40,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50};

		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle("大唐黑龙江所属电厂"+titdate+"入厂煤炭情况表",ArrWidth);
		rt.setDefaultTitle(1, 3, "单位：大唐黑龙江发电有限公司燃料管理中心", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 3, titdate, Table.ALIGN_CENTER);
//		rt.setDefaultTitle(18,2,"单位:吨 元/吨",Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs,1,0,2));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
		rt.body.setRowHeight(1, 50);
		rt.body.setPageRows(24);
		rt.body.ShowZero = true;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
	//		页脚 
//		 rt.createDefautlFooter(ArrWidth);
			
			 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

public String getRulmcbreport(){
		
		_CurrentPage=1;
		_AllPages=1;
		
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		String strdate = intyear+"-"+StrMonth+"-01";
		String titdate = intyear+"年"+StrMonth+"月";
		Report rt=new Report();
		JDBCcon cn = new JDBCcon();

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 2) {// 选分公司
			str = " and (d.id="+getTreeid()+" or d.fuid = "+ getTreeid() + ") ";
		} else if (treejib == 3) {// 选电厂
			str = " and d.id = " + getTreeid() + " ";
		}
	
		String sql = 
			"select d.mingc as diancxxb_id,\n" +
			"       decode(r.fenx,'计划差值','差值',r.fenx) as fenx,\n" + 
			"       r.haoml,\n" + 
			"       r.rultrmj,\n" + 
			"       r.farl,\n" + 
			"       r.meizbmdj,\n" + 
			"       r.haoyl,\n" + 
			"       r.rulyj,\n" + 
			"       r.youzbmdj,\n" + 
			"       r.meiyhjbml,\n" + 
			"       r.meiyzhbmdj,\n" + 
			"       r.fadl,\n" + 
			"       r.fadbzmh,\n" + 
			"       r.gongdl,\n" + 
			"       r.gongdbzmh,\n" + 
			"       r.faddwrlcb,\n" + 
			"       r.gongrl,\n" + 
			"       r.gongrbzmh,\n" + 
			"       r.gongrdwrlcb\n" + 
			"from rulmtqkb r,diancxxb d\n" + 
			"where r.diancxxb_id = d.id\n" + 
			"  and r.riq = to_date('"+strdate+"','yyyy-MM-dd')\n" + 
				str+"\n" + 
			"order by r.diancxxb_id,decode(r.fenx,'当月',1,'本期',2,'同期',3,'差值',4,'计划',5,'计划差值',6,7)";

		 String ArrHeader[][]=new String[1][19];
		 ArrHeader[0]=new String[] {"电厂","电厂","耗煤量<br>(吨)","入炉天然<br>煤价<br>(元/吨)","发热量<br>(MJ/kg)","煤折标煤<br>单价<br>(元/吨)","耗油量<br>(吨)","入炉油价<br>(元/吨)","油折标煤<br>单价<br>(元/吨)","煤油合计<br>标煤量<br>(吨)","煤油综合<br>标煤单价<br>(元/吨)","发电量 万<br>(kw.h)","发电标准<br>煤耗<br>(g/kw.h)","供电量 万<br>(kw.h)","供电标准<br>煤耗<br>(g/kw.h)","发电单位<br>燃料成本<br>(元/kkw.h)","供热量<br>(GJ)","供热标准<br>煤耗<br>(kg/GJ)","供热单位<br>燃料成本<br>(元/TJ)"};
		 int ArrWidth[]=new int[] {150,40,50,50,50,50,50,50,50,50,60,50,50,50,50,60,50,50,50};

		ResultSet rs = cn.getResultSet(sql);
		
		rt.setTitle("大唐黑龙江所属电厂"+titdate+"入炉煤炭情况表",ArrWidth);
		rt.setDefaultTitle(1, 3, "单位：大唐黑龙江发电有限公司燃料管理中心", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 3, titdate, Table.ALIGN_CENTER);
//		rt.setDefaultTitle(18,2,"单位:吨 元/吨",Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs,1,0,1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		
		rt.body.setRowHeight(1, 50);
		rt.body.setPageRows(24);
		rt.body.ShowZero = true;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
	//		页脚 
//		 rt.createDefautlFooter(ArrWidth);
			
			 
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
			this.setTreeid(null);
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
					this.setTreeid(null);
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }
		getToolBars();
		isBegin=true;
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
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
//		tb1.addItem(tbLink);
		setToolbar(tb1);
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
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		
		_IDiancmcModel = new IDropDownModel(sql);
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

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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

