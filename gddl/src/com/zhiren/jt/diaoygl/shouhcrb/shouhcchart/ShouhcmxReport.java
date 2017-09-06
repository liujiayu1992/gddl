package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;

/* 
* ʱ�䣺2009-08-13
* ���ߣ� ll
* �޸����ݣ�1��ҳ����ʾ���ӿ���������
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class ShouhcmxReport extends BasePage {

	public final static String LX_DW="dw";
	public final static String LX_KC="kc";
	
	private String leix="fc";
	private String lx="";
	
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

	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	private String getCondtion(){
		String strCondtion="";
		int intLen=0;
		Date datStart=((Visit)getPage().getVisit()).getDate2();
		Date datEnd=((Visit)getPage().getVisit()).getDate1();
		
		
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		
		String strDate1=DateUtil.FormatDate(datStart);//�����ַ�
		String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
		
		strCondtion="and riq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
			"and riq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
		
		String lx=getLx();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				strCondtion=strCondtion+" and d.id=" +pa[0];
			}else{
				strCondtion=" and d.id=-1";
			}
		}else{
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
	
	public String getLx(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}
	
	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString10().equals("")) {
			return visit.getString10();
        } 
		return ""; 
	}
	
	
	private String getPrintData(){
		if (getLeix().equals("")){
			return "�޴˱���";
		}else{
			if (getLeix().equals("dc")){
				return getPrintDc();
			}else if (getLeix().equals("kc")){
				return getPrintKc();
			}
		}
		return "";
	}
	
	private String getPrintDc(){//�糧����
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq1=DateUtil.FormatDate(getBeginriqDate());
		String riq2=DateUtil.FormatDate(getEndriqDate());
		 
		sbsql.append("select  d.mingc as danw,decode(riq,null,'С��',to_char(riq,'yyyy-mm-dd')) as riq,sum(dangrgm) as dangrgm,sum(fady+gongry+qity+yuns+cuns) as hej,sum(fady) as fady,sum(gongry) as gongry, \n");
		sbsql.append("sum(qity) as qity,sum(yuns) as yuns,sum(cuns) as chus,sum(shuifctz) as shuifctz, \n");
		sbsql.append("sum(panyk) as panyk,sum(tiaozl) as tiaozl,decode(riq,null,'',sum(kuc)) as kuc \n");
		sbsql.append(",decode(riq,null,'',round_new(sum(nvl(kuc,0))/sum( keyts_rijhm_new(d.id,(fady+gongry+qity+yuns+cuns),riq,3)),1))  as keyts  \n");
		sbsql.append("from shouhcrbb s,diancxxb d where d.id=s.diancxxb_id  \n");
		sbsql.append(getCondtion());
		sbsql.append(" group by rollup(d.mingc,riq) \n");
		sbsql.append("having not grouping(d.mingc)=1 \n");
		sbsql.append("order by grouping(d.mingc),max(d.xuh),d.mingc,riq ");
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[2][14];
		 ArrHeader[0]=new String[] {"��λ","����","�볧ú��","����","����","����","����","����","����","ˮ�ֲ����","��ӯ��","������","���","��������"};
		 ArrHeader[1]=new String[] {"��λ","����","�볧ú��","�ϼ�","����","����","������","����","����","ˮ�ֲ����","��ӯ��","������","���","��������"};

		 int ArrWidth[]=new int[] {130,75,75,75,75,75,60,60,60,75,60,60,75,60};

		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		rt.body.setPageRows(22);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//�ϲ���
		rt.body.mergeFixedCols();//�Ͳ���
		rt.setTitle("�պĴ���ϸ", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(5, 3,riq1+"��"+riq2,Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
//		if (rt.body.getRows()>2){
//			rt.body.setCellValue(rt.body.getRows(), rt.body.getCols(), "");
//		}
		rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "����ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 2, "��ˣ�", Table.ALIGN_LEFT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getPrintKc(){//�������
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq2=DateUtil.FormatDate(getEndriqDate());
		
		Date datEnd=((Visit)getPage().getVisit()).getDate1();
		String strDate2=DateUtil.FormatDate(datEnd);//�����ַ�
		
		
		String lx=getLx();
		int intLen=lx.indexOf(",");
		String fgsid="";
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				fgsid=" and dc.fgsid=" +pa[0];
			}else{
				fgsid=" and dc.fgsid=-1 ";
			}
		}
		 
		sbsql.append("select  d.mingc as danw,to_char(riq,'yyyy-mm-dd') as riq,hc.kuc,d.jingjcml,d.xianfhkc \n");
		sbsql.append("from shouhcrbb hc,diancxxb d,vwdianc dc where hc.diancxxb_id(+)=d.id and d.id=dc.id \n");
		sbsql.append("and riq=to_date('"+strDate2+"','yyyy-mm-dd') ").append(fgsid);
		sbsql.append(" order by d.xuh \n");
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[1][5];
		 ArrHeader[0]=new String[] {"��λ","����","���","������","�޸��ɿ��"};

		 int ArrWidth[]=new int[] {130,80,75,75,75};

		Table bt=new Table(rs,1,0,2);
		rt.setBody(bt);
		//bt.setColAlign(2, Table.ALIGN_CENTER);
		
//		if(rt.body.getRows()>2){
//			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
//		}
		rt.body.setPageRows(22);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		//rt.body.mergeFixedRow();//�ϲ���
		//rt.body.mergeFixedCols();//�Ͳ���
		rt.setTitle("�����ϸ", ArrWidth);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(5, 3,riq2,Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "����ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 2, "��ˣ�", Table.ALIGN_LEFT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
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
		blnIsBegin = true;
		//����������ͣ�dc �糧��λ��kc ���������
		if(cycle.getRequestContext().getParameters("leix") !=null) {
			visit.setString10((cycle.getRequestContext().getParameters("leix")[0]));
			leix = visit.getString10();
        }else{
        	if(!visit.getString10().equals("")) {
        		leix = visit.getString10();
            }
        }
		//��������
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			lx = visit.getString9();
        }else{
        	if(!visit.getString9().equals("")) {
        		lx = visit.getString9();
            }
        }
	}

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib(String DiancTreeJib) {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		
		String sqlJib = "select d.jib from diancxxb d where d.id="			+ DiancTreeJib;
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