

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.axis.utils.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.tools.RandomGUID ;

import com.zhiren.webservice.*;
public class HuayInterFace {
    /**
     * @author zhangl
     */
    private static final String error000="1,000,接收成功";
    private static final String error001="-1,001,XMLData数据转换为gb312编码时出错";
    private static final String error002="-1,002,文档不符合dom规范,可能是发送配置表中sql的字段为函数时没有写别名";
    private static final String error003="-1,003,双方没有安照接口协议传输数据，请检查发送接收的配置，例如发送数据的日期时间类型必须用格式字符串，接收端编码类型字段的配置等或远程数据库编码重复或违反唯一约束条件";//
    private static final String error004="-1,004,执行远程的sql时远程服务器数据库连接失败";
    private static final String error005="-1,005,执行远程的sql时出错，可能是客户端生成的sql语句不符合规范，请检查发送配置表";
    private static final String error007="-1,007,删除远程数据时没有找到主键字段名称，可能是远程接收配置没有设置主键名称!";
    private static final String error008="1,008,删除0条数据，也就是远程数据与本地数据没有同步";
    private static final String error009="-1,009,用户名称不在，你的电厂还没有在集团注册用户";
    private static final String error010="-1,010,用户账户的密码错误请检查系统密码设置";
    private static final String error011="-1,011,接收数据库的接收配置有不识别的类型";
    private static final String error013="-1,013,发送端与接收端配置的字段个数不一致。";
    private static final String error014="-1,014,未知异常。";


    private static final String rizPath="d:/zhiren/";// 发送日志路径
    private String user;
    private String password;
    private String endpointAddress;

    public static byte[] getFahInfo_jt(String dcId, String user, String password, String bianm){
        //业务处理
        JDBCcon cn = new JDBCcon();
        byte[] xml_by = null ;
        String message="";
        String sql = "";
        dcId="938";
        sql = "SELECT f.id,m.mingc AS meikmc,p.mingc AS meizmc,c.mingc AS faz,(f.maoz-f.piz) meil,'' AS caiyr,\n" +
                "       '' AS zhiyr,to_char(cy.caiyrq,'yyyy-mm-dd') AS caiyrq,f.chec,f.ches,cheph.bg||'-'||cheph.sm AS cheh\n" +
                "       FROM fahb f,zhilb z,meikxxb m,chezxxb c,pinzb p,zhillsb zl,zhuanmb zm,caiyb cy,\n" +
                "       (SELECT f.id,MAX(cheph) bg,MIN(cheph) sm from chepb c,fahb f\n" +
                "       WHERE c.fahb_id = f.id\n" +
                "       GROUP BY f.id) cheph\n" +
                "       WHERE f.zhilb_id = z.id(+) AND f.meikxxb_id = m.id AND f.faz_id = c.id AND f.pinzb_id = p.id\n" +
                "       and f.zhilb_id = zl.zhilb_id and zl.id = zm.zhillsb_id and cy.zhilb_id = f.zhilb_id \n" +
                "	AND f.id = cheph.id AND zm.bianm = '"+bianm+"'  and f.diancxxb_id = "+dcId;

        ResultSet rs = cn.getResultSet(sql);
        try{
            if(rs.next()){
                String xml = "";
                //调用CreateXml方法，对记录集rs生成XML文件
                xml=CreateXml(cn.getResultSet(sql));
                try{
                    xml_by=xml.getBytes();//tencryptByDES(xml.getBytes());//对xml进行DES加密

                }catch(Exception e){
                    e.printStackTrace();
                    message=error003;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            message=error005;
        }
        return xml_by;
    }


    //	加密方法
    private static byte[] encryptByDES(byte[] bytP) throws Exception {
        String key="KINGROCK";
        String iv="ZHIRENWL";
        byte[] key_byte= Str2Byt(key);
        byte[] iv_byte= Str2Byt(iv);
        DESKeySpec desKS = new DESKeySpec(key_byte);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey sk = skf.generateSecret(desKS);
        IvParameterSpec ivSpec = new IvParameterSpec (iv_byte);
        Cipher cip = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cip.init(Cipher.ENCRYPT_MODE, sk, ivSpec);
        //System.out..println("JMID="+cip.doFinal(bytP));
        return cip.doFinal(bytP);
    }

    private static byte[] Str2Byt(String str) {
        byte[] bt=null;
        char[] chars = str.toCharArray();
        bt = new byte[chars.length];
        for(int i=0;i<chars.length;i++) {
            bt[i] = (byte)chars[i];
        }
        return bt;
    }

    private static String CreateXml(ResultSet rs){
        String xmlAray="";//没有记录客户端异常。。。。。
        // TODO 自动生成方法存根
        Element root = new Element("data");
        try {
            while(rs.next()){
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
                    root.addContent(new Element(rs.getMetaData().getColumnName(i)).addContent(rs.getString(i)));
                }
            }
            File file=new File(rizPath) ;
            FileWriter writer = new FileWriter(file.getAbsolutePath()+"Fahxx.xml");
            XMLOutputter outputter = new XMLOutputter();
            Format format=Format.getPrettyFormat();
            format.setEncoding("gb2312");
//				format.setOmitDeclaration(true);
            outputter.setFormat(format);
            outputter.output(root, writer);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            outputter.output(root, bo);
            xmlAray=bo.toString();
            //xmlAray=bo.toByteArray();

        }catch (Exception e) {
            e.printStackTrace();
        }

        return xmlAray;
    }
    public static String getNewId(JDBCcon cn){
        String id=null;
        String sql="select  getnewid(938) id from dual";
        ResultSet rs=cn.getResultSet(sql);
        try {
            if(rs.next()){
                id=rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    public static void saveBianm(JDBCcon cn,String zhillsb_id,String bianm,String zhuanmlb_id) throws SQLException {
        String sql="select id from zhuanmb where zhillsb_id="+zhillsb_id+" and zhuanmlb_id="+zhuanmlb_id;
        ResultSet rs=cn.getResultSet(sql);
        if(rs.next()){
            sql="update zhuanmb set bianm='"+bianm+"' where id="+rs.getString("id");
        }else {
            sql="insert into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id) values("+getNewId(cn)+","+zhillsb_id+",'"+bianm+"',"+zhuanmlb_id+")";
        }
        rs.close();
        cn.getUpdate(sql);
    }
    public static String getPathName(String dcId,String bianm) throws Exception {
        JDBCcon cn = new JDBCcon();
        String bakFilePath = "D:/xmlbak";
        RandomGUID guid = new RandomGUID();
        String MD5 = guid.valueAfterMD5;
        File file = new File(bakFilePath);
        String path=file.getAbsolutePath() + "/"
                + bianm + "-" + dcId + "-" + MD5 + ".xml";
        cn.Close();
        return  path;
    }
    public static String setHuayxx_jt(String dcId, String user, String password,String bianm,  byte[] XMLData){
        String message = "";
        JDBCcon cn = new JDBCcon();
        //---------------------------------输出传入参数-------------------------------------------------------------------
        BufferedOutputStream bos;
        try {
            String pathname=getPathName(dcId,bianm);
            bos = new BufferedOutputStream(new FileOutputStream(new File(pathname)));
            bos.write(XMLData);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------------------------------------------------------
        try {
            //			业务处理
            //1.解析xml
            // 0,解析xml取出数据1,取出格式设置2,构造table、values
            String xmlstr = new String(XMLData, "GB2312");
            StringReader sr = new StringReader(xmlstr);
            InputSource is = new InputSource(sr);
            Document doc = (new SAXBuilder()).build(is);// 解读每一个数据帧文件
            Element root = doc.getRootElement();
            String zhiybm=root.getChildText("bianm");
            String huaybh=root.getChildText("huaybh");
            String huayrq=root.getChildText("huayrq");
            String mt=root.getChildText("mt");
            String mad=root.getChildText("mad");
            String aad=root.getChildText("aad");
            String aar=root.getChildText("aar");
            String ad=root.getChildText("ad");
            String vad=root.getChildText("vad");
            String vdaf=root.getChildText("vdaf");
            String stad=root.getChildText("stad");
            String std=root.getChildText("std");
            String had=root.getChildText("had");
            String qbad=root.getChildText("qbad");
            String qgrad=root.getChildText("qgrad");
            String qgrd=root.getChildText("qgrd");
            String qnetar_mj=root.getChildText("qnetar_mj");
            String qnetar_kcal=root.getChildText("qnetar_kcal");
            String shenhr=root.getChildText("shenhr");
            String huayy=root.getChildText("huayy");
            String shenhsj=root.getChildText("shenhsj");
            String beiz=root.getChildText("beiz");
            String sdaf=root.getChildText("sdaf");
            String hdaf=root.getChildText("hdaf");
            String fcad=root.getChildText("fcad");
            String qgraddaf=root.getChildText("qgraddaf");
            //2.根据制样编号查jk_assaycode找到采用编码匹配fahb中的车次
            String sql="select distinct j.samcode caiybm,z.id zhillsb_id from jk_assaycode j \n" +
                    "inner join fahb f on j.samcode=f.chec\n" +
                    "inner join zhillsb z on f.zhilb_id=z.zhilb_id\n" +
                    "where j.assaycode='"+zhiybm+"'";
            ResultSet rs = cn.getResultSet(sql);
            while(rs.next()){
                String zhillsb_id=rs.getString("zhillsb_id");
                String caiybm=rs.getString("caiybm");
                //更新zhuanmb
                //更新采用编码100661
                saveBianm(cn,zhillsb_id,caiybm,"100661");
                //更新制样编码100662
                saveBianm(cn,zhillsb_id,zhiybm,"100662");
                //更新化验编码100663
                saveBianm(cn,zhillsb_id,huaybh,"100663");
                //更新质量
                sql = "update zhillsb set "
                        + "huaysj = to_date('"+huayrq+"', 'yyyy-mm-dd'),  "
                        + "qnet_ar = round_new("+  qnetar_mj + ",2),\n"
                        + "aar = " +  aar+ ",\n"
                        + "ad = " +  ad + ",\n"
                        + "vdaf = " +  vdaf + ",\n"
                        + "mt = round_new(" + mt + ",1),\n"
                        + "stad = " + stad + ",\n"
                        + "aad = " + aad + ",\n"
                        + "mad = " + mad + ",\n"
                        + "qbad = " + qbad + ",\n"
                        + "had = " +had + ",\n"
                        + "vad = " + vad + ",\n"
                        + "fcad = " + fcad + ",\n"
                        + "std = " + std + ",\n"
                        + "qgrad = round_new(" + qgrad + ",3),\n"
                        + "hdaf = " + hdaf + ",\n"
                        + "qgrad_daf = " + qgraddaf + ",\n"
                        + "sdaf = " + sdaf + ",\n"
                        // + "har = " + elmt.getText() + ",\n"
                        + "qgrd = round_new(" + qgrd + ",3),\n"
                        +" lury = '"+shenhr+"',huayy = '"+huayy+"',beiz = '"+beiz+"',shenhzt=3  " +
                        "where id = "+zhillsb_id;
                cn.getUpdate(sql);
            }
            rs.close();
            message = "true" ;
        } catch (Exception e) {
            e.printStackTrace();
            message=e.getMessage();
        }finally {
            cn.Close();
        }
        return message ;
    }

    public static boolean isHuit_jt(String dcId, String user, String password, String bianm){

        boolean biaoz = false;
        String sql = "";
        String message = error000;
        JDBCcon con = new JDBCcon();
        sql = "SELECT zl.shenhzt FROM zhilb z,fahb f ,zhillsb zl ,zhuanmb zm\n" +
                "WHERE f.zhilb_id = z.id(+) and zl.zhilb_id = f.zhilb_id and zm.zhillsb_id = zl.id\n" +
                "and zm.zhuanmlb_id = 100663 and zm.bianm = '"+bianm+"' and f.diancxxb_id ="+dcId;

        ResultSet rs = con.getResultSet(sql);
        try {
            if(rs.next()){
                if(rs.getInt("shenhzt")>=5){
                    biaoz = false;
                    message = "数据已审核，不可以进行回退操作";
                }else{
                    biaoz = true;
                    message = "数据未审核，可以进行回退操作";
                }
            }
        } catch (Exception e4) {
            con.rollBack();
            message = error014;// zhixzt cuowlb zhixbz
        } finally {
            con.Close();
        }
        return biaoz ;
    }

    private void loadDateRec(String sql,List list) throws SQLException{
        JDBCcon con=new JDBCcon();
        ResultSet rs=con.getResultSet(sql);

        while(rs.next()){//数据记录集合
            List zidList=new ArrayList();
            for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
                zidList.add(new String[]{rs.getMetaData().getColumnName(i),rs.getString(i)});
            }
            list.add(zidList);
        }
    }

//		public static void main(String[] args) {
//			String id = "476";
//			String user = "ads";
//			String pasd = "123456";
//			String bianm = "Q127551";
//			HuayInter h = new HuayInter();
////			h.setHuayxx_jt(id, user, pasd, bianm);
//			String srr=
//				"<data>\n" +
//				"<huayrq>2012-01-01</huayrq>\n" +
//				"<mt>14.98</mt>\n" +
//				"<mad>9.8</mad>\n" +
//				"<aad>29.1</aad>\n" +
//				"<aar>27.1</aar>\n" +
//				"<ad>15</ad>\n" +
//				"<vad>14</vad>\n" +
//				"<vdaf>13</vdaf>\n" +
//				"<stad>1.2</stad>\n" +
//				"<std>0.9</std>\n" +
//				"<had>3</had>\n" +
//				"<qbad>18.2</qbad>\n" +
//				"<qgrad>22</qgrad>\n" +
//				"<qgrd>23</qgrd>\n" +
//				"<qnetar_mj>3447</qnetar_mj>\n" +
//				"<qnetar_kcal>18.23</qnetar_kcal>\n" +
//				"<shenhr>审核人</shenhr>\n" +
//				"<huayy>化验员</huayy>\n" +
//				"<shenhsj>2012-01-10</shenhsj>\n" +
//				"<beiz>备注</beiz>\n" +
//				"</data>";
    //
//			byte [] a=srr.getBytes();
//			h.setHuayxx_jt(id,user,pasd,bianm,a);
//		}

}
