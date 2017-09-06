package com.zhiren.jt.zdt.rulgl.rulmzlys;

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

public class RulmzlysReport extends BasePage {
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

	private String getCondtion(){
		String strCondtion="";
		
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
		
		strCondtion="and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
			"and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
		
		/*if (lngYunsfsId!=-1){
			strCondtion=strCondtion+" and fh.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (lngJihkjId!=-1){
			strCondtion=strCondtion+" and fh.yunsfsb_id=" +lngYunsfsId;
		}
		
		if (!strGongys_id.equals("-1")){
			strCondtion=strCondtion+" and y.dqid=" +strGongys_id;
		}*/
		
		if (jib==2){
			strCondtion=strCondtion+" and dc.fgsid=" +strDiancxxb_id;
		}else if (jib==3){
			strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
		}else if (jib==-1){
			strCondtion=strCondtion+" and dc.di=" +strDiancxxb_id;
		}
		return strCondtion;
	}
	
	private String getPrintData(){
		StringBuffer sbsql = new StringBuffer();
		StringBuffer sbSqlBody=new StringBuffer();
		String strTitle="";
		sbSqlBody.append(" nvl(round(decode(sum(sj.haoyl),0,0,sum((sj.qnet_ar/0.0041816)*sj.haoyl)/sum(sj.haoyl))),0) as qnet_ar, \n");
		sbSqlBody.append(" nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.mt*sj.haoyl)/sum(sj.haoyl)),2),0) as mt, \n");
		sbSqlBody.append(" nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.vdaf*sj.haoyl)/sum(sj.haoyl)),2),0) as vdaf, \n");
		sbSqlBody.append(" nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.ad*sj.haoyl)/sum(sj.haoyl)),2),0) as ad, \n");
		sbSqlBody.append(" nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.stad*sj.haoyl)/sum(sj.haoyl)),2),0) as stad  \n");
		
//		sbSqlBody.append("       decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),0,0,round(sum(round(jingz)*z.qnet_ar)/sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),3)) as farl,   \n");
//		sbSqlBody.append(" round(decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),0,0,round(sum(round(jingz)*z.qnet_ar)/sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),3))*7000/29.271) as farldk,   \n");
//		sbSqlBody.append("       decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),0,0,round(sum(round(jingz)*z.mt)/sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),2)) as shuif,   \n");
//		sbSqlBody.append("       decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),0,0,round(sum(round(jingz)*z.vdaf)/sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),2)) as huiff,   \n");
//		sbSqlBody.append("       decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),0,0,round(sum(round(jingz)*z.ad)/sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),2)) as huif,   \n");
//		sbSqlBody.append("       decode(sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),0,0,round(sum(round(jingz)*z.std)/sum(round(decode(nvl(z.qnet_ar,0),0,0,jingz))),2)) as liuf   \n");
		
		sbSqlBody.append("       from rulmzlb r,meihyb m,vwdianc dc  \n");
		sbSqlBody.append(" where m.rulmzlb_id=r.id \n");
		sbSqlBody.append(getCondtion());
		sbSqlBody.append("     and r.diancxxb_id=dc.id     \n");
		
		if (leix.equals(this.LX_FC)){
			sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc, \n");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(dc.fgsmc,dc.mingc)   \n");
			sbsql.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="(分厂)";
		}
		/*else if(leix.equals(this.LX_FK)){
			sbsql.append("select decode(grouping(y.smc)+grouping(y.mingc),2,'总计',1,y.smc,'&nbsp;&nbsp;&nbsp;&nbsp;'||y.mingc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(y.smc,y.mingc)   \n");
			sbsql.append(" order by grouping(y.smc) desc,max(y.sxh) ,y.smc, \n");
			sbsql.append("          grouping(y.mingc) desc,max(y.xuh) ,y.mingc \n");
			strTitle="(分矿)";
		}else if(leix.equals(this.LX_FCFK)){
			sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(y.mingc),3,'总计',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||y.mingc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append("group by rollup(dc.fgsmc,dc.mingc,y.mingc)   \n");
			sbsql.append(" order by grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc, \n");
			sbsql.append("          grouping(y.mingc) desc,max(y.xuh) ,y.mingc \n");
			strTitle="(分厂分矿)";
		}else if(leix.equals(this.LX_FKFC)){
			sbsql.append("select decode(grouping(dc.fgsmc)+grouping(dc.mingc)+grouping(y.mingc),3,'总计',2,y.mingc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as mingc,");
			sbsql.append(sbSqlBody);
			sbsql.append(" group by rollup(y.mingc,dc.fgsmc,dc.mingc)   \n");
			sbsql.append(" order by  grouping(y.mingc) desc,max(y.xuh) ,y.mingc, \n");
			sbsql.append("          grouping(dc.fgsmc) desc,max(dc.fgsxh) ,dc.fgsmc, \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc \n");
			strTitle="(分矿分厂)";
		}else if(leix.equals(this.LX_QP)){
			strTitle="(棋盘)";
		}*/
		
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		
		String titleName="入炉煤质量验收"+strTitle;
		
		// 报表表头定义
		Report rt = new Report();
		ResultSet rs = cn.getResultSet(sbsql.toString());
		String ArrHeader[][] = new String[2][8];
		ArrHeader[0] = new String[] { "单位", "净重<br>(吨)","水分<br>Mt(%)", "灰分<br>Ad(%)", "挥发分<br>Vdaf(%)","硫分<br>St,d(%)", "发热量qnet,ar", "发热量qnet,ar" };
		ArrHeader[1] = new String[] { "单位", "净重<br>(吨)","水分<br>Mt(%)", "灰分<br>Ad(%)", "挥发分<br>Vdaf(%)","硫分<br>St,d(%)", "MJ/kg", "Kacl/Kg" };
		int ArrWidth[] = new int[] { 150,  80, 80, 80, 80, 80, 80, 80 };
		
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.setTitle(titleName, ArrWidth);
		rt.setDefaultTitle(1, 4, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(5,  3, "", Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(48);
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