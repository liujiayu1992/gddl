package com.zhiren.dc.rulgl.rulbb;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * 类名：入炉煤统计报表
 */

public class RulmtjReport extends BasePage implements PageValidateListener {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	年份下拉框_开始
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	年份下拉框_结束
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String sql = 
			"select substrb(rlmzl.rulrq, 4, 2) yuef,\n" +
			"       nvl(round_new(sum(rlmzl.qnet_ar * rlmzl.meil) / sum(rlmzl.meil) * 1000 / 4.1816, 0), 0) qnet_ar,\n" + 
			"       nvl(formatxiaosws(round_new(sum(rlmzl.stad * rlmzl.meil) / sum(rlmzl.meil), 2),2), 0) stad\n" + 
			"  from rulmzlb rlmzl\n" + 
			" where to_char(rlmzl.rulrq, 'yyyy') = '"+ getNianfValue().getValue() +"'\n" + 
			" group by substrb(rlmzl.rulrq, 4, 2)\n" + 
			" order by to_number(substrb(rlmzl.rulrq, 4, 2))";
		
		ResultSetList rslData =  con.getResultSetList(sql);
		String[][] ArrHeader = new String[1][13];
		ArrHeader[0] = new String[]{"月份", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
		int[] ArrWidth = new int[] {100, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
		
		Table tb = new Table(3, 13);
		
		String month = "";
		if (rslData.getRows() != 0) {
			
			String[][] strData = new String[rslData.getRows()][3];
			int temp = 0;
			while(rslData.next()) {
				strData[temp][0] = rslData.getString("yuef");
				strData[temp][1] = rslData.getString("qnet_ar");
				strData[temp][2] = rslData.getString("stad");
				temp ++;
			}
			
			for (int i = 0; i < rslData.getRows(); i ++) {
				month = strData[i][0].trim()+"月份";
				tb.setCellValue(2, Integer.parseInt(strData[i][0].trim())+1, strData[i][2]);
				tb.setCellValue(3, Integer.parseInt(strData[i][0].trim())+1, strData[i][1]);
			}
		}
		
		rt.setTitle(getNianfValue().getValue()+"年"+ month +"入炉煤热值、硫份加权统计表", ArrWidth);
		tb.setCellValue(2, 1, "St,ad(%)");
		tb.setCellAlign(2, 1, Table.ALIGN_CENTER);
		tb.setCellValue(3, 1, "Qnet,ar(卡/克)");
		tb.setCellAlign(3, 1, Table.ALIGN_CENTER);
		for (int i = 2; i <= 13; i ++) {
			tb.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		tb.ShowZero = true;
		tb.setWidth(ArrWidth);
		tb.setHeaderData(ArrHeader);
		rt.setBody(tb);
		//rt.setDefaultTitle(1, 4, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		//rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_CENTER);
		//rt.setDefautlFooter(9, 2, "审核：", Table.ALIGN_LEFT);
		//rt.setDefautlFooter(11, 2, "制表：", Table.ALIGN_CENTER);

		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rslData.close();
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public void getSelectData() {
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("年份："));
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(80);
		nf_comb.setTransform("Nianf");
		nf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf_comb.setLazyRender(true);
		tbr.addField(nf_comb);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "查询", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
		}
		getSelectData();
	}
}