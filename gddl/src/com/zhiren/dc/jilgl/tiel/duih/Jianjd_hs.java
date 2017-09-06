package com.zhiren.dc.jilgl.tiel.duih;


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

public class Jianjd_hs extends BasePage {
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
	
//	衡单下拉框
	public IDropDownBean getHengdValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getHengdModel().getOptionCount()>0) {
				setHengdValue((IDropDownBean)getHengdModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setHengdValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getHengdModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setHengdModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setHengdModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
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
	
	public void setHengdModels() {
//		Visit visit = (Visit)this.getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
//		sb.append("select zhi from xitxxb where mingc='过衡单打印时间范围' and leib='数量' and diancxxb_id="+visit.getDiancxxb_id());
//		ResultSetList rsl = con.getResultSetList(sb.toString());
//		String tians = "30";
//		if(rsl.next()) {
//			tians = rsl.getString("zhi");
//		}
//		sb.delete(0, sb.length());
		String diancid = "" ;
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id ="+ getTreeid_dc();
			}
		}
		sb.append("select g.id,to_char(guohsj,'hh24:mi:ss') guohsj from guohb g,chepb c,fahb f,vwdianc d \n");
		sb.append("where c.guohb_id=g.id and c.fahb_id=f.id and f.diancxxb_id=d.id\n and to_char(guohsj,'yyyy-mm-dd') = '");
		sb.append(getRiq()).append("'");
		sb.append(diancid);
		
		setHengdModel(new IDropDownModel(sb.toString()));
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
//		if(visit.isFencb()) {
//			tb1.addText(new ToolbarText("厂别:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
//		电厂Tree
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
		
		tb1.addText(new ToolbarText("检斤时间:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		ComboBox hengdcb = new ComboBox();
		hengdcb.setTransform("HengdSelect");
		hengdcb.setWidth(130);
		hengdcb.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
		tb1.addField(hengdcb);
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
		ph.addText("<B>轨道衡称重记录</B></font></td></tr>");
		ph.addText("<tr height=10><td colspan=3></td></tr>");
		ph.addText("<tr><td align='center'><font size=3 >"
						+ date
						+"</font></td><td align='right'><font size=2 >");
		ph.addText(unit);
		ph.addText("</font></td></tr></table>");
	}
	
	/*
	 * 设置表尾
	 */
	private void setTableFooter(Paragraph ph,String jianjy) {
		ph.addText("<table valign=top align=center width='527'　border='0' cellspacing='0' cellpadding='0'>");
		ph.addText("<tr><td align='left' width=40%><font size=3>打印日期："
						+ DateUtil.FormatDate(new Date()));
		ph.addText("</font></td>");
		ph.addText("<td align='left' width=30%><font size=3>");
		ph.addText("检斤员："+jianjy+"</font></td></tr></table>");
	}
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		Document _Document = new Document();
		long hengdid = -2;
		if(getHengdValue()!=null) {
			hengdid = getHengdValue().getId();
		}
		//报表时间
		String time = (hengdid > 0) ? getRiq()+" "+getHengdValue().getValue()
				: "1990-01-01 23:00:00";
		//设置检斤员
		sb.append("select distinct zhongcjjy from chepb where guohb_id = ")
			.append(hengdid);
		ResultSetList rsl = con.getResultSetList(sb.toString()); 
		String lury = ""; 
		if (rsl.next()) {
			lury = rsl.getString(0); 
		}
		// 设置明细行SQL
		sb.setLength(0);
		

		
				sb.append( "select c.xuh,cz.mingc faz,c.cheph,c.piz, c.biaoz,c.maoz,\n");
				sb.append(" c.maoz-c.piz jingz,p.mingc pinz,m.mingc meikdw\n");
				sb.append("from chepb c, fahb f,chezxxb cz, meikxxb m, pinzb p\n");
				sb.append("where f.id = c.fahb_id\n");
				sb.append("and f.meikxxb_id = m.id\n");
				sb.append(" and f.pinzb_id = p.id\n");
				sb.append(" and f.faz_id=cz.id\n");
				sb.append("and c.guohb_id =").append(hengdid).append(" \n");
				sb.append("and f.yunsfsb_id=").append(SysConstant.YUNSFS_HUOY);
				sb.append("order by c.xuh");

		
		
		ResultSet rs = con.getResultSet(sb,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		int EndPageRow = 0;
		try {
			if (rs.next()) {
				Table tb = new Table(rs, 1, 0, 2);
				String[][] ArrHeader = new String[1][15];
				ArrHeader[0] = new String[] { "序号",  "发站","车皮号","皮重","票重", "毛重",
						"净重", "品种", "煤矿" };
				int[] ArrWidth = new int[9];
				ArrWidth = new int[] { 32, 65, 70, 50, 45, 45, 45 ,45,125};
				tb.setWidth(ArrWidth);
				tb.setHeaderData(ArrHeader);
				tb.setPageRows(50);
				//tb.merge(1,2,tb.getRows(),3,false);
				
				tb.setColFormat(4,"0.00");
				tb.setColFormat(5,"0.00");
				tb.setColFormat(6,"0.00");
				tb.setColFormat(7,"0.00");
				
				tb.setColAlign(1, Table.ALIGN_CENTER);

				tb.setColAlign(4, Table.ALIGN_CENTER);
//				数字右对齐
				tb.setColAlign(5, Table.ALIGN_RIGHT);
				tb.setColAlign(6, Table.ALIGN_RIGHT);
				tb.setColAlign(7, Table.ALIGN_RIGHT);
				tb.setColAlign(8, Table.ALIGN_CENTER);
				tb.setColAlign(9, Table.ALIGN_CENTER);
				
				//设置标题字体
				tb.setRowCells(1, Table.PER_FONTSIZE, 11);
				tb.setCells(2, 2, tb.getRows(), 3, Table.PER_ALIGN,
						Table.ALIGN_CENTER);
				tb.setCells(2, 2, tb.getRows(), 3, Table.PER_VALIGN,
						Table.VALIGN_TOP);
				tb.setRowHeight(21);
				tb.setFontSize(11);
				tb.ShowZero=true;
				EndPageRow = (tb.getRows()-1)%50+3;
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
					
					setTableHeader(ph,"过衡时间："+time,"");
					ph.addText("<center>"+tb.getHtml(iPage));
					if(iPage == tb.getPages()){
						if(EndPageRow != 0 && 51-EndPageRow>0){
							Table total = getTotalTable();
							if(total!=null){
								ph.addText("<table><tr height=10><td></td></tr></table>");
								//total.setPageRows(41-EndPageRow);
								ph.addText(total.getHtml(1));
							}
						}else{
							EndPageRow = 0;
						}
					}
					ph.addText("</center>");
					setTableFooter(ph,lury);
					ph.addText("</span>");
					_Document.addParagraph(ph);
				}
				
			
				
				
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	
		
		con.Close();
		_Document.setTableWidth(0);
		if(_Document.paragraphs.size() > 0) {
			setCurrentPage(1);
			setAllPages(_Document.paragraphs.size());
		}
		return _Document.getHtml();// ph;
	}
	
	private Table getTotalTable(){
		JDBCcon con = new JDBCcon();
		long hengdid = -1;
		if(getHengdValue()!=null) {
			hengdid = getHengdValue().getId();
		}
		Table tb1 = null;
		
		ResultSet hz;
		StringBuffer sb = new StringBuffer();
		
		
		
		
				sb.append("select decode(grouping(cz.mingc)+grouping(m.mingc),2,'合计',1,'小计',cz.mingc) faz,\n");
				sb.append("m.mingc as meik, max(p.mingc) as pinz,count(c.id) ches, sum(c.maoz) as maoz,\n");
				sb.append("sum(c.piz) as piz, sum(c.maoz-c.piz-c.zongkd) jingz, sum(c.biaoz) as biaoz\n");
				sb.append("from chepb c, fahb f,chezxxb cz, meikxxb m, pinzb p\n");
				sb.append("where f.id = c.fahb_id\n");
				sb.append("and f.meikxxb_id = m.id\n");
				sb.append(" and f.pinzb_id = p.id\n");
				sb.append(" and f.faz_id=cz.id\n");
				sb.append("and c.guohb_id =");
				sb.append(hengdid);
				sb.append("and f.yunsfsb_id=").append(SysConstant.YUNSFS_HUOY);
				sb.append("group by rollup (cz.mingc,m.mingc)\n");
				sb.append("having not grouping(m.mingc)+grouping(cz.mingc)=1");

		
		hz = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		try {
			if(hz.next()){
				tb1 = new Table(hz, 1, 0, 1);
				String ArrHeaderHZ[][] = new String[1][13];
				ArrHeaderHZ[0] = new String[] { "发站", "煤矿", "品种","车数", "毛重", "皮重",
						"净重", "票重"};
				int[] ArrWidthHz = new int[13];
				ArrWidthHz = new int[] { 60, 105, 60, 60, 60, 60, 60,60 };
				tb1.setWidth(ArrWidthHz);
				tb1.setHeaderData(ArrHeaderHZ);
				tb1.setColFormat(5,"0.00");
				tb1.setColFormat(6,"0.00");
				tb1.setColFormat(7,"0.00");
				tb1.setColFormat(8,"0.00");
				tb1.setColAlign(1, Table.ALIGN_CENTER);
				tb1.setColAlign(2, Table.ALIGN_CENTER);
				tb1.setColAlign(3, Table.ALIGN_CENTER);
				tb1.setColAlign(4, Table.ALIGN_CENTER);
				tb1.setColAlign(5, Table.ALIGN_RIGHT);
				tb1.setColAlign(6, Table.ALIGN_RIGHT);
				tb1.setColAlign(7, Table.ALIGN_RIGHT);
				tb1.setColAlign(8, Table.ALIGN_RIGHT);
			
//				设置标题字体
				tb1.setRowCells(1, Table.PER_FONTSIZE, 11);
				tb1.setFontSize(11);
				tb1.setRowHeight(21);
				tb1.ShowZero = true;
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
			setHengdValue(null);
			setHengdModel(null);
			getSelectData();
		}
		if(riqchange){
			riqchange = false;
			setHengdValue(null);
			setHengdModel(null);
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
			setHengdModels();
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
