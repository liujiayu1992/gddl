package com.zhiren.main.gongs;

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
/*
 * 作者：夏峥
 * 时间：2011-05-05
 * 描述：使用visit中的String3作为页面跳转传递的参数
 */
public class Gongs_fx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
//	保存数据list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }
    
//  保存页面Bean
    private GongsBean editValue;
    public GongsBean getEditValue() {
    	return editValue;
    }
    public void setEditValue(GongsBean editValue) {
    	this.editValue = editValue;
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _GongswhChick = false;

	public void GongswhButton(IRequestCycle cycle) {
		_GongswhChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_GongswhChick){
			_GongswhChick = false;
			Update(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select id,mingc,leix,beiz,\n" +
				"decode(zhuangt,1,'是','否') zhuangt,diancxxb_id \n" +
				"from gongsb where zhuangt=1 " +
				//"and diancxxb_id = "+visit.getDiancxxb_id()+
				"\n and leix = '经济分析'\n" +
				" order by zhuangt,leix,mingc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("gongsb");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").hidden = true;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(visit.getDiancxxb_id()));
		egu.getColumn("mingc").setHeader("公式名");
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("zhuangt").setHeader("启用");
		egu.getColumn("mingc").setWidth(150);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);//只能单选中行
		//复选框
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(25);
//		egu.getColumn("lujxxb_id").setEditor(new ComboBox());
//		egu.getColumn("lujxxb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from lujxxb"));
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "是"));
		l.add(new IDropDownBean(0, "否"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(l));
		//egu.getColumn("zhuangt").setReturnId(false);
		egu.getColumn("zhuangt").setDefaultValue("是");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, null);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gb = new GridButton("公式维护","function(){ " +
				" var rec = gridDiv_sm.getSelected(); " +
				" if(rec != null){var id = rec.get('ID'); var title = rec.get('LEIX') + '  :  ' + rec.get('MINGC'); " +
				" var Cobj = document.getElementById('CHANGE'); var Cobjid = document.getElementById('CHANGEID');" +
				" Cobj.value = title; Cobjid.value = id;" +
				" document.getElementById('GongswhButton').click();}}");
		egu.addTbarBtn(gb);
//		egu.addToolbarButton(GridButton.ButtonType_Update, "GongswhButton");
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
//	页面判定方法
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

	private void Update(IRequestCycle cycle) {
		
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个分组设置权限!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setLong1(Long.parseLong(getChangeid()));
		visit.setString1(getChange());
		visit.setString3(visit.getActivePageName());
		cycle.activate("Gongsnrext");
		
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
