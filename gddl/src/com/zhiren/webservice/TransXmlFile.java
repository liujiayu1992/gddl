package com.zhiren.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.FileNameFilter;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.dc.jilgl.Jilcz;

public class TransXmlFile {
	
	public static final String xmlName_Shulsjdr = "�������ݵ���";
	
	public TransXmlFile() {
		super();
	}
	private static void SaveShulsjdr(List elist) {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		SearchValidate sv = new SearchValidate();
		StringBuffer sb = new StringBuffer();
		String diancxxb_id=null;String gongysb_id;String meikxxb_id;
		String pinzb_id;String faz_id;String daoz_id;
		String jihkjb_id;String fahrq;String daohrq;
		String caiyrq;String caiybm;String yunsfsb_id;
		String chec;String cheh;String maoz;
		String piz;String piaoz;String koud;String kous;
		String kouz;String sanfsl;String jianjfs;String chebb_id;
		String yuandz_id;String yuanshdwb_id;String yuanmkdw;
		String zhongcjjy;String zhongcsj;String zhongchh;
		String qingcjjy;String qingcsj;String qingchh;
		String daozch;String lursj;String lurry;
		String yunsdwb_id;String beiz;
		
		sb.append("begin\n");
		for (int i = 0; i < elist.size(); i++) {
			Element ehead = (Element) elist.get(i);
			diancxxb_id = sv.getDiancxxb_id(con, ehead.getChildText("�糧����"));
			if(diancxxb_id == null) {	WriteLog.writeErrorLog("�糧����Ϊ�գ�");continue;}
			gongysb_id = sv.getGongysb_id(con, ehead.getChildText("��Ӧ�̱���"));
			if(gongysb_id == null) {	WriteLog.writeErrorLog("��Ӧ�̱���Ϊ�գ�");continue;}
			meikxxb_id = sv.getMeikxxb_id(con, ehead.getChildText("ú�����"));
			if(meikxxb_id == null) {	WriteLog.writeErrorLog("ú�����Ϊ�գ�");continue;}
			pinzb_id = sv.getPinzb_id(con, ehead.getChildText("Ʒ�ֱ���"));
			if(pinzb_id == null) {	WriteLog.writeErrorLog("Ʒ�ֱ���Ϊ�գ�");continue;}
			faz_id = sv.getChezxxb_id(con, ehead.getChildText("��վ����"));
			if(faz_id == null) {	WriteLog.writeErrorLog("��վ����Ϊ�գ�");continue;}
			daoz_id = sv.getChezxxb_id(con, ehead.getChildText("��վ����"));
			if(daoz_id == null) {	WriteLog.writeErrorLog("��վ����Ϊ�գ�");continue;}
			jihkjb_id = sv.getJihkjb_id(con, ehead.getChildText("�ھ�����")); 
			if(jihkjb_id == null) {		WriteLog.writeErrorLog("�ھ�����Ϊ�գ�");continue;}
			fahrq = sv.getOracleDate(ehead.getChildText("��������"));
			if(fahrq == null) {		WriteLog.writeErrorLog("��������Ϊ�գ�");continue;}
			daohrq = sv.getOracleDate(ehead.getChildText("��������"));
			if(daohrq == null) {	WriteLog.writeErrorLog("��������Ϊ�գ�");continue;}
			caiyrq = sv.getOracleDate(ehead.getChildText("��������"));
			if(caiyrq == null) {	WriteLog.writeErrorLog("��������Ϊ�գ�");continue;}
			caiybm = ehead.getChildText("��������");
			if(caiybm == null) {	WriteLog.writeErrorLog("��������Ϊ�գ�");continue;}
			yunsfsb_id = sv.getYunsfs_id(ehead.getChildText("���䷽ʽ"));
			if(yunsfsb_id == null) {	WriteLog.writeErrorLog("���䷽ʽΪ�գ�");continue;}
			chec = ehead.getChildText("����"); 
			if(chec == null) {	WriteLog.writeErrorLog("����Ϊ�գ�");continue;}
			cheh = ehead.getChildText("����");
			if(cheh == null) {	WriteLog.writeErrorLog("����Ϊ�գ�");continue;}
			maoz = sv.getDouble(ehead.getChildText("ë��")); 
			if(maoz == null) {	WriteLog.writeErrorLog("ë��Ϊ�գ�");continue;}
			piz = sv.getDouble(ehead.getChildText("Ƥ��"));
			if(piz == null) {	WriteLog.writeErrorLog("Ƥ��Ϊ�գ�");continue;}
			piaoz = sv.getDouble(ehead.getChildText("Ʊ��")); 
			if(piaoz == null) {	WriteLog.writeErrorLog("Ʊ��Ϊ�գ�");continue;}
			koud = sv.getDouble(ehead.getChildText("�۶�"));
			if(koud == null) {	WriteLog.writeErrorLog("�۶�Ϊ�գ�");continue;}
			kous = sv.getDouble(ehead.getChildText("��ˮ")); 
			if(kous == null) {	WriteLog.writeErrorLog("��ˮΪ�գ�");continue;}
			kouz = sv.getDouble(ehead.getChildText("����"));
			if(kouz == null) {	WriteLog.writeErrorLog("����Ϊ�գ�");continue;}
			sanfsl = sv.getDouble(ehead.getChildText("��������"));
			if(sanfsl == null) {	WriteLog.writeErrorLog("��������Ϊ�գ�");continue;}
			jianjfs = ehead.getChildText("��﷽ʽ");
			if(jianjfs == null) {	WriteLog.writeErrorLog("��﷽ʽΪ�գ�");continue;}
			chebb_id = sv.getCheb_id(ehead.getChildText("����"));
			if(chebb_id == null) {	WriteLog.writeErrorLog("����Ϊ�գ�");continue;}
			yuandz_id = sv.getChezxxb_id(con, ehead.getChildText("ԭ��վ����"));
			if(yuandz_id == null) {	WriteLog.writeErrorLog("ԭ��վ����Ϊ�գ�");continue;}
			yuanshdwb_id = sv.getYuanshdw_id(con, ehead.getChildText("ԭ�ջ���λ"));
			if(yuanshdwb_id == null) {	WriteLog.writeErrorLog("ԭ�ջ���λΪ�գ�");continue;}
			yuanmkdw = ehead.getChildText("ԭú��λ");
			if(yuanmkdw == null) {	WriteLog.writeErrorLog("ԭú��λΪ�գ�");continue;}
			zhongcjjy = ehead.getChildText("�س����Ա");
			if(zhongcjjy == null) {	WriteLog.writeErrorLog("�س����ԱΪ�գ�");continue;}
			zhongcsj = sv.getOracleDateTime(ehead.getChildText("�س�ʱ��"));
			if(zhongcsj == null) {	WriteLog.writeErrorLog("�س�ʱ��Ϊ�գ�");continue;}
			zhongchh = ehead.getChildText("�س����");
			if(zhongchh == null) {	WriteLog.writeErrorLog("�س����Ϊ�գ�");continue;}
			qingcjjy = ehead.getChildText("�ᳵ���Ա");
			if(qingcjjy == null) {	WriteLog.writeErrorLog("�ᳵ���ԱΪ�գ�");continue;}
			qingcsj = sv.getOracleDateTime(ehead.getChildText("�ᳵʱ��"));
			if(qingcsj == null) {	WriteLog.writeErrorLog("�ᳵʱ��Ϊ�գ�");continue;}
			qingchh = ehead.getChildText("�ᳵ���");
			if(qingchh == null) {	WriteLog.writeErrorLog("�ᳵ���Ϊ�գ�");continue;}
			daozch = ehead.getChildText("��װ����");
			if(daozch == null) {	WriteLog.writeErrorLog("��װ����Ϊ�գ�");continue;}
			lursj = sv.getOracleDateTime(ehead.getChildText("¼��ʱ��"));
			if(lursj == null) {	WriteLog.writeErrorLog("¼��ʱ��Ϊ�գ�");continue;}
			lurry = ehead.getChildText("¼����Ա"); 
			if(lurry == null) {	WriteLog.writeErrorLog("¼����ԱΪ�գ�");continue;}
			yunsdwb_id = sv.getYunsdw_id(con, ehead.getChildText("���䵥λ"));
			if(yunsdwb_id == null) {	yunsdwb_id = "-1";}
			beiz = ehead.getChildText("��ע");
			if(beiz == null) {	beiz = "";}
			sb.append("insert into cheplsb(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, \n")
			.append("faz_id, daoz_id, jihkjb_id, fahrq, daohrq, caiyrq, caiybh, yunsfsb_id, \n")
			.append("chec, cheph, maoz, piz, biaoz, koud, kous, kouz, sanfsl, jianjfs, chebb_id, \n")
			.append("yuandz_id, yuanshdwb_id, yuanmkdw, zhongcjjy, zhongcsj, zhongchh, qingcjjy, \n")
			.append("qingcsj, qingchh, daozch, lursj, lury, yunsdwb_id, beiz) \n")
			.append("values(getnewid(").append(diancxxb_id).append("),")
			.append(diancxxb_id).append(",").append(gongysb_id).append(",")
			.append(meikxxb_id).append(",").append(pinzb_id).append(",")
			.append(faz_id).append(",").append(daoz_id).append(",")
			.append(jihkjb_id).append(",").append(fahrq).append(",")
			.append(daohrq).append(",").append(caiyrq).append(",'")
			.append(caiybm).append("',").append(yunsfsb_id).append(",'")
			.append(chec).append("','").append(cheh).append("',")
			.append(maoz).append(",").append(piz).append(",")
			.append(piaoz).append(",").append(koud).append(",")
			.append(kous).append(",").append(kouz).append(",")
			.append(sanfsl).append(",'").append(jianjfs).append("',")
			.append(chebb_id).append(",").append(yuandz_id).append(",")
			.append(yuanshdwb_id).append(",'").append(yuanmkdw).append("','")
			.append(zhongcjjy).append("',").append(zhongcsj).append(",'")
			.append(zhongchh).append("','").append(qingcjjy).append("',")
			.append(qingcsj).append(",'").append(qingchh).append("','")
			.append(daozch).append("',").append(lursj).append(",'")
			.append(lurry).append("',").append(yunsdwb_id).append(",'")
			.append(beiz).append("');\n");
		}
		sb.append("end;\n");
		int flag = con.getInsert(sb.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
					+"SQL:"+sb);
			return;
		}
		flag = Jilcz.Updatezlid(con, Long.parseLong(diancxxb_id),
				SysConstant.YUNSFS_HUOY, "");
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog("����ú��������ID��ʧ�ܣ�");
			return;
		}
		flag = Jilcz.Updatezlid(con, Long.parseLong(diancxxb_id),
				SysConstant.YUNSFS_QIY, "");
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog("����ú��������ID��ʧ�ܣ�");
			return;
		}
		flag = Jilcz.INSorUpfahb(con, Long.parseLong(diancxxb_id));
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog("���·�����ʧ�ܣ�");
			return;
		}
		flag = Jilcz.InsChepb(con, Long.parseLong(diancxxb_id),
				"lursj" , 9);
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog("���복Ƥ��ʧ�ܣ�");
			return;
		}
		sb.delete(0, sb.length());
		sb.append("select * from chepb where hedbz=9");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			flag = Jilcz.CountChepbYuns(con, rsl.getString("id"),SysConstant.HEDBZ_YJJ);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog("��������ӯ����������");
				return;
			}
		}
		sb.delete(0, sb.length());
		sb.append("select distinct fahb_id from cheplsb");
		rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			flag = Jilcz.updateLieid(con, rsl.getString("fahb_id"));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog("������IDʧ�ܣ�");
				return;
			}
		}
		con.commit();
		con.Close();
	}
	
	public static void Run() {
		com.zhiren.common.WriteLog.writeInfoLog("������:"+DateUtil.Formatdate("yyyy��MM��dd��HHʱmm��ss��", new Date())+"  �ļ�����ʼ...>_< ");
		String filepath = MainGlobal.getXitsz("ϵͳ�ļ���λ��", "0", "C:/zhiren/")+"/webservice/receive";
		String dirrec = filepath +"/webservice/receive";
		String dirrecbak = filepath +"/webservice/receivebak";
		File Dir = new File(dirrec);
		if(!Dir.exists()){
			Dir.mkdirs();
		}
		if (Dir.isDirectory()) {
			FileNameFilter fnf = new FileNameFilter("xml");
			File xmls[] = Dir.listFiles(fnf);
			for (int i = 0; i < xmls.length; i++) {
				try {
					SAXBuilder builder = new SAXBuilder();
					FileInputStream fiss = new FileInputStream(xmls[i]);
					Document docw = builder.build(fiss);
					Element root = docw.getRootElement();
					List elist = root.getChildren();
					WriteLog.writeInfoLog("���ڴ����ļ�"+(i+1)+":" + root.getName());
					if(xmlName_Shulsjdr.equals(root.getName())) {
						SaveShulsjdr(elist);
					}
					
					File bakdir = new File(dirrecbak);
					if(!bakdir.exists()) {
						bakdir.mkdirs();
					}
					File bakfile = new File(bakdir, xmls[i].getName()+DateUtil.Formatdate("yyyyMMddHH", new Date()));
					if (bakfile.exists()) {
						bakfile.delete();
					}
					xmls[i].renameTo(bakfile);
					fiss.close();
					xmls[i].delete();
				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		WriteLog.writeInfoLog("������:"+DateUtil.Formatdate("yyyy��MM��dd��HHʱmm��ss��", new Date())+"  �ļ��������...>_< ");
	}

	public static void main(String[] args) throws Exception {
		Run();
	}
}
