package com.zhiren.jt.zdt.rulgl.gusmh;

/* 
* ʱ�䣺2009-08-06
* ���ߣ� ll
* �޸����ݣ�
		((Visit) getPage().getVisit()).getRuljiaql().equals("��¯����ú��"))����Ϊ����  rz.diancxxb_id �ֶ���SQL�в�����bug��
* 		   
*/
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
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
 * ���ߣ�������
 * ʱ�䣺2010-01-30 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/**
 * chenzt
 * 2010-04-06
 * �������޸ĺӱ��ֹ�˾������Ʊ���һ�� ����������Ϊû�У�null��
 */

public class GusmhReport extends BasePage {
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
		return getRimhReport();
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
	
	// ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getRimhReport() {
		JDBCcon con = new JDBCcon();
		
		String strDiancxxbID= ((Visit) getPage().getVisit()).getString9();
		String strCondition ="";
		String strDateStart=getBeginDate();
		String strDateEnd=getEndDate();
		
		int jib=this.getDiancTreeJib();
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
		
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strCondition =strCondition+ " and (dc.fgsid="+strDiancxxbID+" or dc.rlgsid="+strDiancxxbID + ")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+strDiancxxbID;
		}
		

		StringBuffer sqlzhi_by = new StringBuffer();//��Ȩ������
		StringBuffer sqlzhi_lj = new StringBuffer();//��Ȩ���ۼ�
		StringBuffer sql_str = new StringBuffer();
		String rzdc_id="";
		
		
		if(((Visit) getPage().getVisit()).getRuljiaql().equals("��¯����ú��")){
			
			rzdc_id="and dc.id=rz.diancxxb_id(+)";//������¯��������糧��Ĺ���
			
			sql_str.append("sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,\n");
			sql_str.append("decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl,\n");
			
			//����
//			sqlzhi_by.append("(select hy.diancxxb_id,hy.rulrq,fady,gongrhy,rz.meil,rz.rulrl from \n");
//			sqlzhi_by.append("(select hy.diancxxb_id,hy.rulrq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
//			sqlzhi_by.append("from meihyb hy where hy.rulrq=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_by.append(" group by hy.diancxxb_id,hy.rulrq) hy,\n");
//			sqlzhi_by.append("(select rz.diancxxb_id,rz.rulrq,sum(rz.meil) as meil,\n");
//			sqlzhi_by.append("decode(sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil)),0,0, \n");
//			sqlzhi_by.append(" round_new(sum((rz.meil)*round_new(rz.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil)),"+((Visit) getPage().getVisit()).getFarldec()+")) as rulrl\n");
//			sqlzhi_by.append("from rulmzlb rz where rz.rulrq=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_by.append("group by rz.diancxxb_id,rz.rulrq) rz \n");
//			sqlzhi_by.append("where hy.diancxxb_id=rz.diancxxb_id(+) and hy.rulrq=rz.rulrq(+)  ) hy, \n");
			
			sqlzhi_by.append("(select hy.diancxxb_id,hy.rulrq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
			sqlzhi_by.append("  from meihyb hy \n");
			sqlzhi_by.append(" where hy.rulrq=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
			sqlzhi_by.append("  group by hy.diancxxb_id,hy.rulrq) hy, \n");
			sqlzhi_by.append(" (select rz.diancxxb_id,rz.rulrq,sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)) as meil,\n");
			sqlzhi_by.append(" decode(sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)),0,0,sum((rz.meil)*round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil))) as rulrl \n");
			sqlzhi_by.append(" from rulmzlb rz \n");
			sqlzhi_by.append(" where rz.rulrq=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
			sqlzhi_by.append(" group by rz.diancxxb_id,rz.rulrq) rz, \n");
			
			
			//�ۼ�
//			sqlzhi_lj.append("(select hy.diancxxb_id,fady,gongrhy,rz.meil,rz.rulrl from \n");
//			sqlzhi_lj.append("(select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
//			sqlzhi_lj.append("from meihyb hy where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_lj.append(" group by hy.diancxxb_id) hy,\n");
//			sqlzhi_lj.append("(select rz.diancxxb_id,sum(rz.meil) as meil,\n");
//			sqlzhi_lj.append("decode(sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil)),0,0, \n");
//			sqlzhi_lj.append(" round_new(sum((rz.meil)*round_new(rz.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil)),"+((Visit) getPage().getVisit()).getFarldec()+")) as rulrl\n");
//			sqlzhi_lj.append("from rulmzlb rz where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
//			sqlzhi_lj.append("group by rz.diancxxb_id) rz \n");
//			sqlzhi_lj.append("where hy.diancxxb_id=rz.diancxxb_id(+)) hy, \n");
			
			sqlzhi_lj.append("(select hy.diancxxb_id,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
			sqlzhi_lj.append("  from meihyb hy \n");
			sqlzhi_lj.append(" where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
			sqlzhi_lj.append(" and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
			sqlzhi_lj.append("  group by hy.diancxxb_id) hy, \n");
			sqlzhi_lj.append(" (select rz.diancxxb_id,sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)) as meil,\n");
			sqlzhi_lj.append(" decode(sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)),0,0,sum((rz.meil)*round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil))) as rulrl \n");
			sqlzhi_lj.append(" from rulmzlb rz \n");
			sqlzhi_lj.append(" where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
			sqlzhi_lj.append(" and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
			sqlzhi_lj.append(" group by rz.diancxxb_id) rz, \n");
			
		}else{
			sql_str.append("sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry,\n");
			sql_str.append("decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,  \n");
			
			sqlzhi_by.append("   (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
			sqlzhi_by.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
			sqlzhi_by.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
			sqlzhi_by.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
			sqlzhi_by.append("        from meihyb hy,rulmzlb zl  \n");
			sqlzhi_by.append("        where hy.rulrq=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
			sqlzhi_by.append("              and hy.rulmzlb_id=zl.id(+)  \n");
			sqlzhi_by.append("       group by hy.diancxxb_id) hy,  \n");
			
			sqlzhi_lj.append(" (select hy.diancxxb_id,sum(fadhy) as fady,sum(gongrhy) as gongry ,--������¯���� \n");
			sqlzhi_lj.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
			sqlzhi_lj.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
			sqlzhi_lj.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
			sqlzhi_lj.append("        from meihyb hy,rulmzlb zl  \n");
			sqlzhi_lj.append("        where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
			sqlzhi_lj.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
			sqlzhi_lj.append("              and hy.rulmzlb_id=zl.id(+)  \n");
			sqlzhi_lj.append("       group by hy.diancxxb_id) hy,  \n");
		}
			
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select mingc,fenx,fardl,gongdl,gongrl,fady,gongry,rulrl,round(rulrl*1000/4.1816,0) as rulrldk,fadyy,gongryy,rulrly,round(rulrly*1000/4.1816,0) as rulrlydk, \n");
		sbsql.append(" fadzbm,gongrzbm,fadyzbm,gongryzbm,fadzhzbm,gongrzhzbm, \n");
		sbsql.append(" fadbzmh,gongdbzmh,gongrbzmh from (\n");
		sbsql.append("select mingc,rownum as xuh, decode(1,1,'����','����') as fenx,round(fardl/10000,2) as fardl,round(gongdl/10000,2) as gongdl,gongrl,fady,gongry,round(rulrl,2) as rulrl,fadyy,gongryy,rulrly, \n");
		sbsql.append("  round(rulrl*fady/29.271) as fadzbm,  round(rulrl*gongry/29.271) as gongrzbm, \n");
		sbsql.append("  round(rulrly*fadyy/29.271) as fadyzbm,round(rulrly*gongryy/29.271) as gongryzbm, \n");
		sbsql.append("  round(rulrl*fady/29.271)+round(rulrly*fadyy/29.271) as fadzhzbm,round(rulrl*gongry/29.271)+round(rulrly*gongryy/29.271)  as gongrzhzbm, \n");
		sbsql.append("  round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,   \n");
		sbsql.append("  round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,   \n");
		sbsql.append("  round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh  \n");
		sbsql.append("from  \n");
		sbsql.append("(select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,  \n");
		
//		sbsql.append(" sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry, \n");
//		sbsql.append("     decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,  \n");
		
		sbsql.append(sql_str.toString());
		
		sbsql.append("     sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,  \n");
		sbsql.append("     round(sum(nvl(yb.fadyy,0)),0) as fadyy,round(sum(nvl(yb.gongryy,0)),0) as gongryy,  \n");
		sbsql.append("     decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly  \n");
		sbsql.append("  from   \n");
		
		sbsql.append(sqlzhi_by.toString());
		
		sbsql.append("     (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl  \n");
		sbsql.append("               from  shouhcrbyb  yrb  \n");
		sbsql.append("                where yrb.riq=to_date('"+strDateEnd+"','yyyy-mm-dd')     \n");
		sbsql.append("       group by diancxxb_id) yb,   \n");
		sbsql.append("      (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������  \n");
		sbsql.append("              from riscsjb sc  \n");
		sbsql.append("             where sc.riq=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
		sbsql.append("      group by  sc.diancxxb_id) sc, vwdianc dc \n");
		sbsql.append(" where dc.id=hy.diancxxb_id(+) "+rzdc_id+" \n");
		sbsql.append("    and dc.id=sc.diancxxb_id(+)  \n").append(strCondition);
		sbsql.append("    and dc.id=yb.diancxxb_id(+) group by rollup(dc.fgsmc,dc.mingc) \n");
		sbsql.append("    order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ) \n");
		
		sbsql.append("union all \n");
		
		sbsql.append("select mingc,rownum as xuh, decode(1,1,'�ۼ�','�ۼ�') as fenx,round(fardl/10000,2) as fardl,round(gongdl/10000,2) as gongdl,gongrl,fady,gongry,round(rulrl,2) as rulrl,fadyy,gongryy,rulrly, \n");
		sbsql.append("  round(rulrl*fady/29.271) as fadzbm,  round(rulrl*gongry/29.271) as gongrzbm, \n");
		sbsql.append("  round(rulrly*fadyy/29.271) as fadyzbm,round(rulrly*gongryy/29.271) as gongryzbm, \n");
		sbsql.append("  round(rulrl*fady/29.271)+round(rulrly*fadyy/29.271) as fadzhzbm,round(rulrl*gongry/29.271)+round(rulrly*gongryy/29.271)  as gongrzhzbm, \n");
		sbsql.append("  round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,   \n");
		sbsql.append("  round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,   \n");
		sbsql.append("  round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh  \n");
		sbsql.append("from  \n");
		sbsql.append("(select decode(grouping(fgsmc)+ grouping(mingc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||mingc) as mingc,  \n");
		
//		sbsql.append(" sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongry,0)) as gongry,\n");
//		sbsql.append("     decode(sum(nvl(hy.rulzl,0)),0,0, sum(nvl(hy.rulrl*hy.rulzl,0))/sum(nvl(hy.rulzl,0))) as rulrl,  \n");
		
		sbsql.append(sql_str.toString());
		
		sbsql.append("     sum(nvl(sc.gongdl,0)) as gongdl,sum(nvl(sc.fadl,0)) as fardl,sum(nvl(sc.gongrl,0)) as gongrl,  \n");
		sbsql.append("     round(sum(nvl(yb.fadyy,0)),0) as fadyy,round(sum(nvl(yb.gongryy,0)),0) as gongryy,  \n");
		sbsql.append("     decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly  \n");
		sbsql.append("  from   \n");
		
		sbsql.append(sqlzhi_lj.toString());
		
		sbsql.append("     (select diancxxb_id,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl  \n");
		sbsql.append("               from  shouhcrbyb  yrb  \n");
		sbsql.append("                where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')     \n");
		sbsql.append("              and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
		sbsql.append("       group by diancxxb_id) yb,   \n");
		sbsql.append("      (select sc.diancxxb_id, sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������  \n");
		sbsql.append("              from riscsjb sc  \n");
		sbsql.append("             where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
		sbsql.append("             and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
		sbsql.append("      group by  sc.diancxxb_id) sc, vwdianc dc \n");
		sbsql.append(" where dc.id=hy.diancxxb_id(+) "+rzdc_id+"  \n");
		sbsql.append("    and dc.id=sc.diancxxb_id(+)  \n").append(strCondition);
		sbsql.append("    and dc.id=yb.diancxxb_id(+) group by rollup(dc.fgsmc,dc.mingc) \n");
		sbsql.append("    order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ) \n");
		sbsql.append("order by xuh,fenx) \n");
		
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String ArrHeader[][]=new String[2][22];
		ArrHeader[0]=new String[] {"��λ","��λ","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)","������<br>(����)","����Ȼú��(��)","����Ȼú��(��)","��ú��ֵ","��ú��ֵ","����Ȼ����(��)","����Ȼ����(��)","������ֵ<br>","������ֵ<br>","����ú�۱�ú��(��)","����ú�۱�ú��(��)","�������۱�ú��(��)","�������۱�ú��(��)","�ۺϱ�ú��(��)","�ۺϱ�ú��(��)","��׼ú��","��׼ú��","��׼ú��"};
		ArrHeader[1]=new String[] {"��λ","��λ","������<br>(��ǧ��ʱ)","������<br>(��ǧ��ʱ)","������<br>(����)","����","����","(�׽�/ǧ��)","(ǧ��/ǧ��)","����","����","(�׽�/ǧ��)","(ǧ��/ǧ��)","����","����","����","����","����","����","����<br>(��/��)","����<br>(��/��)","����<br>(ǧ��/����)"};
		int ArrWidth[]=new int[] {200,45,50,50,50,50,50,50,50,50,45,45,45,45,45,45,45,45,45,45,45,45};
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		// ����
		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		//�ڶ��о���
		bt.setColAlign(2,Table.ALIGN_CENTER);
		rt.setTitle("��ú�Ĺ���", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 4,DateUtil.Formatdate("yyyy��MM��dd��", DateUtil.getDate(getBeginDate()))+"-"+DateUtil.Formatdate("yyyy��MM��dd��", DateUtil.getDate(getEndDate())), Table.ALIGN_CENTER);
		rt.setDefaultTitle(19,4,"��λ:��ǧ��ʱ���׽�/ǧ�ˡ���",Table.ALIGN_RIGHT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColFormat(3,"0.00");
		rt.body.setColFormat(4, "0.00");
		rt.body.ShowZero = false;
		
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
		
		con.Close();
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
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

		blnIsBegin = true;
		visit.setString9("-1");
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
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
		
		//begin��������г�ʼ������
		visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
		//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			
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
