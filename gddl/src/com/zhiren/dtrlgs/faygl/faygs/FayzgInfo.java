package com.zhiren.dtrlgs.faygl.faygs;

import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
/*
 * 作者:tzf
 * 时间:2009-07-01
 * 修改内容:CountChengb方法的jdbc链接，应该用一个事物中的链接     否则  当  对 同一个发货进行操作时，前一个事物没有commit，
 * 后一个事物要进行确认，可能使系统停滞.
 */
public class FayzgInfo {

	private static final int noIgrone = 0;
	private static final int Igrone_Faz = 1;
	private static final int Igrone_Meik = 2;
	
	public FayzgInfo() {
		// TODO 自动生成构造函数存根
	}
	
	public static void addFahid(List list,String fahid) {
		if(list == null) {
			list = new ArrayList();
		}
		int i=0;
		for( ;i<list.size() ;i++) {
			if(((String)list.get(i)).equals(fahid)) {
				break;
			}
		}
		if(i == list.size()) {
			list.add(fahid);
		}
	}
	
	public static void CountChengb(long diancxxb_id,List fhlist,boolean qiangzcs) {
		String strzglb="大唐国际";
		strzglb=MainGlobal.getXitxx_item("成本", "暂估算法分类", String.valueOf(diancxxb_id), strzglb).trim();
		
		
		JDBCcon con = new JDBCcon();
		List list = new ArrayList();
		for(int i =0;i<fhlist.size();i++) {
			String fhid = (String)fhlist.get(i);
			long htid = UpdateFahHt(con,diancxxb_id,fhid);
			String ruccbb_id = saveRuccb(con,diancxxb_id,fhid,htid);
			if(ruccbb_id != null) {
				addFahid(list, ruccbb_id);
			}
		}
		con.Close();
		if(strzglb.equals("中国大唐")){
			
		}else if(strzglb.equals("大唐国际")){
			Fayzgcl.CountCb_PerFah(fhlist, qiangzcs);
		}		
//		因为结算改动造成成本模块出现BUG 2008-11-04
//		Chengbcl.CountCb(list, diancxxb_id, 0);
	}
	
	public static void CountChengb(List list,boolean qiangzcs) {//List中存放diancxxb_id 和 fayslb_id
		String strzglb="大唐国际";
//		strzglb=MainGlobal.getXitxx_item("成本", "暂估算法分类", String.valueOf(diancxxb_id), strzglb).trim();
		
		
		JDBCcon con = new JDBCcon();
		List Fahidlist = new ArrayList();
		List cblist = new ArrayList();
		for(int i =0;i<list.size();i++) {
			long dcid = ((FaycbBean)list.get(i)).getDiancID();
			String fhid = ""+((FaycbBean)list.get(i)).getFhfyID();//fahb_id||fayslb_id
			
			addFahid(Fahidlist, fhid);
			
			long htid = UpdateFahHt(con,dcid,fhid);
			String ruccbb_id = saveRuccb(con,dcid,fhid,htid);
			if(ruccbb_id != null) {
				addFahid(cblist, ruccbb_id);
			}
		}
		con.Close();
		if(strzglb.equals("中国大唐")){
			
		}else if(strzglb.equals("大唐国际")){
			Fayzgcl.CountCb_PerFah(Fahidlist, qiangzcs);
		}		
//		因为结算改动造成成本模块出现BUG 2008-11-04
//		Chengbcl.CountCb(list, diancxxb_id, 0);
	}
	
	public static void CountChengb(JDBCcon con,long diancxxb_id,List fhlist,boolean qiangzcs) {
		String strzglb="大唐国际";
		strzglb=MainGlobal.getXitxx_item("成本", "暂估算法分类", String.valueOf(diancxxb_id), strzglb).trim();
		
		if(con==null){
			 con = new JDBCcon();
		}
//		JDBCcon con = new JDBCcon();
		List list = new ArrayList();
		for(int i =0;i<fhlist.size();i++) {
			String fhid = (String)fhlist.get(i);
			long htid = UpdateFahHt(con,diancxxb_id,fhid);
			String ruccbb_id = saveRuccb(con,diancxxb_id,fhid,htid);
			if(ruccbb_id != null) {
				addFahid(list, ruccbb_id);
			}
		}
		
	//	con.Close();
		if(strzglb.equals("中国大唐")){
			
		}else if(strzglb.equals("大唐国际")){
			Fayzgcl.CountCb_PerFah(fhlist, qiangzcs);
		}		
//		因为结算改动造成成本模块出现BUG 2008-11-04
//		Chengbcl.CountCb(list, diancxxb_id, 0);
	}
	public static void CountChengb(JDBCcon con,long diancxxb_id,List fhlist) {
		CountChengb(con,diancxxb_id,fhlist,false);
	}
	
	public static void CountChengb(long diancxxb_id,List fhlist) {
		CountChengb(diancxxb_id,fhlist,false);
	}
	
	
	public static long UpdateFahHt(JDBCcon con,long diancxxb_id,String fahbid) {
		long htid = 0;
//		long newhtid = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select hetb_id from fayslb where id=").append(fahbid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			htid = rsl.getLong("hetb_id");
		}
//		if(newhtid == 0) {
//			rsl = con.getResultSetList(getHtidSql(noIgrone,fahbid));
//			if(rsl.next()) {
//				newhtid = rsl.getLong("hetb_id");
//			}
//		}
//		if(newhtid == 0) {
//			rsl = con.getResultSetList(getHtidSql(Igrone_Faz,fahbid));
//			if(rsl.next()) {
//				newhtid = rsl.getLong("hetb_id");
//			}
//		}
//		if(newhtid == 0) {
//			rsl = con.getResultSetList(getHtidSql(Igrone_Meik,fahbid));
//			if(rsl.next()) {
//				newhtid = rsl.getLong("hetb_id");
//			}
//		}
//		if(newhtid != htid){
//			con.getUpdate("update fayslb set hetb_id = "+newhtid+" where id="+fahbid);
//		}
//		return newhtid;
		return htid;
	}
	
	public static String saveRuccb(JDBCcon con,long diancxxb_id,String fhid,long htid) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ruccbb_id from fayslb where to_char(daohrq,'yyyy-mm') =(select to_char(daohrq,'yyyy-mm') from fayslb where id=")
		.append(fhid).append(") and hetb_id=").append(htid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		String ruccbb_id = null;
		if(rsl.next()) {
			ruccbb_id = rsl.getString(0);
		}
		if(ruccbb_id == null || "".equals(ruccbb_id)) {
			sb.delete(0, sb.length());
			ruccbb_id = MainGlobal.getNewID(diancxxb_id);
			sb.append("insert into ruccb (id,FAYSLB_ID)")
			.append("values(").append(ruccbb_id)
			.append(",").append(fhid).append(")");
			int flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				WriteLog.writeErrorLog("插入入厂成本时发生错误！");
			}
		}
		if(ruccbb_id != null && !"0".equals(ruccbb_id)) {
			sb.delete(0, sb.length());
			sb.append("update fayslb set ruccbb_id=")
			.append(ruccbb_id)
			.append(" where id=").append(fhid);
			con.getUpdate(sb.toString());
			return ruccbb_id;
		}
		return null;
	}
	
	public static String getHtidSql(int IntModel,String fahid) {
		StringBuffer sb = new StringBuffer();
		sb.append("select h.hetb_id from fayslb f, fahhtb h\n")
		.append("where f.id =").append(fahid).append("\n")
		.append("and f.gongysb_id = h.gongysb_id\n");
		switch(IntModel) {
			case Igrone_Meik:sb.append("and h.meikxxb_id = -1 and h.faz_id = -1\n");break;
			case Igrone_Faz:sb.append("and h.meikxxb_id = f.meikxxb_id and h.faz_id = -1\n");break;
			default:sb.append("and h.meikxxb_id = f.meikxxb_id and h.faz_id = f.faz_id\n");break;
		}
		sb.append(" and f.fahrq >= h.qisrq and f.fahrq <= h.jiesrq");
		return sb.toString();
	}
}
