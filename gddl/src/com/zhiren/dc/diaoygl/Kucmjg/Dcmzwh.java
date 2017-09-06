package com.zhiren.dc.diaoygl.Kucmjg;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Dcmzwh extends BasePage implements PageValidateListener {

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

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private void Save() {
		JDBCcon con=new JDBCcon();
		ResultSetList changList=getExtGrid().getModifyResultSet(getChange());
		String diancxxb_id = getTreeid();
		String sql="";
		String errorMsg="";
		
		while(changList.next()){
			if ("0".equals(changList.getString("id"))) {
				sql="INSERT INTO dcmzb\n" +
					"  (ID, diancxxb_id, meizmc, beiz, zhuangt)\n" + 
					"VALUES\n" + 
					"  (getnewid("+diancxxb_id+"), "+diancxxb_id+", '"+changList.getString("MEIZMC")+"', '"+changList.getString("BEIZ")+"', 1)";
				if(con.getInsert(sql)<0){
					errorMsg+="煤种："+changList.getString("MEIZMC")+"写入失败<br>";
				}
			}else{
				sql="update dcmzb set beiz='"+changList.getString("BEIZ")+"' where id="+changList.getString("id");
				if(con.getUpdate(sql)<0){
					errorMsg+="煤种："+changList.getString("MEIZMC")+"更新失败<br>";
				}
			}
		}
		if(errorMsg.length()>0){
			setMsg(errorMsg+"其他项保存成功");
		}else{
			setMsg("保存成功");
		}
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _QiyButtonClick = false;

	public void QiyButton(IRequestCycle cycle) {
		_QiyButtonClick = true;
	}

	private boolean _TingyButtonClick = false;

	public void DelButton(IRequestCycle cycle) {
		_TingyButtonClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}

		if (_QiyButtonClick) {
			_QiyButtonClick = false;
			Qiy();
		}
		if (_TingyButtonClick) {
			_TingyButtonClick = false;
			Tingy();
		}
	}

	public void Tingy() {
		JDBCcon con=new JDBCcon();
		String sql="UPDATE dcmzb SET ZHUANGT=0 WHERE ID="+getChange();
		con.getUpdate(sql);
		con.Close();
	}

	public void Qiy() {
		JDBCcon con=new JDBCcon();
		String sql="UPDATE dcmzb SET ZHUANGT=1 WHERE ID="+getChange();
		con.getUpdate(sql);
		con.Close();
	}

	public void getSelectData() {
		String diancxxb_id = getTreeid();
		String sql = 
			"SELECT M.ID,\n" +
			"       M.DIANCXXB_ID DIANCXXB_ID,\n" + 
			"       M.MEIZMC,\n" + 
			"       M.BEIZ,\n" + 
			"       DECODE(M.ZHUANGT, 1, '启用', '停用') ZHUANGT\n" + 
			"  FROM DCMZB M, DIANCXXB DC\n" + 
			" WHERE DC.ID = M.DIANCXXB_ID\n" + 
			"   AND (DC.ID = "+diancxxb_id+" OR DC.FUID = "+diancxxb_id+")";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("DCMZB");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("ID").setEditor(null);
		
		egu.getColumn("DIANCXXB_ID").setHeader("单位ID");
		egu.getColumn("DIANCXXB_ID").setWidth(120);
		egu.getColumn("DIANCXXB_ID").setDefaultValue(getTreeid());
		egu.getColumn("DIANCXXB_ID").setHidden(true);
		egu.getColumn("DIANCXXB_ID").setEditor(null);
		
		egu.getColumn("MEIZMC").setHeader("煤种名称");
		egu.getColumn("MEIZMC").setWidth(120);
		
		egu.getColumn("BEIZ").setHeader("备注");
		egu.getColumn("BEIZ").setWidth(80);
		
		egu.getColumn("ZHUANGT").setHeader("启用状态");
		egu.getColumn("ZHUANGT").setDefaultValue("启用");
		egu.getColumn("ZHUANGT").setWidth(80);
		egu.getColumn("ZHUANGT").setEditor(null);

		egu.setDefaultsortable(false);
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符

		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){"+MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
			.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		 保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		String datachk="	if(gridDiv_sm.getSelected()== null){\n"
						+ "		Ext.MessageBox.alert('提示信息','请选中一条记录');\n" 
						+ " return;\n"
						+ "	}\n " 
						+ "	var rec=gridDiv_sm.getSelected(); \n"
						+ "	document.getElementById('CHANGE').value=rec.get('ID'); \n";
//		 启用按钮
		GridButton gbc = new GridButton("启用","function (){"+datachk+" document.getElementById('QiyButton').click();}");
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
//		 停用按钮
		GridButton gbd = new GridButton("停用", "function (){"+datachk+" document.getElementById('TingyButton').click();}");
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		
		egu.addTbarText("-");
		egu.addTbarText("注：煤种名称不可重复");
		
		egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){\n"
				+ "if(e.record.get('ID')!='0'&& e.field=='MEIZMC'){e.cancel=true;}\n" 
				+ "});");
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
		}
		getSelectData();
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}