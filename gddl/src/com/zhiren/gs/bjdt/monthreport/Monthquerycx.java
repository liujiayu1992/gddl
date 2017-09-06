package com.zhiren.gs.bjdt.monthreport;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.filejx.FileDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Monthquerycx extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}

	
//	煤矿地区
	private boolean _meikdqmc = false;
    public IPropertySelectionModel getMeikdqmcModel() {
    	if(((Visit)getPage().getVisit()).getProSelectionModel3() == null){
    		getMeikdqmcModels();
    	}
    	return ((Visit)getPage().getVisit()).getProSelectionModel3();
    }

    public IDropDownBean getMeikdqmcValue() {
    	if(((Visit)getPage().getVisit()).getDropDownBean3() == null){
			((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getMeikdqmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
    }
    
    public void setMeikdqmcValue(IDropDownBean Value){
    	if (Value==null){
    		((Visit)getPage().getVisit()).setDropDownBean3(Value);
			return;
		}
    	if (((Visit)getPage().getVisit()).getDropDownBean3().getId()==Value.getId()) {
    		_meikdqmc = false;
		}else{
			_meikdqmc = true;
		}
    	((Visit)getPage().getVisit()).setDropDownBean3(Value);
    }
    
    public IPropertySelectionModel getMeikdqmcModels(){
        long  lngDiancxxbID= ((Visit) getPage().getVisit()).getDiancxxb_id();
        String sql="";
        
        if (((Visit) getPage().getVisit()).isDCUser()){
        	sql="select distinct gys.id,gys.mingc from diaor16bb d,gongysb gys where d.gongysb_id=gys.id and diancxxb_id=" + lngDiancxxbID;
        }else if(((Visit) getPage().getVisit()).isJTUser()){
        	sql="select distinct gys.id,gys.mingc from diaor16bb d ,gongysb gys where d.gongysb_id=gys.id  ";
        }else {
        	sql="select distinct gys.id,gys.mingc from diaor16bb d,diancxxb dc,gongysb gys where d.gongysb_id=gys.id and d.diancxxb_id=dc.id and dc.fuid=" + lngDiancxxbID;	        	
        }
        ((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"全部"));
        return ((Visit)getPage().getVisit()).getProSelectionModel3();
    }
    
    public void setMeikdqmcModel(IPropertySelectionModel _value) {
    	((Visit)getPage().getVisit()).setProSelectionModel3(_value);
    }
    
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}
	
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
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
	
//	***************设置消息框******************//
	private String _msg;

	public void setMsg(String _value) {
		 _msg = MainGlobal.getExtMessageBox(_value,false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private String RT_DR16="diaor16bb";//调燃16表
	private String RT_DR03="diaor03bb";//调燃03表
	private String RT_DR04="diaor04bb";//调燃04表
	private String RT_DR08="diaor08bb";//调燃08表
	private String RT_DR02="diaor02bb";//调燃02表
	private String RT_DR01="diaor01bb";//调燃01表
	private String RT_DR08_NEW="diaor08bb_new";//调燃08表NEW
	
	private String mstrReportName="";
	
	public String getTianzdwQuanc(){
		return getTianzdwQuanc(getDiancxxbId());
	}
	
	public long getDiancxxbId(){
		
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	public boolean isJTUser(){
		return ((Visit)getPage().getVisit()).isJTUser();
	}
	//得到单位全称
	public String getTianzdwQuanc(long gongsxxbID){
		String _TianzdwQuanc="";  
		JDBCcon cn = new JDBCcon();
		
		try {
			ResultSet rs=cn.getResultSet(" select quanc from diancxxb where id="+gongsxxbID);
			while (rs.next()){
				_TianzdwQuanc=rs.getString("quanc");
			}
			if(_TianzdwQuanc.equals("北京大唐燃料有限公司")){
				_TianzdwQuanc = "大唐国际发电股份有限公司燃料管理部";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	private boolean blnIsBegin = false;
//	private String leix="";
	public String getPrintTable(){
		Visit visit = (Visit)getPage().getVisit();
		setMsg(null);
		if(!blnIsBegin){
			return "";
		}
		blnIsBegin=false;
//        list.add(new IDropDownBean(1, "电生16-1表"));
//    	list.add(new IDropDownBean(2, "调燃03表"));
//    	list.add(new IDropDownBean(3, "调燃04表"));
//    	list.add(new IDropDownBean(4, "调燃01表"));
//    	list.add(new IDropDownBean(5, "调燃02表"));
//    	list.add(new IDropDownBean(6, "调燃08表"));
//    	list.add(new IDropDownBean(7, "调燃08-1表"));
    	
		if (getBBMCValue().getId()==1){

			return getDiaor16();
		}else if(getBBMCValue().getId()==4){

			return  getDiaor01();
		}else if(getBBMCValue().getId()==2){

			return  getDiaor03();
		}else if(getBBMCValue().getId()==3){

			return  getDiaor04();
		}else if(getBBMCValue().getId()==6){

			return getDiaor08();
			
		}else if(getBBMCValue().getId()==7){

			return getDiaor08_New();
		}
//		else if(getBBMCValue().getId()==5){
//			return getDiaor02();
//		}
		else{	
			return "无此报表";
		}
	}
	
	private String getDiancCondition(){
		JDBCcon cn = new JDBCcon();
		String diancxxb_id=getTreeid();
		String condition ="";
		ResultSet rs=cn.getResultSet("select jib,id,fuid from diancxxb where id=" +diancxxb_id);
		try {
			if (rs.next()){
				if( rs.getLong("jib")==SysConstant.JIB_JT){
					condition="";
				}else if(rs.getLong("jib")==SysConstant.JIB_GS){
					condition=" and dc.fuid=" +diancxxb_id;
				}else {
					condition=" and dc.id=" +diancxxb_id;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return condition;
	}
	
	private String getGongysCondition(){
		if (getMeikdqmcValue().getId()==-1){
			return "";
		}else{
			return " and dq.id=" +getMeikdqmcValue().getId();
		}
	}
	
	
	private String getDiaor01(){
		if(RiqMsg(RT_DR01)){
			return "数据错误！"; 
		}
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		long lngDiancId=getDiancxxbId();//电厂信息表id
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strDate2=getNianf2Value().getValue() +"-"+getYuef2Value().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月至"+getNianf2Value().getValue()+"年"+getYuef2Value().getValue()+"月";
		 
		String _Danwqc=getTianzdwQuanc();
		
		String condition = getDiancCondition();
		String fenz="";
		String neir="";
		if(getFenzValue().getValue().equals("当月")){
			fenz=" and  fenx='本月'\n";
		}else if(getFenzValue().getValue().equals("累计")){
			fenz=" and  fenx='累计'\n";
		}
		if(getBBLXValue().getValue().equals("汇总")){
			fenz=" and  fenx='本月'\n";
			neir=" '' as 日期, decode(fenx,'本月','汇总','汇总') as 当月及累计, ";
		}else{
			neir="to_char(dr.riq,'yyyy-mm') as 日期, fenx as 当月及累计, ";
		}
		
		sbsql.append("select decode(grouping(dc.mingc)+grouping(lx.mingc),2,'总计',1,lx.mingc||'合计',dc.mingc) as 电厂, \n");
		sbsql.append("       sum(fadsbrl) as 发电设备容量, \n" );
		sbsql.append(neir+" \n");
		sbsql.append("		 sum(meitsg) as 煤炭实供,  \n");
		sbsql.append("       sum(meithyhj) as 煤炭耗用合计, sum(meithyfd) as 煤炭耗用发电, sum(meithygr) as 煤炭耗用供热,  \n");
		sbsql.append("       sum(meithyqt) as 煤炭耗用其它, sum(meithysh) as 煤炭耗用损耗, decode(fenx,'累计','-',sum(meitkc)) as 煤炭库存,  \n");
		sbsql.append("       sum(shiysg) as 石油实供, sum(shiyhyhj) as 石油耗用合计, sum(shiyhyfd) as 石油耗用发电, sum(shiyhygr) as 石油耗用供热,  \n");
		sbsql.append("       sum(shiyhyqt) as 石油耗用其它, sum(shiyhysh) as 石油耗用损耗, decode(fenx,'累计','-',sum(shiykc)) as 石油库存,  \n");
		sbsql.append("       round(sum(fadl)) as 发电量,  sum(gongrl) as 供热量,  \n");
		sbsql.append("       case when sum(fadl) = 0 then 0 else round(sum(biaozmlfd) / sum(fadl) * 100, 0) end as 标准煤耗发电,  \n");
		sbsql.append("       case when sum(gongrl) = 0 then 0 else round(sum(biaozmlgr) / sum(gongrl) * 1000, 1) end as 标准煤耗供热,  \n");
		sbsql.append("       case when sum(fadl) = 0 then 0 else round((sum(shiyhyfd) * 2 + sum(meithyfd)) / sum(fadl) * 100, 0)  end as 天然煤耗发电,  \n");
		sbsql.append("       case when sum(gongrl) = 0 then 0 else round((sum(shiyhygr) * 2 + sum(meithygr)) / sum(gongrl) * 1000, 1) end as 天然煤耗供热,  \n");
		sbsql.append("       round(sum(biaozmlfd)) as 标准煤量发电, sum(biaozmlgr) as 标准煤量供热,  \n");
//		旧算法包含供热耗用
		sbsql.append("       fun_zonghrlfrl(sum(biaozmlfd), sum(biaozmlgr), sum(shiyhyfd), sum(shiyhygr), sum(meithyfd),sum(meithygr)) as 综合燃料,  \n");
		sbsql.append("       fun_cunrlfrl(sum(biaozmlfd), sum(biaozmlgr), sum(shiyhyfd), sum(shiyhygr), sum(meithyfd),sum(meithygr)) As 纯燃料  \n");
//		新算法不包含供热耗用
//		sbsql.append("       fun_zonghrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) as 综合燃料,  \n");
//		sbsql.append("       fun_cunrlfrl(sum(biaozmlfd), sum(shiyhyfd), sum(meithyfd)) As 纯燃料  \n");
		
		sbsql.append("  from diaor01bb dr,diancxxb dc,dianclbb lx,dianckjpxb px \n");
		if(RiqCheck()){
			sbsql.append("        where dr.riq=to_date('"+strDate2+"','yyyy-mm-dd') \n "+condition+"  \n");
			fenz=" and  fenx='累计'\n";
		}else{
			sbsql.append("        where dr.riq>=to_date('"+strDate+"','yyyy-mm-dd')  and dr.riq<to_date('"+strDate2+"','yyyy-mm-dd')+1 \n "+condition+"  \n");
		}
		sbsql.append("   and dc.id=dr.diancxxb_id and dc.dianclbb_id=lx.id and dc.id=px.diancxxb_id and px.kouj='月报' \n");
		sbsql.append(fenz);
		if(getBBLXValue().getValue().equals("汇总")){
			sbsql.append(" group by rollup ((fenx,lx.mingc,dc.mingc))   \n");
			sbsql.append(" order by grouping(lx.mingc) desc,max(lx.xuh),grouping(dc.mingc) desc,max(px.xuh),grouping(fenx) desc,fenx   \n");
		}else{
			sbsql.append(" group by rollup (fenx,dr.riq,(lx.mingc,dc.mingc))   \n");
			sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)  \n");
			sbsql.append(" order by grouping(lx.mingc) desc,max(lx.xuh),grouping(dc.mingc) desc,max(px.xuh),grouping(fenx) desc,fenx,dr.riq   \n");
		}
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		//定义表头数据
		 String ArrHeader[][]=new String[4][28];
		 ArrHeader[0]=new String[] {"单位名称","发电<br>设备<br>容量<br>(兆瓦)","日期","本月<br>或<br>累计","煤碳","煤碳","煤碳","煤碳","煤碳","煤碳","库存","石油","石油","石油","石油","石油","石油","库存","实际完成","实际完成","煤耗","煤耗","煤耗","煤耗","标准煤量","标准煤量","发热量","发热量"};
		 ArrHeader[1]=new String[] {"单位名称","发电<br>设备<br>容量<br>(兆瓦)","日期","本月<br>或<br>累计","实供<br>(吨)","耗用","耗用","耗用","耗用","耗用","库存<br>(吨)","实供<br>(吨)","耗用","耗用","耗用","耗用","耗用","库存<br>(吨)","发电<br>(万千瓦时)","供热<br>(吉焦)","标准煤耗","标准煤耗","天然煤耗","天然煤耗","发电(吨)","供热(吨)","综合燃料<br>(兆焦/千克)","纯燃料<br>(兆焦/千克)"};
		 ArrHeader[2]=new String[] {"单位名称","发电<br>设备<br>容量<br>(兆瓦)","日期","本月<br>或<br>累计","实供<br>(吨)","小计<br>(吨)","发电<br>(吨)","供热<br>(吨)","其他<br>(吨)","损耗<br>(吨)","库存<br>(吨)","实供<br>(吨)","小计<br>(吨)","发电<br>(吨)","供热<br>(吨)","其他<br>(吨)","损耗<br>(吨)","库存<br>(吨)","发电<br>(万千瓦时)","供热<br>(吉焦)","发电<br>(克/千瓦时)","供热<br>(千克/吉焦)","发电<br>(克/千瓦时)","供热<br>(千克/吉焦)","发电(吨)","供热(吨)","综合燃料<br>(兆焦/千克)","纯燃料<br>(兆焦/千克)"};
		 ArrHeader[3]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"};
//		列宽
		 int ArrWidth[]=new int[] {58,38,48,28,48,48,53,43,43,43,53,38,38,38,38,38,38,38,48,48,38,48,38,43,48,43,43,43};
		
		 //设置页标题
		rt.setTitle("生产用煤碳供应、耗用与结存汇总表("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,7,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
//		rt.setDefaultTitle(1,7,"填报单位:大唐国际发电股份有限公司燃料管理部",Table.ALIGN_LEFT);
		rt.setDefaultTitle(11,4,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃01表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,4,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.ShowZero=false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.setColAlign(2,Table.ALIGN_RIGHT);
		rt.body.setColFormat(5,"0");
		rt.body.setColFormat(6,"0");
		rt.body.setColFormat(7,"0");
		rt.body.setColFormat(8,"0");
		rt.body.setColFormat(9,"0");
		rt.body.setColFormat(10,"0");
		rt.body.setColFormat(11,"0");
		rt.body.setColFormat(12,"0");
		rt.body.setColFormat(13,"0");
		rt.body.setColFormat(14,"0");
		rt.body.setColFormat(15,"0");
		rt.body.setColFormat(16,"0");
		rt.body.setColFormat(17,"0");
		rt.body.setColFormat(18,"0");
		rt.body.setColFormat(22,"0.0");
		rt.body.setColFormat(24,"0.0");
		rt.body.setColFormat(27,"0.00");
		rt.body.setColFormat(28,"0.00");	
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(6,1,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(10,1,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();	
	}

	
	private String getDiaor03(){
		String _Danwqc=getTianzdwQuanc();
		if(RiqMsg(RT_DR03)){
			return "数据错误！"; 
		}
		String condition = getDiancCondition();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strDate2=getNianf2Value().getValue() +"-"+getYuef2Value().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月至"+getNianf2Value().getValue()+"年"+getYuef2Value().getValue()+"月";
		String strDanw="";
		String strMeik="";
		boolean blnIsFcFk=false;
		String fenz="";
		String neir="";
		if(getFenzValue().getValue().equals("当月")){
			fenz=" and  fenx='本月'\n";
		}else if(getFenzValue().getValue().equals("累计")){
			fenz=" and  fenx='累计'\n";
		}
		if(getBBLXValue().getValue().equals("汇总")){
			fenz=" and  fenx='本月'\n";
			neir=" '' as 日期, decode(fenx,'本月','汇总','汇总') as 当月及累计, ";
		}else{
			neir="to_char(dr.riq,'yyyy-mm') as 日期, fenx as 当月及累计, ";
		}
		
		sbsql.append(neir);
		sbsql.append("       sum(jincsl) as 进厂数量, sum(choucsl) as 抽查数量,  \n");
		sbsql.append("       decode(sum(jincsl),0,0,round(sum(choucsl)/sum(jincsl)*100,2)) as 占进厂煤 , \n");
		sbsql.append("       (case when sum(jincsl)>0 then round(sum(jincsl * guoh) /sum(jincsl),2) else 0 end) as 过衡,  \n");
		sbsql.append("       100-(case when sum(jincsl)>0 then round(sum(jincsl * guoh) /sum(jincsl),2) else 0 end) as 检尺,  \n");
		sbsql.append("       sum(yingdsl) as 盈吨数量,  sum(yingdzje) as 盈吨折金额,  \n");
		sbsql.append("       sum(kuid) as 亏吨,  sum(kuidzje) as 亏吨折金额,  \n");
		sbsql.append("       sum(suopje) as 索赔金额, \n");
		sbsql.append("     max(shuom) as 说明  \n");
		sbsql.append(" from diaor03bb dr,gongysb dq,diancxxb dc,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='月报')  px,dianclbb lb \n");
		if(RiqCheck()){
			sbsql.append("        where dr.riq=to_date('"+strDate2+"','yyyy-mm-dd') \n");
			fenz=" and  fenx='累计'\n";
		}else{
			sbsql.append("        where dr.riq>=to_date('"+strDate+"','yyyy-mm-dd')  and dr.riq<to_date('"+strDate2+"','yyyy-mm-dd')+1 \n");
		}
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(condition);
		sbsql.append("         \n").append(getGongysCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+)  \n");
		sbsql.append(" and lb.id(+)=dc.dianclbb_id\n");
		
		
		String strSqlBody=sbsql.toString();
		sbsql.setLength(0);
		
		if (getSelzhuangtValue().getValue().equals("分矿")) {
			strDanw="矿别";
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'总计',1,sf.quanc||'合计',dq.mingc)) as 煤矿名称,   \n");
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,sf.quanc,dq.mingc))    \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(sf.quanc,dq.mingc))    \n");
				sbsql.append(" having(grouping(dr.fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
		}else if (getSelzhuangtValue().getValue().equals("分厂")){
			strDanw="单位";
			sbsql.append(" select  (decode(grouping(dc.mingc)+grouping(dc.dianclbb_id),2,'总计',1,max(lb.mingc)||'合计',dc.mingc)) as 电厂, \n ");
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc))    \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh) ,grouping(dc.mingc) desc, \n");
				sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc))    \n");
				sbsql.append(" having(grouping(dr.fenx)+grouping(dr.riq)=0)    \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh) ,grouping(dc.mingc) desc, \n");
				sbsql.append(" max(px.xuh),dr.riq,dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
		}else if (getSelzhuangtValue().getValue().equals("分矿分厂")){
			strDanw="矿别";
			strMeik="单位";
			blnIsFcFk=true;
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'总计',dq.mingc) as 煤矿, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'小计',dc.mingc) as 电厂, \n");
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dq.mingc,dc.mingc))     \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dq.mingc,dc.mingc))     \n");
				sbsql.append("  having(grouping(fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dr.riq,dc.mingc,grouping(fenx) desc,fenx    \n");
			}

		}else if(getSelzhuangtValue().getValue().equals("分厂分矿")){
			strDanw="单位";
			strMeik="矿别";
			blnIsFcFk=true;
			sbsql.append(" select decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(dc.dianclbb_id),3,'总计',2,max(lb.mingc)||'合计',dc.mingc) as 电厂,\n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'电厂小计',dq.mingc) as 煤矿, \n");
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
			sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc,dq.mingc)) \n");
			sbsql.append("order by  grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc,dq.mingc)) \n");
				sbsql.append("  having(grouping(fenx)+grouping(dr.riq)=0)    \n");
				sbsql.append("order by  grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dr.riq,dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(fenx) desc,fenx    \n");
			}
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//定义表头数据
		String ArrHeader[][];
		int ArrWidth[];
		
		if (!blnIsFcFk){
			 ArrHeader=new String[4][14];
			 ArrHeader[0]=new String[] {strDanw,"日期","本月<br>或<br>累计","进厂数量<br>(吨)","         进厂抽查数量","         进厂抽查数量","         进厂抽查数量","         进厂抽查数量","盈亏情况","盈亏情况","盈亏情况","盈亏情况","索赔金额<br>(元)","说明"};
			 ArrHeader[1]=new String[] {strDanw,"日期","本月<br>或<br>累计","进厂数量<br>(吨)","      数量","      数量","      其中","      其中","盈煤<br>(吨)","盈煤金额<br>(元)","亏吨<br>(吨)","亏吨金额<br>(元)","索赔金额<br>(元)","说明"};
			 ArrHeader[2]=new String[] {strDanw,"日期","本月<br>或<br>累计","进厂数量<br>(吨)","(吨)","%","过衡%","检尺%","盈煤<br>(吨)","盈煤金额<br>(元)","亏吨<br>(吨)","亏吨金额<br>(元)","索赔金额<br>(元)","说明"};
			 ArrHeader[3]=new String[] {"甲","乙","丙","1","2","3","4","5","6","7","8","9","10","11"};
			 ArrWidth=new int[] {100,60,40,60,60,60,60,60,60,80,60,80,80,120};
		}else{
			 ArrHeader=new String[4][15];
			 ArrHeader[0]=new String[] {strDanw,strMeik,"日期","本月<br>或<br>累计","进厂数量<br>(吨)","进厂抽查数量","进厂抽查数量","进厂抽查数量","进厂抽查数量","盈亏情况","盈亏情况","盈亏情况","盈亏情况","索赔金额<br>(元)","说明"};
			 ArrHeader[1]=new String[] {strDanw,strMeik,"日期","本月<br>或<br>累计","进厂数量<br>(吨)","数量","数量","其中","其中","盈煤<br>(吨)","盈煤金额<br>(元)","亏吨<br>(吨)","亏吨金额<br>(元)","索赔金额<br>(元)","说明"};
			 ArrHeader[2]=new String[] {strDanw,strMeik,"日期","本月<br>或<br>累计","进厂数量<br>(吨)","(吨)","%","过衡%","检尺%","盈煤<br>(吨)","盈煤金额<br>(元)","亏吨<br>(吨)","亏吨金额<br>(元)","索赔金额<br>(元)","说明"};
			 ArrHeader[3]=new String[] {"甲","乙","丙","丁","1","2","3","4","5","6","7","8","9","10","11"};
			 ArrWidth=new int[] {100,120,60,35,70,70,40,40,40,55,80,55,80,70,120};
		}
			 //设置页标题
		rt.setTitle("进厂煤计量盈亏月报表("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,5,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(6,5,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃03表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,4,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.ShowZero=reportShowZero();
		rt.body.mergeFixedRow();
		
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (blnIsFcFk){
				rt.body.mergeFixedCol(2);
			}else{
				rt.body.mergeFixedCol(1);
			}
		}
		
		int iFormatCol=6;
		if(blnIsFcFk){
			iFormatCol=7;
		}
		rt.body.setColFormat(iFormatCol,"0.00");
		rt.body.setColFormat(iFormatCol+1,"0.00");
		rt.body.setColFormat(iFormatCol+2,"0.00");
		rt.body.setColFormat(iFormatCol+4,"0.00");
		rt.body.setColFormat(iFormatCol+6,"0.00");
		rt.body.setColFormat(iFormatCol+7,"0.00");
		rt.body.setColFormat(iFormatCol+8,"0.00");
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();	
	}
	
	private String getDiaor04(){
		String _Danwqc=getTianzdwQuanc();
		if(RiqMsg(RT_DR04)){
			return "数据错误！"; 
		}
				String condition = getDiancCondition();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strDate2=getNianf2Value().getValue() +"-"+getYuef2Value().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月至"+getNianf2Value().getValue()+"年"+getYuef2Value().getValue()+"月";
		String strDanw="";
		String strMeik="";
		boolean blnIsFcFk=false;
		String fenz="";
		String neir="";
		if(getFenzValue().getValue().equals("当月")){
			fenz=" and  fenx='本月'\n";
		}else if(getFenzValue().getValue().equals("累计")){
			fenz=" and  fenx='累计'\n";
		}
		if(getBBLXValue().getValue().equals("汇总")){
			fenz=" and  fenx='本月'\n";
			neir=" '' as 日期, decode(fenx,'本月','汇总','汇总') as 当月及累计, ";
		}else{
			neir="to_char(dr.riq,'yyyy-mm') as 日期, fenx as 当月及累计, ";
		}
		
		sbsql.append("    日期,当月及累计,JINCSL as 进厂数量,YANSSL as 验收数量,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(YANSSL/JINCSL*100,2)) as 检质率,    \n");
		sbsql.append("    decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2)) as 矿方热量,   \n");
		sbsql.append("    dengji(decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2))) as 矿方等级,  \n");
		sbsql.append("    decode(KUANGFSFsl,0, 0,round(KUANGFSF/KUANGFSFsl,2)) as 矿方水份,    \n");
		sbsql.append("    decode(KUANGFHFsl,0,0 ,round(KUANGFHF/KUANGFHFsl,2)) as 矿方灰份,    \n");
		sbsql.append("    decode(KUANGFHFFsl,0,0,round(KUANGFHFF/KUANGFHFFsl,2)) as 矿方挥发份,    \n");
		sbsql.append("    decode(KUANGFLFsl,0,0,round(KUANGFLF/KUANGFLFsl,2)) as 矿方硫份,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(FARL/JINCSL,2)) as 热量,    \n");
		sbsql.append("    dengji(decode(JINCSL,0,0,round(FARL/JINCSL,2))) as 等级,  \n");
		sbsql.append("    decode(JINCSL,0,0,round(SHUIF/JINCSL,2)) as 水份,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(HUIF/JINCSL,2)) as 灰份,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(HUIFF/JINCSL,2)) as 挥发份,    \n");
		sbsql.append("    decode(JINCSL,0,0,round(LIUF/JINCSL,2)) as 硫份,    \n");
		sbsql.append("    (dengji(decode(JINCSL,0,0,round(FARL/JINCSL,2)))-dengji(decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2))))*2 as 等级差,  \n");
		sbsql.append("    decode(JINCSL,0,0,round(FARL/JINCSL,2))-decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2)) as 热量差,  \n");
		sbsql.append("    decode(JINCSL,0,0,round(BUFL/JINCSL,2)) as 不符率,    \n");
		sbsql.append("    decode(BUFL,0,0,round(DANJC/BUFL,2)) as 单价差,  \n");
		sbsql.append("    ZONGJE as 总金额,SUOPJE as 索赔金额  \n");
		sbsql.append(" from (  \n");
		
		String strSqlBody1=sbsql.toString();
		sbsql.setLength(0);
		sbsql.append(neir+" \n");
		sbsql.append("        sum(JINCSL) as JINCSL,sum(YANSSL) as YANSSL ,    \n");
		sbsql.append("        sum(JINCSL * KUANGFFRL) as KUANGFFRL,sum(decode(nvl(KUANGFFRL,0),0,0,JINCSL)) as KUANGFFRLsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFSF)as KUANGFSF,   sum(decode(nvl(KUANGFSF,0),0,0,JINCSL))  as KUANGFSFsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFHF) as KUANGFHF,  sum(decode(nvl(KUANGFHF,0),0,0,JINCSL))  as KUANGFHFsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFHFF) as KUANGFHFF,sum(decode(nvl(KUANGFHFF,0),0,0,JINCSL)) as KUANGFHFFsl,  \n");
		sbsql.append("        sum(JINCSL * KUANGFLF) as KUANGFLF,  sum(decode(nvl(KUANGFLF,0),0,0,JINCSL))  as KUANGFLFsl,    \n");
		sbsql.append("        sum(JINCSL * CHANGFFRL) as FARL,    \n");
		sbsql.append("        sum(JINCSL * CHANGFSF)  as SHUIF,    \n");
		sbsql.append("        sum(JINCSL * CHANGFHF)  as HUIF,    \n");
		sbsql.append("        sum(JINCSL * CHANGFHFF) as HUIFF,    \n");
		sbsql.append("        sum(JINCSL * CHANGFLF)  as LIUF,    \n");
		sbsql.append("        sum(JINCSL * BUFL)      as BUFL,    \n");
		sbsql.append("        sum(JINCSL * DANJC*BUFL)     as DANJC,  \n");
		sbsql.append("        sum(ZONGJE) as ZONGJE,sum(SUOPJE) as SUOPJE,  \n");
		sbsql.append("        sum(RELSP) as RELSP,sum(KUIKSPSL) as KUIKSPSL,  \n");
		sbsql.append("        sum(LIUSP) as LIUSP,sum(LIUSPSL)  as LIUSPSL    \n");
		sbsql.append(" from diaor04bb dr,gongysb dq,diancxxb dc,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='月报')  px,dianclbb lb \n");
		if(RiqCheck()){
			sbsql.append("        where dr.riq=to_date('"+strDate2+"','yyyy-mm-dd') \n");
			fenz=" and  fenx='累计'\n";
		}else{
			sbsql.append("        where dr.riq>=to_date('"+strDate+"','yyyy-mm-dd')  and dr.riq<to_date('"+strDate2+"','yyyy-mm-dd')+1 \n");
		}
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(condition);
		sbsql.append("          \n").append(getGongysCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+)  \n");
		sbsql.append("  and lb.id(+)=dc.dianclbb_id \n");
		
		String strSqlBody2=sbsql.toString();;
		sbsql.setLength(0);
		if (getSelzhuangtValue().getValue().equals("分矿")) {
			strDanw="矿别";
			sbsql.append("select  煤矿,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'总计',1,sf.quanc||'合计',dq.mingc)) as 煤矿,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,sf.quanc,dq.mingc))    \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(sf.quanc,dq.mingc))    \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)    \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
			sbsql.append(")   \n");
		}else if (getSelzhuangtValue().getValue().equals("分厂")){
			strDanw="单位";
			 
			sbsql.append("select 电厂,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(dc.dianclbb_id),2,'总计',1,max(lb.mingc)||'合计',dc.mingc)) as 电厂,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc))  \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx\n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc))  \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)    \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dr.riq,dc.mingc,grouping(fenx) desc,fenx\n");
			}
			sbsql.append(")   \n");
		}else if (getSelzhuangtValue().getValue().equals("分矿分厂")){
			strDanw="矿别";
			strMeik="单位";
			blnIsFcFk=true;
			
			sbsql.append("select 煤矿,电厂,").append(strSqlBody1);
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'总计',dq.mingc) as 煤矿, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'小计',dc.mingc) as 电厂, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dq.mingc,dc.mingc))    \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dq.mingc,dc.mingc))    \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)  \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dr.riq,dc.mingc,grouping(fenx) desc,fenx    \n");
			}
			sbsql.append(")  \n");
		}else if(getSelzhuangtValue().getValue().equals("分厂分矿")){
			strDanw="单位";
			strMeik="矿别";
			blnIsFcFk=true;
			sbsql.append("select 电厂,煤矿,").append(strSqlBody1);
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(dc.dianclbb_id),3,'总计',2,max(lb.mingc)||'合计',dc.mingc) as 电厂, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'电厂小计',dq.mingc) as 煤矿, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc,dq.mingc)) \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh) ,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(fenx) desc,fenx \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc,dq.mingc)) \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh) ,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,dq.mingc,grouping(fenx) desc,fenx \n");
			}
			sbsql.append(")  \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//定义表头数据
		String ArrHeader[][];
		int ArrWidth[];
		
		if (getSelzhuangtValue().getValue().equals("分厂") || getSelzhuangtValue().getValue().equals("分矿")){
			 ArrHeader=new String[3][24];
			 ArrHeader[0]=new String[] {strDanw,"日期","本月<br>或<br>累计","验收数量","验收数量","验收数量","矿方化验","矿方化验","矿方化验","矿方化验","矿方化验","矿方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","等级差","热值差","质价不符情况","质价不符情况","质价不符情况","索赔金额<br>(元)"};
			 ArrHeader[1]=new String[] {strDanw,"日期","本月<br>或<br>累计","进厂煤量<br>(吨)","验收煤量<br>(吨)","检质率%","Qnet, ar%","等级","Mt%","Aar%","Vdaf%","St,d%","Qnet, ar%","等级","Mt%","Aar%","Vdaf%","St,d%","等级","Qnet, ar%","%","单价差<br>(元/吨)","总金额<br>(元)","索赔金额<br>(元)"};
			 ArrHeader[2]=new String[] {"甲","乙","丙","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};
			 ArrWidth=new int[] {100,50,34,60,60,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,50,85,85};
		}else{
			 ArrHeader=new String[3][25];
			 ArrHeader[0]=new String[] {strDanw,strMeik,"日期","本月<br>或<br>累计","验收数量","验收数量","验收数量","矿方化验","矿方化验","矿方化验","矿方化验","矿方化验","矿方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","厂方化验","等级差","热值差","质价不符情况","质价不符情况","质价不符情况","索赔金额<br>(元)"};
			 ArrHeader[1]=new String[] {strDanw,strMeik,"日期","本月<br>或<br>累计","进厂煤量<br>(吨)","验收煤量<br>(吨)","检质率%","Qnet, ar%","等级","Mt%","Aar%","Vdaf%","St,d%","Qnet, ar%","等级","Mt%","Aar%","Vdaf%","St,d%","等级","Qnet, ar%","%","单价差<br>(元/吨)","总金额<br>(元)","索赔金额<br>(元)"};
			 ArrHeader[2]=new String[] {"甲","乙","丙","丁","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};
			 ArrWidth=new int[] {70,90,50,34,60,60,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,50,85,85};

		}
			 //设置页标题
		rt.setTitle("进厂煤发热量计价煤质验收情况月报("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,5,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(11,3,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃04表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,3,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedRow();
		rt.body.ShowZero=reportShowZero();
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (getSelzhuangtValue().getValue().equals("分厂分矿") || getSelzhuangtValue().getValue().equals("分矿分厂")){
				rt.body.mergeFixedCol(2);
			}
			else{
				rt.body.mergeFixedCol(1);
			}
		}
		if(blnIsFcFk){
			rt.body.setColFormat(8,"0.00");
			rt.body.setColFormat(9,"0.0");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.0");
			rt.body.setColFormat(21,"0.00");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(25,"0.00");
			rt.body.setColFormat(24,"0.00");
			
		}else{
			rt.body.setColFormat(7,"0.00");
			rt.body.setColFormat(8,"0.0");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.0");
			rt.body.setColFormat(20,"0.00");
			rt.body.setColFormat(21,"0.00");
			rt.body.setColFormat(23,"0.00");
			rt.body.setColFormat(24,"0.00");
		}

		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();		
	}
	
	private String getDiaor08(){
		String _Danwqc=getTianzdwQuanc();
		if(RiqMsg(RT_DR08)){
			return "数据错误！"; 
		}
		String condition = getDiancCondition();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strDate2=getNianf2Value().getValue() +"-"+getYuef2Value().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月至"+getNianf2Value().getValue()+"年"+getYuef2Value().getValue()+"月";
		String strDanw="";
		String strMeik="";
		boolean blnIsFcFk=false;
		
		String fenz="";
		String neir="";
		if(getFenzValue().getValue().equals("当月")){
			fenz=" and  fenx='本月'\n";
		}else if(getFenzValue().getValue().equals("累计")){
			fenz=" and  fenx='累计'\n";
		}
		if(getBBLXValue().getValue().equals("汇总")){
			fenz=" and  fenx='本月'\n";
			neir=" '' as 日期, decode(fenx,'本月','汇总','汇总') as 当月及累计, ";
		}else{
			neir="to_char(dr.riq,'yyyy-mm') as 日期, fenx as 当月及累计, ";
		}
//		
//		
		String strZengzsl = "";//增值税率
		if(getNianfValue().getId()<2009){
			strZengzsl = "0.13";
		}else{
			strZengzsl = "0.17";
		}
		
		sbsql.append("a.日期,a.当月及累计,a.煤量, 矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费 as 到厂综合价, \n");
		sbsql.append("        a.矿价,a.增值税率,a.增值税额,a.交货前运杂费,a.铁路运费,a.铁运税额,a.铁路杂费,a.水运费,a.水运税额,a.水运杂费,a.汽运费,a.汽运税额,a.港杂费,a.到站杂费,a.其他费用,a.热值, \n");
		sbsql.append("        case when 热值>0 then round((矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费)*29.271/热值,2) else 0 end as 标煤单价, \n");
		sbsql.append("        case when 热值>0 then round((矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费-增值税额-铁运税额-水运税额-汽运税额)*29.271/热值,2) else 0 end as 不含税标煤单价, \n");
		sbsql.append("        round((矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费)*煤量/10000,2) as 煤款  \n");
		sbsql.append("    from ( \n");
		String strSqlBody1=sbsql.toString();
		
		sbsql.setLength(0);
		sbsql.append(neir+"\n");
		sbsql.append("		  sum(meil) as 煤量, \n");
		sbsql.append("        	 case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*kuangj)/sum(meil),2) end as 矿价, \n");
		if(getBBLXValue().getValue().equals("汇总")){
			sbsql.append( strZengzsl+" as 增值税率,\n");
		}else{
			sbsql.append("			 case when dr.riq<to_date('2009-01-01','yyyy-mm-dd') then 0.13 else 0.17 end as 增值税率,\n");
		}
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*zengzse)/sum(meil),2) end as 增值税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*jiaohqyzf)/sum(meil),2) end as 交货前运杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielyf)/sum(meil),2) end as 铁路运费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tieyse)/sum(meil),2) end as 铁运税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielzf)/sum(meil),2) end as 铁路杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyf)/sum(meil),2) end as 水运费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyse)/sum(meil),2) end as 水运税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyzf)/sum(meil),2) end as 水运杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyf)/sum(meil),2) end as 汽运费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyse)/sum(meil),2) end as 汽运税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*gangzf)/sum(meil),2) end as 港杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*daozzf)/sum(meil),2) end as 到站杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qitfy)/sum(meil),2) end as 其他费用, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*rez)/sum(meil),2) end as 热值 \n");
		sbsql.append(" from diaor08bb dr,gongysb dq,diancxxb dc,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='月报')  px\n");
		if(RiqCheck()){
			sbsql.append("        where dr.riq=to_date('"+strDate2+"','yyyy-mm-dd') \n");
			fenz=" and  fenx='累计'\n";
		}else{
			sbsql.append("        where dr.riq>=to_date('"+strDate+"','yyyy-mm-dd')  and dr.riq<to_date('"+strDate2+"','yyyy-mm-dd')+1 \n");
		}
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(condition);
		sbsql.append("          \n").append(getGongysCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+)  \n");
		
		String strSqlBody2=sbsql.toString();
		sbsql.setLength(0);
		if (getSelzhuangtValue().getValue().equals("分矿")) {
			strDanw="矿别";
			sbsql.append("select a.煤矿,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'总计',1,sf.quanc||'合计',dq.mingc)) as 煤矿,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,sf.quanc,dq.mingc))    \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lx.xuh), grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,sf.quanc,dq.mingc))    \n");
				sbsql.append(" having(grouping(dr.fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lx.xuh), grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
			sbsql.append("       ) a \n");
			
		}else if (getSelzhuangtValue().getValue().equals("分厂")){
			strDanw="单位";
			 
			sbsql.append("select a.电厂,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(dc.dianclbb_id),2,'总计',1,max(lx.mingc)||'合计',dc.mingc)) as 电厂,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc))    \n");
				sbsql.append(" order by grouping(dc.dianclbb_id)desc ,max(lx.xuh), grouping(dc.mingc) desc, \n");
				sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc))    \n");
				sbsql.append(" having(grouping(dr.fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dc.dianclbb_id)desc ,max(lx.xuh), grouping(dc.mingc) desc, \n");
				sbsql.append(" max(px.xuh),dr.riq,dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
			sbsql.append("          ) a \n");
		}else if (getSelzhuangtValue().getValue().equals("分矿分厂")){
			strDanw="矿别";
			strMeik="单位";
			blnIsFcFk=true;
			 
			sbsql.append("select a.煤矿,a.电厂,").append(strSqlBody1);
			sbsql.append("	select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'总计',dq.mingc) as 煤矿, \n");
			sbsql.append("		decode(grouping(dc.mingc)+grouping(dq.mingc),1,'小计',dc.mingc) as 电厂, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dq.mingc,dc.mingc))    \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dq.mingc,dc.mingc))    \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dr.riq,dc.mingc,grouping(fenx) desc,fenx    \n");
			}
			sbsql.append("      ) a \n");
		}else if(getSelzhuangtValue().getValue().equals("分厂分矿")){
			strDanw="单位";
			strMeik="矿别";
			blnIsFcFk=true;
			
			sbsql.append("select a.电厂,a.煤矿,").append(strSqlBody1);
			sbsql.append("	select decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(dc.dianclbb_id),3,'总计',2,max(lx.mingc)||'合计',dc.mingc) as 电厂, \n");
			sbsql.append("		decode(grouping(dc.mingc)+grouping(dq.mingc),1,'电厂小计',dq.mingc) as 煤矿, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc,dq.mingc))    \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lx.xuh) ,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc,dq.mingc))    \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lx.xuh) ,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,dq.mingc,grouping(fenx) desc,fenx    \n");
			}
			sbsql.append("      ) a \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//定义表头数据
		String ArrHeader[][];
		int ArrWidth[];
		
		if (getSelzhuangtValue().getValue().equals("分厂") || getSelzhuangtValue().getValue().equals("分矿")){
			 ArrHeader = new String[2][24];
			 ArrHeader[0]=new String[] {strDanw,"日期","本月<br>或<br>累计","煤量(吨)","到厂<br>综合价<br>(元/吨)","矿价<br>(元/吨)","增值<br>税率<br>%","增值税额<br>(元/吨)","交货前<br>运杂费<br>(元/吨)","铁路运费<br>(元/吨)","铁路税额<br>(元/吨)","铁路杂费<br>(元/吨)","水运费<br>(元/吨)","水运税额<br>(元/吨)","水运杂费<br>(元/吨)","汽运费<br>(元/吨)","汽运税额<br>(元/吨)","港杂费<br>(元/吨)","到站杂费<br>(元/吨)","其他费用<br>(元/吨)","热值<br>Mj/kg","标煤单价<br>含税<br>(元/吨)","标煤单价<br>不含税<br>(元/吨)","煤款<br>(万元)"};
			 ArrHeader[1]=new String[] {"甲","乙","丙","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};

			 ArrWidth=new int[] {100,50,40,60,50,50,40,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,60};

		}else{
			 ArrHeader=new String[2][25];
			 ArrHeader[0]=new String[] {strDanw,strMeik,"日期","本月<br>或<br>累计","煤量(吨)","到厂<br>综合价<br>(元/吨)","矿价<br>(元/吨)","增值<br>税率<br>%","增值税额<br>(元/吨)","交货前<br>运杂费<br>(元/吨)","铁路运费<br>(元/吨)","铁路税额<br>(元/吨)","铁路杂费<br>(元/吨)","水运费<br>(元/吨)","水运税额<br>(元/吨)","水运杂费<br>(元/吨)","汽运费<br>(元/吨)","汽运税额<br>(元/吨)","港杂费<br>(元/吨)","到站杂费<br>(元/吨)","其他费用<br>(元/吨)","热值<br>Mj/kg","标煤单价<br>含税<br>(元/吨)","标煤单价<br>不含税<br>(元/吨)","煤款<br>(万元)"};
			 ArrHeader[1]=new String[] {"甲","乙","丙","丁","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};

			 ArrWidth=new int[] {80,100,50,30,60,50,50,40,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,60};

		}
			 //设置页标题
		rt.setTitle("电煤价格情况("+getSelzhuangtValue().getValue()+")表",ArrWidth);
		rt.setDefaultTitle(1,5,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(11,3,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃08-1表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,2,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(22);
		rt.body.setHeaderData(ArrHeader);//表头数据
//		rt.body.mergeFixedRow();
		rt.body.ShowZero=reportShowZero();
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (getSelzhuangtValue().getValue().equals("分厂分矿") || getSelzhuangtValue().getValue().equals("分矿分厂")){
				rt.body.mergeFixedCol(2);
			}
			else{
				rt.body.mergeFixedCol(1);
			}
		}
		if(blnIsFcFk){
			rt.body.setColFormat(6,"0.00");
			rt.body.setColFormat(7,"0.00");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.00");
			rt.body.setColFormat(21,"0.000");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(23,"0.00");
			rt.body.setColFormat(24,"0.00");
			rt.body.setColFormat(25,"0.00");
			
		}else{
			rt.body.setColFormat(5,"0.00");
			rt.body.setColFormat(6,"0.00");
			rt.body.setColFormat(8,"0.00");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.000");
			rt.body.setColFormat(21,"0.00");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(23,"0.00");
			rt.body.setColFormat(24,"0.00");
		}
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();		
	}
	
	private String getDiaor08_New(){
		if(RiqMsg(RT_DR08)){
			return "数据错误！"; 
		}
		String _Danwqc=getTianzdwQuanc();
		
		String condition = getDiancCondition();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strDate2=getNianf2Value().getValue() +"-"+getYuef2Value().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月至"+getNianf2Value().getValue()+"年"+getYuef2Value().getValue()+"月";
		String strDanw="";
		String strMeik="";
		boolean blnIsFcFk=false;
		
		String fenz="";
		String neir="";
		if(getFenzValue().getValue().equals("当月")){
			fenz=" and  fenx='本月'\n";
		}else if(getFenzValue().getValue().equals("累计")){
			fenz=" and  fenx='累计'\n";
		}
		if(getBBLXValue().getValue().equals("汇总")){
			fenz=" and  fenx='本月'\n";
			neir=" '' as 日期, decode(fenx,'本月','汇总','汇总') as 当月及累计, ";
		}else{
			neir="to_char(dr.riq,'yyyy-mm') as 日期, fenx as 当月及累计, ";
		}
		String strZengzsl = "";//增值税率
		if(getNianfValue().getId()<2009){
			strZengzsl = "0.13";
		}else{
			strZengzsl = "0.17";
		}
		sbsql.append("a.日期,a.当月及累计,a.煤量, 矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费 as 到厂综合价, \n");
		sbsql.append("        a.矿价,a.增值税率,a.增值税额,a.交货前运杂费,a.铁路运费,a.铁运税额,a.铁路杂费,a.水运费,a.水运税额,a.水运杂费,a.汽运费,a.汽运税额,a.港杂费,a.到站杂费,a.其他费用,a.热值, \n");
		sbsql.append("        case when 热值>0 then round((矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费)*29.271/热值,2) else 0 end as 标煤单价, \n");
		sbsql.append("        case when 热值>0 then round((矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费-增值税额-铁运税额-水运税额-汽运税额)*29.271/热值,2) else 0 end as 不含税标煤单价, \n");
		sbsql.append("        round((矿价+交货前运杂费+铁路运费+铁路杂费+到站杂费+其他费用+水运费+水运杂费+汽运费+港杂费)*煤量/10000,2) as 煤款  \n");
		sbsql.append("    from ( \n");
		String strSqlBody1=sbsql.toString();
		
		sbsql.setLength(0);
		sbsql.append(neir+"\n");
		sbsql.append("        	 sum(meil) as 煤量, \n");
		sbsql.append("        	 case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*kuangj)/sum(meil),2) end as 矿价, \n");
		if(getBBLXValue().getValue().equals("汇总")){
			sbsql.append( strZengzsl+" as 增值税率,\n");
		}else{
			sbsql.append("		 case when dr.riq<to_date('2009-01-01','yyyy-mm-dd') then 0.13 else 0.17 end as 增值税率,\n");
		}
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*zengzse)/sum(meil),2) end as 增值税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*jiaohqyzf)/sum(meil),2) end as 交货前运杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielyf)/sum(meil),2) end as 铁路运费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tieyse)/sum(meil),2) end as 铁运税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielzf)/sum(meil),2) end as 铁路杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyf)/sum(meil),2) end as 水运费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyse)/sum(meil),2) end as 水运税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyzf)/sum(meil),2) end as 水运杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyf)/sum(meil),2) end as 汽运费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyse)/sum(meil),2) end as 汽运税额, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*gangzf)/sum(meil),2) end as 港杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*daozzf)/sum(meil),2) end as 到站杂费, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qitfy)/sum(meil),2) end as 其他费用, \n");
		sbsql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*rez)/sum(meil),2) end as 热值 \n");
		sbsql.append(" from diaor08bb_new dr,gongysb dq,diancxxb dc,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='月报')  px,dianclbb lb \n");
		if(RiqCheck()){
			sbsql.append("        where dr.riq=to_date('"+strDate2+"','yyyy-mm-dd') \n");
			fenz=" and  fenx='累计'\n";
		}else{
			sbsql.append("        where dr.riq>=to_date('"+strDate+"','yyyy-mm-dd')  and dr.riq<to_date('"+strDate2+"','yyyy-mm-dd')+1 \n");
		}
		sbsql.append("         and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) \n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(condition);
		sbsql.append("          \n").append(getGongysCondition());
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+)  \n");
		sbsql.append("and lb.id(+)=dc.dianclbb_id\n");
		String strSqlBody2=sbsql.toString();
		sbsql.setLength(0);
		if (getSelzhuangtValue().getValue().equals("分矿")) {
			strDanw="矿别";
			sbsql.append("select a.煤矿,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'总计',1,sf.quanc||'合计',dq.mingc)) as 煤矿,   \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,sf.quanc,dq.mingc) )   \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(sf.quanc,dq.mingc))    \n");
				sbsql.append(" having(grouping(dr.fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),sf.quanc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,dq.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
			sbsql.append("       ) a \n");
			
		}else if (getSelzhuangtValue().getValue().equals("分厂")){
			strDanw="单位";
			 
			sbsql.append("select a.电厂,").append(strSqlBody1);
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(dc.dianclbb_id),2,'总计',1,max(lb.mingc)||'合计',dc.mingc)) as 电厂, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc))  \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc, \n");
				sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc))  \n");
				sbsql.append(" having(grouping(dr.fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc, \n");
				sbsql.append(" max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
			sbsql.append("          ) a \n");
		}else if (getSelzhuangtValue().getValue().equals("分矿分厂")){
			strDanw="矿别";
			strMeik="单位";
			blnIsFcFk=true;
			 
			sbsql.append("select a.煤矿,a.电厂,").append(strSqlBody1);
			sbsql.append("	select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'总计',dq.mingc) as 煤矿, \n");
			sbsql.append("		decode(grouping(dc.mingc)+grouping(dq.mingc),1,'小计',dc.mingc) as 电厂, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dq.mingc,dc.mingc))    \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dq.mingc,dc.mingc) )   \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(dc.mingc) desc,max(px.xuh),dr.riq,dc.mingc,grouping(fenx) desc,fenx    \n");
			}
			sbsql.append("      ) a \n");
		}else if(getSelzhuangtValue().getValue().equals("分厂分矿")){
			strDanw="单位";
			strMeik="矿别";
			blnIsFcFk=true;
			
			sbsql.append("select a.电厂,a.煤矿,").append(strSqlBody1);
			sbsql.append("	select decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(dc.dianclbb_id),3,'总计',2,max(lb.mingc)||'合计',dc.mingc) as 电厂, \n");
			sbsql.append("		decode(grouping(dc.mingc)+grouping(dq.mingc),1,'电厂小计',dq.mingc) as 煤矿, \n");
			sbsql.append(strSqlBody2);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc,dq.mingc))    \n");
				sbsql.append(" order by grouping(dc.dianclbb_id)desc ,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc,dq.mingc))    \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dc.dianclbb_id)desc ,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,dq.mingc,grouping(fenx) desc,fenx    \n");
			}
			sbsql.append("      ) a \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//定义表头数据
		String ArrHeader[][];
		int ArrWidth[];
		
		if (getSelzhuangtValue().getValue().equals("分厂") || getSelzhuangtValue().getValue().equals("分矿")){
			 ArrHeader = new String[2][24];
			 ArrHeader[0]=new String[] {strDanw,"日期","本月<br>或<br>累计","煤量(吨)","到厂<br>综合价<br>(元/吨)","矿价<br>(元/吨)","增值<br>税率<br>%","增值税额<br>(元/吨)","交货前<br>运杂费<br>(元/吨)","铁路运费<br>(元/吨)","铁路税额<br>(元/吨)","铁路杂费<br>(元/吨)","水运费<br>(元/吨)","水运税额<br>(元/吨)","水运杂费<br>(元/吨)","汽运费<br>(元/吨)","汽运税额<br>(元/吨)","港杂费<br>(元/吨)","到站杂费<br>(元/吨)","其他费用<br>(元/吨)","热值<br>Mj/kg","标煤单价<br>含税<br>(元/吨)","标煤单价<br>不含税<br>(元/吨)","煤款<br>(万元)"};
			 ArrHeader[1]=new String[] {"甲","乙","丙","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};

			 ArrWidth=new int[] {100,50,40,60,50,50,40,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,60};

		}else{
			 ArrHeader=new String[2][25];
			 ArrHeader[0]=new String[] {strDanw,strMeik,"日期","本月<br>或<br>累计","煤量(吨)","到厂<br>综合价<br>(元/吨)","矿价<br>(元/吨)","增值<br>税率<br>%","增值税额<br>(元/吨)","交货前<br>运杂费<br>(元/吨)","铁路运费<br>(元/吨)","铁路税额<br>(元/吨)","铁路杂费<br>(元/吨)","水运费<br>(元/吨)","水运税额<br>(元/吨)","水运杂费<br>(元/吨)","汽运费<br>(元/吨)","汽运税额<br>(元/吨)","港杂费<br>(元/吨)","到站杂费<br>(元/吨)","其他费用<br>(元/吨)","热值<br>Mj/kg","标煤单价<br>含税<br>(元/吨)","标煤单价<br>不含税<br>(元/吨)","煤款<br>(万元)"};
			 ArrHeader[1]=new String[] {"甲","乙","丙","丁","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21"};

			 ArrWidth=new int[] {80,100,50,30,60,50,50,40,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,60};

		}
			 //设置页标题
		rt.setTitle("电煤价格情况("+getSelzhuangtValue().getValue()+")表",ArrWidth);
		rt.setDefaultTitle(1,7,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(11,3,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃08表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,2,0,3));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(22);
		rt.body.setHeaderData(ArrHeader);//表头数据
//		rt.body.mergeFixedRow();
		rt.body.ShowZero=reportShowZero();
		if (rt.body.getRows()>rt.body.getFixedRows()){
			if (getSelzhuangtValue().getValue().equals("分厂分矿") || getSelzhuangtValue().getValue().equals("分矿分厂")){
				rt.body.mergeFixedCol(2);
			}
			else{
				rt.body.mergeFixedCol(1);
			}
		}
		if(blnIsFcFk){
			rt.body.setColFormat(6,"0.00");
			rt.body.setColFormat(7,"0.00");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.00");
			rt.body.setColFormat(21,"0.000");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(23,"0.00");
			rt.body.setColFormat(24,"0.00");
			rt.body.setColFormat(25,"0.00");
			
		}else{
			rt.body.setColFormat(5,"0.00");
			rt.body.setColFormat(6,"0.00");
			rt.body.setColFormat(8,"0.00");
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
			rt.body.setColFormat(14,"0.00");
			rt.body.setColFormat(15,"0.00");
			rt.body.setColFormat(16,"0.00");
			rt.body.setColFormat(17,"0.00");
			rt.body.setColFormat(18,"0.00");
			rt.body.setColFormat(19,"0.00");
			rt.body.setColFormat(20,"0.000");
			rt.body.setColFormat(21,"0.00");
			rt.body.setColFormat(22,"0.00");
			rt.body.setColFormat(23,"0.00");
			rt.body.setColFormat(24,"0.00");
		}
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();		
	
	}

	private String getDiaor16(){
		if(RiqMsg(RT_DR16)){
			return "数据错误！";
		}
		String _Danwqc=getTianzdwQuanc();
		
		String condition = getDiancCondition();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strDate2=getNianf2Value().getValue() +"-"+getYuef2Value().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月至"+getNianf2Value().getValue()+"年"+getYuef2Value().getValue()+"月";
		String strDanw="";
		String strMeik="";
		String strSqlBody="";
		boolean blnIsFcFk =false;
		String fenz="";
		String neir="";
		if(getFenzValue().getValue().equals("当月")){
			fenz=" and  fenx='本月'\n";
		}else if(getFenzValue().getValue().equals("累计")){
			fenz=" and  fenx='累计'\n";
		}
		if(getBBLXValue().getValue().equals("汇总")){
			fenz=" and  fenx='本月'\n";
			neir=" '' as 日期, decode(fenx,'本月','汇总','汇总') as 当月及累计, ";
		}else{
			neir="to_char(dr.riq,'yyyy-mm') as 日期, fenx as 当月及累计, ";
		}
		
		sbsql.append("        ' ' as 品种, "+neir+" \n");
		sbsql.append("        case when fenx='累计' then '-' else to_char( sum(shangyjc)) end as 上月结存,   \n");
		sbsql.append("        sum(kuangfgyl) as 矿方供应量, sum(kuid) as 亏吨  ,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * farl) /sum(kuangfgyl),2) else 0 end) as 发热量,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * huif) /sum(kuangfgyl),2) else 0 end) as 灰份,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * shuif) /sum(kuangfgyl),2) else 0 end) as 水份,   \n");
		sbsql.append("        (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * huiff) /sum(kuangfgyl),2) else 0 end) as 挥发份 ,   \n");
		sbsql.append("        sum(qitsrl) as 其他收入量,sum(shijhylhj) as 合计用,sum(shijhylfdy) as 发电用,   \n");
		sbsql.append("        sum(shijhylgry) as 供热用,sum(shijhylqty) as 其他用,sum(SHIJHYLZCSH) as 存损,sum(diaocl) as 调出量,sum(panypk) as 盘盈亏,   \n");
		sbsql.append("        case when fenx='累计' then '-' else to_char(sum(yuemjc)) end as 月末结存    \n");
		sbsql.append("from diaor16bb dr,gongysb dq,diancxxb dc,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='月报')  px, dianclbb lb\n");
		if(RiqCheck()){
			sbsql.append("        where dr.riq=to_date('"+strDate2+"','yyyy-mm-dd') \n");
			fenz=" and  fenx='累计'\n";
		}else{
			sbsql.append("        where dr.riq>=to_date('"+strDate+"','yyyy-mm-dd')  and dr.riq<to_date('"+strDate2+"','yyyy-mm-dd')+1 \n");
		}
		sbsql.append(" and dr.gongysb_id=dq.id(+) and dc.id=px.diancxxb_id  (+) and lb.id(+)=dc.dianclbb_id\n");
		sbsql.append("         and dc.id=dr.diancxxb_id   \n").append(getGongysCondition());
		sbsql.append("          \n").append(condition);
		sbsql.append("         and dc.dianclbb_id(+)=lx.id  and dq.shengfb_id=sf.id(+)  \n");
		
		strSqlBody=sbsql.toString();
		sbsql.setLength(0);
		
		if (getSelzhuangtValue().getValue().equals("分矿")) {
			strDanw="矿别";
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'总计',1,sf.quanc||'合计',dq.mingc)) as 煤矿名称,   \n");
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,sf.quanc,dq.mingc) ) \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),grouping(dq.mingc) desc,dq.mingc,max(dq.xuh),grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(sf.quanc,dq.mingc) ) \n");
				sbsql.append("  having(grouping(dr.fenx)+grouping(dr.riq)=0)     \n");
				sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),dr.riq,grouping(dq.mingc) desc,dq.mingc,max(dq.xuh),grouping(dr.fenx) desc,dr.fenx  \n");
			}
		}else if (getSelzhuangtValue().getValue().equals("分厂")){
			String xiugai="select (case when grouping(dc.mingc)=1 and grouping(dc.dianclbb_id)=1then '总计'when grouping(dc.mingc)=1 and grouping(dc.dianclbb_id)=0 then max(lb.mingc)||'合计' when grouping(dc.mingc)=0 and grouping(dc.dianclbb_id)=0 then dc.mingc  end ) as 电厂,\n";
			strDanw="单位";
			sbsql.append(xiugai);
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc) ) \n");
				sbsql.append(" order by   grouping(dc.dianclbb_id) desc, grouping(dc.mingc) desc, max(px.xuh),dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc) ) \n");
				sbsql.append("  having(grouping(dr.fenx)+grouping(dr.riq)=0)     \n");
				sbsql.append(" order by   grouping(dc.dianclbb_id) desc, grouping(dc.mingc) desc, max(px.xuh),dr.riq,dc.mingc,grouping(dr.fenx) desc,dr.fenx  \n");
			}
		}else if (getSelzhuangtValue().getValue().equals("分矿分厂")){
			blnIsFcFk=true;
			strDanw="矿别";
			strMeik="单位";
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'总计',dq.mingc) as 煤矿, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'小计',dc.mingc) as 电厂, \n");
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append("  group by rollup ((dr.fenx,dq.mingc,dc.mingc) )   \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(fenx) desc,fenx    \n");
			}else{
				sbsql.append("  group by rollup (dr.fenx,dr.riq,(dq.mingc,dc.mingc) )   \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)   \n");
				sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),grouping(dc.mingc) desc,max(px.xuh),dr.riq,dc.mingc,grouping(fenx) desc,fenx    \n");
			}
		}else if(getSelzhuangtValue().getValue().equals("分厂分矿")){
			strDanw="单位";
			strMeik="矿别";
			blnIsFcFk=true;
			sbsql.append("select --dr.fenx,fgs.mingc,dc.mingc,dq.mingc, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(dc.dianclbb_id),3,'总计',2,max(lb.mingc)||'合计',dc.mingc) as 电厂, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'电厂小计',dq.mingc) as 煤矿, \n");
			sbsql.append(strSqlBody);
			sbsql.append(fenz);
			if(getBBLXValue().getValue().equals("汇总")){
				sbsql.append(" group by rollup ((dr.fenx,dc.dianclbb_id,dc.mingc,dq.mingc))    \n");
				sbsql.append("order by grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),grouping(fenx) desc,fenx\n");
			}else{
				sbsql.append(" group by rollup (dr.fenx,dr.riq,(dc.dianclbb_id,dc.mingc,dq.mingc))    \n");
				sbsql.append(" having(grouping(fenx)+grouping(dr.riq)=0)    \n");
				sbsql.append("order by grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh),dr.riq,grouping(fenx) desc,fenx\n");
			}
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//定义表头数据
		String ArrHeader[][];
		int ArrWidth[];
		
		if (getSelzhuangtValue().getValue().equals("分厂") || getSelzhuangtValue().getValue().equals("分矿")){
			 ArrHeader=new String[4][20];
			 ArrHeader[0]=new String[] {"品名","品名","日期","本月<br>或<br>累计","月初<br>结存量<br>(吨)","实际供应情况","实际供应情况","实际供应情况","实际供应情况","实际供应情况","实际供应情况","其它<br>收入量<br>(吨)","实际耗用量","实际耗用量","实际耗用量","实际耗用量","实际耗用量","调出量<br>(吨)","盘盈<br>或<br>盘亏<br>(吨)","月末<br>结存量<br>(吨)"};
			 ArrHeader[1]=new String[] {strDanw,"煤种","日期","本月<br>或<br>累计","月初<br>结存量<br>(吨)","矿方供应量","矿方供应量","发热量<br>Qnet,ar<br>MJ/kg","灰分Ad%","水分Mt%","挥发分<br>Vdaf%","其它<br>收入量<br>(吨)","合计<br>(吨)","发电用<br>(吨)","供热用<br>(吨)","其它用<br>(吨)","存损用<br>(吨)","调出量<br>(吨)","盘盈<br>或<br>盘亏<br>(吨)","月末<br>结存量<br>(吨)"};
			 ArrHeader[2]=new String[] {strDanw,"煤种","日期","本月<br>或<br>累计","月初<br>结存量<br>(吨)","合计<br>(吨)","运损/亏吨<br>(吨)","发热量<br>Qnet,ar<br>MJ/kg","灰分Ad%","水分Mt%","挥发分<br>Vdaf%","其它<br>收入量<br>(吨)","合计<br>(吨)","发电用<br>(吨)","供热用<br>(吨)","其它用<br>(吨)","存损用<br>(吨)","调出量<br>(吨)","盘盈<br>或<br>盘亏<br>(吨)","月末<br>结存量<br>(吨)"};
			 ArrHeader[3]=new String[] {"甲","乙","丙","丁","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
			 ArrWidth=new int[] {80,40,60,30,60,60,60,60,40,40,40,60,60,60,60,60,60,60,60,60};
					
		}else{
			 ArrHeader=new String[4][21];
			 ArrHeader[0]=new String[] {"品名","品名","品名","日期","本月<br>或<br>累计","月初<br>结存量<br>(吨)","实际供应情况","实际供应情况","实际供应情况","实际供应情况","实际供应情况","实际供应情况","其它<br>收入量<br>(吨)","实际耗用量","实际耗用量","实际耗用量","实际耗用量","实际耗用量","调出量<br>(吨)","盘盈<br>或<br>盘亏<br>(吨)","月末<br>结存量<br>(吨)"};
			 ArrHeader[1]=new String[] {strDanw,strMeik,"煤种","日期","本月<br>或<br>累计","月初<br>结存量<br>(吨)","矿方供应量","矿方供应量","发热量<br>Qnet,ar<br>MJ/kg","灰分Ad%","水分Mt%","挥发分<br>Vdaf%","其它<br>收入量<br>(吨)","合计<br>(吨)","发电用<br>(吨)","供热用<br>(吨)","其它用<br>(吨)","存损用<br>(吨)","调出量<br>(吨)","盘盈<br>或<br>盘亏<br>(吨)","月末<br>结存量<br>(吨)"};
			 ArrHeader[2]=new String[] {strDanw,strMeik,"煤种","日期","本月<br>或<br>累计","月初<br>结存量<br>(吨)","合计<br>(吨)","运损/亏吨<br>(吨)","发热量<br>Qnet,ar<br>MJ/kg","灰分Ad%","水分Mt%","挥发分<br>Vdaf%","其它<br>收入量<br>(吨)","合计<br>(吨)","发电用<br>(吨)","供热用<br>(吨)","其它用<br>(吨)","存损用<br>(吨)","调出量<br>(吨)","盘盈<br>或<br>盘亏<br>(吨)","月末<br>结存量<br>(吨)"};
			 ArrHeader[3]=new String[] {"甲","乙","丙","丁","戊","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};

			 ArrWidth=new int[] {80,100,45,60,30,60,60,60,60,40,40,40,60,60,60,60,60,60,60,60,60};
		}
			 //设置页标题
		rt.setTitle("生产用煤炭供应、耗用与结存月报("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,5,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(9,4,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols()-1,2,"电生16-1表",Table.ALIGN_RIGHT);
		
		
		//设置页面
//		rt.setPaper(rt.PAPER_A4);
//		rt.setOrientation(rt.PAPER_Landscape);
//		rt.setSplitPageStyle(rt.PAGE_HEIGHT);
//		rt.setMarginBottom(rt.getMarginBottom()+25);
//		数据
		rt.setBody(new Table(rs,4,0,4));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		rt.getPages();
		
//		
//		if (rt.body.getRows()>rt.body.getFixedRows()){
//			if (getSelzhuangtValue().getValue().equals("分厂分矿") || getSelzhuangtValue().getValue().equals("分矿分厂")){
//				rt.body.mergeFixedCol(2);
//			}
//			else{
//				rt.body.mergeFixedCol(1);
//			}
//		}
		if (blnIsFcFk){
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
			rt.body.setColFormat(13,"0.00");
		}else{
			rt.body.setColFormat(9,"0.00");
			rt.body.setColFormat(10,"0.00");
			rt.body.setColFormat(11,"0.00");
			rt.body.setColFormat(12,"0.00");
		}
		rt.body.ShowZero=reportShowZero();
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(8,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
//		return rt.getAllPapersHtml(0);
	}
	
	private String getDiaor02(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		String strCondtion="";
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strDate2=getNianf2Value().getValue() +"-"+getYuef2Value().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月至"+getNianf2Value().getValue()+"年"+getYuef2Value().getValue()+"月";
		String strYear=getNianfValue().getValue() +"-01-01";
		StringBuffer sbsql = new StringBuffer();
		
		if (isJTUser()){
			strCondtion="";
		}
		String condition = getDiancCondition();
		sbsql.append("  sum(nvl(hetby,0)) as 计划当月,sum(nvl(hetlj,0)) as 计划累计, \n");
		sbsql.append("      sum(nvl(shisby,0)) as 实收当月, sum(nvl(shislj,0)) as 实收累计, \n");
		sbsql.append("      sum(nvl(shisby,0))-sum(nvl(hetby,0)) as 超欠当月, \n");
		sbsql.append("      sum(nvl(shislj,0))-sum(nvl(hetlj,0)) as 超欠累计, \n");
		sbsql.append("      baifb(sum(nvl(shisby,0)),sum(nvl(hetby,0)) ) as 到货率当月, \n");
		sbsql.append("      baifb(sum(nvl(shislj,0)),sum(nvl(hetlj,0)) ) as 到货率累计 \n");
		sbsql.append("from (select ht.diancxxb_id,decode(dq.fuid,0,dq.id,dq.fuid) as gongysb_id \n");
		sbsql.append(" 		from  hetb ht,hetslb sl,gongysb dq,diancxxb dc \n");
		sbsql.append("  	where ht.gongysb_id=dq.id and ht.id=sl.id  and ht.liucztb_id=1  \n").append(getGongysCondition());
		sbsql.append("       and ht.id=sl.id and dc.id=ht.diancxxb_id \n").append(condition);
		sbsql.append("       and sl.riq>=to_date('"+strYear+"','yyyy-mm-dd') \n");
		sbsql.append("       and sl.riq<=to_date('"+strDate+"','yyyy-mm-dd')  \n");
		sbsql.append(" union \n");
		sbsql.append(" select dc.id as diancxxb_id,gongysb_id from diaor16bb dr,diancxxb dc,gongysb dq \n");
		sbsql.append("        where kuangfgyl>0 and dq.id=dr.gongysb_id and dc.id=dr.diancxxb_id \n").append(condition);
		sbsql.append("        and  riq=to_date('"+strDate+"','yyyy-mm-dd') \n");
		sbsql.append("        ) a, \n");
		sbsql.append("(select ht.diancxxb_id,decode(dq.fuid,0,dq.id,dq.fuid) as gongysb_id, \n");
		sbsql.append(" sum(decode(sl.riq,to_date('"+strDate+"','yyyy-mm-dd'),sl.hetl,0)) as hetby, \n");
		sbsql.append(" sum(sl.hetl) as hetlj \n");
		sbsql.append(" from hetb ht,hetslb sl,gongysb dq,diancxxb dc \n");
		sbsql.append("       where  ht.gongysb_id=dq.id \n");
		sbsql.append("       and ht.liucztb_id=1 and ht.diancxxb_id=dc.id \n");
		sbsql.append("       and ht.id=sl.id \n").append(condition);
		sbsql.append("       and sl.riq>=to_date('"+strYear+"','yyyy-mm-dd') \n");
		sbsql.append("       and sl.riq<=to_date('"+strDate+"','yyyy-mm-dd') \n");
		sbsql.append("group by ht.diancxxb_id,decode(dq.fuid,0,dq.id,dq.fuid)) ht, \n");
		sbsql.append("(select dr.diancxxb_id,gongysb_id, sum(decode(fenx,'本月',kuangfgyl)) as shisby,sum(decode(fenx,'累计',kuangfgyl)) as shislj \n");
		sbsql.append("    from diaor16bb  dr,diancxxb dc,gongysb dq \n");
		sbsql.append("    where riq = to_date('"+strDate+"', 'yyyy-mm-dd') ");
		sbsql.append("    and dc.id=dr.diancxxb_id and dq.id=dr.gongysb_id \n").append(condition);
		sbsql.append("group by dr.diancxxb_id,gongysb_id) sl, \n");
		sbsql.append("diancxxb dc,gongysb dq,dianclbb lx,shengfb sf ,(select * from dianckjpxb where kouj='月报')  px,dianclbb lb \n");
		sbsql.append("WHERE  a.diancxxb_id=dc.id \n");
		sbsql.append("     and a.gongysb_id=dq.id \n");
		sbsql.append("     and dc.dianclbb_id=lx.id(+)  \n");
		sbsql.append("     and a.diancxxb_id=ht.diancxxb_id(+) and a.gongysb_id=ht.gongysb_id(+) \n");
		sbsql.append("     and a.diancxxb_id=sl.diancxxb_id(+) and a.gongysb_id=sl.gongysb_id(+) \n");
		sbsql.append("     and dc.id=px.diancxxb_id(+)  and dq.shengfb_id=sf.id(+)  \n");
		sbsql.append(" and lb.id(+)=dc.dianclbb_id\n");
		
		String strSqlBody=sbsql.toString();
		sbsql.setLength(0);
		if (getSelzhuangtValue().getValue().equals("分矿")) {
			sbsql.append("select  (decode(grouping(dq.mingc)+grouping(sf.quanc),2,'总计',1,sf.quanc||'合计',dq.mingc)) as 煤矿名称,   \n");
			sbsql.append(strSqlBody); 
			sbsql.append(" group by rollup (sf.quanc,dq.mingc)    \n");
			sbsql.append(" order by grouping(sf.quanc) desc,max(sf.xuh),grouping(dq.mingc) desc,dq.mingc,max(dq.xuh)  \n");
			
		}else if(getSelzhuangtValue().getValue().equals("分厂")){
			sbsql.append("select  (decode(grouping(dc.mingc)+grouping(dc.dianclbb_id),2,'总计',1,max(lb.mingc)||'合计',dc.mingc)) as 电厂, \n");
//			sbsql.append("select  (decode(grouping(dc.mingc),1,'总计',dc.mingc)) as 电厂,   \n");
			sbsql.append(strSqlBody); 
//			sbsql.append(" group by rollup (dc.mingc)    \n");
//			sbsql.append(" order by grouping(dc.mingc) desc, \n");
			sbsql.append("group by rollup (dc.dianclbb_id,dc.mingc)    \n");
			sbsql.append(" order by grouping(dc.dianclbb_id) desc,max(lb.xuh), grouping(dc.mingc) desc, \n");
			sbsql.append(" max(px.xuh),dc.mingc \n");
			
		}else if(getSelzhuangtValue().getValue().equals("分厂分矿")){
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(lx.id),3,'总计',2,max(lx.mingc)||'合计',dc.mingc) as 电厂,  \n");
			sbsql.append("      decode(grouping(dc.mingc)+grouping(dq.mingc),1,'电厂小计',dq.mingc) as 煤矿,  \n");
			sbsql.append(strSqlBody); 
			sbsql.append("group by rollup (lx.id,dc.mingc,dq.mingc)     \n");
			sbsql.append("order by grouping(lx.id) desc,max(lx.xuh), grouping(dc.mingc) desc,max(px.xuh),dc.mingc,grouping(dq.mingc) desc,max(dq.xuh) \n");
		
		}else if(getSelzhuangtValue().getValue().equals("分矿分厂")){
			sbsql.append("select decode(grouping(dc.mingc)+grouping(dq.mingc),2,'总计',dq.mingc) as 煤矿, \n");
			sbsql.append("decode(grouping(dc.mingc)+grouping(dq.mingc),1,'小计',dc.mingc) as 电厂, \n");
			sbsql.append(strSqlBody); 
			sbsql.append(" group by rollup (dq.mingc,dc.mingc)    \n");
			sbsql.append(" order by grouping(dq.mingc) desc,max(dq.xuh),grouping(dc.mingc) desc,max(px.xuh),dc.mingc   \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//定义表头数据
		String ArrHeader[][]=null;
		int ArrWidth[]=null;
		if(getSelzhuangtValue().getValue().equals("分矿")){
			ArrHeader=new String[2][9];
			ArrHeader[0]=new String[] {"矿别","计划(吨)","计划(吨)","实收(吨)","实收(吨)","亏欠(吨)","亏欠(吨)","到货率%","到货率%"};
			ArrHeader[1]=new String[] {"矿别","当月","累计","当月","累计","当月","累计","当月","累计"};
			ArrWidth=new int[] {200,100,100,100,100,100,100,100,100};
			rt.setBody(new Table(rs,2,0,1));
		}else if(getSelzhuangtValue().getValue().equals("分厂")){
			ArrHeader=new String[2][9];
			ArrHeader[0]=new String[] {"厂别","计划(吨)","计划(吨)","实收(吨)","实收(吨)","亏欠(吨)","亏欠(吨)","到货率%","到货率%"};
			ArrHeader[1]=new String[] {"厂别","当月","累计","当月","累计","当月","累计","当月","累计"};
			ArrWidth=new int[] {200,100,100,100,100,100,100,100,100};
			rt.setBody(new Table(rs,2,0,1));
		}else if(getSelzhuangtValue().getValue().equals("分厂分矿")){
			ArrHeader=new String[2][10];
			ArrHeader[0]=new String[] {"厂别","矿别","计划(吨)","计划(吨)","实收(吨)","实收(吨)","亏欠(吨)","亏欠(吨)","到货率%","到货率%"};
			ArrHeader[1]=new String[] {"厂别","矿别","当月","累计","当月","累计","当月","累计","当月","累计"};
			ArrWidth=new int[] {150,200,100,100,100,100,100,100,100,100};
			rt.setBody(new Table(rs,2,0,2));
		}else if(getSelzhuangtValue().getValue().equals("分矿分厂")){
			ArrHeader=new String[2][10];
			ArrHeader[0]=new String[] {"矿别","厂别","计划(吨)","计划(吨)","实收(吨)","实收(吨)","亏欠(吨)","亏欠(吨)","到货率%","到货率%"};
			ArrHeader[1]=new String[] {"矿别","厂别","当月","累计","当月","累计","当月","累计","当月","累计"};
			ArrWidth=new int[] {200,150,100,100,100,100,100,100,100,100};
			rt.setBody(new Table(rs,2,0,2));
		}
		//设置页标题
		rt.setTitle("燃料供应月报("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,3,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(4,2,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃02表",Table.ALIGN_RIGHT);
		
		//数据
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(22);
		rt.body.setHeaderData(ArrHeader);//表头数据
		
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero=reportShowZero();
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,1,"制表:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private boolean _QueryClick = false;
	
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}

	}
	


	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString2("");
			visit.setDefaultTree(null);
			this.setTreeid(null);
			visit.setList1(null);
			getDiancmcModels();
			getMeikdqmcModels();
			
			setNianfValue(null);
			setYuefValue(null);
			setNianfModel(null);
			setYuefModel(null);
			setNianf2Value(null);
			setYuef2Value(null);
			setNianf2Model(null);
			setYuef2Model(null);
			setFenzModel(null);
			setFenzValue(null);
			setBBLXValue(null);
			setBBLXModel(null);
			setBBMCValue(null);
			setBBMCModel(null);
			visit.setString3("");
		}
		getToolbars();
		setUserName(visit.getRenymc());
//		setYuebmc(visit.getString2());
		blnIsBegin = true;
	}
	
	private boolean RiqCheck(){
		if(getBBLXValue().getValue().equals("汇总")){
		if(getNianfValue().getId()==getNianf2Value().getId()){
			if(getYuefValue().getId()==1){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		}else{
			return false;
		}
	}
	
	private boolean RiqMsg(String tablename){
		
		JDBCcon cn = new JDBCcon();
		String sql="select to_char(max(riq),'yyyymm') riq,to_char(max(riq),'yyyy-mm') riq2 from "+tablename;
		ResultSetList rsl=cn.getResultSetList(sql);
		String Riq="";
		String Riq2=getNianf2Value().getValue()+getYuef2Value().getValue();
		String Riq3="";
		
		while(rsl.next()){
			Riq=rsl.getString("riq");
			Riq3=rsl.getString("riq2");
		}
		cn.Close();
		
			if(Long.parseLong(Riq2)>Long.parseLong(Riq)){
				setMsg("请选择"+Riq3+" 以前的的数据,或请先生成！");
				return true;
			}else{
				return false;
			}
		
	}
	
	public void getToolbars() {
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("报表名称"));
		ComboBox bbmc = new ComboBox();
		bbmc.setTransform("BBMCDropDown");
		bbmc.setWidth(90);
		bbmc.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(bbmc);
		
		if (getBBMCValue().getId()!=4){
			tb1.addText(new ToolbarText("统计口径:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("SelzhuangtSelect");
			cb.setWidth(60);
			cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		}
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(50);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(40);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("至"));
		
		ComboBox nianf2 = new ComboBox();
		nianf2.setTransform("Nianf2DropDown");
		nianf2.setWidth(50);
		nianf2.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf2);
		tb1.addText(new ToolbarText("年"));
		ComboBox yuef2 = new ComboBox();
		yuef2.setTransform("Yuef2DropDown");
		yuef2.setWidth(40);
		yuef2.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef2);
		tb1.addText(new ToolbarText("月"));
		tb1.addText(new ToolbarText("-"));
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(80);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("报表类型"));
		ComboBox bblx = new ComboBox();
		bblx.setTransform("BBLXDropDown");
		bblx.setWidth(60);
		bblx.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(bblx);
		
		if(getBBLXValue().getId()==1){
		tb1.addText(new ToolbarText("分组方式"));
		ComboBox fenz = new ComboBox();
		fenz.setTransform("FenzDropDown");
		fenz.setWidth(60);
		fenz.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(fenz);
		}
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";document.Form0.submit();}");
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
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

//	private String m_yuebmc;
//	public void setYuebmc(String yuebmc){
//		m_yuebmc=yuebmc;
//	}
//	public String getYuebmc(){
//		return m_yuebmc;
//	}
	
	

//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc,bianm from diancxxb";
        ((Visit)getPage().getVisit()).setProSelectionModel10(new IDropDownModel(sql));

		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(_value);
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
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2003; i <= DateUtil.getYear(new Date())+1; i++) {
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
    	if  (_YuefValue!=Value){
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
    
	
	 // 年份下拉框
   private static IPropertySelectionModel _Nianf2Model;
   public IPropertySelectionModel getNianf2Model() {
       if (_Nianf2Model == null) {
           getNianf2Models();
       }
       return _Nianf2Model;
   }
   
	private IDropDownBean _Nianf2Value;
	
   public IDropDownBean getNianf2Value() {
       if (_Nianf2Value == null) {
           int _nianf = DateUtil.getYear(new Date());
           int _yuef = DateUtil.getMonth(new Date());
           if (_yuef == 1) {
               _nianf = _nianf - 1;
           }
           for (int i = 0; i < getNianf2Model().getOptionCount(); i++) {
               Object obj = getNianf2Model().getOption(i);
               if (_nianf == ((IDropDownBean) obj).getId()) {
                   _Nianf2Value = (IDropDownBean) obj;
                   break;
               }
           }
       }
       return _Nianf2Value;
   }
	
   public void setNianf2Value(IDropDownBean Value) {
   	if  (_Nianf2Value!=Value){
   		_Nianf2Value = Value;
   	}
   }

   public IPropertySelectionModel getNianf2Models() {
       List listNianf = new ArrayList();
       int i;
       for (i = 2003; i <= DateUtil.getYear(new Date())+1; i++) {
           listNianf.add(new IDropDownBean(i, String.valueOf(i)));
       }
       _Nianf2Model = new IDropDownModel(listNianf);
       return _Nianf2Model;
   }

   public void setNianf2Model(IPropertySelectionModel _value) {
       _Nianf2Model = _value;
   }

	// 月份下拉框
	private static IPropertySelectionModel _Yuef2Model;
	
	public IPropertySelectionModel getYuef2Model() {
	    if (_Yuef2Model == null) {
	        getYuef2Models();
	    }
	    return _Yuef2Model;
	}
	
	private IDropDownBean _Yuef2Value;
	
	public IDropDownBean getYuef2Value() {
	    if (_Yuef2Value == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuef2Model().getOptionCount(); i++) {
	            Object obj = getYuef2Model().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _Yuef2Value = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _Yuef2Value;
	}
	
	public void setYuef2Value(IDropDownBean Value) {
   	if  (_Yuef2Value!=Value){
   		_Yuef2Value = Value;
   	}
	}

   public IPropertySelectionModel getYuef2Models() {
       List listYuef = new ArrayList();
       for (int i = 1; i < 13; i++) {
           listYuef.add(new IDropDownBean(i, String.valueOf(i)));
       }
       _Yuef2Model = new IDropDownModel(listYuef);
       return _Yuef2Model;
   }

   public void setYuef2Model(IPropertySelectionModel _value) {
       _Yuef2Model = _value;
   }
    
private static IPropertySelectionModel _SelzhuangtModel;
	
	public IPropertySelectionModel getSelzhuangtModel() {
		if (_SelzhuangtModel == null) {
			getSelzhuangtModels();
		}
		return _SelzhuangtModel;
	}

	private IDropDownBean _SelzhuangtValue;

	public IDropDownBean getSelzhuangtValue() {
		Visit visit = (Visit)getPage().getVisit();
		if(_SelzhuangtValue==null){
			setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
		}
		if(visit.getString2().equals(RT_DR01)){
			setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
		}
		return _SelzhuangtValue;
	}
	
	private boolean _SelzhuangtChange=false;
	public void setSelzhuangtValue(IDropDownBean Value) {
		if (_SelzhuangtValue==Value) {
			_SelzhuangtChange = false;
		}else{
			_SelzhuangtValue = Value;
			_SelzhuangtChange = true;
		}
	}

	public IPropertySelectionModel getSelzhuangtModels() {
		List listSelzhuangt=new ArrayList();
		listSelzhuangt.add(new IDropDownBean(1, "分厂"));
		listSelzhuangt.add(new IDropDownBean(2, "分矿"));
		listSelzhuangt.add(new IDropDownBean(3, "分厂分矿"));
		listSelzhuangt.add(new IDropDownBean(4, "分矿分厂"));
		
		_SelzhuangtModel = new IDropDownModel(listSelzhuangt);
		return _SelzhuangtModel;
	}

	public void setSelzhuangtModel(IPropertySelectionModel _value) {
		_SelzhuangtModel = _value;
	}
//	分组
    private static IPropertySelectionModel _FenzModel;
    public IPropertySelectionModel getFenzModel() {
        if (_FenzModel == null) {
            getFenzModels();
        }
        return _FenzModel;
    }
    
    public void setFenzModel(IPropertySelectionModel _value) {
        _FenzModel = _value;
    }
    
	private IDropDownBean _FenzValue;
	
    public IDropDownBean getFenzValue() {
        if (_FenzValue == null) {
            _FenzValue = (IDropDownBean) getFenzModel().getOption(0);
        }
        return _FenzValue;
    }
	
    public void setFenzValue(IDropDownBean Value) {
    	if  (_FenzValue!=Value){
    		_FenzValue = Value;
    	}
    }

    public IPropertySelectionModel getFenzModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean(1, "当月/累计"));
    	list.add(new IDropDownBean(2, "当月"));
    	list.add(new IDropDownBean(3, "累计"));
    	
        _FenzModel = new IDropDownModel(list);
        return _FenzModel;
    }
    

//    报表类型
    public void setBBLXModel(IPropertySelectionModel _value) {
        _BBLXModel = _value;
    }
    private static IPropertySelectionModel _BBLXModel;
    public IPropertySelectionModel getBBLXModel() {
        if (_BBLXModel == null) {
            getBBLXModels();
        }
        return _BBLXModel;
    }
    
	private IDropDownBean _BBLXValue;
	
    public IDropDownBean getBBLXValue() {
        if (_BBLXValue == null) {
            _BBLXValue = (IDropDownBean) getBBLXModel().getOption(0);
        }
        return _BBLXValue;
    }
	
    public void setBBLXValue(IDropDownBean Value) {
    	if  (_BBLXValue!=Value){
    		_BBLXValue = Value;
    	}
    }

    public IPropertySelectionModel getBBLXModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean(1, "明细"));
    	list.add(new IDropDownBean(2, "汇总"));
    	
        _BBLXModel = new IDropDownModel(list);
        return _BBLXModel;
    }

//  报表名称
    public void setBBMCModel(IPropertySelectionModel _value) {
        _BBMCModel = _value;
    }
    private static IPropertySelectionModel _BBMCModel;
    public IPropertySelectionModel getBBMCModel() {
        if (_BBMCModel == null) {
            getBBMCModels();
        }
        return _BBMCModel;
    }
    
	private IDropDownBean _BBMCValue;
	
    public IDropDownBean getBBMCValue() {
        if (_BBMCValue == null) {
            _BBMCValue = (IDropDownBean) getBBMCModel().getOption(0);
        }
        return _BBMCValue;
    }
	
    public void setBBMCValue(IDropDownBean Value) {
    	if  (_BBMCValue!=Value){
    		_BBMCValue = Value;
    	}
    }

    public IPropertySelectionModel getBBMCModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean(1, "电生16-1表"));
    	list.add(new IDropDownBean(2, "调燃03表"));
    	list.add(new IDropDownBean(3, "调燃04表"));
    	list.add(new IDropDownBean(4, "调燃01表"));
//    	list.add(new IDropDownBean(5, "调燃02表"));
    	list.add(new IDropDownBean(6, "调燃08-1表"));
    	list.add(new IDropDownBean(7, "调燃08表"));
    	
        _BBMCModel = new IDropDownModel(list);
        return _BBMCModel;
    }
//煤矿地区_id	
	private IPropertySelectionModel _IMeikdqIdModel;
	public IPropertySelectionModel getIMeikdqIdModel() {
		if (_IMeikdqIdModel == null) {
			getIMeikdqIdModels();
		}
		return _IMeikdqIdModel;
	}
	
	public IPropertySelectionModel getIMeikdqIdModels() {
		
		String sql="";
		
		sql = "select id,meikdqbm from meikdqb order by meikdqmc";
		
		_IMeikdqIdModel = new IDropDownModel(sql);
		return _IMeikdqIdModel;
	}
//	
	//地区名称
	public boolean _diqumcchange = false;
	private IDropDownBean _DiqumcValue;

	public IDropDownBean getDiqumcValue() {
		if(_DiqumcValue==null){
			_DiqumcValue=(IDropDownBean)getIDiqumcModels().getOption(0);
		}
		return _DiqumcValue;
	}

	public void setDiqumcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiqumcValue != null) {
			id = _DiqumcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diqumcchange = true;
			} else {
				_diqumcchange = false;
			}
		}
		_DiqumcValue = Value;
	}

	private IPropertySelectionModel _IDiqumcModel;

	public void setIDiqumcModel(IPropertySelectionModel value) {
		_IDiqumcModel = value;
	}

	public IPropertySelectionModel getIDiqumcModel() {
		if (_IDiqumcModel == null) {
			getIDiqumcModels();
		}
		return _IDiqumcModel;
	}

	public IPropertySelectionModel getIDiqumcModels() {
		String sql="";
		
		sql = "select mk.meikdqbm,mk.meikdqmc from meikdqb mk order by meikdqmc";
		
		_IDiqumcModel = new IDropDownModel(sql);
		return _IDiqumcModel;
	}
	

	
	public String getcontext() {
			return "";
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).get() + "';";
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
//		setMsg("");
	}
}