//chh 2008-03-17 修改查询语句与表格格式

//chh 2008-10-13 标题显示不正确

package com.zhiren.jt.zdt.rulgl.rulzltj;

/*
 * 作者：赵胜男
 * 时间：2012-04-22
 * 描述: 热值调至水分前。
 */
/*
 * 作者：夏峥
 * 时间：2012-10-15
 * 描述：清除冗余信息
 */
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Calendar;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ext.Toolbar;
//import com.zhiren.common.ext.ToolbarButton;
//import com.zhiren.common.ext.ToolbarText;
//import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rulzltjbb extends BasePage {
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
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr1(_Money);
	}
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
			return getJiesd();
//		} else {
//			return "无此报表";
//		}
	}
	
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
	}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	
	//入煤计量分厂统计报表
	private String getJiesd() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		Report rt=new Report();
		
		String strDate1 = getBeginriqDate();
		String strDate2 = getEndriqDate();
//		String strleix = visit.getString1().substring(0,2);
		long lngjiesbID = Long.parseLong(visit.getString1().substring(2));
		StringBuffer sqlbf=new StringBuffer("");
		String mingc = "";
//		String riq;
//		String haoyl;
//		String mt;
//		String ad;
//		String vdaf;
//		String stad;
//		String qnet_ar;
//		String qnet_ar1;
		
		 try {
//			 结算数据信息
//			 if(strleix.equals("mk")){
				String sql = 
					 "select dc.mingc as mingc,to_char(r.rulrq,'yyyy-mm-dd') as riq,nvl(sum(fadhy+gongrhy),0) as haoyl,\n" +
					 "round(decode(sum(fadhy+gongrhy),0,0,sum(qnet_ar*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as qnet_ar,\n" + 
					 "round(decode(sum(fadhy+gongrhy),0,0,sum((qnet_ar/0.0041816)*(fadhy+gongrhy))/sum(fadhy+gongrhy))) as qnet_ar1,\n" + 
					 "round(decode(sum(fadhy+gongrhy),0,0,sum(mt*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as mt,\n" + 
					 "round(decode(sum(fadhy+gongrhy),0,0,sum(ad*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as ad,\n" + 
					 "round(decode(sum(fadhy+gongrhy),0,0,sum(vdaf*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as vdaf,\n" + 
					 "round(decode(sum(fadhy+gongrhy),0,0,sum(std*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as std\n" + 
					 "from rulmzlb r,meihyb m,diancxxb dc\n" + 
					 "where m.rulmzlb_id(+)=r.id\n" + 
					 "and r.rulrq>=to_date('" + strDate1+ "','yyyy-mm-dd')\n" + 
					 "and r.rulrq<=to_date('" + strDate2+ "','yyyy-mm-dd')\n" + 
					 "and dc.id(+)=r.diancxxb_id\n" + 
					 "and r.diancxxb_id in (select id\n" + 
					 " from(\n" + 
					 " select id from diancxxb\n" + 
					 " start with id=" +lngjiesbID+ "\n" + 
					 " connect by fuid=prior id\n" + 
					 " )\n" + 
					 " union\n" + 
					 " select id\n" + 
					 " from diancxxb\n" + 
					 " where id=" +lngjiesbID+ ")group by r.diancxxb_id,dc.id,r.rulrq,dc.mingc order by r.rulrq";

				 sqlbf.append(sql);
				 ResultSet rs = con.getResultSet(sql);
				 if(rs.next()){
					mingc = rs.getString("mingc");
//					riq = rs.getString("riq");
//					haoyl = rs.getString("haoyl");
//					mt = rs.getString("mt");
//					ad = rs.getString("ad");
//					vdaf = rs.getString("vdaf");
//					stad = rs.getString("std");
//					qnet_ar = rs.getString("qnet_ar");
//					qnet_ar1 = rs.getString("qnet_ar1");
				}
					ResultSet res = con.getResultSet(sqlbf,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			 		
				String ArrHeader[][]=new String[2][9];
				ArrHeader[0]=new String[] {"单位","日期","入炉煤量<br>(吨)","低位发热量","低位发热量","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)"};
				ArrHeader[1]=new String[] {"单位","日期","入炉煤量<br>(吨)","(MJ/kg)","(kcal/kg)","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)"};

				int ArrWidth[]=new int[] {100,80,80,80,80,80,80,80,80};
				Table bt=new Table(res,2,0,2);
				rt.setBody(bt);
				bt.setColAlign(2, Table.ALIGN_CENTER);
				if(rt.body.getRows()>2){
					rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
				}
				
				
				rt.setTitle("入炉煤质量统计",ArrWidth);
				rt.setDefaultTitle(1, 2, "制表单位:"+mingc, Table.ALIGN_LEFT);
				rt.setDefaultTitle(4, 1, "日期:"+strDate1+"至:"+strDate2, Table.ALIGN_CENTER);

				
				rt.setBody(new Table(rs,2,0,1));
				rt.body.setHeaderData(ArrHeader);
				rt.body.setWidth(ArrWidth);
				rt.body.setPageRows(24);
				rt.body.ShowZero = true;
				rt.body.mergeFixedRow();
				rt.body.mergeFixedCols();
				
//				页脚 
				 rt.createDefautlFooter(ArrWidth);
				 rt.setDefautlFooter(1,3,"开始日期:"+strDate1+"至:"+strDate2,Table.ALIGN_LEFT);
//				 rt.setDefautlFooter(5,2,"单位:吨",Table.ALIGN_RIGHT);
				
				_CurrentPage = 1;
				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				
				
				
		 
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			con.Close();
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	
//	private void getToolbars(){
//		Toolbar tb1 = new Toolbar("tbdiv");
//		tb1.addText(new ToolbarText("查询日期:"));
//		DateField df = new DateField();
//		
//		df.setValue(this.getBeginriqDate());
//		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
//		df.setWidth(80);
//		tb1.addField(df);
//		
//		DateField df1 = new DateField();
//		df1.setValue(this.getEndriqDate());
//		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
//		df1.setWidth(80);
//		tb1.addField(df1);
//		tb1.addText(new ToolbarText("-"));
//		
//		
//		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
//		tb1.addItem(tb);
//		
//		setToolbar(tb1);
//	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
//	________________________________________________________

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
	
//	private String FormatDate(Date _date) {
//		if (_date == null) {
//			return "";
//		}
//		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
//	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1("");
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
		}
		if (cycle.getRequestContext().getParameter("rulzltjbb") != null&&!cycle.getRequestContext().getParameter("rulzltjbb").equals("-1")) {
			visit.setString1(cycle.getRequestContext().getParameter("rulzltjbb"));
		}else{
			blnIsBegin = false;
			return;
		}
//		getToolbars();
		blnIsBegin = true;
	}
   
}
