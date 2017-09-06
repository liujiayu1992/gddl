package com.zhiren.jt.zdt.chengbgl.rucmcbtj;
/* 
* ʱ�䣺2009-08-29
* ���ߣ� ll
* �޸����ݣ�1���޸�ҳ����ʾsql�У��ۺϼ۹�ʽ��
* 		   
*/ 
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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
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

public class RucmcbtjReport extends BasePage {
	public final static String LX_FC="fc";
	public final static String LX_FK="fk";
	public final static String LX_FCFK="fcfk";
	public final static String LX_FKFC="fkfc";
	public final static String LX_QP="qp";
	
	
	private String leix="fchs";
	private String Year="";
	private String Month="";
	private String Nian="";
	private String Yue=""; 
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='������ⵥλ����'";
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
		String sets = "";
		long lngYunsfsId=-1;
		long lngJihkjId= -1;
		
		String fchaving = "";
		String fkhaving = "";
		String datStart=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1());
		String datEnd=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2());
		
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		
	
		
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
			
//		strCondtion="and tj.riq=to_date('"+Year+"-"+Month+"-01','yyyy-mm-dd') \n" ;
		
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and tj.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and tj.jihkjb_id=" +lngJihkjId;
		}
		
//		if (!strGongys_id.equals("-1")){
//			strCondtion=strCondtion+" and gy.fuid=" +strGongys_id;
//		}
//		
//		if (jib==2){
//			strCondtion=strCondtion+" and (dc.fuid=" +strDiancxxb_id+" or dc.shangjgsid="+strDiancxxb_id+")";
//			sets = "";
//			fchaving = "";
//			fkhaving = "";
//		}else if (jib==3){
//			strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
//			sets = "";
//			fchaving = " having not grouping(dc.mingc)=1 ";
//			fkhaving = "";
//		}else if (jib==-1){
//			strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
//		
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
		Visit visit = (Visit) getPage().getVisit();
		String lngzhuangt="";
		String zhuangtsql="";
		String zhuangtsql2="";
		
		if  (((Visit) getPage().getVisit()).getDropDownBean7()!=null){
			lngzhuangt= ((Visit) getPage().getVisit()).getDropDownBean7().getValue();
		}
		String zhuangt="";
		String shulzt="";
		String zhilzt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
				shulzt="";
				zhilzt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (dj.zhuangt=1 or dj.zhuangt=2)";
				shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
				zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and dj.zhuangt=2";
				shulzt=" and sl.zhuangt=2";
				zhilzt=" and zl.zhuangt=2";
			}
		String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();
		String guolzj = "";
		int jib=getDiancTreeJib(strDiancxxb_id);
		if (jib==2){			
			guolzj=" and  grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
		}else if (jib==3){
			guolzj=" and grouping(dc.mingc)=0\n";
		}
		String leijrq="";
		if(Yue.equals("1")){
			leijrq="and dj.fenx='�ۼ�' and tj.riq=to_date('"+Year+"-"+Month+"-01','yyyy-mm-dd') ";
		}else{
			leijrq="and dj.fenx='����' and tj.riq>=to_date('"+Nian+"-"+Yue+"-01','yyyy-mm-dd') and  tj.riq<=to_date('"+Year+"-"+Month+"-01','yyyy-mm-dd') ";
		}
		String ArrHeader[][]=null;
		int ArrWidth[]=null;
		if(lngzhuangt.equals("��˰")){
			zhuangtsql="decode(sum(sj.laimsl),0,0,round(sum(sj.zonghj*sj.laimsl)/sum(laimsl),2)) as �ۺϼ�,\n" +
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(laimsl),2)) as ú��,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.meijs*sj.laimsl)/sum(laimsl),2)) as ��ֵ˰,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.jiaohqzf*sj.laimsl)/sum(laimsl),2)) as ����ǰ���ӷ�,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.tielyf*sj.laimsl)/sum(laimsl),2)) as ��·�˷�,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.tielyfse*sj.laimsl)/sum(laimsl),2)) as ��·�˷�˰��,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.daoztlzf*sj.laimsl)/sum(laimsl),2)) as ��վ��·�ӷ�,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.qiyf*sj.laimsl)/sum(laimsl),2)) as ���˷�,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.qiyse*sj.laimsl)/sum(laimsl),2)) as ����˰��,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.qiyzf*sj.laimsl)/sum(laimsl),2)) as �����ӷ�,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.haiyf*sj.laimsl)/sum(laimsl),2)) as ���˷�,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.haiyse*sj.laimsl)/sum(laimsl),2)) as ����˰��,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.gangzf*sj.laimsl)/sum(laimsl),2)) as ���ӷ�,\n" + 
				"     decode(sum(sj.laimsl),0,0,round(sum(sj.qit*sj.laimsl)/sum(laimsl),2)) as ����,\n" + 
				"     round(decode(sum(sj.laimsl),0,0,\n" + 
				"          round(sum(sj.zonghj*sj.laimsl)/sum(laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)),2) as ��˰��ú���� \n";
			zhuangtsql2="a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.pidyf+a.pidyzf+a.qit+a.zaf as zonghj,\n" +
            	"        a.meij,a.meijs,a.jiaohqzf,a.tielyf,a.tielyfse,a.daoztlzf,a.qiyf,a.qiyzf,a.qiyse,a.haiyf,a.haiyse,a.gangzf,a.qit";		
		}else if(lngzhuangt.equals("����˰")){
			zhuangtsql="decode(sum(sj.laimsl),0,0,round(sum(sj.zonghj*sj.laimsl)/sum(laimsl),2)) as �ۺϼ�,\n" +
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.meij*sj.laimsl)/sum(laimsl),2)) as ú��,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.jiaohqzf*sj.laimsl)/sum(laimsl),2)) as ����ǰ���ӷ�,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.tielyf*sj.laimsl)/sum(laimsl),2)) as ��·�˷�,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.daoztlzf*sj.laimsl)/sum(laimsl),2)) as ��վ��·�ӷ�,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.qiyf*sj.laimsl)/sum(laimsl),2)) as ���˷�,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.qiyzf*sj.laimsl)/sum(laimsl),2)) as �����ӷ�,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.haiyf*sj.laimsl)/sum(laimsl),2)) as ���˷�,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.gangzf*sj.laimsl)/sum(laimsl),2)) as ���ӷ�,\n" + 
				"             decode(sum(sj.laimsl),0,0,round(sum(sj.qit*sj.laimsl)/sum(laimsl),2)) as ����,\n" + 
				"             decode(sum(sj.laimsl),0,0,\n" + 
				"                    round(round(sum(sj.zonghj*sj.laimsl)/sum(laimsl),2)*29.271/round(sum(sj.farl*sj.laimsl)/sum(laimsl),2),2)) as ����˰��ú���� \n";

			zhuangtsql2="a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.pidyf+a.pidyse+a.pidyzf+a.qit-a.meijs-tielyfse-a.qiyse-a.haiyse-a.pidyse+a.zaf as zonghj,\n" +
				"              a.meij-a.meijs as meij,a.jiaohqzf,a.tielyf-a.tielyfse as tielyf,\n" + 
				"              a.daoztlzf,a.qiyf-a.qiyse as qiyf,a.qiyzf,a.haiyf-a.haiyse as haiyf,a.gangzf,a.qit";
		}
		
		StringBuffer sbsql =new StringBuffer();
		StringBuffer sbSqlBody= new StringBuffer();
		String strTitle="";
		sbSqlBody.append("   fx.fenx, \n");
		sbSqlBody.append("       sum(sj.laimsl) as �볧����,   \n");
		sbSqlBody.append("       decode(sum(sj.laimsl),0,0,round(sum(sj.farl*sj.laimsl)/sum(laimsl),2)) as ������,   \n");
		sbSqlBody.append(zhuangtsql);
		sbSqlBody.append("from   \n");
		sbSqlBody.append("(select distinct dc.diancxxb_id,dc.gongysb_id,fx.fenx,fx.xuh from  \n");
		sbSqlBody.append("             (select distinct dc.id as diancxxb_id ,y.id as gongysb_id \n");
		sbSqlBody.append("                     from yuercbmdj ycb,yuetjkjb kj,vwdianc dc,vwgongys y \n");
		sbSqlBody.append("                     where ycb.yuetjkjb_id=kj.id and kj.diancxxb_id=dc.id and kj.gongysb_id=y.id  \n").append(getCondtion());
		sbSqlBody.append("                     and (riq=to_date('"+Year+"-"+Month+"-01','yyyy-mm-dd') \n");
		sbSqlBody.append("                     or riq=last_year_today(to_date('"+Year+"-"+Month+"-01','yyyy-mm-dd'))) ) dc,vwfenxYue fx \n");
		sbSqlBody.append("         ) fx, \n");
		sbSqlBody.append("( select decode(1,1,'����','') as fenx,a.diancxxb_id,a.gongysb_id,a.laimsl,a.qnet_ar as farl,   \n");
		sbSqlBody.append(zhuangtsql2);
		sbSqlBody.append("   from    \n");
		sbSqlBody.append("(select tj.diancxxb_id as diancxxb_id,tj.gongysb_id as gongysb_id,sum(nvl(sl.laimsl,0)) as laimsl, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,sum(nvl(zl.qnet_ar,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0))) as qnet_ar, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.meij,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as meij, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.meijs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as meijs, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.jiaohqzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as jiaohqzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'��·',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as tielyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'��·',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as tielyfse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'��·',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as daoztlzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'��·',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as qiyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'��·',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as qiyse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'��·',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as qiyzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'ˮ·����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as haiyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'ˮ·����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as haiyse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'ˮ·����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as gangzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'Ƥ����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as  pidyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'Ƥ����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as pidyse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'Ƥ����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as pidyzf,   \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.zaf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as zaf, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.qit,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as qit  \n");
		sbSqlBody.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,jihkjb jh  ,vwdianc dc,vwgongys y \n");
		sbSqlBody.append("  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   \n");
		sbSqlBody.append("    and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='����' and tj.jihkjb_id=jh.id  "+zhuangt+shulzt+zhilzt+" \n").append(getCondtion());
		sbSqlBody.append("    and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id and tj.riq=to_date('"+Year+"-"+Month+"-01','yyyy-mm-dd')   \n");
		sbSqlBody.append("  group by (tj.diancxxb_id,tj.gongysb_id)  \n");
		sbSqlBody.append("    ) a  \n");
		sbSqlBody.append(" union  \n");
		sbSqlBody.append(" select decode(1,1,'�ۼ�','') as fenx,a.diancxxb_id,a.gongysb_id,a.laimsl,a.qnet_ar as farl,   \n");
		sbSqlBody.append(zhuangtsql2);
		sbSqlBody.append("   from    \n");
		sbSqlBody.append("(select tj.diancxxb_id as diancxxb_id,tj.gongysb_id as gongysb_id,sum(nvl(sl.laimsl,0)) as laimsl, \n");
//		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(zl.qnet_ar,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as qnet_ar, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,sum(nvl(zl.qnet_ar,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0))) as qnet_ar,\n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.meij,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as meij, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.meijs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as meijs, \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.jiaohqzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as jiaohqzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'"+Locale.yunsfs_tiel+"',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as tielyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'"+Locale.yunsfs_tiel+"',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as tielyfse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'"+Locale.yunsfs_tiel+"',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as daoztlzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'"+Locale.yunsfs_gongl+"',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as qiyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'"+Locale.yunsfs_gongl+"',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as qiyse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'"+Locale.yunsfs_gongl+"',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as qiyzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'ˮ·����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as haiyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'ˮ·����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as haiyse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'ˮ·����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as gangzf,   \n");
		sbSqlBody.append("        decode(max(ys.mingc),'Ƥ����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunj,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as  pidyf, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'Ƥ����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.yunjs,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as pidyse, \n");
		sbSqlBody.append("        decode(max(ys.mingc),'Ƥ����',decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.daozzf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)),0) as pidyzf,   \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.zaf,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as zaf,   \n");
		sbSqlBody.append("        decode(sum(nvl(sl.laimsl,0)),0,0,round(sum(nvl(dj.qit,0)*nvl(sl.laimsl,0))/sum(nvl(sl.laimsl,0)),2)) as qit  \n");
		sbSqlBody.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys,jihkjb jh  ,vwdianc dc,vwgongys y \n");
		sbSqlBody.append("  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id   \n");
		sbSqlBody.append("    and dj.fenx=sl.fenx and dj.fenx=zl.fenx  and tj.jihkjb_id=jh.id  "+zhuangt+shulzt+zhilzt+" \n").append(getCondtion());
		sbSqlBody.append("    and tj.diancxxb_id=dc.id and tj.gongysb_id=y.id  "+leijrq+"\n");
		sbSqlBody.append("  group by (tj.diancxxb_id,tj.gongysb_id )  \n");
		sbSqlBody.append("    ) a  \n");
		sbSqlBody.append("  ) sj,vwdianc dc,vwgongys y     \n");
		sbSqlBody.append("  where   fx.diancxxb_id=dc.id(+)  and fx.gongysb_id=y.id(+)     \n");
		sbSqlBody.append("          and fx.diancxxb_id=sj.diancxxb_id(+)  and fx.gongysb_id=sj.gongysb_id(+) \n");
		sbSqlBody.append("  		and fx.fenx=sj.fenx(+) \n");
		sbSqlBody.append("          and sj.diancxxb_id=dc.id and sj.gongysb_id=y.id \n");
		if (leix.equals(this.LX_FC)){
			sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc, \n");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(fx.fenx,dc.fgsmc,dc.mingc)   \n");
			sbsql.append(" having not grouping(fx.fenx)=1 "+guolzj+" \n");
			sbsql.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbsql.append("          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx");
			strTitle="�볧ú�ɱ�(�ֳ�)";
			
			if(lngzhuangt.equals("��˰")){
				ArrHeader =new String[1][19];
				ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
				ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55,55};
			}else if(lngzhuangt.equals("����˰")){
				 ArrHeader =new String[1][15];
				 ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
				 ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55};
			}
		}else if(leix.equals(this.LX_FK)){
			sbsql.append("select decode(grouping(smc)+grouping(dqmc),2,'�ܼ�',1,smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(fx.fenx,smc,dqmc)   \n");
			sbsql.append(" having not grouping(fx.fenx)=1");
			sbsql.append(" order by grouping(smc) desc,max(sxh) ,smc, \n");
			sbsql.append("          grouping(dqmc) desc,max(dqxh) ,dqmc, \n");
			sbsql.append("          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx");
			strTitle="�볧ú�ɱ�(�ֿ�)";
			
			if(lngzhuangt.equals("��˰")){
				ArrHeader =new String[1][19];
				ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
				ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55,55};
			}else if(lngzhuangt.equals("����˰")){
				 ArrHeader =new String[1][15];
				 ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
				 ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55};
			}
		}else if(leix.equals(this.LX_FCFK)){
			sbsql.append("select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'�ܼ�',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append("group by rollup(fx.fenx,fgsmc,dc.mingc,dqmc)   \n");
			sbsql.append(" having not grouping(fx.fenx)=1 "+guolzj+" \n");
			sbsql.append(" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbsql.append("          grouping(dqmc) desc,max(dqxh) ,dqmc, \n");
			sbsql.append("          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx");
			strTitle="�볧ú�ɱ�(�ֳ��ֿ�)";
			if(lngzhuangt.equals("��˰")){
				ArrHeader =new String[1][19];
				ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
				ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55,55};
			}else if(lngzhuangt.equals("����˰")){
				 ArrHeader =new String[1][15];
				 ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
				 ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55};
			}
		}else if(leix.equals(this.LX_FKFC)){
			if(jib==1){
				sbsql.append("select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'�ܼ�',2,dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc,");
				sbsql.append(sbSqlBody);
				sbsql.append(" group by rollup(fx.fenx,dqmc,fgsmc,dc.mingc)   \n");
				sbsql.append(" having not grouping(fx.fenx)=1");
				sbsql.append(" order by  grouping(dqmc) desc,max(dqxh) ,dqmc, \n");
				sbsql.append("          grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
				sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
				sbsql.append("          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx");
				
			}else{
				sbsql.append("select decode(grouping(dc.mingc)+grouping(dqmc),2,'�ܼ�',1,dqmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc,");
				sbsql.append(sbSqlBody);
				sbsql.append(" group by rollup(fx.fenx,dqmc,dc.mingc)   \n");
				sbsql.append(" having not grouping(fx.fenx)=1");
				sbsql.append(" order by  grouping(dqmc) desc,max(dqxh) ,dqmc, \n");
				sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
				sbsql.append("          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx");
				
			}
			strTitle="�볧ú�ɱ�(�ֿ�ֳ�)";
			if(lngzhuangt.equals("��˰")){
				ArrHeader =new String[1][19];
				ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
				ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55,55};
			}else if(lngzhuangt.equals("����˰")){
				 ArrHeader =new String[1][15];
				 ArrHeader[0]=new String[] {"��λ","����","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
				 ArrWidth =new int[] {150,50,65,55,55,55,55,55,55,55,55,55,55,55,55};
			}
		}else if(leix.equals(this.LX_QP)){
			strTitle="�볧ú�ɱ�(����)";
		}
//		else if(leix.indexOf(",")>0){
//			return getPrintDataTz();
//		}
		
			
		
//		StringBuffer sbSelect=new StringBuffer();
//		StringBuffer sbSelectTitle=new StringBuffer();
//		StringBuffer sbSelectBody=new StringBuffer();
//		StringBuffer sbGroup=new StringBuffer();
//		StringBuffer sbGroupBody=new StringBuffer();
//		
//		if (leix.equals(LX_FCHS)){//�ֳ�
//			sbSelect.append("select case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||gy.mingc else  \n");
//			sbSelect.append("       case when grouping(kj.mingc)=0 then '&nbsp;&nbsp;'||kj.mingc else  \n");
//			sbSelect.append("       case when grouping(dc.mingc)=0 then dc.mingc else  \n");
//			sbSelect.append("       case when grouping(gs.mingc)=0 then gs.mingc else '�ܼ�' end end  end end ��ú��λ,  \n");
//			sbSelect.append("       sum(sj.jingz) as �볧����,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meijs*sj.jingz)/sum(jingz),2)) as ��ֵ˰,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.tielyfse*sj.jingz)/sum(jingz),2)) as ��·�˷�˰��,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyse*sj.jingz)/sum(jingz),2)) as ����˰��,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.haiyse*sj.jingz)/sum(jingz),2)) as ����˰��,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
//			sbSelect.append(" 	 	      round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ��˰��ú����   \n");
//			sbSelectBody.append("from  yuetjkjb tj,diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj,  \n");
//			sbSelectBody.append("(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl,  \n");
//			sbSelectBody.append("        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit as zonghj,  \n");
//			sbSelectBody.append("        a.meij,a.meijs,a.jiaohqzf,a.tielyf,a.tielyfse,a.daoztlzf,a.qiyf,a.qiyzf,a.qiyse,a.haiyf,a.haiyse,a.gangzf,a.qit  \n");
//			sbSelectBody.append("   from   \n");
//			sbSelectBody.append("(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf,  \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf,  \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf,  \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf,  \n");
//			sbSelectBody.append("        dj.qit \n");
//			sbSelectBody.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys  \n");
//			sbSelectBody.append("  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id  \n");
//			sbSelectBody.append("    and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='����' ) a ) sj  \n");
//			sbSelectBody.append("  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id and dc.fuid=gs.id  \n");
//			sbGroup.append("  group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gs.mingc,sj.fenx),(gs.mingc,kj.mingc,sj.fenx),  \n");
//			sbGroup.append("        (dc.mingc,sj.fenx),(dc.mingc,kj.mingc,sj.fenx),(dc.mingc,kj.mingc,gy.mingc,sj.fenx))  \n");
//			sbGroup.append("order by \n");
//			sbGroup.append("    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj.mingc),1,4,3),0) desc,  \n");
//			sbGroup.append("    min(gs.xuh), gs.mingc,min(dc.xuh),grouping(kj.mingc) desc,min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh) \n");
// 
//			strTitle="(�ֳ���˰)";
//			
//			 ArrHeader =new String[1][18];
//			 ArrHeader[0]=new String[] {"��λ","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
//			 ArrWidth =new int[] {150,65,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55,55};
//			
//			
//		}else if(leix.equals(LX_FCBHS)){//�ֳ�����˰
//			sbSelect.append("select case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||gy.mingc else  \n");
//			sbSelect.append("       case when grouping(kj.mingc)=0 then '&nbsp;&nbsp;'||kj.mingc else  \n");
//			sbSelect.append("       case when grouping(dc.mingc)=0 then dc.mingc else  \n");
//			sbSelect.append("       case when grouping(gs.mingc)=0 then gs.mingc else '�ܼ�' end end  end end ��ú��λ,  \n");
//			sbSelect.append("       sum(sj.jingz) as �볧����,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����,  \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
//			sbSelect.append("              round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ����˰��ú���� \n");
//			sbSelectBody.append("from  yuetjkjb tj,diancxxb dc,diancxxb gs,gongysb gy,jihkjb kj,  \n");
//			sbSelectBody.append("(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl,  \n");
//			sbSelectBody.append("        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit-a.meijs-tielyfse-a.qiyse-a.haiyse as zonghj,  \n");
//			sbSelectBody.append("        a.meij-a.meijs as meij,a.jiaohqzf,a.tielyf-a.tielyfse as tielyf, \n");
//			sbSelectBody.append("        a.daoztlzf,a.qiyf-a.qiyse as qiyf,a.qiyzf,a.haiyf-a.haiyse as haiyf,a.gangzf,a.qit  \n");
//			sbSelectBody.append("   from   \n");
//			sbSelectBody.append("(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf,  \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf,  \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf,  \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf,  \n");
//			sbSelectBody.append("        dj.qit \n");
//			sbSelectBody.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys  \n");
//			sbSelectBody.append("  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id  \n");
//			sbSelectBody.append("    and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='����' ) a ) sj  \n");
//			sbSelectBody.append("  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id and dc.fuid=gs.id  \n");
//			sbGroup.append(" group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gs.mingc,sj.fenx),(gs.mingc,kj.mingc,sj.fenx),  \n");
//			sbGroup.append("        (dc.mingc,sj.fenx),(dc.mingc,kj.mingc,sj.fenx),(dc.mingc,kj.mingc,gy.mingc,sj.fenx))  \n");
//			sbGroup.append("order by \n");
//			sbGroup.append("    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(gy.mingc),3,decode(grouping(kj.mingc),1,4,3),0) desc,  \n");
//			sbGroup.append("    min(gs.xuh), gs.mingc,min(dc.xuh),grouping(kj.mingc) desc,min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh) \n");
//			
//			strTitle="(�ֳ�����˰)";
//			
//			 ArrHeader =new String[1][14];
//			 ArrHeader[0]=new String[] {"��λ","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
//			 ArrWidth =new int[] {150,65,55,55,55,55,55,55,55,55,55,55,55,55};
//	
//		}else if (leix.equals(LX_FKHS)){//�ֿ�
//			sbSelect.append("select case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc else \n");
//			sbSelect.append("       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;'||gy.mingc else \n");
//			sbSelect.append("       case when grouping(kj.mingc)=0 then kj.mingc else '�ܼ�' end end  end ��ú��λ, \n");
//			sbSelect.append("       sum(sj.jingz) as �볧����, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meijs*sj.jingz)/sum(jingz),2)) as ��ֵ˰, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.tielyfse*sj.jingz)/sum(jingz),2)) as ��·�˷�˰��, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyse*sj.jingz)/sum(jingz),2)) as ����˰��, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.haiyse*sj.jingz)/sum(jingz),2)) as ����˰��, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����, \n");
//			sbSelect.append("      decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
//			sbSelect.append("  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ��˰��ú����  \n");
//			sbSelectBody.append("from  yuetjkjb tj,diancxxb dc,gongysb gy,jihkjb kj, \n");
//			sbSelectBody.append("(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl, \n");
//			sbSelectBody.append("        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit as zonghj, \n");
//			sbSelectBody.append("        a.meij,a.meijs,a.jiaohqzf,a.tielyf,a.tielyfse,a.daoztlzf,a.qiyf,a.qiyse,a.qiyzf,a.haiyf,a.haiyse,a.gangzf,a.qit  \n");
//			sbSelectBody.append("   from \n");
//			sbSelectBody.append("(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf, \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf, \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf, \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf, \n");
//			sbSelectBody.append("        dj.qit \n");
//			sbSelectBody.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys \n");
//			sbSelectBody.append("  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id \n");
//			sbSelectBody.append("    and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='����') a \n");
//			sbSelectBody.append("    ) sj \n");
//			sbSelectBody.append("  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id \n");
//			sbGroup.append("  group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gy.mingc,sj.fenx),(gy.mingc,dc.mingc,sj.fenx)) \n");
//			sbGroup.append("order by \n");
//			sbGroup.append("    decode(grouping(kj.mingc)+grouping(gy.mingc)+grouping(dc.mingc),3,3,0) desc, \n");
//			sbGroup.append("    min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),grouping(dc.mingc) desc,min(dc.xuh) \n");
//			
//			strTitle="(�ֿ�˰)";
//			
//			ArrHeader =new String[1][18];
//			ArrHeader[0]=new String[] {"��λ","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","��ֵ˰<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��·<br>�˷�˰��<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","����˰��<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","��(ˮ)<br>��˰��<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","��˰<br>��ú����<br>(Ԫ/��)"};
//			ArrWidth =new int[] {150,65,55,55,55,55,55,55,55,55,55,55,55,55,55,50,55,55};
//	
//			
//			
//		}else if(leix.equals(LX_FKBHS)){//�ֿ󲻺�˰
//			sbSelect.append("select case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc else \n");
//			sbSelect.append("       case when grouping(gy.mingc)=0 then '&nbsp;&nbsp;'||gy.mingc else \n");
//			sbSelect.append("       case when grouping(kj.mingc)=0 then kj.mingc else '�ܼ�' end end  end ��ú��λ, \n");
//			sbSelect.append("       sum(sj.jingz) as �볧����, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.farl*sj.jingz)/sum(jingz),3)) as ������, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)) as �ۺϼ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.meij*sj.jingz)/sum(jingz),2)) as ú��, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.jiaohqzf*sj.jingz)/sum(jingz),2)) as ����ǰ���ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.tielyf*sj.jingz)/sum(jingz),2)) as ��·�˷�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.daoztlzf*sj.jingz)/sum(jingz),2)) as ��վ��·�ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n");
//			sbSelect.append("  decode(sum(sj.jingz),0,0,round(sum(sj.qiyzf*sj.jingz)/sum(jingz),2)) as �����ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.haiyf*sj.jingz)/sum(jingz),2)) as ���˷�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.gangzf*sj.jingz)/sum(jingz),2)) as ���ӷ�, \n");
//			sbSelect.append("       decode(sum(sj.jingz),0,0,round(sum(sj.qit*sj.jingz)/sum(jingz),2)) as ����, \n");
//			sbSelect.append("  decode(sum(sj.jingz),0,0,decode(round(sum(sj.farl*sj.jingz)/sum(jingz),3),0,0, \n");
//			sbSelect.append("  round(round(sum(sj.zonghj*sj.jingz)/sum(jingz),2)*29.271/round(sum(sj.farl*sj.jingz)/sum(jingz),3),2))) as ����˰��ú���� \n");
//			sbSelectBody.append("from  yuetjkjb tj,diancxxb dc,gongysb gy,jihkjb kj, \n");
//			sbSelectBody.append("(select a.yuetjkjb_id,a.fenx,a.jingz,a.qnet_ar as farl, \n");
//			sbSelectBody.append("        a.meij+a.jiaohqzf+a.tielyf+a.daoztlzf+a.qiyf+a.qiyzf+a.haiyf+a.gangzf+a.qit-a.meijs-tielyfse-a.qiyse-a.haiyse as zonghj,  \n");
//			sbSelectBody.append("        a.meij-a.meijs as meij,a.jiaohqzf,a.tielyf-a.tielyfse as tielyf, \n");
//			sbSelectBody.append("   a.daoztlzf,a.qiyf-a.qiyse as qiyf,a.qiyzf,a.haiyf-a.haiyse as haiyf,a.gangzf,a.qit  \n");
//			sbSelectBody.append("   from \n");
//			sbSelectBody.append("(select dj.yuetjkjb_id,dj.fenx,sl.jingz,zl.qnet_ar,dj.meij,dj.meijs,dj.jiaohqzf, \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunj,0) as tielyf,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.yunjs,0) as tielyfse,decode(ys.mingc,'"+Locale.yunsfs_tiel+"',dj.daozzf,0) as daoztlzf, \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunj,0) as qiyf,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.yunjs,0) as qiyse,decode(ys.mingc,'"+Locale.yunsfs_gongl+"',dj.daozzf,0) as qiyzf, \n");
//			sbSelectBody.append("        decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunj,0) as haiyf,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.yunjs,0) as haiyse,decode(ys.mingc,'"+Locale.yunsfs_shuiy+"',dj.daozzf,0) as gangzf, \n");
//			sbSelectBody.append("        dj.qit \n");
//			sbSelectBody.append("   from yuercbmdj dj,yueslb sl,yuezlb zl,yuetjkjb tj,yunsfsb ys \n");
//			sbSelectBody.append("  where dj.yuetjkjb_id=tj.id and sl.yuetjkjb_id=tj.id and zl.yuetjkjb_id=tj.id and tj.yunsfsb_id=ys.id \n");
//			sbSelectBody.append("    and dj.fenx=sl.fenx and dj.fenx=zl.fenx and dj.fenx='����' ) a \n");
//			sbSelectBody.append("    ) sj \n");
//			sbSelectBody.append("  where sj.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.gongysb_id=gy.id and tj.jihkjb_id=kj.id \n");
//			sbGroup.append("  group by grouping sets (sj.fenx,(kj.mingc,sj.fenx),(gy.mingc,sj.fenx),(gy.mingc,dc.mingc,sj.fenx)) \n");
//			sbGroup.append("order by \n");
//			sbGroup.append("    decode(grouping(kj.mingc)+grouping(gy.mingc)+grouping(dc.mingc),3,3,0) desc, \n");
//			sbGroup.append("    min(kj.xuh),grouping(gy.mingc) desc,min(gy.xuh),grouping(dc.mingc) desc,min(dc.xuh) \n");
//			strTitle="(�ֿ�)";
//			
//			ArrHeader =new String[1][14];
//			ArrHeader[0]=new String[] {"��λ","�볧ú��<br>(��)","�볧��ֵ<br>(MJ/kg)","�ۺϼ�<br>(Ԫ/��)","ú��<br>(Ԫ/��)","����<br>ǰ���ӷ�<br>(Ԫ/��)","��·�˷�<br>(Ԫ/��)","��վ<br>��·�ӷ�<br>(Ԫ/��)","�����˷�<br>(Ԫ/��)","�����ӷ�<br>(Ԫ/��)","��(ˮ)<br>�˷�<br>(Ԫ/��)","���ӷ�<br>(Ԫ/��)","��������<br>(Ԫ/��)","����˰<br>��ú����<br>(Ԫ/��)"};
//	
//			ArrWidth =new int[] {150,65,55,55,55,55,55,55,55,55,55,55,55,55};
//		}else if(leix.equals(LX_QP)){
//			strTitle="(��ú�������̱�)";
//		}
//		
//		sbGroupBody.append(sbSelect).append(sbSelectBody).append(getCondtion()).append(sbGroup);
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName="ú̿�볧�ɱ�ͳ��"+strTitle;
		
		
		// �����ͷ����
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(5,  3, "", Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

		// ����ҳ��
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
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		
		//begin��������г�ʼ������
		visit.setString4(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString4(pagewith);
			}
		//	visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
 
		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			visit.setString10((cycle.getRequestContext().getParameters("NianF1")[0]));
			visit.setString11((cycle.getRequestContext().getParameters("YueF1")[0]));
			visit.setString12((cycle.getRequestContext().getParameters("nianf")[0]));
			visit.setString13((cycle.getRequestContext().getParameters("yuef")[0]));
			leix = visit.getString9();
			Nian= visit.getString10();
			Yue= visit.getString11();
			Year= visit.getString12();
			Month= visit.getString13();
        }else{
        	if(!visit.getString1().equals("")) {
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
	// ��Ӧ��
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
				.setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
		return;
	}

	// ����
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
		list.add(new IDropDownBean(1, "�ֳ�"));
		list.add(new IDropDownBean(2, "�ֿ�"));
		list.add(new IDropDownBean(3, "���̱�"));
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

	// �糧����
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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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