package com.zhiren.gangkjy.xit;
/**
 * @author 张琦
 */


import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Bowxx extends BasePage implements PageValidateListener {
	public List gridColumns;
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

	private void Save() {
		if(getChange() == null || "".equals(getChange())){
			setMsg("error,修改记录为空！");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag =0;
		con.setAutoCommit(false);
		
		String sqldel ="";
		//删除
		ResultSetList rsldel = getExtGrid().getDeleteResultSet(getChange());
		while (rsldel.next()) {
			sqldel ="delete from bowxxb where id = "+rsldel.getString(0)+";\n";
		}
		
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		
		long chezxxb_id=0;
		String sql = "begin\n";
		sql+=sqldel;
		
		while (rsl.next()) {
		
			chezxxb_id=(getExtGrid().getColumn("CHEZXXB_ID").combo).getBeanId(rsl.getString("chezxxb_id"));

				if("0".equals(rsl.getString("ID"))){
					long chez_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));

					sql += "insert into bowxxb(id,xuh,mingc,dunw,chezxxb_id,beiz) values(" + chez_id + "," + rsl.getInt("xuh") 
					+ ",'" +rsl.getString("mingc")+"',"+rsl.getDouble("dunw")+","+chezxxb_id +",'"+
					rsl.getString("beiz")+"');\n";
				}else{
					int id = rsl.getInt("id");
					sql += "update bowxxb set "
						+ " chezxxb_id = " + chezxxb_id
						+ ",xuh = " + rsl.getString("xuh")
						+ ", mingc='" + rsl.getString("mingc")
						+ "',dunw = " + rsl.getString("dunw")
						+ ",beiz = '" + rsl.getString("beiz")
						+ "' where id = " + id + ";\n";
				}
		}
		if(rsl.getRows()>0 || rsldel.getRows()>0){
			sql += "end;\n";
			flag = con.getUpdate(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
			if (flag !=-1){
				setMsg("保存成功！");
			}
		}
		rsldel.close();
		rsl.close();
		con.Close();
		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	



	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}

	}

	public void getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		String sql1 = "";
		JDBCcon con = new JDBCcon();
		
			
			 sql1= "select b.id    id,\n" +
				 "       b.xuh   xuh,\n" + 
				 "       b.mingc mingc,\n" + 
				 "       b.dunw  dunw,\n" + 
				 "       c.quanc chezxxb_id,\n" + 
				 "       b.beiz  beiz\n" + 
				 "  from bowxxb b, vwchez c\n" + 
				 " where b.chezxxb_id = c.id\n" + 
				 "   and c.leib = '车站'\n" + 
				 " order by b.xuh";


		
		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("bowxxb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader(Local.xuh);
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("mingc").setHeader(Local.mingc);
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("dunw").setHeader(Local.dunw);
		egu.getColumn("dunw").setWidth(60);
		egu.getColumn("chezxxb_id").setHeader(Local.gangk);
		egu.getColumn("chezxxb_id").setWidth(80);
	
		egu.getColumn("beiz").setHeader(Local.beiz);
		egu.getColumn("beiz").setWidth(120);
		
		
		


		//港口下拉框
		egu.getColumn("chezxxb_id").setEditor(new ComboBox());
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select  id,mingc from vwchez  where leib='车站' order by mingc"));


		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
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



