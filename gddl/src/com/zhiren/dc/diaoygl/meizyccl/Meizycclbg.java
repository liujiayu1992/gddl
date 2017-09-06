package com.zhiren.dc.diaoygl.meizyccl;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Meizycclbg extends BasePage {
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getData();
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

	private String getData() {
		JDBCcon con = new JDBCcon();
		
		String sql = "select gys.mingc as gongmdw, mk.mingc as gongmkd, ys.mingc as yunsdw, to_char(yc.daocsj, 'yyyy-mm-dd') as daocsj, yc.* \n" +
				     "  from meizycb yc, gongysb gys, meikxxb mk, yunsdwb ys \n" +
				     " where to_char(yc.daocsj, 'yyyy-mm-dd') = '" + getRiqi() + 
					 "'  and yc.diancxxb_id = " + getTreeid() + 
					 "   and yc.bianh = '" + getBianh() + "'";
		ResultSetList rsl = con.getResultSetList(sql);
		String gongmdw = "";                  // ��ú��λ
		String gongmkd = "";                  // ��ú���
		String yunsdw = "";                   // ���䵥λ
		String yunsclch_hhc = "";             // ���䳵�����ź���
		String shiszl = "";                   // ʵ������
		String daocsj = "";                   // ����ʱ��
		String yicqk = "";                    // �쳣���
		String ranlbqz = "";                  // ȼ�ϲ�ǩ��
		String yicms = "";                    // �쳣����
		String huanbbqz = "";                 // ������ǩ�� 
		String yicds = "";                    // �쳣����
		String faxr = "";                     // ������
		String yunsdwhsj = "";                // ���䵥λ��˾��
		String gongmdwqz = "";                // ��ú��λǩ��
		String meizzgyj = "";                 // ú���������
		String meizzgqz = "";                 // ú������ǩ��
		String lingdyj = "";                  // �쵼���
		String lingdqz = "";                  // �쵼ǩ��
		
		if (rsl.next()) {
			gongmdw = rsl.getString("gongmdw");
			gongmkd = rsl.getString("gongmkd");
			yunsdw = rsl.getString("yunsdw");
			yunsclch_hhc = rsl.getString("yunsclch_hhc");
			shiszl = rsl.getString("shiszl");
			daocsj = rsl.getString("daocsj");
			yicqk = rsl.getString("yicqk");
			ranlbqz = "ȷ��ǩ��<br><br><br>ȼ�ϲ���" + rsl.getString("ranlbqz");
			yicms = rsl.getString("yicms");
			huanbbqz = "��������" + rsl.getString("huanbbqz");
			yicds = rsl.getString("yicds");
			faxr = "�����ˣ�" + rsl.getString("faxr");
			yunsdwhsj = "���䵥λ��˾����<br>&nbsp&nbsp" + rsl.getString("yunsdwhsj");
			gongmdwqz = "��ú��λ��" + rsl.getString("gongmdwqz");
			meizzgyj = rsl.getString("meizzgyj");
			meizzgqz = "ǩ�֣�" + rsl.getString("meizzgqz");
			lingdyj = rsl.getString("lingdyj");
			lingdqz = "ǩ�֣�" + rsl.getString("lingdqz");
		} else {
			return null;
		}
		
		String date = DateUtil.FormatDate(new Date());
		date = date.substring(0, 4)+"��"+date.substring(5, 7)+"��"+date.substring(8, 10)+"��";
		
		String ArrHeader[][] = new String[15][4];
		ArrHeader[0] = new String[]{"", "<font style='font-size:22pt'>�� �� �� �� �� �� �� �� �� �� �� ˾</font><br><br>" +
									"<font style='font-size:16pt'>YANGCHENG INTERNATIONAL POWER GENERATING CO.,LTD</font>", 
									"�� �� �� �� �� �� �� �� �� �� �� ˾<br><br>YANGCHENG INTERNATIONAL POWER GENERATING CO.,LTD", 
									"�� �� �� �� �� �� �� �� �� �� �� ˾<br><br>YANGCHENG INTERNATIONAL POWER GENERATING CO.,LTD"};
		ArrHeader[1] = new String[]{"ú���쳣������", "ú���쳣������", "ú���쳣������", "ú���쳣������"};
		ArrHeader[2] = new String[]{"��ţ�"+getBianh(), "", "", date};
		ArrHeader[3] = new String[]{"", "", "", ""};
		ArrHeader[4] = new String[]{"һ��ú���쳣��ú��λ�����䵥λ���", "", "", ""};
		ArrHeader[5] = new String[]{"��ú��λ", gongmdw, "��ú���", gongmkd};
		ArrHeader[6] = new String[]{"���䵥λ", yunsdw, "", ""};
		ArrHeader[7] = new String[]{"���䳵������<br>(����)", yunsclch_hhc, "", ""};
		ArrHeader[8] = new String[]{"ʵ������", shiszl, "����ʱ��", daocsj};
		ArrHeader[9] = new String[]{"����ú�ʲ鴦����", "", "", ""};
		ArrHeader[10] = new String[]{"����ú����<br>���������", yicqk, "", ranlbqz};
		ArrHeader[11] = new String[]{"����ú����<br>���������", yicms+"<br><br><br><br>&nbsp&nbsp&nbsp&nbsp��ȷ���쳣ú�ʶ���Ϊ:&nbsp"+yicds+"&nbsp��", "", huanbbqz};
		ArrHeader[12] = new String[]{"����ú����<br>���������", faxr, yunsdwhsj, gongmdwqz};
		ArrHeader[13] = new String[]{"ú������<br>�������", meizzgyj, "", meizzgqz};
		ArrHeader[14] = new String[]{"�쵼<br>���", lingdyj, "", lingdqz};
		
		int[] ArrWidth = new int[]{140, 200, 200, 150};
		
		Report rt = new Report();

		rt.setBody(new Table(ArrHeader,0,0,0));
		
		rt.getArrWidth(ArrWidth, Report.PAPER_A4_WIDTH);
		rt.body.setWidth(ArrWidth);
//		rt.setTitle("ú���쳣������", ArrWidth);
		
		rt.body.setBorder(0, 0, 0, 1);
		rt.body.setCells(1, 1, 4, 4, Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(1, 1, 1, 4, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(1, 2, 1, 4, Table.PER_BORDER_BOTTOM, 2);
		rt.body.setCells(2, 1, 2, 1, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(3, 1, 3, 1, Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(3, 3, 3, 3, Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(3, 1, 3, 4, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(4, 1, 4, 4, Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(4, 1, 4, 4, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(5, 1, 5, 1, Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(10, 1, 10, 1, Table.PER_BORDER_RIGHT, 0);
		rt.body.setCells(6, 1, 9, 1, Table.PER_BORDER_LEFT, 2);
		rt.body.setCells(11, 1, 15, 1, Table.PER_BORDER_LEFT, 2);
		rt.body.setCells(5, 1, 5, 4, Table.PER_BORDER_BOTTOM, 2);
		rt.body.setCells(9, 1, 9, 4, Table.PER_BORDER_BOTTOM, 2);
		rt.body.setCells(10, 1, 10, 4, Table.PER_BORDER_BOTTOM, 2);
		rt.body.setCells(6, 4, 6, 4, Table.PER_BORDER_RIGHT, 2);
		rt.body.setCells(7, 2, 8, 2, Table.PER_BORDER_RIGHT, 2);
		rt.body.setCells(9, 4, 9, 4, Table.PER_BORDER_RIGHT, 2);
		rt.body.setCells(11, 4, 15, 4, Table.PER_BORDER_RIGHT, 2);
		rt.body.setCells(11, 4, 11, 4, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setCells(12, 4, 12, 4, Table.PER_BORDER_BOTTOM, 0);
		
		rt.body.setPageRows(21);
		rt.body.mergeCell(1, 2, 1, 4);
		rt.body.mergeCell(2, 1, 2, 4);
		rt.body.mergeCell(3, 2, 3, 3);
		rt.body.mergeCell(4, 1, 4, 4);
		rt.body.mergeCell(5, 1, 5, 4);
		rt.body.mergeCell(7, 2, 7, 4);
		rt.body.mergeCell(8, 2, 8, 4);
		rt.body.mergeCell(10, 1, 10, 4);
		rt.body.mergeCell(11, 1, 13, 1);
		rt.body.mergeCell(11, 2, 11, 3);
		rt.body.mergeCell(12, 2, 12, 3);
		rt.body.mergeCell(14, 2, 14, 3);
		rt.body.mergeCell(15, 2, 15, 3);
		for (int i = 1; i <= 16; i ++) {
			rt.body.setRowHeight(i, 60);			
		}
		rt.body.setRowHeight(3, 5);
		rt.body.setRowHeight(4, 5);
		rt.body.setRowHeight(11, 100);
		rt.body.setRowHeight(12, 200);
		rt.body.setRowHeight(14, 90);
		rt.body.setRowHeight(15, 90);
		
		for (int i = 1; i <= 4; i ++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.setCellAlign(1, 1, Table.ALIGN_LEFT);
		rt.body.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.body.setCellAlign(3, 4, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(5, 1, Table.ALIGN_LEFT);
		rt.body.setCellAlign(10, 1, Table.ALIGN_LEFT);
		
		rt.body.setCells(1, 2, 1, 4, Table.PER_FONTNAME, "����");
//		rt.body.setCells(1, 2, 1, 4, Table.PER_FONTSIZE, 16);
		rt.body.setCells(2, 1, 2, 4, Table.PER_FONTNAME, "����");
		rt.body.setCells(2, 1, 2, 4, Table.PER_FONTSIZE, 18);
		rt.body.setCells(5, 1, 5, 4, Table.PER_FONTNAME, "����");
		rt.body.setCells(5, 1, 5, 4, Table.PER_FONTSIZE, 13);
		rt.body.setCells(10, 1, 10, 4, Table.PER_FONTNAME, "����");
		rt.body.setCells(10, 1, 10, 4, Table.PER_FONTSIZE, 13);
		rt.body.setCells(3, 1, 3, 1, Table.PER_FONTNAME, "����");
		rt.body.setCells(3, 1, 3, 1, Table.PER_FONTSIZE, 11);
		rt.body.setCells(3, 4, 3, 4, Table.PER_FONTNAME, "����");
		rt.body.setCells(3, 4, 3, 4, Table.PER_FONTSIZE, 11);
		
		rt.body.setCellImage(1, 1, 120, 120, "imgs/report/YCBZ.gif");
		
		rt.body.setCellImage(4, 1, 690, 7, "imgs/report/GDHX.gif");

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();

	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("����ʱ��:"));
		DateField df = new DateField();
		df.setValue(getRiqi());
		df.Binding("RIQI", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("RIQI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox bianh = new ComboBox();
		bianh.setTransform("BIANH");
		bianh.setWidth(90);
		bianh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(bianh);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");

		tb1.addItem(tb);
		setToolbar(tb1);
	}

    // ��¼���
	public String getBianh() {
		return getBianhValue().getValue();
	}
	
	// ���ڿؼ�
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);

			visit.setDropDownBean7(null);
			visit.setProSelectionModel7(null);

			visit.setDefaultTree(null);
			setTreeid(null);
		}
		getToolbars();
		blnIsBegin = true;
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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

    // ���������
	public IDropDownBean getBianhValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean7()==null){
			if (getBianhModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean7((IDropDownBean)getBianhModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean7();
	}

	public void setBianhValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setBianhModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel7() == null) {
			getIBianhModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel7();
	}

	public void getIBianhModels() {		
		String sql = "select id, bianh from meizycb order by bianh";
		((Visit)getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql));
	}
	
	// �糧����
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
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
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql);
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
