package com.zhiren.jingjfx;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry.IPage;
import org.jfree.data.category.CategoryDataset;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-22
 * ���������ӳ�������ֵ��������ͺ��÷������ص��ͬ�����ʷ������޸��������ʷ�����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-26
 * �����������ۺϱ�ú���۷������ۺϱ�ú�����ۼƷ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-18
 * �����������µĹ��Ĵ洦����getMeitghcztqk_fgs 
 * 		�޸��볧��ֵ�����е�����˵�����ֵĳ�����
 */
/**
 * @author Rock
 * @since 2009-08-20
 * @version v1.1.2.1
 * @discription ���÷������Ի�����
 */

public class AnalyOperation {
	/*  �����ʽ��ID  */
	public static final String analyId_Meitshcqk_fgs = "3";
	public static final String analyId_Zhongdhtdhl_fgs = "11";
	public static final String analyId_Ranyhyqk_fgs = "21";
	public static final String analyId_Jicmfrlqk_fgs = "31";
	public static final String analyId_Jicmfrllj_fgs = "32";
	public static final String analyId_Rucbmdjwcqk_fgs = "41";
	public static final String analyId_Rucbmdjwcqklj_fgs = "42";
	public static final String analyId_Rucbmdjwcdb_fgs = "43";
	public static final String analyId_Rucbmdjwcdblj_fgs = "44";
	public static final String analyId_Rulbmdj_fgs = "51";
	public static final String analyId_Rulbmdjlj_fgs = "52";
	public static final String analyId_Rucrlbmdjc_fgs = "61";
	public static final String analyId_Rezcqk_fgs = "71";
	public static final String analyId_Rezcqkck_fgs = "72";
	public static final String analyId_Yunsqk_fgs = "81";
	public static final String analyId_Shouudlqk_fgs = "91";
	public static final String analyId_Shuifctz_fgs = "101";
	
	
	/**
	 * 
	 * @param diancxxb_id  	�糧ID
	 * @param oraDate		oracle����
	 * @param fenx			����(���¡��ۼ�)
	 * @return				ú�պĴ��SQL
	 */
	private static String getMeighcSql(String diancxxb_id,
			String oraDate, String fenx){
		String sql = 
			"select round_new(s.shouml/10000,2) as gm,\n" +
			"round_new((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000,2) hy,\n" + 
			"round_new(s.kuc/10000,2) kc\n" + 
			"from yueshchjb s, diancxxb d\n" + 
			"where s.diancxxb_id = d.id\n" + 
			"and d.fuid =" + diancxxb_id + "\n" + 
			"and s.riq =" + oraDate + "\n" +
			"and s.fenx ='" + fenx + "'\n" + 
			"order by d.xuh,d.id";
		return sql;
	}
//	ú̿���Ĵ����
	public static String getMeitghcztqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef){
		String html = "";				//		���ص�htmlcode
		int row_title = 0;				//		�����к�
		int r_g_dy = 1;					//		��ú������
		int r_g_hb = 2;					//		��ú������
		int r_g_lj = 3;					//		��ú�ۼ���
		int r_g_tq = 4;					//		��úͬ����
		int r_g_tb = 5;					//		��úͬ����
		int r_g_tbz = 6;				//		��úͬ��������
		
		int r_h_dy = 7;					//		��ú������
		int r_h_hb = 8;					//		��ú������
		int r_h_lj = 9;					//		��ú�ۼ���
		int r_h_tq = 10;				//		��úͬ����
		int r_h_tb = 11;				//		��úͬ����
		int r_h_tbz = 12;				//		��úͬ��������
		
		int r_c_dy = 13;				//		��ú������
		int r_c_hb = 14;				//		��ú������
//		int r_c_lj = 15;				//		��ú�ۼ���
		int r_c_tq = 15;				//		��úͬ����
		int r_c_tb = 16;				//		��úͬ����
		int r_c_kyts = 17;				//		��������
		
		int c_title_lb = 0;				//		���������
		int c_title = 1;				//		������ʵ��
		int c_fgs = 2;				//		�ֹ�˾��
		int c_ssgs = 3;				//		���й�˾��
		String strDate = nianf + "-" + yuef + "-01";	//		���·�
//		Current Date
		Date cDate = DateUtil.getDate(strDate);
//		Current Date for Oracle
		String coDate = DateUtil.FormatOracleDate(cDate);	//		oracle����
//		Last Month Date
		Date lmDate = DateUtil.AddDate(cDate, -1, DateUtil.AddType_intMonth);
//		Last Month Date for Oracle
		String lmoDate = DateUtil.FormatOracleDate(lmDate);
//		Last Year Date 
		Date lyDate = DateUtil.AddDate(cDate, -1, DateUtil.AddType_intYear);
//		Last Year Date for Oracle
		String lyoDate = DateUtil.FormatOracleDate(lyDate);
		
//		��ѯ�糧���ƣ�����ú��SQL
		String sql = "select mingc,round(jingjcml/10000,2) jjml from diancxxb where fuid = " + diancxxb_id +
		" order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[18][rsl.getRows() + 4];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 4];
		/*  �����п�  */
		colwidth[0] = 35;
		colwidth[1] = 35;
		for(int i = 2; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/*  ���õ糧���ĵ糧����  */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 4] = rsl.getString("mingc");
		}
		rsl.close();
//		���õ������Ĵ�
		sql = getMeighcSql(diancxxb_id,coDate,SysConstant.Fenx_Beny);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_dy][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_dy][rsl.getRow() + 4] = rsl.getString("hy");
			data[r_c_dy][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();
//		�����ۼ����Ĵ�
		sql = getMeighcSql(diancxxb_id,coDate,SysConstant.Fenx_Leij);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_lj][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_lj][rsl.getRow() + 4] = rsl.getString("hy");
//			data[r_c_lj][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();
//		���û������Ĵ�
		sql = getMeighcSql(diancxxb_id,lmoDate,SysConstant.Fenx_Beny);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_hb][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_hb][rsl.getRow() + 4] = rsl.getString("hy");
			data[r_c_hb][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();
//		����ͬ�����Ĵ�
		sql = getMeighcSql(diancxxb_id,lyoDate,SysConstant.Fenx_Leij);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_tq][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_tq][rsl.getRow() + 4] = rsl.getString("hy");
			data[r_c_tq][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();

		/*  ���ر�����(��һ��)   */
		for(int i = r_g_dy; i <= r_g_tbz; i++){
			data[i][c_title_lb] = "��ú";
		}
		for(int i = r_h_dy; i <= r_h_tbz; i++){
			data[i][c_title_lb] = "����";
		}
		for(int i = r_c_dy; i <= r_c_kyts; i++){
			data[i][c_title_lb] = "��ú";
		}
		data[r_g_dy][c_title] = yuef + "��";
		data[r_g_hb][c_title] = "����";
		data[r_g_lj][c_title] = "�ۼ�";
		data[r_g_tq][c_title] = "ͬ��";
		data[r_g_tb][c_title] = "ͬ��";
		data[r_g_tbz][c_title] = "ͬ��<br>����";
		data[r_h_dy][c_title] = yuef + "��";
		data[r_h_hb][c_title] = "����";
		data[r_h_lj][c_title] = "�ۼ�";
		data[r_h_tq][c_title] = "ͬ��";
		data[r_h_tb][c_title] = "ͬ��";
		data[r_h_tbz][c_title] = "ͬ��<br>����";
		data[r_c_dy][c_title] = yuef + "��";
		data[r_c_hb][c_title] = "����";
		data[r_c_tq][c_title] = "ͬ��";
		data[r_c_tb][c_title] = "ͬ��";
		data[r_c_kyts][c_title] = "����<br>����";
//		���طֹ�˾����
		/*  ���ñ���  */
		data[row_title][c_fgs] = "�е����";
		/*  ȡ�õ��¹��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
			"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
			"round_new(sum(s.kuc/10000),2) kc\n" + 
			"from yueshchjb s, diancxxb d\n" + 
			"where s.diancxxb_id = d.id\n" + 
			"and d.fuid =" + diancxxb_id + "\n" + 
			"and s.riq =" + coDate + "\n" + 
			"and s.fenx ='" + SysConstant.Fenx_Beny + "'";
		//+ "\n" + "order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_dy][c_fgs] = rsl.getString("gm");
			data[r_h_dy][c_fgs] = rsl.getString("hy");
			data[r_c_dy][c_fgs] = rsl.getString("kc");
		}
		rsl.close();
		/*  ȡ�û��ȹ��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
		"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
		"round_new(sum(s.kuc/10000),2) kc\n" + 
		"from yueshchjb s, diancxxb d\n" + 
		"where s.diancxxb_id = d.id\n" + 
		"and d.fuid =" + diancxxb_id + "\n" + 
		"and s.riq =" + lmoDate + "\n" + 
		"and s.fenx ='" + SysConstant.Fenx_Beny + "'";
		//+ "\n" + "order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_tb][c_fgs] = rsl.getString("gm");
			data[r_h_tb][c_fgs] = rsl.getString("hy");
			data[r_c_tb][c_fgs] = rsl.getString("kc");
		}
		rsl.close();
		/*  ȡ���ۼƹ��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
		"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
		"round_new(sum(s.kuc/10000),2) kc\n" + 
		"from yueshchjb s, diancxxb d\n" + 
		"where s.diancxxb_id = d.id\n" + 
		"and d.fuid =" + diancxxb_id + "\n" + 
		"and s.riq =" + coDate + "\n" + 
		"and s.fenx ='" + SysConstant.Fenx_Leij + "'";
		//+ "\n" + "order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_lj][c_fgs] = rsl.getString("gm");
			data[r_h_lj][c_fgs] = rsl.getString("hy");
		}
		rsl.close();
		/*  ȡ��ͬ�ڹ��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
		"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
		"round_new(sum(s.kuc/10000),2) kc\n" + 
		"from yueshchjb s, diancxxb d\n" + 
		"where s.diancxxb_id = d.id\n" + 
		"and d.fuid =" + diancxxb_id + "\n" + 
		"and s.riq =" + lyoDate + "\n" + 
		"and s.fenx ='" + SysConstant.Fenx_Leij + "'";
		//+ "\n" + "order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_tq][c_fgs] = rsl.getString("gm");
			data[r_h_tq][c_fgs] = rsl.getString("hy");
			data[r_c_tq][c_fgs] = rsl.getString("kc");
		}
		rsl.close();
//		�������й�˾����
		/*  ���ñ���  */
		data[row_title][c_ssgs] = "�й�����";
		/*  ȡ�õ��¹��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
			"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
			"round_new(sum(s.kuc/10000),2) kc\n" + 
			"from yueshchjb s, diancxxb d, itemsort i, item t\n" + 
			"where s.diancxxb_id = d.id\n" + 
			"and i.bianm = 'DCSFSS' and i.id = t.itemsortid\n" + 
			"and t.mingc = d.id and t.bianm = 'SS'\n" + 
			"and d.fuid =" + diancxxb_id + "\n" + 
			"and s.riq =" + coDate + "\n" + 
			"and s.fenx ='" + SysConstant.Fenx_Beny + "'";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_dy][c_ssgs] = rsl.getString("gm");
			data[r_h_dy][c_ssgs] = rsl.getString("hy");
			data[r_c_dy][c_ssgs] = rsl.getString("kc");
		}
		rsl.close();
		/*  ȡ�û��ȹ��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
			"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
			"round_new(sum(s.kuc/10000),2) kc\n" + 
			"from yueshchjb s, diancxxb d, itemsort i, item t\n" + 
			"where s.diancxxb_id = d.id\n" + 
			"and i.bianm = 'DCSFSS' and i.id = t.itemsortid\n" + 
			"and t.mingc = d.id and t.bianm = 'SS'\n" + 
			"and d.fuid =" + diancxxb_id + "\n" + 
			"and s.riq =" + lmoDate + "\n" + 
			"and s.fenx ='" + SysConstant.Fenx_Beny + "'";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_hb][c_ssgs] = rsl.getString("gm");
			data[r_h_hb][c_ssgs] = rsl.getString("hy");
			data[r_c_hb][c_ssgs] = rsl.getString("kc");
		}
		rsl.close();
		/*  ȡ���ۼƹ��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
			"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
			"round_new(sum(s.kuc/10000),2) kc\n" + 
			"from yueshchjb s, diancxxb d, itemsort i, item t\n" + 
			"where s.diancxxb_id = d.id\n" + 
			"and i.bianm = 'DCSFSS' and i.id = t.itemsortid\n" + 
			"and t.mingc = d.id and t.bianm = 'SS'\n" + 
			"and d.fuid =" + diancxxb_id + "\n" + 
			"and s.riq =" + coDate + "\n" + 
			"and s.fenx ='" + SysConstant.Fenx_Leij + "'";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_lj][c_ssgs] = rsl.getString("gm");
			data[r_h_lj][c_ssgs] = rsl.getString("hy");
		}
		rsl.close();
		
		/*  ȡ��ͬ�ڹ��Ĵ�  */
		sql = "select round_new(sum(s.shouml/10000),2) as gm,\n" +
			"round_new(sum((s.fady+s.gongry+s.qith+s.diaocl-s.shuifctz)/10000),2) hy,\n" + 
			"round_new(sum(s.kuc/10000),2) kc\n" + 
			"from yueshchjb s, diancxxb d, itemsort i, item t\n" + 
			"where s.diancxxb_id = d.id\n" + 
			"and i.bianm = 'DCSFSS' and i.id = t.itemsortid\n" + 
			"and t.mingc = d.id and t.bianm = 'SS'\n" + 
			"and d.fuid =" + diancxxb_id + "\n" + 
			"and s.riq =" + lyoDate + "\n" + 
			"and s.fenx ='" + SysConstant.Fenx_Leij + "'";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[r_g_tq][c_ssgs] = rsl.getString("gm");
			data[r_h_tq][c_ssgs] = rsl.getString("hy");
			data[r_h_tq][c_ssgs] = rsl.getString("kc");
		}
		rsl.close();
//		���ȡ�ͬ�ȡ�ͬ������ֵ���������������㡣
		for(int i = 2; i < data[row_title].length ; i++){
			/*  ���ȵĲ�ֵ����  */
			String bj = CustomMaths.sub(data[r_g_dy][i], data[r_g_hb][i]);
			data[r_g_hb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_h_dy][i], data[r_h_hb][i]);
			data[r_h_hb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_c_dy][i], data[r_c_hb][i]);
			data[r_c_hb][i] = bj==null?"":bj;
			/*  ͬ�ȵĲ�ֵ����  */
			bj = CustomMaths.sub(data[r_g_lj][i], data[r_g_tq][i]);
			data[r_g_tb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_h_lj][i], data[r_h_tq][i]);
			data[r_h_tb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_c_dy][i], data[r_c_tq][i]);
			data[r_c_tb][i] = bj==null?"":bj;
			/*  ͬ�������ļ���  */
			bj = CustomMaths.div(data[r_g_tb][i], data[r_g_tq][i] + "00", 2);
			data[r_g_tbz][i] = bj==null?"":bj;
			bj = CustomMaths.div(data[r_h_tb][i], data[r_h_tq][i] + "00", 2);
			data[r_h_tbz][i] = bj==null?"":bj;
			/*  �����������ļ���  */
			bj = CustomMaths.div(CustomMaths.mul(data[r_c_dy][i],"30")
					, data[r_h_dy][i], 0);
			data[r_c_kyts][i] = bj==null?"":bj;
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		rt.setTitle(">ú̿���Ĵ����("+yuef + "��)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:���", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i= 3; i <= colwidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		for(int i = 1; i<=rt.body.getRows(); i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
			rt.body.setCellAlign(i, 2, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
//			rt.body.setCellAlign(2, i, Table.ALIGN_CENTER);
		}
		/* �ϲ��б��� */
		rt.body.merge(1, 1, rt.body.getRows() , 1);
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		��������˵��
//		����˵������
		String wenz = "����" + yuef + "�¹�˾����ԭú " + data[r_g_dy][c_fgs] + "��֣�";
		if(!"".equals(data[r_g_hb][c_fgs]))
			if(Double.parseDouble(data[r_g_hb][c_fgs])>0){
				wenz += "������" + data[r_g_hb][c_fgs] + "��֣�";
			}else{
				wenz += "���ȼ�" + Math.abs(Double.parseDouble(data[r_g_hb][c_fgs]))
				+ "��֣�";
			}
		wenz += "��ú" + data[r_h_dy][c_fgs] + "��֣�";
		if(!"".equals(data[r_h_hb][c_fgs]))
			if(Double.parseDouble(data[r_h_hb][c_fgs])>0){
				wenz += "������" + data[r_h_hb][c_fgs] + "��֣�";
			}else{
				wenz += "���ȼ�" + Math.abs(Double.parseDouble(data[r_h_hb][c_fgs])) + "��֣�";
			}
		wenz += "�ۼƹ���ԭú" + data[r_h_lj][c_fgs] + "��֣�";
		if(!"".equals(data[r_g_tb][c_fgs]))
			if(Double.parseDouble(data[r_g_tb][c_fgs])>0){
				wenz += "ͬ����" + data[r_g_tb][c_fgs] + "��֣�����" + data[r_g_tbz][c_fgs] + "%��";
			}else{
				wenz += "ͬ�ȼ�" + Math.abs(Double.parseDouble(data[r_g_tb][c_fgs])) + 
				"��֣�����" + Math.abs(Double.parseDouble(data[r_g_tb][c_fgs])) + "%��";
			}
		wenz += "�ۼƺ�ú" + data[r_h_lj][c_fgs] + "��֣�";
		if(!"".equals(data[r_h_tb][c_fgs]))
			if(Double.parseDouble(data[r_h_tb][c_fgs])>0){
				wenz += "ͬ����" + data[r_h_tb][c_fgs] + "��֣�����" + data[r_h_tbz][c_fgs] + "%��";
			}else{
				wenz += "ͬ�ȼ�" + Math.abs(Double.parseDouble(data[r_h_tb][c_fgs])) + 
				"��֣�����" + Math.abs(Double.parseDouble(data[r_h_tb][c_fgs])) + "%��";
			}
		wenz += "��ĩ���ú" + data[r_c_dy][c_fgs] + "��֣�";
		if(!"".equals(data[r_c_tb][c_fgs]))
			if(Double.parseDouble(data[r_c_tb][c_fgs])>0){
				wenz += "ͬ����" + data[r_c_tb][c_fgs] + "��֣�";
			}else{
				wenz += "ͬ�ȼ�" + Math.abs(Double.parseDouble(data[r_c_tb][c_fgs])) + 
				"��֣�";
			}
		wenz += "��������" + data[r_c_kyts][c_fgs] + "�졣";
		html += getWenzHtml(wenz,data[0].length);
		return html;
	}
	
//	�ص��ͬ������
	public static String getZhongdhtdhl_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int row_title = 0;				//		������
		int row_title2 = 1;				//		������2
		int col_title = 0;				//		������
		int col_ht_bn = 1;				//		��ͬ����
		int col_ht_bq = 2;				//		��ͬ����
		int col_ht_tq = 3;				//		��ͬͬ��
		int col_sh_bq = 4;				//		ʵ�ձ���
		int col_sh_tq = 5;				//		ʵ��ͬ��
		int col_dh_bn = 6;				//		��������
		int col_dh_bq = 7;				//		��������
		int col_dh_tq = 8;				//		����ͬ��
		String strDate = nianf + "-" + yuef + "-01";	//		���·�
//		Current Date
		Date cDate = DateUtil.getDate(strDate);
//		Current Date for Oracle
		String coDate = DateUtil.FormatOracleDate(cDate);	//		oracle����
//		First Date of Year
		Date fDate = DateUtil.getDate(nianf + "-01-01");
//		First Date of Year for Oracle
		String fyoDate = DateUtil.FormatOracleDate(fDate);
//		Last Date of Year
		Date lDate = DateUtil.getDate(nianf + "-12-01");
//		Last Date of year for Oracle
		String loDate = DateUtil.FormatOracleDate(lDate);
		
		String sql = "select id,mingc from diancxxb where fuid = " + diancxxb_id + " order by xuh,id";
		ResultSetList rsl = con.getResultSetList(sql);
		String[][] data = new String[rsl.getRows() + 4][9];
		rsl.close();
		data[row_title][col_title] = "��λ";
		data[row_title2][col_title] = "��λ";
		data[row_title][col_ht_bn] = "��ͬ��";
		data[row_title][col_ht_bq] = "��ͬ��";
		data[row_title][col_ht_tq] = "��ͬ��";
		data[row_title][col_sh_bq] = "ʵ����";
		data[row_title][col_sh_tq] = "ʵ����";
		data[row_title][col_dh_bn] = "������";
		data[row_title][col_dh_bq] = "������";
		data[row_title][col_dh_tq] = "������";
		data[row_title2][col_ht_bn] = "����";
		data[row_title2][col_ht_bq] = "����";
		data[row_title2][col_ht_tq] = "ͬ��";
		data[row_title2][col_sh_bq] = "����";
		data[row_title2][col_sh_tq] = "ͬ��";
		data[row_title2][col_dh_bn] = "����";
		data[row_title2][col_dh_bq] = "����";
		data[row_title2][col_dh_tq] = "ͬ��";
//		�е����
		sql = 
			"select sum(bnht.bnht)bnht ,sum(bqht.bqht) bqht,sum(tqht.tqht) tqht,\n" +
			"sum(bqdh.bqdh) bqdh,sum(tqdh.tqdh) tqdh,\n" + 
			"round(decode(sum(bnht.bnht),0,0,sum(bqdh.bqdh*100)/sum(bnht.bnht)),2) bndhl,\n" + 
			"round(decode(sum(bqht.bqht),0,0,sum(bqdh.bqdh*100)/sum(bqht.bqht)),2) bqdhl,\n" + 
			"round(decode(sum(tqht.tqht),0,0,sum(tqdh.tqdh*100)/sum(tqht.tqht)),2) tqdhl\n" + 
			"from diancxxb d,\n" + 
			"(select n.diancxxb_id,sum(hej) bqht  from niandhtqkb n\n" + 
			"where n.riq >= "+fyoDate+"\n" + 
			"and n.riq <= "+coDate+"\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) bqht,\n" + 
			"(select n.diancxxb_id,sum(hej) bnht  from niandhtqkb n\n" + 
			"where n.riq >= "+fyoDate+"\n" + 
			"and n.riq <= "+loDate+"\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) bnht,\n" + 
			"(select n.diancxxb_id,sum(hej) tqht  from niandhtqkb n\n" + 
			"where n.riq >= add_months("+fyoDate+",-12)\n" + 
			"and n.riq <= add_months("+coDate+",-12)\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) tqht,\n" + 
			"(select t.diancxxb_id,round(sum(y.laimsl)/10000,0) bqdh from yueslb y,yuetjkjb t\n" + 
			"where t.riq >= "+fyoDate+"\n" + 
			"and t.riq <= "+coDate+"\n" + 
			"and y.yuetjkjb_id = t.id and t.jihkjb_id = 1\n" + 
			"group by t.diancxxb_id) bqdh,\n" + 
			"(select t.diancxxb_id,round(sum(y.laimsl)/10000,0) tqdh from yueslb y,yuetjkjb t\n" + 
			"where t.riq >= add_months("+fyoDate+",-12)\n" + 
			"and t.riq <= add_months("+coDate+",-12)\n" + 
			"and y.yuetjkjb_id = t.id and t.jihkjb_id = 1\n" + 
			"group by t.diancxxb_id) tqdh\n" + 
			"where d.id = bnht.diancxxb_id(+) and d.id = bqht.diancxxb_id(+)\n" + 
			"and d.id = tqht.diancxxb_id(+) and d.id = bqdh.diancxxb_id(+)\n" + 
			"and d.id = tqdh.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[2][col_title] = "�е����";
			data[2][col_ht_bn] = rsl.getString("bnht");
			data[2][col_ht_bq] = rsl.getString("bqht");
			data[2][col_ht_tq] = rsl.getString("tqht");
			data[2][col_sh_bq] = rsl.getString("bqdh");
			data[2][col_sh_tq] = rsl.getString("tqdh");
			data[2][col_dh_bn] = rsl.getString("bndhl");
			data[2][col_dh_bq] = rsl.getString("bqdhl");
			data[2][col_dh_tq] = rsl.getString("tqdhl");
		}
		rsl.close();
		sql = 
			"select sum(bnht.bnht)bnht ,sum(bqht.bqht) bqht,sum(tqht.tqht) tqht,\n" +
			"sum(bqdh.bqdh) bqdh,sum(tqdh.tqdh) tqdh,\n" + 
			"round(decode(sum(bnht.bnht),0,0,sum(bqdh.bqdh*100)/sum(bnht.bnht)),2) bndhl,\n" + 
			"round(decode(sum(bqht.bqht),0,0,sum(bqdh.bqdh*100)/sum(bqht.bqht)),2) bqdhl,\n" + 
			"round(decode(sum(tqht.tqht),0,0,sum(tqdh.tqdh*100)/sum(tqht.tqht)),2) tqdhl\n" + 
			"from diancxxb d, itemsort o, item i,\n" + 
			"(select n.diancxxb_id,sum(hej) bqht  from niandhtqkb n\n" + 
			"where n.riq >= "+fyoDate+"\n" + 
			"and n.riq <= "+coDate+"\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) bqht,\n" + 
			"(select n.diancxxb_id,sum(hej) bnht  from niandhtqkb n\n" + 
			"where n.riq >= "+fyoDate+"\n" + 
			"and n.riq <= "+loDate+"\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) bnht,\n" + 
			"(select n.diancxxb_id,sum(hej) tqht  from niandhtqkb n\n" + 
			"where n.riq >= add_months("+fyoDate+",-12)\n" + 
			"and n.riq <= add_months("+coDate+",-12)\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) tqht,\n" + 
			"(select t.diancxxb_id,round(sum(y.laimsl)/10000,0) bqdh from yueslb y,yuetjkjb t\n" + 
			"where t.riq >= "+fyoDate+"\n" + 
			"and t.riq <= "+coDate+"\n" + 
			"and y.yuetjkjb_id = t.id and t.jihkjb_id = 1\n" + 
			"group by t.diancxxb_id) bqdh,\n" + 
			"(select t.diancxxb_id,round(sum(y.laimsl)/10000,0) tqdh from yueslb y,yuetjkjb t\n" + 
			"where t.riq >= add_months("+fyoDate+",-12)\n" + 
			"and t.riq <= add_months("+coDate+",-12)\n" + 
			"and y.yuetjkjb_id = t.id and t.jihkjb_id = 1\n" + 
			"group by t.diancxxb_id) tqdh\n" + 
			"where d.id = bnht.diancxxb_id(+) and d.id = bqht.diancxxb_id(+)\n" + 
			"and d.id = tqht.diancxxb_id(+) and d.id = bqdh.diancxxb_id(+)\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and d.id = tqdh.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[3][col_title] = "�й�����";
			data[3][col_ht_bn] = rsl.getString("bnht");
			data[3][col_ht_bq] = rsl.getString("bqht");
			data[3][col_ht_tq] = rsl.getString("tqht");
			data[3][col_sh_bq] = rsl.getString("bqdh");
			data[3][col_sh_tq] = rsl.getString("tqdh");
			data[3][col_dh_bn] = rsl.getString("bndhl");
			data[3][col_dh_bq] = rsl.getString("bqdhl");
			data[3][col_dh_tq] = rsl.getString("tqdhl");
		}
		rsl.close();
		sql = 
			"select d.mingc,bnht.bnht,bqht.bqht,tqht.tqht,bqdh.bqdh,tqdh.tqdh,\n" +
			"round(decode(bnht.bnht,0,0,bqdh.bqdh*100/bnht.bnht),2) bndhl,\n" + 
			"round(decode(bqht.bqht,0,0,bqdh.bqdh*100/bqht.bqht),2) bqdhl,\n" + 
			"round(decode(tqht.tqht,0,0,tqdh.tqdh*100/tqht.tqht),2) tqdhl\n" + 
			"from diancxxb d,\n" + 
			"(select n.diancxxb_id,sum(hej) bqht  from niandhtqkb n\n" + 
			"where n.riq >= "+fyoDate+"\n" + 
			"and n.riq <= "+coDate+"\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) bqht,\n" + 
			"(select n.diancxxb_id,sum(hej) bnht  from niandhtqkb n\n" + 
			"where n.riq >= "+fyoDate+"\n" + 
			"and n.riq <= "+loDate+"\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) bnht,\n" + 
			"(select n.diancxxb_id,sum(hej) tqht  from niandhtqkb n\n" + 
			"where n.riq >= add_months("+fyoDate+",-12)\n" + 
			"and n.riq <= add_months("+coDate+",-12)\n" + 
			"and n.jihkjb_id=1\n" + 
			"group by diancxxb_id) tqht,\n" + 
			"(select t.diancxxb_id,round(sum(y.laimsl)/10000,0) bqdh from yueslb y,yuetjkjb t\n" + 
			"where t.riq >= "+fyoDate+"\n" + 
			"and t.riq <= "+coDate+"\n" + 
			"and y.yuetjkjb_id = t.id and t.jihkjb_id = 1\n" + 
			"group by t.diancxxb_id) bqdh,\n" + 
			"(select t.diancxxb_id,round(sum(y.laimsl)/10000,0) tqdh from yueslb y,yuetjkjb t\n" + 
			"where t.riq >= add_months("+fyoDate+",-12)\n" + 
			"and t.riq <= add_months("+coDate+",-12)\n" + 
			"and y.yuetjkjb_id = t.id and t.jihkjb_id = 1\n" + 
			"group by t.diancxxb_id) tqdh\n" + 
			"where d.id = bnht.diancxxb_id(+) and d.id = bqht.diancxxb_id(+)\n" + 
			"and d.id = tqht.diancxxb_id(+) and d.id = bqdh.diancxxb_id(+)\n" + 
			"and d.id = tqdh.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id\n" + 
			"\n" + 
			"\n" + 
			"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[4+rsl.getRow()][col_title] = rsl.getString("mingc");
			data[4+rsl.getRow()][col_ht_bn] = rsl.getString("bnht");
			data[4+rsl.getRow()][col_ht_bq] = rsl.getString("bqht");
			data[4+rsl.getRow()][col_ht_tq] = rsl.getString("tqht");
			data[4+rsl.getRow()][col_sh_bq] = rsl.getString("bqdh");
			data[4+rsl.getRow()][col_sh_tq] = rsl.getString("tqdh");
			data[4+rsl.getRow()][col_dh_bn] = rsl.getString("bndhl");
			data[4+rsl.getRow()][col_dh_bq] = rsl.getString("bqdhl");
			data[4+rsl.getRow()][col_dh_tq] = rsl.getString("tqdhl");
		}
		rsl.close();
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "���ۼ�";
		}
		int colwidth[] = {100,60,60,60,60,60,60,60,60};
		rt.setTitle(">���ص㶩����ͬ������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:��֡�%", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		rt.body.merge(1, 1, 2, colwidth.length);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=data.length; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =1; i <= colwidth.length ; i++){
			for(int j = 1; j <= 2;j++){
				rt.body.setCellAlign(j, i, Table.ALIGN_CENTER);
			}
		}
		for(int i =2; i <= colwidth.length ; i++){
			for(int j = 2; j < data.length;j++){
				rt.body.setCellAlign(j, i, Table.ALIGN_RIGHT);
			}
		}
		
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		double btq = CustomMaths.sub(parseDouble(data[3][col_dh_bq]),parseDouble(data[3][col_dh_tq]));
		rslist.add(new String[]{data[3][col_title],data[3][col_dh_bq],String.valueOf(btq)});
		for(int i =4 ; i <data.length;i++){
			btq = CustomMaths.sub(parseDouble(data[i][col_dh_bq]),parseDouble(data[i][col_dh_tq]));
			for(int j=0; j<rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(btq < bj){
					rslist.add(j,new String[]{data[i][col_title],data[i][col_dh_bq],String.valueOf(btq)});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[i][col_title],data[i][col_dh_bq],String.valueOf(btq)});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"ȼ�ͺ������",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
//		��ʱ������ͼ��
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "����" + yuef + "�¸���λ�ص��ͬ���ڵ��������" ;
		for(int i=4; i < data.length ; i++){
			btq = CustomMaths.sub(parseDouble(data[i][col_dh_bq]),parseDouble(data[i][col_dh_tq]));
			wenz += data[i][col_title] + "���ڵ�����" + data[i][col_dh_bq] + 
			"��ͬ��" + data[i][col_dh_tq] + "ͬ��" + (btq>0?"����":"����")  + btq + "��";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
		
	}
//	ȼ�ͺ������
	public static String getRanyhyqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_by = 1;					//		����������
		int row_sy = 2;					//		���������� 
		int row_hb = 3;					//		����������
		int row_lj = 4;					//		�ۼ�������
		int row_tq = 5;					//		�ۼ�ͬ��������
		int row_tb = 6;					//		ͬ�Ȳ�ֵ
		int row_tbl = 7;				//		ͬ��������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		����
		String sql = 
			"select d.mingc, y.hy from\n" +
			"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
			"from yueshcyb where riq = "+oraDate+"\n" + 
			"and fenx = '"+SysConstant.Fenx_Beny+"' group by diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
			" order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[8][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* ��ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();
//		����
		sql = 
			"select d.mingc, y.hy from\n" +
			"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
			"from yueshcyb where riq = add_months("+oraDate+",-1)\n" + 
			"and fenx = '"+SysConstant.Fenx_Beny+"' group by diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
			" order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ���ȸ�ֵ */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();
//		�ۼ�
		sql = 
			"select d.mingc, y.hy from\n" +
			"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
			"from yueshcyb where riq = "+oraDate+"\n" + 
			"and fenx = '"+SysConstant.Fenx_Leij+"' group by diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
			" order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �ۼƸ�ֵ */
		while(rsl.next()){
			data[row_lj][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();
//		ͬ��
		sql = "select d.mingc, y.hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = add_months("+oraDate+",-12)\n" + 
		"and fenx = '"+SysConstant.Fenx_Leij+"' group by diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
		" order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڸ�ֵ */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();

//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_by][col_title] = yuef + "��";
		data[row_sy][col_title] = "����";
		data[row_hb][col_title] = "���Ȳ�ֵ";
		data[row_lj][col_title] = yuef + "���ۼ�";
		data[row_tq][col_title] = "ͬ��";
		data[row_tb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_tbl][col_title] = "��(��)%";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  ��������  */
		sql ="select sum(y.hy) hy from\n" +
			"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
			"from yueshcyb where riq = "+oraDate+"\n" + 
			"and fenx = '"+SysConstant.Fenx_Beny+"' group by diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id ;
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_fgs] = rsl.getString("hy");
		}
		rsl.close();
		/*  ��������  */
		sql = "select sum(y.hy) hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = add_months("+oraDate+",-1)\n" + 
		"and fenx = '"+SysConstant.Fenx_Beny+"' group by diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id ;
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("hy");
		}
		rsl.close();
		/*  �ۼ�����  */
		sql = "select sum(y.hy) hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = "+oraDate+"\n" + 
		"and fenx = '"+SysConstant.Fenx_Leij+"' group by diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id ;
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_fgs] = rsl.getString("hy");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select sum(y.hy) hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = add_months("+oraDate+",-12)\n" + 
		"and fenx = '"+SysConstant.Fenx_Beny+"' group by diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id ;
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_fgs] = rsl.getString("hy");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  ��������  */
		sql = "select sum(y.hy) hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = "+oraDate+"\n" + 
		"and fenx = '"+SysConstant.Fenx_Beny+
		"' group by diancxxb_id) y,diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"and i.bianm = 'SS' and i.mingc = d.id\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_ssgs] = rsl.getString("hy");
		}
		rsl.close();
		/*  ��������  */
		sql = "select sum(y.hy) hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = add_months("+oraDate+",-1)\n" + 
		"and fenx = '"+SysConstant.Fenx_Beny+
		"' group by diancxxb_id) y,diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"and i.bianm = 'SS' and i.mingc = d.id\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_ssgs] = rsl.getString("hy");
		}
		rsl.close();
		/*  �ۼ�����  */
		sql = "select sum(y.hy) hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = "+oraDate+"\n" + 
		"and fenx = '"+SysConstant.Fenx_Leij+
		"' group by diancxxb_id) y,diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"and i.bianm = 'SS' and i.mingc = d.id\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("hy");
		}
		rsl.close();
		/*  ͬ������  */
		sql ="select sum(y.hy) hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = add_months("+oraDate+",-12)\n" + 
		"and fenx = '"+SysConstant.Fenx_Beny+
		"' group by diancxxb_id) y,diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"and i.bianm = 'SS' and i.mingc = d.id\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("hy");
		}
		rsl.close();
//		���㻷�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����ͬ�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "���ۼ�";
		}
		rt.setTitle(">ȼ�ͺ������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=8; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(5, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(6, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(7, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(8, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
		}
		for(int i=4 ; i<data[0].length;i++){
			double by = parseDouble(data[row_hb][i]);
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by<bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"ȼ�ͺ������",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
//		��ʱ������ͼ��
//		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "����" + yuef + "�¹�˾ȼ�ͺ���" + data[row_by][col_fgs] + 
		"��";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "����������" + data[row_hb][col_fgs] + "�֡�";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "��";
			}else{
				wenz += "�����½�" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"�֡�";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "��" + data[row_title][i]; 
					sgv  += "��" + v;	
				}else{
					jdmc += "��" + data[row_title][i]; 
					jdv  += "��" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "�����ϴ󣬷ֱ�����" + sgv.substring(1) +"�֡�";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "�����ϴ󣬷ֱ𽵵�" +jdv.substring(1) +"�֡�";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	����ú���������
	public static String getJicmfrlqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_by = 1;					//		����������
		int row_sy = 2;					//		���������� 
		int row_hb = 3;					//		����������  
		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		���¸��糧��ֵKcal/Kg
		String sql = "select d.mingc,qnetar from\n" +
			"(select diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
			"from yueslb s,yuezlb z,yuetjkjb t\n" + 
			"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"group by t.diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+)= d.id and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[4][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* ������ֵ��ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		������ֵ
		sql = "select d.mingc, round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id(+) = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"group by d.mingc order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* ������ֵ��ֵ */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_by][col_title] = yuef + "��";
		data[row_sy][col_title] = "����";
		data[row_hb][col_title] = "���Ȳ�ֵ";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = "+oraDate+"\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* ������ֵ��ֵ */
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
			"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d, itemsort o, item i\n" + 
			"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.mingc = d.id  and i.bianm = 'SS'\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_ssgs] = rsl.getString("qnetar");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d, itemsort o, item i\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
		"and i.mingc = d.id  and i.bianm = 'SS'\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
		"and t.riq = add_months("+oraDate+",-1)\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_ssgs] = rsl.getString("qnetar");
		}
		rsl.close();
//		���㻷�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			int by = parseInt(data[row_by][i]);
			int sy = parseInt(data[row_sy][i]);
			if(by ==0 || sy ==0 ){
				data[row_hb][i] = "";
			}else{
				data[row_hb][i] = String.valueOf(by - sy);
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		rt.setTitle(">����ú���������("+yuef + "��)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:ǧ��/ǧ��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=4; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			rslist.add(new String[]{data[0][i],data[1][i],data[3][i]});
		}
		for(int i=4 ; i<data[0].length;i++){
			double by = parseDouble(data[row_hb][i]);
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by>bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"����ú���������",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "";
		wenz = "����" + yuef + "�·ݹ�˾�볧ú��ֵ" + data[row_by][col_fgs] + "ǧ��/ǧ�ˣ�����";
		if(parseInt(data[row_hb][col_fgs])>0){
			wenz += "����" + Math.abs(parseInt(data[row_hb][col_fgs])) + "ǧ��/ǧ�ˡ�";
		}else if(parseInt(data[row_hb][col_fgs])<0){
			wenz += "�½�" + Math.abs(parseInt(data[row_hb][col_fgs])) + "ǧ��/ǧ�ˡ�";
		}else{
			wenz += "�ޱ仯��";
		}
		String jddw = "";
		String jdrz = "";
		boolean bhfdcc = false;
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])<-100){
				jddw += "��" + data[row_title][i] ;
				jdrz += "��" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		String hb = getJicmfrlqk_fgswzdb("��", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		jddw = "";
		jdrz = "";
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])>100){
				jddw += "��" + data[row_title][i] ;
				jdrz += "��" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		hb = getJicmfrlqk_fgswzdb("��", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		if(bhfdcc){
			wenz += "������λú������������";
		}else{
			wenz += "����λú�ʲ���������";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	����ú���������(�ۼ�)
	public static String getJicmfrllj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_by = 1;					//		�ۼ�������
		int row_sy = 2;					//		ͬ�������� 
		int row_hb = 3;					//		ͬ��������  
		int row_zb = 4;					//		ָ��
		int row_zbc = 5;				//		ָ���
		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		�ۼƵ糧��ֵKcal/Kg
		String sql = "select d.mingc,qnetar from\n" +
			"(select diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
			"from yueslb s,yuezlb z,yuetjkjb t\n" + 
			"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and z.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"group by t.diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+)= d.id and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[6][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* �ۼ���ֵ��ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		ͬ����ֵ
		sql = "select d.mingc, round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and z.fenx = '"+
		SysConstant.Fenx_Leij+"'\n" + "and t.diancxxb_id(+) = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-12)\n" + 
		"group by d.mingc order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* ͬ����ֵ��ֵ */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		��ָ��
		sql = "select rucmrz qnetar from jjfxnzbwh j, diancxxb d\n" +
			"where j.diancxxb_id(+) = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"and j.riq(+) = getyearfirstdate("+oraDate+")\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ��ָ����ֵ��ֵ */
		while(rsl.next()){
			data[row_zb][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_by][col_title] = yuef + "���ۼ�";
		data[row_sy][col_title] = "ͬ��";
		data[row_hb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_zb][col_title] = "���ָ��";
		data[row_zbc][col_title] = "��ָ���";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = "+oraDate+"\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* ������ֵ��ֵ */
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
		/*  ��ָ������  */
		sql = "select rucmrz qnetar from jjfxnzbwh j\n" +
		"where j.diancxxb_id = "+diancxxb_id+"\n" + 
		"and j.riq = getyearfirstdate("+oraDate+")\n";
		rsl = con.getResultSetList(sql);
		/* ��ָ����ֵ��ֵ */
		if(rsl.next()){
			data[row_zb][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
			"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d, itemsort o, item i\n" + 
			"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.mingc = d.id  and i.bianm = 'SS'\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_ssgs] = rsl.getString("qnetar");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d, itemsort o, item i\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
		"and i.mingc = d.id  and i.bianm = 'SS'\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
		"and t.riq = add_months("+oraDate+",-1)\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_ssgs] = rsl.getString("qnetar");
		}
		rsl.close();
//		���㻷�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			int by = parseInt(data[row_by][i]);
			int sy = parseInt(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(by - sy);
			}else{
				data[row_hb][i] = "";
			}
		}
//		������ָ���
		for(int i = col_fgs; i < data[0].length ; i++){
			int by = parseInt(data[row_by][i]);
			int zb = parseInt(data[row_zb][i]);
			if(by != 0 && zb != 0){
				data[row_zbc][i] = String.valueOf(by - zb);
			}else{
				data[row_zbc][i] = "";
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "���ۼ�";
		}
		rt.setTitle(">����ú���������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:ǧ��/ǧ��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=6; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(5, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(6, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			rslist.add(new String[]{data[0][i],data[1][i],data[3][i]});
		}
		for(int i=4 ; i<data[0].length;i++){
			double by = parseDouble(data[row_hb][i]);
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by>bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"����ú���������",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "";
		wenz = "����" + stryf + "��˾�볧ú��ֵ" + data[row_by][col_fgs] + "ǧ��/ǧ�ˣ�ͬ��";
		if(parseInt(data[row_hb][col_fgs])>0){
			wenz += "����" + Math.abs(parseInt(data[row_hb][col_fgs])) + "ǧ��/ǧ�ˡ�";
		}else if(parseInt(data[row_hb][col_fgs])<0){
			wenz += "�½�" + Math.abs(parseInt(data[row_hb][col_fgs])) + "ǧ��/ǧ�ˡ�";
		}else{
			wenz += "�ޱ仯��";
		}
		String jddw = "";
		String jdrz = "";
		boolean bhfdcc = false;
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])<-100){
				jddw += "��" + data[row_title][i] ;
				jdrz += "��" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		String hb = getJicmfrlqk_fgswzdb("��", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		jddw = "";
		jdrz = "";
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])>100){
				jddw += "��" + data[row_title][i] ;
				jdrz += "��" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		hb = getJicmfrlqk_fgswzdb("��", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		if(bhfdcc){
			wenz += "������λú������������";
		}else{
			wenz += "����λú�ʲ���������";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		wenz = "";
		jddw = "";
		for(int i = 3; i < data[row_zbc].length ; i++){
			if(parseInt(data[row_zbc][i])>0){
				jddw += "��" + data[row_title][i] ;
			}
		}
		if(!"".equals(jddw)){
			wenz += "����" + jddw.substring(1) + "�ۼ��볧��ֵ������ָ�ꣻ";
		}
		jddw = "";
		jdrz = "";
		for(int i = 3; i < data[row_zbc].length ; i++){
			if(parseInt(data[row_zbc][i])<0){
				jddw += "��" + data[row_title][i] ;
				jdrz += "��" + Math.abs(parseInt(data[row_zbc][i])) ;
			}
		}
		if(!"".equals(jddw)){
			wenz += jddw.substring(1) + "�ֱ�����ָ���" + jdrz.substring(1) + "ǧ��/ǧ�ˡ�";
		}
		if(!wenz.equals(""))
			html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	�볧��ú����������
	public static String getRucbmdjwcqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_by = 1;					//		����������
		int row_sy = 2;					//		���������� 
		int row_hb = 3;					//		����������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		���ڲ���˰��ú����
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"group by t.diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[4][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* ��ú���۸�ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		���Ȳ���˰��ú��
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.riq = add_months("+oraDate+",-1)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ���ȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_by][col_title] = yuef + "��";
		data[row_sy][col_title] = "����";
		data[row_hb][col_title] = "���Ȳ�ֵ";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+
			SysConstant.Fenx_Beny+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '" +
			SysConstant.Fenx_Beny+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
		"and i.bianm = 'SS' and i.mingc = d.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '" +
		SysConstant.Fenx_Beny+"'\n" + "and t.riq =add_months("+oraDate+",-1)\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		���㻷�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
//		if(!"1".equals(yuef)){
//			stryf = "1-" + yuef + "���ۼ�";
//		}
		rt.setTitle(">�볧��ú����������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=4; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
		}
		for(int i=4 ; i<data[0].length;i++){
			double by = parseDouble(data[row_hb][i]);
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by<bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"�볧��ú����",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "����" + yuef + "�¹�˾�볧��ú��" + data[row_by][col_fgs] + 
		"Ԫ/��";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "����������" + data[row_hb][col_fgs] + "Ԫ/�֡�";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "��";
			}else{
				wenz += "�����½�" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"Ԫ/�֡�";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "��" + data[row_title][i]; 
					sgv  += "��" + v;	
				}else{
					jdmc += "��" + data[row_title][i]; 
					jdv  += "��" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "�����ϴ󣬷ֱ�����" + sgv.substring(1) +"Ԫ/�֡�";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "�����ϴ󣬷ֱ𽵵�" +jdv.substring(1) +"Ԫ/�֡�";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	�볧��ú����������(�ۼ�)
	public static String getRucbmdjwcqklj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
//		int row_by = 1;					//		����������
//		int row_sy = 2;					//		���������� 
//		int row_hb = 3;					//		����������
		int row_lj = 1;					//		�ۼ�������
		int row_tq = 2;					//		�ۼ�ͬ��������
		int row_tb = 3;					//		ͬ�Ȳ�ֵ
		int row_tbl = 4;				//		ͬ��������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		���ڲ���˰��ú����
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"group by t.diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[5][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
//		�ۼƲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq = "+oraDate+"\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �ۼƱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		ͬ�ڲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq = add_months("+oraDate+",-12)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_lj][col_title] = yuef + "���ۼ�";
		data[row_tq][col_title] = "ͬ��";
		data[row_tb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_tbl][col_title] = "��(��)%";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  �ۼ�����  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+
			SysConstant.Fenx_Leij+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+
		SysConstant.Fenx_Leij+"'\n" + "and t.riq = add_months("+oraDate+",-12)\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  �ۼ�����  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '" +
			SysConstant.Fenx_Leij+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '" +
			SysConstant.Fenx_Leij+"'\n" + "and t.riq =add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		����ͬ�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "���ۼ�";
		}
		rt.setTitle(">�볧��ú����������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=5; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(5, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			rslist.add(new String[]{data[row_title][i],data[row_lj][i],data[row_tb][i]});
		}
		for(int i=4 ; i<data[0].length;i++){
			double by = parseDouble(data[row_tb][i]);
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by<bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_lj][i],data[row_tb][i]});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_lj][i],data[row_tb][i]});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"�볧��ú����",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "����" + yuef + "�¹�˾�볧��ú��" + data[row_lj][col_fgs] + 
		"Ԫ/��";
		
		if(parseDouble(data[row_tb][col_fgs])>0){
			wenz += "��ͬ������" + data[row_tb][col_fgs] + "Ԫ/�֡�";
		}else
			if(parseDouble(data[row_tb][col_fgs])==0){
				wenz += "��";
			}else{
				wenz += "ͬ���½�" + Math.abs(parseDouble(data[row_tb][col_fgs])) +
				"Ԫ/�֡�";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_tb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "��" + data[row_title][i]; 
					sgv  += "��" + v;	
				}else{
					jdmc += "��" + data[row_title][i]; 
					jdv  += "��" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "�����ϴ󣬷ֱ�����" + sgv.substring(1) +"Ԫ/�֡�";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "�����ϴ󣬷ֱ𽵵�" +jdv.substring(1) +"Ԫ/�֡�";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	�볧��ú�ۼ��뼯��ͬ����Ƚ�
	public static String getRucbmdjwcdb_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_lj = 1;					//		�ۼ�������
		int row_h = 2;					//		��������
		int row_hb = 3;					//		��������
		int row_tq = 4;					//		ͬ�������� 
		int row_tb = 5;					//		ͬ��������
		int row_tbl = 6;				//		ͬ��������
		int row_dbtitle = 7;			//		�Աȱ���
		int row_dblj = 8;				//		�Ա��ۼ�
		int row_dbh = 9;				//		�Ա�����
		int row_dbhb = 10;				//		�ԱȻ���
		int row_dbtq = 11;				//		�Ա�ͬ��
		int row_dbtb = 12;				//		�Ա�ͬ��
		int row_dbtbl = 13;				//		�Ա�ͬ��������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		���²���˰��ú����
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"group by t.diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[14][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* ��ú���۸�ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		���Ȳ���˰��ú��
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.riq = add_months("+oraDate+",-1)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_h][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		ͬ�ڲ���˰��ú��
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.riq = add_months("+oraDate+",-12)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�Աȹ�˾����
		sql = "select z.mingc,z.bmdj from\n" +
			"(select g.diancxxb_id, d.mingc, l.bmdj from\n" + 
			"(select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+
			SysConstant.Fenx_Beny+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g, diancxxb d\n" + 
			"where l.fgsid = g.fgs_id and d.id = g.fgs_id) z, diancxxb d\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �Աȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_dbtitle][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_dblj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�Աȹ�˾���� 
		sql = "select d.mingc,z.bmdj from\n" +
			"(select g.diancxxb_id, l.bmdj from\n" + 
			"(select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+
			SysConstant.Fenx_Beny+"'\n" + "and t.riq =add_months("+oraDate+",-1)\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �Աȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_dbh][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�Աȹ�˾ͬ�� 
		sql = "select d.mingc,z.bmdj from\n" +
			"(select g.diancxxb_id, l.bmdj from\n" + 
			"(select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+
			SysConstant.Fenx_Beny+"'\n" + "and t.riq =add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �Աȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_dbtq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_lj][col_title] = yuef + "��";
		data[row_h][col_title] = "����";
		data[row_hb][col_title] = "����";
		data[row_tq][col_title] = "ͬ��";
		data[row_tb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_tbl][col_title] = "��(��)%";
		data[row_dbtitle][col_title] = "";
		data[row_dblj][col_title] = yuef + "��";
		data[row_dbh][col_title] = "����";
		data[row_dbhb][col_title] = "����";
		data[row_dbtq][col_title] = "ͬ��";
		data[row_dbtb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_dbtbl][col_title] = "��(��)%";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		data[row_dbtitle][col_fgs] = "����";
		/*  ��������  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+
			"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
			"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+
		"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.riq =add_months("+oraDate+",-1)\n" + 
		"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
		"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_h][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+
		"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.riq =add_months("+oraDate+",-12)\n" + 
		"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
		"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա��ۼ�����  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+
			"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"and t.diancxxb_id = d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dblj][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �ԱȻ�������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+
			"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = add_months("+oraDate+",-1)\n" + 
			"and t.diancxxb_id = d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dbh][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա�ͬ������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+
			"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dbtq][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		data[row_dbtitle][col_ssgs] = "��Ӧ����";
		/*  �ۼ�����  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" +
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+
			"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
			"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" +
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+
			"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq =add_months("+oraDate+",-1)\n" + 
			"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
			"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_h][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" +
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+
			"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq =add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
			"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Աȵ�������  */
		sql = 
			"select round_new(decode(sum(z.jingz),0,0,\n" +
			"sum(z.bmdj * z.jingz)/sum(z.jingz)),2) bmdj from\n" + 
			"(select g.diancxxb_id,l.jingz, l.bmdj from\n" + 
			"(select d.fgsid,sum(s.jingz) jingz,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d, itemsort o, item i\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dblj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա���������  */
		sql = 
			"select round_new(decode(sum(z.jingz),0,0,\n" +
			"sum(z.bmdj * z.jingz)/sum(z.jingz)),2) bmdj from\n" + 
			"(select g.diancxxb_id,l.jingz, l.bmdj from\n" + 
			"(select d.fgsid,sum(s.jingz) jingz,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = add_months("+oraDate+",-1)\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d, itemsort o, item i\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dbtq][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա�ͬ������  */
		sql = 
			"select round_new(decode(sum(z.jingz),0,0,\n" +
			"sum(z.bmdj * z.jingz)/sum(z.jingz)),2) bmdj from\n" + 
			"(select g.diancxxb_id,l.jingz, l.bmdj from\n" + 
			"(select d.fgsid,sum(s.jingz) jingz,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d, itemsort o, item i\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dbtq][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		���㻷���ڲ�ֵ
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_h][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����ԱȻ����ڲ�ֵ
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_dblj][i]);
			double sy = parseDouble(data[row_dbh][i]);
			if(by != 0 && sy != 0){
				data[row_dbhb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		���㱾ͬ�ڲ�ֵ
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����Աȱ�ͬ�ڲ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dblj][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		���㱾ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		����Աȱ�ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dbtb][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
//		if(!"1".equals(yuef)){
//			stryf = "1-" + yuef + "���ۼ�";
//		}
		rt.setTitle(">�볡��ú����������("+stryf+")���뼯��ͬ����Ƚ�", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=14; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(5, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(6, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(7, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(8, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(9, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(10, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(11, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(12, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(13, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(14, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			double dbzdgj = parseDouble(data[row_lj][i]);
			double dbqt = parseDouble(data[row_dblj][i]);
			String dbc = "0";
			if(dbzdgj != 0 && dbqt != 0){
				dbc = String.valueOf(CustomMaths.sub(dbzdgj, dbqt));
			}
			rslist.add(new String[]{data[row_title][i],data[row_lj][i],dbc});
		}
		for(int i=4 ; i<data[0].length;i++){
			double dbzdgj = parseDouble(data[row_lj][i]);
			double dbqt = parseDouble(data[row_dblj][i]);
			double by = CustomMaths.sub(dbzdgj, dbqt);
			if(dbqt==0){
				by = 0;
			}
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by<bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_lj][i],String.valueOf(by)});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_lj][i],String.valueOf(by)});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"�볧��ú���۶ԱȲ�",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "";
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	�ۼ��볧��ú�����뼯�Ź�˾ͬ����Ա�
	public static String getRucbmdjwcdblj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_lj = 1;					//		�ۼ�������
		int row_tq = 2;					//		ͬ�������� 
		int row_tb = 3;					//		ͬ��������
		int row_tbl = 4;				//		ͬ��������
		int row_dbtitle = 5;			//		�Աȱ���
		int row_dblj = 6;				//		�Ա��ۼ�
		int row_dbtq = 7;				//		�Ա�ͬ��
		int row_dbtb = 8;				//		�Ա�ͬ��
		int row_dbtbl = 9;				//		�Ա�ͬ��������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		�ۼƲ���˰��ú����
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"group by t.diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[10][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* ��ú���۸�ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		ͬ�ڲ���˰��ú��
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq = add_months("+oraDate+",-12)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�ۼƲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq = "+oraDate+"\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �ۼƱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		ͬ�ڲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq = add_months("+oraDate+",-12)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�Աȹ�˾�ۼ�
		sql = "select z.mingc,z.bmdj from\n" +
			"(select g.diancxxb_id, d.mingc, l.bmdj from\n" + 
			"(select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+
			SysConstant.Fenx_Leij+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g, diancxxb d\n" + 
			"where l.fgsid = g.fgs_id and d.id = g.fgs_id) z, diancxxb d\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �Աȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_dbtitle][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_dblj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�Աȹ�˾ͬ�� 
		sql = "select d.mingc,z.bmdj from\n" +
			"(select g.diancxxb_id, l.bmdj from\n" + 
			"(select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+
			SysConstant.Fenx_Leij+"'\n" + "and t.riq =add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �Աȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_dbtq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_lj][col_title] = yuef + "���ۼ�";
		data[row_tq][col_title] = "ͬ��";
		data[row_tb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_tbl][col_title] = "��(��)%";
		data[row_dbtitle][col_title] = "";
		data[row_dblj][col_title] = yuef + "���ۼ�";
		data[row_dbtq][col_title] = "ͬ��";
		data[row_dbtb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_dbtbl][col_title] = "��(��)%";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		data[row_dbtitle][col_fgs] = "����";
		/*  �ۼ�����  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+
			"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
			"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+
		"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq =add_months("+oraDate+",-12)\n" + 
		"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
		"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա��ۼ�����  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+
			"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"and t.diancxxb_id = d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dblj][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա�ͬ������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+
			"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq = add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dbtq][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		data[row_dbtitle][col_ssgs] = "��Ӧ����";
		/*  �ۼ�����  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" +
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+
			"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
			"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select d.fgsid,round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" +
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+
			"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq =add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id and d.fgsid = "+diancxxb_id+"\n" + 
			"group by d.fgsid";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա��ۼ�����  */
		sql = 
			"select round_new(decode(sum(z.jingz),0,0,\n" +
			"sum(z.bmdj * z.jingz)/sum(z.jingz)),2) bmdj from\n" + 
			"(select g.diancxxb_id,l.jingz, l.bmdj from\n" + 
			"(select d.fgsid,sum(s.jingz) jingz,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d, itemsort o, item i\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dblj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �Ա�ͬ������  */
		sql = 
			"select round_new(decode(sum(z.jingz),0,0,\n" +
			"sum(z.bmdj * z.jingz)/sum(z.jingz)),2) bmdj from\n" + 
			"(select g.diancxxb_id,l.jingz, l.bmdj from\n" + 
			"(select d.fgsid,sum(s.jingz) jingz,round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, vwdianc d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
			"and t.riq = add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id group by d.fgsid) l, jjfxdbgs g\n" + 
			"where l.fgsid = g.fgs_id) z, diancxxb d, itemsort o, item i\n" + 
			"where d.id = z.diancxxb_id(+) and d.fuid = "+diancxxb_id+"\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_dbtq][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		
//		���㱾ͬ�ڲ�ֵ
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����Աȱ�ͬ�ڲ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dblj][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		���㱾ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		���㱾ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dbtb][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "���ۼ�";
		}
		rt.setTitle(">�볡��ú����������("+stryf+")���뼯��ͬ����Ƚ�", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=10; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(5, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(6, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(7, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(8, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(9, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(10, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			double dbzdgj = parseDouble(data[row_lj][i]);
			double dbqt = parseDouble(data[row_dblj][i]);
			String dbc = "0";
			if(dbzdgj != 0 && dbqt != 0){
				dbc = String.valueOf(CustomMaths.sub(dbzdgj, dbqt));
			}
			rslist.add(new String[]{data[row_title][i],data[row_lj][i],dbc});
		}
		for(int i=4 ; i<data[0].length;i++){
			double dbzdgj = parseDouble(data[row_lj][i]);
			double dbqt = parseDouble(data[row_dblj][i]);
			double by = CustomMaths.sub(dbzdgj, dbqt);
			if(dbqt==0){
				by = 0;
			}
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by<bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_lj][i],String.valueOf(by)});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_lj][i],String.valueOf(by)});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"�볧��ú���۶ԱȲ�",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "";
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	��¯��ú����������
	public static String getRulbmdjwcqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_by = 1;					//		����������
		int row_sy = 2;					//		���������� 
		int row_hb = 3;					//		����������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		���ڲ���˰��ú����
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
			"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
			"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =" +oraDate +") y" +
			",diancxxb d\n" +
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[4][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* ��ú���۸�ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		���Ȳ���˰��ú��
		sql = "select d.mingc,y.bmdj from\n" +
		"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
		"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
		"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =add_months(" +oraDate +",-1)) y" +
		",diancxxb d\n" +
		"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ���ȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_by][col_title] = yuef + "��";
		data[row_sy][col_title] = "����";
		data[row_hb][col_title] = "���Ȳ�ֵ";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  ��������  */
		sql = 
			"select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
			"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
			"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
			"from yuezbb y, diancxxb d\n" + 
			"where y.diancxxb_id = d.id\n" + 
			"  and d.fuid ="+diancxxb_id+"\n" + 
			"and y.fenx = '"+SysConstant.Fenx_Beny+"' and y.riq ="+oraDate+"\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = 
			"select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
			"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
			"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
			"from yuezbb y, diancxxb d\n" + 
			"where y.diancxxb_id = d.id\n" + 
			"  and d.fuid ="+diancxxb_id+"\n" + 
			"and y.fenx = '"+SysConstant.Fenx_Beny+"' and y.riq =add_months("+oraDate+",-1)\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  ��������  */
		sql = "select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
		"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
		"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
		"from yuezbb y, diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id = d.id\n" + 
		"  and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"  and i.bianm = 'SS' and i.mingc = d.id\n" + 
		"  and d.fuid ="+diancxxb_id+"\n" + 
		"and y.fenx = '"+SysConstant.Fenx_Beny+"' and y.riq ="+oraDate+"\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = "select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
		"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
		"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
		"from yuezbb y, diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id = d.id\n" + 
		"  and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"  and i.bianm = 'SS' and i.mingc = d.id\n" + 
		"  and d.fuid ="+diancxxb_id+"\n" + 
		"and y.fenx = '"+SysConstant.Fenx_Beny+"' and y.riq =add_months("+oraDate+",-1)\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		���㻷�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
//		if(!"1".equals(yuef)){
//			stryf = "1-" + yuef + "���ۼ�";
//		}
		rt.setTitle(">�ۺϱ�ú����������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=4; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
////		����ͼ��
//		/*  ����ͼ����������  */
//		String[] colname = new String[]{"��λ","����","���Ȳ�"};
//		rsl = new ResultSetList();
//		rsl.setColumnNames(colname);
//		List rslist = new ArrayList(); 
//		for(int i = 1; i <= 3 ; i++){
//			rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
//		}
//		for(int i=4 ; i<data[0].length;i++){
//			double by = parseDouble(data[row_hb][i]);
//			for(int j=2; j < rslist.size(); j++){
//				String[] s = (String[])rslist.get(j);
//				double bj = parseDouble(s[2]);
//				if(by<bj){
//					rslist.add(j,new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
//					break;
//				}else
//					if(j+1==rslist.size()){
//						rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
//						break;
//					}
//			}
//		}
//		rsl.setResultSetlist(rslist);
//		/*  ��ʼ��ͼ��  */
//		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
//				"�볧��ú����",200,600);
//		ct.xTiltShow = true;
//		ct.showLegend = false;
//		/*  ���õ�����״ͼд��html  */
//		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "����" + yuef + "�¹�˾�ۺϱ�ú��" + data[row_by][col_fgs] + 
		"Ԫ/��";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "����������" + data[row_hb][col_fgs] + "Ԫ/�֡�";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "��";
			}else{
				wenz += "�����½�" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"Ԫ/�֡�";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "��" + data[row_title][i]; 
					sgv  += "��" + v;	
				}else{
					jdmc += "��" + data[row_title][i]; 
					jdv  += "��" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "�����ϴ󣬷ֱ�����" + sgv.substring(1) +"Ԫ/�֡�";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "�����ϴ󣬷ֱ𽵵�" +jdv.substring(1) +"Ԫ/�֡�";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	��¯��ú����������(�ۼ�)
	public static String getRulbmdjwcqklj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
//		int row_by = 1;					//		����������
//		int row_sy = 2;					//		���������� 
//		int row_hb = 3;					//		����������
		int row_lj = 1;					//		�ۼ�������
		int row_tq = 2;					//		�ۼ�ͬ��������
		int row_tb = 3;					//		ͬ�Ȳ�ֵ
		int row_tbl = 4;				//		ͬ��������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		���ڲ���˰��ú����
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
			"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
			"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =" +oraDate +") y" +
			",diancxxb d\n" +
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[5][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
//		�ۼƲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
		"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
		"where z.fenx = '"+SysConstant.Fenx_Leij+"' and z.riq =" +oraDate +") y" +
		",diancxxb d\n" +
		"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �ۼƱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		ͬ�ڲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
		"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
		"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =add_months(" +oraDate +",-12)) y" +
		",diancxxb d\n" +
		"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_lj][col_title] = yuef + "���ۼ�";
		data[row_tq][col_title] = "ͬ��";
		data[row_tb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_tbl][col_title] = "��(��)%";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  �ۼ�����  */
		sql ="select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
		"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
		"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
		"from yuezbb y, diancxxb d\n" + 
		"where y.diancxxb_id = d.id\n" + 
		"  and d.fuid ="+diancxxb_id+"\n" + 
		"and y.fenx = '"+SysConstant.Fenx_Leij+"' and y.riq ="+oraDate+"\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
		"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
		"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
		"from yuezbb y, diancxxb d\n" + 
		"where y.diancxxb_id = d.id\n" + 
		"  and d.fuid ="+diancxxb_id+"\n" + 
		"and y.fenx = '"+SysConstant.Fenx_Leij+"' and y.riq =add_months("+oraDate+",-12)\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  �ۼ�����  */
		sql = "select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
		"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
		"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
		"from yuezbb y, diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id = d.id\n" + 
		"  and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"  and i.bianm = 'SS' and i.mingc = d.id\n" + 
		"  and d.fuid ="+diancxxb_id+"\n" + 
		"and y.fenx = '"+SysConstant.Fenx_Leij+"' and y.riq ="+oraDate+"\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select decode(sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),0,0,\n" +
		"round_new(sum(y.rultrmpjdj*29271*y.rulmzbzml)/\n" + 
		"sum(decode(y.rultrmpjdj,0,0,y.rulmzbzml)*y.rultrmpjfrl),2)) bmdj\n" + 
		"from yuezbb y, diancxxb d, itemsort o, item i\n" + 
		"where y.diancxxb_id = d.id\n" + 
		"  and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" +
		"  and i.bianm = 'SS' and i.mingc = d.id\n" + 
		"  and d.fuid ="+diancxxb_id+"\n" + 
		"and y.fenx = '"+SysConstant.Fenx_Leij+"' and y.riq =add_months("+oraDate+",-12)\n";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		����ͬ�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "���ۼ�";
		}
		rt.setTitle(">�ۺϱ�ú����������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=5; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(5, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
//		String[] colname = new String[]{"��λ","����","���Ȳ�"};
//		rsl = new ResultSetList();
//		rsl.setColumnNames(colname);
//		List rslist = new ArrayList(); 
//		for(int i = 1; i <= 3 ; i++){
//			rslist.add(new String[]{data[row_title][i],data[row_lj][i],data[row_tb][i]});
//		}
//		for(int i=4 ; i<data[0].length;i++){
//			double by = parseDouble(data[row_tb][i]);
//			for(int j=2; j < rslist.size(); j++){
//				String[] s = (String[])rslist.get(j);
//				double bj = parseDouble(s[2]);
//				if(by<bj){
//					rslist.add(j,new String[]{data[row_title][i],data[row_lj][i],data[row_tb][i]});
//					break;
//				}else
//					if(j+1==rslist.size()){
//						rslist.add(new String[]{data[row_title][i],data[row_lj][i],data[row_tb][i]});
//						break;
//					}
//			}
//		}
//		rsl.setResultSetlist(rslist);
//		/*  ��ʼ��ͼ��  */
//		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
//				"�볧��ú����",200,600);
//		ct.xTiltShow = true;
//		ct.showLegend = false;
//		/*  ���õ�����״ͼд��html  */
//		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "����" + yuef + "�¹�˾�ۺϱ�ú��" + data[row_lj][col_fgs] + 
		"Ԫ/��";
		
		if(parseDouble(data[row_tb][col_fgs])>0){
			wenz += "��ͬ������" + data[row_tb][col_fgs] + "Ԫ/�֡�";
		}else
			if(parseDouble(data[row_tb][col_fgs])==0){
				wenz += "��";
			}else{
				wenz += "ͬ���½�" + Math.abs(parseDouble(data[row_tb][col_fgs])) +
				"Ԫ/�֡�";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_tb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "��" + data[row_title][i]; 
					sgv  += "��" + v;	
				}else{
					jdmc += "��" + data[row_title][i]; 
					jdv  += "��" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "�����ϴ󣬷ֱ�����" + sgv.substring(1) +"Ԫ/�֡�";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "�����ϴ󣬷ֱ𽵵�" +jdv.substring(1) +"Ԫ/�֡�";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	�볧����¯ú�۱�ú���۲�ֵ���һ����
	public static String getRucrlbmdjc_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		HashMap rowmap = new HashMap();
		int col_title = 0;				//		������
		int col_ylj = 13;				//		���ۼ���
		int col_nzb = 14;				//		��ָ����
		int row_title = 0;				//		������
		int row_fgs = 1;				//		�ֹ�˾��

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
		
		String sql = "select id,mingc from diancxxb where id = "+diancxxb_id+
		" or fuid = " + diancxxb_id + " order by decode(id,"+diancxxb_id+",-999,xuh),id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[rsl.getRows() + 1][15];
		int[] colwidth = new int[15];
		for(int i = 0; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		while(rsl.next()){
			rowmap.put(rsl.getString("id"), String.valueOf(rsl.getRow() + 1));
			/*  �������и�ֵ  */
			data[rsl.getRow()+1][col_title] = rsl.getString("mingc");
		}
		rsl.close();

//		����糧���µ��۲�
		sql = "select d.id,d.mingc,d.riq,rc.rcdj-rl.rldj djc from\n" +
			"(select t.diancxxb_id,to_char(t.riq,'mm') riq,\n" + 
			"round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(y.buhsbmdj*s.jingz) /sum(jingz)),2) rcdj\n" + 
			"from yueslb s,yuercbmdj y, yuetjkjb t\n" + 
			"where y.yuetjkjb_id = t.id and y.fenx ='"+SysConstant.Fenx_Beny+"'\n" + 
			"and s.yuetjkjb_id = t.id and s.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and to_char(t.riq,'yyyy') = '"+nianf+"'\n" + 
			"group by t.diancxxb_id,to_char(t.riq,'mm') ) rc,\n" + 
			"(select diancxxb_id,to_char(z.riq,'mm') riq,\n" + 
			"round_new(decode(sum(z.fadmzbml),0,0,\n" + 
			"sum((z.fadmcb + z.qiz_ranm)/10000)/sum(z.fadmzbml)),2) rldj\n" + 
			"from yuezbb z where z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and to_char(z.riq,'yyyy') = '"+nianf+"'\n" + 
			"group by diancxxb_id,to_char(z.riq,'mm') ) rl,\n" + 
			"(select d.id,d.mingc,y.mvalue riq from diancxxb d, yuefb y\n" + 
			"where d.fuid =  "+diancxxb_id+") d\n" + 
			"where d.id = rc.diancxxb_id(+) and d.id = rl.diancxxb_id(+)\n" + 
			"and d.riq = rc.riq(+) and d.riq = rl.riq(+)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			int col = rsl.getInt("riq");
			int row = parseInt((String)rowmap.get(rsl.getString("id")));
			if(row != 0){
				data[row][col] = rsl.getString("djc");
			}
		}
		rsl.close();
//		����ֹ�˾���µ��۲�
		sql = "select yf.mvalue riq,rc.rcdj-rl.rldj djc from\n" +
		"(select to_char(t.riq,'mm') riq,\n" + 
		"round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(y.buhsbmdj*s.jingz) /sum(jingz)),2) rcdj\n" + 
		"from yueslb s,yuercbmdj y, yuetjkjb t, diancxxb d\n" + 
		"where y.yuetjkjb_id = t.id and y.fenx ='"+SysConstant.Fenx_Beny+"'\n" + 
		"and s.yuetjkjb_id = t.id and s.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
		"and to_char(t.riq,'yyyy') = '"+nianf+"'\n" + 
		"group by to_char(t.riq,'mm') ) rc,\n" + 
		"(select to_char(z.riq,'mm') riq,\n" + 
		"round_new(decode(sum(z.fadmzbml),0,0,\n" + 
		"sum((z.fadmcb + z.qiz_ranm)/10000)/sum(z.fadmzbml)),2) rldj\n" + 
		"from yuezbb z,diancxxb d where z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and z.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
		"and to_char(z.riq,'yyyy') = '"+nianf+"'\n" + 
		"group by to_char(z.riq,'mm') ) rl, yuefb yf\n" + 
		"where yf.mvalue = rc.riq(+) and yf.mvalue = rl.riq(+)";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			int col = rsl.getInt("riq");
			if(col > 0){
				data[row_fgs][col] = rsl.getString("djc");
			}
		}
		rsl.close();
//		�����ۼƵ��۲�
		sql = "select d.id,d.mingc,rc.rcdj-rl.rldj djc from\n" +
			"(select decode(d.id,null,"+diancxxb_id+",d.id) diancxxb_id,\n" + 
			"round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(y.buhsbmdj*s.jingz) /sum(jingz)),2) rcdj\n" + 
			"from yueslb s,yuercbmdj y, yuetjkjb t, diancxxb d\n" + 
			"where y.yuetjkjb_id = t.id and y.fenx ='"+SysConstant.Fenx_Beny+"'\n" + 
			"and s.yuetjkjb_id = t.id and s.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"and to_char(t.riq,'yyyy') = '"+nianf+"'\n" + 
			"group by rollup(d.id) ) rc,\n" + 
			"(select decode(d.id,null,"+diancxxb_id+",d.id) diancxxb_id,\n" + 
			"round_new(decode(sum(z.fadmzbml),0,0,\n" + 
			"sum((z.fadmcb + z.qiz_ranm)/10000)/sum(z.fadmzbml)),2) rldj\n" + 
			"from yuezbb z, diancxxb d where z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and to_char(z.riq,'yyyy') = '"+nianf+"'\n" + 
			"and z.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"group by rollup(d.id) ) rl, diancxxb d\n" + 
			"where d.id = rc.diancxxb_id(+) and d.id = rl.diancxxb_id(+)\n" + 
			"and (d.fuid = "+diancxxb_id+" or d.id = "+diancxxb_id+")";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			int row = parseInt((String)rowmap.get(rsl.getString("id")));
//			if(row == 0){
//				row = row_fgs;
//			}
			data[row][col_ylj] = rsl.getString("djc");
		}
		rsl.close();
//		ָ��
		sql = 
			"select d.id,d.mingc,(z.rucbmdj-z.rulmzbmdj) djc from jjfxnzbwh z, diancxxb d\n" +
			"where d.id = z.diancxxb_id(+) and z.riq = getyearfirstdate("+oraDate+")\n" + 
			"and (d.fuid = "+diancxxb_id+" or d.id = "+diancxxb_id+")\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			int row = parseInt((String)rowmap.get(rsl.getString("id")));
//			if(row == 0){
//				row = row_fgs;
//			}
			data[row][col_nzb] = rsl.getString("djc");
		}
		rsl.close();
//		������
		data[row_title][col_title] = "��λ"; 
		for(int i = 1; i < 13; i++ ){
			data[row_title][i] = i + "��";
		}
		data[row_title][col_ylj] = "�ۼ�";
		data[row_title][col_nzb] = "��ָ��";
		
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		rt.setTitle(">�볧����¯ú�۱�ú���۲�ֵ���("+yuef + "��)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=data.length; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			for(int j = 2; j <= data.length; j++ ){
				rt.body.setCellAlign(j, i, Table.ALIGN_RIGHT);
			}
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
		return html;
	}
//	�볧����¯��ֵ��
	public static String getRezcqk_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		String strDateBQ=nianf + "-" + yuef + "-01";
		String strDateNC=nianf + "-01-01";
		Report rt = new Report();
		
		String sql="select rc.mingc as ��λ,round_new((rc.����-rl.����)*1000/4.1816,0) as ����,\n" +
		"       round((rc.�ۼ�-rl.�ۼ�)*1000/4.1816,0) as �ۼ�,\n" + 
		"       round((rc.ͬ��-rl.ͬ��)*1000/4.1816,0) as ͬ��,round((rc.�ۼ�-rl.�ۼ�)*1000/4.1816,0)-round((rc.ͬ��-rl.ͬ��)*1000/4.1816,0) as ����,\n"+
		"       zb.rezc as ָ��, round((rc.�ۼ�-rl.�ۼ�)*1000/4.1816,0)-zb.rezc as ��ֵ\n" + 
		" from (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    grouping(ss) as ssfz, ss,grouping(dd.mingc) dcfz,max(dd.xuh) as dcxh,\n" + 
		"    round_new(decode(sum(decode(fenx,'���±���',rucsl)),0,0,sum(decode(fenx,'���±���',rucsl*rucrl)))/sum(decode(fenx,'���±���',rucsl)),2) as ����,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼƱ���',rucsl)),0,0,sum(decode(fenx,'�ۼƱ���',rucsl*rucrl)))/sum(decode(fenx,'�ۼƱ���',rucsl)),2) as �ۼ�,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼ�ͬ��',rucsl)),0,0,sum(decode(fenx,'�ۼ�ͬ��',rucsl*rucrl)))/sum(decode(fenx,'�ۼ�ͬ��',rucsl)),2) as ͬ��\n" + 
		" from ( select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id,sl.laimsl as rucsl,decode(dc.id,141,zl.diancrz,zl.qnet_ar) as rucrl,\n" + 
		"              sl.fenx||decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'����','ͬ��') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'������','�й�����') as ss\n" + 
		"                   from  yueslb sl, yuetjkjb tj, yuezlb zl,vwdianc dc,(select d.id\n" + 
		"                     from diancxxb d, itemsort s, item t\n" + 
		"                     where s.bianm = 'DCSFSS'\n" + 
		"                     and s.id = t.itemsortid\n" + 
		"                     and t.mingc = d.id and t.bianm = 'SS') ss--���з�����\n" + 
		"                   where zl.yuetjkjb_id=sl.yuetjkjb_id\n" + 
		"                         and sl.yuetjkjb_id = tj.id\n" + 
		"                         and sl.fenx=zl.fenx(+)\n" + 
		"                         and ss.id(+)=dc.id\n" + 
		"                         and (tj.riq = to_date('"+strDateBQ+"','yyyy-mm-dd') or tj.riq = add_months( to_date('"+strDateBQ+"','yyyy-mm-dd'),-12))\n" + 
		"                         and dc.fuid="+diancxxb_id+"\n" + 
		"                         and tj.diancxxb_id = dc.id) dd\n" + 
		"        group by rollup(dd.ss,dd.mingc)  ) rc,\n" + 
		" (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    round_new(decode(sum(decode(fenx,'���±���',rulsl)),0,0,sum(decode(fenx,'���±���',rulsl*rulrz)))/sum(decode(fenx,'���±���',rulsl))/1000,2) as ����,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼƱ���',rulsl)),0,0,sum(decode(fenx,'�ۼƱ���',rulsl*rulrz)))/sum(decode(fenx,'�ۼƱ���',rulsl))/1000,2) as �ۼ�,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼ�ͬ��',rulsl)),0,0,sum(decode(fenx,'�ۼ�ͬ��',rulsl*rulrz)))/sum(decode(fenx,'�ۼ�ͬ��',rulsl))/1000,2) as ͬ��\n" + 
		" from  (select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id, zb.fenx||decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'����','ͬ��') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'������','�й�����') as ss,zb.fadgrytrml as rulsl,zb.rultrmpjfrl as rulrz\n" + 
		"                 from yuezbb zb,vwdianc dc,(select d.id\n" + 
		"                     from diancxxb d, itemsort s, item t\n" + 
		"                     where s.bianm = 'DCSFSS'\n" + 
		"                     and s.id = t.itemsortid\n" + 
		"                     and t.mingc = d.id and t.bianm = 'SS') ss\n" + 
		"                 where zb.diancxxb_id=dc.id\n" + 
		"                 and (zb.riq = to_date('"+strDateBQ+"','yyyy-mm-dd') or zb.riq =add_months( to_date('"+strDateBQ+"','yyyy-mm-dd'),-12))\n" + 
		"                 and ss.id(+)=dc.id\n" + 
		"                 and dc.fuid="+diancxxb_id+") dd\n" + 
		" group by rollup(dd.ss,dd.mingc)  ) rl,\n" + 
		" (select dc.mingc, rezc\n" +
		"        from jjfxnzbwh zb,vwdianc dc\n" + 
		"        where zb.riq=to_date('"+strDateNC+"','yyyy-mm-dd')\n" + 
		"        and zb.diancxxb_id=dc.id\n" + 
		"        and (dc.fuid="+diancxxb_id+" or dc.id="+diancxxb_id+")) zb\n" + 
		"  where rc.mingc=rl.mingc(+) and rc.mingc=zb.mingc(+) \n" + 
		"  and rc.mingc<>'������'\n" + 
		"  order by dcfz desc,ssfz desc,dcxh\n";

		ResultSetList rsl = con.getResultSetList(sql);
		
		//�����������溬��table��ͷ
		String[][] data = new String[7][rsl.getRows() +1];
		
		//�п�����
		int[] colwidth = new int[rsl.getRows() + 1];
		colwidth[0] = 75;
		for(int i =1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		
		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[0][j]=rsl.getString("��λ");
			data[1][j]=rsl.getString("����");
			data[2][j]=rsl.getString("�ۼ�");
			data[3][j]=rsl.getString("ͬ��");
			data[4][j]=rsl.getString("����");
			data[5][j]="��"+rsl.getString("ָ��");
			data[6][j]=rsl.getString("��ֵ");
		}
		
		data[1][0]=yuef+"��";
		data[2][0]="1-"+yuef+"���ۼ�";
		data[3][0]="ͬ��";
		data[4][0]="����";
		data[5][0]="���Ŀ��ֵ";
		data[6][0]="�����Ŀ���ֵ";
		
		rt.setTitle("1-"+yuef+"����ֵ�����", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:ǧ����ǧ��", 2);
		
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		rt.body.setFixedRows(1);
		rt.body.setFixedCols(2);
		
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i =1; i <= colwidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(1,Table.ALIGN_CENTER );
		rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		//�������Ϣ����html String
		html = rt.getAllPagesHtml();
		
		//�������ַ���
		String wenz = "";
		
		//����ͼ��
		sql="select rc.mingc as ��λ,round_new((rc.����-rl.����)*1000/4.1816,0) as ��ֵ��\n" +
		" from (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    grouping(ss) as ssfz, ss,grouping(dd.mingc) dcfz,max(dd.xuh) as dcxh,\n" + 
		"    round_new(decode(sum(decode(fenx,'���±���',rucsl)),0,0,sum(decode(fenx,'���±���',rucsl*rucrl)))/sum(decode(fenx,'���±���',rucsl)),2) as ����,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼƱ���',rucsl)),0,0,sum(decode(fenx,'�ۼƱ���',rucsl*rucrl)))/sum(decode(fenx,'�ۼƱ���',rucsl)),2) as �ۼ�,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼ�ͬ��',rucsl)),0,0,sum(decode(fenx,'�ۼ�ͬ��',rucsl*rucrl)))/sum(decode(fenx,'�ۼ�ͬ��',rucsl)),2) as ͬ��\n" + 
		" from ( select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id,sl.laimsl as rucsl,decode(dc.id,141,zl.diancrz,zl.qnet_ar) as rucrl,\n" + 
		"              sl.fenx||decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'����','ͬ��') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'������','�й�����') as ss\n" + 
		"                   from  yueslb sl, yuetjkjb tj, yuezlb zl,vwdianc dc,(select d.id\n" + 
		"                     from diancxxb d, itemsort s, item t\n" + 
		"                     where s.bianm = 'DCSFSS'\n" + 
		"                     and s.id = t.itemsortid\n" + 
		"                     and t.mingc = d.id and t.bianm = 'SS') ss--���з�����\n" + 
		"                   where zl.yuetjkjb_id=sl.yuetjkjb_id\n" + 
		"                         and sl.yuetjkjb_id = tj.id\n" + 
		"                         and sl.fenx=zl.fenx(+)\n" + 
		"                         and ss.id(+)=dc.id\n" + 
		"                         and (tj.riq = to_date('"+strDateBQ+"','yyyy-mm-dd') or tj.riq = add_months( to_date('"+strDateBQ+"','yyyy-mm-dd'),-12))\n" + 
		"                         and dc.fuid="+diancxxb_id+"\n" + 
		"                         and tj.diancxxb_id = dc.id) dd\n" + 
		"        group by rollup(dd.ss,dd.mingc)  ) rc,\n" + 
		" (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    round_new(decode(sum(decode(fenx,'���±���',rulsl)),0,0,sum(decode(fenx,'���±���',rulsl*rulrz)))/sum(decode(fenx,'���±���',rulsl))/1000,2) as ����,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼƱ���',rulsl)),0,0,sum(decode(fenx,'�ۼƱ���',rulsl*rulrz)))/sum(decode(fenx,'�ۼƱ���',rulsl))/1000,2) as �ۼ�,\n" + 
		"    round_new(decode(sum(decode(fenx,'�ۼ�ͬ��',rulsl)),0,0,sum(decode(fenx,'�ۼ�ͬ��',rulsl*rulrz)))/sum(decode(fenx,'�ۼ�ͬ��',rulsl))/1000,2) as ͬ��\n" + 
		" from  (select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id, zb.fenx||decode(riq,to_date('2009-7-01','yyyy-mm-dd'),'����','ͬ��') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'������','�й�����') as ss,zb.fadgrytrml as rulsl,zb.rultrmpjfrl as rulrz\n" + 
		"                 from yuezbb zb,vwdianc dc,(select d.id\n" + 
		"                     from diancxxb d, itemsort s, item t\n" + 
		"                     where s.bianm = 'DCSFSS'\n" + 
		"                     and s.id = t.itemsortid\n" + 
		"                     and t.mingc = d.id and t.bianm = 'SS') ss\n" + 
		"                 where zb.diancxxb_id=dc.id\n" + 
		"                 and (zb.riq = to_date('"+strDateBQ+"','yyyy-mm-dd') or zb.riq =add_months( to_date('"+strDateBQ+"','yyyy-mm-dd'),-12))\n" + 
		"                 and ss.id(+)=dc.id\n" + 
		"                 and dc.fuid="+diancxxb_id+") dd\n" + 
		" group by rollup(dd.ss,dd.mingc)  ) rl\n" + 
		"  where rc.mingc=rl.mingc(+) \n" + 
		"  and rc.mingc<>'������'\n" + 
		"  order by dcfz desc,ssfz desc,round((rc.�ۼ�-rl.�ۼ�)*1000/4.1816,0),dcxh \n";

		rsl = con.getResultSetList(sql);
		Chart ct = new Chart(rsl,  "��λ", "��λ", "��ֵ��", false, null, ipage,"1-"+ yuef+"���볧��¯��ֵ��",300,600);
		ct.xTiltShow=true;
		ct.showLegend=false;
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
		html += getWenzHtml(wenz,data[0].length);
		
		return html;
	}
//	�󡢳���ֵ�����
	public static String getKuangcrzc_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";
		Report rt = new Report();
		String sql = 
			"select d.mingc||'-'||g.mingc,k.yiy,k.ery,k.sany,k.siy,\n" +
			"k.wuy,k.liuy,k.qiy,k.bay,k.jiuy,k.shiy,k.shiyy,k.shiey,\n" + 
			"k.leij,'��'||k.zhib zhib\n" + 
			"from kuangcrzcb k,diancxxb d, gongysb g\n" + 
			"where k.diancxxb_id = d.id and k.gongysb_id = g.id\n" + 
			"and d.fuid = "+ diancxxb_id + " and to_char(k.riq,'yyyy')='" + 
			nianf + "'" + " order by g.xuh,g.mingc,d.xuh,d.mingc";
		ResultSetList rsl = con.getResultSetList(sql);
		
		String[][] ArrHeader = new String[][]{{"��λ","1��","2��","3��","4��","5��","6��",
				"7��","8��","9��","10��","11��","12��","�ۼ�","��ָ��"}};
		int[] colwidth = {100,40,40,40,40,40,40,40,40,40,40,40,40,40,60};
		rt.setTitle("�󡢳���ֵ�����", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:ǧ����ǧ��", 2);
		rt.setBody(new Table(rsl,1,0,1));
		rt.body.setFixedRows(1);
		rt.body.setFixedCols(2);
		
		rt.body.setHeaderData(ArrHeader);
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		
		html = rt.getAllPagesHtml();
//		���ֹ���
		String wenz = "";
		String weiw = "";
		String yiw = "";
		sql = 
			"select d.mingc||'-'||g.mingc mc,k.leij - k.zhib as wanc,\n" + 
			"k.leij,k.zhib zhib\n" + 
			"from kuangcrzcb k,diancxxb d, gongysb g\n" + 
			"where k.diancxxb_id = d.id and k.gongysb_id = g.id\n" + 
			"and d.fuid = "+ diancxxb_id + " and to_char(k.riq,'yyyy')='" + 
			nianf + "'" + " order by g.xuh,g.mingc,d.xuh,d.mingc";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			if(rsl.getInt("wanc")>0){
				weiw += "," + rsl.getString("mc");
			}else{
				yiw += "," + rsl.getString("mc");
			}
		}
		if(rsl.getRows()>0){
			if(rsl.getRows()+1 == yiw.split(",").length){
				wenz += "1-" + yuef + "�ۼƿ󡢳���ֵ��ȫ��������ָ�ꡣ";
			}else if(rsl.getRows()+1 == weiw.split(",").length){
				wenz += "1-" + yuef + "�ۼƿ󡢳���ֵ��ȫ��δ������ָ�ꡣ";
			}else{
				wenz += "1-" + yuef + "�ۼƿ󡢳���ֵ��������ָ�����:" + ("".equals(yiw)?"":yiw.substring(1))
				+";δ������ָ�����:" + ("".equals(weiw)?"":weiw.substring(1)) + "��";
			}
			html += getWenzHtml(wenz,colwidth.length);
		}
		rsl.close();
		return html;
	}
//	�������
	public static String getYunsqk_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		Report rt = new Report();
		
		String strDateBQ=nianf + "-" + yuef + "-01";
		String strOraDateBQ = DateUtil.FormatOracleDate(strDateBQ);
		String strDateNC=nianf + "-01-01";
		String strOraDateNc = DateUtil.FormatOracleDate(strDateNC);
		
		String sql=

			"select xt.mingc||decode(xt.yunsfs,0,'',1,'(����)',2,'(����)') as ��λ,xt.biaoz as ����,xt.jingz as ����,xt.biaoz-xt.jingz as ����,\n" +
			"     round( decode(xt.biaoz,0,0, (xt.biaoz-xt.jingz)/xt.biaoz*100),2) as ������,yt.zhib as Ԥ��ָ��\n" + 
			"from    (select dc.mingc,max(dc.xuh) as xuh,decode(dc.id,113,yunsfsb_id,144,yunsfsb_id,0) as yunsfs,\n" + 
			"     sum(round_new(sj.biaoz,0)) as biaoz,sum(round_new(sj.jingz,0)) as jingz,\n" + 
			"     sum(round_new(sj.yuns,0)) as yuns,sum(round_new(sj.yingd,0)) as yingd\n" + 
			"     from  (select fh.diancxxb_id,fh.gongysb_id,fh.yunsfsb_id,\n" + 
			"           round_new(sum(fh.biaoz),0) as biaoz,round_new(sum(fh.jingz),0) as jingz,\n" + 
			"                  round_new(sum(fh.yuns),0) as yuns,round_new(sum(fh.yingd),0) as yingd,\n" + 
			"                  round_new(sum(fh.yingd-fh.yingk),0) as kuid\n" + 
			"           from fahb fh,vwdianc dc\n" + 
			"           where fh.daohrq>="+strOraDateNc+"\n" + 
			"             and fh.daohrq<="+strOraDateBQ+"\n" + 
			"             and dc.fuid=158\n" + 
			"             and fh.diancxxb_id=dc.id\n" + 
			"             group by fh.diancxxb_id,fh.gongysb_id,fh.lieid,fh.yunsfsb_id ) sj,vwdianc dc\n" + 
			"     where  sj.diancxxb_id=dc.id\n" + 
			"     and dc.fuid="+diancxxb_id+"\n" + 
			"     group by (dc.id,dc.mingc,decode(dc.id,113,yunsfsb_id,144,yunsfsb_id,0))\n" + 
			"     order by  dc.mingc) xt,\n" + 
			"     (select d.mingc, decode(d.id,113,yunsfsb_id,144,yunsfsb_id,0) yunsfs, y.zhib\n" + 
			"     from yunslzbb y, diancxxb d where y.diancxxb_id = d.id\n" + 
			"     and y.riq = "+strOraDateNc+" and d.fuid="+diancxxb_id+ ") yt\n" + 
			"     where xt.mingc = yt.mingc(+) and xt.yunsfs = yt.yunsfs(+)\n" + 
			" order by xt.xuh,xt.yunsfs";

		ResultSetList rsl = con.getResultSetList(sql);
		
		//�����������溬��table��ͷ
		String[][] data = new String[6][rsl.getRows() +1];
		
		//�п�����
		int[] colwidth = new int[rsl.getRows() + 1];
		colwidth[0] = 75;
		for(int i =1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		
		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[0][j]=rsl.getString("��λ");
			data[1][j]=rsl.getString("����");
			data[2][j]=rsl.getString("����");
			data[3][j]=rsl.getString("����");
			data[4][j]=rsl.getString("������");
			data[5][j]="��"+rsl.getString("Ԥ��ָ��") +"%";
		}
		
		data[0][0]="����";
		data[1][0]="����";
		data[2][0]="�볧����";
		data[3][0]="�������";
		data[4][0]="������%";
		data[5][0]="���Ŀ��";
		
		rt.setTitle("1-"+yuef+"������������", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:��", 2);
		
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		rt.body.setFixedRows(1);
		rt.body.setFixedCols(2);
		
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(1,Table.ALIGN_LEFT);
		rt.body.setColAlign(2,Table.ALIGN_CENTER );
		rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.mergeFixedCols();
		
//		rt.body.mergeCell(1, 1,1, 2);
//		rt.body.mergeCell(9, 1,9, 2);
//		rt.body.mergeCell(8, 1,8, 2);
		
		//�������Ϣ����html String
		html = rt.getAllPagesHtml();
		
		//�������ַ���
		String wenz = "";
		String weiw = "";
		String yiw = "";
		for(int i =1; i< data[0].length; i++){
			double yunsl = parseDouble(data[4][i]);
			double zb = parseDouble(data[5][i].replaceAll("��", "").replaceAll("%", ""));
			if(yunsl - zb <=0 || zb==0){
				yiw += "," + data[0][i];
			}else{
				weiw += "," + data[0][i];
			}
		}
		if(data[0].length>0){
			if(data[0].length+1 == yiw.split(",").length){
				wenz += "1-" + yuef + "�ۼ�������ȫ�����������Ŀ��ֵ�ڡ�";
			}else if(data[0].length+1 == weiw.split(",").length){
				wenz += "1-" + yuef + "�ۼ�������ȫ������Ԥ��Ŀ�ꡣ";
			}else{
				wenz += "1-" + yuef + "�ۼ�" + ("".equals(yiw)?"":yiw.substring(1))
				+"�����ʿ��������Ŀ��ֵ��;" + ("".equals(weiw)?"":weiw.substring(1)) + "����Ԥ��Ŀ�ꡣ";
			}
			html += getWenzHtml(wenz,colwidth.length);
		}
		
		return html;
	}
//	ˮ�ֲ����
	public static String getRucbmdjwcqdylj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		���ص�htmlcode
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		int row_title = 0;				//		������
		int row_by = 1;					//		����������
		int row_sy = 2;					//		���������� 
		int row_hb = 3;					//		����������
		int row_lj = 4;					//		�ۼ�������
		int row_tq = 5;					//		�ۼ�ͬ��������
		int row_tb = 6;					//		ͬ�Ȳ�ֵ
		int row_tbl = 7;				//		ͬ��������

		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		���ڲ���˰��ú����
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.riq = "+oraDate+"\n" + 
			"group by t.diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[8][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* ��ú���۸�ֵ */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		���Ȳ���˰��ú��
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"and t.riq = add_months("+oraDate+",-1)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ���ȱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		�ۼƲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq = "+oraDate+"\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* �ۼƱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		ͬ�ڲ���˰��ú����
		sql = "select d.mingc,y.bmdj from\n" +
		"(select t.diancxxb_id, round_new(decode(sum(s.jingz),0,0,\n" + 
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+SysConstant.Fenx_Leij+"'\n" + 
		"and t.riq = add_months("+oraDate+",-12)\n" + 
		"group by t.diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* ͬ�ڱ�ú�۸�ֵ */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		�����д���
		/*  �����д���  */
		data[row_title][col_title] = "��λ";
		data[row_by][col_title] = yuef + "��";
		data[row_sy][col_title] = "����";
		data[row_hb][col_title] = "���Ȳ�ֵ";
		data[row_lj][col_title] = yuef + "���ۼ�";
		data[row_tq][col_title] = "ͬ��";
		data[row_tb][col_title] = "ͬ�Ȳ�ֵ";
		data[row_tbl][col_title] = "��(��)%";
//		�е���ʴ���
		data[row_title][col_fgs] = "�е����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+
			SysConstant.Fenx_Beny+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �ۼ�����  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+
			SysConstant.Fenx_Leij+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '"+
		SysConstant.Fenx_Leij+"'\n" + "and t.riq = add_months("+oraDate+",-12)\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_tq][col_fgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		�й�����
		data[row_title][col_ssgs] = "�й�����";
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '" +
			SysConstant.Fenx_Beny+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_by][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ��������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
		"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
		"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
		"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
		"and i.bianm = 'SS' and i.mingc = d.id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and b.fenx = '" +
		SysConstant.Fenx_Beny+"'\n" + "and t.riq =add_months("+oraDate+",-1)\n" + 
		"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_sy][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  �ۼ�����  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '" +
			SysConstant.Fenx_Leij+"'\n" + "and t.riq ="+oraDate+"\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
		/*  ͬ������  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
			"sum(buhsbmdj* jingz)/sum(s.jingz)),2) bmdj\n" + 
			"from yueslb s, yuercbmdj b, yuetjkjb t, diancxxb d, itemsort o, item i\n" + 
			"where b.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
			"and o.id = i.itemsortid and o.bianm = 'DCSFSS'\n" + 
			"and i.bianm = 'SS' and i.mingc = d.id\n" + 
			"and s.fenx = '"+SysConstant.Fenx_Leij+"' and b.fenx = '" +
			SysConstant.Fenx_Leij+"'\n" + "and t.riq =add_months("+oraDate+",-12)\n" + 
			"and t.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[row_lj][col_ssgs] = rsl.getString("bmdj");
		}
		rsl.close();
//		���㻷�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����ͬ�Ȳ�
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		����ͬ��������
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		String stryf = yuef + "��";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "���ۼ�";
		}
		rt.setTitle(">�볧��ú����������("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:Ԫ/��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=8; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(5, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(6, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(7, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(8, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		����ͼ��
		/*  ����ͼ����������  */
		String[] colname = new String[]{"��λ","����","���Ȳ�"};
		rsl = new ResultSetList();
		rsl.setColumnNames(colname);
		List rslist = new ArrayList(); 
		for(int i = 1; i <= 3 ; i++){
			rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
		}
		for(int i=4 ; i<data[0].length;i++){
			double by = parseDouble(data[row_hb][i]);
			for(int j=2; j < rslist.size(); j++){
				String[] s = (String[])rslist.get(j);
				double bj = parseDouble(s[2]);
				if(by<bj){
					rslist.add(j,new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
					break;
				}else
					if(j+1==rslist.size()){
						rslist.add(new String[]{data[row_title][i],data[row_by][i],data[row_hb][i]});
						break;
					}
			}
		}
		rsl.setResultSetlist(rslist);
		/*  ��ʼ��ͼ��  */
		Chart ct = new Chart(rsl, "��λ", "��λ", "���Ȳ�", false, null, ipage, 
				"�볧��ú����",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		�������ַ���
		String wenz = "����" + yuef + "�¹�˾�볧��ú��" + data[row_by][col_fgs] + 
		"Ԫ/��";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "����������" + data[row_hb][col_fgs] + "Ԫ/�֡�";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "��";
			}else{
				wenz += "�����½�" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"Ԫ/�֡�";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "��" + data[row_title][i]; 
					sgv  += "��" + v;	
				}else{
					jdmc += "��" + data[row_title][i]; 
					jdv  += "��" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "�����ϴ󣬷ֱ�����" + sgv.substring(1) +"Ԫ/�֡�";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "�����ϴ󣬷ֱ𽵵�" +jdv.substring(1) +"Ԫ/�֡�";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
	public static String getShuifctz_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		Report rt = new Report();
		int row_title = 0;				//		�����к�
		int row_leij = 2;				//		�ۼ�
		int row_beny = 1;				//		�ۼƱ仯
		int col_title = 0;				//		������
		
		String strDateBQ=nianf + "-" + yuef + "-01";
		String sql = "select mingc from diancxxb where fuid = " + diancxxb_id + " order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
		
		//�����������溬��table��ͷ
		String[][] data = new String[3][rsl.getRows() + 1];
		
		//�п�����
		int[] colwidth = new int[rsl.getRows() + 1];
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}

		sql="select dc.mingc as ��λ,����,�ۼ�\n" +
		" from  vwdianc dc ,\n" + 
		"     (select dc.id,sum(decode(sj.fenx,'����',shuifctz)) as ����,sum(decode(sj.fenx,'�ۼ�',shuifctz)) as �ۼ�\n" + 
		"             from yueshchjb sj,vwdianc dc\n" + 
		"             where riq=to_date('"+strDateBQ+"','yyyy-mm-dd')\n" + 
		"             and sj.diancxxb_id(+)=dc.id\n" + 
		"             and dc.fuid=" + diancxxb_id + " \n" + 
		"             group by dc.id) sj--����\n" + 
		" where dc.fuid=" + diancxxb_id + "\n" + 
		" and dc.id=sj.id(+)\n" + 
		" order by dc.xuh";

		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[row_title][j]=rsl.getString("��λ");
			data[row_beny][j]=rsl.getString("����");
			data[row_leij][j]=rsl.getString("�ۼ�");
		}
		data[row_beny][col_title]=yuef+"��";
		data[row_leij][col_title]="1-"+yuef+"��";
		
		rt.setTitle("ˮ�ֲ�������", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:��", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(1,Table.ALIGN_RIGHT);
		rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		//�������Ϣ����html String
		html = rt.getAllPagesHtml();
		
		//�������ַ���
		String wenz = "����λӦ���Ӳ����йع涨�淶����ˮ�ֲ������ͳ�ƹ���";
		html += getWenzHtml(wenz,data[0].length);
		
		//ͼ��
		sql="select bt.mingc as ��λ,bt.fenx as ����,ˮ�ֲ�\n" +
		" from (select dc.id,dc.mingc,fx.fenx,dc.xuh\n" + 
		"             from  vwdianc dc ,vwfenxyue fx\n" + 
		"             where dc.fuid="+diancxxb_id+") bt,--��ͷ\n" + 
		"     (select dc.id,fenx,shuifctz as ˮ�ֲ�\n" + 
		"             from yueshchjb sj,vwdianc dc\n" + 
		"             where riq=to_date('"+strDateBQ+"','yyyy-mm-dd')\n" + 
		"             and sj.diancxxb_id(+)=dc.id\n" + 
		"             and dc.fuid="+diancxxb_id+"\n" + 
		"             ) sj--����\n" + 
		" where bt.id=sj.id(+) and bt.fenx=sj.fenx(+)\n" + 
		" order by bt.xuh,bt.fenx";
		
		rsl = con.getResultSetList(sql);
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//ͼƬ������ɫ
		CategoryDataset dataset = cd.getRsDataChart(rsl, "��λ", "����", "ˮ�ֲ�");
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.showLegend = true;
		ct.xTiltShow = true;//��б��ʾX�������
		ct.chartBackgroundPaint=gp;
		String charImg=ct.ChartBar3D(ipage,dataset, "", 600, 250);
		
		//Chart ct = new Chart(rsl,  "����", "��λ", "ˮ�ֲ�", false, null, ipage, yuef+"ˮ�ֲ�������",300,600);
		/*  ���õ�����״ͼд��html  */
		html += getChartHtml(charImg,data[0].length);
		
		return html;
	}
//	�۵������
	public static String getShouudlqk_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		Report rt = new Report();
		int row_title = 0;				//		�����к�
		int row_leij = 4;				//		�ۼ�
		int row_leijbh = 6;				//		�ۼƱ仯
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		
		String sql = "select mingc from diancxxb where fuid = " + diancxxb_id + " order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
		
		//�����������溬��table��ͷ
		String[][] data = new String[7][rsl.getRows() + 3];
		
		//�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		
		String strDateBQ=nianf + "-" + yuef + "-01";
		sql="select mingc,����,ͬ��,decode(ͬ��,0,0,round((����-ͬ��)/����*100,2)) as ����,\n" +
		"       �ۼ�,�ۼ�ͬ��,decode(�ۼ�ͬ��,0,0,round((�ۼ�-�ۼ�ͬ��)/�ۼ�ͬ��*100,2)) as �ۼ�����\n" + 
		"from (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"      round(sum(decode(fenx,'����',decode(btq,'����',shoudl)))/10000,2) as ����,\n" + 
		"      round(sum(decode(fenx,'����',decode(btq,'ͬ��',shoudl)))/10000,2) as ͬ��,\n" + 
		"      round(sum(decode(fenx,'�ۼ�',decode(btq,'����',shoudl)))/10000,2) as �ۼ�,\n" + 
		"      round(sum(decode(fenx,'�ۼ�',decode(btq,'ͬ��',shoudl)))/10000,2) as �ۼ�ͬ��\n" + 
		"from (select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id ,fenx, shoudl,decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'����','ͬ��') as btq,decode(nvl(ss.id,0),0,'������','�й�����') as ss\n" + 
		"       from yuezbb zb,vwdianc dc,(select d.id\n" + 
		"                   from diancxxb d, itemsort s, item t\n" + 
		"                   where s.bianm = 'DCSFSS'\n" + 
		"                   and s.id = t.itemsortid\n" + 
		"                   and t.mingc = d.id and t.bianm = 'SS') ss\n" + 
		"       where (zb.riq=to_date('"+strDateBQ+"','yyyy-mm-dd')\n" + 
		"             or zb.riq=add_months(to_date('"+strDateBQ+"','yyyy-mm-dd'),-12))\n" + 
		"             and zb.diancxxb_id=dc.id(+)\n" + 
		"             and dc.id=ss.id(+)\n" + 
		"             and dc.fuid="+diancxxb_id+") dd\n" + 
		"      group by rollup(dd.ss,dd.mingc)\n" + 
		" order by grouping(dd.ss) desc,ss desc,grouping(dd.mingc) desc,max(dd.xuh),max(dd.mingc))\n" + 
		" where mingc<>'������'";
		
		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[0][j]=rsl.getString("mingc");
			data[1][j]=rsl.getString("����");
			data[2][j]=rsl.getString("ͬ��");
			data[3][j]=rsl.getString("����");
			data[4][j]=rsl.getString("�ۼ�");
			data[5][j]=rsl.getString("�ۼ�ͬ��");
			data[6][j]=rsl.getString("�ۼ�����");
		}
		
		j=0;
		data[0][j]=yuef+"��λ";
		data[1][j]=yuef+"��";
		data[2][j]="ͬ��";
		data[3][j]="����";
		data[4][j]="�ۼ�";
		data[5][j]="ͬ��";
		data[6][j]="����";
		
		rt.setTitle("1��"+yuef+"�·��۵������", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:��ǧ��ʱ", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		rt.body.setColAlign(1,Table.ALIGN_RIGHT);
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setColFormat(i,"0.00");
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		
		//�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
		
		//�������ַ���
		String strSs="";
		int iSsCount=0;
		int iXjCount=0;
		String strXj="";
		String wenz = "";
		
		wenz ="��"+ yuef + "�µף���˾�ۼ�����۵���" + data[row_leij][col_fgs] + "��ǧ��ʱ��ͬ��";
		if(parseDouble(data[row_leij][col_fgs])>0){
			wenz += "����" + Math.abs(parseDouble(data[row_leijbh][col_fgs])) + "%��";
			for(int i = 3; i < colwidth.length ; i++){
				if(parseDouble(data[row_leijbh][i])<0){
					strSs +=data[0][i]+ "�½�" + Math.abs(parseDouble(data[row_leijbh][i])) + "%,";
					iSsCount=iSsCount+1;
				}
			}
			if  (strSs.length()>0){
				if (iSsCount==colwidth.length-2){
					wenz=wenz+"���е�λͬ��ȫ���½���";
				}else{
					wenz=wenz+"����:"+strSs +"����ȫ��������";
				}
			}
			
		}else if(parseDouble(data[row_leij][col_fgs])<0){
			wenz += "�½�" + Math.abs(parseDouble(data[row_leijbh][col_fgs])) + "%��";
			for(int i = 1; i < colwidth.length ; i++){
				if(parseDouble(data[row_leijbh][i])>0){
					strXj +=data[0][i]+ "����" + Math.abs(parseDouble(data[row_leijbh][i])) + "%,";
					iXjCount=iXjCount+1;
				}
			}
			if  (strSs.length()>0){
				if (iXjCount==colwidth.length-2){
					wenz=wenz+"���е�λͬ��ȫ��������";
				}else{
					wenz=wenz+"����:"+strXj +"����ȫ���½�����";
				}
			}
		}else{
			wenz += "�ޱ仯��";
		}
		
		html += getWenzHtml(wenz,data[0].length);
		
		//ͼ��
		sql="select dc.mingc as ��λ, round(shoudl/10000,2) as �۵���\n" +
		"       from yuezbb zb,vwdianc dc\n" + 
		"       where (zb.riq=to_date('"+strDateBQ+"','yyyy-mm-dd') )\n" + 
		"             and zb.diancxxb_id=dc.id(+)\n" + 
		"             and zb.fenx='����'\n" + 
		"             and dc.fuid="+diancxxb_id+"\n" + 
		"  order by dc.xuh";

		rsl = con.getResultSetList(sql);
		Chart ct = new Chart(rsl,  "��λ", "��λ", "�۵���", false, null, ipage, yuef+"�·��۵������",300,600);
		ct.xTiltShow=true;
		ct.showLegend=false;
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
		
		return html;
	}
//	ú̿�պĴ棨��澳������
	public static String getMeitshcqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef){
		String html = "";				//		���ص�htmlcode
		int row_title = 0;				//		�����к�
		int row_jjml = 1;				//		����ú���к�
		int row_kuc = 2;				//		����к�
		int col_title = 0;				//		������
		int col_fgs = 1;				//		�ֹ�˾��
		int col_ssgs = 2;				//		���й�˾��
		String strDate = nianf + "-" + yuef + "-01";	//		���·�
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle����
//		��ѯ�糧���ƣ�����ú��SQL
		String sql = "select mingc,round(jingjcml/10000,2) jjml from diancxxb where fuid = " + diancxxb_id +
		" order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
//		�����������溬��table��ͷ
		String[][] data = new String[3][rsl.getRows() + 3];
//		�п�����
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  �����п�  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/*  ���õ糧���ĵ糧���Ƽ�����ú��  */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_jjml][rsl.getRow() + 3] = rsl.getString("jjml");
		}
		rsl.close();
//		��ѯ��������պĴ�ϼƱ���ȡ���µ�kuc��SQL
		sql = 
			"select d.id,round(y.kuc/10000,2) kuc from yueshchjb y,diancxxb d\n" +
			"where y.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"and y.riq = "+oraDate+"\n" + 
			"and y.fenx = '����'\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/*  ���ÿ��  */
		while(rsl.next()){
			data[row_kuc][rsl.getRow() + 3] = rsl.getString("kuc");
		}
		rsl.close();
		/*  ���ر�����(��һ��)   */
		data[row_title][col_title] = "��λ";
		data[row_jjml][col_title] = "����ú��";
		data[row_kuc][col_title] = "��ĩ���";
//		���طֹ�˾����
		/*  ���ñ���  */
		data[row_title][col_fgs] = "�е����";
		/*  ���㾯��ú��  */
		sql = "select sum(jingjcml/10000) jjml from diancxxb where fuid = " + diancxxb_id;
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[row_jjml][col_fgs] = rsl.getString("jjml");
		}
		rsl.close();
		/*  ������  */
		sql = "select round(sum(y.kuc/10000),2) kuc from yueshchjb y,diancxxb d\n" +
		"where y.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
		"and y.riq = "+oraDate+"\n" + 
		"and y.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[row_kuc][col_fgs] = rsl.getString("kuc");
		}
		rsl.close();
//		�������й�˾����
		/*  ���ñ���  */
		data[row_title][col_ssgs] = "�й�����";
		/*  ���㾯��ú��  */
		sql = "select round(sum(d.jingjcml/10000),2) jjml\n" +
			"from diancxxb d, itemsort s, item t\n" + 
			"where s.bianm = 'DCSFSS' and s.id = t.itemsortid\n" + 
			"and t.mingc = d.id and t.bianm = 'SS'\n" +
			"and d.fuid = " + diancxxb_id;

		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[row_jjml][col_ssgs] = rsl.getString("jjml");
		}
		rsl.close();
		/*  ������  */
		sql = "select round(sum(y.kuc/10000),2) kuc\n" +
		"    from yueshchjb y, diancxxb d, itemsort s, item t\n" + 
		"    where s.bianm = 'DCSFSS' and s.id = t.itemsortid\n" + 
		"    and t.mingc = d.id and t.bianm = 'SS' and y.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
		"    and y.riq = "+oraDate+" and d.fuid = "+diancxxb_id+"\n" + 
		"    order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[row_kuc][col_ssgs] = rsl.getString("kuc");
		}
		rsl.close();
//		����� 
		/*  ���屨��  */
		Report rt = new Report();
		/*  ���ñ�ͷ���ơ��п������Ҳ൥λ  */
		rt.setTitle(">ú̿���Ĵ����("+yuef + "��)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("��λ:���", 2);
		/*  ���ñ������  */
		rt.setBody(new Table(data,0,0,1));
		/*  ���ñ����  */
		rt.body.setWidth(colwidth);
		/*  ���ñ������и߶�  */
		rt.body.setRowHeight(1, 40);
		/*  ���ñ�����ֶ��뷽ʽ  */
		for(int i = 1; i<=3; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
		}
//		�������Ϣ����htmlString
		html = rt.getAllPagesHtml();
//		��������˵��
//		����˵������
		String wenz = "";
		/*  �������Ĵ�  */
		sql = "select round(sum(shouml/10000),2) sm,\n" +
			"    round(sum((y.fady + y.gongry + y.sunh + y.qith + y.diaocl - y.shuifctz)/10000),2) hm,\n" + 
			"    round(sum(y.kuc/10000),2) kc\n" + 
			"    from yueshchjb y, diancxxb d\n" + 
			"    where y.diancxxb_id = d.id and y.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"    and y.riq = "+oraDate+"\n" + " and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			wenz = "����" + yuef + "�¹�˾����ԭú " + rsl.getString("sm") + "��֣���ú " + 
			rsl.getString("hm") + "��֣���ĩ��� " + rsl.getString("kc") + "��֣�";
		}
		rsl.close();
		/*  �ۼ�����ͬ��  */
		sql = "select round(sum(c.shouml/10000),2) sm,\n" +
			"   round(sum((c.fady + c.gongry + c.sunh + c.qith + c.diaocl - c.shuifctz)/10000),2) hm,\n" + 
			"   round(decode(sum(l.shouml),0,0,sum((c.shouml-l.shouml)/l.shouml)),2) smtb,\n" + 
			"   round(decode(sum(l.fady + l.gongry + l.sunh + l.qith + l.diaocl - l.shuifctz),0,0,\n" + 
			"   sum(((c.fady + c.gongry + c.sunh + c.qith + c.diaocl - c.shuifctz) -\n" + 
			"   (l.fady + l.gongry + l.sunh + l.qith + l.diaocl - l.shuifctz))/\n" + 
			"   (l.fady + l.gongry + l.sunh + l.qith + l.diaocl - l.shuifctz))),2) hmtb\n" + 
			"   from diancxxb d,\n" + 
			"   (select * from yueshchjb where riq = "+oraDate+" and fenx = '"+SysConstant.Fenx_Leij+"') c,\n" + 
			"   (select * from yueshchjb where riq = add_months("+oraDate+",-12) and fenx = '"+SysConstant.Fenx_Leij+"') l\n" + 
			"   where d.id = c.diancxxb_id and d.id = l.diancxxb_id and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			/*  �ۼ���ú  */
			wenz += "�ۼƹ�ú " + rsl.getString("sm") + "��֣�ͬ��";
			/*  �ж�������  */
			if(rsl.getDouble("smtb") > 0){
				wenz += "���� ";
			}else{
				wenz += "���� ";
			}
			wenz += rsl.getString("smtb") + "%��";
			/*  �ۼƺ�ú  */
			wenz += "�ۼƺ�ú " + rsl.getString("hm") + "��֣�ͬ��";
			/*  �ж�������  */
			if(rsl.getDouble("hmtb") > 0){
				wenz += "���� ";
			}else{
				wenz += "���� ";
			}
			wenz += rsl.getString("hmtb") + "%��";
		}
		rsl.close();
		html += getWenzHtml(wenz,data[0].length);
		return html;
	}
	
	private static String getJicmfrlqk_fgswzdb(String sj, String dw, String rz){
		String wenz = "";
		if(!dw.equals("")){
			boolean isOnly = false;
			isOnly = dw.substring(1).split("��").length == 1;
			String only = "";
			if(!isOnly){
				only = "�ֱ�";
			}
			wenz += dw.substring(1) + "��ֵ"+sj+"���ϴ󣬻���"+only+ 
			("��".equals(sj)?"����":"�½�") +
			rz.substring(1) + "ǧ��/ǧ�ˡ�";
		}
		return wenz;
	}
	
	private static String getWenzHtml(String wenz,int colspan){
		String strspan = "";
		if(colspan > 0){
			strspan = "colspan="+colspan;
		}
		String html = "";
		html += "<center><span><table><tr><td "+strspan+" align=left width=600>";
//		ȡ�����ַ���
//    	�������е������ַ��滻��html����
    	wenz = wenz.replaceAll("\r\n", "<br>");
    	wenz = wenz.replaceAll("\n", "<br>");
    	wenz = wenz.replaceAll("\r", "<br>");
    	wenz = wenz.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		html += wenz;
    	html += "</td></tr></table></span></center>";
		return html;
	}
	
	private static String getChartHtml(String strChart,int colspan){
		String strspan = "";
		if(colspan > 0){
			strspan = "colspan="+colspan;
		}
		String html = "";
		html += "<center><span><table><tr><td "+strspan+" align=left width=600>";
    	html += strChart;
    	html += "</td></tr></table></span></center>";
		return html;
	}
	
	private static int parseInt(String value){
		return Integer.parseInt(value==null||
		"".equals(value)?"0":value);
	}
	
	private static double parseDouble(String value){
		return Double.parseDouble(value==null||
		"".equals(value)?"0":value);
	}
}