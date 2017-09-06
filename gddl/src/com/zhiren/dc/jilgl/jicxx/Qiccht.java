package com.zhiren.dc.jilgl.jicxx;


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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qiccht extends BasePage implements PageValidateListener {
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
		
		String strchange=getChange();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		System.out.println(strchange);
		
		ResultSetList delrsl =getExtGrid().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(getExtGrid().tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			System.out.println(visit.getDiancxxb_id());
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(getExtGrid().tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(mdrsl.getColumnNames()[i].equals("ZHUANGT")){
						if(mdrsl.getString(i).equals("使用")){
							sql.append(",").append(mdrsl.getColumnNames()[i]);
							sql2.append(",").append("1");
						}else{
							sql.append(",").append(mdrsl.getColumnNames()[i]);
							sql2.append(",").append("0");
						}
					}else{
						sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(
							getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i)));
									}
				}
				sql.append(") values(").append(sql2).append(");\n");
				 
				
			} else {
				sql.append("update ").append(getExtGrid().tableName).append(" set ");
				
				
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(mdrsl.getColumnNames()[i].equals("ZHUANGT")){
							if(mdrsl.getString(i).equals("使用")){
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");
							sql.append("1").append(",");
							
							
							
						}else{
							sql.append(mdrsl.getColumnNames()[i]).append(" = ");
							sql.append("0").append(",");
							
						}
					}else{
						System.out.println("get in the ELSE");
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
						
						System.out.println(mdrsl.getString(i));
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
	
				System.out.println(sql);
			}
		}
		System.out.println("END END END END ");
		mdrsl.close();
		sql.append("end;");
		System.out.println(sql);
		con.getUpdate(sql.toString());
		con.Close();
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

	public void getSelectData() {
		//Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id, xuh, mingc, decode(zhuangt,1,'使用','未使用') zhuangt,beiz\n"
						+ "  from qiccht");
					 
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qiccht");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("id").editor = null;
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"使用"));
		list.add(new IDropDownBean(2,"未使用"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setDefaultValue("使用");
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("beiz").setReturnId(false);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		//egu.getColumn("id").setDefaultValue(""+visit.getDiancxxb_id());
		 
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		/*String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Duibcxfz';" +
   	    " window.open(url,'newWin');";
	//egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");*/
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
