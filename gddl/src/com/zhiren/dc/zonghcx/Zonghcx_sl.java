package com.zhiren.dc.zonghcx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zonghcx_sl extends BasePage implements PageValidateListener {
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
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				.append(
						"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append(
						"rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("var gysid,gys,mkid,mk,fzid,fz;")
				.append("if(cks.getDepth() == 3){ \n")
				.append("gysid = cks.parentNode.parentNode.id;")
				.append("gys = cks.parentNode.parentNode.text;")
				.append("mkid = cks.parentNode.id;")
				.append("mk = cks.parentNode.text;")
				.append("fzid = cks.id;")
				.append("fz = cks.text;")
				.append("}else if(cks.getDepth() == 2){ \n")
				.append("gysid = cks.parentNode.id;")
				.append("gys = cks.parentNode.text;")
				.append("mkid = cks.id;")
				.append("mk = cks.text;")
				.append("}else if(cks.getDepth() == 1){ \n")
				.append("gysid = cks.id;")
				.append("gys = cks.text;")
				.append("}")
				
				.append(
						"rec.set('GONGYSB_ID', gysid); \n")
						.append(
						"rec.set('GONGYS', gys); \n")
				.append("rec.set('MEIKXXB_ID', mkid); \n")
				.append("rec.set('MEIKMC', mk); \n")
				.append("rec.set('FAZ_ID', fzid); \n")
				.append("rec.set('FAZ', fz); \n")
				.append("gongysTree_window.hide(); \n")
				.append("return;")
				.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
			visit.setDefaultTree(null);
			setTbmsg(null);
			getSelectData();
		}
	}
	
	// 燃料品种
	private IDropDownBean _PinzValue;

	public IDropDownBean getPinzValue() {
		if (_PinzValue == null) {
			_PinzValue = (IDropDownBean) getIPinzModel().getOption(0);
		}
		return _PinzValue;
	}

	public void setPinzValue(IDropDownBean Value) {
		_PinzValue = Value;
	}

	private IPropertySelectionModel _IPinzModel;

	public void setIPinzModel(IPropertySelectionModel value) {
		_IPinzModel = value;
	}

	public IPropertySelectionModel getIPinzModel() {
		if (_IPinzModel == null) {
			getIPinzModels();
		}
		return _IPinzModel;
	}

	public void getIPinzModels() {
		_IPinzModel = new IDropDownModel(SysConstant.SQL_Pinz_mei);
	}





	// 运输单位
	private IDropDownBean _YunsdwValue;

	public IDropDownBean getYunsdwValue() {
		if (_YunsdwValue == null) {
			_YunsdwValue = (IDropDownBean)getIYunsdwModel().getOption(0);
		}
		return _YunsdwValue;
	}

	public void setYunsdwValue(IDropDownBean Value) {
		_YunsdwValue = Value;
	}

	private IPropertySelectionModel _IYunsdwModel;

	public void setIYunsdwModel(IPropertySelectionModel value) {
		_IYunsdwModel = value;
	}

	public IPropertySelectionModel getIYunsdwModel() {
		if (_IYunsdwModel == null) {
			getIYunsdwModels();
		}
		return _IYunsdwModel;
	}

	public void getIYunsdwModels() {
		_IYunsdwModel = new IDropDownModel(SysConstant.SQL_Yunsdw_mc);
	}

//--------------------------------------------------------------------------------



	// 运输方式
	private IDropDownBean _YunsValue;

	public IDropDownBean getYunsValue() {
		if (_YunsValue == null) {
			_YunsValue = (IDropDownBean) getIYunsModel().getOption(0);
		}
		return _YunsValue;
	}

	public void setYunsValue(IDropDownBean Value) {
		_YunsValue = Value;
	}

	private IPropertySelectionModel _IYunsModel;

	public void setIYunsModel(IPropertySelectionModel value) {
		_IYunsModel = value;
	}

	public IPropertySelectionModel getIYunsModel() {
		if (_IYunsModel == null) {
			getIYunsModels();
		}
		return _IYunsModel;
	}

	public void getIYunsModels() {
		_IYunsModel = new IDropDownModel(SysConstant.SQL_yunsfs);
	}

	// 计划口径
	private IDropDownBean _JihkjValue;

	public IDropDownBean getJihkjValue() {
		if (_JihkjValue == null) {
			_JihkjValue = (IDropDownBean) getIJihkjModel().getOption(0);
		}
		return _JihkjValue;
	}

	public void setJihkjValue(IDropDownBean Value) {
		_JihkjValue = Value;
	}

	private IPropertySelectionModel _IJihkjModel;

	public void setIJihkjModel(IPropertySelectionModel value) {
		_IJihkjModel = value;
	}

	public IPropertySelectionModel getIJihkjModel() {
		if (_IJihkjModel == null) {
			getIJihkjModels();
		}
		return _IJihkjModel;
	}

	public void getIJihkjModels() {
		_IJihkjModel = new IDropDownModel(SysConstant.SQL_Kouj);
	}
	
	// 厂别
	private IDropDownBean _ChangbValue;

	public IDropDownBean getChangbValue() {
		if (_ChangbValue == null) {
			_ChangbValue = (IDropDownBean) getIChangbModel().getOption(0);
		}
		return _ChangbValue;
	}

	public void setChangbValue(IDropDownBean Value) {
		_ChangbValue = Value;
	}

	private IPropertySelectionModel _IChangbModel;

	public void setIChangbModel(IPropertySelectionModel value) {
		_IChangbModel = value;
	}

	public IPropertySelectionModel getIChangbModel() {
		if (_IChangbModel == null) {
			getIChangbModels();
		}
		return _IChangbModel;
	}

	public void getIChangbModels() {
		_IChangbModel = new IDropDownModel("select id, mingc from diancxxb order by id");
	}
}