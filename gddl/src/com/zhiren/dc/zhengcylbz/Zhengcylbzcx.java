package com.zhiren.dc.zhengcylbz;

import java.sql.ResultSet;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhengcylbzcx extends BasePage {
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

	// 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean3() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
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

	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

	public String getYuef() {
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		return StrMonth;
	}
    
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		String riq = DateUtil.FormatOracleDate(getNianf() + "-01" + "-01");
		String cnDate = getNianf() + "年" + getYuef() + "月";

		String ArrHeader[][] = new String[3][10];

		ArrHeader[0] = new String[] { "单位名称", "争创一流指标", "争创一流指标", "争创一流指标",
				"争创一流指标", "争创一流指标", "争创一流指标", "争创一流指标", "争创一流指标", "争创一流指标" };

		ArrHeader[1] = new String[] { "单位名称", "不含税标煤单价（元/吨）", "不含税标煤单价（元/吨）",
				"不含税标煤单价（元/吨）", "煤质（mj/kg）", "煤质（mj/kg）", "煤质（mj/kg）",
				"热值差（kj/kg）", "热值差（kj/kg）", "热值差（kj/kg）" };

		ArrHeader[2] = new String[] { "单位名称", "标准", "实际完成", "得分", "标准", "实际完成",
				"得分", "标准", "实际完成", "得分" };

		int ArrWidth[] = new int[] { 125, 100, 100, 60, 100, 100, 60, 100, 100,
				60 };
		String[] strFormat = new String[] { "", "0.00", "0.00", "0.00", "0.00",
				"0.00", "0.00", "0.00", "0.00", "0.00" };

		StringBuffer buffer = new StringBuffer();		
		
		String sql = "select d.mingc, nvl(a.biaoz,0) as bmdjbz, Round_new(nvl(a.shijwc,0),3) as bmdjwc, Round_new(nvl(a.def,0),3) as bmdjdf, nvl(b.biaoz,0) as mzbz, Round_new(nvl(b.shijwc,0),3) " +
		"as mzwc, Round_new(nvl(b.def,0),3) as mzdf, nvl(c.biaoz,0) as rzcbz, Round_new(nvl(c.shijwc,0),3) as rzcwc, Round_new(nvl(c.def,0),3) as rzcdf\n" +
		"from\n" + 
		"(select wc.id as bhsbmdj_id, d.*, wc.biaoz, wc.shijwc, wc.def from zhengcylbzbwcqkb wc, zhengcylbzbhzb hz, item i, diancxxb d where wc.zhengcylbzbhzb_id = hz.id and\n" + 
		"wc.zhengcylzb_item_id = i.id and i.bianm = 'bhsbmdj' and hz.diancxxb_id = d.id and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+") and hz.riq = "+riq+") a,\n" + 
		"(select wc.id as mz_id, d.*, wc.biaoz, wc.shijwc, wc.def from zhengcylbzbwcqkb wc, zhengcylbzbhzb hz, item i, diancxxb d where wc.zhengcylbzbhzb_id = hz.id and\n" + 
		"wc.zhengcylzb_item_id = i.id and i.bianm = 'mz' and hz.diancxxb_id = d.id and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+") and hz.riq = "+riq+") b,\n" + 
		"(select wc.id as rzc_id, d.*,wc.biaoz, wc.shijwc, wc.def from zhengcylbzbwcqkb wc, zhengcylbzbhzb hz, item i, diancxxb d where wc.zhengcylbzbhzb_id = hz.id and\n" + 
		"wc.zhengcylzb_item_id = i.id and i.bianm = 'rzc' and hz.diancxxb_id = d.id and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+") and hz.riq = "+riq+") c,\n" + 
		"(select * from diancxxb order by jib) d\n" + 
		"where d.id=a.id(+) and d.id=b.id(+) and d.id=c.id(+) and (d.id = "+getTreeid()+" or d.fuid = "+getTreeid()+")";
	
	    buffer.append(sql);
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		Report rt = new Report();
 
		rt.setBody(new Table(rs, 3, 0, 1));
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.setTitle(cnDate + "争创一流标准表", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt
				.setDefaultTitle(1, 5, "填报单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "主管领导:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 2, "制表:", Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
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

		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
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

			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);

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
