package com.zhiren.jt.zdt.monthreport.yuebsbzn;


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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/* 
* 时间：2009-04-09
* 作者： ll
* 修改内容：  页面显示修改字段宽度。
*/
public class Gonghdwsz extends BasePage implements PageValidateListener {
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

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
	private void Save() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		int flag=0;
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");
		while (rsl.next()) {
			
			long id = 0;
						
			String ID_1=rsl.getString("ID");

							
			if ("0".equals(ID_1)||"".equals(ID_1)) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				
				//向上报中能口径定义表表里插入数据
				sql.append("insert into zhongnghdwglb("
								+ "id,gongysb_id,zhongnghdwb_id,beiz)values("
								+ id + ","
								+ rsl.getLong("gongysb_id")
								+ ",(select distinct max(id) from zhongnghdwb where mingc='"+ rsl.getString("zhongnghdwmc") + "'),"
								+  "'');\n");
			
			} else {
				
				//修改上报中能口径定义表表数据
				 sql.append("update zhongnghdwglb set zhongnghdwb_id=(select distinct max(id) from zhongnghdwb where mingc='"+ rsl.getString("zhongnghdwmc")
						 + "') where id=" + rsl.getLong("id")+";\n");
			}
			
		}
		sql.append("end;");
		flag=con.getUpdate(sql.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
			setMsg(ErrorMessage.UpdateDatabaseFail);
			return;
		}
		
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.Close();
		setMsg("保存成功!");
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
        _ReturnChick = true;
    }
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("Yuebsbzn");
        }
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList(
						"select gl.id as id, g.id as gongysb_id,g.mingc as gongymc,gl.zngh_id as zhongnghdwb_id,gl.mingc as zhongnghdwmc \n" +
						"            from (select dwgl.id as id,dwgl.gongysb_id,zngh.id as zngh_id,zngh.mingc as mingc from zhongnghdwglb dwgl,zhongnghdwb zngh\n" + 
						"                 where dwgl.zhongnghdwb_id=zngh.id)gl,\n" + 
						"               (select distinct gy.id,gy.mingc from gongysb gy,yuetjkjb kj\n" + 
						"               where kj.gongysb_id=gy.id order by gy.id) g\n" + 
						"     where gl.gongysb_id(+)=g.id"
						);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zhongnghdwglb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader("gongysb_id");
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("gongymc").setHeader("供货单位名称");
		egu.getColumn("gongymc").setWidth(220);
		egu.getColumn("gongymc").setUpdate(false);
		egu.getColumn("zhongnghdwb_id").setHeader("zhongnghdwb_id");
		egu.getColumn("zhongnghdwb_id").setHidden(true);
		egu.getColumn("zhongnghdwmc").setHeader("中能供货单位名称");
		egu.getColumn("zhongnghdwmc").setReturnId(true);
		egu.getColumn("zhongnghdwmc").setWidth(280);
		
//		ComboBox c4 = new ComboBox();
//		egu.getColumn("gongysb_id").setEditor(c4);
//		c4.setEditable(true);
//		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, 
//				new IDropDownModel("select id,mingc from gongysb  where fuid=0 or fuid =-1"));
		
		ComboBox c5 = new ComboBox();
		egu.getColumn("zhongnghdwmc").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("zhongnghdwmc").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from zhongnghdwb order by mingc"));

		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("返回","function(){"+
				
				"document.getElementById('ReturnButton').click();" +
				"}").getScript()+"}");
		
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
