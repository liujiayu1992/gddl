package com.zhiren.dc.monthReport;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：hc
 * 时间：2014-3-12
 * 描述：新增。
 */

public  class Zickjsjwh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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

	private void Shenc() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String cxsql = "select distinct d.diancxxb_id,nvl(y.RLBMDJBNJH,0)RLBMDJBNJH,nvl(y.RLBMLBNJH,0)RLBMLBNJH\n"
				+ "from (select * from zickjsjb where riq=add_months(to_date('"
				+ strDate
				+ "','yyyy-mm-dd'),-1)) y,yuebdwb d \n"
				+ "where y.diancxxb_id(+)=d.diancxxb_id and d.zhuangt=1 and fuid<>0";
		ResultSetList cxrsl = con.getResultSetList(cxsql);
		StringBuffer sql = new StringBuffer("begin\n");
		while (cxrsl.next()) {
			sql
					.append(
							"insert into zickjsjb (id,riq,diancxxb_id,RLBMDJBNJH,RLBMLBNJH)")
					.append(
							"values(getnewid(" + visit.getDiancxxb_id()
									+ "),to_date('" + strDate
									+ "','yyyy-mm-dd'),").append(
							cxrsl.getLong("diancxxb_id")).append(",").append(
							cxrsl.getDouble("RLBMDJBNJH")).append(",").append(
							cxrsl.getDouble("RLBMLBNJH")).append(");\n");
		}

		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			setMsg("保存失败！");
		} else {
			setMsg("保存成功！");
		}
		con.Close();
	}

	private void Shanc() {
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String scsql = "delete from zickjsjb where riq=date'" + strDate + "'";
		int flag = con.getDelete(scsql);
		if (flag == -1) {
			setMsg("删除失败！");
		} else {
			setMsg("删除成功！");
		}
		con.Close();
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin\n");

		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update zickjsjb set \n")
					.append("RLZHBMDJ = ").append(mdrsl.getDouble("RLZHBMDJ"))
					.append(",RLBML = ").append(mdrsl.getDouble("RLBML"))
					.append(",rlbmdjbnjh=").append(mdrsl.getDouble("rlbmdjbnjh"))
					.append(",rlbmlbnjh=").append(mdrsl.getDouble("rlbmlbnjh"))
					.append(" where id = ").append(mdrsl.getString("id"))
					.append(";\n");
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag == -1) {
			setMsg("保存失败！");
		} else {
			setMsg("保存成功！");
		}
		mdrsl.close();
		con.Close();
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _ShencChick = false;

	public void ShencButton(IRequestCycle cycle) {
		_ShencChick = true;
	}

	private boolean _ShancChick = false;

	public void ShancButton(IRequestCycle cycle) {
		_ShancChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_ShencChick) {
			_ShencChick = false;
			Shenc();
		}
		if (_ShancChick) {
			_ShancChick = false;
			Shanc();
		}
		getSelectData();
	}
	
	public void getSelectData() {
		String strDate = getNianfValue().getValue() + "-"
		+ getYuefValue().getValue() + "-01";
		String sDate = getNianfValue().getValue() + "-"
		+ getYuefValue().getValue();
		JDBCcon con = new JDBCcon();
		String sql = "select y.id,to_char(y.riq,'yyyy-mm-dd')riq,\n" 
				+ "d.mingc diancxxb_id,\n"
				+ "round(y.RLZHBMDJ,2)RLZHBMDJ,round(y.RLBML,0)RLBML,\n" 
				+ "round(y.rlbmdjbnjh,2)rlbmdjbnjh,\n"
				+ "round(y.rlbmlbnjh,0)rlbmlbnjh\n"
				+ "from zickjsjb y,yuebdwb d\n"
				+ "where y.diancxxb_id=d.diancxxb_id and y.riq=date'"
				+ strDate
				+ "'\n"
				+ "order by y.riq,d.mingc";
		ResultSetList rsl = con.getResultSetList(sql);
		boolean a = false;
		if (rsl.next()) {
			a = true;
		}
		rsl.beforefirst();

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth("bodyWidth");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(23);// 设置分页
		egu.setTableName("yuebqtsjb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("riq").setCenterHeader("日期");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setCenterHeader("电厂名称");
		egu.getColumn("diancxxb_id").setEditor(null);

		egu.getColumn("RLZHBMDJ").setCenterHeader("入炉综合<br>标煤单价");
		egu.getColumn("RLZHBMDJ").setWidth(80);
		egu.getColumn("RLZHBMDJ").editor.setMinValue("0");
		((NumberField) egu.getColumn("RLZHBMDJ").editor).setDecimalPrecision(2);
		egu.getColumn("RLBML").setCenterHeader("入炉标煤量");
		egu.getColumn("RLBML").setWidth(80);
		egu.getColumn("RLBML").editor.setMinValue("0");
		((NumberField) egu.getColumn("RLBML").editor).setDecimalPrecision(0);
		egu.getColumn("rlbmdjbnjh").setCenterHeader("入炉标煤<br>单价本年计划");
		egu.getColumn("rlbmdjbnjh").setWidth(90);
		egu.getColumn("rlbmdjbnjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmdjbnjh").editor)
				.setDecimalPrecision(2);
		egu.getColumn("rlbmlbnjh").setCenterHeader("入炉标<br>煤量本年计划");
		egu.getColumn("rlbmlbnjh").setWidth(90);
		egu.getColumn("rlbmlbnjh").editor.setMinValue("0");
		((NumberField) egu.getColumn("rlbmlbnjh").editor).setDecimalPrecision(0);

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符

//		 设定工具栏下拉框自动刷新
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		if(!a){
			String handler = "function (){Ext.MessageBox.confirm('提示信息','是否生成"+sDate+"月所有数据？',function(btn){if(btn == 'yes'){" +
			"document.getElementById('ShencButton').click();"+
			"Ext.MessageBox.show({msg:'正在处理数据,请稍后...'," +
			"progressText:'处理中...',width:300,wait:true,waitConfig: " +
			"{interval:200},icon:Ext.MessageBox.INFO});} // end if \n});}";
			GridButton cpy = new GridButton("生成", handler);
			cpy.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(cpy);
		}
		if (a) {
			String shanc = "function (){Ext.MessageBox.confirm('提示信息','是否删除"+sDate+"月所有数据？',function(btn){if(btn == 'yes'){" +
			"document.getElementById('ShancButton').click();"+
			"Ext.MessageBox.show({msg:'正在处理数据,请稍后...'," +
			"progressText:'处理中...',width:300,wait:true,waitConfig: " +
			"{interval:200},icon:Ext.MessageBox.INFO});} // end if \n});}";
			GridButton shc = new GridButton("删除", shanc);
			shc.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(shc);
		}
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
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=200 scrollamount=2>" + getTbmsg()+ "</marquee>'");
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.setMsg(null);
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 12) {
				_nianf = _nianf + 1;
			}
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
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
		for (i = 2009; i <= DateUtil.getYear(new Date()) + 1; i++) {
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

		public IDropDownBean getYuefValue() {
			if (_YuefValue == null) {
				int _yuef = DateUtil.getMonth(new Date());
				if (_yuef == 12) {
					_yuef = 1;
				} else {
					_yuef = _yuef + 1;
				}
				for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
					Object obj = _YuefModel.getOption(i);
					if (_yuef == ((IDropDownBean) obj).getId()) {
						_YuefValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _YuefValue;
		}

		public void setYuefValue(IDropDownBean Value) {
			long id = -2;
			if (_YuefValue != null) {
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