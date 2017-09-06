package com.zhiren.dc.liucdzxjsz;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2010-01-20
 * 内容:流程动作衔接表配置
 */
public class Liucdzxjsz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		// TODO 自动生成方法存根
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
		
		int flag=this.getExtGrid().Save(this.getChange(), (Visit)this.getPage().getVisit());
		
		if(flag>=0){
			this.setMsg("数据操作成功");
		}else{
			this.setMsg("数据操作失败");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;
	
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    
    private void Refurbish() {
       
    	getSelectData();
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		
		if (_RefurbishChick) {
            _RefurbishChick = false;
        }
		
		Refurbish();
	}
	
//	长别下拉框取值
	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String liucb_id=this.getFencbValue().getStrId();
		String sql =" select lj.id,lj.liucb_id,js.mingc liucjsb_id,lj.liucztqqmc,lj.liuczthjmc,lj.caoz,lj.liucdz,lj.xianjdz,lj.xianjl,lj.youxj,lj.beiz  " +
				"from liucdzxjb lj,liucjsb js \n" +
				" where liucb_id="+liucb_id+" and lj.liucjsb_id=js.id \n" ;
						
	
		
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("liucdzxjb");		
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("id").setWidth(70);
		
		egu.getColumn("liucb_id").setHeader("ID");
		egu.getColumn("liucb_id").setHidden(true);
		egu.getColumn("liucb_id").editor = null;
		egu.getColumn("liucb_id").setWidth(70);
		egu.getColumn("liucb_id").setDefaultValue(liucb_id);
		
		
		egu.getColumn("liucjsb_id").setHeader("流程角色");
		egu.getColumn("liucjsb_id").setWidth(130);
		egu.getColumn("liucjsb_id").setEditor(new ComboBox());
		egu.getColumn("liucjsb_id").setComboEditor(egu.gridId, new IDropDownModel("" +
				"select id,mingc from liucjsb" +
				""));
		
		egu.getColumn("liucztqqmc").setHeader("前驱状态");
		egu.getColumn("liucztqqmc").setWidth(90);		
		egu.getColumn("liucztqqmc").setEditor(new ComboBox());
		egu.getColumn("liucztqqmc").setComboEditor(egu.gridId, new IDropDownModel("" +
				getComSql(liucb_id,"leibztb1.id,leibztb1.mingc") +
				""));
		egu.getColumn("liucztqqmc").setReturnId(false);
		
		egu.getColumn("liuczthjmc").setHeader("后继状态");
		egu.getColumn("liuczthjmc").setWidth(90);		
		egu.getColumn("liuczthjmc").setEditor(new ComboBox());
		egu.getColumn("liuczthjmc").setComboEditor(egu.gridId, new IDropDownModel("" +
				getComSql(liucb_id,"leibztb2.id,leibztb2.mingc") +
				""));
		egu.getColumn("liuczthjmc").setReturnId(false);
		
		egu.getColumn("caoz").setHeader("操作名");
		egu.getColumn("caoz").setWidth(90);
		egu.getColumn("caoz").setEditor(new ComboBox());
		egu.getColumn("caoz").setComboEditor(egu.gridId, new IDropDownModel("" +
				getComSql(liucb_id,"liucdzb.id,liucdzb.mingc") +
				""));
		egu.getColumn("caoz").setReturnId(false);
		
		egu.getColumn("liucdz").setHeader("动作");
		egu.getColumn("liucdz").setWidth(90);
		egu.getColumn("liucdz").setEditor(new ComboBox());
		egu.getColumn("liucdz").setComboEditor(egu.gridId, new IDropDownModel("" +
				getComSql(liucb_id,"liucdzb.id,liucdzb.dongz mingc") +
				""));
		egu.getColumn("liucdz").setReturnId(false);
		egu.getColumn("liucdz").editor.setAllowBlank(true);
		
		egu.getColumn("xianjdz").setHeader("衔接动作");
		egu.getColumn("xianjdz").setWidth(90);
		
		egu.getColumn("xianjl").setHeader("衔接类");
		egu.getColumn("xianjl").setWidth(200);
		egu.getColumn("xianjl").setDefaultValue("com.zhiren.common.Liucxjcl");
		
		egu.getColumn("youxj").setHeader("优先级");
		egu.getColumn("youxj").setWidth(90);
		egu.getColumn("youxj").editor.allowBlank=false;
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(90);
		
		egu.addTbarText("流程:");
		ComboBox comb5 = new ComboBox();
		comb5.setTransform("FencbDropDown");
		comb5.setId("FencbDropDown");
		comb5.setEditable(false);
		comb5.setLazyRender(true);// 动态绑定
		comb5.setWidth(135);
		comb5.setReadOnly(true);
		egu.addToolbarItem(comb5.getScript());
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		GridButton ref=new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}");
		ref.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(ref);
	
		egu.addOtherScript("FencbDropDown.on('select',function(){document.forms[0].submit();});\n");
	        
		setExtGrid(egu);
		con.Close();
	}

	private String getComSql(String liucb_id,String getCol){
		
		String comSql=" select "+getCol+" \n" +
				" from liucdzb,liucdzjsb,liucjsb,liucztb liucztb1,liucztb liucztb2,leibztb leibztb1,leibztb leibztb2  \n" +
				" where liucztb1.liucb_id="+liucb_id+" \n" +
				" and liucdzb.id=liucdzjsb.liucdzb_id and liucjsb.id=liucdzjsb.liucjsb_id and liucztb1.id=liucdzb.liucztqqid  \n" +
				" and liucztb2.id=liucdzb.liuczthjid and leibztb1.id=liucztb1.leibztb_id and leibztb2.id=liucztb2.leibztb_id  \n" +
				" order by liucdzb.id  ";
		
		return comSql;
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
			visit.setString3("");	//厂
			
			visit.setList1(null);
			setFencbValue(null);	//5
			setFencbModel(null);
			getFencbModels();		//5
			getSelectData();
		}
	}
	
//	流程
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		
			String sql =" select id,mingc from liucb ";
		
			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql,"请选择"));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
}
