package com.zhiren.dc.diaoygl;

import java.util.Date;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
/*
 * 作者：王磊
 * 时间：2009-11-24
 * 描述：添加分厂别时按总厂生成的方法参数
 */
/*
 * 作者：王磊
 * 时间：2009-10-27
 * 描述：计算更新库存时，改动计算方法不正确的BUG由DIV改为SUB
 */
/*
 * 2009-05-14
 * 王磊
 * 增加Create con,dcid,riq,autoCommit方法 判断是否需要自动计算收耗存
 */
/*
 * 作者：王磊
 * 时间：2009-05-23
 * 描述：增加收耗存日报是否休约为零的判断
 */
/*
 * 作者：王磊
 * 时间：2009-06-02
 * 描述：修改create方法中的自动上传判定判断错误的BUG
 */
public class AutoCreateShouhcrb {
	public static void Create(JDBCcon con, long dcid, Date riq) {
		Create(con,dcid,riq,false,false);
	}
	public static void Create(JDBCcon con, long dcid, Date riq,boolean autoCommit) {
//		Create(con,dcid,riq,autoCommit,false);
	}
	public static void Create(JDBCcon con, long dcid, Date riq, boolean autoCommit, boolean Fenczc) {
/*		if(!autoCommit&&!con.getHasIt("select zhi,danw,beiz from xitxxb where mingc = '自动计算收耗存' and zhuangt = 1 and diancxxb_id=" + dcid)){
//			系统中没有设置自动计算收耗存日报
			return;
		}
		// 变量定义
		String CurOraDate = DateUtil.FormatOracleDate(riq); // 当前时间
		String LastOraDate = DateUtil.FormatOracleDate(DateUtil.AddDate(riq,
				-1, DateUtil.AddType_intDay)); // 当前时间的前一天
		// 收耗存日报中的字段变量
		double zuorkc = 0.0, fadl = 0.0, jingz = 0.0, biaoz = 0.0, yuns = 0.0, yingd = 0.0, kuid = 0.0, fady = 0.0, gongry = 0.0, qity = 0.0, cuns = 0.0, tiaozl = 0.0, shuifctz = 0.0, panyk = 0.0, kuc = 0.0, feiscy = 0.0;
		StringBuffer sb = new StringBuffer();
		// 计算昨日库存
		sb.append("select kuc from shouhcrbb where diancxxb_id=").append(dcid)
				.append(" and riq = ").append(LastOraDate);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			zuorkc = rsl.getDouble("kuc");
		}
		rsl.close();
		// 从日生产数据中计算发电量
		sb.delete(0, sb.length());
		sb.append("select sum(fadl) fadl from riscsjb where riq = ").append(
				CurOraDate).append(" and diancxxb_id = ").append(dcid);
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			fadl = rsl.getDouble("fadl");
		}
		rsl.close();
		// 从发货中计算来煤运损等来煤数量
		String sql = "select * from xitxxb where mingc='收耗存日报来煤量是否休约' " +
				"and zhuangt=1 and diancxxb_id =" + dcid +" and zhi ='是'";
		String roundFuncName = "";
		String roundSacle = "";
		if(con.getHasIt(sql)){
			roundFuncName = "round_new(" ;
			roundSacle = ",0)";
		}
		sb.delete(0, sb.length());
		if(Fenczc){
			sb
			.append(
					"select nvl(sum("+roundFuncName+"laimsl"+roundSacle+"),0) jingz, nvl(sum("+roundFuncName+"biaoz"+roundSacle+"),0) biaoz, nvl(sum("+roundFuncName+"yuns"+roundSacle+"),0) yuns,\n")
			.append(
					"nvl(sum("+roundFuncName+"yingd"+roundSacle+"),0) yingd, nvl(sum("+roundFuncName+"yingd-yingk"+roundSacle+"),0) kuid \n")
			.append("from fahb,diancxxb d where daohrq =").append(CurOraDate).append(
					" and diancxxb_id=d.id and d.fuid =").append(dcid);
		}else{
			sb
				.append(
						"select nvl(sum("+roundFuncName+"laimsl"+roundSacle+"),0) jingz, nvl(sum("+roundFuncName+"biaoz"+roundSacle+"),0) biaoz, nvl(sum("+roundFuncName+"yuns"+roundSacle+"),0) yuns,\n")
				.append(
						"nvl(sum("+roundFuncName+"yingd"+roundSacle+"),0) yingd, nvl(sum("+roundFuncName+"yingd-yingk"+roundSacle+"),0) kuid \n")
				.append("from fahb where daohrq =").append(CurOraDate).append(
						" and diancxxb_id=").append(dcid);
		}
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			jingz = rsl.getDouble("jingz");
			biaoz = rsl.getDouble("biaoz");
			yuns = rsl.getDouble("yuns");
			yingd = rsl.getDouble("yingd");
			kuid = rsl.getDouble("kuid");
		}
		rsl.close();
		// 从煤耗用里计算耗用量
		sb.delete(0, sb.length());
		sb
				.append(
						"select nvl(sum(fadhy),0) fady, nvl(sum(gongrhy),0) gongry, nvl(sum(qity),0) qity,nvl(sum(feiscy),0) feiscy from meihyb\n")
				.append("where rulrq =").append(CurOraDate).append(
						" and diancxxb_id =").append(dcid);
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			fady = rsl.getDouble("fady");
			gongry = rsl.getDouble("gongry");
			qity = rsl.getDouble("qity");
			feiscy = rsl.getDouble("feiscy");
		}
		rsl.close();
		// 计算库存
		kuc = zuorkc + jingz - fady - gongry - qity - cuns - feiscy + tiaozl
				+ shuifctz + panyk;
		// 查看当日是否已经生成来耗存日报
		sb.delete(0, sb.length());
		sb.append("select * from shouhcrbb where diancxxb_id=").append(dcid)
				.append(" and riq = ").append(CurOraDate);
		rsl = con.getResultSetList(sb.toString());
		sb.delete(0, sb.length());
		if (rsl.next()) {
			// 如果日收耗存中已有数据则进行对比 有差异则更新
			boolean changed = false;
//			if (zuorkc != rsl.getDouble("zuorkc")) {
//				changed = true;
//			}
			if (fadl != rsl.getDouble("fadl")) {
				changed = true;
			}
			if (jingz != rsl.getDouble("jingz")) {
				changed = true;
			}
			if (biaoz != rsl.getDouble("biaoz")) {
				changed = true;
			}
			if (yuns != rsl.getDouble("yuns")) {
				changed = true;
			}
			if (yingd != rsl.getDouble("yingd")) {
				changed = true;
			}
			if (kuid != rsl.getDouble("kuid")) {
				changed = true;
			}
			if (fady != rsl.getDouble("fady")) {
				changed = true;
			}
			if (gongry != rsl.getDouble("gongry")) {
				changed = true;
			}
			if (qity != rsl.getDouble("qity")) {
				changed = true;
			}
			if (cuns != rsl.getDouble("cuns")) {
				changed = true;
			}
			if (tiaozl != rsl.getDouble("tiaozl")) {
				changed = true;
			}
			if (shuifctz != rsl.getDouble("shuifctz")) {
				changed = true;
			}
			if (panyk != rsl.getDouble("panyk")) {
				changed = true;
			}
			if (feiscy != rsl.getDouble("feiscy")) {
				changed = true;
			}
//			如果有变化则更新收耗存日报表
			if (changed) {
				sb.append("update shouhcrbb set ")
				.append("fadl=").append(fadl)
				.append(",jingz=").append(jingz)
				.append(",biaoz=").append(biaoz)
				.append(",yuns=").append(yuns)
				.append(",yingd=").append(yingd)
				.append(",kuid=").append(kuid)
				.append(",fady=").append(fady)
				.append(",gongry=").append(gongry)
				.append(",qity=").append(qity)
				.append(",cuns=").append(cuns)
				.append(",tiaozl=").append(tiaozl)
				.append(",shuifctz=").append(shuifctz)
				.append(",panyk=").append(panyk)
				.append(",dangrgm=").append(jingz)
				.append(",haoyqkdr=").append(fady + gongry + qity + cuns)
				.append(",feiscy=").append(feiscy)
				.append(" where id =").append(rsl.getString("id"));
				con.getUpdate(sb.toString());
				double kuccha = CustomMaths.sub(kuc, rsl.getDouble("kuc"));
				
//				判断是否同步更新库存 默认同步更新设置为否时不同步更新
				sb.delete(0, sb.length());
				boolean synchronicUpdate = true;
				sb.append("select * from xitxxb where mingc='收耗存日报实时更新库存' and zhi ='否' and zhuangt=1 and diancxxb_id =" +dcid);
				synchronicUpdate = !con.getHasIt(sb.toString());
				if(synchronicUpdate){
	//				更新当前日期以后的所有库存
					sb.delete(0, sb.length());
					sb.append("update shouhcrbb set ")
					.append("kuc = kuc + ").append(kuccha)
					.append(" where riq >= ").append(CurOraDate)
					.append(" and diancxxb_id = ").append(dcid);
					con.getUpdate(sb.toString());
				}
			}
		} else {
			// 如果日收耗存中没有数据则插入
			sb
					.append(
							"insert into shouhcrbb(id, diancxxb_id, riq, fadl, jingz, biaoz, yuns,\n")
					.append(
							"yingd, kuid, fady, gongry, qity, cuns, tiaozl, shuifctz, panyk, kuc, dangrgm, haoyqkdr,feiscy) values(")
					.append("getnewid(").append(dcid).append("),").append(dcid)
					.append(",").append(CurOraDate).append(",").append(fadl)
					.append(",").append(jingz).append(",").append(biaoz)
					.append(",").append(yuns).append(",").append(yingd).append(
							",").append(kuid).append(",").append(fady).append(
							",").append(gongry).append(",").append(qity)
					.append(",").append(cuns).append(",").append(tiaozl)
					.append(",").append(shuifctz).append(",").append(panyk)
					.append(",").append(kuc).append(",").append(jingz).append(
							",").append(fady + gongry + qity + cuns)
					.append(",").append(feiscy).append(")");
			con.getInsert(sb.toString());
		}
		rsl.close();
	*/
	}

}
