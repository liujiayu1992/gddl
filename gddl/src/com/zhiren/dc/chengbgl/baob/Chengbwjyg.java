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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者：王磊
 * 时间：2009-11-19
 * 描述：更改SQL错误
 */
/*
 * 作者：王磊
 * 时间：2009-12-08
 * 描述：未结算预估成本增加品种
 */
/*
 * 作者：王磊
 * 时间：2010-01-19
 * 描述：修改电厂树未初始化的问题
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Chengbwjyg extends BasePage  implements PageValidateListener  {
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
	
	private boolean hasFenC(String id) { // 是否有分厂
		JDBCcon con = new JDBCcon();
		String sql = "select mingc from diancxxb where fuid=" + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			rsl.close();
			return true;

		}
		rsl.close();
		return false;

	}
	
	public String getBaseSql() {
		String talbe = "";
		if(getLeibValue().getId()==0){
			talbe = "m";
		}else{
			talbe = "g";
		}
		String sql2 = "";
		if (this.hasFenC(getTreeid_dc())) {
			sql2 = " and d.fgsid = " + getTreeid_dc();
		}else{
			sql2 = " and d.id = " + getTreeid_dc();
		}
		String sql = 
			"select mingc,pinz,jingz,biaoz,zj,mj,yj,zf,je,qnetar,decode(qnetar,0,0,zj*29.271/qnetar) bmdj from\n" +
			"(select decode(grouping("+talbe+".mingc),1,'总计',"+talbe+".mingc) mingc, p.mingc pinz,\n" + 
			"sum(round(f.jingz,0)) jingz, sum(round(f.biaoz,0)) biaoz,\n" + 
			"decode(sum(decode(nvl(r.meij-r.meijs+r.yunj-r.yunjs+r.jiaohqzf + r.zaf + r.daozzf + r.qitfy,0),0,0,f.jingz)),0,0,\n" + 
			"round(sum((r.meij - r.meijs + r.yunj - r.yunjs+r.jiaohqzf + r.zaf + r.daozzf + r.qitfy)* f.jingz)/\n" + 
			"sum(decode(nvl(r.meij-r.meijs+r.yunj-r.yunjs+r.jiaohqzf + r.zaf + r.daozzf + r.qitfy,0),0,0,f.jingz)),2)) zj,\n" + 
			"decode(sum(decode(nvl(r.meij-r.meijs,0),0,0,f.jingz)),0,0,round(sum((r.meij - r.meijs)\n" + 
			"* f.jingz)/sum(decode(nvl(r.meij-r.meijs,0),0,0,f.jingz)),2)) mj,\n" + 
			"decode(sum(decode(nvl(r.yunj-r.yunjs,0),0,0,f.jingz)),0,0,round(sum((r.yunj - r.yunjs)\n" + 
			"* f.jingz)/sum(decode(nvl(r.yunj-r.yunjs,0),0,0,f.jingz)),2)) yj,\n" + 
			"decode(sum(decode(nvl(r.jiaohqzf + r.zaf + r.daozzf + r.qitfy,0),0,0,f.jingz)),0,0,round(sum((r.jiaohqzf + r.zaf + r.daozzf + r.qitfy)\n" + 
			"* f.jingz)/sum(decode(nvl(r.jiaohqzf + r.zaf + r.daozzf + r.qitfy,0),0,0,f.jingz)),2)) zf,\n" + 
			"round(sum((r.meij - r.meijs + r.yunj - r.yunjs + r.jiaohqzf + r.zaf + r.daozzf + r.qitfy)* f.jingz),2) je,\n" + 
			"decode(sum(decode(z.qnet_ar,0,0,f.jingz)),0,0,round_new(sum(z.qnet_ar*f.jingz)/sum(decode(z.qnet_ar,0,0,f.jingz)),2)) qnetar\n" + 
			"from fahb f, zhilb z, ruccb r, vwdianc d, meikxxb m, gongysb g, pinzb p\n" + 
			"where f.zhilb_id = z.id(+) and f.ruccbb_id = r.id(+)\n" + 
			"and f.pinzb_id = p.id\n" + 
			"and f.jiesb_id <= 0 and f.diancxxb_id = d.id\n" + 
			"and f.meikxxb_id = m.id and f.gongysb_id = g.id\n" + 
			"and f.daohrq >= "+DateUtil.FormatOracleDate(getRiq())+"\n" +
			"and f.daohrq <= "+DateUtil.FormatOracleDate(getAfter())+"\n" + 
			sql2 + "\n" +
			"group by rollup("+talbe+".mingc,p.mingc) having not grouping(p.mingc) + grouping("+talbe+".mingc) = 1\n" + 
			"order by grouping("+talbe+".mingc) desc,max("+talbe+".xuh),"+talbe+".mingc, grouping(p.mingc), max(p.xuh), p.mingc)";

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
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
		df1.setValue(getRiq());
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
		tb1.addText(new ToolbarText("类别:"));
		ComboBox changbcb = new ComboBox();
		changbcb.setTransform("LeibSelect");
		changbcb.setWidth(130);
		tb1.addField(changbcb);
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
		if(getRiq()==null || getAfter() == null){
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
		String colname=new String();
		if(getLeibValue().getId()==0){
			colname="煤矿";
		}else{
			colname="供应商";
		}
		String[][] ArrHeader;
		int[] ArrWidth;
		String[] strFormat;
		
		ArrHeader = new String[2][9];
		ArrHeader[0]=new String[]{colname,"品种","数量（吨）","数量（吨）"
				 ,"价格（元/吨）" ,"价格（元/吨）" ,"价格（元/吨）" ,"价格（元/吨）" 
				 ,"金额<br>（元）" ,"发热量<br>（Mj/Kg）" ,"标煤单价<br>（元/吨）"};
		ArrHeader[1]=new String[]{colname,"品种","净重","票重"
				 ,"总计" ,"煤款" ,"运费" ,"杂费" 
				 ,"金额<br>（元）" ,"发热量<br>（Mj/Kg）" ,"标煤单价<br>（元/吨）"};

		ArrWidth = new int[] { 80, 60, 60, 60, 60, 60, 60, 60, 80, 60, 60};

		strFormat = new String[] {
				"", "", "0", "0" 
				,"0.00", "0.00", "0.00", "0.00"
				,"0.00", "0.00", "0.00"};
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("成本的未结算预估表", ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(),
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
		rt.createDefautlFooter(ArrWidth);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 3, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(10, 3, "制表：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
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
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			setLeibValue(null);
			setLeibModel(null);
			getDiancmcModels();
		}
		
		//begin方法里进行初始化设置
		visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
		//	visit.setString1(null);保存传递的非默认纸张的样式
			
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
			getSelectData();
		}
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
	
//gongysb  meikxxb
//	下拉框
	public IDropDownBean getLeibValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getLeibModel().getOptionCount()>0) {
				setLeibValue((IDropDownBean)getLeibModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setLeibValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getLeibModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setLeibModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setLeibModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setLeibModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select 0 id, '煤矿' mingc from dual union select 1 id,'供应商' mingc from dual");
		setLeibModel(new IDropDownModel(sb.toString()));
	}
	
//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(_value);
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