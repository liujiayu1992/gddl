
//2009-8-10 sy
//�޸����� :�볧��¯��ֵȡgetFarldec�е�С��λ�ټ���

//2009-08-08 chh 
//�޸����ݣ�SQL����е��������е���ʵ�id158д���ˣ���Ϊ����
//2008-10-12 chh 
//�޸����� :�볧����¯û�л������ݵ��������μӼ�Ȩ

//2008-10-13 chh 
//�޸����� :���������

//2010-04-02 chenzt 
//�޸����� :�ӱ��ֹ�˾����ֵ���ӡ�ޱ���ߣ���ӡ��ȫ��

package com.zhiren.jt.zdt.rulgl.rucrlrzc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

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
/*
 * ���ߣ����
 * ʱ�䣺2012-10-16
 * ���������δʹ�õ�����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-24
 * �������볧��¯��ֵ���������ʾ�볧����������¯��������Ϣ������������Ӧ�ļ�Ȩ�����������Ӧ�ļ�������Ϣ���м�Ȩ��
 * 		�ڱ����м����볧����������¯��������
 */
public class Rucrlrzc extends BasePage {
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
		return getZhiltj();
	}
	private String getLaimlField(){
		JDBCcon con = new JDBCcon();
		String laiml = SysConstant.LaimField;
		ResultSetList rs = con.getResultSetList("select * from xitxxb where mingc = 'ʹ�ü���' and zhuangt = 1 and zhi = '�й�����'");
		if(rs.next()){
			laiml = "sum(round_new(fh.laimsl,"+((Visit) getPage().getVisit()).getShuldec()+"))";
		}
		rs.close();
		return laiml;
	}
	// ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getZhiltj() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String Start_riq=DateUtil.FormatDate(visit.getDate1());
		String End_riq=DateUtil.FormatDate(visit.getDate2());
		
//		String strConditonTitle="";
//		if (Start_riq==End_riq){
//			strConditonTitle=DateUtil.FormatDate(DateUtil.getDate(Start_riq));
//		}else{
//			strConditonTitle=DateUtil.FormatDate(DateUtil.getDate(Start_riq))+"��"+DateUtil.FormatDate(DateUtil.getDate(End_riq));
//		}
		
		//����
		String strGongsID = "";
		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";

		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			notHuiz=" and not grouping(gs.mingc)=1 ";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			notHuiz=" and not  grouping(dc.mingc)=1";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String danw="";
		String table="";
		String where="";
		String groupby = "";
		String ordeby ="";

		JDBCcon cn = new JDBCcon();
		if (jib==2){
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();

			try{
				ResultSet rl = cn.getResultSet(ranlgs);
				if(rl.next()){
					danw="select decode(grouping(vdc.rlgsmc)+grouping(vdc.fgsmc)+grouping(vdc.mingc),3,'�ܼ�',2,vdc.rlgsmc,1,vdc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||vdc.mingc) as danw,\n";
					table=",vwdianc vdc ";
					where="and dc.id=vdc.id";
					groupby ="group by rollup (fx.fenx,vdc.rlgsmc,vdc.fgsmc,vdc.mingc)\n";
					notHuiz =" and not grouping(vdc.rlgsmc)=1 ";
					ordeby="order by  grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(vdc.fgsmc) desc,vdc.fgsmc,grouping(vdc.mingc) desc ,vdc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
				}else{
					danw="select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n";
					table="";
					where="";
					groupby ="group by rollup (fx.fenx,gs.mingc,dc.mingc)\n";
					ordeby="order by  grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc ,dc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
				}
				rl.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
		}else{
			danw="select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n";
			table="";
			where="";
			groupby ="group by rollup (fx.fenx,gs.mingc,dc.mingc)\n";
			ordeby="order by  grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc ,dc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
		}
		
		StringBuffer sql_rc = new StringBuffer();//�볧
		sql_rc.append("(select diancxxb_id  as id,decode(1,1,'����') as fenx,          \n");
		sql_rc.append("        sum(sj.laiml) as laimsl,   \n");
		sql_rc.append("        sum(sj.laimzl) as jianzsl,   \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.farl*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as farl,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.mt*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),1)) as mt,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.vdaf*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as vdaf,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.ad*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as ad,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.std*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as std  \n");
		sql_rc.append(" from   \n");
		sql_rc.append(" (select fh.diancxxb_id,fh.gongysb_id,     \n");
		sql_rc.append("   "+getLaimlField()+" as laiml,     \n");
		sql_rc.append("   sum(round(fh.laimsl)) as laimsl,   \n");
		sql_rc.append("   round(decode(sum(nvl(z.qnet_ar,0)),0,0,    \n");
		sql_rc.append("       (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)))) as laimzl,   \n");
		sql_rc.append("   decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as farl,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.mt,1))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),1)) as mt,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.ad,2))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as ad,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.std,2))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as std,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.vdaf,2))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as vdaf \n");
		sql_rc.append(" from fahb fh,zhilb z,vwdianc dc,vwgongys y     \n");
		sql_rc.append(" where fh.zhilb_id=z.id(+) and fh.diancxxb_id=dc.id and fh.gongysb_id=y.id     \n");
		sql_rc.append("         and fh.daohrq=to_date('"+End_riq+"','yyyy-mm-dd')     \n");
		sql_rc.append(strGongsID+" \n");
		sql_rc.append(" group by fh.diancxxb_id,fh.gongysb_id,fh.lieid) sj  \n");
		sql_rc.append(" group by sj.diancxxb_id \n");
		sql_rc.append(" union \n");
		
		sql_rc.append("select diancxxb_id  as id,decode(2,2,'�ۼ�') as fenx,          \n");
		sql_rc.append("        sum(sj.laiml) as laimsl,   \n");
		sql_rc.append("        sum(sj.laimzl) as jianzsl,   \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.farl*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as farl,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.mt*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),1)) as mt,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.vdaf*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as vdaf,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.ad*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as ad,  \n");
		sql_rc.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.std*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),2)) as std  \n");
		sql_rc.append(" from   \n");
		sql_rc.append(" (select fh.diancxxb_id,fh.gongysb_id,     \n");
		sql_rc.append("   "+getLaimlField()+" as laiml,     \n");
		sql_rc.append("   sum(round(fh.laimsl)) as laimsl,   \n");
		sql_rc.append("   round(decode(sum(nvl(z.qnet_ar,0)),0,0,    \n");
		sql_rc.append("       (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)))) as laimzl,   \n");
		sql_rc.append("   decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as farl,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.mt,1))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),1)) as mt,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.ad,2))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as ad,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.std,2))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as std,  \n");
		sql_rc.append("    decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,   \n");
		sql_rc.append("          round_new(sum(round_new(fh.laimzl,0)*round_new(z.vdaf,2))/   \n");
		sql_rc.append("          sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as vdaf \n");
		sql_rc.append(" from fahb fh,zhilb z,vwdianc dc,vwgongys y     \n");
		sql_rc.append(" where fh.zhilb_id=z.id(+) and fh.diancxxb_id=dc.id and fh.gongysb_id=y.id   \n");
		sql_rc.append("         and fh.daohrq>=to_date('"+Start_riq+"','yyyy-mm-dd')    \n");
		sql_rc.append("         and fh.daohrq<=to_date('"+End_riq+"','yyyy-mm-dd')     \n");
		sql_rc.append(strGongsID+" \n");
		sql_rc.append(" group by fh.diancxxb_id,fh.gongysb_id,fh.lieid) sj  \n");
		sql_rc.append(" group by sj.diancxxb_id )rc, \n");
		
		
		StringBuffer sql_rl = new StringBuffer();//��¯
		sql_rl.append("(select dc.id, decode(1,1,'����') as fenx,nvl(sum(haoyl),0) as laimsl,sum(nvl(jianzl,0)) as jianzl,   \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,2)*jianzl)/sum(jianzl)),2),0) as farl, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(mt,1)*jianzl)/sum(jianzl)),1),0) as mt, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(ad,2)*jianzl)/sum(jianzl)),2),0) as ad, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(vdaf,2)*jianzl)/sum(jianzl)),2),0) as vdaf, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(std,2)*jianzl)/sum(jianzl)),2),0) as std \n");
		sql_rl.append(" from (select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where 1=1 "+strGongsID+") dc,   \n");
		sql_rl.append("(select dc.id as id,nvl(sum(fadhy+gongrhy),0) as haoyl    \n");
		sql_rl.append("from  meihyb m,vwdianc dc where m.rulrq=to_date('"+End_riq+"','yyyy-mm-dd')   \n");
		sql_rl.append(" and dc.id=m.diancxxb_id group by dc.id) haoy,   \n");
		sql_rl.append("(select dc.id as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,   \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,1)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),1) as mt,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as qnet_ar,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1    \n");
		sql_rl.append(" from rulmzlb r,vwdianc dc   \n");
		sql_rl.append(" where r.rulrq=to_date('"+End_riq+"','yyyy-mm-dd')    \n");
		sql_rl.append(" and dc.id(+)=r.diancxxb_id    \n");
		sql_rl.append(strGongsID+" \n");
		sql_rl.append(" group by dc.id ) sj    \n");
		sql_rl.append(" where sj.id(+)=dc.id and haoy.id(+)=dc.id   \n");
		sql_rl.append(" group by dc.id \n");
		sql_rl.append(" union \n");
		 
		sql_rl.append("select dc.id, decode(2,2,'�ۼ�') as fenx,nvl(sum(haoyl),0) as laimsl,sum(nvl(jianzl,0)) as jianzl,   \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,2)*jianzl)/sum(jianzl)),2),0) as farl, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(mt,1)*jianzl)/sum(jianzl)),1),0) as mt, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(ad,2)*jianzl)/sum(jianzl)),2),0) as ad, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(vdaf,2)*jianzl)/sum(jianzl)),2),0) as vdaf, \n");
		sql_rl.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(std,2)*jianzl)/sum(jianzl)),2),0) as std \n");
		sql_rl.append(" from (select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where 1=1 "+strGongsID+ ") dc,   \n");
		sql_rl.append("(select dc.id as id,nvl(sum(fadhy+gongrhy),0) as haoyl    \n");
		sql_rl.append("from  meihyb m,vwdianc dc where m.rulrq>=to_date('"+Start_riq+"','yyyy-mm-dd') and m.rulrq<=to_date('"+End_riq+"','yyyy-mm-dd')   \n");
		sql_rl.append(" and dc.id=m.diancxxb_id group by dc.id) haoy,   \n");
		sql_rl.append("(select dc.id as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,   \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,1)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),1) as mt,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as qnet_ar,    \n");
		sql_rl.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1    \n");
		sql_rl.append(" from rulmzlb r,vwdianc dc   \n");
		sql_rl.append(" where r.rulrq>=to_date('"+Start_riq+"','yyyy-mm-dd') and r.rulrq<=to_date('"+End_riq+"','yyyy-mm-dd')    \n");
		sql_rl.append(" and dc.id(+)=r.diancxxb_id    \n");
		sql_rl.append(strGongsID+" \n");
		sql_rl.append(" group by dc.id ) sj    \n");
		sql_rl.append(" where sj.id(+)=dc.id and haoy.id(+)=dc.id  group by dc.id ) rl, \n");
		
		
		String sbsql = "select l.danw,l.rc_laimsl,rc_laimzl,l.rc_farl,l.rc_farldk,l.rc_shuif,l.rc_ad,l.rc_std,l.rc_vdaf,\n"//l.fenx,
			+ "       l.rl_jingz,rl_rulmzl,l.rl_farl,rl_farldk,l.rl_shuif,l.rl_ad,l.rl_std,l.rl_vdaf,\n"
			+ "       (l.rc_farl-l.rl_farl) as tiaozq_mj,\n"
			+ "       Round((l.rc_farl-l.rl_farl)*1000/4.1816,0) as tiaozq_dk,\n"
			+ "       Round((l.rc_farl-l.rl_farl*(100-l.rc_shuif)/(100-l.rl_shuif)),2) as tiaozh_mj,\n"
			+ "       Round((Round((l.rc_farl-l.rl_farl*(100-l.rc_shuif)/(100-l.rl_shuif)),2)*1000/4.1816),0) as tiaozh_dk\n"
//			+ "       Round((l.rc_farl-((l.rl_farl+2.3)*(100-l.rc_shuif)/(100-l.rl_shuif)-2.3)),2) as tiaozh_mj,\n"
//			+ "       Round((Round((l.rc_farl-((l.rl_farl+2.3)*(100-l.rc_shuif)/(100-l.rl_shuif)-2.3)),2)*1000/4.1816),0) as tiaozh_dk\n"
			+ "from\n"
			+ "(\n"
			+ danw
			+ "       fx.fenx,\n"+
			"       Round(sum(rc.laimsl),0) as rc_laimsl,sum(rc.jianzsl) as rc_laimzl,\n" +
			"       decode(sum(rc.jianzsl),0,0,round_new(sum(rc.farl*rc.jianzsl)/sum(rc.jianzsl),2)) as rc_farl,\n" + 
			"       decode(sum(rc.jianzsl),0,0,round_new(round_new(sum(rc.farl*rc.jianzsl)/sum(rc.jianzsl),2)*1000/4.1816,0)) as rc_farldk,\n" + 
			"       decode(sum(rc.jianzsl),0,0,round_new(sum(rc.mt*rc.jianzsl)/sum(rc.jianzsl),2)) as rc_shuif,\n" + 
			"       decode(sum(rc.jianzsl),0,0,round_new(sum(rc.ad*rc.jianzsl)/sum(rc.jianzsl),2)) as rc_ad,\n" + 
			"       decode(sum(rc.jianzsl),0,0,round_new(sum(rc.std*rc.jianzsl)/sum(rc.jianzsl),2)) as rc_std,\n" + 
			"       decode(sum(rc.jianzsl),0,0,round_new(sum(rc.vdaf*rc.jianzsl)/sum(rc.jianzsl),2)) as rc_vdaf,\n" + 
			"\n" + 
			"       Round(sum(rl.laimsl),0) as rl_jingz,sum(rl.jianzl) as rl_rulmzl,\n" + 
			"       decode(sum(rl.jianzl),0,0,round_new(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),2)) as rl_farl,\n" + 
			"       decode(sum(rl.jianzl),0,0,round_new(round_new(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),2)*1000/4.1816,0)) as rl_farldk,\n" + 
			"       decode(sum(rl.jianzl),0,0,round_new(sum(rl.mt*rl.jianzl)/sum(rl.jianzl),2)) as rl_shuif,\n" + 
			"       decode(sum(rl.jianzl),0,0,round_new(sum(rl.ad*rl.jianzl)/sum(rl.jianzl),2)) as rl_ad,\n" + 
			"       decode(sum(rl.jianzl),0,0,round_new(sum(rl.std*rl.jianzl)/sum(rl.jianzl),2)) as rl_std,\n" + 
			"       decode(sum(rl.jianzl),0,0,round_new(sum(rl.vdaf*rl.jianzl)/sum(rl.jianzl),2)) as rl_vdaf"
			+ "\n"
			+ "\n"
			+ "from\n"
			+ "     (select diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (  select distinct f.diancxxb_id from fahb f  where f.daohrq>=to_date('"+Start_riq+"','yyyy-mm-dd') and  f.daohrq<=to_date('"+End_riq+"','yyyy-mm-dd')  \n"
			+ "        union     \n"
			+ "        select distinct hy.diancxxb_id from meihyb hy  where hy.rulrq>=to_date('"+Start_riq+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+End_riq+"','yyyy-mm-dd') \n"
			+ "     ) dcid,(select decode(1,1,'����') as fenx,1 as xuh  from dual union select decode(1,1,'�ۼ�')  as fenx,2 as xhu from dual ) fx) fx,\n"
			+ "\n"
			+ sql_rc.toString()+ "\n"
			+ sql_rl.toString()+ "\n"
			+ "diancxxb dc, vwfengs gs"+table+"\n"
			+ "where fx.diancxxb_id=rc.id(+)\n"
			+ "and   fx.diancxxb_id=rl.id(+)\n"
			+ "and   fx.diancxxb_id=dc.id\n"
			+ "and   fx.fenx=rc.fenx(+)\n"
			+ "and   fx.fenx=rl.fenx(+)\n"
			+ "and   dc.fuid=gs.id  "+where+" "+strGongsID+"\n"
			+ groupby
			+ "having not grouping (fx.fenx)=1 "+notHuiz+"\n"
			+ ordeby +") l where fenx = '�ۼ�'";

//		System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		String ArrHeader[][]=new String[2][21];
		ArrHeader[0]=new String[] {"��λ","�볧ú��","�볧ú��","�볧ú��","�볧ú��","�볧ú��","�볧ú��","�볧ú��","�볧ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��ֵ��","��ֵ��","ˮ�ֵ�������ֵ��","ˮ�ֵ�������ֵ��"};//"����<br>�ۼ�",
		ArrHeader[1]=new String[] {"��λ","ʵ����<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","ˮ��<br>Mt(%)","�ҷ�<br>Ad(%)","���<br>St,d(%)","�ӷ���<br>Vdaf(%)","��¯ú��<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","ˮ��<br>Mt(%)","�ҷ�<br>Ad(%)","���<br>St,d(%)","�ӷ���<br>Vdaf(%)","(MJ/kg)","(kcal/kg)","(MJ/kg)","(kcal/kg)"};//,"����<br>�ۼ�"

		int ArrWidth[]=new int[] {150,60,60,48,48,48,48,48,48,48,60,60,48,48,48,48,48,48,48,52,52};
		String arrFormat[]=new String[]{"","0","0","0.00","0","0.0","0.00","0.00","0.00","0","0","0.00","0","0.0","0.00","0.00","0.00","0.00","0","0.00","0"};

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1,Table.ALIGN_CENTER);
		}
		//
//		rt.body.setUseDefaultCss(true);
		
		rt.body.ShowZero=false;
		rt.body.setColFormat(arrFormat);
		rt.body.setWidth(ArrWidth);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();		//�ϲ���
		rt.body.mergeFixedCols();		//�Ͳ���
		rt.setTitle("�볧��¯��ֵ��", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 6, FormatDate(DateUtil.getDate(Start_riq))+"-"+FormatDate(DateUtil.getDate(End_riq)),Table.ALIGN_CENTER);
		rt.setDefaultTitle(20, 2, "��λ:��" ,Table.ALIGN_RIGHT);
		
		rt.body.setPageRows(26);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 3, "���:", Table.ALIGN_CENTER);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
					
			rt.setDefautlFooter(20, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(20, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

		(),Table.ALIGN_RIGHT);
				}

//		rt.setDefautlFooter(20, 2, "�Ʊ�:", Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
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

	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());

			visit.setList1(null);
			setTreeid(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setString4(null);
			visit.setString5(null);

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
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
//	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

//	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
//		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
//			_DiancmcChange = false;
//		}else{
//			_DiancmcChange = true;
//		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
//	�ֹ�˾������
//	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean) getFengsModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
//		if (getFengsValue().getId() != Value.getId()) {
//			_fengschange = true;
//		}
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
		setDiancxxModel(new IDropDownModel(sql,"�й����Ƽ���"));
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

	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
