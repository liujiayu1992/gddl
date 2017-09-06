package com.zhiren.dc.huaygl.huaybl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * �޸�ʱ�䣺2009-09-18
 * �޸��ˣ�  ww
 * �޸����ݣ�
 * 			1 ���ӳ����ж�
 * 			�ڻ�ȡԪ�ط���ʱû�м��볧����жϣ��޷���ȡԪ�ط�����Ŀ��ֵ
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '�ֳ�ͬ����ֵ��ͬ',
 '��',
 '',
 '����',
 1,
 'ʹ��'
 )
 
 *          2 ��Ӳ���ʱ��,Ĭ�ϲ���ʾ
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '����¼����ʾ��������',
 '��',
 '',
 '����',
 1,
 'ʹ��'
 )
 */

/*����:���ܱ�
 * ʱ��:2009-10-26 10:33:14
 * �޸�����:ˢ�¼����������ڵ��ж�,ֻ�������糧�����ַ���,
 *         ��ʱ�������糧������
 * 
 * 
 * 
 */

/* �޸�ʱ��:2010-01-25 
 * ��Ա��liht
 * �޸�����:����ʱ��¼��ʱ��ԭ�������ջ����Ͼ�ȷ��ʱ����
 * 
 * 
 * 
 */
public class Ruchylr extends BasePage implements PageValidateListener {

	private String CustomSetKey = "Ruchylr";

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

	
	private String zhiy1; // ��������1
	
	public String getZhiy1() {
		return zhiy1;
	}

	public void setZhiy1(String zhiy1) {
		this.zhiy1 = zhiy1;
	}
	
	private String zhiy2; // ��������2
	
	public String getZhiy2() {
		return zhiy2;
	}

	public void setZhiy2(String zhiy2) {
		this.zhiy2 = zhiy2;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getKuangm() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setKuangm(String kuangm) {
		((Visit) this.getPage().getVisit()).setString1(kuangm);
	}

	public String getBianh() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setBianh(String bianh) {
		((Visit) this.getPage().getVisit()).setString2(bianh);
	}

	public String getShul() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setShul(String shul) {
		((Visit) this.getPage().getVisit()).setString3(shul);
	}

	public String getHuaysj() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setHuaysj(String huaysj) {
		((Visit) this.getPage().getVisit()).setString4(huaysj);
	}

	// �����ֺ���Χ����ȡ�� Mt, Mad��Aad,Vad,Std,Qbad�ķ�Χ
	public ResultSetList getmaxmin(JDBCcon con, long diancxxb_id) {
		ResultSetList shuzhirsl;
		String sql = "select shangx,xiax,mingc from shuzhlfwb "
				+ "where leib = '����' and beiz='����¼��'";
		shuzhirsl = con.getResultSetList(sql);
		return shuzhirsl;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		// ���Ƿ�ɱ༭
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// ���Ƿ�ɱ༭
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// ��Ԫ�ط���
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// ��Ԫ�ط���
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}
		// ��ֵ����Χ
		ResultSetList szhlfw = getmaxmin(con, visit.getDiancxxb_id());
		// ת������ж�Ӧ��������ID��
		String zhuanmlbid = "";
		sql = "select id from zhuanmlb where mingc = '�������'";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			zhuanmlbid = rsl.getString("id");
		}
		rsl.close();
		if ("".equals(zhuanmlbid)) {
			WriteLog.writeErrorLog(ErrorMessage.ZhuanmlbNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.ZhuanmlbNotFound);
			return;
		}

		// �糧Treeˢ������
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			try {
				ResultSet rsss = con
						.getResultSet("select id from diancxxb where fuid="
								+ getTreeid());
				if (rsss.next()) {
					str = "and dc.fuid=" + getTreeid();
				} else {
					str = "and dc.id = " + getTreeid();
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}

		}

		// �Ƿ���ʾ����ʱ��
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || "".equals(riqTiaoj)) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		boolean isShowRq = false;
		String strTemp = "";
		isShowRq = "��".equals(MainGlobal.getXitxx_item("����", "����¼����ʾ��������",
				visit.getDiancxxb_id() + "", "��"));
		if (isShowRq) {
			strTemp = " AND (SELECT caiyrq FROM caiyb WHERE zhilb_id=l.zhilb_id)=to_date('"
					+ riqTiaoj + "','yyyy-mm-dd')\n";
		}

		// �Ƿ�Ϊ���Ƿ���
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���", visit
				.getDiancxxb_id()
				+ "", "��"));
		//�Ƿ���������
		boolean isXuanw = false;
		isXuanw = "��".equals(MainGlobal.getXitxx_item("����", "�볧����¼���Ƿ�Ϊ��������", visit
				.getDiancxxb_id()
				+ "", "��"));
		
		String sql_h = "";
		if (!isYc) {
			sql_h = " to_char(l." + sign_H + ",90.99) "+sign_H+",\n";
		}
		
		if(isXuanw){//���������볧¼��
			
			sql = "select l.id,\n"
				+"        zy.zhiyrq,"
				+ "       to_char(z.bianm) as huaybh,\n"
				+ "       nvl(l.huaysj,sysdate) huaysj,\n"
				+ "       to_char(l.mt,90.9) mt,\n"
				+ "       l.mad,\n"
				+ "       l.aad,\n"
				+ "       l.vad,\n"
				+ "       l."
				+ sign_S
				+ ","
				+ sql_h
				+ "       l.qbad,\n"
				// + " l." + sign_H + ",\n" + " l.qbad,\n"
				+ "       l.t1,\n" + "       l.t2,\n" + "       l.t3,\n"
				+ "       l.t4,\n" + "       huayy,\n" + "       l.lury,\n"
				+ "       l.beiz,\n" + "       l.huaylb\n"
				+ "from zhuanmb z, zhillsb l,zhiyryb zy,\n"
				+ "       (select distinct f.zhilb_id\n"
				+ "        from fahb f,diancxxb dc\n"
				+ "        where f.diancxxb_id = dc.id\n" + str + ") fh\n"
				+ "where z.zhillsb_id = l.id and zhuanmlb_id = "
				+ zhuanmlbid + "\n"
				+ "      and l.zhilb_id = fh.zhilb_id\n"
				+ "      and (l.shenhzt = 0 or l.shenhzt = 2)\n" 
				+"        and  zy.zhillsb_id=l.id  and zy.zhiyrq>=to_date('"+this.getZhiy1()+"','yyyy-mm-dd') and zy.zhiyrq<=to_date('"+this.getZhiy2()+"','yyyy-mm-dd') \n"
				+ "order by zy.zhiyrq,l.id, l.huaylb";
			
		}else{
			sql = "select l.id,\n"
				+ "       to_char(z.bianm) as huaybh,\n"
				+ "       nvl(l.huaysj,sysdate) huaysj,\n"
				+ "       to_char(l.mt,90.9) mt,\n"
				+ "       to_char(l.mad,90.99) mad,\n"
				+ "       to_char(l.aad,90.99) aad,\n"
				+ "       to_char(l.vad,90.99) vad,\n"
				+ "       to_char(l."
				+ sign_S
				+ ",90.99) "+sign_S+","
				+ sql_h
				+ "       to_char(round_new(l.qbad,2),90.99) qbad,\n"
				// + " l." + sign_H + ",\n" + " l.qbad,\n"
				+ "       l.t1,\n" + "       l.t2,\n" + "       l.t3,\n"
				+ "       l.t4,\n" + "       huayy,\n" + "       l.lury,\n"
				+ "       l.beiz,\n" + "       l.huaylb\n"
				+ "from zhuanmb z, zhillsb l,\n"
				+ "       (select distinct f.zhilb_id\n"
				+ "        from fahb f,diancxxb dc\n"
				+ "        where f.diancxxb_id = dc.id\n" + str + ") fh\n"
				+ "where z.zhillsb_id = l.id and zhuanmlb_id = "
				+ zhuanmlbid + "\n"
				+ "      and l.zhilb_id = fh.zhilb_id\n"
				+ "      and (l.shenhzt = 0 or l.shenhzt = 2)\n" + strTemp
				+ "order by l.id, l.huaylb";
		}
		
			
		
		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomSetKey);
		// //

		// //���ñ��������ڱ���
		egu.setTableName("zhillsb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth("bodyWidth");
		// /������ʾ������
		
		if(isXuanw){
			egu.getColumn("zhiyrq").setHeader("��������");
			egu.getColumn("zhiyrq").setEditor(null);
		}
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("mt").setHeader("ȫˮ��Mt(%)");
		egu.getColumn("mad").setHeader("ˮ��Mad(%)");
		egu.getColumn("aad").setHeader("�ҷ�Aad(%)");
		egu.getColumn("vad").setHeader("�ӷ���Vad(%)");
		egu.getColumn("qbad").setHeader("��Ͳ����Qbad(Mj/kg)");
		egu.getColumn(sign_S).setHeader("���" + sign_S + "(%)");
		if (!isYc) {
			egu.getColumn(sign_H).setHeader("��" + sign_H + "(%)");
		}
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setEditor(null);

		egu.getColumn("huaysj").setColtype(GridColumn.ColType_default);
		egu.getColumn("huaysj").editor.setAllowBlank(false);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("mt").editor.setMaxValue("100");
		egu.getColumn("mt").editor.setMinValue("1");
		egu.getColumn("mt").setColtype(GridColumn.ColType_default);
		egu.getColumn("mt").editor.setAllowBlank(false);

		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mad").editor.setMaxValue("100");
		egu.getColumn("mad").editor.setMinValue("0");
		egu.getColumn("mad").setColtype(GridColumn.ColType_default);
		egu.getColumn("mad").editor.setAllowBlank(false);

		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad").editor.setMaxValue("100");
		egu.getColumn("aad").editor.setMinValue("1");
		egu.getColumn("aad").setColtype(GridColumn.ColType_default);
		egu.getColumn("aad").editor.setAllowBlank(false);

		egu.getColumn("vad").setWidth(80);
		egu.getColumn("vad").editor.setMaxValue("100");
		egu.getColumn("vad").editor.setMinValue("1");
		egu.getColumn("vad").setColtype(GridColumn.ColType_default);
		egu.getColumn("vad").editor.setAllowBlank(false);

		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("qbad").editor.setMaxValue("100");
		egu.getColumn("qbad").editor.setMinValue("1");
		egu.getColumn("qbad").setColtype(GridColumn.ColType_default);
		egu.getColumn("qbad").editor.setAllowBlank(false);

		egu.getColumn(sign_S).setWidth(80);
		egu.getColumn(sign_S).editor.setMaxValue("100");
		egu.getColumn(sign_S).editor.setMinValue("0");
		egu.getColumn(sign_S).setColtype(GridColumn.ColType_default);
		egu.getColumn(sign_S).setHidden(!editable_S);
		if (editable_S) {
			egu.getColumn(sign_S).editor.setAllowBlank(false);
			if (isYc) {
				egu.getColumn(sign_S).editor.setListeners(getStr(9));
			}
		}

		if (!isYc) {
			egu.getColumn(sign_H).setWidth(80);
			egu.getColumn(sign_H).editor.setMaxValue("100");
			egu.getColumn(sign_H).editor.setMinValue("0");
			egu.getColumn(sign_H).setColtype(GridColumn.ColType_default);
			egu.getColumn(sign_H).setHidden(!editable_H);
			if (editable_H) {
				egu.getColumn(sign_H).editor.setAllowBlank(false);
			}
		}

		// ���û����õķ�Χ���ǳ���Ĭ��
		while (szhlfw.next()) {
			GridColumn gc = egu.getColumn(szhlfw.getString("mingc"));
			if (gc != null) {
				gc.editor.setMaxValue(szhlfw.getString("shangx"));
				gc.editor.setMinValue(szhlfw.getString("xiax"));
			}
		}
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t1").setDefaultValue("20");
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t2").setDefaultValue("20");
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t3").setDefaultValue("20");
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("t4").setDefaultValue("20");
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setWidth(80);

		if (isYc) {
			egu.getColumn("mt").editor.setListeners(getStr(5));// ��������ƹ��
			egu.getColumn("mad").editor.setListeners(getStr(6));
			egu.getColumn("aad").editor.setListeners(getStr(7));
			egu.getColumn("vad").editor.setListeners(getStr(8));
			egu.getColumn("qbad").editor.setListeners(getStr(10));
			egu.getColumn("t1").editor.setListeners(getStr(11));
			egu.getColumn("t2").editor.setListeners(getStr(12));
			egu.getColumn("t3").editor.setListeners(getStr(13));
			egu.getColumn("t4").editor.setListeners(getStr(14));
			egu.getColumn("huayy").editor.setListeners(getStr(15));
			egu.getColumn("beiz").editor.setListeners(getStr(17));
		}

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(1000);

		// �жϻ���ʱ��¼���Ƿ�ȷ��ʱ��
		String strXitszzt = MainGlobal.getXitxx_item("����",
				Locale.huaysjlrsfjqdsf_xitxx, String.valueOf(getTreeid()), "��");
		if (strXitszzt.equals("��")) {
			DatetimeField huaysj = new DatetimeField();
			huaysj.setFormat("Y-m-d H:i");
			huaysj.setMenu("new DatetimeMenu()");
			egu.getColumn("huaysj").setRenderer(GridColumn.Renderer_DateTime);
			egu.getColumn("huaysj").setEditor(huaysj);
		}

		// Toolbar tb1 = new Toolbar("tbdiv");
		// egu.setGridSelModel(3);
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// �Ƿ���ʾ��������
		if (isShowRq) {
			egu.addTbarText("��������:");
			DateField df = new DateField();
			df.setValue(this.getRiq());
			df.Binding("Caiyrq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
			egu.addToolbarItem(df.getScript());
			egu.addTbarText("-");
		}
		
		if(isXuanw){
			egu.addTbarText("��������:");
			DateField zhiy1 = new DateField();
			zhiy1.setValue(this.getZhiy1());
			zhiy1.Binding("Zhiy1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
			egu.addToolbarItem(zhiy1.getScript());
			egu.addTbarText("-");
			egu.addTbarText(" �� ");
			DateField zhiy2 = new DateField();
			zhiy2.setValue(this.getZhiy2());
			zhiy2.Binding("Zhiy2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
			egu.addToolbarItem(zhiy2.getScript());
			egu.addTbarText("-");
			
		}

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("�ύ", GridButton.ButtonType_SubmitSel,
				"SaveButton", null, null);
		// �����ж�
		String sqll = " select id from xitxxb x where x.mingc='�����볧���鱣��'";
		ResultSetList rsll = con.getResultSetList(sqll);
		if (rsll.getRows() >= 1) {

			GridButton baocun = new GridButton("����", getfunction("SavebButton",
					sign_S));
			baocun.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(baocun);
		}
		// egu.addToolbarButton(GridButton.ButtonType_Save,
		// getfunction("SavebButton"));
		GridButton Huit = new GridButton("���",
				getfunction("HuitButton", sign_S));
		Huit.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(Huit);
		// �Ƿ���ʾ���ϰ�ť
		String flag = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ���ϰ�ť", String.valueOf(visit.getDiancxxb_id()), "��");
		if (flag.equals("��")) {
			GridButton zuofei = new GridButton("����", getfunction("ZuofButton",sign_S));
			zuofei.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(zuofei);
		}
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		// �����Ƿ�ʹ�ù�������
		boolean isLiuc = false;
		isLiuc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�ʹ�ù�������", visit
				.getDiancxxb_id()
				+ "", "��"));
		
		if (isLiuc) {
			egu.addTbarText("��������:");
			ComboBox comb1=new ComboBox();
			comb1.setTransform("LiucmcDropDown");
			comb1.setId("LiucmcDropDown");
			comb1.setLazyRender(true);//��̬��
			comb1.setWidth(100);
			egu.addToolbarItem(comb1.getScript());
		}
		
		// ��֤�Ƿ����� �������
		String sqltmp = "select * from xitxxb where mingc='����ֵ¼����ʾ��Ʒ���' and zhi='����'";
		ResultSetList rsl2 = con.getResultSetList(sqltmp);
		if (rsl2.next()) {
			egu.getColumn("huaylb").setHidden(true);
		}

		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;});");
		
		ResultSetList rsl3 = con.getResultSetList("select * from xitxxb where mingc='��������¼���Ƿ���ʾ' and zhi='��' and leib='����' and zhuangt=1 ");
		if(rsl3.next()){
			StringBuffer sb = new StringBuffer();
			sb.append("gridDiv_grid.on('afteredit',function(e){\n");
			sb.append("if(e.field=='QBAD'){if(e.record.get('QBAD')>25||e.record.get('QBAD')<10){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ�ϵ�Ͳ��ֵ�Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='MAD'){if(e.record.get('MAD')>20||e.record.get('MAD')<0.6){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ��ˮ���Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='MT'){if(e.record.get('MT')>20||e.record.get('MT')<3.5){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ��ȫˮ���Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='AAD'){if(e.record.get('AAD')>43||e.record.get('AAD')<15){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ�ϻҷ��Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='VAD'){if(e.record.get('VAD')>50||e.record.get('VAD')<10){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ�ϻӷ����Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='STAD'){if(e.record.get('STAD')>10||e.record.get('STAD')<0){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ������Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='HAD'){if(e.record.get('HAD')>10||e.record.get('HAD')<0){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ����ֵ�Ƿ���ȷ��');}}\n");
			sb.append("});");
			egu.addOtherScript(sb.toString());
		}
		setExtGrid(egu);
		rsl2.close();
		con.Close();
	}

	public String getStr(int col) {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String Str = "";
		// ���Ƿ�ɱ༭
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		Str = "specialkey:function(own,e){\n" + "			if(row>0){\n"
				+ "				if(e.getKey()==e.UP){\n"
				+ "					gridDiv_grid.startEditing(row-1, " + col + ");\n"
				+ "					row = row-1;\n" + "				}\n" + "			}\n"
				+ "			if(row+1 < gridDiv_grid.getStore().getCount()){\n"
				+ "				if(e.getKey()==e.DOWN){\n"
				+ "					gridDiv_grid.startEditing(row+1, " + col + ");\n"
				+ "					row = row+1;\n" + "				}\n" + "			}\n";
		if (editable_S) {
			Str += "		if(e.getKey()==e.LEFT){\n" + "			if(" + col + ">5&&"
					+ col + "<=10){\n" + "				gridDiv_grid.startEditing(row, "
					+ col + "-1);\n" + "			} else if (" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ "			} else if (" + col + "==17){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n" + "			}\n"
					+ "		}\n" + "		if(e.getKey()==e.RIGHT){\n" + "			if(" + col
					+ ">=5&&" + col + "<10){\n"
					+ "				gridDiv_grid.startEditing(row, " + col + "+1);\n"
					+ "			} else if (" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n"
					+ "			} else if (" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 17);\n" + "			}\n"
					+ "		}\n" + "	  }\n";
		} else {
			Str += "		if(e.getKey()==e.LEFT){\n" + "			if(" + col + ">5&&"
					+ col + "<=8){\n" + "				gridDiv_grid.startEditing(row, "
					+ col + "-1);\n" + "			} else if(" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 8);\n"
					+ " 			} else if(" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ " 			} else if(" + col + "==17){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n" + "			}\n"
					+ "		}\n" + "		if(e.getKey()==e.RIGHT){\n" + "			if(" + col
					+ ">=5&&" + col + "<8){\n"
					+ "				gridDiv_grid.startEditing(row, " + col + "+1);\n"
					+ "			} else if(" + col + "==8){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ "	 		} else if(" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n"
					+ "	 		} else if(" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 17);\n" + "			}\n"
					+ "		}\n" + "	  }\n";
		}
		return Str;
	}

	public String getfunction(String binder, String s) {
		String handler = "function(){\n"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+ '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+ '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' + Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ '<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<"
				+ s.toUpperCase()
				+ " update=\"true\">' + Mrcd[i].get('"
				+ s.toUpperCase()
				+ "')+ '</"
				+ s.toUpperCase()
				+ ">'+ '<QBAD update=\"true\">' + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<T1 update=\"true\">' + Mrcd[i].get('T1')+ '</T1>'+ '<T2 update=\"true\">' + Mrcd[i].get('T2')+ '</T2>'+ '<T3 update=\"true\">' + Mrcd[i].get('T3')+ '</T3>'+ '<T4 update=\"true\">' + Mrcd[i].get('T4')+ '</T4>'+ '<HUAYY update=\"true)\">' + Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+ '<LURY update=\"true\">' + Mrcd[i].get('LURY').replace('<','&lt;').replace('>','&gt;')+ '</LURY>'+ '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>'+ '<HUAYLB update=\"true\">' + Mrcd[i].get('HUAYLB').replace('<','&lt;').replace('>','&gt;')+ '</HUAYLB>' + '</result>' ; \n"
				+ "}\n"
				+ "if(gridDiv_history==''){ \n"
				+ "Ext.MessageBox.alert('��ʾ��Ϣ','û��ѡ��������Ϣ');\n"
				+ "}else{\n"
				+ "    var Cobj = document.getElementById('CHANGE');\n"
				+ "    Cobj.value = '<result>'+gridDiv_history+'</result>';\n"
				+ "    document.getElementById('"
				+ binder
				+ "').click();\n"
				+ "    Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"
				+ "}\n" + "}";
		return handler;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
	}

	private void Zuof() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save3(getChange(), visit);
	}

	private int Judgment(String value) {
		int v = 0;
		if (value.equals(null) || value.equals("")) {
			v = 0;
		} else {
			v = Integer.parseInt(value);
		}
		return v;
	}

	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		
        // �����Ƿ�ʹ�ù�������
		boolean isLiuc = false;
		isLiuc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�ʹ�ù�������", visit
				.getDiancxxb_id()
				+ "", "��"));
		
		
//		 ����ӿ���,
		//����ֵ¼���ύʱ�Ƿ���»���ӿڻ���ԭʼ���浥�����״̬
		boolean isUpdateZt = false;
		isUpdateZt = "��".equals(MainGlobal.getXitxx_item("����", "����ֵ¼���ύʱ�Ƿ���»���ӿڻ���ԭʼ���浥�����״̬", visit
				.getDiancxxb_id()
				+ "", "��"));
		
		String table_name = "zhillsb";
		
		// �Ƿ�Ϊ���Ƿ���
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���", visit
				.getDiancxxb_id()
				+ "", "��"));
		// ���Ƿ�ɱ༭
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// ���Ƿ�ɱ༭
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// ��Ԫ�ط���
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// ��Ԫ�ط���
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}

		// �жϵ�½��Ա�Ƿ���ǻ���Ա,�����,���Զ���huayy�ֶδ����½��Ա����
		boolean xiansztq = false;
		String sql2 = "select zhi from xitxxb where mingc = '�볧����¼��Ĭ�ϵ�¼��Ա���ǻ���Ա' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql2);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		rsl.close();

		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		long zhillsbid = 0;
		String msg = "";
		while (mdrsl.next()) {
			sql.append("begin\n ");
			zhillsbid = Long.parseLong(mdrsl.getString("ID"));
			double H = 0;
			if (!isYc) {
				H = mdrsl.getDouble(sign_H);
			}
			double S = mdrsl.getDouble(sign_S);
			double Mt = mdrsl.getDouble("mt");
			double Mad = mdrsl.getDouble("mad");
			double Aad = mdrsl.getDouble("aad");
			double Vad = mdrsl.getDouble("vad");
			double Qbad = mdrsl.getDouble("qbad");
			double Vdaf = 0;
			boolean failed = false;

			/*
			 * ���ߣ�ww ʱ�䣺2009-09-18 �������ֳ���ͬ��Ĺ�Ӧ����ֵ��ͬ
			 */
			boolean isNotValue = "��".equals(MainGlobal.getXitxx_item("����",
					"�ֳ�ͬ����ֵ��ͬ", visit.getDiancxxb_id() + "", "��"));
			boolean blnFencb = false;
			blnFencb = ((Visit) this.getPage().getVisit()).isFencb();
			
//			��ͬ���������п��̶�hadֵ��ֻ�Ǹ��ڲ�һ��
			String xit_H = MainGlobal.getXitxx_item("����", "����hadֵ", getTreeid(), "");

			if (isNotValue && blnFencb) {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						if(!"".equals(xit_H)){
							H = Double.parseDouble(xit_H);
						}else{
							H = Compute.getYuansValue(con, zhillsbid, visit
									.getDiancxxb_id(), sign_H, mdrsl
									.getDouble("vad"), blnFencb, getTreeid());
						}
						if (H == -1) {
							msg += "����:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"),
							blnFencb, getTreeid());
					if (S == -1) {
						msg += "����:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
						failed = true;
					}
				}

			} else {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl
								.getDouble("vad"));
						if (H == -1) {
							msg += "����:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"));
					if (S == -1) {
						msg += "����:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
						failed = true;
					}
				}
			}

			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update zhillsb set ");
			sql.append("huaysj").append("=").append(
					"to_date('" + mdrsl.getString("Huaysj")
							+ "','yyyy-mm-dd'),");
			sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad, sign_H, H,
					sign_S, S, sacle));
			sql.append("t1").append("=" + Judgment(mdrsl.getString("t1")) + ",");
			sql.append("t2").append("=" + Judgment(mdrsl.getString("t2")) + ",");
			sql.append("t3").append("=" + Judgment(mdrsl.getString("t3")) + ",");
			sql.append("t4").append("=" + Judgment(mdrsl.getString("t4")) + ",");
			if (!failed) {
				sql.append("shenhzt").append("=3,");
				if (isLiuc) {
					long Liuc_id = MainGlobal.getProperId(getILiucmcModel(), getLiucmcValue().getValue());
					Liuc.tij(table_name, mdrsl.getLong("id"), ((Visit) getPage().getVisit()).getRenyID(), "", Liuc_id);
				}
			}
			// ���ܱ�,�жϻ���ֵ¼��ʱ,��½��Ա�Ƿ���ǻ���Ա
			if (xiansztq) {
				sql.append("huayy").append("='" + visit.getRenymc() + "',");
			} else {
				sql.append("huayy").append(
						"='" + mdrsl.getString("huayy") + "',");
			}

			sql.append("lury").append("='" + visit.getRenymc() + "',");
			sql.append("beiz").append("='" + mdrsl.getString("beiz") + "' ");
			sql.append("where id =").append(mdrsl.getString("ID")).append(" ;\n");
			
			if(isUpdateZt){
				/*����:���ܱ�
				 *����:2010-8-23
				 *����:������ӿ�ʱ,��������ݶ��Ǵӻ���������ȡ����������,�������黯��ԭʼ����,��ĳһ�������ŵ�����(�������,������ˮ)
				 *    ���ڵ糧��Ա�ڻ��������ϰѱ�����,���������ŵ�ĳ��������ϴ�������ԭʼ���浥,��ʱ�糧����Ҫ���볧����¼��ҳ��
				 *    ¼�������ŵĻ���ֵ,��¼�뻯��ֵʱ,�Զ����»���ԭʼ���浥�������ŵ����״̬.��δ���״̬�е������Ÿó������
				 */
				//���鹤ҵ������
				sql.append("update huaygyfxb   set shenhzt=1 where bianm='").append(mdrsl.getString("huaybh")).append("';\n");
				//������ֱ�
				sql.append("update huaylfb  set shenhzt=1 where bianm='").append(mdrsl.getString("huaybh")).append("';\n");
//				����������
				sql.append("update huayrlb  set shenhzt=1 where bianm='").append(mdrsl.getString("huaybh")).append("';\n");
			
			}
			
			sql.append("end;\n ");
			con.getUpdate(sql.toString());

			sql.delete(0, sql.length());
		}
		setMsg(msg);
	}

	public void Save2(String strchange, Visit visit) {
		String tableName = "zhillsb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append("huaysj").append("= null,");
			sql.append("qnet_ar").append("=0,");
			sql.append("aar").append("=0,");
			sql.append("ad").append("=0,");
			sql.append("vdaf").append("=0,");
			sql.append("mt").append("=0,");
			sql.append("stad").append("=0,");
			sql.append("aad").append("=0,");
			sql.append("mad").append("=0,");
			sql.append("qbad").append("=0,");
			sql.append("had").append("=0,");
			sql.append("vad").append("=0,");
			sql.append("fcad").append("=0,");
			sql.append("std").append("=0,");
			sql.append("qgrad").append("=0,");
			sql.append("hdaf").append("=0,");
			sql.append("qgrad_daf").append("=0,");
			sql.append("sdaf").append("=0,");
			sql.append("t1").append("=0,");
			sql.append("t2").append("=0,");
			sql.append("t3").append("=0,");
			sql.append("t4").append("=0,");
			sql.append("shenhzt").append("=0,");
			sql.append("huayy").append("=null,");
			sql.append("lury").append("=null,");
			sql.append("beiz").append("=null ");
			sql.append("where id =").append(mdrsl.getString("ID"))
					.append(";\n");
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
	}

	public void Save3(String strchange, Visit visit) {
		String tableName = "zhillsb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append("=-1 ");
			sql.append("where id =").append(mdrsl.getString("ID"))
					.append(";\n");
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ZuofChick = false;

	public void ZuofButton(IRequestCycle cycle) {
		_ZuofChick = true;
	}

	private boolean _SavebChick = false;

	public void SavebButton(IRequestCycle cycle) {
		_SavebChick = true;
	}

	public void Saveb() {

		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	public void submit(IRequestCycle cycle) {

		if (_SavebChick) {
			_SavebChick = false;
			Saveb();

			getSelectData();
		}

		if (_SaveChick) {
			_SaveChick = false;
			Save();

			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();

		}
		if (_RefurbishChick) {
			_RefurbishChick = false;

		}
		if (_ZuofChick) {
			_ZuofChick = false;
			Zuof();

		}

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

	public String gridId;

	public int pagsize;

	public int getPagSize() {
		return pagsize;
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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

	private String treeid = "";

	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	public String tbars;

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
	}

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        try{
            Visit visit = (Visit) getPage().getVisit();
            if (!visit.getActivePageName().toString().equals(
                    this.getPageName().toString())) {
                // �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                setExtGrid(null);
                setZhiy1(DateUtil.FormatDate(new Date()));
                setZhiy2(DateUtil.FormatDate(new Date()));
                //visit.setString6(DateUtil.FormatDate(new Date()));

                visit.setDropDownBean8(null);
                visit.setProSelectionModel8(null);

                getSelectData();

            }
            getSelectData();
        }catch (Exception e){
            e.printStackTrace();
        }

	}

	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null
				|| ((Visit) this.getPage().getVisit()).getString5().equals("")) {

			((Visit) this.getPage().getVisit()).setString5(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null
				&& !((Visit) this.getPage().getVisit()).getString5().equals(
						riq1)) {

			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}

//	�������� DropDownBean8
//  �������� ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		String sql = "select liucb.id, liucb.mingc from liucb, liuclbb where liucb.liuclbb_id = " +
					 "liuclbb.id and liuclbb.mingc = '����' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   �������� end
}