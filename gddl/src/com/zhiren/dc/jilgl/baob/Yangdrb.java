package com.zhiren.dc.jilgl.baob;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
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
import com.zhiren.report.Document;
import com.zhiren.report.Paragraph;
import com.zhiren.report.Table;
/*
 * 作者:王总兵
 * 日期:2010-1-21 14:13:19
 * 描述:阳城发电,汽车衡火车衡日报表
 * 
 * 
 */



public class Yangdrb extends BasePage {
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
//	日期是否变化
	private boolean riqchange = false;
//	绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq != null){
			if(!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
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
		String sql = "select id,mingc from diancxxb";
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
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	

//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	


//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

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
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	/*
	 * 设置表眉
	 */
	private void setTableHeader(Paragraph ph, String date, String unit) {
		//ph.addText("<pre>");
		ph.addText("<table align=center width='685'　border='0' cellspacing='0' cellpadding='0'>");
		ph.addText("<tr height=50><td colspan=3></td></tr>");
		ph.addText("<tr><td align='center' colspan=3><font style='font-size:20pt'>");
		ph.addText("<B>"+((Visit)this.getPage().getVisit()).getDiancmc()+"进厂煤日报表</B></font></td></tr>");
		ph.addText("<tr height=10><td colspan=3></td></tr>");
		ph.addText("<tr><td align='left'><font size=2 >制表单位:"
						+ ((Visit)this.getPage().getVisit()).getDiancqc()
						+ "</font></td><td align='center'><font size=2 >"
						+ date
						+"</font></td><td align='right'><font size=2 >");
		ph.addText(unit);
		ph.addText("</font></td></tr></table>");
	}
	
	/*
	 * 设置表尾
	 */
	private void setTableFooter(String riq,JDBCcon con,Paragraph ph,String jianjy) {
		String zongj="";
		String Axitong="";
		String Bxitong="";
		String kuc="";
		String haoy="";
		String Axit=
			"select c.zhongchh,sum(c.maoz-c.piz-c.koud) as jingz\n" +
			"from chepb c ,fahb f\n" + 
			"where c.fahb_id=f.id\n" + 
			"and f.daohrq>=First_day(to_date('"+riq+"','yyyy-mm-dd'))\n" + 
			"and f.daohrq<=to_date('"+riq+"','yyyy-mm-dd')\n" + 
			"and c.zhongchh='A'\n" + 
			"group by (c.zhongchh)";

		ResultSetList rs=con.getResultSetList(Axit);
		if(rs.next()){
			Axitong=String.valueOf(rs.getDouble("jingz"));
		}
		String Bxit=
			"select c.zhongchh,sum(c.maoz-c.piz-c.koud) as jingz\n" +
			"from chepb c ,fahb f\n" + 
			"where c.fahb_id=f.id\n" + 
			"and f.daohrq>=First_day(to_date('"+riq+"','yyyy-mm-dd'))\n" + 
			"and f.daohrq<=to_date('"+riq+"','yyyy-mm-dd')\n" + 
			"and c.zhongchh='C'\n" + 
			"group by (c.zhongchh)";
		rs=con.getResultSetList(Bxit);
		if(rs.next()){
			Bxitong=String.valueOf(rs.getDouble("jingz"));
		}
		
		String Zongji=
			"select sum(c.maoz-c.piz-c.koud) as jingz\n" +
			"from chepb c ,fahb f\n" + 
			"where c.fahb_id=f.id\n" + 
			"and f.daohrq>=First_day(to_date('"+riq+"','yyyy-mm-dd'))\n" + 
			"and f.daohrq<=to_date('"+riq+"','yyyy-mm-dd')";
		rs=con.getResultSetList(Zongji);
		if(rs.next()){
			zongj=String.valueOf(rs.getDouble("jingz"));
		}
		
		String kucHaoy="select haoyqkdr,kuc from shouhcrbb where riq=to_date('"+riq+"','yyyy-mm-dd')";
		
		rs=con.getResultSetList(kucHaoy);
		if(rs.next()){
			haoy=String.valueOf(rs.getDouble("haoyqkdr"));
			kuc=String.valueOf(rs.getDouble("kuc"));
		}
		
		rs.close();
		
		ph.addText("<table valign=top align=center width='685'　border='0' cellspacing='0' cellpadding='0'>");
		ph.addText("<tr><td align='left' width=40%><font size=2>累计进煤总量:"+zongj+"吨");
		ph.addText("</font></td><td align='left' width=30%><font size=2>");
		ph.addText("本日耗煤:"+haoy+"吨：</font></td><td align='left' width=30%><font size=2>");
		ph.addText("库存:"+kuc+"吨"+jianjy+"</font></td></tr>");
		
		ph.addText("<tr><td align='left' width=40%><font size=2>A系统累计进煤:"+Axitong+"吨 </font></td>" );
		ph.addText(	"<td align='left' width=30%><font size=2></font></td>" );
		ph.addText(	"<td align='left' width=30%><font size=2></font></td></tr>" );
		
		ph.addText("<tr><td align='left' width=40%><font size=2>B系统累计进煤:"+Bxitong+"吨 </font></td>" );
		ph.addText(	"<td align='left' width=30%><font size=2></font></td>" );
		ph.addText(	"<td align='left' width=30%><font size=2></font></td></tr>" );
		ph.addText(	"</table>");
	}
	

	
	
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		Document _Document = new Document();
		
		String riq=this.getRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		
		// 设置明细行SQL
	
		sb.setLength(0);
		
		

				sb.append("select decode(ys.mingc,null,'汇总',ys.mingc) as yunsfs,\n");
				sb.append("decode(grouping(g.mingc)+grouping(ys.mingc),2,'',1,'总计',g.mingc) as gongys,\n");
				sb.append("decode(grouping(g.mingc)+grouping(ys.mingc)+grouping(mk.mingc),3,'',2,'',1,'合计',mk.mingc) as meikmc,\n");
				sb.append("decode(grouping(g.mingc)+grouping(ys.mingc)+grouping(mk.mingc)+grouping(c.zhongchh),4,'',3,'',2,'',1,'小计', decode(c.zhongchh,'A','A系统','C','B系统','')) as fenl,\n");
				sb.append("count(*) as ches,\n");
				sb.append("decode(c.zhongchh,'A',sum(c.biaoz),sum(c.biaoz)) as biaoz,\n");
				sb.append("decode(c.zhongchh,'A',sum(c.maoz-c.piz-c.koud-c.kouz),sum(c.maoz-c.piz-c.koud-c.kouz)) as shis,\n");
				sb.append("sum(c.maoz-c.piz-c.koud-c.kouz)-sum(c.biaoz) as chaz,\n");
				sb.append("sum(c.biaoz)*0.01 as lus,\n");
				sb.append("sum(c.biaoz)*0.01+sum(c.maoz-c.piz-c.koud-c.kouz)-sum(c.biaoz) as yingk\n");
				sb.append("from chepb c ,fahb f,gongysb g ,meikxxb mk,yunsfsb ys\n");
				sb.append("where c.fahb_id=f.id\n"); 
				sb.append("and f.gongysb_id=g.id\n");
				sb.append("and f.meikxxb_id=mk.id\n"); 
				sb.append("and f.yunsfsb_id=ys.id\n");
				sb.append("and f.daohrq=to_date('"+riq+"','yyyy-mm-dd')\n");
				sb.append("group by rollup(ys.mingc,g.mingc,mk.mingc,c.zhongchh)\n");
				sb.append("order by ys.mingc,g.mingc,mk.mingc,c.zhongchh");

		ResultSet rs = con.getResultSet(sb,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		int EndPageRow = 0;
		try {
			if (rs.next()) {
				Table tb = new Table(rs, 1, 0, 3);
				String[][] ArrHeader = new String[1][10];
				ArrHeader[0] = new String[] { "运输方式","供应商","煤矿","分类","车数","票重","实收数","差值","路损","盈亏" };
				int[] ArrWidth = new int[10];
				ArrWidth = new int[] { 60, 100, 100, 50, 50, 70, 70, 70, 70, 70 };
				tb.setWidth(ArrWidth);
				tb.setHeaderData(ArrHeader);
				tb.setPageRows(999);
				//tb.merge(2,1,tb.getRows(),3,false);
				tb.mergeFixedCols();
				tb.setColFormat(6,"0.00");
				tb.setColFormat(7,"0.00");
				tb.setColFormat(8,"0.00");
				tb.setColFormat(9,"0.00");
				tb.setColFormat(10,"0.00");
				tb.setColAlign(1, Table.ALIGN_CENTER);
				tb.setColAlign(2, Table.ALIGN_CENTER);
				tb.setColAlign(3, Table.ALIGN_CENTER);
				tb.setColAlign(4, Table.ALIGN_CENTER);

				tb.setColAlign(5, Table.ALIGN_CENTER);
//				数字右对齐
				tb.setColAlign(6, Table.ALIGN_RIGHT);
				tb.setColAlign(7, Table.ALIGN_RIGHT);
				tb.setColAlign(8, Table.ALIGN_RIGHT);
				tb.setColAlign(9, Table.ALIGN_RIGHT);
				tb.setColAlign(10, Table.ALIGN_RIGHT);
				
				//设置标题字体
				tb.setRowCells(1, Table.PER_FONTSIZE, 10);
				tb.setCells(2, 2, tb.getRows(), 3, Table.PER_ALIGN,
						Table.ALIGN_CENTER);
				/*tb.setCells(2, 2, tb.getRows(), 3, Table.PER_VALIGN,
						Table.VALIGN_CENTER);*/
				tb.setRowHeight(21);
				EndPageRow = (tb.getRows()-1)%999+3;
				setAllPages(tb.getPages());
				for(int iPage=1; iPage <= tb.getPages(); iPage++){
					String display = "";
					String feny = "";
					if(iPage != 1){
						display = "style='display:none'";
						feny = "<span style=\"display:none\"><p style='page-break-after: always'>&nbsp;</p></span>";
					}
					Paragraph ph = new Paragraph();
					ph.addText(feny+"<span id='reportpage"+iPage+"' "+display+" >");
					
					setTableHeader(ph,"日期："+getRiq(),"单位：吨、车");
					ph.addText("<center>"+tb.getHtml(iPage));
					if(iPage == tb.getPages()){
						if(EndPageRow != 0 && 1000-EndPageRow>0){
							Table total = getTotalTable(true,1000-EndPageRow);
							if(total!=null){
								ph.addText("<table><tr height=10><td></td></tr></table>");
								total.setPageRows(1000-EndPageRow);
								ph.addText(total.getHtml(1));
							}
						}else{
							EndPageRow = 0;
						}
					}
					ph.addText("</center>");
					setTableFooter(riq,con,ph,"");
					ph.addText("</span>");
					_Document.addParagraph(ph);
				}
				
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Table total = null;
		if(EndPageRow == 0){
			total = getTotalTable(false,0);
		}else{
			total = getTotalTable(false,1000-EndPageRow);
		}
		/*if(total != null){
			total.setPageRows(999);
			for(int iPage=1; iPage <= total.getPages(); iPage++){
				Paragraph ph = new Paragraph();
				ph.addText("<span style=\"display:none\"><p style='page-break-after: always'>&nbsp;</p></span><span id='reportpage"+(getAllPages()+iPage)+"' style='display:none'>");
				setTableHeader(ph,"日期："+"","单位：吨、车");
				ph.addText("<center>"+total.getHtml(iPage)+"</center>");
				setTableFooter(ph,"");
				ph.addText("</span>");
				_Document.addParagraph(ph);
			}
		}*/
		con.Close();
		_Document.setTableWidth(0);
		if(_Document.paragraphs.size() > 0) {
			setCurrentPage(1);
			setAllPages(_Document.paragraphs.size());
		}
		return _Document.getHtml();// ph;
	}
	
	private Table getTotalTable(boolean less,int rownum){
		JDBCcon con = new JDBCcon();
		
		Table tb1 = null;
		String riq=this.getRiq();
		if(riq==null||riq.equals("")){
			riq=DateUtil.FormatDate(new Date());
		}
		ResultSet hz;
		StringBuffer sb = new StringBuffer();
				sb.append("select decode(ys.mingc,null,'汇总',ys.mingc) as yunsfs,\n");
				sb.append("decode(grouping(g.mingc)+grouping(ys.mingc),2,'',1,'总计',g.mingc) as gongys,\n");
				sb.append("decode(grouping(g.mingc)+grouping(ys.mingc)+grouping(mk.mingc),3,'',2,'',1,'合计',mk.mingc) as meikmc,\n");
				sb.append("'本月累计',\n");
				sb.append("count(*) as ches,sum(c.maoz) as maoz,sum(c.piz) as piz,sum(c.koud) as koud,\n");
				sb.append("sum(c.maoz-c.piz-c.koud) as jingz,sum(c.biaoz) as biaoz\n");
				sb.append("from chepb c ,fahb f,gongysb g ,meikxxb mk,yunsfsb ys\n");
				sb.append("where c.fahb_id=f.id\n");
				sb.append("and f.gongysb_id=g.id\n");
				sb.append("and f.meikxxb_id=mk.id\n");
				sb.append("and f.yunsfsb_id=ys.id\n");
				sb.append("and f.daohrq>=First_day(to_date('"+riq+"','yyyy-mm-dd'))\n");
				sb.append("and f.daohrq<=to_date('"+riq+"','yyyy-mm-dd')\n");
				sb.append("group by rollup(ys.mingc,g.mingc,mk.mingc)\n");
				sb.append("order by ys.mingc,g.mingc,mk.mingc");

		
		hz = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		try {
			if(hz.next()){
				tb1 = new Table(hz, 1, 0, 3);
				String ArrHeaderHZ[][] = new String[1][10];
				ArrHeaderHZ[0] = new String[] { "运输方式","供应商","煤矿","累计","车数","毛重","皮重","扣吨","净重","票重" };
				int[] ArrWidthHz = new int[13];
				ArrWidthHz = new int[] { 60, 100, 100, 50, 50, 70, 70, 70, 70, 70 };
				tb1.setWidth(ArrWidthHz);
				tb1.setHeaderData(ArrHeaderHZ);
				tb1.mergeFixedCols();
				tb1.setColFormat(6,"0.00");
				tb1.setColFormat(7,"0.00");
				tb1.setColFormat(8,"0.00");
				tb1.setColFormat(9,"0.00");
				tb1.setColFormat(10,"0.00");
				tb1.setColAlign(1, Table.ALIGN_CENTER);
				tb1.setColAlign(2, Table.ALIGN_CENTER);
				tb1.setColAlign(3, Table.ALIGN_CENTER);
				tb1.setColAlign(4, Table.ALIGN_CENTER);
				tb1.setColAlign(5, Table.ALIGN_CENTER);
				tb1.setColAlign(6, Table.ALIGN_RIGHT);
				tb1.setColAlign(7, Table.ALIGN_RIGHT);
				tb1.setColAlign(8, Table.ALIGN_RIGHT);
				tb1.setColAlign(9, Table.ALIGN_RIGHT);
				tb1.setColAlign(10, Table.ALIGN_RIGHT);
				
//				设置标题字体
				tb1.setRowCells(1, Table.PER_FONTSIZE, 10);
				tb1.setRowHeight(21);
				tb1.ShowZero = false;
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return tb1;
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
		((DateField)getToolbar().getItem("guohrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString3(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			visit.setboolean1(false);
			visit.setString1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			
			getSelectData();
		}
		if(riqchange){
			riqchange = false;
		
			setTbmsg(null);
		}
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
	
}
