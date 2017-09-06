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

public class Yundxx_hj extends BasePage {
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	public String getPrintTable() {
	 if(getLeixValue().getValue().equals("����")){
			return getYundxx_hz();
		}
	 else if(getLeixValue().getValue().equals("��ϸ")){
		    return getYundxx_mx();
	 }
		 else {
			return "�޴˱���";
		}
	}
	private String getYundxx_mx() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
			sbsql.
			append(
					"select\n" +
					" gongysb.quanc as gonghdw,\n" + 
					"       luncxxb.mingc as huocmc ,\n" + 
					"       fahb.yundh as yunsbh,\n" + 
					"       chezxxb.mingc as faz,\n" + 
					"       kaobrq as daobrq,\n" + 
					"       fahb.daohrq as librq,\n" + 
					"       luncxxb.dunw as tingkbw,\n" + 
					"       yunsfsb.mingc as yunsdw,\n" + 
					"       pinzb.mingc as pinz,\n" + 
					"       fahb.biaoz as huopzl,\n" + 
					"       fahb.maoz as yanszl,\n" + 
					"       hetb.hetbh as caightbh,\n" + 
					"       hetys.mingc as caightmc\n" + 
					"   from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb\n" + 
					"   where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+)\n" + 
					"   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+)\n" + 
					"   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)\n" + 
					"   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)\n" + 
					"   and fahb.id = hetys.id(+)\n"+
					"   and yunsfsb.mingc='����' and fahb.jiesb_id=0\n" + 
					"   and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+getRiqi()+"'\n" + 
					"   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+getRiq2()+"'\n"+
					"   order by gongysb.quanc,luncxxb.mingc");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][13];
		ArrHeader[0] = new String[] { "������λ","��������","������","��վ","��������","�벴����","ͣ����λ",
				
                                       "���䵥λ","Ʒ��","��Ʊ����","��������","�ɹ���ͬ���","�ɹ���ͬ����" };

		int ArrWidth[] = new int[] { 160,70,60,50,100,100,60,60,50,60,60,90,120};
		
		rt.setTitle("�˵���Ϣ", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 3, getRiqi()+"-"+getRiq2(),Table.ALIGN_CENTER);
		rt.setDefaultTitle(12,2, "��λ:��", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 3, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(12, 2,"�Ʊ�:", Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	private String getYundxx_hz() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
			sbsql
			.append(
					"select\n" +
					"       decode(grouping(gongysb.quanc),1,'�ܼ�',gongysb.quanc) as gonghdw,\n" + 
					"       decode(grouping(gongysb.quanc)+grouping(luncxxb.mingc),2,'',1,'�ϼ�',\n" + 
					"       decode(grouping(fahb.yundh),1,luncxxb.mingc||'С��',luncxxb.mingc)) as huocmc,\n" + 
					"       fahb.yundh as yunsbh,\n" + 
					"       chezxxb.mingc as faz,\n" + 
					"       kaobrq as daobrq,\n" + 
					"       fahb.daohrq as librq,\n" + 
					"       luncxxb.dunw as tingkbw,\n" + 
					"       yunsfsb.mingc as yunsdw,\n" +
					"       pinzb.mingc as pinz,\n" + 
					"       sum(fahb.biaoz)  as huopzl,\n" + 
					"       sum(fahb.maoz) as yanspz,\n" + 
					"       hetb.hetbh as caightbh,\n" + 
					"       hetys.mingc as caightmc\n" + 
					"   from fahb,zhilb,luncxxb,chezxxb,gongysb,meikxxb,pinzb,hetb,hetys,yunsfsb\n" + 
					"   where fahb.luncxxb_id=luncxxb.id(+) and fahb.zhilb_id=zhilb.id(+)\n" + 
					"   and fahb.faz_id=chezxxb.id(+) and fahb.meikxxb_id=meikxxb.id(+)\n" + 
					"   and fahb.pinzb_id=pinzb.id(+) and fahb.hetb_id=hetb.id(+)\n" + 
					"   and fahb.yunsfsb_id=yunsfsb.id(+) and fahb.gongysb_id=gongysb.id(+)\n" + 
					"   and fahb.id = hetys.id(+)\n"+
					"   and yunsfsb.mingc='����' and fahb.jiesb_id=0\n" + 
				    "   and to_char(fahb.daohrq,'YYYY-MM-DD')>='"+getRiqi()+"'\n" + 
					"   and to_char(fahb.daohrq,'YYYY-MM-DD')<='"+getRiq2()+"'\n"+
					"   group by rollup(gongysb.quanc,luncxxb.mingc,fahb.yundh,chezxxb.mingc,kaobrq,fahb.daohrq,luncxxb.dunw,yunsfsb.mingc,pinzb.mingc,hetb.hetbh,hetys.mingc)\n" + 
					"   having not (grouping(fahb.yundh) || grouping(hetys.mingc)) =1\n" + 
					"   order by grouping(gongysb.quanc) desc,gongysb.quanc,grouping(luncxxb.mingc) desc,grouping(luncxxb.dunw) desc,luncxxb.mingc\n");

		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][13];
		ArrHeader[0] = new String[] {"������λ","��������","������","��վ","��������","�벴����","ͣ����λ",
			
                                       "���䵥λ","Ʒ��","��Ʊ����","��������","�ɹ���ͬ���","�ɹ���ͬ����" };

		int ArrWidth[] = new int[] {160,70,60,50,100,100,60,60,50,60,60,90,120};
		
		rt.setTitle("�˵���Ϣ_����", ArrWidth);
		rt.setDefaultTitle(1, 3, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 3, getRiqi()+"-"+getRiq2(),Table.ALIGN_CENTER);
		rt.setDefaultTitle(12,2, "��λ:��", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "�Ʊ�ʱ��:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 3, "���:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(12, 2,"�Ʊ�:", Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			getTingbModels();
			setTingbValue(null);
			getTingbModel();
			getLeixModels();
			setLeixValue(null);
			getLeixModel();
			setRiqi(null);
			setRiq2(null);
			visit.setboolean3(false);
			getSelectData();
		}
		getSelectData();
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

	// ����

	public IDropDownBean getLeixValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLeixModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLeixValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getLeixModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getLeixModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setLeixModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLeixModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			List.add(new IDropDownBean(0, "��ϸ"));
			List.add(new IDropDownBean(1, "����"));

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	// ͣ������
	public IDropDownBean getTingbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getTingbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setTingbValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setTingbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getTingbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getTingbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getTingbModels() {

		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			List.add(new IDropDownBean(0, "�벴"));
			List.add(new IDropDownBean(1, "����"));
			List.add(new IDropDownBean(2, "ͣ��"));
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("����:"));
		ComboBox tk = new ComboBox();
		tk.setTransform("TingbDropDown");
		tk.setEditable(true);
		tk.setWidth(100);
		tk.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(tk);
		setToolbar(tb1);
		tb1.addText(new ToolbarText("-"));

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

		tb1.addText(new ToolbarText("����:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("LeixDropDown");
		leix.setEditable(true);
		leix.setWidth(100);
		leix.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(leix);
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
