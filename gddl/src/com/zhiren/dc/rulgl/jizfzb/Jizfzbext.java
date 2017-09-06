package com.zhiren.dc.rulgl.jizfzb;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jizfzbext extends BasePage implements PageValidateListener {
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

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	 private boolean _PowerChick = false;
	    public void PowerButton(IRequestCycle cycle) {
	        _PowerChick = true;
	    }
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
		if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
	}
	
	// 设置分组
	private void Power(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		visit.setString10(getTreeid());
		cycle.activate("Jizmc");
	}
	
	private void save() {
	Visit visit = (Visit) this.getPage().getVisit();
	JDBCcon con = new JDBCcon();
	StringBuffer sql = new StringBuffer();
	sql.append("begin \n");
	
	ResultSetList rsl = visit.getExtGrid1().getDeleteResultSet(getChange());
	while (rsl.next()) {
		sql.append("delete from jizfzb where id = ").append(rsl.getString("id")).append("; \n");
		sql.append("delete from jizfzglb where jizfzb_id = ").append(rsl.getString("id")).append("; \n");
	}
	rsl.close();
	
	ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
	while (mdrsl.next()) {
		
		if ("0".equals(mdrsl.getString("id"))) {
			sql.append("insert into jizfzb(id,diancxxb_id, xuh,mingc, leib) values(")
			.append("getnewid("+ getTreeid() +"),").append(getTreeid())
			.append(", '").append(mdrsl.getString("xuh")).append("', '")
			.append(mdrsl.getString("mingc")).append("', '")
			.append(mdrsl.getString("leib")).append("'); \n");
			
		} else {
			sql.append("update jizfzb set")
			.append(" xuh = ").append(mdrsl.getDouble("xuh")).append(", ")
			.append(" mingc = '").append(mdrsl.getString("mingc")).append("',")
			.append("leib='").append(mdrsl.getString("leib"))
			.append("' where id = ")
			.append(mdrsl.getString("id")).append("; \n");
		}
	}
	
	sql.append("end;");
	con.getResultSet(sql.toString());
	}
	
	// 一厂多制时判断是否是总厂
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
			
		String chaxun = "select j.id, " +
						"	    j.diancxxb_id, " +
						"		j.xuh, " +
						"		j.mingc, " +
						"		j.leib " +
						"  from jizfzb j, diancxxb d\n" + 
						" where j.diancxxb_id = d.id\n" + 
						"   and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid() + ")";
		
		//System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
	    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
	   	egu.setTableName("jizfzb");
	   	egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("电厂信息");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("xuh").setHeader("排序号");
		egu.getColumn("mingc").setHeader("分组名称");
		egu.getColumn("leib").setHeader("类别");
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
		egu.addPaging(25);//设置分页
		//设置页面的宽度,当超过这个宽度时显示滚动条
		//*****************************************设置默认值****************************
		egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		
		//类别下拉框
		List leib = new ArrayList();
		leib.add(new IDropDownBean(0, "入炉数量"));
		leib.add(new IDropDownBean(1, "入炉质量"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(leib));
		egu.getColumn("leib").setReturnId(false);
		
		// 设置树
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		// 一厂多制总厂不显示操作按钮
		if (visit.isFencb() && isParentDc(con)) {
			
		}else{
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			egu.addToolbarItem("{"+new GridButton("设置包含的机组","function(){"+
					"if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){" +
					"Ext.MessageBox.alert('提示信息','请选中一个机组分组');" +
					"return;}" +
					"grid1_rcd = gridDiv_sm.getSelections()[0];" +
					"if(grid1_rcd.get('ID') == '0'){" +
					"Ext.MessageBox.alert('提示信息','在设置分组之前请先保存!');" +
					"return;}" +
					"grid1_history = grid1_rcd.get('ID');" +
					"var Cobj = document.getElementById('CHANGE');" +
					"Cobj.value = grid1_history;" +
					"document.getElementById('PowerButton').click();" +
					"}").getScript()+"}");
		}
		
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

	//树
	private String treeid;
	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
		return getTree().getWindowTreeScript();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
		
			visit.setList1(null);
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		
		getSelectData();
		
		
	}


}
