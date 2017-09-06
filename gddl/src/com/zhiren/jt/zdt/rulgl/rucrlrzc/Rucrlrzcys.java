//2008-08-05 chh 
//�޸����� ��ȼ�Ϲ�˾���û����Բ鿴������
//		   ���һ���ѯ��ʾ��ϸ����
//         ȥ��Сͼ����ͼ�ı���������ʾ
package com.zhiren.jt.zdt.rulgl.rucrlrzc;

import java.awt.Color;
import java.awt.GradientPaint;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
// 
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-16
 * ������������ֵ����㷽ʽ����ҳ�������㣩
 * 		 ���δʹ�õ�����
 * 		��ֵ����ֶ�����ֵ��MJ�ֶ�ֱ��ת��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-23
 * �������볧��¯��ֵ���������ʾ�볧����������¯��������Ϣ������������Ӧ�ļ�Ȩ�����������Ӧ�ļ�������Ϣ���м�Ȩ��
 * 		�ڱ����м����볧����������¯��������
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-12-18
 * ������������λ�п��
 */
public class Rucrlrzcys extends BasePage {
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	 //	ҳ���ж�����
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
    
    
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean IsRanlgs(){
		Visit visit=((Visit) getPage().getVisit());
		JDBCcon cn = new JDBCcon();
		boolean isRanlgs=false;
		ResultSet rs =null;
	
		if (visit.getRenyjb() == 2){
			try {
				String sql_diancmc = "select id from diancxxb d where d.shangjgsid="+ visit.getDiancxxb_id();
				rs= cn.getResultSet(sql_diancmc);
			
				while (rs.next()) {
					isRanlgs=true;//�����ȼ�Ϲ�˾�û�
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				cn.Close();
			}
		}
		return isRanlgs;
	}
	private void setReturnValue(){
		String strDiancid=getTreeid();
		Visit visit=((Visit) getPage().getVisit());
		long diancxxb_id=visit.getDiancxxb_id();
		String strleix=getLeixDropDownValue().getValue();
		
		String strOldId="";
		if (String.valueOf(diancxxb_id).equals(strDiancid)){
			if (strleix.equals("�ֿ�")){
				if (!"-1".equals(visit.getString3())){
					visit.setString3("-1");
				}
			}
			return;
		}

		JDBCcon cn = new JDBCcon();
		try {
			String sql_diancmc = "select fuid from diancxxb d where d.id="+ strDiancid;
			if (IsRanlgs()){
				sql_diancmc = "select shangjgsid as fuid from diancxxb d where d.id="+ strDiancid;
			}
			
			ResultSet rs = cn.getResultSet(sql_diancmc);
			
			while (rs.next()) {
				strOldId = rs.getString("fuid");
				if (strOldId.equals("0")){
					return;
				}
			}
			setTreeid(strOldId);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
	}
	
//	��ʼ����v
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setBeginriqDate(Date _value) {
//		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
//			_BeginriqChange=false;
//		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
//			_BeginriqChange=true;
//		}
	}
	
//	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), 0, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndriqDate(Date _value) {
//		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
//			_EndriqChange=false;
//		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
//			_EndriqChange=true;
//		}
	}
	

//	��ʼ����v
//	private boolean _QisrqChange=false;
	public String getQisrq() {
		if (((Visit)getPage().getVisit()).getString12()==null){
			((Visit)getPage().getVisit()).setString12(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit)getPage().getVisit()).getString12();
	}
	
	public void setQisrq(String _value) {
//		if (((Visit)getPage().getVisit()).getString12().equals(_value)) {
//			_QisrqChange=false;
//		} else {
			((Visit)getPage().getVisit()).setString12(_value);
//			_QisrqChange=true;
//		}
	}
//	��������v
//	private boolean _JiezrqChange=false;
	public String getJiezrq() {
		if (((Visit)getPage().getVisit()).getString11()==null){
			((Visit)getPage().getVisit()).setString11(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getString11();
	}
	
	public void setJiezrq(String _value) {
//		if (((Visit)getPage().getVisit()).getString11().equals(_value)) {
//			_JiezrqChange=false;
//		} else {
			((Visit)getPage().getVisit()).setString11(_value);
//			_JiezrqChange=true;
//		}
	}
	
	public boolean getRaw() {
		return true;
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
	
	public void setGongysId(String _value){
		 ((Visit) getPage().getVisit()).setString3(_value);
	}
	
	public String getGongysId(){
		Visit visit = (Visit) getPage().getVisit();
		
		return visit.getString3();
	}
	
//	private String REPORT_ONEGRAPH_RUCMZLYS="rucmzlys";
//	private String mstrReportName="";
	 
	//�õ���½��Ա�����糧��ֹ�˾������
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		return getRucmzlys();
	}
	
	private String getFilerCondtion(int jib){
		String strCondition="";

		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+ " or dc.rlgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}
		return strCondition;
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
	public String getRucmzlys(){
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(getBeginriqDate());//�����ַ�
		String strDate2=DateUtil.FormatDate(getEndriqDate());//�����ַ�
		
		Report rt=new Report();
		
		int jib=this.getDiancTreeJib();
		
		String strgongs = "";
		String strgongs1 = "";
		String strGroupID = "";
		String strQLeix="��";
		String strCondition=getFilerCondtion(jib);
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//ȡ�ü����µ����зֹ�˾
			strgongs1 = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc union select 0,'�ܼ�',0 from dual ";//ȡ�ü����µ����зֹ�˾
			strGroupID = "dc.fgsid";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strgongs = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where (dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+")";//ȡ�÷ֹ�˾�µ����е糧
			strgongs1 = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where " +
					"(dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+") union select 0,'�ܼ�',0 from dual ";//ȡ�÷ֹ�˾�µ����е糧
			strGroupID = "dc.id";
		}else if (jib==3){
			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
			strgongs1 = "select distinct  id, mingc,xuh from vwdianc dc where " +
					"id="+this.getTreeid()+" union select 0,'�ܼ�',0 from dual";
			strGroupID = "dc.id";
		}else if (jib==-1){
			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
			strgongs1 = "select distinct  id, mingc,xuh from vwdianc dc where " +
					"id="+this.getTreeid()+" union select 0,'�ܼ�',0 from dual";
			strGroupID = "dc.id";
		}
		
		/*if ((strleix.equals("�ֳ�") && jib==3)){
			strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
			"        rulmzlb r,meihyb m,vwdianc dc\n" + 
			"        where m.rulmzlb_id(+)=r.id and dc.id=r.diancxxb_id \n" + strCondition+
			"        and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
			"        and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
			strGroupID = " y.dqid ";
			strQLeix="��";
		}*/
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		
		if ((strleix.equals("�ֳ�") && jib==2)){
			//sbsql.append(" select getLinkMingxTaiz('"+this.getTreeid()+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc,");
			sbsql.append(" SELECT danwmc,rc_laimsl,rc_jianzsl,rc_farl,rc_farl_dk,rl_jingz,rl_jianzl,rl_farl,rl_farl_dk,rc_farl-rl_farl tiaozq_mj,ROUND((rc_farl-rl_farl)/0.0041816,0)tiaozq_dk\n");
			sbsql.append(" from (select decode(dc.id,0,dc.mingc,getLinkMingxTaiz('"+this.getTreeid()+"',dc.id,dc.mingc)) as danwmc,");
		}else{
			//sbsql.append(" select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc,");
			sbsql.append(" SELECT danwmc,rc_laimsl,rc_jianzsl,rc_farl,rc_farl_dk,rl_jingz,rl_jianzl,rl_farl,rl_farl_dk,rc_farl-rl_farl tiaozq_mj,ROUND((rc_farl-rl_farl)/0.0041816,0)tiaozq_dk\n");
			sbsql.append(" from (select decode(dc.id,0,dc.mingc,getAlink('"+strQLeix+"',dc.id,dc.mingc)) as danwmc,");
		}
		
		sbsql.append(" Round(rc.laimsl,0) as rc_laimsl,  \n");
		sbsql.append(" Round(rc.jianzsl,0) as rc_jianzsl,  \n");
		sbsql.append(" round_new(rc.farl,"+((Visit) getPage().getVisit()).getFarldec()+") as rc_farl,\n");
		sbsql.append(" round_new(rc.farl/0.0041816,0) as rc_farl_dk,\n");
		sbsql.append(" round_new(rl.laimsl,0) as rl_jingz, \n");
		sbsql.append(" round_new(rl.jianzl,0) as rl_jianzl, \n");
		sbsql.append(" round_new(rl.farl,"+((Visit) getPage().getVisit()).getFarldec()+") as rl_farl, ");
		sbsql.append(" round_new(rl.farl/0.0041816,0) as rl_farl_dk, ");
		sbsql.append(" round_new(rc.farl-rl.farl,"+((Visit) getPage().getVisit()).getFarldec()+") as tiaozq_mj, ");
		sbsql.append(" round_new((rc.farl-rl.farl)/0.0041816,0) as tiaozq_dk ");
		
//		sbsql.append(" decode(sum(rc.laimsl),0,0,round(sum(rc.farl*rc.laimsl)/sum(round(rc.laimsl)),"+((Visit) getPage().getVisit()).getFarldec()+")) as rc_farl,  \n");
//		sbsql.append(" Round(decode(sum(rc.laimsl),0,0,round(sum(rc.farl*rc.laimsl)/sum(round(rc.laimsl)),"+((Visit) getPage().getVisit()).getFarldec()+"))*1000/4.1816,0) as rc_farl_dk,   \n");
//		sbsql.append(" Round(sum(rl.laimsl),0) as rl_jingz,  \n");
//		sbsql.append(" decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+")) as rl_farl,  \n");
//		sbsql.append(" Round( decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+"))*1000/4.1816,0) as rl_farl_dk, \n");
//		sbsql.append(" (decode(sum(rc.laimsl),0,0,Round(sum(rc.farl*rc.laimsl)/sum(rc.laimsl),"+((Visit) getPage().getVisit()).getFarldec()+"))- \n");
//		sbsql.append(" decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+"))) as tiaozq_mj , \n");
//		sbsql.append(" Round((decode(sum(rc.laimsl),0,0,Round(sum(rc.farl*rc.laimsl)/sum(rc.laimsl),"+((Visit) getPage().getVisit()).getFarldec()+"))- \n");
//		sbsql.append(" decode(sum(rl.jianzl),0,0,Round(sum(rl.farl*rl.jianzl)/sum(rl.jianzl),"+((Visit) getPage().getVisit()).getFarldec()+")))*1000/4.1816,0) as tiaozq_dk  \n");
		sbsql.append(" from  \n");
		sbsql.append(" ("+strgongs1+" ) dc, \n");
		sbsql.append(" ( \n");
		sbsql.append("SELECT nvl(ID,0) AS ID,Round(sum(sr.laimsl),0) as laimsl,sum(sr.jianzsl) as jianzsl,\n");
		sbsql.append("decode(sum(sr.jianzsl),0,0,round_new(sum(sr.farl*sr.jianzsl)/sum(sr.jianzsl),2)) AS farl\n");
		sbsql.append("FROM (\n");
		sbsql.append("select  diancxxb_id  as id,\n");
		sbsql.append("        sum(sj.laiml) as laimsl,  \n");
		sbsql.append("        sum(sj.laimzl) as jianzsl,  \n");
		sbsql.append("        decode(sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),0,0,round_new(sum(sj.farl*sj.laimzl)/sum(decode(nvl(sj.farl,0),0,0,sj.laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl \n");
		sbsql.append(" from  \n");
		sbsql.append("       (select "+strGroupID+" as diancxxb_id,fh.gongysb_id,    \n");
		sbsql.append("          "+getLaimlField()+"   as laiml,    \n");
		sbsql.append("       sum(round(fh.laimsl)) as laimsl,  \n");
		sbsql.append("       round(decode(sum(nvl(z.qnet_ar,0)),0,0,   \n");
		sbsql.append("       (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)))) as laimzl,  \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/  \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl  \n");
		sbsql.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y    \n");
		sbsql.append("       where fh.zhilb_id=z.id(+) and fh.diancxxb_id=dc.id and fh.gongysb_id=y.id    \n");
		sbsql.append("         and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')    \n");
		sbsql.append("         and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')    \n");
		sbsql.append(strCondition+" \n");
		sbsql.append(" group by "+strGroupID+",fh.gongysb_id,fh.lieid) sj \n");
		sbsql.append("  group by (sj.diancxxb_id))sr  GROUP BY ROLLUP((ID)) \n");
		sbsql.append(" ) rc, \n");
		
		sbsql.append("(select nvl(dc.id,0) as id, nvl(sum(haoyl),0) as laimsl,sum(nvl(jianzl,0)) as jianzl,  \n");
		sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*jianzl)/sum(jianzl)),2),0) as farl   \n");
		sbsql.append(" from ("+strgongs+" ) dc,  \n");
		sbsql.append("(select dc.id as id,nvl(sum(fadhy+gongrhy),0) as haoyl   \n");
		sbsql.append("from  meihyb m,vwdianc dc where m.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and m.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n");
		sbsql.append(" and dc.id=m.diancxxb_id group by dc.id) haoy,  \n");
		sbsql.append("(select "+strGroupID+" as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,1)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),1) as mt,   \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,   \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,   \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,   \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+((Visit) getPage().getVisit()).getFarldec()+") as qnet_ar,   \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1   \n");
		sbsql.append(" from rulmzlb r,vwdianc dc  \n");
		sbsql.append(" where r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')   \n");
		sbsql.append(" and dc.id(+)=r.diancxxb_id   \n");
		sbsql.append(strCondition+" \n");
		sbsql.append(" group by "+strGroupID+" ) sj   \n");
		sbsql.append(" where sj.id(+)=dc.id and haoy.id(+)=dc.id  \n");
		sbsql.append(" group by rollup (dc.id) ) rl \n");
		
		sbsql.append(" where rl.id(+)=dc.id and rc.id(+)=dc.id  order by dc.mingc,decode(dc.xuh,0,dc.xuh) )sr\n");
//		sbsql.append(" group by rollup(dc.mingc)   \n");
//		sbsql.append(" order by grouping(dc.mingc),max(dc.xuh),dc.mingc  \n");
		
		
		 String ArrHeader[][]=new String[3][11];
		 ArrHeader[0]=new String[] {"�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��","�볧��¯��ֵ��"};
		 ArrHeader[1]=new String[] {"��λ","�볧ú��","�볧ú��","�볧ú��","�볧ú��","��¯ú��","��¯ú��","��¯ú��","��¯ú��","��ֵ��","��ֵ��"};
		 ArrHeader[2]=new String[] {"��λ","ʵ����<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","��¯ú��<br>(��)","������<br>(��)","��ֵ<br>(MJ/kg)","��ֵ<br>(kcal/kg)","(MJ/kg)","(kcal/kg)"};

		 int ArrWidth[]=new int[] {100,70,70,70,70,70,70,70,70,70,70};
		 
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		rt.setCenter(false);
		rt.setBody(new Table(rs,3,0,1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setFontSize(12);
		
		rt.body.setUseCss(true);
		rt.body.setColHeaderClass("tab_colheader");
		rt.body.setRowHeaderClass("tab_rowheader");
		rt.body.setFirstDataRowClass("tab_data_line_one");
		rt.body.setSecondDataRowClass("tab_data_line_two");
		rt.body.setCellsClass("tab_cells");
		rt.body.setTableClass("tab_body");
		
		rt.body.setBorder(2);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero = true;
		
		cn.Close();
		return rt.getHtml();
	}
	
	//����ͼ
	public String getChart3D(){
		String strDate1=DateUtil.FormatDate(getBeginriqDate());//�����ַ�
		String strDate2=DateUtil.FormatDate(getEndriqDate());//�����ַ�
		
		int jib=this.getDiancTreeJib();
//		String strCondition=getFilerCondtion(jib);
		String strgongs = "";
		String strGroupID = "";
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsid,dc.rlgsid,dc.fgsxh as xuh from vwdianc dc ";//ȡ�ü����µ����зֹ�˾
			strGroupID = "dc.fgsid";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strgongs = "select distinct dc.id,dc.mingc,dc.xuh,dc.fgsid,dc.rlgsid from vwdianc dc where (dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+")";//ȡ�÷ֹ�˾�µ����е糧
			strGroupID = "dc.id";
		}else if (jib==3){
			strgongs = "select distinct  id, mingc,xuh,dc.fgsid,dc.rlgsid from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}else if (jib==-1){
			strgongs = "select distinct  id, mingc,xuh,dc.fgsid,dc.rlgsid from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" select dc.mingc as danwmc, \n");
		sbsql.append(" nvl(sum(haoyl),0) as laimsl,sum(nvl(jianzl,0)) as jianzl, \n");
		sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(mt,"+visit.getMtdec()+")*jianzl)/sum(jianzl)),"+visit.getMtdec()+"),0) as mt,  \n");
		sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(ad*jianzl)/sum(jianzl)),2),0) as ad,  \n");
		sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(vdaf*jianzl)/sum(jianzl)),2),0) as vdaf,  \n");
		sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(std*jianzl)/sum(jianzl)),2),0) as std,  \n");
		sbsql.append(" nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0) as qnet_ar,  \n");
		sbsql.append(" round_new(nvl(round_new(decode(sum(jianzl),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0)/0.0041816,0) as qnet_ar1  \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("(select "+strGroupID+" as id,nvl(sum(fadhy+gongrhy),0) as haoyl  \n");
		sbsql.append("from  meihyb m,vwdianc dc where m.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and m.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n");
		sbsql.append(" and dc.id=m.diancxxb_id group by "+strGroupID+") haoy, \n");
		sbsql.append("(select "+strGroupID+" as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl, \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,"+visit.getMtdec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getMtdec()+") as mt,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getFarldec()+") as qnet_ar,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,2)/0.0041816*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),0) as qnet_ar1  \n");
		sbsql.append(" from rulmzlb r,vwdianc dc \n");
		sbsql.append(" where r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n");
		sbsql.append(" and dc.id(+)=r.diancxxb_id  \n");
		sbsql.append(" group by "+strGroupID+" ,dc.mingc,dc.fgsmc,dc.fgsxh) sj  \n");
		sbsql.append(" where sj.id(+)=dc.id and haoy.id(+)=dc.id \n");
		sbsql.append(" group by dc.mingc \n");
		sbsql.append(" order by max(dc.xuh),dc.mingc");
	   	ResultSet rs = cn.getResultSet(sbsql.toString());
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		
		DefaultPieDataset dataset = cd.getRsDataPie(rs, "danwmc", "laimsl", true);
		ct.showLegend=false;
		ct.chartBackgroundPaint=gp;
		ct.pieLabFormat=false;//����ʾ��������
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChart3D','show')\"");
		String charImg=ct.ChartPie3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChart3D");
		ct.showLegend=true;
		ct.pieLabFormat=true;//����ʾ��������
		ct.pieLabGenerator=ct.piedatformat2;//��ʾ�ٷֱ�
		ct.setDisplayPicture(false);
		((Visit) getPage().getVisit()).setString7(ct.ChartPie3D(getPage(),dataset, "", 600, 300));
		cn.Close();
		return charImg;
	}
	
	public String getChart3DBig(){
		return ((Visit) getPage().getVisit()).getString7();
	}
	
//	����ͼ
	public String getChartBar(){
//		String strDataField=getDataField();
		String strDate1=DateUtil.FormatDate(getBeginriqDate());//�����ַ�
		String strDate2=DateUtil.FormatDate(getEndriqDate());//�����ַ�
		String strDate3=getQisrq();
		String strDate4=getJiezrq();
		int jib=this.getDiancTreeJib();
		String date1="";
		String date2="";
		String date3="";
		String date4="";
		
		String strgongs = "";
		String strgongsq="";
		String strGroupID = "";
		String strCondition=getFilerCondtion(jib);
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//ȡ�ü����µ����зֹ�˾
			strGroupID = "dc.fgsid";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strgongs = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where (dc.fgsid="+this.getTreeid() +" or dc.rlgsid="+this.getTreeid()+")";//ȡ�÷ֹ�˾�µ����е糧
			strGroupID = "dc.id";
		}else if (jib==3){
			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}else if (jib==-1){
			strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}
		
		if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
			
			date1=" and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and fh.daohrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-12) and fh.daohrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12)  \n";
			date3=" and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date4=" and r.rulrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-12) and r.rulrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12)  \n";
		
		}else if(getRbvalue().equals("tiaoj2")){
			
			date1=" and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and fh.daohrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-1) and fh.daohrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1)  \n";
			date3=" and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date4=" and r.rulrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-1) and r.rulrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1)  \n";
		
		}else{
			
			date1=" and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and fh.daohrq>=to_date('"+strDate3+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+strDate4+"','yyyy-mm-dd') \n";
			date3=" and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date4=" and r.rulrq>=to_date('"+strDate3+"','yyyy-mm-dd') and r.rulrq<=to_date('"+strDate4+"','yyyy-mm-dd') \n";
		
		}
		strgongsq=strgongs;
		
		Visit visit= (Visit)getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append(" select rc.danwmc,rc.fenx,round_new((rc.farl-rl.farl)/0.0041816,0) rzc from  \n");
		//�볧
		sbsql.append("( \n");
		sbsql.append(" select danwmc,fenx,round(decode(sum(jianzsl),0,0,round(sum(farl)/sum(jianzsl),2)),"+((Visit) getPage().getVisit()).getFarldec()+") as farl  from (");
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'����','����') as fenx,nvl(farl,0) as farl,nvl(mt,0) as mt,nvl(ad,0) as ad,nvl(vdaf,0) as vdaf,nvl(std,0) as std, nvl(jianzsl, 0) as jianzsl  \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id, \n");
		sbsql.append(" "+getLaimlField()+"  as laimsl,\n");
		sbsql.append(" round(decode(sum(nvl(z.qnet_ar,0)),0,0,  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)) )) as laimzl,\n");
		sbsql.append(" decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl,\n");
		sbsql.append(" sum(round(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.std) as std \n");
		sbsql.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y  \n");
		sbsql.append("       where fh.zhilb_id=z.id and fh.gongysb_id=y.id  \n");
		sbsql.append(date1).append(strCondition);
		sbsql.append("         and fh.diancxxb_id=dc.id    \n");
		sbsql.append("         and fh.gongysb_id=y.id \n");
		sbsql.append("         group by "+strGroupID+",dc.mingc,y.mingc,dc.fgsmc,dc.fgsxh,y.mingc,y.smc,y.dqmc,fh.lieid) sj  \n");
		sbsql.append(" where sj.id(+)=dc.id  \n");
		sbsql.append(" union all  \n");
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'ͬ��','ͬ��') as fenx,nvl(farl,0) farl,nvl(mt,0) as mt,nvl(ad,0) as ad,nvl(vdaf,0) as vdaf,nvl(std,0) as std,nvl(laimzl,0) as laimzl  \n");
		sbsql.append(" from ( "+strgongsq+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id,\n");
		sbsql.append(" "+getLaimlField()+"  as laimsl,\n");
		sbsql.append(" round(decode(sum(nvl(z.qnet_ar,0)),0,0,  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)) )) as laimzl,\n");
		sbsql.append(" decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl,\n");
		sbsql.append(" sum(round(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.std) as std \n");
		sbsql.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y  \n");
		sbsql.append("       where fh.zhilb_id=z.id and fh.gongysb_id=y.id  \n");
		sbsql.append(date2).append(strCondition);
		sbsql.append("         and fh.diancxxb_id=dc.id    \n");
		sbsql.append("         and fh.gongysb_id=y.id \n");
		sbsql.append("         group by "+strGroupID+",dc.mingc,y.mingc,dc.fgsmc,dc.fgsxh,y.mingc,y.smc,y.dqmc,fh.lieid) sj  \n");
		sbsql.append(" where sj.id(+)=dc.id )  group by danwmc,fenx order by danwmc \n");

		sbsql.append(") rc,\n");
		//��¯
		sbsql.append("( \n");
		sbsql.append(" select danwmc,fenx,nvl(round(decode(sum(jianzl),0,0,sum(farl*jianzl)/sum(jianzl)),"+visit.getFarldec()+"),0) as farl  from ( \n");
		sbsql.append(" select dc.mingc as danwmc,xuh, decode(1,1,'����','����') as fenx,nvl(jianzl,0) as jianzl,nvl(mt,0) as mt,nvl(ad,0) as ad,nvl(vdaf,0) as vdaf,nvl(std,0) as std,nvl(qnet_ar,0) as farl,nvl(qnet_ar1,0) as qnet_ar1  \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,"+visit.getMtdec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getMtdec()+") as mt,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getFarldec()+") as qnet_ar,  \n");
		sbsql.append(" round_new(round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getFarldec()+")/0.0041816,0) as qnet_ar1  \n");
		sbsql.append("       from rulmzlb r,vwdianc dc  \n");
		sbsql.append("      where r.diancxxb_id=dc.id  \n");
		sbsql.append(date3).append(strCondition);
		sbsql.append("         group by "+strGroupID+") sj  \n");
		sbsql.append(" where sj.id(+)=dc.id  \n");
		sbsql.append(" union all  \n");
		sbsql.append(" select dc.mingc as danwmc,xuh, decode(1,1,'ͬ��','ͬ��') as fenx,nvl(jianzl,0) as jianzl,nvl(mt,0) as mt,nvl(ad,0) as ad,nvl(vdaf,0) as vdaf,nvl(std,0) as std,nvl(qnet_ar,0) as farl,nvl(qnet_ar1,0) as qnet_ar1  \n");
		sbsql.append(" from ( "+strgongsq+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id,sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(mt,"+visit.getMtdec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getMtdec()+") as mt,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(ad*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as ad,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(vdaf*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as vdaf,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(std*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),2) as std,  \n");
		sbsql.append(" round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getFarldec()+") as qnet_ar,  \n");
		sbsql.append(" round_new(round_new(decode(sum(decode(nvl(qnet_ar,0),0,0,r.meil)),0,0,sum(round_new(qnet_ar,"+visit.getFarldec()+")*r.meil)/sum(decode(nvl(qnet_ar,0),0,0,r.meil))),"+visit.getFarldec()+")/0.0041816,0) as qnet_ar1  \n");
		sbsql.append("       from rulmzlb r,vwdianc dc  \n");
		sbsql.append("      where r.diancxxb_id=dc.id  \n");
		sbsql.append(date4).append(strCondition);
		sbsql.append("         group by "+strGroupID+") sj  \n");
		sbsql.append(" where sj.id(+)=dc.id )  group by danwmc,fenx order by fenx, max(xuh),danwmc \n");
		
		sbsql.append(") rl where rc.danwmc=rl.danwmc and rc.fenx=rl.fenx \n");
		


		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		CategoryDataset dataset = cd.getRsDataChart(rs, "danwmc", "fenx", "rzc");
		ct.intDigits=2;				//	��ʾС��λ��
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = false;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.chartBackgroundPaint=gp;
		
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartBar','show')\" ");
		String charImg=ct.ChartBar3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChartBar");
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//��б��ʾX�������
		ct.showLegend = true;
		ct.setImgEvents("");
		ct.setDisplayPicture(false);
		((Visit) getPage().getVisit()).setString5(ct.ChartBar3D(getPage(),dataset, "", 600, 300));
		//((Visit) getPage().getVisit()).setString5("");
		// charImg="";
		cn.Close();
		return charImg;
	}
	
	public String getChartBarBig(){
		return ((Visit) getPage().getVisit()).getString5();
	}
	
	
//	����ͼ
	public String getChartLine(){
		String strDateBegin=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(getEndriqDate()), -1, DateUtil.AddType_intYear));//�����ַ�
		String strDateEnd=DateUtil.FormatDate(getEndriqDate());//�����ַ�
		Visit visit= (Visit)getPage().getVisit();
		int jib=this.getDiancTreeJib();
		String strCondition=getFilerCondtion(jib);
//		String strDataField=getDataField();
		
		JDBCcon cn = new JDBCcon();
		 
		StringBuffer sbsql = new StringBuffer();
	
		sbsql.append("select rc.yuef,rc.fenx, round_new((rc.farl-rl.farl)/0.0041816,0) as rzc from ");
		//�볧
		sbsql.append("(");
		
		sbsql.append("select to_date(bq.yuef||'-01','yyyy-mm-dd') as yuef,bq.fenx,  \n");
		sbsql.append(" decode(sum(jianzsl),0,0,round(sum(farl)/sum(jianzsl),2)) as farl \n");
		sbsql.append("  from (select to_char(daohrq,'yyyy-mm') as yuef, \n");
		sbsql.append(" "+getLaimlField()+"  as laimsl,  \n");
		sbsql.append("  decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl,\n");
		sbsql.append(" round(decode(sum(nvl(z.qnet_ar,0)),0,0,  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)) )) as laimzl,\n");
		sbsql.append(" sum(round(fh.laimzl,0)*round_new(z.qnet_ar,"+visit.getFarldec()+")) as farl,sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.std) as std \n");
		sbsql.append("               from fahb fh,zhilb z,vwdianc dc,vwgongys y  \n");
		sbsql.append("        where fh.zhilb_id=z.id and fh.gongysb_id=y.id   \n");
		sbsql.append("              and fh.daohrq>=to_date('"+strDateBegin+"','yyyy-mm-dd')   \n");
		sbsql.append("              and fh.daohrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
		sbsql.append("              and fh.diancxxb_id=dc.id     \n").append(strCondition);
		sbsql.append("              and fh.gongysb_id=y.id  \n");
		sbsql.append("         group by to_char(daohrq,'yyyy-mm')) bqsj, \n");
		sbsql.append("         (select yuef,rownum as yf,nvl('����','') as fenx from vwnianyue  \n");
		sbsql.append("           where to_date(yuef,'yyyy-mm') <= to_date('"+strDateEnd+"','yyyy-mm-dd') and rownum<=12) bq \n");
		sbsql.append(" where bq.yuef=bqsj.yuef(+) group by bq.yuef,bq.fenx \n");
		sbsql.append(" union all \n");
		sbsql.append(" select  add_months(to_date(to_char(bq.yuef)||'-01','yyyy-mm-dd'),12) as yuef,bq.fenx, \n");
		sbsql.append(" decode(sum(jianzsl),0,0,round(sum(farl)/sum(jianzsl),2)) as farl \n");
		sbsql.append("  from (select to_char(daohrq,'yyyy-mm') as yuef, \n");
		sbsql.append(" "+getLaimlField()+"  as laimsl,  \n");
		sbsql.append("  decode(sum(nvl(z.qnet_ar,0)),0,0,sum(round(fh.laimzl,0))) as jianzsl,\n");
		sbsql.append(" round(decode(sum(nvl(z.qnet_ar,0)),0,0,  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)) )) as laimzl,\n");
		sbsql.append(" sum(round(fh.laimzl,0)*round_new(z.qnet_ar,"+visit.getFarldec()+")) as farl,sum(round(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+")) as mt, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.vdaf) as vdaf, sum(round(fh.laimzl,0)*z.ad) as ad, \n");
		sbsql.append(" sum(round(fh.laimzl,0)*z.std) as std \n");
		sbsql.append("               from fahb fh,zhilb z,vwdianc dc,vwgongys y  \n");
		sbsql.append("        where fh.zhilb_id=z.id and fh.gongysb_id=y.id   \n");
		sbsql.append("              and fh.daohrq>=add_months(to_date('"+strDateBegin+"','yyyy-mm-dd'),-12) \n");
		sbsql.append("              and fh.daohrq<=add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12) \n");
		sbsql.append("              and fh.diancxxb_id=dc.id     \n").append(strCondition);
		sbsql.append("              and fh.gongysb_id=y.id  \n");
		sbsql.append("         group by to_char(daohrq,'yyyy-mm')) bqsj  , \n");
		sbsql.append("         (select yuef,rownum as yf,nvl('ͬ��','') as fenx  from vwnianyue \n");
		sbsql.append("            where to_date(yuef,'yyyy-mm') <= add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12) and rownum<=12) bq \n");
		sbsql.append("  where bq.yuef=bqsj.yuef(+) group by bq.yuef,bq.fenx \n");
		sbsql.append(" order by yuef,fenx \n");
		
		sbsql.append(") rc,");
		//��¯
		sbsql.append("(");
		sbsql.append("select to_date(bq.yuef||'-01','yyyy-mm-dd') as yuef,bq.fenx, \n");
		sbsql.append(" decode(sum(jianzl),0,0,round_new(sum(farl)/sum(jianzl),"+visit.getFarldec()+")) as farl  \n");
		sbsql.append("  from (select to_char(r.rulrq,'yyyy-mm') as yuef, \n");
		sbsql.append("  sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,\n");
		sbsql.append("  sum(round(r.meil,0)*round_new(qnet_ar,"+visit.getFarldec()+")) as farl \n");
		sbsql.append("               from rulmzlb r,vwdianc dc  \n");
		sbsql.append("              where r.rulrq>=to_date('"+strDateBegin+"','yyyy-mm-dd')   \n");
		sbsql.append("              and r.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') and dc.id=r.diancxxb_id  \n");
		sbsql.append(strCondition);
		sbsql.append("         group by to_char(r.rulrq,'yyyy-mm')) bqsj, \n");
		sbsql.append("         (select yuef,rownum as yf,nvl('����','') as fenx from vwnianyue  \n");
		sbsql.append("           where to_date(yuef,'yyyy-mm') <= to_date('"+strDateEnd+"','yyyy-mm-dd') and rownum<=12) bq \n");
		sbsql.append(" where bq.yuef=bqsj.yuef(+) group by bq.yuef,bq.fenx \n");
		sbsql.append(" union all \n");
		sbsql.append("select add_months(to_date(to_char(bq.yuef)||'-01','yyyy-mm-dd'),12) as yuef,bq.fenx, \n");
		sbsql.append(" decode(sum(jianzl),0,0,round_new(sum(farl)/sum(jianzl),"+visit.getFarldec()+")) as farl  \n");
		sbsql.append("  from (select to_char(r.rulrq,'yyyy-mm') as yuef, \n");
		sbsql.append("  sum(decode(nvl(qnet_ar,0),0,0,r.meil)) as jianzl,\n");
		sbsql.append("  sum(round(r.meil,0)*round_new(qnet_ar,"+visit.getFarldec()+")) as farl \n");
		sbsql.append("               from rulmzlb r,vwdianc dc  \n");
		sbsql.append("         where r.rulrq>=add_months(to_date('"+strDateBegin+"','yyyy-mm-dd'),-12) \n");
		sbsql.append("              and r.rulrq<=add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12) and dc.id=r.diancxxb_id \n");
		sbsql.append(strCondition);
		sbsql.append("         group by to_char(r.rulrq,'yyyy-mm')) bqsj  , \n");
		sbsql.append("         (select yuef,rownum as yf,nvl('ͬ��','') as fenx  from vwnianyue \n");
		sbsql.append("           where to_date(yuef,'yyyy-mm') <= add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12) and rownum<=12) bq \n");
		sbsql.append("  where bq.yuef=bqsj.yuef(+) group by bq.yuef,bq.fenx \n");
		sbsql.append(" order by yuef,fenx \n");
		sbsql.append(") rl where rc.yuef=rl.yuef and rc.fenx=rl.fenx ");
		
		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","yuef", "rzc");//rs��¼����������ͼƬ��Ҫ������
		ct.intDigits=0;
		ct.lineDateFormatOverride="MM";
		ct.setDateUnit(Chart.DATEUNIT_MONTH, 1);
		ct.chartBackgroundPaint=gp;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartLine','show')\"");
		String charImg=ct.ChartTimeGraph(getPage(),data2, "", 200, 120);
		ct.setID("imgChartLine");
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//��б��ʾX�������
		ct.showLegend = true;
		ct.setImgEvents("");
		ct.setDisplayPicture(false);
		((Visit) getPage().getVisit()).setString6(ct.ChartTimeGraph(getPage(),data2, "", 600, 300));
		
		cn.Close();
		return charImg;
	}
	
	public String getChartLineBig(){
		return ((Visit) getPage().getVisit()).getString6();
	}
	
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ReturnClick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			return;
		}
		if (_ReturnClick){
			_ReturnClick=false;
			setReturnValue();
			return;
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString3("-1");
			visit.setString13("");
			
			visit.setDate1(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));

			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			this.setTreeid(null);
			
			//begin��������г�ʼ������
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			this.setBeginriqDate(visit.getMorkssj());
			this.setEndriqDate(visit.getMorjssj()); 
//			getDayValue();
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDate2(DateUtil.AddDate(new Date(), 0, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setDropDownBean2(null);
					visit.setDropDownBean3(null);
					visit.setDropDownBean4(null);
					visit.setDropDownBean5(null);
					
					visit.setProSelectionModel1(null);
					visit.setProSelectionModel2(null);
					visit.setProSelectionModel3(null);
					visit.setProSelectionModel4(null);
					visit.setProSelectionModel5(null);
					this.setTreeid(null);
					this.setBeginriqDate(visit.getMorkssj());
					this.setEndriqDate(visit.getMorjssj()); 
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
//			mstrReportName = visit.getString1();
        }
//		else{
//        	if(!visit.getString1().equals("")) {
//        		mstrReportName = visit.getString1();
//            }
//        }
		getToolBars();
		isBegin=true;
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		
//		Visit visit = (Visit) getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		String danx1="";
        String danx2="";
        String danx3="";
        if(getRbvalue().equals("tiaoj1")||getRbvalue().equals("")){
			danx1="checked:true ,   \n";
		}else if(getRbvalue().equals("tiaoj2")){
			danx2="checked:true ,   \n";
		}else{
			danx3="checked:true ,   \n";
		}

	    String Strtmpfunction="var form = new Ext.FormPanel({ "
	        + "labelAlign:'right', \n"
	        + "frame:true,\n"
	        + "items: [ \n"
		    + "{ \n"
		    + "  layout:'column',\n"
		    + "  items:[{ \n"
		    + "    layout:'form',\n"
		    + "    columnWidth:.5,"
		    + "       items:[{ \n"
		    + "    	    xtype:'textfield', \n"
		    + "    		fieldLabel:'����ѡ��',\n"
		    + "    		width:0 \n"
		    + "    		},	\n"
			+ " 	{ \n"
			+ "         xtype:'radio',\n"
			+ "			boxLabel:'��ͬ�ڶԱ�', \n"
		    + "     	Value:'tiaoj1', \n"
		    + "         labelSeparator:'',\n"
		    +	danx1
			+ "			name:'tiaoj',\n"
			+ "			listeners:{ \n"
			+ "				'check':function(r,c){ \n"
			+ "                if(r.checked){"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				}\n"
			+ "         }"
			+ "		} \n"
			+ "		},\n"
			+ "		{  \n"   
			+ "         xtype:'radio',\n"
			+ "			boxLabel:'����',\n"
			+ "			Value:'tiaoj2', \n"
			+ "         labelSeparator:'',\n"
			+	danx2
			+ "			name:'tiaoj',\n"
			+ "			listeners:{ \n"
			+ "				'check':function(r,c){ \n"
			+ "					if(r.checked){"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				}\n"
			+ "           }"
			+ "			} \n"
			+ "		},		\n"	
			+ "		{  \n"
			+ "         xtype:'radio',\n"
			+ "			boxLabel:'ʱ��Ա�------------>�����Ҳ�����ʱ��',\n"
			+ "			Value:'tiaoj3', \n"
			+ "         labelSeparator:'',\n"
			+	danx3
			+ "			name:'tiaoj',\n"
			+ "			listeners:{ \n"
			+ "				'check':function(r,c){ \n"
			+ "					if(r.checked){"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				}\n"
			+ "            }"
			+ "			} \n"
			+ "		}]},		\n"	
			+ "		{ \n"
			+ "   		layout:'form',\n"
			+ "   		columnWidth:.5,\n"
			+ "   		items:[{},{},{},{},{},\n"
			+ "   	{ \n"
			+ "			xtype:'datefield', \n"   
			+ "			fieldLabel:'��ʼ����', \n"   
			+ "			name:'qisrq', \n" 
			+ "    	 	listeners:{change:function(own,newValue,oldValue) {document.getElementById('QISRQ').value = newValue.dateFormat('Y-m-d');}},\n"
			+ "     	value:document.getElementById('QISRQ').value \n"
			+ "		}, \n"   
			+ "		{ \n"   
			+ "			xtype:'datefield', \n"   
			+ "			fieldLabel:'��ֹ����', \n"   
			+ "			name:'jiezrq', \n"
			+ "     	value:'', \n"
			+ "     	listeners:{change:function(own,newValue,oldValue) {document.getElementById('JIEZRQ').value = newValue.dateFormat('Y-m-d');}},\n"
			+ "     	value:document.getElementById('JIEZRQ').value \n"
			+ "			}] \n"
			+ "			}] \n"
			+ "		}] \n"
			+ " });\n"
			+ " win = new Ext.Window({\n"
			+ " layout:'fit',\n"
			+ " width:500,\n"
			+ " height:300,\n"
			+ " closeAction:'hide',\n"
			+ " plain: true,\n"
			+ " title:'����',\n"
			+ " items: [form],\n"
			+ " buttons: [{\n"
			+ "   text:'ȷ��',\n"
			+ "   handler:function(){  \n"
			+ "  	win.hide();\n"
			+ "		document.getElementById('TEXT_RADIO_SELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
			+ " 	document.getElementById('RefurbishButton').click(); \n"
			+ "  	}   \n"
			+ "},{\n"
			+ "   text: 'ȡ��',\n"
			+ "   handler: function(){\n"
			+ "     win.hide();\n"
			+ "   }\n"
			+ "}]\n"
           
			+ " });";
	    
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(60);
 		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("��:"));
		DateField edf = new DateField();
		edf.setValue(DateUtil.FormatDate(this.getEndriqDate()));
		edf.Binding("EndriqDateSelect","");// ��htmlҳ�е�id��,���Զ�ˢ��
		edf.setWidth(60);
		tb1.addField(edf);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tbok = new ToolbarButton(null,"ȷ��","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		ToolbarButton tbtj=new ToolbarButton(null,"��������","function(){ if(!win){ "+Strtmpfunction+"}"
				+ " win.show(this);	\n" 
				+ "}"); 
		tbtj.setIcon(SysConstant.Btn_Icon_Search);	
		ToolbarButton tbfh = new ToolbarButton(null,"�����ϼ�","function(){document.getElementById('ReturnButton').click();}");
		tbfh.setIcon(SysConstant.Btn_Icon_Return);
		
//		ToolbarButton tbfh1 = new ToolbarButton(null,"�볧��¯��ֵ��","function(){document.getElementById('RucrlButton').click();}");
//		tbfh1.setIcon(SysConstant.Btn_Icon_Search);
		
		tb1.addItem(tbok);
		tb1.addItem(tbtj);
		tb1.addItem(tbfh);
//		tb1.addItem(tbfh1);
		
		setToolbar(tb1);
	}
	
//	�糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;
	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql="";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
	}
	
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

//	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		Visit visit =((Visit) getPage().getVisit());
		visit.setString2(treeid);
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
		return getTree().getWindowTreeScript();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
	

//	����
	public IDropDownBean getLeixDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getLeixDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeixDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeixDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeixDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeixDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeixDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(2, "�ֳ�"));
		list.add(new IDropDownBean(1, "�ֿ�"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
//	�ƻ��ھ�
	public IDropDownBean getJihkjDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean) getJihkjDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setJihkjDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getJihkjDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getJihkjDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getJihkjDropDownModels() {
		String sql = "select id,mingc\n" + "from jihkjb \n";
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql, "ȫ��"));
		return;
	}	
	
//	���䷽ʽ
	public IDropDownBean getYunsfsDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getYunsfsDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYunsfsDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setYunsfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getYunsfsDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYunsfsDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getYunsfsDropDownModels() {
		String sql = "select id,mingc\n" + "from yunsfsb \n";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "ȫ��"));
		return;
	}
	
//	 ��Ӧ��
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
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
		return;
	}
	

	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString13(rbvalue);
	}
	
//	����
//	public IDropDownBean getChartDropDownValue() {
//		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
//			((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean) getChartDropDownModel().getOption(0));
//		}
//		return ((Visit) getPage().getVisit()).getDropDownBean6();
//	}
//
//	public void setChartDropDownValue(IDropDownBean Value) {
//		((Visit) getPage().getVisit()).setDropDownBean6(Value);
//	}
//
//	public void setChartDropDownModel(IPropertySelectionModel value) {
//		((Visit) getPage().getVisit()).setProSelectionModel6(value);
//	}
//
//	public IPropertySelectionModel getChartDropDownModel() {
//		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
//			getChartDropDownModels();
//		}
//		return ((Visit) getPage().getVisit()).getProSelectionModel6();
//	}
//
//	public void getChartDropDownModels() {
//		List list = new ArrayList();
//		list.add(new IDropDownBean(1, "��λ������"));
//		list.add(new IDropDownBean(2, "ȫˮ��mt(%)"));
//		list.add(new IDropDownBean(3, "�ӷ���vdaf(%)"));
//		list.add(new IDropDownBean(4, "�ҷ�ad(%)"));
//		list.add(new IDropDownBean(5, "���std(%)"));
//		
//		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
//		return;
//	}
	
//	private String getDataField(){
//		String strDataField=getChartDropDownValue().getValue();
//		
//		if (strDataField.equals("��λ������")){
//			return "  round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(qnet_ar/0.0041816*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)))) as farl \n";
//		}else if(strDataField.equals("ȫˮ��mt(%)")){
//			return "round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(mt*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as farl \n";
//		}else if(strDataField.equals("�ӷ���vdaf(%)")){
//			return " round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(vdaf*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as farl \n";
//		}else if(strDataField.equals("�ҷ�ad(%)")){
//			return "  round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(ad*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as farl \n";
//		}else if(strDataField.equals("���std(%)")){
//			return " round(decode(sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy)),0,0,sum(std*(fadhy+gongrhy))/sum(decode(nvl(qnet_ar,0),0,0,fadhy+gongrhy))),2) as farl \n";
//		}
//		return " nvl(Round((decode(sum(sj.laimzl),0,0,Round(sum(sj.farl*sj.laimzl)/sum(sj.laimzl),2))-decode(sum(sj1.rulml),0,0,Round(sum(sj1.farl*sj1.rulml)/sum(sj1.rulml),2)))*1000/4.1816,0),0) as farl \n";
//	}
	
//	private int getDigts(){
//		String strDataField=getChartDropDownValue().getValue();
//		if (strDataField.equals("��λ������")){
//			return 0;
//		}else if(strDataField.equals("ȫˮ��mt(%)")){
//			return 1;
//		}else if(strDataField.equals("�ӷ���vdaf(%)")){
//			return 2;
//		}else if(strDataField.equals("�ҷ�ad(%)")){
//			return 2;
//		}else if(strDataField.equals("���std(%)")){
//			return 3;
//		}
//		return 0;
//	}
}