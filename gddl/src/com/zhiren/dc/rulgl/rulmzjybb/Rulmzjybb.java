package com.zhiren.dc.rulgl.rulmzjybb;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rulmzjybb extends BasePage {
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getRulmzdata();
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
	
    // 入炉日期
	boolean rlriqichange = false;

	private String rlriqi;

	public String getRlriqi() {
		if (rlriqi == null || rlriqi.equals("")) {
			rlriqi = DateUtil.FormatDate(new Date());
		}
		return rlriqi;
	}

	public void setRlriqi(String riqi) {
		if (this.rlriqi != null && !this.rlriqi.equals(riqi)) {
			this.rlriqi = riqi;
			rlriqichange = true;
		}
	}
	

	
    // 记录机组分组表ID
	public long getJizfzb_id() {
		return getJizfzValue().getId();
	}
	
	public String getSql1(int xuh, String mingc, String name, String xiangmmc) {
		String sql = "select "+xuh+" as xuh, " +
				     "       '"+mingc+"' as mingc1, '"+mingc+"' as mingc2, '"+mingc+"' as mingc3, " +
				     "       name as name1, name as name2 , name as name3, name as name4, name as name5   " +
				     "       from\n" +
				     "(select Formatxiaosws("+name+",decode("+name+",Mt,1,2)) as name, 1 as hh" +
				     "  from rulmzlb zl" +
				     " where to_char(rulrq,'yyyy-mm-dd') = '" + getRlriqi() +
				     
				     "'  and jizfzb_id = " + getJizfzb_id() +
				     "   and diancxxb_id = " + getTreeid() +
				     ") zl " +
				     " ";
		return sql;
	}
	
	public String getSql2(int xuh, String mingc, String name, String xiangmmc) {
		String sql = "select "+xuh+" as xuh, " +
	                 "       '"+mingc+"' as mingc1, '"+mingc+"' as mingc2, '"+mingc+"' as mingc3, " +
	                 "       ''||name*1000 as name1,  ''||name*1000 as name2 ,  ''||name*1000 as name3" +
	                 "     ,  ''||name*1000 as name4 ,  ''||name*1000 as name5 from\n" +
	                 "(select round_new("+name+",2) as name, 1 as hh" +
	                 "  from rulmzlb zl" +
	                 " where to_char(rulrq,'yyyy-mm-dd') = '" + getRlriqi() +
	                // "'  and to_char(fenxrq,'yyyy-mm-dd') = '" + getFxriqi() + 
	                 "'  and jizfzb_id = " + getJizfzb_id() +
	                 "   and diancxxb_id = " + getTreeid() +
	                 ") zl " +
	                 " ";
		return sql;
	}
	
	public String getSql3(int xuh, String mingc, String name, String xiangmmc) {
		String sql = "select "+xuh+" as xuh, " +
        			 "       '"+mingc+"' as mingc1, '"+mingc+"' as mingc2, '"+mingc+"' as mingc3, " +
        			 "       name*1000/4.1816 as name1, name*1000/4.1816 as name2, name*1000/4.1816 as name3 " +
        			 "       , name*1000/4.1816 as name4, name*1000/4.1816 as name5  from\n" +
        			 "(select "+name+" as name, 1 as hh" +
        			 "  from rulmzlb zl" +
        			 " where to_char(rulrq,'yyyy-mm-dd') = '" + getRlriqi() +
        			// "'  and to_char(fenxrq,'yyyy-mm-dd') = '" + getFxriqi() + 
        			 "'  and jizfzb_id = " + getJizfzb_id() +
        			 "   and diancxxb_id = " + getTreeid() +
        			 ") zl" +
        			 " ";
		return sql;
	}
	
	private String getRulmzdata() {
		JDBCcon con = new JDBCcon();
		String sql = "select mingc from jizfzb where id = " + getJizfzb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String jizmc = "";
		if (rsl.next()) {
			jizmc = rsl.getString("mingc");
		}
		
		sql = "select m.zhiyr,zl.lury,zl.shenhryyj,zl.shenhryej from meihyb m,rulmzlb zl where to_char(m.rulrq,'yyyy-mm-dd') = '" + getRlriqi() + "' " +
			  "  and m.rulmzlb_id=zl.id and m.jizfzb_id = " + getJizfzb_id() +
			  "   and m.diancxxb_id = " + getTreeid();
		rsl = con.getResultSetList(sql);
		String zhiyr = "";
		String fenxy="";
		String shenhryyj="";
		String shenhryej="";
		if (rsl.next()) {
			zhiyr = rsl.getString("zhiyr");
			fenxy= rsl.getString("lury");
			shenhryyj=rsl.getString("SHENHRYYJ");
			shenhryej=rsl.getString("SHENHRYEJ");
		}
		
		sql = "select Qnet_ar*1000 as ar1, round_new(Qnet_ar*1000/4.1816, 0) as ar2,to_char(fenxrq,'yyyy-mm-dd') as fenxrq from rulmzlb " +
		  " where to_char(rulrq,'yyyy-mm-dd') = '" + getRlriqi() + "' "+
		  " and jizfzb_id = " + getJizfzb_id() + "   and diancxxb_id = " + getTreeid();
	rsl = con.getResultSetList(sql);
	String ar1 = "";
	String ar2 = "";
	String fenxrq="";
	if (rsl.next()) {
		ar1 = rsl.getString("ar1");
		ar2 = rsl.getString("ar2");
		fenxrq=rsl.getString("fenxrq");
	}
	String cell = ar1 + "(约合" + ar2 + "卡/克)";
	if (ar1.equals("") || ar1 == null) {
		cell = "";
	}
		
		
		
		String ArrHeader[][] = new String[2][8];
		ArrHeader[0] = new String[] { "机组名称", jizmc, "HGI", "38和48的混样", "煤种", "煤种", "无烟煤", "无烟煤"};
		ArrHeader[1] = new String[] { "采样日期", getRlriqi(), "制样日期", fenxrq, "采样", "输煤", "制样", zhiyr};

		int ArrWidth[] = new int[] { 100, 100, 100, 100, 50, 50, 50, 50};
		
		
		
		
		
		String sql1 = "select 13 as xuh, " +
				      "       '收到基低位热值Qnet,ar(J/g)' as mingc1, '收到基低位热值Qnet,ar(J/g)' as mingc2, '收到基低位热值Qnet,ar(J/g)' as mingc3, " +
				      "        ''||0 as name1,  ''||0 as name2 , ''||0 as name3, ''||0 as name4, ''||0 as name5" +
				      "       from dual";
		String sql2 = "select 14 as xuh, '备注' as mingc1, '' as mingc2, '' as mingc3,  ''||0 as name1,  ''||0 as name2 , ''||0 as name3, ''||0 as name4, ''||0 as name5 from dual";
		
		StringBuffer buffer = new StringBuffer("select mingc1, mingc2, mingc3, name1, name2,name3,name4,name5 from (");		
	    buffer.append(getSql1(1, "收到基水分Mar(%)", "Mt", "收到基水分")).append("\n union \n")
			  .append(getSql1(2, "空气干燥基水分Mad(%)", "Mad", "空气干燥基水分")).append("\n union \n")
			  .append(getSql1(3, "空气干燥基灰分Aad(%)", "Aad", "空气干燥基灰分")).append("\n union \n")
			  .append(getSql1(4, "干燥基灰分Ad(%)", "Ad", "干燥基灰分")).append("\n union \n")
			  .append(getSql1(5, "空气干燥基挥发分Vad(%)", "Vad", "空气干燥基挥发分")).append("\n union \n")
			  .append(getSql1(6, "干燥无灰基挥发分Vdaf(%)", "Vdaf", "干燥无灰基挥发分")).append("\n union \n")
			  .append(getSql1(7, "固定碳FCad(%)", "FCad", "固定碳")).append("\n union \n")
			  .append(getSql1(8, "空气干燥基全硫St,ad(%)", "Stad", "空气干燥基全硫")).append("\n union \n")
			  .append(getSql1(9, "干燥基全硫St,d(%)", "Std", "干燥基全硫")).append("\n union \n")
			  .append(getSql1(10, "空气干燥基氢值Had(%)", "Had", "空气干燥基氢值")).append("\n union \n")
			  .append(getSql2(11, "弹筒发热量Qbad(J/g)", "Qbad", "弹筒发热量")).append("\n union \n")
			  .append(getSql2(12, "干燥基高位热值Qgrd(J/g)", "Qgrd", "干燥基高位热值")).append("\n union \n")
			  .append(sql1).append("\n union \n").append(sql2).append(") order by xuh");
	    
	    ResultSet rs = con.getResultSet(buffer, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    
		Report rt = new Report();
 
		rt.setBody(new Table(rs, 2, 0, 1));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		Visit visit = (Visit) getPage().getVisit();
		rt.setTitle(visit.getDiancmc()+"入炉煤质检验报表", ArrWidth);
		
		
		rt.setDefaultTitle(6,2,getRlriqi(),Table.ALIGN_CENTER);
		rt.body.setPageRows(21);
		
		rsl = con.getResultSetList(buffer.toString());
		int g = rsl.getRows();
		rt.body.setCellValue(g+1, 4, cell);
		rt.body.setCellValue(g+1, 5, cell);
		rt.body.setCellValue(g+1, 6, cell);
		rt.body.setCellValue(g+1, 7, cell);
		rt.body.setCellValue(g+1, 8, cell);
		for (int i = 2; i <=8; i++) {
			rt.body.setCellValue(g+2, i, "");
		}
		rt.body.mergeCell(1, 5, 1, 6);
		rt.body.mergeCell(1, 7, 1, 8);
		//rt.body.mergeCell(1, 5, 1, 6);
		for (int i = 3; i <= 15; i++) {
			rt.body.mergeRow(i);
		}
		
		rt.body.mergeRow(g+2);
		for (int i = 1; i <= 8; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		for (int i = 1; i < 16; i++) {
			rt.body.setRowHeight(i, 45);
		}
		rt.body.setRowHeight(g+2, 130);
		
		//rt.body.merge(g-1, 4, g-1, 8);
		
		rt.title.setFontSize(12);
		rt.body.setFontSize(12);
		rt.createDefautlFooter(ArrWidth);
		
		rt.setDefautlFooter(1, 2, "批准:"+shenhryej, Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 1, "审核:"+shenhryyj, Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 2, "分析:"+fenxy, Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		sql = "select shenhzt from rulmzlb where to_char(rulrq,'yyyy-mm-dd') = '" + getRlriqi() + "' " + 
		  " and jizfzb_id = " + getJizfzb_id() + "   and diancxxb_id = " + getTreeid();
		rsl = con.getResultSetList(sql);
		int shenhzt = 0;
		if (rsl.next()) {
			shenhzt = rsl.getInt("shenhzt");
		}
		if (shenhzt == 0) {
			rsl.close();
			con.Close();
			return this.getJizfzValue().getValue()+" 当天入炉化验数据未录入！";
		} else {
			rsl.close();
			con.Close();
			return rt.getAllPagesHtml();
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

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();
		tb1.addText(new ToolbarText("入炉日期:"));
		DateField df1 = new DateField();
		df1.setValue(getRlriqi());
		df1.Binding("RLRIQI", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("RLRIQI");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		
		
		tb1.addText(new ToolbarText("机组分组:"));
		ComboBox jizfz = new ComboBox();
		jizfz.setTransform("JIZFZB_ID");
		jizfz.setWidth(80);
		jizfz.setListeners("select:function(){document.Form0.submit();}");
		jizfz.setLazyRender(true);
		tb1.addField(jizfz);
		tb1.addText(new ToolbarText("-"));
		
		//if(visit.isFencb()){
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
							.getVisit()).getDiancxxb_id(),
					"-1".equals(getTreeid()) ? null : getTreeid());
			setTree(etu);
			TextField tf = new TextField();
			tf.setId("diancTree_text");
			tf.setWidth(120);
			tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
					.parseLong(getTreeid())));

			ToolbarButton tb2 = new ToolbarButton(null, null,
					"function(){diancTree_window.show();}");
			tb2.setIcon("ext/resources/images/list-items.gif");
			tb2.setCls("x-btn-icon");
			tb2.setMinWidth(20);

			//tb1.addText(new ToolbarText("单位:"));
		//	tb1.addField(tf);
			//tb1.addItem(tb2);
			tb1.addText(new ToolbarText("-"));
		//}
		
		
		
		
		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setRlriqi(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			
			visit.setDefaultTree(null);
			setTreeid(null);
		}		
		getToolbars();
		blnIsBegin = true;
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
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
	
    // 机组分组下拉框
	public IDropDownBean getJizfzValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getJizfzModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getJizfzModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setJizfzValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setJizfzModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getJizfzModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIJizfzModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIJizfzModels() {		
		String sql = "select id, mingc from jizfzb  order by xuh";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
    // 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
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
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql);
	}
}
