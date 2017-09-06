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
 * 作者：王磊
 * 时间：2009-11-22
 * 描述：增加厂、矿热值差分析、油耗用分析、重点合同到货率分析，修改了运损率分析。
 */
/*
 * 作者：王磊
 * 时间：2009-10-26
 * 描述：增加综合标煤单价分析及综合标煤单价累计分析
 */
/*
 * 作者：王磊
 * 时间：2009-10-18
 * 描述：增加新的供耗存处理方法getMeitghcztqk_fgs 
 * 		修改入厂热值分析中的文字说明部分的程序处理
 */
/**
 * @author Rock
 * @since 2009-08-20
 * @version v1.1.2.1
 * @discription 经济分析个性化处理
 */

public class AnalyOperation {
	/*  报表格式的ID  */
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
	 * @param diancxxb_id  	电厂ID
	 * @param oraDate		oracle日期
	 * @param fenx			分项(当月、累计)
	 * @return				煤收耗存的SQL
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
//	煤炭购耗存情况
	public static String getMeitghcztqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef){
		String html = "";				//		返回的htmlcode
		int row_title = 0;				//		标题行号
		int r_g_dy = 1;					//		购煤当月行
		int r_g_hb = 2;					//		购煤环比行
		int r_g_lj = 3;					//		购煤累计行
		int r_g_tq = 4;					//		购煤同期行
		int r_g_tb = 5;					//		购煤同比行
		int r_g_tbz = 6;				//		购煤同比增长行
		
		int r_h_dy = 7;					//		耗煤当月行
		int r_h_hb = 8;					//		耗煤环比行
		int r_h_lj = 9;					//		耗煤累计行
		int r_h_tq = 10;				//		耗煤同期行
		int r_h_tb = 11;				//		耗煤同比行
		int r_h_tbz = 12;				//		耗煤同比增长行
		
		int r_c_dy = 13;				//		存煤当月行
		int r_c_hb = 14;				//		存煤环比行
//		int r_c_lj = 15;				//		存煤累计行
		int r_c_tq = 15;				//		存煤同期行
		int r_c_tb = 16;				//		存煤同比行
		int r_c_kyts = 17;				//		可用天数
		
		int c_title_lb = 0;				//		标题列类别
		int c_title = 1;				//		标题列实际
		int c_fgs = 2;				//		分公司列
		int c_ssgs = 3;				//		上市公司列
		String strDate = nianf + "-" + yuef + "-01";	//		年月份
//		Current Date
		Date cDate = DateUtil.getDate(strDate);
//		Current Date for Oracle
		String coDate = DateUtil.FormatOracleDate(cDate);	//		oracle日期
//		Last Month Date
		Date lmDate = DateUtil.AddDate(cDate, -1, DateUtil.AddType_intMonth);
//		Last Month Date for Oracle
		String lmoDate = DateUtil.FormatOracleDate(lmDate);
//		Last Year Date 
		Date lyDate = DateUtil.AddDate(cDate, -1, DateUtil.AddType_intYear);
//		Last Year Date for Oracle
		String lyoDate = DateUtil.FormatOracleDate(lyDate);
		
//		查询电厂名称，警戒煤量SQL
		String sql = "select mingc,round(jingjcml/10000,2) jjml from diancxxb where fuid = " + diancxxb_id +
		" order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
//		数据数组里面含有table表头
		String[][] data = new String[18][rsl.getRows() + 4];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 4];
		/*  设置列宽  */
		colwidth[0] = 35;
		colwidth[1] = 35;
		for(int i = 2; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/*  设置电厂级的电厂名称  */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 4] = rsl.getString("mingc");
		}
		rsl.close();
//		设置当月来耗存
		sql = getMeighcSql(diancxxb_id,coDate,SysConstant.Fenx_Beny);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_dy][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_dy][rsl.getRow() + 4] = rsl.getString("hy");
			data[r_c_dy][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();
//		设置累计来耗存
		sql = getMeighcSql(diancxxb_id,coDate,SysConstant.Fenx_Leij);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_lj][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_lj][rsl.getRow() + 4] = rsl.getString("hy");
//			data[r_c_lj][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();
//		设置环比来耗存
		sql = getMeighcSql(diancxxb_id,lmoDate,SysConstant.Fenx_Beny);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_hb][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_hb][rsl.getRow() + 4] = rsl.getString("hy");
			data[r_c_hb][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();
//		设置同期来耗存
		sql = getMeighcSql(diancxxb_id,lyoDate,SysConstant.Fenx_Leij);
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			data[r_g_tq][rsl.getRow() + 4] = rsl.getString("gm");
			data[r_h_tq][rsl.getRow() + 4] = rsl.getString("hy");
			data[r_c_tq][rsl.getRow() + 4] = rsl.getString("kc");
		}
		rsl.close();

		/*  加载标题列(第一列)   */
		for(int i = r_g_dy; i <= r_g_tbz; i++){
			data[i][c_title_lb] = "购煤";
		}
		for(int i = r_h_dy; i <= r_h_tbz; i++){
			data[i][c_title_lb] = "耗用";
		}
		for(int i = r_c_dy; i <= r_c_kyts; i++){
			data[i][c_title_lb] = "存煤";
		}
		data[r_g_dy][c_title] = yuef + "月";
		data[r_g_hb][c_title] = "环比";
		data[r_g_lj][c_title] = "累计";
		data[r_g_tq][c_title] = "同期";
		data[r_g_tb][c_title] = "同比";
		data[r_g_tbz][c_title] = "同比<br>增幅";
		data[r_h_dy][c_title] = yuef + "月";
		data[r_h_hb][c_title] = "环比";
		data[r_h_lj][c_title] = "累计";
		data[r_h_tq][c_title] = "同期";
		data[r_h_tb][c_title] = "同比";
		data[r_h_tbz][c_title] = "同比<br>增幅";
		data[r_c_dy][c_title] = yuef + "月";
		data[r_c_hb][c_title] = "环比";
		data[r_c_tq][c_title] = "同期";
		data[r_c_tb][c_title] = "同比";
		data[r_c_kyts][c_title] = "可用<br>天数";
//		加载分公司数据
		/*  设置标题  */
		data[row_title][c_fgs] = "中电国际";
		/*  取得当月供耗存  */
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
		/*  取得环比供耗存  */
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
		/*  取得累计供耗存  */
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
		/*  取得同期供耗存  */
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
//		加载上市公司数据
		/*  设置标题  */
		data[row_title][c_ssgs] = "中国电力";
		/*  取得当月供耗存  */
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
		/*  取得环比供耗存  */
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
		/*  取得累计供耗存  */
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
		
		/*  取得同期供耗存  */
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
//		环比、同比、同比增长值、库存可用天数计算。
		for(int i = 2; i < data[row_title].length ; i++){
			/*  环比的差值计算  */
			String bj = CustomMaths.sub(data[r_g_dy][i], data[r_g_hb][i]);
			data[r_g_hb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_h_dy][i], data[r_h_hb][i]);
			data[r_h_hb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_c_dy][i], data[r_c_hb][i]);
			data[r_c_hb][i] = bj==null?"":bj;
			/*  同比的差值计算  */
			bj = CustomMaths.sub(data[r_g_lj][i], data[r_g_tq][i]);
			data[r_g_tb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_h_lj][i], data[r_h_tq][i]);
			data[r_h_tb][i] = bj==null?"":bj;
			bj = CustomMaths.sub(data[r_c_dy][i], data[r_c_tq][i]);
			data[r_c_tb][i] = bj==null?"":bj;
			/*  同比增长的计算  */
			bj = CustomMaths.div(data[r_g_tb][i], data[r_g_tq][i] + "00", 2);
			data[r_g_tbz][i] = bj==null?"":bj;
			bj = CustomMaths.div(data[r_h_tb][i], data[r_h_tq][i] + "00", 2);
			data[r_h_tbz][i] = bj==null?"":bj;
			/*  库存可用天数的计算  */
			bj = CustomMaths.div(CustomMaths.mul(data[r_c_dy][i],"30")
					, data[r_h_dy][i], 0);
			data[r_c_kyts][i] = bj==null?"":bj;
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		rt.setTitle(">煤炭购耗存情况("+yuef + "月)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:万吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
		/* 合并列标题 */
		rt.body.merge(1, 1, rt.body.getRows() , 1);
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		设置文字说明
//		文字说明变量
		String wenz = "　　" + yuef + "月公司购入原煤 " + data[r_g_dy][c_fgs] + "万吨，";
		if(!"".equals(data[r_g_hb][c_fgs]))
			if(Double.parseDouble(data[r_g_hb][c_fgs])>0){
				wenz += "环比增" + data[r_g_hb][c_fgs] + "万吨；";
			}else{
				wenz += "环比减" + Math.abs(Double.parseDouble(data[r_g_hb][c_fgs]))
				+ "万吨；";
			}
		wenz += "耗煤" + data[r_h_dy][c_fgs] + "万吨，";
		if(!"".equals(data[r_h_hb][c_fgs]))
			if(Double.parseDouble(data[r_h_hb][c_fgs])>0){
				wenz += "环比增" + data[r_h_hb][c_fgs] + "万吨；";
			}else{
				wenz += "环比减" + Math.abs(Double.parseDouble(data[r_h_hb][c_fgs])) + "万吨；";
			}
		wenz += "累计购入原煤" + data[r_h_lj][c_fgs] + "万吨，";
		if(!"".equals(data[r_g_tb][c_fgs]))
			if(Double.parseDouble(data[r_g_tb][c_fgs])>0){
				wenz += "同比增" + data[r_g_tb][c_fgs] + "万吨；增幅" + data[r_g_tbz][c_fgs] + "%；";
			}else{
				wenz += "同比减" + Math.abs(Double.parseDouble(data[r_g_tb][c_fgs])) + 
				"万吨；减幅" + Math.abs(Double.parseDouble(data[r_g_tb][c_fgs])) + "%；";
			}
		wenz += "累计耗煤" + data[r_h_lj][c_fgs] + "万吨，";
		if(!"".equals(data[r_h_tb][c_fgs]))
			if(Double.parseDouble(data[r_h_tb][c_fgs])>0){
				wenz += "同比增" + data[r_h_tb][c_fgs] + "万吨；增幅" + data[r_h_tbz][c_fgs] + "%；";
			}else{
				wenz += "同比减" + Math.abs(Double.parseDouble(data[r_h_tb][c_fgs])) + 
				"万吨；减幅" + Math.abs(Double.parseDouble(data[r_h_tb][c_fgs])) + "%；";
			}
		wenz += "期末库存煤" + data[r_c_dy][c_fgs] + "万吨，";
		if(!"".equals(data[r_c_tb][c_fgs]))
			if(Double.parseDouble(data[r_c_tb][c_fgs])>0){
				wenz += "同比增" + data[r_c_tb][c_fgs] + "万吨；";
			}else{
				wenz += "同比减" + Math.abs(Double.parseDouble(data[r_c_tb][c_fgs])) + 
				"万吨；";
			}
		wenz += "可用天数" + data[r_c_kyts][c_fgs] + "天。";
		html += getWenzHtml(wenz,data[0].length);
		return html;
	}
	
//	重点合同到货率
	public static String getZhongdhtdhl_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int row_title = 0;				//		标题行
		int row_title2 = 1;				//		标题行2
		int col_title = 0;				//		标题列
		int col_ht_bn = 1;				//		合同本年
		int col_ht_bq = 2;				//		合同本期
		int col_ht_tq = 3;				//		合同同期
		int col_sh_bq = 4;				//		实收本期
		int col_sh_tq = 5;				//		实收同期
		int col_dh_bn = 6;				//		到货本年
		int col_dh_bq = 7;				//		到货本期
		int col_dh_tq = 8;				//		到货同期
		String strDate = nianf + "-" + yuef + "-01";	//		年月份
//		Current Date
		Date cDate = DateUtil.getDate(strDate);
//		Current Date for Oracle
		String coDate = DateUtil.FormatOracleDate(cDate);	//		oracle日期
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
		data[row_title][col_title] = "单位";
		data[row_title2][col_title] = "单位";
		data[row_title][col_ht_bn] = "合同量";
		data[row_title][col_ht_bq] = "合同量";
		data[row_title][col_ht_tq] = "合同量";
		data[row_title][col_sh_bq] = "实收量";
		data[row_title][col_sh_tq] = "实收量";
		data[row_title][col_dh_bn] = "到货率";
		data[row_title][col_dh_bq] = "到货率";
		data[row_title][col_dh_tq] = "到货率";
		data[row_title2][col_ht_bn] = "本年";
		data[row_title2][col_ht_bq] = "本期";
		data[row_title2][col_ht_tq] = "同期";
		data[row_title2][col_sh_bq] = "本期";
		data[row_title2][col_sh_tq] = "同期";
		data[row_title2][col_dh_bn] = "本年";
		data[row_title2][col_dh_bq] = "本期";
		data[row_title2][col_dh_tq] = "同期";
//		中电国际
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
			data[2][col_title] = "中电国际";
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
			data[3][col_title] = "中国电力";
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
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "月累计";
		}
		int colwidth[] = {100,60,60,60,60,60,60,60,60};
		rt.setTitle(">月重点订货合同到货率("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:万吨、%", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		rt.body.merge(1, 1, 2, colwidth.length);
		/*  设置表格文字对齐方式  */
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
		
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本期","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"燃油耗用情况",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
//		暂时不加入图形
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "　　" + yuef + "月各单位重点合同本期到货情况：" ;
		for(int i=4; i < data.length ; i++){
			btq = CustomMaths.sub(parseDouble(data[i][col_dh_bq]),parseDouble(data[i][col_dh_tq]));
			wenz += data[i][col_title] + "本期到货率" + data[i][col_dh_bq] + 
			"，同期" + data[i][col_dh_tq] + "同比" + (btq>0?"升幅":"降幅")  + btq + "。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
		
	}
//	燃油耗用情况
	public static String getRanyhyqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_by = 1;					//		本月数据行
		int row_sy = 2;					//		上月数据行 
		int row_hb = 3;					//		环比数据行
		int row_lj = 4;					//		累计数据行
		int row_tq = 5;					//		累计同期数据行
		int row_tb = 6;					//		同比差值
		int row_tbl = 7;				//		同比升降幅

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		本期
		String sql = 
			"select d.mingc, y.hy from\n" +
			"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
			"from yueshcyb where riq = "+oraDate+"\n" + 
			"and fenx = '"+SysConstant.Fenx_Beny+"' group by diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
			" order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		数据数组里面含有table表头
		String[][] data = new String[8][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();
//		环比
		sql = 
			"select d.mingc, y.hy from\n" +
			"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
			"from yueshcyb where riq = add_months("+oraDate+",-1)\n" + 
			"and fenx = '"+SysConstant.Fenx_Beny+"' group by diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
			" order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* 环比赋值 */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();
//		累计
		sql = 
			"select d.mingc, y.hy from\n" +
			"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
			"from yueshcyb where riq = "+oraDate+"\n" + 
			"and fenx = '"+SysConstant.Fenx_Leij+"' group by diancxxb_id) y,diancxxb d\n" + 
			"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
			" order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* 累计赋值 */
		while(rsl.next()){
			data[row_lj][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();
//		同期
		sql = "select d.mingc, y.hy from\n" +
		"(select diancxxb_id,sum(fadyy + gongry + qithy +sunh +diaocl) hy\n" + 
		"from yueshcyb where riq = add_months("+oraDate+",-12)\n" + 
		"and fenx = '"+SysConstant.Fenx_Leij+"' group by diancxxb_id) y,diancxxb d\n" + 
		"where y.diancxxb_id(+) = d.id and d.fuid = "+ diancxxb_id +
		" order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* 同期赋值 */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("hy");
		}
		rsl.close();

//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_by][col_title] = yuef + "月";
		data[row_sy][col_title] = "上月";
		data[row_hb][col_title] = "环比差值";
		data[row_lj][col_title] = yuef + "月累计";
		data[row_tq][col_title] = "同期";
		data[row_tb][col_title] = "同比差值";
		data[row_tbl][col_title] = "增(降)%";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  本月数据  */
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
		/*  上月数据  */
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
		/*  累计数据  */
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
		/*  同期数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  本月数据  */
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
		/*  上月数据  */
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
		/*  累计数据  */
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
		/*  同期数据  */
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
//		计算环比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算同比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算同比升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "月累计";
		}
		rt.setTitle(">燃油耗用情况("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"燃油耗用情况",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
//		暂时不加入图形
//		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "　　" + yuef + "月公司燃油耗用" + data[row_by][col_fgs] + 
		"吨";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "，环比上升" + data[row_hb][col_fgs] + "吨。";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "。";
			}else{
				wenz += "环比下降" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"吨。";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "、" + data[row_title][i]; 
					sgv  += "、" + v;	
				}else{
					jdmc += "、" + data[row_title][i]; 
					jdv  += "、" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "升幅较大，分别升高" + sgv.substring(1) +"吨。";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "降幅较大，分别降低" +jdv.substring(1) +"吨。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	进厂煤发热量情况
	public static String getJicmfrlqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_by = 1;					//		当月数据行
		int row_sy = 2;					//		上月数据行 
		int row_hb = 3;					//		环比数据行  
		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		当月个电厂热值Kcal/Kg
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
//		数据数组里面含有table表头
		String[][] data = new String[4][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 当月热值赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		上月热值
		sql = "select d.mingc, round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id(+) = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"group by d.mingc order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* 上月热值赋值 */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_by][col_title] = yuef + "月";
		data[row_sy][col_title] = "上月";
		data[row_hb][col_title] = "环比差值";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  本月数据  */
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
		/*  上月数据  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* 上月热值赋值 */
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  本月数据  */
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
		/*  上月数据  */
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
//		计算环比差
		for(int i = col_fgs; i < data[0].length ; i++){
			int by = parseInt(data[row_by][i]);
			int sy = parseInt(data[row_sy][i]);
			if(by ==0 || sy ==0 ){
				data[row_hb][i] = "";
			}else{
				data[row_hb][i] = String.valueOf(by - sy);
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		rt.setTitle(">进厂煤发热量情况("+yuef + "月)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:千卡/千克", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
		for(int i = 1; i<=4; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
		}
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"进厂煤发热量情况",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "";
		wenz = "　　" + yuef + "月份公司入厂煤热值" + data[row_by][col_fgs] + "千卡/千克，环比";
		if(parseInt(data[row_hb][col_fgs])>0){
			wenz += "上升" + Math.abs(parseInt(data[row_hb][col_fgs])) + "千卡/千克。";
		}else if(parseInt(data[row_hb][col_fgs])<0){
			wenz += "下降" + Math.abs(parseInt(data[row_hb][col_fgs])) + "千卡/千克。";
		}else{
			wenz += "无变化。";
		}
		String jddw = "";
		String jdrz = "";
		boolean bhfdcc = false;
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])<-100){
				jddw += "、" + data[row_title][i] ;
				jdrz += "、" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		String hb = getJicmfrlqk_fgswzdb("降", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		jddw = "";
		jdrz = "";
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])>100){
				jddw += "、" + data[row_title][i] ;
				jdrz += "、" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		hb = getJicmfrlqk_fgswzdb("升", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		if(bhfdcc){
			wenz += "其他单位煤质正常波动。";
		}else{
			wenz += "各单位煤质波动正常。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	进厂煤发热量情况(累计)
	public static String getJicmfrllj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_by = 1;					//		累计数据行
		int row_sy = 2;					//		同期数据行 
		int row_hb = 3;					//		同比数据行  
		int row_zb = 4;					//		指标
		int row_zbc = 5;				//		指标差
		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		累计电厂热值Kcal/Kg
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
//		数据数组里面含有table表头
		String[][] data = new String[6][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 累计热值赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		同期热值
		sql = "select d.mingc, round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Leij+"' and z.fenx = '"+
		SysConstant.Fenx_Leij+"'\n" + "and t.diancxxb_id(+) = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-12)\n" + 
		"group by d.mingc order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* 同期热值赋值 */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		年指标
		sql = "select rucmrz qnetar from jjfxnzbwh j, diancxxb d\n" +
			"where j.diancxxb_id(+) = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"and j.riq(+) = getyearfirstdate("+oraDate+")\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* 年指标热值赋值 */
		while(rsl.next()){
			data[row_zb][rsl.getRow() + 3] = rsl.getString("qnetar");
		}
		rsl.close();
//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_by][col_title] = yuef + "月累计";
		data[row_sy][col_title] = "同期";
		data[row_hb][col_title] = "同比差值";
		data[row_zb][col_title] = "年度指标";
		data[row_zbc][col_title] = "年指标差";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  本月数据  */
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
		/*  上月数据  */
		sql = "select round_new(decode(sum(s.jingz),0,0,\n" +
		"sum(s.jingz*z.qnet_ar)/(sum(s.jingz)*0.0041816)),0) qnetar\n" + 
		"from yueslb s,yuezlb z,yuetjkjb t,diancxxb d\n" + 
		"where t.id = s.yuetjkjb_id and t.id = z.yuetjkjb_id\n" + 
		"and s.fenx = '"+SysConstant.Fenx_Beny+"' and z.fenx = '"+
		SysConstant.Fenx_Beny+"'\n" + "and t.diancxxb_id = d.id\n" +
		" and d.fuid = "+diancxxb_id+"\n" + "and t.riq = add_months("+oraDate+",-1)\n" + 
		"order by max(d.xuh),max(d.id)";
		rsl = con.getResultSetList(sql);
		/* 上月热值赋值 */
		while(rsl.next()){
			data[row_sy][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
		/*  年指标数据  */
		sql = "select rucmrz qnetar from jjfxnzbwh j\n" +
		"where j.diancxxb_id = "+diancxxb_id+"\n" + 
		"and j.riq = getyearfirstdate("+oraDate+")\n";
		rsl = con.getResultSetList(sql);
		/* 年指标热值赋值 */
		if(rsl.next()){
			data[row_zb][col_fgs] = rsl.getString("qnetar");
		}
		rsl.close();
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  本月数据  */
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
		/*  上月数据  */
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
//		计算环比差
		for(int i = col_fgs; i < data[0].length ; i++){
			int by = parseInt(data[row_by][i]);
			int sy = parseInt(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(by - sy);
			}else{
				data[row_hb][i] = "";
			}
		}
//		计算年指标差
		for(int i = col_fgs; i < data[0].length ; i++){
			int by = parseInt(data[row_by][i]);
			int zb = parseInt(data[row_zb][i]);
			if(by != 0 && zb != 0){
				data[row_zbc][i] = String.valueOf(by - zb);
			}else{
				data[row_zbc][i] = "";
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "月累计";
		}
		rt.setTitle(">进厂煤发热量情况("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:千卡/千克", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"进厂煤发热量情况",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "";
		wenz = "　　" + stryf + "公司入厂煤热值" + data[row_by][col_fgs] + "千卡/千克，同比";
		if(parseInt(data[row_hb][col_fgs])>0){
			wenz += "上升" + Math.abs(parseInt(data[row_hb][col_fgs])) + "千卡/千克。";
		}else if(parseInt(data[row_hb][col_fgs])<0){
			wenz += "下降" + Math.abs(parseInt(data[row_hb][col_fgs])) + "千卡/千克。";
		}else{
			wenz += "无变化。";
		}
		String jddw = "";
		String jdrz = "";
		boolean bhfdcc = false;
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])<-100){
				jddw += "、" + data[row_title][i] ;
				jdrz += "、" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		String hb = getJicmfrlqk_fgswzdb("降", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		jddw = "";
		jdrz = "";
		for(int i = 3; i < data[row_hb].length ; i++){
			if(parseInt(data[row_hb][i])>100){
				jddw += "、" + data[row_title][i] ;
				jdrz += "、" + Math.abs(parseInt(data[row_hb][i])) ;
			}
		}
		hb = getJicmfrlqk_fgswzdb("升", jddw, jdrz);
		if(!"".equals(hb)){
			bhfdcc = true;
			wenz += hb;
		}
		if(bhfdcc){
			wenz += "其他单位煤质正常波动。";
		}else{
			wenz += "各单位煤质波动正常。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		wenz = "";
		jddw = "";
		for(int i = 3; i < data[row_zbc].length ; i++){
			if(parseInt(data[row_zbc][i])>0){
				jddw += "、" + data[row_title][i] ;
			}
		}
		if(!"".equals(jddw)){
			wenz += "　　" + jddw.substring(1) + "累计入厂热值完成年度指标；";
		}
		jddw = "";
		jdrz = "";
		for(int i = 3; i < data[row_zbc].length ; i++){
			if(parseInt(data[row_zbc][i])<0){
				jddw += "、" + data[row_title][i] ;
				jdrz += "、" + Math.abs(parseInt(data[row_zbc][i])) ;
			}
		}
		if(!"".equals(jddw)){
			wenz += jddw.substring(1) + "分别比年度指标低" + jdrz.substring(1) + "千卡/千克。";
		}
		if(!wenz.equals(""))
			html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	入厂标煤单价完成情况
	public static String getRucbmdjwcqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_by = 1;					//		本月数据行
		int row_sy = 2;					//		上月数据行 
		int row_hb = 3;					//		环比数据行

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		本期不含税标煤单价
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
//		数据数组里面含有table表头
		String[][] data = new String[4][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 标煤单价赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		环比不含税标煤价
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
		/* 环比标煤价赋值 */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_by][col_title] = yuef + "月";
		data[row_sy][col_title] = "上月";
		data[row_hb][col_title] = "环比差值";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  本月数据  */
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
		/*  上月数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  本月数据  */
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
		/*  上月数据  */
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
//		计算环比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
//		if(!"1".equals(yuef)){
//			stryf = "1-" + yuef + "月累计";
//		}
		rt.setTitle(">入厂标煤单价完成情况("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
		for(int i = 1; i<=4; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
		}
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"入厂标煤单价",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "　　" + yuef + "月公司入厂标煤价" + data[row_by][col_fgs] + 
		"元/吨";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "，环比上升" + data[row_hb][col_fgs] + "元/吨。";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "。";
			}else{
				wenz += "环比下降" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"元/吨。";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "、" + data[row_title][i]; 
					sgv  += "、" + v;	
				}else{
					jdmc += "、" + data[row_title][i]; 
					jdv  += "、" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "升幅较大，分别升高" + sgv.substring(1) +"元/吨。";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "降幅较大，分别降低" +jdv.substring(1) +"元/吨。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	入厂标煤单价完成情况(累计)
	public static String getRucbmdjwcqklj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
//		int row_by = 1;					//		本月数据行
//		int row_sy = 2;					//		上月数据行 
//		int row_hb = 3;					//		环比数据行
		int row_lj = 1;					//		累计数据行
		int row_tq = 2;					//		累计同期数据行
		int row_tb = 3;					//		同比差值
		int row_tbl = 4;				//		同比升降幅

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		本期不含税标煤单价
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
//		数据数组里面含有table表头
		String[][] data = new String[5][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
//		累计不含税标煤单价
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
		/* 累计标煤价赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		同期不含税标煤单价
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
		/* 同期标煤价赋值 */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_lj][col_title] = yuef + "月累计";
		data[row_tq][col_title] = "同期";
		data[row_tb][col_title] = "同比差值";
		data[row_tbl][col_title] = "增(降)%";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  累计数据  */
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
		/*  同期数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  累计数据  */
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
		/*  同期数据  */
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
//		计算同比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算同比升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "月累计";
		}
		rt.setTitle(">入厂标煤单价完成情况("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"入厂标煤单价",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "　　" + yuef + "月公司入厂标煤价" + data[row_lj][col_fgs] + 
		"元/吨";
		
		if(parseDouble(data[row_tb][col_fgs])>0){
			wenz += "，同比上升" + data[row_tb][col_fgs] + "元/吨。";
		}else
			if(parseDouble(data[row_tb][col_fgs])==0){
				wenz += "。";
			}else{
				wenz += "同比下降" + Math.abs(parseDouble(data[row_tb][col_fgs])) +
				"元/吨。";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_tb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "、" + data[row_title][i]; 
					sgv  += "、" + v;	
				}else{
					jdmc += "、" + data[row_title][i]; 
					jdv  += "、" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "升幅较大，分别升高" + sgv.substring(1) +"元/吨。";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "降幅较大，分别降低" +jdv.substring(1) +"元/吨。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	入厂标煤价及与集团同区域比较
	public static String getRucbmdjwcdb_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_lj = 1;					//		累计数据行
		int row_h = 2;					//		上月数据
		int row_hb = 3;					//		环比数据
		int row_tq = 4;					//		同期数据行 
		int row_tb = 5;					//		同比数据行
		int row_tbl = 6;				//		同比增降幅
		int row_dbtitle = 7;			//		对比标题
		int row_dblj = 8;				//		对比累计
		int row_dbh = 9;				//		对比上月
		int row_dbhb = 10;				//		对比环比
		int row_dbtq = 11;				//		对比同期
		int row_dbtb = 12;				//		对比同比
		int row_dbtbl = 13;				//		对比同比增降幅

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		当月不含税标煤单价
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
//		数据数组里面含有table表头
		String[][] data = new String[14][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 标煤单价赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		环比不含税标煤价
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
		/* 同期标煤价赋值 */
		while(rsl.next()){
			data[row_h][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		同期不含税标煤价
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
		/* 同期标煤价赋值 */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		对比公司当月
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
		/* 对比标煤价赋值 */
		while(rsl.next()){
			data[row_dbtitle][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_dblj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		对比公司上月 
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
		/* 对比标煤价赋值 */
		while(rsl.next()){
			data[row_dbh][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		对比公司同期 
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
		/* 对比标煤价赋值 */
		while(rsl.next()){
			data[row_dbtq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_lj][col_title] = yuef + "月";
		data[row_h][col_title] = "上月";
		data[row_hb][col_title] = "环比";
		data[row_tq][col_title] = "同期";
		data[row_tb][col_title] = "同比差值";
		data[row_tbl][col_title] = "增(降)%";
		data[row_dbtitle][col_title] = "";
		data[row_dblj][col_title] = yuef + "月";
		data[row_dbh][col_title] = "上月";
		data[row_dbhb][col_title] = "环比";
		data[row_dbtq][col_title] = "同期";
		data[row_dbtb][col_title] = "同比差值";
		data[row_dbtbl][col_title] = "增(降)%";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		data[row_dbtitle][col_fgs] = "集团";
		/*  当月数据  */
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
		/*  上月数据  */
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
		/*  同期数据  */
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
		/*  对比累计数据  */
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
		/*  对比环比数据  */
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
		/*  对比同期数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		data[row_dbtitle][col_ssgs] = "对应区域";
		/*  累计数据  */
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
		/*  上月数据  */
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
		/*  同期数据  */
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
		/*  对比当月数据  */
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
		/*  对比上月数据  */
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
		/*  对比同期数据  */
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
//		计算环比期差值
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_h][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算对比环比期差值
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_dblj][i]);
			double sy = parseDouble(data[row_dbh][i]);
			if(by != 0 && sy != 0){
				data[row_dbhb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算本同期差值
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算对比本同期差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dblj][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算本同期升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		计算对比本同期升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dbtb][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
//		if(!"1".equals(yuef)){
//			stryf = "1-" + yuef + "月累计";
//		}
		rt.setTitle(">入场标煤单价完成情况("+stryf+")及与集团同区域比较", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"入厂标煤单价对比差",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "";
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	累计入厂标煤单价与集团公司同区域对比
	public static String getRucbmdjwcdblj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_lj = 1;					//		累计数据行
		int row_tq = 2;					//		同期数据行 
		int row_tb = 3;					//		同比数据行
		int row_tbl = 4;				//		同比增降幅
		int row_dbtitle = 5;			//		对比标题
		int row_dblj = 6;				//		对比累计
		int row_dbtq = 7;				//		对比同期
		int row_dbtb = 8;				//		对比同比
		int row_dbtbl = 9;				//		对比同比增降幅

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		累计不含税标煤单价
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
//		数据数组里面含有table表头
		String[][] data = new String[10][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 标煤单价赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		同期不含税标煤价
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
		/* 同期标煤价赋值 */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		累计不含税标煤单价
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
		/* 累计标煤价赋值 */
		while(rsl.next()){
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		同期不含税标煤单价
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
		/* 同期标煤价赋值 */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		对比公司累计
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
		/* 对比标煤价赋值 */
		while(rsl.next()){
			data[row_dbtitle][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_dblj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		对比公司同期 
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
		/* 对比标煤价赋值 */
		while(rsl.next()){
			data[row_dbtq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_lj][col_title] = yuef + "月累计";
		data[row_tq][col_title] = "同期";
		data[row_tb][col_title] = "同比差值";
		data[row_tbl][col_title] = "增(降)%";
		data[row_dbtitle][col_title] = "";
		data[row_dblj][col_title] = yuef + "月累计";
		data[row_dbtq][col_title] = "同期";
		data[row_dbtb][col_title] = "同比差值";
		data[row_dbtbl][col_title] = "增(降)%";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		data[row_dbtitle][col_fgs] = "集团";
		/*  累计数据  */
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
		/*  同期数据  */
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
		/*  对比累计数据  */
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
		/*  对比同期数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		data[row_dbtitle][col_ssgs] = "对应区域";
		/*  累计数据  */
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
		/*  同期数据  */
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
		/*  对比累计数据  */
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
		/*  对比同期数据  */
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
		
//		计算本同期差值
		for(int i = col_fgs; i < data[row_title].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算对比本同期差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dblj][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算本同期升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		计算本同期升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_dbtb][i]);
			double sy = parseDouble(data[row_dbtq][i]);
			if(by != 0 && sy != 0){
				data[row_dbtbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "月累计";
		}
		rt.setTitle(">入场标煤单价完成情况("+stryf+")及与集团同区域比较", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"入厂标煤单价对比差",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "";
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	入炉标煤单价完成情况
	public static String getRulbmdjwcqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_by = 1;					//		本月数据行
		int row_sy = 2;					//		上月数据行 
		int row_hb = 3;					//		环比数据行

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		本期不含税标煤单价
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
			"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
			"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =" +oraDate +") y" +
			",diancxxb d\n" +
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		数据数组里面含有table表头
		String[][] data = new String[4][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 标煤单价赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		环比不含税标煤价
		sql = "select d.mingc,y.bmdj from\n" +
		"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
		"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
		"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =add_months(" +oraDate +",-1)) y" +
		",diancxxb d\n" +
		"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* 环比标煤价赋值 */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_by][col_title] = yuef + "月";
		data[row_sy][col_title] = "上月";
		data[row_hb][col_title] = "环比差值";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  本月数据  */
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
		/*  上月数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  本月数据  */
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
		/*  上月数据  */
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
//		计算环比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
//		if(!"1".equals(yuef)){
//			stryf = "1-" + yuef + "月累计";
//		}
		rt.setTitle(">综合标煤单价完成情况("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
		for(int i = 1; i<=4; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(4, i, Table.ALIGN_RIGHT);
		}
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
////		构造图形
//		/*  构造图形所需结果集  */
//		String[] colname = new String[]{"单位","本月","环比差"};
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
//		/*  初始化图形  */
//		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
//				"入厂标煤单价",200,600);
//		ct.xTiltShow = true;
//		ct.showLegend = false;
//		/*  将得到的柱状图写回html  */
//		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "　　" + yuef + "月公司综合标煤价" + data[row_by][col_fgs] + 
		"元/吨";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "，环比上升" + data[row_hb][col_fgs] + "元/吨。";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "。";
			}else{
				wenz += "环比下降" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"元/吨。";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "、" + data[row_title][i]; 
					sgv  += "、" + v;	
				}else{
					jdmc += "、" + data[row_title][i]; 
					jdv  += "、" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "升幅较大，分别升高" + sgv.substring(1) +"元/吨。";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "降幅较大，分别降低" +jdv.substring(1) +"元/吨。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	入炉标煤单价完成情况(累计)
	public static String getRulbmdjwcqklj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
//		int row_by = 1;					//		本月数据行
//		int row_sy = 2;					//		上月数据行 
//		int row_hb = 3;					//		环比数据行
		int row_lj = 1;					//		累计数据行
		int row_tq = 2;					//		累计同期数据行
		int row_tb = 3;					//		同比差值
		int row_tbl = 4;				//		同比升降幅

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		本期不含税标煤单价
		String sql = 
			"select d.mingc,y.bmdj from\n" +
			"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
			"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
			"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =" +oraDate +") y" +
			",diancxxb d\n" +
			"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
			"order by d.xuh,d.id";
		ResultSetList rsl = con.getResultSetList(sql);
//		数据数组里面含有table表头
		String[][] data = new String[5][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
//		累计不含税标煤单价
		sql = "select d.mingc,y.bmdj from\n" +
		"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
		"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
		"where z.fenx = '"+SysConstant.Fenx_Leij+"' and z.riq =" +oraDate +") y" +
		",diancxxb d\n" +
		"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* 累计标煤价赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		同期不含税标煤单价
		sql = "select d.mingc,y.bmdj from\n" +
		"(select z.diancxxb_id,round_new(decode(z.rultrmpjfrl,0,0,\n" +
		"z.rultrmpjdj*29271/z.rultrmpjfrl),2) bmdj from yuezbb z\n" + 
		"where z.fenx = '"+SysConstant.Fenx_Beny+"' and z.riq =add_months(" +oraDate +",-12)) y" +
		",diancxxb d\n" +
		"where y.diancxxb_id(+) = d.id and d.fuid ="+ diancxxb_id +
		"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/* 同期标煤价赋值 */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_lj][col_title] = yuef + "月累计";
		data[row_tq][col_title] = "同期";
		data[row_tb][col_title] = "同比差值";
		data[row_tbl][col_title] = "增(降)%";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  累计数据  */
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
		/*  同期数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  累计数据  */
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
		/*  同期数据  */
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
//		计算同比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算同比升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "月累计";
		}
		rt.setTitle(">综合标煤单价完成情况("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
//		String[] colname = new String[]{"单位","本月","环比差"};
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
//		/*  初始化图形  */
//		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
//				"入厂标煤单价",200,600);
//		ct.xTiltShow = true;
//		ct.showLegend = false;
//		/*  将得到的柱状图写回html  */
//		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "　　" + yuef + "月公司综合标煤价" + data[row_lj][col_fgs] + 
		"元/吨";
		
		if(parseDouble(data[row_tb][col_fgs])>0){
			wenz += "，同比上升" + data[row_tb][col_fgs] + "元/吨。";
		}else
			if(parseDouble(data[row_tb][col_fgs])==0){
				wenz += "。";
			}else{
				wenz += "同比下降" + Math.abs(parseDouble(data[row_tb][col_fgs])) +
				"元/吨。";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_tb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "、" + data[row_title][i]; 
					sgv  += "、" + v;	
				}else{
					jdmc += "、" + data[row_title][i]; 
					jdv  += "、" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "升幅较大，分别升高" + sgv.substring(1) +"元/吨。";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "降幅较大，分别降低" +jdv.substring(1) +"元/吨。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
//	入厂与入炉煤折标煤单价差值情况一览表
	public static String getRucrlbmdjc_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		HashMap rowmap = new HashMap();
		int col_title = 0;				//		标题列
		int col_ylj = 13;				//		月累计列
		int col_nzb = 14;				//		年指标列
		int row_title = 0;				//		标题行
		int row_fgs = 1;				//		分公司行

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
		
		String sql = "select id,mingc from diancxxb where id = "+diancxxb_id+
		" or fuid = " + diancxxb_id + " order by decode(id,"+diancxxb_id+",-999,xuh),id";
		ResultSetList rsl = con.getResultSetList(sql);
//		数据数组里面含有table表头
		String[][] data = new String[rsl.getRows() + 1][15];
		int[] colwidth = new int[15];
		for(int i = 0; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		while(rsl.next()){
			rowmap.put(rsl.getString("id"), String.valueOf(rsl.getRow() + 1));
			/*  给标题列赋值  */
			data[rsl.getRow()+1][col_title] = rsl.getString("mingc");
		}
		rsl.close();

//		计算电厂各月单价差
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
//		计算分公司各月单价差
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
//		计算累计单价差
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
//		指标
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
//		标题栏
		data[row_title][col_title] = "单位"; 
		for(int i = 1; i < 13; i++ ){
			data[row_title][i] = i + "月";
		}
		data[row_title][col_ylj] = "累计";
		data[row_title][col_nzb] = "年指标";
		
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		rt.setTitle(">入厂与入炉煤折标煤单价差值情况("+yuef + "月)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
		for(int i = 1; i<=data.length; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			for(int j = 2; j <= data.length; j++ ){
				rt.body.setCellAlign(j, i, Table.ALIGN_RIGHT);
			}
		}
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
		return html;
	}
//	入厂、入炉热值差
	public static String getRezcqk_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		String strDateBQ=nianf + "-" + yuef + "-01";
		String strDateNC=nianf + "-01-01";
		Report rt = new Report();
		
		String sql="select rc.mingc as 单位,round_new((rc.本月-rl.本月)*1000/4.1816,0) as 本月,\n" +
		"       round((rc.累计-rl.累计)*1000/4.1816,0) as 累计,\n" + 
		"       round((rc.同期-rl.同期)*1000/4.1816,0) as 同期,round((rc.累计-rl.累计)*1000/4.1816,0)-round((rc.同期-rl.同期)*1000/4.1816,0) as 升降,\n"+
		"       zb.rezc as 指标, round((rc.累计-rl.累计)*1000/4.1816,0)-zb.rezc as 差值\n" + 
		" from (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    grouping(ss) as ssfz, ss,grouping(dd.mingc) dcfz,max(dd.xuh) as dcxh,\n" + 
		"    round_new(decode(sum(decode(fenx,'本月本期',rucsl)),0,0,sum(decode(fenx,'本月本期',rucsl*rucrl)))/sum(decode(fenx,'本月本期',rucsl)),2) as 本月,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计本期',rucsl)),0,0,sum(decode(fenx,'累计本期',rucsl*rucrl)))/sum(decode(fenx,'累计本期',rucsl)),2) as 累计,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计同期',rucsl)),0,0,sum(decode(fenx,'累计同期',rucsl*rucrl)))/sum(decode(fenx,'累计同期',rucsl)),2) as 同期\n" + 
		" from ( select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id,sl.laimsl as rucsl,decode(dc.id,141,zl.diancrz,zl.qnet_ar) as rucrl,\n" + 
		"              sl.fenx||decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'本期','同期') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'非上市','中国电力') as ss\n" + 
		"                   from  yueslb sl, yuetjkjb tj, yuezlb zl,vwdianc dc,(select d.id\n" + 
		"                     from diancxxb d, itemsort s, item t\n" + 
		"                     where s.bianm = 'DCSFSS'\n" + 
		"                     and s.id = t.itemsortid\n" + 
		"                     and t.mingc = d.id and t.bianm = 'SS') ss--上市非上市\n" + 
		"                   where zl.yuetjkjb_id=sl.yuetjkjb_id\n" + 
		"                         and sl.yuetjkjb_id = tj.id\n" + 
		"                         and sl.fenx=zl.fenx(+)\n" + 
		"                         and ss.id(+)=dc.id\n" + 
		"                         and (tj.riq = to_date('"+strDateBQ+"','yyyy-mm-dd') or tj.riq = add_months( to_date('"+strDateBQ+"','yyyy-mm-dd'),-12))\n" + 
		"                         and dc.fuid="+diancxxb_id+"\n" + 
		"                         and tj.diancxxb_id = dc.id) dd\n" + 
		"        group by rollup(dd.ss,dd.mingc)  ) rc,\n" + 
		" (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    round_new(decode(sum(decode(fenx,'本月本期',rulsl)),0,0,sum(decode(fenx,'本月本期',rulsl*rulrz)))/sum(decode(fenx,'本月本期',rulsl))/1000,2) as 本月,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计本期',rulsl)),0,0,sum(decode(fenx,'累计本期',rulsl*rulrz)))/sum(decode(fenx,'累计本期',rulsl))/1000,2) as 累计,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计同期',rulsl)),0,0,sum(decode(fenx,'累计同期',rulsl*rulrz)))/sum(decode(fenx,'累计同期',rulsl))/1000,2) as 同期\n" + 
		" from  (select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id, zb.fenx||decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'本期','同期') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'非上市','中国电力') as ss,zb.fadgrytrml as rulsl,zb.rultrmpjfrl as rulrz\n" + 
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
		"  and rc.mingc<>'非上市'\n" + 
		"  order by dcfz desc,ssfz desc,dcxh\n";

		ResultSetList rsl = con.getResultSetList(sql);
		
		//数据数组里面含有table表头
		String[][] data = new String[7][rsl.getRows() +1];
		
		//列宽数组
		int[] colwidth = new int[rsl.getRows() + 1];
		colwidth[0] = 75;
		for(int i =1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		
		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[0][j]=rsl.getString("单位");
			data[1][j]=rsl.getString("本月");
			data[2][j]=rsl.getString("累计");
			data[3][j]=rsl.getString("同期");
			data[4][j]=rsl.getString("升降");
			data[5][j]="≤"+rsl.getString("指标");
			data[6][j]=rsl.getString("差值");
		}
		
		data[1][0]=yuef+"月";
		data[2][0]="1-"+yuef+"月累计";
		data[3][0]="同期";
		data[4][0]="升降";
		data[5][0]="年度目标值";
		data[6][0]="与年度目标差值";
		
		rt.setTitle("1-"+yuef+"月热值差情况", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:千卡／千克", 2);
		
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		rt.body.setFixedRows(1);
		rt.body.setFixedCols(2);
		
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		
		/*  设置表格文字对齐方式  */
		for(int i =1; i <= colwidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(1,Table.ALIGN_CENTER );
		rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		//将表格信息存入html String
		html = rt.getAllPagesHtml();
		
		//构造文字分析
		String wenz = "";
		
		//构造图表
		sql="select rc.mingc as 单位,round_new((rc.本月-rl.本月)*1000/4.1816,0) as 热值差\n" +
		" from (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    grouping(ss) as ssfz, ss,grouping(dd.mingc) dcfz,max(dd.xuh) as dcxh,\n" + 
		"    round_new(decode(sum(decode(fenx,'本月本期',rucsl)),0,0,sum(decode(fenx,'本月本期',rucsl*rucrl)))/sum(decode(fenx,'本月本期',rucsl)),2) as 本月,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计本期',rucsl)),0,0,sum(decode(fenx,'累计本期',rucsl*rucrl)))/sum(decode(fenx,'累计本期',rucsl)),2) as 累计,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计同期',rucsl)),0,0,sum(decode(fenx,'累计同期',rucsl*rucrl)))/sum(decode(fenx,'累计同期',rucsl)),2) as 同期\n" + 
		" from ( select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id,sl.laimsl as rucsl,decode(dc.id,141,zl.diancrz,zl.qnet_ar) as rucrl,\n" + 
		"              sl.fenx||decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'本期','同期') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'非上市','中国电力') as ss\n" + 
		"                   from  yueslb sl, yuetjkjb tj, yuezlb zl,vwdianc dc,(select d.id\n" + 
		"                     from diancxxb d, itemsort s, item t\n" + 
		"                     where s.bianm = 'DCSFSS'\n" + 
		"                     and s.id = t.itemsortid\n" + 
		"                     and t.mingc = d.id and t.bianm = 'SS') ss--上市非上市\n" + 
		"                   where zl.yuetjkjb_id=sl.yuetjkjb_id\n" + 
		"                         and sl.yuetjkjb_id = tj.id\n" + 
		"                         and sl.fenx=zl.fenx(+)\n" + 
		"                         and ss.id(+)=dc.id\n" + 
		"                         and (tj.riq = to_date('"+strDateBQ+"','yyyy-mm-dd') or tj.riq = add_months( to_date('"+strDateBQ+"','yyyy-mm-dd'),-12))\n" + 
		"                         and dc.fuid="+diancxxb_id+"\n" + 
		"                         and tj.diancxxb_id = dc.id) dd\n" + 
		"        group by rollup(dd.ss,dd.mingc)  ) rc,\n" + 
		" (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"    round_new(decode(sum(decode(fenx,'本月本期',rulsl)),0,0,sum(decode(fenx,'本月本期',rulsl*rulrz)))/sum(decode(fenx,'本月本期',rulsl))/1000,2) as 本月,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计本期',rulsl)),0,0,sum(decode(fenx,'累计本期',rulsl*rulrz)))/sum(decode(fenx,'累计本期',rulsl))/1000,2) as 累计,\n" + 
		"    round_new(decode(sum(decode(fenx,'累计同期',rulsl)),0,0,sum(decode(fenx,'累计同期',rulsl*rulrz)))/sum(decode(fenx,'累计同期',rulsl))/1000,2) as 同期\n" + 
		" from  (select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id, zb.fenx||decode(riq,to_date('2009-7-01','yyyy-mm-dd'),'本期','同期') as fenx,\n" + 
		"              decode(nvl(ss.id,0),0,'非上市','中国电力') as ss,zb.fadgrytrml as rulsl,zb.rultrmpjfrl as rulrz\n" + 
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
		"  and rc.mingc<>'非上市'\n" + 
		"  order by dcfz desc,ssfz desc,round((rc.累计-rl.累计)*1000/4.1816,0),dcxh \n";

		rsl = con.getResultSetList(sql);
		Chart ct = new Chart(rsl,  "单位", "单位", "热值差", false, null, ipage,"1-"+ yuef+"月入厂入炉热值差",300,600);
		ct.xTiltShow=true;
		ct.showLegend=false;
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
		html += getWenzHtml(wenz,data[0].length);
		
		return html;
	}
//	矿、厂热值差情况
	public static String getKuangcrzc_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";
		Report rt = new Report();
		String sql = 
			"select d.mingc||'-'||g.mingc,k.yiy,k.ery,k.sany,k.siy,\n" +
			"k.wuy,k.liuy,k.qiy,k.bay,k.jiuy,k.shiy,k.shiyy,k.shiey,\n" + 
			"k.leij,'≤'||k.zhib zhib\n" + 
			"from kuangcrzcb k,diancxxb d, gongysb g\n" + 
			"where k.diancxxb_id = d.id and k.gongysb_id = g.id\n" + 
			"and d.fuid = "+ diancxxb_id + " and to_char(k.riq,'yyyy')='" + 
			nianf + "'" + " order by g.xuh,g.mingc,d.xuh,d.mingc";
		ResultSetList rsl = con.getResultSetList(sql);
		
		String[][] ArrHeader = new String[][]{{"单位","1月","2月","3月","4月","5月","6月",
				"7月","8月","9月","10月","11月","12月","累计","年指标"}};
		int[] colwidth = {100,40,40,40,40,40,40,40,40,40,40,40,40,40,60};
		rt.setTitle("矿、厂热值差情况", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:千卡／千克", 2);
		rt.setBody(new Table(rsl,1,0,1));
		rt.body.setFixedRows(1);
		rt.body.setFixedCols(2);
		
		rt.body.setHeaderData(ArrHeader);
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		
		html = rt.getAllPagesHtml();
//		文字构造
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
				wenz += "1-" + yuef + "累计矿、厂热值差全部完成年度指标。";
			}else if(rsl.getRows()+1 == weiw.split(",").length){
				wenz += "1-" + yuef + "累计矿、厂热值差全部未完成年度指标。";
			}else{
				wenz += "1-" + yuef + "累计矿、厂热值差完成年度指标的有:" + ("".equals(yiw)?"":yiw.substring(1))
				+";未完成年度指标的有:" + ("".equals(weiw)?"":weiw.substring(1)) + "。";
			}
			html += getWenzHtml(wenz,colwidth.length);
		}
		rsl.close();
		return html;
	}
//	运输损耗
	public static String getYunsqk_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		Report rt = new Report();
		
		String strDateBQ=nianf + "-" + yuef + "-01";
		String strOraDateBQ = DateUtil.FormatOracleDate(strDateBQ);
		String strDateNC=nianf + "-01-01";
		String strOraDateNc = DateUtil.FormatOracleDate(strDateNC);
		
		String sql=

			"select xt.mingc||decode(xt.yunsfs,0,'',1,'(火运)',2,'(汽运)') as 单位,xt.biaoz as 矿发量,xt.jingz as 净重,xt.biaoz-xt.jingz as 运损,\n" +
			"     round( decode(xt.biaoz,0,0, (xt.biaoz-xt.jingz)/xt.biaoz*100),2) as 运损率,yt.zhib as 预控指标\n" + 
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
		
		//数据数组里面含有table表头
		String[][] data = new String[6][rsl.getRows() +1];
		
		//列宽数组
		int[] colwidth = new int[rsl.getRows() + 1];
		colwidth[0] = 75;
		for(int i =1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		
		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[0][j]=rsl.getString("单位");
			data[1][j]=rsl.getString("矿发量");
			data[2][j]=rsl.getString("净重");
			data[3][j]=rsl.getString("运损");
			data[4][j]=rsl.getString("运损率");
			data[5][j]="≤"+rsl.getString("预控指标") +"%";
		}
		
		data[0][0]="厂别";
		data[1][0]="矿发量";
		data[2][0]="入厂净重";
		data[3][0]="运输损耗";
		data[4][0]="运损率%";
		data[5][0]="年度目标";
		
		rt.setTitle("1-"+yuef+"月运输损耗情况", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:吨", 2);
		
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		rt.body.setFixedRows(1);
		rt.body.setFixedCols(2);
		
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		
		/*  设置表格文字对齐方式  */
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
		
		//将表格信息存入html String
		html = rt.getAllPagesHtml();
		
		//构造文字分析
		String wenz = "";
		String weiw = "";
		String yiw = "";
		for(int i =1; i< data[0].length; i++){
			double yunsl = parseDouble(data[4][i]);
			double zb = parseDouble(data[5][i].replaceAll("≤", "").replaceAll("%", ""));
			if(yunsl - zb <=0 || zb==0){
				yiw += "," + data[0][i];
			}else{
				weiw += "," + data[0][i];
			}
		}
		if(data[0].length>0){
			if(data[0].length+1 == yiw.split(",").length){
				wenz += "1-" + yuef + "累计运损率全部控制在年度目标值内。";
			}else if(data[0].length+1 == weiw.split(",").length){
				wenz += "1-" + yuef + "累计运损率全部高于预控目标。";
			}else{
				wenz += "1-" + yuef + "累计" + ("".equals(yiw)?"":yiw.substring(1))
				+"运损率控制在年度目标值内;" + ("".equals(weiw)?"":weiw.substring(1)) + "高于预控目标。";
			}
			html += getWenzHtml(wenz,colwidth.length);
		}
		
		return html;
	}
//	水分差调整
	public static String getRucbmdjwcqdylj_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef, IPage ipage){
		String html = "";				//		返回的htmlcode
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		int row_title = 0;				//		标题行
		int row_by = 1;					//		本月数据行
		int row_sy = 2;					//		上月数据行 
		int row_hb = 3;					//		环比数据行
		int row_lj = 4;					//		累计数据行
		int row_tq = 5;					//		累计同期数据行
		int row_tb = 6;					//		同比差值
		int row_tbl = 7;				//		同比升降幅

		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		本期不含税标煤单价
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
//		数据数组里面含有table表头
		String[][] data = new String[8][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/* 标煤单价赋值 */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_by][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		环比不含税标煤价
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
		/* 环比标煤价赋值 */
		while(rsl.next()){
			data[row_sy][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		累计不含税标煤单价
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
		/* 累计标煤价赋值 */
		while(rsl.next()){
			data[row_lj][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();
//		同期不含税标煤单价
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
		/* 同期标煤价赋值 */
		while(rsl.next()){
			data[row_tq][rsl.getRow() + 3] = rsl.getString("bmdj");
		}
		rsl.close();

//		特殊列处理
		/*  标题列处理  */
		data[row_title][col_title] = "单位";
		data[row_by][col_title] = yuef + "月";
		data[row_sy][col_title] = "上月";
		data[row_hb][col_title] = "环比差值";
		data[row_lj][col_title] = yuef + "月累计";
		data[row_tq][col_title] = "同期";
		data[row_tb][col_title] = "同比差值";
		data[row_tbl][col_title] = "增(降)%";
//		中电国际处理
		data[row_title][col_fgs] = "中电国际";
		/*  本月数据  */
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
		/*  上月数据  */
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
		/*  累计数据  */
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
		/*  同期数据  */
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
//		中国电力
		data[row_title][col_ssgs] = "中国电力";
		/*  本月数据  */
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
		/*  上月数据  */
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
		/*  累计数据  */
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
		/*  同期数据  */
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
//		计算环比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_by][i]);
			double sy = parseDouble(data[row_sy][i]);
			if(by != 0 && sy != 0){
				data[row_hb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算同比差
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_lj][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tb][i] = String.valueOf(CustomMaths.sub(by, sy));
			}
		}
//		计算同比升降幅
		for(int i = col_fgs; i < data[0].length ; i++){
			double by = parseDouble(data[row_tb][i]);
			double sy = parseDouble(data[row_tq][i]);
			if(by != 0 && sy != 0){
				data[row_tbl][i] = String.valueOf(CustomMaths.mul(CustomMaths.div(by, sy, 4),100));
			}
		}
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		String stryf = yuef + "月";
		if(!"1".equals(yuef)){
			stryf = "1-" + yuef + "月累计";
		}
		rt.setTitle(">入厂标煤单价完成情况("+ stryf + ")", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:元/吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
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
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		构造图形
		/*  构造图形所需结果集  */
		String[] colname = new String[]{"单位","本月","环比差"};
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
		/*  初始化图形  */
		Chart ct = new Chart(rsl, "单位", "单位", "环比差", false, null, ipage, 
				"入厂标煤单价",200,600);
		ct.xTiltShow = true;
		ct.showLegend = false;
		/*  将得到的柱状图写回html  */
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
//		构造文字分析
		String wenz = "　　" + yuef + "月公司入厂标煤价" + data[row_by][col_fgs] + 
		"元/吨";
		
		if(parseDouble(data[row_hb][col_fgs])>0){
			wenz += "，环比上升" + data[row_hb][col_fgs] + "元/吨。";
		}else
			if(parseDouble(data[row_hb][col_fgs])==0){
				wenz += "。";
			}else{
				wenz += "环比下降" + Math.abs(parseDouble(data[row_hb][col_fgs])) +
				"元/吨。";
			}
		String sgmc = "";
		String sgv = "";
		String jdmc = "";
		String jdv = "";
		for(int i= 3; i<data[0].length ; i++){
			double v = parseDouble(data[row_hb][i]);
			if(Math.abs(v)>10){
				if(v>0){
					sgmc += "、" + data[row_title][i]; 
					sgv  += "、" + v;	
				}else{
					jdmc += "、" + data[row_title][i]; 
					jdv  += "、" + Math.abs(v);
				}
			}
		}
		if(sgmc.length()>0){
			wenz += sgmc.substring(1) + "升幅较大，分别升高" + sgv.substring(1) +"元/吨。";
		}
		if(jdmc.length()>0){
			wenz += jdmc.substring(1) + "降幅较大，分别降低" +jdv.substring(1) +"元/吨。";
		}
		html += getWenzHtml(wenz,data[row_title].length);
		return html;
	}
	public static String getShuifctz_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		Report rt = new Report();
		int row_title = 0;				//		标题行号
		int row_leij = 2;				//		累计
		int row_beny = 1;				//		累计变化
		int col_title = 0;				//		标题列
		
		String strDateBQ=nianf + "-" + yuef + "-01";
		String sql = "select mingc from diancxxb where fuid = " + diancxxb_id + " order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
		
		//数据数组里面含有table表头
		String[][] data = new String[3][rsl.getRows() + 1];
		
		//列宽数组
		int[] colwidth = new int[rsl.getRows() + 1];
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}

		sql="select dc.mingc as 单位,本月,累计\n" +
		" from  vwdianc dc ,\n" + 
		"     (select dc.id,sum(decode(sj.fenx,'本月',shuifctz)) as 本月,sum(decode(sj.fenx,'累计',shuifctz)) as 累计\n" + 
		"             from yueshchjb sj,vwdianc dc\n" + 
		"             where riq=to_date('"+strDateBQ+"','yyyy-mm-dd')\n" + 
		"             and sj.diancxxb_id(+)=dc.id\n" + 
		"             and dc.fuid=" + diancxxb_id + " \n" + 
		"             group by dc.id) sj--数据\n" + 
		" where dc.fuid=" + diancxxb_id + "\n" + 
		" and dc.id=sj.id(+)\n" + 
		" order by dc.xuh";

		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[row_title][j]=rsl.getString("单位");
			data[row_beny][j]=rsl.getString("本月");
			data[row_leij][j]=rsl.getString("累计");
		}
		data[row_beny][col_title]=yuef+"月";
		data[row_leij][col_title]="1-"+yuef+"月";
		
		rt.setTitle("水分差调整情况", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		
		/*  设置表格文字对齐方式  */
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rt.body.setColAlign(1,Table.ALIGN_RIGHT);
		rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		//将表格信息存入html String
		html = rt.getAllPagesHtml();
		
		//构造文字分析
		String wenz = "各单位应重视并按有关规定规范进行水分差调整及统计管理。";
		html += getWenzHtml(wenz,data[0].length);
		
		//图型
		sql="select bt.mingc as 单位,bt.fenx as 分项,水分差\n" +
		" from (select dc.id,dc.mingc,fx.fenx,dc.xuh\n" + 
		"             from  vwdianc dc ,vwfenxyue fx\n" + 
		"             where dc.fuid="+diancxxb_id+") bt,--表头\n" + 
		"     (select dc.id,fenx,shuifctz as 水分差\n" + 
		"             from yueshchjb sj,vwdianc dc\n" + 
		"             where riq=to_date('"+strDateBQ+"','yyyy-mm-dd')\n" + 
		"             and sj.diancxxb_id(+)=dc.id\n" + 
		"             and dc.fuid="+diancxxb_id+"\n" + 
		"             ) sj--数据\n" + 
		" where bt.id=sj.id(+) and bt.fenx=sj.fenx(+)\n" + 
		" order by bt.xuh,bt.fenx";
		
		rsl = con.getResultSetList(sql);
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		CategoryDataset dataset = cd.getRsDataChart(rsl, "单位", "分项", "水分差");
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.showLegend = true;
		ct.xTiltShow = true;//倾斜显示X轴的文字
		ct.chartBackgroundPaint=gp;
		String charImg=ct.ChartBar3D(ipage,dataset, "", 600, 250);
		
		//Chart ct = new Chart(rsl,  "分项", "单位", "水分差", false, null, ipage, yuef+"水分差调整情况",300,600);
		/*  将得到的柱状图写回html  */
		html += getChartHtml(charImg,data[0].length);
		
		return html;
	}
//	售电量情况
	public static String getShouudlqk_fgs(JDBCcon con, String diancxxb_id,String nianf, String yuef,IPage ipage){
		String html="";
		Report rt = new Report();
		int row_title = 0;				//		标题行号
		int row_leij = 4;				//		累计
		int row_leijbh = 6;				//		累计变化
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		
		String sql = "select mingc from diancxxb where fuid = " + diancxxb_id + " order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
		
		//数据数组里面含有table表头
		String[][] data = new String[7][rsl.getRows() + 3];
		
		//列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		
		String strDateBQ=nianf + "-" + yuef + "-01";
		sql="select mingc,本月,同期,decode(同期,0,0,round((本月-同期)/本月*100,2)) as 增幅,\n" +
		"       累计,累计同期,decode(累计同期,0,0,round((累计-累计同期)/累计同期*100,2)) as 累计增幅\n" + 
		"from (select decode(grouping(ss)+grouping(dd.mingc),2,max(dd.fgsmc),1,ss,dd.mingc) as mingc,\n" + 
		"      round(sum(decode(fenx,'本月',decode(btq,'本期',shoudl)))/10000,2) as 本月,\n" + 
		"      round(sum(decode(fenx,'本月',decode(btq,'同期',shoudl)))/10000,2) as 同期,\n" + 
		"      round(sum(decode(fenx,'累计',decode(btq,'本期',shoudl)))/10000,2) as 累计,\n" + 
		"      round(sum(decode(fenx,'累计',decode(btq,'同期',shoudl)))/10000,2) as 累计同期\n" + 
		"from (select dc.mingc,dc.xuh,dc.fgsmc, diancxxb_id ,fenx, shoudl,decode(riq,to_date('"+strDateBQ+"','yyyy-mm-dd'),'本期','同期') as btq,decode(nvl(ss.id,0),0,'非上市','中国电力') as ss\n" + 
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
		" where mingc<>'非上市'";
		
		rsl = con.getResultSetList(sql);
		int j=0;
		while (rsl.next()){
			j=j+1;
			data[0][j]=rsl.getString("mingc");
			data[1][j]=rsl.getString("本月");
			data[2][j]=rsl.getString("同期");
			data[3][j]=rsl.getString("增幅");
			data[4][j]=rsl.getString("累计");
			data[5][j]=rsl.getString("累计同期");
			data[6][j]=rsl.getString("累计增幅");
		}
		
		j=0;
		data[0][j]=yuef+"单位";
		data[1][j]=yuef+"月";
		data[2][j]="同期";
		data[3][j]="增幅";
		data[4][j]="累计";
		data[5][j]="同期";
		data[6][j]="增幅";
		
		rt.setTitle("1、"+yuef+"月份售电量情况", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:亿千万时", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
		rt.body.setColAlign(1,Table.ALIGN_RIGHT);
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setColFormat(i,"0.00");
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		
		//将表格信息存入htmlString
		html = rt.getAllPagesHtml();
		
		//构造文字分析
		String strSs="";
		int iSsCount=0;
		int iXjCount=0;
		String strXj="";
		String wenz = "";
		
		wenz ="至"+ yuef + "月底，公司累计完成售电量" + data[row_leij][col_fgs] + "亿千瓦时，同比";
		if(parseDouble(data[row_leij][col_fgs])>0){
			wenz += "上升" + Math.abs(parseDouble(data[row_leijbh][col_fgs])) + "%。";
			for(int i = 3; i < colwidth.length ; i++){
				if(parseDouble(data[row_leijbh][i])<0){
					strSs +=data[0][i]+ "下降" + Math.abs(parseDouble(data[row_leijbh][i])) + "%,";
					iSsCount=iSsCount+1;
				}
			}
			if  (strSs.length()>0){
				if (iSsCount==colwidth.length-2){
					wenz=wenz+"所有单位同比全部下降。";
				}else{
					wenz=wenz+"其中:"+strSs +"其他全部上升。";
				}
			}
			
		}else if(parseDouble(data[row_leij][col_fgs])<0){
			wenz += "下降" + Math.abs(parseDouble(data[row_leijbh][col_fgs])) + "%。";
			for(int i = 1; i < colwidth.length ; i++){
				if(parseDouble(data[row_leijbh][i])>0){
					strXj +=data[0][i]+ "上升" + Math.abs(parseDouble(data[row_leijbh][i])) + "%,";
					iXjCount=iXjCount+1;
				}
			}
			if  (strSs.length()>0){
				if (iXjCount==colwidth.length-2){
					wenz=wenz+"所有单位同比全部上升。";
				}else{
					wenz=wenz+"其中:"+strXj +"其他全部下降升。";
				}
			}
		}else{
			wenz += "无变化。";
		}
		
		html += getWenzHtml(wenz,data[0].length);
		
		//图型
		sql="select dc.mingc as 单位, round(shoudl/10000,2) as 售电量\n" +
		"       from yuezbb zb,vwdianc dc\n" + 
		"       where (zb.riq=to_date('"+strDateBQ+"','yyyy-mm-dd') )\n" + 
		"             and zb.diancxxb_id=dc.id(+)\n" + 
		"             and zb.fenx='本月'\n" + 
		"             and dc.fuid="+diancxxb_id+"\n" + 
		"  order by dc.xuh";

		rsl = con.getResultSetList(sql);
		Chart ct = new Chart(rsl,  "单位", "单位", "售电量", false, null, ipage, yuef+"月份售电量情况",300,600);
		ct.xTiltShow=true;
		ct.showLegend=false;
		html += getChartHtml(ct.getStringChart(Chart.ChartType_StackBar3D),data[0].length);
		
		return html;
	}
//	煤炭收耗存（库存境界量）
	public static String getMeitshcqk_fgs(JDBCcon con, String diancxxb_id, 
			String nianf, String yuef){
		String html = "";				//		返回的htmlcode
		int row_title = 0;				//		标题行号
		int row_jjml = 1;				//		警戒煤量行号
		int row_kuc = 2;				//		库存行号
		int col_title = 0;				//		标题列
		int col_fgs = 1;				//		分公司列
		int col_ssgs = 2;				//		上市公司列
		String strDate = nianf + "-" + yuef + "-01";	//		年月份
		String oraDate = DateUtil.FormatOracleDate(strDate);	//		oracle日期
//		查询电厂名称，警戒煤量SQL
		String sql = "select mingc,round(jingjcml/10000,2) jjml from diancxxb where fuid = " + diancxxb_id +
		" order by xuh,id"; 
		ResultSetList rsl = con.getResultSetList(sql);
//		数据数组里面含有table表头
		String[][] data = new String[3][rsl.getRows() + 3];
//		列宽数组
		int[] colwidth = new int[rsl.getRows() + 3];
		/*  设置列宽  */
		colwidth[0] = 60;
		for(int i = 1; i < colwidth.length ; i++){
			colwidth[i] = 50;
		}
		/*  设置电厂级的电厂名称及警戒煤量  */
		while(rsl.next()){
			data[row_title][rsl.getRow() + 3] = rsl.getString("mingc");
			data[row_jjml][rsl.getRow() + 3] = rsl.getString("jjml");
		}
		rsl.close();
//		查询库存在月收耗存合计表中取本月的kuc的SQL
		sql = 
			"select d.id,round(y.kuc/10000,2) kuc from yueshchjb y,diancxxb d\n" +
			"where y.diancxxb_id = d.id and d.fuid = "+diancxxb_id+"\n" + 
			"and y.riq = "+oraDate+"\n" + 
			"and y.fenx = '本月'\n" + 
			"order by d.xuh,d.id";
		rsl = con.getResultSetList(sql);
		/*  设置库存  */
		while(rsl.next()){
			data[row_kuc][rsl.getRow() + 3] = rsl.getString("kuc");
		}
		rsl.close();
		/*  加载标题列(第一列)   */
		data[row_title][col_title] = "单位";
		data[row_jjml][col_title] = "警戒煤量";
		data[row_kuc][col_title] = "月末库存";
//		加载分公司数据
		/*  设置标题  */
		data[row_title][col_fgs] = "中电国际";
		/*  计算警戒煤量  */
		sql = "select sum(jingjcml/10000) jjml from diancxxb where fuid = " + diancxxb_id;
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			data[row_jjml][col_fgs] = rsl.getString("jjml");
		}
		rsl.close();
		/*  计算库存  */
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
//		加载上市公司数据
		/*  设置标题  */
		data[row_title][col_ssgs] = "中国电力";
		/*  计算警戒煤量  */
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
		/*  计算库存  */
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
//		表格处理 
		/*  定义报表  */
		Report rt = new Report();
		/*  设置表头名称、列宽、报表右侧单位  */
		rt.setTitle(">煤炭购耗存情况("+yuef + "月)", colwidth);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.setDefaultTitleRight("单位:万吨", 2);
		/*  设置表格内容  */
		rt.setBody(new Table(data,0,0,1));
		/*  设置表格宽度  */
		rt.body.setWidth(colwidth);
		/*  设置表格标题行高度  */
		rt.body.setRowHeight(1, 40);
		/*  设置表格文字对齐方式  */
		for(int i = 1; i<=3; i++){
			rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
		}
		for(int i =2; i <= colwidth.length ; i++){
			rt.body.setCellAlign(1, i, Table.ALIGN_CENTER);
			rt.body.setCellAlign(2, i, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(3, i, Table.ALIGN_RIGHT);
		}
//		将表格信息存入htmlString
		html = rt.getAllPagesHtml();
//		设置文字说明
//		文字说明变量
		String wenz = "";
		/*  本月来耗存  */
		sql = "select round(sum(shouml/10000),2) sm,\n" +
			"    round(sum((y.fady + y.gongry + y.sunh + y.qith + y.diaocl - y.shuifctz)/10000),2) hm,\n" + 
			"    round(sum(y.kuc/10000),2) kc\n" + 
			"    from yueshchjb y, diancxxb d\n" + 
			"    where y.diancxxb_id = d.id and y.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"    and y.riq = "+oraDate+"\n" + " and d.fuid = "+diancxxb_id+"";
		rsl = con.getResultSetList(sql);
		if(rsl.next()){
			wenz = "　　" + yuef + "月公司购入原煤 " + rsl.getString("sm") + "万吨，耗煤 " + 
			rsl.getString("hm") + "万吨，月末库存 " + rsl.getString("kc") + "万吨；";
		}
		rsl.close();
		/*  累计来耗同比  */
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
			/*  累计来煤  */
			wenz += "累计购煤 " + rsl.getString("sm") + "万吨，同比";
			/*  判断增降幅  */
			if(rsl.getDouble("smtb") > 0){
				wenz += "增幅 ";
			}else{
				wenz += "降幅 ";
			}
			wenz += rsl.getString("smtb") + "%，";
			/*  累计耗煤  */
			wenz += "累计耗煤 " + rsl.getString("hm") + "万吨，同比";
			/*  判断增降幅  */
			if(rsl.getDouble("hmtb") > 0){
				wenz += "增幅 ";
			}else{
				wenz += "降幅 ";
			}
			wenz += rsl.getString("hmtb") + "%。";
		}
		rsl.close();
		html += getWenzHtml(wenz,data[0].length);
		return html;
	}
	
	private static String getJicmfrlqk_fgswzdb(String sj, String dw, String rz){
		String wenz = "";
		if(!dw.equals("")){
			boolean isOnly = false;
			isOnly = dw.substring(1).split("、").length == 1;
			String only = "";
			if(!isOnly){
				only = "分别";
			}
			wenz += dw.substring(1) + "热值"+sj+"幅较大，环比"+only+ 
			("升".equals(sj)?"上升":"下降") +
			rz.substring(1) + "千卡/千克。";
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
//		取得文字分析
//    	将文字中的特殊字符替换成html语言
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