////2008-08-05 chh 
//修改内容 ：燃料公司的用户可以查看到数据
//		   明细数据显示

package com.zhiren.jt.zdt.yansgl.rucmzcj;

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

public class ShangcshReport extends BasePage {
//	public final static String LX_FC="fc";
//	public final static String LX_FK="fk";
//	public final static String LX_FKFC="fkfc";
//	public final static String LX_FCFK="fcfk";
//	public final static String LX_QP="qp";
	
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
		
		int intLen=0;
		String lx=getLeix();
		intLen=lx.indexOf(",");
		String diancid="";
		String meikxxbid="";
		String leix="";
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				leix=pa[0];//类型
				meikxxbid=" and fh.meikxxb_id=" +pa[1];//煤矿ID
				diancid=" and diancxxb_id=" +pa[0];//电厂ID
			}else{
				diancid=" and diancxxb_id=-1";
				meikxxbid=" and fh.meikxxb_id=-1";//煤矿ID
			}
		}else{
			return "";
		}
		return getRucmzcjmx(diancid,meikxxbid);
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	/**
	 * 抽检明细表
	 * @param diancid 电厂ID
	 * @param meikxxbid 煤矿ID
	 * @return
	 */
	private String getRucmzcjmx(String diancid,String meikxxbid){
		
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String BeginriqDate=((Visit)getPage().getVisit()).getString4();
		String EndriqDate=((Visit)getPage().getVisit()).getString5();
		
		String date=BeginriqDate+"至"+EndriqDate;
		
		//报表表头定义
		Report rt = new Report();
		String titlename="双采双化对比情况";
		
		//danwmc=getTreeDiancmc(this.getTreeid());
		//报表内容
				titlename=titlename+"";
				 
				 StringBuffer sbsql = new StringBuffer();
				sbsql.append("select  \n");
				sbsql.append("decode(grouping(dc.mingc)+grouping(mk.mingc),2,'总计',1,dc.mingc||'小计',mk.mingc) as meikmc, \n");
				sbsql.append("decode(grouping(dc.mingc)+grouping(mk.mingc)+grouping(fah.huaybh),1,'小计',fah.huaybh) as huaybh, \n");
				sbsql.append("fah.daohrq, \n");
				sbsql.append("sum(sy.ches) as choujcs, \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(sy.mt*fah.jingz)/sum(fah.jingz),2)) as symt,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(sy.ad*fah.jingz)/sum(fah.jingz),2)) as syad,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(sy.vdaf*fah.jingz)/sum(fah.jingz),2)) as syvdaf,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(sy.std*fah.jingz)/sum(fah.jingz),2)) as systd,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(sy.qnet_ar*fah.jingz)/sum(fah.jingz),2)) as syqnet_ar,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(sy.qnet_ar*fah.jingz)/sum(fah.jingz)/0.0041816,0)) as syqnet_ardk,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.mt*fah.jingz)/sum(fah.jingz),2)) as xymt,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.ad*fah.jingz)/sum(fah.jingz),2)) as xyad,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.vdaf*fah.jingz)/sum(fah.jingz),2)) as xyvdaf,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.std*fah.jingz)/sum(fah.jingz),2)) as xystd,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.qnet_ar*fah.jingz)/sum(fah.jingz),2)) as xyqnet_ar,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.qnet_ar*fah.jingz)/sum(fah.jingz)/0.0041816,0)) as xyqnet_ardk,  \n");
				sbsql.append("(decode(sum(fah.jingz),0,0,round(sum(sy.qnet_ar*fah.jingz)/sum(fah.jingz),2))-  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.qnet_ar*fah.jingz)/sum(fah.jingz),2))) as rezc,  \n");
				sbsql.append("round((decode(sum(fah.jingz),0,0,round(sum(sy.qnet_ar*fah.jingz)/sum(fah.jingz),2))-  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(xy.qnet_ar*fah.jingz)/sum(fah.jingz),2)))/0.0041816) as rezc1,  \n");
				//sbsql.append("--decode(sum(fah.ches),0,0,round(sum(sy.ches)/sum(fah.ches)*100,2)) as choujl,  \n");
				sbsql.append("decode(sum(fah.jingz),0,0,round(sum(fah.cyrz*fah.jingz)/sum(fah.jingz),2)) as cyrz  \n");
				sbsql.append("from   \n");
				sbsql.append("(select fh.diancxxb_id,fh.meikxxb_id,zl.id,zl.huaybh,to_char(fh.daohrq,'yyyy-mm-dd') as daohrq,sum(fh.jingz) as jingz,sum(fh.ches) as ches,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zl.qnet_ar*fh.jingz)/sum(fh.jingz)) as cyrz  \n");
				sbsql.append("from fahb fh ,zhilb zl  \n");
				sbsql.append("where fh.zhilb_id=zl.id and zl.huaysj>=to_date('"+BeginriqDate+"','yyyy-mm-dd')  \n");
				sbsql.append("and zl.huaysj<=to_date('"+EndriqDate+"','yyyy-mm-dd') "+diancid+" "+meikxxbid+" \n");
				sbsql.append("group by fh.diancxxb_id,fh.meikxxb_id,zl.id,zl.huaybh,fh.daohrq) fah,  \n");
				sbsql.append("(select zl.id,zl.huaybh,to_char(fh.daohrq,'yyyy-mm-dd') as daohrq,sum(fh.jingz) as jingz,sum(fh.ches) as ches,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.mt*fh.jingz)/sum(fh.jingz)) as mt,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.ad*fh.jingz)/sum(fh.jingz)) as ad,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.vdaf*fh.jingz)/sum(fh.jingz)) as vdaf,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.std*fh.jingz)/sum(fh.jingz)) as std,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.qnet_ar*fh.jingz)/sum(fh.jingz)) as qnet_ar   \n");
				sbsql.append("from zhilb zl,zhillsb zlls,fahb fh,bumb bm  \n");
				sbsql.append("where zlls.zhilb_id=zl.id and fh.zhilb_id=zl.id and zlls.bumb_id=bm.id \n");
				sbsql.append("and zl.huaysj>=to_date('"+BeginriqDate+"','yyyy-mm-dd')  \n");
				sbsql.append("and zl.huaysj<=to_date('"+EndriqDate+"','yyyy-mm-dd')  \n");
				sbsql.append("and bm.mingc like '燃料部' "+diancid+" "+meikxxbid+" group by zl.id,zl.huaybh,fh.daohrq) sy,  \n");
				sbsql.append("(select zl.id,zl.huaybh,to_char(fh.daohrq,'yyyy-mm-dd') as daohrq,sum(fh.jingz) as jingz,sum(fh.ches) as ches,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.mt*fh.jingz)/sum(fh.jingz)) as mt,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.ad*fh.jingz)/sum(fh.jingz)) as ad,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.vdaf*fh.jingz)/sum(fh.jingz)) as vdaf,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.std*fh.jingz)/sum(fh.jingz)) as std,  \n");
				sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.qnet_ar*fh.jingz)/sum(fh.jingz)) as qnet_ar   \n");
				sbsql.append("from zhilb zl,zhillsb zlls,fahb fh,bumb bm  \n");
				sbsql.append("where zlls.zhilb_id=zl.id and fh.zhilb_id=zl.id and zlls.bumb_id=bm.id \n");
				sbsql.append("and zl.huaysj>=to_date('"+BeginriqDate+"','yyyy-mm-dd')  \n");
				sbsql.append("and zl.huaysj<=to_date('"+EndriqDate+"','yyyy-mm-dd')  \n");
				sbsql.append("and bm.mingc not like '燃料部' and bm.mingc not like '人工' and bm.mingc not like '机械' "+diancid+" "+meikxxbid+" group by zl.id,zl.huaybh,fh.daohrq) xy,  \n");
				sbsql.append("diancxxb dc,meikxxb mk  \n");
				sbsql.append("where fah.diancxxb_id=dc.id and fah.meikxxb_id=mk.id  \n");
				sbsql.append("and fah.id=sy.id and fah.id=xy.id  \n");
				sbsql.append("group by rollup(dc.mingc,mk.mingc,fah.huaybh,fah.daohrq)  \n");
				sbsql.append("having not (grouping(fah.huaybh) || grouping(fah.daohrq)) =1 and grouping(dc.mingc)=0 \n");
				sbsql.append("order by grouping(dc.mingc) desc,dc.mingc, \n");
				sbsql.append("grouping(mk.mingc) desc,mk.mingc, \n");
				sbsql.append("grouping(fah.huaybh) desc,fah.huaybh,grouping(fah.daohrq) desc  \n");
				
				
				
				
		 
				String ArrHeader[][]=new String[2][19];
				ArrHeader[0]=new String[] {"单位","化验编号","到货日期","抽检车数(车)","燃料部","燃料部","燃料部","燃料部","燃料部","燃料部","其它部","其它部","其它部","其它部","其它部","其它部","热值差","热值差","采用值"};
				ArrHeader[1]=new String[] {"单位","化验编号","到货日期","抽检车数(车)","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","低位发热量Kcal/kg","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","低位发热量Kcal/kg","MJ/kg","kcal/kg","采用值"};

  			    int ArrWidth[]=new int[] {200,80,80,54,54,54,54,54,54,54,54,60,54,54,54,54,54,60,54};

				ResultSet rs = cn.getResultSet(sbsql.toString());


		
			
			// 数据
			rt.setBody(new Table(rs,2, 0, 1));

			rt.setTitle(titlename, ArrWidth);
//			rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 2, "制表单位:"+((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 3,date, Table.ALIGN_CENTER);
			rt.setDefaultTitle(7, 2, "", Table.ALIGN_RIGHT);
			
			rt.body.setUseDefaultCss(true);
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(48);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = true;
			rt.createDefautlFooter(ArrWidth);
			//页脚 
			
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(8,1,"单位:车、吨",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(6,3,"制表:",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(12,2,"审核:",Table.ALIGN_LEFT);
//			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			
			
			//设置页数
//		    rt.createDefautlFooter(ArrWidth);
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
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