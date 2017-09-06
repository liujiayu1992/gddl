package com.zhiren.jt.jiesgl.changfhs;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author ly
 * 2009-08-24
 * 亏吨亏卡拒付单
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-27 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Kuidkkjfd  extends BasePage implements PageValidateListener{

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
	
//	private String OraDate(Date _date){
//		if (_date == null) {
//			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
//		}
//		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
//	}
	
	private String getDate(){
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
	}
	
	private boolean rqchange = false;//判断日期是否改变
	private boolean gyschange = false;//判断供应商是否改变
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
			visit.setString1("");
			
			if(cycle.getRequestContext().getParameters("lx")!= null){
				
				visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			}
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

	private String getHetbh(String jiesbh) {
		String Hetbh = "";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String sql = "select ht.hetbh from jiesb js, hetb ht, fahb fh where js.id = fh.jiesb_id and ht.id = fh.hetb_id and js.bianm = '" + jiesbh + "'";
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			Hetbh = rsl.getString("hetbh");
		}
		
		rsl.close();
		con.Close();
		return Hetbh;
	}
	
	private boolean isBegin=false;

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();	
		
		String table = v.getString1().substring(0,v.getString1().indexOf(";"));
		String table_mk = table.substring(0,table.indexOf(","));
		String table_yf = table.substring(table.indexOf(",")+1);
		String jiesbh = v.getString1().substring(v.getString1().indexOf(";")+1);
		
		String tiaoj = "";
		
		if(table_mk.equals("jiesb")){
			tiaoj = "diancjsmkb_id";
		}else if(table_mk.equals("diancjsmkb")){
			tiaoj = "diancjsmkb_id";
		}else if(table_mk.equals("kuangfjsmkb")){
			tiaoj = "kuangfjsmkb_id";
		}
		
		String sql = 
			"select to_char(js.jiesrq,'yyyy')||to_char(js.jiesrq,'MM')||to_char(js.jiesrq,'dd') as riq,\n" +
			"		   to_char(js.yansksrq,'yyyy')||'.'||to_char(js.yansksrq,'MM')||'.'||to_char(js.yansksrq,'dd')||'--'||to_char(js.yansjzrq,'yyyy')||'.'||to_char(js.yansjzrq,'MM')||'.'||to_char(js.yansjzrq,'dd') as jiesrq,\n" +
			"		   m.quanc as kuangb,\n" +
			"		   dc.quanc as jiesdw,\n" +
			"          getjiesdzb('jiesb' ,js.id ,'结算数量' ,'gongf') as biaoz,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'结算数量' ,'yingk') as yingk,\n" + 
			"          nvl(js.jiessl,0) as jijsl,\n" + 
			"          nvl(jy.kuidjfyf,0)+nvl(jy.kuidjfzf,0) as yunzf,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'结算数量' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'结算数量' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'结算数量' ,'zhejje'))  as meikjf,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'hetbz')  as hQnetar,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Vad' ,'hetbz')  as hVad,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Std' ,'hetbz')  as hStd,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Mt' ,'hetbz')  as hMt,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'changf')  as cQnetar,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Vad' ,'changf')  as cVad,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Std' ,'changf')  as cStd,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Mt' ,'changf')  as cMt,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'gongf')  as gQnetar,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Vad' ,'gongf')  as gVad,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Std' ,'gongf')  as gStd,\n" + 
			"          getjiesdzb('jiesb' ,js.id ,'Mt' ,'gongf')  as gMt,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'zhejbz'))  as dQnetar,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Vad' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Vad' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Vad' ,'zhejbz'))  as dVad,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Std' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Std' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Std' ,'zhejbz'))  as dStd,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Mt' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Mt' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Mt' ,'zhejbz'))  as dMt,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Qnetar' ,'zhejje'))  as jQnetar,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Vad' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Vad' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Vad' ,'zhejje'))  as jVad,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Std' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Std' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Std' ,'zhejje'))  as jStd,\n" + 
			"          decode(abs(getjiesdzb('jiesb' ,js.id ,'Mt' ,'zhejje')),getjiesdzb('jiesb' ,js.id ,'Mt' ,'zhejje'),0,getjiesdzb('jiesb' ,js.id ,'Mt' ,'zhejje'))  as jMt,\n" + 
			"          js.hansdj as chebj\n" + 
			"from "+table_mk+" js,"+table_yf+" jy,meikxxb m,diancxxb dc\n" + 
			"where js.id=jy."+tiaoj+"(+)\n" + 
			"             and (js.bianm='"+jiesbh+"' or jy.bianm='"+jiesbh+"')\n" +
			"			  and js.meikxxb_id = m.id\n" +
			"			  and js.diancxxb_id = dc.id\n";

		ResultSetList rs = con.getResultSetList(sql);
		String jiesrq = "";
		String riq = "";
		String kuangb = "";
		String jiesdw = "";
		String biaoz = "";
		String yingk = "";
		String jijsl = "";
		
		int yunzf = 0;
		int meikjf = 0;
		int hej = 0;
		
		String hQnetar = "";
		String hVad = "";
		String hStd = "";
		String hMt = "";
		
		String cQnetar = "";
		String cVad = "";
		String cStd = "";
		String cMt = "";
		
		String gQnetar = "";
		String gVad = "";
		String gStd = "";
		String gMt = "";
		
		int dQnetar = 0;
		int dVad = 0;
		int dStd = 0;
		int dMt = 0;
		
		int jQnetar = 0;
		int jVad = 0;
		int jStd = 0;
		int jMt = 0;
		
		int dj_hj = 0;
		int je_hj = 0;
		String chebj = "";

		if(rs.next()){
			  jiesrq = rs.getString("jiesrq");
			  riq = rs.getString("riq");
			  kuangb = rs.getString("kuangb");
			  jiesdw = rs.getString("jiesdw");
			  biaoz = rs.getString("biaoz");
			  yingk = rs.getString("yingk");
			  jijsl = rs.getString("jijsl");
			
			  yunzf = rs.getInt("yunzf");
			  meikjf = rs.getInt("meikjf");
			  hej = yunzf+meikjf;
			
			  hQnetar = rs.getString("hQnetar");
			  hVad = rs.getString("hVad");
			  hStd = rs.getString("hStd");
			  hMt = rs.getString("hMt");
			
			  cQnetar = rs.getString("cQnetar");
			  cVad = rs.getString("cVad");
			  cStd = rs.getString("cStd");
			  cMt = rs.getString("cMt");
			
			  gQnetar = rs.getString("gQnetar");
			  gVad = rs.getString("gVad");
			  gStd = rs.getString("gStd");
			  gMt = rs.getString("gMt");
			
			  dQnetar = rs.getInt("dQnetar");
			  dVad = rs.getInt("dVad");
			  dStd = rs.getInt("dStd");
			  dMt = rs.getInt("dMt");
			
			  jQnetar = rs.getInt("jQnetar");
			  jVad = rs.getInt("jVad");
			  jStd = rs.getInt("jStd");
			  jMt = rs.getInt("jMt");
			  
			  dj_hj = dQnetar + dVad + dStd +dMt;
			  je_hj = jQnetar + jVad + jStd +jMt;
			  chebj = rs.getString("chebj");
		}
		
		
		 String ArrBody[][]=new String[11][8];
		 ArrBody[0]=new String[] {"结算日期",jiesrq,jiesrq,jiesrq,jiesrq,"亏吨扣款","运杂费",yunzf+""};
		 ArrBody[1]=new String[] {"票重",biaoz,biaoz,biaoz,biaoz,"亏吨扣款","煤款",meikjf+""};
		 ArrBody[2]=new String[] {"盈亏",yingk,yingk,yingk,yingk,"其它扣款","其它扣款",""};
		 ArrBody[3]=new String[] {"计价数量",jijsl,jijsl,jijsl,jijsl ,"合计","合计",hej+""};
		 ArrBody[4]=new String[] {"合同指标","合同指标","入厂化验情况","入厂化验情况","矿方化验","煤质扣款","单价","金额"};
		 ArrBody[5]=new String[] {"Qnet,ar",hQnetar,"Qnet,ar",cQnetar,gQnetar,"Qnet,ar",dQnetar+"",jQnetar+""};
		 ArrBody[6]=new String[] {"Vad",hVad,"Vad",cVad,gVad,"Vad",dVad+"",jVad+""};
		 ArrBody[7]=new String[] {"St",hStd,"St",cStd,gStd,"St",dStd+"",jStd+""};
		 ArrBody[8]=new String[] {"Mt",hMt,"Mt",cMt,gMt,"Mt",dMt+"",jMt+""};
		 ArrBody[9]=new String[] {"其它","","","","","","",""};
		 ArrBody[10]=new String[] {"车板价(元/吨)",chebj,"车板价(元/吨)",chebj,chebj,"合计",dj_hj+"",je_hj+""};

		 int ArrWidth[]=new int[] {95,120,95,80,85,105,85,85};

		 rt.setBody(new Table(ArrBody,0,0,0));
		 rt.body.setWidth(ArrWidth);

		rt.body.merge(1, 2, 1, 5);
		rt.body.merge(2, 2, 2, 5);
		rt.body.merge(3, 2, 3, 5);
		rt.body.merge(4, 2, 4, 5);
		rt.body.merge(1, 6, 2, 6);
		rt.body.merge(3, 6, 3, 7);
		rt.body.merge(4, 6, 4, 7);
		rt.body.merge(5, 3, 5, 4);
		rt.body.merge(11, 4, 11, 5);

		rt.body.setPageRows(18);
		rt.body.setRowHeight(30);
		rt.body.ShowZero = true;
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		
		rt.setTitle("亏 吨 亏 卡 拒 付 单", ArrWidth);
		rt.setDefaultTitle(1, 5, "矿&nbsp;&nbsp;&nbsp;&nbsp;别："+kuangb+"<br>结算单位："+jiesdw,Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 3, "结算单号：" + jiesbh + "<br>" + "合同编号：" + getHetbh(jiesbh) + "",Table.ALIGN_LEFT);
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 2, "数量负责人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "质量负责人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "数量质量复审员：", Table.ALIGN_LEFT);
		
		rt.title.fontSize=11;
		rt.body.fontSize=11;
		rt.footer.fontSize=11;
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
//	******************************************************************************
	
	
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