package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王总兵
 * 时间：2012-11-5 1
 * 描述：大同电厂管理员临时用的收耗存日报界面
 *  * 其中，
 * 
 */

public abstract class Shouhcrbb_gd_old extends BasePage {
//	界面用户提示
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
//	绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer("begin \n");
		while (mdrsl.next()) {
			String mokmc="";
			if(!(mokmc==null)&&!mokmc.equals("")){
				String id = mdrsl.getString("id");
				//更改时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),SysConstant.RizOpType_UP,mokmc,"Shouhcrbb",id);
			}
			sql.append("update Shouhcrbb set ");
			for (int i = 1; i < mdrsl.getColumnCount(); i++) {
				sql.append(mdrsl.getColumnNames()[i]).append(" = ");
				sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			
//			判断是否同步更新库存 默认同步更新设置为否时不同步更新
			if(MainGlobal.getXitxx_item("收耗存日报", "收耗存日报实时更新库存", "0", "否").equals("是")){
				String kuc_sql="select kuc from shouhcrbb where diancxxb_id="+getTreeid()+" and riq = "+DateUtil.FormatOracleDate(this.getRiq());
				ResultSetList kuc_rsl=con.getResultSetList(kuc_sql);
				if(kuc_rsl.next()){
					double kuccha = CustomMaths.sub(mdrsl.getDouble("kuc"),kuc_rsl.getDouble("KUC"));
	//				更新当前日期以后的所有库存
						sql.append("update shouhcrbb set ")
						.append("kuc = kuc + ").append(kuccha)
						.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
						.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
//					更新当前日期以后的所有可调库存
						sql.append("update shouhcrbb set ")
						.append("kedkc = kuc -bukdml ")
						.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
						.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
					}
				kuc_rsl.close();
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if(flag==-1){
			setMsg( "收耗存库存信息更新失败");
		}
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
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
	}

	private void CreateData() {
//		Visit visit = (Visit) getPage().getVisit();
		long diancxxb_id = Long.parseLong(getTreeid());
		JDBCcon con = new JDBCcon();
//		生成时自动生成日收耗存和分矿数据
		AutoCreateDaily_Report_gd RP=new AutoCreateDaily_Report_gd();
		String rbb=RP.CreateRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String fcb=RP.CreateFCB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String Smsg="";
		if(rbb.length()>0){
			Smsg+=rbb+"<br>";
		}
		if(fcb.length()>0){
			Smsg+=fcb+"<br>";
		}
		if(Smsg.length()>0){
			setMsg(Smsg);
		}
		con.Close();
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		JDBCcon con = new JDBCcon();
		sb.append("begin \n");
//		判断是否同步更新库存 默认同步更新设置为否时不同步更新
		if(MainGlobal.getXitxx_item("收耗存日报", "收耗存日报实时更新库存", "0", "否").equals("是")){
			String kuc_sql="select jingz+CHANGWML - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk kucca from shouhcrbb where diancxxb_id="+getTreeid()+" and riq = "+DateUtil.FormatOracleDate(this.getRiq());
			ResultSetList kuc_rsl=con.getResultSetList(kuc_sql);
				if(kuc_rsl.next()){
//					更新当前日期以后的所有库存
					sb.append("update shouhcrbb set ")
					.append("kuc = kuc - ").append(kuc_rsl.getDouble("KUCCA"))
					.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
					.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
//					更新当前日期以后的所有可调库存
					sb.append("update shouhcrbb set ")
					.append("kedkc = kuc -bukdml ")
					.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
					.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
				}
			kuc_rsl.close();
		}
		sb.append("delete from shouhcrbb where diancxxb_id=").append(diancxxb_id).append(" and riq=").append(CurDate).append(";\n");
		sb.append("end;");

		con.getUpdate(sb.toString());
		con.Close();
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
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		} 
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("select s.id,s.fadl, s.jingz,s.jingz as dangrgm, s.biaoz, s.yuns, s.yingd, s.kuid, s.fady, \n")
		.append("s.gongry, s.qity,s.fady+s.gongry+s.qity+s.cuns+s.feiscy as haoyqkdr,s.feiscy, " +
				"s.cuns, s.tiaozl, s.shuifctz, s.panyk,s.changwml, s.kuc,s.bukdml,s.kedkc \n")
		.append("from shouhcrbb s where diancxxb_id =")
		.append(diancxxb_id).append(" and riq=").append(CurDate);
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcrbb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("dangrgm").setHidden(true);
		egu.getColumn("haoyqkdr").setHidden(true);
		egu.getColumn("fadl").setHeader(Locale.fadl);
		egu.getColumn("fadl").setWidth(60);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);

		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(60);
	
		egu.getColumn("yingd").setHeader(Locale.yingd_fahb);
		egu.getColumn("yingd").setWidth(60);
	
		egu.getColumn("kuid").setHeader(Locale.kuid_fahb);
		egu.getColumn("kuid").setWidth(60);
		
		egu.getColumn("fady").setHeader(Locale.fady);
		egu.getColumn("fady").setWidth(60);
		egu.getColumn("gongry").setHeader(Locale.gongry);
		egu.getColumn("gongry").setWidth(60);
		egu.getColumn("qity").setHeader(Locale.qity);
		egu.getColumn("qity").setWidth(60);
		egu.getColumn("feiscy").setHeader(Locale.feiscy);
		egu.getColumn("feiscy").setWidth(60);
		egu.getColumn("cuns").setHeader(Locale.cuns);
		egu.getColumn("cuns").setWidth(60);
		egu.getColumn("tiaozl").setHeader(Locale.tiaozl);
		egu.getColumn("tiaozl").setWidth(60);
		egu.getColumn("shuifctz").setHeader(Locale.shuifctz);
		egu.getColumn("shuifctz").setWidth(80);
		egu.getColumn("panyk").setHeader(Locale.panyk);
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("kuc").setHeader(Locale.kuc);
		egu.getColumn("kuc").setWidth(60);
		egu.getColumn("bukdml").setHeader("不可调煤量");
		egu.getColumn("bukdml").setWidth(80);
		egu.getColumn("changwml").setHeader("厂外煤场进煤量");
		egu.getColumn("changwml").setWidth(100);
		egu.getColumn("kedkc").setHeader("可调库存");
		egu.getColumn("kedkc").setWidth(60);
		egu.getColumn("kedkc").setHidden(true);
		//egu.getColumn("kedkc").setEditor(null);
//		判断当前系统日期是否为本月1日至4日。如果为本月1日至4日，那么所选的日期是否为上月月末，如果条件都成立则可以查出数据
		String sql= "SELECT 1 FROM (SELECT FIRST_DAY(SYSDATE) FD,\n" + 
			"               TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)), 'yyyy-mm-dd') LD\n" + 
			"          FROM DUAL) SDAY,\n" + 
			"       (SELECT TO_CHAR(SYSDATE, 'yyyy-mm-dd') FD,\n" + 
			"               TO_CHAR("+CurDate+", 'yyyy-mm-dd') LD\n" + 
			"          FROM DUAL) UDAY\n" + 
			" WHERE SDAY.LD = UDAY.LD\n" +
			"	AND SYSDATE BETWEEN SDAY.FD AND SDAY.FD+3";
//		如果不能查询出数据那么存损，调整量，水分差调整和盘盈亏信息将不可显示且不可编辑
		if(!con.getHasIt(sql)&&MainGlobal.getXitxx_item("收耗存日报", "收耗存日报库存调整信息可编辑", "0", "否").equals("否")){
			egu.getColumn("cuns").setEditor(null);
			egu.getColumn("cuns").setHidden(true);
			egu.getColumn("tiaozl").setEditor(null);
			egu.getColumn("tiaozl").setHidden(true);
			egu.getColumn("shuifctz").setEditor(null);
			egu.getColumn("shuifctz").setHidden(true);
			egu.getColumn("panyk").setEditor(null);
			egu.getColumn("panyk").setHidden(true);
		}
		
		
			egu.getColumn("biaoz").setEditor(null);
			egu.getColumn("jingz").setEditor(null);
			egu.getColumn("yuns").setEditor(null);
			egu.getColumn("yingd").setEditor(null);
			egu.getColumn("kuid").setEditor(null);
			egu.getColumn("kuc").setEditor(null);
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('RIQ').value+'的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		生成按钮
		if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("收耗存日报", "分厂别总厂显示生成按钮", diancxxb_id, "否").equals("否")){
			
		}else{
			GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
//			删除按钮
			GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
//			重写保存按钮
			String Script="function (){\n"+
				"Ext.MessageBox.confirm('提示信息','耗用、库存信息是否正确？',function(btn){if(btn == 'yes'){\n"+
				"var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();\n"+
				"for(i = 0; i< Mrcd.length; i++){\n"+
				"if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n"+
				"if(Mrcd[i].get('FADL') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段发电量 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('FADL') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 发电量 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('FADL')!=0 && Mrcd[i].get('FADL') == ''){Ext.MessageBox.alert('提示信息','字段 发电量 不能为空');return;\n"+
				"}if(Mrcd[i].get('DANGRGM') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段DANGRGM 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('DANGRGM') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 DANGRGM 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('DANGRGM')!=0 && Mrcd[i].get('DANGRGM') == ''){Ext.MessageBox.alert('提示信息','字段 DANGRGM 不能为空');return;\n"+
				"}if(Mrcd[i].get('FADY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段发电用 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('FADY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 发电用 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('FADY')!=0 && Mrcd[i].get('FADY') == ''){Ext.MessageBox.alert('提示信息','字段 发电用 不能为空');return;\n"+
				"}if(Mrcd[i].get('GONGRY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段供热用 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('GONGRY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 供热用 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('GONGRY')!=0 && Mrcd[i].get('GONGRY') == ''){Ext.MessageBox.alert('提示信息','字段 供热用 不能为空');return;\n"+
				"}if(Mrcd[i].get('QITY') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段其它用 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('QITY') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 其它用 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('QITY')!=0 && Mrcd[i].get('QITY') == ''){Ext.MessageBox.alert('提示信息','字段 其它用 不能为空');return;\n"+
				"}if(Mrcd[i].get('CUNS') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段存损 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('CUNS') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 存损 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('CUNS')!=0 && Mrcd[i].get('CUNS') == ''){Ext.MessageBox.alert('提示信息','字段 存损 不能为空');return;\n"+
				"}if(Mrcd[i].get('TIAOZL') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段调整量 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('TIAOZL') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 调整量 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('TIAOZL')!=0 && Mrcd[i].get('TIAOZL') == ''){Ext.MessageBox.alert('提示信息','字段 调整量 不能为空');return;\n"+
				"}if(Mrcd[i].get('SHUIFCTZ') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段水分差调整 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('SHUIFCTZ') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 水分差调整 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('SHUIFCTZ')!=0 && Mrcd[i].get('SHUIFCTZ') == ''){Ext.MessageBox.alert('提示信息','字段 水分差调整 不能为空');return;\n"+
				"}if(Mrcd[i].get('PANYK') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段盘盈亏 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('PANYK') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 盘盈亏 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('PANYK')!=0 && Mrcd[i].get('PANYK') == ''){Ext.MessageBox.alert('提示信息','字段 盘盈亏 不能为空');return;\n"+
				"}if(Mrcd[i].get('CHANGWML') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段厂外存煤 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('CHANGWML') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 厂外存煤 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('CHANGWML')!=0 && Mrcd[i].get('CHANGWML') == ''){Ext.MessageBox.alert('提示信息','字段 厂外存煤 不能为空');return;\n"+
				"}if(Mrcd[i].get('BUKDML') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段不可调煤量 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('BUKDML') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 不可调煤量 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('BUKDML')!=0 && Mrcd[i].get('BUKDML') == ''){Ext.MessageBox.alert('提示信息','字段 不可调煤量 不能为空');return;\n"+
				"}if(Mrcd[i].get('KEDKC') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段可调库存 低于最小值 -100000000');return;\n"+
				"}if( Mrcd[i].get('KEDKC') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 可调库存 高于最大值 100000000000');return;\n"+
				"}if(Mrcd[i].get('KEDKC')!=0 && Mrcd[i].get('KEDKC') == ''){Ext.MessageBox.alert('提示信息','字段 可调库存 不能为空');return;\n"+
				"}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n"+
				"+ '<FADL update=\"true\">' + Mrcd[i].get('FADL')+ '</FADL>'\n"+
				"+ '<JINGZ update=\"true\">' + Mrcd[i].get('JINGZ')+ '</JINGZ>'\n"+
				"+ '<DANGRGM update=\"true\">' + Mrcd[i].get('DANGRGM')+ '</DANGRGM>'\n"+
				"+ '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'\n"+
				"+ '<YUNS update=\"true\">' + Mrcd[i].get('YUNS')+ '</YUNS>'\n"+
				"+ '<YINGD update=\"true\">' + Mrcd[i].get('YINGD')+ '</YINGD>'\n"+
				"+ '<KUID update=\"true\">' + Mrcd[i].get('KUID')+ '</KUID>'\n"+
				"+ '<FADY update=\"true\">' + Mrcd[i].get('FADY')+ '</FADY>'\n"+
				"+ '<GONGRY update=\"true\">' + Mrcd[i].get('GONGRY')+ '</GONGRY>'\n"+
				"+ '<QITY update=\"true\">' + Mrcd[i].get('QITY')+ '</QITY>'\n"+
				"+ '<HAOYQKDR update=\"true\">' + Mrcd[i].get('HAOYQKDR')+ '</HAOYQKDR>'\n"+
				"+ '<FEISCY update=\"true\">' + Mrcd[i].get('FEISCY')+ '</FEISCY>'\n"+
				"+ '<CUNS update=\"true\">' + Mrcd[i].get('CUNS')+ '</CUNS>'\n"+
				"+ '<TIAOZL update=\"true\">' + Mrcd[i].get('TIAOZL')+ '</TIAOZL>'\n"+
				"+ '<SHUIFCTZ update=\"true\">' + Mrcd[i].get('SHUIFCTZ')+ '</SHUIFCTZ>'\n"+
				"+ '<PANYK update=\"true\">' + Mrcd[i].get('PANYK')+ '</PANYK>'\n"+
				"+ '<KUC update=\"true\">' + Mrcd[i].get('KUC')+ '</KUC>'\n"+
				"+ '<CHANGWML update=\"true\">' + Mrcd[i].get('CHANGWML')+ '</CHANGWML>'\n"+
				"+ '<BUKDML update=\"true\">' + Mrcd[i].get('BUKDML')+ '</BUKDML>'\n"+
				"+ '<KEDKC update=\"true\">' + Mrcd[i].get('KEDKC')+ '</KEDKC>'\n"+
				" + '</result>' ; }\n"+
				"if(gridDiv_history=='' && gridDivsave_history==''){\n"+ 
				"Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n"+
				"}else{\n"+
				"var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n"+
				"document.getElementById('SaveButton').click();\n"+
				"Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"+
				"}\n"+
				"};});\n"+
				"}";
//			
//			GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
//			egu.addTbarBtn(gbs);
			GridButton gbs =new GridButton("保存",Script);
			gbs.setIcon(SysConstant.Btn_Icon_Save);
			egu.addTbarBtn(gbs);
		}

//		grid 计算方法
		egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);\n");
		
		AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
		String msg=DR.ChkRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		if(msg.length()>0){
			egu.addOtherScript("Ext.MessageBox.alert('提示信息','"+msg+"日数据不完整！');\n");
		}
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将同时覆盖:日收耗存和日估价<br>")
			.append(cnDate).append("的已存数据，是否继续？");
		}else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
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
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			setChangbModels();
			getSelectData();
		}
	}
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
