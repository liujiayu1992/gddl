package com.zhiren.dc.jilgl.tiel.hengd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author Rock
 *
 */
/*
 * 2009-02-16
 * 王磊
 * 增加正常过衡文件不能修改的判断
 */
/*
 * 2009-02-19
 * 王磊
 * 增加正常过衡文件仅可以修改车号的功能
 */
/*
 * 2009-05-25
 * 李鹏
 * 增加序号
 */
public class Hengdxg extends BasePage implements PageValidateListener {
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

	// 绑定日期
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
		Visit visit = (Visit) this.getPage().getVisit();
		File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwj");
		String filename = getHengdSelectValue().getValue();
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		File oldfilename = new File(filepath+"\\"+filename) ;
		boolean success = oldfilename.delete();
		if(!success){
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg001);
			setMsg(ErrorMessage.Hengdxg001);
		}
		String strkeygen = filename.substring(0, 8);
		BigInteger keygen = BigInteger.valueOf(Long.parseLong(strkeygen));
		BigInteger bi;
		File ghfile = new File(filepath, filename);
		FileWriter fwn = null;
		PrintWriter pwn = null;
		try {
			ghfile.createNewFile();
			fwn = new FileWriter(ghfile);
			pwn = new PrintWriter(fwn);
			ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
			while (rsl.next()) {
				String sm = "";
				String ss = String.valueOf(rsl.getDouble(0) * 1000) + ","
						+ String.valueOf(rsl.getDouble(1)*1000) + "," + rsl.getString(2) + ","
						+ rsl.getString(3);
				for (int j = 0; j < ss.length(); j++) {
					bi = BigInteger.valueOf((long) ss.charAt(j));
					sm = sm + " " + bi.xor(keygen).longValue() + " ";
				}
				pwn.println(sm);
			}
			setMsg("文件写入成功");
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg002);
			setMsg(ErrorMessage.Hengdxg002);
			ghfile.delete();
		}finally {
			try {
				pwn.close();
				fwn.close();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
	}

	private void Delete(){
		Visit visit = (Visit) this.getPage().getVisit();
		if(getHengdSelectValue() == null){
			setMsg("没有可删除文件!");
		}
		File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwj");
		File oldfilename = new File(filepath+"\\"+getHengdSelectValue().getValue()) ;
		boolean success = oldfilename.delete();
		if(!success){
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg003);
			setMsg(ErrorMessage.Hengdxg003);
		}
	}
	
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _DeleteClick = false;
	
	public void DeleteButton(IRequestCycle cycle){
		_DeleteClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshClick) {
			_RefreshClick = false;
			getSelectData();
		}
		if(_DeleteClick){
			_DeleteClick = false;
			Delete();
			setHengdSelectModels();
			setHengdSelectValue(null);
			getSelectData();
		}
	}

	private List getDataList() {
		Visit visit = (Visit) this.getPage().getVisit();
		File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwj");
		if(getHengdSelectValue()== null){
			return null;
		}
		File file = new File(filepath, getHengdSelectValue().getValue());

		FileReader fr;
		BufferedReader br;
		BigInteger bi;
		BigInteger keygen;
		String bufferStr;
		String strarr[];
		StringBuffer sb;
		char c;
		long asclong;
		List list = new ArrayList();
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg004);
			setMsg(ErrorMessage.Hengdxg004);
			return null;
		}
		br = new BufferedReader(fr);
		try {
			keygen = BigInteger.valueOf(Long.parseLong(file.getName()
					.substring(0, 8)));
			while ((bufferStr = br.readLine()) != null) {
				if("".equals(bufferStr)) {
					continue;
				}
				sb = new StringBuffer();
				strarr = bufferStr.split("  ");
				for (int k = 0; k < strarr.length; k++) {
					bi = new BigInteger(strarr[k].trim());
					asclong = bi.xor(keygen).longValue();
					c = (char) asclong;
					sb.append(c);
				}
				String strGuohxx[] = sb.toString().trim().split(",", 4);
				String maoz = "".equals(strGuohxx[0])?"":String.valueOf(CustomMaths.div(Double.parseDouble(strGuohxx[0]),
						1000.0));
				String piz = "".equals(strGuohxx[1])?"":String.valueOf(CustomMaths.div(Double.parseDouble(strGuohxx[1]),
						1000.0));
				String sud = strGuohxx[2];
				String cheph = strGuohxx[3];
				list.add(new Guohxx(maoz, piz, sud, cheph));
			}
		} catch (NumberFormatException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg005);
			setMsg(ErrorMessage.Hengdxg005);
			return null;
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg006);
			setMsg(ErrorMessage.Hengdxg006);
			return null;
		}finally {
			try {
				br.close();
				fr.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 刷新衡单列表
	public void getSelectData() {
		ExtGridUtil egu = new ExtGridUtil("gridDiv");
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.addPaging(0);
		ComboBox hengdcb = new ComboBox();
		hengdcb.setTransform("HengdSelect");
		hengdcb.setWidth(130);
		hengdcb.setId("HengdSelect");
		hengdcb.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
		egu.addToolbarItem(hengdcb.getScript());
//		 设置GRID是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		
		GridColumn gridc = new GridColumn(0);
		gridc.setUpdate(false);
		gridc.setColtype(GridColumn.ColType_Rownum);
		egu.addColumn(gridc);
		

		
		GridColumn gridc1 = new GridColumn(0, "maoz", Locale.maoz_chepb, 60);
		gridc1.setUpdate(true);
		gridc1.setColtype(GridColumn.ColType_default);
		gridc1.setDataType(GridColumn.DataType_Float);
		NumberField nfmz = new NumberField();
		nfmz.setDecimalPrecision(3);
		gridc1.setEditor(nfmz);
		egu.addColumn(gridc1);
		
		GridColumn gridc2 = new GridColumn(0, "piz", Locale.piz_chepb, 60);
		gridc2.setUpdate(true);
		gridc2.setColtype(GridColumn.ColType_default);
		gridc2.setDataType(GridColumn.DataType_Float);
		NumberField nfpz = new NumberField();
		nfpz.setAllowBlank(true);
		nfpz.setDecimalPrecision(3);
		gridc2.setEditor(nfpz);
		egu.addColumn(gridc2);
		
		GridColumn gridc3 = new GridColumn(0, "sud", Locale.ches_chepb, 60);
		gridc3.setUpdate(true);
		gridc3.setColtype(GridColumn.ColType_default);
		gridc3.setDataType(GridColumn.DataType_Float);
		NumberField nfsd = new NumberField();
		nfsd.setAllowBlank(true);
		nfsd.setDecimalPrecision(2);
		gridc3.setEditor(nfsd);
		egu.addColumn(gridc3);
		
		GridColumn gridc4 = new GridColumn(0, "cheph",Locale.cheph_chepb, 70);
		gridc4.setUpdate(true);
		gridc4.setColtype(GridColumn.ColType_default);
		gridc4.setDataType(GridColumn.DataType_String);
		TextField tfcph = new TextField();
		tfcph.setAllowBlank(true);
		gridc4.setEditor(tfcph);
		egu.addColumn(gridc4);
		
		if(getHengdSelectValue()!= null && getHengdSelectValue().getValue().indexOf("z")==-1){
			egu.getColumn("maoz").setEditor(null);
			egu.getColumn("piz").setEditor(null);
			egu.getColumn("sud").setEditor(null);
		}
		
		
//		egu.setHeight("bodyHeight");
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefreshButton').click()}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addToolbarItem("{"+refurbish.getScript()+"}");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
		GridButton delete = new GridButton("删除文件","function (){document.getElementById('DeleteButton').click()}");
		delete.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addToolbarItem("{"+delete.getScript()+"}");
		List list = getDataList();
		if(list == null) {
			String[][] data = {{"","","",""}};
			egu.setData(data);
			setExtGrid(egu);
			return;
		}
		if(list.isEmpty()) {
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg007);
			setMsg(ErrorMessage.Hengdxg007);
			String[][] data = {{"","","",""}};
			egu.setData(data);
			setExtGrid(egu);
			return;
		}
		String[][] data = new String[list.size()][4];
		for (int i = 0; i < list.size(); i++) {
			
			data[i][0] = ((Guohxx) list.get(i))._maoz;
			data[i][1] = ((Guohxx) list.get(i))._piz;
			data[i][2] = ((Guohxx) list.get(i))._sud;
			data[i][3] = ((Guohxx) list.get(i))._cheph;			
		}
/*
*huochaoyuan
*2009-10-22添加如果衡单皮重为空，则可以编辑的判断语句，适用于电厂需要在衡单修改页面输入皮重而不是核对完车号输入皮重的业务需求
*/		
		if(data[0][1].equals("")||data[0][1].equals("0.0")){
			egu.getColumn("piz").setEditor(nfpz);
		}
/*
 * huochaoyuan
 * 2010-01-28针对灞桥电厂翻车机衡需要修改取到的皮重信息，故对后缀“d”的过衡文件进行皮重修改判断
 */		
		if(getHengdSelectValue()!= null && getHengdSelectValue().getValue().indexOf("d")==-1){
			egu.getColumn("piz").setEditor(nfpz);
		}
		egu.setData(data);
		egu.addPaging(0);
		setExtGrid(egu);

	}
	
	

	// 表格使用的方法

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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	// ///////////////////////////////////////
	public IDropDownBean getHengdSelectValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getHengdSelectModel().getOptionCount() > 0) {
				setHengdSelectValue((IDropDownBean) getHengdSelectModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHengdSelectValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getHengdSelectModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setHengdSelectModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHengdSelectModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
	}

	public void setHengdSelectModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwj");
		File[] files = filepath.listFiles();
		List hengdList = new ArrayList();
		if(files != null) {
			for (int i = files.length - 1; i >= 0; i--) {
				if (files[i].isFile() && files[i].getName().length() == 15
						&& files[i].getName().indexOf(".") == -1) {
					hengdList.add(new IDropDownBean(i, files[i].getName()));
				}
			}
		}
		setHengdSelectModel(new IDropDownModel(hengdList));
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
			setHengdSelectValue(null);
			setHengdSelectModel(null);
			setExtGrid(null);
			getSelectData();
		}
//		getSelectData();
	}

	private class Guohxx {

		
		public String _maoz;

		public String _piz;

		public String _sud;

		public String _cheph;

		public Guohxx( String maoz, String piz, String sud, String cheph) {

			_maoz = maoz;
			_piz = piz;
			_sud = sud;
			_cheph = cheph;
		}
	}
}
