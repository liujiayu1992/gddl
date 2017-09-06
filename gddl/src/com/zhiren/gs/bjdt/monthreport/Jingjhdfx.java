package com.zhiren.gs.bjdt.monthreport;

import java.sql.ResultSet;
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
import com.zhiren.common.filejx.FileJx;
import com.zhiren.common.filejx.FilePathRead;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Jingjhdfx extends BasePage  {
	private boolean Create = false;
    public void Create(IRequestCycle cycle) {
    	Create = true;
    }
    private boolean _ShengcChick = false;
    public void ShengcButton(IRequestCycle cycle) {
    	_ShengcChick = true;
    }
    private boolean _ZhongdgysszChick = false;
    public void ZhongdgysszButton(IRequestCycle cycle) {
    	_ZhongdgysszChick = true;
    }
    private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
			if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
			}
			
			if (_ShengcChick) {
				_ShengcChick = false;
				
		   getSelectData();
			}
		   if (_ZhongdgysszChick) {
			_ZhongdgysszChick = false;
			
			cycle.activate("Zhongdgyssz_bjdt");
		}
		   if (Create) {
				Create = false;
				Create();
	        }
	}

	private void getSelectData(){
		ExtGridUtil egu = new ExtGridUtil("wenjmc","wenjsj","fuj","gridDiv",getFenxFile());
		
		egu.getColumn("wenjmc").setHeader("文件名称");
		egu.getColumn("wenjmc").setDefaultValue("");
		egu.getColumn("wenjmc").setWidth(300);
		egu.getColumn("wenjmc").setEditor(null);
		
		egu.getColumn("wenjsj").setHeader("文件时间");
		egu.getColumn("wenjsj").setDefaultValue("");
		egu.getColumn("wenjsj").setWidth(150);
		egu.getColumn("wenjsj").setEditor(null);
		
		egu.getColumn("fuj").setHeader("附件");
		egu.getColumn("fuj").setDefaultValue("");
		egu.getColumn("fuj").setWidth(350);
		egu.getColumn("fuj").setEditor(null);
		//年
		egu.addTbarText("年:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NianDropDown");
		comb1.setId("Nian");
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(55);
		comb1.setListWidth(58);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Nian.on('select',function(){document.forms[0].submit();});");//动态刷新
		
		egu.addTbarText("月:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YueDropDown");
		comb2.setId("Yue");
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(45);
		comb2.setListWidth(48);
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("Yue.on('select',function(){document.forms[0].submit();});");//动态刷新
		egu.addTbarText("-");// 设置分隔符
		
//		egu.addToolbarButton(GridButton.ButtonType_Refresh, "");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton("生成", GridButton.ButtonType_SubmitSel, "ShengcButton",SysConstant.Btn_Icon_SelSubmit );
		GridButton gb=new GridButton("重点供应商设置", "function(){document.getElementById('ZhongdgysszButton').click();}");
		egu.addTbarBtn(gb);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);//解决宽度问题
		setExtGrid(egu);		
		
		
	}
	
	//年
	public IDropDownBean getNianSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getNianSelectModel()
							.getOption(DateUtil.getYear(new Date())-2007));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}
	public void setNianSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}
	public void setNianSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}
	public IPropertySelectionModel getNianSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	public void getNianSelectModels() {
				StringBuffer sql=new StringBuffer();
		int i=0;
		for(i=2007;i<DateUtil.getYear(new Date())+2;i++){
			sql.append("select " + i + " id," + i + " mingc from dual union all ");
		}
		sql.append("select " + i + " id," + i + " mingc from dual ");
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}
	
//	月
	public IDropDownBean getYueSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {

				int _yuef = DateUtil.getMonth(new Date());
				for (int i = 0; i < getYueSelectModel().getOptionCount(); i++) {
					Object obj = getYueSelectModel().getOption(i);
					if (_yuef == ((IDropDownBean) obj).getId()) {
						((Visit) getPage().getVisit())
						.setDropDownBean3((IDropDownBean) obj);
						break;
					}
				}
			
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setYueSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public void setYueSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}
	public IPropertySelectionModel getYueSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYueSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	public void getYueSelectModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
	}
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setNianSelectValue(null);
			setNianSelectModel(null);
			setYueSelectValue(null);
			setYueSelectModel(null);
			getNianSelectModels();
			getYueSelectModels();
		}
		
		
		init();
	}

	private void init() {
		getSelectData();
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


	public String getFilepath(String str){
		JDBCcon con=new JDBCcon();
		String filepath="";
		 try{
			 String pathsql = "select zhi from xitxxb where mingc='"+str+"'";
			 ResultSet rspath = con.getResultSet(pathsql);
			 if(rspath.next()){
				 filepath = rspath.getString("zhi");
			 }
			 rspath.close();
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 con.Close();
		 }
		return filepath;
	}
	
	private String[][] getFenxFile(){
		ArrayList strbf=new ArrayList();
		FileJx wjjx=new FileJx();
		
		String NameYear = getNianSelectValue().getValue();
		String NameMonths = getYueSelectValue().getValue();
		
		String FileName = NameYear+NameMonths+"经济活动分析";
		String strURL = getFilepath("经济活动分析文档路径");
		
		FilePathRead jx=new FilePathRead(FileName,strURL);
		strbf=jx.getTxtFileList();//得到文件列表
		String fujbItem="";
//		String FileInfo [][] = {{"","",""},{"","",""}};
	//	String FileInfo [][] = new String[3][3];
		
		String FileInfo [][] = new String[strbf.size()+2][3];
		FileInfo [0][0] = "";
		FileInfo [0][1] = "";
		FileInfo [0][2] = "";
//		
		FileInfo [1][0] = "";
		FileInfo [1][1] = "";
		FileInfo [1][2] = "";
//		
		
//		FileInfo [2][0] = "aa";
//		FileInfo [2][1] = "2009-07-27";
//		FileInfo [2][2] = "<a  onclick=\"window.open(\'http;//10.115.25.88:8086/zgdt/app?service=page/downfile&filename=aa.txt&filepath=D:/wenjgl/\')\" href=\"#\" >aa.txt</a>";
//		
		
		for(int j=0;j<strbf.size();j++){
//			 strbf2=wjjx.TextJx(strbf.get(j).toString());//一个文件
			 String fileDate = wjjx.getWenjrq(strbf.get(j).toString());//文件时间
			 String fileName = strbf.get(j).toString().substring(strbf.get(j).toString().lastIndexOf("\\")+1);//文件名称
			// fujbItem= "<a  onclick=\"window.open(\'http;//10.115.25.88:8086/zgdt/app?service=page/downfile&filename="+fileName+"&filepath=D:/wenjgl/\')\" href=\"#\" >"+fileName+"</a>'";
			 fujbItem= "<a  onclick=\"window.open(\'"+getContext()+"/app?service=page/downfile&filename="+fileName+"&filepath="+strURL+"/')\" href=\"#\" >"+fileName+"</a>";
//		在此循环里构造显示内容的JS代码，包括文件名称、文件时间、附件信息
			 
			 FileInfo[j+2][0] = fileName;
			 FileInfo[j+2][1] = fileDate;
			 FileInfo[j+2][2] = fujbItem;
		}
		return FileInfo;
			 
	}
	public String getContext(){
		return "http://"+this.getRequestCycle().getRequestContext().getServerName()+":"
				+this.getRequestCycle().getRequestContext().getServerPort()+this.getEngine().getContextPath();
	}
	
	public void Create(){
		 Runtime rn=Runtime.getRuntime();
		 Process p=null;
		 String riq = getLastMonths(DateUtil.FormatDate(DateUtil.getDate(getNianSelectValue().getValue()+"-"+getYueSelectValue().getValue()+"-01")));
		 String strURL = getFilepath("经济活动分析应用路径");
		 try{
//			 System.out.println("C:\\jingjhdfx\\Analy.exe "+riq+" ");
			 p=rn.exec(strURL+"  "+riq+" ");
//			 InputStream stderr = p.getErrorStream();
//			 InputStreamReader isr = new InputStreamReader(stderr);
//			 
//			 BufferedReader br = new BufferedReader(isr);
//			 String line = null;
//			 while ( (line = br.readLine()) != null)
//			 System.out.println(line);
//
//			 int exitVal = p.waitFor();
//			 System.out.println("Process exitValue: " + exitVal);
//			 
		 }catch(Exception e){
			 System.out.println("Error exec "+strURL+" "+riq+"!");
		 }finally{
		 }

	}

     
	public String getLastMonths(String riq){
		 String date="";
		 String months="";
		 String year="";
		 year = riq.substring(0,4);
		 months = riq.substring(5,7);
		 if(months.equals("01")||months.equals("1")){
			 months="12";
			 year = String.valueOf((Integer.parseInt(year)-1));
		 }else{
			 months = String.valueOf((Integer.parseInt(months)-1));
		 }
		 date = year+"-"+months+"-"+"01";
		 return date;
	 }
	
	private String GridData;
	public String getGridData(){
		return GridData;
	}
	public void setGridData(String value){
		this.GridData=value;
	}
	
//	  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    
    public void setChange(String change) {
    	Change = change;
    }
    
    public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	}
//   

	