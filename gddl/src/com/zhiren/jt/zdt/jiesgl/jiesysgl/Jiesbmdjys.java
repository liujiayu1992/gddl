package com.zhiren.jt.zdt.jiesgl.jiesysgl;
/* 
* ʱ�䣺2009-07-27
* ���ߣ� ll
* �޸����ݣ����Ӱ��볧���ڲ�ѯ��
 		   
*/ 
/*
 * ���ߣ�songy
 * ʱ�䣺2011-03-23 
 * �������޸������˵�������Ҫ�������ƽ�������
 */
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

public class Jiesbmdjys extends BasePage {
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
		return getJiesbmdjys();
	}
	
	public String getJiesbmdjys(){
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
			strCondition ="and js.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and js.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		
		String jsrq1="";
		
		if(getRiqSelectValue().getValue().equals("��������")){
			
			jsrq1="and js.jiesrq>=to_date('"+ strDate1 + "','yyyy-mm-dd') and js.jiesrq<=to_date('"+ strDate2+ "','yyyy-mm-dd') \n";
			
		}else{			
			jsrq1="and yf.yansjzrq>=to_date('"+ strDate1 + "','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+ strDate2+ "','yyyy-mm-dd') \n";
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
				strCondition =strCondition+ " and js.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
					"        jiesb js,jiesyfb yf,vwgongys y,vwdianc dc\n" + 
					"        where js.id=yf.diancjsmkb_id(+) and js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+

					jsrq1;
				strGroupID = " y.dqid ";
				strQLeix="��";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				diancid = "";

				strgongs = "select distinct dc.id as id,dc.fgsmc,dc.fgsxh as xuh,dc.mingc as mingc from vwdianc dc";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.id";
			}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				strCondition =strCondition+ " and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid()+")";
				strgongs = "select distinct dc.id,mingc,dc.xuh from vwdianc dc where dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID = "dc.id";
			}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strgongs = "select distinct dc.id,mingc,dc.xuh from vwdianc dc where dc.id="+this.getTreeid();//ȡ�÷ֹ�˾�µ����е糧
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//ȡ��ĳ���ڶεĹ�����λ		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
				"        jiesb js,jiesyfb yf,vwgongys y,vwdianc dc\n" + 
				"        where js.id=yf.diancjsmkb_id(+) and js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+

				jsrq1;
				strGroupID = "y.dqid";
				strQLeix="��";
			}
		}
		
		JDBCcon cn = new JDBCcon();
		String danwmc=getTreeDiancmc(this.getTreeid());
		
		StringBuffer sbsql = new StringBuffer();
		
//		---------------------------------------------------------------------------------------------------------------------------
		sbsql.append("select danwmc,jiessl,round_new(farl_dk/1000*4.1816,2) as far,farl_dk, \n");
		sbsql.append("   hanszhj as daoczhj,hansdj,yunf,zaf, \n");
		sbsql.append("   round_new (hanszhj*7000/farl_dk,2) as hansbmdj, \n");
		sbsql.append("   round_new ((hanszhj-meis-yunfs)*7000/farl_dk,2) as buhsbmdj \n");
		sbsql.append("from( \n");
		if ((strleix.equals("�ֳ�") && jib==3)){
			sbsql.append(" select getLinkMingxTaiz('"+this.getTreeid()+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc,");
		}else if ((strleix.equals("�ֿ�") && (jib==2 || jib ==1)  &&  !"-1".equals(getGongysId()))){
			sbsql.append(" select getLinkMingxTaiz(decode(grouping(dc.mingc),1,-1,max(dc.id)) ,'"+getGongysId()+"',decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc,");
		}else{
			sbsql.append("select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)), decode(grouping(dc.mingc),1,'�ܼ�',dc.mingc)) as danwmc, \n");
		}
		sbsql.append("     sum(nvl(sj.jiessl,0)) as jiessl,    \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(farl*jiessl)/sum(jiessl),0)) farl_dk, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk+yunf+zaf)/sum(jiessl),2)) hanszhj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk)/sum(jiessl),2)) hansdj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunf)/sum(jiessl),2)) yunf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(zaf)/sum(jiessl),2)) zaf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(meis)/sum(jiessl),2)) meis, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunfs)/sum(jiessl),2)) yunfs \n");
		sbsql.append("  from     \n");
		sbsql.append("      ( select  "+strGroupID+" as id,  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbsql.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbsql.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbsql.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbsql.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbsql.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbsql.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbsql.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbsql.append("           and js.diancxxb_id=dc.id  \n");
		sbsql.append("           and js.gongysb_id=y.id    ").append(strCondition).append(" \n");

		sbsql.append(jsrq1+" \n");
		sbsql.append("        ) sj,   \n");
		sbsql.append("       ( "+strgongs+" ) dc   \n");
		sbsql.append("  where sj.id(+)=dc.id    \n");
		sbsql.append("  group by rollup(dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc  \n");
		sbsql.append(") \n");
//		---------------------------------------------------------------------------------------------------------------------------		
		String ArrHeader[][]=new String[2][10];
		ArrHeader[0]=new String[] {"��λ","��������","��������","��������","�����ۺϼ�","ú��","�˷�","�����ӷ�","�����ú����<br>��˰","�����ú����<br>����˰"};
		ArrHeader[1]=new String[] {"��λ","��","MJ/kg","Kcal/kg","Ԫ/��"," Ԫ/�� ","Ԫ/��"," Ԫ/�� ","Ԫ/��"," Ԫ/�� "};

		int ArrWidth[]=new int[] {110,75,60,60,70,60,60,75,85,85};
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
			strCondition ="and js.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and js.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		String jsrq1="";
		
		if(getRiqSelectValue().getValue().equals("��������")){
			
			jsrq1="and js.jiesrq>=to_date('"+ strDate1 + "','yyyy-mm-dd') and js.jiesrq<=to_date('"+ strDate2+ "','yyyy-mm-dd') \n";
			
		}else{			
			jsrq1="and yf.yansjzrq>=to_date('"+ strDate1 + "','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+ strDate2+ "','yyyy-mm-dd') \n";
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
				strCondition =strCondition+ " and js.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
					"        jiesb js,jiesyfb yf,vwgongys y,vwdianc dc\n" + 
					"        where js.id=yf.diancjsmkb_id(+) and js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+

					jsrq1;
				strGroupID = " y.dqid ";
				strQLeix="��";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				diancid = "";
//				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc,dc.fgsxh as xuh from vwdianc dc";//ȡ�ü����µ����зֹ�˾
//				strGroupID = "dc.fgsid";
				strgongs = "select distinct dc.id as id,dc.fgsmc,dc.fgsxh as xuh,dc.mingc as mingc from vwdianc dc";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.id";
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
				"        jiesb js,jiesyfb yf,vwgongys y,vwdianc dc\n" + 
				"        where js.id=yf.diancjsmkb_id(+) and js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+

				jsrq1;
				strGroupID = "y.dqid";
				strQLeix="��";
			}
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();

		sbsql.append(" select dc.mingc as danwmc,  \n");
		sbsql.append("     sum(nvl(sj.jiessl,0)) as jiessl \n");
		sbsql.append("     from  (select "+strGroupID+" as id,  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbsql.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbsql.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbsql.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbsql.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbsql.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbsql.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbsql.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbsql.append("           and js.diancxxb_id=dc.id  \n");
		sbsql.append("           and js.gongysb_id=y.id     ").append(strCondition).append(" \n");

		sbsql.append(jsrq1);
		sbsql.append("      )  sj,   \n");
		sbsql.append("     (  "+strgongs+" ) dc   \n");
		sbsql.append("where sj.id(+)=dc.id    \n");
		sbsql.append("  group by (dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc \n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		
		DefaultPieDataset dataset = cd.getRsDataPie(rs, "danwmc", "jiessl", true);
		
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
			strCondition ="and js.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and js.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		String jsrq1="";
		
		if(getRiqSelectValue().getValue().equals("��������")){
			
			jsrq1="and js.jiesrq>=to_date('"+ strDate1 + "','yyyy-mm-dd') and js.jiesrq<=to_date('"+ strDate2+ "','yyyy-mm-dd') \n";
			
		}else{			
			jsrq1="and yf.yansjzrq>=to_date('"+ strDate1 + "','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+ strDate2+ "','yyyy-mm-dd') \n";
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
				strCondition =strCondition+ " and js.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct y.dqmc as mingc,y.dqid as id,y.dqxh as xuh from\n" +
					"        jiesb js,jiesyfb yf,vwgongys y,vwdianc dc\n" + 
					"        where js.id=yf.diancjsmkb_id(+) and js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+

					jsrq1;
				strGroupID = " y.dqid ";
				strQLeix="��";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc,xuh from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//ѡ����ʱˢ�³����еĵ糧
				diancid = "";
				strgongs = "select distinct dc.id as id,dc.fgsmc,dc.fgsxh as xuh,dc.mingc as mingc from vwdianc dc";//ȡ�ü����µ����зֹ�˾
				strGroupID = "dc.id";
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
				"        jiesb js,jiesyfb yf,vwgongys y,vwdianc dc\n" + 
				"        where js.id=yf.diancjsmkb_id(+) and js.gongysb_id=y.id and dc.id=js.diancxxb_id \n" + strCondition+
				jsrq1;
				
				strGroupID = "y.dqid";
				strQLeix="��";
			}
		}
		
    	JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		

		if(getRbvalue().equals("tiaoj1")||this.getRbvalue().equals("")){
			if(getRiqSelectValue().getValue().equals("��������")){
				date1=" and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') and js.jiesrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
				date2=" and js.jiesrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-12) and js.jiesrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12) \n";
			}else{
				date1=" and yf.yansjzrq>=to_date('"+strDate1+"','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
				date2=" and yf.yansjzrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-12) and yf.yansjzrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12) \n";
			}
		}else if(getRbvalue().equals("tiaoj2")){
			if(getRiqSelectValue().getValue().equals("��������")){
				date1=" and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') and js.jiesrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
				date2=" and js.jiesrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-1) and js.jiesrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1) \n";
			}else{
				date1=" and yf.yansjzrq>=to_date('"+strDate1+"','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
				date2=" and yf.yansjzrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-1) and yf.yansjzrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-1) \n";
			}
		}else{
			if(getRiqSelectValue().getValue().equals("��������")){
				date1=" and js.jiesrq>=to_date('"+strDate1+"','yyyy-mm-dd') and js.jiesrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
				date2=" and js.jiesrq>=to_date('"+strDate3+"','yyyy-mm-dd') and js.jiesrq<=to_date('"+strDate4+"','yyyy-mm-dd') \n";
			}else{
				date1=" and yf.yansjzrq>=to_date('"+strDate1+"','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n";
				date2=" and yf.yansjzrq>=to_date('"+strDate3+"','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+strDate4+"','yyyy-mm-dd') \n";
			}
		}
		sbsql.append("select danwmc,fenx,jiessl,round_new(farl_dk/1000*4.1816,2) as far,farl_dk, \n");
		sbsql.append("   hanszhj as daoczhj,hansdj,yunf,zaf, \n");
		sbsql.append("   round_new (hanszhj*7000/farl_dk,2) as hansbmdj, \n");
		sbsql.append("   round_new ((hanszhj-meis-yunfs)*7000/farl_dk,2) as buhsbmdj \n");
		sbsql.append("from(select dc.mingc as danwmc, decode(1,1,'����') as fenx,   \n");
		sbsql.append("     sum(nvl(sj.jiessl,0)) as jiessl,    \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(farl*jiessl)/sum(jiessl),0)) farl_dk, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk+yunf+zaf)/sum(jiessl),2)) hanszhj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk)/sum(jiessl),2)) hansdj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunf)/sum(jiessl),2)) yunf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(zaf)/sum(jiessl),2)) zaf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(meis)/sum(jiessl),2)) meis, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunfs)/sum(jiessl),2)) yunfs \n");
		sbsql.append("from ( select "+strGroupID+" as id,  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbsql.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbsql.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbsql.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbsql.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbsql.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbsql.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbsql.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbsql.append("           and js.diancxxb_id=dc.id  \n");
		sbsql.append("           and js.gongysb_id=y.id   \n").append(strCondition).append(" \n");
		sbsql.append(date1);
		sbsql.append("       )  sj,   \n");
		sbsql.append("       ( "+strgongs+" ) dc   \n");
		sbsql.append("  where sj.id(+)=dc.id    \n");
		sbsql.append("  group by (dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc  \n");
		sbsql.append(") \n");
		sbsql.append("union \n");
		sbsql.append("select danwmc,fenx,jiessl,round_new(farl_dk/1000*4.1816,2) as far,farl_dk, \n");
		sbsql.append("   hanszhj as daoczhj,hansdj,yunf,zaf, \n");
		sbsql.append("   round_new (hanszhj*7000/farl_dk,2) as hansbmdj, \n");
		sbsql.append("   round_new ((hanszhj-meis-yunfs)*7000/farl_dk,2) as buhsbmdj \n");
		sbsql.append("from(select dc.mingc as danwmc, decode(1,1,'ͬ��') as fenx,   \n");
		sbsql.append("     sum(nvl(sj.jiessl,0)) as jiessl,    \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(farl*jiessl)/sum(jiessl),0)) farl_dk, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk+yunf+zaf)/sum(jiessl),2)) hanszhj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk)/sum(jiessl),2)) hansdj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunf)/sum(jiessl),2)) yunf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(zaf)/sum(jiessl),2)) zaf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(meis)/sum(jiessl),2)) meis, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunfs)/sum(jiessl),2)) yunfs \n");
		sbsql.append("from ( select "+strGroupID+" as id,  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbsql.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbsql.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbsql.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbsql.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbsql.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbsql.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbsql.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbsql.append("           and js.diancxxb_id=dc.id  \n");
		sbsql.append("           and js.gongysb_id=y.id    \n").append(strCondition).append(" \n");
		sbsql.append( date2);
		sbsql.append("       )  sj,   \n");
		sbsql.append("       ( "+strgongs+" ) dc   \n");
		sbsql.append("  where sj.id(+)=dc.id    \n");
		sbsql.append("  group by (dc.mingc) order by grouping(dc.mingc),max(dc.xuh),dc.mingc  \n");
		sbsql.append(") \n");
		
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
		Date dat1=getBeginriqDate();//����
		String strleix=getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//�����ַ�
		
		Date dat2=getEndriqDate();//����
		String strDate2=DateUtil.FormatDate(dat2);//�����ַ�
		
		String strDateBegin=DateUtil.FormatDate(DateUtil.getFirstDayOfYear(getBeginriqDate()));//�����ַ�
		String strDateEnd=DateUtil.FormatDate(DateUtil.getLastDayOfYear(getBeginriqDate()));//�����ַ�
		
		int jib=this.getDiancTreeJib();
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and js.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//ѡ�����䷽ʽ
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and js.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		String jsrq="";
		String jsrq1="";
		String jsrq2="";
		
		if(getRiqSelectValue().getValue().equals("��������")){
			jsrq="js.jiesrq";
			jsrq1="and js.jiesrq>=to_date('"+ strDateBegin + "','yyyy-mm-dd') and js.jiesrq<=to_date('"+ strDateEnd+ "','yyyy-mm-dd') \n";
			jsrq2="and js.jiesrq>=add_months(to_date('"+strDateBegin+"','yyyy-mm-dd'),-12) and js.jiesrq<=add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12)";
			
		}else{			
			jsrq="yf.yansjzrq";
			jsrq1="and yf.yansjzrq>=to_date('"+ strDateBegin + "','yyyy-mm-dd') and yf.yansjzrq<=to_date('"+ strDateEnd+ "','yyyy-mm-dd') \n";
			jsrq2="and yf.yansjzrq>=add_months(to_date('"+strDateBegin+"','yyyy-mm-dd'),-12) and yf.yansjzrq<=add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),-12)";
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

		sbsql.append("select yuef,fenx,jiessl,round_new(farl_dk/1000*4.1816,2) as far,farl_dk, \n");
		sbsql.append("   hanszhj as daoczhj,hansdj,yunf,zaf, \n");
		sbsql.append("   round_new (hanszhj*7000/farl_dk,2) as hansbmdj, \n");
		sbsql.append("   round_new ((hanszhj-meis-yunfs)*7000/farl_dk,2) as buhsbmdj \n");
		sbsql.append("from(select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx as fenx,   \n");
		sbsql.append("     sum(nvl(sj.jiessl,0)) as jiessl,    \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(farl*jiessl)/sum(jiessl),0)) farl_dk, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk+yunf+zaf)/sum(jiessl),2)) hanszhj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk)/sum(jiessl),2)) hansdj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunf)/sum(jiessl),2)) yunf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(zaf)/sum(jiessl),2)) zaf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(meis)/sum(jiessl),2)) meis, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunfs)/sum(jiessl),2)) yunfs \n");
		sbsql.append("from ( select to_char("+jsrq+",'MM') as yuef ,  \n");
		sbsql.append("  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbsql.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbsql.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbsql.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbsql.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbsql.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbsql.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbsql.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbsql.append("           and js.diancxxb_id=dc.id  \n");
		sbsql.append("           and js.gongysb_id=y.id     \n").append(strCondition);

		sbsql.append(jsrq1);
		sbsql.append("         )  sj,   \n");
		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'����') as fenx   \n");
		sbsql.append("    from xitxxb  where rownum<=12) bq    \n");
		sbsql.append("        where bq.yuef=sj.yuef(+)   \n");
		sbsql.append("group by (to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd'),bq.fenx)  \n");
		sbsql.append(") \n");
		sbsql.append("       union     \n");
		sbsql.append("select yuef,fenx,jiessl,round_new(farl_dk/1000*4.1816,2) as far,farl_dk, \n");
		sbsql.append("   hanszhj as daoczhj,hansdj,yunf,zaf, \n");
		sbsql.append("   round_new (hanszhj*7000/farl_dk,2) as hansbmdj, \n");
		sbsql.append("   round_new ((hanszhj-meis-yunfs)*7000/farl_dk,2) as buhsbmdj \n");
		sbsql.append("from(select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx as fenx,   \n");
		sbsql.append("     sum(nvl(sj.jiessl,0)) as jiessl,    \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(farl*jiessl)/sum(jiessl),0)) farl_dk, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk+yunf+zaf)/sum(jiessl),2)) hanszhj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk)/sum(jiessl),2)) hansdj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunf)/sum(jiessl),2)) yunf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(zaf)/sum(jiessl),2)) zaf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(meis)/sum(jiessl),2)) meis, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunfs)/sum(jiessl),2)) yunfs \n");
		sbsql.append("from ( select to_char("+jsrq+",'MM') as yuef ,  \n");
		sbsql.append("  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbsql.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbsql.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbsql.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbsql.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbsql.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbsql.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbsql.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbsql.append("           and js.diancxxb_id=dc.id  \n");
		sbsql.append("           and js.gongysb_id=y.id     \n").append(strCondition);
		sbsql.append(jsrq2);
		sbsql.append("         )  sj,   \n");
		sbsql.append("       (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'ͬ��') as fenx   \n");
		sbsql.append("    from xitxxb  where rownum<=12) bq    \n");
		sbsql.append("        where bq.yuef=sj.yuef(+)   \n");
		sbsql.append("group by (to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd'),bq.fenx)  \n");
		sbsql.append(") \n");

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
			visit.setDropDownBean7(null);
			
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			visit.setProSelectionModel6(null);
			visit.setProSelectionModel7(null);
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
					visit.setDropDownBean6(null);
					visit.setDropDownBean7(null);
					
					visit.setProSelectionModel1(null);
					visit.setProSelectionModel2(null);
					visit.setProSelectionModel3(null);
					visit.setProSelectionModel4(null);
					visit.setProSelectionModel5(null);
					visit.setProSelectionModel6(null);
					visit.setProSelectionModel7(null);
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
		if(RiqSelectChange == false){
			RiqSelectChange = false;
			getJiesbmdjys();
			getChartPie3D();
			getChartBar();
			getChartLine();
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
		
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
//		
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
//		
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);
		
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
		
//		tb1.addText(new ToolbarText("��������:"));
//		ComboBox rq = new ComboBox();
//		rq.setTransform("RiqSelect");
//		rq.setWidth(80);
//		tb1.addField(rq);
//		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("ͼ������:"));
		ComboBox tbChart = new ComboBox();
		tbChart.setTransform("ChartDropDown");
		tbChart.setWidth(90);
		tb1.addField(tbChart);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("��λ:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
//		tb1.addText(new ToolbarText("-"));
		ToolbarButton tbok = new ToolbarButton(null,"ȷ��","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
//		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
//		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
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
//		tb1.addItem(tb);
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
		sql = "select d.id,d.mingc from diancxxb d order by d.xuh  ";
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
		String sql = "select id,mingc\n" + "from jihkjb order by mingc \n";
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
		String sql = "select id,mingc\n" + "from yunsfsb order by mingc  \n";
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
		String sql = "select id,mingc\n" + "from gongysb order by mingc \n";
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
		list.add(new IDropDownBean(6, "��������"));
		list.add(new IDropDownBean(7, "��������"));
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
		}else if(strDataField.equals("��������")){
			return "farl_dk";
		}else if(strDataField.equals("��������")){
			return "jiessl";
		}
		return "biaomdj";
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
	 //��������
	private boolean RiqSelectChange = false;
    public IDropDownBean getRiqSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean7()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean)getRiqSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean7();
    }
    public void setRiqSelectValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean7(Value);

    }
    public void setRiqSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getRiqSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
            getRiqSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }
    public void getRiqSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(0,"��������"));
        list.add(new IDropDownBean(1,"�볧����"));
        ((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(list)) ;
        return ;
    }
}