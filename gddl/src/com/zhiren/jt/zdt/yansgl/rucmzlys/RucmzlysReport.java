////2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   明细数据显示
////2008-10-12 chh 
//修改内容 :标题加入口径、运输方式条件

//2008-08-16 ll 
//修改内容 ：打印报表中增加计划口径和运输方式名称


package com.zhiren.jt.zdt.yansgl.rucmzlys;

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
import com.zhiren.common.ResultSetList;
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

/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：夏峥
 * 时间：2011-08-26
 * 描述：修正getLaimlField()方法中对来煤数量的取值方式。
 */
public class RucmzlysReport extends BasePage {
	public final static String LX_DB="db";
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
	
	private String title_date1="";//页面标题日期1
	private String title_date2="";//页面标题日期2
	private String strdate1=""; 
	private String strdate2="";
	private String strYunsfsvalue="";//运输方式名称
	private String strJihkjvalue="";//计划口径名称
	
	/*
	 * 作为对比表的条件
	 */
	private String getCondtion1(){
		
		String strCondtion="";
		
		/*
		 * 页面选择日期，条件选择日期
		 */
		String date1=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1());//页面开始日期
		String date2=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2());//页面结束日期
		
		String date3= ((Visit)getPage().getVisit()).getString12();//条件开始日期
		String date4= ((Visit)getPage().getVisit()).getString11();//条件结束日期
		
		/*条件选择:
		 * 1.本同期对比
		 * 2.环比
		 * 3.时间对比
		 */
		String rbvalue="";
		if (((Visit) getPage().getVisit()).getString13()!=null){
			rbvalue= ((Visit) getPage().getVisit()).getString13();
		}
		if(rbvalue.equals("tiaoj1")||rbvalue.equals("")){//本同期对比
			
			strdate1=" and fh.daohrq>=to_date('"+date1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date2+"','yyyy-mm-dd') \n";
			strdate2=" and fh.daohrq>=add_months(to_date('"+date1+"','yyyy-mm-dd'),-12) and fh.daohrq<=add_months(to_date('"+date2+"','yyyy-mm-dd'),-12)  \n";
			title_date1=date1+"<br>至<br>"+date2;
			title_date2=DateUtil.FormatDate(DateUtil.getFirstDayOfLastYear(date1))+"<br>至<br>"+DateUtil.FormatDate(DateUtil.getFirstDayOfLastYear(date2));
			
		}else if(rbvalue.equals("tiaoj2")){//环比
			
			strdate1=" and fh.daohrq>=to_date('"+date1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date2+"','yyyy-mm-dd') \n";
			strdate2=" and fh.daohrq>=add_months(to_date('"+date1+"','yyyy-mm-dd'),-1) and fh.daohrq<=add_months(to_date('"+date2+"','yyyy-mm-dd'),-1)  \n";
			title_date1=date1+"<br>至<br>"+date2;
			title_date2=DateUtil.FormatDate(DateUtil.getDate(date1))+"<br>至<br>"+DateUtil.FormatDate(DateUtil.getDate(date2));

		}else{//时间段对比
			
			strdate1=" and fh.daohrq>=to_date('"+date1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date2+"','yyyy-mm-dd') \n";
			strdate2=" and fh.daohrq>=to_date('"+date3+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date4+"','yyyy-mm-dd') \n";
			title_date1=date1+"<br>至<br>"+date2;
			title_date2=date3+"<br>至<br>"+date4;
			
		}
		
		//strCondtion= strCondtion+Beginriq+Endriq;//得到时间段
		/*
		 * 运输方式
		 */
		long lngYunsfsId=-1;
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and fh.yunsfsb_id=" +lngYunsfsId;
		}
		/*
		 * 计划口径
		 */
		long lngJihkjId= -1;
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and fh.jihkjb_id=" +lngJihkjId;
		}
		
		return strCondtion;
		
	}
	
	private String getCondtion(){
		String strCondtion="";
		
		int intLen=0;
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		long lngYunsfsId=-1;
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
			if(lngYunsfsId!=-1){
				strYunsfsvalue= ((Visit) getPage().getVisit()).getDropDownBean3().getValue();
			}else{
				strYunsfsvalue="";
			}
		}
		
		long lngJihkjId= -1;
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
			if(lngJihkjId!=-1){
				strJihkjvalue= ((Visit) getPage().getVisit()).getDropDownBean4().getValue();
			}else{
				strJihkjvalue="";
			}
		}
		
		String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();//((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
		
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		
		strCondtion="and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
			"and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
		
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and fh.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and fh.jihkjb_id=" +lngJihkjId;
		}
		
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==3){
				strCondtion=strCondtion+" and dc.id=" +pa[0];
				if (pa[1]!="000"){
					strCondtion=strCondtion+" and fh.jihkjb_id="+pa[1] ;
				} 
				strCondtion=strCondtion+" and y.dqid=" +pa[2];
				   
			
			}else if (pa.length==2){
				strCondtion=strCondtion+" and dc.id=" +pa[0];
				 
				strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}
			else {
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
		 
	private String getFiler(){
		Visit visit=((Visit) getPage().getVisit());
		String strCondtion="";
		String strJihkj="";
		String strYunsfs="";

		long lngYunsfsId=-1;
		if  (visit.getDropDownBean3()!=null){
			lngYunsfsId=visit.getDropDownBean3().getId();
		}
		
		long lngJihkjId= -1;
		if  (visit.getDropDownBean4()!=null){
			lngJihkjId= visit.getDropDownBean4().getId();
		}
		
		
		try {
			strJihkj=MainGlobal.getTableCol("jihkjb", "mingc", "id", String.valueOf(lngJihkjId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			strYunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", String.valueOf(lngYunsfsId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		
		if (!"".equals(strJihkj)){
			strCondtion=strJihkj;
		}
		
		if (!"".equals(strYunsfs)){
			if (strCondtion.length()>0){
				strCondtion=strCondtion+"、";
			}
			strCondtion=strCondtion+strYunsfs;
		}
	
		if (strCondtion.length()>0){
			strCondtion=strCondtion+"、";
		}
		return strCondtion;
	}
	private String getLaimlField(){
		JDBCcon con = new JDBCcon();
		String laiml = SysConstant.LaimField;
		laiml="round_new(sum(fh.laimsl),0)";
		ResultSetList rs = con.getResultSetList("select * from xitxxb where mingc = '使用集团' and zhuangt = 1 and zhi = '中国大唐'");
		if(rs.next()){
			laiml = "sum(round_new(fh.laimsl,"+((Visit) getPage().getVisit()).getShuldec()+"))";
		}
		rs.close();
		con.Close();
		return laiml;
	}
	private String getPrintData(){
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		String dec="";//小数位
		
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		
		String strConditonTitle="";
		if (strDate1.equals(strDate2)){
			strConditonTitle=DateUtil.Formatdate("yyyy年MM月dd日",datStart);
		}else{
			strConditonTitle=strDate1+"至"+strDate2;
		}
		strConditonTitle=strConditonTitle;
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
//		sbSqlBody.append("sum(round(sj.laimsl)) as laimsl,sum(round(sj.laimzl)) as laimzl, \n");
//		sbSqlBody.append("decode(sum(sj.jianzsl),0,0,round(sum(sj.farl)/sum(round(sj.jianzsl)),2)) as farl,\n");
//		sbSqlBody.append("decode(sum(sj.jianzsl),0,0,round(round(sum(sj.farl)/sum(jianzsl),2)*1000/4.1816)) as farldk,\n");
//		sbSqlBody.append("decode(sum(sj.jianzsl),0,0,round(sum(sj.mt)/sum(jianzsl),1)) as shuif, \n");
//		sbSqlBody.append("decode(sum(sj.jianzsl),0,0,round(sum(sj.vdaf)/sum(jianzsl),2)) as huiff,\n");
//		sbSqlBody.append("decode(sum(sj.jianzsl),0,0,round(sum(sj.ad)/sum(jianzsl),2)) as huif, \n");
//		sbSqlBody.append("decode(sum(sj.jianzsl),0,0,round(sum(sj.std)/sum(jianzsl),2)) as liuf from \n");
//		sbSqlBody.append("(select dc.mingc as diancmc,max(dc.xuh) as diancxh,dc.fgsmc,dc.fgsxh,y.mingc as gonysmc,y.smc,y.dqmc,max(sxh) as sxh,max(dqxh) as dqxh,max(y.xuh) as gongysxh, \n");
//		sbSqlBody.append("      "+SysConstant.LaimField+" as laimsl,decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl, round(decode(sum(nvl(z.qnet_ar,0)),0,0,"+SysConstant.LaimField+")) as laimzl, \n ");
//		sbSqlBody.append("        sum(round(fh.laimzl)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt,\n");
//		sbSqlBody.append(" 		  sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad,\n");
//		sbSqlBody.append("        sum(round(fh.laimzl,0)*z.std) as std \n");
//		sbSqlBody.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y  \n");
//		sbSqlBody.append(" where fh.zhilb_id=z.id(+) and fh.gongysb_id=y.id   \n");
//		sbSqlBody.append(getCondtion());
//		sbSqlBody.append("     and fh.diancxxb_id=dc.id     \n");
//		sbSqlBody.append("     and fh.gongysb_id=y.id  \n");
//		sbSqlBody.append(" group by  dc.mingc,y.mingc,dc.fgsmc,dc.fgsxh,y.mingc,y.smc,y.dqmc,fh.lieid) sj \n");
		
		
		sbSqlBody.append("       sum(laiml) as laimsl, \n");
		sbSqlBody.append("       sum(decode(nvl(sj.farl,0),0,0,sj.laiml))  as jianzsl, \n"); // sum(laimzl) as jianzsl,
		sbSqlBody.append("       decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.farl*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,   \n");
		sbSqlBody.append("       round_new(decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.farl*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),"+((Visit) getPage().getVisit()).getFarldec()+")*1000/4.1816),0) as farldk,   \n");
		sbSqlBody.append("       decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.mt*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),"+((Visit) getPage().getVisit()).getMtdec()+")) as shuif,   \n");
		sbSqlBody.append("       decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.vad*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),2)) as huiff,   \n");
		sbSqlBody.append("       decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.ad*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),2)) as huif,   \n");
		sbSqlBody.append("       decode(sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),0,0,round_new(sum(sj.std*sj.laiml)/sum(decode(nvl(sj.farl,0),0,0,sj.laiml)),2)) as liuf   \n");
		sbSqlBody.append(" from \n"); //( "+strgongs+" ) dc, 
		sbSqlBody.append("       (select fh.diancxxb_id,fh.gongysb_id,    \n");//"+strGroupID+" as id,
		sbSqlBody.append("        "+getLaimlField()+"  as laiml,   \n");
		sbSqlBody.append("       sum(round_new(fh.laimsl,0)) as laimsl, \n");
		sbSqlBody.append("       round_new(decode(sum(nvl(z.qnet_ar,0)),0,0, "+getLaimlField()+" ),0) as laimzl, \n");
		sbSqlBody.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbSqlBody.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/ \n");
		sbSqlBody.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl, \n");
		sbSqlBody.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbSqlBody.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+"))/ \n");
		sbSqlBody.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
		sbSqlBody.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbSqlBody.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.vad,2))/ \n");
		sbSqlBody.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as vad, \n");
		sbSqlBody.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbSqlBody.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.ad,2))/ \n");
		sbSqlBody.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as ad, \n");
		sbSqlBody.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbSqlBody.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.std,2))/ \n");
		sbSqlBody.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as std     \n");
		sbSqlBody.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y   \n");
		sbSqlBody.append("       where fh.zhilb_id=z.id(+) and fh.diancxxb_id=dc.id and fh.gongysb_id=y.id   \n");
		sbSqlBody.append(getCondtion());
		sbSqlBody.append("         group by fh.diancxxb_id,fh.gongysb_id,fh.lieid) sj,vwdianc dc,vwgongys y   \n");
		sbSqlBody.append(" where  sj.diancxxb_id=dc.id and sj.gongysb_id=y.id \n");//"+strGroupID+"(+)=dc.id  
		
//		sbsql.append(" group by rollup("+strgongs+")   \n");
//		sbsql.append(" order by grouping("+strgongs+"),max("+Strxuh+"),"+strgongs+"  \n");
		
		/*if (leix.equals(this.LX_DB)){
			
			 * 获得电厂，煤矿条件
			 
			String where="";
			String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();//((Visit) getPage().getVisit()).getString2();
			int jib=getDiancTreeJib(strDiancxxb_id);
			String strGongys_id=((Visit) getPage().getVisit()).getString3();
			String lx=getLeix();
			int intLen=0;
			intLen=lx.indexOf(",");
			if (intLen>0){
				String [] pa=lx.split(",");
				if (pa.length==2){
					where=where+" and dc.id=" +pa[0];
					where=where+" and y.dqid=" +pa[1];
				}else{
					where=" and dc.id=-1";
				}
			}else{
				if (!strGongys_id.equals("-1")){
					where=where+" and y.dqid=" +strGongys_id;
				}
				if (jib==2){
					where=where+" and (dc.fgsid=" +strDiancxxb_id +" or dc.rlgsid="+strDiancxxb_id +")"; 
				}else if (jib==3){
					where=where+" and dc.id=" +strDiancxxb_id;
				}else if (jib==-1){
					where=where+" and dc.id=" +strDiancxxb_id;
				}
			}
			
			
			 * 得到数据项：低位热，全水分，挥发份，灰份，硫份
			 
			String shuzhi="";
			String sql_shuzhi="";
			if  (((Visit) getPage().getVisit()).getDropDownBean6()!=null){
				shuzhi= ((Visit) getPage().getVisit()).getDropDownBean6().getValue();
			}
			
			if (shuzhi.equals("低位发热量")){
				sql_shuzhi="farl";
				dec="0.00";
			}else if (shuzhi.equals("全水分Mt(%)")){
				sql_shuzhi="mt";
				dec="0.0";
			}else if (shuzhi.equals("挥发份Vdaf(%)")){
				sql_shuzhi="vdaf";
				dec="0.00";
			}else if (shuzhi.equals("灰份Ad(%)")){
				sql_shuzhi="ad";
				dec="0.00";
			}else if (shuzhi.equals("硫份Std(%)")){
				sql_shuzhi="std";
				dec="0.00";
			}
			
			 * 得到类型下拉框的值 ：分厂，分矿
			 
			String str_danwmc="";
			
			if  (((Visit) getPage().getVisit()).getDropDownBean5()!=null){
				str_danwmc= ((Visit) getPage().getVisit()).getDropDownBean5().getValue();
			}
			
			if (str_danwmc.equals("分厂")){
				sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc, \n");
				sbsql.append("decode(sum(sj1.jianzsl),0,0,round(sum(sj1."+sql_shuzhi+")/sum(round(sj1.jianzsl)),2)) as laimsl1,\n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)) as laimsl2,\n");
				sbsql.append("decode(sum(sj1.jianzsl),0,0,round(sum(sj1."+sql_shuzhi+")/sum(round(sj1.jianzsl)),2))- \n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)) as bianhl,\n");
				sbsql.append("round(decode(decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)),0,0,\n");
				sbsql.append("(decode(sum(sj1.jianzsl),0,0,round(sum(sj1."+sql_shuzhi+")/sum(round(sj1.jianzsl)),2))- \n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)))/ \n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2))),2) as bianhbl \n");
				sbsql.append("from (select fh.diancxxb_id, \n");
				sbsql.append(""+SysConstant.LaimField+" as laimsl, \n ");
				sbsql.append("decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl, \n");
				sbsql.append("round(decode(sum(nvl(z.qnet_ar,0)),0,0,"+SysConstant.LaimField+")) as laimzl,\n");
				sbsql.append("sum(round(fh.laimzl)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,\n");
				sbsql.append("sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
				sbsql.append("sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad,\n");
				sbsql.append("sum(round(fh.laimzl,0)*z.std) as std \n");
				sbsql.append("from fahb fh,zhilb z  \n");
				sbsql.append("where fh.zhilb_id=z.id "+getCondtion1()+" "+strdate1+" \n");
				sbsql.append("group by fh.diancxxb_id,fh.lieid) sj1, \n");
				sbsql.append("(select fh.diancxxb_id, \n");
				sbsql.append(""+SysConstant.LaimField+" as laimsl, \n ");
				sbsql.append("decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl, \n");
				sbsql.append("round(decode(sum(nvl(z.qnet_ar,0)),0,0,"+SysConstant.LaimField+")) as laimzl,\n");
				sbsql.append("sum(round(fh.laimzl)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,\n");
				sbsql.append("sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
				sbsql.append("sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad,\n");
				sbsql.append("sum(round(fh.laimzl,0)*z.std) as std \n");
				sbsql.append("from fahb fh,zhilb z  \n");
				sbsql.append("where fh.zhilb_id=z.id "+getCondtion1()+" "+strdate2+" \n");
				sbsql.append("group by fh.diancxxb_id,fh.lieid) sj2,vwdianc dc  \n");
				sbsql.append("where dc.id=sj1.diancxxb_id(+) and dc.id=sj2.diancxxb_id(+)  \n");
				sbsql.append(where);
				sbsql.append("group by rollup(dc.fgsmc,dc.mingc)    \n");
				sbsql.append("order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc  \n");
				
				strTitle="入厂煤质量对比表(分厂)";
			}else if(str_danwmc.equals("分矿")){
				 
				sbsql.append("select decode(grouping(y.smc)+grouping(y.mingc),2,'总计',1,y.smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||y.mingc) as mingc, \n");
				sbsql.append("decode(sum(sj1.jianzsl),0,0,round(sum(sj1."+sql_shuzhi+")/sum(round(sj1.jianzsl)),2)) as laimsl1,\n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)) as laimsl2,\n");
				sbsql.append("decode(sum(sj1.jianzsl),0,0,round(sum(sj1."+sql_shuzhi+")/sum(round(sj1.jianzsl)),2))- \n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)) as bianhl,\n");
				sbsql.append("round(decode(decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)),0,0,\n");
				sbsql.append("(decode(sum(sj1.jianzsl),0,0,round(sum(sj1."+sql_shuzhi+")/sum(round(sj1.jianzsl)),2))- \n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2)))/ \n");
				sbsql.append("decode(sum(sj2.jianzsl),0,0,round(sum(sj2."+sql_shuzhi+")/sum(round(sj2.jianzsl)),2))),2) as bianhbl \n");
				sbsql.append("from (select fh.diancxxb_id,fh.gongysb_id, \n");
				sbsql.append(""+SysConstant.LaimField+" as laimsl, \n ");
				sbsql.append("decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl, \n");
				sbsql.append("round(decode(sum(nvl(z.qnet_ar,0)),0,0,"+SysConstant.LaimField+")) as laimzl,\n");
				sbsql.append("sum(round(fh.laimzl)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,\n");
				sbsql.append("sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
				sbsql.append("sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad,\n");
				sbsql.append("sum(round(fh.laimzl,0)*z.std) as std \n");
				sbsql.append("from fahb fh,zhilb z  \n");
				sbsql.append("where fh.zhilb_id=z.id "+getCondtion1()+" "+strdate1+" \n");
				sbsql.append("group by fh.diancxxb_id,fh.gongysb_id,fh.lieid) sj1, \n");
				sbsql.append("(select fh.diancxxb_id,fh.gongysb_id, \n");
				sbsql.append(""+SysConstant.LaimField+" as laimsl, \n ");
				sbsql.append("decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl, \n");
				sbsql.append("round(decode(sum(nvl(z.qnet_ar,0)),0,0,"+SysConstant.LaimField+")) as laimzl,\n");
				sbsql.append("sum(round(fh.laimzl)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,\n");
				sbsql.append("sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
				sbsql.append("sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad,\n");
				sbsql.append("sum(round(fh.laimzl,0)*z.std) as std \n");
				sbsql.append("from fahb fh,zhilb z  \n");
				sbsql.append("where fh.zhilb_id=z.id "+getCondtion1()+" "+strdate2+" \n");
				sbsql.append("group by fh.diancxxb_id,fh.gongysb_id,fh.lieid) sj2,vwdianc dc,vwgongys y  \n");
				sbsql.append("where dc.id=sj1.diancxxb_id and dc.id=sj2.diancxxb_id and y.id=sj1.gongysb_id and y.id=sj2.gongysb_id  \n");
				sbsql.append(where).append("\n");
				sbsql.append("group by rollup(y.smc,y.mingc)    \n");
				sbsql.append("order by grouping(y.smc) desc,max(y.sxh) ,y.smc,grouping(y.mingc) desc,max(y.dqxh) ,y.mingc  \n");
				
				strTitle="入厂煤质量对比表(分矿)";
			}
			
		}else*/ if (leix.equals(this.LX_FC)){
			sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc, \n");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbsql.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="入厂煤质量统计(分厂表)";
		}else if(leix.equals(this.LX_FK)){
			sbsql.append("select decode(grouping(smc)+grouping(dqmc),2,'总计',1,smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(smc,dqmc)   \n");
			sbsql.append(" order by grouping(smc) desc,max(sxh) ,smc, \n");
			sbsql.append("          grouping(dqmc) desc,max(dqxh) ,dqmc \n");
			strTitle="入厂煤质量统计(分矿表)";
//			System.out.println(sbsql.toString());
		}else if(leix.equals(this.LX_FCFK)){
			sbsql.append("select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'总计',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append("group by rollup(fgsmc,dc.mingc,dqmc)   \n");
			sbsql.append(" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbsql.append("          grouping(dqmc) desc,max(dqxh) ,dqmc \n");
			strTitle="入厂煤质量统计(分厂分矿表)";
		}else if(leix.equals(this.LX_FKFC)){
			sbsql.append("select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'总计',2,dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(dqmc,fgsmc,dc.mingc)   \n");
			sbsql.append(" order by  grouping(dqmc) desc,max(dqxh) ,dqmc, \n");
			sbsql.append("          grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="入厂煤质量统计(分矿分厂表)";
		}else if(leix.equals(this.LX_QP)){
			strTitle="入厂煤质量统计(棋盘)";
		}else if(leix.indexOf(",")>0){
			return getPrintDataTz();
		}
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName=strTitle;
		
		
//		 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][]=null;
		int ArrWidth[] =null;
		if (leix.equals(this.LX_DB)){//对比表
			ArrHeader = new String[2][5];
			
			ArrHeader[0] = new String[] { "单位", "实收量(吨)","实收量(吨)", "变化", "变化" };
			ArrHeader[1] = new String[] { "单位", title_date1,title_date2, "变化量(吨)", "变化比例(%)" };
			ArrWidth = new int[] { 150, 80, 80, 80, 80 };
			
		}else{//其它
			ArrHeader = new String[2][9];
			
			ArrHeader[0] = new String[] { "单位", "实收量<br>(吨)", "检质量<br>(吨)", "发热量qnet,ar", "发热量qnet,ar" ,"水分<br>Mt(%)","挥发分<br>Vad(%)", "灰分<br>Ad(%)","硫分<br>St,d(%)"};
			ArrHeader[1] = new String[] { "单位", "实收量<br>(吨)", "检质量<br>(吨)", "MJ/kg", "Kacl/Kg","水分<br>Mt(%)", "挥发分<br>Vad(%)", "灰分<br>Ad(%)","硫分<br>St,d(%)" };
			ArrWidth = new int[] { 150, 65, 65, 65, 65, 65, 65, 65, 65 };
			
		}
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		
		String biaot="";
		if(((Visit) getPage().getVisit()).getDiancqc().equals("北京大唐燃料有限公司")){
			biaot = "大唐国际发电股份有限公司燃料管理部";
		}else{
			biaot=((Visit) getPage().getVisit()).getDiancqc();
		}
		
		rt.setDefaultTitle(1, 3, "制表单位:"+ biaot,Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, DateUtil.Formatdate("yyyy年MM月dd日", datStart)+"-"+DateUtil.Formatdate("yyyy年MM月dd日", datEnd),Table.ALIGN_CENTER);
	//	rt.setDefaultTitle(6,  2, strJihkjvalue+" "+strYunsfsvalue, Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 3,"单位:吨",Table.ALIGN_RIGHT); 
		rt.body.setUseDefaultCss(true);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		if (leix.equals(this.LX_DB)){//对比表
			rt.body.setColFormat(2, dec);
			rt.body.setColFormat(3, dec);
			rt.body.setColFormat(4, dec);
			rt.body.setColFormat(5, dec);
			
		}else{
			rt.body.setColFormat(4, "0.00");
			rt.body.setColFormat(6, "0.0");
			rt.body.setColFormat(7, "0.00");
			rt.body.setColFormat(8, "0.00");
			rt.body.setColFormat(9, "0.00");
		}
		
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "制表时间:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "审批:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 3, "制表:"+((Visit) getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
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
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		
		StringBuffer sbsql = new StringBuffer();
//		sbsql.append("select decode(grouping(y.mingc)+grouping(fh.id),2,'总计',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Rucmzlysmx','vwgongys_id',y.id,y.mingc)) as mingc,\n");
//		sbsql.append("decode(grouping(y.mingc)+grouping(fh.id),2,'',1,'小计',max(z.huaybh)) as huaybh,to_char(fh.daohrq,'yyyy-mm-dd') as daohrq,to_char(z.huaysj,'yyyy-mm-dd') as huaysj,sum(ches) as ches, sum(round(fh.laimsl)) as laimsl, \n");//to_char(fahrq,'yyyy-mm-dd') as fahrq,
//		sbsql.append("           decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),0,0,round(sum(round(fh.laimzl)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),2)) as farl,    \n");
//		sbsql.append("     round(decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),0,0,round(sum(round(fh.laimzl)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),2))*7000/29.271) as farldk,    \n");
//		sbsql.append("           decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),0,0,round(sum(round(fh.laimzl)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+"))/sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),1)) as shuif,    \n");
//		sbsql.append("           decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),0,0,round(sum(round(fh.laimzl)*z.vdaf)/sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),2)) as huiff,    \n");
//		sbsql.append("           decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),0,0,round(sum(round(fh.laimzl)*z.ad)/sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),2)) as huif,    \n");
//		sbsql.append("           decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),0,0,round(sum(round(fh.laimzl)*z.std)/sum(round(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl))),2)) as liuf    \n");
//		sbsql.append("           from fahb fh,zhilb z,vwdianc dc,vwgongys y   \n");
//		sbsql.append("     where fh.zhilb_id(+)=z.id and fh.gongysb_id=y.id   \n");
//		sbsql.append("         and fh.diancxxb_id=dc.id      \n");
//		sbsql.append("         and fh.gongysb_id=y.id   \n");
//		sbsql.append(getCondtion());
//		sbsql.append("     group by rollup((y.id,y.mingc),(fh.id,fh.zhilb_id,daohrq,huaysj)) having not grouping(fh.id)=0 order by grouping(y.mingc) ,max(y.xuh),y.mingc,grouping(fh.id),daohrq,huaysj ");
		 sbsql.append("select decode(grouping(y.mingc),1,'总计',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Rucmzlysmx','vwgongys_id',y.id||','||max(fh.jihkjb_id),y.mingc)) as danwmc, \n");
//		sbsql.append("select decode(grouping(y.mingc),1,'总计',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Rucmzlysmx','vwgongys_id',y.id,y.mingc)) as mingc, \n");
		sbsql.append("       sum(ches) as ches, \n");
//		sbsql.append("       sum(laiml) as laimsl, \n");
//		sbsql.append("       sum(decode(nvl(fh.farl,0),0,0,fh.laimzl))  as jianzsl, \n");
		sbsql.append("       sum(laiml) as laimsl, \n");
		sbsql.append("       sum(laimzl) as jianzsl, \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*farl)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl, \n");
		sbsql.append("       round_new(decode(sum(decode(nvl(fh.farl,0),0,0,fh.laimsl)),0,0,round_new(sum(laiml*farl)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),"+((Visit) getPage().getVisit()).getFarldec()+")/0.0041816),0) as farldk,  \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*mt)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*vad)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),2)) as vad, \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*ad)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),2)) as ad, \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*std)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),2)) as std \n");
		sbsql.append("  from  \n");//max(y.xuh) as xuh,y.mingc,fh.diancxxb_id,fh.gongysb_id
		sbsql.append("       (select fh.diancxxb_id,fh.meikxxb_id, max(fh.jihkjb_id)jihkjb_id,sum(ches) as ches,   max(fh.daohrq) as daohrq,  \n");//"+strGroupID+" as id,
		sbsql.append("        "+getLaimlField()+"  as laiml,   \n");
		sbsql.append("       sum(round(fh.laimsl)) as laimsl, \n");
		sbsql.append("       round(decode(sum(nvl(z.qnet_ar,0)),0,0, "+getLaimlField()+" )) as laimzl, \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/ \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl, \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+"))/ \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.vad,2))/ \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as vad, \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.ad,2))/ \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as ad, \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0, \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.std,2))/ \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as std     \n");
		sbsql.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y  \n");
		sbsql.append("       where fh.zhilb_id=z.id(+) and fh.diancxxb_id=dc.id and fh.gongysb_id=y.id \n");
		sbsql.append("         and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')   \n");
		sbsql.append("         and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')   \n").append(getCondtion());
		sbsql.append("         group by fh.diancxxb_id,fh.meikxxb_id,fh.lieid) fh,vwdianc dc,meikxxb y   \n");
		sbsql.append(" where fh.diancxxb_id=dc.id and fh.meikxxb_id=y.id  \n");//"+strGroupID+"(+)=dc.id  
		sbsql.append(" group by rollup ((y.id,y.mingc))  order by max(daohrq) ,y.mingc desc  \n");
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="入厂煤质量明细"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
//		System.out.println(sbsql.toString());
		String ArrHeader[][]=new String[2][10];
		ArrHeader[0]=new String[] {"单位","车数","实收量<br>(吨)","检质量<br>(吨)","发热量qnet,ar","发热量qnet,ar","水分<br>Mt(%)","挥发分<br>Vad(%)", "灰分<br>Ad(%)","硫分St,d(%)"};
		ArrHeader[1]=new String[] {"单位","车数","实收量<br>(吨)","检质量<br>(吨)","MJ/kg","Kacl/Kg","水分<br>Mt(%)", "挥发分<br>Vad(%)", "灰分<br>Ad(%)","硫分St,d(%)"};
		int ArrWidth[]=new int[] {150,60,60,60,60,60,60,60,60,60};
		
		rt.setBody(new Table(rs, 2, 0, 4));
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle(titleName, ArrWidth);
		String biaot="";
		if(((Visit) getPage().getVisit()).getDiancqc().equals("北京大唐燃料有限公司")){
			biaot = "大唐国际发电股份有限公司燃料管理部";
		}else{
			biaot=((Visit) getPage().getVisit()).getDiancqc();
		}
		rt.setDefaultTitle(1, 2, "制表单位:"+biaot,Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 5, DateUtil.Formatdate("yyyy年MM月dd日", datStart)+"-"+DateUtil.Formatdate("yyyy年MM月dd日", datEnd),Table.ALIGN_CENTER); 
		rt.setDefaultTitle(8,  2, "", Table.ALIGN_LEFT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(7, "0.0");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(9, "0.00");
		rt.body.setColFormat(10, "0.00");
		
		
		rt.body.setUseDefaultCss(true);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.mergeFixedCol(3);
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "制表时间:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

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