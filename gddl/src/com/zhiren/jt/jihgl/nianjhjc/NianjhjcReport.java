package com.zhiren.jt.jihgl.nianjhjc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterFac_dt;

public class NianjhjcReport extends BasePage implements PageValidateListener {

	private String _msg;

	private int _CurrentPage = -1;

	private int _AllPages = -1;

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	public boolean getRaw() {
		return true;
	}

	private boolean reportShowZero() {
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	// ***************设置消息框******************//
	// 页面判定方法
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

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// 得到单位全称
	public String getTianzdwQuanc(String dcid) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();
		ResultSetList rs = cn
				.getResultSetList(" select quanc from diancxxb where id="
						+ dcid);
		if (rs.next()) {
			_TianzdwQuanc = rs.getString("quanc");
		}
		rs.close();
		return _TianzdwQuanc;
	}

	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		String Year = getNianfValue().getValue();
		String YUEF = getYuefValue().getValue();
		if (Integer.parseInt(YUEF) < 10) {
			YUEF = "0" + YUEF;
		} else {
			YUEF = getYuefValue().getValue();
		}
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";
		
		

		String sql = 
			"SELECT GONGYSB_ID,FAZ_ID,NIANJH_DUN,NIANJH_CHE,NIANJH_BUHSBMDJ,\n" +
			"      ZHEHMY_DUN,ZHEHMY_CHE,ZHEHMY_BUHSBMDJ,\n" + 
			"      BENY_DUN,BENY_CHE,BENY_BUHSBMDJ,LEIJ_DUN,LEIJ_CHE,LEIJ_BUHSBMDJ,\n" + 
			"      (ZHEHMY_DUN-BENY_DUN) AS WEIWC_DUN,(NIANJH_DUN-LEIJ_DUN) AS WEIWC_LEIJ_DUN,\n" + 
			"      (ZHEHMY_BUHSBMDJ-BENY_BUHSBMDJ) AS WEIWC_BUSBMDJ\n" + 
			"FROM\n" + 
			"(SELECT DECODE(GONGYSB.MINGC,NULL,'总计',GONGYSB.MINGC) AS GONGYSB_ID,\n" + 
			"       DECODE(CHEZXXB.MINGC,NULL,'总计',CHEZXXB.MINGC)  AS FAZ_ID,\n" + 
			"       SUM(JH.NIANJH_DUN) AS NIANJH_DUN,\n" + 
			"       SUM(JH.NIANJH_CHE) AS NIANJH_CHE,\n" + 
			"       ROUND(DECODE(SUM(JH.NIANJH_DUN),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              SUM(JH.NIANJH_BUHSBMDJ * JH.NIANJH_DUN) / SUM(JH.NIANJH_DUN)),2) AS NIANJH_BUHSBMDJ,\n" + 
			"       SUM(JH.ZHEHMY_DUN) AS ZHEHMY_DUN,\n" + 
			"       SUM(JH.ZHEHMY_CHE) AS ZHEHMY_CHE,\n" + 
			"       ROUND(DECODE(SUM(JH.ZHEHMY_DUN),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              SUM(JH.ZHEHMY_BUHSBMDJ * JH.ZHEHMY_DUN) / SUM(JH.ZHEHMY_DUN)),2) AS ZHEHMY_BUHSBMDJ,\n" + 
			"       SUM(SJ.BENY_DUN) AS BENY_DUN,\n" + 
			"       SUM(LJ.BENY_DUN) AS LEIJ_DUN,\n" + 
			"       SUM(SJ.BENY_CHE) AS BENY_CHE,\n" + 
			"       SUM(LJ.BENY_CHE) AS LEIJ_CHE,\n" + 
			"       ROUND(DECODE(SUM(DECODE(SJ.BUHSBMDJ,0,0,SJ.BENY_DUN)),0,0,\n" + 
			"       SUM(SJ.BUHSBMDJ*SJ.BENY_DUN)/SUM(DECODE(SJ.BUHSBMDJ,0,0,SJ.BENY_DUN))\n" + 
			"       ),2) AS BENY_BUHSBMDJ,\n" + 
			"       --\n" + 
			"       ROUND(DECODE(SUM(DECODE(LJ.BUHSBMDJ,0,0,LJ.BENY_DUN)),0,0,\n" + 
			"       SUM(LJ.BUHSBMDJ*LJ.BENY_DUN)/SUM(DECODE(LJ.BUHSBMDJ,0,0,LJ.BENY_DUN))\n" + 
			"       ),2) AS LEIJ_BUHSBMDJ\n" + 
			"  FROM (SELECT SUM(F.LAIMSL) BENY_DUN,\n" + 
			"               SUM(F.CHES) BENY_CHE,\n" + 
			"               F.NIANJHJCB_ID,\n" + 
			"               F.GONGYSB_ID,\n" + 
			"               F.FAZ_ID,\n" + 
			"               DECODE(SUM(DECODE( DECODE(F.QNET_AR,0,0,(NVL(CB.MEIJ,0) - NVL(CB.MEIJS,0))*29.271/F.QNET_AR),0,0,F.LAIMSL)),0,0,\n" + 
			"               SUM(DECODE(F.QNET_AR,0,0,(NVL(CB.MEIJ,0) - NVL(CB.MEIJS,0))*29.271/F.QNET_AR)*F.LAIMSL)/\n" + 
			"               SUM(DECODE( DECODE(F.QNET_AR,0,0,(NVL(CB.MEIJ,0) - NVL(CB.MEIJS,0))*29.271/F.QNET_AR),0,0,F.LAIMSL))\n" + 
			"               )BUHSBMDJ\n" + 
			"          FROM (SELECT F.ID,\n" + 
			"                       F.DIANCXXB_ID,\n" + 
			"                       F.LAIMSL,\n" + 
			"                       F.CHES,\n" + 
			"                       GL.NIANJHJCB_ID,\n" + 
			"                       GL.GONGYSB_ID,\n" + 
			"                       GL.MEIKXXB_ID,\n" + 
			"                       GL.FAZ_ID,\n" + 
			"                       GL.PINZB_ID,\n" + 
			"                       GL.YUNSDWB_ID,\n" + 
			"                       ZL.QNET_AR\n" + 
			"                  FROM FAHB F, NIANJHJCGLB GL, ZHILB ZL\n" + 
			"                 WHERE F.GONGYSB_ID = GL.GONGYSB_ID\n" + 
			"                   AND F.MEIKXXB_ID = GL.MEIKXXB_ID\n" + 
			"                   AND F.PINZB_ID = GL.PINZB_ID\n" + 
			"                   AND F.FAZ_ID = GL.FAZ_ID\n" + 
			"                   AND F.ZHILB_ID = ZL.ID\n" + 
			"                   AND YUNSDWB_ID = DECODE(GL.YUNSDWB_ID,\n" + 
			"                                           -1,\n" + 
			"                                           -1,\n" + 
			"                                           (SELECT C.YUNSDWB_ID\n" + 
			"                                              FROM CHEPB C\n" + 
			"                                             WHERE FAHB_ID = F.ID\n" + 
			"                                               AND ROWNUM = 1))\n" + 
			"                   AND TO_CHAR(F.DAOHRQ, 'yyyy-mm') = '" + Year + "-" + YUEF +"') F,\n" + 
			"               (SELECT FAHB_ID, MEIJ, MEIJS FROM RUCCB) CB,\n" + 
			"               (SELECT ID FROM DIANCXXB WHERE (ID=" + getTreeid() +" OR FUID=" + getTreeid() +")) DC\n" + 
			"         WHERE F.ID = CB.FAHB_ID(+)\n" + 
			"            AND F.DIANCXXB_ID=DC.ID\n" + 
			"         GROUP BY F.NIANJHJCB_ID, F.GONGYSB_ID, F.FAZ_ID) SJ,\n" + 
			"         (SELECT SUM(F.LAIMSL) BENY_DUN,\n" + 
			"               SUM(F.CHES) BENY_CHE,\n" + 
			"               F.NIANJHJCB_ID,\n" + 
			"               F.GONGYSB_ID,\n" + 
			"               F.FAZ_ID,\n" + 
			"               DECODE(SUM(DECODE( DECODE(F.QNET_AR,0,0,(NVL(CB.MEIJ,0) - NVL(CB.MEIJS,0))*29.271/F.QNET_AR),0,0,F.LAIMSL)),0,0,\n" + 
			"               SUM(DECODE(F.QNET_AR,0,0,(NVL(CB.MEIJ,0) - NVL(CB.MEIJS,0))*29.271/F.QNET_AR)*F.LAIMSL)/\n" + 
			"               SUM(DECODE( DECODE(F.QNET_AR,0,0,(NVL(CB.MEIJ,0) - NVL(CB.MEIJS,0))*29.271/F.QNET_AR),0,0,F.LAIMSL))\n" + 
			"               )BUHSBMDJ\n" + 
			"          FROM (SELECT F.ID,\n" + 
			"                       F.DIANCXXB_ID,\n" + 
			"                       F.LAIMSL,\n" + 
			"                       F.CHES,\n" + 
			"                       GL.NIANJHJCB_ID,\n" + 
			"                       GL.GONGYSB_ID,\n" + 
			"                       GL.MEIKXXB_ID,\n" + 
			"                       GL.FAZ_ID,\n" + 
			"                       GL.PINZB_ID,\n" + 
			"                       GL.YUNSDWB_ID,\n" + 
			"                       ZL.QNET_AR\n" + 
			"                  FROM FAHB F, NIANJHJCGLB GL, ZHILB ZL\n" + 
			"                 WHERE F.GONGYSB_ID = GL.GONGYSB_ID\n" + 
			"                   AND F.MEIKXXB_ID = GL.MEIKXXB_ID\n" + 
			"                   AND F.PINZB_ID = GL.PINZB_ID\n" + 
			"                   AND F.FAZ_ID = GL.FAZ_ID\n" + 
			"                   AND F.ZHILB_ID = ZL.ID\n" + 
			"                   AND YUNSDWB_ID = DECODE(GL.YUNSDWB_ID,\n" + 
			"                                           -1,\n" + 
			"                                           -1,\n" + 
			"                                           (SELECT C.YUNSDWB_ID\n" + 
			"                                              FROM CHEPB C\n" + 
			"                                             WHERE FAHB_ID = F.ID\n" + 
			"                                               AND ROWNUM = 1))\n" + 
			"                  AND TO_CHAR(F.DAOHRQ, 'yyyy') = '" + Year + "') F,\n" + 
			"               (SELECT FAHB_ID, MEIJ, MEIJS FROM RUCCB) CB,\n" + 
			"               (SELECT ID FROM DIANCXXB WHERE (ID=" + getTreeid() +" OR FUID=" + getTreeid() +")) DC\n" + 
			"         WHERE F.ID = CB.FAHB_ID(+)\n" + 
			"           AND F.DIANCXXB_ID=DC.ID\n" + 
			"         GROUP BY F.NIANJHJCB_ID, F.GONGYSB_ID, F.FAZ_ID) LJ,\n" + 
			"       (SELECT JH.ID,\n" + 
			"               JH.GONGYSB_ID,\n" + 
			"               JH.FAZ_ID,\n" + 
			"               JH.NIANJH_DUN,\n" + 
			"               JH.NIANJH_CHE,\n" + 
			"               JH.NIANJH_BUHSBMDJ,\n" + 
			"               JH.ZHEHMY_DUN,\n" + 
			"               JH.ZHEHMY_CHE,\n" + 
			"               JH.ZHEHMY_BUHSBMDJ\n" + 
			"          FROM NIANJHJCB JH, NIANJHJCHZB HZB,(SELECT ID FROM DIANCXXB WHERE (ID=" + getTreeid() +" OR FUID=" + getTreeid() +")) DC\n" + 
			"         WHERE JH.NIANJHJCHZB_ID = HZB.ID\n" + 
			"           AND HZB.DIANCXXB_ID = DC.ID\n" + 
			"           AND TO_CHAR(HZB.RIQ, 'yyyy') = '" + Year +"') JH,\n" + 
			"           GONGYSB,CHEZXXB\n" + 
			" WHERE SJ.NIANJHJCB_ID = JH.ID\n" + 
			"   AND SJ.GONGYSB_ID = JH.GONGYSB_ID\n" + 
			"   AND SJ.FAZ_ID = JH.FAZ_ID\n" + 
			"   AND SJ.GONGYSB_ID = LJ.GONGYSB_ID\n" + 
			"   AND SJ.FAZ_ID = LJ.FAZ_ID\n" + 
			"   AND SJ.GONGYSB_ID = GONGYSB.ID\n" + 
			"   AND SJ.FAZ_ID = CHEZXXB.ID\n" + 
			" GROUP BY ROLLUP (GONGYSB.MINGC, CHEZXXB.MINGC)\n" + 
			" HAVING (GROUPING(GONGYSB.MINGC)+GROUPING(CHEZXXB.MINGC)=0) OR (GROUPING(GONGYSB.MINGC)+GROUPING(CHEZXXB.MINGC)=2))";

		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();
		// 定义表头数据
		String ArrHeader[][] = new String[3][17];
		ArrHeader[0] = new String[] { "单位", "发站", "年计划", "年计划", "年计划", "折合每月", "折合每月", "折合每月",
				"实际完成情况", "实际完成情况", "实际完成情况", "实际完成情况", "实际完成情况", "实际完成情况",
				"未完成", "未完成", "未完成"};
		ArrHeader[1] = new String[] { "单位", "发站", "年计划", "年计划", "年计划", "折合每月", "折合每月", "折合每月",
				"本月", "本月", "本月", "累计", "累计", "累计",
				"本月", "累计", "不含税标煤价"};
		ArrHeader[2] = new String[] { "单位", "发站", "吨", "车", "不含税标煤价（元/吨）", "吨", "车", "不含税标煤价（元/吨）",
				"吨", "车", "不含税标煤价（元/吨）", "吨", "车", "不含税标煤价（元/吨）",
				"吨", "吨", "（元/吨）"};
		// 列宽
		int ArrWidth[] = new int[] { 120, 60, 80, 80, 80, 80, 80, 80, 80, 80, 80,
				80, 80, 80, 80, 80, 80  };

		// 设置页标题
		rt.setTitle("年计划检测表", ArrWidth);
		rt.setDefaultTitle(8, 3, strMonth, Table.ALIGN_CENTER);

		// 数据
		rt.setBody(new Table(rs, 3, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.getPageRows(24, this.paperStyle));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 2);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}


	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
			getSelectData();
		}

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
		getSelectData();
	}

	private void init() {
		Visit visit = (Visit) getPage().getVisit();
		visit.setProSelectionModel10(null);
		visit.setDropDownBean10(null);
		visit.setProSelectionModel3(null);
		visit.setDropDownBean3(null);
		visit.setDefaultTree(null);
		setDiancmcModel(null);
		setTreeid(visit.getDiancxxb_id() + "");
		paperStyle();
		getSelectData();
	}

	private boolean getDiancg() {
		JDBCcon con = new JDBCcon();
		String sqlq = "select id from diancxxb where fuid in(select id from diancxxb where id="
				+ getTreeid() + " and jib=3)";
		ResultSetList rs = con.getResultSetList(sqlq);
		if (rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		JDBCcon con = new JDBCcon();
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);

		setToolbar(tb1);
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
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
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

	private int paperStyle;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
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

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='纸张类型' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
	}

	public int getJibbyDCID(JDBCcon con, String dcid) {
		int jib = 3;
		ResultSetList rsl = con
				.getResultSetList("select jib from diancxxb where id =" + dcid);
		if (rsl.next()) {
			jib = rsl.getInt("jib");
		}
		rsl.close();
		return jib;
	}
}
