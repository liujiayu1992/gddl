package com.zhiren.dc.diaoygl.fahhy;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�����
 * ʱ�䣺2011-12-13
 * �������������ò�ѯ����
 */

public class FahhyReport  extends BasePage implements PageValidateListener{

	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
	private boolean blnIsBegin = false;
	
	
	private String mstrReportName="";
	
//	 �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
	}
	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";	
			
		}
		blnIsBegin = false;
		
		
		return getQipb();
		
	}

//	public String getLeix(){
//		Visit visit = (Visit) getPage().getVisit();
//		if(!visit.getString9().equals("")) {
//			return visit.getString9();
//        } 
//		return ""; 
//	}
	
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
//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();
			visit.setDropDownBean5(null);		//���
			visit.setProSelectionModel5(null); //���
			this.setTreeid(null);
		}
		isBegin=true;
		getToolBars();

		blnIsBegin = true;
		
        }
		
	

	private boolean isBegin=false;

	private String getQipb() {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		//ArrWidth = new int[] { 150, 40, 80, 80, 80, 80, 80, 80 };

		//�õ������ݣ��糧
		StringBuffer strRow = new StringBuffer();
		StringBuffer strCol = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		
		String sqrCol ="";
		String sbsql = "";
		String sqlRow= "";
		
		String danwmc="";
		String where="";
		
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intmonth;
		if (getYuefValue() == null) {
			intmonth = DateUtil.getMonth(new Date());
		} else {
			intmonth = getYuefValue().getId();
		}
		
		int jib=getDiancTreeJib();
		if(jib==1){//����
			danwmc="vdc.fgsmc";
			where="";
		}else if(jib==2){//�ֹ�˾��ȼ�Ϲ�˾
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();

			try{
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if(rl.getRows()!=0){//ȼ�Ϲ�˾
					danwmc="vdc.mingc";
					where=" and (dc.fuid=  " +this.getTreeid()+") ";
				}else{//�ֹ�˾
					danwmc="vdc.mingc";
					where=" and (dc.fuid=  " +this.getTreeid()+") ";
				}
				rl.close();

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
		}else if(jib==3){
			danwmc="vdc.mingc";
			where=" and dc.id=  " +this.getTreeid()+" ";
		}
		
//		�õ������ݣ����ڡ�����
		sqlRow ="SELECT riq,mingc FROM (SELECT to_char(rq.riq,'yyyy-mm-dd') riq ,r.mingc,r.xuh\n" +
				"FROM rulbzb r,(SELECT DISTINCT riq FROM fahhybzb h \n" +
				"WHERE h.riq>=DATE'"+intyear+"-"+intmonth+"-01'\n" + 
				"AND h.riq<=add_months(DATE'"+intyear+"-"+intmonth+"-01',1)-1) rq  WHERE r.mingc<>'ȫ��')\n" + 
				"ORDER BY riq ,xuh";
		
//		�õ������ݣ�ú��
		sqrCol ="SELECT mingc FROM (\n" +
				"SELECT DISTINCT m.mingc,m.xuh FROM meikxxb m,fahb f ,fahhybzb h\n"+
				"WHERE h.riq>=DATE'"+intyear+"-"+intmonth+"-01'\n" + 
				"AND h.riq<=add_months(DATE'"+intyear+"-"+intmonth+"-01',1)-1\n" + 
				"AND f.meikxxb_id = m.id AND h.fahb_id = f.id\n" +
				"UNION\n" + 
				"SELECT '����С��' AS mingc,0 xuh FROM dual)\n" + 
				"ORDER BY xuh";
		
//		 �õ�ȫ������ ���̱�
		
		sbsql = 
			"SELECT decode(grouping(bz.riq),1,'�ϼ�',to_char(bz.riq,'yyyy-mm-dd')) riq,\n" + 
			"decode(grouping(bz.mingc),1,'����С��',bz.mingc) bzmc,\n" + 
			"decode(grouping(m.mingc),1,'����С��',m.mingc) mkmc ,\n" + 
			"SUM(nvl(haoyl,0)) haoyl,getDaohrq(to_char(bz.riq,'yyyy-mm-dd'),bz.mingc,m.mingc) dhrq\n" + 
			"FROM fahhybzb fh,fahb f,meikxxb m,\n" + 
			"(SELECT r.xuh,rq.riq ,r.mingc\n" + 
			"FROM rulbzb r,(Select ROWNUM AS xuh, DATE'"+intyear+"-"+intmonth+"-01'+Rownum-1 riq from dual\n" + 
			"connect by rownum<= add_months(DATE'"+intyear+"-"+intmonth+"-01',1)-1-DATE'"+intyear+"-"+intmonth+"-01' +1) rq  WHERE r.mingc<>'ȫ��'\n" + 
			" ORDER BY riq,xuh)bz\n" + 
			"WHERE fh.riq(+) = bz.riq AND fh.banzm(+) = bz.mingc AND f.id = fh.fahb_id AND f.meikxxb_id = m.id\n" + 
			"GROUP BY ROLLUP (bz.riq,bz.xuh,bz.mingc,m.mingc)\n" + 
			"HAVING NOT grouping(m.mingc)+grouping(bz.riq)+grouping(bz.mingc)>1 \n" + 
			"ORDER BY grouping(bz.riq),bz.riq,GROUPING(bz.xuh),bz.xuh,\n" + 
			"GROUPING(bz.mingc),GROUPING(m.mingc),m.mingc";

		sb.append(sbsql);
		
		ResultSetList rslcol = cn.getResultSetList(sqrCol);
		ResultSetList rslrow = cn.getResultSetList(sqlRow);
		ResultSetList rl = cn.getResultSetList(sbsql);
		int intRows = rslrow.getRows();
		int intCols = rslcol.getRows();
		String strTable[][] = new String[intRows+1][intCols+2];
		
		for(int i=0;i<strTable.length;i++){
			for(int j=0;j<strTable[i].length;j++){
				strTable[i][j] = "";
			}
		}
		
		for(int i=1;rslrow.next();i++){
			strTable[i][0] = rslrow.getString("riq");
			strTable[i][1] = rslrow.getString("mingc");
		}
		for(int i=2;rslcol.next();i++){
			strTable[0][i] = rslcol.getString("mingc");
		} 
		while(rl.next()){
			for(int i=1;i<intRows+1;i++){
				if(strTable[i][0].equals(rl.getString("riq"))){
					if(strTable[i][1].equals(rl.getString("bzmc"))){
						for(int j=2;j<=intCols+1;j++){
							if(strTable[0][j].equals(rl.getString("mkmc"))){
								strTable[i][j] = rl.getString("haoyl")+"<br>"+rl.getString("dhrq");
							}
						}
					}
				}
			}
		}
		int ArrWidth1[] = new int[]{};
		if(intCols<5){
			 ArrWidth1=new int[5];
		}else{
			 ArrWidth1=new int[intCols];
		}
		for(int i=0;i<ArrWidth1.length;i++){
			ArrWidth1[i] = 50;
			
		}
		/*try{
			ResultSetList rl = cn.getResultSetList(sbsql);
			
			if(!rl.next()){
				return "�����ݣ�";
			}
			
			rl.close();

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}*/
		/*
		strRow.append(sqlRow);
		strCol.append(sqrCol);
		
		
		
	    cd.setRowNames("�糧,����");
	    cd.setColNames("��Ӧ��");
	    cd.setDataNames("zhi");
	    cd.setDataOnRow(false);
	    cd.setRowToCol(false);
//	    System.out.println(sbsql.toString());
	    //�������̱�����
	    cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		
		
		ArrWidth = new int[cd.DataTable.getCols()];
		int cols = ArrWidth.length;
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 35;
		}
		ArrWidth[0] = 30;
		ArrWidth[1] = 30;
		*/
		
		/*rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();*/
		
		rt.setTitle("��ú���ͳ�Ʊ�",ArrWidth1);
		
		rt.setBody(new Table(strTable,0,0,1));
		
		rt.body.mergeCell(1,1,1,2);
		rt.body.mergeFixedCol(1);
		rt.body.ShowZero = true;
//		rt.setTitle(this.getDiancmc()+intyear+"ú̿�ص��ͬ�������", ArrWidth);
//		rt.title.setRowHeight(2, 40);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);


//		cd.DataTable.setCellValue(1, 1, "��ҵ����");

//		rt.setDefaultTitle(1, 8, "�Ʊ�λ:"+this.getDiancmc(),Table.ALIGN_LEFT);
//		rt.setDefaultTitle(cols-1, 2, "��λ:���",Table.ALIGN_LEFT);

		
		// ����ҳ��
		int rows= rt.body.getRows();
//		for (int i=2;i<rows;i++){
//			String mingc=rt.body.getCellValue(i, 1);
//			String mingc2=mingc.substring(mingc.length()-2, mingc.length());
//			if (mingc2.equals("С��")){
//				for (int j=1;j<cols+1;j++){
////					rt.body.getCell(i, j).setBg_Color("#B6C6D8");
//					rt.body.getCell(i, j).backColor=rt.body.getCell(i, j).getBg_Color();
//				}
//			}else{
//				continue;
//			}
//		} 
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}
//	******************************************************************************
	//���
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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
//	 �·�������
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }
	
//	��������
	public IDropDownBean getBaoblxDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getBaoblxDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setBaoblxDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setBaoblxDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getBaoblxDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getBaoblxDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getBaoblxDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "�糧"));
		list.add(new IDropDownBean(1, "�糧���"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
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
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("NianfDropDown");
		cb1.setWidth(60);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
				
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.getElementById('RefurbishButton').click();}");
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

//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	private String treeid;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

//	�糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}

}