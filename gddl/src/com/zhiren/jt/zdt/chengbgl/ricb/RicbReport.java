package com.zhiren.jt.zdt.chengbgl.ricb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/* 
* 时间：2009-07-20
* 作者： ll
* 修改内容：修改查询sql公式。
*                    
*			
*/ 
/* 
* 时间：2009-08-7
* 作者： ll
* 修改内容：根据日成本查询计算公式，修改页面显示sql。       
*			
*/ 
/* 
* 时间：2009-08-29
* 作者： ll
* 修改内容：1、修改连接条件，以fahb 的diiancxxb_id 与vwdianc ,zhilb_id 和zhilb连接,
* 			  fahb的到货日期做条件
* 			  与厂级数据保持一致。
* 		   2、增加当成本数量、累积成本数量列。
* 			3、煤矿地区下一层展开，增加矿别小计层。
*/ 
/* 
* 时间：2009-09-4
* 作者： ll
* 修改内容：1、二级公司登陆时去“总计”行。
* 		   
*/ 
/* 
* 时间：2009-09-14
* 作者： ll
* 修改内容：1、在报表打印中，增加分厂分供应商表
* 		   
*/ 
/* 
* 时间：2009-09-17
* 作者： ll
* 修改内容：综合到厂价公式修改
*/ 
/* 
* 时间：2009-09-25
* 作者： ll
* 修改内容：分厂分供应商统计表的增加煤矿列
*/ 
public class RicbReport extends BasePage {
	public final static String LX_FC="fc";
	public final static String LX_FK="fk";
	public final static String LX_FKFC="fkfc";
	public final static String LX_FCFK="fcfk";
	public final static String LX_FCFGYS="fcfgys";
	public final static String LX_QP="qp";
	
	private String leix="fc";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return biaotmc;
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
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getPrintData();
	}
	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}
	
	private String getCondtion(){
		String strCondtion="";
		int intLen=0;
		long lngYunsfsId=-1;
		long lngJihkjId= -1;
		String datStart=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1());
		String datEnd=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2());
		
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		
//		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();
		
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
			
		strCondtion="and f.daohrq>=to_date('"+datStart+"','yyyy-mm-dd') \n" +
			"and f.daohrq<=to_date('"+datEnd+"','yyyy-mm-dd') \n" ;
		
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and ys.id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and jh.id=" +lngJihkjId;
		}
		
//		if (!strGongys_id.equals("-1")){
//			strCondtion=strCondtion+" and y.dqid=" +strGongys_id;
//		}
//		
//		if (jib==2){
//			strCondtion=strCondtion+" and (dc.fgsid=" +strDiancxxb_id+" or dc.rlgsid="+strDiancxxb_id+")";
//		}else if (jib==3){
//			strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
//		}else if (jib==-1){
//			strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
//		}
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				strCondtion=strCondtion+" and dc.id=" +pa[0];
				strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}else{
				strCondtion=" and dc.id=-1";
			}
		}else{
			if (!strGongys_id.equals("-1")){
				strCondtion=strCondtion+" and y.dqid=" +strGongys_id;
			}
			if (jib==2){
				strCondtion=strCondtion+" and (dc.fgsid=" +strDiancxxb_id +" or dc.rlgsid="+strDiancxxb_id +")"; 
			}else if (jib==3){
				strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
			}else if (jib==-1){
				strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
			}
		}
		return strCondtion;
	}
	
	private String getPrintData(){
		
		String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();
		String guolzj = "";
		String guolzj2="";
		int jib=getDiancTreeJib(strDiancxxb_id);
		if (jib==2){			
			guolzj=" having not  grouping(dc.fgsmc)=1\n";//分公司查看报表时过滤总计。
			guolzj2=" and grouping(dc.fgsmc)=0";
		}else if (jib==3){
			guolzj=" having not grouping(dc.mingc)=1\n";
			guolzj2=" and grouping(dc.mingc)=0";
		}
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
		
		StringBuffer sbSelect=new StringBuffer();
		StringBuffer sbGroup=new StringBuffer();
		String gongysid="";
		String hetgongys="";
		String hetb="";
		String hetb_tj="";
		String jihkjb="";
		String jihkjb_tj="";
		String jihkjbid="";
		String jihkjb_gb="";
		String meikmc="";
		String meikxxb="";
		String meikxxb_tj="";
		String meikxxbmc="";
		String meik_mc="";
		String meikxxb_gb="";
		String meikmc_gb="";
		if (leix.equals(LX_FC)){
			gongysid="y.id as dqid,";
			hetgongys="y.id, ";
			sbSelect.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc, \n");
			sbGroup.append(" group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbGroup.append(""+guolzj+" \n");
			sbGroup.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="(分厂)";
		}else if(leix.equals(LX_FK)){
			gongysid="y.id as dqid,";
			hetgongys="y.id, ";
			sbSelect.append("select decode(grouping(y.smc)+grouping(y.dqmc),2,'总计',1,y.smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||y.dqmc) as danwmc,");
			sbGroup.append(" group by rollup(y.smc,y.dqmc)   \n");
			sbGroup.append(" order by grouping(y.smc) desc,max(y.sxh) ,y.smc, \n");
			sbGroup.append("          grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc \n");
			
			strTitle="(分矿)";
		}else if(leix.equals(LX_FCFK)){
			gongysid="y.id as dqid,";
			hetgongys="y.id, ";
			sbSelect.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(y.dqmc),3,'总计',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||y.dqmc) as danwmc,");
			sbGroup.append("group by rollup(dc.fgsmc,dc.mingc,y.dqmc)   \n");
			sbGroup.append(""+guolzj+" \n");
			sbGroup.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbGroup.append("          grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc \n");
			strTitle="(分厂分矿)";
		}else if(leix.equals(LX_FKFC)){
			gongysid="y.id as dqid,";
			hetgongys="y.id, ";
			if(jib==1){
				sbSelect.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(y.dqmc),3,'总计',2,y.dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc,");
				sbGroup.append(" group by rollup(y.dqmc,dc.fgsmc,dc.mingc)   \n");
				sbGroup.append(" order by  grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc, \n");
				sbGroup.append("          grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
				sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			}else{
				sbSelect.append("select decode(grouping(dc.mingc)+grouping(y.dqmc),2,'总计',1,y.dqmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc,");
				sbGroup.append(" group by rollup(y.dqmc,dc.mingc)   \n");
				sbGroup.append(" order by  grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc, \n");
				sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			}
			
			strTitle="(分矿分厂)";
		}else if(leix.equals(LX_FCFGYS)){
			//取合同供应商名称,如果没有合同则取发货中的供应商名称。
			gongysid="decode(nvl(ht.gongysb_id,0),0,y.dqid,ht.gongysb_id) as dqid,jh.id as jihkjb_id,";
			jihkjbid="dc.jihkjb_id,  ";
			hetgongys="ht.gongysb_id,jh.id,y.dqid, ";
			hetb=",hetb ht ";
			hetb_tj="and f.hetb_id=ht.id(+) ";
			jihkjb=",jihkjb jh ";
			jihkjb_tj=" and wclj.jihkjb_id=jh.id";
			jihkjb_gb=",dc.jihkjb_id";
			meikmc=",meikmc";
			meikxxb=",meikxxb mk";
			meikxxb_tj=" and f.meikxxb_id=mk.id";
			meikxxbmc=" mk.mingc as meikmc, ";
			meik_mc="dc.meikmc as meikmc,";
			meikxxb_gb=" ,mk.mingc";
			meikmc_gb=",dc.meikmc";
			sbSelect.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(jh.mingc)+grouping(y.mingc),4,'总计',3,fgsmc,2,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,1,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp'||jh.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||y.mingc) as danwmc,\n");
			sbSelect.append("wclj.meikmc as meikmc,\n");
			sbGroup.append("group by rollup(dc.fgsmc,dc.mingc,jh.mingc,y.mingc,wclj.meikmc)   \n");
			sbGroup.append("having not (grouping(y.mingc)||grouping(wclj.meikmc)) =1  "+guolzj2+" \n");
			sbGroup.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbGroup.append("		  grouping(jh.mingc) desc,max(jh.xuh) ,jh.mingc, \n");
			sbGroup.append("          grouping(y.mingc) desc,max(y.xuh) ,y.mingc \n");
			strTitle="(分厂分供应商)";
		}else if(leix.equals(LX_QP)){
			strTitle="(棋盘)";
		}else if(leix.indexOf(",")>0){
			return getPrintDataTz();
		}
	
//		---------------------------------------------------------------------
//		sbSqlBody.append("select danwmc,laimsl,cbsl,ljrez,ljrezdk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj   \n");
		sbSqlBody.append("select danwmc"+meikmc+",laimsl,cbsl,ljrez,ljrezdk,round_new((ljmeij+ljyunf+ljzaf),2) as daoczhj   \n");
		sbSqlBody.append("   ,ljmeij,ljyunf,ljzaf   \n");
		sbSqlBody.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as ljhansbmdj   \n");
		sbSqlBody.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as ljbmdj   \n");
		sbSqlBody.append("from (   \n");
		sbSqlBody.append(sbSelect).append("\n"); 
		sbSqlBody.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as cbsl,  \n");
		sbSqlBody.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,   \n");
		sbSqlBody.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,   \n");
		sbSqlBody.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,   \n");
		sbSqlBody.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,   \n");
		sbSqlBody.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf    \n");
		sbSqlBody.append("    from (select dc.did as diancxxb_id,dc.dqid as gongysb_id,"+jihkjbid+meik_mc+" \n");
		sbSqlBody.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,   \n");
		sbSqlBody.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,   \n");
		sbSqlBody.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,   \n");
		sbSqlBody.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,   \n");
		sbSqlBody.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf   \n");
		sbSqlBody.append("            from ( select f.lieid,dc.id as did,"+gongysid+"dc.fgsid,"+meikxxbmc+" \n");
		sbSqlBody.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,   \n");
		sbSqlBody.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --成本数量,   \n");
		sbSqlBody.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,   \n");
		sbSqlBody.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,   \n");
		sbSqlBody.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,   \n");
		sbSqlBody.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf   \n");
		sbSqlBody.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys"+hetb+meikxxb+"   \n");
		sbSqlBody.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id "+hetb_tj+meikxxb_tj+"   \n");
		sbSqlBody.append("                      and f.diancxxb_id=dc.id   \n");
		sbSqlBody.append("                      and f.zhilb_id =zl.id(+)   \n");
		sbSqlBody.append("                      and f.gongysb_id=y.id       \n").append(getCondtion()).append(" \n");
		sbSqlBody.append("                  group by ("+hetgongys+"f.lieid,dc.id,dc.fgsid"+meikxxb_gb+")) dc   \n");
		sbSqlBody.append("              group by (dc.did,dc.dqid"+jihkjb_gb+meikmc_gb+") )  wclj,vwdianc dc ,vwgongys y"+jihkjb+"  \n");
		sbSqlBody.append("  where wclj.diancxxb_id=dc.id and wclj.gongysb_id=y.id "+jihkjb_tj+"  \n");
		sbSqlBody.append(sbGroup);
		sbSqlBody.append(")   \n");
		
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName="日入厂成本标煤单价情况"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbSqlBody.toString());
		String ArrHeader[][];
		int ArrWidth[];
		if(leix.equals(LX_FCFGYS)){
		    ArrHeader =new String[2][11];
			ArrHeader[0]=new String[] {"单位","煤矿名称","煤量","成本量","入厂热值","入厂热值","综合价","煤价","运费","杂费","标煤单价(元/吨)","标煤单价(元/吨)"};
			ArrHeader[1]=new String[] {"单位","煤矿名称","(吨)","(吨)","(MJ/kg)","(Kcal/kg)","(元/吨)","&nbsp;(元/吨)","(元/吨)","&nbsp;(元/吨)","含税","不含税"};
			ArrWidth =new int[] {150,120,80,80,80,60,60,60,60,60,60};
		}else{
			ArrHeader =new String[2][10];
			ArrHeader[0]=new String[] {"单位","煤量","成本量","入厂热值","入厂热值","综合价","煤价","运费","杂费","标煤单价(元/吨)","标煤单价(元/吨)"};
			ArrHeader[1]=new String[] {"单位","(吨)","(吨)","(MJ/kg)","(Kcal/kg)","(元/吨)","&nbsp;(元/吨)","(元/吨)","&nbsp;(元/吨)","含税","不含税"};
			ArrWidth =new int[] {150,80,80,80,60,60,60,60,60,60};
		}
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 4, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(5,  3, "", Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(48);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getPrintDataTz(){
		StringBuffer sbsql = new StringBuffer();

//		------------------------修改后sql--------------------------------------------- 
//		 sbsql.append("select danwmc,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj    \n");
		 sbsql.append("select danwmc,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new((ljmeij+ljyunf+ljzaf),2) as daoczhj    \n");
		 sbsql.append("   ,ljmeij as hansdj,ljyunf as yunf,ljzaf as zaf    \n");
		 sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as hansbmdj    \n");
		 sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as buhsbmdj    \n");
		 sbsql.append("from (    \n");
		 sbsql.append(" select decode(grouping(y.mingc),1,'总计',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','RicbReportmx_zdt','vwgongys_id',dc.id||','||y.id,y.mingc)) as danwmc,   \n");
		 sbsql.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as cbsl,   \n");
		 sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,    \n");
		 sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,    \n");
		 sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,    \n");
		 sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,    \n");
		 sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf     \n");
		 sbsql.append("    from (select dc.diancid as did,dc.dqid as yid, \n");
		 sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,    \n");
		 sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,    \n");
		 sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,    \n");
		 sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,    \n");
		 sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf    \n");
		 sbsql.append("            from ( select f.lieid as id,dc.id as diancid,y.id as dqid, \n");
		 sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,    \n");
		 sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --成本数量,    \n");
		 sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,    \n");
		 sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,    \n");
		 sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,    \n");
		 sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf    \n");
		 sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys    \n");
		 sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id     \n");
		 sbsql.append("                      and f.diancxxb_id=dc.id    \n");
		 sbsql.append("                      and f.zhilb_id =zl.id(+)    \n");
		 sbsql.append("                      and f.gongysb_id=y.id        \n").append(getCondtion()).append(" \n");
		 sbsql.append("                  group by (y.id,f.lieid,dc.id)) dc    \n");
		 sbsql.append("             group by diancid,dqid )  wclj,--日成本    \n");
		 sbsql.append("       vwdianc dc, vwgongys y   \n");
		 sbsql.append("   where wclj.yid=y.id and wclj.did=dc.id    \n");
		 sbsql.append("   group by rollup((y.id,y.mingc,dc.id))   \n");
		 sbsql.append("  order by grouping(y.mingc) ,max(y.xuh),y.mingc \n");
		 sbsql.append(")    \n");
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="日入厂煤成本明细"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][]=new String[2][11];
		ArrHeader[0]=new String[] {"单位","煤量","成本量","热值","热值","综合价","煤价","运费","杂费","标煤单价(元/吨)","标煤单价(元/吨)"};
		ArrHeader[1]=new String[] {"单位","(吨)","(吨)","(MJ/kg)","(Kcal/kg)","(元/吨)","&nbsp;(元/吨)","(元/吨)","&nbsp;(元/吨)","含税","不含税"};
		int ArrWidth[]=new int[] {150,80,80,60,60,60,60,60,60,60,60};
		
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 4, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(5,  3, "", Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(48);
		rt.body.setUseDefaultCss(true);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		leix = visit.getString9();
            }
        }
	}

	// 供应商
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getGongysDropDownModels() {
		String sql = "select id,mingc\n" + "from gongysb\n";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
	}

	// 类型
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);

	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "分厂"));
		list.add(new IDropDownBean(2, "分矿"));
		list.add(new IDropDownBean(3, "棋盘表"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 电厂名称
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib(String DiancTreeJib) {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}