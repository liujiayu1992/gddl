package  com.zhiren.jt.zdt.yansgl.rucmzcj;

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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */


public class Rucmzcj  extends BasePage {
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
	
	private String RT_DR1 = "shangxycj";// 上下样抽检
	
	private String RT_DR2 = "shuancsh";//双采双化
	
	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		String title = "";
		String where1 = "";
		String where2 = "";
		String func="";
		
		String title1="";
		String title2="";
		
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;

		if (mstrReportName.equals(RT_DR1)) {// 上下样抽检
			title="入厂质量抽检情况";
			func= "1";
			where1=" where huaylbb_id=24712314 ";
			where2=" where huaylbb_id=24712315 ";
			title1="上样";
			title2="下样";
			return  getZhiltj(title,where1,where2,func,title1,title2);
		}else if(mstrReportName.equals(RT_DR2)){//双采双化
			
			if(getBaoblxValue()!=null){
				if (getBaoblxValue().getValue().equals("多部门对比")){
					title="双采双化对比情况";
					func= "2";
					where1=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc = '质检中心' ";
					where2=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc != '质检中心' and bm.mingc != '人工' and bm.mingc != '机械' ";
					title1="质检中心";
					title2="其它部";
				}else if(getBaoblxValue().getValue().equals("人工机械对比")){
					title="双采双化对比情况";
					func= "3";
					where1=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc = '人工' ";
					where2=" zlls,bumb bm where zlls.bumb_id=bm.id  and bm.mingc = '机械' ";
					title1="人工";
					title2="机械";
				}
				
				return getZhiltj(title,where1,where2,func,title1,title2);
			}else{
				return "";
			}
		}else {
			return "无此报表";
		}

	}
	
	private String getLaimlField(){
		return SysConstant.LaimField;
	}
	
	// 
	private String getZhiltj(String title,String where1,String where2,String func,String title1,String title2) {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq1=getBeginriqDate();
		String riq2=getEndriqDate();
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		
		/*//select getLinkMingxTaiz(decode(grouping(dc.mingc),1,-1,max(dc.id)) ,'"+this.getTreeid()+"',decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,
		sbsql.append("select decode(dc.mingc,null,'总计',dc.mingc) as diancmc, \n");
		sbsql.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',g.dqmc) as dqmc,\n");
		sbsql.append("decode(grouping(dc.mingc)+grouping(g.dqmc)+grouping(mk.mingc),2,'',1,'小计',mk.mingc) as meikmc, \n");
		//sbsql.append("decode(grouping(dc.mingc)+grouping(mk.mingc),2,'',1,'小计',mk.mingc) as meikmc, \n");
		sbsql.append("sum(sj.jingz) as jingz,sum(sj.syjingz) as choujzl,   \n");
		sbsql.append("sum(sj.ches) as ruccs,  \n");
		sbsql.append("decode(sum(sj.syches),null,'0',getLinkMingxTaizlx(decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(mk.mingc),1,-1,max(mk.id)),"+func+",sum(syches))) as choujcs, \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(symt*sj.jingz)/sum(sj.jingz),"+((Visit) getPage().getVisit()).getMtdec()+")) as symt,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(syad*sj.jingz)/sum(sj.jingz),2)) as syad,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(syvdaf*sj.jingz)/sum(sj.jingz),2)) as syvdaf,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(systd*sj.jingz)/sum(sj.jingz),2)) as systd,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),"+((Visit) getPage().getVisit()).getFarldec()+")) as syqnet_ar,  \n");
		sbsql.append("round(decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),"+((Visit) getPage().getVisit()).getFarldec()+"))/0.0041816,0) as syqnet_ardk,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xymt*sj.jingz)/sum(sj.jingz),"+((Visit) getPage().getVisit()).getMtdec()+")) as xymt,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyad*sj.jingz)/sum(sj.jingz),2)) as xyad,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyvdaf*sj.jingz)/sum(sj.jingz),2)) as xyvdaf,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xystd*sj.jingz)/sum(sj.jingz),2)) as xystd,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),"+((Visit) getPage().getVisit()).getFarldec()+")) as xyqnet_ar,  \n");
		sbsql.append("round(decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),"+((Visit) getPage().getVisit()).getFarldec()+"))/0.0041816,0) as xyqnet_ardk,  \n");
		sbsql.append("(decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),2))-  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),2))) as rezc,  \n");
		sbsql.append("round((decode(sum(sj.jingz),0,0,round(sum(syqnet_ar*sj.jingz)/sum(sj.jingz),2))-  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(xyqnet_ar*sj.jingz)/sum(sj.jingz),2)))/0.0041816) as rezc1,  \n");
		sbsql.append("decode(sum(sj.ches),0,0,round(sum(syches)/sum(sj.ches)*100,2)) as choujl,  \n");
		sbsql.append("decode(sum(sj.jingz),0,0,round(sum(sj.cyrz*sj.jingz)/sum(sj.jingz),2)) as cyrz  \n");
		sbsql.append("from  \n");
		//数据表
		sbsql.append("(select fh.diancxxb_id,fh.gongysb_id,fh.meikxxb_id, \n");//z.huaybh,
		sbsql.append("       decode(sum(fh.jingz),0,0,sum(z.qnet_ar*fh.jingz)/sum(fh.jingz)) as cyrz,  \n");
		sbsql.append("       "+SysConstant.LaimField+"  as jingz, \n");
		sbsql.append("       sum(decode(sy.zhilb_id,null,0,fh.biaoz+fh.yingd-(fh.yingd-fh.yingk))) as syjingz, \n");
		//sbsql.append("       "+getLaimlField()+" as laimsl, \n");//得到来煤量
		sbsql.append("       sum(fh.ches) ches, \n");
		sbsql.append("       sum(decode(sy.zhilb_id,null,0,fh.ches)) syches, \n");
		sbsql.append("       sum(decode(xy.zhilb_id,null,0,fh.ches)) xyches, \n");
		sbsql.append("       --sy \n");
		sbsql.append("       round_new(decode(sum(decode(sy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(sy.zhilb_id,null,0,fh.laimzl)*round_new(sy.mt,"+((Visit) getPage().getVisit()).getMtdec()+"))/sum(decode(sy.zhilb_id,null,0,fh.laimzl))),"+((Visit) getPage().getVisit()).getMtdec()+") symt, \n");
		sbsql.append("       round_new(decode(sum(decode(sy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(sy.zhilb_id,null,0,fh.laimzl)*sy.ad)/sum(decode(sy.zhilb_id,null,0,fh.laimzl))),2) syad, \n");
		sbsql.append("       round_new(decode(sum(decode(sy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(sy.zhilb_id,null,0,fh.laimzl)*sy.vdaf)/sum(decode(sy.zhilb_id,null,0,fh.laimzl))),2) syvdaf, \n");
		sbsql.append("       round_new(decode(sum(decode(sy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(sy.zhilb_id,null,0,fh.laimzl)*sy.std)/sum(decode(sy.zhilb_id,null,0,fh.laimzl))),2) systd, \n");
		sbsql.append("       round_new(decode(sum(decode(sy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(sy.zhilb_id,null,0,fh.laimzl)*round_new(sy.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(sy.zhilb_id,null,0,fh.laimzl))),"+((Visit) getPage().getVisit()).getFarldec()+") syqnet_ar, \n");
		sbsql.append("       --xy \n");
		sbsql.append("       round_new(decode(sum(decode(xy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(xy.zhilb_id,null,0,fh.laimzl)*round_new(xy.mt,"+((Visit) getPage().getVisit()).getMtdec()+"))/sum(decode(xy.zhilb_id,null,0,fh.laimzl))),"+((Visit) getPage().getVisit()).getMtdec()+") xymt, \n");
		sbsql.append("       round_new(decode(sum(decode(xy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(xy.zhilb_id,null,0,fh.laimzl)*xy.ad)/sum(decode(xy.zhilb_id,null,0,fh.laimzl))),2) xyad, \n");
		sbsql.append("       round_new(decode(sum(decode(xy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(xy.zhilb_id,null,0,fh.laimzl)*xy.vdaf)/sum(decode(xy.zhilb_id,null,0,fh.laimzl))),2) xyvdaf, \n");
		sbsql.append("       round_new(decode(sum(decode(xy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(xy.zhilb_id,null,0,fh.laimzl)*xy.std)/sum(decode(xy.zhilb_id,null,0,fh.laimzl))),2) xystd, \n");
		sbsql.append("       round_new(decode(sum(decode(xy.zhilb_id,null,0,fh.laimzl)),0,0,sum(decode(xy.zhilb_id,null,0,fh.laimzl)*round_new(xy.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+"))/sum(decode(xy.zhilb_id,null,0,fh.laimzl))),"+((Visit) getPage().getVisit()).getFarldec()+") xyqnet_ar \n");
		sbsql.append("       from zhilb z,fahb fh, \n");
		sbsql.append("       (select zhilb_id,mt,ad,vdaf,std,qnet_ar from zhillsb");
		sbsql.append("       "+where1+") sy, \n");
		sbsql.append("       (select zhilb_id,mt,ad,vdaf,std,qnet_ar from zhillsb");
		sbsql.append("       "+where2+") xy \n");
		sbsql.append("where z.id =fh.zhilb_id and z.id = sy.zhilb_id(+) and z.id = xy.zhilb_id(+) \n");
		sbsql.append("and z.huaysj>=to_date('"+riq1+"','yyyy-mm-dd') \n");
		sbsql.append("and z.huaysj<=to_date('"+riq2+"','yyyy-mm-dd')  \n");
		sbsql.append("group by fh.diancxxb_id,fh.gongysb_id,meikxxb_id) sj,  \n");//z.huaybh,fh.lieid
		
		sbsql.append("diancxxb dc,meikxxb mk,vwgongys g \n");
		sbsql.append("where sj.diancxxb_id=dc.id and sj.gongysb_id=g.id and sj.meikxxb_id=mk.id  and sj.syches!=0 \n");
		sbsql.append(diancCondition);
		sbsql.append("group by rollup(dc.mingc,g.dqmc,mk.mingc)  \n");
		sbsql.append("order by grouping(dc.mingc) desc,dc.mingc desc,grouping(g.dqmc) desc,g.dqmc desc,grouping(mk.mingc) desc, mk.mingc desc  \n");*/
			
		
		//***************************
		sbsql.append("select decode(dc.mingc,null,'总计',dc.mingc) as diancmc, \n");
		sbsql.append("decode(grouping(dc.mingc)+grouping(gy.dqmc),2,'',1,'小计',gy.dqmc) as dqmc, \n");
		sbsql.append("decode(grouping(dc.mingc)+grouping(gy.dqmc)+grouping(mk.mingc),2,'',1,'小计',mk.mingc) as meikmc, \n");
		sbsql.append("  round_new(sum(laimzl),0) as laimzl,sum(ches) as ches, \n");
		sbsql.append("  round_new(sum(jiaql),0) as jiaql, \n");//sum(cjcs) as cjcs,
		sbsql.append("  getLinkMingxTaizlx(decode(grouping(dc.mingc),1,-1,max(dc.id)),decode(grouping(mk.mingc),1,-1,max(mk.id)),"+func+",sum(cjcs)) as cjcs,\n ");
		//sbsql.append("  --decode(sum(jiaql),0,0,sum(qnetar*jiaql)/sum(jiaql)) as qnetar, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getMtdec()+")) as sy_mt, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_ad,2)* jiaql)/sum(jiaql),2)) as sy_ad, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_vdaf,2)* jiaql)/sum(jiaql),2)) as sy_vdaf,   \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_std,2)* jiaql)/sum(jiaql),2)) as sy_std,  \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getFarldec()+")) as sy_qnet_ar, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*jiaql)/sum(jiaql)/0.0041816,0)) as sy_qnet_ardk, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getMtdec()+")) as xy_mt, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_ad,2)* jiaql)/sum(jiaql),2)) as xy_ad, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_vdaf,2)* jiaql)/sum(jiaql),2)) as xy_vdaf,   \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_std,2)* jiaql)/sum(jiaql),2)) as xy_std, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getFarldec()+")) as xy_qnet_ar, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* jiaql)/sum(jiaql)/0.0041816,0)) as xy_qnet_ardk, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_qnet_ar,2)*jiaql)/sum(jiaql),2))- \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_qnet_ar,2)* jiaql)/sum(jiaql),2)) as rezc, \n");
		sbsql.append(" round_new((decode(sum(jiaql),0,0,round_new(sum(round_new(sy_qnet_ar,2)*jiaql)/sum(jiaql),2))- \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_qnet_ar,2)* jiaql)/sum(jiaql),2)))/0.0041816,0) as rezdk, \n");
		sbsql.append("  decode(sum(ches),0,0,round(sum(cjcs)/sum(ches)*100,0)) as choujl, \n");
		sbsql.append("  decode(sum(laimzl),0,0,round(sum(qnetar*laimzl)/sum(laimzl),2)) as cyrz, \n");
		sbsql.append("  decode(sum(laimzl),0,0,round(sum(qnetar*laimzl)/sum(laimzl)/0.0041816,0)) as cyrzdk \n");
		sbsql.append("from  \n");
		sbsql.append("(select diancxxb_id,gongysb_id,meikxxb_id,  \n");
		sbsql.append("  sum(ches) as ches,sum(cjcs) as cjcs, \n");
		sbsql.append("  sum(round_new(laimzl,0)) as laimzl, \n");
		sbsql.append("  sum(round_new(jiaql,0)) as jiaql, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(qnetar,"+((Visit) getPage().getVisit()).getFarldec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getFarldec()+")) as qnetar, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getFarldec()+")) as sy_qnet_ar, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getMtdec()+")) as sy_mt, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_ad,2)* jiaql)/sum(jiaql),2)) as sy_ad, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_vdaf,2)* jiaql)/sum(jiaql),2)) as sy_vdaf,   \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(sy_std,2)* jiaql)/sum(jiaql),2)) as sy_std,  \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getFarldec()+")) as xy_qnet_ar, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* jiaql)/sum(jiaql),"+((Visit) getPage().getVisit()).getMtdec()+")) as xy_mt, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_ad,2)* jiaql)/sum(jiaql),2)) as xy_ad, \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_vdaf,2)* jiaql)/sum(jiaql),2)) as xy_vdaf,   \n");
		sbsql.append("  decode(sum(jiaql),0,0,round_new(sum(round_new(xy_std,2)* jiaql)/sum(jiaql),2)) as xy_std  \n");
		sbsql.append("from  \n");
		sbsql.append("(select diancxxb_id,gongysb_id,meikxxb_id,lieid,sum(ches) as ches,sum(decode(sxy,0,0,ches)) as cjcs, \n");
		sbsql.append("  "+SysConstant.LaimField1+" as laimzl, \n");
		sbsql.append("  sum(decode(sxy,0,0,biaoz+yingd-(yingd-yingk))) as jiaql, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(qnetar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))  as qnetar, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))  as sy_qnet_ar, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getMtdec()+"))  as sy_mt, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_ad,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as sy_ad, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_vdaf,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as sy_vdaf, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(sy_std,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as sy_std, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getFarldec()+"))  as xy_qnet_ar, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_mt,"+((Visit) getPage().getVisit()).getMtdec()+")* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),"+((Visit) getPage().getVisit()).getMtdec()+"))  as xy_mt, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_ad,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as xy_ad, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_vdaf,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as xy_vdaf, \n");
		sbsql.append("  decode(sum(decode(sxy,0,0,laimzl)),0,0, round_new(sum(round_new(xy_std,2)* decode(sxy,0,0,laimzl))/sum(decode(sxy,0,0,laimzl)),2))  as xy_std \n");
		sbsql.append("from  \n");
		sbsql.append("(select fh.diancxxb_id,fh.gongysb_id,fh.meikxxb_id,fh.biaoz,fh.yingd,fh.yingk,laimzl,fh.lieid, \n");
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
		sbsql.append("and z.id = xy.zhilb_id(+) \n");
		sbsql.append("and fh.daohrq>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
		sbsql.append("and fh.daohrq<=to_date('"+riq2+"','yyyy-mm-dd')) ysj--原数据 \n");
		sbsql.append("group by diancxxb_id,lieid,gongysb_id,meikxxb_id) jqsj--加权数据 \n");
		sbsql.append("group by diancxxb_id,gongysb_id,meikxxb_id) a,vwgongys gy,vwdianc dc,meikxxb mk \n");
		sbsql.append("where a.diancxxb_id=dc.id \n");
		sbsql.append("and a.gongysb_id=gy.id \n");
		sbsql.append("and a.meikxxb_id=mk.id \n");
		sbsql.append(diancCondition);
		sbsql.append("and a.cjcs<>0 \n");
		sbsql.append("group by rollup(dc.mingc,gy.dqmc,mk.mingc) \n");
		sbsql.append("order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc, \n");
		sbsql.append("grouping(gy.dqmc) desc,max(gy.dqxh),gy.dqmc, \n");
		sbsql.append("grouping(mk.mingc) desc,max(mk.xuh),mk.mingc \n");
		
		
//		System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		

		 String ArrHeader[][]=new String[2][23];
		 ArrHeader[0]=new String[] {"电厂","煤矿地区","供货单位","入厂","入厂","抽检","抽检",title1,title1,title1,title1,title1,title1,title2,title2,title2,title2,title2,title2,"热值差","热值差","抽检率(%)","采用值","采用值"};
		 ArrHeader[1]=new String[] {"电厂","煤矿地区","供货单位","数量(吨)","车数(车)","数量(吨)","车数(车)","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","低位发热量Kcal/kg","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","低位发热量Kcal/kg","MJ/kg","kcal/kg","抽检率(%)","MJ/kg","kcal/kg"};

		 int ArrWidth[]=new int[] {100,80,150,50,50,50,50,50,50,50,50,50,55,50,50,50,50,50,55,50,50,50,50,50};

			Table bt=new Table(rs,2,0,3);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setPageRows(21);
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
			
			//
			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle(title, ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(9, 4, FormatDate(DateUtil.getDate(riq1))+"-"+ FormatDate(DateUtil.getDate(riq2)),Table.ALIGN_CENTER);
			rt.setDefaultTitle(22, 3,"单位:吨" ,Table.ALIGN_RIGHT);
			
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "制表时间:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(10, 2, "审核：", Table.ALIGN_CENTER);
			if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
				
				rt.setDefautlFooter(22, 3, "制表:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(22, 3, "制表:"+((Visit) getPage().getVisit()).getDiancmc

		(),Table.ALIGN_RIGHT);
				}
//			rt.setDefautlFooter(22, 3, "制表：", Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	
	
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List fahdwList = new ArrayList();
		fahdwList.add(new IDropDownBean(0,"多部门对比"));
		fahdwList.add(new IDropDownBean(1,"人工机械对比"));

		_IBaoblxModel = new IDropDownModel(fahdwList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////

//	开始日期v
	private boolean _BeginriqChange=false;

	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("至"));
		tb1.addText(new ToolbarText("-"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
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
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		if (mstrReportName.equals(RT_DR2)){//双采双化
			tb1.addText(new ToolbarText("采样方式:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BaoblxDropDown");
			cb.setListeners("select:function(){document.Form0.submit();}");
			cb.setId("Baoblx");
			cb.setWidth(120);
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		}
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
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
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			visit.setDefaultTree(null);
			
			this.setTreeid(null);
			this.getTreeid();
			visit.setString4(null);
			visit.setString5(null);
			this.setBeginriqDate(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj()));
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		if(_Baoblxchange){
			_Baoblxchange=false;
			getPrintTable();
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
//	电厂名称
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
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
//		 分公司下拉框
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
			setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
		}
	
//		得到系统信息表中配置的报表标题的单位名称
		public String getBiaotmc(){
			String biaotmc="";
			JDBCcon cn = new JDBCcon();
			String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
			ResultSet rs=cn.getResultSet(sql_biaotmc);
			try {
				while(rs.next()){
					 biaotmc=rs.getString("zhi");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
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
