////2008-08-05 chh 
//�޸����� ��ȼ�Ϲ�˾���û����Բ鿴������
//		   ��ϸ������ʾ

//2008-08-16 ll 
//�޸����� ����ӡ���������Ӽƻ��ھ������䷽ʽ����
/*
 * ����:tzf
 * ʱ��:2010-01-27
 * �޸�����:����������˾Ҫ��ӯ���������໥���㣬���򱨱����治ƽ���ò������ơ�
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-08-26
 * ����������getLaimlField()�����ж���ú������ȡֵ��ʽ��
 */
package com.zhiren.jt.zdt.yansgl.rucmslys;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/*
 * ���ߣ�songy
 * ʱ�䣺2011-7-20
 * ���������Ӵ�ú�󼶱�ʼ���ھ�����
 */
public class RucmslysReport extends BasePage {
	public final static String LX_DB="db";
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

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}
	
	private String title_date1="";//ҳ���������1
	private String title_date2="";//ҳ���������2
	private String strdate1=""; 
	private String strdate2="";
	private String strJihkjvalue="";//�ƻ��ھ�����
	private String strYunsfsvalue="";//���䷽ʽ����
	/*
	 * ��Ϊ�Աȱ������
	 */
	private String getCondtion1(){
		
		String strCondtion="";
		
		/*
		 * ҳ��ѡ�����ڣ�����ѡ������
		 */
		String date1=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1());//ҳ�濪ʼ����
		String date2=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2());//ҳ���������
		
		String date3= ((Visit)getPage().getVisit()).getString12();//������ʼ����
		String date4= ((Visit)getPage().getVisit()).getString11();//������������
		
		/*����ѡ��:
		 * 1.��ͬ�ڶԱ�
		 * 2.����
		 * 3.ʱ��Ա�
		 */
		String rbvalue="";
		if (((Visit) getPage().getVisit()).getString13()!=null){
			rbvalue= ((Visit) getPage().getVisit()).getString13();
		}
		if(rbvalue.equals("tiaoj1")||rbvalue.equals("")){//��ͬ�ڶԱ�
			
			strdate1=" and fh.daohrq>=to_date('"+date1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date2+"','yyyy-mm-dd') \n";
			strdate2=" and fh.daohrq>=add_months(to_date('"+date1+"','yyyy-mm-dd'),-12) and fh.daohrq<=add_months(to_date('"+date2+"','yyyy-mm-dd'),-12)  \n";
			title_date1=date1+"<br>��<br>"+date2;
			title_date2=DateUtil.FormatDate(DateUtil.getFirstDayOfLastYear(date1))+"<br>��<br>"+DateUtil.FormatDate(DateUtil.getFirstDayOfLastYear(date2));
			
		}else if(rbvalue.equals("tiaoj2")){//����
			
			strdate1=" and fh.daohrq>=to_date('"+date1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date2+"','yyyy-mm-dd') \n";
			strdate2=" and fh.daohrq>=add_months(to_date('"+date1+"','yyyy-mm-dd'),-1) and fh.daohrq<=add_months(to_date('"+date2+"','yyyy-mm-dd'),-1)  \n";
			title_date1=date1+"<br>��<br>"+date2;
			title_date2=DateUtil.FormatDate(DateUtil.getDate(date1))+"<br>��<br>"+DateUtil.FormatDate(DateUtil.getDate(date2));

		}else{//ʱ��ζԱ�
			
			strdate1=" and fh.daohrq>=to_date('"+date1+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date2+"','yyyy-mm-dd') \n";
			strdate2=" and fh.daohrq>=to_date('"+date3+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+date4+"','yyyy-mm-dd') \n";
			title_date1=date1+"<br>��<br>"+date2;
			title_date2=date3+"<br>��<br>"+date4;
			
		}
		
		//strCondtion= strCondtion+Beginriq+Endriq;//�õ�ʱ���
		/*
		 * ���䷽ʽ
		 */
		long lngYunsfsId=-1;
		
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and fh.yunsfsb_id=" +lngYunsfsId;
		}
		/*
		 * �ƻ��ھ�
		 */
		long lngJihkjId= -1;
		
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and fh.jihkjb_id=" +lngJihkjId;
		}
		
		return strCondtion;
		
	}
	
	private String getCondtion(){//��Ϊ�����������
		String strCondtion="";
		int intLen=0;
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		long lngYunsfsId=-1;
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
			if(lngYunsfsId!=-1){
				strYunsfsvalue=((Visit) getPage().getVisit()).getDropDownBean3().getValue();
			}else{
				strYunsfsvalue="";
			}	
		}
		
		long lngJihkjId= -1;
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
			if(lngJihkjId!=-1){
				strJihkjvalue= ((Visit) getPage().getVisit()).getDropDownBean4().getValue();
			}else{
				strJihkjvalue="";
			}
		
		}
		
		String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();//((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
		
		String strDate1=DateUtil.FormatDate(datStart);//�����ַ�
		String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
		
		strCondtion="and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
			"and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
		
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and fh.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and fh.jihkjb_id=" +lngJihkjId;
		}
		
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==3){
				strCondtion=strCondtion+" and dc.id=" +pa[0];
				if (pa[1]!="000"){
					strCondtion=strCondtion+" and fh.jihkjb_id="+pa[1] ;
				} 
				strCondtion=strCondtion+" and y.dqid=" +pa[2];

			}else if (pa.length==2){
				strCondtion=strCondtion+" and dc.id=" +pa[0];

				strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}
			else {
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
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		String strDate1=DateUtil.FormatDate(datStart);//�����ַ�
		String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
		
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
		JDBCcon cn = new JDBCcon();
		
		String xhjs_str=" select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
		ResultSetList rsl_ys=cn.getResultSetList(xhjs_str);
		
		String yuns_js=" sum(round_new(fh.yuns,"+"0"+")) as yuns,\n";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
		
	
		if(rsl_ys.next()){
			
			 yuns_js=" sum(round_new(fh.yingk,"+"0"+")) + sum(round_new(fh.biaoz,"+"0"+")) " +
			"- sum(round_new(fh.jingz,"+"0"+")) as yuns,\n ";//�𳵵� ���� �������䷽ʽ�� ���� ������ʽ
		}
		
		rsl_ys.close();
		
		 
		
		sbSqlBody.append(" sum(round_new(sj.ches,0)) as ches,\n");
		sbSqlBody.append(" sum(round_new(sj.biaoz,0)) as biaoz,sum(round_new(sj.laimsl,0)) as laimsl,\n");
		sbSqlBody.append("sum(round_new(sj.yuns,0)) as yuns,sum(round_new(sj.jingz,0)) as jingz,  sum(round_new(sj.yingd,0)) as yingd,\n");
		sbSqlBody.append(" sum(round_new(sj.kuid,0)) as kuid, sum(round_new(sj.koud,0)) as koud \n");
		sbSqlBody.append(" from \n");
		sbSqlBody.append("       (select fh.diancxxb_id,fh.gongysb_id, sum(fh.ches) as ches,"+getLaimlField()+" as laimsl,round_new(sum(fh.biaoz),0) as biaoz,round_new(sum(fh.jingz),0) as jingz,  \n");
		sbSqlBody.append(yuns_js+"             round_new(sum(fh.yingd),0) as yingd, \n");
		sbSqlBody.append("              round_new(sum(fh.yingd-fh.yingk),0) as kuid, round_new(sum(fh.koud),0) as koud \n");
		sbSqlBody.append("       from fahb fh,vwdianc dc,vwgongys y  \n");
		sbSqlBody.append("       where fh.gongysb_id=y.id  \n");
		sbSqlBody.append(getCondtion());
		sbSqlBody.append("         and fh.diancxxb_id=dc.id    \n");
		sbSqlBody.append("         and fh.gongysb_id=y.id \n");
		sbSqlBody.append("         group by fh.diancxxb_id,fh.gongysb_id,fh.lieid ) sj,vwdianc dc,vwgongys y  \n");
		sbSqlBody.append(" where  sj.diancxxb_id=dc.id and sj.gongysb_id=y.id  \n");
		
		 
		if (leix.equals(this.LX_FC)){
			sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc, \n");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbsql.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="�볧ú����ͳ��(�ֳ���)";
		}else if(leix.equals(this.LX_FK)){
			sbsql.append("select decode(grouping(smc)+grouping(dqmc),2,'�ܼ�',1,smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(smc,dqmc)   \n");
			sbsql.append(" order by grouping(smc) desc,max(sxh) ,smc, \n");
			sbsql.append("          grouping(dqmc) desc,max(dqxh) ,dqmc \n");
			strTitle="�볧ú����ͳ��(�ֿ��)";
		}else if(leix.equals(this.LX_FCFK)){
			sbsql.append("select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'�ܼ�',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append("group by rollup(fgsmc,dc.mingc,dqmc)   \n");
			sbsql.append(" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbsql.append("          grouping(dqmc) desc,max(dqxh) ,dqmc \n");
			strTitle="�볧ú����ͳ��(�ֳ��ֿ��)";
		}else if(leix.equals(this.LX_FKFC)){
			sbsql.append("select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'�ܼ�',2,dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(dqmc,fgsmc,dc.mingc)   \n");
			sbsql.append(" order by  grouping(dqmc) desc,max(dqxh) ,dqmc, \n");
			sbsql.append("          grouping(fgsmc) desc,max(fgsxh) ,fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="�볧ú����ͳ��(�ֿ�ֳ���)";
		}else if(leix.equals(this.LX_QP)){
			strTitle="�볧ú����ͳ��(����)";
		}else if(leix.indexOf(",")>0){
			return getPrintDataTz();
		}
		
		_CurrentPage = 1;
		_AllPages = 1;
	
		
		String titleName=strTitle;
		
		// �����ͷ����
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][]=null;
		int ArrWidth[] =null;
		if (leix.equals(this.LX_DB)){//�Աȱ�
			ArrHeader = new String[2][5];
			
			ArrHeader[0] = new String[] { "��λ", "ʵ����(��)","ʵ����(��)", "�仯", "�仯" };
			ArrHeader[1] = new String[] { "��λ", title_date1,title_date2, "�仯��(��)", "�仯����(%)" };
			ArrWidth = new int[] { 150, 80, 80, 80, 80 };
		}else{//����
			ArrHeader = new String[2][9];
			
			ArrHeader[0] = new String[] { "��λ", "����", "Ʊ��<br>(��)", "ʵ����<br>(��)","����<br>(��)","����<br>(��)", "ӯ��<br>(��)", "����<br>(��)", "�۶�<br>(��)" };
			ArrHeader[1] = new String[] { "��λ", "����","Ʊ��<br>(��)","ʵ����<br>(��)", "����<br>(��)", "����<br>(��)", "ӯ��<br>(��)", "����<br>(��)", "�۶�<br>(��)" };
			ArrWidth = new int[] { 150, 65, 65, 65, 65, 65, 65, 65, 65 };
		}
		
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		  Visit visit=((Visit) getPage().getVisit());
		   String zhibdw=visit.getDiancqc();
		if(zhibdw.equals("��������ȼ�����޹�˾")&&visit.getRenyjb()==2){
			zhibdw="���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���";
		}
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+ zhibdw,Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, DateUtil.Formatdate("yyyy��MM��dd��", datStart)+"-"+DateUtil.Formatdate("yyyy��MM��dd��", datEnd),Table.ALIGN_CENTER);
	//	rt.setDefaultTitle(6,  2, strJihkjvalue+" "+strYunsfsvalue, Table.ALIGN_LEFT);
		rt.setDefaultTitle(7, 3,"��λ:��",Table.ALIGN_RIGHT); 
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 3, "�Ʊ�:"+((Visit) getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getLaimlField(){
		JDBCcon con = new JDBCcon();
		String laiml = SysConstant.LaimField;
		laiml="round_new(sum(fh.laimsl),0)";
		ResultSetList rs = con.getResultSetList("select * from xitxxb where mingc = 'ʹ�ü���' and zhuangt = 1 and zhi = '�й�����'");
		if(rs.next()){
			laiml = "sum(round_new(fh.laimsl,"+((Visit) getPage().getVisit()).getShuldec()+"))";
		}
		rs.close();
		con.Close();
		return laiml;
	}
	
	private String getPrintDataTz(){
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
//		String strDate1=DateUtil.FormatDate(datStart);//�����ַ�
//		String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
		StringBuffer sbsql = new StringBuffer();
//		sbsql.append("select decode(grouping(y.mingc)+grouping(fh.lieid),2,'�ܼ�',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Rucmslfkmx_zdt','vwgongys_id',y.id,y.mingc)) as mingc,\n");
//		sbsql.append(" sum(fh.ches) as ches,\n");
//		sbsql.append(" sum(round_new(fh.laimsl,0)) as laimsl, \n");
//		sbsql.append(" sum(round_new(fh.biaoz,0)) as biaoz, \n");
//		sbsql.append(" sum(round_new(fh.jingz,0)) as jingz, \n");
//		sbsql.append(" sum(round_new(fh.yuns,0)) as yuns, \n");
//		sbsql.append(" sum(round_new(fh.yingd,0)) as yingd, \n");
//		sbsql.append(" sum(round_new((fh.yingd-fh.yingk),0)) as kuid, \n" );
//		sbsql.append(" sum(round_new(fh.koud,0)) as koud  \n");
//		sbsql.append("           from fahb fh,vwdianc dc,vwgongys y   \n");
//		sbsql.append("     where fh.gongysb_id=y.id   \n");
//		sbsql.append("         and fh.diancxxb_id=dc.id      \n");
//		sbsql.append("         and fh.gongysb_id=y.id   \n");
//		sbsql.append(getCondtion());
//		sbsql.append(" group by rollup((fh.lieid,y.id,y.mingc)) order by grouping(fh.lieid) ,max(y.xuh),y.mingc");
	
		 sbsql.append("select decode(grouping(y.mingc),1,'�ܼ�',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Rucmslfkmx_zdt','vwgongys_id',y.id||','||max(sj.jihkjb_id),y.mingc)) as danwmc, \n");
		 
//		 sbsql.append("       sum(ches) as ches,sum(laiml) as laiml,sum(biaoz) as biaoz,sum(jingz) as jingz, \n");
//		 sbsql.append("       sum(yuns)as yuns,sum(yingd) as yingd, \n");
//		 sbsql.append("       sum(kuid) as kuid,sum(koud) as koud \n");
//		 sbsql.append("from  (select y.mingc,y.xuh, y.id, \n");
//		 sbsql.append("      sum(fh.ches) as ches, \n");
//		 sbsql.append(getLaimlField()+ " as laiml, \n");
//		 sbsql.append("         sum(round_new(fh.biaoz,0)) as biaoz,  \n");
//		 sbsql.append("         sum(round_new(fh.jingz,0)) as jingz,  \n");
//		 sbsql.append("         sum(round_new(fh.yuns,0)) as yuns,  \n");
//		 sbsql.append("         sum(round_new(fh.yingd,0)) as yingd,  \n");
//		 sbsql.append("         sum(round_new((fh.yingd-fh.yingk),0)) as kuid,  \n");
//		 sbsql.append("         sum(round_new(fh.koud,0)) as koud   \n");
//		 sbsql.append("       from fahb fh,vwdianc dc,vwgongys y    \n");
//		 sbsql.append("       where fh.gongysb_id=y.id    \n");
//		 sbsql.append("         and fh.diancxxb_id=dc.id       \n");
//		 sbsql.append("         and fh.gongysb_id=y.id    \n");
//		 sbsql.append(getCondtion());
//		 sbsql.append("         group by fh.lieid,y.id,y.mingc,y.xuh) \n");
		 
		 sbsql.append(" sum(round_new(sj.ches,0)) as ches,sum(round_new(sj.biaoz,0)) as biaoz,sum(round_new(sj.laimsl,0)) as laimsl,\n");
		 sbsql.append(" sum(round_new(sj.yuns,0)) as yuns,sum(round_new(sj.jingz,0)) as jingz, \n");
		 sbsql.append(" sum(round_new(sj.yingd,0)) as yingd,\n");
		 sbsql.append(" sum(round_new(sj.kuid,0)) as kuid, sum(round_new(sj.koud,0)) as koud \n");
		 sbsql.append(" from \n");
		 sbsql.append("       (select fh.diancxxb_id,fh.meikxxb_id, max(fh.jihkjb_id)jihkjb_id,sum(fh.ches) as ches,"+getLaimlField()+" as laimsl,round_new(sum(fh.biaoz),0) as biaoz,round_new(sum(fh.jingz),0) as jingz,  \n");
		 sbsql.append("              round_new(sum(fh.yuns),0) as yuns,round_new(sum(fh.yingd),0) as yingd, \n");
		 sbsql.append("              round_new(sum(fh.yingd-fh.yingk),0) as kuid, round_new(sum(fh.koud),0) as koud \n");
		 sbsql.append("       from fahb fh,vwdianc dc,vwgongys y  \n");
		 sbsql.append("       where fh.gongysb_id=y.id  \n");
		 sbsql.append(getCondtion());
		 sbsql.append("         and fh.diancxxb_id=dc.id    \n");
		 sbsql.append("         group by fh.diancxxb_id,fh.meikxxb_id,fh.lieid ) sj,vwdianc dc,meikxxb y  \n");
		 sbsql.append(" where  sj.diancxxb_id=dc.id and sj.meikxxb_id=y.id  \n");		 
		 
		 sbsql.append(" group by rollup((y.id,y.mingc)) \n");
		 sbsql.append(" order by grouping(y.mingc) ,max(y.xuh),y.mingc \n");
		 
		 
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="�볧ú������ϸ"+strTitle;
		
		// �����ͷ����
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		String ArrHeader[][]=new String[1][9];
		ArrHeader[0]=new String[] {"��λ","����","Ʊ��<br>(��)","ʵ����<br>(��)","����<br>(��)","����<br>(��)","ӯ��<br>(��)","����<br>(��)","�۶�<br>(��)"};
		int ArrWidth[]=new int[] {200,60,80,80,80,80,60,60,60};

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		  Visit visit=((Visit) getPage().getVisit());
		   String zhibdw=visit.getDiancqc();
		if(zhibdw.equals("��������ȼ�����޹�˾")&&visit.getRenyjb()==2){
			zhibdw="���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���";
		}
		rt.setDefaultTitle(1, 5, "�Ʊ�λ:"+ zhibdw,Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, DateUtil.Formatdate("yyyy��MM��dd��", datStart)+"��"+DateUtil.Formatdate("yyyy��MM��dd��", datEnd),Table.ALIGN_CENTER); 
		rt.setDefaultTitle(6,  2, "", Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
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
			visit.setString9("");
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString9().equals("")) {
        		leix = visit.getString9();
            }
        }
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