package com.zhiren.dc.tub;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;

import com.zhiren.common.DateUtil;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
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
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Tub_szs extends BasePage implements PageValidateListener{
	private static final String kucm_rb_zx="煤炭库存走势图";
	private static final String rucmrz_rb_zx="入厂煤热值走势图";
	private static final String rucmrz_rb_zz="矿点月度供煤量柱状图";
	
	
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	初始化
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	当前页
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}
//	所有页
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages = _value;
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
//	绑定日期
	public String getBRiq() {
		return ((Visit)getPage().getVisit()).getString2();
	}
	public void setBRiq(String briq) {
		((Visit)getPage().getVisit()).setString2(briq);
	}
//	绑定日期
	public String getERiq() {
		return ((Visit)getPage().getVisit()).getString3();
	}
	public void setERiq(String eriq) {
		((Visit)getPage().getVisit()).setString3(eriq);
	}
	
	public boolean getRaw() {
		return true;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getToolbar();
		}
	}

	public String getPrintTable() {
		if (kucm_rb_zx.equals(getLeixValue().getValue())) {
			return getKucChart();
		}else if(rucmrz_rb_zx.equals(getLeixValue().getValue())){
			return getRucrzrbChart();
		}else if(rucmrz_rb_zz.equals(getLeixValue().getValue())){
			return getKuangfydgmlChart();
		}else{
			return "";
		}
	}
//	煤炭库存走势图
	public String getKucChart(){
		StringBuffer sbsql = new StringBuffer();
		long lngDays = DateUtil.getQuot(getERiq(), getBRiq());
		sbsql.append(
				"select x.riq,x.fenx,shuj.zhi from (\n" +
				"select s.riq, d.mingc fenx, s.kuc zhi\n" + 
				"  from shouhcrbb s, diancxxb d\n" + 
				" where s.diancxxb_id = d.id\n" + 
				"   and to_char(s.riq, 'yyyy-mm-dd') >= '"+getBRiq()+"'\n" + 
				"   and to_char(s.riq, 'yyyy-mm-dd') < '"+getERiq()+"'\n" + 
				") shuj,\n" + 
				"(select * from\n" + 
				"(select to_date('"+getERiq()+"','yyyy-mm-dd')-rownum as riq from all_objects where rownum<="+lngDays+"),\n" + 
				"(select mingc fenx from diancxxb where id in(509,510))) x\n" + 
				"where shuj.fenx(+)=x.fenx\n" + 
				"   and shuj.riq(+)=x.riq\n" + 
				"order by x.riq,x.fenx");
		return getZheXChart(sbsql.toString(),kucm_rb_zx);
	}
//	入厂煤热值走势图
	public String getRucrzrbChart(){
		StringBuffer sbsql = new StringBuffer();
		String diancid = "and f.diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n";
		sbsql.append(
				"select * from (\n" +
				"select f.daohrq riq,'地方' fenx ,decode(sum(f.sanfsl),0,0,round(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),3)) zhi\n" + 
				"  from zhilb z, fahb f, meikxxb m\n" + 
				" where f.zhilb_id = z.id and f.meikxxb_id=m.id\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') >= '"+getBRiq()+"'\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') < '"+getERiq()+"'\n" + 
				"   and m.jihkjb_id=2\n" + //地方
				diancid +
				"group by f.daohrq\n" + 
				"union\n" + 
				"select f.daohrq riq,'统配' fenx ,round(sum(f.sanfsl*z.qnet_ar)/sum(f.sanfsl),3) zhi\n" + 
				"  from zhilb z, fahb f, meikxxb m\n" + 
				" where f.zhilb_id = z.id and f.meikxxb_id=m.id\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') >= '"+getBRiq()+"'\n" + 
				"   and to_char(f.daohrq, 'yyyy-mm-dd') < '"+getERiq()+"'\n" + 
				"   and m.jihkjb_id=1\n" + //统配
				diancid +
				"group by f.daohrq\n" + 
				") order by riq,fenx");
		return getZheXChart(sbsql.toString(),rucmrz_rb_zx);
	}
//	矿点月度供煤量柱状图
	public String getKuangfydgmlChart(){
		StringBuffer sbsql = new StringBuffer();
		String diancid = "and f.diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n";
		
		sbsql.append("select quanc||' '||mingc as mingc,fenx,zhi shuj from   \n");
		sbsql.append(" (select dc.mingc as quanc,m.mingc, '供应' as fenx,sum(f.sanfsl) as zhi \n");
		sbsql.append(" from fahb f, meikxxb m,diancxxb dc \n");
		sbsql.append("where f.meikxxb_id = m.id \n");
		sbsql.append("and f.daohrq >= to_date('" + getBRiq() + "', 'yyyy-mm-dd') \n");
		sbsql.append("and f.daohrq <= to_date('" + getERiq() + "', 'yyyy-mm-dd') \n");
		sbsql.append("and dc.id=f.diancxxb_id \n");
		sbsql.append(diancid);
		sbsql.append("group by dc.mingc,m.mingc) s order by s.quanc");
		return getZhuZChart(sbsql.toString(),rucmrz_rb_zz);
	}
//	折线图
	public String getZheXChart(String sql,String title){
		JDBCcon cn = new JDBCcon();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		Report rt=new Report();
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];
		
		ResultSet rs=cn.getResultSet(sql.toString());
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","riq", "zhi");//rs记录集构造生成图片需要的数据
		/*--------------设置图片参数开始-------------------*/
		ct.intDigits=0;			//	显示小数位数
		ct.xfontSize=9;		//	x轴字体大小
		ct.dateApeakShowbln = false;//竖直显示x轴的日期
//		ct.lineDateFormatOverride = "dd";//x轴的日期只显示天
		/*--------------设置图片参数结束-------------------*/		
		ArrHeader[0]=new String[] {""+ct.ChartTimeGraph(getPage(), data2, title + "("+getBRiq()+"~"+getERiq()+")", 900, 445)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		cn.Close();

		return rt.getHtml();
	}
//	柱状图
	public String getZhuZChart(String sql,String title){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];
			
		ResultSet rs=cn.getResultSet(sql);
		
		CategoryDataset dataset = cd.getRsDataChart(rs, "mingc", "fenx", "shuj");//rs记录集构造生成图片需要的数据
		/*--------------设置图片参数开始-------------------*/
		ct.intDigits=0;			//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;//倾斜显示X轴的文字
		/*--------------设置图片参数结束-------------------*/		
		//生成柱状图并显示到页面
		ArrHeader[0]=new String[] {""+ct.ChartBar3D(getPage(), dataset,title+"("+getBRiq()+"~"+getERiq()+")", 900, 445)};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		
		cn.Close();
		return rt.getHtml();
	}
	
	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		---------------------------
		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
//		----------------------------
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
//		----------------------------
		tb1.addText(new ToolbarText("类型:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("LeixSelect");
		leix.setWidth(150);
		leix.setListeners("select:function(own,rec,index){Ext.getDom('LeixSelect').selectedIndex=index}");
		tb1.addField(leix);
//		----------------------------
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		setToolbar(tb1);
	}
	
//	-------------------------------------
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			setDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
	public void setDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
	}
//	-------------------------------------------
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	--------------------------------------------
//	类型下拉框
	public IDropDownBean getLeixValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getLeixModel().getOptionCount()>0) {
				setLeixValue((IDropDownBean)getLeixModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setLeixValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getLeixModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setLeixModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setLeixModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	public void setLeixModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0,kucm_rb_zx));
		list.add(new IDropDownBean(1,rucmrz_rb_zx));
		list.add(new IDropDownBean(2,rucmrz_rb_zz));
		list.add(new IDropDownBean(3,"X"));
		list.add(new IDropDownBean(4,"X"));
		setLeixModel(new IDropDownModel(list));
	}
//	--------------------------------------------
	
//	 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			
			setBRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -15, DateUtil.AddType_intDay)));
			setERiq(DateUtil.FormatDate(new Date()));
			setDiancmcModels();

			visit.setString4(null);
			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString4(pagewith);
			}
			getToolBars();
		}
		
		getToolBars();
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
	
}