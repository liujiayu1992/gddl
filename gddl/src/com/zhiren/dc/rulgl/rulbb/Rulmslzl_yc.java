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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author wangzongbing
 * 类名：阳城发电个性化入炉报表
 */

public class Rulmslzl_yc extends BasePage implements PageValidateListener {
	
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
	
//	开始日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

//	结束日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
	
	public String getPrintTable() {
		
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		
		
		//电厂入炉机组分组有(A系统,B1系统，B2系统,C系统,)统计的时候要把B2系统和C系统加权统计一次,另外两个不用加权统计.
		String sql = 
		

		"(select to_char(r.rulrq,'yyyy-mm-dd') as rulrq,\n" +
		"decode(j.mingc,null,'C系统B2系统',j.mingc) as mingc,\n" + 
		"sum(r.meil) as meil,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.mt*r.meil)/sum(r.meil),1)) as mt,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.mad*r.meil)/sum(r.meil),2)) as mad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.aad*r.meil)/sum(r.meil),2)) as aad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.ad*r.meil)/sum(r.meil),2)) as ad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.vad*r.meil)/sum(r.meil),2)) as vad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.vdaf*r.meil)/sum(r.meil),2)) as vdaf,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.fcad*r.meil)/sum(r.meil),2)) as fcad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.stad*r.meil)/sum(r.meil),2)) as stad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.std*r.meil)/sum(r.meil),2)) as std,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.had*r.meil)/sum(r.meil),2)) as had,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.qbad*r.meil)/sum(r.meil),2)*1000) as qbad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.qgrd*r.meil)/sum(r.meil),2)*1000) as qgrd,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.qnet_ar*r.meil)/sum(r.meil)*1000/4.1816,0)) as qnet_ar\n" + 
		"from rulmzlb r,jizfzb j\n" + 
		"where r.jizfzb_id=j.id\n" + 
		"and r.rulrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
		"and r.rulrq<=to_date('"+getERiq()+"','yyyy-mm-dd')\n" + 
		"and r.shenhzt=3\n" + 
		"and j.id in (2641500,264103047)\n" + 
		"group by rollup(r.rulrq,j.mingc)\n" + 
		"having  (grouping(r.rulrq)+grouping(j.mingc)=1))\n" + 
		"union\n" + 
		"(select decode(r.rulrq,null,'总计',to_char(r.rulrq,'yyyy-mm-dd')) as rulrq,\n" + 
		"decode(grouping(j.mingc)+grouping(r.rulrq),1,'日小计',j.mingc) as mingc,\n" + 
		"sum(r.meil) as meil,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.mt*r.meil)/sum(r.meil),1)) as mt,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.mad*r.meil)/sum(r.meil),2)) as mad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.aad*r.meil)/sum(r.meil),2)) as aad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.ad*r.meil)/sum(r.meil),2)) as ad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.vad*r.meil)/sum(r.meil),2)) as vad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.vdaf*r.meil)/sum(r.meil),2)) as vdaf,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.fcad*r.meil)/sum(r.meil),2)) as fcad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.stad*r.meil)/sum(r.meil),2)) as stad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.std*r.meil)/sum(r.meil),2)) as std,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.had*r.meil)/sum(r.meil),2)) as had,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.qbad*r.meil)/sum(r.meil),2)*1000) as qbad,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.qgrd*r.meil)/sum(r.meil),2)*1000) as qgrd,\n" + 
		"decode(sum(r.meil),0,0,round_new(sum(r.qnet_ar*r.meil)/sum(r.meil)*1000/4.1816,0)) as qnet_ar\n" + 
		"from rulmzlb r,jizfzb j\n" + 
		"where r.jizfzb_id=j.id\n" + 
		"and r.rulrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')\n" + 
		"and r.rulrq<=to_date('"+getERiq()+"','yyyy-mm-dd')\n" + 
		"and r.shenhzt=3\n" + 
		"group by rollup(r.rulrq,j.mingc))\n" + 
		"order by rulrq,mingc";



		ResultSetList rslData = con.getResultSetList(sql);
		String[][] ArrHeader = new String[1][16];
		ArrHeader[0] = new String[]{"入炉日期", "系统", "煤量","Mt(%)","Mad(%)","Aad(%)","Ad(%)",
				"Vad(%)","Vdaf(%)","Fc,ad(%)","St,ad(%)","S,td(%)","Had(%)",
				"Qb,ad(j/g)","Qgr,d(j/g)","Qnet,ar(卡/克)"};
		
		int[] ArrWidth = new int[] {80, 80, 60, 50,50,50,50,50,50, 50,50,50,50,55,55, 55};
		
		rt.setTitle(((Visit)this.getPage().getVisit()).getDiancmc()+"入炉煤化验煤量热值表", ArrWidth);
		rt.setBody(new Table(rslData, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);
		rt.body.ShowZero = true;
		for (int i = 1; i <= rt.body.getCols(); i ++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		//rt.setDefaultTitle(1, 4, "制表单位："+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(1, 4, "入炉日期："+getBRiq()+"至"+getERiq(), Table.ALIGN_LEFT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.Formatdate("yyyy年MM月dd日", new Date()),Table.ALIGN_LEFT);
//		rt.setDefautlFooter(4, 2, "审核：", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(7, 2, "制表：", Table.ALIGN_LEFT);
		
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
		tbr.addText(new ToolbarText("入炉日期："));
		DateField bdf = new DateField();
		bdf.setValue(getBRiq());
		bdf.Binding("BRIQ", "");
		bdf.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRiq').value = newValue.dateFormat('Y-m-d'); " +
		" document.forms[0].submit();}");
		tbr.addField(bdf);
		
		tbr.addText(new ToolbarText("至"));
		DateField edf = new DateField();
		edf.setValue(getERiq());
		edf.Binding("ERIQ", "");
		tbr.addField(edf);
		edf.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERiq').value = newValue.dateFormat('Y-m-d'); " +
		" document.forms[0].submit();}");
		tbr.addText(new ToolbarText("-"));
		
		
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
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			
			setBRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			setERiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		getSelectData();
	}
}