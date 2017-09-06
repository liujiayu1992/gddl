package com.zhiren.shanxdted.hetgl.hetsh;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Hetsh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��¼δ���Ķ����豣�棡");
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		int flag = 0;
		while (rsl.next()) {
			 sql.append("update hetb set liucztb_id = decode('").append(rsl.getString("liucztb_id")).append("','δ���',0,1)")
				.append(" where id = ").append(rsl.getString("id")).append(";");
		}
		rsl.close();
		sql.append("end;");
		flag = con.getUpdate(sql.toString());	
		if (flag >= 0) {
			setMsg("����ɹ�");
		} else {
			setMsg("����ʧ��");
			return;
		}
		con.Close();
	}

	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}

		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = new ExtGridUtil();
		ResultSetList rsl = new ResultSetList();

		String meikdw = this.getMeikdwValue().getStrId();
		if (meikdw.equals("-1")) {
			meikdw = "";
		} else
			meikdw = " AND H.GONGYSB_ID = " + this.getMeikdwValue().getStrId()
					+ "\n";

		String sql = "SELECT DISTINCT H.ID,H.HETBH,G.MINGC, QISRQ, GUOQRQ, DANJ, DECODE(H.LIUCZTB_ID,0,'δ���',1,'�����') AS LIUCZTB_ID,h.qiandrq\n"
				+ "  FROM HETB H,\n"
				+ "       HETJGB JG,\n"
				+ "       GONGYSB G,\n"
				+ "       (SELECT HETB_ID, WM_CONCAT(DANJ) AS DANJ\n"
				+ "          FROM (SELECT HETB_ID,\n"
				+ "                       ('����:' || SHANGX || '  ����:' || XIAX || '  ����:' || JIJ) AS DANJ\n"
				+ "                  FROM HETJGB)\n"
				+ "         GROUP BY HETB_ID) J\n"
				+ " WHERE H.ID = JG.HETB_ID\n"
				+ "   AND H.ID = J.HETB_ID\n"
				+ "   AND H.GONGYSB_ID = G.ID\n"
				+ " AND TO_CHAR(H.QIANDRQ, 'YYYY') =" + getNianfValue().getId()	+ "\n"
				+ " AND TO_CHAR(H.QIANDRQ,'MM') = " + getYuefValue().getId() + "\n" + meikdw
				+ " order by h.hetbh,h.qiandrq";
		rsl = con.getResultSetList(sql.toString());

		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("hetb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("hetbh").setHeader("��ͬ���");
		egu.getColumn("hetbh").setUpdate(false);
		egu.getColumn("hetbh").setEditor(null);
		egu.getColumn("hetbh").setWidth(200);
		egu.getColumn("mingc").setHeader("��Ӧ��");
		egu.getColumn("mingc").setUpdate(false);
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("mingc").setWidth(200);
		egu.getColumn("qisrq").setHeader("��Ч����");
		egu.getColumn("qisrq").setEditor(null);
		egu.getColumn("guoqrq").setHeader("��������");
		egu.getColumn("guoqrq").setEditor(null);
		egu.getColumn("danj").setHeader("����");
		egu.getColumn("danj").setEditor(null);
		egu.getColumn("danj").setWidth(250);
		egu.getColumn("liucztb_id").setHeader("���״̬");
		
		egu.getColumn("qiandrq").setHeader("��������");
		egu.getColumn("qiandrq").setEditor(null);
		egu.getColumn("qiandrq").setHidden(true);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		// ----------
		ComboBox shenh = new ComboBox();
		shenh.setAllowBlank(false);
		egu.getColumn("liucztb_id").setEditor(shenh);
		List list = new ArrayList();
		list.add(new IDropDownBean("0", "δ���"));
		list.add(new IDropDownBean("1", "�����"));
		egu.getColumn("liucztb_id").setComboEditor(egu.gridId,
				new IDropDownModel(list));
		egu.getColumn("liucztb_id").setReturnId(true);

		// ********************������************************************************
		
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("-");
		
		egu.addTbarText("ǩ�����:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("�·�:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ����ú��λ
		// -------------------------------
		egu.addTbarText(Locale.meikxxb_id_fahb);
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MEIKDW");
		comb3.setId("MEIKDW");
		comb3.setLazyRender(true);// ��̬��
		comb3.setWidth(120);
		egu.addToolbarItem(comb3.getScript());
		egu.addTbarText("-");

		// ˢ�°�ť
		StringBuffer rsb2 = new StringBuffer();
		rsb2.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr2 = new GridButton("ˢ��", rsb2.toString());
		gbr2.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr2);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
			setMeikdwModel(null);
			setMeikdwValue(null);
			visit.setString3(null);
		}

		getSelectData();

	}

	// ���
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// ����ú��λ
	private IDropDownBean MeikdwValue;

	public IDropDownBean getMeikdwValue() {
		if (MeikdwValue == null) {
			MeikdwValue = (IDropDownBean) getMeikdwModel().getOption(0);
		}
		return MeikdwValue;
	}

	public void setMeikdwValue(IDropDownBean Value) {
		if (!(MeikdwValue == Value)) {
			MeikdwValue = Value;
		}
	}

	private IPropertySelectionModel MeikdwModel;

	public void setMeikdwModel(IPropertySelectionModel value) {
		MeikdwModel = value;
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (MeikdwModel == null) {
			getMeikdwModels();
		}
		return MeikdwModel;
	}

	public IPropertySelectionModel getMeikdwModels() {

		String sql = " select id,mingc from gongysb order by mingc ";
		MeikdwModel = new IDropDownModel(sql, "ȫ��");
		return MeikdwModel;
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

}