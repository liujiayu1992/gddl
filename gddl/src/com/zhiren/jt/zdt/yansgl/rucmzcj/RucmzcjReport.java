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

public class RucmzcjReport extends BasePage {
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
		String caiyfs="";
		String where1 ="";
		String where2 ="";
		String title1="";
		String title2="";
		String title="";
		String leix="";
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length>0){
				leix=pa[0];//类型
				meikxxbid=" and meikxxb_id=" +pa[1];//煤矿ID
				diancid=" and diancxxb_id=" +pa[0];//电厂ID
				caiyfs=pa[2];//采样方式:上下样，多部门，人工机械
				if(caiyfs.equals("1")){//上下样
					where1=" where huaylbb_id=24712314 ";
					where2=" where huaylbb_id=24712315 ";
					title1="上样";
					title2="下样";
					title="入厂质量抽检情况";
				}else if(caiyfs.equals("2")){//多部门
					where1=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc = '质检中心' ";
					where2=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc != '质检中心' and bm.mingc != '人工' and bm.mingc != '机械' ";
					title1="质检中心";
					title2="其它部";
					title="双采双化对比情况";
				}else if(caiyfs.equals("3")){//人工机械
					where1=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc = '人工' ";
					where2=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc = '机械' ";
					title1="人工";
					title2="机械";
					title="双采双化对比情况";
				}
			}else{
				diancid=" and diancxxb_id=-1";
				meikxxbid=" and meikxxb_id=-1";//煤矿ID
			}
		}else{
			return "";
		}
		return getRucmzcjmx(diancid,meikxxbid,title,where1,where2,title1,title2);
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
	private String getRucmzcjmx(String diancid,String meikxxbid,String title,String where1,String where2,String title1,String title2){
		
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String BeginriqDate=((Visit)getPage().getVisit()).getString4();
		String EndriqDate=((Visit)getPage().getVisit()).getString5();
		
		String date=BeginriqDate+"至"+EndriqDate;
		
		//报表表头定义
		Report rt = new Report();
		
		//danwmc=getTreeDiancmc(this.getTreeid());
		//报表内容
				 
				 StringBuffer sbsql = new StringBuffer();
				 	/*sbsql.append("select decode(grouping(dc.mingc)+grouping(mk.mingc),2,'总计',1,dc.mingc||'小计',mk.mingc) as meikmc, \n");
					sbsql.append("decode(grouping(dc.mingc)+grouping(mk.mingc)+grouping(sj.huaybh),1,'小计',sj.huaybh) as huaybh, sj.daohrq, \n");
					//sbsql.append("sum(sj.jingz) as jingz,   \n");
					//sbsql.append("sum(sj.syjingz) as choujzl,\n");
					//sbsql.append("sum(sj.ches) as ruccs,  \n");
					sbsql.append("sum(syches) as choujcs, \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(symt*sj.jingz)/sum(sj.jingz),2)) as symt,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(syad*sj.jingz)/sum(sj.jingz),2)) as syad,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(syvdaf*sj.jingz)/sum(sj.jingz),2)) as syvdaf,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(systd*sj.jingz)/sum(sj.jingz),2)) as systd,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),2)) as syqnet_ar,  \n");
					sbsql.append("round(decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),2))/0.0041816,0) as syqnet_ardk,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xymt*sj.jingz)/sum(sj.jingz),2)) as xymt,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyad*sj.jingz)/sum(sj.jingz),2)) as xyad,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyvdaf*sj.jingz)/sum(sj.jingz),2)) as xyvdaf,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xystd*sj.jingz)/sum(sj.jingz),2)) as xystd,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),2)) as xyqnet_ar,  \n");
					sbsql.append("round(decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),2))/0.0041816,0) as xyqnet_ardk,  \n");
					sbsql.append("(decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),2))-  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),2))) as rezc,  \n");
					sbsql.append("round((decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),2))-  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),2)))/0.0041816) as rezc1,  \n");
					//sbsql.append("decode(sum(sj.ches),0,0,round(sum(syches)/sum(sj.ches)*100,2)) as choujl,  \n");
					sbsql.append("decode(sum(sj.jingz),0,0,round(sum(sj.cyrz*sj.jingz)/sum(sj.jingz),2)) as cyrz  \n");
					sbsql.append("from  \n");
					//数据表
					sbsql.append("(select f.diancxxb_id,f.meikxxb_id,z.huaybh,to_char(f.daohrq,'yyyy-mm-dd') as daohrq, \n");
					sbsql.append("       decode(sum(f.jingz),0,0,sum(z.qnet_ar*f.jingz)/sum(f.jingz)) as cyrz,  \n");
					sbsql.append("       sum(f.jingz) as jingz, \n");
					sbsql.append("       sum(decode(sy.zhilb_id,null,0,f.jingz)) as syjingz, \n");
					sbsql.append("       sum(f.ches) ches, \n");
					sbsql.append("       sum(decode(sy.zhilb_id,null,0,f.ches)) syches, \n");
					sbsql.append("       sum(decode(xy.zhilb_id,null,0,f.ches)) xyches, \n");
					sbsql.append("       --sy \n");
					sbsql.append("       round(decode(sum(decode(sy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(sy.zhilb_id,null,0,f.jingz)*sy.mt)/sum(decode(sy.zhilb_id,null,0,f.jingz))),1) symt, \n");
					sbsql.append("       round(decode(sum(decode(sy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(sy.zhilb_id,null,0,f.jingz)*sy.ad)/sum(decode(sy.zhilb_id,null,0,f.jingz))),2) syad, \n");
					sbsql.append("       round(decode(sum(decode(sy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(sy.zhilb_id,null,0,f.jingz)*sy.vdaf)/sum(decode(sy.zhilb_id,null,0,f.jingz))),2) syvdaf, \n");
					sbsql.append("       round(decode(sum(decode(sy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(sy.zhilb_id,null,0,f.jingz)*sy.std)/sum(decode(sy.zhilb_id,null,0,f.jingz))),2) systd, \n");
					sbsql.append("       round(decode(sum(decode(sy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(sy.zhilb_id,null,0,f.jingz)*sy.qnet_ar)/sum(decode(sy.zhilb_id,null,0,f.jingz))),2) syqnet_ar, \n");
					sbsql.append("       --xy \n");
					sbsql.append("       round(decode(sum(decode(xy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(xy.zhilb_id,null,0,f.jingz)*xy.mt)/sum(decode(xy.zhilb_id,null,0,f.jingz))),1) xymt, \n");
					sbsql.append("       round(decode(sum(decode(xy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(xy.zhilb_id,null,0,f.jingz)*xy.ad)/sum(decode(xy.zhilb_id,null,0,f.jingz))),2) xyad, \n");
					sbsql.append("       round(decode(sum(decode(xy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(xy.zhilb_id,null,0,f.jingz)*xy.vdaf)/sum(decode(xy.zhilb_id,null,0,f.jingz))),2) xyvdaf, \n");
					sbsql.append("       round(decode(sum(decode(xy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(xy.zhilb_id,null,0,f.jingz)*xy.std)/sum(decode(xy.zhilb_id,null,0,f.jingz))),2) xystd, \n");
					sbsql.append("       round(decode(sum(decode(xy.zhilb_id,null,0,f.jingz)),0,0,sum(decode(xy.zhilb_id,null,0,f.jingz)*xy.qnet_ar)/sum(decode(xy.zhilb_id,null,0,f.jingz))),2) xyqnet_ar \n");
					sbsql.append("       from zhilb z,fahb f, \n");
					sbsql.append("       (select zhilb_id,mt,ad,vdaf,std,qnet_ar from zhillsb");
					sbsql.append("       "+where1+") sy, \n");
					sbsql.append("       (select zhilb_id,mt,ad,vdaf,std,qnet_ar from zhillsb");
					sbsql.append("       "+where2+") xy \n");
					sbsql.append("where z.id = f.zhilb_id and z.id = sy.zhilb_id(+) and z.id = xy.zhilb_id(+) \n");
					sbsql.append("and z.huaysj>=to_date('"+BeginriqDate+"','yyyy-mm-dd') \n");
					sbsql.append("and z.huaysj<=to_date('"+EndriqDate+"','yyyy-mm-dd')  \n");
					sbsql.append("group by f.diancxxb_id,f.meikxxb_id,z.huaybh,f.daohrq) sj,  \n");
					
					sbsql.append("diancxxb dc,meikxxb mk \n");
					sbsql.append("where sj.diancxxb_id=dc.id and sj.meikxxb_id=mk.id and sj.syches!=0 \n");
					sbsql.append(diancid).append(meikxxbid);
					sbsql.append("group by rollup(dc.mingc,mk.mingc,sj.huaybh,sj.daohrq) \n");
					sbsql.append("having not (grouping(sj.huaybh) || grouping(sj.daohrq)) =1 and grouping(dc.mingc)=0 \n");
					sbsql.append("order by grouping(dc.mingc) desc,dc.mingc, \n");
					sbsql.append("grouping(mk.mingc) desc,mk.mingc, \n");
					sbsql.append("grouping(sj.huaybh) desc,sj.huaybh,grouping(sj.daohrq) desc  \n");*/
					
					
					 
					sbsql.append("select decode(grouping(dc.mingc)+grouping(mk.mingc),2,'总计',1,dc.mingc,mk.mingc) as danwmc, \n");
					sbsql.append("  decode(grouping(mk.mingc)+grouping(huaybh),1,'小计',huaybh) as huaybh, \n");
					sbsql.append("  decode(grouping(huaybh),1,'',max(daohrq)) as daohrq,sum(decode(sxy,0,0,ches)) as cjcs, \n");
					sbsql.append("  --decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(qnetar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))  as qnetar, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getMtdec()+"))  as sy_mt, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_ad,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as sy_ad, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_vdaf,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as sy_vdaf, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_std,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as sy_std, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))  as sy_qnet_ar, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl))/0.0041816,0))  as sy_qnet_ardk, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getMtdec()+"))  as xy_mt, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_ad,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as xy_ad, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_vdaf,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as xy_vdaf, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_std,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as xy_std, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))  as xy_qnet_ar, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl))/0.0041816,0))  as xy_qnet_ardk, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(qnetar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))- \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+")) as rzc, \n");
					sbsql.append("  round_new((decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))- \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+")))/0.0041816,0) as rzcdk, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(qnetar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2)) as cyrz, \n");
					sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(qnetar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl))/0.0041816,0)) as cyrzdk \n");
					sbsql.append("from  \n");
					sbsql.append("(select fh.diancxxb_id,fh.gongysb_id,fh.meikxxb_id,z.id as zhilbid,z.huaybh, \n");
					sbsql.append("       to_char(fh.daohrq,'yyyy-mm-dd') as daohrq,fh.biaoz,fh.yingd,fh.yingk,laimzl,fh.lieid, \n");
					sbsql.append("       fh.ches as ches,--质量车数  \n");
					sbsql.append("       decode(nvl(sy.zhilb_id,0)*nvl(xy.zhilb_id,0),0,0,1) as sxy, \n");
					sbsql.append("       z.qnet_ar as qnetar,--质量热质 \n");
					sbsql.append("       sy.qnet_ar as sy_qnet_ar,sy.mt as sy_mt,sy.ad as sy_ad,sy.vdaf as sy_vdaf,sy.std as sy_std, \n");
					sbsql.append("       xy.qnet_ar as xy_qnet_ar,xy.mt as xy_mt,xy.ad as xy_ad,xy.vdaf as xy_vdaf,xy.std as xy_std      \n");
					sbsql.append("       from zhilb z,fahb fh,  \n");
					sbsql.append("       (select zhilb_id,mt,ad,vdaf,std,qnet_ar from zhillsb "+where1+" ) sy,  \n");
					sbsql.append("       (select zhilb_id,mt,ad,vdaf,std,qnet_ar from zhillsb "+where2+" ) xy  \n");
					sbsql.append("where z.id =fh.zhilb_id  \n");
					sbsql.append("and z.id = sy.zhilb_id(+)  \n");
					sbsql.append("and z.id = xy.zhilb_id(+)  \n");
					sbsql.append("and fh.daohrq>=to_date('"+BeginriqDate+"','yyyy-mm-dd')  \n");
					sbsql.append("and fh.daohrq<=to_date('"+EndriqDate+"','yyyy-mm-dd')) ysj,--原数据 \n");
					sbsql.append("diancxxb dc,meikxxb mk \n");
					sbsql.append("where sxy!=0 and ysj.diancxxb_id=dc.id and ysj.meikxxb_id=mk.id  \n");
					sbsql.append(diancid).append(meikxxbid);
					sbsql.append("group by rollup (dc.mingc,mk.mingc,huaybh) \n");
					sbsql.append("having not grouping(dc.mingc) =1 \n");
					sbsql.append("order by grouping(dc.mingc) desc,dc.mingc,grouping(mk.mingc) desc,mk.mingc,huaybh desc \n");
				
		 
				String ArrHeader[][]=new String[2][19];
				ArrHeader[0]=new String[] {"单位","化验编号","到货日期","抽检车数(车)",title1,title1,title1,title1,title1,title1,title2,title2,title2,title2,title2,title2,"热值差","热值差","采用值","采用值"};
				ArrHeader[1]=new String[] {"单位","化验编号","到货日期","抽检车数(车)","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","低位发热量Kcal/kg","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","低位发热量Kcal/kg","MJ/kg","kcal/kg","MJ/kg","kcal/kg"};

  			    int ArrWidth[]=new int[] {200,80,80,54,54,54,54,54,54,54,54,60,54,54,54,54,54,60,54,54};

				ResultSet rs = cn.getResultSet(sbsql.toString());


		
			
			// 数据
			rt.setBody(new Table(rs,2, 0, 1));

			rt.setTitle(title, ArrWidth);
//			rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 2, "制表单位:"+((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 3,date, Table.ALIGN_CENTER);
			rt.setDefaultTitle(9, 2, "", Table.ALIGN_RIGHT);
			
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
        	if(!visit.getString9().equals("")) {
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