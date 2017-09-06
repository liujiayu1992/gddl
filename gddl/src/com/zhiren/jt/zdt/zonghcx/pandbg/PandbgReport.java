package com.zhiren.jt.zdt.zonghcx.pandbg;
/*
 * ʱ�䣺2009-12-18
 * ���ߣ�tzf
 * �޸����ݣ��������ڶ�    ��ѡ��ֹ�˾�����ܳ�ʱ  �����ӵ糧������(�̵����)
 */
/* 
* ʱ�䣺2009-08-11
* ���ߣ� ll
* �޸����ݣ�1���޸��̵㱨����ʾ���岻һ�£���Ϊ9pt.
*/ 
/* 
* ʱ�䣺2009-08-21
* ���ߣ� sy
* �޸����ݣ�1���޸���ĩ��ú���ͣ������ı�߾�
           2�����̵�ʱ��������³�������λ��
*/ 
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.CustomDate;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ResultSetList;
//import com.zhiren.common.IDropDownSelectionModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
//import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
//import com.zhiren.jt.zdt.zonghcx.pandbg.Pandcytjbean;
import com.zhiren.jt.zdt.zonghcx.pandbg.Pandtjbean;
import com.zhiren.report.Document;
import com.zhiren.report.Paragraph;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class PandbgReport extends BasePage {

	
	public boolean getRaw() {
		return true;
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

//	private int _Flag = 0;

//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '�����еĳ����Ƿ���ʾ'";
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}

//	public void setFlag(int _value) {
//		_Flag = _value;
//	}

	//�������
	private String[] getShul(JDBCcon con) {

		String sql = "select (nvl(zhangmkc,0)-(nvl(benyjm,0)-nvl(fadh,0)-nvl(gongrh,0)-nvl(feiscy,0)-nvl(qity,0)-nvl(diaocl,0)-nvl(cuns,0)-nvl(yuns,0)+nvl(shuifc,0)))as shangykc,\n"
			+ " nvl(benyjm,0) as benyjm,(nvl(fadh,0)+nvl(gongrh,0)+nvl(qity,0)+nvl(cuns,0))as haomhj,nvl(fadh,0) as fadh,nvl(gongrh,0) as gongrh,nvl(qity,0) as qity,nvl(cuns,0) as cuns,\n"
			+ " nvl(yuns,0) as yuns,nvl(diaocl,0) as diaocl,nvl(shuifc,0) as shuifc,nvl(zhangmkc,0) as zhangmkc,nvl(panyk,0) as panyk from pandzmm"
			+ " where pandb_id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		String[] strShul = new String[12];
		strShul = new String[]{"","","","","","","","","","","",""};
		if (rsl.next()) {
//			int columnCount = rsl.getColumnCount();
			for (int i = 0; i < 12; i++) {
				strShul[i] = rsl.getString(i);
			}
		}
		rsl.close();
		return strShul;
	}
	
	private String[][] getPandMxx(JDBCcon con) {
		String[][] meic = null;
		String sql = "select nvl(xuh,0) as xuh,decode(grouping(mingc),1,'�̵��ú�ϼ�',mingc) as mingc,nvl(tij,0) as tij,nvl(mid,0) as mid,sum(nvl(cunml,0)) as cunml from ( \n"
				+"select ('0'||rownum) as xuh,meicb.mingc,nvl(pandtjb.tij,0) as tij,nvl(mid,0) as mid,nvl(cunml,0) as cunml \n"
				+ "from pandtjb,meicb\n "
				+ "where pandtjb.meicb_id=meicb.id and pandb_id="
				+ getPandbID()
				+ "\n"
				+ "union\n"
				+ "select ('1'||rownum) as xuh,pandcmwz.mingc,0 as tij,0 as mid,nvl(cunml,0) as cunml \n"
				+ "from pandwzcmb,pandcmwz \n"
				+ "where pandwzcmb.pandcmwz_id=pandcmwz.id and pandb_id="
				+ getPandbID() + "\n"
				+ " order by mingc ) group by rollup((xuh,mingc,tij,mid)) ";
		ResultSetList rsl = con.getResultSetList(sql);
		int rows = 0;
		if (rsl.getRows() != 0) {
			rows = rsl.getRows();
			meic = new String[rows][4];
		}
		int i = 0;
		while (rsl.next()) {
			if (rsl.getString("xuh").startsWith("0")) {
			}
			meic[i][0] = rsl.getString("mingc");
			meic[i][1] = rsl.getString("tij");
			meic[i][2] = rsl.getString("mid");
			meic[i][3] = rsl.getString("cunml");
			i++;
		}
		rsl.close();
		return meic;
	}
	
	private String[] getShouHCY(JDBCcon con) {

		String sql = "select (nvl(zhangmkc,0)-(nvl(benyjy,0)-nvl(fadh,0)-nvl(gongrh,0)-nvl(qity,0))) as qickc,\n"
					+"       nvl(benyjy,0) as benyjy,nvl(fadh,0)+nvl(gongrh,0)+nvl(qity,0) as haoyhj,nvl(fadh,0) as fadh,\n"
					+"       nvl(gongrh,0) as gongrh,nvl(qity,0) as qity,0 as cunchy,0 as yunshy,0 as diaocyl,0 as shuijtzc,\n"
					+"       nvl(zhangmkc,0) as zhangmkc,nvl(py.cunyl,0) as cunyhj,nvl(panyk,0) as panyk from pandzmy,\n"
					+"	(select sum(round(cunyl))as cunyl from pandyb where pandb_id="
					+ getPandbID() + ")py \n" + " where pandb_id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		
		String[] shouhcy = new String[]{"","","","","","","","","","","","",""};
		if (rsl.next()) {
			int columnCount = rsl.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				shouhcy[i] = rsl.getString(i);
			}
		}
		rsl.close();
		return shouhcy;
	}
	
	private String[][] getCunYxx(JDBCcon con,int maxRows) {
		String[][] youg = null;
		String sql = "select yougb.mingc,round_new(nvl(cunyl,0),2) as cunyl from pandyb,yougb where pandyb.yougb_id=yougb.id \n"
				+ "and pandb_id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		
		int rows = 0;
		if (rsl.getRows() != 0) {
			if(rsl.getRows()<=maxRows){
				rows = rsl.getRows();
			}else{
				rows = maxRows;
			}
			youg = new String[rows][4];
		}
		
		int i = 0;
		while (rsl.next()) {
			youg[i][0] = rsl.getString("mingc");
			youg[i][1] = String.valueOf(rsl.getDouble("cunyl"));
			if(i++>=maxRows){
				break;
			}
		}
		rsl.close();
		return youg;
	}

	private String[] getRenyInfo(JDBCcon con) {
		String[] beiz = null;
		String sql = "select bum,reny,zhiz from pandbmryzzb where pandb_id="
				+ getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() != 0) {
			beiz = new String[rsl.getRows()];
		}
		int i = 0;
		while (rsl.next()) {
			beiz[i++] = "���ţ�" + rsl.getString("bum") + "," + "��Ա��"
					+ rsl.getString("reny") + "," + "ְ��" + rsl.getString("zhiz")
					+",\"\",\"\",\"\",\"\",\"\",\"\",\"\"";
		}
		return beiz;
	}
	
	private String pandgbReport() throws SQLException {
		String strPandsj = "";
		long pandID = -1;
		int pageRows = 43;// ����һҳ�ж�����
		if (getPandsjValue() == null) {// ����ѡ��ȥ��,��ʱ���пյ�ʱ��,����
			setPrintTable("");
			return "";
		} else {
			pandID = getPandsjValue().getId();
		}
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		StringBuffer buf = new StringBuffer();
		StringBuffer strBuf = new StringBuffer();
		String diancm = "";// �糧����
		String time = "";
		String sjsql = "select to_char(p.riq,'yyyy-mm-dd HH24:mi:ss') as riq  from  pandb p where p.id="+pandID;
		ResultSet sjrs = con.getResultSet(sjsql);
		
		if(sjrs.next()){
			time = custom.FormatDate(custom.getDate(sjrs.getString("riq"), "yyyy-MM-dd HH:mm:ss"), "yyyy��MM��dd�� HHʱmm��ss��");
			strPandsj = "to_date('"+sjrs.getString("riq")+"','yyyy-mm-dd HH24:mi:ss')";
		}
		Report rt = new Report();
		List list = new ArrayList();
		String[] strlist = { "", "", "", "", "", "", "", "", "", "" };
		addList(10, list, strlist);// ����10������
		int[] ArrWidth = { 92, 50, 103, 50, 50, 50, 92, 40, 92, 40 };
		buf.append("SELECT QUANC FROM DIANCXXB WHERE ID="+this.getTreeid());
		ResultSet rs = con.getResultSet(buf);

		if (rs.next()) {
			list.add(new String[10]);
			diancm = rs.getString("QUANC");
			((String[]) list.get(10))[0] = "<b>" + diancm + "�̵㱨��</b>";
			strBuf.append("11").append(",");
		}
		rs.close();
		addList(1, list, strlist);// ����1������
		/*******�ж��ǵ糧���ǹ�˾�û�********/
//		if (getShow() == 1) {
			addOne(strBuf, list, 0, "");
//		} else {
//			addList(1, list, strlist);// ����1������
//		}
		
		addOne(strBuf, list, 0, "�̵�ʱ��:" + time);
		addList(19, list, strlist);// ����10������
		
		buf.setLength(0);// �����Ǽ��빤������
		buf.append("SELECT distinct zz.zhiz as gongznr FROM pandbmryzzb zz,pandb p \n");
		buf.append(" WHERE zz.pandb_id=p.id and p.id = " + pandID + changbId(true) +"  GROUP BY zhiz \n");

		rs = con.getResultSet(buf);
		while (rs.next()) {
			buf.setLength(0);
			buf.append("SELECT distinct zz.reny as lury FROM pandbmryzzb zz,pandb p \n");
			buf.append(" WHERE zz.pandb_id=p.id and p.id = " + pandID + changbId(true) +" AND zhiz='" + rs.getString("GONGZNR") + "' GROUP BY reny \n");
			
			ResultSet newRs = con.getResultSet(buf);
			buf.setLength(0);
			buf.append(rs.getString("GONGZNR") + ":&nbsp;&nbsp;");
			while (newRs.next()) {
				buf.append(newRs.getString(1)).append("&nbsp;");
			}
			newRs.close();
			addOne(strBuf, list, 2, buf.toString());
		}
		rs.close();

		// �����Ǽ���μӲ���
		buf.setLength(0);
		buf.append("SELECT distinct zz.bum FROM PANDB p,pandbmryzzb zz where \n");
		buf.append(" p.id = " + pandID + " and zz.pandb_id=p.id \n");
		buf.append(" "+changbId(true));
		buf.append(" order BY BUM");
		rs = con.getResultSet(buf);
		buf.setLength(0);
		buf.append("�μӲ���:&nbsp;&nbsp;");
		while (rs.next()) {
			buf.append(rs.getString("BUM")).append("&nbsp;");
		}
		rs.close();

		addOne(strBuf, list, 2, buf.toString());
		if (list.size() > pageRows) {// ���list��size>43�ͻ��Զ���ȡ
			list = list.subList(0, pageRows - 1);
		} else {
			addList(pageRows - list.size(), list, strlist);
		}
		// ���������õ�һҳ�ı���--------------------�����濪ʼҪ���õڶ�ҳ�ı���------------------------
		
		int zhuNum = 0;
		addList(1, list, strlist);
		if(time!=null && !time.equals("")){
			addOne(strBuf, list, 0, "<b>"+ time.substring(0, 8)+ "��ú(��)�������</b>");
		}else{
			addOne(strBuf, list, 0, "<b>"+ getPandsjValue().getValue().substring(0, 4)+"��"+getPandsjValue().getValue().substring(4, 6)+ "����ú(��)�������</b>");
		}
		addList(1, list, new String[] { "�糧����:" + diancm, "", "","�̵�ʱ�䣺" + time, "", "", "", "", "��λ���֡�������", "" });
		addList(1, list, new String[] { "��ú���", "", "", "", "", "", "�������", "","", "" });
		addList(1, list, new String[] { "�������", "", "�̵����", "", "", "", "�������","", "�̵����", "" });
		addList(1, list, new String[] { "��Ŀ", "����", "��úλ��", "���", "����", "��ú��","��Ŀ", "����", "�͹�", "������" });
		buf.setLength(0);// ���buf
		
		
		int PanmxxRowCount = 12;
		
		String strShul[] = getShul(con);
		String strMeicxx[][] = getPandMxx(con);
		
		if(strMeicxx != null){
			if(strMeicxx.length>=12){
				PanmxxRowCount = strMeicxx.length+1;
			}else{
				PanmxxRowCount = 12;
			}
		}
		
		String strPanmxx[][] = new String[PanmxxRowCount][10];
		for(int i=0;i<strPanmxx.length;i++){
			for(int j=0;j<strPanmxx[i].length;j++){
				strPanmxx[i][j] = "";
			}
		}
		
		strPanmxx[0][0] = "�³�������";
		strPanmxx[1][0] = "���½�ú��";
		strPanmxx[2][0] = "��ú�ϼ�";
		strPanmxx[3][0] = "&nbsp;&nbsp;�����ú";
		strPanmxx[4][0] = "&nbsp;&nbsp;���Ⱥ�ú";
		strPanmxx[5][0] = "&nbsp;&nbsp;������";
		strPanmxx[6][0] = "&nbsp;&nbsp;�������";
		strPanmxx[7][0] = "�������";
		strPanmxx[8][0] = "������";
		strPanmxx[9][0] = "ˮ�ֲ����";
//		strPanmxx[10][0] = "��������ú";
		
		strPanmxx[0][6] = "�³�������";
		strPanmxx[1][6] = "���½�����";
		strPanmxx[2][6] = "���ͺϼ�";
		strPanmxx[3][6] = "&nbsp;&nbsp;�������";
		strPanmxx[4][6] = "&nbsp;&nbsp;���Ⱥ���";
		strPanmxx[5][6] = "&nbsp;&nbsp;������";
		strPanmxx[6][6] = "&nbsp;&nbsp;�������";
		strPanmxx[7][6] = "�������";
		strPanmxx[8][6] = "������";
		strPanmxx[9][6] = "ˮ�ֲ����";
//		strPanmxx[10][6] = "��������ú";
		
		strPanmxx[PanmxxRowCount-2][0] = "�̵�ʱ������";
		strPanmxx[PanmxxRowCount-1][0] = "��ӯ(+)���̿�(-)";
		
		strPanmxx[PanmxxRowCount-2][6] = "�̵�ʱ�������";
		strPanmxx[PanmxxRowCount-2][8] = "�̵���ͺϼ�";
		
		String strPanykqk = "";
		for(int i=0;i<strShul.length-2;i++){
			strPanmxx[i][1]=strShul[i];
		}
		strPanmxx[PanmxxRowCount-2][1]=strShul[strShul.length-2];
		
		if(strMeicxx != null){
			for(int i=0;i<strMeicxx.length-1;i++){
				strPanmxx[i][2]=strMeicxx[i][0];
				strPanmxx[i][3]=strMeicxx[i][1];
				strPanmxx[i][4]=strMeicxx[i][2];
				strPanmxx[i][5]=strMeicxx[i][3];
			}
			strPanmxx[PanmxxRowCount-2][2]=strMeicxx[strMeicxx.length-1][0];
			strPanmxx[PanmxxRowCount-2][5]=strMeicxx[strMeicxx.length-1][3];
		}
		
		String strYouSHC[] = getShouHCY(con);
		for(int i=0;i<strYouSHC.length-3;i++){
			strPanmxx[i][7]=strYouSHC[i];
		}
		strPanmxx[PanmxxRowCount-2][7]=strYouSHC[strYouSHC.length-3];
		strPanmxx[PanmxxRowCount-2][9]=strYouSHC[strYouSHC.length-2];
		
		strPanykqk = "ú��"+strShul[strShul.length-1]+"/�ͣ�"+strYouSHC[strYouSHC.length-1];
		strPanmxx[PanmxxRowCount-1][2]=strPanykqk;
		
		String strCunyxx[][] = getCunYxx(con,PanmxxRowCount-2);
		if(strCunyxx != null){
			for(int i=0;i<strCunyxx.length;i++){
				strPanmxx[i][8]=strCunyxx[i][0];
				strPanmxx[i][9]=strCunyxx[i][1];
			}
		}
		
		for(int i=0;i<strPanmxx.length;i++){
			addList(1, list, strPanmxx[i]);
		}

		// ӯ����������
		zhuNum = list.size();
		addList(1, list, new String[] {"ӯ�����������","","","","","","","","",""});
		addList(1, list, strlist);
		addList(1, list, strlist);
		addList(1, list, new String[] {"��ע˵����","","","","","","","","",""});
		addList(1, list, strlist);
		addList(1, list, strlist);
		addList(1, list, new String[] { "�μ��̵���Ա��", "", "", "", "", "", "", "", "", "" });
		addList(1, list, strlist);
		/*
//		String s = "";
		String strReny[] = getRenyInfo(con);
		if (strReny != null) {
			for (int i = 0; i < strReny.length; i++) {
				addList(1, list, new String[] {strReny[i]});
//				s += strReny[i] + "<br>";
			}
//			addList(1, list, new String[] {s});
		}else{
			addList(1, list, strlist);
		}*/
		
		addList(1, list, new String[] { "���ܣ�", "", "", "��ˣ�", "", "","", "�Ʊ�", "", ""});
		
		if (list.size() > 2*pageRows) {// ���list��size>2*43�ͻ��Զ���ȡ
			list = list.subList(0, 2*pageRows-1);
		} else {
			addList(2*pageRows - list.size(), list, strlist);
		}

		// ���������õڶ�ҳ�ı���--------------------�����濪ʼҪ���õ���ҳ�ı���------------------------
		addList(1, list, strlist);
		addOne(strBuf, list, 0, "<b>��ĩ��ú���ͣ������</b>");

		addList(1, list, new String[] { tableValue(diancm, strPandsj), "", "",
				"", "", "", "", "", "", "" });

		if (list.size() > 3 * pageRows) {// ���list��size>2*43�ͻ��Զ���ȡ
			list = list.subList(0, 3 * pageRows-1);
		} else {
			addList(3 * pageRows - list.size(), list, strlist);
		}
		// ���������õ���ҳ�ı���--------------------�����濪ʼҪ���õ���ҳ�ı���---------------------------
		addList(1, list, strlist);

		addOne(strBuf, list, 0, "<b>���ܶȲ�������</b>");
		addList(1, list, new String[] { "�糧����:" + diancm, "", "", "", "", "",
				"�ⶨʱ�䣺" + time, "", "", "" });
		addList(1, list, new String[] { midTable(diancm, strPandsj), "", "",
				"", "", "", "", "", "", "" });

//		������û�е���ҳ������
//		if (list.size() > 3 * pageRows) {// ���list��size>3*43�ͻ��Զ���ȡ
//			list = list.subList(0, 3 * pageRows-1);
//		} else {
//			addList(3 * pageRows - list.size(), list, strlist);
//		}
//		�������е���ҳʱ������
		if (list.size() > 4 * pageRows) {// ���list��size>4*43�ͻ��Զ���ȡ
			list = list.subList(0, 4 * pageRows-1);
		} else {
			addList(4 * pageRows - list.size(), list, strlist);
		}

		// ���������õ���ҳ�ı���--------------------�����濪ʼҪ���õ���ҳ�Ժ�ı���------------------------
		buf.setLength(0);
//		buf.append("SELECT T.MINGC \n");
//		buf.append("  FROM PANDTJB T,PANDB P\n");
//		buf.append(" WHERE T.PANDB_ID = P.ID AND P.ID=" + getPandbID());

		buf.append("select * from pandb,pandtb where pandb.riq=pandtb.riq\n" +
		" and pandb.diancxxb_id=pandtb.diancxxb_id and pandb.id="+ getPandbID());

//		buf.append(changbId(true));
		ResultSetList mapRs = con.getResultSetList(buf.toString());
		String ddd = "";
		if(mapRs.getRows()==0){//û�����ݾ����һ������
			addList(1, list, strlist);
		}
		for(int i=0;mapRs.next();i++) {
			String strPanmt = mapRs.getString("fuj");
			addList(1, list, strlist);
			if(strPanmt != null && !strPanmt.equals("")){//ͼƬ��Ϊ��
				addList(1, list, new String[] {
						strPanmt.substring(strPanmt.lastIndexOf("/")+1, strPanmt.indexOf(".")), "", "", "",
						"", "", "", "", "", "" });
				ddd = ddd + list.size() + ",";
				addList(1, list, new String[] {
						"<p><img name='Picture"+i+"' " + "src='" + getcontext()
								+ "/" +this.getTreeid()+"/"
								+ strPanmt
								+ "' width = '686' /></p>", "", "", "", "", "", "",
						"", "", "" });
				addList(40, list, strlist);
			}
			
		}
		mapRs.close();
		
		
		// ���������б���_______________________________--------------------__________________����

		// ���������뵽���㷨����ô�ã����Ǻ�ʡ��Ŷ���ٺ�!
		String[][] strList = new String[list.size()][10];
		for (int i = 0; i < strList.length; i++) {
			strList[i] = (String[]) list.get(i);
//			System.out.println(i);
		}

		rt.setBody(new Table(strList, 0, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(pageRows);

		// �����濪ʼ���ñ��ϵı߿�

		rt.body.setBorderNone();
		for (int i = 1; i <= 10; i++) {
			rt.body.setColCells(i, Table.PER_BORDER_BOTTOM, 0);
			rt.body.setColCells(i, Table.PER_BORDER_RIGHT, 0);
		}

		// �����濪ʼ��������
		// ���������ö��뷽ʽ.
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(7, Table.ALIGN_LEFT);
		rt.body.setColAlign(9, Table.ALIGN_LEFT);
		rt.body.setRowCells(pageRows + 3, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setCellAlign(pageRows + 3, 9, Table.ALIGN_RIGHT);

//		û�е���ҳʱ������		
//		rt.body.setCellAlign(2 * pageRows + 3, 7, Table.ALIGN_RIGHT);
//		�е���ҳʱ������
		rt.body.setCellAlign(3 * pageRows + 3, 7, Table.ALIGN_RIGHT);

		rt.body.setRowCells(pageRows + 4, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setRowCells(pageRows + 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setRowCells(pageRows + 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCellAlign(zhuNum, 3, Table.ALIGN_CENTER);
		
//		û�е���ҳʱ������
//		rt.body.setCellAlign(2 * pageRows + 4, 1, Table.ALIGN_CENTER);
//		�е���ҳʱ������
		rt.body.setCellAlign(3 * pageRows + 4, 1, Table.ALIGN_CENTER);
		
		
		// �����濪ʼ���úϲ�
		rt.body.mergeCell(pageRows + 3, 1, pageRows + 3, 4);
		
//		û�е���ҳʱ������
//		rt.body.mergeCell(2 * pageRows + 3, 1, 2 * pageRows + 3, 4);
//		�е���ҳʱ������
		rt.body.mergeCell(3 * pageRows + 3, 1, 3 * pageRows + 3, 4);

		rt.body.mergeCell(pageRows + 3, 5, pageRows + 3, 8);
		rt.body.mergeCell(pageRows + 3, 9, pageRows + 3, 10);
		
//		û�е���ҳʱ������
//		rt.body.mergeCell(2 * pageRows + 3, 7, 2 * pageRows + 3, 10);
//		�е���ҳʱ������
		rt.body.mergeCell(3 * pageRows + 3, 7, 3 * pageRows + 3, 10);

		rt.body.mergeCell(pageRows + 4, 1, pageRows + 4, 6);
		rt.body.mergeCell(pageRows + 4, 7, pageRows + 4, 10);
		rt.body.mergeCell(pageRows + 5, 1, pageRows + 5, 2);
		rt.body.mergeCell(pageRows + 5, 3, pageRows + 5, 6);
		rt.body.mergeCell(pageRows + 5, 7, pageRows + 5, 8);
		rt.body.mergeCell(pageRows + 5, 9, pageRows + 5, 10);

		rt.body.mergeCell(zhuNum-1, 3, zhuNum-1, 5);
		rt.body.mergeCell(zhuNum, 1, zhuNum, 2);
		rt.body.mergeCell(zhuNum, 3, zhuNum, 10);
		rt.body.mergeCell(zhuNum + 1, 1, zhuNum + 1, 10);
		rt.body.mergeCell(zhuNum + 4, 1, zhuNum + 4, 10);
		rt.body.mergeCell(zhuNum + 7, 1, zhuNum + 7, 10);
		rt.body.mergeCell(zhuNum + 8, 1, zhuNum + 8, 10);
		
		
//		û�е���ҳʱ������		
//		rt.body.mergeCell(2 * pageRows + 4, 1, 3 * pageRows, 10);
//		�е���ҳʱ������
		rt.body.mergeCell(2 * pageRows + 3, 1, 3 * pageRows, 10);
		rt.body.mergeCell(3 * pageRows + 4, 1, 4 * pageRows, 10);

		// �������ñ߿�
		rt.body.setCellBorder(pageRows + 4, 1, 2, 2, 2, 2);
		rt.body.setCellBorder(pageRows + 4, 7, 0, 2, 2, 2);
		rt.body.setCellBorder(pageRows + 5, 1, 2, 2, 0, 1);
		rt.body.setCellBorder(pageRows + 5, 1, 2, 1, 0, 1);
		rt.body.setCellBorder(pageRows + 5, 3, 0, 2, 0, 1);
		rt.body.setCellBorder(pageRows + 5, 7, 0, 1, 0, 1);
		rt.body.setCellBorder(pageRows + 5, 9, 0, 2, 0, 1);

		for (int i = pageRows + 6; i < zhuNum; i++) {
			rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 1);
			rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 1);

			rt.body.setCellBorderLeft(i, 1, 2);
			rt.body.setCellBorderRight(i, 6, 2);
			rt.body.setCellBorderRight(i, 10, 2);
		}
		rt.body.setRowCells(zhuNum - 1, Table.PER_BORDER_BOTTOM, 2);
		rt.body.setRowCells(zhuNum, Table.PER_BORDER_BOTTOM, 2);
		rt.body.setCellBorderLeft(zhuNum, 1, 2);
		rt.body.setCellBorderLeft(zhuNum, 3, 1);
		rt.body.setCellBorderRight(zhuNum, 3, 2);
		rt.body.setCellBorder(zhuNum + 1, 1, 2, 2, 0, 0);
		rt.body.setCellBorderLeft(zhuNum + 2, 1, 2);
		rt.body.setCellBorderRight(zhuNum + 2, 10, 2);
		rt.body.setCellBorderLeft(zhuNum + 3, 1, 2);
		rt.body.setCellBorderRight(zhuNum + 3, 10, 2);
		rt.body.setCellBorder(zhuNum + 4, 1, 2, 2, 2, 0);
		rt.body.setCellBorderLeft(zhuNum + 5, 1, 2);
		rt.body.setCellBorderRight(zhuNum + 5, 10, 2);
		rt.body.setCellBorderLeft(zhuNum + 6, 1, 2);
		rt.body.setCellBorderRight(zhuNum + 6, 10, 2);
		rt.body.setCellBorder(zhuNum + 7, 1, 2, 2, 2, 0);
		rt.body.setCellBorder(zhuNum + 8, 1, 2, 2, 0, 2);

		String[] listSplit = strBuf.toString().split(",");
		for (int i = 0; i < listSplit.length; i++) {
			int num = Integer.parseInt(listSplit[i]);
			// �����濪ʼ��������
			rt.body.setRowCells(num, Table.PER_FONTSIZE, 22);
			// ���������ö��뷽ʽ

			// �����濪ʼ���úϲ�

			if (num < pageRows && num > 31 && num < 2 * pageRows) {
				rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_LEFT);
				rt.body.setRowCells(num, Table.PER_FONTSIZE, 12);
				rt.body.mergeCell(num, 3, num, 10);
			} else if (num < 31 && num < pageRows || num > 2 * pageRows) {
				rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_CENTER);
				rt.body.mergeCell(num, 1, num, 10);
			} else if (num > pageRows && (num - pageRows) == 2) {
				rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_CENTER);
				rt.body.mergeCell(num, 1, num, 10);
			}
		}
		if (!(ddd == "")) {
			String[] dddd = ddd.split(",");
			for (int i = 0; i < dddd.length; i++) {
				int num = Integer.parseInt(dddd[i]);
				rt.body.mergeCell(num, 1, num, 10);
				rt.body.setRowCells(num, Table.PER_ALIGN, Table.ALIGN_CENTER);
				rt.body.setRowCells(num, Table.PER_FONTSIZE, 22);
				rt.body.mergeCell(num + 1, 1, num + 40, 10);
			}
		}

		rt.body.setRowCells(14, Table.PER_FONTSIZE, 16);

		con.Close();
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();// ph;
	}

	public List getList() {

		if (getPandsjValue() == null) {// ����ѡ��ȥ��,��ʱ���пյ�ʱ��,����
			return null;
		}
		List list = new ArrayList();
		list.add(new Pandtjbean(-1, 1, "&nbsp;", 0, 0));
		list.add(new Pandtjbean(-1, 2, "���½�ú(��)��", 0, 0));
		list.add(new Pandtjbean(-1, 3, "���º�ú(��)��", 0, 0));
		list.add(new Pandtjbean(-1, 4, "&nbsp;&nbsp;�����", 0, 0));
		list.add(new Pandtjbean(-1, 5, "&nbsp;&nbsp;���Ⱥ�", 0, 0));
		list.add(new Pandtjbean(-1, 6, "&nbsp;&nbsp;������", 0, 0));
		list.add(new Pandtjbean(-1, 7, "&nbsp;&nbsp;�������", 0, 0));
		list.add(new Pandtjbean(-1, 8, "�����������", 0, 0));
		list.add(new Pandtjbean(-1, 9, "���µ�����", 0, 0));
		list.add(new Pandtjbean(-1, 10, "����ˮ�ֲ����", 0, 0));
		list.add(new Pandtjbean(-1, 11, "��ĩ24ʱ������", 0, 0));
		JDBCcon con = new JDBCcon();
		CustomDate custom = new CustomDate();
		StringBuffer buf = new StringBuffer();
		
//		��ʼ��һЩ����
		((Pandtjbean) list.get(1)).setShulm(0);// ����ú��ʵ����
		((Pandtjbean) list.get(7)).setShulm(0);// ����ú������
		
		((Pandtjbean) list.get(1)).setShuly(0);// �͵�������
		
		((Pandtjbean) list.get(2)).setShuly(0);
		((Pandtjbean) list.get(3)).setShuly(0);
		((Pandtjbean) list.get(4)).setShuly(0);
		((Pandtjbean) list.get(5)).setShuly(0);
		
		((Pandtjbean) list.get(10)).setShuly(0);// �̵�ʱ�����棨�ͣ�
		try {
			// �����濪ʼ��ú��
			buf.setLength(0);// ���buf//��������ʵ�պ�����
			buf.append("SELECT NVL(ROUND_NEW(SUM(f.BIAOZ),0)+ROUND_NEW(sum(f.YINGD),0)-ROUND_NEW(sum(f.yingk),0),0) SHISL,\n");
			buf.append("       NVL(ROUND_NEW(SUM(f.YUNS),0),0) YUNS  \n");
			buf.append("FROM   FAHB F,\n");
			buf.append("       MEIKXXB M\n");
			buf.append(" WHERE TO_CHAR(daohrq,'YYYYMM') = '"+ getPandsjValue().getValue().substring(0, 6) + "'\n");
			buf.append("   AND F.MEIKXXB_ID = M.ID\n");
//			buf.append("   AND M.TONGJBZ != 0\n");
			buf.append(changbId("F"));
			ResultSet rsShis = con.getResultSet(buf);
			rsShis.next();// �����Ǹ�sql���һ������ֵ��//�α������ƶ�
			((Pandtjbean) list.get(1)).setShulm(rsShis.getInt("SHISL"));// ����ú��ʵ����
			((Pandtjbean) list.get(7)).setShulm(rsShis.getInt("YUNS"));// ����ú������
			rsShis.close();// �ص������Ǹ�sql
			
			
			buf.setLength(0);// ��պ���Ҫ��Ҫ����
			buf.append("SELECT SUM(FADY + GONGRY + QITY + CUNS) HEJ,\n");
			buf.append("       SUM(FADY) FADY,\n");
			buf.append("       SUM(GONGRY) GONGRY,\n");
			buf.append("       SUM(QITY) QITY,\n");
			buf.append("       SUM(CUNS) CHUS,\n");
			buf.append("       SUM(DIAOC) DIAOC,\n");
			buf.append("       SUM(SHUIFCTZ) SHUIFCTZ\n");
			buf.append("  FROM shouhcrbb\n");
			buf.append(" WHERE TO_CHAR(RIQ,'YYYYMM') = '"
					+ getPandsjValue().getValue().substring(0, 6) + "'\n");
			buf.append(changbId(true));
			ResultSet mRs = con.getResultSet(buf);
			mRs.next();// ָ�������ƶ�һ��
			((Pandtjbean) list.get(2)).setShulm(mRs.getInt("HEJ"));// ���º�ú��
			((Pandtjbean) list.get(3)).setShulm(mRs.getInt("FADY"));// �����
			((Pandtjbean) list.get(4)).setShulm(mRs.getInt("GONGRY"));// ���Ⱥ�
			((Pandtjbean) list.get(5)).setShulm(mRs.getInt("QITY"));// ������
			((Pandtjbean) list.get(6)).setShulm(mRs.getInt("CHUS"));// �������
			((Pandtjbean) list.get(8)).setShulm(mRs.getInt("DIAOC"));// ������
			((Pandtjbean) list.get(9)).setShulm(mRs.getInt("SHUIFCTZ"));// ˮ�ֲ����
			mRs.close();// �ص�mRs

			buf.setLength(0);// ���buf
			buf.append("SELECT KUC FROM SHOUHCRBB\n");
			buf.append("WHERE RIQ = ADD_MONTHS("+ custom.OraDateFormat(
				custom.getMonthFirstDate(getPandsjValue().getValue().substring(0, 4)
						+"-"+getPandsjValue().getValue().substring(4, 6)
						+"-"+getPandsjValue().getValue().substring(6, 8)),
				false) + ",1)-1");
			// ���ϱ�������δ�ķ������ȵõ�ÿ���µĵ�һ�죬��һ���£���һ��Ϊ��ĩ
			buf.append(changbId(true));// �õ�ÿ����ĩ��ʵ�ʿ��
			ResultSet kucRs = con.getResultSet(buf);
			if (kucRs.next()) {// ָ�������ƶ�һ��
				((Pandtjbean) list.get(10)).setShulm(kucRs.getInt("KUC"));// ���������
			}
			kucRs.close();// ����������ֱ�������ݿ���ȡ��

			// _______________������ú�����ݼ������_________________________
//			buf.setLength(0);// ���
//			buf.append("SELECT SUM(SHOURL) SHOURL FROM LAIYQKB\n");
//			buf.append("WHERE  TO_CHAR(RIQ,'YYYYMM') = '"
//					+ getPandsjValue().getValue().substring(0, 6) + "'");
//			buf.append(changbId(true));
//			ResultSet shourlRs = con.getResultSet(buf);
//			shourlRs.next();// ���½���
//			((Pandtjbean) list.get(1)).setShuly(shourlRs.getInt("SHOURL"));// �͵�������
//			shourlRs.close();// �ص�
			
//			buf.setLength(0);// ���
//			buf.append("SELECT SUM(FADYY) FADYY, SUM(GONGRYY) GONGRYY,\n");
//			buf.append("SUM(QITYY) QITYY,SUM(FADYY+GONGRYY+QITYY) HEJ\n");
//			buf.append("FROM HAOYQKYB WHERE RIQ <= "
//					+ custom.OraDateFormat(getPandsjValue().getValue().substring(0, 4)
//							+"-"+getPandsjValue().getValue().substring(4, 6)
//							+"-"+getPandsjValue().getValue().substring(6, 8), false));
//			buf.append(" AND TO_CHAR(RIQ,'YYYYMM') = '"
//					+ getPandsjValue().getValue().substring(0, 6) + "'");
//			buf.append(changbId(true));
//			buf.append(" AND BANZH = '�ϼ�'");
//			ResultSet haoyRs = con.getResultSet(buf);
//			haoyRs.next();
//			buf.setLength(0);
//			((Pandtjbean) list.get(2)).setShuly(haoyRs.getInt("HEJ"));
//			((Pandtjbean) list.get(3)).setShuly(haoyRs.getInt("FADYY"));
//			((Pandtjbean) list.get(4)).setShuly(haoyRs.getInt("GONGRYY"));
//			((Pandtjbean) list.get(5)).setShuly(haoyRs.getInt("QITYY"));

//			buf.append("SELECT BENRKC FROM RANLKCQK\n");
//			buf.append("WHERE RIQ = ADD_MONTHS("
//					+ custom.OraDateFormat(custom
//							.getMonthFirstDate(getPandsjValue().getValue().substring(0, 4)
//									+"-"+getPandsjValue().getValue().substring(4, 6)
//									+"-"+getPandsjValue().getValue().substring(6, 8)),
//							false) + ",1)-1");
			// ���ϱ�������δ�ķ������ȵõ�ÿ���µĵ�һ�죬��һ���£���һ��Ϊ��ĩ
//			buf.append(changbId(true));
//			buf.append("AND PINZ = '��'");
//
//			ResultSet benrkcRsY = con.getResultSet(buf);
//			if (benrkcRsY.next()) {
//				((Pandtjbean) list.get(10))
//						.setShuly(benrkcRsY.getInt("BENRKC"));// �̵�ʱ�����棨�ͣ�
//
//			}
//			benrkcRsY.close();
			// �����������ˡ�
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return list;
	}
	
	public long getPandbID() {
		int id = -1;
		if (getPandsjValue() == null) {
			return id;
		}
		return getPandsjValue().getId();
	}
	public String getRiq(JDBCcon con) {
		String sDate = "";
		String sql = "select riq from pandb where id=" + getPandbID();
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			sDate = DateUtil.Formatdate("yyyy �� MM �� dd ��", rsl.getDate("riq"));
		}
		if (sDate.equals("")) {
			sDate = DateUtil.Formatdate("yyyy �� MM �� dd ��", new Date());
		}
		return sDate;

	}
	
	
	public String getcontext() {
		JDBCcon con = new JDBCcon();
		String filePath = "http://"
				+ this.getRequestCycle().getRequestContext().getServerName()
				+ ":"
				+ this.getRequestCycle().getRequestContext().getServerPort();
		
		String sql = "select zhi from xitxxb where mingc='�̵㱨��ͼƬ·��'";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			filePath = filePath	+ "/" + rsl.getString("zhi");
		}
		rsl.close();
		con.Close();
		
		return filePath;
	}

	/**
	 * @author Yangzong
	 * @change �޸Ķ����Ȩƽ��������
	 * @sina 2008-10-09
	 * @param str
	 * @param strPandsj
	 * @return
	 * @throws SQLException
	 */
	private String midTable(String str, String strPandsj) throws SQLException {
		StringBuffer buf = new StringBuffer();
		JDBCcon con = new JDBCcon();
		StringBuffer bufSql = new StringBuffer();
		bufSql.append("SELECT ID,mingc as MEICMC FROM MEICB where diancxxb_id="+this.getTreeid());
		ResultSet rs = con.getResultSet(bufSql);
		while (rs.next()) {
			bufSql.setLength(0);
			bufSql.append("SELECT ROWNUM AS ID,NVL(CED,'0') ");
			bufSql.append("CED,YANGPZL,CEDJJ FROM pandmdb,pandb p");
			bufSql.append(" WHERE pandb_id=p.id and p.id="+getPandbID()+" and meicb_id=" + rs.getLong("id"));
			bufSql.append(changbId(true));
			bufSql.append("UNION\n");
			bufSql.append("SELECT 66 AS ID,NVL('','��Ȩƽ��') CED,");
			bufSql.append("ROUND_NEW(NVL(AVG(YANGPZL),0),4) YANGPZL,"
					+ "ROUND_NEW(NVL(AVG(CEDJJ),0),0) CEDJJ\n");
			bufSql.append("FROM pandmdb,pandb p WHERE pandb_id=p.id and p.id="+getPandbID()+" and meicb_id=" + rs.getLong("id"));
			bufSql.append(changbId(true));// ȥ������ӵ�group by
			ResultSet bRs = con.getResultSet(bufSql);
			boolean flag = false;
			bufSql.setLength(0);
			String firstStr = firstTd("���", 230, "center")
					+ middleTd(230, "ú������(t)", "center")
					+ middleTd(230, "�����(m)", "center") + "</tr>";
			while (bRs.next()) {
				bufSql.append(firstTd(bRs.getString(2), 230, "center"));
				bufSql.append(middleTd(230, bRs.getString(3), "right"));
				bufSql.append(middleTd(230, bRs.getString(4), "right"));
				bufSql.append("</tr>");
				flag = true;
			}
			bRs.close();
			if (flag) {
				String sql = "SELECT nvl(AVG(MID),0) MID FROM pandtjb,pandb p"
						+ " WHERE PANDB_ID=p.id and p.id=" + getPandbID()+" and meicb_id="+rs.getLong("id")
						+ changbId(true);
				ResultSet midRs = con.getResultSet(sql);
				if (midRs.next()) {
					bufSql.append(lastTr(midRs.getString("MID")));
				}
				midRs.close();
				buf.append("<br>");
				buf.append(tableMid(rs.getString("MEICMC")));
				buf.append(firstStr);
				buf.append(bufSql);
				buf.append("</table>");
			}
		}
		rs.close();
		con.Close();

		return buf.toString();
	}

	private String lastTr(String str) {
		StringBuffer buf = new StringBuffer();
		buf.append("<tr height='21'>\n");
		buf.append("<td width='230'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>�ܶ�(��/m<sup>3)</td>\n");
		buf.append("<td width='460' colspan = 2 align='right' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td></tr>\n");

		return buf.toString();
	}

	private String firstTd(String str, int num, String align) {
		StringBuffer buf = new StringBuffer();
		buf.append("<tr height='21'>\n");
		buf.append("<td width='" + num + "'  align='" + align + "' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td>\n");

		return buf.toString();
	}

	private String tableMid(String str) {
		StringBuffer buf = new StringBuffer();
		buf.append("<table width='690' cellpadding='0' cellspacing='0'"
				+ " style='font-family:����;font-size:9pt;border-left:2px "
				+ "solid rgb(0,0,0);border-top:2px solid rgb(0,0,0);"
				+ "border-right:1px solid rgb(0,0,0);border-bottom:1px "
				+ "solid rgb(0,0,0);'>");
		buf.append("<tr height='21'>\n");
		buf.append("<td width='690' colspan='3'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td>\n");
		buf.append("</tr>");
		return buf.toString();
	}

	private String tableValue(String str, String strPandsj) throws SQLException {
		StringBuffer buf = new StringBuffer();
		buf.append("<table width='620' cellpadding='0' cellspacing='0'"
				+ " style='font-family:����;font-size:9pt;border-left:0px "
				+ "solid rgb(0,0,0);border-top:0px solid rgb(0,0,0);"
				+ "border-right:0px solid rgb(0,0,0);border-bottom:1px "
				+ "solid rgb(0,0,0);'>");
		buf.append("<tr height='21'>\n");
		buf.append("<td width='310' colspan='3' class='tdNoneLeft'>�糧����:");
		buf.append(str);
		buf.append("</td>\n");
		buf.append("<td width='310' colspan='3' align='right' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:0px ");
		buf.append("solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);3");
		buf.append("'>��λ���֡�������</td>\n");
		buf.append("</tr>");

		buf.append("<tr height='21'>\n");
		buf.append("<td width='310' colspan='3' align='center' style='");
		buf.append("padding-right:2; border-left:2px solid rgb(0,0,0); ");
		buf.append("border-top:2px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>��ĩ��ú���ͣ�����</td>\n");
		buf.append("<td width='310' colspan='3' align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:2px solid rgb(0,0,0); border-right:2px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>�¶��̵�����ϼ�</td>\n");
		buf.append("</tr>");

		buf.append("<tr height='21'>\n");
		buf.append("<td width='150' rowspan='2' align='center' style='");
		buf.append("padding-right:2; border-left:2px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>��Ŀ</td>\n");
		buf.append("<td width='160' colspan='2' align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>����</td>\n");
		buf.append("<td width='150' rowspan='2' align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>��Ŀ</td>\n");
		buf.append("<td width='160' colspan='2' align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:2px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>����</td>\n");
		buf.append("</tr>");

		buf.append("<tr height='21'>\n");
		buf.append("<td width='80'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>ú</td>\n");
		buf.append("<td width='80'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>��</td>\n");
		buf.append("<td width='80'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>ú</td>\n");
		buf.append("<td width='80'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:2px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>��</td>\n");
		buf.append("</tr>");
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer();

		String strCunmxm[] = new String[12];
		
		strCunmxm[0] = "�̵�ʱʵ�ʿ��";
		strCunmxm[1] = "�̵���ú(��)��";
		strCunmxm[2] = "�̵���ú(��)��";
		strCunmxm[3] = "  �����";
		strCunmxm[4] = "  ���Ⱥ�";
		strCunmxm[5] = "  ������";
		strCunmxm[6] = "�̵�󴢴����";
		strCunmxm[7] = "�̵���������";
		strCunmxm[8] = "�̵�������";
		strCunmxm[9] = "�̵��ˮ�ֲ����";
		strCunmxm[10] = "��ĩʵ�ʿ��";
		strCunmxm[11] = "��ӯ(+)���̿�(-)";
		
//		strCunmxm[12] = "��ĩ������";
		
		buffer.append("SELECT ID,NVL(YINGKQKFX,'') YINGKQKFX,NVL(BEIZ,' ')\n");
		buffer.append(" BEIZ  FROM YUEMCMYZB WHERE RIQ = " + strPandsj);
		buffer.append(changbId(true));
		ResultSet rs = con.getResultSet(buffer);
		buffer.setLength(0);// ���buf
//		buffer.append("SELECT ID,NVL(YINGKQKFX,'') YINGKQKFX,NVL(BEIZ,' ')\n");
//		buffer.append(" BEIZ  FROM PANMYQKBGZB WHERE RIQ = " + strPandsj);
//		buffer.append(changbId(true));
//		ResultSet mRs = con.getResultSet(buffer);

//		if (mRs.next()) {
//			buffer.setLength(0);
		
			String msql = "select (nvl(zhangmkc,0)-(nvl(benyjm,0)-nvl(fadh,0)-nvl(gongrh,0)-nvl(feiscy,0)-nvl(qity,0)-nvl(diaocl,0)-nvl(cuns,0)-nvl(yuns,0)+nvl(shuifc,0)))as shangykc,\n"
						+ " nvl(zhangmkc,0) as zhangmkc from pandzmm"
						+ " where pandb_id=" + getPandbID();
			
			String ysql = "select (nvl(zhangmkc,0)-(nvl(benyjy,0)-nvl(fadh,0)-nvl(gongrh,0)-nvl(qity,0))) as qickc,\n"
						+"       nvl(zhangmkc,0) as zhangmkc from pandzmy where pandb_id=" + getPandbID();
		
			ResultSet shulm = con.getResultSet(msql);
			ResultSet shuly = con.getResultSet(ysql);
			String qickcm = "";
			String zhangmkcm = "";
			String qickcy = "";
			String zhangmkcy = "";
			
			if(shulm.next()){
				qickcm = shulm.getString("shangykc");
				zhangmkcm = shulm.getString("zhangmkc");
			}
			if(shuly.next()){
				qickcy = shuly.getString("qickc");
				zhangmkcy = shuly.getString("zhangmkc");
			}
			buf.append(firstTd("�̵�ʱ������"));
			buf.append(middleTd(80, zhangmkcm, "center"));
			buf.append(middleTd(80, zhangmkcy, "center"));
			buf.append(middleTd(150, "�³�������", "left"));
			buf.append(middleTd(80, qickcm, "center"));
			buf.append(lastTd(qickcy));
//		}
		
		try {

			if (rs.next()) {
				buffer.setLength(0);
				buffer.append("SELECT id, yuemcmyzb_id, pandssjkc, pandhjmyl, pandhhmyl, fadh, gongrh, qith, pandhccsh, pandhyssh, pandhdcl, pandhsfctz, yuemsjkc,  panyk,"
						+" pandssjkc_m, pandhjmyl_m, pandhhmyl_m, fadh_m, gongrh_m, qith_m, pandhccsh_m, pandhyssh_m, pandhdcl_m, pandhsfctz_m, yuemsjkc_m,  panyk_m FROM YUEMCMYB WHERE YUEMCMYZB_ID = " + rs.getLong("ID"));
				ResultSet shulRs = con.getResultSet(buffer);
				List getlist = getList();
				int numl = 0;
				String strPanyk_m = "0";
				String strPanyk = "0";
//				int j = 0;
				if (shulRs.next()) {
					strPanyk_m = shulRs.getString("panyk_m");
					strPanyk = shulRs.getString("panyk");
					
					for (int  j=0;j < 11;j++){
						buf.append(firstTd(strCunmxm[j].replaceAll("  ", "&nbsp;&nbsp;")));
						buf.append(middleTd(80, shulRs.getString(15+j),"center"));
						buf.append(middleTd(80, shulRs.getString(3+j),"center"));
						buf.append(middleTd(150, ((Pandtjbean) getlist.get(numl)).getXiangm(), "left"));
						buf.append(middleTd(80, ""+ ((Pandtjbean) getlist.get(numl)).getShulm(),"center"));
						buf.append(lastTd(""+ ((Pandtjbean) getlist.get(numl)).getShuly()));
						numl++;
					}
//					j++;
				}
				buf.append(firstTd(strCunmxm[11].replaceAll("  ","&nbsp;&nbsp;")));
				buf.append("<td width='470'  align='center' colspan='5' style='");
				buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
				buf.append("border-top:0px solid rgb(0,0,0); border-right:2px ");
				buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
				buf.append("'>ú:" + strPanyk_m + "\\��:"+ strPanyk + "</td>\n");

				buf.append(firstTd("��ע˵����"));
				buf.append("<td width='470'  align='center' colspan='5' style='");
				buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
				buf.append("border-top:0px solid rgb(0,0,0); border-right:2px ");
				buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
				buf.append("'>&nbsp;" + "</td>\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		buf.append("</table>");
		return buf.toString();
	}

	private String firstTd(String str) {
		StringBuffer buf = new StringBuffer();
		buf.append("<tr height='21'>\n");
		buf.append("<td width='150'  align='left' style='");
		buf.append("padding-right:2; border-left:2px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td>\n");

		return buf.toString();
	}

	private String middleTd(int width, String str, String align) {
		StringBuffer buf = new StringBuffer();
		buf.append("<td width='" + width + "'  align='" + align + "' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:1px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td>\n");

		return buf.toString();
	}

	private String lastTd(String str) {
		StringBuffer buf = new StringBuffer();
		buf.append("<td width='80'  align='center' style='");
		buf.append("padding-right:2; border-left:0px solid rgb(0,0,0); ");
		buf.append("border-top:0px solid rgb(0,0,0); border-right:2px ");
		buf.append("solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);3");
		buf.append("'>" + str + "</td></tr>\n");

		return buf.toString();
	}

	/**
	 * @author Yangzong
	 * @param list
	 *            ����һ��list
	 * @param add
	 *            Ҫ�ڵڼ�����
	 * @param str
	 *            ����Ķ���
	 */
	private void addOne(StringBuffer strBuf, List list, int add, String str) {
		String[] strList = { "", "", "", "", "", "", "", "", "", "" };
		strList[add] = str;
		list.add(strList);
		strBuf.append(list.size()).append(",");
	}

	/**
	 * @author Yangzong
	 * @param list
	 *            ����һ��list
	 * @param add
	 *            Ҫ�ڵڼ�����
	 * @param str
	 *            ����Ķ���
	 */
	private void addOne(List list, int add, String str) {
		String[] strList = { "", "", "", "", "", "", "", "", "", "" };
		strList[add] = str;
		list.add(strList);
	}

	/**
	 * @author Yangzong
	 * @since 2008-08-25
	 * @param size
	 *            Ҫ���뼸��
	 * @param list
	 *            Ҫ���ӵ�list
	 * @param strlist
	 *            ��ʲô����
	 */
	private void addList(int size, List list, String[] strlist) {
		for (int i = 0; i < size; i++) {// �õ��Ǹı����������ǻ�ı�ԭ���Ķ���ķ���
			list.add(strlist);
		}
	}

	private boolean blnIsBegin = false;

	private String printTable = "";

	private void setPrintTable(String str) {
		printTable = str;
	}

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		setPrintTable("");
		blnIsBegin = false;
		try {
			return pandgbReport();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			blnIsBegin = true;
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	// ú��ʱ��������
	/**
	 * @author Yangzong
	 * @return
	 */

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
//			visit.setEditValues(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			this.setTreeid(null);
		}

		if (treeChange) {
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			treeChange = false;
		}
		
		if(this.briqBoo || this.eriqBoo){
			this.briqBoo=false;
			this.eriqBoo=false;
			
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
		}
		getToolbars();
		blnIsBegin = true;
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
	
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		
		tb1.addText(new ToolbarText("�̵�����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�̵���:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("PandsjDropDown");
		cb2.setEditable(true);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
	
//	������
	private String briq;
	private boolean briqBoo=false;
	public String getBRiq() {
		if (briq==null && "".equals(briq)){
			briq = DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {
		if(this.briq==null || !this.briq.equals(briq)){
			briqBoo=true;
		}
		this.briq = briq;
	}
	
//	������
	private String eriq;
	private boolean eriqBoo=false;
	public String getERiq() {
		if (eriq==null && "".equals(eriq)){
			eriq = DateUtil.FormatDate(new Date());
		}
		return eriq;
	}

	public void setERiq(String eriq) {
		if(this.eriq==null || !this.eriq.equals(eriq)){
			eriqBoo=true;
		}
		this.eriq = eriq;
	}
	
	
	
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	private boolean treeChange = false;
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
				treeChange = true;
			}else{
				treeChange = false;
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
//		�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
		public int getDiancTreeJib() {
			JDBCcon con = new JDBCcon();
			int jib = -1;
			String DiancTreeJib = this.getTreeid();
			//System.out.println("jib:" + DiancTreeJib);
			if (DiancTreeJib == null || DiancTreeJib.equals("")) {
				DiancTreeJib = "0";
			}
			String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
			ResultSet rs = con.getResultSet(sqlJib.toString());
			
			try {
				while (rs.next()) {
					jib = rs.getInt("jib");
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}finally{
				con.Close();
			}

			return jib;
		}
//		�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
		public String getTreeDiancmc(String diancmcId) {
			if(diancmcId==null||diancmcId.equals("")){
				diancmcId="1";
			}
			String IDropDownDiancmc = "";
			JDBCcon cn = new JDBCcon();
			
			String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
			ResultSet rs = cn.getResultSet(sql_diancmc);
			try {
				while (rs.next()) {
					IDropDownDiancmc = rs.getString("mingc");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return IDropDownDiancmc;
		}
		
		
//		�̵���������
		public IDropDownBean getPandsjValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
				if (getPandsjModel().getOptionCount()>0){
					((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getPandsjModel().getOption(0));
				}
			}
			return ((Visit)getPage().getVisit()).getDropDownBean2();
		}

		public void setPandsjValue(IDropDownBean Value) {
			((Visit)getPage().getVisit()).setDropDownBean2(Value);
		}

		public void setPandsjModel(IPropertySelectionModel value) {
			((Visit)getPage().getVisit()).setProSelectionModel2(value);
		}

		public IPropertySelectionModel getPandsjModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
				getIPandsjModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel2();
		}

		public void getIPandsjModels() {
			String strGongsID = "";
			

			strGongsID=" and ( dc.id= " +this.getTreeid()+" or dc.fuid="+this.getTreeid()+") ";
			
			String riqIf=" and pd.riq>="+DateUtil.FormatOracleDate(this.getBRiq())+" and pd.riq<="+DateUtil.FormatOracleDate(this.getERiq())+" ";
			
			String sql = "select pd.id,bianm as bianm from pandb pd,vwdianc dc"+
				 " where pd.diancxxb_id=dc.id" +strGongsID+riqIf
					+ " order by pd.bianm desc";
			((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));
		}
		
		//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	
	
	
//	private int show = 0;// ���ó�����������ʾ���ǲ���ʾ
//
//	public int getShow() {// ��������������Զ���ʾ
//		// �����һ�����Ͳ�����ʾ
//		return show;
//	};
//
//	public void setShow(int show) {
//		this.show = show;
//	}

	private String changbId(boolean flag) {// �����
		String changb = "";
		long changbb_id = -1;
		changbb_id = getDiancmcValue() == null ? ((Visit) getPage()
				.getVisit()).getDiancxxb_id() : Long.parseLong(this.getTreeid());
		if (flag) {
			changb = " AND DIANCXXB_ID = " + changbb_id + "\n";
		} else {
			changb = "" + changbb_id;
		}
		return changb;
	}

	/**
	 * for get the changbId
	 * 
	 * @param str
	 *            you can get str.CHANGBB_ID=0,1,2
	 * @author Yangzong
	 * @since 2008-06-12
	 * @return String
	 */
	private String changbId(String str) {// �����
		String changb = "";
		long changbb_id = getDiancmcValue() == null ? ((Visit) getPage()
				.getVisit()).getDiancxxb_id() : Long.parseLong(this.getTreeid());
		changb = " AND " + str.toUpperCase() + ".DIANCXXB_ID = " + changbb_id
				+ "\n";

		return changb;
	}

	// -------------��������������------------//
	/*private IDropDownBean ChangbValue;

	private boolean changbFlag = true;

	public IDropDownBean getChangbValue() {
		if (ChangbValue == null) {
			StringBuffer buf = new StringBuffer();
			JDBCcon con = new JDBCcon();
			if (getChangbModel().getOptionCount() > 1) {
				buf.append("SELECT NUM ID FROM(SELECT ROWNUM AS NUM,\n");
				buf.append("DEFAULTVAL FROM CHANGBB) WHERE DEFAULTVAL=1");
			} else {
				buf.append("SELECT ROWNUM ID FROM CHANGBB");
			}
			int num = 0;
			ResultSet rs = con.getResultSet(buf);
			try {
				if (rs.next()) {
					num = rs.getInt("ID");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ChangbValue = (IDropDownBean) getChangbModel().getOption(num - 1);
		}
		return ChangbValue;
	}

	public void setChangbValue(IDropDownBean Value) {
		if (ChangbValue != null && !(ChangbValue == Value)) {
			ChangbValue = Value;
			changbFlag = true;
		}

	}

	private IPropertySelectionModel ChangbModel;

	public void setChangbModel(IPropertySelectionModel value) {
		ChangbModel = value;
	}

	public IPropertySelectionModel getChangbModel() {
		if (ChangbModel == null) {
			getChangbModels();
		}
		return ChangbModel;
	}

	public IPropertySelectionModel getChangbModels() {
		List changList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT ID,MINGC FROM CHANGBB");
		JDBCcon con = new JDBCcon();
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				changList
						.add(new IDropDownBean(rs.getLong(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ChangbModel = new IDropDownModel(changList);
		return ChangbModel;
	}*/

}