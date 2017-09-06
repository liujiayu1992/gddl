package  com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
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

public class ShouhcyrbReport  extends BasePage {
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

		JDBCcon con = new JDBCcon();
		String sqlJib = "select zhi from xitxxb x where mingc ='���պĴ��ձ��Ƿ��Ʒ��' and x.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		ResultSet rs = con.getResultSet(sqlJib.toString());
		String zhi="";
		try {
			if (rs.next()) {
				zhi = rs.getString("zhi");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		if(zhi.equals("1")){//��Ʒ��
			return getZhiltj();
		}else{//����Ʒ��
			return getZhiltj_bfpz();
		}

	}
	// ��ͬ���ֳ��ֿ�ֿ�ֳ�ͳ�Ʊ���
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=getBeginriqDate();
		String riq2=getEndriqDate();
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and y.diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ;
		String diancCondition1=
			" where dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		
		 
//		sbsql.append("(select 1 as xuh,decode(1,1,'����') as fenx from dual \n");
//		sbsql.append("union \n");
//		sbsql.append("select 2 as xuh,decode(1,1,'�ۼ�') as fenx from dual) fx, \n");
//		-------------------��Ʒ�ַ����Ʒ��С��-------------------------------
		sbsql.append("(select dc.id,dc.mingc,dc.xuh,dc.fuid,gs.mingc as gsmc,gs.xuh as gsxh,p.id as pzid,p.mingc as pzmc,vwfenx.fenx \n");
		sbsql.append("from vwfenx,diancxxb dc,pinzb p,vwfengs gs \n");
		sbsql.append(diancCondition1);
        sbsql.append("and gs.id=dc.fuid and p.leib='��' ) fx,\n");
//		------------------------------------------------------------------
		sbsql.append("(select y.diancxxb_id,y.pinzb_id,decode(1,1,'����') as fenx, \n");
		sbsql.append("Zuorykc(diancxxb_id,to_date('"+riq2+"','yyyy-mm-dd')) as zuorkc ,sum(nvl(shourl,0)) as shourl, \n");
		sbsql.append("sum(fady+gongry+qity) as haoyhj,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("sum(cuns) as cuns,sum(panyk) as panyk,sum(kuc) as kuc from shouhcrbyb y \n");
		sbsql.append("where y.riq=to_date('"+riq2+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("group by diancxxb_id,y.pinzb_id \n");
		sbsql.append("union \n");
		sbsql.append("select y.diancxxb_id,y.pinzb_id,decode(1,1,'�ۼ�') as fenx, \n");
		sbsql.append("0 as zuorkc ,sum(shourl) as shourl,sum(fady+gongry+qity) as haoyhj, \n");
		sbsql.append("sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("sum(cuns) as cuns,sum(panyk) as panyk,0 as kuc from shouhcrbyb y \n");
		sbsql.append("where y.riq<=to_date('"+riq2+"','yyyy-mm-dd') \n");
		sbsql.append("and y.riq>=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("group by diancxxb_id,y.pinzb_id) shul \n");
		String sbsqlz=sbsql.toString();
        sbsql.setLength(0);
		if(jib==3){
//			sbsql.append("select dc.mingc as danw , \n");
//			sbsql.append("p.mingc as pinz,fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
//			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
//			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
//			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
//			sbsql.append("from diancxxb dc,pinzb p,vwfengs gs, \n");
//			sbsql.append(sbsqlz);
//			sbsql.append("where dc.id=shul.diancxxb_id and fx.fenx=shul.fenx (+) \n");
//			sbsql.append("and shul.pinzb_id=p.id and gs.id=dc.fuid \n");
//			sbsql.append("group by rollup(p.mingc,fx.fenx, gs.mingc,dc.mingc) \n");
//			sbsql.append("having not( grouping(dc.mingc)=1) \n");
//			sbsql.append("order by grouping(gs.mingc)desc,min(gs.xuh),gs.mingc, \n");
//			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh) ,dc.mingc,p.mingc,fx.fenx \n");
			sbsql.append("select fx.mingc as danw , \n");
			sbsql.append("decode(grouping(fx.pzmc),1,decode(fx.mingc,null,fx.pzmc,'С��'),fx.pzmc) as pinz, \n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc,\n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from  \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx (+) \n");
			sbsql.append("and fx.pzid =shul.pinzb_id(+)\n");
			sbsql.append("group by grouping sets (fx.fenx,(fx.pzmc,fx.fenx),(fx.mingc,fx.fenx),(fx.mingc,fx.pzmc,fx.fenx))\n");
			sbsql.append("having not( grouping(fx.mingc)=1)  \n");
			sbsql.append("order by grouping(fx.mingc) desc,min(fx.xuh) ,fx.mingc,grouping(fx.pzmc)desc,fx.pzmc,fx.fenx  \n");
		}else if(jib==2){
//			sbsql.append("select decode(grouping(gs.mingc)+grouping(dc.mingc),1,gs.mingc,'&nbsp&nbsp'||dc.mingc) as danw , \n");
//			sbsql.append("p.mingc as pinz,fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
//			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
//			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
//			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
//			sbsql.append("from diancxxb dc,pinzb p,vwfengs gs, \n");
//			sbsql.append(sbsqlz);
//			sbsql.append("where dc.id=shul.diancxxb_id and fx.fenx=shul.fenx (+) \n");
//			sbsql.append("and shul.pinzb_id=p.id and gs.id=dc.fuid \n");
//			sbsql.append("group by rollup(p.mingc,fx.fenx,gs.mingc,dc.mingc) \n");
//			sbsql.append("having not(grouping(gs.mingc)=1 )\n");
//			sbsql.append("order by grouping(gs.mingc)desc,min(gs.xuh),gs.mingc, \n");
//			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh) ,dc.mingc,p.mingc,fx.fenx \n");
			sbsql.append("select decode(grouping(fx.gsmc)+grouping(fx.mingc),1,fx.gsmc,'&nbsp&nbsp'||fx.mingc) as danw , \n");
			sbsql.append("decode(grouping(fx.gsmc)+grouping(fx.mingc)+grouping(fx.pzmc),3,'С��',2,decode(fx.gsmc,null,fx.pzmc,'С��'),1,decode(fx.mingc,null,fx.pzmc,'С��'),fx.pzmc) as pinz,\n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from  \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx(+) and  fx.pzid=shul.pinzb_id(+)  \n");
			sbsql.append("group by grouping sets (fx.fenx,(fx.gsmc,fx.fenx),(fx.pzmc,fx.fenx),(fx.gsmc,fx.pzmc,fx.fenx),(fx.gsmc,fx.mingc,fx.fenx),(fx.gsmc,fx.mingc,fx.pzmc,fx.fenx))\n");
			sbsql.append("having not(grouping(fx.gsmc)=1 )\n");
			sbsql.append("order by grouping(fx.gsmc)desc,max(fx.gsxh),fx.gsmc,grouping(fx.mingc) desc,max(fx.xuh) ,fx.mingc,grouping(fx.pzmc)desc,fx.pzmc,fx.fenx ");
		}else{
//			sbsql.append("select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'�ܼ�',1,gs.mingc,'&nbsp&nbsp'||dc.mingc) as danw , \n");
//			sbsql.append("p.mingc as pinz,fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
			sbsql.append("select  decode(grouping(fx.gsmc)+grouping(fx.mingc),2,'�ܼ�',1,fx.gsmc,'&nbsp&nbsp'||fx.mingc) as danw,\n");
			sbsql.append("decode(grouping(fx.gsmc)+grouping(fx.mingc)+grouping(fx.pzmc),3,'С��',2,decode(fx.gsmc,null,fx.pzmc,'С��'),1,decode(fx.mingc,null,fx.pzmc,'С��'),fx.pzmc) as pinz,\n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc,\n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx (+) \n");
			sbsql.append("and shul.pinzb_id(+)=fx.pzid \n");
//			sbsql.append("group by rollup(p.mingc,fx.fenx,gs.mingc,dc.mingc) \n");
//			sbsql.append("having not(grouping(fx.fenx)=1) \n");
//			sbsql.append("order by grouping(gs.mingc)desc,min(gs.xuh),gs.mingc, \n");
//			sbsql.append("grouping(dc.mingc) desc,min(dc.xuh) ,dc.mingc,p.mingc,fx.fenx \n");
			sbsql.append("group by grouping sets (fx.fenx,(fx.gsmc,fx.fenx),(fx.pzmc,fx.fenx),(fx.gsmc,fx.pzmc,fx.fenx),(fx.gsmc,fx.mingc,fx.fenx),(fx.gsmc,fx.mingc,fx.pzmc,fx.fenx))\n");
			sbsql.append(" having not (grouping(fx.fenx)=1 or (grouping(fx.gsmc)=1 and grouping(fx.mingc)<>1)) \n");
			sbsql.append("order by grouping(fx.gsmc) desc,max(fx.gsxh),fx.gsmc,grouping(fx.mingc) desc,max(fx.xuh),fx.mingc desc,fx.pzmc desc ,fx.fenx\n");

		}
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		

		 String ArrHeader[][]=new String[2][12];
		 ArrHeader[0]=new String[] {"��λ","����","����","���տ��","�볧����","����","����","����","����","���","��ӯ��","���"};
		 ArrHeader[1]=new String[] {"��λ","����","����","���տ��","�볧����","�ϼ�","����","����","������","���","��ӯ��","���"};

		 int ArrWidth[]=new int[] {150,60,60,54,54,54,54,54,54,54,54,54};
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			Table bt=new Table(rs,2,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
			//
//			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//�ϲ���
			rt.body.mergeFixedCols();//�Ͳ���
			rt.setTitle( "���պĴ��ձ�", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(12, 2, "��λ:(��)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(5, 2,FormatDate(DateUtil.getDate(riq2)),Table.ALIGN_CENTER);
			rt.body.setPageRows(rt.PAPER_ROWS);
//			���ӳ��ȵ�����
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(9, 2, "���:", Table.ALIGN_CENTER);
			if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
				
				rt.setDefautlFooter(11, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(11, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

		(),Table.ALIGN_RIGHT);
				}
//			rt.setDefautlFooter(11, 2, "�Ʊ�:", Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	
	
	
	private String getZhiltj_bfpz() {//����Ʒ��
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=getBeginriqDate();
		String riq2=getEndriqDate();
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and y.diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ;
		String diancCondition1=
			" where dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		
		 
		sbsql.append("(select dc.*,1 as xuh1,decode(1,1,'����') as fenx from vwdianc dc "+diancCondition1+" \n");
		sbsql.append("union \n");
		sbsql.append("select dc.*,2 as xuh1,decode(1,1,'�ۼ�') as fenx from vwdianc dc "+diancCondition1+") fx, \n");
		
		sbsql.append("(select y.diancxxb_id,y.pinzb_id,decode(1,1,'����') as fenx, \n");
		sbsql.append("Zuorykc(diancxxb_id,to_date('"+riq2+"','yyyy-mm-dd')) as zuorkc ,sum(nvl(shourl,0)) as shourl, \n");
		sbsql.append("sum(fady+gongry+qity) as haoyhj,sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("sum(cuns) as cuns,sum(panyk) as panyk,sum(kuc) as kuc from shouhcrbyb y \n");
		sbsql.append("where y.riq=to_date('"+riq2+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("group by diancxxb_id,y.pinzb_id \n");
		sbsql.append("union \n");
		sbsql.append("select y.diancxxb_id,y.pinzb_id,decode(1,1,'�ۼ�') as fenx, \n");
		sbsql.append("0 as zuorkc ,sum(shourl) as shourl,sum(fady+gongry+qity) as haoyhj, \n");
		sbsql.append("sum(fady) as fady,sum(gongry) as gongry,sum(qity) as qity, \n");
		sbsql.append("sum(cuns) as cuns,sum(panyk) as panyk,0 as kuc from shouhcrbyb y \n");
		sbsql.append("where y.riq<=to_date('"+riq2+"','yyyy-mm-dd') \n");
		sbsql.append("and y.riq>=to_date('"+riq+"','yyyy-mm-dd') \n");
		sbsql.append(diancCondition);
		sbsql.append("group by diancxxb_id,y.pinzb_id) shul \n");
		String sbsqlz=sbsql.toString();
        sbsql.setLength(0);
		if(jib==3){
			sbsql.append("select fx.mingc as danw , \n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from  \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,fx.fgsmc,fx.mingc)  \n");
			sbsql.append("having not(grouping(fx.fgsmc)=1 )\n");
			sbsql.append("order by grouping(fx.fgsmc)desc,min(fx.fgsxh),fx.fgsmc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh) ,fx.mingc,fx.fenx \n");
			
		}else if(jib==2){
			sbsql.append("select decode(grouping(fx.fgsmc)+grouping(fx.mingc),1,fx.fgsmc,'&nbsp&nbsp'||fx.mingc) as danw , \n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from  \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,fx.fgsmc,fx.mingc)  \n");
			sbsql.append("having not(grouping(fx.fgsmc)=1 )\n");
			sbsql.append("order by grouping(fx.fgsmc)desc,min(fx.fgsxh),fx.fgsmc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh) ,fx.mingc,fx.fenx \n");
			
		}else{
			sbsql.append("select decode(grouping(fx.fgsmc)+grouping(fx.mingc),2,'�ܼ�',1,fx.fgsmc,'&nbsp&nbsp'||fx.mingc) as danw , \n");
			sbsql.append("fx.fenx,sum(shul.zuorkc) as zuorkc, \n");
			sbsql.append("sum(shul.shourl) as shourl,sum(shul.haoyhj) as haoyhj,sum(shul.fady) as fady, \n");
			sbsql.append("sum(shul.gongry) as gongry,sum(shul.qity) as qity,sum(shul.cuns) as cuns, \n");
			sbsql.append("sum(shul.panyk) as panyk,sum(shul.kuc) as kuc  \n");
			sbsql.append("from \n");
			sbsql.append(sbsqlz);
			sbsql.append("where fx.id=shul.diancxxb_id(+) and fx.fenx=shul.fenx(+) \n");
			sbsql.append("group by rollup(fx.fenx,fx.fgsmc,fx.mingc)  \n");
			sbsql.append("having not(grouping(fx.fgsmc)=1 )\n");
			sbsql.append("order by grouping(fx.fgsmc)desc,min(fx.fgsxh),fx.fgsmc, \n");
			sbsql.append("grouping(fx.mingc) desc,min(fx.xuh) ,fx.mingc,fx.fenx \n");

		}
			
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		

		 String ArrHeader[][]=new String[2][11];
		 ArrHeader[0]=new String[] {"��λ","����","���տ��","�볧����","����","����","����","����","���","��ӯ��","���"};
		 ArrHeader[1]=new String[] {"��λ","����","���տ��","�볧����","�ϼ�","����","����","������","���","��ӯ��","���"};

		 int ArrWidth[]=new int[] {120,54,54,54,54,54,54,54,54,54,54};
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
			Table bt=new Table(rs,2,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
			}
			//
//			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
//			���ӳ��ȵ�����
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.mergeFixedRow();//�ϲ���
			rt.body.mergeFixedCols();//�Ͳ���
			rt.setTitle( "���պĴ��ձ�", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 3, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 5, FormatDate(DateUtil.getDate(riq))+"-"+FormatDate(DateUtil.getDate(riq2)),Table.ALIGN_CENTER);
			rt.setDefaultTitle(10, 2, "��λ:��" ,Table.ALIGN_RIGHT);
			
			rt.body.setPageRows(rt.PAPER_ROWS);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(6, 2, "��ˣ�", Table.ALIGN_CENTER);
			if(((Visit) getPage().getVisit()).getDiancmc().equals("�ӱ�����")){
				
				rt.setDefautlFooter(10, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(10, 2, "�Ʊ�:"+((Visit) getPage().getVisit()).getDiancmc

		(),Table.ALIGN_RIGHT);
				}
//			rt.setDefautlFooter(10, 2, "�Ʊ�:",Table.ALIGN_RIGHT);
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
	///////

//	��ʼ����v
	private boolean _BeginriqChange=false;
	/*public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_BeginriqChange=true;
		}
	}*/

	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
//				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date())-1, 1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
//			Calendar stra=Calendar.getInstance();
//			stra.set(DateUtil.getYear(new Date()), 0, 1);
//			stra.setTime(new Date());
//			stra.add(Calendar.DATE,-1);
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("��ѯ����:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(80);
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("��"));
		tb1.addText(new ToolbarText("-"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
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
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			
			this.setTreeid(null);
			this.getTreeid();
			visit.setString4(null);
			visit.setString5(null);
			
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
			this.setBeginriqDate(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj()));
		}
		getToolbars();

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
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
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
//		�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
//		�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
//		 �ֹ�˾������
		private boolean _fengschange = false;

		public IDropDownBean getFengsValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getFengsModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean4();
		}

		public void setFengsValue(IDropDownBean Value) {
			if (getFengsValue().getId() != Value.getId()) {
				_fengschange = true;
			}
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
	
//		�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
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
