package com.zhiren.dc.chengbgl;

import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.dc.jilgl.Jilcz;
/*
 * 作者：王磊
 * 时间：2009-10-29
 * 描述：增加关联合同时对品种的判断
 */
/*
 * 作者:tzf
 * 时间:2009-07-01
 * 修改内容:CountChengb方法的jdbc链接，应该用一个事物中的链接     否则  当  对 同一个发货进行操作时，前一个事物没有commit，
 * 后一个事物要进行确认，可能使系统停滞.
 */
/*
 * 作者：王磊
 * 时间：2009-11-30
 * 描述：生成成本表ID时如果合同表ID为0 则重新生成成本表ID
 */
/*
 * 作者：王磊
 * 时间：2009-12-03
 * 描述：修改成本计算时发货合同匹配及入厂成本生成的方式 现在的成本计算按照每质量对应一条成本记录
 */
public class Chengbjs {

	private static final int noIgnore = 0;//匹配合同时，按照供应商、煤矿、发站、品种完全匹配
	private static final int Ignore_Faz = 1;//匹配合同时，按照供应商、煤矿、品种进行匹配，仅忽略发站条件
	private static final int Ignore_Pinz = 2;//匹配合同时，按照供应商、煤矿、发站进行匹配，仅忽略品种条件
	private static final int Ignore_Meik = 3;//匹配合同时，按照供应商、发站、品种进行匹配，仅忽略煤矿条件
	private static final int Ignore_Faz_Pinz = 4;//匹配合同时，按照供应商、煤矿进行匹配，忽略发站、品种条件
	private static final int Ignore_Meik_Faz = 5;//匹配合同时，按照供应商、品种进行匹配，忽略发站、煤矿条件
	private static final int Ignore_Meik_Pinz = 6;//匹配合同时，按照供应商、发站进行匹配，忽略品种、煤矿条件
	private static final int Ignore_All = 7;//匹配合同时，仅匹配供应商，忽略其他相关条件
	
	public Chengbjs() {
		// TODO 自动生成构造函数存根
	}
	public static void CountChengb(long diancxxb_id,List fhlist,boolean qiangzcs) {
//		String strzglb="中国大唐";
//		strzglb=MainGlobal.getXitxx_item("成本", "暂估算法分类", String.valueOf(diancxxb_id), strzglb).trim();
		String strzglb="大唐国际";
		
		
		JDBCcon con = new JDBCcon();
//		List list = new ArrayList();
		for(int i =0;i<fhlist.size();i++) {
			String fhid = (String)fhlist.get(i);
			long htid = UpdateFahHt(con,diancxxb_id,fhid);
//			String ruccbb_id = saveRuccb(con,diancxxb_id,fhid,htid);
//			if(ruccbb_id != null) {
//				Jilcz.addFahid(list, ruccbb_id);
//			}
		}
		con.Close();
		//if(strzglb.equals("大唐国际")){
			
			Chengbcl.CountCb_PerFah(fhlist, true);
		//}		
	}
	
	
	public static void CountChengb(JDBCcon con,long diancxxb_id,List fhlist,boolean qiangzcs) {
//		String strzglb="中国大唐";
//		strzglb=MainGlobal.getXitxx_item("成本", "暂估算法分类", String.valueOf(diancxxb_id), strzglb).trim();
		String strzglb="大唐国际";
		if(con==null){
			 con = new JDBCcon();
		}
//		JDBCcon con = new JDBCcon();
//		List list = new ArrayList();
		for(int i =0;i<fhlist.size();i++) {
			String fhid = (String)fhlist.get(i);
			long htid = UpdateFahHt(con,diancxxb_id,fhid);
//			String ruccbb_id = saveRuccb(con,diancxxb_id,fhid,htid);
//			if(ruccbb_id != null) {
//				Jilcz.addFahid(list, ruccbb_id);
//			}
		}
	//	con.Close();
//		if(strzglb.equals("中国大唐")){
//			
//		}else if(strzglb.equals("大唐国际")){
//			Chengbcl.CountCb_PerFah(fhlist, qiangzcs);
//		}	
		if(strzglb.equals("大唐国际")){
			
			Chengbcl.CountCb_PerFah(fhlist, true);
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
//		函数作用，为要计算成本的fahb记录找到最合适的合同信息
		long htid = 0;	//发货审核时已经找到的hetid
		long newhtid = 0;
		StringBuffer sb = new StringBuffer();
//		1、取到现有发货的合同表id
		sb.append("select hetb_id from fahb where id=").append(fahbid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			htid = rsl.getLong("hetb_id");
		}
		rsl.close();
//		2011年6月24日 Qiuzw 根据发货合同维护的信息匹配合同
		for (int i = noIgnore; i <= Ignore_All; i++) {
			if (newhtid == 0) {
				rsl = con.getResultSetList(getHtidSql(i, fahbid));
				if (rsl.next()) {
					newhtid = rsl.getLong("hetb_id");
					if (newhtid != 0)
						break;
				}
				rsl.close();
			} else
				break;
		}
		
//		如果发货已有的hetb_id不等于人为匹配的hetb_id则已人为指定匹配的合同表id为准
		if(newhtid != htid){
			if (newhtid!=0){
				//如果原有的发货有合同，而没有匹配到新合同则不更新
				con.getUpdate("update fahb set hetb_id = "+newhtid+" where id="+fahbid);
			}
		}
		return newhtid;
	}
	
	public static String saveRuccb(JDBCcon con,long diancxxb_id,String fhid,long htid) {
//		函数作用：得到要计算入厂成本的发货信息对应的入厂成本表id
		StringBuffer sb = new StringBuffer();
		String ruccbb_id = null;
		String oraDate = null;
		String zhilb_id = null;
//		按照质量生成入厂成本
		sb.append("select hetb_id,daohrq,zhilb_id from fahb where id = " + fhid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()){
			oraDate = DateUtil.FormatOracleDate(rsl.getDateString("daohrq"));
			zhilb_id = rsl.getString("zhilb_id");
		}
		rsl.close();
		if(oraDate != null && zhilb_id !=null){
			sb.delete(0, sb.length());
			sb.append("select ruccbb_id from fahb where daohrq =").append(oraDate)
			.append(" and zhilb_id=").append(zhilb_id).append(" and hetb_id=")
			.append(htid);
			rsl = con.getResultSetList(sb.toString());
			if(rsl.next()) {
				ruccbb_id = rsl.getString(0);
			}
		}
//		1、找到与此发货同一天到货的且使用同一个合同的发货信息，我们认为这些发货应对应同一个入厂成本，找到这个入厂成本表id即可
//		if(htid > 0){
//			sb.append("select ruccbb_id from fahb where to_char(daohrq,'yyyy-mm') =(select to_char(daohrq,'yyyy-mm') from fahb where id=")
//			.append(fhid).append(") and hetb_id=").append(htid);
//			ResultSetList 
//		}
		if(ruccbb_id == null || "".equals(ruccbb_id)) {
//			如果发现没有得到入厂成本表id，就要新插入入厂成本信息
			sb.delete(0, sb.length());
			ruccbb_id = MainGlobal.getNewID(diancxxb_id);
			sb.append("insert into ruccb (id,fahb_id)")
			.append("values(").append(ruccbb_id)
			.append(",").append(fhid).append(")");
			int flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				WriteLog.writeErrorLog("插入入厂成本时发生错误！");
			}
		}
		if(ruccbb_id != null && !"0".equals(ruccbb_id)) {
//			更新发货信息的入厂成本表id
			sb.delete(0, sb.length());
			sb.append("update fahb set ruccbb_id=")
			.append(ruccbb_id)
			.append(" where id=").append(fhid);
			con.getUpdate(sb.toString());
			return ruccbb_id;
		}
		return null;
	}
	
	public static String getHtidSql(int IntModel,String fahid) {
		//加入hetb的连接，避免匹配到一个已删除的合同
		StringBuffer sb = new StringBuffer();
		sb.append("select h.hetb_id from fahb f, fahhtb h,hetb ht\n")
		.append("where f.id =").append(fahid).append("\n")
		.append("and f.gongysb_id = h.gongysb_id\n")
		.append("and f.diancxxb_id = h.diancxxb_id and h.hetb_id=ht.id \n");
		switch(IntModel) {
			case Ignore_Meik:
				//仅忽略煤矿的情况
				sb.append("and h.meikxxb_id = -1 and h.faz_id = f.faz_id and h.pinzb_id = f.pinzb_id\n");
				break;
			case Ignore_Pinz:
				//仅忽略品种的情况
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.pinzb_id = -1 and h.faz_id = f.faz_id\n");
				break;
			case Ignore_Faz:
				//仅忽略发站的情况
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.pinzb_id = f.pinzb_id and h.faz_id = -1\n");
				break;
			case Ignore_Faz_Pinz:
				//同时忽略发站、品种
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.faz_id = -1 and h.pinzb_id = -1\n");
				break;
			case Ignore_Meik_Faz:
				//同时忽略煤矿、发站
				sb.append("and h.meikxxb_id = -1 and h.pinzb_id = f.pinzb_id and h.faz_id = -1\n");
				break;
			case Ignore_Meik_Pinz:
				//同时忽略煤矿、品种
				sb.append("and h.meikxxb_id = -1 and h.pinzb_id = -1 and h.faz_id = f.faz_id\n");
				break;
			case noIgnore:
				//最严格匹配，考虑全部条件匹配
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.faz_id = f.faz_id and h.pinzb_id = f.pinzb_id\n");
				break;
			case Ignore_All:
				break;
			default:
				//等同于Ignore_All
				break;
		}
		sb.append(" and f.fahrq >= h.qisrq and f.fahrq <= h.jiesrq  Order By h.qisrq Desc, h.jiesrq ");
		return sb.toString();
	}
}