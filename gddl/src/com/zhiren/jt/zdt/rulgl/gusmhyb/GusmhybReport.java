package com.zhiren.jt.zdt.rulgl.gusmhyb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/* 
* ʱ�䣺2009-05-4
* ���ߣ� ll
* �޸����ݣ�1���޸Ĳ�ѯsql��yuezbb�����ֶε�����,����yuezbb���µĹ�ʽ��ȡ�µ��ֶ�����
* 		   
*/ 
/* 
* ʱ�䣺2009-05-20
* ���ߣ� ll
* �޸����ݣ�1���޸Ĳ�ѯsql�й�������ʽ��
* 		   
*/ 
/* 
* ʱ�䣺2009-09-4
* ���ߣ� ll
* �޸����ݣ�1��������˾��½ʱȥ���ܼơ��С�
* 		   
*/ 
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class GusmhybReport extends BasePage {
	public boolean getRaw() {
		return true;
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
		String strReortLeix=getReportLeix();
		if (strReortLeix.equals("gs")){
			return getMhybReport();
		}else if (strReortLeix.equals("sb")){
			return getShangbmh();
		}else if(strReortLeix.equals("db")){
			return getDuibmh();
		}
		return "";
	}

	public void submit(IRequestCycle cycle) {
		return;
	}

	private String getBeginDate(){
		return  ((Visit) getPage().getVisit()).getString10();
	}

	private String getEndDate(){
		return   ((Visit) getPage().getVisit()).getString11();
	}

	private String getReportLeix(){
		return   ((Visit) getPage().getVisit()).getString8(); 
	}

	//�¹�ú��
	private String getMhybReport() {
		JDBCcon con = new JDBCcon();

		String strDiancxxbID= ((Visit) getPage().getVisit()).getString9();
		String strCondition ="";
		String strDateStart=getBeginDate();
		String strDateEnd=getEndDate();
		String strDateMonthStart=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(getEndDate())));
		int jib=this.getDiancTreeJib();
		String guolzj="";
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧

		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strCondition =strCondition+ " and (dc.fgsid="+strDiancxxbID+" or dc.rlgsid="+strDiancxxbID + ")";
			guolzj=" having grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
			guolzj=" having grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
		}
		
		StringBuffer sbsql = new StringBuffer();
		
//		StringBuffer sqlzhi_by = new StringBuffer();//��Ȩ������
//		StringBuffer sqlzhi_lj = new StringBuffer();//��Ȩ���ۼ�
//		StringBuffer sql_str = new StringBuffer();
		
//		if(!((Visit) getPage().getVisit()).getRuljiaql().equals("��¯����ú��")){
//			
//			sql_str.append("sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,\n");
//			sql_str.append("decode(sum(nvl(hy.meil,0)),0,0, sum(nvl(hy.rulrl*hy.meil,0))/sum(nvl(hy.meil,0))) as rulrl,\n");
//			
//			//����
//			sqlzhi_by.append("(select hy.diancxxb_id,hy.rulrq,fady,gongrhy,rz.meil,rz.rulrl from \n");
//			sqlzhi_by.append("(select hy.diancxxb_id,hy.rulrq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
//			sqlzhi_by.append("		from meihyb hy where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_by.append(" 		group by hy.diancxxb_id,hy.rulrq) hy,\n");
//			sqlzhi_by.append("(select rz.diancxxb_id,rz.rulrq,sum(rz.meil) as meil,\n");
//			sqlzhi_by.append("		decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0, \n");
//			sqlzhi_by.append("	 	round_new(sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),"+((Visit) getPage().getVisit()).getFarldec()+")) as rulrl\n");
//			sqlzhi_by.append("		from rulmzlb rz where rz.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_by.append("		group by rz.diancxxb_id,rz.rulrq) rz \n");
//			sqlzhi_by.append("		where hy.diancxxb_id=rz.diancxxb_id(+) and hy.rulrq=rz.rulrq(+) ) hy, \n");
//			//�ۼ�
//			sqlzhi_lj.append("(select hy.diancxxb_id,fady,gongrhy,rz.meil,rz.rulrl from \n");
//			sqlzhi_lj.append("(select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
//			sqlzhi_lj.append("from meihyb hy where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_lj.append(" group by hy.diancxxb_id) hy,\n");
//			sqlzhi_lj.append("(select rz.diancxxb_id,sum(rz.meil) as meil,\n");
//			sqlzhi_lj.append("decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0, \n");
//			sqlzhi_lj.append(" round_new(sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),"+((Visit) getPage().getVisit()).getFarldec()+")) as rulrl\n");
//			sqlzhi_lj.append("from rulmzlb rz where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_lj.append("group by rz.diancxxb_id) rz \n");
//			sqlzhi_lj.append("where hy.diancxxb_id=rz.diancxxb_id(+)) hy, \n");
//		}else{
//			sql_str.append("sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry,\n");
//			sql_str.append("decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,  \n");
//			
//			sqlzhi_by.append("  (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
//			sqlzhi_by.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
//			sqlzhi_by.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
//			sqlzhi_by.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
//			sqlzhi_by.append("        from meihyb hy,rulmzlb zl  \n");
//			sqlzhi_by.append("        where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')    \n");
//			sqlzhi_by.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
//			sqlzhi_by.append("              and hy.rulmzlb_id=zl.id(+)  \n");
//			sqlzhi_by.append("       group by hy.diancxxb_id) hy,  \n");
//			
//			sqlzhi_lj.append(" (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
//			sqlzhi_lj.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
//			sqlzhi_lj.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
//			sqlzhi_lj.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
//			sqlzhi_lj.append("        from meihyb hy,rulmzlb zl  \n");
//			sqlzhi_lj.append("        where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
//			sqlzhi_lj.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
//			sqlzhi_lj.append("              and hy.rulmzlb_id=zl.id(+)  \n");
//			sqlzhi_lj.append("       group by hy.diancxxb_id) hy,  \n");
//		}
//			
//
//		
//		sbsql.append("select mingc,fenx,round(fardl/10000,2) as fardl,round(gongdl/10000,2) as gongdl,gongrl,fady,gongry,rulrl,round(rulrl*1000/4.1816,0) as rulrldk,round(fadyy,0) as fadyy,round(gongryy) as gongryy,rulrly,round(rulrly*1000/4.1816,0) as rulrlydk,  \n");
//		sbsql.append(" fadzbm,gongrzbm,fadyzbm,gongryzbm,fadzhzbm,gongrzhzbm, \n");
//		sbsql.append(" fadbzmh,gongdbzmh,gongrbzmh from (\n");
//		sbsql.append("select mingc,rownum as xuh, decode(1,1,'����','����') as fenx,fardl,gongdl,gongrl,fady,gongry,round(rulrl,2) as rulrl,fadyy,gongryy,rulrly, \n");
//		sbsql.append("  round(rulrl*fady/29.271) as fadzbm,  round(rulrl*gongry/29.271) as gongrzbm, \n");
//		sbsql.append("  round(rulrly*fadyy/29.271) as fadyzbm,round(rulrly*gongryy/29.271) as gongryzbm, \n");
//		sbsql.append("  round(rulrl*fady/29.271)+round(rulrly*fadyy/29.271) as fadzhzbm,round(rulrl*gongry/29.271)+round(rulrly*gongryy/29.271)  as gongrzhzbm, \n");
//		sbsql.append("  round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,   \n");
//		sbsql.append("  round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,   \n");
//		sbsql.append("  round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh  \n");
//		sbsql.append("from  \n");
//		sbsql.append("(select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,  \n");
//		
////		sbsql.append(" sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry,\n");
////		sbsql.append("     decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,  \n");
//		
//		sbsql.append(sql_str.toString());
//		
//		sbsql.append("     sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,  \n");
//		sbsql.append("     sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,  \n");
//		sbsql.append("     decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly  \n");
//		sbsql.append("  from   \n");
//		//����
////		sbsql.append("  (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
////		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
////		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
////		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
////		sbsql.append("        from meihyb hy,rulmzlb zl  \n");
////		sbsql.append("        where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')    \n");
////		sbsql.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
////		sbsql.append("              and hy.rulmzlb_id=zl.id(+)  \n");
////		sbsql.append("       group by hy.diancxxb_id) hy,  \n");
//		
//		sbsql.append(sqlzhi_by.toString());
//		
//		sbsql.append("     (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl  \n");
//		sbsql.append("               from  shouhcrbyb  yrb  \n");
//		sbsql.append("                where yrb.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')     \n");
//		sbsql.append("              and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
//		sbsql.append("       group by diancxxb_id) yb,   \n");
//		sbsql.append("      (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������  \n");
//		sbsql.append("              from riscsjb sc  \n");
//		sbsql.append("             where sc.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')    \n");
//		sbsql.append("             and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
//		sbsql.append("      group by  sc.diancxxb_id) sc, vwdianc dc \n");
//		sbsql.append(" where dc.id=hy.diancxxb_id(+)  \n");
//		sbsql.append("    and dc.id=sc.diancxxb_id(+)  \n").append(strCondition);
//		sbsql.append("    and dc.id=yb.diancxxb_id(+) group by rollup(dc.fgsmc,dc.mingc) \n");
//		sbsql.append("    order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ) \n");
//		sbsql.append("union all \n");
//		sbsql.append("select mingc,rownum as xuh, decode(1,1,'�ۼ�','�ۼ�') as fenx,fardl,gongdl,gongrl,fady,gongry,round(rulrl,2) as rulrl,fadyy,gongryy,rulrly, \n");
//		sbsql.append("  round(rulrl*fady/29.271) as fadzbm,  round(rulrl*gongry/29.271) as gongrzbm, \n");
//		sbsql.append("  round(rulrly*fadyy/29.271) as fadyzbm,round(rulrly*gongryy/29.271) as gongryzbm, \n");
//		sbsql.append("  round(rulrl*fady/29.271)+round(rulrly*fadyy/29.271) as fadzhzbm,round(rulrl*gongry/29.271)+round(rulrly*gongryy/29.271)  as gongrzhzbm, \n");
//		sbsql.append("  round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,   \n");
//		sbsql.append("  round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,   \n");
//		sbsql.append("  round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh  \n");
//		sbsql.append("from  \n");
//		sbsql.append("(select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc, \n");
//		
////		sbsql.append("  sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry, \n");
////		sbsql.append("     decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,  \n");
//		
//		sbsql.append(sql_str.toString());
//		
//		sbsql.append("     sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,  \n");
//		sbsql.append("     sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,  \n");
//		sbsql.append("     decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly  \n");
//		sbsql.append("  from  \n");
//		//�ۼ�
////		sbsql.append(" (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
////		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
////		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
////		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
////		sbsql.append("        from meihyb hy,rulmzlb zl  \n");
////		sbsql.append("        where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
////		sbsql.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
////		sbsql.append("              and hy.rulmzlb_id=zl.id(+)  \n");
////		sbsql.append("       group by hy.diancxxb_id) hy,  \n");
//		
//		sbsql.append(sqlzhi_lj.toString());
//		
//		sbsql.append("     (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl  \n");
//		sbsql.append("               from  shouhcrbyb  yrb  \n");
//		sbsql.append("                where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')     \n");
//		sbsql.append("              and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
//		sbsql.append("       group by diancxxb_id) yb,   \n");
//		sbsql.append("      (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������  \n");
//		sbsql.append("              from riscsjb sc  \n");
//		sbsql.append("             where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
//		sbsql.append("             and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
//		sbsql.append("      group by  sc.diancxxb_id) sc, vwdianc dc \n");
//		sbsql.append(" where dc.id=hy.diancxxb_id(+)  \n");
//		sbsql.append("    and dc.id=sc.diancxxb_id(+)  \n").append(strCondition);
//		sbsql.append("    and dc.id=yb.diancxxb_id(+) group by rollup(dc.fgsmc,dc.mingc) \n");
//		sbsql.append("    order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ) \n");
//		sbsql.append("order by xuh,fenx) \n");

		
		int intRezDec=((Visit) getPage().getVisit()).getFarldec();
		if(((Visit) getPage().getVisit()).getRuljiaql().equals("��¯����ú��")){
			sbsql.append("select mingc,fenx,round(fardl/10000,2) as fardl,round(gongdl/10000,2) as gongdl,gongrl,fady,gongry,rulrl,round(rulrl*1000/4.1816,0) as rulrldk,round(fadyy,0) as fadyy,round(gongryy) as gongryy,rulrly,round(rulrly*1000/4.1816,0) as rulrlydk,   \n");
			sbsql.append("       fadzbm,gongrzbm,fadyzbm,gongryzbm,fadzhzbm,gongrzhzbm,  \n");
			sbsql.append("       fadbzmh,gongdbzmh,gongrbzmh  \n");
			sbsql.append("from (select mingc,rownum as xuh, decode(1,1,'����','����') as fenx,fardl,gongdl,gongrl,fady,gongry,round_new(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,  \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,  \n");
			sbsql.append("                  round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,  \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0)+round_new(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,  \n");
			sbsql.append("                  round_new(decode(fardl,0,0, 1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,    \n");
			sbsql.append("                  round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,    \n");
			sbsql.append("                  round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh   \n");
			sbsql.append("    from  (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,   \n");
			sbsql.append("                 sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry, \n");
			sbsql.append("                 decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl, \n");
			sbsql.append("                 sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,   \n");
			sbsql.append("                 sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,   \n");
			sbsql.append("                 decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly   \n");
			sbsql.append("          from  (select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy  \n");
			sbsql.append("                     from meihyb hy  \n");
			sbsql.append("                     where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')  \n");
			sbsql.append("                     and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
			sbsql.append("                     group by hy.diancxxb_id) hy, \n");
			sbsql.append("                 (select rz.diancxxb_id,sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)) as meil, \n");
			sbsql.append("                         decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0,  \n");
			sbsql.append("                         round_new(sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),2)) as rulrl \n");
			sbsql.append("                       from rulmzlb rz  \n");
			sbsql.append("                       where rz.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd') \n");
			sbsql.append("                       and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
			sbsql.append("                       group by rz.diancxxb_id) rz, \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl   \n");
			sbsql.append("                       from  shouhcrbyb  yrb   \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')     \n");
			sbsql.append("                       group by diancxxb_id) yb,    \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������   \n");
			sbsql.append("                       from riscsjb sc   \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc  \n");
			sbsql.append("          where dc.id=hy.diancxxb_id(+)   \n");
			sbsql.append("                and dc.id=sc.diancxxb_id(+)   \n");
			sbsql.append("                and dc.id=rz.diancxxb_id(+) \n");
			sbsql.append("                and dc.id=yb.diancxxb_id(+)  \n").append(strCondition);
			sbsql.append("          group by rollup(dc.fgsmc,dc.mingc)  \n");
			sbsql.append("          "+guolzj+"\n");
			sbsql.append("          order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc )  \n");
			sbsql.append("    union all     \n");
			sbsql.append("    select mingc,rownum as xuh, decode(1,1,'�ۼ�','�ۼ�') as fenx,fardl,gongdl,gongrl,fady,gongry,round(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,  \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,  \n");
			sbsql.append("                  round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,  \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0)+round_new(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,  \n");
			sbsql.append("                  round_new(decode(fardl,0,0, 1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,    \n");
			sbsql.append("                  round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,    \n");
			sbsql.append("                  round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh   \n");
			sbsql.append("    from  (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,   \n");
			sbsql.append("                 sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry, \n");
			sbsql.append("                 decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl, \n");
			sbsql.append("                 sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,   \n");
			sbsql.append("                 sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,   \n");
			sbsql.append("                 decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly   \n");
			sbsql.append("          from  (select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy  \n");
			sbsql.append("                     from meihyb hy  \n");
			sbsql.append("                     where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
			sbsql.append("                     and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
			sbsql.append("                     group by hy.diancxxb_id) hy, \n");
			sbsql.append("                 (select rz.diancxxb_id,sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)) as meil, \n");
			sbsql.append("                         decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0,  \n");
			sbsql.append("                         round_new(sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),2)) as rulrl \n");
			sbsql.append("                       from rulmzlb rz  \n");
			sbsql.append("                       where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
			sbsql.append("                       and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
			sbsql.append("                       group by rz.diancxxb_id) rz, \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl   \n");
			sbsql.append("                       from  shouhcrbyb  yrb   \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                       group by diancxxb_id) yb,    \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������   \n");
			sbsql.append("                       from riscsjb sc   \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc  \n");
			sbsql.append("          where dc.id=hy.diancxxb_id(+)   \n");
			sbsql.append("                and dc.id=sc.diancxxb_id(+)   \n");
			sbsql.append("                and dc.id=rz.diancxxb_id(+) \n");
			sbsql.append("                and dc.id=yb.diancxxb_id(+)  \n").append(strCondition);
			sbsql.append("          group by rollup(dc.fgsmc,dc.mingc)  \n");
			sbsql.append("          "+guolzj+"\n");
			sbsql.append("          order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ) ) \n");
			sbsql.append("order by xuh,fenx \n");
		}else{
			sbsql.append("select mingc,fenx,round(fardl/10000,2) as fardl,round(gongdl/10000,2) as gongdl,gongrl,fady,gongry,rulrl,round_new(rulrl*1000/4.1816,0) as rulrldk,round(fadyy,0) as fadyy,round(gongryy) as gongryy,rulrly,round(rulrly*1000/4.1816,0) as rulrlydk,   \n");
			sbsql.append(" fadzbm,gongrzbm,fadyzbm,gongryzbm,fadzhzbm,gongrzhzbm,  \n");
			sbsql.append(" fadbzmh,gongdbzmh,gongrbzmh  \n");
			sbsql.append("from (select mingc,rownum as xuh, decode(1,1,'����','����') as fenx,fardl,gongdl,gongrl,fady,gongry,round(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,  \n");
			sbsql.append("          round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,  \n");
			sbsql.append("          round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,  \n");
			sbsql.append("          round_new(rulrl*fady/29.271,0)+round(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,  \n");
			sbsql.append("          round_new(decode(fardl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,    \n");
			sbsql.append("          round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,    \n");
			sbsql.append("          round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh   \n");
			sbsql.append("     from (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,   \n");
			sbsql.append("               sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry, \n");
			sbsql.append("               decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,   \n");
			sbsql.append("               sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,   \n");
			sbsql.append("               sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,   \n");
			sbsql.append("               decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly   \n");
			sbsql.append("          from (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯����  \n");
			sbsql.append("                    sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,   \n");
			sbsql.append("                    decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,   \n");
			sbsql.append("                    sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl   \n");
			sbsql.append("                	from meihyb hy,rulmzlb zl   \n");
			sbsql.append("                	where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                        and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                        and hy.rulmzlb_id=zl.id(+)   \n");
			sbsql.append("                	group by hy.diancxxb_id) hy,   \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl   \n");
			sbsql.append("                       from  shouhcrbyb  yrb   \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')     \n");
			sbsql.append("                       group by diancxxb_id) yb,    \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������   \n");
			sbsql.append("                       from riscsjb sc   \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc  \n");
			sbsql.append(" where dc.id=hy.diancxxb_id(+)   \n");
			sbsql.append("    and dc.id=sc.diancxxb_id(+)   \n");
			sbsql.append("    and dc.id=yb.diancxxb_id(+) \n").append(strCondition);
			sbsql.append("    group by rollup(dc.fgsmc,dc.mingc)  \n");
			sbsql.append("          "+guolzj+"\n");
			sbsql.append("    order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc )  \n");
			sbsql.append("union all  \n");
			sbsql.append("select mingc,rownum as xuh, decode(1,1,'�ۼ�','�ۼ�') as fenx,fardl,gongdl,gongrl,fady,gongry,round_new(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,  \n");
			sbsql.append("     round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,  \n");
			sbsql.append("          round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,  \n");
			sbsql.append("          round_new(rulrl*fady/29.271,0)+round(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,  \n");
			sbsql.append("          round_new(decode(fardl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,    \n");
			sbsql.append("          round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,    \n");
			sbsql.append("          round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh   \n");
			sbsql.append("     from (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,   \n");
			sbsql.append("               sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry, \n");
			sbsql.append("               decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,   \n");
			sbsql.append("               sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,   \n");
			sbsql.append("               sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,   \n");
			sbsql.append("               decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly   \n");
			sbsql.append("          from (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯����  \n");
			sbsql.append("                    sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,   \n");
			sbsql.append("                    decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,   \n");
			sbsql.append("                    sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl   \n");
			sbsql.append("                from meihyb hy,rulmzlb zl   \n");
			sbsql.append("                		where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                        and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                        and hy.rulmzlb_id=zl.id(+)   \n");
			sbsql.append("                		group by hy.diancxxb_id) hy,   \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl   \n");
			sbsql.append("                       from  shouhcrbyb  yrb   \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                       group by diancxxb_id) yb,    \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������   \n");
			sbsql.append("                       from riscsjb sc   \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')     \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc  \n");
			sbsql.append(" where dc.id=hy.diancxxb_id(+)   \n");
			sbsql.append("    and dc.id=sc.diancxxb_id(+)   \n");
			sbsql.append("    and dc.id=yb.diancxxb_id(+) \n");
			sbsql.append("    and dc.id=yb.diancxxb_id(+) \n").append(strCondition);
			sbsql.append("    group by rollup(dc.fgsmc,dc.mingc)  \n");
			sbsql.append("          "+guolzj+"\n");
			sbsql.append("    order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc )  \n");
			sbsql.append("order by xuh,fenx)  \n");
		}
	
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);		
		Report rt = new Report();
		String ArrHeader[][]=new String[2][22];
		ArrHeader[0]=new String[] {"��λ","��λ","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)","������<br>(����)","����Ȼú��(��)","����Ȼú��(��)","��ú��ֵ","��ú��ֵ","����Ȼ����(��)","����Ȼ����(��)","������ֵ","������ֵ","����ú�۱�ú��(��)","����ú�۱�ú��(��)","�������۱�ú��(��)","�������۱�ú��(��)","�ۺϱ�ú��(��)","�ۺϱ�ú��(��)","��׼ú��","��׼ú��","��׼ú��"};
		ArrHeader[1]=new String[] {"��λ","��λ","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)","������<br>(����)","����","����","(�׽�/ǧ��)","(ǧ��/ǧ��)","����","����","(�׽�/ǧ��)","(ǧ��/ǧ��)","����","����","����","����","����","����","����<br>(��/��)","����<br>(��/��)","����<br>(ǧ��/����)"};
		int ArrWidth[]=new int[] {180,45,80,80,50,50,50,50,50,50,45,45,45,45,45,45,45,45,45,45,45,45};

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		// ����
		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		//�ڶ��о���
		bt.setColAlign(2,Table.ALIGN_CENTER);
		bt.setColAlign(3,Table.ALIGN_RIGHT);
		bt.setColAlign(4,Table.ALIGN_RIGHT);
		rt.setTitle("��ú�Ĺ���", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4,DateUtil.Formatdate("yyyy��MM��", DateUtil.getDate(getEndDate())), Table.ALIGN_CENTER);
		rt.setDefaultTitle(19,4,"��λ:��ǧ��ʱ���׽�/ǧ�ˡ���",Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		rt.body.setColFormat(3,"0.00");
		rt.body.setColFormat(4,"0.00");
		rt.body.setColFormat(8,"0.00");
		//�����С���һ�о���
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);

		//	����ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.Formatdate("yyyy��MM��dd��",new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(9,3,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
			rt.setDefautlFooter(21,2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(21,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(21,2,"�Ʊ�:",Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	//�ϱ�ú��
	private String getShangbmh(){
		String strDiancxxbID= ((Visit) getPage().getVisit()).getString9();
		String strCondition ="";
		String strDateStart=getBeginDate();
		String strDateEnd=getEndDate();
		String strDateMonthStart=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(getEndDate())));
		int jib=this.getDiancTreeJib();
		String guolzj="";
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧

		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strCondition =strCondition+ " and (dc.fgsid="+strDiancxxbID+" or dc.rlgsid="+strDiancxxbID + ")";
			guolzj=" and grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
			guolzj=" and grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
		}

		JDBCcon con = new JDBCcon();

//		String diancCondition=
//			"and y.diancxxb_id in (select id\n" + 
//			" from(\n" + 
//			" select id from diancxxb\n" + 
//			" start with id="+strDiancxxbID+"\n" + 
//			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
//			" )\n" + 
//			" union\n" + 
//			" select id\n" + 
//			" from diancxxb\n" + 
//			" where id="+strDiancxxbID+")" ; 

		//�����ͷ����
		Report rt = new Report();
		StringBuffer sbsql = new StringBuffer();

		//��������

//		if(jib==1){//��Ϊ����ʱ,���շֹ�˾����
//			sbsql.append("select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp;&nbsp;'||dc.mingc) as danw, \n");
//			sbsql.append("fx.fenx,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl,sum(fadhml) as fadhml, \n");
//			sbsql.append("sum(gongrhml) as gongrhml,decode(sum(fadhml+gongrhml),0,0, \n");
//			sbsql.append("round((sum(fadhml*rulmrz)+sum(gongrhml*rulmrz))/sum(fadhml+gongrhml),2)) as rulmrz, \n");
//			sbsql.append("sum(fadhy) as fadhy,sum(gongrhy) as gongrhy, \n");
//			sbsql.append("decode(sum(fadhy+gongrhy),0,0,round((sum(fadhy*rulyrz)+sum(gongrhy*rulyrz))/sum(fadhy+gongrhy),2)) as rulyrz, \n");
//			sbsql.append("sum(fadhbzml) as fadbml,sum(gongrhbzml) as gongrbml,sum(FADHYZBZML) as fadhyzbml,sum(GONGRHYZBZML) as gongrhyzbml, \n");
//			sbsql.append("sum(fadhbzml+FADHYZBZML) as fadzhbml,sum(gongrhbzml+GONGRHYZBZML) as gongrzhbml, \n");
//			sbsql.append("decode(sum(fadl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(fadl)*10000),1)) as fadbzmh, \n");
//			sbsql.append("decode(sum(gongdl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(gongdl)*10000),1))as gongdbzmh, \n");
//			sbsql.append("decode(sum(gongrl),0,0,round(sum(gongrhbzml+GONGRHYZBZML)*1000/sum(gongrl),2)) as gongrbzmh \n");
//			sbsql.append("from vwyuezbb y ,vwdianc dc, \n");
//			sbsql.append("(select 1 as xuh,decode(1,1,'����') as fenx from dual \n");
//			sbsql.append("union \n");
//			sbsql.append("select 2 as xuh,decode(1,1,'�ۼ�') as fenx from dual) fx \n");
//			sbsql.append("where riq=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and y.diancxxb_id=dc.id \n");
//			sbsql.append("and gs.id=dc.fuid and fx.fenx=y.fenx ").append(strCondition);
//			sbsql.append("group by rollup(fx.fenx,gs.mingc,dc.mingc) \n");
//			sbsql.append("having not(grouping(fx.fenx)=1) \n");
//			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
//			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,fx.fenx \n");
//		}else if(jib==2){
//			JDBCcon cn = new JDBCcon();
//			String ranlgs = "select id from diancxxb where shangjgsid= "+strDiancxxbID;
//			String danw = "";
//			String groupby = "";
//			String having ="";
//			String orderby = "";
//			try{
//				ResultSet rl = cn.getResultSet(ranlgs);
//				if(rl.next()){
//					danw="decode(grouping(vdc.rlgsmc)+grouping(gs.mingc)+grouping(vdc.mingc),2,vdc.rlgsmc,1,gs.mingc,'&nbsp;&nbsp;'||vdc.mingc) as danw,\n";
//					groupby="group by rollup(fx.fenx,vdc.rlgsmc,gs.mingc,vdc.mingc) \n";
//					having="having not(grouping(vdc.rlgsmc)=1) \n";
//					orderby="order by grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(gs.mingc) desc,min(gs.xuh),gs.mingc,grouping(vdc.mingc) desc,min(vdc.xuh),vdc.mingc,fx.fenx \n";
//				}else{
//					danw="decode(grouping(gs.mingc)+grouping(vdc.mingc),1,gs.mingc,'&nbsp;&nbsp;'||vdc.mingc) as danw,\n";
//					groupby="group by rollup(fx.fenx,gs.mingc,vdc.mingc) \n";
//					having="having not(grouping(gs.mingc)=1) \n";
//					orderby="order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc,grouping(vdc.mingc) desc,min(fx.xuh),vdc.mingc,fx.fenx \n";
//				}
//				rl.close();
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				cn.Close();
//			}
//			sbsql.append("select "+danw+" \n");
//			sbsql.append("fx.fenx,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl,sum(fadhml) as fadhml, \n");
//			sbsql.append("sum(gongrhml) as gongrhml,decode(sum(fadhml+gongrhml),0,0, \n");
//			sbsql.append("round((sum(fadhml*rulmrz)+sum(gongrhml*rulmrz))/sum(fadhml+gongrhml),2)) as rulmrz, \n");
//			sbsql.append("sum(fadhy) as fadhy,sum(gongrhy) as gongrhy, \n");
//			sbsql.append("decode(sum(fadhy+gongrhy),0,0,round((sum(fadhy*rulyrz)+sum(gongrhy*rulyrz))/sum(fadhy+gongrhy),2)) as rulyrz, \n");
//			sbsql.append("sum(fadhbzml) as fadbml,sum(gongrhbzml) as gongrbml,sum(FADHYZBZML) as fadhyzbml,sum(GONGRHYZBZML) as gongrhyzbml, \n");
//			sbsql.append("sum(fadhbzml+FADHYZBZML) as fadzhbml,sum(gongrhbzml+GONGRHYZBZML) as gongrzhbml, \n");
//			sbsql.append("decode(sum(fadl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(fadl)*10000),1)) as fadbzmh, \n");
//			sbsql.append("decode(sum(gongdl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(gongdl)*10000),1))as gongdbzmh, \n");
//			sbsql.append("decode(sum(gongrl),0,0,round(sum(gongrhbzml+GONGRHYZBZML)*1000/sum(gongrl),2)) as gongrbzmh \n");
//			sbsql.append("from vwyuezbb y ,diancxxb dc,vwfengs gs, vwdianc vdc,\n");
//			sbsql.append("(select 1 as xuh,decode(1,1,'����') as fenx from dual \n");
//			sbsql.append("union \n");
//			sbsql.append("select 2 as xuh,decode(1,1,'�ۼ�') as fenx from dual) fx \n");
//			sbsql.append("where riq=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and y.diancxxb_id=dc.id \n");
//			sbsql.append("and gs.id=dc.fuid and fx.fenx=y.fenx and dc.id=vdc.id ").append(strCondition);
//			sbsql.append(groupby);
//			sbsql.append(having);
//			sbsql.append(orderby);
//		}else{//��Ϊ�ֹ�˾�͵糧ʱ,���յ糧���ͻ���
//			sbsql.append("select dc.mingc as danw, \n");
//			sbsql.append("fx.fenx,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl,sum(fadhml) as fadhml, \n");
//			sbsql.append("sum(gongrhml) as gongrhml,decode(sum(fadhml+gongrhml),0,0, \n");
//			sbsql.append("round((sum(fadhml*rulmrz)+sum(gongrhml*rulmrz))/sum(fadhml+gongrhml),2)) as rulmrz, \n");
//			sbsql.append("sum(fadhy) as fadhy,sum(gongrhy) as gongrhy, \n");
//			sbsql.append("decode(sum(fadhy+gongrhy),0,0,round((sum(fadhy*rulyrz)+sum(gongrhy*rulyrz))/sum(fadhy+gongrhy),2)) as rulyrz, \n");
//			sbsql.append("sum(fadhbzml) as fadbml,sum(gongrhbzml) as gongrbml,sum(FADHYZBZML) as fadhyzbml,sum(GONGRHYZBZML) as gongrhyzbml, \n");
//			sbsql.append("sum(fadhbzml+FADHYZBZML) as fadzhbml,sum(gongrhbzml+GONGRHYZBZML) as gongrzhbml, \n");
//			sbsql.append("decode(sum(fadl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(fadl)*10000),1)) as fadbzmh, \n");
//			sbsql.append("decode(sum(gongdl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(gongdl)*10000),1))as gongdbzmh, \n");
//			sbsql.append("decode(sum(gongrl),0,0,round(sum(gongrhbzml+GONGRHYZBZML)*1000/sum(gongrl),2)) as gongrbzmh \n");
//			sbsql.append("from vwyuezbb y ,diancxxb dc,vwfengs gs, \n");
//			sbsql.append("(select 1 as xuh,decode(1,1,'����') as fenx from dual \n");
//			sbsql.append("union \n");
//			sbsql.append("select 2 as xuh,decode(1,1,'�ۼ�') as fenx from dual) fx \n");
//			sbsql.append("where riq=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and y.diancxxb_id=dc.id \n");
//			sbsql.append("and gs.id=dc.fuid and fx.fenx=y.fenx ").append(strCondition);
//			sbsql.append("group by rollup(fx.fenx,gs.mingc,dc.mingc) \n");
//			sbsql.append("having not(grouping(dc.mingc)=1) \n");
//			sbsql.append("order by grouping(gs.mingc) desc,min(gs.xuh),gs.mingc, \n");
//			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh),dc.mingc,fx.fenx \n");
//		}
		
		sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp;'||dc.mingc) as danw, \n");
//		sbsql.append("       fx.fenx,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl, \n");
//		sbsql.append("       sum(fadhml) as fadhml, sum(gongrhml) as gongrhml, \n");
//		sbsql.append("       decode(sum(fadhml+gongrhml),0,0, round((sum(fadhml*rulmrz)+sum(gongrhml*rulmrz))/sum(fadhml+gongrhml),2)) as rulmrz,  \n");
//		sbsql.append("       round(decode(sum(fadhml+gongrhml),0,0, round((sum(fadhml*rulmrz)+sum(gongrhml*rulmrz))/sum(fadhml+gongrhml),2))*1000/4.1816) as rulmrzdk,  \n");
//		sbsql.append("       sum(fadhy) as fadhy,sum(gongrhy) as gongrhy,  \n");
//		sbsql.append("       decode(sum(fadhy+gongrhy),0,0,round((sum(fadhy*rulyrz)+sum(gongrhy*rulyrz))/sum(fadhy+gongrhy),2)) as rulyrz,  \n");
//		sbsql.append("       round(decode(sum(fadhy+gongrhy),0,0,round((sum(fadhy*rulyrz)+sum(gongrhy*rulyrz))/sum(fadhy+gongrhy),2))*1000/4.1816) as rulyrzdk,  \n");
//		sbsql.append("       sum(fadhbzml) as fadbml,sum(gongrhbzml) as gongrbml,sum(FADHYZBZML) as fadhyzbml,sum(GONGRHYZBZML) as gongrhyzbml,  \n");
//		sbsql.append("       sum(fadhbzml+FADHYZBZML) as fadzhbml,sum(gongrhbzml+GONGRHYZBZML) as gongrzhbml,  \n");
//		sbsql.append("       decode(sum(fadl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(fadl)*10000),0)) as fadbzmh,  \n");
//		sbsql.append("       decode(sum(gongdl),0,0,round(sum(fadhbzml+FADHYZBZML)*1000000/(sum(gongdl)*10000),0))as gongdbzmh,  \n");
//		sbsql.append("       decode(sum(gongrl),0,0,round(sum(gongrhbzml+GONGRHYZBZML)*1000/sum(gongrl),0)) as gongrbzmh  \n");
//		-----------------------------�޸ĵ�����ʽ��yuezbb�е��ֶ�--------------------------------
		sbsql.append("       fx.fenx,sum(fadl) as fadl,sum(y.gongdl+y.FADZHCGDL+y.gongrcgdl) as gongdl,sum(gongrl) as gongrl,  \n");
		sbsql.append("       sum(FADYTRML) as fadhml, sum(GONGRYTRML) as gongrhml,  \n");
		sbsql.append("       decode(sum(FADYTRML+GONGRYTRML),0,0, round((sum(FADYTRML*RULTRMPJFRL/1000)+sum(GONGRYTRML*RULTRMPJFRL/1000))/sum(FADYTRML+GONGRYTRML),2)) as rulmrz,   \n");
		sbsql.append("       round(decode(sum(FADYTRML+GONGRYTRML),0,0, round((sum(FADYTRML*RULTRMPJFRL/1000)+sum(GONGRYTRML*RULTRMPJFRL/1000))/sum(FADYTRML+GONGRYTRML),2))*1000/4.1816) as rulmrzdk,   \n");
		sbsql.append("       sum(FADYTRYL) as fadhy,sum(GONGRYTRYL) as gongrhy,   \n");
		sbsql.append("       decode(sum(FADYTRYL+GONGRYTRYL),0,0,round((sum(FADYTRYL*RULTRYPJFRL/1000)+sum(GONGRYTRYL*RULTRYPJFRL/1000))/sum(FADYTRYL+GONGRYTRYL),2)) as rulyrz,   \n");
		sbsql.append("       round(decode(sum(FADYTRYL+GONGRYTRYL),0,0,round((sum(FADYTRYL*RULTRYPJFRL/1000)+sum(GONGRYTRYL*RULTRYPJFRL/1000))/sum(FADYTRYL+GONGRYTRYL),2))*1000/4.1816) as rulyrzdk,   \n");
		sbsql.append("       sum(FADMZBML) as fadbml,sum(GONGRMZBML) as gongrbml,sum(FADYZBZML) as fadhyzbml,sum(GONGRYZBZML) as gongrhyzbml,   \n");
		sbsql.append("       sum(FADMZBML+FADYZBZML+fadqzbzml) as fadzhbml,sum(GONGRMZBML+GONGRYZBZML+gongrqzbzml) as gongrzhbml,   \n");
		sbsql.append("       decode(sum(fadl),0,0,round(sum(FADMZBML+FADYZBZML+fadqzbzml)*1000000/(sum(fadl)*10000),0)) as fadbzmh,   \n");
		sbsql.append("       decode(sum(y.fadl-y.gongdl),0,0,round(sum(FADMZBML+FADYZBZML+fadqzbzml)*1000000/(sum(y.fadl-y.gongdl)*10000),0))as gongdbzmh,   \n");
		sbsql.append("       decode(sum(gongrl),0,0,round(sum(GONGRMZBML+GONGRYZBZML+gongrqzbzml)*1000/sum(gongrl),0)) as gongrbzmh   \n");
//		-------------------------------------------------------------
		sbsql.append("from vwyuezbb y , vwdianc dc, \n");
		sbsql.append("     (select 1 as xuh,decode(1,1,'����') as fenx from dual  \n");
		sbsql.append("        union  \n");
		sbsql.append("      select 2 as xuh,decode(1,1,'�ۼ�') as fenx from dual) fx  \n");
		sbsql.append("where riq=to_date('"+strDateMonthStart+"','yyyy-mm-dd')  \n");
		sbsql.append("      and y.diancxxb_id(+)=dc.id  and y.fenx=fx.fenx(+)\n").append(strCondition);
		sbsql.append("group by rollup(fx.fenx,dc.fgsmc,dc.mingc)  \n");
		sbsql.append("having not(grouping(fx.fenx)=1) "+guolzj+" \n");
		sbsql.append("order by grouping(dc.fgsmc) desc,min(dc.fgsxh),dc.fgsmc, \n");
		sbsql.append("      grouping(dc.mingc) desc,min(dc.xuh) ,dc.mingc, \n");
		sbsql.append("      min(fx.xuh),fx.fenx  \n");
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String ArrHeader[][]=new String[2][22];
		ArrHeader[0]=new String[] {"��λ","��λ","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)","������<br>(����)","����Ȼú��(��)","����Ȼú��(��)","��ú��ֵ","��ú��ֵ","����Ȼ����(��)","����Ȼ����(��)","������ֵ","������ֵ","����ú�۱�ú��(��)","����ú�۱�ú��(��)","�������۱�ú��(��)","�������۱�ú��(��)","�ۺϱ�ú��(��)","�ۺϱ�ú��(��)","��׼ú��","��׼ú��","��׼ú��"};
		ArrHeader[1]=new String[] {"��λ","��λ","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)","������<br>(����)","����","����","(�׽�/ǧ��)","(ǧ��/ǧ��)","����","����","(�׽�/ǧ��)","(ǧ��/ǧ��)","����","����","����","����","����","����","����<br>(��/��)","����<br>(��/��)","����<br>(ǧ��/����)"};
		int ArrWidth[]=new int[] {250,45,50,50,50,50,50,50,50,50,45,45,45,45,45,45,45,45,45,45,45,45};

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		// ����
		Table bt=new Table(rs,2,0,2);

		rt.setBody(bt);
		//�ڶ��о���
		bt.setColAlign(2,Table.ALIGN_CENTER);
		rt.setTitle("��ú�±�", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 4,DateUtil.Formatdate("yyyy��MM��", DateUtil.getDate(getEndDate())), Table.ALIGN_CENTER);
		rt.setDefaultTitle(19,4,"��λ:��ǧ��ʱ���׽�/ǧ�ˡ���",Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColFormat(8,"0.00");
		rt.body.setColFormat(12,"0.00");
		rt.body.ShowZero = false;

		//�����С���һ�о���
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}

		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
	//	rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);

//		����ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.Formatdate("yyyy��MM��dd��",new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(9,3,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
			rt.setDefautlFooter(21,2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(21,2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(21,2,"�Ʊ�:",Table.ALIGN_RIGHT);

		_AllPages=1;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=1;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}


	private String getDuibmh(){
		Visit visit = (Visit) getPage().getVisit();
		String strDiancxxbID= ((Visit) getPage().getVisit()).getString9();
		String strCondition ="";
		String strDateStart=getBeginDate();
		String strDateEnd=getEndDate();
		String strDateMonthStart=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(getEndDate())));

		JDBCcon con = new JDBCcon();
//		String diancCondition=
//			"where d.id in (select id\n" + 
//			" from(select id from diancxxb start with id="+strDiancxxbID+" connect by fuid=prior id)\n" + 
//			" union select id  from diancxxb where id="+strDiancxxbID+")" ; 
		//�����ͷ����
		Report rt = new Report();
		StringBuffer sbsql = new StringBuffer();
		
		int jib=this.getDiancTreeJib();
		String guolzj = "";
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧

		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strCondition =strCondition+ " and (dc.fgsid="+strDiancxxbID+" or dc.rlgsid="+strDiancxxbID + ")";
			guolzj=" having grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
			guolzj=" having grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
		}
		

		StringBuffer sqlzhi_by = new StringBuffer();//��Ȩ������
		StringBuffer sqlzhi_lj = new StringBuffer();//��Ȩ���ۼ�
		
//		StringBuffer sql_str = new StringBuffer();
//
//		if(((Visit) getPage().getVisit()).getRuljiaql().equals("��¯����ú��")){
//			
////			sql_str.append("sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,\n");
////			sql_str.append("decode(sum(nvl(hy.meil,0)),0,0, sum(nvl(hy.rulrl*hy.meil,0))/sum(nvl(hy.meil,0))) as rulrl,\n");
//			
//			//����
//			sqlzhi_by.append("(select hy.diancxxb_id,fady,gongrhy,rz.meil,rz.rulrl from \n");
//			sqlzhi_by.append("(select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
//			sqlzhi_by.append("from meihyb hy where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_by.append(" group by hy.diancxxb_id) hy,\n");
//			sqlzhi_by.append("(select rz.diancxxb_id,sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)) as meil, \n");
//			sqlzhi_by.append("decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0, \n");
//			sqlzhi_by.append(" sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil))) as rulrl\n");
//			sqlzhi_by.append("from rulmzlb rz where rz.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_by.append("group by rz.diancxxb_id,rz.rulrq) rz \n");
//			sqlzhi_by.append("where hy.diancxxb_id=rz.diancxxb_id(+)) hy, \n");
//			//�ۼ�
//			sqlzhi_lj.append("(select hy.diancxxb_id,fady,gongrhy,rz.meil,rz.rulrl from \n");
//			sqlzhi_lj.append("(select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
//			sqlzhi_lj.append("from meihyb hy where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_lj.append(" group by hy.diancxxb_id) hy,\n");
//			sqlzhi_lj.append("(select rz.diancxxb_id,sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)) as meil, \n");
//			sqlzhi_lj.append("decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0, \n");
//			sqlzhi_lj.append(" sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil))) as rulrl\n");
//			sqlzhi_lj.append("from rulmzlb rz where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_lj.append("group by rz.diancxxb_id) rz \n");
//			sqlzhi_lj.append("where hy.diancxxb_id=rz.diancxxb_id(+)) hy, \n");
//		}else{
////			sql_str.append("sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry,\n");
////			sql_str.append("decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,  \n");
//			
//			sqlzhi_by.append("  (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
//			sqlzhi_by.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
//			sqlzhi_by.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
//			sqlzhi_by.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
//			sqlzhi_by.append("        from meihyb hy,rulmzlb zl  \n");
//			sqlzhi_by.append("        where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')    \n");
//			sqlzhi_by.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
//			sqlzhi_by.append("              and hy.rulmzlb_id=zl.id(+)  \n");
//			sqlzhi_by.append("       group by hy.diancxxb_id) hy,  \n");
//			
//			sqlzhi_lj.append(" (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
//			sqlzhi_lj.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
//			sqlzhi_lj.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
//			sqlzhi_lj.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
//			sqlzhi_lj.append("        from meihyb hy,rulmzlb zl  \n");
//			sqlzhi_lj.append("        where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
//			sqlzhi_lj.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
//			sqlzhi_lj.append("              and hy.rulmzlb_id=zl.id(+)  \n");
//			sqlzhi_lj.append("       group by hy.diancxxb_id) hy,  \n");
//		}
//			
//		
//		sbsql.append("select  DANW,FENX,FADL_YB,GONGDL_YB,GONGRL_YB,HAOML_YB,RULMRZ_YB, round(RULMRZ_YB*1000/4.1816) as RULMRZDK_YB,  FADBZMH_YB,GONGDBZMH_YB,GONGRBZMH_YB, \n");
//		sbsql.append("        FADL_GS,GONGDL_GS,GONGRL_GS,HAOML_GS,RULMRZ_GS,round(RULMRZ_GS*1000/4.1816) as RULMRZDK_GS, FADBZMH_GS,GONGDBZMH_GS,GONGRBZMH_GS, \n");
//		sbsql.append("        HAOML_YB-HAOML_GS as meilc,RULMRZ_YB-RULMRZ_GS as rulrzc, round(RULMRZ_YB*1000/4.1816)-round(RULMRZ_GS*1000/4.1816) as rulrzcdk,\n");
//		sbsql.append("        FADBZMH_YB-FADBZMH_GS as fadbzmhc,GONGDBZMH_YB-GONGDBZMH_GS as gongdbzmhc,GONGRBZMH_YB-GONGRBZMH_gs as gongrbzmhc \n");
//		sbsql.append("from  \n");
//		sbsql.append("(select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp;'||dc.mingc) as danw,  \n");
//		sbsql.append("       gs.fenx,sum(yb.fadl) as fadl_yb,sum(yb.gongdl) as gongdl_yb,sum(yb.gongrl) as gongrl_yb,  \n");
//		sbsql.append("       sum(yb.fadhml+yb.gongrhml) as haoml_yb,  \n");
//		sbsql.append("       decode(sum(yb.fadhml+yb.gongrhml),0,0, round((sum(yb.fadhml*yb.rulmrz)+sum(yb.gongrhml*yb.rulmrz))/sum(yb.fadhml+yb.gongrhml),2)) as rulmrz_yb,    \n");
//		sbsql.append("       decode(sum(yb.fadl),0,0,round(sum(yb.fadhbzml+yb.FADHYZBZML)*1000000/(sum(yb.fadl)*10000)))as fadbzmh_yb,   \n");
//		sbsql.append("       decode(sum(yb.gongdl),0,0,round(sum(yb.fadhbzml+yb.FADHYZBZML)*1000000/(sum(yb.gongdl)*10000)))as gongdbzmh_yb, \n");
//		sbsql.append("       decode(sum(yb.gongrl),0,0,round(sum(yb.gongrhbzml+yb.GONGRHYZBZML)*1000/sum(yb.gongrl))) as gongrbzmh_yb, \n");
//		sbsql.append("       round(sum(gs.fadl)/10000,0) as fadl_gs,round(sum(gs.gongdl)/10000,0) as gongdl_gs,sum(gs.gongrl) as gongrl_gs, \n");
//		sbsql.append("       sum(gs.fady+gs.gongry) as haoml_gs, \n");
//		sbsql.append("       decode(sum(gs.fady+gs.gongry),0,0, round((sum(gs.fady*gs.rulrl)+sum(gs.gongry*gs.rulrl))/sum(gs.fady+gs.gongry),2)) as rulmrz_gs, \n");
//		sbsql.append("       decode(sum(gs.fadl),0,0,round(round(sum(gs.fady*gs.rulrl)/29.271+sum(gs.fadyy*gs.rulrly/29.271))*1000000/sum(gs.fadl),0)) as fadbzmh_gs, \n");
//		sbsql.append("       decode(sum(gs.gongdl),0,0,round(round(sum(gs.fady*gs.rulrl)/29.271+sum(gs.fadyy*gs.rulrly/29.271))*1000000/sum(gs.gongdl),0)) as gongdbzmh_gs, \n");
//		sbsql.append("       decode(sum(gs.gongrl),0,0,round(round(sum(gs.gongry*gs.rulrl)/29.271+sum(gs.gongryy*gs.rulrly/29.271))*1000/sum(gs.gongrl),0)) as gongrbzmh_gs \n");
//		sbsql.append("from (select  decode(1,1,'����','����') as fenx, dc.id,hy.fady as fady,hy.gongrhy as gongry,hy.rulrl,   \n");
//		sbsql.append("   sc.gongdl,sc.fadl,sc.gongrl,yb.fadyy,gongryy,yb.rulrl as rulrly   \n");
//		sbsql.append("  from    \n");
//		
////		sbsql.append("  (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
////		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,   \n");
////		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,   \n");
////		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl   \n");
////		sbsql.append("        from meihyb hy,rulmzlb zl   \n");
////		sbsql.append("        where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
////		sbsql.append("              and hy.rulmzlb_id=zl.id(+)   \n");
////		sbsql.append("       group by hy.diancxxb_id) hy,   \n");
//		
//		sbsql.append(sqlzhi_by.toString());
//		
//		sbsql.append("     (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl   \n");
//		sbsql.append("              from  shouhcrbyb  yrb   \n");
//		sbsql.append("              where yrb.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd') and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')     \n");
//		sbsql.append("       group by diancxxb_id) yb,    \n");
//		sbsql.append("      (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������   \n");
//		sbsql.append("              from riscsjb sc   \n");
//		sbsql.append("             where sc.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')   and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
//		sbsql.append("      group by  sc.diancxxb_id) sc, vwdianc dc  \n");
//		sbsql.append(" where dc.id=hy.diancxxb_id(+)  and dc.id=sc.diancxxb_id(+)  and dc.id=yb.diancxxb_id(+) \n").append(strCondition);
//		sbsql.append(" union all  \n");
//		sbsql.append(" select  decode(1,1,'�ۼ�','�ۼ�') as fenx, dc.id,hy.fady as fady,hy.gongrhy as gongry,hy.rulrl,   \n");
//		sbsql.append("   sc.gongdl,sc.fadl,sc.gongrl,yb.fadyy,gongryy,yb.rulrl as rulrly   \n");
//		sbsql.append("  from   \n");
//		
////		sbsql.append(" (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
////		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,   \n");
////		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,   \n");
////		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl   \n");
////		sbsql.append("        from meihyb hy,rulmzlb zl   \n");
////		sbsql.append("        where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')  and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
////		sbsql.append("              and hy.rulmzlb_id=zl.id(+)   \n");
////		sbsql.append("       group by hy.diancxxb_id) hy,   \n");
//		
//		sbsql.append(sqlzhi_lj.toString());
//		
//		sbsql.append("     (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl   \n");
//		sbsql.append("               from  shouhcrbyb  yrb   \n");
//		sbsql.append("                where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd') and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')     \n");
//		sbsql.append("       group by diancxxb_id) yb,    \n");
//		sbsql.append("      (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������   \n");
//		sbsql.append("              from riscsjb sc   \n");
//		sbsql.append("             where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd') and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
//		sbsql.append("      group by  sc.diancxxb_id) sc,vwdianc dc \n");
//		sbsql.append(" where dc.id=hy.diancxxb_id(+)   \n");
//		sbsql.append("       and dc.id=sc.diancxxb_id(+)   \n");
//		sbsql.append("       and dc.id=yb.diancxxb_id(+) \n").append(strCondition);
//		sbsql.append("       ) gs, \n");
//		sbsql.append("       (select * from  vwyuezbb where riq=to_date('"+strDateMonthStart+"','yyyy-mm-dd')) yb,vwdianc dc \n");
//		sbsql.append("where yb.diancxxb_id(+)=gs.id  \n");
//		sbsql.append("       and yb.fenx(+)=gs.fenx \n");
//		sbsql.append("       and gs.id=dc.id \n");
//		sbsql.append("group by rollup(gs.fenx,dc.fgsmc,dc.mingc)   \n");
//		sbsql.append("having not(grouping(gs.fenx)=1)   \n");
//		sbsql.append("order by grouping(dc.fgsmc) desc,min(dc.fgsxh),dc.fgsmc,  \n");
//		sbsql.append("      grouping(dc.mingc) desc,min(dc.xuh) ,dc.mingc,gs.fenx ) \n");
//		
		//������ĺϼ���ɺ����±��ϼ���ɺ�������ӷ������±���������ݲ�һ��
		int intRezDec=((Visit) getPage().getVisit()).getFarldec();
		sbsql.setLength(0);
		if(((Visit) getPage().getVisit()).getRuljiaql().equals("��¯����ú��")){
			sbsql.append("select  gs.mingc,gs.FENX,FADL_YB,GONGDL_YB,GONGRL_YB,HAOML_YB,RULMRZ_YB, round(RULMRZ_YB*1000/4.1816) as RULMRZDK_YB,  FADBZMH_YB,GONGDBZMH_YB,GONGRBZMH_YB,  \n");
			sbsql.append("        FADL,GONGDL,GONGRL,(FAdy+gongry),RULRL,round(RULRL*1000/4.1816) as RULMRZDK, \n");
			sbsql.append("        FADBZMH,GONGDBZMH,GONGRBZMH,  \n");
			sbsql.append("        HAOML_YB-(FAdy+gongry) as meilc,RULMRZ_YB-RULRL as rulrzc, round(RULMRZ_YB*1000/4.1816)-round(RULRL*1000/4.1816) as rulrzcdk, \n");
			sbsql.append("        FADBZMH_YB-FADBZMH as fadbzmhc,GONGDBZMH_YB-GONGDBZMH as gongdbzmhc,GONGRBZMH_YB-GONGRBZMH as gongrbzmhc  \n");
			sbsql.append("from (select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,   \n");
//			sbsql.append("       yb.fenx,sum(yb.fadl) as fadl_yb,sum(yb.gongdl) as gongdl_yb,sum(yb.gongrl) as gongrl_yb,   \n");
//			sbsql.append("       sum(yb.fadhml+yb.gongrhml) as haoml_yb,   \n");
//			sbsql.append("       decode(sum(yb.fadhml+yb.gongrhml),0,0, round((sum(yb.fadhml*yb.rulmrz)+sum(yb.gongrhml*yb.rulmrz))/sum(yb.fadhml+yb.gongrhml),2)) as rulmrz_yb,     \n");
//			sbsql.append("       decode(sum(yb.fadl),0,0,round(sum(yb.fadhbzml+yb.FADHYZBZML)*1000000/(sum(yb.fadl)*10000)))as fadbzmh_yb,    \n");
//			sbsql.append("       decode(sum(yb.gongdl),0,0,round(sum(yb.fadhbzml+yb.FADHYZBZML)*1000000/(sum(yb.gongdl)*10000)))as gongdbzmh_yb, \n");
//			sbsql.append("       decode(sum(yb.gongrl),0,0,round(sum(yb.gongrhbzml+yb.GONGRHYZBZML)*1000/sum(yb.gongrl))) as gongrbzmh_yb \n");
//			-----------------�޸ĵ�����ʽ��yuezbb�е��ֶ�--------------------------------------
			sbsql.append("       yb.fenx,sum(yb.fadl) as fadl_yb,sum(yb.fadl-yb.gongdl) as gongdl_yb,sum(yb.gongrl) as gongrl_yb,    \n");
			sbsql.append("       sum(yb.FADYTRML+yb.GONGRYTRML) as haoml_yb,    \n");
			sbsql.append("       decode(sum(yb.FADYTRML+yb.GONGRYTRML),0,0, round(sum((yb.FADYTRML+yb.GONGRYTRML)*yb.RULTRMPJFRL/1000)/sum(yb.FADYTRML+yb.GONGRYTRML),2)) as rulmrz_yb,      \n");
			sbsql.append("       decode(sum(yb.fadl),0,0,round(sum(yb.FADMZBML+yb.FADYZBZML+yb.fadqzbzml)*1000000/(sum(yb.fadl)*10000)))as fadbzmh_yb,     \n");
			sbsql.append("       decode(sum(yb.fadl-yb.gongdl),0,0,round(sum(yb.FADMZBML+yb.FADYZBZML+yb.fadqzbzml)*1000000/(sum(yb.fadl-yb.gongdl)*10000)))as gongdbzmh_yb,  \n");
			sbsql.append("       decode(sum(yb.gongrl),0,0,round(sum(yb.GONGRMZBML+yb.GONGRYZBZML+yb.gongrqzbzml)*1000/sum(yb.gongrl))) as gongrbzmh_yb  \n");
//			----------------------------------------------------------------------------------
			sbsql.append(" from (select * from  vwyuezbb where riq=to_date('"+strDateMonthStart+"','yyyy-mm-dd')) yb, \n");
			sbsql.append("      vwdianc dc  \n");
			sbsql.append("      where dc.id=yb.diancxxb_id(+) \n").append(strCondition);
			sbsql.append(" group by rollup(yb.fenx,dc.fgsmc,dc.mingc) \n "+guolzj+") yb,  \n");
			sbsql.append(" (select mingc,fenx,round(fardl/10000,2) as fadl,round(gongdl/10000,2) as gongdl,gongrl,fady,gongry,rulrl,round(rulrl*1000/4.1816,0) as rulrldk,round(fadyy,0) as fadyy,round(gongryy) as gongryy,rulrly,round(rulrly*1000/4.1816,0) as rulrlydk,    \n");
			sbsql.append("       fadzbm,gongrzbm,fadyzbm,gongryzbm,fadzhzbm,gongrzhzbm,   \n");
			sbsql.append("       fadbzmh,gongdbzmh,gongrbzmh   \n");
			sbsql.append("from (select mingc,rownum as xuh, decode(1,1,'����','����') as fenx,fardl,gongdl,gongrl,fady,gongry,round_new(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,   \n");
			sbsql.append("                  round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0)+round_new(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,   \n");
			sbsql.append("                  round_new(decode(fardl,0,0, 1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,     \n");
			sbsql.append("                  round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,     \n");
			sbsql.append("                  round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh    \n");
			sbsql.append("    from  (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,    \n");
			sbsql.append("                 sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,  \n");
			sbsql.append("                 decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl,  \n");
			sbsql.append("                 sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,    \n");
			sbsql.append("                 sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,    \n");
			sbsql.append("                 decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly    \n");
			sbsql.append("          from  (select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy   \n");
			sbsql.append("                     from meihyb hy   \n");
			sbsql.append("                     where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')   \n");
			sbsql.append("                     and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
			sbsql.append("                     group by hy.diancxxb_id) hy,  \n");
			sbsql.append("                 (select rz.diancxxb_id,sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)) as meil,  \n");
			sbsql.append("                         decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0,   \n");
			sbsql.append("                         round_new(sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),2)) as rulrl  \n");
			sbsql.append("                       from rulmzlb rz   \n");
			sbsql.append("                       where rz.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')  \n");
			sbsql.append("                       and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
			sbsql.append("                       group by rz.diancxxb_id) rz,  \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl    \n");
			sbsql.append("                       from  shouhcrbyb  yrb    \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')      \n");
			sbsql.append("                       group by diancxxb_id) yb,     \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������    \n");
			sbsql.append("                       from riscsjb sc    \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc   \n");
			sbsql.append("          where dc.id=hy.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=sc.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=rz.diancxxb_id(+)  \n");
			sbsql.append("                and dc.id=yb.diancxxb_id(+)   \n").append(strCondition);;
			sbsql.append("      group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbsql.append("		"+guolzj+" \n");
			sbsql.append("          order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc )   \n");
			sbsql.append("    union all      \n");
			sbsql.append("    select mingc,rownum as xuh, decode(1,1,'�ۼ�','�ۼ�') as fenx,fardl,gongdl,gongrl,fady,gongry,round(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,   \n");
			sbsql.append("                  round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0)+round_new(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,   \n");
			sbsql.append("                  round_new(decode(fardl,0,0, 1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,     \n");
			sbsql.append("                  round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,     \n");
			sbsql.append("                  round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh    \n");
			sbsql.append("    from  (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,    \n");
			sbsql.append("                 sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,  \n");
			sbsql.append("                 decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl,  \n");
			sbsql.append("                 sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,    \n");
			sbsql.append("                 sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,    \n");
			sbsql.append("                 decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly    \n");
			sbsql.append("          from  (select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy   \n");
			sbsql.append("                     from meihyb hy   \n");
			sbsql.append("                     where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')  \n");
			sbsql.append("                     and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
			sbsql.append("                     group by hy.diancxxb_id) hy,  \n");
			sbsql.append("                 (select rz.diancxxb_id,sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)) as meil,  \n");
			sbsql.append("                         decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),0,0,   \n");
			sbsql.append("                         round_new(sum((rz.meil)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)),2)) as rulrl  \n");
			sbsql.append("                       from rulmzlb rz   \n");
			sbsql.append("                       where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')  \n");
			sbsql.append("                       and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
			sbsql.append("                       group by rz.diancxxb_id) rz,  \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl    \n");
			sbsql.append("                       from  shouhcrbyb  yrb    \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')       \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')     \n");
			sbsql.append("                       group by diancxxb_id) yb,     \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������    \n");
			sbsql.append("                       from riscsjb sc    \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc   \n");
			sbsql.append("          where dc.id=hy.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=sc.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=rz.diancxxb_id(+)  \n");
			sbsql.append("                and dc.id=yb.diancxxb_id(+)   \n").append(strCondition);
			sbsql.append(" 			group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbsql.append("		    "+guolzj+" \n");
			sbsql.append("          order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ) )  \n");
			sbsql.append("order by xuh,fenx ) gs \n");
			sbsql.append("where gs.mingc=yb.danw(+) \n");
			sbsql.append("and gs.fenx=yb.fenx(+) \n");
		}else{
			sbsql.append("select  gs.mingc,gs.FENX,FADL_YB,GONGDL_YB,GONGRL_YB,HAOML_YB,RULMRZ_YB, round(RULMRZ_YB*1000/4.1816) as RULMRZDK_YB,  FADBZMH_YB,GONGDBZMH_YB,GONGRBZMH_YB,  \n");
			sbsql.append("        FADL,GONGDL,GONGRL,(FAdy+gongry),RULRL,round(RULRL*1000/4.1816) as RULMRZDK, \n");
			sbsql.append("        FADBZMH,GONGDBZMH,GONGRBZMH,  \n");
			sbsql.append("        HAOML_YB-(FAdy+gongry) as meilc,RULMRZ_YB-RULRL as rulrzc, round(RULMRZ_YB*1000/4.1816)-round(RULRL*1000/4.1816) as rulrzcdk, \n");
			sbsql.append("        FADBZMH_YB-FADBZMH as fadbzmhc,GONGDBZMH_YB-GONGDBZMH as gongdbzmhc,GONGRBZMH_YB-GONGRBZMH as gongrbzmhc  \n");
			sbsql.append("from (select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,   \n");
//			sbsql.append("       yb.fenx,sum(yb.fadl) as fadl_yb,sum(yb.gongdl) as gongdl_yb,sum(yb.gongrl) as gongrl_yb,   \n");
//			sbsql.append("       sum(yb.fadhml+yb.gongrhml) as haoml_yb,   \n");
//			sbsql.append("       decode(sum(yb.fadhml+yb.gongrhml),0,0, round((sum(yb.fadhml*yb.rulmrz)+sum(yb.gongrhml*yb.rulmrz))/sum(yb.fadhml+yb.gongrhml),2)) as rulmrz_yb,     \n");
//			sbsql.append("       decode(sum(yb.fadl),0,0,round(sum(yb.fadhbzml+yb.FADHYZBZML)*1000000/(sum(yb.fadl)*10000)))as fadbzmh_yb,    \n");
//			sbsql.append("       decode(sum(yb.gongdl),0,0,round(sum(yb.fadhbzml+yb.FADHYZBZML)*1000000/(sum(yb.gongdl)*10000)))as gongdbzmh_yb, \n");
//			sbsql.append("       decode(sum(yb.gongrl),0,0,round(sum(yb.gongrhbzml+yb.GONGRHYZBZML)*1000/sum(yb.gongrl))) as gongrbzmh_yb \n");
//			-----------------�޸ĵ�����ʽ��yuezbb�е��ֶ�--------------------------------------
			sbsql.append("       yb.fenx,sum(yb.fadl) as fadl_yb,sum(yb.fadl-yb.gongdl) as gongdl_yb,sum(yb.gongrl) as gongrl_yb,    \n");
	        sbsql.append("       sum(yb.FADYTRML+yb.GONGRYTRML) as haoml_yb,    \n");
	        sbsql.append("       decode(sum(yb.FADYTRML+yb.GONGRYTRML),0,0, round(sum((yb.FADYTRML+yb.GONGRYTRML)*yb.RULTRMPJFRL/1000)/sum(yb.FADYTRML+yb.GONGRYTRML),2)) as rulmrz_yb,      \n");
	        sbsql.append("       decode(sum(yb.fadl),0,0,round(sum(yb.FADMZBML+yb.FADYZBZML+yb.fadqzbzml)*1000000/(sum(yb.fadl)*10000)))as fadbzmh_yb,     \n");
	        sbsql.append("       decode(sum(yb.fadl-yb.gongdl),0,0,round(sum(yb.FADMZBML+yb.FADYZBZML+yb.fadqzbzml)*1000000/(sum(yb.fadl-yb.gongdl)*10000)))as gongdbzmh_yb,  \n");
	        sbsql.append("       decode(sum(yb.gongrl),0,0,round(sum(yb.GONGRMZBML+yb.GONGRYZBZML+yb.gongrqzbzml)*1000/sum(yb.gongrl))) as gongrbzmh_yb  \n");
//			----------------------------------------------------------------------------------
			sbsql.append(" from (select * from  vwyuezbb where riq=to_date('"+strDateMonthStart+"','yyyy-mm-dd')) yb, \n");
			sbsql.append("      vwdianc dc  \n");
			sbsql.append("      where dc.id=yb.diancxxb_id(+) \n").append(strCondition);
			sbsql.append(" group by rollup(yb.fenx,dc.fgsmc,dc.mingc) \n"+guolzj+" ) yb,  \n");
			sbsql.append(" (select mingc,fenx,round(fardl/10000,2) as fadl,round(gongdl/10000,2) as gongdl,gongrl,fady,gongry,rulrl,round(rulrl*1000/4.1816,0) as rulrldk,round(fadyy,0) as fadyy,round(gongryy) as gongryy,rulrly,round(rulrly*1000/4.1816,0) as rulrlydk,    \n");
			sbsql.append("       fadzbm,gongrzbm,fadyzbm,gongryzbm,fadzhzbm,gongrzhzbm,   \n");
			sbsql.append("       fadbzmh,gongdbzmh,gongrbzmh   \n");
			sbsql.append("from (select mingc,rownum as xuh, decode(1,1,'����','����') as fenx,fardl,gongdl,gongrl,fady,gongry,round_new(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,   \n");
			sbsql.append("                  round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0)+round_new(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,   \n");
			sbsql.append("                  round_new(decode(fardl,0,0, 1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,     \n");
			sbsql.append("                  round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,     \n");
			sbsql.append("                  round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh    \n");
			sbsql.append(" from  (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,    \n");
			sbsql.append("                 sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,  \n");
			sbsql.append("                 decode(sum(nvl(hy.meil,0)),0,0, sum(nvl(hy.rulrl*hy.meil,0))/sum(nvl(hy.meil,0))) as rulrl,  \n");
			sbsql.append("                 sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,    \n");
			sbsql.append("                 sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,    \n");
			sbsql.append("                 decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly    \n");
			sbsql.append("          from  (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongrhy,sum(decode(nvl(rz.qnet_ar,0),0, 0,hy.gongrhy+hy.fadhy)) as meil,  \n");
			sbsql.append("                       decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,hy.gongrhy+hy.fadhy)),0,0,   \n");
			sbsql.append("                       round_new(sum((hy.gongrhy+hy.fadhy)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,hy.gongrhy+hy.fadhy)),2)) as rulrl  \n");
			sbsql.append("                       from meihyb hy ,rulmzlb rz   \n");
			sbsql.append("                       where hy.rulrq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')  \n");
			sbsql.append("                       and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
			sbsql.append("                       and hy.rulmzlb_id=rz.id(+) \n");
			sbsql.append("                       group by hy.diancxxb_id) hy,  \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl    \n");
			sbsql.append("                       from  shouhcrbyb  yrb    \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')      \n");
			sbsql.append("                       group by diancxxb_id) yb,     \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������    \n");
			sbsql.append("                       from riscsjb sc    \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateMonthStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc   \n");
			sbsql.append("          where dc.id=hy.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=sc.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=yb.diancxxb_id(+)   \n").append(strCondition);;
			sbsql.append("      group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbsql.append("		"+guolzj+" \n");
			sbsql.append("          order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc )   \n");
			sbsql.append("    union all      \n");
			sbsql.append("    select mingc,rownum as xuh, decode(1,1,'�ۼ�','�ۼ�') as fenx,fardl,gongdl,gongrl,fady,gongry,round(rulrl,"+intRezDec+") as rulrl,fadyy,gongryy,rulrly,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0) as fadzbm,  round_new(rulrl*gongry/29.271,0) as gongrzbm,   \n");
			sbsql.append("                  round_new(rulrly*fadyy/29.271,0) as fadyzbm,round_new(rulrly*gongryy/29.271,0) as gongryzbm,   \n");
			sbsql.append("                  round_new(rulrl*fady/29.271,0)+round_new(rulrly*fadyy/29.271,0) as fadzhzbm,round_new(rulrl*gongry/29.271,0)+round_new(rulrly*gongryy/29.271,0)  as gongrzhzbm,   \n");
			sbsql.append("                  round_new(decode(fardl,0,0, 1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,     \n");
			sbsql.append("                  round_new(decode(gongdl,0,0,1000000*(round_new(fady*rulrl/29.271,0)+round_new(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,     \n");
			sbsql.append("                  round_new(decode(gongrl,0,0,1000*(round_new(gongry*rulrl/29.271,0)+round_new(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh    \n");
			sbsql.append(" from  (select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,    \n");
			sbsql.append("                 sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,  \n");
			sbsql.append("                 decode(sum(nvl(hy.meil,0)),0,0, sum(nvl(hy.rulrl*hy.meil,0))/sum(nvl(hy.meil,0))) as rulrl,  \n");
			sbsql.append("                 sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,    \n");
			sbsql.append("                 sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,    \n");
			sbsql.append("                 decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly    \n");
			sbsql.append("          from  (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongrhy,sum(decode(nvl(rz.qnet_ar,0),0, 0,hy.gongrhy+hy.fadhy)) as meil,  \n");
			sbsql.append("                       decode(sum(decode(nvl(rz.qnet_ar,0),0, 0,hy.gongrhy+hy.fadhy)),0,0,   \n");
			sbsql.append("                       round_new(sum((hy.gongrhy+hy.fadhy)*rz.qnet_ar)/sum(decode(nvl(rz.qnet_ar,0),0, 0,hy.gongrhy+hy.fadhy)),2)) as rulrl  \n");
			sbsql.append("                       from meihyb hy ,rulmzlb rz   \n");
			sbsql.append("                       where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')  \n");
			sbsql.append("                       and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
			sbsql.append("                       and hy.rulmzlb_id=rz.id(+) \n");
			sbsql.append("                       group by hy.diancxxb_id) hy,  \n");
			sbsql.append("                 (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl    \n");
			sbsql.append("                       from  shouhcrbyb  yrb    \n");
			sbsql.append("                       where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')       \n");
			sbsql.append("                       and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')     \n");
			sbsql.append("                       group by diancxxb_id) yb,     \n");
			sbsql.append("                 (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������    \n");
			sbsql.append("                       from riscsjb sc    \n");
			sbsql.append("                       where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')      \n");
			sbsql.append("                       and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sbsql.append("                       group by  sc.diancxxb_id) sc, vwdianc dc   \n");
			sbsql.append("          where dc.id=hy.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=sc.diancxxb_id(+)    \n");
			sbsql.append("                and dc.id=yb.diancxxb_id(+)   \n").append(strCondition);
			sbsql.append(" 			group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbsql.append("		"+guolzj+" \n");
			sbsql.append("          order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ) )  \n");
			sbsql.append("order by xuh,fenx ) gs \n");
			sbsql.append("where gs.mingc=yb.danw(+) \n");
			sbsql.append("and gs.fenx=yb.fenx(+) \n");
			
		}
		
//		System.out.println(sbsql.toString());
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String ArrHeader[][]=new String[3][26];
		ArrHeader[0]=new String[] {"��λ","��λ","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","�糧�����ϱ�����","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","ϵͳ��������","��ֵ","��ֵ","��ֵ","��ֵ","��ֵ","��ֵ"};
		ArrHeader[1]=new String[] {"��λ","��λ","������","������","������","����ȼú��","��ֵ","��ֵ","ú��","ú��","ú��","������","������","������","����ȼú��","��ֵ","��ֵ","ú��","ú��","ú��","����ȼú��","��ֵ","��ֵ","ú��","ú��","ú��"};
		ArrHeader[2]=new String[] {"��λ","��λ","������","������","������","����ȼú��","��ֵ","��ֵ","����","����","����","������","������","������","����ȼú��","��ֵ","��ֵ","����","����","����","����ȼú��","�׽�","��ֵ","����","����","����"};

		int ArrWidth[]=new int[] {180,45,80,80,50,60,40,40,30,30,30,80,80,50,70,40,40,30,30,30,50,40,40,40,40,40};

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		// ����
		Table bt=new Table(rs,3,0,2);
		rt.setBody(bt);
		//�ڶ��о���
		bt.setColAlign(2,Table.ALIGN_CENTER);
		rt.setTitle("��ú�ĶԱȱ�", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(10,4,DateUtil.Formatdate("yyyy��MM��", DateUtil.getDate(getEndDate())), Table.ALIGN_CENTER);
		rt.setDefaultTitle(22,5,"��λ:��ǧ��ʱ���׽�/ǧ�ˡ���",Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		
		rt.body.setColFormat(3,"0.00");
		rt.body.setColFormat(4,"0.00");
		rt.body.setColFormat(7,"0.00");
		rt.body.setColFormat(12,"0.00");
		rt.body.setColFormat(13,"0.00");
		rt.body.setColFormat(16,"0.00");
		
		//�����С���һ�о���
		if(rt.body.getRows()>3){
			rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
		}
		//ҳ�� 
		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);

//		����ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+DateUtil.Formatdate("yyyy��MM��dd��",new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3,"���:",Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
				rt.setDefautlFooter(25, 3, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
				rt.setDefautlFooter(25, 3, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(25,2,"�Ʊ�:",Table.ALIGN_RIGHT);

		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		//begin��������г�ʼ������
		visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
		//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			
		blnIsBegin = true;

		visit.setString8("-1");
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString8((cycle.getRequestContext().getParameters("lx")[0]));
		}

		visit.setString9("-1");
		if(cycle.getRequestContext().getParameters("dc") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("dc")[0]));
		}


		visit.setString10("");
		if(cycle.getRequestContext().getParameters("datStart") !=null) {
			visit.setString10((cycle.getRequestContext().getParameters("datStart")[0]));
		}

		visit.setString11("");
		if(cycle.getRequestContext().getParameters("datEnd") !=null) {
			visit.setString11((cycle.getRequestContext().getParameters("datEnd")[0]));
		}

		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
		}
		blnIsBegin = true;
	}

//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = ((Visit) getPage().getVisit()).getString9();
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
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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

//	�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}
}
