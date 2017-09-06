package com.zhiren.shanxdted.zhonghtj;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public abstract class Zuhcx extends BasePage {
	
	private String OraDate(Date _date) {
		if (_date == null) {
			return  DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return  DateUtil.Formatdate("yyyy-MM-dd", _date);
	}
	
	
	
	//*********************设置文本框
	
	private String _MyID;
	public String getMyID(){
		return _MyID;
	}
	
	public void setMyID(String value){
		_MyID=value;
	}
	
	
	private String _Shifen1;
	public String getShifen1(){
		return _Shifen1;
	}
	
	public void setShifen1(String value){
		_Shifen1=value;
	}
	
	private String _Shifen2;
	public String getShifen2(){
		return _Shifen2;
	}
	
	public void setShifen2(String value){
		_Shifen2=value;
	}
	
	// ****************判断页面是否是第一次调用**************//
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
	// *****************************************条件设置开始******************************************//
	

	
	// ****************设置日期*******************//
//	绑定日期
	// 开始日期
	private Date _BeginriqValue = new Date();

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
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

		listPrint.add(new IDropDownBean(0, "="));
		listPrint.add(new IDropDownBean(1, "like"));

		_PrintModel = new IDropDownModel(listPrint);
		return _PrintModel;
	}

	public void setPrintModel(IPropertySelectionModel _value) {
		_PrintModel = _value;
	}



	// 供应商
	
	private static IPropertySelectionModel _GongysModel;

	public IPropertySelectionModel getIGongysModel() {
		if (_GongysModel == null) {
			getIGongysModels();
		}
		return _GongysModel;
	}

	private IDropDownBean _GongysValue;

	public IDropDownBean getGongysValue() {
		if (_GongysValue == null) {
			setGongysValue((IDropDownBean) getIGongysModel().getOption(0));
		}
		return _GongysValue;
	}

	private boolean _GongysChange = false;

	public void setGongysValue(IDropDownBean Value) {
		if (_GongysValue == Value) {
			_GongysChange = false;
		} else {
			_GongysValue = Value;
			_GongysChange = true;
		}
	}

	public IPropertySelectionModel getIGongysModels() {
		List list = new ArrayList();

		list.add(new IDropDownBean(1,"运输单位"));
		list.add(new IDropDownBean(2,"煤矿单位"));
		list.add(new IDropDownBean(3,"车号"));
		list.add(new IDropDownBean(4,"车型"));
		list.add(new IDropDownBean(5,"重车检斤员"));
		list.add(new IDropDownBean(6,"轻车检斤员"));
		list.add(new IDropDownBean(7,"质检员"));
		list.add(new IDropDownBean(8,"发卡人"));

		_GongysModel = new IDropDownModel(list);
		return _GongysModel;
	}

	public void setIGongysModel(IPropertySelectionModel _value) {
		_GongysModel = _value;
	}


	// 电厂
	
	private static IPropertySelectionModel  DiancModel;

	public IPropertySelectionModel getDiancSelectModel() {
		if (DiancModel == null) {
			getDiancSelectModels();
		}
		return DiancModel;
	}

	private IDropDownBean DiancValue;

	public IDropDownBean getDiancSelectValue() {
		if (DiancValue == null) {
			setDiancSelectValue((IDropDownBean) getDiancSelectModel().getOption(0));
		}
		return DiancValue;
	}

	private boolean DiancChange = false;

	public void setDiancSelectValue(IDropDownBean Value) {
		if (DiancValue == Value) {
			DiancChange = false;
		} else {
			DiancValue = Value;
			DiancChange = true;
		}
	}

	public IPropertySelectionModel getDiancSelectModels() {
	
		
		
		String sql = "select id,mingc from diancxxb d where (id="+getTreeid_dc() +" or d.fuid="+getTreeid_dc()+")";
		DiancModel = new IDropDownModel(sql);
		return DiancModel;
	}

	public void setDiancSelectModel(IPropertySelectionModel _value) {
		DiancModel = _value;
	}
	
	
	
	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
//			getIDiancmcModels();
		}
	}
	
	
//	public IPropertySelectionModel getIDiancmcModels() {
//
//		String sql = "";
//
//		if (((Visit) getPage().getVisit()).isGSUser()) {
//			// 分公司
//			sql = "select d.id,d.mingc from diancxxb d where fuid="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//					+ " order by d.mingc";
//		} else if(((Visit) getPage().getVisit()).isJTUser()){
//			// 集团
//			if (getGongsmcValue().getId() > -1) {
//
//				sql = "select d.id,d.mingc from diancxxb d where fuid="
//						+ getGongsmcValue().getId() + " order by d.mingc";
//			} else {
//
//				sql = "select d.id,d.mingc from diancxxb d where jib=3 order by d.mingc";
//			}
//		}else{
////			电厂
//			if(((Visit) getPage().getVisit()).isFencb()){
//				
////				分厂别
//				sql="select id,mingc from diancxxb where fuid="
//					+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc";
//			}else{
//				
//				sql="select id,mingc from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
//			}
//		}
//
//		((Visit) getPage().getVisit())
//				.setProSelectionModel3(new IDropDownModel(sql, "全部"));
//		return ((Visit) getPage().getVisit()).getProSelectionModel3();
//	}
	
	
	

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

		List riqlist = new ArrayList();

		riqlist.add(new IDropDownBean(0, "重车时间"));
		riqlist.add(new IDropDownBean(1, "轻车时间"));
		riqlist.add(new IDropDownBean(2, "发卡时间"));
		
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(riqlist, "select '182','全部' from dual"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
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

	
	private int zhuangt = 1;

	public void submit(IRequestCycle cycle) {

		if (_RefurbishChick) {
			_RefurbishChick = false;
			// setBianhValue(null);
			// setIBianhModel(null);
			// getIBianhModels();
			zhuangt = 2;
			getPrintTable();
		}
		if (_QuedChick) {
			zhuangt = 2;
			Refurbish();
			_QuedChick = false;
		}
		this.setWindowScript("");
	}
	
	
	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getPrintTable();
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(2);// 是第一次显示
			visit.setString2("");	//结算单编号
//			visit.setString3("");	//向下个页面传递的参数
			zhuangt = visit.getInt1();
			// System.out.println("第一次运行zhuangt="+visit.getZhuangt());
			// *************条件设置开始***************//

			((Visit) getPage().getVisit()).setString4("");	//WindowScript(页面显示)
			((Visit) getPage().getVisit()).setString5("");	//子结算单编号
			visit.setString6("");							//分公司采购结算的分公司id
			

			// 煤矿
			setGongysValue(null);
			setIGongysModel(null);
			getIGongysModels();

			//时间类型
			
			
			setGongsmcValue(null);
			setIGongsmcModel(null);
			getIGongsmcModels();
			
			//条件
			setPrintValue(null);
			setPrintModel(null);
			getPrintModels();
			
			//  文本框

			this.setMyID("");
			this.setShifen1("");
			this.setShifen2("");
			 setEndriqDate(new Date());
			 setBeginriqDate(new Date());
			 setTreeid_dc(visit.getDiancxxb_id() + "");
		}

		getPrintTable();
		
		
		// *************条件设置结束***************//
	}

	// *****************************报表数据设置*****************************//

	
//	窗口弹出js_begin
	public String getWindowScript(){
		
		return ((Visit) getPage().getVisit()).getString4();
	}
	
	public void setWindowScript(String value){
		
		((Visit) getPage().getVisit()).setString4(value);
	}
//	窗口弹出js_end





	private String nvlStr(String strValue) {
		if (strValue == null) {
			return "";
		} else if (strValue.equals("null")) {
			return "";
		}

		return strValue;
	}

	
	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		String shif1;
		String shif2;
		
 		if(this.getShifen1()==null||"".equals(this.getShifen1())){
			shif1="23:59";
		}else{
			
			shif1=this.getShifen1();
		}
		if(this.getShifen2()==null||"".equals(this.getShifen2())){
			
			shif2="00:00";
		}else{
			
			shif2=this.getShifen2();
		}
		
		buffer.append(
		"select m.mingc as meikdw,\n" +
		"       y.mingc as yunsdw,\n" + 
		"       j.cheh,\n" + 
		"		j.chex,\n"+
		"      to_char( j.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,\n" + 
		"       j.zhongcjjy,\n" + 
		"      to_char( j.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,\n" + 
		"       j.qingcjjy,\n" + 
		"       j.beiz,\n" + 
		"      to_char( j.faksj,'yyyy-mm-dd hh24:mi:ss') as faksj,\n" + 
		"       j.fakr\n" + 
		"  from jianjghb j, meikxxb m, yunsdwb y,diancxxb d\n" + 
		" where j.meikxxb_id = m.id\n" + 
		"   and j.yunsdwb_id = y.id and j.diancxxb_id=d.id and \n"+
		"decode('"+getGongsmcValue().getValue()+"','重车时间',zhongcsj,'轻车时间',qingcsj,'发卡时间',faksj)"
		+"<=to_date('"+OraDate(_EndriqValue)+" "+shif1+"','yyyy-mm-dd hh24:mi') and decode('"+getGongsmcValue().getValue()+"','重车时间',zhongcsj,'轻车时间',qingcsj,'发卡时间',faksj)>=to_date('"+OraDate(_BeginriqValue)
		+" "+shif2+"','yyyy-mm-dd hh24:mi') and decode('"+getGongysValue().getValue()+"','运输单位',y.mingc,'煤矿单位',m.mingc,'车号',cheh,'车型',chex,'重车检斤员',zhongcjjy,'轻车检斤员',qingcjjy,'发卡人',fakr,'质检员',j.beiz)"
		+getPrintValue().getValue()+"'%"+this.getMyID()+"%' and (d.id="+getDiancSelectValue().getId() +" or d.fuid="+getDiancSelectValue().getId()+") order by meikdw,yunsdw");
	
		
	
		ResultSet rs = con.getResultSet(buffer.toString());
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][11];
		ArrWidth = new int[] { 120, 120, 80,80,80,80,80,80,80,80,80};
		ArrHeader[0] = new String[] { "煤矿单位", "运输单位", "车号","车型","重车时间","重车检斤员","轻车时间","轻车检斤员"
									,"质检员","发卡时间","发卡人"};
	
		rt.setTitle("组 合 明 细 查 询", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(8, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
		
		rt.setBody(new Table(rs, 1, 0, 2));
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		rt.body.mergeFixedCols();
		
		for(int i=3;i<=rt.body.getCols();i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		
		
		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(1, 15, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
	
		con.Close();
		return rt.getAllPagesHtml();

	
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



//	-------------------------电厂Tree END-------------------------------------------------------------

	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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
}
