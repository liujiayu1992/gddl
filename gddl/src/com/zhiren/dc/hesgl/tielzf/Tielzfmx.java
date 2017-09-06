package com.zhiren.dc.hesgl.tielzf;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Tielzfmx extends BasePage implements PageValidateListener {
	private static final String CustomSetKey = "Tielzfmx"; 
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
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
		JDBCcon con = new JDBCcon();
		String sql ;
		Visit visit = ((Visit) this.getPage().getVisit());
		String tielzfid = visit.getString1();
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		while(rsl.next()){
			sql = "delete from tielzfmx where id =" + rsl.getString("id");
			con.getDelete(sql);
		}
		rsl.close();
		rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			long id = rsl.getLong("id");
			String feixmid = getExtGrid().getColumn("feiymc_item_id").combo.getBeanStrId(rsl.getString("feiymc_item_id"));
			if(id ==0 ){
				sql = "insert into tielzfmx(id,tielzf_id,feiymc_item_id,zongje,beiz) values(" +
				"getnewid(" + visit.getDiancxxb_id() + ")," + tielzfid + "," +
				feixmid + "," + rsl.getDouble("zongje") +",'" + rsl.getString("beiz") + "')";
				con.getInsert(sql);
			}else{
				sql = "update tielzfmx set feiymc_item_id = " + feixmid +
				",zongje = " + rsl.getDouble("zongje") +" ,beiz = '" + 
				rsl.getString("beiz") +"' where id = " + id;
				con.getUpdate(sql);
			}
		}
		Tielzf.updatezongje(con, visit.getString1());
		rsl.close();
		con.Close();
		setMsg("保存成功");
		
	}
	private void Return(IRequestCycle cycle){
		cycle.activate("Tielzf");
	}
	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _Returnclick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_Returnclick = true;
	}
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		if (_Returnclick) {
			_Returnclick = false;
			Return(cycle);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
	}

	public void getSelectData() {
		Visit visit = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String sql = 
			"select t.id,t.tielzf_id,i.mingc as feiymc_item_id,\n" +
			"t.shuik,t.zongje,t.beiz from tielzfmx t, item i\n" + 
			"where t.feiymc_item_id = i.id and t.tielzf_id =" + visit.getString1();

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey );
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(50);
		egu.setTableName("tielzfmx");
		egu.getColumn("feiymc_item_id").setHeader("费用项目");
		egu.getColumn("shuik").setHeader("税款");
		egu.getColumn("zongje").setHeader("金额");
		egu.getColumn("beiz").setHeader("备注");
		
		egu.getColumn("feiymc_item_id").setWidth(150);
		egu.getColumn("zongje").setWidth(80);
		egu.getColumn("beiz").setWidth(260);
		egu.getColumn("shuik").setHidden(true);
		
		ComboBox fymc = new ComboBox();
		fymc.setEditable(true);
		egu.getColumn("feiymc_item_id").setEditor(fymc);
		sql = "select id, mingc from item where itemsortid = (select itemsortid " +
		"from itemsort where bianm = 'DZZF') order by mingc";
		egu.getColumn("feiymc_item_id").setComboEditor(egu.gridId, new IDropDownModel(sql));
		egu.getColumn("feiymc_item_id").setReturnId(true);
		egu.getColumn("feiymc_item_id").editor.setAllowBlank(true);
		
		egu.getColumn("tielzf_id").setHidden(true);
		egu.getColumn("tielzf_id").setEditor(null);
		egu.getColumn("tielzf_id").setDefaultValue(visit.getString1());

		String rfb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("刷新", rfb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarBtn(new GridButton("返回", "function(){document.getElementById('ReturnButton').click();}",SysConstant.Btn_Icon_Return));
		setExtGrid(egu);
		rsl.close();
		con.Close();
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
			visit.setList1(null);
			visit.setActivePageName(getPageName().toString());
			getSelectData();
		}
	}
}
