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

/**
 * @author yinjm
 * ������Duibcx(�ԱȲ�ѯ)
 */

public class Duibcx extends BasePage implements PageValidateListener {
	
	private String bianm; // ����ǰ��ҳ�洫������bianm(�������)
	
	public String getBianm() {
		return bianm;
	}

	public void setBianm(String bianm) {
		this.bianm = bianm;
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
		int headIndex = 0; // ͷָ��
		int colIndex = 1; // ��ָ��
		
//		�����ͷ
		String sqlTableHead = "select lb.mingc from leibb lb, zhuanmb zm, zhillsb zlls " +
			"where zlls.huaylbb_id = lb.id and zm.zhillsb_id = zlls.id and zm.bianm in ("+ getBianm() +") order by lb.mingc";
		
		ResultSetList rslTableHead = con.getResultSetList(sqlTableHead);
		String[][] ArrHeader = new String[1][1 + rslTableHead.getRows()];
		int[] ArrWidth = new int[1 + rslTableHead.getRows()];
		
		while(rslTableHead.next()) {
			ArrHeader[0][1 + headIndex] = rslTableHead.getString("mingc");
			ArrWidth[1 + headIndex] = 100;
			headIndex = headIndex + 1;
		}
		ArrHeader[0][0] = "����ֵ";
		ArrWidth[0] = 250;

//		�������
		String sqlBody = "select zlls.huaylb,\n" +
		"  round_new(zlls.qnet_ar,2) qnet_ar, round_new(zlls.aar,2) aar, round_new(zlls.ad,2) ad,\n" + 
		"  round_new(zlls.vdaf,2) vdaf, round_new(zlls.mt,1) mt, round_new(zlls.stad,2) stad,\n" + 
		"  round_new(zlls.aad,2) aad, round_new(zlls.mad,2) mad, round_new(zlls.qbad,2) qbad,\n" + 
		"  round_new(zlls.had,2) had, round_new(zlls.vad,2) vad, round_new(zlls.std,2) std,\n" + 
		"  round_new(zlls.qgrad,2) qgrad, round_new(zlls.qgrad_daf,2) qgrad_daf, round_new(zlls.har,2) har,\n" + 
		"  round_new(zlls.qgrd,2) qgrd, round_new((100 - zlls.mt) * zlls.stad / (100 - zlls.mad),2) star,\n" + 
		"  round_new(zlls.qnet_ar * 7000 / 29.271,0) frl\n" + 
		"from zhillsb zlls, zhuanmb zm, leibb lb\n" + 
		"where zm.zhillsb_id = zlls.id and zlls.huaylbb_id = lb.id and zm.bianm in ("+ getBianm() +")\n" + 
		"order by lb.mingc";
		
		ResultSetList rslBody = con.getResultSetList(sqlBody);
		String[][] ArrBody = new String[18][1 + rslTableHead.getRows()];

//		��������һ��
		ArrBody[0][0] = "ȫˮ��Mt(%)";
		ArrBody[1][0] = "���������ˮ��Mad(%)";
		ArrBody[2][0] = "����������ҷ�Aad(%)";
		ArrBody[3][0] = "�յ����ҷ�Aar(%)";
		ArrBody[4][0] = "������ҷ�Ad(%)";
		ArrBody[5][0] = "����������ӷ���Vad(%)";
		ArrBody[6][0] = "�����޻һ��ӷ���Vdaf(%)";
		ArrBody[7][0] = "���������ȫ��St,ad(%)";
		ArrBody[8][0] = "�����ȫ��St,d(%)";
		ArrBody[9][0] = "�յ���ȫ��St,ar(%)";
		ArrBody[10][0] = "�����������Had(%)";
		ArrBody[11][0] = "�յ�����Har(%)";
		ArrBody[12][0] = "�����������Ͳ��ֵQb,ad(J/g)";
		ArrBody[13][0] = "�������λ��ֵQgr,d(J/g)";
		ArrBody[14][0] = "�����������λ��ֵQgr,ad(J/g)";
		ArrBody[15][0] = "�����޻һ���λ��ֵQgr,daf(J/g)";
		ArrBody[16][0] = "�յ�����λ��ֵQnet,ar(J/g)";
		ArrBody[17][0] = "������(ǧ��/ǧ��)";

		while(rslBody.next()) {
			ArrBody[0][colIndex] = rslBody.getDouble("mt")+"";
			ArrBody[1][colIndex] = rslBody.getDouble("mad")+"";
			ArrBody[2][colIndex] = rslBody.getDouble("aad")+"";
			ArrBody[3][colIndex] = rslBody.getDouble("aar")+"";
			ArrBody[4][colIndex] = rslBody.getDouble("ad")+"";
			ArrBody[5][colIndex] = rslBody.getDouble("vad")+"";
			ArrBody[6][colIndex] = rslBody.getDouble("vdaf")+"";
			ArrBody[7][colIndex] = rslBody.getDouble("stad")+"";
			ArrBody[8][colIndex] = rslBody.getDouble("std")+"";
			ArrBody[9][colIndex] = rslBody.getDouble("star")+"";
			ArrBody[10][colIndex] = rslBody.getDouble("had")+"";
			ArrBody[11][colIndex] = rslBody.getDouble("har")+"";
			ArrBody[12][colIndex] = rslBody.getDouble("qbad")+"";
			ArrBody[13][colIndex] = rslBody.getDouble("qgrd")+"";
			ArrBody[14][colIndex] = rslBody.getDouble("qgrad")+"";
			ArrBody[15][colIndex] = rslBody.getDouble("qgrad_daf")+"";
			ArrBody[16][colIndex] = rslBody.getDouble("qnet_ar")+"";
			ArrBody[17][colIndex] = rslBody.getDouble("frl")+"";
			colIndex = colIndex + 1;
		}

		rt.setTitle("�� �� �� ѯ", ArrWidth);
		rt.setBody(new Table(ArrBody, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setRowHeight(40);
		rt.body.setCells(1, 1, 19, 1 + rslTableHead.getRows(), Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, colIndex, "��ӡ���ڣ�"+DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_RIGHT);

		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rslTableHead.close();
		rslBody.close();
		con.Close();
		return rt.getAllPagesHtml();
		
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
		setBianm("'"+cycle.getRequestContext().getParameter("bianm").replaceAll(";", "','")+"'");
	}
	
}
