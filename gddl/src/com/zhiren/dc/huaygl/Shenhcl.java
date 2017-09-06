package com.zhiren.dc.huaygl;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

/*
 * 作者：夏峥
 * 时间：2014-03-14
 * 描述：增加热值大卡的显示
 */

public class Shenhcl {

	public static String Judgment(JDBCcon con, String Bianh) {
		String sb="select zhi from xitxxb where mingc='最终化验编号设置' and leib = '化验' and zhuangt =1";
		String zhi="自动";
		boolean IShuaybh=false;
		ResultSetList rsl=con.getResultSetList(sb);
		if(rsl.next()){
			zhi = rsl.getString("zhi");
		}
		
		sb="select * from xitxxb x where x.mingc='入厂化验二级审核插入zhilb的huaybh是否是zhuanmb中的化验编号'and leib='化验' and zhi='是' and zhuangt=1";
		rsl=con.getResultSetList(sb);
		if(rsl.next()){
			IShuaybh=true;
		}
		String sql = "select z.id,\n"
			+ "       m.mingc meikdw,\n"
			+ "       cz.mingc as faz,\n"
			+ "       c.id as caiyb_id,\n"
			+ "       p.mingc as pinz,\n"
			+ "       f.ches as ches,\n"
			+ "       f.jingz as shul,\n";
	
			if(zhi.equals("进厂批号")){
				sql+="c.bianm as huaybh,\n";
			}else if(IShuaybh){
				
				sql+="zm.bianm as huaybh,\n";
				
			}else{
				sql+= "       (select max(zl.bianh) as bianh\n"
					+ "          from (select substr(to_char(Sysdate, 'yyyy') ||\n"
					+ "                              decode(to_char(Sysdate, 'yy'),\n"
					+ "                                     substr(max(huaybh), 0, 2),\n"
					+ "                                     substr(max(huaybh), 3),\n"
					+ "                                     '00000') + 1,\n"
					+ "                              3) as bianh\n"
					+ "                  from zhilb) zl) as huaybh,\n";
			}
			sql+= "   z.huaysj,\n"
			+"       round_new(z.qnet_ar/4.1816*1000,0)rezk,\n"
			+ "       z.qnet_ar,\n"
			+ "       z.aar,\n"
			+ "       z.ad,\n"
			+ "       z.vdaf,\n"
			+ "       z.mt,\n"
			+ "       z.stad,\n"
			+ "       z.aad,\n"
			+ "       z.mad,\n"
			+ "       z.qbad,\n"
			+ "       z.had,\n"
			+ "       z.vad,\n"
			+ "       z.fcad,\n"
			+ "       z.std,\n"
			+ "       z.qgrad,\n"
			+ "       z.qgrd,\n"
			+ "       z.hdaf,\n"
			+ "       z.qgrad_daf,\n"
			+ "       z.sdaf,\n"
			+ "       z.t1,\n"
			+ "       z.t2,\n"
			+ "       z.t3,\n"
			+ "       z.t4,\n"
			+ "       GetHuayy(z.id) as huayy,\n"
			+ "       GetLury(z.id) as lury,\n"
			+ "       GetBeiz(z.id) as beiz\n"
			+ "  from (select max(l.zhilb_id) as id,\n"
			+ "               max(l.huaysj) as huaysj,\n"
			+ "               avg(l.qnet_ar) qnet_ar,\n"
			+ "               avg(l.aar) aar,\n"
			+ "               avg(l.ad) ad,\n"
			+ "               avg(l.vdaf) vdaf,\n"
			+ "               avg(l.mt) mt,\n"
			+ "               avg(l.stad) stad,\n"
			+ "               avg(l.aad) aad,\n"
			+ "               avg(l.mad) mad,\n"
			+ "               avg(l.qbad) qbad,\n"
			+ "               avg(l.had) had,\n"
			+ "               avg(l.vad) vad,\n"
			+ "               avg(l.fcad) fcad,\n"
			+ "               avg(l.std) std,\n"
			+ "               avg(l.qgrad) qgrad,\n"
			+ "               avg(l.qgrd) qgrd,\n"
			+ "               avg(l.hdaf) hdaf,\n"
			+ "               avg(l.qgrad_daf) qgrad_daf,\n"
			+ "               avg(l.sdaf) sdaf,\n"
			+ "               avg(l.t1) t1,\n"
			+ "               avg(l.t2) t2,\n"
			+ "               avg(l.t3) t3,\n"
			+ "               avg(l.t4) t4\n"
			+ "          from zhillsb l\n"
			+ "         where id in ("
			+ Bianh
			+ ")\n"
			+ "         group by l.zhilb_id, l.huaysj) z,\n"
			+ "       caiyb c,\n"
			+ "       (select sum(laimsl) as jingz,sum(ches) as ches, meikxxb_id, zhilb_id, pinzb_id,faz_id\n"
/**
* huochaoyuan 2009-02-17
* 修改上边fahb中取得的数量信息，以前是sum(jingz-koud),修改为根据新的统计量取值(laimsl)
*/					
			+ "          from fahb\n"
			+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
			+ "       meikxxb m,\n" ;
			if(IShuaybh){
				sql+=" zhuanmb zm,\n ";
			}
			sql+="       pinzb p, chezxxb cz\n"
			+ " where f.zhilb_id = c.zhilb_id\n"
			+ "   and f.zhilb_id = z.id\n" 
			+ "   and c.zhilb_id = z.id\n";
			if(IShuaybh){
				sql+="  and zm.zhillsb_id in ("+Bianh+")   and zm.zhuanmlb_id=(select id from zhuanmlb where jib=3)\n ";
			}
			sql+= "   and f.meikxxb_id = m.id\n" 
			+ "   and f.pinzb_id = p.id   and f.faz_id=cz.id";
		
		return sql;
	}
	
	/**
	 * 用于在二级审核中，通过输入的百分比计算平均值。
	 * @author yinjm 2009-11-23
	 * @param con
	 * @param bianh
	 */
	/*
	 * 作者：玉沙沙
	 * 时间：2012-3-31
	 * 描述：当一批发货多个质量时，入厂化验二级审核插入zhilb的huaybh是zhuanmb中的化验编号时，取第一条质量的化验编号写入质量表 
	 */
	public static String getErjshpj(JDBCcon con, String bianh, String total) {
		String[] aa=bianh.split(",");
		String sb = "select zhi from xitxxb where mingc = '最终化验编号设置' and leib = '化验' and zhuangt = 1";
		String zhi = "自动";
		boolean IShuaybh = false;
		ResultSetList rsl = con.getResultSetList(sb);
		if(rsl.next()){
			zhi = rsl.getString("zhi");
		}
		
		sb = "select * from xitxxb x where x.mingc = '入厂化验二级审核插入zhilb的huaybh是否是zhuanmb中的化验编号'and leib = '化验' and zhi = '是' and zhuangt = 1";
		rsl=con.getResultSetList(sb);
		if(rsl.next()){
			IShuaybh = true;
		}
		String sql = 
			"select zlls.id,\n" +
			"       m.mingc as meikdw,\n" + 
			"       cz.mingc as faz,\n" + 
			"       c.id as caiyb_id,\n" + 
			"       p.mingc as pinz,\n" + 
			"       f.ches as ches,\n" + 
			"       f.jingz as shul,";
	
			if(zhi.equals("进厂批号")){
				sql+="c.bianm as huaybh,\n";
			}else if(IShuaybh){
				//sql+="zm.bianm as huaybh,\n";
				sql+="(select bianm from zhuanmb where zhillsb_id="+aa[0]+" and zhuanmlb_id=(select id from zhuanmlb where jib=3)) as huaybh,\n";
			}else{
				sql+= "       (select max(zl.bianh) as bianh\n"
					+ "          from (select substr(to_char(Sysdate, 'yyyy') ||\n"
					+ "                              decode(to_char(Sysdate, 'yy'),\n"
					+ "                                     substr(max(huaybh), 0, 2),\n"
					+ "                                     substr(max(huaybh), 3),\n"
					+ "                                     '00000') + 1,\n"
					+ "                              3) as bianh\n"
					+ "                  from zhilb) zl) as huaybh,\n";
			}
			sql += 
				"		zlls.huaysj,\n" +
				"       round_new(zlls.qnet_ar/4.1816*1000,0)rezk,\n"+
				"       zlls.qnet_ar,\n" + 
				"       zlls.aar,\n" + 
				"       zlls.ad,\n" + 
				"       zlls.vdaf,\n" + 
				"       zlls.mt,\n" + 
				"       zlls.stad,\n" + 
				"       zlls.aad,\n" + 
				"       zlls.mad,\n" + 
				"       zlls.qbad,\n" + 
				"       zlls.had,\n" + 
				"       zlls.vad,\n" + 
				"       zlls.fcad,\n" + 
				"       zlls.std,\n" + 
				"       zlls.qgrad,\n" + 
				"       zlls.qgrd,\n"+
				"       zlls.hdaf,\n" + 
				"       zlls.qgrad_daf,\n" + 
				"       zlls.sdaf,\n" + 
				"       zlls.t1,\n" + 
				"       zlls.t2,\n" + 
				"       zlls.t3,\n" + 
				"       zlls.t4,\n" + 
				"       gethuayy(zlls.id) as huayy,\n" + 
				"       getlury(zlls.id) as lury,\n" + 
				"       getbeiz(zlls.id) as beiz\n" +
				"  from (select max(zlls.zhilb_id) id,\n" + 
				"               max(zlls.huaysj) huaysj,\n" + 
				"               round_new(sum(zlls.qnet_ar * zlls.bil / "+ total +"), 2) qnet_ar,\n" + 
				"               round_new(sum(zlls.aar * zlls.bil / "+ total +"), 2) aar,\n" + 
				"               round_new(sum(zlls.ad * zlls.bil / "+ total +"), 2) ad,\n" + 
				"               round_new(sum(zlls.vdaf * zlls.bil / "+ total +"), 2) vdaf,\n" + 
				"               round_new(sum(zlls.mt * zlls.bil / "+ total +"), 1) mt,\n" + 
				"               round_new(sum(zlls.stad * zlls.bil / "+ total +"), 2) stad,\n" + 
				"               round_new(sum(zlls.aad * zlls.bil / "+ total +"), 2) aad,\n" + 
				"               round_new(sum(zlls.mad * zlls.bil / "+ total +"), 2) mad,\n" + 
				"               round_new(sum(zlls.qbad * zlls.bil / "+ total +"), 2) qbad,\n" + 
				"               round_new(sum(zlls.had * zlls.bil / "+ total +"), 2) had,\n" + 
				"               round_new(sum(zlls.vad * zlls.bil / "+ total +"), 2) vad,\n" + 
				"               round_new(sum(zlls.fcad * zlls.bil / "+ total +"), 2) fcad,\n" + 
				"               round_new(sum(zlls.std * zlls.bil / "+ total +"), 2) std,\n" + 
				"               round_new(sum(zlls.qgrad * zlls.bil / "+ total +"), 2) qgrad,\n" + 
				"               round_new(sum(zlls.qgrd * zlls.bil / "+ total +"), 2) qgrd,\n"+
				"               round_new(sum(zlls.hdaf * zlls.bil / "+ total +"), 2) hdaf,\n" + 
				"               round_new(sum(zlls.qgrad_daf * zlls.bil / "+ total +"), 2) qgrad_daf,\n" + 
				"               round_new(sum(zlls.sdaf * zlls.bil / "+ total +"), 2) sdaf,\n" + 
				"               round_new(sum(zlls.t1 * zlls.bil / "+ total +"), 2) t1,\n" + 
				"               round_new(sum(zlls.t2 * zlls.bil / "+ total +"), 2) t2,\n" + 
				"               round_new(sum(zlls.t3 * zlls.bil / "+ total +"), 2) t3,\n" + 
				"               round_new(sum(zlls.t4 * zlls.bil / "+ total +"), 2) t4\n" + 
				"          from zhillsb zlls\n" + 
				"         where zlls.id in ("+ bianh +")) zlls,\n" + 
				"       (select sum(laimsl) as jingz,\n" + 
				"               sum(ches) as ches,\n" + 
				"               meikxxb_id,\n" + 
				"               zhilb_id,\n" + 
				"               pinzb_id,\n" + 
				"               faz_id\n" + 
				"          from fahb\n" + 
				"         group by pinzb_id, zhilb_id, meikxxb_id, faz_id) f,\n" + 
				"       meikxxb m,\n";
				/*if (IShuaybh){
					sql +=" zhuanmb zm,\n ";
				} */
				sql +=
				"       chezxxb cz,\n" + 
				"       pinzb p,\n" + 
				"       caiyb c\n" + 
				" where f.zhilb_id = c.zhilb_id\n" + 
				"   and f.zhilb_id = zlls.id\n" + 
				"   and c.zhilb_id = zlls.id\n";
				/*if (IShuaybh){
					sql +=" and zm.zhillsb_id in ("+ bianh +") and zm.zhuanmlb_id=(select id from zhuanmlb where jib=3)\n ";
				}*/
				sql +=
				"   and f.meikxxb_id = m.id\n" + 
				"   and f.pinzb_id = p.id\n" + 
				"   and f.faz_id = cz.id";
		return sql;
	}
}
