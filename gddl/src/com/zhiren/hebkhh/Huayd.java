package com.zhiren.hebkhh;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-25
 * ������ȥ�������糧��ϢID�Ĺ���
 */
/**
 * @author yinjm
 * ������Huayd(���鵥)
 */

public class Huayd extends BasePage implements PageValidateListener {
	
	private String zhilb_id; // ���ǰ��ҳ�洫������zhilb_id
	
	private String dianc_id; // ���ǰ��ҳ�洫������dianc_id
	
	public String getDianc_id() {
		return dianc_id;
	}

	public void setDianc_id(String dianc_id) {
		this.dianc_id = dianc_id;
	}

	public String getZhilb_id() {
		return zhilb_id;
	}

	public void setZhilb_id(String zhilb_id) {
		this.zhilb_id = zhilb_id;
	}

	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
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
	
	public boolean getRaw() {
		return true;
	}
	
	public String getPrintTable() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		
		String strSql = "select zlls.zhilb_id zlls_zhilb_id, getHuaybh4zl(zlls.zhilb_id) bianm, max(mkxx.mingc) mkmc,\n" +
			"  max(czxx.mingc) fzmc, to_char(max(zlls.huaysj), 'yyyy-MM-dd') huaysj, to_char(cy.caiyrq, 'yyyy-MM-dd') caiyrq, sum(fh.laimsl) laimsl,\n" + 
			"  pz.mingc meiz, GetCaiyry4zl(zlls.zhilb_id) lurry, GETHUAYBBCHEPS(zlls.zhilb_id) cheph, getbeiz(zlls.zhilb_id) beiz,\n" +
			"  gethuayy4zl(zlls.zhilb_id) huayy,\n" + 
			"  round_new(avg(zlls.qnet_ar),2) qnet_ar, round_new(avg(zlls.aar),2) aar, round_new(avg(zlls.ad),2) ad,\n" + 
			"  round_new(avg(zlls.vdaf),2) vdaf, round_new(avg(zlls.mt), 1) mt, round_new(avg(zlls.stad),2) stad,\n" + 
			"  round_new(avg(zlls.aad),2) aad, round_new(avg(zlls.mad),2) mad, round_new(avg(zlls.qbad),2) qbad,\n" + 
			"  round_new(avg(zlls.had),2) had, round_new(avg(zlls.vad),2) vad, round_new(avg(zlls.std),2) std,\n" + 
			"  round_new(avg(zlls.qgrad),2) qgrad, round_new(avg(zlls.qgrad_daf),2) qgrad_daf, round_new(avg(zlls.har),2) har,\n" + 
			"  round_new(avg(zlls.qgrd),2) qgrd, round_new((100 - avg(zlls.mt)) * avg(zlls.stad) / (100 - avg(zlls.mad)),2) star,\n" + 
			"  round_new(avg(zlls.qnet_ar) * 7000 / 29.271,0) frl\n" + 
			"from zhillsb zlls, fahb fh, meikxxb mkxx, chezxxb czxx, caiyb cy, pinzb pz\n" + 
			"where zlls.zhilb_id = "+ getZhilb_id() +//" and fh.diancxxb_id = "+ getDianc_id() +
			" and fh.zhilb_id = zlls.zhilb_id and fh.meikxxb_id = mkxx.id\n" + 
			"  and fh.faz_id = czxx.id and cy.zhilb_id = zlls.zhilb_id and fh.pinzb_id = pz.id\n" + 
			"group by zlls.zhilb_id, cy.caiyrq, pz.mingc";
		ResultSetList rs = con.getResultSetList(strSql);
		String huayy = ""; // ���뻯��Ա��Ϣ
		
		String[][] ArrBody = new String[21][6];
		if (rs.next()) {
			ArrBody[0] = new String[] {"������", rs.getString("bianm"), "���", rs.getString("mkmc"), "��վ", rs.getString("fzmc")};
			ArrBody[1] = new String[] {"��������", rs.getString("huaysj"), "��������", rs.getString("caiyrq"), "ú��(t)", rs.getString("laimsl")};
			ArrBody[2] = new String[] {"ú��", rs.getString("meiz"), "��������Ա", rs.getString("lurry"), "", ""};
			ArrBody[3] = new String[] {"����", getCheph(rs.getString("cheph")), "", "", "", ""};
			ArrBody[4] = new String[] {"ȫˮ��Mt(%)", "", "", rs.getDouble("mt")+"", "��ע", ""};
			ArrBody[5] = new String[] {"���������ˮ��Mad(%)", "", "", rs.getDouble("mad")+"", "", ""};
			ArrBody[6] = new String[] {"����������ҷ�Aad(%)", "", "", rs.getDouble("aad")+"", "", ""};
			ArrBody[7] = new String[] {"�յ����ҷ�Aar(%)", "", "", rs.getDouble("aar")+"", "", ""};
			ArrBody[8] = new String[] {"������ҷ�Ad(%)", "", "", rs.getDouble("ad")+"", "", ""};
			ArrBody[9] = new String[] {"����������ӷ���Vad(%)", "", "", rs.getDouble("vad")+"", "", ""};
			ArrBody[10] = new String[] {"�����޻һ��ӷ���Vdaf(%)", "", "", rs.getDouble("vdaf")+"", "", ""};
			ArrBody[11] = new String[] {"���������ȫ��St,ad(%)", "","", rs.getDouble("stad")+"", "", ""};
			ArrBody[12] = new String[] {"�����ȫ��St,d(%)", "", "", rs.getDouble("std")+"", "", ""};
			ArrBody[13] = new String[] {"�յ���ȫ��St,ar(%)", "", "", rs.getDouble("star")+"", "", ""};
			ArrBody[14] = new String[] {"�����������Had(%)", "", "", rs.getDouble("had")+"", "", ""};
			ArrBody[15] = new String[] {"�յ�����Har(%)", "", "", rs.getDouble("har")+"", "", ""};
			ArrBody[16] = new String[] {"�����������Ͳ��ֵQb,ad(J/g)", "", "", rs.getDouble("qbad")+"", "", ""};
			ArrBody[17] = new String[] {"�������λ��ֵQgr,d(J/g)", "", "", rs.getDouble("qgrd")+"", "", ""};
			ArrBody[18] = new String[] {"�����������λ��ֵQgr,ad(J/g)", "", "", rs.getDouble("qgrad")+"", "", "" };
			ArrBody[19] = new String[] {"�����޻һ���λ��ֵQgr,daf(J/g)", "", "", rs.getDouble("qgrad_daf")+"", "", ""};
			ArrBody[20] = new String[] {"�յ�����λ��ֵQnet,ar(J/g)", "", "", rs.getDouble("qnet_ar")+"", rs.getDouble("frl")+"(ǧ��/ǧ��)", ""};

//			�ӽ������ȡ����ע��Ϣ��ʾ��ҳ����
			String beiz = rs.getString("beiz");
			if (beiz != null && !"".equals(beiz)) {
				for (int i = 5; i <= 19; i ++) {
					ArrBody[i][4] = beiz;
					ArrBody[i][5] = beiz;
				}
			}
			huayy = rs.getString("huayy");
			if (huayy == null) { // ����ӽ������ȡ�����Ļ���Ա��Ϣ��null����ô��ҳ������ʾ""(�մ�)
				huayy = "";
			}
		} else {
			return null;
		}
		
		int[] ArrWidth = new int[] {100, 155, 95, 155, 95, 95};
		
		rt.setTitle("�� �� ��", ArrWidth);
		rt.setBody(new Table(ArrBody, 0, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setCells(1, 1, 21, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setRowHeight(40);
		for(int i = 1; i <= 21; i ++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		for (int i = 5; i <= 21; i ++) {
			rt.body.mergeCell(i, 1, i, 3);
		}
		rt.body.mergeCell(3, 4, 3, 6);
		rt.body.mergeCell(4, 2, 4, 6);
		rt.body.setCells(4, 2, 4, 2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.mergeCell(5, 5, 5, 6);
		rt.body.mergeCell(6, 5, 20, 6);
		rt.body.mergeCell(21, 5, 21, 6);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 3, "����Ա��" + huayy, Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rs.close();
		con.Close();
		return rt.getAllPagesHtml();
		
	}
	
	/*
	 * �����ݿ��ѯ�����ĳ�Ƥ���ַ������ܺܳ����Ӷ�Ӱ�쵽�����ȵ�������ʾ��
	 * �������Ƥ���ַ�������<br>(���з�)��������һ���⡣
	 */
	public String getCheph(String strCheph) {
		String[] arr = strCheph.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i ++) {
			sb.append(arr[i] + ",");
			if (sb.toString().split(",").length % 10 == 0) {
				sb.append("<br>");
			}
		}
		if (sb.toString().endsWith("<br>")) {
			return sb.toString();
		} else {
			return sb.substring(0, sb.length() - 1);
		}
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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
		}
		setZhilb_id(cycle.getRequestContext().getParameter("zhilb_id"));
		setDianc_id(cycle.getRequestContext().getParameter("dianc_id"));
	}
}
