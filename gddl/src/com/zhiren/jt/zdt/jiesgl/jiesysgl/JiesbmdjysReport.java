package com.zhiren.jt.zdt.jiesgl.jiesysgl;

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
* 时间：2009-07-20
* 作者： ll
* 修改内容： 修改分矿结算标煤单价标题 
*/ 
/* 
* 时间：2009-07-27
* 作者： ll
* 修改内容：增加按入厂日期查询。
 		   
*/ 
/* 
* 时间：2009-09-4
* 作者： ll
* 修改内容：1、二级公司登陆时去“总计”行。
* 		   
*/ 
public class JiesbmdjysReport extends BasePage {
	public final static String LX_FC="fc";
	public final static String LX_FK="fk";
	public final static String LX_FKFC="fkfc";
	public final static String LX_FCFK="fcfk";
	public final static String LX_QP="qp";
	
	private String leix="fc";
	
	private String riq_lx="";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='报表标题单位名称'";
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
	
	private String getCondtion(){
		String strCondtion="";
		int intLen=0;
		long lngYunsfsId=-1;
		long lngJihkjId= -1;
		String datStart=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1());
		String datEnd=DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2());
		
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		
		String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
			
		
		if(riq_lx.equals("0")){//按结算日期查询
			strCondtion="and js.jiesrq>=to_date('"+datStart+"','yyyy-mm-dd') \n" +
			"and js.jiesrq<=to_date('"+datEnd+"','yyyy-mm-dd') \n" ;
			
		}else{//按入厂日期查询
			strCondtion="and yf.yansjzrq>=to_date('"+datStart+"','yyyy-mm-dd') \n" +
			"and yf.yansjzrq<=to_date('"+datEnd+"','yyyy-mm-dd') \n" ;
			
		}
		
		
		if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and js.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and js.jihkjb_id=" +lngJihkjId;
		}
		
		String lx=getLeix();
		intLen=lx.indexOf(",");
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				strCondtion=strCondtion+" and dc.id=" +pa[0];
				strCondtion=strCondtion+" and y.dqid=" +pa[1];
			}else{
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
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
		String strDiancxxb_id=""+((Visit) getPage().getVisit()).getDiancxxb_id();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String guolzj = "";
		if (jib==2){
			guolzj=" having grouping(dc.fgsmc)=0\n";//分公司查看报表时过滤总计。 
		}else if (jib==3){
			guolzj=" having grouping(dc.mingc)=0\n";
		}
		StringBuffer sbSelect=new StringBuffer();
		StringBuffer sbGroup=new StringBuffer();

		if (leix.equals(LX_FC)){
		
			sbSelect.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc, \n");
			sbGroup.append(" group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbGroup.append(" "+guolzj+"\n");
			sbGroup.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="(分厂)";
		}else if(leix.equals(LX_FK)){
			sbSelect.append("select decode(grouping(y.smc)+grouping(y.dqmc),2,'总计',1,y.smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||y.dqmc) as danwmc,");
			sbGroup.append(" group by rollup(y.smc,y.dqmc)   \n");
			sbGroup.append(" order by grouping(y.smc) desc,max(y.sxh) ,y.smc, \n");
			sbGroup.append("          grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc \n");
			
			strTitle="(分矿)";
		}else if(leix.equals(LX_FCFK)){
			sbSelect.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(y.dqmc),3,'总计',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||y.dqmc) as danwmc,");
			sbGroup.append("group by rollup(dc.fgsmc,dc.mingc,y.dqmc)   \n");
			sbGroup.append(" "+guolzj+"\n");
			sbGroup.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbGroup.append("          grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc \n");
			strTitle="(分厂分矿)";
		}else if(leix.equals(LX_FKFC)){
			if(jib==2){
				sbSelect.append("select decode(grouping(dc.mingc)+grouping(y.dqmc),2,'总计',1,y.dqmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc,");
				sbGroup.append(" group by rollup(y.dqmc,dc.mingc)   \n");
				sbGroup.append(" order by  grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc, \n");
				sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			}else{
				sbSelect.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(y.dqmc),3,'总计',2,y.dqmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc,");
				sbGroup.append(" group by rollup(y.dqmc,dc.fgsmc,dc.mingc)   \n");
				sbGroup.append(" order by  grouping(y.dqmc) desc,max(y.dqxh) ,y.dqmc, \n");
				sbGroup.append("          grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
				sbGroup.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			}
			strTitle="(分矿分厂)";
		}else if(leix.equals(LX_QP)){
			strTitle="(棋盘)";
		}else if(leix.indexOf(",")>0){
			return getPrintDataTz();
		}


		sbSqlBody.append("select danwmc,jiessl,round_new(farl_dk/1000*4.1816,2) as far,farl_dk, \n");
		sbSqlBody.append("   hanszhj as daoczhj,hansdj,yunf,zaf, \n");
		sbSqlBody.append("   round_new (hanszhj*7000/farl_dk,2) as hansbmdj, \n");
		sbSqlBody.append("   round_new ((hanszhj-meis-yunfs)*7000/farl_dk,2) as buhsbmdj \n");
		sbSqlBody.append("from( \n");
		sbSqlBody.append(sbSelect).append("\n"); 
		sbSqlBody.append("     sum(nvl(sj.jiessl,0)) as jiessl,    \n");
		sbSqlBody.append("     decode(sum(sj.jiessl),0,0,round_new(sum(farl*jiessl)/sum(jiessl),0)) farl_dk, \n");
		sbSqlBody.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk+yunf+zaf)/sum(jiessl),2)) hanszhj, \n");
		sbSqlBody.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk)/sum(jiessl),2)) hansdj, \n");
		sbSqlBody.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunf)/sum(jiessl),2)) yunf, \n");
		sbSqlBody.append("     decode(sum(sj.jiessl),0,0,round_new(sum(zaf)/sum(jiessl),2)) zaf, \n");
		sbSqlBody.append("     decode(sum(sj.jiessl),0,0,round_new(sum(meis)/sum(jiessl),2)) meis, \n");
		sbSqlBody.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunfs)/sum(jiessl),2)) yunfs \n");
		sbSqlBody.append("  from (select js.diancxxb_id,js.gongysb_id,  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbSqlBody.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbSqlBody.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbSqlBody.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbSqlBody.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbSqlBody.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbSqlBody.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbSqlBody.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbSqlBody.append("           and js.diancxxb_id=dc.id  \n");
		sbSqlBody.append("           and js.gongysb_id=y.id    \n");
		sbSqlBody.append(getCondtion());
		sbSqlBody.append("           )sj,vwdianc dc ,vwgongys y \n");
		sbSqlBody.append("   where sj.diancxxb_id=dc.id and sj.gongysb_id=y.id    \n");
		sbSqlBody.append(sbGroup);
		sbSqlBody.append(") \n");

		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName="结算标煤单价情况"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbSqlBody.toString());
		
		String ArrHeader[][]=new String[2][9];
		ArrHeader[0]=new String[] {"单位","结算煤量","结算热值","结算热值","综合价","煤价","运费","杂费","标煤单价(元/吨)","标煤单价(元/吨)"};
		ArrHeader[1]=new String[] {"单位","(吨)","(MJ/kg)","(Kcal/kg)","(元/吨)","&nbsp;(元/吨)","(元/吨)","&nbsp;(元/吨)","含税","不含税"};
		int ArrWidth[]=new int[] {150,80,80,60,60,60,60,60,60};

		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 4, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(5,  3, "", Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(36);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getPrintDataTz(){
		StringBuffer sbsql = new StringBuffer();

		sbsql.append(" select danwmc,jiesdw,jiesrq,jiessl,round_new(farl_dk/1000*4.1816,2) as far,farl_dk, \n");
		sbsql.append("   hanszhj as daoczhj,hansdj,yunf,zaf, \n");
		sbsql.append("   round_new (hanszhj*7000/farl_dk,2) as hansbmdj, \n");
		sbsql.append("   round_new ((hanszhj-meis-yunfs)*7000/farl_dk,2) as buhsbmdj \n");
		sbsql.append("from(select decode(grouping(y.mingc)+grouping(sj.id),2,'总计',y.mingc) as danwmc,  max(sj.shoukdw) as jiesdw, \n");
		sbsql.append("      decode(grouping(y.mingc)+grouping(sj.id),2,'',1,'小计',to_char(sj.jiesrq,'yyyy-mm-dd')) as jiesrq,  \n");
		sbsql.append("     sum(nvl(sj.jiessl,0)) as jiessl,    \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(farl*jiessl)/sum(jiessl),0)) farl_dk, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk+yunf+zaf)/sum(jiessl),2)) hanszhj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(hansmk)/sum(jiessl),2)) hansdj, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunf)/sum(jiessl),2)) yunf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(zaf)/sum(jiessl),2)) zaf, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(meis)/sum(jiessl),2)) meis, \n");
		sbsql.append("     decode(sum(sj.jiessl),0,0,round_new(sum(yunfs)/sum(jiessl),2)) yunfs \n");
		sbsql.append(" from( select  js.id as id,y.id as yid,dc.id as did,js.shoukdw as shoukdw,js.jiesrq as jiesrq,  js.jiessl, getjiesfarl(js.id) as farl, \n");
		sbsql.append("               (nvl(js.hansmk,0)+nvl(js.bukmk,0)) as hansmk,    \n");
		sbsql.append("               (nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0)) as yunf,    \n");
		sbsql.append("               (nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0)) as zaf,    \n");
		sbsql.append("               (nvl(js.shuik,0)) as meis,    \n");
		sbsql.append("               (nvl(yf.shuik,0)) as yunfs         \n");
		sbsql.append("          from jiesb js,jiesyfb yf,vwdianc dc, vwgongys y \n");
		sbsql.append("         where js.id=yf.diancjsmkb_id(+)  \n");
		sbsql.append("           and js.diancxxb_id=dc.id  \n");
		sbsql.append("           and js.gongysb_id=y.id    \n");
		sbsql.append( getCondtion());
		sbsql.append("   ) sj,vwdianc dc, vwgongys y  \n");
		sbsql.append("  where sj.yid=y.id and sj.did=dc.id  \n");
		sbsql.append("   group by rollup(y.mingc,(sj.id,jiesrq))  \n");
		sbsql.append("  order by grouping(y.mingc) ,max(y.xuh),y.mingc,grouping(sj.id),jiesrq  \n");
		sbsql.append(") \n");
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="结算标煤单价明细"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][]=new String[2][12];
		ArrHeader[0]=new String[] {"发货单位","结算单位","结算日期","结算煤量","结算热值","结算热值","综合价","煤价","运费","杂费","标煤单价(元/吨)","标煤单价(元/吨)"};
		ArrHeader[1]=new String[] {"发货单位","结算单位","结算日期","(吨)","(MJ/kg)","(Kcal/kg)","(元/吨)","&nbsp;(元/吨)","(元/吨)","&nbsp;(元/吨)","含税","不含税"};
		int ArrWidth[]=new int[] {150,150,80,80,60,60,60,60,60,60};
		
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 4, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(5,  3, "", Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(48);
		rt.body.setUseDefaultCss(true);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

		// 设置页数
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
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		leix = visit.getString9();
            }
        }
		if(cycle.getRequestContext().getParameters("riq_lx") !=null) {
			visit.setString10((cycle.getRequestContext().getParameters("riq_lx")[0]));
			riq_lx = visit.getString10();
        }else{
        	if(!visit.getString1().equals("")) {
        		riq_lx = visit.getString10();
            }
        }
	}

	// 供应商
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
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
	}

	// 类型
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
		list.add(new IDropDownBean(1, "分厂"));
		list.add(new IDropDownBean(2, "分矿"));
		list.add(new IDropDownBean(3, "棋盘表"));
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

	// 电厂名称
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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