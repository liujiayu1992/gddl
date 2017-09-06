package com.zhiren.dc.jilgl.gongl.daoy;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;
/*
 * 作者：王磊
 * 时间：2009-10-28
 * 描述：将时间由12进制转换为24进制
 */
/*
 * 作者:tzf
 * 时间:2009-07-15
 * 内容:根据系统配置，显示净重列。
 */
/**
 * @author ly
 *
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-20 17：51
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Daoytjcx  extends BasePage implements PageValidateListener{
	
	private static final String BAOBPZB_GUANJZ = "JL_DY_TJ";// baobpzb中对应的关键字   计量管理 倒运统计查询

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
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
		}
	}

	

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setShijValue(null);
			setShijModel(null);
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			visit.setList4(null);
			this.getSelectData();
			
			//begin方法里进行初始化设置
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString4(null);保存传递的非默认纸张的样式
		}
		isBegin=true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;

	private StringBuffer getBaseSql(){

		StringBuffer buffer = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		
		JDBCcon con=new JDBCcon();
//		-----------------------------
		String jingz_sql=" select zhi  from xitxxb where mingc='搬倒车数量净重字段截取保留位数' and leib='数量' and zhuangt=1 and diancxxb_id="+v.getDiancxxb_id();
		ResultSetList rsl_jz=con.getResultSetList(jingz_sql);
		
		String baol_cu="-1";
		
		String jingz_cons=" sum(q.maoz-q.piz) as jingz, q.qitz as qitz, ";//净重字段 用到的 计算公式
		if(rsl_jz.next()){//判断系统净重  字段 模式   
			baol_cu=rsl_jz.getString("zhi");
			jingz_cons=" sum(trunc((q.maoz-q.piz),"+baol_cu+")) as jingz, ";
		}
		
		
		
		String sql = 
			"select decode(grouping(decode('" + getShijValue().getValue() + "','重车时间',to_char(q.zhongcsj,'yyyy-MM-dd'),to_char(q.qingcsj,'yyyy-MM-dd'))),1,'合计',decode('" + getShijValue().getValue() + "','重车时间',to_char(q.zhongcsj,'yyyy-MM-dd'),to_char(q.qingcsj,'yyyy-MM-dd'))) as riq,\n" +
			"       decode(grouping(decode('" + getShijValue().getValue() + "','重车时间',to_char(q.zhongcsj,'yyyy-MM-dd'),to_char(q.qingcsj,'yyyy-MM-dd')))+grouping(q.cheph),1,'小计',q.cheph) as cheh,\n" + 
			"       (select m.mingc from meicb m where q.yuanmc_id = m.id) as yuanmc,\n" + 
			"       (select m.mingc from meicb m where q.xiemc_id = m.id) as xiemc,\n" + 
			"       count(*) as ches," +	
			"       sum(q.maoz) as maoz,\n" + 
			"       sum(q.piz) as piz,\n" + 
			jingz_cons+"\n"+
			"       sum(q.biaoz) as biaoz,\n" + 
			"       to_char(q.zhongcsj,'yyyy-MM-dd HH24:mi:ss') as zhongcsj,\n" + 
			"       q.zhongch,\n" + 
			"       q.zhongcjjy,\n" + 
			"       to_char(q.qingcsj,'yyyy-MM-dd HH24:mi:ss;') as qingcsj,\n" + 
			"       q.qingch,\n" + 
			"       q.qingcjjy,\n" + 
			"       q.beiz\n" + 
			"from qicdyb q,diancxxb d\n" + 
			"where q.diancxxb_id = d.id\n" + 
			"      and d.id = " + getTreeid_dc() + "\n" + 
			"      and decode('" + getShijValue().getValue() + "','重车时间',q.zhongcsj,q.qingcsj) >= "+DateUtil.FormatOracleDate(getBRiq()) + 
			"      and decode('" + getShijValue().getValue() + "','重车时间',q.zhongcsj,q.qingcsj) < "+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" +
			"group by rollup (decode('" + getShijValue().getValue() + "','重车时间',to_char(q.zhongcsj,'yyyy-MM-dd'),to_char(q.qingcsj,'yyyy-MM-dd')),q.cheph,q.yuanmc_id,q.xiemc_id,q.zhongcsj,q.zhongch,q.zhongcjjy,q.qingcsj,q.qingch,q.qingcjjy,q.beiz)\n" + 
			"having not (grouping(q.cheph)||grouping(q.beiz))=1\n" + 
			"order by grouping(decode('" + getShijValue().getValue() + "','重车时间',to_char(q.zhongcsj,'yyyy-MM-dd'),to_char(q.qingcsj,'yyyy-MM-dd'))),\n" + 
			"         decode('" + getShijValue().getValue() + "','重车时间',to_char(q.zhongcsj,'yyyy-MM-dd'),to_char(q.qingcsj,'yyyy-MM-dd')),\n" + 
			"         grouping(q.cheph),q.cheph,q.zhongcsj,q.qingcsj\n";

		buffer.append(sql);
		return buffer;
	}
	
	private String getSelectData(){
		Visit v = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
//
//		StringBuffer buffer = new StringBuffer();
//		buffer=this.getBaseSql();

		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
//		System.out.println(buffer);
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		int aw=0;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con
					.getResultSetList("select biaot from baobpzb where guanjz='"
							+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制

			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;

			ArrHeader = new String[][] { {"日期","车号","原煤场","卸煤场","车数","毛重","皮重","净重","票重","重车时间","重车衡号","重车检斤员","轻车时间","轻车衡号","轻车检斤员","备注"} };

			ArrWidth = new int[] { 100,80,80,80,60,60,60,60,60,140,60,65,140,60,65,80 };

			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.setTitle("汽车厂内倒运统计查询", ArrWidth);
		}
		

		ArrWidth = new int[] { 100,80,80,80,60,60,60,60,60,140,60,65,140,60,65,80};
		rt.setTitle("汽车厂内倒运统计查询", ArrWidth);
		
		Table tb=new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle("汽车厂内倒运统计查询", ArrWidth);
//		rt.setDefaultTitle(1, 18, getBRiq() + " 至 " + getERiq(), Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_LEFT);
		rt.body.setColAlign(11, Table.ALIGN_LEFT);
		rt.body.setColAlign(12, Table.ALIGN_CENTER);
		rt.body.setColAlign(13, Table.ALIGN_LEFT);
		rt.body.setColAlign(14, Table.ALIGN_LEFT);
		rt.body.setColAlign(15, Table.ALIGN_LEFT);

		rt.createDefautlFooter(ArrWidth);

		con.Close();
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages> 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), getBaseSql().toString(), rt,
				"汽车厂内倒运统计查询", "" + BAOBPZB_GUANJZ);
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

	
	public void getToolBars() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
//		tb1.addText(new ToolbarText(":"));
		ComboBox fh = new ComboBox();
		fh.setTransform("ShijSelect");
		fh.setWidth(130);
		fh.setListeners("select:function(own,rec,index){Ext.getDom('ShijSelect').selectedIndex=index}");
		tb1.addField(fh);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("起始日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("xiemrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
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
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
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
	
//	重车时间/轻车时间下拉框
	public IDropDownBean getShijValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getShijModel().getOptionCount()>0) {
				setShijValue((IDropDownBean)getShijModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setShijValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getShijModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setShijModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setShijModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public IPropertySelectionModel setShijModels() {
		List list = new ArrayList();
		list.add(new  IDropDownBean("1","重车时间"));
		list.add(new  IDropDownBean("2","轻车时间"));

		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
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

}