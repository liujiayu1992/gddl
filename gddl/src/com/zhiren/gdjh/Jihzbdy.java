package com.zhiren.gdjh;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Jihzbdy extends BasePage implements PageValidateListener {
	
	
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//-------------------------------------------------------------  save()  -----------------------------

	private void Save() {
  		Visit visit = (Visit) this.getPage().getVisit();
 		int flag=visit.getExtGrid1().Save(getChange(), visit);
 		if(flag!=-1){
 			setMsg(ErrorMessage.SaveSuccessMessage);
 		}
 	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String sql="SELECT ID,\n" +
			"       DIANCXXB_ID,\n" + 
			"       XUH,\n" + 
			"       BIANM,\n" + 
			"       MINGC,\n" + 
			"       ZIDM,\n" + 
			"       DANW,\n" + 
			"       GONGS,\n" + 
			"       LEIJGS,\n" + 
			"       LAIY,\n" + 
			"		MOKMC,\n"+
			"       DECODE(ZHUANGT, 1, '启用', 0, '未启用') ZHUANGT\n" + 
			"  FROM JIHZBDYB\n" + 
			" WHERE DIANCXXB_ID = "+this.getTreeid()+"\n" +
			"	AND MOKMC='"+this.getMokValue().getValue()+"'\n" + 
			" ORDER BY XUH";

		ResultSetList rsl = con.getResultSetList(sql);
			
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("JIHZBDYB");
		egu.setWidth("bodyWidth");

		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setDefaultValue(this.getTreeid());
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("xuh").setHeader(" 序号");
		egu.getColumn("xuh").setWidth(50);
		
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(50);
		
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(175);
		
		egu.getColumn("zidm").setHeader("字段名");
		egu.getColumn("zidm").setWidth(120);
		
		egu.getColumn("danw").setHeader("单位");
		egu.getColumn("danw").setWidth(80);

		egu.getColumn("gongs").setHeader("公式");
		egu.getColumn("gongs").setWidth(300);
		
		egu.getColumn("leijgs").setHeader("累计公式");
		egu.getColumn("leijgs").setWidth(100);
		egu.getColumn("leijgs").setHidden(true);
		
		egu.getColumn("laiy").setHeader("来源");
		egu.getColumn("laiy").setWidth(80);
		egu.getColumn("laiy").setHidden(true);
		
		egu.getColumn("MOKMC").setHeader("模块名称");
		egu.getColumn("MOKMC").setWidth(80);
		egu.getColumn("MOKMC").setHidden(true);
		egu.getColumn("MOKMC").setDefaultValue(this.getMokValue().getValue());
		
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setDefaultValue("启用");
		egu.getColumn("zhuangt").setWidth(80);
		
		// 状态类型
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"启用"));
		list.add(new IDropDownBean(0,"未启用"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("zhuangt").setReturnId(true);//绑定了，页面显示汉字，库里显示数字。
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(0);
		egu.setDefaultsortable(false);//设定页面不自动排序
		
//		工具栏
//		电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		egu.addTbarText("指标类型:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("MOK");
		comb1.setId("MOK");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			getTreeid();
			this.setMokValue(null);
			this.getMokModels();
		}
		getSelectData();

	}
	
//	模块下拉框
	public IDropDownBean getMokValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getMokModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setMokValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setMokModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getMokModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getMokModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getMokModels() {
		List listMok = new ArrayList();
		listMok.add(new IDropDownBean(1, "年计划"));
		listMok.add(new IDropDownBean(2, "月计划"));
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listMok));
		return;
	}

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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

}
