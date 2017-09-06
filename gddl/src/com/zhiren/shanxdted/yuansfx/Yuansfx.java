package com.zhiren.shanxdted.yuansfx;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-09-17
 * 内容:完成一厂多制情况下元素分析
 */
public class Yuansfx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	

	protected void initialize() {
		// TODO Auto-generated method stub
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

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		getSelectData();
		
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		// 供应商条件
		long gongysID = this.getMeikdqmcValue().getId();
		// 项目名称条件
		long xiangmmcID = -1;
		if(this.getYuansxmmcValue()!=null){
			xiangmmcID = this.getYuansxmmcValue().getId();
		}
		
		

		ResultSetList rsl = con
				.getResultSetList("select ysf.id,ysf.diancxxb_id,mk.mingc as meikxxb_id,mz.mingc as meizb_id,"
						+ " ys.mingc as yunsfsb_id,ysf.riq,ysx.mingc as yuansxmb_id,ysf.zhi,ysf.zhuangt "
						+ "from yuansfxb ysf,diancxxb dc,(select mk.id as id,mk.mingc as mingc from meikxxb mk,gongysmkglb gm,gongysb gy "
						+ "where gm.meikxxb_id(+)=mk.id and gm.gongysb_id=gy.id(+) and gy.id="
						+ gongysID
						+ " ) mk,pinzb mz, yunsfsb ys,"
						+ " (select id,mingc from yuansxmb where id="
						+ xiangmmcID
						+ ") ysx "
						+ "where ysf.diancxxb_id=dc.id and ysf.meikxxb_id=mk.id and ysf.meizb_id=mz.id"
						+ " and ysf.yunsfsb_id=ys.id and ysf.yuansxmb_id=ysx.id and ysf.diancxxb_id="
						+ this.getTreeid()
						+ " and to_char(ysf.riq, 'yyyy') = '"
						+ intyear
						+ "' order by ysf.id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuansfxb");
		// /设置显示列名称
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		
		egu.getColumn("meizb_id").setHeader("品种");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yuansxmb_id").setHeader("项目名称");

		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("riq").setHeader("启用日期");

		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setDefaultValue("1");
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + this.getTreeid());
		egu.getColumn("diancxxb_id").setEditor(null);

		
		egu.getColumn("meikxxb_id").setWidth(180);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.addPaging(25);
		
		// /动态下拉框
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu
				.getColumn("meikxxb_id")
				.setComboEditor(
						egu.gridId,
						new IDropDownModel(
								"select mk.id as id,mk.mingc as mingc from meikxxb mk,gongysmkglb gm,gongysb gy "
										+ "where gm.meikxxb_id(+)=mk.id and gm.gongysb_id=gy.id(+) and gy.id="
										+ gongysID));
		
		
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb"));
		
		
		egu.getColumn("yuansxmb_id").setEditor(new ComboBox());
		egu.getColumn("yuansxmb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id,mingc  from yuansxmb"));
		// /设置下拉框默认值
		egu.getColumn("yuansxmb_id").setDefaultValue(
				(getYuansxmmcValue()==null)?"":getYuansxmmcValue().getValue());

		
		egu.getColumn("meizb_id").setEditor(new ComboBox());
		egu.getColumn("meizb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from pinzb"));

		// /是否返回下拉框的值或ID
		// egu.getColumn("meikxxb_id").setReturnId(true);
		// egu.getColumn("yunsfsb_id").setReturnId(true);
		// egu.getColumn("yuansxmb_id").setReturnId(true);
		// egu.getColumn("meizb_id").setReturnId(true);

		// ********************工具栏************************************************
		
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

		egu.addTbarText("供应商:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("MeikmcDropDown");
		comb2.setId("gongys");
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(130);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");

		egu.addTbarText("项目名称:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("YuansxmmcDropDown");
		comb3.setId("yuansxm");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());
		
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});gongys.on('select',function(){document.forms[0].submit();});yuansxm.on('select',function(){document.forms[0].submit();});");
		// /设置按钮
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
//				+ "var end = url.indexOf(';');"
//				+ "url = url.substring(0,end);"
//				+ "url = url + '?service=page/Yuansfxreport&lx='+NIANF.getValue()+'&lx='+gongys.getValue()+'&lx='+yuansxm.getValue();"
//				+ " window.open(url,'newWin');";
//		egu.addToolbarItem("{"
//				+ new GridButton("打印", "function (){" + str + "}").getScript()
//				+ "}");
		setExtGrid(egu);
		con.Close();
	}

//	-----电厂tree
	private String treeid;
	private boolean diancFlag=false;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		if(((Visit) getPage().getVisit()).getString2()!=null && !((Visit) getPage().getVisit()).getString2().equals(treeid)){
			diancFlag=true;
		}
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
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			setYuansxmmcValue(null);
			getIYuansxmmcModels();
			getSelectData();
		}
		
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
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
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
			sql = "select id,mingc  from yuansxmb order by mingc";
			_IYuansxmmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IYuansxmmcModel;
	}

	// 矿别名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from gongysb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
}
