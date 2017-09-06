package com.zhiren.gs.bjdt.chengbgl;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


public class Caighthzb  extends BasePage implements PageValidateListener{
//
//	public boolean isJitUserShow() {
//		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
//	}
//
//	public boolean isGongsUser() {
//		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
//	}
//
//	public boolean isDiancUser() {
//		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
//	}
//	
	private boolean blnIsBegin = false;
	

	
//	 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
//	public int getDiancTreeJib() {
//		JDBCcon con = new JDBCcon();
//		int jib = -1;
//		String DiancTreeJib = this.getTreeid();
//		//System.out.println("jib:" + DiancTreeJib);
//		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
//			DiancTreeJib = "0";
//		}
//		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
//		ResultSet rs = con.getResultSet(sqlJib.toString());
//
//		try {
//			while (rs.next()) {
//				jib = rs.getInt("jib");
//			}
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}finally{
//			con.Close();
//		}
//
//		return jib;
//	}

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		
		String leib="";//类别:1:重点订货 3：区域订货
		String table="";
		
			return getQibb(leib,table);
		
		
		
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
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
	
	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

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
//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			this.setNianfValue(null);
			this.getNianfModels();
			//this.setTreeid(null);
		}
		isBegin=true;
		getToolBars();

		blnIsBegin = true;
		
        }
		
	

	private boolean isBegin=false;

	/**
	 * 发电集团电煤信息日报表
	 * @author xzy
	 * @param leib 参数
	 */
	
	private String getQibb(String leib,String table) {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;

		//得到行数据：电厂
		StringBuffer strRow = new StringBuffer();
		StringBuffer strCol = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		
		String sqrCol ="";
		String sbsql = "";
		String sqlRow= "";
		
		String danwmc="";
		String where="";
		
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
//		int jib=getDiancTreeJib();
//		if(jib==1){//集团
//			danwmc="vdc.fgsmc";
//			where="";
//		}else if(jib==2){//分公司或燃料公司
//			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
//
//			try{
//				ResultSetList rl = cn.getResultSetList(ranlgs);
//				if(rl.getRows()!=0){//燃料公司
//					danwmc="vdc.mingc";
//					where=" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ";
//				}else{//分公司
//					danwmc="vdc.mingc";
//					where=" and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid= "+this.getTreeid()+") ";
//				}
//				rl.close();
//
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				cn.Close();
//			}
//		}else if(jib==3){
//			danwmc="vdc.mingc";
//			where=" and dc.id=  " +this.getTreeid()+" ";
//		}
		
		sqlRow = 

			"select\n" +
			"       decode(grouping(db.mingc)+grouping(dc.mingc),\n" + 
			"          2,'合计',1,db.mingc||'小计',dc.mingc) as 电厂\n" + 
			"  from niancghtb n, diancxxb dc, dianclbb db\n" + 
			" where n.diancxxb_id = dc.id\n" + 
			"   and (dc.fuid = 1 or dc.shangjgsid = 1)\n" + 
			"   and to_char(n.riq, 'yyyy') = "+intyear+"\n" + 
			"   and dc.dianclbb_id = db.id\n" + 
			" group by rollup(db.mingc, dc.mingc)\n" + 
			" order by grouping(db.mingc) desc,db.mingc,\n" + 
			"          grouping(dc.mingc) desc,dc.mingc";

//		得到列数据：供应商
		
		sqrCol = 

			"select decode(grouping(j.mingc)+grouping(g.mingc),\n" +
			"       2,'合计',1,j.mingc||'小计',g.mingc) as 供应商\n" + 
			"  from niancghtb n, diancxxb dc, gongysb g, jihkjb j\n" + 
			" where n.diancxxb_id = dc.id\n" + 
			"   and n.gongysb_id = g.id\n" + 
			"   and n.jihkjb_id = j.id\n" + 
			"   and (dc.fuid = 1 or dc.shangjgsid = 1)\n" + 
			"   and to_char(n.riq, 'yyyy') = "+intyear+"\n" + 
			"   group by rollup(j.mingc, g.mingc)\n" + 
			" order by grouping(j.mingc) desc,j.mingc, grouping(g.mingc) desc, g.mingc";

		
//		 得到全部数据 棋盘表
		
		sbsql = 

			"select grouping(dc.mingc) as rowjb,\n" +
			"       grouping(g.mingc) as coljb,\n" + 
			"       decode(grouping(db.mingc)+grouping(dc.mingc),\n" + 
			"          2,'合计',1,db.mingc||'小计',dc.mingc) as 电厂,\n" + 
			"       decode(grouping(j.mingc)+grouping(g.mingc),\n" + 
			"       2,'合计',1,j.mingc||'小计',g.mingc) as 供应商,\n" + 
			"       sum(n.dinghl) as dinghl\n" + 
			"  from niancghtb n,\n" + 
			"       diancxxb  dc,\n" + 
			"       gongysb   g,\n" + 
			"       dianclbb  db,\n" + 
			"       jihkjb    j\n" + 
			" where n.diancxxb_id = dc.id\n" + 
			"   and dc.dianclbb_id = db.id\n" + 
			"   and n.jihkjb_id = j.id\n" + 
			"   and n.gongysb_id = g.id\n" + 
			"   and (dc.fuid = 1 or dc.shangjgsid = 1)\n" + 
			"   and to_char(n.riq, 'yyyy') = "+intyear+"\n" + 
			"   group by cube(db.mingc,dc.mingc, j.mingc, g.mingc)";

		sb.append(sbsql);
		
		try{
			ResultSetList rl = cn.getResultSetList(sbsql);
			
			if(!rl.next()){
				return "无数据！";
			}
			
			rl.close();

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		
		strRow.append(sqlRow);
		strCol.append(sqrCol);
		
		
		
//		System.out.println(sb);
		
		cd.setRowNames("电厂");
		cd.setColNames("供应商");
		cd.setDataNames("dinghl");
		cd.setDataOnRow(false);
		cd.setRowToCol(true);
		cd.setData(strRow.toString(), strCol.toString(), sb.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		int cols = ArrWidth.length;
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 80;
		}
		ArrWidth[0] = 80;
		ArrWidth[1] = 80;
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;
		rt.setTitle(intyear+"年采购合同分配估算"+table, ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.body.setCellValue(1, 0, "日期");
//		rt.body.setCellValue(1, 1, "日期");
//		for (int j = 1; j < cols + 1; j++) {
//			if (j == 2) {
//				rt.body.setColAlign(j, Table.ALIGN_CENTER);
//			}
//			if (j > 2) {
//				rt.body.setColAlign(j, Table.ALIGN_RIGHT);
//			}
//		}
		rt.setDefaultTitle(1, 2, "单位:万吨",Table.ALIGN_LEFT);
		// 设置页数
		int rows= rt.body.getRows();
		for (int i=2;i<rows;i++){
			String mingc=rt.body.getCellValue(i, 1);
			String mingc2=mingc.substring(mingc.length()-2, mingc.length());
			if (mingc2.equals("小计")){
				for (int j=1;j<cols+1;j++){
//					rt.body.getCell(i, j).setBg_Color("#B6C6D8");
					rt.body.getCell(i, j).backColor=rt.body.getCell(i, j).getBg_Color();
				}
			}else{
				continue;
			}
		} 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}
//	******************************************************************************
	//年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
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
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NIANF");
		cb1.setWidth(60);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
//
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);

		//tb1.addText(new ToolbarText("单位:"));
		//tb1.addField(tf);
//		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("至:"));
//		DateField df1 = new DateField();
//		df1.setReadOnly(true);
//		df1.setValue(this.getEndriqDateSelect());
//		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
//		df1.setId("after");
//		tb1.addField(df1);
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.getElementById('RefurbishButton').click();}");
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

//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
//	public String getTreeDiancmc(String diancmcId) {
//		if(diancmcId==null||diancmcId.equals("")){
//			diancmcId="1";
//		}
//		String IDropDownDiancmc = "";
//		JDBCcon cn = new JDBCcon();
//
//		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
//		ResultSet rs = cn.getResultSet(sql_diancmc);
//		try {
//			while (rs.next()) {
//				IDropDownDiancmc = rs.getString("mingc");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			cn.Close();
//		}
//		return IDropDownDiancmc;
//	}
	
//	private String treeid;
//	public String getTreeid() {
//		String treeid=((Visit) getPage().getVisit()).getString2();
//		if(treeid==null||treeid.equals("")){
//			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString2();
//	}
//	public void setTreeid(String treeid) {
//		((Visit) getPage().getVisit()).setString2(treeid);
//	}
//
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//
//	public String getTreeScript() {
//		return getTree().getWindowTreeScript();
//	}

//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}

}