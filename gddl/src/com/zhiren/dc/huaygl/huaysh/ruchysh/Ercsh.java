package com.zhiren.dc.huaygl.huaysh.ruchysh;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ercsh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRiqi() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiqi(String change) {
		((Visit) this.getPage().getVisit()).setString1(change);
	}

	public String getRiq2() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setRiq2(String change) {
		((Visit) this.getPage().getVisit()).setString2(change);
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// tbars = "";
		String sql = "";
		boolean Display = false;
		sql = "select zhi from xitxxb where mingc = '���������ʾ��'";
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��ʾ")) {
				Display = true;
			}
		}
		rsl.close();
		sql = ("select l.id ,\n"
				+ "       m.mingc meikdw,\n"
				+ "       p.mingc as pinz,\n"
				+ "       f.jingz as shul,\n"
				+ "       l.huaysj,\n"
				+ "       z.bianm as huaybh,\n"
				+ "       l.qnet_ar,\n"
				+ "       l.aar,\n"
				+ "       l.ad,\n"
				+ "       l.vdaf,\n"
				+ "       l.mt,\n"
				+ "       l.stad,\n"
				+ "       l.aad,\n"
				+ "       l.mad,\n"
				+ "       l.qbad,\n"
				+ "       l.had,\n"
				+ "       l.vad,\n"
				+ "       l.fcad,\n"
				+ "       l.std,\n"
				+ "       l.qgrad,\n"
				+ "       l.hdaf,\n"
				+ "       l.qgrad_daf,\n"
				+ "       l.sdaf,\n"
				+ "       l.t1,\n"
				+ "       l.t2,\n"
				+ "       l.t3,\n"
				+ "       l.t4,\n"
				+ "       l.huayy,\n"
				+ "       l.lury,\n"
				+ "       l.beiz,\n"
				+ "       l.huaylb\n"
				+ "  from zhuanmb z, zhillsb l, caiyb c, "
				+ " (select sum(laimsl) as jingz, meikxxb_id, zhilb_id, pinzb_id \n"
				/**
				 * huochaoyuan 2009-02-17
				 * �޸��ϱ�fahb��ȡ�õ�������Ϣ����ǰ��sum(jingz-koud),�޸�Ϊ�����µ�ͳ����ȡֵ(laimsl)
				 */	
				+ "  from fahb \n"
				+ "  group by pinzb_id, zhilb_id, meikxxb_id) f, meikxxb m, pinzb p\n"
				+ " where z.zhillsb_id = l.id\n"
				+ "   and f.zhilb_id = c.zhilb_id\n"
				+ "   and c.zhilb_id = l.zhilb_id\n"
				+ "   and f.meikxxb_id = m.id\n"
				+ "   and f.pinzb_id = p.id\n"
				+ "   and l.huaysj between "
				+ DateUtil.FormatOracleDate(getRiqi())
				+ " and\n"
				+ "             "
				+ DateUtil.FormatOracleDate(getRiq2())
				+ "\n"
				+ "and l.zhilb_id ="
				+ ((Visit) getPage().getVisit()).getString6()
				+ "\n"
				+ "   and z.zhuanmlb_id =\n"
				+ "       (select id\n"
				+ "          from zhuanmlb\n"
				+ "         where jib = (select nvl(max(jib), 0) from zhuanmlb))\n"
		// + " and (shenhzt = 3 or shenhzt = 4)\n"
		+ " order by z.bianm, l.huaylb");
		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("zhilb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(1000);
		// /������ʾ������
		if (!Display) {
			egu.getColumn("meikdw").setHidden(true);
			egu.getColumn("pinz").setHidden(true);
			egu.getColumn("shul").setHidden(true);
		}
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikdw").setHeader("ú��λ");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		egu.getColumn("shul").setHeader("����(��)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("shul").setWidth(60);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("�յ�����λ����<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("aar").setHeader("�յ����ҷ�<p>Aar(%)</p>");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("������ҷ�<p>Ad(%)</p>");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("�����޻һ��ӷ���<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("ȫˮ��<p>Mt(%)</p>");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("stad").setHeader("���������ȫ��<p>St,ad(%)</p>");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("����������ҷ�<p>Aad(%)</p>");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("���������ˮ��<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("qbad").setHeader("�����������Ͳ��ֵ<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("�����������<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("����������ӷ���<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("�̶�̼<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("�����ȫ��<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("�����������λ��ֵ<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("�����޻һ���<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("�����޻һ�ȫ��<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t1").setEditor(null);
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t2").setEditor(null);
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t3").setEditor(null);
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("t4").setEditor(null);
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		egu.addPaging(25);
		// Toolbar tb1 = new Toolbar("tbdiv");

		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		egu.addTbarText("-");

		egu.addToolbarButton("ȷ��", GridButton.ButtonType_SubmitSel, "RbButton");
		// egu.addToolbarButton("���", GridButton.ButtonType_Sel, "SaveButton",
		// null, SysConstant.Btn_Icon_Show);
		// egu.addToolbarButton("����", GridButton.ButtonTypes_Sel, "ShenhButton",
		// null, SysConstant.Btn_Icon_Show);
		egu.addToolbarItem("{"
				+ new GridButton("����",
						"function(){ document.getElementById('ReturnButton').click();"
								+ "}").getScript() + "}");
		setExtGrid(egu);
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private boolean _RbChick = false;;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;

			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			cycle.activate("Huayejsh");

		}
		if (_RbChick) {
			_RbChick = false;
			gotoruchyejsh(cycle);

		}

	}

	private void gotoruchyejsh(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		// ��Ҫ�����¸�ҳ��ȡֵ
		// diancxxb_id��lie_id��feiylbb_id��meikxxb_id��faz_id.daoz_id,fahrq,yansbh
		// JDBCcon con = new JDBCcon();
		ExtGridUtil egu = this.getExtGrid();
		ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
		StringBuffer sql = new StringBuffer("");
		// String[] mstr_huaybh = new String[mdrsl.getRows()];
		while (mdrsl.next()) {
			for (int i = 0; i < mdrsl.getColumnCount(); i++) {

				if (mdrsl.getColumnNames()[i].equals("ID")) {

					sql.append(mdrsl.getString(i)).append(",");
				}
			}
		}
		mdrsl.close();
		sql.deleteCharAt(sql.length() - 1);
		// String SQL = "String * from zhillsb where id in
		// ("+sql.toString()+")";
		// ResultSetList rsl = con.getResultSetList(SQL);
		// while(rsl.next()){
		//			
		// }
		// ������
		((Visit) getPage().getVisit()).setString3(sql.toString());

		cycle.activate("Huayejsh");
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
			visit.isFencb();
			// visit.setString1("");
			// visit.setString2("");
			visit.setString3("");

			getSelectData();

		}
		getSelectData();
	}

}