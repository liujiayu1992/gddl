package com.zhiren.dc.hesgl.report;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;


public class Fenqzlb extends BasePage implements PageValidateListener {

	private String _msg;

	private int _CurrentPage = -1;

	private int _AllPages = -1;
	
	private int paperStyle = Report.PAPER_A4;
	
	private String Change = "1";	//结算方案多选结果值

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

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
	private String riq;
	public String getRiq(){
		
		return riq;
	}
	public void setRiq(String riq){
		this.riq=riq;
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

	public String _miaos;

	public String getMiaos() {
		return _miaos;
	}

	public void setMiaos(String miaos) {
		_miaos = miaos;
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
		String sql=	"SELECT quanc\n" +
					"\tFROM DIANCXXB\n" + 
					" WHERE ID IN (SELECT JIESDW_ID FROM JIESFAB WHERE ID IN ("+this.getChange()+"))";
	    StringBuffer danw=new StringBuffer("");
	    String title = "";
	    ResultSetList rs1=cn.getResultSetList(sql);
	    while(rs1.next()){
	    	
	    	danw.append(rs1.getString("QUANC")).append(",");
	    }
	    
	    if(danw.length()>0){
	    	
	    	danw.deleteCharAt(danw.length()-1);
	    }
	       
	    sql = 
				"SELECT TO_CHAR(min(FA.DAOHJZSJ), 'yyyy') || '年' || TO_CHAR(max(FA.DAOHJZSJ), 'MM') || '月' AS TITLE\n" +
				"\tFROM JIESFAB FA\n" + 
				" WHERE FA.ID in ("+this.getChange()+")";

	    rs1 = cn.getResultSetList(sql);
		if(rs1.next()){
			
			title = rs1.getString("TITLE");
		}
	    //时间设置
		String []sj = getRiq().split("-");
		String a = sj[0];
		String b = sj[1];
	    //在jiesfab 中取得电厂信息表id

		
		sql=


			" select\n" +
			"decode(a.meitly,'','煤矿属性',a.meitly) as meitly,\n" + 
			"       decode(a.mingc,'','运输方式',a.mingc) as mingc,\n" + 
			"       decode(a.meikdq,'','地区名称',a.meikdq) as meikdq,\n" + 
			"       decode(a.meikmc,'','煤矿名称',a.meikmc) as meikmc,\n" + 
			"       a.yunj as yunj,\n" + 
			"       a.hetl as hetl,\n" + 
			"       Round_new(a.qnet_ar1*4.1816/1000,2) as qnet_ar1,\n" + 
			"       Round_new(a.qnet_ar2*4.1816/1000,2) as qnet_ar2,\n" + 
			"       a.st1,\n" + 
			"       a.st2,\n" + 
			"       a.hetj,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.hansdj)/sum(a.jiessl),2)) as yunsj,\n" + 
			"       nvl(decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.hansdj)/sum(a.jiessl),2)),0)+nvl(sum(a.hetj),0) as xiaoj,\n" + 
			"       Round_new(decode(nvl(Round_new(a.qnet_ar1*4.1816/1000,2),0),0,0,(nvl(decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.hansdj)/sum(a.jiessl),2)),0)+nvl(sum(a.hetj),0))/Round_new(a.qnet_ar1*4.1816/1000,2))*29.271,2) as hbiaomdj,\n" + 
			"       a.beny,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.mt)/sum(a.jiessl),1)) as mt,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.aad)/sum(a.jiessl),2)) as aad,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.vdaf)/sum(a.jiessl),2)) as vdaf,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.std)/sum(a.jiessl),2)) as std,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.qnetar)/sum(a.jiessl),2)) as qnetar,\n" + 
			"       sum(a.jiessl) as jiessl,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.jiesdj)/sum(a.jiessl),2)) as jiesdj,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.hansdj)/sum(a.jiessl),2)) as hansdj,\n" + 
			"       decode(sum(nvl(a.jiessl,0)),0,0,Round_new(sum(a.jiessl*a.hejmj)/sum(a.jiessl),2)) as hejmj,\n" + 
			"       sum(a.hejje) as hejje,\n" + 
			"       decode(nvl(sum(a.jiessl),0),0,0,Round_new(sum(a.biaomdj*a.jiessl)/sum(a.jiessl),2)) as biaomdj      from\n" + 
			"       --本月\n" + 
			"(\n" + 
			" select decode(gb.meitly,'','a',gb.meitly) as meitly,\n" + 
			"        yf.mingc,\n" + 
			"        a.mingc as meikdq,\n" + 
			"        mk.mingc as meikmc,\n" + 
			"        to_number(substr(jy.yunj,INSTR(jy.yunj,':')+1)) as yunj,\n" + 
			"        h.hetl,\n" + 
			"        getstr(b.hetbz,'-',1) as qnet_ar1,\n" + 
			"        getstr(b.hetbz,'-',2) as qnet_ar2,\n" + 
			"        getstr(c.hetbz,'-',1) as st1,\n" + 
			"        getstr(c.hetbz,'-',2) as st2,\n" + 
			"        b.hetj,\n" + 
			"        '本月' as beny,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.mt*j.jiessl)/sum(j.jiessl)),2) as mt,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.aad*j.jiessl)/sum(j.jiessl)),2) as aad,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.vdaf*j.jiessl)/sum(j.jiessl)),2) as vdaf,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.std*j.jiessl)/sum(j.jiessl)),2) as std,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.qnetar*j.jiessl)/sum(j.jiessl)),2) as qnetar,\n" + 
			"        sum(j.jiessl) as jiessl,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.jiesdj*j.jiessl)/sum(j.jiessl)),2) as jiesdj,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(jy.hansdj*j.jiessl)/sum(j.jiessl)),2) as hansdj,\n" + 
			"        sum(nvl(b.jiesdj,0)+nvl(jy.hansdj,0)) as hejmj,\n" + 
			"        Round_new(sum(j.jiessl)*(sum(nvl(b.jiesdj,0)+nvl(jy.hansdj,0))),3) as hejje,\n" + 
			"        Round_new((Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.jiesdj*j.jiessl)/sum(j.jiessl)),2)+ Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(jy.hansdj*j.jiessl)/sum(j.jiessl)),2)\n" + 
			"        )*29.271/(getstr(b.hetbz,'-',2)*4.1816/1000),2) as biaomdj\n" + 
			"        from\n" + 
			"        (\n" + 
			"          select m.id,gb.mingc from gongysb gb ,meikxxb m\n" + 
			"          where gb.id = m.meikdq_id\n" + 
			"        ) a,\n" + 
			"        (\n" + 
			"         select * from danpcjsmxb dp where dp.zhibb_id =2\n" + 
			"        ) b,\n" + 
			"        (\n" + 
			"         select * from danpcjsmxb dp where dp.zhibb_id = 3\n" + 
			"        ) c, gongysb gb ,jiesb j ,yunsfsb yf,meikxxb mk,jiesyfb jy,jiesfab jf,hetslb h\n" + 
			"      where j.gongysb_id = gb.id\n" + 
			"       and j.yunsfsb_id = yf.id\n" + 
			"       and j.meikxxb_id = a.id\n" + 
			"       and j.meikxxb_id = mk.id\n" + 
			"       and j.id = b.jiesdid(+)\n" + 
			"       and j.id = c.jiesdid(+)\n" + 
			"       and j.id = jy.diancjsmkb_id\n" + 
			"       and jf.id = j.jiesfab_id\n" + 
			"       and j.hetb_id = h.hetb_id\n" + 
			"       and j.jiesfab_id in("+this.getChange()+")\n" + 
			"       and to_char(jf.daohjzsj,'yyyy')='"+a+"' and to_char(jf.daohjzsj,'MM')='"+b+"'\n" + 
			"       group by rollup (gb.meitly,yf.mingc,a.mingc,mk.mingc,b.hetbz,c.hetbz,b.hetj,jy.yunj,h.hetl)\n" + 
			"       having not(grouping(h.hetl)=1)\n" + 
			"   union --累计\n" + 
			" select decode(gb.meitly,'','a',gb.meitly) as meitly,\n" + 
			"        yf.mingc,\n" + 
			"        a.mingc,\n" + 
			"        mk.mingc,\n" + 
			"        0 yunj,\n" + 
			"        0 as hetl,\n" + 
			"        '' as qnet_ar1,\n" + 
			"        '' as qnet_ar2,\n" + 
			"        '' as st1,\n" + 
			"        '' as st2,\n" + 
			"        0  as hetj,\n" + 
			"        '累计' as beny,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.mt*j.jiessl)/sum(j.jiessl)),2) as mt,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.aad*j.jiessl)/sum(j.jiessl)),2) as aad,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.vdaf*j.jiessl)/sum(j.jiessl)),2) as vdaf,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.std*j.jiessl)/sum(j.jiessl)),2) as std,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.qnetar*j.jiessl)/sum(j.jiessl)),2) as qnetar,\n" + 
			"        sum(j.jiessl) as jiessl,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.jiesdj*j.jiessl)/sum(j.jiessl)),2) as jiesdj,\n" + 
			"        Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(jy.hansdj*j.jiessl)/sum(j.jiessl)),2) as hansdj,\n" + 
			"        sum(nvl(b.jiesdj,0)+nvl(jy.hansdj,0)) as hejmj,\n" + 
			"        Round_new(sum(j.jiessl)*(sum(nvl(b.jiesdj,0)+nvl(jy.hansdj,0))),3) as hejje,\n" + 
			"        Round_new((Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(b.jiesdj*j.jiessl)/sum(j.jiessl)),2)+ Round_new(decode(sum(nvl(j.jiessl,0)),0,0,sum(jy.hansdj*j.jiessl)/sum(j.jiessl)),2)\n" + 
			"        )*29.271/(getstr(b.hetbz,'-',2)*4.1816/1000),2) as biaomdj\n" + 
			"        from\n" + 
			"        (\n" + 
			"          select m.id,gb.mingc from gongysb gb ,meikxxb m\n" + 
			"          where gb.id = m.meikdq_id\n" + 
			"        ) a,\n" + 
			"        (\n" + 
			"         select * from danpcjsmxb dp where dp.zhibb_id =2\n" + 
			"        ) b,\n" + 
			"        (\n" + 
			"         select * from danpcjsmxb dp where dp.zhibb_id = 3\n" + 
			"        ) c, gongysb gb ,jiesb j ,yunsfsb yf,meikxxb mk,jiesyfb jy,jiesfab jf,hetslb h\n" + 
			"      where j.gongysb_id = gb.id\n" + 
			"       and j.yunsfsb_id = yf.id\n" + 
			"       and j.meikxxb_id = a.id\n" + 
			"       and j.meikxxb_id = mk.id\n" + 
			"       and j.id = b.jiesdid(+)\n" + 
			"       and j.id = c.jiesdid(+)\n" + 
			"       and j.id = jy.diancjsmkb_id\n" + 
			"       and jf.id = j.jiesfab_id\n" + 
			"       and j.hetb_id = h.hetb_id\n" + 
			"       and j.jiesfab_id in("+this.getChange()+")\n" + 
			"       and jf.daohjzsj>=to_date('"+a+"-01-01','yyyy-MM-dd')\n" + 
			"       and jf.daohjzsj<=to_date('"+this.getRiq()+"','yyyy-MM-dd')\n" + 
			"       group by rollup (gb.meitly,yf.mingc,a.mingc,mk.mingc,b.hetbz,c.hetbz,jy.yunj,b.hetj)\n" + 
			"       having not(grouping(b.hetj)=1)\n" + 
			"       ) a\n" + 
			"     group by rollup(a.beny,a.meitly,a.mingc,a.meikdq,a.meikmc,a.qnet_ar1,a.qnet_ar2,a.st1,a.st2,a.hetj,a.yunj,a.hetl)\n" + 
			"     having not((grouping(a.meikmc)+grouping(a.qnet_ar1)+grouping(a.qnet_ar2)+grouping(a.st1)+grouping(a.st2)+grouping(a.hetj)+grouping(a.yunj)+grouping(a.hetl)=7)\n" + 
			"     or (grouping(a.meikmc)+grouping(a.qnet_ar1)+grouping(a.qnet_ar2)+grouping(a.st1)+grouping(a.st2)+grouping(a.hetj)+grouping(a.yunj)+grouping(a.hetl)=6)\n" + 
			"     or (grouping(a.meikmc)+grouping(a.qnet_ar1)+grouping(a.qnet_ar2)+grouping(a.st1)+grouping(a.st2)+grouping(a.hetj)+grouping(a.yunj)+grouping(a.hetl)=5)\n" + 
			"     or (grouping(a.meikmc)+grouping(a.qnet_ar1)+grouping(a.qnet_ar2)+grouping(a.st1)+grouping(a.st2)+grouping(a.hetj)+grouping(a.yunj)+grouping(a.hetl)=4)\n" + 
			"     or (grouping(a.meikmc)+grouping(a.qnet_ar1)+grouping(a.qnet_ar2)+grouping(a.st1)+grouping(a.st2)+grouping(a.hetj)+grouping(a.yunj)+grouping(a.hetl)=3)\n" + 
			"     or (grouping(a.meikmc)+grouping(a.qnet_ar1)+grouping(a.qnet_ar2)+grouping(a.st1)+grouping(a.st2)+grouping(a.hetj)+grouping(a.yunj)+grouping(a.hetl)=2)\n" + 
			"     or (grouping(a.meikmc)+grouping(a.qnet_ar1)+grouping(a.qnet_ar2)+grouping(a.st1)+grouping(a.st2)+grouping(a.hetj)+grouping(a.yunj)+grouping(a.hetl)=1)\n" + 
			"     or grouping(a.beny)=1 or grouping(a.meikdq)+grouping(a.meikmc)=1)\n" + 
			"     order by grouping(a.meitly),a.meitly,grouping(a.mingc),a.mingc,grouping(a.meikdq),a.meikdq,grouping(a.meikmc),a.meikmc,grouping(a.beny),a.beny";



		ResultSetList rs = cn.getResultSetList(sql);
		Report rt = new Report();
		String ArrHeader[][] = new String[2][26];
		ArrHeader[0]=new String[] {"煤矿属性","运输方式","地区名称","煤矿名称","合同部分","合同部分",
				"合同部分","合同部分","合同部分","合同部分","合同部分","合同部分","合同部分","合同部分","当月及累计","厂化验部分",
				"厂化验部分","厂化验部分","厂化验部分","厂化验部分","本月拉运情况","本月拉运情况","本月拉运情况",
				"本月拉运情况","本月拉运情况","本月拉运情况"};
		ArrHeader[1]=new String[] {"煤矿属性","运输方式","地区名称","煤矿名称","运距<br>KM","合同煤量<br>吨",
				"热值<br>下限","热值<br>上限","硫值<br>下限","硫值<br>上限","煤价<br>(元/吨)","运价<br>(元/吨)","小计<br>(元/吨)",
				"标煤单价<br>(元/吨)","当月及累计","全水","灰份<br>Aad%","挥发份<br>Vdaf%","硫份<br>(St.d%)","发热量<br>(MJ/kg)","煤量<br>(吨)",
				"煤价<br>(元/吨)","运价<br>(元/吨)","合计煤价<br>(元/吨)","合计金额<br>(元)","标煤单价<br>(元/吨)"};
		int ArrWidth[] = new int[]{50,50,50,100,50,50,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,100,70};
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		rt.setTitle(a+"年"+b+"月"+"煤质分析情况表", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:"+danw, Table.ALIGN_LEFT);
		rt.setDefaultTitle(22, 4, "填报年月:"+a+"年"+b+"月", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 2, 0, 2));
		
        //	设置数据
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(40);
		//rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);
		rt.body.merge(1, 1, 2, 1);
		rt.body.merge(1, 2, 2, 2);
		rt.body.merge(1, 3, 2, 3);
		rt.body.merge(1, 4, 2, 4);
		rt.body.merge(1, 5, 2, 14);
		rt.body.merge(1, 15, 2, 15);
		rt.body.merge(1, 16, 1, 20);
		rt.body.merge(1, 21, 1, 26);
		for(int i=3;i<=rt.body.getRows();i++){
			if(rt.body.getCellValue(i, 15).equals("累计")){
				for(int j=5;j<=14;j++){
					rt.body.setCellValue(i, j, "0");
				}
				rt.body.merge(i, 5, i, 14);
			}
			if(rt.body.getCellValue(i, 4).equals("煤矿名称")){
				for(int j=4;j<=14;j++){
				rt.body.setCellValue(i, j, rt.body.getCellValue(i, 15)+rt.body.getCellValue(i, 3)+"地区合计");
				 }
				rt.body.merge(i, 4, i, 14);
				rt.body.setCellAlign(i, 4, Table.ALIGN_CENTER);
				}
			if(rt.body.getCellValue(i, 3).equals("地区名称")){
				for(int j=3;j<=14;j++){
					rt.body.setCellValue(i, j, rt.body.getCellValue(i, 15)+rt.body.getCellValue(i, 2)+"合计");
				}
				rt.body.merge(i, 3, i, 14);
				rt.body.setCellAlign(i, 3, Table.ALIGN_CENTER);
			}
			if(rt.body.getCellValue(i, 2).equals("运输方式")){
				for(int j=2;j<=14;j++){
					rt.body.setCellValue(i, j, rt.body.getCellValue(i, 15)+rt.body.getCellValue(i, 1)+"合计");
				}
				rt.body.merge(i, 2, i, 14);
				rt.body.setCellAlign(i, 2, Table.ALIGN_CENTER);
			}
			if(rt.body.getCellValue(i, 1).equals("煤矿属性")){
				for(int j=1;j<=14;j++){
					rt.body.setCellValue(i, j, rt.body.getCellValue(i, 15)+"总合计");
				}
				rt.body.merge(i, 1, i, 14);
				rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
			}
		
		}
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.mergeFixedCol(3);
		rt.body.mergeFixedCol(4);
		rt.body.setPageRows(40);
		rt.getPages();
		rt.createDefautlFooter(ArrWidth);	
		rt.setDefautlFooter(2, 2, "主管领导:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "审核:" , Table.ALIGN_LEFT);
		rt.setDefautlFooter(22, 2, "制表:", Table.ALIGN_LEFT);
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

	
	

	private boolean _SbClick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbClick = true;
	}

	private boolean _SqxgClick = false;

	public void SqxgButton(IRequestCycle cycle) {
		_SqxgClick = true;
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
		//setTreeid(visit.getDiancxxb_id() + "");

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			init();
		}
		getSelectData();
		if(visit.getboolean1()){
			
			visit.setboolean1(false);
			getIJiesfaModels();
		}
	}

	private void init() {
		
		setJiesfaValue(null);	//2
		setIJiesfaModel(null);	//2
		getIJiesfaModels();	//2
		//visit.setDefaultTree(null);
		((Visit) getPage().getVisit()).setboolean1(false);
		((Visit) getPage().getVisit()).setString3("");	//电厂树
		setDiancmcModel(null);
		paperStyle();
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//   时间下拉框
		tb1.addText(new ToolbarText("结算日期:"));
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("riq", "Form0");
		df.setId("riq");
	    tb1.addField(df);
	    tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("结算方案:"));
		LovComboBox jiesfa = new LovComboBox();
		jiesfa.setId("jiesfa");
//		ComboBox jiesfa = new ComboBox();
		jiesfa.setTransform("JiesfaDropDown");
		jiesfa.setWidth(160);
		jiesfa.setForceSelection(false);
		jiesfa.setListeners("select:function(e){document.getElementById('CHANGE').value = e.getValue();}");
		tb1.addField(jiesfa);
		tb1.addText(new ToolbarText("-"));
	
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

//	结算方案名称
	public IDropDownBean getJiesfaValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getIJiesfaModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getIJiesfaModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean2(new IDropDownBean(-1, "1"));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesfaValue(IDropDownBean Value) {
		
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean2()!= null) {
			
			id = ((Visit) getPage().getVisit()).getDropDownBean2().getId();
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIJiesfaModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIJiesfaModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIJiesfaModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIJiesfaModels() {

			String sql = 
//				"SELECT ID, BIANM\n" +
//				"\tFROM JIESFAB\n" + 
//				" WHERE SHIFJS = 1\n" + 
//				"\t AND JIESLX = "+Locale.guotyf_feiylbb_id+"\n" + 
////				"\t AND JIESDW_ID = "+this.getTreeid()+"\n" + 
//				" ORDER BY BIANM desc";

				"select id,j.bianm from jiesfab j\n" +
				"where j.daohjzsj= to_date('"+this.getRiq()+"','yyyy-MM-dd')\n"+
				"  and j.jieslx=2 ";

			
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
//	结算方案名称_end

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
		
		if(!((Visit) getPage().getVisit()).equals(treeid)){
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
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
}