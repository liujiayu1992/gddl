package com.zhiren.jt.zdt.jiesgl.meijjk;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;


public class Jiesmjjkbreport  extends BasePage implements PageValidateListener{
	
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
	
	//开始日期
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
			this.getSelectData();
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}

		if(_fengschange){
			
			_fengschange=false;
			Refurbish();
		}
		getToolBars() ;
		Refurbish();
	}
//	得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				 biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return biaotmc;
		
	}
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
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
	private String getSelectData(){
		Visit visit = (Visit) getPage().getVisit();
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

		String strGongsID = "";
		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+") ";
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		danwmc=getTreeDiancmc(this.getTreeid());
		
		String strCondition="";
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jh.id=" +getJihkjDropDownValue().getId();
		}
		
	
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="结算煤价监控";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		//报表内容
			if(getJihkjDropDownValue().getId()==2){
				strSQL=
					"select dc.mingc,decode(grouping(gy.dqmc)+grouping(gy.mingc),2,'小计',1,'&nbsp;&nbsp;'||gy.dqmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||gy.mingc) as gymc,round(min(htj),2) as hetj,\n" +
					"       round((sum(js.meikje+js.jiessl)/sum(js.jiessl)),2) as meikj,\n" + 
					"       round(decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz)),2) as hetje,\n" + 
					"       round(sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl),2),\n" + 
					"       round(decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl)),2) as meikje_by,\n" + 
					"\n" + 
					"       round((decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz))-\n" + 
					"                 decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl))),2) as hetbmcj,\n" + 
					"\n" + 
					"       round(decode(decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz)),0,0,\n" + 
					"             (decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz))-\n" + 
					"                 decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl)))/\n" + 
					"                 decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz))*100) ,2) as hetbl,\n" + 
					"       round((decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl))-(sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl))),2) as meikcj,\n" + 
					"       round(decode(sum(sy.meikje_sy),0,0,((decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl))-\n" + 
					"       (sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl))))/(sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl)))*100,2) as meikbl\n" + 
					",decode(sum(js.jiessl),0,0,round(sum(js.jiessl*jszbj.jies*4.1814/1000)/sum(js.jiessl),2)) as jiesrz\n" +
					",max(jszbj.hetbz) as hetrz \n"+

					"from hetb ht,jiesb js,diancxxb dc,vwgongys gy,\n" + 

					"(select htzl.htid as htid,getHetjg(htzl.htid, htzl.shangx, htzl.xiax, htzl.tiaojbm) as htj\n" +
					"     from\n" + 
					"     (\n" + 
					"     select distinct hetb.id as htid, shangx, xiax, tj.bianm as tiaojbm\n" + 
					"        from hetzlb ,tiaojb tj,hetb\n" + 
					"        where  hetzlb.zhibb_id=2 and hetzlb.tiaojb_id=tj.id and hetb.id=hetzlb.hetb_id(+)\n" + 
					"      )htzl\n" + 
					"    ) htj,"+

					"      (select js.hetb_id as id,sum(js.jiessl) as jiessl,\n" + 
//					"              round(decode(sum(nvl(jszb.jies, 0)),0,0,\n" + 
//					"                    sum(js.jiessl*(js.meikje/js.jiessl) * 7000 / jszb.jies)/sum(js.jiessl)), 2) as meikje_sy\n" + 
//----------------------------判断除数为0------------------------
					"				round(decode(sum(nvl(js.jiessl, 0)),0,0,\n" +
					"                  sum(decode(jszb.jies,0,0,js.jiessl*(decode(js.jiessl,0,0,js.meikje/js.jiessl)) * 7000 / jszb.jies))/sum(js.jiessl)), 2) as meikje_sy"+
//---------------------------------------------------
					"        from jiesb js, jieszbsjb jszb, diancxxb dc\n" + 
					"        where js.jiesrq >= add_months(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'), -1)\n" + 
					"         and js.jiesrq <=\n" + 
					"             add_months(last_day(to_date('"+intyear+"-"+intMonth+"-1', 'yyyy-mm-dd')), -1)\n" + 
					"         and jszb.jiesdid = js.id\n" + 
					"         and js.diancxxb_id = dc.id\n" + 
					"         and jszb.zhibb_id = 2\n "+strGongsID + 
					"         group by js.hetb_id\n" + 
					"         ) sy,jieszbsjb jszbj,jihkjb jh \n" + 
					"where js.hetb_id=ht.id and js.diancxxb_id=dc.id and js.gongysb_id=gy.id and jszbj.jiesdid=js.id\n" + 
					"      and js.jiesrq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and js.jiesrq<=last_day(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))\n" + 
					" and jszbj.zhibb_id=2 and js.hetb_id=htj.htid and js.jihkjb_id=jh.id and ht.id=sy.id "+strGongsID+strCondition+"\n" + 
					"group by rollup(dc.mingc,gy.dqmc,(gy.mingc))\n" + 
					"having not grouping(dc.mingc)=1\n"+
					"order by grouping(dc.mingc)desc,dc.mingc,grouping(gy.dqmc) desc,gy.dqmc,grouping(gy.mingc) desc,gy.mingc,max(gy.xuh)\n" ;

			}else{
				strSQL=
					"select dc.mingc,decode(grouping(gy.dqmc),1,'小计','&nbsp;&nbsp;'||gy.dqmc) as gymc,round(min(htj),2) as hetj,\n" +
					"       round((sum(js.meikje+js.jiessl)/sum(js.jiessl)),2) as meikj,\n" + 
					"       round(decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz)),2) as hetje,\n" + 
					"       round(sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl),2),\n" + 
					"       round(decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl)),2) as meikje_by,\n" + 
					"\n" + 
					"       round((decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz))-\n" + 
					"                 decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl))),2) as hetbmcj,\n" + 
					"\n" + 
					"       round(decode(decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz)),0,0,\n" + 
					"             (decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz))-\n" + 
					"                 decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl)))/\n" + 
					"                 decode(sum(nvl(jszbj.hetbz,0)),0,0,sum(htj*29.271)/sum(jszbj.hetbz))*100) ,2) as hetbl,\n" + 
					"       round((decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl))-(sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl))),2) as meikcj,\n" + 
					"       round(decode(sum(sy.meikje_sy),0,0,((decode(sum(nvl(jszbj.jies,0)),0,0,sum(js.jiessl*(js.meikje/js.jiessl)*7000/jszbj.jies)/sum(js.jiessl))-\n" + 
					"       (sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl))))/(sum(sy.meikje_sy*sy.jiessl)/sum(sy.jiessl)))*100,2) as meikbl\n" + 
					",decode(sum(js.jiessl),0,0,round(sum(js.jiessl*jszbj.jies*4.1814/1000)/sum(js.jiessl),2)) as jiesrz\n" +
					",max(jszbj.hetbz) as hetrz \n"+

					"from hetb ht,jiesb js,diancxxb dc,vwgongys gy,\n" + 

					"(select htzl.htid as htid,getHetjg(htzl.htid, htzl.shangx, htzl.xiax, htzl.tiaojbm) as htj\n" +
					"     from\n" + 
					"     (\n" + 
					"     select distinct hetb.id as htid, shangx, xiax, tj.bianm as tiaojbm\n" + 
					"        from hetzlb ,tiaojb tj,hetb\n" + 
					"        where  hetzlb.zhibb_id=2 and hetzlb.tiaojb_id=tj.id and hetb.id=hetzlb.hetb_id(+)\n" + 
					"      )htzl\n" + 
					"    ) htj,"+

					"      (select js.hetb_id as id,sum(js.jiessl) as jiessl,\n" + 
//					"              round(decode(sum(nvl(jszb.jies, 0)),0,0,\n" + 
//					"                    sum(js.jiessl*(js.meikje/js.jiessl) * 7000 / jszb.jies)/sum(js.jiessl)), 2) as meikje_sy\n" + 
//----------------------------判断除数为0------------------------
					"				round(decode(sum(nvl(js.jiessl, 0)),0,0,\n" +
					"                  sum(decode(jszb.jies,0,0,js.jiessl*(decode(js.jiessl,0,0,js.meikje/js.jiessl)) * 7000 / jszb.jies))/sum(js.jiessl)), 2) as meikje_sy"+
//---------------------------------------------------
					"        from jiesb js, jieszbsjb jszb, diancxxb dc\n" + 
					"        where js.jiesrq >= add_months(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'), -1)\n" + 
					"         and js.jiesrq <=\n" + 
					"             add_months(last_day(to_date('"+intyear+"-"+intMonth+"-1', 'yyyy-mm-dd')), -1)\n" + 
					"         and jszb.jiesdid = js.id\n" + 
					"         and js.diancxxb_id = dc.id\n" + 
					"         and jszb.zhibb_id = 2\n "+strGongsID + 
					"         group by js.hetb_id\n" + 
					"         ) sy,jieszbsjb jszbj,jihkjb jh \n" + 
					"where js.hetb_id=ht.id and js.diancxxb_id=dc.id and js.gongysb_id=gy.id and jszbj.jiesdid=js.id\n" + 
					"      and js.jiesrq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and js.jiesrq<=last_day(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))\n" + 
					" and jszbj.zhibb_id=2 and js.hetb_id=htj.htid and js.jihkjb_id=jh.id and ht.id=sy.id "+strGongsID+strCondition+"\n" + 
					"group by rollup (dc.mingc,gy.dqmc) \n" + 
					"having not grouping(dc.mingc)=1 \n"+
					"order by dc.mingc,grouping(gy.dqmc) desc,max(gy.xuh)\n" ;
			}
				
			
//				直属分厂汇总	

				 ArrHeader =new String[2][13];
				 ArrHeader[0]=new String[] {"电厂","供货单位","天然煤价","天然煤价","标煤价","标煤价","标煤价","合同标煤价差","合同标煤价差","标煤价环比差","标煤价环比差","结算热值","合同热值"};
				 ArrHeader[1]=new String[] {"电厂","供货单位","合同","本月结","合同","上月结算","本月结算","差价","比率","差价","比率","结算热值","合同热值"};

				 ArrWidth =new int[] {150,150,60,60,60,60,60,60,60,60,60,60,60};

				 
				 iFixedRows=1;
				 iCol=10;
				 
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// 数据
//			rt.setBody(new Table(rs,3, 0, iFixedRows));
			Table tb=new Table(rs, 2, 0,1);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"年"+intMonth+"月"+titlename, ArrWidth);
//			rt.setDefaultTitle(1, 3, "填报单位:"+this.getDiancmc(), Table.ALIGN_LEFT);
//			rt.setDefaultTitle(7, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
//			rt.setDefaultTitle(11, 2, "单位:吨、%", Table.ALIGN_RIGHT);
//			rt.setDefaultTitle(14, 2, "cpi燃料管理05表", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = true;
			if(rt.body.getRows()>3){
				rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			}
			//页脚 
			
			rt.createDefautlFooter(ArrWidth);
			
			rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(7,3,"审核:",Table.ALIGN_LEFT);
			rt.setDefautlFooter(11,2,"制表:",Table.ALIGN_LEFT);
			tb.setColAlign(2, Table.ALIGN_LEFT);
			rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);

		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
//			System.out.println(rt.getAllPagesHtml());
			return rt.getAllPagesHtml();
	}
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
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return diancmc;
		
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
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

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
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
	}
	
//	年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * 月份
	 */
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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
			rs.close();
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
			rs.close();
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
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
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
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(60);
		tb1.addField(tbjhkj);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
		
	}
//	计划口径
	public IDropDownBean getJihkjDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getJihkjDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJihkjDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJihkjDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJihkjDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJihkjDropDownModels() {
		String sql = "select id,mingc\n" + "from jihkjb \n";
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql, "全部"));
		return;
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