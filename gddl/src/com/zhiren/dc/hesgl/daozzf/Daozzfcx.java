package com.zhiren.dc.hesgl.daozzf;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author 尹佳明
 * 2009-07-15
 * 类名：Daozzfcx(到站杂费查询)
 */
/*
 * 给getPrintTable()方法中的SQL查询语句添加外连接
 * 2009-07-27
 */
/*
 * 作者：王磊
 * 时间：2009-10-16
 * 描述：增加按费用名称过滤的功能。
 */
/*
 * 作者：王磊
 * 时间：2009-11-30
 * 描述：修改费用项目下拉框初始化
 */
/*
 * 作者：陈泽天 时间：2010-01-20 15：30 描述：修改关于报表的A4纸的格式问题， 把报表的格式设定为A4打印形式。
 */
public class Daozzfcx extends BasePage implements PageValidateListener {

	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	protected void initialize() {
		this._msg = "";
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

	public boolean getRaw() {
		return true;
	}

	// 起始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	// 结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

	// 发站名称下拉框
	public IDropDownBean getDaozValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getDaozModel().getOptionCount() > 0) {
				setDaozValue((IDropDownBean) getDaozModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setDaozValue(IDropDownBean gongysValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(gongysValue);
	}

	public IPropertySelectionModel getDaozModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setDaozModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setDaozModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setDaozModels() {
		String str = "select 0 id, '全部' mingc from dual union all select * from "
				+ "(select id, mingc from chezxxb where id in (select distinct d.faz_id from "
				+ "daozzfb d) order by mingc)";
		setDaozModel(new IDropDownModel(str));
	}

	// 费用下拉框
	public IDropDownBean getFeiyValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean5() == null) {
			if (getFeiyModel().getOptionCount() > 0) {
				setFeiyValue((IDropDownBean) getFeiyModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean5();
	}

	public void setFeiyValue(IDropDownBean gongysValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean5(gongysValue);
	}

	public IPropertySelectionModel getFeiyModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			setFeiyModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setFeiyModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(value);
	}

	public void setFeiyModels() {
		String str = 
			"select 0 id, nvl('全部', '') mingc from dual\n" +
			"union\n" + 
			"select i.id, i.mingc from item i where itemsortid=108";
		setFeiyModel(new IDropDownModel(str));
	}

	// 显示方式下拉框
	public IDropDownBean getXiansfsValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getXiansfsModel().getOptionCount() > 0) {
				setXiansfsValue((IDropDownBean) getXiansfsModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setXiansfsValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getXiansfsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setXiansfsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setXiansfsModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void setXiansfsModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "汇总"));
		list.add(new IDropDownBean(2, "明细"));
		setXiansfsModel(new IDropDownModel(list));
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	public String getPrintTable() {

		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String strSql = "";
		String faz = getDaozValue().getValue();
		if (!faz.equals("全部")) {
			faz = " and faz_id = " + (int) getDaozValue().getId();
		} else {
			faz = "";
		}
		String feiy = "";
		if (getFeiyValue() != null) {
			if (getFeiyValue().getId() != 0) {
				feiy = " and i.id = " + getFeiyValue().getId();
			}
		}
		if (getXiansfsValue().getId() == 1) {
			strSql = "select '' riq,b.faz,b.feiymc,b.ches,b.zibcs,b.jine from \n"
					+ "(select c.mingc as faz,decode(grouping(i.mingc)+grouping(c.mingc),2,'总计',1,'小计',i.mingc) as feiymc, \n"
					+ "sum(d.ches) as ches,sum(d.zibcs) as zibcs,sum(d.zongje) as jine \n"
					+ "from daozzfb d, chezxxb c, item i \n"
					+ "where d.faz_id=c.id(+) and d.feiymc_item_id=i.id \n"
					+ "and riq >= to_date('"
					+ getBRiq()
					+ "', 'yyyy-MM-dd') \n"
					+ "and riq <= to_date('"
					+ getERiq()
					+ "', 'yyyy-MM-dd') \n"
					+ "and d.diancxxb_id = "
					+ getTreeid()
					+ faz
					+ feiy
					+ " \n"
					+ "group by rollup(c.mingc,i.mingc) \n"
					+ "order by c.mingc,i.mingc) b";
		} else {
			strSql = "select decode(grouping(czxx.mingc)+grouping(riq)+grouping(i.mingc), "
					+ "3, '总计', 2, '小计', to_char(dzxx.riq, 'yyyy-MM-dd')) as riq,\n"
					+ "czxx.mingc czmc, i.mingc fymc, sum(dzxx.ches) ches, sum(dzxx.zibcs) zibcs, sum(dzxx.zongje) zongje\n"
					+ "from daozzfb dzxx, chezxxb czxx, item i\n"
					+ "where dzxx.faz_id = czxx.id(+) and dzxx.feiymc_item_id = i.id and riq >= to_date('"
					+ getBRiq()
					+ "', 'yyyy-MM-dd')\n"
					+ "and riq <= to_date('"
					+ getERiq()
					+ "', 'yyyy-MM-dd') and dzxx.diancxxb_id = "
					+ getTreeid()
					+ faz
					+ feiy
					+ "\n"
					+ "group by rollup(czxx.mingc, dzxx.riq, i.mingc)\n"
					+ "having not(grouping(czxx.mingc)+grouping(riq)+grouping(i.mingc) = 1)";
		}
		ResultSetList rslData = con.getResultSetList(strSql);

		int ArrWidth[] = new int[6];
		ArrWidth[0] = 100;
		ArrWidth[1] = 120;
		ArrWidth[2] = 140;
		ArrWidth[3] = 90;
		ArrWidth[4] = 90;
		ArrWidth[5] = 100;

		String ArrHeader[][] = new String[1][6];
		ArrHeader[0][0] = "日期";
		ArrHeader[0][1] = "发站名称";
		ArrHeader[0][2] = "费用名称";
		ArrHeader[0][3] = "车数";
		ArrHeader[0][4] = "自备车数";
		ArrHeader[0][5] = "总金额";

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle("到 站 杂 费 查 询", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 0));
		rt.body.setPageRows(rt.PAPER_ROWS);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));

		rt.body.setColCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setColCells(5, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.setDefaultTitle(1, 2, "制表单位："
				+ ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, "查询日期：" + getBRiq() + " 至 " + getERiq(),
				Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(5, 2, "制表：", Table.ALIGN_CENTER);
		if (rslData.getRows() > 0) {
			rt.body.mergeCell(rslData.getRows() + 1, 1, rslData.getRows() + 1,
					2);
		}
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}

	private void getSelectData() {

		Visit visit = (Visit) this.getPage().getVisit();

		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("查询日期："));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");
		tbr.addField(dfb);
		tbr.addText(new ToolbarText("至"));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");
		tbr.addField(dfe);

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

		tbr.addText(new ToolbarText("-"));
		tbr.addText(new ToolbarText("电厂："));
		tbr.addField(tf);
		tbr.addItem(tb2);

		tbr.addText(new ToolbarText("-"));
		tbr.addText(new ToolbarText("发站名称："));
		ComboBox comb = new ComboBox();
		comb.setWidth(100);
		comb.setTransform("Daoz");
		comb.setId("Daoz");
		comb
				.setListeners("select :function(combo,newValue,oldValue){document.forms[0].submit();}");
		comb.setLazyRender(true);
		comb.setEditable(true);
		tbr.addField(comb);
		tbr.addText(new ToolbarText("-"));

		tbr.addText(new ToolbarText("查询方式："));
		ComboBox xiansfs = new ComboBox();
		xiansfs.setTransform("XiansfsSelect");
		xiansfs.setWidth(60);
		xiansfs
				.setListeners("select :function(xiansfs,newValue,oldValue){document.forms[0].submit();}");
		xiansfs.setLazyRender(true);
		xiansfs.setEditable(true);
		tbr.addField(xiansfs);
		tbr.addText(new ToolbarText("-"));

		tbr.addText(new ToolbarText("费用项目："));
		ComboBox fy = new ComboBox();
		fy.setTransform("Feiyxm");
		fy.setWidth(120);
		fy
				.setListeners("select :function(own,newValue,oldValue){document.forms[0].submit();}");
		fy.setLazyRender(true);
		fy.setEditable(true);
		tbr.addField(fy);
		tbr.addText(new ToolbarText("-"));

		ToolbarButton tbrtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		tbr.addFill();
		setToolbar(tbr);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		return getToolbar().getRenderScript();
	}

	// 电厂tree_Begin
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

	// 电厂tree_End

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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			this.setTreeid("" + visit.getDiancxxb_id());
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			getDiancmcModels();
			setXiansfsValue(null);
			setXiansfsModels();
			setFeiyValue(null);
			setFeiyModel(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
            // begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
		}
		getSelectData();
	}

}
