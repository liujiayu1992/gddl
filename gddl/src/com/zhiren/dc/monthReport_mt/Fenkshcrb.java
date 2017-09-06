package com.zhiren.dc.monthReport_mt;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：刘雨
 * 时间：2010-08-24
 * 描述：马头分矿收耗存日报
 */
public class Fenkshcrb extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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

//	刷新数据日期绑定
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=this.getExtGrid();
		StringBuffer sql = new StringBuffer("begin \n");
		String strchange=this.getChange();
		String tableName="fenkshcrbb_mt";
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			sql.append("update ").append(tableName).append(" set ")
				.append("yuejh=").append(mdrsl.getString("yuejh"))
				.append(",haom=").append(mdrsl.getString("dangrhm"))
				.append(",kuc=").append(mdrsl.getString("dangrkc"))
				.append(",beiz='").append(mdrsl.getString("beiz")).append("'")
			    .append(" where id =").append(mdrsl.getString("ID")).append(";\n");
		}
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			setMsg("保存成功！");
		}
		con.Close();
		sql=null;
	}
	
	public void DelData() {
		JDBCcon con = new JDBCcon();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String strRiq = rq[0]+"年"+rq[1]+"月"+rq[2]+"日";
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		
		String sql = "select * from fenkshcrbb_mt where riq>"+CurrODate+"\n";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			setMsg(strRiq+"的数据不是最新的数据，不能删除！");
			return;
		}
		
		String strSql = "delete from fenkshcrbb_mt where riq="+ CurrODate
				+ "\n";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("删除过程中发生错误！");
		} else {
			setMsg(strRiq + "的数据被成功删除！");
		}
		rsl.close();
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}

		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
//			Countlj();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		getSelectData();
	}


	public void CreateData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) getPage().getVisit();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String strRiq = rq[0]+"年"+rq[1]+"月"+rq[2]+"日";
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		long diancxxb_id = visit.getDiancxxb_id();
		int flag_delete = -1;
		int flag_insert = -1;
		String DeSql = "delete fenkshcrbb_mt where riq = " + CurrODate + "\n";
		flag_delete = con.getDelete(DeSql);
		String strSql = 
			"select 1 as jihkjb_id,\n" +
			"		bt.gongysb_id,\n" +
			"       bt.meikxxb_id,\n" + 
			"       bt.laim,\n" + 
			"       nvl(zr.kuc,0)+bt.laim as kuc\n" + 
			"from\n" + 
			"(select f.gongysb_id,\n" + 
			"        f.meikxxb_id,\n" + 
			"        sum(nvl(f.jingz,0)) as laim\n" + 
			"from fahb f\n" + 
			"where f.daohrq = "+CurrODate+" and f.yunsfsb_id=1 \n" + 
			"group by f.gongysb_id,f.meikxxb_id) bt,\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       nvl(f.kuc,0) as kuc\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = ("+CurrODate+"-1) and f.jihkjb_id=1) zr\n" + 
			"where bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = zr.meikxxb_id(+)\n" +
		    "	union\n" +
		    "select 2 as jihkjb_id,\n" +
		    "		bt.gongysb_id,\n" +
			"       bt.meikxxb_id,\n" + 
			"       bt.laim,\n" + 
			"       nvl(zr.kuc,0)+bt.laim as kuc\n" + 
			"from\n" + 
			"(select 1 as gongysb_id,\n" + 
			"        1 as meikxxb_id,\n" + 
			"        sum(nvl(f.jingz,0)) as laim\n" + 
			"from fahb f\n" + 
			"where f.daohrq = "+CurrODate+" and f.yunsfsb_id<>1) bt,\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       nvl(f.kuc,0) as kuc\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = ("+CurrODate+"-1) and f.jihkjb_id=2) zr\n" + 
			"where bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = zr.meikxxb_id(+)";
		ResultSetList rsl = con.getResultSetList(strSql);
		String sql = "begin\n";
		String strid = "";
		while(rsl.next()){
			strid = MainGlobal.getNewID(con, diancxxb_id);
			sql += 
				"insert into fenkshcrbb_mt\n" +
				"values\n" + 
				"  ("+strid+",\n" + 
				"	"+rsl.getString("jihkjb_id")+",\n" +
				"   "+diancxxb_id+",\n" + 
				"   "+rsl.getString("gongysb_id")+",\n" + 
				"   "+rsl.getString("meikxxb_id")+",\n" + 
				"   "+CurrODate+",\n" + 
				"   "+rsl.getString("laim")+",\n" + 
				"   0,\n" + 
				"   "+rsl.getString("kuc")+",\n" + 
				"   0,\n" + 
				"   '');\n";
		}
		sql+="end;\n";
		if(sql.length()>14){
			flag_insert = con.getInsert(sql);
		}
		if(flag_delete!=-1&&flag_insert!=-1){
			rsl.close();
			con.commit();
			con.Close();
			setMsg(strRiq+"的数据成功生成！");
		} else {
			rsl.close();
			con.rollBack();
			con.Close();
			setMsg(strRiq+"的数据成功失败！");
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String MonthFirstDate = "";
		if(rq.length==3){
			MonthFirstDate = DateUtil.FormatOracleDate(rq[0]+"-"+rq[1]+"-01");
		}
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		String strSql = "";
		strSql =
			"select bt.id,\n" +
			"		decode(bt.jihkjb_id,1,'重点订货','市场采购') as jihkjb_id,\n" +
			"       g.mingc as gongysb_id,\n" + 
			"       m.mingc as meikxxb_id,\n" + 
			"       nvl(bt.yuejh,0) as yuejh,\n" + 
			"       nvl(zr.kuc,0) as shangrkc,\n" + 
			"       nvl(bt.laim,0) as dangrlm,\n" + 
			"       nvl(lj.laim,0) as leijlm,\n" + 
			"       nvl(bt.haom,0) as dangrhm,\n" + 
			"       nvl(lj.haom,0) as leijhm,\n" + 
			"       nvl(bt.kuc,0) as dangrkc,\n" + 
			"       bt.beiz\n" + 
			"from gongysb g,\n" + 
			"     meikxxb m,\n" + 
			"(select f.id,\n" + 
			"		f.jihkjb_id,\n" +
			"       f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       f.yuejh,\n" + 
			"       f.laim,\n" + 
			"       f.haom,\n" + 
			"       f.kuc,\n" + 
			"       f.beiz\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = "+CurrODate+") bt,\n" + 
			"\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       f.kuc\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = ("+CurrODate+"-1)) zr,\n" + 
			"\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       sum(f.laim) as laim,\n" + 
			"       sum(f.haom) as haom\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq >= "+MonthFirstDate+"\n" + 
			"  and f.riq <= "+CurrODate+"\n" + 
			"group by f.gongysb_id,f.meikxxb_id) lj\n" + 
			"where bt.gongysb_id = g.id(+)\n" + 
			"  and bt.meikxxb_id = m.id(+)\n" + 
			"  and bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = zr.meikxxb_id(+)\n" + 
			"  and bt.gongysb_id = lj.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = lj.meikxxb_id(+)\n" +
			"order by bt.jihkjb_id,g.mingc,m.mingc\n";

		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:"
					+ strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("fenkshcrbb_mt");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setWidth(120);
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("yuejh").setHeader("月计划");
		egu.getColumn("yuejh").setWidth(65);
		egu.getColumn("shangrkc").setHeader("上日库存");
		egu.getColumn("shangrkc").setEditor(null);
		egu.getColumn("shangrkc").setWidth(65);
		egu.getColumn("dangrlm").setHeader("当日来煤");
		egu.getColumn("dangrlm").setEditor(null);
		egu.getColumn("dangrlm").setWidth(65);
		egu.getColumn("leijlm").setHeader("累计来煤");
		egu.getColumn("leijlm").setEditor(null);
		egu.getColumn("leijlm").setWidth(65);
		egu.getColumn("dangrhm").setHeader("当日耗煤");
		egu.getColumn("dangrhm").setWidth(65);
		egu.getColumn("leijhm").setHeader("累计耗煤");
		egu.getColumn("leijhm").setEditor(null);
		egu.getColumn("leijhm").setWidth(65);
		egu.getColumn("dangrkc").setHeader("当日库存");
		egu.getColumn("dangrkc").setEditor(null);
		egu.getColumn("dangrkc").setWidth(65);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(90);

		egu.setDefaultsortable(false);

		// 把所有列改为可以编辑的。
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.addTbarText("日期:");
		DateField dfRIQ = new DateField();
		dfRIQ.setReadOnly(true);
		dfRIQ.Binding("RIQ", "forms[0]");
		dfRIQ.setValue(getRiq());
		dfRIQ.setId("RIQ");
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'正在刷新数据,请稍候！'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		// 生成按钮
		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		GridButton gbd = new GridButton("删除", getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		// 保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv", egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(
				"	var haom = 0;											\n"+
				"	var leijhm = 0; var dangrkc = 0; var r = 0;				\n"+
				"	gridDiv_grid.on('beforeedit',function(e){				\n"+
				"		if(e.field=='DANGRHM'){								\n"+
				"			haom = e.value;									\n"+
				"		}													\n"+
				"	});														\n"+
				"	gridDiv_grid.on('afteredit',function(e){				\n"+
				"		r = e.row;											\n"+
				"		if(e.field=='DANGRHM'){								\n"+
				"			leijhm = eval(gridDiv_ds.getAt(r).get('DANGRHM'))-eval(haom)+eval(gridDiv_ds.getAt(r).get('LEIJHM'));\n" +
				"			dangrkc = eval(gridDiv_ds.getAt(r).get('SHANGRKC'))+eval(gridDiv_ds.getAt(r).get('DANGRLM'))-eval(gridDiv_ds.getAt(r).get('DANGRHM'));\n" +
				"			gridDiv_ds.getAt(r).set('LEIJHM',leijhm);		\n"+
				"			gridDiv_ds.getAt(r).set('DANGRKC',dangrkc);		\n"+
				"		}													\n"+
				"	});														\n"
		);
	
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String cnDate = rq[0] + "年" + rq[1] + "月" + rq[2] + "日";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
//-----------------  这里用来释放记录的   ----------------------------------------------------
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiq(DateUtil.FormatDate( new Date()));
			getSelectData();
		}
		getSelectData();
	}

//	private boolean Cleaning_up_History(String Date,String Condition){
////		当用户点击“生成”按钮时，要求先删除两个表的数据，才能重新计算
////		当用户点击“保存”按钮时，要求先将累计数据删除，再从新计算累计值
//		JDBCcon con = new JDBCcon();
//		String sql="";
//		boolean Falg=false;
//		String diancxxb_id=this.getTreeid();
//		
//		sql=
//				"delete from yuercbmdj where id in (\n" +
//				"    select js.id\n" + 
//				"       from yuercbmdj js,yuetjkjb_mt kj\n" + 
//				"       where js.yuetjkjb_id=kj.id\n" + 
//				"			  and kj.diancxxb_id=" +diancxxb_id+"\n"+
//				"             and kj.riq="+Date+"\n" +Condition+ 
//				")";
//		
//		if(con.getDelete(sql)>=0){
//			
//			Falg=true;
//		}
//		con.Close();
//		return Falg;
//	}
}