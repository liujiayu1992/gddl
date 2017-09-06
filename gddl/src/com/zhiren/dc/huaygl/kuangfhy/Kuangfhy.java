package com.zhiren.dc.huaygl.kuangfhy;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kuangfhy extends BasePage implements PageValidateListener {

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
	//新添加的日期字段
	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString3() == null || ((Visit) this.getPage().getVisit()).getString3().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString3() != null && !((Visit) this.getPage().getVisit()).getString3().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString3(riq2);
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
		String strDiancxxb_id="";
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
			
			if(getChangbValue().getId()==-1){
				
				this.setMsg("请选择一个电厂");
				return;
			}else{
				
				Diancxxb_id=this.getChangbValue().getId();
			}
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(getExtGrid().tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
			
			sql.append("update fahb set kuangfzlb_id=0").append(" where id=")
					.append(delrsl.getString("FAHB_ID")).append(";\n");
			
			sql.append("update chepb set kuangfzlzb_id=0 where fahb_id=").append(delrsl.getString("FAHB_ID")).append("; \n");
			
			sql.append("delete from kuangfzlzb where id in (select distinct kuangfzlzb_id from chepb where fahb_id="+delrsl.getString("FAHB_ID")+");		\n");
		}
		delrsl.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			
			strDiancxxb_id=MainGlobal.getNewID(Diancxxb_id);
				
			sql2.append(strDiancxxb_id);
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(getExtGrid().tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")
							||mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")){
						
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
							
							sql2.append(",").append(MainGlobal.getProperId(this.getGongysModel(), mdrsl.getString(i)));
						}else if(mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")){
							
							sql2.append(",").append(MainGlobal.getProperId(this.getMeikdwModel(), mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("FAHB_ID")
							||mdrsl.getColumnNames()[i].equals("BIAOZ")
							||mdrsl.getColumnNames()[i].equals("FAHRQ")){
						
					}else{
						
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(
								getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
				}
				sql.append(") values(").append(sql2).append(");\n");
				
				sql.append("update fahb set kuangfzlb_id=").append(strDiancxxb_id)
					.append(" 	where id=").append(mdrsl.getString("FAHB_ID")).append(";\n");
				
				strDiancxxb_id=MainGlobal.getNewID(Diancxxb_id);
				sql.append(" insert into kuangfzlzb(id, bianm, qnet_ar, aar, ad, vdaf, mt, stad,star, aad, mad, qbad, had, vad, fcad, std, qgrad, hdaf, qgrad_daf, sdaf, var, t1, t2, t3, t4, lury)	\n")
					.append(" 	values	\n")
					.append("("+strDiancxxb_id+", ' ', "+mdrsl.getString("QNET_AR")+", "+mdrsl.getString("AAR")+", "+mdrsl.getString("AD")+", "+mdrsl.getString("VDAF")+", "+mdrsl.getString("MT")+", ")
					.append(mdrsl.getString("STAD")+", "+mdrsl.getString("STAR")+", "+mdrsl.getString("AAD")+", "+mdrsl.getString("MAD")+", "+mdrsl.getString("QBAD")+", "+mdrsl.getString("HAD")+", "+mdrsl.getString("VAD")+", ")
					.append(mdrsl.getString("FCAD")+", "+mdrsl.getString("STD")+", "+mdrsl.getString("QGRAD")+", "+mdrsl.getString("HDAF")+", "+mdrsl.getString("QGRAD_DAF")+", "+mdrsl.getString("SDAF")+", ")
					.append(mdrsl.getString("VAR")+", "+mdrsl.getString("T1")+", "+mdrsl.getString("T2")+", "+mdrsl.getString("T3")+", "+mdrsl.getString("T4")+",'"+mdrsl.getString("LURY")+"');	\n");
				
				sql.append(" update chepb set kuangfzlzb_id=").append(strDiancxxb_id).append(" where fahb_id=").append(mdrsl.getString("FAHB_ID")).append(";\n");
				
			} else {
				sql.append("update ").append(getExtGrid().tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					
					if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")
							||mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")){
						
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						if(mdrsl.getColumnNames()[i].equals("GONGYSB_ID")){
							
							sql.append(MainGlobal.getProperId(this.getGongysModel(), mdrsl.getString(i)))
								.append(",");
						}else if(mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")){
							
							sql.append(MainGlobal.getProperId(this.getMeikdwModel(), mdrsl.getString(i)))
								.append(",");
						}
						
					}else if(mdrsl.getColumnNames()[i].equals("FAHB_ID")
							||mdrsl.getColumnNames()[i].equals("BIAOZ")
							||mdrsl.getColumnNames()[i].equals("FAHRQ")){
						
						
					}else{
						
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
				
				sql.append("update chepb set kuangfzlzb_id=0 where fahb_id=").append(mdrsl.getString("FAHB_ID")).append("; \n");
				
				sql.append("delete from kuangfzlzb where id in (select distinct kuangfzlzb_id from chepb where fahb_id="+mdrsl.getString("FAHB_ID")+");		\n");
				
				strDiancxxb_id=MainGlobal.getNewID(Diancxxb_id);
				sql.append(" insert into kuangfzlzb(id, bianm, qnet_ar, aar, ad, vdaf, mt, stad,star, aad, mad, qbad, had, vad, fcad, std, qgrad, hdaf, qgrad_daf, sdaf, var, t1, t2, t3, t4, lury)	\n")
					.append(" 	values	\n")
					.append("("+strDiancxxb_id+", ' ', "+mdrsl.getString("QNET_AR")+", "+mdrsl.getString("AAR")+", "+mdrsl.getString("AD")+", "+mdrsl.getString("VDAF")+", "+mdrsl.getString("MT")+", ")
					.append(mdrsl.getString("STAD")+", "+mdrsl.getString("STAR")+", "+mdrsl.getString("AAD")+", "+mdrsl.getString("MAD")+", "+mdrsl.getString("QBAD")+", "+mdrsl.getString("HAD")+", "+mdrsl.getString("VAD")+", ")
					.append(mdrsl.getString("FCAD")+", "+mdrsl.getString("STD")+", "+mdrsl.getString("QGRAD")+", "+mdrsl.getString("HDAF")+", "+mdrsl.getString("QGRAD_DAF")+", "+mdrsl.getString("SDAF")+", ")
					.append(mdrsl.getString("VAR")+", "+mdrsl.getString("T1")+", "+mdrsl.getString("T2")+", "+mdrsl.getString("T3")+", "+mdrsl.getString("T4")+",'"+mdrsl.getString("LURY")+"');	\n");
				
				sql.append(" update chepb set kuangfzlzb_id=").append(strDiancxxb_id).append(" where fahb_id=").append(mdrsl.getString("FAHB_ID")).append(";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			setMsg("保存成功！");
		}else {
			
			setMsg("保存失败！");
		}
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

	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 日期条件
		String Riq = this.getRiq();
		String Riq2 = this.getRiq2();
		
		long Diancxxb_id=0;
		String where="";
		
		if(!getTreeid().equals("0")){
			
			where=" and (m.id = "+ getTreeid()+" or g.id = "+getTreeid()+")";
		}
		
		if(((Visit) getPage().getVisit()).isFencb()){
			
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		String sql="select distinct nvl(k.id,0) as id,f.id as fahb_id,g.mingc as gongysb_id,m.mingc as meikxxb_id, 					\n"
				+ "  f.fahrq as fahrq, f.biaoz,nvl(k.qnet_ar,0) as qnet_ar,nvl(k.aar,0) as aar,nvl(k.ad,0) as ad,nvl(k.vdaf,0) as vdaf,  			\n"
				+ " nvl(k.mt,0) as mt,nvl(k.stad,0) as stad,nvl(k.star,0) as star,nvl(k.aad,0) as aad,nvl(k.mad,0) as mad,nvl(k.qbad,0) as qbad,  	\n"
				+ " nvl(k.had,0) as had,nvl(k.vad,0) as vad,nvl(k.fcad,0) as fcad,nvl(k.std,0) as std,nvl(k.qgrad,0) as qgrad,	\n"
				+ " nvl(k.hdaf,0) as hdaf,nvl(k.qgrad_daf,0) as qgrad_daf,nvl(k.sdaf,0) as sdaf,  								\n"
				+ " nvl(k.var,0) as var,nvl(k.t1,0) as t1,nvl(k.t2,0) as t2,nvl(k.t3,0) as t3,nvl(k.t4,0) as t4,  				\n"
				+ " nvl('"+((Visit) getPage().getVisit()).getRenymc()+"','') as lury										\n"
				+ " from kuangfzlb k,gongysb g,meikxxb m,fahb f																	\n"
				+ " where f.kuangfzlb_id=k.id(+) and g.id = f.gongysb_id  and  f.meikxxb_id=m.id 								\n"
				+ where +" and f.diancxxb_id="+Diancxxb_id+"					\n"
				+ " and f.jiesb_id=0 and f.fahrq >= to_date('"+ Riq + "','yyyy-mm-dd')										\n"
				+" and f.fahrq <= to_date('"+ Riq2 + "','yyyy-mm-dd') \n" //添加时间段sql语句
				+" order by g.mingc,m.mingc,f.fahrq";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("kuangfzlb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		//设置多选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// /设置显示列名称
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("fahrq").setHeader("发货日期");//新添加的发货日期
		egu.getColumn("biaoz").setHeader("矿发量");
		egu.getColumn("qnet_ar").setHeader("QNET_AR(Kcal/kg)");
		egu.getColumn("qnet_ar").setWidth(100);
		((NumberField)egu.getColumn("qnet_ar").editor).setDecimalPrecision(2);
		egu.getColumn("aar").setHeader("AAR(%)");
		egu.getColumn("aar").setWidth(60);
		((NumberField)egu.getColumn("aar").editor).setDecimalPrecision(2);
		egu.getColumn("ad").setHeader("AD(%)");
		egu.getColumn("ad").setWidth(60);
		((NumberField)egu.getColumn("ad").editor).setDecimalPrecision(2);
		egu.getColumn("vdaf").setHeader("VDAF(%)");
		egu.getColumn("vdaf").setWidth(60);
		((NumberField)egu.getColumn("vdaf").editor).setDecimalPrecision(2);
		egu.getColumn("mt").setHeader("MT(%)");
		egu.getColumn("mt").setWidth(60);
		((NumberField)egu.getColumn("mt").editor).setDecimalPrecision(1);
		egu.getColumn("stad").setHeader("STAD(%)");
		egu.getColumn("stad").setWidth(60);
		((NumberField)egu.getColumn("stad").editor).setDecimalPrecision(2);
		egu.getColumn("star").setHeader("STAR(%)");
		egu.getColumn("star").setWidth(60);
		((NumberField)egu.getColumn("star").editor).setDecimalPrecision(2);
		egu.getColumn("aad").setHeader("AAD(%)");
		egu.getColumn("aad").setWidth(60);
		((NumberField)egu.getColumn("aad").editor).setDecimalPrecision(2);
		egu.getColumn("mad").setHeader("MAD(%)");
		egu.getColumn("mad").setWidth(60);
		((NumberField)egu.getColumn("mad").editor).setDecimalPrecision(2);
		egu.getColumn("qbad").setHeader("QBAD(Mj/kg)");
		egu.getColumn("qbad").setWidth(80);
		((NumberField)egu.getColumn("qbad").editor).setDecimalPrecision(2);
		egu.getColumn("had").setHeader("HAD(%)");
		egu.getColumn("had").setWidth(60);
		((NumberField)egu.getColumn("had").editor).setDecimalPrecision(2);
		egu.getColumn("vad").setHeader("VAD(%)");
		egu.getColumn("vad").setWidth(60);
		((NumberField)egu.getColumn("vad").editor).setDecimalPrecision(2);
		egu.getColumn("fcad").setHeader("FCAD(%)");
		egu.getColumn("fcad").setWidth(60);
		((NumberField)egu.getColumn("fcad").editor).setDecimalPrecision(2);
		egu.getColumn("std").setHeader("STD(%)");
		egu.getColumn("std").setWidth(60);
		((NumberField)egu.getColumn("std").editor).setDecimalPrecision(2);
		egu.getColumn("qgrad").setHeader("QGRAD(Kcal/kg)");
		egu.getColumn("qgrad").setWidth(80);
		((NumberField)egu.getColumn("qgrad").editor).setDecimalPrecision(2);
		egu.getColumn("hdaf").setHeader("HDAF(%)");
		egu.getColumn("hdaf").setWidth(60);
		((NumberField)egu.getColumn("hdaf").editor).setDecimalPrecision(2);
		egu.getColumn("qgrad_daf").setHeader("QGRAD_DAF(Mj/kg)");
		egu.getColumn("qgrad_daf").setWidth(110);
		((NumberField)egu.getColumn("qgrad_daf").editor).setDecimalPrecision(2);
		egu.getColumn("sdaf").setHeader("SDAF(%)");
		egu.getColumn("sdaf").setWidth(60);
		((NumberField)egu.getColumn("sdaf").editor).setDecimalPrecision(2);
		egu.getColumn("var").setHeader("VAR(%)");
		egu.getColumn("var").setWidth(60);
		((NumberField)egu.getColumn("var").editor).setDecimalPrecision(2);
		egu.getColumn("t1").setHeader("T1(℃)");
		egu.getColumn("t1").setWidth(60);
		((NumberField)egu.getColumn("t1").editor).setDecimalPrecision(2);
		egu.getColumn("t2").setHeader("T2(℃)");
		egu.getColumn("t2").setWidth(60);
		((NumberField)egu.getColumn("t2").editor).setDecimalPrecision(2);
		egu.getColumn("t3").setHeader("T3(℃)");
		egu.getColumn("t3").setWidth(60);
		((NumberField)egu.getColumn("t3").editor).setDecimalPrecision(2);
		egu.getColumn("t4").setHeader("T4(℃)");
		egu.getColumn("t4").setWidth(60);
		((NumberField)egu.getColumn("t4").editor).setDecimalPrecision(2);
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setWidth(60);
//		设置ExtGridUtil不可被编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Read);

		egu.addPaging(25);
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").setEditor(null);
//		egu.getColumn("gongysb_id").setEditor(null);
//		egu.getColumn("meikxxb_id").setEditor(null);
//		egu.getColumn("biaoz").setEditor(null);
//		egu.getColumn("lury").setEditor(null);

		// ********************工具栏************************************************

		egu.addTbarText("发货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至");
		//新添加的日期
		DateField df2 = new DateField();
		df2.setValue(this.getRiq2());
		df2.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df2.getScript());
		
		egu.addTbarText("-");
		// 设置树
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition=" and fahrq>=to_date('"+Riq+"','yyyy-MM-dd') \n " +
						" and fahrq<=to_date('"+Riq2+"','yyyy-MM-dd') and jiesb_id=0 ";
		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
		
//		分厂别
		egu.addTbarText("-");
		egu.addTbarText("单位:");
		ComboBox comb = new ComboBox();
		comb.setTransform("ChangbDropDown");
		comb.setId("Changb");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");

//		设置Toolbar按钮	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "");
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		//添加编辑按钮
		GridButton Create = new GridButton("编辑", "EditValue",SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(Create);
		
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/Yuansfxreport&lx='+RIQI.getValue()+'&lx='+gongys.getValue()+'&lx='+meik.getValue();"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("打印", "function (){" + str + "}").getScript()
				+ "}");
		
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
			init();
		}
		getSelectData();
	}
	
	private void init() {
		
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setString3("");			//riq2
		((Visit) getPage().getVisit()).setString2("");			//Treeid
		((Visit) getPage().getVisit()).setboolean1(false);		//厂别
		this.setChangbModel(null);		//IPropertySelectionModel1
		this.setChangbValue(null);		//IDropDownBean1
		this.getChangbModels();
		
		this.setGongysModel(null);		//IPropertySelectionModel2
		getGongysModels();
		
		this.setMeikdwModel(null);		//IPropertySelectionModel3
		getMeikdwModels();
		
		this.setTree(null);
		this.setTreeid("0");
	}
	
//	厂别
	public IDropDownBean getChangbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setChangbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getChangbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setChangbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getChangbModels() {
		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
					((Visit) getPage().getVisit()).getDiancxxb_id()+"\n"
					+ " union select id,mingc from diancxxb where id="
					+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc";

			((Visit) getPage().getVisit())
			.setProSelectionModel1(new IDropDownModel(sql));
			return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	厂别_End
	
	//供应商Model
	
	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select g.id,g.mingc from gongysb g,gongysdcglb l where 	\n"
				+ " l.gongysb_id=g.id and l.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by g.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	//供应商Model_end
	
//	煤矿Model
	
	public IPropertySelectionModel getMeikdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getMeikdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getMeikdwModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select m.id,m.mingc from gongysb g,meikxxb m,gongysdcglb lg,gongysmkglb lm where g.id=lg.gongysb_id	\n"
				+ " and lm.gongysb_id=g.id and lm.meikxxb_id=m.id and lg.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by m.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
//	煤矿Model_end

	public String getTreeid() {

		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString2(treeid);
			((Visit) getPage().getVisit()).setboolean2(true);
		}
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
}
