package com.zhiren.dc.monthReport.gd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Yuebmdj extends BasePage {
	
	private String _msg;
	
	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	public boolean getRaw() {
		return true;
	}
	
	// 报表初始设置
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 年份下拉框
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
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2008; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份下拉框
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
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
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
	

	public long getDiancxxbId(){	
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	
	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "查询",
			"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	// submit
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		this.getSelectData();
	}

	// 报表主体
	public String getPrintTable() {	
		JDBCcon cn = new JDBCcon();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" 年 "+getYuefValue().getValue()+" 月";

		String sql=
			"SELECT\n" +
			"DCMC,\n" + 
			"RLBMDJXYJH,\n" + 
			"BYWC,\n" + 
			"RLBMDJBYJH,\n" + 
			"BYWC-RLBMDJBYJH BJHZJ,\n" + 
			"BYWC-SYWC HBZJ,\n" + 
			"BYWC-SNWC TBZJ,\n" + 
			"RLBMDJBNJH,\n" + 
			"LJWC,\n" + 
			"LJWC-RLBMDJBNJH BJHZJN,\n" + 
			"LJWC-SNLJWC TBZJN,\n" + 
			"--DECODE(RLBMLBNJH,0,'0',DECODE(TRUNC(ROUND((LJWC-SNLJWC)/SNLJWC*100,2)),0, REPLACE(ROUND((LJWC-SNLJWC)/SNLJWC*100,2), '.', '0.')||'%',ROUND((LJWC-SNLJWC)/SNLJWC*100,2)||'%'))TBZJL,\n" + 
			"SYWC,\n" + 
			"SNWC,\n" + 
			"SNLJWC\n" + 
			"FROM(\n" + 
			"SELECT DECODE(GROUPING(ZB.DIANCXXB_ID),1,NVL('直管口径1(燃料)',''))DCMC,\n" + 
			"       -2 XUH,\n" + 
			"       --RLBMLBYJH,RLBMDJBYJH, RLBMLXYJH,RLBMDJXYJH, RLBMLBNJH,RLBMDJBNJH\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMLXYJH,0),0,0,ROUND(NVL(JH.RLBMLXYJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLXYJH,0),0)*NVL(JH.RLBMLXYJH,0))/SUM(DECODE(NVL(JH.RLBMLXYJH,0),0,0,ROUND(NVL(JH.RLBMLXYJH,0),0)))),2) RLBMDJXYJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.BYWCDJ,0,0,ROUND(ZB.BYWCML,0))),0,0, SUM(ROUND(ZB.BYWCML,0)*ZB.BYWCDJ)/SUM(DECODE(ZB.BYWCDJ,0,0,ROUND(ZB.BYWCML,0)))),2) BYWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMDJBYJH,0),0,0,ROUND(NVL(JH.RLBMLBYJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLBYJH,0),0)*NVL(JH.RLBMDJBYJH,0))/SUM(DECODE(NVL(JH.RLBMDJBYJH,0),0,0,ROUND(NVL(JH.RLBMLBYJH,0),0)))),2) RLBMDJBYJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMDJBNJH,0),0,0,ROUND(NVL(JH.RLBMLBNJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLBNJH,0),0)*NVL(JH.RLBMDJBNJH,0))/SUM(DECODE(NVL(JH.RLBMDJBNJH,0),0,0,ROUND(NVL(JH.RLBMLBNJH,0),0)))),2) RLBMDJBNJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.LJWCDJ,0,0,ROUND(ZB.LJWCML,0))),0,0, SUM(ROUND(ZB.LJWCML,0)*ZB.LJWCDJ)/SUM(DECODE(ZB.LJWCDJ,0,0,ROUND(ZB.LJWCML,0)))),2) LJWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SYWCDJ,0,0,ROUND(ZB.SYWCML,0))),0,0, SUM(ROUND(ZB.SYWCML,0)*ZB.SYWCDJ)/SUM(DECODE(ZB.SYWCDJ,0,0,ROUND(ZB.SYWCML,0)))),2) SYWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SNWCDJ,0,0,ROUND(ZB.SNWCML,0))),0,0, SUM(ROUND(ZB.SNWCML,0)*ZB.SNWCDJ)/SUM(DECODE(ZB.SNWCDJ,0,0,ROUND(ZB.SNWCML,0)))),2) SNWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SNLJWCDJ,0,0,ROUND(ZB.SNLJWCML,0))),0,0, SUM(ROUND(ZB.SNLJWCML,0)*ZB.SNLJWCDJ)/SUM(DECODE(ZB.SNLJWCDJ,0,0,ROUND(ZB.SNLJWCML,0)))),2) SNLJWC\n" + 
			"  FROM (SELECT DIANCXXB_ID,\n" + 
			"               SUM(DECODE(FENX, NVL('本月', ''), RULBML)) BYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('本月', ''), RULZHBMDJ)) BYWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('累计', ''), RULBML)) LJWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('累计', ''), RULZHBMDJ)) LJWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上月', ''), RULBML)) SYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上月', ''), RULZHBMDJ)) SYWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上年本月', ''), RULBML)) SNWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上年本月', ''), RULZHBMDJ)) SNWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上年累计', ''), RULBML)) SNLJWCML,\n" + 
			"                SUM(DECODE(FENX, NVL('上年累计', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
			"          FROM (SELECT DIANCXXB_ID,\n" + 
			"                       FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID NOT IN (215)\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上月', '') FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID NOT IN (215)\n" + 
			"                  AND FENX='本月'\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上年', '') || FENX FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12) AND DIANCXXB_ID NOT IN (215))\n" + 
			"         GROUP BY DIANCXXB_ID) ZB,\n" + 
			"       (SELECT DIANCXXB_ID, RLBMLBYJH,RLBMDJBYJH, RLBMLXYJH,RLBMDJXYJH, RLBMLBNJH,RLBMDJBNJH\n" + 
			"          FROM YUEBQTSJB\n" + 
			"         WHERE RIQ = DATE '"+strDate+"') JH\n" + 
			" WHERE ZB.DIANCXXB_ID = JH.DIANCXXB_ID(+)\n" + 
			" GROUP BY ROLLUP(ZB.DIANCXXB_ID)\n" + 
			" HAVING GROUPING(ZB.DIANCXXB_ID)=1\n" + 
			" UNION ALL\n" + 
			" SELECT DECODE(GROUPING(ZB.DIANCXXB_ID),1,NVL('直管口径2(计划)',''))DCMC,\n" + 
			"       -1 XUH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMLXYJH,0),0,0,ROUND(NVL(JH.RLBMLXYJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLXYJH,0),0)*NVL(JH.RLBMLXYJH,0))/SUM(DECODE(NVL(JH.RLBMLXYJH,0),0,0,ROUND(NVL(JH.RLBMLXYJH,0),0)))),2) RLBMDJXYJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.BYWCDJ,0,0,ROUND(ZB.BYWCML,0))),0,0, SUM(ROUND(ZB.BYWCML,0)*ZB.BYWCDJ)/SUM(DECODE(ZB.BYWCDJ,0,0,ROUND(ZB.BYWCML,0)))),2) BYWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMDJBYJH,0),0,0,ROUND(NVL(JH.RLBMLBYJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLBYJH,0),0)*NVL(JH.RLBMDJBYJH,0))/SUM(DECODE(NVL(JH.RLBMDJBYJH,0),0,0,ROUND(NVL(JH.RLBMLBYJH,0),0)))),2) RLBMDJBYJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMDJBNJH,0),0,0,ROUND(NVL(JH.RLBMLBNJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLBNJH,0),0)*NVL(JH.RLBMDJBNJH,0))/SUM(DECODE(NVL(JH.RLBMDJBNJH,0),0,0,ROUND(NVL(JH.RLBMLBNJH,0),0)))),2) RLBMDJBNJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.LJWCDJ,0,0,ROUND(ZB.LJWCML,0))),0,0, SUM(ROUND(ZB.LJWCML,0)*ZB.LJWCDJ)/SUM(DECODE(ZB.LJWCDJ,0,0,ROUND(ZB.LJWCML,0)))),2) LJWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SYWCDJ,0,0,ROUND(ZB.SYWCML,0))),0,0, SUM(ROUND(ZB.SYWCML,0)*ZB.SYWCDJ)/SUM(DECODE(ZB.SYWCDJ,0,0,ROUND(ZB.SYWCML,0)))),2) SYWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SNWCDJ,0,0,ROUND(ZB.SNWCML,0))),0,0, SUM(ROUND(ZB.SNWCML,0)*ZB.SNWCDJ)/SUM(DECODE(ZB.SNWCDJ,0,0,ROUND(ZB.SNWCML,0)))),2) SNWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SNLJWCDJ,0,0,ROUND(ZB.SNLJWCML,0))),0,0, SUM(ROUND(ZB.SNLJWCML,0)*ZB.SNLJWCDJ)/SUM(DECODE(ZB.SNLJWCDJ,0,0,ROUND(ZB.SNLJWCML,0)))),2) SNLJWC\n" + 
			"  FROM (SELECT DIANCXXB_ID,\n" + 
			"               SUM(DECODE(FENX, NVL('本月', ''), RULBML)) BYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('本月', ''), RULZHBMDJ)) BYWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('累计', ''), RULBML)) LJWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('累计', ''), RULZHBMDJ)) LJWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上月', ''), RULBML)) SYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上月', ''), RULZHBMDJ)) SYWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上年本月', ''), RULBML)) SNWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上年本月', ''), RULZHBMDJ)) SNWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上年累计', ''), RULBML)) SNLJWCML,\n" + 
			"                SUM(DECODE(FENX, NVL('上年累计', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
			"          FROM (SELECT DIANCXXB_ID,\n" + 
			"                       FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID NOT IN (215,391)\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上月', '') FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID NOT IN (215,391)\n" + 
			"                  AND FENX='本月'\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上年', '') || FENX FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12) AND DIANCXXB_ID NOT IN (215,391))\n" + 
			"         GROUP BY DIANCXXB_ID) ZB,\n" + 
			"       (SELECT DIANCXXB_ID, RLBMLBYJH,RLBMDJBYJH, RLBMLXYJH,RLBMDJXYJH, RLBMLBNJH,RLBMDJBNJH\n" + 
			"          FROM YUEBQTSJB\n" + 
			"         WHERE RIQ = DATE '"+strDate+"') JH\n" + 
			" WHERE ZB.DIANCXXB_ID = JH.DIANCXXB_ID(+)\n" + 
			" GROUP BY ROLLUP(ZB.DIANCXXB_ID)\n" + 
			" HAVING GROUPING(ZB.DIANCXXB_ID)=1\n" + 
			" UNION ALL\n" + 
			"  SELECT DC.MINGC DCMC,\n" + 
			"       DC.XUH XUH,\n" + 
			"       NVL(JH.RLBMDJXYJH,0)RLBMDJXYJH,\n" + 
			"       ZB.BYWCDJ BYWC,\n" + 
			"       NVL(JH.RLBMDJBYJH,0) RLBMDJBYJH,\n" + 
			"       NVL(JH.RLBMDJBNJH,0) RLBMDJBNJH,\n" + 
			"       ZB.LJWCDJ LJWC,\n" + 
			"       ZB.SYWCDJ SYWC,\n" + 
			"       ZB.SNWCDJ SNWC,\n" + 
			"       ZB.SNLJWCDJ SNLJWC\n" + 
			"  FROM (SELECT DIANCXXB_ID,\n" + 
			"               --SUM(DECODE(FENX, NVL('本月', ''), RULBML)) BYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('本月', ''), RULZHBMDJ)) BYWCDJ,\n" + 
			"               --SUM(DECODE(FENX, NVL('累计', ''), RULBML)) LJWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('累计', ''), RULZHBMDJ)) LJWCDJ,\n" + 
			"               --SUM(DECODE(FENX, NVL('上月', ''), RULBML)) SYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上月', ''), RULZHBMDJ)) SYWCDJ,\n" + 
			"               --SUM(DECODE(FENX, NVL('上年本月', ''), RULBML)) SNWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上年本月', ''), RULZHBMDJ)) SNWCDJ,\n" + 
			"               --SUM(DECODE(FENX, NVL('上年累计', ''), RULBML)) SNLJWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上年累计', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
			"          FROM (SELECT DIANCXXB_ID,\n" + 
			"                       FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID NOT IN (215,391,476)\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上月', '') FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID NOT IN (215,391,476)\n" + 
			"                  AND FENX='本月'\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上年', '') || FENX FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12) AND DIANCXXB_ID NOT IN (215,391,476))\n" + 
			"         GROUP BY DIANCXXB_ID) ZB,\n" + 
			"       (SELECT DIANCXXB_ID,RLBMLBYJH,RLBMDJBYJH, RLBMLXYJH,RLBMDJXYJH, RLBMLBNJH,RLBMDJBNJH\n" + 
			"          FROM YUEBQTSJB\n" + 
			"         WHERE RIQ = DATE '"+strDate+"') JH,DIANCXXB DC\n" + 
			" WHERE ZB.DIANCXXB_ID = JH.DIANCXXB_ID(+)\n" + 
			" AND DC.ID=ZB.DIANCXXB_ID\n" + 
			"UNION ALL\n" + 
			"SELECT\n" + 
			"SR.DCMC,\n" + 
			"SR.XUH+ROWNUM XUH,\n" + 
			"SR.RLBMDJXYJH,\n" + 
			"SR.BYWC,\n" + 
			"SR.RLBMDJBYJH,\n" + 
			"SR.RLBMDJBNJH,\n" + 
			"SR.LJWC,\n" + 
			"SR.SYWC,\n" + 
			"SR.SNWC,\n" + 
			"SR.SNLJWC\n" + 
			"FROM(SELECT DECODE(GROUPING(DC.MINGC),1,NVL('英力特小计',''),'    '||DC.MINGC)DCMC,\n" + 
			"       (SELECT MAX(XUH) FROM DIANCXXB) XUH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMLXYJH,0),0,0,ROUND(NVL(JH.RLBMLXYJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLXYJH,0),0)*NVL(JH.RLBMLXYJH,0))/SUM(DECODE(NVL(JH.RLBMLXYJH,0),0,0,ROUND(NVL(JH.RLBMLXYJH,0),0)))),2) RLBMDJXYJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.BYWCDJ,0,0,ROUND(ZB.BYWCML,0))),0,0, SUM(ROUND(ZB.BYWCML,0)*ZB.BYWCDJ)/SUM(DECODE(ZB.BYWCDJ,0,0,ROUND(ZB.BYWCML,0)))),2) BYWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMDJBYJH,0),0,0,ROUND(NVL(JH.RLBMLBYJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLBYJH,0),0)*NVL(JH.RLBMDJBYJH,0))/SUM(DECODE(NVL(JH.RLBMDJBYJH,0),0,0,ROUND(NVL(JH.RLBMLBYJH,0),0)))),2) RLBMDJBYJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(NVL(JH.RLBMDJBNJH,0),0,0,ROUND(NVL(JH.RLBMLBNJH,0),0))),0,0, SUM(ROUND(NVL(JH.RLBMLBNJH,0),0)*NVL(JH.RLBMDJBNJH,0))/SUM(DECODE(NVL(JH.RLBMDJBNJH,0),0,0,ROUND(NVL(JH.RLBMLBNJH,0),0)))),2) RLBMDJBNJH,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.LJWCDJ,0,0,ROUND(ZB.LJWCML,0))),0,0, SUM(ROUND(ZB.LJWCML,0)*ZB.LJWCDJ)/SUM(DECODE(ZB.LJWCDJ,0,0,ROUND(ZB.LJWCML,0)))),2) LJWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SYWCDJ,0,0,ROUND(ZB.SYWCML,0))),0,0, SUM(ROUND(ZB.SYWCML,0)*ZB.SYWCDJ)/SUM(DECODE(ZB.SYWCDJ,0,0,ROUND(ZB.SYWCML,0)))),2) SYWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SNWCDJ,0,0,ROUND(ZB.SNWCML,0))),0,0, SUM(ROUND(ZB.SNWCML,0)*ZB.SNWCDJ)/SUM(DECODE(ZB.SNWCDJ,0,0,ROUND(ZB.SNWCML,0)))),2) SNWC,\n" + 
			"       ROUND(DECODE(SUM(DECODE(ZB.SNLJWCDJ,0,0,ROUND(ZB.SNLJWCML,0))),0,0, SUM(ROUND(ZB.SNLJWCML,0)*ZB.SNLJWCDJ)/SUM(DECODE(ZB.SNLJWCDJ,0,0,ROUND(ZB.SNLJWCML,0)))),2) SNLJWC\n" + 
			"  FROM (SELECT DIANCXXB_ID,\n" + 
			"               SUM(DECODE(FENX, NVL('本月', ''), RULBML)) BYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('本月', ''), RULZHBMDJ)) BYWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('累计', ''), RULBML)) LJWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('累计', ''), RULZHBMDJ)) LJWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上月', ''), RULBML)) SYWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上月', ''), RULZHBMDJ)) SYWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上年本月', ''), RULBML)) SNWCML,\n" + 
			"               SUM(DECODE(FENX, NVL('上年本月', ''), RULZHBMDJ)) SNWCDJ,\n" + 
			"               SUM(DECODE(FENX, NVL('上年累计', ''), RULBML)) SNLJWCML,\n" + 
			"                SUM(DECODE(FENX, NVL('上年累计', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
			"          FROM (SELECT DIANCXXB_ID,\n" + 
			"                       FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID IN (391,476)\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上月', '') FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID IN (391,476)\n" + 
			"                 AND FENX='本月'\n" + 
			"                UNION ALL\n" + 
			"                SELECT DIANCXXB_ID,\n" + 
			"                       NVL('上年', '') || FENX FENX,\n" + 
			"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
			"                       RULZHBMDJ\n" + 
			"                  FROM YUEZBB\n" + 
			"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12) AND DIANCXXB_ID IN (391,476))\n" + 
			"         GROUP BY DIANCXXB_ID) ZB,\n" + 
			"       (SELECT DIANCXXB_ID,RLBMLBYJH,RLBMDJBYJH, RLBMLXYJH,RLBMDJXYJH, RLBMLBNJH,RLBMDJBNJH\n" + 
			"          FROM YUEBQTSJB\n" + 
			"         WHERE RIQ = DATE '"+strDate+"') JH,DIANCXXB DC\n" + 
			" WHERE ZB.DIANCXXB_ID = JH.DIANCXXB_ID(+)\n" + 
			" AND DC.ID=ZB.DIANCXXB_ID\n" + 
			" GROUP BY ROLLUP((DC.MINGC,DC.XUH))\n" + 
			" ORDER BY GROUPING(DC.XUH)DESC,DC.XUH,DC.MINGC)SR)\n" + 
			" ORDER BY XUH";

		ResultSetList rs=cn.getResultSetList(sql.toString());
//		System.out.print(sql.toString());
		Report rt=new Report();
		//定义表头数据
		 String ArrHeader[][]=new String[2][15];

		 ArrHeader[0]=new String[] {"单位","下月<br>计划","月度计划完成情况","月度计划完成情况","月度计划完成情况",
				 					"月度计划完成情况","月度计划完成情况","年度计划完成情况","年度计划完成情况","年度计划完成情况",
				 					"年度计划完成情况","备注","备注","备注"};
		 ArrHeader[1]=new String[] {"单位","下月<br>计划","本月<br>完成","本月<br>计划","比计划<br>增减",
					"环比<br>增减","同比<br>增减","本年<br>计划","累计<br>完成","比计划<br>增减",
					"同比<br>增减","上月<br>完成","上年<br>同期","上年同期<br>累计"};
		 
		 int ArrWidth[]=new int[] {120,80,80,80,80,80,80,80,80,80,80,80,80,80};
		 
		 rt.setTitle(strMonth+"入炉标煤单价-指标计划与完成情况表", ArrWidth);
		 rt.setDefaultTitle(1,3,"单位：组合电厂",Table.ALIGN_LEFT);
		 rt.setDefaultTitle(12, 3, "单位：元/吨", Table.ALIGN_RIGHT);
		 
//		数据
		rt.setBody(new Table(rs,2,0,1));
		rt.body.setWidth(ArrWidth);

		rt.body.setHeaderData(ArrHeader);//表头数据
//		rt.body.mergeCol(1);
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		rt.body.setColAlign(12, Table.ALIGN_CENTER);
		rt.body.setColAlign(13, Table.ALIGN_CENTER);
		rt.body.setColAlign(14, Table.ALIGN_CENTER);

		rt.body.setRowHeight(1, 30);
		rt.body.setRowHeight(2, 50);
		
		rt.getPages();
		rt.body.ShowZero=true;//reportShowZero();
		_CurrentPage=1;
		_AllPages=rt.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();		
	}
	
	private void init(){
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel1(null);
		visit.setDropDownBean1(null);
		visit.setProSelectionModel2(null);
		visit.setDropDownBean2(null);
		getSelectData();
	}
	
	// 页面初始设置
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
}