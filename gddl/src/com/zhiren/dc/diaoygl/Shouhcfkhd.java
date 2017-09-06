package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

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
 * 时间：2012-06-24
 * 描述：基于Shouhcfkb.java 1.1.1.29,并根据需求变更而成。
 */
/*
 * 作者：夏峥
 * 时间：2012-07-03
 * 适用范围：国电电力
 * 描述：新增"来煤是否区分厂别"的判断条件	
 */
/*
 * 作者:夏峥
 * 日期:2013-09-26
 * 修改内容:煤价和运价将不从估收取，默认为0。
 * 			生成时增加煤价税，运价税
 */
/*
 * 作者:夏峥
 * 日期:2014-03-26
 * 修改内容:日报火车运费营改增
 * 		 	增加参数“铁路运费税是否是增值税”默认值为“是”
 * 		 	增加参数“铁路运费增值税率”默认值为“0.11”
 */	

public abstract class Shouhcfkhd extends BasePage {
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
		JDBCcon con=new JDBCcon();
		ResultSetList changList=getExtGrid().getModifyResultSet(getChange());
		int rs=0;
		String sql="";
		while(changList.next()){
			String id=changList.getString("id");
			if(id.equals("-1")){
				continue;
			}
			double rez=changList.getDouble("rez");
			double meij=changList.getDouble("meij");
			double yunj=changList.getDouble("yunj");
			double laimsl=changList.getDouble("laimsl");
			
			double meijs=changList.getDouble("meijs");
			double yunjs=changList.getDouble("yunjs");
			double huocyj=changList.getDouble("huocyj");
			double huocyjs=changList.getDouble("huocyjs");
			double qicyj=changList.getDouble("qicyj");
			double qicyjs=changList.getDouble("qicyjs");
			
			sql+="update shouhcfkb\n" +
				"   set rez = "+rez+", meij = "+meij+", meijs = "+meijs+",\n" +
						" huocyj = "+huocyj+", huocyjs = "+huocyjs+", \n" +
						" qicyj = "+qicyj+", qicyjs = "+qicyjs+",\n" +
						" yunjs = "+yunjs+", yunj = "+yunj+",laimsl="+laimsl+"\n" + 
				" where id = "+id+";\n";

		}
		if(sql.length()!=0){
			rs=con.getInsert("begin\n"+sql.toString()+"\nend;");
			if (rs==-1) {
				setMsg("保存失败！");
			}else{
				setMsg("保存成功！");
			}
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
		long diancxxb_id = Long.parseLong(getTreeid());
		JDBCcon con = new JDBCcon();
		
		AutoCreateDaily_Report_gd RP=new AutoCreateDaily_Report_gd();
		String fcb=RP.GetRez(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String Smsg="";
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
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
//		界面是否可编辑
		boolean isEditable=true;
		StringBuffer sb = new StringBuffer();
//		需修正前台查询语句
		sb.append(
				"select s.id,\n" +
				"       g.mingc  gongysmc,\n" + 
				"       m.mingc  meikmc,\n" + 
				"       p.mingc  pinz,\n" + 
				"       j.mingc  JIHKJB_ID,\n" + 
				"       Y.mingc  YUNSFSB_ID,\n" + 
				"       f.biaoz,\n" + 
				"       f.ches,\n" + 
				"       s.rez,\n" + 
				"       s.meij,\n" + 
				"       s.meijs,\n" + 
				"       s.huocyj,\n" + 
				"       s.huocyjs,\n" + 
				"       s.qicyj,\n" + 
				"       s.qicyjs,\n" + 
				"       s.yunj,\n" + 
				"       s.yunjs,\n" + 
				"       s.laimsl\n" + 
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
				"   and s.riq = "+CurDate+"\n");
		
		if(MainGlobal.getXitxx_item("收耗存日报", "来煤是否区分厂别", "0", "是").equals("是")){
			sb.append("   and s.diancxxb_id in (select id from diancxxb where id="+diancxxb_id+" or fuid="+diancxxb_id+")");
		}
		JDBCcon con = new JDBCcon();
//		判断所选日期内是否有数据
		if(con.getHasIt(sb.toString())){
			Date curdate=new Date();
			long t=(curdate.getTime()-DateUtil.getDate(getRiq()).getTime())/(3600*24*1000);
//			如果时间多于2天，那么界面显示编辑相关按钮将为关闭状态
//			从系统信息表中取得日报不可编辑天数信息，默认值为2吨。
			if(t>Long.parseLong(MainGlobal.getXitxx_item("收耗存日报", "日报不可编辑天数", "0", "2"))){
				isEditable=false;
			}
		}
//		新增汇总行信息
		sb.append(
				"   UNION\n" +
				"SELECT -1 ID,'总计' GONGYSMC,MEIKMC,PINZ,JIHKJB_ID,YUNSFSB_ID,BIAOZ,CHES,REZ,MEIJ,MEIJS,HUOCYJ,HUOCYJS,QICYJ,QICYJS,YUNJ,YUNJS,LAIMSL\n" + 
				"  FROM (SELECT G.MINGC GONGYSMC,\n" + 
				"               M.MINGC MEIKMC,\n" + 
				"               P.MINGC PINZ,\n" + 
				"               J.MINGC JIHKJB_ID,\n" + 
				"               Y.MINGC YUNSFSB_ID,\n" + 
				"               SUM(F.BIAOZ) BIAOZ,\n" + 
				"               SUM(F.CHES) CHES,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.REZ,0,0,S.LAIMSL)), 0, 0, SUM(S.REZ * S.LAIMSL) / SUM(DECODE(S.REZ,0,0,S.LAIMSL))),2) REZ,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(S.MEIJ * S.LAIMSL) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) MEIJ,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJS,0,0,S.LAIMSL)), 0, 0, SUM(S.MEIJS * S.LAIMSL) / SUM(DECODE(S.MEIJS,0,0,S.LAIMSL))),2) MEIJS,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.huocyj) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) huocyj,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.huocyjs) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) huocyjs,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.qicyj) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) qicyj,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.qicyjs) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) qicyjs,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.YUNJ) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) YUNJ,\n" + 
				"               ROUND_NEW(DECODE(SUM(DECODE(S.MEIJ,0,0,S.LAIMSL)), 0, 0, SUM(DECODE(S.MEIJ,0,0,S.LAIMSL) * S.yunjs) / SUM(DECODE(S.MEIJ,0,0,S.LAIMSL))),2) YUNJS,\n" + 
				"               ROUND_NEW(SUM(S.LAIMSL),2) LAIMSL\n" + 
				"          FROM SHOUHCFKB S,\n" + 
				"               MEIKXXB M,\n" + 
				"               GONGYSB G,\n" + 
				"               PINZB P,\n" + 
				"               JIHKJB J,YUNSFSB Y,\n" + 
				"               (SELECT DIANCXXB_ID,\n" + 
				"                       DAOHRQ,\n" + 
				"                       GONGYSB_ID,\n" + 
				"                       PINZB_ID,\n" + 
				"                       JIHKJB_ID,\n" + 
				"                       MEIKXXB_ID,YUNSFSB_ID,\n" + 
				"                       NVL(SUM(ROUND_NEW(BIAOZ, 2)), 0) BIAOZ,\n" + 
				"                       NVL(SUM(CHES), 0) CHES\n" + 
				"                  FROM FAHB\n" + 
				"                 GROUP BY DIANCXXB_ID,\n" + 
				"                          DAOHRQ,\n" + 
				"                          GONGYSB_ID,\n" + 
				"                          MEIKXXB_ID,\n" + 
				"                          PINZB_ID,\n" + 
				"                          JIHKJB_ID,YUNSFSB_ID) F\n" + 
				"         WHERE S.RIQ = F.DAOHRQ(+)\n" + 
				"           AND S.GONGYSB_ID = F.GONGYSB_ID(+)\n" + 
				"           AND S.MEIKXXB_ID = F.MEIKXXB_ID(+)\n" + 
				"           AND S.PINZB_ID = F.PINZB_ID(+)\n" + 
				"           AND S.JIHKJB_ID = F.JIHKJB_ID(+)\n" + 
				"           AND S.YUNSFSB_ID = F.YUNSFSB_ID(+)\n" + 
				"   		AND S.YUNSFSB_ID = Y.ID(+)\n" + 
				"           AND S.JIHKJB_ID = J.ID(+)\n" + 
				"           AND S.PINZB_ID = P.ID(+)\n" + 
				"           AND S.MEIKXXB_ID = M.ID(+)\n" + 
				"           AND S.GONGYSB_ID = G.ID(+)\n" + 
				"           AND S.RIQ = "+CurDate+"\n");
				if(MainGlobal.getXitxx_item("收耗存日报", "来煤是否区分厂别", "0", "是").equals("是")){
					sb.append("   and s.diancxxb_id = f.diancxxb_id(+)\n");
					sb.append("   and s.diancxxb_id in (select id from diancxxb where id="+diancxxb_id+" or fuid="+diancxxb_id+")");
				}
		sb.append("         GROUP BY ROLLUP((G.MINGC, M.MINGC, P.MINGC, J.MINGC,Y.MINGC))\n" + 
				  "        HAVING GROUPING(G.MINGC) = 1) ORDER BY ID");

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		String yunjslx = MainGlobal.getXitxx_item("结算", "运费税是否是增值税", diancxxb_id, "是");
		String huoqlx = MainGlobal.getXitxx_item("月报", "运费是否火汽联运", diancxxb_id, "否");
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcfkb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("gongysmc").setHeader("供货单位");
		egu.getColumn("gongysmc").setWidth(160);
		egu.getColumn("gongysmc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("meikmc").setHeader("煤矿单位");
		egu.getColumn("meikmc").setWidth(160);
		egu.getColumn("meikmc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
				
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("pinz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("JIHKJB_ID").setHeader("统计口径");
		egu.getColumn("JIHKJB_ID").setWidth(70);
		egu.getColumn("JIHKJB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("YUNSFSB_ID").setHeader("运输方式");
		egu.getColumn("YUNSFSB_ID").setWidth(60);
		egu.getColumn("YUNSFSB_ID").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("biaoz").setHeader("标重");
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("biaoz").setWidth(60);
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("ches").setWidth(40);
		
		egu.getColumn("rez").setHeader("热值<br>(MJ/kg)");
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		egu.getColumn("rez").setWidth(80);
		
		egu.getColumn("rez").setHeader("热值<br>(MJ/kg)");
		((NumberField)egu.getColumn("rez").editor).setDecimalPrecision(2);
		egu.getColumn("rez").setWidth(50);
		
		egu.getColumn("meij").setHeader("含税煤价<br>(元/吨)");
		((NumberField)egu.getColumn("meij").editor).setDecimalPrecision(2);
		egu.getColumn("meij").setWidth(60);
		
		egu.getColumn("meijS").setHeader("煤价税<br>(元/吨)");
		egu.getColumn("meijS").setWidth(60);
		egu.getColumn("meijS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meijS").setEditor(null);
		
		egu.getColumn("HUOCYJ").setHeader("火车<br>含税运价<br>(元/吨)");
		egu.getColumn("HUOCYJ").setWidth(60);
		egu.getColumn("HUOCYJ").editor.setMinValue("0");
		((NumberField)egu.getColumn("HUOCYJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("HUOCYJS").setHeader("火车<br>运价税<br>(元/吨)");
		egu.getColumn("HUOCYJS").setWidth(60);
		egu.getColumn("HUOCYJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("HUOCYJS").setEditor(null);
		
		egu.getColumn("QICYJ").setHeader("汽车<br>含税运价<br>(元/吨)");
		egu.getColumn("QICYJ").setWidth(60);
		egu.getColumn("QICYJ").editor.setMinValue("0");
		((NumberField)egu.getColumn("QICYJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("QICYJS").setHeader("汽车<br>运价税<br>(元/吨)");
		egu.getColumn("QICYJS").setWidth(60);
		egu.getColumn("QICYJS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("QICYJS").setEditor(null);
		
		egu.getColumn("yunj").setHeader("总运价<br>(元/吨)");
		egu.getColumn("yunj").editor.setMinValue("0");
		((NumberField)egu.getColumn("yunj").editor).setDecimalPrecision(2);
		egu.getColumn("yunj").setWidth(60);
		
//		火汽联运时，运价将不可编辑。
		if(huoqlx.equals("否")){
			egu.getColumn("HUOCYJ").setHidden(true);
			egu.getColumn("HUOCYJS").setHidden(true);
			egu.getColumn("QICYJ").setHidden(true);
			egu.getColumn("QICYJS").setHidden(true);
		}else{
			egu.getColumn("yunj").setEditor(null);
			egu.getColumn("yunj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		}
		egu.getColumn("yunjs").setHeader("总运价税<br>(元/吨)");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("yunjs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunjs").setEditor(null);
		
		egu.getColumn("laimsl").setHeader("来煤数量");
		egu.getColumn("laimsl").setWidth(80);
		egu.getColumn("laimsl").setEditor(null);
		
		if("是".equals(MainGlobal.getXitxx_item("日报", "日估价维护标重是否隐藏", getTreeid(), "否"))){
			egu.getColumn("biaoz").setHidden(true);
		}
		
		if(MainGlobal.getXitxx_item("收耗存日报", "收耗存日报热值可编辑", "0", "否").equals("否")){
			egu.getColumn("rez").setEditor(null);
			egu.getColumn("rez").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
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
		
		if(isEditable){
//			生成按钮
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("收耗存日报", "分厂别总厂显示生成按钮", diancxxb_id, "否").equals("否")){
				
			}else{
				GridButton gbc = new GridButton("获取数据",getBtnHandlerScript("CreateButton"));
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);

//				保存按钮
				GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
				egu.addTbarBtn(gbs);
				egu.addTbarText("-");
				egu.addTbarText("保存后刷新总计行数据，如若数据异常可手动删除后再次生成");
			}
		}
		
		String meijs = MainGlobal.getXitxx_item("结算", "煤款税率", diancxxb_id, "0.17");
		String yunjsl = MainGlobal.getXitxx_item("结算", "运费税率", diancxxb_id, "0.07");		
		String yunjzzsl = MainGlobal.getXitxx_item("结算", "运费增值税率", diancxxb_id, "0.11");		
		String Tlyjslx = MainGlobal.getXitxx_item("结算", "铁路运费税是否是增值税", diancxxb_id, "是");
		String Tlyjzzsl = MainGlobal.getXitxx_item("结算", "铁路运费增值税率", diancxxb_id, "0.11");	
		
//		grid 计算方法
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){"+
				"if(e.field=='GONGYSMC'||e.field=='MEIKMC'||e.field=='PINZ'||e.field=='JIHKJB_ID'||e.field=='YUNSFSB_ID'||e.record.get('ID')=='-1'){e.cancel=true;}\n" +
				"if(e.field=='MEIJ'||e.field=='YUNJ'){if(e.record.get('REZ')==0){e.cancel=true;}}\n" +
				"});\n");
		
		StringBuffer sbAF = new StringBuffer();
		sbAF.append("gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='MEIJ'){\n" + 
				"    var meijs=0,i=0;\n" + 
				"    i=e.row;\n" + 
				"    meijs=Round_new(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)-(eval(gridDiv_ds.getAt(i).get('MEIJ')||0)/(1+" + meijs + ")),2);\n" + 
				"    gridDiv_ds.getAt(i).set('MEIJS',meijs);\n" + 
				"  }\n"); 
		if(huoqlx.equals("是")){
			sbAF.append("  if(e.field=='HUOCYJ'){\n" + 
					"    var HCyunjs=0,i=0;\n" + 
					"    i=e.row;\n" );
			if(Tlyjslx.equals("是")){
				sbAF.append("HCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)-(eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)/(1+" + Tlyjzzsl + ")),2);\n");
			}else{
				sbAF.append("HCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)*" + yunjsl + ",2);\n" );
			}

			sbAF.append("    gridDiv_ds.getAt(i).set('HUOCYJS',HCyunjs);\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJ',eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)+eval(gridDiv_ds.getAt(i).get('QICYJ')||0));\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJS',eval(gridDiv_ds.getAt(i).get('HUOCYJS')||0)+eval(gridDiv_ds.getAt(i).get('QICYJS')||0));}\n" );
			
			sbAF.append("  if(e.field=='QICYJ'){\n" + 
					"    var QCyunjs=0,i=0;\n" + 
					"    i=e.row;\n" );
			if(yunjslx.equals("是")){
				sbAF.append("QCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('QICYJ')||0)-(eval(gridDiv_ds.getAt(i).get('QICYJ')||0)/(1+" + yunjzzsl + ")),2);\n");
			}else{
				sbAF.append("QCyunjs=Round_new(eval(gridDiv_ds.getAt(i).get('QICYJ')||0)*" + yunjsl + ",2);\n" );
			}
			sbAF.append("   gridDiv_ds.getAt(i).set('QICYJS',QCyunjs);\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJ',eval(gridDiv_ds.getAt(i).get('HUOCYJ')||0)+eval(gridDiv_ds.getAt(i).get('QICYJ')||0));\n" +
					  "    gridDiv_ds.getAt(i).set('YUNJS',eval(gridDiv_ds.getAt(i).get('HUOCYJS')||0)+eval(gridDiv_ds.getAt(i).get('QICYJS')||0));}\n" );
		}
				
		sbAF.append("  if(e.field=='YUNJ'){\n" + 
				"    var yunjs=0,i=0;\n" + 
				"    i=e.row;\n" );
		if(yunjslx.equals("是")){
			sbAF.append("if(gridDiv_ds.getAt(i).get('YUNSFSB_ID')=='铁路'){  \n");
			if(Tlyjslx.equals("是")){
				sbAF.append(" yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + Tlyjzzsl + ")),2);\n");
			}else{
				sbAF.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n");
			}
			sbAF.append("} else{  yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)-(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)/(1+" + yunjzzsl + ")),2);}\n");
		}else{
			sbAF.append("yunjs=Round_new(eval(gridDiv_ds.getAt(i).get('YUNJ')||0)*" + yunjsl + ",2);\n" );
		}
									
		sbAF.append("    gridDiv_ds.getAt(i).set('YUNJS',yunjs);\n" + 
				"  }\n });"
		);
		egu.addOtherScript(sbAF.toString());
		
		AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
		String errmsg=DR.ChkFKB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		if(errmsg.length()>0){
			egu.addOtherScript("Ext.MessageBox.alert('提示信息','"+errmsg+"日数据不完整,请补全！');\n");
			
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
