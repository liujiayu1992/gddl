package com.zhiren.heiljkhh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
 * 作者:ly
 * 时间:2009-06-10
 * 内容:新增需求：1.类别增加全部选项 2.增加所有矿合计查询
 */
/*
 * 作者:tzf
 * 时间:2009-05-28
 * 内容:根据反馈的要求，没有的数据 用0显示，时间段以所选的时间当月初至所选时间为段，进行查询
 */
/*
 * 作者:tzf
 * 时间:2009-5-8
 * 内容:实现龙江公司 燃料调运管理 个性化报表
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Ranldygl extends BasePage implements PageValidateListener {


	private final static String jihkj_zddh_id="1";//计划口径  重点订货id
	private final static String jihkj_sccg_id="2";//市场采购
	private final static String jihkj_qydh_id="3";//区域订货,龙媒计划外
	private final static String meiklb_tp="统配";//煤矿类别  统配
	private final static String meiklb_df="地方";//煤矿类别  地方
	
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
	
	
	
	private StringBuffer getBaseSql(String sql_mc,Date date){
		
		StringBuffer bf = new StringBuffer();
		String diancxxb_id=this.getTreeid_dc();
		String briq_s=this.getBRiq();
		
		String sql = 
			"(select z.id as id,\n" +
			"        (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) mdr,\n" + 
			"       (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) myl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) rdr,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id = z.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) ryl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum((r.meij + r.yunj + r.jiaohqzf + r.daozzf + r.qitfy) *\n" + 
			"                          nvl(f.laimsl, 0)) / sum(nvl(f.laimsl, 0)))\n" + 
			"          from ruccb r, fahb f\n" + 
			"         where r.fahb_id = f.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.meikxxb_id = z.id\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) dcj\n" + 
			"  from (select m.id from meikxxb m where m.id in "+sql_mc+") z\n" + 
			"  )\n" + 
			"\n" + 
			" union\n" + 
			"\n" + 
			"  (select 0 as id,\n" + 
			"       (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) mdr,\n" + 
			"       (select sum(nvl(f.laimsl, 0))\n" + 
			"          from fahb f\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) myl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq = to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) rdr,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum(nvl(zl.qnet_ar, 0) * nvl(f.laimsl, 0)) /\n" + 
			"                      sum(nvl(f.laimsl, 0)))\n" + 
			"          from fahb f, zhilb zl\n" + 
			"         where f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.zhilb_id = zl.id\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) ryl,\n" + 
			"       (select decode(sum(nvl(f.laimsl, 0)),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      sum((r.meij + r.yunj + r.jiaohqzf + r.daozzf + r.qitfy) *\n" + 
			"                          nvl(f.laimsl, 0)) / sum(nvl(f.laimsl, 0)))\n" + 
			"          from ruccb r, fahb f\n" + 
			"         where r.fahb_id = f.id\n" + 
			"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and f.meikxxb_id in "+sql_mc+"\n" + 
			"           and f.daohrq >= to_date('"+briq_s+"', 'yyyy-MM-dd')\n" + 
			"           and f.daohrq <= to_date('"+DateUtil.FormatDate(date)+"', 'yyyy-MM-dd')) dcj\n" + 
			"  from dual)\n" + 
			"  order by id\n";

		
		bf.append(sql);
	
	
//		System.out.println(bf.toString());
		return bf;
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
			if(day!=0){
				long l = d.getTime()+24*60*60*day*1000;
				d.setTime(l);
			}

//			String ds=DateUtil.Formatdate("yyyy-MM-dd", d);
//			String new_s=ds.substring(0, 7)+"-"+day;
//			
//			d=sf.parse(new_s);
			
		}catch(Exception e){
			d=new Date();
			e.printStackTrace();
		}
		
	return d;
	
	
	}
	
	//计算两个日期相差天数
	public static int daysOfTwo(Date fDate, Date oDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(oDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	 }

	
	//获得当月和发货表关联的所有煤矿信息
	private StringBuffer getMeikXX(){
		StringBuffer bf = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		String diancxxb_id=this.getTreeid_dc();
		String riq_s=this.getRiq();
		String briq_s=this.getBRiq();
		
//	---	求出该电厂或者集团所有最下属的电厂
		String s=diancxxb_id;
		
		String leib="";
		if(this.getBianmValue().getId()==0){
			leib = " ";
		}else{
			leib = " and m.leib='"+this.getBianmValue().getValue()+"' \n";
		}
		
//		bf.append(" select distinct m.id ,m.mingc from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
//		bf.append(" and to_char(f.daohrq,'yyyy-MM')=to_char(to_date('"+this.getDateStr(riq_s, "yyyy-MM-dd")+"','yyyy-MM-dd'),'yyyy-MM') \n");
//		bf.append(" and f.diancxxb_id in ( "+s+" ) \n");
//		bf.append(" and m.leib='"+leib+"' \n");//统配煤
//		bf.append(" order by m.id \n");System.out.println(bf.toString());
		
		bf.append(" select distinct m.id ,m.mingc from fahb f,meikxxb m where f.meikxxb_id=m.id \n");
		bf.append(" and f.daohrq>=to_date('"+briq_s+"','yyyy-MM-dd') \n");
		bf.append(" and f.daohrq<=to_date('"+riq_s+"','yyyy-MM-dd') \n");
		bf.append(" and f.diancxxb_id in ( "+s+" ) \n");
		bf.append(leib);//统配煤
		bf.append(" union \n");
		bf.append(" select 0 as id,'合计' as mingc from dual \n");
		bf.append(" order by id \n");
//		System.out.println(bf.toString());
		
		return bf;
	}
	// 燃料采购部指标完成情况日报
	private String getHuaybgd() {
		Report rt = new Report();
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String briq_s=this.getBRiq();
		String riq_s=this.getRiq();
		StringBuffer bf=new StringBuffer();
		bf=this.getMeikXX();
		
		
		
		ResultSetList rs = con.getResultSetList(bf.toString());
		
		int rows=rs.getRows();
		
		if(rows==1){
			
			if(!visit.getboolean5()){
				visit.setboolean5(true);
			}else{
//				this.setMsg("该电厂本月没有数据！");
			}
			this.setAllPages(-1);
			this.setCurrentPage(-1);
			return "";
		}
		
//		this.setMsg(null);
		
		
		Date dt=null;
		Date bdt=null;
		Date xitsj=null;
		try{
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			dt=sf.parse(riq_s);
			bdt=sf.parse(briq_s);
			xitsj=new Date();
		}catch(Exception e){
			dt=new Date();
			bdt=new Date();
			xitsj=new Date();
		}
		
//		//系统当前时间
//		
//		int xit_year=Integer.valueOf(DateUtil.Formatdate("yyyy", new Date())).intValue();
//		int xit_month=Integer.valueOf(DateUtil.Formatdate("MM", new Date())).intValue();
//		int xit_day=Integer.valueOf(DateUtil.Formatdate("dd", new Date())).intValue();
//		
//		//被选择日期
//		int bsele_year=Integer.valueOf(DateUtil.Formatdate("yyyy", bdt)).intValue();
//		int bsele_month=Integer.valueOf(DateUtil.Formatdate("MM", bdt)).intValue();
//		int bsele_day=Integer.valueOf(DateUtil.Formatdate("dd", bdt)).intValue();
//		
//		int sele_year=Integer.valueOf(DateUtil.Formatdate("yyyy", dt)).intValue();
//		int sele_month=Integer.valueOf(DateUtil.Formatdate("MM", dt)).intValue();
//		int sele_dat=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
		
		int count=-1;
		if(daysOfTwo(bdt,xitsj)>=0){
			if(daysOfTwo(dt,xitsj)>=0){
				count = daysOfTwo(bdt,dt)+1;
			}else{
				count = daysOfTwo(bdt,xitsj)+1;
			}
		}else{
			count = -1;
		}

//		if(bsele_year==xit_year){
//			if(bsele_month==xit_month){
//				if()
//				count = sele_dat-bsele_day+1;
//			}else if(bsele_month<xit_month){
//				
//			}else{
//				count=-1;
//			}
//			
////			if(sele_month<xit_month){
//////				count=this.getDays(riq_s);
////				count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
////			}else if(sele_month==xit_month){
////				
////				if(sele_year==xit_year){
////					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
////				}else{
//////					count=this.getDays(riq_s);
////					count=Integer.valueOf(DateUtil.Formatdate("dd", dt)).intValue();
////				}
////				
////			}else{
////				count=-1;
////			}
//		}else if(bsele_year<xit_year){
//		
//		}else{
//			count=-1;
//		}
		
		if(count<0){
			this.setAllPages(-1);
			this.setCurrentPage(-1);
			return "";
		}
		
		
		
		
		int mc_count=5;//一个煤矿信息 有5个字段
//		String ArrHeader[][]=new String[3+this.getDays(riq_s)][1+rows*mc_count];
		String ArrHeader[][]=new String[3+count][1+rows*mc_count];
		
		ArrHeader[0][0] = "日期";
		ArrHeader[1][0] = "日期";
		ArrHeader[2][0] = "日期";
		String sql_mc="(";
		int col_index=1;//列 下标 开始位置
		
		int i=1;
		while(rs.next()){
			sql_mc+=rs.getString("id")+",";
			
			if(i!=1){
				col_index+=mc_count;
			}
			for(int j=0;j<=2;j++){  //3行
				
				if(j==0){//第一行  表头
					ArrHeader[j][col_index]=rs.getString("MINGC");
					ArrHeader[j][col_index+1]=rs.getString("MINGC");
					ArrHeader[j][col_index+2]=rs.getString("MINGC");
					ArrHeader[j][col_index+3]=rs.getString("MINGC");
					ArrHeader[j][col_index+4]=rs.getString("MINGC");
				}
				if(j==1){
					ArrHeader[j][col_index]="来煤";
					ArrHeader[j][col_index+1]="来煤";
					ArrHeader[j][col_index+2]="热值";
					ArrHeader[j][col_index+3]="热值";
					ArrHeader[j][col_index+4]="到厂价";
				}
				if(j==2){
					ArrHeader[j][col_index]="当日";
					ArrHeader[j][col_index+1]="月累";
					ArrHeader[j][col_index+2]="当日";
					ArrHeader[j][col_index+3]="月累";
					ArrHeader[j][col_index+4]="到厂价";
				}
				
			}
			i++;
		}
		
		sql_mc=sql_mc.substring(0, sql_mc.lastIndexOf(","))+")";
		
	//  ArrHeader 赋值
		rs.close();
		
		int data_index_start=3;//从第4行开始赋值
		col_index=1;

		
		for(int m=0;m<count;m++){//每一次循环 取得当天的发货
			
//			Date ora_date=this.getDateOfDay_Oracle(briq_s, m);
			
			Date ora_date=DateUtil.AddDate(DateUtil.FormatDateTime(DateUtil.getDate(briq_s)), m, DateUtil.AddType_intDay);
			
			StringBuffer sql=this.getBaseSql(sql_mc,ora_date);
			
			rs=con.getResultSetList(sql.toString());
			
			ArrHeader[data_index_start][0]=DateUtil.FormatDate(ora_date);
			
			i=1;
			col_index=1;
			while(rs.next()){
				
				if(i!=1){
					col_index+=mc_count;
				}
				
				ArrHeader[data_index_start][col_index]=rs.getString("MDR");
				ArrHeader[data_index_start][col_index+1]=rs.getString("MYL");
				ArrHeader[data_index_start][col_index+2]=rs.getString("RDR");
				ArrHeader[data_index_start][col_index+3]=rs.getString("RYL");
				ArrHeader[data_index_start][col_index+4]=rs.getString("DCJ");
				
				i++;
			}
			
			data_index_start++;
		}
		
		
		int[] ArrWidth = new int[1+rows*mc_count];
		ArrWidth[0]=75;
		for(int m=1;m<ArrWidth.length;m++){  //显示宽度 设置
			ArrWidth[m]=55;
		}
		
//		Table bt=new Table(3+this.getDays(riq_s),1+rows*mc_count);
		Table bt=new Table(3+count,1+rows*mc_count);
		rt.setBody(bt);
		
		

		String[][] ArrHeader1 = new String[3][1+rows*mc_count];
		ArrHeader1[0] = ArrHeader[0];
		ArrHeader1[1] = ArrHeader[1];
		ArrHeader1[2] = ArrHeader[2];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.body.setWidth(ArrWidth);
		
		rt.setTitle(this.getDateStr(this.getRiq(), "yyyy年MM月")+visit.getDiancmc()+this.getBianmValue().getValue()+"煤量质价统计台帐", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 14);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.setDefaultTitle(rows*mc_count-3, 2, "单位:吨 MJ/KJ",
				Table.ALIGN_RIGHT);
		
		
//		for ( i = 3; i < 3+this.getDays(riq_s); i++) {
		for ( i = 3; i < 3+count; i++) {
			for (int j = 0; j < 1+rows*mc_count; j++) {
				
				if ( ArrHeader[i][j] != null && !ArrHeader[i][j].equals("") ) {
					
					ArrHeader[i][j] = rt.body.format(ArrHeader[i][j], "0.00");
				}else{
					ArrHeader[i][j] ="0";
				}
				
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
				rt.body.setCellAlign(i+1, j+1, Table.ALIGN_RIGHT);

				
			}
		}
		
//		for (i = 1; i <= 1+rows*mc_count; i++) {
//			rt.body.setColAlign(i, Table.ALIGN_CENTER);
//		}
//		
		rt.body.merge(0, 0, 3, 1+rows*mc_count);
		
		//rt.body.mergeCol(0);
//		rt.body.mergeCol(1);
		
//		rt.body.merge(2, 0, rs.getRows()+1, 3);
//		rt.body.merge(3, 7, rs.getRows()+2, 17);
		
		
		
		rt.body.ShowZero = true;

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
		String sql = " select d.id,d.mingc from diancxxb d ";
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
		
		
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("BRIQ");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("类别:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
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
			visit.setboolean5(false);
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
			getSelectData();
		}
	
		
	}

	boolean riqchange = false;
	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}
	
	boolean briqchange = false;
	private String briq;

	public String getBRiq() {
		if (briq == null || briq.equals("")) {
			briq =DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return briq;
	}

	public void setBRiq(String briq) {

		if (this.briq != null && !this.briq.equals(briq)) {
			this.briq = briq;
			briqchange = true;
		}

	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {

		List list=new ArrayList();
		list.add(new IDropDownBean("0","全部"));
		list.add(new IDropDownBean("1",meiklb_tp));
		list.add(new IDropDownBean("2",meiklb_df));
			
		setBianmModel(new IDropDownModel(list));
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
