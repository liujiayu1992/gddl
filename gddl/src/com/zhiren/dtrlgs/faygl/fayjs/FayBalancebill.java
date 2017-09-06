package com.zhiren.dtrlgs.faygl.fayjs;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.zhiren.common.DateUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import org.apache.tapestry.contrib.palette.SortMode;

public class FayBalancebill extends BasePage {

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}

	public boolean isEditable() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setEditable(boolean editable) {
		((Visit) getPage().getVisit()).setboolean4(editable);
	}

	public boolean isEditable2() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setEditable2(boolean editable) {
		((Visit) getPage().getVisit()).setboolean5(editable);
	}

	// 格式化
	public String format(double dblValue, String strFormat) {
		DecimalFormat df = new DecimalFormat(strFormat);
		return formatq(df.format(dblValue));

	}

	public String formatq(String strValue) {// 加千位分隔符
		String strtmp = "", xiaostmp = "", tmp = "";
		int i = 3;
		if (strValue.lastIndexOf(".") == -1) {

			strtmp = strValue;
			if (strValue.equals("")) {

				xiaostmp = "";
			} else {

				xiaostmp = ".00";
			}

		} else {

			strtmp = strValue.substring(0, strValue.lastIndexOf("."));

			if (strValue.substring(strValue.lastIndexOf(".")).length() == 2) {

				xiaostmp = strValue.substring(strValue.lastIndexOf(".")) + "0";
			} else {

				xiaostmp = strValue.substring(strValue.lastIndexOf("."));
			}

		}
		tmp = strtmp;

		while (i < tmp.length()) {
			strtmp = strtmp.substring(0, strtmp.length() - (i + (i - 3) / 3))
					+ ","
					+ strtmp.substring(strtmp.length() - (i + (i - 3) / 3),
							strtmp.length());
			i = i + 3;
		}

		return strtmp + xiaostmp;
	}

	// ****************判断页面是否是第一次调用**************//
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
		zhuangt = _value;
	}

	// *****************************************条件设置开始******************************************//
	// ****************设置日期*******************//
	// 开始日期
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

	// 结束日期
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

	// 公司名称
	public IDropDownBean getGongsmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIGongsmcModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongsmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != null) {

			((Visit) getPage().getVisit()).setDropDownBean2(Value);
			getIDiancmcModels();
		}
	}

	public void setIGongsmcModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIGongsmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIGongsmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIGongsmcModels() {

		String sql = "";
		sql = "select id,mingc from diancxxb where jib=2 order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();	
	}

	// 电厂名称
	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIDiancmcModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setDiancmcValue(IDropDownBean value) {
		if (value == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(value);
		} else {
			if (((Visit) getPage().getVisit()).getDropDownBean3().getId() != value
					.getId()) {

				((Visit) getPage().getVisit()).setboolean2(true);
				((Visit) getPage().getVisit()).setDropDownBean3(value);
			} else {
				((Visit) getPage().getVisit()).setboolean2(false);
			}
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getIDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIDiancmcModels() {

		String sql = "";

		
		if (((Visit) getPage().getVisit()).isGSUser()) {
			// 分公司
			sql = "select d.id,d.mingc from diancxxb d where fuid="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ getGongsmcValue().getId()
					+ " order by d.mingc";
		} else if(((Visit) getPage().getVisit()).isJTUser()){
			// 集团
			if (getGongsmcValue().getId() > -1) {

				sql = "select d.id,d.mingc from diancxxb d where fuid="
						+ getGongsmcValue().getId() + " order by d.mingc";
			} else {

				sql = "select d.id,d.mingc from diancxxb d where jib=3 order by d.mingc";
			}
		}//else{
//			电厂
		//		sql="select id,mingc from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";

		//}

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// 供应商
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getIGongysModels()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean4() != value) {

			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean4(value);
		}
	}

	public void setIGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getIGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIGongysModels() {

		String sql = "";
		String diancxxb_id="";
		
		if (((Visit) getPage().getVisit()).isDCUser()) {
			// 电厂
			
				
			diancxxb_id=" and diancxxb_id="+String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			
			sql = " select gongysb.id,gongysb.mingc from gongysb,diancxxb,		\n"
				+ " ((select diancxxb_id,gongysb_id from diancjsmkb where jiesrq>="+this.OraDate(this.getBeginriqDate())+"	\n" 
				+ " 	and jiesrq<="+this.OraDate(this.getEndriqDate())+")		\n" 
				+ " union	\n" 
				+ " (select diancxxb_id,gongysb_id from diancjsmkb where jiesrq>="+this.OraDate(this.getBeginriqDate())+"	\n" 
				+ " 	and jiesrq<="+this.OraDate(this.getEndriqDate())+")) a 	\n"        
				+ " where gongysb.id=a.gongysb_id and diancxxb.id=a.diancxxb_id	\n"
				+ " 	"+diancxxb_id+"		\n"
				+ " order by mingc ";
		} else if (((Visit) getPage().getVisit()).isGSUser()) {
			// 公司
			
			if(this.getDiancmcValue().getId()>-1){
				
				diancxxb_id=String.valueOf(this.getDiancmcValue().getId());
			}else{
				
				diancxxb_id=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			}
			
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and diancxxb_id in (select id from diancxxb where (fuid="
					+ diancxxb_id
					+ " or id=" + diancxxb_id + "))"
					+ " and diancjsmkb.jiesrq>=" + this.OraDate(this.getBeginriqDate())
					+ " and diancjsmkb.jiesrq<=" + this.OraDate(this.getEndriqDate())
					+ " order by gongysmc";
			
		} else if (((Visit) getPage().getVisit()).isJTUser()) {
			// 集团
			
			if(this.getDiancmcValue().getId()>-1){
				
				diancxxb_id=" and diancxxb_id="+String.valueOf(this.getDiancmcValue().getId());
			}else{
				
				diancxxb_id="";
			}
			
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id order by gongysmc";
		}
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	// 打印状态
	private static IPropertySelectionModel _PrintModel;

	public IPropertySelectionModel getPrintModel() {
		if (_PrintModel == null) {
			getPrintModels();
		}
		return _PrintModel;
	}

	private IDropDownBean _PrintValue;

	public IDropDownBean getPrintValue() {
		if (_PrintValue == null) {
			setPrintValue((IDropDownBean) getPrintModel().getOption(0));
		}
		return _PrintValue;
	}

	private boolean _PrintChange = false;

	public void setPrintValue(IDropDownBean Value) {
		if (_PrintValue == Value) {
			_PrintChange = false;
		} else {
			_PrintValue = Value;
			_PrintChange = true;
		}
	}

	public IPropertySelectionModel getPrintModels() {
		List listPrint = new ArrayList();

		listPrint.add(new IDropDownBean(0, "待打印"));
		listPrint.add(new IDropDownBean(1, "已打印"));
		listPrint.add(new IDropDownBean(2, "全部"));

		_PrintModel = new IDropDownModel(listPrint);
		return _PrintModel;
	}

	public void setPrintModel(IPropertySelectionModel _value) {
		_PrintModel = _value;
	}

	// ******************编号设置*****************//
	public IDropDownBean getBianhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			if (getIBianhModel().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getIBianhModel()
								.getOption(0));
			} else {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getIBianhModel()
								.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setBianhValue(IDropDownBean Value) {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5()!= Value) {
			
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
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

		StringBuffer w_sql = new StringBuffer();
		w_sql.append(" where ");
		String beginriq = "";
		String endriq = "";
		String gongsid = "";
		String diancid = "";
		String diqumc = "";
		String printState = "";

		beginriq = " jiesrq>=" + OraDate(_BeginriqValue);// 开始日期
		endriq = " and jiesrq<=" + OraDate(_EndriqValue);// 结束日期
		w_sql.append(beginriq);
		w_sql.append(endriq);

		if (((Visit) getPage().getVisit()).getRenyjb() == 1) {

			if (getGongsmcValue() != null && getGongsmcValue().getId() != -1) {// 公司名称

				gongsid = " and diancxxb_id in (select id from diancxxb where fuid="
						+ getGongsmcValue().getId() + ")";
				w_sql.append(gongsid);
			}

			if (getDiancmcValue() != null && getDiancmcValue().getId() != -1) {// 电厂名称

				diancid = " and diancxxb_id=" + getDiancmcValue().getId();
				w_sql.append(diancid);
			}

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 2) {

			if (getDiancmcValue() != null && getDiancmcValue().getId() != -1) {// 电厂名称

				diancid = " and diancxxb_id=" + getDiancmcValue().getId();
				w_sql.append(diancid);
			} else {

				diancid = " and (diancxxb_id in (select id from diancxxb where fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id() + " or id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "))";
				w_sql.append(diancid);
			}

		} else if (((Visit) getPage().getVisit()).getRenyjb() == 3) {

			diancid = " and diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			w_sql.append(diancid);
		}

		if (getGongysValue() != null && getGongysValue().getId() != -1) {// 供应商

			diqumc = " and gongysb_id='" + getGongysValue().getId() + "'";
			w_sql.append(diqumc);
		}

		String sql = "";		
		sql = " select id,bianm from ("
				+ " select js.id,js.bianm,js.diancxxb_id,gongysb_id from "+tables.substring(0,tables.lastIndexOf(","))+" js"
				+ w_sql.toString()
				+ printState
				+ " union "
				+ " select yf.id,yf.bianm,yf.diancxxb_id,gongysb_id from "+tables.substring(tables.lastIndexOf(",")+1)+" yf "
				+ w_sql.toString() + " and yf.jieslx<>1" + " ) order by bianm";

		setIBianhModel(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
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

	// ******************按钮设置****************//
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
	
//	明晰查询
	private boolean _YansmxChick = false;

	public void YansmxButton(IRequestCycle cycle) {
		
		_YansmxChick = true;
	}

	public void submit(IRequestCycle cycle) {

		if (_RefurbishChick) {
			_RefurbishChick = false;
			chaxunzt1 = 1;// 查询状态
			zhuangt = 2;
			// setBianhValue(null);
			// setIBianhModel(null);
			// getIBianhModels();
			getSelectData();
		}
		if (_QuedChick) {
			chaxunzt1 = 1;// 查询状态
			zhuangt = 2;
			Refurbish();
			_QuedChick = false;
		}
		if (_PrintChick) {
			_PrintChick = false;
			PrintState();
		}
		this.setWindowScript("");
		if(_YansmxChick){
			_YansmxChick = false;
			Yansmx();
		}
	}
	
	private void Yansmx(){
		
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
			+ "var end = url.indexOf(';');"
			+ "url = url.substring(0,end);"
			+ "url = url + '?service=page/' + 'BalanceMingx&lx=balancemingx';"
			+ " window.open(url,'Yansmx');";
		
		((Visit) getPage().getVisit()).setInt1(2);
		zhuangt = 2;
		((Visit) getPage().getVisit()).setString10(this.getJiesbhSel());
		
		((Visit) getPage().getVisit()).setString11("changf");
		
//		((Visit) getPage().getVisit()).setboolean7(true);
		this.setWindowScript(str);
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
		// String sql = "update diancjsmkb set gongsdyzt=1 where bianm in
		// ("+where+")";
		// con.getUpdate(sql);
		con.Close();
		setBianhValue(null);
		setIBianhModel(null);
		chaxunzt1 = 1;// 查询状态
		zhuangt = 2;
		getSelectData();
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getSelectData();
	}

	

	// *****************************报表数据设置*****************************//
	private int chaxunzt1 = 0;// 查询状态

	private int zhuangt = 1;

	public String getJiesbhSel() {

		return ((Visit) getPage().getVisit()).getString1();
	}
	
//	窗口弹出js_begin
	public String getWindowScript(){
		
		return ((Visit) getPage().getVisit()).getString4();
	}
	
	public void setWindowScript(String value){
		
		((Visit) getPage().getVisit()).setString4(value);
	}
//	窗口弹出js_end

	public void setJiesbhSel(String Jiesbhsel) {
	//	if (((Visit) getPage().getVisit()).getString1() != null) {
//			if (((Visit) getPage().getVisit()).getString1().equals(Jiesbhsel)) {
//				((Visit) getPage().getVisit()).setboolean3(false);
//			} else {
//				((Visit) getPage().getVisit()).setboolean3(true);
//			}
//		} else if (Jiesbhsel != null) {
   		if (Jiesbhsel != null && !Jiesbhsel.equals("")) {
				((Visit) getPage().getVisit()).setboolean3(true);
//			}
		}
		((Visit) getPage().getVisit()).setString1(Jiesbhsel);
	}

	public String getJiesbhSel1() {

		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setJiesbhSel1(String Jiesbhsel) {
//		if (((Visit) getPage().getVisit()).getString2() != null) {
//			if (((Visit) getPage().getVisit()).getString2().equals(Jiesbhsel)) {
//				((Visit) getPage().getVisit()).setboolean3(false);
//			} else {
//				((Visit) getPage().getVisit()).setboolean3(true);
//			}
//		} else if (Jiesbhsel != null) {
//			if (!Jiesbhsel.equals("")) {
//				((Visit) getPage().getVisit()).setboolean3(true);
//			}
//		}
		((Visit) getPage().getVisit()).setString2(Jiesbhsel);
	}

	public String[] getWhere() {// 查询条件
		// 编号条件
		String bianh[];

		if (getJiesbhSel() != null && !getJiesbhSel().equals("")
				&& !getJiesbhSel().equals("请选择")) {// 结算编号
			bianh = getJiesbhSel().split(",");
		} else {
			bianh = null;
		}
		return bianh;
	}

//	private String nvlStr(String strValue) {
//		if (strValue == null) {
//			return "";
//		} else if (strValue.equals("null")) {
//			return "";
//		}
//
//		return strValue;
//	}

	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		if (chaxunzt1 == 1) {// 查询状态
			chaxunzt1 = 2;
			return "";
		} else if (chaxunzt1 == 2) {
			String bianh[] = getWhere();
			if (bianh != null) {
				if (bianh.length == 0) {
					return "";
				} else {
//					visit.setInt1(2);// 是第一次显示
					chaxunzt1 = 0;
					zhuangt = visit.getInt1();
					int k=0;
					Changfjsd jsd = new Changfjsd();
					StringBuffer sb = new StringBuffer();
//					setAllPages(bianh.length);
//					jsd.setAllPages(bianh.length);
					for (int p = 0; p < bianh.length; p++) {
						sb.append(jsd.getChangfjsd(bianh[p], p,visit.getString3()));
						k+=jsd.getAllPages();
					}
					setAllPages(k);
					_CurrentPage=1;

					return sb.toString();
				}
			} else {
				return "";
			}
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

	private String RT_HET = "jies";

	private String mstrReportName = "jies";

	public String getPrintTable() {
		if (mstrReportName.equals(RT_HET)) {
			return getSelectData();
		} else {
			return "无此报表";
		}
	}

	// ******************************其他*******************************//

	// public String getcontext() {
	// return "var context='http://"
	// + this.getRequestCycle().getRequestContext().getServerName()
	// + ":"
	// + this.getRequestCycle().getRequestContext().getServerPort()
	// + ((Visit) getPage().getVisit()).getContextPath() + "';";
	// }

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			// _pageLink = "window.location.target = '_blank';"+_pageLink;
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
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

	public String getTianzdw(String jiesdbh) {
		String Tianzdw = "";
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select quanc from diancxxb where id=(select diancxxb_id from diancjsmkb where bianm='"
					+ jiesdbh + "')";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				Tianzdw = rs.getString("quanc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return Tianzdw;
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			// return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
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
	
	
//	 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		} else {
			visit.setboolean1(false);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())&&
			(!visit.getActivePageName().toString().equals("BalanceMingx") /*|| visit.getboolean7()*/)) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(2);// 是第一次显示
			visit.setString2("");	//结算单编号
//			visit.setString3("");	//向下个页面传递的参数
			zhuangt = visit.getInt1();
			// System.out.println("第一次运行zhuangt="+visit.getZhuangt());
			chaxunzt1 = 0;
			// *************条件设置开始***************//
			_BeginriqValue = getMonthFirstday(new Date());
			_EndriqValue = new Date();
			visit.setboolean2(false);// 共用 ;
			visit.setboolean3(false);// 结算单
			visit.setboolean4(false);// 公司显示
			visit.setboolean5(false);// 电厂显示
//			visit.setboolean7(false);//解决：发运结算单、收煤结算单之间，当点击某一个页的验收明细，再点击另一个页时出错的问题
			((Visit) getPage().getVisit()).setString3("");

			((Visit) getPage().getVisit()).setString4("");	//WindowScript(页面显示)
			
//			判断是不是火运汽运查询
			if(cycle.getRequestContext().getParameters("lx") !=null) {
				
				((Visit) getPage().getVisit()).setString3("");
				((Visit) getPage().getVisit()).setString3(cycle.getRequestContext().getParameters("lx")[0]);
	        }
			// 集团
			if (visit.getRenyjb() < 2) {

				visit.setboolean4(true);
				visit.setboolean5(true);
				setGongsmcValue(null);
				setIGongsmcModel(null);
				getIGongsmcModels();
			}
			// 分公司
			if (visit.getRenyjb() < 3) {
				visit.setboolean5(true);
				setDiancmcValue(null);
				setIDiancmcModel(null);
				getIDiancmcModels();
				
			}
			
//			电厂
			if(visit.isDCUser()&&visit.isFencb()){
//				即是电厂用户又是分厂别
				visit.setboolean5(true);	//显示电厂下拉框
			}

			// 供应商
			setGongysValue(null);
			setIGongysModel(null);
			getIGongysModels();

			// 打印状态
			// 编号
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels(((Visit) getPage().getVisit()).getString3());

			setJiesbhSel(null);
			setJiesbhSel1(null);
		}
		
		

		if (visit.getboolean2()) {// 公司名称更变时
			visit.setboolean2(false);
			setBianhValue(null);
			setIBianhModel(null);
//			getIBianhModels(((Visit) getPage().getVisit()).getString3());
		}

		if (_BeginriqChange) {
			_BeginriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIGongysModels();//刷新div 供应商
		}

		if (_EndriqChange) {
			_EndriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIGongysModels();//刷新div 供应商
		}
		if (_PrintChange) {
			_PrintChange = false;
			setBianhValue(null);
			setIBianhModel(null);
		}
		if (visit.getboolean3()) {
			visit.setboolean3(false);
			// if(getJiesbhSel()!=null){
			chaxunzt1 = 1;// 查询状态
			zhuangt = 2;
			getIBianhModels(((Visit) getPage().getVisit()).getString3());
//			this.setJiesbhSel("请选择");
			Refurbish();
			// }
		}
		if (zhuangt == 1) {// 不要动
			visit.setInt1(1);
		}
		if (zhuangt == 2) {// 不要动
			visit.setInt1(2);
		}
	
//		zhuangt = 1;

		// *************条件设置结束***************//
	}
}