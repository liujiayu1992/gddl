package com.zhiren.dc.huaygl.huaybb.huaybgd;

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
/*
 * ����:tzf
 * ʱ��:2009-07-02
 * �޸�����:����  �зֳ�ʱ ��Ҫ��ʾ �糧������  ҳ����س������ext�齨 ��Ӧͬһ�� html�ڵ㣬
 * ��  һ������ʱ ��sql��ѯʱ   ֻ�Ǹ��ݵ�¼��Ա�ĵ糧id���в�������û�и���������ѡ���
 * �糧���в�ѯ�����ѵ���.
 */
public class Rulhyd_hb extends BasePage {

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
		;
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

	// ȼ�ϲɹ���ָ���������ձ�
	private String getHuaybgd() {
		Visit visit = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String diancxxb_id=visit.getDiancxxb_id()+"";
		
		if(visit.isFencb()){
			
			diancxxb_id=this.getDCValue().getStrId();
		}
		StringBuffer sqlRulhyd = new StringBuffer();
		sqlRulhyd
				.append("select TO_CHAR(r.lursj, 'YYYY-MM-DD') AS lursj,\n"
						+ "       TO_CHAR(r.rulrq, 'YYYY-MM-DD') AS rulrq,\n"
						+ "       round_new(r.mt, 1) as MT,\n"
						+ "       round_new(r.mad, 2) as MAD,\n"
						+ "       round_new(r.aad, 2) as AAD,\n"
						+ "       round_new(r.aar, 2) as AAR,\n"
						+ "       round_new(r.ad, 2) as AD,\n"
						+ "       round_new(r.vad, 2) as VAD,\n"
						+ "       round_new(r.vdaf, 2) as VDAF,\n"
						+ "       round_new(r.stad, 2) as STAD,\n"
						+ "       round_new(r.std, 2) as STD,\n"
						+ "       ROUND_NEW((100 - r.MT) * r.STAD / (100 - r.MAD), 2) AS STAR,\n"
						+ "       round_new(r.had, 2) as HAD,\n"
						+ "       round_new(r.har, 2) as HAR,\n"
						+ "       round_new(r.qbad, 2) * 1000 as QBAD,\n"
						+ "       round_new(r.qgrd, 2) * 1000 as QGRD,\n"
						+ "       round_new(r.qgrad, 2) * 1000 as QGRAD,\n"
						+ "       round_new(r.qgrad_daf, 2) * 1000 as GANZWHJGWRZ,\n"
						+ "       round_new(r.qnet_ar, 2) * 1000 as QNETAR,\n"
						+ "       round_new(round_new(r.qnet_ar, 2) * 7000 / 29.271, 0) as FRL,\n"
						+ "       r.lury,\n"
						+ "       '' as shenhry,\n"
						+ "       0.00 as meihy,\n"
						+ "       to_char(r.fenxrq,'yyyy-mm-dd') as fenxrq,\n" + "       rb.mingc banzmc,\n"
						+ "       j.mingc jizmc\n"
						+ "  from rulmzlzb r,(select item.id,item.mingc from item,itemsort where item.itemsortid=itemsort.id and itemsort.bianm='RUHYBZ') rb,jizfzb j\n"
						+ " where r.rulrq = "
						+ DateUtil.FormatOracleDate(getRiq()) + "\n"
//							+ "   and r.diancxxb_id=" + visit.getDiancxxb_id()
						+ "   and r.diancxxb_id=" + diancxxb_id
						+
						"   and r.rulbzb_id=rb.id\n"
						+ "   and r.jizfzb_id=j.id" + "   and r.rulbzb_id="
						+ getBianmValue().getId() + "   and r.jizfzb_id="
						+ getChangbValue().getId());
		
		ResultSet rs = con.getResultSet(sqlRulhyd);
		String shangjshry = "";
		String lury = "";
		String[][] ArrHeader = new String[20][6];
		try {
			if (rs.next()) {
				lury = rs.getString("lury");
//					StringBuffer buffer = new StringBuffer();
				// String cheph = rs.getString("CHEPH");
				// String[] list = cheph.split(",");
				// for (int i = 1; i <= list.length; i++) {
				// if (i % 11 == 0) {
				// buffer.append(list[i - 1] + ",<br>");
				// } else {
				// buffer.append(list[i - 1] + ",");
				// }
				// }
				// cheph = buffer.toString().substring(0, buffer.length() - 1);
				String num = rs.getString("FRL");
				ArrHeader[0] = new String[] { "��¯����",
						"" + rs.getString("rulrq") + "", "¼��ʱ��",
						"" + rs.getString("lursj") + "", "��������",
						"" + rs.getString("fenxrq") + "" };

				ArrHeader[1] = new String[] { "ú����",
						"" + rs.getString("meihy") + "", "������Ϣ",
						"" + rs.getString("banzmc") + "", "������Ϣ",
						"" + rs.getString("jizmc") + "" };
				ArrHeader[2] = new String[] { "¼����Ա",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "",
						"" + rs.getString("lury") + "" };
				ArrHeader[3] = new String[] { "ȫˮ��Mt(%)", "ȫˮ��Mt(%)",
						"ȫˮ��Mt(%)", "" + rs.getDouble("MT") + "", "��ע", "��ע" };
				ArrHeader[4] = new String[] { "���������ˮ��Mad(%)", "���������ˮ��Mad(%)",
						"���������ˮ��Mad(%)", "" + rs.getDouble("MAD") + "", "", "" };
				ArrHeader[5] = new String[] { "����������ҷ�Aad(%)", "����������ҷ�Aad(%)",
						"����������ҷ�Aad(%)", "" + rs.getDouble("AAD") + "", "", "" };
				ArrHeader[6] = new String[] { "�յ����ҷ�Aar(%)", "�յ����ҷ�Aar(%)",
						"�յ����ҷ�Aar(%)", "" + rs.getDouble("AAR") + "", "", "" };
				ArrHeader[7] = new String[] { "������ҷ�Ad(%)", "������ҷ�Ad(%)",
						"������ҷ�Ad(%)", "" + rs.getDouble("AD") + "", "0", "0" };
				ArrHeader[8] = new String[] { "����������ӷ���Vad(%)",
						"����������ӷ���Vad(%)", "����������ӷ���Vad(%)",
						"" + rs.getDouble("VAD") + "", "", "" };
				ArrHeader[9] = new String[] { "�����޻һ��ӷ���Vdaf(%)",
						"�����޻һ��ӷ���Vdaf(%)", "�����޻һ��ӷ���Vdaf(%)",
						"" + rs.getDouble("VDAF") + "", "", "" };
				ArrHeader[10] = new String[] { "���������ȫ��St,ad(%)",
						"���������ȫ��St,ad(%)", "���������ȫ��St,ad(%)",
						"" + rs.getDouble("STAD") + "", "", "" };
				ArrHeader[11] = new String[] { "�����ȫ��St,d(%)", "�����ȫ��St,d(%)",
						"�����ȫ��St,d(%)", "" + rs.getDouble("STD") + "", "", "" };
				ArrHeader[12] = new String[] { "�յ���ȫ��St,ar(%)",
						"�յ���ȫ��St,ar(%)", "�յ���ȫ��St,ar(%)",
						"" + rs.getDouble("STAR") + "", "", "" };
				ArrHeader[13] = new String[] { "�����������Had(%)", "�����������Had(%)",
						"�����������Had(%)", "" + rs.getDouble("HAD") + "", "", "" };
				ArrHeader[14] = new String[] { "�յ�����Har(%)", "�յ�����Har(%)",
						"�յ�����Har(%)", "" + rs.getDouble("HAR") + "", "", "" };
				ArrHeader[15] = new String[] { "�����������Ͳ��ֵQb,ad(J/g)",
						"�����������Ͳ��ֵQb,ad(J/g)", "�����������Ͳ��ֵQb,ad(J/g)",
						"" + rs.getDouble("QBAD") + "", "", "" };
				ArrHeader[16] = new String[] { "�������λ��ֵQgr,d(J/g)",
						"�������λ��ֵQgr,d(J/g)", "�������λ��ֵQgr,d(J/g)",
						"" + rs.getDouble("QGRD") + "", "", "" };
				ArrHeader[17] = new String[] { "�����������λ��ֵQgr,ad(J/g)",
						"�����������λ��ֵQgr,ad(J/g)", "�����������λ��ֵQgr,ad(J/g)",
						"" + rs.getDouble("QGRAD") + "", "", "" };
				ArrHeader[18] = new String[] { "�����޻һ���λ��ֵQgr,daf(J/g)",
						"�����޻һ���λ��ֵQgr,daf(J/g)", "�����޻һ���λ��ֵQgr,daf(J/g)",
						"" + rs.getDouble("GANZWHJGWRZ") + "", "", "" };
				ArrHeader[19] = new String[] { "�յ�����λ��ֵQnet,ar(J/g)",
						"�յ�����λ��ֵQnet,ar(J/g)", "�յ�����λ��ֵQnet,ar(J/g)",
						"" + rs.getDouble("QNETAR") + "",
						"" + num + "" + "(ǧ��/ǧ��)", "" + num + "" + "(ǧ��/ǧ��)" };
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 100, 95, 95, 155, 95, 95 };

		rt.setTitle("��  ¯  ú  ��  ��  ��", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:" + str, -1);
		rt.setDefautlFooter(3, 1, "�����ˣ�", -1);
		rt.setDefautlFooter(4, 1, "��ˣ�" + shangjshry, -1);
		rt.setDefautlFooter(5, 2, "����Ա��" + lury, Table.ALIGN_RIGHT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(20, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		for (int i = 1; i < 20; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
		for (int i = 1; i <= 20; i++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCellValue(4, 4, rt.body.format(rt.body.getCellValue(4, 4),
				"0.0"));
		for (int i = 5; i < 16; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0.00"));
		}
		for (int i = 16; i < 21; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0"));
		}
		// rt.body.setCellValue(i, j, strValue);

		rt.body.setCellFontSize(4, 0, 9);
		rt.body.setCells(2, 1, 20, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		// rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.merge(2, 1, 20, 3);
		rt.body.merge(2, 5, 20, 6);
		rt.body.merge(3, 2, 3, 6);
		// rt.body.merge(3, 4, 3, 6);
		rt.body.ShowZero = false;

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(43);

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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if (visit.isFencb()) {
			tb1.addText(new ToolbarText("����:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("DCSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('DCSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("��¯����:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("rulrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("������Ϣ:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("������Ϣ:"));
		ComboBox jiz = new ComboBox();
		jiz.setTransform("ChangbSelect");
		jiz.setWidth(130);
		jiz
				.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		tb1.addField(jiz);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
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
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		((DateField) getToolbar().getItem("rulrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			
			this.setDCModel(null);
			this.setDCValue(null);
			
			getSelectData();
		}
		
//			if (riqchange) {
//				riqchange = false;
//				setBianmValue(null);
//				setBianmModel(null);
//			}
//			
		if(visit.isFencb()){
			
			if(DCflage){
				DCflage=false;
				setChangbValue(null);
				setChangbModel(null);
				setBianmValue(null);
				setBianmModel(null);
			}
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		
		String diancxxb_id=visit.getDiancxxb_id()+"";
		
//			if(visit.isFencb()){
//				
//				diancxxb_id=this.getDCValue().getStrId();
//			}
		
		
		sb.append("select item.id,item.mingc from item,itemsort where item.itemsortid=itemsort.id and itemsort.bianm='RUHYBZ'");
		setBianmModel(new IDropDownModel(sb.toString(),"��ѡ��"));
	}

	//����������
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		
		String diancxxb_id=visit.getDiancxxb_id()+"";
		
//			if(visit.isFencb()){
//				
//				diancxxb_id=this.getDCValue().getStrId();
//			}
		
		sb.append("select id,mingc from jizfzb where diancxxb_id="
				+ diancxxb_id);

		setChangbModel(new IDropDownModel(sb.toString(),"��ѡ��"));
	}



//	�ֳ���

private boolean DCflage=false; 
public IDropDownBean getDCValue() {
	if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
		if (getDCModel().getOptionCount() > 0) {
			setDCValue((IDropDownBean) getDCModel().getOption(0));
		}
	}
	return ((Visit) this.getPage().getVisit()).getDropDownBean3();
}

public void setDCValue(IDropDownBean value) {
	
	if(((Visit) this.getPage().getVisit()).getDropDownBean3()!=null){
		
		if(!((Visit) this.getPage().getVisit()).getDropDownBean3().equals(value)){
			DCflage=true;
		}
	}
	((Visit) this.getPage().getVisit()).setDropDownBean3(value);
}

public IPropertySelectionModel getDCModel() {
	if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
		setDCModel();
	}
	return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
}

public void setDCModel(IPropertySelectionModel value) {
	((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
}

public void setDCModel() {
	Visit visit = (Visit) this.getPage().getVisit();
	StringBuffer sb = new StringBuffer();

	sb.append("select id,mingc from diancxxb where fuid="
			+ visit.getDiancxxb_id());

	setDCModel(new IDropDownModel(sb.toString(),"��ѡ��"));
}
}


