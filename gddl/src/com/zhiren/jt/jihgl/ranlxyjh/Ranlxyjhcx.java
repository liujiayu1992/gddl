package com.zhiren.jt.jihgl.ranlxyjh;

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
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 
*/


public class Ranlxyjhcx extends BasePage implements PageValidateListener {

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

	// 开始日期
	private Date _BeginriqValue = new Date();

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
			// _BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			// _BeginriqChange=true;
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
			// this.getSelectData();
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
			visit.setList1(null);
			setNianfValue(null);
//			setYuefValue(null);
			getNianfModels();
//			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			isBegin = true;

		}

		getToolBars();
		this.Refurbish();
	}

	private String RT_HET = "Yuedmjgmxreport";// 月度煤价格明细

	private String mstrReportName = "Yuedmjgmxreport";

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

	private boolean isBegin = false;

	private String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}

		String zhuangt_sl = "";
		String zhuangt_zl = "";
		String zhuangt_dj = "";

		String strGongsID = "";
		String guoltj = "";
		String notHuiz = "";
		String biaot = "";
		String biaoti = "";
		String group = "";
		String order = "";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// 选集团时刷新出所有的电厂
			strGongsID = " ";
			guoltj = " and dc.id not in(" + Guoldcid() + ")\n";
			biaot = "select decode(grouping(g.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
			group = "  group by rollup(f.mingc,g.mingc)\n";

			order = " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " + this.getTreeid()
					+ " or dc.shangjgsid=" + this.getTreeid() + ")";
			guoltj = " and dc.id not in(" + Guoldcid() + ")\n";
			notHuiz = " having not grouping(f.mingc)=1 ";// 当电厂树是分公司时,去掉集团汇总

			biaot = "select decode(grouping(g.mingc)+grouping(f.mingc),2,'总计',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
			group = "  group by rollup(f.mingc,g.mingc)\n";

			order = " order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

		} else if (jib == 3) {// 选电厂只刷新出该电厂
			strGongsID = " and dc.id= " + this.getTreeid();

			if (getBaoblxValue().getValue().equals("分厂汇总")) {
				notHuiz = " having not  grouping(dc.mingc)=1";
			} else if (getBaoblxValue().getValue().equals("分矿汇总")) {
				biaot = "select decode(grouping(g.mingc)+grouping(dc.mingc),2,'总计',1,dc.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n";
				group = "  group by rollup(dc.mingc,g.mingc)\n";

				order = " order by grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

				notHuiz = " having not  grouping(dc.mingc)=1";// 当电厂树是电厂时,去掉分公司和集团汇总
			} else {
				notHuiz = " having not  grouping(f.mingc)=1";// 当电厂树是电厂时,去掉分公司和集团汇总
			}
		} else if (jib == -1) {
			strGongsID = " and dc.id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			guoltj = "";
		}

		long yuefen = getYuefValue().getId();
		long yuefen1 = getYuefValue().getId();
		long yuefen2 = getYuefValue().getId();
		long yuefen3 = getYuefValue().getId();
		long yuefen4 = getYuefValue().getId();
		long nianfen = getNianfValue().getId();
		String nianyue = "";
		if (String.valueOf(yuefen).length() == 1) {
			nianyue = String.valueOf(nianfen) + "0" + String.valueOf(yuefen)
					+ "01";
		} else {
			nianyue = String.valueOf(nianfen) + String.valueOf(yuefen) + "01";
		}

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] = null;
		int iFixedRows = 0;// 固定行号
		int iCol = 0;// 列数
		// 报表内容

		biaoti = "燃料需用计划表";
		// JDBCcon con = new JDBCcon();
//		visit.getRenyjb();	3、电厂；2、公司；1、集团
		String sql="";
		String idpd="";
		String sqq="select  d.jib jib from diancxxb d where d.id="+getTreeid();
		ResultSet rsq = cn.getResultSet(sqq);
//		System.out.println(sqq);
		try {
			while(rsq.next()){
				idpd=rsq.getString("jib");
				
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
        if("3".equals(idpd)){
        	
        	sql=

       		 "select decode(xuh,8,'其中',"
					+ "9,'其中',10,'其中',11,'其中',12,'其中',15,'其中',16,'其中',"
					+ "17,'其中',18,'其中',19,'其中',20,'其中',21,'其中',22,'其中',23,"
					+ "'其中',24,'其中',25,'其中',26,'其中',27,'其中',28,'其中',29,'其中',30,'其中',31,'其中'"
					+ ",32,'其中',33,'其中',34,'其中',35,'其中',36,'其中',37,'其中',38,'其中',39,'其中',40,'其中',13,8,14,9,41,10,42,11,xuh) as 序号, mingc ,beiz ,nvl(value,0) as 合计 ,nvl(y1,0) as 一月份 ,nvl(y2,0) as 二月份,nvl(y3,0) as 三月份,nvl(y4,0) as 四月份,nvl(y5,0) as 五月份,nvl(y6,0) as 六月份,\n"
					+ "nvl(y7,0) as 七月份,nvl(y8,0) as 八月份,nvl(y9,0) as 九月份,nvl(y10,0) as 十月份,nvl(y11,0) as 十一月份,nvl(y12,0) as 十二月份 from (\n"
					+ "select im.mingc,im.xuh xuh,im.beiz beiz, nvl(sum(rhb.value),0) value,nvl(sum(rhb.y1),0) y1,nvl(sum(rhb.y2),0) y2,nvl(sum(rhb.y3),0) y3,nvl(sum(rhb.y4),0) y4,"
					+ "nvl(sum(rhb.y5),0) y5,nvl(sum(rhb.y6),0) y6,nvl(sum(rhb.y7),0) y7\n"
					+ ",nvl(sum(rhb.y8),0) y8,nvl(sum(rhb.y9),0) y9,nvl(sum(rhb.y10),0) y10 ,nvl(sum(rhb.y11),0) y11 ,nvl(sum(rhb.y12),0) y12\n"
					+ " from ranlxyjhzbb rzb ,ranlxyjhb rhb ,item im,itemsort it\n"
					+ "where rhb.ranlxyjhzbb_id=rzb.id and to_char(rzb.nianf,'yyyy')="
					+ getNianfValue()
					+ " and rzb.diancxxb_id="
					+ getTreeid()
					+ "\n"
					+ "and im.itemsortid=it.id and rhb.zhibmc_item_id=im.id\n"
					+ " and instr(im.diancxxbs_id,to_char(rzb.diancxxb_id))>0 \n"
					+

					"group by (im.xuh,im.mingc,im.beiz)\n"
					+ "order by im.xuh\n" + "\n" + ")\n" + "";
        	
        }else{
        	
        	 sql=

             	"select decode(xuh,8,'其中',"
					+ "9,'其中',10,'其中',11,'其中',12,'其中',15,'其中',16,'其中',"
					+ "17,'其中',18,'其中',19,'其中',20,'其中',21,'其中',22,'其中',23,"
					+ "'其中',24,'其中',25,'其中',26,'其中',27,'其中',28,'其中',29,'其中',30,'其中',31,'其中'"
					+ ",32,'其中',33,'其中',34,'其中',35,'其中',36,'其中',37,'其中',38,'其中',39,'其中',40,'其中',13,8,14,9,41,10,42,11,xuh) as 序号, mingc ,beiz ,nvl(value,0) as 合计 ,nvl(y1,0) as 一月份 ,nvl(y2,0) as 二月份,nvl(y3,0) as 三月份,nvl(y4,0) as 四月份,nvl(y5,0) as 五月份,nvl(y6,0) as 六月份,\n"
					+ "nvl(y7,0) as 七月份,nvl(y8,0) as 八月份,nvl(y9,0) as 九月份,nvl(y10,0) as 十月份,nvl(y11,0) as 十一月份,nvl(y12,0) as 十二月份 from (\n"
					+ "select im.mingc,im.xuh xuh,im.beiz beiz, nvl(sum(rhb.value),0) value,nvl(sum(rhb.y1),0) y1,nvl(sum(rhb.y2),0) y2,nvl(sum(rhb.y3),0) y3,nvl(sum(rhb.y4),0) y4,"
					+ "nvl(sum(rhb.y5),0) y5,nvl(sum(rhb.y6),0) y6,nvl(sum(rhb.y7),0) y7\n"
					+ ",nvl(sum(rhb.y8),0) y8,nvl(sum(rhb.y9),0) y9,nvl(sum(rhb.y10),0) y10 ,nvl(sum(rhb.y11),0) y11 ,nvl(sum(rhb.y12),0) y12\n"
					+ " from ranlxyjhzbb rzb ,ranlxyjhb rhb ,item im,itemsort it\n"
					+ "where rhb.ranlxyjhzbb_id=rzb.id and to_char(rzb.nianf,'yyyy')="
					+ getNianfValue()
					+ "and im.itemsortid=it.id and rhb.zhibmc_item_id=im.id\n"
					+ "group by (im.xuh,im.mingc,im.beiz)\n"
					+ "order by im.xuh\n" + "\n" + ")\n" + "";
        	 
        }
    	StringBuffer cs = new StringBuffer();
		cs.append("序号,指标名称,单位,合计,一月份,二月份,三月份,四月份,五月份,六月份,七月份,八月份,九月份,十月份,十一月份,十二月份");
		ArrHeader = new String[1][16];
		ArrHeader[0] = cs.toString().split(",");
//		
		ArrWidth = new int[] { 30, 100, 60, 80, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50,50 };
		// arrFormat=new String
		// []{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
//		iFixedRows = 1;
//		iCol = 10;
		// }
		ResultSet rs = cn.getResultSet(sql);
		String biao="";
		String sqlq="select mingc from diancxxb d where id="+getTreeid();
		ResultSet rsr = cn.getResultSet(sqlq);
		try {
			while(rsr.next()){
				biao=rsr.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			try {
				rsr.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}		
		// 数据
		Table tb = new Table(rs, 1, 0, 1);
		rt.setBody(tb);

		rt.setTitle("大唐黑龙江发电有限公司"+ intyear + "年"  + biaoti,
				ArrWidth);
		rt
				.setDefaultTitle(1, 3, "填报单位:"
						+biao ,
						Table.ALIGN_LEFT);

//		rt.setDefaultTitle(7, 3, "填报日期:" + intyear + "年" + intMonth + "月",
//				Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 5, "", Table.ALIGN_RIGHT);//

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = true;

		// rt.body.setColFormat(arrFormat);
		// 页脚
		rt.body.mergeFixedRow();
		
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 3, "制表:", Table.ALIGN_LEFT);
		

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();


		return rt.getAllPagesHtml();
	}

	// 得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  mingc from itemsort where bianm='RANLXYJHZB'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return biaotmc;

	}

	// 电厂名称
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

		String sql = "";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);

	}

	// 矿报表类型
	public boolean _Baoblxchange = false;

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if (_BaoblxValue == null) {
			_BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
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
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "分矿汇总"));
			fahdwList.add(new IDropDownBean(1, "分厂汇总"));
			fahdwList.add(new IDropDownBean(2, "分厂分矿汇总"));

			_IBaoblxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IBaoblxModel;
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

	// 页面判定方法
	public void pageValidate(PageEvent arg0) {
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
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
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));


		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));


		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}

	// 查询是否设置过滤电厂id
	private String Guoldcid() {
		JDBCcon con = new JDBCcon();
		String dcid = "";
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb where mingc='过滤上海杨树浦发电、上海吴泾热电、上海闵行数据'\n");

		while (rsl.next()) {
			dcid = rsl.getString("zhi");
		}
		con.Close();

		return dcid;
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

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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
//	 年份
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
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
	public boolean Changeyuef = false;

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
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}


}
