package com.zhiren.dc.rulgl.rulmzlcx;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：yangzl
 * 时间：2010-03-24
 */

/*
 * 作者：yangzl
 * 修改时间：2010-03-26
 * 描述：
 * 		当天没有记录时仍然显示当天记录，记录为空
 */

public class RulmzlcxReport extends BasePage implements PageValidateListener {

	// 消息框
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, true);
	}

	protected void initialize() {
		super.initialize();
		_pageLink = "";
		setMsg(null);
	}

	// 按钮事件－“刷新”
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = true;
			Refurbish();
		}
	}

	// 页面初始化刷新事件
	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getSelectData();
	}

	public String getPrintTable() {
		return getSelectData();
	}

	private String getSelectData() {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();

		// 机组
		ResultSetList jizrs = cn
				.getResultSetList("select mingc from jizfzb where diancxxb_id="+this.getTreeid()+" order by xuh");

		// 机组数
		int jizs = jizrs.getRows();

		if(jizs<=0){
			this.setMsg("'当前电厂没有机组,无法显示报表！'");
			return "";
		}
		
		// 宽度
		int ArrWidth[] = new int[1 + (jizs * 2)];
		// 表头长度
		int headLength = 1 + (jizs * 2);
		// 表头数组
		String ArrHeader[][] = new String[2][headLength];
		ArrHeader[0][0] = "时间";
		ArrHeader[1][0] = "系统";

		// 机组名称数组
		String jizmcs[] = new String[jizs];
		int i = 0;
		while (jizrs.next()) {
			jizmcs[i] = jizrs.getString("mingc");
			i++;
		}
		jizrs.close();

		int width=jizs<=4?600/(2*jizs+1):770/(2*jizs+1);
		
		// 动态构建报表表头及单元格宽度
		for (int j = 0; j < headLength; j++) {
			ArrWidth[j] = width;
			if ((j + 1) < (jizs + 1)) {
				ArrHeader[0][(j + 1)] = "St,ad(%)";
				ArrHeader[1][(j + 1)] = jizmcs[j];
			} else if ((j + 1) < headLength) {
				ArrHeader[0][(j + 1)] = "Qnet,ar(卡/克)";
				ArrHeader[1][(j + 1)] = jizmcs[(j - jizs)];
			}
		}

		String riq = this.getNianfValue().getValue() + "-"
				+ this.getYuefValue().getValue();

		//根据需求显示30天的数据，构建tmp临时表，存储01到30天
		StringBuffer sql = new StringBuffer();
		
		sql.append("select decode(t.day,null,'算术平均',t.day) ri,");

		for (int j = 0; j < 2 * jizs; j++) {
			if (j < jizs) {
				sql.append("		formatxiaosws(round(avg(decode(mingc,'" + jizmcs[j]
						+ "',decode(zhib,'stad',zhi,''),'')),2),2) " + jizmcs[j]
						+ ",\n");
			} else {
				sql.append("		round(avg(decode(mingc,'" + jizmcs[j - jizs]
						+ "',decode(zhib,'qnet',zhi,''),zhi,''))) "
						+ jizmcs[j - jizs] + ",\n");
			}
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(" from (\n");
		sql.append("		select to_char(r.rulrq,'dd') ri,j.mingc,'stad' zhib,case when sum(decode(r.meil,null,0,0,0))=0 then '' else to_char(round(sum(nvl(r.stad,0)*r.meil)/sum(r.meil),2)) end as  zhi\n");
		sql.append("    	from rulmzlb r,jizfzb j\n");
		sql.append("    	where r.jizfzb_id=j.id\n");
		sql.append("          	and r.diancxxb_id=");
		sql.append(this.getTreeid());
		sql.append("\n");
		sql.append("          	and to_char(r.rulrq,'yyyy-mm')='");
		sql.append(riq);
		sql.append("'\n");
		sql.append("    	group by r.rulrq,j.mingc\n");
		sql.append("    union\n");
		sql.append("    	select to_char(r.rulrq,'dd') ri,j.mingc,'qnet' zhib,case when sum(decode(r.meil,null,0,0,0))=0 then '' else to_char(round(sum(nvl(r.qnet_ar,0)*r.meil)*1000/(sum(r.meil)*4.1816))) end as zhi\n");
		sql.append("    	from rulmzlb r,jizfzb j\n");
		sql.append("    	where r.jizfzb_id=j.id\n");
		sql.append("          	and r.diancxxb_id=");
		sql.append(this.getTreeid());
		sql.append("\n");
		sql.append("          	and to_char(r.rulrq,'yyyy-mm')='");
		sql.append(riq);
		sql.append("'\n");
		sql.append("    	group by r.rulrq,j.mingc\n");
		sql.append("    order by ri ) rl,(");
		sql.append("		select to_char(date0, 'dd') day\n");
		sql.append("		from (select trunc(to_date('"+this.getNianfValue().getId()+"','yyyy'), 'yyyy') + rn - 1 date0");
		sql.append("				from (select rownum rn from all_objects where rownum < 366))");
		sql.append("		where to_char(date0, 'mm') = '"+this.getYuefValue().getValue()+"'");
		sql.append(") t");
		sql.append(" where rl.ri(+)=t.day");
		sql.append(" group by rollup(t.day)\n");
		sql.append(" union \n");
		//加权平均值
				sql.append("select '',\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("formatxiaosws(decode(sum(r.meil),0,0,round_new(sum(r.meil*stad)/sum(r.meil),2)),2) as stad,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar,\n");
				sql.append("round(decode(sum(r.meil),0,0,round_new(sum(r.meil*r.qnet_ar)/sum(r.meil),2))*1000/4.1816,0) as qnet_ar\n");
				sql.append(" from rulmzlb r where to_char(r.rulrq, 'yyyy-mm') = '"+this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"'\n");
				sql.append("and r.qnet_ar is not null\n");
				sql.append("and r.meil!=0");


		// 定义报表
		Report rt = new Report();

		ResultSetList rs = cn.getResultSetList(sql.toString());

		// 数据
		rt.setBody(new Table(rs, 2, 0, 1));
	
		rt.setTitle( this.getYuefValue().getValue() + "月份" + "入炉煤热值、硫分统计", ArrWidth);
		rt.setDefaultTitle(1, 3, "",Table.ALIGN_LEFT);
		rt.body.setCells(1, 1, rt.body.getRows(), ArrHeader[0].length,
				Table.PER_ALIGN, Table.ALIGN_CENTER);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(35);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = true;
//		rt.body.mergeFixedRow();// 合并行
//		rt.body.mergeFixedCols();// 和并列

		if (rt.body.getRows() > 1) {
			rt.body.merge(1, 2, 1, 1+jizs);
			rt.body.merge(1, 2+jizs, 1, 1+2*jizs);
		}
		rt.body.setCellValue(rt.body.getRows(), 1, "加权平均");
		rt.body.merge(rt.body.getRows(), 2, rt.body.getRows(), 5);
		rt.body.merge(rt.body.getRows(), 6, rt.body.getRows(), 9);
		// 页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		// rt.setDefautlFooter(5, 2, "审核:", Table.ALIGN_LEFT);
		rt
				.setDefautlFooter(rt.body.cols.length - 2, 2, "制表:",
						Table.ALIGN_LEFT);
		rt.setPaper(Report.PAPER_A4);
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rs.close();
		cn.Close();
		return rt.getAllPagesHtml();
	}

	public String getDiancmc(String id){
		String diancmc="";
		JDBCcon con=new JDBCcon();
		ResultSetList rs=con.getResultSetList("select mingc from diancxxb where id="+id);
		while(rs.next()){
			diancmc=rs.getString("mingc");
		}
		rs.close();
		con.Close();
		return diancmc;
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// ***************************报表初始设置***************************//
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

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

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

	/**
	 * 页面开始时初始化方法
	 */
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			this.setNianfValue(null);
			this.setNianfModel(null);
			this.setYuefModel(null);
			this.setYuefValue(null);
			visit.setString2(null);		
			getSelectData();
		}
		getToolBars();
	}

	// 年份下拉框
	private IPropertySelectionModel _NianfModel;

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
		_NianfValue = Value;
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
	private IPropertySelectionModel _YuefModel;

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
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, i < 10 ? ("0" + i) : String
					.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// 条件工具
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("年份:"));
		ComboBox nf = new ComboBox();
		nf.setWidth(50);
		nf.setTransform("NianfDropDown");
		nf.setId("NianfDropDown");
		nf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf.setLazyRender(true);
		nf.setEditable(true);
		tb1.addField(nf);

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yf = new ComboBox();
		yf.setWidth(50);
		yf.setTransform("YuefDropDown");
		yf.setId("YuefDropDown");
		yf.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		yf.setLazyRender(true);
		yf.setEditable(true);
		tb1.addField(yf);

		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		visit.setDefaultTree(dt);

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

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}

	// 电厂名称
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		System.out.println("treeid:" + treeid);
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public String getTreeScript() {
		return getTree().getScript();
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

}