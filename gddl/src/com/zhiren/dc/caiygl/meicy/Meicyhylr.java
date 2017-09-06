package com.zhiren.dc.caiygl.meicy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
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
import com.zhiren.common.ext.grid.ColumnModel;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Meicyhylr extends BasePage implements PageValidateListener {

	private String CustomSetKey = "Meicyhylr";

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
		
		//�Ƿ�Ϊ���Ƿ���
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���",
				visit.getDiancxxb_id() + "", "��"));

		String sql_h = "";
		if(!isYc){
			sql_h = " l."+sign_H+",\n";
		}
		// ��ѯ����SQL
		
	
			sql = 
				"select k.id,k.huaybh, nvl(k.huaysj,sysdate) huaysj,k.mt,k.mad,k.aad,k.vad,k.stad,k.qbad,k.zhiyry,k.huayy,k.beiz\n" +
				"from meicyb k\n" + 
				"where k.shenhzt=0 \n" + 
				"order by k.caiysj,k.huaybh";

		

		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomSetKey);
		// //

		// //���ñ��������ڱ���
		egu.setTableName("meicyb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth("bodyWidth");
		// /������ʾ������
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("mt").setHeader("ȫˮ��Mt(%)");
		egu.getColumn("mad").setHeader("ˮ��Mad(%)");
		egu.getColumn("aad").setHeader("�ҷ�Aad(%)");
		egu.getColumn("vad").setHeader("�ӷ���Vad(%)");
		egu.getColumn("qbad").setHeader("��Ͳ����Qbad(Mj/kg)");
		
		egu.getColumn("zhiyry").setHeader("������Ա");
		ComboBox meik = new ComboBox();
		egu.getColumn("zhiyry").setEditor(meik);
		meik.setEditable(true);
		sql = "select id,quanc from renyxxb r where r.bum='����'";
		egu.getColumn("zhiyry").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("zhiyry").editor.setAllowBlank(true);
		egu.getColumn("zhiyry").setReturnId(false);
		egu.getColumn(sign_S).setHeader("���" + sign_S + "(%)");
		
		egu.getColumn("huayy").setHeader("����Ա");
		
		egu.getColumn("beiz").setHeader("���鱸ע");
		

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
		
		egu.getColumn("qbad").setWidth(120);
		egu.getColumn("qbad").editor.setMaxValue("100");
		egu.getColumn("qbad").editor.setMinValue("1");
		egu.getColumn("qbad").setColtype(GridColumn.ColType_default);
		egu.getColumn("qbad").editor.setAllowBlank(false);
		
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("stad").editor.setMaxValue("100");
		egu.getColumn("stad").editor.setMinValue("0");
		egu.getColumn("stad").setColtype(GridColumn.ColType_default);
		egu.getColumn("stad").setHidden(!editable_S);
		if (editable_S) {
			egu.getColumn(sign_S).editor.setAllowBlank(false);
			if(isYc){
				egu.getColumn(sign_S).editor.setListeners(getStr(9));
			}
		}
		//�����糧ȥ��
//		if(!isYc){
//			egu.getColumn(sign_H).setWidth(80);
//			egu.getColumn(sign_H).editor.setMaxValue("100");
//			egu.getColumn(sign_H).editor.setMinValue("0");
//			egu.getColumn(sign_H).setColtype(GridColumn.ColType_default);
//			egu.getColumn(sign_H).setHidden(!editable_H);
//			if (editable_H) {
//				egu.getColumn(sign_H).editor.setAllowBlank(false);
//			}
//		}
		
		// ���û����õķ�Χ���ǳ���Ĭ��
		while (szhlfw.next()) {
			GridColumn gc = egu.getColumn(szhlfw.getString("mingc"));
			if (gc != null) {
				gc.editor.setMaxValue(szhlfw.getString("shangx"));
				gc.editor.setMinValue(szhlfw.getString("xiax"));
			}
		}
		
		egu.getColumn("huayy").setWidth(80);
	
		egu.getColumn("beiz").setWidth(80);
		
		if(isYc){
			egu.getColumn("mt").editor.setListeners(getStr(5));//��������ƹ��
			egu.getColumn("mad").editor.setListeners(getStr(6));
			egu.getColumn("aad").editor.setListeners(getStr(7));
			egu.getColumn("vad").editor.setListeners(getStr(8));
			egu.getColumn("qbad").editor.setListeners(getStr(10));
			egu.getColumn("zhiyry").editor.setListeners(getStr(11));
			egu.getColumn("huayy").editor.setListeners(getStr(12));
			egu.getColumn("beiz").editor.setListeners(getStr(13));
		}
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);

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
//		if (visit.getDiancmc().equals("��������")) {
//			egu.addTbarText("��������:");
//			DateField zhiyrq = new DateField();
//			zhiyrq.Binding("Zhiyrq", "");
//			zhiyrq.setValue(this.getZhiyrq());
//			egu.addToolbarItem(zhiyrq.getScript());
//			egu.addTbarText("-");
//		}

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("�ύ", GridButton.ButtonType_SubmitSel,
				"SaveButton", null, null);
		//�����ж�
		String sqll=" select id from xitxxb x where x.mingc='�����볧���鱣��'";
		ResultSetList rsll= con.getResultSetList(sqll);
		if(rsll.getRows()>=1){
			
			//GridButton baocun = new GridButton("����", GridButton.ButtonType_Save,"SavebButton",null,null);
			GridButton baocun = new GridButton("����", getfunction("SavebButton",sign_S));
			baocun.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(baocun);
		}
//		egu.addToolbarButton(GridButton.ButtonType_Save, getfunction("SavebButton"));
		GridButton Huit = new GridButton("���", getfunction("HuitButton",sign_S));
		Huit.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(Huit);
		
			GridButton zuofei = new GridButton("����", getfunction("ZuofButton",sign_S));
			zuofei.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(zuofei);
		
		
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		
		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;});");

		setExtGrid(egu);
		
		con.Close();
	}
	
	public String getStr(int col){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String Str = "";

		Str = "specialkey:function(own,e){\n" +
				"			if(row>0){\n" +
				"				if(e.getKey()==e.UP){\n" +
				"					gridDiv_grid.startEditing(row-1, "+col+");\n" +
				"					row = row-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(row+1 < gridDiv_grid.getStore().getCount()){\n" +
				"				if(e.getKey()==e.DOWN){\n" +
				"					gridDiv_grid.startEditing(row+1, "+col+");\n" +
				"					row = row+1;\n" +
				"				}\n" +
				"			}\n";
		
			Str += 	"		if(e.getKey()==e.LEFT){\n" +
					"			if("+col+">5&&"+col+"<=13){\n" +
					"				gridDiv_grid.startEditing(row, "+col+"-1);\n" +
					"			} else if ("+col+"==5){\n" +
					"				gridDiv_grid.startEditing(row, 5);\n" +
					"			} else if ("+col+"==12){\n" +
					"				gridDiv_grid.startEditing(row, 12);\n" +
					"			}\n" +
					"		}\n" +
					"		if(e.getKey()==e.RIGHT){\n" +
					"			if("+col+">=4&&"+col+"<=12){\n" +
					"				gridDiv_grid.startEditing(row, "+col+"+1);\n" +
					"			} else if ("+col+"==13){\n" +
					"				gridDiv_grid.startEditing(row, 13);\n" +
					"			} else if ("+col+"==13){\n" +
					"				gridDiv_grid.startEditing(row, 13);\n" +
					"			}\n" +
					"		}\n" +
					"	  }\n";
		
		return Str;
	}
	public String getfunction(String binder,String s) {
		String handler = "function(){\n"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+ '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+ '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' + Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ '<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<"+s.toUpperCase()+" update=\"true\">' + Mrcd[i].get('"+s.toUpperCase()+"')+ '</"+s.toUpperCase()+">'+ '<QBAD update=\"true\">' + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<HUAYY update=\"true)\">' + Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+ '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>'+ '<ZHIYRY update=\"true\">' + Mrcd[i].get('ZHIYRY').replace('<','&lt;').replace('>','&gt;')+ '</ZHIYRY>' + '</result>' ; \n"
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
		
		
		


//		 ����ӿ���,
		//����ֵ¼���ύʱ�Ƿ���»���ӿڻ���ԭʼ���浥�����״̬
		boolean isUpdateZt = false;
		isUpdateZt = "��".equals(MainGlobal.getXitxx_item("����", "����ֵ¼���ύʱ�Ƿ���»���ӿڻ���ԭʼ���浥�����״̬", visit
				.getDiancxxb_id()
				+ "", "��"));
		
//		�Ƿ�Ϊ���Ƿ���
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���",
				visit.getDiancxxb_id() + "", "��"));
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
			if(!isYc){
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
			if (isNotValue && blnFencb) {
				if(isYc){
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H=CustomMaths.Round_new((100-Mad-Aad)/100*Vdaf/(0.1462*Vdaf+1.1124),2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl.getDouble("vad"),
								blnFencb, getTreeid());
						
						
						if (H == -1) {
							msg += "����:" + mdrsl.getString("huaybh") + " " + sign_H
									+ " " + ErrorMessage.getYuansfsFailed;
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
				if(isYc){
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H=CustomMaths.Round_new((100-Mad-Aad)/100*Vdaf/(0.1462*Vdaf+1.1124),2);
				} else {
					if (!editable_H) {
//						H = Compute.getYuansValue(con, zhillsbid, visit
//								.getDiancxxb_id(), sign_H, mdrsl.getDouble("vad"));
//						�����糧��������ֵĬ��Ϊ3
					
						H=3.0;
						if (H == -1) {
							msg += "����:" + mdrsl.getString("huaybh") + " " + sign_H
									+ " " + ErrorMessage.getYuansfsFailed;
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
			
			sql.append("update meicyb set ");
			sql.append("huaysj").append("=").append(
					"to_date('" + mdrsl.getString("Huaysj")
							+ "','yyyy-mm-dd'),");
			sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad, sign_H, H,
					sign_S, S, sacle));
			
			
			if (!failed) {
				sql.append("shenhzt").append("=3,");
			}
			// ���ܱ�,�жϻ���ֵ¼��ʱ,��½��Ա�Ƿ���ǻ���Ա
			if (xiansztq) {
				sql.append("huayy").append("='" + visit.getRenymc() + "',");
			} else {
				sql.append("huayy").append(
						"='" + mdrsl.getString("huayy") + "',");
			}

			sql.append("lury").append("='" + visit.getRenymc() + "',");
			sql.append("zhiyry").append("='" + mdrsl.getString("zhiyry") + "',");
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
		String tableName = "meicyb";
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
		String tableName = "meicyb";

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

	public void Saveb(){
		
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
		/*
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				// �����ڲ������
			} else {
				sbsql.append("update kuangcyb set ")
				.append("mt = ").append(mdrsl.getDouble("mt"))
				.append(", mad = ").append(mdrsl.getDouble("mad")).append(", aad = ").append(mdrsl.getDouble("aad"))
				.append(", vad = '").append(mdrsl.getDouble("vad"))
				.append(", stad = ").append(mdrsl.getDouble("stad"))
				.append(", qbad = ").append(mdrsl.getDouble("qbad"))
				.append(", zhiyry = ").append(mdrsl.getString("zhiyry"))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();*/
		
		
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

		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setExtGrid(null);
			visit.setString6(DateUtil.FormatDate(new Date()));
			getSelectData();

		}
		getSelectData();
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

	// �������ڰ�,�����糧��

	public void setZhiyrq(String zhiyrq) {
		if (((Visit) this.getPage().getVisit()).getString6() != null
				&& !((Visit) this.getPage().getVisit()).getString6().equals(
						zhiyrq)) {

			((Visit) this.getPage().getVisit()).setString6(zhiyrq);
		}
	}

	public String getZhiyrq() {
		if (((Visit) this.getPage().getVisit()).getString6() == null
				|| ((Visit) this.getPage().getVisit()).getString6().equals("")) {

			((Visit) this.getPage().getVisit()).setString6(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}
}