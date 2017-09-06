package com.zhiren.jt.zdt.shengcdy.diaoyxxcx;

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

/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Diaoyxxcx  extends BasePage implements PageValidateListener{
//	判断是否是集团用户
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
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
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
			if(getBaoblxValue().getValue().equals("装车预报")){
				getZhuangcyb();
			}else if(getBaoblxValue().getValue().equals("到站预报")){
				getDaozyb();
			}else if(getBaoblxValue().getValue().equals("取重排空")){
				getQuzpk();
			}
		}
	}

//	******************页面初始设置********************//
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
	private String RT_HET="zhuangcyb";
	private String mstrReportName="zhuangcyb";
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			if(getBaoblxValue()!=null){
				if(getBaoblxValue().getValue().equals("装车预报")){
					return getZhuangcyb();
				}else if(getBaoblxValue().getValue().equals("到站预报")){
					return getDaozyb();
				}else if(getBaoblxValue().getValue().equals("取重排空")){
					return getQuzpk();
				}
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
	 * 发电集团电煤信息日报表
	 * @author xzy
	 */
	private String getZhuangcyb(){//装车预报 leix：0
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//当前日期
		String riq1=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="装车预报";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		String danwmc="";//汇总名称
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			danwmc="集团公司合计";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			danwmc=getTreeDiancmc(this.getTreeid())+"合计";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//报表内容
		titlename=titlename+"";

		StringBuffer grouping_sql = new StringBuffer();
		StringBuffer where_sql = new StringBuffer();
		StringBuffer rollup_sql = new StringBuffer();
		StringBuffer having_sql = new StringBuffer();
		StringBuffer orderby_sql = new StringBuffer();

		StringBuffer strSQL = new StringBuffer();


		if (jib==1) {//选集团时刷新出所有的电厂
			grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc)+grouping(dc.fengs),2,'总计',1,dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

			where_sql.append(" ");
			rollup_sql.append(" group by rollup (dc.fengs,dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) ");
			having_sql.append(" having not (grouping(dc.mingc) || grouping(zb.yujddsj) )=1 ");
			orderby_sql.append("  order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc ,max(dc.xuh1) ");
		}else if(jib==2) {//选分公司的时候刷新出分公司下所有的电厂

			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();

			try{
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//燃料公司
					grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.quygs)+grouping(dc.fengs)+grouping(dc.mingc),3,'总计',\n");
					grouping_sql.append("2,dc.quygs,1,'&nbsp;&nbsp;'||dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.quygs,dc.fengs,dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) ");
					having_sql.append(" having not (grouping(dc.quygs) || grouping(zb.yujddsj) )=1\n");
					orderby_sql.append(" order by grouping(dc.quygs) desc,dc.quygs,grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,dc.mingc,max(dc.xuh1)\n ");
				}else{
					grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.fengs)+grouping(dc.mingc),2,'总计',\n");
					grouping_sql.append("1,dc.fengs,'&nbsp;&nbsp;'||dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.fengs,dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) ");
					having_sql.append(" having not (grouping(dc.fengs) || grouping(zb.yujddsj) )=1 \n");
					orderby_sql.append(" order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,dc.mingc,max(dc.xuh1)\n ");
				}
				rl.close();

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}

		}else{//选择电厂
			grouping_sql.append(" select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'总计',\n");
			grouping_sql.append("dc.mingc)) as danw,gy.mingc,cz.mingc,\n");

			where_sql.append(" and dc.id=").append(this.getTreeid()).append("  and riq="+riq+" \n");
			rollup_sql.append(" group by rollup (dc.mingc,gy.mingc,cz.mingc,zb.yujddsj) \n");
			having_sql.append(" having not (grouping(dc.mingc) || grouping(zb.yujddsj) )=1 \n");
			orderby_sql.append(" order by grouping(dc.mingc) desc,dc.mingc,max(dc.xuh1)\n ");
		}

		strSQL.append(grouping_sql.toString());
		strSQL.append("sum(zb.zuorzc) as zuorzc,to_char(zb.yujddsj,'yyyy-mm-dd') as yujddsj,sum(zb.jinrcr) as jinrcr,sum(zb.mingrqc) as mingrqc\n");//,sum(zb.mingrsd) as mingrsd


		strSQL.append("from zhuangcyb zb,gongysb gy,chezxxb cz, \n");


		strSQL.append("  ( select d.id,d.xuh as xuh1,d.mingc,d.jingjcml ,d.rijhm,dc.mingc as fengs,sf.id as quygsid,sf.mingc as quygs,d.xuh,dc.id as fuid,d.shangjgsid\n");
		strSQL.append(" from diancxxb d, diancxxb dc ,diancxxb sf\n");
		strSQL.append(" where d.jib = 3\n");
		strSQL.append(" and d.fuid=dc.id(+)  and d.shangjgsid=sf.id(+)) dc\n");

		strSQL.append(" where dc.id=zb.diancxxb_id(+) and zb.gongysb_id=gy.id(+) and zb.chezxxb_id=cz.id  and riq="+riq+"\n");

		strSQL.append(where_sql.toString());
		strSQL.append(rollup_sql.toString());
		strSQL.append(having_sql.toString());
		strSQL.append(orderby_sql.toString());





		ArrHeader =new String[1][7];
		ArrHeader[0]=new String[] {"单位","煤矿","发站","昨日实际<br>装车数","预计到达时间","今日承认车数","明日请车数"};//,"明日实到车数"

		ArrWidth=new int[] {150,150,100,80,80,80,80};


		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		//rt.setBody(new Table(rs,1, 0, 1));


		Table tb=new Table(rs,1, 0, 1);
		rt.setBody(tb);

		rt.setTitle(titlename, ArrWidth);

		rt.setDefaultTitle(1, 2, "制表单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2, riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(6, 2, "单位:吨",Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.ShowZero = false;

		if(jib==1){
			if(rt.body.getRows()>1){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
		}

		//页脚 

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3,2,"审核:",Table.ALIGN_CENTER);
		if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
			
			rt.setDefautlFooter(6,2, "制表:",Table.ALIGN_RIGHT);
			}else{
				
				rt.setDefautlFooter(6,2, "制表:"+((Visit) getPage().getVisit()).getDiancmc

	(),Table.ALIGN_RIGHT);
			}
//		rt.setDefautlFooter(6,2,"制表:",Table.ALIGN_RIGHT);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);



		//设置页数
//		rt.createDefautlFooter(ArrWidth);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}
//	******************************************************************************

//	发电集团电煤信息日报表
	private String getDaozyb(){//到站预报 leix：1
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//当前日期
		String riq1=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="到站预报";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		String danwmc="";//汇总名称
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			danwmc="集团公司合计";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			danwmc=getTreeDiancmc(this.getTreeid())+"合计";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=
				"select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc),lj.mingc,cz.mingc,tb.banjh,sum(tb.ches)\n" +
				"from tielyb tb,diancxxb dc,chezxxb cz,lujxxb lj,diancxxb gs\n" + 
				"where tb.diancxxb_id=dc.id and tb.chezxxb_id=cz.id  and gs.id=dc.fuid\n" + 
				"      and lj.id=cz.lujxxb_id and riq="+riq+strGongsID+"\n" + 
				"group by rollup(gs.mingc,(dc.mingc,lj.mingc,cz.mingc,tb.banjh))\n" + 
				"having  grouping(gs.mingc)=0\n" + 
				"order by grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc,dc.mingc";


		}else{
			strSQL=
				"select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc),lj.mingc,cz.mingc,tb.banjh,sum(tb.ches)\n" +
				"from tielyb tb,diancxxb dc,chezxxb cz,lujxxb lj,diancxxb gs\n" + 
				"where tb.diancxxb_id=dc.id and tb.chezxxb_id=cz.id  and gs.id=dc.fuid\n" + 
				"      and lj.id=cz.lujxxb_id and riq="+riq+strGongsID+"\n" + 
				"group by rollup(gs.mingc,(dc.mingc,lj.mingc,cz.mingc,tb.banjh))\n" + 
				"having  grouping(gs.mingc)=0\n" + 
				"order by grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc,dc.mingc";
		}

		ArrHeader =new String[1][5];
		ArrHeader[0]=new String[] {"单位","路局","发站","班计划","车数"};

		ArrWidth =new int[] {150,100,100,100,100};


		ResultSet rs = cn.getResultSet(strSQL);



		// 数据
		rt.setBody(new Table(rs,1, 0, 1));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, riq1, Table.ALIGN_RIGHT);
	//	rt.setDefaultTitle(5, 0, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(12);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		//页脚 

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(5,0,"制表:",Table.ALIGN_RIGHT);
//		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);



		//设置页数
//		rt.createDefautlFooter(ArrWidth);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

	}

	private String getQuzpk(){//取重排空 leix：2
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//当前日期
		String riq1=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="调运取重排空情况";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		String danwmc="";//汇总名称
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			danwmc="集团公司合计";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//报表内容
		titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=
				"select getLinkMingxTaiz(2,dc.id,dc.mingc) as mingc,q.quz6,q.paik6,q.daix6,q.quz18,q.paik18,q.daix18,q.CHANGCKC,\n" +//q.changtcs,
				"            q.changtcs,q.JIAOJZC,q.DAOZC,q.changtcs,q.changtsj\n" + 
				"       from quzpkb q,diancxxb dc\n" +  //,jiaojjlb j
				"       where q.diancxxb_id=dc.id and q.riq="+riq+strGongsID+"  ";//and j.diancxxb_id(+)=dc.id

		}else{
			strSQL=
				"select getLinkMingxTaiz(2,dc.id,dc.mingc) as mingc,q.quz6,q.paik6,q.daix6,q.quz18,q.paik18,q.daix18,q.CHANGCKC,\n" +//q.changtcs,
				"            q.changtcs,q.JIAOJZC,q.DAOZC,q.changtcs,q.changtsj\n" + 
				"       from quzpkb q,diancxxb dc\n" +  //,jiaojjlb j
				"       where q.diancxxb_id=dc.id and q.riq="+riq+strGongsID+"  "; //and j.diancxxb_id(+)=dc.id
		}


		int intZuor =DateUtil.getDay(getBeginriqDate());
		int intQianr=DateUtil.getDay(DateUtil.AddDate(getBeginriqDate(), -1, DateUtil.AddType_intDay));
		//chh 2008-12-16 将前日，当日替换为具体日期的天
		ArrHeader =new String[2][13];
		//ArrHeader[0]=new String[] {"电厂","(前日)18时---(当日)6时","(前日)18时---(当日)6时","(前日)18时---(当日)6时","(前日)18时---(当日)18时","(前日)18时---(当日)18时","(前日)18时---(当日)18时","交接记录","交接记录","交接记录","交接记录","厂停时间","厂停时间"};
		ArrHeader[0]=new String[] {"电厂","("+intQianr+"日)18时---("+intZuor+"日)6时","("+intQianr+"日)18时---("+intZuor+"日)6时","("+intQianr+"日)18时---("+intZuor+"日)6时","("+intQianr+"日)18时---("+intZuor+"日)18时","("+intQianr+"日)18时---("+intZuor+"日)18时","("+intQianr+"日)18时---("+intZuor+"日)18时","交接记录","交接记录","交接记录","交接记录","厂停时间","厂停时间"};
		ArrHeader[1]=new String[] {"电厂","取重","排空","待卸","取重","排空","待卸","厂存空车","厂存重车","交接站存","站存","厂停车数","平均时间"};

		ArrWidth =new int[] {120,50,50,50,50,50,50,50,50,50,50,50,50};


		ResultSet rs = cn.getResultSet(strSQL);




		// 数据
		rt.setBody(new Table(rs,2, 0, 1));

		rt.setTitle(titlename, ArrWidth);
//		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+getDiancName(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 4, riq1, Table.ALIGN_CENTER);
		rt.setDefaultTitle(12, 2, "单位:吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		//页脚 

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,3,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6,3,"审核:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(12,2,"制表:",Table.ALIGN_RIGHT);
    //	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);



		//设置页数
//		rt.createDefautlFooter(ArrWidth);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();

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
//	分公司下拉框
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
			fahdwList.add(new IDropDownBean(0,"装车预报"));
			fahdwList.add(new IDropDownBean(1,"到站预报"));
			fahdwList.add(new IDropDownBean(2,"取重排空"));

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