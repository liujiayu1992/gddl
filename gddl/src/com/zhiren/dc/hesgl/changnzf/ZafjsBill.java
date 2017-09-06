package com.zhiren.dc.hesgl.changnzf;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;

import com.zhiren.main.Visit;

/**
 * @author 尹佳明
 * 2009-06-23
 * 类名：ZafjsBill(杂费结算单)
 */ 

public class ZafjsBill extends BasePage {

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}

//	判断页面是否是第一次调用
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

//*****************************************条件设置开始******************************************//
//	开始日期
	private Date _BeginriqValue = getMonthFirstday(new Date());

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = getMonthFirstday(new Date());
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange = false;
		} else {

			_BeginriqValue = _value;
			_BeginriqChange = true;
		}
	}

//	结束日期
	private Date _EndriqValue = new Date();

	private boolean _EndriqChange = false;

	public Date getEndriqDate() {
		if (_EndriqValue == null) {
			_EndriqValue = new Date();
		}
		return _EndriqValue;
	}

	public void setEndriqDate(Date _value) {
		if (FormatDate(_EndriqValue).equals(FormatDate(_value))) {
			_EndriqChange = false;
		} else {
			_EndriqValue = _value;
			_EndriqChange = true;
		}
	}

//	收款单位
	public IDropDownBean getShoukdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getShoukdwModels().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setShoukdwValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean4() != value) {
			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean4(value);
		}
	}

	public void setShoukdwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getShoukdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getShoukdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getShoukdwModels() {
		String sql = "select id, mingc from shoukdw";
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

//	编号下拉框
	public IDropDownBean getBianhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getIBianhModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setBianhValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean5()!= Value) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
			((Visit) getPage().getVisit()).setboolean3(true);
		}
	}

	public void setIBianhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIBianhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIBianhModels(String tables) {
		
		String shoukdw_id;
		if (getShoukdwValue().getValue().equals("全部")) {
			shoukdw_id = "";
		} else {
			shoukdw_id = " and shoukdw_id = '" + this.getShoukdwValue().getStrId() + "'";
		}

		String sql1 = "select id, bianm from zafjsb where riq between "+ this.OraDate(this.getBeginriqDate()) 
		+" and "+ this.OraDate(this.getEndriqDate()) + shoukdw_id;
		setIBianhModel(new IDropDownModel(sql1, "请选择"));
		setIBianhModel1(new IDropDownModel(sql1, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

//	弹出框里的编号下拉框
	public boolean BianhChange1 = false;

	public IDropDownBean getBianhValue1() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean) getIBianhModel1().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setBianhValue1(IDropDownBean Value) {
		if (Value == null) {
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		} else {
			if (((Visit) getPage().getVisit()).getDropDownBean1().getId() == Value.getId()) {
				BianhChange1 = false;
			} else {
				((Visit) getPage().getVisit()).setDropDownBean1(Value);
				BianhChange1 = true;
			}
		}
	}

	public void setIBianhModel1(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIBianhModel1() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	 *****************************************条件设置结束******************************************
	
	
//	 ***************设置消息框******************
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

//	 ***************按钮设置********************
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		_QuedChick = true;
	}

	private boolean _PrintChick = false;

	public void PrintButton(IRequestCycle cycle) {
		_PrintChick = true;
	}
	
	public void submit(IRequestCycle cycle) {

		if (_RefurbishChick) {
			_RefurbishChick = false;
			chaxunzt = 1; // 查询状态
			zhuangt = 2; // 状态
			getSelectData();
		}
		if (_QuedChick) {
			chaxunzt = 1;
			zhuangt = 2;
			Refurbish();
			_QuedChick = false;
		}
		if (_PrintChick) {
			_PrintChick = false;
			PrintState();
		}
		this.setWindowScript("");
	}
	
	private void PrintState() {
		JDBCcon con = new JDBCcon();

		String bianh[] = getWhere();
		String where = "";
		for (int i = 0; i < bianh.length; i++) {
			if (where.equals("")) {
				where = "'" + bianh[i] + "'";
			} else {
				where = where + ",'" + bianh[i] + "'";
			}
		}
		con.Close();
		setBianhValue(null);
		setIBianhModel(null);
		chaxunzt = 1;// 查询状态
		zhuangt = 2;
		getSelectData();
	}

	private void Refurbish() {
		getSelectData();
	}

	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		} else {
			visit.setboolean1(false);
		}
		if (!visit.getActivePageName().equals(getPageName().toString())) {

			visit.setActivePageName(getPageName().toString());
			visit.setInt1(2); // 是第一次显示
			visit.setString1("");
			visit.setString2(""); // 结算单编号
			visit.setString3(""); // 向下个页面传递的参数
			zhuangt = visit.getInt1(); // 状态
			chaxunzt = 0; // 查询状态
			_BeginriqValue = getMonthFirstday(new Date());
			_EndriqValue = new Date();
			visit.setboolean2(false); // 共用
			visit.setboolean3(false); // 结算单编号是否改变

			((Visit) getPage().getVisit()).setString4(""); //WindowScript(页面显示)
			
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
			
			setBianhValue1(null);
			setIBianhModel1(null);
		}

		if (_BeginriqChange) {
			_BeginriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}

		if (_EndriqChange) {
			_EndriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}
		
		if (visit.getboolean2()) {
			visit.setboolean2(false);
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}
		
		if (visit.getboolean3()) {
			visit.setboolean3(false);
			zhuangt = 2;
			chaxunzt = 1; // 查询状态
			Refurbish();
		}
		
		if (zhuangt == 1) {
			visit.setInt1(1);
		}
		
		if (zhuangt == 2) {
			visit.setInt1(2);
		}
	}

	// *****************************报表数据设置*****************************//
	private int chaxunzt = 0;// 查询状态

	private int zhuangt = 1;

//	窗口弹出js_begin
	public String getWindowScript(){
		return ((Visit) getPage().getVisit()).getString4();
	}
	
	public void setWindowScript(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
//	窗口弹出js_end
	
	
//	结算单编号文本框
	public String getJiesbhSel() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setJiesbhSel(String Jiesbhsel) {
		if (((Visit) getPage().getVisit()).getString1() != null) {
			if (((Visit) getPage().getVisit()).getString1().equals(Jiesbhsel)) {
				((Visit) getPage().getVisit()).setboolean3(false);
			} else {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		} else if (Jiesbhsel != null) {
			if (!Jiesbhsel.equals("")) {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		}
		((Visit) getPage().getVisit()).setString1(Jiesbhsel);
	}

//	弹出窗口里的结算单编号
	public String getJiesbhSel1() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setJiesbhSel1(String Jiesbhsel) {
		if (((Visit) getPage().getVisit()).getString2() != null) {
			if (((Visit) getPage().getVisit()).getString2().equals(Jiesbhsel)) {
				((Visit) getPage().getVisit()).setboolean3(false);
			} else {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		} else if (Jiesbhsel != null) {
			if (!Jiesbhsel.equals("")) {
				((Visit) getPage().getVisit()).setboolean3(true);
			}
		}
		((Visit) getPage().getVisit()).setString2(Jiesbhsel);
	}

	public String[] getWhere() { // 查询条件
		String[] bianh;

		if (getJiesbhSel() != null && !getJiesbhSel().equals("")
				&& !getJiesbhSel().equals("请选择")) { // 结算编号
			bianh = getJiesbhSel().split(",");
		} else {
			bianh = null;
		}
		return bianh;
	}

	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		int k = 0;
		if (chaxunzt == 1) { // 查询状态
			chaxunzt = 2;
			return "";
		} else if (chaxunzt == 2) {
			String[] bianh = getWhere();
			if (bianh != null) {
				if (bianh.length == 0) {
					return "";
				} else {
					visit.setInt1(2);// 是第一次显示
					chaxunzt = 0;
					zhuangt = 1;
					
					Zafjsd jsd = new Zafjsd();
					StringBuffer sb = new StringBuffer();
					for (int p = 0; p < bianh.length; p++) {
						sb.append(jsd.getZafjsd(bianh[p], p,visit.getString3()));
						k += jsd.getAllPages();
					}
					setAllPages(k);
					_CurrentPage = 1;
					return sb.toString();
				}
			} else {
				setAllPages(k);
				_CurrentPage = 0;
				return "";
			}
		} else {
			setAllPages(k);
			_CurrentPage = 0;
			return "";
		}
	}

//	***************************报表初始设置***************************
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

	public String getPrintTable() {
		return getSelectData();
	}

//	******************************其他*******************************
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

//	Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+ getpageLinks() + "','');";
		} else {
			return "";
		}
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
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
}