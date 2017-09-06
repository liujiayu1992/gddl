package com.zhiren.dc.yuansfx;

/*
 * 作者：张立东
 * 时间：2009-08-15
 * 描述：增加原始记录Had的维护
 */
/*
 * 作者：张立东
 * 时间：2009-08-17
 * 描述：增加原始记录 煤矿代码的维护
 * alter table caiyb add meikdm varchar2(50) default null;
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class YuansjlHad extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
	}

	// 保存数据list
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}

	boolean riqichange1 = false;

	boolean riqichange2 = false;

	private String riqi1; // 页面起始日期日期选择

	private String riqi2; // 页面结束日期选择

	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}

	public void setRiqi1(String riqi1) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi1)) {
			this.riqi1 = riqi1;
			riqichange1 = true;
		}
	}

	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(new Date());
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi1 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
			riqichange2 = true;

		}

	}

	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Fahhtwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		JDBCcon con = new JDBCcon();
		int flag = 0;
		int flag1 = 0;
		con.setAutoCommit(true);
		StringBuffer sb1 = new StringBuffer();
		// 更新样品单号、采样表
		while (rsl.next()) {
			String id = rsl.getString("id");
			sb1.delete(0, sb1.length());
			
			sb1.append("update caiyb \n");
			sb1.append("set had=").append(rsl.getDouble("had"));
			sb1.append(",meikdm='").append(rsl.getString("meikdm"));
			sb1.append("' where id =")
					.append(rsl.getString("id")).append("");
			flag1 = con.getUpdate(sb1.toString());
			// MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
			// SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Caiyxxwh,
			// "caiyb",id);
			if (flag == -1 || flag1 == -1) {
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb1.toString());
				setMsg("保存过程出现错误！保存未成功！");
				con.rollBack();
				con.Close();
				return;
			}

		}
		rsl.close();
		con.Close();
	}

	private String getZhillsb_id() {
		Visit visit = (Visit) this.getPage().getVisit();
		String zhillsb_id = "";
		String bianm = "";
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(
				getChange());
		JDBCcon cn = new JDBCcon();
		while (mdrsl.next()) {
			bianm = mdrsl.getString("BIANH");
		}
		String sql = "select zhillsb_id from zhuanmb where bianm in '" + bianm
				+ "'";
		ResultSet rs = cn.getResultSet(sql);

		try {
			while (rs.next()) {
				zhillsb_id = rs.getString("zhillsb_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return zhillsb_id;
	}

	private void Save3(String strchang, Visit visit) {

		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("begin \n");
		StringBuffer sb1 = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchang);
		// ResultSetList rs = visit.getExtGrid1().getModifyResultSet(strchang);

		while (mdrsl.next()) {

			sb.append("delete  from zhuanmb\n");
			sb.append("where zhillsb_id = ").append(getZhillsb_id()).append(
					";\n");
			sb1.append("update zhillsb  \n");
			sb1.append("set shenhzt=-1");
			sb1.append(" where id=").append(getZhillsb_id()).append(";\n");
			sb.append(" end;");
			sb1.append(" end;");

			con.getUpdate(sb.toString());
			con.getUpdate(sb1.toString());
		}
		con.Close();

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ShedsqlChick = false;

	public void ShedsqlButton(IRequestCycle cycle) {
		_ShedsqlChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}

	private boolean _Delete = false;

	public void DeleteButton(IRequestCycle cycle) {

		_Delete = true;
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
		
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();

		
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi1());
		String caiyrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql;
		sql = "select k.id,j.bianm bianh,k.had,k.meikdm from\n"
				+ "(select c.id,c.had,c.meikdm from fahb f, caiyb c, diancxxb d\n"
				+ "where f.zhilb_id = c.zhilb_id and f.diancxxb_id = d.id\n"
				+ "and (f.diancxxb_id = "
				+ getTreeid()
				+ " or d.fuid = "
				+ getTreeid()
				+ ") \n"
				+ "and c.caiyrq >= "
				+ caiyrqb
				+ "\n"
				+ "and c.caiyrq < "
				+ caiyrqe
				+ "+ 1 "
				+ "\n"
				+ "group by c.id,c.had,c.meikdm) k,\n"
				+ "(select y.caiyb_id,y.id,z.bianm from yangpdhb y, zhuanmb z, zhuanmlb zb\n"
				+ "where y.zhilblsb_id = z.zhillsb_id and zb.id = z.zhuanmlb_id\n"
				+ "and zb.jib = 1) j\n" + "where k.id = j.caiyb_id";
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yangpdhb");
		// /设置显示列名称
		egu.getColumn("id").setHidden(true);
		egu.getColumn("bianh").setHeader("编号");
		egu.getColumn("bianh").setEditor(null);
		egu.getColumn("had").setHeader("Had(%)");
		egu.getColumn("had").setWidth(80);
		egu.getColumn("meikdm").setHeader("煤矿代码");
		egu.getColumn("meikdm").setWidth(100);

		// 工具
		egu.addTbarText("采样日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi1());
		df.setReadOnly(true);
		df.Binding("RIQI1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riqi1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");

		DateField df1 = new DateField();
		df1.setValue(this.getRiqi2());
		df1.setReadOnly(true);
		df1.Binding("RIQI2", "forms[0]");
		df1.setId("riqi2");
		egu.addToolbarItem(df1.getScript());
		// /设置下拉框默认值
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		// // //设置列宽度
		// egu.getColumn("mingc").setWidth(70);
		// // //设置当前列是否编辑
		// egu.getColumn("piny").setEditor(null);
		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// 只能选中单行
		// // /是否返回下拉框的值或ID
		// egu.getColumn("leib").setReturnId(false);
		
		// 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		// 复选框
		// egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		// egu.getGridColumns().add(2, new
		// GridColumn(GridColumn.ColType_Check));
		setExtGrid(egu);
		con.Close();
	}

	public String getfunction(String binder) {
		String handler = "function(){"
				+ "if(gridDiv_sm.getSelections().length <= 0 ){"
				+ "Ext.MessageBox.alert('提示信息','请选中一条记录');"
				+ "return;}"
				+ "Ext.MessageBox.confirm('提示信息','删除操作将不能恢复，确认删除吗？',function(btn){if(btn == 'yes'){"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i<Mrcd.length; i++){\n"
				+ "grid1_history = '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<BIANH update=\"true\">' + Mrcd[i].get('BIANH').replace('<','&lt;').replace('>','&gt;')+ '</BIANH>'+ '<CAIYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('CAIYSJ'))?Mrcd[i].get('CAIYSJ'):Mrcd[i].get('CAIYSJ').dateFormat('Y-m-d'))+ '</CAIYSJ>'+ '<KAISSJ update=\"true\">' + Mrcd[i].get('KAISSJ')+ '</KAISSJ>'+ '<JIESSJ update=\"true\">' + Mrcd[i].get('JIESSJ')+ '</JIESSJ>'+'<LURRY update=\"true\">' + Mrcd[i].get('LURRY')+ '</LURRY>'+'</result>' ; \n"
				+ "}\n" + "var Cobj = document.getElementById('CHANGE');"
				+ "Cobj.value = '<result>'+grid1_history+'</result>';"
				+ "document.getElementById('" + binder + "').click();}});"
				+ "}";
		return handler;
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

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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

	// 页面判定方法
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
			visit.setLong1(0);
			setExtGrid(null);
			visit.setString1(null);
		}
		getSelectData();
	}

}