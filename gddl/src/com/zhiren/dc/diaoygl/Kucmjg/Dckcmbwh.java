package com.zhiren.dc.diaoygl.Kucmjg;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
 
public  class Dckcmbwh extends BasePage implements PageValidateListener {
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
	
	private void copy(){
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String sqlchk="SELECT GETNEWID(1),DIANCXXB_ID,DATE'"+strDate+"',ZHI FROM DCYKKCMB WHERE RIQ=ADD_MONTHS(DATE'"+strDate+"',-1)";
//		如果上月无数据则不复制
		if(!con.getHasIt(sqlchk)){
			setMsg("上月无数据");
			con.Close();
			return;
		}
		
		String sql = "";
//		//删除本月数据
		sql = "delete DCYKKCMB where riq=DATE'"+strDate+"'";
		int flag = con.getDelete(sql);
		if(flag==-1){
			setMsg("本月数据删除失败");
			con.Close();
			return;
		}
		//复制上月数据
		sql = 
			"INSERT INTO DCYKKCMB\n" +
			"  (ID, DIANCXXB_ID, RIQ, ZHI)\n" + 
			"  (SELECT GETNEWID(1), DIANCXXB_ID, DATE '"+strDate+"', ZHI\n" + 
			"     FROM DCYKKCMB\n" + 
			"    WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1))";
		
		flag = con.getUpdate(sql);
		if(flag==-1)
			setMsg("复制上月数据失败");
		else
			setMsg("复制上月数据已完成");
		
		con.Close();
	}

	private void Save()	{
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();

//		保存更新或新增 
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");
		while (rsl.next()) {
			String ID_1=rsl.getString("ID");
			double ZHI=rsl.getDouble("ZHI");
			sql.append("update DCYKKCMB set ZHI="+ ZHI+" where id=" + ID_1+";\n");
		}
		sql.append("end;");
		int flag=0;
		if(sql.length()>11){
			flag=con.getUpdate(sql.toString());
		}
		
		if (flag>-1){
			setMsg("保存成功！");
		}else{
			setMsg("保存失败！");
		}
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _CopyChick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_RefurbishChick){
			_RefurbishChick=false;
			getSelectData();
		}
		if(_CopyChick){
			_CopyChick=false;
			copy();
			getSelectData();
		}
	}
	
	public void createdate(String date){
		JDBCcon con = new JDBCcon();
		String sql=
			"INSERT INTO DCYKKCMB\n" +
			"  (ID, DIANCXXB_ID, RIQ, ZHI)\n" + 
			"  (SELECT GETNEWID(1), ID, DATE '"+date+"', 0 FROM DIANCXXB WHERE JIB = 3)";
		con.getInsert(sql);
		con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		// 工具栏的年份下拉框
		long intyear;
	
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
//		 工具栏的月份下拉框
		long intmonth;
		if (getYuefValue()==null){
			intmonth =DateUtil.getMonth(new Date());
		}else{
			intmonth= getYuefValue().getId();
		}
		
		String  chaxun = 
	    	"SELECT K.ID, DC.MINGC DIANCXXB_ID, K.RIQ, K.ZHI\n" +
	    	"  FROM DCYKKCMB K, DIANCXXB DC\n" + 
	    	" WHERE K.DIANCXXB_ID = DC.ID\n" + 
	    	"   AND RIQ = DATE '"+intyear+"-"+intmonth+"-01' ORDER BY DC.XUH";
//		如果所选月份没有数据，则需对数据进行初始化操作。
		if(!con.getHasIt(chaxun)){
			createdate(intyear+"-"+intmonth+"-01");
		}
		
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("DCYKKCMB");
		egu.setWidth("bodyWidth");
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("ID").setEditor(null);
		
		egu.getColumn("DIANCXXB_ID").setHeader("单位名称");
		egu.getColumn("DIANCXXB_ID").setWidth(120);
		egu.getColumn("DIANCXXB_ID").setEditor(null);
		
		egu.getColumn("RIQ").setHeader("日期");
		egu.getColumn("RIQ").setWidth(100);
		egu.getColumn("RIQ").setEditor(null);
		
		egu.getColumn("ZHI").setHeader("预控目标值<br>(吨)");
		egu.getColumn("ZHI").editor.setAllowBlank(false);
		egu.getColumn("ZHI").setWidth(120);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(23);// 设置分页
	
		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(60);
		egu.addToolbarItem(comb2.getScript());
		
		egu.addToolbarButton("刷新", GridButton.ButtonType_Refresh, "RefurbishButton");
//					
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		
//		String copyscript = "{"+ //复制按钮
//	    new GridButton("复制","function(){document.getElementById('CopyButton').click();\n"
//	    		,SysConstant.Btn_Icon_Copy).getScript()+  "}}";
//		egu.addToolbarItem(copyscript);

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
		if (getExtGrid() == null) {
			return "";
		}
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();
			setTbmsg(null); 
		}
		getSelectData();
	}
//	 年份
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
//				判断如果当前是1月份，年份为去年
				int year = DateUtil.getYear(new Date());
				if(DateUtil.getMonth(new Date())==1){
					year -= 1;
				}
//				-------
				if (year == ((IDropDownBean) obj).getId()) {
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
		for (i = 2013; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	/**
	 * 月份
	 */
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	 
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
}