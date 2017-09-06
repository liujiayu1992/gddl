package com.zhiren.dc.jilgl.gongl.guohcx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
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
 * ���ߣ����� ʱ�䣺2011-11-21 �������޸��ѻ�Ƥ��δ��Ƥ���ж�����
 */

public class Guohcx_Qlfd extends BasePage implements PageValidateListener {

	protected void initialize() {
		super.initialize();
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
	
	// ������
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}

	
//	 ������
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
	
	
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public boolean getRaw() {
		return true;
	}
	
	public void SetOtherScript( String otherScript) {
		((Visit) getPage().getVisit()).setString3(otherScript);
	}
	
	public String getOtherScript() {
		if(((Visit) getPage().getVisit()).getString3()==null){
			
			((Visit) getPage().getVisit()).setString3("");
		}
		return ((Visit) getPage().getVisit()).getString3();
	}
	

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	private Connection getSQLServerCon () {
		Connection cnn = null;
		String SQLServer_IP = "10.112.111.11";
		String SQLSever_DB = "nyh";
		String DBDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		String ConnStr = "jdbc:microsoft:sqlserver://" + SQLServer_IP + ":1433;DatabaseName=" + SQLSever_DB;
		String UserName = "sa";
		String UserPassword = "";
		try {
			Class.forName(DBDriver);
			cnn = DriverManager.getConnection(ConnStr,
					UserName, UserPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnn;
	}
	
	public synchronized ResultSet getResultSet(String sql) {
		Connection con = getSQLServerCon();
		ResultSet rs = null;
		try {
			con.setAutoCommit(true);
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(sql);
			if (rs == null) {
				con.rollback();
			}
			con.close();
		} catch (Exception exp) {
			System.out.println("SQL:\n");
			System.out.println(sql);
			exp.printStackTrace();
			return null;
		} 		
		return rs;
	}
	
	public String getPrintTable() {

		JDBCcon con = new JDBCcon();
		Report rt=new Report();
		String riqTiaoj = this.getRiqi();
		String gysWhere = "";
		String sql = "";
		String strPinz = "and meiz like '%ú%' \n";
		String add_sql_where = "";
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		
		if ("������úҵ�������޹�˾".equals(this.getGysValue().getValue())) {
			gysWhere = "AND fhdw like '��%'\n";
		}else if("����С��".equals(this.getGysValue().getValue())){
			gysWhere = "AND fhdw not like '��%'\n";
		}
		
		if ("�ѻ�Ƥ".equals(this.getZtValue().getValue())) {
			add_sql_where = " and meiz like '%ú%' \n" +
							" and piz<>0\n";
		}else if("δ��Ƥ".equals(this.getZtValue().getValue())){
			add_sql_where = " and meiz like '%ú%' \n" +
							" and piz=0\n";
		}else if("��������".equals(this.getZtValue().getValue())){
			add_sql_where = " and meiz not like '%ú%' \n";
		}else{
			add_sql_where = "";
		}
		
		if ("����".equals(getGsValue().getValue())) {
			
			sql=
				"SELECT decode(fhdw,null,'�ϼ�',fhdw),shdw,cydw,\n" +
				"    	count(cheh) as ches ,\n" + 
				"		sum(maoz) as maoz,sum(piz) as piz,sum(jingz) as jingz,sum(biaoz) as biaoz\n" + 
				"		FROM qlfd_qch_tmp\n" + 
				"		WHERE to_char(zhongcsj,'yyyy-mm-dd') = '" + riqTiaoj+ "'\n" + 
				gysWhere + 
				add_sql_where +
				"GROUP by grouping sets((),(fhdw,shdw,cydw)) \n" +
				"order by grouping(fhdw),fhdw,shdw,cydw";
			
			con.setAutoCommit(false);
			ResultSet rs=con.getResultSet(sql);
			con.commit();
			String ArrHeader[][]=new String[1][8];
			ArrHeader[0] = new String[] {"������λ","���","���˵�λ","����","ë��","Ƥ��",
									"����","Ʊ��"};
			int ArrWidth[]=new int[] {200,100,200,50,80,80,80,80};
			rt.setTitle("�������ݲ�ѯ-����", ArrWidth);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			
		} else {
			
			sql = 
				"SELECT decode(fhdw,NULL,'�ϼ�',fhdw) AS fhdw,shdw,cydw,\n" +
				"		decode(cheh,NULL,''||COUNT(cheh),cheh) AS cheh,\n" + 
				"		SUM(maoz) AS maoz,SUM(piz) AS piz,\n" + 
				"		SUM(jingz) AS jingz,sum(biaoz) as biaoz,\n" +
				"		to_char(zhongcsj,'yyyy-mm-dd hh24:mi:ss'),to_char(qingcsj,'yyyy-mm-dd hh24:mi:ss')\n" + 
				"FROM qlfd_qch_tmp WHERE to_char(zhongcsj,'yyyy-mm-dd') = '" + riqTiaoj + "'\n" + 
				gysWhere + 
				add_sql_where + 
				"GROUP BY GROUPING SETS((fhdw,shdw,cydw,cheh,maoz,piz,jingz,biaoz,zhongcsj,qingcsj),())\n" + 
				"ORDER BY zhongcsj";
				
			con.setAutoCommit(false);
			ResultSet rs=con.getResultSet(sql);
			con.commit();
			String ArrHeader[][]=new String[1][10];
			ArrHeader[0] = new String[] {"������λ","���","���˵�λ","����","ë��","Ƥ��",
									"����","Ʊ��","����ʱ��","����ʱ��"};
			int ArrWidth[]=new int[] {200,100,200,80,60,60,60,60,120,120};
			rt.setTitle("�������ݲ�ѯ-��ϸ", ArrWidth);
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);

		}
		
		rt.body.setColFormat(5, "0.00");
		rt.body.setColFormat(6, "0.00");
		rt.body.setColFormat(7, "0.00");
		rt.body.setColFormat(8, "0.00");
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);

//		for (int i=2;i<=6;i++) rt.body.setColFormat(i, "0.00");
//		
		rt.body.setPageRows(10000);
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.createDefautlFooter(new int[] {200,100,200,50,80,80,80,80});
//		rt.setDefautlFooter(1, 3, "��ӡ���ڣ�"
//				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
//				Table.ALIGN_RIGHT);
		rt.setDefautlFooter(1, 7, "ע������ú  ����=ë��-Ƥ��-�۶�;Ʊ��=�󷽷�������; <br>&nbsp;&nbsp;&nbsp;&nbsp;����С��  ����=ë��-Ƥ�أ�Ʊ��=����-�۶�", Table.ALIGN_LEFT);
		
//		rt.footer.setCells(1, 1, 1, 7, 5, "ע������ú  ����=ë��-Ƥ��-�۶�;Ʊ��=�󷽷�������;   ����С��  ����=ë��-Ƥ�أ�Ʊ��=����-�۶�");
//		rt.setDefautlFooter(10, 2, "�Ʊ�", Table.ALIGN_CENTER);
		return rt.getAllPagesHtml();
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
//			getSelectData();
		}
	}
	
	// ҳ���½��֤
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
	//��������������
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString2(null);
			visit.setString3(null);
			visit.setString5(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			getGysModels();
			getZtModels();
			getGsModels();
		}
		getSelectData();
	}
	
	public void getSelectData() {
		String riqTiaoj = this.getRiqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df.setId("riqI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
//
//		tb1.addText(new ToolbarText("��������:"));
//		DateField df1 = new DateField();
//		df1.setReadOnly(true);
//		df1.setValue(this.getRiq2());
//		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df1.setId("riq2");
//		tb1.addField(df1);
//		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("������λ:"));	
		ComboBox cboGys = new ComboBox();
		cboGys.setTransform("GysSelect");	
		cboGys.setWidth(200);
		tb1.addField(cboGys);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��Ƥ״̬:"));	
		ComboBox cboZt = new ComboBox();
		cboZt.setTransform("ZtSelect");	
		cboZt.setWidth(100);
		tb1.addField(cboZt);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ʽ:"));	
		ComboBox cboGs = new ComboBox();
		cboGs.setTransform("GsSelect");	
		cboGs.setWidth(100);
		tb1.addField(cboGs);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null, "ˢ��",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}
	
//	��Ӧ��������
	public IDropDownBean getGysValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getGysModel().getOptionCount()>0) {
				setGysValue((IDropDownBean)getGysModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	
	public void setGysValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getGysModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
        	getGysModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
	public void setGysModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }
	
    public void getGysModels() {
    	JDBCcon con = new JDBCcon();
    	con.setAutoCommit(false);
    	String sql = 
    		"SELECT 0 ID,'ȫ��' AS fhdw FROM dual\n" +
    		"UNION\n" + 
    		"SELECT  ID,mingc FROM gongysb where leix=0";

    	setGysModel(new IDropDownModel(con,sql)) ;
    	con.commit();
        return ;
    }
    
//  ��Ƥ״̬������
    public IDropDownBean getZtValue() {
    	if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
    		if(getZtModel().getOptionCount()>0) {
    			setZtValue((IDropDownBean)getZtModel().getOption(0));
    		}
    	}
    	return ((Visit)this.getPage().getVisit()).getDropDownBean2();
    }

    public void setZtValue(IDropDownBean value) {
    	((Visit)this.getPage().getVisit()).setDropDownBean2(value);
    }

    public IPropertySelectionModel getZtModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
        	getZtModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void setZtModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public void getZtModels() {
    	List list = new ArrayList();
    	list.add(new IDropDownBean(-1,"ȫ��"));
    	list.add(new IDropDownBean(0,"�ѻ�Ƥ"));
    	list.add(new IDropDownBean(1,"δ��Ƥ"));
    	list.add(new IDropDownBean(2,"��������"));
    	setZtModel(new IDropDownModel(list)) ;
        return ;
    }
    
//  ��ʽ״̬������
    public IDropDownBean getGsValue() {
    	if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
    		if(getGsModel().getOptionCount()>0) {
    			setGsValue((IDropDownBean)getGsModel().getOption(0));
    		}
    	}
    	return ((Visit)this.getPage().getVisit()).getDropDownBean3();
    }

    public void setGsValue(IDropDownBean value) {
    	((Visit)this.getPage().getVisit()).setDropDownBean3(value);
    }

    public IPropertySelectionModel getGsModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
        	getGsModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }
    public void setGsModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public void getGsModels() {
    	List list = new ArrayList();
    	list.add(new IDropDownBean(0,"����"));
    	list.add(new IDropDownBean(1,"��ϸ"));
    	setGsModel(new IDropDownModel(list)) ;
        return ;
    }
}

