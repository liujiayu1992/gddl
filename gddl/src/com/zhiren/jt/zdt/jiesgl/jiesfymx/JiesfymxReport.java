////2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   明细数据显示

package com.zhiren.jt.zdt.jiesgl.jiesfymx;

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
import com.zhiren.common.SysConstant;
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

public class JiesfymxReport extends BasePage {
	public final static String LX_FC="fc";
	public final static String LX_FK="fk";
	public final static String LX_FKFC="fkfc";
	public final static String LX_FCFK="fcfk";
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
		Date datStart=((Visit)getPage().getVisit()).getDate1();
//		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		long lngYunsfsId=-1;
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		
		long lngJihkjId= -1;
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
		
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
//		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		
		strCondtion="and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
			"and js.jiesrq<add_months(to_date('"+strDate1+"','yyyy-mm-dd'),1) \n" ;
		
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and js.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and js.jihkjb_id=" +lngJihkjId;
		}
		
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
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
		sbSqlBody.append(" 		sum(fh.ches) as ches, \n");
		sbSqlBody.append(" 		(round_new(sum(laimsl),0))  as laimsl, \n");
		sbSqlBody.append(" 		round_new(sum(fh.biaoz),0) as biaoz, \n");
		sbSqlBody.append(" 		round_new(sum(fh.jingz),0) as jingz, \n");
		sbSqlBody.append(" 		round_new(sum(fh.yuns),0) as yuns, \n");
		sbSqlBody.append(" 		round_new(sum(fh.yingd),0) as yingd, \n");
		sbSqlBody.append(" 		round_new(sum(fh.kuid),0) as kuid,  \n");
		sbSqlBody.append(" 		round_new(sum(fh.koud),0) as koud \n");
		sbSqlBody.append(" from \n");
		sbSqlBody.append("		 (select dc.fgsmc,dc.fgsxh,dc.mingc,dc.xuh,y.dqmc,y.dqxh,y.smc,y.sxh, \n");
		sbSqlBody.append(" 		 		 sum(fh.ches) as ches,\n");
		sbSqlBody.append(" 				 "+SysConstant.LaimField+" as laimsl, \n");
		sbSqlBody.append(" 				 round_new(sum(fh.biaoz),0) as biaoz, \n");
		sbSqlBody.append(" 				 round_new(sum(fh.jingz),0) as jingz, \n");
		sbSqlBody.append(" 				 round_new(sum(fh.yuns),0) as yuns, \n");
		sbSqlBody.append(" 				 round_new(sum(fh.yingd),0) as yingd, \n");
		sbSqlBody.append(" 				 round_new(sum(fh.yingd-fh.yingk),0) as kuid,  \n");
		sbSqlBody.append(" 				 round_new(sum(fh.koud),0) as koud \n" );
		
		sbSqlBody.append("       from fahb fh,vwdianc dc,vwgongys y  \n");
		sbSqlBody.append(" where fh.gongysb_id=y.id   \n");
		sbSqlBody.append(getCondtion());
		sbSqlBody.append("     and fh.diancxxb_id=dc.id     \n");
		sbSqlBody.append("     and fh.gongysb_id=y.id  \n");
		sbSqlBody.append("   group by fh.lieid,dc.fgsmc,dc.fgsxh,dc.mingc,dc.xuh,y.dqmc,y.dqxh,y.smc,y.sxh  ) fh  \n");
		
		if (leix.equals(this.LX_FC)){
			sbsql.append("select decode(grouping(fh.fgsmc)+grouping(fh.mingc),2,'总计',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||fh.mingc) as mingc, \n");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(fh.fgsmc,fh.mingc)   \n");
			sbsql.append(" order by grouping(fh.fgsmc) desc,max(fh.fgsxh) ,fh.fgsmc, \n");
			sbsql.append("          grouping(fh.mingc) desc,max(fh.xuh) ,fh.mingc \n");
			strTitle="(分厂)";
		}else if(leix.equals(this.LX_FK)){
			sbsql.append("select decode(grouping(fh.smc)+grouping(fh.dqmc),2,'总计',1,fh.smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||fh.dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(fh.smc,fh.dqmc)   \n");
			sbsql.append(" order by grouping(fh.smc) desc,max(fh.sxh) ,fh.smc, \n");
			sbsql.append("          grouping(fh.dqmc) desc,max(fh.dqxh) ,fh.dqmc \n");
			strTitle="(分矿)";
		}else if(leix.equals(this.LX_FCFK)){
			sbsql.append("select decode(grouping(fh.fgsmc)+grouping(fh.mingc)+grouping(fh.dqmc),3,'总计',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fh.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||fh.dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append("group by rollup(fh.fgsmc,fh.mingc,fh.dqmc)   \n");
			sbsql.append(" order by grouping(fh.fgsmc) desc,max(fh.fgsxh) ,fh.fgsmc, \n");
			sbsql.append("          grouping(fh.mingc) desc,max(fh.xuh) ,fh.mingc, \n");
			sbsql.append("          grouping(fh.dqmc) desc,max(fh.dqxh) ,fh.dqmc \n");
			strTitle="(分厂分矿)";
		}else if(leix.equals(this.LX_FKFC)){
			sbsql.append("select decode(grouping(fh.fgsmc)+grouping(fh.mingc)+grouping(fh.dqmc),3,'总计',2,fh.dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||fh.mingc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(fh.dqmc,fh.fgsmc,fh.mingc)   \n");
			sbsql.append(" order by  grouping(fh.dqmc) desc,max(fh.dqxh) ,fh.dqmc, \n");
			sbsql.append("          grouping(fh.fgsmc) desc,max(fh.fgsxh) ,fh.fgsmc, \n");
			sbsql.append("          grouping(fh.mingc) desc,max(fh.xuh) ,fh.mingc \n");
			strTitle="(分矿分厂)";
		}else if(leix.equals(this.LX_QP)){
			strTitle="(棋盘)";
		}else if(leix.indexOf(",")>0){
			return getPrintDataTz();
		}
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName="入厂煤数量统计"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][] = new String[2][9];
		
		ArrHeader[0] = new String[] { "单位", "车数","实收量<br>(吨)", "票重<br>(吨)", "净重<br>(吨)", "运损<br>(吨)","盈吨<br>(吨)", "亏吨<br>(吨)", "扣吨<br>(吨)" };
		ArrHeader[1] = new String[] { "单位", "车数","实收量<br>(吨)", "票重<br>(吨)", "净重<br>(吨)", "运损<br>(吨)","盈吨<br>(吨)", "亏吨<br>(吨)", "扣吨<br>(吨)" };
		int ArrWidth[] = new int[] { 150, 80, 80, 80, 80, 80, 80, 80, 80 };
		
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, strDate1+"-"+strDate2,Table.ALIGN_CENTER); 
		rt.setDefaultTitle(6,  2, "", Table.ALIGN_LEFT);
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

		Date datStart=((Visit)getPage().getVisit()).getDate1();
//		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
//		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		String strgongs=
			"select distinct y.mingc as mingc,y.id as id from\n" +
			"        jiesb js ,vwgongys y,vwdianc dc\n" + 
			"        where js.gongysb_id=y.id and dc.id=js.diancxxb_id\n" + 
			"       and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strDate1+"','yyyy-mm-dd'),1)";

		StringBuffer sbsql = new StringBuffer();
//		---------------------------------------------------
		sbsql.append("select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,");
		sbsql.append("       sum(sj.jiessl) as jiessl,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),3)) as farl,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),2)) as zonghj,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.hansdj)/sum(sj.jiessl),2)) as hansdj,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)) as zengzs,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.kuangyf)/sum(sj.jiessl),2)) as kuangyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.jiaohqyzf)/sum(sj.jiessl),2)) as jiaohqyzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.tielyf)/sum(sj.jiessl),2)) as tielyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)) as tielyfs,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.tielzf)/sum(sj.jiessl),2)) as tielzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qiyf)/sum(sj.jiessl),2)) as qiyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)) as qiys,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qiyzf)/sum(sj.jiessl),2)) as qiyzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.haiyf)/sum(sj.jiessl),2)) as haiyf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2)) as haiys,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.gangzf)/sum(sj.jiessl),2)) as gangzf,\n" );
		sbsql.append("       decode(sum(sj.jiessl),0,0,round(sum(sj.jiessl*sj.qitfy)/sum(sj.jiessl),2)) as qitfy,\n" );
		sbsql.append("       decode( sum(nvl(sj.jiessl,0)),0,0,decode(round(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),3),0,0,\n" );
		sbsql.append("               round(round(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)*29.271/round(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),3),2))\n" );
		sbsql.append("       ) as hansbmdj,\n" );
		sbsql.append("       decode(sum(nvl(sj.jiessl,0)),0,0,decode(round(sum(sj.jiessl*nvl(sj.farl,0))/sum(sj.jiessl),3),0,0,\n" );
		sbsql.append("              round(\n" );
		sbsql.append("                   (round(sum(sj.jiessl*sj.zonghj)/sum(sj.jiessl),3)-round(sum(sj.jiessl*sj.zengzs)/sum(sj.jiessl),2)\n" );
		sbsql.append("                   -round(sum(sj.jiessl*sj.tielyfs)/sum(sj.jiessl),2)-round(sum(sj.jiessl*sj.qiys)/sum(sj.jiessl),2)\n" );
		sbsql.append("                   -round(sum(sj.jiessl*sj.haiys)/sum(sj.jiessl),2))*29.271/round(sum(sj.jiessl*sj.farl)/sum(sj.jiessl),3)\n" );
		sbsql.append("        ,2))) as buhsbmdj\n" );
		sbsql.append(" from jihkjb jh,(\n" );
		sbsql.append("       select js.id,y.id as diancxxb_id,dc.mingc as mingc,ht.jihkjb_id as jihkjb_id,js.gongysb_id,js.jiesrq as riq,\n" );
		sbsql.append("             nvl(js.jiessl,0) as jiessl,round(nvl(rl.farl,0)*4.1816/1000,3) as farl,decode(nvl(js.jiessl,0),0,0,round((nvl(js.meikje,0)/js.jiessl),2)) as hansdj,\n" );
		sbsql.append("             decode(nvl(js.jiessl,0),0,0,round((nvl(js.meikje,0)/js.jiessl),2))+decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.dityf,0)/yf.jiessl,2)+round(nvl(yf.ditzf,0)/yf.jiessl,2))+decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0)\n" );
		sbsql.append("                   +decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" );
		sbsql.append("                   +decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0)+decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0)\n" );
		sbsql.append("                   +0 as zonghj,\n" );
		sbsql.append("             decode(nvl(js.jiessl,0),0,0,round((nvl(js.shuik,0)/js.jiessl),2)) as zengzs,\n" );
		sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.dityf,0)/yf.jiessl,2)) as kuangyf,\n" );
		sbsql.append("             decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.ditzf,0)/yf.jiessl,2)) as jiaohqyzf,\n" );
		sbsql.append("             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round((nvl(yf.guotyf,0)+nvl(yf.bukyf,0))/yf.jiessl,2)),0) as tielyf,\n" );
		sbsql.append("             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.shuik,0)/yf.jiessl,2)),0) as tielyfs,\n" );
		sbsql.append("             decode(ys.mingc,'铁路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as tielzf,\n" );
		sbsql.append("             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as qiyf,\n" );
		sbsql.append("             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.shuik,0)/yf.jiessl,2)),0) as qiys,\n" );
		sbsql.append("             decode(ys.mingc,'公路',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as qiyzf,\n" );
		sbsql.append("             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotyf,0)/yf.jiessl,2)),0) as haiyf,\n" );
		sbsql.append("             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.shuik,0)/yf.jiessl,2)),0) as haiys,\n" );
		sbsql.append("             decode(ys.mingc,'水运',decode(nvl(yf.jiessl,0),0,0,round(nvl(yf.guotzf,0)/yf.jiessl,2)),0) as gangzf,\n" );
		sbsql.append("             0 as qitfy\n" );
		sbsql.append("        from jiesb js,jiesyfb yf,hetb ht,yunsfsb ys,vwdianc dc,vwgongys y,\n" );
		sbsql.append("              (select js.id,nvl(zl.jies,0) as farl from jiesb js,jieszbsjb zl,zhibb zb\n" );
		sbsql.append("                where js.id=zl.jiesdid and zl.zhibb_id=zb.id and zb.bianm='收到基低位热值Qnetar(MJ/Kg)'\n" );
		sbsql.append("                  and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') and js.jiesrq<add_months(to_date('"+strDate1+"','yyyy-mm-dd'),1)\n" );
		sbsql.append("               ) rl\n" );
		sbsql.append("       where js.id=yf.diancjsmkb_id and js.hetb_id=ht.id and js.id=rl.id and js.yunsfsb_id=ys.id and y.id=js.gongysb_id and js.diancxxb_id=dc.id \n" );
		sbsql.append(getCondtion());
		sbsql.append("    ) sj,\n" );
		sbsql.append("     ( "+strgongs+" ) dc \n"); 
		sbsql.append("   where jh.id=sj.jihkjb_id and sj.diancxxb_id=dc.id and sj.riq>=to_date('"+strDate1+"','yyyy-mm-dd') and sj.riq<add_months(to_date('"+strDate1+"','yyyy-mm-dd'),1)\n" );
		sbsql.append("     group by rollup(dc.mingc)");
		sbsql.append("     order by grouping(dc.mingc)");
	
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="结算费用明细"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());

		String ArrHeader[][]=new String[1][21];
		
		ArrHeader[0] = 
		new String[] {"单位","入厂煤量<br>(吨)","入厂热值<br>(MJ/kg)","综合价<br>(元/吨)","煤价<br>(元/吨)","增值税<br>(元/吨)","矿运费<br>(元/吨)","交货<br>前运杂费<br>(元/吨)",
					 					"铁路运费<br>(元/吨)","铁路<br>运费税额<br>(元/吨)","到站<br>铁路杂费<br>(元/吨)","汽车运费<br>(元/吨)","汽运税额<br>(元/吨)","汽运杂费<br>(元/吨)",
					 					"海(水)<br>运费<br>(元/吨)","海(水)<br>运税额<br>(元/吨)","港杂费<br>(元/吨)","其它费用<br>(元/吨)","含税<br>标煤单价<br>(元/吨)","不含税<br>标煤单价<br>(元/吨)"};
		
		int ArrWidth[]=new int[] {100,40,65,55,55,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55,55};
		
		
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 2, strDate1,Table.ALIGN_CENTER); 
		rt.setDefaultTitle(7,  2, "", Table.ALIGN_LEFT);
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