package com.zhiren.haiym.baob;
/*
 * 作者：赵胜男
 * 时间：2012-08-27
 * 适用范围：只限于庄河电厂
 * 描述：	要求入进煤日报数据需经过数量审核后显示其数据
?           增加参数判断
 */
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Jinm extends BasePage {
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

	// ***************设置消息框******************//
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			getSelectData();
		}

	}

	private boolean isBegin=false;
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getXiem();
	
	}
//	******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			setRiqi(null);
			setRiq2(null);
			visit.setboolean3(false);
			getSelectData();
		}
		getSelectData();
		isBegin=true;
	}

	private String getXiem() {
		JDBCcon cn = new JDBCcon();
		String sbsql = "";
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = cn.getResultSetList("select id from xitxxb where mingc='进煤日报' and zhi='是' and leib='日报' and zhuangt=1 ");
		String shuicl="";
		boolean biaot=false;
		while(rsl.next()){
			shuicl="    sum(round_new(fahb.sanfsl,"+visit.getShuldec()+"))  as shuicl,\n";
			biaot=true;
		}
		
		String condition="";
		if(MainGlobal.getXitxx_item("进煤日报", "是否显示审核后数据", "0", "否").equals("是")){
			condition="  and fahb.liucztb_id=1 and fahb.hedbz=2 \n";
		}
		
			sbsql="select\n" +
					" decode(grouping(gongysb.mingc),1,'合计',gongysb.mingc) as gonghs,\n" + 
					" decode(grouping(meikxxb.mingc)+grouping(gongysb.mingc),1,'合计',2,'',meikxxb.mingc) as meikdw,\n" + 
					" luncxxb.mingc as chuanm,\n" + 
					" fahb.chec as hangc,\n" + 
					" decode(grouping(daohrq)+grouping(meikxxb.mingc)+grouping(gongysb.mingc),1,'小计',2,'合计',3,'',to_char(fahb.daohrq,'yyyy-mm-dd' ))as licrq,\n" + 
					"    sum(round_new(fahb.biaoz,"+visit.getShuldec()+")) as yundl,\n" + 
					"    sum(round_new(fahb.yunsl,"+visit.getShuldec()+")) as yuns ,\n" + 
					"    sum(round_new(fahb.yingk,"+visit.getShuldec()+")) as yingk ,\n" + 
					"    sum(round_new(fahb.maoz,"+visit.getShuldec()+"))  as xiehl,\n" + 
					 shuicl+ 
					"    sum(fahb.ches) as chuans\n" + 
					"from fahb,luncxxb,gongysb,meikxxb,yunsfsb\n" + 
					"    where fahb.luncxxb_id=luncxxb.id(+)\n" + 
					"    and fahb.meikxxb_id=meikxxb.id(+)\n" + 
					"    and fahb.gongysb_id=gongysb.id(+)\n" + 
					"    and yunsfsb.mingc='海运'\n" + 
					condition+
					"    and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+getRiqi()+"'\n" + 
					"    and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+getRiq2()+"'\n" + 
					"group by rollup (gongysb.mingc,meikxxb.mingc,daohrq,luncxxb.mingc,fahb.chec,fahb.ches)\n" + 
					"having not(grouping(daohrq)|| grouping(fahb.ches))=1\n" + 
					"order by  grouping(gongysb.mingc)desc,gongysb.mingc,grouping(meikxxb.mingc)desc,meikxxb.mingc,grouping(luncxxb.mingc)desc,luncxxb.mingc,\n" + 
					"          grouping(daohrq)desc,to_char(fahb.daohrq,'yyyy-mm-dd'),grouping(fahb.chec)desc,fahb.chec,grouping(fahb.ches) desc,fahb.ches";


		ResultSetList rs = cn.getResultSetList(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][10];
		if(biaot){
			ArrHeader[0] = new String[] { "供货商","煤矿单位","船名","航次","离厂日期","运单量","运损","盈亏","卸货量","水尺量","船数" };

		}else{
			ArrHeader[0] = new String[] { "供货商","煤矿单位","船名","航次","离厂日期","运单量","运损","盈亏","卸货量","船数" };
			
		}

		int ArrWidth[] = new int[] { 90,100,60,60,100,90,90,90,90,60};
		
		rt.setTitle("进煤日报", ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, getRiqi()+"至"+getRiq2(),Table.ALIGN_CENTER);
		rt.setDefaultTitle(8, 3, "单位:吨", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "制表时间:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 3,"制表:", Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("开始日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("结束日期:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
		"function(){document.Form0.submit();}");
        tb1.addItem(tb);
        tb1.addText(new ToolbarText("-"));

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

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
}
