package com.zhiren.jt.zdt.chengbgl.ricb;

import org.apache.tapestry.html.BasePage;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
/* 
* ʱ�䣺2009-07-20
* ���ߣ� ll
* �޸����ݣ��޸Ĳ�ѯsql��ʽ��   
*			
*/ 
/* 
* ʱ�䣺2009-08-7
* ���ߣ� ll
* �޸����ݣ�1�������ճɱ���ѯ���㹫ʽ���޸�ҳ����ʾsql��       
*			2��ҳ�����ӹ�����
*/ 
/* 
* ʱ�䣺2009-08-29
* ���ߣ� ll
* �޸����ݣ�1���޸�������������fahb ��diiancxxb_id ��vwdianc ,zhilb_id ��zhilb����,
* 			  fahb�ĵ�������������
* 			  �볧�����ݱ���һ�¡�
* 		   2�����ӵ��ɱ��������ۻ��ɱ������С�
*/ 
/* 
* ʱ�䣺2009-09-17
* ���ߣ� ll
* �޸����ݣ��ۺϵ����۹�ʽ�޸�
*/ 
public class Ricb extends BasePage {
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
		
		String sql_diancmc = "select fuid from diancxxb d where d.id="+ strDiancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
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
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_BeginriqChange=true;
		}
	}
//	��������v
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), 0, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndriqChange=true;
		}
	}
	
//	��ʼ����v
	private boolean _QisrqChange=false;
	public String getQisrq() {
		if (((Visit)getPage().getVisit()).getString12()==null){
			((Visit)getPage().getVisit()).setString12(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit)getPage().getVisit()).getString12();
	}
	
	public void setQisrq(String _value) {
		if (((Visit)getPage().getVisit()).getString12().equals(_value)) {
			_QisrqChange=false;
		} else {
			((Visit)getPage().getVisit()).setString12(_value);
			_QisrqChange=true;
		}
	}
//	��ֹ����v
	private boolean _JiezrqChange=false;
	public String getJiezrq() {
		if (((Visit)getPage().getVisit()).getString11()==null){
			((Visit)getPage().getVisit()).setString11(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getString11();
	}
	
	public void setJiezrq(String _value) {
		if (((Visit)getPage().getVisit()).getString11().equals(_value)) {
			_JiezrqChange=false;
		} else {
			((Visit)getPage().getVisit()).setString11(_value);
			_JiezrqChange=true;
		}
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
	
	private String REPORT_ONEGRAPH_JIESBMDJYS="jiesbmdjys";
	private String mstrReportName="";
	 
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
		}
		cn.Close();
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		return getRicbcx();
	}
	
	public String getRicbcx(){
		Date dat1=getBeginriqDate();//����
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//�����ַ�
		
		Date dat2=getEndriqDate();//����
		String strDate2=DateUtil.FormatDate(dat2);//�����ַ�
		
		Report rt=new Report();
		
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="��";
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and ys.id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jh.id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if  (strleix.equals("�ֳ�")){
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.fgsid";
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID = "dc.id";
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strCondition =strCondition+ " and f.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
					"        rigjb r,fahb f,jihkjb jh,yunsfsb ys,vwgongys y,vwdianc dc\n" + 
					"        where r.fahb_id(+)=f.lieid and f.gongysb_id=y.id and dc.id=f.diancxxb_id and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id \n" + strCondition+
					"        and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
					"        and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				strGroupID = " dc.dqid ";
				strQLeix="��";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				diancid = "";
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh  from vwdianc dc";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.fgsid";
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,mingc,dc.xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID = "dc.id";
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//ȡ��ĳ���ڶεĹ�����λ		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
				"        rigjb r,fahb f,jihkjb jh,yunsfsb ys,vwgongys y,vwdianc dc\n" + 
				"        where r.fahb_id(+)=f.lieid and f.gongysb_id=y.id and dc.id=f.diancxxb_id and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id\n" + strCondition+
				"        and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
				"        and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "dc.dqid";
				strQLeix="��";
			}
		}
		
		JDBCcon cn = new JDBCcon();
		String danwmc=getTreeDiancmc(this.getTreeid());
		
		StringBuffer sbsql = new StringBuffer();

//		---------------------------�޸ĺ�-------------------------------------------------------------------------------

//		sbsql.append("select danwmc,laimsl,drcbl,ljrez,ljrezdk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj   \n");
		sbsql.append("select danwmc,laimsl,drcbl,ljrez,ljrezdk,round_new((ljmeij+ljyunf+ljzaf),2) as daoczhj   \n");
		sbsql.append("   ,ljmeij,ljyunf,ljzaf   \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as ljhansbmdj   \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as ljbmdj   \n");
		sbsql.append("from (   \n");
		if ((strleix.equals("�ֳ�") && jib==3)){
			sbsql.append(" select getLinkMingxTaiz('"+this.getTreeid()+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc,");
		}else if ((strleix.equals("�ֿ�") && jib==2 &&  !"-1".equals(getGongysId()))){
			sbsql.append(" select getLinkMingxTaiz(decode(grouping(dc.mingc),1,-1,max(dc.id)) ,'"+getGongysId()+"',decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc,");
		}else{
			sbsql.append("select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)), decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc, \n");
		}
		sbsql.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as drcbl,   \n");
		sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf    \n");
		sbsql.append("    from (select "+strGroupID+" as id,  \n");
		sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf   \n");
		sbsql.append("            from ( select f.lieid,dc.id as id,y.dqid,dc.fgsid, \n");
		sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,   \n");
		sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf   \n");
		sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys   \n");
		sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id    \n");
		sbsql.append("                      and f.diancxxb_id=dc.id   \n");
		sbsql.append("                      and f.zhilb_id =zl.id(+)   \n");
		sbsql.append("                      and f.gongysb_id=y.id       \n").append(strCondition).append(" \n");
		sbsql.append("                      and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')   \n");
		sbsql.append("                  group by (y.dqid,f.lieid,dc.id,dc.fgsid)) dc   \n");
		sbsql.append("             group by "+strGroupID+" )  wclj,--�ճɱ�   \n");
		sbsql.append("       ( "+strgongs+" ) dc   \n");
		sbsql.append("  where wclj.id(+)=dc.id    \n");
		sbsql.append("  group by rollup(dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc     \n");
		sbsql.append(")   \n");
		
		String ArrHeader[][]=new String[2][11];
		ArrHeader[0]=new String[] {"��λ","����","�ɱ���","�볧����","�볧����","�����ۺϼ�","ú��","�˷�","�ӷ�","��˰<br>��ú����","����˰<br>��ú����"};
		ArrHeader[1]=new String[] {"��λ","��","��","MJ/kg","Kcal/kg","Ԫ/��"," Ԫ/�� ","Ԫ/��"," Ԫ/�� ","Ԫ/��"," Ԫ/�� "};

		int ArrWidth[]=new int[] {120,70,70,55,55,70,55,55,55,70,70};
//		System.out.println(sbsql.toString());
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		rt.setCenter(false);
		rt.setBody(new Table(rs,2,0,1));
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
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero = true;
		
		cn.Close();
		return rt.getHtml();
	}
	
	//����ͼ
	public String getChartPie3D(){
		Date dat1=getBeginriqDate();//����
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//�����ַ�
		
		Date dat2=getEndriqDate();//����
		String strDate2=DateUtil.FormatDate(dat2);//�����ַ�
		
		
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="��";
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and ys.id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jh.id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if  (strleix.equals("�ֳ�")){
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.fgsid";
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID = "dc.id";
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strCondition =strCondition+ " and r.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
					"        rigjb r,fahb f,vwgongys y,vwdianc dc,jihkjb jh,yunsfsb ys  \n" + 
					"        where r.fahb_id(+)=f.lieid and f.gongysb_id=y.id and dc.id=f.diancxxb_id and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id \n" + strCondition+
					"        and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
					"        and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				strGroupID = " dc.dqid ";
				strQLeix="��";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				diancid = "";
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.fgsid";
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,mingc,dc.xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID = "dc.id";
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//ȡ��ĳ���ڶεĹ�����λ		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
				"         rigjb r,fahb f,vwgongys y,vwdianc dc,jihkjb jh,yunsfsb ys  \n" + 
				"        where r.fahb_id(+)=f.lieid and f.gongysb_id=y.id and dc.id=f.diancxxb_id and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id  \n" + strCondition+
				"        and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
				"        and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "dc.dqid";
				strQLeix="��";
			}
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();

//		--------------------------------�޸ĺ�--------------------------------------------------
		sbsql.append("select danwmc,cbsl \n");
		sbsql.append("from (   \n");
		sbsql.append("select  dc.mingc as danwmc,");
		sbsql.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as cbsl,  \n");
		sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf    \n");
		sbsql.append("    from (select "+strGroupID+" as id,  \n");
		sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf   \n");
		sbsql.append("            from ( select f.lieid,dc.id as id,y.dqid,dc.fgsid, \n");
		sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,   \n");
		sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf   \n");
		sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys   \n");
		sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id    \n");
		sbsql.append("                      and f.diancxxb_id=dc.id   \n");
		sbsql.append("                      and f.zhilb_id =zl.id(+)   \n");
		sbsql.append("                      and f.gongysb_id=y.id       \n").append(strCondition).append(" \n");
		sbsql.append("                      and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')   \n");
		sbsql.append("                  group by (y.dqid,f.lieid,dc.id,dc.fgsid)) dc   \n");
		sbsql.append("             group by "+strGroupID+" )  wclj,--�ճɱ�   \n");
		sbsql.append("       ( "+strgongs+" ) dc   \n");
		sbsql.append("  where wclj.id(+)=dc.id    \n");
		sbsql.append("  group by (dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc     \n");
		sbsql.append(")   \n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		
		DefaultPieDataset dataset = cd.getRsDataPie(rs, "danwmc", "cbsl", true);
		
		ct.showLegend=false;
		ct.chartBackgroundPaint=gp;
		ct.pieLabFormat=false;//����ʾ��������
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartPie3D','show')\"");
		String charImg=ct.ChartPie3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChartPie3D");
		ct.showLegend=true;
		ct.pieLabFormat=true;//����ʾ��������
		ct.pieLabGenerator=ct.piedatformat2;//��ʾ�ٷֱ�
		ct.setImgEvents("");
		((Visit) getPage().getVisit()).setString7(ct.ChartPie3D(getPage(),dataset, "", 600, 300));
		
		return charImg;
	}
	
	public String getChartPie3DBig(){
		return ((Visit) getPage().getVisit()).getString7();
	}
	
//	����ͼ
	public String getChartBar(){
		Date dat1=getBeginriqDate();//����
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//�����ַ�
		
		Date dat2=getEndriqDate();//����
		String strDate2=DateUtil.FormatDate(dat2);//�����ַ�
		String strDate3=getQisrq();
		String strDate4=getJiezrq();
		String date1="";
		String date2="";
		
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="��";
		
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and ys.id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jh.id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if  (strleix.equals("�ֳ�")){
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc ";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.fgsid";
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,dc.mingc,dc.xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID = "dc.id";
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strCondition =strCondition+ " and r.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
				"        rigjb r,fahb f,vwgongys y,vwdianc dc,jihkjb jh,yunsfsb ys  \n" + 
				"        where r.fahb_id(+)=f.lieid and f.gongysb_id=y.id and dc.id=f.diancxxb_id and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id \n" + strCondition+
				"        and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
				"        and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				strGroupID = " dc.dqid ";
				strQLeix="��";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				diancid = "";
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.fgsid";
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,mingc,xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID = "dc.id";
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//ȡ��ĳ���ڶεĹ�����λ		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
				"        rigjb r,fahb f,vwgongys y,vwdianc dc,jihkjb jh,yunsfsb ys  \n" + 
				"        where r.fahb_id(+)=f.lieid and f.gongysb_id=y.id and dc.id=f.diancxxb_id and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id \n" + strCondition+
				"        and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
				"        and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "dc.dqid";
				strQLeix="��";
			}
		}
		
    	JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		

		if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
			date1=" and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and f.daohrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-12) and f.daohrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12) \n";
		}else if(getRbvalue().equals("tiaoj2")){
			date1=" and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and f.daohrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-1) and f.daohrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1) \n";
		}else{
			date1=" and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
			date2=" and f.daohrq>=to_date('"+strDate3+"','yyyy-mm-dd') and f.daohrq<=to_date('"+strDate4+"','yyyy-mm-dd') \n";
		}
//		-------------------------�޸ĺ�-------------------------------------------------
//		sbsql.append("select danwmc,fenx,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj   \n");
		sbsql.append("select danwmc,fenx,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new((ljmeij+ljyunf+ljzaf),2) as daoczhj   \n");
		sbsql.append("   ,ljmeij as hansdj,ljyunf as  yunf,ljzaf as zaf  \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as hansbmdj   \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as buhsbmdj   \n");
		sbsql.append("from (   \n");
		sbsql.append("	 select dc.mingc as danwmc,decode(1,1,'����') as fenx, ");
		sbsql.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as cbsl,  \n");
		sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf    \n");
		sbsql.append("    from (select "+strGroupID+" as id,  \n");
		sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf   \n");
		sbsql.append("            from ( select f.lieid,dc.id as id,y.dqid,dc.fgsid, \n");
		sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,   \n");
		sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf   \n");
		sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys   \n");
		sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id    \n");
		sbsql.append("                      and f.diancxxb_id=dc.id   \n");
		sbsql.append("                      and f.zhilb_id =zl.id(+)   \n");
		sbsql.append("                      and f.gongysb_id=y.id       \n").append(strCondition).append(" \n");
		sbsql.append("                      "+date1+" \n");
		sbsql.append("                  group by (y.dqid,f.lieid,dc.id,dc.fgsid)) dc   \n");
		sbsql.append("             group by "+strGroupID+" )  wclj,--�ճɱ�   \n");
		sbsql.append("       ( "+strgongs+" ) dc   \n");
		sbsql.append("  where wclj.id(+)=dc.id    \n");
		sbsql.append("  group by (dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc     \n");
		sbsql.append(")   \n");
		sbsql.append("union \n");
		sbsql.append("select danwmc,fenx,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj   \n");
		sbsql.append("   ,ljmeij as hansdj,ljyunf as  yunf,ljzaf as zaf  \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as hansbmdj   \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as buhsbmdj   \n");
		sbsql.append("from (   \n");
		sbsql.append("	 select dc.mingc as danwmc,decode(1,1,'ͬ��') as fenx, ");
		sbsql.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as cbsl,  \n");
		sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf    \n");
		sbsql.append("    from (select "+strGroupID+" as id,  \n");
		sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf   \n");
		sbsql.append("            from ( select f.lieid,dc.id as id,y.dqid,dc.fgsid, \n");
		sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,   \n");
		sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf   \n");
		sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys   \n");
		sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id    \n");
		sbsql.append("                      and f.diancxxb_id=dc.id   \n");
		sbsql.append("                      and f.zhilb_id =zl.id(+)   \n");
		sbsql.append("                      and f.gongysb_id=y.id       \n").append(strCondition).append(" \n");
		sbsql.append("                      "+date2+" \n");
		sbsql.append("                  group by (y.dqid,f.lieid,dc.id,dc.fgsid)) dc   \n");
		sbsql.append("             group by "+strGroupID+" )  wclj,--�ճɱ�   \n");
		sbsql.append("       ( "+strgongs+" ) dc   \n");
		sbsql.append("  where wclj.id(+)=dc.id    \n");
		sbsql.append("  group by (dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc     \n");
		sbsql.append(")   \n");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		CategoryDataset dataset = cd.getRsDataChart(rs, "danwmc", "fenx",  getDataField());
		ct.intDigits=getDigts();				//	��ʾС��λ��
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = false;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.chartBackgroundPaint=gp;
		//return ct.ChartBar3D(getPage(), dataset, "", 200, 120);
		
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartBar','show')\" ");
		String charImg=ct.ChartBar3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChartBar");
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//��б��ʾX�������
		ct.showLegend = true;
		ct.setImgEvents("");
		
		((Visit) getPage().getVisit()).setString5(ct.ChartBar3D(getPage(),dataset, "", 600, 300));
		
		return charImg;
	}
	
	public String getChartBarBig(){
		return ((Visit) getPage().getVisit()).getString5();
	}
	
	
//	����ͼ
	public String getChartLine(){
		String strDateBegin=DateUtil.FormatDate(DateUtil.getFirstDayOfYear(getBeginriqDate()));//�����ַ�
		String strDateEnd=DateUtil.FormatDate(DateUtil.getLastDayOfYear(getBeginriqDate()));//�����ַ�
		
		int jib=this.getDiancTreeJib();
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and ys.id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jh.id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
//		------------------------�޸ĺ�---------------------------------------------------------
//		sbsql.append("select yuef,fenx,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj   \n");
		sbsql.append("select yuef,fenx,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new((ljmeij+ljyunf+ljzaf),2) as daoczhj   \n");
		sbsql.append("   ,ljmeij as hansdj,ljyunf as  yunf,ljzaf as zaf  \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as hansbmdj   \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as buhsbmdj   \n");
		sbsql.append("from (   \n");
		sbsql.append("	 select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx as fenx, ");
		sbsql.append("   SUM(wclj.LAIML) AS laimsl, sum(wclj.cbsl) as cbsl,  \n");
		sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf    \n");
		sbsql.append("    from (select to_char(dc.daohrq,'MM') as yuef,  \n");
		sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf   \n");
		sbsql.append("            from ( select f.lieid,dc.id as id,y.dqid,dc.fgsid,f.daohrq, \n");
		sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,   \n");
		sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf   \n");
		sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys   \n");
		sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id    \n");
		sbsql.append("                      and f.diancxxb_id=dc.id   \n");
		sbsql.append("                      and f.zhilb_id =zl.id(+)   \n");
		sbsql.append("                      and f.gongysb_id=y.id       \n").append(strCondition).append(" \n");
		sbsql.append("          			and f.daohrq>=to_date('"+strDateBegin+"','yyyy-mm-dd')  \n");
		sbsql.append("           			and f.daohrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')    \n");
		sbsql.append("                  group by (y.dqid,f.lieid,dc.id,dc.fgsid,f.daohrq)) dc   \n");
		sbsql.append("             group by to_char(dc.daohrq,'MM') )  wclj,--�ճɱ�   \n");
		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'����') as fenx   \n");
		sbsql.append("    from xitxxb  where rownum<=12) bq    \n");
		sbsql.append("        where bq.yuef=wclj.yuef(+)   \n");
		sbsql.append("group by (to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd'),bq.fenx)  \n");
		sbsql.append(")   \n");
		sbsql.append("union \n");
		sbsql.append("select yuef,fenx,laimsl,cbsl,ljrez as farl,ljrezdk as farl_dk,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)/ljrez)),2) as daoczhj   \n");
		sbsql.append("   ,ljmeij as hansdj,ljyunf as  yunf,ljzaf as zaf  \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij+ljyunf+ljzaf)*29.271/ljrez)),2) as hansbmdj   \n");
		sbsql.append("   ,round_new(decode(ljrez,0,0,((ljmeij/1.17+ljyunf*0.93+ljzaf)*29.271/ljrez)),2) as buhsbmdj   \n");
		sbsql.append("from (   \n");
		sbsql.append("	 select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx as fenx, ");
		sbsql.append("   SUM(wclj.LAIML) AS laimsl,sum(wclj.cbsl) as cbsl,   \n");
		sbsql.append("   round_new(round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2)*1000/4.1816,0) as ljrezdk,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.farl,0))/sum(nvl(wclj.cbsl,0))),2) as ljrez,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.meij,0))/sum(nvl(wclj.cbsl,0))),2) as   ljmeij,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.yunf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljyunf,   \n");
		sbsql.append("   round_new(decode(sum(nvl(wclj.cbsl,0)),0,0,sum(nvl(wclj.cbsl*wclj.zaf,0))/sum(nvl(wclj.cbsl,0))),2) as   ljzaf    \n");
		sbsql.append("    from (select to_char(dc.daohrq,'MM') as yuef,  \n");
		sbsql.append("                 SUM(LAIML) AS LAIML,sum(cbsl) as cbsl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*farl)/sum(cbsl)) as farl,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*meij)/sum(cbsl)) as   meij,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*yunf)/sum(cbsl)) as   yunf,   \n");
		sbsql.append("                 decode(sum(cbsl),0,0,sum(cbsl*zaf)/sum(cbsl)) as   zaf   \n");
		sbsql.append("            from ( select f.lieid,dc.id as id,y.dqid,dc.fgsid,f.daohrq, \n");
		sbsql.append("                      round_new(sum(f.biaoz),0)+round_new(sum(f.yingd),0)-round_new(sum(f.yingd-f.yingk),0)   as laiml,   \n");
		sbsql.append("                      round_new(sum(decode(nvl(r.id,0),0,0,f.biaoz)),0)+round_new(sum(decode(nvl(r.id,0),0,0,f.yingd)),0)-round_new(sum(decode(nvl(r.id,0),0,0,f.yingd-f.yingk)),0) as cbsl, --�ɱ�����,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(round_new(nvl(zl.qnet_ar,0),2)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as farl,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.meij,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as meij,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.disyf+r.qiyf+r.kuangyf+r.yingdyf+r.daozzf ,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as yunf,   \n");
		sbsql.append("                      decode(sum(decode(nvl(r.id,0),0,0,f.laimsl)),0,0,sum(nvl(r.jiaohqzf+r.qitzf,0)*decode(nvl(r.id,0),0,0,f.laimsl))/sum(decode(nvl(r.id,0),0,0,f.laimsl))) as zaf   \n");
		sbsql.append("                  from Rigjb r,fahb f,zhilb zl,vwdianc dc,vwgongys y,jihkjb jh,yunsfsb ys   \n");
		sbsql.append("                  where r.fahb_id(+)=f.lieid and f.jihkjb_id=jh.id and f.yunsfsb_id=ys.id    \n");
		sbsql.append("                      and f.diancxxb_id=dc.id   \n");
		sbsql.append("                      and f.zhilb_id =zl.id(+)   \n");
		sbsql.append("                      and f.gongysb_id=y.id       \n").append(strCondition).append(" \n");
		sbsql.append("           and f.daohrq>=add_months(to_date('"+strDateBegin+"','yyyy-mm-dd'),-12) \n");
		sbsql.append("           and f.daohrq<=add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12)  \n");
		sbsql.append("                  group by (y.dqid,f.lieid,dc.id,dc.fgsid,f.daohrq)) dc   \n");
		sbsql.append("             group by to_char(dc.daohrq,'MM') )  wclj,--�ճɱ�   \n");
		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'ͬ��') as fenx   \n");
		sbsql.append("    from xitxxb  where rownum<=12) bq    \n");
		sbsql.append("        where bq.yuef=wclj.yuef(+)   \n");
		sbsql.append("group by (to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd'),bq.fenx)  \n");
		sbsql.append(")   \n");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","yuef", getDataField());//rs��¼����������ͼƬ��Ҫ������
		ct.intDigits=0;
//		ct.intDigits=getDigts();
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
			
			visit.setDate1(DateUtil.getFirstDayOfMonth(new Date()));
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			visit.setDropDownBean6(null);
			
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			visit.setProSelectionModel6(null);
     		this.setTreeid(null);
     		this.setBeginriqDate(visit.getMorkssj());
     		this.setEndriqDate(visit.getMorjssj()); 
     		
		}
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(new Date()),0, DateUtil.AddType_intDay));
					visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
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
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }
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
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String danx1="";
        String danx2="";
        String danx3="";
        if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
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
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(60);
 		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("��:"));
		DateField edf = new DateField();
		edf.setValue(DateUtil.FormatDate(this.getEndriqDate()));
		edf.Binding("EndriqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		edf.setWidth(60);
		tb1.addField(edf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("����:"));
		ComboBox tblx = new ComboBox();
		tblx.setTransform("LeixDropDown");
		tblx.setWidth(60);
		tb1.addField(tblx);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�ƻ��ھ�:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(80);
		tb1.addField(tbjhkj);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���䷽ʽ:"));
		ComboBox tbysfs = new ComboBox();
		tbysfs.setTransform("YunsfsDropDown");
		tbysfs.setWidth(60);
		tb1.addField(tbysfs);
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("ͼ������:"));
		ComboBox tbChart = new ComboBox();
		tbChart.setTransform("ChartDropDown");
		tbChart.setWidth(80);
		tb1.addField(tbChart);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tbok = new ToolbarButton(null,"ȷ��","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		ToolbarButton tbtj=new ToolbarButton(null,"��������","function(){ if(!win){ "+Strtmpfunction+"}"
				+ " win.show(this);	\n" 
				+ "}"); 
		tbtj.setIcon(SysConstant.Btn_Icon_Search);	
//		------------��߲�ʱ�������ϼ���ť����--------
		String strDiancid=getTreeid();
		long diancxxb_id=visit.getDiancxxb_id();
		String diancxxbid=""+diancxxb_id;
		if(getLeixDropDownValue().getValue().equals("�ֳ�")){
			if(!strDiancid.equals(diancxxbid)){
				ToolbarButton tbfh = new ToolbarButton(null,"�����ϼ�","function(){document.getElementById('ReturnButton').click();}");
				tbfh.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(tbfh);
			}
		}else{
			if(!"-1".equals(getGongysId())){
				ToolbarButton tbfh = new ToolbarButton(null,"�����ϼ�","function(){document.getElementById('ReturnButton').click();}");
				tbfh.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(tbfh);
			}
		}
			
		tb1.addItem(tbok);
		tb1.addItem(tbtj);

		
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

	private String treeid;

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
	
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString13();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString13(rbvalue);
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
		list.add(new IDropDownBean(1, "�ֳ�"));
		list.add(new IDropDownBean(2, "�ֿ�"));
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
			((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getGongysDropDownModel().getOption(0));
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
	
//	����
	public IDropDownBean getChartDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean) getChartDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setChartDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setChartDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getChartDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getChartDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getChartDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "����˰��ú����"));
		list.add(new IDropDownBean(2, "�����ۺϼ�"));
		list.add(new IDropDownBean(3, "ú��"));
		list.add(new IDropDownBean(4, "�˷�"));
		list.add(new IDropDownBean(5, "��˰��ú����"));
		list.add(new IDropDownBean(6, "�볧����"));
		list.add(new IDropDownBean(7, "�ɱ���"));
		list.add(new IDropDownBean(8, "����"));
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	private String getDataField(){
		String strDataField=getChartDropDownValue().getValue();

		if (strDataField.equals("��˰��ú����")){
			return "hansbmdj";
		}else if(strDataField.equals("�����ۺϼ�")){
			return "daoczhj";
		}else if(strDataField.equals("ú��")){
			return "hansdj";
		}else if(strDataField.equals("�˷�")){
			return "yunf";
		}else if(strDataField.equals("����˰��ú����")){
			return "buhsbmdj";
		}else if(strDataField.equals("�볧����")){
			return "farl_dk";
		}else if(strDataField.equals("�ɱ���")){
			return "cbsl";
		}else if(strDataField.equals("����")){
			return "laimsl";
		}
		return "buhsbmdj";
	}
	
	private int getDigts(){
		String strDataField=getChartDropDownValue().getValue();
		if (strDataField.equals("��������")){
			return 0;
		}else if(strDataField.equals("������")){
			return 0;
		}else if(strDataField.equals("��˰����")){
			return 2;
		}else if(strDataField.equals("���ӷ�")){
			return 2;
		}else if(strDataField.equals("�ҷ�ad(%)")){
			return 2;
		}
		return 2;
	}
}