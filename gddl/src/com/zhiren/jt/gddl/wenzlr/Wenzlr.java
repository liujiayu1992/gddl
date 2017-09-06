package com.zhiren.jt.gddl.wenzlr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
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
/**
 * 
 * @author Elwin
 *
 */
public class Wenzlr extends BasePage implements PageValidateListener {

	private String msg;
	public String getMsg() {
		if (msg == null) {
            msg = "";
        }
        return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	private String neir;
	public String getNeir() {
		return neir;
	}
	
	//文字录入的时间插件
	public void setNeir(String neir) {
		this.neir = neir;
	}
	
	private Date Lursj;
	public void setLursj(Date Lursj){
		this.Lursj = Lursj;
	}
	public Date getLursj(){
		if(null == Lursj){
			Lursj = new Date();
		}
		return Lursj;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
    
//  取得初始化数据
    public String getInitData() {
    	String initData = "";
    	initData = "intitle ='"+getBiaot()+"';";
    	return initData;
    }
	
    public String getBiaot() {
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setBiaot(String title) {
		((Visit)this.getPage().getVisit()).setString1(title);
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

	private boolean _QueryChick = false;
	public void QueryButton(IRequestCycle cycle) {
		_QueryChick = true;
	}

	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
	
	public void submit(IRequestCycle cycle) {
		if (_QueryChick) {
			_QueryChick = false;
			getSelectData();
		}
		if(_SaveChick) {
			_SaveChick = false;
			save();
		}
	}
	
	public void save(){
		if(null == getNeir()||("").equals(getNeir())){
			setMsg("文字描述不能为空！");
			return ;
		}
		JDBCcon con = new JDBCcon();
		int flag = 0;
		if(getSql().startsWith("update")){
			flag = con.getUpdate(getSql());
		}else if(getSql().startsWith("insert")){
			flag = con.getInsert(getSql());
		}else{
			return ;
		}
		if(flag == -1){
			setMsg("保存失败！");
		}else{
			setMsg("保存成功！");
		}
	}
	
	private String getSql(){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from wenzxxb wb where wb.diancxxb_id = "+getTreeid()+"\n" +
				"and wb.riq = to_date('"+getRiq()+"','yyyy-mm-dd')\n");
		ResultSetList rsl = con.getResultSetList(sql.toString());
		if(rsl.next()){
			sql.delete(0, sql.length());
			sql.append("update wenzxxb wb set wb.wenz = '"+getNeir()+"'\n" +
					",wb.lurry = '"+visit.getRenymc()+"'\n" +
							"where wb.diancxxb_id = "+visit.getDiancxxb_id()+"\n" +
									"and wb.riq = to_date('"+getRiq()+"','yyyy-mm-dd')");
		}else{
			sql.delete(0, sql.length());
			String CurrentDate = DateUtil.FormatOracleDate(getRiq());
			sql.append("insert into wenzxxb wb(id,riq,diancxxb_id,wenz,lursj,lurry) values(");
			sql.append(MainGlobal.getNewID(visit.getDiancxxb_id())).append("," +
					CurrentDate+",");
			sql.append(visit.getDiancxxb_id()).append(",'");
			sql.append(getNeir()).append("',sysdate,'");
			sql.append(visit.getRenymc()).append("')");
		}
		
		return sql.toString();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		String CurrentDate = DateUtil.FormatOracleDate(getRiq());
//		电厂
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("lursj");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null, "刷新",
		"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		ToolbarButton stb = new ToolbarButton(null, "保存",
		"function(){ document.Form0.SaveButton.click();}");
		stb.setIcon(SysConstant.Btn_Icon_Save);
		tb1.addItem(stb);
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select wenz from wenzxxb wb where wb.diancxxb_id = ");
		sql.append(visit.getDiancxxb_id()).append(" and wb.riq = "+ CurrentDate);
		ResultSetList rs = con.getResultSetList(sql.toString());
		rs.next();
		setNeir(rs.getString("wenz")); 
		setToolbar(tb1);             //添加工具条到页面
	}
	
//	-------------------------电厂Tree-----------------------------------------------------------------
	private String treeid;
	public String getTreeid() {
		Visit visit = (Visit) this.getPage().getVisit();
		return String.valueOf(visit.getDiancxxb_id());
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
    
    public String getGridScript() {          //页面内容的Script
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
    
//	绑定日期
//	 绑定日期
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		long diancId = ((Visit)getPage().getVisit()).getDiancxxb_id();
		String sql = "select id,mingc from diancxxb dc where dc.id = "+diancId+"\n" +
				"or dc.fuid = "+diancId;;
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	-------------------------电厂Tree END----------
	
	
//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	/*// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}*/

	// 得到电厂ID
	public String getIDropDownDiancid(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancid = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.id from diancxxb d where d.mingc='"
				+ diancmcId + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancid = rs.getString("id");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

	}

	// 得到电厂编码
	public String getIDropDownDiancbm(String diancmc) {
		if (diancmc == null || diancmc.equals("")) {
			diancmc = "1";
		}
		String IDropDownDiancid = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select bianm from diancxxb d where d.mingc='"
				+ diancmc + "'";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancid = rs.getString("bianm");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancid;

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
		super.initialize();
        msg = "";
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setTreeid(null);
			getTreeid();
		}
		getSelectData();
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
}