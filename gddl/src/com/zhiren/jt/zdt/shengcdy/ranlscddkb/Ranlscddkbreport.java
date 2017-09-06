package com.zhiren.jt.zdt.shengcdy.ranlscddkb;

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
import com.zhiren.common.ResultSetList;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import org.apache.tapestry.contrib.palette.SortMode;

/**
 * 作者:sy
 * 时间:2009-9-11
 * 修改内容:修改不能查出蒙东和金元数据的sql问题
 *
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-28 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Ranlscddkbreport  extends BasePage implements PageValidateListener{
	
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
	
//	 开始日期
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
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
			setBaoblxValue(null);
			getIBaoblxModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
			isBegin=true;
			this.getSelectData();
		}

		
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="Ranlsckb";
	private String mstrReportName="Ranlsckb";//燃料生产快报
	
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

	private String FormatDate1(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	private boolean isBegin=false;
	private boolean checkXitszKyts() {
		// TODO 自动生成方法存根
		// 检查系统设置中的"可单独结算运费"设置
		JDBCcon con = new JDBCcon();
		try {
			String zhi = "";

			String sql = "select zhi from xitxxb where mingc='库存煤可用天数计算方法' and zhuangt=1";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				zhi = rs.getString("zhi");
			}

			if (zhi.trim().equals("大唐国际")) {

				return true;
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return false;
	}
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		
		String riq=DateUtil.FormatDate(this.getBeginriqDate());
		String riq1=FormatDate1(this.getBeginriqDate());
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		
		int tians=DateUtil.getDay(this.getBeginriqDate());
		
		int jib=this.getDiancTreeJib();
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		//报表内容
		//说明:燃料生产调度快报的计划和日均计划的值只判断了hetslb的状态,没有判断hetb的流程状态,如果以后需要可以加上.
		
		StringBuffer grouping_sql = new StringBuffer();
		StringBuffer where_sql = new StringBuffer();
		StringBuffer rollup_sql = new StringBuffer();
		StringBuffer having_sql = new StringBuffer();
		StringBuffer orderby_sql = new StringBuffer();
		
		StringBuffer strSQL = new StringBuffer();
		
		
		if (jib==1) {//选集团时刷新出所有的电厂
			grouping_sql.append(" select decode(grouping(dc.mingc)+grouping(dc.fengs),2,'总计',1,dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n");
			
			where_sql.append(" ");
			rollup_sql.append(" group by rollup (dc.fengs,dc.mingc) ");
			having_sql.append("");
			orderby_sql.append("  order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc ,max(dc.xuh1) ");
		}else if(jib==2) {//选分公司的时候刷新出分公司下所有的电厂
			
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
			
			try{
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//燃料公司
					grouping_sql.append(" select decode(grouping(dc.quygs)+grouping(dc.fengs)+grouping(dc.mingc),3,'总计',\n");
					grouping_sql.append("2,dc.quygs,1,'&nbsp;&nbsp;'||dc.fengs,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n");
					
					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.quygs,dc.fengs,dc.mingc) ");
					having_sql.append(" having not grouping(dc.quygs)=1\n");
					orderby_sql.append(" order by grouping(dc.quygs) desc,dc.quygs,grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,max(dc.xuh1)\n ");
				}else{
					grouping_sql.append(" select decode(grouping(dc.fengs)+grouping(dc.mingc),2,'总计',\n");
					grouping_sql.append("1,dc.fengs,'&nbsp;&nbsp;'||dc.mingc) as danw,\n");
					
					where_sql.append(" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ").append("\n");
					rollup_sql.append(" group by rollup (dc.fengs,dc.mingc) ");
					having_sql.append(" having not grouping(dc.fengs)=1\n");
					orderby_sql.append(" order by grouping(dc.fengs) desc ,dc.fengs,grouping(dc.mingc) desc,max(dc.xuh1)\n ");
				}
				rl.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			
		}else{//选择电厂
			grouping_sql.append(" select decode(grouping(dc.mingc),1,'总计',\n");
			grouping_sql.append("dc.mingc) as danw,\n");
			
			where_sql.append(" and dc.id=").append(this.getTreeid()).append("\n");
			rollup_sql.append(" group by rollup (dc.mingc) \n");
			having_sql.append(" having not grouping(dc.mingc)=1\n");
			orderby_sql.append(" order by grouping(dc.mingc) desc,max(dc.xuh1)\n ");
		}
		
			strSQL.append(grouping_sql.toString());
			strSQL.append("sum(htjh.jih) as jih,sum(htjh.rijjh) as rijh,sum(dr.dangrgm) as dangrgm,\n");
			strSQL.append("sum(lj.leijgm) as leijgm,sum(dr.haoyqkdr) as dangrhy,sum(lj.leijhy) as leijhy,\n");
			strSQL.append("(nvl(sum(lj.leijgm),0)-sum(htjh.rijjh)*"+tians+") as jihljc,sum(dr.kuc) as kuc,\n");
			
			if(checkXitszKyts()){
				strSQL.append("decode(grouping(dc.mingc),1,'',sum(decode(hm.meizrjhm, 0, 0, round(h.kuc / hm.meizrjhm)))) as keyts\n");
			}else{
				strSQL.append("decode(grouping(dc.mingc),1,round(sum(dr.kuc)/sum(dr.rizdhml),1),\n");
				strSQL.append("round(keyts_rb(max(dr.diancxxb_id),to_date('"+riq+"','yyyy-mm-dd')),1))  as keyts\n");
			}
			
			strSQL.append("from\n");
			strSQL.append("(select y.diancxxb_id,sum(y.yuejhcgl) as jih,\n");
			strSQL.append("Round(sum(y.yuejhcgl)/daycount(to_date('"+riq+"', 'yyyy-mm-dd')),0) as rijjh\n");
			strSQL.append("from yuecgjhb y\n");
			strSQL.append("where y.riq=First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n");
			strSQL.append("group by y.diancxxb_id ) htjh,\n");
			strSQL.append("(select s.diancxxb_id, sum(s.dangrgm) as dangrgm, sum(s.haoyqkdr) as haoyqkdr, sum(s.kuc) as kuc,\n");
			strSQL.append("sum(nvl(keyts_rijhm(s.diancxxb_id,s.haoyqkdr,s.riq),0)) as rizdhml\n");
			strSQL.append("from shouhcrbb s\n");
			strSQL.append("where s.riq = to_date('"+riq+"', 'yyyy-mm-dd') group by s.diancxxb_id) dr,\n");
			strSQL.append("( select s.diancxxb_id,\n");
			strSQL.append(" sum(s.dangrgm) as leijgm,\n");
			strSQL.append(" sum(s.haoyqkdr) as leijhy\n");
			strSQL.append(" from shouhcrbb s\n");
			strSQL.append(" where s.riq >= First_day(to_date('"+riq+"', 'yyyy-mm-dd'))\n");
			strSQL.append(" and s.riq <= to_date('"+riq+"', 'yyyy-mm-dd')\n");
			strSQL.append(" group by (s.diancxxb_id)) lj,\n");
			
			if(checkXitszKyts()){
			strSQL.append("  ( select d.id,d.xuh as xuh1,d.mingc,d.jingjcml ,d.rijhm,dc.mingc as fengs,sf.id as quygsid,sf.mingc as quygs,d.xuh,dc.id as fuid,d.shangjgsid\n");
			strSQL.append(" from diancxxb d, diancxxb dc ,diancxxb sf\n");
			strSQL.append(" where d.jib = 3\n");
			strSQL.append(" and d.fuid=dc.id  and d.shangjgsid=sf.id(+)) dc,\n");	
			
			strSQL.append(" (select *\n" ); 
			strSQL.append(" from shouhcrbb h\n" );
			strSQL.append(" where h.riq = to_date('"+riq+"', 'yyyy-mm-dd')) h,\n");
			
			strSQL.append(" (select hc.diancxxb_id,\n" );
			strSQL.append(" nvl(round(sum(hc.haoyqkdr) / 7), 0) as meizrjhm\n" );
			strSQL.append(" from shouhcrbb hc\n" ); 
			strSQL.append(" where hc.riq >= to_date('"+riq+"', 'yyyy-mm-dd') - 6\n" );
			strSQL.append(" and hc.riq <= to_date('"+riq+"', 'yyyy-mm-dd')\n");
			strSQL.append(" group by (hc.diancxxb_id)) hm\n");
			}else{
				strSQL.append("  ( select d.id,d.xuh as xuh1,d.mingc,d.jingjcml ,d.rijhm,dc.mingc as fengs,sf.id as quygsid,sf.mingc as quygs,d.xuh,dc.id as fuid,d.shangjgsid\n");
				strSQL.append(" from diancxxb d, diancxxb dc ,diancxxb sf\n");
				strSQL.append(" where d.jib = 3\n");
				strSQL.append(" and d.fuid=dc.id  and d.shangjgsid=sf.id(+)) dc \n");
			}
			
			strSQL.append(" where dc.id=htjh.diancxxb_id(+)\n");
			strSQL.append(" and dc.id=dr.diancxxb_id(+)\n");
			strSQL.append(" and dc.id=lj.diancxxb_id(+) \n");
			if(checkXitszKyts()){
			strSQL.append(" and dc.id = h.diancxxb_id(+)\n");
			strSQL.append(" and dc.id = hm.diancxxb_id(+) \n");
			}
			strSQL.append(where_sql.toString());
			strSQL.append(rollup_sql.toString());
			strSQL.append(having_sql.toString());
			strSQL.append(orderby_sql.toString());
			
		
			// 直属分厂汇总
			ArrHeader=new String[2][10];
			ArrHeader[0]=new String[] {"电厂名称","计划","日均计划","实际供煤","实际供煤","耗用","耗用","与计划累计差","库存","库存煤可用天数"};
			ArrHeader[1]=new String[] {"电厂名称","计划","日均计划","当日","累计","当日","累计","与计划累计差","库存","库存煤可用天数"};
		 
			ArrWidth=new int[] {140,60,65,65,65,65,65,75,65,80};
			
			//System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL.toString());
			 
			// 数据
			Table tb=new Table(rs,2, 0, 1);
			rt.setBody(tb);
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.setTitle("燃料(煤炭)生产调度快报", ArrWidth);
			rt.setDefaultTitle(1, 2, "制表单位:"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(4, 4,  riq1, Table.ALIGN_CENTER);
			rt.setDefaultTitle(9, 2, "单位:吨", Table.ALIGN_RIGHT);
			
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(rt.PAPER_ROWS);
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			
			if(jib==1){
				if(rt.body.getRows()>2){
					rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
				}
			}
			
			
			//页脚 
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,2,"制表时间:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(5,2,"审核:",Table.ALIGN_CENTER);
			  if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
					
					rt.setDefautlFooter(9, 2, "制表:",Table.ALIGN_RIGHT);
					}else{
						
					rt.setDefautlFooter(9, 2, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
					}
		  //  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
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
			if(diancmc.equals("大唐国际发电股份有限公司燃料管理部")){
		          return "大唐国际燃料管理部";
			}else{
				return diancmc;
			}
			
		
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
	
//	矿报表类型
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
		fahdwList.add(new IDropDownBean(0,"分厂汇总"));
		_IBaoblxModel = new IDropDownModel(fahdwList);
		
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
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
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(100);
		tb1.addField(df);
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

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}
}