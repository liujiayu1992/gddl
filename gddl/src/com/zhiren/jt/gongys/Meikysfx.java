package com.zhiren.jt.gongys;

import java.util.Date;

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
 * 煤矿元素分析
 */

public class Meikysfx extends BasePage implements PageValidateListener {
	
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
	
	private String Meik_id; // 保存从上个页面传过来的煤矿id

	public String getMeikid() {
		return Meik_id;
	}

	public void setMeikid(String id) {
		this.Meik_id = id;
	}
	
	private String Meikmc; // 保存从上个页面传过来的煤矿名称
	
	public String getMeikmc() {
		return Meikmc;
	}

	public void setMeikmc(String meikmc) {
		Meikmc = meikmc;
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	"返回"按钮
	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	
//	"设置元素分析项目"按钮
	private boolean _YuansxmButton = false;
	
	public void YuansxmButton(IRequestCycle cycle) {
		_YuansxmButton = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			cycle.activate("Meikxx");
		}
		if (_YuansxmButton) {
			_YuansxmButton = false;
			cycle.activate("Yuansxm");
		}
	}
	
//	年份下拉框_开始
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	年份下拉框_结束
	
//	项目名称下拉框_开始
	public IDropDownBean getXiangmmcValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getXiangmmcModel().getOptionCount() > 0) {
				setXiangmmcValue((IDropDownBean) getXiangmmcModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setXiangmmcValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
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
		String str = "select id, mingc from yuansxmb where zhuangt = 1 order by mingc";
		setXiangmmcModel(new IDropDownModel(str));
	}
//	项目名称下拉框_结束
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from yuansfxb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into yuansfxb(id, diancxxb_id, meikxxb_id, meizb_id, yunsfsb_id, riq, yuansxmb_id, zhi, zhuangt) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ").append(visit.getDiancxxb_id()).append(", ").append(getMeikid()).append(", ")
				.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(mdrsl.getString("pinzb_id"))).append(", ")
				.append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(mdrsl.getString("yunsfsb_id"))).append(", ")
				.append("to_date('").append(mdrsl.getString("riq")).append("', 'yyyy-mm-dd'), ")
				.append(getXiangmmcValue().getStrId()).append(", '")
				.append(mdrsl.getString("zhi")).append("', ").append(mdrsl.getString("zhuangt")).append(");\n");
			} else {
				sbsql.append("update yuansfxb set")
				.append(" meizb_id = ").append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(mdrsl.getString("pinzb_id")))
				.append(", yunsfsb_id = ").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(mdrsl.getString("yunsfsb_id")))
				.append(", riq = ").append("to_date('").append(mdrsl.getString("riq")).append("', 'yyyy-mm-dd')")
				.append(", zhi = '").append(mdrsl.getString("zhi"))
				.append("' where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql = 
			"select ysfx.id,\n" +
			"       mk.mingc meikxxb_id,\n" + 
			"       pz.mingc pinzb_id,\n" + 
			"       ysfs.mingc yunsfsb_id,\n" + 
			"       ysfx.riq,\n" + 
			"       ysxm.mingc yuansxmb_id,\n" + 
			"       ysfx.zhi,\n" + 
			"       ysfx.zhuangt\n" + 
			"  from yuansfxb ysfx,\n" + 
			"       diancxxb dc,\n" + 
			"       pinzb    pz,\n" + 
			"       yunsfsb  ysfs,\n" + 
			"       yuansxmb ysxm,\n" + 
			"       meikxxb  mk\n" + 
			" where ysfx.meikxxb_id = "+ getMeikid() +"\n" + 
			"   and ysfx.meizb_id = pz.id\n" + 
			"   and ysfx.yunsfsb_id = ysfs.id\n" + 
			"   and ysfx.yuansxmb_id = ysxm.id\n" + 
			"   and ysfx.diancxxb_id = dc.id\n" + 
			"   and ysfx.meikxxb_id = mk.id\n" + 
			"   and to_char(ysfx.riq, 'yyyy') = '"+ getNianfValue().getValue() +"'" +
			"   and ysxm.id = "+ getXiangmmcValue().getStrId() +"\n" ;
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setDefaultValue(getMeikmc());
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("riq").setHeader("启用日期");
		egu.getColumn("yuansxmb_id").setHeader("项目名称");
		egu.getColumn("yuansxmb_id").setEditor(null);
		egu.getColumn("yuansxmb_id").setDefaultValue(getXiangmmcValue().getValue());
		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("zhuangt").setDefaultValue("1");
		
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from pinzb"));
		
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from yunsfsb"));
		
		egu.addTbarText("年份：");
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(80);
		nf_comb.setTransform("Nianf");
		nf_comb.setId("Nianf");
		nf_comb.setLazyRender(true);
		nf_comb.setEditable(true);
		egu.addToolbarItem(nf_comb.getScript());
		egu.addOtherScript("Nianf.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("项目名称：");
		ComboBox xmmc_comb = new ComboBox();
		xmmc_comb.setWidth(80);
		xmmc_comb.setTransform("Xiangmmc");
		xmmc_comb.setId("Xiangmmc");
		xmmc_comb.setLazyRender(true);
		xmmc_comb.setEditable(true);
		egu.addToolbarItem(xmmc_comb.getScript());
		egu.addOtherScript("Xiangmmc.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("元素分析项目维护", "function(){document.getElementById('YuansxmButton').click();\n}"));
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("返回","function(){document.all.ReturnButton.click();}",SysConstant.Btn_Icon_Return));
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
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
			setMeikid(visit.getString9()); // 将从前个页面传过来的煤矿id保存到Meik_id变量中。
			setMeikmc(visit.getString10()); // 将从前个页面传过来的煤矿名称保存到Meikmc变量中。
			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // 项目名称下拉框
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}
}