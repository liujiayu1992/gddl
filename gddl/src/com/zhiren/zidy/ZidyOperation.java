package com.zhiren.zidy;

import java.util.Date;
import java.util.List;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.report.Chart;

public class ZidyOperation {
	public static final String group_strHeader = "ҳü";
	public static final String group_strFooter = "ҳ��";
	public static final String group_strtHeader = "��ͷ";
	public static final String group_strtBodyer = "����";
	
	public static final String param_Vtype_date = "date";
	public static final String param_Vtype_datetime = "datetime";
	public static final String param_Vtype_varchar = "varchar";
	public static final String param_Vtype_number = "number";
	
	public static final String param_Ctrl_textField = "textField";
	public static final String param_Ctrl_numberField = "numberField";
	public static final String param_Ctrl_dateField = "dateField";
	public static final String param_Ctrl_combo = "combo";
	
	public static final String param_DefV_Day = "?����";
	public static final String param_DefV_LastDay = "?��һ��";
	public static final String param_DefV_NextDay = "?��һ��";
	public static final String param_DefV_Month = "?����";
	public static final String param_DefV_LastMonth = "?��һ��";
	public static final String param_DefV_NextMonth = "?��һ��";
	public static final String param_DefV_Year = "?����";
	public static final String param_DefV_LastYear = "?��һ��";
	public static final String param_DefV_NextYear = "?��һ��";
	
	public static final int param_DefV_type_Sql = 0;
	public static final int param_DefV_type_Field = 1;
	
	public static final int direction_Row = 0;
	public static final int direction_Col = 1;
	
	public static final String ChartType_xy = "����ͼ";
	public static final String ChartType_pie = "����ͼ";
	public static final String ChartType_bar = "��״ͼ";
	public static final String ChartType_time = "ʱ��ͼ";
	
	public static String getDefaultValueSql(String defV,String vtype) {
		return getDefaultValue(defV,vtype,param_DefV_type_Sql);
	}
	
	public static String getDefaultValueField(String defV,String vtype) {
		return getDefaultValue(defV,vtype,param_DefV_type_Field);
	}
	
	public static String getDefaultValue(String defV,String vtype,int Rvtype) {
		String value = "";
		if(defV.indexOf("?")==0) {
			if(param_Vtype_date.equals(vtype) || param_Vtype_datetime.equals(vtype)) {
				Date d = new Date();
				if(param_DefV_Day.equals(defV)) {
					d = new Date();
				}else if(param_DefV_LastDay.equals(defV)) {
					d = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
				}else if(param_DefV_NextDay.equals(defV)) {
					d = DateUtil.AddDate(new Date(), 1, DateUtil.AddType_intDay);
				}
				if(param_DefV_type_Sql == Rvtype) {
					defV = DateUtil.FormatOracleDate(d);
				}else {
					defV = DateUtil.FormatDate(d);
				}
			}else if(param_Vtype_varchar.equals(vtype)) {
				if(param_DefV_Day.equals(defV)) {
					defV = DateUtil.FormatDate(new Date());
				}else if(param_DefV_LastDay.equals(defV)) {
					defV = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
				}else if(param_DefV_NextDay.equals(defV)) {
					defV = DateUtil.FormatDate(DateUtil.AddDate(new Date(), 1, DateUtil.AddType_intDay));
				}
				if(param_DefV_type_Sql == Rvtype) {
					defV = "'"+defV+"'";
				}
			}else if(param_Vtype_number.equals(vtype)) {
				int nv = 0;
				if(param_DefV_Month.equals(defV)) {
					nv = DateUtil.getMonth(new Date());
				}else if(param_DefV_LastMonth.equals(defV)) {
					nv = DateUtil.getMonth(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intMonth));
				}else if(param_DefV_NextMonth.equals(defV)) {
					nv = DateUtil.getMonth(DateUtil.AddDate(new Date(),1,DateUtil.AddType_intMonth));
				}else if(param_DefV_Year.equals(defV)) {
					nv = DateUtil.getYear(new Date());
				}else if(param_DefV_LastYear.equals(defV)) {
					nv = DateUtil.getYear(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intYear));
				}else if(param_DefV_NextYear.equals(defV)) {
					nv = DateUtil.getYear(DateUtil.AddDate(new Date(),1,DateUtil.AddType_intYear));
				}else {
					if(param_DefV_type_Field == Rvtype) {
						return "ȫ��";
					}
				}
				defV = String.valueOf(nv);
			}else {
				defV = defV.replaceAll("?", "");
			}
		}else {
			if(param_DefV_type_Field == Rvtype && param_Vtype_varchar.equals(vtype)) {
				defV = defV.replaceAll("'", "");
			}
		}
		value = defV;
		return value;
	}
	

	public static String getDataSourceSql(List params,String sql) {
		for(int p=0; p< params.size(); p++) {
			String paramName = ((ZidyParam)params.get(p)).getName();
			String value = ((ZidyParam)params.get(p)).getSvalue();
			sql = sql.replaceAll(paramName, value);
		}
		return sql;
	}
	
	public static String getTypeValue(String type,String value) {
		if("DATE".equals(type.toUpperCase())) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else 
			if("DATETIME".equals(type.toUpperCase())) {
				return "to_date('"+value+"','yyyy-mm-dd hh24:mi:ss')";
			}else
				if("VARCHAR".equals(type.toUpperCase())) {
					return "'"+value+"'";
				}else
					return value;
	}
	
	public static int getChartTypeInt(String ChartTypeStr){
		if(ChartType_xy.equals(ChartTypeStr)){
			return Chart.ChartType_XYLine;
		}else
			if(ChartType_pie.equals(ChartTypeStr)){
				return Chart.ChartType_Pie;
			}else
				if(ChartType_bar.equals(ChartTypeStr)){
					return Chart.ChartType_Bar;
				}else
					if(ChartType_time.equals(ChartTypeStr)){
						return Chart.ChartType_TimeGraph;
					}
		return Chart.ChartType_XYLine;
	}
	
	public static String getReportSetId(JDBCcon con, String DesignID){
//		���ݷ���ID��ѯ�Զ��巽������ID
		String sql = "select * from zidyfapz where zidyfa_id = " + DesignID;
		ResultSetList rsl = con.getResultSetList(sql);
		String ReportSetId = "";	//����ID
		if(rsl.next()){
			ReportSetId = rsl.getString("id");
		}
		rsl.close();
		return ReportSetId;
	}
	
	public static String getDataSourcesID(JDBCcon con, String ReportSetId){
//		���ݷ���ID��ѯ�Զ��巽������ID
		String sql = "select * from zidyfapz where id = " + ReportSetId;
		ResultSetList rsl = con.getResultSetList(sql);
		String DataSourcesID = "";	//����ID
		if(rsl.next()){
			DataSourcesID = rsl.getString("zidyjcsjy_id");
		}
		rsl.close();
		return DataSourcesID;
	}
	
	
	public static String getScales(JDBCcon con, String ReportSetId, String column){
//		�õ�ĳһ�ֶε�С��λ(Ĭ��Ϊ0)
		String scales = getPzms(con,ReportSetId,column,"scales");
		return scales==null?"0":scales;
	}

	public static String getCnName(JDBCcon con, String ReportSetId, String column){
//		�õ�ĳһ�ֶε���������
		String cnname = getPzms(con,ReportSetId,column,"colname_cn");
		return cnname==null?"δ��������":cnname;
	}
	
	public static String getDataType(JDBCcon con, String ReportSetId, String column){
//		�õ�ĳһ�ֶε���������
		String datatype = getPzms(con,ReportSetId,column,"datatype");
		return datatype==null?"varchar":datatype;
	}
	
	public static String getAlign(JDBCcon con, String ReportSetId, String column){
		String align = getPzms(con,ReportSetId,column,"align");
		if(align.equals("����")){
			align = "-1";
		}else if(align.equals("����")){
			align = "1";
		}else if(align.equals("����")){
			align = "2";
		}
		return align == null?"1":align;
	}
	
	public static String getFontSize(JDBCcon con, String ReportSetId, String column){
		String fontsize = getPzms(con,ReportSetId,column,"fontsize");
		return fontsize==null?"9":fontsize;
	}
	
	public static String getPzms(JDBCcon con, String ReportSetId, String column, String code){
		String sql = "select * from zidypzms where zidyfapz_id = "+ReportSetId
		+" and z_code = '"+code+"' and z_column ='"+column+"'";
		ResultSetList rs = con.getResultSetList(sql);
		String ms = "";
		if(rs.next())
			ms = rs.getString("z_value");
		return ms;
	}
	
	public static String getGroups(JDBCcon con, String ReportSetId){
//		�������������ҳ�SQL��������
		String sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId 
		+ " and z_code = 'groupNum' order by z_value" ;
		String groups = "";	//��������
		ResultSetList rsl = con.getResultSetList(sql);
		while(rsl.next()){
			groups += "," + rsl.getString("z_column");
		}
		if(groups.length()>1){
			groups = groups.substring(1);
		}
		rsl.close();
		return groups;
	}
	
	public static String getSelects(JDBCcon con, String ReportSetId, boolean isgroup){
//		����select����ʾ�ֶ�
		String sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId
			+ " and z_code = 'showNum' order by z_value";
		String select = "";	//select�ֶ�
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.getRows() == 0){
			return select;
		}
		if(isgroup){
			while(rsl.next()){
				String oper = "";
				sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId
					+ " and z_code = 'operational' and z_column ='" + rsl.getString("z_column") 
					+ "'";
				ResultSetList rs = con.getResultSetList(sql);
				if(rs.next()){
					oper = rs.getString("z_value");
				}
				rs.close();
				if("��".equals(oper)){
					String datatype = getDataType(con,ReportSetId,rsl.getString("z_column"));
					if(datatype != null && param_Vtype_date.equals(datatype.toLowerCase())){
						select += ",to_char(" + rsl.getString("z_column") + ",'yyyy-mm-dd') "+rsl.getString("z_column");
					}else if(datatype != null && param_Vtype_datetime.equals(datatype.toLowerCase())){
						select += ",to_char(" + rsl.getString("z_column") + ",'yyyy-mm-dd hh24:mi:ss') "+rsl.getString("z_column");
					}else 
						select += "," + rsl.getString("z_column");
				}else if("sum".equals(oper)){
					select += ","+SysConstant.RoundFunction+"(sum(" + rsl.getString("z_column")+ "), "
						+ getScales(con, ReportSetId, rsl.getString("z_column")) + ")" + rsl.getString("z_column");
				}else if("avg".equals(oper)){
					select += ","+SysConstant.RoundFunction+"(avg(" + rsl.getString("z_column")+ "), " 
						+ getScales(con, ReportSetId, rsl.getString("z_column")) + ")" + rsl.getString("z_column");
				}else if("��Ȩ".equals(oper)){
					sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId
					+ " and z_code = 'weighted' and z_column ='" + rsl.getString("z_column") 
					+ "'";
					rs = con.getResultSetList(sql);
					if(rs.next()){
						select += ","+SysConstant.RoundFunction+"(decode(sum(" + rs.getString("z_value") 
							+ "),0,0,sum(" + rsl.getString("z_column") + "*" + rs.getString("z_value") 
							+ ")/sum(" + rs.getString("z_value") + ")), " 
							+ getScales(con, ReportSetId, rsl.getString("z_column")) + ")" + rsl.getString("z_column");
					}else{
//						�˴���Ӽ�Ȩ�� ȨCan not Found �Ĵ������
						return "";
					}
				}
			}
		}else
			while(rsl.next()){
				String datatype = getDataType(con,ReportSetId,rsl.getString("z_column"));
				if(datatype != null && param_Vtype_date.equals(datatype.toLowerCase())){
					select += ",to_char(" + rsl.getString("z_column") + ",'yyyy-mm-dd') "+rsl.getString("z_column");
				}else if(datatype != null && param_Vtype_datetime.equals(datatype.toLowerCase())){
					select += ",to_char(" + rsl.getString("z_column") + ",'yyyy-mm-dd hh24:mi:ss') "+rsl.getString("z_column");
				}else 
					select += "," + rsl.getString("z_column");
			}
		if(select.length()>1){
			select = select.substring(1);
		}
		rsl.close();
		return select;
	}

	public static String getWheres(JDBCcon con, String ReportSetId){
//		�õ�SQL�в�ѯ��������
		String sql = "select m.z_column,c.z_paramname from zidypzms m,zidybxcs c where m.zidyfapz_id = " + ReportSetId
			+ " and m.z_code = 'conditions' and m.z_value = ''||c.id ";
		String wheres = ""; 
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			wheres += " and " + rs.getString("z_column") 
				+ getConditionOP(con,ReportSetId,rs.getString("z_column"))
				+ " " + rs.getString("z_paramname");
		}
		if(wheres.length()>4){
			wheres = wheres.substring(4);
		}
		rs.close();
		return wheres;
	}
	
	public static String getConditionOP(JDBCcon con, String ReportSetId, String column){
//		ȡ��ĳһ�ֶε�����������
		String sql = "select z_value from zidypzms where zidyfapz_id = " + ReportSetId
			+ " and z_code = 'conditionOP' and z_column ='"+column+"'";
		String op = "=";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			op = rs.getString("z_value");
		}
		rs.close();
		return op;
	}
	
	public static String getOrders(JDBCcon con, String ReportSetId){
//		ȡ��SQL�е����򲿷�
		String sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId
		+ " and z_code = 'orderNum' order by z_value";
		String orders = "";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			orders += "," + rs.getString("z_column");
		}
		if(orders.length()>1){
			orders = orders.substring(1);
		}
		rs.close();
		return orders;
	}
	
	public static String getBasicSQL(JDBCcon con, String DataSourcesId){
//		ȡ�û�������Դ�е�SQL
		String sql = "select * from zidyjcsjysql where zidyjcsjy_id = " + DataSourcesId
			+ " order by xuh ";
		String basicSQL = "";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			basicSQL += rs.getString("z_query");
		}
		rs.close();
		return basicSQL;
	}
	
	public static String getDesignName(JDBCcon con, String DesignId){
//		ȡ���Զ��巽��������
		String sql = "select z_name from zidyfa where id = " + DesignId;
		String DesignName = "";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			DesignName = rs.getString("z_name");	
		}
		rs.close();
		return DesignName;
	}
	
	public static String SaveDataSources(JDBCcon con, String DesignId, String DataSourcesName, String DataSourcesSQL){
//		�����½���DataSources
		String DataSourcesId = MainGlobal.getNewID(100); 
		String[] query = MainGlobal.getSplitStringArray(DataSourcesSQL.replaceAll("'", "''"), 2000);
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb.append("insert into zidysjy(id,z_Name,z_Query,z_remark) values(")
		.append(DataSourcesId).append(",'").append(DataSourcesName).append("','','');\n");
		for(int i = 0; i< query.length; i++){
			sb.append("insert into zidysjysql(id,zidysjy_id,xuh,z_query) values(")
			.append("getnewId(100),").append(DataSourcesId)
			.append(",").append(i+1).append(",'").append(query[i]).append("');");
		}
		sb.append("insert into zidyfasjy(id,zidyfa_id,zidysjy_id) values(getnewid(100),")
		.append(DesignId).append(",").append(DataSourcesId).append(");\n");
		sb.append("end;");
		int result = con.getInsert(sb.toString());
		if(result!=-1){
			return DataSourcesId;
		}else{
			return "";
		}
	}
	
	public static String CreateDataSourcesSet(JDBCcon con, String DesignID){
		String ReportSetId = getReportSetId(con, DesignID);	//��������ID
		if(ReportSetId.length() == 0){
//			�˴���ӱ�������ID Can not Found �Ĵ������
			return null;
		}
		String BasicDataSourcesID = getDataSourcesID(con, ReportSetId);
		if(BasicDataSourcesID.length() == 0){
//			�˴���ӱ�������Դ Can not Found �Ĵ������
			return null;
		}
		String Groups = getGroups(con, ReportSetId);	//SQL�еķ��鲿��
		boolean isGroup = Groups.length() > 0 ;	//������Դ�Ƿ������
		String Selects = getSelects(con, ReportSetId, isGroup);	//SQL��select��ʾ�ֶ�
		if(Selects.length() == 0){
//			�˴���ӱ�����ʾ�� Can not Found �Ĵ������
			return null;
		}
		String Wheres = getWheres(con, ReportSetId);	//SQL�еĲ�ѯ��������
		String Orders = getOrders(con, ReportSetId);	//SQL�������ֶ� 
		String BasicSQL = getBasicSQL(con, BasicDataSourcesID);
		String DesignName = getDesignName(con, DesignID);
//		�����������ѯSQL
		String DataSourcesSQL = "select " + Selects + " from ("
			+ BasicSQL + ")";
//		�������
		if(Wheres.length()>0){
			DataSourcesSQL += " where " + Wheres + " ��������";
		}else{
			DataSourcesSQL += " where 1=1 ��������";
		}
//		��ӷ���
		if(isGroup){
			DataSourcesSQL += " group by rollup(" + Groups + ")";
		}
//		�������
		if(Orders.length()>0){
			DataSourcesSQL += " order by " + Orders;
		}
//		�����µ�DataSources
		String DataSourcesId = SaveDataSources(con, DesignID, DesignName, DataSourcesSQL);
		if(DataSourcesId.length()==0){
//			�˴��������ԴSQLδ�����ɹ��Ĵ������
			return null;
		}else{
			return DataSourcesId;
		}
	}
	
	public static int SaveParamSet(JDBCcon con, String DesignId, String ReportSetId){
//		�����Զ�������ֵ�
		String sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId 
		+ " and z_code = 'parameters' and z_value = '��'" ;
		ResultSetList rs = con.getResultSetList(sql);
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		while(rs.next()){
			sb.append("insert into zidycszd(id,zidyfa_id,z_alias,z_alias_cn,z_type) values(")
			.append("getnewid(100),").append(DesignId).append(",'").append(rs.getString("z_column"))
			.append("','").append(getCnName(con, ReportSetId, rs.getString("z_column"))).append("','")
			.append(getDataType(con, ReportSetId, rs.getString("z_column"))).append("');\n");
		}
		sb.append("end;");
		if(rs.getRows()>0){
			rs.close();
			return con.getUpdate(sb.toString());
		}else{
			return 0;
		}
	}
	
	public static boolean CreateParamSet(JDBCcon con, String DesignId){
		String ReportSetId = getReportSetId(con, DesignId);
		if(ReportSetId.length() == 0){
//			�˴���ӱ�������ID Can not Found �Ĵ������
			return false;
		}
		boolean issuccess = SaveParamSet(con,DesignId,ReportSetId)>-1;
		return issuccess;
	}
	

	
	public static int SaveConditions(JDBCcon con, String DesignId, String ReportSetId){
//		���汨���������
		String sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId 
		+ " and z_code = 'conditions'" ;
		ResultSetList rs = con.getResultSetList(sql);
		sql = "begin\n";
		while(rs.next()){
			sql += "insert into zidyfacsb(id,zidyfa_id,z_paramName,z_datatypes,z_prompttext,z_controltype,"
				+"z_controlwidth,zidysjy_id,z_labelfield,z_valuefield,z_defaultvalue) (select getnewid(100),"
				+ DesignId +",z_paramName,z_datatypes,z_prompttext,z_controltype,z_controlwidth,zidysjy_id,"
				+ "z_labelfield,z_valuefield,z_defaultvalue from zidybxcs where id = "+rs.getString("z_value")+");\n";
			
		}
		sql += "end;";
		if(rs.getRows()>0){
			return con.getInsert(sql);
		}else{
			return 0;
		}
		
	}
	
	public static boolean CreateConditions(JDBCcon con, String DesignId){
		String ReportSetId = getReportSetId(con, DesignId);
		if(ReportSetId.length() == 0){
//			�˴���ӱ�������ID Can not Found �Ĵ������
			return false;
		}
		boolean issuccess = SaveConditions(con,DesignId,ReportSetId)>-1;
		return issuccess;
	}
	
	public static String saveCol(JDBCcon con,String DesignId, String ReportSetId, String column, int col){
		String sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId
		+ " and (z_code = 'width' or z_code = 'merger') and z_column='"+column+"'";
		String id = MainGlobal.getNewID(100);
		String width = "80";
		String merger = "0";
		String style = "";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			if("width".equals(rs.getString("z_code"))){
				width = rs.getString("z_value");
			}else if("merger".equals(rs.getString("z_code"))){
				merger = rs.getString("z_value");
			}
		}
		sql = "insert into zidyfal(id,zidyfa_id,z_col,z_width,z_style,z_ismerger) values("
			+ id + "," + DesignId +"," + col +"," + width +",'" +style + "',"+merger+")";
		if(con.getInsert(sql)==-1){
			return null;
		}
		rs.close();
		return id;
	}
	
	public static String[] saveCols(JDBCcon con,String DesignId, String ReportSetId){
		String[] arrcol = null;
		String sql = "select * from zidypzms where zidyfapz_id = " + ReportSetId
		+ " and z_code = 'showNum' order by z_value";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.getRows() == 0){
			return arrcol;
		}
		arrcol = new String[rs.getRows()];
		while(rs.next()){
			String id = saveCol(con,DesignId,ReportSetId,rs.getString("z_column"),rs.getRow()+1);
			if(id == null){
				rs.close();
				return null;
			}
			arrcol[rs.getRow()] = id;
		}
		rs.close();
		return arrcol;
	}
	
	public static void SavePageHeader(JDBCcon con, String DesignId, String[] cols){
		String rowid = MainGlobal.getNewID(100);
		String sql = "insert into zidyfah(id,zidyfa_id,z_group,z_row,z_height,z_style) values("
			+rowid +"," +DesignId +",'ҳü',1,40,'')";
		con.getInsert(sql);
		sql = "insert into zidyfanr(id,zidyfa_id,z_alias,zidyfah_id,zidyfal_id,z_colspan,z_align,z_font_size) values("
			+rowid +"," +DesignId + ",'" +getDesignName(con, DesignId) +"'," + rowid +","
			+ cols[0] +"," +cols.length+",1,20)";
		con.getInsert(sql);
	}
	
	public static void SaveTableTitle(JDBCcon con, String DesignId, String ReportSetId, String[] cols){
		String rowid = MainGlobal.getNewID(100);
		String sql = "insert into zidyfah(id,zidyfa_id,z_group,z_row,z_height,z_style) values("
			+rowid +"," +DesignId +",'��ͷ',1,24,'')";
		con.getInsert(sql);
		sql = "select b.z_value from zidypzms a,zidypzms b where a.zidyfapz_id ="+ ReportSetId
		+" and a.zidyfapz_id = b.zidyfapz_id and a.z_column = b.z_column"
		+" and a.z_code = 'showNum' and b.z_code = 'colname_cn' order by a.z_value";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			String id = MainGlobal.getNewID(100);
			sql = "insert into zidyfanr(id,zidyfa_id,z_alias,zidyfah_id,zidyfal_id) values("
				+ id +"," + DesignId +",'"+rs.getString("z_value")+"',"+rowid+","+cols[rs.getRow()]+")";
			con.getInsert(sql);
		}
		rs.close();
	}
	
	public static void SaveTableBody(JDBCcon con, String DesignId, String ReportSetId, String DataSourcesId, String[] cols){
		String rowid = MainGlobal.getNewID(100);
		String sql = "insert into zidyfah(id,zidyfa_id,z_group,z_row,z_height,z_style) values("
			+rowid +"," +DesignId +",'����',1,24,'')";
		con.getInsert(sql);
		sql = "select * from zidypzms where zidyfapz_id = "+ReportSetId
			+" and z_code = 'showNum' order by z_value";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			String id = MainGlobal.getNewID(100);
			sql = "insert into zidyfanr(id,zidyfa_id,zidysjy_id,z_alias,zidyfah_id,zidyfal_id,z_align,z_font_size) values("
				+ id +"," + DesignId +","+DataSourcesId+",'"+rs.getString("z_column")+"',"+rowid+","+cols[rs.getRow()]+","+getAlign(con,ReportSetId,rs.getString("z_column"))+","+getFontSize(con,ReportSetId,rs.getString("z_column"))+")";
			con.getInsert(sql);
		}
		rs.close();
	}
	
	public static void CreateReportView(JDBCcon con, String DesignId, String DataSourcesId){
		String ReportSetId = getReportSetId(con, DesignId);
		if(ReportSetId.length() == 0){
//			�˴���ӱ�������ID Can not Found �Ĵ������
		}
		String[] cols = saveCols(con, DesignId, ReportSetId);
		if(cols == null){
//			�˴���ӱ�����ͼ��δ����ɹ��Ĵ�����ʾ
			return;
		}
		SavePageHeader(con,DesignId,cols);
		SaveTableTitle(con,DesignId,ReportSetId,cols);
		SaveTableBody(con,DesignId,ReportSetId,DataSourcesId,cols);
	}
	
	public static String SaveReportSet(String DesignID){
		JDBCcon con = new JDBCcon();
		DeleteReportSet(con, DesignID);
		String DataSourcesId = CreateDataSourcesSet(con, DesignID);
		if(DataSourcesId==null){
			con.Close();
			return "��������Դʧ��";
		}
		boolean paramOk = CreateParamSet(con, DesignID);
		boolean CondiOk = CreateConditions(con, DesignID);
		CreateReportView(con,DesignID,DataSourcesId);
		con.Close();
		return null;
	}
	
	public static void DeleteDataSources(JDBCcon con, String DesignId){
//		ɾ�����е�DataSources
		String sql = "begin\n";
		sql += "delete from zidysjysql where zidysjy_id in (select zidysjy_id from zidyfasjy where zidyfa_id = "+DesignId +");\n";
		sql += "delete from zidysjy where id in (select zidysjy_id from zidyfasjy where zidyfa_id = "+DesignId +");\n";
		sql += "delete from zidyfasjy where zidyfa_id ="+DesignId +";\n";
		sql += "end;";
			con.getDelete(sql);
	}
	
	public static void DeleteParamSet(JDBCcon con, String DesignId){
//		ɾ���Զ�������ֵ�
		String sql = "delete from zidycszd where zidyfa_id = " + DesignId;
		con.getDelete(sql);
	}
	
	public static void DeleteConditions(JDBCcon con, String DesignId){
//		ɾ�����б������
		String sql = "delete from zidyfacsb where zidyfa_id =" + DesignId;
		con.getDelete(sql);
	}
	
	public static void DeleteCol(JDBCcon con, String DesignId){
//		ɾ�����б�����Զ�������Ϣ
		String sql = "delete from zidyfal where zidyfa_id ="+DesignId;
		con.getDelete(sql);
	}
	
	public static void DeleteRow(JDBCcon con, String DesignId){
//		ɾ�����б�����Զ�������Ϣ
		String sql = "delete from zidyfah where zidyfa_id ="+DesignId;
		con.getDelete(sql);
	}
	
	public static void DeleteContent(JDBCcon con, String DesignId){
//		ɾ�����б�����Զ���������Ϣ
		String sql = "delete from zidyfanr where zidyfa_id ="+DesignId;
		con.getDelete(sql);
	}
	
	public static void DeleteReportSet(JDBCcon con,String DesignID){
//		ɾ������DataSources
		DeleteDataSources(con, DesignID);
//		ɾ���Զ�������ֵ�
		DeleteParamSet(con,DesignID);
//		ɾ�����б������
		DeleteConditions(con,DesignID);
//		ɾ�����б�����Զ�������Ϣ
		DeleteCol(con,DesignID);
//		ɾ�����б�����Զ�������Ϣ
		DeleteRow(con,DesignID);
//		ɾ�����б�����Զ���������Ϣ
		DeleteContent(con,DesignID);
	}
}
