package com.zhiren.dc.monthReport.rulbhsbmdj;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterFac_dt;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

/*
 * 作者：刘雨
 * 时间：2010-07-29
 * 描述：新增入炉不含税标煤单价查询，分时间和机组进行查询
 */

public class Rulbhsbmdj extends BasePage implements PageValidateListener {

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

	public SortMode getSort() {
		return SortMode.USER;
	}

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	public String getPrintTable() {
		return getYuehcb();
		
	}

	private String getYuehcb() { // 耗存表
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String lngDiancId = getTreeid();// 电厂信息表id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";

		String sql = getBaseSql();
		if(sql.equals("")||sql==null){
			this.setMsg("无法获得机组分组信息！");
			return "";
		} 
		ResultSet rs = cn.getResultSet(sql);
		Report rt = new Report();

		
//		 定义表头数据
		String ArrHeader[][]=new String[3][16];
		ArrHeader[0]=new String[] {"机组分期","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","天然煤单价(去税)","油","油","单价（去税）","单价（去税）"};
		ArrHeader[1]=new String[] {"机组分期","金额","金额","金额","金额","金额","数量","数量","数量","数量","数量","数量","去税单价","本月耗用","本月入炉<br>天然煤","当月入炉<br>标煤"};
		ArrHeader[2]=new String[] {"机组分期","上月存煤","本月承付","本月估价","上月估价","厂内杂费","上月存煤","本月承付","本月估价","上月估价","入炉天然煤","本月耗用<br>标煤量","去税单价","本月耗用","本月入炉<br>天然煤","当月入炉<br>标煤"};

		int ArrWidth[]=new int[] {80,65,65,65,65,65,65,65,65,65,65,65,60,60,65,65};

		// 设置页标题
		rt.setTitle(strMonth+"入炉不含税标煤单价", ArrWidth);

		// 数据
		rt.setBody(new Table(rs, 3, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = true;
		rt.body.mergeFixedRow();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getBaseSql() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		String diancxxb_id = getTreeid();// 电厂信息表id
		String strDate = getNianfValue().getValue() + "-"
				+ getYuefValue().getValue() + "-01";
		String strMonth = getNianfValue().getValue() + "年"
				+ getYuefValue().getValue() + "月";
		
		String sql = 
			"select decode(grouping(jiz),1,'合计',jiz),\n" +
			"       round_new(sum(shangycm),2),\n" + 
			"       round_new(sum(benycf),2),\n" + 
			"       round_new(sum(benygj),2),\n" + 
			"       round_new(sum(shangygj),2),\n" + 
			"       round_new(sum(ranlcnfy),2),\n" + 
			"       round_new(sum(shangycmsl),2),\n" + 
			"       round_new(sum(benycfsl),2),\n" + 
			"       round_new(sum(benygjsl),2),\n" + 
			"       round_new(sum(shangygjsl),2),\n" + 
			"       round_new(sum(rultrmsl),2),\n" + 
			"       round_new(sum(benyhybml),2),\n" + 
			"       round_new(sum(youqsdj),2),\n" + 
			"       round_new(sum(yousl),2),\n" + 
			"       decode(grouping(jiz),1,null,sum(benyrltrmdj)),\n" + 
			"       decode(sum(benyhybml),0,0,round_new(sum(benyrltrmdj*rultrmsl+youqsdj*yousl)/sum(benyhybml),2))\n" + 
			"from\n" + 
			"(\n";
		
		String sql_jz = "select * from jizfzb where diancxxb_id = " + diancxxb_id + "\n";
		ResultSetList rsl = con.getResultSetList(sql_jz);
		
		int i = 0;
		String pinz = "";
		while(rsl.next()){
			if(i!=0){
				sql += "union\n";
			}
			if(rsl.getString("mingc").equals("二期")){
				pinz = "'贫瘦煤','无烟煤'";
			} else if(rsl.getString("mingc").equals("三期")){
				pinz = "'气肥煤','褐煤'";
			}
			
			sql += 
				"select jz.jiz,\n" +
				"       round_new(nvl(cm.shangycmsl,0)*nvl(rl.shangycmdj,0),2) as shangycm,\n" + 
				"       nvl(cf.chengfje,0) as benycf,\n" + 
				"       nvl(bygj.benygjje,0) as benygj,\n" + 
				"       nvl(sygj.shangygjje,0) as shangygj,\n" + 
				"       nvl(rl.changnzf,0) as ranlcnfy,\n" + 
				"       nvl(cm.rultrmsl,0) as shangycmsl,\n" + 
				"       nvl(cf.meil,0) as benycfsl,\n" + 
				"       nvl(bygj.benygjsl,0) as benygjsl,\n" + 
				"       nvl(sygj.shangygjsl,0) as shangygjsl,\n" + 
				"       nvl(bycm.rultrmsl,0) as rultrmsl,\n" + 
				"       nvl(bm.benyhybml,0) as benyhybml,\n" + 
				"       nvl(rl.youqsdj,0) as youqsdj,\n" + 
				"       nvl(you.yousl,0) as yousl,\n" + 
				"       decode(nvl(cm.rultrmsl,0)+nvl(cf.meil,0)+nvl(bygj.benygjsl,0)-nvl(sygj.shangygjsl,0),\n" + 
				"              0,\n" + 
				"              0,\n" + 
				"             round_new((nvl(cm.shangycmsl,0)*nvl(rl.shangycmdj,0)+nvl(cf.chengfje,0)+nvl(bygj.benygjje,0)-nvl(sygj.shangygjje,0)+nvl(rl.changnzf,0))/(nvl(cm.rultrmsl,0)+nvl(cf.meil,0)+nvl(bygj.benygjsl,0)-nvl(sygj.shangygjsl,0)),2)) as benyrltrmdj\n" + 
				"from\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz from dual) jz," +
				"--上月存煤数量\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,nvl(sum(y.kuc), 0) as shangycmsl,\n" + 
				"        nvl(sum(y.fady + y.gongry),0) as  rultrmsl\n" + 
				"          from yuehcb y, yuetjkjb tj\n" + 
				"         where y.yuetjkjb_id = tj.id\n" + 
				"           and y.fenx = '本月'\n" + 
				"           and tj.diancxxb_id = "+diancxxb_id+"\n" + 
				"           and tj.riq = add_months(to_date('"+strDate+"', 'yyyy-MM-dd'), -1)\n" + 
				"           and tj.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+pinz+"))) cm,\n" + 
				"--入炉天然煤数量\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,nvl(sum(y.fady + y.gongry),0) as  rultrmsl\n" + 
				"          from yuehcb y, yuetjkjb tj\n" + 
				"         where y.yuetjkjb_id = tj.id\n" + 
				"           and y.fenx = '本月'\n" + 
				"           and tj.diancxxb_id = "+diancxxb_id+"\n" + 
				"           and tj.riq = to_date('"+strDate+"', 'yyyy-MM-dd')\n" + 
				"           and tj.pinzb_id in\n" + 
				"               (select id from pinzb where mingc in ("+pinz+"))) bycm,\n" + 
				"--入炉信息\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,r.shangycmdj, r.youqsdj, r.changnzf\n" + 
				"  from rulxxwh r, jizfzb j\n" + 
				" where r.jizfzb_id = j.id\n" + 
				"   and j.mingc = '"+rsl.getString("mingc")+"'\n" + 
				"   and r.diancxxb_id = "+diancxxb_id+"\n" + 
				"   and r.riq = to_date('"+strDate+"', 'yyyy-MM-dd')) rl,\n" + 
				"--本月承付金额、数量\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,sum((y.meij + y.yunj + y.zaf + y.daozzf + y.qit + y.kuangqyf) * y.jiesl) as chengfje,\n" + 
				"        sum(y.jiesl) as meil\n" + 
				"          from yuejsbmdj y, yuetjkjb tj\n" + 
				"         where y.yuetjkjb_id = tj.id\n" + 
				"           and y.fenx = '本月'\n" + 
				"           and tj.diancxxb_id = "+diancxxb_id+"\n" + 
				"           and tj.riq = to_date('"+strDate+"', 'yyyy-MM-dd')\n" + 
				"           and tj.pinzb_id in (select id from pinzb where mingc in ("+pinz+"))) cf,\n" + 
				"--本月估价金额、数量\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,nvl(sum(max(g.meij) * f.laimsl), 0) as benygjje,\n" + 
				"               nvl(sum(f.laimsl), 0) as benygjsl\n" + 
				"          from guslsb g, fahb f\n" + 
				"         where g.fahb_id = f.id\n" + 
				"           and g.leix < 4\n" + 
				"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
				"           and last_day(f.daohrq) = last_day(to_date('"+strDate+"', 'yyyy-MM-dd'))\n" + 
				"           and f.pinzb_id in (select id from pinzb where mingc in ("+pinz+"))\n" + 
				"         group by f.id, f.laimsl) bygj,\n" + 
				"--上月估价金额、数量\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,nvl(sum(max(g.meij) * f.laimsl), 0) as shangygjje,\n" + 
				"               nvl(sum(f.laimsl), 0) as shangygjsl\n" + 
				"          from guslsb g, fahb f\n" + 
				"         where g.fahb_id = f.id\n" + 
				"           and g.leix < 4\n" + 
				"           and f.diancxxb_id = "+diancxxb_id+"\n" + 
				"           and last_day(f.daohrq) = last_day(to_date('"+strDate+"', 'yyyy-MM-dd'))\n" + 
				"           and f.pinzb_id in (select id from pinzb where mingc in ("+pinz+"))\n" + 
				"         group by f.id, f.laimsl) sygj,\n" + 
				"--本月耗用标煤量\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,nvl(sum(m.fadhy+m.gongrhy),0) as benyhybml\n" + 
				"from meihyb m\n" + 
				"where m.diancxxb_id = "+diancxxb_id+"\n" + 
				"  and last_day(m.rulrq) = last_day(to_date('"+strDate+"', 'yyyy-MM-dd'))\n" + 
				"  and m.jizfzb_id = "+rsl.getString("id")+") bm,\n" + 
				"--当月耗用油数量\n" + 
				"(select '"+rsl.getString("mingc")+"' as jiz,nvl(sum(y.fadyy+y.gongry),0) as yousl\n" + 
				"from yueshcyb y\n" + 
				"where y.diancxxb_id = "+diancxxb_id+"\n" + 
				"  and last_day(y.riq) = last_day(to_date('"+strDate+"', 'yyyy-MM-dd'))\n" + 
				"  and y.pinzb_id in (select id from pinzb where mingc in ("+pinz+"))\n" + 
				") you\n" +
				"where jz.jiz = cm.jiz(+)\n" +
				"     and jz.jiz = bycm.jiz(+)\n" + 
				"     and jz.jiz = rl.jiz(+)\n" + 
				"     and jz.jiz = cf.jiz(+)\n" + 
				"     and jz.jiz = bygj.jiz(+)\n" + 
				"     and jz.jiz = sygj.jiz(+)\n" + 
				"     and jz.jiz = bm.jiz(+)\n" + 
				"     and jz.jiz = you.jiz(+)\n";

			i++;
		}
		
		sql += 
			")\n" +
			"group by rollup(jiz)\n" + 
			"order by grouping(jiz) desc,jiz\n";
		
		if(i==0){
			return "";
		}

		return sql;
		
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
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit = (Visit) getPage().getVisit();
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
//			int _yuef = DateUtil.getMonth(new Date());
//			if (_yuef == 1) {
//				_nianf = _nianf - 1;
//			}
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
//			if (_yuef == 1) {
//				_yuef = 12;
//			} else {
//				_yuef = _yuef - 1;
//			}
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
				.getResultSetList("select zhi from xitxxb where zhuangt=1 and mingc='纸张类型' and diancxxb_id="
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