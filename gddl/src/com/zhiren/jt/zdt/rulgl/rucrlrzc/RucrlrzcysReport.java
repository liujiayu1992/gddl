////2008-08-05 chh 
//�޸����� ��ȼ�Ϲ�˾���û����Բ鿴������
//		   ��ϸ������ʾ
/* 
* ʱ�䣺2009-09-3
* ���ߣ� ll
* �޸����ݣ�1�����û�����Ϊ�ֹ�˾ʱ����ʾ��ϸ�������ˡ��ܼơ��С�
*/ 
package com.zhiren.jt.zdt.rulgl.rucrlrzc;
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
/*
 * ���ߣ����
 * ʱ�䣺2012-10-16
 * ������������ֵ����㷽ʽ����ҳ�������㣩
 * 		 �����볧��¯��ֵ��Ȩ��ʽ��������������ֵʱ��������������ֵ���м�Ȩ��
 * 		��ֵ����ֶ�����ֵ��MJ�ֶ�ֱ��ת��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-23
 * �������볧��¯��ֵ���������ʾ�볧����������¯��������Ϣ������������Ӧ�ļ�Ȩ�����������Ӧ�ļ�������Ϣ���м�Ȩ��
 * 		�ڱ����м����볧����������¯��������
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class RucrlrzcysReport extends BasePage {
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
		String diancid="";
		int intLen=0;
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				diancid=" and dc.id=" +pa[1];
				//strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}else{
				diancid=" and dc.id=-1";
			}
		}else{
			if (jib==2){
				diancid=" and (dc.fgsid=" +strDiancxxb_id +" or dc.rlgsid="+strDiancxxb_id +")"; 
			}else if (jib==3){
				diancid=" and dc.id=" +strDiancxxb_id;
			}else if (jib==-1){
				diancid=" and dc.id=" +strDiancxxb_id;
			}
		}
		return diancid;
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
	private String getPrintData(){
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		String strDate1=DateUtil.FormatDate(datStart);//�����ַ�
		String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
		
		
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
		
//		sbSqlBody.append(" Round(sum(rc.laimsl),0) as rc_laimsl,  \n");
//		sbSqlBody.append(" decode(sum(rc.laimsl),0,0,round(sum(rc.farl*rc.laimsl)/sum(round(rc.laimsl)),"+((Visit) getPage().getVisit()).getFarldec()+")) as rc_farl,  \n");
//		sbSqlBody.append(" Round(decode(sum(rc.laimsl),0,0,round(sum(rc.farl*rc.laimsl)/sum(round(rc.laimsl)),"+((Visit) getPage().getVisit()).getFarldec()+"))*1000/4.1816,0) as rc_farl_dk,   \n");
//		sbSqlBody.append(" Round(sum(rl.laimsl),0) as rl_jingz,  \n");
//		sbSqlBody.append(" decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+")) as rl_farl,  \n");
//		sbSqlBody.append(" Round( decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+"))*1000/4.1816,0) as rl_farl_dk, \n");
		
		sbSqlBody.append(" Round(sum(rc.laimsl),0) as rc_laimsl, \n"); 
		sbSqlBody.append(" Round(sum(rc.jianzsl),0) as rc_jianzsl, \n"); 
		sbSqlBody.append(" decode(sum(decode(nvl(rc.farl,0),0,0,rc.jianzsl)),0,0,round_new(sum(rc.farl*rc.jianzsl)/sum(decode(nvl(rc.farl,0),0,0,rc.jianzsl)),2)) as rc_farl,\n");    
		sbSqlBody.append(" decode(sum(decode(nvl(rc.farl,0),0,0,rc.jianzsl)),0,0,round_new(round_new(sum(rc.farl*rc.jianzsl)/sum(decode(nvl(rc.farl,0),0,0,rc.jianzsl)),2)/0.0041816,0)) as rc_farl_dk,\n");    
		sbSqlBody.append(" Round(sum(rl.laimsl),0) as rl_jingz,\n"); 
		sbSqlBody.append(" Round(sum(rl.jianzl),0) as rl_jianzl,\n"); 
		sbSqlBody.append(" decode(sum(decode(nvl(rl.farl,0),0,0,rl.jianzl)),0,0,round_new(sum(rl.farl*rl.jianzl)/sum(decode(nvl(rl.farl,0),0,0,rl.jianzl)),2)) as rl_farl,\n");    
		sbSqlBody.append(" decode(sum(decode(nvl(rl.farl,0),0,0,rl.jianzl)),0,0,round_new(round_new(sum(rl.farl*rl.jianzl)/sum(decode(nvl(rl.farl,0),0,0,rl.jianzl)),2)/0.0041816,0)) as rl_farl_dk,\n"); 
		
		sbSqlBody.append(" (decode(sum(rc.jianzsl),0,0,Round(sum(rc.farl*rc.jianzsl)/sum(rc.jianzsl),"+((Visit) getPage().getVisit()).getFarldec()+"))- \n");
		sbSqlBody.append(" decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+"))) as tiaozq_mj , \n");
		sbSqlBody.append(" Round((decode(sum(rc.jianzsl),0,0,Round(sum(rc.farl*rc.jianzsl)/sum(rc.jianzsl),"+((Visit) getPage().getVisit()).getFarldec()+"))- \n");
		sbSqlBody.append(" decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+")))*1000/4.1816,0) as tiaozq_dk  \n");
		sbSqlBody.append(" from  \n");
		sbSqlBody.append(" (select dc.id,dc.mingc as diancmc,dc.fgsmc,dc.xuh from vwdianc dc where 1=1 "+getCondtion()+" ) dc, \n");
		sbSqlBody.append(" ( select diancxxb_id  as id,        \n");
		sbSqlBody.append("        sum(sj.laiml) as laimsl,  \n");
		sbSqlBody.append("        sum(sj.laimzl) as jianzsl,  \n");
		sbSqlBody.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.farl*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl \n");
		sbSqlBody.append(" from  \n");
		sbSqlBody.append("       (select fh.diancxxb_id,fh.gongysb_id,    \n");
		sbSqlBody.append("          "+getLaimlField()+"   as laiml,    \n");
		sbSqlBody.append("       sum(round(fh.laimsl)) as laimsl,  \n");
		sbSqlBody.append("       round(decode(sum(nvl(z.qnet_ar,0)),0,0,   \n");
		sbSqlBody.append("       (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)))) as laimzl,  \n");
		sbSqlBody.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
		sbSqlBody.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/  \n");
		sbSqlBody.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl  \n");
		sbSqlBody.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y    \n");
		sbSqlBody.append("       where fh.zhilb_id=z.id(+) and fh.diancxxb_id=dc.id and fh.gongysb_id=y.id    \n");
		sbSqlBody.append("         and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')    \n");
		sbSqlBody.append("         and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')    \n");
		sbSqlBody.append(getCondtion()+" \n");
		sbSqlBody.append(" group by fh.diancxxb_id,fh.gongysb_id,fh.lieid) sj \n");
		sbSqlBody.append("  group by sj.diancxxb_id    \n");
		sbSqlBody.append(" order by sj.diancxxb_id  ) rc, \n");
		
		sbSqlBody.append("(select dc.id, nvl(sum(haoyl),0) as laimsl,sum(nvl(jianzl,0)) as jianzl,  \n");
		sbSqlBody.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*jianzl)/sum(jianzl)),2),0) as farl   \n");
		sbSqlBody.append(" from (select dc.id,dc.mingc as diancmc,dc.fgsmc,dc.xuh from vwdianc dc where 1=1 "+getCondtion()+" ) dc,  \n");
		sbSqlBody.append("(select dc.id as id,nvl(sum(fadhy+gongrhy),0) as haoyl   \n");
		sbSqlBody.append("from  meihyb m,vwdianc dc where m.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and m.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n");
		sbSqlBody.append(" and dc.id=m.diancxxb_id group by dc.id) haoy,  \n");
		sbSqlBody.append("(select dc.id as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,  \n");
		sbSqlBody.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,1)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),1) as mt,   \n");
		sbSqlBody.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,   \n");
		sbSqlBody.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,   \n");
		sbSqlBody.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,   \n");
		sbSqlBody.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+((Visit) getPage().getVisit()).getFarldec()+") as qnet_ar,   \n");
		sbSqlBody.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1   \n");
		sbSqlBody.append(" from rulmzlb r,vwdianc dc  \n");
		sbSqlBody.append(" where r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')   \n");
		sbSqlBody.append(" and dc.id(+)=r.diancxxb_id   \n");
		sbSqlBody.append(getCondtion()+" \n");
		sbSqlBody.append(" group by dc.id ) sj   \n");
		sbSqlBody.append(" where sj.id(+)=dc.id and haoy.id(+)=dc.id  \n");
		sbSqlBody.append(" group by dc.id ) rl \n");
		sbSqlBody.append(" where rl.id(+)=dc.id and rc.id(+)=dc.id   \n");
		
		if (leix.equals(LX_FC)){
			sbsql.append("  SELECT mingc,rc_laimsl,rc_jianzsl,rc_farl,rc_farl_dk,rl_jingz,rl_jianzl,rl_farl,rl_farl_dk,rc_farl-rl_farl tiaozq_mj,ROUND((rc_farl-rl_farl)/0.0041816,0) tiaozq_dk  \n");
			sbsql.append(" from(select decode(grouping(fgsmc)+grouping(diancmc),2,'�ܼ�',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||diancmc) as mingc, \n");
			//sbsql.append("decode(grouping(diancmc),1,'С��',sj.riq) as riq,\n");
			sbsql.append(sbSqlBody);
			sbsql.append("  group by rollup(fgsmc,diancmc)  \n");
			sbsql.append("  having not grouping(fgsmc)=1 \n");
			sbsql.append("  order by grouping(fgsmc) desc,fgsmc,grouping(diancmc) desc,diancmc,max(xuh))sr \n");
			strTitle="(�ֳ�)";
		}else if(leix.equals(LX_QP)){
			strTitle="(����)";
		}else if(leix.indexOf(",")>0){
			return getPrintDataTz();
		}
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName="�볧��¯��ֵ��"+strTitle;
		
		// �����ͷ����
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][]=new String[2][11];
		 ArrHeader[0]=new String[] {"��λ","�볧ú��","�볧ú��","�볧ú��","�볧ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��ֵ��","��ֵ��"};
		 ArrHeader[1]=new String[] {"��λ","ʵ����<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","��¯ú��<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","(MJ/kg)","(kcal/kg)"};

		 int ArrWidth[]=new int[] {120,60,60,60,60,60,60,60,60,60,60};
		
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4,  FormatDate(DateUtil.getDate(strDate1))+"-"+FormatDate(DateUtil.getDate(strDate2)),Table.ALIGN_CENTER); 
		rt.setDefaultTitle(10, 2, "��λ:��",Table.ALIGN_RIGHT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 4, "�Ʊ�ʱ��:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "���:",Table.ALIGN_CENTER);
		if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
			
			rt.setDefautlFooter(10, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
			}else{
				
				rt.setDefautlFooter(10, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

	(),Table.ALIGN_RIGHT);
			}
//		rt.setDefautlFooter(8, 2, "�Ʊ�:",Table.ALIGN_RIGHT);

		
		// ����ҳ��
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
			
			String strCondtion_rc="";
//			String strCondtion_rl="";
			
			String strDate1=DateUtil.FormatDate(datStart);//�����ַ�
			String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
			
			strCondtion_rc="and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
				"and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
//			strCondtion_rl="and h.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
//			"and h.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
			
			 StringBuffer sbsql = new StringBuffer();
			sbsql.append("SELECT danwmc,riq,rc_laimsl,rc_laimzl,rc_farl,rc_farldk,rl_jingz,rl_ruljzl,rl_farl,rl_farldk,rc_farl-rl_farl tiaozq_mj,ROUND((rc_farl-rl_farl)/0.0041816,0) tiaozq_dk \n");
			sbsql.append("from (select decode(grouping(riq.mingc),1,'�ܼ�',riq.mingc) as danwmc,to_char(riq.riq,'yyyy-mm-dd') as riq,\n");
			sbsql.append("Round(sum(sj.laimsl),0) as rc_laimsl,\n");
			sbsql.append("Round(sum(sj.laimzl),0) as rc_laimzl,\n");
			sbsql.append(" decode(sum(decode(nvl(sj.farl,0),0,0,sj.rucjzl)),0,0,round_new(sum(sj.farl*sj.rucjzl)/sum(decode(nvl(sj.farl,0),0,0,sj.rucjzl)),2)) as rc_farl,\n");
			sbsql.append(" decode(sum(decode(nvl(sj.farl,0),0,0,sj.rucjzl)),0,0,round_new(round_new(sum(sj.farl*sj.rucjzl)/sum(decode(nvl(sj.farl,0),0,0,sj.rucjzl)),2)/0.0041816,0)) as rc_farldk,\n");
			sbsql.append(" Round(sum(sj1.rulml),0) as rl_jingz,  \n");
			sbsql.append(" Round(sum(sj1.ruljzl),0) as rl_ruljzl,  \n");
			sbsql.append(" decode(sum(decode(nvl(sj1.farl,0),0,0,sj1.ruljzl)),0,0,round_new(sum(sj1.farl*sj1.ruljzl)/sum(decode(nvl(sj1.farl,0),0,0,sj1.ruljzl)),2)) as rl_farl,\n");
			sbsql.append(" decode(sum(decode(nvl(sj1.farl,0),0,0,sj1.ruljzl)),0,0,round_new(round_new(sum(sj1.farl*sj1.ruljzl)/sum(decode(nvl(sj1.farl,0),0,0,sj1.ruljzl)),2)/0.0041816,0)) as rl_farldk,\n");
			sbsql.append(" (decode(sum(sj.rucjzl),0,0,Round(sum(sj.farl*sj.rucjzl)/sum(sj.rucjzl),2))-decode(sum(sj1.ruljzl),0,0,Round(sum(sj1.farl*sj1.ruljzl)/sum(sj1.ruljzl),2))) as tiaozq_mj , \n");
			sbsql.append(" Round((decode(sum(sj.rucjzl),0,0,Round(sum(sj.farl*sj.rucjzl)/sum(sj.rucjzl),2))-decode(sum(sj1.ruljzl),0,0,Round(sum(sj1.farl*sj1.ruljzl)/sum(sj1.ruljzl),2)))*1000/4.1816,0) as tiaozq_dk  \n");
			sbsql.append("  from  \n");
			//����
			sbsql.append(" ( select dc.mingc,dc.id,(to_date('"+strDate1+"','yyyy-mm-dd')-1)+rownum as riq \n");
			sbsql.append("  from all_objects,vwdianc dc \n");
			sbsql.append("  where rownum<=(to_date('"+strDate2+"','yyyy-mm-dd')-to_date('"+strDate1+"','yyyy-mm-dd')+1) "+getCondtion()+") riq, \n");
			//�볧
			sbsql.append("  (select dc.id as id, f.daohrq as riq,sum(f.laimsl) as laimsl,sum(f.laimzl) as laimzl,  sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)) as rucjzl, \n");
			sbsql.append(" decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)),0,0,sum(round_new(nvl(z.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+")*f.laimzl)/sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl))) as farl \n");
			sbsql.append(" from fahb f,zhilb z,vwdianc dc  \n");
			sbsql.append(" where f.zhilb_id=z.id(+) "+strCondtion_rc+"  \n");
			sbsql.append(" "+getCondtion()+" and dc.id(+)=f.diancxxb_id  \n");
			sbsql.append(" group by dc.id,dc.mingc,dc.fgsmc,dc.fgsxh,f.daohrq) sj,  \n");
			//��¯
			sbsql.append("(select dc.id as id,sj.riq riq,sum(sj.rulml) as rulml,sum(sj.rulmzl) as ruljzl,\n"); 
			sbsql.append(" decode(sum(decode(nvl(sj.farl,0),0,0,sj.rulmzl)),0,0,sum(sj.farl*(sj.rulmzl))/sum(decode(nvl(sj.farl,0),0,0,sj.rulmzl))) as farl\n");
			sbsql.append(" from \n");
			sbsql.append("(select riq.id,riq.riq,fady,gongrhy,rulml,rz.meil as rulmzl,rz.farl from\n");
//			����
			sbsql.append(" ( select dc.mingc,dc.id,(to_date('"+strDate1+"','yyyy-mm-dd')-1)+rownum as riq \n");
			sbsql.append("  from all_objects,vwdianc dc \n");
			sbsql.append("  where rownum<=(to_date('"+strDate2+"','yyyy-mm-dd')-to_date('"+strDate1+"','yyyy-mm-dd')+1) "+getCondtion()+") riq, \n");
			//����
			sbsql.append("(select hy.diancxxb_id,hy.rulrq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy,sum(hy.fadhy+hy.gongrhy) as rulml \n");
			sbsql.append("	from meihyb hy where hy.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n");
			sbsql.append("	 group by hy.diancxxb_id,hy.rulrq) hy,\n");
			//��¯����
			sbsql.append("(select rz.diancxxb_id,rz.rulrq,sum(decode(nvl(rz.qnet_ar,0),0, 0,rz.meil)) as meil,\n");
			sbsql.append("	decode(sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil)),0,0, \n");
			sbsql.append("	 sum((rz.meil)*round_new(rz.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil))) as farl\n");
			sbsql.append("	from rulmzlb rz where rz.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and rz.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n");
			sbsql.append("	group by rz.diancxxb_id,rz.rulrq) rz \n");
			
			sbsql.append("where hy.diancxxb_id(+)=riq.id and rz.diancxxb_id(+)=riq.id and hy.rulrq(+)=riq.riq and rz.rulrq(+)=riq.riq) sj,vwdianc dc \n");
			sbsql.append("where sj.id=dc.id  \n");//and hy.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')
			sbsql.append(" group by dc.id,dc.mingc,dc.fgsmc,dc.fgsxh,sj.riq) sj1 \n");
			
			
			sbsql.append(" where riq.id=sj.id(+) and riq.id=sj1.id(+) and riq.riq=sj.riq(+) and riq.riq=sj1.riq(+)  \n");
			sbsql.append("  group by rollup (riq.mingc,riq.riq) \n");
			sbsql.append("  having not (grouping(riq.mingc) || grouping(riq.riq)) =1 \n");
			sbsql.append("  order by grouping(riq.mingc) desc,riq.mingc,grouping(riq.riq) desc,riq.riq)sr \n");
			
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="�볧��¯��ֵ����ϸ"+strTitle;
		
		// �����ͷ����
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		String ArrHeader[][]=new String[2][12];
		 ArrHeader[0]=new String[] {"��λ","����","�볧ú��","�볧ú��","�볧ú��","�볧ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��ֵ��","��ֵ��"};
		 ArrHeader[1]=new String[] {"��λ","����","ʵ����<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","��¯ú��<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","(MJ/kg)","(kcal/kg)"};

		 int ArrWidth[]=new int[] {150,80,60,60,60,60,60,60,60,60,60,60};
		
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 5, FormatDate(DateUtil.getDate(strDate1))+"-"+FormatDate(DateUtil.getDate(strDate2)),Table.ALIGN_CENTER); 
//		rt.setDefaultTitle(6,  2, "", Table.ALIGN_LEFT);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setUseDefaultCss(true);
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
		visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
		//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			
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
//	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

//	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
//		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
//			_DiancmcChange = false;
//		} else {
//			_DiancmcChange = true;
//		}
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