package com.zhiren.haiym.baob;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Xiemsjcx extends BasePage {
	public boolean getRaw() {
		return true;
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

	// ***************������Ϣ��******************//
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

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			getSelectData();
		}

	}

	private boolean isBegin=false;
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getXiem();
	
	}
//	******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			setRiqi(null);
			setRiq2(null);
			visit.setboolean3(false);
			getSelectData();
		}
		getSelectData();
		isBegin=true;
	}

	private String getXiem() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
			sbsql.
			append(

					"select\n" +
					"     fahb.yundh as yundh,\n" + 
					"     to_char(fahb.kaobrq,'YYYY-MM-DD') as kaobrq,\n" + 
					"     fahb.biaoz as yundl,\n" + 
					"     luncxxb.mingc as huocmc,\n" + 
					"     fahb.chec as hangc,\n" + 
					"     to_char(fahb.xiemkssj,'YYYY-MM-DD hh24:mi:ss')as xiemkssj,\n" + 
					"     to_char(fahb.xiemjssj,'YYYY-MM-DD hh24:mi:ss')as xiemjssj,\n" + 
					"     to_char(fahb.zhuanggkssj,'YYYY-MM-DD hh24:mi:ss') as librq\n" + 
					" from fahb,luncxxb,gongysb,meikxxb,yunsfsb\n" + 
					"     where fahb.luncxxb_id=luncxxb.id(+)\n" + 
					"     and fahb.meikxxb_id=meikxxb.id(+)\n" + 
					"     and fahb.gongysb_id=gongysb.id(+)\n" + 
					"     and fahb.yunsfsb_id=yunsfsb.id(+)\n" + 
					"     and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+getRiqi()+"'\n" + 
					"     and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+getRiq2()+"'\n" + 
					"    group by(fahb.yundh,luncxxb.mingc,fahb.chec,fahb.kaobrq ,fahb.biaoz,fahb.yunsl,xiemjssj,xiemkssj,fahb.zhuanggkssj)\n" + 
					"    order by fahb.kaobrq");

			
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][8];
		ArrHeader[0] = new String[] { "�˵����","��������","�˵���","��������","����","жú��ʼʱ��","жú����ʱ��","�벴" };

		int ArrWidth[] = new int[] { 70,100,50,60,50,120,120,120};
		
		rt.setTitle("жú��ҵ�ƻ�", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 3, getRiqi()+"��"+getRiq2(),Table.ALIGN_CENTER);
		rt.setDefaultTitle(7, 2, "��λ:��", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 2,"�Ʊ�:", Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ʼ����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��������:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
		"function(){document.Form0.submit();}");
        tb1.addItem(tb);
        tb1.addText(new ToolbarText("-"));

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

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}
}
