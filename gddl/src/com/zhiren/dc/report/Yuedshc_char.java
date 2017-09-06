package com.zhiren.dc.report;


import java.awt.Color;
import java.awt.GradientPaint;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.data.category.CategoryDataset;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;

/*
 * 时间：2013年6月29日
 * 作者：李凯洋
 * 修改内容：图表中部分数字显示不完整
 * 
 */
/*
 * 时间：2013年7月4日
 * 作者：李凯洋
 * 修改内容：调整图标间距
 * 
 */
public class Yuedshc_char  extends BasePage implements PageValidateListener {

	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
		
	private boolean _QueryClick = false;
	
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}
	
	public void submit(IRequestCycle cyele){
		if(_QueryClick){
			_QueryClick = false;
			this.getPrintChar();
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
	
	public boolean getRaw() {
		return true;
	}
	
	//柱图
	public String getPrintChar(){
		Visit visit = (Visit)this.getPage().getVisit();
		String Sql=
			"SELECT RIQ, LEIX, ZHI\n" +
			"  FROM (SELECT 1 XUH,\n" + 
			"               TO_CHAR(RIQ, 'mm')||'月' RIQ,\n" + 
			"               NVL('收煤量', '') LEIX,\n" + 
			"               SHOUML ZHI\n" + 
			"          FROM YUESHCHJB\n" + 
			"         WHERE DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" + 
			"           AND TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"           AND FENX = '本月'\n" + 
			"        UNION\n" + 
			"        SELECT 2 XUH,\n" + 
			"               TO_CHAR(RIQ, 'mm')||'月' RIQ,\n" + 
			"               NVL('耗用量', '') LEIX,\n" + 
			"               FADY + GONGRY + QITH ZHI\n" + 
			"          FROM YUESHCHJB\n" + 
			"         WHERE DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" + 
			"           AND TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"           AND FENX = '本月'\n" + 
			"        UNION\n" + 
			"        SELECT 3 XUH,\n" + 
			"               TO_CHAR(RIQ, 'mm')||'月' RIQ,\n" + 
			"               NVL('库存量', '') LEIX,\n" + 
			"               KUC ZHI\n" + 
			"          FROM YUESHCHJB\n" + 
			"         WHERE DIANCXXB_ID = "+visit.getDiancxxb_id()+"\n" + 
			"           AND TRUNC(RIQ, 'yyyy') = DATE '"+getYearValue().getValue()+"-01-01'\n" + 
			"           AND FENX = '本月')\n" + 
			" ORDER BY RIQ, XUH";

		JDBCcon con = new JDBCcon();
		ResultSetList rstmp=con.getResultSetList(Sql);
		
		
		//设置柱形图	
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		CategoryDataset dataset = cd.getRsDataChart(rstmp, "RIQ","LEIX","ZHI");//rs记录集构造生成图片需要的数据
		ct.intDigits=0;				//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.chartBackgroundPaint=gp;
		//柱子 间距
		//ct.barItemMargin=0.2;
		ct.barItemMargin=0.09;
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
//		倾斜显示X轴的文字
		ct.xTiltShow = true;		
		ct.showLegend = true;
//		设置不能正常在BAR内显示数据的显示位置
		ct.barfontPlace=ItemLabelAnchor.OUTSIDE1;
//		设置能正常在BAR内显示数据的显示位置
		ct.barfontPlaceNormal=ItemLabelAnchor.OUTSIDE1;		
//		设置Bar的最大宽度
		ct.MaximumBarWidth = 0.35;
//		设置数据距离柱子顶端的距离
		ct.outSide=32;
//		柱子上字的倾斜度。0:水平
		//ct.barfontTilt=-0.7;
		ct.barfontTilt=-1.3;

		
		return ct.ChartBar3D(getPage(), dataset,  getYearValue().getValue()+"年收耗存情况",  1000,  470);
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
	
	
	  //年份下拉框
	  public IDropDownBean getYearValue() {
	    if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
	      ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getYearModel().getOption(DateUtil.getYear(new Date())-2008));
	    }
	    return ((Visit) getPage().getVisit()).getDropDownBean4();
	  }
	  public void setYearValue(IDropDownBean Value) {
	    if(Value!=((Visit) getPage().getVisit()).getDropDownBean4()){
	      ((Visit) getPage().getVisit()).setDropDownBean4(Value);
	    }
	  }
	  public void setYearModel(IPropertySelectionModel value) {
	    ((Visit) getPage().getVisit()).setProSelectionModel4(value);
	  }
	  public IPropertySelectionModel getYearModel() {
	    if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
	      getYearModels();
	    }
	    return ((Visit) getPage().getVisit()).getProSelectionModel4();
	  }
	  public void getYearModels() {
	    StringBuffer sql=new StringBuffer();
	    int i=0;
	    for(i=0;i<=DateUtil.getYear(new Date())-2008;i++){
	      sql.append("select "+i+" id,2008+"+i+" mingc from dual union\n");
	    }
	    sql.append("select "+i+" id,2008+"+i+" mingc from dual\n");
	    ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql.toString()));
	  }
	  
		 private void getToolbars() {
			Toolbar tb1 = new Toolbar("tbdiv");
			
			tb1.addText(new ToolbarText("年份:"));
			ComboBox year = new ComboBox();
			year.setTransform("YearDropDown");
			year.setEditable(true);
			year.setWidth(80);
			tb1.addField(year);
			tb1.addText(new ToolbarText("-"));
			
			//添加刷新按钮
			ToolbarButton tb = new ToolbarButton(null, "查询","function(){document.Form0.submit();}");
			tb.setIcon(SysConstant.Btn_Icon_Search);
			tb1.addItem(tb);
			
			setToolbar(tb1);															
		 }
	  
		 //	页面初始化	
		 public void beginResponse(IMarkupWriter write , IRequestCycle cycle){
			Visit visit = (Visit)this.getPage().getVisit();
			if(!visit.getActivePageName().equals(this.getPageName().toString())){
				//在此添加，在页面第一次加载时需要置为空的变量或方法
				visit.setActivePageName(this.getPageName().toString());
				visit.setList1(null);
				setYearModel(null);
				setYearValue(null);
				getPrintChar();
			}
		getToolbars(); 
	}
	
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

}