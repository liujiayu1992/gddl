package com.zhiren.dc.zhuangh.huaybl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuansfx extends BasePage implements PageValidateListener {
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

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		int flag=visit.getExtGrid1().Save(getChange(), visit);
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if(_RefChick){
			_RefChick=false;
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		// 项目名称条件
		long xiangmmcID = -1;
		if(this.getYuansxmmcValue()!=null){
			xiangmmcID = this.getYuansxmmcValue().getId();
		}
		
		String sql=	"SELECT YSF.ID,\n" +
			"       YSF.DIANCXXB_ID,\n" + 
			"       MZ.MINGC        AS MEIZB_ID,\n" + 
			"       YSF.RIQ,\n" + 
			"       YSX.MINGC       AS YUANSXMB_ID,\n" + 
			"       YSF.ZHI,\n" + 
			"       YSF.ZHUANGT\n" + 
			"  FROM YUANSFXB YSF,\n" + 
			"       DIANCXXB DC,\n" + 
			"       PINZB MZ,\n" + 
			"       (SELECT ID, MINGC FROM YUANSXMB WHERE ID = "+xiangmmcID+") YSX\n" + 
			" WHERE YSF.DIANCXXB_ID = DC.ID\n" + 
			"   AND YSF.MEIZB_ID = MZ.ID\n" + 
			"   AND YSF.YUANSXMB_ID = YSX.ID\n" + 
			"   AND YSF.DIANCXXB_ID = "+this.getTreeid()+"\n" + 
			"   AND TO_CHAR(YSF.RIQ, 'yyyy') = '"+intyear+"'\n" + 
			" ORDER BY MEIZB_ID,YSF.RIQ";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuansfxb");
		// /设置显示列名称
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue("" + this.getTreeid());
		egu.getColumn("diancxxb_id").setEditor(null);		
		
		egu.getColumn("meizb_id").setHeader("品种");
		egu.getColumn("meizb_id").setEditor(new ComboBox());
		egu.getColumn("meizb_id").setComboEditor(egu.gridId,new IDropDownModel("select id, mingc from pinzb"));
		
		egu.getColumn("yuansxmb_id").setHeader("项目名称");
		egu.getColumn("yuansxmb_id").setEditor(new ComboBox());
		egu.getColumn("yuansxmb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc  from yuansxmb where zhuangt=1 "));
		// /设置下拉框默认值
		egu.getColumn("yuansxmb_id").setDefaultValue((getYuansxmmcValue()==null)?"":getYuansxmmcValue().getValue());
		egu.getColumn("yuansxmb_id").setReturnId(true);
		
		egu.getColumn("zhi").setHeader("值");
		
		egu.getColumn("riq").setHeader("启用日期");

		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setDefaultValue("1");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.addPaging(25);

//		设置树
		egu.addTbarText("电厂:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");
		
		
		// 与页面上的下拉框绑定
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");

		egu.addTbarText("项目名称:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("YuansxmmcDropDown");
		comb3.setId("yuansxm");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());
		
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});yuansxm.on('select',function(){document.forms[0].submit();});");
		// /设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		String Scrpit="gridDiv_grid.on('beforeedit',function(e){\n"+
		"if(e.field=='YUANSXMB_ID'){e.cancel=true;}\n"+
		"});\n";
		egu.addOtherScript(Scrpit);
		
		setExtGrid(egu);
		con.Close();
	}

//	-----电厂tree

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
	
	//--------------------------------
	
	
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
			this.setTreeid(visit.getDiancxxb_id()+"");
			this.setNianfValue(null);
			this.getNianfModels();
			setYuansxmmcValue(null);
			getIYuansxmmcModels();
		}
		getSelectData();
	}

	// 年份
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2011; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 项目名称
	public boolean _Yuansxmmcchange = false;

	private IDropDownBean _YuansxmmcValue;

	public IDropDownBean getYuansxmmcValue() {
		if (_YuansxmmcValue == null) {
			
			if(getIYuansxmmcModels()==null || getIYuansxmmcModels().getOptionCount()<=0){
				_YuansxmmcValue=null;
				
			}else{

				_YuansxmmcValue = (IDropDownBean) getIYuansxmmcModel()
						.getOption(0);
			}
				
			
		}
		return _YuansxmmcValue;
	}

	public void setYuansxmmcValue(IDropDownBean Value) {
		long id = -2;
		if (_YuansxmmcValue != null) {
			id = _YuansxmmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Yuansxmmcchange = true;
			} else {
				_Yuansxmmcchange = false;
			}
		}
		_YuansxmmcValue = Value;
	}

	private IPropertySelectionModel _IYuansxmmcModel;

	public void setIYuansxmmcModel(IPropertySelectionModel value) {
		_IYuansxmmcModel = value;
	}

	public IPropertySelectionModel getIYuansxmmcModel() {
		if (_IYuansxmmcModel == null) {
			getIYuansxmmcModels();
		}
		return _IYuansxmmcModel;
	}

	public IPropertySelectionModel getIYuansxmmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc  from yuansxmb where zhuangt=1 order by mingc";
			_IYuansxmmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IYuansxmmcModel;
	}
}
