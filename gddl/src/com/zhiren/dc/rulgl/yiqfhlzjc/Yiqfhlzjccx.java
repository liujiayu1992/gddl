package com.zhiren.dc.rulgl.yiqfhlzjc;

import java.util.Date;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Yiqfhlzjccx extends BasePage {
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
		return getSelectData();
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

	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String sql = "select jizbh from jizb where id = " + getJizb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String jiz = "";
		if (rsl.next()) {
			jiz = rsl.getString("jizbh");
		}
		
		String fenxsj = this.getRiqi();
		if (fenxsj == null || fenxsj.equals("")) {
			fenxsj = DateUtil.FormatDate(new Date());
		}
		String fenxtime = fenxsj.substring(0, 4) + "年" + fenxsj.substring(5, 7) + "月" + fenxsj.substring(8, 10) + "日";
		
		sql = "select to_char(quysj,'yyyy-mm-dd') as quysj, pingjz, diz, beiz from yiqfhlzjcb where diancxxb_id = " + getTreeid() 
				+ " and jizb_id = " + getJizb_id() + " and fenxsj = to_date('" + fenxsj + "','yyyy-mm-dd')";
		rsl = con.getResultSetList(sql);
		String quysj = "";
		String pingjz = "";
		String diz = "";
		String beiz = "";
		if (rsl.next()) {
			quysj = rsl.getString("quysj");
			pingjz = rsl.getString("pingjz");
			diz = rsl.getString("diz");
			beiz = rsl.getString("beiz");
		}
		
		String quytime = "";
		if (!quysj.equals("") && quysj != null) {
			quytime = quysj.substring(0, 4) + "年" + quysj.substring(5, 7) + "月" + quysj.substring(8, 10) + "日";
		}
		
		sql = "select mingc, kerw from yiqfhlzjcb jc, huidxxb hd where diancxxb_id = \n" + getTreeid() 
				+ " and jizb_id = " + getJizb_id() + " and fenxsj = to_date('" + fenxsj + "','yyyy-mm-dd') " +
				"and jc.huidxxb_id = hd.id";
		rsl = con.getResultSetList(sql);
		String ArrHeader[][] = new String[16][5];
		ArrHeader[0] = new String[]{"取样时间", "取样时间", "", "", quytime};
		ArrHeader[1] = new String[]{"分析时间", "分析时间", "", "", fenxtime};
		ArrHeader[2] = new String[]{"机组名称", "机组名称", "", "", jiz};
		ArrHeader[3] = new String[]{"取样地点", "取样地点", "", "", "可燃物(%)"};
		int t = 4;
		while (rsl.next()) {
			ArrHeader[t] = new String[]{rsl.getString("mingc"), rsl.getString("mingc"), "", "", rsl.getString("kerw")};
			t ++;
		}
		
		ArrHeader[t] = new String[]{"飞灰含碳量的平均值A/B", "飞灰含碳量的平均值A/B", "", "", pingjz};
		ArrHeader[t + 1] = new String[]{"底渣", "底渣", "", "", diz};			
		
		if (t + 6 < 16) {
			for (int i = t + 3; i < 15; i ++) {
				ArrHeader[i] = new String[]{"", "", "", "", ""};
			}
		}
		
//		ArrHeader[15] = new String[]{"备注", "备注", beiz, "", ""};
		ArrHeader[15] = new String[]{"备注", "备注", beiz, beiz, beiz};
		
		int[] ArrWidth = new int[]{50,50,50,50,250};
		
		Report rt = new Report();

		rt.setBody(new Table(ArrHeader,0,0,0));
		
		rt.getArrWidth(ArrWidth, Report.PAPER_A4_WIDTH);
		rt.body.setWidth(ArrWidth);
		rt.setTitle("飞灰、炉渣可燃物检验报表", ArrWidth);
		rt.body.setPageRows(21);
		
		for (int i = 1; i <= 15; i ++) {
			rt.body.mergeCell(i, 1, i, 4);
			rt.body.setRowHeight(i, 40);			
		}
		
		rt.body.setRowHeight(16, 120);		;
		rt.body.mergeRow(16);
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		rt.setDefaultTitleRight(fenxtime, 5);

		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "批准:秦晓林", Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "审核:焦瑾辉", Table.ALIGN_CENTER);
		rt.setDefautlFooter(5, 1, "分析:梁建芬", Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();

	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("分析时间:"));
		DateField df = new DateField();
		df.setValue(getRiqi());
		df.Binding("RIQI", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("RIQI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("机组:"));
		ComboBox jiz = new ComboBox();
		jiz.setTransform("JIZB_ID");
		jiz.setListeners("select:function(){document.Form0.submit();}");
		jiz.setLazyRender(true);
		jiz.setEditable(false);
		tb1.addField(jiz);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");

		tb1.addItem(tb);
		setToolbar(tb1);
	}

    //记录Jizb表ID
	public long getJizb_id() {
		return getJizValue().getId();
	}
	
	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {
		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);

			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);

			visit.setDefaultTree(null);
			setTreeid(null);
		}
		getToolbars();
		blnIsBegin = true;
	}

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

    //机组下拉框
	public IDropDownBean getJizValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getJizModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getJizModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setJizValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setJizModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getJizModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIJizModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIJizModels() {		
		String sql = "select id, jizbh from jizb order by xuh";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
	// 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql);
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
