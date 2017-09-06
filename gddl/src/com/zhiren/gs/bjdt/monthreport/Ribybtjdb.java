package com.zhiren.gs.bjdt.monthreport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ribybtjdb extends BasePage {

	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	private boolean reportShowZero() {
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}

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

	// ***************设置消息框******************//
	private String _msg;

	public void setMsg(String _value) {
		_msg = MainGlobal.getExtMessageBox(_value, false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	// *************删除前的确认*****************
	private String getConfirm(String title, String content, String tapsetryId) {
		return "Ext.MessageBox.confirm('" + title + "', '" + content
				+ "', function(btn) {" + " if(btn=='yes'){"
				+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)
				+ ";document.getElementById('" + tapsetryId
				+ "').click();}else{return;}" + "})";

	}

		
	private String mstrReportName = "";

	public String getTianzdwQuanc() {
		return getTianzdwQuanc(getDiancxxbId());
	}

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// 得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			if (_TianzdwQuanc.equals("北京大唐燃料有限公司")) {
				_TianzdwQuanc = "大唐国际发电股份有限公司燃料管理部";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}

	private boolean blnIsBegin = false;

	private String leix = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		String contantString = "";
		contantString = getDany_Leij();

		return contantString;
	}

	private String getDiancCondition() {
		JDBCcon cn = new JDBCcon();
		String diancxxb_id = getTreeid();
		String condition = "";
		ResultSet rs = cn
				.getResultSet("select jib,id,fuid from diancxxb where id="
						+ diancxxb_id);
		try {
			if (rs.next()) {
				if (rs.getLong("jib") == SysConstant.JIB_JT) {
					condition = "";
				} else if (rs.getLong("jib") == SysConstant.JIB_GS) {
					condition = " and dc.fuid=" + diancxxb_id;
				} else {
					condition = " and dc.id=" + diancxxb_id;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cn.Close();
		return condition;
	}

	private String getDany_Leij() {

		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		long lngDiancId = getDiancxxbId();// 电厂信息表id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String _Danwqc = getTianzdwQuanc();
	
		String condition = getDiancCondition();
		String fenx = "";
		String riqtiaojian = "fenx='" + fenx + "' and riq = to_date('"
				+ strDate + "','yyyy-mm-dd')";
		String riqtiaojian2 =""; 
		if (this.getSelzhuangtValue().getId() == 1) {
			fenx = "本月";
			riqtiaojian = "fenx='" + fenx + "' and riq = to_date('" + strDate
					+ "','yyyy-mm-dd')";
			riqtiaojian2="r.riq>=to_date('" + strDate
			+ "','yyyy-mm-dd') and r.riq<add_months(to_date('" + strDate
			+ "','yyyy-mm-dd'),1)";
		} else if (this.getSelzhuangtValue().getId() == 2) {
			fenx = "累计";
			riqtiaojian = "fenx='" + fenx + "' and riq = to_date('" + strDate
					+ "','yyyy-mm-dd')";
			riqtiaojian2 = "r.riq>=to_date('" + getNianfValue().getValue()
					+ "-01-01"
					+ "','yyyy-mm-dd') and r.riq<add_months(to_date('"
					+ strDate + "','yyyy-mm-dd'),1)";
		}

		String dcid = "";
		if (!this.getTreeid().equals("1")) {
			dcid = "and dc.id=" + this.getTreeid() + "\n";
		}
		

		sbsql.append(" select\n");
		sbsql.append(" decode(grouping(dc.dianclbb_id)+grouping(dc.mingc),2,'大唐国际发电股份有限公司',1,'★'||max(lb.mingc)||'小计',dc.mingc) as 电厂名,\n");
		sbsql.append(" nvl(sum(rh.rulsl),0) as 日报入炉数量,\n");
		sbsql.append(" nvl(sum(d01h.meithyhj),0) as 月报入炉数量,\n");
		sbsql.append("nvl(sum(rh.rulsl),0)-nvl(sum(d01h.meithyhj),0) as 数量差,\n");
		sbsql.append("  decode(sum(rh.rulsl),0,0,round(sum(rh.rulslrulrl) / sum(rh.rulsl),2)) 日报报入炉热值,\n");
		sbsql.append(" round(fun_zonghrlfrl(sum(d01h.biaozmlfd), sum(d01h.biaozmlgr), sum(d01h.shiyhyfd), sum(d01h.shiyhygr), sum(d01h.meithyfd),sum(d01h.meithygr)),2)  as 月报入炉热值,\n");
		sbsql.append("decode(sum(rh.rulsl),0,0,round(sum(rh.rulslrulrl) / sum(rh.rulsl),2))-round(fun_zonghrlfrl(sum(d01h.biaozmlfd), sum(d01h.biaozmlgr), sum(d01h.shiyhyfd), sum(d01h.shiyhygr), sum(d01h.meithyfd),sum(d01h.meithygr)),2) as 热值差\n");
		sbsql.append(" from \n");
		sbsql.append("         diancxxb dc,(select * from dianckjpxb where kouj='月报') px,dianclbb lb,\n");
		sbsql.append("         (select  diancxxb_id,  sum(meithyhj) meithyhj,sum(biaozmlfd) biaozmlfd, sum(biaozmlgr) biaozmlgr,\n");
		sbsql.append("        sum(shiyhyfd) shiyhyfd, sum(shiyhygr) shiyhygr, sum(meithyfd) meithyfd,sum(meithygr) meithygr\n");
		sbsql.append("          from diaor01bb  where " + riqtiaojian+ " group by diancxxb_id )d01h,\n");
		sbsql.append("       (select  diancxxb_id,sum(r.rulsl) rulsl,sum(r.rulsl*r.rulsf) rulslRulsf,sum(r.rulsl * r.rulrl) rulslrulrl--,sum(rh.rucsl) as rucsl,sum(r.rulsl) rulsl\n");
		sbsql.append("      from rezcb r \n");
		sbsql.append("      where\n");
		sbsql.append(riqtiaojian2 + "\n");
		sbsql.append("         group by diancxxb_id )rh\n");
		sbsql.append("  where  dc.id=px.diancxxb_id and dc.dianclbb_id=lb.id and dc.id=d01h.diancxxb_id(+) and dc.id=rh.diancxxb_id(+)   --and px.kouj='月报'  \n");
		sbsql.append(dcid);
		if (this.getTreeid().equals("1")) {
			sbsql.append("   group by  rollup(dc.dianclbb_id,dc.mingc) \n");
		} else {
			sbsql.append("   group by (dc.dianclbb_id,dc.mingc) \n");
		}
		sbsql.append("  order by grouping(dc.dianclbb_id) desc ,max(lb.xuh),grouping(dc.mingc) desc,max(px.xuh),dc.mingc\n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		// 定义表头数据
		String ArrHeader[][] = new String[2][5];
		ArrHeader[0] = new String[] { "", "数量(t)", "数量(t)","数量差", "Qnet,ar(MJ/kg)","Qnet,ar(MJ/kg)","热量差" };
		ArrHeader[1] = new String[] { "", "日报数统计", "月报数统计","日报数统计-月报数统计", "日报数统计", "月报数统计","日报数统计-月报数统计" };
		// 列宽
		int ArrWidth[] = new int[] { 100, 100, 100, 100, 100,100,100 };

		// 设置页标题
		rt.setTitle("日报月报统计对比表(" + getSelzhuangtValue().getValue() + ")",ArrWidth);
		rt.setDefaultTitle(1, 3, "填报单位:" + _Danwqc, Table.ALIGN_LEFT);
		// rt.setDefaultTitle(13,2,strMonth,Table.ALIGN_CENTER);
		rt.setDefaultTitle(rt.title.getCols() - 1, 2, "时间:" + strMonth,Table.ALIGN_RIGHT);
		// 数据
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);
		// rt.body.setColFormat(3,"0.000");
		rt.body.setColFormat(5, "0.000");
		rt.body.setColFormat(6, "0.000");
		rt.body.setColFormat(7,"0.000");
	
		// 页脚
		rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(1,1,"批准:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(4,2,"制表:"+((Visit)this.getPage().getVisit()).getRenymc(),Table.ALIGN_LEFT);
		// rt.setDefautlFooter(9,1,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 2, Table.PAGENUMBER_CHINA, Table.ALIGN_RIGHT);

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
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			// visit.setProSelectionModel3(null);
			// visit.setDropDownBean3(null);
			this.setSelzhuangtModel(null);
			this.setSelzhuangtValue(null);
			visit.setString2("");
			this.setTreeid(null);
			visit.setList1(null);
			getDiancmcModels();
			this.setTreeid(null);
			this.setNianfModel(null);
			this.setNianfValue(null);
			this.setYuefModel(null);
			this.setYuefValue(null);
			this.setEndNianfModel(null);
			this.setEndNianfValue(null);
			this.setEndNianfModel(null);
			this.setEndYuefValue(null);
		
		}
		getToolbars();
		blnIsBegin = true;
	}

	public void getToolbars() {
		Visit visit = (Visit) this.getPage().getVisit();

		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询方式:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("SelzhuangtSelect");
		cb.setWidth(100);
		cb.setListeners("select:function(){"
				+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)
				+ ";document.Form0.submit();}");
		tb1.addField(cb);

		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(50);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(40);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(80);
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
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null, "刷新", "function(){"
				+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200)
				+ ";document.Form0.submit();}");
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

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 电厂名称
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc,bianm from diancxxb";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(_value);
	}

	// end年份下拉框
	private static IPropertySelectionModel _EndNianfModel;

	public IPropertySelectionModel getEndNianfModel() {
		if (_EndNianfModel == null) {
			getEndNianfModels();
		}
		return _EndNianfModel;
	}

	private IDropDownBean _EndNianfValue;

	public IDropDownBean getEndNianfValue() {
		if (_EndNianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getEndNianfModel().getOptionCount(); i++) {
				Object obj = getEndNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_EndNianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _EndNianfValue;
	}

	public void setEndNianfValue(IDropDownBean Value) {
		if (_EndNianfValue != Value) {
			_EndNianfValue = Value;
		}
	}

	public IPropertySelectionModel getEndNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2003; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_EndNianfModel = new IDropDownModel(listNianf);
		return _EndNianfModel;
	}

	public void setEndNianfModel(IPropertySelectionModel _value) {
		_EndNianfModel = _value;
	}

	// end月份下拉框
	private static IPropertySelectionModel _EndYuefModel;

	public IPropertySelectionModel getEndYuefModel() {
		if (_EndYuefModel == null) {
			getEndYuefModels();
		}
		return _EndYuefModel;
	}

	private IDropDownBean _EndYuefValue;

	public IDropDownBean getEndYuefValue() {
		if (_EndYuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getEndYuefModel().getOptionCount(); i++) {
				Object obj = getEndYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_EndYuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _EndYuefValue;
	}

	public void setEndYuefValue(IDropDownBean Value) {
		if (_EndYuefValue != Value) {
			_EndYuefValue = Value;
		}
	}

	public IPropertySelectionModel getEndYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_EndYuefModel = new IDropDownModel(listYuef);
		return _EndYuefModel;
	}

	public void setEndYuefModel(IPropertySelectionModel _value) {
		_EndYuefModel = _value;
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
		for (i = 2003; i <= DateUtil.getYear(new Date()) + 1; i++) {
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

	// 条件选择框
	private static IPropertySelectionModel _SelzhuangtModel;

	public void setSelzhuangtModel(IDropDownModel _value) {
		_SelzhuangtModel = _value;
	}

	public IPropertySelectionModel getSelzhuangtModel() {
		if (_SelzhuangtModel == null) {
			getSelzhuangtModels();
		}
		return _SelzhuangtModel;
	}

	private IDropDownBean _SelzhuangtValue;

	public IDropDownBean getSelzhuangtValue() {
		if (_SelzhuangtValue == null) {
			getSelzhuangtModels();
			setSelzhuangtValue((IDropDownBean) getSelzhuangtModel()
					.getOption(0));
		}
		// if(mstrReportName.equals(RT_DR01)){
		// setSelzhuangtValue((IDropDownBean)getSelzhuangtModel().getOption(0));
		// }
		return _SelzhuangtValue;
	}

	private boolean _SelzhuangtChange = false;

	public void setSelzhuangtValue(IDropDownBean Value) {
		if (_SelzhuangtValue == Value) {
			_SelzhuangtChange = false;
		} else {
			_SelzhuangtValue = Value;
			_SelzhuangtChange = true;
		}
	}

	public IPropertySelectionModel getSelzhuangtModels() {
		List listSelzhuangt = new ArrayList();
		listSelzhuangt.add(new IDropDownBean(1, "当月月报"));
		listSelzhuangt.add(new IDropDownBean(2, "累计月报"));
		_SelzhuangtModel = new IDropDownModel(listSelzhuangt);
		return _SelzhuangtModel;
	}

	

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}

}