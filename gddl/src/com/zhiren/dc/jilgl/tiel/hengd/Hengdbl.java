package com.zhiren.dc.jilgl.tiel.hengd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 王磊
 * 功能：手动添加过衡文件
 * 说明：文件名后缀为Z。基础文件夹 由visit.getXitwjjwz() 取得
 * 		具体对应的目录为 “shul/jianjwj/”
 *
 */
public class Hengdbl extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	//绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	// 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit)this.getPage().getVisit();
		File filepath = new File(visit.getXitwjjwz()+"/shul/jianjwj");
		if(!filepath.exists()) {
			filepath.mkdirs();
		}
		String filename = getRiq().replaceAll("-", "")+getHourValue().getValue()+getMinValue().getValue()+getSecValue().getValue();
		String strkeygen = filename.substring(0,8);
		BigInteger keygen = BigInteger.valueOf(Long.parseLong(strkeygen));
		BigInteger bi;
		File ghfile = new File(filepath,filename+"z"); 
		try {
			ghfile.createNewFile();
			FileWriter fwn = new FileWriter(ghfile);
	        PrintWriter pwn = new PrintWriter(fwn);
			ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
			if(rsl==null){
				WriteLog.writeErrorLog(ErrorMessage.NullResult 
						+ "Hengdbl.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
				setMsg(ErrorMessage.NullResult);
				return;
			}
			while(rsl.next()) {
				String sm = "";
		        String ss = String.valueOf(rsl.getDouble(1)*1000)+","+String.valueOf(rsl.getDouble(2)*1000)
		        +","+rsl.getString(3)+","+rsl.getString(4);
		        for(int j=0;j<ss.length();j++){
					bi = BigInteger.valueOf((long)ss.charAt(j));
					sm = sm + " " + bi.xor(keygen).longValue() + " ";
				}
		        pwn.println(sm);
			}
			pwn.close();
			fwn.close();
			setMsg("文件保存成功");
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			ghfile.delete();
			setMsg("文件保存失败");
		}
	}
 
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb.append("select 0 id,0.000 maoz,0 piz,0.00 sud,'' cheph from dual where sysdate is null");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		//egu.setHeight("bodyHeight");
		egu.getColumn(2).setHeader(Locale.maoz_chepb);
		((NumberField)egu.getColumn(2).editor).setDecimalPrecision(3);
		egu.getColumn(3).setHeader(Locale.piz_chepb);
		((NumberField)egu.getColumn(3).editor).setDecimalPrecision(2);
		egu.getColumn(4).setHeader(Locale.ches_chepb);
		((NumberField)egu.getColumn(4).editor).setDecimalPrecision(1);
		egu.getColumn(5).setHeader(Locale.cheph_chepb);
//		egu.getColumn(6).setHeader("过衡时间");
//		egu.getColumn(6).setHidden(true);
		
		egu.addTbarText("过衡时间:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("时:");
		ComboBox hour=new ComboBox();
		hour.setWidth(60);
		hour.setTransform("HOUR");
		hour.setEditable(true);
		//hour.setListeners("select:function(own,rec,i){own.getEl().dom.selectedIndex = i;}");
		egu.addToolbarItem(hour.getScript());
		
		egu.addTbarText("分:");
		ComboBox min=new ComboBox();
		min.setWidth(60);
		min.setTransform("MIN");
		min.setEditable(true);
		egu.addToolbarItem(min.getScript());
		
		egu.addTbarText("秒:");
		ComboBox sec=new ComboBox();
		sec.setWidth(60);
		sec.setTransform("SEC");
		sec.setEditable(true);
		egu.addToolbarItem(sec.getScript());
		
		
		egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		setExtGrid(egu);
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
//	设置小时下拉框
	public IDropDownBean getHourValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean)obj);
					break;
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	
	public void setHourValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(Value);		
	}

	public IPropertySelectionModel getHourModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			getHourModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(_value);
	}
	
	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if(i<10) 
				listHour.add(new IDropDownBean(i, "0"+i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}
	
//	 设置分钟下拉框
	public IDropDownBean getMinValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	
	public void setMinValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit)this.getPage().getVisit()).getProSelectionModel2() == null) {
			getMinModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setMinModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if(i<10)
				listMin.add(new IDropDownBean(i, "0"+i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}

	
//	 设置秒下拉框
	public IDropDownBean getSecValue() {
		if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getSecModel().getOptionCount(); i++) {
				Object obj = getSecModel().getOption(i);
				if (DateUtil.getSeconds(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setSecValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}

	public void setSecValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getSecModel() {
		if (((Visit)this.getPage().getVisit()).getProSelectionModel3() == null) {
			getSecModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setSecModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	

	public void getSecModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if(i<10)
				listMin.add(new IDropDownBean(i, "0"+i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setSecModel(new IDropDownModel(listMin));
	}
	
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			setHourValue(null);
			setHourModel(null);
			setMinValue(null);
			setMinModel(null);
			setSecValue(null);
			setSecModel(null);
			setTbmsg(null);
			getSelectData();
		}
	}
}