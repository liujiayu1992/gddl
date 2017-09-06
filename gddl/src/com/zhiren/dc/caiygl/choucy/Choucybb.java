package com.zhiren.dc.caiygl.choucy;

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


/*作者:王总兵
 *日期:2010-5-20 14:23:24
 *描述:抽查样登记表
 * 
 * 
 */
public class Choucybb extends BasePage {
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
		return getSelectData();
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

    // 起始日期
	boolean briqichange = false;

	private String briqi;

	public String getBriqi() {
		if (briqi == null || briqi.equals("")) {
			briqi = DateUtil.FormatDate(new Date());
		}
		return briqi;
	}

	public void setBriqi(String riqi) {
		if (this.briqi != null && !this.briqi.equals(riqi)) {
			this.briqi = riqi;
			briqichange = true;
		}
	}
	
    // 终止日期
	boolean eriqichange = false;

	private String eriqi;

	public String getEriqi() {
		if (eriqi == null || eriqi.equals("")) {
			eriqi = DateUtil.FormatDate(new Date());
		}
		return eriqi;
	}

	public void setEriqi(String riqi) {
		if (this.eriqi != null && !this.eriqi.equals(riqi)) {
			this.eriqi = riqi;
			eriqichange = true;
		}
	}
	
    // 记录煤矿ID
	public long getMeikxxb_id() {
		return getMeikValue().getId();
	}
	
	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		
	
		
		String meiktiaojian="";
		if(getMeikxxb_id()==-1){
			meiktiaojian="";
		}else{
			meiktiaojian="and c.meikxxb_id = " + getMeikxxb_id();
		}
		
		StringBuffer buffer = new StringBuffer();		
	    buffer.append(

	    		"select to_char(c.caiysj,'yyyy-mm-dd') as caiysj,mk.mingc,\n" +
	    		"c.huaybh,c.stad,round_new(c.qnet_ar*1000/4.1816,0) as chouy_rez,\n" + 
	    		"c.ruc_liuf,c.ruc_rez,c.yuc_biz,c.yuc_rez,c.beiz2\n" + 
	    		"from  choucyb c ,meikxxb mk\n" + 
	    		"where c.meikxxb_id=mk.id\n" + 
	    		"and c.caiysj>=to_date('"+this.getBriqi()+"','yyyy-mm-dd')\n" + 
	    		"and c.caiysj<=to_date('"+this.getEriqi()+"','yyyy-mm-dd')\n" + 
	    		" "+meiktiaojian+"\n"+
	    		"order by c.caiysj,c.huaybh"
);
	    
		String ArrHeader[][] = new String[2][10];
		ArrHeader[0] = new String[] { "采样日期","煤矿", "抽查样化验结果", "抽查样化验结果", "抽查样化验结果", "当日煤质报表<br>化验结果",
				"当日煤质报表<br>化验结果", "抽查原煤","抽查原煤","备注"};
		
		ArrHeader[1] = new String[] { "采样日期","煤矿", "抽查样编号", "硫分", "发热量", "硫分",
				"发热量", "比重","预测发热量","备注"};
		

		int ArrWidth[] = new int[] { 80,120 ,80, 55, 55, 55, 55, 55,70,120};
	    ResultSet rs = con.getResultSet(buffer, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    
		Report rt = new Report();
 
		rt.setBody(new Table(rs, 2, 0, 0));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.setTitle("煤质抽查采样登记", ArrWidth);
		
		
		//rt.setDefaultTitleLeft("煤矿:"+this.getMeikValue().getValue(), 3);
		rt.setDefaultTitleRight("采样日期:"+getBriqi()+"至"+getEriqi(), 4);
		rt.body.setPageRows(30);
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for (int i = 1; i <= 11; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		ResultSetList rsl = con.getResultSetList(buffer.toString());
		/*for (int i = 0; i < rsl.getRows(); i++) {
			rt.body.setRowHeight(i+3, 40);
		}*/
//		rt.body.setRowHeight(1, 30);
//		rt.body.setRowHeight(2, 45);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(8, 3, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;	
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
				
		if (!rsl.next()) {
			rsl.close();
			con.Close();
			return null;
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
		
		tb1.addText(new ToolbarText("起始日期:"));
		DateField df1 = new DateField();
		df1.setValue(getBriqi());
		df1.Binding("BRIQI", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("BRIQI");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("终止日期:"));
		DateField df2 = new DateField();
		df2.setValue(getEriqi());
		df2.Binding("ERIQI", "Form0");
		df2.setId("ERIQI");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("煤矿:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("MEIKXXB_ID");
		meik.setWidth(120);
		meik.setListeners("select:function(){document.Form0.submit();}");
		meik.setLazyRender(true);
		meik.setEditable(false);
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
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

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

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

			setMeikModel(null);
			setMeikValue(null);
			
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
	
    // 煤矿下拉框
	public IDropDownBean getMeikValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getMeikModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getMeikModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setMeikValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setMeikModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIMeikModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIMeikModels() {
		
		String sql = "select id, mingc from meikxxb order by mingc";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"全部"));
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

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
