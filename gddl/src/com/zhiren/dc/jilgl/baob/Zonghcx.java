package com.zhiren.dc.jilgl.baob;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * 
 * @author yuss
 * time:2012-3-5
 * describe:根据大开热电厂提供的报表格式开发综合查询报表
 *
 */
public class Zonghcx extends BasePage {
	
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
	
	// 绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
	// 绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

	// 页面变化记录
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
	
	// 设置制表人默认当前用户
	private String getZhibr() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhibr = "";
		String zhi = "否";
		String sql = "select zhi from xitxxb where mingc = '月报管理制表人是否默认当前用户' and diancxxb_id = " + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				zhi = rs.getString("zhi");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		if (zhi.equals("是")) {
			zhibr = visit.getRenymc();
		}
		return zhibr;
	}
	
	// 结算情况下拉框
	public IDropDownBean getJiesqkValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getJiesqkModel().getOptionCount() > 0) {
				setJiesqkValue((IDropDownBean) getJiesqkModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setJiesqkValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getJiesqkModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setJiesqkModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setJiesqkModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setJiesqkModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1, "全部"));
		String sql =
					"select 1 id, nvl('未结算', '') from dual\n" + 
					" union\n" + 
					"select 2 id, nvl('已结算', '') from dual";
		setJiesqkModel(new IDropDownModel(list, sql));
	}
	
	// 化验情况
	public IDropDownBean getHuayqkValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getHuayqkModel().getOptionCount() > 0) {
				setHuayqkValue((IDropDownBean) getHuayqkModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setHuayqkValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getHuayqkModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setHuayqkModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setHuayqkModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void setHuayqkModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1, "全部"));
		String sql =
					"select 1 id, nvl('未化验', '') from dual\n" + 
					" union\n" + 
					"select 2 id, nvl('已化验', '') from dual";
		setHuayqkModel(new IDropDownModel(list, sql));
	}
	
	// 获得选择的树节点的对应的电厂名称
	private String getDcMingc(String id) {
		if (id == null || "".equals(id)) {
			return "";
		}
		JDBCcon con = new JDBCcon();
		String mingc = "";
		String sql = "select mingc from diancxxb where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}

	// 获得选择的树节点的对应的供应商名称
	private String[] getGys(String id) {
		String[] gys = { "全部", "-1" };
		if (id == null || "".equals(id)) {
			return gys;
		}
		JDBCcon con = new JDBCcon();
		String sql = "select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			gys[0] = rsl.getString("mingc");
			gys[1] = rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
	// 判断电厂Tree中所选电厂时候还有子电厂
	private boolean isParentDc(String id) {
		boolean isParent = false;
		if (id == null || "".equals(id)) {
			return isParent;
		}
		JDBCcon con = new JDBCcon();
		String sql = "select mingc from diancxxb where fuid = " + id;
		if (con.getHasIt(sql)) {
			isParent = true;
		}
		con.Close();
		return isParent;
	}
    
	// 发站下拉框
	public IDropDownBean getFazSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getFazSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setFazSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setFazSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getFazSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getFazSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getFazSelectModels() {
		String sql = "";
		if (getTreeid().equals("0")) {
			sql = "select id, mingc from chezxxb order by mingc";
		} else {
			sql = "select distinct c.id, c.mingc " +
				  "  from chezxxb c, fahb fh, gongysb gys, meikxxb m " +
				  " where fh.daohrq between to_date('" + getBRiq() + "', 'yyyy-mm-dd') " +
				  "   and to_date('" + getERiq() + "', 'yyyy-mm-dd') " +
				  "   and fh.faz_id = c.id " +
				  "   and fh.gongysb_id = gys.id " +
				  "   and fh.meikxxb_id = m.id" + TreeID() + 
				  " order by mingc";
		}
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "请选择"));
	}
	
	public String TreeID() {
		return " and (m.id = " + this.getTreeid() + " or gys.id = " + this.getTreeid() + ")";
	}
    
	// 取得日期参数SQL
	private String getDateParam() {
		// 日期条件
		String rqsql = "";
		if (getBRiq() == null || "".equals(getBRiq())) {
			rqsql = " f.daohrq >= " + DateUtil.FormatOracleDate(new Date()) + "\n";
		} else {
			rqsql = " f.daohrq >= " + DateUtil.FormatOracleDate(getBRiq()) + "\n";
		}
		if (getERiq() == null || "".equals(getERiq())) {
			rqsql += " and f.daohrq <= " + DateUtil.FormatOracleDate(new Date()) + "\n";
		} else {
			rqsql += " and f.daohrq <= " + DateUtil.FormatOracleDate(getERiq()) + "\n";
		}
		return rqsql;
	}

	// 取得供应商参数SQL
	private String getGysParam() {
		// 供应商煤矿条件
		String gyssql = "";
		if ("1".equals(getGys(getTreeid())[1])) {
			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
		} else if ("0".equals(getGys(getTreeid())[1])) {
			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
		}
		return gyssql;
	}

	private String getJiesqkParam() {
		String jiesqk = "";
		if (getJiesqkValue().getId() > -1) {
			if (getJiesqkValue().getId() == 1) {// 未结算
				jiesqk = " and f.jiesb_id = 0\n";
			} else {// 已结算
				jiesqk = " and f.jiesb_id > 0\n";
			}
		}
		return jiesqk;
	}
	
	private String getHuayqkParam() {
		String huayqk = "";
		if (getHuayqkValue().getId() > -1) {
			if (getHuayqkValue().getId() == 1) {// 未化验
				huayqk = " and zls.qnet_ar is null\n";
			} else {// 已化验
				huayqk = " and zls.qnet_ar is not null\n";
			}
		}
		return huayqk;
	}
	
    private String getFazParam() {
		String faz = "";
		// 发站
		if (getFazSelectValue().getId() > -1) {
			faz = " and f.faz_id = " + getFazSelectValue().getId() + "\n";
		}
		return faz;
	}
    
	// 取得电厂参数SQL
	private String getDcParam() {
		// 电厂sql
		String dcsql = "";
		if (isParentDc(getTreeid_dc())) {
			dcsql = " and d.fuid = " + getTreeid_dc() + "\n";
		} else {
			dcsql = " and f.diancxxb_id = " + getTreeid_dc() + "\n";
		}
		return dcsql;
	}
	
	// 刷新衡单列表
	public void initToolbar() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		// 电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(80);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null, "function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));

		// 供应商树
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win, "gongysTree"
				, "" + visit.getDiancxxb_id(), "forms[0]", getTreeid(), getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(90);
		tf.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null, null, "function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("供货单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("发站:"));
		ComboBox faz = new ComboBox();
		faz.setTransform("FazSelect");
		faz.setWidth(70);
		faz.setListeners("select:function(own,rec,index){Ext.getDom('FazSelect').selectedIndex=index}");
		tb1.addField(faz);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("结算情况:"));
		ComboBox jiesqk = new ComboBox();
		jiesqk.setTransform("JiesqkSelect");
		jiesqk.setWidth(70);
		jiesqk.setListeners("select:function(own,rec,index){Ext.getDom('JiesqkSelect').selectedIndex = index}");
		tb1.addField(jiesqk);
		tb1.addText(new ToolbarText("-"));
			
		tb1.addText(new ToolbarText("化验情况:"));
		ComboBox huayqk = new ComboBox();
		huayqk.setTransform("HuayqkSelect");
		huayqk.setWidth(70);
		huayqk.setListeners("select:function(own,rec,index){Ext.getDom('HuayqkSelect').selectedIndex = index}");
		tb1.addField(huayqk);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addText(new ToolbarText("-"));
		
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
	}
	
	private String getZonghcx(){
		JDBCcon con = new JDBCcon();
		
		String sql = 
			"select decode(grouping(f.daohrq),\n" +
			"              0,\n" + 
			"              to_char(f.daohrq, 'yyyy-mm-dd'),\n" + 
			"              '合计') daohrq,\n" + 
			"       decode(grouping(g.mingc), 0, g.mingc, '合计') gys,\n" + 
			"       decode(grouping(m.mingc), 0, m.mingc, '合计') mk,\n" + 
			"       decode(grouping(cz.mingc), 0, cz.mingc, '合计') faz,\n" + 
			"       decode(grouping(c.bianm), 0, c.bianm, '合计') jincph,\n" + 
			"       decode(grouping(zm1.bianm), 0, zm1.bianm, '合计') diaodbm,\n" + 
			"       decode(grouping(zm2.bianm), 0, zm2.bianm, '合计') huaybh,\n" + 
			"       sum(f.ches) ches,\n" + 
			"       sum(f.biaoz) biaoz,\n" + 
			"       sum(f.jingz) jingz,\n" + 
			"       sum(f.koud) koud,\n" + 
			"       sum(f.yuns) yuns,\n" + 
			"       sum(f.yingd) yingd,\n" + 
			"       sum(f.yingd - f.yingk) kuid,\n" + 
			"       round_new(decode(sum(f.jingz), 0, 0, sum(zls.mt * f.jingz) / sum(f.jingz)), 2) mt,\n" + 
			"       round_new(decode(sum(f.jingz), 0, 0, sum(round_new(zls.qnet_ar * 1000 / 4.1816, 0) * f.jingz) / sum(f.jingz)), 0) rez,\n" + 
			"       round_new(decode(sum(f.jingz), 0, 0, sum(zls.vdaf * f.jingz) / sum(f.jingz)), 2) vdaf,\n" + 
			"       decode(f.jiesb_id, 0, '未结算','',null, '已结算') jsqk\n" + 
			"  from fahb f,\n" + 
			"       gongysb g,\n" + 
			"       meikxxb m,\n" + 
			"       chezxxb cz,\n" + 
			"       (select zhillsb_id, bianm from zhuanmb where zhuanmlb_id = 100661) zm1,\n" + 
			"       (select zhillsb_id, bianm from zhuanmb where zhuanmlb_id = 100663) zm2,\n" + 
			"       caiyb c,\n" + 
			"       zhillsb zls,\n" + 
			"       diancxxb d\n" + 
			" where " + getDateParam() + getGysParam() + getDcParam() + getFazParam() + getJiesqkParam()+ getHuayqkParam() + 
			"   and f.gongysb_id = g.id\n" + 
			"   and f.meikxxb_id = m.id\n" + 
			"   and f.faz_id = cz.id\n" + 
			"   and f.diancxxb_id = d.id\n" + 
			"   and f.zhilb_id = c.zhilb_id\n" + 
			"   and f.zhilb_id = zls.zhilb_id\n" + 
//			"   and zls.shenhzt = 7\n" + 
			"   and zls.id = zm1.zhillsb_id\n" + 
			"   and zls.id = zm2.zhillsb_id\n" + 
			"   and zm1.zhillsb_id = zm2.zhillsb_id\n" + 
			" group by rollup(f.daohrq,\n" + 
			"                 (g.mingc, m.mingc, cz.mingc, c.bianm, zm1.bianm, zm2.bianm,\n" + 
			"                  f.jiesb_id))\n" + 
			"having not(grouping(g.mingc) + grouping(f.daohrq) = 1)\n" + 
			" order by f.daohrq, g.mingc, m.mingc, cz.mingc";
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		int[] ArrWidth = null;
		int aw = 0;
        rs = rstmp;
        ArrHeader = new String[][] {{"到货日期", "供货单位", "煤矿单位", "发站", "进厂批号", "调度编码", "化验编号", "车数", 
        					"票重", "净重", "扣吨", "运损", "盈吨", "亏吨", "全水分", "热值", "挥发分", "是否结算"}};
        	    
        ArrWidth = new int[] {80, 100, 100, 60, 70, 70, 70, 50, 80, 80, 50, 50, 50, 50, 50, 50, 50, 60};
    	aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
    	rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
    	rt.setTitle("综合查询", ArrWidth);
    	
		rt.title.fontSize = 10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 3, "制表单位：" + ((Visit) this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 5, getBRiq() + " 至 " + getERiq(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 4, "单位：吨、车", Table.ALIGN_RIGHT);
		
		rt.setBody(new Table(rs, 1, 0, 7));
		for (int i = 1; i <= 18; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(28);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		for (int i = 2; i < rt.body.getRows(); i++) {
			rt.body.merge(i, 2, i, 4);
		}
		rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 7);
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 2, "制表：" + getZhibr(), Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
     	return rt.getAllPagesHtml();
	}
	
	public String getPrintTable(){
		return getZonghcx();
	}
	
	// 工具栏使用的方法
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = "0";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("gongysTree_text"))
						.setValue(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	//	-------------------------电厂Tree-----------------------------------------------------------------
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

	DefaultTree dc;

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
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			// begin方法里进行初始化设置
			visit.setString1(null);
			
			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {
				visit.setString1(pagewith);
			}
			// visit.setString1(null);保存传递的非默认纸张的样式
			setFazSelectValue(null);
			setFazSelectModel(null);
			setJiesqkValue(null);
			setJiesqkModel(null);
			setHuayqkValue(null);
			setHuayqkModel(null);
			setTreeid_dc(visit.getDiancxxb_id() + "");

			initToolbar();
		}
	}
	
	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;

		}
		initToolbar();
	}
	
	// 页面登陆验证
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