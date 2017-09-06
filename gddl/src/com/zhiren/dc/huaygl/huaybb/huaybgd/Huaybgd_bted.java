package com.zhiren.dc.huaygl.huaybb.huaybgd;

/*
 * �޸��ˣ�ww
 * �޸�ʱ�䣺 2009-10-25
 * �޸����ݣ� �޸Ļ��鱨�浥��ʽ�������ʱ�䡢������
 */
/*
 * �޸��ˣ�ww
 * �޸�ʱ�䣺 2009-11-18
 * �޸����ݣ� ���鱨�浥��Ϊ������˺���ʾ
 */
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

public class Huaybgd_bted extends BasePage {

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
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		boolean isChes = MainGlobal.getXitxx_item("����", "���鱨�浥��ʾ����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd
				.append("select decode(y.caiysj,null,' ',TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD')) AS CAIYRQ,GetCaiyry(y.id) as caiyry,to_char(y.zhiysj,'yyyy-mm-dd') as zhiyrq,getzhiyry(y.id) as zhiyry, TO_CHAR(Z.HUAYSJ, 'YYYY-MM-DD') AS HUAYRQ,round_new(z.mt,1) as MT,round_new(z.mad,2) as MAD,round_new(z.fcad, 2) as FCAD,round_new(z.aad,2)as AAD,round_new(z.aar,2) as AAR,\n");
		sqlHuaybgd
				.append("round_new(z.ad,2) as AD,round_new(z.vad,2) as VAD,round_new(z.vdaf,2) as VDAF,round_new(z.stad,2) as STAD,round_new(z.std,2) as STD,\n");
		sqlHuaybgd
				.append("ROUND_NEW((100 - Z.MT) * Z.STAD / (100 - Z.MAD), 2) AS STAR,round_new(z.had,2) as HAD,round_new(z.har,2) as HAR,round_new(z.qbad,2) as QBAD,round_new(z.qgrd,2) as QGRD,round_new(z.qgrad,2) as QGRAD,\n");
		sqlHuaybgd
				.append("round_new(z.qgrad_daf,2) as GANZWHJGWRZ,round_new(z.qnet_ar,2) as QNETAR,round_new(round_new(z.qnet_ar,2)* 7000/29.271,0) as FRL,decode(z.huayy,null,' ',z.huayy) as huayy,'' as shenhry,\n");
		sqlHuaybgd
				.append("'"
						+ getBianmValue().getValue()
						+ "' as bianh,a.meikdwmc,a.chez,a.pinz,a.cheph,a.ches,a.meil,z.beiz ,z.huayy, nvl(z.shenhry,' ') as shenhryyj ,nvl(z.shenhryej,' ') as shenhryej from zhillsb z,yangpdhb y,\n");
		sqlHuaybgd.append("(select distinct m.mingc as meikdwmc,\n");
		sqlHuaybgd.append("cz.mingc as chez,\n");
		sqlHuaybgd.append("p.mingc as pinz,\n");
		sqlHuaybgd.append(" f.zhilb_id as zhilb_id,\n");
		sqlHuaybgd
				.append("round_new(sum(f.maoz) - sum(f.piz) -sum(f.zongkd), 2) as meil,\n");
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
		sqlHuaybgd.append("and z.shenhzt>=7");
		sqlHuaybgd.append("and z.id = " + getBianmValue().getId());
		ResultSet rs = con.getResultSet(sqlHuaybgd);
		String lury = "";
		String yisry = "";
		String ersry = "";
		String[][] ArrHeader = new String[14][6];
		try {
			if (rs.next()) {
				lury = rs.getString("HUAYY");
				yisry = rs.getString("shenhryyj");
				ersry = rs.getString("shenhryej");
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
				ArrHeader[0] = new String[] { "������", rs.getString("BIANH"), "���", rs.getString("MEIKDWMC"), "ú��(t)",  rs.getString("MEIL")};
				
				ArrHeader[1] = new String[] { "��������", rs.getString("HUAYRQ"), rs.getString("CHEZ"), rs.getString("PINZ"),  "����", rs.getString("ches")};
				
				ArrHeader[2] = new String[] { "��������", rs.getString("CAIYRQ"), "������Ա", rs.getString("caiyry"), rs.getString("caiyry"), rs.getString("caiyry")};
				
				ArrHeader[3] = new String[] { "��������", rs.getString("zhiyrq"), "������Ա", rs.getString("zhiyry"), rs.getString("zhiyry"), rs.getString("zhiyry")};

				ArrHeader[4] = new String[] { "ȫˮ��Mt(%)", "ȫˮ��Mt(%)", rs.getString("MT"), "�����������Had(%)", "�����������Had(%)", rs.getString("HAD")};
				
				ArrHeader[5] = new String[] { "���������ˮ��Mad(%)", "���������ˮ��Mad(%)", rs.getString("MAD"), "����������̶�̼FCad(%)", "����������̶�̼FCad(%)", (100-rs.getDouble("MT")-rs.getDouble("AAD")-rs.getDouble("VAD")) + ""};
				
				ArrHeader[6] = new String[] { "����������ҷ�Aad(%)", "����������ҷ�Aad(%)", rs.getString("AAD"), 
						"�յ����̶�̼FCar(%)", "�յ����̶�̼FCar(%)", (100-rs.getDouble("MT")-rs.getDouble("AAD")-rs.getDouble("VAD"))*(100-rs.getDouble("MT"))/(100-rs.getDouble("MAD")) + ""};
				
				ArrHeader[7] = new String[] { "�յ����ҷ�Aar(%)", "�յ����ҷ�Aar(%)", "" + rs.getDouble("AAR") + "",
						"��Ͳ��ֵQb,ad(MJ/kg)","��Ͳ��ֵQb,ad(MJ/kg)","" + rs.getDouble("QBAD") + ""};
				
				ArrHeader[8] = new String[] { "������ҷ�Ad(%)", "������ҷ�Ad(%)", "" + rs.getDouble("AD") + "",
						"�����������λ��ֵQgr,ad(MJ/kg)", "�����������λ��ֵQgr,ad(MJ/kg)","" + rs.getDouble("QGRAD") + ""};
				
				ArrHeader[9] = new String[] { "����������ӷ���Vad(%)", "����������ӷ���Vad(%)","" + rs.getDouble("VAD") + "", 
						"�������λ��ֵQgr,d(MJ/kg)", "�������λ��ֵQgr,d(MJ/kg)","" + rs.getDouble("QGRD")};
				
				ArrHeader[10] = new String[] { "�����޻һ��ӷ���Vdaf(%)", "�����޻һ��ӷ���Vdaf(%)","" + rs.getDouble("VDAF") + "",
						"�����޻һ���λ��ֵQgr,daf(MJ/kg)", "�����޻һ���λ��ֵQgr,daf(MJ/kg)","" + rs.getDouble("GANZWHJGWRZ") + ""};
				
				ArrHeader[11] = new String[] {"���������ȫ��St,ad(%)", "���������ȫ��St,ad(%)","" + rs.getDouble("STAD") + "",
						"�յ�����λ��ֵQnet,ar(MJ/kg)", "�յ�����λ��ֵQnet,ar(MJ/kg)","" + rs.getDouble("QNETAR") + ""};
				
				ArrHeader[12] = new String[] { "�����ȫ��St,d(%)", "�����ȫ��St,d(%)", "" + rs.getDouble("STD") + "", 
						"�յ�����λ��ֵQnet,ar(kcal/kg)", "�յ�����λ��ֵQnet,ar(kcal/kg)","" + num + ""};
				
				ArrHeader[13] = new String[] {"��ע","","","","",""};
				
				if (!rs.getString("CHEZ").equals("��")) {
					cheph = rs.getString("CHEPH");
					String[] list = cheph.split(",");				
					for (int i = 1; i <= list.length; i++) {
						if (i % 8 == 0) {
							buffer.append(list[i - 1] + "<br>&nbsp;&nbsp;");
						} else {
							if (i==1) {
								buffer.append("&nbsp;&nbsp;" + list[i - 1] + ",");
							} else {
								buffer.append(list[i - 1] + ",");
							}
							
						}
					}
					cheph = buffer.toString().substring(0, buffer.length() - 1);				
				}
				if (cheph!=null && !"".equals(cheph)) {	
					    ArrHeader[13][1] = cheph;
						ArrHeader[13][2] = cheph;
						ArrHeader[13][3] = cheph;
						ArrHeader[13][4] = cheph;
						ArrHeader[13][5] = cheph;						
				}
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 120, 105, 105, 165, 105, 105 };

		rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		rt.footer.setRowHeight(12, 100);
		rt.footer.setCellValue(1, 1, "��ӡ����:" + str);
		rt.footer.setCellAlign(1, 1, -1);
		rt.footer.mergeCell(1, 1, 1, 2);
//		rt.setDefautlFooter(1, 2, "��ӡ����:" + str, -1);
//		rt.setDefautlFooter(1, 2, "�����ˣ�", -1);
//		rt.setDefautlFooter(3, 2, "��ˣ�",-1 );
//		rt.setDefautlFooter(5, 2, "����Ա��",-1);
		rt.setDefautlFooter(1, 2, "������Ա��"+ersry , -1);
		rt.setDefautlFooter(3, 2, "һ����Ա��"+yisry,-1 );
		rt.setDefautlFooter(5, 2, "����Ա��"+lury,-1);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 12);
		rt.setBody(new Table(14, 6));
		rt.body.setFontSize(12);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// ��ͷ����
		
		for (int i = 1; i < 14; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}

		rt.body.setCellValue(5, 3, rt.body.format(rt.body.getCellValue(5, 3), "0.0"));
		rt.body.setCellValue(6, 6, rt.body.format(rt.body.getCellValue(6, 6), "0.00"));
		rt.body.setCellValue(7, 6, rt.body.format(rt.body.getCellValue(7, 6), "0.00"));

		for (int i = 5; i <= 13; i++) {
			rt.body.setCellValue(i, 6, rt.body.format(rt.body
					.getCellValue(i, 6), "0.00"));
			if (i==5) {
			//ȫˮ
				rt.body.setCellValue(i, 3, rt.body.format(rt.body
						.getCellValue(i, 3), "0.0"));
			} else {
				rt.body.setCellValue(i, 3, rt.body.format(rt.body
						.getCellValue(i, 3), "0.00"));
			}
			//����
			if (i==13) {
				rt.body.setCellValue(i, 6, rt.body.format(rt.body
						.getCellValue(i, 6), "0"));
			}
		}
		
		rt.body.setCells(2, 1, 13, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCellAlign(14, 1, Table.ALIGN_CENTER);
		rt.body.merge(5, 1, 13, 2);
		rt.body.merge(5, 4, 13, 5);
		rt.body.merge(3,4,3,6);
		rt.body.merge(4,4,4,6);
		rt.body.merge(14, 2, 14, 6);
		
		rt.body.ShowZero = false;

		rt.body.setFontSize(12);
		
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(43);
		rt.body.setRowHeight(14, 43*3);
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
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
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
		sb
				.append(
						"select l.id,z.bianm from zhuanmb z,zhillsb l,caiyb c\n")
				.append(
						"where z.zhillsb_id= l.id and c.zhilb_id = l.zhilb_id\n")
				.append("and l.huaysj = ")
				.append(DateUtil.FormatOracleDate(getRiq()))
				.append("\n")
				.append("and l.shenhzt>=7 \n")
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
