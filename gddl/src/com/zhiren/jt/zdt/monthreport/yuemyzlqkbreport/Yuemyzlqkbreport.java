package com.zhiren.jt.zdt.monthreport.yuemyzlqkbreport;

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

import org.apache.tapestry.contrib.palette.SortMode;
/* 
* 时间：2009-04-16
* 作者： ll
* 修改内容：修改查询sql，判断电厂id为141(芜湖电厂)，入厂低位热值取yuezlb中的diancrz字段，其它电厂入厂低位热值取yuezlb中的qnet_ar字段.
* 		   
*/ 
/* 
* 时间：2009-05-4
* 作者： ll
* 修改内容：1、修改查询sql，判断电厂入厂低位热值为0时，入厂、入炉热值差为0.
* 			2、修改查询sql中yuezbb表中字段的名称,按照yuezbb中新的公式，取新的字段名。
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
* 时间：2009-12-03
* 作者： ll
* 修改内容：1、月报-cpi09表入厂热值增加两列  “其中重点”，“其中市场”
			其中重点=重点+区域
			其中市场=市场
* 		   
*/ 
/* 
* 时间：2010-01-4
* 作者： ll
* 修改内容：1、增加“按分公司统计”条件查询。
* 			2、增加“月报口径”下拉框，对数据进行查询。
*/
/* 
* 时间：2010-03-31
* 作者： ll
* 修改内容：1、修改页面显示sql，原sql中各表的全连接改为左连接。
*/
/* 
* 时间：2010-09-19
* sy
* 修改内容：1修改sql，不使用having not，否则有oracle版本bug，会出错
*/ 
/* 
* 时间：2010-11-04
* 作者：liufl
* 修改内容：1、增加"口径类型"下拉菜单，可选择"等比口径"和"月报电厂口径"
*          2、未审核数据用红色背景显示
*/ 
/* 
* 时间：2010-12-20
* 作者：liufl
* 修改内容：1、修改sql，电厂树选电厂+按电厂统计时过滤公司总计
*/ 
/* 
* 时间：2010-12-31
* 作者：liufl
* 修改内容：1、修改sql，单位选电厂时不显示“统计口径”下拉框
*            2、单位选分公司+按地区统计时红色显示不对，修改sql
*/
/* 
* 时间：2011-01-10
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
* 时间：2012-01-12
* 作者：liufl
* 修改内容：添加系统参数控制是否查询视图
*            
*/
public class Yuemyzlqkbreport  extends BasePage implements PageValidateListener{
	
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
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			isBegin=true;
			this.getSelectData();
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
				this.getTree();
				visit.setDropDownBean4(null);
				visit.setProSelectionModel4(null);
				visit.setProSelectionModel2(null);
				visit.setDropDownBean2(null);
				this.setBaoblxValue(null);
				this.getIBaoblxModels();
				isBegin=true;
				this.getSelectData();	
			}
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}

		if(_fengschange){
			
			_fengschange=false;
			Refurbish();
		}
		getToolBars() ;
		Refurbish();
	}
//	得到系统信息表中配置的报表标题的单位名称
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
	//--------------------------
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	
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
	    long intyear;
	    long intMonth;
	    Visit visit = (Visit)getPage().getVisit();
	    String strSQL = "";
	    this._CurrentPage = 1;
	    this._AllPages = 1;

	    JDBCcon cn = new JDBCcon();
	    String zhuangt = "";
	    String shulzt = "";
	    String zhilzt = "";
	    String guolzj = "";
	    String guolzj2 = "";
	    String yb_sl = "";
	    String yb_zl = "";
	    String yb_zb = "";
	    String yb_rcy = "";
	    if (visit.getRenyjb() == 3) {
	      yb_sl = "yueslb";
	      yb_zl = "yuezlb";
	      yb_zb = "yuezbb";
	      yb_rcy = "rucycbb";
	      zhuangt = "";
	      shulzt = "";
	      zhilzt = ""; } else {
	      ResultSet rs;
	      String dcids;
	      if (visit.getRenyjb() == 2) {
	        rs = cn.getResultSet("select zhi from xitxxb where mingc='cpi报表是否查询视图' and zhuangt=1");
	        try {
	          if (rs.next()) {
	            dcids = rs.getString("zhi");
	            if (dcids.indexOf(visit.getDiancxxb_id()+"") >= 0) {
	              yb_sl = "vwyueslb";
	              yb_zl = "vwyuezlb";
	              yb_zb = "vwyuezbb";
	              yb_rcy = "vwrucycbb";
	            } else {
	              yb_sl = "yueslb";
	              yb_zl = "yuezlb";
	              yb_zb = "yuezbb";
	              yb_rcy = "rucycbb";
	            }
	          } else {
	            yb_sl = "yueslb";
	            yb_zl = "yuezlb";
	            yb_zb = "yuezbb";
	            yb_rcy = "rucycbb";
	          }
	          zhuangt = " and (zb.zhuangt=1 or zb.zhuangt=2) ";
	          shulzt = " and (sl.zhuangt=1 or sl.zhuangt=2) ";
	          zhilzt = " and (zl.zhuangt=1 or zl.zhuangt=2) ";
	          cn.Close();
	          rs.close();
	        }
	        catch (SQLException e) {
	          e.printStackTrace();
	        }
	      }
	      else if (visit.getRenyjb() == 1) {
	        rs = cn.getResultSet("select zhi from xitxxb where mingc='cpi报表是否查询视图' and zhuangt=1");
	        try {
	          if (rs.next()) {
	        	  String e = rs.getString("zhi");
	            if (e.indexOf(visit.getDiancxxb_id()+"") >= 0) {
	              yb_sl = "vwyueslb";
	              yb_zl = "vwyuezlb";
	              yb_zb = "vwyuezbb";
	              yb_rcy = "vwrucycbb";
	            } else {
	              yb_sl = "yueslb";
	              yb_zl = "yuezlb";
	              yb_zb = "yuezbb";
	              yb_rcy = "rucycbb";
	            }
	          } else {
	            yb_sl = "yueslb";
	            yb_zl = "yuezlb";
	            yb_zb = "yuezbb";
	            yb_rcy = "rucycbb";
	          }
	          zhuangt = " and zb.zhuangt=2 ";
	          shulzt = " and sl.zhuangt=2 ";
	          zhilzt = " and zl.zhuangt=2 ";
	          cn.Close();
	          rs.close();
	        }
	        catch (SQLException e) {
	          e.printStackTrace();
	        }
	      }
	    }

	    if (getNianfValue() == null)
	      intyear = DateUtil.getYear(new Date());
	    else {
	      intyear = getNianfValue().getId();
	    }

	    if (getYuefValue() == null)
	      intMonth = DateUtil.getMonth(new Date());
	    else {
	      intMonth = getYuefValue().getId();
	    }

	    String strGongsID = "";
	    String strDiancFID = "";
	    String danwmc = "";
	    int jib = getDiancTreeJib();
	    if (jib == 1) {
	      strGongsID = " ";
	      guolzj = "";
	      strDiancFID = "'',";
	    } else if (jib == 2) {
	      strGongsID = "  and (dc.fuid=  " + getTreeid() + " or dc.shangjgsid=" + getTreeid() + ")";
	      guolzj = " and  a.fgs=0--grouping(dc.fgsmc)=0\n";
	      guolzj2 = "";
	      strDiancFID = getTreeid() + ",";
	    } else if (jib == 3) {
	      strGongsID = " and dc.id= " + getTreeid();
	      guolzj = "   and a.fgs=0 --grouping(dc.mingc)=0\n";
	      guolzj2 = " and a.dcmc=0";
	      strDiancFID = "'',";
	    } else if (jib == -1) {
	      strGongsID = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id();
	      strDiancFID = "'',";
	    }

	    danwmc = getTreeDiancmc(getTreeid());

	    Report rt = new Report();
	    int[] ArrWidth = null;
	    String[][] ArrHeader = null;
	    String titlename = "煤(油)质量情况表";
	    int iFixedRows = 0;
	    int iCol = 0;

	    String biaot = "";
	    String dianc = "";
	    String tiaoj = "";
	    String fenz = "";
	    String grouping = "";
	    String dianckjmx_bm = "";
	    String dianckjmx_tj = "";
	    String strzt = "";
	    String koujid = "";

	    if (getYuebValue().getValue().equals("全部")) {
	      dianckjmx_bm = "";
	      dianckjmx_tj = "";
	      koujid = "'',";
	    } else {
	      dianckjmx_bm = ",dianckjmx kjmx";
	      dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
	      koujid = getYuebValue().getId() + ",";
	    }

	    strzt = "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),'rucycbb','本月'" + "," + visit.getRenyjb() + "),0)as bqby,\n" + 
	      "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),'rucycbb','累计'" + "," + visit.getRenyjb() + "),0) as bqlj,\n" + 
	      "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),'rucycbb','本月'" + "," + visit.getRenyjb() + "),0) as tqby,\n" + 
	      "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),'rucycbb','累计'" + "," + visit.getRenyjb() + "),0) as tqlj\n";
	    if (jib == 3)
	    {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select" + 
	        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc1";
	      dianc = " vwdianc dc \n";
	      tiaoj = "";
	      fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n--having not grouping(fx.fenx)=1" + 
	        guolzj + 
	        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
	        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" + 
	        " )a   where a.f=0 and a.dcmc=0";
	      grouping = "   ,grouping(dc.fgsmc)  fgs \n";
	    }
	    else if (getBaoblxValue().getValue().equals("按地区统计"))
	    {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select \n" + 
	        " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'总计',1,dq.mingc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(" + jib + ",2,'',dq.mingc) as dqmc,decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + getTreeid() + ")) fgsmc,dc.mingc as diancmc1";
	      dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
	      tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
	      fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n--having not grouping(fx.fenx)=1\n \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\ngrouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) )a   where a.f=0 ";
	    }
	    else if (getBaoblxValue().getValue().equals("按电厂统计")) {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select" + 
	        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc||'合计','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc1";
	      dianc = " vwdianc dc \n";
	      tiaoj = "";
	      fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n--having not grouping(fx.fenx)=1" + 
	        guolzj + 
	        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
	        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" + 
	        " )a   where a.f=0 " + guolzj + guolzj2;
	      grouping = "   ,grouping(dc.fgsmc)  fgs \n";
	    } else if (getBaoblxValue().getValue().equals("按分公司统计")) {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select" + 
	        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,'' as diancmc1";
	      dianc = " vwdianc dc \n";
	      tiaoj = "";
	      fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n--having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)" + 
	        guolzj + 
	        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
	        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" + 
	        " )a   where a.f=0  and   dcmc=1  " + guolzj;
	      grouping = "   ,grouping(dc.fgsmc)  fgs \n";
	    }

	    String shul = "";
	    String zhib = "";
	    String zhib2 = "";
	    if (getKoujlxValue().getValue().equals("等比口径")) {
	      shul = "decode(sum(sl.laimsl),0,0,sum((sl.laimsl*dc.konggbl) *decode(dc.id,141,zl.diancrz,zl.qnet_ar)) /sum(sl.laimsl*dc.konggbl)) as rcfal,\nsum(sl.laimsl*dc.konggbl) as laimsl";

	      zhib = "sum(zb.FADGRYTRML*dc.konggbl) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz";
	      zhib2 = "sum(rcy.shul*dc.konggbl) as rucysl,decode(sum(rcy.shul),0,0,sum((rcy.shul*dc.konggbl) * rcy.youfrl) /sum(rcy.shul*dc.konggbl)) as rucyrl";
	    } else {
	      shul = "decode(sum(sl.laimsl),0,0,sum(sl.laimsl *decode(dc.id,141,zl.diancrz,zl.qnet_ar)) /sum(sl.laimsl)) as rcfal,\nsum(sl.laimsl) as laimsl";

	      zhib = "sum(zb.FADGRYTRML) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz";
	      zhib2 = "sum(rcy.shul) as rucysl,decode(sum(rcy.shul),0,0,sum(rcy.shul * rcy.youfrl) /sum(rcy.shul)) as rucyrl";
	    }

	    strSQL = 
	      "select  " + biaot + ",\n" + 
	      "  decode(1,1,fx.fenx,'') as fenx,\n" + 
	      "                   decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000) as ruc_frl_bq ,\n" + 
	      "                   decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000) as ruc_frl_tq,\n" + 
	      "                   decode(sum(bq.laimsl_zd),0,0,round(sum(bq.rc_farl_zd*bq.laimsl_zd)/sum(bq.laimsl_zd),2)*1000) as ruc_frl_zd_bq ,\n" + 
	      "                   decode(sum(tq.laimsl_zd),0,0,round(sum(tq.rc_farl_zd*tq.laimsl_zd)/sum(tq.laimsl_zd),2)*1000) as ruc_frl_zd_tq,\n" + 
	      "                   decode(sum(bq.laimsl_sc),0,0,round(sum(bq.rc_farl_sc*bq.laimsl_sc)/sum(bq.laimsl_sc),2)*1000) as ruc_frl_sc_bq ,\n" + 
	      "                   decode(sum(tq.laimsl_sc),0,0,round(sum(tq.rc_farl_sc*tq.laimsl_sc)/sum(tq.laimsl_sc),2)*1000) as ruc_frl_sc_tq,\n" + 
	      "\n" + 
	      "                   decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)*1000) as rul_frl_bq,\n" + 
	      "                   decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)*1000) as rul_frl_tq,\n" + 
	      "                   decode(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000),0,0,(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2))-decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)))*1000) as rezc_bq,\n" + 
	      "                   decode(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000),0,0,(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2))-decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)))*1000) as rezc_tq,\n" + 
	      "                    decode(sum(bq.rulyl),0,0,round(sum(bq.sy_farl*bq.rulyl)/sum(bq.rulyl),2)*1000) as sy_farl_bq,\n" + 
	      "                    decode(sum(tq.rulyl),0,0,round(sum(tq.sy_farl*tq.rulyl)/sum(tq.rulyl),2)*1000) as sy_farl_tq\n" + 
	      "                   , grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n" + grouping + 
	      "      from\n" + 
	      "       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
	      "                   (select distinct diancxxb_id from  " + yb_sl + " sl, " + yb_zl + " zl,yuetjkjb tj\n" + 
	      "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) and (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	      "                    union\n" + 
	      "                     select distinct diancxxb_id from  " + yb_zb + "\n" + 
	      "                        where  (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	      "                    ) yid,\n" + 
	      "               (select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计') as fenx,2 as xhu from dual ) fx,diancxxb dc" + dianckjmx_bm + "\n" + 
	      "               where dc.id=yid.diancxxb_id   " + dianckjmx_tj + strGongsID + "   ) fx,\n" + 
	      "\n" + 
	      "             (select  decode(1,1,'本月') as fenx,dc.id as diancxxb_id,\n" + 
	      "                    r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='本月'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='本月'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "  and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "            union\n" + 
	      "              select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
	      "                                          r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='累计'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='累计'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "     )bq,\n" + 
	      "          (\n" + 
	      "         select  decode(1,1,'本月') as fenx,dc.id as diancxxb_id,\n" + 
	      "                  r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='本月'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='本月'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "            union\n" + 
	      "              select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
	      "                        r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj, " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='累计'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='累计'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "              )tq, \n" + dianc + 
	      "          where dc.id=fx.diancxxb_id " + tiaoj + "\n" + 
	      "                 and  fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
	      "                 and  fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
	      "                 and fx.fenx=tq.fenx(+)\n" + 
	      "                 and fx.fenx=bq.fenx(+)\n" + fenz;

	    ArrHeader = new String[3][18];
	    ArrHeader[0] = new String[]{ "单位名称", "单位名称", "入厂煤平均低位发热量", "入厂煤平均低位发热量", "其中：重点订货", "其中：重点订货", "其中：市场采购", "其中：市场采购", "入炉煤平均低位发热量", "入炉煤平均低位发热量", 
	      "入厂、入炉热值差", "入厂、入炉热值差", "天然油平均发热量", "天然油平均发热量", "审核状态", "审核状态", "审核状态", "审核状态" };
	    ArrHeader[1] = new String[]{ "单位名称", "单位名称", "本期", "同期", "本期", "同期", "本期", "同期", "本期", "同期", "本期", "同期", "本期", "同期", "本期", "本期", "同期", "同期" };
	    ArrHeader[2] = new String[]{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };

	    ArrWidth = new int[] { 150, 60, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70 };

	    iFixedRows = 1;
	    iCol = 10;
	//---------------qin
	    String SQL = "";
	    long intMonth2 = intMonth + 1;
	    long intyear2 = intyear;
	    if (intMonth2 > 12) {
	      intyear2 = intyear + 1;
	      intMonth2 =intMonth2 - 12;
	    }

	    if (intyear >= 2016)
	    {
	      String strSQL2 = 
	        "select  " + biaot + ",\n" + 
	        "  decode(1,1,fx.fenx,'') as fenx,\n" + 
	        "                   decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000) as ruc_frl_bq ,\n" + 
	        "                   decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000) as ruc_frl_tq,\n" + 
	        "                   decode(sum(bq.laimsl_zd),0,0,round(sum(bq.rc_farl_zd*bq.laimsl_zd)/sum(bq.laimsl_zd),2)*1000) as ruc_frl_zd_bq ,\n" + 
	        "                   decode(sum(tq.laimsl_zd),0,0,round(sum(tq.rc_farl_zd*tq.laimsl_zd)/sum(tq.laimsl_zd),2)*1000) as ruc_frl_zd_tq,\n" + 
	        "                   decode(sum(bq.laimsl_sc),0,0,round(sum(bq.rc_farl_sc*bq.laimsl_sc)/sum(bq.laimsl_sc),2)*1000) as ruc_frl_sc_bq ,\n" + 
	        "                   decode(sum(tq.laimsl_sc),0,0,round(sum(tq.rc_farl_sc*tq.laimsl_sc)/sum(tq.laimsl_sc),2)*1000) as ruc_frl_sc_tq,\n" + 
	        "\n" + 
	        "                   decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)*1000) as rul_frl_bq,\n" + 
	        "                   decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)*1000) as rul_frl_tq,\n" + 
	        "                   decode(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000),0,0,(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2))-decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)))*1000) as rezc_bq,\n" + 
	        "                   decode(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000),0,0,(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2))-decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)))*1000) as rezc_tq,\n" + 
	        "                    decode(sum(bq.rulyl),0,0,round(sum(bq.sy_farl*bq.rulyl)/sum(bq.rulyl),2)*1000) as sy_farl_bq,\n" + 
	        "                    decode(sum(tq.rulyl),0,0,round(sum(tq.sy_farl*tq.rulyl)/sum(tq.rulyl),2)*1000) as sy_farl_tq\n" + 
	        "                   , grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n" + grouping + 
	        "      from\n" + 
	        "       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
	        "                   (select distinct diancxxb_id from  " + yb_sl + " sl, " + yb_zl + " zl,yuetjkjb tj\n" + 
	        "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) and (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	        "                    union\n" + 
	        "                     select distinct diancxxb_id from  " + yb_zb + "\n" + 
	        "                        where  (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	        "                    ) yid,\n" + 
	        "               (select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计') as fenx,2 as xhu from dual ) fx,diancxxb dc" + dianckjmx_bm + "\n" + 
	        "               where dc.id=yid.diancxxb_id   " + dianckjmx_tj + strGongsID + "   ) fx,\n" + 
	        "\n" + 
	        "             (select  decode(1,1,'本月') as fenx,dc.id as diancxxb_id,\n" + 
	        "                    r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='本月'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='本月'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "  and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	        "            union\n" + 
	       //----------------qinlizhong    旧算法  jisff =1 
	        "              select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
	        "                                          r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='累计'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='累计'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        //-----------------qin
	        "                                ,suanfpzb sf   --------qin \n" +	 //-----------------qin    
	        "               where R.DIANCXXB_ID(+)=dc.id \n"+
	        //-----------------qin
	        "              and sf.diancxxb_id(+) = dc.id     ------------qin\n" +       //-----------------qin
	        "               and sf.yuebrq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')   \n" + //-----------------qin
	        "                 and sf.jisff = 1     \n" +            //-----------------qin
	        "and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	       //-----------------qin
	        "    UNION    \n" + 
	      //----------------qinlizhong    新算法  jisff = 2
	        "              select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
	        "                                          r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id,\n" + 
	        "                   sum(zb.FADGRYTRML * dc.konggbl) as rulml,\n" + 
	        "                   sum(c.rucysl) as rulyl,\n" + 
	        "                   sum(c.rucyrl) as rulyrz,\n" + 
	        //--------------------qin
	        "                   sum(d.rulmrz ) as rulmrz\n" +  //--------------------qin
	        "              from yuezbb zb,\n" + 
	        "                   diancxxb dc,\n" + 
	        "                   (select rcy.diancxxb_id,\n" + zhib2 + "\n" + 
	        "                      from rucycbb rcy, diancxxb dc\n" + 
	        "                     where rcy.riq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')\n" + 
	        "                       and rcy.fenx = '累计'\n" + 
	        "                       and rcy.diancxxb_id = dc.id\n" + strGongsID + 
	        "                     group by (rcy.diancxxb_id)) c\n" + 
	      //--------------------qin
	        "   ,( select diancxxb_id,\n" + 
	        "      sum(meil),\n" + 
	        "      round_new(sum(meil * qnet_ar) / sum(meil), 2)  as rulmrz\n" + 
	        " from (select diancxxb_id,\n" + 
	        "              rulrq,\n" + 
	        "              fenxrq,\n" + 
	        "              sum(meil) meil,\n" + 
	        "              round_new(sum(meil * round_new(qnet_ar, 2)) / sum(meil), 2) qnet_ar\n" + 
	        "         from rulmzlb, diancxxb dc\n" + 
	        "        where diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "          and rulrq >= date '" + intyear + "-01-01'\n" + 
	        "          and rulrq < date '" + intyear2 + "-" + intMonth2 + "-01'\n" + 
	        "          and shenhzt = 3\n" + 
	        "        group by diancxxb_id, rulrq, fenxrq)\n" + 
	        "group by diancxxb_id  ) d\n" + 
	      //--------------------qin	        
	        "             where zb.riq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')\n" + 
	        "               and zb.fenx = '累计'\n" + 
	        "               and zb.diancxxb_id = dc.id\n" + strGongsID + zhuangt + 
	        "               and zb.diancxxb_id = c.diancxxb_id(+)\n" + 
	        //--------------------qin
	        "               and zb.diancxxb_id = d.diancxxb_id(+)       \n" +    //--------------------qin
	        "             group by (zb.diancxxb_id))  y,vwdianc dc \n" + 
	        "             ,suanfpzb sf    -------------qin\n" +           //--------------------qin   suanfpzb
	        "               where R.DIANCXXB_ID(+)=dc.id  \n"+
	      //--------------------qin
	      "              and sf.diancxxb_id(+) = dc.id     ------------qin\n" + 
	      "                and sf.yuebrq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')   ------------qin\n" + 
	      "                  and sf.jisff = 2     ------------qin\n" +      //--------------------qin
	        "        and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	                
	       //-----------------------qin 
	        "     )bq,\n" + 
	        "          (\n" + 
	        "         select  decode(1,1,'本月') as fenx,dc.id as diancxxb_id,\n" + 
	        "                  r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='本月'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='本月'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	        "            union\n" + 
	  //---------同期累计-----旧算法    	        
	        "              select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
	        "                        r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj, " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='累计'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='累计'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        //---------------qin
	        "             ,suanfpzb sf    -------------qin8  \n" +
	        "               where R.DIANCXXB_ID(+)=dc.id \n"+
	      //---------------qin
	      "      and sf.diancxxb_id(+) = dc.id     ------------qin2\n" + //---------------qin
	        "    and sf.yuebrq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12)  ------------qin3\n" +  //---------------qin
	        "    and sf.jisff = 1    ------------qin4\n" +       //---------------qin
	        "    and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      //---------------qin
	        "    union\n" +
	      //---------------qin  ----同期累计-----新算法  	        
        "              select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
        "                        r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
        "              from (\n" + 
        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
        "                                                 group by (tj.diancxxb_id) )a,\n" + 
        "\n" + 
        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
        "                                                 group by (tj.diancxxb_id) )b,\n" + 
        "\n" + 
        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
        "                                                 from  " + yb_sl + " sl, yuetjkjb tj, " + yb_zl + " zl,diancxxb dc\n" + 
        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
        "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
        "                                                 group by (tj.diancxxb_id) )c\n" + 
        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='累计'\n" + 
        "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
      //---------------qin  d
        
"-------qin6\n" +
"                                     ,(select diancxxb_id,\n" + 
"                       sum(meil),\n" + 
"                       round_new(sum(meil * qnet_ar) / sum(meil), 2) as rulmrz\n" + 
"                  from (select diancxxb_id,\n" + 
"                               rulrq,\n" + 
"                               fenxrq,\n" + 
"                               sum(meil) meil,\n" + 
"                               round_new(sum(meil * round_new(qnet_ar, 2)) /\n" + 
"                                         sum(meil),\n" + 
"                                         2) qnet_ar\n" + 
"                          from rulmzlb, diancxxb dc\n" + 
"                         where diancxxb_id = dc.id " + strGongsID + "\n" +
"                           and rulrq >= add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12)\n" + 
"                           and rulrq < add_months(to_date( '" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12)\n" + 
"                           and shenhzt = 3\n" + 
"                         group by diancxxb_id, rulrq, fenxrq)\n" + 
"                 group by diancxxb_id) d\n" + 
//---------------qin  d
        "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='累计'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
        //---------------qin
        "             ,suanfpzb sf    -------------qin8  \n" +
        "               where R.DIANCXXB_ID(+)=dc.id \n"+
      //---------------qin
      "      and sf.diancxxb_id(+) = dc.id     ------------qin2\n" + //---------------qin
        "    and sf.yuebrq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12)  ------------qin3\n" +  //---------------qin
        "    and sf.jisff = 2    ------------qin4\n" +       //---------------qin
        
        "    and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	        "              )tq, \n" + dianc + 	        
	        "          where dc.id=fx.diancxxb_id " + tiaoj + "\n" +       
	        "                 and  fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
	        "                 and  fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
	        "                 and fx.fenx=tq.fenx(+)\n" + 
	        "                 and fx.fenx=bq.fenx(+)\n" + fenz ;
	      SQL = strSQL2;
	    }
	    else
	    {
	      SQL = strSQL;
	    }
//------------qinlizhong
	    //System.out.println("-------------------------------****************--------------------------------");
	    System.out.println("SQL="+SQL);
	    ResultSet rs = cn.getResultSet(SQL);

	    Table tb = new Table(rs, 3, 0, 1, 4);
	    rt.setBody(tb);

	    rt.setTitle(getBiaotmc() + intyear + "年" + intMonth + "月" + titlename, ArrWidth, 4);
	    rt.setDefaultTitle(1, 2, "填报单位:" + getDiancmc(), -1);
	    rt.setDefaultTitle(6, 2, "填报日期:" + intyear + "年" + intMonth + "月", 2);
	    rt.setDefaultTitle(9, 2, "单位:千焦/千克", 2);
	    rt.setDefaultTitle(12, 2, "cpi燃料管理09表", 2);

	    rt.body.setWidth(ArrWidth);
	    rt.body.setPageRows(22);
	    rt.body.setHeaderData(ArrHeader);
	    rt.body.mergeFixedRow();
	    rt.body.mergeFixedCols();
	    rt.body.ShowZero = false;

	    int rows = rt.body.getRows();
	    int cols = rt.body.getCols();
	    if (visit.getRenyjb() != 3)
	      try {
	        rs.beforeFirst();
	        for (int i = 4; i < rows + 1; ++i) {
	          rs.next();
	          for (int k = 0; k < cols + 1; ++k)
	          {
	            if ((!(rs.getString(cols + 1).equals("0"))) || (!(rs.getString(cols + 2).equals("0"))) || 
	              (!(rs.getString(cols + 3).equals("0"))) || (!(rs.getString(cols + 4).equals("0"))))
	              rt.body.getCell(i, k).backColor = "red";
	          }
	        }
	      }
	      catch (SQLException e) {
	        e.printStackTrace();
	      }

	    if (rt.body.getRows() > 3) {
	      rt.body.setCellAlign(4, 1, 1);
	    }

	    rt.createDefautlFooter(ArrWidth);

	    rt.setDefautlFooter(1, 2, "打印日期:" + FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))), -1);
	    rt.setDefautlFooter(5, 3, "审核:", -1);
	    rt.setDefautlFooter(10, 2, "制表:", -1);
	    tb.setColAlign(2, 1);
	    rt.setDefautlFooter(rt.footer.getCols() - 5, 2, "(第Page/Pages页)", 2);

	    this._CurrentPage = 1;
	    this._AllPages = rt.body.getPages();
	    if (this._AllPages == 0)
	      this._CurrentPage = 0;

	    cn.Close();
	    return rt.getAllPagesHtml();
	  }
	//得到登陆人员所属电厂或分公司的名称
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
	
	
//	矿别名称
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{
//		List fahdwList = new ArrayList();
//		fahdwList.add(new IDropDownBean(-1,"请选择"));
//		
//		String sql="";
//		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
////		System.out.println(sql);
//		ResultSet rs = con.getResultSet(sql);
//		for(int i=0;rs.next();i++){
//			fahdwList.add(new IDropDownBean(i,rs.getString("meikdqmc")));
//		}
		
		String sql="";
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
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
//	 分公司下拉框
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
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
	
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		if(getDiancTreeJib()!=3){
			tb1.addText(new ToolbarText("统计口径:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BaoblxDropDown");
			cb.setWidth(120);
			cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		
			tb1.addText(new ToolbarText("月报口径:"));
			ComboBox cb2 = new ComboBox();
			cb2.setTransform("YuebDropDown");
			cb2.setWidth(120);
			cb2.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb2);
			tb1.addText(new ToolbarText("-"));
		}
		
			tb1.addText(new ToolbarText("口径类型:"));
			ComboBox cb3 = new ComboBox();
			cb3.setTransform("KoujlxDropDown");
			cb3.setWidth(120);
			cb3.setListeners("select:function(){document.Form0.submit();}");
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
	/////////////////////////////////////////////////////
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
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	
}