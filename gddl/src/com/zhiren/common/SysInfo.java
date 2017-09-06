/*
 * ���ڣ�2012��4��25��
 * ���ߣ�Qiuzw
 * �޸����ݣ����Ӵ�jdbc���ӵĶ�ȡϵͳ�����ͻ��������ķ���
 * */

/*
 * ʱ�䣺2009-12-7 
 * ���ߣ�Qiuzw
 * �޸����ݣ������˼������������ʵķ���2��
* */

/*
 * �������� 2006-2-25
 * ���ߣ�Qiuzw
 */
/*
 * ʱ�䣺2008-03-05
 * ���ߣ�Qiuzw
 * ԭ���������˰��ա��糧���롱��ȡ���㹫ʽ�ķ��������ô˷���ǰ����Ҫ����gongsb�е�gongs�ֶ�Ϊ�糧����
 * */

package com.zhiren.common;

/*
 * ʱ�䣺2007-06-12 ���ߣ�Qiuzuwei �����������˸���ȼ��Ʒ�ַ����˵������ύʱ�ĺ˶Ա�ʶ״̬��true �� 1,false �� 0
 */

/*
 * ʱ�䣺2007-02-07 ���ߣ�Qiuzuwei �������Ż��˷���getFormula()
 */

/*
 * ʱ�䣺2007-02-06 ���ߣ�Qiuzuwei ������������public String getFormula(String leix, String
 * gongsm) {...}��������ȡ��ʽ�����ָ����ʽ
 */

/*
 * ʱ�䣺2007-01-10 ���ߣ�Qiuzuwei �������޸���getBasicValue()��������ȡ����ʱ���ֶ�������
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
         * @return ����ϵͳ��Ϣ�����int����
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
         * @return ϵͳ��Ϣ����ָ���ֶε�ֵ
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
         * @return ������Ϣ��Ϣ����ָ���ֶε�ֵ
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
         * @return �糧��Ϣ���е糧�����ֶε�ֵ����ƻ�ȫ�ƣ�
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
         * @return �糧��Ϣ����ĳ���ֶε�ֵ
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
         * @return �糧��ȱʡ��վ���ۣ�
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
         * @return ϵͳ��Ϣ����ָ���ֶε�ֵ
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
         * @param leix,��Ӧ��ʽ����ġ�leix���ֶ�
         * @param gongsm,��Ӧ��ʽ����ġ�gongsm��
         * @return ���ع�ʽ����ָ���ļ��㹫ʽ
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
         * ʱ�䣺2008-03-02 Qiuzw
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
         * ʱ�䣺2007-06-07 ���ߣ�Qizuwei ������¼���˵�ʱ��Ĭ�ϵĺ˶Ա�־
         */
    public boolean isDefaultHed(long id) {
	boolean rtn = false;
	JDBCcon con = new JDBCcon();
	String sql = "select * from fahbtmp f,ranlpzb r "
		+ "where (leix = '��' Or leix = '����') "
		+ "and f.ranlpzb_id = r.id and f.id =" + id;
	if (con.getHasIt(sql)) {
	    rtn = true;
	}
	con.Close();
	return rtn;
    }
    
    
    public static double getYunsl(){
    	//��ȡ����ú������
		double yunsl =0.0;
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select zhi from xitxxb where duixm='����������' and shifsy=1");
		ResultSet rs = con.getResultSet(sql);
		try{
			if(rs.next()){
				yunsl = rs.getDouble("zhi");
			}
			rs.close();
		}catch(Exception e){
			System.out.println("��ȡ����ú������ʧ��!");
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return yunsl;
	}
    public static double getYunsl(long meikxxb_id){
//    	������ȡ����ú������
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
    		System.out.println("��ȡ���"+meikxxb_id+"������ú������ʧ��!");
    		e.printStackTrace();
    	}finally{
    		con.Close();
    	}
    	return yunsl;
    }
    
}
