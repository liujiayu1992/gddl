package com.zhiren.gs.bjdt.pandreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Pandqkcx extends BasePage implements PageValidateListener {

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
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean _FindChick = false;

	public void FindButton(IRequestCycle cycle) {
		_FindChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_FindChick) {
			_FindChick = false;
	    }
	}

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
			return getZhiltz();
		}

	
	private StringBuffer getBaseSql(){
		StringBuffer buffer = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		String date1=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
//		String date2=getNianfValue().getValue()+"-01-01";
		buffer.append(
				"select dc.mingc, mx.zhangmkc, mx.shijkc,mx.panyk,mx.lv\n" +
				"from\n" + 
				"(select  pd.diancxxb_id, p.zhangmkc,p.shijkc shijkc,\n" + 
				"p.panyk,decode(p.shijkc,0,0, p.zhangmkc/p.shijkc) lv\n" + 
				"from pandzmm p, pandb pd\n" + 
				"where p.pandb_id=pd.id\n" + 
				"and pd.riq>=to_date('"+date1+"','yyyy-mm-dd')\n" + 
				"and pd.riq<last_day(to_date('"+date1+"','yyyy-mm-dd'))\n" + 
				")mx, dianckjpxb dp, diancxxb dc\n" + 
				"where dp.kouj='月报' and dc.id=dp.diancxxb_id\n" + 
				" and mx.diancxxb_id(+)=dp.diancxxb_id\n" + 
				"order by dp.xuh"
		);
				
		return buffer;
	}
	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		ResultSetList rs = con.getResultSetList(getBaseSql().toString());
		String[][] ArrHeader = new String[1][5];
		String[] strFormat = null;
		int[] ArrWidth = null;

			ArrHeader[0] =new String[]  { "单位","月报账面","盘点","盈亏","帐物相符率"} ;

			ArrWidth = new int[] {100,100,100,100,100};

			rt.setTitle(Local.RptTitle_Jiexyb, ArrWidth);

		rt.setTitle("盘点情况查询", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		strFormat = new String[] { "", "0", "0", "0", "0.0000" };

		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.setColFormat(strFormat);
//		for (int i = 1; i <= 5; i++) {
//			rt.body.setColAlign(i, Table.ALIGN_CENTER);
//		}

		rt.createDefautlFooter(ArrWidth);
//
		rt.setDefaultTitle(1, 3, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
//		
		rt.setDefaultTitle(	4,2,"查询日期:"+getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_RIGHT);
//		
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 5, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(6, 6, "审核:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(13, 2, "制表:", Table.ALIGN_LEFT);
		
		con.Close();
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages> 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();

	}
	
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}
	
//	 年份下拉框
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

		public boolean nianfchanged;
		public void setNianfValue(IDropDownBean Value) {
			if (_NianfValue != Value) {
				nianfchanged = true;
			}
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

		// 月份
		public boolean Changeyuef = false;
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
			long id = -2;
			if (_YuefValue!= null) {
				id = getYuefValue().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					Changeyuef = true;
				} else {
					Changeyuef = false;
				}
			}
			_YuefValue = Value;
			
		}

		public IPropertySelectionModel getYuefModels() {
			List listYuef = new ArrayList();
			for (int i = 1; i < 13; i++) {
				if(i<10){
					listYuef.add(new IDropDownBean(i, "0"+String.valueOf(i)));
				}else{
					listYuef.add(new IDropDownBean(i, String.valueOf(i)));
				}
			}
			_YuefModel = new IDropDownModel(listYuef);
			return _YuefModel;
		}

		public void setYuefModel(IPropertySelectionModel _value) {
			_YuefModel = _value;
		}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
		}
		
		blnIsBegin = true;
		getToolBars();
		getSelectData();

	}
	
	public void getSelectData() {

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
	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
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

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}
	



}
