////2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   明细数据显示

/* 
* 时间：2009-09-3
* 作者： ll
* 修改内容：1、当用户级别为分公司时，显示明细报表将过滤“总计”行。
*/ 
package com.zhiren.jt.zdt.rulgl.rulzltj;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class RulmzltjysReport extends BasePage {
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

	private String getCondtion(){
		String strCondtion="";
		int intLen=0;
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
		
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		
//		strCondtion="and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
//			"and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				strCondtion=strCondtion+" and dc.id=" +pa[0];
				//strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}else{
				strCondtion=" and dc.id=-1";
			}
		}else{
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
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		String guolzj="";
		int jib=getDiancTreeJib(strDiancxxb_id);
		if (jib==2){
			guolzj=" and grouping(fgsmc)=0\n";//分公司查看报表时过滤总计。
		}else if (jib==3){
			guolzj=" and grouping(diancmc)=0\n";
		}
		Visit visit = (Visit)getPage().getVisit();
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
//		sbSqlBody.append("nvl(sum(haoyl),0) as haoyl,sum(nvl(jianzl,0)) as jianzl, \n");
//		sbSqlBody.append(" nvl(round(decode(sum(jianzl),0,0,sum(mt*jianzl)/sum(jianzl)),2),0) as mt, \n");
//		sbSqlBody.append(" nvl(round(decode(sum(jianzl),0,0,sum(ad*jianzl)/sum(jianzl)),2),0) as ad, \n");
//		sbSqlBody.append(" nvl(round(decode(sum(jianzl),0,0,sum(vdaf*jianzl)/sum(jianzl)),2),0) as vdaf, \n");
//		sbSqlBody.append(" nvl(round(decode(sum(jianzl),0,0,sum(std*jianzl)/sum(jianzl)),2),0) as std, \n");
//		sbSqlBody.append(" nvl(round(decode(sum(jianzl),0,0,sum(qnet_ar*jianzl)/sum(jianzl)),2),0) as qnet_ar, \n");
//		sbSqlBody.append(" nvl(round(decode(sum(jianzl),0,0,sum((qnet_ar/0.0041816)*jianzl)/sum(jianzl))),0) as qnet_ar1 \n");
//		sbSqlBody.append(" from  \n");
//		sbSqlBody.append("       (select dc.mingc as diancmc,to_char(r.rulrq,'yyyy-mm-dd') as riq,max(dc.xuh) as diancxh,dc.fgsmc,dc.fgsxh, nvl(sum(fadhy+gongrhy),0) as haoyl, \n");
//		sbSqlBody.append(" sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)) as jianzl,\n");
//		sbSqlBody.append(" round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(mt*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as mt, \n");
//		sbSqlBody.append(" round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(ad*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as ad, \n");
//		sbSqlBody.append(" round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(vdaf*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as vdaf, \n");
//		sbSqlBody.append(" round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(std*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as std, \n");
//		sbSqlBody.append(" round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(qnet_ar*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as qnet_ar, \n");
//		sbSqlBody.append(" round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(qnet_ar/0.0041816*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)))) as qnet_ar1 \n");
//		sbSqlBody.append(" from rulmzlb r,meihyb m,vwdianc dc \n");
//		sbSqlBody.append(" where m.rulmzlb_id(+)=r.id \n");
//		sbSqlBody.append(getCondtion());
//		sbSqlBody.append(" and dc.id(+)=r.diancxxb_id \n");
//		sbSqlBody.append(" group by dc.mingc,r.rulrq,dc.xuh,dc.fgsmc,dc.fgsxh \n");
//		sbSqlBody.append("  having not (grouping(dc.mingc) || grouping(r.rulrq))=1  order by dc.mingc,r.rulrq ) sj \n");
		
		if(visit.getRuljiaql().equals(SysConstant.RuLJQL)){
			 
			sbSqlBody.append("nvl(sum(haoyl),0) as haoyl,sum(nvl(jianzl,0)) as jianzl,  \n");
			sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(mt,"+visit.getMtdec()+")*jianzl)/sum(jianzl)),"+visit.getMtdec()+"),0) as mt,  \n");
			sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(ad*jianzl)/sum(jianzl)),2),0) as ad,  \n");
			sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(vdaf*jianzl)/sum(jianzl)),2),0) as vdaf,  \n");
			sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(std*jianzl)/sum(jianzl)),2),0) as std,  \n");
			sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0) as qnet_ar,  \n");
			sbSqlBody.append("round_new(nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0)/0.0041816,0) as qnet_ar1  \n");
			sbSqlBody.append("from ( select distinct dc.xuh,dc.id,dc.fgsid as fgsid,dc.fgsmc,dc.mingc as diancmc,dc.fgsxh,dc.xuh as diancxh from vwdianc dc where 1=1 "+getCondtion()+") dc,  \n");
			sbSqlBody.append("(select m.diancxxb_id, nvl(sum(fadhy+gongrhy),0) as haoyl from meihyb m  \n");
			sbSqlBody.append("where m.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and m.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n");
			sbSqlBody.append("group by m.diancxxb_id) haoy, \n");
			sbSqlBody.append("(select r.diancxxb_id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl, \n");
			sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,"+visit.getMtdec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getMtdec()+") as mt,  \n");
			sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,  \n");
			sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,  \n");
			sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,  \n");
			sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getFarldec()+") as qnet_ar,  \n");
			sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(qnet_ar/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1  \n");
			sbSqlBody.append("from rulmzlb r,vwdianc dc  \n");
			sbSqlBody.append("where dc.id(+)=r.diancxxb_id  \n");
			sbSqlBody.append("and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') ");
			sbSqlBody.append(getCondtion());
			sbSqlBody.append("group by dc.id,dc.mingc,dc.fgsmc,dc.fgsxh,r.diancxxb_id) sj  \n");
			
			if (leix.equals(this.LX_FC)){
				sbsql.append("select decode(grouping(fgsmc)+grouping(diancmc),2,'总计',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||diancmc) as mingc, \n");
				sbsql.append(sbSqlBody);
				sbsql.append(" where sj.diancxxb_id(+)=dc.id and haoy.diancxxb_id(+)=dc.id  \n");
				sbsql.append(" group by rollup(fgsmc,diancmc,dc.xuh)   \n");
				sbsql.append(" having not(grouping(dc.xuh)=1 and grouping(diancmc)<>1) "+guolzj+"\n");
				sbsql.append(" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
				sbsql.append(" grouping(diancmc) desc,dc.xuh,max(diancxh) ,diancmc");
				strTitle="(分厂)";
			}else if(leix.equals(this.LX_QP)){
				strTitle="(棋盘)";
			}else if(leix.indexOf(",")>0){
				return getPrintDataTz();
			}	
		}else{
			
		sbSqlBody.append("nvl(sum(haoyl),0) as haoyl,sum(nvl(jianzl,0)) as jianzl, \n");
		sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(mt,"+visit.getMtdec()+")*jianzl)/sum(jianzl)),"+visit.getMtdec()+"),0) as mt, \n");
		sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(ad*jianzl)/sum(jianzl)),2),0) as ad, \n");
		sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(vdaf*jianzl)/sum(jianzl)),2),0) as vdaf, \n");
		sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(std*jianzl)/sum(jianzl)),2),0) as std, \n");
		sbSqlBody.append("nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0) as qnet_ar, \n");
		sbSqlBody.append("round_new(nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0)/0.0041816,0) as qnet_ar1 \n");
		sbSqlBody.append("from ( select distinct  dc.id,dc.fgsid as fgsid,dc.fgsmc,dc.mingc as diancmc,dc.fgsxh,dc.xuh as diancxh from vwdianc dc where 1=1 "+getCondtion()+" ) dc, \n");
		sbSqlBody.append("(select dc.id, nvl(sum(fadhy+gongrhy),0) as haoyl, \n");
		sbSqlBody.append("sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)) as jianzl,\n");
		sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(round_new(mt,"+visit.getMtdec()+")*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),"+visit.getMtdec()+") as mt, \n");
		sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(ad*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as ad, \n");
		sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(vdaf*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as vdaf, \n");
		sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(std*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as std, \n");
		sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),"+visit.getFarldec()+") as qnet_ar, \n");
		sbSqlBody.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(qnet_ar/0.0041816*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),0) as qnet_ar1 \n");
		sbSqlBody.append("from rulmzlb r,meihyb m,vwdianc dc \n");
		sbSqlBody.append("where m.rulmzlb_id(+)=r.id  \n");
		sbSqlBody.append("and dc.id(+)=r.diancxxb_id \n");
		sbSqlBody.append("and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') ");
		sbSqlBody.append(getCondtion());
		sbSqlBody.append("group by dc.id,dc.mingc,dc.fgsmc,dc.fgsxh) sj \n");
		
		if (leix.equals(this.LX_FC)){
			sbsql.append("select decode(grouping(fgsmc)+grouping(diancmc),2,'总计',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||diancmc) as mingc, \n");
//			sbsql.append("decode(grouping(diancmc),1,'',sj.riq) as riq,\n");
			sbsql.append(sbSqlBody);
			sbsql.append(" where sj.id=dc.id(+) \n");
			sbsql.append(" group by rollup(fgsmc,diancmc)   \n");
			//sbsql.append(" having not (grouping(diancmc) || grouping(riq)) =1 \n");
			sbsql.append(" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
			sbsql.append("          grouping(diancmc) desc,max(diancxh) ,diancmc  \n");
			strTitle="(分厂)";
		}else if(leix.equals(this.LX_QP)){
			strTitle="(棋盘)";
		}else if(leix.indexOf(",")>0){
			return getPrintDataTz();
		}
		}
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName="入炉煤质量统计明细"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][]=new String[2][9];
		ArrHeader[0]=new String[] {"电厂","入炉煤量(吨)","入炉煤量(吨)","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)","低位发热量","低位发热量"};
		ArrHeader[1]=new String[] {"电厂","入炉煤量","检质量","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)","MJ/kg","Kcal/Kg"};

		int ArrWidth[]=new int[] {150,60,60,60,60,60,60,60,60};
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 4,FormatDate(DateUtil.getDate(strDate1))+"-"+FormatDate(DateUtil.getDate(strDate2)),Table.ALIGN_CENTER); 
		rt.setDefaultTitle(8, 2, "单位:吨", Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "制表时间:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
        rt.setDefautlFooter(4, 2 ,"审核:",Table.ALIGN_CENTER );
        if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
			
        	rt.setDefautlFooter(8, 2, "制表:",Table.ALIGN_RIGHT);
    		}else{
    			
    			rt.setDefautlFooter(8, 2, "制表:"+((Visit) getPage().getVisit()).getDiancmc

    (),Table.ALIGN_RIGHT);
    		}
//        rt.setDefautlFooter(8, 2, "制表:",Table.ALIGN_RIGHT);
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
		int intLen=0;
//		String strDiancxxb_id="";
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		Visit visit = (Visit)getPage().getVisit();
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		StringBuffer sbsql = new StringBuffer();
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				strDiancxxb_id=pa[0];
				//strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}
		}
		String guolzj="";
		int jib=getDiancTreeJib(strDiancxxb_id);
		if (jib==2){
			guolzj=" and grouping(fgsmc)=0\n";//分公司查看报表时过滤总计。
		}else if (jib==3){
			guolzj=" and grouping(mingc)=0\n";
		}
			if(visit.getRuljiaql().equals(SysConstant.RuLJQL)){
				 
				sbsql.append("select decode(grouping(fgsmc)+grouping(mingc)+grouping(riq.riq),3,'总计',2,fgsmc||'',mingc) as danwmc,  \n");
				sbsql.append("decode(grouping(riq.riq)+grouping(mingc),1,'小计',to_char(riq.riq,'yyyy-mm-dd')) as riq, \n");
				sbsql.append("nvl(sum(haoyl),0) as haoyl,sum(nvl(jianzl,0)) as jianzl,  \n");
				sbsql.append("nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(mt,"+visit.getMtdec()+")*jianzl)/sum(jianzl)),"+visit.getMtdec()+"),0) as mt,  \n");
				sbsql.append("nvl(round_new(decode(sum(jianzl),0,0,sum(ad*jianzl)/sum(jianzl)),2),0) as ad,  \n");
				sbsql.append("nvl(round_new(decode(sum(jianzl),0,0,sum(vdaf*jianzl)/sum(jianzl)),2),0) as vdaf,  \n");
				sbsql.append("nvl(round_new(decode(sum(jianzl),0,0,sum(std*jianzl)/sum(jianzl)),2),0) as std,  \n");
				sbsql.append("nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0) as qnet_ar,  \n");
				sbsql.append("round_new(nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0)/0.0041816,0) as qnet_ar1  \n");
				sbsql.append("from   \n");
				sbsql.append("(select riq,dc.* from \n");
				sbsql.append("        (select to_date('"+strDate1+"','yyyy-mm-dd')+rownum-1 as riq \n");
				sbsql.append("                from all_objects \n");
				sbsql.append("                where rownum<=(to_date('"+strDate2+"','yyyy-mm-dd')-to_date('"+strDate1+"','yyyy-mm-dd')+1)) riq, \n");
				sbsql.append("        vwdianc dc  \n");
				sbsql.append("        where dc.id="+strDiancxxb_id+") riq, \n");
				sbsql.append("(select diancxxb_id, m.rulrq,sum(nvl(m.fadhy,0)+nvl(m.gongrhy,0)) as haoyl from meihyb m where m.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')  \n");
				sbsql.append("        and m.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n");
				sbsql.append("        and m.diancxxb_id="+strDiancxxb_id+" group by diancxxb_id,m.rulrq order by m.rulrq) haoy, \n");
				sbsql.append("(select r.diancxxb_id,r.rulrq as riq,max(dc.xuh) as diancxh, \n");
				sbsql.append("sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl, \n");
				sbsql.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,"+visit.getMtdec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getMtdec()+") as mt,  \n");
				sbsql.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,  \n");
				sbsql.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,  \n");
				sbsql.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,  \n");
				sbsql.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getFarldec()+") as qnet_ar,  \n");
				sbsql.append("round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(qnet_ar/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1  \n");
				sbsql.append("from rulmzlb r,vwdianc dc  \n");
				sbsql.append("where dc.id=r.diancxxb_id   \n");
				
				//加入时间条件
				sbsql.append("and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') ");
				
				sbsql.append(getCondtion());
				sbsql.append(" group by r.diancxxb_id,r.rulrq) sj  \n");
				sbsql.append("where sj.riq(+)=riq.riq  \n");
				sbsql.append("and haoy.rulrq(+)=riq.riq \n");
				sbsql.append("AND riq.id=sj.diancxxb_id(+) \n");
				sbsql.append("and riq.id=haoy.diancxxb_id(+) \n");
				sbsql.append("group by rollup(fgsmc,mingc,riq.riq) \n");
				sbsql.append("having not (grouping(mingc)+grouping(fgsmc)=1) "+guolzj+"\n");
				sbsql.append("order by grouping(fgsmc) desc,max(fgsxh),fgsmc, \n");
				sbsql.append("grouping(mingc) desc, max(diancxh),mingc, \n");
				sbsql.append("grouping(riq.riq) desc,riq.riq \n");

			}else{
				sbsql.append("select decode(grouping(fgsmc)+grouping(diancmc),2,'总计',1,fgsmc||'总计','&nbsp;&nbsp;&nbsp;&nbsp;'||diancmc) as mingc, \n");
				sbsql.append("decode(grouping(diancmc),1,'',sj.riq) as riq,\n");
	
				sbsql.append("nvl(sum(haoyl),0) as haoyl,sum(nvl(jianzl,0)) as jianzl, \n");
				sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(mt,"+visit.getMtdec()+")*jianzl)/sum(jianzl)),"+visit.getMtdec()+"),0) as mt, \n");
				sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(ad*jianzl)/sum(jianzl)),2),0) as ad, \n");
				sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(vdaf*jianzl)/sum(jianzl)),2),0) as vdaf, \n");
				sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(std*jianzl)/sum(jianzl)),2),0) as std, \n");
				sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,2)*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0) as qnet_ar, \n");
				sbsql.append(" round_new(nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0)/0.0041816,0) as qnet_ar1 \n");
				sbsql.append(" from  \n");
				sbsql.append("       (select dc.mingc as diancmc,to_char(r.rulrq,'yyyy-mm-dd') as riq,max(dc.xuh) as diancxh,dc.fgsmc,dc.fgsxh, nvl(sum(fadhy+gongrhy),0) as haoyl, \n");
				sbsql.append(" sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)) as jianzl,\n");
				sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(round_new(mt,"+visit.getMtdec()+")*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),"+visit.getMtdec()+") as mt, \n");
				sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(ad*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as ad, \n");
				sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(vdaf*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as vdaf, \n");
				sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(std*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as std, \n");
				sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),"+visit.getFarldec()+") as qnet_ar, \n");
				sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(qnet_ar/0.0041816*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),0) as qnet_ar1 \n");
				sbsql.append(" from rulmzlb r,meihyb m,vwdianc dc \n");
				sbsql.append(" where m.rulmzlb_id(+)=r.id \n");
				
//				加入时间条件
				sbsql.append("and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') ");
				
				
				sbsql.append(getCondtion());
				sbsql.append(" and dc.id(+)=r.diancxxb_id \n");
				sbsql.append(" group by dc.mingc,r.rulrq,dc.xuh,dc.fgsmc,dc.fgsxh \n");
				sbsql.append("  having not (grouping(dc.mingc) || grouping(r.rulrq))=1  order by dc.mingc,r.rulrq ) sj \n");
				
				sbsql.append(" group by rollup(fgsmc,diancmc,sj.riq)   \n");
				sbsql.append(" having not (grouping(diancmc) || grouping(riq)) =1 and grouping(fgsmc) =0 \n");
				sbsql.append(" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
				sbsql.append("          grouping(diancmc) desc,max(diancxh) ,diancmc,sj.riq  \n");
			}
			
			
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="入炉煤质量明细"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
//		String ArrHeader[][]=new String[2][12];
//		ArrHeader[0]=new String[] {"单位","编号","发货日期","收煤日期","车数","实收量<br>(吨)","发热量qnet,ar","发热量qnet,ar","水分<br>Mt(%)","挥发分<br>Vdaf(%)", "灰分<br>Ad(%)","硫分St,d(%)"};
//		ArrHeader[1]=new String[] {"单位","编号","发货日期","收煤日期","车数","实收量<br>(吨)","MJ/kg","Kacl/Kg","水分<br>Mt(%)", "挥发分<br>Vdaf(%)", "灰分<br>Ad(%)","硫分St,d(%)"};
//		int ArrWidth[]=new int[] {150,80,80,80,60,60,60,60,60,60,60,60};
		
		String ArrHeader[][]=new String[2][9];
		ArrHeader[0]=new String[] {"单位","日期","入炉煤量(吨)","入炉煤量(吨)","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)","低位发热量","低位发热量"};
		ArrHeader[1]=new String[] {"单位","日期","入炉煤量","检质量","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)","MJ/kg","Kcal/Kg"};

		int ArrWidth[]=new int[] {180,70,70,70,70,70,70,70,70,70};
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 4, strDate1+"至"+strDate2,Table.ALIGN_CENTER); 
		rt.setDefaultTitle(7,  2, "", Table.ALIGN_LEFT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
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
		//begin方法里进行初始化设置
		visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
		//	visit.setString1(null);保存传递的非默认纸张的样式

		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString9().equals("")) {
        		leix = visit.getString9();
            }
        }
	}
	
	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
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