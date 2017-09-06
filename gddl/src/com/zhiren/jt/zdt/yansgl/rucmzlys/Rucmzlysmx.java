////2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   明细数据显示

/*2009-08-29 chh 
*修改内容 :问题：计划口径不是全部时没有数据        
*/
/*
 * 作者：songy
 * 时间：2011-7-20
 * 描述：增加从煤矿级别开始按口径分组
 */
/*
 * 作者：夏峥
 * 时间：2011-08-26
 * 描述：修正getLaimlField()方法中对来煤数量的取值方式。
 */
package com.zhiren.jt.zdt.yansgl.rucmzlys;

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


public class Rucmzlysmx extends BasePage {
	public final static String LX_FC="fc";
	public final static String LX_FK="fk";
	public final static String LX_FKFC="fkfc";
	public final static String LX_FCFK="fcfk";
	public final static String LX_QP="qp";
	
	private String leix="fc";
	private String kouj="-1";
	
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
		return getPrintDataTz();
	}

	private String getCondtion(){
		String strCondtion="";
		int intLen=0;
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		long lngYunsfsId=-1;
		if  (((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean3().getId();
		}
		
		long lngJihkjId= -1;
		if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
			lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
		}
		
		String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
		int jib=getDiancTreeJib(strDiancxxb_id);
		String strGongys_id=((Visit) getPage().getVisit()).getString3();
		
		String strDate1=DateUtil.FormatDate(datStart);//日期字符
		String strDate2=DateUtil.FormatDate(datEnd);//日期字符
		
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
				strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;// +" and y.id="+leix+" ";
			}else if (jib==-1){
				strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
			}
		}
		return strCondtion;
	}
	
	private String getLaimlField(){
		JDBCcon con = new JDBCcon();
		String laiml = SysConstant.LaimField;
		laiml="round_new(sum(fh.laimsl),0)";
		ResultSetList rs = con.getResultSetList("select * from xitxxb where mingc = '使用集团' and zhuangt = 1 and zhi = '中国大唐'");
		if(rs.next()){
			laiml = "sum(round_new(fh.laimsl,"+((Visit) getPage().getVisit()).getShuldec()+"))";
		}
		rs.close();
		return laiml;
	}
	private String getPrintDataTz(){
		StringBuffer sbsql = new StringBuffer();
		Date datStart=((Visit)getPage().getVisit()).getDate1();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
//		sbsql.append(" select decode(grouping(y.mingc)+grouping(fh.lieid),2,'总计',y.mingc) as mingc,z.huaybh, \n");
//		sbsql.append(" decode(grouping(y.mingc)+grouping(fh.lieid),2,'',1,'小计',to_char(max(fh.daohrq),'yyyy-mm-dd')) as daohrq, \n");
//		sbsql.append(" max(to_char(z.huaysj,'yyyy-mm-dd'))  as huaysj,  \n");
//		sbsql.append(" sum(fh.ches) as ches, \n");
//		sbsql.append(" "+SysConstant.LaimField+"  as laimsl,  \n");
//		sbsql.append("  round(decode(sum(nvl(z.qnet_ar,0)),0,0,"+SysConstant.LaimField+" )) as laimzl, \n");
//		sbsql.append(" decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
//		sbsql.append("        round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/  \n");
//		sbsql.append("        sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,      \n");
//		sbsql.append(" round_new(decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
//		sbsql.append("        round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/  \n");
//		sbsql.append("        sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getFarldec()+")/0.0041816),0) as farl,      \n");
//		sbsql.append(" decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
//		sbsql.append("        round_new(sum(round_new(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+"))/  \n");
//		sbsql.append("        sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getMtdec()+")) as mt,    \n");
//		sbsql.append(" decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
//		sbsql.append("        round_new(sum(round_new(fh.laimzl,0)*round_new(z.vdaf,2))/  \n");
//		sbsql.append("        sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as vdaf,   \n");
//		sbsql.append(" decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
//		sbsql.append("        round_new(sum(round_new(fh.laimzl,0)*round_new(z.ad,2))/  \n");
//		sbsql.append("        sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as ad,   \n");
//		sbsql.append(" decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
//		sbsql.append("        round_new(sum(round_new(fh.laimzl,0)*round_new(z.std,2))/  \n");
//		sbsql.append("        sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as std   \n");
//		sbsql.append(" from fahb fh,zhilb z,vwdianc dc,vwgongys y    \n");
//		sbsql.append(" where fh.gongysb_id=y.id    \n");
//		sbsql.append(" and fh.diancxxb_id=dc.id       \n");
//		sbsql.append(" and fh.zhilb_id=z.id \n");
//		sbsql.append(" and fh.gongysb_id=y.id  \n");
//		sbsql.append(getCondtion());
//		sbsql.append(" group by rollup((y.mingc,fh.lieid),z.huaybh) \n");
//		sbsql.append("  having not (grouping(y.mingc) || grouping(z.huaybh)) =1 \n");
//		sbsql.append(" order by grouping(y.mingc),max(y.xuh),y.mingc,max(to_char(z.huaysj,'yyyy-mm-dd')) \n");
		
		
		sbsql.append("select decode(grouping(y.mingc),1,'总计',y.mingc) as mingc, huaybh, \n");
		sbsql.append("       to_char(max(fh.daohrq),'yyyy-mm-dd') as daohrq,  \n");
		sbsql.append("       max(to_char(fh.huaysj,'yyyy-mm-dd'))  as huaysj, \n");
		sbsql.append("       sum(ches) as ches,  \n");
		sbsql.append("       sum(laiml) as laimsl,  \n");
		sbsql.append("       sum(laimzl) as jianzsl,  \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*farl)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,  \n");
		sbsql.append("       round_new(decode(sum(decode(nvl(fh.farl,0),0,0,fh.laimsl)),0,0,round_new(sum(laiml*farl)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),2)/0.0041816),0) as farldk,   \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*mt)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),"+((Visit) getPage().getVisit()).getMtdec()+")) as mt,  \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*vad)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),2)) as vad,  \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*ad)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),2)) as ad,  \n");
		sbsql.append("       decode(sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),0,0,round_new(sum(laiml*std)/sum(decode(nvl(fh.farl,0),0,0,fh.laiml)),2)) as std  \n");
		sbsql.append("  from   \n");
		sbsql.append("       (select fh.diancxxb_id,fh.meikxxb_id,z.huaybh,max(fh.daohrq) as daohrq,max(z.huaysj) as huaysj,sum(ches) as ches,    \n");
		sbsql.append("        "+getLaimlField()+"   as laiml,    \n");
		sbsql.append("       sum(round(fh.laimsl)) as laimsl,  \n");
		sbsql.append("       round(decode(sum(nvl(z.qnet_ar,0)),0,0,"+getLaimlField()+")) as laimzl,  \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/  \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getFarldec()+")) as farl,  \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.mt,"+((Visit) getPage().getVisit()).getMtdec()+"))/  \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),"+((Visit) getPage().getVisit()).getMtdec()+")) as mt,  \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.vad,2))/  \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as vad,  \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.ad,2))/  \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as ad,  \n");
		sbsql.append("       decode(sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),0,0,  \n");
		sbsql.append("              round_new(sum(round_new(fh.laimzl,0)*round_new(z.std,2))/  \n");
		sbsql.append("              sum(round_new(decode(nvl(z.qnet_ar,0),0,0,fh.laimzl),0)),2)) as std      \n");
		sbsql.append("       from fahb fh,zhilb z,vwdianc dc,vwgongys y    \n");
		sbsql.append("       where fh.zhilb_id=z.id(+) and fh.diancxxb_id=dc.id and fh.gongysb_id=y.id    \n");
		sbsql.append("         and fh.jihkjb_id="+kouj+"  \n");
		sbsql.append(getCondtion());
		sbsql.append(" group by fh.diancxxb_id,fh.meikxxb_id,z.huaybh,fh.lieid) fh,vwdianc dc,meikxxb y    \n");
		sbsql.append(" where fh.diancxxb_id=dc.id and fh.meikxxb_id=y.id   \n");
		sbsql.append("         and y.id="+leix+"  \n");
		sbsql.append(" group by rollup (y.mingc,huaybh)  \n");
		sbsql.append(" having not (grouping(y.mingc) || grouping(huaybh)) = 1 \n");
		sbsql.append(" order by grouping(y.mingc),y.mingc,max(fh.daohrq) \n");
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String strTitle="";
		String titleName="入厂煤质量明细"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		String ArrHeader[][]=new String[2][13];
		ArrHeader[0]=new String[] {"单位","编号","收煤日期","化验日期","车数","实收量<br>(吨)","检质量<br>(吨)","发热量qnet,ar","发热量qnet,ar","水分<br>Mt(%)","挥发分<br>Vad(%)", "灰分<br>Ad(%)","硫分St,d(%)"};
		ArrHeader[1]=new String[] {"单位","编号","收煤日期","化验日期","车数","实收量<br>(吨)","检质量<br>(吨)","MJ/kg","Kacl/Kg","水分<br>Mt(%)", "挥发分<br>Vad(%)", "灰分<br>Ad(%)","硫分St,d(%)"};
		int ArrWidth[]=new int[] {150,80,80,80,60,60,60,60,60,60,60,60,60};
		
		rt.setBody(new Table(rs, 2, 0, 4));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4,  5, DateUtil.Formatdate("yyyy年MM月dd日", datStart)+"至"+DateUtil.Formatdate("yyyy年MM月dd日", datEnd), Table.ALIGN_CENTER);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(48);
		
		rt.body.setColFormat(8, "0.00");
		rt.body.setColFormat(10, "0.0");
		rt.body.setColFormat(11, "0.00");
		rt.body.setColFormat(12, "0.00");
		rt.body.setColFormat(13, "0.00");
		
		rt.body.setUseDefaultCss(true);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.mergeFixedCol(3);

		if(rt.body.getRows()>3){
			rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(), 4);
			rt.body.setCellAlign(rt.body.getRows(), 1, Table.ALIGN_CENTER);
		}
		
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
		int intct=0;
		if(cycle.getRequestContext().getParameters("vwgongys_id") !=null) {
			visit.setString9("");
			visit.setString10("");
			intct=(cycle.getRequestContext().getParameters("vwgongys_id")[0]).indexOf(",");
			if (intct>0){
				String [] pa=(cycle.getRequestContext().getParameters("vwgongys_id")[0]).split(",");
				if (pa.length==2){
					visit.setString9(pa[0]);
					visit.setString10(pa[1]);
				} 
				leix = visit.getString9();
				kouj=visit.getString10();
			}	
		}else{
			if(!visit.getString9().equals("")) {
				leix = visit.getString9();
			}
			if(!visit.getString10().equals("")) {
				kouj = visit.getString10();
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