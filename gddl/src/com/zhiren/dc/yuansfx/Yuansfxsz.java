package com.zhiren.dc.yuansfx;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 类名：元素分析设置
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Yuansfxsz extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	项目名称下拉框_开始
	public IDropDownBean getXiangmmcValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getXiangmmcModel().getOptionCount() > 0) {
				setXiangmmcValue((IDropDownBean) getXiangmmcModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setXiangmmcValue(IDropDownBean LuhValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LuhValue);
	}

	public IPropertySelectionModel getXiangmmcModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getXiangmmcModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setXiangmmcModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getXiangmmcModels() {
		String sql = "select id, mingc from yuansxmb where zhuangt = 1 order by mingc";
		setXiangmmcModel(new IDropDownModel(sql));
	}
//	项目名称下拉框_结束
	
//	品种下拉框_开始
	public IDropDownBean getPinzValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getPinzModel().getOptionCount() > 0) {
				setPinzValue((IDropDownBean) getPinzModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setPinzValue(IDropDownBean LuhValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LuhValue);
	}

	public IPropertySelectionModel getPinzModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			getPinzModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setPinzModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getPinzModels() {
		String sql = "select id, mingc from pinzb order by leib, mingc";
		setPinzModel(new IDropDownModel(sql));
	}
//	品种下拉框_结束
	
//	运输方式下拉框_开始
	public IDropDownBean getYunsfsValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean5() == null) {
			if (getYunsfsModel().getOptionCount() > 0) {
				setYunsfsValue((IDropDownBean) getYunsfsModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean5();
	}

	public void setYunsfsValue(IDropDownBean LuhValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean5(LuhValue);
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			getYunsfsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setYunsfsModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(value);
	}

	public void getYunsfsModels() {
		String sql = "select id, mingc from yunsfsb order by mingc";
		setYunsfsModel(new IDropDownModel(sql));
	}
//	运输方式下拉框_结束
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			
			long meikxxb_id = (getExtGrid().getColumn("meikxxb_id").combo).getBeanId(delrsl.getString("meikxxb_id"));
			long yuansxmb_id = getXiangmmcValue().getId();
			long meizb_id = getPinzValue().getId();
			long yunsfsb_id = getYunsfsValue().getId();
			String riq = DateUtil.FormatOracleDate(delrsl.getString("riq"));
			
			if (visit.isFencb()) {
				String sel_diancid = "select id from diancxxb where fuid = " + visit.getDiancxxb_id();
				ResultSetList dcrsl = con.getResultSetList(sel_diancid);
				while(dcrsl.next()) {
					sbsql.append("delete from yuansfxb where diancxxb_id = ").append(dcrsl.getString("id"))
						.append(" and meikxxb_id = ").append(meikxxb_id).append(" and meizb_id = ").append(meizb_id)
						.append(" and yunsfsb_id = ").append(yunsfsb_id).append(" and riq = ").append(riq)
						.append(" and yuansxmb_id = ").append(yuansxmb_id).append(";\n");
				}
			}
			sbsql.append("delete from yuansfxb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			
			long meikxxb_id = (getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"));
			long yuansxmb_id = getXiangmmcValue().getId();
			long meizb_id = getPinzValue().getId();
			long yunsfsb_id = getYunsfsValue().getId();
			String riq = DateUtil.FormatOracleDate(mdrsl.getString("riq"));
			
			if (visit.isFencb()) {
				String sel_diancid = "select id from diancxxb where fuid = " + visit.getDiancxxb_id();
				ResultSetList dcrsl = con.getResultSetList(sel_diancid);
				while (dcrsl.next()) {
					if ("0".equals(mdrsl.getString("id"))) {
						sbsql.append("insert into yuansfxb(id, diancxxb_id, meikxxb_id, meizb_id, yunsfsb_id, riq, yuansxmb_id, zhi, zhuangt) values(getnewid(")
							.append(dcrsl.getString("id")).append("), ").append(dcrsl.getString("id")).append(", ").append(meikxxb_id)
							.append(", ").append(meizb_id).append(", ").append(yunsfsb_id).append(", ").append(riq)
							.append(", ").append(yuansxmb_id).append(", '").append(mdrsl.getString("zhi"))
							.append("', ").append(getSqlValue(mdrsl.getString("zhuangt"))).append(");\n");
					} else {
						sbsql.append("update yuansfxb set zhi = '").append(mdrsl.getString("zhi")).append("', ")
							.append("riq = ").append(riq).append(", zhuangt = ").append(getSqlValue(mdrsl.getString("zhuangt")))
							.append(" where diancxxb_id = ").append(dcrsl.getString("id"))
							.append(" and meikxxb_id = ").append(meikxxb_id).append(" and meizb_id = ").append(meizb_id)
							.append(" and yunsfsb_id = ").append(yunsfsb_id).append(" and riq = ").append(riq)
							.append(" and yuansxmb_id = ").append(yuansxmb_id).append(";\n");
					}
				}
				dcrsl.close();
			}
			
			if ("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into yuansfxb(id, diancxxb_id, meikxxb_id, meizb_id, yunsfsb_id, riq, yuansxmb_id, zhi, zhuangt) values(getnewid(")
					.append(visit.getDiancxxb_id()).append("), ").append(visit.getDiancxxb_id()).append(", ").append(meikxxb_id)
					.append(", ").append(meizb_id).append(", ").append(yunsfsb_id).append(", ").append(riq)
					.append(", ").append(yuansxmb_id).append(", '").append(mdrsl.getString("zhi"))
					.append("', ").append(getSqlValue(mdrsl.getString("zhuangt"))).append(");\n");
			} else {
				sbsql.append("update yuansfxb set zhi = '").append(mdrsl.getString("zhi")).append("', ")
					.append("riq = ").append(riq).append(", zhuangt = ").append(getSqlValue(mdrsl.getString("zhuangt")))
					.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql = 
			"select decode(ysfx.id, '', 0, ysfx.id) id,\n" +
			"       ysfx.diancxxb_id,\n" + 
			"       mk.mingc as meikxxb_id,\n" + 
			"       decode(ysfx.pinzb_id, '', '"+ getPinzValue().getValue() +"', ysfx.pinzb_id) pinzb_id,\n" + 
			"       decode(ysfx.yunsfsb_id, '', '"+ getYunsfsValue().getValue() +"', ysfx.yunsfsb_id) yunsfsb_id,\n" + 
			"       decode(ysfx.riq, '', to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd'), ysfx.riq) riq,\n" + 
			"       decode(ysfx.yuansxmb_id, '', '"+ getXiangmmcValue().getValue() +"', ysfx.yuansxmb_id) yuansxmb_id,\n" + 
			"       ysfx.zhi,\n" + 
			"       decode(ysfx.zhuangt, '', 1, ysfx.zhuangt) zhuangt\n" + 
			"  from (select fx.id,\n" + 
			"               fx.diancxxb_id,\n" + 
			"               fx.meikxxb_id,\n" + 
			"               pz.mingc as pinzb_id,\n" + 
			"               ys.mingc as yunsfsb_id,\n" + 
			"               fx.riq,\n" + 
			"               xm.mingc as yuansxmb_id,\n" + 
			"               fx.zhi,\n" + 
			"               fx.zhuangt\n" + 
			"          from yuansfxb fx, diancxxb dc, pinzb pz, yunsfsb ys, yuansxmb xm\n" + 
			"         where fx.diancxxb_id = dc.id\n" + 
			"           and fx.meizb_id = pz.id\n" + 
			"           and fx.yunsfsb_id = ys.id\n" + 
			"           and fx.yuansxmb_id = xm.id\n" + 
			"           and fx.diancxxb_id = "+ visit.getDiancxxb_id() +"\n" + 
			"           and fx.yuansxmb_id = "+ getXiangmmcValue().getStrId() +"\n" + 
			"           and fx.meizb_id = "+ getPinzValue().getStrId() +"\n" + 
			"           and fx.yunsfsb_id = "+ getYunsfsValue().getStrId() +"\n" + 
			"         order by fx.meikxxb_id) ysfx,\n" + 
			"       (select distinct mk.id, mk.mingc\n" + 
			"          from meikxxb mk, gongysmkglb gm\n" + 
			"         where gm.meikxxb_id = mk.id\n" + 
			"         order by mk.mingc) mk\n" + 
			" where ysfx.meikxxb_id(+) = mk.id\n" + 
			" order by mk.mingc";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("diancxxb_id").setHeader("电厂ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("meikxxb_id").setWidth(180);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("riq").setHeader("启用日期");
		egu.getColumn("yuansxmb_id").setHeader("项目名称");
		egu.getColumn("yuansxmb_id").setEditor(null);
		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setHidden(true);
		
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select distinct mk.id, mk.mingc from meikxxb mk, gongysmkglb gm where gm.meikxxb_id = mk.id  order by mk.mingc"));
		egu.getColumn("meikxxb_id").setEditor(null);
		
		egu.addTbarText("项目名称：");
		ComboBox xiangmc = new ComboBox();
		xiangmc.setWidth(80);
		xiangmc.setTransform("Xiangmmc");
		xiangmc.setId("Xiangmmc");
		xiangmc.setLazyRender(true);
		xiangmc.setEditable(false);
		egu.addToolbarItem(xiangmc.getScript());
//		egu.addOtherScript("Xiangmmc.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("品种：");
		ComboBox pinz = new ComboBox();
		pinz.setWidth(80);
		pinz.setTransform("Pinz");
		pinz.setId("Pinz");
		pinz.setLazyRender(true);
		pinz.setEditable(false);
		egu.addToolbarItem(pinz.getScript());
//		egu.addOtherScript("pinz.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("运输方式：");
		ComboBox yunsfs = new ComboBox();
		yunsfs.setWidth(80);
		yunsfs.setTransform("Yunsfs");
		yunsfs.setId("Yunsfs");
		yunsfs.setLazyRender(true);
		yunsfs.setEditable(false);
		egu.addToolbarItem(yunsfs.getScript());
//		egu.addOtherScript("Yunsfs.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		String condition = 
			"var newMrcd = gridDiv_ds.getModifiedRecords();\n" +
			"for(var i = 0; i< newMrcd.length; i++){\n" + 
			"    if(newMrcd[i].get('ZHI') == '' || newMrcd[i].get('ZHI') == null){\n" + 
			"        Ext.MessageBox.alert('提示信息','字段 值 不能为空');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"}";
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton", condition);
		egu.addTbarText("-");
		
		String str = 
			"var url = 'http://'+document.location.host+document.location.pathname;\n" +
			"var end = url.indexOf(';');\n" + 
			"url = url.substring(0,end);\n" + 
			"url = url + '?service=page/YuansfxszReport&lx='+Xiangmmc.getValue()+'&lx='+Pinz.getValue()+'&lx='+Yunsfs.getValue();\n" + 
			"window.open(url,'newWin');";
		egu.addToolbarItem("{" + new GridButton("打印", "function (){" + str + "}", SysConstant.Btn_Icon_Print).getScript() + "}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 如果在页面上取到的值为空或是空串，那么向数据库保存0
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel3(null); // 项目名称下拉框
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null); // 品种下拉框
			visit.setDropDownBean4(null);
			visit.setProSelectionModel5(null); // 运输方式下拉框
			visit.setDropDownBean5(null);
		}
		getSelectData();
	}

}
