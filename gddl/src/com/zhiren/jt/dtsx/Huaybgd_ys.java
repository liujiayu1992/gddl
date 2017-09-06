package com.zhiren.jt.dtsx;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * 
 * @author huochaoyuan
 * ���鱨�浥����ʾԭʼ����ֵ�������й�Լ
 *��Դ�����õ���Huaybgd_ys
 */
public class Huaybgd_ys extends BasePage {

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

	
	private String getHuaybgd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		boolean isChes = MainGlobal.getXitxx_item("����", "���鱨�浥��ʾ����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,decode(y.lurry,null,' ',y.lurry) as lurry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,2) as MT,round_new(z.mad,2) as MAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,z.qbad*1000 as QBAD,z.qgrd*1000 as QGRD,z.qgrad*1000 as QGRAD,\n");
		sqlHuaybgd
				.append("z.qgrad_daf*1000 as GANZWHJGWRZ,z.qnet_ar*1000 as QNETAR,round_new(z.qnet_ar* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,\n");
		sqlHuaybgd
				.append("'"
						+ getBianmValue().getValue()
						+ "' as bianh,a.meikdwmc,a.chez,a.pinz,a.cheph,a.ches,a.meil,z.beiz from zhillsb z,yangpdhb y,\n");
		sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
		sqlHuaybgd.append("cz.mingc as chez,\n");
		sqlHuaybgd.append("p.mingc as pinz,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd
				.append("round_new(sum(f.laimsl), 2) as meil,\n");
		sqlHuaybgd.append("sum(f.ches) AS CHES, \n");			
		sqlHuaybgd.append("GETHUAYBBCHEPS(f.zhilb_id) AS CHEPH \n");
		sqlHuaybgd
				.append("from fahb f, zhillsb z,meikxxb m,chezxxb cz,pinzb p\n");
		sqlHuaybgd.append("where z.zhilb_id = f.zhilb_id\n");
		sqlHuaybgd.append(" and f.pinzb_id=p.id\n");
		sqlHuaybgd.append("and f.faz_id=cz.id\n");
		sqlHuaybgd.append("and f.meikxxb_id=m.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId()).append("\n");
		sqlHuaybgd.append("group by m.mingc,cz.mingc,p.mingc,f.zhilb_id) a \n");
		sqlHuaybgd.append("where z.zhilb_id=a.zhilb_id\n");
		sqlHuaybgd.append("and y.zhilblsb_id=z.id\n");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
		ResultSet rs = con.getResultSet(sqlHuaybgd);
		String shangjshry = "";
		String lury = "";
		String[][] ArrHeader = new String[21][6];
		try {
			if (rs.next()) {
				lury = rs.getString("HUAYY");
				
				//�Ƿ���ʾ����
				StringBuffer buffer = new StringBuffer();
				String cheph = "";
				if (!isChes) {
					cheph = rs.getString("CHEPH");
					String[] list = cheph.split(",");				
					for (int i = 1; i <= list.length; i++) {
						if (i % 11 == 0) {
							buffer.append(list[i - 1] + ",<br>");
						} else {
							buffer.append(list[i - 1] + ",");
						}
					}
					cheph = buffer.toString().substring(0, buffer.length() - 1);				
				}
				String num = rs.getString("FRL");
				ArrHeader[0] = new String[] { "������",
						"" + rs.getString("BIANH") + "", "���",
						"" + rs.getString("MEIKDWMC") + "", "��վ",
						"" + rs.getString("chez") + "" };

				ArrHeader[1] = new String[] { "��������",
						"" + rs.getString("HUAYRQ") + "", "��������",
						"" + rs.getString("CAIYRQ") + "", "ú��(t)",
						"" + rs.getString("MEIL") + "" };
				ArrHeader[2] = new String[] { "ú��",
						"" + rs.getString("PINZ") + "", "��������Ա",
						"" + rs.getString("lurry") + "",
						"" + rs.getString("lurry") + "",
						"" + rs.getString("lurry") + "" };
				
				//�Ƿ���ʾ����
				if (isChes){
					ArrHeader[3] = new String[] { "����:", "&nbsp;&nbsp;" + rs.getString("ches"),
							"&nbsp;&nbsp;" + rs.getString("ches"), "&nbsp;&nbsp;" + rs.getString("ches"), 
							"&nbsp;&nbsp;" + rs.getString("ches"),"&nbsp;&nbsp;" + rs.getString("ches")};
				} else {
					ArrHeader[3] = new String[] { "����", "" + cheph + "",
							"" + cheph + "", "" + cheph + "", "" + cheph + "",
							"" + cheph + "" };
				}
				ArrHeader[4] = new String[] { "ȫˮ��Mt(%)", "ȫˮ��Mt(%)",
						"ȫˮ��Mt(%)", "" + rs.getDouble("MT") + "", "��ע", "��ע" };
				ArrHeader[5] = new String[] { "���������ˮ��Mad(%)", "���������ˮ��Mad(%)",
						"���������ˮ��Mad(%)", "" + rs.getDouble("MAD") + "", "", "" };
				ArrHeader[6] = new String[] { "����������ҷ�Aad(%)", "����������ҷ�Aad(%)",
						"����������ҷ�Aad(%)", "" + rs.getDouble("AAD") + "", "", "" };
				ArrHeader[7] = new String[] { "�յ����ҷ�Aar(%)", "�յ����ҷ�Aar(%)",
						"�յ����ҷ�Aar(%)", "" + rs.getDouble("AAR") + "", "", "" };
				ArrHeader[8] = new String[] { "������ҷ�Ad(%)", "������ҷ�Ad(%)",
						"������ҷ�Ad(%)", "" + rs.getDouble("AD") + "", "0", "0" };
				ArrHeader[9] = new String[] { "����������ӷ���Vad(%)",
						"����������ӷ���Vad(%)", "����������ӷ���Vad(%)",
						"" + rs.getDouble("VAD") + "", "", "" };
				ArrHeader[10] = new String[] { "�����޻һ��ӷ���Vdaf(%)",
						"�����޻һ��ӷ���Vdaf(%)", "�����޻һ��ӷ���Vdaf(%)",
						"" + rs.getDouble("VDAF") + "", "", "" };
				ArrHeader[11] = new String[] { "���������ȫ��St,ad(%)",
						"���������ȫ��St,ad(%)", "���������ȫ��St,ad(%)",
						"" + rs.getDouble("STAD") + "", "", "" };
				ArrHeader[12] = new String[] { "�����ȫ��St,d(%)", "�����ȫ��St,d(%)",
						"�����ȫ��St,d(%)", "" + rs.getDouble("STD") + "", "", "" };
				ArrHeader[13] = new String[] { "�յ���ȫ��St,ar(%)",
						"�յ���ȫ��St,ar(%)", "�յ���ȫ��St,ar(%)",
						"" + rs.getDouble("STAR") + "", "", "" };
				ArrHeader[14] = new String[] { "�����������Had(%)", "�����������Had(%)",
						"�����������Had(%)", "" + rs.getDouble("HAD") + "", "", "" };
				ArrHeader[15] = new String[] { "�յ�����Har(%)", "�յ�����Har(%)",
						"�յ�����Har(%)", "" + rs.getDouble("HAR") + "", "", "" };
				ArrHeader[16] = new String[] { "�����������Ͳ��ֵQb,ad(J/g)",
						"�����������Ͳ��ֵQb,ad(J/g)", "�����������Ͳ��ֵQb,ad(J/g)",
						"" + rs.getDouble("QBAD") + "", "", "" };
				ArrHeader[17] = new String[] { "�������λ��ֵQgr,d(J/g)",
						"�������λ��ֵQgr,d(J/g)", "�������λ��ֵQgr,d(J/g)",
						"" + rs.getDouble("QGRD") + "", "", "" };
				ArrHeader[18] = new String[] { "�����������λ��ֵQgr,ad(J/g)",
						"�����������λ��ֵQgr,ad(J/g)", "�����������λ��ֵQgr,ad(J/g)",
						"" + rs.getDouble("QGRAD") + "", "", "" };
				ArrHeader[19] = new String[] { "�����޻һ���λ��ֵQgr,daf(J/g)",
						"�����޻һ���λ��ֵQgr,daf(J/g)", "�����޻һ���λ��ֵQgr,daf(J/g)",
						"" + rs.getDouble("GANZWHJGWRZ") + "", "", "" };
				ArrHeader[20] = new String[] { "�յ�����λ��ֵQnet,ar(J/g)",
						"�յ�����λ��ֵQnet,ar(J/g)", "�յ�����λ��ֵQnet,ar(J/g)",
						"" + rs.getDouble("QNETAR") + "",
						"" + num + "" + "(ǧ��/ǧ��)", "" + num + "" + "(ǧ��/ǧ��)" };
				String beiz = rs.getString("beiz");
				
				if (beiz!=null && !"".equals(beiz)) {
					for (int i=5; i<=19; i++) {
						ArrHeader[i][4] = beiz;
						ArrHeader[i][5] = beiz;
					}
				}
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 100, 95, 95, 155, 95, 95 };

		rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
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

		rt.setBody(new Table(21, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		for (int i = 1; i < 21; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
		for (int i = 1; i <= 21; i++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCellValue(5, 4, rt.body.format(rt.body.getCellValue(5, 4),
				"0.0"));
		for (int i = 6; i < 17; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0.00"));
		}
		for (int i = 17; i < 22; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0"));
		}
		// rt.body.setCellValue(i, j, strValue);

		rt.body.setCellFontSize(4, 2, 9);
		rt.body.setCells(2, 1, 21, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.merge(2, 1, 21, 3);
		rt.body.merge(2, 5, 21, 6);
		rt.body.merge(4, 2, 4, 6);
		rt.body.merge(3, 4, 3, 6);
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
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
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
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	
	
	
	
	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//		if (visit.isFencb()) {
//			tb1.addText(new ToolbarText("����:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb
//					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
		
		
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
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
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
			setChangbValue(null);
			setChangbModel(null);
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
		StringBuffer sb = new StringBuffer();
//		sb
//				.append(
//						"select l.id,z.bianm from zhuanmb z,zhillsb l,caiyb c\n")
//				.append(
//						"where z.zhillsb_id= l.id and c.zhilb_id = l.zhilb_id\n")
//				.append("and l.huaysj = ")
//				.append(DateUtil.FormatOracleDate(getRiq()))
//				.append("\n")
//				.append("and l.shenhzt>-1 \n")
//				.append("and z.zhuanmlb_id = \n")
//				.append(
//						"(select id from zhuanmlb where jib = (select nvl(max(jib),0) from zhuanmlb))");
		String s="";//����ѡ��Ĳ��Ǹ��糧ʱ���������� ��Ϊ��ѯ����  ������ ���ӵ糧ʱ  ȫ����ѯ
		if(!this.hasDianc(this.getTreeid_dc())){
			s=" and f.diancxxb_id="+this.getTreeid_dc()+" \n";
		}
		sb
		.append(
				"select distinct l.id,z.bianm from zhuanmb z,zhillsb l,caiyb c,fahb f \n")
		.append(
				"where z.zhillsb_id= l.id and c.zhilb_id = l.zhilb_id \n")
		.append(" and l.zhilb_id = f.zhilb_id ").append(" \n")
		.append(s)
		.append("and l.huaysj = ")
		.append(DateUtil.FormatOracleDate(getRiq()))
		.append("\n")
		.append("and l.shenhzt>-1 \n")
		.append("and z.zhuanmlb_id = \n")
		.append(
				"(select id from zhuanmlb where jib = (select nvl(max(jib),0) from zhuanmlb))");
		
		
		setBianmModel(new IDropDownModel(sb.toString(), "��ѡ��"));
	}

	// ����������
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
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
}
