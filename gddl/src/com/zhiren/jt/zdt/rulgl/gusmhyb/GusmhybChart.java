package com.zhiren.jt.zdt.rulgl.gusmhyb;

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
import com.zhiren.common.MainGlobal;
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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class GusmhybChart extends BasePage {
    private static final String DATA_FADBMH="�����ú��";
    private static final String DATA_GONGDBMH="�����ú��";
    private static final String DATA_GONGRBMH="���ȱ�ú��";
    
    private static final String CHART_LINE="����ͼ";
    private static final String CHART_BAR="����λ�Ƚ�ͼ";
    
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
    private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private void setReturnValue(){
		String strDiancid=getTreeid();
		String strOldId="";
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
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		isBegin = false;
		return getRigsmhImg();
	}
	
	public String getRigsmhImg(){
		Report rt=new Report();
		rt.setCenter(false);
		rt.setBody(new Table(2,1));
		
		if (GusmhybChart.CHART_LINE.equals(getChartTypeDropDownValue().getValue())){
			rt.body.setCellValue(2,1,getChartLine());
		}else{
			rt.body.setCellValue(2,1,getChartBar());
		}
		
		rt.body.setCellBorderNone(1, 1);
		rt.body.setCellBorderNone(2, 1);
		rt.body.setBorderNone();
		rt.body.setFontSize(12);
		return rt.getHtml();
	}
	
	private Date getBeginriqDate(){
		return DateUtil.getDate(getNianfValue().getValue()+"-01-01");
	}
	
	private Date getEndriqDate(){
		return DateUtil.getLastDayOfMonth(getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01");
	}
	
	private String getTitle(){
		String title=getTreeDiancmc(this.getTreeid());//getDiancmc();
		title=title+getChartDropDownValue().getValue();
		title=title+getChartTypeDropDownValue().getValue()+"\n";
		title=title+DateUtil.Formatdate("yyyy��MM��",getEndriqDate());
		return title;
	}
	
	public String getReportHrefYuemhgsb(){
		String strParam="";
		strParam="gs&dc="+getTreeid()+"&datStart="+DateUtil.FormatDate(getBeginriqDate())+"&datEnd="+DateUtil.FormatDate(getEndriqDate());
		return "<a href=\"#\" onclick=printReport(\""+strParam+"\") ><b>��ú�Ĺ����</b>";
	}
	
	public String getReportHrefShangbmhyb(){
		String strParam="";
		strParam="sb&dc="+getTreeid()+"&datStart="+DateUtil.FormatDate(getBeginriqDate())+"&datEnd="+DateUtil.FormatDate(getEndriqDate());
		return "<a href=\"#\" onclick=printReport(\""+strParam+"\") ><b>�ϱ�ú���±�</b>";
	}

	public String getReportHrefMeihdb(){
		String strParam="";
		strParam="db&dc="+getTreeid()+"&datStart="+DateUtil.FormatDate(getBeginriqDate())+"&datEnd="+DateUtil.FormatDate(getEndriqDate());
		return "<a href=\"#\" onclick=printReport(\""+strParam+"\") ><b>ú�ĶԱ�</b>";
	}
	 
//	����ͼ
	public String getChartLine(){
		String strCondition ="";
		String strDateStart=DateUtil.FormatDate(getBeginriqDate());//��ʼ����
		String strDateEnd=DateUtil.FormatDate(getEndriqDate());//��ֹ����
		
		String strDateStartP=DateUtil.FormatDate(DateUtil.AddDate(getBeginriqDate(),-1,DateUtil.AddType_intYear));//��ʼ����ͬ��
		String strDateEndP=DateUtil.FormatDate(DateUtil.AddDate(getEndriqDate(),-1,DateUtil.AddType_intYear));//��ʼ����ͬ��
		
		int jib=this.getDiancTreeJib();
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
		
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strCondition =strCondition+ " where (dc.fuid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid() + ")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " where dc.id="+this.getTreeid();
		}else if (jib==-1){
			strCondition =strCondition+ " where dc.id="+this.getTreeid();
		}
		
		StringBuffer sqlzhi_by = new StringBuffer();//��Ȩ������
		StringBuffer sqlzhi_lj = new StringBuffer();//��Ȩ���ۼ�
		
//		����
		sqlzhi_by.append("(select hy.diancxxb_id,to_char(hy.rulrq,'yyyy-MM') riq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
		sqlzhi_by.append("  from meihyb hy \n");
		sqlzhi_by.append(" where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
		sqlzhi_by.append(" and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
		sqlzhi_by.append("  group by hy.diancxxb_id,to_char(hy.rulrq,'yyyy-MM')) hy, \n");
		sqlzhi_by.append(" (select rz.diancxxb_id,to_char(rz.rulrq,'yyyy-MM') riq,sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)) as meil,\n");
		sqlzhi_by.append(" decode(sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)),0,0,sum((rz.meil)*round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil))) as rulrl \n");
		sqlzhi_by.append(" from rulmzlb rz \n");
		sqlzhi_by.append(" where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
		sqlzhi_by.append(" and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
		sqlzhi_by.append(" group by rz.diancxxb_id,to_char(rz.rulrq,'yyyy-MM')) rz, \n");
		
		//�ۼ�
		sqlzhi_lj.append("(select hy.diancxxb_id,to_char(hy.rulrq,'yyyy-MM') as riq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
		sqlzhi_lj.append("  from meihyb hy \n");
		sqlzhi_lj.append(" where hy.rulrq>=to_date('"+strDateStartP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append(" and hy.rulrq<=to_date('"+strDateEndP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append("  group by hy.diancxxb_id,to_char(hy.rulrq,'yyyy-MM')) hy, \n");
		sqlzhi_lj.append(" (select rz.diancxxb_id,to_char(rz.rulrq,'yyyy-MM') as riq,sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)) as meil,\n");
		sqlzhi_lj.append(" decode(sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)),0,0,sum((rz.meil)*round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil))) as rulrl \n");
		sqlzhi_lj.append(" from rulmzlb rz \n");
		sqlzhi_lj.append(" where rz.rulrq>=to_date('"+strDateStartP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append(" and rz.rulrq<=to_date('"+strDateEndP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append(" group by rz.diancxxb_id,to_char(rz.rulrq,'yyyy-MM')) rz, \n");
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select riq, decode(1,1,'����','����') as fenx,fady,gongryy,fardl,gongdl,gongrl,rulrl,rulrly, \n");
		sbsql.append("    round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,   \n");
		sbsql.append("    round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,   \n");
		sbsql.append("    round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh  \n");
		sbsql.append(" from  \n");
		sbsql.append("(select  to_date(a.riq||'-01','yyyy-mm-dd') as riq,sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,  \n");
		sbsql.append("     decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl,  \n");
		sbsql.append("     sum(sc.gongdl) as gongdl,sum(sc.fadl) as fardl,sum(sc.gongrl) as gongrl,  \n");
		sbsql.append("     sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,  \n");
		sbsql.append("     decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly  \n");
		sbsql.append("  from --������¯����  \n");
		//����
//		sbsql.append(" 		 (select hy.diancxxb_id,to_char(hy.rulrq,'yyyy-MM') riq, sum(fadhy) as fady,sum(gongrhy) as gongry , \n");
//		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
//		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
//		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
//		sbsql.append("              from meihyb hy,rulmzlb zl  \n");
//		sbsql.append("        where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')   \n");
//		sbsql.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
//		sbsql.append("                   and hy.rulmzlb_id=zl.id(+)  \n");
//		sbsql.append("             group by hy.diancxxb_id, to_char(hy.rulrq,'yyyy-MM') ) hy,  \n");
		sbsql.append(sqlzhi_by.toString());
		sbsql.append("       (select diancxxb_id,to_char(riq,'yyyy-MM') riq,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl  \n");
		sbsql.append("               from  shouhcrbyb  yrb  \n");
		sbsql.append("                where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
		sbsql.append("              and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
		sbsql.append("              group by diancxxb_id,to_char(riq,'yyyy-MM') ) yb,   \n");
		sbsql.append("       (select sc.diancxxb_id,to_char(riq,'yyyy-MM') riq,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������  \n");
		sbsql.append("             from riscsjb sc  \n");
		sbsql.append("             where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')   \n");
		sbsql.append("             and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
		sbsql.append("             group by  sc.diancxxb_id,to_char(riq,'yyyy-MM')) sc,  \n");
		sbsql.append("       (select dc.id, dc.mingc,dc.xuh,dc.fgsmc,dc.fgsxh,dc.rlgsmc,dc.rlgsxh, riq--���е����ڻ�糧  \n");
		sbsql.append("             from vwdianc dc ,  \n");
		sbsql.append("             (select to_char(add_months(to_date('"+strDateEnd+"','yyyy-mm-dd'),1-rownum),'yyyy-MM') as riq   \n");
		sbsql.append("                   from all_objects   \n");
		sbsql.append("                  where rownum<="+getYuefValue().getValue()+")  "+strCondition+") a  \n");
		sbsql.append("      where a.riq=hy.riq(+)  and a.id=hy.diancxxb_id(+) and a.riq=rz.riq(+)  and a.id=rz.diancxxb_id(+) \n");
		sbsql.append("           and a.riq=sc.riq(+) and a.id=sc.diancxxb_id(+)  \n");
		sbsql.append("           and a.riq=yb.riq(+)  and a.id=yb.diancxxb_id(+) \n");
		sbsql.append("      group by a.riq  order by a.riq) \n");
		sbsql.append("union \n");
		sbsql.append("select add_months( riq,12) as riq, decode(1,1,'ͬ��','ͬ��') as fenx,fady,gongryy,fardl,gongdl,gongrl,rulrl,rulrly, \n");
		sbsql.append("    round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,   \n");
		sbsql.append("    round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,   \n");
		sbsql.append("    round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh  \n");
		sbsql.append(" from  \n");
		sbsql.append("(select to_date(a.riq||'-01','yyyy-mm-dd') as riq,sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry,  \n");
		sbsql.append("     decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl,  \n");
		sbsql.append("     sum(sc.gongdl) as gongdl,sum(sc.fadl) as fardl,sum(sc.gongrl) as gongrl,  \n");
		sbsql.append("     sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy,  \n");
		sbsql.append("     decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly  \n");
		sbsql.append("  from --������¯����  \n");
		//�ۼ�
//		sbsql.append(" 		 (select hy.diancxxb_id,to_char(hy.rulrq,'yyyy-MM') riq, sum(fadhy) as fady,sum(gongrhy) as gongry ,\n");
//		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl,  \n");
//		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0,  \n");
//		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl  \n");
//		sbsql.append("              from meihyb hy,rulmzlb zl  \n");
//		sbsql.append("        where hy.rulrq>=to_date('"+strDateStartP+"','yyyy-mm-dd')   \n");
//		sbsql.append("              and hy.rulrq<=to_date('"+strDateEndP+"','yyyy-mm-dd')  \n");
//		sbsql.append("                   and hy.rulmzlb_id=zl.id(+)  \n");
//		sbsql.append("             group by hy.diancxxb_id, to_char(hy.rulrq,'yyyy-MM') ) hy,  \n");
		sbsql.append(sqlzhi_lj.toString());
		sbsql.append("       (select diancxxb_id,to_char(riq,'yyyy-MM') riq,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl  \n");
		sbsql.append("               from  shouhcrbyb  yrb  \n");
		sbsql.append("                where yrb.riq>=to_date('"+strDateStartP+"','yyyy-mm-dd')    \n");
		sbsql.append("              and yrb.riq<=to_date('"+strDateEndP+"','yyyy-mm-dd')   \n");
		sbsql.append("              group by diancxxb_id,to_char(riq,'yyyy-MM') ) yb,   \n");
		sbsql.append("       (select sc.diancxxb_id,to_char(riq,'yyyy-MM') riq,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������  \n");
		sbsql.append("             from riscsjb sc  \n");
		sbsql.append("             where sc.riq>=to_date('"+strDateStartP+"','yyyy-mm-dd')   \n");
		sbsql.append("             and sc.riq<=to_date('"+strDateEndP+"','yyyy-mm-dd')  \n");
		sbsql.append("             group by  sc.diancxxb_id,to_char(riq,'yyyy-MM')) sc,  \n");
		sbsql.append("       (select dc.id, dc.mingc,dc.xuh,dc.fgsmc,dc.fgsxh,dc.rlgsmc,dc.rlgsxh, riq--���е����ڻ�糧  \n");
		sbsql.append("             from vwdianc dc  ,  \n");
		sbsql.append("             (select to_char(add_months(to_date('"+strDateEndP+"','yyyy-mm-dd'),1-rownum),'yyyy-MM') as riq   \n");
		sbsql.append("                   from all_objects   \n");
		sbsql.append("                  where rownum<="+getYuefValue().getValue()+") "+strCondition+") a  \n");
		sbsql.append("      where a.riq=hy.riq(+)  and a.id=hy.diancxxb_id(+) and a.riq=rz.riq(+)  and a.id=rz.diancxxb_id(+)  \n");
		sbsql.append("           and a.riq=sc.riq(+) and a.id=sc.diancxxb_id(+)  \n");
		sbsql.append("           and a.riq=yb.riq(+)  and a.id=yb.diancxxb_id(+) \n");
		sbsql.append("      group by a.riq  order by a.riq) \n");
		
		JDBCcon cn = new JDBCcon();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","riq",getDataField());//rs��¼����������ͼƬ��Ҫ������
		ct.intDigits=0;
		ct.lineDateFormatOverride="yyyy-MM";
		ct.setDateUnit(Chart.DATEUNIT_MONTH , 1);
		ct.chartBackgroundPaint=gp;
		
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//��б��ʾX�������
		ct.showLegend = true;
		ct.setID("imgChartLine");
		cn.Close();
		return ct.ChartTimeGraph(getPage(),data2, getTitle(), 800, 400);
	}
	

//	����ͼ
	public String getChartBar(){
		String strCondition ="";
		String strDateStart=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(getEndriqDate()));//��ʼ����
		String strDateEnd=DateUtil.FormatDate(getEndriqDate());//��ֹ����
		
		String strDateStartP=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(getEndriqDate()),-1,DateUtil.AddType_intYear));//��ʼ����ͬ��
		String strDateEndP=DateUtil.FormatDate(DateUtil.AddDate(getEndriqDate(),-1,DateUtil.AddType_intYear));//��ʼ����ͬ��
		
		String selectFiedld="";
		String GroupField="";
		int jib=this.getDiancTreeJib();
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			selectFiedld="a.fgsmc as mingc ,max(a.fgsxh) as xuh ,";
			GroupField=" a.fgsmc  order by max(a.fgsxh)";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			selectFiedld="a.mingc as mingc ,max(a.xuh) as xuh ,";
			GroupField=" a.mingc  order by max(a.xuh)";
			
			strCondition =strCondition+ " where (dc.fgsid="+this.getTreeid()+" or dc.rlgsid="+this.getTreeid() + ")";
			
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strCondition =strCondition+ " where dc.id="+this.getTreeid();
			selectFiedld="a.mingc as mingc ,max(a.xuh) as xuh ,";
			GroupField=" a.mingc  order by max(a.xuh)";
			
		}else if (jib==-1){
			strCondition =strCondition+ " where dc.id="+this.getTreeid();
			selectFiedld="a.mingc as mingc ,max(a.xuh) as xuh ,";
			GroupField="= a.mingc  order by max(a.xuh)";
			
		}
		
		StringBuffer sqlzhi_by = new StringBuffer();//��Ȩ������
		StringBuffer sqlzhi_lj = new StringBuffer();//��Ȩ���ۼ�
		
		sqlzhi_by.append("(select hy.diancxxb_id,hy.rulrq as riq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
		sqlzhi_by.append("  from meihyb hy \n");
		sqlzhi_by.append(" where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
		sqlzhi_by.append(" and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
		sqlzhi_by.append("  group by hy.diancxxb_id,hy.rulrq) hy, \n");
		sqlzhi_by.append(" (select rz.diancxxb_id,rz.rulrq as riq,sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)) as meil,\n");
		sqlzhi_by.append(" decode(sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)),0,0,sum((rz.meil)*round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil))) as rulrl \n");
		sqlzhi_by.append(" from rulmzlb rz \n");
		sqlzhi_by.append(" where rz.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd') \n");
		sqlzhi_by.append(" and rz.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd') \n");
		sqlzhi_by.append(" group by rz.diancxxb_id,rz.rulrq) rz, \n");
		
		sqlzhi_lj.append("(select hy.diancxxb_id,hy.rulrq as riq,sum(hy.fadhy) as fady,sum(hy.gongrhy) as gongrhy \n");
		sqlzhi_lj.append("  from meihyb hy \n");
		sqlzhi_lj.append(" where hy.rulrq>=to_date('"+strDateStartP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append(" and hy.rulrq<=to_date('"+strDateEndP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append("  group by hy.diancxxb_id,hy.rulrq) hy, \n");
		sqlzhi_lj.append(" (select rz.diancxxb_id,rz.rulrq as riq,sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)) as meil,\n");
		sqlzhi_lj.append(" decode(sum(decode(nvl(rz.qnet_ar,0),0,0,rz.meil)),0,0,sum((rz.meil)*round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(round_new(nvl(rz.qnet_ar,0),"+((Visit) getPage().getVisit()).getFarldec()+"),0, 0,rz.meil))) as rulrl \n");
		sqlzhi_lj.append(" from rulmzlb rz \n");
		sqlzhi_lj.append(" where rz.rulrq>=to_date('"+strDateStartP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append(" and rz.rulrq<=to_date('"+strDateEndP+"','yyyy-mm-dd') \n");
		sqlzhi_lj.append(" group by rz.diancxxb_id,rz.rulrq) rz, \n");
		 
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select mingc,  decode(1,1,'����','����') as fenx,fady,gongryy,fardl,gongdl,gongrl,rulrl,rulrly,\n");
		sbsql.append(" 	round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,  \n");
		sbsql.append("  round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,  \n");
		sbsql.append("  round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh \n");
		sbsql.append("from \n");
		sbsql.append("(select "+selectFiedld+" sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry, \n");
		sbsql.append("       decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl, \n");
		sbsql.append("       sum(sc.gongdl) as gongdl,sum(sc.fadl) as fardl,sum(sc.gongrl) as gongrl, \n");
		sbsql.append(" 		sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy, \n");
		sbsql.append("   	decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly \n");
		sbsql.append("  from --������¯���� \n");
		
//		sbsql.append(" 		(select hy.diancxxb_id,hy.rulrq riq, sum(fadhy) as fady,sum(gongrhy) as gongry , \n");
//		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl, \n");
//		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0, \n");
//		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl \n");
//		sbsql.append("        from meihyb hy,rulmzlb zl \n");
//		sbsql.append("        where hy.rulrq>=to_date('"+strDateStart+"','yyyy-mm-dd')   \n");
//		sbsql.append("              and hy.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
//		sbsql.append("              and hy.rulmzlb_id=zl.id(+) \n");
//		sbsql.append("       group by hy.diancxxb_id,hy.rulrq) hy, \n");
		
		sbsql.append(sqlzhi_by.toString());
		
		sbsql.append(" 		(select diancxxb_id,riq,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl \n");
		sbsql.append("               from  shouhcrbyb  yrb \n");
		sbsql.append("                where yrb.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')    \n");
		sbsql.append("              and yrb.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
		sbsql.append("       group by diancxxb_id,riq) yb,  \n");
		sbsql.append("      (select sc.diancxxb_id, riq,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������ \n");
		sbsql.append("              from riscsjb sc \n");
		sbsql.append("             where sc.riq>=to_date('"+strDateStart+"','yyyy-mm-dd')   \n");
		sbsql.append("             and sc.riq<=to_date('"+strDateEnd+"','yyyy-mm-dd')  \n");
		sbsql.append("      group by  sc.diancxxb_id,sc.riq) sc, \n");
		sbsql.append("     (select dc.id, dc.mingc,dc.xuh,dc.fgsmc,dc.fgsxh,dc.rlgsmc,dc.rlgsxh, riq--���е����ڻ�糧 \n");
		sbsql.append("      from vwdianc dc  , \n");
		sbsql.append("           (select to_date('"+strDateStart+"','yyyy-mm-dd')+rownum-1 as riq  \n");
		sbsql.append("           from all_objects  \n");
		sbsql.append("           where rownum<=(to_date('"+strDateEnd+"','yyyy-mm-dd')-to_date('"+strDateStart+"','yyyy-mm-dd'))+1)"+strCondition+") a \n");
		sbsql.append(" where a.riq=hy.riq(+)     and a.id=hy.diancxxb_id(+) and a.riq=rz.riq(+)   and a.id=rz.diancxxb_id(+) \n");
		sbsql.append("       and a.riq=sc.riq(+) and a.id=sc.diancxxb_id(+) \n");
		sbsql.append("		 and a.riq=yb.riq(+)  and a.id=yb.diancxxb_id(+)");
		sbsql.append("group by "+GroupField+") \n");
		sbsql.append(" union all \n");
		sbsql.append(" select mingc, decode(1,1,'ͬ��','ͬ��') as fenx,fady,gongryy,fardl,gongdl,gongrl,rulrl,rulrly,\n");
		sbsql.append(" 		round(decode(fardl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/fardl),0) as fadbzmh,  \n");
		sbsql.append("  	round(decode(gongdl,0,0,1000000*(round(fady*rulrl/29.271,0)+round(fadyy*rulrly/29.271,0))/gongdl),0) as gongdbzmh,  \n");
		sbsql.append("  	round(decode(gongrl,0,0,1000*(round(gongry*rulrl/29.271,0)+round(gongryy*rulrly/29.271,0))/gongrl),0) as gongrbzmh \n");
		sbsql.append(" from \n");
		sbsql.append("(select "+selectFiedld+" sum(nvl(hy.fady,0)) as fady,sum(nvl(hy.gongrhy,0)) as gongry, \n");
		sbsql.append("       decode(sum(nvl(rz.meil,0)),0,0, sum(nvl(rz.rulrl*rz.meil,0))/sum(nvl(rz.meil,0))) as rulrl,  \n");
		sbsql.append("       sum(sc.gongdl) as gongdl,sum(sc.fadl) as fardl,sum(sc.gongrl) as gongrl, \n");
		sbsql.append(" 		sum(nvl(yb.fadyy,0)) as fadyy,sum(nvl(yb.gongryy,0)) as gongryy, \n");
		sbsql.append("   	decode(sum(nvl(yb.rulzl,0)),0,0, sum(nvl(yb.rulrl*yb.rulzl,0))/sum(nvl(yb.rulzl,0))) as rulrly \n");
		sbsql.append("  from --������¯���� \n");
		
//		sbsql.append("      (select hy.diancxxb_id,hy.rulrq riq, sum(fadhy) as fady,sum(gongrhy) as gongry ,\n");
//		sbsql.append("               sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)) as rulzl, \n");
//		sbsql.append("               decode(sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy)),0,0, \n");
//		sbsql.append("               sum((fadhy+gongrhy)*zl.qnet_ar)/sum(decode(nvl(zl.qnet_ar,0),0, 0,fadhy+gongrhy))) as rulrl \n");
//		sbsql.append("        from meihyb hy,rulmzlb zl \n");
//		sbsql.append("        where hy.rulrq>=to_date('"+strDateStartP+"','yyyy-mm-dd')   \n");
//		sbsql.append("              and hy.rulrq<=to_date('"+strDateEndP+"','yyyy-mm-dd')  \n");
//		sbsql.append("              and hy.rulmzlb_id=zl.id(+) \n");
//		sbsql.append("       group by hy.diancxxb_id,hy.rulrq) hy, \n");
		
		sbsql.append(sqlzhi_lj.toString());
		
		sbsql.append(" 		(select diancxxb_id,riq,sum(yrb.fady) as fadyy,sum(yrb.gongry) as gongryy,sum(yrb.fady+yrb.gongry) as rulzl,40.98 as rulrl \n");
		sbsql.append("               from  shouhcrbyb  yrb \n");
		sbsql.append("                where yrb.riq>=to_date('"+strDateStartP+"','yyyy-mm-dd')    \n");
		sbsql.append("              and yrb.riq<=to_date('"+strDateEndP+"','yyyy-mm-dd')   \n");
		sbsql.append("       group by diancxxb_id,riq) yb,  \n");
		sbsql.append("      (select sc.diancxxb_id, riq,sum(fadl) as fadl,sum(gongdl) as gongdl,sum(gongrl) as gongrl--������������ \n");
		sbsql.append("              from riscsjb sc \n");
		sbsql.append("             where sc.riq>=to_date('"+strDateStartP+"','yyyy-mm-dd')   \n");
		sbsql.append("             and sc.riq<=to_date('"+strDateEndP+"','yyyy-mm-dd')  \n");
		sbsql.append("      group by  sc.diancxxb_id,sc.riq) sc, \n");
		sbsql.append("     (select dc.id, dc.mingc,dc.xuh,dc.fgsmc,dc.fgsxh,dc.rlgsmc,dc.rlgsxh, riq--���е����ڻ�糧 \n");
		sbsql.append("      from vwdianc dc , \n");
		sbsql.append("           (select to_date('"+strDateStartP+"','yyyy-mm-dd')+rownum-1 as riq  \n");
		sbsql.append("           from all_objects  \n");
		sbsql.append("           where rownum<=(to_date('"+strDateEndP+"','yyyy-mm-dd')-to_date('"+strDateStartP+"','yyyy-mm-dd'))+1) "+strCondition+") a \n");
		sbsql.append(" where a.riq=hy.riq(+)     and a.id=hy.diancxxb_id(+) and a.riq=rz.riq(+)   and a.id=rz.diancxxb_id(+) \n");
		sbsql.append("       and a.riq=sc.riq(+) and a.id=sc.diancxxb_id(+) \n");
		sbsql.append("		 and a.riq=yb.riq(+)  and a.id=yb.diancxxb_id(+)");
		sbsql.append("group by "+GroupField+") \n");
		
		JDBCcon cn = new JDBCcon();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		CategoryDataset dataset = cd.getRsDataChart(rs, "mingc", "fenx", getDataField());
		ct.intDigits=0;				//	��ʾС��λ��
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;		//��б��ʾX�������
		ct.chartBackgroundPaint=gp;
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//��б��ʾX�������
		ct.showLegend = true;
		cn.Close();
		return ct.ChartBar3D(getPage(),dataset, getTitle(), 800, 400);
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
			
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			visit.setDropDownBean7(null);
			visit.setProSelectionModel7(null);
			this.setTreeid(null);
		}
		
		getToolBars();
		isBegin=true;
	}	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
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
	    
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
	
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("ͼ������:"));
		ComboBox tbChart = new ComboBox();
		tbChart.setTransform("ChartDropDown");
		tbChart.setWidth(90);
		tb1.addField(tbChart);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("ͼ������:"));
		ComboBox tbChartType = new ComboBox();
		tbChartType.setTransform("ChartTypeDropDown");
		tbChartType.setWidth(120);
		tb1.addField(tbChartType);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tbok = new ToolbarButton(null,"ȷ��","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tbok);
		tb1.addItem(tb);
		String diancxxbid=this.getTreeid();
		long strDiancid=visit.getDiancxxb_id();
		String Diancid=""+strDiancid;
		if(!diancxxbid.equals(Diancid)){
		ToolbarButton tbfh = new ToolbarButton(null,"�����ϼ�","function(){document.getElementById('ReturnButton').click();}");
		tbfh.setIcon(SysConstant.Btn_Icon_Return);
		tb1.addItem(tbfh);
		}
		setToolbar(tb1);
	}
	

//	���
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}
 
	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * �·�
	 */
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
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
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
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
		list.add(new IDropDownBean(1, GusmhybChart.DATA_FADBMH));
		list.add(new IDropDownBean(2, GusmhybChart.DATA_GONGDBMH));
		list.add(new IDropDownBean(3, GusmhybChart.DATA_GONGRBMH));
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}

	//ͼ������
	public IDropDownBean getChartTypeDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean) getChartTypeDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setChartTypeDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setChartTypeDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getChartTypeDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getChartTypeDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	private void getChartTypeDropDownModels(){
		List list = new ArrayList();
		list.add(new IDropDownBean(1, GusmhybChart.CHART_BAR));
		list.add(new IDropDownBean(2, GusmhybChart.CHART_LINE));
		((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(list));
	}
	
	private String getDataField(){
		if (GusmhybChart.DATA_FADBMH.equals(getChartDropDownValue().getValue())){
			return "fadbzmh";
		}if (GusmhybChart.DATA_GONGDBMH.equals(getChartDropDownValue().getValue())){
			return "gongdbzmh";
		}if (GusmhybChart.DATA_GONGRBMH.equals(getChartDropDownValue().getValue())){
			return "gongrbzmh";
		}
		return "";
	}
	
	private int getDigts(){
		return 0;
	}
}