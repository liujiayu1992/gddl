package com.zhiren.zidy.duibcx;

import java.util.HashMap;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.zidy.ZidyOperation;
/*
 * 作者：王磊
 * 时间：2009-12-09
 * 描述：增加对上级公司的处理
 */
/*
 * 作者：王磊
 * 时间：2009-08-29 02：30
 * 描述：增加对日期参数的处理
 */
/*
 * 作者：王磊
 * 时间：2009-08-18 13：26
 * 描述:修改插入duibcxsjlpzb数据重复的问题
 */
/*
 * 作者:王磊
 * 时间：2009-08-18 10：22
 * 描述：增加CopyDesign方法完成报表的复制功能
 */
/*
 * 作者：王磊
 * 时间：2009-08-04 16：39
 * 描述：修改对比查询生成报表时无数据不显示表头的问题
 */
public class DuibcxOperation {
	public static final String StartDate = "startDate";
	public static final String EndDate = "endDate";
	public static final String Duibcx_ColType_DataSource = "1";
	public static final String Duibcx_ColType_Formula = "0";
	public static void SaveDesignParam(String paramtype, JDBCcon con,long dcid,String faid,String type){
		if("月报对比".equals(paramtype)){
			String sql = "insert into zidyfacsb(id,zidyfa_id,z_paramname,z_datatypes,\n" +
			"z_prompttext,z_controltype,zidysjy_id,z_labelfield,z_valuefield,\n" + 
			"z_defaultvalue,z_controlwidth) values(getnewid(" + dcid+")," + faid + ",'"+type+"年份',"
			+"'number','"+type+"年份:','combo',100,'ylabel','yvalue','?本年',60)";
			con.getInsert(sql);
			sql = "insert into zidyfacsb(id,zidyfa_id,z_paramname,z_datatypes,\n" +
			"z_prompttext,z_controltype,zidysjy_id,z_labelfield,z_valuefield,\n" + 
			"z_defaultvalue,z_controlwidth) values(getnewid(" + dcid+")," + faid + ",'"+type+"月份',"
			+"'number','"+type+"月份:','combo',101,'mlabel','mvalue','?本月',60)";
			con.getInsert(sql);
		}else{
			String sql = "insert into zidyfacsb(id,zidyfa_id,z_paramname,z_datatypes,\n" +
			"z_prompttext,z_controltype,zidysjy_id,z_labelfield,z_valuefield,\n" + 
			"z_defaultvalue,z_controlwidth) values(getnewid(" + dcid+")," + faid + ",'"+type+"日期',"
			+"'date','"+type+"日期:','dateField',101,'mlabel','mvalue','?本日',100)";
			con.getInsert(sql);
		}
	}
	
	public static void SaveDesign(long dcid,long RId, String Rname,String Dname,String Dtype){
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String DesignID = MainGlobal.getNewID(dcid);
		String sql = "insert into zidyfa(id,z_report_id,z_name,z_remark,z_designer,z_time) values("
			+ DesignID + "," + DesignID + ",'" + Dname + "','" + Dtype + "','" + Rname + "',sysdate)";
		con.getInsert(sql);
		sql = "insert into zonghfxdbglb(id,diancxxb_id,renyxxb_id,zidyfa_id) values(getnewid("
			+ dcid + "),"+ dcid + "," + RId + "," + DesignID + ")";
		con.getInsert(sql);
		SaveDesignParam(Dtype,con,dcid,DesignID,"起始");
		SaveDesignParam(Dtype,con,dcid,DesignID,"终止");
		con.commit();
		con.Close();
	}
	
	public static void CopyDesign(long dcid,long RId, String Rname,String DId){
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String sql = "select * from zidyfa where id = " + DId;
		ResultSetList rsl = con.getResultSetList(sql);
		String DesignID = MainGlobal.getNewID(dcid);
		if(rsl.next()){
			String Dname = rsl.getString("z_name");
			String Dtype = rsl.getString("z_remark");
			sql = "insert into zidyfa(id,z_report_id,z_name,z_remark,z_designer,z_time) values("
				+ DesignID + "," + DesignID + ",'" + Dname + "','" + Dtype + "','" + Rname + "',sysdate)";
			con.getInsert(sql);
			sql = "insert into zonghfxdbglb(id,diancxxb_id,renyxxb_id,zidyfa_id) values(getnewid("
				+ dcid + "),"+ dcid + "," + RId + "," + DesignID + ")";
			con.getInsert(sql);
			SaveDesignParam(Dtype,con,dcid,DesignID,"起始");
			SaveDesignParam(Dtype,con,dcid,DesignID,"终止");
		}
		rsl.close();
		sql = "select * from duibcxsjlb where zidyfa_id =" + DId;
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			String newsjlid = MainGlobal.getNewID(dcid);
			sql = "insert into duibcxsjlpzb(id,duibcxsjlb_id,z_code,z_value) (\n" +
				"select getnewid("+dcid+"),"+newsjlid+",z_code,z_value from duibcxsjlpzb \n" +
				"where duibcxsjlb_id =" + rsl.getString("id") +")";
			con.getUpdate(sql);
			sql = "insert into duibcxsjlb(id,zidyfa_id,colnum,colsign,coltype) (\n" +
				"select "+newsjlid+","+DesignID+",colnum,colsign,coltype \n" +
				"from duibcxsjlb where id =" + rsl.getString("id") + ")";
			con.getUpdate(sql);
		}
		rsl.close();
		
		con.commit();
		con.Close();
	}
	
	public static String InsertDuibcxDataCol(){
		return "";
	}
	
	
	public static String[] getTableHeaderCol(String GroupID){
		String TableHeadCol[] = new String[7]; 
		String sql = "select * from ZIDYJCSJYFZ where id=" + GroupID; 
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			TableHeadCol[0] = rsl.getString("selectSql");
			TableHeadCol[1] = rsl.getString("groupbysql");
			TableHeadCol[2] = rsl.getString("orderbysql");
			TableHeadCol[3] = rsl.getString("colname_cn");
			TableHeadCol[4] = rsl.getString("colwidth");
			TableHeadCol[5] = rsl.getString("selectsql_jc");
			TableHeadCol[6] = rsl.getString("colbm");
		}
		return TableHeadCol;
	}
	
	public static String getDiancList(String baobkjId){
		JDBCcon con=new JDBCcon();
		String sql="select diancxxb_id from dianckjmx where dianckjb_id="+baobkjId;
		StringBuffer str=new StringBuffer();
		if(!con.getHasIt(sql)){
			return null;
		}
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			str.append(rs.getString("diancxxb_id")).append(",");
		}
		return str.toString().substring(0,str.length()-1);
	}
	
	public static Report CreateReport(String DesignID, String GroupID, List Parameters,String baobkjId){
		String strName = "";		//报表名称
		String strType = "";		//报表类型
		String[][] arrTableHead;	//报表表头
		int[] arrColWidth;			//报表列宽度
		String[] TableHeadCol ;		//报表的分组列
		int TableHeadColNum = 0;	//表头行数 
		String strHeaderColSql = "";//表头SQL
		String[][] arrColSql;		//存储各列的列ID【0】列取值方法【1】和列取值SQL【2】
		String[][] arrData;			//数据数组
		HashMap colMap = new HashMap();	//列标识
		HashMap rowMap = new HashMap();	//行标识
		
//		1、取得报表名称
		String sql = 
			"select f.z_name,f.z_remark from zidyfa f where f.id=" + DesignID;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			strName = rsl.getString("z_name");
			strType = rsl.getString("z_remark");
		}
		rsl.close();
//		2、取得报表的 表头列 相关信息
		TableHeadCol = DuibcxOperation.getTableHeaderCol(GroupID);
		if(TableHeadCol[0] == null){
			return null;
		}
		TableHeadColNum = TableHeadCol[6].split(",").length;
//		取得报表列设置的结果集
		sql = "select * from DUIBCXSJLB where zidyfa_id=" + DesignID + " order by colnum";
		rsl = con.getResultSetList(sql);
		if(rsl.getRows() == 0) return null;
//		初始化数据列的取得方法
		arrColSql = new String[6][TableHeadColNum + rsl.getRows()];
//		初始化报表表头的数组
		arrTableHead = new String[2][TableHeadColNum + rsl.getRows()];
		String[] tmp = TableHeadCol[3].split(",");
		for(int i=0; i< tmp.length; i++){
			arrTableHead[0][i] = tmp[i];
			arrTableHead[1][i] = tmp[i];
		}
//		初始化报表列宽的数组
		arrColWidth = new int[TableHeadColNum + rsl.getRows()];
		tmp = TableHeadCol[4].split(",");
		for(int i=0; i< tmp.length; i++){
			arrColWidth[i] = Integer.parseInt(tmp[i]);
		}
//		循环列数据设置相关报表值
		while(rsl.next()){
			String strcol = "";		//数据列
			String basicSql = "";	//基础数据
			String whereSql = "";	//条件
			String oper = "";		//统计方法
			String weid = "";		//加权表达式
			String col = "";		//列名
			String paramname = "";	//参数名
			String paramBegin = "";	//参数起始偏移量
			String paramEnd = "";	//参数结束偏移量
			String colformula = "";	//公式
//			设置列标识
			colMap.put(String.valueOf(TableHeadColNum + rsl.getRow()), rsl.getString("colsign"));
			sql = "select * from DUIBCXSJLPZB where DUIBCXSJLB_ID =" + rsl.getString("id");
			ResultSetList rs = con.getResultSetList(sql);
			while(rs.next()){
				if(SysConstant.CustomAttribute_DataSource.equalsIgnoreCase(rs.getString("z_code"))){
//					设置基础数据源
					basicSql = ZidyOperation.getBasicSQL(con,rs.getString("z_value"));
				}else if(SysConstant.CustomAttribute_ColHead.equalsIgnoreCase(rs.getString("z_code"))){
//					设置主表头
					arrTableHead[0][TableHeadColNum + rsl.getRow()] = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColSubHead.equalsIgnoreCase(rs.getString("z_code"))){
//					设置副表头
					arrTableHead[1][TableHeadColNum + rsl.getRow()] = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColWidth.equalsIgnoreCase(rs.getString("z_code"))){
//					设置列宽
					arrColWidth[TableHeadColNum + rsl.getRow()] = rs.getInt("z_value");
				}else if(SysConstant.CustomAttribute_ColWord.equalsIgnoreCase(rs.getString("z_code"))){
//					设置列名
					arrColSql[3][TableHeadColNum + rsl.getRow()] = rs.getString("z_value");
					col = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColFormat.equalsIgnoreCase(rs.getString("z_code"))){
//					设置格式化
					arrColSql[4][TableHeadColNum + rsl.getRow()] = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColAlign.equalsIgnoreCase(rs.getString("z_code"))){
//					设置对齐
					arrColSql[5][TableHeadColNum + rsl.getRow()] = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_SourceParamCol.equalsIgnoreCase(rs.getString("z_code"))){
//					设置参数名
					paramname = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColOperational.equalsIgnoreCase(rs.getString("z_code"))){
//					设置统计方式
					oper = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColWeighted.equalsIgnoreCase(rs.getString("z_code"))){
//					设置统计加权表达式
					weid = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColWeighted.equalsIgnoreCase(rs.getString("z_code"))){
//					设置统计加权表达式
					weid = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ParamPointBegin.equalsIgnoreCase(rs.getString("z_code"))){
//					设置参数起始偏移
					paramBegin = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ParamPointEnd.equalsIgnoreCase(rs.getString("z_code"))){
//					设置参数起始偏移
					paramEnd = rs.getString("z_value");
				}else if(SysConstant.CustomAttribute_ColFormula.equalsIgnoreCase(rs.getString("z_code"))){
//					设置公式
					colformula = rs.getString("z_value");
				}
				
				
			}
			rs.close();
//			设置列sql
			if("加权".equals(oper)){
				strcol += "decode(sum(" + weid + "),0,0,round_new(sum("+weid + "*" +col+")/sum("+weid+"),2)) " + col;
			}else{
				strcol += oper + "(" + col +") " + col;
			}
//			设置参数
			if(strType.equals("月报对比")){ 
				if(col.toLowerCase().matches("\\bo_.*")){
					if("0".equals(paramEnd)){
						whereSql = whereSql + " and " + paramname + " = to_date('终止年份-终止月份-01','yyyy-mm-dd') ";
					}else{
						whereSql = whereSql + " and " + paramname + " = add_months(to_date('终止年份-终止月份-01','yyyy-mm-dd')," + paramEnd + ") ";
					}
				}else{
					if("0".equals(paramBegin)){
						whereSql = whereSql + " and " + paramname + " >= to_date('起始年份-起始月份-01','yyyy-mm-dd') ";
					}else{
						whereSql = whereSql + " and " + paramname + " >= add_months(to_date('起始年份-起始月份-01','yyyy-mm-dd'),"+ paramBegin +") ";
					}
					if("0".equals(paramEnd)){
						whereSql = whereSql + " and " + paramname + " <= to_date('终止年份-终止月份-01','yyyy-mm-dd') ";
					}else{
						whereSql = whereSql + " and " + paramname + " <= add_months(to_date('终止年份-终止月份-01','yyyy-mm-dd')," + paramEnd + ") ";
					}
				}
			}else{
				if(col.toLowerCase().matches("\\bo_.*")){
					if("0".equals(paramEnd)){
						whereSql = whereSql + " and " + paramname + " = 终止日期 ";
					}else{
						whereSql = whereSql + " and " + paramname + " = (终止日期 + " + paramEnd + ") ";
					}
				}else{
					if("0".equals(paramBegin)){
						whereSql = whereSql + " and " + paramname + " >= 起始日期 ";
					}else{
						whereSql = whereSql + " and " + paramname + " >= (起始日期 + "+ paramBegin +") ";
					}
					if("0".equals(paramEnd)){
						whereSql = whereSql + " and " + paramname + " <= 终止日期 ";
					}else{
						whereSql = whereSql + " and " + paramname + " <= (终止日期 + " + paramEnd + ") ";
					}
				}
			}
			if(getDiancList(baobkjId)==null||getDiancList(baobkjId).equals("")){
				whereSql+=" and (d.id=电厂ID or d.fuid=电厂ID)";
			}else{
				whereSql+=" and d.id in ("+getDiancList(baobkjId)+")";
			}
			/*if(fgsdc==1){
				whereSql += " and ( d.fgsid = 电厂ID or d.shangjgsid = 电厂ID )";
			}else if(fgsdc ==0){
				whereSql += " and d.id = 电厂ID ";
			}else if(fgsdc==-1){
				whereSql += " and (d.id = 电厂ID or d.fuid=电厂ID)";
			}*/
//			构造SQL数组
			arrColSql[0][TableHeadColNum + rsl.getRow()] = rsl.getString("id");
			if("1".equals(rsl.getString("coltype"))){
				arrColSql[1][TableHeadColNum + rsl.getRow()] = "SQL";
				arrColSql[2][TableHeadColNum + rsl.getRow()] = "select " + TableHeadCol[0]
				+ "," + strcol + " from ("
				+ basicSql + whereSql + ") " + TableHeadCol[1] + " " + TableHeadCol[2];
//				构造表头列的SQL
				if(rsl.getRow() != 0)
					strHeaderColSql += " union ";
				strHeaderColSql += "select " + TableHeadCol[5] + " from (" + basicSql 
								+ whereSql + ") " ;
			}else{
				arrColSql[1][TableHeadColNum + rsl.getRow()] = "公式";
				arrColSql[2][TableHeadColNum + rsl.getRow()] = colformula;
			}
		}
		rsl.close();
//		最终构造表头列的SQL
		strHeaderColSql = "select "+TableHeadCol[0]+" from ("+ strHeaderColSql +")" + TableHeadCol[1] + " " + TableHeadCol[2];
//		取得表头列数组并将数据数组赋值
		if(Parameters != null){
			sql = ZidyOperation.getDataSourceSql(Parameters,strHeaderColSql);
		}else{
			sql = strHeaderColSql;
		}
		rsl = con.getResultSetList(sql);
		if(rsl.getRows()==0){
			arrData = new String[1][arrColSql[0].length];
//			return null;
		}else{
			arrData = new String[rsl.getRows()][arrColSql[0].length];
			String[] strHeadCol = TableHeadCol[6].split(",");
			while(rsl.next()){
				String rowmap = "";
				for(int i = 0 ; i < strHeadCol.length ;  i++){
					arrData[rsl.getRow()][i] = rsl.getString(strHeadCol[i]);
					rowmap += rsl.getString(strHeadCol[i]);
				}
				rowMap.put(rowmap, String.valueOf(rsl.getRow()));
			}
			rsl.close();
	//		按列赋值
			for(int i = TableHeadColNum; i < arrColSql[0].length ; i ++){
				if("SQL".equals(arrColSql[1][i])){
					if(Parameters != null )
						sql = ZidyOperation.getDataSourceSql(Parameters,arrColSql[2][i]);
					rsl = con.getResultSetList(sql);
					while(rsl.next()){
						String rowmap = "";
						for(int m = 0 ; m < strHeadCol.length ;  m++){
							//arrData[rsl.getRow()][m] = rsl.getString(strHeadCol[m]);
							rowmap += rsl.getString(strHeadCol[m]);
						}
						if((String)rowMap.get(rowmap) != null){
							int row = Integer.parseInt((String)rowMap.get(rowmap));
							arrData[row][i]	= rsl.getString(arrColSql[3][i]);
						}
					}
				}
			}
	//		计算公式
	//		公式计算变量
			for(int i = TableHeadColNum; i < arrColSql[0].length ; i ++){
				if("公式".equals(arrColSql[1][i])){
					for(int m =0; m < arrData.length; m++){
						try {
							Interpreter bsh = new Interpreter();
							for(int n=TableHeadColNum; n < arrColSql[0].length ; n++){
								bsh.set((String)colMap.get(String.valueOf(n)), Double.parseDouble((arrData[m][n]==null||"".equals(arrData[m][n])?"0":arrData[m][n])));
							}
							bsh.eval("gongs = " + arrColSql[2][i]);
							String value = String.valueOf(bsh.get("gongs"));
							if("NaN".equals(value) || "Infinity".equals(value)){
								value = "";
							}
							arrData[m][i] = value;
							
						} catch (EvalError e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		Report rt = new Report();
//		设置标题
		rt.setTitle(strName, arrColWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		设置报表体
		rt.setBody(new Table(arrData, 2, 0, TableHeadColNum));
		rt.body.setWidth(arrColWidth);
		rt.body.setHeaderData(arrTableHead);
		rt.body.setPageRows(40);
		rt.body.mergeFixedRowCol();
		for(int i = TableHeadColNum; i < arrColSql[0].length ; i ++){
			rt.body.setColFormat(i+1, arrColSql[4][i]);
			rt.body.setColAlign(i+1, Integer.parseInt(arrColSql[5][i]));
		}
		return rt;
	}
	
	public static Report CreateReport(String DesignID, boolean isparam, List Parameters, String baobkjId){
		String strName = "";		//报表名称
		String strType = "";		//报表类型
		String strGrouptype = "";	//报表数据类型
		String[][] arrTableHead;	//报表表头
		int[] arrColWidth;			//报表列宽度
		String[] TableHeadCol ;		//报表的分组列
		int TableHeadColNum = 0;	//表头行数 
		String strHeaderColSql = "";//表头SQL
		String[][] arrColSql;		//存储各列的列ID【0】列取值方法【1】和列取值SQL【2】
		String[][] arrData;			//数据数组
		HashMap colMap = new HashMap();	//列标识
		HashMap rowMap = new HashMap();	//行标识
//		取得报表的分组名称类型
		String sql = 
			"select l.mingc, f.z_name, f.z_remark, l.zhi\n" +
			"       from zidyfa f, duibcxfzlbb l, duibcxfzb z\n" + 
			"where z.duibcxfzlbb_id = l.id and z.zidyfa_id = f.id\n" + 
			"and f.id =" + DesignID;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			strName = rsl.getString("z_name");
			strType = rsl.getString("z_remark");
			strGrouptype = rsl.getString("mingc");
//			TableHeadCol = rsl.getString("zhi");
		}
		rsl.close();
//		取得报表的 表头列 相关信息
		TableHeadCol = DuibcxOperation.getTableHeaderCol(strGrouptype);
		if(TableHeadCol[0] == null){
			return null;
		}
		TableHeadColNum = TableHeadCol[6].split(",").length;
//		取得报表列设置的结果集
		sql = "select * from DUIBCXSJLB where zidyfa_id=" + DesignID + " order by xuh";
		rsl = con.getResultSetList(sql);
		
		if(rsl.getRows() == 0) return null;
//		初始化数据列的取得方法
		arrColSql = new String[6][TableHeadColNum + rsl.getRows()];
//		初始化报表表头的数组
		arrTableHead = new String[2][TableHeadColNum + rsl.getRows()];
		String[] tmp = TableHeadCol[3].split(",");
		for(int i=0; i< tmp.length; i++){
			arrTableHead[0][i] = tmp[i];
			arrTableHead[1][i] = tmp[i];
		}
//		初始化报表列宽的数组
		arrColWidth = new int[TableHeadColNum + rsl.getRows()];
		tmp = TableHeadCol[4].split(",");
		for(int i=0; i< tmp.length; i++){
			arrColWidth[i] = Integer.parseInt(tmp[i]);
		}
//		循环列数据设置相关报表值
		while(rsl.next()){
			String strcol = "";		//数据列
			String basicSql = "";	//基础数据
			String whereSql = "";	//条件
//			设置列标识
			colMap.put(String.valueOf(TableHeadColNum + rsl.getRow()), rsl.getString("liebz"));
//			设置列宽
			arrColWidth[TableHeadColNum + rsl.getRow()] = rsl.getInt("liek");
//			设置表头
			arrTableHead[0][TableHeadColNum + rsl.getRow()] = rsl.getString("biaot");
			arrTableHead[1][TableHeadColNum + rsl.getRow()] = rsl.getString("fubt");
//			取得当前列的sql配置（得到构成sql字段的String）
			sql = "select * from zidyjcsjyms where id = " + rsl.getString("zidyjcsjyms_id");
			ResultSetList rs = con.getResultSetList(sql);
			if(rs.next()){
				String oper = rs.getString("z_operational");
				String weid = rs.getString("z_weighted");
				String col = rs.getString("z_column");
				String format = rs.getString("z_format");
				String align = rs.getString("z_align");
				basicSql = ZidyOperation.getBasicSQL(con,rs.getString("zidyjcsjy_id"));
				if("加权".equals(oper)){
					strcol += "decode(sum(" + weid + "),0,0,round_new(sum("+weid + "*" +col+")/sum("+weid+"),2)) " + col;
				}else{
					strcol += oper + "(" + col +") " + col;
				}
//				sql中的列名
				arrColSql[3][TableHeadColNum + rsl.getRow()] = col;
				arrColSql[4][TableHeadColNum + rsl.getRow()] = format;
				arrColSql[5][TableHeadColNum + rsl.getRow()] = align;
			}
			rs.close();
//			取得sql的条件选项
			if(isparam){
				sql = "select * from DUIBCXSJLCSB where shujlb_id = " + rsl.getString("id");
				rs = con.getResultSetList(sql);
				while(rs.next()){
					if(DuibcxOperation.StartDate.equals(rs.getString("leib"))){
						if("0".equals(rs.getString("zhi"))){
							whereSql = whereSql + " and riq >= to_date('起始年份-起始月份-01','yyyy-mm-dd') ";
						}else{
							whereSql = whereSql + " and riq >= add_months(to_date('起始年份-起始月份-01','yyyy-mm-dd'),"+rs.getString("zhi") +") ";
						}
					}else if(DuibcxOperation.EndDate.equals(rs.getString("leib"))){
						if("0".equals(rs.getString("zhi"))){
							whereSql = whereSql + " and riq <= to_date('终止年份-终止月份-01','yyyy-mm-dd') ";
						}else{
							whereSql = whereSql + " and riq <= add_months(to_date('终止年份-终止月份-01','yyyy-mm-dd'),"+rs.getString("zhi")+") ";
						}
					}else{
						whereSql = whereSql + " and " + rs.getString("leib") + " in (" +
						rs.getString("zhi") + ")";
					}
				}
				rs.close();
			}
			if(getDiancList(baobkjId)==null||getDiancList(baobkjId).equals("")){
				whereSql+=" and (d.id=电厂ID or d.fuid=电厂ID)";
			}else{
				whereSql+=" and d.id in ("+getDiancList(baobkjId)+")";
			}
			/*if(fgsdc==1){
				whereSql += " and ( d.fgsid = 电厂ID or d.shangjgsid = 电厂ID ) ";
			}else if(fgsdc ==0){
				whereSql += " and d.id = 电厂ID ";
			}else if(fgsdc==-1){
				whereSql += " and (d.id = 电厂ID or d.fuid=电厂ID)";
			}*/
//			构造SQL数组
			arrColSql[0][TableHeadColNum + rsl.getRow()] = rsl.getString("id");
			if("".equals(rsl.getString("gongs"))){
				arrColSql[1][TableHeadColNum + rsl.getRow()] = "SQL";
				arrColSql[2][TableHeadColNum + rsl.getRow()] = "select " + TableHeadCol[0]
				+ "," + strcol + " from ("
				+ basicSql + whereSql + ") " + TableHeadCol[1] + " " + TableHeadCol[2];
			}else{
				arrColSql[1][TableHeadColNum + rsl.getRow()] = "公式";
				arrColSql[2][TableHeadColNum + rsl.getRow()] = rsl.getString("gongs");
			}
			
//			构造表头列的SQL
			if(rsl.getRow() != 0)
				strHeaderColSql += " union ";
			strHeaderColSql += "select " + TableHeadCol[5] + " from (" + basicSql 
							+ whereSql + ") " ;
		}
		rsl.close();
//		最终构造表头列的SQL
		String strHeadWhere = "";
		if(!isparam){
			strHeadWhere = " where rownum < 10 ";
		}
		strHeaderColSql = "select "+TableHeadCol[0]+" from ("+ strHeaderColSql +")" + strHeadWhere + TableHeadCol[1] + " " + TableHeadCol[2];
//		取得表头列数组并将数据数组赋值
		if(Parameters != null){
			sql = ZidyOperation.getDataSourceSql(Parameters,strHeaderColSql);
		}
		//else{
		//}
		rsl = con.getResultSetList(sql);
		arrData = new String[rsl.getRows()][arrColSql[0].length];
		String[] strHeadCol = TableHeadCol[6].split(",");
		while(rsl.next()){
			String rowmap = "";
			for(int i = 0 ; i < strHeadCol.length ;  i++){
				arrData[rsl.getRow()][i] = rsl.getString(strHeadCol[i]);
				rowmap += rsl.getString(strHeadCol[i]);
			}
			rowMap.put(rowmap, String.valueOf(rsl.getRow()));
		}
		rsl.close();
//		按列赋值
		for(int i = TableHeadColNum; i < arrColSql[0].length ; i ++){
			if("SQL".equals(arrColSql[1][i])){
				if(Parameters != null )
					sql = ZidyOperation.getDataSourceSql(Parameters,arrColSql[2][i]);
				rsl = con.getResultSetList(sql);
				while(rsl.next()){
					String rowmap = "";
					for(int m = 0 ; m < strHeadCol.length ;  m++){
						//arrData[rsl.getRow()][m] = rsl.getString(strHeadCol[m]);
						rowmap += rsl.getString(strHeadCol[m]);
					}
					if((String)rowMap.get(rowmap) != null){
						int row = Integer.parseInt((String)rowMap.get(rowmap));
						arrData[row][i]	= rsl.getString(arrColSql[3][i]);
					}
				}
			}
		}
//		计算公式
//		公式计算变量
		for(int i = TableHeadColNum; i < arrColSql[0].length ; i ++){
			if("公式".equals(arrColSql[1][i])){
				for(int m =0; m < arrData.length; m++){
					try {
						Interpreter bsh = new Interpreter();
						for(int n=TableHeadColNum; n < arrColSql[0].length ; n++){
							bsh.set((String)colMap.get(String.valueOf(n)), Double.parseDouble((arrData[m][n]==null||"".equals(arrData[m][n])?"0":arrData[m][n])));
						}
						bsh.eval("gongs = " + arrColSql[2][i]);
						String value = String.valueOf(bsh.get("gongs"));
						if("NaN".equals(value) || "Infinity".equals(value)){
							value = "";
						}
						arrData[m][i] = value;
						
					} catch (EvalError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		Report rt = new Report();
//		设置标题
		rt.setTitle(strName, arrColWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		设置报表体
		rt.setBody(new Table(arrData, 2, 0, TableHeadColNum));
		rt.body.setWidth(arrColWidth);
		rt.body.setHeaderData(arrTableHead);
		rt.body.setPageRows(40);
		rt.body.mergeFixedRowCol();
		for(int i = TableHeadColNum; i < arrColSql[0].length ; i ++){
			rt.body.setColFormat(i+1, arrColSql[4][i]);
			rt.body.setColAlign(i+1, Integer.parseInt(arrColSql[5][i]));
		}
		return rt;
	}
}
