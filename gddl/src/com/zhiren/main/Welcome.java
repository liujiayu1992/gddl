package com.zhiren.main;
/*
 * ʱ�䣺2008-12-29
 * ���ߣ�chh
 * �ؼ��֣�Welcome,��ҳ��getShouhChart
 * �������պĶԱ�ͼ���ڼ����˺Ž���ʱ����ʾ��ȼ�Ϲ�˾������
 * �޸� getShouhChart �ļ��Ŷ�Ӧ�ĵ糧��������
 */

/*
 * ʱ�䣺2008-12-21
 * ���ߣ�chh
 * �ؼ��֣�Welcome,��ҳ
 * ��������֪ͨ���ļ�������г���̬���Ӱ���
 *  �ػʸ۵�ú̿�г�����۸��ܱ� 
 * 	����ú̿���ӽ���ƽ̨NEWCָ�� 
 * 	����ԭ�ͼ۸����� 
 * ��ϵͳ��Ϣ������'��ҳ��ʾ�г���̬����'����ֵΪ"��"������ʾ�г���̬��Ϣ
 */

/*
 * ʱ�䣺2008-09-09
 * ���ߣ�chh
 * �ؼ��֣�Welcome,��ҳ
 * ������ͼ��ѡ���������Ҫ֪ͨdiv Ĭ����ʾ����Ҫ֪ͨ��
 * ��ϵͳ�����п�������
 */
/*
 * ʱ�䣺2008-09-10
 * ���ߣ�chh
 * �ؼ��֣�Welcome,��ҳ
 * ��������Ҫ֪ͨ������ʱ��Ӻ�ǰ����
 * ��ϵͳ�����п�������
 */
/*
 * ʱ�䣺2008-09-26
 * ���ߣ�chh
 * �ؼ��֣�Welcome,��ҳ
 * ��������ݲ˵������ӵ��Ⱥ�˳����ʾ
 * ��ϵͳ�����п�������
 */
/*
 * ʱ�䣺2009-04-23
 * ���ߣ�ly
 * �ؼ��֣�Welcome,��ҳ��getZuorshc()
 * �������������Ĵ��е����ݣ����ŵ�½ʱ������������˾�͵糧��½ʱ����2λ��Ч����
 * �޸�: getZuorshc()����
 */
/*
* ʱ�䣺2009-12-04
* ���ߣ�chh
* �ؼ��֣�Welcome,��ҳ
* ������һ�����������ܳ���¼�����պĴ���ʾ�ܳ��պĴ棬ͼ������,
* 
*/
/*
 * ʱ��:2010-01-28
 * ����:tzf
 * �޸�����:��ҳ��ʾʱ  ���� ������ ��Ϣ��ʾ��
 */
/*
 * ʱ��:2010-01-28
 * ����:tzf
 * �޸�����:��ҳ��ʾʱ  ������ ��Ϣ�е���¯ú�� �ĳ�  ��¯������
 */
/*
 * ʱ��:2010-03-21
 * ����:tzf
 * �޸�����:��ҳ��ʾʱ  ������ ��Ϣ�е�shij  �ĳ� riq�ֶΡ�
 * 
 */
/*
 * ʱ��:2011-06-08
 * ����:���
 * ����:�޸�ú̿�������ͼ
 * 		��ʾǰ90���ÿ��3�Ŀ�����
 * 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2011-06-27
 * �����������IE7\8\9����ʾ���˿���ͼƬ�����⡣
 * 		�޸���java�����ɵ�js��ö�����﷨
 * 		��document.all.item(...)��Ϊdocument.getElementById(...)
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-01-25
 * ��������̬������ص�ַ
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.jfree.data.category.CategoryDataset;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public abstract class Welcome extends BasePage {
	private int msg = 1;

	public int getMsg() {
		return msg;
	}

	public void setMsg(int msg) {
		this.msg = msg;
	}

	public String getUserDanwmc(){
		Visit visit = (Visit)getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		return visit.getDiancqc();
	}
	
	public String getQuickStart() {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbhtml = new StringBuffer();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		//chh 2008-09-26 �������ӵ��Ⱥ�˳������
		sb.append("select z.id,z.wenjwz,z.mingc from ziyxxb z, quickstart q \n")
		.append("where z.id = q.ziyxxb_id and q.renyxxb_id = ")
		.append(visit.getRenyID()).append(" order by q.id");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		
		String sql = "select distinct x.zhi from xitxxb x where x.mingc='����ͨ������'";
		String strBeiymc = "����ͨ��";
		ResultSetList rsmc = con.getResultSetList(sql);
		if(rsmc.next()){
			strBeiymc = rsmc.getString("zhi");
		}
		
		sbhtml.append("<tr>");
		for(int i=1;i<9 ; i++) {
			if(i==5) {
				sbhtml.append("<td width=\"43%\">&nbsp;</td></tr><tr><td colspan=\"7\"><img src=\"imgs/startpage/spacer.gif\" width=\"1\" height=\"6\" /></td></tr><tr>");
			}else {
				//sbhtml.append("</tr><tr>");
			}
			if(i == 1 || i == 5) {
				sbhtml.append("<td width=\"1%\" align=\"right\"><img src=\"imgs/startpage/start_page_link_icon.gif\" alt=\"\" width=\"16\" height=\"16\" /></td>");
			}else {
				sbhtml.append("<td width=\"8%\" align=\"right\"><img src=\"imgs/startpage/start_page_link_icon.gif\" alt=\"\" width=\"16\" height=\"16\" /></td>");
			}
			if( rsl!= null && rsl.next()) {
				
				sbhtml.append("<td width=\"10%\" nowrap=\"nowrap\">").append("<a href=\"#\"  title='������' id=Node")
				.append(rsl.getString("id")).append(" url=\"").append(rsl.getString("wenjwz")).append("\"  onclick=\"QuickStart(this);\">")
				.append(rsl.getString("mingc")).append("</a></td>");
			}else {
				sbhtml.append("<td width=\"10%\" nowrap=\"nowrap\">").append("<a href=\"#\"  title='������' id=Node")
				.append("").append(" url=\"").append("").append("\"  onclick=\"\">")
				.append(strBeiymc).append("</a></td>");
			}
		}
		sbhtml.append("<td>&nbsp;</td></tr>");
		con.Close();
		return sbhtml.toString();
	}
//	public String addtd() {
//		
//	}
	
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
	
	public boolean getRaw() {
		return true;
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//�ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
		}	
	}
	
//	�õ��糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(long diancmcId) {
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
	
	public String getShouhChart(){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		StringBuffer sbsql = new StringBuffer();
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];
		
		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		
		String diancid = "";
		String groupid = "";
		String dcmc = "";
		String zhi = "��";
		String idDianc = "";
		
		if(isJitUserShow()){//ѡ����ʱˢ�³����еĵ糧
			diancid = " ";
			dcmc = " dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" and dc.ranlgs<>1  ";
			groupid = "dc.fuid";
		}else if (isGongsUser()){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" or  dc.shangjgsid="+((Visit) getPage().getVisit()).getDiancxxb_id()+") ";
			dcmc = " (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" or  dc.shangjgsid="+((Visit) getPage().getVisit()).getDiancxxb_id()+") ";
			groupid = "dc.id";
		}else{//ѡ�糧ֻˢ�³��õ糧
			zhi = MainGlobal.getXitsz("�糧��״ͼ�Ƿ���ʾú��",String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()) , "��");
			if (zhi.equals("��")){
				idDianc = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
				if (IsZongc(cn ,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
					diancid = " and dc.id in(select id from diancxxb where fuid in(select id from diancxxb where id=" + idDianc + ")) \n";
				}else{
					diancid = " and dc.id=" + idDianc + " \n";
				}

			}else{
				if (IsZongc(cn,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
					diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id();
					dcmc = " dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
				}else{
					diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
					dcmc = " dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				}
				
				groupid = "dc.id";
			}
		}
		
//		��״ͼ
		if (zhi.equals("��")){
			sbsql.append("select quanc||' '||mingc as mingc,fenx,laimsl shuj from   \n");
			sbsql.append(" (select dc.mingc as quanc,m.mingc, decode(1,1,'��Ӧ','') as fenx,sum(f.laimsl) as laimsl \n");
			sbsql.append(" from fahb f, meikxxb m,diancxxb dc \n");
			sbsql.append("where f.meikxxb_id = m.id \n");
			sbsql.append("and f.daohrq = to_date('" + strDate + "', 'yyyy-mm-dd') \n");
			sbsql.append("and dc.id=f.diancxxb_id \n");
			sbsql.append(diancid);
			sbsql.append("group by dc.mingc,m.mingc) s order by s.quanc");
		}else{
			sbsql.append("select fx.mingc,fx.fenx,nvl(sj.shul,0) as shuj from   \n");
			sbsql.append("(select dc.mingc as danw,fenx,shul from   \n");
			sbsql.append("(select "+groupid+" as danwid,decode(1,1,'��Ӧ','') as fenx,sum(nvl(dangrgm,0)) as shul from shouhcrbb rb,diancxxb dc   \n");
			sbsql.append("where rb.riq=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=dc.id "+diancid+" group by ("+groupid+")  \n");
			sbsql.append("union  \n");
			sbsql.append("select "+groupid+" as danwid,decode(1,1,'����','') as fenx,sum(nvl(haoyqkdr,0)) as shul from shouhcrbb rb,diancxxb dc  \n");
			sbsql.append("where rb.riq=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=dc.id "+diancid+" group by ("+groupid+") ) s,diancxxb dc where s.danwid=dc.id  \n");
			sbsql.append("order by fenx )sj,  \n");
			sbsql.append("(select dc.mingc,fx.fenx,fx.xuh from  \n");
			sbsql.append("(select mingc from diancxxb dc where "+dcmc+") dc, \n");
			sbsql.append("(select decode(1,1,'��Ӧ','') as fenx,1 as xuh from dual union select decode(1,1,'����','') as fenx,2 as xuh from dual)  fx ) fx \n");
			sbsql.append("where sj.danw(+)=fx.mingc and sj.fenx(+)=fx.fenx  \n");
			sbsql.append("order by mingc,fx.xuh \n");
		}
		ResultSet rs=cn.getResultSet(sbsql.toString());
		
		CategoryDataset dataset = cd.getRsDataChart(rs, "mingc", "fenx", "shuj");//rs��¼����������ͼƬ��Ҫ������
		
		/*--------------����ͼƬ������ʼ-------------------*/
		
		ct.intDigits=0;			//	��ʾС��λ��
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;//��б��ʾX�������
		
		/*--------------����ͼƬ��������-------------------*/
		
		//������״ͼ����ʾ��ҳ��
		ArrHeader[0]=new String[] {""+ct.ChartBar3D(getPage(), dataset,DateUtil.Formatdate("yyyy��MM��dd��",dat)+ "ú̿��ú��ͼ", 600, 295)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		
		cn.Close();

		return rt.getHtml();
	}
	
	public String getKucChart(){
		JDBCcon cn = new JDBCcon();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		Report rt=new Report();
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];

		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		
		long lngDays=90; //������������
		String WeekDay="4";//������Ϊ1������һΪ2,....�Դ����ƣ�������Ϊ7
		String diancid = "";
		
		if(isJitUserShow()){
			diancid = " and dc.ranlgs<>1 ";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
		}else{//ѡ�糧ֻˢ�³��õ糧
			if (IsZongc(cn,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
				diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" and dc.ranlgs<>1 ";
			}else{
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" and dc.ranlgs<>1 ";
			}
		}
		//�»��� 2008-12-24 ���������ı���diancxxb ��Ϊall_objects
		StringBuffer sbsql = new StringBuffer();
//		ʱ��ͼ
		sbsql.append("select  to_char(riq,'mm-dd') riq, fenx, kuc from (select * from  \n");
		sbsql.append(" (select bqrq.riq as riq,decode(1,1,'���ڿ��','') as fenx,round_new(nvl(kuc,0)/10000,2) as kuc from   \n");
		sbsql.append(" (select riq,sum(nvl(kuc,0)) as kuc  from shouhcrbb s,diancxxb dc   \n");
		sbsql.append("      where riq > to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+"   \n");
		sbsql.append("      and  riq <= to_date('"+strDate+"','yyyy-mm-dd') and s.diancxxb_id=dc.id "+diancid+" group by riq) bqsj,  \n");
		sbsql.append(" (SELECT riq, to_char(riq,'d') zhou FROM(select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from all_objects where rownum<="+lngDays+")WHERE to_char(riq,'d')="+WeekDay+") bqrq  \n");
		sbsql.append("   where bqrq.riq=bqsj.riq(+)   \n");
		sbsql.append(" union \n");
		sbsql.append("  select add_months(tqrq.riq,12) as riq,decode(1,1,'ͬ�ڿ��','') as fenx,round_new(nvl(kuc,0)/10000,2) as kuc from \n");
		sbsql.append(" (select riq,sum(nvl(kuc,0)) as kuc from shouhcrbb s,diancxxb dc   \n");
		sbsql.append("      where riq > add_months(to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+",-12)  \n");
		sbsql.append("      and  riq <= add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12) and s.diancxxb_id=dc.id "+diancid+" group by riq) tqsj,  \n");
		sbsql.append(" (SELECT add_months(riq,-12) riq, to_char(riq,'d') zhou FROM(select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from all_objects dc where rownum<="+lngDays+")WHERE to_char(riq,'d')="+WeekDay+") tqrq  \n");
		sbsql.append("  where tqrq.riq=tqsj.riq(+)   \n");
		sbsql.append("  ) order by riq ) \n");
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		
		CategoryDataset data2 = cd.getRsDataChart(rs,  "riq","fenx", "kuc");//rs��¼����������ͼƬ��Ҫ������
		
		/*--------------����ͼƬ������ʼ-------------------*/
		
		ct.intDigits=0;			//	��ʾС��λ��
		ct.xfontSize=9;		//	x�������С
//		ct.dateApeakShowbln = false;//��ֱ��ʾx�������
		ct.xTiltShow=false;  //x�������Ƿ���б
//		ct.lineDateFormatOverride = "dd";//x�������ֻ��ʾ��
		/*--------------����ͼƬ��������-------------------*/		
		ArrHeader[0]=new String[] {""+ct.ChartXYLine(getPage(), data2, "ú̿�������ͼ(ǰ90��)", 600, 295)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		cn.Close();

		return rt.getHtml();
	}
	
	//��һ�����Ƶ��ܳ���������3�������к���
	private boolean IsZongc(JDBCcon cn,String strDiancID ){
		String sqlq="select id from diancxxb where fuid in(select id from diancxxb where id="+strDiancID+" and jib=3)";
		ResultSetList rs=new ResultSetList();
		rs=cn.getResultSetList(sqlq);
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
	
	public String getZuorshc(){
		
		JDBCcon con = new JDBCcon();
		ResultSetList rs;
		
		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		Date datmm=DateUtil.getFirstDayOfMonth(dat);//�³�
		Date datyy=DateUtil.getFirstDayOfYear(dat);//���
		String fdom=DateUtil.FormatDate(datmm);//�����ַ�
		String fdoy=DateUtil.FormatDate(datyy);//�����ַ�
		String diancid = "";
		String strMeil = "";
		String strDanw = "";
		
		
		if(isJitUserShow()){
			diancid = "";
			strMeil = "select (select dia.quanc from diancxxb dia where id="+((Visit) getPage().getVisit()).getDiancxxb_id() +") as quanc  ,round(c.laim/10000,0) as laim,round(c.haom/10000,0) as haom,round(c.kuc/10000,0) as kuc,round(a.nljlm/10000,0) as nlm,round(a.nljhm/10000,0) as nhm,round(b.yljlm/10000,0) as ylm,round(b.yljhm/10000,0) as yhm\n" ;
			strDanw = "���";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
			strMeil = "select (select dia.quanc from diancxxb dia where id="+((Visit) getPage().getVisit()).getDiancxxb_id() +") as quanc  ,round(c.laim/10000,2) as laim,round(c.haom/10000,2) as haom,round(c.kuc/10000,2) as kuc,round(a.nljlm/10000,2) as nlm,round(a.nljhm/10000,2) as nhm,round(b.yljlm/10000,2) as ylm,round(b.yljhm/10000,2) as yhm\n" ;
			strDanw = "���";
		}else{//ѡ�糧ֻˢ�³��õ糧
			//��ϵͳ��Ϣ���в�ѯ������ǳ������Ƿ���ʾ������ú���
			//��һ��������ʾ��������ܳ���ʾ�ӳ��ĺϼ�
			if (IsZongc(con,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
				diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}else{
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			
			strMeil = "select dc.quanc as quanc ,round(c.laim,2) as laim,round(c.haom,2) as haom,round(c.kuc,2) as kuc,round(a.nljlm/10000,2) as nlm,round(a.nljhm/10000,2) as nhm,round(b.yljlm,2) as ylm,round(b.yljhm,2) as yhm\n" ;
			strDanw = "��";
		}
		
		
		String sql = strMeil +
//			"select round(c.laim/10000,0) as laim,round(c.haom/10000,0) as haom,round(c.kuc/10000,0) as kuc,round(a.nljlm/10000,0) as nlm,round(a.nljhm/10000,0) as nhm,round(b.yljlm/10000,0) as ylm,round(b.yljhm/10000,0) as yhm\n" +
			"from shouhcrbb rb,diancxxb dc,(select nvl(sum(rb.dangrgm),0) as nljlm,nvl(sum(rb.haoyqkdr),0) as nljhm from shouhcrbb rb,diancxxb dc\n" + 
			"where rb.diancxxb_id = dc.id and dc.ranlgs<>1 "+diancid+" \n" + 
			"and rb.riq < =to_date('"+strDate+"','yyyy-mm-dd') and rb.riq >= to_date('"+fdoy+"','yyyy-mm-dd')) a,(select nvl(sum(rb.dangrgm),0) as yljlm,nvl(sum(rb.haoyqkdr),0) as yljhm\n" + 
			" from shouhcrbb rb,diancxxb dc\n" + 
			"where rb.diancxxb_id = dc.id and dc.ranlgs<>1 "+diancid+" \n" + 
			"and rb.riq < =to_date('"+strDate+"','yyyy-mm-dd') and rb.riq >= to_date('"+fdom+"','yyyy-mm-dd')) b,(select nvl(sum(rb.dangrgm),0) as laim,nvl(sum(rb.haoyqkdr),0) as haom,nvl(sum(rb.kuc),0) as kuc\n" + 
			" from shouhcrbb rb,diancxxb dc\n" + 
			"where rb.diancxxb_id = dc.id and dc.ranlgs<>1 "+diancid+" \n" + 
			"and rb.riq = to_date('"+strDate+"','yyyy-mm-dd'))c\n" + 
			"where\n" + 
			"rb.diancxxb_id=dc.id and dc.ranlgs<>1 "+diancid+"\n" + 
			"group by dc.quanc,c.laim,c.haom,a.nljlm,a.nljhm,b.yljlm,b.yljhm,c.kuc";

		rs = con.getResultSetList(sql);
		String laim="";
		String ylm="";
		String nlm="";
		String haom="";
		String yhm="";
		String nhm="";
		String kuc="";
		String strshouhc = "";
		String diancMC = "";
		if(rs.next()){
			diancMC = rs.getString("quanc");
			laim = rs.getString("laim");			
			if(laim.equals("0")||laim.equals("")){
				laim="������ú: ";
			}else{
				laim= diancMC + " ������ú:<a style=\"color: #FF6600;font-size:18px;\">"+laim+"</a>"+strDanw+";<br/>";
			}
			ylm = rs.getString("ylm");
			if(ylm.equals("0")||ylm.equals("")){
				ylm="����ú: ";
			}else{
				ylm= diancMC + " ����ú:<a style=\"color: #FF6600;font-size:18px;\">"+ylm+"</a>"+strDanw+";<br/>";
			}
			nlm = rs.getString("nlm");
			if(nlm.equals("0")||nlm.equals("")){
				nlm="����ú: ";
			}else{
				nlm= diancMC + " ����ú:<a style=\"color: #FF6600;font-size:18px;\">"+nlm+"</a>���;<br/>";
			}
			
			haom = rs.getString("haom");
			if(haom.equals("0")||haom.equals(""))
			{
				haom="���պ�ú: ";
			}
			else
			{
				haom= diancMC + " ���պ�ú:<a style=\"color: #FF6600;font-size:18px;\">"+haom+"</a>"+strDanw+";<br/>";
			}
			yhm = rs.getString("yhm");
			if(yhm.equals("0")||yhm.equals(""))
			{
				yhm="�º�ú: ";
			}
			else
			{
				yhm= diancMC + " �º�ú:<a style=\"color: #FF6600;font-size:18px;\">"+yhm+"</a>"+strDanw+";<br/>";
			}
			nhm = rs.getString("nhm");
			if(nhm.equals("0")||nhm.equals(""))
			{
				nhm="���ú: ";
			}
			else
			{
				nhm= diancMC + " ���ú:<a style=\"color: #FF6600;font-size:18px;\">"+nhm+"</a>���;<br/>";
			}
			kuc = rs.getString("kuc");
			if(kuc.equals("0")||kuc.equals(""))
			{
				kuc="�����: </br>";
			}
			else
			{
				kuc= diancMC + " �����:<a style=\"color: #FF6600;font-size:18px;\">"+kuc+"</a>"+strDanw+"&nbsp;</br>";
			}
			
			
			}
		if(strshouhc.equals("")){
			strshouhc = laim+haom+kuc+ylm+yhm+kuc+nlm+nhm+kuc;
		}
		con.Close();
		return strshouhc;
	}

	public String getKucgjdc(){
		JDBCcon con = new JDBCcon();
		Date dat=DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		
		String diancid = "";
		if(isJitUserShow()){
			diancid = "";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
		}else{//ѡ�糧ֻˢ�³��õ糧
			if (IsZongc(con,""+((Visit) getPage().getVisit()).getDiancxxb_id())){
				diancid = " and dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}else{
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
			}
		}
		
		String sql = "select * from (select dc.id as id,dc.mingc\n"
					+ "  from shouhcrbb rb,diancxxb dc\n"
					+ " where rb.diancxxb_id=dc.id " +
							"and rb.riq=to_date('"+strDate+"','yyyy-mm-dd') "+
							diancid+" \n"
					+ "   and rb.kuc<dc.jingjcml order by dc.xuh)";

		ResultSetList rs = con.getResultSetList(sql);
		
		String strkucgjdc = "";
		int danws = 0;//��λ����
		for(int i=0;rs.next();i++){
			
			danws = danws+1;
			if(i<50){
				if(strkucgjdc.equals("")){
					strkucgjdc ="<a href='#' onclick='getShouhcPage("+ rs.getString("id") +");'>" + rs.getString("mingc") + "</a>";
				}else{
					strkucgjdc = strkucgjdc+"<a href='#' onclick='getShouhcPage("+ rs.getString("id") +");'>" + rs.getString("mingc") + "</a>";
				}
				if(danws>=2 && danws%2==0){
					strkucgjdc = strkucgjdc+"</br>";
				}else{
					strkucgjdc = strkucgjdc+"<a>&nbsp;&nbsp;&nbsp;&nbsp;</a>";
				}
			}else{
				break;
			}
		}
		if(danws>0 && danws%2!=0){
			strkucgjdc = strkucgjdc+"<br>";
		}
		setMsg((danws+1)/2);
		con.Close();
		return strkucgjdc;
	}
	
public String getWenj(){
		JDBCcon con = new JDBCcon();
		Date dat=new Date();//����
		String strDate=DateUtil.FormatDate(dat);//�����ַ�
		Visit visit = (Visit)this.getPage().getVisit();
		String diancid = "";
		if(isJitUserShow()){
			diancid = "";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
		}else{//ѡ�糧ֻˢ�³��õ糧
			diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		String sql =
			"select w.id as id,r.mingc reny,to_char(min(f.shij),'YYYY-MM-DD HH24:MI:SS')shij,to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd') as tians,x.zhi as tiansbj,"
			+"to_char(min(f.youxq),'YYYY-MM-DD')youxsj,getMainHtmlAlert('main','"+MainGlobal.getHomeContext(this)
			+"','Tongzwj','wenj',w.id, w.biaot,to_char(f.shij,'yyyy-mm-dd'),(x.zhi-(to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd')))) as biaot,getJiesdws(w.id,f.shij) jiesdws\n" +
			"from fabwjb f,renyxxb r,wenjb w,xitxxb x \n" + 
			"where f.renyxxb_id=r.id and f.wenjb_id=w.id and f.diancxxb_id = "+visit.getDiancxxb_id()+" and x.mingc='������������' \n" +
			"and f.youxq>=sysdate\n" + 
			"group by w.id,r.mingc ,w.biaot ,f.shij,x.zhi order by shij desc";

		ResultSetList rs = con.getResultSetList(sql);
		
		
		int i = 0;
		int j = 0;
		int iAllLines=13;
		
		String wenj = "";
		String strbiaot = "";
		String strShicdt=getShicdtLink();
		if (strShicdt.length()>0) {//���г���̬��������ã����������и��г���̬��3��
			iAllLines=iAllLines-3;
		}
		
		for( i=0;rs.next();i++){
			if(i<iAllLines){
				if(rs.getInt("tians")<=rs.getInt("tiansbj")){
					strbiaot = "<img src='"+ MainGlobal.getHomeContext(this)+"/imgs/login/zdtgs/news.gif'" +">"+rs.getString("biaot");
				}else{
					strbiaot = rs.getString("biaot");
				}
				if(wenj.equals("")){
					wenj ="<tr><td class=\"news\">" + strbiaot+"&nbsp</td></tr>" ;
				}else{
					wenj = wenj+"<tr><td class=\"news\">" +strbiaot+"&nbsp</td></tr>" ;
				}
			}else{
				break;
			}
			j = i;
		}
		for(int k =j;k<iAllLines;k++){
			if(wenj.equals("")){
				wenj="<tr><td>&nbsp;</td></tr>";
			}else{
				wenj=wenj+"<tr><td>&nbsp;</td></tr>";
			}
		}
		wenj=wenj+"<tr><td>&nbsp;</td></tr>";
		wenj=wenj+getShicdtLink();
//		wenj = wenj+"<tr><td class=\"news\" align=\"right\"><a href=\"#\">���� >></a></td></tr>";
		con.Close();
		return wenj;
	}
	
	public String getShicdtLink(){
		JDBCcon cn=new JDBCcon();
		String strSz ="";
		ResultSetList rsmc = cn.getResultSetList( "select distinct x.zhi from xitxxb x where x.mingc='��ҳ��ʾ�г���̬����'");
		if(rsmc.next()){
			strSz= rsmc.getString("zhi");
		}
		rsmc.close();
		cn.Close();
		
		String wenj="";
		if ("��".equals(strSz)){//����������ڡ���ҳ��ʾ�г���̬���ӡ���ʾ���������������
			wenj ="<tr><td class=\"news\" id=\"GangkjgReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">�ػʸ۵�ú̿�г�����۸��ܱ�</a></td></tr>" ;
			wenj =wenj+"\n<tr><td class=\"news\" id=\"GuojmjReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">����ú̿���ӽ���ƽ̨NEWCָ��</a></td></tr>" ;
			wenj =wenj+"\n<tr><td class=\"news\" id=\"GuojyjReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">����ԭ�ͼ۸�����</a></td></tr>" ;
		}
		return wenj;
	}
	public String getOneTongz(String strWenjID ){
		StringBuffer str=new StringBuffer();	
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String biaot="";
		String neir="";
		String yuanmc="";
		String url="";
		String fujbItem="";
		String fabr="";
		String fabsj="";
		try {
			String sql="select id,biaot,neir from wenjb where id="+strWenjID;
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				biaot=""+rs.getString("biaot");
				neir="&nbsp;&nbsp;&nbsp;&nbsp;"+rs.getString("neir");
				
				String newurl=MainGlobal.getHomeContext(this);
				String sql1="select f.yuanmc,NVL(SUBSTR(URL,0,INSTR('"+newurl+"','service=page')-1),'"+newurl+"')||SUBSTR(URL,INSTR(URL,'service=page')-1) newurl from fujb f where f.wenjb_id="+rs.getLong("id");
				ResultSet rs1=con.getResultSet(sql1);
				int i=0;
				while(rs1.next()){
					i++;
					yuanmc=rs1.getString("yuanmc");
					url=rs1.getString("url");
					System.out.println("url:"+url);
					fujbItem=fujbItem+ "����"+i+":"+ "<a  onclick=\"window.open('"+url+"')\" href=\"#\" >"+yuanmc+"</a><br>";
				}
				rs1.close();
				String sql2="select r.quanc,f.shij from fabwjb f,renyxxb r where f.renyxxb_id=r.id and f.diancxxb_id="+visit.getDiancxxb_id()+" and wenjb_id="+rs.getLong("id")+"";
				ResultSet rs2=con.getResultSet(sql2);
				while(rs2.next()){
					fabr=rs2.getString("quanc");
					fabsj=rs2.getString("shij");
				}
				rs2.close();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		str.append("<table ><tr> \n");
		str.append("<td width=20></td> \n");
		str.append("<td width=90% > \n");
		str.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
		str.append("<tr><td height=5></td></tr> \n");
		str.append("<tr><td align=\"center\" style=\"font-size: 15pt;color: #FF0000\" height=\"50\" ><b>"+biaot+"</b></td></tr> \n");
		str.append("<tr><td height=30 align=right valign=top >�����ˣ�"+fabr+"&nbsp;&nbsp;&nbsp;&nbsp;����ʱ�䣺"+fabsj+"<hr></td></tr> \n");
		str.append("<tr><td  align=\"left\"  valign=top style=\"line-height: 150%\" >&nbsp;&nbsp;&nbsp;&nbsp; "+neir+"</td></tr>");
		str.append("<tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr> \n");
		str.append("<tr><td align=\"left\"><hr>"+fujbItem+"</td></tr></table></td><td width=20></td> \n");
		str.append("</tr></table> \n");
		return str.toString();
	}
	public String getTongz() {//����ҳ�����ݡ�biaot,neir,fujbItem
		StringBuffer str=new StringBuffer();	
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		try {
			 //�õ��ö��ĸ��ļ�
			 String strsql="select w.id as id \n" + 
				"      from fabwjb f,wenjb w ,renyxxb ry\n" + 
				"      where f.wenjb_id=w.id and f.diancxxb_id ="+visit.getDiancxxb_id()+"\n" + 
				"      and ry.id=f.renyxxb_id\n" + 
				"      and f.zhid=1\n" + 
				"      and f.youxq>=sysdate order by f.shij desc\n"  ;
			 ResultSet rs=con.getResultSet(strsql);
			 while (rs.next()){
				 if (str.length()>0){
					str.append("\n<br>\n"); 
				 }
				 str.append(getOneTongz(rs.getString("id")));
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return str.toString();
	}
	
	public String getSetDefaultItem(){
		//Ĭ����ʾ�����ͼ������֪ͨ
		Visit visit = (Visit) getPage().getVisit();
		String defaultItem=MainGlobal.getXitsz("��ҳĬ����ʾѡ�",visit.getDiancxxb_id()+"", "���ͼ");
		String strItem ="kuc";
		if (defaultItem.equals("���ͼ")){
			strItem="kuc";
		}else if(defaultItem.equals("��Ҫ֪ͨ")){
			strItem="tongz";
		}else if(defaultItem.equals("�պıȽ�ͼ")){
			strItem="shouh";
		}
		return "<script>\n" +
			"ChangeChart(document.getElementById(\""+strItem+"\"));\n"+
			"</script>\n";
	}
	
	
	private String DaibrwMsg;//����������ʾ��Ϣ����

	public String getDaibrwMsg() {
		this.ShezDaibrwMsg();
		return DaibrwMsg;
	}

	public void setDaibrwMsg(String daibrwMsg) {
		DaibrwMsg = daibrwMsg;
	}
	
	private void ShezDaibrwMsg(){//���ô���������ʾ��Ϣ
		
		String sql=" select * from xitxxb where mingc='��ҳ���½ǳ��ִ���������Ϣ��ʾ'  and zhi='��' and leib='��ҳ' and zhuangt=1 ";
		JDBCcon con=new JDBCcon();
		if(!con.getHasIt(sql)){
			con.Close();
			return;
		}
		
		String tabrTitle="��Ϣ��ʾ";
		String conTitle="��������&nbsp;&nbsp;"+this.getYestodayStr("yyyy��MM��dd��",new Date(),0);
		String msgstr="";
		String handler="";
		
		ResultSetList rsl=this.hasDianc(con, ((Visit)this.getPage().getVisit()).getDiancxxb_id());
		ResultSetList rs=null;
		String brq=DateUtil.FormatOracleDate(this.getYestodayStr("yyyy-MM-dd",new Date(),-1));//����
		String qrq=DateUtil.FormatOracleDate(this.getYestodayStr("yyyy-MM-dd",new Date(),-2));//ǰ��
		int cun=0;
		while(rsl.next()){
			String diancxxb_id=rsl.getString("id");
			String dcid=rsl.getString("dcid");
			
			StringBuffer bf=new StringBuffer();
			bf.append(" select \n");
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='fahb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;��ú��:δ����','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='fahb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='fahb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;��ú��:δ��ȫ����' " +
					") " +
					")  " //Ϊ�� ˵����Щ��������û�е���ɹ�
					);
			
			bf.append(" || \n");
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='zhilb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;�볧����:δ����','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='zhilb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='zhilb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;�볧����:δ��ȫ����' " +
					") " +
					")  " //Ϊ�� ˵����Щ��������û�е���ɹ�
					);
			bf.append(" || \n");
			
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;��¯����:δ����','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;��¯����:δ��ȫ����' " +
					") " +
					")  " //Ϊ�� ˵����Щ��������û�е���ɹ�
					);
			bf.append(" || \n");
			
			
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;�պĴ�:δ����','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;�պĴ�:δ��ȫ����' " +
					") " +
					")  " //Ϊ�� ˵����Щ��������û�е���ɹ�
					);
			bf.append(" || \n");
			
			
			
			bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='riscsjb'and j.caoz=0 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
			bf.append(" '',\n");
			bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'', nvl('&nbsp;&nbsp;����������:δ����','') ),\n");
			bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='riscsjb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
					" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='riscsjb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','' ,'&nbsp;&nbsp;����������:δ��ȫ����' " +
					") " +
					")   \n " //Ϊ�� ˵����Щ��������û�е���ɹ�
					);
			bf.append(" || \n");
			
			
			
			bf.append(" decode(" +
					"(select count(distinct leib) from hetb where diancxxb_id="+dcid+" and liucztb_id!=1 ),\n" +
					"0,''," +
					"1,decode( (select distinct leib from hetb where diancxxb_id="+dcid+" and liucztb_id!=1 ),0,'&nbsp;&nbsp;��ͬ:δ����','&nbsp;&nbsp;��ͬ:δ���'),\n" +
					"'&nbsp;&nbsp;��ͬ:δ�����δ���'" +
					")\n");
			bf.append(" || \n");
			
			
			
			bf.append(" ( select decode(count(distinct leib),2,nvl('&nbsp;&nbsp;���㵥:δ�����δ���',''),decode(max(leib),0,nvl('&nbsp;&nbsp;���㵥:δ����',''),1,nvl('&nbsp;&nbsp;���㵥:δ���','') )  )  from "+
					" (select  0 leib from jiesb where diancxxb_id="+dcid+" and liucztb_id=0 and rownum=1 union select 0 leib from jiesyfb where diancxxb_id="+dcid+" and liucztb_id=0 and rownum=1 \n" +
					" union select  1 leib from kuangfjsmkb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 union select  1 leib from kuangfjsyfb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 \n" +
					" union select  1 leib from diancjsmkb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 union select   1 leib from diancjsyfb where diancxxb_id="+dcid+" and liucztb_id<>1 and rownum=1 " +
					") " +
					") ");
		
			
			bf.append(" msg from dual \n");
	
			rs=con.getResultSetList(bf.toString());
			cun++;
			if(rs.next()){
				if(rs.getString("msg")!=null && !rs.getString("msg").equals("")){
				msgstr+=" <A href=\\'\\' onclick =\\'parent.openNewPage("+dcid+")\\'  hidefocus=false id=\\'btCommand"+cun+"\\' style=\\'text-decoration: underline\\'> "+"<FONT color=#ff0000>"+rsl.getString("mingc")+":"+"</FONT>"+rs.getString("msg")+"</A>"+"<br><br>";
//				handler+=" oPopup.document.getElementById(\\'btCommand"+cun+"\\').onclick =openNewPage;";
				}
			}
		}
		
		rs.close();
		rsl.next();
		con.Close();
		this.setDaibrwMsg("popmsg('"+tabrTitle+"','"+conTitle+"','"+msgstr+"','"+handler+"');");
	}
	
	protected void initialize(){
		this.setDaibrwMsg("");
	}
	
	
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private ResultSetList hasDianc(JDBCcon con,long id){ 
		
//		String sql="select z.jiekzhb_id id,d.id dcid, d.mingc from diancxxb d,dianczhb z where d.id=z.diancxxb_id and  fuid = " + id +" order by xuh asc ";
//		ResultSetList rsl=con.getResultSetList(sql);
		
//		if(rsl.getRows()>0){//˵���� ���糧 ���߹�˾��
//			return rsl;
//		}
//		
		String sql="select nvl(z.jiekzhb_id,-1) id,d.id dcid, d.mingc from diancxxb d,dianczhb z where d.id=z.diancxxb_id(+) and ( d.id = " + id +" or d.fuid="+id+") order by xuh asc ";
		ResultSetList rsl=con.getResultSetList(sql);
		return rsl;
	}
    
    
    
	//�õ����������
	private String getYestodayStr(Date date){
		
		return getYestodayStr( null, date,-1);
	}
	
//	�õ�ǰ�������
	private String getYestodayStr(String format,Date date,int i){
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		cal.add(Calendar.DATE, i);
		
		if(format==null || format.equals(""))
			return DateUtil.FormatDate(cal.getTime());
		
		return DateUtil.Formatdate(format, cal.getTime());
	}
    
    
}