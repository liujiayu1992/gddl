package com.zhiren.jt.jihgl.ranlzhzb;

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

public class Ranlzhzbcx extends BasePage {
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
		return getRanlzhzb();
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
	
    //记录查询条件表ID
	public long getChaxtjb_id() {
		return getChaxtjValue().getId();
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
	
	private String getRanlzhzb() {
		JDBCcon con = new JDBCcon();
		
		String sql = "select nianf, beginyuef, endyuef from chaxtjb where id = " + getChaxtjb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String nianf = "";
		String beginyuef = "";
		String endyuef = "";
		if(rsl.next()) {
			nianf = rsl.getString("nianf");
			beginyuef = rsl.getString("beginyuef");
			endyuef = rsl.getString("endyuef");
		}
		String Nianf = (Integer.parseInt(nianf) + 1) + "";
		
		StringBuffer ArrHeader0 = new StringBuffer("序号,指标名称,单位,合计,合计,合计,");
		StringBuffer ArrHeader1 = new StringBuffer("序号,指标名称,单位,"+nianf+"年,"+nianf+"年,"+Nianf+"年,");
		StringBuffer ArrHeader2 = new StringBuffer("序号,指标名称,单位,"+beginyuef+"-"+endyuef+"月实际,全年预计,预算,");
		sql = "select id, mingc, piny from diancxxb where id not in (select fuid from diancxxb) order by id";
		rsl = con.getResultSetList(sql);
		
		StringBuffer sqlbuffer = new StringBuffer("select i.xuh, i.mingc, i.beiz, ");
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();
		StringBuffer buffer4 = new StringBuffer();
		StringBuffer buffer5 = new StringBuffer();
		
		while (rsl.next()) {
			for(int i = 0; i < 3; i++) {
				ArrHeader0.append(rsl.getString("mingc")).append(",");
			}
			ArrHeader1.append(""+nianf+"年,"+nianf+"年,"+Nianf+"年,");
			ArrHeader2.append(""+beginyuef+"-"+endyuef+"月实际,全年预计,预算,");	
			
			buffer.append("(select i.id, z.shijwc, a.quannyj, b.xianyj\n" +
						  "  from (select yj.item_id, yj.quannyj\n" + 
						  "          from quannyjb yj\n" + 
						  "         where yj.diancxxb_id = "+rsl.getString("id")+"\n" + 
						  "           and to_char(yj.riq, 'yyyy') = "+nianf+") a,\n" + 
						  "       (select qnyj.item_id, qnyj.quannyj as xianyj\n" + 
						  "          from quannyjb qnyj\n" + 
						  "         where qnyj.diancxxb_id = "+rsl.getString("id")+"\n" + 
						  "           and to_char(qnyj.riq, 'yyyy') = "+Nianf+") b,\n" + 
						  "       (select sj.item_id, sj.shijwc\n" + 
						  "          from shijwcb sj, chaxtjb tj\n" + 
						  "         where sj.chaxtjb_id = tj.id\n" + 
						  "           and sj.chaxtjb_id = "+getChaxtjb_id()+"\n" + 
						  "           and sj.diancxxb_id = "+rsl.getString("id")+") z,\n" + 
						  "       item i,\n" + 
						  "       itemsort it\n" + 
						  " where i.itemsortid = it.itemsortid\n" + 
						  "   and it.bianm = 'RANLZHZB'\n" + 
						  "   and i.id = a.item_id(+)\n" + 
						  "   and i.id = b.item_id(+)\n" + 
						  "   and i.id = z.item_id(+))"  + rsl.getString("piny") + ",");
			
			buffer1.append("nvl(" + rsl.getString("piny") + ".shijwc,0)+");
			buffer2.append("nvl(" + rsl.getString("piny") + ".quannyj,0)+");
			buffer3.append("nvl(" + rsl.getString("piny") + ".xianyj,0)+");
			buffer4.append(rsl.getString("piny") + ".shijwc," + rsl.getString("piny") + ".quannyj," + rsl.getString("piny") + ".xianyj,");
			buffer5.append("i.id="+rsl.getString("piny")+".id(+) and ");
		}

		buffer1.deleteCharAt(buffer1.length()-1);
		buffer2.deleteCharAt(buffer2.length()-1);
		buffer3.deleteCharAt(buffer3.length()-1);
		buffer4.deleteCharAt(buffer4.length()-1);
		buffer5.delete(buffer5.length()-4, buffer5.length()-1);
		sqlbuffer.append(buffer1).append(",").append(buffer2).append(",").append(buffer3).append(",").append(buffer4).append(" from ")
		.append(buffer).append("itemsort it, item i ").append("where i.itemsortid = it.itemsortid and it.bianm = 'RANLZHZB' and i.xuh " +
				"between 1 and 31 and ").append(buffer5).append(" order by i.xuh");
		
		// 宽度
		int[] ArrWidth = new int[6 + rsl.getRows() * 3];

		ArrWidth[0] = 50;
		ArrWidth[1] = 100;
		for(int i = 2; i < ArrHeader0.toString().split(",").length; i++) {
			ArrWidth[i] = 65;
		}

		ResultSet rs = con.getResultSet(sqlbuffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[3][6 + rsl.getRows() * 3];

		ArrHeader[0] = ArrHeader0.deleteCharAt(ArrHeader0.length() - 1).toString().split(",");

		ArrHeader[1] = ArrHeader1.deleteCharAt(ArrHeader1.length() - 1).toString().split(",");

		ArrHeader[2] = ArrHeader2.deleteCharAt(ArrHeader2.length() - 1).toString().split(",");

		Report rt = new Report();

		rt.setBody(new Table(rs, 3, 0, 0));
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("燃料综合指标表", ArrWidth);
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

		 // 年份ComBox
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setId("NIANF");// 和自动刷新绑定
		nianf.setLazyRender(true);
		nianf.setEditable(false);
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("查询条件:"));
		ComboBox chaxtj = new ComboBox();
		chaxtj.setTransform("CHAXTJB_ID");
		chaxtj.setWidth(150);
		chaxtj.setListeners("select:function(){document.Form0.submit();}");
		chaxtj.setLazyRender(true);
		chaxtj.setEditable(false);
		tb1.addField(chaxtj);
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
			
			setChaxtjModel(null);
			setChaxtjValue(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);

			visit.setDefaultTree(null);
			setTreeid(null);
		}
		
		if(((Visit) getPage().getVisit()).getboolean2()){
			setChaxtjModel(null);
			setChaxtjValue(null);
		}
		getToolbars();
		blnIsBegin = true;
		((Visit) getPage().getVisit()).setboolean2(false);
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

    //查询条件下拉框
	public IDropDownBean getChaxtjValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getChaxtjModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getChaxtjModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setChaxtjValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setChaxtjModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getChaxtjModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIChaxtjModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIChaxtjModels() {
		
		String sql = "select id, mingc from chaxtjb where nianf = " + getNianf();
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

	//public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			((Visit) getPage().getVisit()).setboolean2(true);
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
}
