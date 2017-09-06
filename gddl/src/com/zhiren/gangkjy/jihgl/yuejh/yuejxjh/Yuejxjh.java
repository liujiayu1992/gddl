package com.zhiren.gangkjy.jihgl.yuejh.yuejxjh;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @version
 * @author 张琦
 *
 */

public class Yuejxjh extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
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
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}

	}
	private void Save(){
		if(getChange() == null || "".equals(getChange())){
			setMsg("error,修改记录为空！");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag =0;
		con.setAutoCommit(false);
//		StringBuffer sql = new StringBuffer("begin \n");
		
		String sqldel ="";
		//删除
		ResultSetList rsldel = getExtGrid().getDeleteResultSet(getChange());
		while (rsldel.next()) {
			sqldel ="delete from yuejxjhb where id = "+rsldel.getString(0)+";\n";
		}
		
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		
		long pinzb_id=0;
		long gongysb_id = 0;
		long faz_id = 0;
		long daoz_id = 0;
		
		String sql = "begin\n";
		sql+=sqldel;
		
		while (rsl.next()) {
		
			pinzb_id=(getExtGrid().getColumn("PINZB_ID").combo).getBeanId(rsl.getString("pinzb_id"));
			gongysb_id = (getExtGrid().getColumn("GONGYSB_ID").combo).getBeanId(rsl.getString("gongysb_id"));
			faz_id	=(getExtGrid().getColumn("FAZ_ID").combo).getBeanId(rsl.getString("faz_id"));
			daoz_id	=(getExtGrid().getColumn("DAOZ_ID").combo).getBeanId(rsl.getString("daoz_id"));
				if("0".equals(rsl.getString("ID"))){
					long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));

					sql += "insert into yuejxjhb(id,diancxxb_id,riq,gongysb_id,faz_id,pinzb_id,daoz_id,lies,ches,duns,beiz) values(" + new_id + ","
					+ v.getDiancxxb_id()+",to_date('"+rsl.getString("riq")+"','yyyy-mm-dd'),"
					+ gongysb_id+","+faz_id+","+pinzb_id+","+daoz_id+","+rsl.getString("lies")+","
					+ rsl.getString("ches")+","+rsl.getString("duns")+",'"
					+ rsl.getString("beiz")+"');\n";
				}else{
					int id = rsl.getInt("id");
					sql += "update yuejxjhb set "
						+ " riq = " +"to_date('"+rsl.getString("riq")+"','yyyy-mm-dd'),"
						+ ",gongysb_id = " + gongysb_id
						+ ", faz_id='" + faz_id
						+ "',pinzb_id = " + pinzb_id
						+ "',daoz_id = " + daoz_id
						+ "',lies = " + rsl.getString("lies")
						+ "',ches = " + rsl.getString("ches")
						+ "',duns = " + rsl.getString("duns")
						+ ",beiz = '" + rsl.getString("beiz")
						+ "' where id = " + id + ";\n";
				}
		}
		if(rsl.getRows()>0 || rsldel.getRows()>0){
			sql += "end;\n";
			flag = con.getUpdate(sql);
//			if (flag == -1) {
//				con.rollBack();
//				con.Close();
//				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
//						+ sql);
//				setMsg(ErrorMessage.DeleteDatabaseFail);
//				return;
//			}int flag=con.getUpdate(sql.toString());
			
			if(flag>=0){
				this.msg="Ext.Msg.alert('提示信息',' 数据更新成功!')";
			}else{
				this.msg="Ext.Msg.alert('提示信息',' 数据更新失败!')";
			}
//			if (flag !=-1){
//				setMsg("保存成功！");
//			}
		}
		rsldel.close();
		rsl.close();
		con.Close();
		getSelectData();
		
	}
	public void getSelectData(){
		
		 int month=Integer.parseInt(getYuefValue().getValue());
			
			String yuef="";
			
			if(month<10){
				yuef="0"+month;
			}else{
				yuef=""+month;
			}
			
		Visit visit = (Visit) getPage().getVisit();
		String sql = "";
		JDBCcon con = new JDBCcon();
//		 工具栏的年份和月份下拉框
		long intyear;
		String StrMonth = "";
		long intMonth;
		long nextMonth;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else { 
			intMonth = getYuefValue().getId();
		}
		String strdate;
		String strdate1;
		String monthstr;
		//当月份小于12月时，得到当月第一天和下个月第一天的值。
		//当月份等于12月时，得到12月第一天和最后一天的值。
		if(intMonth <12){
			if (intMonth < 9) {

				StrMonth = "0" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "0" + nextMonth;
			}else if(intMonth == 9){
				StrMonth = "0"+ intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
				
			}else {
				StrMonth = "" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
			}
			strdate = intyear+"-"+StrMonth+"-01";
			strdate1 = intyear + "-" +monthstr +"-01";
			
		}else{
			strdate = intyear+"-"+intMonth+"-01";
			strdate1 = (intyear+1) +"-01-01";
		}
	
		
		sql = 	"select  y.id id,\n" +
			"       y.riq riq,\n" + 
			"       f.mingc gongysb_id,\n" + 
			"       c.quanc faz_id,\n" + 
			"       (select distinct quanc from vwchez v where y.daoz_id = v.id) daoz_id,\n" + 
			"       p.mingc pinzb_id,\n" + 
			"       y.lies lies,\n" + 
			"       y.ches ches,\n" + 
			"       y.duns duns,\n" + 
			"       y.beiz beiz\n" + 
			"  from yuejxjhb y, vwfahr f, vwchez c, diancxxb d, vwpinz p\n" + 
			" where y.diancxxb_id = d.id\n" + 
			"   and y.faz_id = c.id\n" + 
			"   and y.pinzb_id = p.id  and y.gongysb_id = f.id \n" +
			"and y.riq >= to_date('"+strdate+"', 'yyyy-mm-dd')\n" +
			"and y.riq < to_date('"+strdate1+"', 'yyyy-mm-dd')";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yuejxjhb");
		egu.getColumn("id").setHidden(true);
		//设置默认日期
	//	String riq = DateUtil.FormatDate(new Date());
		egu.getColumn("riq").setHeader(Local.riq);
		egu.getColumn("riq").setDefaultValue(getNianfValue()+"-"+ yuef+"-01");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("gongysb_id").setHeader(Local.fahr);
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("faz_id").setHeader(Local.faz);
		egu.getColumn("faz_id").setWidth(80);
		egu.getColumn("daoz_id").setHeader(Local.daoz);
		egu.getColumn("daoz_id").setWidth(80);
		egu.getColumn("pinzb_id").setHeader(Local.pinz);
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("lies").setHeader(Local.lies);
	    egu.getColumn("lies").setDefaultValue("0");
		egu.getColumn("lies").setWidth(60);
		egu.getColumn("ches").setHeader(Local.ches);
		egu.getColumn("ches").setDefaultValue("0");
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("duns").setHeader(Local.duns);
		egu.getColumn("duns").setDefaultValue("0");
		egu.getColumn("duns").setWidth(70);
		egu.getColumn("beiz").setHeader(Local.beiz);
		egu.getColumn("beiz").setWidth(120);
		// 设置发货下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c1);
		c1.setEditable(true);
		String fahSql = "select id,mingc from vwfahr  order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(fahSql));
		// 设置到站下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("faz_id").setEditor(c2);
		c2.setEditable(true);
		String fazSql = "select id ,mingc from vwchez order by mingc";

		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		// 设置到站下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c3);
		c3.setEditable(true);
		String daozSql = "select id ,mingc from vwchez order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		// 设置到站下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c4);
		c4.setEditable(true);
		String pinzSql = "select id,mingc from pinzb where leib='煤'order by mingc asc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		
//		 工具栏
		egu.addTbarText("年份:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("月份:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		setExtGrid(egu);
		rsl.close();
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

//	日期控件
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
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
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

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;


	public IDropDownBean getYuefValue(){
		if (_YuefValue == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
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