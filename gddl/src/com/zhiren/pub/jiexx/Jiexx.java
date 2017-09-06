package com.zhiren.pub.jiexx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.engine.RestartService;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jiexx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg(null);
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
		 int flag = -1;
		 flag = visit.getExtGrid1().Save(getChange(), visit);
		 if(flag>=0){
			 setMsg("保存成功！");
		 }else{
			 setMsg("保存失败！");
		 }
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	public String getDanwmc(){
		Visit visit=(Visit) getPage().getVisit(); 
		JDBCcon con= new JDBCcon();
		String dwmc="";
		String sql="select mingc from diancxxb where id="+visit.getDiancxxb_id();
		ResultSetList rsld=con.getResultSetList(sql);
		try{
			while(rsld.next()){
				dwmc=rsld.getString("mingc");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return dwmc;
	}
	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select j.id as id,\n" +
			"          j.diancxxb_id,\n" + 
			"          d.mingc,\n" + 
			"          j.bianm as bianm,\n" + 
			"          yongt,\n" + 
			"          kerncs,\n" + 
			"           '<a href = \""+MainGlobal.getHomeContext(this)+"/app?service=page/ImageReport&&id=' || j.id ||'&&mk=jiexx\" target=\"_blank\">查看</a>' as changntlxlt,\n" + 
			"          j.beiz as beiz\n" + 
			"from jiexx j, diancxxb d\n" + 
			"where j.diancxxb_id = d.id\n" +
			"       and d.id="+visit.getDiancxxb_id();

		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jiexx");
		egu.getColumn("diancxxb_id").setHeader("diancxxb_id");
		egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("mingc").setHeader("单位名称");
		egu.getColumn("mingc").setDefaultValue(""+getDanwmc());
		egu.getColumn("mingc").setUpdate(false);
		egu.getColumn("mingc").setEditor(null);
		
		egu.getColumn("bianm").setHeader("线路编码");
		egu.getColumn("yongt").setHeader("线路用途");
		egu.getColumn("kerncs").setHeader("可容纳车数");
		egu.getColumn("changntlxlt").setHeader("厂内铁路线路图");
		egu.getColumn("changntlxlt").update = false;
		egu.getColumn("changntlxlt").setEditor(null);
		egu.getColumn("beiz").setHeader("备注");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		// List l = new ArrayList();
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gbphoto = new GridButton("图片处理","function (){var rec = gridDiv_sm.getSelected();if(!rec){"+MainGlobal.getExtMessageBox("请选择接卸线查看对应的图片", false)+"\n return;}"+MainGlobal.getOpenWinScript("ImageUpLoad&mk=jiexx&id='+rec.get(\"ID\")+'")+ "}");
		gbphoto.setIcon(SysConstant.Btn_Icon_Show);
		egu.addTbarBtn(gbphoto);
		
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
			getSelectData();
		}
	}
}
