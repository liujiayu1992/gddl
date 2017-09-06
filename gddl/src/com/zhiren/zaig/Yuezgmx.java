package com.zhiren.zaig;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
public class Yuezgmx extends BasePage implements PageValidateListener {
/*
 * 2013-07-04
 * 李凯洋
 * 调整sql
 */
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
		Visit visit=(Visit) getPage().getVisit();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());//变更数据
		StringBuffer sql=new StringBuffer();
		sql.append("begin \n");
		while(rsl.next()){
			long meikxxb_id= (getExtGrid().getColumn("meikxxb_id").combo).getBeanId(rsl.getString("meikxxb_id"));
			String id = rsl.getString("id");
			String riq = getYearValue().getValue()+"-"+getMonthValue().getValue()+"-01";
			String meil_sy = rsl.getString("meil_sy");
			String meil_rc = rsl.getString("meil_rc");
			String meil_js = rsl.getString("meil_js");
			String meil_zg = rsl.getString("meil_zg");
			String jiesdj_hs = rsl.getString("jiesdj_hs");
			String jiesdj_bhs = rsl.getString("jiesdj_bhs");
			String hej_hs = rsl.getString("hej_hs");
			String hej_bhs = rsl.getString("hej_bhs");
		
			if(!"0".equals(rsl.getString("id"))){
				//修改数据
				sql.append("update yuezgmxb set meikxxb_id = "+meikxxb_id+",meil_sy = "+meil_sy+",");
				sql.append(" meil_rc = "+meil_rc+",meil_js = "+meil_js+",");
				sql.append(" meil_zg = "+meil_zg+",jiesdj_hs = "+jiesdj_hs+",");
				sql.append(" jiesdj_bhs = "+jiesdj_bhs+",hej_hs = "+hej_hs+",hej_bhs = "+hej_bhs+" ");
				sql.append(" where id = "+id+";\n");
				//修改
				
			}else {
				//新增数据。
				long zgid = 500;
				String szgid = MainGlobal.getNewID(zgid);
				sql.append("insert into yuezgmxb ");
				sql.append("values("+szgid+",to_date('"+riq+"','yyyy-mm-dd'),"+meikxxb_id+","+meil_sy+","+meil_rc+","+meil_js+","+meil_zg+","+jiesdj_hs+",");
				sql.append(""+jiesdj_bhs+","+hej_hs+","+hej_bhs+");\n");
			}
		}
		ResultSetList delRs=getExtGrid().getDeleteResultSet(getChange());//删除的数据
		while(delRs.next()){
			//删除数据
			String szgid=delRs.getString("id");
			sql.append("delete from yuezgmxb where id="+szgid+";\n");
		}
		sql.append("end ;\n");
		
			JDBCcon con = new JDBCcon();
			con.getUpdate(sql.toString());
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefrushChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefrushChick = true;
	}
	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}

	public void submit(IRequestCycle cycle) {
		
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefrushChick) {
			_RefrushChick = false;
			//Create();
			getSelectData();
		}
		if (_CreateChick) {
			_CreateChick = false;
			Create();
			getSelectData();
		}
	
	}
	
	
	
	public void Create(){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String del = 
			"delete from yuezgmxb where riq = to_date('"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"','yyyy-mm')";

		con.getDelete(del);
		

		String sql = 
			"insert into yuezgmxb\n" +
			"  select getnewid(500),\n" + 
			"         to_date('"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"','yyyy-mm') riq,\n" + 
			"         zhu.meikxxb_id meikxxb_id,\n" + 
			"         nvl(zg.meil_zg,0) meil_sy,\n" + 
			"         nvl(fh.jingz,0) meil_rc,\n" + 
			"         0 meil_js,\n"+
			"        nvl(zg.meil_zg + fh.jingz,0) meil_zg,\n" + 
			"         0 jiesdj_hs,\n" + 
			"         0 jiesdj_bhs,\n" + 
			"         0 hej_hs,\n" + 
			"         0 hej_bhs\n" + 
			"    from (select distinct meikxxb_id\n" + 
			"            from (select meikxxb_id\n" + 
			"                    from fahb\n" + 
			"                   where to_char(daohrq, 'yyyy-mm') = '"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"'\n" + 
			"                  union\n" + 
			"                  select meikxxb_id\n" + 
			"                    from yuezgmxb zg\n" + 
			"                   where zg.riq = date '"+getYearValue().getValue()+"-"+(Integer.parseInt(getMonthValue().getValue())-1)+"-01')) zhu,\n" + 
			"         meikxxb mk,\n" + 
			"         (select meikxxb_id, sum(jingz) jingz\n" + 
			"            from fahb\n" + 
			"           where to_char(daohrq, 'yyyy-mm') = '"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"'\n" + 
			"           group by meikxxb_id) fh,\n" + 
			"         (select meikxxb_id, meil_zg\n" + 
			"            from yuezgmxb\n" + 
			"           where riq = date '"+getYearValue().getValue()+"-"+(Integer.parseInt(getMonthValue().getValue())-1)+"-01') zg\n" + 
			"   where zhu.meikxxb_id = fh.meikxxb_id(+)\n" + 
			"     and zhu.meikxxb_id = zg.meikxxb_id(+)\n" + 
			"     and zhu.meikxxb_id = mk.id";

		int flag = con.getInsert(sql);
		if (flag == -1) {
			setMsg("生成失败！");
			con.rollBack();
			con.Close();
		}
	
		con.Close();
		setMsg("生成成功！");
	}
	


	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		//查询数据
		String sql =
			"select zg.id id,mk.mingc meikxxb_id ,round_new(decode(zg.meil_sy,null,0.00,zg.meil_sy),2) meil_sy,\n" +
			"       decode(zg.meil_rc,null,0,zg.meil_rc) meil_rc,zg.meil_js," +
			"       decode(zg.meil_sy,null,0,zg.meil_sy) + decode(zg.meil_rc,null,0,zg.meil_rc) - zg.meil_js  meil_zg,\n" + 
			"       zg.jiesdj_hs,zg.jiesdj_bhs,zg.hej_hs,\n" + 
			"       zg.hej_bhs\n" + 
			" from yuezgmxb zg,meikxxb mk\n" + 
			"  where zg.meikxxb_id = mk.id\n" + 
			"  and zg.riq = to_date('"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"','yyyy-mm')";


		ResultSetList rsl = con.getResultSetList(sql);
		
		
		String sqlcx=	"select zg.id id,mk.mingc meikxxb_id ,zg.meil_sy,\n" +
		"       zg.meil_rc,zg.meil_js,zg.meil_zg,\n" + 
		"       zg.jiesdj_hs,zg.jiesdj_bhs,zg.hej_hs,\n" + 
		"       zg.hej_bhs\n" + 
		" from yuezgmxb zg,meikxxb mk\n" + 
		"  where zg.meikxxb_id = mk.id\n" + 
		"  and zg.riq = to_date('"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"','yyyy-mm')";

		ResultSetList rslcs = con.getResultSetList(sqlcx);
		//是否有数据
		boolean shifcz=false;
		if(rslcs.next()){
				shifcz=true;
			
		}
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
//		公共设置
		egu.pagsize=1000;
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row);
		egu.setWidth("bodyWidth");
		egu.setHeight("bodyHeight");
		egu.setTableName("yuezgmxb");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.getColumn("id").setCenterHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikxxb_id").setCenterHeader("矿点");
		//egu.getColumn("meikxxb_id").setEditor(null);	

		egu.getColumn("meil_sy").setCenterHeader("上月暂估煤量");
//		egu.getColumn("meil_sy")
		((NumberField)egu.getColumn("meil_sy").editor).setDecimalPrecision(2);
		
		egu.getColumn("meil_rc").setCenterHeader("本月进厂煤量");
		((NumberField)egu.getColumn("meil_rc").editor).setDecimalPrecision(2);
		
		egu.getColumn("meil_js").setCenterHeader("本月结算煤量");
		egu.getColumn("meil_zg").setHeader("本月暂估煤量");
		egu.getColumn("meil_zg").setEditor(null);
		
		egu.getColumn("jiesdj_hs").setCenterHeader("拟结算单价(含税)");
		egu.getColumn("jiesdj_bhs").setCenterHeader("拟结算单价(不含税)");
		egu.getColumn("jiesdj_bhs").setEditor(null);
		
		egu.getColumn("hej_hs").setCenterHeader("金额合计(含税)");
		egu.getColumn("hej_hs").setEditor(null);
		egu.getColumn("hej_bhs").setCenterHeader("金额合计(不含税)");
		egu.getColumn("hej_bhs").setEditor(null);
		
		//设置宽度
		egu.getColumn("meikxxb_id").setWidth(110);
		egu.getColumn("meil_sy").setWidth(95);
		egu.getColumn("meil_rc").setWidth(95);
		egu.getColumn("meil_js").setWidth(95);
		egu.getColumn("meil_zg").setWidth(95);
		
		egu.getColumn("jiesdj_hs").setWidth(110);
		egu.getColumn("jiesdj_bhs").setWidth(115);
		egu.getColumn("hej_hs").setWidth(100);
		egu.getColumn("hej_bhs").setWidth(105);

		//设置对齐
		egu.getColumn("meikxxb_id").setAlign("left");
		egu.getColumn("meil_sy").setAlign("right");
		egu.getColumn("meil_rc").setAlign("right");
		egu.getColumn("meil_js").setAlign("right");
		egu.getColumn("meil_zg").setAlign("right");
		
		egu.getColumn("jiesdj_hs").setAlign("right");
		egu.getColumn("jiesdj_bhs").setAlign("right");
		egu.getColumn("hej_hs").setAlign("right");
		egu.getColumn("hej_bhs").setAlign("right");
		
		
		//默认值
		egu.getColumn("meil_sy").setDefaultValue("0");
		egu.getColumn("meil_rc").setDefaultValue("0");
		egu.getColumn("meil_js").setDefaultValue("0");
		egu.getColumn("meil_zg").setDefaultValue("0");
		
		egu.getColumn("jiesdj_hs").setDefaultValue("0");
		egu.getColumn("jiesdj_bhs").setDefaultValue("0");
		egu.getColumn("hej_hs").setDefaultValue("0");
		egu.getColumn("hej_bhs").setDefaultValue("0");
		
		
		//矿点下拉框
		ComboBox combKuangd = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(combKuangd);
		combKuangd.setEditable(true);
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from meikxxb order by mingc"));

		egu.addTbarText("年份:");
		ComboBox Year = new ComboBox();
		Year.setTransform("YearDropDown");
		Year.setId("Year");
		Year.setLazyRender(true);// 动态绑定
		Year.setWidth(60);
		egu.addToolbarItem(Year.getScript());
		//egu.addOtherScript("Year.on('select',function(){document.forms[0].submit();});");
		// 月
		egu.addTbarText("月份:");
		ComboBox Month = new ComboBox();
		Month.setTransform("MonthDropDown");
		Month.setId("Month");
		Month.setLazyRender(true);// 动态绑定
		Month.setWidth(50);
		egu.addToolbarItem(Month.getScript());
		//egu.addOtherScript("Month.on('select',function(){document.forms[0].submit();});");
	
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
	
		if(shifcz){
			GridButton shengc= new GridButton("生成", "function (){ " +
					"	Ext.MessageBox.confirm('提示信息','此操作会删除之前数据，是否继续？" +
					"',function(btn){if(btn=='yes'){" +
			" document.getElementById('CreateButton').click();}else{return;}})}");
			shengc.setId("CreateButton");			
			egu.addTbarBtn(shengc);
			
		}else{
			GridButton shengc= new GridButton("生成", "function (){ document.getElementById('CreateButton').click();}");
			shengc.setId("CreateButton");			
			egu.addTbarBtn(shengc);
			
		}
		
			
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		/*GridButton del= new GridButton("删除", "function (){ " +
				"	Ext.MessageBox.confirm('提示信息','删除当前页面所有数据,是否继续?',function(btn){if(btn=='yes'){" +
				" document.getElementById('DeleteButton').click();}else{return;}})}");
		del.setId("DeleteButton");	
		egu.addTbarBtn(del);*/
		
//		添加保存按钮
		/*String save_script ="var grid_rcd = gridDiv_ds.getModifiedRecords();" +
				"for(i=0;i<grid_rcd.length;i++){" +
				"if(grid_rcd[i].get('KGWEIF')<0){" +
				"Ext.MessageBox.alert('提示信息','自开工累计已付大于应付');" +
				"return;}" +
				"}" ;
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",save_script);*/	

		/*GridButton shenh=new GridButton("提交","function(){if(gridDiv_ds.getModifiedRecords().length>0){Ext.MessageBox.alert('信息提示','请先保存记录!');}else{ document.getElementById('SubmitButton').click();}}");	
		shenh.setId("SubmitButton");			
		egu.addTbarBtn(shenh);*/
		
		
		String script ="gridDiv_grid.on('afteredit',function(e){\n" +
			"  if(e.field=='MEIL_SY' || e.field=='MEIL_RC' || e.field=='MEIL_JS'){\n" +
			"  e.record.set('MEIL_ZG',(parseFloat(e.record.get('MEIL_SY')) + parseFloat(e.record.get('MEIL_RC')) - parseFloat(e.record.get('MEIL_JS'))).toFixed(2));"+
			"  }\n" + 
			//年累计页面计算
			"  if(e.field=='JIESDJ_HS'){\n " + 
			"  e.record.set('JIESDJ_BHS',(parseFloat(e.record.get('JIESDJ_HS')) /1.17).toFixed(2) ) ;"+
			"  }\n"+
			
			"  if(e.field=='MEIL_SY' || e.field=='MEIL_RC' || e.field=='MEIL_JS' || e.field=='JIESDJ_HS'){\n " + 
			"  e.record.set('HEJ_HS',(parseFloat(e.record.get('MEIL_ZG'))*parseFloat(e.record.get('JIESDJ_HS'))).toFixed(2));"+
			"  }\n"+
			
			"  if(e.field=='MEIL_SY' || e.field=='MEIL_RC' || e.field=='MEIL_JS'  || e.field=='JIESDJ_HS'){\n " + 
			"  e.record.set('HEJ_BHS',(parseFloat(e.record.get('MEIL_ZG'))*parseFloat(e.record.get('JIESDJ_HS')) / 1.17).toFixed(2));"+
			"  }\n"+
			
			"});";
		egu.addOtherScript(script);
		
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

	

//	 年
	public IDropDownBean getYearValue() {
		int _nianf = DateUtil.getYear(new Date());
		int _yuef = DateUtil.getMonth(new Date());
		if (_yuef == 1) {
			_nianf = _nianf - 1;
		}
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getYearModel().getOptionCount(); i++) {
				Object obj = getYearModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean4((IDropDownBean) getYearModel()
									.getOption(i));
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYearValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public void setYearModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYearModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYearModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getYearModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select yvalue id,ylabel name from nianfb");
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql.toString()));
	}

	// 月
	public IDropDownBean getMonthValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			int _yuef = DateUtil.getMonth(new Date());

			if (1 == _yuef) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getMonthModel()
								.getOption(11));
			} else {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getMonthModel()
								.getOption(_yuef - 2));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setMonthValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}
	}

	public void setMonthModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getMonthModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getMonthModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getMonthModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select mvalue id,decode(length(mlabel),1,'0'||mlabel,mlabel) name from yuefb");
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql.toString()));
	}
	
	public Toolbar getToolbar() {      //工具条
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setYearValue(null);
			setYearModel(null);
			getYearModels();
			setMonthValue(null);
			setMonthModel(null);
			getMonthModels();
			getChange();
			getSelectData();
		}
	}
}
