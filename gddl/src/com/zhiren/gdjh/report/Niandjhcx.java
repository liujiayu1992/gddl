package com.zhiren.gdjh.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterCom_dt;
/*
 * ���ߣ����
 * ʱ�䣺2012-10-19
 * �����������ƻ��ھ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-11-14
 * �������������ӵ�λ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-12-22
 * ������������ú���ۼ��㹫ʽ
 */
/*
 * ���ߣ� ��ʤ��
 * ʱ�䣺2013-03-01
 * �����������ۺ͵�����ú���ۺ�˰�Ͳ���˰������ú���� �������������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-04-10
 * ���������ӳ�������ύ���ܡ�
 * 		���Ӷ��ύ�����Ƿ�ʹ�ó���������̽����жϡ�
 * 		�������˹��ܣ�������ʱֱ�ӻ��˳������̺͹�˾�ύ���̡�
 */

public class Niandjhcx extends BasePage implements PageValidateListener {

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _TijiaoChick = false;

	public void TijiaoButton(IRequestCycle cycle) {
		_TijiaoChick = true;
	}
	
	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if(_TijiaoChick){
			_TijiaoChick=false;
			Tij();
		}
		if(_HuitChick){
			_HuitChick=false;
			Huit();
		}
	}
	
	private String UPDZhuangtSQL(String zhuangt){
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String diancxxb_id=this.getTreeid();
		
		String liuczt="";
		if(zhuangt.equals("0")&& MainGlobal.getXitxx_item("�ƻ�", "���������������",diancxxb_id, "��").equals("��")){
			liuczt= ", CG.LIUCZTB_ID=0 ";
		}
		
		String sqlChk="BEGIN\n" +
			"  UPDATE NIANDJH_CAIG CG\n" + 
			"     SET CG.ZHUANGT = "+zhuangt+"\n" + 
			"   WHERE CG.RIQ = "+curdate+"\n" + 
			"     AND CG.DIANCXXB_ID =  "+diancxxb_id+";\n" + 
			"  UPDATE NIANDJH_ZAF CG\n" + 
			"     SET CG.ZHUANGT = "+zhuangt+"\n" + 
			"   WHERE CG.RIQ = "+curdate+"\n" + 
			"     AND CG.DIANCXXB_ID =  "+diancxxb_id+";\n" + 
			"  UPDATE NIANDJH_ZHIB CG\n" + 
			"     SET CG.ZHUANGT = "+zhuangt+" " + liuczt+
			"   WHERE CG.RIQ = "+curdate+"\n" + 
			"     AND CG.DIANCXXB_ID =  "+diancxxb_id+";\n" + 
			"END;";
		return sqlChk;
	}
	
//	�ύ����
	public void Tij(){
		JDBCcon con = new JDBCcon();

		if (MainGlobal.getXitxx_item("�ƻ�", "�Ƿ��������������","0", "��").equals("��")) {
			long intyear;
			if (getNianfValue() == null) {
				intyear = DateUtil.getYear(new Date());
			} else {
				intyear = getNianfValue().getId();
			}

			String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
			
			String diancxxb_id=this.getTreeid();
			
//			ȡ��ָ��ID
			String sql="  select id from NIANDJH_ZHIB CG\n" + 
			"   WHERE CG.RIQ = "+curdate+"\n" + 
			"     AND CG.DIANCXXB_ID =  "+diancxxb_id;
			ResultSetList rsl = new ResultSetList();
			rsl= con.getResultSetList(sql);
			long ID=0;
			while(rsl.next()){
				ID=rsl.getLong("ID");
			}
//			ȡ������ID
			long liucb_id=0;
			sql="SELECT LC.ID  FROM LIUCB LC, LIUCLBB LB  WHERE LC.LIUCLBB_ID = LB.ID AND LB.MINGC = '�ƻ�'";
			rsl= con.getResultSetList(sql);
			while(rsl.next()){
				liucb_id=rsl.getLong("ID");
			}
			rsl.close();
//			�����������
			Liuc.tij("NIANDJH_ZHIB", ID, ((Visit) getPage().getVisit()).getRenyID(), "", liucb_id);
			setMsg("�ύ�������");
		}else{
			con.getUpdate(UPDZhuangtSQL("1"));
			setMsg("�ύ�������");
		}
		
		con.Close();
	}
	
//	���˷���
	public void Huit() {
		JDBCcon con = new JDBCcon();
		String sql=UPDZhuangtSQL("0");

//		ʹ�õ糧�����б��ػ��˵��ж�
		String diancxxb_id = getTreeid();
		if (MainGlobal.getXitxx_item("�±��ϴ�", "�Ƿ������ػ���",diancxxb_id, "��").equals("��")) {
			// ����Ǳ��ػ��˵Ļ� ֱ���ڱ��ؿ���������ύ״̬��
			int num = con.getUpdate(sql);
			if (num != -1) {
				setMsg("�������ݻ��˳ɹ���");
			} else {
				setMsg("�������ݻ���ʧ�ܣ�");
			}
			con.Close();
		} else {
			// �����Զ�̻��˵Ļ����ȸ���Զ�̻���״̬���ٸ��±��ػ���״̬��
			InterCom_dt dt = new InterCom_dt();
			String[] sqls = new String[] { sql };
			String[] answer = dt.sqlExe(diancxxb_id, sqls, true);
			if (answer[0].equals("true")) {
				int num = con.getUpdate(sql);
				if (num == -1) {
					setMsg("�������ݻ��˷����쳣��");
				} else {
					setMsg("���ݻ��˳ɹ���");
				}
			} else {
				setMsg("�������ݷ����쳣��");
			}
		}
	}
	

	// ����չʾ
	public String getPrintTable() {
		return getCgjh()+getZfjh()+getZhib();
	}

	private String getCgjh() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";

		String strSQL =
			"SELECT GHDW,JIHKJ,HET_SL,HET_REZ,HET_MEIJ,HET_YUNJ,JIH_SL,JIH_REZ,JIH_REZC,JIH_MEIJ,JIH_MEIJBHS,JIH_YUNJ,\n" +
			"JIH_YUNJBHS,JIH_ZAF,JIH_ZAFBHS,JIH_QIT,JIH_QITBHS,(JIH_MEIJ+JIH_YUNJ+JIH_ZAF+JIH_QIT)JIH_DAOCJ," +
			"DECODE(JIH_REZ,0,0,ROUND((JIH_MEIJ+JIH_YUNJ+JIH_ZAF+JIH_QIT)*29.271/JIH_REZ,2))JIH_DAOCBMDJ\n" +
			"FROM ("+
			"SELECT DECODE(GROUPING(G.MINGC), 1, '<b>�ܼ�</b>', G.MINGC) GHDW, J.MINGC JIHKJ,\n" +
			"      SUM(CG.HET_SL) HET_SL,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_REZ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_REZ,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_MEIJ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_MEIJ,\n" + 
			"      ROUND(DECODE(SUM(CG.HET_SL),0,0,SUM(CG.HET_YUNJ * CG.HET_SL) / SUM(CG.HET_SL)),2) HET_YUNJ,\n" + 
			"      SUM(CG.JIH_SL) JIH_SL,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_REZ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_REZ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM((CG.JIH_REZ - NVL(ZB.RUCRLRZC, 0)) * CG.JIH_SL) /SUM(CG.JIH_SL)),2) JIH_REZC,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_MEIJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_MEIJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_MEIJBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_MEIJBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_YUNJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_YUNJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_YUNJBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_YUNJBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_ZAF * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_ZAF,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_ZAFBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_ZAFBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_QIT * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_QIT,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_QITBHS * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_QITBHS,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_DAOCJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_DAOCJ,\n" + 
			"      ROUND(DECODE(SUM(CG.JIH_SL),0,0,SUM(CG.JIH_DAOCBMDJ * CG.JIH_SL) / SUM(CG.JIH_SL)),2) JIH_DAOCBMDJ\n" + 
			"  FROM NIANDJH_CAIG CG, GONGYSB G, NIANDJH_ZHIB ZB, JIHKJB J\n" + 
			" WHERE CG.GONGYSB_ID = G.ID AND CG.JIHKJB_ID=J.ID\n" + 
			"   AND CG.DIANCXXB_ID = ZB.DIANCXXB_ID\n" + 
			"   AND CG.RIQ = ZB.RIQ\n" + 
			"   AND CG.RIQ = "+curdate+"\n" + 
			"   AND CG.DIANCXXB_ID="+this.getTreeid()+"\n" + 
			" GROUP BY ROLLUP((G.MINGC,J.MINGC)))";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 4, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[4][19];
		ArrHeader[0] = (new String[] { "������λ","�ƻ��ھ�", intyear+"���ͬ��", intyear+"���ͬ��", intyear+"���ͬ��", intyear+"���ͬ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��",intyear+"��ɹ����Ԥ��"});
		ArrHeader[1] = (new String[] { "������λ","�ƻ��ھ�", "����", "��ֵ", "�����<br>(��˰)", "�˷�<br>(��˰)","������","��ֵ","����¯��ֵ","�����","�����<br>(����˰)","�˷�","�˷�<br>(����˰)","�ӷ�","�ӷ�<br>(����˰)","����","����<br>(����˰)","������","������<br>ú����" });
		ArrHeader[2] = (new String[] { "������λ","�ƻ��ھ�", "��","�׽�/ǧ��", "Ԫ/��", "Ԫ/��","��","�׽�/ǧ��","�׽�/ǧ��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��"});
		ArrHeader[3] = (new String[] { "��","��", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16","17"});
		
		ArrWidth = (new int[] { 130,60,70,60,60,60,70,60,60,60,60,60,60,60,60,60,60,60,60 });
		String rptitle="��һ��"+intyear+"��ú̿�ɹ��ƻ�(���С��ӷѡ���ָú̿������ͬ��Լ������ú����˷�һͬ����ķ��ã������װ����վ̨�����ӵȷ��ã�һƱ����ĳ���)";
		rt.setTitle("", ArrWidth);
		
		rt.title.setCellValue(2, 1, "��λ���ƣ�"+((IDropDownModel) getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		rt.title.setCellFont(2, 1, "", 12, true);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 3, 1);
		rt.body.merge(1, 2, 3, 2);
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getZfjh() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";

		String strSQL =
			"SELECT DECODE(GROUPING(ZAFMC), 1, '<b>�ܼ�</b>', ZAFMC) ZAFMC,\n" +
			"       SUM(YUCJE),\n" + 
			"       YUCSM,\n" + 
			"       SUM(SHIJWCJE),\n" + 
			"       SUM(YUJWCJE),\n" + 
			"       YUJWCSM\n" + 
			"  FROM NIANDJH_ZAF\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+this.getTreeid()+"\n" + 
			" GROUP BY ROLLUP((ZAFMC, YUCSM, YUJWCSM))";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 2, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[2][6];
		ArrHeader[0] = (new String[] { "��������", intyear+"��Ԥ��", intyear+"��Ԥ��", (intyear-1)+"��Ԥ�����", (intyear-1)+"��Ԥ�����",(intyear-1)+"��Ԥ�����"});
		ArrHeader[1] = (new String[] { "��������", "Ԥ��(Ԫ)", "˵��", "ʵ�����(Ԫ)", "Ԥ��(Ԫ)","˵��"});
		
		ArrWidth = (new int[] { 170,120,440,120,120,260});
		String rptitle="���:����ȼ�ϳɱ����������ã�ȼ���ӷѣ�";
		rt.setTitle("", ArrWidth);
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
		rt.body.mergeFixedRowCol();
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}
	
	private String getZhib() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";

		String strSQL ="SELECT DECODE(0, 0, '�ϼ�') QIB,\n" +
			"       ZB.FADL,\n" + 
			"       ZB.GONGDMH,\n" + 
			"       ZB.FADCYDL,\n" + 
			"       ZB.FADBML,\n" + 
			"       ZB.GONGRL,\n" + 
			"       ZB.GONGRMH,\n" + 
			"       ZB.GONGRBML,\n" + 
			"       ZB.BIAOMLHJ,\n" + 
			"       ZB.MEIZBML,\n" + 
			"       ZB.MEIZBMDJ,\n" + 
			"       ZB.RANYL, \n" +
			"		ZB.YOUZBML,\n" + 
			"       ZB.RANYDJ,\n" + 
			"       ZB.YOUZBMDJ,\n" + 
			"       ZB.QITFY,\n" + 
			"       ZB.RLZHBMDJ\n" + 
			"  FROM NIANDJH_ZHIB ZB\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+this.getTreeid()+"";

		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[3][17];
		ArrHeader[0] = (new String[] { "�ڱ�", "������", "����ú��", "���糧<br>�õ���", "�����ú��","������","����ú��","���ȱ�ú��","��ú���ϼ�","ú�۱�ú��","ú��<br>��ú����","ȼ����","����<br>��ú��","ȼ�͵���<br>(����˰)","����<br>��ú����","��������","��¯�ۺϱ�ú����"});
		ArrHeader[1] = (new String[] { "�ڱ�", "��ǧ��ʱ","��/ǧ��ʱ", "%", "��","�򼪽�","ǧ��/����","��","��","��","Ԫ/��","��","��","Ԫ/��","Ԫ/��","Ԫ","Ԫ/��"});
		ArrHeader[2] = (new String[] { "��", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16"});
		
		ArrWidth = (new int[] { 60,80,70,60,80,80,70,80,80,80,60,80,70,70,70,80,60});
		String rptitle="����:"+intyear+"�����ָ��Ԥ��";
		rt.setTitle("", ArrWidth);
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 2, 1);
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArrWidth);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged = false;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2011; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	// ***************************�����ʼ����***************************//
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

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	// ҳ���ж�����
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

	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
//			ͨ���������Ƿ���Ի��˽�������
			if(cycle.getRequestContext().getParameter("lx") != null){
				visit.setString1(cycle.getRequestContext().getParameter("lx"));
			}
			visit.setList1(null);
			setNianfValue(null);
			getNianfModels();
			getIDiancmcModels();
			this.setTreeid(null);
			setToolbar(null);
		}
		getToolBars();
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
//		 ��λ
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
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
		// ���
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		
		String msg=MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 5000);
		ToolbarButton sbbt = new ToolbarButton(null, "�ύ",
		"function(){document.getElementById('Tijiao').click();\n "+msg+"}");
		sbbt.setIcon(SysConstant.Btn_Icon_SelSubmit);
//		���ϵͳ����δ�ύ�����ݣ���ô�ύ��ť�ɼ�
		if(DataChk()){
			tb1.addItem(sbbt);
		}else{
			Visit visit = (Visit) getPage().getVisit();
			if(visit.getString1()!=null && !visit.getString1().equals("") &&visit.getString1().equals("return")){
				ToolbarButton sbht = new ToolbarButton(null, "����",
				"function(){document.getElementById('Huit').click();\n "+msg+"}");
				sbht.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(sbht);
			}
		}
		setToolbar(tb1);
	}
	
//	���ϵͳ���Ƿ����δ�ύ������
	private boolean DataChk(){
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		String curdate = "to_date('" + intyear + "-01-01','yyyy-mm-dd')";
		String diancxxb_id=this.getTreeid();
		JDBCcon cn = new JDBCcon();
		String sqlChk=
			"SELECT ID\n" +
			"  FROM NIANDJH_CAIG CG\n" + 
			" WHERE CG.RIQ = "+curdate+"\n" + 
			"   AND CG.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"   AND CG.ZHUANGT = 0\n" + 
			"UNION ALL\n" + 
			"SELECT ID\n" + 
			"  FROM NIANDJH_ZAF CG\n" + 
			" WHERE CG.RIQ = "+curdate+"\n" + 
			"   AND CG.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"   AND CG.ZHUANGT = 0\n" + 
			"UNION ALL\n" + 
			"SELECT ID\n" + 
			"  FROM NIANDJH_ZHIB CG\n" + 
			" WHERE CG.RIQ = "+curdate+"\n" + 
			"   AND CG.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"   AND CG.ZHUANGT = 0";
		
//		�ж��Ƿ����ƻ�����������̣����������ֻ���ύ״̬�����״̬��Ϊ0ʱ�����ݲ�Ϊδ�������
		if (MainGlobal.getXitxx_item("�ƻ�", "�Ƿ��������������","0", "��").equals("��")) {
			sqlChk=
				"SELECT ID\n" + 
				"  FROM NIANDJH_ZHIB CG\n" + 
				" WHERE CG.RIQ = "+curdate+"\n" + 
				"   AND CG.DIANCXXB_ID = "+diancxxb_id+"\n" + 
				"   AND CG.ZHUANGT = 0 AND CG.LIUCZTB_ID=0";
		}
		boolean statu=cn.getHasIt(sqlChk);
		cn.Close();
		return statu;
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

	// �糧��
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
		String sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
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
}