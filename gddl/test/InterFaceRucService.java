/*
 *���ڣ�2016��5��18��
 *���ߣ�Qiuzw
 *���������ջ��鱨���е��еġ��������λ��ֵ��ָ��ֵ
 */
 
/*
 *���ڣ�2015��1��1��
 *���ߣ�������
 *��������ʱ���ѼϪ���⣬�޸�ë�ء�Ʊ�� 
 */

/*
 * ���ڣ�2014��12��17��
 * ����: �κ�
 * ��������ʱ���ѼϪ���⣬���Ӳ𳵹���
 */

/*
 * ���ڣ�2014��12��7��
 * ����: �κ�
 * ����������������⣬����˫���ƻ��������
 */

/*
 * ���ڣ�2014��11��18��
 * ����: �κ�
 * ������������ɽ����
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
    private static final String ERRMSG000 = "���ճɹ�";
    private static final String ERRMSG001 = "XMLData����ת��Ϊgb312����ʱ����";
    private static final String ERRMSG002 = "XML��ʽ��������";
    private static final String ERRMSG003 = "�˺���Ϣ����";
    private static final String ERRMSG004 = "��Ч��SQL���";
    private static final String ERRMSG005 = "ȼ��ϵͳδ����";

    private static final String ERRMSG104 = "δ֪����";
    private static final String ERRCODE000 = "1000";
    private static final String ERRCODE001 = "-1001";
    private static final String ERRCODE002 = "-1002";
    private static final String ERRCODE003 = "-1003";
    private static final String ERRCODE004 = "-1004";
    private static final String ERRCODE005 = "-1005";
    private static final String ERRCODE104 = "-1999";
    private static final String ERRCODE105 = "ϵͳ��û����ʽ�������ȴ�����ʽ��";

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
        //---------------------------------����������-------------------------------------------------------------------
        BufferedOutputStream bos;
        try {
            String pathname=getPathName("197",type);
            bos = new BufferedOutputStream(new FileOutputStream(new File(pathname)));
            bos.write(XMLData);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //type����ʾҪͬ������������
        // ����ͬ�����޸ģ���ÿ�ν�����һ���޸ļ�¼
        String strTransCode = "xxx";
        String strSyncFileName = "sync" + type + "data";
        byte[] b =  null;
        /* ��֤�û� */
//        if (!ValidateUser(user, password)) {
//            // �Ƿ��˺�
//            b = bulidXMLData(ERRCODE003, ERRMSG003, strTransCode, strSyncFileName);
//            return b;
//        }
        if (XMLData != null) {
//            JDBCcon cn = new JDBCcon( 0,  JNDIName,  ConnStr, UserName,  UserPassword);
            JDBCcon cn = new JDBCcon();
            cn.setAutoCommit(false);
            try {
                String res = new String(XMLData, "GB2312");// xml�ַ���ת��,�쳣UnsupportedEncodingException
                // �����û������ͬ������ 2011��8��26��
//                bulidXMLData(strSyncFileName, res);
                // ����xml
                SAXBuilder builder = new SAXBuilder();
                StringReader sr = new StringReader(res);
                InputSource is = new InputSource(sr);
                Document doc = builder.build(is);// ���ÿһ������֡�ļ����쳣IOException
                Element root = doc.getRootElement();// �õ�xml�ļ��ĸ��ڵ�
                List lstDataRecords = root.getChildren();// �õ������顰�ļ���
                int intChildrenLen = lstDataRecords.size();// �õ������顰�ĳ���
                StringBuffer sqls = new StringBuffer("");
                if (type.equalsIgnoreCase("CZSJ")) {
                    for (int i = 0; i < intChildrenLen; i++) {
                        Element e1 = (Element) lstDataRecords.get(i);
                        //����ϵͳ��ȼ��ϵͳ������
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
        // ͬ����������
//        StringBuffer sql = new StringBuffer();
//        String strChex = e.getChildText("����");
        String cheph = e.getChildText("��Ƥ��");// ��Ƥ��
        String maoz = e.getChildText("ë��");// ë��
        String piz = e.getChildText("Ƥ��");// Ƥ��
        String biaoz = e.getChildText("����");// Ʊ��
//        String strJingz = e.getChildText("����");// ����,
        String yingd = e.getChildText("ӯ��");// ӯ��
        String yuns = e.getChildText("����");// ����
        String koud = e.getChildText("�۶�");// �۶�
//        double piz2=Double.parseDouble(piz)-Double.parseDouble(koud);
        String gongysmc = e.getChildText("������λ");// ������λ��MEIKXXB�еġ�ú��λ���ơ���ͨ�����õ�MEIKDQB�е�ID
//        strFahdw = getTableID("MEIKDWQC", "MEIKXXB", strFahdw, con, "MEIKDQB_ID");
        String meikdwmc = e.getChildText("ú��λ");// ú��λ\

        String fahrq = e.getChildText("��������");// �������ڣ���ʱû���õ�
        String daohrq = e.getChildText("��������");// �������ڣ���ʱû���õ�
        String pinz = e.getChildText("ú��");// ú��
//            System.out.println("���ƺ�"+strCheph+" ú��:"+strMeiz+"]");
//		strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con,"ID");
        String faz = e.getChildText("��վ");// ��վ
//        strFaz = getTableID("JIANC", "CHEZXXB", strFaz, con, "ID");
        String yunsdw = e.getChildText("���䵥λ");// ���䵥λ
//2015��6��29�� qzw �ӡ�ú����Ϣ���ж�ȡú���Ĭ�Ͽھ�
        String jihkj = e.getChildText("�ƻ��ھ�");// �ƻ��ھ�
//		strJihkj = getTableID("MINGC", "JIHKJB", strJihkj, con,"ID");
//		String strJihkj = getTableID("ID", "MEIKXXB", strMeikdw, con,"JIHKJB_ID");
//        String strCheb = e.getChildText("����");// ������ʱû���õ�
//        String strJilfs = e.getChildText("������ʽ");// ������ʽ����ʱû���õ�
        String yunsfs = e.getChildText("���䷽ʽ");// ���䷽ʽ����ʱû���õ�
//        String strPiaojh = e.getChildText("Ʊ�ݺ�");// Ʊ�ݺţ���ʱû���õ�
        String zhongcsj = e.getChildText("ë��ʱ��");// ë��ʱ��
        String qingcsj = e.getChildText("Ƥ��ʱ��");// Ƥ��ʱ��
        String meigy = e.getChildText("ë�ؼ��Ա");// ë�ؼ��Ա
        String qingcjjy = e.getChildText("Ƥ�ؼ��Ա");// Ƥ�ؼ��Ա
        String strMeic = e.getChildText("ú��");// ú��
//        strMeic = getTableID("MEICMC", "MEICFQB", strMeic, con, "ID");
        String zhongchh = e.getChildText("�س�����");// �س��������
        String qingchh = "";//e.getChildText("�ᳵ����");// �ᳵ�������
        String strShouhr = e.getChildText("�ջ���");// �ջ���
        String beiz = e.getChildText("��ע");// ��ע
        String piaojh = e.getChildText("������ʶ��");
        String chec = e.getChildText("samcode");
        String zhilb_id=this.getZhilbid(chec,con);
        String sql="select * from chepbtmp where piaojh='"+piaojh+"'";
        ResultSet rs=con.getResultSet(sql);
        if(rs.next()){
            int fahb_id=0;
            fahb_id=rs.getInt("fahb_id");
            String id=rs.getString("id");
            if(fahb_id!=0){
                throw new Exception("ȼ��ϵͳ�Ѿ�����!");
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
            "'"+faz+"','ϵͳ����',trunc(sysdate),'"+gongysmc+"','��','��','�ٺӷ���',0,'"+zhilb_id+"','"+chec+"','"+meikdwmc+"','"+chec+"',0)";
        }
        System.out.println(sql);
        int r = con.getUpdate(sql);
        if (r == -1) {
            throw new Exception(sql + "\nִ�д���!");
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

    //����ΪRg��������������zhilbtmp
    private void saveZhillsb(Element e2, JDBCcon cn) throws Exception {
        String caiybm = e2.getChildText("samcode");
        if(caiybm==null){
            throw new Exception("�������벻����!");
        }
        String sql="select id from chepbtmp where chec='"+caiybm+"'";
        ResultSet crs=cn.getResultSet(sql);
        if(!crs.next()){
            throw new Exception("�����ϴ����κ�Ϊ"+caiybm+"����������!");
        }
        crs.close();
        // ͬ����������
//		String strDiancmc = e2.getChildText("�糧����");
        String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");// ��ʶҪ���еĲ�����0
        // ��ʾ���ǻ����Ӷ�Ӧ�����ݣ�1��ʾɾ��
        String strLury1 = e2.getChildText("Lury1");// Ʊ�ݺ�
        String strCaiyry = e2.getChildText("Caiyry");// ��Ƥ��
        String strCaiyfs = e2.getChildText("Caiyfs");// ú�����
        String strHuaysj = e2.getChildText("Huaysj");// �ֿ����,
        String strQnetar = e2.getChildText("Qnetar");// �Ա�ţ�ERPϵͳ�γɣ�ȼ��ϵͳ�в�ʹ��
        String strAar = e2.getChildText("Aar");// ȼ��Ʒ������
        String strAd = e2.getChildText("Ad");// �������ڣ�ȼ��ϵͳ�в���chepb.zhongcsj
        String strVdaf = e2.getChildText("Vdaf");// ��������ʱ�䣬chepb.zhongcsj
        String strMt = e2.getChildText("Mt");// �볧����ʱ�䣬chepb.qingcsj
        // double dblZK = noDoubleNullVal(e.getChildText("����ID"));// �۶�
        String strStad = e2.getChildText("Stad");// ë��
        String strAad = e2.getChildText("Aad");// Ƥ��
        String strMad = e2.getChildText("Mad");// �۶�
        String strQbad = e2.getChildText("Qbad");// ��ע
        String strHad = e2.getChildText("Had");// zihilb_id
        String strVad = e2.getChildText("Vad");// ���ƻ����ε���ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strStd = e2.getChildText("Std");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strQgrad = e2.getChildText("Qgrad");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strHuayy = e2.getChildText("Huayy");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strLury2 = e2.getChildText("Lury2");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShenhzt = "1";//e2.getChildText("Shenhzt");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShenhry = e2.getChildText("Shenhry");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strHuaybh = e2.getChildText("Huaybh");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strFcad = e2.getChildText("Fcad");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strHdaf = e2.getChildText("Hdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strQgrdaf = e2.getChildText("Qgrdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShenhrq = e2.getChildText("Shenhrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshry = e2.getChildText("Shangjshry");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshrq = e2.getChildText("Shangjshrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strSdaf = e2.getChildText("Sdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ

        String Qgrd = e2.getChildText("Qgrd");
        String strQgrd = e2.getChildText("Qgrd");// 2016��5��18�� Qiuzw ��ȡ�ӿ��ļ��еġ��������λ��ֵ��
        //--------------------------------------------------------------------------------------------------------------
        sql=" select distinct z.id zhillsb_id,z.zhilb_id from fahb f inner join zhillsb z on f.zhilb_id=z.zhilb_id where f.chec='"+caiybm+"'";
        ResultSet rs = cn.getResultSet(sql);
        String zhillsb_id;
        if (rs.next()) {
//            String id = rs.getString("id");
            zhillsb_id = rs.getString("zhillsb_id");
            //����zhuanmb
            //���²��ñ���100661
            saveBianm(cn, zhillsb_id, caiybm, "100661");
            //������������100662
            saveBianm(cn, zhillsb_id, strBianh, "100662");
            //���»������100663
            saveBianm(cn, zhillsb_id, strHuaybh, "100663");
            //��������
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
                throw new Exception(sql + "\nִ�д���!");
            }
        }else{
            throw new Exception("���ȵ�������!");
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
                // �ⲿ�û���֤ͨ��
                return rs.getString("id");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        } finally {
            //	con.Close();   //20161125ע�ͣ������ط���Ҫ�ò��ܹر�
        }

        return "-1";// �û���֤��ͨ��
    }


    private static String syncCZSJ(Element e, JDBCcon con) {
        // ͬ����������
        StringBuffer sql = new StringBuffer();
        String strChex = e.getChildText("����");
        String strCheph = e.getChildText("��Ƥ��");// ��Ƥ��
        String strMaoz = e.getChildText("ë��");// ë��
        String strPiz = e.getChildText("Ƥ��");// Ƥ��
        String strPiaoz = e.getChildText("����");// Ʊ��
        String strJingz = e.getChildText("����");// ����,
        String strYingd = e.getChildText("ӯ��");// ӯ��
        String strYuns = e.getChildText("����");// ����
        String strKoud = e.getChildText("�۶�");// �۶�
        String strKuid = e.getChildText("����");// ����
        String strChes = e.getChildText("����");// ����
        String strChec = e.getChildText("����");// ����
        String strDiancmc = e.getChildText("�糧����");// �糧���ƣ�DIANCXXB�еġ���ơ�
        strDiancmc = getTableID("JIANC", "DIANCXXB", strDiancmc, con, "ID");
        String strChangb = e.getChildText("����ID");// ����
        String strFahdw = e.getChildText("������λ");// ������λ��MEIKXXB�еġ�ú��λ���ơ���ͨ�����õ�MEIKDQB�е�ID
        strFahdw = getTableID("MEIKDWQC", "MEIKXXB", strFahdw, con, "MEIKDQB_ID");
        String strMeikdw = e.getChildText("ú��λ");// ú��λ\
        //System.out.println("strMeikdw111 ============ " + strMeikdw);
//		strMeikdw = getTableID("MEIKDWQC", "MEIKXXB", strMeikdw, con,"ID");
        double yunsl = getYunsl(strMeikdw);
        //2015��6��27�� qzw ���¼��� ӯ��������
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
        String strFahrq = e.getChildText("��������");// �������ڣ���ʱû���õ�
        String strDaohrq = e.getChildText("��������");// �������ڣ���ʱû���õ�
        String strMeiz = e.getChildText("ú��");// ú��
//            System.out.println("���ƺ�"+strCheph+" ú��:"+strMeiz+"]");
//		strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con,"ID");
        String strFaz = e.getChildText("��վ");// ��վ
        strFaz = getTableID("JIANC", "CHEZXXB", strFaz, con, "ID");
        String strYunsdw = e.getChildText("���䵥λ");// ���䵥λ
//2015��6��29�� qzw �ӡ�ú����Ϣ���ж�ȡú���Ĭ�Ͽھ�
        String strJihkj = e.getChildText("�ƻ��ھ�");// �ƻ��ھ�
//		strJihkj = getTableID("MINGC", "JIHKJB", strJihkj, con,"ID");
//		String strJihkj = getTableID("ID", "MEIKXXB", strMeikdw, con,"JIHKJB_ID");
        String strCheb = e.getChildText("����");// ������ʱû���õ�
        String strJilfs = e.getChildText("������ʽ");// ������ʽ����ʱû���õ�
        String strYunsfs = e.getChildText("���䷽ʽ");// ���䷽ʽ����ʱû���õ�
        String strPiaojh = e.getChildText("Ʊ�ݺ�");// Ʊ�ݺţ���ʱû���õ�
        String strMaozsj = e.getChildText("ë��ʱ��");// ë��ʱ��
        String strPizsj = e.getChildText("Ƥ��ʱ��");// Ƥ��ʱ��
        String strMaozjjy = e.getChildText("ë�ؼ��Ա");// ë�ؼ��Ա
        String strPizjjy = e.getChildText("Ƥ�ؼ��Ա");// Ƥ�ؼ��Ա
        String strMeic = e.getChildText("ú��");// ú��
        strMeic = getTableID("MEICMC", "MEICFQB", strMeic, con, "ID");
        String strZhongchbh = e.getChildText("�س�����");// �س��������
        String strQingchbh = "";//e.getChildText("�ᳵ����");// �ᳵ�������
        String strShouhr = e.getChildText("�ջ���");// �ջ���
        String strBeiz = e.getChildText("��ע");// ��ע
        String piaojh = e.getChildText("������ʶ��");
        String chec = e.getChildText("samcode");
        sql.append("DELETE FROM chepbtmp WHERE piaojh = " + piaojh + ";\n");//ɾ���Ѿ������ĳ�Ƥ
        sql.append("INSERT INTO chepbtmp");
        sql.append("(ID, CHEPH,  meikdwmc, pinz, ");
        sql.append("MAOZ, PIZ, KOUD, YUNS,  YINGD, ZHUANGT, ");
        sql.append("jihkj, MEIGY, yunsdw, qingcjjy, BEIZ, MEICB_ID,zhongchh,qingchh,piaojh,GUOHSJ,DIANCXXB_ID,fahrq,chec)");
        sql.append(" VALUES(");
        sql.append("getnewid(197),"); // id
        sql.append("'" + strCheph + "',"); // ��Ƥ��
//		sql.append("'" + strShouhr + "',"); // �ջ���
//		sql.append(strFahdw + ","); // ������λ
        sql.append(strMeikdw + ","); // ú��λ
        sql.append(strMeiz + ","); // ȼ��Ʒ��
//		sql.append(strPiaoz + ","); // ������
        sql.append(strMaoz + ","); // ë��=ë��-�۶�
        sql.append(strPiz + ","); // Ƥ��
        sql.append(strKoud + ","); // �۶�
        sql.append(strYuns + ","); // ����
//		sql.append(strKuid + ","); // ����
//		sql.append("TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS'),"); // ��ëʱ��
//		sql.append("TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS'),"); // ��Ƥʱ��
        sql.append(strYingd + ","); // ӯ��
        sql.append(0 + ","); // ״̬
        sql.append(strJihkj + ","); // �ƻ��ھ�
        sql.append("'" + strMaozjjy + "',"); // ú��Ա
        sql.append("'" + strYunsdw + "',");// ���˵�λ
        sql.append("'" + strPizjjy + "',");// ���Ա
        sql.append("'" + strBeiz + "',"); // ��ע
        sql.append(strMeic + ","); // ú��
//		sql.append(strChex + ","); // ���
//		sql.append(strChangb + ","); // ����
//		sql.append(-1); // �̵�ú��
        sql.append(",'" + strZhongchbh + "'");
        //2015��6��27�� qzw qichjjbtmp.jingz����Ϊ�жϡ����������Ƿ��˹�����ı��ʹ�ã���������ʵ�ġ����ء�
        sql.append(",'" + strQingchbh + "'," + piaojh + ")\n");
        sql.append(",TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS')");
        sql.append(",197,TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS')," + chec + ");\n");
//		System.out.println("huaboyang456");
        return sql.toString();
    }

    private static String syncCZSJ_YX(Element e, JDBCcon con) {

        // ͬ����������
        StringBuffer sql = new StringBuffer();
        String strChex = e.getChildText("����");
        String strCheph = e.getChildText("��Ƥ��");// ��Ƥ��
        String strMaoz = e.getChildText("ë��");// ë��
        String strPiz = e.getChildText("Ƥ��");// Ƥ��
        String strPiaoz = e.getChildText("����");// Ʊ��
        String strJingz = e.getChildText("����");// ����,
        String strYingd = e.getChildText("ӯ��");// ӯ��
        String strYuns = e.getChildText("����");// ����
        String strKoud = e.getChildText("�۶�");// �۶�
        String strKuid = e.getChildText("����");// ����
        String strChes = e.getChildText("����");// ����
        String strChec = e.getChildText("����");// ����
        String strDiancmc = e.getChildText("�糧����");// �糧���ƣ�DIANCXXB�еġ���ơ�
        strDiancmc = getTableID("JIANC", "DIANCXXB", strDiancmc, con, "ID");
        String strChangb = e.getChildText("����ID");// ����
        String strFahdw = e.getChildText("������λ");// ������λ��MEIKXXB�еġ�ú��λ���ơ���ͨ�����õ�MEIKDQB�е�ID
        strFahdw = getTableID("MEIKDWQC", "MEIKXXB", strFahdw, con, "MEIKDQB_ID");
        String strMeikdw = e.getChildText("ú��λ");// ú��λ
        strMeikdw = getTableID("MEIKDWQC", "MEIKXXB", strMeikdw, con, "ID");
        String strFahrq = e.getChildText("��������");// �������ڣ���ʱû���õ�
        String strDaohrq = e.getChildText("��������");// �������ڣ���ʱû���õ�
        String strMeiz = e.getChildText("ú��");// ú��
//		strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con,"ID");
        String strFaz = e.getChildText("��վ");// ��վ
        strFaz = getTableID("JIANC", "CHEZXXB", strFaz, con, "ID");
        String strYunsdw = e.getChildText("���䵥λ");// ���䵥λ
        String strJihkj = e.getChildText("�ƻ��ھ�");// �ƻ��ھ�
        strJihkj = getTableID("MINGC", "JIHKJB", strJihkj, con, "ID");
        String strCheb = e.getChildText("����");// ������ʱû���õ�
        String strJilfs = e.getChildText("������ʽ");// ������ʽ����ʱû���õ�
        String strYunsfs = e.getChildText("���䷽ʽ");// ���䷽ʽ����ʱû���õ�
        String strPiaojh = e.getChildText("Ʊ�ݺ�");// Ʊ�ݺţ���ʱû���õ�
        String strMaozsj = e.getChildText("ë��ʱ��");// ë��ʱ��
        String strPizsj = e.getChildText("Ƥ��ʱ��");// Ƥ��ʱ��
        String strMaozjjy = e.getChildText("ë�ؼ��Ա");// ë�ؼ��Ա
        String strPizjjy = e.getChildText("Ƥ�ؼ��Ա");// Ƥ�ؼ��Ա
        String strMeic = e.getChildText("ú��");// ú��
        strMeic = getTableID("MEICMC", "MEICFQB", strMeic, con, "ID");
        String strZhongchbh = e.getChildText("�س�����");// �س��������
        //��ȼ��ϵͳ������������ʱ�Ǹ����ᳵ����ж������䷽ʽ��������ᳵ��������䷽ʽΪ�ᳵ��ţ����û�����䷽ʽ��Ϊ����
        //����ѼϪĿǰ״̬����ʱ��������ǽ��ᳵ����ÿ�
        String strQingchbh = "";//e.getChildText("�ᳵ����");// �ᳵ�������
        String strShouhr = e.getChildText("�ջ���");// �ջ���
        String strBeiz = e.getChildText("��ע");// ��ע
        String strZhefl = e.getChildText("�۷���");// �۷���
//		System.out.println("strZhefl = " + strZhefl);
//		System.out.println("strPiz = " + strPiz);
//		System.out.println("strPiaoz = " + strPiaoz);
//		System.out.println("strMaoz = " + strMaoz);
//		System.out.println("strJingz = " + strJingz);
//		System.out.println("strYingd = " + strYingd);
//		System.out.println("strKoud = " + strKoud);
//		System.out.println("strKuid = " + strKuid);
//		System.out.println("strChex = " + strChex);
        if (strMeiz.equals("��ú") || strMeiz.equals("������")) {
//			System.out.println("����Ϊ ����ú����");
            strChangb = "2";
        } else {
//			System.out.println("����Ϊ ������ѼϪ�������޹�˾��");
            strChangb = "1";
        }
        if (!strZhefl.equals("") && !strZhefl.equals("0") && !strZhefl.equals("0.0") && !strZhefl.equals("0.00")) {
//			System.out.println("������");
            String strJingz1 = "" + (double) Math.round(Double.parseDouble(strJingz) * Double.parseDouble(strZhefl) * 100) / 100;//�¾���
            String strMaoz1 = "" + (double) Math.round((Double.parseDouble(strJingz1) + Double.parseDouble(strPiz)) * 100) / 100; //��ë��
            String strPiaoz1 = strJingz1;
            String strKuid1 = "0";
            String strYingd1 = "0";
            String strKoud1 = "0";
            String strChex1 = Integer.parseInt(strChex) + 600 + "";
            String strMeiz1 = "";
            if (strMeiz.equals("��ú")) {
                strMeiz1 = "������";
            } else {
                strMeiz1 = "������";
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
            sql.append("DELETE FROM QICHJJBTMP WHERE XUH = '" + strChex1 + "' AND CHEPH = '" + strCheph + "' AND JIANPSJ = TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS');\n");//ɾ���Ѿ������ĳ�Ƥ
            sql.append("INSERT INTO QICHJJBTMP");
            sql.append("(ID, CHEPH, SHOUHR, MEIKDQB_ID, MEIKXXB_ID, RANLPZB_ID, FAHL, ");
            sql.append("MAOZ, PIZ, KOUD, YUNS, KUID, JIANMSJ, JIANPSJ, YINGD, ZHUANGT, ");
            sql.append("JIHKJB_ID, MEIGY, CHENGYDW, JIANJY, BEIZ, MEICB_ID, XUH, CHANGBB_ID, PANDMC_ID,JILHH,JINGZ,HENGH)");
            sql.append(" VALUES(");
            sql.append("XL_QICHJJBTMP_ID.NEXTVAL,"); // id
            sql.append("'" + strCheph + "',"); // ��Ƥ��
            sql.append("'" + strShouhr + "',"); // �ջ���
            sql.append(strFahdw + ","); // ������λ
            sql.append(strMeikdw + ","); // ú��λ
            sql.append(strMeiz1 + ","); // ȼ��Ʒ��
            sql.append(strPiaoz1 + ","); // ������
            sql.append(strMaoz1 + ","); // ë��=ë��-�۶�
            sql.append(strPiz + ","); // Ƥ��
            sql.append(strKoud1 + ","); // �۶�
            sql.append(strYuns + ","); // ����
            sql.append(strKuid1 + ","); // ����
            sql.append("TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS'),"); // ��ëʱ��
            sql.append("TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS'),"); // ��Ƥʱ��
            sql.append(strYingd1 + ","); // ӯ��
            sql.append(0 + ","); // ״̬
            sql.append(strJihkj + ","); // �ƻ��ھ�
            sql.append("'" + strMaozjjy + "',"); // ú��Ա
            sql.append("'" + strYunsdw + "',");// ���˵�λ
            sql.append("'" + strPizjjy + "',");// ���Ա
            sql.append("'" + strBeiz + "',"); // ��ע
            sql.append(strMeic + ","); // ú��
            sql.append("'" + strChex1 + "',"); // ���
            sql.append(strChangb + ","); // ����
            sql.append(-1); // �̵�ú��
            sql.append(",'" + strZhongchbh + "'");
            sql.append("," + strJingz1 + ",'" + strQingchbh + "');\n");
            strMaoz = "" + (double) Math.round((Double.parseDouble(strMaoz) - Double.parseDouble(strJingz1)) * 100) / 100;
            strJingz = "" + (double) Math.round((Double.parseDouble(strJingz) - Double.parseDouble(strJingz1)) * 100) / 100;
            strPiaoz = "" + (double) Math.round((Double.parseDouble(strPiaoz) - Double.parseDouble(strPiaoz1)) * 100) / 100;
        }
        //����ֱ�Ӵ��ú�֣��Ǹ���ú����ranlpzb���ҵĶ�Ӧid
        strMeiz = getTableID("PINZ", "RANLPZB", strMeiz, con, "ID");
//		System.out.println("strMeiz = " + strMeiz);
//		System.out.println("strPiaoz = " + strPiaoz);
//		System.out.println("strMaoz = " + strMaoz);
//		System.out.println("strJingz = " + strJingz);
//		System.out.println("strYingd = " + strYingd);
//		System.out.println("strKoud = " + strKoud);
//		System.out.println("strKuid = " + strKuid);
//		System.out.println("strChex = " + strChex);
        sql.append("DELETE FROM QICHJJBTMP WHERE XUH = '" + strChex + "' AND CHEPH = '" + strCheph + "' AND JIANPSJ = TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS');\n");//ɾ���Ѿ������ĳ�Ƥ
        sql.append("INSERT INTO QICHJJBTMP");
        sql.append("(ID, CHEPH, SHOUHR, MEIKDQB_ID, MEIKXXB_ID, RANLPZB_ID, FAHL, ");
        sql.append("MAOZ, PIZ, KOUD, YUNS, KUID, JIANMSJ, JIANPSJ, YINGD, ZHUANGT, ");
        sql.append("JIHKJB_ID, MEIGY, CHENGYDW, JIANJY, BEIZ, MEICB_ID, XUH, CHANGBB_ID, PANDMC_ID,JILHH,JINGZ,HENGH)");
        sql.append(" VALUES(");
        sql.append("XL_QICHJJBTMP_ID.NEXTVAL,"); // id
        sql.append("'" + strCheph + "',"); // ��Ƥ��
        sql.append("'" + strShouhr + "',"); // �ջ���
        sql.append(strFahdw + ","); // ������λ
        sql.append(strMeikdw + ","); // ú��λ
        sql.append(strMeiz + ","); // ȼ��Ʒ��
        sql.append(strPiaoz + ","); // ������
        sql.append(strMaoz + ","); // ë��=ë��-�۶�
        sql.append(strPiz + ","); // Ƥ��
        sql.append(strKoud + ","); // �۶�
        sql.append(strYuns + ","); // ����
        sql.append(strKuid + ","); // ����
        sql.append("TO_DATE('" + strMaozsj + "','YYYY-MM-DD HH24:MI:SS'),"); // ��ëʱ��
        sql.append("TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS'),"); // ��Ƥʱ��
        sql.append(strYingd + ","); // ӯ��
        sql.append(0 + ","); // ״̬
        sql.append(strJihkj + ","); // �ƻ��ھ�
        sql.append("'" + strMaozjjy + "',"); // ú��Ա
        sql.append("'" + strYunsdw + "',");// ���˵�λ
        sql.append("'" + strPizjjy + "',");// ���Ա
        sql.append("'" + strBeiz + "',"); // ��ע
        sql.append(strMeic + ","); // ú��
        sql.append("'" + strChex + "',"); // ���
        sql.append(strChangb + ","); // ����
        sql.append(-1); // �̵�ú��
        sql.append(",'" + strZhongchbh + "'");
        sql.append("," + strJingz + ",'" + strQingchbh + "');\n");
//		System.out.println("���մ�ȼ�ϣ�");
        return sql.toString();
    }

    //����Ƥ��Ϣд��chepbtmp
    private static String syncCHEPBTMP(Element e2) {
//		System.out.println("���ݲ���chepbtmp");
        StringBuffer sql = new StringBuffer();
        String strDiancmc = e2.getChildText("�糧����");
//		String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");// ��ʶҪ���еĲ�����0
//		// ��ʾ���ǻ����Ӷ�Ӧ�����ݣ�1��ʾɾ��
//		String strLury1 = e2.getChildText("Lury1");// Ʊ�ݺ�
//		String strCaiyry = e2.getChildText("Caiyry");// ��Ƥ��
//		String strCaiyfs = e2.getChildText("Caiyfs");// ú�����
//		String strHuaysj = e2.getChildText("Huaysj");// �ֿ����,
//		String strQnetar = e2.getChildText("Qnetar");// �Ա�ţ�ERPϵͳ�γɣ�ȼ��ϵͳ�в�ʹ��
//		String strAar = e2.getChildText("Aar");// ȼ��Ʒ������
//		String strAd = e2.getChildText("Ad");// �������ڣ�ȼ��ϵͳ�в���chepb.zhongcsj
//		String strVdaf = e2.getChildText("Vdaf");// ��������ʱ�䣬chepb.zhongcsj
//		String strMt = e2.getChildText("Mt");// �볧����ʱ�䣬chepb.qingcsj
//		// double dblZK = noDoubleNullVal(e.getChildText("����ID"));// �۶�
//		String strStad = e2.getChildText("Stad");// ë��
//		String strAad = e2.getChildText("Aad");// Ƥ��
//		String strMad = e2.getChildText("Mad");// �۶�
//		String strQbad = e2.getChildText("Qbad");// ��ע
//		String strHad = e2.getChildText("Had");// zihilb_id
//		String strVad = e2.getChildText("Vad");// ���ƻ����ε���ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strStd = e2.getChildText("Std");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strQgrad = e2.getChildText("Qgrad");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strHuayy = e2.getChildText("Huayy");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strLury2 = e2.getChildText("Lury2");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strShenhzt = e2.getChildText("Shenhzt");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strShenhry = e2.getChildText("Shenhry");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strHuaybh = e2.getChildText("Huaybh");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strFcad = e2.getChildText("Fcad");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strShangjshzt = e2.getChildText("Shangjshzt");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strHdaf = e2.getChildText("Hdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strQgrdaf = e2.getChildText("Qgrdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strShenhrq = e2.getChildText("Shenhrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strShangjshry = e2.getChildText("Shangjshry");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strShangjshrq = e2.getChildText("Shangjshrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		String strSdaf = e2.getChildText("Sdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        // ����Ƥ��Ϣд����ʱ��(chepbtmp)
        List lstChildrenRecords = e2.getChild("�˵�").getChildren();// ÿ���������¶�Ӧ�ĳ�Ƥ����
        int intListSize = lstChildrenRecords.size();
        for (int j = 0; j < intListSize; j++) {
            Element e = (Element) lstChildrenRecords.get(j);
            String strPizsj = e.getChildText("ë��ʱ��");
            String strCheph = e.getChildText("��Ƥ��");
            String strqingcsj = e.getChildText("Ƥ��ʱ��");
            //Ƥëʱ�䲻��Ӧ
            sql.append("INSERT INTO CHEPBTMP (ID,CHEPH,GUOHSJ,PIAOJH,DIANCXXB_ID,fahrq) VALUES (XL_CHEPB_ID.NEXTVAL,");
            sql.append("'" + strCheph + "'");
            sql.append(",TO_DATE('" + strPizsj + "','YYYY-MM-DD HH24:MI:SS')");
            sql.append(",'" + strBianh + "'");
            sql.append(",(SELECT ID FROM DIANCXXB WHERE JIANC = '" + strDiancmc + "'),TO_DATE('" + strqingcsj + "','YYYY-MM-DD HH24:MI:SS'));\n");
        }

        return sql.toString();
    }

    private String getZhilbtmpId(String zhilb_id, String bum) {
        //˫�����ʱ�򣬸���zhilb_id���ҵ��������Ŷ�Ӧzhilbtmp��id
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
                // �ⲿ�û���֤ͨ��
                return true;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            con.Close();
        }

        return false;// �û���֤��ͨ��
    }

    private static void bulidXMLData(String fileType, String xml) {
        try {
            String sql = "SELECT ZHI FROM XITXXB WHERE DUIXM = '"
                    + "��Ԫ�����ļ����ݸ�Ŀ¼' AND SHIFSY=1";
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
//                    + "�ӿ��ļ����ݸ�Ŀ¼' AND SHIFSY=1";
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
            outputter.output(document, writer);// ���ݷ��ر����ļ�
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


    //��ȡfahbtmp��qichjjbtmp��zhilb_id
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
//			sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n" +		//���µ糧������
//					" FROM FAHBTMP\n " +
//					"WHERE (to_char(YUNDLRSJ,'yyyy-mm-dd HH24'), CHEPH) " +
//					"IN (SELECT to_char(GUOHSJ,'yyyy-mm-dd HH24'), CHEPH FROM CHEPBTMP)";
            sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n" +            //����糧ʹ���˵�¼��ʱ��
                    " FROM FAHBTMP\n" +
                    " WHERE (TRUNC(YUNDLRSJ), CHEPH) IN (SELECT TRUNC(GUOHSJ), CHEPH FROM CHEPBTMP)";

//			sql = "SELECT DISTINCT NVL(ZHILB_ID, 0) AS ZHILB_ID\n"+			//�����糧
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

    //��ȡԭzhilb��caiysj
    private static String yuanCaiYSJ(String zhilb_Id) {
        JDBCcon con = new JDBCcon();
        String caiYSJ = "";
        ResultSet rs = con.getResultSet("SELECT TO_CHAR(CAIYSJ,'yyyy-mm-dd hh24:mi:ss') as CAIYSJ FROM ZHILB WHERE ID = '" + zhilb_Id + "'");
        try {
            while (rs.next()) {
                caiYSJ = rs.getString("CAIYSJ");
//				System.out.println("ԭcaiYSJ = "+caiYSJ);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return caiYSJ;
    }

    //��ȡԭzhilb��caiyry
    private static String yuanCaiYRY(String zhilb_Id) {
        JDBCcon con = new JDBCcon();
        String caiYRY = "";
        ResultSet rs = con.getResultSet("SELECT CAIYRY FROM ZHILB WHERE ID = '" + zhilb_Id + "'");
        try {
            while (rs.next()) {
                caiYRY = rs.getString("CAIYRY");
//				System.out.println("ԭcaiYRY = "+caiYRY);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return caiYRY;
    }


    //�ж�fahbtmp���Ƿ���zhilb_id
    private int panDuanFahbtmp(Element e1) {
        int result = 0;
        JDBCcon con = new JDBCcon();
        List lstChildrenRecords = e1.getChild("�˵�").getChildren();// ÿ���������¶�Ӧ�ĳ�Ƥ����
        int intListSize = lstChildrenRecords.size();
        Element e2 = (Element) lstChildrenRecords.get(0);
        String strCheph = e2.getChildText("��Ƥ��");
        String strqingcsj = e2.getChildText("Ƥ��ʱ��");
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

    //����ΪRg��������������zhilbtmp
    private static String insertZhilbtmpRg(Element e2, String zhilb_Id) {
        // ͬ����������
        StringBuffer sql = new StringBuffer();
//		String strDiancmc = e2.getChildText("�糧����");
        String strCaiysj = e2.getChildText("Caiysj");
        String strBianh = e2.getChildText("Bianh");// ��ʶҪ���еĲ�����0
        // ��ʾ���ǻ����Ӷ�Ӧ�����ݣ�1��ʾɾ��
        String strLury1 = e2.getChildText("Lury1");// Ʊ�ݺ�
        String strCaiyry = e2.getChildText("Caiyry");// ��Ƥ��
        String strCaiyfs = e2.getChildText("Caiyfs");// ú�����
        String strHuaysj = e2.getChildText("Huaysj");// �ֿ����,
        String strQnetar = e2.getChildText("Qnetar");// �Ա�ţ�ERPϵͳ�γɣ�ȼ��ϵͳ�в�ʹ��
        String strAar = e2.getChildText("Aar");// ȼ��Ʒ������
        String strAd = e2.getChildText("Ad");// �������ڣ�ȼ��ϵͳ�в���chepb.zhongcsj
        String strVdaf = e2.getChildText("Vdaf");// ��������ʱ�䣬chepb.zhongcsj
        String strMt = e2.getChildText("Mt");// �볧����ʱ�䣬chepb.qingcsj
        // double dblZK = noDoubleNullVal(e.getChildText("����ID"));// �۶�
        String strStad = e2.getChildText("Stad");// ë��
        String strAad = e2.getChildText("Aad");// Ƥ��
        String strMad = e2.getChildText("Mad");// �۶�
        String strQbad = e2.getChildText("Qbad");// ��ע
        String strHad = e2.getChildText("Had");// zihilb_id
        String strVad = e2.getChildText("Vad");// ���ƻ����ε���ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strStd = e2.getChildText("Std");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strQgrad = e2.getChildText("Qgrad");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strHuayy = e2.getChildText("Huayy");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strLury2 = e2.getChildText("Lury2");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShenhzt = "1";//e2.getChildText("Shenhzt");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShenhry = e2.getChildText("Shenhry");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strHuaybh = e2.getChildText("Huaybh");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strFcad = e2.getChildText("Fcad");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strHdaf = e2.getChildText("Hdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strQgrdaf = e2.getChildText("Qgrdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShenhrq = e2.getChildText("Shenhrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshry = e2.getChildText("Shangjshry");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshrq = e2.getChildText("Shangjshrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strSdaf = e2.getChildText("Sdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
//		List lstChildrenRecords = e2.getChild("�˵�").getChildren();// ÿ���������¶�Ӧ�ĳ�Ƥ����
//		int intListSize = lstChildrenRecords.size();
//		Element e3 = (Element) lstChildrenRecords.get(0);
//		String strCheph = e3.getChildText("��Ƥ��");
//		String strqingcsj=e3.getChildText("Ƥ��ʱ��");
        String strQgrd = e2.getChildText("Qgrd");// 2016��5��18�� Qiuzw ��ȡ�ӿ��ļ��еġ��������λ��ֵ��

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
        sql.append("'����'," + strQgrd + ");\n");

        return sql.toString();
    }

    //����ΪJc��������������zhilbtmp���Ҹ��� zhilb
    private static String insertZhilbtmpJc(Element e2, String zhilb_Id) {
        // ͬ����������
        StringBuffer sql = new StringBuffer();
//		String strDiancmc = e2.getChildText("�糧����");
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
//		List lstChildrenRecords = e2.getChild("�˵�").getChildren();// 
//		int intListSize = lstChildrenRecords.size();
//		Element e3 = (Element) lstChildrenRecords.get(0);
//		String strCheph = e3.getChildText("��Ƥ��");
//		String strqingcsj=e3.getChildText("Ƥ��ʱ��");
        String strQgrd = e2.getChildText("Qgrd");// 2016��5��18�� Qiuzw ��ȡ�ӿ��ļ��еġ��������λ��ֵ��

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
        sql.append("'����'," + strQgrd + ");\n");

//		sql.append("update zhilbtmp set HUAYBH='" + strHuaybh + "' where zhilb_id="+zhilb_Id+";\n");
//		String yuanCaiYSJ = yuanCaiYSJ(zhilb_Id);
//		String yuanCaiYRY = yuanCaiYRY(zhilb_Id);

        //����zhilb
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

    //����ΪCc��������������zhilbtmp
    private static String insertZhilbtmpCc(Element e2, String zhilb_Id) {
        // ͬ����������
        StringBuffer sql = new StringBuffer();
//		String strDiancmc = e2.getChildText("�糧����");
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
        // double dblZK = noDoubleNullVal(e.getChildText("����ID"));//
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
        String strShangjshzt = "0";//e2.getChildText("Shangjshzt");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strHdaf = e2.getChildText("Hdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strQgrdaf = e2.getChildText("Qgrdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShenhrq = e2.getChildText("Shenhrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshry = e2.getChildText("Shangjshry");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strShangjshrq = e2.getChildText("Shangjshrq");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        String strSdaf = e2.getChildText("Sdaf");// ���ƻ����ε��¶ȣ���ȼ��ϵͳ�еĲ���ʱ���Ӧ
        List lstChildrenRecords = e2.getChild("�˵�").getChildren();// ÿ���������¶�Ӧ�ĳ�Ƥ����
        int intListSize = lstChildrenRecords.size();
        Element e3 = (Element) lstChildrenRecords.get(0);
        String strCheph = e3.getChildText("��Ƥ��");
        String strqingcsj = e3.getChildText("Ƥ��ʱ��");
        String strQgrd = e2.getChildText("Qgrd");// 2016��5��18�� Qiuzw ��ȡ�ӿ��ļ��еġ��������λ��ֵ��

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
        sql.append("'����'," + strQgrd + ");\n");

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
        // ��˫���������д��zhilb��zhilbtmp
        // ͬ����������
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
        String strQgrd = e2.getChildText("Qgrd");// 2016��5��18�� Qiuzw ��ȡ�ӿ��ļ��еġ��������λ��ֵ��
        // ��Ϊ�����ŵı���ֵ��bum = null��leib = ����������������
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
//			System.out.println("strShangjshrqΪ�س�");
//			sql.append("'',");
//		}else{
//			System.out.println("strShangjshrq��Ϊ��");
//			sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
//		}
        sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
        sql.append("'" + strSdaf + "',");
        sql.append("null,");
        sql.append("'" + leib + "'," + strQgrd + ");\n");

        if (leib.equals("����")) {
            //����zhilbtmp
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
//			System.out.println("Ϊ����ʱstrShangjshrq ="+strShangjshrq+"test");
            sql.append("to_date('" + strShangjshrq + "','yyyy-mm-dd'),");
            sql.append("'" + strSdaf + "'");
            sql.append(",'" + bum + "',null," + strQgrd + ");\n");

        }


        return sql.toString();
    }


}
