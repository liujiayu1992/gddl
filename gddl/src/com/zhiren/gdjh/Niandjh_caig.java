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
 * 时间：2012-10-17
 * 描述：新增计划口径列
 * 		 新增复制计划按钮（如果界面中没有数据则显示该按钮，否则不显示）
 * 		增加总厂不能填报的限制。
 */
/*
 * 作者：夏峥
 * 时间：2012-11-19
 * 适用范围：国电电力及其下属电厂
 * 描述：调整年计划指标中标煤单价和入炉综合标煤单价的计算公式。
 */
/*
 * 作者：夏峥
 * 时间：2012-12-15
 * 描述：下拉框采用模糊查询
 */
/*
 * 作者：赵胜男
 * 时间：2012-12-17
 * 描述：按照需求调整界面名称，隐藏计划煤价（不含税），计划运价（不含税），计划到厂价，计划杂费列，
 *            新增加预计到厂不含税标煤单价列，并实现前台自动计算
 */
/*
 * 作者：夏峥
 * 时间：2012-12-20
 * 描述：修正结算错误
 */
public  class Niandjh_caig extends BasePage implements PageValidateListener {
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
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		return con.getHasIt(sql);
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		ResultSetList rsl;
		rsl=getExtGrid().getModifyResultSet(getChange());
		String strDate=getNianfValue().getValue() +"-01-01";
		
		while (rsl.next()){
			double HET_SL=rsl.getDouble("HET_SL");
			double HET_REZ=rsl.getDouble("HET_REZ");
			double HET_MEIJ=rsl.getDouble("HET_MEIJ");
			double HET_YUNJ=rsl.getDouble("HET_YUNJ");
			double JIH_SL=rsl.getDouble("JIH_SL");
			double JIH_REZ=rsl.getDouble("JIH_REZ");
			double JIH_MEIJ=rsl.getDouble("JIH_MEIJ");
			double JIH_MEIJBHS=rsl.getDouble("JIH_MEIJBHS");
			double JIH_YUNJ=rsl.getDouble("JIH_YUNJ");
			double JIH_YUNJBHS=rsl.getDouble("JIH_YUNJBHS");
			double JIH_ZAF=rsl.getDouble("JIH_ZAF");
			double JIH_ZAFBHS=rsl.getDouble("JIH_ZAFBHS");
			double JIH_QIT=rsl.getDouble("JIH_QIT");
			double JIH_QITBHS=rsl.getDouble("JIH_QITBHS");
			double JIH_DAOCJ=rsl.getDouble("JIH_DAOCJ");
			double JIH_DAOCBMDJ=rsl.getDouble("JIH_DAOCBMDJ");
			double ZHUANGT=rsl.getDouble("ZHUANGT");
			String  DIANCXXB_ID=rsl.getString("DIANCXXB_ID");

			long GONGYSB_ID=(getExtGrid().getColumn("GONGYSB_ID").combo).getBeanId(rsl.getString("GONGYSB_ID"));
			long JIHKJB_ID=(getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(rsl.getString("JIHKJB_ID"));
			
			
			if("0".equals(rsl.getString("ID"))){	
				String insertsql="INSERT INTO NIANDJH_CAIG\n" +
						"(ID,DIANCXXB_ID,RIQ,GONGYSB_ID,HET_SL,HET_REZ,HET_MEIJ,HET_YUNJ,JIH_SL,JIH_REZ,\n" + 
						"JIH_MEIJ,JIH_MEIJBHS,JIH_YUNJ,JIH_YUNJBHS,JIH_ZAF,JIH_ZAFBHS,JIH_QIT,JIH_QITBHS,\n" + 
						"JIH_DAOCJ,JIH_DAOCBMDJ,ZHUANGT,JIHKJB_ID)VALUES\n" + 
						"  (getnewid("+DIANCXXB_ID+"),\n" + 
						"   "+DIANCXXB_ID+",\n" + 
						" "+"to_date('"+strDate+"','yyyy-mm-dd'),\n" + 
						"   "+GONGYSB_ID+",\n" + 
						"   "+HET_SL+",\n" + 
						"   "+HET_REZ+",\n" + 
						"   "+HET_MEIJ+",\n" + 
						"  "+HET_YUNJ+",\n" + 
						"   "+JIH_SL+",\n" + 
						"   "+JIH_REZ+",\n" + 
						"   "+JIH_MEIJ+",\n" + 
						"   "+JIH_MEIJBHS+",\n" + 
						"   "+JIH_YUNJ+",\n" + 
						"   "+JIH_YUNJBHS+",\n" + 
						"  "+JIH_ZAF+",\n" + 
						"  "+JIH_ZAFBHS+",\n" + 
						"  "+JIH_QIT+",\n" + 
						"   "+JIH_QITBHS+",\n" + 
						"   "+JIH_DAOCJ+",\n" + 
						"   "+JIH_DAOCBMDJ+",\n" + 
						"   "+ZHUANGT+","+JIHKJB_ID+")";
				con.getInsert(insertsql);
			}else{
				String updatesql="UPDATE NIANDJH_CAIG\n" +
								"   SET GONGYSB_ID   ="+GONGYSB_ID+",\n" + 
								"       HET_SL       = "+HET_SL+",\n" + 
								"       HET_REZ      = "+HET_REZ+",\n" + 
								"       HET_MEIJ     = "+HET_MEIJ+",\n" + 
								"       HET_YUNJ     = "+HET_YUNJ+",\n" + 
								"       JIH_SL       = "+JIH_SL+",\n" + 
								"       JIH_REZ      = "+JIH_REZ+",\n" + 
								"       JIH_MEIJ     = "+JIH_MEIJ+",\n" + 
								"       JIH_MEIJBHS  ="+JIH_MEIJBHS+",\n" + 
								"       JIH_YUNJ     = "+JIH_YUNJ+",\n" + 
								"       JIH_YUNJBHS  = "+JIH_YUNJBHS+",\n" + 
								"       JIH_ZAF      = "+JIH_ZAF+",\n" + 
								"       JIH_ZAFBHS   = "+JIH_ZAFBHS+",\n" + 
								"       JIH_QIT      = "+JIH_QIT+",\n" + 
								"       JIH_QITBHS   = "+JIH_QITBHS+",\n" + 
								"       JIH_DAOCJ    = "+JIH_DAOCJ+",\n" + 
								"       JIHKJB_ID    = "+JIHKJB_ID+",\n" + 
								"       JIH_DAOCBMDJ = "+JIH_DAOCBMDJ+"\n" + 
								" WHERE ID = "+rsl.getString("id")+"";
				con.getDelete(updatesql);
			}
		}
		
		//删除数据
		rsl=getExtGrid().getDeleteResultSet(getChange());
		while (rsl.next()){
			String deletesql =	"DELETE FROM NIANDJH_CAIG  WHERE id ="+rsl.getString("id")+"";
			con.getDelete(deletesql);
		}
// 		每次保存时重新计算指标中的相关参数
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
 		rsl.close();
		setMsg("保存完成！");
	}

//	每次保存时重新计算指标中的相关参数
	private int countBMDJ(JDBCcon con, String diancxxb_id, String CurrODate) {
		String upsql = 
			"SELECT MEIZBMDJ,\n" +
			"       ZAFJE,\n" + 
			"       ROUND(DECODE(BIAOMLHJ,0,0,(MEIZBML * MEIZBMDJ + RANYL * RANYDJ + ZAFJE) / BIAOMLHJ),2) RULZHBMDJ\n" + 
			"  FROM (SELECT NVL(CGJH.JIH_REZ, 0) JIH_REZ,\n" + 
			"               NVL(CGJH.JIH_DAOCJ, 0) JIH_DAOCJ,\n" + 
			"               NVL(ZAF.ZAFJE, 0) ZAFJE,\n" + 
			"               NVL(ZHIB.RUCRLRZC, 0) RUCRLRZC,\n" + 
			"               NVL(ZHIB.MEIZBML, 0) MEIZBML,\n" + 
			"               NVL(ZHIB.RANYL, 0) RANYL,\n" + 
			"               NVL(ZHIB.RANYDJ, 0) RANYDJ,\n" + 
			"               NVL(ZHIB.BIAOMLHJ, 0) BIAOMLHJ,\n" + 
			"               ROUND(DECODE((NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0)),0,0,NVL(JIH_DAOCJ, 0) * 29.2712 /(NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0))),2) MEIZBMDJ\n" + 
			"          FROM (SELECT SUM(JIH_SL) JIHSL,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * JIH_REZ) / SUM(JIH_SL)) JIH_REZ,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * (JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS+JIH_QITBHS)) / SUM(JIH_SL)) JIH_DAOCJ\n" + 
			"                  FROM NIANDJH_CAIG\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") CGJH,\n" + 
			"               (SELECT SUM(YUCJE) ZAFJE\n" + 
			"                  FROM NIANDJH_ZAF\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND TRUNC(RIQ, 'yyyy') = "+CurrODate+") ZAF,\n" + 
			"               (SELECT RUCRLRZC, MEIZBML, RANYL, RANYDJ, BIAOMLHJ\n" + 
			"                  FROM NIANDJH_ZHIB\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") ZHIB) SJ";
		
		ResultSetList rsl = con.getResultSetList(upsql);
		
		String updateSql="";
		if(rsl.next()) {
			double MEIZBMDJ=rsl.getDouble("MEIZBMDJ"); 
			double QITFY=rsl.getDouble("ZAFJE"); 
			double RULZHBMDJ=rsl.getDouble("RULZHBMDJ"); 
			updateSql = "update niandjh_zhib set MEIZBMDJ="+MEIZBMDJ+", QITFY="+QITFY+", RLZHBMDJ="+RULZHBMDJ+" where riq = " + CurrODate + " and diancxxb_id=" + diancxxb_id;
		}
		rsl.close();
		if(updateSql.length()>1){
			con.getUpdate(updateSql);
			return 1;
		}else{
			return -1;
		}
	}
	
//	复制功能
	private void CopyData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		String strDate=getNianfValue().getValue() +"-01-01";
		
		String CopySql=
		"INSERT INTO NIANDJH_CAIG\n" +
		"(ID,DIANCXXB_ID,RIQ,GONGYSB_ID,HET_SL,HET_REZ,HET_MEIJ,HET_YUNJ,\n" + 
		"JIH_SL,JIH_REZ,JIH_MEIJ,JIH_MEIJBHS,JIH_YUNJ,JIH_YUNJBHS,JIH_ZAF,\n" + 
		"JIH_ZAFBHS,JIH_QIT,JIH_QITBHS,JIH_DAOCJ,JIH_DAOCBMDJ,ZHUANGT,JIHKJB_ID)\n" + 
		"\n" + 
		"(SELECT GETNEWID("+this.getTreeid()+"),DIANCXXB_ID,ADD_MONTHS(RIQ, 12) RIQ,\n" + 
		"GONGYSB_ID,HET_SL,HET_REZ,HET_MEIJ,HET_YUNJ,JIH_SL,JIH_REZ,JIH_MEIJ,\n" + 
		"JIH_MEIJBHS,JIH_YUNJ,JIH_YUNJBHS,JIH_ZAF,JIH_ZAFBHS,JIH_QIT,JIH_QITBHS,\n" + 
		"JIH_DAOCJ,JIH_DAOCBMDJ,0 ZHUANGT,JIHKJB_ID\n" + 
		"FROM NIANDJH_CAIG\n" + 
		"WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n" + 
		"AND DIANCXXB_ID = "+this.getTreeid()+")";
		con.getInsert(CopySql);
// 		每次保存时重新计算指标中的相关参数
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
		setMsg("复制操作完成！");
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
	
	private boolean DataChk(long year, String treeid){
		JDBCcon con = new JDBCcon();
		String sql =	"SELECT DISTINCT ZHUANGT\n" + 
				"  FROM NIANDJH_CAIG where RIQ=TO_DATE('"+year+"-01-01','yyyy-mm-dd') AND DIANCXXB_ID ="+this.getTreeid()+"";
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
	
		String sql =	"SELECT NC.ID,\n" +
						"       NC.DIANCXXB_ID,\n" + 
						"       GS.MINGC AS GONGYSB_ID,\n" + 
						"       J.MINGC AS JIHKJB_ID,\n" + 
						"       NC.HET_SL,\n" + 
						"       NC.HET_REZ,\n" + 
						"       NC.HET_MEIJ,\n" + 
						"       NC.HET_YUNJ,\n" + 
						"       NC.JIH_SL,\n" + 
						"       NC.JIH_REZ,\n" + 
						"       NC.JIH_MEIJ,\n" + 
						"       NC.JIH_MEIJBHS,\n" + 
						"       NC.JIH_YUNJ,\n" + 
						"       NC.JIH_YUNJBHS,\n" + 
						"       NC.JIH_ZAF,\n" + 
						"       NC.JIH_ZAFBHS,\n" + 
						"       NC.JIH_QIT,\n" + 
						"       NC.JIH_QITBHS,\n" + 
						"       NC.JIH_DAOCJ,\n" + 
						"       NC.JIH_DAOCBMDJ,\n" + 
						"     DECODE(NC.JIH_REZ,0,0,ROUND(( ROUND(NC.JIH_MEIJ/1.17,2) +  ROUND(NC.JIH_YUNJ*0.93,2)  + NC.JIH_ZAFBHS + NC.JIH_QITBHS) *29.271 / NC.JIH_REZ,2)) daocbhsbmdj ," + 
						"       NC.ZHUANGT\n" +	
						"  FROM NIANDJH_CAIG NC,GONGYSB GS,JIHKJB J  \n" +
						"	WHERE NC.JIHKJB_ID=J.ID AND NC.GONGYSB_ID = GS.ID  \n" +
						"  		AND TO_CHAR(NC.RIQ, 'yyyy')='"+intyear+"' \n" +
						"  		AND DIANCXXB_ID ="+this.getTreeid()+"";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("Niandjh_caig");
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
		
		egu.getColumn("GONGYSB_ID").setCenterHeader("供货单位");
		egu.getColumn("GONGYSB_ID").setWidth(250);
		
		egu.getColumn("JIHKJB_ID").setCenterHeader("计划口径");
		egu.getColumn("JIHKJB_ID").setWidth(80);
		
		egu.getColumn("HET_SL").setCenterHeader("合同数量<br>(吨)");
		egu.getColumn("HET_SL").setWidth(70);
		
		
		egu.getColumn("HET_REZ").setCenterHeader("合同热值<br>(MJ/Kg)");
		((NumberField)egu.getColumn("HET_REZ").editor).setDecimalPrecision(2);
		egu.getColumn("HET_REZ").setWidth(70);
		
		egu.getColumn("HET_MEIJ").setCenterHeader("合同煤价<br>(元/吨)");
		egu.getColumn("HET_MEIJ").setWidth(70);
		
		egu.getColumn("HET_YUNJ").setCenterHeader("合同运价<br>(元/吨)");
		egu.getColumn("HET_YUNJ").setWidth(70);
		
	
		egu.getColumn("JIH_SL").setCenterHeader("预计到货量<br>(吨)");
		((NumberField)egu.getColumn("JIH_SL").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_SL").setWidth(70);
		
		egu.getColumn("JIH_REZ").setCenterHeader("预计到货<br>热值(MJ/Kg)");
		((NumberField)egu.getColumn("JIH_REZ").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_REZ").setWidth(70);
		
		egu.getColumn("JIH_MEIJ").setCenterHeader("预计到货<br>煤价(元/吨)");
		((NumberField)egu.getColumn("JIH_MEIJ").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_MEIJ").setWidth(70);
		
		egu.getColumn("JIH_MEIJBHS").setCenterHeader("预计到货煤价<br>(不含税)<br>(元/吨)");
		((NumberField)egu.getColumn("JIH_MEIJBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_MEIJBHS").setWidth(70);
		egu.getColumn("JIH_MEIJBHS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_MEIJBHS").setEditor(null);
		egu.getColumn("JIH_MEIJBHS").setHidden(true);
		
		egu.getColumn("JIH_YUNJ").setCenterHeader("预计到货<br>运价(元/吨)");
		egu.getColumn("JIH_YUNJ").setWidth(70);
		((NumberField)egu.getColumn("JIH_YUNJ").editor).setDecimalPrecision(2);
	
		
		egu.getColumn("JIH_YUNJBHS").setCenterHeader("预计到货<br>运价(不含税)<br>(元/吨)");
		egu.getColumn("JIH_YUNJBHS").setWidth(70);
		((NumberField)egu.getColumn("JIH_YUNJBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_YUNJBHS").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_YUNJBHS").setEditor(null);
		egu.getColumn("JIH_YUNJBHS").setHidden(true);
		
		egu.getColumn("JIH_ZAF").setCenterHeader("计划杂费<br>(元/吨)");
		((NumberField)egu.getColumn("JIH_ZAF").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_ZAF").setWidth(70);
		egu.getColumn("JIH_ZAF").setHidden(true);
		
		egu.getColumn("JIH_ZAFBHS").setCenterHeader("计划杂费<br>(不含税)<br>(元/吨)");
		((NumberField)egu.getColumn("JIH_ZAFBHS").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_ZAFBHS").setWidth(70);
		egu.getColumn("JIH_ZAFBHS").setHidden(true);
		
		egu.getColumn("JIH_QIT").setCenterHeader("预计其他费用<br>(元/吨)");
		egu.getColumn("JIH_QIT").setWidth(80);
		
		egu.getColumn("JIH_QITBHS").setCenterHeader("预计其他费<br>用(不含税)<br>(元/吨)");
		egu.getColumn("JIH_QITBHS").setWidth(70);
		((NumberField)egu.getColumn("JIH_QITBHS").editor).setDecimalPrecision(2);
		
		egu.getColumn("JIH_DAOCJ").setCenterHeader("预计到厂价<br>(元/吨)");
		egu.getColumn("JIH_DAOCJ").setWidth(80);
		((NumberField)egu.getColumn("JIH_DAOCJ").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_DAOCJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_DAOCJ").setEditor(null);
		
		egu.getColumn("JIH_DAOCBMDJ").setCenterHeader("预计到厂<br>标煤单价<br>(元/吨)");
		egu.getColumn("JIH_DAOCBMDJ").setWidth(80);
		((NumberField)egu.getColumn("JIH_DAOCBMDJ").editor).setDecimalPrecision(2);
		egu.getColumn("JIH_DAOCBMDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("JIH_DAOCBMDJ").setEditor(null);		
		
		egu.getColumn("daocbhsbmdj").setCenterHeader("预计到厂<br>不含税标<br>煤单价<br>(元)");
		egu.getColumn("daocbhsbmdj").setDefaultValue("0");
		egu.getColumn("daocbhsbmdj").setWidth(80);
		egu.getColumn("daocbhsbmdj").setEditor(null);
		egu.getColumn("daocbhsbmdj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("daocbhsbmdj").setUpdate(false);
		
		egu.getColumn("ZHUANGT").setCenterHeader("上报状态");
		egu.getColumn("ZHUANGT").setEditor(null);
		egu.getColumn("ZHUANGT").setDefaultValue("0");
		egu.getColumn("ZHUANGT").setHidden(true);
		egu.getColumn("ZHUANGT").setWidth(65);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(23);// 设置分页

		// 设置供应商的下拉框
		ComboBox cb_gongys=new ComboBox();
		egu.getColumn("GONGYSB_ID").setEditor(cb_gongys);
		cb_gongys.setEditable(true);
		cb_gongys.setLazyRender(true);
		cb_gongys.setIsMohcx(true);
		String GongysSql = "select id, mingc from gongysb where leix = 1 order by mingc";
		egu.getColumn("GONGYSB_ID").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));

		// 设置计划口径的下拉框
		ComboBox cb_jihkj=new ComboBox();
		egu.getColumn("JIHKJB_ID").setEditor(cb_jihkj);
		cb_jihkj.setEditable(true);
		String JihkjSql = "select id, mingc from jihkjb order by xuh";
		egu.getColumn("JIHKJB_ID").setComboEditor(egu.gridId,new IDropDownModel(JihkjSql));
		
		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		// 设置树
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,
				((Visit) this.getPage().getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		

//		 设定工具栏下拉框自动刷新
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
//		如果登录用户为国电电力且数据已提交，那么国电电力用户可编辑该信息
		if(DataChk(intyear,this.getTreeid())){
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
					GridButton cpy = new GridButton("复制上年度计划", handler);
					cpy.setIcon(SysConstant.Btn_Icon_Copy);
					egu.addTbarBtn(cpy);
				}
			}
		}
		
		egu.setDefaultsortable(true);
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		
		String yunjslx = MainGlobal.getXitxx_item("结算", "运费税是否是增值税", this.getTreeid(), "是");
		String yunjzzsl = MainGlobal.getXitxx_item("结算", "运费增值税率", this.getTreeid(), "0.11");	
		String yunfcondition="var yunfbhs=Round(yunfbhsvar*0.93,2);\n" ;
		if(yunjslx.equals("是")){
			yunfcondition="var yunfbhs=Round(yunfbhsvar/(1+"+yunjzzsl+"),2);\n" ;
		}	
		
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			sb.append(
					"var chebjbhsvar=parseFloat(e.record.get('JIH_MEIJ')==''?0:e.record.get('JIH_MEIJ'));\n" +
							"\t\t\t\t\tvar yunfbhsvar=parseFloat(e.record.get('JIH_YUNJ')==''?0:e.record.get('JIH_YUNJ'));\n" + 
							"\t\t\t\t\tvar zafbhsvar=parseFloat(e.record.get('JIH_ZAF')==''?0:e.record.get('JIH_ZAF'));\n" + 
							"\t\t\t\t\tvar qitbhsvar=parseFloat(e.record.get('JIH_QIT')==''?0:e.record.get('JIH_QIT'));\n" + 
							"\t\t\t\t\tvar rez=parseFloat(e.record.get('JIH_REZ')==''?0:e.record.get('JIH_REZ'));\n" + 
							"\t\t\t\t\tvar chebjbhs=Round(chebjbhsvar/1.17,2);\n" + 
							yunfcondition+
							"\t\t\t\t\tvar zafbhs=Round(zafbhsvar,2);\n" + 
							"\t\t\t\t\tvar qitbhs=Round(qitbhsvar,2);\n" + 
							"\t\t\t\t\tvar daocj=Round(chebjbhsvar+yunfbhsvar+zafbhsvar+qitbhsvar,2);\n" + 
							"     if(rez==0){\n" + 
							"       biaomdj=0;\n" +
							"		daocbhsbmdj=0;\n" + 
							"     }else{\n" +
							"\t\t\t\t\tvar biaomdj=Round(Round(daocj*29.271/rez,2),2);\n" +
							"\t\t\t\t\tvar daocbhsbmdj=Round((chebjbhs+yunfbhs+zafbhs+qitbhs)*29.271/rez,2); \n" +
							"			}\n" + 
							"          e.record.set('JIH_MEIJBHS',chebjbhs);\n" + 
							"          e.record.set('JIH_YUNJBHS',yunfbhs);\n" + 
							"          e.record.set('JIH_ZAFBHS',zafbhs);\n" + 
							"          e.record.set('JIH_QITBHS',qitbhs);\n" + 
							"          e.record.set('JIH_DAOCJ',daocj);\n" + 
							"          e.record.set('JIH_DAOCBMDJ',biaomdj);\n" +
							" e.record.set('DAOCBHSBMDJ',daocbhsbmdj);");			
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		setExtGrid(egu);
		rsl.close();
		con.Close();
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
			int _nianf = DateUtil.getYear(new Date())+1;
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