package com.zhiren.dc.huaygl.disfhyjg;

import org.apache.tapestry.html.BasePage;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;


import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Disfhyjg extends BasePage implements PageValidateListener {

	public void ExtGridUtil() {
	}

	public String OratypeOfExt(String oratype) {
		if ("NUMBER".equals(oratype)) {
			return "float";
		}
		if ("DATE".equals(oratype)) {
			return "date";
		}

		return "string";
	}

//	日期
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {

		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq)) {
			
			((Visit) this.getPage().getVisit()).setString1(riq);
		}
	}

	//结束
	protected void initialize() {
		msg = "";
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
		Save1(getChange(), visit);
	}
	
	public void Save1(String strchange, Visit visit) {

		JDBCcon con = new JDBCcon();
		
		//获得删除的数据
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		while (delrsl.next()) {
			sbsql.append("delete from ").append("kuangfzlb").append(" where id ='").append(delrsl.getString("id")).append("';\n");
			sbsql.append("delete from kuangfzlzb zb where zb.bianm = '").append(delrsl.getString("HUAYBM")).append("';\n");
		}
		delrsl.close();
		
		//获得所有修改的数据
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		ResultSetList rsl_in = null;
		//以下是添加操作

		while(mdrsl.next()){
			//随机获取id
			String id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			if ("0".equals(mdrsl.getString("ID"))) {
				String sql_in = "select * from kuangfzlb where huaybm='" + mdrsl.getString("HUAYBM")+"'";
				rsl_in = con.getResultSetList(sql_in);
				
				//判断录入的值不能为空
				if(mdrsl.getString("HUAYBM").equals("")) {
					setMsg("您录入的化验编码存在空值，请您重新录入");
				}else if(rsl_in.next()){
					setMsg("编码重复");
				}else{
		
					sbsql.append("insert into ").append("kuangfzlb").append("(id")
					.append(",QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD," +
							"STD,QGRAD,HDAF,QGRAD_DAF,SDAF,VAR,T1,T2,T3,T4,LURY,STAR,HUAYY,HUAYBM,HUAYRQ,jiesszbmzb_id,LURRQ)")
					.append("values").append("('").append(id).append("'").append(",'")
					.append(mdrsl.getString("QNET_AR")).append("'").append(",'")
					.append(mdrsl.getString("AAR")).append("'").append(",'")
					.append(mdrsl.getString("AD")).append("'").append(",'")
					.append(mdrsl.getString("VDAF")).append("'").append(",'")
					.append(mdrsl.getString("MT")).append("'").append(",'")
					.append(mdrsl.getString("STAD")).append("'").append(",'")
					.append(mdrsl.getString("AAD")).append("'").append(",'")
					.append(mdrsl.getString("MAD")).append("'").append(",'")
					.append(mdrsl.getString("QBAD")).append("'").append(",'")
					.append(mdrsl.getString("HAD")).append("'").append(",'")
					.append(mdrsl.getString("VAD")).append("'").append(",'")
					.append(mdrsl.getString("FCAD")).append("'").append(",'")
					.append(mdrsl.getString("STD")).append("'").append(",'")
					.append(mdrsl.getString("QGRAD")).append("'").append(",'")
					
					.append(mdrsl.getString("HDAF")).append("'").append(",'")
					.append(mdrsl.getString("QGRAD_DAF")).append("'").append(",'")
					.append(mdrsl.getString("SDAF")).append("'").append(",'")
					.append(mdrsl.getString("VAR")).append("'").append(",'")
					.append(mdrsl.getString("T1")).append("'").append(",'")
					.append(mdrsl.getString("T2")).append("'").append(",'")
					.append(mdrsl.getString("T3")).append("'").append(",'")
					.append(mdrsl.getString("T4")).append("'").append(",'")
					.append(mdrsl.getString("LURY")).append("'").append(",'")
					.append(mdrsl.getString("STAR")).append("'").append(",'")
					.append(mdrsl.getString("HUAYY")).append("'").append(",'")
					.append(mdrsl.getString("HUAYBM")).append("'").append(",").append(" to_date('")
					.append(mdrsl.getString("HUAYRQ")).append("','yyyy-mm-dd')").append(",'")
					.append(this.getDisfhyjgValue().getId()).append("',to_date('")
					.append(mdrsl.getString("LURRQ")).append("','yyyy-mm-dd'));\n");
					
					sbsql.append("insert into kuangfzlzb(id, bianm, qnet_ar, aar, ad, vdaf, mt, stad, aad, mad, qbad, had, vad, " +
						"fcad, std, qgrad, hdaf, qgrad_daf, sdaf, var, t1, t2, t3, t4, lury, star) values(getnewid(")
						.append(visit.getDiancxxb_id()).append("), '")
						.append(mdrsl.getString("HUAYBM")).append("', ").append(mdrsl.getString("QNET_AR")).append(", ").append(mdrsl.getString("AAR")).append(", ")
						.append(mdrsl.getString("AD")).append(", ").append(mdrsl.getString("VDAF")).append(", ").append(mdrsl.getString("MT")).append(", ")
						.append(mdrsl.getString("STAD")).append(", ").append(mdrsl.getString("AAD")).append(", ").append(mdrsl.getString("MAD")).append(", ")
						.append(mdrsl.getString("QBAD")).append(", ").append(mdrsl.getString("HAD")).append(", ").append(mdrsl.getString("VAD")).append(", ")
						.append(mdrsl.getString("FCAD")).append(", ").append(mdrsl.getString("STD")).append(", ").append(mdrsl.getString("QGRAD")).append(", ")
						.append(mdrsl.getDouble("HDAF")).append(", ").append(mdrsl.getString("QGRAD_DAF")).append(", ").append(mdrsl.getString("SDAF")).append(", ")
						.append(mdrsl.getString("VAR")).append(", ").append(mdrsl.getString("T1")).append(", ").append(mdrsl.getString("T2")).append(", ")
						.append(mdrsl.getString("T3")).append(", ").append(mdrsl.getString("T4")).append(", '").append(mdrsl.getString("LURY")).append("', ")
						.append(mdrsl.getString("STAR")).append(");\n");
					
				}
				
			}
		}
		sbsql.append("end;");
		
		if (!sbsql.toString().equals("begin\nend;")) {
			con.getUpdate(sbsql.toString());
		}
		
		mdrsl.close();
		con.Close();
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
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
		} else if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		}
	}
	// 第三方化验机构
	public IDropDownBean getDisfhyjgValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getDisfhyjgModel().getOptionCount()>0) {
				setDisfhyjgValue((IDropDownBean)getDisfhyjgModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setDisfhyjgValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getDisfhyjgModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setDisfhyjgModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setDisfhyjgModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setDisfhyjgModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql="";
//		if(HAIY_SH.equals(getLeix())){
			
			sql="select jz.id,jz.zhi from jiesszbmzb jz ,jiesszbmb j,jiesszbmglb jg where jz.id = jg.jiesszbmzb_id and j.id = jg.jiesszbmb_id and j.bianm = '"+Locale.addhyjg_jies+"'";
 
		setDisfhyjgModel(new IDropDownModel(sql,"全部"));
	}
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		String huayjg = "";
		if(!getDisfhyjgValue().getValue().equals("全部")){
			huayjg = "and jz.zhi='"+getDisfhyjgValue().getValue()+"'";
		}
		String sql=
			"select distinct nvl(k.id,0) as id,\n" +
			"jz.zhi,k.huaybm,k.huayrq,k.lurrq\n" + 
			",nvl(k.qnet_ar,0) as qnet_ar,nvl(k.aar,0) as aar,nvl(k.ad,0) as ad,nvl(k.vdaf,0) as vdaf,\n" + 
			"nvl(k.mt,0) as mt,nvl(k.stad,0) as stad,nvl(k.aad,0) as aad,nvl(k.mad,0) as mad,nvl(k.qbad,0) as qbad,\n" + 
			"nvl(k.had,0) as had,nvl(k.vad,0) as vad,nvl(k.fcad,0) as fcad,nvl(k.std,0) as std,nvl(k.qgrad,0) as qgrad,\n" + 
			"nvl(k.hdaf,0) as hdaf,nvl(k.qgrad_daf,0) as qgrad_daf,nvl(k.star,0) as star,nvl(k.sdaf,0) as sdaf,\n" + 
			"nvl(k.var,0) as var,nvl(k.t1,0) as t1,nvl(k.t2,0) as t2,nvl(k.t3,0) as t3,nvl(k.t4,0) as t4,\n" + 
			"nvl('"+((Visit) getPage().getVisit()).getRenymc()+"','') as lury,k.huayy\n" + 
			"from kuangfzlb k,jiesszbmzb jz ,jiesszbmb j,jiesszbmglb jg\n" + 
			"where k.jiesszbmzb_id = jz.id(+) and jz.id = jg.jiesszbmzb_id(+) and jg.jiesszbmb_id =  j.id(+) "+huayjg+" and j.bianm = '"+Locale.addhyjg_jies+"' and k.lurrq = to_date('"+getRiq()+"','yyyy-mm-dd')";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		//设置多选框
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// /设置显示列名称

		egu.getColumn("zhi").setHeader("化验机构");
		egu.getColumn("zhi").setDefaultValue(getDisfhyjgValue().getValue());
		egu.getColumn("zhi").setEditor(null);
		egu.getColumn("HUAYBM").setHeader("化验编码");
		egu.getColumn("HUAYRQ").setHeader("化验日期");
		egu.getColumn("LURRQ").setHeader("录入日期");
		egu.getColumn("LURRQ").setDefaultValue(getRiq());
		egu.getColumn("LURRQ").setEditor(null);

		egu.getColumn("qnet_ar").setHeader("收到基低位热值<p>Qnet_ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setWidth(100);
		((NumberField)egu.getColumn("qnet_ar").editor).setDecimalPrecision(2);
		egu.getColumn("qnet_ar").editor.minValue="0";
		egu.getColumn("qnet_ar").editor.maxValue="100";
		egu.getColumn("qnet_ar").setDefaultValue("0");
//		egu.getColumn("qnet_ar").setWidth(180);

		
		egu.getColumn("aar").setHeader("收到基灰分<p>Aar(%)</p>");
		egu.getColumn("aar").setWidth(60);
		((NumberField)egu.getColumn("aar").editor).setDecimalPrecision(2);
		egu.getColumn("aar").editor.minValue="0";
		egu.getColumn("aar").editor.maxValue="100";
		egu.getColumn("aar").setDefaultValue("0");
		egu.getColumn("aar").setWidth(100);
		

		egu.getColumn("ad").setHeader("干燥基<p>Ad(%)</p>");
		egu.getColumn("ad").setWidth(60);
		((NumberField)egu.getColumn("ad").editor).setDecimalPrecision(2);
		egu.getColumn("ad").editor.minValue="0";
		egu.getColumn("ad").editor.maxValue="100";
		egu.getColumn("ad").setDefaultValue("0");
//		egu.getColumn("ad").setWidth(150);
		
		egu.getColumn("vdaf").setHeader("挥发分<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setWidth(60);
		((NumberField)egu.getColumn("vdaf").editor).setDecimalPrecision(2);
		egu.getColumn("vdaf").editor.minValue="0";
		egu.getColumn("vdaf").editor.maxValue="100";
		egu.getColumn("vdaf").setDefaultValue("0");
//		egu.getColumn("vdaf").setWidth(150);
		
		egu.getColumn("mt").setHeader("全水分<p>Mt(%)</p>");
		egu.getColumn("mt").setWidth(60);
		((NumberField)egu.getColumn("mt").editor).setDecimalPrecision(1);
		egu.getColumn("mt").editor.minValue="0";
		egu.getColumn("mt").editor.maxValue="100";
		egu.getColumn("mt").setDefaultValue("0");
//		egu.getColumn("mt").setWidth(150);
		
		egu.getColumn("stad").setHeader("硫分<p>Stad(%)</p>");
		egu.getColumn("stad").setWidth(60);
		((NumberField)egu.getColumn("stad").editor).setDecimalPrecision(2);
		egu.getColumn("stad").editor.minValue="0";
		egu.getColumn("stad").editor.maxValue="100";
		egu.getColumn("stad").setDefaultValue("0");
//		egu.getColumn("stad").setWidth(150);
		
		egu.getColumn("star").setHeader("收到基硫<p>Star(%)</p>");
		egu.getColumn("star").setWidth(60);
		((NumberField)egu.getColumn("star").editor).setDecimalPrecision(2);
		egu.getColumn("star").editor.minValue="0";
		egu.getColumn("star").editor.maxValue="100";
		egu.getColumn("star").setDefaultValue("0");
//		egu.getColumn("star").setWidth(180);
		
		egu.getColumn("aad").setHeader("空气干燥基灰分<p>Aad(%)</p>");
		egu.getColumn("aad").setWidth(60);
		((NumberField)egu.getColumn("aad").editor).setDecimalPrecision(2);
		egu.getColumn("aad").editor.minValue="0";
		egu.getColumn("aad").editor.maxValue="100";
		egu.getColumn("aad").setDefaultValue("0");
		egu.getColumn("aad").setWidth(100);
		
		egu.getColumn("mad").setHeader("空气干燥基水分<p>Mad(%)</p>");
		egu.getColumn("mad").setWidth(60);
		((NumberField)egu.getColumn("mad").editor).setDecimalPrecision(2);
		egu.getColumn("mad").editor.minValue="0";
		egu.getColumn("mad").editor.maxValue="100";
		egu.getColumn("mad").setDefaultValue("0");
		egu.getColumn("mad").setWidth(100);
		
		egu.getColumn("qbad").setHeader("弹筒热值<p>Qbad(Mj/kg)</p>");
		egu.getColumn("qbad").setWidth(80);
		((NumberField)egu.getColumn("qbad").editor).setDecimalPrecision(2);
		egu.getColumn("qbad").editor.minValue="0";
		egu.getColumn("qbad").editor.maxValue="100";
		egu.getColumn("qbad").setDefaultValue("0");
//		egu.getColumn("qbad").setWidth(180);
		
		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
		egu.getColumn("had").setWidth(60);
		((NumberField)egu.getColumn("had").editor).setDecimalPrecision(2);
		egu.getColumn("had").editor.minValue="0";
		egu.getColumn("had").editor.maxValue="100";
		egu.getColumn("had").setDefaultValue("0");
		egu.getColumn("had").setWidth(100);
		
		egu.getColumn("vad").setHeader("空气干燥基挥发分<p>Vad(%)</p>");
		egu.getColumn("vad").setWidth(60);
		((NumberField)egu.getColumn("vad").editor).setDecimalPrecision(2);
		egu.getColumn("vad").editor.minValue="0";
		egu.getColumn("vad").editor.maxValue="100";
		egu.getColumn("vad").setDefaultValue("0");
		egu.getColumn("vad").setWidth(100);
		
		egu.getColumn("fcad").setHeader("固定碳<p>Fcad(%)</p>");
		egu.getColumn("fcad").setWidth(60);
		((NumberField)egu.getColumn("fcad").editor).setDecimalPrecision(2);
		egu.getColumn("fcad").editor.minValue="0";
		egu.getColumn("fcad").editor.maxValue="100";
		egu.getColumn("fcad").setDefaultValue("0");
//		egu.getColumn("fcad").setWidth(150);
		
		egu.getColumn("std").setHeader("干燥基硫<p>Std(%)</p>");
		egu.getColumn("std").setWidth(60);
		((NumberField)egu.getColumn("std").editor).setDecimalPrecision(2);
		egu.getColumn("std").setDefaultValue("0");
//		egu.getColumn("std").setWidth(150);
		
		egu.getColumn("qgrad").setHeader("干燥基高位热值<p>Qgrad(Mj/kg)</p>");
		egu.getColumn("qgrad").setWidth(80);
		((NumberField)egu.getColumn("qgrad").editor).setDecimalPrecision(2);
		egu.getColumn("qgrad").setDefaultValue("0");
		egu.getColumn("qgrad").setWidth(100);
		
		egu.getColumn("hdaf").setHeader("干燥无灰基氢<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setWidth(60);
		((NumberField)egu.getColumn("hdaf").editor).setDecimalPrecision(2);
		egu.getColumn("hdaf").setDefaultValue("0");
		egu.getColumn("hdaf").setWidth(100);
		
		egu.getColumn("qgrad_daf").setHeader("Qgrad_daf(Mj/kg)");
		egu.getColumn("qgrad_daf").setWidth(110);
		((NumberField)egu.getColumn("qgrad_daf").editor).setDecimalPrecision(2);
		egu.getColumn("qgrad_daf").setDefaultValue("0");
//		egu.getColumn("qgrad_daf").setWidth(150);
		
		egu.getColumn("sdaf").setHeader("Sdaf(%)");
		egu.getColumn("sdaf").setWidth(60);
		((NumberField)egu.getColumn("sdaf").editor).setDecimalPrecision(2);
		egu.getColumn("sdaf").setDefaultValue("0");
//		egu.getColumn("sdaf").setWidth(150);
		
		egu.getColumn("var").setHeader("收到基挥发分<p>Var(%)</p>");
		egu.getColumn("var").setWidth(60);
		((NumberField)egu.getColumn("var").editor).setDecimalPrecision(2);
		egu.getColumn("var").setDefaultValue("0");
		egu.getColumn("var").setWidth(100);
		
		egu.getColumn("t1").setHeader("T1(℃)");
		egu.getColumn("t1").setWidth(60);
		((NumberField)egu.getColumn("t1").editor).setDecimalPrecision(2);
		egu.getColumn("t1").setDefaultValue("0");
		
		egu.getColumn("t2").setHeader("T2(℃)");
		egu.getColumn("t2").setWidth(60);
		((NumberField)egu.getColumn("t2").editor).setDecimalPrecision(2);
		egu.getColumn("t2").setDefaultValue("0");
		
		egu.getColumn("t3").setHeader("T3(℃)");
		egu.getColumn("t3").setWidth(60);
		((NumberField)egu.getColumn("t3").editor).setDecimalPrecision(2);
		egu.getColumn("t3").setDefaultValue("0");
		
		egu.getColumn("t4").setHeader("T4(℃)");
		egu.getColumn("t4").setWidth(60);
		((NumberField)egu.getColumn("t4").editor).setDecimalPrecision(2);
		egu.getColumn("t4").setDefaultValue("0");
		
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setWidth(60);
		egu.getColumn("lury").setDefaultValue(visit.getDiancmc());
		egu.getColumn("huayy").setHeader("化验员");

//		设置ExtGridUtil可被编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.addPaging(25);
		
		egu.getColumn("id").setEditor(null);

		
	
		egu.addTbarText("化验机构");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("DisfhyjgSelect");
		comb1.setId("DisfhyjgSelect");
		comb1.setWidth(100);
		comb1.setListeners("select:function(own,rec,index){Ext.getDom('DisfhyjgSelect').selectedIndex=index}");
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("DisfhyjgSelect.on('select',function(){document.forms[0].submit();});");
		
		
		
		
		egu.addTbarText("录入日期");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "forms[0]");// 与html页中的id绑定,并自动刷新
//		df.setListeners("select:function(own,rec,index){Ext.getDom('RIQ').selectedIndex=index}");
		egu.addToolbarItem(df.getScript());


//		设置Toolbar按钮	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		StringBuffer insert_Condition=new StringBuffer("");
		if(this.getDisfhyjgValue().getId()==-1){
			insert_Condition.append("alert('请选择化验机构');return ;");
		}
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,null,insert_Condition.toString());
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "");
		
		egu.addTbarText("-");
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
			visit.getDropDownBean1();
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			init();
		}
		getSelectData();
	}
	
	private void init() {
		
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setboolean1(false);		//厂别
	}
}


