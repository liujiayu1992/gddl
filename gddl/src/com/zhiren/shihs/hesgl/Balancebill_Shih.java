package com.zhiren.shihs.hesgl;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.zhiren.common.DateUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.contrib.palette.SortMode;

/**
 * @author zsj
 *
 */
public class Balancebill_Shih extends BasePage {

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
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " order by d.mingc";
		} else if(((Visit) getPage().getVisit()).isJTUser()){
			// 集团
			if (getGongsmcValue().getId() > -1) {

				sql = "select d.id,d.mingc from diancxxb d where fuid="
						+ getGongsmcValue().getId() + " order by d.mingc";
			} else {

				sql = "select d.id,d.mingc from diancxxb d where jib=3 order by d.mingc";
			}
		}else{
//			电厂
			if(((Visit) getPage().getVisit()).isFencb()){
				
//				分厂别
				sql="select id,mingc from diancxxb where fuid="
					+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc";
			}else{
				
				sql="select id,mingc from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
			}
		}

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
			
			if(((Visit) getPage().getVisit()).isFencb()){
				
				if(this.getDiancmcValue().getId()>-1){
					
					diancxxb_id=" and diancxxb_id="+String.valueOf(this.getDiancmcValue().getId());
				}else{
					
					diancxxb_id=" and dianxxb.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
				}
			}else{
				
				diancxxb_id=" and diancxxb_id="+String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			}
			
			sql ="select * from\n" +
				"(select gys.id,gys.mingc from shihjsb js,shihgysb gys\n" + 
				"       where js.shihgysb_id=gys.id\n" + 
				"             and js.jiesrq>="+OraDate(this.getBeginriqDate())+"\n" + 
				"             and js.jiesrq<="+OraDate(this.getEndriqDate())+"\n" + 
				"             "+diancxxb_id+"\n" + 
				"union\n" + 
				"select gys.id,gys.mingc from shihjsyfb js,gongysb gys\n" + 
				"       where js.shihgysb_id=gys.id\n" + 
				"             and js.jiesrq>="+OraDate(this.getBeginriqDate())+"\n" + 
				"             and js.jiesrq<="+OraDate(this.getEndriqDate())+"\n" + 
				"             "+diancxxb_id+")\n" + 
				"order by mingc";

		} else if (((Visit) getPage().getVisit()).isGSUser()
				||((Visit) getPage().getVisit()).isJTUser()) {
			// 公司
			
			if(this.getDiancmcValue().getId()>-1){
				
				diancxxb_id=" and dc.id="+String.valueOf(this.getDiancmcValue().getId());
			}else{
				
				diancxxb_id=" and dc.fuid="+String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			}
			
			sql ="select * from\n" +
				"(select gys.id,gys.mingc from shihjsb js,shihgysb gys,diancxxb dc\n" + 
				"       where js.shihgysb_id=gys.id\n" + 
				"             and js.diancxxb_id=dc.id\n" + 
				"             and js.jiesrq>="+OraDate(this.getBeginriqDate())+"\n" + 
				"             and js.jiesrq<="+OraDate(this.getEndriqDate())+"\n" + 
				"             "+diancxxb_id+"\n" + 
				"union\n" + 
				"select gys.id,gys.mingc from shihjsyfb js,gongysb gys,diancxxb dc\n" + 
				"       where js.shihgysb_id=gys.id\n" + 
				"             and js.diancxxb_id=dc.id\n" + 
				"             and js.jiesrq>=to_date('2008-1-1','yyyy-MM-dd')\n" + 
				"             and js.jiesrq<=to_date('2009-1-1','yyyy-MM-dd')\n" + 
				"             "+diancxxb_id+")\n" + 
				"order by mingc";

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
			((Visit) getPage().getVisit()).setboolean3(true);
		}
	}

	public void setIBianhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getIBianhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIBianhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IPropertySelectionModel getIBianhModels() {

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

				diancid = " and diancxxb_id in (select id from diancxxb where fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id() + ")";
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
				+ " select js.id,js.bianm,js.diancxxb_id,shihgysb_id from shihjsb js"
				+ w_sql.toString()
				+ printState
				+ " union "
				+ " select yf.id,yf.bianm,yf.diancxxb_id,shihgysb_id from shihjsyfb yf "
				+ w_sql.toString() + " and yf.jieslx<>1" + " ) order by bianm";

		setIBianhModel(new IDropDownModel(sql, "请选择"));
		setIBianhModel1(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	// *****************************************条件设置结束******************************************//
	public boolean BianhChange1 = false;

	public IDropDownBean getBianhValue1() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			if (getIBianhModel1().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean1((IDropDownBean) getIBianhModel1()
								.getOption(0));
			} else {
				((Visit) getPage().getVisit())
						.setDropDownBean1((IDropDownBean) getIBianhModel1()
								.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setBianhValue1(IDropDownBean Value) {
		if (Value == null) {
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		} else {
			if (((Visit) getPage().getVisit()).getDropDownBean1().getId() == Value
					.getId()) {
				BianhChange1 = false;
			} else {
				((Visit) getPage().getVisit()).setDropDownBean1(Value);
				BianhChange1 = true;
			}
		}
	}

	// private IPropertySelectionModel _IBianhModel;

	public void setIBianhModel1(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIBianhModel1() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIBianhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
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
			visit.setString3("");	//向下个页面传递的参数
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
			
			// 集团
			if (visit.isJTUser()) {

				visit.setboolean4(true);
				visit.setboolean5(true);
			}
			// 分公司
			if (visit.isGSUser()) {
				
				visit.setboolean5(true);
			}
			
//			电厂
			if(visit.isDCUser()&&visit.isFencb()){
//				即是电厂用户又是分厂别
				visit.setboolean5(true);	//显示电厂下拉框
			}

			// 供应商
			setGongysValue(null);
			setIGongysModel(null);
			
//			电厂
			setDiancmcValue(null);
			setIDiancmcModel(null);
			
//			 编号
			setBianhValue(null);
			setIBianhModel(null);
			
			getIDiancmcModels();
			getIGongysModels();
			getIBianhModels();

			setJiesbhSel(null);
			setJiesbhSel1(null);
		}

		if (visit.getboolean2()) {// 公司名称更变时
			visit.setboolean2(false);
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
		}

		if (_BeginriqChange) {
			_BeginriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
		}

		if (_EndriqChange) {
			_EndriqChange = false;
			setBianhValue(null);
			setIBianhModel(null);
			getIBianhModels();
		}
		if (_PrintChange) {
			_PrintChange = false;
			setBianhValue(null);
			setIBianhModel(null);
		}
		// if(BianhChange){
		// BianhChange = false;
		// chaxunzt1 = 1;// 查询状态
		// zhuangt=2;
		// Refurbish();
		// }
		if (visit.getboolean3()) {
			visit.setboolean3(false);
			// if(getJiesbhSel()!=null){
			chaxunzt1 = 1;// 查询状态
			zhuangt = 2;
			Refurbish();
			// }
		}
		if (zhuangt == 1) {// 不要动
			visit.setInt1(1);
		}
		if (zhuangt == 2) {// 不要动
			visit.setInt1(2);
		}
		zhuangt = 1;

		// *************条件设置结束***************//

	}

	// *****************************报表数据设置*****************************//
	private int chaxunzt1 = 0;// 查询状态

	private int zhuangt = 1;

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

	private String nvlStr(String strValue) {
		if (strValue == null) {
			return "";
		} else if (strValue.equals("null")) {
			return "";
		}

		return strValue;
	}

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
					visit.setInt1(2);// 是第一次显示
					chaxunzt1 = 0;
					zhuangt = 1;
					StringBuffer sb = new StringBuffer();
					setAllPages(bianh.length);
					for (int p = 0; p < bianh.length; p++) {
						
						JDBCcon con=new JDBCcon();
						Report rt=new Report();
						
						 String sql=""; 
						 String strjiesrq="";
						 String strfahdw="";
						 String strfaz="";
						 String strdiabch="";
						 long 	lgdiancxxb_id=0;
						 String strbianh="";
						 String strfahksrq="";
						 String strfahjzrq="";
						 String strfahrq = "";
						 String strdiqdm = "";
						 String stryuanshr = "";
						 String strshoukdw = "";
						 String strkaihyh = "";
						 String strkaisysrq="";
						 String strjiezysrq="";
						 String stryansrq = "";
						 String strhuowmc = "";
						 String strxianshr = "";
						 String stryinhzh = "";
						 String strfahsl = "";
						 String strches = "";
						 String stryansbh = "";
						 String strfapbh = "";
						 String strduifdd = "";
						 String strfukfs = "";
						 String strshijfk = "";
						 
						 String strhetbz_sl="";
						 String strgongfbz_sl = "";
						 String strchangfys_sl = "";
						 String strjiesbz_sl="";
						 String strxiancsl_sl = "";
						 String strzhejbz_sl = "";
						 String strzhehje_sl = "";
						 
						 String strhetbz_CaO="";
						 String strgongfbz_CaO = "";
						 String strchangfys_CaO = "";
						 String strjiesbz_CaO="";
						 String strxiancsl_CaO = "";
						 String strzhejbz_CaO = "";
						 String strzhehje_CaO = "";
						 
						 String strhetbz_MgO = "";
						 String strgongfbz_MgO = "";
						 String strchangfys_MgO = "";
						 String strjiesbz_MgO="";
						 String strxiancsl_MgO = "";
						 String strzhejbz_MgO = "";
						 String strzhehje_MgO = "";
						 
						 String strhetbz_Xid="";
						 String strgongfbz_Xid="";
						 String strchangfys_Xid="";
						 String strjiesbz_Xid="";
						 String strxiancsl_Xid="";
						 String strzhejbz_Xid="";
						 String strzhehje_Xid="";
						 
						 String strdanj = "";
						 String strbuhsdj = "";
						 String strjine = "";
						 String strbukouqjk = "";
						 String strjiakhj = "";
						 String strshuil_mk = "";
						 String strshuik_mk = "";
						 String strjialhj = "";
						 String strtielyf = "";
						 String strtielzf = "";
						 String strkuangqyf = "";
						 String strkuangqzf = "";
						 String strbukouqzf = "";
						 String strjiskc = "";
						 String strbuhsyf = "";
						 String strshuil_ys = "";
						 String strshuik_ys = "";
						 String stryunzshj = "";
						 String strhej_dx = "";
						 String strhej_xx = "";
						 String strbeiz = "";
						 String strguohzl = "";
						 String stryuns = "";
						 String strranlbmjbr=" ";
						 String strranlbmjbrq="";
						 String strchangcwjbr=" ";
						 String strchangcwjbrq="";
						 String strzhijzxjbr=" ";
						 String strzhijzxjbrq="";
						 String strlingdqz=" ";
						 String strlingdqzrq="";
						 String strzonghcwjbr=" ";
						 String strzonghcwjbrq="";
						 String strmeikhjdx="";
						 String stryunzfhjdx="";
						 String strJihkj="";
						 double danjc = 0;
						 String stryunsfs="";	//运输方式
						 // 
						 
					try{
//						
						 double dblMeik =0;
						 double dblYunf =0;
						 sql="select * from shihjsb where bianm='"+bianh[p]+"'"; 
						 ResultSet rs = con.getResultSet(sql);
						 
						 int intLeix=3;
						 long intDiancjsmkId=0;
						 long strkuangfjsmkb_id = -1;
						 boolean blnHasMeik =false;
						 
							if(rs.next()){
								 
//							 danjc = rs.getDouble("danjc");
								 lgdiancxxb_id=rs.getLong("diancxxb_id");
								 strbianh=rs.getString("bianm");
								 strjiesrq=FormatDate(rs.getDate("jiesrq"));
								 intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
								 intDiancjsmkId =rs.getInt("id");//煤款id
								 strfahdw=rs.getString("gongysmc");
								 strfahksrq=rs.getString("fahksrq");
								 strfahjzrq=rs.getString("fahjzrq");
								 if(strfahksrq.equals(strfahjzrq)){
									 strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
								 }else{
									 strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
								 }
								 strfaz=rs.getString("faz");
								 strdiabch=rs.getString("daibch");
								 stryuanshr = rs.getString("yuanshr");//原收货人
								 strshoukdw = rs.getString("shoukdw");//收款单位
								 strkaihyh = rs.getString("kaihyh");//开户银行
								 strkaisysrq=rs.getString("yansksrq");
								 strjiezysrq=rs.getString("yansjzrq");
//								 strJihkj=MainGlobal.getTableCol("jihkjb", "mingc", "id", rs.getString("jihkjb_id"));
								 
								 if(strkaisysrq.equals(strjiezysrq)){
									 stryansrq=FormatDate(rs.getDate("yansksrq"));
								 }else{
									 stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
								 }
								 strhuowmc = rs.getString("MEIZ");//货物名称
								 strxianshr = rs.getString("xianshr");//现收货人
								 stryinhzh = rs.getString("zhangh");//帐号
								 strches = rs.getString("ches");//车数
//								 stryansbh = rs.getString("yansbh");//验收编号
								 strfapbh = rs.getString("fapbh");//发票编号
								 strduifdd = rs.getString("duifdd");//兑付地点
								 strfukfs =rs.getString("fukfs") ;//付款方式
								 strshijfk =rs.getString("hansdj");//实际付款
								 strbuhsdj=rs.getString("buhsdj");	//不含税单价
//								 stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
								 
								 sql="select shihjszbsjb.*,zhibb.bianm as mingc from shihjszbsjb,shihjsb,zhibb "
									 + " where shihjszbsjb.jiesdid=shihjsb.id and zhibb.id=shihjszbsjb.zhibb_id"
							        + " and shihjsb.bianm='"+bianh[p]+"' and shihjszbsjb.zhuangt=1";
								 
								 ResultSet rs2=con.getResultSet(sql);
								 while(rs2.next()){
									 
									 if(rs2.getString("mingc").equals(Locale.jiessl_zhibb)){
										 
										 strhetbz_sl = rs2.getString("hetbz");		//合同标准
										 strgongfbz_sl = rs2.getString("gongf");	//供方数量
										 strfahsl=strgongfbz_sl;
										 strchangfys_sl = rs2.getString("CHANGF");	//验收数量
										 strjiesbz_sl = rs2.getString("JIES");		//结算数量
										 strxiancsl_sl = String.valueOf((rs2.getDouble("YINGK")>0?(-rs2.getDouble("YINGK")):0));//亏吨数量
										 strzhejbz_sl = rs2.getString("ZHEJBZ");	//折价标准
										 strzhehje_sl = rs2.getString("ZHEJJE");	//折合金额
										 
									 }else if(rs2.getString("mingc").equals(Locale.CaO_zhibb)){
										 
										 strhetbz_CaO = rs2.getString("hetbz");
										 strgongfbz_CaO = String.valueOf(rs2.getDouble("GONGF"));	    //供方钙
										 strchangfys_CaO = String.valueOf(rs2.getDouble("CHANGF"));		//验收钙
										 strjiesbz_CaO = String.valueOf(rs2.getDouble("jies"));			//结算钙
										 strxiancsl_CaO = rs2.getString("YINGK"); 		//相差数量钙
										 strzhejbz_CaO = rs2.getString("ZHEJBZ");		//折价标准钙
										 strzhehje_CaO = rs2.getString("ZHEJJE");		//折合金额钙
										 
									 }else if(rs2.getString("mingc").equals(Locale.MgO_zhibb)){
										 
										 strhetbz_MgO=rs2.getString("hetbz");
										 strgongfbz_MgO = rs2.getString("GONGF");	//供方标准镁
										 strchangfys_MgO = rs2.getString("CHANGF");	//验收镁
										 strjiesbz_MgO=rs2.getString("jies");		//结算镁
										 strxiancsl_MgO = rs2.getString("YINGK");	//相差数量镁
										 strzhejbz_MgO = rs2.getString("ZHEJBZ");	//折价标准镁
										 strzhehje_MgO = rs2.getString("ZHEJJE");	//折合金额镁
										 
									 }else if(rs2.getString("mingc").equals(Locale.Xid_zhibb)){
										 
										 strhetbz_Xid=rs2.getString("hetbz");
										 strgongfbz_Xid = rs2.getString("GONGF");	//供方标准细度
										 strchangfys_Xid = rs2.getString("CHANGF");	//验收细度
										 strjiesbz_Xid=rs2.getString("jies");		//结算细度
										 strxiancsl_Xid = rs2.getString("YINGK");	//相差数量细度
										 strzhejbz_Xid = rs2.getString("ZHEJBZ");	//折价标准细度
										 strzhehje_Xid = rs2.getString("ZHEJJE");	//折合金额细度
										 
									 }
								 }
								 rs2.close();

								 //********************其他*****************//
								 strdanj = rs.getString("hansdj");		//单价
								 strjine = rs.getString("meikje");		//金额
								 strbukouqjk = rs.getString("bukk");	//补(扣)以前价款
								 strjiakhj = rs.getString("buhsje");	//价款合计
								 strshuil_mk = rs.getString("shuil");	//税率(煤矿)
								 strshuik_mk = rs.getString("shuik");	//税款(煤矿)
								 strjialhj = rs.getString("hansje");	//价税合计
								 strguohzl =rs.getString("GUOHL");		//过衡重量
								 stryuns =String.valueOf(CustomMaths.Round_new(rs.getDouble("JIESSL")-rs.getDouble("guohl"),2));		//运损(结算数量差异)
								 strbeiz = nvlStr(rs.getString("beiz"));//备注
								 dblMeik= Double.parseDouble(strjialhj);
								 strranlbmjbr=rs.getString("jingbr");
								 strranlbmjbrq=FormatDate(rs.getDate("jingrq"));
								 blnHasMeik=true;
							 }
							 
//						1, 两票结算;
//						10, 石灰石结算
//						3, 国铁运费
						 
						 if ((blnHasMeik)&&(intLeix==Locale.liangpjs_feiylbb_id)){
//							 两票结算
							 
							 sql="select * from shihjsyfb where bianm='"+bianh[p]+"'";
							 
							 ResultSet rs3=con.getResultSet(sql);
							 if (rs3.next()){
								 
								 strtielyf =rs3.getString("GUOTYF");//铁路运费
//								 strtielzf = rs3.getString("guotzf");//杂费
								 strkuangqyf = rs3.getString("dityf");//矿区运费
//								 strkuangqzf = rs3.getString("kuangqzf");//矿区杂费
								 strbukouqzf = rs3.getString("bukyf");//补(扣)以前运杂费
								 strjiskc = rs3.getString("JISKC");//计税扣除
								 strbuhsyf =rs3.getString("buhsyf");//不含税运费
								 strshuil_ys = rs3.getString("shuil");//税率(运费)
								 strshuik_ys = rs3.getString("shuik");//税款(运费)
								 stryunzshj = rs3.getString("hansyf");//运杂费合计
								 dblYunf=rs3.getDouble("hansyf");
							 }
							 rs3.close();
							 
						 }else if(intLeix!=Locale.shihsjs_feiylbb_id){
//								 运费结算、两票
							 sql="select * from shihjsyfb where bianm='"+bianh[p]+"'"; 
							 rs=con.getResultSet(sql); 
							 	if(rs.next()){
				//					 strshijfk =rs.getString("yunfhsdj");
									 lgdiancxxb_id=rs.getLong("diancxxb_id");
									 strbianh=rs.getString("bianm");
									 strjiesrq=FormatDate(rs.getDate("jiesrq"));
									 intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
				//					 intDiancjsmkId =rs.getInt("id");//煤款id
									 strfahdw=rs.getString("gongysmc");
									 strfahksrq=rs.getString("fahksrq");
									 strfahjzrq=rs.getString("fahjzrq");
									 if(strfahksrq.equals(strfahjzrq)){
										 strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
									 }else{
										 strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
									 }
				//					 strfahrq = rs.getString("fahrq");//发货日期
									 strfaz=rs.getString("faz");
									 strdiabch=rs.getString("daibch");
									 stryuanshr = rs.getString("yuanshr");//原收货人
									 strshoukdw = rs.getString("shoukdw");//收款单位
									 strkaihyh = rs.getString("kaihyh");//开户银行
									 strkaisysrq=rs.getString("yansksrq");
									 strjiezysrq=rs.getString("yansjzrq");
									 
//									 strgongfbz_sl=rs.getString("gongfsl");
//									 strchangfys_sl=rs.getString("yanssl");
									 strjiesbz_sl=rs.getString("jiessl");
//									 strxiancsl_sl=String.valueOf(-rs.getDouble("yingk"));
									 
									 
									 if(strkaisysrq.equals(strjiezysrq)){
										 stryansrq=FormatDate(rs.getDate("yansksrq"));
									 }else{
										 stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
									 }
				//					 stryansrq = rs.getString("yansrq");//验收日期
									 strhuowmc = rs.getString("MEIZ");//货物名称
									 strxianshr = rs.getString("xianshr");//现收货人
									 stryinhzh = rs.getString("zhangh");//帐号
//									 strfahsl =rs.getString("gongfsl");//发运数量？？？？？？？？？？？？？？？？？？
									 strches = rs.getString("ches");//车数
//									 stryansbh = rs.getString("yansbh");//验收编号
									 strfapbh = rs.getString("fapbh");//发票编号
									 strduifdd = rs.getString("duifdd");//兑付地点
									 strfukfs = rs.getString("fukfs") ;//付款方式
									 strdiqdm="";
									 strshijfk =" ";//实际付款？？？？？？？？？？？？？？？？？
//									 stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
									 
									 strhetbz_CaO = "";		//合同
									 strgongfbz_CaO = "";	//供方
									 strchangfys_CaO = "";	//验收
									 strjiesbz_CaO = "";	//结算
									 strxiancsl_CaO= "";	//相差数量
									 strzhejbz_CaO = "";	//折价标准
									 strzhehje_CaO = "";	//折合金额
									 
									 strhetbz_MgO = "";		//合同
									 strgongfbz_MgO = "";	//供方标准
									 strchangfys_MgO = "";	//验收
									 strjiesbz_MgO = "";	//结算标准
									 strxiancsl_MgO = "";	//相差数量
									 strzhejbz_MgO = "";	//折价标准
									 strzhehje_MgO = "";	//折合金额
									 
									 strhetbz_Xid = "";		//合同
									 strgongfbz_Xid = "";	//供方标准挥发分
									 strchangfys_Xid = "";	//验收挥发分
									 strjiesbz_Xid = "";	//结算标准
									 strxiancsl_Xid = "";	//相差数量挥发分
									 strzhejbz_Xid = "";	//折价标准挥发分
									 strzhehje_Xid = "";	//折合金额挥发分
				
									 strhetbz_sl = "";			//合同数量
//									 strgongfbz_sl = "";		//供方数量
//									 strchangfys_sl ="";		//验收数量
//									 strjiesbz_sl = "";			//结算标准
//									 strxiancsl_sl = "";		//相差数量
									 strzhejbz_sl ="";			//亏吨数量
									 strzhehje_sl = "";			//折合金额
									 
									 
				
//									 strjiessl = rs.getString("jiessl");//结算数量
//									 strjiesbz_sl = rs.getString("gongfsl");
//									 strdanj = (double)Math.round(a);//单价
									 strjine = "0";//金额
									 strbukouqjk = "0";//补(扣)以前价款
									 strjiakhj = "0";//价款合计
									 strshuil_mk = "0";//税率(煤矿)
									 strshuik_mk = "0";//税款(煤矿)
									 strjialhj = "0";//价税合计
									 strguohzl =rs.getString("GUOHL");//过衡重量
									 strbeiz = nvlStr(rs.getString("beiz"));//备注
									 dblMeik= Double.parseDouble(strjialhj);
									 blnHasMeik=true;
									 
									 strranlbmjbr=rs.getString("ranlbmjbr");
									 strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));
							 }
						 }
						 
						 Money money=new Money();
						 //计算合计
						 strhej_xx=format(dblYunf+dblMeik,"0.00");
						 strmeikhjdx=money.NumToRMBStr(dblMeik);
						 stryunzfhjdx=money.NumToRMBStr(dblYunf);
						 strhej_dx=money.NumToRMBStr(dblYunf+dblMeik);
						 
						 rs.close();
						 con.Close();
//						 控制Cao,Mgo,细度 隐藏显示
						 
						 boolean CaO_bn=false;
						 boolean MgO_bn=false;	
						 boolean Xid_bn=false;
						 
						 
						 //设置指标所在的行数
						 int CaO_row=7;
						 int MgO_row=8;
						 int Xid_row=9;
						 
						 //设置指标字段不显示
						 if(strjiesbz_CaO.equals("")||strjiesbz_CaO.equals("0")){
							 CaO_bn=true;
						 }
						 if(strjiesbz_MgO.equals("")||strjiesbz_MgO.equals("0")){
							 MgO_bn=true;
						 }
						 if(strjiesbz_Xid.equals("")||strjiesbz_Xid.equals("0")){
							 Xid_bn=true;
						 }
						 
//							 中国大唐
							 
							 int ArrWidth[]=new int[] {125,70,65,65,65,65,75,70,76,75,80,70};
							 
							 String ArrHeader[][]=new String[20][12];
							 ArrHeader[0]=new String[] {"供货单位:"+strfahdw,"","","发站:",strfaz,"代表车号:",strdiabch,"","收款单位:",strshoukdw,"",""};
							 ArrHeader[1]=new String[] {"发货日期:"+strfahrq,"","","地区代码:",strdiqdm,"原收货人:",stryuanshr,"","开户银行:",strkaihyh,"",""};
							 ArrHeader[2]=new String[] {"验收日期:"+stryansrq,"","","货物名称:",strhuowmc,"现收货人:",strxianshr,"","银行帐号:",stryinhzh,"",""};
							 ArrHeader[3]=new String[] {"发运数量:",strfahsl,"车数:"+strches,"验收编号:",stryansbh,"发票编号:",strfapbh,"","兑付地点:",strduifdd,"付款方式:",strfukfs};
							 ArrHeader[4]=new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","","",""};
							 ArrHeader[5]=new String[] {"含税价:"+strshijfk+"(元)","合同标准","供方标准","厂方验收","结算标准","相差数量","折价标准","折合金额","供方数量","验收数量","亏吨数量","折合金额"};
							 ArrHeader[6]=new String[] {""+Locale.CaO_zhibb+"("+Locale.baifb_danw+")",strhetbz_CaO,strgongfbz_CaO,strchangfys_CaO,strjiesbz_CaO,strxiancsl_CaO,strzhejbz_CaO,strzhehje_CaO,"(吨)","(吨)","(吨)","(元)"};
							 ArrHeader[7]=new String[] {""+Locale.MgO_zhibb+"("+Locale.baifb_danw+")",strhetbz_MgO,strgongfbz_MgO,strchangfys_MgO,strjiesbz_MgO,strxiancsl_MgO,strzhejbz_MgO,strzhehje_MgO,strgongfbz_sl,strchangfys_sl,strxiancsl_sl,strzhehje_sl};
							 ArrHeader[8]=new String[] {""+Locale.Xid_zhibb+"("+Locale.baifb_danw+")",strhetbz_Xid,strgongfbz_Xid,strchangfys_Xid,strjiesbz_Xid,strxiancsl_Xid,strzhejbz_Xid,strzhehje_Xid,"","","",""};
							 ArrHeader[9]=new String[] {"结算数量","单价","金额","","补(扣)以前价款","补(扣)以前价款","价款合计","","税率","税款","价税合计","价税合计"};
							 ArrHeader[10]=new String[] {strjiesbz_sl,strbuhsdj,strjine,"",strbukouqjk," ",formatq(strjiakhj),"",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
							 ArrHeader[11]=new String[] {"煤款合计(大写):",strmeikhjdx,"","","","","","","","","",""};
							 ArrHeader[12]=new String[] {"铁路运费","铁路杂费","矿区运费","矿区杂费","补(扣)以前运杂费","补(扣)以前运杂费","不含税运费","","税率","税款","运杂费合计","运杂费合计"};
							 ArrHeader[13]=new String[] {strtielyf,strtielzf,strkuangqyf,strkuangqzf,strbukouqzf,"",formatq(strbuhsyf),"",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
							 ArrHeader[14]=new String[] {"运杂费合计(大写):",stryunzfhjdx,"","","","","","","","","",""};
							 ArrHeader[15]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,"",""};
							 ArrHeader[16]=new String[] {"备注:",strbeiz,"","","","","","","过衡重量:",strguohzl,""+Locale.jiesslcy_title+"",stryuns};
							 ArrHeader[17]=new String[] {"电厂燃料部门:(盖章)","","电厂财务部门:(盖章)","","","质量监督处:(签章)","","领导审批:(签章)","","综合财务处:(签章)","",""};
							 ArrHeader[18]=new String[] {"经办人:"+strranlbmjbr,"","经办人:"+strchangcwjbr,"","","经办人:"+strzhijzxjbr,"",""+strlingdqz,"","经办人:"+strzonghcwjbr,"",""};
							 ArrHeader[19]=new String[] {""+strranlbmjbrq+"","",""+strchangcwjbrq+"","","",""+strzhijzxjbrq+"","",""+strlingdqzrq+"","",""+strzonghcwjbrq+"","",""};
							 
//							 定义页Title
//							 Report rt=new Report();
							 rt.setTitle(Locale.jiesd_title,ArrWidth);
							 String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
							 rt.setDefaultTitleLeft("填制单位："+tianbdw,3);
							 rt.setDefaultTitle(5,3,"结算日期："+strjiesrq,Table.ALIGN_CENTER);
							 rt.setDefaultTitle(9,4,"编号:"+strbianh,Table.ALIGN_RIGHT);
							 rt.setBody(new Table(ArrHeader,0,0,0));
							 rt.body.setWidth(ArrWidth);	
							 
							 rt.body.mergeCell(1,1,1,3);
							 rt.body.mergeCell(1,7,1,8);
							 rt.body.mergeCell(1,10,1,12);
							 rt.body.setCells(1,4,1,4,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(1,6,1,6,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(1,9,1,9,Table.PER_BORDER_RIGHT,0);
							 
							 rt.body.mergeCell(2,1,2,3);
							 rt.body.mergeCell(2,7,2,8);
							 rt.body.mergeCell(2,10,2,12);
							 rt.body.setCells(2,4,2,4,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(2,6,2,6,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(2,9,2,9,Table.PER_BORDER_RIGHT,0);
							 
							 rt.body.mergeCell(3,1,3,3);
							 rt.body.mergeCell(3,7,3,8);
							 rt.body.mergeCell(3,10,3,12);
							 rt.body.setCells(3,4,3,4,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(3,6,3,6,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(3,9,3,9,Table.PER_BORDER_RIGHT,0);
							 
							 rt.body.mergeCell(4,7,4,8);
							 rt.body.setCells(4,4,4,4,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
							 
							 rt.body.mergeCell(5,1,5,8);
							 rt.body.mergeCell(5,9,5,12);
							 
//							 rt.body.mergeCell(8,3,8,4);
//							 rt.body.mergeCell(8,5,8,6);
//							 rt.body.mergeCell(8,7,8,8);
//							 rt.body.mergeCell(8,11,8,12);
//							 
//							 rt.body.mergeCell(9,3,9,4);
//							 rt.body.mergeCell(9,5,9,6);
//							 rt.body.mergeCell(9,7,9,8);
//							 rt.body.mergeCell(9,11,9,12);
							 
							 rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_CENTER);
							 
							 rt.body.mergeCell(10,3,10,4);
							 rt.body.mergeCell(10,5,10,6);
							 rt.body.mergeCell(10,7,10,8);
							 rt.body.mergeCell(10,11,10,12);
							 
							 rt.body.mergeCell(11,3,11,4);
							 rt.body.mergeCell(11,5,11,6);
							 rt.body.mergeCell(11,7,11,8);
							 rt.body.mergeCell(11,11,11,12);				
							 rt.body.setCells(11,12,11,12,Table.PER_BORDER_RIGHT,1);
							 rt.body.setCells(11,1,11,1,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 
							 rt.body.mergeCell(12,2,12,12);	//煤款合计大写
							 rt.body.setCells(12,2,12,12,Table.PER_ALIGN,Table.ALIGN_LEFT);	//煤款合计大写
							 
							 rt.body.mergeCell(13,5,13,6);
							 rt.body.mergeCell(13,7,13,8);
							 rt.body.mergeCell(13,11,13,12);
							 
							 rt.body.mergeCell(14,5,14,6);
							 rt.body.mergeCell(14,7,14,8);
							 rt.body.mergeCell(14,11,14,12);
							 
							 rt.body.mergeCell(15,2,15,12);//运费合计大写-13
							 
							 rt.body.mergeCell(16,2,16,8);
							 rt.body.mergeCell(16,10,16,12);
							 
							 rt.body.mergeCell(17,2,17,8);	//备注
							 
							 rt.body.mergeCell(18,1,18,2);
							 rt.body.mergeCell(18,3,18,5);
							 rt.body.mergeCell(18,6,18,7);
							 rt.body.mergeCell(18,8,18,9);
							 rt.body.mergeCell(18,10,18,12);
							 
							 rt.body.mergeCell(19,1,19,2);
							 rt.body.mergeCell(19,3,19,5);
							 rt.body.mergeCell(19,6,19,7);
							 rt.body.mergeCell(19,8,19,9);
							 rt.body.mergeCell(19,10,19,12);
							 
							 rt.body.mergeCell(20,1,20,2);
							 rt.body.mergeCell(20,3,20,5);
							 rt.body.mergeCell(20,6,20,7);
							 rt.body.mergeCell(20,8,20,9);
							 rt.body.mergeCell(20,10,20,12);
							 
							 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
							 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
							 
							 rt.body.setCells(5, 1, 11, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
							 rt.body.setCells(6,1,9,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
							 
							 rt.body.setCells(7,2,7,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 rt.body.setCells(8,2,8,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 rt.body.setCells(9,2,9,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 rt.body.setCells(11,1,11,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 rt.body.setCells(13,1,13,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 rt.body.setRowCells(8,Table.PER_ALIGN,Table.ALIGN_CENTER);
//							 rt.body.setRowCells(17,Table.PER_BORDER_BOTTOM,0);
							 rt.body.setRowCells(18,Table.PER_BORDER_BOTTOM,0);
							 rt.body.setRowCells(19,Table.PER_BORDER_BOTTOM,0);
							 rt.body.setRowCells(18,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 rt.body.setRowCells(19,Table.PER_ALIGN,Table.ALIGN_CENTER);
							 rt.body.setRowCells(20,Table.PER_ALIGN,Table.ALIGN_RIGHT);
							 
//							 设置隐藏行
//							 if(Qnetar_bn){
//								 rt.body.setRowCells(Qnetar_row, Table.PER_USED, false);
//								 rt.body.setRowHeight(Qnetar_row,0);
//								 rt.body.rows[Qnetar_row].hidden=true;
//							 }

							 if(MgO_bn){
								 
								 rt.body.setRowCells(MgO_row, Table.PER_USED, false);
								 rt.body.setRowHeight(MgO_row,0);
								 rt.body.rows[MgO_row].hidden=true;
							 }
							 if(Xid_bn){
								 
								 rt.body.setRowCells(Xid_row, Table.PER_USED, false);
								 rt.body.setRowHeight(Xid_row,0);
								 rt.body.rows[Xid_row].hidden=true;
							 }
							 sb.append(rt.getAllPagesHtml(p));
					} catch (NumberFormatException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					} catch (Exception e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
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

	public String getTianzdw(long Diancxxb_id) {
		String Tianzdw = "";
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select quanc from diancxxb where id="+Diancxxb_id;
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