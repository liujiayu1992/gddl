package com.zhiren.dc.gdxw.caiychxg;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caiyxgch extends BasePage implements PageValidateListener {
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
	/*	Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);*/
		
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择数据！");
			return;
		}
		int flag=0;
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		sb.append("begin \n");
		while(rsl.next()) {
			
			sb.append("update chepbtmp set " +
					"cheph='"+rsl.getString("cheph")+"'  where id ="+rsl.getString("id")+";\n");
			
			
		}
		sb.append("end ; \n");
		
		
		
		flag=con.getUpdate(sb.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:" + sb.toString());
			setMsg("保存过程出现错误！保存未成功！");
			con.rollBack();
			con.Close();
			return;
		}
		
		con.Close();
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		getSelectData();
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id,to_char(lursj,'yyyy-mm-dd hh24:mi:ss') as lursj,lury,cheph,piaojh from chepbtmp where maoz<=0  and isjus=0 order by lursj");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setTitle("车站信息");
		egu.setTableName("chepbtmp");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("lursj").setHeader("采样时间");
		egu.getColumn("lury").setHeader("采样员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lursj").width=120;
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("piaojh").setHeader("采样号");
		egu.getColumn("piaojh").setEditor(null);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	
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
