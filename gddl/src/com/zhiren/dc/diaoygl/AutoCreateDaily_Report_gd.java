package com.zhiren.dc.diaoygl;

import java.util.Date;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

public class AutoCreateDaily_Report_gd {

/*
 *		日收耗存录入界面中库存的计算方法为：
 *			期初库存：从当月月报中取得本月的期初库存，如果月报中无数据则取日报上月月底库存
 *			库存：期初库存+总收入（截止至所选日期的进煤总量）-总耗用（截止至所选日期的耗用总量）
*/
/*
 * 作者：夏峥
 * 时间：2011-11-25
 * 使用范围：国电电力及其下属单位
 * 描述：修正库存的取得方法（从收耗存合计中取得上月库存总数）
 * 		 增加系统锁‘日报库存是否从月报中取得’，默认值为否。	
 */
/*
 * 作者：夏峥
 * 时间：2011-11-28
 * 使用范围：国电电力及其下属单位
 * 描述：报表数据检查中增加对当日空数据的判断，并且额外增加当日没有来煤时不提示日估价无对应数据的错误。
 */	
/*
 * 作者：夏峥
 * 时间：2011-11-29
 * 使用范围：国电电力及其下属单位
 * 描述：新增收耗存日报实时更新库存功能（即更改某一日库存后滚动计算之后的库存信息）
 * 		 在新增时更新当日的来煤量-耗用信息滚动新增至日期后的库存中
 */		
/*
 * 作者：夏峥
 * 时间：2011-11-30
 * 使用范围：国电电力及其下属单位
 * 描述：修正在收耗存日报录入界面中生成方法中对耗用信息是否更新的判断逻辑异常
 */	
/*
 * 作者：夏峥
 * 时间：2011-12-13
 * 使用范围：国电电力及其下属单位
 * 描述：界面提示内容每行显示3条错误信息
 */
/*
 * 作者：夏峥
 * 时间：2011-12-20
 * 适用范围：国电电力及其下属单位
 * 描述：使用参数对生成日报时数量信息进行判断（是否只取得发货中有皮重信息的数据作为当日来煤信息）	
 * 		修正日估价的生成统计方式
 * 		使用收耗存分矿生成方法中‘收耗存日报来煤自动覆盖’参数对收耗存日报中来煤数量，票重，运损，亏吨等信息进行配置
 */
/*
 * 作者：夏峥
 * 时间：2012-03-13
 * 适用范围：国电电力及其下属单位
 * 描述：修改分矿数据中煤价和运价的取值方式，只有当热值取得信息时才写入煤价和运价信息。
 */	
/*
 * 作者：赵胜男
 * 时间：2012-04-18
 * 适用范围：国电电力及其下属单位
 * 描述：修改2012-03-13日BUG。
 */
/*
 * 作者：夏峥
 * 时间：2012-06-04
 * 适用范围：国电电力及其下属单位
 * 描述：修正由于调整量和水分差调整录入信息为负数导致的生成错误
 */
/*
 * 作者：夏峥
 * 时间：2012-06-27
 * 适用范围：邯郸电厂
 * 描述：为邯郸电厂增加相应的保存方法和获取方法
 */
/*
 * 作者：夏峥
 * 时间：2012-07-02
 * 适用范围：国电电力
 * 描述：新增"收耗存日报耗用是否单独判断","来煤是否区分厂别"的判断条件	
 */
/*
 * 作者：夏峥
 * 时间：2012-07-11
 * 适用范围：国电电力
 * 描述：处理由于三位小数导致的误差错误
 */
/*
 * 作者：夏峥
 * 时间：2012-08-10
 * 适用范围：国电电力及其下属电厂（不包括庄河和邯郸）
 * 描述：增加厂外煤场进煤量列，厂外煤场进煤量将影响库存。
 */	
/*
 * 作者：夏峥
 * 时间：2012-09-26
 * 适用范围：国电电力及其下属电厂（不包括庄河和邯郸）
 * 描述：调整RPChk检查方法中煤价和运价的检查方式
 * 		调整ChkFKB检查方法中煤价和运价的检查方式
 */	
/*
 * 作者：夏峥
 * 时间：2012-10-29
 * 适用范围：国电电力及其下属电厂（不包括庄河）
 * 描述：修正可调库存计算未计算的BUG
 */	
/*
 * 作者:夏峥
 * 日期:2013-03-25
 * 修改内容:将发电量的单位变更为万千瓦时，并调整生成方法。
 */
/*
 * 作者:夏峥
 * 日期:2013-07-18
 * 修改内容:从系统中自动获得供热量信息
 */		
/*
 * 作者:夏峥
 * 日期:2013-09-26
 * 修改内容:煤价和运价将不从估收取，默认为0。
 * 			生成时增加煤价税，运价税，火车运价，火车运价税，汽车运价，汽车运价税
 */
/*
 * 作者:夏峥
 * 日期:2013-10-16
 * 修改内容:生成分矿数据时保存运输方式
 */		
	public String CreateRBB(JDBCcon con, long dcid, Date riq) {
//		变量定义
		String CurOraDate = DateUtil.FormatOracleDate(riq); // 当前时间
		int flag=0;//更新成功标识
//		收耗存日报中的字段变量
		double zuorkc = 0.0, fadl = 0.0,GONGRL=0.0, jingz = 0.0, biaoz = 0.0, yuns = 0.0, yingd = 0.0;
		double kuid = 0.0, fady = 0.0, gongry = 0.0, qity = 0.0, cuns = 0.0, tiaozl = 0.0;
		double shuifctz = 0.0, panyk = 0.0, kuc = 0.0, feiscy = 0.0,changwml=0.0,bukdml=0.0,kedkc=0.0;
		StringBuffer sb = new StringBuffer();

		sb.append("select sum(fadl)/10000 fadl,sum(GONGRL) GONGRL from riscsjb where riq = ").append(
				CurOraDate).append(" and diancxxb_id = ").append(dcid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			fadl = rsl.getDouble("fadl");
			GONGRL=rsl.getDouble("GONGRL");
		}
		rsl.close();

		String roundFuncName = "sum(round(";
		String roundSacle = ",2))";

		sb.delete(0, sb.length());
		String whereCondition="";
		if(MainGlobal.getXitxx_item("收耗存日报", "是否判断发货皮重信息", "0", "否").equals("是")){
			whereCondition=" and piz>0 \n";
		}
		sb.append("select nvl("+roundFuncName+"jingz-koud"+roundSacle+",0) jingz, nvl("+roundFuncName+"biaoz"+roundSacle+",0) biaoz, nvl("+roundFuncName+"yuns"+roundSacle+",0) yuns,\n")
			.append("nvl("+roundFuncName+"yingd"+roundSacle+",0) yingd, nvl("+roundFuncName+"yingd-yingk"+roundSacle+",0) kuid \n")
			.append("from fahb where daohrq =")
			.append(CurOraDate)
			.append(" "+whereCondition);
		if(MainGlobal.getXitxx_item("收耗存日报", "来煤是否区分厂别", "0", "是").equals("是")){
			sb.append(" and diancxxb_id=").append(dcid);
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
		
// 		计算昨日库存
//		修正库存计算方法：期初库存+总收入-总耗用
//		取上月月末日报库存
		sb.delete(0, sb.length());
		sb.append("SELECT KUC FROM SHOUHCRBB\n" +
				"WHERE DIANCXXB_ID = "+dcid+"\n" + 
				"AND RIQ = LAST_DAY(ADD_MONTHS("+CurOraDate+", -1))");
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			zuorkc = rsl.getDouble("kuc");
		}
		rsl.close();

		if(MainGlobal.getXitxx_item("收耗存日报", "日报库存是否从月报中取得", "0", "否").equals("是")){
			sb.delete(0, sb.length());
//			尝试从耗存合计中取得上月月报库存
			sb.append("SELECT KUC\n" +
					"  FROM YUESHCHJB TJ\n" + 
					" WHERE TJ.RIQ =FIRST_DAY(ADD_MONTHS("+CurOraDate+", -1))\n" + 
					"   AND TJ.DIANCXXB_ID = "+dcid+"\n" + 
					"   AND FENX = '本月'");
			rsl = con.getResultSetList(sb.toString());
			if (rsl.next()) {
				zuorkc = rsl.getDouble("kuc");
			}
			rsl.close();
		}

//		取得昨日库存+上日库存差
		sb.delete(0, sb.length());
		sb.append("SELECT SUM(DANGRGM)+SUM(changwml) - SUM(FADY) - SUM(GONGRY) - SUM(QITY) - SUM(FEISCY) -\n" +
		"       SUM(CUNS) + SUM(TIAOZL) + SUM(SHUIFCTZ) + SUM(PANYK) KCC\n" + 
		"  FROM SHOUHCRBB WHERE DIANCXXB_ID = "+dcid+"\n" + 
		"   AND RIQ BETWEEN FIRST_DAY("+CurOraDate+") AND "+CurOraDate+" - 1");
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			zuorkc = zuorkc+rsl.getDouble("KCC");
		}
		rsl.close();
//		取得昨日不可调煤量
		sb.delete(0, sb.length());
		sb.append("SELECT BUKDML FROM SHOUHCRBB WHERE DIANCXXB_ID = "+dcid+" AND RIQ = "+CurOraDate+" - 1");
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			bukdml = rsl.getDouble("BUKDML");
		}
		rsl.close();

		// 计算库存
		kuc = zuorkc + jingz - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk;
		// 计算可调库存
		kedkc=kuc-bukdml;
		// 查看当日是否已经生成来耗存日报
		sb.delete(0, sb.length());
		sb.append("select * from shouhcrbb where diancxxb_id="+dcid+" and riq = "+CurOraDate);
		rsl = con.getResultSetList(sb.toString());
		
//		日报信息只更新差异数据，即耗用信息是否大于0如果大于0则不进行更新。
		sb.delete(0, sb.length());
		if (rsl.next()) {
			// 如果日收耗存中已有数据则进行对比 有差异则更新
			boolean changed = false;
//			如果发电量有数据则不更新发电量信息
			if(fadl!=rsl.getDouble("fadl")){
				changed = true;
				if (rsl.getDouble("fadl")>0) {
					fadl=rsl.getDouble("fadl");
				}
			}
			
			if(GONGRL!=rsl.getDouble("GONGRL")){
				changed = true;
				if (rsl.getDouble("GONGRL")>0) {
					GONGRL=rsl.getDouble("GONGRL");
				}
			}


//			新增日估价来煤自动覆盖数量信息判断，默认系统自动覆盖数量信息。
			boolean Laimqz=MainGlobal.getXitxx_item("收耗存日报", "收耗存日报来煤自动覆盖", "0", "是").equals("是");
			
			if (jingz != rsl.getDouble("jingz")) {
				changed = true;
				if(!Laimqz){
					jingz=rsl.getDouble("jingz");
				}
			}
			if (biaoz != rsl.getDouble("biaoz")) {
				changed = true;
				if(!Laimqz){
					biaoz=rsl.getDouble("biaoz");
				}
			}
			if (yuns != rsl.getDouble("yuns")) {
				changed = true;
				if(!Laimqz){
					yuns=rsl.getDouble("yuns");
				}
			}
			if (yingd != rsl.getDouble("yingd")) {
				changed = true;
				if(!Laimqz){
					yingd=rsl.getDouble("yingd");
				}
			}
			if (kuid != rsl.getDouble("kuid")) {
				changed = true;
				if(!Laimqz){
					kuid=rsl.getDouble("kuid");
				}
			}
//			如果发电耗用有相关信息，则不更新
			if(fady!=rsl.getDouble("fady")){
				changed = true;
				if (rsl.getDouble("fady")>0) {
					fady=rsl.getDouble("fady");
				}
			}

//			如果供热耗用有信息，则不更新
			if(gongry!=rsl.getDouble("gongry")){
				changed = true;
				if (rsl.getDouble("gongry")>0) {
					gongry=rsl.getDouble("gongry");
				}
			}

//			如果其他耗用有信息，则不更新
			if(qity!=rsl.getDouble("qity")){
				changed = true;	
				if (rsl.getDouble("qity")>0) {
					qity = rsl.getDouble("qity");
				}
			}
			
//			如果非生产用有信息，则不更新
			if(feiscy!=rsl.getDouble("feiscy")){
				changed = true;
				if (rsl.getDouble("feiscy")>0) {
					feiscy = rsl.getDouble("feiscy");
				}
			}
			
//			如果发电，供热，其他，非生产中任意一个有数据，那么将不覆盖更新内容
			if(MainGlobal.getXitxx_item("收耗存日报", "收耗存日报耗用是否单独判断", "0", "否").equals("否")){
				if(rsl.getDouble("fady")+rsl.getDouble("gongry")+rsl.getDouble("qity")+rsl.getDouble("feiscy")>0){
					fady=rsl.getDouble("fady");
					gongry=rsl.getDouble("gongry");
					qity = rsl.getDouble("qity");
					feiscy = rsl.getDouble("feiscy");
				}
			}

//			如果存损有信息，则不更新
			if(cuns!=rsl.getDouble("cuns")){
				changed = true;
				if (rsl.getDouble("cuns")>0) {
					cuns = rsl.getDouble("cuns");
				}
			}
			
//			如果调整量有信息，则不更新
			if(tiaozl!=rsl.getDouble("tiaozl")){
				changed = true;
				tiaozl = rsl.getDouble("tiaozl");
			}

//			如果水分差调整有信息，则不更新
			if(shuifctz!=rsl.getDouble("shuifctz")){
				changed = true;
				shuifctz = rsl.getDouble("shuifctz");
			}

//			如果盘盈亏有信息，则不更新
			if(panyk!=rsl.getDouble("panyk")){
				changed = true;
				panyk = rsl.getDouble("panyk");
			}
			
//			如果不可调煤量有信息，则不更新
			if(bukdml!=rsl.getDouble("bukdml")){
				changed = true;
				bukdml = rsl.getDouble("bukdml");
			}
//			如果厂外煤量有信息，则不更新
			if(changwml!=rsl.getDouble("changwml")){
				changed = true;
				changwml = rsl.getDouble("changwml");
			}

			if (rsl.getDouble("kedml")>0){
				changed = true;
				kedkc=rsl.getDouble("kedml");
			}
//			重新计算库存
			kuc = zuorkc + jingz + changwml - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk;
//			重新计算可调库存
			kedkc=kuc-bukdml;
//			如果有变化则更新收耗存日报表
			if (changed) {
				sb.append("update shouhcrbb set ")
				.append("fadl=").append(fadl)
				.append(",GONGRL=").append(GONGRL)
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
				.append(",bukdml=").append(bukdml)
				.append(",changwml=").append(changwml)
				.append(",kedkc=").append(kedkc)
				.append(",kuc=").append(kuc)
				.append(" where id =").append(rsl.getString("id"));
				flag=con.getUpdate(sb.toString());
				if(flag==-1){
					return "收耗存信息更新失败";
				}
				double kuccha = CustomMaths.sub(kuc, rsl.getDouble("kuc"));
//				判断是否同步更新库存 默认同步更新设置为否时不同步更新
				if(MainGlobal.getXitxx_item("收耗存日报", "收耗存日报实时更新库存", "0", "否").equals("是")){
//				更新当前日期以后的所有库存和可调库存
					sb.delete(0, sb.length());
					sb.append("begin \n");
					sb.append("update shouhcrbb set ");
					sb.append("kuc = kuc + ").append(kuccha+"\n");
					sb.append(" where riq > ").append(CurOraDate);
					sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
					sb.append("update shouhcrbb set ");
					sb.append("kedkc = kuc - bukdml").append("\n");
					sb.append(" where riq > ").append(CurOraDate);
					sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
					sb.append("end;");
					flag=con.getUpdate(sb.toString());
					if(flag==-1){
						return "收耗存库存信息更新失败";
					}
				}
			}
		} else {
			// 如果日收耗存中没有数据则插入
			sb.append("insert into shouhcrbb(id, diancxxb_id, riq, fadl, jingz, biaoz, yuns,\n")
			.append("yingd, kuid, fady, gongry, qity, cuns, tiaozl, shuifctz, panyk, kuc, dangrgm," +
					" haoyqkdr,feiscy,bukdml,changwml,kedkc,GONGRL) values(")
			.append("getnewid(").append(dcid).append("),").append(dcid)
			.append(",").append(CurOraDate).append(",").append(fadl)
			.append(",").append(jingz).append(",").append(biaoz)
			.append(",").append(yuns).append(",").append(yingd).append(",")
			.append(kuid).append(",").append(fady).append(",")
			.append(gongry).append(",").append(qity)
			.append(",").append(cuns).append(",").append(tiaozl)
			.append(",").append(shuifctz).append(",").append(panyk)
			.append(",").append(kuc).append(",").append(jingz).append(",")
			.append(fady + gongry + qity + cuns)
			.append(",").append(feiscy)
			.append(",").append(bukdml)
			.append(",").append(changwml)
			.append(",").append(kedkc)
			.append(",").append(GONGRL).append(")");
			flag=con.getInsert(sb.toString());
			if(flag==-1){
				return "收耗存信息插入失败";
			}
//			判断是否同步更新库存 默认同步更新设置为否时不同步更新
			if(MainGlobal.getXitxx_item("收耗存日报", "收耗存日报实时更新库存", "0", "否").equals("是")){
//			更新当前日期以后的所有库存和可调库存
				sb.delete(0, sb.length());
				sb.append("begin \n");
				sb.append("update shouhcrbb set ");
				sb.append("kuc = kuc + ").append(jingz - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk);
				sb.append(" where riq > ").append(CurOraDate);
				sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
				sb.append("update shouhcrbb set ");
				sb.append("kedkc = kuc - bukdml");
				sb.append(" where riq > ").append(CurOraDate);
				sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
				sb.append("end;");
				
				flag=con.getUpdate(sb.toString());
				if(flag==-1){
					return "收耗存库存信息更新失败";
				}
			}
		}
		rsl.close();
		return "";
	}
//	生成分矿信息
	public String CreateFCB(JDBCcon con, long dcid, Date riq)  {
		String CurOraDate = DateUtil.FormatOracleDate(riq); // 当前时间
		ResultSetList list=getShouhcfk(con, dcid,riq);
		ResultSetList OLDlist=getOLDShouhcfk(con, dcid,riq);
		StringBuffer insertSql=new StringBuffer();
		boolean isTrue=false;
//		新增日估价来煤自动覆盖数量信息判断，默认系统自动覆盖数量信息。
		boolean Laimqz=MainGlobal.getXitxx_item("收耗存日报", "收耗存日报来煤自动覆盖", "0", "是").equals("是");
		boolean Rezqz=MainGlobal.getXitxx_item("收耗存日报", "收耗存日报热值自动覆盖", "0", "是").equals("是");
		
		insertSql.append("begin \n");
//		判断是否存在原始记录
		if(OLDlist.getRows()>0){
			while (list.next()) {
				while(OLDlist.next()){
					double laimsl=list.getDouble("laimsl");
					double rez=list.getDouble("REZ");
//					判断是否对来煤信息进行覆盖
					if(!Laimqz){
						laimsl=OLDlist.getDouble("laimsl");
					}
//					判断是否对热值信息进行覆盖
					if(!Rezqz){
						rez=OLDlist.getDouble("rez");
					}
//					如果煤价为0，则使用系统取值进行覆盖操作，否则数据将保持不变
					double meij=OLDlist.getDouble("meij");
//					if(rez>0 && meij==0){
//						meij=list.getDouble("meij");
//					}
//					如果运价为0，则使用系统取值进行覆盖操作，否则数据将保持不变
					double yunj=OLDlist.getDouble("yunj");
//					if(rez>0 && yunj==0){
//						yunj=list.getDouble("yunj");
//					}
					
					double meijs=OLDlist.getDouble("meijs");
					double yunjs=OLDlist.getDouble("yunjs");
					double huocyj=OLDlist.getDouble("huocyj");
					double huocyjs=OLDlist.getDouble("huocyjs");
					double qicyj=OLDlist.getDouble("qicyj");
					double qicyjs=OLDlist.getDouble("qicyjs");
					
//					如果热值为0那么煤价和运价及其对应的税金均为0
					if(rez==0){
						yunj=0;
						meij=0;
						meijs=0;
						yunjs=0;
						huocyj=0;
						huocyjs=0;
						qicyj=0;
						qicyjs=0;
					}
					if(list.getString("meikxxb_id").equals(OLDlist.getString("meikxxb_id")) 
						&& list.getString("pinzb_id").equals(OLDlist.getString("pinzb_id")) 
						&& list.getString("gongysb_id").equals(OLDlist.getString("gongysb_id")) 
						&& list.getString("JIHKJB_ID").equals(OLDlist.getString("JIHKJB_ID"))
						&& list.getString("YUNSFSB_ID").equals(OLDlist.getString("YUNSFSB_ID"))){
						insertSql.append(
								"insert into shouhcfkb\n" +
								"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meijs,yunjs,huocyj,huocyjs,qicyj,qicyjs,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
								"values\n" + 
								"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
								laimsl+","+rez+","+meij+","+yunj+","+meijs+","+yunjs+","+huocyj+","+huocyjs+","
								+qicyj+","+qicyjs+","+list.getString("meikxxb_id")+","+
								list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
						isTrue=true;
						break;
					}
				}
				if(!isTrue){
					insertSql.append(
							"insert into shouhcfkb\n" +
							"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
							"values\n" + 
							"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
							list.getDouble("laimsl")+","+list.getDouble("rez")+","+
							list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
							list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
				}
				isTrue=false;
				OLDlist.beforefirst();
			}
			insertSql.append("end;");
		}else{
			while (list.next()) {
				insertSql.append(
						"insert into shouhcfkb\n" +
						"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
						"values\n" + 
						"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
						list.getDouble("laimsl")+","+list.getDouble("rez")+","+
						list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
						list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
			}
			insertSql.append("end;");
		}

		int re=0;

//		删除数据
		String delData="delete from shouhcfkb where diancxxb_id="+dcid+" and riq = "+CurOraDate;
		re=con.getDelete(delData);
		if(re==-1){
			OLDlist.close();
			list.close();
			return "分矿数据删除失败！";
		}
//		更新数据
		if(insertSql.length()>20){
			re=con.getInsert(insertSql.toString());
			if(re==-1){
				OLDlist.close();
				list.close();
				return "分矿数据生成失败！";
			}
		}
		OLDlist.close();
		list.close();
		return "";
	}
	
	
	//得到原始信息
	private ResultSetList getOLDShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		StringBuffer sql=new StringBuffer();
		sql.append(
				"SELECT SHC.DIANCXXB_ID,\n" +
				"       SHC.REZ,\n" + 
				"       SHC.MEIJ,\n" + 
				"       SHC.YUNJ,\n" + 
				"       SHC.MEIJS,\n" + 
				"       SHC.YUNJS,\n" + 
				"       SHC.HUOCYJ,\n" + 
				"       SHC.HUOCYJS,\n" + 
				"       SHC.QICYJ,\n" + 
				"       SHC.QICYJS,\n" + 
				"       SHC.LAIMSL,\n" + 
				"       SHC.MEIKXXB_ID,\n" + 
				"       SHC.GONGYSB_ID,\n" + 
				"       SHC.JIHKJB_ID,\n" + 
				"       SHC.YUNSFSB_ID,\n" + 
				"       SHC.PINZB_ID\n" + 
				"  FROM SHOUHCFKB SHC, DIANCXXB DC\n" + 
				" WHERE SHC.RIQ ="+DateUtil.FormatOracleDate(riq)+"\n" + 
				"   AND SHC.DIANCXXB_ID = DC.ID"+
				"   and dc.id = "+diancxxb_id+" \n" );
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
	private ResultSetList getShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		String roundFuncName = "sum(round(";
		String roundSacle = ",2))";
		String whereCondition="";
		if(MainGlobal.getXitxx_item("收耗存日报", "是否判断发货皮重信息", "0", "否").equals("是")){
			whereCondition+=" and f.piz>0 \n";
		}
		if(MainGlobal.getXitxx_item("收耗存日报", "来煤是否区分厂别", "0", "是").equals("是")){
			whereCondition+=" and diancxxb_id="+diancxxb_id;
		}

//		暂时不取煤价和运价的值
		StringBuffer sql=new StringBuffer();
		sql.append(
				"select meikxxb_id,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,diancxxb_id,rez,\n" +
//				"		decode(rez,0,0,meij) meij,\n" +
//				"		decode(rez,0,0,yunj) yunj,\n" +
				"		0 meij,\n" +
				"		0 yunj,\n" +
				"		laimsl from(\n" +
				"		select f.meikxxb_id,\n" +
				"       f.gongysb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id,\n" + 
				"		f.diancxxb_id,\n"+
				"       decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)),0,0,round_new(sum(z.qnet_ar * f.laimsl) / sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)), 2)) rez,\n" + 
				"       decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum((g.meij + g.meis) * f.laimsl) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)), 2)) meij,\n" + 
				//"       decode(sum(f.laimsl),0,0,round_new(sum((g.yunf + g.yunfs) * f.laimsl) / sum(f.laimsl),2)) yunj,\n" + 
				"		decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum(f.laimsl * decode(g.meij,0,0,(nvl(g.yunf,0) + nvl(g.yunfs,0)))) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)),2)) yunj,\n" + 
				"       nvl("+roundFuncName+"f.laimsl"+roundSacle+", 0) laimsl\n" + 
				"  from fahb f,\n" + 
				"       zhilb z,diancxxb d,\n" + 
				"       (select id, fahb_id, meij, meis, yunf, yunfs\n" + 
				"          from guslsb\n" + 
				"         where id in (select max(id) id\n" + 
				"                        from guslsb g,\n" + 
				"                             (select fahb_id, max(leix) leix\n" + 
				"                                from guslsb\n" + 
				"                               group by fahb_id) g2\n" + 
				"                       where g.fahb_id = g2.fahb_id\n" + 
				"                         and g.leix = g2.leix\n" + 
				"                       group by g.fahb_id)) g\n" + 
				" where f.zhilb_id = z.id(+)\n" + 
				"   and f.id = g.fahb_id(+)\n" + 
				"   and f.diancxxb_id = d.id \n"+
				whereCondition+
				"   and f.daohrq = "+DateUtil.FormatOracleDate(riq)+"\n" + 
				" group by f.diancxxb_id,f.gongysb_id, f.meikxxb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id)");
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
	
//	按照供应商，煤矿，品种，计划口径，运输方式取值方式（邯郸特殊）
//	生成分矿信息
	public String CreateFCB_HD(JDBCcon con, long dcid, Date riq)  {
		String CurOraDate = DateUtil.FormatOracleDate(riq); // 当前时间
		ResultSetList list=getHDShouhcfk(con, dcid,riq);
		ResultSetList OLDlist=getOLDHDShouhcfk(con, dcid,riq);
		StringBuffer insertSql=new StringBuffer();
		boolean isTrue=false;
//		新增日估价来煤自动覆盖数量信息判断，默认系统自动覆盖数量信息。
		boolean Laimqz=MainGlobal.getXitxx_item("收耗存日报", "收耗存日报来煤自动覆盖", "0", "是").equals("是");
		boolean Rezqz=MainGlobal.getXitxx_item("收耗存日报", "收耗存日报热值自动覆盖", "0", "是").equals("是");
		
		insertSql.append("begin \n");
//		判断是否存在原始记录
		if(OLDlist.getRows()>0){
			while (list.next()) {
				while(OLDlist.next()){
					double laimsl=list.getDouble("laimsl");
					double rez=list.getDouble("REZ");
//					判断是否对来煤信息进行覆盖
					if(!Laimqz){
						laimsl=OLDlist.getDouble("laimsl");
					}
//					判断是否对热值信息进行覆盖
					if(!Rezqz){
						rez=OLDlist.getDouble("rez");
					}
//					如果煤价为0，则使用系统取值进行覆盖操作，否则数据将保持不变
					double meij=OLDlist.getDouble("meij");
//					if(rez>0 && meij==0){
//						meij=list.getDouble("meij");
//					}
//					如果运价为0，则使用系统取值进行覆盖操作，否则数据将保持不变
					double yunj=OLDlist.getDouble("yunj");
//					if(rez>0 && yunj==0){
//						yunj=list.getDouble("yunj");
//					}
					
					double meijs=OLDlist.getDouble("meijs");
					double yunjs=OLDlist.getDouble("yunjs");
					double huocyj=OLDlist.getDouble("huocyj");
					double huocyjs=OLDlist.getDouble("huocyjs");
					double qicyj=OLDlist.getDouble("qicyj");
					double qicyjs=OLDlist.getDouble("qicyjs");
					
//					如果热值为0那么煤价和运价及其对应的税金均为0
					if(rez==0){
						yunj=0;
						meij=0;
						meijs=0;
						yunjs=0;
						huocyj=0;
						huocyjs=0;
						qicyj=0;
						qicyjs=0;
					}
					
					if(list.getString("meikxxb_id").equals(OLDlist.getString("meikxxb_id")) && list.getString("pinzb_id").equals(OLDlist.getString("pinzb_id")) 
							&& list.getString("gongysb_id").equals(OLDlist.getString("gongysb_id")) && list.getString("JIHKJB_ID").equals(OLDlist.getString("JIHKJB_ID"))
							&& list.getString("YUNSFSB_ID").equals(OLDlist.getString("YUNSFSB_ID"))){
						insertSql.append(
								"insert into shouhcfkb\n" +
								"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meijs,yunjs,huocyj,huocyjs,qicyj,qicyjs,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
								"values\n" + 
								"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
								laimsl+","+rez+","+meij+","+yunj+","
								+meijs+","+yunjs+","+huocyj+","+huocyjs+","+qicyj+","+qicyjs+","
								+list.getString("meikxxb_id")+","+
								list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
						isTrue=true;
						break;
					}
				}
				if(!isTrue){
					insertSql.append(
							"insert into shouhcfkb\n" +
							"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,YUNSFSB_ID)\n" + 
							"values\n" + 
							"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
							list.getDouble("laimsl")+","+list.getDouble("rez")+","+
							list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
							list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
				}
				isTrue=false;
				OLDlist.beforefirst();
			}
			insertSql.append("end;");
		}else{
			while (list.next()) {
				insertSql.append(
						"insert into shouhcfkb\n" +
						"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,YUNSFSB_ID)\n" + 
						"values\n" + 
						"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
						list.getDouble("laimsl")+","+list.getDouble("rez")+","+
						list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
						list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
			}
			insertSql.append("end;");
		}

		int re=0;

//		删除数据
		String delData="delete from shouhcfkb where diancxxb_id="+dcid+" and riq = "+CurOraDate;
		re=con.getDelete(delData);
		if(re==-1){
			OLDlist.close();
			list.close();
			return "分矿数据删除失败！";
		}
//		更新数据
		if(insertSql.length()>20){
			re=con.getInsert(insertSql.toString());
			if(re==-1){
				OLDlist.close();
				list.close();
				return "分矿数据生成失败！";
			}
		}
		OLDlist.close();
		list.close();
		return "";
	}
	
//	获取热值并更新
	public String GetRez(JDBCcon con, long dcid, Date riq)  {
		ResultSetList list=getHDShouhcfk(con, dcid,riq);
		ResultSetList OLDlist=getOLDHDShouhcfk(con, dcid,riq);
		StringBuffer insertSql=new StringBuffer();

		insertSql.append("begin \n");
//		判断是否存在原始记录
		if(OLDlist.getRows()>0){
			while (list.next()) {
				while(OLDlist.next()){
					double rez=list.getDouble("REZ");

//					如果煤价为0，则使用系统取值进行覆盖操作，否则数据将保持不变
					double meij=OLDlist.getDouble("meij");
//					if(rez>0 && meij==0){
//						meij=list.getDouble("meij");
//					}
//					如果运价为0，则使用系统取值进行覆盖操作，否则数据将保持不变
					double yunj=OLDlist.getDouble("yunj");
//					if(rez>0 && yunj==0){
//						yunj=list.getDouble("yunj");
//					}
					
					double meijs=OLDlist.getDouble("meijs");
					double yunjs=OLDlist.getDouble("yunjs");
					double huocyj=OLDlist.getDouble("huocyj");
					double huocyjs=OLDlist.getDouble("huocyjs");
					double qicyj=OLDlist.getDouble("qicyj");
					double qicyjs=OLDlist.getDouble("qicyjs");
					
//					如果热值为0那么煤价和运价及其对应的税金均为0
					if(rez==0){
						yunj=0;
						meij=0;
						meijs=0;
						yunjs=0;
						huocyj=0;
						huocyjs=0;
						qicyj=0;
						qicyjs=0;
					}
					
					if(list.getString("meikxxb_id").equals(OLDlist.getString("meikxxb_id")) && list.getString("pinzb_id").equals(OLDlist.getString("pinzb_id")) 
							&& list.getString("gongysb_id").equals(OLDlist.getString("gongysb_id")) && list.getString("JIHKJB_ID").equals(OLDlist.getString("JIHKJB_ID"))
							&& list.getString("YUNSFSB_ID").equals(OLDlist.getString("YUNSFSB_ID"))){
						
						insertSql.append("update shouhcfkb set rez="+rez+",\n" +
								" meij="+meij+","+
								" meijs="+meijs+","+
								" yunjs="+yunjs+","+
								" huocyj="+huocyj+","+
								" huocyjs="+huocyjs+","+
								" qicyj="+qicyj+","+
								" qicyjs="+qicyjs+","+
								" yunj="+yunj+" where id="+OLDlist.getString("ID")+";\n");
						break;
					}
				}			
				OLDlist.beforefirst();
			}
			insertSql.append("end;");
		}
		
//		更新数据
		if(insertSql.length()>20){
			int	re=con.getUpdate(insertSql.toString());
			if(re==-1){
				OLDlist.close();
				list.close();
				return "分矿热值获取失败！";
			}
		}
		OLDlist.close();
		list.close();
		return "";
	}	
	
//	得到原始信息
	private ResultSetList getOLDHDShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		StringBuffer sql=new StringBuffer();
		sql.append(
				"SELECT SHC.ID,\n" +
				"		SHC.DIANCXXB_ID,\n" +
				"       SHC.REZ,\n" + 
				"       SHC.MEIJ,\n" + 
				"       SHC.YUNJ,\n" + 
				"       SHC.MEIJS,\n" + 
				"       SHC.YUNJS,\n" + 
				"       SHC.HUOCYJ,\n" + 
				"       SHC.HUOCYJS,\n" + 
				"       SHC.QICYJ,\n" + 
				"       SHC.QICYJS,\n" + 
				"       SHC.LAIMSL,\n" + 
				"       SHC.MEIKXXB_ID,\n" + 
				"       SHC.GONGYSB_ID,\n" + 
				"       SHC.JIHKJB_ID,\n" + 
				"       SHC.yunsfsb_id,\n" + 
				"       SHC.PINZB_ID\n" + 
				"  FROM SHOUHCFKB SHC, DIANCXXB DC\n" + 
				" WHERE SHC.RIQ ="+DateUtil.FormatOracleDate(riq)+"\n" + 
				"   AND SHC.DIANCXXB_ID = DC.ID"+
				"   and dc.id = "+diancxxb_id+" \n" );
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
	private ResultSetList getHDShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		String roundFuncName = "sum(round(";
		String roundSacle = ",2))";
		String whereCondition="";
		if(MainGlobal.getXitxx_item("收耗存日报", "来煤是否区分厂别", "0", "是").equals("是")){
			whereCondition+=" and diancxxb_id="+diancxxb_id;
		}
		StringBuffer sql=new StringBuffer();
		sql.append(
				"select meikxxb_id,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,diancxxb_id,rez,\n" +
//				"		decode(rez,0,0,meij) meij,\n" +
//				"		decode(rez,0,0,yunj) yunj,\n" +
				"		0 meij,\n" +
				"		0 yunj,\n" +
				"		laimsl from(\n" +
				"		select f.meikxxb_id,\n" +
				"       f.gongysb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id,\n" + 
				"		f.diancxxb_id,\n"+
				"       decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)),0,0,round_new(sum(z.qnet_ar * f.laimsl) / sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)), 2)) rez,\n" + 
				"       decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum((g.meij + g.meis) * f.laimsl) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)), 2)) meij,\n" + 
				//"       decode(sum(f.laimsl),0,0,round_new(sum((g.yunf + g.yunfs) * f.laimsl) / sum(f.laimsl),2)) yunj,\n" + 
				"		decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum(f.laimsl * decode(g.meij,0,0,(nvl(g.yunf,0) + nvl(g.yunfs,0)))) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)),2)) yunj,\n" + 
				"       nvl("+roundFuncName+"f.laimsl"+roundSacle+", 0) laimsl\n" + 
				"  from fahb f,\n" + 
				"       zhilb z,diancxxb d,\n" + 
				"       (select id, fahb_id, meij, meis, yunf, yunfs\n" + 
				"          from guslsb\n" + 
				"         where id in (select max(id) id\n" + 
				"                        from guslsb g,\n" + 
				"                             (select fahb_id, max(leix) leix\n" + 
				"                                from guslsb\n" + 
				"                               group by fahb_id) g2\n" + 
				"                       where g.fahb_id = g2.fahb_id\n" + 
				"                         and g.leix = g2.leix\n" + 
				"                       group by g.fahb_id)) g\n" + 
				" where f.zhilb_id = z.id(+)\n" + 
				"   and f.id = g.fahb_id(+)\n" + 
				"   and f.diancxxb_id = d.id \n"+
				whereCondition+"\n" + 
				"   and f.daohrq = "+DateUtil.FormatOracleDate(riq)+"\n" + 
				" group by f.diancxxb_id,f.gongysb_id, f.meikxxb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id)");
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
//	取值结束
	
//	检查日估价分矿表
	public String ChkFKB(JDBCcon con, String dcid, Date riq){
		String CurOraDate = DateUtil.FormatOracleDate(riq); // 当前时间
		String sql="SELECT TO_CHAR(SHC.RIQ,'yyyy-mm-dd')RIQ\n" +
			"  FROM SHOUHCFKB SHC\n" + 
			" WHERE SHC.RIQ BETWEEN FIRST_DAY("+CurOraDate+") AND "+CurOraDate+"-1\n" + 
			"   AND SHC.DIANCXXB_ID = "+dcid+"\n" + 
			"   AND (SHC.REZ = 0 OR (SHC.MEIJ = 0 AND SHC.YUNJ=0))\n" + 
			" GROUP BY SHC.RIQ\n" + 
			" ORDER BY SHC.RIQ";
		String InvalidDay="";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			InvalidDay = rsl.getString("RIQ");
		}
		rsl.close();
		return InvalidDay;
	}
	
	
//	检查收耗存日报表
	public String ChkRBB(JDBCcon con, String dcid, Date riq){
		String CurOraDate = DateUtil.FormatOracleDate(riq); // 当前时间
		String sql="SELECT TO_CHAR(BB.RIQ, 'yyyy-mm-dd') RIQ\n" +
			"  FROM SHOUHCRBB BB\n" + 
			" WHERE BB.RIQ BETWEEN FIRST_DAY("+CurOraDate+") AND "+CurOraDate+"-1\n" + 
			"   AND BB.DIANCXXB_ID = "+dcid+"\n" + 
			"   AND BB.FADY + BB.GONGRY + BB.QITY + BB.FEISCY = 0\n" + 
			" GROUP BY BB.RIQ\n" + 
			" ORDER BY BB.RIQ";

		String InvalidDay="";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			InvalidDay = rsl.getString("RIQ");
		}
		rsl.close();
		return InvalidDay;
	}
	
//	检查收耗存日报表和日估价报表
	public String RPChk(JDBCcon con, String dcid, Date riq1, Date riq2){
		String BeginOraDate = DateUtil.FormatOracleDate(riq1); // 当前时间
		String EndOraDate = DateUtil.FormatOracleDate(riq2); // 当前时间
		String sql=
			"SELECT TH.DCMC, TH.RIQ, NVL(SJ.RBZHI, -1), NVL(SJ.GM,-1) GM, NVL(SJ.SHCZHI, -1),\n" +
			"DECODE(NVL(SJ.RBZHI, 0),0,0,DECODE(NVL(SJ.SHCZHI, 0),0,DECODE(NVL(SJ.GM,0),0,1,0),1))ck\n" + 
			"  FROM (SELECT DC.DCMC, RIQ.RIQ\n" + 
			"          FROM (SELECT TO_CHAR("+BeginOraDate+" + ROWNUM - 1, 'yyyy-mm-dd') RIQ\n" + 
			"                  FROM USER_OBJECTS\n" + 
			"                 WHERE ROWNUM <= "+EndOraDate+" - "+BeginOraDate+"+1) RIQ,\n" + 
			"               (SELECT MINGC DCMC FROM DIANCXXB WHERE ID IN ("+dcid+")AND ID NOT IN(100,112,300)) DC) TH,\n" + 
			"       (SELECT RB.DCMC, RB.RIQ, RB.ZHI RBZHI, RB.GM, SHC.ZHI SHCZHI\n" + 
			"          FROM (SELECT DCMC, RIQ, MIN(ZHI) ZHI\n" + 
			"                  FROM (SELECT DC.MINGC DCMC,\n" + 
			"                               TO_CHAR(SHC.RIQ, 'yyyy-mm-dd') RIQ,\n" + 
			"                               CASE\n" + 
			"                                 WHEN (SHC.REZ = 0 OR (SHC.MEIJ = 0 AND SHC.YUNJ=0)) THEN\n" + 
			"                                  0\n" + 
			"                                 ELSE\n" + 
			"                                  1\n" + 
			"                               END ZHI\n" + 
			"                          FROM SHOUHCFKB SHC, DIANCXXB DC\n" + 
			"                         WHERE SHC.RIQ BETWEEN "+BeginOraDate+" AND "+EndOraDate+"\n" + 
			"                           AND DC.ID = SHC.DIANCXXB_ID\n" + 
			"                           AND DC.ID IN ("+dcid+"))\n" + 
			"                 GROUP BY DCMC, RIQ) SHC,\n" + 
			"               (SELECT DC.MINGC DCMC,\n" + 
			"                       TO_CHAR(SHC.RIQ, 'yyyy-mm-dd') RIQ,\n" + 
			"                       DECODE(SHC.FADY + SHC.GONGRY + SHC.QITY + SHC.FEISCY, 0, 0, 1) ZHI,\n" + 
			"                       DECODE(SHC.DANGRGM, 0, 0, 1) GM\n" + 
			"                  FROM SHOUHCRBB SHC, DIANCXXB DC\n" + 
			"                 WHERE SHC.RIQ BETWEEN "+BeginOraDate+" AND "+EndOraDate+"\n" + 
			"                   AND DC.ID = SHC.DIANCXXB_ID\n" + 
			"                   AND DC.ID IN ("+dcid+")) RB\n" + 
			"         WHERE RB.DCMC = SHC.DCMC(+)\n" + 
			"           AND RB.RIQ = SHC.RIQ(+)) SJ\n" + 
			" WHERE TH.DCMC = SJ.DCMC(+)\n" + 
			"   AND TH.RIQ = SJ.RIQ(+)\n" + 
			"AND DECODE(NVL(SJ.RBZHI, 0),0,0,DECODE(NVL(SJ.SHCZHI, 0),0,DECODE(NVL(SJ.GM,0),0,1,0),1))=0\n"+
			" ORDER BY TH.DCMC, TH.RIQ";

		String InvalidDay="";
		ResultSetList rsl = con.getResultSetList(sql);
		int i=1;
		while(rsl.next()) {
			if(i%3==0 ){
				InvalidDay += rsl.getString("DCMC")+"："+rsl.getString("RIQ")+"日,<br>";
			}else{
				InvalidDay += rsl.getString("DCMC")+"："+rsl.getString("RIQ")+"日, ";
			}
			i++;
		}
		rsl.close();
		return InvalidDay;
	}
	
}
