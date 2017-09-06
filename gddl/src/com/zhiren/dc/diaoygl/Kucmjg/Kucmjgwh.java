package com.zhiren.dc.diaoygl.Kucmjg;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Kucmjgwh extends BasePage {
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
	
//	保存方法
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	private StringBuffer DataCheck(){
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		JDBCcon con=new JDBCcon();
		String sql="SELECT RBHY, RBKC, HY, KUC\n" +
			"  FROM (SELECT NVL(SUM(FADY + GONGRY + QITY + FEISCY + CUNS - TIAOZL -\n" + 
			"                       SHUIFCTZ - PANYK),\n" + 
			"                   0) AS RBHY,\n" + 
			"               NVL(SUM(KEDKC), 0) AS RBKC\n" + 
			"          FROM SHOUHCRBB\n" + 
			"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"           AND RIQ = "+CurDate+") RB,\n" + 
			"       (SELECT NVL(SUM(KUCMJGB.HAOMSL), 0) HY,\n" + 
			"               NVL(SUM(KUCMJGB.KUCSL)*10000, 0) KUC\n" + 
			"          FROM KUCMJGB\n" + 
			"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"           AND RIQ = "+CurDate+") LR";
		ResultSetList rsl=con.getResultSetList(sql);
		StringBuffer errorMsg=new StringBuffer();
		while(rsl.next()) {
			if(rsl.getDouble("RBHY")!=rsl.getDouble("HY")){
				errorMsg.append("日报耗用总量与录入耗用总量不匹配<br>");
				errorMsg.append("日报耗用总量："+rsl.getDouble("RBHY")+"吨<br>");
				errorMsg.append("录入耗用总量："+rsl.getDouble("HY")+"吨<br>");
			}
			if(rsl.getDouble("RBKC")!=rsl.getDouble("KUC")){
				errorMsg.append("日报库存总量与录入库存总量不匹配<br>");
				errorMsg.append("日报库存总量："+rsl.getDouble("RBKC")+"吨<br>");
				errorMsg.append("录入库存总量："+rsl.getDouble("KUC")+"吨<br>");
			}
			break;
		}
		return errorMsg;	
	}
	
	
//	得到记录状态
	private int getRecZhuangt(JDBCcon con){
//		当返回值为0时，无记录集。
//		当返回值为1时，记录已提交。
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		String sql = "select  nvl(count(ID),0) Rec,max(zhuangt) zhuangt from KUCMJGB where diancxxb_id="+diancxxb_id+" and riq="+CurDate;		
		ResultSetList rsl=con.getResultSetList(sql);
		while (rsl.next()){
			if(rsl.getString("REC").equals("0")){
				rsl.close();
				return 0;
			}
			if(rsl.getString("ZHUANGT").equals("1")){
				rsl.close();
				return 1;
			}
			break;
		}
		rsl.close();
		return 2;
	}
	
//	创建数据
	private void CreateData() {
//		先清除数据
		DelData();
//		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
//		写入数据
//		取得前一日KUCMJGB中的品种信息
//		并联上取得当日来煤的品种信息形成当日的品种信息并存入KUCMJGB中
		String sql=
		"INSERT INTO KUCMJGB\n" + 
		"  (ID, DIANCXXB_ID, RIQ, PINZB_ID)\n" + 
		"  (SELECT GETNEWID("+diancxxb_id+"), "+diancxxb_id+", "+CurDate+", PINZB_ID\n" + 
		"     FROM (SELECT PINZB_ID\n" + 
		"             FROM KUCMJGB\n" + 
		"            WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"              AND RIQ ="+CurDate+" - 1\n" + 
		"           UNION\n" + 
		"           SELECT PINZB_ID\n" + 
		"             FROM SHOUHCFKB\n" + 
		"            WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"              AND RIQ = "+CurDate+"))";
		con.getInsert(sql);
//		更新KUCMJGB中的昨日品种对应的库存信息 
		sql="UPDATE KUCMJGB K1\n" + 
		"   SET (KUCSL, KUCRL, KUCBMDJ) =\n" + 
		"       (SELECT  nvl(sum(KUCSL),0), nvl(sum(KUCRL),0), nvl(sum(KUCBMDJ),0)\n" + 
		"          FROM KUCMJGB K\n" + 
		"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"           AND RIQ ="+CurDate+"- 1\n" + 
		"           AND K.PINZB_ID = K1.PINZB_ID)\n" + 
		" WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"   AND RIQ = "+CurDate+"";
		con.getUpdate(sql);
//		更新当日来煤信息至KUCMJGB表中对应的品种中
		sql=
		"UPDATE (SELECT PINZB_ID, LAIMSL, LAIMRL,RUCBMDJ\n" + 
		"          FROM KUCMJGB\n" + 
		"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"           AND RIQ = "+CurDate+" \n" + 
		"           AND PINZB_ID IN\n" + 
		"               (SELECT PINZB_ID\n" + 
		"                  FROM SHOUHCFKB SHC\n" + 
		"                 WHERE SHC.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"                   AND SHC.RIQ = "+CurDate+"\n" + 
		"                 GROUP BY SHC.PINZB_ID)) K1\n" + 
		"   SET (LAIMSL, LAIMRL, RUCBMDJ) =\n" + 
		" (SELECT LAIMSL,LAIMRL,RUCBMDJ FROM (SELECT SHC.PINZB_ID,\n" +
		"             SUM(SHC.LAIMSL) LAIMSL,\n" + 
		"             ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ, 0, 0, SHC.LAIMSL)),\n" + 
		"                              0,\n" + 
		"                              0,\n" + 
		"                              SUM(SHC.REZ * SHC.LAIMSL) /\n" + 
		"                              SUM(DECODE(SHC.REZ, 0, 0, SHC.LAIMSL))) * 1000 /\n" + 
		"                       4.1816,\n" + 
		"                       0) LAIMRL,\n" + 
		"             ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ, 0, 0, SHC.LAIMSL)),\n" + 
		"                              0,\n" + 
		"                              0,\n" + 
		"                              SUM(DECODE(SHC.REZ,\n" + 
		"                                         0,\n" + 
		"                                         0,\n" + 
		"                                         (SHC.MEIJ + SHC.YUNJ) * 29.271 /\n" + 
		"                                         SHC.REZ * SHC.LAIMSL)) /\n" + 
		"                              SUM(DECODE(SHC.REZ, 0, 0, SHC.LAIMSL))),\n" + 
		"                       2) RUCBMDJ\n" + 
		"        FROM SHOUHCFKB SHC\n" + 
		"       WHERE SHC.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"         AND SHC.RIQ = "+CurDate+"\n" + 
		"       GROUP BY SHC.PINZB_ID) K\n"+
		"         WHERE K.PINZB_ID = K1.PINZB_ID)\n";
		con.getUpdate(sql);
//		更新当日耗煤热量
		sql="UPDATE KUCMJGB K1\n" + 
		"   SET HAOMRL =\n" + 
		"       (SELECT NVL(ROUND_NEW(DECODE(SUM(MEIL),\n" + 
		"                                0,\n" + 
		"                                0,\n" + 
		"                                SUM(MEIL * QNET_AR) / SUM(MEIL) * 1000 /\n" + 
		"                                4.1816),0),\n" + 
		"                   0) HAOMRL\n" + 
		"          FROM RULMZLB\n" + 
		"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"           AND FENXRQ = "+CurDate+")\n" + 
		" WHERE K1.DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"   AND K1.RIQ = "+CurDate;
		con.getUpdate(sql);
		
//		更新库存信息
		sql="UPDATE (SELECT ID, KUCSL, KUCRL, KUCBMDJ\n" +
			"          FROM KUCMJGB\n" + 
			"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"           AND RIQ = "+CurDate+"\n" + 
			"           AND PINZB_ID IN (SELECT PINZB_ID\n" + 
			"                              FROM SHOUHCFKB SHC\n" + 
			"                             WHERE SHC.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                               AND SHC.RIQ = "+CurDate+"\n" + 
			"                             GROUP BY SHC.PINZB_ID)) K1\n" + 
			"   SET (KUCSL, KUCRL, KUCBMDJ) =\n" + 
			"       (SELECT K.KUCSL, K.KUCRL, K.KUCBMDJ\n" + 
			"          FROM (SELECT ID,\n" + 
			"                       KUCSL + LAIMSL/10000 KUCSL,\n" + 
			"                       DECODE((KUCSL + LAIMSL/10000),\n" + 
			"                              0,\n" + 
			"                              0,\n" + 
			"                              (KUCSL * KUCRL + LAIMSL * LAIMRL/10000) /\n" + 
			"                              (KUCSL + LAIMSL/10000)) KUCRL,\n" + 
			"                       ROUND_NEW(decode(KUCRL,0,RUCBMDJ,LAIMRL,0,0,DECODE((KUCSL*7000/KUCRL + LAIMSL*7000/LAIMRL/10000),\n" + 
			"                              0,\n" + 
			"                              0,\n" + 
			"                              (KUCSL*7000/KUCRL * KUCBMDJ + LAIMSL*7000/LAIMRL * RUCBMDJ/10000) /\n" + 
			"                              (KUCSL*7000/KUCRL + LAIMSL*7000/LAIMRL/10000))),2) KUCBMDJ\n" + 
			"                  FROM KUCMJGB\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurDate+"\n" + 
			"                   AND PINZB_ID IN (SELECT PINZB_ID\n" + 
			"                                      FROM SHOUHCFKB SHC\n" + 
			"                                     WHERE SHC.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                                       AND SHC.RIQ = "+CurDate+"\n" + 
			"                                     GROUP BY SHC.PINZB_ID)) K\n" + 
			"         WHERE K1.ID = K.ID)";
		con.getUpdate(sql);
		con.Close();
	}
	
//	删除操作
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("delete from KUCMJGB where diancxxb_id="+diancxxb_id+" and riq="+CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
		con.Close();
	}
	
	private void Tij(){
		StringBuffer msg=new StringBuffer();
		msg=DataCheck();
		if(msg.length()>1){
			setMsg("数据异常<br>"+msg.toString()+"<br>请修正保存后再提交！");
			return;
		}
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("update KUCMJGB set zhuangt=1 where diancxxb_id="+diancxxb_id+" and riq="+CurDate);
		JDBCcon con = new JDBCcon();
		con.getDelete(sb.toString());
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
	
	private boolean _TijClick=false;
	
	public void TijButton(IRequestCycle cycle){
		_TijClick=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
			StringBuffer msg=new StringBuffer();
			msg=DataCheck();
			if(msg.length()>1){
				setMsg(msg.toString());
			}
		}
		if(_TijClick){
			_TijClick=false;
			Tij();
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
//		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT K.ID,\n" +
				"       K.DIANCXXB_ID,\n" + 
				"       K.RIQ,\n" + 
				"       P.MINGC PINZB_ID,\n" + 
				"       K.LAIMSL,\n" + 
				"       K.LAIMRL,k.RUCBMDJ,\n" + 
				"       K.HAOMSL,\n" + 
				"       K.HAOMRL,k.HAOMZF,k.RULBMDJ,\n" + 
				"       K.KUCSL,\n" + 
				"       K.KUCRL,\n" + 
				"       K.KUCBMDJ,\n" + 
				"       K.XIAZLMYC,\n" + 
				"       K.BEIZ,\n" + 
				"       nvl(zr.KUCSL,0) zrsl,\n" + 
				"       nvl(zr.KUCRL,0) zrrl,\n" + 
				"       nvl(zr.KUCBMDJ,0) zrdj\n" + 
				"  FROM KUCMJGB K,\n" + 
				"       PINZB P,\n" + 
				"       (SELECT PINZB_ID, KUCSL, KUCRL,KUCBMDJ\n" + 
				"          FROM KUCMJGB\n" + 
				"         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
				"           AND RIQ = "+CurDate+" - 1) zr\n" + 
				" WHERE P.ID = K.PINZB_ID\n" + 
				" AND zr.pinzb_id(+)=k.pinzb_id\n" + 
				"   AND K.DIANCXXB_ID = "+diancxxb_id+"\n" + 
				"   AND K.RIQ = "+CurDate);

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sb.toString());

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("KUCMJGB");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("DIANCXXB_ID").setHidden(true);
		egu.getColumn("DIANCXXB_ID").setEditor(null);
		
		egu.getColumn("RIQ").setHeader("日期");
		egu.getColumn("RIQ").setWidth(80);
		egu.getColumn("RIQ").setEditor(null);
		egu.getColumn("RIQ").update=false;
		
		egu.getColumn("PINZB_ID").setHeader("煤种");
		egu.getColumn("PINZB_ID").setWidth(60);
		egu.getColumn("PINZB_ID").setEditor(null);
		egu.getColumn("PINZB_ID").update=false;

		egu.getColumn("LAIMSL").setCenterHeader("来煤数量<br>(吨)");
		egu.getColumn("LAIMSL").setWidth(80);
		egu.getColumn("LAIMSL").setEditor(null);
		
		egu.getColumn("LAIMRL").setCenterHeader("来煤热值<br>(Kcal/Kg)");
		egu.getColumn("LAIMRL").setWidth(80);
		egu.getColumn("LAIMRL").setEditor(null);
		
		egu.getColumn("RUCBMDJ").setCenterHeader("入厂标煤单价<br>(元/吨)");
		egu.getColumn("RUCBMDJ").setWidth(80);
		((NumberField) egu.getColumn("RUCBMDJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("HAOMSL").setCenterHeader("耗煤量<br>(吨)");
		egu.getColumn("HAOMSL").setWidth(80);
		((NumberField) egu.getColumn("HAOMSL").editor).setDecimalPrecision(3);
		
		egu.getColumn("HAOMRL").setCenterHeader("耗煤热值<br>(Kcal/Kg)");
		egu.getColumn("HAOMRL").setWidth(80);
		((NumberField) egu.getColumn("HAOMRL").editor).setDecimalPrecision(0);
//		egu.getColumn("HAOMRL").setEditor(null);
		
		egu.getColumn("HAOMZF").setCenterHeader("耗煤杂费<br>(万元)");
		egu.getColumn("HAOMZF").setWidth(80);
		((NumberField) egu.getColumn("HAOMZF").editor).setDecimalPrecision(6);
		
		egu.getColumn("RULBMDJ").setCenterHeader("入炉标煤单价<br>(元/吨)");
		egu.getColumn("RULBMDJ").setWidth(80);
		((NumberField) egu.getColumn("RULBMDJ").editor).setDecimalPrecision(2);
		
		
		egu.getColumn("KUCSL").setCenterHeader("库存数量<br>(万吨)");
		egu.getColumn("KUCSL").setWidth(80);
		((NumberField) egu.getColumn("KUCSL").editor).setDecimalPrecision(6);

		egu.getColumn("KUCRL").setCenterHeader("库存热值<br>(Kcal/Kg)");
		egu.getColumn("KUCRL").setWidth(80);
		((NumberField) egu.getColumn("KUCRL").editor).setDecimalPrecision(0);
		
		egu.getColumn("KUCBMDJ").setCenterHeader("库存标煤单价<br>(元/吨)");
		egu.getColumn("KUCBMDJ").setWidth(80);
		((NumberField) egu.getColumn("KUCBMDJ").editor).setDecimalPrecision(2);
		
		egu.getColumn("XIAZLMYC").setCenterHeader("下周来煤预测<br>(万吨)");
		egu.getColumn("XIAZLMYC").setWidth(80);
		((NumberField) egu.getColumn("XIAZLMYC").editor).setDecimalPrecision(2);
		
		egu.getColumn("BEIZ").setHeader("备注");
		egu.getColumn("BEIZ").setWidth(120);
		
		egu.getColumn("ZRSL").setHeader("昨日库存数量");
		egu.getColumn("ZRSL").setEditor(null);
		egu.getColumn("ZRSL").setHidden(true);
		egu.getColumn("ZRSL").update=false;
		
		egu.getColumn("ZRRL").setHeader("昨日库存热量");
		egu.getColumn("ZRRL").setEditor(null);
		egu.getColumn("ZRRL").setHidden(true);
		egu.getColumn("ZRRL").update=false;
		
		egu.getColumn("ZRDJ").setHeader("昨日库存单价");
		egu.getColumn("ZRDJ").setEditor(null);
		egu.getColumn("ZRDJ").setHidden(true);
		egu.getColumn("ZRDJ").update=false;
		
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
		
		int ButtonZhuangt=getRecZhuangt(con);
//		生成按钮
		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
			
//		删除按钮
		GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);

//		保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		
//		提交按钮
		GridButton gbsubmit = new GridButton("提交","function(){document.getElementById('TijButton').click();}");
		gbsubmit.setIcon(SysConstant.Btn_Icon_SelSubmit);
			
//		如果无数据则显示生成按钮
		if (ButtonZhuangt == 0) {
			egu.addTbarBtn(gbc);
		} else {
//			如果数据状态不为1，则显示删除和保存按钮
			if (ButtonZhuangt != 1) {
				egu.addTbarBtn(gbd);
				egu.addTbarBtn(gbs);
				egu.addTbarBtn(gbsubmit);
			}
		}

//		grid 计算方法
		egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);");
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖")
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
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString1("");
//			setChangbModels();
			getSelectData();
		}
	}
	
	public String getTreeid() {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getString1() == null || visit.getString1().equals("")) {
			visit.setString1(String.valueOf((visit.getDiancxxb_id())));
		}
		return visit.getString1();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString1(treeid);
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
