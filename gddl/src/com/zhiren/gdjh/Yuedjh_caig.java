package com.zhiren.gdjh;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*
 * 作者：夏峥
 * 时间：2012-10-18
 * 描述：新增复制计划按钮（如果界面中没有数据则显示该按钮，否则不显示）
 * 		增加计划口径列
 * 		 增加总厂不能填报的限制。
 */
/*
 * 作者：夏峥
 * 时间：2012-11-22
 * 描述：调整入炉综合标煤单价计算公式
 */
/*
 * 作者：夏峥
 * 时间：2012-12-15
 * 描述：下拉框采用模糊查询
 */

public  class Yuedjh_caig extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		ResultSetList rsl;
		rsl=getExtGrid().getModifyResultSet(getChange());
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		
		while (rsl.next()){
			double JIH_LIUF=rsl.getDouble("JIH_LIUF");
			double JIH_HFF=rsl.getDouble("JIH_HFF");
			double JIH_SL=rsl.getDouble("JIH_SL");
			double JIH_REZ=rsl.getDouble("JIH_REZ");
			double JIH_MEIJ=rsl.getDouble("JIH_MEIJ");
			double JIH_MEIJBHS=rsl.getDouble("JIH_MEIJBHS");
			double JIH_YUNJ=rsl.getDouble("JIH_YUNJ");
			double JIH_YUNJBHS=rsl.getDouble("JIH_YUNJBHS");
			double JIH_ZAF=rsl.getDouble("JIH_ZAF");
			double JIH_ZAFBHS=rsl.getDouble("JIH_ZAFBHS");
			double JIH_DAOCJ=rsl.getDouble("JIH_DAOCJ");
			double JIH_DAOCJBHS=rsl.getDouble("JIH_DAOCJBHS");
			double JIH_DAOCBMDJ=rsl.getDouble("JIH_DAOCBMDJ");
			double JIH_DAOCBMDJBHS=rsl.getDouble("JIH_DAOCBMDJBHS");
			double ZHUANGT=rsl.getDouble("ZHUANGT");
			String  DIANCXXB_ID=rsl.getString("DIANCXXB_ID");

			long GONGYSB_ID=(getExtGrid().getColumn("GONGYSB_ID").combo).getBeanId(rsl.getString("GONGYSB_ID"));
			long MEIKXXB_ID=(getExtGrid().getColumn("MEIKXXB_ID").combo).getBeanId(rsl.getString("MEIKXXB_ID"));
			long PINZB_ID=(getExtGrid().getColumn("PINZB_ID").combo).getBeanId(rsl.getString("PINZB_ID"));
			long FAZ_ID=(getExtGrid().getColumn("FAZ_ID").combo).getBeanId(rsl.getString("FAZ_ID"));
			long JIHKJB_ID=(getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(rsl.getString("JIHKJB_ID"));
			
			if("0".equals(rsl.getString("ID"))){	
				String insertsql="INSERT INTO YUEDJH_CAIG(ID,DIANCXXB_ID,RIQ,GONGYSB_ID,MEIKXXB_ID,PINZB_ID,FAZ_ID,JIH_SL,\n" +
								"JIH_REZ,JIH_LIUF,JIH_HFF, JIH_MEIJ, JIH_MEIJBHS, JIH_YUNJ, JIH_YUNJBHS,\n" + 
								"JIH_ZAF,JIH_ZAFBHS,JIH_DAOCJ,JIH_DAOCJBHS,JIH_DAOCBMDJ,JIH_DAOCBMDJBHS,ZHUANGT,JIHKJB_ID)\n" + 
								"VALUES\n" + 
								"   (getnewid("+DIANCXXB_ID+"),\n" + 
								"   "+DIANCXXB_ID+",\n" + 
								"  "+"to_date('"+strDate+"','yyyy-mm-dd'),\n" + 
								"   "+GONGYSB_ID+",\n" + 
								"    "+MEIKXXB_ID+",\n" + 
								"    "+PINZB_ID+",\n" + 
								"    "+FAZ_ID+",\n" + 
								"    "+JIH_SL+",\n" + 
								"    "+JIH_REZ+",\n" + 
								"    "+JIH_LIUF+",\n" + 
								"    "+JIH_HFF+",\n" + 
								"    "+JIH_MEIJ+",\n" + 
								"    "+JIH_MEIJBHS+",\n" + 
								"    "+JIH_YUNJ+",\n" + 
								"    "+JIH_YUNJBHS+",\n" + 
								"    "+JIH_ZAF+",\n" + 
								"    "+JIH_ZAFBHS+",\n" + 
								"    "+JIH_DAOCJ+",\n" + 
								"    "+JIH_DAOCJBHS+",\n" + 
								"    "+JIH_DAOCBMDJ+",\n" + 
								"    "+JIH_DAOCBMDJBHS+",\n" + 
								"    "+ZHUANGT+","+JIHKJB_ID+")";
				con.getInsert(insertsql);
			}else{
				String updatesql="UPDATE YUEDJH_CAIG\n" + 
								"   SET    GONGYSB_ID      =  "+GONGYSB_ID+",\n" + 
								"       MEIKXXB_ID      =  "+MEIKXXB_ID+",\n" + 
								"       JIHKJB_ID       = "+JIHKJB_ID+",\n" + 
								"       PINZB_ID        =  "+PINZB_ID+",\n" + 
								"       FAZ_ID          =  "+FAZ_ID+",\n" + 
								"       JIH_SL          =  "+JIH_SL+",\n" + 
								"       JIH_REZ         =  "+JIH_REZ+",\n" + 
								"       JIH_LIUF        =  "+JIH_LIUF+",\n" + 
								"       JIH_HFF         =  "+JIH_HFF+",\n" + 
								"       JIH_MEIJ        =  "+JIH_MEIJ+",\n" + 
								"       JIH_MEIJBHS     =  "+JIH_MEIJBHS+",\n" + 
								"       JIH_YUNJ        =  "+JIH_YUNJ+",\n" + 
								"       JIH_YUNJBHS     =  "+JIH_YUNJBHS+",\n" + 
								"       JIH_ZAF         =  "+JIH_ZAF+",\n" + 
								"       JIH_ZAFBHS      =  "+JIH_ZAFBHS+",\n" + 
								"       JIH_DAOCJ       =  "+JIH_DAOCJ+",\n" + 
								"       JIH_DAOCJBHS    =  "+JIH_DAOCJBHS+",\n" + 
								"       JIH_DAOCBMDJ    =  "+JIH_DAOCBMDJ+",\n" + 
								"       JIH_DAOCBMDJBHS =  "+JIH_DAOCBMDJBHS+",\n" + 
								"       ZHUANGT         =  "+ZHUANGT+"\n" + 
								" WHERE ID = "+rsl.getString("id")+"";
				con.getDelete(updatesql);
			}
		}
//		删除数据
		rsl=getExtGrid().getDeleteResultSet(getChange());
		while (rsl.next()){
			String deletesql =	"DELETE FROM YUEDJH_CAIG  WHERE id ="+rsl.getString("id")+"";
			con.getDelete(deletesql);
		}
// 		每次保存时重新计算指标中的相关参数
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
 		rsl.close();
 		setMsg("保存完成！");
	}
	
//	复制功能
	private void CopyData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		
		String CopySql=
		"INSERT INTO YUEDJH_CAIG\n" +
		"(ID,DIANCXXB_ID,RIQ,GONGYSB_ID,MEIKXXB_ID,PINZB_ID,FAZ_ID,JIH_SL,JIH_REZ,\n" + 
		"JIH_LIUF,JIH_HFF,JIH_MEIJ,JIH_MEIJBHS,JIH_YUNJ,JIH_YUNJBHS,JIH_ZAF,JIH_ZAFBHS,\n" + 
		"JIH_DAOCJ,JIH_DAOCJBHS,JIH_DAOCBMDJ,JIH_DAOCBMDJBHS,ZHUANGT,JIHKJB_ID)\n" + 
		"(SELECT GETNEWID("+this.getTreeid()+"),DIANCXXB_ID,ADD_MONTHS(RIQ, 1) RIQ,GONGYSB_ID,\n" + 
		"MEIKXXB_ID,PINZB_ID,FAZ_ID,JIH_SL,JIH_REZ,JIH_LIUF,JIH_HFF,JIH_MEIJ,\n" + 
		"JIH_MEIJBHS,JIH_YUNJ,JIH_YUNJBHS,JIH_ZAF,JIH_ZAFBHS,JIH_DAOCJ,\n" + 
		"JIH_DAOCJBHS,JIH_DAOCBMDJ,JIH_DAOCBMDJBHS,0 ZHUANGT,JIHKJB_ID\n" + 
		"FROM YUEDJH_CAIG\n" + 
		"WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1)\n" + 
		"AND DIANCXXB_ID = "+this.getTreeid()+")";

		con.getInsert(CopySql);
// 		每次保存时重新计算指标中的相关参数
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
		setMsg("复制操作完成！");
	}	
	
	private int countBMDJ(JDBCcon con, String diancxxb_id, String CurrODate) {
		String upsql = "SELECT ROUND(DECODE((EL3 - EL1), 0, 0, EL2 / (EL3 - EL1) * 29.2712 + EL4),2) RULZHBMDJ,\n" +
		"       ROUND(DECODE(EL3, 0, 0, BIAOMLHJ * 29.2712 / EL3),2) HAOYYML\n" + 
		"  FROM (SELECT RUCRLRZC EL1,\n" + 
		"               DECODE((JIH_SL + SHANGYMKC),0,0,ROUND((JIH_DAOCJBHS * JIH_SL + SHANGYMKCDJ * SHANGYMKC) /(JIH_SL + SHANGYMKC),2)) EL2,\n" + 
		"               DECODE((JIH_SL + SHANGYMKC),0,0,ROUND((JIH_REZ * JIH_SL + SHANGYMKCRZ * SHANGYMKC) /(JIH_SL + SHANGYMKC),2)) EL3,\n" + 
		"               JIAJ EL4,\n" + 
		"               BIAOMLHJ\n" + 
		"          FROM (SELECT NVL(NZB.RUCRLRZC, 0) RUCRLRZC,\n" + 
		"                       NVL(NZB.JIAJ, 0) JIAJ,\n" + 
		"                       NVL(JHCG.JIH_SL, 0) JIH_SL,\n" + 
		"                       NVL(JHCG.JIH_DAOCJBHS, 0) JIH_DAOCJBHS,\n" + 
		"                       NVL(JHCG.JIH_REZ, 0) JIH_REZ,\n" + 
		"                       NVL(YUEZB.SHANGYMKC, 0) SHANGYMKC,\n" + 
		"                       NVL(YUEZB.SHANGYMKCDJ, 0) SHANGYMKCDJ,\n" + 
		"                       NVL(YUEZB.SHANGYMKCRZ, 0) SHANGYMKCRZ,\n" + 
		"                       NVL(YUEZB.BIAOMLHJ, 0) BIAOMLHJ\n" + 
		"                  FROM (SELECT ZB.RUCRLRZC,\n" + 
		"                               ROUND(DECODE(ZB.BIAOMLHJ,0,0,(ZB.QITFY + ZB.RANYL * ZB.RANYDJ) /ZB.BIAOMLHJ),2) JIAJ\n" + 
		"                          FROM NIANDJH_ZHIB ZB\n" + 
		"                         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"                           AND RIQ = TRUNC("+CurrODate+", 'yyyy')) NZB,\n" + 
		"                       (SELECT SUM(CG.JIH_SL) JIH_SL,\n" + 
		"                               ROUND(DECODE(SUM(JIH_SL),0,0,SUM(CG.JIH_SL * CG.JIH_DAOCJBHS) /SUM(JIH_SL)),2) JIH_DAOCJBHS,\n" + 
		"                               ROUND(DECODE(SUM(JIH_SL),0,0,SUM(CG.JIH_SL * CG.JIH_REZ) /SUM(JIH_SL)),2) JIH_REZ\n" + 
		"                          FROM YUEDJH_CAIG CG\n" + 
		"                         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"                           AND RIQ = "+CurrODate+") JHCG,\n" + 
		"                       (SELECT ZB.SHANGYMKC,\n" + 
		"                               ZB.SHANGYMKCDJ,\n" + 
		"                               ZB.SHANGYMKCRZ,\n" + 
		"                               ZB.BIAOMLHJ\n" + 
		"                          FROM YUEDJH_ZHIB ZB\n" + 
		"                         WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
		"                           AND RIQ = "+CurrODate+") YUEZB) LY)";

		ResultSetList rsl = con.getResultSetList(upsql);
		
		String updateSql="";
		if(rsl.next()) {
			double HAOYYML=rsl.getDouble("HAOYYML"); 
			double RLZHBMDJ=rsl.getDouble("RULZHBMDJ"); 
			updateSql = "update YUEDJH_ZHIB set HAOYYML="+HAOYYML+", RLZHBMDJ="+RLZHBMDJ+" where riq = " + CurrODate + " and diancxxb_id=" + diancxxb_id;
		}
		rsl.close();
		if(updateSql.length()>1){
			con.getUpdate(updateSql);
			return 1;
		}else{
			return -1;
		}
	}

	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _Cpyclick = false;

	public void CpyButton(IRequestCycle cycle) {
		_Cpyclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if(_Cpyclick){
			_Cpyclick=false;
			CopyData();
		}
	}
	
	private boolean DataChk(String  CurrentDate, String treeid){
		JDBCcon con = new JDBCcon();
		String sql =	"SELECT DISTINCT ZHUANGT\n" + 
				"  FROM YUEDJH_CAIG where RIQ=TO_DATE('"+CurrentDate+"','yyyy-mm-dd') AND DIANCXXB_ID ="+this.getTreeid()+"";
		ResultSetList rsl = con.getResultSetList(sql);
		boolean status=false;
		while(rsl.next()){
			int Zhuangt=rsl.getInt("ZHUANGT");
			if(Zhuangt==1){
				status=true;
			}else{
				status=false;
			}
		}
		return status;
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 工具栏的年份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
//		String diancxxb_id = this.getTreeid();

		StringBuffer strwhere = new StringBuffer();
		String sql =
			"SELECT YC.ID,\n" +
			"       YC.DIANCXXB_ID,\n" + 
			"       GS.MINGC           AS GONGYSB_ID,\n" + 
			"       MK.MINGC           AS MEIKXXB_ID,\n" + 
			"       J.MINGC            AS JIHKJB_ID,\n" + 
			"       PZ.MINGC           AS PINZB_ID,\n" + 
			"       CZ.MINGC           AS FAZ_ID,\n" + 
			"       YC.JIH_SL,\n" + 
			"       YC.JIH_REZ,\n" + 
			"       YC.JIH_LIUF,\n" + 
			"       YC.JIH_HFF,\n" + 
			"       YC.JIH_MEIJ,\n" + 
			"       YC.JIH_MEIJBHS,\n" + 
			"       YC.JIH_YUNJ,\n" + 
			"       YC.JIH_YUNJBHS,\n" + 
			"       YC.JIH_ZAF,\n" + 
			"       YC.JIH_ZAFBHS,\n" + 
			"       YC.JIH_DAOCJ,\n" + 
			"       YC.JIH_DAOCJBHS,\n" + 
			"       YC.JIH_DAOCBMDJ,\n" + 
			"       YC.JIH_DAOCBMDJBHS,\n" + 
			"       YC.ZHUANGT\n" + 
			"  FROM YUEDJH_CAIG YC,\n" + 
			"       GONGYSB     GS,\n" + 
			"       DIANCXXB    DC,\n" + 
			"       MEIKXXB     MK,\n" + 
			"       PINZB       PZ,\n" + 
			"       CHEZXXB     CZ,\n" + 
			"       JIHKJB      J\n" + 
			" WHERE YC.DIANCXXB_ID = DC.ID\n" + 
			"   AND YC.GONGYSB_ID = GS.ID\n" + 
			"   AND YC.MEIKXXB_ID = MK.ID(+)\n" + 
			"   AND YC.PINZB_ID = PZ.ID(+)\n" + 
			"   AND YC.FAZ_ID = CZ.ID(+)\n" + 
			"   AND YC.JIHKJB_ID = J.ID(+)\n" + 
			"   AND YC.RIQ = DATE '"+intyear+"-"+StrMonth+"-01'\n" + 
			"   AND YC.DIANCXXB_ID = "+this.getTreeid();

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("Yuejh_caig");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.getColumn("ID").setCenterHeader("电厂标识");
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("ID").setWidth(70);
		
		egu.getColumn("DIANCXXB_ID").setCenterHeader("单位");
		egu.getColumn("DIANCXXB_ID").setWidth(70);
		egu.getColumn("DIANCXXB_ID").setDefaultValue(this.getTreeid());	
		egu.getColumn("DIANCXXB_ID").setHidden(true);

		
		egu.getColumn("GONGYSB_ID").setCenterHeader("供应商");
		egu.getColumn("GONGYSB_ID").setWidth(160);
		
		egu.getColumn("MEIKXXB_ID").setCenterHeader("煤矿单位");
		egu.getColumn("MEIKXXB_ID").setWidth(230);
		
		egu.getColumn("JIHKJB_ID").setCenterHeader("计划口径");
		egu.getColumn("JIHKJB_ID").setWidth(80);
		
		egu.getColumn("PINZB_ID").setCenterHeader("品种");
		egu.getColumn("PINZB_ID").setWidth(60); 
		
		egu.getColumn("FAZ_ID").setCenterHeader("发站");
		egu.getColumn("FAZ_ID").setWidth(60);
		
		egu.getColumn("JIH_SL").setCenterHeader("采购量<br>(吨)");
		egu.getColumn("JIH_SL").setWidth(60);
		
		egu.getColumn("JIH_REZ").setCenterHeader("热值<br>(MJ/Kg)");
		egu.getColumn("JIH_REZ").setWidth(60);
		
		egu.getColumn("JIH_LIUF").setCenterHeader("硫分(%)");
		egu.getColumn("JIH_LIUF").setWidth(60);
		
		egu.getColumn("JIH_HFF").setCenterHeader("挥发分<br>(%)");
		egu.getColumn("JIH_HFF").setWidth(60);
		
		egu.getColumn("JIH_MEIJ").setCenterHeader("车板价<br>(元/吨)");
		((NumberField)egu.getColumn("JIH_MEIJ").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_MEIJ").setWidth(60);
		
		egu.getColumn("JIH_MEIJBHS").setCenterHeader("车板价<br>(不含税)<br>(元/吨)");
		((NumberField)egu.getColumn("JIH_MEIJBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_MEIJBHS").setWidth(60);
		egu.getColumn("JIH_MEIJBHS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_MEIJBHS").setEditor(null);
		
		egu.getColumn("JIH_YUNJ").setCenterHeader("运费<br>(元/吨)");
		egu.getColumn("JIH_YUNJ").setWidth(60);
		((NumberField)egu.getColumn("JIH_YUNJ").editor).setDecimalPrecision(2);
	
		egu.getColumn("JIH_YUNJBHS").setCenterHeader("运费<br>(不含税)<br>(元/吨)");
		egu.getColumn("JIH_YUNJBHS").setWidth(60);
		((NumberField)egu.getColumn("JIH_YUNJBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_YUNJBHS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_YUNJBHS").setEditor(null);
		
		egu.getColumn("JIH_ZAF").setCenterHeader("杂费<br>(元/吨)");
		((NumberField)egu.getColumn("JIH_ZAF").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_ZAF").setWidth(60);
		
		egu.getColumn("JIH_ZAFBHS").setCenterHeader("杂费<br>(不含税)<br>(元/吨)");
		((NumberField)egu.getColumn("JIH_ZAFBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_ZAFBHS").setWidth(60);
	
		egu.getColumn("JIH_DAOCJ").setCenterHeader("到厂价<br>(元/吨)");
		egu.getColumn("JIH_DAOCJ").setWidth(60);
		((NumberField)egu.getColumn("JIH_DAOCJ").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_DAOCJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_DAOCJ").setEditor(null);

		egu.getColumn("JIH_DAOCJBHS").setCenterHeader("到厂价<br>(不含税)<br>(元/吨)");
		egu.getColumn("JIH_DAOCJBHS").setWidth(80);
		((NumberField)egu.getColumn("JIH_DAOCJBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_DAOCJBHS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_DAOCJBHS").setEditor(null);
		
		egu.getColumn("JIH_DAOCBMDJ").setCenterHeader("到厂标<br>煤单价<br>(元/吨)");
		egu.getColumn("JIH_DAOCBMDJ").setWidth(80);
		((NumberField)egu.getColumn("JIH_DAOCBMDJ").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_DAOCBMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
    	egu.getColumn("JIH_DAOCBMDJ").setEditor(null);
		
		egu.getColumn("JIH_DAOCBMDJBHS").setCenterHeader("到厂标煤<br>单价(不含税)<br>(元/吨)");
		egu.getColumn("JIH_DAOCBMDJBHS").setWidth(85);
		((NumberField)egu.getColumn("JIH_DAOCBMDJBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_DAOCBMDJBHS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_DAOCBMDJBHS").setEditor(null);
		
		egu.getColumn("ZHUANGT").setCenterHeader("上报状态");
		egu.getColumn("ZHUANGT").setEditor(null);
		egu.getColumn("ZHUANGT").setDefaultValue("0");
		egu.getColumn("ZHUANGT").setHidden(true);
		egu.getColumn("ZHUANGT").setWidth(65);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(23);// 设置分页

		// *************************下拉框*****************************************
		// 设置供应商的下拉框
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		cb_gongysb.setLazyRender(true);
		cb_gongysb.setIsMohcx(true);
		
		String GongysSql = 
				"select distinct g.id,g.mingc from vwdianc dc,gongysdcglb gd,gongysb g\n" +
				"where gd.diancxxb_id=dc.id and gd.gongysb_id=g.id "+strwhere+" and g.leix = 1\n" + 
				"union\n" + 
				"select distinct g.id,g.mingc from yuesbjhb n,gongysb g,vwdianc dc\n" + 
				"where n.gongysb_id=g.id and n.diancxxb_id=dc.id "+strwhere+" and g.leix = 1\n" +
				"union\n" + 
				"select distinct g.id,g.mingc from gongysb g where  g.leix = 1 ";
			egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
					new IDropDownModel(GongysSql));
			egu.getColumn("gongysb_id").setReturnId(true);
			
//			 设置煤矿的下拉框
			ComboBox cb_meik=new ComboBox();
			egu.getColumn("meikxxb_id").setEditor(cb_meik);
			cb_meik.setEditable(true);
			cb_meik.setLazyRender(true);
			cb_meik.setIsMohcx(true);
			String cb_meikSql = "select id, mingc from meikxxb order by mingc";
			egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(cb_meikSql));
			egu.getColumn("meikxxb_id").setReturnId(true);
			
//			 设置计划口径的下拉框
			ComboBox cb_jihkj=new ComboBox();
			egu.getColumn("JIHKJB_ID").setEditor(cb_jihkj);
			cb_jihkj.setEditable(true);
			String JihkjSql = "select id, mingc from jihkjb order by xuh";
			egu.getColumn("JIHKJB_ID").setComboEditor(egu.gridId,new IDropDownModel(JihkjSql));
			
//			设置品种下拉框
			ComboBox cb_pinz=new ComboBox();
			egu.getColumn("pinzb_id").setEditor(cb_pinz);
			cb_pinz.setEditable(true);
//			egu.getColumn("pinzb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
			String pinzSql="select id,mingc from pinzb order by id ";
			egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
			
//			 设置发站下拉框
			ComboBox cb_faz = new ComboBox();
			egu.getColumn("faz_id").setEditor(cb_faz);
			cb_faz.setEditable(true);
			String fazSql = "select distinct c.id,c.mingc from fahb f,vwdianc dc,chezxxb c " +
					" where f.diancxxb_id=dc.id  and f.faz_id=c.id and c.leib='车站' "+strwhere+
					" union\n" +
					" select distinct c.id,c.mingc from yuesbjhb n,vwdianc dc,chezxxb c\n" + 
					" where n.diancxxb_id=dc.id and n.faz_id=c.id " +
					" and n.riq=to_date('" + intyear+ "-01-01','yyyy-mm-dd') "+strwhere+ ""+
	                 "union\n" + 
	                 "select distinct c.id,c.mingc from chezxxb c  ";  
			egu.getColumn("faz_id").setComboEditor(egu.gridId,new IDropDownModel(fazSql));

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		

//		 设定工具栏下拉框自动刷新
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
//		如果登录用户为国电电力且数据已提交，那么国电电力用户可编辑该信息
		if(DataChk(intyear+"-"+StrMonth+"-01",this.getTreeid())){
			if(visit.getDiancxxb_id()==112){
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			}
		}else{
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("计划模块", "分厂别总厂显示按钮", this.getTreeid(), "否").equals("否")){
				
			}else{
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
				
//				如果界面中没有数据则显示复制按钮
				if(!con.getHasIt(sql)){
					String handler="function (){document.getElementById('CpyButton').click();"+
					"Ext.MessageBox.show({msg:'正在处理数据,请稍后...'," +
					"progressText:'处理中...',width:300,wait:true,waitConfig: " +
					"{interval:200},icon:Ext.MessageBox.INFO});}";
					GridButton cpy = new GridButton("复制上月计划", handler);
					cpy.setIcon(SysConstant.Btn_Icon_Copy);
					egu.addTbarBtn(cpy);
				}
			}
		}
		
		egu.setDefaultsortable(false);
//		String BgColor=MainGlobal.getXitxx_item("年计划", "总计小计行颜色", "0", "#E3E3E3");
		//---------------页面js的计算开始------------------------------------------
	StringBuffer sb = new StringBuffer();
	
	String yunjslx = MainGlobal.getXitxx_item("结算", "运费税是否是增值税", this.getTreeid(), "是");
	String yunjzzsl = MainGlobal.getXitxx_item("结算", "运费增值税率", this.getTreeid(), "0.11");	
	String yunfcondition="     var yunfbhs=Round(yunfvar*0.93,2);\n" ;
	if(yunjslx.equals("是")){
		yunfcondition="     var yunfbhs=Round(yunfvar/(1+"+yunjzzsl+"),2);\n" ;
	}	
	
	sb.append("gridDiv_grid.on('afteredit',function(e){");
		sb.append(
				"var chebjvar=parseFloat(e.record.get('JIH_MEIJ')==''?0:e.record.get('JIH_MEIJ'));\n" +
						"      var yunfvar=parseFloat(e.record.get('JIH_YUNJ')==''?0:e.record.get('JIH_YUNJ'));\n" + 
						"      var zafvar=parseFloat(e.record.get('JIH_ZAF')==''?0:e.record.get('JIH_ZAF'));\n" + 
						"      var rez=parseFloat(e.record.get('JIH_REZ')==''?0:e.record.get('JIH_REZ'));\n" + 
						"\n" + 
						"       var chebjbhs=Round(chebjvar/1.17,2);\n" + 
						yunfcondition+ 
						"     var zafbhs=Round(zafvar,2);\n" + 
						"       var daocj=Round(chebjvar+yunfvar+zafvar,2);\n" + 
						"       var daocjbhs=Round(chebjbhs+yunfbhs+zafbhs,2);\n" + 
						"     var biaomdj=Round(Round(daocj*29.2712/rez,2),2);\n" + 
						"     var biaomdjbhs=Round(Round(daocjbhs*29.2712/rez,2),2);\n" + 
						"     if(rez==0){\n" + 
						"       biaomdj=0;\n" + 
						"       biaomdjbhs=0;\n" + 
						"     }\n" + 
						"\n" + 
						"     e.record.set('JIH_MEIJBHS',chebjbhs);\n" + 
						"     e.record.set('JIH_YUNJBHS',yunfbhs);\n" + 
						"     e.record.set('JIH_ZAFBHS',zafbhs);\n" + 
						"     e.record.set('JIH_DAOCJ',daocj);\n" + 
						"     e.record.set('JIH_DAOCJBHS',daocjbhs);\n" + 
						"     e.record.set('JIH_DAOCBMDJ',biaomdj);\n" + 
						"     e.record.set('JIH_DAOCBMDJBHS',biaomdjbhs);");
		
		sb.append("});"
				);
	
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		setExtGrid(egu);
		con.Close();
	}

	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		return con.getHasIt(sql);
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=200 scrollamount=2>" + getTbmsg()+ "</marquee>'");
		}
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
			setTbmsg(null);
		}
			getSelectData();
	}
	
//	 年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 12) {
				_nianf = _nianf + 1;
			}
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2009; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
		public boolean Changeyuef = false;

		private static IPropertySelectionModel _YuefModel;

		public IPropertySelectionModel getYuefModel() {
			if (_YuefModel == null) {
				getYuefModels();
			}
			return _YuefModel;
		}

		private IDropDownBean _YuefValue;

		public IDropDownBean getYuefValue() {
			if (_YuefValue == null) {
				int _yuef = DateUtil.getMonth(new Date());
				if (_yuef == 12) {
					_yuef = 1;
				} else {
					_yuef = _yuef + 1;
				}
				for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
					Object obj = _YuefModel.getOption(i);
					if (_yuef == ((IDropDownBean) obj).getId()) {
						_YuefValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _YuefValue;
		}

		public void setYuefValue(IDropDownBean Value) {
			long id = -2;
			if (_YuefValue != null) {
				id = getYuefValue().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					Changeyuef = true;
				} else {
					Changeyuef = false;
				}
			}
			_YuefValue = Value;

		}

		public IPropertySelectionModel getYuefModels() {
			List listYuef = new ArrayList();
			for (int i = 1; i < 13; i++) {
				listYuef.add(new IDropDownBean(i, String.valueOf(i)));
			}
			_YuefModel = new IDropDownModel(listYuef);
			return _YuefModel;
		}

		public void setYuefModel(IPropertySelectionModel _value) {
			_YuefModel = _value;
		}

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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