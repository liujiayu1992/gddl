package com.zhiren.webservice;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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

public class HuayInterFace_QL {
	/**
	 * @author zhangl
	 * 
	 *
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
		
		public static String setHuayxx_jt(String dcId, String user, String password,String bianm,  byte[] XMLData){
//			业务处理
			String biaoz = "";
			String sql = "";
			JDBCcon cn = new JDBCcon();
			String message = "";
			JDBCcon con = new JDBCcon();
			Element elhuayrq = null;
			Element elmt = null;
			Element elmad = null;
			Element elaad = null;
			Element elaar = null;
			Element elad = null;
			Element elvad = null;
			Element elvdaf = null;
			Element elstad = null;
			Element elstd = null;
			Element elhad = null;
			Element elqbad = null;
			Element elqgrad = null;
			Element elqgrd = null;
			Element elqnetar_mj = null;
			Element elqnetar_kcal = null;
			Element elsdaf = null;
			Element elhdaf = null;
			Element elfcad= null;
			Element elshenhr = null;
			Element elhuayy = null;
			Element elshenhsj = null;
			Element elbeiz = null;
			Element elqgraddaf=null;

			boolean HasBianm=false;

			// 0,解析xml取出数据1,取出格式设置2,构造table、values
			try {
				String xmlstr = new String(XMLData, "GB2312");
				StringReader sr = new StringReader(xmlstr);
				InputSource is = new InputSource(sr);
				Document doc = (new SAXBuilder()).build(is);// 解读每一个数据帧文件
				Element root = doc.getRootElement();
				List Table = root.getChildren();

				elhuayrq = (Element) Table.get(0);
				elmt = (Element) Table.get(1);
				elmad = (Element) Table.get(2);
				elaad = (Element) Table.get(3);
				elaar = (Element) Table.get(4);
				elad = (Element) Table.get(5);
				elvad = (Element) Table.get(6);
				elvdaf = (Element) Table.get(7);
				elstad = (Element) Table.get(8);
				elstd = (Element) Table.get(9);
				elhad = (Element) Table.get(10);
				elqbad = (Element) Table.get(11);
				elqgrad = (Element) Table.get(12);
				elqgrd = (Element) Table.get(13);
				elqnetar_mj = (Element) Table.get(14);
				elqnetar_kcal = (Element) Table.get(15);
				elshenhr = (Element) Table.get(16);
				elhuayy = (Element) Table.get(17);
				elshenhsj = (Element) Table.get(18);
				elbeiz = (Element) Table.get(19);
				elsdaf = (Element) Table.get(20);
				elhdaf = (Element) Table.get(21);
				elfcad= (Element) Table.get(22);
				elqgraddaf= (Element) Table.get(23);

				String Sql = 
					"select f.zhilb_id from fahb f,zhuanmb zm,zhillsb zl,zhilb z\n" +
					"where  f.diancxxb_id = 938 and zl.shenhzt<=3 and f.zhilb_id=z.id(+)\n" + 
					"and f.zhilb_id = zl.zhilb_id and zl.id = zm.zhillsb_id\n" + 
					"and zm.bianm='"+bianm+"'";

				ResultSet rs = cn.getResultSet(Sql);
				while(rs.next()){
					HasBianm=true;
					sql = "update zhillsb set "
						+ "huaysj = to_date('"+elhuayrq.getText()+"', 'yyyy-mm-dd'),  "
						+ "qnet_ar = "+  elqnetar_mj.getText() + ",\n" 
						+ "aar = " +  elaar.getText()+ ",\n"
						+ "ad = " +  elad.getText() + ",\n"
						+ "vdaf = " +  elvdaf.getText() + ",\n"
						+ "mt = " + elmt.getText() + ",\n"
						+ "stad = " + elstad.getText() + ",\n"
						+ "aad = " + elaad.getText() + ",\n"
						+ "mad = " + elmad.getText() + ",\n"
						+ "qbad = " + elqbad.getText() + ",\n"
						+ "had = " + elhad.getText() + ",\n"
						+ "vad = " + elvad.getText() + ",\n"
						+ "fcad = " + elfcad.getText() + ",\n"
						+ "std = " + elstd.getText() + ",\n"
						+ "qgrad = " + elqgrad.getText() + ",\n"
						+ "hdaf = " + elhdaf.getText() + ",\n"
						+ "qgrad_daf = " + elqgraddaf.getText() + ",\n"
						+ "sdaf = " + elsdaf.getText() + ",\n"
						// + "har = " + elmt.getText() + ",\n"
						+ "qgrd = " + elqgrd.getText() + ",\n"
						+" lury = '"+elshenhr.getText()+"',huayy = '"+elhuayy.getText()+"',beiz = '"+elbeiz.getText()+"',shenhzt=3  where zhilb_id = "+rs.getString("zhilb_id");

					int flag = con.getUpdate(sql);
					if(flag == 1){
						message = "true" ;
					}else{
						con.rollBack();
						message = "更新0行数据，传输失败!";// zhixzt cuowlb zhixbz
						return "";
					}
				}
				if(!HasBianm){
					message="未找到编码"+bianm+"的数据!";
				}
			} catch (IOException e) {
				con.rollBack();
				message = error001;// zhixzt cuowlb zhixbz
			} catch (JDOMException e1) {
				e1.printStackTrace();
				con.rollBack();
				message = error002;// zhixzt cuowlb zhixbz
			} catch (Exception e4) {
				con.rollBack();
				message = error014;// zhixzt cuowlb zhixbz
			} finally {
				con.Close();
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
