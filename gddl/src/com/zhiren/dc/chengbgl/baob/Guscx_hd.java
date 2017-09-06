package com.zhiren.dc.chengbgl.baob;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者：张乐
 * 时间：2011-06-29
 * 描述：增加估收查询
 */

public class Guscx_hd extends BasePage implements PageValidateListener  {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	
	private String after;
	public String getAfter() {
		return after;
	}
	public void setAfter(String after) {
		this.after = after;
	}
	
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}

	public String getBaseSql() {
		String sql = "";
		sql="SELECT --GROUPING(gys.mingc)a,GROUPING(m.mingc) b,GROUPING(p.mingc) c,\n" +
			"decode(gys.mingc,NULL,'总计',gys.mingc) gongys,\n" + 
			"decode(m.mingc,NULL,'小计',m.mingc) meik,p.mingc pinz,sum(round_new(f.laimsl,0)) shul,\n" + 
			"decode(sum(decode(qnet_ar,0,0,f.laimsl)),0,0,round_new(sum(qnet_ar*f.laimsl)/sum(decode(qnet_ar,0,0,f.laimsl)),2)) farl,\n" + 
			"decode(sum(decode(mt,0,0,f.laimsl)),0,0,round_new(sum(mt*f.laimsl)/sum(decode(mt,0,0,f.laimsl)),2)) mt,\n" + 
			"decode(sum(decode(std,0,0,f.laimsl)),0,0,round_new(sum(std*f.laimsl)/sum(decode(std,0,0,f.laimsl)),2)) std,\n" + 
			"decode(sum(decode(vad,0,0,f.laimsl)),0,0,round_new(sum(vad*f.laimsl)/sum(decode(vad,0,0,f.laimsl)),2)) vad,\n" + 
			"decode(sum(decode(aad,0,0,f.laimsl)),0,0,round_new(sum(aad*f.laimsl)/sum(decode(z.aad,0,0,f.laimsl)),2)) aad,\n" + 
			"decode(sum(decode(meij,0,0,f.laimsl)),0,0,round_new(sum(meij*f.laimsl)/sum(decode(meij,0,0,f.laimsl)),2)) meij,\n" + 
			"decode(sum(decode(yunf,0,0,f.laimsl)),0,0,round_new(sum(yunf*f.laimsl)/sum(decode(yunf,0,0,f.laimsl)),2)) yunf,\n" +
			"decode(sum(decode(zaf,0,0,f.laimsl)),0,0,round_new(sum(zaf*f.laimsl)/sum(decode(zaf,0,0,f.laimsl)),2)) zaf,\n" + 
			"decode(sum(decode(meij+yunf+zaf,0,0,f.laimsl)),0,0,round_new(sum((meij+yunf+zaf)*f.laimsl)/sum(decode(meij+yunf+zaf,0,0,f.laimsl)),2)) hej\n" + 
			"FROM fahb f,zhilb z,gongysb gys,meikxxb m,pinzb p,diancxxb d,\n" + 
			"(select id, fahb_id, meij, meis, yunf, yunfs,zaf\n" + 
			"          from guslsb\n" + 
			"         where id in (select max(id) id\n" + 
			"                        from guslsb g,\n" + 
			"                             (select fahb_id, max(leix) leix\n" + 
			"                                from guslsb\n" + 
			"                               group by fahb_id) g2\n" + 
			"                       where g.fahb_id = g2.fahb_id\n" + 
			"                       and g.leix = g2.leix\n" + 
			"                       group by g.fahb_id)) g\n" + 
			"WHERE g.fahb_id = f.id AND f.gongysb_id = gys.id AND f.diancxxb_id = d.id \n" + 
			"AND f.daohrq>="+DateUtil.FormatOracleDate(getRiq())+"\n" + 
			"AND f.daohrq<="+DateUtil.FormatOracleDate(getAfter())+"\n" + 
			"AND f.zhilb_id = z.id AND f.meikxxb_id = m.id AND f.pinzb_id = p.id\n" + 
			"AND (d.id = " + getTreeid_dc()+" or d.fuid = "+ getTreeid_dc()+") \n"+
			"GROUP BY ROLLUP (gys.mingc,m.mingc,p.mingc)\n" + 
			"HAVING NOT GROUPING(gys.mingc)+GROUPING(m.mingc)+GROUPING(p.mingc)=1\n" + 
			"ORDER BY gys.mingc,m.mingc,p.mingc";

		return sql;
	}
	
//	刷新衡单列表
	public void getSelectData() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "");
		df.setId("Riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		DateField df1 = new DateField();
		df1.setValue(getAfter());
		df1.Binding("After", "");
		df1.setId("After");
		tb1.addField(df1);
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
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	public String getPrintTable(){
		if(getRiq()==null || getAfter()==null){
			return "";
		}
		JDBCcon con = new JDBCcon();
		
		ResultSetList rs = con.getResultSetList(getBaseSql());
		if (rs == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		Report rt = new Report();
		
		String[][] ArrHeader;
		int[] ArrWidth;
		String[] strFormat;
		
		ArrHeader = new String[2][13];
		ArrHeader[0]=new String[]{"供应商", "煤矿","品种","数量<br>(吨)","发热量<br>(Mj/kg)"
				,"质量(%)","质量(%)","质量(%)","质量(%)"
				,"价格(元/吨)","价格(元/吨)","价格(元/吨)","价格(元/吨)",};
		
		ArrHeader[1]=new String[]{"供应商", "煤矿","品种","数量<br>(吨)","发热量<br>(Mj/kg)"
				
				,"水分<br>(Mt)","挥发分<br>(Vad)","灰分<br>(Aad)","硫<br>(Std)"
				,"煤价","运费","杂费","合计"};
		ArrWidth = new int[] { 100, 100, 50, 50, 50, 50, 50, 50, 50, 50, 50,50, 50};
		strFormat = new String[] {
				"" , "","", "0"  ,"0.00"
				,"0.00","0.00","0.00","0.00"
				,"0.00" ,"0.00" ,"0.00" ,"0.00"};
			
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("估收情况表", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 6, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

//		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 5, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "制表：", Table.ALIGN_LEFT);
//		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;
	}
//	工具栏使用的方法
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setInt1(Integer.parseInt(reportType));
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setAfter(DateUtil.FormatDate(new Date()));
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getDiancmcModels();
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
				
		}
		getSelectData();
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		getSelectData();
	}
//	页面登陆验证
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
	
//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
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
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
}
