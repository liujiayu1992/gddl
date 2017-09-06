package com.zhiren.dc.caiygl;
/*
 * 作者：罗朱平
 * 时间：2009-11-27
 * 描述：在采样信息维护页面添加排序。
 */
/*
 * 作者：王磊
 * 时间：2009-07-28
 * 描述：增加采样信息维护时采样时间的默认值,增加删除时的提示
 */
/**
 * @author 张琦
 * @version 2009318.1
 */
/*
 * 2009-05-08
 * 王磊
 * 修改页面加载时清空msg 客户提示信息
 * 现有版本:1.1.2.8
 */

/*
 * 2009-05-15
 * 车必达
 * 增加删除采样编码功能。
 * 现有版本:1.1.2.10
 */
/*
 * 作者：王磊
 * 时间：2009-05-26 14:59
 * 描述：修改多行记录时保存出错的问题。
 * 版本：
 */
/*
 * 作者：王刚
 * 时间：2009-08-11 
 * 描述：增加删除采样编码功能的设置，避免有些电厂误删除采样编码。
 * 版本：
 */

/*
 * 修改人: 王伟
 * 时间：  2010-05-24
 * 内容 ： 将Page名称存放在Visit.String11()中
 * 		  在Caiyangry.java取得要返回该页面的Page名称
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

public class Cysc extends BasePage implements PageValidateListener {
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
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		// 更新样品单号、采样表
		while (rsl.next()) {
			String id = rsl.getString("id");
			sb.delete(0, sb.length());
			sb.append("update yangpdhb\n");
			sb.append("set caiysj=to_date('").append(rsl.getString("caiysj"))
					.append("','yyyy-mm-dd')");
			sb.append(",bianh='").append(rsl.getString("bianh")).append("'");
			sb.append(",kaissj='").append(rsl.getString("kaissj")).append("'");
			sb.append(",jiessj='").append(rsl.getString("jiessj")).append("'");
			sb.append(",beiz='").append(rsl.getString("beiz")).append("'");
			sb.append(" where id=").append(rsl.getString("id")).append("\n");
			flag = con.getUpdate(sb.toString());
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Caiyxxwh, "yangpdhb", id);
			sb1.delete(0, sb.length());
			sb1.append("update caiyb \n");
			sb1.append("set caiyrq=to_date('").append(rsl.getString("caiysj"))
					.append("','yyyy-mm-dd')");
			sb1.append(" where id in (select caiyb_id from yangpdhb where id=")
					.append(rsl.getString("id")).append(")");
			flag1 = con.getUpdate(sb1.toString());
			// MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
			// SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Caiyxxwh,
			// "caiyb",id);
			if (flag == -1 || flag1 == -1) {
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:"
						+ sb.toString());
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

	// 删除采样编码的方法
	private void shanc() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save3(getChange(), visit);
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
		if (_ShedsqlChick) {
			_ShedsqlChick = false;
			Update(cycle);
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}

		if (_Delete) {
			_Delete = false;

			shanc();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();

		String sqlysfs = "";
		if (getYunsfsValue().getId() != -1) {
			sqlysfs = " and f.yunsfsb_id =" + getYunsfsValue().getId();
		}
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi1());
		String caiyrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql;
		sql = "select j.id,j.bianm bianh,j.caiysj,decode(j.kaissj,null,to_char(sysdate,'hh24:mi:ss'),j.kaissj) kaissj,"
				+ "decode(j.jiessj,null,to_char(sysdate,'hh24:mi:ss'),j.jiessj) jiessj,j.caiyry lurry,j.beiz beiz from\n"
				+ "(select c.id from fahb f, caiyb c, diancxxb d\n"
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
				+ sqlysfs
				+ "\n"
				+ "group by c.id) k,\n"
				+ "(select y.caiyb_id,y.id,z.bianm,y.caiysj,y.kaissj,y.jiessj,GetCaiyry(y.id) caiyry,y.beiz\n"
				+ "from yangpdhb y, zhuanmb z, zhuanmlb zb\n"
				+ "where y.zhilblsb_id = z.zhillsb_id and zb.id = z.zhuanmlb_id\n"
				+ "and zb.jib = 1) j\n" + "where k.id = j.caiyb_id order by caiysj,bianh";
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yangpdhb");
		// /设置显示列名称
		egu.getColumn("id").setHidden(true);
		egu.getColumn("bianh").setHeader("编号");
		egu.getColumn("bianh").setEditor(null);
		egu.getColumn("caiysj").setHeader("采样日期");
		egu.getColumn("caiysj").setWidth(80);
		egu.getColumn("kaissj").setHeader("开始时间");
		egu.getColumn("jiessj").setHeader("结束时间");
		egu.getColumn("lurry").setHeader("采样人员");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(200);
		egu.getColumn("lurry").setWidth(140);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("lurry").setReturnId(false);

		// 工具
		egu.addTbarText("采样日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi1());
		df.setReadOnly(true);
		df.Binding("RIQI1", "");// 与html页中的id绑定,并自动刷新
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
		// 设置运输方式下拉框
		egu.addTbarText("运输方式选择:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(80);
		comb1.setTransform("Yunsfs");
		comb1
				.setListeners("select:function(own,rec,index){ Ext.getDom('Yunsfs').selectedIndex=index;}");
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		// 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		GridButton gb = new GridButton(
				"选择采样人员",
				"function(){"
						+ "if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){"
						+ "Ext.MessageBox.alert('提示信息','请选中一条采样记录');"
						+ "return;}"
						+ "grid1_rcd = gridDiv_sm.getSelections()[0];"
						+ "var fun=gridDiv_grid.on('afteredit',function(e){"
						+ "Ext.MessageBox.alert('提示信息','在选择采样人员之前请先保存');"
						+ "return;});" + "if(grid1_rcd.get('ID') == '0'){"
						+ "Ext.MessageBox.alert('提示信息','在选择采样人员之前请先保存!');"
						+ "return;}" + "grid1_history = grid1_rcd.get('ID');"
						+ "var Cobj = document.getElementById('CHANGE');"
						+ "Cobj.value = grid1_history;"
						+ "document.getElementById('ShedsqlButton').click();"
						+ "}");

		gb.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(gb);
		// 删除按钮
		GridButton dbtn = new GridButton("删除", getfunction("DeleteButton"));
		dbtn.setIcon(SysConstant.Btn_Icon_Delete);
		if (MainGlobal.getXitxx_item("采样", "是否可删除采样编码",
				"" + ((Visit) getPage().getVisit()).getDiancxxb_id(), "否")
				.equals("是")) {
			egu.addTbarBtn(dbtn);
		}
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
				+ "Ext.MessageBox.alert('提示信息','请选中一条采样记录');"
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

	/* 运输方式 */
	public IDropDownBean getYunsfsValue() {
		IDropDownBean vi = ((Visit) this.getPage().getVisit())
				.getDropDownBean4();
		if (vi == null) {
			if (getYunsfsModel().getOptionCount() > 0) {
				setYunsfsValue((IDropDownBean) getYunsfsModel().getOption(0));
			}

		}

		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setYunsfsValue(IDropDownBean value) {
		// ((Visit) this.getPage().getVisit()).setLong1(2);

		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setYunsfsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setYunsfsModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setYunsfsModels() {
		String sb;
		// 只取发货表中存在的运输方式
		// sb= "select distinct yun.id id,yun.mingc mingc from\n" +
		// " ( select f.yunsfsb_id\n" +
		// " from caiyb c, fahb f, yangpdhb y\n" +
		// " where c.zhilb_id = f.zhilb_id\n" +
		// " and y.caiyb_id = c.id) k ,yunsfsb yun where yun.id=k.yunsfsb_id";
		// 取全部的运输方式
		sb = "select -1 id, '全部' mingc from yunsfsb\n" + "union\n"
				+ "select id,mingc\n" + "  from yunsfsb";

		setYunsfsModel(new IDropDownModel(sb));
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

	private void Update(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Caiyangry");
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
			setYunsfsValue(null);
			setYunsfsModel(null);
			visit.setString1(null);
			
			//ww 记录page  当选择采样人后跳转回
			visit.setString11(getPageName().toString());
		}
		getSelectData();
	}

}