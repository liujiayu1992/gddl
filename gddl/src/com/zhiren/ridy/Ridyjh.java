package com.zhiren.ridy;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间：2009-5-4
 * 修改内容:增加图片查看功能
 */
/**
 * @author Administrator
 *
 */
public class Ridyjh extends BasePage implements PageValidateListener {
	
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	刷新数据日期绑定
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _RefreshChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}else if(_InsertChick){
			_InsertChick=false;
		}else if(_DeleteChick){
			_DeleteChick=false;
		}
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ridyjhb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			sSql = "delete from ridyjhb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ridyjhb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sSql = "insert into ridyjhb(ID,MEIKXXB_ID,YUNSDWB_ID,CHES,DUNS,BEIZ,YEWRQ,CREATETIME,UPDATETIME) values("
					+"XL_RIJHDYB_ID.nextval"+ ","
					+"(select id from meikxxb where quanc='" +rsl.getString("MEIKXXBNAME") + "'),"
					+"(select id from yunsdwb where quanc='" +rsl.getString("YUNSDWNAME") + "'),"
					+ rsl.getString("ches") + ","
					+ rsl.getString("duns") + ","
					+ "'"+rsl.getString("beiz") + "',"
					+"to_date('"+getRiq()+ "','yyyy-mm-dd'),"
					+"sysdate,"
					+"sysdate"
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				sSql = "update ridyjhb set "
					+ " MEIKXXB_ID=" +"(select id from meikxxb where quanc='" +rsl.getString("MEIKXXBNAME") + "')"+ ","
					+ " YUNSDWB_ID="+"(select id from yunsdwb where quanc='" +rsl.getString("YUNSDWNAME") + "')"+ ","
					+ " CHES=" + rsl.getString("CHES") + ","
					+ " DUNS=" + rsl.getString("DUNS") + ","
					+ " BEIZ='" + rsl.getString("BEIZ") +"'"
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
		
		con.commit();
		con.Close();
	}
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql ="SELECT ID ," +
				"(SELECT QUANC FROM MEIKXXB WHERE ID=MEIKXXB_ID)MEIKXXBNAME," +
				"(SELECT QUANC FROM YUNSDWB WHERE ID=YUNSDWB_ID)YUNSDWNAME," +
				"CHES," +
				"DUNS, " +
				"BEIZ " +
				"FROM " +
				"RIDYJHB " +
				"WHERE YEWRQ =TO_DATE('"+getRiq()+"','yyyy-mm-dd')";
		ResultSetList rs = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rs);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setTableName("ridyjhb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("MEIKXXBNAME").setHeader("煤矿单位");
		egu.getColumn("YUNSDWNAME").setHeader("运输单位");
		egu.getColumn("CHES").setHeader("车数");
		egu.getColumn("DUNS").setHeader("吨数");
		egu.getColumn("BEIZ").setHeader("备注");
		egu.getColumn("MEIKXXBNAME").setWidth(230);
		egu.getColumn("YUNSDWNAME").setWidth(230);
		egu.getColumn("BEIZ").setWidth(300);
		
		egu.getColumn("MEIKXXBNAME").setEditor(new ComboBox());
		egu.getColumn("MEIKXXBNAME").setComboEditor(egu.gridId,new IDropDownModel("SELECT ID, QUANC FROM meikxxb WHERE ZHUANGT = 1",null));
		((ComboBox)(egu.getColumn("MEIKXXBNAME").editor)).setEditable(true);
		
		egu.getColumn("YUNSDWNAME").setEditor(new ComboBox());
		egu.getColumn("YUNSDWNAME").setComboEditor(egu.gridId,new IDropDownModel("SELECT ID, QUANC FROM yunsdwb ",null));
		((ComboBox)(egu.getColumn("YUNSDWNAME").editor)).setEditable(true);
		egu.getColumn("MEIKXXBNAME").editor.setAllowBlank(false);
		egu.getColumn("YUNSDWNAME").editor.setAllowBlank(false);
		((NumberField)egu.getColumn("DUNS").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("CHES").editor).setDecimalPrecision(0);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		
		egu.addTbarText("日期:");
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, "InsertButton");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "DeleteButton");
		egu.addToolbarButton(GridButton.ButtonType_Save, "savebutton");
		
		setExtGrid(egu);
		con.Close();
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
		}
		init();
	}
	private void init() {
		setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
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
