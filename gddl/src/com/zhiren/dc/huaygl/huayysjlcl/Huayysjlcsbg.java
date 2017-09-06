package com.zhiren.dc.huaygl.huayysjlcl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Huayysjlcsbg extends BasePage implements PageValidateListener {
	
	private final static String round_count="###.00";//�Զ�����ʱ  ������С���� β��
	
	public boolean getRaw() {
		return true;
	}
	
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getPrintTable() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		
		//��
		Report rt = new Report();
		int ArrWidth[] = new int[] { 90, 90, 90, 90, 90, 90, 90};
		String[][] ArrHead = new String[1][];
		ArrHead[0] = new String[] {"�Զ����","������","ʽ������mg","������g","�ոɻ�%","��������","����Ա"};
		String sql = 
			"SELECT zidbm,bianm,meiyzl*1000 as meiyzl,qimzl,liuf,to_char(riq,'yyyy-mm-dd'),huayy \n" +
			"FROM huaylfb\n" +
			"WHERE riq>=to_date('" + this.getRiq() + "','yyyy-mm-dd')-2\n" + 
			"AND riq<=to_date('" + this.getRiq() + "','yyyy-mm-dd')+2\n" + 
			"AND bianm='" + this.getBianmValue().getValue() + "'\n" + 
			"AND shenhzt in (1,2)" +
			"AND diancxxb_id= " + visit.getDiancxxb_id();
		
		ResultSetList rsl = con.getResultSetList(sql);
		
		rt.setTitle("���������Ա���", ArrWidth);
		rt.setBody(new Table(rsl,1,0,1));
		rt.body.setHeaderData(ArrHead);
		rt.body.setWidth(ArrWidth);
		for (int i=1;i<=rt.body.getCols();i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.setColFormat(3, "0.0");
		rt.body.setColFormat(4, "0.0000");
		rt.body.setColFormat(5, "0.00");
		// ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 6, "��λ:" + visit.getDiancqc(), Table.ALIGN_LEFT);

		
		//����
		Report rt2 = new Report();
		sql = 
			"SELECT zidbm,bianm,meiyzl,rerl,farl,to_char(riq,'yyyy-mm-dd'),huayy  \n" +
			"FROM huayrlb\n" +
			"WHERE riq>=to_date('" + this.getRiq() + "','yyyy-mm-dd')-2\n" + 
			"AND riq<=to_date('" + this.getRiq() + "','yyyy-mm-dd')+2\n" + 
			"AND bianm='" + this.getBianmValue().getValue() + "'\n" + 
			"AND shenhzt in (1,2)" +
			"AND diancxxb_id= " + visit.getDiancxxb_id();
		
		ArrHead[0] = new String[] {"�Զ����","������","ʽ������g","������J/g","��Ͳ��ֵJ/g","��������","����Ա"};
		
		rsl = con.getResultSetList(sql);
		rt2.setTitle("���������Ա���", ArrWidth);
		rt2.setBody(new Table(rsl,1,0,1));
		rt2.body.setHeaderData(ArrHead);
		rt2.body.setWidth(ArrWidth);
		for (int i=1;i<=rt2.body.getCols();i++) {
			rt2.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		
		rt2.body.setColFormat(3, "0.0000");
		rt2.body.setColFormat(4, "0");
		rt2.body.setColFormat(5, "0");
		// ҳ��
		rt2.createDefautlFooter(ArrWidth);
		rt2.setDefautlFooter(1, 6, "��λ:" + visit.getDiancqc(), Table.ALIGN_LEFT);
		
		return rt.getAllPagesHtml() + rt2.getAllPagesHtml();
	}
	
	
	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		if (this.riq==null){
			riq = DateUtil.FormatDate(new Date());
		}
		
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}
	
	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}
	
	private void setBianmModels() {
		StringBuffer sb = new StringBuffer();
		Visit visit=(Visit)this.getPage().getVisit();
		
			
		sb.append(
				"SELECT rownum as id,g.bianm from\n" +
				"( select DISTINCT h.bianm from huaylfb h\n" + 
				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" +
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"and h.shenhzt in (1,2)\n" + 
				") g ,\n" + 
				"(\n" + 
				"select DISTINCT h.bianm from huayrlb h\n" + 
				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" +
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"and h.shenhzt in (1,2)\n" + 
				") r\n" + 
				"WHERE r.bianm=g.bianm\n" + 
				"order by g.bianm"
		);
		
		setBianmModel(new IDropDownModel(sb.toString(), "��ѡ��"));
	}
	
	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {

		((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		
		return getToolbar().getRenderScript();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		JDBCcon con=new JDBCcon();
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("huayrq");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("-"));
		
		if(visit.getString12().indexOf("RC")!=-1){  //�볧����  ���
			tb1.addText(new ToolbarText("�������:"));
			ComboBox shij = new ComboBox();
			shij.setTransform("BianmSelect");
			shij.setWidth(130);
			shij.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index;Form0.submit();}");
			tb1.addField(shij);
		}else{//��¯���� ���
			
		}
		
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		rbtn.setId("search_id");
	
		con.Close();
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
			getSelectData();
		}
	}
	
	public void pageValidate(PageEvent arg0) {

		
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setString12("RC");//�����ת������ҳ������  Ĭ����  �볡 ���� 
			
			String pageName=this.getPageName().toString();
			if(pageName!=null && pageName.toUpperCase().equals("RL")){
				visit.setString12(pageName);
			}
			
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setBianmValue(null);
			setBianmModel(null);
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}
		getSelectData();
	}
}
