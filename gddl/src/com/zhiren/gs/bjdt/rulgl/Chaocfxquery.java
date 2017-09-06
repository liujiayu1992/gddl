package com.zhiren.gs.bjdt.rulgl;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.contrib.palette.SortMode;

public class Chaocfxquery extends BasePage implements PageValidateListener{

	// 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
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
	// 电厂用户进来时显示电厂名称
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.mingc as mingc from diancxxb dc where dc.id="
				+ this.getTreeid();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}

	// 开始日期
	private Date _BeginriqValue = new Date();

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (DateUtil.Formatdate("yyyy-MM-dd", _BeginriqValue).equals(
				DateUtil.Formatdate("yyyy-MM-dd", _value))) {
			_BeginriqChange = false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange = true;
		}
	}

	// 开始日期
	private Date _EndriqValue = new Date();

	private boolean _EndriqChange = false;

	public Date getEndriqDate() {
		if (_EndriqValue == null) {
			_EndriqValue = new Date();
		}
		return _EndriqValue;
	}

	public void setEndriqDate(Date _value) {
		if (_EndriqValue.equals(_value)) {
			_EndriqChange = false;
		} else {
			_EndriqValue = _value;
			_EndriqChange = true;
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
		// 为 "刷新" 按钮添加处理程序
		isBegin = true;
		 getSelectData();
	}

	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setList1(null);
			((Visit) getPage().getVisit()).getList1();
			_BeginriqValue = new Date();
			_EndriqValue = new Date();
//			setBaoblxValue(null);
//			getIBaoblxModels();
			isBegin = true;
			this.setTreeid(null);
			visit.setList1(null);
			this.getSelectData();
			
		}
//		if (_Baoblxchange) {
//			_Baoblxchange = false;
//			Refurbish();
//		}
//		if (_BeginriqChange) {
//			_BeginriqChange = false;
//			Refurbish();
//		}
		if (_EndriqChange) {
			_EndriqChange = false;
			Refurbish();
		}
		getToolBars();
//		Refurbish();
	}

	private String RT_HET = "Rezcbcx";

	private String mstrReportName = "Rezcbcx";

	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;
		if (mstrReportName.equals(RT_HET)) {
			return getSelectData();
		} else {
			return "无此报表";
		}
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt = 0;

	public void setZhuangt(int _value) {
		intZhuangt = 1;
	}

	private String Week(Date date) {// 得到当前日期所在周
		String week = "";
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int day = cal.get(Calendar.DATE);
		int weekname = cal.get(Calendar.DAY_OF_WEEK);

		String beginweek = "";
		String endweek = "";
		// GetDate dg = new GetDate();
		switch (weekname) {

		case 4: // 星期三
			beginweek = "r.riq>=" + OraDate(date) + "";
			endweek = "r.riq<=" + OraDate(date) + "+6";
			break;
		case 5: // 星期四
			beginweek = "r.riq>=" + OraDate(date) + "-1";
			endweek = "r.riq<=" + OraDate(date) + "+5";
			break;
		case 6: // 星期五
			beginweek = "r.riq>=" + OraDate(date) + "-2";
			endweek = "r.riq<=" + OraDate(date) + "+4";
			break;
		case 7: // 星期六
			beginweek = "r.riq>=" + OraDate(date) + "-3";
			endweek = "r.riq<=" + OraDate(date) + "+3";
			break;
		case 1: // 星期日
			beginweek = "r.riq>=" + OraDate(date) + "-4";
			endweek = "r.riq<=" + OraDate(date) + "+2";
			break;
		case 2: // 星期一
			beginweek = "r.riq>=" + OraDate(date) + "-5";
			endweek = "r.riq<=" + OraDate(date) + "+1";
			break;
		case 3: // 星期二
			beginweek = "r.riq>=" + OraDate(date) + "-6";
			endweek = "r.riq<=" + OraDate(date) + "";
			break;

		default:
			break;
		}
		week = " and " + beginweek + " and " + endweek;
		return week;
	}

	private Date WeekFistDate(Date date) {// 得到当前日期所在周
		
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		return cal.getTime();
	}

	private Date WeekLastDate(Date date) {// 得到当前日期所在周
		
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		cal.add(Calendar.DAY_OF_MONTH, 6);
		return cal.getTime();
	}

	


	private boolean isBegin = false;

	private String getSelectData() {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String ArrFormat[] = null;
		Visit visit=(Visit)this.getPage().getVisit();
		   String zhibdw=this.getDiancName();
			if(zhibdw.equals("北京大唐")&&visit.getRenyjb()==2){
				zhibdw="";
			}
		String titlename =zhibdw+"热值差超差分析";
		int iFixedRows = 0;// 固定行号
		int iCol = 0;// 列数
//		String riq = "";
//		String riqlj="";
		String titledate = "";// 标题日期
		
		int year = 0;
		int month = 0;
		boolean zongh=false;


		String strdiancid = "";
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancid=" ";
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancid = "  and dc.fuid=  " +this.getTreeid();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancid=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strdiancid=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		String riq = " r.riq>=" + OraDate(WeekFistDate(getEndriqDate()))+ " and r.riq<=" + OraDate(WeekLastDate(getEndriqDate()))+ "";
		JDBCcon con=new JDBCcon();
		
		// 报表内容
			strSQL=
				"select decode(grouping(riq),1,'合计',to_char(r.riq, 'yyyy-mm-dd')) as riq,\n" +
				"       sum(r.rucsl) as rucsl,\n" + 
				"       decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)) as rucrl,\n" + 
				"       decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucsf)/sum(r.rucsl),2)) as rucsf,\n" + 
				"       sum(r.rulsl) as rulsl,\n" + 
				"       decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) as rulrl,\n" + 
				"       decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulsf)/sum(r.rulsl),2)) as rulsf,\n" + 
				"       decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2))\n" + 
				"            -decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) as rezctzq,\n" + 
				"       round((decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2))\n" + 
				"            -decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)))*1000/4.1816,0) as rezctzqdk,\n" + 
				"       ROUND(decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2))\n" + 
				"           - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2))\n" + 
				"           * (100 - decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucsf)/sum(r.rucsl),2)))\n" + 
				"           / (100 - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulsf)/sum(r.rulsl),2))), 2) as rezctzh,\n" + 
				"       ROUND(ROUND(decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2))\n" + 
				"           - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2))\n" + 
				"           * (100 - decode(sum(r.rucsl),0,0,round(sum(r.rucsl*r.rucsf)/sum(r.rucsl),2)))\n" + 
				"           / (100 - decode(sum(r.rulsl),0,0,round(sum(r.rulsl*r.rulsf)/sum(r.rulsl),2))), 2)\n" + 
				"           * 1000 / 4.1816, 0) as rezctzhdk,r.beiz\n" + 
				"  from rezcb r, diancxxb dc\n" + 
				" where  "+riq+"\n" + 
				"   and r.diancxxb_id = dc.id  "+strdiancid+"\n" + 
				" group by rollup ((r.beiz,riq)) order by riq";

			
			ArrHeader = new String[2][12];
			ArrHeader[0] = new String[] { "日期", "入厂煤", "入厂煤", "入厂煤", "入炉煤", "入炉煤","入炉煤", "水分调整前热值差", "水分调整前热值差", "水分调整后热值差", "水分调整后热值差", "备注" };
			ArrHeader[1] = new String[] { "日期", "数量(t)", "Qnet,ar(MJ/kg)", "Mt(%)","数量(t)", "Qnet,ar(MJ/kg)", "Mt(%)", "mj/kg", "大卡/公斤", "mj/kg","大卡/公斤", "备注" };

			ArrWidth = new int[] { 130, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
			ArrFormat = new String[] { "", "0", "0.00", "0.00", "0", "0.00", "0.00", "0.00", "0", "0.00", "0", "" };
			iFixedRows = 1;
			iCol = 10;

		

//		 System.out.println(strSQL);
		ResultSet rs = con.getResultSet(strSQL);

		// 数据
		rt.setBody(new Table(rs, 2, 0, iFixedRows));

		rt.setTitle(titledate + titlename, ArrWidth);
		//记住这个时间
		rt.setDefaultTitle(4, 4, DateUtil.FormatDate(WeekFistDate(getEndriqDate())) + " 至 "
				+DateUtil.FormatDate(WeekLastDate(getEndriqDate())) , Table.ALIGN_CENTER);
//		rt.setDefaultTitle(iCol, 3, "制表日期:" + FormatDate(getBeginriqDate()),Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColFormat(ArrFormat);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
//		 rt.createDefautlFooter(ArrWidth);
//		 rt.setDefautlFooter(10,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		 
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		// System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
	}
	//新添加的方法，超差分析的内容和人员
	public String getChaocfx(){
		StringBuffer sb = new StringBuffer();
//		int leib=((Visit) getPage().getVisit()).getLeib();
//		long strdiancid = -1;
		String strdiancid = "";
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancid=" ";
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancid = this.getTreeid();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancid=this.getTreeid();
			 
		}else if (jib==-1){
			strdiancid=""+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
//		if (leib==0){
//			strdiancid = ((Visit) getPage().getVisit()).getDiancxxbId();
//		}else if(leib==1){
//			strdiancid = getDiancmcValue().getId();
//		}
		String fenxnr = "";//超差分析内容
		String fenxry = "";
		String fenxrq = DateUtil.FormatDate(getEndriqDate());
		JDBCcon cn = new JDBCcon();
		String sql = "select replace(replace(rz.chaocfx,chr(9),chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'||chr(38)||'nbsp;'),chr(32),chr(38)||'nbsp;') as chaocfx,rz.chaocfxry " 
			       + "  from rezcb rz where rz.riq="+OraDate(getEndriqDate())+" and rz.diancxxb_id="+strdiancid;
		try{
			ResultSet rs = cn.getResultSet(sql);
			if(rs.next()){
				fenxnr = rs.getString("chaocfx");
				fenxry = rs.getString("chaocfxry");
				if(fenxnr != null){
					fenxnr = fenxnr.replaceAll("\n", "<br>");
				}
			}
//			fenxry = getProperValue(getIDiancmcModel(),strdiancid);
			if(fenxnr!=null){
				if(!fenxnr.equals("")){
					sb.append("<center><span id=\"chaocfx\" style=\"page-break-after: always;\">");
					sb.append("<table width=\"690\" style=\"font-family:宋体;font-size:11pt;\"><tr align=\"left\" height=\"25\"><td valign=\"bottom\">超差分析:</td></tr><tr align=\"justify\"><td>");
					sb.append(fenxnr);
					sb.append("</td></td><tr align=\"right\"><td>");
					sb.append(fenxry);
					sb.append("</td></td><tr align=\"right\"><td>");
					sb.append(fenxrq);
					sb.append("</td></td></table></span></center>");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return sb.toString();
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

	// ***************************报表初始设置***************************//
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

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
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



	// 分公司下拉框
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
		setDiancxxModel(new IDropDownModel(sql));
	}

	// 集团用户用的和分公司下拉框关联的电厂下拉框
	public boolean _diancmcchange1 = false;

	private IDropDownBean _DiancmcValue1;

	public IDropDownBean getDiancmcValue1() {
		if (_DiancmcValue1 == null) {
			_DiancmcValue1 = (IDropDownBean) getIDiancmcModel1s().getOption(0);
		}
		return _DiancmcValue1;
	}

	public void setDiancmcValue1(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue1 != null) {
			id = _DiancmcValue1.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange1 = true;
			} else {
				_diancmcchange1 = false;
			}
		}
		_DiancmcValue1 = Value;
	}

	private IPropertySelectionModel _IDiancmcModel1;

	public void setIDiancmcModel1(IPropertySelectionModel value) {
		_IDiancmcModel1 = value;
	}

	public IPropertySelectionModel getIDiancmcModel1() {
		if (_IDiancmcModel1 == null) {
			getIDiancmcModel1s();
		}
		return _IDiancmcModel1;
	}

	public IPropertySelectionModel getIDiancmcModel1s() {

		String sql = "";
		long fenggsId = this.getFengsValue().getId();
		sql = "select d.id,d.mingc from diancxxb d where d.fuid=" + fenggsId
				+ " order by d.mingc desc";

		_IDiancmcModel1 = new IDropDownModel(sql);
		return _IDiancmcModel1;
	}

	// /结束

	// 分公司用户进来时显示的电厂名称下拉框
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
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
		
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getEndriqDate()));
		df.Binding("riqEndSelect","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
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
	
}