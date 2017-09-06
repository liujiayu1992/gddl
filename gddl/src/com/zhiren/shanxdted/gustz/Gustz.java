package com.zhiren.shanxdted.gustz;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.Locale;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ����:tzf
 * ʱ��:2009-07-20
 * ����:ʵ�� ����̨��
 */
public class Gustz extends BasePage implements PageValidateListener {

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
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
		}
	}



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			visit.setList1(null);
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			this.getSelectData();
		}
		isBegin=true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;

	private String getBaseSql(){
		
		Visit visit=(Visit)this.getPage().getVisit();
		String riq_sql = "  f.daohrq >= to_date('" + getBeginriqDateSelect() + "','yyyy-MM-dd') \n"
		+ " and f.daohrq <= to_date('" + getEndriqDateSelect() + "','yyyy-MM-dd') \n";
		String gys_sql = "";
		String fahgl="";
		if(getGongysValue().getValue()=="ȫ��"){
			gys_sql = " ";
			fahgl=" ";
		}else{
			gys_sql = " where id =" + getGongysValue().getId() + " \n";
			fahgl="  and f.gongysb_id="+getGongysValue().getId() +"\n";
		}

		StringBuffer bf=new StringBuffer();
		
		bf.append(" select \n");
		bf.append(" decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n");
		bf.append(" decode(grouping(g.mingc)+grouping(fh.daohrq),2,'',1,'С��',to_char(fh.daohrq,'yyyy-MM-dd')) daohrq,\n");
		bf.append(" sum(round_new(fh.laimsl,"+visit.getShuldec()+")) laimsl,\n");
		bf.append(" sum(round_new(fh.biaoz,"+visit.getShuldec()+")) biaoz,\n");
		bf.append(" sum(round_new(fh.jingz,"+visit.getShuldec()+")) jingz,\n");
		bf.append("  decode(sum(round_new(fh.laimsl,"+visit.getShuldec()+")), 0, 0, round_new(sum(round_new(r.qnet_ar,"+visit.getShuldec()+") * round_new(fh.laimsl,"+visit.getShuldec()+"))/ sum(round_new(fh.laimsl,"+visit.getShuldec()+")),"+visit.getFarldec()+"))||'' as qnet_ar,\n");
		bf.append(" round_new(decode(sum(fh.laimsl),0,sum(fh.laimsl*r.meij))/sum(fh.laimsl),"+visit.getShuldec()+") meij,\n");
		bf.append(" round_new(sum(fh.laimsl*(r.meij-r.meijs)),"+visit.getShuldec()+") jik,\n");
		bf.append("   round_new(sum(fh.laimsl*r.meijs),"+visit.getShuldec()+") jiksk,\n");
		bf.append("   round_new(sum(fh.laimsl*r.meij),"+visit.getShuldec()+") jikhsj,\n");
		bf.append("   round_new(sum(fh.laimsl*(r.yunj-r.yunjs)),"+visit.getShuldec()+") yunjk,\n");
		bf.append("  round_new(sum(fh.laimsl*r.yunjs),"+visit.getShuldec()+") yunjs,\n");
		bf.append("  round_new(sum(fh.laimsl*r.yunj),"+visit.getShuldec()+") yunjhs,\n");
		bf.append(" round_new(sum(r.zaf+r.jiaohqzf+r.qitfy+r.daozzf),"+visit.getShuldec()+") zafhj,\n");
		bf.append("   round_new(sum(fh.laimsl*r.meij+fh.laimsl*r.yunj),"+visit.getShuldec()+") zongje,\n");
		bf.append("  round_new(sum(r.relzj),"+visit.getShuldec()+") relzj,\n");
		bf.append("   round_new(sum(r.liuzj),"+visit.getShuldec()+") liuzj,\n");
		bf.append("   round_new(sum(r.shuifzj),"+visit.getShuldec()+") shuifzj,\n");
		bf.append("   round_new(sum(r.huifzj),"+visit.getShuldec()+") huifzj\n");
		
		bf.append("  from   ruccb r,\n");
		bf.append(" (select * from fahb f  where "+riq_sql+fahgl+"\n");
		bf.append(" ) fh,\n");
		bf.append(" (select id,mingc  from gongysb "+gys_sql+") g\n");
		bf.append(" where r.fahb_id=fh.id and fh.gongysb_id=g.id and r.zhuangt in (0,1)\n");
		bf.append(" group by grouping sets('1',(g.mingc,fh.daohrq),(g.mingc))\n");
		bf.append("  order by grouping(g.mingc) desc,g.mingc asc,grouping(fh.daohrq) desc,fh.daohrq asc\n");

		
		
		return bf.toString();
	}
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
//		String riq=OraDate(_BeginriqValue);//��ǰ����
		
		
		StringBuffer strSQL = new StringBuffer();
		String sql =this.getBaseSql(); 


		strSQL.append(sql);
		 String ArrHeader[][]=new String[1][26];
		 ArrHeader[0]=new String[] {"��Ӧ��","��������","��ú��<br>(��)","Ʊ��<br>(��)","����<br>(��)","����<br>(MJ/kg)","����<br>����˰��","�ۿ���<br>(Ԫ)","�ۿ�˰��<br>(Ԫ)","��˰�ϼ�<br>(Ԫ)","����˰�˷�<br>(Ԫ)","�˷�˰��<br>(Ԫ)","�˷�<br>(Ԫ)","�ӷѺϼ�<br>(Ԫ)","�ܽ��<br>(Ԫ)","�����ۼ۽��<br>(Ԫ)","����ۼ۽��<br>(Ԫ)","ˮ���ۼ۽��<br>(Ԫ)","�ҷ��ۼ۽��<br>(Ԫ)"};

		 int ArrWidth[]=new int[] {180,90,65, 65,65,65, 60,60,60, 85,  85,85,85, 85,85,85, 85,80,80};

		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// ����
		
		
		Table tb=new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		
		String[] format=new String[]{"","","","","","","","","","","","","","","","","","",""};
		rt.setTitle("����ͳ��̨��", ArrWidth);
//		rt.setDefaultTitle(1, 2, "��λ����Ԫ/��", Table.ALIGN_LEFT);
		rt.body.setRowHeight(1, 40);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.setColFormat(format);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
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

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	
//	***************************�����ʼ����***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
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

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBeginriqDateSelect());
		df.Binding("riq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getEndriqDateSelect());
		df1.Binding("after", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��Ӧ��:"));
		ComboBox gys = new ComboBox();
		gys.setTransform("GongysDropDown");
		gys.setEditable(true);
		gys.setWidth(200);
		gys.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gys);
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.getElementById('RefurbishButton').click();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
//	 ��Ӧ��������
	public boolean _Gongyschange = false;
	public IDropDownBean getGongysValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGongysValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGongysModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();

		
			String sql ="select g.id,g.mingc from gongysb g,fahb f \n"
				+ " where f.gongysb_id = g.id \n"
				+ " 	and f.daohrq >= to_date('" + getBeginriqDateSelect() + "','yyyy-MM-dd') \n"
				+ " 	and f.daohrq <= to_date('" + getEndriqDateSelect() + "','yyyy-MM-dd') \n";
			
			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "ȫ��"));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
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


}
