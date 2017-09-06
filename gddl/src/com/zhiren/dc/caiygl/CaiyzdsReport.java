package com.zhiren.dc.caiygl;

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

public class CaiyzdsReport extends BasePage {
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

	
//  获得选择的树节点的对应的电厂名称   
    private String getDcMingc(String id){ 
    	if(id == null || "".equals(id)){
    		return "";
    	}
		JDBCcon con=new JDBCcon();
		String mingc="";
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
    
	/*从数据库查询出来的车皮号字符串可能很长，会影响到报表宽度的正常显示，
	  给车皮号字符串加上<br>(换行符)来处理这个问题。*/
	private String getCheph(String str,int n){
		String[] arrstr = str.split(",");
		str = "";
		for(int i=1; i < arrstr.length+1 ; i++){
			if( i%n ==0){
				str += arrstr[i-1] + ",<br>"; 
			}else{
				str += arrstr[i-1] + ",";
			}
		}
		str = str.substring(0, str.length()-1);
		return str;
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
	
		return getCaiyzds();
	
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
			setBianmValue(null);
			setBianmModel(null);
			setRiqi(null);
			visit.setboolean3(false);
			getSelectData();
			setTreeid_dc(visit.getDiancxxb_id() + "");
		}
		
		if(riqichange){	
			riqichange=false;
			this.setBianmValue(null);
			this.setBianmModel(null);
		}
		
		getSelectData();
		isBegin=true;
	}

	private String getCaiyzds() {
		JDBCcon cn = new JDBCcon();
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi());
        String sbsql="";
		sbsql=	"select to_char(c.jincsj,'hh24:mi') jincsj,\n" +
					"       c.caiybm,\n" + 
					"       c.yunsfs,\n" + 
					"       c.pinz,\n" + 
					"       c.ches,\n" + 
					"       c.meilzdbcld,\n" + 
					"       c.daob,\n" + 
					"       c.caiyry,\n" + 
					"       c.zhibry,\n" + 
					"       c.caiyfs,\n" + 
					"       c.caiyff,\n" + 
					"       c.zhiyff,\n" + 
					"       c.ziygs,\n" + 
					"       c.ziyzl,\n" + 
					"       c.ziygs*c.ziyzl meiyzl,\n" + 
					"       c.cheph,\n" + 
					"       c.laimqk,\n" + 
					"       c.pingj\n" + 
					"  from caiyzdsb c,diancxxb d\n" + 
					" where c.zhilb_id = "+this.getBianmValue().getId()+"" +
					"  and c.diancxxb_id="+getTreeid_dc()+"\n"+
					"  and c.riq >=  "+caiyrqb+"\n" + 
					"  and c.riq < "+caiyrqb+"+ 1";

		ResultSetList rs = cn.getResultSetList(sbsql);

		String[][] ArrHeader = new String[8][6];

			if (rs.next()) {
				
				ArrHeader[0] = new String[] { 
						"进车时间","" + rs.getString("jincsj") + "", 
						"采样编码","" + rs.getString("caiybm") + "",
						"运输方式","" + rs.getString("yunsfs") + "" };
				
				ArrHeader[1] = new String[] { 
						"品种","" + rs.getString("pinz") + "", 
						"车数","" + rs.getString("ches") + "", 
						"来煤最大标称粒度","" + rs.getString("meilzdbcld") + "" };
				
				ArrHeader[2] = new String[] { 
						"道别","" + rs.getString("daob") + "", 
						"采样人员","" + rs.getString("caiyry") + "",
						"值班人员" ,""+ rs.getString("zhibry") + "" };
				
				ArrHeader[3] = new String[] { 
						"采样方式","" + rs.getString("caiyfs") + "", 
						"采样方法","" + rs.getString("caiyff") + "",
						"制样方法" ,""+ rs.getString("zhiyff") + "" };
				
				ArrHeader[4] = new String[] { 
						"子样个数","" + rs.getString("ziygs") + "", 
						"字样质量","" + rs.getString("ziyzl") + "",
						"煤样质量" ,""+ rs.getString("meiyzl") + "" };
				
				ArrHeader[5] = new String[] { "车号 :<br>" + getCheph(rs.getString("cheph"),10),
						   "" ,
						   "",
						   "", 
						   "",
						   "" };
				
				ArrHeader[6] = new String[] { "来煤情况:<br>"+rs.getString("laimqk"), 
						"来煤情况"+ rs.getString("laimqk") + "",
						"来煤情况"+ rs.getString("laimqk") + "",
						"来煤情况"+ rs.getString("laimqk") + "", 
						"来煤情况"+ rs.getString("laimqk") + "",
						"来煤情况"+ rs.getString("laimqk") + "" };
				
				ArrHeader[7] = new String[] { "评价" ,
						   ""+ rs.getString("pingj")+"", 
						   ""+ rs.getString("pingj")+"" ,
						   ""+ rs.getString("pingj")+"" ,
						   ""+ rs.getString("pingj")+"" , 
						   ""+ rs.getString("pingj")+"" ,
						   ""+ rs.getString("pingj")+"" };
			} else{
				return null;
		}
		Report rt = new Report();

	int[] ArrWidth = new int[] { 90, 90, 90, 140, 100, 90 };
		
		rt.setTitle("采样作业指导书", ArrWidth);

		rt.setBody(new Table(ArrHeader,0,0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		
		for(int i = 1; i <= 8; i ++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCells(1, 1, 5, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
        rt.setDefaultTitle(5, 2, "日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_RIGHT);
		rt.body.setRowHeight(1, 60);
		rt.body.setRowHeight(2, 30);
		rt.body.setRowHeight(3, 60);
		rt.body.setRowHeight(4, 45);
		rt.body.setRowHeight(5, 45);
		rt.body.setRowHeight(6, 150);
		rt.body.setRowHeight(7, 200);
		rt.body.setRowHeight(8, 30);
		
		rt.body.setCellVAlign(6, 1, Table.VALIGN_TOP);
		rt.body.setCellVAlign(7, 1, Table.VALIGN_TOP);
		rt.body.setCellAlign(6, 1, Table.ALIGN_LEFT);
		rt.body.setCellAlign(7, 1, Table.ALIGN_LEFT);
		rt.body.setCellAlign(8, 1, Table.ALIGN_CENTER);
      	
		rt.body.mergeCell(6, 1, 6, 6);
		rt.body.mergeCell(7, 1, 7, 6);
		rt.body.mergeCell(8, 2, 8, 6);
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

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
        //电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		//化验编码
		tb1.addText(new ToolbarText("采样编码:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null, "刷新",
		"function(){document.Form0.submit();}");
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

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select cy.zhilb_id,cy.caiybm from caiyzdsb cy where cy.riq="+DateUtil.FormatOracleDate(getRiqi())+"");
		
		setBianmModel(new IDropDownModel(sb.toString(), "请选择"));
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
//	电厂Tree
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
