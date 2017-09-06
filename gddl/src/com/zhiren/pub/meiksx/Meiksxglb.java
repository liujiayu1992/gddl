package com.zhiren.pub.meiksx;






import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.*;



public class Meiksxglb extends BasePage implements PageValidateListener {
	public List gridColumns;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		getExtGrid().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private void Return(IRequestCycle cycle){
		cycle.activate("Meikxx");
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}


	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(visit.getString9());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData(String tt) {
		Visit visit = (Visit) getPage().getVisit();
		String sql1 = "";
	
		JDBCcon con = new JDBCcon();
//		String sql="select id from zidyjcsjyms";
//		ResultSetList sqlrsl = con.getResultSetList(sql);
//		if(sqlrsl==null||sqlrsl.equals("")){
			 sql1=
				 "select m.id,\n" +
				 "       m.diancxxb_id,\n" + 
				 "       m.meikxxb_id,\n" + 
				 "      (select i.mingc from item i where i.id= m.shuxmc) shuxmc,\n" + 
				 "       m.shuxbm,\n" + 
				 "       m.zhi,\n" + 
				 "       d.mingc as danwb_id,\n"+
				 "       decode(m.shifsy,1,'是',0,'否') as shifsy,\n" + 
				 "       m.beiz\n" + 
				 "  from MEIKSXGLB m,danwb d\n" + 
				 "  where m.danwb_id=d.id and " +
				 "        m.diancxxb_id="+visit.getDiancxxb_id() +
				 "     and   m.meikxxb_id="+tt;

		
			
		
		
		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meiksxglb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("shuxmc").setHeader("属性名称");
		egu.getColumn("shuxbm").setHidden(true);
		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("danwb_id").setHeader("单位");
		
		egu.getColumn("shifsy").setHeader("是否使用");
		
		egu.getColumn("beiz").setHeader("备注");
		

		egu.getColumn("shuxmc").setEditor(new ComboBox());
		egu.getColumn("shuxmc").setComboEditor(egu.gridId,
					new IDropDownModel("select i.id,i.mingc from item i,itemsort s where i.itemsortid = s.id and s.mingc='煤矿属性名称' order by mingc") );
		egu.getColumn("shuxmc").setReturnId(true);
	
		
		
		egu.getColumn("meikxxb_id").setDefaultValue(visit.getString9());
		egu.getColumn("shuxbm").setDefaultValue("运距");
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(visit.getDiancxxb_id()));
		
		
		
		egu.getColumn("danwb_id").setEditor(new ComboBox());
		egu.getColumn("danwb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select distinct id,mingc from danwb order by mingc"));
		egu.getColumn("danwb_id").setReturnId(true);
		
		List shifsy2 = new ArrayList();
		shifsy2.add(new IDropDownBean(0, "否"));
		shifsy2.add(new IDropDownBean(1, "是"));
	
		
		egu.getColumn("shifsy").setEditor(new ComboBox());
		egu.getColumn("shifsy").setComboEditor(egu.gridId,
				new IDropDownModel(shifsy2));
		egu.getColumn("shifsy").setDefaultValue("否");
		egu.getColumn("shifsy").setReturnId(true);
		egu.getColumn("shifsy").setWidth(90);
		


		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton btnreturn = new GridButton("返回",
		"function (){document.getElementById('ReturnButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
		setExtGrid(egu);
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
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData(visit.getString9());
		}
	}

}



