package com.zhiren.dc.hesgl.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Dancyfjs extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	protected void initialize() {
		msg = "";
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	// 绑定日期
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getRiq2() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	// 生成按钮
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}

		if (_CreateClick) {
			_CreateClick = false;
			create();
			getSelectData();
		}
	}

	// 生成
	public void create() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		
		String sql = "select * from dancyfjsb where diancxxb_id = " + getTreeid() + " and qisrq >= " +
				"to_date('" + getRiqi() + "', 'yyyy-mm-dd') and zhongzrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')";
		
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			
			sql = "delete from dancyfjsb where diancxxb_id = " + getTreeid() + " and qisrq >= " +
				"to_date('" + getRiqi() + "', 'yyyy-mm-dd') and zhongzrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')";
			
			con.getDelete(sql);
			rsl.close();
		}
		
		sql = 
			"select cp.yunsdwb_id,\n" +
			"       cp.cheph,\n" + 
			"       js.meikxxb_id,\n" + 
			"       count(cp.id) as ches,\n" + 
			"       sum((cp.maoz - cp.piz - cp.koud - cp.kous - cp.kouz)) as layl,\n" + 
			"       sum((cp.maoz - cp.piz - cp.koud - cp.kous - cp.kouz) * a.danj) as yunf\n" + 
			"  from (select dj.chepb_id,\n" + 
			"               yf.id,\n" + 
			"               round_new(decode(sum(yf.jiessl),\n" + 
			"                                0,\n" + 
			"                                0,\n" + 
			"                                sum(yf.hansyf) / sum(yf.jiessl)),\n" + 
			"                         2) as danj\n" + 
			"          from danjcpb dj, jiesyfb yf\n" + 
			"         where dj.yunfjsb_id = yf.id\n" + 
			"           and dj.yunfjsb_id != 0\n" + 
			"           and yf.diancxxb_id = " + getTreeid() + "\n" + 
			"           and yf.fahksrq >= to_date('" + getRiqi() + "', 'yyyy-mm-dd')\n" + 
			"           and yf.fahjzrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
			"         group by dj.chepb_id, yf.id) a,\n" + 
			"       chepb cp,\n" + 
			"       jiesyfb js\n" + 
			" where a.chepb_id = cp.id\n" + 
			"   and a.id = js.id\n" + 
			"   and cp.yunsdwb_id != -1\n" + 
			"   and js.yunsfsb_id = 2\n" + 
			" group by cp.yunsdwb_id, cp.cheph, js.meikxxb_id";
		
		rsl = con.getResultSetList(sql);

		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		while (rsl.next()) {
			
			sql = "insert into dancyfjsb values(getNewID(" + getTreeid() + "), "
					+ getTreeid()
					+ ", "
					+ "to_date('" + getRiqi() + "', 'yyyy-mm-dd')"
					+ ", "
					+ "to_date('" + getRiq2() + "', 'yyyy-mm-dd')"
					+ ", "
					+ rsl.getLong("yunsdwb_id")
					+ ", '"
					+ rsl.getString("cheph")
					+ "', "
					+ rsl.getLong("meikxxb_id")
					+ ", "
					+ rsl.getInt("ches")
					+ ", "
					+ rsl.getDouble("layl")
					+ ", "
					+ rsl.getDouble("yunf") 
					+ ", '"
					+ "正常量" +
					"');";
			sb.append(sql);
		}

		sb.append("end;");
		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getInsert(sb.toString());
		}
		rsl.close();
		con.Close();
	}

	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		
		String sql = 
			"select js.id,\n" +
			"       js.diancxxb_id,\n" + 
			"       js.qisrq,\n" + 
			"       js.zhongzrq,\n" + 
			"       ys.mingc as yunsdwb_id,\n" + 
			"       js.cheph,\n" + 
			"       mk.mingc as meikxxb_id,\n" + 
			"       js.ches,\n" + 
			"       js.layl,\n" + 
			"       js.yunf,\n" + 
			"       js.beiz\n" + 
			"  from dancyfjsb js, yunsdwb ys, meikxxb mk\n" + 
			" where js.diancxxb_id = " + getTreeid() + "\n" + 
			"   and js.qisrq >= to_date('" + getRiqi() + "', 'yyyy-mm-dd')\n" + 
			"   and js.zhongzrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
			"   and js.yunsdwb_id = ys.id\n" + 
			"   and js.meikxxb_id = mk.id(+)\n" + 
			" order by js.id";
		
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("dancyfjsb");
		egu.setWidth("bodyWidth");

		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("qisrq").setHidden(true);
		egu.getColumn("qisrq").setDefaultValue(getRiqi());
		egu.getColumn("zhongzrq").setHidden(true);
		egu.getColumn("zhongzrq").setDefaultValue(getRiq2());
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setEditor(new ComboBox());
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsdwb order by mingc"));
		egu.getColumn("yunsdwb_id").returnId = true;
		egu.getColumn("cheph").setHeader("车牌号");
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meikxxb order by mingc"));
		egu.getColumn("meikxxb_id").returnId = true;
		egu.getColumn("meikxxb_id").editor.allowBlank = true;
		egu.getColumn("ches").setHeader("车数(车)");
		egu.getColumn("layl").setHeader("拉运量(吨)");
		egu.getColumn("yunf").setHeader("运费(元)");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setEditor(new ComboBox());
		egu.getColumn("beiz").setComboEditor(egu.gridId,
				new IDropDownModel(
						"select id, mingc\n" +
						"  from (select 1 as id, '正常量' as mingc\n" + 
						"          from dual\n" + 
						"        union\n" + 
						"        select 2 as id, '补价' as mingc from dual)"));
		egu.getColumn("beiz").returnId = false;
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());		
		egu.addTbarText("-");

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});" +
				"YUEF.on('select',function(){document.forms[0].submit();});");

		// 刷新按钮
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		
		egu.addTbarBtn(refurbish);
		refurbish.setMinWidth(40);
		egu.addTbarText("-");

		// 生成按钮
		GridButton CreateButton = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		
		CreateButton.setIcon(SysConstant.Btn_Icon_Create);
		CreateButton.setMinWidth(40);
		egu.addTbarBtn(CreateButton);
		egu.addTbarText("-");

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");					

		setExtGrid(egu);
		rsl.close();
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖已存数据，是否继续？");
		} else {
			btnsb.append("是否删除数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTreeid(String.valueOf(visit.getDiancxxb_id()));

			setRiqi(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {			
			con.Close();
		}

		return jib;
	}
}