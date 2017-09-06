package com.zhiren.gs.bjdt.monthreport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rezcquery extends BasePage{


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
//*************删除前的确认*****************
	private String getConfirm(String title ,String content,String tapsetryId){
		return "Ext.MessageBox.confirm('"+title+"', '"+content+"', function(btn) {"+
                " if(btn=='yes'){"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";document.getElementById('"+tapsetryId+"').click();}else{return;}"+
                "})";

	}
	
	private static String REZC_QUERY="rezcquery";
	private static String REZC_HB="rezchb";
//	
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
	public String getPrintTable(){
		setMsg(null);
		if(!blnIsBegin){
			return "";
		}
		blnIsBegin=false;
		String contantString="";
		if(mstrReportName.equals(REZC_QUERY)){
			switch(new Long(this.getSelzhuangtValue().getId()).intValue()){
				case 1:
				case 4:
				case 2: contantString= getDany_Leij();break;
				case 3: 
				contantString=getHunh_Shij();break;
				default: contantString="无此报表";
		
			}
		}else if(mstrReportName.equals(REZC_HB)){
			contantString = getRezchb();
		}else{
			contantString="无此报表";
		}
		return contantString;
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
		}
		cn.Close();
		return condition;
	}
	
	
	private String getDany_Leij(){
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		long lngDiancId=getDiancxxbId();//电厂信息表id
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月";
		
		
		String _Danwqc=getTianzdwQuanc();
		String condition = getDiancCondition();
		String fenx="";
		String riqtiaojian="fenx='"+fenx+"' and riq = to_date('"+strDate+"','yyyy-mm-dd')";
		String riqtiaojian2="r.riq>=to_date('"+strDate+"','yyyy-mm-dd') and r.riq<add_months(to_date('"+strDate+"','yyyy-mm-dd'),1)";
		if(this.getSelzhuangtValue().getId()==1){
			fenx="本月";	
			riqtiaojian="fenx='"+fenx+"' and riq = to_date('"+strDate+"','yyyy-mm-dd')";
		}else if(this.getSelzhuangtValue().getId()==2){
			fenx="累计";
			riqtiaojian="fenx='"+fenx+"' and riq = to_date('"+strDate+"','yyyy-mm-dd')";
			 riqtiaojian2="r.riq>=to_date('"+this.getNianfValue().getValue()+"-1-1','yyyy-mm-dd') and r.riq<add_months(to_date('"+strDate+"','yyyy-mm-dd'),1)";
		}else if(this.getSelzhuangtValue().getId()==4){
			String endDate=this.getEndNianfValue().getValue()+"-"+this.getEndYuefValue().getValue()+"-01";
			fenx="本月";
			riqtiaojian="fenx='"+fenx+"' and riq between to_date('"+strDate+"','yyyy-mm-dd') and to_date('"+endDate+"','yyyy-mm-dd')";
			riqtiaojian2="r.riq>=to_date('"+strDate+"','yyyy-mm-dd') and r.riq<add_months(to_date('"+endDate+"','yyyy-mm-dd'),1)";
		}
		
		String dcid="";
		if(!this.getTreeid().equals("1")){
			dcid="and dc.id="+this.getTreeid()+"\n";
		}

		sbsql.append("	select\n"); 
		sbsql.append("	a.电厂名,a.结算数量,a.结算热值,a.入厂数量,a.入厂热量,a.入厂水分,a.入炉数量,a.入炉热值,a.入炉水分,\n");
		sbsql.append("	 --水分调整前 \n");
		sbsql.append("      case\n");
		sbsql.append("	     when a.入炉数量 = 0 or a.入厂数量 = 0 then\n");
		sbsql.append("     '-'\n");
		sbsql.append("else\n");
		sbsql.append("to_char( a.入厂热量- a.入炉热值)\n");
		sbsql.append("      end as 水分调整前热值差,\n");
		sbsql.append(" --水分调整后\n");
		sbsql.append("     case\n");
		sbsql.append("      when a.入炉数量 = 0 or a.入厂数量 = 0 then\n");
		sbsql.append("			          '-'\n");
		sbsql.append("				         else\n");
		sbsql.append("				          to_char(round(a.入厂热量 - a.入炉热值 * (100 - a.入厂水分) / (100 - a.入炉水分), 3))\n");
		sbsql.append("		       end as 水分调整后热值差,\n");
		sbsql.append("--结算-入炉\n");
		sbsql.append("    a. 结算热值-a.入炉热值  结算入炉热值差\n");
		sbsql.append("from(\n");
		sbsql.append(" select \n");
		sbsql.append("decode(grouping(dc.dianclbb_id)+grouping(dc.mingc),2,'大唐国际发电股份有限公司',1,'★'||max(lb.mingc)||'小计',dc.mingc) as 电厂名,\n");
		sbsql.append(" --08表\n");
		sbsql.append(" sum(d08h.meil) as 结算数量, \n");
		sbsql.append(" case when sum(d08h.meil)=0 or sum(d08h.meil) is null then 0 else round(sum(d08h.meilRez)/sum(d08h.meil),2) end as 结算热值,\n");
		sbsql.append(" --04表\n");
		sbsql.append(" sum(d16h.jincsl) as 入厂数量,\n");
		sbsql.append(" decode(sum(d16h.JINCSL),0,0,round(sum(d16h.jincslChangffrl)/sum(d16h.JINCSL),2)) as 入厂热量,\n");
		sbsql.append(" decode(sum(d16h.JINCSL),0,0,round(sum(d16h.JINCSLCHANGFSF)/sum(d16h.JINCSL),1)) as 入厂水分,\n");
		sbsql.append("  --01表\n");
		sbsql.append("  sum(d01h.meithyhj) as 入炉数量,\n");
		sbsql.append("   round(fun_zonghrlfrl(sum(d01h.biaozmlfd), sum(d01h.biaozmlgr), sum(d01h.shiyhyfd), sum(d01h.shiyhygr), sum(d01h.meithyfd),sum(d01h.meithygr)),2)  as 入炉热值,\n");
		sbsql.append(" --rezcb\n");
		sbsql.append("   round(decode(sum(rh.rulsl),0,0,sum(rh.rulslRulsf)/sum(rh.rulsl)),1) as 入炉水分\n");
		sbsql.append("	from diancxxb dc,(select * from dianckjpxb where kouj='月报') px,dianclbb lb,\n");
		sbsql.append("	  (select\n");
		sbsql.append("     diancxxb_id, sum(d08.meil) meil,sum(d08.meil*d08.rez) meilRez \n");
		sbsql.append("     from diaor08bb d08 where "+riqtiaojian+" group by diancxxb_id   ) d08h,  \n");    
		sbsql.append("		  (select  diancxxb_id ,sum(d16.kuangfgyl) jincsl,sum(d16.kuangfgyl * d16.farl) jincslChangffrl,sum(d16.kuangfgyl * d16.shuif) JINCSLCHANGFSF\n");
		sbsql.append("      from diaor16bb d16  where "+riqtiaojian+" group by diancxxb_id )d16h,\n");
		sbsql.append("	  (select  diancxxb_id,  sum(meithyfd+meithygr) meithyhj,sum(biaozmlfd) biaozmlfd, sum(biaozmlgr) biaozmlgr,\n");
		sbsql.append("         sum(shiyhyfd) shiyhyfd, sum(shiyhygr) shiyhygr, sum(meithyfd) meithyfd,sum(meithygr) meithygr\n");
		sbsql.append("      from diaor01bb  where "+riqtiaojian+" group by diancxxb_id )d01h,\n");
		sbsql.append("		  (select  diancxxb_id,sum(r.rulsl) rulsl,sum(r.rulsl*r.rulsf) rulslRulsf--,sum(rh.rucsl) as rucsl,sum(r.rulsl) rulsl\n");
		sbsql.append("       from rezcb r \n");
		sbsql.append("        where "+riqtiaojian2+" group by diancxxb_id )rh  \n");       
		sbsql.append("		where dc.id=d08h.diancxxb_id(+) and dc.id=d16h.diancxxb_id(+) and dc.id=d01h.diancxxb_id(+) and dc.id=rh.diancxxb_id(+)\n");
		sbsql.append("      and lb.id(+)=dc.dianclbb_id and px.diancxxb_id=dc.id "+dcid+"\n");
		
		if(this.getTreeid().equals("1")){//单厂查询不再汇总
			sbsql.append("		group by rollup(dc.dianclbb_id,dc.mingc) order by grouping(dc.dianclbb_id) desc ,max(lb.xuh),grouping(dc.mingc) desc,max(px.xuh),dc.mingc\n");
		}else{
			sbsql.append("		group by (dc.dianclbb_id,dc.mingc) order by grouping(dc.dianclbb_id) desc ,max(lb.xuh),grouping(dc.mingc) desc,max(px.xuh),dc.mingc\n");
		}
		sbsql.append("		 )a\n");
		
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		//定义表头数据
		 String ArrHeader[][]=new String[2][12];
		 ArrHeader[0]=new String[] {"单位","结算","结算","入厂煤","入厂煤","入厂煤","入炉煤","入炉煤","入炉煤","热值差(mj/kg)","热值差(mj/kg)","热值差(mj/kg)"};
		 ArrHeader[1]=new String[] {"单位","数量(t)","Qnet,ar<br>(MJ/kg)","数量(t)","Qnet,ar<br>(MJ/kg)","Mt(%)","数量(t)","Qnet,ar<br>(MJ/kg)","Mt(%)","水分调整前","水分调整后","结算-入炉"};
//		  列宽
		 int ArrWidth[]=new int[] {100,60,50,60,50,50,55,45,45,50,50,50};
		
		 //设置页标题
		rt.setTitle("燃料系统热值差统计汇总表("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,7,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(13,2,strMonth,Table.ALIGN_CENTER);
		if(!(this.getSelzhuangtValue().getId()==4)){
		    rt.setDefaultTitle(rt.title.getCols()-1,2,"时间:"+strMonth,Table.ALIGN_RIGHT);
		}else{
			rt.setDefaultTitle(rt.title.getCols()-3,4,"时间:"+strMonth+"--"+getEndNianfValue().getValue()+"年"+getEndYuefValue().getValue()+"月",Table.ALIGN_RIGHT);
		}
		//数据
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(28);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.ShowZero=false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.setColAlign(2,Table.ALIGN_RIGHT);
		rt.body.setColAlign(3,Table.ALIGN_RIGHT);
		rt.body.setColAlign(4,Table.ALIGN_RIGHT);
		rt.body.setColFormat(3,"0.00");
		rt.body.setColFormat(5,"0.00");
		rt.body.setColFormat(6,"0.0");
		rt.body.setColFormat(8,"0.00");
		rt.body.setColFormat(9,"0.0");
		rt.body.setColFormat(10,"0.000");
		rt.body.setColFormat(11,"0.000");
		rt.body.setColFormat(12,"0.000");
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
//		rt.setDefautlFooter(4,2,"制表:"+((Visit)this.getPage().getVisit()).getRenymc(),Table.ALIGN_LEFT);
//		rt.setDefautlFooter(9,1,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(11,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();	
	}
	
	private String getHunh_Shij(){
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		long lngDiancId=getDiancxxbId();//电厂信息表id
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月";
		 
		String _Danwqc=getTianzdwQuanc();
		String condition = getDiancCondition();
		String fenx="";
	
		String dcid="";
		if(!this.getTreeid().equals("1")){
			dcid="and dc.id="+this.getTreeid()+"\n";
		}


		sbsql.append("select\n"); 
		sbsql.append("a.电厂名,decode(1,1,a.本月或累计,a.本月或累计) 本月或累计,a.结算数量,a.结算热值,a.入厂数量,a.入厂热量,a.入厂水分,a.入炉数量,a.入炉热值,a.入炉水分,\n");
		sbsql.append(" --水分调整前 \n");
	    sbsql.append("case\n");
		sbsql.append("when a.入炉数量 = 0 or a.入厂数量 = 0 then\n");
		sbsql.append("  '-'\n");
		sbsql.append("else\n");
		sbsql.append("   to_char( a.入厂热量- a.入炉热值)\n");
		sbsql.append("   end as 水分调整前热值差,\n");
	    sbsql.append("  --水分调整后\n");
		sbsql.append("   case\n");
	    sbsql.append("        when a.入炉数量 = 0 or a.入厂数量 = 0 then\n");
	    sbsql.append("        '-'\n");
	    sbsql.append("       else\n");
	    sbsql.append("      to_char(round(a.入厂热量 - a.入炉热值 * (100 - a.入厂水分) / (100 - a.入炉水分), 3))\n");
		sbsql.append("    end as 水分调整后热值差,\n");
		sbsql.append(" --结算-入炉\n");
		sbsql.append("   a. 结算热值-a.入炉热值  结算入炉热值差\n");
		sbsql.append("	from(\n");
		sbsql.append("	 select \n");
		sbsql.append("    decode(grouping(dc.dianclbb_id)+grouping(dc.mingc),2,'大唐国际发电股份有限公司',1,'★'||max(lb.mingc)||'小计',dc.mingc) as 电厂名,\n");
		sbsql.append("   decode(1,1,px.fenx,px.fenx) as 本月或累计,\n");
		sbsql.append("    --08表\n");
		sbsql.append("  sum(d08h.meil) as 结算数量, \n");
		sbsql.append("   case when sum(d08h.meil)=0 or sum(d08h.meil) is null then 0 else round(sum(d08h.meilRez)/sum(d08h.meil),2) end as 结算热值,\n");
		sbsql.append("  --04表\n");
		sbsql.append("  sum(d16h.jincsl) as 入厂数量,\n");
		sbsql.append("   decode(sum(d16h.JINCSL),0,0,round(sum(d16h.jincslChangffrl)/sum(d16h.JINCSL),2)) as 入厂热量,\n");
		sbsql.append("    decode(sum(d16h.JINCSL),0,0,round(sum(d16h.JINCSLCHANGFSF)/sum(d16h.JINCSL),1)) as 入厂水分,\n");
		sbsql.append("    --01表\n");
		sbsql.append("    sum(d01h.meithyhj) as 入炉数量,\n");
		sbsql.append("    fun_zonghrlfrl(sum(d01h.biaozmlfd), sum(d01h.biaozmlgr), sum(d01h.shiyhyfd), sum(d01h.shiyhygr), sum(d01h.meithyfd),sum(d01h.meithygr))  as 入炉热值,\n");
		sbsql.append("    --rezcb\n");
		sbsql.append("     round(decode(sum(rh.rulsl),0,0,sum(rh.rulslRulsf)/sum(rh.rulsl)),1) as 入炉水分 \n");
		sbsql.append("from diancxxb dc,(select * from dianckjpxb,(select decode(1,1,'本月','本月') fenx  from dual union select decode(1,1,'累计','累计') fenx from dual) where kouj='月报') px,dianclbb lb,\n");
		sbsql.append(" (select\n");
		sbsql.append("     fenx, diancxxb_id, sum(d08.meil) meil,sum(d08.meil*d08.rez) meilRez \n");
		sbsql.append("       from diaor08bb d08 where  riq=to_date('"+strDate+"','yyyy-mm-dd') group by diancxxb_id,fenx \n");
		sbsql.append("         ) d08h,      \n");
		sbsql.append(" (select fenx, diancxxb_id ,sum(d16.kuangfgyl) jincsl,sum(d16.kuangfgyl * d16.farl) jincslChangffrl,sum(d16.kuangfgyl * d16.shuif) JINCSLCHANGFSF\n");
		sbsql.append("         from diaor16bb d16  where riq=to_date('"+strDate+"','yyyy-mm-dd') group by diancxxb_id,fenx )d16h,\n");
		sbsql.append(" (select fenx, diancxxb_id,  sum(meithyfd+meithygr) meithyhj,sum(biaozmlfd) biaozmlfd, sum(biaozmlgr) biaozmlgr,\n");
		sbsql.append("           sum(shiyhyfd) shiyhyfd, sum(shiyhygr) shiyhygr, sum(meithyfd) meithyfd,sum(meithygr) meithygr\n");
		sbsql.append("   from diaor01bb  where  riq=to_date('"+strDate+"','yyyy-mm-dd') group by diancxxb_id,fenx )d01h,\n");
		sbsql.append(" (select decode(1,1,'本月','本月') fenx , diancxxb_id,sum(r.rulsl) rulsl,sum(r.rulsl*r.rulsf) rulslRulsf--,sum(rh.rucsl) as rucsl,sum(r.rulsl) rulsl\n");
		sbsql.append("     from rezcb r \n");
		sbsql.append("     where r.riq>=to_date('"+strDate+"','yyyy-mm-dd') and r.riq<add_months(to_date('"+strDate+"','yyyy-mm-dd'),1) group by diancxxb_id \n");
		sbsql.append("     union all\n");
		sbsql.append("  select decode(1,1,'累计','累计') fenx, diancxxb_id,sum(r1.rulsl) rulsl,sum(r1.rulsl*r1.rulsf) rulslRulsf  \n");
		sbsql.append("		           from rezcb  r1\n");
		sbsql.append("       where r1.riq>=to_date('"+getNianfValue().getValue()+"-01-01','yyyy-mm-dd') and r1.riq<add_months(to_date('"+strDate+"','yyyy-mm-dd'),1) group by diancxxb_id   \n"); 
		sbsql.append("   )rh   \n");      
		sbsql.append("	where px.diancxxb_id=d08h.diancxxb_id(+) and px.diancxxb_id=d16h.diancxxb_id(+) and px.diancxxb_id=d01h.diancxxb_id(+) and px.diancxxb_id=rh.diancxxb_id(+)\n");
		sbsql.append("     and lb.id=dc.dianclbb_id and px.diancxxb_id=dc.id   and px.fenx=d08h.fenx(+) and px.fenx=d16h.fenx(+)\n");
		sbsql.append("       and px.fenx=d01h.fenx(+) and px.fenx=rh.fenx(+) "+dcid+"\n");
		
		if(this.getTreeid().equals("1")){
			sbsql.append("	group by rollup(px.fenx,dc.dianclbb_id,dc.mingc)having grouping(px.fenx)=0\n");
		}else{
			sbsql.append("	group by (px.fenx,dc.dianclbb_id,dc.mingc)\n");
		}
		sbsql.append("	 order by grouping(dc.dianclbb_id) desc ,max(lb.xuh),grouping(dc.mingc) desc,max(px.xuh),dc.mingc,px.fenx\n");
		sbsql.append("	)a\n");

		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		
		//定义表头数据
		 String ArrHeader[][]=new String[2][13];
		 ArrHeader[0]=new String[] {"单位","当月<br>累计","结算","结算","入厂煤","入厂煤","入厂煤","入炉煤","入炉煤","入炉煤","热值差(mj/kg)","热值差(mj/kg)","热值差(mj/kg)"};
		 ArrHeader[1]=new String[] {"单位","当月<br>累计","数量(t)","Qnet,ar<br>(MJ/kg)","数量(t)","Qnet,ar<br>(MJ/kg)","Mt(%)","数量(t)","Qnet,ar<br>(MJ/kg)","Mt(%)","水分调整前","水分调整后","结算-入炉"};
//		列宽  {100,60,50,60,50,50,55,45,45,50,50,50};
		 int ArrWidth[]=new int[] {100,40,60,50,60,50,50,55,45,45,50,50,50};
		 System.out.println();
		
		 //设置页标题
		rt.setTitle("燃料系统热值差统计汇总表("+getSelzhuangtValue().getValue()+")",ArrWidth);
		rt.setDefaultTitle(1,7,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
//		rt.setDefaultTitle(1,7,"填报单位:大唐国际发电股份有限公司燃料管理部",Table.ALIGN_LEFT);
		rt.setDefaultTitle(13,2,strMonth,Table.ALIGN_CENTER);
		
		rt.setDefaultTitle(rt.title.getCols()-2,2,"时间:"+strMonth,Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(26);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.ShowZero=false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
//		rt.body.setColAlign(2,Table.ALIGN_RIGHT);
		rt.body.setColFormat(4,"0.00");
		rt.body.setColFormat(6,"0.00");
		rt.body.setColFormat(7,"0.0");
		rt.body.setColFormat(9,"0.00");
		rt.body.setColFormat(10,"0.0");
		rt.body.setColFormat(11,"0.000");
		rt.body.setColFormat(12,"0.000");
		rt.body.setColFormat(13,"0.000");
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(2,2,"批准:",Table.ALIGN_CENTER);
//		rt.setDefautlFooter(6,2,"制表:"+((Visit)this.getPage().getVisit()).getRenymc(),Table.ALIGN_LEFT);
//		rt.setDefautlFooter(8,1,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(12,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();	
	}
	
	private String getRezchb(){//热值差环比
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		long lngDiancId=getDiancxxbId();//电厂信息表id
		
		String strFristDay = "to_date('"+getNianfValue().getValue() +"-01-01','yyyy-mm-dd')";
		String strDate = "to_date('"+getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01','yyyy-mm-dd')";
		
		String strHuanbFristDay = "to_date('"+getNianfValue().getId() +"-01-01','yyyy-mm-dd')";
		String strHuanbRiq = "add_months(to_date('"+getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01','yyyy-mm-dd'),-1)";
		if(getYuefValue().getId()==1){
			strHuanbFristDay = "to_date('"+(getNianfValue().getId()-1) +"-01-01','yyyy-mm-dd')";
		}
		
		if(getBaoblxValue().getValue().equals("季度")){
			if(getYuefValue().getId()==1 || getYuefValue().getId()==2 || getYuefValue().getId()==3){
				strHuanbFristDay = "to_date('"+(getNianfValue().getId()-1) +"-01-01','yyyy-mm-dd')";
				strDate = "to_date('"+getNianfValue().getValue() +"-03-01','yyyy-mm-dd')";
				strHuanbRiq = "to_date('"+(getNianfValue().getId()-1) +"-12-01','yyyy-mm-dd')";
				
			}else if(getYuefValue().getId()==4 || getYuefValue().getId()==5 || getYuefValue().getId()==6){
//				strHuanbFristDay = "to_date('"+getNianfValue().getValue() +"-01-01','yyyy-mm-dd')";
				strDate="to_date('"+getNianfValue().getValue() +"-06-01','yyyy-mm-dd')";
				strHuanbRiq = "to_date('"+getNianfValue().getId() +"-03-01','yyyy-mm-dd')";
				
			}else if(getYuefValue().getId()==7 || getYuefValue().getId()==8 || getYuefValue().getId()==9){
//				strHuanbFristDay = "to_date('"+getNianfValue().getValue() +"-01-01','yyyy-mm-dd')";
				strDate="to_date('"+getNianfValue().getValue() +"-09-01','yyyy-mm-dd')";
				strHuanbRiq = "to_date('"+getNianfValue().getId() +"-06-01','yyyy-mm-dd')";
				
			}else if(getYuefValue().getId()==10 || getYuefValue().getId()==11 || getYuefValue().getId()==12){
//				strHuanbFristDay = "to_date('"+getNianfValue().getValue() +"-01-01','yyyy-mm-dd')";
				strDate="to_date('"+getNianfValue().getValue() +"-12-01','yyyy-mm-dd')";
				strHuanbRiq = "to_date('"+getNianfValue().getId() +"-09-01','yyyy-mm-dd')";
			}
		}else{//默认为累计
			strDate = "to_date('"+getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01','yyyy-mm-dd')";
			strHuanbRiq = "add_months(to_date('"+getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01','yyyy-mm-dd'),-1)";
		}
		String strMonth=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月";
		 
		String _Danwqc=getTianzdwQuanc();
//		String condition = getDiancCondition();
		String dcid="";
		if(!this.getTreeid().equals("1")){
			dcid="and dc.id="+this.getTreeid()+"\n";
		}
		String sql = "select 电厂名,\n"
				
				+ "       bq热值差调整前,bq热值差调整后,bq结算入炉热值差, hb热值差调整前,hb热值差调整后,hb结算入炉热值差,\n"
				+"bq热值差调整前-hb热值差调整前 as 调整前环比,bq热值差调整后-hb热值差调整后 as 调整后环比,\n"
		        + "       bq结算入炉热值差-hb结算入炉热值差 as 结算入炉热值差环比\n"
				+ "  from (\n"
				+ " select decode(grouping(dc.dianclbb_id)+grouping(dc.mingc),2,'大唐国际发电股份有限公司',1,'★'||max(lb.mingc)||'小计',dc.mingc) as 电厂名,\n"
				+ "/*  case when sum(d08bq.meil)=0 or sum(d08bq.meil) is null then 0 else round(sum(d08bq.meilRez)/sum(d08bq.meil),2) end as 结算热值,\n"
				+ "    decode(sum(d16bq.JINCSL),0,0,round(sum(d16bq.jincslChangffrl)/sum(d16bq.JINCSL),2)) as 入厂热量,\n"
				+ "    decode(sum(d16bq.JINCSL),0,0,round(sum(d16bq.JINCSLCHANGFSF)/sum(d16bq.JINCSL),1)) as 入厂水分,\n"
				+ "    round(fun_zonghrlfrl(sum(d01bq.biaozmlfd), sum(d01bq.biaozmlgr), sum(d01bq.shiyhyfd), sum(d01bq.shiyhygr), sum(d01bq.meithyfd),sum(d01bq.meithygr)),2)  as 入炉热量,\n"
				+ "    round(decode(sum(rlsfbq.rulsl),0,0,sum(rlsfbq.rulslRulsf)/sum(rlsfbq.rulsl)),1) as 入炉水分   */\n"
				+ "--本期--水分调整前热值差\n"
				+ "     nvl(decode(sum(d16bq.JINCSL),0,0,round(sum(d16bq.jincslChangffrl)/sum(d16bq.JINCSL),2)),0)\n"
				+ "     -nvl(round(fun_zonghrlfrl(sum(d01bq.biaozmlfd), sum(d01bq.biaozmlgr), sum(d01bq.shiyhyfd), sum(d01bq.shiyhygr), sum(d01bq.meithyfd),sum(d01bq.meithygr)),2),0) as bq热值差调整前,\n"
				+ "--本期--水分调整后热值差\n"
				+ "     round(nvl(decode(sum(d16bq.JINCSL),0,0,round(sum(d16bq.jincslChangffrl)/sum(d16bq.JINCSL),2)),0)\n"
				+ "     -nvl(round(fun_zonghrlfrl(sum(d01bq.biaozmlfd), sum(d01bq.biaozmlgr), sum(d01bq.shiyhyfd), sum(d01bq.shiyhygr), sum(d01bq.meithyfd),sum(d01bq.meithygr)),2),0)\n"
				+ "     *(100-nvl(decode(sum(d16bq.JINCSL),0,0,round(sum(d16bq.JINCSLCHANGFSF)/sum(d16bq.JINCSL),1)),0))\n"
				+ "     /(100-nvl(round(decode(sum(rlsfbq.rulsl),0,0,sum(rlsfbq.rulslRulsf)/sum(rlsfbq.rulsl)),1),0)),3) as bq热值差调整后,\n"
				+ "--本期--结算入炉热值差\n"
				+ "     nvl(case when sum(d08bq.meil)=0 or sum(d08bq.meil) is null then 0 else round(sum(d08bq.meilRez)/sum(d08bq.meil),2) end,0)\n"
				+ "     -nvl(round(fun_zonghrlfrl(sum(d01bq.biaozmlfd), sum(d01bq.biaozmlgr), sum(d01bq.shiyhyfd), sum(d01bq.shiyhygr), sum(d01bq.meithyfd),sum(d01bq.meithygr)),2),0) as bq结算入炉热值差,\n"
				+ "\n"
				+ "--环比--水分调整前热值差\n"
				+ "     nvl(decode(sum(d16hb.JINCSL),0,0,round(sum(d16hb.jincslChangffrl)/sum(d16hb.JINCSL),2)),0)\n"
				+ "     -nvl(round(fun_zonghrlfrl(sum(d01hb.biaozmlfd), sum(d01hb.biaozmlgr), sum(d01hb.shiyhyfd), sum(d01hb.shiyhygr), sum(d01hb.meithyfd),sum(d01hb.meithygr)),2),0) as hb热值差调整前,\n"
				+ "--环比--水分调整后热值差\n"
				+ "     round(nvl(decode(sum(d16hb.JINCSL),0,0,round(sum(d16hb.jincslChangffrl)/sum(d16hb.JINCSL),2)),0)\n"
				+ "     -nvl(round(fun_zonghrlfrl(sum(d01hb.biaozmlfd), sum(d01hb.biaozmlgr), sum(d01hb.shiyhyfd), sum(d01hb.shiyhygr), sum(d01hb.meithyfd),sum(d01hb.meithygr)),2),0)\n"
				+ "     *(100-nvl(decode(sum(d16hb.JINCSL),0,0,round(sum(d16hb.JINCSLCHANGFSF)/sum(d16hb.JINCSL),1)),0))\n"
				+ "     /(100-nvl(round(decode(sum(rlsfhb.rulsl),0,0,sum(rlsfhb.rulslRulsf)/sum(rlsfhb.rulsl)),1),0)),3) as hb热值差调整后,\n"
				+ "--环比--结算入炉热值差\n"
				+ "     nvl(case when sum(d08hb.meil)=0 or sum(d08hb.meil) is null then 0 else round(sum(d08hb.meilRez)/sum(d08hb.meil),2) end,0)\n"
				+ "     -nvl(round(fun_zonghrlfrl(sum(d01hb.biaozmlfd), sum(d01hb.biaozmlgr), sum(d01hb.shiyhyfd), sum(d01hb.shiyhygr), sum(d01hb.meithyfd),sum(d01hb.meithygr)),2),0) as hb结算入炉热值差\n"
				+ "\n"
				+ "from diancxxb dc,(select * from dianckjpxb where kouj='月报') px,dianclbb lb,\n"
				+ "  --累计、季度\n"
				+ "  (select diancxxb_id, sum(d08.meil) meil,sum(d08.meil*d08.rez) meilRez\n"
				+ "     from diaor08bb d08 where fenx='累计'and riq="+strDate+" group by diancxxb_id ) d08bq,\n"
				+ "  (select diancxxb_id ,sum(d16.kuangfgyl) jincsl,sum(d16.kuangfgyl * d16.farl) jincslChangffrl,\n"
				+ "          sum(d16.kuangfgyl * d16.shuif) JINCSLCHANGFSF\n"
				+ "     from diaor16bb d16  where fenx='累计'and riq="+strDate+" group by diancxxb_id )d16bq,\n"
				+ "  (select diancxxb_id,  sum(meithyfd+meithygr) meithyhj,sum(biaozmlfd) biaozmlfd, sum(biaozmlgr) biaozmlgr,\n"
				+ "          sum(shiyhyfd) shiyhyfd, sum(shiyhygr) shiyhygr, sum(meithyfd) meithyfd,sum(meithygr) meithygr\n"
				+ "     from diaor01bb  where fenx='累计'and riq="+strDate+" group by diancxxb_id )d01bq,\n"
				+ "  (select diancxxb_id,sum(r.rulsl) rulsl,sum(r.rulsl*r.rulsf) rulslRulsf\n"
				+ "     from rezcb r  where r.riq>="+strFristDay+" \n"
				+ "      and r.riq<add_months("+strDate+",1) group by diancxxb_id ) rlsfbq,\n"
				+ "\n"
				+ "   --环比\n"
				+ "  (select diancxxb_id, sum(d08.meil) meil,sum(d08.meil*d08.rez) meilRez\n"
				+ "     from diaor08bb d08 where fenx='累计'and riq="+strHuanbRiq+" group by diancxxb_id ) d08hb,\n"
				+ "  (select diancxxb_id ,sum(d16.kuangfgyl) jincsl,sum(d16.kuangfgyl * d16.farl) jincslChangffrl,\n"
				+ "          sum(d16.kuangfgyl * d16.shuif) JINCSLCHANGFSF\n"
				+ "     from diaor16bb d16  where fenx='累计'and riq="+strHuanbRiq+" group by diancxxb_id )d16hb,\n"
				+ "  (select diancxxb_id,  sum(meithyfd+meithygr) meithyhj,sum(biaozmlfd) biaozmlfd, sum(biaozmlgr) biaozmlgr,\n"
				+ "          sum(shiyhyfd) shiyhyfd, sum(shiyhygr) shiyhygr, sum(meithyfd) meithyfd,sum(meithygr) meithygr\n"
				+ "     from diaor01bb  where fenx='累计'and riq="+strHuanbRiq+" group by diancxxb_id )d01hb,\n"
				+ "  (select diancxxb_id,sum(r.rulsl) rulsl,sum(r.rulsl*r.rulsf) rulslRulsf\n"
				+ "     from rezcb r  where r.riq>="+strHuanbFristDay+" \n"
				+ "      and r.riq<add_months("+strHuanbRiq+",1) group by diancxxb_id ) rlsfhb\n"
				+ "\n"
				+ "where dc.id=d08bq.diancxxb_id(+) and dc.id=d16bq.diancxxb_id(+)\n"
				+ "  and dc.id=d01bq.diancxxb_id(+) and dc.id=rlsfbq.diancxxb_id(+)\n"
				+ "  and dc.id=d08hb.diancxxb_id(+) and dc.id=d16hb.diancxxb_id(+)\n"
				+ "  and dc.id=d01hb.diancxxb_id(+) and dc.id=rlsfhb.diancxxb_id(+)\n"
				+ "  and lb.id=dc.dianclbb_id and px.diancxxb_id=dc.id "+dcid+"\n";
				if(this.getTreeid().equals("1")){//单厂查询不再汇总
					sql+= "group by rollup(dc.dianclbb_id,dc.mingc)\n";
				}else{
					sql+="group by (dc.dianclbb_id,dc.mingc)\n";
				}
				sql+= "order by grouping(dc.dianclbb_id) desc ,max(lb.xuh),grouping(dc.mingc) desc,max(px.xuh),dc.mingc )";
		
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		
		//定义表头数据
		 String ArrHeader[][]=new String[2][10];
		 ArrHeader[0]=new String[] {"单位名称","本期热值差(MJ/KG)","本期热值差(MJ/KG)","本期热值差(MJ/KG)","上期热值差(MJ/KG)","上期热值差(MJ/KG)","上期热值差(MJ/KG)","热值差环比(MJ/KG)","热值差环比(MJ/KG)","热值差环比(MJ/KG)"};
		 ArrHeader[1]=new String[] {"单位名称","入厂-入炉<br>(调水前)","入厂-入炉<br>(调水后)","结算-入炉","入厂-入炉<br>(调水前)","入厂-入炉<br>(调水后)","结算-入炉","入厂-入炉<br>(调水前)","入厂-入炉<br>(调水后)","结算-入炉"};
//		列宽
		 int ArrWidth[]=new int[] {130,65,65,65,65,65,65,65,65,65};
		
		 //设置页标题
		rt.setTitle(getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月"+getBaoblxValue().getValue()+"热值差环比",ArrWidth);
		rt.setDefaultTitle(1,4,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(9,2,strMonth,Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,2,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(26);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.ShowZero=false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.setColAlign(2,Table.ALIGN_RIGHT);
		rt.body.setColFormat(2,"0.000");
		rt.body.setColFormat(3,"0.000");
		rt.body.setColFormat(4,"0.000");
		rt.body.setColFormat(5,"0.000");
		rt.body.setColFormat(6,"0.000");
		rt.body.setColFormat(7,"0.000");
		rt.body.setColFormat(8,"0.000");
		rt.body.setColFormat(9,"0.000");
		rt.body.setColFormat(10,"0.000");
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(4,1,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(7,1,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(9,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
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
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
//			visit.setProSelectionModel3(null);
//			visit.setDropDownBean3(null);
			this.setSelzhuangtModel(null);
			this.setSelzhuangtValue(null);
			visit.setString2("");
			this.setTreeid(null);
			visit.setList1(null);
			getDiancmcModels();
			this.setTreeid(null);
			this.setNianfModel(null);
			this.setNianfValue(null);
			this.setYuefModel(null);
			this.setYuefValue(null);
			this.setEndNianfModel(null);
			this.setEndNianfValue(null);
			this.setEndNianfModel(null);
			this.setEndYuefValue(null);
			setBaoblxValue(null);
			setBaoblxModel(null);
			
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			if(visit.getString2()!= null){
				if(! visit.getString2().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setProSelectionModel10(null);
					visit.setDropDownBean10(null);
//					visit.setProSelectionModel3(null);
//					visit.setDropDownBean3(null);
					this.setSelzhuangtModel(null);
					this.setSelzhuangtValue(null);
					visit.setString2("");
					this.setTreeid(null);
					visit.setList1(null);
					getDiancmcModels();
					this.setTreeid(null);
					this.setNianfModel(null);
					this.setNianfValue(null);
					this.setYuefModel(null);
					this.setYuefValue(null);
					this.setEndNianfModel(null);
					this.setEndNianfValue(null);
					this.setEndNianfModel(null);
					this.setEndYuefValue(null);
					setBaoblxValue(null);
					setBaoblxModel(null);
				}
				
			}
			visit.setString2(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName =visit.getString2();
        }else{
        	if(visit.getString2().equals("")) {
        		mstrReportName =visit.getString2();
            }
        }
		getToolbars();
		blnIsBegin = true;
	}
	
	public void getToolbars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		if(mstrReportName.equals(REZC_QUERY)){
			tb1.addText(new ToolbarText("查询方式:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("SelzhuangtSelect");
			cb.setWidth(100);
			cb.setListeners("select:function(){"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";document.Form0.submit();}");
			tb1.addField(cb);
		
		}else if(mstrReportName.equals(REZC_HB)){
			tb1.addText(new ToolbarText("查询方式:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BaoblxSelect");
			cb.setWidth(80);
			cb.setListeners("select:function(){"+MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)+";document.Form0.submit();}");
			tb1.addField(cb);
		}
		
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(50);
//		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(40);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
		if(mstrReportName.equals(REZC_QUERY) && this.getSelzhuangtValue().getId()==4){
			tb1.addText(new ToolbarText("结束年份:"));
			ComboBox endnianf = new ComboBox();
			endnianf.setTransform("EndNianfDropDown");
			endnianf.setWidth(50);
//			endnianf.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(endnianf);
			
			tb1.addText(new ToolbarText("-"));
			
			tb1.addText(new ToolbarText("结束月份:"));
			ComboBox endyuef = new ComboBox();
			endyuef.setTransform("EndYuefDropDown");
			endyuef.setWidth(40);
//			endyuef.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(endyuef);
		}
		
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

	 // end年份下拉框
    private static IPropertySelectionModel _EndNianfModel;
    public IPropertySelectionModel getEndNianfModel() {
        if (_EndNianfModel == null) {
            getEndNianfModels();
        }
        return _EndNianfModel;
    }
    
	private IDropDownBean _EndNianfValue;
	
    public IDropDownBean getEndNianfValue() {
        if (_EndNianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getEndNianfModel().getOptionCount(); i++) {
                Object obj = getEndNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _EndNianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _EndNianfValue;
    }
	
    public void setEndNianfValue(IDropDownBean Value) {
    	if  (_EndNianfValue!=Value){
    		_EndNianfValue = Value;
    	}
    }

    public IPropertySelectionModel getEndNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2003; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _EndNianfModel = new IDropDownModel(listNianf);
        return _EndNianfModel;
    }

    public void setEndNianfModel(IPropertySelectionModel _value) {
        _EndNianfModel = _value;
    }

	// end月份下拉框
	private static IPropertySelectionModel _EndYuefModel;
	
	public IPropertySelectionModel getEndYuefModel() {
	    if (_EndYuefModel == null) {
	        getEndYuefModels();
	    }
	    return _EndYuefModel;
	}
	
	private IDropDownBean _EndYuefValue;
	
	public IDropDownBean getEndYuefValue() {
	    if (_EndYuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getEndYuefModel().getOptionCount(); i++) {
	            Object obj = getEndYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _EndYuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _EndYuefValue;
	}
	
	public void setEndYuefValue(IDropDownBean Value) {
    	if  (_EndYuefValue!=Value){
    		_EndYuefValue = Value;
    	}
	}

    public IPropertySelectionModel getEndYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _EndYuefModel = new IDropDownModel(listYuef);
        return _EndYuefModel;
    }

    public void setEndYuefModel(IPropertySelectionModel _value) {
        _EndYuefModel = _value;
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
    //条件选择框
    private static IPropertySelectionModel _SelzhuangtModel;
   
    
	public void setSelzhuangtModel(IDropDownModel _value) {
		_SelzhuangtModel = _value;
	}
	public IPropertySelectionModel getSelzhuangtModel() {
		if (_SelzhuangtModel == null) {
			getSelzhuangtModels();
		}
		return _SelzhuangtModel;
	}

	private IDropDownBean _SelzhuangtValue;

	public IDropDownBean getSelzhuangtValue() {
		if(_SelzhuangtValue==null){
			getSelzhuangtModels();
			setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
		}
//		if(mstrReportName.equals(RT_DR01)){
//			setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
//		}
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
		listSelzhuangt.add(new IDropDownBean(1, "当月月报"));
		listSelzhuangt.add(new IDropDownBean(2, "累计月报"));
		listSelzhuangt.add(new IDropDownBean(3, "当月及累计月报"));
		listSelzhuangt.add(new IDropDownBean(4, "按时间段查询"));
		_SelzhuangtModel = new IDropDownModel(listSelzhuangt);
		return _SelzhuangtModel;
	}

	
private static IPropertySelectionModel _BaoblxModel;
   
    
	public void setBaoblxModel(IDropDownModel _value) {
		_BaoblxModel = _value;
	}
	public IPropertySelectionModel getBaoblxModel() {
		if (_BaoblxModel == null) {
			getBaoblxModels();
		}
		return _BaoblxModel;
	}

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			getBaoblxModels();
			setBaoblxValue((IDropDownBean)getBaoblxModel().getOption(0));
		}
//		if(mstrReportName.equals(RT_DR01)){
//			setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
//		}
		return _BaoblxValue;
	}
	
	private boolean _BaoblxChange=false;
	public void setBaoblxValue(IDropDownBean Value) {
		if (_BaoblxValue==Value) {
			_BaoblxChange = false;
		}else{
			_BaoblxValue = Value;
			_BaoblxChange = true;
		}
	}

	public IPropertySelectionModel getBaoblxModels() {
		List listBaoblx=new ArrayList();
		listBaoblx.add(new IDropDownBean(1, "累计"));
		listBaoblx.add(new IDropDownBean(2, "季度"));
		_BaoblxModel = new IDropDownModel(listBaoblx);
		return _BaoblxModel;
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
	}

}