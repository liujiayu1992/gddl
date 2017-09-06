package com.zhiren.jt.zdt.monthreport.yuesjtzjk;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;


public class Yuesjtzjk  extends BasePage implements PageValidateListener{

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
			//this.getSelectData();
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
			isBegin=true;
		
		}
		
		getToolBars() ;
		this.Refurbish();
	}
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		return getSelectData();
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
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		String strMonth="";
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		if(intMonth<10){
			strMonth="0"+intMonth;
		}else{
			strMonth=intMonth+"";
		}
		int jib11=0,ranlgs11=0;
		String sql="select jib,diancxxb.ranlgs\n" +
		"from diancxxb\n" + 
		"where id="+getTreeid();
		JDBCcon con11=new JDBCcon();
		ResultSet rs11=con11.getResultSet(sql);
		try {
			while(rs11.next()){
				jib11=rs11.getInt("jib") ;
				ranlgs11=rs11.getInt("ranlgs") ;
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		con11.Close();
		String diancWhere="";
		String gongsRanlgs="fgsmc";
		String YearMon=intyear+"-"+strMonth;
		if(jib11==2&&ranlgs11==1){//燃料公司
			diancWhere=" and vwdianc.shangjgsid="+getTreeid()+" ";
			gongsRanlgs="rlgsmc";
		}else if(jib11==2&&ranlgs11!=1){//分公司
			diancWhere=" and vwdianc.fuid="+getTreeid()+" ";
		}else if(jib11==3){//电厂
			diancWhere=" and vwdianc.id="+getTreeid()+" ";
		}else{//集团
			diancWhere=" ";
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
//		int iFixedRows=2;//固定行号
//		int iCol=1;//列数
//		strSQL="select decode(grouping(mingc),1,decode(grouping(fgsmc),1,'总计',fgsmc),mingc)mingc1,sum(rihy)rihy,sum(yuehy)yuehy,sum(rihy)-sum(yuehy) chay1\n" +
//		",sum(rifrl)rifrl,sum(yuefrl)yuefrl,sum(rifrl)-sum(yuefrl) chay2\n" + 
//		",sum(rirlfrl)rirlfrl,sum(yuerlfrl)yuerlfrl,sum(rirlfrl)-sum(yuerlfrl) chay3\n" + 
//		",sum(rikc)rikc,sum(yuekc)yuekc,sum(rikc)-sum(yuekc) chay4\n" + 
//		"from(\n" + 
//		"--日耗用\n" + 
//		"select rh.diancxxb_id,sum(rh.fady+rh.gongry+rh.qity)rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0 yuerlfrl,0 rikc,0 yuekc\n" + 
//		"from shouhcrbb rh,vwdianc\n" + 
//		"where rh.diancxxb_id=vwdianc.id "+diancWhere+" and to_char(rh.riq,'yyyy-mm')='"+YearMon+"'\n" + 
//		"group by  rh.diancxxb_id\n" + 
//		"--月耗用\n" + 
//		"union\n" + 
//		"select diancxxb_id,0 rihy,(fady+gongry+qith)yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0 yuerlfrl,0rikc,0 yuekc\n" + 
//		"from yueshchjb yh,vwdianc\n" + 
//		"where yh.diancxxb_id=vwdianc.id and to_char(yh.riq,'yyyy-mm')='"+YearMon+"' "+diancWhere+" and fenx='本月'\n" + 
//		"--日入厂热量\n" + 
//		"union\n" + 
//		"select fahb.diancxxb_id,\n" + 
//		"0 rihy,0 yuehy,decode(sum(fahb.laimsl),0,0,round_new(sum(zhilb.qnet_ar*fahb.laimsl)/sum(fahb.laimsl),2)) rifrl\n" + 
//		",0 yuefrl,0 rirlfrl,0 yuerlfrl,0rikc,0 yuekc\n" + 
//		"from fahb,zhilb,vwdianc\n" + 
//		"where fahb.zhilb_id=zhilb.id and fahb.diancxxb_id=vwdianc.id\n" + 
//		"and  to_char(fahb.daohrq,'yyyy-mm')='"+YearMon+"' "+diancWhere+"\n" + 
//		"group by fahb.diancxxb_id\n" + 
//		"union\n" + 
//		"--月入厂热量\n" + 
//		"select yuetjkjb.diancxxb_id,\n" + 
//		"0 rihy,0 yuehy,0 rifrl,decode(sum(yueslb.laimsl),0,0,round_new(sum(yuezlb.qnet_ar*yueslb.laimsl)/sum(yueslb.laimsl),2)) yuefrl,0 rirlfrl,0 yuerlfrl,0rikc,0 yuekc\n" + 
//		"from yuezlb,yuetjkjb,yueslb,vwdianc\n" + 
//		"where yuezlb.yuetjkjb_id=yuetjkjb.id and yueslb.yuetjkjb_id=yuetjkjb.id and yuetjkjb.diancxxb_id=vwdianc.id\n" + 
//		"and to_char(yuetjkjb.riq,'yyyy-mm')='"+YearMon+"' "+diancWhere+" and yuezlb.fenx='本月'and yueslb.fenx='本月'\n" + 
//		"group by yuetjkjb.diancxxb_id\n" + 
//		"union\n" + 
//		"--日入炉热量16.487\n" + 
//		"select rulmzlb.diancxxb_id,\n" + 
//		"0 rihy,0 yuehy,0 rifrl,0 yuefrl,decode(sum(rulmzlb.meil ),0,0,round_new(sum(rulmzlb.qnet_ar*rulmzlb.meil )/sum(rulmzlb.meil ),2)) rirlfrl,0 yuerlfrl,0rikc,0 yuekc\n" + 
//		"from rulmzlb,vwdianc\n" + 
//		"where rulmzlb.diancxxb_id=vwdianc.id and to_char(rulmzlb.rulrq,'yyyy-mm')='"+YearMon+"' "+diancWhere+"\n" + 
//		"group by rulmzlb.diancxxb_id\n" + 
//		"--月入炉热量\n" + 
//		"union\n" + 
//		"select diancxxb_id,0 rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,round_new(RULTRMPJFRL/1000,2)  yuerlfrl,0rikc,0 yuekc\n" + 
//		"from yuezbb,vwdianc\n" + 
//		"where  yuezbb.diancxxb_id=vwdianc.id and to_char(yuezbb.riq,'yyyy-mm')='"+YearMon+"' "+diancWhere+" and yuezbb.fenx='本月'\n" + 
//		"\n" + 
//		"--日库存\n" + 
//		"union\n" + 
//		"select s.diancxxb_id, 0 rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0  yuerlfrl,s.kuc-s.tiaozl rikc,0 yuekc\n" + 
//		"from shouhcrbb s\n" + 
//		"where s.diancxxb_id||s.riq in(\n" + 
//		"select r.diancxxb_id||max(r.riq)\n" + 
//		"from shouhcrbb r,vwdianc\n" + 
//		"where r.diancxxb_id=vwdianc.id "+diancWhere+" and\n" + 
//		"to_char(r.riq,'yyyy-mm')='"+YearMon+"'\n" + 
//		"group by  r.diancxxb_id\n" + 
//		")\n" + 
//		"--月库存\n" + 
//		"union\n" + 
//		"select diancxxb_id,0 rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0  yuerlfrl,0 rikc,y.kuc yuekc\n" + 
//		"from yueshchjb y,vwdianc\n" + 
//		"where y.diancxxb_id=vwdianc.id and  to_char(y.riq,'yyyy-mm')='"+YearMon+"' "+diancWhere+" and fenx='本月'\n" + 
//		")a,vwdianc where vwdianc.id=a.diancxxb_id group by rollup(vwdianc.fgsmc,vwdianc.mingc)order by grouping(fgsmc)desc,vwdianc.fgsmc,grouping(mingc)desc,vwdianc.mingc";


		strSQL="select decode(grouping(mingc),1,decode(grouping("+gongsRanlgs+"),1,'总计',"+gongsRanlgs+"),mingc)mingc1,sum(rihy),sum(yuehy),sum(rihy)-sum(yuehy)chay1,\n" +
		"decode(sum(rircl),0,0,round_new(sum(rifrl*rircl)/sum(rircl),2))rifrl,\n" + 
		"decode(sum(yuercl),0,0,round_new(sum(yuefrl*yuercl)/sum(yuercl),2))yuefrl,\n" + 
		"decode(sum(rircl),0,0,round_new(sum(rifrl*rircl)/sum(rircl),2))-decode(sum(yuercl),0,0,round_new(sum(yuefrl*yuercl)/sum(yuercl),2)) chay2,\n" + 
		"decode(sum(rirll),0,0,round_new(sum(rirll*rirlfrl)/sum(rirll),2))rirlfrl,\n" + 
		"decode(sum(yuerll),0,0,round_new(sum(yuerll*yuerlfrl)/sum(yuerll),2))yuerlfrl,\n" + 
		"decode(sum(rirll),0,0,round_new(sum(rirll*rirlfrl)/sum(rirll),2))-decode(sum(yuerll),0,0,round_new(sum(yuerll*yuerlfrl)/sum(yuerll),2)) chay3,\n" + 
		"sum(rikc)rikc,sum(yuekc)yuekc,sum(rikc)-sum(yuekc) chay4\n" + 
		"\n" + 
		"from(\n" + 
		"select vwdianc."+gongsRanlgs+",vwdianc.mingc,sum(rihy)rihy,sum(yuehy)yuehy--,sum(rihy)-sum(yuehy) chay1\n" + 
		",sum(rifrl)rifrl,sum(yuefrl)yuefrl--,sum(rifrl)-sum(yuefrl) chay2\n" + 
		",sum(rirlfrl)rirlfrl,sum(yuerlfrl)yuerlfrl--,sum(rirlfrl)-sum(yuerlfrl) chay3\n" + 
		",sum(rikc)rikc,sum(yuekc)yuekc--,sum(rikc)-sum(yuekc) chay4\n" + 
		",sum(rircl)rircl,sum(rirll)rirll,sum(yuercl)yuercl,sum(yuerll)yuerll\n" + 
		"from(\n" + 
		"--日耗用\n" + 
		"select rh.diancxxb_id,sum(rh.fady+rh.gongry+rh.qity)rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0 yuerlfrl,0 rikc,0 yuekc,0 rircl,0 rirll,0 yuercl,0 yuerll\n" + 
		"from shouhcrbb rh,vwdianc\n" + 
		"where rh.diancxxb_id=vwdianc.id  "+diancWhere+" and to_char(rh.riq,'yyyy-mm')='"+YearMon+"'\n" + 
		"group by  rh.diancxxb_id\n" + 
		"--月耗用\n" + 
		"union\n" + 
		"select diancxxb_id,0 rihy,(fady+gongry+qith)yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0 yuerlfrl,0rikc,0 yuekc,0 rircl,0 rirll,0 yuercl,0 yuerll\n" + 
		"from yueshchjb yh,vwdianc\n" + 
		"where yh.diancxxb_id=vwdianc.id and to_char(yh.riq,'yyyy-mm')='"+YearMon+"'  "+diancWhere+" and fenx='本月'\n" + 
		"--日入厂热量\n" + 
		"union\n" + 
		"select fahb.diancxxb_id,\n" + 
		"0 rihy,0 yuehy,decode(sum(fahb.laimsl),0,0,round_new(sum(zhilb.qnet_ar*fahb.laimsl)/sum(fahb.laimsl),2)) rifrl\n" + 
		",0 yuefrl,0 rirlfrl,0 yuerlfrl,0rikc,0 yuekc,sum(fahb.laimsl) rircl,0 rirll,0 yuercl,0 yuerll\n" + 
		"from fahb,zhilb,vwdianc\n" + 
		"where fahb.zhilb_id=zhilb.id and fahb.diancxxb_id=vwdianc.id\n" + 
		"and  to_char(fahb.daohrq,'yyyy-mm')='"+YearMon+"'  "+diancWhere+"\n" + 
		"group by fahb.diancxxb_id\n" + 
		"union\n" + 
		"--月入厂热量\n" + 
		"select yuetjkjb.diancxxb_id,\n" + 
		"0 rihy,0 yuehy,0 rifrl,decode(sum(yueslb.laimsl),0,0,round_new(sum(yuezlb.qnet_ar*yueslb.laimsl)/sum(yueslb.laimsl),2)) yuefrl,0 rirlfrl,0 yuerlfrl,0rikc,0 yuekc\n" + 
		",0 rircl,0 rirll,sum(yueslb.laimsl) yuercl,0 yuerll\n" + 
		"from yuezlb,yuetjkjb,yueslb,vwdianc\n" + 
		"where yuezlb.yuetjkjb_id=yuetjkjb.id and yueslb.yuetjkjb_id=yuetjkjb.id and yuetjkjb.diancxxb_id=vwdianc.id\n" + 
		"and to_char(yuetjkjb.riq,'yyyy-mm')='"+YearMon+"'  "+diancWhere+" and yuezlb.fenx='本月'and yueslb.fenx='本月'\n" + 
		"group by yuetjkjb.diancxxb_id\n" + 
		"union\n" + 
		"--日入炉热量16.487\n" + 
		"select rulmzlb.diancxxb_id,\n" + 
		"0 rihy,0 yuehy,0 rifrl,0 yuefrl,decode(sum(rulmzlb.meil ),0,0,round_new(sum(rulmzlb.qnet_ar*rulmzlb.meil )/sum(rulmzlb.meil ),2)) rirlfrl,0 yuerlfrl,0rikc,0 yuekc\n" + 
		",0 rircl,sum(rulmzlb.meil ) rirll,0 yuercl,0 yuerll\n" + 
		"from rulmzlb,vwdianc\n" + 
		"where rulmzlb.diancxxb_id=vwdianc.id and to_char(rulmzlb.rulrq,'yyyy-mm')='"+YearMon+"'  "+diancWhere+"\n" + 
		"group by rulmzlb.diancxxb_id\n" + 
		"--月入炉热量\n" + 
		"union\n" + 
		"select diancxxb_id,0 rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,round_new(RULTRMPJFRL/1000,2)  yuerlfrl,0rikc,0 yuekc\n" + 
		",0 rircl,0 rirll,0 yuercl,FADGRYTRML yuerll\n" + 
		"from yuezbb,vwdianc\n" + 
		"where  yuezbb.diancxxb_id=vwdianc.id and to_char(yuezbb.riq,'yyyy-mm')='"+YearMon+"'  "+diancWhere+" and yuezbb.fenx='本月'\n" + 
		"\n" + 
		"--日库存\n" + 
		"union\n" + 
		"select s.diancxxb_id, 0 rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0  yuerlfrl,s.kuc-s.tiaozl rikc,0 yuekc\n" + 
		",0 rircl,0 rirll,0 yuercl,0 yuerll\n" + 
		"from shouhcrbb s\n" + 
		"where s.diancxxb_id||s.riq in(\n" + 
		"select r.diancxxb_id||max(r.riq)\n" + 
		"from shouhcrbb r,vwdianc\n" + 
		"where r.diancxxb_id=vwdianc.id  "+diancWhere+" and\n" + 
		"to_char(r.riq,'yyyy-mm')='"+YearMon+"'\n" + 
		"group by  r.diancxxb_id\n" + 
		")\n" + 
		"--月库存\n" + 
		"union\n" + 
		"select diancxxb_id,0 rihy,0 yuehy,0 rifrl,0 yuefrl,0 rirlfrl,0  yuerlfrl,0 rikc,y.kuc yuekc\n" + 
		",0 rircl,0 rirll,0 yuercl,0 yuerll\n" + 
		"from yueshchjb y,vwdianc\n" + 
		"where y.diancxxb_id=vwdianc.id and  to_char(y.riq,'yyyy-mm')='"+YearMon+"'  "+diancWhere+" and fenx='本月'\n" + 
		")a,vwdianc where vwdianc.id=a.diancxxb_id group by vwdianc."+gongsRanlgs+",vwdianc.mingc\n" + 
		")\n" + 
		"group by rollup("+gongsRanlgs+",mingc)\n" + 
		"order by grouping("+gongsRanlgs+")desc,"+gongsRanlgs+",grouping(mingc)desc,mingc";

		ArrHeader=new String[2][13];
		ArrHeader[0]=new String[] {"单位","耗用(吨)","耗用(吨)","耗用(吨)","入厂热量(Mj/kg)","入厂热量(Mj/kg)","入厂热量(Mj/kg)","入炉热量(Mj/kg)","入炉热量(Mj/kg)","入炉热量(Mj/kg)","库存（吨)","库存（吨)","库存（吨)"};
		ArrHeader[1]=new String[] {"单位","日数据","月报","差异","日数据","月报","差异","日数据","月报","差异","日数据","月报","差异"};
		 
		ArrWidth=new int[] {150,60,60,60,60,60,60,60,60,60,60,60,60};
//		arrFormat= new String []{"","0","0.00","0.000","0.00","0","0.00","0.000","0.00","0","0.00","0.000","0.00"};
//		 iFixedRows=1;
//		 iCol=10;
			// System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			Table tb = new Table(rs, 2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle( "月数据调整监控", ArrWidth);
			rt.setDefaultTitle(1, 3, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			
			rt.setDefaultTitle(7, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_LEFT);
			rt.setDefaultTitle(13, 5, "月数据调整监控表", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
//			if(rt.body.getRows()>4){
//				rt.body.setCellAlign(5, 1, Table.ALIGN_CENTER);
//			}
		
//			rt.body.setColFormat(arrFormat);
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(8,2,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,3,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
//	得到系统信息表中配置的报表标题的单位名称
//	public String getBiaotmc(){
//		String biaotmc="";
//		JDBCcon cn = new JDBCcon();
//		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
//		ResultSet rs=cn.getResultSet(sql_biaotmc);
//		try {
//			while(rs.next()){
//				 biaotmc=rs.getString("zhi");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}finally{
//			cn.Close();
//		}
//			
//		return biaotmc;
//		
//	}
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
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setWidth(120);
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));
		
		
		
		
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