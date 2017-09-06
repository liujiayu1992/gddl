package com.zhiren.pub.image;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.FileUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author ly
 *
 */
public class ImageReport  extends BasePage implements PageValidateListener{

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
	
	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getEndriqDateSelect() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setEndriqDateSelect(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

	}
	
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}



//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		//获得跳转页面信息
		String mk = cycle.getRequestContext().getParameter("mk");
		if(mk != null) {
			setMk(mk);
			init();
		}
		String id = cycle.getRequestContext().getParameter("id");
		if(id != null) {
			setId(id);
			init();
		}
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			File tmp = getImageFilePath();
			if(!tmp.exists()){
				tmp.mkdirs();
			}
			init();
			visit.setList1(null);
			this.getSelectData();
		}
		isBegin=true;

		
	}
	
	public String getMk(){
		return ((Visit) this.getPage().getVisit()).getString1();
	}
	
	public void setMk(String leix){
		((Visit) this.getPage().getVisit()).setString1(leix);
	}
	
	public String getId(){
		return ((Visit) this.getPage().getVisit()).getString2();
	}
	
	public void setId(String leix){
		((Visit) this.getPage().getVisit()).setString2(leix);
	}
	
	public IPropertySelectionModel getFileModel() {
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setFileModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public File getImageFilePath(){
		return new File(MainGlobal.getXitsz("系统文件夹位置", ""+((Visit) getPage().getVisit()).getDiancxxb_id(), "D:/zhiren/") + "/" + getMk());
	}
	public File getImageTmpPath(){
		return new File(MainGlobal.getWebAbsolutePath().getParentFile() + "/img/tmp/" + getMk() + "/");
	}
	public String getImagePath(String fileName){
		String imgPath = "#";
		if(fileName != null
				&& !"".equals(fileName)){
			imgPath = "img/tmp/" + getMk() +"/" +
			fileName;
		}
		return  imgPath;//"imgs/login/spacer.gif";
	}
	public void init(){
		JDBCcon con = new JDBCcon();
		String sql = "select * from tupccb where bianm = '" + getId() + "'";
		ResultSetList rsl = con.getResultSetList(sql);
		List fileModel = new ArrayList();
		while(rsl.next()){
			String srcFileName = getId() + "_" + rsl.getInt("xuh") 
				+ FileUtil.getSuffix(rsl.getString("mingc"));
			File srcFile = new File(getImageFilePath(),srcFileName);
			try{
				fileModel.add(new IDropDownBean(rsl.getString("xuh"),
						rsl.getString("mingc")));
				FileUtil.copy(srcFile, getImageTmpPath().getPath());
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		setFileModel(new IDropDownModel(fileModel));
		rsl.close();
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;
	

	private String getSelectData(){
		JDBCcon con = new JDBCcon();
		String picstr = "";
		String sql = "select * from tupccb where bianm = '" + getId() + "'";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl==null){
			return " ";
		}
		while(rsl.next()){
			String srcFileName = getId() + "_" + rsl.getInt("xuh") 
			+ FileUtil.getSuffix(rsl.getString("mingc"));
			picstr+="<p align=\"center\">"+
				"<img src='"+ //MainGlobal.getHomeContext(this)
				getImagePath(srcFileName) + "' />";
		}
		rsl.close();
		return picstr;
	}

	//	******************************************************************************
	
	
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

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	
//	***************************报表初始设置***************************//
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