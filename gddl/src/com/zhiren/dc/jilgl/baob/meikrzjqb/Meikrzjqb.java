package com.zhiren.dc.jilgl.baob.meikrzjqb;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author wangzongbing
 * 日期:2010-5-21 9:17:55
 * 类名：供应商,煤矿,热值加权表
 */

public class Meikrzjqb extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	开始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

//	结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
//	供应商下拉框_开始
	public IDropDownBean getGongysValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getGongysModel().getOptionCount() > 0) {
				setGongysValue((IDropDownBean) getGongysModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setGongysValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getGongysModels() {
		String sql = "select id, mingc from gongysb where leix=1 order by mingc";
		setGongysModel(new IDropDownModel(sql, "请选择"));
	}
//	供应商下拉框_结束
	
	
	
//	煤矿下拉框_开始
	public IDropDownBean getMeikValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getMeikModel().getOptionCount() > 0) {
				setMeikValue((IDropDownBean) getMeikModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setMeikValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LeibValue);
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			getMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setMeikModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getMeikModels() {
		String sql = "select id, mingc from meikxxb  order by mingc";
		setMeikModel(new IDropDownModel(sql, "请选择"));
	}
//	煤矿下拉框_结束
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String tiaoj = "";
		if (!getGongysValue().getStrId().equals("-1")) {
			tiaoj = "and f.gongysb_id = " + getGongysValue().getStrId();
		}
		//如果煤矿下拉框有值,则只考虑煤矿条件,不考虑供应商条件
		if(this.getMeikValue().getId()!=-1){
			tiaoj=" and f.meikxxb_id="+this.getMeikValue().getId();
		}
		
		
		String sql=
		"select decode(g.mingc,null,'总计',g.mingc) as gongys,\n" +
		"decode(grouping(m.mingc)+grouping(g.mingc),1,'合计',m.mingc) as meik,\n" + 
		"decode(grouping(g.mingc)+grouping(m.mingc)+grouping(f.daohrq),1,'小计',to_char(f.daohrq,'yyyy-mm-dd')) as daohrq,\n" + 
		"sum(f.jingz) as jingz,\n" + 
		"decode(sum(f.jingz),0,0,round_new(sum(z.stad*f.jingz)/sum(decode(z.stad,null,0, f.jingz)),2)) as stad,\n" + 
		"decode(sum(f.jingz),0,0,round_new(sum(z.qnet_ar*f.jingz)/sum(decode(z.qnet_ar,null,0,f.jingz))*1000/4.1816,0)) as rez\n" + 
		" from fahb f,gongysb g,meikxxb m,zhilb z\n" + 
		" where f.zhilb_id=z.id(+)\n" + 
		" and f.gongysb_id=g.id\n" + 
		" and f.meikxxb_id=m.id\n" + 
		" and f.daohrq>=to_date('"+ getBRiq() +"','yyyy-mm-dd')\n" + 
		" and f.daohrq<=to_date('"+ getERiq() +"','yyyy-mm-dd')\n" + 
		""+tiaoj+"\n"+
		" group by rollup (g.mingc,m.mingc,f.daohrq)\n" + 
		" order by g.mingc,m.mingc,f.daohrq";


		ResultSetList rslData = con.getResultSetList(sql);
		String[][] ArrHeader = new String[1][6];
		ArrHeader[0] = new String[]{"供应商", "煤矿", "日期", "来煤量(吨)", "硫分", "热值"};
		
		int[] ArrWidth = new int[] {160, 120, 100, 80, 80, 80};
		
		rt.setTitle(((Visit)this.getPage().getVisit()).getDiancmc()+"煤矿热值硫分加权表", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(30);
		rt.body.setHeaderData(ArrHeader);
		rt.body.ShowZero = true;
		for (int i = 1; i <= 6; i ++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		

//		设定小计行的背景色和字体
		for (int i=2;i<=rt.body.getRows();i++){
			String xiaoj=rt.body.getCellValue(i, 3);
			if((xiaoj.equals(""))){
				for (int j=0;j<rt.body.getCols()+1;j++){
					rt.body.getCell(i, j).backColor="silver";
					rt.body.getCell(i, j).fontBold=true;
				}
			}
		}
		
		
		//rt.setDefaultTitle(1, 4, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(1, 4, "日期："+getBRiq()+"至"+getERiq(), Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_LEFT);
		//rt.setDefautlFooter(4, 2, "审核：", Table.ALIGN_CENTER);
		//rt.setDefautlFooter(7, 2, "制表：", Table.ALIGN_LEFT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("到货日期："));
		DateField bdf = new DateField();
		bdf.setValue(getBRiq());
		bdf.Binding("BRIQ", "Form0");// 与html页中的id绑定,并自动刷新
		tbr.addField(bdf);
		
		tbr.addText(new ToolbarText("至"));
		DateField edf = new DateField();
		edf.setValue(getERiq());
		edf.Binding("ERIQ", "Form0");// 与html页中的id绑定,并自动刷新
		tbr.addField(edf);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("供应商单位："));
		ComboBox gysdw = new ComboBox();
		gysdw.setTransform("Gongys");
		gysdw.setWidth(120);
		gysdw.setListWidth(150);
		gysdw.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		gysdw.setLazyRender(true);
		tbr.addField(gysdw);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("煤矿单位："));
		ComboBox mk = new ComboBox();
		mk.setTransform("Meik");
		mk.setWidth(120);
		mk.setListWidth(150);
		mk.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		mk.setLazyRender(true);
		tbr.addField(mk);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel3(null); //供应商下拉框
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null); //煤矿下拉框
			visit.setDropDownBean4(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}