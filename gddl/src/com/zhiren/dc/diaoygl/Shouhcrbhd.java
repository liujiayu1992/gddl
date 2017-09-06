package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
 /*
  * 作者：夏峥
  * 时间：2012-07-11
  * 描述：前台计算保留2位小数
  */
/*
 * 作者：赵胜男
 * 时间：2013-03-07
 * 描述：增加发电量和供热量填报内容
 */
/*
 * 作者:夏峥
 * 日期:2013-03-25
 * 修改内容:将供热量的单位变更为吉焦。
 */
public abstract class Shouhcrbhd extends BasePage {
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
	
	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer("begin \n");
		while (mdrsl.next()) {
			if(mdrsl.getString("BBLX").equals("1")){
//				更新汇总信息
				sql.append("update Shouhcrbb set ");
				sql.append("JINGZ="+mdrsl.getString("JINGZ")+",\n");
				sql.append("DANGRGM="+mdrsl.getString("DANGRGM")+",\n");
				sql.append("FADY="+mdrsl.getString("FADY")+",\n");
				sql.append("GONGRY="+mdrsl.getString("GONGRY")+",\n");
				sql.append("QITY="+mdrsl.getString("QITY")+",\n");
				sql.append("HAOYQKDR="+mdrsl.getString("HAOYQKDR")+",\n");
				sql.append("FEISCY="+mdrsl.getString("FEISCY")+",\n");
				sql.append("CUNS="+mdrsl.getString("CUNS")+",\n");
				sql.append("TIAOZL="+mdrsl.getString("TIAOZL")+",\n");
				sql.append("SHUIFCTZ="+mdrsl.getString("SHUIFCTZ")+",\n");
				sql.append("PANYK="+mdrsl.getString("PANYK")+",\n");
				sql.append("KUC="+mdrsl.getString("KUC")+",\n");
				sql.append("KEDKC="+mdrsl.getString("KEDKC")+",\n");
				sql.append("FADL="+mdrsl.getString("FADL")+",\n");
				sql.append("GONGRL="+mdrsl.getString("GONGRL")+"\n");
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
				
//				判断是否同步更新库存 默认同步更新设置为否时不同步更新
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
						}
					kuc_rsl.close();
				}
			}else{
//				更新明细信息
				sql.append("update SHOUHCFKB set ");
				sql.append("LAIMSL="+mdrsl.getString("JINGZ")+"\n");
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
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
		String fcb=RP.CreateFCB_HD(con, diancxxb_id, DateUtil.getDate(getRiq()));
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
			String kuc_sql="select jingz - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk kucca from shouhcrbb where diancxxb_id="+getTreeid()+" and riq = "+DateUtil.FormatOracleDate(this.getRiq());
			ResultSetList kuc_rsl=con.getResultSetList(kuc_sql);
				if(kuc_rsl.next()){
//					更新当前日期以后的所有库存
					sb.append("update shouhcrbb set ")
					.append("kuc = kuc - ").append(kuc_rsl.getDouble("KUCCA"))
					.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
					.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
				}
			kuc_rsl.close();
		}
		sb.append("delete from shouhcrbb where diancxxb_id=").append(diancxxb_id).append(" and riq=").append(CurDate).append(";\n");
		sb.append("delete from shouhcfkb where diancxxb_id=").append(diancxxb_id).append(" and riq=").append(CurDate).append(";\n");
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
		sb.append(
		"SELECT ID,BBLX,GONGYSB_ID,MEIKXXB_ID,PINZB_ID,JIHKJB_ID,YUNSFSB_ID,\n" +
		"BIAOZ,JINGZ,DANGRGM,\n" +
		"DECODE(BBLX,1,FADY,DECODE(FADY,0,'',FADY))FADY,\n" +
		"DECODE(BBLX,1,GONGRY,DECODE(GONGRY,0,'',GONGRY))GONGRY,\n" +
		"DECODE(BBLX,1,QITY,DECODE(QITY,0,'',QITY))QITY,\n" +
		"HAOYQKDR,\n" +
		"DECODE(BBLX,1,FEISCY,DECODE(FEISCY,0,'',FEISCY))FEISCY,\n" +
		"DECODE(BBLX,1,CUNS,DECODE(CUNS,0,'',CUNS))CUNS,\n" +
		"DECODE(BBLX,1,TIAOZL,DECODE(TIAOZL,0,'',TIAOZL))TIAOZL,\n" +
		"DECODE(BBLX,1,SHUIFCTZ,DECODE(SHUIFCTZ,0,'',SHUIFCTZ))SHUIFCTZ,\n" +
		"DECODE(BBLX,1,PANYK,DECODE(PANYK,0,'',PANYK))PANYK,\n" +
		"DECODE(BBLX,1,KUC,DECODE(KUC,0,'',KUC))KUC,KEDKC,\n" +
		"DECODE(BBLX,1,FADL,DECODE(FADL,0,'',FADL))FADL,\n"+
        "DECODE(BBLX,1,GONGRL,DECODE(GONGRL,0,'',GONGRL))GONGRL" + 
		"  FROM (SELECT S.ID,\n" + 
		"               1 BBLX,\n" + 
		"               '<font color=''red''>收耗存总计</font>' GONGYSB_ID,\n" + 
		"               '' MEIKXXB_ID,\n" + 
		"               '' PINZB_ID,\n" + 
		"               '' JIHKJB_ID,\n" + 
		"               '' YUNSFSB_ID,\n" + 
		"               S.JINGZ,\n" + 
		"               S.JINGZ AS DANGRGM,\n" + 
		"               S.FADL, S.GONGRL,\n" + 
		"               S.BIAOZ,\n" + 
		"               S.YUNS,\n" + 
		"               S.YINGD,\n" + 
		"               S.KUID,\n" + 
		"               S.FADY,\n" + 
		"               S.GONGRY,\n" + 
		"               S.QITY,\n" + 
		"               S.FADY + S.GONGRY + S.QITY + S.CUNS + S.FEISCY AS HAOYQKDR,\n" + 
		"               S.FEISCY,\n" + 
		"               S.CUNS,\n" + 
		"               S.TIAOZL,\n" + 
		"               S.SHUIFCTZ,\n" + 
		"               S.PANYK,\n" + 
		"               S.KUC,\n" + 
		"               S.KEDKC\n" + 
		"          FROM SHOUHCRBB S\n" + 
		"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"           AND RIQ = "+CurDate+" \n" + 
		"        UNION ALL\n" + 
		"select s.id,\n" +
		"       2         BBLX,\n" + 
		"       g.mingc  GONGYSB_ID,\n" + 
		"       m.mingc  MEIKXXB_ID,\n" + 
		"       p.mingc  PINZB_ID,\n" + 
		"       j.mingc  JIHKJB_ID,\n" + 
		"       Y.mingc  YUNSFSB_ID,\n" + 
		"       s.laimsl AS JINGZ,\n" + 
		"       0    AS DANGRGM,\n" + 
		"       0    AS FADL, 0   AS GONGRL,\n" + 
		"       f.biaoz    AS BIAOZ,\n" + 
		"       0    AS YUNS,\n" + 
		"       0    AS YINGD,\n" + 
		"       0    AS KUID,\n" + 
		"       0    AS FADY, \n" + 
		"       0    AS GONGRY,\n" + 
		"       0    AS QITY,\n" + 
		"       0    AS HAOYQKDR,\n" + 
		"       0    AS FEISCY,\n" + 
		"       0    AS CUNS,\n" + 
		"       0    AS TIAOZL,\n" + 
		"       0    AS SHUIFCTZ,\n" + 
		"       0    AS PANYK,\n" + 
		"       0    AS KUC,\n" + 
		"       0    AS KEDKC\n" + 
		"  from shouhcfkb s,\n" + 
		"       meikxxb m,\n" + 
		"       gongysb g,pinzb p,JIHKJB j,YUNSFSB Y,\n" + 
		"       (select diancxxb_id,\n" + 
		"               daohrq,\n" + 
		"               gongysb_id,pinzb_id,JIHKJB_ID,YUNSFSB_ID,\n" + 
		"               meikxxb_id,\n" + 
		"               nvl(sum(round_new(biaoz,2)), 0) biaoz,\n" + 
		"               nvl(sum(ches), 0) ches\n" + 
		"          from fahb\n" + 
		"         group by diancxxb_id, daohrq, gongysb_id, meikxxb_id,pinzb_id,JIHKJB_ID,YUNSFSB_ID) f\n" + 
		" where s.riq = f.daohrq(+)\n" + 
		"   and s.diancxxb_id = f.diancxxb_id(+)\n" +
		"   and s.gongysb_id = f.gongysb_id(+)\n" +
		"   and s.meikxxb_id = f.meikxxb_id(+)\n" + 
		"   AND s.PINZB_ID=f.PINZB_ID(+)\n" +
		"   AND s.jihkjb_id=f.JIHKJB_ID(+)\n" +
		"   AND S.YUNSFSB_ID = F.YUNSFSB_ID(+)\n" + 
		"   AND S.YUNSFSB_ID = Y.ID(+)\n" + 
		"   and s.JIHKJB_ID=j.ID(+)\n" +
		"   and s.pinzb_id = p.id(+) \n"+
		"   and s.meikxxb_id = m.id(+)\n" + 
		"   and s.gongysb_id = g.id(+)\n" + 
		"   and s.riq = "+CurDate+"\n" + 
		"   and s.diancxxb_id ="+diancxxb_id+") \n"+
		" ORDER BY BBLX,YUNSFSB_ID DESC, GONGYSB_ID, MEIKXXB_ID, PINZB_ID, JIHKJB_ID");

		
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
		
		egu.getColumn("BBLX").setHidden(true);
		egu.getColumn("BBLX").setEditor(null);
		
		egu.getColumn("GONGYSB_ID").setCenterHeader("供应商");
		egu.getColumn("GONGYSB_ID").setEditor(null);
		
		egu.getColumn("MEIKXXB_ID").setCenterHeader("煤矿单位");
		egu.getColumn("MEIKXXB_ID").setEditor(null);
		
		egu.getColumn("PINZB_ID").setCenterHeader("品种");
		egu.getColumn("PINZB_ID").setWidth(60);
		egu.getColumn("PINZB_ID").setEditor(null);
		
		egu.getColumn("JIHKJB_ID").setCenterHeader("计划口径");
		egu.getColumn("JIHKJB_ID").setWidth(80);
		egu.getColumn("JIHKJB_ID").setEditor(null);

		egu.getColumn("YUNSFSB_ID").setCenterHeader("运输方式");
		egu.getColumn("YUNSFSB_ID").setWidth(60);
		egu.getColumn("YUNSFSB_ID").setEditor(null);
		
		egu.getColumn("dangrgm").setHidden(true);
		egu.getColumn("dangrgm").setEditor(null);
		
		egu.getColumn("haoyqkdr").setHidden(true);
		egu.getColumn("haoyqkdr").setEditor(null);
		
		egu.getColumn("BIAOZ").setHeader("票重(吨)");
		egu.getColumn("BIAOZ").setWidth(70);
		egu.getColumn("BIAOZ").setEditor(null);
		
		egu.getColumn("jingz").setHeader("净重(吨)");
		((NumberField)egu.getColumn("jingz").editor).setDecimalPrecision(2);
		egu.getColumn("jingz").setWidth(70);
		
		egu.getColumn("fady").setHeader("发电用(吨)");
		egu.getColumn("fady").setWidth(80);
		((NumberField)egu.getColumn("fady").editor).setDecimalPrecision(2);
		
		
		egu.getColumn("gongry").setHeader("供热用(吨)");
		egu.getColumn("gongry").setWidth(80);
		((NumberField)egu.getColumn("gongry").editor).setDecimalPrecision(2);
		
		egu.getColumn("qity").setHeader("其他用(吨)");
		egu.getColumn("qity").setWidth(80);
		((NumberField)egu.getColumn("qity").editor).setDecimalPrecision(2);
		
		egu.getColumn("feiscy").setHeader("非生产用(吨)");
		egu.getColumn("feiscy").setWidth(80);
		((NumberField)egu.getColumn("feiscy").editor).setDecimalPrecision(2);
		
		egu.getColumn("cuns").setHeader("存损(吨)");
		egu.getColumn("cuns").setWidth(60);
		((NumberField)egu.getColumn("cuns").editor).setDecimalPrecision(2);
		
		egu.getColumn("tiaozl").setHeader("调整量(吨)");
		egu.getColumn("tiaozl").setWidth(60);
		((NumberField)egu.getColumn("tiaozl").editor).setDecimalPrecision(2);
		
		egu.getColumn("shuifctz").setHeader("水分差调整(吨)");
		egu.getColumn("shuifctz").setWidth(80);
		((NumberField)egu.getColumn("shuifctz").editor).setDecimalPrecision(2);
		
		egu.getColumn("panyk").setHeader("盘盈亏(吨)");
		egu.getColumn("panyk").setWidth(60);
		((NumberField)egu.getColumn("panyk").editor).setDecimalPrecision(2);
		
		egu.getColumn("kuc").setHeader("库存(吨)");
		egu.getColumn("kuc").setEditor(null);
		egu.getColumn("kuc").setWidth(80);		

		egu.getColumn("kedkc").setHeader("可调库存(吨)");
		egu.getColumn("kedkc").setWidth(60);
		egu.getColumn("kedkc").setHidden(true);
		egu.getColumn("kedkc").setEditor(null);
		
		egu.getColumn("fadl").setHeader("发电量(万千瓦时)");
		egu.getColumn("fadl").setWidth(110);
		
		egu.getColumn("gongrl").setHeader("供热量(吉焦)");
		egu.getColumn("gongrl").setWidth(90);
		
		
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
		String dataChk=" SELECT  1  FROM SHOUHCRBB S\n" + 
		"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"           AND RIQ = "+CurDate;
		
		if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("收耗存日报", "分厂别总厂显示生成按钮", diancxxb_id, "否").equals("否")){
			
		}else{
//			如果当日有汇总数据，则允许用户删除或编辑
			if(con.getHasIt(dataChk)){
				GridButton gbc = new GridButton("生成差异",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
//				删除按钮
				GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);
//				保存按钮
				GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
				egu.addTbarBtn(gbs);
			}else{
				GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
			}

		}

//		grid 计算方法
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){\n"+
				"if(e.record.get('BBLX')=='2'&&e.field!='JINGZ'){ e.cancel=true;}\n" +
				"if(e.record.get('BBLX')=='1'&&e.field=='JINGZ'){ e.cancel=true;}\n" +
				"});\n"+
				"gridDiv_grid.on('afteredit',function(e){\n"+
				"	if(e.record.get('BBLX')=='1'){countKuc(e);} \n"+
				"	if(e.record.get('BBLX')=='2'){\n"+
				"		changeValue = Math.round(parseFloat(e.value - e.originalValue)*100)/100;\n"+
				"		var rec = gridDiv_ds.getRange();\n"+
				"		for(i = 0; i< rec.length; i++){\n"+
				"			if(rec[i].get('BBLX') =='1'){\n"+
				"				rec[i].set('JINGZ',Math.round((eval(rec[i].get('JINGZ')||0)+changeValue)*100)/100);\n"+
				"				rec[i].set('DANGRGM',eval(rec[i].get('JINGZ')||0));\n"+
				"				rec[i].set('KUC',Math.round((eval(rec[i].get('KUC')||0)+changeValue)*100)/100);\n"+
				"				rec[i].set('KEDKC',Math.round((eval(rec[i].get('KEDKC')||0)+changeValue)*100)/100);\n"+
				"	    		break;\n"+
				"			}\n"+
				"		}	\n"+
				"	}\n"+
				"	});\n");
		
		StringBuffer otherScript = new StringBuffer();
		//加载数据后，设置不可编辑的单元格为蓝色。
		//设置自己的数据的目标值为不可编辑的蓝色
				otherScript.append(" var gridcount=0;\n"+
				"   gridDiv_ds.each(function(r){\n"+
				"	gridDiv_grid.getView().getCell(gridcount,1).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,2).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,3).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,4).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,5).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,6).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,7).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,8).style.backgroundColor='#d6e3f1';\n"+
				"    if(r.get('BBLX')=='2'){\n"+
				"    gridDiv_grid.getView().getCell(gridcount,10).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,11).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,12).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,13).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,14).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,15).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,16).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,17).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,18).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,19).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,20).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,21).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,22).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,23).style.backgroundColor='#d6e3f1';\n"+
				"    }\n"+
				"    if(r.get('BBLX')=='1'){\n"+
				"    gridDiv_grid.getView().getCell(gridcount,9).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,20).style.backgroundColor='#d6e3f1';\n"+
				"    gridDiv_grid.getView().getCell(gridcount,21).style.backgroundColor='#d6e3f1';\n"+
				"    }\n"+
				"    gridcount=gridcount+1;\n"+
				"});");		
		egu.addOtherScript(otherScript.toString());		
				
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
			btnsb.append("是否删除日收耗存和日估价").append(cnDate).append("的全部数据？");
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
			setTreeid(null);
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
