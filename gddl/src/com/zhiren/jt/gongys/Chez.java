package com.zhiren.jt.gongys;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*2009-4-8 
 * wzb
 * 
 * 修改煤矿和车站关联的时候，车站不能删除的bug
 * 
 * */
/*
 * 时间：2010-03-11
 * 作者：yjm
 * 内容：重写Save()保存方法，解决事务处理的潜在Bug，优化SQL操作语句，修改程序流程。
 */
/*
 * 作者：夏峥
 * 时间：2012-03-20
 * 描述：使用String2作为返回页面的参数
 */
public class Chez extends BasePage implements PageValidateListener {
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sql.append("delete from kuangzglb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
				if("0".equals(mdrsl.getString("ID"))){
					sql.append("insert into kuangzglb(id, meikxxb_id, chezxxb_id) values(getnewid(").append(v.getDiancxxb_id())
						.append("), ").append(v.getString1()).append(", ")
						.append((getExtGrid().getColumn("CHEZXXB_ID").combo).getBeanId(mdrsl.getString("chezxxb_id")))
						.append(");\n");
				}else{
					sql.append("update kuangzglb set chezxxb_id = ")
						.append((getExtGrid().getColumn("CHEZXXB_ID").combo).getBeanId(mdrsl.getString("chezxxb_id")))
						.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
				}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"+ sql.toString());
			setMsg(ErrorMessage.UpdateDatabaseFail);
		} else {
			setMsg("保存成功！");
		}
		mdrsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private void Return(IRequestCycle cycle){
		Visit visit = (Visit) this.getPage().getVisit();
		if(visit.getString2()!=null && visit.getString2().equals("Meikxx_gd")){
			cycle.activate(visit.getString2());
		}else{
			cycle.activate("Meikxx");
		}
		
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
		String sql_mk = "";
		String meikmc = "";
		JDBCcon con = new JDBCcon();
		
		sql_mk = "select distinct id,mingc from meikxxb "
				+ " where id = " + visit.getString1() 
				+ " order by mingc";
		ResultSetList rsl_mk = con.getResultSetList(sql_mk);
		if(rsl_mk.next()){
			meikmc = rsl_mk.getString("mingc");
		}
			 sql1=
				 " select k.id,m.mingc as meikxxb_id,c.mingc as chezxxb_id\n" +
				 " from kuangzglb k,meikxxb m,chezxxb c\n" + 
				 " where k.meikxxb_id = m.id\n" + 
				 "     and k.chezxxb_id = c.id\n" + 
				 " 	   and k.meikxxb_id = " + visit.getString1() +
				 " order by c.mingc";
		
		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("kuangzglb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("chezxxb_id").setHeader("车站名称");
		
		
		egu.getColumn("meikxxb_id").setDefaultValue(meikmc);
		egu.getColumn("meikxxb_id").editor = null;
//		ComboBox c = new ComboBox();
//		c.setEditable(false);
//		egu.getColumn("meikxxb_id").setEditor(c);
//		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select distinct id,mingc from meikxxb "
//						+ " where id = " + visit.getString1() 
//						+ " order by mingc"));
//		egu.getColumn("meikxxb_id").setReturnId(true);
		
		egu.getColumn("chezxxb_id").setEditor(new ComboBox());
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select distinct id,mingc from chezxxb order by mingc"));
		egu.getColumn("chezxxb_id").returnId=true;

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



