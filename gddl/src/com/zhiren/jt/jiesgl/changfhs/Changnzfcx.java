package com.zhiren.jt.jiesgl.changfhs;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author ly
 *
 */
public class Changnzfcx  extends BasePage implements PageValidateListener{

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
	
	
//	 �����ʼ
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
//	��ݽ���
	private static IPropertySelectionModel _bNianfModel;

	public IPropertySelectionModel getbNianfModel() {
		if (_bNianfModel == null) {
			getbNianfModels();
		}
		return _bNianfModel;
	}

	private IDropDownBean _bNianfValue;

	public IDropDownBean getbNianfValue() {
		if (_bNianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getbNianfModel().getOptionCount(); i++) {
				Object obj = getbNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_bNianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _bNianfValue;
	}

	public void setbNianfValue(IDropDownBean Value) {
		if (_bNianfValue != Value) {
			_bNianfValue = Value;
		}
	}

	public IPropertySelectionModel getbNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_bNianfModel = new IDropDownModel(listNianf);
		return _bNianfModel;
	}

	public void setbNianfModel(IPropertySelectionModel _value) {
		_bNianfModel = _value;
	}
	
//	 �·���ʼ
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel5() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean5((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean5();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean5()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean5(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel5(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel5(_value);
	}
	
//	�·ݽ���
	public boolean Changebyuef = false;

	private static IPropertySelectionModel _bYuefModel;

	public IPropertySelectionModel getbYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel4() == null) {
			getbYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}

	private IDropDownBean _bYuefValue;

	public IDropDownBean getbYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getbYuefModel().getOptionCount(); i++) {
				Object obj = getbYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean4((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean4();
	}

	public void setbYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean4()!= null) {
			id = getbYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changebyuef = true;
			} else {
				Changebyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean4(Value);
		
	}

	public IPropertySelectionModel getbYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel4(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}

	public void setbYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel4(_value);
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
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
//			getSelectData();
		}
		
	}



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean4(null);
			setChangbValue(null);
			setChangbModel(null);
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
	
	private String getDiancmc(){
		JDBCcon cn = new JDBCcon();
		String sql_dc = "";
		String qc = "";
		sql_dc = "select quanc from diancxxb where id = " + getTreeid_dc();
		ResultSetList rsl = cn.getResultSetList(sql_dc);
		if(rsl.next()){
			qc = rsl.getString("QUANC");
		}
		cn.Close();
		return qc;
	}

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
//		String riq = OraDate(_BeginriqValue);//��ǰ����
//		String friq = OraDate(_friqValue);
		
// **ú��**
		//�����ͷ����
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
//		ArrWidth = new int[] {100,50,50,50,50,50,50,50,50,50,50};
		
		String SJ_sql = "";
		int iy = (int)getYuefValue().getId();
		int by = (int)getbYuefValue().getId();
		int yue = (Integer.parseInt(getbNianfValue().getValue())-Integer.parseInt(getNianfValue().getValue()))*12+(by-iy)+4;
		String y_bt = "";
		String sj = "";
		ArrWidth = new int[yue+1];
		ArrWidth[0] = 100;
		for(int i=1;i<=yue;i++){
			ArrWidth[i] = 60;
		}
		
		if(getNianfValue().getValue().equals(getbNianfValue().getValue())){
			if(getYuefValue().getValue().equals(getbYuefValue().getValue())){
				sj = getNianfValue().getValue() + "��" + getYuefValue().getValue() + "��";
			}else{
				sj = getNianfValue().getValue() + "��" + getYuefValue().getValue() + "����" + getbYuefValue().getValue() + "��";
			}
			
			y_bt = "to_number(to_char(z.qiszyfw,'MM'))||'��'"; //"to_char(z.qiszyfw,'MM')||'��'"; 
			SJ_sql = 
				"select ʱ�� from (\n" +
				"select rownum as id,("+iy+"+rownum-1)||'��' as ʱ�� from all_objects where rownum<="+(by-iy+1)+"\n" + 
				"union\n" + 
				"select 0 as id,'�ϼ�' as ʱ�� from dual\n" + 
				"union\n" + 
				"select -1 as id,'ͬ��' as ʱ�� from dual\n" + 
				"union\n" + 
				"select -2 as id,'ͬ��' as ʱ�� from dual\n" + 
				")\n" + 
				"order by decode(ʱ��, 'ͬ��',1,'ͬ��',2, '�ϼ�',3,4) desc,id\n";
		}else{
			sj = getNianfValue().getValue() + "��" + getYuefValue().getValue() + "����" + getbNianfValue().getValue() + "��" + getbYuefValue().getValue() + "��";
			y_bt = "to_char(z.qiszyfw,'yyyy')||'��'||to_number(to_char(z.qiszyfw,'MM'))||'��'";
			SJ_sql += " select ʱ�� from (\n";
			int year = Integer.parseInt(getNianfValue().getValue());
			int y = (int)getYuefValue().getId();
			for(int i=1;i<=(yue-3);i++){
				if((y+i-1)==13){
					year+=1;
					y-=12;
				}
				SJ_sql += "select "+i+" as id,"+year+"||'��'||"+(y+i-1)+"||'��' as ʱ�� from dual\n" +
						  "union\n";
			}
			SJ_sql +=
				"select 0 as id,'�ϼ�' as ʱ�� from dual\n" +
				"union\n" + 
				"select -1 as id,'ͬ��' as ʱ�� from dual\n" + 
				"union\n" + 
				"select -2 as id,'ͬ��' as ʱ�� from dual\n" + 
				")\n" + 
				"order by decode(ʱ��, 'ͬ��',1,'ͬ��',2, '�ϼ�',3,4) desc,id\n";
		}
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		Date dt=new Date();
		try{
			dt=sf.parse(getbNianfValue().getValue()+"-"+by+"-01");
		}catch(Exception e){
			e.printStackTrace();
		}
		String changb = "";
		if(getChangbValue().getValue().equals("ȫ��")){
			changb = " ";
		}else{
			changb = "and z.feiylb_item_id = " + getChangbValue().getId() + "\n";
		}
		String MC_sql = 
					"select ���� from (\n" +
					"select i.id,i.mingc as ����\n" +
					"from changnzfb z,item i\n" + 
					"where z.feiyxm_item_id = i.id\n" + 
					"		and z.qiszyfw >= to_date('"+getNianfValue().getValue()+"-"+iy+"-01','yyyy-MM-dd') " +
				    "  		and z.qiszyfw <= to_date('"+DateUtil.Formatdate("yyyy-MM-dd",DateUtil.getLastDayOfMonth(dt))+"','yyyy-MM-dd')\n" +
					"       and z.diancxxb_id = " +getTreeid_dc()+"\n" + 
					changb +
					"union\n" +
					"select 0 as id,'�ϼ�' as ���� from dual)\n" +
					"order by id desc\n";

		String YF_sql = 
					"select * from ( --�·�\n" +
					"select decode(grouping("+y_bt+"),1,'�ϼ�',"+y_bt+") as ʱ��,\n" + 
					"       decode(grouping(i.mingc),1,'�ϼ�',i.mingc) as ����,\n" + 
					"       --z.feiyhj as ����\n" + 
					"       sum(z.feiyhj) as ����\n" + 
					"from changnzfb z,item i\n" + 
					"where z.feiyxm_item_id = i.id\n" + 
					"		and z.qiszyfw >= to_date('"+getNianfValue().getValue()+"-"+iy+"-01','yyyy-MM-dd') " +
				    "  		and z.qiszyfw <= to_date('"+DateUtil.Formatdate("yyyy-MM-dd",DateUtil.getLastDayOfMonth(dt))+"','yyyy-MM-dd')\n" +
				    changb + 
					"       and z.diancxxb_id = " +getTreeid_dc()+ "\n" + 
					"group by rollup ("+y_bt+",i.mingc)\n" + 
					"having not (grouping("+y_bt+")+grouping(i.mingc)) =2\n" + 
					"\n" + 
					"union\n" + 
					"--�ϼ�\n" + 
					"select decode(grouping(z.qiszyfw),1,'�ϼ�',z.qiszyfw) as ʱ��,\n" + 
					"       decode(grouping(i.mingc),1,'�ϼ�',i.mingc) as ����,\n" + 
					"       sum(z.feiyhj) as ����\n" + 
					"from changnzfb z,item i\n" + 
					"where z.feiyxm_item_id = i.id\n" + 
					"		and z.qiszyfw >= to_date('"+getNianfValue().getValue()+"-"+iy+"-01','yyyy-MM-dd') " +
				    "  		and z.qiszyfw <= to_date('"+DateUtil.Formatdate("yyyy-MM-dd",DateUtil.getLastDayOfMonth(dt))+"','yyyy-MM-dd')\n" +
				    changb + 
					"       and z.diancxxb_id = " +getTreeid_dc()+ "\n" + 
					"group by rollup (i.mingc,z.qiszyfw)\n" + 
					"having not (grouping(z.qiszyfw)||grouping(i.mingc))=0\n" + 
					"\n" + 
					"union\n" + 
					"--ͬ��\n" + 
					"select decode(grouping(z.qiszyfw),1,'ͬ��',z.qiszyfw) as ʱ��,\n" + 
					"       decode(grouping(i.mingc),1,'�ϼ�',i.mingc) as ����,\n" + 
					"       sum(z.feiyhj) as ����\n" + 
					"from changnzfb z,item i\n" + 
					"where z.feiyxm_item_id = i.id\n" + 
					"		and z.qiszyfw >= (to_date('"+getNianfValue().getValue()+"-"+iy+"-01','yyyy-MM-dd')-365) " +
				    "  		and z.qiszyfw <= (to_date('"+DateUtil.Formatdate("yyyy-MM-dd",DateUtil.getLastDayOfMonth(dt))+"','yyyy-MM-dd')-365)\n" +
				    changb + 
					"       and z.diancxxb_id = " +getTreeid_dc()+ "\n" + 
					"group by rollup (i.mingc,z.qiszyfw)\n" + 
					"having not (grouping(z.qiszyfw)||grouping(i.mingc))=0\n" + 
					"\n" + 
					"union\n" + 
					"--ͬ��\n" + 
					"select 'ͬ��' as ʱ��,\n" + 
					"       h.���� as ����,\n" + 
					"       h.����-nvl(t.����,0) as ����\n" + 
					"from\n" + 
					"(select decode(grouping(z.qiszyfw),1,'�ϼ�',z.qiszyfw) as ʱ��,\n" + 
					"       decode(grouping(i.mingc),1,'�ϼ�',i.mingc) as ����,\n" + 
					"       sum(z.feiyhj) as ����\n" + 
					"from changnzfb z,item i\n" + 
					"where z.feiyxm_item_id = i.id\n" + 
					"		and z.qiszyfw >= to_date('"+getNianfValue().getValue()+"-"+iy+"-01','yyyy-MM-dd') " +
				    "  		and z.qiszyfw <= to_date('"+DateUtil.Formatdate("yyyy-MM-dd",DateUtil.getLastDayOfMonth(dt))+"','yyyy-MM-dd')\n" +
				    changb + 
					"       and z.diancxxb_id = " +getTreeid_dc()+ "\n" + 
					"group by rollup (i.mingc,z.qiszyfw)\n" + 
					"having not (grouping(z.qiszyfw)||grouping(i.mingc))=0) h,\n" + 
					"(select decode(grouping(z.qiszyfw),1,'ͬ��',z.qiszyfw) as ʱ��,\n" + 
					"       decode(grouping(i.mingc),1,'�ϼ�',i.mingc) as ����,\n" + 
					"       sum(z.feiyhj) as ����\n" + 
					"from changnzfb z,item i\n" + 
					"where z.feiyxm_item_id = i.id\n" + 
					"		and z.qiszyfw >= to_date('"+getNianfValue().getValue()+"-"+iy+"-01','yyyy-MM-dd') " +
				    "  		and z.qiszyfw <= to_date('"+DateUtil.Formatdate("yyyy-MM-dd",DateUtil.getLastDayOfMonth(dt))+"','yyyy-MM-dd')\n" +
				    changb + 
					"       and z.diancxxb_id = " +getTreeid_dc()+ "\n" + 
					"group by rollup (i.mingc,z.qiszyfw)\n" + 
					"having not (grouping(z.qiszyfw)||grouping(i.mingc))=0) t\n" + 
					"where h.����=t.����(+) )\n";

		
//		�õ������ݣ���������
		StringBuffer strCol= new StringBuffer();
		strCol.append(SJ_sql);
		
//		�õ������ݣ�ʱ��
		StringBuffer strRow = new StringBuffer();
		strRow.append(MC_sql);
		
//		 �õ�ȫ������ ���̱�
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(YF_sql);
		
		cd.setRowNames("����");
		cd.setColNames("ʱ��");
		cd.setDataNames("����");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 65;
		}
		ArrWidth[0] = 120;
		ArrWidth[1] = 65;
		rt.setBody(cd.DataTable);
		rt.body.setRowHeight(30);
//		rt.body.setCellValue(0, 0, strValue)
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;
		rt.setTitle(sj+"�ӷ���ϸ��", ArrWidth);
		rt.title.setRowHeight(2, 50);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.body.setCellValue(1, 0, "ú̿Ʒ��");
//		rt.body.setCellValue(1, 1, "ú̿Ʒ��");
//		rt.setDefaultTitle(1, 2, "��λ��Ԫ/��",Table.ALIGN_LEFT);
						
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		

			
			_CurrentPage=1;
			_AllPages=1;
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

//		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("��"));
	
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("��"));
		
		tb1.addText(new ToolbarText(" �� "));
		ComboBox bnianf = new ComboBox();
		bnianf.setTransform("bNianfDropDown");
		bnianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(bnianf);
		tb1.addText(new ToolbarText("��"));
		
		ComboBox byuef = new ComboBox();
		byuef.setTransform("BYUEF");
		byuef.setWidth(60);
//		byuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(byuef);
		tb1.addText(new ToolbarText("��"));

		tb1.addText(new ToolbarText("-"));
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�������:"));
		ComboBox fh = new ComboBox();
		fh.setTransform("ChangbSelect");
		fh.setWidth(130);
		fh.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		tb1.addField(fh);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
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

//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END----------
	
//�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid_dc();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}
	
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public IPropertySelectionModel setChangbModels() {
		String sql = 
				"select distinct i.id,i.mingc\n" +
				"from changnzfb z,item i\n" + 
				"where z.feiylb_item_id = i.id";

		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

}