package com.zhiren.dc.yunf;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：刘雨
 * 时间：2010-11-30 
 * 描述：新增运费结算统计台帐
 */

public class Yunfjstjtz extends BasePage {
	public boolean getRaw() {
		return true;
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	//  
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		String sql = 
			"select decode(grouping(j.shoukdw), 1, '总计', j.shoukdw) as yunsdw,\n" + 
			"       decode(grouping(j.shoukdw) + grouping(j.jiesrq), 1, '小计', to_char(j.jiesrq,'yyyy-MM-dd')) as jiesrq,\n" + 
			"       to_char(j.ruzrq,'yyyy-MM-dd') as ruzrq,\n" + 
			"       j.bianm as jiesbh,\n" + 
			"       m.mingc as kuangb,\n" + 
			"       sum(j.yunj) as yunj,\n" + 
			"       sum(c.biaoz) as biaoz,\n" + 
			"       sum(c.jingz) as jingz,\n" + 
			"       sum(j.jiessl) as jiessl,\n" + 
			"       decode(sum(c.jingz),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(c.rel * c.jingz) / sum(c.jingz), 2)) as rel,\n" + 
			"       decode(sum(j.jiessl),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum(j.hansdj * j.jiessl) / sum(j.jiessl), 2)) as hansdj,\n" + 
			"       sum(j.kuidjfyf) as kuidjfyf,\n" + 
			"       decode(sum(j.jiessl),\n" + 
			"              0,\n" + 
			"              0,\n" + 
			"              round_new(sum((j.hansdj * (1 - j.shuil)) * j.jiessl) /\n" + 
			"                        sum(j.jiessl),\n" + 
			"                        2)) as buhsdj,\n" + 
			"       sum(j.hansyf) as hansyf,\n" + 
			"       sum(j.buhsyf) as buhsyf\n" + 
			"  from jiesyfb j,\n" + 
			"       meikxxb m,\n" + 
			"       (select j.id,\n" + 
			"               sum(c.biaoz) as biaoz,\n" + 
			"               sum(c.maoz - c.piz - c.zongkd) as jingz,\n" + 
			"               decode(sum(c.maoz - c.piz - c.zongkd),\n" + 
			"                      0,\n" + 
			"                      0,\n" + 
			"                      round_new(sum(z.qnet_ar * (c.maoz - c.piz - c.zongkd)) /\n" + 
			"                                sum(c.maoz - c.piz - c.zongkd),\n" + 
			"                                2)) as rel\n" + 
			"          from jiesyfb j, danjcpb d, chepb c, fahb f, zhilb z\n" + 
			"         where d.yunfjsb_id = j.id\n" + 
			"           and d.chepb_id = c.id\n" + 
			"           and c.fahb_id = f.id\n" + 
			"           and f.zhilb_id = z.id\n" + 
			"         group by j.id) c\n" + 
			" where j.meikxxb_id = m.id\n" + 
			"   and j.id = c.id\n" + 
			"   and j.diancxxb_id = " + getTreeid() + "\n" +
			"   and j.jiesrq >= to_date('"+this.getRiq1()+"', 'yyyy-MM-dd')\n" + 
			"   and j.jiesrq < to_date('"+this.getRiq2()+"', 'yyyy-MM-dd') + 1\n" + 
			" group by rollup(j.shoukdw, j.jiesrq, j.ruzrq, j.bianm, m.mingc)\n" + 
			"having not grouping(j.jiesrq) + grouping(m.mingc) = 1\n" + 
			" order by grouping(j.shoukdw) desc,\n" + 
			"          j.shoukdw,\n" + 
			"          grouping(j.jiesrq) desc,\n" + 
			"          j.jiesrq,\n" + 
			"          j.ruzrq,\n" + 
			"          j.bianm,\n" + 
			"          m.mingc";

		ResultSet rs = con.getResultSet(sql);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][15];
		ArrWidth = new int[] { 180, 75, 75, 130, 80, 30, 60, 60, 60, 40, 45, 40, 45, 65, 65};
		ArrHeader[0] = new String[] { "运输单位", "结算日期", "入账日期","结算编号","矿别","运距<br>(Km)","票重<br>(吨)","净重<br>(吨)","结算量<br>(吨)","结算<br>热量<br>(kcal/kg)","运费<br>单价<br>(含税)","亏吨<br>费用","运费<br>单价<br>(不含税)","运费<br>(元)","不含税运费<br>(元)"};
		rt.setBody(new Table(rs, 1, 0, 1));
		//
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("运 费 结 算 统 计 台 帐", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		
		rt.body.mergeFixedRowCol();//自动合并
		
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.ShowZero=true;
		
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	boolean riqichange = false;
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setRiq1(String riq) {
		if (this.riq1 != null && !this.riq1.equals(riq)) {
			this.riq1 = riq;
			riqichange = true;
		}
	}
	
	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq) {
		if (this.riq2!= null && !this.riq2.equals(riq)) {
			this.riq2 = riq;
			riqichange = true;
		}
	}
	private void getToolbars(){
		Visit visit = (Visit) this.getPage().getVisit();
		
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("结算日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ1");
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		
		tb1.addText(new ToolbarText("-"));
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null,
				getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(getTreeid() == null
						|| "".equals(getTreeid()) ? "-1" : getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
//			visit.setDropDownBean1(null);
//			visit.setProSelectionModel1(null);
//			visit.setDropDownBean2(null);
//			visit.setProSelectionModel2(null);
//			visit.setDefaultTree(null);
			setTreeid(null);
		}
		//begin方法里进行初始化设置
		visit.setString1(null);
		getToolbars();
		blnIsBegin = true;

	}
	
//	 获取供应商
	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getGongysDropDownModels() {
		// String sql="select d.id,d.mingc from diancxxb d where d.id="+((Visit)
		// getPage().getVisit()).getDiancxxb_id()+" or d.fuid="+((Visit)
		// getPage().getVisit()).getDiancxxb_id();
		String sql = "select d.id,d.mingc from diancxxb d ";
		setGongysDropDownModel(new IDropDownModel(sql, "全部"));
		return;
	}

	//	工具栏使用的方法
	//工具栏使用的方法
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
						.setValue(((IDropDownModel) getGongysDropDownModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		//	System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
