package com.zhiren.jt.het.diancgmht;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.Liuc;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.jt.het.hetmb.Fahxxbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.jt.het.hetmb.Zengkkbean;

/**
 * @author caolin2
 * 
 */
/*
 * 作者：王磊
 * 时间：2009-05-20
 * 描述：	修改增扣款中扣价数据为BigDecimal类型解决 
 * 		小数位多余4位时自动使用科学计数法显示的问题
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
/**
 * @author yangzl 修改时间：2010-3-19 描述： 修改合同复制，选择多个合同
 */
/**
 * @author licj 修改时间：2010-10-14 描述： 选择需方时增加外二的特例
 */
/*
 * 作者：夏峥
 * 时间：2013-06-27
 * 描述：在数据库中增加合同编号的唯一键索引，并在保存合同号失败时给出保存失败的提示框
 */
public class Diancgmht extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	public boolean isHetbl() {
		return ((Visit) getPage().getVisit()).getboolean8();
	}

	public void setHetbl(boolean value) {
		((Visit) getPage().getVisit()).setboolean8(value);
	}
	
//	public String getHetbl(String value) {
//		return ((Visit) getPage().getVisit()).getString10();
//	}
//	
//	public void setHetbl(String value) {
//		((Visit) getPage().getVisit()).setString10(value);
//	}
	
	private String zafTab = "";
	
	public String getZafTab() {
		return zafTab;
	}

	public void setZafTab(String zafTab) {
		this.zafTab = zafTab;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	private String getDanwglStr() {
		;// 指标单位关联数组
		return ((Visit) getPage().getVisit()).getString3();
	}

	private void setDanwglStr(String value) {
		;// 指标单位关联数组
		((Visit) getPage().getVisit()).setString3(value);
	}

	public String getpageLink() {
		return " var context='" + MainGlobal.getHomeContext(this) + "';"
				+ getDanwglStr();
	}

	public void setTabbarSelect(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}

	public int getTabbarSelect() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public boolean isXinjht() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setXinjht(boolean value) {
		((Visit) getPage().getVisit()).setboolean5(value);
	}

	// 功能区 新建模板 打开 保存 选择流程 模板名称
	// 模板名称下拉框
	public IDropDownBean getmobmcSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getmobmcSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setmobmcSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean1() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean1().getId()) {
				((Visit) getPage().getVisit()).setboolean6(true);
			} else {
				((Visit) getPage().getVisit()).setboolean6(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public void setmobmcSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getmobmcSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getmobmcSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getmobmcSelectModels() {

		String sql = "";

		if (this.isTijsh()) {
			sql = "select id,mingc " + "from hetb_mb "
					+ "where diancxxb_id in(" + "select id from diancxxb\n"
					+ "start with id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ "connect by prior fuid= id)" + " and nvl(liucztb_id,0)=1  order by  hetb_mb.mingc";
		} else {
			sql = "select id,mingc " + "from hetb_mb "
					+ "where diancxxb_id in(" + "select id from diancxxb\n"
					+ "start with id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ "connect by prior fuid= id) order by  hetb_mb.mingc ";
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, ""));
		return;
	}

	// 合同
	public IDropDownBean gethetSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) gethetSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void sethetSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean2() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean2().getId()) {
				((Visit) getPage().getVisit()).setboolean1(true);
			} else {
				((Visit) getPage().getVisit()).setboolean1(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void sethetSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel gethetSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			gethetSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	
	public void gethetSelectModels(){
		String sql="";
		if (((Visit) getPage().getVisit()).getString4().equals("XS")) {

			sql = "select hetb.id,hetb.hetbh\n"
					+ "       from hetb,diancxxb\n"
					+ "       where hetb.diancxxb_id=diancxxb.id\n"
					+ "             and (leib=0 or leib=1)\n"
					+ "             and hetb.fuid=0\n"
					+ "             and liucztb_id=0\n"
					+ "             and to_char(hetb.qiandrq,'YYYY')='"	+ getNianfValue().getId() + "'\n"
					+ "				and to_char(hetb.qiandrq,'MM')=" + getYuefValue().getValue() + "\n"
					+ "             and (diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ "					or diancxxb.fuid=" + ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ "					or diancxxb.id in(select fuid from diancxxb where id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ")) order by hetbh";

		} else if (((Visit) getPage().getVisit()).getString4().equals("CG")) {
			// 分公司对煤矿的合同
			sql = "select hetb.id,hetb.hetbh\n"
					+ "       from hetb,diancxxb\n"
					+ "       where hetb.diancxxb_id=diancxxb.id\n"
					+ "             and leib=2\n"
					+ "             and hetb.fuid=0\n"
					+ "             and liucztb_id=0\n"
					+ "             and to_char(hetb.qiandrq,'YYYY')='" + getNianfValue().getId() + "'\n"
					+ "				and to_char(hetb.qiandrq,'MM')=" + getYuefValue().getValue() + "\n"
					+ "             and (diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ "					or diancxxb.fuid=" + ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ ") order by hetbh";
		}

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "新建合同"));
	}

//	public void gethetSelectModels() {
//		// 合同选择（选择已有合同）
//		String sql = "";
//
//		if (value != null && !value.equals("") && !value.equals("fanhui")) {
//			String new_ID = value;
//			sql = "select h.id,h.hetbh from hetb h\n" 
//					+ "where h.liucztb_id=0 and h.id in("
//					+ new_ID.substring(0, new_ID.lastIndexOf(",")) + ")";
//			if (((Visit) getPage().getVisit()).getString4().equals("XS")) {
//
//				sql += " union select hetb.id,hetb.hetbh\n"
//						+ "       from hetb,diancxxb\n"
//						+ "       where hetb.diancxxb_id=diancxxb.id\n"
//						+ "             and (leib=0 or leib=1)\n"
//						+ "             and hetb.fuid=0\n"
//						+ "             and liucztb_id=0\n"
//						+ "             and to_char(hetb.qiandrq,'YYYY')='"
//						+ getNianfValue().getId()
//						+ "'\n"
//						+ "             and (diancxxb_id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " or diancxxb.fuid="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ "or diancxxb.id in(select fuid from diancxxb where id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ ")) order by hetbh";
//
//			} else if (((Visit) getPage().getVisit()).getString4().equals("CG")) {
//				// 分公司对煤矿的合同
//				sql += "union select hetb.id,hetb.hetbh\n"
//						+ "       from hetb,diancxxb\n"
//						+ "       where hetb.diancxxb_id=diancxxb.id\n"
//						+ "             and leib=2\n"
//						+ "             and hetb.fuid=0\n"
//						+ "             and liucztb_id=0\n"
//						+ "             and to_char(hetb.qiandrq,'YYYY')='"
//						+ getNianfValue().getId() + "'\n"
//						+ "             and (diancxxb_id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " or diancxxb.fuid="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ ") order by hetbh";
//			}
//			// 获取复制合同ID后置空
//			// ((Visit) getPage().getVisit()).setString12("");
//			((Visit) getPage().getVisit())
//					.setProSelectionModel2(new IDropDownModel(sql, "新建合同"));
//		} else {
//			if (((Visit) getPage().getVisit()).getString4().equals("XS")) {
//
//				sql = "select hetb.id,hetb.hetbh\n"
//						+ "       from hetb,diancxxb\n"
//						+ "       where hetb.diancxxb_id=diancxxb.id\n"
//						+ "             and (leib=0 or leib=1)\n"
//						+ "             and hetb.fuid=0\n"
//						+ "             and liucztb_id=0\n"
//						+ "             and to_char(hetb.qiandrq,'YYYY')='"
//						+ getNianfValue().getId()
//						+ "'\n"
//						+ "             and (diancxxb_id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " or diancxxb.fuid="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ "or diancxxb.id in(select fuid from diancxxb where id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ ")) order by hetbh";
//
//			} else if (((Visit) getPage().getVisit()).getString4().equals("CG")) {
//				// 分公司对煤矿的合同
//				sql = "select hetb.id,hetb.hetbh\n"
//						+ "       from hetb,diancxxb\n"
//						+ "       where hetb.diancxxb_id=diancxxb.id\n"
//						+ "             and leib=2\n"
//						+ "             and hetb.fuid=0\n"
//						+ "             and liucztb_id=0\n"
//						+ "             and to_char(hetb.qiandrq,'YYYY')='"
//						+ getNianfValue().getId() + "'\n"
//						+ "             and (diancxxb_id="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ " or diancxxb.fuid="
//						+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//						+ ") order by hetbh";
//			}
//
//			((Visit) getPage().getVisit())
//					.setProSelectionModel2(new IDropDownModel(sql, "新建合同"));
//		}
//	}
	
	
//	合同变量设置_Grid_begin
	
	public void Dak_Hetbl(long hetmb_id){
		
//		合同变量定义Grid_begin
		JDBCcon con = new JDBCcon();
		String sql = "select id,hetb_id,bianlmc,value from hetblb where hetb_id="+hetmb_id;
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("hetb_id").setHidden(true);
		egu.getColumn("hetb_id").setEditor(null);
		egu.getColumn("bianlmc").setHeader("变量名称");
		egu.getColumn("value").setHeader("值");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);	//添加
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);	//删除
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");	//删除
		setExtGrid(egu);
		rsl.close();
		con.Close();
//		合同变量定义Grid_end
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		
		if(getExtGrid()!=null){
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("if(cbo_hetSelect.getValue()>-1){ \n")			
				.append("if(!win_bl){	\n").append(getExtGrid().getGridScript()).append("\n")
				.append(
					"win_bl = new Ext.Window({\n" +
					"\n" + 
					"   el:'hetbl-win',\n" + 
					"   layout:'fit',\n" + 
					"   width:600,\n" + 
					"   height:400,\n" + 
					"   closeAction:'hide',\n" + 
					"   plain: true,\n" + 
					"   title:'变量设置',\n" + 
					"          modal:true,\n" + 
					"   items: [gridDiv_grid]\n" + 
					" });\n" + 
					"}\n" + 
					"win_bl.show();}else{\n " +
					"	Ext.MessageBox.alert('提示信息','请先选择或保存合同!');return;	\n" +
					"}"
				);
			return sb.toString();
			
		}else{
			
			return "Ext.MessageBox.alert('提示信息','请先选择或保存合同!');	\n";
		}
	}

	public String getGridHtml() {
		
		if(getExtGrid()!=null){
			
			return getExtGrid().getHtml();
		}else{
			
			return "";
		}
	}
//	合同变量设置_Grid_end

	// 新模板名称
	public String getmobmc() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setmobmc(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	// 合同信息
	public Hetxxbean gethetxxbean() {
		if (((Visit) getPage().getVisit()).getObject1() == null) {
			((Visit) getPage().getVisit()).setObject1(new Hetxxbean());
		}
		return (Hetxxbean) ((Visit) getPage().getVisit()).getObject1();
	}

	public void sethetxxbean(Hetxxbean value) {
		((Visit) getPage().getVisit()).setObject1(value);
	}

	// 计划口径
	public IDropDownBean getJihxzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIJihxzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJihxzValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setIJihxzModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIJihxzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIJihxzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getIJihxzModels() {
		String sql = "select id,mingc from jihkjb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, ""));
		return;
	}
	
//	电厂结算下拉框_开始
	public IDropDownBean getDiancjsValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean27() == null) {
			if (getDiancjsModel().getOptionCount() > 0) {
				setDiancjsValue((IDropDownBean) getDiancjsModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean27();
	}

	public void setDiancjsValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean27(LeibValue);
	}

	public IPropertySelectionModel getDiancjsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel27() == null) {
			getDiancjsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel27();
	}

	public void setDiancjsModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel27(value);
	}

	public void getDiancjsModels() {
		ArrayList list = new ArrayList();
		list.add(new IDropDownBean(0, "否"));
		list.add(new IDropDownBean(1, "是"));
		setDiancjsModel(new IDropDownModel(list));
	}
//	电厂结算下拉框_结束
	
//	杂费名称下拉框_开始
	public IDropDownBean getZafmcValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean28() == null) {
			if (getZafmcModel().getOptionCount() > 0) {
				setZafmcValue((IDropDownBean) getZafmcModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean28();
	}

	public void setZafmcValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean28(LeibValue);
	}

	public IPropertySelectionModel getZafmcModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel28() == null) {
			getZafmcModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel28();
	}

	public void setZafmcModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel28(value);
	}

	public void getZafmcModels() {
		String sql = "select it.id, it.mingc from item it where it.itemsortid = (select id from itemsort ist where ist.bianm = 'DZZF') order by it.mingc";
		setZafmcModel(new IDropDownModel(sql));
	}
//	杂费名称下拉框_结束

	// 合同数量信息
	private int _editTableRows = -1;

	public int getEditTableRows() {
		return _editTableRows;
	}

	public void setEditTableRows(int _value) {
		_editTableRows = _value;
	}

	public List geteditValuess() {
		if (((Visit) getPage().getVisit()).getList1() == null) {
			((Visit) getPage().getVisit()).setList1(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList1();
	}

	private Fahxxbean editValues1;

	public Fahxxbean geteditValues1() {
		return editValues1;
	}

	public void seteditValues1(Fahxxbean value) {
		this.editValues1 = value;
	}

	// 运输方式
	public IDropDownBean getyunsfsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getyunsfsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setyunsfsValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setyunsfsModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getyunsfsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getyunsfsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getyunsfsModels() {
		String sql = "select id,mingc\n" + "from yunsfsb  order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 燃料品种
	public IDropDownBean getRanlpzb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getRanlpzb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setRanlpzb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setRanlpzb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getRanlpzb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getRanlpzb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getRanlpzb_idModels() {
		String sql = "select id,mingc from pinzb where instr(mingc,'油') = 0   order by mingc  ";// where
		// pinzb.zhangt=1
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 车站
	public IDropDownBean getchezValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getchezModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setchezValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setchezModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getchezModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getchezModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getchezModels() {
		String sql = "select id,mingc\n" + "from chezxxb\n" + "order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 收货人
	public IDropDownBean getshouhrValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getshouhrModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setshouhrValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setshouhrModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getshouhrModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getshouhrModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	// public void getshouhrModels() {
	// String sql =
	// "select id,mingc\n" +
	// "from diancxxb";
	// ((Visit) getPage().getVisit()).setProSelectionModel7(new
	// IDropDownModel(sql,"")) ;
	// return ;
	// }
	public void getshouhrModels() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select jib from diancxxb where id = " + 
				((Visit) getPage().getVisit()).getDiancxxb_id());
		String tiaoj = "or id not in (select id from diancxxb)";
		if (rsl.next()) {
			if (rsl.getInt("jib") == 3) {
				tiaoj = "";
			}
		}
		rsl.next();
		
		String sql = "select id,mingc,jib\n" + "from(\n"
				+ " select id,mingc,0 as jib\n" + " from vwxufdw\n"
				+ " where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + tiaoj + "\n"
				+ " union\n" + " select *\n" + " from(\n"
				+ " select id,mingc,level as jib\n" + "  from diancxxb\n"
				+ " start with fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ " connect by fuid=prior id\n" + " order SIBLINGS by  xuh)\n"
				+ " )\n" + " order by jib,mingc";
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(0, ""));
		
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib = rs.getInt("jib");
				String nbsp = String.valueOf((char) 0xA0);
				for (int i = 0; i < jib; i++) {
					mc = nbsp + nbsp + nbsp + nbsp + mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(dropdownlist));
		return;
	}

	// 质量
	private int _editTableRowz = -1;

	public int getEditTableRowz() {
		return _editTableRowz;
	}

	public void setEditTableRowz(int _value) {
		_editTableRowz = _value;
	}

	public List geteditValuesz() {
		if (((Visit) getPage().getVisit()).getList2() == null) {
			((Visit) getPage().getVisit()).setList2(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList2();
	}

	public Zhilyqbean editValuez;

	public Zhilyqbean geteditValuez() {
		return this.editValuez;
	}

	public void seteditValuez(Zhilyqbean value) {
		this.editValuez = value;
	}
	
	// 杂费
	private int _editTableRowzf = -1;

	public int getEditTableRowzf() {
		return _editTableRowzf;
	}

	public void setEditTableRowzf(int _value) {
		_editTableRowzf = _value;
	}

	public List geteditValueszf() {
		if (((Visit) getPage().getVisit()).getList5() == null) {
			((Visit) getPage().getVisit()).setList5(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList5();
	}

	public Zafxxbean editValuezf;

	public Zafxxbean geteditValuezf() {
		return this.editValuezf;
	}

	public void seteditValuezf(Zafxxbean value) {
		this.editValuezf = value;
	}

	// 指标
	public IDropDownBean getZHIBValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getZHIBModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setZHIBValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setZHIBModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getZHIBModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getZHIBModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getZHIBModels() {
		String sql = "select id,mingc\n" + "from zhibb where leib=1  order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel8(new IDropDownModel(sql, ""));
		return;
	}

	// 条件
	public IDropDownBean getTIAOJValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getTIAOJModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setTIAOJValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean9(Value);
	}

	public void setTIAOJModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getTIAOJModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getTIAOJModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void getTIAOJModels() {
		String sql = "select id,mingc\n" + "from tiaojb  order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(sql, ""));
		return;
	}

	// 单位
	public IDropDownBean getDANWValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getDANWModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setDANWValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean10(Value);
	}

	public void setDANWModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getDANWModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getDANWModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getDANWModels() {
		String sql = "select id,mingc\n" + "from danwb   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, ""));
		return;
	}

	// 增扣款
	private int _editTableRowj = -1;

	public int getEditTableRowj() {
		return _editTableRowj;
	}

	public void setEditTableRowj(int _value) {
		_editTableRowj = _value;
	}

	public List geteditValuesj() {
		if (((Visit) getPage().getVisit()).getList3() == null) {
			((Visit) getPage().getVisit()).setList3(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList3();
	}

	public Zengkkbean editValuej;

	public Zengkkbean geteditValuej() {
		return this.editValuej;
	}

	public void seteditValuej(Zengkkbean value) {
		this.editValuej = value;
	}

	// 价格
	private int _editTableRowg = -1;

	public int getEditTableRowg() {
		return _editTableRowg;
	}

	public void setEditTableRowg(int _value) {
		_editTableRowg = _value;
	}

	public List geteditValuesg() {
		if (((Visit) getPage().getVisit()).getList4() == null) {
			((Visit) getPage().getVisit()).setList4(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList4();
	}

	public jijbean editValueg;

	public jijbean geteditValueg() {
		return this.editValueg;
	}

	public void seteditValueg(jijbean value) {
		this.editValueg = value;
	}

	public IDropDownBean getzhilxmjSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean11((IDropDownBean) getzhilxmjSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setzhilxmjSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean11(Value);
	}

	public void setzhilxmjSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getzhilxmjSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
			getzhilxmjSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public void getzhilxmjSelectModels() {
		String sql = "select id,mingc\n" + "from zhibb\n"
				+ "where zhibb.leib=1   order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(sql, ""));
		return;
	}

	// 结算用条件
	public IDropDownBean gettiaojjSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean12() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean12((IDropDownBean) gettiaojjSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean12();
	}

	public void settiaojjSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean12(Value);
	}

	public void settiaojjSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel12(value);
	}

	public IPropertySelectionModel gettiaojjSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel12() == null) {
			gettiaojjSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel12();
	}

	public void gettiaojjSelectModels() {
		String sql = "select id,mingc\n" + "from tiaojb\n"
				+ "where tiaojb.leib=1    order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel12(new IDropDownModel(sql, ""));
		return;
	}

	// 价格单位
	public IDropDownBean getjiagValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean13() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean13((IDropDownBean) getjiagModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean13();
	}

	public void setjiagValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean13(Value);
	}

	public void setjiagModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel13(value);
	}

	public IPropertySelectionModel getjiagModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel13() == null) {
			getjiagModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel13();
	}

	public void getjiagModels() {
		String sql = "select id,mingc\n" + "from danwb\n"
				+ "where danwb.zhibb_id=0   order by mingc  ";// zhibb_id=0为价格单位
		((Visit) getPage().getVisit())
				.setProSelectionModel13(new IDropDownModel(sql, ""));
		return;
	}

	// 结算用指标单位
	public IDropDownBean getzhibdwSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean14() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean14((IDropDownBean) getzhibdwSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean14();
	}

	public void setzhibdwSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean14(Value);
	}

	public void setzhibdwSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel14(value);
	}

	public IPropertySelectionModel getzhibdwSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel14() == null) {
			getzhibdwSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel14();
	}

	public void getzhibdwSelectModels() {// zhibb_id=
		String sql = "select id,mingc " + "from danwb   order by mingc  \n";
		((Visit) getPage().getVisit())
				.setProSelectionModel14(new IDropDownModel(sql, ""));
		return;
	}

	// 合同结算方式表
	public IDropDownBean getjiesfsgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean15() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean15((IDropDownBean) getjiesfsgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean15();
	}

	public void setjiesfsgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean15(Value);
	}

	public void setjiesfsgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel15(value);
	}

	public IPropertySelectionModel getjiesfsgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel15() == null) {
			getjiesfsgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel15();
	}

	public void getjiesfsgSelectModels() {
		String sql = "select id,mingc\n" + "from hetjsfsb   order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel15(new IDropDownModel(sql, ""));
		return;
	}

	// 合同计价方式
	public IDropDownBean getjijfsgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean16() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean16((IDropDownBean) getjijfsgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean16();
	}

	public void setjijfsgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean16(Value);
	}

	public void setjijfsgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel16(value);
	}

	public IPropertySelectionModel getjijfsgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel16() == null) {
			getjijfsgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel16();
	}

	public void getjijfsgSelectModels() {
		String sql = "select id,mingc\n" + "from hetjjfsb   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel16(new IDropDownModel(sql, ""));
		return;
	}

	// 小数处理
	public IDropDownBean getxiaoswcljSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean17() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean17((IDropDownBean) getxiaoswcljSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean17();
	}

	public void setxiaoswcljSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean17(Value);
	}

	public void setxiaoswcljSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel17(value);
	}

	public IPropertySelectionModel getxiaoswcljSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel17() == null) {
			getxiaoswcljSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel17();
	}

	public void getxiaoswcljSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, " "));
		list.add(new IDropDownBean(1, "进位"));
		list.add(new IDropDownBean(2, "舍去"));
		list.add(new IDropDownBean(3, "四舍五入"));
		list.add(new IDropDownBean(4, "四舍五入(0.1)"));
		list.add(new IDropDownBean(5, "四舍五入(0.01)"));
		list.add(new IDropDownBean(6, "四舍五入(0.001)"));
		list.add(new IDropDownBean(7, "四舍五入(0.0001)"));
		list.add(new IDropDownBean(8, "四舍五入(0.000001)"));
		((Visit) getPage().getVisit())
				.setProSelectionModel17(new IDropDownModel(list));
		return;
	}

	// 适用范围
	public IDropDownBean getshiyfwSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean26() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean26((IDropDownBean) getshiyfwSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean26();
	}

	public void setshiyfwSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean26(Value);
	}

	public void setshiyfwSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel26(value);
	}

	public IPropertySelectionModel getshiyfwSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel26() == null) {
			getshiyfwSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel26();
	}

	public void getshiyfwSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "全部"));
		list.add(new IDropDownBean(1, "超出部分"));
		((Visit) getPage().getVisit())
				.setProSelectionModel26(new IDropDownModel(list));
		return;
	}

	// 文字
	String buffer;

	public String getWenz() {
		return buffer;
	}

	public void setWenz(String value) {
		this.buffer = value;
	}

	// 合同供方
	public IDropDownBean getgongfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean18() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean18((IDropDownBean) getgongfModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean18();
	}

	public void setgongfValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean18() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean18().getId()) {
				((Visit) getPage().getVisit()).setboolean2(true);
			} else {
				((Visit) getPage().getVisit()).setboolean2(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean18(Value);
		}
	}

	public void setgongfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel18(value);
	}

	public IPropertySelectionModel getgongfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel18() == null) {
			getgongfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel18();
	}

	public void getgongfModels() {
		String sql = "select id,piny||' '||mingc from gongysb where leix=1 and zhuangt=1 order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel18(new IDropDownModel(sql, ""));
		return;
	}

	// 需方
	public IDropDownBean getxufValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean19() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean19((IDropDownBean) getxufModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean19();
	}

	public void setxufValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean19() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean19().getId()) {
				((Visit) getPage().getVisit()).setboolean3(true);
			} else {
				((Visit) getPage().getVisit()).setboolean3(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean19(Value);
		}
	}

	public void setxufModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel19(value);
	}

	public IPropertySelectionModel getxufModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel19() == null) {
			getxufModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel19();
	}

	// public void getxufModels() {//显示该用户单位下所有孩子
	// String sql=
	// "select id,mingc " +
	// "from diancxxb\n" +
	// "where diancxxb.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"
	// or id="+((Visit) getPage().getVisit()).getDiancxxb_id();
	// ((Visit) getPage().getVisit()).setProSelectionModel19(new
	// IDropDownModel(sql,"")) ;
	// return ;
	// }
	public void getxufModels() {// 显示该用户单位下所有孩子
		String sql = "";
		long diancId=((Visit) getPage().getVisit()).getDiancxxb_id();
		String diancqc=((Visit) getPage().getVisit()).getDiancqc();
		if (((Visit) getPage().getVisit()).getString4().equals("CG")) {
			//专为外二设定需方单位
			if (diancqc.equals("上海外高桥第二发电有限责任公司")) {
				sql="select id,mingc from diancxxb where quanc='上海外高桥第二发电有限责任公司'";
			}else{
				sql = "select id,mingc from diancxxb where  fuid="
					+ diancId
					+ "or  id="
					+ diancId
					+ " or id in(select fuid from diancxxb where id="
					+ diancId + " )   order by mingc ";
			}
		} else if (((Visit) getPage().getVisit()).getString4().equals("XS")) {
			if (diancqc.equals("上海外高桥第二发电有限责任公司")) {
				sql="select id,mingc from vwxufdw where quanc='上海外高桥第二发电有限责任公司'";
			}else{
				sql = "select id,mingc from vwxufdw order by mingc";
			}
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel19(new IDropDownModel(sql, ""));
		return;
	}

	// 供方
	public IDropDownBean getshijgfSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean20() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean20((IDropDownBean) getshijgfSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean20();
	}

	public void setshijgfSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean20() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean20().getId()) {
				((Visit) getPage().getVisit()).setboolean7(true);
			} else {
				((Visit) getPage().getVisit()).setboolean7(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean20(Value);
		}
		((Visit) getPage().getVisit()).setDropDownBean20(Value);
	}

	public void setshijgfSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel20(value);
	}

	public IPropertySelectionModel getshijgfSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel20() == null) {
			getshijgfSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel20();
	}

	public void getshijgfSelectModels() {
		String sql = "select id,mingc from gongysb where leix=1 and zhuangt=1 order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel20(new IDropDownModel(sql, ""));
		return;
	}

	// 加权方式
	public IDropDownBean getjiaqfsgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean21() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean21((IDropDownBean) getjiaqfsgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean21();
	}

	public void setjiaqfsgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean21(Value);
	}

	public void setjiaqfsgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel21(value);
	}

	public IPropertySelectionModel getjiaqfsgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel21() == null) {
			getjiaqfsgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel21();
	}

	public void getjiaqfsgSelectModels() {
		String sql = "select id,mingc\n" + "from hetjsxsb  order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel21(new IDropDownModel(sql, ""));
		return;
	}

	// 拒付亏吨运费
	public IDropDownBean getyingdkfgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean22() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean22((IDropDownBean) getyingdkfgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean22();
	}

	public void setyingdkfgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean22(Value);
	}

	public void setyingdkfgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel22(value);
	}

	public IPropertySelectionModel getyingdkfgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel22() == null) {
			getyingdkfgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel22();
	}

	public void getyingdkfgSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, ""));
		list.add(new IDropDownBean(1, "是"));
		list.add(new IDropDownBean(2, "否"));
		((Visit) getPage().getVisit())
				.setProSelectionModel22(new IDropDownModel(list));
		return;
	}

	//
	// 年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean23() == null) {
			((Visit) getPage().getVisit()).setDropDownBean23(getIDropDownBean(
					getNianfModel(), DateUtil.getYear(new Date())));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean23();
	}

	public void setNianfValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean23() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean23().getId()) {
				((Visit) getPage().getVisit()).setboolean4(true);
			} else {
				((Visit) getPage().getVisit()).setboolean4(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean23(Value);
		}
	}

	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel23() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel23();
	}

	public void getNianfModels() {
		List listNianf = new ArrayList();

		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel23(new IDropDownModel(listNianf));
	}
	
//	 月份
	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean29() == null) {
			((Visit) getPage().getVisit()).setDropDownBean29(getIDropDownBean(
					getYuefModel(), DateUtil.getMonth(new Date())));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean29();
	}

	public void setYuefValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean29() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean29().getId()) {
				((Visit) getPage().getVisit()).setboolean10(true);
			} else {
				((Visit) getPage().getVisit()).setboolean10(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean29(Value);
		}
	}

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel29() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel29();
	}

	public void getYuefModels() {
		List listYuef = new ArrayList();
		listYuef.add(new IDropDownBean(1,"01"));
		listYuef.add(new IDropDownBean(2,"02"));
		listYuef.add(new IDropDownBean(3,"03"));
		listYuef.add(new IDropDownBean(4,"04"));
		listYuef.add(new IDropDownBean(5,"05"));
		listYuef.add(new IDropDownBean(6,"06"));
		listYuef.add(new IDropDownBean(7,"07"));
		listYuef.add(new IDropDownBean(8,"08"));
		listYuef.add(new IDropDownBean(9,"09"));
		listYuef.add(new IDropDownBean(10,"10"));
		listYuef.add(new IDropDownBean(11,"11"));
		listYuef.add(new IDropDownBean(12,"12"));
		((Visit) getPage().getVisit())
				.setProSelectionModel29(new IDropDownModel(listYuef));
	}

	// 煤矿
	public IDropDownBean getMeiksselectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean24() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean24((IDropDownBean) getMeiksselectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean24();
	}

	public void setMeiksselectValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean24(Value);
	}

	public void setMeiksselectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel24(value);
	}

	public IPropertySelectionModel getMeiksselectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel24() == null) {
			getMeiksselectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel24();
	}

	public void getMeiksselectModels() {
		String sql = "select meikxxb.id,meikxxb.mingc\n"
				+ "from meikxxb,gongysmkglb\n"
				+ "where meikxxb.id=gongysmkglb.meikxxb_id\n"
				+ "and gongysmkglb.gongysb_id="
				+ getshijgfSelectValue().getId() +"  order by meikxxb.mingc ";

		((Visit) getPage().getVisit())
				.setProSelectionModel24(new IDropDownModel(sql, ""));
		return;
	}

	// 基价类型
	public IDropDownBean getJijlxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean25() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean25((IDropDownBean) getIJijlxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean25();
	}

	public void setJijlxValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean25(Value);
	}

	public void setIJijlxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel25(value);
	}

	public IPropertySelectionModel getIJijlxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel25() == null) {
			getIJijlxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel25();
	}

	public void getIJijlxModels() {
		List Jijlx = new ArrayList();
		Jijlx.add(new IDropDownBean(-1, ""));
		Jijlx.add(new IDropDownBean(0, "含税单价"));
		Jijlx.add(new IDropDownBean(1, "不含税单价"));
		((Visit) getPage().getVisit())
				.setProSelectionModel25(new IDropDownModel(Jijlx));
	}

	// 基价类型_end

	private boolean tijsh;// 是否添加 提交进入流程审核功能

	public boolean isTijsh() {

		return tijsh;
	}

	public void setTijsh() {

		tijsh = false;

		String sql = " select * from xitxxb  where mingc='合同模板提交审核' and leib='合同模板' and zhi='是' and zhuangt=1 ";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);

		if (rsl.next()) {
			tijsh = true;
		}

	}

	public void Clear() {
		Visit visit = (Visit) getPage().getVisit();
		this.setHetxxbjFlag(false);
		this.setTijsh();
		visit.setActivePageName(getPageName().toString());
		visit.setString1(""); // 新模板名称
		visit.setString2(""); // 发货人
		visit.setString3(""); // 指标单位关联数组
		visit.setString4(""); // 页面参数
		
		setExtGrid(null);		//变量Grid
        setFujzt(0); // 附件状态
        setFujScript(""); // 添加附件
        setExtGrid(null); // 变量Grid

        // ((Visit) getPage().getVisit()).setDropDownBean1(null); //合同模版
		((Visit) getPage().getVisit()).setDropDownBean2(null);
		((Visit) getPage().getVisit()).setDropDownBean3(null);
		((Visit) getPage().getVisit()).setDropDownBean4(null);
		((Visit) getPage().getVisit()).setDropDownBean5(null);
		((Visit) getPage().getVisit()).setDropDownBean6(null);
		((Visit) getPage().getVisit()).setDropDownBean7(null);
		((Visit) getPage().getVisit()).setDropDownBean8(null);
		((Visit) getPage().getVisit()).setDropDownBean9(null);
		((Visit) getPage().getVisit()).setDropDownBean10(null);
		((Visit) getPage().getVisit()).setDropDownBean11(null);
		((Visit) getPage().getVisit()).setDropDownBean12(null);
		((Visit) getPage().getVisit()).setDropDownBean13(null);
		((Visit) getPage().getVisit()).setDropDownBean14(null);
		((Visit) getPage().getVisit()).setDropDownBean15(null);
		((Visit) getPage().getVisit()).setDropDownBean16(null);
		((Visit) getPage().getVisit()).setDropDownBean17(null);
		((Visit) getPage().getVisit()).setDropDownBean18(null);
		((Visit) getPage().getVisit()).setDropDownBean19(null);
		((Visit) getPage().getVisit()).setDropDownBean20(null);
		((Visit) getPage().getVisit()).setDropDownBean21(null);
		((Visit) getPage().getVisit()).setDropDownBean22(null);
		// ((Visit) getPage().getVisit()).setDropDownBean23(null);
		((Visit) getPage().getVisit()).setDropDownBean24(null);
		((Visit) getPage().getVisit()).setDropDownBean25(null);
		((Visit) getPage().getVisit()).setDropDownBean26(null);
		((Visit) getPage().getVisit()).setDropDownBean27(null);
		((Visit) getPage().getVisit()).setDropDownBean28(null);

		// ((Visit) getPage().getVisit()).setProSelectionModel1(null);
		((Visit) getPage().getVisit()).setProSelectionModel2(null);
		((Visit) getPage().getVisit()).setProSelectionModel3(null);
		((Visit) getPage().getVisit()).setProSelectionModel4(null);
		((Visit) getPage().getVisit()).setProSelectionModel5(null);
		((Visit) getPage().getVisit()).setProSelectionModel6(null);
		((Visit) getPage().getVisit()).setProSelectionModel7(null);
		((Visit) getPage().getVisit()).setProSelectionModel8(null);
		((Visit) getPage().getVisit()).setProSelectionModel9(null);
		((Visit) getPage().getVisit()).setProSelectionModel10(null);
		((Visit) getPage().getVisit()).setProSelectionModel11(null);
		((Visit) getPage().getVisit()).setProSelectionModel12(null);
		((Visit) getPage().getVisit()).setProSelectionModel13(null);
		((Visit) getPage().getVisit()).setProSelectionModel14(null);
		((Visit) getPage().getVisit()).setProSelectionModel15(null);
		((Visit) getPage().getVisit()).setProSelectionModel16(null);
		((Visit) getPage().getVisit()).setProSelectionModel17(null);
		((Visit) getPage().getVisit()).setProSelectionModel18(null);
		((Visit) getPage().getVisit()).setProSelectionModel19(null);
		((Visit) getPage().getVisit()).setProSelectionModel20(null);
		((Visit) getPage().getVisit()).setProSelectionModel21(null);
		((Visit) getPage().getVisit()).setProSelectionModel22(null);
		// ((Visit) getPage().getVisit()).setProSelectionModel23(null);
		((Visit) getPage().getVisit()).setProSelectionModel24(null);
		((Visit) getPage().getVisit()).setProSelectionModel25(null);
		((Visit) getPage().getVisit()).setProSelectionModel26(null);
		((Visit) getPage().getVisit()).setProSelectionModel27(null);
		((Visit) getPage().getVisit()).setProSelectionModel28(null);
		// 结算类型清楚
		setJijlxValue(null); // 25
		setIJijlxModel(null); // 25

		((Visit) getPage().getVisit()).setObject1(null); // 合同信息
		((Visit) getPage().getVisit()).setList1(null);
		((Visit) getPage().getVisit()).setList2(null);
		((Visit) getPage().getVisit()).setList3(null);
		((Visit) getPage().getVisit()).setList4(null);
		((Visit) getPage().getVisit()).setList5(null); // 杂费信息
		setTabbarSelect(0);
		setXinjht(true);
		getDanwGL();// 构造指标单位和指标单位对应的单位的关系数组

		// 初始化状态标识
		((Visit) getPage().getVisit()).setboolean1(false);
		((Visit) getPage().getVisit()).setboolean2(false);
		((Visit) getPage().getVisit()).setboolean3(false);
		((Visit) getPage().getVisit()).setboolean4(false);
		((Visit) getPage().getVisit()).setboolean6(false);
		((Visit) getPage().getVisit()).setboolean7(false);
		((Visit) getPage().getVisit()).setboolean9(false);
	}

	private String value = "";

	boolean isFuz = false;

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			isFuz=false;
            if (visit.getActivePageName().toString().equals("Fujsc")) {
                // 附件上传
                setFujzt(this.gethetSelectValue().getId()); // 附件状态
                setFujScript(""); // 添加附件window.open
                isFuz = true;
            } else if (visit.getActivePageName().toString().equals("Shenhrz")) {
                isFuz = true;
            } else {

                // 合同下拉框
                ((Visit) getPage().getVisit()).setDropDownBean2(null);
                ((Visit) getPage().getVisit()).setProSelectionModel2(null);
                setHetbl(false); // Hetbl
            }
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);	
			setHetbl(false);	//Hetbl
			setZafTab("");
			
			if(MainGlobal.getXitxx_item("合同", "是否定义合同变量", 
					String.valueOf(visit.getDiancxxb_id()), "否").equals("是")){
				
				setHetbl(true);	//显示合同变量配置按钮
			}
			
			if (MainGlobal.getXitxx_item("合同", "是否在煤款合同中录入杂费信息", 
					String.valueOf(visit.getDiancxxb_id()), "否").equals("是")) {
//				如果配制了“是否在煤款合同中录入杂费信息”参数，那么在购煤合同中显示杂费Tab。
				setZafTab(",{contentEl:'div7',  title: '杂费'}");
			}
			
			//是否为从复制合同页后返回
			if (visit.getActivePageName().toString().equals("Hetfz")) {
				value = visit.getString12();
				if (visit.getString12() != null) {
					isFuz = value.equals("") ? false : true;
				}
			}
			
			((Visit) this.getPage().getVisit()).setExtGrid1(null);	//变量
			
			if(!visit.getActivePageName().toString().equals("Hetfz") && 
					!visit.getActivePageName().toString().equals("Shenhrz")){
				visit.setString12(null);
				value=null;
				//isFuz=false;
				Clear();
				if (cycle.getRequestContext().getParameters("lx") != null) {
					// 认为是采购合同(CG)是和矿方签订的合同
					((Visit) this.getPage().getVisit()).setString4(cycle
							.getRequestContext().getParameters("lx")[0]);
				} else {
					// 认为是电厂和煤矿或电厂和分公司签订的合同
					((Visit) this.getPage().getVisit()).setString4("XS");
				}
			}
			
			if (!isFuz) {
				// 模板
				((Visit) getPage().getVisit()).setProSelectionModel1(null);
				((Visit) getPage().getVisit()).setDropDownBean1(null);
				// 年份
				((Visit) getPage().getVisit()).setProSelectionModel23(null);
				((Visit) getPage().getVisit()).setDropDownBean23(null);
				//月份
				((Visit) getPage().getVisit()).setProSelectionModel29(null);
				((Visit) getPage().getVisit()).setDropDownBean29(null);
				// 合同
						
			}
						
			visit.setActivePageName(getPageName().toString());	
			setHetbl(false);	//是否显示合同变量配置的按钮
			
			this.setExtGrid(null); //合同变量Grid定义
			
			if(MainGlobal.getXitxx_item("合同", "是否定义合同变量", 
					String.valueOf(visit.getDiancxxb_id()), "否").equals("是")){
				
				setHetbl(true);	//显示合同变量配置按钮
			}
		}
		
		if (cycle.getRequestContext().getParameters("lx") != null
				&& !cycle.getRequestContext().getParameters("lx")[0].equals("")) {
			// 认为是采购合同(CG)是和矿方签订的合同

			if (!((Visit) this.getPage().getVisit()).getString4().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {
				isFuz=false;
				
				//是否为从复制合同页后返回
				if (visit.getActivePageName().toString().equals("Hetfz")) {
					value = visit.getString12();
					if (visit.getString12() != null) {
						isFuz = value.equals("") ? false : true;
					}
				}
				
				Clear();
				((Visit) this.getPage().getVisit()).setString4(cycle
						.getRequestContext().getParameters("lx")[0]);
				getxufModels();
			}

		}
		if (((Visit) getPage().getVisit()).getboolean1()) {// 选择合同
			getXuanzht();
		}
		if (((Visit) getPage().getVisit()).getboolean2()) {// 合同供方
			getGongf();
		}
		if (((Visit) getPage().getVisit()).getboolean7()) {// 真实供方
			getMeiksselectModels();
		}
		if (((Visit) getPage().getVisit()).getboolean3()) {// 需方
			getXuf();
		}
		if (((Visit) getPage().getVisit()).getboolean4()) {// 年份刷新合同
			gethetSelectModels();
		}
		if (((Visit) getPage().getVisit()).getboolean10()) {// 月份刷新合同
			gethetSelectModels();
		}
		if (((Visit) getPage().getVisit()).getboolean6()) {// 加载模板
			gethetmb(getmobmcSelectValue().getId());
		}
		
		setHetbl(false);
	}

	private boolean HetxxbjFlag = false;

	public boolean isHetxxbjFlag() {
		return HetxxbjFlag;
	}

	public void setHetxxbjFlag(boolean hetxxbjFlag) {
		JDBCcon con = new JDBCcon();
		HetxxbjFlag = hetxxbjFlag;// 初始化
		String sql = " select * from xitxxb where mingc='合同模板合同信息可录入' and leib='合同'  "
				+ " and zhi='是' and zhuangt=1 and diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();

		if (con.getResultSetList(sql).next()) {
			HetxxbjFlag = true;
		} else {
			HetxxbjFlag = false;
		}

	}

	private void getDanwGL() {
		String sql = "";
		List list = new ArrayList();
		StringBuffer Tem = new StringBuffer();
		JDBCcon con = new JDBCcon();
		sql = "select zhibb_id zhibid,danwb.id danwid,danwb.mingc,zhibb.mingc\n"
				+ "from danwb,zhibb\n"
				+ "where danwb.zhibb_id=zhibb.id and zhibb_id<>0\n"
				+ "order by  zhibb_id ,zhibb.mingc";
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				String[] gl = new String[4];
				gl[0] = rs.getString(1);
				gl[1] = rs.getString(2);
				gl[2] = rs.getString(3);
				gl[3] = rs.getString(4);
				list.add(gl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		Tem.append("var zhib_danw=new Array();");
		for (int i = 0; i < list.size(); i++) {
			Tem.append("zhib_danw[" + i + "]=new Array("
					+ ((String[]) list.get(i))[0] + ","
					+ ((String[]) list.get(i))[1] + ",'"
					+ ((String[]) list.get(i))[2] + "','"
					+ ((String[]) list.get(i))[3] + "');");// +
		}
		setDanwglStr(Tem.toString());
	}

	// 页面判定方法
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

	// 模板管理
	// private boolean _XinjButton = false;
	//
	// public void XinjButton(IRequestCycle cycle) {
	// _XinjButton = true;
	// }
	//
	// private boolean _DakButton = false;
	//
	// public void DakButton(IRequestCycle cycle) {
	// _DakButton = true;
	// }

	private boolean _ShancButton = false;

	public void ShancButton(IRequestCycle cycle) {
		_ShancButton = true;
	}

	private boolean _BaocButton = false;

	public void BaocButton(IRequestCycle cycle) {
		_BaocButton = true;
	}

	private boolean TijButton = false;

	public void TijButton(IRequestCycle cycle) {
		TijButton = true;
	}

	// 数量
	private boolean _InsertButtons = false;

	public void InsertButtons(IRequestCycle cycle) {
		_InsertButtons = true;
	}

	private boolean _DeleteButtons = false;

	public void DeleteButtons(IRequestCycle cycle) {
		_DeleteButtons = true;
	}

	// 质量
	private boolean _InsertButtonz = false;

	public void InsertButtonz(IRequestCycle cycle) {
		_InsertButtonz = true;
	}

	private boolean _DeleteButtonz = false;

	public void DeleteButtonz(IRequestCycle cycle) {
		_DeleteButtonz = true;
	}
	
	// 杂费
	private boolean _InsertButtonzf = false;
	
	public void InsertButtonzf(IRequestCycle cycle) {
		_InsertButtonzf = true;
	}

	private boolean _DeleteButtonzf = false;

	public void DeleteButtonzf(IRequestCycle cycle) {
		_DeleteButtonzf = true;
	}

	// 增扣价格
	private boolean _InsertButtonj = false;

	public void InsertButtonj(IRequestCycle cycle) {
		_InsertButtonj = true;
	}

	private boolean _DeleteButtonj = false;

	public void DeleteButtonj(IRequestCycle cycle) {
		_DeleteButtonj = true;
	}

	// 价格
	private boolean _InsertButtong = false;

	public void InsertButtong(IRequestCycle cycle) {
		_InsertButtong = true;
	}

	private boolean _DeleteButtong = false;

	public void DeleteButtong(IRequestCycle cycle) {
		_DeleteButtong = true;
	}

	private boolean fuzButton = false;

	public void fuzButton(IRequestCycle cycle) {
		fuzButton = true;
	}
	
	private boolean _SaveButton=false;
	
	public void SaveButton(IRequestCycle cycle){
		_SaveButton=true;
	}
    private boolean _FujButton = false;

    public void FujButton(IRequestCycle cycle) {
        _FujButton = true;
    }

	public void submit(IRequestCycle cycle) {
		// 模板管理操作
		// if (_XinjButton) {
		// _XinjButton = false;
		// Xinj();
		// }
		if (TijButton) {
			TijButton = false;
			Tij();
		}
		if (_ShancButton) {
			_ShancButton = false;
			Shanc();
		}
		if (_BaocButton) {
			_BaocButton = false;
			Baoc();
		}
		// 数量
		if (_InsertButtons) {
			_InsertButtons = false;
			Inserts();
		}
		if (_DeleteButtons) {
			_DeleteButtons = false;
			Deletes();
		}
		// 质量
		if (_InsertButtonz) {
			_InsertButtonz = false;
			Insertz();
		}
		if (_DeleteButtonz) {
			_DeleteButtonz = false;
			Deletez();
		}
		// 杂费
		if (_InsertButtonzf) {
			_InsertButtonzf = false;
			Insertzf();
		}
		if (_DeleteButtonzf) {
			_DeleteButtonzf = false;
			Deletezf();
		}
		// 价格
		if (_InsertButtonj) {
			_InsertButtonj = false;
			Insertj();
		}
		if (_DeleteButtonj) {
			_DeleteButtonj = false;
			Deletej();
		}
		// 价格
		if (_InsertButtong) {
			_InsertButtong = false;
			Insertg();
		}
		if (_DeleteButtong) {
			_DeleteButtong = false;
			Deleteg();
		}
		if (fuzButton) {
			fuzButton = false;
			// if(gethetSelectValue().getId()==-1){
			// return;
			// }
			fuz(cycle);
		}
		if(_SaveButton){
			_SaveButton = false;
			
			Save((Visit) this.getPage().getVisit());//保存合同变量Grid
			getXuanzht();
		}
        if (_FujButton) {
		    try{
                _FujButton = false;
//                if (!getHetShzt().equals("1")) {
//                    Baoc();
//                }
                Fujcl();
            }catch (Exception e){
		        e.printStackTrace();
            }

        }
	}
    private void Fujcl() {
        // 处理附件逻辑
        String openwindow_url = "var url1 = 'http://'+document.location.host+document.location.pathname;var end1 = url1.indexOf(';');url1 = url1.substring(0,end1);window.open(url1+'?service=page/Fujsc&yewid="
                + gethetSelectValue().getId()
                + "','fuj','toolbar=0,resizable=0,status=1,width=600,height=430,scrollbar=1,left='+(window.screen.width-600)/2+',top='+(window.screen.height-430)/2);";
        this.setFujScript(openwindow_url);
    }
    public void setHetShzt(String value) {

        // // 合同审核状态
        // if(((Visit) getPage().getVisit()).getboolean1()){
        //
        // value = SetHetshztFromHetid(this.gethetSelectValue().getId());
        // //配置合同的审核状态
        // }

        ((Visit) getPage().getVisit()).setString8(value);
    }
    public String getHetShzt() {

        return ((Visit) getPage().getVisit()).getString8();
    }
	private void Save(Visit visit) {
//		保存合同变量方法
		long hetb_id = gethetSelectValue().getId();
		
		if(hetb_id>-1){
//			已保存了合同
			JDBCcon con = new JDBCcon();
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("begin \n");
			
			ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet((getChange()));
			while (delrsl.next()) {
				// 删除操作
				
				sbSql.append("	delete from hetblb where id=").append(delrsl.getString("ID")).append(";	\n");
			}
			delrsl.close();
			
			ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
			while (mdrsl.next()) {
				
				if ("0".equals(mdrsl.getString("ID"))) {
					
					sbSql.append("	insert into hetblb(id,hetb_id,bianlmc,value)")
						.append(" values(getnewid("+visit.getDiancxxb_id()+"), ")
						.append(hetb_id).append(", '")
						.append(mdrsl.getString("BIANLMC").trim()).append("', '")
						.append(mdrsl.getString("VALUE").trim()).append("');	\n");
				}else{
					
					sbSql.append(" update hetblb set bianlmc='")
						.append(mdrsl.getString("BIANLMC").trim()).append("',")
						.append(" value='").append(mdrsl.getString("VALUE").trim())
						.append("' where id=").append(mdrsl.getString("ID"))
						.append(";	\n");
				}
			}
			sbSql.append("end;");
//			System.out.println("Licext save() sbSql: \n" + sbSql);
			mdrsl.close();
			if(sbSql.length()>13){
				
				con.getUpdate(sbSql.toString());
			}
			con.Close();
		}else{
//			未保存了合同
			this.setMsg("请先保存合同!");
		}
	}

	public void fuz(IRequestCycle cycle) {
		cycle.activate("Hetfz");
	}

	private void fuz(String hetxxb_id) {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String newid = MainGlobal.getNewID(visit.getDiancxxb_id());
		String sql = "begin\n"
				+ "  insert into hetb( ID,FUID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH, \n"
				+ "  GONGFFDDBR,GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ, \n"
				+ "  XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID, \n"
				+ "  QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,LIUCZTB_ID,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS,XIAF) \n"
				+ "  (select "
				+ newid
				+ ",FUID,DIANCXXB_ID,HETBH||'副本',QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH, \n"
				+ "  GONGFFDDBR,GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ, \n"
				+ "  XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID, \n"
				+ "  QISRQ,GUOQRQ,HETB_MB_ID,JIHKJB_ID,0,LIUCGZID,LEIB,HETJJFSB_ID,MEIKMCS,XIAF \n"
				+ "  from hetb where hetb.id="
				+ hetxxb_id
				+ "); \n"
				+ "  insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT) \n"
				+ "  (select ID+10000000000,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,"
				+ newid
				+ ",ZHUANGT \n"
				+ "  from hetslb where hetb_id="
				+ hetxxb_id
				+ "); \n"
				+ "  insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID) \n"
				+ "  (select getnewid("
				+ visit.getDiancxxb_id()
				+ "),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,"
				+ newid
				+ " \n"
				+ "  from hetzlb where hetb_id="
				+ hetxxb_id
				+ "); \n"
				+ "  insert into hetzkkb(JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID \n"
				+ "  ,YUNSFSB_ID,BEIZ,HETB_ID,ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,SHIYFW,PINZB_ID) \n"
				+ "  (select JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID \n"
				+ " 	,YUNSFSB_ID,BEIZ,"
				+ newid
				+ ",getnewid("
				+ visit.getDiancxxb_id()
				+ "),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,SHIYFW,PINZB_ID \n"
				+ "  from hetzkkb where hetb_id="
				+ hetxxb_id
				+ "); \n"
				+ "  insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID \n"
				+ "  ,YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,FENGSJJ,JIJLX,PINZB_ID) \n"
				+ "  (select getnewid("
				+ visit.getDiancxxb_id()
				+ "),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID \n"
				+ "  ,YINGDKF,YUNSFSB_ID,ZUIGMJ," + newid
				+ ",HETJJFSB_ID,FENGSJJ,JIJLX,PINZB_ID \n"
				+ "  from hetjgb where hetb_id=" + hetxxb_id + "); \n"
				+ "  insert into hetwzb(ID,WENZNR,HETB_ID) \n"
				+ "  (select getnewid(" + visit.getDiancxxb_id() + "),WENZNR,"
				+ newid + " \n" + "  from hetwzb where hetb_id=" + hetxxb_id
				+ "); \n" + "end;";
		con.getInsert(sql);
		Dak(Long.parseLong(newid));
		gethetSelectModels();
		sethetSelectValue(getIDropDownBean(getmobmcSelectModel(), Long
				.parseLong(newid)));
		con.Close();
		// getmobmcSelectModels();
	}

	private void Xinj() {
		// 合同信息
		setXinjht(true);
		Hetxxbean bean = gethetxxbean();
		setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(), -1));// 合同模板赋值
		setJihxzValue(getIDropDownBean(getIJihxzModel(), -1)); // 计划口径
		setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(), -1)); // 实际供方(供应商表)
		((Visit) getPage().getVisit()).setDropDownBean19(getIDropDownBean(
				getxufModel(), -1)); // 需方(电厂信息表)
		((Visit) getPage().getVisit()).setDropDownBean18(getIDropDownBean(
				getgongfModel(), -1)); // 供方信息(供应商表)
		bean.setGONGFDBGH("");
		bean.setGONGFDH("");
		bean.setGONGFDWDZ("");
		bean.setGONGFDWMC("");
		bean.setGONGFFDDBR("");
		bean.setGONGFKHYH("");
		bean.setGongfsh("");
		bean.setGONGFWTDLR("");
		bean.setGONGFYZBM("");
		bean.setGONGFZH("");
		bean.setGuoqsj(null);
		bean.setHetbh("");
		// bean.setHetyj("");
		bean.setQianddd("");
		bean.setQiandsj(null);
		bean.setShengxsj(null);
		bean.setXUFDBGH("");
		bean.setXUFDH("");
		bean.setXUFDWDZ("");
		bean.setXUFDWMC("");
		bean.setXUFFDDBR("");
		bean.setXUFKHYH("");
		bean.setXufsh("");
		bean.setXUFWTDLR("");
		bean.setXUFYZBM("");
		bean.setXUFZH("");
		setFahr("");
		// 数量信息
		geteditValuess().clear();
		// 质量信息
		geteditValuesz().clear();
		// 价格信息
		geteditValuesg().clear();
		// 增扣款信息
		geteditValuesj().clear();
		// 文字信息
		setWenz("");
//		变量信息
		setExtGrid(null);
        // 附件状态
        this.setFujzt(0);
        setFujScript("");
	}
    private void setFujzt(long hetb_id) {
        setShenpwjzt("<input type=\"radio\" disabled=\"true\" />审批文件");
        setQianswjzt("<input type=\"radio\" disabled=\"true\" />签署文件");

        // 设置附件状态
        if (hetb_id == 0) {
            // 说明是新增的合同，附件状态全部置为“空”
        } else {
            // 打开已有的合同
            JDBCcon con = new JDBCcon();
            String sql = "select fenl,count(mingc) as fenljs from hetfjb where hetid = "
                    + hetb_id + " group by fenl order by fenl";
            ResultSetList rsl = con.getResultSetList(sql);
            if (rsl.getRows() > 0) {
                // 说明有附件
                while (rsl.next()) {
                    if (rsl.getInt("fenl") == 0 && rsl.getInt("fenljs") >= 1) {
                        setShenpwjzt("<input type=\"radio\" disabled=\"true\" checked=\"true\" />审批文件");
                    } else if (rsl.getInt("fenl") == 1
                            && rsl.getInt("fenljs") >= 1) {
                        setQianswjzt("<input type=\"radio\" disabled=\"true\" checked=\"true\" />签署文件");
                    }
                }
            }
            rsl.close();
            con.Close();
        }
    }
    public void setShenpwjzt(String value) {

        ((Visit) getPage().getVisit()).setString5(value);
    }

    public String getShenpwjzt() {

        return ((Visit) getPage().getVisit()).getString5();
    }
    public void setQianswjzt(String value) {

        ((Visit) getPage().getVisit()).setString6(value);
    }

    public String getQianswjzt() {

        return ((Visit) getPage().getVisit()).getString6();
    }
    public void setFujScript(String value) {

        ((Visit) getPage().getVisit()).setString7(value);
    }
    public String getFujScript() {

        return ((Visit) getPage().getVisit()).getString7();
    }
	private void Dak(long hetmb_id) {
		setXinjht(false);
		String sql = "";
		JDBCcon con = new JDBCcon();
		try {
			// 合同信息
			sql = "select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,"
					+ "GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,"
					+ "XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ,"
					+ "JIHKJB_ID,diancxxb_id,hetb_mb_id,meikmcs "
					+ "from hetb"
					+ " where ID=" + hetmb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				Hetxxbean bean = gethetxxbean();
				setJihxzValue(getIDropDownBean(getIJihxzModel(), rs
						.getLong("JIHKJB_ID")));
				// setmobmc(rs.getString("MINGC"));
				setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),
						rs.getLong("GONGYSB_ID")));
				((Visit) getPage().getVisit())
						.setDropDownBean19(getIDropDownBean(getxufModel(), rs
								.getLong("diancxxb_id")));
				((Visit) getPage().getVisit())
						.setDropDownBean18(getIDropDownBean(getgongfModel(), rs
								.getLong("HETGYSBID")));
				// setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),rs.getLong("hetb_mb_id")));
				bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
				bean.setGONGFDH(rs.getString("GONGFDH"));
				bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
				bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
				bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
				bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
				bean.setGongfsh(rs.getString("Gongfsh"));
				bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
				bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
				bean.setGONGFZH(rs.getString("GONGFZH"));
				bean.setGuoqsj(rs.getDate("GUOQRQ"));
				bean.setHetbh(rs.getString("Hetbh"));
				// bean.setHetyj(rs.getString("Hetyj"));
				bean.setQianddd(rs.getString("Qianddd"));
				bean.setQiandsj(rs.getDate("QIANDRQ"));
				bean.setShengxsj(rs.getDate("QISRQ"));
				bean.setXUFDBGH(rs.getString("XUFDBGH"));
				bean.setXUFDH(rs.getString("XUFDH"));
				bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
				bean.setXUFDWMC(rs.getString("XUFDWMC"));
				bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
				bean.setXUFKHYH(rs.getString("XUFKHYH"));
				bean.setXufsh(rs.getString("Xufsh"));
				bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
				bean.setXUFYZBM(rs.getString("XUFYZBM"));
				bean.setXUFZH(rs.getString("XUFZH"));
				setFahr(rs.getString("meikmcs"));
			}
			// 数量信息
			List list = geteditValuess();
			list.clear();
			sql = "select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n"
					+ " y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n"
					+ ",diancxxb.mingc shouhr\n"
					+ "from(\n"
					+ "\n"
					+ "select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n"
					+ "y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n"
					+ "from\n"
					+ "    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n"
					+ "    from hetslb\n"
					+ "    where hetb_id="
					+ hetmb_id
					+ "\n"
					+ "    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n"
					+ "    )a,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,1 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='01'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y1,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,2 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='02'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y2,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,3 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='03'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y3,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,4 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='04'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y4,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,5 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='05'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y5,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,6as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='06'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y6,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,7 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='07'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y7,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,8 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='08'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y8,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,9 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='09'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y9,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,10 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='10'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y10,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,11 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='11'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y11,\n"
					+ "    ----------\n"
					+ "    (select DISTINCT hetb_id,id,12 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='12'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y12\n"
					+ "\n"
					+ "------------------------------------\n"
					+ "where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n"
					+ "and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n"
					+ "and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n"
					+ "and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n"
					+ "and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n"
					+ "and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n"
					+ "and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n"
					+ "and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n"
					+ "and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n"
					+ "and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n"
					+ "and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n"
					+ "and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n"
					+ ")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n"
					+ "where  aa.pinzb_id=pinzb.id and aa.yunsfsb_id=yunsfsb.id\n"
					+ "and faz.id=aa.faz_id and aa.daoz_id=daoz.id and diancxxb.id=aa.diancxxb_id";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				String pinz = rs.getString("pinz");
				String yunsfs = rs.getString("yunsfs");
				String faz = rs.getString("faz");
				String daoz = rs.getString("daoz");
				String shouhr = rs.getString("shouhr");
				long id = rs.getLong("id");
				long Y1 = rs.getLong("Y1");
				long Y2 = rs.getLong("Y2");
				long Y3 = rs.getLong("Y3");
				long Y4 = rs.getLong("Y4");
				long Y5 = rs.getLong("Y5");
				long Y6 = rs.getLong("Y6");
				long Y7 = rs.getLong("Y7");
				long Y8 = rs.getLong("Y8");
				long Y9 = rs.getLong("Y9");
				long Y10 = rs.getLong("Y10");
				long Y11 = rs.getLong("Y11");
				long Y12 = rs.getLong("Y12");
				long hej = Y1 + Y2 + Y3 + Y4 + Y5 + Y6 + Y7 + Y8 + Y9 + Y10
						+ Y11 + Y12;
				list.add(new Fahxxbean(id, pinz, yunsfs, faz, daoz, shouhr,
								hej, Y1, Y2, Y3, Y4, Y5, Y6, Y7, Y8, Y9, Y10,
								Y11, Y12));
			}
			// 质量信息
			list = geteditValuesz();
			list.clear();
			sql = "select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n"
					+ "from hetzlb,zhibb,tiaojb,danwb\n"
					+ "where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="
					+ hetmb_id;
			rs = con.getResultSet(sql);
			int i = 0;
			while (rs.next()) {
				String zhib = rs.getString("zhib");
				String tiaoj = rs.getString("tiaoj");
				double shangx = rs.getDouble("shangx");
				double xiax = rs.getDouble("xiax");
				String danw = rs.getString("danw");
				long id = rs.getLong("id");
				list.add(new Zhilyqbean(++i, id, zhib, tiaoj, shangx, xiax,
						danw));
			}
			
//			 杂费信息
			if (MainGlobal.getXitxx_item("合同", "是否在煤款合同中录入杂费信息", String.valueOf(((Visit)getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
//				如果配制了“是否在煤款合同中录入杂费信息”参数，那么在购煤合同中显示杂费Tab，并且打开合同时也要打开已经录入的杂费信息
				list = geteditValueszf();
				list.clear();
				sql = "select zf.id, it.mingc, decode(zf.diancjszf, 1, '是', '否') diancjszf\n" +
					"  from hetzfb zf, item it\n" + 
					" where zf.hetb_id = "+ hetmb_id +"\n" + 
					"   and zf.item_id = it.id";
				rs = con.getResultSet(sql);
				i = 0;
				while (rs.next()) {
					String zafmc = rs.getString("mingc");
					String diancjszf = rs.getString("diancjszf");
					long id = rs.getLong("id");
					list.add(new Zafxxbean(++i, id, zafmc, diancjszf));
				}
			}
			
			// 价格信息
			list = geteditValuesg();
			list.clear();
			sql = "select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,jijgs,\n"
					+ "hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'是',2,'否','')YINGDKF,\n"
					+ "yunsfsb.mingc yunsfsmc,ZUIGMJ,zuigmjdw.mingc zuigmjdw,fengsjj,fengsjjdw.mingc fengsjjdw,decode(jijlx,0,'含税单价',1,'不含税单价') as jijlx ,pinzb.mingc pinz\n"
					+ "from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,danwb zuigmjdw,danwb fengsjjdw,yunsfsb,pinzb\n"
					+ "where hetjgb.pinzb_id=pinzb.id(+) and hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n"
					+ "and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n"
					+ "and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.zuigmjdw = zuigmjdw.id(+) "
					+ "and hetjgb.fengsjjdw = fengsjjdw.id(+) and hetjgb.hetb_id="+ hetmb_id + "order by zhibb.mingc,xiax";
			rs = con.getResultSet(sql);
			i = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				String zhibmc = rs.getString("zhibmc");
				String tiaojmc = rs.getString("tiaojmc");
				double XIAX = rs.getDouble("XIAX");
				double SHANGX = rs.getDouble("SHANGX");
				String zhibdwmc = rs.getString("zhibdwmc");
				double JIJ = rs.getDouble("JIJ");
				String jijdwmc = rs.getString("jijdwmc");
				String jijgs = rs.getString("jijgs");
				String hetjjfsmc = rs.getString("hetjjfsmc");
				String hetjsfsmc = rs.getString("hetjsfsmc");
				String hetjsxsmc = rs.getString("hetjsxsmc");
				double YUNJ = rs.getDouble("YUNJ");
				String yunjdwmc = rs.getString("yunjdwmc");
				String YINGDKF = rs.getString("YINGDKF");
				String yunsfsmc = rs.getString("yunsfsmc");
				double ZUIGMJ = rs.getDouble("ZUIGMJ");
				String zuigmjdw = rs.getString("zuigmjdw");
				double fengsjj = rs.getDouble("fengsjj");
				String fengsjjdw = rs.getString("fengsjjdw");
				String jijlx = rs.getString("jijlx");
				String pinz = rs.getString("pinz");

				list.add(new jijbean(++i, id, zhibmc, tiaojmc, SHANGX, XIAX,
						zhibdwmc, JIJ, jijdwmc, jijgs, hetjsfsmc, hetjjfsmc,
						hetjsxsmc, YUNJ, yunjdwmc, YINGDKF, yunsfsmc, ZUIGMJ,
						zuigmjdw, fengsjj, fengsjjdw, jijlx, pinz));
			}
			// 增扣款信息
			list = geteditValuesj();
			list.clear();
			sql = "select z.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n"
					+ "KOUJ,koujgs,koujdw.mingc koujdwmc,ZENGFJ,zengfjgs,zengfjdw.mingc zengfjdwmc,\n"
					+ "decode(XIAOSCL,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)',6,'四舍五入(0.001)',7,'四舍五入(0.0001)',8,'四舍五入(0.000001)','')XIAOSCL,JIZZKJ,JIZZB\n"
					+ ",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ,\n"
					+ "decode(z.SHIYFW,0,'全部','超出部分')SHIYFW,pinzb.mingc as pinz, decode(z.jijlx, 0, '含税单价', '不含税单价') jijlx \n"
					+ "from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb,pinzb\n"
					+ "where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n"
					+ "and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n"
					+ "and z.yunsfsb_id=yunsfsb.id(+) and z.pinzb_id=pinzb.id(+) and z.hetb_id="
					+ hetmb_id + " order by zhibb.mingc,z.xiax";
			rs = con.getResultSet(sql);
			i = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				String zhibmc = rs.getString("zhibmc");
				String tiaojmc = rs.getString("tiaojmc");
				double XIAX = rs.getDouble("XIAX");
				double SHANGX = rs.getDouble("SHANGX");
				String zhibdwmc = rs.getString("zhibdwmc");
				double JIS = rs.getDouble("JIS");
				String jisdwmc = rs.getString("jisdwmc");
				double KOUJ = rs.getDouble("KOUJ");
				String koujgs = rs.getString("koujgs");
				String koujdwmc = rs.getString("koujdwmc");
				double ZENGFJ = rs.getDouble("ZENGFJ");
				String zengfjgs = rs.getString("zengfjgs");
				String zengfjdwmc = rs.getString("zengfjdwmc");
				String XIAOSCL = rs.getString("XIAOSCL");
				double JIZZKJ = rs.getDouble("JIZZKJ");
				double JIZZB = rs.getDouble("JIZZB");
				String canzxmmc = rs.getString("canzxmmc");
				String canzxmdwmc = rs.getString("canzxmdwmc");
				double CANZSX = rs.getDouble("CANZSX");
				double CANZXX = rs.getDouble("CANZXX");
				String hetjsxsmc = rs.getString("hetjsxsmc");
				String yunsfsmc = rs.getString("yunsfsmc");
				String pinzb_id = rs.getString("pinz");
				String BEIZ = rs.getString("BEIZ");
				String SHIYFW = rs.getString("SHIYFW");
				String jijlx = rs.getString("jijlx");
				list.add(new Zengkkbean(id, ++i, zhibmc, tiaojmc, SHANGX, XIAX,
						zhibdwmc, JIS, jisdwmc, KOUJ, koujgs ,koujdwmc, ZENGFJ,
						zengfjgs, zengfjdwmc, XIAOSCL, JIZZKJ, JIZZB, canzxmmc,
						canzxmdwmc, CANZSX, CANZXX, hetjsxsmc, yunsfsmc, pinzb_id,
						SHIYFW, BEIZ, jijlx));
			}

			// 文字信息
			sql = "select id,wenznr\n" + "from hetwzb\n" + "where hetb_id="
					+ hetmb_id;
			rs = con.getResultSet(sql);
			if (rs.next()) {
				setWenz(rs.getString("wenznr"));
			}
			rs.close();
			
			Dak_Hetbl(hetmb_id);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	public String _xiaosw;

	public void setXiaosw(String _value) {
		_xiaosw = _value;
	}

	public String getXiaosw() {
		Visit visit = (Visit) this.getPage().getVisit();
		String xsw = "3";
		xsw = MainGlobal.getXitxx_item("合同", "扣价和增付价字段要保留的小数位", String
				.valueOf(visit.getDiancxxb_id()), "3");
		return xsw;
	}

	private void getGongf() {
		Hetxxbean htxxbean = gethetxxbean();
		JDBCcon con = new JDBCcon();
		String sql = "select quanc,danwdz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH,chuanz\n"
				+ "from gongysb where id=" + getgongfValue().getId() +"  order by quanc ";
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				String Gongfdn = rs.getString("DIANH");
				String Gongfdwdz = rs.getString("danwdz");
				String Gongfdwmc = rs.getString("quanc");
				String Gongffddbr = rs.getString("FADDBR");
				String Gongfzh = rs.getString("ZHANGH");
				String Gongfkhyh = rs.getString("KAIHYH");
				String Gongfyzbm = rs.getString("YOUZBM");
				String Gongfwtdlr = rs.getString("WEITDLR");
				String shuih = rs.getString("shuih");
				String chuanz = rs.getString("chuanz");

				htxxbean.setGONGFDH(Gongfdn);
				htxxbean.setGONGFDWDZ(Gongfdwdz);
				htxxbean.setGONGFDWMC(Gongfdwmc);
				htxxbean.setGONGFFDDBR(Gongffddbr);
				htxxbean.setGONGFZH(Gongfzh);
				htxxbean.setGONGFKHYH(Gongfkhyh);
				htxxbean.setGONGFYZBM(Gongfyzbm);
				htxxbean.setGONGFWTDLR(Gongfwtdlr);
				htxxbean.setGongfsh(shuih);
				htxxbean.setGONGFDBGH(chuanz);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		con.Close();
		// 发货人同时相同
		setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),
				getgongfValue().getId()));
		getMeiksselectModels();
	}

	private void getXuf() {
		Hetxxbean htxxbean = gethetxxbean();
		JDBCcon con = new JDBCcon();
		String sql = "";
		if (((Visit) getPage().getVisit()).getString4().equals("XS")) {
			sql = "select quanc,diz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n"
					+ "from vwxufdw where id=" + getxufValue().getId()+"  order by quanc";
		} else if (((Visit) getPage().getVisit()).getString4().equals("CG")) {
			sql = "select quanc,diz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n"
					+ "from diancxxb where id=" + getxufValue().getId()+"   order by xuh ";
		}
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				String XUFDH = rs.getString("DIANH");
				String XUFDWDZ = rs.getString("DIZ");
				String XUFDWMC = rs.getString("QUANC");
				String XUFFDDBR = rs.getString("FADDBR");
				String XUFZH = rs.getString("ZHANGH");
				String XUFKHYH = rs.getString("KAIHYH");
				String XUFYZBM = rs.getString("YOUZBM");
				String XUFWTDLR = rs.getString("WEITDLR");
				String XUFSH = rs.getString("shuih");
				htxxbean.setXUFDH(XUFDH);
				htxxbean.setXUFDWDZ(XUFDWDZ);
				htxxbean.setXUFDWMC(XUFDWMC);
				htxxbean.setXUFFDDBR(XUFFDDBR);
				htxxbean.setXUFZH(XUFZH);
				htxxbean.setXUFKHYH(XUFKHYH);
				htxxbean.setXUFYZBM(XUFYZBM);
				htxxbean.setXUFWTDLR(XUFWTDLR);
				htxxbean.setXufsh(XUFSH);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
    /*
     * 获取三级审批状态
     */
    private long sanjzt(long hetb_id) {
        long zhuant = 0;
        JDBCcon con = new JDBCcon();
        String sql = "select sanj_zt from  hetb where id = " + hetb_id;
        ResultSet rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
                zhuant = rs.getLong("sanj_zt");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return zhuant;
    }
    private String SetHetshztFromHetid(long Hetb_id) {

        String Hetshzt = "";
        JDBCcon con = new JDBCcon();
        String sql = "select * from hetshzt where hetid = " + Hetb_id
                + " and diancxxb_id = "
                + ((Visit) getPage().getVisit()).getDiancxxb_id();
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next() && rsl.getInt("zhuangt") >= 0) {

            Hetshzt = "1";
        } else {

            Hetshzt = "0";
        }
        rsl.close();
        con.Close();
        return Hetshzt;
    }
	private void getXuanzht() {
//        String zhuangt = "";
		// 选择合同
		if (gethetSelectValue().getId() == -1) {
			// 没有选择已有合同，认为是新建合同。
			Xinj();
		} else {
//            zhuangt = sanjzt(gethetSelectValue().getId()) + "";
//            setHetShzt(zhuangt);
			// 已选择了合同，进入合同维护功能
			Dak(gethetSelectValue().getId());
		}
	}

	private void Tij() {
		Baoc();// 提交先进行保存工作
		String sql = "";
		long hetb_id = gethetxxbean().getId();
		JDBCcon con = new JDBCcon();
		long liucb_id = 0;
		// 提交后进入相应流程的初始状态（xuh＝1）
		// long chuszt=0;
		// sql="select liucztb.id\n" +
		// "from hetb,hetb_mb,liucztb\n" +
		// "where hetb.hetb_mb_id=hetb_mb.id and
		// hetb_mb.liucb_id=liucztb.liucb_id and xuh=1 and hetb.id="+hetb_id;
		// ResultSet rs=con.getResultSet(sql);
		// try{
		// if(rs.next()){
		// chuszt=rs.getLong("id");
		// }
		// }catch(Exception e){
		// e.printStackTrace();
		// return;
		// }
		// sql="update hetb set liucztb_id=" +chuszt
		// +" where hetb.id="+hetb_id;
		// con.getUpdate(sql);
		// con.Close();

		sql = "select liucb.id\n"
				+ "from hetb,hetb_mb,liucb\n"
				+ "where hetb.hetb_mb_id=hetb_mb.id and hetb_mb.liucb_id=liucb.id and hetb.id="
				+ hetb_id ;
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				liucb_id = rs.getLong("id");
			}
			Liuc.tij("hetb", hetb_id, ((Visit) getPage().getVisit())
					.getRenyID(), "", liucb_id);
			gethetSelectModels();
			Xinj();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			con.Close();
		}
	}

	private void Shanc() {
		String sql = "";
		JDBCcon con = new JDBCcon();
		long hetb_id = gethetSelectValue().getId();
		// 删除合同信息表
		sql = "select  xiaf from hetb where hetb.id=" + hetb_id ;
		ResultSet rs = con.getResultSet(sql);
		int xiaf = 0;
		try {
			if (rs.next()) {
				xiaf = rs.getInt("xiaf");
			}
			if (xiaf != 0) {// 下发过不可以删除
				return;
			}
			con.setAutoCommit(false);
			sql = "delete\n" + "from hetb\n" + "where id=" + hetb_id;
			con.getDelete(sql);
			sql = "delete\n" + "from hetslb\n" + "where hetslb.hetb_id="
					+ hetb_id;
			con.getDelete(sql);
			sql = "delete \n" + "from hetzlb\n" + "where hetzlb.hetb_id="
					+ hetb_id;
			con.getDelete(sql);
			sql = "delete \n" + "from hetjgb\n" + "where hetjgb.hetb_id="
					+ hetb_id;
			con.getDelete(sql);
			sql = "delete\n" + "from hetwzb\n" + "where hetwzb.hetb_id="
					+ hetb_id;
			con.getDelete(sql);
			sql = "delete from hetblb where hetb_id="+ hetb_id;
			con.getDelete(sql);
			
//			删除杂费信息
			if (MainGlobal.getXitxx_item("合同", "是否在煤款合同中录入杂费信息", String.valueOf(((Visit)getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
//				如果配制了“是否在煤款合同中录入杂费信息”参数，那么在购煤合同中显示杂费Tab，并且在删除合同时也要删除杂费信息
				sql = "delete from hetzfb where hetb_id="+ hetb_id;
				con.getDelete(sql);
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			con.rollBack();
		} finally {
			con.Close();
		}
		Xinj();
		gethetSelectModels();
	}

	private void Baoc() {
		JDBCcon con = new JDBCcon();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Hetxxbean bean = gethetxxbean();
		String sql = "";
		int mleib = 0; // 合同类别（0电厂与煤矿，1电厂与分公司，2分公司采购合同）

		if (((Visit) getPage().getVisit()).getString4().equals("CG")
				&& ((Visit) getPage().getVisit()).getRenyjb() < SysConstant.JIB_DC) {
			// 如果是采购合同，用户级别又高于电厂，认为是分公司与矿方的合同，leib=2
			mleib = 2;
		} else if (((Visit) getPage().getVisit()).getString4().equals("XS")) {

			// 两种情况：1、电厂录入，供方不是上级公司，我们认为这就是和煤矿签订的合同，类别为0
			// 2、电厂录入，供方为上级公司，我们认为这就是和分公司签订的合同，类别为1
			// 3、分公司或集团录入供方为它本身，我们认为这就是和分公司签订的合同，类别为1
			try {
				if (((Visit) getPage().getVisit()).getRenyjb() == SysConstant.JIB_DC
						&& !String
								.valueOf(getgongfValue().getValue())
								.trim()
								.equals(
										MainGlobal
												.getTableCol(
														"diancxxb",
														"mingc",
														"id",
														"'||(select fuid from diancxxb where id="
																+ ((Visit) getPage()
																		.getVisit())
																		.getDiancxxb_id()
																+ ")||'")
												.trim())) {

					mleib = 0;
				} else if (((Visit) getPage().getVisit()).getRenyjb() == SysConstant.JIB_DC
						&& String
								.valueOf(getgongfValue().getValue())
								.trim()
								.equals(
										MainGlobal
												.getTableCol(
														"diancxxb",
														"mingc",
														"id",
														"'||(select fuid from diancxxb where id="
																+ ((Visit) getPage()
																		.getVisit())
																		.getDiancxxb_id()
																+ ")||'")
												.trim())) {

					mleib = 1;
				} else if (((Visit) getPage().getVisit()).getRenyjb() < SysConstant.JIB_DC
						&& String
								.valueOf(getgongfValue().getValue().trim())
								.equals(
										MainGlobal
												.getTableCol(
														"diancxxb",
														"mingc",
														"id",
														String
																.valueOf(((Visit) getPage()
																		.getVisit())
																		.getDiancxxb_id()))
												.trim())) {

					mleib = 1;
				}
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
        String hetbh=bean.getHetbh();
		String htsql="select id from hetb where hetbh='"+hetbh+"'";
		ResultSet rs=con.getResultSet(htsql);
        try {
            if (!rs.next()) {// 如果是新合同

                if(this.getmobmcSelectValue().getId()==-1){

                }

                // 插入（新增）保存
                // 保存合同信息（包括供方需方）
                long hetb_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql = "insert into hetb(ID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,"
                        + "GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,"
                        + "XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ,JIHKJB_ID,hetb_mb_id,liucgzid,leib,meikmcs)\n"
                        + "values("
                        + hetb_id
                        + ","
                        + getxufValue().getId()
                        + ",'"
                        + bean.getHetbh()
                        + "',to_date('"
                        + format.format(bean.getQiandsj())
                        + "','YYYY-MM-DD'),'"
                        + bean.getQianddd()
                        + "','"
                        + bean.getGONGFDWMC()
                        + "','"
                        + bean.getGONGFDWDZ()
                        + "','"
                        + bean.getGONGFDH()
                        + "','"
                        + bean.getGONGFFDDBR()
                        + "','"
                        + bean.getGONGFWTDLR()
                        + "','"
                        + bean.getGONGFDBGH()
                        + "','"
                        + bean.getGONGFKHYH()
                        + "','"
                        + bean.getGONGFZH()
                        + "','"
                        + bean.getGONGFYZBM()
                        + "','"
                        + bean.getGongfsh()
                        + "','"
                        + bean.getXUFDWMC()
                        + "','"
                        + bean.getXUFDWDZ()
                        + "','"
                        + bean.getXUFFDDBR()
                        + "','"
                        + bean.getXUFWTDLR()
                        + "','"
                        + bean.getXUFDH()
                        + "','"
                        + bean.getXUFDBGH()
                        + "','"
                        + bean.getXUFKHYH()
                        + "','"
                        + bean.getXUFZH()
                        + "','"
                        + bean.getXUFYZBM()
                        + "','"
                        + bean.getXufsh()
                        + "',"
                        + getgongfValue().getId()
                        + ","
                        + getshijgfSelectValue().getId()
                        + ",to_date('"
                        + format.format(bean.getShengxsj())
                        + "','YYYY-MM-DD'),to_date('"
                        + format.format(bean.getGuoqsj())
                        + "','YYYY-MM-DD'),"
                        + getJihxzValue().getId()
                        + ","
                        + getmobmcSelectValue().getId() // 模版id
                        // +","
                        // +0//结算形式和结算方式表有待于拓展暂不考虑
                        // +","
                        // +0
                        // +","
                        // +0
                        + ",0,"// 流程跟踪表id
                        + mleib + ",'" + getFahr() + "')";
                con.getInsert(sql);
                bean.setId(hetb_id);
                // 保存数量
                List list = geteditValuess();
                long[] Y = new long[12];
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    String Pinz = ((Fahxxbean) list.get(i)).getPinz();
                    String Yunsfs = ((Fahxxbean) list.get(i)).getYunsfs();
                    String Faz = ((Fahxxbean) list.get(i)).getFaz();
                    String Daoz = ((Fahxxbean) list.get(i)).getDaoz();
                    String Shouhr = ((Fahxxbean) list.get(i)).getShouhr();
                    Shouhr = Shouhr.substring(Shouhr.lastIndexOf(";") + 1, Shouhr
                            .length());
                    ((Fahxxbean) list.get(i)).setShouhr(Shouhr);
                    Y[0] = ((Fahxxbean) list.get(i)).getY1();
                    Y[1] = ((Fahxxbean) list.get(i)).getY2();
                    Y[2] = ((Fahxxbean) list.get(i)).getY3();
                    Y[3] = ((Fahxxbean) list.get(i)).getY4();
                    Y[4] = ((Fahxxbean) list.get(i)).getY5();
                    Y[5] = ((Fahxxbean) list.get(i)).getY6();
                    Y[6] = ((Fahxxbean) list.get(i)).getY7();
                    Y[7] = ((Fahxxbean) list.get(i)).getY8();
                    Y[8] = ((Fahxxbean) list.get(i)).getY9();
                    Y[9] = ((Fahxxbean) list.get(i)).getY10();
                    Y[10] = ((Fahxxbean) list.get(i)).getY11();
                    Y[11] = ((Fahxxbean) list.get(i)).getY12();
                    long hetslb_id = Long.parseLong(MainGlobal
                            .getNewID(((Visit) getPage().getVisit())
                                    .getDiancxxb_id()));
                    for (int j = 0; j < 12; j++) {
                        if (Y[j] != 0) {
                            sql = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values("
                                    + +hetslb_id
                                    + ",(select id from pinzb where mingc='"
                                    + Pinz
                                    + "'),(select id from yunsfsb where mingc='"
                                    + Yunsfs
                                    + "'),(select id from chezxxb where mingc='"
                                    + Faz
                                    + "'),(select id from chezxxb where mingc='"
                                    + Daoz
                                    + "'),(select id from diancxxb where mingc='"
                                    + Shouhr
                                    + "'),to_date('"
                                    + String.valueOf(DateUtil.getYear(bean
                                            .getShengxsj()))
                                    + (j + 1)
                                    + "','YYYYMM')," + Y[j] + "," + hetb_id + ")";
                            con.getInsert(sql);
                        }
                    }
                }
                // 保存质量
                list = geteditValuesz();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    long hetzlb_id = Long.parseLong(MainGlobal
                            .getNewID(((Visit) getPage().getVisit())
                                    .getDiancxxb_id()));
                    String zhib = ((Zhilyqbean) list.get(i)).getMingc();
                    String tiaoj = ((Zhilyqbean) list.get(i)).getTiaoj();
                    double shangx = ((Zhilyqbean) list.get(i)).getShangx();
                    double xiax = ((Zhilyqbean) list.get(i)).getXiax();
                    String danw = ((Zhilyqbean) list.get(i)).getDanw();
                    sql = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values("
                            + hetzlb_id
                            + ",(select id from zhibb where mingc='"
                            + zhib
                            + "' and leib=1),(select id from tiaojb where mingc='"
                            + tiaoj
                            + "'),"
                            + shangx
                            + ","
                            + xiax
                            + ",(select max(id) from danwb where mingc='"
                            + danw
                            + "')," + hetb_id + ")";
                    con.getInsert(sql);
                }

                // 保存杂费
                list = geteditValueszf();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    long hetzfb_id = Long.parseLong(MainGlobal
                            .getNewID(((Visit) getPage().getVisit())
                                    .getDiancxxb_id()));
                    String zafmc = ((Zafxxbean) list.get(i)).getItem_id();
                    String diancjs = ((Zafxxbean) list.get(i)).getDiancjszf();
                    sql = "insert into hetzfb(id, hetb_id, item_id, diancjszf) values("
                        + hetzfb_id
                        + ", "
                        + hetb_id
                        + ", (select it.id from item it where it.itemsortid = (select id from itemsort ist where ist.bianm = 'DZZF') and it.mingc = '"
                        + zafmc
                        + "'), (select decode('"
                        + diancjs
                        +"', '是', 1, 0) from dual))";
                    con.getInsert(sql);
                }

                // 保存价格
                list = geteditValuesg();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    String zhibb_id = ((jijbean) list.get(i)).getZhibb_id();
                    String tiaojb_id = ((jijbean) list.get(i)).getTiaojb_id();
                    double shangx = ((jijbean) list.get(i)).getShangx();
                    double xiax = ((jijbean) list.get(i)).getXiax();
                    String zhibdw = ((jijbean) list.get(i)).getDanwb_id();
                    double jij = ((jijbean) list.get(i)).getJij();
                    String jijdw = ((jijbean) list.get(i)).getJijdwid();
                    String jijgs = ((jijbean) list.get(i)).getJijgs();
                    String hetjsfsb_id = ((jijbean) list.get(i)).getHetjsfsb_id();
                    String Hetjsxsb_id = ((jijbean) list.get(i)).getHetjsxsb_id();
                    double yunj = ((jijbean) list.get(i)).getYunj();
                    String yunjdw = ((jijbean) list.get(i)).getYunjdw_id();
                    String yingdkf = ((jijbean) list.get(i)).getYingdkf();
                    String Yunsfsb_id = ((jijbean) list.get(i)).getYunsfsb_id();
                    double zuigmj = ((jijbean) list.get(i)).getZuigmj();
                    String zuigmjdw = ((jijbean) list.get(i)).getZuigmjdw();
                    String hetjjfsb_id = ((jijbean) list.get(i)).getHetjjfsb_id();
                    double fengsjj = ((jijbean) list.get(i)).getFengsjj(); // 分公司加价
                    String fengsjjdw = ((jijbean) list.get(i)).getFengsjjdw();
                    String jijlx = ((jijbean) list.get(i)).getJijlx(); // 基价类型
                    String pinz = ((jijbean) list.get(i)).getPinz();// 品种
                    sql = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n"
                            + "YINGDKF,YUNSFSB_ID,ZUIGMJ,ZUIGMJDW,HETB_ID,HETJJFSB_ID,FENGSJJ,FENGSJJDW,JIJLX,pinzb_id) values("
                            + "getnewid("
                            + ((Visit) getPage().getVisit()).getDiancxxb_id()
                            + ")"
                            + ",(select id from zhibb where mingc='"
                            + zhibb_id
                            + "' and leib=1),(select id from tiaojb where mingc='"
                            + tiaojb_id
                            + "'),"
                            + shangx
                            + ","
                            + xiax
                            + ",(select max(id) from danwb where mingc='"
                            + zhibdw
                            + "'),"
                            + jij
                            + ",(select max(id) from danwb where mingc='"
                            + jijdw
                            + "'),'"
                            + jijgs
                            + "',(select id from hetjsfsb where mingc='"
                            + hetjsfsb_id
                            + "'),(select id from hetjsxsb where mingc='"
                            + Hetjsxsb_id
                            + "'),"
                            + yunj
                            + ",(select max(id) from danwb where mingc='"
                            + yunjdw
                            + "'),"
                            + getProperId(getyingdkfgSelectModel(), yingdkf)
                            + ",(select id from yunsfsb where mingc='"
                            + Yunsfsb_id
                            + "'),"
                            + zuigmj
                            + ",(select max(id) from danwb where mingc='"
                            + zuigmjdw
                            + "'),"
                            + hetb_id
                            + ",(select id from hetjjfsb where mingc='"
                            + hetjjfsb_id
                            + "'),"
                            + fengsjj
                            + ",(select max(id) from danwb where mingc='"
                            + fengsjjdw
                            + "'),"
                            + this.getProperId(this.getIJijlxModel(), jijlx)
                            + ",(select max(id) from pinzb where mingc='"
                            + pinz
                            + "'))";
                    con.getInsert(sql);
                }
                // 保存增扣价格
                list = geteditValuesj();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    String zhibb_id = ((Zengkkbean) list.get(i)).getZHIBB_ID();
                    String tiaojb_id = ((Zengkkbean) list.get(i)).getTIAOJB_ID();
                    double shangx = ((Zengkkbean) list.get(i)).getSHANGX();
                    double xiax = ((Zengkkbean) list.get(i)).getXIAX();
                    String zhibdw = ((Zengkkbean) list.get(i)).getDANWB_ID();
                    double jis = ((Zengkkbean) list.get(i)).getJIS();
                    String jisdwb_id = ((Zengkkbean) list.get(i)).getJISDWID();
                    double kouj = ((Zengkkbean) list.get(i)).getKOUJ();
                    String koujgs = ((Zengkkbean) list.get(i)).getKoujgs();
                    String koujdw = ((Zengkkbean) list.get(i)).getKOUJDW();
                    double zengfj = ((Zengkkbean) list.get(i)).getZENGFJ();
                    String zengfjgs = ((Zengkkbean) list.get(i)).getZengfjgs();
                    String zengfjdw = ((Zengkkbean) list.get(i)).getZENGFJDW();
                    String xiaoscl = ((Zengkkbean) list.get(i)).getXIAOSCL();
                    double jizzb = ((Zengkkbean) list.get(i)).getJIZZB();
                    double jizzkj = ((Zengkkbean) list.get(i)).getJIZZKJ();
                    String canzxm = ((Zengkkbean) list.get(i)).getCANZXM();
                    String canzxmdw = ((Zengkkbean) list.get(i)).getCANZXMDW();
                    double canzsx = ((Zengkkbean) list.get(i)).getCANZSX();
                    double canzxx = ((Zengkkbean) list.get(i)).getCANZXX();
                    String hetjsxsb_id = ((Zengkkbean) list.get(i)).getJIESXXB_ID();
                    String yunsfsb_id = ((Zengkkbean) list.get(i)).getYUNSFSB_ID();
                    String beiz = ((Zengkkbean) list.get(i)).getBEIZ();
                    String SHIYFW = ((Zengkkbean) list.get(i)).getShiyfw();
                    String pinzb_id = ((Zengkkbean) list.get(i)).getPinzb_id();
                    String jijlx = ((Zengkkbean) list.get(i)).getJIJLX();

                    sql = "insert into hetzkkb( ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,"
                            + "ZENGFJGS,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID,"
                            + "SHIYFW,PINZB_ID,JIJLX)"
                            + "values("
                            + "getnewid("
                            + ((Visit) getPage().getVisit()).getDiancxxb_id()
                            + ")"
                            + ",(select id from zhibb where mingc='"
                            + zhibb_id
                            + "' and leib=1),(select id from tiaojb where mingc='"
                            + tiaojb_id
                            + "'),"
                            + shangx
                            + ","
                            + xiax
                            + ",(select max(id) from danwb where mingc='"
                            + zhibdw
                            + "'),"
                            + jis
                            + ",(select max(id) from danwb where mingc='"
                            + jisdwb_id
                            + "'),"
                            + kouj
                            + ",'"
                            + koujgs
                            + "',(select max(id) from danwb where mingc='"
                            + koujdw
                            + "'),"
                            + zengfj
                            + ",'"
                            + zengfjgs
                            + "',(select max(id) from danwb where mingc='"
                            + zengfjdw
                            + "'),"
                            + getProperId(getxiaoswcljSelectModel(), xiaoscl)
                            + ","
                            + jizzkj
                            + ","
                            + jizzb
                            + ",(select id from zhibb where mingc='"
                            + canzxm
                            + "' and leib=1),(select max(id) from danwb where mingc='"
                            + canzxmdw
                            + "'),"
                            + canzsx
                            + ","
                            + canzxx
                            + ",(select id from hetjsxsb where mingc='"
                            + hetjsxsb_id
                            + "'),(select id from yunsfsb where mingc='"
                            + yunsfsb_id
                            + "'),'"
                            + beiz
                            + "',"
                            + hetb_id
                            + ",decode('"
                            + SHIYFW
                            + "','超出部分',1,0),(select max(id) from pinzb where mingc='"
                            +pinzb_id+"'),"
                            +this.getProperId(this.getIJijlxModel(), jijlx) +")";
                    con.getInsert(sql);
                }
                // 保存文字
                long hetwzb_id = Long.parseLong(MainGlobal
                        .getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql = "insert into hetwzb(id,wenznr,hetb_id) values(" + hetwzb_id
                        + ",'" + getWenz() + "'," + hetb_id + ")";
                con.getInsert(sql);
                // 保存后改变合同下拉框，并使选择框定位到新合同名称

//			新建合同_保存合同变量_begin
                sql = "select *　from hetblb where hetb_id="+this.getmobmcSelectValue().getId();
                ResultSetList rsl = con.getResultSetList(sql);
                if(rsl.getRows()>0){
                    StringBuffer sb = new StringBuffer("begin	\n");
                    while(rsl.next()){

                        sb.append("insert into hetblb(id, hetb_id, bianlmc, value)	\n")
                            .append("values	\n")
                            .append("(getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+") ,"
                                    +hetb_id+" , '"+rsl.getString("bianlmc")+"' , '"+rsl.getString("value")+"'); \n");
                    }
                    sb.append("end;");

                    if(sb.length()>13){

                        con.getUpdate(sb.toString());
                    }
                }

                rsl.close();
//			新建合同_保存变量_end
                gethetSelectModels();
                sethetSelectValue(getIDropDownBean(gethetSelectModel(), hetb_id));
            } else {// 如果不是新建合同则是已经存在的合同，这时进行更新操作
                // 保存合同信息（包括供方需方）
                long hetb_id = gethetSelectValue().getId();
                sql = "update hetb set HETBH='" + bean.getHetbh()
                        + "',QIANDRQ=to_date('" + format.format(bean.getQiandsj())
                        + "','YYYY-MM-DD'),QIANDDD='" + bean.getQianddd()
                        + "',GONGFDWMC='" + bean.getGONGFDWMC() + "',GONGFDWDZ='"
                        + bean.getGONGFDWDZ() + "',GONGFDH='" + bean.getGONGFDH()
                        + "',GONGFFDDBR='" + bean.getGONGFFDDBR()
                        + "',GONGFWTDLR='" + bean.getGONGFWTDLR() + "',GONGFDBGH='"
                        + bean.getGONGFDBGH() + "',GONGFKHYH='"
                        + bean.getGONGFKHYH() + "',GONGFZH='" + bean.getGONGFZH()
                        + "',GONGFYZBM='" + bean.getGONGFYZBM() + "',GONGFSH='"
                        + bean.getGongfsh() + "',XUFDWMC='" + bean.getXUFDWMC()
                        + "',XUFDWDZ='" + bean.getXUFDWDZ() + "',XUFFDDBR='"
                        + bean.getXUFFDDBR() + "',XUFWTDLR='" + bean.getXUFWTDLR()
                        + "',XUFDH='" + bean.getXUFDH() + "',XUFDBGH='"
                        + bean.getXUFDBGH() + "',XUFKHYH='" + bean.getXUFKHYH()
                        + "',XUFZH='" + bean.getXUFZH() + "',XUFYZBM='"
                        + bean.getXUFYZBM() + "',XUFSH='" + bean.getXufsh()
                        + "',HETGYSBID=" + getgongfValue().getId()
                        + ",GONGYSB_ID="
                        + getshijgfSelectValue().getId()
                        + ",QISRQ=to_date('"
                        + format.format(bean.getShengxsj())
                        + "','YYYY-MM-DD'),GUOQRQ=to_date('"
                        + format.format(bean.getGuoqsj())
                        + "','YYYY-MM-DD'),JIHKJB_ID="
                        + getJihxzValue().getId()
                        // +",HETJSFSB_ID=" +0// 暂时为空
                        // +",HETJSXSB_ID=" + 0
                        // +",HETYJ=" + 0
                        // +",MINGC='"+getmobmc()
                        + ",diancxxb_id=" + getxufValue().getId() + ",meikmcs='"
                        + getFahr() + "' where id=" + hetb_id;
                if(con.getUpdate(sql)<0){
                    setMsg("保存失败");
                }

                // 保存数量
                List list = geteditValuess();
                long[] Y = new long[12];
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    long hetslb_id = ((Fahxxbean) list.get(i)).getId();
                    String Pinz = ((Fahxxbean) list.get(i)).getPinz();
                    String Yunsfs = ((Fahxxbean) list.get(i)).getYunsfs();
                    String Faz = ((Fahxxbean) list.get(i)).getFaz();
                    String Daoz = ((Fahxxbean) list.get(i)).getDaoz();
                    String Shouhr = ((Fahxxbean) list.get(i)).getShouhr();
                    Shouhr = Shouhr.substring(Shouhr.lastIndexOf(";") + 1, Shouhr
                            .length());
                    ((Fahxxbean) list.get(i)).setShouhr(Shouhr);
                    Y[0] = ((Fahxxbean) list.get(i)).getY1();
                    Y[1] = ((Fahxxbean) list.get(i)).getY2();
                    Y[2] = ((Fahxxbean) list.get(i)).getY3();
                    Y[3] = ((Fahxxbean) list.get(i)).getY4();
                    Y[4] = ((Fahxxbean) list.get(i)).getY5();
                    Y[5] = ((Fahxxbean) list.get(i)).getY6();
                    Y[6] = ((Fahxxbean) list.get(i)).getY7();
                    Y[7] = ((Fahxxbean) list.get(i)).getY8();
                    Y[8] = ((Fahxxbean) list.get(i)).getY9();
                    Y[9] = ((Fahxxbean) list.get(i)).getY10();
                    Y[10] = ((Fahxxbean) list.get(i)).getY11();
                    Y[11] = ((Fahxxbean) list.get(i)).getY12();
                    if (hetslb_id == 0) {
                        hetslb_id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));
                        for (int j = 0; j < 12; j++) {
                            if (Y[j] != 0) {
                                sql = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values("
                                        + +hetslb_id
                                        + ",(select id from pinzb where mingc='"
                                        + Pinz
                                        + "'),(select id from yunsfsb where mingc='"
                                        + Yunsfs
                                        + "'),(select id from chezxxb where mingc='"
                                        + Faz
                                        + "'),(select id from chezxxb where mingc='"
                                        + Daoz
                                        + "'),(select id from diancxxb where mingc='"
                                        + Shouhr
                                        + "'),to_date('"
                                        + String.valueOf(DateUtil.getYear(bean
                                                .getShengxsj()))
                                        + (j + 1)
                                        + "','YYYYMM'),"
                                        + Y[j]
                                        + ","
                                        + hetb_id
                                        + ")";
                                con.getInsert(sql);
                            }
                        }
                        ((Fahxxbean) list.get(i)).setId(hetslb_id);
                    } else {// 对于非新增的记录
                        sql = "delete\n" + "from hetslb\n" + "where id="
                                + hetslb_id;
                        con.getDelete(sql);
                        for (int j = 0; j < 12; j++) {
                            if (Y[j] != 0) {
                                sql = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values("
                                        + +hetslb_id
                                        + ",(select id from pinzb where mingc='"
                                        + Pinz
                                        + "'),(select id from yunsfsb where mingc='"
                                        + Yunsfs
                                        + "'),(select id from chezxxb where mingc='"
                                        + Faz
                                        + "'),(select id from chezxxb where mingc='"
                                        + Daoz
                                        + "'),(select id from diancxxb where mingc='"
                                        + Shouhr
                                        + "'),to_date('"
                                        + String.valueOf(DateUtil.getYear(bean
                                                .getShengxsj()))
                                        + (j + 1)
                                        + "','YYYYMM'),"
                                        + Y[j]
                                        + ","
                                        + hetb_id
                                        + ")";
                                con.getInsert(sql);
                            }
                        }
                    }

                }
                // 保存质量
                list = geteditValuesz();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    long hetzlb_id = ((Zhilyqbean) list.get(i)).getId(); //
                    String zhib = ((Zhilyqbean) list.get(i)).getMingc();
                    String tiaoj = ((Zhilyqbean) list.get(i)).getTiaoj();
                    double shangx = ((Zhilyqbean) list.get(i)).getShangx();
                    double xiax = ((Zhilyqbean) list.get(i)).getXiax();
                    String danw = ((Zhilyqbean) list.get(i)).getDanw();
                    if (hetzlb_id == 0) {
                        hetzlb_id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));
                        sql = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values("
                                + hetzlb_id
                                + ",(select id from zhibb where mingc='"
                                + zhib
                                + "' and leib=1),(select id from tiaojb where mingc='"
                                + tiaoj
                                + "'),"
                                + shangx
                                + ","
                                + xiax
                                + ",(select max(id) from danwb where mingc='"
                                + danw + "')," + hetb_id + ")";
                        con.getInsert(sql);
                        ((Zhilyqbean) list.get(i)).setId(hetzlb_id);
                    } else {
                        sql = "update hetzlb "
                                + "set zhibb_id=(select id from zhibb where mingc='"
                                + zhib
                                + "' and leib=1),tiaojb_id=(select id from tiaojb where mingc='"
                                + tiaoj
                                + "'),shangx="
                                + shangx
                                + ",xiax="
                                + xiax
                                + ",danwb_id=(select max(id) from danwb where mingc='"
                                + danw + "')where id=" + hetzlb_id;
                        con.getUpdate(sql);
                    }

                }

                // 保存杂费
                list = geteditValueszf();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    long hetzfb_id = ((Zafxxbean) list.get(i)).getId();
                    String zafmc = ((Zafxxbean) list.get(i)).getItem_id();
                    String diancjs = ((Zafxxbean) list.get(i)).getDiancjszf();
                    if (hetzfb_id == 0) {
                        hetzfb_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                        sql = "insert into hetzfb(id, hetb_id, item_id, diancjszf) values("
                                + hetzfb_id
                                + ", "
                                + hetb_id
                                + ", (select it.id from item it where it.itemsortid = (select id from itemsort ist where ist.bianm = 'DZZF') and it.mingc = '"
                                + zafmc
                                + "'), (select decode('"
                                + diancjs
                                +"', '是', 1, 0) from dual))";
                        con.getInsert(sql);
                        ((Zafxxbean) list.get(i)).setId(hetzfb_id);
                    } else {
                        sql = "update hetzfb "
                                + "set item_id = (select it.id from item it where it.itemsortid = (select id from itemsort ist where ist.bianm = 'DZZF') and it.mingc = '"
                                + zafmc
                                + "'), diancjszf = (select decode('"
                                + diancjs
                                + "', '是', 1, 0) from dual)"
                                + " where id=" + hetzfb_id;
                        con.getUpdate(sql);
                    }
                }

                // 保存价格
                list = geteditValuesg();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    long hetjgb_id = ((jijbean) list.get(i)).getId();
                    String zhibb_id = ((jijbean) list.get(i)).getZhibb_id();
                    String tiaojb_id = ((jijbean) list.get(i)).getTiaojb_id();
                    double shangx = ((jijbean) list.get(i)).getShangx();
                    double xiax = ((jijbean) list.get(i)).getXiax();
                    String zhibdw = ((jijbean) list.get(i)).getDanwb_id();
                    double jij = ((jijbean) list.get(i)).getJij();
                    String jijdw = ((jijbean) list.get(i)).getJijdwid();
                    String jijgs = ((jijbean) list.get(i)).getJijgs();
                    String hetjsfsb_id = ((jijbean) list.get(i)).getHetjsfsb_id();
                    String Hetjsxsb_id = ((jijbean) list.get(i)).getHetjsxsb_id();
                    double yunj = ((jijbean) list.get(i)).getYunj();
                    String yunjdw = ((jijbean) list.get(i)).getYunjdw_id();
                    String yingdkf = ((jijbean) list.get(i)).getYingdkf();
                    String Yunsfsb_id = ((jijbean) list.get(i)).getYunsfsb_id();
                    double zuigmj = ((jijbean) list.get(i)).getZuigmj();
                    String zuigmjdw = ((jijbean) list.get(i)).getZuigmjdw();
                    String hetjjfsb = ((jijbean) list.get(i)).getHetjjfsb_id();
                    double fengsjj = ((jijbean) list.get(i)).getFengsjj();
                    String fengsjjdw = ((jijbean) list.get(i)).getFengsjjdw();
                    String jijlx = ((jijbean) list.get(i)).getJijlx();
                    String pinz = ((jijbean) list.get(i)).getPinz();// 品种
                    if (hetjgb_id == 0) {
                        hetjgb_id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));
                        sql = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n"
                                + "YINGDKF,YUNSFSB_ID,ZUIGMJ,ZUIGMJDW,HETB_ID,HETJJFSB_ID,FENGSJJ,FENGSJJDW,JIJLX,pinzb_id) values("
                                + hetjgb_id
                                + ",(select id from zhibb where mingc='"
                                + zhibb_id
                                + "' and leib=1),(select id from tiaojb where mingc='"
                                + tiaojb_id
                                + "'),"
                                + shangx
                                + ","
                                + xiax
                                + ",(select max(id) from danwb where mingc='"
                                + zhibdw
                                + "'),"
                                + jij
                                + ",(select max(id) from danwb where mingc='"
                                + jijdw
                                + "'),'"
                                + jijgs
                                + "',(select id from hetjsfsb where mingc='"
                                + hetjsfsb_id
                                + "'),(select id from hetjsxsb where mingc='"
                                + Hetjsxsb_id
                                + "'),"
                                + yunj
                                + ",(select max(id) from danwb where mingc='"
                                + yunjdw
                                + "'),"
                                + getProperId(getyingdkfgSelectModel(), yingdkf)
                                + ",(select id from yunsfsb where mingc='"
                                + Yunsfsb_id
                                + "'),"
                                + zuigmj
                                + ",(select max(id) from danwb where mingc='"
                                + zuigmjdw
                                + "'),"
                                + hetb_id
                                + ",(select id from hetjjfsb where mingc='"
                                + hetjjfsb
                                + "'),"
                                + fengsjj
                                + ",(select max(id) from danwb where mingc='"
                                + fengsjjdw
                                + "'),"
//							+ ","
                                + this.getProperId(this.getIJijlxModel(), jijlx)
                                + ",(select max(id) from pinzb where mingc='"
                                + pinz + "'))";
                        con.getInsert(sql);
                        ((jijbean) list.get(i)).setId(hetjgb_id);
                    } else {
                        sql = "update hetjgb set ZHIBB_ID=(select id from zhibb where mingc='"
                                + zhibb_id
                                + "' and leib=1),TIAOJB_ID=(select id from tiaojb where mingc='"
                                + tiaojb_id
                                + "'),SHANGX="
                                + shangx
                                + ",XIAX="
                                + xiax
                                + ",DANWB_ID=(select max(id) from danwb where mingc='"
                                + zhibdw
                                + "'),JIJ="
                                + jij
                                + ",JIJDWID=(select max(id) from danwb where mingc='"
                                + jijdw
                                + "'),jijgs='"
                                + jijgs
                                + "',HETJSFSB_ID=(select id from hetjsfsb where mingc='"
                                + hetjsfsb_id
                                + "'),HETJSXSB_ID=(select id from hetjsxsb where mingc='"
                                + Hetjsxsb_id
                                + "'),YUNJ="
                                + yunj
                                + ",YUNJDW_ID=(select max(id) from danwb where mingc='"
                                + yunjdw
                                + "'),YINGDKF="
                                + getProperId(getyingdkfgSelectModel(), yingdkf)
                                + ",YUNSFSB_ID=(select id from yunsfsb where mingc='"
                                + Yunsfsb_id
                                + "'),ZUIGMJ="
                                + zuigmj
                                + ", ZUIGMJDW = (select max(id) from danwb where mingc='" + zuigmjdw
                                + "'),HETJJFSB_ID=(select id from hetjjfsb where mingc='"
                                + hetjjfsb
                                + "'),fengsjj="
                                + fengsjj
                                + ", FENGSJJDW = (select max(id) from danwb where mingc='" + fengsjjdw
                                + "'),jijlx="
                                + this.getProperId(this.getIJijlxModel(), jijlx)
                                + ",pinzb_id=(select max(id) from pinzb where mingc='"
                                + pinz + "') where hetjgb.id=" + hetjgb_id;
                        con.getUpdate(sql);
                    }
                }
                // 保存增扣款
                list = geteditValuesj();
                for (int i = 0; i < list.size(); i++) {// 遍历容器
                    long hetzkkb_id = ((Zengkkbean) list.get(i)).getId();
                    String zhibb_id = ((Zengkkbean) list.get(i)).getZHIBB_ID();
                    String tiaojb_id = ((Zengkkbean) list.get(i)).getTIAOJB_ID();
                    double shangx = ((Zengkkbean) list.get(i)).getSHANGX();
                    double xiax = ((Zengkkbean) list.get(i)).getXIAX();
                    String zhibdw = ((Zengkkbean) list.get(i)).getDANWB_ID();
                    double jis = ((Zengkkbean) list.get(i)).getJIS();
                    String jisdwb_id = ((Zengkkbean) list.get(i)).getJISDWID();
                    double kouj = ((Zengkkbean) list.get(i)).getKOUJ();
                    String koujgs = ((Zengkkbean) list.get(i)).getKoujgs();
                    String koujdw = ((Zengkkbean) list.get(i)).getKOUJDW();
                    double zengfj = ((Zengkkbean) list.get(i)).getZENGFJ();
                    String zengfjgs = ((Zengkkbean) list.get(i)).getZengfjgs();
                    String zengfjdw = ((Zengkkbean) list.get(i)).getZENGFJDW();
                    String xiaoscl = ((Zengkkbean) list.get(i)).getXIAOSCL();
                    double jizzb = ((Zengkkbean) list.get(i)).getJIZZB();
                    double jizzkj = ((Zengkkbean) list.get(i)).getJIZZKJ();
                    String canzxm = ((Zengkkbean) list.get(i)).getCANZXM();
                    String canzxmdw = ((Zengkkbean) list.get(i)).getCANZXMDW();
                    double canzsx = ((Zengkkbean) list.get(i)).getCANZSX();
                    double canzxx = ((Zengkkbean) list.get(i)).getCANZXX();
                    String hetjsxsb_id = ((Zengkkbean) list.get(i)).getJIESXXB_ID();
                    String yunsfsb_id = ((Zengkkbean) list.get(i)).getYUNSFSB_ID();
                    String pinzb_id = ((Zengkkbean) list.get(i)).getPinzb_id();
                    String beiz = ((Zengkkbean) list.get(i)).getBEIZ();
                    String SHIYFW = ((Zengkkbean) list.get(i)).getShiyfw();
                    String jijlx = ((Zengkkbean) list.get(i)).getJIJLX();
                    if (hetzkkb_id == 0) {
                        hetzkkb_id = Long.parseLong(MainGlobal
                                .getNewID(((Visit) getPage().getVisit())
                                        .getDiancxxb_id()));
                        sql = "insert into hetzkkb( ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,ZENGFJGS,"
                                + "ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID,SHIYFW,PINZB_ID,JIJLX)"
                                + "values("
                                + hetzkkb_id
                                + ",(select id from zhibb where mingc='"
                                + zhibb_id
                                + "' and leib=1),(select id from tiaojb where mingc='"
                                + tiaojb_id
                                + "'),"
                                + shangx
                                + ","
                                + xiax
                                + ",(select max(id) from danwb where mingc='"
                                + zhibdw
                                + "'),"
                                + jis
                                + ",(select max(id) from danwb where mingc='"
                                + jisdwb_id
                                + "'),"
                                + kouj
                                + ",'"
                                + koujgs
                                + "',(select max(id) from danwb where mingc='"
                                + koujdw
                                + "'),"
                                + zengfj
                                + ",'"
                                + zengfjgs
                                + "',(select max(id) from danwb where mingc='"
                                + zengfjdw
                                + "'),"
                                + getProperId(getxiaoswcljSelectModel(), xiaoscl)
                                + ","
                                + jizzkj
                                + ","
                                + jizzb
                                + ",(select id from zhibb where mingc='"
                                + canzxm
                                + "' and leib=1),(select max(id) from danwb where mingc='"
                                + canzxmdw
                                + "'),"
                                + canzsx
                                + ","
                                + canzxx
                                + ",(select id from hetjsxsb where mingc='"
                                + hetjsxsb_id
                                + "'),(select id from yunsfsb where mingc='"
                                + yunsfsb_id
                                + "'),'"
                                + beiz
                                + "',"
                                + hetb_id
                                + ",decode('" + SHIYFW + "','超出部分',1,0),(select max(id) from pinzb where mingc='"+pinzb_id+"'),"
                                + this.getProperId(this.getIJijlxModel(), jijlx) +")";
                        con.getInsert(sql);
                        ((Zengkkbean) list.get(i)).setId(hetzkkb_id);
                    } else {
                        sql = "update hetzkkb set ZHIBB_ID=(select id from zhibb where mingc='"
                                + zhibb_id
                                + "' and leib=1),TIAOJB_ID=(select id from tiaojb where mingc='"
                                + tiaojb_id
                                + "'),SHANGX="
                                + shangx
                                + ",XIAX="
                                + xiax
                                + ",DANWB_ID=(select max(id) from danwb where mingc='"
                                + zhibdw
                                + "'),JIS="
                                + jis
                                + ",JISDWID=(select max(id) from danwb where mingc='"
                                + jisdwb_id
                                + "'),KOUJ="
                                + kouj
                                + ",KOUJGS='"
                                + koujgs
                                + "',KOUJDW=(select max(id) from danwb where mingc='"
                                + koujdw
                                + "'),ZENGFJ="
                                + zengfj
                                + ",ZENGFJGS='"
                                + zengfjgs
                                + "',ZENGFJDW=(select max(id) from danwb where mingc='"
                                + zengfjdw
                                + "'),XIAOSCL="
                                + getProperId(getxiaoswcljSelectModel(), xiaoscl)
                                + ",JIZZKJ="
                                + jizzkj
                                + ",JIZZB="
                                + jizzb
                                + ",SHIYFW=decode('"
                                + SHIYFW
                                + "','超出部分',1,0)"
                                + ",CANZXM=(select id from zhibb where mingc='"
                                + canzxm
                                + "' and leib=1),CANZXMDW=(select max(id) from danwb where mingc='"
                                + canzxmdw
                                + "'),CANZSX="
                                + canzsx
                                + ",CANZXX="
                                + canzxx
                                + ",HETJSXSB_ID=(select id from hetjsxsb where mingc='"
                                + hetjsxsb_id
                                + "'),YUNSFSB_ID=(select id from yunsfsb where mingc='"
                                + yunsfsb_id
                                + "'),BEIZ='"
                                + beiz
                                + "',pinzb_id=(select max(id) from pinzb where mingc='"+pinzb_id+"'), jijlx="+ this.getProperId(this.getIJijlxModel(), jijlx) +" where id="
                                + hetzkkb_id;
                        con.getUpdate(sql);
                    }
                }
                // 保存文字
                sql = "update hetwzb\n" + "set wenznr='" + getWenz()
                        + "'where hetb_id=" + hetb_id;
                con.getUpdate(sql);

                gethetSelectModels();
                sethetSelectValue(getIDropDownBean(gethetSelectModel(), hetb_id));
                bean.setId(hetb_id);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            con.Close();
        }

	}

	private void Inserts() {
		List _value = geteditValuess();
		_value.add(new Fahxxbean("", ""));// 缺省到站,收货人
	}

	private void Deletes() {
		List _value = geteditValuess();
		if (_editTableRows != -1) {
			long id = ((Fahxxbean) _value.get(_editTableRows)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetslb where id="
						+ id
						+ " and not exists(select * from hetslb,hetb_mb where hetslb.hetb_id=hetb_mb.id and hetslb.id="
						+ id + ")";
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRows);
		}
	}

	private void Insertz() {
		List _value = geteditValuesz();
		_value.add(new Zhilyqbean(_value.size() + 1));
	}

	private void Deletez() {
		List _value = geteditValuesz();
		if (_editTableRowz != -1) {
			long id = ((Zhilyqbean) _value.get(_editTableRowz)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetzlb where id="
						+ id
						+ " and not exists(select * from hetzlb,hetb_mb where hetzlb.hetb_id=hetb_mb.id and hetzlb.id="
						+ id + ")";
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowz);
			int c = _value.size();
			for (int a = _editTableRowz; a < c; a++) {
				((Zhilyqbean) _value.get(a))
						.setXuh(((Zhilyqbean) _value.get(a)).getXuh() - 1);
			}
		}
	}
	
	private void Insertzf() {
		List _value = geteditValueszf();
		_value.add(new Zafxxbean(_value.size() + 1));
	}

	private void Deletezf() {
		List _value = geteditValueszf();
		if (_editTableRowzf != -1) {
			long id = ((Zafxxbean) _value.get(_editTableRowzf)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetzfb where id="
						+ id
						+ " and not exists(select * from hetzfb,hetb_mb where hetzfb.hetb_id=hetb_mb.id and hetzfb.id="
						+ id + ")";
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowzf);
			int c = _value.size();
			for (int a = _editTableRowzf; a < c; a++) {
				((Zafxxbean) _value.get(a))
						.setXuh(((Zafxxbean) _value.get(a)).getXuh() - 1);
			}
		}
	}

	private void Insertj() {
		List _value = geteditValuesj();
		_value.add(new Zengkkbean(_value.size() + 1));
	}

	private void Deletej() {
		List _value = geteditValuesj();
		if (_editTableRowj != -1) {
			long id = ((Zengkkbean) _value.get(_editTableRowj)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetzkkb where id="
						+ id
						+ " and not exists(select * from hetzkkb,hetb_mb where hetzkkb.hetb_id=hetb_mb.id and hetzkkb.id="
						+ id + ")";
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowj);
			int c = _value.size();
			for (int a = _editTableRowj; a < c; a++) {
				((Zengkkbean) _value.get(a))
						.setXuh(((Zengkkbean) _value.get(a)).getXuh() - 1);
			}
		}
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}

	private void Insertg() {
		List _value = geteditValuesg();
		_value.add(new jijbean(_value.size() + 1));
	}

	private void Deleteg() {
		List _value = geteditValuesg();
		if (_editTableRowg != -1) {
			long id = ((jijbean) _value.get(_editTableRowg)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetjgb where id="
						+ id
						+ " and not exists(select * from hetjgb,hetb_mb where hetjgb.hetb_id=hetb_mb.id and hetjgb.id="
						+ id + ")";
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowg);
			int c = _value.size();
			for (int a = _editTableRowg; a < c; a++) {
				((jijbean) _value.get(a)).setXuh(((jijbean) _value.get(a))
						.getXuh() - 1);
			}
		}
	}

	// private String getProperValue(IPropertySelectionModel _selectModel,
	// int value) {
	// int OprionCount;
	// OprionCount = _selectModel.getOptionCount();
	// for (int i = 0; i < OprionCount; i++) {
	// if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
	// return ((IDropDownBean) _selectModel.getOption(i)).getValue();
	// }
	// }
	// return null;
	// }
	private IDropDownBean getIDropDownBean(IPropertySelectionModel model,
			long id) {
		int OprionCount;
		OprionCount = model.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) model.getOption(i)).getId() == id) {
				return (IDropDownBean) model.getOption(i);
			}
		}
		return null;
	}

	private void gethetmb(long hetmb_id) {
		String sql = "";
		JDBCcon con = new JDBCcon();
		
		try {
			// 合同信息
			sql = "select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,"
					+ "GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,"
					+ "XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ,"
					+ "JIHKJB_ID,LIUCB_ID,MINGC,diancxxb_id "
					+ "from hetb_mb"
					+ " where ID=" + hetmb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				Hetxxbean bean = gethetxxbean();
				// sethetSelectValue(getIDropDownBean(gethetSelectModel(),rs.getLong("LIUCB_ID")));
				setJihxzValue(getIDropDownBean(getIJihxzModel(), rs
						.getLong("JIHKJB_ID")));
				// setmobmc(rs.getString("MINGC"));
				// setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),rs.getLong("GONGYSB_ID")));
				// setxufValue(getIDropDownBean(getxufModel(),rs.getLong("diancxxb_id")));
				// setgongfValue(getIDropDownBean(getgongfModel(),rs.getLong("HETGYSBID")));
				bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
				bean.setGONGFDH(rs.getString("GONGFDH"));
				bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
				bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
				bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
				bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
				bean.setGongfsh(rs.getString("Gongfsh"));
				bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
				bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
				bean.setGONGFZH(rs.getString("GONGFZH"));
				// bean.setGuoqsj(rs.getDate("GUOQRQ"));
				bean.setHetbh(rs.getString("Hetbh"));
				// bean.setHetyj(rs.getString("Hetyj"));
				bean.setQianddd(rs.getString("Qianddd"));
				// bean.setQiandsj(rs.getDate("QIANDRQ"));
				// bean.setShengxsj(rs.getDate("QISRQ"));
				bean.setXUFDBGH(rs.getString("XUFDBGH"));
				bean.setXUFDH(rs.getString("XUFDH"));
				bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
				bean.setXUFDWMC(rs.getString("XUFDWMC"));
				bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
				bean.setXUFKHYH(rs.getString("XUFKHYH"));
				bean.setXufsh(rs.getString("Xufsh"));
				bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
				bean.setXUFYZBM(rs.getString("XUFYZBM"));
				bean.setXUFZH(rs.getString("XUFZH"));

				if (this.isHetxxbjFlag()) {
					bean.setGuoqsj(rs.getDate("GUOQRQ"));
					bean.setQiandsj(rs.getDate("QIANDRQ"));
					bean.setShengxsj(rs.getDate("QISRQ"));
				}
			}
			// 数量信息
			List list = geteditValuess();
			list.clear();
			sql = "select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n"
					+ " y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n"
					+ ",diancxxb.mingc shouhr\n"
					+ "from(\n"
					+ "\n"
					+ "select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n"
					+ "y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n"
					+ "from\n"
					+ "    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n"
					+ "    from hetslb\n"
					+ "    where hetb_id="
					+ hetmb_id
					+ "\n"
					+ "    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n"
					+ "    )a,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,1 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='01'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y1,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,2 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='02'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y2,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,3 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='03'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y3,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,4 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='04'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y4,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,5 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='05'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y5,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,6as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='06'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y6,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,7 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='07'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y7,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,8 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='08'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y8,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,9 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='09'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y9,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,10 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='10'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y10,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,11 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='11'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y11,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,12 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='12'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y12\n"
					+ "\n"
					+ "------------------------------------\n"
					+ "where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n"
					+ "and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n"
					+ "and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n"
					+ "and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n"
					+ "and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n"
					+ "and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n"
					+ "and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n"
					+ "and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n"
					+ "and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n"
					+ "and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n"
					+ "and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n"
					+ "and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n"
					+ ")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n"
					+ "where  aa.pinzb_id=pinzb.id and aa.yunsfsb_id=yunsfsb.id\n"
					+ "and faz.id=aa.faz_id and aa.daoz_id=daoz.id and diancxxb.id=aa.diancxxb_id";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				String pinz = rs.getString("pinz");
				String yunsfs = rs.getString("yunsfs");
				String faz = rs.getString("faz");
				String daoz = rs.getString("daoz");
				String shouhr = rs.getString("shouhr");
				long id = rs.getLong("id");
				long Y1 = rs.getLong("Y1");
				long Y2 = rs.getLong("Y2");
				long Y3 = rs.getLong("Y3");
				long Y4 = rs.getLong("Y4");
				long Y5 = rs.getLong("Y5");
				long Y6 = rs.getLong("Y6");
				long Y7 = rs.getLong("Y7");
				long Y8 = rs.getLong("Y8");
				long Y9 = rs.getLong("Y9");
				long Y10 = rs.getLong("Y10");
				long Y11 = rs.getLong("Y11");
				long Y12 = rs.getLong("Y12");
				long hej = Y1 + Y2 + Y3 + Y4 + Y5 + Y6 + Y7 + Y8 + Y9 + Y10
						+ Y11 + Y12;
				list
						.add(new Fahxxbean(id, pinz, yunsfs, faz, daoz, shouhr,
								hej, Y1, Y2, Y3, Y4, Y5, Y6, Y7, Y8, Y9, Y10,
								Y11, Y12));
			}
			// 质量信息
			list = geteditValuesz();
			list.clear();
			sql = "select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n"
					+ "from hetzlb,zhibb,tiaojb,danwb\n"
					+ "where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="
					+ hetmb_id;
			rs = con.getResultSet(sql);
			int i = 0;
			while (rs.next()) {
				String zhib = rs.getString("zhib");
				String tiaoj = rs.getString("tiaoj");
				double shangx = rs.getDouble("shangx");
				double xiax = rs.getDouble("xiax");
				String danw = rs.getString("danw");
				long id = rs.getLong("id");
				list.add(new Zhilyqbean(++i, id, zhib, tiaoj, shangx, xiax,
						danw));
			}
			// 价格信息
			list = geteditValuesg();
			list.clear();
			sql = "select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,jijgs,\n"
					+ "hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'是',2,'否','')YINGDKF,\n"
					+ "yunsfsb.mingc yunsfsmc,ZUIGMJ,zuigmjdw.mingc zuigmjdw,fengsjj,fengsjjdw.mingc fengsjjdw,decode(jijlx,0,'含税单价',1,'不含税单价') as jijlx, pinzb.mingc pinz\n"
					+ "from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,danwb zuigmjdw,danwb fengsjjdw,yunsfsb,pinzb\n"
					+ "where hetjgb.pinzb_id=pinzb.id(+) and hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n"
					+ "and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n"
					+ "and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.zuigmjdw = zuigmjdw.id(+) "
					+ "and hetjgb.fengsjjdw = fengsjjdw.id(+) and hetjgb.hetb_id="+ hetmb_id + "order by xiax, hetjgb.id ";
			rs = con.getResultSet(sql);
			i = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				String zhibmc = rs.getString("zhibmc");
				String tiaojmc = rs.getString("tiaojmc");
				double XIAX = rs.getDouble("XIAX");
				double SHANGX = rs.getDouble("SHANGX");
				String zhibdwmc = rs.getString("zhibdwmc");
				double JIJ = rs.getDouble("JIJ");
				String jijdwmc = rs.getString("jijdwmc");
				String jijgs = rs.getString("jijgs");
				String hetjjfsmc = rs.getString("hetjjfsmc");
				String hetjsfsmc = rs.getString("hetjsfsmc");
				String hetjsxsmc = rs.getString("hetjsxsmc");
				double YUNJ = rs.getDouble("YUNJ");
				String yunjdwmc = rs.getString("yunjdwmc");
				String YINGDKF = rs.getString("YINGDKF");
				String yunsfsmc = rs.getString("yunsfsmc");
				double ZUIGMJ = rs.getDouble("ZUIGMJ");
				String zuigmjdw = rs.getString("zuigmjdw");
				double fengsjj = rs.getDouble("fengsjj");
				String fengsjjdw = rs.getString("fengsjjdw");
				String jijlx = rs.getString("jijlx");
				String pinz = rs.getString("pinz");
				list.add(new jijbean(++i, id, zhibmc, tiaojmc, SHANGX, XIAX,
						zhibdwmc, JIJ, jijdwmc, jijgs, hetjsfsmc, hetjjfsmc,
						hetjsxsmc, YUNJ, yunjdwmc, YINGDKF, yunsfsmc, ZUIGMJ,
						zuigmjdw, fengsjj, fengsjjdw, jijlx, pinz));
			}
			// 增扣款信息
			list = geteditValuesj();
			list.clear();
			sql = "select z.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n"
					+ "KOUJ,koujgs,koujdw.mingc koujdwmc,ZENGFJ,zengfjgs,zengfjdw.mingc zengfjdwmc,\n"
					+ "decode(XIAOSCL,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)','')XIAOSCL,JIZZKJ,JIZZB\n"
					+ ",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,z.BEIZ,\n"
					+ "pinzb.mingc as pinz,decode(z.SHIYFW,0,'全部','超出部分')SHIYFW, decode(z.jijlx, 0, '含税单价', '不含税单价') jijlx\n"
					+ "from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb,pinzb\n"
					+ "where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n"
					+ "and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n"
					+ "and z.yunsfsb_id=yunsfsb.id(+) and z.pinzb_id=pinzb.id(+) and z.hetb_id="
					+ hetmb_id;
			rs = con.getResultSet(sql);
			i = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				String zhibmc = rs.getString("zhibmc");
				String tiaojmc = rs.getString("tiaojmc");
				double XIAX = rs.getDouble("XIAX");
				double SHANGX = rs.getDouble("SHANGX");
				String zhibdwmc = rs.getString("zhibdwmc");
				double JIS = rs.getDouble("JIS");
				String jisdwmc = rs.getString("jisdwmc");
				double KOUJ = rs.getDouble("KOUJ");
				String koujgs = rs.getString("koujgs");
				String koujdwmc = rs.getString("koujdwmc");
				double ZENGFJ = rs.getDouble("ZENGFJ");
				String zengfjgs = rs.getString("zengfjgs");
				String zengfjdwmc = rs.getString("zengfjdwmc");
				String XIAOSCL = rs.getString("XIAOSCL");
				double JIZZKJ = rs.getDouble("JIZZKJ");
				double JIZZB = rs.getDouble("JIZZB");
				String canzxmmc = rs.getString("canzxmmc");
				String canzxmdwmc = rs.getString("canzxmdwmc");
				double CANZSX = rs.getDouble("CANZSX");
				double CANZXX = rs.getDouble("CANZXX");
				String hetjsxsmc = rs.getString("hetjsxsmc");
				String yunsfsmc = rs.getString("yunsfsmc");
				String BEIZ = rs.getString("BEIZ");
				String PINZB_ID	= rs.getString("pinz");
				String SHIYFW = rs.getString("SHIYFW");
				String jijlx = rs.getString("jijlx");

				list.add(new Zengkkbean(id, ++i, zhibmc, tiaojmc, SHANGX, XIAX,
						zhibdwmc, JIS, jisdwmc, KOUJ, koujgs, koujdwmc, ZENGFJ,
						zengfjgs, zengfjdwmc, XIAOSCL, JIZZKJ, JIZZB, canzxmmc,
						canzxmdwmc, CANZSX, CANZXX, hetjsxsmc, yunsfsmc,PINZB_ID,
						SHIYFW, BEIZ, jijlx));
			}

			// 文字信息
			sql = "select id,wenznr\n" + "from hetwzb\n" + "where hetb_id="
					+ hetmb_id;
			rs = con.getResultSet(sql);
			if (rs.next()) {
				setWenz(rs.getString("wenznr"));
			}
			rs.close();
			
			Dak_Hetbl(hetmb_id);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	public String getFahr() {
		if (((Visit) getPage().getVisit()).getString2() == null) {
			((Visit) getPage().getVisit()).setString2("");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setFahr(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
}