package com.zhiren.pub.jitrbdwb;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jitrbdwb extends BasePage implements PageValidateListener{
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
	//js判断页面名称，全称，编码字段的值不能重复。
	String condition = 
		"\n" +
		"var allr = gridDiv_ds.getRange();\n" + 
		"var allm = gridDiv_ds.getModifiedRecords();\n" + 
		"\tfor(var i=0;i<allr.length;i++){\n" + 
		"\t\tif(allr[i].get('ID')>0){\n" + 
		"\n" + 
		"\t\tfor(var j=0;j<allm.length;j++){\n" + 
		"\n" + 
		"\n" + 
		"\t\t\tif(allr[i].get('MINGC')==allm[j].get('MINGC')){\n" + 
		"\t\t\t\tExt.MessageBox.alert('提示信息','名称字段不能重复');\n" + 
		"\t\t\t\treturn;\n" + 
		"\t\t\t}\n" + 
		"\t\t\tif(allr[i].get('QUANC')==allm[j].get('QUANC')){\n" + 
		"\t\t\t\tExt.MessageBox.alert('提示信息','全称字段不能重复');\n" + 
		"\t\t\t\treturn;\n" + 
		"\t\t\t}\n" + 
		"\t\t\tif(allr[i].get('BIANM')==allm[j].get('BIANM')){\n" + 
		"\t\t\t\tExt.MessageBox.alert('提示信息','编码字段不能重复');\n" + 
		"\t\t\t\treturn;\n" + 
		"\t\t\t}\n" + 
		"\n" + 
		"\n" + 
		"\t\t}\n" + 
		"\t\t}\n" + 
		"\n" + 
		"\t}";



	public void setChange(String change) {
		Change = change;
	}

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Visit visit = (Visit) this.getPage().getVisit();
			Save(this.getChange(),visit);
			getSelectData();
		}else if(_RefreshChick){
			_RefreshChick = false;
			getSelectData();
		}
	}
	public int Save(String strchange, Visit visit) {
		
		JDBCcon con = new JDBCcon();
		
		//获得删除的数据
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		StringBuffer sbb = new StringBuffer();
		while (delrsl.next()) {
			String sql2 = "select * from diancgysmykjb where jitrbdwb_id = "+delrsl.getInt("ID");
			ResultSetList rs1 = con.getResultSetList(sql2);
			String sql3 = "select * from jitrbjhb where jitrbdwb_id = "+delrsl.getInt("ID");
			ResultSetList rs2 = con.getResultSetList(sql3);
			if(rs1.next()||rs2.next()){
				setMsg(delrsl.getString("mingc")+"已被使用(禁止删除)");
				
			}else{
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append("jitrbdwb").append(" where id ='")
					.append(delrsl.getString("id")).append("'\n");
			con.getDelete(sql.toString());
			}
		}

		delrsl.close();
		//获得所有修改的数据
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		//判断添加的数据是否重复
		ResultSetList rsl = con.getResultSetList("select * from jitrbdwb");
		ResultSetList rsl_in = null;
		ResultSetList rsl_pd = null;
		//保存添加时的重复值
		StringBuffer st = new StringBuffer();
		//保存修改时的重复值
		StringBuffer st1 = new StringBuffer();
		//以下是添加操作

		while(mdrsl.next()){
			//随机获取id
			String id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			if ("0".equals(mdrsl.getString("ID"))) {
					
				StringBuffer sql1 = new StringBuffer("");
				
				String sql_in = "select * from jitrbdwb where mingc='"+mdrsl.getString("MINGC")+"' or quanc='"+mdrsl.getString("QUANC")+"' or bianm = '"+mdrsl.getString("BIANM")+"'\n";
				rsl_in = con.getResultSetList(sql_in);
				if(rsl_in.next()){
					if(rsl_in.getString("MINGC").equals(mdrsl.getString("MINGC"))){
						st.append("名称<"+mdrsl.getString("MINGC")+">");
					}
					if(rsl_in.getString("QUANC").equals(mdrsl.getString("QUANC"))){
						st.append("全称<"+mdrsl.getString("QUANC")+">");
					}
					if(rsl_in.getString("BIANM").equals(mdrsl.getString("BIANM"))){
						st.append("编码<"+mdrsl.getString("BIANM")+">");
					}
				}else{
					sql1.append("insert into ").append("jitrbdwb").append("(id")
					.append(",mingc,quanc,bianm,beiz)").append("values").append("('").append(id).append("'")
					.append(",'").append(mdrsl.getString("MINGC")).append("'").append(",'").append(mdrsl.getString("QUANC"))
					.append("'").append(",'").append(mdrsl.getString("BIANM")).append("'").append(",'").append(mdrsl.getString("BEIZ"))
					.append("'").append(") \n");
					con.getUpdate(sql1.toString());
				}
				
			} else {
				
				
				String sql_pd="select * from jitrbdwb where (mingc='"+mdrsl.getString("MINGC")+"' or quanc='"+mdrsl.getString("QUANC")+"' or bianm = '"+mdrsl.getString("BIANM")+"') and id<>'"+mdrsl.getString("id")+"'\n";
				rsl_pd = con.getResultSetList(sql_pd);

				if(rsl_pd.next()){
					if(rsl_pd.getString("MINGC").equals(mdrsl.getString("MINGC"))){
						st1.append("名称<"+mdrsl.getString("MINGC")+">");
					}
					if(rsl_pd.getString("QUANC").equals(mdrsl.getString("QUANC"))){
						st1.append("全称<"+mdrsl.getString("QUANC")+">");
					}
					if(rsl_pd.getString("BIANM").equals(mdrsl.getString("BIANM"))){
						st1.append("编码<"+mdrsl.getString("BIANM")+">");
					}
				}else{
					StringBuffer sql2 = new StringBuffer("");
					sql2.append("update jitrbdwb set mingc='").append(mdrsl.getString("MINGC")).append("',").append("quanc='")
					.append(mdrsl.getString("QUANC")).append("',").append("bianm='").append(mdrsl.getString("BIANM")).append("',")
					.append("beiz='").append(mdrsl.getString("BEIZ")).append("' ").append("where id = '").append(mdrsl.getString("ID")).append("'");
					con.getUpdate(sql2.toString());
				}
				
			}
		}	
		if(st.length()>0&&st1.length()>0){
			setMsg("添加"+st.toString()+"重复"+"并且"+"修改"+st1.toString()+"重复");
		}else
			if(st.length()>0){
			setMsg("添加"+st.toString()+"重复");
		}else
			if(st1.length()>0){
			setMsg("修改"+st1.toString()+"重复") ;
		}
		int stl = st.length();
		st.delete(0, stl);
		int stl1 = st1.length();
		st1.delete(0, stl1);
		mdrsl.close();
		con.Close();
		return 1;
	}
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select * from jitrbdwb");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jitrbdwb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(300);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(300);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(300);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(300);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
//		egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}").getScript()+"}");
		GridButton gRefresh = new GridButton("刷新",
		"function(){document.getElementById('RefreshButton').click();}");
			gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
				egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",condition);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

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
