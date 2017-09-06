package com.zhiren.dc.handkhh;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author 尹佳明
 * 2009-06-02
 */
/*
 * 2009-06-09
 * 修改时间和下拉框输入
 * 
 * 2009-06-11
 * 修改日期显示及报表内容
 */
public class DianczbrzReport extends BasePage {
	
	public IDropDownBean getBumValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getBumModel().getOptionCount()>0) {
				setBumValue((IDropDownBean)getBumModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setBumValue(IDropDownBean bumValue) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(bumValue);
	}
	
	public IPropertySelectionModel getBumModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setBumModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setBumModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setBumModels() {
		String str = "select 0 id,'全部' bum from dual union select rownum id,b.bum bum " +
			"from (select distinct bum from dianczbrzb order by bum) b";
		setBumModel(new IDropDownModel(str.toString()));
	}

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

//	绑定起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String REPORT_NAME_REZC = "zbrz";

	private String mstrReportName = "";

	public String getPrintTable() {
		if (mstrReportName.equals(REPORT_NAME_REZC)) {
			return getSelectData();
		} else {
			return "无此报表";
		}
	}

	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期："));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "Form0");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" "));
		tb1.addText(new ToolbarText(" 至 "));
		tb1.addText(new ToolbarText(" "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "Form0");// 与html页中的id绑定,并自动刷新
		tb1.addField(dfe);
		
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("部门："));
		ComboBox comb = new ComboBox();
		comb.setWidth(100);
		comb.setTransform("Bum");
		comb.setId("Bum");
		comb.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
		comb.setLazyRender(true);
		comb.setEditable(true);
		tb1.addField(comb);
		
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
		
		StringBuffer sbsql = new StringBuffer();
		
		String bumName = getBumValue().getValue();
		String briq = getBRiq();
		String eriq = getERiq();
		if(!bumName.equals("全部")){
			bumName = " and d1.bum = " + "'" + bumName + "'";
		} else {
			bumName = " ";
		}
		
		sbsql.append("select rownum rw, d2.mingc, to_char(d1.riq, 'yyyy-MM-dd'), " +
			"d1.shij, d1.bum, d1.neir, d3.quanc, to_char(d1.caozsj, 'yyyy-mm-dd'), " +
			"d1.beiz from dianczbrzb d1, diancxxb d2, renyxxb d3 where d1.diancxxb_id = d2.id and d1.caozyid = d3.id" )
			.append(bumName).append(" and d1.riq between to_date('").append(briq).append("', 'yyyy-MM-dd') and to_date('")
			.append(eriq).append("', 'yyyy-MM-dd')").append(" order by rw \n");
		ResultSet rs = con.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][] = new String[1][11];
		ArrHeader[0] = new String[] { "序号", "电厂名称", "日期", "时间", "部门", "内容", "操作员", "操作时间", "备注"};

		// 定义列宽
		int ArrWidth[] = new int[] { 30, 120, 80, 70, 80, 140, 70, 80, 140};

		// 设置页标题
		rt.setTitle("电厂值班日志表", ArrWidth);
		
		// 数据
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.ShowZero = false;

		// 页眉页脚
		rt.createDefautlFooter(ArrWidth);
		
		rt.setDefaultTitle(1, 3, "制表单位：" + ((Visit)this.getPage().getVisit()).getDiancqc(), 
				Table.ALIGN_LEFT);
		
		rt.setDefaultTitle(5, 2, getBRiq() + " 至 " + getERiq(), Table.ALIGN_CENTER);
		
		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "制表：", Table.ALIGN_LEFT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		return rt.getAllPagesHtml();
	}

//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
	
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel2(null);
			visit.setString1("");
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		
		String[] param = null;
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
            param= cycle.getRequestContext().getParameters("lx");
            if(param != null ) {
            	mstrReportName=param[0];
            }
        }
		getSelectData();
	}
}

