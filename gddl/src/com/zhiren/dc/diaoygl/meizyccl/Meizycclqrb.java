package com.zhiren.dc.diaoygl.meizyccl;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Meizycclqrb extends BasePage {
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
	
	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String ArrHeader[][] = new String[1][8];
		ArrHeader[0] = new String[] { "供煤单位", "矿(场)点", "发现日期", "异常描述", "查证报告编号", "确认扣吨", "领导确认扣吨","处理结果"};

		int ArrWidth[] = new int[] { 100, 80, 80, 150, 80, 80, 80, 150};
		
		String sql = "select min(to_char(daocsj, 'dd')) as day1, max(to_char(daocsj, 'dd')) as day2, sum(yicds) as yicds, sum(lingdqrkd) as lingdqrkd" +
			 	     "  from meizycb where diancxxb_id = " + getTreeid() + " and to_char(daocsj, 'yyyy') = " + getNianf() +
			 	     "   and to_char(daocsj, 'mm') = " + getYuef();
		rsl = con.getResultSetList(sql);
		String date = "";
		double yicds = 0.0;
		double lingdqrkd = 0.0;
		if (rsl.next()) {
			date = getYuef() + "." + rsl.getString("day1") + "-" + getYuef() + "." + rsl.getString("day2");
			yicds = rsl.getDouble("yicds");
			lingdqrkd = rsl.getDouble("lingdqrkd");
		}
		
		StringBuffer buffer = new StringBuffer();		
	    buffer.append("select * from (" +
	    		      "select gys.mingc, mk.mingc as mk, to_char(daocsj, 'mm.dd') as sj, yicms, bianh, yicds, lingdqrkd, lingdyj " +
	    		      "  from meizycb yc, gongysb gys, meikxxb mk \n" +
	    		      " where diancxxb_id = " + getTreeid() + " and gongysb_id = gys.id and meikxxb_id = mk.id \n" +
	    		      "   and to_char(daocsj, 'yyyy') = '"+getNianf()+"' and to_char(daocsj, 'mm') = '"+getYuef()+"' \n" +
	    		      "union select '', '合计', '"+date+"', '', '', "+yicds+", "+lingdqrkd+", '' from dual) order by decode(mk, '合计', 1, 2) desc, sj");
	    ResultSet rs = con.getResultSet(buffer, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    
		Report rt = new Report();
 
		rt.setBody(new Table(rs, 1, 0, 1));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
		rt.setTitle(getNianf() + "年" + getYuef() + "月" + "煤质异常处理确认表", ArrWidth);
		
		String time = DateUtil.FormatDate(new Date());
		time = time.substring(0,4) + "." + time.substring(5, 7) + "." + time.substring(8, 10);
		
		rt.body.setPageRows(20);
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for (int i = 1; i <= 8; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		rsl = con.getResultSetList(buffer.toString());
		for (int i = 0; i < rsl.getRows(); i++) {
			rt.body.setRowHeight(i+2, 40);
		}
		rt.body.setRowHeight(1, 45);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(8, 1, time, Table.ALIGN_RIGHT);
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
				
		if (!rsl.next()) {
			rsl.close();
			con.Close();
			return null;
		} else {
			rsl.close();
			con.Close();
			return rt.getAllPagesHtml();
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

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){Form0.submit();}");
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

			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			
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
	
    // 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean5((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean5() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean6((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean6() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(_value);
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
