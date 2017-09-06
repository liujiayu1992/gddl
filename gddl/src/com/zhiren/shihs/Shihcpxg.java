package com.zhiren.shihs;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihcpxg extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

//	 绑定日期
	private String riqi;

	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
			this.riqi = riqi;
	}

	private String riq2;

	public String getRiq2() {
		return riq2;
	}

	public void setRiq2(String riq2) {
			this.riq2 = riq2;
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
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String count = " ";
		StringBuffer sb = new StringBuffer();
		sb.append("select count(cp.id) cs from shihcpb cp,shihgysb gys,shihpzb pz,chebb cb,yunsdwb dw ")
		.append("where gys.id = cp.gongysb_id ").append("and pz.id = cp.shihpzb_id ")
		.append("and cb.id = cp.chebb_id ").append("and dw.id = cp.yunsdwb_id ")
		.append("and cp.daohrq>=to_date('").append(getRiqi()).append("','yyyy-mm-dd') ")
		.append("and cp.daohrq<to_date('").append(getRiq2()).append("','yyyy-mm-dd')+1 ");
		ResultSetList rslc = con.getResultSetList(sb.toString());
		if(rslc.next()){
			if(rslc.getInt("cs")>500){
				count+="and rownum <= 500 ";
				setMsg("您查询的记录数过多，会造成程序运行速度缓慢，<br>系统已经自动为您截取了前500条记录。");
			}
		}
		
		ResultSetList rsl = con
				.getResultSetList("select cp.id,cp.diancxxb_id,cp.xuh,cp.fahrq,cp.daohrq, " 
						+ " gys.mingc as gongysb_id,pz.mingc as shihpzb_id,"
						+ " cp.cheph,cp.piaojh,cp.yuanmz,cp.maoz,cp.piz,cp.biaoz,cp.yingk,cp.yuns,cp.koud,cp.ches,cp.jianjfs, "
						+ " cp.chebb_id,cp.yuanghdw,dw.mingc as yunsdwb_id,"
						+ " cp.qingcsj,cp.qingchh,cp.qingcjjy,cp.zhongcsj,cp.zhongchh,cp.zhongcjjy," 
//						+ " cp.hedbz,cp.lursj,"
						+ " cp.lury,cp.beiz "
//						+ " cp.shihhtb_id,cp.shihcyb_id,cp.shihjsb_id,cp.shenhr,cp.shenhsj,cp.shenhzt "
						+ " from shihcpb cp,shihgysb gys,shihpzb pz,chebb cb,yunsdwb dw "
						+ " where gys.id = cp.gongysb_id and pz.id = cp.shihpzb_id and cb.id = cp.chebb_id and dw.id = cp.yunsdwb_id "
						+ " and cp.daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and cp.daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
						+ count
						+ " order by cp.daohrq,cp.xuh");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("shihcpb");
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(v.getDiancxxb_id()));
		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("gongysb_id").setWidth(70);
		egu.getColumn("shihpzb_id").setHeader("品种");
		egu.getColumn("shihpzb_id").setWidth(70);
		egu.getColumn("cheph").setHeader("车皮号");
		egu.getColumn("cheph").setWidth(70);
		egu.getColumn("piaojh").setHeader("票据号");
		egu.getColumn("piaojh").setHidden(true);
		egu.getColumn("piaojh").setWidth(70);
		egu.getColumn("yuanmz").setHeader("原毛重");
		egu.getColumn("yuanmz").setWidth(60);
		egu.getColumn("yuanmz").setHidden(true);
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingk").setHeader("盈亏");
		egu.getColumn("yingk").setWidth(50);
		egu.getColumn("yuns").setHeader("运损");
		egu.getColumn("yuns").setWidth(50);
		egu.getColumn("koud").setHeader("扣顿");
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("ches").setHeader("车速");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").setHidden(true);
		egu.getColumn("jianjfs").setHeader("检斤方式");
		egu.getColumn("jianjfs").setWidth(65);
		egu.getColumn("chebb_id").setHeader("车别");
		egu.getColumn("chebb_id").setWidth(50);
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("yuanghdw").setHeader("原供货单位");
		egu.getColumn("yuanghdw").setWidth(70);
		egu.getColumn("yuanghdw").setHidden(true);
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setWidth(70);
		
		egu.getColumn("qingcsj").setHeader("轻车时间");
		egu.getColumn("qingcsj").setWidth(70);
		egu.getColumn("qingcsj").setHidden(true);
		egu.getColumn("qingchh").setHeader("轻车衡号");
		egu.getColumn("qingchh").setWidth(70);
		egu.getColumn("qingchh").setHidden(true);
		egu.getColumn("qingcjjy").setHeader("轻车检斤员");
		egu.getColumn("qingcjjy").setWidth(70);
		egu.getColumn("qingcjjy").setHidden(true);
		
		egu.getColumn("zhongcsj").setHeader("重车时间");
		egu.getColumn("zhongcsj").setWidth(70);
		egu.getColumn("zhongcsj").setHidden(true);
		egu.getColumn("zhongchh").setHeader("重车衡号");
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("zhongchh").setHidden(true);
		egu.getColumn("zhongcjjy").setHeader("重车检斤员");
		egu.getColumn("zhongcjjy").setWidth(70);
		egu.getColumn("zhongcjjy").setHidden(true);
//		egu.getColumn("hedbz").setHeader("核对标志");
//		egu.getColumn("hedbz").setWidth(65);
//		egu.getColumn("lursj").setHeader("录入时间");
//		egu.getColumn("lursj").setWidth(70);
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setWidth(50);
		egu.getColumn("lury").setDefaultValue(v.getRenymc());
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(65);
//		egu.getColumn("shihhtb_id").setHeader("SHIHHTB_ID");
//		egu.getColumn("shihhtb_id").setHidden(true);
//		egu.getColumn("shihhtb_id").setWidth(70);
//		egu.getColumn("shihcyb_id").setHeader("SHIHCYB_ID");
//		egu.getColumn("shihcyb_id").setHidden(true);
//		egu.getColumn("shihcyb_id").setWidth(70);
//		egu.getColumn("shihjsb_id").setHeader("SHIHJSB_ID");
//		egu.getColumn("shihjsb_id").setHidden(true);
//		egu.getColumn("shihjsb_id").setWidth(70);
//		egu.getColumn("shenhr").setHeader("审核人");
//		egu.getColumn("shenhr").setWidth(50);
//		egu.getColumn("shenhsj").setHeader("审核时间");
//		egu.getColumn("shenhsj").setWidth(70);
//		egu.getColumn("shenhzt").setHeader("审核状态");
//		egu.getColumn("shenhzt").setWidth(65);
		
//		到货日期查询
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
//		 设置供应商下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c1);
		c1.setEditable(true);
		String gysSql = "select id,mingc from shihgysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gysSql));
//		 设置品种下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("shihpzb_id").setEditor(c2);
		c2.setEditable(true);
		String pzSql = "select id,mingc from shihpzb order by mingc";
		egu.getColumn("shihpzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pzSql));
//		 设置车别下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c3);
		c3.setEditable(true);
		String cbSql = "select id,mingc from chebb order by mingc";
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cbSql));
//		 设置运输单位下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(c4);
		c4.setEditable(true);
		String ysdwSql = "select id,mingc from yunsdwb order by mingc";
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ysdwSql));
////		 设置审核状态下拉框
//		List ls = new ArrayList();
//		ls.add(new IDropDownBean(1, "已审核"));
//		ls.add(new IDropDownBean(2, "未审核"));
//		ComboBox c5 = new ComboBox();
//		egu.getColumn("shenhzt").setEditor(c5);
//		c5.setEditable(true);
//		egu.getColumn("shenhzt").setComboEditor(egu.gridId,
//				new IDropDownModel(ls));
//		egu.getColumn("shenhzt").setDefaultValue("未审核");
//		egu.getColumn("shenhzt").returnId = true;
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		StringBuffer sb1 = new StringBuffer();
		sb1.append("gridDiv_grid.on('afteredit', function(e){\n" +
				"	if (e.field=='MAOZ'||e.field=='PIZ') {\n" +
				"		var MAOZ=eval(gridDiv_ds.getAt(e.row).get('MAOZ')||0);\n" +
				"		var PIZ=eval(gridDiv_ds.getAt(e.row).get('PIZ')||0);\n" +
				"		var BIAOZ=Round_new(MAOZ-PIZ, 2);\n" +
				"		if (BIAOZ<0) {\n" +
				"			Ext.MessageBox.alert('提示信息','毛重小于皮重！');" +
				"			BIAOZ=0;\n" +
				"		}\n" +
				"		gridDiv_ds.getAt(e.row).set('BIAOZ', BIAOZ);\n" +
				"		gridDiv_ds.getAt(e.row).set('YUANMZ', MAOZ);\n" +
				"	}\n" +
				"});\n");
		egu.addOtherScript(sb1.toString());
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiqi(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
	}
}
