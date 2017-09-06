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

public class Yuerlf extends BasePage {
	
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
	
	// ������ʼ����
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
	
	// ���������
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

	// �·�������
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
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
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

	// ��������
	public String getPrintTable() {	
		JDBCcon cn = new JDBCcon();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+" �� "+getYuefValue().getValue()+" ��";

		String sql="SELECT\n" +
		"DCMC,\n" + 
		"ROUND(RLBMDJXYJH/10000,0)RLBMDJXYJH,\n" + 
		"ROUND(BYWC/10000,0)BYWC,\n" + 
		"ROUND(RLBMDJBYJH/10000,0)RLBMDJBYJH,\n" + 
		"DECODE(ROUND(RLBMDJBYJH/10000,0),0,'0',DECODE(TRUNC(ROUND((ROUND(BYWC/10000,0)-ROUND(RLBMDJBYJH/10000,0))/ROUND(RLBMDJBYJH/10000,0)*100,2)),0, REPLACE(ROUND((ROUND(BYWC/10000,0)-ROUND(RLBMDJBYJH/10000,0))/ROUND(RLBMDJBYJH/10000,0)*100,2), '.', '0.')||'%',ROUND((ROUND(BYWC/10000,0)-ROUND(RLBMDJBYJH/10000,0))/ROUND(RLBMDJBYJH/10000,0)*100,2)||'%'))BJHZJL,\n" + 
		"\n" + 
		"DECODE(ROUND(SYWC/10000,0),0,'0',DECODE(TRUNC(ROUND((ROUND(BYWC/10000,0)-ROUND(SYWC/10000,0))/ROUND(SYWC/10000,0)*100,2)),0, REPLACE(ROUND((ROUND(BYWC/10000,0)-ROUND(SYWC/10000,0))/ROUND(SYWC/10000,0)*100,2), '.', '0.')||'%',ROUND((ROUND(BYWC/10000,0)-ROUND(SYWC/10000,0))/ROUND(SYWC/10000,0)*100,2)||'%'))BYHBZJL,\n" + 
		"DECODE(ROUND(SNWC/10000,0),0,'0',DECODE(TRUNC(ROUND((ROUND(BYWC/10000,0)-ROUND(SNWC/10000,0))/ROUND(SNWC/10000,0)*100,2)),0, REPLACE(ROUND((ROUND(BYWC/10000,0)-ROUND(SNWC/10000,0))/ROUND(SNWC/10000,0)*100,2), '.', '0.')||'%',ROUND((ROUND(BYWC/10000,0)-ROUND(SNWC/10000,0))/ROUND(SNWC/10000,0)*100,2)||'%'))BYTBZJL,\n" + 
		"ROUND(RLBMDJBNJH/10000,0)RLBMDJBNJH,\n" + 
		"ROUND(LJWC/10000,0)LJWC,\n" + 
		"DECODE(ROUND(RLBMDJBNJH/10000,0),0,'0',DECODE(TRUNC(ROUND(ROUND(LJWC/10000,0)/ROUND(RLBMDJBNJH/10000,0)*100,2)),0, REPLACE(ROUND(ROUND(LJWC/10000,0)/ROUND(RLBMDJBNJH/10000,0)*100,2), '.', '0.')||'%',ROUND(ROUND(LJWC/10000,0)/ROUND(RLBMDJBNJH/10000,0)*100,2)||'%'))NJHWCL,\n" + 
		"DECODE(ROUND(SNLJWC/10000,0),0,'0',DECODE(TRUNC(ROUND((ROUND(LJWC/10000,0)-ROUND(SNLJWC/10000,0))/ROUND(SNLJWC/10000,0)*100,2)),0, REPLACE(ROUND((ROUND(LJWC/10000,0)-ROUND(SNLJWC/10000,0))/ROUND(SNLJWC/10000,0)*100,2), '.', '0.')||'%',ROUND((ROUND(LJWC/10000,0)-ROUND(SNLJWC/10000,0))/ROUND(SNLJWC/10000,0)*100,2)||'%'))LJTBZJL,\n" + 
		"ROUND(SYWC/10000,0)SYWC,\n" + 
		"ROUND(SNWC/10000,0)SNWC,\n" + 
		"ROUND(SNLJWC/10000,0)SNLJWC\n" + 
		"FROM(\n" + 
		"SELECT DECODE(GROUPING(ZB.DIANCXXB_ID),1,NVL('ֱ�ܿھ�1(ȼ��)',''))DCMC,\n" + 
		"       -2 XUH,\n" + 
		"       --RLBMLBYJH,RLBMDJBYJH, RLBMLXYJH,RLBMDJXYJH, RLBMLBNJH,RLBMDJBNJH\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLXYJH,0),0)*NVL(JH.RLBMLXYJH,0)),0) RLBMDJXYJH,\n" + 
		"       ROUND(SUM(ROUND(ZB.BYWCML,0)*ZB.BYWCDJ),0) BYWC,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLBYJH,0),0)*NVL(JH.RLBMDJBYJH,0)),0) RLBMDJBYJH,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLBNJH,0),0)*NVL(JH.RLBMDJBNJH,0)),0) RLBMDJBNJH,\n" + 
		"       ROUND(SUM(ROUND(ZB.LJWCML,0)*ZB.LJWCDJ),0) LJWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SYWCML,0)*ZB.SYWCDJ),0) SYWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SNWCML,0)*ZB.SNWCDJ),0) SNWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SNLJWCML,0)*ZB.SNLJWCDJ),0) SNLJWC\n" + 
		"  FROM (SELECT DIANCXXB_ID,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) BYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) BYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULBML)) LJWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULZHBMDJ)) LJWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) SYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) SYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULBML)) SNWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULZHBMDJ)) SNWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULBML)) SNLJWCML,\n" + 
		"                SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
		"          FROM (SELECT DIANCXXB_ID,\n" + 
		"                       FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID NOT IN (215)\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID NOT IN (215)\n" + 
		"                  AND FENX='����'\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') || FENX FENX,\n" + 
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
		" SELECT DECODE(GROUPING(ZB.DIANCXXB_ID),1,NVL('ֱ�ܿھ�2(�ƻ�)',''))DCMC,\n" + 
		"       -1 XUH,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLXYJH,0),0)*NVL(JH.RLBMLXYJH,0)),0) RLBMDJXYJH,\n" + 
		"       ROUND(SUM(ROUND(ZB.BYWCML,0)*ZB.BYWCDJ),0) BYWC,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLBYJH,0),0)*NVL(JH.RLBMDJBYJH,0)),0) RLBMDJBYJH,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLBNJH,0),0)*NVL(JH.RLBMDJBNJH,0)),0) RLBMDJBNJH,\n" + 
		"       ROUND(SUM(ROUND(ZB.LJWCML,0)*ZB.LJWCDJ),0) LJWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SYWCML,0)*ZB.SYWCDJ),0) SYWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SNWCML,0)*ZB.SNWCDJ),0) SNWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SNLJWCML,0)*ZB.SNLJWCDJ),0) SNLJWC\n" + 
		"  FROM (SELECT DIANCXXB_ID,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) BYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) BYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULBML)) LJWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULZHBMDJ)) LJWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) SYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) SYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULBML)) SNWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULZHBMDJ)) SNWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULBML)) SNLJWCML,\n" + 
		"                SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
		"          FROM (SELECT DIANCXXB_ID,\n" + 
		"                       FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID NOT IN (215,391)\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID NOT IN (215,391)\n" + 
		"                  AND FENX='����'\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') || FENX FENX,\n" + 
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
		"       ROUND((ROUND(NVL(JH.RLBMLXYJH,0),0)*NVL(JH.RLBMLXYJH,0)),0) RLBMDJXYJH,\n" + 
		"       ROUND((ROUND(ZB.BYWCML,0)*ZB.BYWCDJ),0) BYWC,\n" + 
		"       ROUND((ROUND(NVL(JH.RLBMLBYJH,0),0)*NVL(JH.RLBMDJBYJH,0)),0) RLBMDJBYJH,\n" + 
		"       ROUND((ROUND(NVL(JH.RLBMLBNJH,0),0)*NVL(JH.RLBMDJBNJH,0)),0) RLBMDJBNJH,\n" + 
		"       ROUND((ROUND(ZB.LJWCML,0)*ZB.LJWCDJ),0) LJWC,\n" + 
		"       ROUND((ROUND(ZB.SYWCML,0)*ZB.SYWCDJ),0) SYWC,\n" + 
		"       ROUND((ROUND(ZB.SNWCML,0)*ZB.SNWCDJ),0) SNWC,\n" + 
		"       ROUND((ROUND(ZB.SNLJWCML,0)*ZB.SNLJWCDJ),0) SNLJWC\n" + 
		"  FROM (SELECT DIANCXXB_ID,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) BYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) BYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULBML)) LJWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULZHBMDJ)) LJWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) SYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) SYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULBML)) SNWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULZHBMDJ)) SNWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULBML)) SNLJWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
		"          FROM (SELECT DIANCXXB_ID,\n" + 
		"                       FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID NOT IN (215,391,476)\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID NOT IN (215,391,476)\n" + 
		"                  AND FENX='����'\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') || FENX FENX,\n" + 
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
		"FROM(SELECT DECODE(GROUPING(DC.MINGC),1,NVL('Ӣ����С��',''),'    '||DC.MINGC)DCMC,\n" + 
		"       (SELECT MAX(XUH) FROM DIANCXXB) XUH,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLXYJH,0),0)*NVL(JH.RLBMLXYJH,0)),0) RLBMDJXYJH,\n" + 
		"       ROUND(SUM(ROUND(ZB.BYWCML,0)*ZB.BYWCDJ),0) BYWC,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLBYJH,0),0)*NVL(JH.RLBMDJBYJH,0)),0) RLBMDJBYJH,\n" + 
		"       ROUND(SUM(ROUND(NVL(JH.RLBMLBNJH,0),0)*NVL(JH.RLBMDJBNJH,0)),0) RLBMDJBNJH,\n" + 
		"       ROUND(SUM(ROUND(ZB.LJWCML,0)*ZB.LJWCDJ),0) LJWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SYWCML,0)*ZB.SYWCDJ),0) SYWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SNWCML,0)*ZB.SNWCDJ),0) SNWC,\n" + 
		"       ROUND(SUM(ROUND(ZB.SNLJWCML,0)*ZB.SNLJWCDJ),0) SNLJWC\n" + 
		"  FROM (SELECT DIANCXXB_ID,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) BYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) BYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULBML)) LJWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('�ۼ�', ''), RULZHBMDJ)) LJWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULBML)) SYWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('����', ''), RULZHBMDJ)) SYWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULBML)) SNWCML,\n" + 
		"               SUM(DECODE(FENX, NVL('���걾��', ''), RULZHBMDJ)) SNWCDJ,\n" + 
		"               SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULBML)) SNLJWCML,\n" + 
		"                SUM(DECODE(FENX, NVL('�����ۼ�', ''), RULZHBMDJ)) SNLJWCDJ\n" + 
		"          FROM (SELECT DIANCXXB_ID,\n" + 
		"                       FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = DATE '"+strDate+"' AND DIANCXXB_ID IN (391,476)\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') FENX,\n" + 
		"                       RULMZBZML + RULYZBZML + RULQZBZML RULBML,\n" + 
		"                       RULZHBMDJ\n" + 
		"                  FROM YUEZBB\n" + 
		"                 WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -1) AND DIANCXXB_ID IN (391,476)\n" + 
		"                 AND FENX='����'\n" + 
		"                UNION ALL\n" + 
		"                SELECT DIANCXXB_ID,\n" + 
		"                       NVL('����', '') || FENX FENX,\n" + 
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
		//�����ͷ����
		 String ArrHeader[][]=new String[2][15];

		 ArrHeader[0]=new String[] {"��λ","����<br>�ƻ�","�¶ȼƻ�������","�¶ȼƻ�������","�¶ȼƻ�������",
				 					"�¶ȼƻ�������","�¶ȼƻ�������","��ȼƻ�������","��ȼƻ�������","��ȼƻ�������",
				 					"��ȼƻ�������","��ע","��ע","��ע"};
		 ArrHeader[1]=new String[] {"��λ","����<br>�ƻ�","����<br>���","����<br>�ƻ�","�ȼƻ�<br>������",
					"����<br>������","ͬ��<br>������","����<br>�ƻ�","�ۼ�<br>���","��ƻ�<br>�����",
					"ͬ��<br>������","����<br>���","����<br>ͬ��","����ͬ��<br>�ۼ�"};
		 
		 int ArrWidth[]=new int[] {120,80,80,80,80,80,80,80,80,80,80,80,80,80};
		 
		 rt.setTitle(strMonth+"ȼ�Ϸ�-ָ��ƻ�����������", ArrWidth);
		 rt.setDefaultTitle(1,3,"��λ����ϵ糧",Table.ALIGN_LEFT);
		 rt.setDefaultTitle(12, 3, "��λ����Ԫ", Table.ALIGN_RIGHT);
		 
//		����
		rt.setBody(new Table(rs,2,0,1));
		rt.body.setWidth(ArrWidth);

		rt.body.setHeaderData(ArrHeader);//��ͷ����
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
	
	// ҳ���ʼ����
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
}