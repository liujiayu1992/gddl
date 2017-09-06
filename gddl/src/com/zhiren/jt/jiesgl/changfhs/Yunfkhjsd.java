package com.zhiren.jt.jiesgl.changfhs;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者:tzf
 * 时间:2010-03-11
 * 修改内容:发货日期  到货日期    批次号 调整成 区间
 */
public class Yunfkhjsd  extends BasePage implements PageValidateListener{

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
	
	private String getDate(){
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
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

	public void submit(IRequestCycle cycle) {

	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1("");	//记录上一页面传过来的表名，编号
			
			if(cycle.getRequestContext().getParameters("lx")!=null){
				
				visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			}
		}
//		第二次跳转页面用
		if(cycle.getRequestContext().getParameters("lx")!=null){
			
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
		}
		
		this.getSelectData();
		isBegin = true;
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;
	
	private String getSelectData(){
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn=new JDBCcon();
		String jiesbh = "";
		jiesbh = visit.getString1();
		String sql_jieslx="select jieslx,hetb_id from jiesb where bianm='"+jiesbh+"'";
		ResultSetList rsl_jieslx=cn.getResultSetList(sql_jieslx);
		rsl_jieslx.next();
		if(!rsl_jieslx.getString("jieslx").equals("1")){//若jiesb.jieslx!=1,即非两票结算
			setMsg("该结算单没有运费结算附单");
			return "";
		}
		String sql_hetgl="select * from meikyfhtglb where hetb_id="+rsl_jieslx.getString("hetb_id");
		ResultSetList rsl_hetgl=cn.getResultSetList(sql_hetgl);
		if(!rsl_hetgl.next()){//若meikyfhtglb里无数据，说明该结算单没有运费结算附单
			setMsg("该结算单没有运费结算附单");
			return "";
		}
		
		_CurrentPage=1;
		_AllPages=1;
		
		
		String yunsdw="";//运输单位
        String zhidrq="";//制单日期
		String hetbh = "";//合同编号
		String no = "";//结算单编号
		String gonghdw="";//供货单位
		String jieszq = "";//结算周期
		double kuangfsl =0;//矿发数量
		double shissl=0;//实收数量
		double kuissl=0;//亏损数量
		double yunxks =0;//允许亏损
		double kaohks =0;//考核亏损
		double hetyj =0;//合同运费单价
		double hetmj =0;//合同煤价
		double yunfsp = 0;//运费索赔
		double meiksp = 0;//煤款索赔
		double suopzj = 0;//索赔总计
		double shijyfdj =0;//实际运费单价
		double yingfyf = 0;//应付运费
		double shifyf =0;//实付运费
		String hetb_id="";//hetb_id煤款合同Id
		String hetys_id="";//hetys_id 运输合同id
		String jiesb_id="";
		
		String table = "";
		String table_mk = "";
		String table_yf = "";
		String tiaoj = "";
		String sql_jsb = "";
		/*table = visit.getString1().substring(0,visit.getString1().indexOf(";"));
		table_mk = table.substring(0,table.indexOf(","));
		table_yf = table.substring(table.indexOf(",")+1);*/
		
		
		//运输单位
		String sql_yunsdw=
			"select quanc\n" +
			"  from yunsdwb\n" + 
			" where id in (select distinct yunsdwb_id\n" + 
			"                from chepb\n" + 
			"               where fahb_id in\n" + 
			"                     (select id\n" + 
			"                        from fahb\n" + 
			"                       where jiesb_id =\n" + 
			"                             (select id\n" + 
			"                                from jiesb\n" + 
			"                               where bianm = '"+jiesbh+"')))";

		ResultSetList rsl_yunsdw = cn.getResultSetList(sql_yunsdw);
		if(rsl_yunsdw.next()){
			yunsdw=rsl_yunsdw.getString("quanc");
		}
		
		
		//结算信息
		String sql_jies="select * from jiesb where bianm='"+jiesbh+"'";
		ResultSetList rsl_jies = cn.getResultSetList(sql_jies);
		if(rsl_jies.next()){
			jiesb_id=rsl_jies.getString("id");
			zhidrq=rsl_jies.getDateString("jiesrq");
			jieszq=rsl_jies.getDateString("fahksrq")+"<br>至<br>"+rsl_jies.getDateString("fahjzrq");
			//kuangfsl=rsl_jies.getDouble("jiessl")+rsl_jies.getDouble("koud");//票重
			shissl=rsl_jies.getDouble("guohl");
			hetb_id=rsl_jies.getString("hetb_id");
			gonghdw=rsl_jies.getString("gongysmc");
			
		}
        //矿发数量
		String sql_kuangfsl="select sum(biaoz) kuangfsl from fahb where jiesb_id="+jiesb_id;
		ResultSetList rsl_kuangfsl = cn.getResultSetList(sql_kuangfsl);
		if(rsl_kuangfsl.next()){
			kuangfsl=rsl_kuangfsl.getDouble("kuangfsl");
		}
		
		//合同煤价
		String sql_hetjg="select jij from hetjgb where hetb_id="+hetb_id;
		ResultSetList rsl_hetjg = cn.getResultSetList(sql_hetjg);
		if(rsl_hetjg.next()){
			hetmj=rsl_hetjg.getDouble("jij");
		}
		
		String sql_het="select * from meikyfhtglb where hetb_id="+hetb_id;
		ResultSetList rsl_het = cn.getResultSetList(sql_het);
		if(rsl_het.next()){
			hetys_id=rsl_het.getString("hetys_id");
		}
		//运输合同编号
		String sql_yunsht="select * from hetys where id="+hetys_id;
		ResultSetList rsl_yunsht = cn.getResultSetList(sql_yunsht);
		if(rsl_yunsht.next()){
			//hetys_id=rsl_yunsht.getString("hetys_id");
			hetbh=rsl_yunsht.getString("hetbh");
		}
		//运费单价
		String sql_hetysjg="select * from hetysjgb where hetys_id="+hetys_id;
		ResultSetList rsl_hetysjg = cn.getResultSetList(sql_hetysjg);
		if(rsl_hetysjg.next()){
			hetyj=rsl_hetysjg.getDouble("yunja");
		}
		//实付汽车运费
		String sql_shifyf=
			"select a.zhi\n" +
			"  from (select *\n" + 
			"          from feiyb\n" + 
			"         where feiyb_id =\n" + 
			"               (select feiyb_id\n" + 
			"                  from yunfdjb\n" + 
			"                 where id =\n" + 
			"                       (select distinct yunfdjb_id\n" + 
			"                          from danjcpb\n" + 
			"                         where yunfjsb_id =\n" + 
			"                               (select id\n" + 
			"                                  from jiesyfb\n" + 
			"                                 where bianm = '"+jiesbh+"')))) a,\n" + 
			"       (select feiyxm.id feiyxmb_id, feiymcb.mingc\n" + 
			"          from (select id, feiymcb_id from feiyxmb) feiyxm, feiymcb\n" + 
			"         where feiyxm.feiymcb_id = feiymcb.id) b\n" + 
			" where a.feiyxmb_id = b.feiyxmb_id\n" + 
			"   and b.mingc = '汽车运费'";
		ResultSetList rsl_shifyf = cn.getResultSetList(sql_shifyf);
		if(rsl_shifyf.next()){
			shifyf=rsl_shifyf.getDouble("zhi");
		}

		
		kuissl=CustomMaths.Round_New(kuangfsl-shissl,2);
		yunxks=CustomMaths.Round_New(kuangfsl*0.005,4);
		kaohks=CustomMaths.Round_New(kuissl-yunxks,4);
		yunfsp=CustomMaths.Round_New(hetyj*(kuissl-yunxks),2);
		meiksp=CustomMaths.Round_New(hetmj*kaohks,2);
		suopzj=CustomMaths.Round_New(yunfsp+meiksp,2);
		shijyfdj=CustomMaths.Round_New(hetyj-suopzj/kuangfsl,6);
		yingfyf=CustomMaths.Round_New(shijyfdj*kuangfsl,2);
		
		rsl_yunsdw.close();
		rsl_jies.close();
		rsl_kuangfsl.close();
		rsl_hetjg.close();
		rsl_het.close();
		rsl_yunsht.close();
		rsl_hetysjg.close();
		rsl_shifyf.close();
		
		String ArrHeader[][]=new String[5][15];
		ArrHeader[0]=new String[] {"供货单位:"+gonghdw,"","","","","结算明细","","","","","","","","",""};
		ArrHeader[1]=new String[] {"序号","结算周期","矿发数量<br>(吨)","实收数量<br>(吨)","亏损数量<br>(吨)","允许亏损<br>(吨)","考核亏损<br>(吨)","合同运<br>费单价<br>(吨)","合同煤<br>炭单价<br>(吨)",
				                   "运费索赔<br>(元)","煤款索赔<br>(元)","索赔总计<br>(元)","实际运费单价<br>(元)","应付运费<br>(元)","实付运费<br>(元)"};
		ArrHeader[2]=new String[] {"1",jieszq,kuangfsl+"",shissl+"",kuissl+"",yunxks+"",kaohks+"",hetyj+"",hetmj+"",yunfsp+"",meiksp+"",suopzj+"",shijyfdj+"",yingfyf+"",shifyf+""};
		ArrHeader[3]=new String[] {"合计","合计",kuangfsl+"",shissl+"",kuissl+"",yunxks+"",kaohks+"",hetyj+"",hetmj+"",yunfsp+"",meiksp+"",suopzj+"",shijyfdj+"",yingfyf+"",shifyf+""};
		ArrHeader[4]=new String[] {"备注:","","","","","","","","","","","","","",""};
		
		int ArrWidth[]=new int[] {40,80,60,60,60,60,60,60,60,60,60,60,60,60,60};
//		 数据
		Report rt=new Report();
		rt.setTitle("国电电力酒泉发电有限公司燃料运费结算单",ArrWidth);
		rt.setDefaultTitle(1,9,"承运单位:"+yunsdw+"<br>结算部门:国电电力酒泉发电有限公司计划经营部",Table.ALIGN_LEFT);
		rt.setDefaultTitle(10,5,"制单日期:"+zhidrq+"<br>合同编号:"+hetbh,Table.ALIGN_LEFT);
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		
		rt.body.mergeCell(1,1,1,5);
		rt.body.mergeCell(1,6,1,15);
		rt.body.mergeCell(4,1,4,2);
		rt.body.mergeCell(5,1,5,15);
		
		rt.body.setRowHeight(30);

		rt.body.setPageRows(18);
		//rt.body.setHeaderData(ArrHeader);// 表头数据
		for(int i=1;i<=4;i++){
		  for(int j=1;j<=15;j++){
			rt.body.setColAlign(j, Table.ALIGN_CENTER);
		  }
		}
        
		
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 5, "计划经营部审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13,2, "制表:", Table.ALIGN_LEFT);
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
	
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

}