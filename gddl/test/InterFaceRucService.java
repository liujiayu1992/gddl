/*
 *日期：2016年5月18日
 *作者：Qiuzw
 *描述：接收化验报告中的中的“干燥基高位热值”指标值
 */
 
/*
 *日期：2015年1月1日
 *作者：户丹阳
 *描述：临时解决鸭溪问题，修改毛重、票重 
 */

/*
 * 日期：2014年12月17日
 * 作者: 任海
 * 描述：临时解决鸭溪问题，增加拆车功能
 */

/*
 * 日期：2014年12月7日
 * 作者: 任海
 * 描述：解决新乡问题，增加双采制化解决方案
 */

/*
 * 日期：2014年11月18日
 * 作者: 任海
 * 描述：解决大别山问题
 */

import java.io.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.tools.RandomGUID;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;



public class InterFaceRucService {

    /**
     * 999
     *
     * @param args
     */
    private static final String ERRMSG000 = "接收成功";
    private static final String ERRMSG001 = "XMLData数据转换为gb312编码时出错";
    private static final String ERRMSG002 = "XML格式解析错误";
    private static final String ERRMSG003 = "账号信息错误";
    private static final String ERRMSG004 = "无效的SQL语句";
    private static final String ERRMSG005 = "燃料系统未编码";

    private static final String ERRMSG104 = "未知错误";
    private static final String ERRCODE000 = "1000";
    private static final String ERRCODE001 = "-1001";
    private static final String ERRCODE002 = "-1002";
    private static final String ERRCODE003 = "-1003";
    private static final String ERRCODE004 = "-1004";
    private static final String ERRCODE005 = "-1005";
    private static final String ERRCODE104 = "-1999";
    private static final String ERRCODE105 = "系统中没有正式样，请先传入正式样";

//    private static String DBDriver = "oracle.jdbc.driver.OracleDriver";
//
//    private static String ConnStr = "jdbc:oracle:thin:@localhost:1521:orcl";// jdbc:oracle:thin:@localhost:1521:rmis
//
//    private static String UserName = "linh";//"zgdtrmis";//zgdt"
//
//    private static String UserPassword = "a";//"rgzx123";//z'gdt"
//
//    private static String JNDIName = "oracle";//jdbc/Oracle
    public InterFaceRucService() {
        super();

    }
    public static String getPathName(String dcId,String bianm) throws Exception {
        JDBCcon cn = new JDBCcon();
        String bakFilePath = "E:/xmlbak";
        RandomGUID guid = new RandomGUID();
        String MD5 = guid.valueAfterMD5;
        File file = new File(bakFilePath);
        String path=file.getAbsolutePath() + "/"
                + bianm + "-" + dcId + "-" + MD5 + ".xml";
        cn.Close();
        return  path;
    }
    public byte[] syncdata(String user, String password, byte[] XMLData, String type) {
        //---------------------------------输出传入参数-------------------------------------------------------------------
        BufferedOutputStream bos;
        try {
            String pathname=getPathName("197",type);
            bos = new BufferedOutputStream(new FileOutputStream(new File(pathname)));
            bos.write(XMLData);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //type，表示要同步的数据类型
        // 数据同步（修改），每次仅接受一条修改记录
        String strTransCode = "xxx";
        String strSyncFileName = "sync" + type + "data";
        byte[] b =  null;
        /* 验证用户 */
//        if (!ValidateUser(user, password)) {
//            // 非法账号
//            b = bulidXMLData(ERRCODE003, ERRMSG003, strTransCode, strSyncFileName);
//            return b;
//        }
        if (XMLData != null) {
//            JDBCcon cn = new JDBCcon( 0,  JNDIName,  ConnStr, UserName,  UserPassword);
            JDBCcon cn = new JDBCcon();
            cn.setAutoCommit(false);
            try {
                String res = new String(XMLData, "GB2312");// xml字符集转换,异常UnsupportedEncodingException
                // 备份用户申请的同步数据 2011年8月26日
//                bulidXMLData(strSyncFileName, res);
                // 解析xml
                SAXBuilder builder = new SAXBuilder();
                StringReader sr = new StringReader(res);
                InputSource is = new InputSource(sr);
                Document doc = builder.build(is);// 解读每一个数据帧文件，异常IOException
                Element root = doc.getRootElement();// 得到xml文件的根节点
                List lstDataRecords = root.getChildren();// 得到”化验“的集合
                int intChildrenLen = lstDataRecords.size();// 得到”化验“的长度
                StringBuffer sqls = new StringBuffer("");
                if (type.equalsIgnoreCase("CZSJ")) {
                    for (int i = 0; i < intChildrenLen; i++) {
                        Element e1 = (Element) lstDataRecords.get(i);
                        //验收系统往燃料系统传数据
                            this.saveChepbtmp(e1, cn);
                            cn.commit();
                    }
                } else {
                    for (int i = 0; i < intChildrenLen; i++) {
//                        try {
                            Element e1 = (Element) lstDataRecords.get(i);
                            this.saveZhillsb(e1, cn);
                            cn.commit();
                    }
                }

                b = bulidXMLData(ERRMSG000, ERRMSG000, strTransCode, strSyncFileName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                b = bulidXMLData(ERRCODE001, ERRMSG001, strTransCode, "sync");
            } catch (JDOMException e) {
                e.printStackTrace();
                b = bulidXMLData(ERRCODE002, ERRMSG002, strTransCode, "sync");
            } catch (IOException e) {
                e.printStackTrace();
                b = bulidXMLData(ERRCODE104, ERRMSG104, strTransCode, "sync");
            }catch (Exception e){
                e.printStackTrace();
                b = bulidXMLData(ERRCODE104, e.getMessage(), strTransCode, "sync");
            }finally {
                cn.rollBack();
                cn.Close();
            }
            cn.Close();
        }
        return b;
    }
    private String getZhilbid(String chec,JDBCcon cn) throws SQLException {
        String zhilb_id="0";
        String sql="select zhilb_id from zhillsb where id=(select max(zhillsb_id) from zhuanmb where bianm='"+chec+"' and zhuanmlb_id=100661)";
        ResultSet rs=cn.getResultSet(sql);
        if(rs.next()){
            zhilb_id=rs.getString("zhilb_id");
        }
        return zhilb_id;
    }
    private void saveChepbtmp(Element e, JDBCcon con) throws Exception {
        // 同步计量数据
//        StringBuffer sql = new StringBuffer();
//        String strChex = e.getChildText("车序");
        String cheph = e.getChildText("车皮号");// 车皮号
        String maoz = e.getChildText("毛重");// 毛重
        String piz = e.getChildText("皮重");// 皮重
        String biaoz = e.getChildText("标重");// 票重
//        String strJingz = e.getChildText("净重");// 净重,
        String yingd = e.getChildText("盈吨");// 盈吨
        String yuns = e.getChildText("运损");// 运损
        String koud = e.getChildText("扣吨");// 扣吨
//        double piz2=Double.parseDouble(piz)-Double.parseDouble(koud);
        String gongysmc = e.getChildText("发货单位");// 发货单位，MEIKXXB中的“煤矿单位名称”，通过它得到MEIKDQB中的ID
//        strFahdw = getTableID("MEIKDWQC", "MEIKXXB", strFahdw, con, "MEIKDQB_ID");
        String meikdwmc = e.getChildText("煤矿单位");// 煤矿单位\

        String fahrq = e.getChildText("发货日期");// 发货日期，暂时没有用到
        String daohrq = e.getChildText("到货日期");// 到货日期，暂时没有用到
        String pinz = e.getChildText("煤种");// 煤种
//            System.out.println("车牌号"+strCheph+" 煤种:"+strMeiz+"]");
//		strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con,"ID");
        String faz = e.getChildText("发站");// 发站
//        strFaz = getTableID("JIANC", "CHEZXXB", strFaz, con, "ID");
        String yunsdw = e.getChildText("运输单位");// 运输单位
//2015年6月29日 qzw 从“煤矿信息”中读取煤矿的默认口径
        String jihkj = e.getChildText("计划口径");// 计划口径
//		strJihkj = getTableID("MINGC", "JIHKJB", strJihkj, con,"ID");
//		String strJihkj = getTableID("ID", "MEIKXXB", strMeikdw, con,"JIHKJB_ID");
//        String strCheb = e.getChildText("车别");// 车别，暂时没有用到
//        String strJilfs = e.getChildText("计量方式");// 计量方式，暂时没有用到
        String yunsfs = e.getChildText("运输方式");// 运输方式，暂时没有用到
//        String strPiaojh = e.getChildText("票据号");// 票据号，暂时没有用到
        String zhongcsj = e.getChildText("毛重时间");// 毛重时间
        String qingcsj = e.getChildText("皮重时间");// 皮重时间
        String meigy = e.getChildText("毛重检斤员");// 毛重检斤员
        String qingcjjy = e.getChildText("皮重检斤员");// 皮重检斤员
        String strMeic = e.getChildText("煤场");// 煤场
//        strMeic = getTableID("MEICMC", "MEICFQB", strMeic, con, "ID");
        String zhongchh = e.getChildText("重车衡编号");// 重车衡器编号
        String qingchh = "";//e.getChildText("轻车衡编号");// 轻车衡器编号
        String strShouhr = e.getChildText("收货人");// 收货人
        String beiz = e.getChildText("备注");// 备注
        String piaojh = e.getChildText("车辆标识号");
        String chec = e.getChildText("samcode");
        String zhilb_id=this.getZhilbid(chec,con);
        String sql="select * from chepbtmp where piaojh='"+piaojh+"'";
        ResultSet rs=con.getResultSet(sql);
        if(rs.next()){
            int fahb_id=0;
            fahb_id=rs.getInt("fahb_id");
            String id=rs.getString("id");
            if(fahb_id!=0){
                throw new Exception("燃料系统已经导入!");
            }
            sql="update chepbtmp set\n " +
                    "cheph='"+cheph+"'"+
                    ",caiybh='"+chec+"'"+
                    ",meikdwmc='"+meikdwmc+"'"+
                    ",yunsfs='"+yunsfs+"'"+
                    ",pinz='"+pinz+"'"+
                    ",maoz='"+maoz+"'"+
                    ",piz='"+piz+"'"+
                    ",biaoz='"+biaoz+"'"+
                    ",koud='"+koud+"'"+
                    ",yuns='"+yuns+"'"+
                    ",yingd='"+yingd+"'"+
                    ",jihkj='"+jihkj+"'"+
                    ",meigy='"+meigy+"'"+
                    ",yunsdw='"+yunsdw+"'"+
                    ",qingcjjy='"+qingcjjy+"'"+

                    ",zhongchh='"+zhongchh+"'"+
                    ",qingchh='"+4+"'"+
                    ",piaojh='"+piaojh+"'"+
                    ",diancxxb_id=197"+
                    ",fahrq=to_date(substr('"+fahrq+"',0,10),'yyyy-MM-dd')"+
                    ",daohrq=to_date(substr('"+daohrq+"',0,10),'yyyy-MM-dd')"+
                    ",chec='"+chec+"'"+
                    ",zhilb_id='"+zhilb_id+"'"+
                    ",faz='"+faz+"'"+
                    ",zhongcsj=to_date('"+zhongcsj+"','yyyy-MM-dd hh24:mi:ss')"+
                    ",qingcsj=to_date('"+qingcsj+"','yyyy-MM-dd hh24:mi:ss')"+
                    ",gongysmc='"+gongysmc+"'"+
                    ",yuanmkdw='"+meikdwmc+"'"+
                    ",beiz='"+chec+"'"+
                    ",yuanmz='"+0+"'"+
                    "where id="+id;
        }else {
            sql="insert into CHEPBTMP\n"+
            "(id, CHEPH, meikdwmc, yunsfs, pinz, MAOZ, biaoz,PIZ, KOUD, YUNS, YINGD, jihkj, MEIGY, yunsdw, qingcjjy," +
                    " zhongchh, qingchh, piaojh,  DIANCXXB_ID, fahrq, daohrq,zhongcsj,qingcsj, chec, faz,lury,caiyrq," +
                    "gongysmc,daoz,yuandz,yuanshdw,fahb_id,zhilb_id,caiybh,yuanmkdw,beiz,yuanmz) VALUES (\n"+
                    "getnewid(197),"+
            "'"+cheph+"',"+
            "'"+meikdwmc+"',"+
            "'"+yunsfs+"',"+
            "'"+pinz+"',"+
            "'"+maoz+"',"+
            "'"+biaoz+"',"+
            "'"+piz+"',"+
            "'"+koud+"',"+
            "'"+yuns+"',"+
            "'"+yingd+"',"+
            "'"+jihkj+"',"+
            "'"+meigy+"',"+
            "'"+yunsdw+"',"+
            "'"+qingcjjy+"',"+

            "'"+zhongchh+"',"+
            "'"+4+"',"+
            "'"+piaojh+"',"+
            "197,"+
            "to_date(substr('"+fahrq+"',0,10),'yyyy-MM-dd'),"+
            "to_date(substr('"+daohrq+"',0,10),'yyyy-MM-dd'),"+
            "to_date('"+zhongcsj+"','yyyy-MM-dd hh24:mi:ss'),"+
            "to_date('"+qingcsj+"','yyyy-MM-dd hh24:mi:ss'),"+
            "'"+chec+"',"+
            "'"+faz+"','系统导入',trunc(sysdate),'"+gongysmc+"','汽','汽','临河发电',0,'"+zhilb_id+"','"+chec+"','"+meikdwmc+"','"+chec+"',0)";
        }
        System.out.println(sql);
        int r = con.getUpdate(sql);
        if (r == -1) {
            throw new Exception(sql + "\n执行错误!");
        }
    }

    public static String getNewId(JDBCcon cn) {
        String id = null;
        String sql = "select  getnewid(197) id from dual";
        ResultSet rs = cn.getResultSet(sql);
        try {
            if (rs.next()) {
                id = rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void saveBianm(JDBCcon cn, String zhillsb_id, String bianm, String zhuanmlb_id) throws SQLException {
        String sql = "select id from zhuanmb where zhillsb_id=" + zhillsb_id + " and zhuanmlb_id=" + zhuanmlb_id;
        ResultSet rs = cn.getResultSet(sql);
        if (rs.next()) {
            sql = "update zhuanmb set bianm='" + bianm + "' where id=" + rs.getString("id");
        } else {
            sql = "insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id) values(" + getNewId(cn) + "," + zhillsb_id + ",'" + bianm + "'," + zhuanmlb_id + ")";
        }
        rs.close();
        cn.getUpdate(sql);
    }

    //传入为Rg（下样），插入zhilbtmp
    private void saveZhillsb(Element e2, JDBCcon cn) throws Exception {
        String caiybm = e2.getChildText("samcode");
        if(caiybm==null){
            throw new Exception("采样编码不存在!");
        }
        String sql="select id from chepbtmp where chec='"+caiybm+"'";
        ResultSet crs=cn.getResultSet(sql);
        if(!crs.next()){
            throw new Exception("请先上传车次号为"+caiybm+"的验收数量!");
        }
        crs.close();
        // 同步化验数据
//		String strDiancmc = e2.getChildText("电厂名称");
        String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");// 标识要进行的操作。0
        // 表示覆盖或增加对应的数据，1表示删除
        String strLury1 = e2.getChildText("Lury1");// 票据号
        String strCaiyry = e2.getChildText("Caiyry");// 车皮号
        String strCaiyfs = e2.getChildText("Caiyfs");// 煤矿编码
        String strHuaysj = e2.getChildText("Huaysj");// 仓库编码,
        String strQnetar = e2.getChildText("Qnetar");// 自编号，ERP系统形成，燃料系统中不使用
        String strAar = e2.getChildText("Aar");// 燃料品种名称
        String strAd = e2.getChildText("Ad");// 到货日期，燃料系统中采用chepb.zhongcsj
        String strVdaf = e2.getChildText("Vdaf");// 进厂称重时间，chepb.zhongcsj
        String strMt = e2.getChildText("Mt");// 离厂称重时间，chepb.qingcsj
        // double dblZK = noDoubleNullVal(e.getChildText("厂别ID"));// 扣吨
        String strStad = e2.getChildText("Stad");// 毛重
        String strAad = e2.getChildText("Aad");// 皮重
        String strMad = e2.getChildText("Mad");// 扣吨
        String strQbad = e2.getChildText("Qbad");// 备注
        String strHad = e2.getChildText("Had");// zihilb_id
        String strVad = e2.getChildText("Vad");// 采制化批次的年度，与燃料系统中的采样时间对应
        String strStd = e2.getChildText("Std");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strQgrad = e2.getChildText("Qgrad");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strHuayy = e2.getChildText("Huayy");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strLury2 = e2.getChildText("Lury2");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShenhzt = "1";//e2.getChildText("Shenhzt");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShenhry = e2.getChildText("Shenhry");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strHuaybh = e2.getChildText("Huaybh");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strFcad = e2.getChildText("Fcad");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strHdaf = e2.getChildText("Hdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strQgrdaf = e2.getChildText("Qgrdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShenhrq = e2.getChildText("Shenhrq");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshry = e2.getChildText("Shangjshry");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshrq = e2.getChildText("Shangjshrq");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strSdaf = e2.getChildText("Sdaf");// 采制化批次的月度，与燃料系统中的采样时间对应

        String Qgrd = e2.getChildText("Qgrd");
        String strQgrd = e2.getChildText("Qgrd");// 2016年5月18日 Qiuzw 读取接口文件中的“干燥基高位热值”
        //--------------------------------------------------------------------------------------------------------------
        sql=" select distinct z.id zhillsb_id,z.zhilb_id from fahb f inner join zhillsb z on f.zhilb_id=z.zhilb_id where f.chec='"+caiybm+"'";
        ResultSet rs = cn.getResultSet(sql);
        String zhillsb_id;
        if (rs.next()) {
//            String id = rs.getString("id");
            zhillsb_id = rs.getString("zhillsb_id");
            //更新zhuanmb
            //更新采用编码100661
            saveBianm(cn, zhillsb_id, caiybm, "100661");
            //更新制样编码100662
            saveBianm(cn, zhillsb_id, strBianh, "100662");
            //更新化验编码100663
            saveBianm(cn, zhillsb_id, strHuaybh, "100663");
            //更新质量
            sql = " update zhillsb set "
                    + "huaysj = to_date('" + strHuaysj + "', 'YYYY-MM-DD HH24:MI:SS'),  "
                    + "qnet_ar = round_new(" + strQnetar + ",2),\n"
                    + "aar = " + strAar + ",\n"
                    + "ad = " + strAd + ",\n"
                    + "vdaf = " + strVdaf + ",\n"
                    + "mt = round_new(" + strMt + ",1),\n"
                    + "stad = " + strStad + ",\n"
                    + "aad = " + strAad + ",\n"
                    + "mad = " + strMad + ",\n"
                    + "qbad = " + strQbad + ",\n"
                    + "had = " + strHad + ",\n"
                    + "vad = " + strVad + ",\n"
                    + "fcad = " + strFcad + ",\n"
                    + "std = " + strStd + ",\n"
                    + "qgrad = round_new(" + strQgrad + ",3),\n"
                    + "hdaf = " + strHdaf + ",\n"
                    + "qgrad_daf = " + strQgrdaf + ",\n"
                    + "sdaf = " + strSdaf + ",\n"
                    // + "har = " + elmt.getText() + ",\n"
                    + "qgrd = round_new(" + Qgrd + ",3),\n"
                    + " lury = '" + strLury1 + "',huayy = '" + strHuayy + "',shenhzt=5  " +
                    "where id = " + zhillsb_id+"\n";
            System.out.println(sql);
            int r = cn.getUpdate(sql);
            if (r == -1) {
                throw new Exception(sql + "\n执行错误!");
            }
        }else{
            throw new Exception("请先导入数量!");
        }

    }
    private void saveCaiyb(JDBCcon cn,String zhillsb_id,String caiybm,String strCaiysj) throws SQLException {
        String sql = "select id from caiyb where zhilb_id=" + zhillsb_id + " and bianm='" + caiybm+"'";
        ResultSet rs = cn.getResultSet(sql);
        if (!rs.next()) {
            sql = "insert into caiyb (id,zhilb_id,bianm,caiyrq,xuh) values(" + getNewId(cn) + "," + zhillsb_id + ",'" +
                    caiybm + "',to_date(substr('"+strCaiysj+"',0,10),'yyyy-mm-dd'),(select nvl(max(xuh),0)+1 from caiyb))";
        }
        rs.close();
        cn.getUpdate(sql);
    }
    private static String getTableID(String wherefieldname, String tablename,
                                     String fieldValue, JDBCcon con, String getfieldname) {
        ResultSet rs = con.getResultSet("SELECT " + getfieldname + " as id FROM " + tablename
                + " WHERE " + wherefieldname + " = '" + fieldValue + "' ");
        try {
            if (rs.next()) {
                // 外部用户验证通过
                return rs.getString("id");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        } finally {
            //	con.Close();   //20161125注释，其他地方还要用不能关闭
        }

        return "-1";// 用户验证不通过
    }


    private static String syncCZSJ(Element e, JDBCcon con) {
        // 同步计量数据
        StringBuffer sql = new StringBuffer();
        String strChex = e.getChildText("车序");
        String strCheph = e.getChildText("车皮号");// 车皮号
        String strMaoz = e.getChildText("毛重");// 毛重
        String strPiz = e.getChildText("皮重");// 皮重
        String strPiaoz = e.getChildText("标重");// 票重
        String strJingz = e.getChildText("净重");// 净重,
        String strYingd = e.getChildText("盈吨");// 盈吨
        String strYuns = e.getChildText("运损");// 运损
        String strKoud = e.getChildText("扣吨");// 扣吨
        String strKuid = e.getChildText("亏吨");// 亏吨
        String strChes = e.getChildText("车速");// 车速
        String strChec = e.getChildText("车次");// 车次
        String strDiancmc = e.getChildText("电厂名称");// 电厂名称，DIANCXXB中的“简称”
        strDiancmc = getTableID("JIANC", "DIANCXXB", strDiancmc, con, "ID");
        String strChangb = e.getChildText("厂别ID");// 厂别
        String strFahdw = e.getChildText("发货单位");// 发货单位，MEIKXXB中的“煤矿单位名称”，通过它得到MEIKDQB中的ID
        strFahdw = getTableID("MEIKDWQC", "MEIKXXB", strFahdw, con, "MEIKDQB_ID");
        String strMeikdw = e.getChildText("煤矿单位");// 煤矿单位\
        //System.out.println("strMeikdw111 ============ " + strMeikdw);
//		strMeikdw = getTableID("MEIKDWQC", "MEIKXXB", strMeikdw, con,"ID");
        double yunsl = getYunsl(strMeikdw);
        //2015年6月27日 qzw 重新计算 盈、亏、损
        //System.out.println("strMeikdw ============ " + strMeikdw);
        if (yunsl != 0) {
            double yingd = 0.0;
            double kuid = 0.0;
            double yuns = 0.0;
            double maoz = 0.0;
            double piz = 0.0;
            double biaoz = 0.0;
            maoz = Double.parseDouble(strMaoz);
            piz = Double.parseDouble(strPiz);
            biaoz = Double.parseDouble(strPiaoz);
            if (maoz != 0 && piz != 0 && biaoz != 0) {
                double yingk = maoz - piz - biaoz;
                yuns = CustomMaths.Round_new(yunsl * biaoz, 2);
                if (yingk >= 0) {
                    yingd = CustomMaths.Round_new(yingk, 2);
                    yuns = 0;
                } else if (Math.abs(yingk) <= yuns) {
                    yuns = CustomMaths.Round_new(Math.abs(yingk), 2);
                } else {
                    kuid = CustomMaths.Round_new(Math.abs(yingk) - yuns, 2);
                }
            }
            strYingd = yingd + "";
            strKuid = kuid + "";
            strYuns = yuns + "";
        }
        String strFahrq = e.getChildText("发货日期");// 发货日期，暂时没有用到
        String strDaohrq = e.getChildText("到货日期");// 到货日期，暂时没有用到
        String strMeiz = e.getChildText("煤种");// 煤种
//            System.out.println("车牌号"+strCheph+" 煤种:"+strMeiz+"]");
//		strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con,"ID");
        String strFaz = e.getChildText("发站");// 发站
        strFaz = getTableID("JIANC", "CHEZXXB", strFaz, con, "ID");
        String strYunsdw = e.getChildText("运输单位");// 运输单位
//2015年6月29日 qzw 从“煤矿信息”中读取煤矿的默认口径
        String strJihkj = e.getChildText("计划口径");// 计划口径
//		strJihkj = getTableID("MINGC", "JIHKJB", strJihkj, con,"ID");
//		String strJihkj = getTableID("ID", "MEIKXXB", strMeikdw, con,"JIHKJB_ID");
        String strCheb = e.getChildText("车别");// 车别，暂时没有用到
        String strJilfs = e.getChildText("计量方式");// 计量方式，暂时没有用到
        String strYunsfs = e.getChildText("运输方式");// 运输方式，暂时没有用到
        String strPiaojh = e.getChildText("票据号");// 票据号，暂时没有用到
        String strMaozsj = e.getChildText("毛重时间");// 毛重时间
        String strPizsj = e.getChildText("皮重时间");// 皮重时间
        String strMaozjjy = e.getChildText("毛重检斤员");// 毛重检斤员
        String strPizjjy = e.getChildText("皮重检斤员");// 皮重检斤员
        String strMeic = e.getChildText("煤场");// 煤场
        strMeic = getTableID("MEICMC", "MEICFQB", strMeic, con, "ID");
        String strZhongchbh = e.getChildText("重车衡编号");// 重车衡器编号
        String strQingchbh = "";//e.getChildText("轻车衡编号");// 轻车衡器编号
        String strShouhr = e.getChildText("收货人");// 收货人
        String strBeiz = e.getChildText("备注");// 备注
        String piaojh = e.getChildText("车辆标识号");
        String chec = e.getChildText("samcode");
        sql.append("DELETE FROM chepbtmp WHERE piaojh = " + piaojh + ";\n");//删除已经发过的车皮
        sql.append("INSERT INTO chepbtmp");
        sql.append("(ID, CHEPH,  meikdwmc, pinz, ");
        sql.append("MAOZ, PIZ, KOUD, YUNS,  YINGD, ZHUANGT, ");
        sql.append("jihkj, MEIGY, yunsdw, qingcjjy, BEIZ, MEICB_ID,zhongchh,qingchh,piaojh,GUOHSJ,DIANCXXB_ID,fahrq,chec)");
        sql.append(" VALUES(");
        sql.append("getnewid(197),"); // id
        sql.append("'" + strCheph + "',"); // 车皮号
//		sql.append("'" + strShouhr + "',"); // 收货人
//		sql.append(strFahdw + ","); // 发货单位
        sql.append(strMeikdw + ","); // 煤矿单位
        sql.append(strMeiz + ","); // 燃料品种
//		sql.append(strPiaoz + ","); // 发货量
        sql.append(strMaoz + ","); // 毛重=毛重-扣吨
        sql.append(strPiz + ","); // 皮重
        sql.append(strKoud + ","); // 扣吨
        sql.append(strYuns + ","); // 运损
//		sql.append(strKuid + ","); // 亏吨
//		sql.append("TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS'),"); // 检毛时间
//		sql.append("TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS'),"); // 检皮时间
        sql.append(strYingd + ","); // 盈吨
        sql.append(0 + ","); // 状态
        sql.append(strJihkj + ","); // 计划口径
        sql.append("'" + strMaozjjy + "',"); // 煤管员
        sql.append("'" + strYunsdw + "',");// 承运单位
        sql.append("'" + strPizjjy + "',");// 检斤员
        sql.append("'" + strBeiz + "',"); // 备注
        sql.append(strMeic + ","); // 煤场
//		sql.append(strChex + ","); // 序号
//		sql.append(strChangb + ","); // 厂别
//		sql.append(-1); // 盘点煤场
        sql.append(",'" + strZhongchbh + "'");
        //2015年6月27日 qzw qichjjbtmp.jingz是作为判断“发货量”是否人工输入的标记使用，不代表真实的“净重”
        sql.append(",'" + strQingchbh + "'," + piaojh + ")\n");
        sql.append(",TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS')");
        sql.append(",197,TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS')," + chec + ");\n");
//		System.out.println("huaboyang456");
        return sql.toString();
    }

    private static String syncCZSJ_YX(Element e, JDBCcon con) {

        // 同步计量数据
        StringBuffer sql = new StringBuffer();
        String strChex = e.getChildText("车序");
        String strCheph = e.getChildText("车皮号");// 车皮号
        String strMaoz = e.getChildText("毛重");// 毛重
        String strPiz = e.getChildText("皮重");// 皮重
        String strPiaoz = e.getChildText("标重");// 票重
        String strJingz = e.getChildText("净重");// 净重,
        String strYingd = e.getChildText("盈吨");// 盈吨
        String strYuns = e.getChildText("运损");// 运损
        String strKoud = e.getChildText("扣吨");// 扣吨
        String strKuid = e.getChildText("亏吨");// 亏吨
        String strChes = e.getChildText("车速");// 车速
        String strChec = e.getChildText("车次");// 车次
        String strDiancmc = e.getChildText("电厂名称");// 电厂名称，DIANCXXB中的“简称”
        strDiancmc = getTableID("JIANC", "DIANCXXB", strDiancmc, con, "ID");
        String strChangb = e.getChildText("厂别ID");// 厂别
        String strFahdw = e.getChildText("发货单位");// 发货单位，MEIKXXB中的“煤矿单位名称”，通过它得到MEIKDQB中的ID
        strFahdw = getTableID("MEIKDWQC", "MEIKXXB", strFahdw, con, "MEIKDQB_ID");
        String strMeikdw = e.getChildText("煤矿单位");// 煤矿单位
        strMeikdw = getTableID("MEIKDWQC", "MEIKXXB", strMeikdw, con, "ID");
        String strFahrq = e.getChildText("发货日期");// 发货日期，暂时没有用到
        String strDaohrq = e.getChildText("到货日期");// 到货日期，暂时没有用到
        String strMeiz = e.getChildText("煤种");// 煤种
//		strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con,"ID");
        String strFaz = e.getChildText("发站");// 发站
        strFaz = getTableID("JIANC", "CHEZXXB", strFaz, con, "ID");
        String strYunsdw = e.getChildText("运输单位");// 运输单位
        String strJihkj = e.getChildText("计划口径");// 计划口径
        strJihkj = getTableID("MINGC", "JIHKJB", strJihkj, con, "ID");
        String strCheb = e.getChildText("车别");// 车别，暂时没有用到
        String strJilfs = e.getChildText("计量方式");// 计量方式，暂时没有用到
        String strYunsfs = e.getChildText("运输方式");// 运输方式，暂时没有用到
        String strPiaojh = e.getChildText("票据号");// 票据号，暂时没有用到
        String strMaozsj = e.getChildText("毛重时间");// 毛重时间
        String strPizsj = e.getChildText("皮重时间");// 皮重时间
        String strMaozjjy = e.getChildText("毛重检斤员");// 毛重检斤员
        String strPizjjy = e.getChildText("皮重检斤员");// 皮重检斤员
        String strMeic = e.getChildText("煤场");// 煤场
        strMeic = getTableID("MEICMC", "MEICFQB", strMeic, con, "ID");
        String strZhongchbh = e.getChildText("重车衡编号");// 重车衡器编号
        //在燃料系统采样编码生成时是根据轻车衡号判定其运输方式，如果有轻车衡号则运输方式为轻车衡号，如果没有运输方式则为汽运
        //根据鸭溪目前状态，临时解决方案是将轻车衡号置空
        String strQingchbh = "";//e.getChildText("轻车衡编号");// 轻车衡器编号
        String strShouhr = e.getChildText("收货人");// 收货人
        String strBeiz = e.getChildText("备注");// 备注
        String strZhefl = e.getChildText("折粉率");// 折粉率
//		System.out.println("strZhefl = " + strZhefl);
//		System.out.println("strPiz = " + strPiz);
//		System.out.println("strPiaoz = " + strPiaoz);
//		System.out.println("strMaoz = " + strMaoz);
//		System.out.println("strJingz = " + strJingz);
//		System.out.println("strYingd = " + strYingd);
//		System.out.println("strKoud = " + strKoud);
//		System.out.println("strKuid = " + strKuid);
//		System.out.println("strChex = " + strChex);
        if (strMeiz.equals("丁煤") || strMeiz.equals("丁生粉")) {
//			System.out.println("场别为 ‘丁煤处理’");
            strChangb = "2";
        } else {
//			System.out.println("场别为 ‘贵州鸭溪发电有限公司’");
            strChangb = "1";
        }
        if (!strZhefl.equals("") && !strZhefl.equals("0") && !strZhefl.equals("0.0") && !strZhefl.equals("0.00")) {
//			System.out.println("进入拆分");
            String strJingz1 = "" + (double) Math.round(Double.parseDouble(strJingz) * Double.parseDouble(strZhefl) * 100) / 100;//新净重
            String strMaoz1 = "" + (double) Math.round((Double.parseDouble(strJingz1) + Double.parseDouble(strPiz)) * 100) / 100; //新毛重
            String strPiaoz1 = strJingz1;
            String strKuid1 = "0";
            String strYingd1 = "0";
            String strKoud1 = "0";
            String strChex1 = Integer.parseInt(strChex) + 600 + "";
            String strMeiz1 = "";
            if (strMeiz.equals("丁煤")) {
                strMeiz1 = "丁生粉";
            } else {
                strMeiz1 = "块生粉";
            }
            strMeiz1 = getTableID("PINZ", "RANLPZB", strMeiz1, con, "ID");
//			System.out.println("strMeiz1 = " + strMeiz1);
//			System.out.println("strPiaoz1 = " + strPiaoz1);
//			System.out.println("strMaoz1 = " + strMaoz1);
//			System.out.println("strJingz1 = " + strJingz1);
//			System.out.println("strYingd1 = " + strYingd1);
//			System.out.println("strKuid1 = " + strKuid1);
//			System.out.println("strKoud1 = " + strKoud1);
//			System.out.println("strChex1 = " + strChex1);
            sql.append("DELETE FROM QICHJJBTMP WHERE XUH = '" + strChex1 + "' AND CHEPH = '" + strCheph + "' AND JIANPSJ = TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS');\n");//删除已经发过的车皮
            sql.append("INSERT INTO QICHJJBTMP");
            sql.append("(ID, CHEPH, SHOUHR, MEIKDQB_ID, MEIKXXB_ID, RANLPZB_ID, FAHL, ");
            sql.append("MAOZ, PIZ, KOUD, YUNS, KUID, JIANMSJ, JIANPSJ, YINGD, ZHUANGT, ");
            sql.append("JIHKJB_ID, MEIGY, CHENGYDW, JIANJY, BEIZ, MEICB_ID, XUH, CHANGBB_ID, PANDMC_ID,JILHH,JINGZ,HENGH)");
            sql.append(" VALUES(");
            sql.append("XL_QICHJJBTMP_ID.NEXTVAL,"); // id
            sql.append("'" + strCheph + "',"); // 车皮号
            sql.append("'" + strShouhr + "',"); // 收货人
            sql.append(strFahdw + ","); // 发货单位
            sql.append(strMeikdw + ","); // 煤矿单位
            sql.append(strMeiz1 + ","); // 燃料品种
            sql.append(strPiaoz1 + ","); // 发货量
            sql.append(strMaoz1 + ","); // 毛重=毛重-扣吨
            sql.append(strPiz + ","); // 皮重
            sql.append(strKoud1 + ","); // 扣吨
            sql.append(strYuns + ","); // 运损
            sql.append(strKuid1 + ","); // 亏吨
            sql.append("TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS'),"); // 检毛时间
            sql.append("TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS'),"); // 检皮时间
            sql.append(strYingd1 + ","); // 盈吨
            sql.append(0 + ","); // 状态
            sql.append(strJihkj + ","); // 计划口径
            sql.append("'" + strMaozjjy + "',"); // 煤管员
            sql.append("'" + strYunsdw + "',");// 承运单位
            sql.append("'" + strPizjjy + "',");// 检斤员
            sql.append("'" + strBeiz + "',"); // 备注
            sql.append(strMeic + ","); // 煤场
            sql.append("'" + strChex1 + "',"); // 序号
            sql.append(strChangb + ","); // 厂别
            sql.append(-1); // 盘点煤场
            sql.append(",'" + strZhongchbh + "'");
            sql.append("," + strJingz1 + ",'" + strQingchbh + "');\n");
            strMaoz = "" + (double) Math.round((Double.parseDouble(strMaoz) - Double.parseDouble(strJingz1)) * 100) / 100;
            strJingz = "" + (double) Math.round((Double.parseDouble(strJingz) - Double.parseDouble(strJingz1)) * 100) / 100;
            strPiaoz = "" + (double) Math.round((Double.parseDouble(strPiaoz) - Double.parseDouble(strPiaoz1)) * 100) / 100;
        }
        //不是直接存的煤种，是根据煤种在ranlpzb中找的对应id
        strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con, "ID");
//		System.out.println("strMeiz = " + strMeiz);
//		System.out.println("strPiaoz = " + strPiaoz);
//		System.out.println("strMaoz = " + strMaoz);
//		System.out.println("strJingz = " + strJingz);
//		System.out.println("strYingd = " + strYingd);
//		System.out.println("strKoud = " + strKoud);
//		System.out.println("strKuid = " + strKuid);
//		System.out.println("strChex = " + strChex);
        sql.append("DELETE FROM QICHJJBTMP WHERE XUH = '" + strChex + "' AND CHEPH = '" + strCheph + "' AND JIANPSJ = TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS');\n");//删除已经发过的车皮
        sql.append("INSERT INTO QICHJJBTMP");
        sql.append("(ID, CHEPH, SHOUHR, MEIKDQB_ID, MEIKXXB_ID, RANLPZB_ID, FAHL, ");
        sql.append("MAOZ, PIZ, KOUD, YUNS, KUID, JIANMSJ, JIANPSJ, YINGD, ZHUANGT, ");
        sql.append("JIHKJB_ID, MEIGY, CHENGYDW, JIANJY, BEIZ, MEICB_ID, XUH, CHANGBB_ID, PANDMC_ID,JILHH,JINGZ,HENGH)");
        sql.append(" VALUES(");
        sql.append("XL_QICHJJBTMP_ID.NEXTVAL,"); // id
        sql.append("'" + strCheph + "',"); // 车皮号
        sql.append("'" + strShouhr + "',"); // 收货人
        sql.append(strFahdw + ","); // 发货单位
        sql.append(strMeikdw + ","); // 煤矿单位
        sql.append(strMeiz + ","); // 燃料品种
        sql.append(strPiaoz + ","); // 发货量
        sql.append(strMaoz + ","); // 毛重=毛重-扣吨
        sql.append(strPiz + ","); // 皮重
        sql.append(strKoud + ","); // 扣吨
        sql.append(strYuns + ","); // 运损
        sql.append(strKuid + ","); // 亏吨
        sql.append("TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS'),"); // 检毛时间
        sql.append("TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS'),"); // 检皮时间
        sql.append(strYingd + ","); // 盈吨
        sql.append(0 + ","); // 状态
        sql.append(strJihkj + ","); // 计划口径
        sql.append("'" + strMaozjjy + "',"); // 煤管员
        sql.append("'" + strYunsdw + "',");// 承运单位
        sql.append("'" + strPizjjy + "',");// 检斤员
        sql.append("'" + strBeiz + "',"); // 备注
        sql.append(strMeic + ","); // 煤场
        sql.append("'" + strChex + "',"); // 序号
        sql.append(strChangb + ","); // 厂别
        sql.append(-1); // 盘点煤场
        sql.append(",'" + strZhongchbh + "'");
        sql.append("," + strJingz + ",'" + strQingchbh + "');\n");
//		System.out.println("验收传燃料！");
        return sql.toString();
    }

    //将车皮信息写入chepbtmp
    private static String syncCHEPBTMP(Element e2) {
//		System.out.println("数据插入chepbtmp");
        StringBuffer sql = new StringBuffer();
        String strDiancmc = e2.getChildText("电厂名称");
//		String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");// 标识要进行的操作。0
//		// 表示覆盖或增加对应的数据，1表示删除
//		String strLury1 = e2.getChildText("Lury1");// 票据号
//		String strCaiyry = e2.getChildText("Caiyry");// 车皮号
//		String strCaiyfs = e2.getChildText("Caiyfs");// 煤矿编码
//		String strHuaysj = e2.getChildText("Huaysj");// 仓库编码,
//		String strQnetar = e2.getChildText("Qnetar");// 自编号，ERP系统形成，燃料系统中不使用
//		String strAar = e2.getChildText("Aar");// 燃料品种名称
//		String strAd = e2.getChildText("Ad");// 到货日期，燃料系统中采用chepb.zhongcsj
//		String strVdaf = e2.getChildText("Vdaf");// 进厂称重时间，chepb.zhongcsj
//		String strMt = e2.getChildText("Mt");// 离厂称重时间，chepb.qingcsj
//		// double dblZK = noDoubleNullVal(e.getChildText("厂别ID"));// 扣吨
//		String strStad = e2.getChildText("Stad");// 毛重
//		String strAad = e2.getChildText("Aad");// 皮重
//		String strMad = e2.getChildText("Mad");// 扣吨
//		String strQbad = e2.getChildText("Qbad");// 备注
//		String strHad = e2.getChildText("Had");// zihilb_id
//		String strVad = e2.getChildText("Vad");// 采制化批次的年度，与燃料系统中的采样时间对应
//		String strStd = e2.getChildText("Std");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strQgrad = e2.getChildText("Qgrad");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strHuayy = e2.getChildText("Huayy");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strLury2 = e2.getChildText("Lury2");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strShenhzt = e2.getChildText("Shenhzt");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strShenhry = e2.getChildText("Shenhry");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strHuaybh = e2.getChildText("Huaybh");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strFcad = e2.getChildText("Fcad");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strShangjshzt = e2.getChildText("Shangjshzt");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strHdaf = e2.getChildText("Hdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strQgrdaf = e2.getChildText("Qgrdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strShenhrq = e2.getChildText("Shenhrq");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strShangjshry = e2.getChildText("Shangjshry");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strShangjshrq = e2.getChildText("Shangjshrq");// 采制化批次的月度，与燃料系统中的采样时间对应
//		String strSdaf = e2.getChildText("Sdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        // 将车皮信息写入临时表(chepbtmp)
        List lstChildrenRecords = e2.getChild("运单").getChildren();// 每个化验编号下对应的车皮数据
        int intListSize = lstChildrenRecords.size();
        for (int j = 0; j < intListSize; j++) {
            Element e = (Element) lstChildrenRecords.get(j);
            String strPizsj = e.getChildText("毛重时间");
            String strCheph = e.getChildText("车皮号");
            String strqingcsj = e.getChildText("皮重时间");
            //皮毛时间不对应
            sql.append("INSERT INTO CHEPBTMP (ID,CHEPH,GUOHSJ,PIAOJH,DIANCXXB_ID,fahrq) VALUES (XL_CHEPB_ID.NEXTVAL,");
            sql.append("'" + strCheph + "'");
            sql.append(",TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS')");
            sql.append(",'" + strBianh + "'");
            sql.append(",(SELECT ID FROM DIANCXXB WHERE JIANC = '" + strDiancmc + "'),TO_DATE('" + strqingcsj + "','YYYY-MM-DD HH24:MI:SS'));\n");
        }

        return sql.toString();
    }

    private String getZhilbtmpId(String zhilb_id, String bum) {
        //双化验的时候，根据zhilb_id，找到各个部门对应zhilbtmp的id
        String id = "";
        JDBCcon cn = new JDBCcon();
        ResultSet rs = cn.getResultSet("select id from zhilbtmp where zhilb_id = " + zhilb_id + " And bum = '" + bum + "'");
//		System.out.println("select id from zhilbtmp where zhilb_id = " + zhilb_id + " And bum = '"+ bum +"'");
        try {
            if (rs.next()) {
                id = rs.getString("id");
            }
//			System.out.println("zhilbtmp.id = "+id);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cn.Close();
        }
        return id;
    }

    private boolean ValidateUser(String user, String password) {
        JDBCcon con = new JDBCcon();
        ResultSet rs = con
                .getResultSet("SELECT * FROM jiekzhb WHERE yonghmc = '" + user
                        + "' AND mim = '" + password + "'");
//		System.out.println("SELECT * FROM jiekzhb WHERE yonghmc = '" + user
//						+ "' AND mim = '" + password + "'");
        try {
            if (rs.next()) {
                // 外部用户验证通过
                return true;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            con.Close();
        }

        return false;// 用户验证不通过
    }

    private static void bulidXMLData(String fileType, String xml) {
        try {
            String sql = "SELECT ZHI FROM XITXXB WHERE DUIXM = '"
                    + "金元接收文件备份根目录' AND SHIFSY=1";
            String bakFilePath = "C:/xmlbak";
            JDBCcon con = new JDBCcon();
            ResultSet rs = con.getResultSet(sql);
            if (rs.next()) {
                bakFilePath = rs.getString("ZHI");
            }
            RandomGUID guid = new RandomGUID();
            String MD5 = guid.valueAfterMD5;
            File file = new File(bakFilePath);
            FileWriter writer = new FileWriter(file.getAbsolutePath() + "/"
                    + fileType + "-" + MD5 + ".xml");
            writer.write(xml);
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    private static byte[] bulidXMLData(String returnCode, String returnMsg,
                                       String transCode, String fileType) {
        byte[] bXML = null;
        Element root = new Element("FUEL");
        Document document = new Document(root);
        Element e = new Element("param");
        e.addContent(new Element("TransCode").addContent(transCode));
        e.addContent(new Element("RspCode").addContent(returnCode));
        e.addContent(new Element("RspMsg").addContent(returnMsg));
        root.addContent(e);
        try {
//            String sql = "SELECT ZHI FROM XITXXB WHERE DUIXM = '"
//                    + "接口文件备份根目录' AND SHIFSY=1";
            String bakFilePath = "C:/xmlbak";
            JDBCcon con = new JDBCcon();
//            ResultSet rs = con.getResultSet(sql);
//            if (rs.next()) {
//                bakFilePath = rs.getString("ZHI");
//            }
            RandomGUID guid = new RandomGUID();
            String MD5 = guid.valueAfterMD5;
            File file = new File(bakFilePath);
            FileWriter writer = new FileWriter(file.getAbsolutePath() + "/"
                    + fileType + "-" + transCode + "-" + MD5 + ".xml");
            XMLOutputter outputter = new XMLOutputter();
            Format format = Format.getPrettyFormat();
            format.setEncoding("GB2312");
            outputter.setFormat(format);
            outputter.output(document, writer);// 备份返回报文文件
            outputter.outputString(document);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            outputter.output(document, bo);
            bXML = bo.toByteArray();
            bo.close();
            outputter = null;
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return bXML;
    }


    //获取fahbtmp或qichjjbtmp中zhilb_id
    private static String getZhilbId(String type) {
        JDBCcon con = new JDBCcon();
        String zhilb_Id = "";
        String sql = "";
        String Id_temp = "";
        ResultSet rs;
        if (type.equalsIgnoreCase("CZHSJ_R") || type.equalsIgnoreCase("CZHSJ_J") || type.equalsIgnoreCase("CZHSJ_C")) {
            sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n" +
                    "  FROM QICHJJBTMP\n" +
                    " WHERE (JIANMSJ, CHEPH) IN (SELECT GUOHSJ, CHEPH FROM CHEPBTMP)";
        } else if (type.equalsIgnoreCase("HCZHSJ_R") || type.equalsIgnoreCase("HCZHSJ_J") || type.equalsIgnoreCase("HCZHSJ_C")) {
//			sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n" +		//阜新电厂重码解决
//					" FROM FAHBTMP\n " +
//					"WHERE (to_char(YUNDLRSJ,'yyyy-mm-dd HH24'), CHEPH) " +
//					"IN (SELECT to_char(GUOHSJ,'yyyy-mm-dd HH24'), CHEPH FROM CHEPBTMP)";
            sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n" +            //开封电厂使用运单录入时间
                    " FROM FAHBTMP\n" +
                    " WHERE (TRUNC(YUNDLRSJ), CHEPH) IN (SELECT TRUNC(GUOHSJ), CHEPH FROM CHEPBTMP)";

//			sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n"+			//其他电厂
//			  " FROM FAHBTMP\n"+
//			  " WHERE (TRUNC(DAOHRQ), CHEPH) IN (SELECT TRUNC(GUOHSJ), CHEPH FROM CHEPBTMP)";

        } else if (type.substring(15).equalsIgnoreCase("Q")) {
//			System.out.println("type.substring(15) = "+type.substring(15));
            sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n" +
                    "  FROM QICHJJBTMP\n" +
                    " WHERE (JIANMSJ, CHEPH) IN (SELECT GUOHSJ, CHEPH FROM CHEPBTMP)";
        } else if (type.substring(15).equalsIgnoreCase("H")) {
//			System.out.println("type.substring(15) = "+type.substring(15));
            sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n" +
                    "  FROM FAHBTMP\n" +
                    " WHERE (TRUNC(DAOHRQ), CHEPH) IN\n" +
                    "       (SELECT TRUNC(GUOHSJ), CHEPH FROM CHEPBTMP)";
        }
//		System.out.println(sql);
        rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
                Id_temp = rs.getString("ZHILB_ID");
                if (Id_temp.equals("0")) return "";
                if (!zhilb_Id.equals("")) {
                    zhilb_Id = zhilb_Id + "," + Id_temp;
                } else
                    zhilb_Id = Id_temp;
//				System.out.println("zhilb_Id = "+zhilb_Id);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return zhilb_Id;
    }

    //获取原zhilb中caiysj
    private static String yuanCaiYSJ(String zhilb_Id) {
        JDBCcon con = new JDBCcon();
        String caiYSJ = "";
        ResultSet rs = con.getResultSet("SELECT TO_CHAR(CAIYSJ,'yyyy-mm-dd hh24:mi:ss') as CAIYSJ FROM ZHILB WHERE ID = '" + zhilb_Id + "'");
        try {
            while (rs.next()) {
                caiYSJ = rs.getString("CAIYSJ");
//				System.out.println("原caiYSJ = "+caiYSJ);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return caiYSJ;
    }

    //获取原zhilb中caiyry
    private static String yuanCaiYRY(String zhilb_Id) {
        JDBCcon con = new JDBCcon();
        String caiYRY = "";
        ResultSet rs = con.getResultSet("SELECT CAIYRY FROM ZHILB WHERE ID = '" + zhilb_Id + "'");
        try {
            while (rs.next()) {
                caiYRY = rs.getString("CAIYRY");
//				System.out.println("原caiYRY = "+caiYRY);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return caiYRY;
    }


    //判断fahbtmp中是否有zhilb_id
    private int panDuanFahbtmp(Element e1) {
        int result = 0;
        JDBCcon con = new JDBCcon();
        List lstChildrenRecords = e1.getChild("运单").getChildren();// 每个化验编号下对应的车皮数据
        int intListSize = lstChildrenRecords.size();
        Element e2 = (Element) lstChildrenRecords.get(0);
        String strCheph = e2.getChildText("车皮号");
        String strqingcsj = e2.getChildText("皮重时间");
        ResultSet rs = con.getResultSet(
                "SELECT DISTINCT ZHILB_ID FROM FAHBTMP WHERE GUOHSJ BETWEEN " +
                        "(SELECT GUOHSJ-3/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND " +
                        "(SELECT GUOHSJ+3/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND CHEPH IN (SELECT CHEPH FROM CHEPBTMP) AND ROWNUM = 1");
        try {
            while (rs.next()) {
                result += 1;
            }
            rs.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return 0;
    }

    private int panDuanZhilbtmp(String zhilb_Id) {
        int result = 0;
        JDBCcon con = new JDBCcon();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ZHILBTMP WHERE ZHILB_ID in (" + zhilb_Id + ")");
//		System.out.println(sql);
        ResultSet rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
                result += 1;
            }
//			System.out.println("result = "+result);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            con.Close();
        }
    }

    //传入为Rg（下样），插入zhilbtmp
    private static String insertZhilbtmpRg(Element e2, String zhilb_Id) {
        // 同步化验数据
        StringBuffer sql = new StringBuffer();
//		String strDiancmc = e2.getChildText("电厂名称");
        String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");// 标识要进行的操作。0
        // 表示覆盖或增加对应的数据，1表示删除
        String strLury1 = e2.getChildText("Lury1");// 票据号
        String strCaiyry = e2.getChildText("Caiyry");// 车皮号
        String strCaiyfs = e2.getChildText("Caiyfs");// 煤矿编码
        String strHuaysj = e2.getChildText("Huaysj");// 仓库编码,
        String strQnetar = e2.getChildText("Qnetar");// 自编号，ERP系统形成，燃料系统中不使用
        String strAar = e2.getChildText("Aar");// 燃料品种名称
        String strAd = e2.getChildText("Ad");// 到货日期，燃料系统中采用chepb.zhongcsj
        String strVdaf = e2.getChildText("Vdaf");// 进厂称重时间，chepb.zhongcsj
        String strMt = e2.getChildText("Mt");// 离厂称重时间，chepb.qingcsj
        // double dblZK = noDoubleNullVal(e.getChildText("厂别ID"));// 扣吨
        String strStad = e2.getChildText("Stad");// 毛重
        String strAad = e2.getChildText("Aad");// 皮重
        String strMad = e2.getChildText("Mad");// 扣吨
        String strQbad = e2.getChildText("Qbad");// 备注
        String strHad = e2.getChildText("Had");// zihilb_id
        String strVad = e2.getChildText("Vad");// 采制化批次的年度，与燃料系统中的采样时间对应
        String strStd = e2.getChildText("Std");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strQgrad = e2.getChildText("Qgrad");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strHuayy = e2.getChildText("Huayy");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strLury2 = e2.getChildText("Lury2");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShenhzt = "1";//e2.getChildText("Shenhzt");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShenhry = e2.getChildText("Shenhry");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strHuaybh = e2.getChildText("Huaybh");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strFcad = e2.getChildText("Fcad");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strHdaf = e2.getChildText("Hdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strQgrdaf = e2.getChildText("Qgrdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShenhrq = e2.getChildText("Shenhrq");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshry = e2.getChildText("Shangjshry");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshrq = e2.getChildText("Shangjshrq");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strSdaf = e2.getChildText("Sdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
//		List lstChildrenRecords = e2.getChild("运单").getChildren();// 每个化验编号下对应的车皮数据
//		int intListSize = lstChildrenRecords.size();
//		Element e3 = (Element) lstChildrenRecords.get(0);
//		String strCheph = e3.getChildText("车皮号");
//		String strqingcsj=e3.getChildText("皮重时间");
        String strQgrd = e2.getChildText("Qgrd");// 2016年5月18日 Qiuzw 读取接口文件中的“干燥基高位热值”

        sql.append("INSERT INTO ZHILBTMP (ID,zhilb_id,CAIYSJ,CaiYBH,LURY1, CAIYRY,CAIYFS, HUAYSJ, FARL, SHOUDJHF, GANZJHF, GANZWHJHFF, QUANSF, KONGQGZJL, KONGQGZJHF, KONGQGZJSF, DANTRL, KONGQGZJQ, KONGQGZJHFF, GANZJL, GANZJGWRZ,");
        sql.append(" HUAYY,LURY2, SHENHZT, SHENHRY, HUAYBH, FCAD, SHANGJSHZT, HDAF, GANZWHJGWRZ,SHENHRQ, SHANGJSHRY, SHANGJSHRQ, SDAF,bum,LEIB,QGR_D");
        sql.append(")VALUES ");
        sql.append("(");
        sql.append("XL_ZHILBtmp_ID.NEXTVAL,"); // id
//		String zhilb_Id = getZhilbId(type);
        sql.append("'" + zhilb_Id + "'");
//		sql.append("(SELECT DISTINCT ZHILB_ID FROM FAHBTMP WHERE GUOHSJ BETWEEN (SELECT GUOHSJ-1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND (SELECT GUOHSJ+1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND CHEPH IN (SELECT CHEPH FROM CHEPBTMP) AND ROWNUM = 1)");
        sql.append(",to_date('" + strCaiysj + "','yyyy-mm-dd hh24:mi:ss'),");
        sql.append("'" + strBianh + "',");
        sql.append("'" + strLury1 + "',");
        sql.append("'" + strCaiyry + "',");
        sql.append("'" + strCaiyfs + "',");
        sql.append("to_date('" + strHuaysj + "','YYYY-MM-DD HH24:MI:SS'),");
        sql.append("'" + strQnetar + "',");
        sql.append("'" + strAar + "',");
        sql.append("'" + strAd + "',");
        sql.append("'" + strVdaf + "',");
        sql.append("'" + strMt + "',");
        sql.append("'" + strStad + "',");
        sql.append("'" + strAad + "',");
        sql.append("'" + strMad + "',");
        sql.append("'" + strQbad + "',");
        sql.append("'" + strHad + "',");
        sql.append("'" + strVad + "',");
        sql.append("'" + strStd + "',");
        sql.append("'" + strQgrad + "',");
        sql.append("'" + strHuayy + "',");
        sql.append("'" + strLury2 + "',");
        sql.append(strShenhzt + ",");
        sql.append("'" + strShenhry + "',");
        sql.append("'" + strHuaybh + "',");
        sql.append("'" + strFcad + "',");
        sql.append(strShangjshzt + ",");
        sql.append("'" + strHdaf + "',");
        sql.append("'" + strQgrdaf + "',");
        sql.append("to_date('" + strShenhrq + "','yyyy-mm-dd'),");
        sql.append("'" + strShangjshry + "',");
        sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
        sql.append("'" + strSdaf + "',");
        sql.append("'',");
        sql.append("'下样'," + strQgrd + ");\n");

        return sql.toString();
    }

    //传入为Jc（上样），插入zhilbtmp并且更新 zhilb
    private static String insertZhilbtmpJc(Element e2, String zhilb_Id) {
        // 同步化验数据
        StringBuffer sql = new StringBuffer();
//		String strDiancmc = e2.getChildText("电厂名称");
        String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");//
        String strLury1 = e2.getChildText("Lury1");//
        String strCaiyry = e2.getChildText("Caiyry");//
        String strCaiyfs = e2.getChildText("Caiyfs");//
        String strHuaysj = e2.getChildText("Huaysj");//
        String strQnetar = e2.getChildText("Qnetar");//
        String strAar = e2.getChildText("Aar");//
        String strAd = e2.getChildText("Ad");//
        String strVdaf = e2.getChildText("Vdaf");//
        String strMt = e2.getChildText("Mt");//
        String strStad = e2.getChildText("Stad");//
        String strAad = e2.getChildText("Aad");//
        String strMad = e2.getChildText("Mad");//
        String strQbad = e2.getChildText("Qbad");//
        String strHad = e2.getChildText("Had");//
        String strVad = e2.getChildText("Vad");//
        String strStd = e2.getChildText("Std");//
        String strQgrad = e2.getChildText("Qgrad");//
        String strHuayy = e2.getChildText("Huayy");//
        String strLury2 = e2.getChildText("Lury2");//
        String strShenhzt = "3";//e2.getChildText("Shenhzt");//
        String strShenhry = e2.getChildText("Shenhry");//
        String strHuaybh = e2.getChildText("Huaybh");//
        String strFcad = e2.getChildText("Fcad");//
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");//
        String strHdaf = e2.getChildText("Hdaf");//
        String strQgrdaf = e2.getChildText("Qgrdaf");//
        String strShenhrq = e2.getChildText("Shenhrq");//
        String strShangjshry = e2.getChildText("Shangjshry");//
        String strShangjshrq = e2.getChildText("Shangjshrq");//
        String strSdaf = e2.getChildText("Sdaf");//
//		List lstChildrenRecords = e2.getChild("运单").getChildren();// 
//		int intListSize = lstChildrenRecords.size();
//		Element e3 = (Element) lstChildrenRecords.get(0);
//		String strCheph = e3.getChildText("车皮号");
//		String strqingcsj=e3.getChildText("皮重时间");
        String strQgrd = e2.getChildText("Qgrd");// 2016年5月18日 Qiuzw 读取接口文件中的“干燥基高位热值”

        sql.append("INSERT INTO ZHILBTMP (ID,zhilb_id,CAIYSJ,CaiYBH,LURY1,CAIYRY,CAIYFS,HUAYSJ,FARL,SHOUDJHF,GANZJHF,GANZWHJHFF,QUANSF,KONGQGZJL,KONGQGZJHF,KONGQGZJSF,DANTRL,KONGQGZJQ,KONGQGZJHFF,GANZJL,GANZJGWRZ,");
        sql.append(" HUAYY,LURY2,SHENHZT,SHENHRY,HUAYBH,FCAD, SHANGJSHZT, HDAF, GANZWHJGWRZ,SHENHRQ,SHANGJSHRY,SHANGJSHRQ,SDAF,bum,LEIB,QGR_D");
        sql.append(")VALUES ");
        sql.append("(");
        sql.append("XL_ZHILBtmp_ID.NEXTVAL,"); // id
//		String zhilb_Id = getZhilbId(type);
        sql.append("'" + zhilb_Id + "'");
//		sql.append("(SELECT DISTINCT ZHILB_ID FROM FAHBTMP WHERE GUOHSJ BETWEEN (SELECT GUOHSJ-1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND (SELECT GUOHSJ+1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND CHEPH IN (SELECT CHEPH FROM CHEPBTMP) AND ROWNUM = 1)");
        sql.append(",to_date('" + strCaiysj + "','yyyy-mm-dd hh24:mi:ss'),");
        sql.append("'" + strBianh + "',");
        sql.append("'" + strLury1 + "',");
        sql.append("'" + strCaiyry + "',");
        sql.append("'" + strCaiyfs + "',");
        sql.append("to_date('" + strHuaysj + "','YYYY-MM-DD HH24:MI:SS'),");
        sql.append("'" + strQnetar + "',");
        sql.append("'" + strAar + "',");
        sql.append("'" + strAd + "',");
        sql.append("'" + strVdaf + "',");
        sql.append("'" + strMt + "',");
        sql.append("'" + strStad + "',");
        sql.append("'" + strAad + "',");
        sql.append("'" + strMad + "',");
        sql.append("'" + strQbad + "',");
        sql.append("'" + strHad + "',");
        sql.append("'" + strVad + "',");
        sql.append("'" + strStd + "',");
        sql.append("'" + strQgrad + "',");
        sql.append("'" + strHuayy + "',");
        sql.append("'" + strLury2 + "',");
        sql.append(strShenhzt + ",");
        sql.append("'" + strShenhry + "',");
        sql.append("'" + strHuaybh + "',");
        sql.append("'" + strFcad + "',");
        sql.append(strShangjshzt + ",");
        sql.append("'" + strHdaf + "',");
        sql.append("'" + strQgrdaf + "',");
        sql.append("to_date('" + strShenhrq + "','yyyy-mm-dd'),");
        sql.append("'" + strShangjshry + "',");
        sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
        sql.append("'" + strSdaf + "',");
        sql.append("'',");
        sql.append("'上样'," + strQgrd + ");\n");

//		sql.append("update zhilbtmp set HUAYBH='" + strHuaybh + "' where zhilb_id="+zhilb_Id+";\n");
//		String yuanCaiYSJ = yuanCaiYSJ(zhilb_Id);
//		String yuanCaiYRY = yuanCaiYRY(zhilb_Id);

        //更新zhilb
        sql.append("UPDATE ZHILB SET ");
        sql.append("CAIYSJ=to_date('" + strCaiysj + "','yyyy-mm-dd hh24:mi:ss')");
        //sql.append(",BIANH='"+strBianh+"'");
        sql.append(",LURY1='" + strLury1 + "'");
        sql.append(",CAIYRY='" + strCaiyry + "'");
        sql.append(",CAIYFS=" + strCaiyfs);
        sql.append(",HUAYSJ=to_date('" + strHuaysj + "','YYYY-MM-DD HH24:MI:SS')");
        sql.append(",FARL=" + strQnetar);
        sql.append(",SHOUDJHF=" + strAar);
        sql.append(",GANZJHF=" + strAd);
        sql.append(",HUIFF=" + strVdaf);
        sql.append(",QUANSF=" + strMt);
        sql.append(",LIUF=" + strStad);
        sql.append(",KONGQGZJHF=" + strAad);
        sql.append(",KONGQGZJSF=" + strMad);
        sql.append(",DANTRL=" + strQbad);
        sql.append(",KONGQGZJQ=" + strHad);
        sql.append(",KONGQGZJHFF=" + strVad);
        sql.append(",GANZJL=" + strStd);
        sql.append(",GANZJGWRZ=" + strQgrad);
        sql.append(",HUAYY='" + strHuayy + "'");
        sql.append(",LURY2='" + strLury2 + "'");
        sql.append(",SHENHZT=" + strShenhzt);
        sql.append(",SHENHRY='" + strShenhry + "'");
        sql.append(",HUAYBH='" + strHuaybh + "'");
        sql.append(",FCAD=" + strFcad);
        sql.append(",SHANGJSHZT=" + strShangjshzt);
        sql.append(",HDAF=" + strHdaf);
        sql.append(",GANZWHJGWRZ=" + strQgrdaf);
        sql.append(",SHENHRQ=to_date('" + strShenhrq + "','yyyy-mm-dd')");
        sql.append(",SHANGJSHRY='" + strShangjshry + "'");
        sql.append(",SHANGJSHRQ=to_date('" + strShangjshrq + "','yyyy-mm-dd')");
        sql.append(",SDAF=" + strSdaf);
        sql.append(",QGR_D=" + strQgrd);
        sql.append(" WHERE ID=" + zhilb_Id + ";\n ");

        return sql.toString();
    }

    //传入为Cc（中样），插入zhilbtmp
    private static String insertZhilbtmpCc(Element e2, String zhilb_Id) {
        // 同步化验数据
        StringBuffer sql = new StringBuffer();
//		String strDiancmc = e2.getChildText("电厂名称");
        String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");//
        String strLury1 = e2.getChildText("Lury1");//
        String strCaiyry = e2.getChildText("Caiyry");//
        String strCaiyfs = e2.getChildText("Caiyfs");//
        String strHuaysj = e2.getChildText("Huaysj");//
        String strQnetar = e2.getChildText("Qnetar");//
        String strAar = e2.getChildText("Aar");//
        String strAd = e2.getChildText("Ad");//
        String strVdaf = e2.getChildText("Vdaf");//
        String strMt = e2.getChildText("Mt");//
        // double dblZK = noDoubleNullVal(e.getChildText("厂别ID"));//
        String strStad = e2.getChildText("Stad");//
        String strAad = e2.getChildText("Aad");//
        String strMad = e2.getChildText("Mad");//
        String strQbad = e2.getChildText("Qbad");//
        String strHad = e2.getChildText("Had");//
        String strVad = e2.getChildText("Vad");//
        String strStd = e2.getChildText("Std");//
        String strQgrad = e2.getChildText("Qgrad");//
        String strHuayy = e2.getChildText("Huayy");//
        String strLury2 = e2.getChildText("Lury2");//
        String strShenhzt = "1";//e2.getChildText("Shenhzt");//
        String strShenhry = e2.getChildText("Shenhry");//
        String strHuaybh = e2.getChildText("Huaybh");//
        String strFcad = e2.getChildText("Fcad");//
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strHdaf = e2.getChildText("Hdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strQgrdaf = e2.getChildText("Qgrdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShenhrq = e2.getChildText("Shenhrq");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshry = e2.getChildText("Shangjshry");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strShangjshrq = e2.getChildText("Shangjshrq");// 采制化批次的月度，与燃料系统中的采样时间对应
        String strSdaf = e2.getChildText("Sdaf");// 采制化批次的月度，与燃料系统中的采样时间对应
        List lstChildrenRecords = e2.getChild("运单").getChildren();// 每个化验编号下对应的车皮数据
        int intListSize = lstChildrenRecords.size();
        Element e3 = (Element) lstChildrenRecords.get(0);
        String strCheph = e3.getChildText("车皮号");
        String strqingcsj = e3.getChildText("皮重时间");
        String strQgrd = e2.getChildText("Qgrd");// 2016年5月18日 Qiuzw 读取接口文件中的“干燥基高位热值”

        sql.append("INSERT INTO ZHILBTMP (ID,zhilb_id,CAIYSJ,CaiYBH,LURY1, CAIYRY,CAIYFS, HUAYSJ, FARL, SHOUDJHF, GANZJHF, GANZWHJHFF, QUANSF, KONGQGZJL, KONGQGZJHF, KONGQGZJSF, DANTRL, KONGQGZJQ, KONGQGZJHFF, GANZJL, GANZJGWRZ,");
        sql.append(" HUAYY,LURY2, SHENHZT, SHENHRY, HUAYBH, FCAD, SHANGJSHZT, HDAF, GANZWHJGWRZ,SHENHRQ, SHANGJSHRY, SHANGJSHRQ, SDAF,bum,LEIB,QGR_D");
        sql.append(")VALUES ");
        sql.append("(");
        sql.append("XL_ZHILBtmp_ID.NEXTVAL,"); // id
//		String zhilb_Id = getZhilbId(type);
        sql.append("'" + zhilb_Id + "'");
//		sql.append("(SELECT DISTINCT ZHILB_ID FROM FAHBTMP WHERE GUOHSJ BETWEEN (SELECT GUOHSJ-1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND (SELECT GUOHSJ+1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND CHEPH IN (SELECT CHEPH FROM CHEPBTMP) AND ROWNUM = 1)");
        sql.append(",to_date('" + strCaiysj + "','yyyy-mm-dd hh24:mi:ss'),");
        sql.append("'" + strBianh + "',");
        sql.append("'" + strLury1 + "',");
        sql.append("'" + strCaiyry + "',");
        sql.append("'" + strCaiyfs + "',");
        sql.append("to_date('" + strHuaysj + "','YYYY-MM-DD HH24:MI:SS'),");
        sql.append("'" + strQnetar + "',");
        sql.append("'" + strAar + "',");
        sql.append("'" + strAd + "',");
        sql.append("'" + strVdaf + "',");
        sql.append("'" + strMt + "',");
        sql.append("'" + strStad + "',");
        sql.append("'" + strAad + "',");
        sql.append("'" + strMad + "',");
        sql.append("'" + strQbad + "',");
        sql.append("'" + strHad + "',");
        sql.append("'" + strVad + "',");
        sql.append("'" + strStd + "',");
        sql.append("'" + strQgrad + "',");
        sql.append("'" + strHuayy + "',");
        sql.append("'" + strLury2 + "',");
        sql.append(strShenhzt + ",");
        sql.append("'" + strShenhry + "',");
        sql.append("'" + strHuaybh + "',");
        sql.append("'" + strFcad + "',");
        sql.append(strShangjshzt + ",");
        sql.append("'" + strHdaf + "',");
        sql.append("'" + strQgrdaf + "',");
        sql.append("to_date('" + strShenhrq + "','yyyy-mm-dd'),");
        sql.append("'" + strShangjshry + "',");
        sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
        sql.append("'" + strSdaf + "',");
        sql.append("'',");
        sql.append("'中样'," + strQgrd + ");\n");

        return sql.toString();
    }

    private static double getYunsl(String meikxxb_id) {
        double yunsl = 0;
        JDBCcon conyunsl = new JDBCcon();
        StringBuffer sql = new StringBuffer();
        sql.append("select qicysl as zhi from meikxxb where id=" + meikxxb_id);
        ResultSet rs = conyunsl.getResultSet(sql);
        try {
            if (rs.next()) {
                yunsl = rs.getDouble("zhi");
            } else {
                yunsl = 0.01;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conyunsl.Close();
        }
        return yunsl;
    }


    private static String insertZhilb(Element e2, String zhilb_Id, String zhilbtmp_Id, String bum, String leib) {
        // 将双化验的数据写入zhilb、zhilbtmp
        // 同步化验数据
        StringBuffer sql = new StringBuffer();
        String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");//
        String strLury1 = e2.getChildText("Lury1");//
        String strCaiyry = e2.getChildText("Caiyry");//
        String strCaiyfs = e2.getChildText("Caiyfs");//
        String strHuaysj = e2.getChildText("Huaysj");//
        String strQnetar = e2.getChildText("Qnetar");//
        String strAar = e2.getChildText("Aar");//
        String strAd = e2.getChildText("Ad");//
        String strVdaf = e2.getChildText("Vdaf");//
        String strMt = e2.getChildText("Mt");//
        String strStad = e2.getChildText("Stad");//
        String strAad = e2.getChildText("Aad");//
        String strMad = e2.getChildText("Mad");//
        String strQbad = e2.getChildText("Qbad");//
        String strHad = e2.getChildText("Had");//
        String strVad = e2.getChildText("Vad");//
        String strStd = e2.getChildText("Std");//
        String strQgrad = e2.getChildText("Qgrad");//
        String strHuayy = e2.getChildText("Huayy");//
        String strLury2 = e2.getChildText("Lury2");//
        String strShenhzt = "3";//e2.getChildText("Shenhzt");//
        String strShenhry = e2.getChildText("Shenhry");//
        String strHuaybh = e2.getChildText("Huaybh");//
        String strFcad = e2.getChildText("Fcad");//
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");//
        String strHdaf = e2.getChildText("Hdaf");//
        String strQgrdaf = e2.getChildText("Qgrdaf");//
        String strShenhrq = e2.getChildText("Shenhrq");//
        String strShangjshry = e2.getChildText("Shangjshry");//
        String strShangjshrq = e2.getChildText("Shangjshrq");//
        String strSdaf = e2.getChildText("Sdaf");//
        String strQgrd = e2.getChildText("Qgrd");// 2016年5月18日 Qiuzw 读取接口文件中的“干燥基高位热值”
        // 做为单部门的报告值，bum = null，leib = 上样、中样、下样
        sql.append("INSERT INTO ZHILBTMP (ID,ZHILB_ID,CAIYSJ,CaiYBH,LURY1, CAIYRY,CAIYFS, HUAYSJ, FARL, SHOUDJHF, GANZJHF, GANZWHJHFF, QUANSF," +
                " KONGQGZJL, KONGQGZJHF, KONGQGZJSF, DANTRL, KONGQGZJQ, KONGQGZJHFF, GANZJL, GANZJGWRZ,");
        sql.append(" HUAYY,LURY2, SHENHZT, SHENHRY, HUAYBH, FCAD, SHANGJSHZT, HDAF, GANZWHJGWRZ,SHENHRQ, SHANGJSHRY, SHANGJSHRQ, SDAF,bum,LEIB,QGR_D");
        sql.append(")VALUES ");
        sql.append("(");
        sql.append("XL_ZHILBtmp_ID.NEXTVAL,"); // id
//		String zhilb_Id = getZhilbId(type);
        sql.append("'" + zhilbtmp_Id + "'");
//		sql.append("(SELECT DISTINCT ZHILB_ID FROM FAHBTMP WHERE GUOHSJ BETWEEN (SELECT GUOHSJ-1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND (SELECT GUOHSJ+1/24 FROM CHEPBTMP WHERE ROWNUM = 1) AND CHEPH IN (SELECT CHEPH FROM CHEPBTMP) AND ROWNUM = 1)");
        sql.append(",to_date('" + strCaiysj + "','yyyy-mm-dd hh24:mi:ss'),");
        sql.append("'" + strBianh + "',");
        sql.append("'" + strLury1 + "',");
        sql.append("'" + strCaiyry + "',");
        sql.append("'" + strCaiyfs + "',");
        sql.append("to_date('" + strHuaysj + "','YYYY-MM-DD HH24:MI:SS'),");
        sql.append("'" + strQnetar + "',");
        sql.append("'" + strAar + "',");
        sql.append("'" + strAd + "',");
        sql.append("'" + strVdaf + "',");
        sql.append("'" + strMt + "',");
        sql.append("'" + strStad + "',");
        sql.append("'" + strAad + "',");
        sql.append("'" + strMad + "',");
        sql.append("'" + strQbad + "',");
        sql.append("'" + strHad + "',");
        sql.append("'" + strVad + "',");
        sql.append("'" + strStd + "',");
        sql.append("'" + strQgrad + "',");
        sql.append("'" + strHuayy + "',");
        sql.append("'" + strLury2 + "',");
        sql.append(strShenhzt + ",");
        sql.append("'" + strShenhry + "',");
        sql.append("'" + strHuaybh + "',");
        sql.append("'" + strFcad + "',");
        sql.append(strShangjshzt + ",");
        sql.append("'" + strHdaf + "',");
        sql.append("'" + strQgrdaf + "',");
        sql.append("to_date('" + strShenhrq + "','yyyy-mm-dd'),");
        sql.append("'" + strShangjshry + "',");
//		System.out.println("strShangjshrq="+strShangjshrq+"test");
//		if(strShangjshrq.equalsIgnoreCase("\n")){
//			System.out.println("strShangjshrq为回车");
//			sql.append("'',");
//		}else{
//			System.out.println("strShangjshrq不为空");
//			sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
//		}
        sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
        sql.append("'" + strSdaf + "',");
        sql.append("null,");
        sql.append("'" + leib + "'," + strQgrd + ");\n");

        if (leib.equals("上样")) {
            //更新zhilbtmp
            sql.append("DELETE FROM ZHILBTMP WHERE ID = '" + zhilbtmp_Id + "';\n");
            sql.append("INSERT INTO ZHILBTMP (ID,ZHILB_ID,CAIYSJ,CaiYBH,LURY1, CAIYRY,CAIYFS, HUAYSJ, FARL, SHOUDJHF, GANZJHF, GANZWHJHFF, QUANSF," +
                    " KONGQGZJL, KONGQGZJHF, KONGQGZJSF, DANTRL, KONGQGZJQ, KONGQGZJHFF, GANZJL, GANZJGWRZ,");
            sql.append(" HUAYY,LURY2, SHENHZT, SHENHRY, HUAYBH, FCAD, SHANGJSHZT, HDAF, GANZWHJGWRZ,SHENHRQ, SHANGJSHRY, SHANGJSHRQ, SDAF,bum,LEIB,QGR_D");
            sql.append(")VALUES ");
            sql.append("(");
            sql.append("'" + zhilbtmp_Id + "'");
            sql.append("," + zhilb_Id);
            sql.append(",to_date('" + strCaiysj + "','yyyy-mm-dd hh24:mi:ss'),");
            sql.append("'" + strBianh + "',");
            sql.append("'" + strLury1 + "',");
            sql.append("'" + strCaiyry + "',");
            sql.append("'" + strCaiyfs + "',");
            sql.append("to_date('" + strHuaysj + "','YYYY-MM-DD HH24:MI:SS'),");
            sql.append("'" + strQnetar + "',");
            sql.append("'" + strAar + "',");
            sql.append("'" + strAd + "',");
            sql.append("'" + strVdaf + "',");
            sql.append("'" + strMt + "',");
            sql.append("'" + strStad + "',");
            sql.append("'" + strAad + "',");
            sql.append("'" + strMad + "',");
            sql.append("'" + strQbad + "',");
            sql.append("'" + strHad + "',");
            sql.append("'" + strVad + "',");
            sql.append("'" + strStd + "',");
            sql.append("'" + strQgrad + "',");
            sql.append("'" + strHuayy + "',");
            sql.append("'" + strLury2 + "',");
            sql.append(strShenhzt + ",");
            sql.append("'" + strShenhry + "',");
            sql.append("'" + strHuaybh + "',");
            sql.append("'" + strFcad + "',");
            sql.append(strShangjshzt + ",");
            sql.append("'" + strHdaf + "',");
            sql.append("'" + strQgrdaf + "',");
            sql.append("to_date('" + strShenhrq + "','yyyy-mm-dd'),");
            sql.append("'" + strShangjshry + "',");
//			System.out.println("为上样时strShangjshrq ="+strShangjshrq+"test");
            sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
            sql.append("'" + strSdaf + "'");
            sql.append(",'" + bum + "',null," + strQgrd + ");\n");

        }


        return sql.toString();
    }


}
