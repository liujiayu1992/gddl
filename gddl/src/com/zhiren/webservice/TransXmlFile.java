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
	
	public static final String xmlName_Shulsjdr = "数量数据导入";
	
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
			diancxxb_id = sv.getDiancxxb_id(con, ehead.getChildText("电厂编码"));
			if(diancxxb_id == null) {	WriteLog.writeErrorLog("电厂编码为空！");continue;}
			gongysb_id = sv.getGongysb_id(con, ehead.getChildText("供应商编码"));
			if(gongysb_id == null) {	WriteLog.writeErrorLog("供应商编码为空！");continue;}
			meikxxb_id = sv.getMeikxxb_id(con, ehead.getChildText("煤矿编码"));
			if(meikxxb_id == null) {	WriteLog.writeErrorLog("煤矿编码为空！");continue;}
			pinzb_id = sv.getPinzb_id(con, ehead.getChildText("品种编码"));
			if(pinzb_id == null) {	WriteLog.writeErrorLog("品种编码为空！");continue;}
			faz_id = sv.getChezxxb_id(con, ehead.getChildText("发站编码"));
			if(faz_id == null) {	WriteLog.writeErrorLog("发站编码为空！");continue;}
			daoz_id = sv.getChezxxb_id(con, ehead.getChildText("到站编码"));
			if(daoz_id == null) {	WriteLog.writeErrorLog("到站编码为空！");continue;}
			jihkjb_id = sv.getJihkjb_id(con, ehead.getChildText("口径编码")); 
			if(jihkjb_id == null) {		WriteLog.writeErrorLog("口径编码为空！");continue;}
			fahrq = sv.getOracleDate(ehead.getChildText("发货日期"));
			if(fahrq == null) {		WriteLog.writeErrorLog("发货日期为空！");continue;}
			daohrq = sv.getOracleDate(ehead.getChildText("到货日期"));
			if(daohrq == null) {	WriteLog.writeErrorLog("到货日期为空！");continue;}
			caiyrq = sv.getOracleDate(ehead.getChildText("采样日期"));
			if(caiyrq == null) {	WriteLog.writeErrorLog("采样日期为空！");continue;}
			caiybm = ehead.getChildText("采样编码");
			if(caiybm == null) {	WriteLog.writeErrorLog("采样编码为空！");continue;}
			yunsfsb_id = sv.getYunsfs_id(ehead.getChildText("运输方式"));
			if(yunsfsb_id == null) {	WriteLog.writeErrorLog("运输方式为空！");continue;}
			chec = ehead.getChildText("车次"); 
			if(chec == null) {	WriteLog.writeErrorLog("车次为空！");continue;}
			cheh = ehead.getChildText("车号");
			if(cheh == null) {	WriteLog.writeErrorLog("车号为空！");continue;}
			maoz = sv.getDouble(ehead.getChildText("毛重")); 
			if(maoz == null) {	WriteLog.writeErrorLog("毛重为空！");continue;}
			piz = sv.getDouble(ehead.getChildText("皮重"));
			if(piz == null) {	WriteLog.writeErrorLog("皮重为空！");continue;}
			piaoz = sv.getDouble(ehead.getChildText("票重")); 
			if(piaoz == null) {	WriteLog.writeErrorLog("票重为空！");continue;}
			koud = sv.getDouble(ehead.getChildText("扣吨"));
			if(koud == null) {	WriteLog.writeErrorLog("扣吨为空！");continue;}
			kous = sv.getDouble(ehead.getChildText("扣水")); 
			if(kous == null) {	WriteLog.writeErrorLog("扣水为空！");continue;}
			kouz = sv.getDouble(ehead.getChildText("扣杂"));
			if(kouz == null) {	WriteLog.writeErrorLog("扣杂为空！");continue;}
			sanfsl = sv.getDouble(ehead.getChildText("三方数量"));
			if(sanfsl == null) {	WriteLog.writeErrorLog("三方数量为空！");continue;}
			jianjfs = ehead.getChildText("检斤方式");
			if(jianjfs == null) {	WriteLog.writeErrorLog("检斤方式为空！");continue;}
			chebb_id = sv.getCheb_id(ehead.getChildText("车别"));
			if(chebb_id == null) {	WriteLog.writeErrorLog("车别为空！");continue;}
			yuandz_id = sv.getChezxxb_id(con, ehead.getChildText("原到站编码"));
			if(yuandz_id == null) {	WriteLog.writeErrorLog("原到站编码为空！");continue;}
			yuanshdwb_id = sv.getYuanshdw_id(con, ehead.getChildText("原收货单位"));
			if(yuanshdwb_id == null) {	WriteLog.writeErrorLog("原收货单位为空！");continue;}
			yuanmkdw = ehead.getChildText("原煤矿单位");
			if(yuanmkdw == null) {	WriteLog.writeErrorLog("原煤矿单位为空！");continue;}
			zhongcjjy = ehead.getChildText("重车检斤员");
			if(zhongcjjy == null) {	WriteLog.writeErrorLog("重车检斤员为空！");continue;}
			zhongcsj = sv.getOracleDateTime(ehead.getChildText("重车时间"));
			if(zhongcsj == null) {	WriteLog.writeErrorLog("重车时间为空！");continue;}
			zhongchh = ehead.getChildText("重车衡号");
			if(zhongchh == null) {	WriteLog.writeErrorLog("重车衡号为空！");continue;}
			qingcjjy = ehead.getChildText("轻车检斤员");
			if(qingcjjy == null) {	WriteLog.writeErrorLog("轻车检斤员为空！");continue;}
			qingcsj = sv.getOracleDateTime(ehead.getChildText("轻车时间"));
			if(qingcsj == null) {	WriteLog.writeErrorLog("轻车时间为空！");continue;}
			qingchh = ehead.getChildText("轻车衡号");
			if(qingchh == null) {	WriteLog.writeErrorLog("轻车衡号为空！");continue;}
			daozch = ehead.getChildText("倒装车号");
			if(daozch == null) {	WriteLog.writeErrorLog("倒装车号为空！");continue;}
			lursj = sv.getOracleDateTime(ehead.getChildText("录入时间"));
			if(lursj == null) {	WriteLog.writeErrorLog("录入时间为空！");continue;}
			lurry = ehead.getChildText("录入人员"); 
			if(lurry == null) {	WriteLog.writeErrorLog("录入人员为空！");continue;}
			yunsdwb_id = sv.getYunsdw_id(con, ehead.getChildText("运输单位"));
			if(yunsdwb_id == null) {	yunsdwb_id = "-1";}
			beiz = ehead.getChildText("备注");
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
			WriteLog.writeErrorLog("火运煤更新质量ID表失败！");
			return;
		}
		flag = Jilcz.Updatezlid(con, Long.parseLong(diancxxb_id),
				SysConstant.YUNSFS_QIY, "");
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog("汽运煤更新质量ID表失败！");
			return;
		}
		flag = Jilcz.INSorUpfahb(con, Long.parseLong(diancxxb_id));
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog("更新发货表失败！");
			return;
		}
		flag = Jilcz.InsChepb(con, Long.parseLong(diancxxb_id),
				"lursj" , 9);
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog("插入车皮表失败！");
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
				WriteLog.writeErrorLog("计算运损盈亏发生错误！");
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
				WriteLog.writeErrorLog("更新列ID失败！");
				return;
			}
		}
		con.commit();
		con.Close();
	}
	
	public static void Run() {
		com.zhiren.common.WriteLog.writeInfoLog("现在是:"+DateUtil.Formatdate("yyyy年MM月dd日HH时mm分ss秒", new Date())+"  文件处理开始...>_< ");
		String filepath = MainGlobal.getXitsz("系统文件夹位置", "0", "C:/zhiren/")+"/webservice/receive";
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
					WriteLog.writeInfoLog("正在处理文件"+(i+1)+":" + root.getName());
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
		WriteLog.writeInfoLog("现在是:"+DateUtil.Formatdate("yyyy年MM月dd日HH时mm分ss秒", new Date())+"  文件处理结束...>_< ");
	}

	public static void main(String[] args) throws Exception {
		Run();
	}
}
