package com.zhiren.jt.zdt.shengcdy.rishctj;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
 

/*
* ���ߣ���ʤ��
* ʱ�䣺2012-12-20
* ������������ѡ�����Ϊ��ݣ��·�������
*/
/*
 * ���ߣ����
 * ʱ�䣺2013-01-06
 * ����: �������⼰����
 * 		����ͬ���ںʹ�ͬ���ںϲ�Ϊ��ͬ����
 * 		���󿪺ʹ��³��ϲ�Ϊ�󿪳�
 * 		ȡ����λ������
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-06-24
 * ����:������������糧���ƣ������������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-11-28
 * ����: �����б������ơ�
 * 		������㹫ʽ���±����-�ձ����
 * 		������ϸ�и�����ϢӦΪԭֵ�ĸ�ֵ��
 */

public class Ribtzqktj_GD  extends BasePage implements PageValidateListener{
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
	private String diancxxb_id;
	public void setDiancxxb_id(String diancxxb_id){
		this.diancxxb_id = diancxxb_id;
	}
	public String getDiancxxb_id(){
		return diancxxb_id;
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
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		getSelectData();
	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		setDiancxxb_id(cycle.getRequestContext().getParameter("strDiancxxb_id"));
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			initDiancTree();
			setToolbar(null);
			isBegin=true;
		}
	getToolBars() ;
	Refurbish();
	}
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		return getSelectData();
	}

	private boolean isBegin=false;
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String beginRiq=intyear+"-"+intMonth+"-01";
		String sql=
			"SELECT DC.MINGC,\n" +
			"       TIAOZHKC + HJ,\n" + 
			"       TIAOZHKC,\n" + 
			"       KUC.KUC,\n" + 
			"       KUC.KUC-TIAOZHKC CHA,\n" + 
			"       -HJ,\n" + 
			"       -TIAOZL,\n" + 
			"       -SHUIFCTZ,\n" + 
			"       -PANYK,\n" + 
			"       -CUNS\n" + 
			"  FROM (SELECT decode(SH.DIANCXXB_ID,302,1,303,1,325,2,326,2,SH.DIANCXXB_ID)DIANCXXB_ID,\n" + 
			"               sum(SH.KUC - SH.TIAOZL - SH.SHUIFCTZ - SH.PANYK + SH.CUNS) AS KC,\n" + 
			"               sum(SH.KUC) AS TIAOZHKC,\n" + 
			"               sum(SH.CUNS - SH.TIAOZL - SH.SHUIFCTZ - SH.PANYK) AS HJ,\n" + 
			"               sum(-SH.TIAOZL) TIAOZL,\n" + 
			"               sum(-SH.SHUIFCTZ) SHUIFCTZ,\n" + 
			"               sum(-SH.PANYK) PANYK,\n" + 
			"               sum(SH.CUNS) CUNS\n" + 
			"          FROM SHOUHCRBB SH\n" + 
			"         WHERE RIQ = ADD_MONTHS(DATE '"+beginRiq+"', 1) - 1\n" + 
			"         GROUP BY decode(SH.DIANCXXB_ID,302,1,303,1,325,2,326,2,SH.DIANCXXB_ID)\n" + 
			"           ) RB,\n" + 
			"\n" + 
			"       ( SELECT decode(DIANCXXB_ID,302,1,303,1,325,2,326,2,DIANCXXB_ID) DIANCXXB_ID, sum(KUC)KUC\n" + 
			"          FROM YUESHCHJB\n" + 
			"         WHERE RIQ = DATE '"+beginRiq+"'\n" + 
			"           AND FENX = '����'\n" + 
			"            GROUP BY decode(DIANCXXB_ID,302,1,303,1,325,2,326,2,DIANCXXB_ID)) KUC,\n" + 
			"            (SELECT 1 ID, NVL('','��ͬ����')MINGC,4 XUH FROM DUAL\n" + 
			"            UNION\n" + 
			"            SELECT 2 ID, NVL('','�󿪳�')MINGC,7 XUH FROM DUAL\n" + 
			"            UNION\n" + 
			"            SELECT\n" + 
			"            decode(ID,302,1,303,1,325,2,326,2,ID)ID,\n" + 
			"            decode(ID,302,NVL('','��ͬ����'),303,NVL('','��ͬ����'),325,NVL('','�󿪳�'),326,NVL('','�󿪳�'),MINGC)MINGC,\n" + 
			"             decode(ID,302,4,303,4,325,7,326,7,XUH)XUH FROM DIANCXXB\n" + 
			"            WHERE ID NOT IN(300,100,112)) DC\n" + 
			" WHERE DC.ID= RB.DIANCXXB_ID(+)\n" + 
			" AND DC.ID=KUC.DIANCXXB_ID(+)\n" + 
			" ORDER BY DC.XUH";

		ResultSetList rs = con.getResultSetList(sql);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][10];
		ArrHeader[0]=new String[] {"��λ","��ĩ�ձ����","������","������","������","��ĩ��������ϸ","��ĩ��������ϸ","��ĩ��������ϸ","��ĩ��������ϸ","��ĩ��������ϸ"};
		ArrHeader[1]=new String[] {"��λ","��ĩ�ձ����","�ձ����","�±����","����","�ϼ�","������","ˮ�ֲ�","����","��ӯ��"};
		int ArrWidth[]=new int[] {120,90,90,90,90,60,60,60,60,60};

		rt.setBody(new Table(rs, 2, 0, 1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		 
		rt.setTitle(intyear+"��"+intMonth+"����ĩ���������",ArrWidth);
//		rt.setDefaultTitle(1, 2, "���λ:"+getDiancmc(), Table.ALIGN_LEFT);
//		rt.setDefaultTitle(3, 3, intyear+"��"+intMonth+"��", Table.ALIGN_CENTER);
		rt.setDefaultTitle(7, 3, "��λ:�֡�Ԫ/�֡�MJ/Kg", Table.ALIGN_RIGHT);
	   
		rt.body.ShowZero=true;
	
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		
		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 2, "���ܣ�", Table.ALIGN_CENTER);
		rt.setDefautlFooter(4, 3, "��ˣ�", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 2, "�Ʊ�", Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
		
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
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);

	}
	
//	���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * �·�
	 */
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
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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
	 //	ҳ���ж�����
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

//	�õ��糧�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
    public int getDiancmcJib(String diancId){
    	JDBCcon con = new JDBCcon();
    	int jib = -1;
    	ResultSetList rsl = new ResultSetList();
    	StringBuffer sqlJib = new StringBuffer();
    	sqlJib.append("select d.jib from diancxxb d where d.id = '"+diancId+"'");
    	rsl = con.getResultSetList(sqlJib.toString());
    	rsl.next();
    	jib = rsl.getInt("jib");
    	con.Close();
    	return jib;
    }
    
    public String getDiancmcById(String diancid){
    	JDBCcon con = new JDBCcon();
    	String quanc = "";
    	ResultSetList rsl = new ResultSetList();
    	StringBuffer sqlJib = new StringBuffer();
    	sqlJib.append("select d.quanc from diancxxb d where d.id = '"+diancid+"'");
    	rsl = con.getResultSetList(sqlJib.toString());
    	rsl.next();
    	quanc = rsl.getString("quanc");
    	con.Close();
    	return quanc;
    }

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("��ϵ糧");
		}else{
			tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
//		tb1.addText(new ToolbarText("��λ:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
//		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
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
		if(null==getDiancxxb_id()||"".equals(getDiancxxb_id())){   //�´��ڲ���ʾ������
			return getToolbar().getRenderScript()+getOtherScript("diancTree");
		}else{
			return "";
		}
	}

//	���ӵ糧��ѡ���ļ���
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
//	��ʼ����ѡ�糧���е�Ĭ��ֵ
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
	
//	 �õ���½��Ա�����糧��ֹ�˾������
	public String getDiancmc(String diancId) {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;

	}
	public String getDiancmc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "��ϵ糧";
		}else{
			return getDiancmc(str[0]);
		}
	}
	
//	private String treeid;

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
}