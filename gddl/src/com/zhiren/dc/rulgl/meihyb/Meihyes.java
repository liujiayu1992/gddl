package com.zhiren.dc.rulgl.meihyb;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meihyes extends BasePage implements PageValidateListener {
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

	private void Save(int num) {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		int shenhzt = 0;
		String msgstr = "";
		if (num == 1) {
			shenhzt = 5;
			msgstr = "�����ѷ�棡";
		} else if (num == 2) {
			shenhzt = 4;
			msgstr = "�����ѻ��ˣ�";
		}
		// ��������
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Yundlr.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if (rsl.getRows() == 0) {
			return;
		}
		sb.append("begin\n");
		while (rsl.next()) {
			sb.append("update meihyb set FADHY=");
			sb.append(rsl.getString("FADHY")).append(",GONGRHY=").append(
					rsl.getString("GONGRHY")).append(",QITY=");
			sb.append(rsl.getString("QITY")).append(",FEISCY=").append(
					rsl.getString("FEISCY")).append(",BEIZ='");
			sb.append(rsl.getString("BEIZ")).append("',LURY='").append(
					rsl.getString("LURY")).append("',LURSJ=to_date('");
			sb.append(rsl.getString("LURSJ")).append(
					"','yyyy-mm-dd'),SHENHZT= " + shenhzt);
			sb.append(" where id=").append(rsl.getString("id")).append(";\n");
		}
		sb.append("end;");
		con.getUpdate(sb.toString());
		con.Close();
		setMsg(msgstr);

	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save(1);
			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Save(2);
			getSelectData();

		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());

		}

		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,m.beiz,m.lury,m.lursj,m.shenhzt\n"
				+ "  from meihyb m, diancxxb d, rulmzlb r, rulbzb rb, jizfzb j\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulmzlb_id = r.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+ "   and m.rulrq = to_date('"
				+ riqTiaoj
				+ "','yyyy-mm-dd')\n"
				+ "   and d.id ="
				+ visit.getDiancxxb_id()
				+ "   and m.shenhzt = 3";
		// System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");

		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("��������");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("��¯����");
		egu.getColumn("rulbzb_id").setEditor(null);
		egu.getColumn("jizfzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setEditor(null);
		egu.getColumn("fadhy").setHeader("�������(t)");
		egu.getColumn("gongrhy").setHeader("���Ⱥ���(t)");
		egu.getColumn("qity").setHeader("������(t)");
		egu.getColumn("feiscy").setHeader("��������(t)");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lursj").setHeader("¼��ʱ��");
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("shenhzt").setHeader("״̬");
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		egu.setWidth(1000);// ����ҳ��Ŀ��,������������ʱ��ʾ������
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + visit.getDiancxxb_id());
		egu.getColumn("rulrq").setDefaultValue(riqTiaoj);
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("rulmzlb_id").setDefaultValue("0");
		egu.getColumn("fadhy").setDefaultValue("0");
		egu.getColumn("gongrhy").setDefaultValue("0");
		egu.getColumn("qity").setDefaultValue("0");
		egu.getColumn("feiscy").setDefaultValue("0");
		egu.getColumn("shenhzt").setDefaultValue("5");

		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		egu.addToolbarItem(df.getScript());

		// ************************************************************
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addToolbarButton("���", GridButton.ButtonType_Sel, "SaveButton",
				null, SysConstant.Btn_Icon_Show);
		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,
				"HuitButton");
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
			setJizfzbModel(null);
			setJizfzbModels();
			setRulbzbModel(null);
			setRulbzbModels();
			getSelectData();
		}
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

	public static void UpdateRulzlID(String riq, long diancxxb_id) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("update meihyb h ")
				.append("set rulmzlb_id = ( \n")
				.append(
						"select nvl(max(id),0) from rulmzlb z where z.rulrq = h.rulrq \n")
				.append(
						"and z.diancxxb_id = h.diancxxb_id and z.rulbzb_id = h.rulbzb_id \n")
				.append("and z.jizfzb_id = h.jizfzb_id ) where h.rulrq = ")
				.append(DateUtil.FormatOracleDate(riq)).append(
						" and h.diancxxb_id=").append(diancxxb_id);
		JDBCcon con = new JDBCcon();
		con.getUpdate(sb.toString());
		con.Close();
	}

	public IPropertySelectionModel getRulbzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setRulbzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setRulbzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setRulbzbModels() {
		String sql = "select r.id,r.mingc from rulbzb r";
		setRulbzbModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getJizfzbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setJizfzbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setJizfzbModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setJizfzbModels() {
		Visit visit = (Visit) getPage().getVisit();
		String sql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="
				+ visit.getDiancxxb_id() + "";
		setJizfzbModel(new IDropDownModel(sql));
	}

}
