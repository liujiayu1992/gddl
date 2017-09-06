package com.zhiren.shihs.huaygl;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shihhyd extends BasePage {

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

	public String getPrintTable() {
		return getHuaybgd();
	}

	private String getHuaybgd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();	
		
		String sql =
			"SELECT ZL.ID,\n" +
			"       ZL.BIANM,\n" + 
			"       ZL.HUAYSJ,\n" + 
			"       ZL.HUAYY,\n" + 
			"       ZL.CAO,\n" + 
			"       ZL.CACO3,\n" + 
			"       ZL.XID,\n" + 
			"       ZL.BEIZ,\n" + 
			"       ZL.HEGBZ,\n" +
			"       GETSHIHHYCPH(CY.ID) AS CHEPH\n" + 
			"  FROM SHIHCYB CY, SHIHZLB ZL\n" + 
			" WHERE CY.SHIHZLB_ID = ZL.ID\n" +
			" AND ZL.ID = " + getBianmValue().getId();

		ResultSet rs = con.getResultSet(sql);
		String[][] ArrHeader = new String[12][7];
		try {
			if (rs.next()) {
				String huaysj = DateUtil.FormatDate(rs.getDate("huaysj"));
				String cheph = "&nbsp;" + rs.getString("cheph");
				String pinz = "ʯ��ʯ��";
				
				ArrHeader[0] = new String[] { "��Ʒ����",pinz,pinz,pinz,pinz,pinz,pinz};

				ArrHeader[1] = new String[] { "���ƺ�", cheph, cheph,cheph,cheph,cheph,cheph};
				
				ArrHeader[2] = new String[] { "ȡ������","","","ȡ����","","",""};
				
				ArrHeader[3] = new String[] { "��������",huaysj, huaysj,huaysj,huaysj,huaysj,huaysj};

				ArrHeader[4] = new String[] { "���", "","","","","",""};
				
				ArrHeader[5] = new String[] { "����ָ��","��׼","��׼","ʵ��","ʵ��","&nbsp;&nbsp;����:","&nbsp;&nbsp;����:"};
				
				ArrHeader[6] = new String[] { "����ָ��","CaO% > 51.0%","CaO% > 51.0%",rs.getString("CAO")+"%",rs.getString("CAO")+"%",rs.getString("hegbz"),rs.getString("hegbz")};
				
				ArrHeader[7] = new String[] { "����ָ��","CaCO3% > 90%","CaCO3% > 90%",rs.getString("CACO3")+"%",rs.getString("CACO3")+"%",rs.getString("hegbz"),rs.getString("hegbz")};
				
				ArrHeader[8] = new String[] { "����ָ��","ϸ��% > 93%","ϸ��% > 93%",rs.getString("XID")+"%",rs.getString("XID")+"%",rs.getString("hegbz"),rs.getString("hegbz")};
				
				ArrHeader[9] = new String[] { "&nbsp;&nbsp;��ע:","&nbsp;&nbsp;��ע:","&nbsp;&nbsp;��ע:","&nbsp;&nbsp;��ע:",
							"&nbsp;&nbsp;��ע:","&nbsp;&nbsp;��ע:","&nbsp;&nbsp;��ע:"};
				
				ArrHeader[10] = new String[] { "","","","","","",""};
				
				ArrHeader[11] = new String[] { "�� ��","","�� ��","","",
						"&nbsp;&nbsp;�� ׼",""};
				
			} else
				return "";
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 120, 110,110, 100, 20, 100, 120};

		rt.setTitle("��ʤ�ȵ绯���Ҽ��鱨��", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setBody(new Table(12, 7));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		
		rt.body.setCellAlign(6, 6, Table.ALIGN_LEFT);
		rt.body.setCellAlign(6, 7, Table.ALIGN_LEFT);
		rt.body.setCellAlign(12, 6, Table.ALIGN_LEFT);
		rt.body.setCellBorderbottom(6, 7, 0);
		rt.body.setCellBorderbottom(6, 6, 0);
		rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setRowCells(10, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCellBorderRight(12, 5, 0);
		rt.body.merge(1, 2, 1, 7);
		rt.body.merge(2, 2, 2, 7);
		rt.body.merge(3, 2, 3, 3);
		rt.body.merge(3, 5, 3, 7);
		rt.body.merge(4, 2, 4, 7);
		rt.body.merge(5, 2, 5, 7);
		rt.body.merge(6, 1, 9, 1);
		rt.body.merge(6, 2, 6, 3);
		rt.body.merge(6, 4, 6, 5);
		rt.body.merge(6, 6, 6, 7);
		rt.body.merge(7, 2, 7, 3);
		rt.body.merge(7, 4, 7, 5);
		rt.body.merge(7, 6, 7, 7);
		rt.body.merge(8, 2, 8, 3);
		rt.body.merge(8, 4, 8, 5);
		rt.body.merge(8, 6, 8, 7);
		rt.body.merge(9, 2, 9, 3);
		rt.body.merge(9, 4, 9, 5);
		rt.body.merge(9, 6, 9, 7);
		rt.body.merge(7, 6, 9, 7);
		rt.body.merge(10, 1, 10, 7);
		rt.body.merge(11, 1, 11, 7);
//		rt.body.merge(12, 5, 12, 6);
		rt.body.setFontSize(12);
		rt.body.setRowHeight(43);
		rt.body.setRowHeight(5, 86);
		rt.body.setRowHeight(11, 6 * 43);
		rt.body.ShowZero = false;

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();	
		return rt.getAllPagesHtml();
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
			getSelectData();
		}
	}
		
	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");	
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("huayrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("�������:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setBianmValue(null);
			setBianmModel(null);
			getSelectData();
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}
	}

	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
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
		sb.append("SELECT ID,bianm FROM shihzlb  WHERE huaysj=")
			.append(DateUtil.FormatOracleDate(getRiq()));
		
		setBianmModel(new IDropDownModel(sb.toString(), "��ѡ��"));
	}
}
