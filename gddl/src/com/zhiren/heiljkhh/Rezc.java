package com.zhiren.heiljkhh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-05-09
 * 内容:实现 龙江公司 省公司 热值差日报
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

public class Rezc extends BasePage implements PageValidateListener {


	private final static String jihkj_zddh_id="1";//计划口径  重点订货id
	private final static String jihkj_sccg_id="2";//市场采购
	private final static String jihkj_qydh_id="3";//区域订货,龙媒计划外
	private final static String meiklb_tp="统配";//煤矿类别  统配
	private final static String meiklb_df="地方";//煤矿类别  地方
	private final static String laimsl_round_count="0";//对发货表取出的laimsl字段 进行修约的小数个数,默认为2,保留2位小数
	private final static String meil_table_show="0";  //煤量字段  页面显示的尾数
//	private final static String qit_table_show="0.00";//除煤量之外字段  页面显示的尾数
	private final static String qit_table_show="0.000";//除煤量之外字段  页面显示的尾数
	
	public boolean getRaw() {
		return true;
	}

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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

	public String getPrintTable() {
		return getHuaybgd();
	}
	

	//获得当月的天数
	private int getDays(String date){
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		Date d=null;
		try{
			d=sf.parse(date);
		}catch(Exception e){
			d=new Date();
			e.printStackTrace();
		}
		
		int firstD=Integer.valueOf(DateUtil.Formatdate("dd", DateUtil.getFirstDayOfMonth(d))).intValue();
		int lastD=Integer.valueOf(DateUtil.Formatdate("dd", DateUtil.getLastDayOfMonth(d))).intValue();
		
		return lastD-firstD+1;
	}
	
	 //获得指定day的日期
	private Date getDateOfDay_Oracle(String date,int day){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		Date d=null;
		try{
			d=sf.parse(date);
			
			String ds=DateUtil.Formatdate("yyyy-MM-dd", d);
			String new_s=ds.substring(0, 7)+"-"+day;
			
			d=sf.parse(new_s);
			
		}catch(Exception e){
			d=new Date();
			e.printStackTrace();
		}
		
	return d;
	
	
	}
	private StringBuffer getViewSql(){
		StringBuffer bf = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		String diancxxb_id=this.getTreeid_dc();
		String riq_s=this.getRiq();
		
		String firstDay_M=this.getFirstDayOracle(riq_s);
		String firstDay_Y=this.getFirstDay_Year_Oracle(riq_s);
		
		Date dt=null;
		
		try{
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			dt=sf.parse(riq_s);
		}catch(Exception e){
			dt=new Date();
		}
		
		//系统当前时间
		
		int xit_year=Integer.valueOf(DateUtil.Formatdate("yyyy", new Date())).intValue();
		int xit_month=Integer.valueOf(DateUtil.Formatdate("MM", new Date())).intValue();
		
		
		//被选择日期
		int sele_year=Integer.valueOf(DateUtil.Formatdate("yyyy", dt)).intValue();
		int sele_month=Integer.valueOf(DateUtil.Formatdate("MM", dt)).intValue();
		
		int count=-1;
		if(sele_year<=xit_year){
			
			if(sele_month<xit_month){
//				count=this.getDays(riq_s);
				count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
			}else if(sele_month==xit_month){
				
				if(sele_year==xit_year){
					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
				}else{
//					count=this.getDays(riq_s);
					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
				}
				
			}else{
				count=-1;
			}
		}else{
			count=-1;
		}
		
		StringBuffer date_sql=new StringBuffer();
		if(count<0){//无效时间，返回
			this.setEffect(false);
			return null;
		}else{
			this.setEffect(true);
			for(int i=1;i<=count;i++){
				
				Date tep_d=this.getDateOfDay_Oracle(riq_s, i);
				date_sql.append(" select to_date('"+DateUtil.FormatDate(tep_d)+"','yyyy-MM-dd') riq from dual \n");
				
				if(i!=count){
					date_sql.append(" union \n");
				}
			}
			
		}
		//求出该电厂或者集团所有最下属的电厂
		String s=" select db.id from diancxxb db \n"
				+" where db.id in \n"
				+" (select d.id from  diancxxb d start with d.id="+diancxxb_id+"\n"
				+" connect by prior d.id=d.fuid ) \n"
				+" minus \n"
				+" select id from diancxxb where id  in \n"
				+" ( select d.fuid from  diancxxb d start with d.id="+diancxxb_id+" \n"
				+" connect by prior d.id=d.fuid )";
		
		String lai_hj=" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id and m.leib='"+meiklb_tp+"' and f.jihkjb_id="+jihkj_zddh_id+" \n"
						+" and f.daohrq=hk.riq and f.diancxxb_id=hk.id  ),0)+\n"
						+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
						+" and f.daohrq=hk.riq and f.diancxxb_id=hk.id  and f.jihkjb_id="+jihkj_qydh_id+" ),0) +\n"
						+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
						+" and f.daohrq=hk.riq and f.diancxxb_id=hk.id  and \n"
						+" f.jihkjb_id="+jihkj_sccg_id+" ),0) \n";
				
		
		String lai_lj=" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id and m.leib='"+meiklb_tp+"' and f.jihkjb_id="+jihkj_zddh_id+" \n"
		+" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id=hk.id  ),0)+\n"
		+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
		+" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id=hk.id  and f.jihkjb_id="+jihkj_qydh_id+" ),0) +\n"
		+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
		+" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id=hk.id  and \n"
		+" f.jihkjb_id="+jihkj_sccg_id+" ),0) \n";
		
		
		
		
		bf.append(" select  to_number(to_char(hk.riq,'dd')) rq,hk.mingc dc, \n");
		bf.append(" (select sum(r.fadl)/10000 from riscsjb r where r.diancxxb_id=hk.id and r.riq=hk.riq ) fdl,\n");
		
		bf.append(" (select  sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id and m.leib='"+meiklb_tp+"' and  f.jihkjb_id="+jihkj_zddh_id+" \n");
		bf.append(" and f.daohrq=hk.riq and f.diancxxb_id=hk.id ) tp, \n");
		
		bf.append(" (select  sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
		bf.append(" and f.daohrq=hk.riq and f.diancxxb_id=hk.id  and f.jihkjb_id="+jihkj_qydh_id+" )jhw,\n");
		
		bf.append(" (select  sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
		bf.append(" and f.daohrq=hk.riq and f.diancxxb_id=hk.id  and f.jihkjb_id="+jihkj_sccg_id+" ) zg,\n");
		
		bf.append(" ("+lai_hj+") hj,\n");
		
		bf.append(" ("+lai_lj+" ) lj,\n");
		
		bf.append(" (select sb.haoyqkdr from shouhcrbb sb where sb.riq=hk.riq and sb.diancxxb_id=hk.id ) hmd,\n");
		bf.append(" (select sum(sb.haoyqkdr) from shouhcrbb sb where sb.riq>="+firstDay_M+" and sb.riq<=hk.riq \n");
		bf.append(" and sb.diancxxb_id=hk.id )hml,\n");
		
		
		bf.append(" (select sb.kuc from shouhcrbb sb where sb.riq=hk.riq and sb.diancxxb_id=hk.id )mkc,\n");
		
		bf.append(" (select sy.fady+sy.gongry+sy.qity+sy.cuns from shouhcrbyb sy where sy.diancxxb_id=hk.id and sy.riq=hk.riq )hyd,\n");
//		bf.append(" '耗油说明' hysm,\n");
		bf.append(" (select sy.beiz from shouhcrbyb sy where sy.diancxxb_id=hk.id and sy.riq=hk.riq) hysm,\n");
		
		bf.append(" (select sum(sy.fady+sy.gongry+sy.qity+sy.cuns) from shouhcrbyb sy where sy.diancxxb_id=hk.id \n");
		bf.append(" and sy.riq>="+firstDay_M+" and sy.riq<=hk.riq )hyl,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq=hk.riq and f.diancxxb_id=hk.id  )cd,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id \n");
		bf.append(" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id=hk.id  \n");
		bf.append(" )cyl,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id \n");
		bf.append(" and f.daohrq>="+firstDay_Y+" and f.daohrq<=hk.riq and f.diancxxb_id=hk.id )cnl,\n");
		
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id=hk.id and ru.rulrq=hk.riq )ld,\n");
		
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id=hk.id  and ru.rulrq>="+firstDay_M+" and ru.rulrq<=hk.riq )lyl,\n");
		
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id=hk.id and ru.rulrq>="+firstDay_Y+" and ru.rulrq<=hk.riq )lnl,\n");
		
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq=hk.riq and f.diancxxb_id=hk.id)-\n");
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id=hk.id and ru.rulrq=hk.riq ) rcd,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id=hk.id )-\n");
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id=hk.id  and ru.rulrq>="+firstDay_M+" and ru.rulrq<=hk.riq  ) rcy,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq>="+firstDay_Y+" and f.daohrq<=hk.riq and f.diancxxb_id=hk.id  )-\n");
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id=hk.id  and ru.rulrq>="+firstDay_Y+" and ru.rulrq<=hk.riq ) rcn\n");
		
//		bf.append(" from (select distinct sr.riq,d.id,d.mingc from shouhcrbb sr,diancxxb d \n");
//		bf.append(" where \n");
//		bf.append("  to_char(sr.riq,'yyyy-MM')=to_char(to_date('"+riq_s+"','yyyy-MM-dd'),'yyyy-MM')  \n");
//		bf.append("  and d.id in("+s+")\n");
//		bf.append("  ) hk\n");
		
		bf.append(" from (");
		
		bf.append(" select distinct sr.riq,d.id,d.mingc from ( "+date_sql+" ) sr, diancxxb d where d.id in( "+s+" ) ");
		
		
		bf.append(" ) hk");
		
//		System.out.println(bf.toString());
		return bf;
	}
	
	
	private StringBuffer getHJSQL(){

		JDBCcon con=new JDBCcon(); 
		StringBuffer bf = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		String diancxxb_id=this.getTreeid_dc();
		String riq_s=this.getRiq();
		
		String firstDay_M=this.getFirstDayOracle(riq_s);
		String firstDay_Y=this.getFirstDay_Year_Oracle(riq_s);
		
		Date dt=null;
		
		try{
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			dt=sf.parse(riq_s);
		}catch(Exception e){
			dt=new Date();
		}
		
		//系统当前时间
		
		int xit_year=Integer.valueOf(DateUtil.Formatdate("yyyy", new Date())).intValue();
		int xit_month=Integer.valueOf(DateUtil.Formatdate("MM", new Date())).intValue();
		
		
		//被选择日期
		int sele_year=Integer.valueOf(DateUtil.Formatdate("yyyy", dt)).intValue();
		int sele_month=Integer.valueOf(DateUtil.Formatdate("MM", dt)).intValue();
		
		int count=-1;
		if(sele_year<=xit_year){
			
			if(sele_month<xit_month){
//				count=this.getDays(riq_s);
				count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
			}else if(sele_month==xit_month){
				
				if(sele_year==xit_year){
					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
				}else{
//					count=this.getDays(riq_s);
					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
				}
				
			}else{
				count=-1;
			}
		}else{
			count=-1;
		}
		
		StringBuffer date_sql=new StringBuffer();
		if(count<0){//无效时间，返回
			this.setEffect(false);
			return null;
		}else{
			this.setEffect(true);
			for(int i=1;i<=count;i++){
				
				Date tep_d=this.getDateOfDay_Oracle(riq_s, i);
				date_sql.append(" select to_date('"+DateUtil.FormatDate(tep_d)+"','yyyy-MM-dd') riq from dual \n");
				
				if(i!=count){
					date_sql.append(" union \n");
				}
			}
			
		}
		//求出该电厂或者集团所有最下属的电厂
		String s=" select db.id from diancxxb db \n"
				+" where db.id in \n"
				+" (select d.id from  diancxxb d start with d.id="+diancxxb_id+"\n"
				+" connect by prior d.id=d.fuid ) \n"
				+" minus \n"
				+" select id from diancxxb where id  in \n"
				+" ( select d.fuid from  diancxxb d start with d.id="+diancxxb_id+" \n"
				+" connect by prior d.id=d.fuid )";
		
		
		ResultSetList rsl=con.getResultSetList(s);
		String dcs="(";
		while(rsl.next()){
			dcs+=rsl.getString("id")+",";
		}
		
		dcs=dcs.substring(0,dcs.lastIndexOf(","))+")";
		
		String lai_hj=" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id and m.leib='"+meiklb_tp+"' and f.jihkjb_id="+jihkj_zddh_id+" \n"
						+" and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+"  ),0)+\n"
						+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
						+" and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+"  and f.jihkjb_id="+jihkj_qydh_id+" ),0) +\n"
						+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
						+" and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+"  and \n"
						+" f.jihkjb_id="+jihkj_sccg_id+" ),0) \n";
				
		
		String lai_lj=" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id and m.leib='"+meiklb_tp+"' and f.jihkjb_id="+jihkj_zddh_id+" \n"
		+" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id in "+dcs+" ),0)+\n"
		+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
		+" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id in "+dcs+"  and f.jihkjb_id="+jihkj_qydh_id+" ),0) +\n"
		+" nvl((select  sum(nvl(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id  \n"
		+" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id in "+dcs+"  and \n"
		+" f.jihkjb_id="+jihkj_sccg_id+" ),0) \n";
		
		
		
		
		bf.append(" select  to_number(to_char(hk.riq,'dd')) rq,' 合计' dc, \n");
		bf.append(" (select sum(r.fadl)/10000 from riscsjb r where r.diancxxb_id in "+dcs+" and r.riq=hk.riq ) fdl,\n");
		
		bf.append(" (select  sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id and m.leib='"+meiklb_tp+"' and  f.jihkjb_id="+jihkj_zddh_id+" \n");
		bf.append(" and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+" ) tp, \n");
		
		bf.append(" (select  sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
		bf.append(" and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+"  and f.jihkjb_id="+jihkj_qydh_id+" )jhw,\n");
		
		bf.append(" (select  sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)) from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
		bf.append(" and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+"  and f.jihkjb_id="+jihkj_sccg_id+" ) zg,\n");
		
		bf.append(" ("+lai_hj+") hj,\n");
		
		bf.append(" ("+lai_lj+" ) lj,\n");
		
		bf.append(" (select sum(nvl(sb.haoyqkdr,0)) from shouhcrbb sb where sb.riq=hk.riq and sb.diancxxb_id in "+dcs+" ) hmd,\n");
		bf.append(" (select sum(nvl(sb.haoyqkdr,0)) from shouhcrbb sb where sb.riq>="+firstDay_M+" and sb.riq<=hk.riq \n");
		bf.append(" and sb.diancxxb_id in "+dcs+" )hml,\n");
		
		
		bf.append(" (select sum(sb.kuc) from shouhcrbb sb where sb.riq=hk.riq and sb.diancxxb_id in "+dcs+" )mkc,\n");
		
		bf.append(" (select sum(sy.fady+sy.gongry+sy.qity+sy.cuns) from shouhcrbyb sy where sy.diancxxb_id in "+dcs+" and sy.riq=hk.riq )hyd,\n");
//		bf.append(" '耗油说明' hysm,\n");
		bf.append(" ''  hysm,\n");
		
		bf.append(" (select sum(sy.fady+sy.gongry+sy.qity+sy.cuns) from shouhcrbyb sy where sy.diancxxb_id in "+dcs+" \n");
		bf.append(" and sy.riq>="+firstDay_M+" and sy.riq<=hk.riq )hyl,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+"  )cd,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id \n");
		bf.append(" and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id in "+dcs+" \n");
		bf.append(" )cyl,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id \n");
		bf.append(" and f.daohrq>="+firstDay_Y+" and f.daohrq<=hk.riq and f.diancxxb_id in "+dcs+" )cnl,\n");
		
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id in "+dcs+" and ru.rulrq=hk.riq )ld,\n");
		
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id in "+dcs+"  and ru.rulrq>="+firstDay_M+" and ru.rulrq<=hk.riq )lyl,\n");
		
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id in "+dcs+" and ru.rulrq>="+firstDay_Y+" and ru.rulrq<=hk.riq )lnl,\n");
		
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq=hk.riq and f.diancxxb_id in "+dcs+")-\n");
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id in "+dcs+" and ru.rulrq=hk.riq ) rcd,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq>="+firstDay_M+" and f.daohrq<=hk.riq and f.diancxxb_id in "+dcs+" )-\n");
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id in "+dcs+"  and ru.rulrq>="+firstDay_M+" and ru.rulrq<=hk.riq  ) rcy,\n");
		
		bf.append(" (select decode(sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0)),0,0,sum(nvl(z.qnet_ar,0)*nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))/sum(nvl(round_new(nvl(f.laimsl,0),"+laimsl_round_count+"),0))) from fahb f,zhilb z where f.zhilb_id=z.id and f.daohrq>="+firstDay_Y+" and f.daohrq<=hk.riq and f.diancxxb_id in "+dcs+"  )-\n");
		bf.append(" (select decode(sum(nvl(ru.meil,0)),0,0,sum(nvl(ru.qnet_ar,0)*nvl(ru.meil,0))/sum(nvl(ru.meil,0))) from rulmzlb ru where ru.diancxxb_id in "+dcs+"  and ru.rulrq>="+firstDay_Y+" and ru.rulrq<=hk.riq ) rcn\n");
		
//		bf.append(" from (select distinct sr.riq,d.id,d.mingc from shouhcrbb sr,diancxxb d \n");
//		bf.append(" where \n");
//		bf.append("  to_char(sr.riq,'yyyy-MM')=to_char(to_date('"+riq_s+"','yyyy-MM-dd'),'yyyy-MM')  \n");
//		bf.append("  and d.id in("+s+")\n");
//		bf.append("  ) hk\n");
		
		bf.append(" from (");
		
		bf.append(date_sql);
		
		
		bf.append(" ) hk group by hk.riq");
		
//		System.out.println(bf.toString());
		return bf;
	
	}
	private StringBuffer getBaseSql(){

		
		StringBuffer bf = new StringBuffer();
		
		bf.append(" select * from ( \n");
		
		if(this.hasDianc(this.getTreeid_dc())){
	/*		bf.append(" select rq, ' 合计' dc, sum(fdl) fdl, sum(tp) tp, sum(jhw) jhw,\n");
			bf.append(" sum(zg) zg, sum(hj) hj, sum(lj) lj, sum(hmd) hmd, sum(hml) hml,\n");
//			bf.append(" sum(mkc) mkc, sum(hyd) hyd, '说明' hysm, sum(hyl) hyl, 1000*sum(cd) cd,\n");
			bf.append(" sum(mkc) mkc, sum(hyd) hyd, '' hysm, sum(hyl) hyl, sum(cd) cd,\n");
			bf.append(" sum(cyl) cyl,sum(cnl) cnl,sum(ld) ld, sum(lyl) lyl, sum(lnl) lnl,\n");
			bf.append("  sum(rcd) rcd,sum(rcy) rcy,sum(rcn) rcn \n");
			bf.append(" from ("+this.getViewSql()+") group by  rq \n");
			bf.append(" union  \n");*/
			
			bf.append(" select *  from ("+this.getHJSQL()+") union \n");
		}
		
		bf.append(" select * from ("+this.getViewSql()+") \n");
//		bf.append(" ) temp order by rq,dc \n");
		bf.append(" ) temp order by rq,(select xuh from diancxxb where mingc=dc) desc \n");

//		System.out.println(bf.toString());
		return bf;
	}
	
	
	private String getFirstDayOracle(String date){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		
		try{
			Date d=df.parse(date);
			return DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(d));
		}catch(Exception e){
			e.printStackTrace();
		}
		return DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(new Date()));
	}
	
	private String getFirstDay_Year_Oracle(String date){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		
		try{
			Date d=df.parse(date);
			return DateUtil.FormatOracleDate(DateUtil.getFirstDayOfYear(d));
		}catch(Exception e){
			e.printStackTrace();
		}
		return DateUtil.FormatOracleDate(DateUtil.getFirstDayOfYear(new Date()));
	}
	
	private String getDateStr(String date,String format){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(date==null){
			date=DateUtil.FormatDate(new Date());
		}
		try {
			return DateUtil.Formatdate(format, sf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	//获得月初的时间 如2009-1-6---------》  2008-12-31
	private Date getLastdayOfLastMonth(String date){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(date==null){
			date=DateUtil.FormatDate(new Date());
		}
		try {
			Date temD= sf.parse(date);
			int month = DateUtil.getMonth(temD);
			int year =DateUtil.getYear(temD);
			
			if(month==1){
				year-=1;
				month=12;
			}else{
				month-=1;
			}
			
			Date newD = DateUtil.getLastDayOfMonth(year+"-"+month+"-1");
			return newD;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
	
	// 燃料采购部指标完成情况日报
	private String getHuaybgd() {
		Report rt = new Report();
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		StringBuffer bf=null;
		bf=this.getBaseSql();	
		
		if(!this.getEffect()){
			
//			if(_RefurbishChick){
				this.setAllPages(-1);
				this.setCurrentPage(-1);
//				this.setMsg("该时间没有数据!");
//			}
			return "";
		}
		
	
		ResultSetList rs = con.getResultSetList(bf.toString());
		
		String ArrHeader[][]=new String[3][23];
		ArrHeader[0]=new String[] {"日期","电厂","发电量(万千瓦时)","来煤量(吨)","来煤量(吨)","来煤量(吨)","来煤量(吨)","","耗煤量(吨)","耗煤量(吨)","煤库存(吨)","耗油量","耗油量","耗油量","入厂热值","入厂热值","入厂热值","入炉热值","入炉热值","入炉热值","热值差","热值差","热值差"
									};
		ArrHeader[1]=new String[] {"日期","电厂","发电量(万千瓦时)","当日","当日","当日","当日","累计","耗煤量(吨)","耗煤量(吨)","煤库存(吨)","当日","当日","累计","(兆焦/千克)","(兆焦/千克)","(兆焦/千克)","(兆焦/千克)","(兆焦/千克)","(兆焦/千克)","(兆焦/千克)","(兆焦/千克)","(兆焦/千克)"};
		
		ArrHeader[2]=new String[] {"日期","电厂","发电量(万千瓦时)","统配","计划外","自购","合计","累计","当日","累计","当日","(吨)","说明","累计","当日","累计","年累","当日","累计","年累" ,"当日","累计","年累"};
	
		int[] ArrWidth = new int[] { 80, 80, 80, 80, 80, 80 ,80 ,80 ,80 ,80 ,80, 80 ,80 ,80 ,80 ,80 ,80 , 80 ,80 ,80 ,80 ,80 ,80};
		
		rt.setBody(new Table(rs, 3, 0, 0));
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.body.setHeaderData(ArrHeader);
		
		rt.body.setWidth(ArrWidth);
		
		rt.setTitle(visit.getDiancmc()+this.getDateStr(this.getRiq(), "yyyy年MM月")+"发电生产燃料日报表", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 14);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		
		for (int i = 1; i <= 23; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		
		for(int i=3;i<rs.getRows()+4;i++){
			for(int j=2;j<24;j++){
				
				String va_str="";
				if((rt.body.getCellValue(i, j)==null || rt.body.getCellValue(i, j).equals("")) && j!=13){
					va_str="0";
				}else{
					va_str=rt.body.getCellValue(i, j);
				}
				if(j>=4 && j<=11){
//					rt.body.setCellValue(i, j, rt.body.format(rt.body
//							.getCellValue(i, j), meil_table_show));
					rt.body.setCellValue(i, j, rt.body.format(va_str, meil_table_show));
				}else{
//					rt.body.setCellValue(i, j, rt.body.format(rt.body
//							.getCellValue(i, j), qit_table_show));
					rt.body.setCellValue(i, j, rt.body.format(va_str, qit_table_show));
				}
				
			}
		}
		rt.body.merge(0, 0, 3, 14);
		rt.body.merge(0, 15, 3, 17);
		rt.body.merge(0, 18, 3, 20);
		rt.body.merge(0, 21, 3, 23);
		//rt.body.mergeCol(0);
//		rt.body.mergeCol(1);
		rt.body.merge(3, 0, rs.getRows()+3, 1);
		
//		rt.body.merge(2, 0, rs.getRows()+1, 3);
//		rt.body.merge(3, 7, rs.getRows()+2, 17);
		
		
		
//		rt.body.ShowZero = false;
		rt.body.ShowZero = true;
		
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		//rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	
	
//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
//		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
//		sql+=" union \n";
//		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		
		String sql = " select d.id,d.mingc from diancxxb d \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END----------
	
	
//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
	
    
    
	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		
		tb1.addText(new ToolbarText("单位名称:"));
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("-"));
		
	
		
	
		
		
		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}


	
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
	//	((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
		//	setRiq(DateUtil.FormatDate(new Date()));
			visit.setString3(null);
			riq=null;
			visit.setProSelectionModel2(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
			getSelectData();
			visit.setboolean4(true);
		}
	
		
	}

	boolean riqchange = false;
	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}
	
	private void setEffect(boolean t){
		Visit visit=(Visit)this.getPage().getVisit();
		
		visit.setboolean4(t);
	}
	
	private boolean getEffect(){
		Visit visit=(Visit)this.getPage().getVisit();
		
		return visit.getboolean4();
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}




	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
	
	

}



