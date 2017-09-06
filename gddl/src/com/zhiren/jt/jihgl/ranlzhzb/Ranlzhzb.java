package com.zhiren.jt.jihgl.ranlzhzb;

import java.util.ArrayList;
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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ranlzhzb extends BasePage implements PageValidateListener {
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sb = new StringBuffer("begin \n");
		String sql = "";
		while(mdrsl.next()){
			sql = "update quannyjb set quannyj = " + mdrsl.getDouble("quannyj") + ", beiz = '" + mdrsl.getString("beiz")
					 +"' where id = " + mdrsl.getLong("id") + ";";
			
			sb.append(sql);
		}
		sb.append("end;");
		if(sb.length() < 13) {
			con.Close();
		} else {
			con.getUpdate(sb.toString());
		}
		mdrsl.close();
		con.Close();
	}

	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
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

	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}

	// 复制上期按钮
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;
		}

		if (_DeleteClick) {
			_DeleteClick = false;
			delete();
		}

		if (_CreateClick) {
			_CreateClick = false;
			create();
		}

		if (_CopyClick) {
			_CopyClick = false;
			copysq();
		}
		getSelectData();

	}

	// 功能：生成方法
	// 逻辑：先判断全年预计表中有没有当前日期当前电厂的数据如果有，删除；否则从ITEMSORT和ITEM表
	//      中取出指标名称先存入全年预计表然后显示在页面上，维护完后再存入全年预计表
	// 参数：
	public void create() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		
		String sqlS = "select * from quannyjb where diancxxb_id = "
				+ getTreeid() + " and riq = " + CurrODate;
		String sqlD = "delete from quannyjb where diancxxb_id = "
				+ getTreeid() + " and riq = " + CurrODate;

		ResultSetList rsl = con.getResultSetList(sqlS);
		if (rsl.next()) {
			con.getDelete(sqlD);
		}

		sqlS = "select i.id, i.beiz from item i, itemsort it\n"
				+ "where i.itemsortid = it.itemsortid and it.bianm = 'RANLZHZB' \n";
		rsl = con.getResultSetList(sqlS);

		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		while (rsl.next()) {
			sqlD = "insert into quannyjb(id, riq, diancxxb_id,"
					+ " item_id, quannyj, beiz)\n"
					+ "values(getNewID(" + visit.getDiancxxb_id() + "), "
					+ CurrODate +", "+ getTreeid() + ", " + rsl.getLong("id")
					+ ", 0, '"+rsl.getString("beiz")+"');";
			sb.append(sqlD);
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

	// 功能：复制上期方法
	// 逻辑：先判断全年预计表中有没有当前日期当前电厂的数据如果有,删除；
	//      否则复制上一年的数据存入全年预计表中
	// 参数：
	public void copysq() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		int nianf = Integer.parseInt(getNianf()) - 1;
		String Nianf = nianf + "";
		
		String sqlS = "select * from quannyjb where diancxxb_id = " + getTreeid() + " and riq = " + CurrODate;
		String sqlD = "delete from quannyjb where diancxxb_id = " + getTreeid() + " and riq = " + CurrODate;

		ResultSetList rsl = con.getResultSetList(sqlS);
		if (rsl.next()) {
			con.getDelete(sqlD);
		}

		sqlS = "select * from quannyjb \n"
				+ "where diancxxb_id = " + getTreeid() + " and to_char(riq,'yyyy') = '" + Nianf + "'";
		rsl = con.getResultSetList(sqlS);

		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		while (rsl.next()) {
			sqlD = "insert into quannyjb(id, riq, diancxxb_id, item_id, quannyj, beiz)\n"
					+ "values(getNewID("
					+ visit.getDiancxxb_id()
					+ "), "
					+ CurrODate
					+ ", "
					+ getTreeid()
					+ ", "
					+ rsl.getLong("item_id")
					+ ", "
					+ rsl.getLong("quannyj")
					+ ", '"
					+ rsl.getString("beiz") + "');";
			sb.append(sqlD);
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

	// 功能：删除
	// 逻辑：根据日期、电厂删除全年预计表中数据
	// 参数：
	public void delete() {
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil
				.FormatOracleDate(getNianf() + "-01" + "-01");
		
		String sqlD = "";
		String sqlS = "select * from quannyjb where diancxxb_id = " + getTreeid() + " and riq = " + CurrODate;
		
		ResultSetList rsl = con.getResultSetList(sqlS);
		StringBuffer sb = new StringBuffer("begin \n");
		while (rsl.next()) {
			sqlD = "delete from quannyjb where diancxxb_id = " + getTreeid() + " and riq = " + CurrODate + ";";
			sb.append(sqlD);
		}
		sb.append("end;");
		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getDelete(sb.toString());
		}
		rsl.close();
		con.Close();
	}

	// 功能：在页面上显示数据
	// 逻辑：从全年预计表、item、itemsort表中读取当前日期当前电厂数据并显示到页面上
	// 参数：
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql = "select yj.id, i.mingc, yj.quannyj, yj.beiz from item i, quannyjb yj where i.id = yj.item_id \n"
				+ " and yj.diancxxb_id = " + getTreeid() + " and to_char(yj.riq,'yyyy') = '" + getNianf() + "'";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("quannyjb");
		egu.setWidth("bodyWidth");

		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("mingc").setHeader("指标名称");
		egu.getColumn("mingc").editor = null;
		egu.getColumn("quannyj").setHeader("全年预计");
		egu.getColumn("beiz").setHeader("备注");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		// 年份ComBox
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setEditable(false);
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");

		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		// 设定工具栏下拉框自动刷新
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");

		// 刷新按钮
		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);

		// 生成按钮
		GridButton CreateButton = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		CreateButton.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(CreateButton);

		GridButton DeleteButton = new GridButton("删除",
				getBtnHandlerScript("DeleteButton"));
		DeleteButton.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(DeleteButton);

		egu.addToolbarButton(GridButton.ButtonType_Save_condition,
				"SaveButton", "var Mr = gridDiv_grid.getStore().getRange(); \n"
						+ "		if(Mr.length == 0){ \n"
						+ "			Ext.MessageBox.alert('提示信息','没有数据无需保存!');\n"
						+ " 		return false;" 
						+ " 	} ");
		
		// 复制上期按钮
		GridButton CopyButton = new GridButton("复制上期",
				getBtnHandlerScript("CopyButton"));
		egu.addTbarBtn(CopyButton);

		setExtGrid(egu);
		rsl.close();
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "年";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton") || btnName.endsWith("CopyButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
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

			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			getSelectData();
		}
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

	// 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

}
