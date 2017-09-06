
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

/**
 * @author 
 * 
 *
 */
public class Test  {
	private static final String error000="1,000,���ճɹ�";
	private static final String error001="-1,001,XMLData����ת��Ϊgb312����ʱ����";
	private static final String error002="-1,002,�ĵ�������dom�淶,�����Ƿ������ñ���sql���ֶ�Ϊ����ʱû��д����";
	private static final String error003="-1,003,˫��û�а��սӿ�Э�鴫�����ݣ����鷢�ͽ��յ����ã����緢�����ݵ�����ʱ�����ͱ����ø�ʽ�ַ��������ն˱��������ֶε����õȻ�Զ�����ݿ�����ظ���Υ��ΨһԼ������";//
	private static final String error004="-1,004,ִ��Զ�̵�sqlʱԶ�̷��������ݿ�����ʧ��";
	private static final String error005="-1,005,ִ��Զ�̵�sqlʱ�����������ǿͻ������ɵ�sql��䲻���Ϲ淶�����鷢�����ñ�";
	private static final String error007="-1,007,ɾ��Զ������ʱû���ҵ������ֶ����ƣ�������Զ�̽�������û��������������!";
	private static final String error008="1,008,ɾ��0�����ݣ�Ҳ����Զ�������뱾������û��ͬ��";
	private static final String error009="-1,009,�û����Ʋ��ڣ���ĵ糧��û���ڼ���ע���û�";
	private static final String error010="-1,010,�û��˻��������������ϵͳ��������";
	private static final String error011="-1,011,�������ݿ�Ľ��������в�ʶ�������";
	private static final String error013="-1,013,���Ͷ�����ն����õ��ֶθ�����һ�¡�";
	private static final String error014="-1,014,δ֪�쳣��";
	private static final String error015="-1,015,�ύ�Ļ�������ȼ��ϵͳ�в�����,��˶ԡ�";

	
	private static final String rizPath="d:/zhiren/";// ������־·��
	private String user;
	private String password;
	private String endpointAddress;
//	
	
	
	public byte[] getFahInfo_jt(String dcId, String bianm){
				//ҵ����
		JDBCcon cn = new JDBCcon();
		byte[] xml_by = null ;
		String message="";
		String sqlx = "select * from xitxxb where mingc ='����ӿڱ���ǰ׺' and zhuangt=1 and leib='����'";
		ResultSet rsx = cn.getResultSet(sqlx);
		try{
			if(rsx.next()){
				bianm = rsx.getString("zhi") + bianm;
			}
		}catch(SQLException e){
			e.printStackTrace();
			message=error005; 
		}
		
			String sql = 
				"SELECT f.id,m.mingc AS meikmc,p.mingc AS meizmc,c.mingc AS faz,(f.maoz-f.piz) meil,'' AS caiyr,\n" +
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
					//����CreateXml�������Լ�¼��rs����XML�ļ�
					xml=CreateXml(cn.getResultSet(sql));
					try{
						xml_by=xml.getBytes();//tencryptByDES(xml.getBytes());//��xml����DES����
					
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

	
//���ܷ���
	public static byte[] encryptByDES(byte[] bytP) throws Exception {
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
	
	public static byte[] Str2Byt(String str) {
		byte[] bt=null;
		char[] chars = str.toCharArray();
		bt = new byte[chars.length];
		for(int i=0;i<chars.length;i++) {
			bt[i] = (byte)chars[i];
		}
		return bt;
	}
	
	private  String CreateXml(ResultSet rs){
		String xmlAray="";//û�м�¼�ͻ����쳣����������
		// TODO �Զ����ɷ������
		Element root = new Element("data");
		try {  
			while(rs.next()){
				for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
					root.addContent(new Element(rs.getMetaData().getColumnName(i)).addContent(rs.getString(i)));
				}
			}
			File file=new File(rizPath) ;
			FileWriter writer = new FileWriter(file.getAbsolutePath()+"Rulmzlbtmp.xml");
			XMLOutputter outputter = new XMLOutputter();   
			Format format=Format.getPrettyFormat(); 
			format.setEncoding("gb2312"); 
//			format.setOmitDeclaration(true);
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
	
	public String setHuayxx_jt(String user,String pasd,byte[] XMLData){
//			ҵ����
		boolean biaoz = false;
		String sql = "";
		String message = error000;
		JDBCcon con = new JDBCcon();
		Element eljizxx = null;
		Element elpinz = null;
		Element elcaiyrq = null;
		Element elcaiyry = null;
		Element elrelswd = null;
		Element elbanzxx = null;
		Element elmeil = null;
		Element elzhiyrq = null;
		Element elzhiyry = null;
		Element elrelssd = null;
		Element elbaogbh = null;
		Element elrulrq = null;
		Element elhuayrq = null;
		Element elhuaybh = null;
		
		Element elmt = null;
		Element elmad = null;
		Element elaar = null;
		Element elaad = null;
		Element elad = null;
		Element elvad = null;
		Element elvd = null;
		
		Element elvdaf = null;
		Element elstad = null;
		Element elstd = null;
		Element elhad = null;
		Element elqbad = null;
		Element elqgrad = null;
		Element elqgrd = null;
		
		Element elqnetar = null;
		Element elqnetar_dak = null;
		Element eldiancxxb_id = null;
		Element elhar = null;
		Element elhdaf = null;
		Element elsdaf = null;
		Element elvar = null;
		Element elfcad = null;
		Element elqgrad_daf = null;
		Element elhuayy = null;
		// 0,����xmlȡ������1,ȡ����ʽ����2,����table��values
		try {
			String xmlstr = new String(XMLData, "GB2312");
			StringReader sr = new StringReader(xmlstr);
			InputSource is = new InputSource(sr);
			Document doc = (new SAXBuilder()).build(is);// ���ÿһ������֡�ļ�
			Element root = doc.getRootElement();
			List Table = root.getChildren();
			
			 eljizxx = (Element) Table.get(0);
			 elpinz = (Element) Table.get(1);
			 elcaiyrq =(Element) Table.get(2) ;
			 elcaiyry = (Element) Table.get(3);
			 elrelswd = (Element) Table.get(4);
			 elbanzxx = (Element) Table.get(5);
			 elmeil =(Element) Table.get(6) ;
			 elzhiyrq = (Element) Table.get(7);
			 elzhiyry = (Element) Table.get(8);
			 elrelssd = (Element) Table.get(9);
			 elbaogbh =(Element) Table.get(10) ;
			 elrulrq =(Element) Table.get(11) ;
			 elhuayrq =(Element) Table.get(12) ;
			 elhuaybh =(Element) Table.get(13) ;
			
			 elmt = (Element) Table.get(14);
			 elmad =(Element) Table.get(15) ;
			 elaar =(Element) Table.get(16) ;
			 elaad =(Element) Table.get(17) ;
			 elad = (Element) Table.get(18);
			 elvad =(Element) Table.get(19) ;
			 elvd = (Element) Table.get(20);
			 elvdaf = (Element) Table.get(21);
			 elstad = (Element) Table.get(22);
			 elstd = (Element) Table.get(23);
			 elhad = (Element) Table.get(24);
			 elqbad = (Element) Table.get(25);
			 elqgrad = (Element) Table.get(26);
			 elqgrd =(Element) Table.get(27) ;
			 elqnetar = (Element) Table.get(28);
			 elqnetar_dak = (Element) Table.get(29);
			 eldiancxxb_id = (Element) Table.get(30);
			 
			 elhar = (Element) Table.get(31);
			 elhdaf = (Element) Table.get(32);
			 elsdaf = (Element) Table.get(33);
			 elvar = (Element) Table.get(34);
			 elfcad = (Element) Table.get(35);
			 elqgrad_daf = (Element) Table.get(36);
			 elhuayy = (Element) Table.get(37);
			
			String is_hava="select nvl(ispip,0) as ispip from rulmzlbtmp where huaybh='"+elhuaybh.getText()+"'";
			ResultSet rs = con.getResultSet(is_hava);
			String ispip="";//  
			if(rs.next()){
				ispip=String.valueOf(rs.getInt("ispip"));
			}
			if(ispip.equals("1")){
				con.rollBack();
				con.Close();
				message = "�����Ѿ�ƥ��,���������ϴ�";
				return message;
			}else{
				//����֮ǰ�Ƚ���ɾ������.
				String deleSql="delete rulmzlbtmp where huaybh='"+elhuaybh.getText()+"'";
				con.getDelete(deleSql);
				
			}
			
			 sql="insert into rulmzlbtmp (id,rulrq,fenxrq,diancxxb_id,rulbzb_id,jizfzb_id,ispip,jizxx,pinz,caiyrq,relswd,banzxx,zhiyrq,\n" +
				"zhiyry,relssd,baogbh,huaybh,mt,mad,aar,aad,ad,vad,vd,vdaf,stad,std,had,qbad,qgrad,qgrd,qnet_ar,lursj,caiyry,meil,har,hdaf,sdaf,var,fcad,qgrad_daf,huayy) values (\n" + 
				"xl_rulmzlb_id.nextval,to_date('"+elrulrq.getText()+"','yyyy-mm-dd'),to_date('"+elhuayrq.getText()+"','yyyy-mm-dd'),\n" +
				" "+eldiancxxb_id.getText()+",0,0,0,'"+eljizxx.getText()+"','"+elpinz.getText()+"',to_date('"+elcaiyrq.getText()+"','yyyy-mm-dd'),'"+elrelswd.getText()+"',\n" +
				" '"+elbanzxx.getText()+"',to_date('"+elzhiyrq.getText()+"','yyyy-mm-dd'),'"+elzhiyry.getText()+"','"+elrelssd.getText()+"',\n" +
			    " '"+elbaogbh.getText()+"', '"+elhuaybh.getText()+"',"+elmt.getText()+","+elmad.getText()+","+elaar.getText()+","+elaad.getText()+",\n" +
			    "  "+elad.getText()+","+elvad.getText()+","+elvd.getText()+","+elvdaf.getText()+","+elstad.getText()+","+elstd.getText()+",\n" +
			    " "+elhad.getText()+", "+elqbad.getText()+","+elqgrad.getText()+","+elqgrd.getText()+", "+elqnetar.getText()+",sysdate,\n" +
			    " '"+elcaiyry.getText()+"',"+elmeil.getText()+","+elhar.getText()+","+elhdaf.getText()+","+elsdaf.getText()+","+elvar.getText()+"\n" +
			    " ,"+elfcad.getText()+","+elqgrad_daf.getText()+",'"+elhuayy.getText()+"')";

			
			int flag = con.getUpdate(sql);
			if(flag == 1){
				biaoz = true ;
			}else{
				con.rollBack();
				message = error001;// zhixzt cuowlb zhixbz
				return message;
			}
		} catch (IOException e) {
			con.rollBack();
			message = error001;// zhixzt cuowlb zhixbz
			return message;
		} catch (JDOMException e1) {
			e1.printStackTrace();
			con.rollBack();
			message = error002;// zhixzt cuowlb zhixbz
			return message;
		} catch (Exception e4) {
			con.rollBack();
			message = error014;// zhixzt cuowlb zhixbz
		} finally {
			con.Close();
		}
			 return message ;
	}
	
	public String SetZhuanghRul(String DIANCXXB_ID,String BIANM,byte[] XMLData){
//		ҵ����
	String sql = "";
	String message = error000;
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
//	Element elqnetar_kcal = null;
	Element elsdaf = null;
	Element elhdaf = null;
	Element elfcad= null;
//	Element elshenhr = null;
	Element elhuayy = null;
//	Element elshenhsj = null;
	Element elbeiz = null;
	Element elqgraddaf = null;
	
	// 0,����xmlȡ������1,ȡ����ʽ����2,����table��values
	try {
		String xmlstr = new String(XMLData, "GB2312");
		StringReader sr = new StringReader(xmlstr);
		InputSource is = new InputSource(sr);
		Document doc = (new SAXBuilder()).build(is);// ���ÿһ������֡�ļ�
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
//			elqnetar_kcal = (Element) Table.get(15);
//			elshenhr = (Element) Table.get(16);
			elhuayy = (Element) Table.get(17);
//			elshenhsj = (Element) Table.get(18);
			elbeiz = (Element) Table.get(19);
			elsdaf = (Element) Table.get(20);
			elhdaf = (Element) Table.get(21);
			elfcad= (Element) Table.get(22);
			elqgraddaf= (Element) Table.get(23);

		 sql="UPDATE (SELECT RB.HAR,RB.HDAF,RB.SDAF,RB.VAR,RB.FCAD,RB.MT,RB.AAR,RB.AD,\n" +
			 "                RB.VAD,RB.VDAF,RB.STAD,RB.STD,RB.HAD,RB.QBAD,RB.QGRAD,RB.QGRD,RB.QNET_AR,\n" + 
			 "                RB.MAD,RB.AAD,RB.HAR,RB.QGRAD_DAF,RB.LURSJ,RB.HUAYY,RB.CAIYY,RB.FENXRQ,RB.SHENHZT\n" + 
			 "          FROM RULMZLZMXB RB\n" + 
			 "         WHERE RB.ID IN\n" + 
			 "               (SELECT RB.ID\n" + 
			 "                  FROM YANGPDHB YB, RULMZLZMXB RB, ZHUANMB ZB, ZHUANMLB ZLB\n" + 
			 "                 WHERE YB.ZHILBLSB_ID = RB.ZHUANMBZLLSB_ID\n" + 
			 "                   AND ZB.ZHILLSB_ID = RB.ZHUANMBZLLSB_ID\n" + 
			 "                   AND ZB.ZHUANMLB_ID = ZLB.ID\n" + 
			 "                   AND ZLB.JIB = 3\n" + 
			 "                   AND ZLB.DIANCXXB_ID = "+DIANCXXB_ID+"\n" + 
			 "                   AND RB.SHENHZT = 0\n" + 
			 "                   AND ZB.BIANM = '"+BIANM+"'))\n" + 
			 "   SET HDAF      = "+elhdaf.getText()+",\n" + 
			 "       SDAF      = "+elsdaf.getText()+",\n" + 
			 "       FCAD      = "+elfcad.getText()+",\n" + 
			 "       MT        = "+elmt.getText()+",\n" + 
			 "       AAR       = "+elaar.getText()+",\n" + 
			 "       AD        = "+elad.getText()+",\n" + 
			 "       VAD       = "+elvad.getText()+",\n" + 
			 "       VDAF      = "+elvdaf.getText()+",\n" + 
			 "       STAD      = "+elstad.getText()+",\n" + 
			 "       STD       = "+elstd.getText()+",\n" + 
			 "       HAD       = "+elhad.getText()+",\n" + 
			 "       QBAD      = "+elqbad.getText()+",\n" + 
			 "       QGRAD     = "+elqgrad.getText()+",\n" + 
			 "       QGRD      = "+elqgrd.getText()+",\n" + 
			 "       QNET_AR   = "+elqnetar_mj.getText()+",\n" + 
			 "       MAD       = "+elmad.getText()+",\n" + 
			 "       AAD       = "+elaad.getText()+",\n" + 
			 "       QGRAD_DAF = "+elqgraddaf.getText()+",\n" + 
			 "       LURSJ     = sysdate,\n" + 
			 "       HUAYY     = '"+elhuayy.getText()+"',\n" + 
			 "       SHENHZT   = 4,\n" + 
			 "       FENXRQ    = to_date('"+elhuayrq.getText()+"','yyyy-mm-dd')";

		
		int flag = con.getUpdate(sql);
		if(flag == 1){
			
		}else{
			con.rollBack();
			message = error001;// zhixzt cuowlb zhixbz
			return message;
		}
	} catch (IOException e) {
		con.rollBack();
		message = error001;// zhixzt cuowlb zhixbz
		return message;
	} catch (JDOMException e1) {
		e1.printStackTrace();
		con.rollBack();
		message = error002;// zhixzt cuowlb zhixbz
		return message;
	} catch (Exception e4) {
		con.rollBack();
		message = error014;// zhixzt cuowlb zhixbz
	} finally {
		con.Close();
	}
		 return message ;
}

	public boolean isPip(String bianm){
		//��¯�������Ƿ�ƥ��
		boolean biaoz = false;
		String sql = "";
		String message = error000;
		JDBCcon con = new JDBCcon();
		sql = " SELECT ispip FROM rulmzlbtmp WHERE huaybh = '"+bianm+"' ";
		ResultSet rs = con.getResultSet(sql);
		try {
			if(rs.next()){
				if(rs.getInt("ispip")==1){
					biaoz = false;
					message = "������ƥ�䣬���������ϴ�";
				}else{
					biaoz = true;
					message = "����δƥ�䣬���Խ��������ϴ�����";
				}
			}else{
			   biaoz = true;
			   message = "����δƥ�䣬���Խ��������ϴ�����";
			}
		} catch (Exception e4) {
			con.rollBack();
			message = error014;// zhixzt cuowlb zhixbz
		} finally {
			con.Close();
		}
			 return biaoz ;
	}
	
//	ׯ�ӵ糧�������÷�����
	public String getbm(String dcId, String bianm,  byte[] XMLData){
		String sql = "SELECT HUAYLB FROM VW_FAH_CAIYBM WHERE HUAYM = '"+bianm+"'";
		JDBCcon cn = new JDBCcon();
		String message = error000;
		String hylb="";
		ResultSet rsx = cn.getResultSet(sql);
		try{
			if(rsx.next()){
				hylb = rsx.getString("HUAYLB");
			}
			cn.Close();
			if(hylb.equals("�볧")){
				return get_huay_zhillsb( dcId,  bianm, XMLData);
			}else{
				return SetZhuanghRul(dcId,bianm,XMLData);
			}
		}catch(SQLException e){
			e.printStackTrace();
			message=error005; 
		}finally{
			cn.Close();
		}
		return message;
	}

	public String get_huay_zhillsb(String dcId, String bianm,  byte[] XMLData){
//			ҵ����
		String sql = "";
		JDBCcon cn = new JDBCcon();
		String message = error000;
		JDBCcon con = new JDBCcon();
		String sqlx = "select * from xitxxb where mingc ='����ӿڱ���ǰ׺' and zhuangt=1 and leib='����'";
		ResultSet rsx = cn.getResultSet(sqlx);
		try{
			if(rsx.next()){
				bianm = rsx.getString("zhi") + bianm;
			}
		}catch(SQLException e){
			e.printStackTrace();
			message=error005; 
		}
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
		Element elqgraddaf = null;

		// 0,����xmlȡ������1,ȡ����ʽ����2,����table��values
		try {
			String xmlstr = new String(XMLData, "GB2312");
			StringReader sr = new StringReader(xmlstr);
			InputSource is = new InputSource(sr);
			Document doc = (new SAXBuilder()).build(is);// ���ÿһ������֡�ļ�
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
				"select ls.id\n" +
				"from zhuanmb zm ,zhillsb ls\n" + 
				" where zm.bianm='"+bianm+"'\n" + 
				"and zm.zhuanmlb_id in (select id from zhuanmlb  where mingc='�������')\n" + 
				"and zm.zhillsb_id=ls.id\n" + 
				"and ls.shenhzt<=3\n" + 
				"order by ls.id desc";
			


		ResultSet rs = cn.getResultSet(Sql);
		if(rs.next()){
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
	          +" lury = '"+elshenhr.getText()+"',huayy = '"+elhuayy.getText()+"',beiz = '"+elbeiz.getText()+"',shenhzt=3  where id = "+rs.getString("id");

			int flag = con.getUpdate(sql);
			if(flag == 1){
				return message ;
			}else{
				con.rollBack();
				message = error001;// zhixzt cuowlb zhixbz
				return message ;
			}
		}else{
			message = error015;// zhixzt cuowlb zhixbz
			return message ;
		}
		} catch (IOException e) {
			con.rollBack();
			message = error001;// zhixzt cuowlb zhixbz
			return message ;
		} catch (JDOMException e1) {
			e1.printStackTrace();
			con.rollBack();
			message = error002;// zhixzt cuowlb zhixbz
			return message ;
		} catch (Exception e4) {
			con.rollBack();
			message = error014;// zhixzt cuowlb zhixbz
			return message ;
		} finally {
			con.Close();
		}
		//	 return message ;
	}

	public String get_huay_yuansbgd(String huaylx,String huaybh,String huaysj,byte[] XMLdata){
		String sql = "";
		JDBCcon cn = new JDBCcon();
		String message = error000;
		JDBCcon con = new JDBCcon();
		String sqlx = "select * from xitxxb where mingc ='����ӿڱ���ǰ׺' and zhuangt=1 and leib='����'";
		ResultSet rsx = cn.getResultSet(sqlx);
		try{
			if(rsx.next()){
				huaybh = rsx.getString("zhi") + huaybh;
			}
		}catch(SQLException e){
			e.printStackTrace();
			message=error005; 
		}
		Element  diancxxb_id = null;
		Element  kuangb = null;
		Element  faz = null;
		Element  konggjq  = null;
		Element  meil   = null;
		Element  pinz  = null;
		Element  caiyr   = null;
		Element  zhiyr   = null;
		Element  chec   = null;
		Element  shouwch   = null;
		Element  caiyrq   = null;
		Element  huayrq     = null;
		Element  huaycs           = null;
		Element  quansfid        = null;
		Element  quansfsbbh       = null;
		Element  quansfclpbh      = null;
		Element  quansfclpzl      = null;
		Element  quansfsyzl       = null;
		Element  quansfclphsyzzl  = null;
		Element  quansfhghzzl     = null;
		Element  quansfjcxsyhzzl  = null;
		Element  quansfgzhsyjszl  = null;
		Element  quansmt          = null;
		Element  pingjzmt         = null;
		Element  buzhqsmt         = null;
		Element  quansfhyr        = null;
		Element  shuifid          = null;
		Element  shuifsbbh        = null;
		Element  shuifclpbh       = null;
		Element  shuifclpzl       = null;
		Element  shuifsyzl        = null;
		Element  shuifclphsyzzl   = null;
		Element  shuifhghzzl      = null;
		Element  shuifjcxsyhzzl   = null;
		Element  shuifgzhsyjszl   = null;
		Element  shuifmad         = null;
		Element  pingjzmad        = null;
		Element  shuifhyr         = null;
		Element  huifid           = null;
		Element  huifsbbh         = null;
		Element  huifhmbh         = null;
		Element  huifhmzl         = null;
		Element  huifsyzl         = null;
		Element  huifhmhsyzzl     = null;
		Element  huifzshzzl       = null;
		Element  huifjcxsyhzzl    = null;
		Element  huifclwzl        = null;
		Element  huifaad          = null;
		Element  pingjzaad        = null;
		Element  huifhyr          = null;
		Element  huiffid          = null;
		Element  huiffsbbh        = null;
		Element  huiffggbh        = null;
		Element  huiffggzl        = null;
		Element  huiffsyzl        = null;
		Element  huiffgghsyzzl    = null;
		Element  huiffjrhzzl      = null;
		Element  huiffjrhsyjszl   = null;
		Element  huiffvad         = null;
		Element  pingjzvad        = null;
		Element  pingjzvdaf       = null;
		Element  pingjzvd         = null;
		Element  huiffhyr         = null;
		Element  liufid           = null;
		Element  liufsbbh         = null;
		Element  liufqmbh         = null;
		Element  liufqmzl         = null;
		Element  liufsyzl         = null;
		Element  liufqmhsyzzl     = null;
		Element  liufstad         = null;
		Element  pingjzstad       = null;
		Element  pingjzstd        = null;
		Element  liufhyr          = null;
		Element  farlid           = null;
		Element  farlsbbh         = null;
		Element  farlggzl         = null;
		Element  farlsyzl         = null;
		Element  farlqbad         = null;
		Element  konggjgwrqgrad   = null;
		Element  pingjzqgrad      = null;
		Element  pingjzqgrd       = null;
		Element  pingjzqnet_arje  = null;
		Element  pingjzqnet_ardk  = null;
		Element  farlhyr          = null;
		Element  shenhr           = null;
		Element  shenhsj          = null;
		Element  shenhzt          = null;
		Element  beiz             = null;
		Element  pingjzaar        = null;
		Element  pingjzad         = null;
		Element  js_meikmc        = null;
		Element  js_meizmc        = null;
		Element  shougbh          = null;
		Element  pingjzqbad       = null;
		Element  jiaoztz          = null;
		Element  quansfjcxsyhzzl1 = null;
		Element  shuifjcxsyhzzl1  = null;
		Element  huifjcxsyhzzl1   = null;

		// 0,����xmlȡ������1,ȡ����ʽ����2,����table��values
		try {
			String xmlstr = new String(XMLdata, "GB2312");
			StringReader sr = new StringReader(xmlstr);
			InputSource is = new InputSource(sr);
			Document doc = (new SAXBuilder()).build(is);// ���ÿһ������֡�ļ�
			Element root = doc.getRootElement();
			List Table = root.getChildren();
				
			  
			  diancxxb_id      =(Element) Table.get(0);
			  kuangb           =(Element) Table.get(1);
			  faz              =(Element) Table.get(2);
			  konggjq          =(Element) Table.get(3);
			  meil             =(Element) Table.get(4);
			  pinz             =(Element) Table.get(5);
			  caiyr            =(Element) Table.get(6);
			  zhiyr            =(Element) Table.get(7);
			  chec             =(Element) Table.get(8);
			  shouwch          =(Element) Table.get(9);
			  caiyrq           =(Element) Table.get(10);
			  huayrq           =(Element) Table.get(11);
			  huaycs           =(Element) Table.get(12);
			  quansfid         =(Element) Table.get(13);
			  quansfsbbh       =(Element) Table.get(14);
			  quansfclpbh      =(Element) Table.get(15);
			  quansfclpzl      =(Element) Table.get(16);
			  quansfsyzl       =(Element) Table.get(17);
			  quansfclphsyzzl  =(Element) Table.get(18);
			  quansfhghzzl     =(Element) Table.get(19);
			  quansfjcxsyhzzl  =(Element) Table.get(20);
			  quansfgzhsyjszl  =(Element) Table.get(21);
			  quansmt          =(Element) Table.get(22);
			  pingjzmt         =(Element) Table.get(23);
			  buzhqsmt         =(Element) Table.get(24);
			  quansfhyr        =(Element) Table.get(25);
			  shuifid          =(Element) Table.get(26); 
			  shuifsbbh        =(Element) Table.get(27);
			  shuifclpbh       =(Element) Table.get(28);
			  shuifclpzl       =(Element) Table.get(29);
			  shuifsyzl        =(Element) Table.get(30);
			  shuifclphsyzzl   =(Element) Table.get(31);
			  shuifhghzzl      =(Element) Table.get(32);
			  shuifjcxsyhzzl   =(Element) Table.get(33);
			  shuifgzhsyjszl   =(Element) Table.get(34);
			  shuifmad         =(Element) Table.get(35);
			  pingjzmad        =(Element) Table.get(36);
			  shuifhyr         =(Element) Table.get(37);
			  huifid           =(Element) Table.get(38);
			  huifsbbh         =(Element) Table.get(39);
			  huifhmbh         =(Element) Table.get(40);
			  huifhmzl         =(Element) Table.get(41);
			  huifsyzl         =(Element) Table.get(42);
			  huifhmhsyzzl     =(Element) Table.get(43);
			  huifzshzzl       =(Element) Table.get(44);
			  huifjcxsyhzzl    =(Element) Table.get(45);
			  huifclwzl        =(Element) Table.get(46);
			  huifaad          =(Element) Table.get(47);
			  pingjzaad        =(Element) Table.get(48);
			  huifhyr          =(Element) Table.get(49);
			  huiffid          =(Element) Table.get(50);
			  huiffsbbh        =(Element) Table.get(51);
			  huiffggbh        =(Element) Table.get(52);
			  huiffggzl        =(Element) Table.get(53);
			  huiffsyzl        =(Element) Table.get(54);
			  huiffgghsyzzl    =(Element) Table.get(55);
			  huiffjrhzzl      =(Element) Table.get(56);
			  huiffjrhsyjszl   =(Element) Table.get(57);
			  huiffvad         =(Element) Table.get(58);
			  pingjzvad        =(Element) Table.get(59);
			  pingjzvdaf       =(Element) Table.get(60);
			  pingjzvd         =(Element) Table.get(61);
			  huiffhyr         =(Element) Table.get(62);
			  liufid           =(Element) Table.get(63);
			  liufsbbh         =(Element) Table.get(64);
			  liufqmbh         =(Element) Table.get(65);
			  liufqmzl         =(Element) Table.get(66);
			  liufsyzl         =(Element) Table.get(67);
			  liufqmhsyzzl     =(Element) Table.get(68);
			  liufstad         =(Element) Table.get(69);
			  pingjzstad       =(Element) Table.get(70);
			  pingjzstd        =(Element) Table.get(71);
			  liufhyr          =(Element) Table.get(72);
			  farlid           =(Element) Table.get(73);
			  farlsbbh         =(Element) Table.get(74);
			  farlggzl         =(Element) Table.get(75);
			  farlsyzl         =(Element) Table.get(76);
			  farlqbad         =(Element) Table.get(77);
			  konggjgwrqgrad   =(Element) Table.get(78);
			  pingjzqgrad      =(Element) Table.get(79);
			  pingjzqgrd       =(Element) Table.get(80);
			  pingjzqnet_arje  =(Element) Table.get(81);
			  pingjzqnet_ardk  =(Element) Table.get(82);
			  farlhyr          =(Element) Table.get(83);
			  shenhr           =(Element) Table.get(84);
			  shenhsj          =(Element) Table.get(85);
			  shenhzt          =(Element) Table.get(86);
			  beiz             =(Element) Table.get(87);
			  pingjzaar        =(Element) Table.get(88);
			  pingjzad         =(Element) Table.get(89);
			  js_meikmc        =(Element) Table.get(90);
			  js_meizmc        =(Element) Table.get(91);
			  shougbh          =(Element) Table.get(92);
			  pingjzqbad       =(Element) Table.get(93);
			  jiaoztz          =(Element) Table.get(94);
			  quansfjcxsyhzzl1 =(Element) Table.get(95);
			  shuifjcxsyhzzl1  =(Element) Table.get(96);
			  huifjcxsyhzzl1   =(Element) Table.get(97);

			String Sql = "delete from YUANSBGB where diancxxb_id = "+diancxxb_id.getText()+" and huaybh='"+huaybh+"'";
			cn.getDelete(Sql);
			
		    sql = " insert into YUANSBGB (ID,DIANCXXB_ID,HUAYBH,KUANGB,FAZ,KONGGJQ,MEIL,PINZ,CAIYR,ZHIYR,CHEC,SHOUWCH,"+
                      "CAIYRQ,HUAYRQ,HUAYCS,QUANSFID,QUANSFSBBH,QUANSFCLPBH,QUANSFCLPZL,QUANSFSYZL,"+
                      "QUANSFCLPHSYZZL,QUANSFHGHZZL,QUANSFJCXSYHZZL,QUANSFGZHSYJSZL,QUANSMT,PINGJZMT,"+
                      "BUZHQSMT,QUANSFHYR,SHUIFID,SHUIFSBBH,SHUIFCLPBH,SHUIFCLPZL,SHUIFSYZL,SHUIFCLPHSYZZL,"+
                      "SHUIFHGHZZL,SHUIFJCXSYHZZL,SHUIFGZHSYJSZL,SHUIFMAD,PINGJZMAD,SHUIFHYR,HUIFID,HUIFSBBH,"+
                      "HUIFHMBH,HUIFHMZL,HUIFSYZL,HUIFHMHSYZZL,HUIFZSHZZL,HUIFJCXSYHZZL,HUIFCLWZL,HUIFAAD,"+
                      "PINGJZAAD,HUIFHYR,HUIFFID,HUIFFSBBH,HUIFFGGBH,HUIFFGGZL,HUIFFSYZL,HUIFFGGHSYZZL,HUIFFJRHZZL,"+
                      "HUIFFJRHSYJSZL,HUIFFVAD,PINGJZVAD,PINGJZVDAF,PINGJZVD,HUIFFHYR,LIUFID,LIUFSBBH,LIUFQMBH,"+
                      "LIUFQMZL,LIUFSYZL,LIUFQMHSYZZL,LIUFSTAD,PINGJZSTAD,PINGJZSTD,LIUFHYR,FARLID,FARLSBBH,"+
                      "FARLGGZL,FARLSYZL,FARLQBAD,KONGGJGWRQGRAD,PINGJZQGRAD,PINGJZQGRD,PINGJZQNET_ARJE,PINGJZQNET_ARDK,"+
                      "FARLHYR,SHENHR,SHENHSJ,SHENHZT,BEIZ,PINGJZAAR,PINGJZAD,JS_MEIKMC,JS_MEIZMC,SHOUGBH,"+
                      "PINGJZQBAD,JIAOZTZ,QUANSFJCXSYHZZL1,SHUIFJCXSYHZZL1,HUIFJCXSYHZZL1,leix) values (getnewid("+diancxxb_id.getText()+"),"+ 
		    	  diancxxb_id.getText() +",'" +				  
		    	  huaybh +"','" +		
				  kuangb.getText()     +"','" +		      
				  faz.getText()        +"','" +		      
				  konggjq.getText()   +"','" +		       
				  meil.getText()       +"','" +		     
				  pinz.getText()         +"','" +		    
				  caiyr.getText()       +"','" +		     
				  zhiyr.getText()        +"','" +		    
				  chec.getText()          +"','" +		   
				  shouwch.getText()       +"',to_date('" +		   
				  caiyrq.getText()        +"','yyyy-mm-dd'),to_date('" +		   
				  caiyrq.getText()        +"','yyyy-mm-dd'),'" +		     
				  huaycs.getText()           +"','" +		
				  quansfid.getText()         +"','" +		
				  quansfsbbh.getText()      +"','" +		
				  quansfclpbh.getText()     +"','" +		
				  quansfclpzl.getText()      +"','" +		
				  quansfsyzl.getText()      +"','" +		
				  quansfclphsyzzl.getText()  +"','" +		
				  quansfhghzzl.getText()    +"','" +		
				  quansfjcxsyhzzl.getText()  +"','" +		
				  quansfgzhsyjszl.getText()  +"','" +		
				  quansmt.getText()          +"','" +		
				  pingjzmt.getText()        +"','" +		
				  buzhqsmt.getText()         +"','" +		
				  quansfhyr.getText()        +"','" +		
				  shuifid.getText()          +"','" +		
				  shuifsbbh.getText()        +"','" +		
				  shuifclpbh.getText()       +"','" +		
				  shuifclpzl.getText()       +"','" +		
				  shuifsyzl.getText()        +"','" +		
				  shuifclphsyzzl.getText()   +"','" +		
				  shuifhghzzl.getText()      +"','" +		
				  shuifjcxsyhzzl.getText()   +"','" +		
				  shuifgzhsyjszl.getText()   +"','" +		
				  shuifmad.getText()         +"','" +		
				  pingjzmad.getText()        +"','" +		
				  shuifhyr.getText()         +"','" +		
				  huifid.getText()           +"','" +		
				  huifsbbh.getText()         +"','" +		
				  huifhmbh.getText()         +"','" +		
				  huifhmzl.getText()         +"','" +		
				  huifsyzl.getText()         +"','" +		
				  huifhmhsyzzl.getText()     +"','" +		
				  huifzshzzl.getText()       +"','" +		
				  huifjcxsyhzzl.getText()    +"','" +		
				  huifclwzl.getText()        +"','" +		
				  huifaad.getText()          +"','" +		
				  pingjzaad.getText()        +"','" +		
				  huifhyr.getText()          +"','" +		
				  huiffid.getText()          +"','" +		
				  huiffsbbh.getText()        +"','" +		
				  huiffggbh.getText()        +"','" +		
				  huiffggzl.getText()        +"','" +		
				  huiffsyzl.getText()        +"','" +		
				  huiffgghsyzzl.getText()    +"','" +		
				  huiffjrhzzl.getText()      +"','" +		
				  huiffjrhsyjszl.getText()   +"','" +		
				  huiffvad.getText()         +"','" +		
				  pingjzvad.getText()        +"','" +		
				  pingjzvdaf.getText()       +"','" +		
				  pingjzvd.getText()         +"','" +		
				  huiffhyr.getText()         +"','" +		
				  liufid.getText()           +"','" +		
				  liufsbbh.getText()         +"','" +		
				  liufqmbh.getText()         +"','" +		
				  liufqmzl.getText()         +"','" +		
				  liufsyzl.getText()         +"','" +		
				  liufqmhsyzzl.getText()     +"','" +		
				  liufstad.getText()         +"','" +		
				  pingjzstad.getText()       +"','" +		
				  pingjzstd.getText()        +"','" +		
				  liufhyr.getText()          +"','" +		
				  farlid.getText()           +"','" +		
				  farlsbbh.getText()         +"','" +		
				  farlggzl.getText()         +"','" +		
				  farlsyzl.getText()         +"','" +		
				  farlqbad.getText()         +"','" +		
				  konggjgwrqgrad.getText()   +"','" +		
				  pingjzqgrad.getText()      +"','" +		
				  pingjzqgrd.getText()       +"','" +		
				  pingjzqnet_arje.getText()  +"','" +		
				  pingjzqnet_ardk.getText()  +"','" +		
				  farlhyr.getText()          +"','" +		
				  shenhr.getText()           +"',to_date('" +		   
				  caiyrq.getText()        	 +"','yyyy-mm-dd'),'" +		
				  shenhzt.getText()          +"','" +		
				  beiz.getText()             +"','" +		
				  pingjzaar.getText()        +"','" +		
				  pingjzad.getText()         +"','" +		
				  js_meikmc.getText()        +"','" +		
				  js_meizmc.getText()        +"','" +		
				  shougbh.getText()          +"','" +		
				  pingjzqbad.getText()       +"','" +		
				  jiaoztz.getText()          +"','" +		
				  quansfjcxsyhzzl1.getText()+"','" +		
				  shuifjcxsyhzzl1.getText() +"','" +		
				  huifjcxsyhzzl1.getText()+"','"+huaylx+"')" ;		

			int flag = con.getUpdate(sql);
			if(flag == 1){
				return message ;
			}else{
				con.rollBack();
				message = error001;// zhixzt cuowlb zhixbz
				return message ;
			}
		} catch (IOException e) {
			con.rollBack();
			message = error001;// zhixzt cuowlb zhixbz
			return message ;
		} catch (JDOMException e1) {
			e1.printStackTrace();
			con.rollBack();
			message = error002;// zhixzt cuowlb zhixbz
			return message ;
		} catch (Exception e4) {
			con.rollBack();
			message = error014;// zhixzt cuowlb zhixbz
			return message ;
		} finally {
			con.Close();
		}
	}
	
	public String get_huaydxfx(String jieklx,String fenxys,byte[] XMLData){
		String sql = "";
		JDBCcon cn = new JDBCcon();
		String message = error000;
		JDBCcon con = new JDBCcon();
		if(fenxys.equals("Qbad")){
			Element  diancxxb_id  = null;
			Element  riq          = null;
			Element  zidbm        = null;
			Element  bianm        = null;
			Element  fenxxmb_id   = null;
			Element  cis          = null;
			Element  shebbh       = null;
			Element  shebxh       = null;
			Element  qimbh        = null;
			Element  qimzl        = null;
			Element  meiyzl       = null;
			Element  shij         = null;
			Element  wens         = null;
			Element  dianhr       = null;
			Element  rerl         = null;
			Element  farl         = null;
			Element  yangdbh      = null;
			Element  tianjwrz     = null;
			Element  daorwj       = null;
			Element  jiekjmc      = null;
			Element  jiekjip      = null;
			Element  huayy        = null;
			Element  shenhy       = null;
			Element  shenhsj      = null;
			Element  shenhzt      = null;
			Element  beiz         = null;
			try {
				String xmlstr = new String(XMLData, "GB2312");
				StringReader sr = new StringReader(xmlstr);
				InputSource is = new InputSource(sr);
				Document doc = (new SAXBuilder()).build(is);// ���ÿһ������֡�ļ�
				Element root = doc.getRootElement();
				List Table = root.getChildren();

				  diancxxb_id = (Element) Table.get(0);
				  riq         = (Element) Table.get(1);
				  zidbm       = (Element) Table.get(2);
				  bianm       = (Element) Table.get(3);
				  fenxxmb_id  = (Element) Table.get(4);
				  cis         = (Element) Table.get(5);
				  shebbh      = (Element) Table.get(6);
				  shebxh      = (Element) Table.get(7);
				  qimbh       = (Element) Table.get(8);
				  qimzl       = (Element) Table.get(9);
				  meiyzl      = (Element) Table.get(10);
				  shij        = (Element) Table.get(11);
				  wens        = (Element) Table.get(12);
				  dianhr      = (Element) Table.get(13);
				  rerl        = (Element) Table.get(14);
				  farl        = (Element) Table.get(15);
				  yangdbh     = (Element) Table.get(16);
				  tianjwrz    = (Element) Table.get(17);
				  daorwj      = (Element) Table.get(18);
				  jiekjmc     = (Element) Table.get(19);
				  jiekjip     = (Element) Table.get(20);
				  huayy       = (Element) Table.get(21);
				  shenhy      = (Element) Table.get(22);
				  shenhsj     = (Element) Table.get(23);
				  shenhzt     = (Element) Table.get(24);
				  beiz        = (Element) Table.get(25);
				 String huaybm = bianm.getText();
					String sqlx = "select * from xitxxb where mingc ='����ӿڱ���ǰ׺' and zhuangt=1 and leib='����'";
					ResultSet rsx = cn.getResultSet(sqlx);
					try{
						if(rsx.next()){
							huaybm = rsx.getString("zhi") + huaybm;
						}
					}catch(SQLException e){
						e.printStackTrace();
						message=error005; 
					}
				  String Sql = "delete from HUAYRLB where leix = '"+jieklx+"' and bianm = '"+huaybm+"' and diancxxb_id = "+ diancxxb_id.getText();
					cn.getDelete(Sql);
					
					sql = "insert into HUAYRLB (id,diancxxb_id,Riq,Zidbm,Bianm,Fenxxmb_Id,cis,Shebbh,Shebxh,Qimbh,Qimzl,"+
					   " meiyzl,Shij,wens,dianhr,Rerl,Farl,Yangdbh,Tianjwrz,daorwj,Jiekjmc,Jiekjip,Huayy,Shenhy,shenhsj,"+
					   " shenhzt,Beiz,leix) values (getnewid("+diancxxb_id.getText()+"),"+
						  diancxxb_id.getText() +",to_date('" +
						  riq.getText()         +"','yyyy-mm-dd'),'" +
						  zidbm.getText()       +"','"+
						  huaybm              +"','"+
						  fenxxmb_id.getText()  +"','"+
						  cis.getText()         +"','"+
						  shebbh.getText()      +"','"+
						  shebxh.getText()      +"','"+
						  qimbh.getText()       +"','"+
						  qimzl.getText()       +"','"+
						  meiyzl.getText()      +"',to_date('"+
						  shij.getText()        +"','yyyy-mm-dd'),'"+
						  wens .getText()       +"','"+
						  dianhr.getText()      +"','"+
						  rerl.getText()        +"','"+
						  farl.getText()        +"','"+
						  yangdbh.getText()     +"','"+
						  tianjwrz.getText()    +"','"+
						  daorwj.getText()      +"','"+
						  jiekjmc.getText()     +"','"+
						  jiekjip.getText()     +"','"+
						  huayy.getText()       +"','"+
						  shenhy.getText()      +"',to_date('"+
						  shenhsj.getText()     +"','yyyy-mm-dd hh24:mi:ss'),'"+
						  shenhzt.getText()     +"','"+
						  beiz.getText() +"','"+
						  jieklx+"')";

					int flag = con.getInsert(sql);
					if(flag == 1){
						return message ;
					}else{
						con.rollBack();
						message = error001;// zhixzt cuowlb zhixbz
						return message ;
					}
			} catch (IOException e) {
				con.rollBack();
				message = error001;// zhixzt cuowlb zhixbz
				return message ;
			} catch (JDOMException e1) {
				e1.printStackTrace();
				con.rollBack();
				message = error002;// zhixzt cuowlb zhixbz
				return message ;
			} catch (Exception e4) {
				con.rollBack();
				message = error014;// zhixzt cuowlb zhixbz
				return message ;
			} finally {
				con.Close();
			}
		}else if(fenxys.equals("Std")){
			Element  diancxxb_id  = null;
			Element  riq          = null;
			Element  zidbm        = null;
			Element  bianm        = null;
			Element  fenxxmb_id   = null;
			Element  cis          = null;
			Element  shebbh       = null;
			Element  shebxh       = null;
			Element  qimbh        = null;
			Element  qimzl        = null;
			Element  meiyzl       = null;
			Element  liuf         = null;
			Element  daorwj       = null;
			Element  jiekjmc      = null;
			Element  jiekjip      = null;
			Element  huayy        = null;
			Element  shenhy       = null;
			Element shenhsj      = null;
			Element  shenhzt      = null;
			Element  beiz         = null;
			try {
				String xmlstr = new String(XMLData, "GB2312");
				StringReader sr = new StringReader(xmlstr);
				InputSource is = new InputSource(sr);
				Document doc = (new SAXBuilder()).build(is);// ���ÿһ������֡�ļ�
				Element root = doc.getRootElement();
				List Table = root.getChildren();

				  diancxxb_id = (Element) Table.get(0);
				  riq         = (Element) Table.get(1);
				  zidbm       = (Element) Table.get(2);
				  bianm       = (Element) Table.get(3);
				  fenxxmb_id  = (Element) Table.get(4);
				  cis         = (Element) Table.get(5);
				  shebbh      = (Element) Table.get(6);
				  shebxh      = (Element) Table.get(7);
				  qimbh       = (Element) Table.get(8);
				  qimzl       = (Element) Table.get(9);
				  meiyzl      = (Element) Table.get(10);
				  liuf        = (Element) Table.get(11);
				  daorwj      = (Element) Table.get(12);
				  jiekjmc     = (Element) Table.get(13);
				  jiekjip     = (Element) Table.get(14);
				  huayy       = (Element) Table.get(15);
				  shenhy      = (Element) Table.get(16);
				  shenhsj     = (Element) Table.get(17);
				  shenhzt     = (Element) Table.get(18);
				  beiz        = (Element) Table.get(19);
				   String huaybm = bianm.getText();
					String sqlx = "select * from xitxxb where mingc ='����ӿڱ���ǰ׺' and zhuangt=1 and leib='����'";
					ResultSet rsx = cn.getResultSet(sqlx);
					try{
						if(rsx.next()){
							huaybm = rsx.getString("zhi") + huaybm;
						}
					}catch(SQLException e){
						e.printStackTrace();
						message=error005; 
					}
				  String Sql = "delete from HUAYLFB where leix = '"+jieklx+"' and bianm = '"+huaybm +"' and diancxxb_id = "+ diancxxb_id.getText();
					cn.getDelete(Sql);
					
					sql = "insert into HUAYLFB (ID,DIANCXXB_ID,RIQ,ZIDBM,BIANM,FENXXMB_ID,CIS,SHEBBH,SHEBXH,QIMBH,QIMZL,MEIYZL,LIUF,"+
						"DAORWJ,JIEKJMC,JIEKJIP,HUAYY,SHENHY,SHENHSJ,SHENHZT,BEIZ,leix) values (getnewid("+diancxxb_id.getText()+"),"+
						  diancxxb_id.getText() +",to_date('" +
						  riq.getText()         +"','yyyy-mm-dd'),'" +
						  zidbm.getText()      +"','"+
						  huaybm       		+"','"+
						  fenxxmb_id.getText()  +"','"+
						  cis.getText()         +"','"+
						  shebbh.getText()      +"','"+
						  shebxh.getText()      +"','"+
						  qimbh.getText()      +"','"+
						  qimzl.getText()       +"','"+
						  meiyzl.getText()      +"','"+
						  liuf.getText()        +"','"+
						  daorwj.getText()      +"','"+
						  jiekjmc.getText()     +"','"+
						  jiekjip.getText()     +"','"+
						  huayy.getText()       +"','"+
						  shenhy.getText()      +"',to_date('"+
						  shenhsj.getText()     +"','yyyy-mm-dd hh24:mi:ss'),'"+
						  shenhzt.getText()     +"','"+
						  beiz.getText()     +"','"+
						  jieklx+"')";

					int flag = cn.getInsert(sql);
					if(flag == 1){
						return message ;
					}else{
						con.rollBack();
						message = error001;// zhixzt cuowlb zhixbz
						return message ;
					}
			} catch (IOException e) {
				con.rollBack();
				message = error001;// zhixzt cuowlb zhixbz
				return message ;
			} catch (JDOMException e1) {
				e1.printStackTrace();
				con.rollBack();
				message = error002;// zhixzt cuowlb zhixbz
				return message ;
			} catch (Exception e4) {
				con.rollBack();
				message = error014;// zhixzt cuowlb zhixbz
				return message ;
			} finally {
				con.Close();
			}
		}else{
			Element  diancxxb_id  = null;
			Element  riq          = null;
			Element  zidbm        = null;
			Element  bianm        = null;
			Element  fenxxmb_id   = null;
			Element  cis          = null;
			Element  shebbh       = null;
			Element  shebxh       = null;
			Element  qimbh        = null;
			Element  qimzl        = null;
			Element  zongzl       = null;
			Element  meiyzl       = null;
			Element  honghzzl1    = null;
			Element  honghzzl2    = null;
			Element  honghzzl3    = null;
			Element  honghzzl4    = null;
			Element  honghzzl     = null;
			Element  huayz        = null;
			Element  pingjz       = null;
			Element  pingjz_ar    = null;
			Element  pingjz_d     = null;
			Element  pingjz_daf   = null;
			Element  jiekjmc      = null;
			Element  jiekjip      = null;
			Element  huayy        = null;
			Element  shenhy       = null;
			Element  shenhsj      = null;
			Element  shenhzt      = null;
			Element  beiz         = null;
			try {
				String xmlstr = new String(XMLData, "GB2312");
				StringReader sr = new StringReader(xmlstr);
				InputSource is = new InputSource(sr);
				Document doc = (new SAXBuilder()).build(is);// ���ÿһ������֡�ļ�
				Element root = doc.getRootElement();
				List Table = root.getChildren();

				  diancxxb_id = (Element) Table.get(0);
				  riq         = (Element) Table.get(1);
				  zidbm       = (Element) Table.get(2);
				  bianm       = (Element) Table.get(3);
				  fenxxmb_id  = (Element) Table.get(4);
				  cis         = (Element) Table.get(5);
				  shebbh      = (Element) Table.get(6);
				  shebxh      = (Element) Table.get(7);
				  qimbh       = (Element) Table.get(8);
				  qimzl       = (Element) Table.get(9);
				  zongzl      = (Element) Table.get(10);
				  meiyzl      = (Element) Table.get(11);
				  honghzzl1   = (Element) Table.get(12);
				  honghzzl2   = (Element) Table.get(13);
				  honghzzl3   = (Element) Table.get(14);
				  honghzzl4   = (Element) Table.get(15);
				  honghzzl    = (Element) Table.get(16);
				  huayz       = (Element) Table.get(17);
				  pingjz      = (Element) Table.get(18);
				  pingjz_ar   = (Element) Table.get(19);
				  pingjz_d    = (Element) Table.get(20);
				  pingjz_daf  = (Element) Table.get(21);
				  jiekjmc     = (Element) Table.get(22);
				  jiekjip     = (Element) Table.get(23);
				  huayy       = (Element) Table.get(24);
				  shenhy      = (Element) Table.get(25);
				  shenhsj     = (Element) Table.get(26);
				  shenhzt     = (Element) Table.get(27);
				  beiz        = (Element) Table.get(28);
				  String huaybm = bianm.getText();
					String sqlx = "select * from xitxxb where mingc ='����ӿڱ���ǰ׺' and zhuangt=1 and leib='����'";
					ResultSet rsx = cn.getResultSet(sqlx);
					try{
						if(rsx.next()){
							huaybm = rsx.getString("zhi") + huaybm;
						}
					}catch(SQLException e){
						e.printStackTrace();
						message=error005; 
					}
					
				String Sql = "delete from HUAYGYFXB where leix = '"+jieklx+"' and bianm = '"+huaybm+"' and diancxxb_id = "+ diancxxb_id.getText();
				cn.getDelete(Sql);
				
				sql = "insert into HUAYGYFXB(ID,DIANCXXB_ID,RIQ,ZIDBM,BIANM,FENXXMB_ID,CIS,SHEBBH,SHEBXH,QIMBH,QIMZL,"+
                                      "ZONGZL,MEIYZL,HONGHZZL1,HONGHZZL2,HONGHZZL3,HONGHZZL4,HONGHZZL,HUAYZ,PINGJZ,PINGJZ_AR,PINGJZ_D,"+
					"PINGJZ_DAF,JIEKJMC,JIEKJIP,HUAYY,SHENHY,SHENHSJ,SHENHZT,BEIZ,leix)values (getnewid("+diancxxb_id.getText()+"),"+
					  diancxxb_id.getText() +",to_date('" +
					  riq.getText()         +"','yyyy-mm-dd'),'" +
					  zidbm.getText()       +"','"+
					  huaybm             +"','"+
					  fenxxmb_id.getText()  +"','"+
					  cis.getText()         +"','"+
					  shebbh.getText()      +"','"+
					  shebxh.getText()      +"','"+
					  qimbh.getText()       +"','"+
					  qimzl.getText()       +"','"+
					  zongzl.getText()      +"','"+
					  meiyzl.getText()      +"','"+
					  honghzzl1.getText()   +"','"+
					  honghzzl2.getText()   +"','"+
					  honghzzl3.getText()   +"','"+
					  honghzzl4.getText()   +"','"+
					  honghzzl.getText()    +"','"+
					  huayz.getText()       +"','"+
					  pingjz.getText()      +"','"+
					  pingjz_ar.getText()   +"','"+
					  pingjz_d.getText()    +"','"+
					  pingjz_daf.getText()  +"','"+
					  jiekjmc.getText()     +"','"+
					  jiekjip.getText()     +"','"+
					  huayy.getText()       +"','"+
					  shenhy.getText()      +"',to_date('"+
					  shenhsj.getText()     +"','yyyy-mm-dd hh24:mi:ss'),'"+
					  shenhzt.getText()     +"','"+
					  beiz.getText()   +"','"+
					  jieklx+"')";


				int flag = cn.getInsert(sql);
				if(flag == 1){
					return message ;
				}else{
					con.rollBack();
					message = error001;// zhixzt cuowlb zhixbz
					return message ;
				}
			} catch (IOException e) {
				con.rollBack();
				message = error001;// zhixzt cuowlb zhixbz
				return message ;
			} catch (JDOMException e1) {
				e1.printStackTrace();
				con.rollBack();
				message = error002;// zhixzt cuowlb zhixbz
				return message ;
			} catch (Exception e4) {
				con.rollBack();
				message = error014;// zhixzt cuowlb zhixbz
				return message ;
			} finally {
				con.Close();
			}
		}

		// 0,����xmlȡ������1,ȡ����ʽ����2,����table��values
		
	}
	private void loadDateRec(String sql,List list) throws SQLException{
		JDBCcon con=new JDBCcon();
		ResultSet rs=con.getResultSet(sql);
		
		while(rs.next()){//���ݼ�¼����
			List zidList=new ArrayList();
			for(int i=1;i<=rs.getMetaData().getColumnCount();i++){//
				zidList.add(new String[]{rs.getMetaData().getColumnName(i),rs.getString(i)});
			}
			list.add(zidList);
		}
	}
	
	public static void main(String[] args) {
		
//		String bianm = "20120704-003";
		String user = "202";
		String pasd = "20140326J87";
		Test h = new Test();
		//h.getFahInfo_jt(bianm);
		
		
		String srr=
		"<data>\n"+
		"<huayrq>2014-03-28</huayrq>\n"+
		"<mt>22.20</mt>\n"+
		"<mad>12.08</mad>\n"+
		"<aad>8.2</aad>\n"+
		"<aar>7.26</aar>\n"+
		"<ad>9.33</ad>\n"+
		"<vad>16.6</vad>\n"+
		"<vdaf>20.82</vdaf>\n"+
		"<stad>0.38</stad>\n"+
		"<std>0.43</std>\n"+
		"<had>77</had>\n"+
		"<qbad>19.0580</qbad>\n"+
		"<qgrad>18.998</qgrad>\n"+
		"<qgrd>21.608</qgrd>\n"+
		"<qnetar_mj>2.264</qnetar_mj>\n"+
		"<qnetar_kcal>541</qnetar_kcal>\n"+
		"<shenhr>�������Ա</shenhr>\n"+
		"<huayy>�������Ա;</huayy>\n"+
		"<shenhsj>2014-03-31</shenhsj>\n"+
		"<beiz> </beiz>\n"+
		"<sdaf>0.48</sdaf>\n"+
		"<hdaf>96.59</hdaf>\n"+
		"<fcad>63.12</fcad>\n"+
		"<qgraddaf>23.831</qgraddaf>\n"+
		"</data>";

		byte [] a=srr.getBytes();
		h.getbm(user,pasd,a);
	}
	
}