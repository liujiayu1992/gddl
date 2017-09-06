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
/*作者:王总兵
 *日期:2010-4-25 15:40:27
 *描述:修改数字中间对齐,修改小数位的显示位数,例如Mt是8，要显示成8.0，Stad是1.2，要显示成1.20
 */
/**
 * @author yinjm
 * 类名：入炉煤加权平均报表
 */

public class RulmjqpjReport extends BasePage implements PageValidateListener {
	
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
	
//	月份下拉框_开始
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	月份下拉框_结束
	
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
			"select nvl(formatxiaosws(round_new(sum(zl.qbad * zl.meil) / sum(zl.meil), 2),2), 0) qbad,\n" +
			"       nvl(formatxiaosws(round_new(sum(zl.qnet_ar * zl.meil) / sum(zl.meil), 2),2), 0) qnet_ar,\n" + 
			"       nvl(formatxiaosws(round_new(sum(zl.mt * zl.meil) / sum(zl.meil), 1), 1),0) mt,\n" + 
			"       nvl(formatxiaosws(round_new(sum(zl.mad * zl.meil) / sum(zl.meil), 2),2), 0) mad,\n" + 
			"       nvl(formatxiaosws(round_new(sum(zl.aad * zl.meil) / sum(zl.meil), 2),2), 0) aad,\n" + 
			"       nvl(formatxiaosws(round_new(sum(zl.vad * zl.meil) / sum(zl.meil), 2), 2),0) vad,\n" + 
			"       nvl(formatxiaosws(round_new(sum(zl.fcad * zl.meil) / sum(zl.meil), 2),2), 0) fcad,\n" + 
			"       nvl(formatxiaosws(round_new(sum(zl.stad * zl.meil) / sum(zl.meil), 2),2), 0) stad\n" + 
			"  from rulmzlb zl\n" + 
			" where to_char(zl.rulrq, 'yyyy-mm') = to_char(to_date('"+ getNianfValue().getValue() +"-"+ getYuefValue().getValue() +"', 'yyyy-mm'), 'yyyy-mm')";
		
		ResultSetList rslData = con.getResultSetList(sql);
		
		String[][] ArrHeader = new String[2][17];
		ArrHeader[0] = new String[]{"", "燃油分析情况", "燃油分析情况", "燃油分析情况", "燃油分析情况", "燃油分析情况", 
			"燃油分析情况", "燃煤分析情况", "燃煤分析情况", "燃煤分析情况", "燃煤分析情况", "燃煤分析情况", "燃煤分析情况",
			"燃煤分析情况", "燃煤分析情况", "飞灰可燃物", "飞灰可燃物"};
		ArrHeader[1] = new String[]{"", "Qb,ad<br>(Mj/Kg)", "Qnet,ar<br>(Mj/Kg)", "比重<br>g/ml", "粘度<br>(mm&sup2/s)", "闪点<br>℃", 
			"水份<br>(%)", "Qb,ad<br>(Mj/Kg)", "Qnet,ar<br>(Mj/Kg)", "Mt<br>(%)", "Mad<br>(%)", "Aad<br>(%)", "Vad<br>(%)", "Fc,ad<br>(%)", 
			"St,ad<br>(%)", "炉号", "分析结果"};
		int[] ArrWidth = new int[] {80, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 60};
		
		Table tb = new Table(3, 17);
		
		if (rslData.getRows() > 0) {
			String[][] strData = new String[1][8];
			for (int i = 0; i < 8; i ++) {
				strData[0][i] = rslData.getString(0, i);
			}
			for (int j = 0; j < 8; j ++) {
				tb.setCellValue(3, 8 + j, strData[0][j]);
				tb.setCellAlign(3, 8 + j, Table.ALIGN_CENTER);
			}
		}
		
		rt.setTitle("入炉煤加权平均值", ArrWidth);
		tb.setCellValue(3, 1, "加权<br>平均值");
		tb.setCellAlign(3, 1, Table.ALIGN_CENTER);
		
		tb.mergeCell(1, 2, 1, 7);
		tb.mergeCell(1, 8, 1, 15);
		tb.mergeCell(1, 16, 1, 17);
		tb.mergeCell(1, 1, 2, 1);

		tb.setWidth(ArrWidth);
		tb.setHeaderData(ArrHeader);
		rt.setBody(tb);
		rt.setDefaultTitle(1, 5, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(15, 3, "日期："+getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "总工程师：", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(5, 2, "生技部门：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 2, "专业：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(11, 2, "填表人：梁建芬", Table.ALIGN_CENTER);
		rt.setDefautlFooter(15, 3, "上报时间："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()), Table.ALIGN_LEFT);
		
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
		nf_comb.setWidth(60);
		nf_comb.setListWidth(60);
		nf_comb.setTransform("Nianf");
		nf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf_comb.setLazyRender(true);
		tbr.addField(nf_comb);
		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("月份："));
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(60);
		yf_comb.setListWidth(60);
		yf_comb.setTransform("Yuef");
		yf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		yf_comb.setLazyRender(true);
		tbr.addField(yf_comb);
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
			visit.setProSelectionModel3(null); // 月份下拉框
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}
}