package com.zhiren.zidy;

import java.util.List;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import org.apache.tapestry.contrib.palette.SortMode;

public class ZidyReport extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	private int _CurrentPage = 1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = 1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String FieldChange;
	
	public String getFieldChange() {
		return FieldChange;
	}
	public void setFieldChange(String value) {
		FieldChange = value;
	}
	
	private String html;
	
	public String getHtml() {
		return html;
	}
	public void setHtml(String value) {
		html = value;
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	public Aotcr getAotcr() {
		return ((Visit)this.getPage().getVisit()).getAotcr();
	}
	public void setAotcr(Aotcr aotcr) {
		((Visit)this.getPage().getVisit()).setAotcr(aotcr);
	}

	private boolean _StrQitClick = false;
	public void StrQitButton(IRequestCycle cycle) {
		_StrQitClick = true;
	}
	private boolean _SearchClick = false;
	public void SearchButton(IRequestCycle cycle) {
		_SearchClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_StrQitClick) {
			_StrQitClick = false;
			setParameters();
		}
		if (_SearchClick) {
			_SearchClick = false;
			setParameters();
			Search();
		}
	}
	
	private void Search() {
		if(getChange()!=null && !"".equals(getChange())) {
			List plist = getAotcr().getParaMeters();
			String[] params = getChange().split(";");
			for(int i=0;i<plist.size();i++) {
				ZidyParam p = (ZidyParam)plist.get(i);
				for(int j =0; j< params.length ; j++) {
					if(p.getName().equals(params[j].split("=")[0])) {
						p.setSvalue(params[j].split("=")[1]);
						break;
					}
				}
			}
		}
		getAotcr().setData();
	}

	public String getPrintTable() {
		if(getAotcr() == null) {
			return "报表参数不正确";
		}
		return getAotcr().getHtml();
	}
	
	public void setParameters() {
		if(getChange()!=null && !"".equals(getChange())) {
			List plist = getAotcr().getParaMeters();
			String[] params = getChange().split(";");
			for(int i=0;i<plist.size();i++) {
				ZidyParam p = (ZidyParam)plist.get(i);
				for(int j =0; j< params.length ; j++) {
					if(p.getName().equals(params[j].split("=")[0])) {
						p.setSvalue(params[j].split("=")[1]);
						p.setFvalue(params[j].split("=")[2]);
						break;
					}
				}
			}
		}
		initToolBars();
	}
	/*
	public void setToolDefaultValue() {
		if(getFieldChange() != null && !"".equals(getFieldChange())) {
			String[] params = getFieldChange().split(";");
			List plist = getAotcr().getParaMeters();
			for(int i=0;i<plist.size();i++) {
				ZidyParam p = (ZidyParam)plist.get(i);
				for(int j =0; j< params.length ; j++) {
					if(p.getName().equals(params[j].split("=")[0])) {
						p.setFvalue(params[j].split("=")[1]);
						break;
					}
				}
			}
		}
		initToolBars();
	}*/

	public void initToolBars() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		StringBuffer StrSearch = new StringBuffer("change = \"\"");
		//StringBuffer StrRefurbish = new StringBuffer("fieldchange = \"\"");
		StringBuffer StrHtml = new StringBuffer();
		List plist = getAotcr().getParaMeters();
		for(int i=0; i< plist.size(); i++) {
			ZidyParam p = (ZidyParam)plist.get(i);
			String id = p.getId();
			if(id == null) {
				continue;
			}
			String paramName = p.getName();
			String ctrl = p.getCtrl();
			String cwidth = p.getCtrlWidth();
			String ptext = p.getPtext();
			String value = p.getFvalue();
			String sql = p.getDssql();
			if(!"".equals(ptext)) {
				ToolbarText tt = new ToolbarText(ptext);
				tb1.addText(tt);
			}
			StrSearch.append("+ \"" + paramName + "=\"+");
			//StrRefurbish.append("+ \"" + paramName + "=\"+");
			if(ZidyOperation.param_Ctrl_textField.equals(ctrl)) {
				TextField tf = new TextField();
				tf.setId("tf"+id);
				tf.setValue(value);
				tf.setWidth(cwidth);
				tb1.addField(tf);
				StrSearch.append("\"'\"+"+"tf"+id+".getValue()"+"+\"'=\"");
				StrSearch.append("+tf"+id+".getValue()"+"+\";\"");
			}else if(ZidyOperation.param_Ctrl_numberField.equals(ctrl)) {
				NumberField nf = new NumberField();
				nf.setId("nf"+id);
				nf.setValue(value);
				nf.setWidth(cwidth);
				tb1.addField(nf);
				StrSearch.append("nf"+id+".getValue()"+"+\"=\"");
				StrSearch.append("+nf"+id+".getValue()"+"+\";\"");
			}else if(ZidyOperation.param_Ctrl_dateField.equals(ctrl)) {
				DateField df = new DateField();
				df.setId("df"+id);
				df.setValue(value);
				df.setWidth(cwidth);
				tb1.addField(df);
				StrSearch.append("\"to_date('\"+"+"df"+id+".getValue().dateFormat('Y-m-d')"+"+\"','yyyy-mm-dd')=\"");
				StrSearch.append("+df"+id+".getValue().dateFormat('Y-m-d')"+"+\";\"");
			}else if(ZidyOperation.param_Ctrl_combo.equals(ctrl) ) {
				ComboBox cb = new ComboBox();
				cb.setId("db"+id);
				cb.setTransform("cb_db"+id);
				sql = ZidyOperation.getDataSourceSql(plist,sql);
				IDropDownModel idd = new IDropDownModel(sql);
				StrHtml.append("<select style='display:none' name = 'cb_db").append(id).append("'>");
				for(int m = 0 ; m < idd.getOptionCount() ; m++) {
					StrHtml.append("<option value='").append(((IDropDownBean)idd.getOption(m)).getStrId()).append("'");
					if(value.equals(((IDropDownBean)idd.getOption(m)).getValue())) {
						StrHtml.append(" selected");
					}
					StrHtml.append(">").append(((IDropDownBean)idd.getOption(m)).getValue()).append("</option>\n");
				}
				StrHtml.append("</select>");
				cb.setValue(value);
				cb.setWidth(cwidth);
				tb1.addField(cb);
				StrSearch.append("db"+id+".getValue()"+"+\"=\"");
				StrSearch.append("+db"+id+".getRawValue()"+"+\";\"");
			}
		}
		setHtml(StrHtml.toString());
		StrSearch.append(";\n");
		//StrRefurbish.append(";\n");
		String StrSearchClick =  StrSearch + "document.getElementById('CHANGE').value = change; document.getElementById('SearchButton').click();";
		String StrQitClick = MainGlobal.getOpenWinScript("Zidyfacsz&zidid="+visit.getString1());

		ToolbarButton sbtn = new ToolbarButton(null,"查询","function(){"+StrSearchClick+"}");
		ToolbarButton sqbtn = new ToolbarButton(null,"其它条件","function(){"+StrQitClick+"}"); 
		sbtn.setIcon(SysConstant.Btn_Icon_Search);
		sqbtn.setIcon(SysConstant.Btn_Icon_Show);
		tb1.addItem(sbtn);
		tb1.addItem(sqbtn);
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
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
	/*
	public String getToolbarDefaultValue() {
		StringBuffer sb = new StringBuffer();
		if(getToolbar() == null) {
			return sb.toString();
		}
		
		for(int i=0;i<getToolbar().getItems().size();i++) {
//			System.out.println(getToolbar().getItems().get(i).getClass().getPackage().getName());
			String ClassName = getToolbar().getItems().get(i).getClass().getName();
//			System.out.println(ClassName.substring(ClassName.lastIndexOf(".")));
			if(ComboBox.class.getName().equals(ClassName)) {
				ComboBox cb = (ComboBox)getToolbar().getItems().get(i);
				if(!"".equals(cb.getValue()))
				sb.append(cb.getId()).append(".setRawValue('").append(cb.getValue()).append("');\n");
			}
		}
		return "";//sb.toString();
	}
	*/
	public String getAotcrCss() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<getAotcr().getCss().size();i++) {
			sb.append(((ZidyCss)getAotcr().getCss().get(i)).getText()).append("\n");
		}
		return sb.toString();
	}
	
//	 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("id");
		//reportType = reportType + ;
		
		visit.setString1(reportType);
		boolean isInitToolbar = false; 
		if(reportType != null) {
			setAotcr(new Aotcr(reportType));
			getAotcr().setIPage(this);
			ZidyParam zdc = new ZidyParam();
			zdc.setId(null);
			zdc.setName("电厂ID");
			zdc.setSvalue(String.valueOf(visit.getDiancxxb_id()));
			getAotcr().getParaMeters().add(zdc);
			isInitToolbar = true;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			if(reportType == null) {
				setAotcr(new Aotcr("-99999"));
			}
			isInitToolbar = true;
		}
		if(isInitToolbar) {
			getAotcr().setData();
			initToolBars();
		}
		
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
}