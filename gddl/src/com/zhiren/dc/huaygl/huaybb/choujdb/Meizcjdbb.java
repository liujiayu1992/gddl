package com.zhiren.dc.huaygl.huaybb.choujdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import webservice.IDropDownSelectionModel;

import com.zhiren.common.CustomDate;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Meizcjdbb extends BasePage {

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	public Date _riqvalue = getMonthFirstday(new Date());

	public Date _afterValue = new Date();

	private boolean dateChange = false;

	public Date getRiq() {
		return _riqvalue;
	}

	public void setRiq(Date _value) {
		if (_riqvalue == null) {
			_riqvalue = getMonthFirstday(new Date());
		} else {
			if (!(_riqvalue.equals(_value))) {
				_riqvalue = _value;
			}
		}

	}

	public Date getAfter() {
		return _afterValue;
	}

	public void setAfter(Date _value) {
		if (_afterValue == null) {
			_afterValue = _value;
		} else {
			if (!(_afterValue.equals(_value))) {
				dateChange = true;
				_afterValue = _value;
			}
		}
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

	private int _Flag = 0;

	public int getFlag() {
		JDBCcon con = new JDBCcon();
		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '�����еĳ����Ƿ���ʾ'";
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				_Flag = rs.getInt("ZHUANGT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return _Flag;
	}

	public void setFlag(int _value) {
		_Flag = _value;
	}

	private int Flag1 = 0;

	public int getFlag1() {
		JDBCcon con = new JDBCcon();
		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '��������ձ����±��Ƿ�ֻ��˺�����'";
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				Flag1 = rs.getInt("ZHUANGT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Flag1;
	}

	public void setFlag1(int _value) {
		Flag1 = _value;
	}

	private int xianszt = 0;

	public int getXianszt() {
		JDBCcon con = new JDBCcon();
		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '�����е�С��λ���ͼ��㷽ʽ�Ƿ���ʾ'";
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				xianszt = rs.getInt("ZHUANGT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return xianszt;
	}

	public void setXianszt(int value) {
		xianszt = value;
	}

	private String REPORT_NAME_RUCMZJYYB = "Meizcjdbb";// �볧ú�ʼ��鱨��

	private String REPORT_NAME_RUCMZJYYB_A = "Rucmzjyyb_A";// �볧ú�ʼ��鱨��

	private String REPORT_NAME_RUCMZJYYB_B = "Rucmzjyyb_B";// �볧ú�ʼ��鱨��

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB)) {
			return getRucmzjyyb();
		} else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_A)) {
			return getRucmzjyyb_A();
		} else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_B)) {
			return getRucmzjyyb_B();
		} else {
			return "�޴˱���";
		}
	}

	private String getRucmzjyyb() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		String Radix = getRadixValue().getValue();
		int x = (int) getCumStyleValue().getId();

		StringBuffer buffer = new StringBuffer();

		String SQL = "";
		buffer.append(SQL);
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[2][20];
		ArrHeader[0] = new String[] { "ú�����", "ú��λ", "������", "����", "����(��)",
				"����", "", "", "", "����", "", "", "", "����", "", "", "",
				"����ֵ<br>Kcal/Kg", "��ֵ��<br>Kcal/Kg", "����Ա" };
		ArrHeader[1] = new String[] { "ú�����", "ú��λ", "������", "����", "����(��)",
				"ȫˮ��<br>Mt(%)", "�����<br>�ҷ�<br>Ad(%)",
				"������<br>�һ���<br>����<br>Vdaf(%)", "�յ���<br>��λ��<br>ֵ<br>(Kcal/Kg)",
				"ȫˮ��<br>Mt(%)", "�����<br>�ҷ�<br>Ad(%)",
				"������<br>�һ���<br>����<br>Vdaf(%)", "�յ���<br>��λ��<br>ֵ<br>(Kcal/Kg)",
				"ȫˮ��<br>Mt(%)", "�����<br>�ҷ�<br>Ad(%)",
				"������<br>�һ���<br>����<br>Vdaf(%)", "�յ���<br>��λ��<br>ֵ<br>(Kcal/Kg)",
				"����ֵ<br>Kcal/Kg", "��ֵ��<br>Kcal/Kg", "����Ա" };
		int[] ArrWidth = new int[20];

		ArrWidth = new int[] { 90, 90, 50, 40, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50,50 };

		rt.setTitle("ú  ��  ��  ��  ��  ��  ��", ArrWidth);
		rt.title.setRowHeight(2, 56);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "��������:"
				+ custom.FormatDate(getRiq(), "yyyy��MM��dd��") + "��"
				+ custom.FormatDate(getAfter(), "yyyy��MM��dd��"),
				Table.ALIGN_LEFT);

		String[] strFormat = new String[] { "", "", "", "", "", "", "",
				"", "", "", "", "", "", "", "", "",
				"", "", "","" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		for(int i=1;i<6;i++){
			rt.body.merge(1, i, 2, i);
		}
		for(int j=18;j<21;j++){
			rt.body.merge(1, j, 2, j);
		}
		rt.body.mergeCell(1, 6, 1, 9);
		rt.body.mergeCell(1, 10, 1, 13);
		rt.body.mergeCell(1, 14, 1, 17);
		rt.body.setPageRows(19);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 19; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:"
				+ custom.FormatDate(new Date(), "yyyy��MM��dd��"),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "�Ʊ�:", Table.ALIGN_LEFT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();

		return rt.getAllPagesHtml();

	}

	// ����A�볧ú�ʼ��鱨��
	private String getRucmzjyyb_A() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		String Radix = getRadixValue().getValue();
		int x = (int) getCumStyleValue().getId();
		String cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
				+ ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
				+ ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
		switch (x) {
		case 1:
			cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix + ")";
			break;
		case 2:
			cumStyle = "ROUND_NEW(SUM(NVL(C.BIAOZ,0))," + Radix
					+ ") + ROUND_NEW(SUM(NVL(C.YINGD,0))," + Radix
					+ ") - ROUND_NEW(SUM(NVL(C.KUID,0))," + Radix + ")";
			break;
		case 3:
			cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
					+ ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix + ")";
			break;
		case 4:
			cumStyle = "ROUND_NEW(SUM(NVL(C.MAOZ,0))," + Radix
					+ ")-ROUND_NEW(SUM(NVL(C.PIZ,0))," + Radix
					+ ")-ROUND_NEW(SUM(NVL(C.KOUD,0))," + Radix + ")";
			break;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append("");

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][19];

		ArrHeader[0] = new String[] { "ú�����", "ú��λ", "��վ", "Ʒ��", "����",
				"������<br>��(��)", "ȫˮ<br>��<br>(%)Mt",
				"����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad", "����<br>��<br>�ҷ�<br>(%)Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", "������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", "��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>(%)<br>Sdaf", "����<br>��ȫ<br>��(%)<br>St,d",
				"����<br>�޻�<br>����<br>(%)<br>Hdaf" };
		int[] ArrWidth = new int[19];

		ArrWidth = new int[] { 90, 90, 50, 50, 40, 50, 40, 40, 40, 40, 50, 50,
				50, 50, 50, 50, 50, 40, 50 };

		rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
		rt.title.setRowHeight(2, 56);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "��������:"
				+ custom.FormatDate(getRiq(), "yyyy��MM��dd��") + "��"
				+ custom.FormatDate(getAfter(), "yyyy��MM��dd��"),
				Table.ALIGN_LEFT);

		String[] strFormat = new String[] { "", "", "", "", "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(19);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 19; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:"
				+ custom.FormatDate(new Date(), "yyyy��MM��dd��"),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "�Ʊ�:", Table.ALIGN_LEFT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();

		return rt.getAllPagesHtml();

	}

	// ����B�볧ú�ʼ��鱨��
	private String getRucmzjyyb_B() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		String Radix = getRadixValue().getValue();
		int x = (int) getCumStyleValue().getId();
		StringBuffer buffer = new StringBuffer();

		buffer.append("");

		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[1][19];

		ArrHeader[0] = new String[] { "ú�����", "ú��λ", "��վ", "Ʒ��", "����",
				"������<br>��(��)", "ȫˮ<br>��<br>(%)Mt",
				"����<br>����<br>��ˮ<br>��<br>(%)Mad",
				"����<br>����<br>����<br>��<br>(%)Aad", "����<br>��<br>�ҷ�<br>(%)Ad",
				"�յ�<br>��<br>�ҷ�<br>(%)Aar", "������<br>�����<br>����<br>(%)Vad",
				"������<br>�һ���<br>����<br>(%)Vdaf", "��Ͳ��<br>����<br>(J/g)<br>Qb,ad",
				"�յ���<br>��λ��<br>����(J/g)<br>Qnet,ar",
				"�յ�<br>����<br>λ��<br>ֵ(Kcal<br>/Kg)",
				"����<br>�޻�<br>����<br>(%)<br>Sdaf", "����<br>��ȫ<br>��(%)<br>St,d",
				"����<br>�޻�<br>����<br>(%)<br>Hdaf" };
		int[] ArrWidth = new int[19];

		ArrWidth = new int[] { 90, 90, 50, 50, 40, 50, 40, 40, 40, 40, 50, 50,
				50, 50, 50, 50, 50, 40, 50 };

		rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
		rt.title.setRowHeight(2, 56);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 5, "��������:"
				+ custom.FormatDate(getRiq(), "yyyy��MM��dd��") + "��"
				+ custom.FormatDate(getAfter(), "yyyy��MM��dd��"),
				Table.ALIGN_LEFT);

		String[] strFormat = new String[] { "", "", "", "", "", "", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(19);
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 19; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:"
				+ custom.FormatDate(new Date(), "yyyy��MM��dd��"),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "�Ʊ�:", Table.ALIGN_LEFT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();

		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}


	// �����������
	private boolean falg = false;

	private IDropDownBean ChangbValue;

	public IDropDownBean getChangbValue() {
		if (ChangbValue == null) {
			ChangbValue = (IDropDownBean) ChangbModel.getOption(1);
		}
		return ChangbValue;
	}

	public void setChangbValue(IDropDownBean Value) {
		if (!(ChangbValue == Value)) {
			ChangbValue = Value;
			falg = true;
		}

	}

	private IPropertySelectionModel ChangbModel;

	public void setChangbModel(IPropertySelectionModel value) {
		ChangbModel = value;
	}

	public IPropertySelectionModel getChangbModel() {
		if (ChangbModel == null) {
			getChangbModels();
		}
		return ChangbModel;
	}

	public IPropertySelectionModel getChangbModels() {
		StringBuffer sql = new StringBuffer("SELECT ID,MINGC FROM CHANGBB");
		ChangbModel = new IDropDownModel(sql);
		return ChangbModel;
	}

	// ����������
	private IDropDownBean CumStyleValue;

	public IDropDownBean getCumStyleValue() {
		if (CumStyleValue == null) {
			CumStyleValue = (IDropDownBean) CumStyleModel.getOption(1);
		}
		return CumStyleValue;
	}

	public void setCumStyleValue(IDropDownBean Value) {
		CumStyleValue = Value;
	}

	private IPropertySelectionModel CumStyleModel;

	public void setCumStyleModel(IPropertySelectionModel value) {
		CumStyleModel = value;
	}

	public IPropertySelectionModel getCumStyleModel() {
		if (CumStyleModel == null) {
			getCumStyleModels();
		}
		return CumStyleModel;
	}

	public IPropertySelectionModel getCumStyleModels() {
		List listCumStyle = new ArrayList();
		listCumStyle.add(new IDropDownBean(1, "Ʊ��"));
		listCumStyle.add(new IDropDownBean(2, "Ʊ��+ӯ��-����"));
		listCumStyle.add(new IDropDownBean(3, "ë�� - Ƥ��"));
		listCumStyle.add(new IDropDownBean(4, "ë��-Ƥ��-�۶�"));
		CumStyleModel = new IDropDownSelectionModel(listCumStyle);
		return CumStyleModel;
	}

	// ���䷽ʽ������
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		List listCumStyle = new ArrayList();
		if (getFlag1() == 0) {
			listCumStyle.add(new IDropDownBean(0, "ȫ��"));
		} else {
			listCumStyle.add(new IDropDownBean(0, "ȫ��"));
			listCumStyle.add(new IDropDownBean(1, "����"));
			listCumStyle.add(new IDropDownBean(2, "����"));
		}
		YunsfsModel = new IDropDownSelectionModel(listCumStyle);
		return YunsfsModel;
	}

	// С��λ������
	private IDropDownBean RadixValue;

	public IDropDownBean getRadixValue() {
		if (RadixValue == null) {
			RadixValue = (IDropDownBean) RadixModel.getOption(0);
		}
		return RadixValue;
	}

	public void setRadixValue(IDropDownBean Value) {
		RadixValue = Value;
	}

	private IPropertySelectionModel RadixModel;

	public void setRadixModel(IPropertySelectionModel value) {
		RadixModel = value;
	}

	public IPropertySelectionModel getRadixModel() {
		if (RadixModel == null) {
			getRadixModels();
		}
		return RadixModel;
	}

	public IPropertySelectionModel getRadixModels() {
		List listRadix = new ArrayList();
		listRadix.add(new IDropDownBean(0, "0"));
		listRadix.add(new IDropDownBean(1, "1"));
		listRadix.add(new IDropDownBean(2, "2"));
		listRadix.add(new IDropDownBean(3, "3"));
		RadixModel = new IDropDownSelectionModel(listRadix);
		return RadixModel;
	}

}
