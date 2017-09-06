package com.zhiren.dc.jilgl.shujdr;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author rock
 * @since 2009-12-02
 */
public class DataImportDetails extends BasePage implements PageValidateListener {

	private static final String customKey = "DataImportDetails"; 

	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
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
	
	private String getFahtmpId(){
		return ((Visit) this.getPage().getVisit()).getString1();
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private void Return(IRequestCycle cycle) {
		cycle.activate("DataImport");
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		String mokmc="";
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			
			if(!(mokmc==null)&&!mokmc.equals("")){
				String id = delrsl.getString("id");
				//删除时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,mokmc,
						"chepbtmp",id);
			}
			sql.append("delete from ").append(" chepbtmp ").append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl =  getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(" chepbtmp ").append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(
							getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i)));
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				if(!(mokmc==null)&&!mokmc.equals("")){
					String id = mdrsl.getString("id");
					//更改时增加日志
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,mokmc,
							"chepbtmp",id);
				}
				sql.append("update ").append("chepbtmp").append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(mdrsl.getColumnNames()[i].equals("JINGZ")){
						//因为数据库中无净重字段,所以不更新
					}else{
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
					
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		con.Close();
		
	
	
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
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
			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql;
		
		boolean isEditable_Fhrq = false;	//发货日期是否可编辑
		/*  设置发货日期是否可编辑的参数  */
		sql = "select zhi from xitxxb where mingc = '数据导入可否编辑发货日期' and zhi='是' and leib='数量' and zhuangt = 1 and diancxxb_id = " 
			+ visit.getDiancxxb_id();
		isEditable_Fhrq = con.getHasIt(sql);
		
		boolean isEditable_Dhrq = false;	//到货日期是否可编辑
		/*  设置到货日期是否可编辑的参数  */
		sql = "select zhi from xitxxb where mingc = '数据导入可否编辑到货日期' and zhi='是' and leib='数量' and zhuangt = 1 and diancxxb_id = " 
		+ visit.getDiancxxb_id();
		isEditable_Dhrq = con.getHasIt(sql);
		
		boolean isShowSaveButton = true;
		sql = "select zhi from xitxxb where mingc = '数据导入明细是否显示保存按钮' and zhi='否' and leib='数量' and zhuangt = 1 and diancxxb_id =" +
		visit.getDiancxxb_id();
		isShowSaveButton = !con.getHasIt(sql);
		
		sql = "select c.id, d.mingc diancxxb_id, c.gongysmc, c.meikdwmc, c.faz,\n" + 
		"c.pinz, c.jihkj, c.fahrq, c.daohrq, c.jianjfs, c.chebb_id,\n" +
		"c.chec, caiybh, to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') zhongcsj,\n" +
		"c.zhongcjjy, to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') qingcsj,\n" +
		"c.qingcjjy, c.cheph, c.maoz, c.piz, round_new(c.maoz-c.piz-c.koud-c.kous-c.kouz ,3)as jingz ,\n" +
		"c.biaoz,c.koud, c.kous, c.kouz, c.sanfsl, c.daoz, yuandz, c.yuanshdw,\n" +
		"c.meikdwmc yuanmkdw, c.yunsdw yunsdw, c.daozch, c.beiz,c.fahbtmp_id\n" +
		"from chepbtmp c, diancxxb d where c.diancxxb_id = d.id and fahbtmp_id in (" + 
		getFahtmpId() + ") and fahb_id = 0";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, customKey);
//		设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(0);
		egu.setTableName("chepbtmp");
		ComboBox dc= new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		dc.setEditable(true);
		String dcsb="select id,mingc from diancxxb where id = "+visit.getDiancxxb_id()+" or fuid="+visit.getDiancxxb_id();
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcsb));
		egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("diancxxb_id").returnId = true;
		egu.getColumn("gongysmc").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysmc").setEditor(null);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikdwmc").setEditor(null);
		egu.getColumn("faz").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz").setWidth(65);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("pinz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinz").setWidth(50);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("jihkj").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkj").setWidth(65);
		egu.getColumn("jihkj").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		if(!isEditable_Fhrq){
			egu.getColumn("fahrq").setEditor(null);
		}
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		if(!isEditable_Dhrq){
			egu.getColumn("daohrq").setEditor(null);
		}		
		egu.getColumn("jianjfs").setHeader(Locale.jianjfs_chepb);
		egu.getColumn("jianjfs").setWidth(60);
		egu.getColumn("jianjfs").setEditor(null);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chebb_id").setEditor(null);
		egu.getColumn("chebb_id").returnId=false;
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("caiybh").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("zhongcsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("zhongcsj").setWidth(80);
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").update = false;
		egu.getColumn("zhongcsj").setHidden(true);
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("qingcsj").setHeader(Locale.qingcsj_chepb);
		egu.getColumn("qingcsj").setWidth(80);
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("qingcsj").update = false;
		egu.getColumn("qingcsj").setHidden(true);
		egu.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu.getColumn("qingcjjy").setWidth(80);
		egu.getColumn("qingcjjy").setEditor(null);
		egu.getColumn("qingcjjy").setHidden(true);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("jingz").setHeader(Locale.jingz_chepb);
		egu.getColumn("jingz").setWidth(65);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setDefaultValue("0");//设置默认值
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("kous").setHeader(Locale.kous_chepb);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("sanfsl").setWidth(60);
		egu.getColumn("daoz").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz").setWidth(65);
		egu.getColumn("yuandz").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz").setWidth(65);
		egu.getColumn("yuanshdw").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("fahbtmp_id").setHidden(true);
		egu.getColumn("fahbtmp_id").setEditor(null);
		
		//设置供应商下拉框
		ComboBox c8 = new ComboBox();
		egu.getColumn("gongysmc").setEditor(c8);
		c8.setEditable(true);
		String gyssb = "select id,mingc from gongysb where leix=1 order by mingc";
		egu.getColumn("gongysmc").setComboEditor(egu.gridId,
				new IDropDownModel(gyssb));
		egu.getColumn("gongysmc").returnId=false;
		//设置煤矿单位下拉框
		ComboBox c9 = new ComboBox();
		egu.getColumn("meikdwmc").setEditor(c9);
		c9.setEditable(true);
		c9.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		egu.getColumn("meikdwmc").returnId=false;
		//设置发站下拉框
		ComboBox c0 = new ComboBox();
		egu.getColumn("faz").setEditor(c0);
		c0.setEditable(true);
		String Fazsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("faz").setComboEditor(egu.gridId,
				new IDropDownModel(Fazsb));
		egu.getColumn("faz").returnId=false;

		//		设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("daoz").setEditor(c1);
		c1.setEditable(true);
		String daozsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz").setComboEditor(egu.gridId,
				new IDropDownModel(daozsb));
		egu.getColumn("daoz").returnId=false;
		//设置品种下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinz").setEditor(c2);
		c2.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		egu.getColumn("pinz").returnId=false;
		//设置口径下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkj").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jihkj").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjsb));
		egu.getColumn("jihkj").returnId=false;
		//设置检斤方式下拉框
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "过衡"));
		l.add(new IDropDownBean(1, "检尺"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("jianjfs").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("jianjfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("jianjfs").returnId=false;

		//设置车别下拉框
		ComboBox c5 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_Cheb));

		//设置原到站下拉框
		ComboBox c6 = new ComboBox();
		egu.getColumn("yuandz").setEditor(c6);
		c6.setEditable(true);
		String Yuandzsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz").setComboEditor(egu.gridId,
				new IDropDownModel(Yuandzsb));
		egu.getColumn("yuandz").setDefaultValue(visit.getDaoz());
		egu.getColumn("yuandz").returnId=false;
		
		//设置原收货单位下拉框
		ComboBox c7 = new ComboBox();
		egu.getColumn("yuanshdw").setEditor(c7);
		c7.setEditable(true);//设置可输入
		sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdw").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("yuanshdw").returnId=false;

		//设置运输单位下拉框
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdw").setEditor(comb);
		comb.setEditable(true);
		String yunsdwsb = "select id,mingc from yunsdwb where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		egu.getColumn("yunsdw").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwsb));
		egu.getColumn("yunsdw").returnId=false;
		
		if(isShowSaveButton){
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		GridButton bc = new GridButton("返回","function(){ " +
				" document.getElementById('ReturnButton').click();}",
				SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(bc);

		
		if(this.getLeix()!=null && this.getLeix().equals("HY")){
			String dhsql=" select *   from  xitxxb where mingc='火车数据导入查看多行替换' and zhi='是' and leib='数量' and zhuangt=1 ";
			
			ResultSetList rt=con.getResultSetList(dhsql);
			
			boolean isDefaultChecked=false;
			if(rt.next()){
				isDefaultChecked=true;
			}
			Checkbox cbselectlike=new Checkbox();
			cbselectlike.setChecked(isDefaultChecked);
			cbselectlike.setId("SelectLike");
			egu.addToolbarItem(cbselectlike.getScript());
			egu.addTbarText("多行替换");
			egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set(e.field,e.value);}});\n");
		}else if(this.getLeix()!=null && this.getLeix().equals("QY")){
			String dhsql=" select *   from  xitxxb where mingc='汽车数据导入查看多行替换' and zhi='是' and leib='数量' and zhuangt=1";
			ResultSetList rt=con.getResultSetList(dhsql);
			if(rt.next()){
				Checkbox cbselectlike=new Checkbox();
				cbselectlike.setId("SelectLike");
				egu.addToolbarItem(cbselectlike.getScript());
				egu.addTbarText("多行替换");
				egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set(e.field,e.value);}});\n");
			}
		}
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
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
	
	public String getLeix() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setLeix(String leix) {
		((Visit) this.getPage().getVisit()).setString4(leix);
	}

}
