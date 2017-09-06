/*
 * 日期：2012年4月25日
 * 作者：Qiuzw
 * 修改内容：增加带jdbc连接的读取系统参数和基础参数的方法
 * */

/*
 * 时间：2009-12-7 
 * 作者：Qiuzw
 * 修改内容：增加了计算汽车运损率的方法2个
* */

/*
 * 创建日期 2006-2-25
 * 作者：Qiuzw
 */
/*
 * 时间：2008-03-05
 * 作者：Qiuzw
 * 原因：新增加了按照“电厂代码”读取计算公式的方法，调用此方法前，需要更新gongsb中的gongs字段为电厂编码
 * */

package com.zhiren.common;

/*
 * 时间：2007-06-12 作者：Qiuzuwei 描述：增加了根据燃料品种返回运单数据提交时的核对标识状态，true ＝ 1,false ＝ 0
 */

/*
 * 时间：2007-02-07 作者：Qiuzuwei 描述：优化了方法getFormula()
 */

/*
 * 时间：2007-02-06 作者：Qiuzuwei 描述：增加了public String getFormula(String leix, String
 * gongsm) {...}方法，读取公式表里的指定公式
 */

/*
 * 时间：2007-01-10 作者：Qiuzuwei 描述：修改了getBasicValue()方法，读取数据时，字段名错误
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Qiuzuwei
 * @date 2006-2-25
 */
public class SysInfo {

    /**
     * 
     */
    public SysInfo() {

	super();
    }

    /**
         * @param objName
         * @param defaultValue
         * @return 返回系统信息表里的int变量
         */
    public int getIntValue(String objName, int defaultValue) {

	int _result = defaultValue;

	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;

	String sql = "";

	sql = "SELECT zhi FROM xitxxb WHERE duixm = '" + objName
		+ "' AND  shifsy =1 ";

	rs = cn.getResultSet(sql);
	try {
	    if (rs.next()) {
		_result = rs.getInt("zhi");
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }

    /**
         * @param objName
         * @param defaultValue
         * @return 系统信息表中指定字段的值
         */
    public String getValue(String objName, String defaultValue) {

	String _result = defaultValue;

	String sql = "";

	sql = "SELECT zhi FROM xitxxb WHERE duixm = '" + objName
		+ "' AND  shifsy =1 ";
	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;

	rs = cn.getResultSet(sql);
	try {
	    if (rs.next()) {
		_result = rs.getString("zhi");
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }

    /**
         * @param objName
         * @param defaultValue
         * @return 基础信息信息表中指定字段的值
         */
    public String getBasicValue(String objName, String defaultValue) {

	String _result = defaultValue;

	String sql = "";

	sql = "SELECT mingc as zhi FROM jicxxb WHERE leix = '" + objName
		+ "' AND  zhuangt =1 ";
	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;
	rs = cn.getResultSet(sql);
	try {
	    if (rs.next()) {
		_result = rs.getString("zhi");
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }
    public String getBasicValue(String objName, String defaultValue , JDBCcon cn) {

    	String _result = defaultValue;

    	String sql = "";

    	sql = "SELECT mingc as zhi FROM jicxxb WHERE leix = '" + objName
    		+ "' AND  zhuangt =1 ";
    	ResultSet rs = null;
    	rs = cn.getResultSet(sql);
    	try {
    	    if (rs.next()) {
    		_result = rs.getString("zhi");
    	    }
    	    rs.close();
    	}
    	catch (SQLException e) {
    	    e.printStackTrace();
    	}
    	finally {
//    	    cn.Close();
    	}
    	return _result;
        }
    /**
         * @param fieldName
         * @param defaultValue
         * @return 电厂信息表中电厂名称字段的值（简称或全称）
         */
    public String getPowerName(String fieldName, String defaultValue) {

	String _result = defaultValue;

	StringBuffer sql = new StringBuffer("");
	sql.append("SELECT ");
	sql.append(fieldName);
	sql.append(" as zhi FROM diancxxb");
	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;
	rs = cn.getResultSet(sql.toString());
	try {
	    if (rs.next()) {
		_result = rs.getString("zhi");
		if (_result == null) {
		    _result = defaultValue;
		}
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }

    /**
         * @param fieldName
         * @param defaultValue
         * @return 电厂信息表中某个字段的值
         */
    public String getPowerInfo(String fieldName, String defaultValue) {

	String _result = defaultValue;

	StringBuffer sql = new StringBuffer("");
	sql.append("SELECT ");
	sql.append(fieldName);
	sql.append(" as zhi FROM diancxxb");
	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;
	rs = cn.getResultSet(sql.toString());
	try {
	    if (rs.next()) {
		_result = rs.getString("zhi");
		if (_result == null) {
		    _result = defaultValue;
		}
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }

    /**
         * @param fieldName
         * @param defaultValue
         * @return 电厂的缺省到站（港）
         */
    public String getPowerDefaultDock(String fieldName, String defaultValue) {

	String _result = defaultValue;

	StringBuffer sql = new StringBuffer("");
	sql.append("SELECT z.");
	sql.append(fieldName);
	sql
		.append(" as zhi FROM diancxxb c,chezxxb z WHERE c.chezxxb_id = z.id");
	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;
	rs = cn.getResultSet(sql.toString());
	try {
	    if (rs.next()) {
		_result = rs.getString("zhi");
		if (_result == null) {
		    _result = defaultValue;
		}
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }

    public int getBasicValue(String objName, int defaultValue) {

	int _result = defaultValue;

	String sql = "";

	sql = "SELECT mingc as zhi FROM jicxxb WHERE leix = '" + objName
		+ "' AND  zhuangt =1 ";
	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;
	rs = cn.getResultSet(sql);
	try {
	    if (rs.next()) {
		_result = rs.getInt("zhi");
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }
    public int getBasicValue(String objName, int defaultValue, JDBCcon cn) {

    	int _result = defaultValue;

    	String sql = "";

    	sql = "SELECT mingc as zhi FROM jicxxb WHERE leix = '" + objName
    		+ "' AND  zhuangt =1 ";
//    	JDBCcon cn = new JDBCcon();

    	ResultSet rs = null;
    	rs = cn.getResultSet(sql);
    	try {
    	    if (rs.next()) {
    		_result = rs.getInt("zhi");
    	    }
    	    rs.close();
    	}
    	catch (SQLException e) {
    	    e.printStackTrace();
    	}
    	finally {
//    	    cn.Close();
    	}
    	return _result;
        }
    /**
         * @param objName
         * @param defaultValue
         * @return 系统信息表中指定字段的值
         */
    public double getValue(String objName, double defaultValue) {

	double _result = defaultValue;

	String sql = "";

	sql = "SELECT zhi FROM xitxxb WHERE duixm = '" + objName
		+ "' AND  shifsy =1 ";
	JDBCcon cn = new JDBCcon();

	ResultSet rs = null;

	rs = cn.getResultSet(sql);
	try {
	    if (rs.next()) {
		_result = rs.getDouble("zhi");
	    }
	    rs.close();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	finally {
	    cn.Close();
	}
	return _result;
    }

    /**
         * @param leix,对应公式表里的“leix”字段
         * @param gongsm,对应公式表里的“gongsm”
         * @return 返回公式表中指定的计算公式
         */
    public static String getFormula(String leix, String gongsm) {
	String formula = "error";//
	JDBCcon con = new JDBCcon();
	ResultSet rs = null;
	try {
	    StringBuffer gongs = new StringBuffer("");
	    rs = con
		    .getResultSet("Select leix,gongsm,Formula From Gongsb where leix = '"
			    + leix + "' and gongsm = '" + gongsm + "'");
	    if (rs.next()) {
		if (rs.getClob("Formula").length() != 0) {
		    oracle.sql.CLOB clob = (oracle.sql.CLOB) rs
			    .getClob("Formula");
		    BufferedReader br = new BufferedReader(clob
			    .getCharacterStream());
		    try {
			String out = br.readLine();
			while (out != null) {
			    if (gongs.length() != 0) {
				gongs.append("\n");
			    }
			    gongs.append(out);
			    out = br.readLine();
			}
			br.close();
		    }
		    catch (IOException e) {
			formula = "error";
			e.printStackTrace();
		    }
		}
	    }
	    formula = gongs.toString();
	    rs.close();
	    rs = null;
	}
	catch (SQLException e) {
	    formula = "error";
	    e.printStackTrace();
	    rs = null;
	}
	finally {
	    con.Close();
	    con = null;
	}
	return formula;
    }

    public static String getFormula(String leix, String gongsm, String powercode) {
	String formula = "error";//
	JDBCcon con = new JDBCcon();
	ResultSet rs = null;
	/*
         * 时间：2008-03-02 Qiuzw
         */
	try {
	    StringBuffer gongs = new StringBuffer("");
	    rs = con
		    .getResultSet("Select leix,gongsm,Formula From Gongsb where leix = '"
			    + leix
			    + "' and gongsm = '"
			    + gongsm
			    + "' and gongs = '" + powercode + "'");
	    if (rs.next()) {
		if (rs.getClob("Formula").length() != 0) {
		    oracle.sql.CLOB clob = (oracle.sql.CLOB) rs
			    .getClob("Formula");
		    BufferedReader br = new BufferedReader(clob
			    .getCharacterStream());
		    try {
			String out = br.readLine();
			while (out != null) {
			    if (gongs.length() != 0) {
				gongs.append("\n");
			    }
			    gongs.append(out);
			    out = br.readLine();
			}
			br.close();
		    }
		    catch (IOException e) {
			formula = "error";
			e.printStackTrace();
		    }
		}
	    }
	    else {
		formula = getFormula(leix, gongsm);
	    }
	    formula = gongs.toString();
	    rs.close();
	    rs = null;
	}
	catch (SQLException e) {
	    formula = "error";
	    e.printStackTrace();
	    rs = null;
	}
	finally {
	    con.Close();
	    con = null;
	}
	return formula;
    }

    /**
         * 时间：2007-06-07 作者：Qizuwei 描述：录入运单时，默认的核对标志
         */
    public boolean isDefaultHed(long id) {
	boolean rtn = false;
	JDBCcon con = new JDBCcon();
	String sql = "select * from fahbtmp f,ranlpzb r "
		+ "where (leix = '油' Or leix = '其它') "
		+ "and f.ranlpzb_id = r.id and f.id =" + id;
	if (con.getHasIt(sql)) {
	    rtn = true;
	}
	con.Close();
	return rtn;
    }
    
    
    public static double getYunsl(){
    	//获取汽车煤运损率
		double yunsl =0.0;
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select zhi from xitxxb where duixm='汽运运损率' and shifsy=1");
		ResultSet rs = con.getResultSet(sql);
		try{
			if(rs.next()){
				yunsl = rs.getDouble("zhi");
			}
			rs.close();
		}catch(Exception e){
			System.out.println("获取汽车煤运损率失败!");
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return yunsl;
	}
    public static double getYunsl(long meikxxb_id){
//    	按矿别获取汽车煤运损率
    	double yunsl =0.0;
    	JDBCcon con = new JDBCcon();
    	StringBuffer sql = new StringBuffer();
    	sql.append("Select nvl(k.Qicysl,0) As zhi From meikxxb k Where k.Id  = " + meikxxb_id);
    	ResultSet rs = con.getResultSet(sql);
    	try{
    		if(rs.next()){
    			yunsl = rs.getDouble("zhi");
    		}
    		rs.close();
    	}catch(Exception e){
    		System.out.println("获取矿别"+meikxxb_id+"的汽车煤运损率失败!");
    		e.printStackTrace();
    	}finally{
    		con.Close();
    	}
    	return yunsl;
    }
    
}
