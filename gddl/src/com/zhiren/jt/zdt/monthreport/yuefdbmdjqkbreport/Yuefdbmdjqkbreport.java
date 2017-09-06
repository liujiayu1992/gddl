/*
 * 作者：zuoh
 * 时间：2010-11-19
 * 描述：实现页面不自动刷新
 */
package com.zhiren.jt.zdt.monthreport.yuefdbmdjqkbreport;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import org.apache.tapestry.contrib.palette.SortMode;

/* 
* 时间：2009-05-4
* 作者： ll
* 修改内容：1、修改查询sql中yuezbb表中字段的名称,按照yuezbb中新的公式，取新的字段名。
* 		   
*/ 
/* 
* 时间：2009-05-20
* 作者： sy
* 修改内容：由于平圩电厂是一厂两制，平圩一电和平圩二电同在一个厂级系统中填报报表数据。
* 		   当两个电厂填报同一个月报页面时页面出错。所以在beginResponse()中增加了用户级别为电厂级，
  判断登陆电厂与电厂树是否一致，并重新加载刷新页面。
* 		   
*/ 
/* 
* 时间：2009-06-12
* 作者： ll
* 修改内容：1、按二级公司登陆时统计口径默认“按公司统计”；
*          2、二级公司登陆时去“总计”行。
* 		   
*/ 
/* 
* 时间：2010-01-4
* 作者： ll
* 修改内容：1、增加“按分公司统计”条件查询。
* 			2、增加“月报口径”下拉框，对数据进行查询。
*/
/* 
* 时间：2010-09-19
* sy
* 修改内容：1修改sql，不使用having not，否则有oracle版本bug，会出错
*/ 
/* 
* 时间：2010-11-05
* 作者：liufl
* 修改内容：1、增加"口径类型"下拉菜单，可选择"等比口径"和"月报电厂口径"
*          2、未审核数据用红色背景显示         
*/
/* 
* 时间：2010-12-31
* 作者：liufl
* 修改内容：1、修改sql，单位选电厂时不显示“统计口径”下拉框
*            2、单位选分公司+按地区统计时红色显示不对，修改
*/
/* 
* 时间：2011-01-11
* 作者：liufl
* 修改内容：修改导出报表时，隐藏列也导出的问题
*            
*/
/* 
* 时间：2011-01-21
* 作者：liufl
* 修改内容：修改sql,解决页面空指针bug
*            
*/
/* 
* 时间：2011-02-17
* 作者：liufl
* 修改内容：修改页眉页脚，使报表填报单位和打印日期一致
*            
*/
/* 
* 时间：2012-01-12
* 作者：liufl
* 修改内容：添加系统参数控制是否查询视图
*            
*/
public class Yuefdbmdjqkbreport  extends BasePage implements PageValidateListener{
	
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	
	//开始日期
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
		}
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
//			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			isBegin=true;
			
		}
	if(visit.getRenyjb()==3){
		if(!this.getTreeid().equals(visit.getDiancxxb_id()+"")){
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
//			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			isBegin=true;
				
		}
	}
	
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="Fadbmdjqkbreport";//发电标煤单价表
	private String mstrReportName="Fadbmdjqkbreport";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "无此报表";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	private String getSelectData(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		String zhuangt="";
		String guolzj="";
		String yb="";
			if(visit.getRenyjb()==3){
				yb="yuezbb";
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				ResultSet rs=cn.getResultSet("select zhi from xitxxb where mingc='cpi报表是否查询视图' and zhuangt=1");
				try {
					if(rs.next()) {
						String dcids=rs.getString("zhi");
						if(dcids.indexOf(visit.getDiancxxb_id()+"")>=0) {
							yb="vwyuezbb";
						}else {
							yb="yuezbb";
						}
					}else {
						yb="yuezbb";
					}
					zhuangt=" and (y.zhuangt=1 or y.zhuangt=2)";
					cn.Close();
					rs.close();
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				
			}else if(visit.getRenyjb()==1){
				ResultSet rs=cn.getResultSet("select zhi from xitxxb where mingc='cpi报表是否查询视图' and zhuangt=1");
				try {
					if(rs.next()) {
						String dcids=rs.getString("zhi");
						if(dcids.indexOf(visit.getDiancxxb_id()+"")>=0) {
							yb="vwyuezbb";
						}else {
							yb="yuezbb";
						}
					}else {
						yb="yuezbb";
					}
					zhuangt=" and y.zhuangt=2";
					cn.Close();
					rs.close();
				} catch (SQLException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				
			}
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

		String strGongsID = "";
		String strDiancFID="";
		String  notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			strDiancFID="'',";
			guolzj="";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid= "+this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			strDiancFID=this.getTreeid()+",";
			guolzj=" and  a.fgs=0--grouping(dc.fgsmc)=0\n";//分公司查看报表时过滤总计。
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			strDiancFID="'',";
			guolzj="  and a.fgs=0 --grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			strDiancFID="'',";
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		//报表内容
		String biaot="";
		String dianc="";
		String tiaoj="";
		String fenz="";
		String grouping="";
        String shul="";
        String dianckjmx_bm=""; 
		String dianckjmx_tj="";
		String strzt="";
		String koujid="";
		/*String strFunctionName ="";
		if(getYuebValue().getValue().equals("全部")){
			dianckjmx_bm="";
			dianckjmx_tj="";
			strFunctionName = "getShenhzt";
		}else{
			dianckjmx_bm=",dianckjmx kjmx";
			dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
			strFunctionName = "getShenhzt_fenkj";
		}
		String strzt=strFunctionName+"(dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','本月'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+" )as bqby,\n" + 
		             strFunctionName+"(dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','累计'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+") as bqlj,\n" + 
		             strFunctionName+"(dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','本月'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+") as tqby,\n" + 
		             strFunctionName+"(dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','累计'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+") as tqlj\n" ;*/
		
		if(getYuebValue().getValue().equals("全部")){
			dianckjmx_bm="";
			dianckjmx_tj="";
			koujid="'',";
	    }else{
	    	dianckjmx_bm=",dianckjmx kjmx";
	    	dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
	    	koujid=getYuebValue().getId()+",";
	    }
		
		strzt="nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','本月'"+","+visit.getRenyjb()+"),0)as bqby,\n" + 
		      "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','累计'"+","+visit.getRenyjb()+"),0) as bqlj,\n" + 
		      "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','本月'"+","+visit.getRenyjb()+"),0) as tqby,\n" + 
		      "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','累计'"+","+visit.getRenyjb()+"),0) as tqlj\n" ;
		if(jib==3){
			biaot="a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
			"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
			"from ( select"+
			" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
			dianc=" vwdianc dc \n";
			tiaoj="";
			fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
				 "--having not grouping(fx.fenx)=1"+ guolzj+ 
				 "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
				 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
				 " )a   where a.f=0 and a.dcmc=0 ";
			grouping="   ,grouping(dc.fgsmc)  fgs \n";
		}else{
		if(getBaoblxValue().getValue().equals("按地区统计")){
				/*biaot=
					"a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
					"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
					"from ( select"+
                    " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'总计',1,dq.mingc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,dq.mingc as dqmc,'' as fgsmc,dc.mingc as diancmc";
				dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
				tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
				fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" + 
					 "--having not grouping(fx.fenx)=1\n" + 
					 " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" + 
					 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
				 " )a   where a.f=0 ";*/
			biaot=
				"a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
				"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
				"from ( select"+
                " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'总计',1,dq.mingc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,decode("+jib+",2,'',dq.mingc) as dqmc,decode("+jib+",1,'',(select mingc from diancxxb where id="+this.getTreeid()+")) fgsmc,dc.mingc as diancmc";
			dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
			tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
			fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" + 
				 "--having not grouping(fx.fenx)=1\n" + 
				 " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" + 
				 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
			 " )a   where a.f=0 ";
			}else if(getBaoblxValue().getValue().equals("按电厂统计")){
				biaot="a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
				"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
				"from ( select"+
				" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
				dianc=" vwdianc dc \n";
				tiaoj="";
				fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
					 "--having not grouping(fx.fenx)=1"+ guolzj+ 
					 "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
					 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
					 " )a   where a.f=0 "+guolzj;
				grouping="   ,grouping(dc.fgsmc)  fgs \n";
			}else if(getBaoblxValue().getValue().equals("按分公司统计")){
				biaot="a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
				"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
				"from ( select"+
					" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
				dianc=" vwdianc dc \n";
				tiaoj="";
				fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
				      "--having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)"+guolzj+ 
					 " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
					 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
					 " )a   where a.f=0  and   dcmc=1  "+guolzj;
				grouping="   ,grouping(dc.fgsmc)  fgs \n";
			}
		}
			if(getKoujlxValue().getValue().equals("等比口径")){
				shul="         sum(y.FADYTRML*dc.konggbl) as fadhml,sum(y.GONGRYTRML*dc.konggbl) as gongrhml,\n"
				+ "            sum(y.FADMZBML*dc.konggbl+y.FADYZBZML*dc.konggbl+y.FADQZBZML*dc.konggbl) as fadzhbml,\n"
				+ "            sum(y.FADMZBML*dc.konggbl) as fadbml,sum(y.FADYZBZML*dc.konggbl) as fadybml,\n"
				+ "            sum(y.GONGRMZBML*dc.konggbl+y.GONGRYZBZML*dc.konggbl+y.GONGRQZBZML*dc.konggbl) as gongrzhbml,\n"
				+ "            sum(y.GONGRMZBML*dc.konggbl) as grhmbml,sum(y.GONGRYZBZML*dc.konggbl) as grhybml,\n"
				+ "            sum(y.QIZ_FADTRMDJ) as fadtrmdj,sum(y.QIZ_GONGRTRMDJ) as gongrtrmdj,\n"
				+ "            decode(sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML),0,0, Round(sum((y.FADMCB+y.FADYCB+y.FADRQCB+Y.GONGRCYDFTRLF)*dc.konggbl)*10000/sum(y.FADMZBML*dc.konggbl+y.FADYZBZML*dc.konggbl+y.FADQZBZML*dc.konggbl),2)) as fadzhbmdj,\n"
				+ "            decode(sum(y.FADMZBML),0,0,Round(sum((y.FADMCB+y.QIZ_RANM)*dc.konggbl)*10000/sum(y.FADMZBML*dc.konggbl),2)) as fdmzbmdj,\n"
				+ "            decode(sum(y.FADYZBZML),0,0, Round(sum((y.FADYCB+y.QIZ_RANY)*dc.konggbl)*10000/sum(y.FADYZBZML*dc.konggbl),2)) as fdyzbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML),0,0,Round(sum((y.GONGRMCB+y.GONGRYCB+y.GONGRRQCB-y.GONGRCYDFTRLF)*dc.konggbl)*10000/sum(y.GONGRMZBML*dc.konggbl+y.GONGRYZBZML*dc.konggbl+y.GONGRQZBZML*dc.konggbl),2)) as grzhbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML),0,0,Round(sum((y.GONGRMCB-QIZ_RANM)*dc.konggbl)*10000/sum(y.GONGRMZBML*dc.konggbl),2)) as grmzbmdj,\n"
				+ "            decode(sum(y.GONGRYZBZML),0,0,Round(sum((y.GONGRYCB-QIZ_RANY)*dc.konggbl)*10000/sum(y.GONGRYZBZML*dc.konggbl),2)) as gryzbmdj\n";
			}else{
				shul="         sum(y.FADYTRML) as fadhml,sum(y.GONGRYTRML) as gongrhml,\n"
				+ "            sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML) as fadzhbml,\n"
				+ "            sum(y.FADMZBML) as fadbml,sum(y.FADYZBZML) as fadybml,\n"
				+ "            sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML) as gongrzhbml,\n"
				+ "            sum(y.GONGRMZBML) as grhmbml,sum(y.GONGRYZBZML) as grhybml,\n"
				+ "            sum(y.QIZ_FADTRMDJ) as fadtrmdj,sum(y.QIZ_GONGRTRMDJ) as gongrtrmdj,\n"
				+ "            decode(sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML),0,0, Round(sum(y.FADMCB+y.FADYCB+y.FADRQCB+Y.GONGRCYDFTRLF)*10000/sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML),2)) as fadzhbmdj,\n"
				+ "            decode(sum(y.FADMZBML),0,0,Round(sum(y.FADMCB+y.QIZ_RANM)*10000/sum(y.FADMZBML),2)) as fdmzbmdj,\n"
				+ "            decode(sum(y.FADYZBZML),0,0, Round(sum(y.FADYCB+y.QIZ_RANY)*10000/sum(y.FADYZBZML),2)) as fdyzbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML),0,0,Round(sum(y.GONGRMCB+y.GONGRYCB+y.GONGRRQCB-y.GONGRCYDFTRLF)*10000/sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML),2)) as grzhbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML),0,0,Round(sum(y.GONGRMCB-QIZ_RANM)*10000/sum(y.GONGRMZBML),2)) as grmzbmdj,\n"
				+ "            decode(sum(y.GONGRYZBZML),0,0,Round(sum(y.GONGRYCB-QIZ_RANY)*10000/sum(y.GONGRYZBZML),2)) as gryzbmdj\n";
			}
				 strSQL = "select "+biaot+",\n"
				+ "       fx.fenx,\n"
				+ "       decode(sum(bq.fadhml),0,0,Round(sum(bq.fadtrmdj*bq.fadhml)/sum(bq.fadhml),2)) as bq_fdtrmdj,\n"
				+ "       decode(sum(tq.fadhml),0,0,Round(sum(tq.fadtrmdj*tq.fadhml)/sum(tq.fadhml),2)) as tq_fdtrmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.gongrhml),0,0,Round(sum(bq.gongrtrmdj*bq.gongrhml)/sum(bq.gongrhml),2)) as bq_grtrmdj,\n"
				+ "       decode(sum(tq.gongrhml),0,0,Round(sum(tq.gongrtrmdj*tq.gongrhml)/sum(tq.gongrhml),2)) as tq_grtrmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.fadzhbml),0,0,Round(sum(bq.fadzhbmdj*bq.fadzhbml)/sum(bq.fadzhbml),2))  as bq_fdzhbmdj,\n"
				+ "       decode(sum(tq.fadzhbml),0,0,Round(sum(tq.fadzhbmdj*tq.fadzhbml)/sum(tq.fadzhbml),2))  as tq_fdzhbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.fadbml),0,0,Round(sum(bq.fdmzbmdj*bq.fadbml)/sum(bq.fadbml),2)) as bq_fadbmdj,\n"
				+ "       decode(sum(tq.fadbml),0,0,Round(sum(tq.fdmzbmdj*tq.fadbml)/sum(tq.fadbml),2)) as tq_fadbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.fadybml),0,0,Round(sum(bq.fdyzbmdj*bq.fadybml)/sum(bq.fadybml),2)) as bq_fadybmdj,\n"
				+ "       decode(sum(tq.fadybml),0,0,Round(sum(tq.fdyzbmdj*tq.fadybml)/sum(tq.fadybml),2)) as tq_fadybmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.gongrzhbml),0,0,Round(sum(bq.grzhbmdj*bq.gongrzhbml)/sum(bq.gongrzhbml),2)) as bq_grzhbmdj,\n"
				+ "       decode(sum(tq.gongrzhbml),0,0,Round(sum(tq.grzhbmdj*tq.gongrzhbml)/sum(tq.gongrzhbml),2)) as tq_grzhbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.grhmbml),0,0,Round(sum(bq.grmzbmdj*bq.grhmbml)/sum(bq.grhmbml),2)) as bq_grmbmdj,\n"
				+ "       decode(sum(tq.grhmbml),0,0,Round(sum(tq.grmzbmdj*tq.grhmbml)/sum(tq.grhmbml),2)) as tq_grmbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.grhybml),0,0,Round(sum(bq.gryzbmdj*bq.grhybml)/sum(bq.grhybml),2)) as bq_grhybmdj,\n"
				+ "       decode(sum(tq.grhybml),0,0,Round(sum(tq.gryzbmdj*tq.grhybml)/sum(tq.grhybml),2)) as tq_grhybmdj\n"
				+" \n         , grouping(dc.mingc) dcmc,grouping(fx.fenx) f"+grouping
				+ "  \n from\n"
				+ "          (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from "+yb+"\n"
				+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
				+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual union select decode(1,1,'累计','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n"  
				+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n" 
				+ "      ((select decode(1,1,'本月','') as fenx,y.diancxxb_id,"+shul+"\n"
				+ "        from "+yb+" y,diancxxb dc\n"
				+ "       where y.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd')\n"
				+ "         and y.fenx = '本月' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "       group by (y.diancxxb_id))\n"
				+ "       union\n"
				+ "       (select decode(1,1,'累计','') as fenx, y.diancxxb_id,"+shul+"\n"
				+ "        from "+yb+" y,diancxxb dc\n"
				+ "       where y.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd')\n"
				+ "         and y.fenx = '累计' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "       group by (y.diancxxb_id))) bq,\n"
				+ "\n"
				+ "       ((select decode(1,1,'本月','') as fenx,y.diancxxb_id,"+shul+"\n"
				+ "      from "+yb+" y,diancxxb dc\n"
				+ "     where y.riq = last_year_today(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'))\n"
				+ "       and y.fenx = '本月' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "     group by (y.diancxxb_id))\n"
				+ "     union\n"
				+ "     (select decode(1,1,'累计','') as fenx, y.diancxxb_id,"+shul+"\n"
				+ "      from "+yb+" y,diancxxb dc\n"
				+ "     where y.riq = last_year_today(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'))\n"
				+ "       and y.fenx = '累计' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "     group by (y.diancxxb_id))) tq,\n" + dianc
				+ "where fx.diancxxb_id=bq.diancxxb_id(+)\n"
				+ "and   fx.diancxxb_id=tq.diancxxb_id(+)\n"
				+ "and   fx.diancxxb_id=dc.id  "+tiaoj+" \n"
				+ "and   fx.fenx=bq.fenx(+)\n"
				+ "and   fx.fenx=tq.fenx(+)\n"
				+strGongsID+"\n" 
				 +  fenz;
//				+ "group by rollup (fx.fenx,f.mingc,dc.mingc)\n"
//				+ "having not grouping (fx.fenx)=1  "+notHuiz+"\n"
//				+ "order by  grouping(f.mingc) desc,max(f.xuh),f.mingc,grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc,grouping(fx.fenx) desc ,fx.fenx";


// 直属分厂汇总
				 ArrHeader=new String[3][22];
				 ArrHeader[0]=new String[] {"单位名称","当月或累计","发电天然煤单价","发电天然煤单价","供热天然煤单价","供热天然煤单价","发电综合标煤单价","发电综合标煤单价","其中:煤折标煤单价","其中:煤折标煤单价","其中:油折标煤单价","其中:油折标煤单价","供热综合标煤单价","供热综合标煤单价",
						                    "其中:煤折标煤单价","其中:煤折标煤单价","其中:油折标煤单价","其中:油折标煤单价","审核状态","审核状态","审核状态","审核状态"};
				 ArrHeader[1]=new String[] {"单位名称","当月或累计","本期","同期","本期","同期","本期","同期","本期","同期","本期","同期","本期","同期","本期","同期","本期","同期","本期","本期","同期","同期"};
				 ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};
				 
				 ArrWidth=new int[] {150,40,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,0,0,0,0};
				 iFixedRows=1;
				 //String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0","0","0","0"};
				 String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
					
			//} 
			
			//System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// 数据
			
			Table tb = new Table(rs,3, 0, 1,4);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"年"+intMonth+"月发电标煤单价情况表", ArrWidth, 4);
			rt.setDefaultTitle(1, 4, "填报单位(盖章):"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
			rt.setDefaultTitle(10, 4, "单位:元/吨", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(15, 3, "cpi燃料管理11表", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(22);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
//			给未审核电厂设置背景色：只要有未审核数据就红色标记
			int rows=rt.body.getRows();
			int cols=rt.body.getCols();
			if(visit.getRenyjb()!=3){
			try {
				rs.beforeFirst();
				for(int i=4;i<rows+1;i++){
					rs.next();
					   for(int k=0;k<cols+1;k++){	
//					     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//					    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
						   if(!(rs.getString(cols+1).equals("0")&&rs.getString(cols+2).equals("0")&&
								   rs.getString(cols+3).equals("0")&&rs.getString(cols+4).equals("0"))){
							rt.body.getCell(i, k).backColor="red";
						 }
				       }
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			rt.body.setColFormat(arrFormat);
			//rt.body.setColVAlign(1, Table.ALIGN_CENTER);
			//tb.setColAlign(1, Table.ALIGN_LEFT);
			tb.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getRows()>3){
				rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			}
			
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(6,2,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(12,3,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-5,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
	
//	得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return diancmc;
		
	}
	
	//得到系统信息表中配置的报表标题的单位名称
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
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}
//	报表类型
	
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		try{
			List baoblxList = new ArrayList();
			if(visit.getRenyjb()==1){
				baoblxList.add(new IDropDownBean(0,"按分公司统计"));
				baoblxList.add(new IDropDownBean(1,"按电厂统计"));
				baoblxList.add(new IDropDownBean(2,"按地区统计"));
			}else{
				baoblxList.add(new IDropDownBean(0,"按电厂统计"));
				baoblxList.add(new IDropDownBean(1,"按地区统计"));
			}
			_IBaoblxModel = new IDropDownModel(baoblxList);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
//	年份
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}
 
	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * 月份
	 */
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}


	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
	 //	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}

	
	
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
//		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		
		
	    Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
//		
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
//		tb1.addText(new ToolbarText("-"));
		if(getDiancTreeJib()!=3){
			tb1.addText(new ToolbarText("统计口径:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BaoblxDropDown");
			cb.setWidth(120);
//			cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		
			tb1.addText(new ToolbarText("月报口径:"));
			ComboBox cb2 = new ComboBox();
			cb2.setTransform("YuebDropDown");
			cb2.setWidth(120);
//			cb2.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb2);
			tb1.addText(new ToolbarText("-"));
		}
		
			tb1.addText(new ToolbarText("口径类型:"));
			ComboBox cb3 = new ComboBox();
			cb3.setTransform("KoujlxDropDown");
			cb3.setWidth(120);
//			cb3.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb3);
			tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
		
		
	}
	
	
	
	
	
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
//	月报口径
	private boolean _yuebchange = false;
	public IDropDownBean getYuebValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			if (getYuebModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getYuebModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setYuebValue(IDropDownBean Value) {
		if (getYuebValue().getId() != Value.getId()) {
			_yuebchange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setYuebModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getYuebModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getIYuebModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public void getIYuebModels() {
	
		String sql ="select kj.id as id,kj.mingc as mingc from dianckjb kj\n" +
			"\t\twhere kj.fenl_id in (select distinct id from item i where i.bianm='YB' and i.zhuangt=1)\n" + 
			"    and kj.diancxxb_id=" +((Visit) getPage().getVisit()).getDiancxxb_id();
		
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "全部"));
	}
//	0------------------------
//	月报口径类型
	private boolean _koujlxchange = false;
	public IDropDownBean getKoujlxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
			if (getKoujlxModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getKoujlxModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setKoujlxValue(IDropDownBean Value) {
		if (getKoujlxValue().getId() != Value.getId()) {
			_koujlxchange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setKoujlxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getKoujlxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getIKoujlxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public void getIKoujlxModels() {
	
		String sql ="select id,mingc from item where bianm='YB'"; 
		
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql));
	}
	/////////////////////////////////////////////////////
	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//
//	public String getTreeScript() {
//		return getTree().getWindowTreeScript();
//	}

	
}