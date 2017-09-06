/*
* ʱ�䣺2009-04-11
* ���ߣ�chh
* �޸����ݣ�_friqValue ֻ�ڵ�һ�ν���ҳ���Ǹ�ֵ,���Ĳ�ѯ���ں�_friqValue���仯���ݲ�ѯ����ȷ��
* ��Ϊ_friqValue��ÿ���ύҳ��ʱ��ֵ��������ֻ��һ�θ�ֵ��
*/

package com.zhiren.jt.zdt.gonggxx.shicdt;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

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

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


public class Haiyzszhcx  extends BasePage implements PageValidateListener{

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
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
	//��ʼ����v
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
		}
	}
	
//	��������v
	private Date _friqValue = DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
	private boolean _friqChange=false;
	public Date getfriqDate() {
		if (_friqValue==null){
			_friqValue =DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
		}
		return _friqValue;
	}
	
	public void setfriqDate(Date _value) {
		if (FormatDate(_friqValue).equals(FormatDate(_value))) {
			_friqChange=false;
		} else {
			_friqValue = _value;
			_friqChange=true;
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



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			_BeginriqValue = DateUtil.getDate(getZuiwrq());
			
			visit.setList1(null);
			this.getSelectData();
		}
		_friqValue = DateUtil.AddDate(getBeginriqDate(), -10, DateUtil.AddType_intDay);
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

	/**
	 * ���缯�ŵ�ú��Ϣ�ձ���
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		String riq = OraDate(_BeginriqValue);//��ǰ����
		String friq = OraDate(_friqValue);
		
// **ú��**
		//�����ͷ����
		Report rt = new Report();
		String[][] ArrHeader=null;
		 ArrHeader =new String[1][5];
		 ArrHeader[0]=new String[] {"��Ŀ����","�г��˼�","����ָ��","��������ǵ����(%)"};
		int[] ArrWidth;
		ArrWidth = new int[] {260,70,70,70};

		String sql="select  i.mingc itemmingc,yun.shicyj,yun.benzzs,yun.shangzxbzd \n"+
                   "from (select * from YUNJZSHQB yund where yund.riq="+riq+" ) yun,item i,itemsort it \n"+
                    "  where yun.item_id(+)=i.id\n"+
                     " and it.id=i.itemsortid\n"+
                     " and it.mingc in('�����˼�ָ��')\n"+
                     " order by i.xuh";
	    ResultSet rsl=   	cn.getResultSet(sql);
	/*	
//		�õ������ݣ�ʱ��
		StringBuffer strCol= new StringBuffer();
		strCol.append("select to_char("+riq+"-rownum+1,'MM')||'��'|| to_char("+riq+"-rownum+1,'dd')||'��' as ʱ��  \n");
		strCol.append(" from all_objects");
		strCol.append(" where rownum<=10 \n");
		strCol.append(" order by ʱ��");
		
//		�õ������ݣ�Ʒ��
		StringBuffer strRow = new StringBuffer();
		
		//--ʵ��װ��
		strRow.append("select distinct pz.mingc as Ʒ�� \n");
		strRow.append("from gangkscjgb g,pinzb pz where g.pinzb_id=pz.id  \n");
		strRow.append("and riq>="+friq+" and riq<="+riq+"\n");
		strRow.append("order by pz.mingc desc \n");
		
//		 �õ�ȫ������ ���̱�
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append(" select  riq as ʱ��, gk.mingc as Ʒ��, gk.jiag as �۸������� \n");
		sbsql.append(" from (select pz.mingc ,pinzb_id,to_char(riq, 'MM')||'��'||to_char(riq, 'dd')||'��' as riq,(jiagxx||'~'||jiagsx) as jiag \n");
		sbsql.append("       from gangkscjgb gk ,pinzb pz \n");
		sbsql.append("       where gk.riq >="+friq+" and gk.riq <="+riq+"  \n");
		sbsql.append("       and gk.pinzb_id = pz.id  \n");
		sbsql.append("       )  gk order by riq \n");
		
		cd.setRowNames("Ʒ��");
		cd.setColNames("ʱ��");
		cd.setDataNames("�۸�������");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 60;
		}
		ArrWidth[0] = 100;
		ArrWidth[1] = 60;*/
//		rt.setBody(cd.DataTable);
		Table tb = new Table(rsl,1, 0, 1);
		rt.setBody(tb);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(21);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;
		rt.setTitle("�����˼�ָ����", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.body.setCellValue(1, 0, "ú̿Ʒ��");
//		rt.body.setCellValue(1, 1, "ú̿Ʒ��");
		rt.setDefaultTitle(1, 2,"���λ(����):"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_LEFT);
		//ҳ�� 
		 rt.createDefautlFooter(ArrWidth);
		  rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		  rt.setDefautlFooter(4,2,"���:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(7,2,"�Ʊ�:",Table.ALIGN_LEFT);
		  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		
		
		
// **�۴�**
		Report rt_gc = new Report();
		String[][] ArrHeader_gc;
		ArrHeader_gc =new String[1][5];
		ArrHeader_gc[0]=new String[] {"��Ŀ����","�г��˼�","����ָ��","��������ǵ����(%)"};
		int[] ArrWidth_gc;
		ArrWidth_gc = new int[] {260,70,70,70};
		String sql_guonei="select  i.mingc itemmingc,yun.shicyj,yun.benzzs,yun.shangzxbzd \n"+
        "from (select * from YUNJZSHQB yund where yund.riq="+riq+" ) yun,item i,itemsort it \n"+
         "  where yun.item_id(+)=i.id\n"+
          " and it.id=i.itemsortid\n"+
          " and it.mingc in('�����˼�ָ��')\n"+
          " order by i.xuh";
           ResultSet rsl_guonei=   	cn.getResultSet(sql_guonei);
/*		
//		�õ������ݣ�ʱ��
		StringBuffer strCol_gc= new StringBuffer();
		strCol_gc.append("select to_char("+riq+"-rownum+1,'MM')||'��'|| to_char("+riq+"-rownum+1,'dd')||'��' as ʱ��  \n");
		strCol_gc.append(" from all_objects");
		strCol_gc.append(" where rownum<=10 \n");
		strCol_gc.append(" order by ʱ��");

//		�õ������ݣ��ۿ�,����
		StringBuffer strRow_gc = new StringBuffer();
		
		strRow_gc.append("select distinct c.mingc as �ۿ�,g.fenl as ���� \n");
		strRow_gc.append("from gangkgc g,chezxxb c where g.chezxxb_id=c.id  \n");
		strRow_gc.append("and riq>="+friq+" and riq<="+riq+"\n");
		strRow_gc.append("order by c.mingc desc,g.fenl desc \n");
		
//		 �õ�ȫ������ ���̱�
		StringBuffer sbsql_gc = new StringBuffer();
		
		sbsql_gc.append(" select  riq as ʱ��, gk.mingc as �ۿ�, gk.fenl as ����, gk.gangc as �۴�  \n");
		sbsql_gc.append(" from (select c.mingc ,chezxxb_id,to_char(riq, 'MM')||'��'||to_char(riq, 'dd')||'��' as riq,fenl,gangc \n");
		sbsql_gc.append("       from gangkgc gk ,chezxxb c \n");
		sbsql_gc.append("       where gk.riq >="+friq+" and gk.riq <="+riq+"  \n");
		sbsql_gc.append("       and gk.chezxxb_id = c.id  \n");
		sbsql_gc.append("       )  gk order by riq \n");
		
		cd.setRowNames("�ۿ�,����");
		cd.setColNames("ʱ��");
		cd.setDataNames("�۴�");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow_gc.toString(), strCol_gc.toString(), sbsql_gc.toString());
		ArrWidth_gc = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth_gc.length; i++) {
			ArrWidth_gc[i] = 60;
		}
		ArrWidth_gc[0] = 60;
		ArrWidth_gc[1] = 40;*/
//		rt_gc.setBody(cd.DataTable);
       	Table tb_guonei = new Table(rsl_guonei,1, 0, 1);
		rt_gc.setBody(tb_guonei);
		rt_gc.body.setWidth(ArrWidth_gc);
		rt_gc.body.setPageRows(21);
		rt_gc.body.setHeaderData(ArrHeader_gc);
		rt_gc.body.mergeFixedRowCol();
		rt_gc.body.ShowZero = true;
		rt_gc.setTitle("�����˼�ָ����", ArrWidth_gc);
		rt_gc.title.setRowHeight(2, 50);
		rt_gc.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt_gc.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt_gc.body.setCellValue(1, 0, "�ۿ�����");
//		rt_gc.body.setCellValue(1, 1, "�ۿ�����");
		rt_gc.setDefaultTitle(1, 2, "���λ(����):"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_LEFT);
						
//		ҳ�� 
		rt_gc.createDefautlFooter(ArrWidth_gc);
		rt_gc.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt_gc.setDefautlFooter(4,2,"���:",Table.ALIGN_LEFT);
		rt_gc.setDefautlFooter(7,2,"�Ʊ�:",Table.ALIGN_LEFT);
		rt_gc.setDefautlFooter(rt_gc.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
		
		
//		rt_gc.setDefautlFooter(1, 3,
//				"��ӡ����:"
//						+ FormatDate(DateUtil.getDate(DateUtil
//								.FormatDate(new Date()))), Table.ALIGN_LEFT);
//		rt_gc.setDefautlFooter(7, 1, "��λ:������", Table.ALIGN_RIGHT);
			
			_CurrentPage=1;
			_AllPages=1;
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml()+rt_gc.getAllPagesHtml();
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


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
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
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
//		tb1.addText(new ToolbarText("���ͣ�"));
		
		 ComboBox chaxunco=new ComboBox();
		 chaxunco.setTransform("WeizSelectx");
		 chaxunco.setWidth(100);
		 chaxunco.setLazyRender(true);
		 chaxunco.setId("chaxunco");
//		 tb1.addField(chaxunco);
		 tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
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

//	 ��������

//	 ���ҷ�ʽ�����˵�
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean9()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean9(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		
	
		list.add(new IDropDownBean(200, "�����˼�ָ��"));
		
		list.add(new IDropDownBean(201, "�����˼�ָ��"));
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(list));
	}
	public String getZuiwrq(){
		JDBCcon con=new JDBCcon();
		String sql="select to_char(yun.riq,'yyyy-mm-dd') maxriq from  YUNJZSHQB  yun,item i,itemsort it \n"+
                    " where yun.item_id =i.id\n"+
                     " and it.id=i.itemsortid\n";
//                     " and it.mingc in('"+this.getWeizSelectValue().getValue()+"')\n";
		
		 ResultSet rsl= con.getResultSet(sql);
		 String maxriq="";
		 try {
			if(rsl.next()){
				 maxriq=rsl.getString("maxriq");
			 }else{
				 maxriq=DateUtil.FormatDate(new Date());
			 }
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		  con.Close();
		  return maxriq;
	}
}