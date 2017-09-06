package com.zhiren.zaig;

import java.sql.ResultSet;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 2013-07-04
 * ���
 * ����sql
 */
/*
 * 2013-07-18
 * ���
 * ����sql
 */
public abstract class Yuezgmxcx extends BasePage implements PageValidateListener {

	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
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


	public String getPrintTable() {
		setMsg(null);
		return getPrint();
	}
	
	private boolean _QueryClick = false;
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}
	
	/*private boolean _ShangbClick = false;
	public void ShangbButton(IRequestCycle cycle) {
		_ShangbClick = true;
	}*/

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getSelectData();
		}
		/*if (_ShangbClick) {
			_ShangbClick = false;
			getShangb();
			getSelectData();
		}*/
	}
	

	
	private String getSql(){
		Visit v = (Visit) getPage().getVisit();
		
		String sql = 
			"select decode(grouping(mk.mingc),1,'�ϼ�',mk.mingc) meikxxb_id,\n" + 
			"       sum(zg.meil_sy) meil_sy,\n" + 
			"       sum(zg.meil_rc) meil_rc,\n" + 
			"       sum(zg.meil_js) meil_js,\n" + 
			"       sum(zg.meil_zg) meil_zg,\n" + 

			"round( decode(sum(decode(zg.jiesdj_hs, 0, 0, zg.meil_zg)),\n" +
			"                             0,\n" + 
			"                             0,\n" + 
			"                             sum(zg.jiesdj_hs *\n" + 
			"                                 decode(zg.jiesdj_hs, 0, 0, zg.meil_zg)) /\n" + 
			"                             sum(decode(zg.jiesdj_hs, 0, 0, zg.meil_zg))) ,2) jiesdj_hs,"+


			"round( decode(sum(decode(zg.jiesdj_hs, 0, 0, zg.meil_zg)),\n" +
			"                              0,\n" + 
			"                              0,\n" + 
			"                              sum(zg.jiesdj_hs *\n" + 
			"                                  decode(zg.jiesdj_hs, 0, 0, zg.meil_zg)) /\n" + 
			"                              sum(decode(zg.jiesdj_hs, 0, 0, zg.meil_zg)))/1.17 ,2) jiesdj_bhs,"+

			"       sum(zg.hej_hs) hej_hs,\n" + 
			"       sum(zg.hej_bhs) hej_bhs\n" + 
			"  from yuezgmxb zg, meikxxb mk\n" + 
			" where zg.meikxxb_id = mk.id\n" + 
			"   and zg.riq = to_date('"+getYearValue().getValue()+"-"+getMonthValue().getValue()+"', 'yyyy-mm')\n" + 
			"group by rollup(mk.mingc) order by grouping(mk.mingc),mk.mingc";

		return sql.toString();
	}
	
	private String getPrint() {
		int PageRows=30;
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		
		ResultSet rstmp = con.getResultSet(getSql());
		int[] ArrWidth=null;
		String[][] ArrHeader=null;
		ArrHeader = new String[1][9];
		ArrHeader[0] = new String[] { "���", "����<br>����ú��", "����<br>����ú��","����<br>����ú��","����<br>�ݹ�ú��",
									  "����㵥��<br>(��˰)","����㵥��<br>(����˰)","���ϼ�<br>(��˰)","���ϼ�<br>(����˰)"};
		ArrWidth = new int[] { 110, 80, 80, 80, 80, 80,80,100,100};

		rt.createTitle(2, ArrWidth);
		rt.title.setRowHeight(1, 9);
		rt.title.setCellValue(1, 1, getYearValue().getValue() + "��" + getMonthValue().getValue() + "��ȼú�ݹ���ϸ��", ArrWidth.length);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 16);
		rt.title.setCellAlign(1, 1, Table.ALIGN_CENTER);//setCellAlign
		

		rt.setBody(new Table(rstmp, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.createDefautlFooter(ArrWidth);
		
		
		rt.body.setPageRows(PageRows);	
		//rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_RIGHT);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		
		rt.body.ShowZero=true;

		//rt.setDefautlFooter(1, 5, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 1, "��ˣ�", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 1, "�Ʊ�", Table.ALIGN_CENTER);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;

		
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		con.Close();
		rt.body.setRowHeight(20);
		//rt.body.setRowHeight(1,20);
		//rt.setOrientation(Report.PAPER_Landscape);
		//rt.setMargin(0, 0, 0, 0);
		return rt.getAllPagesHtml();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("���:"));
		ComboBox year = new ComboBox();
		year.setTransform("YearSelect");
		year.setEditable(true);
		year.setWidth(80);
		tb1.addField(year);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox month = new ComboBox();
		month.setTransform("MonthSelect");
		month.setEditable(true);
		month.setWidth(60);
//		month.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(month);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
		"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		
		/*tb1.addText(new ToolbarText("-"));
		String shangb = "function(){document.getElementById('ShangbButton').click();Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});}";
		ToolbarButton sbsj = new ToolbarButton(null,"�������ϱ���", shangb);
		sbsj.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		boolean isSuod=false;
		ResultSetList rsl = UtilZhib.getThisJihRsl(getUzb(), false, ZBConstant.JH_zhuangt1,"jihsjb");
		if (rsl.next()) {
			isSuod = true;// �����������Ͳ������ð�ť
		}
		if(!isSuod){
			tb1.addItem(sbsj);
		}else{
			tb1.addText(new ToolbarText("�������ϱ�"));
		}*/
		
		
		setToolbar(tb1);
	}
	//��������
	public IDropDownBean getYearValue() {
		int _nianf = DateUtil.getYear(new Date());
		int _yuef = DateUtil.getMonth(new Date());
		if(_yuef==1 ){
			_nianf=_nianf-1;
		}
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getYearModel().getOptionCount(); i++) {
				Object obj = getYearModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean4((IDropDownBean) getYearModel()
									.getOption(i));
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYearValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public void setYearModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYearModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYearModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getYearModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select yvalue id,ylabel mingc from nianfb");
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql.toString()));
	}
	//��������
	public IDropDownBean getMonthValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			int _yuef=DateUtil.getMonth(new Date());
			if (1 == _yuef) {
				((Visit) getPage().getVisit())
						.setDropDownBean6((IDropDownBean) getMonthModel()
								.getOption(11));
			} else {
				((Visit) getPage().getVisit())
						.setDropDownBean6((IDropDownBean) getMonthModel()
								.getOption(_yuef-2));
			}
//		((Visit) getPage().getVisit())
//				.setDropDownBean6((IDropDownBean) getMonthModel()
//						.getOption(_yuef-1));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setMonthValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean6()) {
			((Visit) getPage().getVisit()).setDropDownBean6(Value);
		}
	}

	public void setMonthModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getMonthModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getMonthModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getMonthModels() {
		StringBuffer sql = new StringBuffer();
		sql.append("select mvalue id,mlabel mingc from yuefb");
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql.toString()));
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
	
	/*private UtilZhibBean getUzb(){
		Visit visit = (Visit) getPage().getVisit();
		String[] riq = Utilbsh.getAdd_months(getYearValue().getValue(),
				getMonthValue().getValue(), ZBConstant.ZBLXCZ_jy);
		UtilZhibBean uzb = new UtilZhibBean(visit, ZBConstant.WD_month, getYearValue()
				.getValue(), getMonthValue().getValue(), "1");
		uzb.setLrleix(ZBConstant.LR_jy);
		uzb.setSbyear(riq[0]);
		uzb.setSbmonth(riq[1]);
		uzb.setZhiblx(ZBConstant.LX_jh);
		uzb.setZhuangt(ZBConstant.JH_zhuangt0+","+ZBConstant.JH_zhuangt1);
		return uzb;
	}*/

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setYearValue(null);
			setYearModel(null);
			getYearModels();
			setMonthValue(null);
			setMonthModel(null);
			getMonthModels();
			visit.setDropDownBean4(null);
			visit.setDropDownBean6(null);
			getSelectData();
		}
//		getSelectData();
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
}
